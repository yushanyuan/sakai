/**   
 *   
 * 版本信息：   
 * Copyright 思开科技    
 * 版权所有   
 */
package org.sakaiproject.cmsrest.controller;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.sakaiproject.cmsrest.logic.ConfigLogic;
import org.sakaiproject.cmsrest.vo.SectionVO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.coursemanagement.api.AcademicSession;
import org.sakaiproject.coursemanagement.api.CanonicalCourse;
import org.sakaiproject.coursemanagement.api.CourseManagementAdministration;
import org.sakaiproject.coursemanagement.api.CourseManagementService;
import org.sakaiproject.coursemanagement.api.CourseOffering;
import org.sakaiproject.coursemanagement.api.CourseSet;
import org.sakaiproject.coursemanagement.api.Section;
import org.sakaiproject.tool.api.SessionManager;
import org.sakaiproject.user.api.UserDirectoryService;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
/**
 * 对应/WEF-INF/jsp/下的页面。提供在页面上录入学期、学院、课程
 * @Description: TODO
 * @author: derek lee
 *
 */
@Controller
@RequestMapping("/cmsAdmin")
public class CmsAdminController {

	
	private static Log logger = LogFactory.getLog(CmsAdminController.class);
	
	@Resource(name="org.sakaiproject.user.api.UserDirectoryService")
	private UserDirectoryService userDirectoryService;

	@Resource(name="org.sakaiproject.coursemanagement.api.CourseManagementAdministration")
	private CourseManagementAdministration courseManagementAdministration = null;
	
	
	@Resource(name="org.sakaiproject.coursemanagement.api.CourseManagementService")
	private CourseManagementService cmService;
	 
	@Resource(name="org.sakaiproject.tool.api.SessionManager")
	private SessionManager sessionManager;
	

	@Resource
	private ConfigLogic configLogic;
	
