package org.sakaiproject.resource.tool.statistics.action;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.sakaiproject.resource.api.file.model.ResourceFileModel;
import org.sakaiproject.resource.api.file.service.IFileService;
import org.sakaiproject.resource.api.statistics.service.IStatisticsService;
import org.sakaiproject.resource.api.study.model.MeleteForumRecordModel;
import org.sakaiproject.resource.api.study.model.MeleteModuleRecordModel;
import org.sakaiproject.resource.api.study.model.MeleteSectionRecordModel;
import org.sakaiproject.resource.api.study.model.MeleteSelftestRecordModel;
import org.sakaiproject.resource.api.study.model.MeleteStudyRecordModel;
import org.sakaiproject.resource.api.study.model.MeleteTestRecordModel;
import org.sakaiproject.resource.api.study.service.IStudyService;
import org.sakaiproject.resource.util.AsyncGridModel;
import org.sakaiproject.resource.util.AsyncTreeModel;
import org.sakaiproject.resource.util.CacheElement;
import org.sakaiproject.resource.util.CacheUtil;
import org.sakaiproject.resource.util.CodeTable;
import org.sakaiproject.resource.util.Constants;
import org.sakaiproject.resource.util.FunctionRegister;
import org.sakaiproject.resource.util.JsonBuilder;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.tool.api.ToolManager;
import org.sakaiproject.user.api.UserDirectoryService;

import com.opensymphony.xwork2.ActionSupport;

public class StatisticsAction extends ActionSupport {

	private static final long serialVersionUID = -4247373068693289760L;

	/**
	 * 是否查询数据展示到页面上
	 */
	private Boolean isShow = false;

	private IFileService fileService;

	private IStatisticsService statisticsService;

	private ICourseService courseService;

	private IStudyService studyService;

	private SiteService siteService = (SiteService) ComponentManager.get(SiteService.class);

	private ToolManager toolManager = (ToolManager) ComponentManager.get(ToolManager.class);

	private UserDirectoryService userDirectoryService = (UserDirectoryService) ComponentManager
			.get(UserDirectoryService.class);

	private SecurityService securityService = (SecurityService) ComponentManager.get(SecurityService.class);

	private static Log logger = LogFactory.getLog(StatisticsAction.class);

	private String node;// 树节点id
	private String nodeType;// 节点类型
	private String nodeIdx;// 节点序号
	private String studyrecordId;
	private String noderecordId;
	private String courseId;// 课程Id

	/** 活动名称 */
	private String activeName;

	/** 次数限制--大于等于小于 */
	private String timesLimit;

	/** 次数限制--数字 */
	private String times;

	/** 状态 */
	private String status;

	/** 最低分数 */
	private String minScore;

	/** 最高分数 */
	private String maxScore;

	/** 返回到页面的集合 */
	private List<Map<String, Object>> studyRecordList = new ArrayList<Map<String, Object>>();

	/** 存储章节点 */
	private List<Map<String, Object>> modelList = new ArrayList<Map<String, Object>>();

	/** 存储章节点，以父节点为key值重新封装后的数据 */
	private Map<String, List<Map<String, Object>>> moduleMap = new LinkedHashMap<String, List<Map<String, Object>>>();

	/** 存储叶节点 */
	private Map<String, List<Map<String, Object>>> sectionMap = new LinkedHashMap<String, List<Map<String, Object>>>();

	/** 存储节点ID用来判断是否遍历过这条记录 */
	private List<Object> modelIdList = new ArrayList<Object>();

	public StringBuffer print;

