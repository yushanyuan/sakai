/**
 * 版权所有 北京思开科技有限公司 
 * All Rights Reserved
 */
package com.bysakai.mooc.rest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.component.api.ServerConfigurationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.bysakai.mooc.api.CategoryService;
import com.bysakai.mooc.api.CourseService;
import com.bysakai.mooc.api.RecItemService;
import com.bysakai.mooc.api.TeacherService;
import com.bysakai.mooc.model.Course;
import com.bysakai.mooc.model.RecItem;
import com.bysakai.mooc.model.Teacher;
import com.bysakai.mooc.rest.vo.CourseVO;
import com.bysakai.mooc.rest.vo.RecItemVO;
import com.bysakai.mooc.utils.Constant;
import com.bysakai.mooc.utils.UploadUtil;


/**
 * @author llk
 *
 */
@Controller
@RequestMapping("/course")
public class CourseController {
	private static Log logger = LogFactory.getLog(CourseController.class);
	
	@Resource(name="com.bysakai.mooc.api.CourseService")
	public CourseService courseService;
	@Resource(name="com.bysakai.mooc.api.TeacherService")
	public TeacherService teacherService;
	@Resource(name="com.bysakai.mooc.api.CategoryService")
	public CategoryService categoryService;
	@Resource(name="com.bysakai.mooc.api.RecItemService")
	public RecItemService recItemService;
	@Resource(name="org.sakaiproject.component.api.ServerConfigurationService")
	private ServerConfigurationService serverConfigurationService;
	
	
	/**
	 * 课程列表
	 * @return
	 */
	@RequestMapping(value = "/list")
	public String list(Model model) {
		String moocServer = serverConfigurationService.getString(Constant.MOOC_SERVER_URL);
		model.addAttribute("moocServer", moocServer);
		
		model.addAttribute("list", courseService.list());
		return "course_list";
	}
	
