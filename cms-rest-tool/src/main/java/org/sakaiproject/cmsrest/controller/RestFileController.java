/**
 * 版权所有 北京思开科技有限公司 
 * All Rights Reserved
 */
package org.sakaiproject.cmsrest.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.cmsrest.logic.AutoNewCourseSite;
import org.sakaiproject.cmsrest.logic.CmDao;
import org.sakaiproject.cmsrest.logic.SiteManage;
import org.sakaiproject.cmsrest.utils.PropertiesUtil;
import org.sakaiproject.cmsrest.utils.UUIDUtil;
import org.sakaiproject.coursemanagement.api.AcademicSession;
import org.sakaiproject.coursemanagement.api.CanonicalCourse;
import org.sakaiproject.coursemanagement.api.CourseManagementAdministration;
import org.sakaiproject.coursemanagement.api.CourseManagementService;
import org.sakaiproject.coursemanagement.api.CourseOffering;
import org.sakaiproject.coursemanagement.api.CourseSet;
import org.sakaiproject.coursemanagement.api.Section;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.user.api.User;
import org.sakaiproject.user.api.UserDirectoryService;
import org.sakaiproject.user.api.UserNotDefinedException;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * 通过在页面导入课程，用户，选课信息。完成教务集成
 * @Description: TODO
 * @author: derek lee
 *
 */
@Controller
@RequestMapping("/cmsFile")
public class RestFileController {
	
	private static Log logger = LogFactory.getLog(RestFileController.class);
	
	@Resource(name="org.sakaiproject.user.api.UserDirectoryService")
	private UserDirectoryService userDirectoryService;
	
	@Resource(name="org.sakaiproject.site.api.SiteService")
	private SiteService siteService;
	
	@Resource(name="org.sakaiproject.coursemanagement.api.CourseManagementAdministration")
	private CourseManagementAdministration courseManagementAdministration = null;
	
	@Resource(name="org.sakaiproject.coursemanagement.api.CourseManagementService")
	private CourseManagementService cmService;
	 
	@Resource(name="org.sakaiproject.cmsrest.logic.AutoNewCourseSite")
	private AutoNewCourseSite autoNewCourseSite;
	
	@Resource(name="org.sakaiproject.cmsrest.logic.SiteManage")
	private SiteManage siteManage;
	
	@Resource(name="messageSource")
	private MessageSource messageSource;
	
	@Resource(name="org.sakaiproject.cmsrest.logic.CmDao")
	private CmDao cmDao;
	
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
	public String academSave(Model model, @RequestParam("academName") String academName,
			 @RequestParam("startDate") Date startDate,
			 @RequestParam("endDate") Date endDate){
		
		String eid = UUIDUtil.getUUID();
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
			Locale locale = LocaleContextHolder.getLocale();
			
			model.addAttribute("alertMesage", messageSource.getMessage("msg_remove_academic_error", null, locale));
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
	public String courseSetSave(Model model, @RequestParam("courseSetName") String courseSetName ){
		String courseSetEid = UUIDUtil.getUUID();
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
				Locale locale = LocaleContextHolder.getLocale();
				model.addAttribute("alertMesage", messageSource.getMessage("msg_remove_college_error", null, locale));
			}else{
				courseManagementAdministration.removeCourseSet(eid);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			logger.warn("学院删除失败", e);
			Locale locale = LocaleContextHolder.getLocale();
			model.addAttribute("alertMesage", messageSource.getMessage("msg_remove_college_error", null, locale));
		}
		return index(model);
	}
 
	/**
	 * 课程站点列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/courseList")
	public String courseList(Model model, @RequestParam("courseSetEid") String courseSetEid) {
		
		Set<CanonicalCourse> canCourseSet = cmService.getCanonicalCourses(courseSetEid);
		model.addAttribute("canonicalCourseList", canCourseSet);
		model.addAttribute("courseSetEid", courseSetEid);
		
        return "courseList";
    }
	
	/**
	 * 进入上传开课列表页面
	 * @param model
	 * @param sessionId
	 * @return
	 */
	@RequestMapping(value="/courseAdd")
	public String courseAdd(Model model, @RequestParam(value="courseSetEid") String courseSetEid){
		
		model.addAttribute("courseSetEid", courseSetEid);
		model.addAttribute("courseSetTitle", cmService.getCourseSet(courseSetEid).getTitle());
		model.addAttribute("academicSessions", cmService.getAcademicSessions());
		
		return "courseForm";
	}
	
