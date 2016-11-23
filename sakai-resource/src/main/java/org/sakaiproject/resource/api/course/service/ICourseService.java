package org.sakaiproject.resource.api.course.service;

import java.io.File;
import java.util.List;
import java.util.Vector;

import org.sakaiproject.resource.api.course.model.MeleteCourseModel;
import org.sakaiproject.resource.api.course.model.MeleteForumModel;
import org.sakaiproject.resource.api.course.model.MeleteModuleModel;
import org.sakaiproject.resource.api.course.model.MeleteSectionModel;
import org.sakaiproject.resource.api.course.model.MeleteSelfTestModel;
import org.sakaiproject.resource.api.course.model.MeleteTestModel;
import org.sakaiproject.resource.api.study.model.MeleteSectionRecordModel;
import org.sakaiproject.resource.api.study.model.MeleteTestAttemptModel;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public interface ICourseService {

	/**
	 * 根据站点id查询课程信息
	 * 
	 * @param siteId
	 * @return
	 * @throws Exception
	 */
	public MeleteCourseModel getCourseBySiteId(String siteId) throws Exception;

	/**
	 * 保存课程信息
	 * 
	 * @param course
	 * @throws Exception
	 */
	public void saveCourse(MeleteCourseModel course) throws Exception;

	/**
	 * 根据课程id查询是否有下级模块，用于判断课程节点是否为叶子
	 * 
	 * @param courseId
	 * @return
	 * @throws Exception
	 */
	public boolean leafModuleByCourseId(String courseId, boolean showHide) throws Exception;

	/**
	 * 根据课程id查询模块集合，展开课程节点时调用，用于加载下级模块
	 * 
	 * @param courseId
	 * @return
	 * @throws Exception
	 */
	public List<MeleteModuleModel> getModuleByCourseId(String courseId, boolean showHide) throws Exception;

	/**
	 * 根据上级模块id查询是否有下级模块，用于判断模块节点是否为叶子
	 * 
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	public boolean leafModuleByParentId(Long parentId, boolean showHide) throws Exception;

	/**
	 * 根据模块id查询是否有页，用于判断模块节点是否为叶子
	 * 
	 * @param moduleId
	 * @return
	 * @throws Exception
	 */
	public boolean leafSectionByModuleId(Long moduleId) throws Exception;

	/**
	 * 根据模块id查询必修页总个数
	 * 
	 * @param moduleId
	 * @return
	 * @throws Exception
	 */
	public Long countRequiredSecByModuleId(Long moduleId) throws Exception;

	/**
	 * 根据模块id查询选修页总个数
	 * 
	 * @param moduleId
	 * @return
	 * @throws Exception
	 */
	public Long countElectiveSecByModuleId(Long moduleId) throws Exception;

	/**
	 * 根据模块id查询是否有作业，用于判断模块节点是否为叶子
	 * 
	 * @param moduleId
	 * @return
	 * @throws Exception
	 */
	public boolean leafTestByModuleId(Long moduleId, boolean showScore) throws Exception;

	/**
	 * 根据模块id查询是否有讨论，用于判断模块节点是否为叶子
	 * 
	 * @param moduleId
	 * @return
	 * @throws Exception
	 */
	public boolean leafForunByModuleId(Long moduleId, boolean showScore) throws Exception;

	/**
	 * 根据模块id查询是否有前测，用于判断模块节点是否为叶子
	 * 
	 * @param moduleId
	 * @return
	 * @throws Exception
	 */
	public boolean leafSelftestByModuleId(Long moduleId, boolean showScore) throws Exception;

	/**
	 * 根据上级模块id查询下级模块集合，展开模块节点时调用，用于加载下级模块
	 * 
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	public List<MeleteModuleModel> getModuleByParentId(Long parentId, boolean showHide) throws Exception;

	/**
	 * 根据模块id查询页集合，展开模块节点时调用，用于加载页
	 * 
	 * @param moduleId
	 * @return
	 * @throws Exception
	 */
	public List<MeleteSectionModel> getSectionByModuleId(Long moduleId, Long status) throws Exception;

	/**
	 * 根据模块id查询作业集合，展开模块节点时调用，用于加载作业
	 * 
	 * @param moduleId
	 * @return
	 * @throws Exception
	 */
	public List<MeleteTestModel> getTestByModuleId(Long moduleId, boolean showScore) throws Exception;

	/**
	 * 根据模块id查询讨论集合，展开模块节点时调用，用于加载讨论
	 * 
	 * @param moduleId
	 * @return
	 * @throws Exception
	 */
	public List<MeleteForumModel> getForumByModuleId(Long moduleId, boolean showScore) throws Exception;

	/**
	 * 根据模块id查询前测集合，展开模块节点时调用，用于加载前测
	 * 
	 * @param moduleId
	 * @return
	 * @throws Exception
	 */
	public List<MeleteSelfTestModel> getSelftestByModuleId(Long moduleId, boolean showScore) throws Exception;

	/**
	 * 根据页id查询是否有作业，用于判断页节点是否为叶子
	 * 
	 * @param sectionId
	 * @return
	 * @throws Exception
	 */
	public boolean leafTestBySectionId(Long sectionId, boolean showScore) throws Exception;

	/**
	 * 根据页id查询是否有讨论，用于判断页节点是否为叶子
	 * 
	 * @param sectionId
	 * @return
	 * @throws Exception
	 */
	public boolean leafForumBySectionId(Long sectionId, boolean showScore) throws Exception;

	/**
	 * 根据页id查询是否有前测，用于判断页节点是否为叶子
	 * 
	 * @param sectionId
	 * @return
	 * @throws Exception
	 */
	public boolean leafSelftestBySectionId(Long sectionId, boolean showScore) throws Exception;

	/**
	 * 根据页id查询作业集合，展开页节点时调用，用于加载作业
	 * 
	 * @param sectionId
	 * @return
	 * @throws Exception
	 */
	public List<MeleteTestModel> getTestBySectionId(Long sectionId, boolean showScore) throws Exception;

	/**
	 * 根据页id查询讨论集合，展开页节点时调用，用于加载讨论
	 * 
	 * @param sectionId
	 * @return
	 * @throws Exception
	 */
	public List<MeleteForumModel> getForumBySectionId(Long sectionId, boolean showScore) throws Exception;

	/**
	 * 根据页id查询前测集合，展开页节点时调用，用于加载前测
	 * 
	 * @param sectionId
	 * @return
	 * @throws Exception
	 */
	public List<MeleteSelfTestModel> getSelftestBySectionId(Long sectionId, boolean showScore) throws Exception;

	/**
	 * 根据课程id获取课程持久类
	 * 
	 * @param courseId
	 * @return
	 * @throws Exception
	 */
	public MeleteCourseModel getCourseById(String courseId) throws Exception;

	/**
	 * 更新课程信息
	 * 
	 * @param course
	 * @throws Exception
	 */
	public void updateCourse(MeleteCourseModel course) throws Exception;

	/**
	 * 获取课件资源json串，用于前台加载下拉列表的数据源
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getCoursewareBox() throws Exception;

	/**
	 * 保存模块信息
	 * 
	 * @param module
	 * @throws Exception
	 */
	public String editModule(MeleteModuleModel module) throws Exception;

	/**
	 * 保存页信息
	 * 
	 * @param section
	 * @throws Exception
	 */
	public String editSection(MeleteSectionModel section) throws Exception;

	/**
	 * 保存作业
	 * 
	 * @param test
	 * @param editType
	 *            保存类型： 添加 add 更改 update
	 * @return
	 * @throws Exception
	 */
	public String editTest(MeleteTestModel test, String editType) throws Exception;

	/**
	 * 保存前测
	 * 
	 * @param test
	 * @param editType
	 *            保存修改 类型 添加 add 更改 update
	 * @return
	 * @throws Exception
	 */
	public String editSelfTest(MeleteSelfTestModel test, String editType) throws Exception;

	/**
	 * 保存讨论
	 * 
	 * @param forum
	 * @return
	 * @throws Exception
	 */
	public String editForum(MeleteForumModel forum) throws Exception;

	/**
	 * 根据课程id查询所有需要计算平时成绩的活动的总个数
	 * 
	 * @param courseId
	 * @return
	 * @throws Exception
	 */
	public Long countScoreActNum(String courseId) throws Exception;

	/**
	 * 根据课程id获取所有计算平时成绩的活动集合
	 * 
	 * @param courseId
	 * @return
	 * @throws Exception
	 */
	public List<Object> getActList(String courseId) throws Exception;

	/**
	 * 保存活动所占成绩百分比
	 * 
	 * @param id
	 * @param type
	 * @param ratio
	 * @param impressionScore
	 * @param impressionType
	 * @param courseId
	 * @throws Exception
	 */
	public void saveRatio(String[] id, String[] type, String[] ratio, String impressionScore, String impressionType,
			String courseId) throws Exception;

	/**
	 * 修改某一持久化类的状态
	 * 
	 * @param clazz
	 * @param id
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public String changeModelStatus(Class clazz, Long id, Long status) throws Exception;

	/**
	 * 根据类名和id获取某个持久化类
	 * 
	 * @param classname
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Object getModelById(Class clazz, Long id) throws Exception;

	/**
	 * 根据课程id查询选修模块的总个数
	 * 
	 * @param courseId
	 * @return
	 * @throws Exception
	 */
	public Long countRequiredByCourseId(String courseId) throws Exception;

	/**
	 * 根据课程id查询必修模块的总个数
	 * 
	 * @param courseId
	 * @return
	 * @throws Exception
	 */
	public Long countElectiveByCourseId(String courseId) throws Exception;

	/**
	 * 保存拖拽结果
	 * 
	 * @param point
	 * @param origId
	 * @param origType
	 * @param destId
	 * @param destType
	 * @throws Exception
	 */
	public String dragNode(String point, String origId, String origType, String destId, String destType)
			throws Exception;

	/**
	 * 修改模块的状态，同时修改子模块页活动的状态
	 * 
	 * @param moduleId
	 * @param status
	 * @throws Exception
	 */
	public void changeModuleStatus(Long moduleId, Long status) throws Exception;

	/**
	 * 修改页的状态，同时修改所包含的活动的状态
	 * 
	 * @param sectionId
	 * @param status
	 * @throws Exception
	 */
	public void changeSectionStatus(Long sectionId, Long status) throws Exception;

	/**
	 * 查询课程一级节点的最大序号
	 * 
	 * @param courseId
	 * @return
	 * @throws Exception
	 */
	public Long getMaxModuleIdxByCourseId(String courseId) throws Exception;

	/**
	 * 查询兄弟节点最大序号
	 * 
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	public Long getMaxModuleIdxByParentId(Long parentId) throws Exception;

	/**
	 * 根据模块id查询兄弟页中的最大序号
	 * 
	 * @param moduleId
	 * @return
	 * @throws Exception
	 */
	public Long getMaxSectionIdxByModuleId(Long moduleId) throws Exception;

	/**
	 * 导入课件资源
	 * 
	 * @param courseId
	 * @param mods
	 * @param path
	 * @throws Exception
	 */
	public List<Object> importScorm(String courseId, String mods, String path) throws Exception;

	/**
	 * 初始化导入资源的属性
	 * 
	 * @param nodeTypes
	 * @param ids
	 * @param requireds
	 * @param studyTimes
	 * @param prerequidses
	 * @param minSecNums
	 * @throws Exception
	 */
	public void setScormAttr(String[] nodeTypes, String[] ids, String[] requireds, String[] studyTimes,
			String[] prerequidses, String[] minSecNums, String[] reqNums, String[] childTypes, String[] titles)
			throws Exception;

	/**
	 * 根据课件资源的根节点获取模块列表树
	 * 
	 * @param wardId
	 * @return
	 * @throws Exception
	 */
	public String initModuleScorm(Node parentNode) throws Exception;

	/**
	 * 导入选择的课件模块
	 * 
	 * @param courseId
	 * @param parentId
	 * @param parentType
	 * @param mods
	 * @param warePath
	 * @param coursePath
	 * @param parentNode
	 * @param resources
	 * @param relaPath
	 * @param resourceIdList
	 * @return
	 * @throws Exception
	 */
	public List importModuleScorm(String courseId, String parentId, String parentType, String mods, String warePath,
			String coursePath, Node parentNode, Vector resources, String relaPath, List<String> resourceIdList)
			throws Exception;

	/**
	 * 根据课件资源的根节点获取页列表树
	 * 
	 * @param wardId
	 * @return
	 * @throws Exception
	 */
	public String initSectionScorm(Node parentNode) throws Exception;
	

	/**
	 * 根据课件资源的根节点获取课件预览页列表树
	 * 
	 * @param wardId
	 * @return
	 * @throws Exception
	 */
	public String initSectionScormPreview(String wareId,Node parentNode,Vector resources) throws Exception;

	/**
	 * 保存从资源包中引入的页
	 * 
	 * @param refs
	 * @param ids
	 * @param titles
	 * @param resources
	 * @param moduleId
	 * @param warePath
	 * @param coursePath
	 * @param relaPath
	 * @return
	 * @throws Exception
	 */
	public List<String> importSectionScorm(String[] idAndIdRef, String[] titles, Vector resources, String moduleId,
			String warePath, String coursePath, String relaPath, String courseId) throws Exception;

	/**
	 * 根据模块id查询下一个模块
	 * 
	 * @param moduleId
	 * @return
	 * @throws Exception
	 */
	public MeleteModuleModel getNextModule(String moduleId) throws Exception;

	/**
	 * 根据模块id查询上一个模块
	 * 
	 * @param moduleId
	 * @return
	 * @throws Exception
	 */
	public MeleteModuleModel getPreviousModule(String moduleId) throws Exception;

	/**
	 * 根据模块id查询第一个模块
	 * 
	 * @param moduleId
	 * @return
	 * @throws Exception
	 */
	public MeleteModuleModel getFirstModule(String moduleId) throws Exception;

	/**
	 * 根据模块id查询最后一个模块
	 * 
	 * @param moduleId
	 * @return
	 * @throws Exception
	 */
	public MeleteModuleModel getLastModule(String moduleId) throws Exception;

	/**
	 * 根据页id查询下一页
	 * 
	 * @param moduleId
	 * @return
	 * @throws Exception
	 */
	public MeleteSectionModel getNextSection(String sectionId) throws Exception;

	/**
	 * 根据页id查询上一页
	 * 
	 * @param moduleId
	 * @return
	 * @throws Exception
	 */
	public MeleteSectionModel getPreviousSection(String sectionId) throws Exception;

	/**
	 * 根据页id查询第一页
	 * 
	 * @param sectionId
	 * @return
	 * @throws Exception
	 */
	public MeleteSectionModel getFirstSection(String sectionId) throws Exception;

	/**
	 * 根据页id查询最后一页
	 * 
	 * @param sectionId
	 * @return
	 * @throws Exception
	 */
	public MeleteSectionModel getLastSection(String sectionId) throws Exception;

	/**
	 * 根据上级模块id查询下级选修模块总个数
	 * 
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	public Long countElectiveByParentId(Long parentId) throws Exception;

	/**
	 * 根据上级模块id查询下级必修模块总个数
	 * 
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	public Long countRequiredByParentId(Long parentId) throws Exception;

	/**
	 * 更新模块的通过条件--实现代码已注销，页面不需要在显示所以也不再更新-lzp 2012-3-29
	 * 
	 * @param id
	 *            模块id
	 * @param childType
	 *            下级节点的类型（模块或页）
	 * @throws Exception
	 */
	public void updateModuleCondition(String id, String childType) throws Exception;

	/**
	 * 更新课程的通过条件--实现代码已注销，页面不需要在显示所以也不再更新-lzp 2012-3-29
	 * 
	 * @param id
	 *            课程id
	 * @throws Exception
	 */
	public void updateCourseCondition(String id) throws Exception;

	/**
	 * 更新页的通过条件--实现代码已注销，页面不需要在显示所以也不再更新-lzp 2012-3-29
	 * 
	 * @param id
	 *            页id
	 * @throws Exception
	 */
	public void updateSectionCondition(String id, String studyTime) throws Exception;

	/**
	 * 根据站点ID所有的作业和前测的ID与名称
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Object> getTestsAndSelfTestsBySiteId(String siteId) throws Exception;

	/**
	 * 根据 作业和前测的ID 批改状态 截止时间 查询要批改的试卷
	 * 
	 * @param testId
	 *            作业和前测的ID
	 * @param checkStatus
	 *            批改状态
	 * @param endTime
	 *            截止时间
	 * @param testType
	 *            试卷类型 1 为作业 2 为前测
	 * @return
	 * @throws Exception
	 */
	public Object[] findCheckWorkList(String testId, String checkStatus, String endTime, String testType, int start)
			throws Exception;

	/**
	 * 通过学习记录ID 和 页节点ID 获得节点记录
	 * 
	 * @param studyRecordId
	 * @param sectionId
	 * @return
	 * @throws Exception
	 */
	public MeleteSectionRecordModel findSectionRecord(Long studyRecordId, String sectionId) throws Exception;

	/**
	 * 通过页ID 获得模块
	 * 
	 * @param sectionId
	 *            页ID
	 * @return
	 * @throws Exception
	 */
	public MeleteModuleModel getModuleBySectionId(String sectionId) throws Exception;

	/**
	 * 通过页ID 获得页
	 * 
	 * @param sectionId
	 *            页ID
	 * @return
	 * @throws Exception
	 */
	public MeleteSectionModel getSectionById(String sectionId) throws Exception;

	/**
	 * 根据课程id获取该课程所有的节点
	 * 
	 * @param courseId
	 * @return
	 * @throws Exception
	 */
	public List<MeleteModuleModel> getAllModule(String courseId) throws Exception;

	public List<MeleteSectionModel> getAllSection(String courseId) throws Exception;

	public List<MeleteTestModel> getAllTest(String courseId) throws Exception;
	
	/**
	 * 获得旧课程空间的作业
	 */
	public List<MeleteTestModel> getAllTestByOldMelete(String courseId) throws Exception;
	
	/**
	 * 得到test
	 * @param testId
	 * @return
	 * @throws Exception
	 */
	public MeleteTestModel getTest(Long testId) throws Exception;

	public List<MeleteForumModel> getAllForum(String courseId) throws Exception;

	public List<MeleteSelfTestModel> getAllSelfTest(String courseId) throws Exception;

	/**
	 * 根据条件查询一门课程下要增加印象分的学生相关信息
	 * 
	 * @param eduCenterId
	 * @param stuName
	 * @param stuNum
	 * @param courseId
	 * @param start
	 * @return
	 * @throws Exception
	 */
	public Object[] findImpScoreStudents(String eduCenterId, String stuName, String stuNum, String courseId, int start)
			throws Exception;

	/**
	 * 获得课程目录下的课件描述文件imsmanifest.xml的document对象
	 * 
	 * @param coursePath
	 *            课程目录
	 */
	public Document getXmlDocument(String coursePath) throws Exception;

	/**
	 * 将添加的页放入课程清单文件的中
	 * 
	 * @param fileName
	 * @param servicePath
	 * @param moduleId
	 * @param courseId
	 * @param section
	 * @throws Exception
	 */
	public void addSectionToManifest(String fileName, String servicePath, String moduleId, String courseId,
			MeleteSectionModel section) throws Exception;

	/**
	 * 将处理过的课件目录的课件描述文件的document对象写入到课程目录中的imsmanifest.xml文件
	 * 
	 * @param coursePath
	 * @param mDocument
	 * @param tempOrg
	 * @param resourcesNode
	 * @param pIdAndImportItemId
	 *            针对父节点为模块的情况要导入的父节点ID与要导入的模块item元素的identifier属性值用";"隔开
	 * @param parentType
	 *            父节点类型 课程 模块
	 * @throws Exception
	 */
	public void toCourseManifest(String coursePath, Document mDocument, Node tempOrg, Node resourcesNode,
			String parentType, String pIdAndImportItemId) throws Exception;

	/**
	 * 将节点添加到课程根节点中
	 * 
	 * @param itemId
	 *            添加的模块在课件描述文件中的item元素中的identifier属性值
	 * @param courseId
	 *            课程Id
	 * @param module
	 *            添加的模块module
	 */
	public void addModuleToCourse(String itemId, String courseId, MeleteModuleModel module) throws Exception;

	/**
	 * 将module添加到课件描述文件中的父module中
	 * 
	 * @param itemId
	 * @param pModuleId
	 * @param module
	 */
	public void addModuleToPModule(String itemId, MeleteModuleModel pModule, MeleteModuleModel module) throws Exception;

	/**
	 * 预处理课件资源库中的课件描述文件文档对象就是将原有的item和rescource元素中的identifier属性值替换为新生成的值
	 * 
	 * @param cwareDocument
	 */
	public void preProcess(Document cwareDocument) throws Exception;
	
	/**
	 * 检查课件包结构是否符合要求
	 * 
	 * @param cwareDocument
	 */
	public boolean checkDocument(Document cwareDocument) throws Exception;

	/**
	 * 将对应的document对象写入到对应的课件描述文件中
	 * 
	 * @param imsmanifestFile
	 * @param courseDocument
	 * @throws Exception
	 */
	public void writeManifest(File imsmanifestFile, Document courseDocument) throws Exception; 

	/**
	 * 移除identifier属性值包含或者不包含(具体由isResourceIdListContain参数确定)
	 * 在resourceIdList中的resources中的元素
	 * 
	 * @param resources 
	 *            resource元素集合
	 * @param resourceIdList
	 *            存放resource元素的identifier属性值的集合
	 * @param isResourceIdListContain
	 *            若值为true则移除identifier属性值包含在resourceIdList中的resource元素,
	 *            若为false则移除不包含在resourceIdList中的resource元素
	 */
	public void removeResource(Vector resources, List<String> resourceIdList, boolean isResourceIdListContain);

	/**
	 * 根据courseId, itemId 删除对应的课程的课件描述文件中的item以及对应的resource元素
	 * 
	 * @param moduleId
	 * @throws Exception
	 */
	public void delItem(String courseId, String itemId) throws Exception;

	/**
	 * 打包课程目录下课件资源
	 * 
	 * @param courseId
	 *            课程ID
	 * @param exportFileName
	 *            导出课件文件名
	 * @return 打包完成的课件包文件
	 */
	public void packCourseWare(String courseId, String exportFileName) throws Exception;

	/**
	 * 删除指定目录中 最后修改日期距当前时间一天前的所有文件
	 * 
	 * @param export
	 *            要删除的文件所在目录
	 */
	public void deleteFormerFile(String dir) throws Exception;

	/**
	 * 删除模块记录
	 * 
	 * @param moduleId
	 * @throws Exception
	 */
	public void delModule(Long moduleId) throws Exception;

	/**
	 * 删除页操作
	 * 
	 * @param sectionId
	 */
	public void delSection(Long sectionId) throws Exception;

	/**
	 * 根据活动类型和id删除对应活动 (作业 前测 论坛)
	 * 
	 * @param clazz
	 * @param id
	 */
	public void delActivity(Class clazz, Long id) throws Exception;

	/**
	 * 
	 * 根据课程id得到所有课程下的作业（MeleteTestModel）
	 * 
	 * @author zihongyan 2013-8-16
	 * @param courseId
	 * @return
	 */
	public List<MeleteTestModel> getAllMeleteTestModelByCourseId(String courseId);

	/**
	 * 根据课程id得到所有课程下的论坛帖子(MeleteForumModel)
	 * 
	 * @author zihongyan 2013-8-22
	 * @param courseId
	 * @param modelId
	 * @return
	 */
	public List<MeleteForumModel> getAllMeleteForumModelByCourseId(String courseId,Long modelId);

	/**
	 * 根据记录id得到所有的尝试记录id
	 * 
	 * @author zihongyan 2013-8-25
	 * @param testRecordId
	 * @return
	 */
	public List<MeleteTestAttemptModel> getAllMeleteTestAttemptModelByTestRecordId(String testRecordId);

	/**
	 * 获取课程学习记录
	 * @param courseId
	 * @return
	 */
	public List<Long> getStudyrecordidListByCourseId(String courseId);
	
	/**
	 * 根据业务对象获取课程id
	 */
	public String getCourseIdFromModel(Object obj);
	
	/**
	 * 获取使用指定模板的课程数
	 * @param templateName
	 * @return
	 */
	public Integer countCourseUseTemplate(String templateName) throws Exception;
	
	/**
	 * 生成课件预览文件
	 * @param wareId
	 * @return
	 * @throws Exception
	 */
	public List<Object> previewScorm(String wareId) throws Exception;
	
	/**
	 * 根据id得到作业对象
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public MeleteTestModel getMeleteTestModelById(Long id) throws Exception;
}
