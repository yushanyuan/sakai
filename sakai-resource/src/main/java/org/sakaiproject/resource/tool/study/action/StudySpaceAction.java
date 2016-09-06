package org.sakaiproject.resource.tool.study.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.content.api.ContentHostingService;
import org.sakaiproject.content.api.ContentResource;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.exception.PermissionException;
import org.sakaiproject.exception.TypeException;
import org.sakaiproject.resource.api.course.model.MeleteCourseModel;
import org.sakaiproject.resource.api.course.model.MeleteForumModel;
import org.sakaiproject.resource.api.course.model.MeleteModuleModel;
import org.sakaiproject.resource.api.course.model.MeleteSectionModel;
import org.sakaiproject.resource.api.course.model.MeleteSelfTestModel;
import org.sakaiproject.resource.api.course.model.MeleteTestModel;
import org.sakaiproject.resource.api.course.service.ICourseService;
import org.sakaiproject.resource.api.course.vo.Node;
import org.sakaiproject.resource.api.study.model.MeleteForumRecordModel;
import org.sakaiproject.resource.api.study.model.MeleteModuleRecordModel;
import org.sakaiproject.resource.api.study.model.MeleteSectionRecordDetailModel;
import org.sakaiproject.resource.api.study.model.MeleteSectionRecordModel;
import org.sakaiproject.resource.api.study.model.MeleteSelftestRecordModel;
import org.sakaiproject.resource.api.study.model.MeleteStudyHistoryRecordModel;
import org.sakaiproject.resource.api.study.model.MeleteStudyRecordModel;
import org.sakaiproject.resource.api.study.model.MeleteTestRecordModel;
import org.sakaiproject.resource.api.study.service.IStudyService;
import org.sakaiproject.resource.api.template.model.TemplateModel;
import org.sakaiproject.resource.util.AsyncGridModel;
import org.sakaiproject.resource.util.AsyncTreeModel;
import org.sakaiproject.resource.util.CacheElement;
import org.sakaiproject.resource.util.CacheUtil;
import org.sakaiproject.resource.util.CodeTable;
import org.sakaiproject.resource.util.Constants;
import org.sakaiproject.resource.util.CourseUtil;
import org.sakaiproject.resource.util.JsonBuilder;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.tool.api.ToolManager;
import org.sakaiproject.user.api.UserDirectoryService;

import com.bupticet.paperadmin.tool.Helper;
import com.bupticet.paperadmin.tool.ServerActionTool;
import com.opensymphony.xwork2.ActionSupport;