	/**
	 * 解析上传的课程列表，生成课程站点
	 * @param model
	 * @param sessionId  学期
	 * @param fileUpload 课程列表
	 * @return
	 */
	@RequestMapping(value="/courseSave")
	public String courseSave(Model model, 
			@RequestParam("academicSessionEid") String academicSessionEid,
			@RequestParam("courseSetEid") String courseSetEid, 
			@RequestParam("fileUpload") MultipartFile fileUpload) {
		
		if (fileUpload.getOriginalFilename() != null && !fileUpload.isEmpty()) {
			//开始解析上传文件
			List<String> couserNameList = new ArrayList<String>();
			InputStreamReader isr = null;
			try {
				isr = new InputStreamReader(fileUpload.getInputStream(), "UTF-8");
				BufferedReader reader = new BufferedReader(isr);	
				
				String line;
				try {
					
                    //循环，每次读一行
					while ((line = reader.readLine()) != null) {
						couserNameList.add(line);
						
					}
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					if(reader!=null){
						reader.close();
					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}finally{
				if(isr!=null){
					try {
						isr.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}//完成解析上传文件
				
			
			//开始创建课程站点
			if(couserNameList!=null && couserNameList.size()>0){
				//读取课程站点最初的工具集
				List<String> toolIds = new ArrayList<String>();
				String tools = PropertiesUtil.getTools();
				String[] toolArray = tools.split(",");
				for(String tool : toolArray){
					toolIds.add(tool);
				}
				
				AcademicSession academicSession = cmService.getAcademicSession(academicSessionEid);
				CourseSet courseSet = cmService.getCourseSet(courseSetEid);
				
				for(String courseName : couserNameList){
					//生成站点
					try {
						String siteName = academicSession.getTitle()+courseSet.getTitle()+courseName;
						
						Site site = autoNewCourseSite.newSite(academicSession.getEid(), siteName, toolIds, null);
						//autoNewCourseSite.closeSession();
						if(site!=null && site.getId()!=null){
							autoNewCourseSite.openSession();
							 
							String courseEid = UUIDUtil.getUUID();
							courseManagementAdministration.createCanonicalCourse(courseEid, siteName, siteName);
							courseManagementAdministration.addCanonicalCourseToCourseSet(courseSetEid, courseEid);
							 
							String courseOfferingEid = site.getId();
							courseManagementAdministration.createCourseOffering(courseOfferingEid, siteName, siteName, "open", academicSessionEid, courseEid, academicSession.getStartDate(), academicSession.getEndDate());
							courseManagementAdministration.addCourseOfferingToCourseSet(courseSetEid, courseOfferingEid);
							
							courseManagementAdministration.createSection(courseOfferingEid, siteName, siteName, "", null, courseOfferingEid, null);	
						}
					} catch (Exception e) {
						logger.error("cms error: AcademicSession Eid: "+academicSession.getEid() +" Section Eid : "+ null +" fail to create course site !");
						e.printStackTrace();
						Locale locale = LocaleContextHolder.getLocale();
						model.addAttribute("alertMesage", messageSource.getMessage("msg_create_site_error", new String[]{courseName}, locale));
						//model.addAttribute("alertMesage", "创建课程站点"+courseName+"失败！");
					}
				}
			}
		}
		return courseList(model,courseSetEid);
	}
	
	/**
	 * 删除课程
	 * @param model
	 * @param courseEid
	 * @param courseSetEid
	 * @return
	 */
	@RequestMapping(value="/courseDelete")
	public String courseDelete(Model model, @RequestParam("courseEid") String courseEid,
				@RequestParam("courseSetEid") String courseSetEid){
		boolean canRemoveCourse = false;
		
		Set<CourseOffering> courseOfferingSet = cmService.getCourseOfferingsInCanonicalCourse(courseEid);
		if(courseOfferingSet!=null && courseOfferingSet.size()>0){
			
			for(CourseOffering offer : courseOfferingSet){
				boolean flag = false;
				Set<Section> sections = cmService.getSections(offer.getEid());
				if(sections!=null && sections.size()>0){
					for(Section sc : sections){
						//查询是否建有课程站点
						Site site = null;
						try {
							site = siteService.getSite(sc.getEid());
						} catch (IdUnusedException e) {
							e.printStackTrace();
						}
						if(site!=null && site.getId()!=null){
							
							Locale locale = LocaleContextHolder.getLocale();
							model.addAttribute("alertMesage", messageSource.getMessage("msg_remove_course_error", null, locale));
							
							//model.addAttribute("alertMesage", "课程正在被使用，请先删除课程站点！");
						}else{
							courseManagementAdministration.removeSection(sc.getEid());
							flag = true;
						}
					}
				}
				if(flag){
					courseManagementAdministration.removeCourseOfferingFromCourseSet(courseSetEid, offer.getEid());
					courseManagementAdministration.removeCourseOffering(offer.getEid());
					canRemoveCourse = true;
				}
				
			}
			
		} else{
			canRemoveCourse = true;
		}
		if(canRemoveCourse){
			courseManagementAdministration.removeCanonicalCourseFromCourseSet(courseSetEid, courseEid);
			courseManagementAdministration.removeCanonicalCourse(courseEid);
		}
		
		return courseList(model, courseSetEid);
	} 
	
	/**
	 * 选课学生文件上传页面
	 * @param model
	 * @param siteId
	 * @return
	 */
	@RequestMapping(value="/courseStuAdd")
	public String courseStuAdd(Model model, @RequestParam(value="courseEid") String courseEid,
											@RequestParam("courseSetEid") String courseSetEid) {
		
		Set<CourseOffering> courseOfferingSet = cmService.getCourseOfferingsInCanonicalCourse(courseEid);
		if(courseOfferingSet!=null && courseOfferingSet.size()>0){
			String courseOfferingEid = courseOfferingSet.iterator().next().getEid();
			Site site = null;
			try {
				site = siteService.getSite(courseOfferingEid);
			} catch (IdUnusedException e) {
				e.printStackTrace();
			}
			model.addAttribute("site", site);
			model.addAttribute("courseEid", courseEid);
			model.addAttribute("courseSetEid", courseSetEid);
		}
		
		return "courseStuForm";
	}
	
	/**
	 * 学生列表
	 * @param model
	 * @param courseEid
	 * @return
	 */
	@RequestMapping(value="/courseStuList")
	public String courseStuList(Model model, @RequestParam(value="courseEid") String courseEid,
												@RequestParam("courseSetEid") String courseSetEid){
		
		Set<CourseOffering> courseOfferingSet = cmService.getCourseOfferingsInCanonicalCourse(courseEid);
		if(courseOfferingSet!=null && courseOfferingSet.size()>0){
			String courseOfferingEid = courseOfferingSet.iterator().next().getEid();
			try {
				Set<String> userIds = siteManage.queryUserIdBySiteId(courseOfferingEid, "Student");
				if(userIds!=null){
					List<User> userList = new ArrayList<User>();
					Iterator it = userIds.iterator();
					while(it.hasNext()){
						String userId = (String)it.next();
						User user = null;
						try{
							user = userDirectoryService.getUser(userId);
						}catch(UserNotDefinedException e){
							e.printStackTrace();
						}
						if(user != null){
							userList.add(user);
						}
					}
					model.addAttribute("userList", userList);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		model.addAttribute("courseEid", courseEid);
		model.addAttribute("courseSetEid", courseSetEid);
		
		return "courseStuList";
	}
	/**
	 * 导入学生列表
	 * 学生列表应为 选课学生的 用户名。这些用户应该先使用用户管理的 文件导入功能先导入到系统
	 * usrename1
	 * username2
	 * 
	 * 
	 * @param model
	 * @param siteId
	 * @param courseEid
	 * @param fileUpload
	 * @return
	 */
	@RequestMapping(value="/courseStuSave")
	public String courseStuSave(Model model, 
			@RequestParam("siteId") String siteId, 
			@RequestParam(value="courseEid") String courseEid,
			@RequestParam("courseSetEid") String courseSetEid,
			@RequestParam("fileUpload") MultipartFile fileUpload) {
		
		if (fileUpload.getOriginalFilename() != null && !fileUpload.isEmpty()) {
			//开始解析上传文件
			List<String> userList = new ArrayList<String>();
			List<String> roleList = new ArrayList<String>();
			InputStreamReader isr = null;
			try {
				isr = new InputStreamReader(fileUpload.getInputStream(), "UTF-8");
				BufferedReader reader = new BufferedReader(isr);	
				
				String line;
				try {
					
                    //循环，每次读一行
					while ((line = reader.readLine()) != null) {
						userList.add(line);
						roleList.add("Student");
					}
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					if(reader!=null){
						reader.close();
					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}finally{
				if(isr!=null){
					try {
						isr.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}//完成解析上传文件
				
			
			//开始为站点加入学生
			if(userList!=null && userList.size()>0){ 
				try {
					siteManage.addUsersToSite(siteId, userList, roleList);
				} catch (Exception e) {
					e.printStackTrace();
					
					
					Locale locale = LocaleContextHolder.getLocale();
					model.addAttribute("alertMesage", messageSource.getMessage("msg_import_student", null, locale));
					
					//model.addAttribute("alertMesage", "选课学生列表导入有错误！");
				}
			}
		}
		return courseStuList(model,courseEid,courseSetEid);
	}
	
	
	

	@RequestMapping("/download")    
    public ResponseEntity<byte[]> download(Model model, 
			@RequestParam("fileName") String fileNameStr) throws IOException {    
        //File file=new File(path);  
        InputStream is = RestFileController.class.getClassLoader().getResourceAsStream(fileNameStr);
        HttpHeaders headers = new HttpHeaders();    
        String fileName=new String(fileNameStr.getBytes("UTF-8"),"iso-8859-1");//为了解决中文名称乱码问题  
        headers.setContentDispositionFormData("attachment", fileName);   
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);   
        return new ResponseEntity<byte[]>(IOUtils.toByteArray(is),    
                                          headers, HttpStatus.CREATED);    
    }    

}
