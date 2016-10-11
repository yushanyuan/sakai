/**
 * 版权所有 北京思开科技有限公司 
 * All Rights Reserved
 */
package com.bysakai.mooc.rest;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.component.api.ServerConfigurationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.bysakai.mooc.api.CourseService;
import com.bysakai.mooc.api.RecItemService;
import com.bysakai.mooc.model.Course;
import com.bysakai.mooc.model.RecItem;
import com.bysakai.mooc.model.Teacher;
import com.bysakai.mooc.utils.Constant;

/**
 * @author nesdu
 *
 */
@Controller
@RequestMapping("/recItem")
public class RecItemController {

	private static Log logger = LogFactory.getLog(RecItemController.class);
	
	@Resource(name="com.bysakai.mooc.api.RecItemService")
	public RecItemService recItemService;
	@Resource(name="com.bysakai.mooc.api.CourseService")
	public CourseService courseService;
	@Resource(name="org.sakaiproject.component.api.ServerConfigurationService")
	private ServerConfigurationService serverConfigurationService;
	
	/**
	 * 推荐课程列表
	 * @return
	 */
	@RequestMapping(value = "/list")
	public String list(Model model) {
		
		String moocServer = serverConfigurationService.getString(Constant.MOOC_SERVER_URL);
		model.addAttribute("moocServer", moocServer);
		
		model.addAttribute("list", recItemService.list());
		return "recItem_list";
	}
	
	/**
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/add")
	public String add(Model model) {
		model.addAttribute("courseList", courseService.list());
		return "recItem_add";
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Model model, @RequestParam(value="courseId", required=false) String courseId, @RequestParam(value="recValue", required=false) String recValue) {
		
		RecItem recItem = new RecItem();
		recItem.setCourse(courseService.findOne(courseId));
		try{
			recItem.setRecValue(Integer.parseInt(recValue));
		}catch(Exception e){
			recItem.setRecValue(10);
		}
		recItemService.save(recItem);
		
		return list(model);
	}
	
	@RequestMapping(value = "/remove")
	public String remove(Model model, @RequestParam("id") String id) {
		
		RecItem recItem = recItemService.findOne(id);
		recItemService.remove(recItem);
		return list(model);
	}
}
