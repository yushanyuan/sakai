package org.sakaiproject.resource.tool.course.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.resource.api.course.model.MeleteCourseModel;
import org.sakaiproject.resource.api.course.service.ICourseService;
import org.sakaiproject.resource.api.study.service.IStudyService;
import org.sakaiproject.resource.util.AsyncGridModel;
import org.sakaiproject.resource.util.CacheElement;
import org.sakaiproject.resource.util.CacheUtil;
import org.sakaiproject.resource.util.JsonBuilder;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.tool.api.ToolManager;

import com.opensymphony.xwork2.ActionSupport;

public class ScoreRecalculateAction extends ActionSupport {

	private ICourseService courseService;

	private IStudyService studyService;

	private SiteService siteService = (SiteService) ComponentManager.get(SiteService.class);

	private ToolManager toolManager = (ToolManager) ComponentManager.get(ToolManager.class);

	private Log logger = LogFactory.getLog(ScoreRecalculateAction.class);

	// 截止时间
	private String endTime;

	// 起始时间
	private String startTime;

	// 学号
	private String stuNum;

	// 学生姓名
	private String stuName;

	// 起始
	private String start;

	// 课程ID
	private String courseid;

	// 学习记录ids
	private String studyRecordIds;
	
	// 排序字段
	private String sort;
	
	// 排序方式
	private String dir;
	

	/**
	 * 获得当前站点
	 * 
	 * @return
	 * @throws Exception
	 */
	private Site getCurrentSite() throws Exception {
		Site site = null;
		try {
			site = siteService.getSite(toolManager.getCurrentPlacement().getContext());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
		return site;
	}

	/**
	 * 查找需要重新计算成绩的学生
	 * 
	 * @return
	 */
	public String findScoreList() throws Exception {

		try {
			int startInt = NumberUtils.toInt(start);// null和异常返回默认值
			String startTimeTrans = null; 
			String endTimeTrans = null; 
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startDate = null; 
			Date endDate = null;
			if(StringUtils.isNotBlank(startTime)){
				startTimeTrans = startTime.replace("T", " ");
				startDate = sdf.parse(startTimeTrans);
			}
			if(StringUtils.isNotBlank(endTime)){
				endTimeTrans = endTime.replace("T", " ");
				endDate = sdf.parse(endTimeTrans);
			}
			Site site = this.getCurrentSite();
			// 检查缓存
			CacheElement cacheCourse = CacheUtil.getInstance().getCacheOfCourse(site.getId());
			MeleteCourseModel courseInfo = cacheCourse.getCourse();
			courseid = courseInfo.getId();

			List<AsyncGridModel> data = new ArrayList<AsyncGridModel>();
			// 根据 作业和前测的ID 批改状态 截止时间 查询要批改的试卷
			Object[] results = studyService.getStudyRecordByCondtion(courseid, startDate, endDate, stuNum, stuName, startInt,sort,dir);
			List<Object> testList = (List<Object>) results[1];
			for (Object obj : testList) {// 遍历testList
				Map map = (Map) obj;
				AsyncGridModel model = new AsyncGridModel();
				model.setDataIndex("userid", map.get("userid"));
				model.setDataIndex("studentName", map.get("studentName"));
				model.setDataIndex("stuNum", map.get("stuNum"));
				model.setDataIndex("eduCenter", map.get("eduCenter"));
				model.setDataIndex("score", map.get("score"));
				model.setDataIndex("startStudyTime", map.get("startStudyTime"));
				model.setDataIndex("scoreUpdateTime", map.get("scoreUpdateTime"));
				model.setDataIndex("studyrecordId", map.get("studyrecordId"));
				data.add(model);
			}
			String json = JsonBuilder.builderAsyncGridJson(data, (Integer) results[0]);
			ServletActionContext.getResponse().getWriter().println(json);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * 重新计算成绩
	 * 
	 * @return
	 */
	public String scoreRecalculate() throws Exception {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			String msg = "success";  
			studyService.updateScoreByStudyRecordIds(studyRecordIds);
			response.getWriter().print(msg);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 重新计算成绩(所有查询结果下的)
	 * 
	 * @return
	 */
	public String scoreRecalculateAll() throws Exception {
		try {
			String msg = "success";
			String startTimeTrans = "";
			String endTimeTrans = "";
			Date startDate = null;
			Date endDate = null;
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			if(StringUtils.isNotBlank(startTime)){
				startTimeTrans = startTime.replace("T", " ");
				startDate = sdf.parse(startTimeTrans);
			}
			
			if(StringUtils.isNotBlank(endTime)){
				endTimeTrans = endTime.replace("T", " ");
				endDate = sdf.parse(endTimeTrans);
			}
			
			Site site = this.getCurrentSite();
			// 检查缓存
			CacheElement cacheCourse = CacheUtil.getInstance().getCacheOfCourse(site.getId());
			MeleteCourseModel courseInfo = cacheCourse.getCourse();
			courseid = courseInfo.getId();

			// 根据 作业和前测的ID 批改状态 截止时间 查询要批改的试卷
			Object[] results = studyService.getStudyRecordByCondtion(courseid, startDate, endDate, stuNum, stuName, null,null,null);
			studyRecordIds = "";
			List<Object> testList = (List<Object>) results[1];
			for (Object obj : testList) {// 遍历testList
				Map map = (Map) obj;
				studyRecordIds += map.get("studyrecordId")+",";
			}
			if(StringUtils.isNotBlank(studyRecordIds)){
				studyRecordIds = studyRecordIds.substring(0, studyRecordIds.length()-1);
				studyService.updateScoreByStudyRecordIds(studyRecordIds);
			}
			ServletActionContext.getResponse().getWriter().print(msg);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	public ICourseService getCourseService() {
		return courseService;
	}

	public void setCourseService(ICourseService courseService) {
		this.courseService = courseService;
	}

	public IStudyService getStudyService() {
		return studyService;
	}

	public void setStudyService(IStudyService studyService) {
		this.studyService = studyService;
	}

	public SiteService getSiteService() {
		return siteService;
	}

	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	public ToolManager getToolManager() {
		return toolManager;
	}

	public void setToolManager(ToolManager toolManager) {
		this.toolManager = toolManager;
	}

	public Log getLogger() {
		return logger;
	}

	public void setLogger(Log logger) {
		this.logger = logger;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getStuNum() {
		return stuNum;
	}

	public void setStuNum(String stuNum) {
		this.stuNum = stuNum;
	}

	public String getStuName() {
		return stuName;
	}

	public void setStuName(String stuName) {
		this.stuName = stuName;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getCourseid() {
		return courseid;
	}

	public void setCourseid(String courseid) {
		this.courseid = courseid;
	}

	public String getStudyRecordIds() {
		return studyRecordIds;
	}

	public void setStudyRecordIds(String studyRecordIds) {
		this.studyRecordIds = studyRecordIds;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

}
