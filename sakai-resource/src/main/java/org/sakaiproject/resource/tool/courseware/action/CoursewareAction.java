package org.sakaiproject.resource.tool.courseware.action;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.sakaiproject.resource.api.course.service.ICourseService;
import org.sakaiproject.resource.api.courseware.model.CoursewareModel;
import org.sakaiproject.resource.api.courseware.service.CoursewareService;
import org.sakaiproject.resource.util.CodeTable;
import org.sakaiproject.resource.util.Constants;
import org.sakaiproject.resource.util.FunctionRegister;
import org.sakaiproject.resource.util.JsonBuilder;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.tool.api.ToolManager;
import org.sakaiproject.user.api.UserDirectoryService;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 课程资源管理
 * 
 * @author lzp
 * 
 */
public class CoursewareAction extends ActionSupport implements ModelDriven<CoursewareModel> {

	private static final long serialVersionUID = -8550171659592450291L;
	private static Log logger = LogFactory.getLog(CoursewareAction.class);

	private SecurityService securityService = (SecurityService) ComponentManager.get(SecurityService.class);
	private SiteService siteService = (SiteService) ComponentManager.get(SiteService.class);
	private ToolManager toolManager = (ToolManager) ComponentManager.get(ToolManager.class);

	/**
	 * 分页查询开始下标
	 */
	private String start;

	/**
	 * 课件资源ID
	 */
	private String id;
	/**
	 * 课件资源状态
	 */
	private String status;

	/**
	 * 根据课件资源名称查询的参数
	 */
	private String courseName;

	/**
	 * 业务逻辑层对象
	 */
	private CoursewareService coursewareService;

	/**
	 * 实体类对象
	 */
	private CoursewareModel coursewareModel;
	
	private ICourseService courseService;

	/**
	 * 课程摘要
	 */
	private String summary;

	/**
	 * 文件上传对象，用SWFUpload上传控件后台就必须这么命名
	 */
	private File Filedata;

	/** 上传的附件文件 名 */
	private String FiledataFileName;

	/**
	 * 课件资源ID集合，当批量操作时把ID封装到这个数组里传到后台
	 */
	private String[] selectionIds;

	/**
	 * 标识方法里是否出现异常
	 */
	private boolean exception = false;

	/**
	 * 返回到前台的响应
	 */
	private String jsonMsg = "";

	/**
	 * 每页显示条数
	 */
	private int pageSize = 0;

	/** 播放地址 */
	private String play_url;
	
	/**
	 * 课件资源id
	 */
	private String wareId;

	/** sakai业务逻辑类 */
	private UserDirectoryService userDirectoryService = (UserDirectoryService) ComponentManager
			.get(UserDirectoryService.class);

