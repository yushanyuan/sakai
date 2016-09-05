/**
 * 
 */
package org.sakaiproject.resource.tool.rest;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.sakaiproject.resource.api.course.model.MeleteModuleModel;
import org.sakaiproject.resource.api.course.model.MeleteSectionModel;
import org.sakaiproject.resource.api.course.vo.VideoVO;
import org.sakaiproject.resource.util.CacheElement;
import org.sakaiproject.resource.util.CacheUtil;
import org.sakaiproject.resource.util.JsonBuilder;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @author yushanyuan
 *	移动端取视频 接口
 */
public class VedioAction extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(VedioAction.class);
	private static Log loggerManage = LogFactory.getLog("sysManageResourceR");
	
	
	private String courseId;//siteId
	private String auth;

	/**
	 * 3.	视频接口，从教学平台根据站点id取得该站点所有课程视频以及对应的图片。
	 * @return
	 * @throws Exception
	 */
	public String getVideo() throws Exception{
		
		
		
		CacheElement cacheCourse = CacheUtil.getInstance().getCacheOfCourse(this.courseId);
		
		List<VideoVO> list = new ArrayList<VideoVO>();
		List<MeleteModuleModel> topModules = cacheCourse.getModuleListByParentid(null);
		if(topModules!=null){
			for(MeleteModuleModel model : topModules){
				getEechVideo(model, cacheCourse, list);
			}
		}
		
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		String json = "";
		 
		json = JsonBuilder.builderFormJson(list);
		 
		response.getWriter().println(json);
		
		return null;
	}
	
	
	private void getEechVideo(MeleteModuleModel model, CacheElement cacheCourse, List list){
		
		VideoVO vo = new VideoVO();
		if(model.getVideoUrl()!=null && !"".equals(model.getVideoUrl())){
			vo.setVideoPicPath(model.getVideoPicPath());
			vo.setVideoUrl(model.getVideoUrl());
			vo.setModuleId(model.getId().toString());
			vo.setNodeType("module");
			vo.setTitle(model.getTitle());
			list.add(vo);
		}
		
		
		List<MeleteModuleModel> subModules = cacheCourse.getModuleListByParentid(model.getId().toString());
		if(subModules!=null && subModules.size()>0){
			for(MeleteModuleModel sub : subModules){
				getEechVideo(sub, cacheCourse, list);
				List<MeleteSectionModel> sections = cacheCourse.getSectionListByModuleId(sub.getId().toString());
				if(sections!=null && sections.size()>0){
					for(MeleteSectionModel s : sections){
						if(s.getVideoUrl()!=null && !"".equals(s.getVideoUrl())){
							VideoVO svo = new VideoVO();
							svo.setNodeType("section");
							svo.setSectionId(s.getId().toString());
							svo.setTitle(s.getTitle());
							svo.setVideoPicPath(s.getVideoPicPath());
							svo.setVideoUrl(s.getVideoUrl());
							list.add(svo);
						}
					}
				}
			}
		}
		
	}


	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getAuth() {
		return auth;
	}
	public void setAuth(String auth) {
		this.auth = auth;
	}
	
}
