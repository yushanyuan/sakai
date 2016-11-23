package org.sakaiproject.resource.tool.course.action;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.adl.parsers.dom.DOMTreeUtility;
import org.adl.samplerte.server.packageimport.ManifestHandler;
import org.adl.samplerte.server.packageimport.parsers.dom.ADLDOMParser;
import org.adl.samplerte.util.UnZipHandler;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.sakaiproject.authz.api.SecurityService;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.resource.api.course.model.MeleteCourseModel;
import org.sakaiproject.resource.api.course.model.MeleteForumModel;
import org.sakaiproject.resource.api.course.model.MeleteModuleModel;
import org.sakaiproject.resource.api.course.model.MeleteSectionModel;
import org.sakaiproject.resource.api.course.model.MeleteSelfTestModel;
import org.sakaiproject.resource.api.course.model.MeleteTestModel;
import org.sakaiproject.resource.api.course.service.ICourseService;
import org.sakaiproject.resource.api.course.vo.PaperAttributeNew;
import org.sakaiproject.resource.api.course.vo.SchemaContentNew;
import org.sakaiproject.resource.api.course.vo.SchemaNewVO;
import org.sakaiproject.resource.api.file.model.ResourceFileModel;
import org.sakaiproject.resource.api.file.service.IFileService;
import org.sakaiproject.resource.api.forum.model.AreaModel;
import org.sakaiproject.resource.api.forum.model.ForumModel;
import org.sakaiproject.resource.api.forum.model.TopicModel;
import org.sakaiproject.resource.api.forum.service.ForumService;
import org.sakaiproject.resource.api.study.model.MeleteStudyRecordModel;
import org.sakaiproject.resource.api.study.service.IStudyService;
import org.sakaiproject.resource.api.template.model.TemplateModel;
import org.sakaiproject.resource.api.template.service.ITemplateService;
import org.sakaiproject.resource.impl.forum.service.MessageForumServiceImpl;
import org.sakaiproject.resource.util.AsyncGridModel;
import org.sakaiproject.resource.util.AsyncTreeModel;
import org.sakaiproject.resource.util.CacheUtil;
import org.sakaiproject.resource.util.CodeTable;
import org.sakaiproject.resource.util.Constants;
import org.sakaiproject.resource.util.CourseUtil;
import org.sakaiproject.resource.util.FunctionRegister;
import org.sakaiproject.resource.util.JsonBuilder;
import org.sakaiproject.resource.util.SpringContextUtil;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.site.api.ToolConfiguration;
import org.sakaiproject.tool.api.ToolManager;
import org.sakaiproject.user.api.UserDirectoryService;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.bupticet.paperadmin.tool.Helper;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 课程空间（教师）
 * 
 * @author Administrator
 * 
 */
