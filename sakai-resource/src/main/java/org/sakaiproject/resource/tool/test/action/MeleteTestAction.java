package org.sakaiproject.resource.tool.test.action;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.sakaiproject.authz.api.SecurityService;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.resource.api.course.model.MeleteCourseModel;
import org.sakaiproject.resource.api.course.model.MeleteForumModel;
import org.sakaiproject.resource.api.course.model.MeleteTestModel;
import org.sakaiproject.resource.api.course.service.ICourseService;
import org.sakaiproject.resource.api.study.model.MeleteTestAttemptModel;
import org.sakaiproject.resource.api.study.model.MeleteTestRecordModel;
import org.sakaiproject.resource.api.study.service.IStudyService;
import org.sakaiproject.resource.util.CacheElement;
import org.sakaiproject.resource.util.CacheUtil;
import org.sakaiproject.resource.util.FunctionRegister;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.tool.api.ToolManager;
import org.sakaiproject.user.api.UserDirectoryService;

import com.bupticet.paperadmin.tool.Helper;
import com.bupticet.paperadmin.tool.ServerActionTool;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 作业空间（数据为阶段作业）
 * 
 * @author zihongyan 2013-8-16
 * 
 */
public class MeleteTestAction extends ActionSupport {

	private ICourseService courseService;

	private IStudyService studyService;

	/** 作业集合 */
	private List<MeleteTestModel> meleteTestModelList;

	/** 作业记录集合 */
	private List<Map<String, String>> meleteTestRecordModelList;

	/** 论坛帖子集合 */
	private List<MeleteForumModel> meleteForumModelList;

	/** 作业尝试记录集合 */
	private List<MeleteTestAttemptModel> meleteTestAttempModelList;

	private List<Map<String, Object>> meleteTestAttempModelMap;

	/** studyrecordId */
	private String studyrecordId;
	/** testRecordId */
	private String testRecordId;

	/** 试卷id */
	private String testPaperid;
	/** 试卷尝试记录idF */
	private String testattemptId;

	private String testId;

	/** 试卷内容 */
	private String context;
	
	/**模块id*/
	private Long modelId;

	private SiteService siteService = (SiteService) ComponentManager
			.get(SiteService.class);

	private ToolManager toolManager = (ToolManager) ComponentManager
			.get(ToolManager.class);

	private UserDirectoryService userDirectoryService = (UserDirectoryService) ComponentManager
			.get(UserDirectoryService.class);

	private SecurityService securityService = (SecurityService) ComponentManager
			.get(SecurityService.class);

