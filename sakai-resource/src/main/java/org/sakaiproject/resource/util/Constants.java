package org.sakaiproject.resource.util;

/**
 * 
 * 系统常用常量。
 * 
 * @author Administrator
 * 
 */
public class Constants {

	/** 缓存名称 */
	public static final String CACHE_NAME = ConstantsSupport.getInstance().get("sakai.resource.cache.name");

	/** 上传文件存储路径 */
	public static final String FILE_PATH = ConstantsSupport.getInstance().get("sakai.resource.path.mount")
			+ ConstantsSupport.getInstance().get("sakai.resource.file.path");
	/** 上传文件存储uri */
	public static final String FILE_URI = ConstantsSupport.getInstance().get("sakai.resource.context.static")
			+ ConstantsSupport.getInstance().get("sakai.resource.file.uri");
	/** 课件压缩包存储路径 */
	public static final String COURSE_RESOURCE_PACK_PATH = ConstantsSupport.getInstance().get(
			"sakai.resource.course.resource.pack.path");

	/** 上传课程资源文件类型 */
	public static final String COURSE_TYPE = ConstantsSupport.getInstance().get("sakai.resource.course.fileType");

	/** 上传课程资源文件大小 */
	public static final String COURSE_SIZE = ConstantsSupport.getInstance().get("sakai.resource.course.fileSize");

	/** 上传文件类型 */
	public static final String FILE_TYPE = ConstantsSupport.getInstance().get("sakai.resource.file.fileType");

	/** 上传文件大小 */
	public static final String FILE_SIZE = ConstantsSupport.getInstance().get("sakai.resource.file.fileSize");

	/** 试卷库的试卷数量 **/
	public static final String operateCount = ConstantsSupport.getInstance().get("sakai.resource.operateCount");

	/** 课程课件存储路径 */
	public static final String SECTION_PATH = ConstantsSupport.getInstance().get("sakai.resource.section.path");

	public static final String SECTION_URL = ConstantsSupport.getInstance().get("sakai.resource.context.static")
			+ ConstantsSupport.getInstance().get("sakai.resource.section.url.prefix");
	
	/**保存课件预览的路径*/
	public static final String COURSEWARE_PATH = ConstantsSupport.getInstance().get("sakai.resource.path.mount")
			+ ConstantsSupport.getInstance().get("sakai.resource.courseware.preview.path");
	
	/**查看课件的预览的路径*/
	public static final String COURSEWARE_URL = ConstantsSupport.getInstance().get("sakai.resource.context.static")
			+ ConstantsSupport.getInstance().get("sakai.resource.courseware.preview.uri");

	/** 学生学习时向服务器提交的时间间隔 */
	public static final String SUBMIT_INTERVAL = ConstantsSupport.getInstance().get("sakai.resource.submit.interval");
	/** 学生学习时弹出是否继续学习提示框的时间间隔 */
	public static final String PROMPT_INTERVAL = ConstantsSupport.getInstance().get("sakai.resource.prompt.interval");
	/** 弹出提示框后从没有操作到跳出学习页面的时间间隔 */
	public static final String SKIP_INTERVAL = ConstantsSupport.getInstance().get("sakai.resource.skip.interval");

	// 页面上展示通过状态时
	public static final String PASS_VIEW = ConstantsSupport.getInstance().get("sakai.resource.pass.view");

	// 页面上展示未通过状态时
	public static final String NOTPASS_VIEW = ConstantsSupport.getInstance().get("sakai.resource.notPass.view");
	/**
	 * 作业提交最小间隔时间-默认 300s(5分钟)
	 */
	public static final String TEST_MININTERVAL = ConstantsSupport.getInstance().get("sakai.resource.test.mininterval");

	/** 课件导出路径 */
	public static final String COURSEWARE_EXPORT_PATH = ConstantsSupport.getInstance().get("sakai.resource.path.mount")
			+ ConstantsSupport.getInstance().get("sakai.resource.courseware.export.path");
	public static final String COURSEWARE_EXPORT_URI = ConstantsSupport.getInstance().get(
			"sakai.resource.context.static")
			+ ConstantsSupport.getInstance().get("sakai.resource.courseware.export.uri");