public class StudySpaceAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7536138516974253700L;
	private static Log logger = LogFactory.getLog(StudySpaceAction.class);
	private static Log loggerManage = LogFactory.getLog("sysManageResourceR");
	private ICourseService courseService;

	private IStudyService studyService;

	private String node;// 树节点id
	private String nodeType;// 节点类型
	private String nodeIdx;// 节点序号
	private String path;// 课件存储路径

	private String courseId;// 课程Id
	private String studentId;// 学生Id
	private String moduleId;// 模块Id
	private String sectionRecordId;// 节点记录Id
	private String detailRecordId;// 节点明细记录Id

	private String studyrecordId;// 学生记录ID
	private String noderecordId;// 节点记录ID
	private String attemptId;
	private String paperid;// 页ID
	private String testId;// 作业ID
	private String testType;// 作业类型
	private String activityId;// 课件中活动的id
	private String activityValue;// 活动值
	private String template;// 播放模板
	private String templateUrl;// 模板路径

	// 工具id
	private String placementId;

	// 模板
	private String templateCommon;

	public String getTestId() {
		return testId;
	}

	public void setTestId(String testId) {
		this.testId = testId;
	}

	public String getTestType() {
		return testType;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public void setTestType(String testType) {
		this.testType = testType;
	}

	public String getPaperid() {
		return paperid;
	}

	public void setPaperid(String paperid) {
		this.paperid = paperid;
	}

	public String getAttemptId() {
		return attemptId;
	}

	public void setAttemptId(String attemptId) {
		this.attemptId = attemptId;
	}

	public String getNoderecordId() {
		return noderecordId;
	}

	public void setNoderecordId(String noderecordId) {
		this.noderecordId = noderecordId;
	}

	public String getStudyrecordId() {
		return studyrecordId;
	}

	public void setStudyrecordId(String studyrecordId) {
		this.studyrecordId = studyrecordId;
	}

	public String getDetailRecordId() {
		return detailRecordId;
	}

	public void setDetailRecordId(String detailRecordId) {
		this.detailRecordId = detailRecordId;
	}

	public String getSectionRecordId() {
		return sectionRecordId;
	}

	public void setSectionRecordId(String sectionRecordId) {
		this.sectionRecordId = sectionRecordId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getNodeIdx() {
		return nodeIdx;
	}

	public void setNodeIdx(String nodeIdx) {
		this.nodeIdx = nodeIdx;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
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

	private SiteService siteService = (SiteService) ComponentManager.get(SiteService.class);

	private ToolManager toolManager = (ToolManager) ComponentManager.get(ToolManager.class);

	private UserDirectoryService userDirectoryService = (UserDirectoryService) ComponentManager
			.get(UserDirectoryService.class);

	private ContentHostingService contentHostingService = (org.sakaiproject.content.api.ContentHostingService) ComponentManager
			.get(ContentHostingService.class);

	/*
	 * 获取登陆用户Id
	 */
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

	public String execute() {
		String page = "study";
		try {
			// 登陆用户Id（学生Id）
			studentId = this.getCurrentUserId();
			Site site = this.getCurrentSite();
			// 检查缓存
			CacheElement cacheCourse = CacheUtil.getInstance().getCacheOfCourse(site.getId());
			CacheUtil.getInstance().getCacheOfStudyrecord(site.getId(), studentId, null);
			MeleteCourseModel courseInfo = cacheCourse.getCourse();
			courseId = courseInfo.getId();

			String checkStudyHistory = checkStudyHistory(cacheCourse);
			if (checkStudyHistory != null) {
				return checkStudyHistory;
			}

			// 获取模板信息
			templateUrl = getTemplateUrlByName(courseInfo, TemplateModel.TEMPLATE_PAGE_CHAPTER, null);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter();  
		    e.printStackTrace(new PrintWriter(sw, true));
			loggerManage.error("study course error-" + courseId + ":" + sw.toString());
		}
		return page;
	}

	/**
	 * 获取课程的模板地址
	 * 
	 * @param course
	 * @param page
	 * @param moduleId
	 * @return
	 */
	private String getTemplateUrlByName(MeleteCourseModel course, String page, String moduleId) {
		String templateUrl = null;

		String template = course.getPlayerTemplate();
		// 工具id
		String placementId = toolManager.getCurrentPlacement().getId();
		if (StringUtils.isBlank(template)) {
			template = TemplateModel.TEMPLATE_NAME_DEFAULT;
		}
		// 系统原自定义的模板
		if (template.equals(TemplateModel.TEMPLATE_NAME_CUSTOM)) {
			ContentResource tplres = getTemplateRes(course.getSiteId(), page);
			if (tplres != null) {
				templateUrl = tplres.getUrl();
			} else {
				// 如果自定义模板有问题，则进入默认模板
				template = TemplateModel.TEMPLATE_NAME_DEFAULT;
			}
		}

		if (!template.equals(TemplateModel.TEMPLATE_NAME_CUSTOM)) {
			// page = getSysPlayTemplate(template, courseInfo);
			templateUrl = Constants.TEMPLATE_PATH_URI_RELATIVE + template + "/" + page;
		}
		templateUrl = templateUrl + "?placementId=" + placementId + "&dt=" + Math.round(Math.random() * 1000);
		if (StringUtils.isNotBlank(moduleId)) {
			templateUrl = templateUrl + "&module=" + moduleId;
		}

		// 将链接进行编码
		templateUrl = URLEncoder.encode(templateUrl);

		return templateUrl;
	}

	/**
	 * 系统自带模板，包括（默认，红蓝绿，山坡）
	 * 
	 * @author zihongyan 2014-01-17
	 * @param template
	 * @param courseInfo
	 * @return
	 * @throws Exception
	 */
	private String getSysPlayTemplate(String template, MeleteCourseModel courseInfo) throws Exception {
		// 默认返回自定义模板
		String page = "study";

		CacheElement cacheCourse = CacheUtil.getInstance().getCacheOfCourse(this.getCurrentSite().getId());
		CacheElement cacheStudy = CacheUtil.getInstance().getCacheOfStudyrecord(this.getCurrentSite().getId(),
				this.getCurrentUserId(), studyrecordId);
		MeleteStudyRecordModel studyRecord = cacheStudy.getStudyRecord();

		if (template.equals(CodeTable.template_default)) {// 默认模板
			ServletActionContext.getRequest().setAttribute("lessonStatus",
					String.valueOf(studyRecord.getLessonStatus()).equals(CodeTable.passStatusYes) ? "通过" : "未通过");
			ServletActionContext.getRequest().setAttribute("score", String.valueOf(studyRecord.getScore()));
			ServletActionContext.getRequest().setAttribute("studyrecordId", studyRecord.getStudyrecordId());
			page = "template";
			templateCommon = "default";
		}
		// 模板红,蓝或者绿中的一种
		else if ((template.equals(CodeTable.template_red)) || (template.equals(CodeTable.template_green))
				|| (template.equals(CodeTable.template_blue))) {
			List<MeleteModuleModel> tempModuleResList = cacheCourse.getModuleListByParentid(null);
			List<MeleteModuleModel> moduleResList = new ArrayList<MeleteModuleModel>();
			if ((tempModuleResList != null) && (tempModuleResList.size() != 0)) {
				for (int i = 0; i < tempModuleResList.size(); i++) {
					MeleteModuleModel module = tempModuleResList.get(i);
					if (CodeTable.hide.equals(module.getStatus().toString())) {
						continue;
					}
					moduleResList.add(module);
				}
			}
			List<MeleteModuleRecordModel> moduleResRecordList = cacheStudy.getModuleRecordList(moduleResList);
			if (moduleResList != null && !moduleResList.isEmpty()) {
				for (int i = 0; i < moduleResList.size(); i++) {
					MeleteModuleRecordModel modRec = moduleResRecordList.get(i);
					if (modRec == null) {// 若模块对应的学习记录为空则
						modRec = CacheUtil.getInstance().getStuModuleRecord(cacheStudy, moduleResList.get(i),
								studyrecordId);
						moduleResRecordList.add(i, modRec);
					}
				}
			}
			List<Node> nodeList = CourseUtil.changeToNode(moduleResList, moduleResRecordList, null, null, null, null,
					null, null, null, null);
			ServletActionContext.getRequest().setAttribute("nodeList", nodeList);
			ServletActionContext.getRequest().setAttribute("studyRecord", studyRecord);
			page = "template";
			// 匹配模板
			if (template.equals(CodeTable.template_red)) {
				templateCommon = "red";
			} else if (template.equals(CodeTable.template_green)) {
				templateCommon = "green";
			} else if (template.equals(CodeTable.template_blue)) {
				templateCommon = "blue";
			}
		}
		return page;
	}

	/**
	 * 根据节的id获得 章的id
	 * 
	 * @author chenbin 2013-7-3
	 * @param n
	 *            节点id
	 * @return
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	private Long getRootModuleId(Long n) throws NumberFormatException, Exception {
		MeleteModuleModel mmodule = (MeleteModuleModel) courseService.getModelById(MeleteModuleModel.class, n);
		Long parId = 0L;
		Long topNodeId = 0L;
		if (mmodule == null || mmodule.getParentId() == null) {
			topNodeId = mmodule.getId();
			return topNodeId;
		}
		parId = mmodule.getParentId();
		while (parId > 0) {
			MeleteModuleModel m = (MeleteModuleModel) courseService.getModelById(MeleteModuleModel.class, parId);
			if (m != null && m.getParentId() != null && m.getParentId() > 0) {
				parId = m.getParentId();
				topNodeId = m.getId();
			} else {
				topNodeId = m.getId();
				parId = 0L;
			}
		}

		return topNodeId;
	}

	/**
	 * 获取模板页
	 * 
	 * @param courseId
	 * @param level
	 * @return
	 */
	private ContentResource getTemplateRes(String courseId, String level) {
		try {
			String dir = "/group/" + courseId + "/site_system_files/coursefiles/" + level;
			return contentHostingService.getResource(dir);
		} catch (PermissionException e) {
			logger.error(e.getMessage());
		} catch (IdUnusedException e) {
			logger.error(e.getMessage());
		} catch (TypeException e) {
			logger.error(e.getMessage());
		} catch (Throwable e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 加载课程树
	 * 
	 * @param nodeType
	 *            类型
	 * @param nodeIdx
	 *            序号
	 * @param node
	 *            id
	 * @param studyrecordId
	 *            学习记录id
	 * @return
	 * @throws Exception
	 */
	public String loadCourse() throws Exception {
		try {
			CacheElement cacheCourse = CacheUtil.getInstance().getCacheOfCourse(this.getCurrentSite().getId());
			CacheElement cacheStudy = CacheUtil.getInstance().getCacheOfStudyrecord(this.getCurrentSite().getId(),
					this.getCurrentUserId(), studyrecordId);

			// 定义查询出的所有节点集合
			List<Object> nodeList = new ArrayList<Object>();
			// 定义要返回的节点集合
			List<AsyncTreeModel> data = new ArrayList<AsyncTreeModel>();
			if (node.equals("0")) {// 加载根节点，显示课程名称节点
				MeleteCourseModel course = cacheCourse.getCourse();
				nodeList.add(course);// 加入到节点集合中
			} else if (nodeType.equals(CodeTable.course)) {// 节点类型是“课程”
				// 查询一级模块节点
				List<MeleteModuleModel> moduleList = cacheCourse.getModuleListByParentid(null);
				nodeList.addAll(moduleList);
			} else if (nodeType.equals(CodeTable.module)) {// 节点类型是“模块”
				boolean moduleLeaf = cacheCourse.getModuleLeaf(new Long(node)).booleanValue();
				if (!moduleLeaf) {
					// 查询当前模块的下级模块
					List<MeleteModuleModel> moduleList = cacheCourse.getModuleListByParentid(node);
					nodeList.addAll(moduleList);
				}
				boolean secLeaf = cacheCourse.getSectionLeaf(new Long(node)).booleanValue();
				if (!secLeaf) {
					// 查询当前模块所包含的页
					List<MeleteSectionModel> sectionList = cacheCourse.getSectionListByModuleId(node);
					nodeList.addAll(sectionList);
				}
				boolean selftestLeaf = cacheCourse.getSelftestLeaf(new Long(node)).booleanValue();
				if (!selftestLeaf) {
					// 查询当前模块所包含的前测
					List<MeleteSelfTestModel> selfList = cacheCourse.getSelftestListByParentId(node, CodeTable.module);
					nodeList.addAll(selfList);
				}
				boolean testLeaf = cacheCourse.getTestLeaf(new Long(node)).booleanValue();
				if (!testLeaf) {
					// 查询当前模块所包含的作业
					List<MeleteTestModel> testList = cacheCourse.getTestListByParentId(node, CodeTable.module);
					nodeList.addAll(testList);
				}
				boolean forumLeaf = cacheCourse.getForumLeaf(new Long(node)).booleanValue();
				if (!forumLeaf) {
					// 查询当前模块所包含的讨论
					List<MeleteForumModel> forumList = cacheCourse.getForumListByParentId(node, CodeTable.module);
					nodeList.addAll(forumList);
				}
			} else if (nodeType.equals(CodeTable.section)) {// 节点类型是“页”
				boolean selftestLeaf = cacheCourse.getSelftestLeaf(new Long(node)).booleanValue();
				if (!selftestLeaf) {
					// 查询当前节点所包含的前测
					List<MeleteSelfTestModel> selfList = cacheCourse.getSelftestListByParentId(node, CodeTable.section);
					nodeList.addAll(selfList);
				}
				boolean testLeaf = cacheCourse.getTestLeaf(new Long(node)).booleanValue();
				if (!testLeaf) {
					// 查询当前节点所包含的作业
					List<MeleteTestModel> testList = cacheCourse.getTestListByParentId(node, CodeTable.section);
					nodeList.addAll(testList);
				}
				boolean forumLeaf = cacheCourse.getForumLeaf(new Long(node)).booleanValue();
				if (!forumLeaf) {
					// 查询当前节点所包含的讨论
					List<MeleteForumModel> forumList = cacheCourse.getForumListByParentId(node, CodeTable.section);
					nodeList.addAll(forumList);
				}
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

					boolean leaf = false;// 是否为叶子节点
					asyncTree.setLeaf(leaf);

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("type", CodeTable.course);// 节点类型：课程
					map.put("courseId", courseId);// 课程id
					map.put("status", status);// 状态
					map.put("studentrecordId", studyrecordId);// 学习记录Id
					asyncTree.setAttributes(map);

					asyncTree.setIcon(CodeTable.icoCourse);
					data.add(asyncTree);
				} else if (obj.getClass() == MeleteModuleModel.class) {// 是模块节点
					MeleteModuleModel module = (MeleteModuleModel) obj;
					if (CodeTable.hide.equals(module.getStatus().toString())) {// 若节点为隐藏
						continue;
					}
					// 获取模块学习记录
					MeleteModuleRecordModel moduleRecord = CacheUtil.getInstance().getStuModuleRecord(cacheStudy,
							module, studyrecordId);
					AsyncTreeModel asyncTree = new AsyncTreeModel();
					Long id = module.getId();
					String title = module.getTitle();
					asyncTree.setId(id.toString());
					asyncTree.setIcon(CodeTable.icoModule);

					// 显示序列号
					String showIdx = ((nodeIdx == null || nodeIdx.equals("")) ? "" : (nodeIdx + "."));
					showIdx += j;
					j++;
					asyncTree.setText(showIdx + " " + title);

					Boolean subLeafObj = cacheCourse.getModuleLeaf(id);// 是否有子模块
					boolean subLeaf = false;
					if (subLeafObj != null) {
						subLeaf = subLeafObj.booleanValue();
					}

					Boolean secLeafObj = cacheCourse.getSectionLeaf(id);// 是否有页
					boolean secLeaf = false;
					if (secLeafObj != null) {
						secLeaf = secLeafObj.booleanValue();
					}
					Boolean testLeafObj = cacheCourse.getTestLeaf(id);// 是否有作业
					boolean testLeaf = false;
					if (testLeafObj != null) {
						testLeaf = testLeafObj.booleanValue();
					}
					Boolean forumLeafObj = cacheCourse.getForumLeaf(id);// 是否有论坛
					boolean forumLeaf = false;
					if (forumLeafObj != null) {
						forumLeaf = forumLeafObj.booleanValue();
					}
					Boolean selftestLeafObj = cacheCourse.getSelftestLeaf(id);// 是否有前测
					boolean selftestLeaf = false;
					if (selftestLeafObj != null) {
						selftestLeaf = selftestLeafObj.booleanValue();
					}
					boolean leaf = (subLeaf && secLeaf && testLeaf && forumLeaf && selftestLeaf);// 是否叶子节点

					asyncTree.setLeaf(leaf);

					// String prerequids = module.getPrerequids();// 是否有开启条件

					Map<String, Object> map = new HashMap<String, Object>();

					map.put("idx", module.getIdx());// 节点序号
					map.put("type", CodeTable.module);// 节点类型：模块
					map.put("moduleTitle", title);// 模块title（用于点击模块是显示title用）
					map.put("studyTimeShow", module.getStudyDay());// 最少学习时间
					// 平均学习时间
					if (moduleRecord != null) {
						map.put("selfStudyTimeShow", moduleRecord.getStudyTime().toString());// 我的时长
					} else {
						map.put("selfStudyTimeShow", 0);// 我的时长
					}

					map.put("studentrecordId", studyrecordId);// 学习记录Id
					map.put("recordId", moduleRecord.getModulerecordId());// 模块学习记录Id
					// map.put("openStatus", moduleRecord.getOpenStatus());//
					// 开始状态
					// map.put("prerequids", prerequids);// 开启条件
					// map.put("status", moduleRecord.getStatus());// 通过状态
					map.put("showIdx", showIdx);
					// 构造主要属性字符串
					String mainAttr = "";
					// 必修选修图标
					String requierdStr = (module.getRequired().toString().equals(CodeTable.required)) ? CodeTable
							.getRequiredIco() : CodeTable.getElectiveIco();
					mainAttr = requierdStr; // 修改后的主要属性只显示必修或者选修
					map.put("mainAttr", mainAttr);// 主要属性
					map.put("courseId", module.getCourseId());// 所属课程id
					asyncTree.setAttributes(map);

					data.add(asyncTree);
				} else if (obj.getClass() == MeleteSectionModel.class) {// 是页节点
					MeleteSectionModel section = (MeleteSectionModel) obj;
					// 获取页记录
					MeleteSectionRecordModel sectionRecord = CacheUtil.getInstance().getStuSecRecord(cacheStudy,
							section, studyrecordId);
					AsyncTreeModel asyncTree = new AsyncTreeModel();
					asyncTree.setIcon(CodeTable.icoSection);
					Long id = section.getId();
					asyncTree.setId(id.toString());

					String showIdx = nodeIdx + "." + j;
					j++;
					String title = section.getTitle();
					asyncTree.setText(showIdx + " " + title);

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("moduleId", section.getModuleId());// 模块Id
					map.put("sectionRecordId", sectionRecord.getSectionrecordId().toString());// 节点记录Id，用于判断是否是第一次学习该节点
					map.put("studyTimeShow", section.getStudyTime());// 最少学习时长
					map.put("selfStudyTimeShow", sectionRecord.getStudyTime().toString());// 我的时长
					map.put("path", CourseUtil.getSectionPath(section.getPath()));// 课件存储路径
					map.put("type", CodeTable.section);// 节点类型：页
					map.put("recordId", sectionRecord.getSectionrecordId());// 页学习记录Id

					// 构造主要属性字符串
					String mainAttr = "";
					// 必修选修图标
					String requierdStr = section.getRequired().toString().equals(CodeTable.required) ? CodeTable
							.getRequiredIco() : CodeTable.getElectiveIco();
					mainAttr = requierdStr;
					map.put("mainAttr", mainAttr);// 主要属性
					asyncTree.setAttributes(map);

					Boolean testLeafObj = cacheCourse.getTestLeaf(id);// 是否有作业
					boolean testLeaf = false;
					if (testLeafObj != null) {
						testLeaf = testLeafObj.booleanValue();
					}
					Boolean forumLeafObj = cacheCourse.getForumLeaf(id);// 是否有讨论
					boolean forumLeaf = true;
					if (forumLeafObj != null) {
						forumLeaf = forumLeafObj.booleanValue();
					}
					Boolean selfLeafObj = cacheCourse.getSelftestLeaf(id);
					boolean selfLeaf = false;
					if (selfLeafObj != null) {
						selfLeaf = selfLeafObj.booleanValue();
					}
					boolean leaf = (selfLeaf && forumLeaf && testLeaf);// 是否叶子节点
					asyncTree.setLeaf(leaf);

					data.add(asyncTree);
				} else if (obj.getClass() == MeleteTestModel.class) {// 是作业节点
					MeleteTestModel test = (MeleteTestModel) obj;
					// 获取作业记录
					MeleteTestRecordModel testRecord = CacheUtil.getInstance().getStuTestRecord(cacheStudy, test,
							studyrecordId, cacheCourse.getCourse().getId());
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
					map.put("masteryScore", test.getMasteryScore());// 通过分数
					map.put("minTimeInterval", test.getMinTimeInterval());// 最小时间间隔
					map.put("lastCommitTime", testRecord.getLastCommitTime());// 最后提交时间
					map.put("isCaculateScore", test.getIsCaculateScore());// 是否计算平时成绩
					map.put("type", CodeTable.test);// 节点类型：作业
					map.put("studyTimeShow", "");// 学习时间
					map.put("mainAttr", qtip);// 主要属性
					map.put("recordId", testRecord.getTestrecordId());// 作业记录Id
					asyncTree.setAttributes(map);
					asyncTree.setIcon(CodeTable.icoTest);
					data.add(asyncTree);
				} else if (obj.getClass() == MeleteForumModel.class) {// 是讨论节点
					MeleteForumModel forum = (MeleteForumModel) obj;
					MeleteForumRecordModel forumRecord = CacheUtil.getInstance().getStuForumRecord(cacheStudy, forum,
							studyrecordId, cacheCourse.getCourse().getId());
					AsyncTreeModel asyncTree = new AsyncTreeModel();
					asyncTree.setAllowDrop(false);
					asyncTree.setAllowDrag(false);
					asyncTree.setId(forum.getId().toString());// 论坛id
					asyncTree.setText(forum.getName());

					String qtip = "";// 定义提示信息
					qtip += "建议发帖次数：" + (forum.getRequirement() == null ? "无" : forum.getRequirement()) + "<br>";
					asyncTree.setQtip(qtip);// 设置提示信息

					asyncTree.setLeaf(true);
					String areaId = forum.getAreaId();// 讨论区id
					String topicId = forum.getTopicId();// 帖子主题iid
					String forumId = forum.getForumId();// 论坛id

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("name", forum.getName());// 论坛名称
					map.put("requirement", forum.getRequirement());// 通过条件中文说明
					map.put("isCaculateScore", forum.getIsCaculateScore());// 是否计算平时成绩
					map.put("areaId", areaId);//
					map.put("topicId", topicId);// 帖子主题id
					map.put("forumId", forumId);// 论坛id
					map.put("type", CodeTable.forum);// 节点类型：讨论
					map.put("studyTimeShow", "");// 学习时间
					map.put("mainAttr", qtip);// 主要属性
					map.put("recordId", forumRecord.getForumrecordId());// 讨论记录Id
					asyncTree.setAttributes(map);
					asyncTree.setIcon(CodeTable.icoForum);
					data.add(asyncTree);
				} else if (obj.getClass() == MeleteSelfTestModel.class) {// 是前测节点
					MeleteSelfTestModel self = (MeleteSelfTestModel) obj;
					MeleteSelftestRecordModel selftestRecord = CacheUtil.getInstance().getStuSelftestRecord(cacheStudy,
							self, studyrecordId, cacheCourse.getCourse().getId());
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
					map.put("masteryScore", self.getMasteryScore());// 通过分数
					map.put("isCaculateScore", self.getIsCaculateScore());// 是否计算平时成绩
					map.put("idx", self.getIdx());// 前测序号
					map.put("attemptNumber", selftestRecord.getAttemptNumber());// 前测尝试次数
					map.put("type", CodeTable.selftest);// 节点类型：前测
					map.put("studyTimeShow", "");// 学习时间
					map.put("mainAttr", qtip);// 主要属性
					map.put("recordId", selftestRecord.getSelftestrecordId());// 前测记录Id
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
			StringWriter sw = new StringWriter();  
		    e.printStackTrace(new PrintWriter(sw, true));
			loggerManage.error("course json error-" + courseId + ":" + sw.toString());
			throw e;
		}
	}

	/**
	 * 点击页节点后跳转到播放课件页面
	 * 
	 * @return
	 * @param node
	 *            页id
	 * @param studyrecordId
	 *            学习记录id
	 * @throws IOException
	 */
	public String fowardCourseware() throws Exception {
		CacheElement cacheCourse = CacheUtil.getInstance().getCacheOfCourse(this.getCurrentSite().getId());
		CacheElement cacheStudy = CacheUtil.getInstance().getCacheOfStudyrecord(this.getCurrentSite().getId(),
				this.getCurrentUserId(), studyrecordId);
		MeleteStudyRecordModel studyRecord = cacheStudy.getStudyRecord();

		HttpServletRequest request = ServletActionContext.getRequest();
		try {
			String courseId = cacheCourse.getCourse().getId();
			MeleteSectionModel section = cacheCourse.getSecton(Long.valueOf(node));// 从缓存中获得页
			String path = CourseUtil.getSectionPath(section.getPath());
			String moduleId = section.getModuleId().toString();
			String studyTime = section.getStudyTime().toString();
			// 获取所有的同级页 zihongyan 2013-7-25
			getAllSectionModel(section.getModuleId());

			Long idx = 0l;// 当前页的序号
			for (int i = 0; i < nList.size(); i++) {
				if (section.getModuleId().equals(nList.get(i).getModuleId())
						&& section.getIdx().equals(nList.get(i).getIdx())) {
					idx = new Long(i);
					break;
				}
			}

			Long minIdx = new Long(0);// 第一页的序号
			Long maxIdx = new Long(nList.size() - 1);// 最后页的序号
			// 用于判断是否是第一页
			if (idx.equals(minIdx)) {
				request.setAttribute("previous", "false");
			} else {
				request.setAttribute("previous", "true");
			}
			// 用于判断是否是最后一页
			if (idx.equals(maxIdx)) {
				request.setAttribute("next", "false");
			} else {
				request.setAttribute("next", "true");
			}

			Date nowTime = new Date();// 当前时间
			StringBuilder moduleIdStr = new StringBuilder("");

			String moduleStrs = this.getModuleIdsbyId(moduleId, moduleIdStr);
			String[] ids = moduleStrs.split(",");
			for (int i = ids.length - 1; i >= 0; i--) {
				MeleteModuleRecordModel existRecord = cacheStudy.getModuleRecord(Long.valueOf(ids[i]));// 从缓存获得模块记录
				if (existRecord.getStartStudyTime() == null) {
					// 若模块节点记录的开始时间为空则设置开始时间
					existRecord.setStartStudyTime(nowTime);
				}
				existRecord.setOpenStatus(Long.valueOf(CodeTable.openStatusYes));
				studyService.updateModel(existRecord);// 将更改写入数据库
				cacheStudy.editModuleRecord(existRecord);// 将更改写入缓存
			}
			MeleteSectionRecordModel sectionRecord = cacheStudy.getSectonRecord(Long.valueOf(node));// 从缓存中获得页节点记录
			String sectionRecordId = sectionRecord.getSectionrecordId().toString();
			// String status = sectionRecord.getStatus().toString();
			String selfStudyTime = sectionRecord.getStudyTime().toString();
			sectionRecord.setOpenStatus(Long.valueOf(CodeTable.openStatusYes));
			if (sectionRecord.getStartStudyTime() == null) {
				sectionRecord.setStartStudyTime(nowTime);
			}
			studyService.updateModel(sectionRecord);
			cacheStudy.editSectionRecord(sectionRecord);// 更改缓存的页记录

			// 保存节点学习详细记录
			MeleteSectionRecordDetailModel sectionDetail = new MeleteSectionRecordDetailModel();
			sectionDetail.setSectionrecordId(Long.valueOf(sectionRecordId));// 节点记录ID
			sectionDetail.setStartStudyTime(nowTime);
			// 设置结束时间为 开始时间 + 向服务器提交时间的一半
			sectionDetail.setEndStudyTime(new Date(nowTime.getTime() + new Long(Constants.SUBMIT_INTERVAL) / 2));
			studyService.saveSectionRecordDetail(sectionDetail);
			request.setAttribute("coursewarePath", path);
			request.setAttribute("moduleId", moduleId);
			request.setAttribute("sectionId", node);
			request.setAttribute("sectionRecordId", sectionRecordId);
			// request.setAttribute("status", status);
			request.setAttribute("selfStudyTime", selfStudyTime);
			request.setAttribute("studyTime", studyTime);
			request.setAttribute("submitInterval", Constants.SUBMIT_INTERVAL);
			request.setAttribute("promptInterval", Constants.PROMPT_INTERVAL);
			request.setAttribute("skipInterval", Constants.SKIP_INTERVAL);
			request.setAttribute("studyrecordId", studyRecord.getStudyrecordId().toString());

			// 根据节的id获得 章的id chenbin 2013-7-3
			Long rootModuleId = getRootModuleId(section.getModuleId());
			request.setAttribute("rootModuleId", rootModuleId);
			// 保存学习记录的值 zihongyan 2013-4-3

			String studentId = this.getCurrentUserId();
			executeSaveStudyHistory(studentId, courseId);

			request.setAttribute("detailRecordId", sectionDetail.getSectionrecorddetailId().toString());

			String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
			request.setAttribute("mainDomain", url);
			String agency = path.substring(0, path.indexOf(courseId) + courseId.length()) + "/agency.htm";
			request.setAttribute("agency", agency);
			return "courseware";
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter();  
		    e.printStackTrace(new PrintWriter(sw, true));
			loggerManage.error("course foward error-" + courseId + ":" + sw.toString());
			throw e;
		}
	}

	/**
	 * 获取上一个页节点课件信息
	 * 
	 * @param node
	 *            页id
	 * @param detailRecordId
	 *            明细记录id
	 * @param studyrecordId
	 *            学习记录id
	 * @return
	 * @throws Exception
	 */
	public String getPreviousSection() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		CacheElement cacheCourse = CacheUtil.getInstance().getCacheOfCourse(this.getCurrentSite().getId());
		CacheElement cacheStudy = CacheUtil.getInstance().getCacheOfStudyrecord(this.getCurrentSite().getId(),
				this.getCurrentUserId(), studyrecordId);
		MeleteStudyRecordModel studyRecord = cacheStudy.getStudyRecord();

		String courseId = cacheCourse.getCourse().getId();
		try {
			this.updateRecord(studyRecord, detailRecordId);// 在学习上一页前
															// 将当前页的相关信息更新

			MeleteSectionModel section = cacheCourse.getSecton(new Long(node));
			String moduleId = section.getModuleId().toString();

			MeleteSectionModel preSection = null;

			// 获取所有的同级页 zihongyan 2013-7-25
			getAllSectionModel(section.getModuleId());

			int idx = 0;
			for (int i = 0; i < nList.size(); i++) {
				MeleteSectionModel tSection = nList.get(i);
				if (tSection.getId().equals(section.getId())) {
					idx = i - 1;
					preSection = nList.get(idx);
					if (idx == 0) {// 是第一页
						request.setAttribute("previous", "false");
					} else {
						request.setAttribute("previous", "true");
					}
					break;
				}
			}

			request.setAttribute("next", "true");

			MeleteSectionRecordModel sectionRecord = cacheStudy.getSectonRecord(preSection.getId());// 从缓存获得页记录

			Date nowTime = new Date();// 当前时间
			StringBuilder moduleIdStr = new StringBuilder("");

			String moduleStrs = this.getModuleIdsbyId(moduleId, moduleIdStr);
			String[] ids = moduleStrs.split(",");
			for (int i = ids.length - 1; i >= 0; i--) {
				MeleteModuleRecordModel existRecord = cacheStudy.getModuleRecord(Long.valueOf(ids[i]));// 从缓存获得模块记录
				if (existRecord.getStartStudyTime() == null) {
					// 若模块节点记录的开始时间为空则设置开始时间
					existRecord.setStartStudyTime(nowTime);
				}
				existRecord.setOpenStatus(Long.valueOf(CodeTable.openStatusYes));
				studyService.updateModel(existRecord);// 将更改写入数据库
				cacheStudy.editModuleRecord(existRecord);// 将更改写入缓存
			}

			sectionRecord.setOpenStatus(Long.valueOf(CodeTable.openStatusYes));
			if (sectionRecord.getStartStudyTime() == null) {
				sectionRecord.setStartStudyTime(nowTime);
			}
			studyService.updateModel(sectionRecord);
			cacheStudy.editSectionRecord(sectionRecord);// 更改缓存的页记录

			// 保存节点学习详细记录
			MeleteSectionRecordDetailModel sectionDetail = new MeleteSectionRecordDetailModel();
			sectionDetail.setSectionrecordId(sectionRecord.getSectionrecordId());// 节点记录ID
			sectionDetail.setStartStudyTime(nowTime);
			// 设置结束时间为 开始时间 + 向服务器提交时间的一半
			sectionDetail.setEndStudyTime(new Date(nowTime.getTime() + new Long(Constants.SUBMIT_INTERVAL) / 2));
			studyService.saveSectionRecordDetail(sectionDetail);

			String path = CourseUtil.getSectionPath(preSection.getPath());
			request.setAttribute("coursewarePath", path);
			request.setAttribute("moduleId", moduleId);
			request.setAttribute("sectionId", preSection.getId().toString());
			request.setAttribute("sectionRecordId", sectionRecord.getSectionrecordId().toString());
			// request.setAttribute("status",
			// sectionRecord.getStatus().toString());
			request.setAttribute("selfStudyTime", sectionRecord.getStudyTime().toString());
			request.setAttribute("studyTime", preSection.getStudyTime().toString());
			request.setAttribute("submitInterval", Constants.SUBMIT_INTERVAL);
			request.setAttribute("promptInterval", Constants.PROMPT_INTERVAL);
			request.setAttribute("skipInterval", Constants.SKIP_INTERVAL);
			request.setAttribute("studyrecordId", studyRecord.getStudyrecordId().toString());
			request.setAttribute("detailRecordId", sectionDetail.getSectionrecorddetailId().toString());

			// 保存学习记录的值 zihongyan 2013-4-3
			// 根据节的id获得 章的id chenbin 2013-7-3
			Long rootModuleId = getRootModuleId(section.getModuleId());
			request.setAttribute("rootModuleId", rootModuleId);

			String studentId = this.getCurrentUserId();
			executeSaveStudyHistory(studentId, courseId);

			String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
			request.setAttribute("mainDomain", url);
			String agency = path.substring(0, path.indexOf(courseId) + courseId.length()) + "/agency.htm";
			request.setAttribute("agency", agency);
			return "courseware";
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter();  
		    e.printStackTrace(new PrintWriter(sw, true));
			loggerManage.error("course pre section error-" + courseId + ":" + sw.toString());
			throw e;
		}
	}

	/**
	 * 检查下一页是否可以开启
	 * 
	 * @return
	 * @param studyrecordId
	 * @param detailRecordId
	 * @param node
	 * @throws Exception
	 */
	public String checkNextSectionStatus() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		CacheElement cacheCourse = CacheUtil.getInstance().getCacheOfCourse(this.getCurrentSite().getId());
		CacheElement cacheStudy = CacheUtil.getInstance().getCacheOfStudyrecord(this.getCurrentSite().getId(),
				this.getCurrentUserId(), studyrecordId);
		MeleteStudyRecordModel studyRecord = cacheStudy.getStudyRecord();

		String res = "true";
		try {
			this.updateRecord(studyRecord, detailRecordId);// 更新当前页的相关信息

			// 获取当前页记录
			MeleteSectionRecordModel curSectionRecord = cacheStudy.getSectonRecord(Long.valueOf(node));
			// 获取当前页
			MeleteSectionModel curSection = cacheCourse.getSecton(new Long(node));
			String moduleId = curSection.getModuleId().toString();
			MeleteSectionModel nextSection = null;// 下一页

			List<MeleteSectionModel> sectionList = cacheCourse.getSectionListByModuleId(moduleId);
			for (int i = 0; i < sectionList.size(); i++) {
				MeleteSectionModel tSection = sectionList.get(i);
				if (tSection.getId() == curSection.getId()) {
					nextSection = sectionList.get(i + 1);
					break;
				}
			}
			String prerequids = nextSection.getPrerequids();// 下一页的开启条件
			if (prerequids.equals(CodeTable.prerequisiteYes)) {// 有开启条件
				if (curSectionRecord.getStatus().toString().equals(CodeTable.passStatusNo)) {// 当前页未通过，则不能开启下一页
					res = "false";
				}
			}
			response.getWriter().println("{success:" + res + "}");
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 获取下一个页节点课件
	 * 
	 * @param node
	 *            页id
	 * @param detailRecordId
	 *            明细记录id
	 * @param studyrecordId
	 *            学习记录id
	 * @return
	 * @throws Exception
	 */
	public String getNextSection() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		CacheElement cacheCourse = CacheUtil.getInstance().getCacheOfCourse(this.getCurrentSite().getId());
		CacheElement cacheStudy = CacheUtil.getInstance().getCacheOfStudyrecord(this.getCurrentSite().getId(),
				this.getCurrentUserId(), studyrecordId);
		String courseId = cacheCourse.getCourse().getId();
		MeleteStudyRecordModel studyRecord = cacheStudy.getStudyRecord();

		try {
			// 获取当前页
			MeleteSectionModel curSection = cacheCourse.getSecton(new Long(node));

			String moduleId = curSection.getModuleId().toString();
			MeleteSectionModel nextSection = null;// 下一页
			// 获取所有的同级页 zihongyan 2013-7-25
			getAllSectionModel(curSection.getModuleId());
			int idx = 0;

			for (int i = 0; i < nList.size(); i++) {
				MeleteSectionModel tSection = nList.get(i);
				if (tSection.getId().equals(curSection.getId())) {
					idx = i + 1; // 下一页序号
					nextSection = nList.get(idx);
					// 用于判断是否是最后一页
					if (idx == nList.size() - 1) {
						request.setAttribute("next", "false");
					} else {
						request.setAttribute("next", "true");
					}
					break;
				}
			}

			request.setAttribute("previous", "true");

			MeleteSectionRecordModel sectionRecord = cacheStudy.getSectonRecord(nextSection.getId());// 从缓存获得页记录

			Date nowTime = new Date();// 当前时间
			StringBuilder moduleIdStr = new StringBuilder("");

			String moduleStrs = this.getModuleIdsbyId(moduleId, moduleIdStr);
			String[] ids = moduleStrs.split(",");
			for (int i = ids.length - 1; i >= 0; i--) {
				MeleteModuleRecordModel existRecord = cacheStudy.getModuleRecord(Long.valueOf(ids[i]));// 从缓存获得模块记录
				if (existRecord.getStartStudyTime() == null) {
					// 若模块节点记录的开始时间为空则设置开始时间
					existRecord.setStartStudyTime(nowTime);
				}
				existRecord.setOpenStatus(Long.valueOf(CodeTable.openStatusYes));
				studyService.updateModel(existRecord);// 将更改写入数据库
				cacheStudy.editModuleRecord(existRecord);// 将更改写入缓存
			}

			sectionRecord.setOpenStatus(Long.valueOf(CodeTable.openStatusYes));
			if (sectionRecord.getStartStudyTime() == null) {
				sectionRecord.setStartStudyTime(nowTime);
			}
			studyService.updateModel(sectionRecord);
			cacheStudy.editSectionRecord(sectionRecord);// 更改缓存的页记录

			// 保存节点学习详细记录
			MeleteSectionRecordDetailModel sectionDetail = new MeleteSectionRecordDetailModel();
			sectionDetail.setSectionrecordId(sectionRecord.getSectionrecordId());// 节点记录ID
			sectionDetail.setStartStudyTime(nowTime);
			// 设置结束时间为 开始时间 + 向服务器提交时间的一半
			sectionDetail.setEndStudyTime(new Date(nowTime.getTime() + new Long(Constants.SUBMIT_INTERVAL) / 2));
			studyService.saveSectionRecordDetail(sectionDetail);

			String path = CourseUtil.getSectionPath(nextSection.getPath());
			request.setAttribute("coursewarePath", path);
			request.setAttribute("moduleId", moduleId);
			request.setAttribute("sectionId", nextSection.getId().toString());
			request.setAttribute("sectionRecordId", sectionRecord.getSectionrecordId().toString());
			// request.setAttribute("status",
			// sectionRecord.getStatus().toString());
			request.setAttribute("selfStudyTime", sectionRecord.getStudyTime().toString());
			request.setAttribute("studyTime", nextSection.getStudyTime().toString());
			request.setAttribute("submitInterval", Constants.SUBMIT_INTERVAL);
			request.setAttribute("promptInterval", Constants.PROMPT_INTERVAL);
			request.setAttribute("skipInterval", Constants.SKIP_INTERVAL);
			request.setAttribute("studyrecordId", studyRecord.getStudyrecordId().toString());
			request.setAttribute("detailRecordId", sectionDetail.getSectionrecorddetailId().toString());

			// 根据节的id获得 章的id chenbin 2013-7-3
			MeleteSectionModel section = cacheCourse.getSecton(new Long(node));
			Long rootModuleId = getRootModuleId(section.getModuleId());
			request.setAttribute("rootModuleId", rootModuleId);

			// 保存学习记录的值 zihongyan 2013-4-3
			String studentId = this.getCurrentUserId();
			executeSaveStudyHistory(studentId, courseId);

			String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
			request.setAttribute("mainDomain", url);
			String agency = path.substring(0, path.indexOf(courseId) + courseId.length()) + "/agency.htm";
			request.setAttribute("agency", agency);
			return "courseware";

		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter();  
		    e.printStackTrace(new PrintWriter(sw, true));
			loggerManage.error("course next section error-" + courseId + ":" + sw.toString());
			throw e;
		}
	}

	/**
	 * 点击页节点后跳转到模块课程列表界面 包含导航信息
	 * 
	 * @param node
	 *            节点id
	 * @param studyrecordId
	 * @return
	 * @throws IOException
	 */
	public String fowardModule() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		try {
			CacheElement cacheCourse = CacheUtil.getInstance().getCacheOfCourse(this.getCurrentSite().getId());
			CacheElement cacheStudy = CacheUtil.getInstance().getCacheOfStudyrecord(this.getCurrentSite().getId(),
					this.getCurrentUserId(), studyrecordId);
			MeleteStudyRecordModel studyRecord = cacheStudy.getStudyRecord();

			MeleteModuleModel module = cacheCourse.getModule(Long.valueOf(node));

			List<MeleteModuleModel> brotherList = cacheCourse
					.getModuleListByParentid(module.getParentId() == null ? null : module.getParentId().toString());
			MeleteModuleModel firstModule = brotherList.get(0);
			MeleteModuleModel lastModule = brotherList.get(brotherList.size() - 1);

			Long idx = module.getIdx();
			Long minIdx = firstModule.getIdx();
			Long maxIdx = lastModule.getIdx();
			// 用于判断是否是最后一页
			if (idx.equals(maxIdx)) {
				request.setAttribute("next", "false");
			} else {
				request.setAttribute("next", "true");
			}

			// 用于判断是否是第一页
			if (idx.equals(minIdx)) {
				request.setAttribute("previous", "false");
			} else {
				request.setAttribute("previous", "true");
			}
			request.setAttribute("nodeId", node);
			// 根据moduleId 获取title值
			request.setAttribute("nodeText", module.getTitle());
			request.setAttribute("description", module.getDescription());
			request.setAttribute("studyrecordId", studyRecord.getStudyrecordId().toString());
			templateCommon = "default";
			return "module";
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 下一模块
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getNextModule() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		try {
			CacheElement cacheCourse = CacheUtil.getInstance().getCacheOfCourse(this.getCurrentSite().getId());
			CacheElement cacheStudy = CacheUtil.getInstance().getCacheOfStudyrecord(this.getCurrentSite().getId(),
					this.getCurrentUserId(), studyrecordId);
			MeleteStudyRecordModel studyRecord = cacheStudy.getStudyRecord();

			MeleteModuleModel curModule = cacheCourse.getModule(Long.valueOf(node));
			List<MeleteModuleModel> brotherList = cacheCourse
					.getModuleListByParentid(curModule.getParentId() == null ? null : curModule.getParentId()
							.toString());
			MeleteModuleModel lastModule = brotherList.get(brotherList.size() - 1);
			MeleteModuleModel module = null;
			for (int i = 0; i < brotherList.size(); i++) {
				MeleteModuleModel tempModel = brotherList.get(i);
				if (tempModel.getId().toString().equals(node)) {
					if (i == brotherList.size() - 1) {
						module = null;
					} else {
						module = brotherList.get(i + 1);
					}
				}
			}
			Long idx = 0L;
			if (module != null) {
				idx = module.getIdx();
			}
			Long maxIdx = 0L;
			if (lastModule != null) {
				maxIdx = lastModule.getIdx();
			}
			// 用于判断是否是最后一页
			if (idx.equals(maxIdx)) {
				request.setAttribute("next", "false");
			} else {
				request.setAttribute("next", "true");
			}
			request.setAttribute("nodeId", module.getId().toString());
			request.setAttribute("nodeType", "2");
			// 根据moduleId 获取title值
			request.setAttribute("nodeText", module.getTitle());
			request.setAttribute("description", module.getDescription());
			request.setAttribute("studyrecordId", studyRecord.getStudyrecordId().toString());
			return "module";
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 上一模块
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getPreviousModule() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		try {
			CacheElement cacheCourse = CacheUtil.getInstance().getCacheOfCourse(this.getCurrentSite().getId());
			CacheElement cacheStudy = CacheUtil.getInstance().getCacheOfStudyrecord(this.getCurrentSite().getId(),
					this.getCurrentUserId(), studyrecordId);
			MeleteStudyRecordModel studyRecord = cacheStudy.getStudyRecord();

			MeleteModuleModel curModule = cacheCourse.getModule(Long.valueOf(node));
			List<MeleteModuleModel> brotherList = cacheCourse
					.getModuleListByParentid(curModule.getParentId() == null ? null : curModule.getParentId()
							.toString());
			MeleteModuleModel firstModule = brotherList.get(0);
			MeleteModuleModel module = null;
			for (int i = brotherList.size() - 1; i >= 0; i--) {
				MeleteModuleModel tempModel = brotherList.get(i);
				if (tempModel.getId().toString().equals(node)) {
					if (i == 0) {
						module = null;
					} else {
						module = brotherList.get(i - 1);
					}
				}
			}

			Long idx = 0L;
			if (module != null) {
				idx = module.getIdx();
			}
			Long minIdx = 0L;
			if (firstModule != null) {
				minIdx = firstModule.getIdx();
			}

			// 用于判断是否是第一页
			if (idx.equals(minIdx)) {
				request.setAttribute("previous", "false");
			} else {
				request.setAttribute("previous", "true");
			}
			request.setAttribute("nodeId", module.getId().toString());
			request.setAttribute("nodeType", "2");
			// 根据moduleId 获取title值
			request.setAttribute("nodeText", module.getTitle());
			request.setAttribute("description", module.getDescription());
			request.setAttribute("studyrecordId", studyRecord.getStudyrecordId().toString());
			return "module";
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 通过章节加载课程树
	 * 
	 * @param studyrecordId
	 * @param node
	 * @param nodeIdx
	 * @param nodeType
	 * @return
	 * @throws Exception
	 */
	public String loadModuleCourse() throws Exception {
		try {
			CacheElement cacheCourse = CacheUtil.getInstance().getCacheOfCourse(this.getCurrentSite().getId());
			CacheElement cacheStudy = CacheUtil.getInstance().getCacheOfStudyrecord(this.getCurrentSite().getId(),
					this.getCurrentUserId(), studyrecordId);
			MeleteStudyRecordModel studyRecord = cacheStudy.getStudyRecord();

			// 定义查询出的所有节点集合
			List<Object> nodeList = new ArrayList<Object>();
			// 定义要返回的节点集合
			List<AsyncTreeModel> data = new ArrayList<AsyncTreeModel>();
			if (nodeType == null || nodeType.equals(CodeTable.module)) {// 节点类型是“模块”
				boolean moduleLeaf = cacheCourse.getModuleLeaf(new Long(node)).booleanValue();
				if (!moduleLeaf) {
					// 查询当前模块的下级模块
					List<MeleteModuleModel> moduleList = cacheCourse.getModuleListByParentid(node);
					nodeList.addAll(moduleList);
				}
				boolean secLeaf = cacheCourse.getSectionLeaf(new Long(node)).booleanValue();
				if (!secLeaf) {
					// 查询当前模块所包含的页
					List<MeleteSectionModel> sectionList = cacheCourse.getSectionListByModuleId(node);
					nodeList.addAll(sectionList);
				}
				boolean selftestLeaf = cacheCourse.getSelftestLeaf(new Long(node)).booleanValue();
				if (!selftestLeaf) {
					// 查询当前模块所包含的前测
					List<MeleteSelfTestModel> selfList = cacheCourse.getSelftestListByParentId(node, CodeTable.module);
					nodeList.addAll(selfList);
				}
				boolean testLeaf = cacheCourse.getTestLeaf(new Long(node)).booleanValue();
				if (!testLeaf) {
					// 查询当前模块所包含的作业
					List<MeleteTestModel> testList = cacheCourse.getTestListByParentId(node, CodeTable.module);
					nodeList.addAll(testList);
				}
				boolean forumLeaf = cacheCourse.getForumLeaf(new Long(node)).booleanValue();
				if (!forumLeaf) {
					// 查询当前模块所包含的讨论
					List<MeleteForumModel> forumList = cacheCourse.getForumListByParentId(node, CodeTable.module);
					nodeList.addAll(forumList);
				}
			} else if (nodeType.equals(CodeTable.section)) {// 节点类型是页
				boolean selftestLeaf = cacheCourse.getSelftestLeaf(new Long(node)).booleanValue();
				if (!selftestLeaf) {
					// 查询当前节点所包含的前测
					List<MeleteSelfTestModel> selfList = cacheCourse.getSelftestListByParentId(node, CodeTable.section);
					nodeList.addAll(selfList);
				}
				boolean testLeaf = cacheCourse.getTestLeaf(new Long(node)).booleanValue();
				if (!testLeaf) {
					// 查询当前节点所包含的作业
					List<MeleteTestModel> testList = cacheCourse.getTestListByParentId(node, CodeTable.section);
					nodeList.addAll(testList);
				}
				boolean forumLeaf = cacheCourse.getForumLeaf(new Long(node)).booleanValue();
				if (!forumLeaf) {
					// 查询当前节点所包含的讨论
					List<MeleteForumModel> forumList = cacheCourse.getForumListByParentId(node, CodeTable.section);
					nodeList.addAll(forumList);
				}
			}

			int j = 1;
			for (int i = 0; i < nodeList.size(); i++) {// 遍历节点集合
				Object obj = nodeList.get(i);
				if (obj.getClass() == MeleteModuleModel.class) {// 是模块节点
					MeleteModuleModel module = (MeleteModuleModel) obj;
					MeleteModuleRecordModel moduleRecord = cacheStudy.getModuleRecord(module.getId());

					AsyncTreeModel asyncTree = new AsyncTreeModel();
					Long id = module.getId();
					String title = module.getTitle();
					asyncTree.setId(id.toString());
					asyncTree.setIcon(CodeTable.icoModule);

					boolean subLeaf = cacheCourse.getModuleLeaf(id).booleanValue();// 是否有子模块
					boolean secLeaf = cacheCourse.getSectionLeaf(id).booleanValue();// 是否有页
					boolean testLeaf = cacheCourse.getTestLeaf(id).booleanValue();// 是否有作业
					boolean forumLeaf = cacheCourse.getForumLeaf(id).booleanValue();// 是否有讨论
					boolean selfLeaf = cacheCourse.getSelftestLeaf(id).booleanValue();// 是否有前测
					boolean leaf = (subLeaf && secLeaf && testLeaf && forumLeaf && selfLeaf);
					asyncTree.setLeaf(leaf);

					String showIdx = ((nodeIdx == null || nodeIdx.equals("")) ? "" : (nodeIdx + "."));
					showIdx += j;
					j++;
					asyncTree.setText(showIdx + " " + title);

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("courseId", module.getCourseId());// 所属课程id
					map.put("idx", module.getIdx());// 节点序号
					map.put("type", CodeTable.module);// 节点类型：模块
					map.put("moduleTitle", title);// 模块title（用于点击模块是显示title用）
					map.put("recordId", moduleRecord.getModulerecordId());
					map.put("studyTimeShow", module.getStudyDay());// 学习时间
					map.put("studentrecordId", studyRecord.getStudyrecordId());
					map.put("selfStudyTimeShow", moduleRecord.getStudyTime().toString());// 我的时长
					// map.put("status", moduleRecord.getStatus());// 通过状态
					// map.put("openStatus", moduleRecord.getOpenStatus());//
					// 开启状态
					map.put("showIdx", showIdx);
					// 构造主要属性字符串
					String mainAttr = "";
					String requierdStr = (module.getRequired().toString().equals(CodeTable.required)) ? CodeTable
							.getRequiredIco() : CodeTable.getElectiveIco();
					mainAttr = requierdStr;
					map.put("mainAttr", mainAttr);// 主要属性

					asyncTree.setAttributes(map);
					data.add(asyncTree);
				} else if (obj.getClass() == MeleteSectionModel.class) {// 是页节点
					MeleteSectionModel section = (MeleteSectionModel) obj;
					MeleteSectionRecordModel sectionRecord = cacheStudy.getSectonRecord(section.getId());

					AsyncTreeModel asyncTree = new AsyncTreeModel();
					Long id = section.getId();
					asyncTree.setId(id.toString());

					String showIdx = (nodeIdx == null ? "" : (nodeIdx + ".")) + j;
					j++;
					String title = section.getTitle();
					asyncTree.setText(showIdx + " " + title);

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("moduleId", section.getModuleId());// 模块Id
					map.put("sectionRecordId", String.valueOf(sectionRecord.getSectionrecordId()));// 节点记录Id，用于判断是否是第一次学习该节点
					map.put("studyTimeShow", section.getStudyTime());// 学习时间
					map.put("selfStudyTimeShow", String.valueOf(sectionRecord.getStudyTime()));// 我的时长
					map.put("path", CourseUtil.getSectionPath(section.getPath()));// 课件存储路径
					// map.put("status", sectionRecord.getStatus());// 通过状态
					map.put("type", CodeTable.section);// 节点类型：页
					// map.put("openStatus", sectionRecord.getOpenStatus());//
					// 开启状态
					// 构造主要属性字符串
					String mainAttr = "";
					String requierdStr = (section.getRequired().toString().equals(CodeTable.required)) ? CodeTable
							.getRequiredIco() : CodeTable.getElectiveIco();
					mainAttr = requierdStr;
					map.put("mainAttr", mainAttr);// 主要属性

					asyncTree.setAttributes(map);

					boolean testLeaf = cacheCourse.getTestLeaf(id).booleanValue();// 是否有作业
					boolean forumLeaf = cacheCourse.getForumLeaf(id).booleanValue();// 是否有讨论
					boolean selfLeaf = cacheCourse.getSelftestLeaf(id).booleanValue();// 是否有前测
					boolean leaf = testLeaf && forumLeaf && selfLeaf;
					asyncTree.setLeaf(leaf);
					asyncTree.setIcon(CodeTable.icoSection);

					data.add(asyncTree);
				} else if (obj.getClass() == MeleteTestModel.class) {// 是作业节点
					MeleteTestModel test = (MeleteTestModel) obj;
					MeleteTestRecordModel testRecord = cacheStudy.getTestRecord(test.getId());

					AsyncTreeModel asyncTree = new AsyncTreeModel();
					asyncTree.setAllowDrop(false);
					asyncTree.setAllowDrag(false);
					asyncTree.setId(test.getId().toString());// 作业id
					asyncTree.setText(test.getName());// 作业名称

					String qtip = "";// 定义提示信息
					qtip += "通过条件：" + (test.getRequirement() == null ? "无" : test.getRequirement()) + "<br>";
					asyncTree.setQtip(qtip);// 设置提示信息

					asyncTree.setLeaf(true);

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("name", test.getName());// 作业名称
					map.put("samepaper", test.getSamepaper());// 使用同一策略
					map.put("schemaId", test.getSchemaId());// 策略id
					map.put("totalScore", test.getTotalScore());// 总分
					map.put("masteryScore", test.getMasteryScore());// 通过分数
					map.put("minTimeInterval", test.getMinTimeInterval());// 最小时间间隔
					map.put("lastCommitTime", testRecord.getLastCommitTime());// 最后提交时间
					map.put("isCaculateScore", test.getIsCaculateScore());// 是否计算平时成绩
					map.put("type", CodeTable.test);// 节点类型：作业
					map.put("studyTimeShow", "");// 学习时间
					map.put("mainAttr", "");// 主要属性
					map.put("recordId", testRecord.getTestrecordId());// 作业记录Id
					asyncTree.setAttributes(map);
					asyncTree.setIcon(CodeTable.icoTest);
					data.add(asyncTree);
				} else if (obj.getClass() == MeleteForumModel.class) {// 是讨论节点
					MeleteForumModel forum = (MeleteForumModel) obj;
					MeleteForumRecordModel forumRecord = cacheStudy.getForumRecord(forum.getId());
					AsyncTreeModel asyncTree = new AsyncTreeModel();
					asyncTree.setAllowDrop(false);
					asyncTree.setAllowDrag(false);
					asyncTree.setId(forum.getId().toString());// 论坛id
					asyncTree.setText(forum.getName());

					String qtip = "";// 定义提示信息
					qtip += "通过条件：" + (forum.getRequirement() == null ? "无" : forum.getRequirement()) + "<br>";
					asyncTree.setQtip(qtip);// 设置提示信息

					asyncTree.setLeaf(true);
					String areaId = forum.getAreaId();
					String topicId = forum.getTopicId();// 帖子id
					String forumId = forum.getForumId();// 论坛主题id

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("name", forum.getName());// 论坛名称
					map.put("isCaculateScore", forum.getIsCaculateScore());// 是否计算平时成绩
					map.put("areaId", areaId);// 帖子id
					map.put("topicId", topicId);// 帖子id
					map.put("forumId", forumId);// 论坛主题id
					map.put("type", CodeTable.forum);// 节点类型：讨论
					map.put("studyTimeShow", "");// 学习时间
					map.put("mainAttr", "");// 主要属性
					map.put("recordId", forumRecord.getForumrecordId());// 讨论记录Id
					asyncTree.setAttributes(map);
					asyncTree.setIcon(CodeTable.icoForum);
					data.add(asyncTree);
				} else if (obj.getClass() == MeleteSelfTestModel.class) {// 是前测节点
					MeleteSelfTestModel self = (MeleteSelfTestModel) obj;
					MeleteSelftestRecordModel selftestRecord = cacheStudy.getSelftestRecord(self.getId());
					AsyncTreeModel asyncTree = new AsyncTreeModel();
					asyncTree.setAllowDrop(false);
					asyncTree.setAllowDrag(false);
					asyncTree.setId(self.getId().toString());// 前测id
					asyncTree.setText(self.getName());

					String qtip = "";// 定义提示信息
					qtip += "通过条件：" + (self.getRequirement() == null ? "无" : self.getRequirement()) + "<br>";
					asyncTree.setQtip(qtip);// 设置提示信息

					asyncTree.setLeaf(true);

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("name", self.getName());// 前测名称
					map.put("belongType", self.getBelongType());// 所属类型
					map.put("samepaper", self.getSamepaper());// 使用同一策略
					map.put("schemaId", self.getSchemaId());// 策略id
					map.put("totalScore", self.getTotalScore());// 总分
					map.put("masteryScore", self.getMasteryScore());// 通过分数
					map.put("isCaculateScore", self.getIsCaculateScore());// 是否
					map.put("idx", self.getIdx());// 前测序号
					map.put("type", CodeTable.selftest);// 节点类型：前测
					map.put("studyTimeShow", "");// 学习时间
					map.put("mainAttr", "");// 主要属性
					map.put("recordId", selftestRecord.getSelftestrecordId());// 前测记录Id
					map.put("attemptNumber", selftestRecord.getAttemptNumber());// 前测尝试次数计算平时成绩
					asyncTree.setAttributes(map);
					asyncTree.setIcon(CodeTable.icoSelftest);
					data.add(asyncTree);
				}
			}

			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			String json = "";
			if (data.size() != 0) {
				json = JsonBuilder.builderAsyncTreeJson(data);
			}
			response.getWriter().println(json);

			return null;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	public String saveSectionRecord() throws Exception {
		CacheElement cacheStudy = CacheUtil.getInstance().getCacheOfStudyrecord(this.getCurrentSite().getId(),
				this.getCurrentUserId(), studyrecordId);

		Date nowTime = new Date();// 当前时间
		StringBuilder moduleIdStr = new StringBuilder("");

		String moduleStrs = this.getModuleIdsbyId(moduleId, moduleIdStr);
		String[] ids = moduleStrs.split(",");
		for (int i = ids.length - 1; i >= 0; i--) {
			MeleteModuleRecordModel existRecord = cacheStudy.getModuleRecord(Long.valueOf(ids[i]));// 从缓存获得模块记录
			if (existRecord.getStartStudyTime() == null) {
				// 若模块节点记录的开始时间为空则设置开始时间
				existRecord.setStartStudyTime(nowTime);
			}
			existRecord.setOpenStatus(Long.valueOf(CodeTable.openStatusYes));
			studyService.updateModel(existRecord);// 将更改写入数据库
			cacheStudy.editModuleRecord(existRecord);// 将更改写入缓存
		}
		MeleteSectionRecordModel sectionRecord = cacheStudy.getSectonRecord(Long.valueOf(node));// 从缓存中获得页节点记录
		sectionRecord.setOpenStatus(Long.valueOf(CodeTable.openStatusYes));
		if (sectionRecord.getStartStudyTime() == null) {
			sectionRecord.setStartStudyTime(nowTime);
		}
		studyService.updateModel(sectionRecord);
		cacheStudy.editSectionRecord(sectionRecord);// 更改缓存的页记录

		// 保存节点学习详细记录
		MeleteSectionRecordDetailModel sectionDetail = new MeleteSectionRecordDetailModel();
		sectionDetail.setSectionrecordId(Long.valueOf(sectionRecordId));// 节点记录ID
		sectionDetail.setStartStudyTime(nowTime);
		// 设置结束时间为 开始时间 + 向服务器提交时间的一半
		sectionDetail.setEndStudyTime(new Date(nowTime.getTime() + new Long(Constants.SUBMIT_INTERVAL) / 2));
		studyService.saveSectionRecordDetail(sectionDetail);
		// 将节点详细学习记录信息Id返回到页面（用于更新该详细学习记录信息表数据）
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().println(sectionDetail.getSectionrecorddetailId() + "");

		return null;
	}

	/**
	 * 用于查询出module节点字符（从某个moduleId开始到该节点所有祖先节点，用","分割）
	 * 
	 * @param moduleId
	 * @return
	 * @throws Exception
	 */
	private String getModuleIdsbyId(String moduleId, StringBuilder moduleIdStr) throws Exception {
		CacheElement cacheCourse = CacheUtil.getInstance().getCacheOfCourse(this.getCurrentSite().getId());

		moduleIdStr.append(moduleId);
		MeleteModuleModel module = cacheCourse.getModule(Long.valueOf(moduleId));// 从缓存中获得模块
		// 如果有父节点则继续向上查询出moduleId，拼成id字符串
		if (module.getParentId() != null) {
			moduleIdStr.append(",");
			MeleteModuleModel parentModule = cacheCourse.getModule(module.getParentId());
			this.getModuleIdsbyId(parentModule.getId().toString(), moduleIdStr);
		}
		return moduleIdStr.toString();
	}

	/**
	 * 更新节点记录详细信息（主要是更新结束时间）
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updateDetailRecord() throws Exception {
		CacheElement cacheStudy = CacheUtil.getInstance().getCacheOfStudyrecord(this.getCurrentSite().getId(),
				this.getCurrentUserId(), studyrecordId);
		MeleteStudyRecordModel studyRecord = cacheStudy.getStudyRecord();

		String recordId = detailRecordId.replace("\r\n", "");
		try {
			updateRecord(studyRecord, recordId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * 当页面跳转时更新节点记录详细信息（主要是更新结束时间）
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updateDetailRecordOnunload() throws Exception {
		Date endTime = new Date();
		String recordId = detailRecordId.replace("\r\n", "");
		HttpServletRequest request = ServletActionContext.getRequest();
		try {
			MeleteSectionRecordDetailModel detailRecord = studyService.getSectionRecordDetailById(recordId);
			// 如果详细记录信息不为空，则更新相关信息
			if (detailRecord != null) {
				// 时间间隔，默认为3分钟
				Long intervalTime = request.getParameter("startTime") == null ? 0L : Long.valueOf(request
						.getParameter("timer")) / (1000 * 60);
				detailRecord.setEndStudyTime(endTime); // 节点学习结束时间
				detailRecord.setStudyTime((endTime.getTime() - detailRecord.getStartStudyTime().getTime())
						/ (1000 * 60));// 节点学习时长
				studyService.updateModel(detailRecord);

				// 根据节点记录信息Id获取节点记录信息对象（更新学习时长）
				MeleteSectionRecordModel sectionRecord = studyService.getSectionRecordById(detailRecord
						.getSectionrecordId());
				if (sectionRecord != null) {
					Long studyTime = sectionRecord.getStudyTime() == null ? detailRecord.getStudyTime()
							: (sectionRecord.getStudyTime() + intervalTime);
					sectionRecord.setStudyTime(studyTime);
					studyService.updateModel(sectionRecord);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * 学生课件播放界面弹出是否继续学习的提示框后 学生点击“否” 或没有操作等到达时间间隔时记录页和节点详细信息后跳回课程加载页面
	 * 
	 * @return
	 */
	public String cancelStudy() throws Exception {
		try {
			CacheElement cacheStudy = CacheUtil.getInstance().getCacheOfStudyrecord(this.getCurrentSite().getId(),
					this.getCurrentUserId(), studyrecordId);
			MeleteStudyRecordModel studyRecord = cacheStudy.getStudyRecord();
			String recordId = detailRecordId.replace("\r\n", "");

			updateRecord(studyRecord, recordId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return "cancelStudy";
	}

	/**
	 * 离开页时更新页记录和相关节点记录的信息
	 * 
	 * @param studyRecord
	 *            学习记录
	 * @param recordId
	 *            页记录
	 * @throws Exception
	 */
	private void updateRecord(MeleteStudyRecordModel studyRecord, String recordId) throws Exception {
		CacheElement cacheCourse = CacheUtil.getInstance().getCacheOfCourse(this.getCurrentSite().getId());
		CacheElement cacheStudy = CacheUtil.getInstance().getCacheOfStudyrecord(this.getCurrentSite().getId(),
				this.getCurrentUserId(), studyrecordId);

		Date endTime = new Date();
		MeleteSectionRecordDetailModel detailRecord = studyService.getSectionRecordDetailById(recordId);
		// 如果详细记录信息不为空，则更新相关信息
		if (detailRecord != null) {
			detailRecord.setEndStudyTime(endTime); // 节点学习结束时间
			detailRecord.setStudyTime((endTime.getTime() - detailRecord.getStartStudyTime().getTime()) / (1000 * 60));// 设置页节点记录学习时长
			// 结束时间-开始时间
			// 单位
			// 分钟
			detailRecord.setStartStudyTime(endTime);// 更新页节点记录的开始时间为本次的结束时间
			studyService.updateModel(detailRecord);

			// 根据节点记录信息Id获取节点记录信息对象（更新学习时长）
			MeleteSectionRecordModel sectionRecord = studyService.getSectionRecordById(detailRecord
					.getSectionrecordId());
			MeleteSectionModel section = cacheCourse.getSecton(sectionRecord.getSectionId());
			String moduleId = section.getModuleId().toString();
			if (sectionRecord != null) {
				Long sectionTime = sectionRecord.getStudyTime() == null ? detailRecord.getStudyTime() : (sectionRecord
						.getStudyTime() + detailRecord.getStudyTime());
				sectionRecord.setStudyTime(sectionTime);
				studyService.updateModel(sectionRecord);// 更新数据库页记录
				cacheStudy.editSectionRecord(sectionRecord);// 更新页缓存
				// 检查并更改页节点通过状态
				studyService.checkSectionPassStatus(sectionRecord.getSectionId(), sectionRecord.getStudyrecordId(),
						true);

				StringBuilder moduleIdStr = new StringBuilder("");
				String moduleStrs = this.getModuleIdsbyId(moduleId, moduleIdStr);
				String[] ids = moduleStrs.split(",");
				for (int i = ids.length - 1; i >= 0; i--) {
					// 查询是否存在对应模块Id的学习记录
					MeleteModuleRecordModel existRecord = studyService.getModuleRecordById(studyRecord
							.getStudyrecordId().toString(), ids[i]);
					// 如果不存在则按照对应的模块Id新增模块学习记录信息
					if (existRecord != null) {
						Long moduleTime = existRecord.getStudyTime() == null ? detailRecord.getStudyTime()
								: (existRecord.getStudyTime() + detailRecord.getStudyTime());
						existRecord.setStudyTime(moduleTime);
						studyService.updateModel(existRecord);// 更新数据库模块记录
						cacheStudy.editModuleRecord(existRecord);// 更新模块缓存
						// 检查并更改模块节点通过状态
						studyService.checkModulePassStatus(existRecord.getModuleId(), existRecord.getStudyrecordId(),
								true);
					}
				}
			}
		}
	}

	/**
	 * 返回上级页面
	 * 
	 * @param node
	 *            节点id
	 * @param type
	 *            =sectionType
	 * @param studyrecordId
	 *            学习记录id
	 * @param detailRecordId
	 *            明细id
	 * @return
	 * @throws Exception
	 */
	public String abovePage() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		String type = request.getParameter("type") == null ? "moduleType" : request.getParameter("type");
		CacheElement cacheCourse = CacheUtil.getInstance().getCacheOfCourse(this.getCurrentSite().getId());
		CacheElement cacheStudy = CacheUtil.getInstance().getCacheOfStudyrecord(this.getCurrentSite().getId(),
				this.getCurrentUserId(), studyrecordId);

		try {
			MeleteModuleModel module = cacheCourse.getModule(Long.valueOf(node));
			if (type.equals("moduleType") && module.getParentId() == null) {// 返回总页面
				return "cancelStudy";
			} else {
				MeleteStudyRecordModel studyRecord = cacheStudy.getStudyRecord();
				this.updateRecord(studyRecord, detailRecordId);// 在返回上一级之前

				MeleteCourseModel course = cacheCourse.getCourse();

				// 获取模板信息
				templateUrl = getTemplateUrlByName(course, TemplateModel.TEMPLATE_PAGE_MODULE, node);
				String page = "study";
				/*
				 * if (!course.getPlayerTemplate().equals("custom")) { page =
				 * abovePageSysTemplate(ce, course, module, type); }
				 */
				return page;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 系统模板返回上一级
	 * 
	 * @author zihongyan 2014-1-17
	 * @param ce
	 * @param course
	 * @param module
	 * @param type
	 * @return
	 */
	private String abovePageSysTemplate(CacheElement ce, MeleteCourseModel course, MeleteModuleModel module, String type) {
		String page = "study";
		String player = course.getPlayerTemplate();
		MeleteStudyRecordModel studyRecord = ce.getStudyRecord();

		if (!player.equals(CodeTable.template_default)) {
			// 查询当前模块所包含的前测
			List<MeleteSelfTestModel> selfList = ce.getSelftestListByParentId(node, CodeTable.module);
			List<MeleteSelftestRecordModel> selfRecordList = ce.getSelfRecordList(selfList);
			// 查询当前模块所包含的作业
			List<MeleteTestModel> testList = ce.getTestListByParentId(node, CodeTable.module);
			List<MeleteTestRecordModel> testRecordList = ce.getTestRecordList(testList);
			// 查询当前模块所包含的讨论
			List<MeleteForumModel> forumList = ce.getForumListByParentId(node, CodeTable.module);
			List<MeleteForumRecordModel> forumRecordList = ce.getForumRecordList(forumList);
			// 查询当前模块的下级模块
			List<MeleteModuleModel> moduleList = ce.getModuleListByParentid(node);
			List<MeleteModuleRecordModel> moduleRecordList = ce.getModuleRecordList(moduleList);
			// 查询当前模块所包含的页
			List<MeleteSectionModel> sectionList = ce.getSectionListByModuleId(node);
			List<MeleteSectionRecordModel> sectionRecordList = ce.getSectionRecordList(sectionList);

			List<Node> nodeList = CourseUtil.changeToNode(moduleList, moduleRecordList, sectionList, sectionRecordList,
					selfList, selfRecordList, testList, testRecordList, forumList, forumRecordList);

			ServletActionContext.getRequest().setAttribute("nodeList", nodeList);
			ServletActionContext.getRequest().setAttribute("studyRecord", studyRecord);
			ServletActionContext.getRequest().setAttribute("course", course);
			ServletActionContext.getRequest().setAttribute("module", module);

			if ("kecheng-R".equals(player)) {
				templateCommon = "red";
			} else if ("kecheng-G".equals(player)) {
				templateCommon = "green";
			} else if ("kecheng-B".equals(player)) {
				templateCommon = "blue";
			}
		} else {
			// 查询出当前Id对应的Bean对象，判断是否有父节点，如果有则表示上级依然是模块，无则表示为课程
			List<MeleteModuleModel> brotherList = ce.getModuleListByParentid(module.getParentId() == null ? null
					: module.getParentId().toString());
			MeleteModuleModel firstModule = brotherList.get(0);
			MeleteModuleModel lastModule = brotherList.get(brotherList.size() - 1);
			Long idx = module.getIdx();
			Long minIdx = firstModule.getIdx();
			Long maxIdx = lastModule.getIdx();
			// 用于判断是否是最后一页
			if (idx.equals(maxIdx)) {
				ServletActionContext.getRequest().setAttribute("next", "false");
			} else {
				ServletActionContext.getRequest().setAttribute("next", "true");
			}
			// 用于判断是否是第一页
			if (idx.equals(minIdx)) {
				ServletActionContext.getRequest().setAttribute("previous", "false");
			} else {
				ServletActionContext.getRequest().setAttribute("previous", "true");
			}

			if (type.equals("moduleType")) {// 从模块节点返回上级
				MeleteModuleModel parentModule = ce.getModule(module.getParentId());
				ServletActionContext.getRequest().setAttribute("nodeId", parentModule.getId().toString());
				ServletActionContext.getRequest().setAttribute("nodeType", "2");
				// 根据moduleId 获取title值
				ServletActionContext.getRequest().setAttribute("nodeText", parentModule.getTitle());
				ServletActionContext.getRequest().setAttribute("description", parentModule.getDescription());
			} else {// 从页节点返回上级
				ServletActionContext.getRequest().setAttribute("nodeId", module.getId().toString());
				ServletActionContext.getRequest().setAttribute("nodeType", "2");
				// 根据moduleId 获取title值
				ServletActionContext.getRequest().setAttribute("nodeText", module.getTitle());
				ServletActionContext.getRequest().setAttribute("description", module.getDescription());
			}
			templateCommon = "default";
		}
		page = "module";
		return page;
	}

	/**
	 * 检查页节点所属的模块节点是否为课程下面的第一个模块节点若不是则判断前一模块节点是否通过
	 * 
	 * @return
	 */
	public String checkModule() throws Exception {
		StringBuffer result = new StringBuffer("");
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			result.append(checkModuleOfSection(node).get("result").toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		response.getWriter().print(result);
		return null;
	}

	/**
	 * 检查页所属的模块节点能否学习
	 * 
	 * @param sectionId
	 *            页节点ID
	 * @return resultMap 包含键值为“result” 和 “resultFlag”的结果Map result为json串
	 *         resultFlag为页所属的模块节点能否学习的boolean值标志
	 * @throws Exception
	 */
	private Map<String, Object> checkModuleOfSection(String sectionId) throws Exception {
		CacheElement cacheCourse = CacheUtil.getInstance().getCacheOfCourse(this.getCurrentSite().getId());
		CacheElement cacheStudy = CacheUtil.getInstance().getCacheOfStudyrecord(this.getCurrentSite().getId(),
				this.getCurrentUserId(), studyrecordId);

		StringBuffer result = new StringBuffer("{success: true");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Boolean resultFlag = false;// 页所属节点能否学习的标志

		MeleteModuleModel module = cacheCourse.getModule(Long.valueOf(moduleId));// 从缓存中获得模块
		List<MeleteModuleModel> moduleList = cacheCourse.getModuleListByParentid(module.getParentId() == null ? null
				: module.getParentId().toString());// 获得该模块兄弟模块
		int moduleCount = 0;
		for (int i = 0; i < moduleList.size(); i++) {
			if (module.getId().longValue() == moduleList.get(i).getId().longValue()) {
				moduleCount = i;
				break;
			}
		}
		MeleteModuleModel firstModule = moduleList.get(0);// 获得第一个模块节点

		if (module.getId().longValue() == firstModule.getId().longValue()) {
			// 是课程下的第一个模块节点
			result.append(",\"firstModule\":true");
			resultFlag = true;
		} else {
			// 不是课程下的第一个模块节点，则判断该模块节点的前一个节点是否通过
			result.append(",\"firstModule\":false");
			if (CodeTable.prerequisiteYes.equals(module.getPrerequids())) {
				// 若有开启条件 则判断前一模块节点记录是否通过
				result.append(",\"hasPrerequids\":\"yes\"");
				MeleteModuleModel preModule = moduleList.get(moduleCount - 1);// 获得前一个模块节点
				MeleteModuleRecordModel preModuleRecord = cacheStudy.getModuleRecord(preModule.getId());// 从缓存中获得前一模块记录
				if (CodeTable.passStatusYes.equals(preModuleRecord.getStatus().toString())) {
					// 若前一模块节点通过
					result.append(",\"preModulePass\":\"yes\"");
					resultFlag = true;
				} else {
					// 若前一模块节点未通过
					result.append(",\"preModulePass\":\"no\"");
					resultFlag = false;
				}
			} else if (CodeTable.prerequisiteNo.equals(module.getPrerequids())) {
				// 若没有开启条件则可以学习此模块节点中的页
				result.append(",\"hasPrerequids\":\"no\"");
				resultFlag = true;
			}

		}
		result.append("}");
		resultMap.put("result", result);
		resultMap.put("resultFlag", resultFlag);
		return resultMap;
	}

	/**
	 * 检查页节点能否开始学习
	 * 
	 * @return
	 */
	public String checkSection() throws Exception {
		StringBuffer result = new StringBuffer("");
		try {
			CacheElement cacheCourse = CacheUtil.getInstance().getCacheOfCourse(this.getCurrentSite().getId());
			CacheElement cacheStudy = CacheUtil.getInstance().getCacheOfStudyrecord(this.getCurrentSite().getId(),
					this.getCurrentUserId(), studyrecordId);

			result = new StringBuffer("{success: true");

			// 获得学习记录ID
			MeleteStudyRecordModel studyRecord = cacheStudy.getStudyRecord();// 从缓存中获得学生学习记录

			MeleteSectionModel curSection = cacheCourse.getSecton(Long.valueOf(node));// 从缓存中获得当前页
			MeleteSectionModel preSection = null;
			List<MeleteSectionModel> sectionList = cacheCourse.getSectionListByModuleId(moduleId);// 从缓存中获得当前页节点的兄弟节点
			for (int i = 0; i < sectionList.size(); i++) {// 寻找当前页节点的前一页节点
				if (sectionList.get(i).getId().longValue() == curSection.getId().longValue()) {
					if (i == 0) {
						preSection = null;
					} else {
						preSection = sectionList.get(i - 1);
					}

					break;
				}
			}
			if (CodeTable.prerequisiteYes.equals(curSection.getPrerequids())) {// 若当前页有开启条件

				// 检查页所属模块节点能否开始学习
				Boolean resultFlag = (Boolean) this.checkModuleOfSection(node).get("resultFlag");
				if (resultFlag.booleanValue()) {
					// 页所属模块节点可以开始学习
					result.append(",\"moduleEnable\":\"yes\"");
					if (preSection != null) {
						// 若该页存在前一页 检查前一页是否通过
						MeleteSectionRecordModel sectionRecord = cacheStudy.getSectonRecord(Long.valueOf(preSection
								.getId()));// 从缓存中获得前一页记录

						if (CodeTable.passStatusYes.equals(sectionRecord.getStatus().toString())) {// 判断前一页节点的记录是否通过
							// 若前一页节点记录为通过
							result.append(",\"preSectionPass\":\"yes\"");
						} else {
							// 若前一页节点记录为未通过
							result.append(",\"preSectionPass\":\"no\"");
						}
					} else {
						// 若该页不存在前一页则可以学习
						result.append(",\"preSectionPass\":\"yes\"");
					}
				} else {
					result.append(",\"moduleEnable\":\"no\"");
				}
			} else if (CodeTable.prerequisiteNo.equals(curSection.getPrerequids())) {// 若当前页没有开启条件
				// 检查页所属模块节点能否开始学习
				Boolean resultFlag = (Boolean) this.checkModuleOfSection(node).get("resultFlag");
				if (resultFlag) {
					// 若页节点上一级的模块节点可以学习
					result.append(",\"moduleEnable\":\"yes\"");
					result.append(",\"preSectionPass\":\"yes\"");
				} else {
					// 若页节点上一级的模块节点不能学习
					result.append(",\"moduleEnable\":\"no\"");
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}

		result.append("}");
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().print(result);
		return null;
	}

	/**
	 * 检查是否能进行活动
	 * 
	 * @param studyrecordId
	 *            学习记录id
	 * @param node
	 *            作业id
	 * @return
	 */
	public String checkActivity() throws Exception {
		CacheElement cacheCourse = CacheUtil.getInstance().getCacheOfCourse(this.getCurrentSite().getId());
		CacheElement cacheStudy = CacheUtil.getInstance().getCacheOfStudyrecord(this.getCurrentSite().getId(),
				this.getCurrentUserId(), studyrecordId);

		String res = "{success: true}";
		MeleteTestModel test = cacheCourse.getTest(Long.valueOf(node));
		Long minTime = test.getMinTimeInterval();
		MeleteTestRecordModel testRecord = cacheStudy.getTestRecord(Long.valueOf(node));
		Date lastCommitTime = testRecord.getLastCommitTime();// 获得作业最后提交时间
		if (lastCommitTime != null) {
			Date nowTime = new Date();
			// 判断时间间隔
			Long between = (nowTime.getTime() - lastCommitTime.getTime()) / 1000 / 60;
			if (between < minTime) {
				res = "{success: false}";
			}
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().print(res);
		return null;
	}

	/**
	 * 检查活动所属（页或模块）节点上一节点的通过情况
	 * 
	 * @param activityId
	 *            活动id
	 * @param activityType
	 *            活动类型
	 * @return 上一节点是否存在或通过
	 */
	private boolean checkpreNode(String activityId, String activityType) {
		boolean result = false;
		try {
			CacheElement cacheCourse = CacheUtil.getInstance().getCacheOfCourse(this.getCurrentSite().getId());
			CacheElement cacheStudy = CacheUtil.getInstance().getCacheOfStudyrecord(this.getCurrentSite().getId(),
					this.getCurrentUserId(), studyrecordId);

			Object sectionOrModule = studyService.getSectionOrModuleByActivityId(node, nodeType, studyrecordId);// 获得活动所属节点
			if (sectionOrModule.getClass() == MeleteSectionModel.class) {
				// 若活动属于页
				MeleteSectionModel section = (MeleteSectionModel) sectionOrModule;
				// 获得前一页节点
				MeleteSectionModel preSection = null;
				List<MeleteSectionModel> sectionList = cacheCourse.getSectionListByModuleId(section.getModuleId()
						.toString());// 从缓存中获得当前页节点的兄弟节点
				for (int i = 0; i < sectionList.size(); i++) {// 寻找当前页节点的前一页节点
					if (sectionList.get(i).getId().longValue() == section.getId().longValue()) {
						if (i == 0) {
							preSection = null;
						} else {
							preSection = sectionList.get(i - 1);
						}

						break;
					}
				}

				if (preSection != null) { // 若前一页存在 则检查前一页是否通过
					MeleteSectionRecordModel preSectionRecord = cacheStudy.getSectonRecord(preSection.getId());// 从缓存中获得前一页节点记录
					if (CodeTable.passStatusYes.equals(preSectionRecord.getStatus().toString())) {// 若前一页节点通过
						result = true;
					} else if (CodeTable.passStatusNo.equals(preSectionRecord.getStatus().toString())) {// 若前一页节点未通过
						result = false;
					}
				} else {// 若前一页不存在
					result = true;
				}
			} else if (sectionOrModule.getClass() == MeleteModuleModel.class) {
				// 若活动属于模块
				MeleteModuleModel module = (MeleteModuleModel) sectionOrModule;
				MeleteModuleModel preModule = null;
				// 获得前一模块节点
				List<MeleteModuleModel> moduleList = cacheCourse
						.getModuleListByParentid(module.getParentId() == null ? null : module.getParentId().toString());// 获得模块的同级的兄弟模块
				for (int i = 0; i < moduleList.size(); i++) {// 寻找前一模块节点
					if (moduleList.get(i).getId().longValue() == module.getId().longValue()) {
						if (i == 0) {
							preModule = null;
						} else {
							preModule = moduleList.get(i - 1);
						}
					}
				}
				if (preModule != null) {// 前一模块节点存在
					MeleteModuleRecordModel preModuleRecord = cacheStudy.getModuleRecord(preModule.getId());
					if (CodeTable.passStatusYes.equals(preModuleRecord.getStatus().toString())) {// 若前一模块节点通过
						result = true;
					} else if (CodeTable.passStatusNo.equals(preModuleRecord.getStatus().toString())) {// 若前一模块节点未通过
						result = false;
					}
				} else {// 前一模块节点不存在
					result = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return result;

	}

	/**
	 * 查找模块下的作业 前测的 尝试记录
	 * 
	 * @return
	 */
	public String findAttemptList() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 格式化日期
			// 获得模块下的作业 前测 的尝试记录
			List<Object> resultList = studyService.getAttemptByModuleIdAndStudyRecordId(moduleId, studyrecordId);
			List<AsyncGridModel> data = new ArrayList<AsyncGridModel>();

			for (Object obj : resultList) {
				AsyncGridModel model = new AsyncGridModel();
				// 若是作业尝试记录
				Map map = (Map) obj;
				model.setDataIndex("id", map.get("id"));
				model.setDataIndex("attemptName", map.get("attemptName"));
				Date endTime = (Date) map.get("endTime");
				model.setDataIndex("endTime", sdf.format(endTime));
				String paperStatus = (String) map.get("pagerstatus");
				if (CodeTable.subCheckYes.equals(paperStatus)) {
					model.setDataIndex("subItem", map.get("subItem"));
				} else if (CodeTable.subCheckNo.equals(paperStatus)) {
					model.setDataIndex("subItem", "主观题未批改");
				} else if (CodeTable.subNotExist.equals(paperStatus)) {
					model.setDataIndex("subItem", "无主观题");
				}

				model.setDataIndex("objItem", map.get("objItem"));
				model.setDataIndex("score", map.get("score"));
				model.setDataIndex("paperid", map.get("paperid"));
				model.setDataIndex("courseId", map.get("courseId"));
				model.setDataIndex("testId", map.get("testId"));
				model.setDataIndex("testType", map.get("testType"));
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

	public String viewAttemptResult() {
		String content = "";
		try {
			String studentId = this.getCurrentUserId();
			CacheElement cacheCourse = CacheUtil.getInstance().getCacheOfCourse(this.getCurrentSite().getId());

			String samepaper = "";
			if (CodeTable.selftest.equals(testType)) {
				MeleteSelfTestModel selfTest = cacheCourse.getSelftest(Long.valueOf(testId));
				samepaper = selfTest.getSamepaper();
				String paperName = Helper.getPaperName(paperid);
				String answerName = Helper.getStuAnswerName(paperid, attemptId);
				String path2 = Constants.getStuSelfTestPath(studentId, courseId);
				content = ServerActionTool.exportExamInfPaper(Constants.getSelfTestMaterialURL(courseId), path2,
						paperName, path2, answerName, samepaper.equals(CodeTable.samePaperNo));

			} else if (CodeTable.test.equals(testType)) {
				MeleteTestModel test = cacheCourse.getTest(Long.valueOf(testId));
				samepaper = test.getSamepaper();
				String paperName = Helper.getPaperName(paperid);
				String answerName = Helper.getStuAnswerName(paperid, attemptId);
				String path2 = Constants.getStuTestPath(studentId, courseId);
				content = ServerActionTool.exportExamInfPaper(Constants.getTestMaterialURL(courseId), path2, paperName,
						path2, answerName, samepaper.equals(CodeTable.samePaperNo));
			}

		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter();  
		    e.printStackTrace(new PrintWriter(sw, true));
			loggerManage.error("test view error-" + courseId + ":" + sw.toString());
		}
		ServletActionContext.getRequest().setAttribute("content", content);
		return "testResult";
	}

	public String getValue() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		List<Object[]> list = this.studyService.getActivityValue(sectionRecordId, activityId);
		if (!list.isEmpty() && list.size() != 0) {
			Object[] objs = list.get(0);
			response.getWriter().println("callBack('" + objs[0] + "','" + objs[1] + "')");
		} else {
			response.getWriter().println("callBack(null,null)");
		}
		return null;
	}

	public String setValue() throws Exception {
		this.studyService.setActivityValue(sectionRecordId, activityId, activityValue);
		return null;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getActivityValue() {
		return activityValue;
	}

	public void setActivityValue(String activityValue) {
		this.activityValue = activityValue;
	}

	/**
	 * @param moduleId
	 * @param noderecordId
	 * @param courseId
	 * @param studyrecordId
	 * @return
	 * @throws Exception
	 */
	public String fowardHillModule() throws Exception {
		try {
			CacheElement cacheCourse = CacheUtil.getInstance().getCacheOfCourse(this.getCurrentSite().getId());
			CacheElement cacheStudy = CacheUtil.getInstance().getCacheOfStudyrecord(this.getCurrentSite().getId(),
					this.getCurrentUserId(), studyrecordId);

			// 查询当前模块所包含的前测
			List<MeleteSelfTestModel> selfList = cacheCourse.getSelftestListByParentId(moduleId, CodeTable.module);
			List<MeleteSelftestRecordModel> selfRecordList = cacheStudy.getSelfRecordList(selfList);
			// 查询当前模块所包含的作业
			List<MeleteTestModel> testList = cacheCourse.getTestListByParentId(moduleId, CodeTable.module);
			List<MeleteTestRecordModel> testRecordList = cacheStudy.getTestRecordList(testList);
			// 查询当前模块所包含的讨论
			List<MeleteForumModel> forumList = cacheCourse.getForumListByParentId(moduleId, CodeTable.module);
			List<MeleteForumRecordModel> forumRecordList = cacheStudy.getForumRecordList(forumList);
			// 查询当前模块的下级模块
			List<MeleteModuleModel> moduleList = cacheCourse.getModuleListByParentid(moduleId);
			List<MeleteModuleRecordModel> moduleRecordList = cacheStudy.getModuleRecordList(moduleList);
			// 查询当前模块所包含的页
			List<MeleteSectionModel> sectionList = cacheCourse.getSectionListByModuleId(moduleId);
			List<MeleteSectionRecordModel> sectionRecordList = cacheStudy.getSectionRecordList(sectionList);

			List<Node> nodeList = CourseUtil.changeToNode(moduleList, moduleRecordList, sectionList, sectionRecordList,
					selfList, selfRecordList, testList, testRecordList, forumList, forumRecordList);

			MeleteCourseModel course = cacheCourse.getCourse();
			MeleteStudyRecordModel studyRecord = cacheStudy.getStudyRecord();

			ServletActionContext.getRequest().setAttribute("nodeList", nodeList);
			ServletActionContext.getRequest().setAttribute("studyRecord", studyRecord);
			ServletActionContext.getRequest().setAttribute("course", course);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return "hill";
	}

	public String fowardTemplateRGBModule() throws Exception {
		try {
			CacheElement cacheCourse = CacheUtil.getInstance().getCacheOfCourse(this.getCurrentSite().getId());
			CacheElement cacheStudy = CacheUtil.getInstance().getCacheOfStudyrecord(this.getCurrentSite().getId(),
					this.getCurrentUserId(), studyrecordId);

			// 查询当前模块所包含的前测
			List<MeleteSelfTestModel> selfList = cacheCourse.getSelftestListByParentId(moduleId, CodeTable.module);
			List<MeleteSelftestRecordModel> selfRecordList = cacheStudy.getSelfRecordList(selfList);
			if (selfList != null && !selfList.isEmpty()) {
				for (int j = 0; j < selfList.size(); j++) {
					MeleteSelftestRecordModel selfTestRec = selfRecordList.get(j);
					if (selfTestRec == null) {// 若模块对应的学习记录为空则
						selfTestRec = CacheUtil.getInstance().getStuSelftestRecord(cacheStudy, selfList.get(j),
								studyrecordId, cacheCourse.getCourse().getId());
						selfRecordList.add(j, selfTestRec);
					}
				}
			}

			// 查询当前模块所包含的作业
			List<MeleteTestModel> testList = cacheCourse.getTestListByParentId(moduleId, CodeTable.module);
			List<MeleteTestRecordModel> testRecordList = cacheStudy.getTestRecordList(testList);
			if (testList != null && !testList.isEmpty()) {
				for (int j = 0; j < testList.size(); j++) {
					MeleteTestRecordModel testRec = testRecordList.get(j);
					if (testRec == null) {// 若模块对应的学习记录为空则
						testRec = CacheUtil.getInstance().getStuTestRecord(cacheStudy, testList.get(j), studyrecordId,
								cacheCourse.getCourse().getId());
						testRecordList.add(j, testRec);
					}
				}
			}

			// 查询当前模块所包含的讨论
			List<MeleteForumModel> forumList = cacheCourse.getForumListByParentId(moduleId, CodeTable.module);
			List<MeleteForumRecordModel> forumRecordList = cacheStudy.getForumRecordList(forumList);
			if (forumList != null && !forumList.isEmpty()) {
				for (int j = 0; j < forumList.size(); j++) {
					MeleteForumRecordModel forumRec = forumRecordList.get(j);
					if (forumRec == null) {// 若模块对应的学习记录为空则
						forumRec = CacheUtil.getInstance().getStuForumRecord(cacheStudy, forumList.get(j),
								studyrecordId, cacheCourse.getCourse().getId());
						forumRecordList.add(j, forumRec);
					}
				}
			}

			// 查询当前模块的下级模块
			List<MeleteModuleModel> tempModuleResList = cacheCourse.getModuleListByParentid(moduleId);
			List<MeleteModuleModel> moduleList = new ArrayList<MeleteModuleModel>();
			if ((tempModuleResList != null) && (tempModuleResList.size() != 0)) {
				for (int i = 0; i < tempModuleResList.size(); i++) {
					MeleteModuleModel module = tempModuleResList.get(i);
					if (CodeTable.hide.equals(module.getStatus().toString())) {
						continue;
					}
					moduleList.add(module);
				}
			}

			List<MeleteModuleRecordModel> moduleRecordList = cacheStudy.getModuleRecordList(moduleList);
			for (int i = 0; i < moduleList.size(); i++) {
				MeleteModuleRecordModel modRec = moduleRecordList.get(i);
				if (modRec == null) {// 若模块对应的学习记录为空则
					modRec = CacheUtil.getInstance().getStuModuleRecord(cacheStudy, moduleList.get(i), studyrecordId);
					moduleRecordList.add(i, modRec);
				}
			}
			// 查询当前模块所包含的页
			List<MeleteSectionModel> sectionList = cacheCourse.getSectionListByModuleId(moduleId);
			List<MeleteSectionRecordModel> sectionRecordList = cacheStudy.getSectionRecordList(sectionList);
			for (int i = 0; i < sectionList.size(); i++) {
				MeleteSectionRecordModel secRec = sectionRecordList.get(i);
				if (secRec == null) {// 若模块对应的学习记录为空则
					secRec = CacheUtil.getInstance().getStuSecRecord(cacheStudy, sectionList.get(i), studyrecordId);
					sectionRecordList.add(i, secRec);
				}
			}
			List<Node> nodeList = CourseUtil.changeToNode(moduleList, moduleRecordList, sectionList, sectionRecordList,
					selfList, selfRecordList, testList, testRecordList, forumList, forumRecordList);

			Map<String, List<Node>> setionActivityMap = null;
			if (sectionList != null) {
				setionActivityMap = new HashMap<String, List<Node>>();
				for (int i = 0; i < sectionList.size(); i++) {
					MeleteSectionModel section = sectionList.get(i);
					String sectionId = section.getId().toString();
					// 查询当前页所包含的前测
					List<MeleteSelfTestModel> sectionSelfList = cacheCourse.getSelftestListByParentId(sectionId,
							CodeTable.section);
					List<MeleteSelftestRecordModel> setionSelftRecordList = cacheStudy
							.getSelfRecordList(sectionSelfList);
					if (sectionSelfList != null && !sectionSelfList.isEmpty()) {
						for (int j = 0; j < sectionSelfList.size(); j++) {
							MeleteSelftestRecordModel selfTestRec = setionSelftRecordList.get(j);
							if (selfTestRec == null) {// 若模块对应的学习记录为空则
								selfTestRec = CacheUtil.getInstance().getStuSelftestRecord(cacheStudy,
										sectionSelfList.get(j), studyrecordId, cacheCourse.getCourse().getId());
								setionSelftRecordList.add(j, selfTestRec);
							}
						}
					}

					// 查询当前页所包含的作业
					List<MeleteTestModel> sectionTestList = cacheCourse.getTestListByParentId(sectionId,
							CodeTable.section);
					List<MeleteTestRecordModel> sectionTestRecordList = cacheStudy.getTestRecordList(sectionTestList);
					if (sectionTestList != null && !sectionTestList.isEmpty()) {
						for (int j = 0; j < sectionTestList.size(); j++) {
							MeleteTestRecordModel secTestRec = sectionTestRecordList.get(j);
							if (secTestRec == null) {// 若模块对应的学习记录为空则
								secTestRec = CacheUtil.getInstance().getStuTestRecord(cacheStudy,
										sectionTestList.get(j), studyrecordId, cacheCourse.getCourse().getId());
								sectionTestRecordList.add(j, secTestRec);
							}
						}
					}

					// 查询当前页所包含的讨论
					List<MeleteForumModel> setionForumList = cacheCourse.getForumListByParentId(sectionId,
							CodeTable.section);
					List<MeleteForumRecordModel> sectionForumRecordList = cacheStudy
							.getForumRecordList(setionForumList);
					if (setionForumList != null && !setionForumList.isEmpty()) {
						for (int j = 0; j < setionForumList.size(); j++) {
							MeleteForumRecordModel forumRec = sectionForumRecordList.get(j);
							if (forumRec == null) {// 若模块对应的学习记录为空则
								forumRec = CacheUtil.getInstance().getStuForumRecord(cacheStudy,
										setionForumList.get(j), studyrecordId, cacheCourse.getCourse().getId());
								sectionForumRecordList.add(j, forumRec);
							}
						}
					}
					List<Node> tempNodeList = CourseUtil.changeToNode(null, null, null, null, sectionSelfList,
							setionSelftRecordList, sectionTestList, sectionTestRecordList, setionForumList,
							sectionForumRecordList);
					setionActivityMap.put(sectionId, tempNodeList);
				}
			}

			MeleteModuleModel module = cacheCourse.getModule(Long.valueOf(moduleId));
			MeleteStudyRecordModel studyRecord = cacheStudy.getStudyRecord();

			ServletActionContext.getRequest().setAttribute("setionActivityMap", setionActivityMap);
			ServletActionContext.getRequest().setAttribute("nodeList", nodeList);
			ServletActionContext.getRequest().setAttribute("studyRecord", studyRecord);
			ServletActionContext.getRequest().setAttribute("module", module);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		templateCommon = template;
		return "module";
	}

	/**
	 * 检查是否开启了学习位置记录
	 * 
	 * @param ce
	 * @return
	 */
	private String checkStudyHistory(CacheElement ce) {
		// 是否开启了学习位置记录
		if (!Constants.FUNC_SWITCH_STUDYHISTORY) {
			return null;
		}
		// 如果存在学习历史记录则直接跳转到上一次浏览界面
		HttpServletRequest request = ServletActionContext.getRequest();
		// 如果是"点击主页"跳转过来的，则不自动定位
		Boolean flag = true;
		if (request.getAttribute("flag") != null) {
			flag = (Boolean) request.getAttribute("flag");
		}

		if (flag) {
			return null;
		}
		MeleteStudyHistoryRecordModel stuHistory = studyService.findStudyHistortRecordByStudentIDAndCourseId(studentId,
				courseId);
		if (stuHistory == null) {
			return null;
		}
		node = stuHistory.getNodeId();
		// 如果原学习记录对应的页存在，则跳转
		if (ce.getSecton(Long.valueOf(node)) != null) {
			studyrecordId = stuHistory.getStudyRecordId();
			request.setAttribute("node", node);
			request.setAttribute("studyrecordId", studyrecordId);
			return "history";
		}
		return null;
	}

	/**
	 * 用于保存最新的一条学习历史记录
	 * 
	 * @author zihongyan 2013-4-3
	 * @param studentId
	 *            学生id
	 * @param courseId
	 *            课程id
	 */
	private void executeSaveStudyHistory(String studentId, String courseId) {
		if (!Constants.FUNC_SWITCH_STUDYHISTORY) {
			return;
		}
		studyService.deleteStudyHistoryRecordByStudentIdAndCourseId(studentId, courseId);
		MeleteStudyHistoryRecordModel stuHistoryRecord = new MeleteStudyHistoryRecordModel();
		stuHistoryRecord.setCourseId(courseId);
		stuHistoryRecord.setStudentId(studentId);
		stuHistoryRecord.setNodeId(node);
		stuHistoryRecord.setStudyRecordId(studyrecordId);
		stuHistoryRecord.setType("2");
		studyService.saveStudyHistoryRecord(stuHistoryRecord);
	}

	/**
	 * 返回子节点列表的json格式 {module:null,course:null,children:[]}
	 * 
	 * @return
	 * @throws Exception
	 */
	public String nodesJson() throws Exception {
		// HttpServletRequest request = ServletActionContext.getRequest();
		// String type = request.getParameter("type") == null ? "moduleType" :
		// request.getParameter("type");

		// 检查缓存数据
		CacheElement cacheCourse = CacheUtil.getInstance().getCacheOfCourse(this.getCurrentSite().getId());
		CacheElement cacheStudy = CacheUtil.getInstance().getCacheOfStudyrecord(this.getCurrentSite().getId(),
				this.getCurrentUserId(), studyrecordId);

		try {
			// 当前节点
			MeleteModuleModel module = null;
			if (StringUtils.isNotEmpty(node)) {
				module = cacheCourse.getModule(Long.valueOf(node));
			}

			MeleteStudyRecordModel studyRecord = cacheStudy.getStudyRecord();

			// 当前课程
			MeleteCourseModel course = cacheCourse.getCourse();

			List<Node> nodes = getSubNodes(cacheCourse, cacheStudy, node);

			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");

			String json_course = JSONObject.fromObject(course).toString();
			String json_module = "null";
			if (module != null) {
				json_module = JSONObject.fromObject(module).toString();
			}
			String json_record = "null";
			if (studyRecord != null) {
				json_record = JSONObject.fromObject(studyRecord).toString();
			}

			String json_children = JSONArray.fromObject(nodes).toString();
			response.getWriter().println(
					"{\"course\":" + json_course + ",\"module\":" + json_module + ",\"record\":" + json_record
							+ ",\"children\":" + json_children + "}");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
		return null;
	}

	/**
	 * 迭代获取节点树
	 * 
	 * @param ce
	 * @param node
	 * @return
	 */
	private List<Node> getSubNodes(CacheElement cacheCourse, CacheElement cacheStudy, String node) {
		List<Node> nodeList = null;
		// 查询当前模块的下级模块
		List<MeleteModuleModel> moduleList = cacheCourse.getModuleListByParentid(node);
		// 当获取课程一级节点时，如果没有下级模块，则跳出
		if (!StringUtils.isNotBlank(node) && (moduleList == null || moduleList.isEmpty())) {
			return null;
		}

		// 查询当前模块所包含的前测
		List<MeleteSelfTestModel> selfList = cacheCourse.getSelftestListByParentId(node, CodeTable.module);
		List<MeleteSelftestRecordModel> selfRecordList = null;
		if (cacheStudy != null) {
			selfRecordList = cacheStudy.getSelfRecordList(selfList);
		}
		// 查询当前模块所包含的作业
		List<MeleteTestModel> testList = cacheCourse.getTestListByParentId(node, CodeTable.module);
		List<MeleteTestRecordModel> testRecordList = null;
		if (cacheStudy != null) {
			testRecordList = cacheStudy.getTestRecordList(testList);
		}
		// 查询当前模块所包含的讨论
		List<MeleteForumModel> forumList = cacheCourse.getForumListByParentId(node, CodeTable.module);
		List<MeleteForumRecordModel> forumRecordList = null;
		if (cacheStudy != null) {
			forumRecordList = cacheStudy.getForumRecordList(forumList);
		}

		List<MeleteModuleRecordModel> moduleRecordList = null;
		if (cacheStudy != null) {
			moduleRecordList = cacheStudy.getModuleRecordList(moduleList);
		}
		// 查询当前模块所包含的页
		List<MeleteSectionModel> sectionList = cacheCourse.getSectionListByModuleId(node);
		List<MeleteSectionRecordModel> sectionRecordList = null;
		if (cacheStudy != null) {
			sectionRecordList = cacheStudy.getSectionRecordList(sectionList);
		}

		nodeList = CourseUtil.changeToNode(moduleList, moduleRecordList, sectionList, sectionRecordList, selfList,
				selfRecordList, testList, testRecordList, forumList, forumRecordList);

		if (StringUtils.isNotBlank(node) && nodeList != null && !nodeList.isEmpty()) {
			for (Node n : nodeList) {
				n.setChildren(getSubNodes(cacheCourse, cacheStudy, n.getId()));
			}
		}

		return nodeList;
	}

	private List<Node> getSectionNodes(CacheElement cacheCourse, String moduleId) {
		if (StringUtils.isBlank(moduleId)) {
			return null;
		}

		List<Node> nodeList = null;
		// 查询当前模块所包含的页
		List<MeleteSectionModel> sectionList = cacheCourse.getSectionListByModuleId(moduleId);

		// 查询当前模块所包含的作业
		List<MeleteTestModel> testList = cacheCourse.getTestListByParentId(moduleId, CodeTable.module);
		// 查询当前模块所包含的讨论
		List<MeleteForumModel> forumList = cacheCourse.getForumListByParentId(moduleId, CodeTable.module);

		nodeList = CourseUtil.changeToNode(null, null, sectionList, null, null, null, testList, null, forumList, null);

		return nodeList;
	}
	
	/**
	 * 获取当前课程全部层级节点
	 * 
	 * @return
	 * @throws Exception
	 */
	public String nodesJsonOfCourse() throws Exception {
		// 检查缓存数据
		CacheElement cacheCourse = CacheUtil.getInstance().getCacheOfCourse(this.getCurrentSite().getId());

		try {
			StringBuilder s = new StringBuilder("{\"course\":");

			// 当前课程
			MeleteCourseModel course = cacheCourse.getCourse();
			s.append(JSONObject.fromObject(course).toString());
			s.append(", \"modules\":[");

			List<Node> nodes_module = getSubNodes(cacheCourse, null, null);
			if (nodes_module != null) {
				int i = 0;
				for (Node m : nodes_module) {
					if (i > 0) {
						s.append(",");
					}
					s.append("{\"module\":");
					s.append(JSONObject.fromObject(m).toString());

					List<Node> nodes_section = getSectionNodes(cacheCourse, m.getId());
					if (nodes_section != null) {
						s.append(",\"sections\":");
						s.append(JSONArray.fromObject(nodes_section).toString());
					}
					s.append("}");
					i++;
				}
			}
			s.append("]}");

			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");

			response.getWriter().println(s.toString());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
		return null;
	}

	/** 存储所有的SetionModel对象 */
	private List<MeleteSectionModel> nList = new ArrayList<MeleteSectionModel>();

	public List<MeleteSectionModel> getnList() {
		return nList;
	}

	public void setnList(List<MeleteSectionModel> nList) {
		this.nList = nList;
	}

	/**
	 * 迭代获得模块下所有的setionModel
	 * 
	 * @author zihongyan 2013-7-30
	 * @param nodeList
	 * @throws Exception
	 */
	private void getAllChildNode(List<Node> nodeList) throws Exception {
		if (nodeList == null || nodeList.isEmpty()) {
			return;
		}
		for (Node n : nodeList) {
			if (n.getChildren() != null && !n.getChildren().isEmpty()) {
				getAllChildNode(n.getChildren());
			} // 只加入页节点
			else if (n.getType().equals(CodeTable.section)) {
				MeleteSectionModel sectionM = (MeleteSectionModel) courseService.getModelById(MeleteSectionModel.class,
						Long.parseLong(n.getId()));
				nList.add(sectionM);
			}
		}
	}

	/**
	 * 获得所有section节点
	 * 
	 * @author zihongyan
	 * @param moduleId
	 * 
	 * @return
	 * @throws Exception
	 * @throws NumberFormatException
	 */
	private List<MeleteSectionModel> getAllSectionModel(Long moduleIds) {
		try {
			CacheElement cacheCourse = CacheUtil.getInstance().getCacheOfCourse(this.getCurrentSite().getId());
			CacheElement cacheStudy = CacheUtil.getInstance().getCacheOfStudyrecord(this.getCurrentSite().getId(),
					this.getCurrentUserId(), studyrecordId);

			// 获得上一级的ModuleId zihongyan 2013-7-24
			Long rootModulesId = getRootModuleId(moduleIds);
			// 迭代看获得所有节点 树zihongyan
			List<Node> nodeList = getSubNodes(cacheCourse, cacheStudy, rootModulesId.toString());
			List<MeleteSectionModel> sectionList = null;
			if (nodeList != null && !nodeList.isEmpty()) {
				getAllChildNode(nodeList);
			}
			return sectionList;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return null;
		}

	}

	public String getTemplateUrl() {
		return templateUrl;
	}

	public void setTemplateUrl(String templateUrl) {
		this.templateUrl = templateUrl;
	}

	public String getPlacementId() {
		return placementId;
	}

	public void setPlacementId(String placementId) {
		this.placementId = placementId;
	}

	public String getTemplateCommon() {
		return templateCommon;
	}

	public void setTemplateCommon(String templateCommon) {
		this.templateCommon = templateCommon;
	}

}