	private Site getCurrentSite() throws Exception {
		Site site = null;
		try {
			site = siteService.getSite(toolManager.getCurrentPlacement()
					.getContext());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return site;
	}

	private String getCurrentUserId() {
		return userDirectoryService.getCurrentUser().getId();
	}

	public String execute() throws Exception {
		Site curSite = this.getCurrentSite();
		String siteRef = siteService.siteReference(curSite.getId());
		String currentUser = this.getCurrentUserId();
		boolean isTeacher = securityService.unlock(currentUser,
				FunctionRegister.COURSE_SPACE_PERM, siteRef);
		boolean isStudent = securityService.unlock(currentUser,
				FunctionRegister.STUDY_SPACE_PERM, siteRef);
		if (isTeacher || isStudent) {// 教师
			// 学生
			// 获得studyrecordId
			CacheUtil.getInstance().getCacheOfCourse(curSite.getId());
			CacheElement cacheStudy = CacheUtil.getInstance().getCacheOfStudyrecord(curSite.getId(),
					currentUser, studyrecordId);
			if(StringUtils.isBlank(studyrecordId)){
				studyrecordId = cacheStudy.getStudyRecord().getStudyrecordId().toString();
			}
			// 加载作业
			loadTest();
			// 根据学习记录id和testId得到作业历史记录
			if (meleteTestModelList != null && !meleteTestModelList.isEmpty()) {
				meleteTestRecordModelList = new ArrayList<Map<String, String>>();
				// 人作业对应的作业历史记录
				List<MeleteTestRecordModel> meTestRecordModelList = studyService
						.getTestRecordByStudyrecordId(studyrecordId);
				
				Date curDate = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				// 比较如果有历史记录则添加进去
				for (MeleteTestModel m : meleteTestModelList) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("testId", m.getId().toString());
					map.put("name", m.getName());
					map.put("requirement", m.getRequirement());
					// 默认为0
					map.put("status", "0");
					map.put("testRecordId", "0");
					map.put("score", "0");
					
					String sod = "";
					String eod = "";
					
					if(m.getStartOpenDate()!=null){
						sod = sdf.format(m.getStartOpenDate());
					}
					
					if(m.getEndOpenDate()!=null){
						eod = sdf.format(m.getEndOpenDate());
					}
					map.put("startOpenDate", sod);
					map.put("endOpenDate", eod);
					
					String isOpen = "0";//0:正常，1：开放时间已经截至，2：未到开放时间,如果是教师的话都为0；
					if(!isTeacher){ //管理员同时有教师和学生权限，所以判断没有教师权限时才加限制
						if(m.getEndOpenDate() != null && curDate.after(m.getEndOpenDate())){
							isOpen = "1";
						} 
						if(m.getStartOpenDate() != null && curDate.before(m.getStartOpenDate())){
							isOpen = "2";
						} 
					}
					map.put("isOpen", isOpen);
					if (meTestRecordModelList != null
							&& !meTestRecordModelList.isEmpty()) {
						// 遍历记录
						boolean flag = false;
						for (MeleteTestRecordModel mm : meTestRecordModelList) {
							if (flag) {
								break;
							}
							if (m.getId().equals(mm.getTestId())) {
								map.put("status", mm.getStatus().toString());
								map.put("testRecordId", mm.getTestrecordId()
										.toString());
								map.put("score", mm.getScore().toString());
								flag = true;
							}
						}
					}
					// 添加封装的数据
					meleteTestRecordModelList.add(map);
				}

			}
			return "student";
		} else {
			addActionError("无权限");
			return ERROR;
		}
	}