	/** 课件引用资源存储路径 */
	public static final String COURSEWARE_RESOURCE_PATH = ConstantsSupport.getInstance().get(
			"sakai.resource.path.mount")
			+ ConstantsSupport.getInstance().get("sakai.resource.courseware.resource.path");
	/** 课件引用资源URI */
	public static final String COURSEWARE_RESOURCE_URI = ConstantsSupport.getInstance().get(
			"sakai.resource.context.static")
			+ ConstantsSupport.getInstance().get("sakai.resource.courseware.resource.uri");
	/** 课件引用资源中图片资源存储的目录 */
	public static final String COURSEWARE_RESOURCE_IMAGE_FOLDER = ConstantsSupport.getInstance().get(
			"sakai.resource.courseware.resource.image.folder");
	/** 课件引用资源中图片文件后缀名 */
	public static final String COURSEWARE_RESOURCE_IMAGE_EXT = ConstantsSupport.getInstance().get(
			"sakai.resource.courseware.resource.image.ext");
	/** 课件引用资源中flash资源存储的目录 */
	public static final String COURSEWARE_RESOURCE_FLASH_FOLDER = ConstantsSupport.getInstance().get(
			"sakai.resource.courseware.resource.flash.folder");
	/** 课件引用资源中flash资源文件后缀 */
	public static final String COURSEWARE_RESOURCE_FLASH_EXT = ConstantsSupport.getInstance().get(
			"sakai.resource.courseware.resource.flash.ext");
	/** 课件引用资源中其他资源存储的目录 */
	public static final String COURSEWARE_RESOURCE_FILE_FOLDER = ConstantsSupport.getInstance().get(
			"sakai.resource.courseware.resource.file.folder");
	/** 课程路径URI */
	public static final String COURSE_PATH_URI = ConstantsSupport.getInstance().get("sakai.resource.context.static")
			+ ConstantsSupport.getInstance().get("sakai.resource.course.path.uri");
	/** 共享路径 */
	public static final String WEB_PATH = ConstantsSupport.getInstance().get("sakai.resource.path.mount")
			+ ConstantsSupport.getInstance().get("sakai.resource.web.path");
	/** 安全路径 */
	public static final String SECURE_PATH = ConstantsSupport.getInstance().get("sakai.resource.path.mount")
			+ ConstantsSupport.getInstance().get("sakai.resource.secure.path");;

	/********** 功能开关 **********/
	/**
	 * 功能开关：记录学习位置
	 */
	public static final Boolean FUNC_SWITCH_STUDYHISTORY = Boolean.parseBoolean(ConstantsSupport.getInstance().get(
			"sakai.resource.func.switch.studyhistory"));

	/******* 模板 ************/
	/**
	 * 模板：访问路径
	 */
	public static final String TEMPLATE_PATH_URI = ConstantsSupport.getInstance().get("sakai.resource.context.static")
			+ ConstantsSupport.getInstance().get("sakai.resource.template.path.uri");
	/**
	 * 模板：访问路径-相对
	 */
	public static final String TEMPLATE_PATH_URI_RELATIVE = ConstantsSupport.getInstance().get(
			"sakai.resource.context.static")
			+ ConstantsSupport.getInstance().get("sakai.resource.template.path.uri.relative");
	/**
	 * 模板：保存物理路径
	 */
	public static final String TEMPLATE_PATH_DIR = ConstantsSupport.getInstance().get("sakai.resource.path.mount")
			+ ConstantsSupport.getInstance().get("sakai.resource.template.path.dir");

	/****** 外部系统集成 *******/
	/**
	 * 题库：访问key
	 */
	public static final String OUTSYS_EXAM_KEY_ENTRY = ConstantsSupport.getInstance().get(
			"sakai.resource.outSys.exam.key.entry");
	/**
	 * 题库：指定用户
	 */
	public static final String OUTSYS_EXAM_USER = ConstantsSupport.getInstance().get("sakai.resource.outSys.exam.user");
	/**
	 * 题库：路径
	 */
	public static final String OUTSYS_EXAM_URL_BASE = ConstantsSupport.getInstance().get(
			"sakai.resource.outSys.exam.url.base");
	/**
	 * 题库：课程列表接口
	 */
	public static final String OUTSYS_EXAM_URL_COURSE = ConstantsSupport.getInstance().get(
			"sakai.resource.outSys.exam.url.course");
	/**
	 * 题库：策略列表接口
	 */
	public static final String OUTSYS_EXAM_URL_SCHEMA = ConstantsSupport.getInstance().get(
			"sakai.resource.outSys.exam.url.schema");
	/**
	 * 题库：策略预览
	 */
	public static final String OUTSYS_EXAM_URL_SCHEMA_VIEW = ConstantsSupport.getInstance().get(
			"sakai.resource.outSys.exam.url.schema.view");
	/**
	 * 题库：试卷生成接口
	 */
	public static final String OUTSYS_EXAM_URL_PAPER = ConstantsSupport.getInstance().get(
			"sakai.resource.outSys.exam.url.paper");

	/**
	 * 题库：登录跳转
	 */
	public static final String OUTSYS_EXAM_URL_ENTRY = ConstantsSupport.getInstance().get(
			"sakai.resource.outSys.exam.url.entry");