	/** 统计分类下载次数 */
	public String downTimes() throws Exception {
		if (!this.getIsShow()) {
			return "downTimes";
		}
		try {
			// 根据当前站点ID获得该站点下所有文件资源
			List<ResourceFileModel> list = fileService.getAllFileBySiteId(this.getCurrentSite().getId());
			// 创建一个格式化对象数据的集合，用来把数据加载到页面的grid里
			List<AsyncGridModel> data = new ArrayList<AsyncGridModel>();
			// 开始遍历集合，封装到AsyncGridModel这个类里
			if (!list.isEmpty() && list.size() != 0) {
				for (Object obj : list) {
					AsyncGridModel model = new AsyncGridModel();
					if (obj.getClass() == ResourceFileModel.class) {
						ResourceFileModel rfm = (ResourceFileModel) obj;
						model.setDataIndex("fileName", rfm.getFileName());// 资源名称
						model.setDataIndex("downloadCount", rfm.getDownloadCount());// 下载次数
						data.add(model);
					}
				}
				String json = JsonBuilder.builderAsyncGridJson(data);
				this.getResponse().getWriter().println(json);
			}

			return null;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * 获取登陆用户Id
	 */
	private String getCurrentUserId() {
		return userDirectoryService.getCurrentUser().getId();
	}

	public String execute() throws Exception {
		try {
			Site curSite = this.getCurrentSite();
			String siteRef = siteService.siteReference(curSite.getId());
			String currentUser = this.getCurrentUserId();
			if (securityService.unlock(currentUser, FunctionRegister.COURSE_SPACE_PERM, siteRef)) {// 教师
				return "success";
			} else if (securityService.unlock(currentUser, FunctionRegister.STUDY_SPACE_PERM, siteRef)) {// 学生
				ServletActionContext.getRequest().setAttribute("studyrecordId", studyrecordId);
				return "studyRecordStat";
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
		return null;
	}

	/**
	 * 加载学习记录统计树
	 * 
	 * @return
	 */
	public String loadStudyRecordStatData() throws Exception {

		try {
			CacheElement ce = (CacheElement) CacheUtil.getInstance().getCacheOfCourse(getCurrentSite().getId());
			// 定义查询出的所有节点集合
			List<Object> nodeList = new ArrayList<Object>();
			// 定义要返回的节点集合
			List<AsyncTreeModel> data = new ArrayList<AsyncTreeModel>();
			if (node.equals("0")) {// 加载根节点，显示课程名称节点
				MeleteCourseModel course = ce.getCourse();
				nodeList.add(course);// 加入到节点集合中
			} else if (nodeType.equals(CodeTable.course)) {// 节点类型是“课程”
				// 查询一级模块节点
				List<MeleteModuleModel> moduleList = ce.getModuleListByParentid(null);
				nodeList.addAll(moduleList);
			} else if (nodeType.equals(CodeTable.module)) {// 节点类型是“模块”
				boolean selftestLeaf = ce.getSelftestLeaf(new Long(node)).booleanValue();
				if (!selftestLeaf) {
					// 查询当前模块所包含的前测
					List<MeleteSelfTestModel> selfList = ce.getSelftestListByParentId(node, CodeTable.module);
					nodeList.addAll(selfList);
				}
				boolean testLeaf = ce.getTestLeaf(new Long(node)).booleanValue();
				if (!testLeaf) {
					// 查询当前模块所包含的作业
					List<MeleteTestModel> testList = ce.getTestListByParentId(node, CodeTable.module);
					nodeList.addAll(testList);
				}
				boolean forumLeaf = ce.getForumLeaf(new Long(node)).booleanValue();
				if (!forumLeaf) {
					// 查询当前模块所包含的讨论
					List<MeleteForumModel> forumList = ce.getForumListByParentId(node, CodeTable.module);
					nodeList.addAll(forumList);
				}
				boolean moduleLeaf = ce.getModuleLeaf(new Long(node)).booleanValue();
				if (!moduleLeaf) {
					// 查询当前模块的下级模块
					List<MeleteModuleModel> moduleList = ce.getModuleListByParentid(node);
					nodeList.addAll(moduleList);
				}
				boolean secLeaf = ce.getSectionLeaf(new Long(node)).booleanValue();
				if (!secLeaf) {
					// 查询当前模块所包含的页
					List<MeleteSectionModel> sectionList = ce.getSectionListByModuleId(node);
					nodeList.addAll(sectionList);
				}

			} else if (nodeType.equals(CodeTable.section)) {// 节点类型是“页”
				boolean selftestLeaf = ce.getSelftestLeaf(new Long(node)).booleanValue();
				if (!selftestLeaf) {
					// 查询当前节点所包含的前测
					List<MeleteSelfTestModel> selfList = ce.getSelftestListByParentId(node, CodeTable.section);
					nodeList.addAll(selfList);
				}
				boolean testLeaf = ce.getTestLeaf(new Long(node)).booleanValue();
				if (!testLeaf) {
					// 查询当前节点所包含的作业
					List<MeleteTestModel> testList = ce.getTestListByParentId(node, CodeTable.section);
					nodeList.addAll(testList);
				}
				boolean forumLeaf = ce.getForumLeaf(new Long(node)).booleanValue();
				if (!forumLeaf) {
					// 查询当前节点所包含的讨论
					List<MeleteForumModel> forumList = ce.getForumListByParentId(node, CodeTable.section);
					nodeList.addAll(forumList);
				}
			}

			// 登陆用户Id（学生Id）
			String studentId = this.getCurrentUserId();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd dd:mm:ss");
			int j = 1;
			for (int i = 0; i < nodeList.size(); i++) {// 遍历节点集合
				Object obj = nodeList.get(i);
				if (obj.getClass() == MeleteCourseModel.class) {// 是课程节点
					MeleteCourseModel course = (MeleteCourseModel) obj;
					AsyncTreeModel asyncTree = new AsyncTreeModel();
					String courseId = course.getId();// 设置树节点的id

					MeleteStudyRecordModel studyRecordModel = studyService.getStudyRecordById(courseId, studentId);
					studyrecordId = studyRecordModel.getStudyrecordId().toString();// 获得学习记录ID

					asyncTree.setId(courseId);
					asyncTree.setText(course.getTitle());// 设置树节点的text
					String status = course.getStatus().toString();// 获取状态

					boolean leaf = true;
					if (status.equals(CodeTable.normal)) {
						// 判断是否包含模块，没有模块则为叶子节点
						leaf = courseService.leafModuleByCourseId(courseId, true);
					}
					asyncTree.setLeaf(leaf);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("type", CodeTable.course);// 节点类型：课程
					map.put("courseId", courseId);// 课程id
					map.put("status", status);// 状态
					map.put("studentrecordId", studyrecordId);// 学习记录Id
					asyncTree.setAttributes(map);

					asyncTree.setIcon(CodeTable.icoCourse);
					String requirement = course.getRequirement();
					// asyncTree.setQtip(qtip);// 设置提示信息
					data.add(asyncTree);
				} else if (obj.getClass() == MeleteModuleModel.class) {// 是模块节点
					MeleteModuleModel module = (MeleteModuleModel) obj;
					String selfStudyTimeShow = "0";

					MeleteModuleRecordModel moduleRecord = studyService.getModuleRecordById(
							String.valueOf(studyrecordId), String.valueOf(module.getId()));

					selfStudyTimeShow = moduleRecord.getStudyTime() == null ? "0" : String.valueOf(moduleRecord
							.getStudyTime());

					AsyncTreeModel asyncTree = new AsyncTreeModel();
					Long id = module.getId();
					String title = module.getTitle();
					asyncTree.setId(id.toString());

					String status = module.getStatus().toString();// 模块状态
					String selftestId = module.getModuleSelftest();// 前测id
					boolean subLeaf = true;// 是否有子模块
					boolean secLeaf = true;// 是否有页
					boolean leaf = true;// 是否叶子节点
					String showIdx = ((nodeIdx == null || nodeIdx.equals("")) ? "" : (nodeIdx + "."));
					if (status.equals(CodeTable.normal)) {// 正常
						showIdx += j;
						j++;

						subLeaf = courseService.leafModuleByParentId(id, false);// 是否有子模块
						leaf = subLeaf;
						if (leaf) {// 没有子模块
							secLeaf = courseService.leafSectionByModuleId(id);// 是否有页
							leaf = secLeaf;
						}
						if (leaf) {// 是否有作业
							leaf = courseService.leafTestByModuleId(id, false);
						}
						if (leaf) {// 是否有讨论
							leaf = courseService.leafForunByModuleId(id, false);
						}
						if (leaf) {// 是否有前测
							leaf = selftestId == null ? true : false;
						}
						asyncTree.setIcon(CodeTable.icoModule);
						asyncTree.setText(showIdx + "&nbsp;&nbsp;" + title);
					} else {// 隐藏
						asyncTree.setIcon(CodeTable.icoHide);
						asyncTree.setText(title);
					}
					asyncTree.setLeaf(leaf);

					String startStudyDate = "";
					if (moduleRecord.getStartStudyTime() != null) {
						startStudyDate = simpleDateFormat.format(moduleRecord.getStartStudyTime());
					}
					Map<String, Object> map = new HashMap<String, Object>();

					map.put("idx", module.getIdx());// 节点序号
					map.put("type", CodeTable.module);// 节点类型：模块
					map.put("moduleTitle", title);// 模块title（用于点击模块是显示title用）
					map.put("startStudyDate", startStudyDate);// 开始学习时间
					map.put("selfStudyTimeShow", selfStudyTimeShow);// 学习时长
					map.put("studentrecordId", studyrecordId);// 学习记录Id
					map.put("recordId", moduleRecord.getModulerecordId());// 模块学习记录Id
					map.put("courseId", module.getCourseId());// 所属课程id
					map.put("status", status);// 节点状态
					map.put("activityScore", "");// 活动得分
					map.put("attemptTimes", "");// 活动尝试次数
					asyncTree.setAttributes(map);

					data.add(asyncTree);
				} else if (obj.getClass() == MeleteSectionModel.class) {// 是页节点
					MeleteSectionModel section = (MeleteSectionModel) obj;
					MeleteSectionRecordModel sectionRecord = studyService.getSectionRecordById(
							String.valueOf(studyrecordId), String.valueOf(section.getId()));

					String selfStudyTimeShow = sectionRecord.getStudyTime() == null ? "0" : String
							.valueOf(sectionRecord.getStudyTime());
					String sectionRecordId = String.valueOf(sectionRecord.getSectionrecordId());

					AsyncTreeModel asyncTree = new AsyncTreeModel();
					Long id = section.getId();
					asyncTree.setId(id.toString());

					String showIdx = nodeIdx + "." + j;
					j++;
					String title = section.getTitle();

					asyncTree.setText(showIdx + "&nbsp;&nbsp;" + title);

					String startStudyDate = "";
					if (sectionRecord.getStartStudyTime() != null) {
						startStudyDate = simpleDateFormat.format(sectionRecord.getStartStudyTime());
					}

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("moduleId", section.getModuleId());// 模块Id
					map.put("sectionRecordId", sectionRecordId);// 节点记录Id，用于判断是否是第一次学习该节点
					map.put("startStudyDate", startStudyDate);// 开始学习时间
					map.put("selfStudyTimeShow", selfStudyTimeShow);// 学习时长
					// map.put("status", sectionRecord.getStatus());// 课件开启状态
					map.put("type", CodeTable.section);// 节点类型：页
					map.put("recordId", sectionRecord.getSectionrecordId());// 页学习记录Id
					map.put("activityScore", "");// 活动得分
					map.put("attemptTimes", "");// 活动尝试次数

					asyncTree.setAttributes(map);

					boolean leaf = courseService.leafTestBySectionId(id, false);// 是否有作业
					if (leaf) {
						leaf = courseService.leafForumBySectionId(id, false);// 是否有讨论
					}
					if (leaf) {
						leaf = section.getSectionSelftest() == null ? true : false;// 是否有前测
					}
					asyncTree.setLeaf(leaf);
					asyncTree.setIcon(CodeTable.icoSection);

					data.add(asyncTree);
				} else if (obj.getClass() == MeleteTestModel.class) {// 是作业节点
					MeleteTestModel test = (MeleteTestModel) obj;
					MeleteTestRecordModel testRecord = studyService.getTestRecordById(String.valueOf(studyrecordId),
							String.valueOf(test.getId()));

					String activityScore = "";// 获得作业分数
					if (testRecord.getScore() != null) {
						activityScore = testRecord.getScore().toString();
					}
					String attemptTimes = testRecord.getAttemptNumber().toString();// 获得尝试次数
					AsyncTreeModel asyncTree = new AsyncTreeModel();
					asyncTree.setAllowDrop(false);
					asyncTree.setAllowDrag(false);
					asyncTree.setId(test.getId().toString());// 作业id
					asyncTree.setText(test.getName());// 作业名称

					asyncTree.setLeaf(true);

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("name", test.getName());// 作业名称
					map.put("type", CodeTable.test);// 节点类型：作业
					map.put("recordId", testRecord.getTestrecordId());// 作业记录Id

					map.put("activityScore", activityScore);// 活动得分
					map.put("attemptTimes", attemptTimes);// 活动尝试次数
					map.put("startStudyDate", "");// 开始学习时间
					map.put("selfStudyTimeShow", "");// 学习时长

					asyncTree.setAttributes(map);
					asyncTree.setIcon(CodeTable.icoTest);
					data.add(asyncTree);
				} else if (obj.getClass() == MeleteForumModel.class) {// 是讨论节点
					MeleteForumModel forum = (MeleteForumModel) obj;
					MeleteForumRecordModel forumRecord = studyService.getForumRecordById(String.valueOf(studyrecordId),
							String.valueOf(forum.getId()));

					String attemptTimes = forumRecord.getAttemptNumber().toString();
					AsyncTreeModel asyncTree = new AsyncTreeModel();
					asyncTree.setAllowDrop(false);
					asyncTree.setAllowDrag(false);
					asyncTree.setId(forum.getId().toString());// 论坛id
					asyncTree.setText(forum.getName());

					asyncTree.setLeaf(true);
					String areaId = forum.getAreaId(); // 讨论区id
					String topicId = forum.getTopicId();// 帖子主题id
					String forumId = forum.getForumId();// 论坛id

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("name", forum.getName());// 论坛名称
					map.put("areaId", areaId);// 讨论区id
					map.put("topicId", topicId);// 帖子主题id
					map.put("forumId", forumId);// 论坛id
					map.put("type", CodeTable.forum);// 节点类型：讨论
					map.put("recordId", forumRecord.getForumrecordId());// 讨论记录Id

					map.put("activityScore", "");// 活动得分
					map.put("attemptTimes", attemptTimes);// 活动尝试次数
					map.put("startStudyDate", "");// 开始学习时间
					map.put("selfStudyTimeShow", "");// 学习时长
					asyncTree.setAttributes(map);
					asyncTree.setIcon(CodeTable.icoForum);
					data.add(asyncTree);
				} else if (obj.getClass() == MeleteSelfTestModel.class) {// 是前测节点
					MeleteSelfTestModel self = (MeleteSelfTestModel) obj;
					MeleteSelftestRecordModel selftestRecord = studyService.getSelftestRecordById(
							String.valueOf(studyrecordId), String.valueOf(self.getId()));

					String activityScore = "";// 获得前测分数
					if (selftestRecord.getScore() != null) {
						activityScore = selftestRecord.getScore().toString();
					}
					String attemptTimes = selftestRecord.getAttemptNumber().toString();// 获得前测尝试次数
					AsyncTreeModel asyncTree = new AsyncTreeModel();
					asyncTree.setAllowDrop(false);
					asyncTree.setAllowDrag(false);
					asyncTree.setId(self.getId().toString());// 前测id
					asyncTree.setText(self.getName());

					asyncTree.setLeaf(true);

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("name", self.getName());// 前测名称
					map.put("belongType", self.getBelongType());// 所属类型
					map.put("requirement", self.getRequirement());// 通过条件中文说明
					map.put("samepaper", self.getSamepaper());// 使用同一策略
					map.put("schemaId", self.getSchemaId());// 策略id
					map.put("idx", self.getIdx());// 前测序号
					map.put("type", CodeTable.selftest);// 节点类型：前测
					map.put("recordId", selftestRecord.getSelftestrecordId());// 前测记录Id

					map.put("activityScore", activityScore);// 活动得分
					map.put("attemptTimes", attemptTimes);// 活动尝试次数
					map.put("startStudyDate", "");// 开始学习时间
					map.put("selfStudyTimeShow", "");// 学习时长
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

	/** 查询活动成绩 */
	public String findActivityScore() throws Exception {
		if (!this.getIsShow()) {
			return "findActivityScore";
		} else if (activeName == null || "".equals(activeName) || maxScore == null || minScore == null || times == null) {
			throw new Exception("选项不能为空！");
		}

		try {
			// 根据条件查询某个站点所有学生的活动成绩
			List<Map<String, Object>> list = statisticsService.findActivityScore(getConditions(),
					Integer.parseInt(this.splitActiveName()[1]));
			// 创建一个格式化对象数据的集合，用来把数据加载到页面的grid里
			List<AsyncGridModel> data = new ArrayList<AsyncGridModel>();
			// 开始遍历集合，封装到AsyncGridModel这个类里
			for (Map map : list) {
				AsyncGridModel model = new AsyncGridModel();
				model.setDataIndex("name", map.get("name"));// 姓名
				model.setDataIndex("publicstunum", map.get("publicstunum"));// 学号
				model.setDataIndex("organizationName", map.get("organizationName"));// 中心名称
				model.setDataIndex("cellphone", map.get("cellphone"));// 手机
				model.setDataIndex("telephone", map.get("telephone"));// 固定电话
				model.setDataIndex("email", map.get("email"));// Email
				model.setDataIndex("attemptNumber", map.get("attemptNumber"));// 次数
				// 状态
				if (CodeTable.passStatusYes.equals(map.get("status").toString())) {
					model.setDataIndex("status", Constants.PASS_VIEW);
				} else if (CodeTable.passStatusNo.equals(map.get("status").toString())) {
					model.setDataIndex("status", Constants.NOTPASS_VIEW);
				}
				// model.setDataIndex("status", map.get("status"));// 状态
				model.setDataIndex("score", map.get("score"));// 成绩
				data.add(model);
			}
			String json = JsonBuilder.builderAsyncGridJson(data);
			this.getResponse().getWriter().println(json);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/** 统计活动平均次数和成绩 */
	public String statActivityAvgTimeAndScore() throws Exception {
		if (!this.getIsShow()) {
			return "statActivityAvgTimeAndScore";
		}

		try {
			DecimalFormat dFormat = new DecimalFormat("#.0");
			// 获得作业的平均次数和成绩
			List<Map<String, Object>> list = statisticsService.statActivityAvgTimeAndScoreOfTestRecord(getCurrentSite()
					.getId());
			// 获得前测的平均次数和成绩
			list.addAll(statisticsService.statActivityAvgTimeAndScoreOfSelfTestRecord(getCurrentSite().getId()));
			// 获得论坛的平均次数和成绩
			list.addAll(statisticsService.statActivityAvgTimeAndScoreOfForumRecord(getCurrentSite().getId()));
			// 创建一个格式化对象数据的集合，用来把数据加载到页面的grid里
			List<AsyncGridModel> data = new ArrayList<AsyncGridModel>();
			// 开始遍历集合，封装到AsyncGridModel这个类里
			if (!list.isEmpty() && list.size() != 0) {
				for (Map map : list) {
					AsyncGridModel model = new AsyncGridModel();
					model.setDataIndex("title", map.get("title"));// 活动名称
					model.setDataIndex("avgTimes", map.get("avgTimes"));// 平均次数
					model.setDataIndex("avgScore", dFormat.format((Double) map.get("avgScore")));// 平均成绩
					data.add(model);
				}
				String json = JsonBuilder.builderAsyncGridJson(data);
				this.getResponse().getWriter().println(json);
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/** 查询某门课程的所有活动名称放在combox里 */
	public String findActiveNameBox() throws Exception {
		// 根据站点ID所有的作业和前测的ID与名称
		List<Object> resultList = courseService.getTestsAndSelfTestsBySiteId(getCurrentSite().getId());
		StringBuffer sb = new StringBuffer("[");
		try {
			if (!resultList.isEmpty()) {
				for (int i = 0; i < resultList.size(); i++) {
					Object object = resultList.get(i);
					if (object.getClass() == MeleteTestModel.class) {
						MeleteTestModel testModel = (MeleteTestModel) object;
						sb.append("['").append(testModel.getId()).append("_1','").append(testModel.getName())
								.append("'],");
					} else {
						MeleteSelfTestModel selfTestModel = (MeleteSelfTestModel) object;
						sb.append("['").append(selfTestModel.getId()).append("_2','").append(selfTestModel.getName())
								.append("'],");
					}

				}
				sb = sb.deleteCharAt(sb.length() - 1);
			}
			sb.append("]");
			this.getResponse().getWriter().println(sb.toString());
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/** 封装查询活动成绩的条件 */
	private Map<String, Object> getConditions() throws Exception {
		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("id", new Long(activeName.split("_")[0]));
		conditions.put("siteId", this.getCurrentSite().getId());
		conditions.put("timesLimit", timesLimit);
		conditions.put("times", new Long(times));
		if (status != null && !status.equals(""))
			conditions.put("status", new Long(status));
		conditions.put("minScore", new Float(minScore));
		conditions.put("maxScore", new Float(maxScore));
		return conditions;
	}

	/**
	 * 重新整合统计学生活动情况的数据
	 * 
	 * @param stuStateList
	 *            获得的学生活动集合
	 */
	private List<Map<String, Object>> getStatStudentList(List<Map<String, Object>> stuActStateList) throws Exception {
		List<Map<String, Object>> lst = new ArrayList<Map<String, Object>>();

		Map<Long, Map<String, Object>> resultMap = new HashMap<Long, Map<String, Object>>();
		for (Map<String, Object> actMap : stuActStateList) {

			if (resultMap.get((Long) actMap.get("id")) == null) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("title", actMap.get("title").toString());
				map.put("partakeNumbers", new Long(0));
				map.put("unpartakeNumbers", new Long(0));
				map.put("passNumbers", new Long(0));
				map.put("unpassNumbers", new Long(0));
				resultMap.put((Long) actMap.get("id"), map);
			}
			Map<String, Object> map = resultMap.get((Long) actMap.get("id"));
			if ((Long) actMap.get("attemptNum") > 0) {
				// 参与活动人数
				map.put("partakeNumbers", (Long) map.get("partakeNumbers") + 1);
			} else if ((Long) actMap.get("attemptNum") == 0) {
				// 未参与活动人数
				map.put("unpartakeNumbers", (Long) map.get("unpartakeNumbers") + 1);
			}

			if (CodeTable.passStatusYes.equals(actMap.get("passStatus").toString())) {
				// 通过人数
				map.put("passNumbers", (Long) map.get("passNumbers") + 1);
			} else if ((Long) actMap.get("attemptNum") > 0) {
				// 参与了活动但未通过人数
				map.put("unpassNumbers", (Long) map.get("unpassNumbers") + 1);
			}

		}
		Set entrySet = resultMap.entrySet();
		Iterator iterator = entrySet.iterator();
		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			lst.add(resultMap.get(entry.getKey()));
		}
		return lst;
	}

	/** 拆分活动名称 */
	private String[] splitActiveName() {
		return this.activeName.split("_");
	}

	/** 查看学生学习记录 */
	public String viewStudyRecordList() throws Exception {
		try {
			modelList = statisticsService.getModuleBySiteId(this.getCurrentSite().getId(), false);
			// 重新封装叶节点的数据
			separateSectionListByModuleId();
			// 重新封装章节点数据
			recursionLoadModel(sectionMap);
			// 把前面封装好的数据封装到最外层的章上
			getFirstModule();
			print = new StringBuffer("<table border='1'><tr><th>名称</th><th>开始学习时间</th><th>学习时长</th>");
			print.append("<th>时长完成率</th><th>作业</th><th>前测</th><th>论坛</th></tr>");
			print(studyRecordList, 0, "");
			print.append("</table>");
			ServletActionContext.getRequest().setAttribute("data", print.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return "studyRecord";
	}

	/** 获得所有的章--父节点为NULL的 */
	private void getFirstModule() throws Exception {
		try {
			List<Map<String, Object>> mapList = statisticsService
					.getModuleBySiteId(this.getCurrentSite().getId(), true);
			for (int i = 0; i < mapList.size(); i++) {
				Map<String, Object> firstMap = mapList.get(i);
				// 创建一个有序的map集合
				Map<String, Object> linkedMap = new LinkedHashMap<String, Object>();
				// linkedMap.put("id", firstMap.get("id"));
				// 给标题重新复制，加上序号
				linkedMap.put("title", firstMap.get("title"));
				// 开始学习时间
				linkedMap.put("sst", firstMap.get("sst"));
				// 存储作业
				linkedMap.put("testModel", getTestModelByParentId(firstMap.get("id").toString()));
				// 存储前测
				linkedMap.put("selfModel", getSelfTestModelByParentId(firstMap.get("id").toString()));
				// 存储论坛
				linkedMap.put("forumModel", getForumModelByParentId(firstMap.get("id").toString()));
				// 存储下一级
				linkedMap.put("child", moduleMap.get(firstMap.get("id")));
				studyRecordList.add(linkedMap);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/** 递归把页加载到(章)节上，然后把(章)节加载到章上 */
	private void recursionLoadModel(Map<String, List<Map<String, Object>>> childMap) throws Exception {
		// 如果已经把modelList里的数据都按规则封装好后就不再执行递归调用
		boolean bl = true;
		// 为了防止childMap没有数据而出现死循环
		if (childMap.size() == 0) {
			bl = false;
		}
		for (Map.Entry<String, List<Map<String, Object>>> srMap : childMap.entrySet()) {
			// 如果已经把modelList集合遍历，则退出循环
			if (modelIdList.size() == modelList.size()) {
				bl = false;
				break;
			}
			try {
				for (Map<String, Object> mp : modelList) {
					if (!modelIdList.contains(mp.get("id"))) {
						// 如果节点的父ID和章节点的ID相同那么就把它加载到章节点里
						if (moduleMap.get(mp.get("pId")) == null && mp.get("id").equals(srMap.getKey())) {
							moduleMap.put(mp.get("pId").toString(), this.getData(mp, null));
							modelIdList.add(mp.get("id"));
						} else if (moduleMap.get(mp.get("pId")) != null && mp.get("id").equals(srMap.getKey())) {
							List<Map<String, Object>> temp = moduleMap.get(mp.get("pId"));
							moduleMap.put(mp.get("pId").toString(), getData(mp, temp));
							modelIdList.add(mp.get("id"));
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
				throw e;
			}
		}
		if (bl) {
			recursionLoadModel(moduleMap);
		}
	}

	/** 把section里的数据以父节点(moduleId)为主重新封装 */
	private void separateSectionListByModuleId() throws Exception {
		// 所有的叶节点
		List<Map<String, Object>> list = statisticsService.getSectionBySiteId(this.getCurrentSite().getId());
		for (Map mp : list) {
			// 根据父节点来重新封装叶节点
			if (sectionMap.get(mp.get("moduleId")) == null) {
				sectionMap.put(mp.get("moduleId").toString(), getData(mp, null));
			} else {
				List<Map<String, Object>> temp = sectionMap.get(mp.get("moduleId"));
				sectionMap.put(mp.get("moduleId").toString(), getData(mp, temp));
			}
		}
	}

	/** 封装页面展示的每行的数据 */
	private List<Map<String, Object>> getData(Map mp, List<Map<String, Object>> lst) throws Exception {
		List<Map<String, Object>> temp;
		try {
			if (lst == null) {
				temp = new ArrayList<Map<String, Object>>();
			} else {
				temp = lst;
			}
			// 创建一个有序的map集合
			Map<String, Object> linkedMap = new LinkedHashMap<String, Object>();
			// 给标题重新复制，加上序号
			linkedMap.put("title", mp.get("title"));
			// 开始学习时间
			linkedMap.put("sst", mp.get("sst"));
			// 存储作业
			linkedMap.put("testModel", getTestModelByParentId(mp.get("id").toString()));
			// 存储前测
			linkedMap.put("selfModel", getSelfTestModelByParentId(mp.get("id").toString()));
			// 存储论坛
			linkedMap.put("forumModel", getForumModelByParentId(mp.get("id").toString()));
			temp.add(linkedMap);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
		return temp;
	}

	/** 根据父ID查询所有的作业 */
	private List<Map<String, Object>> getTestModelByParentId(String pId) throws Exception {
		return separateListByUpModelId(statisticsService.getTestBySIiteId(this.getCurrentSite().getId())).get(pId);
	}

	/** 根据父ID查询所有前测 */
	private List<Map<String, Object>> getSelfTestModelByParentId(String pId) throws Exception {
		return separateListByUpModelId(statisticsService.getSelfTestBySIiteId(this.getCurrentSite().getId())).get(pId);
	}

	/** 根据父ID查询所有论坛 */
	private List<Map<String, Object>> getForumModelByParentId(String pId) throws Exception {
		return separateListByUpModelId(statisticsService.getForumBySIiteId(this.getCurrentSite().getId())).get(pId);
	}

	/** 根据父类的ID来重新封装作业或者前测或者论坛数据 */
	private Map<String, List<Map<String, Object>>> separateListByUpModelId(List<Map<String, Object>> lst)
			throws Exception {
		Map<String, List<Map<String, Object>>> map = new HashMap<String, List<Map<String, Object>>>();
		try {
			for (Map mp : lst) {
				Object pid;
				if (mp.get("sectionId") != null) {
					pid = mp.get("sectionId");
				} else {
					pid = mp.get("moduleId");
				}
				if (map.get(pid) == null) {
					// 创建一个有序的map集合,这个map是有序的
					Map<String, Object> linkedMap = new LinkedHashMap<String, Object>();
					List<Map<String, Object>> temp = new ArrayList<Map<String, Object>>();
					linkedMap.put("name", mp.get("name"));
					if (mp.get("score") != null) {
						linkedMap.put("score", mp.get("score"));
					}
					linkedMap.put("num", mp.get("num"));
					temp.add(linkedMap);
					map.put(pid.toString(), temp);
				} else {
					// 创建一个有序的map集合,这个map是有序的
					Map<String, Object> linkedMap = new LinkedHashMap<String, Object>();
					List<Map<String, Object>> temp = map.get(pid);
					linkedMap.put("name", mp.get("name"));
					if (mp.get("score") != null)
						linkedMap.put("score", mp.get("score"));
					linkedMap.put("num", mp.get("num"));
					temp.add(linkedMap);
					map.put(pid.toString(), temp);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
		return map;
	}

	/** 把一个可以转换成int类型的string类型转换成int值 */
	private int convert(Object str) {
		if (str == null) {
			return 0;
		} else {
			return Integer.parseInt(str.toString());
		}
	}

	/** 输出到页面上 */
	private void print(List<Map<String, Object>> list, int space, String next) {
		if (space == 0 && list.size() == 0) {
			print.append("没有数据");
		}
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			for (int k = 0; k < space; k++) {
				print.append(" ");
			}
			String sn = next + (i + 1) + ".";
			print.append("<tr>");
			print.append("<td>").append(sn + map.get("title")).append("</td>");// 展示标题
			print.append("<td>").append(map.get("sst")).append("</td>");// 展示开始学习时间
			print.append("<td>").append("studyTime").append("</td>");// 展示学习时长
			print.append("<td>").append(map.get("rate")).append("</td>");// 展示完成率
			print.append("<td>");
			List<Map<String, Object>> testList = (List<Map<String, Object>>) map.get("testModel");
			if (testList != null && !testList.isEmpty() && testList.size() != 0) {
				print.append("<table>");
				for (int j = 0; j < testList.size(); j++) {
					Map<String, Object> testMap = testList.get(j);
					print.append("<tr>");
					print.append("<td>").append(testMap.get("name")).append("</td>");// 作业名称
					print.append("<td>").append(testMap.get("score")).append("</td>");// 作业得分
					print.append("<td>").append(testMap.get("num")).append("</td>");// 提交作业次数
					print.append("</tr>");
				}
				print.append("</table>");
			}
			print.append("</td>");
			print.append("<td>");
			List<Map<String, Object>> selfList = (List<Map<String, Object>>) map.get("selfModel");
			if (selfList != null && !selfList.isEmpty() && selfList.size() != 0) {
				print.append("<table>");
				for (int j = 0; j < selfList.size(); j++) {
					Map<String, Object> selfMap = selfList.get(j);
					print.append("<tr>");
					print.append("<td>").append(selfMap.get("name")).append("</td>");// 前测名称
					print.append("<td>").append(selfMap.get("score")).append("</td>");// 前测得分
					print.append("<td>").append(selfMap.get("num")).append("</td>");// 前测次数
					print.append("</tr>");
				}
				print.append("</table>");
			}
			print.append("</td>");
			print.append("<td>");
			List<Map<String, Object>> forumList = (List<Map<String, Object>>) map.get("forumModel");
			if (forumList != null && !forumList.isEmpty() && forumList.size() != 0) {
				print.append("<table>");
				for (int j = 0; j < forumList.size(); j++) {
					Map<String, Object> formMap = forumList.get(j);
					print.append("<tr>");
					print.append("<td>").append(formMap.get("name")).append("</td>");// 论坛名称
					print.append("<td>").append(formMap.get("num")).append("</td>");// 论坛参与次数
					print.append("</tr>");
				}
				print.append("</table>");
			}
			print.append("</td>");
			print.append("</tr>");
			if (map.get("child") != null) {
				print((List<Map<String, Object>>) map.get("child"), space + 1, sn);
			}
		}
	}

	public String view() {
		List<Map<String, Object>> lst1 = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> lst2 = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> lst3 = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> lst4 = new ArrayList<Map<String, Object>>();
		Map<String, Object> obj1 = new HashMap<String, Object>();
		obj1.put("title", "A");
		lst1.add(obj1);
		Map<String, Object> obj2 = new HashMap<String, Object>();
		obj2.put("title", "B");
		obj2.put("chile", lst1);
		lst2.add(obj2);
		Map<String, Object> obj3 = new HashMap<String, Object>();
		obj3.put("title", "C");
		obj3.put("child", lst2);
		lst3.add(obj3);
		Map<String, Object> obj4 = new HashMap<String, Object>();
		obj4.put("title", "D");
		obj4.put("child", lst3);
		lst4.add(obj4);
		test(lst4, 0, "");
		return "";
	}

	private void test(List<Map<String, Object>> list, int space, String next) {
		if (space == 0 && list.size() == 0) {
			print.append("没有数据");
		}
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			for (int k = 0; k < space; k++) {
				print.append(" ");
			}
			String sn = next + (i + 1) + ".";
			print.append("<tr>");
			print.append("<td>").append(sn + map.get("title")).append("</td>");// 展示标题
			System.out.println(sn + map.get("title"));
			print.append("</tr>");
			if (map.get("child") != null) {
				print((List<Map<String, Object>>) map.get("child"), space + 1, sn);
			}
		}
	}

	public Boolean getIsShow() {
		return isShow;
	}

	public void setIsShow(Boolean isShow) {
		this.isShow = isShow;
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

	public IFileService getFileService() {
		return fileService;
	}

	public void setFileService(IFileService fileService) {
		this.fileService = fileService;
	}

	/**
	 * 获得request对象
	 * 
	 * @author lzp
	 */
	private HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	/**
	 * 获得response对象
	 * 
	 * @author lzp
	 */
	private HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}

	public IStatisticsService getStatisticsService() {
		return statisticsService;
	}

	public void setStatisticsService(IStatisticsService statisticsService) {
		this.statisticsService = statisticsService;
	}

	public ICourseService getCourseService() {
		return courseService;
	}

	public void setCourseService(ICourseService courseService) {
		this.courseService = courseService;
	}

	public String getActiveName() {
		return activeName;
	}

	public void setActiveName(String activeName) {
		this.activeName = activeName;
	}

	public String getTimesLimit() {
		return timesLimit;
	}

	public void setTimesLimit(String timesLimit) {
		this.timesLimit = timesLimit;
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMinScore() {
		return minScore;
	}

	public void setMinScore(String minScore) {
		this.minScore = minScore;
	}

	public String getMaxScore() {
		return maxScore;
	}

	public void setMaxScore(String maxScore) {
		this.maxScore = maxScore;
	}

	public StringBuffer getPrint() {
		return print;
	}

	public void setPrint(StringBuffer print) {
		this.print = print;
	}

	public List<Map<String, Object>> getStudyRecordList() {
		return studyRecordList;
	}

	public void setStudyRecordList(List<Map<String, Object>> studyRecordList) {
		this.studyRecordList = studyRecordList;
	}

	public IStudyService getStudyService() {
		return studyService;
	}

	public void setStudyService(IStudyService studyService) {
		this.studyService = studyService;
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

	public String getNodeIdx() {
		return nodeIdx;
	}

	public void setNodeIdx(String nodeIdx) {
		this.nodeIdx = nodeIdx;
	}

	public String getNoderecordId() {
		return noderecordId;
	}

	public void setNoderecordId(String noderecordId) {
		this.noderecordId = noderecordId;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getStudyrecordId() {
		return studyrecordId;
	}

	public void setStudyrecordId(String studyrecordId) {
		this.studyrecordId = studyrecordId;
	}
}