	@InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }
	//--------------------------------------------
	
	@RequestMapping(value="/index")
	public String index(Model model) {
		
		//学期List
		List<AcademicSession> academis = cmService.getAcademicSessions();
		model.addAttribute("academis", academis);
		
		//学院list
		Set<CourseSet> courseSets = cmService.getCourseSets();
		model.addAttribute("courseSets", courseSets);
      
        return "index";
    }
	
	/**
	 * 添加学期
	 * @return
	 */
	@RequestMapping(value="/academAdd")
	public String academAdd(Model model){
		
		return "academInput";
	}
	
	/**
	 * 保存学期
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/academSave")
	public String academSave(Model model, @RequestParam("eid") String eid,
			@RequestParam("academName") String academName,
			 @RequestParam("startDate") Date startDate,
			 @RequestParam("endDate") Date endDate){
		
		courseManagementAdministration.createAcademicSession(eid, academName, academName, startDate, endDate);
		
		
		List<AcademicSession> academis = cmService.getAcademicSessions();
		List<String> acdList = new ArrayList<String>();
		if(academis!=null && academis.size()>0){
			for(AcademicSession s : academis){
				acdList.add(s.getEid());
			}
		}
		courseManagementAdministration.setCurrentAcademicSessions(acdList);
		
		return index(model);
	}
	
	/**
	 * 删除学期
	 * @param model
	 * @param eid
	 * @return
	 */
	@RequestMapping(value="/academDelete")
	public String academDelete(Model model, @RequestParam("eid") String eid){
		try{
			courseManagementAdministration.removeAcademicSession(eid);
		}catch(Exception e){
			e.printStackTrace();
			logger.warn("学期删除失败！", e);
			model.addAttribute("alertMesage", "学期正在被使用，请先删除课程！");
		}
		return index(model);
	} 
	
	/**
	 * 创建学院
	 * @param model
	 * @param courseSetEid
	 * @param courseSetName
	 * @return
	 */
	@RequestMapping(value="/courseSetInput")
	public String courseSetInput(Model model){
		
		
		return "courseSetInput";
	} 
	
	/**
	 * 保存学院
	 * @param model
	 * @param courseSetEid
	 * @param courseSetName
	 * @return
	 */
	@RequestMapping(value="/courseSetSave")
	public String courseSetSave(Model model, @RequestParam("courseSetEid") String courseSetEid,
			@RequestParam("courseSetName") String courseSetName ){
		
		courseManagementAdministration.createCourseSet(courseSetEid, courseSetName, courseSetName, "", null);
		
		return index(model);
	} 
	
	/**
	 * 删除学院
	 * @param model
	 * @param eid
	 * @return
	 */
	@RequestMapping(value="/courseSetDelete")
	public String courseSetDelete(Model model, @RequestParam("eid") String eid){
		try{
			Set<CanonicalCourse> canCourseSet = cmService.getCanonicalCourses(eid);
			if(canCourseSet!=null && canCourseSet.size()>0){
				logger.warn("学院删除失败");
				model.addAttribute("alertMesage", "学院正在被使用，请先删除课程！");
			}else{
				courseManagementAdministration.removeCourseSet(eid);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			logger.warn("学院删除失败", e);
			model.addAttribute("alertMesage", "学院正在被使用，请先删除课程！");
		}
		return index(model);
	}
 
	/**
	 * 课程列表
	 * @param model
	 * @param courseSetEid
	 * @return
	 */
	@RequestMapping(value="/canonicalCourseList")
	public String canonicalCourseList(Model model, @RequestParam("courseSetEid") String courseSetEid ){
		
		
		Set<CanonicalCourse> canCourseSet = cmService.getCanonicalCourses(courseSetEid);
		model.addAttribute("canonicalCourseList", canCourseSet);
		model.addAttribute("courseSetEid", courseSetEid);
		
		return "canonicalCourseList";
	} 
	
	/**
	 * 创建课程
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/canonicalCourseInput")
	public String canonicalCourseInput(Model model){

		//学院list
		Set<CourseSet> courseSets = cmService.getCourseSets();
		model.addAttribute("courseSets", courseSets);
		
		return "canonicalCourseInput";
	} 
	
	/**
	 * 保存课程
	 * @param model
	 * @param courseSetEid
	 * @param eid
	 * @param courseName
	 * @return
	 */
	@RequestMapping(value="/canonicalCourseSave")
	public String canonicalCourseSave(Model model, @RequestParam("courseEid") String courseEid,
			@RequestParam("courseName") String courseName,
			@RequestParam("courseSetEid") String courseSetEid ){
		
		courseManagementAdministration.createCanonicalCourse(courseEid, courseName, courseName);
		courseManagementAdministration.addCanonicalCourseToCourseSet(courseSetEid, courseEid);
		
		return canonicalCourseList(model, courseSetEid);
	} 
	
	/**
	 * 删除课程
	 * @param model
	 * @param courseEid
	 * @param courseSetEid
	 * @return
	 */
	
	@RequestMapping(value="/canonicalCourseDelete")
	public String canonicalCourseDelete(Model model, @RequestParam("courseEid") String courseEid,
				@RequestParam("courseSetEid") String courseSetEid){
		
		Set<CourseOffering> courseOfferingSet = cmService.getCourseOfferingsInCanonicalCourse(courseEid);
		if(courseOfferingSet!=null && courseOfferingSet.size()>0){
			logger.warn("课程删除失败");
			model.addAttribute("alertMesage", "课程正在被使用，请先删除课程实例！");
		}else{
			courseManagementAdministration.removeCanonicalCourseFromCourseSet(courseSetEid, courseEid);
			courseManagementAdministration.removeCanonicalCourse(courseEid);
		}
		
		return canonicalCourseList(model, courseSetEid);
	} 
	
	/**
	 * 课程实例列表
	 * @param model
	 * @param courseSetEid
	 * @return
	 */
	@RequestMapping(value="/sectionList")
	public String sectionList(Model model, @RequestParam("canonicalCourseEid") String canonicalCourseEid, 
			@RequestParam("courseSetEid") String courseSetEid){
		
		//AcademicSession academicSession = cmService.getAcademicSession(academicSessionEid);
		//CourseSet courseSet = cmService.getCourseSet(courseSetEid);
		
		Set<SectionVO> sectionVOs = new HashSet<SectionVO>(); 
		Set<CourseOffering> courseOfferingSet = cmService.getCourseOfferingsInCanonicalCourse(canonicalCourseEid);
		//Set<CourseOffering> courseOfferingSet = cmService.findCourseOfferings(courseSetEid, academicSessionEid);
		if(courseOfferingSet != null && courseOfferingSet.size()>0){
			for(CourseOffering offer : courseOfferingSet){
				 
				Set<Section> sections = cmService.getSections(offer.getEid());
				if(sections!=null && sections.size()>0){
					for(Section sc : sections){
						SectionVO vo = new SectionVO();
						vo.setEid(sc.getEid());
						vo.setTitle(sc.getTitle());
						vo.setCourseOfferingEid(offer.getEid());
						vo.setCourseOfferingTitle(offer.getTitle());
						CanonicalCourse canonicalCourse = cmService.getCanonicalCourse(offer.getCanonicalCourseEid());
						if(canonicalCourse!=null){
							vo.setCanonicalCourseEid(canonicalCourse.getEid());
							vo.setCanonicalCourseTitle(canonicalCourse.getTitle());
						}
						if(offer.getCourseSetEids()!=null && offer.getCourseSetEids().size()>0){
							CourseSet courseSet = cmService.getCourseSet(offer.getCourseSetEids().iterator().next());
							if(courseSet!=null){
								vo.setCourseSetEid(courseSet.getEid());
								vo.setCourseSetTitle(courseSet.getTitle());
							}
						}
							
						vo.setAcademicSessionEid(offer.getAcademicSession().getEid());
						vo.setAcademicSessionTitle(offer.getAcademicSession().getTitle());
						sectionVOs.add(vo);
					}
				}
			}
		}
		
 		model.addAttribute("sectionSet", sectionVOs);
 		
 		model.addAttribute("courseSetEid", courseSetEid);
 		model.addAttribute("canonicalCourseEid", canonicalCourseEid);
 		//model.addAttribute("academicSession", academicSession);
 		
		return "sectionList";
	} 
	
	@RequestMapping(value="/sectionInput")
	public String sectionInput(Model model, @RequestParam("courseSetEid") String courseSetEid){
		
		Set<CanonicalCourse> canonicalCourseList = cmService.getCanonicalCourses(courseSetEid);
		
		model.addAttribute("academicSessions", cmService.getAcademicSessions());
		model.addAttribute("courseSetEid", courseSetEid);
		model.addAttribute("courseSetTitle", cmService.getCourseSet(courseSetEid).getTitle());
		model.addAttribute("canonicalCourseList", canonicalCourseList);
		return "sectionInput";
	}
	
	@RequestMapping(value="/sectionSave")
	public String sectionSave(Model model, @RequestParam("courseSetEid") String courseSetEid, 
			@RequestParam("academicSessionEid") String academicSessionEid,
			@RequestParam("canonicalCourseEid") String canonicalCourseEid,
			@RequestParam("courseOfferingEid") String courseOfferingEid,
			@RequestParam("courseOfferingName") String courseOfferingName)	{
		try{
			AcademicSession academicSession = cmService.getAcademicSession(academicSessionEid);
			courseManagementAdministration.createCourseOffering(courseOfferingEid, courseOfferingName, courseOfferingName, "open", academicSessionEid, canonicalCourseEid, academicSession.getStartDate(), academicSession.getEndDate());
			courseManagementAdministration.addCourseOfferingToCourseSet(courseSetEid, courseOfferingEid);
			
			courseManagementAdministration.createSection(courseOfferingEid, courseOfferingName, courseOfferingName, "", null, courseOfferingEid, null);	
		}catch(Exception e){
			logger.warn("课程实例创建失败");
			model.addAttribute("alertMesage", "课程实例"+courseOfferingEid+"已经存在！");
		}
		
		return sectionList(model, canonicalCourseEid, courseSetEid);
	}
	
	/**
	 * 删除课程实例
	 * @param model
	 * @param courseSetEid
	 * @param canonicalCourseEid
	 * @return
	 */
	@RequestMapping(value="/sectionDelete")
	public String sectionDelete(Model model, @RequestParam("courseSetEid") String courseSetEid, 
									@RequestParam("canonicalCourseEid") String canonicalCourseEid,
									@RequestParam("sectionEid") String sectionEid){
		
		
		Set<CourseOffering> courseOfferingSet = cmService.getCourseOfferingsInCanonicalCourse(canonicalCourseEid);
		if(courseOfferingSet != null && courseOfferingSet.size()>0){
			for(CourseOffering offer : courseOfferingSet){
				boolean flag = false;
				Set<Section> sections = cmService.getSections(offer.getEid());
				if(sections!=null && sections.size()>0){
					for(Section sc : sections){
						if(sc.getEid().equals(sectionEid)){
							courseManagementAdministration.removeSection(sc.getEid());
							flag = true;
						}
					}
				}
				if(flag){
					courseManagementAdministration.removeCourseOfferingFromCourseSet(courseSetEid, offer.getEid());
					courseManagementAdministration.removeCourseOffering(offer.getEid());
				}
				
			}
		}
		
		return sectionList(model, canonicalCourseEid, courseSetEid);
	}

}
