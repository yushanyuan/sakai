/**
 * 
 */
package org.sakaiproject.cmsrest.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.cmsrest.logic.AutoNewCourseSite;
import org.sakaiproject.cmsrest.utils.PropertiesUtil;
import org.sakaiproject.coursemanagement.api.AcademicSession;
import org.sakaiproject.coursemanagement.api.CourseManagementAdministration;
import org.sakaiproject.coursemanagement.api.CourseManagementService;
import org.sakaiproject.coursemanagement.api.CourseOffering;
import org.sakaiproject.coursemanagement.api.CourseSet;
import org.sakaiproject.coursemanagement.api.Section;
import org.sakaiproject.tool.api.SessionManager;
import org.sakaiproject.user.api.UserDirectoryService;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 自动建站
 * @author yushanyuan
 *
 */
@Controller
@RequestMapping("/autoCm")
public class AutoCmController {

	private static Log logger = LogFactory.getLog(AutoCmController.class);
	
	
	@Resource(name="org.sakaiproject.user.api.UserDirectoryService")
	private UserDirectoryService userDirectoryService;

	@Resource(name="org.sakaiproject.coursemanagement.api.CourseManagementAdministration")
	private CourseManagementAdministration courseManagementAdministration = null;
	
	@Resource(name="org.sakaiproject.coursemanagement.api.CourseManagementService")
	private CourseManagementService cmService;
	 
	@Resource(name="org.sakaiproject.tool.api.SessionManager")
	private SessionManager sessionManager;
	
	@Resource
	private AutoNewCourseSite autoNewCourseSite;
	
	
	@InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }
	//--------------------------------------------
	

	
	
	@RequestMapping(value = "/createSite", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String createSite() {
		
		logger.info("---createSite---");
		
		StringBuffer errorStr = new StringBuffer();
		errorStr.append(" failed section eid : ");
		
		Set<CourseSet> courseSets = cmService.getCourseSets();
		if(!(courseSets!=null && courseSets.size()>0)){
			return "{\"error\":\"没有建立学院！ \"}"; 
		}
		
		List<AcademicSession> academicSessions = cmService.getCurrentAcademicSessions();
		if(!(academicSessions != null && academicSessions.size()>0)){
			return "{\"error\":\"没有当前学期!\"}"; 
		}
		
		//读取课程站点最初的工具集
		List<String> toolIds = new ArrayList<String>();
		String tools = PropertiesUtil.getTools();
		String[] toolArray = tools.split(",");
		for(String tool : toolArray){
			toolIds.add(tool);
		}
		
		
		for(AcademicSession session : academicSessions){
			for(CourseSet courseSet : courseSets){
				Set<CourseOffering> courseOfferings = cmService.findCourseOfferings(courseSet.getEid(), session.getEid());
				if(courseOfferings!=null && !courseOfferings.isEmpty()){
					for(CourseOffering courseOffering : courseOfferings){
						Set<Section> sections = cmService.getSections(courseOffering.getEid());
						if(sections!=null && !sections.isEmpty()){
							for(Section section : sections){
								//判断课程站点是否生成
								
								
								//生成站点
								try {
									autoNewCourseSite.newSite(session.getEid(), session.getTitle(), toolIds, section.getEid());
								} catch (Exception e) {
									logger.error("cms error: AcademicSession Eid: "+session.getEid()+" Section Eid : "+section.getEid()+" fail to create course site !");
									errorStr.append(section.getEid()+", ");
									e.printStackTrace();
								}
								
							}
						}
					}
				}
			}
			
		}
		
		return "{\"error\":\""+errorStr.toString()+"\"}";
	}
}
