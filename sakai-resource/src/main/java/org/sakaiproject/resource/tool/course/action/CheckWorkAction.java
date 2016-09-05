package org.sakaiproject.resource.tool.course.action;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.resource.api.course.model.MeleteSectionModel;
import org.sakaiproject.resource.api.course.model.MeleteSelfTestModel;
import org.sakaiproject.resource.api.course.model.MeleteTestModel;
import org.sakaiproject.resource.api.course.service.ICourseService;
import org.sakaiproject.resource.api.study.model.MeleteSelftestRecordModel;
import org.sakaiproject.resource.api.study.model.MeleteTestRecordModel;
import org.sakaiproject.resource.api.study.service.IStudyService;
import org.sakaiproject.resource.util.AsyncGridModel;
import org.sakaiproject.resource.util.CodeTable;
import org.sakaiproject.resource.util.Constants;
import org.sakaiproject.resource.util.JsonBuilder;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.tool.api.ToolManager;

import com.bupticet.paperadmin.tool.Helper;
import com.bupticet.paperadmin.tool.PaperCheckToolUtil;
import com.bupticet.paperadmin.tool.ServerActionTool;
import com.opensymphony.xwork2.ActionSupport;

public class CheckWorkAction extends ActionSupport {

	private ICourseService courseService;

	private IStudyService studyService;

	private Log logger = LogFactory.getLog(CheckWorkAction.class);

	public IStudyService getStudyService() {
		return studyService;
	}

	public void setStudyService(IStudyService studyService) {
		this.studyService = studyService;
	}

	/** 作业或前测ID */
	private String testAndStestId;
	/** 批改状态 */
	private String checkStatus;
	/** 截止时间 */
	private String endTime;
	// 起始
	private String start;

	private String attemptId;
	// 用户ID
	private String userid;
	// 页ID
	private String paperid;
	// 课程ID
	private String courseid;
	// 学生分数
	private String studentScoreString;

	/** 学习记录ID */
	private String studyrecordId;

	/** 批改的试卷类型 1为作业 2为前测 */
	private String paperType;

	public String getStudyrecordId() {
		return studyrecordId;
	}

	public void setStudyrecordId(String studyrecordId) {
		this.studyrecordId = studyrecordId;
	}

	public String getPaperType() {
		return paperType;
	}

	public void setPaperType(String paperType) {
		this.paperType = paperType;
	}

	public String getStudentScoreString() {
		return studentScoreString;
	}

	public void setStudentScoreString(String studentScoreString) {
		this.studentScoreString = studentScoreString;
	}

	public String getCourseid() {
		return courseid;
	}

	public void setCourseid(String courseid) {
		this.courseid = courseid;
	}

	public String getPaperid() {
		return paperid;
	}