public class CourseSpaceAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ICourseService courseService;
	private IStudyService studyService;
	private SecurityService securityService = (SecurityService) ComponentManager.get(SecurityService.class);
	private static Log logger = LogFactory.getLog(CourseSpaceAction.class);
	private static Log loggerManage = LogFactory.getLog("sysManageResourceR");

	private Boolean flag = true;

	private IFileService fileService;

	private ITemplateService templateService;

	/**
	 * 树节点id
	 */
	private String node;
	/**
	 * 节点类型
	 */
	private String nodeType;
	/**
	 * 上级节点id
	 */
	private String parent;
	/**
	 * 上级节点类型
	 */
	private String parentType;
	/**
	 * 节点序号
	 */
	private String nodeIdx;
	/**
	 * 是否显示隐藏节点
	 */
	private String showHide;
	/**
	 * 只显示计分活动
	 */
	private String showScore;
	/**
	 * 教务系统课程id
	 */
	// private String jwCourseid;
	/**
	 * 作业考试系统课程id
	 */
	private String exCourseid;

	/**
	 * 资源系统课程Id
	 */
	private String resCourseid;

	/**
	 * 课程id
	 */
	private String courseId;
	/**
	 * (设置分数所占平时成绩百分比页面)最高印象分--不再使用 lzp
	 */
	private String impressionScore;
	/**
	 * (设置分数所占平时成绩百分比页面)加分方式-- 不再使用 lzp
	 */
	private String impressionType;
	/**
	 * lzp(暂时不再使用) 选修模块最少选修个数
	 */
	private String minModNum;
	/**
	 * 播放模板
	 */
	private String playerTemplate;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 说明
	 */
	private String description;
	/**
	 * 课程引导
	 */
	private String courseGuide;
	/**
	 * 教学目标
	 */
	private String teachGoal;
	/**
	 * 重点难点
	 */
	private String keyAndDifficute;
	/**
	 * 教学方法
	 */
	private String teachMethod;
	/**
	 * 学习导航
	 */
	private String learnNavigation;
	/**
	 * 视频图片地址
	 */
	private String videoPicPath;
	/**
	 * 视频地址
	 */
	private String videoUrl;
	
	/**
	 * 视频大小
	 */
	private Long videoSize;
	
	/**
	 * 视频时长
	 */
	private String videoTime;
	/**
	 * 创建人
	 */
	private String createdByFname;
	/**
	 * 必修or选修
	 */
	private String required;
	/**
	 * 是否有开启条件--不再使用 lzp
	 */
	private String prerequids;
	/**
	 * (模块)概要目标
	 */
	private String learnObj;
	/**
	 * (模块)关键字
	 */
	private String keywords;
	/**
	 * (模块)下一步
	 */
	private String whatsNext;
	/**
	 * 扩展属性选定值
	 */
	private String extAttr;
	/**
	 * (模块)页的最少选修个数-已不再使用 lzp
	 */
	private String minSecNum;
	/**
	 * (模块)学习天数
	 */
	private String studyDay;
	/**
	 * (页)文本内容
	 */
	private String textualContent;
	/**
	 * (页)视频内容
	 */
	private String videoContent;
	/**
	 * (页)音频内容
	 */
	private String audioContent;
	/**
	 * (页)学习时长
	 */
	private String studyTime;
	/**
	 * 拖拽方式：append，above，below
	 */
	private String point;
	/**
	 * 目标节点id
	 */
	private String destId;
	/**
	 * 目标节点类型
	 */
	private String destType;
	/**
	 * 活动名称
	 */
	private String name;
	/**
	 * 活动是否计算平时成绩
	 */
	private String isCaculateScore;
	/**
	 * 活动同一策略是否用同一试卷
	 */
	private String samepaper;
	/**
	 * 活动策略id
	 */
	private String schemaId;
	/**
	 * 活动总分
	 */
	private String totalScore;
	/**
	 * 活动通过分
	 */
	private String masteryScore;
	/**
	 * 重做作业的最小时间间隔-不再使用 lzp
	 */
	private String minTimeInterval;
	
	/**
	 * 作业开放开始时间
	 */
	private Date startOpenDate;
	
	/**
	 * 作业开放结束时间
	 */
	private Date endOpenDate;

	/**
	 * 讨论区id
	 */
	private String areaId;

	/**
	 * 论坛主题id
	 */
	private String topicId;
	/**
	 * 论坛id
	 */
	private String forumId;
	/**
	 * (设置分数所占平时成绩百分比页面)总的百分比（总计）
	 */
	private String ratio;

	protected byte buf[] = new byte[1024];
	/**
	 * 资源路径
	 */
	private String fileUrl;
	/**
	 * (页)编辑内容
	 */
	private String launchData;
	/**
	 * 要修改的节点id
	 */
	private String moduleId;
	/**
	 * 课件资源id
	 */
	private String wareId;
	/**
	 * 必修总个数
	 */
	private String reqNum;
	/**
	 * 子节点类型
	 */
	private String childType;
	/**
	 * 兄弟节点个数
	 */
	private String brotherNum;

	private String testId;
	/**
	 * (设置分数所占平时成绩百分比页面)修改后的百分比
	 */
	private String addOne;
	/**
	 * (设置分数所占平时成绩百分比页面)的原始百分比
	 */
	private String addTwo;
	/**
	 * 是否在学生界面显示通过状态--不再使用 lzp
	 */
	private String showStuStatus;
	/**
	 * 学习中心ID
	 */
	private String eduCenterId;
	/**
	 * 学生姓名
	 */
	private String stuName;
	/**
	 * 学生学号
	 */
	private String stuNum;
	private String start;
	/**
	 * 所选择的学生的学习记录ID
	 */
	private String selectionIds;
	/**
	 * 教师为学生增加的印象分数
	 */
	private String impScore;
	/**
	 * 学生平时成绩
	 */
	private String scores;

	/**
	 * 操作标记
	 */
	private String action;
	
	/**试卷生成方式*/
	private String buildType;
	
	/**试卷生成数量*/
	private Long buildNum;

	public String getScores() {
		return scores;
	}

	public void setScores(String scores) {
		this.scores = scores;
	}

	public String getSelectionIds() {
		return selectionIds;
	}

	public void setSelectionIds(String selectionIds) {
		this.selectionIds = selectionIds;
	}

	public String getImpScore() {
		return impScore;
	}

	public void setImpScore(String impScore) {
		this.impScore = impScore;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEduCenterId() {
		return eduCenterId;
	}

	public void setEduCenterId(String eduCenterId) {
		this.eduCenterId = eduCenterId;
	}

	public String getStuName() {
		return stuName;
	}

	public void setStuName(String stuName) {
		this.stuName = stuName;
	}

	public String getStuNum() {
		return stuNum;
	}

	public void setStuNum(String stuNum) {
		this.stuNum = stuNum;
	}

	public String getShowStuStatus() {
		return showStuStatus;
	}

	public void setShowStuStatus(String showStuStatus) {
		this.showStuStatus = showStuStatus;
	}

	public String getAddOne() {
		return addOne;
	}

	public void setAddOne(String addOne) {
		this.addOne = addOne;
	}

	public String getAddTwo() {
		return addTwo;
	}

	public void setAddTwo(String addTwo) {
		this.addTwo = addTwo;
	}

	public String getForumId() {
		return forumId;
	}

	public void setForumId(String forumId) {
		this.forumId = forumId;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getChildType() {
		return childType;
	}

	public void setChildType(String childType) {
		this.childType = childType;
	}

	public String getReqNum() {
		return reqNum;
	}

	public void setReqNum(String reqNum) {
		this.reqNum = reqNum;
	}

	public String getWareId() {
		return wareId;
	}

	public void setWareId(String wareId) {
		this.wareId = wareId;
	}

	public String getLaunchData() {
		return launchData;
	}

	public void setLaunchData(String launchData) {
		this.launchData = launchData;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public String getRatio() {
		return ratio;
	}

	public void setRatio(String ratio) {
		this.ratio = ratio;
	}

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	/*
	 * public String getJwCourseid() { return jwCourseid; }
	 * 
	 * public void setJwCourseid(String jwCourseid) { this.jwCourseid =
	 * jwCourseid; }
	 */

	public String getExCourseid() {
		return exCourseid;
	}

	public void setExCourseid(String exCourseid) {
		this.exCourseid = exCourseid;
	}

	public String getResCourseid() {
		return resCourseid;
	}

	public void setResCourseid(String resCourseid) {
		this.resCourseid = resCourseid;
	}

	public String getCourseId() {
		if (StringUtils.isBlank(courseId)) {
			return ServletActionContext.getRequest().getSession().getAttribute("courseId").toString();
		} else {
			return courseId;
		}
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public ICourseService getCourseService() {
		return courseService;
	}

	public void setCourseService(ICourseService courseService) {
		this.courseService = courseService;
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
		return (ForumService) SpringContextUtil.getBean("jforumService");
	}

	private SiteService siteService = (SiteService) ComponentManager.get(SiteService.class);

	private ToolManager toolManager = (ToolManager) ComponentManager.get(ToolManager.class);

	/** sakai业务逻辑类 */
	private UserDirectoryService userDirectoryService = (UserDirectoryService) ComponentManager
			.get(UserDirectoryService.class);

	public String execute() throws Exception {
		Site curSite = this.getCurrentSite();
		String siteRef = siteService.siteReference(curSite.getId());
		String currentUser = this.getCurrentUserId();
		// 基于站点Id,获取对应的课程信息
		MeleteCourseModel course = checkCourseExist(curSite);

		if (securityService.unlock(currentUser, FunctionRegister.COURSE_SPACE_PERM, siteRef)) {// 教师
			try {
				String userName = userDirectoryService.getCurrentUser().getLastName()
						+ userDirectoryService.getCurrentUser().getFirstName();
				// this.createCkPath();

				// 考试系统对应的课程id
				ServletActionContext.getRequest().getSession().setAttribute("exCourseId", course.getExCourseid());
				ServletActionContext.getRequest().getSession().setAttribute("resCourseId", course.getResCourseid());

				ServletActionContext.getRequest().getSession().setAttribute("userName", userName);
				ServletActionContext.getRequest().getSession()
						.setAttribute("userEId", userDirectoryService.getCurrentUser().getEid());
				if (course != null) {
					ServletActionContext.getRequest().getSession().setAttribute("courseId", course.getId());
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				StringWriter sw = new StringWriter();  
			    e.printStackTrace(new PrintWriter(sw, true)); 
				loggerManage.error("into coursemanage error- " + siteRef + ":" + sw.toString());
			}
			return "page";
		} else if (securityService.unlock(currentUser, FunctionRegister.STUDY_SPACE_PERM, siteRef)) {// 学生
			// 登陆用户Id（学生Id）
			String studentId = this.getCurrentUserId();
			try {
				// 获取学习记录(用于查询模块记录或节点记录)
				MeleteStudyRecordModel studyRecord = studyService.getStudyRecordById(course.getId(), studentId);
				if (studyRecord == null) {
					// 第一次加载课程树时保存学习记录信息
					studyRecord = new MeleteStudyRecordModel();
					studyRecord.setCourseId(course.getId()); // 课程ID
					studyRecord.setStudentId(studentId); // 学生ID
					studyRecord.setScore(0f); // 课程成绩
					studyRecord.setLessonStatus(Long.valueOf(CodeTable.passStatusNo)); // 课程通过状态
					studyRecord.setStartStudyTime(new Date()); // 学习开始时间
				}else if(studyRecord.getStartStudyTime() == null){
					Date date = studyService.getSectionFirstStartStudyTime(studyRecord.getStudyrecordId().toString());
					studyRecord.setStartStudyTime(date);
					studyService.updateModel(studyRecord);
				}
				ServletActionContext.getRequest().setAttribute("lessonStatus",
						String.valueOf(studyRecord.getLessonStatus()).equals(CodeTable.passStatusYes) ? "通过" : "未通过");
				ServletActionContext.getRequest().setAttribute("score", String.valueOf(studyRecord.getScore()));
				// 用于是否查询历史记录的标记
				ServletActionContext.getRequest().setAttribute("flag", flag);
			} catch (Exception e) {
				e.printStackTrace();
				StringWriter sw = new StringWriter();  
			    e.printStackTrace(new PrintWriter(sw, true));
				loggerManage.error("into coursestudy error- " + siteRef + ":" + sw.toString());
			}
			return "studyManage";
		} else {
			addActionError("无权限");
			return ERROR;
		}
	}

	/**
	 * 检查课程是否已经创建
	 * 
	 * @param site
	 * @return
	 * @throws Exception
	 */
	private MeleteCourseModel checkCourseExist(Site site) throws Exception {
		MeleteCourseModel course = courseService.getCourseBySiteId(site.getId());

		// 课程信息不存在，则插入一条课程信息
		if (course == null) {
			course = new MeleteCourseModel();
			course.setId(site.getId());// 为兼容阶段作业工具，将课程id设置为siteId
			course.setSiteId(site.getId());// 站点id
			course.setTitle(site.getTitle());// 课程标题
			course.setStatus(new Long(CodeTable.hide));// 初始化状态，需要进一步设置详细信息
			String creator = getCurrentUserId();// 当前登录用户
			course.setCreator(creator);
			// 默认加分方式
			course.setPlayerTemplate(TemplateModel.TEMPLATE_NAME_DEFAULT);// 默认播放器
			courseService.saveCourse(course);

			String servicePath = CourseUtil.getServicePath();
			String relaPath = Constants.SECTION_PATH + "/" + course.getId() + "/agency.htm";
			String filePath = servicePath + "/" + relaPath;
			File htmlFile = new File(filePath);
			String cont = "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\r\n";
			cont += "<script>\r\n";
			cont += "var idx = window.location.toString().split(\"?\")[1];\r\n";
			cont += "window.parent.frames[0].helper(idx);\r\n";
			cont += "</script>";
			CourseUtil.createFile(htmlFile);
			FileUtils.writeStringToFile(htmlFile, cont, "UTF-8");
			String coursePath = CourseUtil.getCoursePathByCourseId(course.getId());
			CourseUtil.createManifest(course.getId());// 创建课程目录下的课件描述文件imsmanifest.xml
			logger.debug("课件描述文件创建完成，创建目录:" + coursePath);
		}

		// 检查课程缓存
		CacheUtil.getInstance().getCacheOfCourse(site.getId());
		return course;
	}

	/**
	 * 为当前课程创建ck配置对应的目录（img，flash， file）下的课程目录
	 */
	private void createCkPath() throws Exception {
		Site curSite = this.getCurrentSite();
		// ck存放图片目录下当前课程对应的目录
		File ckCourseImgDir = new File(Constants.COURSEWARE_RESOURCE_PATH + Constants.COURSEWARE_RESOURCE_IMAGE_FOLDER
				+ curSite.getTitle() + "/");
		// ck存放flash目录下当前课程对应的目录
		File ckCourseFlashDir = new File(Constants.COURSEWARE_RESOURCE_PATH
				+ Constants.COURSEWARE_RESOURCE_FLASH_FOLDER + curSite.getTitle() + "/");
		// ck存放其他文件目录下当前课程对应的目录
		File ckCourseFileDir = new File(Constants.COURSEWARE_RESOURCE_PATH + Constants.COURSEWARE_RESOURCE_FILE_FOLDER
				+ curSite.getTitle() + "/");
		// 若以上目录不存在则创建目录
		if (!ckCourseImgDir.exists()) {
			ckCourseImgDir.mkdirs();
			logger.debug("课程在ckfinder下对应目录：" + ckCourseImgDir.getAbsolutePath() + "  创建完成");
		}
		if (!ckCourseFlashDir.exists()) {
			ckCourseImgDir.mkdirs();
			logger.debug("课程在ckfinder下对应目录：" + ckCourseFlashDir.getAbsolutePath() + "  创建完成");
		}
		if (!ckCourseFileDir.exists()) {
			ckCourseImgDir.mkdirs();
			logger.debug("课程在ckfinder下对应目录：" + ckCourseImgDir.getAbsolutePath() + "  创建完成");
		}
	}

	/**
	 * 课程空间管理：页面初始化
	 * 
	 * @return
	 * @throws Exception
	 */
	public String manageInit() throws Exception {
		try {
			HttpSession session = ServletActionContext.getRequest().getSession();
			logger.debug("------manageInit()----sessionid--------" + session.getId());
			Site site = this.getCurrentSite();
			MeleteCourseModel course = courseService.getCourseBySiteId(site.getId());

			// 检查课程描述文件
			String coursePath = CourseUtil.getCoursePathByCourseId(course.getId());
			File manifestFile = new File(coursePath, "imsmanifest.xml");
			if (manifestFile.getParentFile().exists()) {// 若课程目录存在
				if (!manifestFile.exists()) {// 描述文件不存在
					CourseUtil.createManifest(course.getId());// 创建课程目录下的课件描述文件imsmanifest.xml
					logger.debug("课件描述文件创建完成，创建目录:" + coursePath);
				}
			} else {
				logger.error("课程目录：" + manifestFile.getParentFile().getAbsoluteFile() + "   不存在");
			}
			ServletActionContext.getRequest().getSession().setAttribute("toolId", toolManager.getCurrentPlacement().getId());
			ServletActionContext.getRequest().getSession().setAttribute("siteId", site.getId());
			ServletActionContext.getRequest().getSession().setAttribute("courseId", course.getId());
			ServletActionContext.getRequest().getSession().setAttribute("forumType", getForumService().getName());// 使用的那个论坛
			// 获取模板列表
			List<TemplateModel> tpls = templateService.getListForUse();
			if (tpls != null) {
				JSONArray objects = JSONArray.fromObject(tpls);
				String tplsJSON = objects.toString();
				ServletActionContext.getRequest().getSession().setAttribute("tplsJSON", tplsJSON);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return "manage";
	}

	public String checkInit() {
		return "check";
	}
	
	public String scoreInit() {
		return "score";
	}

	private String getCurrentUserId() {
		return userDirectoryService.getCurrentUser().getId();
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

	/**
	 * 加载课程树
	 * 
	 * @return
	 * @throws Exception
	 */
	public String loadCourse() throws Exception {
		try {
			// 定义查询出的所有节点集合
			List<Object> nodeList = new ArrayList<Object>();
			// 定义要返回的节点集合
			List<AsyncTreeModel> data = new ArrayList<AsyncTreeModel>();
			// 获取站点id
			Site site = this.getCurrentSite();
			String siteId = site.getId();
			if (node.equals("0")) {// 加载根节点，显示课程名称节点
				// 根据站点id获取该课程信息
				MeleteCourseModel course = courseService.getCourseBySiteId(siteId);
				nodeList.add(course);// 加入到节点集合中
			} else if (nodeType.equals(CodeTable.course)) {// 节点类型是“课程”
				// 查询一级模块节点
				List<MeleteModuleModel> moduleList = courseService.getModuleByCourseId(node, new Boolean(showHide));
				nodeList.addAll(moduleList);
			} else if (nodeType.equals(CodeTable.module)) {// 节点类型是“模块”

				if (childType.equals(CodeTable.module)) {
					// 查询当前模块的下级模块
					List<MeleteModuleModel> moduleList = courseService.getModuleByParentId(new Long(node), new Boolean(
							showHide));
					nodeList.addAll(moduleList);
				} else if (childType.equals(CodeTable.section)) {
					// 查询当前模块所包含的页
					List<MeleteSectionModel> sectionList = courseService.getSectionByModuleId(new Long(node), new Long(
							CodeTable.normal));
					nodeList.addAll(sectionList);
				}
				// 查询当前模块所包含的前测
				List<MeleteSelfTestModel> selfList = courseService.getSelftestByModuleId(new Long(node), new Boolean(
						showScore));
				nodeList.addAll(selfList);
				// 查询当前模块所包含的作业
				List<MeleteTestModel> testList = courseService
						.getTestByModuleId(new Long(node), new Boolean(showScore));
				nodeList.addAll(testList);
				// 查询当前模块所包含的讨论
				List<MeleteForumModel> forumList = courseService.getForumByModuleId(new Long(node), new Boolean(
						showScore));
				nodeList.addAll(forumList);

			} else if (nodeType.equals(CodeTable.section)) {// 节点类型是“页”
				// 查询当前页所包含的前测
				List<MeleteSelfTestModel> selfList = courseService.getSelftestBySectionId(new Long(node), new Boolean(
						showScore));
				nodeList.addAll(selfList);
				// 查询当前页所包含的作业
				List<MeleteTestModel> testList = courseService.getTestBySectionId(new Long(node),
						new Boolean(showScore));
				nodeList.addAll(testList);
				// 查询当前页所包含的讨论
				List<MeleteForumModel> forumList = courseService.getForumBySectionId(new Long(node), new Boolean(
						showScore));
				nodeList.addAll(forumList);
			}
			int j = 1;
			for (int i = 0; i < nodeList.size(); i++) {// 遍历节点集合
				Object obj = nodeList.get(i);
				if (obj.getClass() == MeleteCourseModel.class) {// 是课程节点
					MeleteCourseModel course = (MeleteCourseModel) obj;
					AsyncTreeModel asyncTree = new AsyncTreeModel();
					String courseId = course.getId();// 设置树节点的id
					asyncTree.setId(courseId);
					asyncTree.setText(course.getTitle());// 设置树节点的text

					String status = course.getStatus().toString();// 获取状态

					boolean leaf = true;
					Long scoreNum = new Long(0);// 需要计算平时成绩的活动的总个数(作业、前测、论坛、资源)
					if (status.equals(CodeTable.normal)) {
						// 判断是否包含模块，没有模块则为叶子节点
						leaf = courseService.leafModuleByCourseId(courseId, new Boolean(showHide));
						if (!leaf) {// 有子节点
							// 获取需要计算平时成绩的活动的总个数
							scoreNum = courseService.countScoreActNum(courseId);
						}
					}
					asyncTree.setLeaf(leaf);

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("type", CodeTable.course);// 节点类型：课程
					map.put("studyTimeShow", "");// 学习时间
					map.put("mainAttr", "");// 主要属性

					String requirement = course.getRequirement();

					map.put("courseId", courseId);// 课程id
					map.put("status", status);// 状态
					// map.put("jwCourseid", course.getJwCourseid());// 教务系统课程id
					map.put("exCourseid", course.getExCourseid());// 考试系统课程id
					map.put("resCourseid", course.getResCourseid());// 资源系统课程id
					map.put("playerTemplate", course.getPlayerTemplate());// 播放模板
					map.put("requirement", requirement);// 通过条件
					map.put("title", course.getTitle());
					map.put("scoreNum", scoreNum);// 计分活动总数
					asyncTree.setAttributes(map);

					asyncTree.setIcon(CodeTable.icoCourse);

					data.add(asyncTree);
				} else if (obj.getClass() == MeleteModuleModel.class) {// 是模块节点
					MeleteModuleModel module = (MeleteModuleModel) obj;
					Long id = module.getId();
					String title = module.getTitle();
					String status = module.getStatus().toString();// 模块状态
					String selftestId = module.getModuleSelftest();// 前测id
					String required = module.getRequired().toString();// 必修or选修
					String childType = module.getChildType();
					String requirement = module.getRequirement();// 通过条件中文说明
					String next = module.getWhatsNext();// 下一步

					AsyncTreeModel asyncTree = new AsyncTreeModel();// 定义树节点对象
					asyncTree.setId(id.toString());
					Map<String, Object> map = new HashMap<String, Object>();
					boolean leaf = true;// 是否叶子节点
					if (childType == null) {
						leaf = true;
					} else if (childType.equals(CodeTable.module)) {
						leaf = false;
					} else {
						leaf = false;
					}
					// asyncTree.setExpanded(false);
					String showIdx = ((nodeIdx == null || nodeIdx.equals("")) ? "" : (nodeIdx + "."));
					if (status.equals(CodeTable.normal)) {// 正常
						showIdx += j;
						j++;
						if (leaf) {// 是否有作业
							leaf = courseService.leafTestByModuleId(id, new Boolean(showScore));
						}
						if (leaf) {// 是否有讨论
							leaf = courseService.leafForunByModuleId(id, new Boolean(showScore));
						}
						if (leaf) {// 是否有前测
							leaf = selftestId == null ? true : false;
						}
						asyncTree.setIcon(CodeTable.icoModule);
						asyncTree.setText(showIdx + " " + title);

						map.put("studyTimeShow", module.getStudyDay() + "分钟");// 学习时间
						map.put("mainAttr", (required.equals(CodeTable.required)) ? CodeTable.getRequiredIco()
								: CodeTable.getElectiveIco());// 主要属性
					} else {// 隐藏
						leaf = true;
						asyncTree.setIcon(CodeTable.icoHide);
						String showTitle = "<font style=color:gray>" + title + "</font>";
						asyncTree.setText(showTitle);

						map.put("studyTimeShow", "<font style=color:gray>" + module.getStudyDay() + "天" + "</font>");// 学习时间
						map.put("mainAttr",
								"<font style=color:gray>"
										+ ((required.equals(CodeTable.required)) ? CodeTable.getRequiredIco()
												: CodeTable.getElectiveIco()) + "</font>");// 主要属性
					}
					asyncTree.setLeaf(leaf);
					map.put("childType", childType);// 下级节点类型
					map.put("showIdx", showIdx);// 显示序号,如1.1,1.2.3
					map.put("title", title);// 节点标题
					map.put("required", required);// 必修or选修
					map.put("studyDay", module.getStudyDay());// 学习时长
					map.put("learnObj", module.getLearnObj());// 学习目标
					map.put("keywords", module.getKeywords());// 关键字
					map.put("parentId", module.getParentId());// 上级id
					map.put("whatsNext", next);// 下一步
					map.put("idx", module.getIdx());// 节点序号
					map.put("requirement", requirement);// 通过条件中文说明
					map.put("selftest", selftestId);// 前测id
					map.put("type", CodeTable.module);// 节点类型：模块
					map.put("courseId", module.getCourseId());// 所属课程id
					map.put("status", status);// 节点状态
					asyncTree.setAttributes(map);
					data.add(asyncTree);
				} else if (obj.getClass() == MeleteSectionModel.class) {// 是页节点
					MeleteSectionModel section = (MeleteSectionModel) obj;
					AsyncTreeModel asyncTree = new AsyncTreeModel();
					asyncTree.setAllowDrop(false);
					Long id = section.getId();
					asyncTree.setId(id.toString());

					String showIdx = nodeIdx + "." + j;
					j++;
					String title = section.getTitle();

					asyncTree.setText(showIdx + " " + title);

					String required = section.getRequired().toString();
					String requirement = section.getRequirement();

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("title", title);// 页标题
					map.put("required", required);// 必修
					map.put("textualContent", section.getTextualContent());// 是否包含文本
					map.put("videoContent", section.getVideoContent());// 是否包含视频
					map.put("audioContent", section.getAudioContent());// 是否包含音频
					map.put("description", section.getDescription());// 说明描述
					map.put("requirement", requirement);// 通过条件中文说明
					map.put("studyTime", section.getStudyTime());// 学习时长
					map.put("moduleId", section.getModuleId());// 模块id
					map.put("selftest", section.getSectionSelftest());// 前测id
					map.put("status", section.getStatus().toString());// 状态
					map.put("idx", section.getIdx());// 页序号
					map.put("path", section.getPath());// 路径
					map.put("type", CodeTable.section);// 节点类型：页
					map.put("studyTimeShow", section.getStudyTime() + "分钟");// 学习时间
					map.put("mainAttr",
							required.equals(CodeTable.required) ? CodeTable.getRequiredIco() : CodeTable
									.getElectiveIco());// 主要属性

					asyncTree.setAttributes(map);

					boolean leaf = courseService.leafTestBySectionId(id, new Boolean(showScore));// 是否有作业
					if (leaf) {
						leaf = courseService.leafForumBySectionId(id, new Boolean(showScore));// 是否有讨论
					}
					if (leaf) {
						leaf = section.getSectionSelftest() == null ? true : false;// 是否有前测
					}
					asyncTree.setLeaf(leaf);
					asyncTree.setIcon(CodeTable.icoSection);

					data.add(asyncTree);
				} else if (obj.getClass() == MeleteTestModel.class) {// 是作业节点
					MeleteTestModel test = (MeleteTestModel) obj;
					AsyncTreeModel asyncTree = new AsyncTreeModel();
					asyncTree.setAllowDrop(false);
					asyncTree.setAllowDrag(false);
					asyncTree.setId(test.getId().toString());// 作业id
					asyncTree.setText(test.getName());// 作业名称

					String qtip = "";// 定义提示信息
					qtip += "建议通过分数百分比：" + (test.getMasteryScore() == null ? "无" : test.getMasteryScore()) + "<br>";
					asyncTree.setQtip(qtip);// 设置提示信息

					asyncTree.setLeaf(true);

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("name", test.getName());// 作业名称
					map.put("requirement", test.getRequirement());// 通过条件中文说明
					map.put("samepaper", test.getSamepaper());// 使用同一策略
					map.put("schemaId", test.getSchemaId());// 策略id
					map.put("totalScore", test.getTotalScore());// 总分
					map.put("masteryScore", test.getMasteryScore());// 建议通过百分比
					// 最小时间间隔
					map.put("isCaculateScore", test.getIsCaculateScore());// 是否计算平时成绩
					map.put("type", CodeTable.test);// 节点类型：作业
					map.put("startOpenDate", test.getStartOpenDate());//开放开始时间
					map.put("endOpenDate", test.getEndOpenDate());//开放结束时间
					map.put("buildType", test.getBuildType());//生成试卷方式
					map.put("buildNum", test.getBuildNum());// 生成试卷数 
					map.put("studyTimeShow", "");// 学习时间
					map.put("minTimeInterval", test.getMinTimeInterval());// 学习时间
					map.put("mainAttr", qtip);// 主要属性
					asyncTree.setAttributes(map);
					asyncTree.setIcon(CodeTable.icoTest);
					data.add(asyncTree);
				} else if (obj.getClass() == MeleteForumModel.class) {// 是讨论节点
					MeleteForumModel forum = (MeleteForumModel) obj;
					AsyncTreeModel asyncTree = new AsyncTreeModel();
					asyncTree.setAllowDrop(false);
					asyncTree.setAllowDrag(false);
					asyncTree.setId(forum.getId().toString());// 论坛id
					asyncTree.setText(forum.getName());

					String qtip = "";// 定义提示信息
					qtip += "建议发帖次数：" + (forum.getRequirement() == null ? "无" : forum.getRequirement()) + "<br>";
					asyncTree.setQtip(qtip);// 设置提示信息

					asyncTree.setLeaf(true);

					String areaId = forum.getAreaId(); // 讨论区id
					String topicId = forum.getTopicId();// 主题id
					String forumId = forum.getForumId();// 论坛id

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("name", forum.getName());// 论坛名称
					map.put("requirement", forum.getRequirement());// 通过条件中文说明
					map.put("isCaculateScore", forum.getIsCaculateScore());// 是否计算平时成绩
					map.put("areaId", areaId);// 讨论区id
					map.put("topicId", topicId);// 帖子主题id
					map.put("forumId", forumId);// 论坛id
					map.put("type", CodeTable.forum);// 节点类型：讨论
					map.put("studyTimeShow", "");// 学习时间
					map.put("mainAttr", qtip);// 主要属性
					asyncTree.setAttributes(map);
					asyncTree.setIcon(CodeTable.icoForum);
					data.add(asyncTree);
				} else if (obj.getClass() == MeleteSelfTestModel.class) {// 是前测节点
					MeleteSelfTestModel self = (MeleteSelfTestModel) obj;
					AsyncTreeModel asyncTree = new AsyncTreeModel();
					asyncTree.setAllowDrop(false);
					asyncTree.setAllowDrag(false);
					asyncTree.setId(self.getId().toString());// 前测id
					asyncTree.setText(self.getName());

					String qtip = "";// 定义提示信息
					qtip += "建议通过分数百分比：" + (self.getMasteryScore() == null ? "无" : self.getMasteryScore()) + "<br>";
					asyncTree.setQtip(qtip);// 设置提示信息

					asyncTree.setLeaf(true);

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("name", self.getName());// 前测名称
					map.put("belongType", self.getBelongType());// 所属类型
					map.put("requirement", self.getRequirement());// 通过条件中文说明
					map.put("samepaper", self.getSamepaper());// 使用同一策略
					map.put("schemaId", self.getSchemaId());// 策略id
					map.put("totalScore", self.getTotalScore());// 总分
					map.put("masteryScore", self.getMasteryScore());// 建议通过百分比
					map.put("isCaculateScore", self.getIsCaculateScore());// 是否计算平时成绩
					map.put("idx", self.getIdx());// 前测序号
					map.put("type", CodeTable.selftest);// 节点类型：前测
					map.put("studyTimeShow", "");// 学习时间
					map.put("buildType", self.getBuildType());//生成试卷方式 
					map.put("buildNum", self.getBuildNum());// 生成试卷数 
					map.put("mainAttr", qtip);// 主要属性
					asyncTree.setAttributes(map);
					asyncTree.setIcon(CodeTable.icoSelftest);
					data.add(asyncTree);
				}
			}

			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			String json = JsonBuilder.builderAsyncTreeJson(data);
			response.getWriter().println(json);

			return null;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 设置课程信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String saveCourse() throws Exception {
		String res = "{success: true}";
		try {
			MeleteCourseModel course = courseService.getCourseById(courseId);
			// course.setJwCourseid(jwCourseid);
			course.setExCourseid(exCourseid);
			course.setResCourseid(resCourseid);
			ServletActionContext.getRequest().getSession().setAttribute("exCourseId", exCourseid);
			ServletActionContext.getRequest().getSession().setAttribute("resCourseId", resCourseid);
			if (playerTemplate != null) {
				course.setPlayerTemplate(playerTemplate);
			}
			course.setStatus(new Long(CodeTable.normal));// 有效状态
			courseService.updateCourse(course);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			res = "{success: false}";
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().println(res);
		return null;
	}

	/**
	 * 加载课件资源下拉列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String loadCoursewareBox() throws Exception {
		try {
			String json = courseService.getCoursewareBox();
			HttpServletResponse response = ServletActionContext.getResponse();
			response.getWriter().println(json);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 获取策略列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String loadSchemaBox() throws Exception {
		try {
			// int cid = new Integer(this.exCourseid).intValue();
			List<SchemaNewVO> ls = getSchemaListFromExam(this.exCourseid);// SchemaToolUtil.getSchemaTOListByCourseId(cid);
			StringBuffer sb = new StringBuffer("[");
			if (ls != null && ls.size() > 0) {
				for (int i = 0; i < ls.size(); i++) {
					SchemaNewVO schema = ls.get(i);
					int tid = schema.getnSchemaID();
					String tn = schema.getStrSchemaName();
					// SchemaTO schemaTO = SchemaToolUtil.getSchemaTO(tid);
					int score = 0;
					SchemaContentNew sc = schema.getcSchemaCont(); // schemaTO.cSchemaCont;
					if (sc != null) {
						PaperAttributeNew pa = sc.getcFullMark();
						if (pa != null) {
							score = pa.getnDemand();
						}
					}
					sb.append("['").append(tid).append("','").append(tn).append("','" + score + "'],");
				}
			}
			sb = sb.deleteCharAt(sb.length() - 1).append("]");

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
	 * 通过接口获取题库策略
	 * 
	 * @param examCourseId
	 * @return
	 * @throws Exception
	 */
	private List<SchemaNewVO> getSchemaListFromExam(String examCourseId) throws Exception {
		// 从新接口获取策略
		String key = CourseUtil.getUserKeyForExamSys(userDirectoryService.getCurrentUser().getEid());
		String urlStr = Constants.OUTSYS_EXAM_URL_BASE
				+ Constants.OUTSYS_EXAM_URL_SCHEMA.replace("${courseId}", examCourseId).replace("${key}", key);
		URL url = new URL(urlStr);
		InputStream inputStream = url.openStream();

		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("utf-8")));
		StringBuilder resp_result = new StringBuilder();
		for (String s = reader.readLine(); s != null; s = reader.readLine()) {
			resp_result.append(s).append("\n");
		}
		reader.close();
		inputStream.close();

		List<SchemaNewVO> objs = null;
		JSONArray jsonArray = (JSONArray) JSONSerializer.toJSON(resp_result.toString());
		if (jsonArray != null) {
			objs = new ArrayList<SchemaNewVO>();
			List list = (List) JSONSerializer.toJava(jsonArray);
			for (Object o : list) {
				JSONObject jsonObject = JSONObject.fromObject(o);
				SchemaNewVO obj = (SchemaNewVO) JSONObject.toBean(jsonObject, SchemaNewVO.class);
				objs.add(obj);
			}
		}

		return objs;
	}

	/**
	 * 加载教务系统课程列表
	 * 
	 * @return
	 * @throws Exception
	 */
	// public String loadJwCourseBox() throws Exception {
	// try {
	// List<Map> list = CourseUtil.getQuickStartCoruse();
	// StringBuffer sb = new StringBuffer("[");
	// for (Map map : list) {
	// sb.append("['").append(map.get("ID")).append("','").append(map.get("cnname")).append("'],");
	// }
	// sb = sb.deleteCharAt(sb.length() - 1).append("]");
	// HttpServletResponse response = ServletActionContext.getResponse();
	// response.getWriter().println(sb.toString());
	// return null;
	// } catch (Exception e) {
	// e.printStackTrace();
	// logger.error(e.getMessage(), e);
	// throw e;
	// }
	// }

	/**
	 * 加载作业系统课程列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String loadExamCourseBox() throws Exception {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			String key = CourseUtil.getUserKeyForExamSys(userDirectoryService.getCurrentUser().getEid());
			String urlStr = Constants.OUTSYS_EXAM_URL_BASE + Constants.OUTSYS_EXAM_URL_COURSE.replace("${key}", key);
			URL url = new URL(urlStr);
			InputStream inputStream = url.openStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("utf-8")));
			StringBuilder resp_result = new StringBuilder();
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				resp_result.append(s).append("\n");
			}
			reader.close();
			inputStream.close();

			// 解析返回结果
			JSONObject jsonResult = JSONObject.fromObject(resp_result.toString());
			String resultStatus = jsonResult.getString("status");
			if (!resultStatus.equals("0")) {
				response.getWriter().println(jsonResult.toString());
				return null;
			}

			StringBuffer sb = new StringBuffer("[");
			JSONArray jsonArray = (JSONArray) JSONSerializer.toJSON(jsonResult.getString("courses"));
			if (jsonArray != null) {
				List list = (List) JSONSerializer.toJava(jsonArray);
				for (int i = 0; i < list.size(); i++) {
					JSONObject jsonObject = JSONObject.fromObject(list.get(i));
					sb.append("['").append(jsonObject.getString("id")).append("','")
							.append(jsonObject.getString("name")).append("']");
					if (i < list.size() - 1) {
						sb.append(",");
					}
				}
			}
			sb.append("]");
			response.getWriter().println(sb.toString());
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 加载资源系统课程列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String loadResCourseBox() throws Exception {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			String urlStr = CourseUtil.getResSysCourcesUrl(userDirectoryService.getCurrentUser().getEid());
			URL url = new URL(urlStr);
			InputStream inputStream = url.openStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("utf-8")));
			StringBuilder resp_result = new StringBuilder();
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				resp_result.append(s).append("\n");
			}
			reader.close();
			inputStream.close();

			// 解析返回结果
			JSONObject jsonResult = JSONObject.fromObject(resp_result.toString());
			String resultStatus = jsonResult.getString("status");
			if (!resultStatus.equals("200")) {
				response.getWriter().println(jsonResult.toString());
				return null;
			}

			StringBuffer sb = new StringBuffer("[");
			JSONArray jsonArray = (JSONArray) JSONSerializer.toJSON(jsonResult.getString("data"));
			if (jsonArray != null) {
				List list = (List) JSONSerializer.toJava(jsonArray);
				for (int i = 0; i < list.size(); i++) {
					JSONObject jsonObject = JSONObject.fromObject(list.get(i));
					sb.append("['").append(jsonObject.getString("id")).append("','")
							.append(jsonObject.getString("name")).append("']");
					if (i < list.size() - 1) {
						sb.append(",");
					}
				}
			}
			sb.append("]");
			response.getWriter().println(sb.toString());
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 加载论坛列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String loadForumBox() throws Exception {
		try {
			List<ForumModel> list = getForumService().selectAllForumByArea(areaId);
			StringBuffer sb = new StringBuffer("[");
			if (!list.isEmpty()) {
				for (ForumModel forum : list) {
					sb.append("['").append(forum.getId()).append("','").append(forum.getName()).append("'],");
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
	 * 加载论坛主题列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String loadTopicBox() throws Exception {
		try {
			List<TopicModel> list = getForumService().selectAllTopicByForum(forumId);
			StringBuffer sb = new StringBuffer("[");
			if (!list.isEmpty()) {
				for (TopicModel topic : list) {
					sb.append("['").append(topic.getTopicId()).append("','").append(topic.getTitle()).append("'],");
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
	 * 加载讨论区列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String loadAreaBox() throws Exception {
		try {
			List<AreaModel> list = getForumService().selectAllArea();
			StringBuffer sb = new StringBuffer("[");
			if (!list.isEmpty()) {
				for (AreaModel area : list) {
					sb.append("['").append(area.getId()).append("','").append(area.getName()).append("'],");
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
	 * 新增节点
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addModule() throws Exception {
		String res = "{success: true}";
		try {
			MeleteModuleModel module = new MeleteModuleModel();
			Long idx = null;
			MeleteModuleModel parentModel = null;
			if (nodeType.equals(CodeTable.course)) {// 父节点是课程节点
				module.setCourseId(node);
				// 查询兄弟节点的最大序号并生成新的序号
				idx = courseService.getMaxModuleIdxByCourseId(node) + 1;
			} else if (nodeType.equals(CodeTable.module)) {// 父节点是模块节点
				module.setParentId(new Long(node));
				module.setCourseId(getCourseId());
				// 查询兄弟节点的最大序号并生成新的序号
				idx = courseService.getMaxModuleIdxByParentId(new Long(node)) + 1;

				parentModel = (MeleteModuleModel) courseService.getModelById(MeleteModuleModel.class, new Long(node));
				parentModel.setChildType(CodeTable.module);
				courseService.editModule(parentModel);
			}
			module.setIdx(idx);// 设置序号
			module.setTitle(title);// 设置模块标题
			module.setCreatedByFname(createdByFname);// 设置创建人
			module.setCreatedByLname(createdByFname);
			module.setUserId(getCurrentUserId());// 设置创建人id
			module.setCreationDate(new Date());// 设置创建时间
			module.setDescription(description);// 设置说明
			module.setCourseGuide(courseGuide);// 设置课程引导
			module.setTeachGoal(teachGoal);// 设置教学目标
			module.setKeyAndDifficute(keyAndDifficute);// 设置重点难点
			module.setTeachMethod(teachMethod);// 设置教学方法
			module.setLearnNavigation(learnNavigation);// 设置学习导航
			module.setKeywords(keywords);// 设置关键字
			module.setLearnObj(learnObj);// 设置目标
			module.setVideoPicPath(videoPicPath);// 视频图片
			module.setVideoUrl(videoUrl);// 视频地址
			module.setVideoSize(videoSize);// 视频大小
			module.setVideoTime(videoTime);// 视频时长
			module.setVideoType(CourseUtil.parseResourceType(videoUrl));
			// 去掉最少选修个数和开启条件设置-lzp 2012.3.27
			module.setRequired(new Long(required));// 设置必修选修
			module.setStatus(new Long(CodeTable.normal));// 设置状态
			module.setStudyDay(new Long(studyDay));// 设置学习时间
			module.setAvgPassTime(0L);
			module.setWhatsNext(whatsNext);// 设置下一步
			String requirementStr = "";
			if (!studyDay.equals("0")) {
				requirementStr += "学习时长>=" + studyDay;
			}
			String ItemId = CourseUtil.getItemId();// 生成课件描述文件中的item元素的identifier属性值
			module.setModuleItemID(ItemId);
			String id = courseService.editModule(module);
			if (required.equals(CodeTable.required)) {// 必修
				if (nodeType.equals(CodeTable.course)) {// 父节点是课程节点
					courseService.updateCourseCondition(node);// 修改课程节点的通过条件
				} else if (nodeType.equals(CodeTable.module)) {// 父节点是模块节点
					courseService.updateModuleCondition(node, CodeTable.module);// 修改上级模块的通过条件
				}
			}
			this.addModuleToXML(nodeType, courseId, ItemId, parentModel, module);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			res = "{success: false , info:'" + e.getMessage() + "'}";
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().println(res);
		return null;
	}

	/**
	 * 将添加的节点加入到课程的课件描述文件中
	 * 
	 * @param nodeType
	 * @param courseId
	 * @param itemId
	 * @param pModule
	 */
	private void addModuleToXML(String nodeType, String courseId, String itemId, MeleteModuleModel pModule,
			MeleteModuleModel module) throws Exception {
		if (nodeType.equals(CodeTable.course)) {// 父节点是课程节点
			courseService.addModuleToCourse(itemId, courseId, module);
		} else if (nodeType.equals(CodeTable.module)) {// 父节点是模块节点
			courseService.addModuleToPModule(itemId, pModule, module);
		}
	}

	/**
	 * 新增页
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addSection() throws Exception {
		String res = "{success: true}";
		try {
			String userName = userDirectoryService.getCurrentUser().getLastName()
					+ userDirectoryService.getCurrentUser().getFirstName();

			MeleteSectionModel section = new MeleteSectionModel();
			section.setCourseId(getCourseId());// 课程id
			section.setModuleId(new Long(node));// 节点id
			section.setTitle(title);// 页标题
			section.setAudioContent(new Long(audioContent));// 是否包含音频
			section.setCreatedByFname(userName);// 创建人
			section.setCreatedByLname(userName);// createdByLname的值与createdByFname相同
			section.setCreationDate(new Date());// 创建时间
			section.setModificationDate(new Date());// 修改时间
			section.setDescription(description);// 说明描述
			section.setRequired(new Long(required));// 必修
			section.setStatus(new Long(CodeTable.normal));// 页状态
			section.setStudyTime(new Long(studyTime));// 学习时长
			section.setAvgPassTime(0L);
			section.setVideoPicPath(videoPicPath);// 视频图片
			section.setTextualContent(new Long(textualContent));// 是否包含文本
			section.setVideoContent(new Long(videoContent));// 是否包含视频
			section.setVideoUrl(videoUrl);
			section.setVideoType(CourseUtil.parseResourceType(videoUrl));
			section.setVideoSize(videoSize);// 视频大小
			section.setVideoTime(videoTime);// 视频时长
			section.setIdx(courseService.getMaxSectionIdxByModuleId(new Long(node)) + 1);// 页序号
			section.setPath("");// 存储路径
			section.setContentType("notype");// 为兼容原有melete中的非空字段contentType
												// 无实际作用
			String sectionItemId = CourseUtil.getItemId();// 新产生一个item的identifier属性值
			section.setSectionItemId(sectionItemId);// 关联课件描述文件中的section的item的identifier属性值
			if (!studyTime.equals("0")) {
				section.setRequirement("学习时长≥" + studyTime);// 通过条件中文说明
			} else {
				section.setRequirement(null);// 通过条件中文说明
			}
			String id = courseService.editSection(section);
			String fileName = id + ".html";

			String servicePath = CourseUtil.getServicePath();
			String relaPath = Constants.SECTION_PATH + "/" + courseId + "/" + fileName;
			String filePath = servicePath + "/" + relaPath;

			File htmlFile = new File(filePath);
			CourseUtil.createFile(htmlFile);
			FileUtils.writeStringToFile(htmlFile, launchData, "UTF-8");

			section.setPath(relaPath);
			courseService.editSection(section);
			courseService.addSectionToManifest(fileName, servicePath, node, courseId, section);// 将添加的section加入到课件课程目录中的课件描述清单文件中

			if (required.equals(CodeTable.required)) { // 是必修页，修改上级节点的通过条件
				courseService.updateModuleCondition(node, CodeTable.section);
			}

			MeleteModuleModel parentModel = (MeleteModuleModel) courseService.getModelById(MeleteModuleModel.class,
					new Long(node));
			parentModel.setChildType(CodeTable.section);
			courseService.editModule(parentModel);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			res = "{success: false , info:'" + e.getMessage() + "'}";
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().println(res);
		return null;
	}

	/**
	 * 添加作业
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addTest() throws Exception {
		String res = "{success: true}";
		try {
			logger.debug("开始添加作业");
			MeleteTestModel test = new MeleteTestModel();
			if (nodeType.equals("2")) {// 父节点是模块节点
				test.setModuleId(new Long(node));// 所属节点id
				test.setBelongType(CodeTable.belongMudole);// 所属类型
			} else if (nodeType.equals("3")) {// 父节点是页节点
				test.setSectionId(new Long(node));// 所属页id
				test.setBelongType(CodeTable.belongSection);// 所属类型
			}
			if (isCaculateScore.equals(CodeTable.IsCaculateScoreYes)) {// 需要计算平时成绩
				BigDecimal total = new BigDecimal(totalScore);
				BigDecimal mastery = new BigDecimal(masteryScore);
				BigDecimal requirement = total.multiply(mastery.divide(new BigDecimal("100")));
				test.setRequirement("≥" + requirement.setScale(0, BigDecimal.ROUND_HALF_UP).toString());// 通过条件中文说明
			}
			test.setCourseId(getCourseId());
			test.setName(name);// 作业名称
			test.setSamepaper(samepaper);// 使用同一策略
			test.setSchemaId(new Long(schemaId));// 策略id
			test.setTotalScore(new Long(totalScore));// 总分
			test.setMasteryScore(new Long(masteryScore));// 建议通过百分比
			test.setCreationDate(new Date());// 创建时间
			test.setModificationDate(new Date());// 修改时间
			test.setStartOpenDate(startOpenDate);
			test.setEndOpenDate(endOpenDate);
			test.setBuildNum(buildNum);
			test.setBuildType(buildType);
			test.setIdx(new Long(0));// 作业序号
			test.setIsCaculateScore(new Long(isCaculateScore));// 是否计算平时成绩
			test.setRatio(new Float(0));// 百分比
			test.setStatus(new Long(CodeTable.normal));// 作业状态

			if (StringUtils.isBlank(minTimeInterval)) {
				minTimeInterval = Constants.TEST_MININTERVAL;
				;
			}
			test.setMinTimeInterval(Long.parseLong(minTimeInterval));

			String testId = courseService.editTest(test, CodeTable.addType);
			logger.debug("保存作业成功");

			if (isCaculateScore.equals(CodeTable.IsCaculateScoreYes)) {// 需要计算平时成绩,设置所属节点或页的通过条件
				if (nodeType.equals("2")) {// 父节点是模块节点
					courseService.updateModuleCondition(node, null);
				} else {// 父节点是页节点
					courseService.updateSectionCondition(node, null);
				}
			}
			res = "{success: true , testId:'" + testId + "'}";

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			res = "{success: true , result:false,info:'" + e.getMessage() + "'}";
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().println(res);
		return null;
	}

	/**
	 * 发布作业
	 * 
	 * @return
	 * @throws Exception
	 */
	public String publishTest() throws Exception {
		try {
			// 生成试卷库
			/*
			 * String count = Constants.operateCount; String materialPath =
			 * Constants.getTestMaterialPath(courseId); String libPath =
			 * Constants.getTestLibPath(courseId, testId); String ansPath =
			 * Constants.getTestLibAnswerPath(courseId, testId);
			 * ServerActionTool.generateTest(libPath, ansPath, materialPath, new
			 * Integer(schemaId).intValue(), new Integer(count).intValue());
			 */

			String testPath = Constants.getTestPath();
			String answerLibPath = Constants.getTestLibAnswerPath(courseId, testId);
			String testLibPath = Constants.getTestLibPath(courseId, testId);
			// 得到作业的信息
			MeleteTestModel mtm = courseService.getMeleteTestModelById(Long.valueOf(testId));
			// 读取题库接口：生成试卷压缩包
			generateTestFile(schemaId, courseId, testId,mtm.getBuildType(), mtm.getBuildNum().toString(), testPath, testLibPath, answerLibPath);

			logger.info("生成试卷成功");
			return null;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 读取试卷包
	 * 
	 * @param schemaId
	 * @param courseId
	 * @param paperType
	 * @param testId
	 * @param count
	 */
	private void generateTestFile(String schemaId, String courseId, String testId,String paperType, String count, String testPath,
			String testLibPath, String answerLibPath) {
		try {
			// 读取题库接口
			String key = CourseUtil.getUserKeyForExamSys(userDirectoryService.getCurrentUser().getEid());
			String urlStr = Constants.OUTSYS_EXAM_URL_BASE
					+ Constants.OUTSYS_EXAM_URL_PAPER.replace("${schemaId}", schemaId).replace("${count}", count)
							.replace("${type}", paperType).replace("${key}", key);
			URL url = new URL(urlStr);

			File zipFile = new File(testPath);
			if (!zipFile.exists()) {
				zipFile.mkdirs();
			}
			String fileExt = ".zip";

			InputStream is = url.openStream();
			OutputStream os = null;
			File file = new File(testPath + schemaId + fileExt);
			os = new FileOutputStream(file);

			int ch = 0;
			while ((ch = is.read()) != -1) {
				os.write(ch);
			}
			os.close();
			is.close();
			logger.info("generate test files success");

			unTestZipFile(schemaId, courseId, testPath, testLibPath, answerLibPath);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter();  
		    e.printStackTrace(new PrintWriter(sw, true));
			loggerManage.error("generate test error- " + courseId + ":" + sw.toString());
		}
	}

	/**
	 * 递归遍历所有子文件
	 */
	private List<File> getTestFiles(String filePath, List<File> allFile) {
		File root = new File(filePath);
		File[] files = root.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				getTestFiles(file.getAbsolutePath(), allFile);
			} else {
				allFile.add(file);
			}
		}
		return allFile;
	}

	/**
	 * 解压试卷包，并copy对应的路径
	 * 
	 * @param schemaId
	 * @param courseId
	 * @param testId
	 * @param testPath
	 *            资源包下载路径
	 * @param testLibPath
	 * @param answerLibPath
	 */
	private void unTestZipFile(String schemaId, String courseId, String testPath, String testLibPath,
			String answerLibPath) {
		try {
			String fileExt = ".zip";
			String filePath = testPath + schemaId + fileExt;
			File zipTestLib = new File(filePath);
			if (!zipTestLib.exists()) {
				logger.error("试卷压缩包不存在：" + filePath);
				return;
			}
			// 正式目录
			// 如果没有此文件夹，则创建
			File aFile = new File(answerLibPath);
			File tFile = new File(testLibPath);
			if (!aFile.exists()) {
				aFile.mkdirs();
			}
			if (!tFile.exists()) {
				tFile.mkdirs();
			}

			String targetPath = testPath + schemaId + "/";
			UnZipHandler uzh = new UnZipHandler(zipTestLib.getPath(), targetPath);
			uzh.extract();
			logger.debug("试卷解压成功");
			// 遍历解压文件
			List<File> allFile = new ArrayList<File>();
			getTestFiles(targetPath, allFile);

			// test material
			String materialDirPath = Constants.getTestMaterialPath(courseId) + "material/";
			File materialDir = new File(materialDirPath);
			if (!materialDir.exists()) {
				materialDir.mkdirs();
			}

			// 遍历得到所有子文件夹
			for (File f : allFile) {
				String copyPath = "";
				if (f.getName().contains("_A")) {
					copyPath = answerLibPath;
				} else {
					copyPath = testLibPath;
				}
				File copyFile = new File(copyPath);
				if (!copyFile.exists()) {
					copyFile.mkdirs();
				}
				FileUtils.copyFileToDirectory(f, copyFile);

				if (!f.getName().contains(Helper.EndTag)) {
					FileUtils.copyFileToDirectory(f, materialDir);
				}
			}
			// 复制完成后删除题库文件
			if (zipTestLib.exists()) {
				zipTestLib.delete();
				logger.debug("试卷压缩包删除成功");
			}
			File unFile = new File(targetPath);
			if (unFile.exists()) {
				FileUtils.deleteDirectory(unFile);
				logger.debug("试卷解压文件删除成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 添加前测
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addSelfTest() throws Exception {
		String res = "{success: true}";
		try {
			MeleteSelfTestModel test = new MeleteSelfTestModel();
			if (nodeType.equals("2")) {// 父节点是模块节点
				test.setModuleId(new Long(node));// 所属节点id
				test.setBelongType(CodeTable.belongMudole);// 所属类型
			} else if (nodeType.equals("3")) {// 父节点是页节点
				test.setSectionId(new Long(node));// 所属页id
				test.setBelongType(CodeTable.belongSection);// 所属类型
			}
			test.setName(name);// 前测名称
			if (isCaculateScore.equals(CodeTable.IsCaculateScoreYes)) {
				test.setRequirement("≥" + masteryScore);// 通过条件中文说明
			}
			test.setCourseId(getCourseId());
			test.setSamepaper(samepaper);// 使用同一策略
			test.setSchemaId(new Long(schemaId));// 策略id
			test.setTotalScore(new Long(totalScore));// 总分
			test.setMasteryScore(new Long(masteryScore));// 建议通过百分比
			test.setCreationDate(new Date());// 创建时间
			test.setModificationDate(new Date());// 修改时间
			test.setStatus(new Long(CodeTable.normal));// 前测状态
			test.setIsCaculateScore(new Long(isCaculateScore));// 是否计算平时成绩
			test.setBuildNum(buildNum);
			test.setBuildType(buildType);
			test.setIdx(new Long(0));// 前测序号
			test.setRatio(new Float(0));// 百分比初始化0
			String testId = courseService.editSelfTest(test, CodeTable.addType);
			if (isCaculateScore.equals(CodeTable.IsCaculateScoreYes)) {// 需要计算平时成绩,设置所属节点或页的通过条件
				if (nodeType.equals("2")) {// 父节点是模块节点
					courseService.updateModuleCondition(node, null);
				} else {// 父节点是页节点
					courseService.updateSectionCondition(node, null);
				}
			}
			res = "{success: true , selfTestId:'" + testId + "'}";
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw new Exception("保存失败");
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().println(res);
		return null;
	}

	/**
	 * 发布前测作业
	 * 
	 * @return
	 * @throws Exception
	 */
	public String publishSelfTest() throws Exception {
		try {
			// 生成试卷库
			/*
			 * String count = Constants.operateCount; String materialPath =
			 * Constants.getSelfTestMaterialPath(courseId); String libPath =
			 * Constants.getSelfTestLibPath(courseId, testId); String ansPath =
			 * Constants.getSelfTestLibAnswerPath(courseId, testId);
			 * ServerActionTool.generateTest(libPath, ansPath, materialPath, new
			 * Integer(schemaId).intValue(), new Integer(count).intValue());
			 * generateTestFile(schemaId, courseId, testId,
			 * Constants.operateCount); System.out.println("生成前测试卷成功"); 
			 */
			String testPath = Constants.getSelfTestPath();
			String answerLibPath = Constants.getSelfTestLibAnswerPath(courseId, testId);
			String testLibPath = Constants.getSelfTestLibPath(courseId, testId);
			// 得到作业的信息
			MeleteSelfTestModel mtm = (MeleteSelfTestModel)courseService.getModelById(MeleteSelfTestModel.class, Long.parseLong(testId));
			// 读取题库接口：生成试卷压缩包
			generateTestFile(schemaId, courseId, testId,mtm.getBuildType(), mtm.getBuildNum().toString(), testPath, testLibPath, answerLibPath);

			return null;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 添加讨论
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addForum() throws Exception {
		String res = "{success: true}";
		try {
			MeleteForumModel forum = new MeleteForumModel();
			if (nodeType.equals("2")) {// 父节点是模块节点
				forum.setModuleId(new Long(node));// 所属节点id
				forum.setBelongType(CodeTable.belongMudole);// 所属类型
			} else if (nodeType.equals("3")) {// 父节点是页节点
				forum.setSectionId(new Long(node));// 所属页id
				forum.setBelongType(CodeTable.belongSection);// 所属类型
			}
			forum.setCourseId(getCourseId());
			forum.setName(name);// 论坛名称
			if (isCaculateScore.equals(CodeTable.IsCaculateScoreYes)) {
				forum.setRequirement("至少发帖一次");// 通过条件中文说明
			}
			forum.setTopicId(topicId);// 主题id
			forum.setForumId(forumId);// 论坛id
			forum.setAreaId(areaId);//
			forum.setIsCaculateScore(new Long(isCaculateScore));// 是否计算平时成绩
			forum.setCreationDate(new Date());// 创建时间
			forum.setModificationDate(new Date());// 更改时间
			forum.setStatus(new Long(CodeTable.normal));// 论坛状态
			forum.setIdx(new Long(0));// 讨论序号
			forum.setRatio(new Float(0));// 百分比
			courseService.editForum(forum);
			if (isCaculateScore.equals(CodeTable.IsCaculateScoreYes)) {// 需要计算平时成绩,设置所属节点或页的通过条件
				if (nodeType.equals("2")) {// 父节点是模块节点
					courseService.updateModuleCondition(node, null);
				} else {// 父节点是页节点
					courseService.updateSectionCondition(node, null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			res = "{success: false , info:'" + e.getMessage() + "'}";
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().println(res);
		return null;
	}

	/**
	 * 获取某一课程的所有需要计算平时成绩的活动列表，用于设置百分比
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getActList() throws Exception {
		try {
			List<Object> list = courseService.getActList(courseId);
			// 获取站点id
			Site site = this.getCurrentSite();
			String siteId = null;
			if (site != null) {
				siteId = site.getId();
			}
			list.addAll(fileService.getAllNormalGradeFileBySiteId(siteId));
			List<AsyncGridModel> data = new ArrayList<AsyncGridModel>();
			BigDecimal countRatio = new BigDecimal(0);
			if (!list.isEmpty() && list.size() != 0) {
				for (Object obj : list) {
					AsyncGridModel model = new AsyncGridModel();
					if (obj.getClass() == MeleteTestModel.class) {// 作业
						MeleteTestModel test = (MeleteTestModel) obj;
						model.setDataIndex("id", test.getId());
						model.setDataIndex("type", CodeTable.test);
						model.setDataIndex("name", test.getName());
						model.setDataIndex("ratio", test.getRatio());
						countRatio = countRatio.add(new BigDecimal(test.getRatio().toString()));
						data.add(model);
					} else if (obj.getClass() == MeleteSelfTestModel.class) {
						MeleteSelfTestModel test = (MeleteSelfTestModel) obj;
						model.setDataIndex("id", test.getId());
						model.setDataIndex("type", CodeTable.selftest);
						model.setDataIndex("name", test.getName());
						model.setDataIndex("ratio", test.getRatio());
						countRatio = countRatio.add(new BigDecimal(test.getRatio().toString()));
						data.add(model);
					} else if (obj.getClass() == MeleteForumModel.class) {
						MeleteForumModel test = (MeleteForumModel) obj;
						model.setDataIndex("id", test.getId());
						model.setDataIndex("type", CodeTable.forum);
						model.setDataIndex("name", test.getName());
						model.setDataIndex("ratio", test.getRatio());
						countRatio = countRatio.add(new BigDecimal(test.getRatio().toString()));
						data.add(model);
					} else if (obj.getClass() == ResourceFileModel.class) {
						ResourceFileModel file = (ResourceFileModel) obj;
						model.setDataIndex("id", file.getId());
						model.setDataIndex("type", CodeTable.file);
						model.setDataIndex("name", file.getFileName());
						Float ratio = file.getRatio();
						model.setDataIndex("ratio", ratio);
						countRatio = countRatio.add(new BigDecimal(ratio == null ? "0" : ratio.toString()));
						data.add(model);
					}
				}
				if (countRatio.floatValue() == BigDecimal.ZERO.floatValue()) {// 平均分配百分比
					int num = list.size();
					// 初始化传的参数是要平分的总数（比如100）
					// divide
					// 第一个参数是平均分成几部分，第二参数是取小数点后几位，第三个参数是如果小数后第三位是小于等于.5就向下取整
					BigDecimal avgRatio = new BigDecimal(100)
							.divide(new BigDecimal(num), 2, BigDecimal.ROUND_HALF_DOWN); // 取小数点后两位
					// 把前list.size()-1个平均比例加起来
					BigDecimal al = avgRatio.multiply(new BigDecimal(num - 1));
					// 用一百减去前list.size()-1个总平均比例，得到最后一个平均比例，防止出现不能平分的现象（比如100/3）
					BigDecimal lastRatio = new BigDecimal(100).subtract(al);
					for (int i = 0; i < num; i++) {
						AsyncGridModel model = data.get(i);
						if (i != num - 1) {
							model.setDataIndex("ratio", avgRatio);
						} else {
							model.setDataIndex("ratio", lastRatio);
						}
					}
				}
			}
			AsyncGridModel model = new AsyncGridModel();
			model.setDataIndex("id", "sum");
			model.setDataIndex("name", "<div align=right style='color:red;font-weight:bold' >总计</div>");
			model.setDataIndex("ratio",
					(countRatio.floatValue() == BigDecimal.ZERO.floatValue()) ? "100" : countRatio.toString());
			data.add(model);

			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			String json = JsonBuilder.builderAsyncGridJson(data);
			response.getWriter().println(json);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 保存活动百分比
	 * 
	 * @return
	 * @throws Exception
	 */
	public String saveActPercent() throws Exception {
		try {
			String[] id = node.split(",");
			String[] type = nodeType.split(",");
			String[] ratios = ratio.split(",");
			courseService.saveRatio(id, type, ratios, impressionScore, impressionType, courseId);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 隐藏模块，将模块状态改为0，同时该模块下的子模块、页、活动的状态也都要改为0，
	 * 
	 * @return
	 * @throws Exception
	 */
	public String hideModule() throws Exception {
		String res = "{success: true}";
		try {
			courseService.changeModuleStatus(new Long(node), new Long(CodeTable.hide));
			if (parentType.equals(CodeTable.course)) {
				courseService.updateCourseCondition(parent);
			} else if (parentType.equals(CodeTable.module)) {
				courseService.updateModuleCondition(parent, CodeTable.module);
				// 获取需要计算平时成绩的活动的总个数
				Long scoreNum = courseService.countScoreActNum(courseId);
				res = "{success: true,scoreNum:'" + scoreNum + "'}";
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			res = "{success: false , info:'" + e.getMessage() + "'}";
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().println(res);
		return null;
	}

	/**
	 * 恢复模块，将模块状态改为1，同时该模块下的子模块、页、活动的状态也都要改为1，
	 * 
	 * @return
	 * @throws Exception
	 */
	public String showModule() throws Exception {
		String res = "{success: true}";
		try {
			courseService.changeModuleStatus(new Long(node), new Long(CodeTable.normal));
			if (parentType.equals(CodeTable.course)) {
				courseService.updateCourseCondition(parent);
			} else if (parentType.equals(CodeTable.module)) {
				courseService.updateModuleCondition(parent, CodeTable.module);
				// 获取需要计算平时成绩的活动的总个数
				Long scoreNum = courseService.countScoreActNum(courseId);
				res = "{success: true,scoreNum:'" + scoreNum + "'}";
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			res = "{success: false , info:'" + e.getMessage() + "'}";
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().println(res);
		return null;
	}

	/**
	 * 修改作业
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updateTest() throws Exception {
		String res = "{success: true}";
		try {
			MeleteTestModel test = (MeleteTestModel) courseService.getModelById(MeleteTestModel.class, new Long(node));
			String oldExtend = test.getIsCaculateScore().toString();// 是否计分
			String oldScore = test.getMasteryScore().toString();// 建议通过百分比
			test.setName(name);// 作业名称
			if (isCaculateScore.equals(CodeTable.IsCaculateScoreYes)) {
				BigDecimal total = new BigDecimal(totalScore);
				BigDecimal mastery = new BigDecimal(masteryScore);
				BigDecimal requirement = total.multiply(mastery.divide(new BigDecimal("100")));
				test.setRequirement("≥" + requirement.setScale(0, BigDecimal.ROUND_HALF_UP).toString());// 通过条件中文说明
			} else {
				test.setRequirement(null);
			}
			test.setSamepaper(samepaper);// 使用同一策略
			test.setSchemaId(new Long(schemaId));// 策略id
			test.setTotalScore(new Long(totalScore));// 总分
			test.setMasteryScore(new Long(masteryScore));// 建议通过百分比
			test.setIsCaculateScore(new Long(isCaculateScore));// 是否计算平时成绩
			test.setStartOpenDate(startOpenDate);
			test.setEndOpenDate(endOpenDate);
			test.setBuildNum(buildNum);
			test.setBuildType(buildType);
			test.setModificationDate(new Date());// 修改时间
			if (StringUtils.isBlank(minTimeInterval)) {
				minTimeInterval = Constants.TEST_MININTERVAL;
			}
			test.setMinTimeInterval(Long.parseLong(minTimeInterval));

			courseService.editTest(test, CodeTable.updateType);
			if (!oldExtend.equals(isCaculateScore)
					|| (oldExtend.equals(CodeTable.IsCaculateScoreYes) && oldExtend.equals(isCaculateScore) && !oldScore
							.equals(masteryScore))) {
				// 需要修改所属节点或页的通过条件
				if (test.getBelongType().equals(CodeTable.belongMudole)) {// 属于模块
					courseService.updateModuleCondition(test.getModuleId().toString(), null);
				} else { // 属于页
					courseService.updateSectionCondition(test.getSectionId().toString(), null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			res = "{success: false , info:'" + e.getMessage() + "'}";
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().println(res);
		return null;
	}

	/**
	 * 修改前测
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updateSelfTest() throws Exception {
		String res = "{success: true}";
		try {
			MeleteSelfTestModel test = (MeleteSelfTestModel) courseService.getModelById(MeleteSelfTestModel.class,
					new Long(node));
			String oldExtend = test.getIsCaculateScore().toString();
			String oldScore = test.getMasteryScore().toString();
			test.setName(name);// 前测名称
			if (isCaculateScore.equals(CodeTable.IsCaculateScoreYes)) {
				test.setRequirement("≥" + masteryScore);// 通过条件中文说明
			} else {
				test.setRequirement(null);
			}
			test.setSamepaper(samepaper);// 使用同一策略
			test.setSchemaId(new Long(schemaId));// 策略id
			test.setTotalScore(new Long(totalScore));// 总分
			test.setMasteryScore(new Long(masteryScore));// 建议通过百分比
			test.setModificationDate(new Date());// 修改时间
			test.setIsCaculateScore(new Long(isCaculateScore));// 是否计算平时成绩
			courseService.editSelfTest(test, CodeTable.updateType);

			if (!oldExtend.equals(isCaculateScore)
					|| (oldExtend.equals(CodeTable.IsCaculateScoreYes) && oldExtend.equals(isCaculateScore) && !oldScore
							.equals(masteryScore))) {
				// 需要修改所属节点或页的通过条件
				if (test.getBelongType().equals(CodeTable.belongMudole)) {// 属于模块
					courseService.updateModuleCondition(test.getModuleId().toString(), null);
				} else { // 属于页
					courseService.updateSectionCondition(test.getSectionId().toString(), null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			res = "{success: false , info:'" + e.getMessage() + "'}";
			logger.error(e.getMessage(), e);
			throw new Exception("更新失败");
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().println(res);
		return null;
	}

	/**
	 * 修改讨论
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updateForum() throws Exception {
		String res = "{success: true}";
		try {
			MeleteForumModel forum = (MeleteForumModel) courseService.getModelById(MeleteForumModel.class, new Long(
					node));
			String oldExtend = forum.getIsCaculateScore().toString();
			forum.setName(name);// 论坛名称
			if (isCaculateScore.equals(CodeTable.IsCaculateScoreYes)) {
				forum.setRequirement("至少发帖一次");// 通过条件中文说明
			} else {
				forum.setRequirement(null);
			}
			forum.setTopicId(topicId);// 版块id
			forum.setAreaId(areaId);//
			forum.setForumId(forumId);// 论坛
			forum.setModificationDate(new Date());// 修改时间
			forum.setIsCaculateScore(new Long(isCaculateScore));// 是否计算平时成绩
			courseService.editForum(forum);
			if (!oldExtend.equals(isCaculateScore)) {
				// 需要修改所属节点或页的通过条件
				if (forum.getBelongType().equals(CodeTable.belongMudole)) {// 属于模块
					courseService.updateModuleCondition(forum.getModuleId().toString(), null);
				} else { // 属于页
					courseService.updateSectionCondition(forum.getSectionId().toString(), null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			res = "{success: false , info:'" + e.getMessage() + "'}";
			logger.error(e.getMessage(), e);
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().println(res);
		return null;
	}

	/**
	 * 删除模块
	 * 
	 * @return
	 * @throws Exception
	 */
	public String delModule() throws Exception {
		String res = "{success: true}";
		try {
			// courseService.changeModuleStatus(new Long(node), new
			// Long(CodeTable.del));//标记删除
			courseService.delModule(new Long(node));
			if (parentType.equals(CodeTable.course)) {
				courseService.updateCourseCondition(parent);
			} else if (parentType.equals(CodeTable.module)) {
				courseService.updateModuleCondition(parent, CodeTable.module);
				boolean leaf = courseService.leafModuleByParentId(new Long(parent), true);
				if (leaf) {
					MeleteModuleModel parentModel = (MeleteModuleModel) courseService.getModelById(
							MeleteModuleModel.class, new Long(parent));
					parentModel.setChildType(null);
					courseService.editModule(parentModel);
				}
				// 获取需要计算平时成绩的活动的总个数
				Long scoreNum = courseService.countScoreActNum(courseId);
				res = "{success: true,scoreNum:'" + scoreNum + "'}";
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			res = "{success: false , info:'" + e.getMessage() + "'}";
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().println(res);
		return null;
	}

	/**
	 * 删除页
	 * 
	 * @return
	 * @throws Exception
	 */
	public String delSection() throws Exception {
		String res = "{success: true}";
		try {
			// courseService.changeSectionStatus(new Long(node), new
			// Long(CodeTable.del));//标记删除
			courseService.delSection(new Long(node));
			courseService.updateModuleCondition(parent, CodeTable.section);

			boolean leaf = courseService.leafSectionByModuleId(new Long(parent));
			if (leaf) {
				MeleteModuleModel parentModel = (MeleteModuleModel) courseService.getModelById(MeleteModuleModel.class,
						new Long(parent));
				parentModel.setChildType(null);
				courseService.editModule(parentModel);
			}
			// 获取需要计算平时成绩的活动的总个数
			Long scoreNum = courseService.countScoreActNum(courseId);
			// MeleteSectionModel section = (MeleteSectionModel)
			// courseService.getModelById(MeleteSectionModel.class,
			// new Long(node));
			// // 操作课件描述文件imsmanifest.xml将对应的页节点item和相关资源节点rescource一并删除
			// 相关资源一并删除
			// courseService.delItem(section.getCourseId(),
			// section.getSectionItemId());
			res = "{success: true,scoreNum:'" + scoreNum + "'}";
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			res = "{success: false , info:'" + e.getMessage() + "'}";
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().println(res);
		return null;
	}

	/**
	 * 删除作业
	 * 
	 * @return
	 * @throws Exception
	 */
	public String delTest() throws Exception {
		String res = "{success: true}";
		try {
			// res = courseService.changeModelStatus(MeleteTestModel.class, new
			// Long(node), new Long(CodeTable.del));//标记删除
			courseService.delActivity(MeleteTestModel.class, new Long(node));
			if (isCaculateScore.equals(CodeTable.IsCaculateScoreYes)) {
				if (parentType.equals(CodeTable.module)) {
					courseService.updateModuleCondition(parent, null);
				} else {
					courseService.updateSectionCondition(parent, null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			res = "{success: false , info:'" + e.getMessage() + "'}";
			logger.error(e.getMessage(), e);
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().println(res);
		return null;
	}

	/**
	 * 删除讨论
	 * 
	 * @return
	 * @throws Exception
	 */
	public String delForum() throws Exception {
		String res = "{success: true}";
		try {
			// res = courseService.changeModelStatus(MeleteForumModel.class, new
			// Long(node), new Long(CodeTable.del));标记删除
			courseService.delActivity(MeleteForumModel.class, new Long(node));
			if (isCaculateScore.equals(CodeTable.IsCaculateScoreYes)) {
				if (parentType.equals(CodeTable.module)) {
					courseService.updateModuleCondition(parent, null);
				} else {
					courseService.updateSectionCondition(parent, null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			res = "{success: false , info:'" + e.getMessage() + "'}";
			logger.error(e.getMessage(), e);
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().println(res);
		return null;
	}

	/**
	 * 删除前测
	 * 
	 * @return
	 * @throws Exception
	 */
	public String delSelfTest() throws Exception {
		String res = "{success: true}";
		try {
			MeleteSelfTestModel self = (MeleteSelfTestModel) courseService.getModelById(MeleteSelfTestModel.class,
					new Long(node));
			if (self.getBelongType().equals(CodeTable.belongMudole)) {
				MeleteModuleModel module = (MeleteModuleModel) courseService.getModelById(MeleteModuleModel.class,
						self.getModuleId());
				module.setModuleSelftest(null);
				courseService.editModule(module);
			} else {
				MeleteSectionModel section = (MeleteSectionModel) courseService.getModelById(MeleteSectionModel.class,
						self.getSectionId());
				section.setSectionSelftest(null);
				courseService.editSection(section);
			}
			courseService.delActivity(MeleteSelfTestModel.class, new Long(node));
		} catch (Exception e) {
			e.printStackTrace();
			res = "{success: false , info:'" + e.getMessage() + "'}";
			logger.error(e.getMessage(), e);
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().println(res);
		return null;
	}

	/**
	 * 拖拽某一个模块或者页节点到另外一个位置
	 * 
	 * @return
	 * @param point
	 *            拖拽方式
	 * @param destId
	 *            目标节点id
	 * @param destType
	 *            目标节点类型
	 * @param node
	 *            拖拽节点id
	 * @param nodeType
	 *            拖拽节点类型
	 * @throws Exception
	 */
	public String dragNode() throws Exception {
		String res = "{success: true}";
		try {
			courseService.dragNode(point, node, nodeType, destId, destType);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			res = "{success: false , info:'" + e.getMessage() + "'}";
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().println(res);
		return null;
	}

	/**
	 * 导出课件资源
	 * 
	 * @return
	 * @throws Exception
	 */
	public String exportScorm() throws Exception {
		StringBuffer res = new StringBuffer("{success: true");
		try {
			String exportFileName = new Date().getTime() + ".zip";
			courseService.packCourseWare(node, exportFileName);
			// 删除导出目录下一天以前的文件
			courseService.deleteFormerFile(Constants.COURSEWARE_EXPORT_PATH);
			// 下载课件包url
			String downloadUrl = Constants.COURSEWARE_EXPORT_URI + exportFileName;

			res.append(",downloadUrl:'" + downloadUrl + "'}");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().println(res);
		return null;
	}

	private void recurseFiles(ZipOutputStream jos, File file, String pathName) throws IOException,
			FileNotFoundException {
		if (file.isDirectory() && !file.getName().equals("mount"))// 是文件夹
		{
			String fileName = new String(file.getName().getBytes("GB2312"), "GBK");
			pathName = pathName + fileName + "/";
			jos.putNextEntry(new ZipEntry(pathName));
			String fileNames[] = file.list();
			if (fileNames != null) {
				for (int i = 0; i < fileNames.length; i++) {
					recurseFiles(jos, new File(file, fileNames[i]), pathName);
				}
			}
		} else { // 是文件
			String fileName = new String(file.getName().getBytes("GB2312"), "GBK");
			ZipEntry jarEntry = new ZipEntry(pathName + fileName);
			FileInputStream fin = new FileInputStream(file);
			BufferedInputStream in = new BufferedInputStream(fin);
			jos.putNextEntry(jarEntry);

			int len;

			while ((len = in.read(buf)) >= 0)
				jos.write(buf, 0, len);
			in.close();
			jos.closeEntry();
		}
	}

	/**
	 * 导入课件资源
	 * 
	 * @return
	 * @throws Exception
	 */
	public String importScorm() throws Exception {
		try {
			List<Object> list = courseService.importScorm(courseId, moduleId, node);
			List<AsyncGridModel> data = new ArrayList<AsyncGridModel>();
			String moduleIcon = CodeTable.icoModule;
			String sectionIcon = CodeTable.icoSection;
			String blankIcon = CodeTable.icoLucency;
			HashMap<Long, Integer> map = new HashMap<Long, Integer>();
			for (Object obj : list) {
				AsyncGridModel model = new AsyncGridModel();
				if (obj.getClass() == MeleteModuleModel.class) {// 是模块节点
					MeleteModuleModel module = (MeleteModuleModel) obj;
					Long id = module.getId();
					Long pid = module.getParentId();
					int level;
					if (pid == null) {
						level = 0;
						map.put(id, 0);
					} else {
						level = map.get(pid) + 1;
						map.put(id, level);
					}
					String icon = "";
					for (int i = 0; i < level; i++) {
						icon += "<img height=16 width=16 src='" + blankIcon + "'>";
					}
					model.setDataIndex("id", id);
					model.setDataIndex("pid", module.getParentId());
					model.setDataIndex("nodeType", CodeTable.module);
					model.setDataIndex("level", level);
					model.setDataIndex("ico", icon + "<img src='" + moduleIcon + "'>");
					model.setDataIndex("title", module.getTitle());
					model.setDataIndex("required", module.getRequired());
					model.setDataIndex("studyTime", module.getStudyDay());
					model.setDataIndex("childType", module.getChildType());// 下级节点类型
					data.add(model);
				} else if (obj.getClass() == MeleteSectionModel.class) {// 是页节点
					MeleteSectionModel section = (MeleteSectionModel) obj;
					Long pid = section.getModuleId();
					int level = map.get(pid) + 1;
					String icon = "";
					for (int i = 0; i < level; i++) {
						icon += "<img height=16 width=16 src='" + blankIcon + "'>";
					}
					model.setDataIndex("id", section.getId());
					model.setDataIndex("pid", section.getModuleId());
					model.setDataIndex("level", level);
					model.setDataIndex("nodeType", CodeTable.section);
					model.setDataIndex("ico", icon + "<img src='" + sectionIcon + "'>");
					model.setDataIndex("title", section.getTitle());
					model.setDataIndex("required", section.getRequired());
					model.setDataIndex("studyTime", section.getStudyTime());
					model.setDataIndex("childType", "");// 下级节点类型
					data.add(model);
				}
			}
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			String json = JsonBuilder.builderAsyncGridJson(data);
			response.getWriter().println(json);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter();  
		    e.printStackTrace(new PrintWriter(sw, true));
			loggerManage.error("import scorm error- " + courseId + ":" + sw.toString());
			throw e;
		}
	}

	/**
	 * 初始化导入资源的属性
	 * 
	 * @return
	 * @throws Exception
	 */
	public String setScormAttr() throws Exception {
		try {
			String[] nodeTypes = nodeType.split(",");
			String[] ids = node.split(",");
			String[] requireds = required.split(",");
			String[] studyTimes = studyTime.split(",");
			String[] childTypes = childType.split(",");
			String[] titles = title.split(",");
			courseService.setScormAttr(nodeTypes, ids, requireds, studyTimes, null, null, null, childTypes, titles);
			courseService.updateCourseCondition(courseId);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 得到资源地址
	 * 
	 * @author lzp
	 */
	public String findResUrl() throws Exception {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			String resCourseId = ServletActionContext.getRequest().getParameter("resCourseId");
			String urlStr = CourseUtil.getResSysResourcesUrl(userDirectoryService.getCurrentUser().getEid(),
					resCourseId);
			URL url = new URL(urlStr);
			InputStream inputStream = url.openStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("utf-8")));
			StringBuilder resp_result = new StringBuilder();
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				resp_result.append(s).append("\n");
			}
			reader.close();
			inputStream.close();

			// 解析返回结果
			JSONObject jsonResult = JSONObject.fromObject(resp_result.toString());
			String resultStatus = jsonResult.getString("status");
			if (!resultStatus.equals("200")) {
				response.getWriter().println(jsonResult.toString());
				return null;
			}

			JSONArray jsonArray = (JSONArray) JSONSerializer.toJSON(jsonResult.getString("data"));
			StringBuffer sb = new StringBuffer("[");
			if (jsonArray != null) {
				List list = (List) JSONSerializer.toJava(jsonArray);
				for (int i = 0; i < list.size(); i++) {
					JSONObject jsonObject = JSONObject.fromObject(list.get(i));
					sb.append("['").append(jsonObject.getString("id")).append("','")
							.append(jsonObject.getString("name")).append("','")
							.append(jsonObject.getString("httpPath")).append("','")
							.append(jsonObject.getString("catalogPath")).append("','")
							.append(jsonObject.getString("fileType")).append("','")
							.append(jsonObject.getString("size")).append("','")
							.append(jsonObject.getString("videoTime")).append("']");
					if (i < list.size() - 1) {
						sb.append(",");
					}
				}

			}

			sb.append("]");

			response.getWriter().println(sb.toString());
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 修改模块信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updateModule() throws Exception {
		String res = "{success: true}";
		try {
			MeleteModuleModel module = (MeleteModuleModel) courseService.getModelById(MeleteModuleModel.class,
					new Long(node));
			Long oldRequired = module.getRequired();// 修改前的必修属性
			String oldStudyDay = module.getStudyDay().toString();
			module.setTitle(title);// 节点标题
			module.setModifiedByFname(createdByFname);// 修改人姓名
			module.setModificationDate(new Date());// 修改时间
			module.setDescription(description);// 说明描述
			module.setCourseGuide(courseGuide);// 设置课程引导
			module.setTeachGoal(teachGoal);// 设置教学目标

			module.setKeyAndDifficute(keyAndDifficute);// 设置重点难点
			module.setTeachMethod(teachMethod);// 设置教学方法
			module.setLearnNavigation(learnNavigation);// 设置学习导航
			module.setKeywords(keywords);// 关键字
			module.setLearnObj(learnObj);// 学习目标
			module.setVideoPicPath(videoPicPath);// 视频图片
			module.setVideoUrl(videoUrl);// 视频地址
			module.setVideoSize(videoSize);// 视频大小
			module.setVideoTime(videoTime);// 视频时长
			module.setVideoType(CourseUtil.parseResourceType(videoUrl));
			module.setRequired(new Long(required));// 必修
			module.setStatus(new Long(CodeTable.normal));
			module.setStudyDay(new Long(studyDay));// 学习时长
			module.setWhatsNext(whatsNext);// 下一步
			String id = courseService.editModule(module);
			if (!oldStudyDay.equals(studyDay)) { // 最少选修个数或学习时长发生改变，通过条件也要修改
				courseService.updateModuleCondition(node, null);
			}
			// 必选修属性发生改变
			if (!oldRequired.toString().equals(required)) {
				Long parentId = module.getParentId();
				if (parentId != null) {// 有上级模块
					courseService.updateModuleCondition(parentId.toString(), CodeTable.module);
				} else {// 没有上级模块
					courseService.updateCourseCondition(module.getCourseId());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			res = "{success: false , info:'" + e.getMessage() + "'}";
			logger.error(e.getMessage(), e);
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().println(res);
		return null;
	}

	/**
	 * 修改页信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updateSection() throws Exception {
		String res = "{success: true}";
		try {
			String userName = userDirectoryService.getCurrentUser().getLastName()
					+ userDirectoryService.getCurrentUser().getFirstName();

			MeleteSectionModel section = (MeleteSectionModel) courseService.getModelById(MeleteSectionModel.class,
					new Long(node));
			Long oldRequired = section.getRequired();
			String oldTime = section.getStudyTime().toString();
			section.setTitle(title);// 页标题
			section.setAudioContent(new Long(audioContent));// 是否包含音频
			section.setModifiedByFname(userName);// 修改人
			section.setModificationDate(new Date());// 修改时间
			section.setDescription(description);// 说明描述
			section.setRequired(new Long(required));// 必修
			section.setStatus(new Long(CodeTable.normal));// 页状态
			section.setStudyTime(new Long(studyTime));// 学习时长
			section.setVideoPicPath(videoPicPath);// 视频图片
			section.setTextualContent(new Long(textualContent));// 是否包含文本
			section.setVideoContent(new Long(videoContent));// 是否包含视频
			section.setVideoUrl(videoUrl);
			section.setVideoType(CourseUtil.parseResourceType(videoUrl));
			section.setVideoSize(videoSize);// 视频大小
			section.setVideoTime(videoTime);// 视频时长
			courseService.editSection(section);
			if (!oldTime.equals(studyTime)) {// 学习时长发生变化，修改通过条件
				courseService.updateSectionCondition(section.getId().toString(), studyTime);
			}

			String path = CourseUtil.getServicePath() + "/" + fileUrl;
			File modifiedFile = new File(path);// 根据传过来的fileUrl定位文件
			FileUtils.writeStringToFile(modifiedFile, launchData, "UTF-8");// 将更改的文件内容写入html文件中
			// 属性发生变化，修改上级模块的通过条件
			if (!oldRequired.toString().equals(required)) {
				courseService.updateModuleCondition(section.getModuleId().toString(), CodeTable.section);
			}

		} catch (Exception e) {
			e.printStackTrace();
			res = "{success: false , info:'" + e.getMessage() + "'}";
			logger.error(e.getMessage(), e);
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().println(res);
		return null;
	}

	/**
	 * 根据资源id获取第一级节点的列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getRootNodeByWareId() throws Exception {
		StringBuffer sb = new StringBuffer("[");
		try {
			// String webPath = Constants.WEB_PATH;
			String securePath = Constants.SECURE_PATH;
			String path = new File(securePath) + "/" + Constants.COURSE_RESOURCE_PACK_PATH + "/" + node + "/"
					+ "imsmanifest.xml";
			ADLDOMParser domParser = new ADLDOMParser();
			boolean success = domParser.createDocument(path, true, false);
			Document mDocument = domParser.getDocument();
			// 检查课件包结构是否符合要求
			if(!courseService.checkDocument(mDocument)){
				HttpServletResponse response = ServletActionContext.getResponse();
				response.getWriter().println("{\"errorMsg\":\"课件包结构不符合要求，请检查课件包。课件包结构中不允许出现空目录，不允许同一级目录中同时存在节点和页\"}");
				return null;
			}
			// 预处理课件资源库中的课件描述文件的mDocument
			courseService.preProcess(mDocument);
			courseService.writeManifest(new File(path), mDocument);// 将预处理后的mDocument写回课件目录
			Node mManifest = mDocument.getDocumentElement();
			Vector mOrganizationList = ManifestHandler.getOrganizationNodes(mManifest, false);
			Node tempOrg = (Node) mOrganizationList.elementAt(0);
			Vector moduleList = DOMTreeUtility.getNodes(tempOrg, "item");// 一级模块集合

			for (int j = 0; j < moduleList.size(); j++) {
				Node tempOrganization = (Node) moduleList.elementAt(j);
				String tempOrgIdentifier = DOMTreeUtility.getAttributeValue(tempOrganization, "identifier");
				Node tempOrgTitleNode = DOMTreeUtility.getNode(tempOrganization, "title");
				String tempOrgTitle = DOMTreeUtility.getNodeValue(tempOrgTitleNode);
				sb.append("['" + tempOrgIdentifier + "','" + tempOrgTitle + "'],");
			}
			sb.deleteCharAt(sb.length() - 1).append("]");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().println(sb.toString());
		return null;
	}

	/**
	 * 根据课件资源id获取模块列表树
	 * 
	 * @return
	 * @throws Exception
	 */
	public String initModuleScorm() throws Exception {
		StringBuffer sb = new StringBuffer("");
		try {
			// String webPath = Constants.WEB_PATH;
			String securePath = Constants.SECURE_PATH;
			String path = new File(securePath) + "/" + Constants.COURSE_RESOURCE_PACK_PATH + "/" + wareId + "/"
					+ "imsmanifest.xml";

			ADLDOMParser adldomparser = new ADLDOMParser();
			adldomparser.createDocument(path, true, false);
			Document mDocument = adldomparser.getDocument();			
			courseService.preProcess(mDocument);// 预处理课件描述文件
			Node mManifest = mDocument.getDocumentElement();
			Vector mOrganizationList = ManifestHandler.getOrganizationNodes(mManifest, false);
			Node tempOrg = (Node) mOrganizationList.elementAt(0);
			String subString = courseService.initModuleScorm(tempOrg);
			sb.append("[" + subString + "]");
			courseService.writeManifest(new File(path), mDocument);// 将处理过的mDocument写回课件描述文件中
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().println(sb.toString());
		return null;
	}
	
	/**
	 * 检查课件包结构是否符合要求
	 * @return
	 * @throws Exception
	 */
	public String checkCourseScorm() throws Exception {
		try {
			// String webPath = Constants.WEB_PATH; 
			String securePath = Constants.SECURE_PATH;
			String path = new File(securePath) + "/" + Constants.COURSE_RESOURCE_PACK_PATH + "/" + wareId + "/"
					+ "imsmanifest.xml";

			ADLDOMParser adldomparser = new ADLDOMParser();
			adldomparser.createDocument(path, true, false);
			Document mDocument = adldomparser.getDocument();			
			// 检查课件包结构是否符合要求
			if(!courseService.checkDocument(mDocument)){
				HttpServletResponse response = ServletActionContext.getResponse();
				response.getWriter().println("{\"errorMsg\":\"课件包结构不符合要求，请检查课件包。课件包结构中不允许出现空目录，不允许同一级目录中同时存在节点和页\"}");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().println("{}");
		return null;
	}

	/**
	 * 保存选择的课件模块节点
	 * 
	 * @return
	 * @throws Exception
	 */
	public String importModuleScorm() throws Exception {
		logger.debug("---进入importModuleScorm()方法---");
		String res = "{success: true}";
		try {
			String webPath = Constants.WEB_PATH;
			String securePath = Constants.SECURE_PATH;
			String warePath = new File(securePath) + "/" + Constants.COURSE_RESOURCE_PACK_PATH + "/" + wareId + "/";
			String coursePath = new File(webPath) + "/" + Constants.SECTION_PATH + "/" + courseId + "/";
			String relaPath = Constants.SECTION_PATH + "/" + courseId + "/";
			File courseRootFile = new File(coursePath);
			if (!courseRootFile.exists()) { // 文件夹不存在则创建
				courseRootFile.mkdirs();
			}
			logger.debug("---课件包具体目录:" + warePath);
			ADLDOMParser adldomparser = new ADLDOMParser();
			adldomparser.createDocument(warePath + "imsmanifest.xml", true, false);
			Document mDocument = adldomparser.getDocument(); // 解析imsmanifest.xml文件
			Node mManifest = mDocument.getDocumentElement();
			Vector mOrganizationList = ManifestHandler.getOrganizationNodes(mManifest, false);
			Node courseNode = (Node) mOrganizationList.elementAt(0);

			Node resourcesNode = DOMTreeUtility.getNode(mManifest, "resources");
			Vector resources = DOMTreeUtility.getNodes(resourcesNode, "resource");
			List<String> resourceIdList = new ArrayList<String>();// 模块包含的页的引用的资源id
			List list = courseService.importModuleScorm(courseId, node, nodeType, moduleId, warePath, coursePath,
					courseNode, resources, relaPath, resourceIdList);
			if (nodeType.equals(CodeTable.module)) {
				MeleteModuleModel pModule = (MeleteModuleModel) courseService.getModelById(MeleteModuleModel.class,
						new Long(node));
				pModule.setChildType(CodeTable.module);
				courseService.editModule(pModule);
			}
			// 遍历resources 移除id不在resourceIdList中的资源节点
			courseService.removeResource(resources, resourceIdList, false);
			String pIdAndImportItemId = node + ";" + moduleId.split(";")[0];// 要导入的父节点id;所选则的要导入的模块的item元素的identifier属性值
			courseService.toCourseManifest(coursePath, mDocument, courseNode, resourcesNode, nodeType,
					pIdAndImportItemId);// 将添加的模块信息写入到课程目录下的课件描述文件中
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			res = "{success: false , info:'" + e.getMessage() + "'}";
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().println(res);
		return null;
	}

	/**
	 * 根据课件资源id获取页列表树
	 * 
	 * @return
	 * @throws Exception
	 */
	public String initSectionScorm() throws Exception {
		StringBuffer sb = new StringBuffer("");
		try {
			// String webPath = Constants.WEB_PATH;
			String securePath = Constants.SECURE_PATH;
			String path = new File(securePath) + "/" + Constants.COURSE_RESOURCE_PACK_PATH + "/" + wareId + "/"
					+ "imsmanifest.xml";

			ADLDOMParser adldomparser = new ADLDOMParser();
			adldomparser.createDocument(path, true, false);
			Document mDocument = adldomparser.getDocument();
			courseService.preProcess(mDocument);// 预处理课件描述文件
			Node mManifest = mDocument.getDocumentElement();
			Vector mOrganizationList = ManifestHandler.getOrganizationNodes(mManifest, false);
			Node tempOrg = (Node) mOrganizationList.elementAt(0);
			String subString = courseService.initSectionScorm(tempOrg);
			sb.append("[" + subString + "]");
			courseService.writeManifest(new File(path), mDocument);// 将处理过的mDocument写回课件描述文件中
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().println(sb.toString());
		return null;
	}

	/**
	 * 保存选择的课件页节点
	 * 
	 * @params moduleId //父节点id
	 * @params node //选中节点id集合
	 * @params courseId //课程节点id
	 * @params wareId //资源id
	 * @params title //页标题
	 * @throws Exception
	 */
	public String importSectionScorm() throws Exception {
		try {
			String servicePath = CourseUtil.getServicePath();
			String securePath = Constants.SECURE_PATH;
			String relaPath = Constants.SECTION_PATH + "/" + courseId + "/";
			String warePath = securePath + "/" + Constants.COURSE_RESOURCE_PACK_PATH + "/" + wareId + "/";
			String coursePath = servicePath + "/" + relaPath;
			File courseRootFile = new File(coursePath);
			CourseUtil.createFolder(courseRootFile);
			ADLDOMParser adldomparser = new ADLDOMParser();
			adldomparser.createDocument(warePath + "imsmanifest.xml", true, false);
			Document mDocument = adldomparser.getDocument(); // 解析imsmanifest.xml文件
			Node mManifest = mDocument.getDocumentElement();
			Vector orgNodes = ManifestHandler.getOrganizationNodes(mManifest, false);
			Node tempOrg = (Node) orgNodes.elementAt(0);
			Node resourcesNode = DOMTreeUtility.getNode(mManifest, "resources");
			Vector resources = DOMTreeUtility.getNodes(resourcesNode, "resource");
			String[] idAndIdRef = node.split(",");
			String ids = "";
			for (int i = 0; i < idAndIdRef.length; i++) {// 将idAndIdRef中存放的identifer放入ids中
				String[] splitIdAndIdRef = idAndIdRef[i].split(";");
				ids += splitIdAndIdRef[1] + ",";// identifer
			}
			List<String> list = courseService.importSectionScorm(idAndIdRef, title.split(","), resources, moduleId,
					warePath, coursePath, relaPath, courseId);
			courseService.removeResource(resources, list, false);
			String pIdAndImportItemId = moduleId + ";" + ids;
			// 将导入的页信息写入到课程目录下的课件描述文件的文档中
			courseService.toCourseManifest(coursePath, mDocument, tempOrg, resourcesNode, CodeTable.module,
					pIdAndImportItemId);
			courseService.updateModuleCondition(moduleId, CodeTable.section);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 修改页初始化
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updateSectionInit() throws Exception {
		String fileContent = "";
		try {
			if (StringUtils.isNotBlank(fileUrl)) {
				String path = CourseUtil.getServicePath() + "/" + fileUrl;
				File file = new File(path);
				fileContent = FileUtils.readFileToString(file, "UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		ServletActionContext.getRequest().setAttribute("data",
				"{\"launchData\":" + JsonBuilder.builderObjectJson(fileContent) + "}");
		return "result";
	}

	/**
	 * 修改节点时初始化 加载说明,课程引导,教学目标,重点难点,教学方法和学习导航字段
	 * 
	 * @return
	 */
	public String updateModuleInit() throws Exception {
		String description = "";
		String courseGuide = "";
		String teachGoal = "";
		String keyAndDifficute = "";
		String teachMethod = "";
		String learnNavigation = "";
		String videoPicPath = "";
		String videoUrl = "";
		try {
			MeleteModuleModel module = null;
			if (StringUtils.isNotBlank(moduleId)) {
				module = (MeleteModuleModel) courseService.getModelById(MeleteModuleModel.class,
						Long.parseLong(moduleId));
				description = module.getDescription();
				courseGuide = module.getCourseGuide();
				teachGoal = module.getTeachGoal();
				keyAndDifficute = module.getKeyAndDifficute();
				teachMethod = module.getTeachMethod();
				learnNavigation = module.getLearnNavigation();
				videoPicPath = module.getVideoPicPath();
				videoUrl = module.getVideoUrl();
			}

			if (description == null) {
				description = "";
			}

			if (courseGuide == null) {
				courseGuide = "";
			}
			if (teachGoal == null) {
				teachGoal = "";
			}
			if (keyAndDifficute == null) {
				keyAndDifficute = "";
			}
			if (teachMethod == null) {
				teachMethod = "";
			}

			if (learnNavigation == null) {
				learnNavigation = "";
			}

			if (videoPicPath == null) {
				videoPicPath = "";
			}
			if (videoUrl == null) {
				videoUrl = "";
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		ServletActionContext.getRequest().setAttribute(
				"data",
				"{\"description\":" + JsonBuilder.builderObjectJson(description) + ",\"courseGuide\":"
						+ JsonBuilder.builderObjectJson(courseGuide) + ",\"teachGoal\":"
						+ JsonBuilder.builderObjectJson(teachGoal) + ",\"keyAndDifficute\":"
						+ JsonBuilder.builderObjectJson(keyAndDifficute) + ",\"teachMethod\":"
						+ JsonBuilder.builderObjectJson(teachMethod) + ",\"videoPicPath\":"
						+ JsonBuilder.builderObjectJson(videoPicPath) + ",\"videoUrl\":"
						+ JsonBuilder.builderObjectJson(videoUrl) + ",\"learnNavigation\":"
						+ JsonBuilder.builderObjectJson(learnNavigation) + "}");
		return "result";
	}

	/**
	 * 把用户修改的百分比加起来
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acountSum() throws Exception {
		BigDecimal sum = new BigDecimal(ratio).subtract(new BigDecimal(addTwo)).add(new BigDecimal(addOne));
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().println("{success: true,sum:" + sum + "}");
		return null;
	}

	/**
	 * 进入设置印象分功能
	 * 
	 * @return
	 */
	public String impressionScoreSetInit() throws Exception {
		try {
			Site curSite = this.getCurrentSite();
			String siteRef = siteService.siteReference(curSite.getId());
			String currentUser = this.getCurrentUserId();
			if (securityService.unlock(currentUser, FunctionRegister.COURSE_SPACE_PERM, siteRef)) {// 该用户是否拥有设置印象分功能权限

				return "impressionScore";
			} else {
				throw new Exception("无权限");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 页面加载中心名称下拉列表调用方法
	 * 
	 * @return
	 */
	public String loadEduCenterBox() {
		/*
		 * try { List<Map> list = CourseUtil.getQuickStartEduCenterList();
		 * StringBuffer sb = new StringBuffer("["); for (Map map : list) {
		 * sb.append
		 * ("['").append(map.get("ID")).append("','").append(map.get("name"
		 * )).append("'],"); } sb = sb.deleteCharAt(sb.length() -
		 * 1).append("]"); HttpServletResponse response =
		 * ServletActionContext.getResponse();
		 * response.getWriter().println(sb.toString()); } catch (IOException e)
		 * { // TODO Auto-generated catch block e.printStackTrace();
		 * logger.error(e.getMessage(), e); }
		 */
		return null;
	}

	/**
	 * 根据页面传过来的条件查询要加印象分的学生
	 * 
	 * @return
	 */
	public String findImpScoreStudents() throws Exception {
		try {
			String siteId = this.getCurrentSite().getId();
			String courseId = courseService.getCourseBySiteId(siteId).getId();
			int startInt = NumberUtils.toInt(start);
			List<AsyncGridModel> data = new ArrayList<AsyncGridModel>();
			Object[] results = courseService.findImpScoreStudents(eduCenterId, stuName, stuNum, courseId, startInt);

			List<Object> testList = (List<Object>) results[1];
			for (Object obj : testList) {
				Map map = (Map) obj;
				AsyncGridModel model = new AsyncGridModel();
				model.setDataIndex("id", map.get("id"));
				model.setDataIndex("stuName", map.get("stuName"));
				model.setDataIndex("stuNum", map.get("stuNum"));
				model.setDataIndex("score", map.get("score"));
				model.setDataIndex("eduCenter", map.get("eduCenter"));
				model.setDataIndex("userid", map.get("userid"));
				model.setDataIndex("paperid", map.get("paperid"));
				model.setDataIndex("courseid", map.get("courseid"));
				model.setDataIndex("studyrecordId", map.get("studyrecordId"));
				data.add(model);
			}
			String json = JsonBuilder.builderAsyncGridJson(data);
			ServletActionContext.getResponse().getWriter().println(json);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
		return null;
	}

	/**
	 * 教师为选择的学生增加印象分数
	 * 
	 * @return
	 */
	public String addImpScore() throws Exception {
		// 查询时之差有者们课程学习记录的学生
		String[] ids = selectionIds.split(",");
		String[] stuScores = scores.split(",");
		try {
			if (selectionIds == null || "".equals(selectionIds) || ids.length == 0) {
				throw new Exception("未选择记录");
			}

			boolean result = studyService.addImpScore(ids, stuScores, impScore);
			if (!result) {
				throw new Exception("更新失败");
			}
			ServletActionContext.getRequest().setAttribute("data", "\"加分成功\"");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
		return "result";
	}

	/**************** 功能调整 独立编辑页面 ********************/
	/**
	 * 编辑：模块页
	 * 
	 * @return
	 * @throws Exception
	 */
	public String goModuleEdit() throws Exception {
		try {
			// 编辑状态
			if (action == null || StringUtils.isBlank(action.toString())) {
				MeleteModuleModel module = (MeleteModuleModel) courseService.getModelById(MeleteModuleModel.class,
						new Long(node));
				if (module != null) {
					JSONObject object = JSONObject.fromObject(module);
					String jsonResult = object.toString();
					ServletActionContext.getRequest().setAttribute("jsonResult", jsonResult);
				}
			}
			ServletActionContext.getRequest().setAttribute("node", node);
			ServletActionContext.getRequest().setAttribute("nodeType", nodeType);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return "modulePage";
	}

	/**
	 * 编辑：节点页
	 * 
	 * @return
	 * @throws Exception
	 */
	public String goSectionEdit() throws Exception {
		try {
			String siteId = this.getCurrentSite().getId();
			MeleteCourseModel course = courseService.getCourseBySiteId(siteId);
			// 编辑状态
			if (action == null || StringUtils.isBlank(action.toString())) {
				MeleteSectionModel section = (MeleteSectionModel) courseService.getModelById(MeleteSectionModel.class,
						new Long(node));
				if (section != null) {
					JSONObject object = JSONObject.fromObject(section);
					String jsonResult = object.toString();
					ServletActionContext.getRequest().setAttribute("jsonResult", jsonResult);
				}
			}

			ServletActionContext.getRequest().setAttribute("node", node);
			ServletActionContext.getRequest().setAttribute("template", course.getPlayerTemplate());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return "sectionPage";
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLearnObj() {
		return learnObj;
	}

	public void setLearnObj(String learnObj) {
		this.learnObj = learnObj;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCourseGuide() {
		return courseGuide;
	}

	public void setCourseGuide(String courseGuide) {
		this.courseGuide = courseGuide;
	}

	public String getTeachGoal() {
		return teachGoal;
	}

	public void setTeachGoal(String teachGoal) {
		this.teachGoal = teachGoal;
	}

	public String getKeyAndDifficute() {
		return keyAndDifficute;
	}

	public void setKeyAndDifficute(String keyAndDifficute) {
		this.keyAndDifficute = keyAndDifficute;
	}

	public String getTeachMethod() {
		return teachMethod;
	}

	public void setTeachMethod(String teachMethod) {
		this.teachMethod = teachMethod;
	}

	public String getLearnNavigation() {
		return learnNavigation;
	}

	public void setLearnNavigation(String learnNavigation) {
		this.learnNavigation = learnNavigation;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getCreatedByFname() {
		return createdByFname;
	}

	public void setCreatedByFname(String createdByFname) {
		this.createdByFname = createdByFname;
	}

	public String getWhatsNext() {
		return whatsNext;
	}

	public void setWhatsNext(String whatsNext) {
		this.whatsNext = whatsNext;
	}

	public String getRequired() {
		return required;
	}

	public void setRequired(String required) {
		this.required = required;
	}

	public String getMinSecNum() {
		return minSecNum;
	}

	public void setMinSecNum(String minSecNum) {
		this.minSecNum = minSecNum;
	}

	public String getStudyDay() {
		return studyDay;
	}

	public void setStudyDay(String studyDay) {
		this.studyDay = studyDay;
	}

	public String getPrerequids() {
		return prerequids;
	}

	public void setPrerequids(String prerequids) {
		this.prerequids = prerequids;
	}

	public String getTextualContent() {
		return textualContent;
	}

	public void setTextualContent(String textualContent) {
		this.textualContent = textualContent;
	}

	public String getVideoContent() {
		return videoContent;
	}

	public void setVideoContent(String videoContent) {
		this.videoContent = videoContent;
	}

	public String getAudioContent() {
		return audioContent;
	}

	public void setAudioContent(String audioContent) {
		this.audioContent = audioContent;
	}

	public String getStudyTime() {
		return studyTime;
	}

	public void setStudyTime(String studyTime) {
		this.studyTime = studyTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSamepaper() {
		return samepaper;
	}

	public void setSamepaper(String samepaper) {
		this.samepaper = samepaper;
	}

	public String getSchemaId() {
		return schemaId;
	}

	public void setSchemaId(String schemaId) {
		this.schemaId = schemaId;
	}

	public String getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(String totalScore) {
		this.totalScore = totalScore;
	}

	public String getMasteryScore() {
		return masteryScore;
	}

	public void setMasteryScore(String masteryScore) {
		this.masteryScore = masteryScore;
	}

	public String getMinTimeInterval() {
		return minTimeInterval;
	}

	public void setMinTimeInterval(String minTimeInterval) {
		this.minTimeInterval = minTimeInterval;
	}

	public String getImpressionScore() {
		return impressionScore;
	}

	public void setImpressionScore(String impressionScore) {
		this.impressionScore = impressionScore;
	}

	public String getMinModNum() {
		return minModNum;
	}

	public void setMinModNum(String minModNum) {
		this.minModNum = minModNum;
	}

	public String getPlayerTemplate() {
		return playerTemplate;
	}

	public void setPlayerTemplate(String playerTemplate) {
		this.playerTemplate = playerTemplate;
	}

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public String getDestId() {
		return destId;
	}

	public void setDestId(String destId) {
		this.destId = destId;
	}

	public String getDestType() {
		return destType;
	}

	public void setDestType(String destType) {
		this.destType = destType;
	}

	public String getImpressionType() {
		return impressionType;
	}

	public void setImpressionType(String impressionType) {
		this.impressionType = impressionType;
	}

	public String getIsCaculateScore() {
		return isCaculateScore;
	}

	public void setIsCaculateScore(String isCaculateScore) {
		this.isCaculateScore = isCaculateScore;
	}

	public IFileService getFileService() {
		return fileService;
	}

	public void setFileService(IFileService fileService) {
		this.fileService = fileService;
	}

	public String getNodeIdx() {
		return nodeIdx;
	}

	public void setNodeIdx(String nodeIdx) {
		this.nodeIdx = nodeIdx;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getShowHide() {
		return showHide;
	}

	public void setShowHide(String showHide) {
		this.showHide = showHide;
	}

	public String getShowScore() {
		return showScore;
	}

	public void setShowScore(String showScore) {
		this.showScore = showScore;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getParentType() {
		return parentType;
	}

	public void setParentType(String parentType) {
		this.parentType = parentType;
	}

	public String getBrotherNum() {
		return brotherNum;
	}

	public void setBrotherNum(String brotherNum) {
		this.brotherNum = brotherNum;
	}

	public String getTestId() {
		return testId;
	}

	public void setTestId(String testId) {
		this.testId = testId;
	}

	public String getExtAttr() {
		return extAttr;
	}

	public void setExtAttr(String extAttr) {
		this.extAttr = extAttr;
	}

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	public IStudyService getStudyService() {
		return studyService;
	}

	public void setStudyService(IStudyService studyService) {
		this.studyService = studyService;
	}

	public ITemplateService getTemplateService() {
		return templateService;
	}

	public void setTemplateService(ITemplateService templateService) {
		this.templateService = templateService;
	}

	public String getVideoPicPath() {
		return videoPicPath;
	}

	public void setVideoPicPath(String videoPicPath) {
		this.videoPicPath = videoPicPath;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public static void main(String[] args) {
		// System.out.println(Long.parseLong("20141027125911917"));
		/*
		 * String r =
		 * "{\"status\":0,\"courses\":[{\"id\":112901,\"name\":\"知识管理\"},{\"id\":628,\"name\":\"现代通信网\"},{\"id\":636,\"name\":\"SDH与MSTP\"},{\"id\":638,\"name\":\"GSM系统与GPRS\"}]}"
		 * ; JSONObject jsonResult = JSONObject.fromObject(r);
		 * //System.out.println(jsonResult.getString("status"));
		 * 
		 * StringBuffer sb = new StringBuffer("["); JSONArray jsonArray =
		 * (JSONArray) JSONSerializer.toJSON(jsonResult.getString("courses"));
		 * if (jsonArray != null) { List list = (List)
		 * JSONSerializer.toJava(jsonArray); for (Object o : list) { JSONObject
		 * jsonObject = JSONObject.fromObject(o);
		 * sb.append("['").append(jsonObject.getString("id")).append("','")
		 * .append(jsonObject.getString("name")).append("'],"); } } sb =
		 * sb.deleteCharAt(sb.length() - 1).append("]");
		 * System.out.println(sb.toString());
		 */

	}

	public Long getVideoSize() {
		return videoSize;
	}

	public void setVideoSize(Long videoSize) {
		this.videoSize = videoSize;
	}

	public String getVideoTime() {
		return videoTime;
	}

	public void setVideoTime(String videoTime) {
		this.videoTime = videoTime;
	}

	public Date getStartOpenDate() {
		return startOpenDate;
	}

	public void setStartOpenDate(Date startOpenDate) {
		this.startOpenDate = startOpenDate;
	}

	public Date getEndOpenDate() {
		return endOpenDate;
	}

	public void setEndOpenDate(Date endOpenDate) {
		this.endOpenDate = endOpenDate;
	}

	public String getBuildType() {
		return buildType;
	}

	public void setBuildType(String buildType) {
		this.buildType = buildType;
	}

	public Long getBuildNum() {
		return buildNum;
	}

	public void setBuildNum(Long buildNum) {
		this.buildNum = buildNum;
	}
}
