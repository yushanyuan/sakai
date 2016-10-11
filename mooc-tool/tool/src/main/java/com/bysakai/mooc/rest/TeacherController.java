/**
 * 版权所有 北京思开科技有限公司 
 * All Rights Reserved
 */
package com.bysakai.mooc.rest;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.component.api.ServerConfigurationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.bysakai.mooc.api.TeacherService;
import com.bysakai.mooc.model.Teacher;
import com.bysakai.mooc.utils.Constant;
import com.bysakai.mooc.utils.UploadUtil;


/**
 * @author llk
 *
 */
@Controller
@RequestMapping("/teacher")
public class TeacherController {

	private static Log logger = LogFactory.getLog(TeacherController.class);
	
	@Resource(name="com.bysakai.mooc.api.TeacherService")
	private TeacherService teacherService;
	@Resource(name="org.sakaiproject.component.api.ServerConfigurationService")
	private ServerConfigurationService serverConfigurationService;
	
	
	/**
	 * 老师列表
	 * @return
	 */
	@RequestMapping(value = "/list")
	public String list(Model model) {
		String moocServer = serverConfigurationService.getString(Constant.MOOC_SERVER_URL);
		model.addAttribute("moocServer", moocServer);
		
		model.addAttribute("list", teacherService.list());
		
		return "teacher_list";
	}
	/**
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/add")
	public String add(Model model) {
		
		return "teacher_add";
	}
	
	@RequestMapping(value = "/edit")
	public String edit(Model model, @RequestParam("id") String id) {
		Teacher persist = teacherService.findOne(id);
		model.addAttribute("teacher", persist);
		return "teacher_add";
	}
	
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Model model, @RequestParam(value="id", required=false) String id, @RequestParam("name") String name, @RequestParam("profile") String profile,  @RequestParam("image") MultipartFile imageFile) {
		
		try {
			Teacher persist = new Teacher();
			if(StringUtils.isNotBlank(id)){
				persist = teacherService.findOne(id);
			} 
			
			persist.setName(name);
			persist.setProfile(profile);
			
			UploadUtil uploadUtil = new UploadUtil();
			uploadUtil.setApacheServerFilePath(serverConfigurationService.getString(Constant.MOOC_FILE_PATH));
			uploadUtil.setApacheServerImagesPath(serverConfigurationService.getString(Constant.apacheServerImagesPath));
			// request.setCharacterEncoding("utf-8");

			logger.debug("表单的参数名称：" + imageFile.getOriginalFilename());
			if (imageFile.getOriginalFilename() != null && !imageFile.isEmpty()) {
				 
				String filePath = uploadUtil.uploadIcon(imageFile.getInputStream(), imageFile.getOriginalFilename(), persist.getImage());
				if (StringUtils.isNotBlank(filePath)) {
					persist.setImage(filePath); 
				}
			}
			teacherService.save(persist);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list(model);
	}
	
	
	@RequestMapping(value = "/remove")
	public String remove(Model model, @RequestParam("id") String id) {
		Teacher teacher = teacherService.findOne(id);
		teacherService.remove(teacher);
		return list(model);
	}
}