	/**
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/add")
	public String add(Model model) {
		model.addAttribute("siteList", courseService.siteList());
		model.addAttribute("teacherList", teacherService.list());
		model.addAttribute("speList", categoryService.list());
		model.addAttribute("courseList", courseService.list()); 
		return "course_add"; 
	}
	
	
	@RequestMapping(value = "/edit")
	public String edit(Model model, @RequestParam("id") String id) {
		Course persist = courseService.findOne(id);
		model.addAttribute("course", persist);
		
		model.addAttribute("siteList", courseService.siteList());
		model.addAttribute("teacherList", teacherService.list());
		model.addAttribute("speList", categoryService.list());
		List<Course> courseList = courseService.list();
		courseList.remove(persist);
		model.addAttribute("courseList", courseService.list());
		return "course_add";
	}
	
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Model model, @RequestParam(value="id", required=false) String id, @RequestParam("name") String name,
									@RequestParam(value="desc", required=false) String desc, @RequestParam(value="syllabus", required=false) String syllabus, 
									@RequestParam(value="teacherId", required=false) String teacherId, @RequestParam(value="siteId", required=false)  String siteId,
									@RequestParam(value="categoryId", required=false) String categoryId, @RequestParam(value="qas", required=false)  String qas,
									@RequestParam(value="sakaiSiteUrl", required=false) String sakaiSiteUrl,
									@RequestParam("image") MultipartFile imageFile, @RequestParam("video") MultipartFile videoFile,
									@RequestParam(value="startDate", required=false)  String startDate,
									@RequestParam(value="endDate", required=false) String endDate,
									@RequestParam(value="assistant", required=false) String assistant,
									@RequestParam(value="certification", required=false) String certification,
									@RequestParam(value="studyTime", required=false) String studyTime,
									@RequestParam(value="studyNum", required=false) String studyNum,
									@RequestParam(value="relatedCourse", required=false) String relatedCourse) {
		
		try {
			Course persist = new Course();
			if(StringUtils.isNotBlank(id)){
				persist = courseService.findOne(id);
				persist.setRelatedCourse(null);
			} 
			
			persist.setName(name);
			
			if(StringUtils.isNotBlank(desc)){
				persist.setDesc(desc);
			}
			if(StringUtils.isNotBlank(syllabus)){
				persist.setSyllabus(syllabus);
			}
			if(StringUtils.isNotBlank(siteId)){
				persist.setSiteId(siteId);
			}
			if(StringUtils.isNotBlank(teacherId)){
				persist.setTeacher(teacherService.findOne(teacherId));
			}
			if(StringUtils.isNotBlank(categoryId)){
				persist.setCategory(categoryService.findOne(categoryId));
			}
			if(StringUtils.isNotBlank(qas)){
				persist.setQas(qas);
			}
			if(StringUtils.isNotBlank(sakaiSiteUrl)){
				persist.setSakaiSiteUrl(sakaiSiteUrl);
			}
			if(StringUtils.isNotBlank(startDate)){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date d = sdf.parse(startDate);
				persist.setStartDate(d);
			}else{
				persist.setStartDate(null);
			}
			
			if(StringUtils.isNotBlank(endDate)){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date d = sdf.parse(endDate);
				persist.setEndDate(d);
			}else{
				persist.setEndDate(null);
			}
			
			persist.setAssistant(assistant);
			persist.setCertification(certification);
			persist.setStudyTime(studyTime);
			if(StringUtils.isNotBlank(studyNum)){
				persist.setStudyNum(Integer.valueOf(studyNum));
			}else{
				persist.setStudyNum(0);
			}
			
			UploadUtil uploadUtil = new UploadUtil();
			uploadUtil.setApacheServerFilePath(serverConfigurationService.getString(Constant.MOOC_FILE_PATH));
			uploadUtil.setApacheServerImagesPath(serverConfigurationService.getString(Constant.apacheServerImagesPath));
			
			logger.debug("表单的参数名称image：" + imageFile.getOriginalFilename());
			if (imageFile.getOriginalFilename() != null && !imageFile.isEmpty()) {
				 
				String filePath = uploadUtil.uploadIcon(imageFile.getInputStream(), imageFile.getOriginalFilename(), persist.getImage());
				if (StringUtils.isNotBlank(filePath)) {
					persist.setImage(filePath); 
				}
			}
			
			logger.debug("表单的参数名称video：" + videoFile.getOriginalFilename());
			if (videoFile.getOriginalFilename() != null && !videoFile.isEmpty()) {
				 
				String filePath = uploadUtil.uploadIcon(videoFile.getInputStream(), videoFile.getOriginalFilename(), persist.getVideo());
				if (StringUtils.isNotBlank(filePath)) {
					persist.setVideo(filePath); 
				}
			}
			
			
			courseService.save(persist);
			
			if(StringUtils.isNotBlank(relatedCourse)){
				String[] courseIds = relatedCourse.split(",");
				List<Course> rcList = new ArrayList<Course>();
				Course c = null;
				for(String cid : courseIds){
					c = courseService.findOne(cid);
					rcList.add(c);
				} 
				persist.setRelatedCourse(rcList);
			} 
			
			courseService.save(persist);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list(model);
	}
	
	
	@RequestMapping(value = "/remove")
	public String remove(Model model, @RequestParam("id") String id) {
		Course course = courseService.findOne(id);
		courseService.remove(course);
		return list(model);
	}
	
	
	//-------------------------------------------------------------
	@RequestMapping(value = "/json/newlist", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, Object> newlist() {
		Map<String, Object> map = new HashMap<String, Object>();
		List<CourseVO> courseVOs = new ArrayList<CourseVO>();
		
		try{
			List<Course> courses=courseService.newList(0, 10);
			if(courses!=null && courses.size()>0){
				for(Course c : courses){
					CourseVO v = new CourseVO();
					v.setId(c.getId());
					v.setName(c.getName());
					v.setImage(serverConfigurationService.getString(Constant.MOOC_SERVER_URL) + c.getImage());
					if(c.getTeacher()!=null  && StringUtils.isNotBlank(c.getTeacher().getName())){
						Teacher t = new Teacher();
						t.setId(c.getTeacher().getId());
						t.setImage(serverConfigurationService.getString(Constant.MOOC_SERVER_URL) + c.getTeacher().getImage());
						t.setName(c.getTeacher().getName());
						t.setProfile(c.getTeacher().getProfile());
						v.setTeacher(t);
					}
					
					courseVOs.add(v);
				}
			}
			
			map.put("data", courseVOs);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return map;
	}
	
	@RequestMapping(value = "/json/hotlist", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, Object> hotlist() {
		Map<String, Object> map = new HashMap<String, Object>();
		List<CourseVO> courseVOs = new ArrayList<CourseVO>();
		
		try{
			List<Course> courses=courseService.hotList(0, 10);
			if(courses!=null && courses.size()>0){
				for(Course c : courses){
					CourseVO v = new CourseVO();
					v.setId(c.getId());
					v.setName(c.getName());
					v.setImage(serverConfigurationService.getString(Constant.MOOC_SERVER_URL) + c.getImage());
					if(c.getTeacher()!=null  && StringUtils.isNotBlank(c.getTeacher().getName())){
						Teacher t = new Teacher();
						t.setId(c.getTeacher().getId());
						t.setImage(serverConfigurationService.getString(Constant.MOOC_SERVER_URL) + c.getTeacher().getImage());
						t.setName(c.getTeacher().getName());
						t.setProfile(c.getTeacher().getProfile());
						v.setTeacher(t);
					}
					
					courseVOs.add(v);
				}
			}
			
			map.put("data", courseVOs);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return map;
	}
	
	@RequestMapping(value = "/json/reclist", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, Object> reclist() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<RecItemVO> recItemVOs = new ArrayList<RecItemVO>();
		
		try{
			List<RecItem> recItemList = recItemService.list();
			if(recItemList!=null && recItemList.size()>0){
				for(RecItem c : recItemList){
					RecItemVO v = new RecItemVO();
					v.setId(c.getCourse().getId());
					v.setName(c.getCourse().getName());
					v.setImage(serverConfigurationService.getString(Constant.MOOC_SERVER_URL) + c.getCourse().getImage());
					if(c.getCourse().getTeacher()!=null  && StringUtils.isNotBlank(c.getCourse().getTeacher().getName())){
						Teacher t = new Teacher();
						t.setImage(serverConfigurationService.getString(Constant.MOOC_SERVER_URL) + c.getCourse().getTeacher().getImage());
						t.setName(c.getCourse().getTeacher().getName());
						v.setTeacher(t);
					}
					
					recItemVOs.add(v);
				}
			}
			
			map.put("data", recItemVOs);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return map;
	}
	
	@RequestMapping(value = "/json/list/{speId}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, Object> list(@PathVariable String speId,@RequestParam(value="q", required=false) String q,
			                        @RequestParam(value="status", required=false) String status,Integer pages) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<CourseVO> courseVOs = new ArrayList<CourseVO>();
		
		try{
			if(StringUtils.isNotBlank(q)){
				q = q.trim();
			}
			
			if(StringUtils.isNotBlank(status)){
				status = status.trim();
			}
			if(speId.equals("all")){
				speId = "";
			}
			
			List<Course> courses = courseService.findCourse(speId,q,status,pages);
			if(courses!=null && courses.size()>0){
				for(Course c : courses){
					CourseVO v = new CourseVO();
					v.setId(c.getId());
					v.setName(c.getName());
					v.setImage(serverConfigurationService.getString(Constant.MOOC_SERVER_URL) + c.getImage());
					if(c.getTeacher()!=null  && StringUtils.isNotBlank(c.getTeacher().getName())){
						Teacher t = new Teacher();
						t.setId(c.getTeacher().getId());
						t.setImage(serverConfigurationService.getString(Constant.MOOC_SERVER_URL) + c.getTeacher().getImage());
						t.setName(c.getTeacher().getName());
						t.setProfile(c.getTeacher().getProfile());
						v.setTeacher(t);
					}
					v.setCertification(c.getCertification());
					v.setStartDate(c.getStartDate());
					v.setEndDate(c.getEndDate());
					v.setAssistant(c.getAssistant());
					courseVOs.add(v);
				}
			}
			
			map.put("data", courseVOs);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return map;
	}
	
	@RequestMapping(value = "/json/one/{courseId}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, Object> one(@PathVariable String courseId) {
		Map<String, Object> map = new HashMap<String, Object>();
		CourseVO v = new CourseVO();
		
		try{
			Course course = courseService.findOne(courseId);
			if(course!=null){
				v.setId(course.getId());
				v.setName(course.getName());
				v.setImage(serverConfigurationService.getString(Constant.MOOC_SERVER_URL) + course.getImage());
				v.setVideo(serverConfigurationService.getString(Constant.MOOC_SERVER_URL) + course.getVideo());
				v.setDesc(course.getDesc());
				v.setSakaiSiteUrl(course.getSakaiSiteUrl());
				if(course.getTeacher()!=null  && StringUtils.isNotBlank(course.getTeacher().getName())){
					Teacher t = new Teacher();
					t.setId(course.getTeacher().getId());
					t.setImage(serverConfigurationService.getString(Constant.MOOC_SERVER_URL) + course.getTeacher().getImage());
					t.setName(course.getTeacher().getName());
					t.setProfile(course.getTeacher().getProfile());
					v.setTeacher(t);
				}
				v.setSyllabus(course.getSyllabus());
				v.setQas(course.getQas());
				v.setCertification(course.getCertification());
				v.setStudyNum(course.getStudyNum());
				v.setStudyTime(course.getStudyTime());
				List<Course> relate = new ArrayList<Course>();
				if(course.getRelatedCourse()!=null){
					Course temp = null;
					for(Course c : course.getRelatedCourse()){
						temp = new Course();
						temp.setId(c.getId());
						temp.setName(c.getName());
						temp.setImage(serverConfigurationService.getString(Constant.MOOC_SERVER_URL) + c.getImage());
						relate.add(temp);
					}
				}
				v.setRelatedCourse(relate);
				v.setStartDate(course.getStartDate());
				v.setEndDate(course.getEndDate());
			}
			
			map.put("data", v);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return map;
	}
	
}