	/**
	 * 资源：访问识别码
	 */
	public static final String OUTSYS_RESSYS_KEY_CODE = ConstantsSupport.getInstance().get(
			"sakai.resource.outSys.resSys.key.code");
	/**
	 * 资源：集成key
	 */
	public static final String OUTSYS_RESSYS_KEY_ENTRY = ConstantsSupport.getInstance().get(
			"sakai.resource.outSys.resSys.key.entry");
	/**
	 * 资源：指定用户
	 */
	public static final String OUTSYS_RESSYS_USER = ConstantsSupport.getInstance().get(
			"sakai.resource.outSys.resSys.user");
	/**
	 * 资源：根地址
	 */
	public static final String OUTSYS_RESSYS_URL_BASE = ConstantsSupport.getInstance().get(
			"sakai.resource.outSys.resSys.url.base");
	/**
	 * 资源：课程列表
	 */
	public static final String OUTSYS_RESSYS_URL_COURSE = ConstantsSupport.getInstance().get(
			"sakai.resource.outSys.resSys.url.course");
	/**
	 * 资源：资源列表
	 */
	public static final String OUTSYS_RESSYS_URL_RESOURCE = ConstantsSupport.getInstance().get(
			"sakai.resource.outSys.resSys.url.resource");
	/**
	 * 资源：登录跳转
	 */	
	public static final String OUTSYS_RESSYS_URL_ENTRY = ConstantsSupport.getInstance().get(
			"sakai.resource.outSys.resSys.url.entry");
	
	/**
	 * 作业文件
	 * 
	 * @return 资源文件保存路径(绝对)，需要做web 的虚拟目录
	 */
	public static String getTestPath() {
		return ConstantsSupport.getInstance().get("sakai.resource.path.mount")
				+ ConstantsSupport.getInstance().get("sakai.resource.test.savepath");
	}

	/**
	 * 学生的作业文件保存路径
	 */
	public static String getStuTestPath(String stuId, String courseId) {
		return getTestPath() + stuId + "/" + courseId + "/";
	}

	/**
	 * 根据作业ID返回作业试卷库文件的保存路径
	 * 
	 * @param testId
	 *            作业ID
	 */
	public static String getTestLibPath(String courseId, String testId) {
		return getTestPath() + "paperlib" + "/" + courseId + "/" + testId + "/";
	}

	/**
	 * 根据作业ID返回作业试卷库答案文件的保存路径
	 * 
	 * @param testId
	 *            作业ID
	 */
	public static String getTestLibAnswerPath(String courseId, String testId) {
		return getTestPath() + "paperlib" + "/" + courseId + "/" + testId + "_answer/";
	}

	/**
	 * 作业资源文件(图片)
	 * 
	 * @return 资源文件路径(绝对)，需要做web 的虚拟目录
	 */
	public static String getTestMaterialPath(String courseId) {
		return getTestMaterialPath() + courseId + "/";
	}

	public static String getTestMaterialPath() {
		return ConstantsSupport.getInstance().get("sakai.resource.path.mount")
				+ ConstantsSupport.getInstance().get("sakai.resource.material.SavePath.test");
	}

	/**
	 * 作业资源文件的URL路径
	 * 
	 * @return String
	 */
	public static String getTestMaterialURL(String courseId) {
		return getTestMaterialURL() + courseId + "/";
	}

	public static String getTestMaterialURL() {
		return ConstantsSupport.getInstance().get("sakai.resource.context.static")
				+ ConstantsSupport.getInstance().get("sakai.resource.material.UrlPath.test");
	}

	/**
	 * 前测文件
	 * 
	 * @return 资源文件保存路径(绝对)，需要做web 的虚拟目录
	 */
	public static String getSelfTestPath() {
		return ConstantsSupport.getInstance().get("sakai.resource.path.mount")
				+ ConstantsSupport.getInstance().get("sakai.resource.selftest.savepath");
	}

	/**
	 * 学生的前测文件保存路径
	 */
	public static String getStuSelfTestPath(String stuId, String courseId) {
		return getSelfTestPath() + stuId + "/" + courseId + "/";
	}

	/**
	 * 根据前测ID返回前测试卷库文件的保存路径
	 * 
	 * @param testId
	 *            作业ID
	 */
	public static String getSelfTestLibPath(String courseId, String testId) {
		return getSelfTestPath() + "paperlib" + "/" + courseId + "/" + testId + "/";
	}

	/**
	 * 根据前测ID返回前测试卷库答案文件的保存路径
	 * 
	 * @param testId
	 *            作业ID
	 */
	public static String getSelfTestLibAnswerPath(String courseId, String testId) {
		return getSelfTestPath() + "paperlib" + "/" + courseId + "/" + testId + "_answer/";
	}

	/**
	 * 前测资源文件(图片)
	 * 
	 * @return 资源文件路径(绝对)，需要做web 的虚拟目录
	 */
	public static String getSelfTestMaterialPath(String courseId) {
		return getSelfTestMaterialPath() + courseId + "/";
	}

	private static String getSelfTestMaterialPath() {
		return ConstantsSupport.getInstance().get("sakai.resource.path.mount")
				+ ConstantsSupport.getInstance().get("sakai.resource.material.SavePath.selftest");
	}

	/**
	 * 前测资源文件的URL路径
	 * 
	 * @return String
	 */
	public static String getSelfTestMaterialURL(String courseId) {
		return getSelfTestMaterialURL() + courseId + "/";
	}

	private static String getSelfTestMaterialURL() {
		return ConstantsSupport.getInstance().get("sakai.resource.context.static")
				+ ConstantsSupport.getInstance().get("sakai.resource.material.UrlPath.selftest");
	}

}