	/**
	 * 返回已做过的测试内容
	 * 
	 * @author zihongyan 2013-8-25
	 * @return
	 */
	public String getTestAttemptRecordPaper() {

		try {
			// 获取站点id
			Site site;
			site = this.getCurrentSite();
			String siteId = site.getId();
			String courseId = courseService.getCourseBySiteId(siteId).getId();
			String materialPath = org.sakaiproject.resource.util.Constants
					.getTestMaterialURL(courseId);
			String userId = this.getCurrentUserId();
			String path2 = org.sakaiproject.resource.util.Constants
					.getStuTestPath(userId, courseId);
			String paperName = Helper.getPaperName(testPaperid);
			String answerName = Helper.getStuAnswerName(testPaperid,
					testattemptId);
			MeleteTestModel test = (MeleteTestModel) courseService
					.getModelById(MeleteTestModel.class, Long.parseLong(testId));
			boolean status = "0".equalsIgnoreCase(test.getSamepaper());
			context = ServerActionTool.exportExamInfPaper(materialPath, path2,
					paperName, path2, answerName, status);
			return "viewPaper";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 获得作业的所有尝试记录
	 * 
	 * @author zihongyan 2013-8-25
	 * @return
	 */
	public String getTestAttemptRecord() {
		try {
			meleteTestAttempModelList = courseService
					.getAllMeleteTestAttemptModelByTestRecordId(testRecordId);
			MeleteTestRecordModel mTestRecord = (MeleteTestRecordModel) courseService
					.getModelById(MeleteTestRecordModel.class,
							Long.parseLong(testRecordId));
			if (meleteTestAttempModelList != null
					&& meleteTestAttempModelList.size() > 0) {
				// 封装尝试记录数据
				meleteTestAttempModelMap = new ArrayList<Map<String, Object>>();
				// 遍历尝试记录得到并封装数据
				for (MeleteTestAttemptModel m : meleteTestAttempModelList) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("testId", mTestRecord.getTestId());
					map.put("name", mTestRecord.getTestName());
					map.put("objScore", m.getObjScore());
					map.put("subScore", m.getSubScore());
					map.put("endTime", m.getEndTime());
					map.put("score", m.getScore());
					map.put("testPaperid", m.getTestPaperid());
					map.put("meleteTestRecordId", m.getMeleteTestRecordId());
					map.put("testattemptId", m.getTestattemptId());
					meleteTestAttempModelMap.add(map);
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "view_testAttemptRecord";
	}

	/**
	 * 加载作业
	 * 
	 * @author zihongyan 2013-8-16
	 * @return
	 */
	public String loadTest() {
		try {
			// 获取站点id
			Site site = this.getCurrentSite();
			String siteId = site.getId();

			MeleteCourseModel course = courseService.getCourseBySiteId(siteId);
			meleteTestModelList = courseService
					.getAllMeleteTestModelByCourseId(course.getId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 加载论坛帖子
	 * 
	 * @return
	 */
	public String loadFroum() {
		try {
			// 获取站点id
			Site site = this.getCurrentSite();
			String siteId = site.getId();
			MeleteCourseModel course = courseService.getCourseBySiteId(siteId);
			meleteForumModelList = courseService
					.getAllMeleteForumModelByCourseId(course.getId(),modelId);
			return "viewForum";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public ICourseService getCourseService() {
		return courseService;
	}

	public void setCourseService(ICourseService courseService) {
		this.courseService = courseService;
	}

	public List<MeleteTestModel> getMeleteTestModelList() {
		return meleteTestModelList;
	}

	public void setMeleteTestModelList(List<MeleteTestModel> meleteTestModelList) {
		this.meleteTestModelList = meleteTestModelList;
	}

	public String getStudyrecordId() {
		return studyrecordId;
	}

	public void setStudyrecordId(String studyrecordId) {
		this.studyrecordId = studyrecordId;
	}

	public IStudyService getStudyService() {
		return studyService;
	}

	public void setStudyService(IStudyService studyService) {
		this.studyService = studyService;
	}

	public List<MeleteForumModel> getMeleteForumModelList() {
		return meleteForumModelList;
	}

	public void setMeleteForumModelList(
			List<MeleteForumModel> meleteForumModelList) {
		this.meleteForumModelList = meleteForumModelList;
	}

	public List<Map<String, String>> getMeleteTestRecordModelList() {
		return meleteTestRecordModelList;
	}

	public void setMeleteTestRecordModelList(
			List<Map<String, String>> meleteTestRecordModelList) {
		this.meleteTestRecordModelList = meleteTestRecordModelList;
	}

	public List<MeleteTestAttemptModel> getMeleteTestAttempModelList() {
		return meleteTestAttempModelList;
	}

	public void setMeleteTestAttempModelList(
			List<MeleteTestAttemptModel> meleteTestAttempModelList) {
		this.meleteTestAttempModelList = meleteTestAttempModelList;
	}

	public String getTestRecordId() {
		return testRecordId;
	}

	public void setTestRecordId(String testRecordId) {
		this.testRecordId = testRecordId;
	}

	public List<Map<String, Object>> getMeleteTestAttempModelMap() {
		return meleteTestAttempModelMap;
	}

	public void setMeleteTestAttempModelMap(
			List<Map<String, Object>> meleteTestAttempModelMap) {
		this.meleteTestAttempModelMap = meleteTestAttempModelMap;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getTestPaperid() {
		return testPaperid;
	}

	public void setTestPaperid(String testPaperid) {
		this.testPaperid = testPaperid;
	}

	public String getTestattemptId() {
		return testattemptId;
	}

	public void setTestattemptId(String testattemptId) {
		this.testattemptId = testattemptId;
	}

	public String getTestId() {
		return testId;
	}

	public void setTestId(String testId) {
		this.testId = testId;
	}

	public Long getModelId() {
		return modelId;
	}

	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}
}
