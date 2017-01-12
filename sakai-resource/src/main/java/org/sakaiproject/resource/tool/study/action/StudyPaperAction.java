package org.sakaiproject.resource.tool.study.action;

import java.io.File;
import java.io.FileFilter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.resource.api.course.model.MeleteCourseModel;
import org.sakaiproject.resource.api.course.model.MeleteForumModel;
import org.sakaiproject.resource.api.course.model.MeleteSelfTestModel;
import org.sakaiproject.resource.api.course.model.MeleteTestModel;
import org.sakaiproject.resource.api.course.service.ICourseService;
import org.sakaiproject.resource.api.forum.service.ForumService;
import org.sakaiproject.resource.api.study.model.MeleteForumRecordModel;
import org.sakaiproject.resource.api.study.model.MeleteSelftestAttemptModel;
import org.sakaiproject.resource.api.study.model.MeleteSelftestRecordModel;
import org.sakaiproject.resource.api.study.model.MeleteTestAttemptModel;
import org.sakaiproject.resource.api.study.model.MeleteTestRecordModel;
import org.sakaiproject.resource.api.study.service.IStudyService;
import org.sakaiproject.resource.impl.forum.service.MessageForumServiceImpl;
import org.sakaiproject.resource.util.CacheElement;
import org.sakaiproject.resource.util.CacheUtil;
import org.sakaiproject.resource.util.CodeTable;
import org.sakaiproject.resource.util.Constants;
import org.sakaiproject.resource.util.JsonBuilder;
import org.sakaiproject.resource.util.SpringContextUtil;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.site.api.ToolConfiguration;
import org.sakaiproject.tool.api.ToolManager;
import org.sakaiproject.user.api.UserDirectoryService;

import com.bupticet.paperadmin.tool.Helper;
import com.bupticet.paperadmin.tool.ServerActionTool;
import com.opensymphony.xwork2.ActionSupport;

public class StudyPaperAction extends ActionSupport {

	private static final long serialVersionUID = -7634905738622573785L;
	private static Log logger = LogFactory.getLog(StudyPaperAction.class);
	private static Log loggerManage = LogFactory.getLog("sysManageResourceR");

	private int schemaId;

	private String testId;

	private String samepaper;

	private String courseId;

	private String topicId;

	private String paperId;

	private String startTime;

	private String answerString;

	private String testrecordId;

	private String passScore;

	private String forumrecordId;

	private String forumId;

	private String studyrecordId;

	private SiteService siteService = (SiteService) ComponentManager.get(SiteService.class);

	private ToolManager toolManager = (ToolManager) ComponentManager.get(ToolManager.class);

	private UserDirectoryService userDirectoryService = (UserDirectoryService) ComponentManager
			.get(UserDirectoryService.class);

	private IStudyService studyService;

	private ICourseService courseService;

	public ICourseService getCourseService() {
		return courseService;
	}