	/**
	 * 保存导入的资源
	 * 
	 * @author lzp
	 * @throws Exception
	 */
	public String save() throws Exception {
		setCharacterEncoding();
		// 返回到页面的信息串
		if (this.analysisScorm()) {
			Date date = new Date();
			try {
				// 如果ID为空则是新增，如果不为空则是修改
				if (StringUtils.isBlank(id)) {
					// 创建一个新的课件资源对象
					coursewareModel = new CoursewareModel();
					// 给课件资源名称赋值
					coursewareModel.setCourseName(courseName);
					// 课件资源摘要赋值
					coursewareModel.setSummary(summary);
					coursewareModel.setPlay_url(play_url);
					// 课件资源创建(也就是上传)时间赋值
					coursewareModel.setCreateTime(new java.sql.Timestamp(System.currentTimeMillis()));
					// 课件资源修改时间赋值
					coursewareModel.setUpdateTime(new java.sql.Timestamp(System.currentTimeMillis()));
					// 创建课件资源的用户赋值
					coursewareModel.setCreater(getCurrentUserName());
					// 给修改课件资源的用户赋值，第一次默认和创建者一样
					coursewareModel.setModifier(getCurrentUserName());
					// 课件资源状态赋值，1是正常
					coursewareModel.setStatus(new Long(1));
					// 课件资源文件大小赋值，因为数据库限制非空，所以先给个0然后等上传完课件资源后在修改
					coursewareModel.setFileSize(new Float(0));
					// 课件资源地址，因为数据库限制非空，所以先给个值然后等上传完课件资源后在修改
					coursewareModel.setFileUrl(Constants.COURSE_RESOURCE_PACK_PATH);
					// 保存课件资源
					coursewareService.save(coursewareModel);
					// 如果课程资源ID不为空并且上传的文件不为空就调上传的方法
					if (StringUtils.isNotBlank(coursewareModel.getId())
							&& StringUtils.isNotBlank(getFiledataFileName())) {
						// 课件资源地址赋值，地址是配置文件的上传地址加上当前对象的ID
						coursewareModel.setFileUrl(Constants.COURSE_RESOURCE_PACK_PATH + "/"
								+ getUploadName(coursewareModel.getId()));
						// 课件资源文件大小
						coursewareModel.setFileSize(Float.parseFloat(upload(Constants.COURSE_RESOURCE_PACK_PATH,
								getUploadName(coursewareModel.getId()), true)));
						coursewareService.save(coursewareModel);
					}
				} else {
					coursewareModel = coursewareService.getCoursewareModelById(id);
					coursewareModel.setCourseName(courseName);
					coursewareModel.setSummary(summary);
					coursewareModel.setPlay_url(play_url);
					coursewareModel.setUpdateTime(new java.sql.Timestamp(System.currentTimeMillis()));
					coursewareModel.setModifier(getCurrentUserName());
					if (StringUtils.isNotBlank(getFiledataFileName())) {
						coursewareModel.setFileUrl(Constants.COURSE_RESOURCE_PACK_PATH + "/"
								+ getUploadName(coursewareModel.getId()));
						coursewareModel.setFileSize(Float.parseFloat(upload(Constants.COURSE_RESOURCE_PACK_PATH,
								getUploadName(coursewareModel.getId()), false)));
					}
					coursewareService.save(coursewareModel);
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				exception = true;
				jsonMsg = "{\"success\":false, \"result\":true,\"msg\":\"" + e.getMessage() + "\"}";
			}
		}
		if (!exception) {
			jsonMsg = "{\"success\":true, \"result\":true,\"msg\":\"上传成功！\"}";
		}
		getResponse().getWriter().print(jsonMsg);
		return null;
	}

	/**
	 * 获得重命名后的资源名
	 * 
	 * @param modelId
	 * @return
	 */
	private String getUploadName(String modelId) {
		return modelId + getExtention(getFiledataFileName());
	}

	/**
	 * 上传文件的方法
	 * 
	 * @param saveDirectory
	 *            保存路径
	 * @param uploadName
	 *            保存的文件名
	 * @param isAdd
	 *            看是添加操作调的这个方法还是修改操作调的这个方法
	 * @return
	 * @throws Exception
	 */
	private String upload(String saveDirectory, String uploadName, boolean isAdd) throws Exception {
		// 定义文件上传路径，之所以定义到外面是为了在上传失败后能在catch里使用这个变量
		String fileBasePath = "";
		String extractDir = "";
		try {
			String securePath = Constants.SECURE_PATH;
			String fileSeparator = "/";
			fileBasePath = new File(securePath) + fileSeparator + saveDirectory + fileSeparator;
			File file = new File(fileBasePath);
			if (!file.exists()) {
				file.mkdir();
			}
			File attachmentFile = new File(fileBasePath, uploadName);
			FileUtils.copyFile(getFiledata(), attachmentFile);

			// 解压缩
			extractDir = fileBasePath + uploadName.substring(0, uploadName.lastIndexOf(".")) + fileSeparator;
			UnZipHandler uzh = new UnZipHandler(attachmentFile.getPath(), extractDir);
			boolean mZipExtractionResult = uzh.extract();
			logger.debug("---------开始scorm校验--------");
			/* scorm标准校验 start */
			if (mZipExtractionResult) {
				String manifestFile = extractDir + "imsmanifest.xml";
				
				if (!new File(manifestFile).exists()) {//解压路径下课件描述文件是否存在
					logger.info("zip包中课件描述文件："+manifestFile+"  不存在");
					throw new Exception("上传zip包不为scorm课件包");
				}
				logger.debug("---课件描述文件位置----" + manifestFile);
				ADLDOMParser domParser = new ADLDOMParser();
				boolean success = domParser.createDocument(manifestFile, true, false);
				if (success) {
					Document mDocument = domParser.getDocument();
					Node mManifest = mDocument.getDocumentElement();

					Node resourcesNode = DOMTreeUtility.getNode(mManifest, "resources");
					Vector resources = DOMTreeUtility.getNodes(resourcesNode, "resource");

					Vector mOrganizationList = ManifestHandler.getOrganizationNodes(mManifest, false);
					Node tempOrg = (Node) mOrganizationList.elementAt(0);
					Vector moduleList = DOMTreeUtility.getNodes(tempOrg, "item");// 一级模块集合
					for (int j = 0; j < moduleList.size(); j++) {
						Node moduleNode = (Node) moduleList.elementAt(j);
						String ref = DOMTreeUtility.getAttributeValue(moduleNode, "identifierref");
						if (ref != null && !ref.equals("")) {
							throw new Exception("scorm包校验失败，课程不能直接添加页");
						} else {
							mZipExtractionResult = validateNode(moduleNode, resources);
						}
						if (!mZipExtractionResult) {
							break;
						}
					}
				} else {
					throw new Exception("imsmanifest.xml文件解析失败");
				}
			} else {
				throw new Exception("scorm包解压缩失败");
			}
			logger.debug("---------scorm校验结束--------");
			/* scorm标准校验 end */

			DecimalFormat decimalFormat = new DecimalFormat("#.00");
			String ft = decimalFormat.format((double) getFiledata().length() / 1024 / 1024);
			return ft;
		} catch (Exception e) {
			if (isAdd) {
				String path = fileBasePath + "/" + uploadName;
				File file = new File(path);
				if (file.exists()){
					file.delete();
					logger.debug("删除上传的zip文件："+file.getAbsolutePath());
				}
				if (!"".equals(extractDir)) {
					FileUtils.deleteDirectory(new File(extractDir));	//删除解压目录
					logger.debug("删除解压目录："+extractDir);
				}
				String[] ids = new String[1];
				ids[0] = uploadName.substring(0, uploadName.indexOf("."));
				coursewareService.remove(ids);
			}
			throw e;
		}
	}
	

	private boolean validateNode(Node moduleNode, Vector resources) throws Exception {
		boolean validateResult = true;
		Vector childList = DOMTreeUtility.getNodes(moduleNode, "item");
		String lastNodeType = null;
		for (int j = 0; j < childList.size(); j++) {
			Node childNode = (Node) childList.elementAt(j);
			String ref = DOMTreeUtility.getAttributeValue(childNode, "identifierref");
			String nodeType = "";
			if (ref != null && !ref.equals("")) {
				nodeType = CodeTable.section;
			} else {
				nodeType = CodeTable.module;
			}
			if (lastNodeType != null && !lastNodeType.equals(nodeType)) {
				logger.debug("scorm包校验失败，同一级不能既有节点又有页");
				throw new Exception("scorm包校验失败，同一级不能既有节点又有页");
			} else if (nodeType.equals(CodeTable.module)) {
				validateResult = validateNode(childNode, resources);
			} else if (nodeType.equals(CodeTable.section)) {
				boolean sectionReslut = false;
				for (int i = 0; i < resources.size(); i++) { // 遍历资源标签
					Node resourceNode = (Node) resources.elementAt(i); // 获取资源节点
					String resourceID = DOMTreeUtility.getAttributeValue(resourceNode, "identifier");
					if (resourceID.equals(ref)) {
						Vector fileList = DOMTreeUtility.getNodes(resourceNode, "file");// 获取file集合
						if (fileList.isEmpty()) {
							logger.debug("scorm包校验失败，页中必须包含file标签");
							throw new Exception("scorm包校验失败，页中必须包含file标签");
						}
						break;
					}
				}
				validateResult = sectionReslut;
			}
			if (!validateResult) {
				break;
			}
			lastNodeType = nodeType;
		}
		return validateResult;
	}

	/**
	 * 导出课程资源
	 * 
	 * @author lzp
	 * @throws Exception
	 */
	public String export() throws Exception {
		// 删除一星期前的压缩包
		Long nowTime = new Date().getTime();
		long second = 1000;
		long weekTime = second * 60 * 60 * 24 * 7;
		String path = Constants.COURSEWARE_EXPORT_PATH;
		File file = new File(path);
		if (file.isDirectory()) {
			File[] tempList = file.listFiles();
			if (tempList != null && tempList.length > 0) {
				for (File f : tempList) {
					if (f.isFile()) {
						String fileName = f.getName().substring(0, f.getName().indexOf("."));
						Boolean b = fileName.matches("\\d+");
						if (b) {
							Long fTime = Long.parseLong(fileName);
							if ((nowTime - fTime) > weekTime) {
								FileUtils.deleteQuietly(f);
							}
						}
					}
				}
			}
		}
		coursewareModel = coursewareService.getCoursewareModelById(id);
		// 课件资源压缩包路径
		File srcFile = new File(Constants.SECURE_PATH + "/" + coursewareModel.getFileUrl());
		// 判断文件是否存在和是否是文件
		if (srcFile.exists() && srcFile.isFile()) {
			// 下载文件名称
			String destFileName = nowTime + ".zip";
			// 下载路径
			File destFile = new File(Constants.COURSEWARE_EXPORT_PATH, destFileName);
			FileUtils.copyFile(srcFile, destFile);
			String fileUrl = Constants.COURSEWARE_EXPORT_URI + destFileName;
			jsonMsg = "{\"success\":true, \"result\":true,\"msg\":\"" + fileUrl + "\"}";
		} else {
			jsonMsg = "{\"success\":true, \"result\":false,\"msg\":\"资源不存在！\"}";
		}
		getResponse().getWriter().print(jsonMsg);
		return null;
	}
	
	
	/**
	 * 生成课件预览文件
	 * @return
	 * @throws Exception
	 */
	public String createPreView() throws Exception {
		try {
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
			courseService.previewScorm(wareId);
				
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().println("{}");
		return null;
	}
	
	/**生成课程预览数*/
	public String initPreviewTree() throws Exception {
		StringBuffer sb = new StringBuffer("");
		try {
			// String webPath = Constants.WEB_PATH;
			String path =Constants.COURSEWARE_PATH + "/" + wareId + "/"
					+ "imsmanifest.xml";

			ADLDOMParser adldomparser = new ADLDOMParser();
			adldomparser.createDocument(path, true, false);
			Document mDocument = adldomparser.getDocument();
			Node mManifest = mDocument.getDocumentElement();
			Vector mOrganizationList = ManifestHandler.getOrganizationNodes(mManifest, false);
			Node orgNode = (Node) mOrganizationList.elementAt(0);
			Node resourcesNode = DOMTreeUtility.getNode(mManifest, "resources");
			Vector resources = DOMTreeUtility.getNodes(resourcesNode, "resource");
			String subString = courseService.initSectionScormPreview(wareId,orgNode,resources);
			sb.append("[" + subString + "]"); 			
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().println(sb.toString());
		return null;
	}

	/**
	 * 这个方法是把对象的状态标记为已删除,放入回收站
	 * 
	 * @author lzp
	 * @throws Exception
	 */
	public String delete() throws Exception {
		setCharacterEncoding();
		if (selectionIds != null && selectionIds.length != 0) {
			try {
				coursewareService.delete(selectionIds);
			} catch (Exception e) {
				logger.debug(e.getStackTrace());
				exception = true;
				jsonMsg = "{\"success\":false, \"result\":true,\"msg\":\"删除失败，原因如下" + e.getMessage() + "\"}";
				throw e;
			}
		} else {
			logger.debug("删除失败，参数为空！");
			exception = true;
			jsonMsg = "{\"success\":false, \"result\":true,\"msg\":\"删除失败，参数为空！\"}";
		}
		if (!exception)
			jsonMsg = "{\"success\":true, \"result\":true,\"msg\":\"删除成功！\"}";
		getResponse().getWriter().print(jsonMsg);
		return null;
	}

	/**
	 * 这个方法是把对象从数据库删除
	 * 
	 * @author lzp
	 * @throws Exception
	 */
	public String remove() throws Exception {
		setCharacterEncoding();
		if (selectionIds != null && selectionIds.length != 0) {
			try {
				coursewareService.remove(selectionIds);
			} catch (Exception e) {
				logger.debug(e.getStackTrace());
				exception = true;
				jsonMsg = "{\"success\":false, \"result\":true,\"msg\":\"彻底删除失败，原因如下" + e.getMessage() + "\"}";
				throw e;
			}
		} else {
			logger.debug("删除失败，参数为空！");
			exception = true;
			jsonMsg = "{\"success\":true, \"result\":true,\"msg\":\"删除失败，参数为空！\"}";
		}
		if (!exception)
			jsonMsg = "{\"success\":true, \"result\":true,\"msg\":\"删除成功！\"}";
		getResponse().getWriter().print(jsonMsg);
		return null;
	}

	/**
	 * 清空回收站
	 * 
	 * @author lzp
	 */
	public String removeAll() throws Exception {
		try {
			setCharacterEncoding();
			coursewareService.removeAll();
		} catch (Exception e) {
			logger.debug(e.getStackTrace());
			exception = true;
			throw e;
		}
		if (!exception)
			jsonMsg = "{\"success\":true, \"result\":true,\"msg\":\"回收站已清空！\"}";
		getResponse().getWriter().print(jsonMsg);
		return null;
	}

	/**
	 * 模糊查询根据用户输入的课件资源名称或课件资源摘要
	 * 
	 * @author lzp
	 */
	public String findByName() throws Exception {
		try {
			setCharacterEncoding();
			int startInt = NumberUtils.toInt(start);
			if (startInt < 0)
				startInt = 0;
			// 定义返回数据的编码格式
			getResponse().setCharacterEncoding("UTF-8");
			// 传入的参数为课件资源名称或摘要，资源状态，分页下标
			Object[] result = coursewareService.findByName(courseName, status, startInt, pageSize);
			if (result != null) {
				logger.debug("findByName资源数据数:" + result.length);
			}
			getResponse().getWriter().println(JsonBuilder.builderGridJson((List) result[1], (Integer) result[0]));
		} catch (Exception e) {
			logger.debug(e.getStackTrace());
			exception = true;
			throw e;
		}
		return null;
	}

	/**
	 * 只是在前台跳转到目标页面没有实际操作
	 */
	public String execute() throws Exception {
		Site curSite = this.getCurrentSite();
		String siteRef = siteService.siteReference(curSite.getId());
		String currentUser = this.getCurrentUserName();
		if (securityService.unlock(currentUser, FunctionRegister.COURSEWARE_PERM_MAINTAIN, siteRef)) {// 课件管理员
			return "success";
		} else {
			addActionError("无权限");
			return ERROR;
		}
	}

	/**
	 * 查询全部数据
	 * 
	 * @author lzp
	 */
	public String query() throws Exception {
		setCharacterEncoding();
		int startInt = NumberUtils.toInt(start);
		if (startInt < 0)
			startInt = 0;
		try {
			Object[] result = coursewareService.query(status, startInt);
			if (result != null) {
				logger.debug("query资源数据数：" + result.length);
			}
			getResponse().setCharacterEncoding("UTF-8");
			getResponse().getWriter().println(JsonBuilder.builderGridJson((List) result[1], (Integer) result[0]));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			exception = true;
			throw e;
		}
		return null;
	}

	/**
	 * 还原资源
	 * 
	 * @author lzp
	 */
	public String courseBack() throws Exception {
		setCharacterEncoding();
		if (selectionIds != null && selectionIds.length != 0) {
			try {
				coursewareService.courseBack(selectionIds);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				exception = true;
				jsonMsg = "{\"success\":false, \"result\":true,\"msg\":\"还原资源失败，原因如下" + e.getMessage() + "\"}";
				throw e;
			}
		} else {
			logger.info("还原失败，参数为空！");
			jsonMsg = "{\"success\":true, \"result\":true,\"msg\":\"还原失败，参数为空！\"}";
		}
		if (!exception)
			jsonMsg = "{\"success\":true, \"result\":true,\"msg\":\"资源已还原！\"}";
		getResponse().getWriter().print(jsonMsg);
		return null;
	}

	/**
	 * 还原所有资源
	 * 
	 * @author lzp
	 */
	public String courseBackAll() throws Exception {
		setCharacterEncoding();
		try {
			coursewareService.courseBackAll();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			exception = true;
			jsonMsg = "{\"success\":false, \"result\":true,\"msg\":\"还原所有资源失败，原因如下" + e.getMessage() + "\"}";
			throw e;
		}
		if (!exception)
			jsonMsg = "{\"success\":true, \"result\":true,\"msg\":\"所有资源已全部还原！\"}";
		getResponse().getWriter().print(jsonMsg);
		return null;
	}

	/**
	 * 解析SCORM包
	 * 
	 * @author lzp
	 */
	private boolean analysisScorm() {
		return true;
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

	/**
	 * 获得当前用户名
	 * 
	 * @return
	 */
	private String getCurrentUserName() {
		return userDirectoryService.getCurrentUser().getId();
	}

	/**
	 * 获得当前站点
	 * 
	 * @return 当前站点
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
	 * 取文件扩展名
	 * 
	 * @param fileName
	 * @return 文件扩展名
	 */
	private String getExtention(String fileName) {
		int pos = fileName.lastIndexOf(".");
		return StringUtils.lowerCase(fileName.substring(pos));
	}

	/**
	 * 设置编码规则，防止乱码
	 * 
	 * @throws Exception
	 */
	private void setCharacterEncoding() throws Exception {
		getRequest().setCharacterEncoding("utf-8");
		getResponse().setCharacterEncoding("utf-8");
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public CoursewareModel getModel() {
		return coursewareModel;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public File getFiledata() {
		return Filedata;
	}

	public void setFiledata(File filedata) {
		Filedata = filedata;
	}

	public String getFiledataFileName() {
		return FiledataFileName;
	}

	public void setFiledataFileName(String filedataFileName) {
		FiledataFileName = filedataFileName;
	}

	public CoursewareService getCoursewareService() {
		return coursewareService;
	}

	public void setCoursewareService(CoursewareService coursewareService) {
		this.coursewareService = coursewareService;
	}

	public CoursewareModel getCoursewareModel() {
		return coursewareModel;
	}

	public void setCoursewareModel(CoursewareModel coursewareModel) {
		this.coursewareModel = coursewareModel;
	}

	public String[] getSelectionIds() {
		return selectionIds;
	}

	public void setSelectionIds(String[] selectionIds) {
		this.selectionIds = selectionIds;
	}

	public boolean isException() {
		return exception;
	}

	public void setException(boolean exception) {
		this.exception = exception;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getPlay_url() {
		return play_url;
	}

	public void setPlay_url(String play_url) {
		this.play_url = play_url;
	}

	public ICourseService getCourseService() {
		return courseService;
	}

	public void setCourseService(ICourseService courseService) {
		this.courseService = courseService;
	}

	public String getWareId() {
		return wareId;
	}

	public void setWareId(String wareId) {
		this.wareId = wareId;
	}
}