	public void setPaperid(String paperid) {
		this.paperid = paperid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getAttemptId() {
		return attemptId;
	}

	public void setAttemptId(String attemptId) {
		this.attemptId = attemptId;
	}

	private SiteService siteService = (SiteService) ComponentManager.get(SiteService.class);

	private ToolManager toolManager = (ToolManager) ComponentManager.get(ToolManager.class);

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getTestAndStestId() {
		return testAndStestId;
	}

	public void setTestAndStestId(String testAndStestId) {
		this.testAndStestId = testAndStestId;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public ICourseService getCourseService() {
		return courseService;
	}

	public void setCourseService(ICourseService courseService) {
		this.courseService = courseService;
	}

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
	 * 查找要批改的作业记录
	 * 
	 * @return
	 */
	public String findCheckWorkList() throws Exception {

		// 将前台的批改试卷下拉框的value拆分为ID与类型
		try {
			String[] testAndStestStrings = testAndStestId.split("_");// "_"拆分
			String testId = testAndStestStrings[0];// id
			String testType = testAndStestStrings[1];// 类型
			int startInt = NumberUtils.toInt(start);// null和异常返回默认值
			String endTimeTrans = endTime.replace("T", " ");// 替换
			List<AsyncGridModel> data = new ArrayList<AsyncGridModel>();
			// 根据 作业和前测的ID 批改状态 截止时间 查询要批改的试卷
			Object[] results = courseService.findCheckWorkList(testId, checkStatus, endTimeTrans, testType, startInt);
			List<Object> testList = (List<Object>) results[1];
			for (Object obj : testList) {// 遍历testList
				Map map = (Map) obj;
				AsyncGridModel model = new AsyncGridModel();
				model.setDataIndex("checkstatus", map.get("checkstatus"));
				model.setDataIndex("studentName", map.get("studentName"));
				model.setDataIndex("stuNum", map.get("stuNum"));
				model.setDataIndex("eduCenter", map.get("eduCenter"));
				model.setDataIndex("score", map.get("score"));
				model.setDataIndex("id", map.get("id"));
				model.setDataIndex("userid", map.get("userid"));
				model.setDataIndex("paperid", map.get("paperid"));
				model.setDataIndex("courseid", map.get("courseid"));
				model.setDataIndex("studyrecordId", map.get("studyrecordId"));
				model.setDataIndex("paperType", testType);
				data.add(model);
			}
			String json = JsonBuilder.builderAsyncGridJson(data);
			ServletActionContext.getResponse().getWriter().println(json);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * 加载课程下的作业和前测试卷列表
	 * 
	 * @return
	 */
	public String loadPaperBox() throws Exception {
		Site curSite = this.getCurrentSite();// 获取站点
		// 根据站点ID所有的作业和前测的ID与名称
		List<Object> resultList = courseService.getTestsAndSelfTestsBySiteId(curSite.getId());
		StringBuffer sb = new StringBuffer("[");
		try {
			if (!resultList.isEmpty()) {
				for (int i = 0; i < resultList.size(); i++) {
					Object object = resultList.get(i);
					if (object.getClass() == MeleteTestModel.class) {
						MeleteTestModel testModel = (MeleteTestModel) object;
						// 前台批改试卷下拉框的value为作业或前测的ID_类型码 类型1表示类型为作业
						sb.append("['").append(testModel.getId()).append("_1','").append(testModel.getName()).append(
								"'],");
					} else {
						MeleteSelfTestModel selfTestModel = (MeleteSelfTestModel) object;
						// 前台批改试卷下拉框的value为作业或前测的ID_类型码 类型2表示类型为前测
						sb.append("['").append(selfTestModel.getId()).append("_2','").append(selfTestModel.getName())
								.append("'],");
					}

				}
				sb = sb.deleteCharAt(sb.length() - 1);
			}
			sb.append("]");
			HttpServletResponse response = ServletActionContext.getResponse();
			response.getWriter().println(sb.toString());
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 教师点击批改时的要向页面传得参数的初始化
	 * 
	 * @return
	 * @throws Exception
	 */
	public String checkObjInit() throws Exception {
		try {
			String pName = Helper.getPaperName(paperid);// 获得页面ID
			String answerName = Helper.getStuAnswerName(paperid, attemptId);
			String path2 = "";
			String contentString = "";
			if (CodeTable.paperTypeTest.equals(paperType)) {
				// 若测试类型为作业
				path2 = Constants.getStuTestPath(userid, courseid);// 保存学生作业路径
				contentString = ServerActionTool.exportObjectPaper(Constants.getTestMaterialURL(courseid), path2,
						pName, path2, answerName);
			} else if (CodeTable.paperTypeSelfTest.equals(paperType)) {
				// 若测试类型为前测
				path2 = Constants.getStuSelfTestPath(userid, courseid);
				contentString = ServerActionTool.exportObjectPaper(Constants.getSelfTestMaterialURL(courseid), path2,
						pName, path2, answerName);
			}

			ServletActionContext.getRequest().setAttribute("content", contentString);
			ServletActionContext.getRequest().setAttribute("paperid", paperid);
			ServletActionContext.getRequest().setAttribute("courseid", courseid);
			ServletActionContext.getRequest().setAttribute("attemptId", attemptId);
			ServletActionContext.getRequest().setAttribute("userid", userid);
			ServletActionContext.getRequest().setAttribute("paperType", paperType);
			ServletActionContext.getRequest().setAttribute("studyrecordId", studyrecordId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
		return "checkpage";
	}

	/**
	 * 教师批完卷提交处理
	 * 
	 * @return
	 * @throws Exception
	 */
	public String checkObjSave() throws Exception {
		try {
			String answerFileName = Helper.getStuAnswerName(paperid, attemptId);
			String path = "";
			if (CodeTable.paperTypeTest.equals(paperType)) {
				// 若测试类型为作业
				path = Constants.getStuTestPath(userid, courseid);
			} else if (CodeTable.paperTypeSelfTest.equals(paperType)) {
				// 若测试类型为前测
				path = Constants.getStuSelfTestPath(userid, courseid);
			}
			try {
				File studentAnswerFile = new File(path, answerFileName);
				studentScoreString = studentScoreString.replace("\"[", "\"").replace("]\"", "\"");

				PaperCheckToolUtil.checkObjPaperAction(studentScoreString, studentAnswerFile);
				Object result = studyService.checkTestSave(attemptId, userid, studentAnswerFile, courseid, paperType,
						studyrecordId);
				if (CodeTable.paperTypeTest.equals(paperType)) {// 如果是作业
					MeleteTestRecordModel testRecord = (MeleteTestRecordModel) result;
					if (CodeTable.passStatusYes.equals(testRecord.getStatus().toString())) {
						// 如果作业记录通过 则检查并更改所属页节点记录是否通过

						if (testRecord.getModuleId() != null) {// 属于节点
							boolean pass = studyService.checkModulePassStatus(testRecord.getModuleId(), testRecord
									.getStudyrecordId(), true);
						} else {// 属于页
							boolean pass = studyService.checkSectionPassStatus(testRecord.getSectionId(), testRecord
									.getStudyrecordId(), true);
							if (pass) {// 页改为通过
								// 判断上级节点是否通过
								MeleteSectionModel section = (MeleteSectionModel) courseService
										.getSectionById(testRecord.getSectionId().toString());
								Long moduleId = section.getModuleId();
								studyService.checkModulePassStatus(moduleId, testRecord.getStudyrecordId(), true);
							}
						}

					}
				} else if (CodeTable.paperTypeSelfTest.equals(paperType)) {
					// 如果是前测
					MeleteSelftestRecordModel selfTestRecord = (MeleteSelftestRecordModel) result;
					if (CodeTable.passStatusYes.equals(selfTestRecord.getStatus().toString())) {
						if (selfTestRecord.getModuleId() != null) {// 属于节点
							boolean pass = studyService.checkModulePassStatus(selfTestRecord.getModuleId(),
									selfTestRecord.getStudyrecordId(), false);
						} else {// 属于页
							boolean pass = studyService.checkSectionPassStatus(selfTestRecord.getSectionId(),
									selfTestRecord.getStudyrecordId(), false);
							if (pass) {// 页改为通过
								// 判断上级节点是否通过
								MeleteSectionModel section = (MeleteSectionModel) courseService
										.getSectionById(selfTestRecord.getSectionId().toString());
								Long moduleId = section.getModuleId();
								studyService.checkModulePassStatus(moduleId, selfTestRecord.getStudyrecordId(), true);
							}
						}
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
				throw e;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
		return null;
	}
}