	public void setCourseService(ICourseService courseService) {
		this.courseService = courseService;
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

	private String getCurrentUserId() {
		return userDirectoryService.getCurrentUser().getId();
	}

	/**
	 * 根据情况返回对应的forum接口
	 * 
	 * @throws Exception
	 */
	public ForumService getForumService() throws Exception {
		Site curSite = this.getCurrentSite();
		ToolConfiguration f = curSite.getToolForCommonId(MessageForumServiceImpl.FORUM_NAME);
		if (f != null) {
			return (ForumService) SpringContextUtil.getBean("messageforumService");
		}
		//return (ForumService) SpringContextUtil.getBean("jforumService");
		return null;
	}

	/**
	 * 写作业初始化
	 * 
	 * @param studyrecordId
	 * @param testId
	 *            作业id
	 * @param
	 * @return
	 * @throws Exception
	 */
	public String writeTestInit() throws Exception {
		String courseId = null;
		String testPaperId = null;
		try {
			String siteId = getCurrentSite().getId();
			CacheElement cacheCourse = CacheUtil.getInstance().getCacheOfCourse(siteId);
			CacheElement cacheStudy = CacheUtil.getInstance().getCacheOfStudyrecord(siteId, getCurrentUserId(),
					studyrecordId);
			if (StringUtils.isBlank(studyrecordId)) {
				studyrecordId = cacheStudy.getStudyRecord().getStudyrecordId().toString();
			}

			MeleteCourseModel course = cacheCourse.getCourse();
			courseId = course.getId();

			MeleteTestRecordModel testRecord = cacheStudy.getTestRecord(Long.valueOf(testId));
			String testrecordId = testRecord.getTestrecordId().toString();

			MeleteTestModel test = cacheCourse.getTest(Long.valueOf(testId));
			String passScore = test.getMasteryScore().toString();
			String samepaper = test.getSamepaper();

			// 检查是否满足最小间隔 分钟
			Long minInterval = test.getMinTimeInterval();
			if (minInterval == null) {
				minInterval = Long.parseLong(Constants.TEST_MININTERVAL);
			}
			if (testRecord.getAttemptNumber() > 0) {
				Calendar c = Calendar.getInstance();
				c.setTime(testRecord.getLastCommitTime());
				c.add(Calendar.SECOND, (int) (minInterval * 60));
				if (c.compareTo(Calendar.getInstance()) > 0) {
					ServletActionContext.getRequest().setAttribute("needWait", true);
					return "test";
				}
			}

			Date startTime = new Date();
			String studentId = getCurrentUserId();
			// 文件保存路径
			String path = Constants.getStuTestPath(studentId, courseId);
			String content = null;

			String testPaperName = null;
			if (samepaper.equals(CodeTable.samePaperYes)) {// 每次使用同一试卷
				testPaperName = Helper.TestPrefix + testId + Helper.EndTag;
				File destFile = new File(path, testPaperName);
				File aDestFile = new File(path, Helper.getAnswerName(testPaperName));
				if (!destFile.exists() || !aDestFile.exists()) {
					// 拷贝试卷文件
					File srcFile = getTestRandomPaper(courseId, testId);
					FileUtils.copyFile(srcFile, destFile);
					// 拷贝答案文件
					File aSrcFile = new File(Constants.getTestLibAnswerPath(courseId, testId),
							Helper.getAnswerName(srcFile.getName()));
					FileUtils.copyFile(aSrcFile, aDestFile);
				}
			} else {
				File srcFile = getTestRandomPaper(courseId, testId);
				testPaperName = srcFile.getName();
				File destFile = new File(path, testPaperName);
				File aDestFile = new File(path, Helper.getAnswerName(testPaperName));
				if (!destFile.exists() || !aDestFile.exists()) {
					FileUtils.copyFile(srcFile, destFile);
					// 拷贝答案文件
					File aSrcFile = new File(Constants.getTestLibAnswerPath(courseId, testId),
							Helper.getAnswerName(testPaperName));
					FileUtils.copyFile(aSrcFile, aDestFile);
				}
			}
			testPaperId = Helper.getPaperId(testPaperName);
			content = ServerActionTool.exportExercisePaper(Constants.getTestMaterialURL(courseId), path, testPaperName);

			ServletActionContext.getRequest().setAttribute("content", content);
			ServletActionContext.getRequest().setAttribute("startTime", startTime.getTime());
			ServletActionContext.getRequest().setAttribute("courseId", courseId);
			ServletActionContext.getRequest().setAttribute("testId", testId);
			ServletActionContext.getRequest().setAttribute("paperId", testPaperId);
			ServletActionContext.getRequest().setAttribute("testrecordId", testrecordId);
			ServletActionContext.getRequest().setAttribute("passScore", passScore);
			ServletActionContext.getRequest().setAttribute("samepaper", samepaper);
			ServletActionContext.getRequest().setAttribute("studyrecordId", studyrecordId);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			loggerManage.error("test write init error-" + courseId + " " + paperId + ":" + sw.toString());
			throw e;
		}
		return "test";
	}

	private static File getTestRandomPaper(String courseId, String testId) throws Exception {
		File p = new File(Constants.getTestLibPath(courseId, testId));
		if (!p.exists() || !p.isDirectory()) {
			throw new Exception("Can't found the test paper lib path!");
		}
		// only get the json
		File[] fList = p.listFiles(new FileFilter() {
			public boolean accept(File arg0) {
				if (arg0.getName().endsWith(Helper.EndTag)) {
					return true;
				}
				return false;
			}
		});

		int fSum = fList.length;
		if (fSum <= 0) {
			throw new Exception("Can't found papers in the test paper lib directory!");
		}
		Random r = new Random();
		int fNum = r.nextInt(fSum);
		return fList[fNum];
	}

	/**
	 * 提交答案并保存作业尝试记录
	 * 
	 * @return
	 * @throws Exception
	 */
	public String writeTestSave() throws Exception {
		String studentId = getCurrentUserId();
		// 先检查学习记录
		CacheElement cacheStudy = CacheUtil.getInstance().getCacheOfStudyrecord(getCurrentSite().getId(), studentId,
				studyrecordId);
		if (StringUtils.isBlank(studyrecordId)) {
			studyrecordId = cacheStudy.getStudyRecord().getStudyrecordId().toString();
		}

		MeleteTestAttemptModel testAttempt = null;
		String content = "";
		try {
			testAttempt = studyService.saveTestAttempt(courseId, testrecordId, studentId, paperId, answerString,
					startTime, passScore, studyrecordId, testId);
			Long attempId = testAttempt.getTestattemptId();
			String paperName = Helper.getPaperName(paperId);
			String answerName = Helper.getStuAnswerName(paperId, attempId.toString());
			String path2 = Constants.getStuTestPath(studentId, courseId);
			content = ServerActionTool.exportExamInfPaper(Constants.getTestMaterialURL(courseId), path2, paperName,
					path2, answerName, samepaper.equals(CodeTable.samePaperNo));

		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			loggerManage.error("test write save error-" + courseId + " " + paperId + ":" + sw.toString());
			throw e;
		}
		logger.debug(content);
		ServletActionContext.getRequest().setAttribute("data", JsonBuilder.builderObjectJson(content));
		return "result";
	}

	/**
	 * 写前测初始化
	 * 
	 * @param studyrecordId
	 * @param testId
	 * @return
	 * @throws Exception
	 */
	public String writeSelfTestInit() throws Exception {
		try {
			String siteId = getCurrentSite().getId();
			CacheElement cacheCourse = CacheUtil.getInstance().getCacheOfCourse(siteId);
			CacheElement cacheStudy = CacheUtil.getInstance().getCacheOfStudyrecord(siteId, getCurrentUserId(),
					studyrecordId);

			MeleteCourseModel course = cacheCourse.getCourse();
			String courseId = course.getId();

			MeleteSelfTestModel selftest = cacheCourse.getSelftest(Long.valueOf(testId));
			String samepaper = selftest.getSamepaper();
			String passScore = selftest.getMasteryScore().toString();

			MeleteSelftestRecordModel selftestRecord = cacheStudy.getSelftestRecord(Long.valueOf(testId));
			String testrecordId = selftestRecord.getSelftestrecordId().toString();

			String studentId = getCurrentUserId();
			// 文件保存路径
			String path = Constants.getStuSelfTestPath(studentId, courseId);
			String content = null;
			Date startTime = new Date();
			String testPaperId = null;
			String testPaperName = null;
			if (samepaper.equals(CodeTable.samePaperYes)) {// 每次使用同一试卷
				testPaperName = Helper.SelfTestPrefix + testId + Helper.EndTag;
				File destFile = new File(path, testPaperName);
				File aDestFile = new File(path, Helper.getAnswerName(testPaperName));
				if (!destFile.exists() || !aDestFile.exists()) {
					// 拷贝试卷文件
					File srcFile = getSelfTestRandomPaper(courseId, testId);
					FileUtils.copyFile(srcFile, destFile);
					// 拷贝答案文件
					File aSrcFile = new File(Constants.getSelfTestLibAnswerPath(courseId, testId),
							Helper.getAnswerName(srcFile.getName()));
					FileUtils.copyFile(aSrcFile, aDestFile);
				}
			} else {
				File srcFile = getSelfTestRandomPaper(courseId, testId);
				testPaperName = srcFile.getName();
				File destFile = new File(path, testPaperName);
				File aDestFile = new File(path, Helper.getAnswerName(testPaperName));
				if (!destFile.exists() || !aDestFile.exists()) {
					FileUtils.copyFile(srcFile, destFile);
					// 拷贝答案文件
					File aSrcFile = new File(Constants.getSelfTestLibAnswerPath(courseId, testId),
							Helper.getAnswerName(testPaperName));
					FileUtils.copyFile(aSrcFile, aDestFile);
				}
			}
			testPaperId = Helper.getPaperId(testPaperName);
			content = ServerActionTool.exportExercisePaper(Constants.getSelfTestMaterialURL(courseId), path,
					testPaperName);

			ServletActionContext.getRequest().setAttribute("content", content);
			ServletActionContext.getRequest().setAttribute("startTime", startTime.getTime());
			ServletActionContext.getRequest().setAttribute("courseId", courseId);
			ServletActionContext.getRequest().setAttribute("testId", testId);
			ServletActionContext.getRequest().setAttribute("paperId", testPaperId);
			ServletActionContext.getRequest().setAttribute("testrecordId", testrecordId);
			ServletActionContext.getRequest().setAttribute("passScore", passScore);
			ServletActionContext.getRequest().setAttribute("samepaper", samepaper);
			ServletActionContext.getRequest().setAttribute("studyrecordId", studyrecordId);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return "selftest";
	}

	private static File getSelfTestRandomPaper(String courseId, String testId) throws Exception {
		File p = new File(Constants.getSelfTestLibPath(courseId, testId));
		if (!p.exists() || !p.isDirectory()) {
			throw new Exception("Can't found the test paper lib path!");
		}
		File[] fList = p.listFiles();
		int fSum = fList.length;
		if (fSum <= 0) {
			throw new Exception("Can't found papers in the test paper lib directory!");
		}
		Random r = new Random();
		int fNum = r.nextInt(fSum);
		return fList[fNum];
	}

	public String writeSelfTestSave() throws Exception {
		String studentId = getCurrentUserId();
		MeleteSelftestAttemptModel testAttempt = null;
		String content = "";
		try {
			testAttempt = studyService.saveSelftestAttempt(courseId, testrecordId, studentId, paperId, answerString,
					startTime, passScore, studyrecordId, testId);
			Long attempId = testAttempt.getSelftestattemptId();
			String paperName = Helper.getPaperName(paperId);
			String answerName = Helper.getStuAnswerName(paperId, attempId.toString());
			String path2 = Constants.getStuSelfTestPath(studentId, courseId);
			content = ServerActionTool.exportExamInfPaper(Constants.getSelfTestMaterialURL(courseId), path2, paperName,
					path2, answerName, samepaper.equals(CodeTable.samePaperNo));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
		ServletActionContext.getRequest().setAttribute("data", JsonBuilder.builderObjectJson(content));
		return "result";
	}

	/**
	 * 论坛初始化
	 * 
	 * @param studyrecordId
	 *            学习记录id
	 * @return forumId
	 * @throws Exception
	 */
	public String forumInit() throws Exception {
		try {
			String siteId = getCurrentSite().getId();
			CacheElement cacheCourse = CacheUtil.getInstance().getCacheOfCourse(siteId);
			CacheElement cacheStudy = CacheUtil.getInstance().getCacheOfStudyrecord(siteId, getCurrentUserId(),
					studyrecordId);

			MeleteCourseModel course = cacheCourse.getCourse();
			MeleteForumModel forum = cacheCourse.getForum(Long.valueOf(forumId));
			MeleteForumRecordModel forumRecord = cacheStudy.getForumRecord(Long.valueOf(forumId));
			String forumId = forum.getForumId();
			String topicId = forum.getTopicId();
			String areaId = forum.getAreaId();

			String courseId = course.getId();
			String forumrecordId = forumRecord.getForumrecordId().toString();

			String url = getForumService().generateUriString(areaId, forumId, topicId);
			url = ServletActionContext.getResponse().encodeRedirectURL(url);
			// System.out.println("---url---"+url);
			ServletActionContext.getRequest().setAttribute("forumUrl", url);
			ServletActionContext.getRequest().setAttribute("courseId", courseId);
			ServletActionContext.getRequest().setAttribute("forumId", forumId);
			ServletActionContext.getRequest().setAttribute("topicId", topicId);
			ServletActionContext.getRequest().setAttribute("areaId", areaId);
			ServletActionContext.getRequest().setAttribute("forumrecordId", forumrecordId);
			ServletActionContext.getRequest().setAttribute("studyrecordId", studyrecordId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return "forum";
	}

	/**
	 * 学生发帖后调用
	 * 
	 * @return
	 * @throws Exception
	 */
	public String writeForumSave() throws Exception {
		String studentId = getCurrentUserId();
		try {
			studyService.saveForumRecord(courseId, studentId, topicId, studyrecordId);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return null;
	}

	private Site getCurrentSite() throws Exception {
		Site site = null;
		try {
			site = siteService.getSite(toolManager.getCurrentPlacement().getContext());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return site;
	}

	public int getSchemaId() {
		return schemaId;
	}

	public void setSchemaId(int schemaId) {
		this.schemaId = schemaId;
	}

	public String getTestId() {
		return testId;
	}

	public void setTestId(String testId) {
		this.testId = testId;
	}

	public String getSamepaper() {
		return samepaper;
	}

	public void setSamepaper(String samepaper) {
		this.samepaper = samepaper;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public String getPaperId() {
		return paperId;
	}

	public void setPaperId(String paperId) {
		this.paperId = paperId;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getAnswerString() {
		return answerString;
	}

	public void setAnswerString(String answerString) {
		this.answerString = answerString;
	}

	public String getTestrecordId() {
		return testrecordId;
	}

	public void setTestrecordId(String testrecordId) {
		this.testrecordId = testrecordId;
	}

	public String getPassScore() {
		return passScore;
	}

	public void setPassScore(String passScore) {
		this.passScore = passScore;
	}

	public String getForumrecordId() {
		return forumrecordId;
	}

	public void setForumrecordId(String forumrecordId) {
		this.forumrecordId = forumrecordId;
	}

	public String getForumId() {
		return forumId;
	}

	public void setForumId(String forumId) {
		this.forumId = forumId;
	}
}
