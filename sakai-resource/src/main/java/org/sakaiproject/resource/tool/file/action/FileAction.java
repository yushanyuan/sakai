/**
 * 
 */
package org.sakaiproject.resource.tool.file.action;

import java.io.File;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.sakaiproject.authz.api.SecurityService;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.resource.api.course.service.ICourseService;
import org.sakaiproject.resource.api.file.model.ResourceFileModel;
import org.sakaiproject.resource.api.file.service.IFileService;
import org.sakaiproject.resource.api.study.service.IStudyService;
import org.sakaiproject.resource.util.AsyncTreeModel;
import org.sakaiproject.resource.util.CacheUtil;
import org.sakaiproject.resource.util.CodeTable;
import org.sakaiproject.resource.util.Constants;
import org.sakaiproject.resource.util.FunctionRegister;
import org.sakaiproject.resource.util.JsonBuilder;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @author zxchaos
 */
public class FileAction extends ActionSupport {

	private String node;

	private IFileService fileService;

	/** 课程id */
	private String siteId;

	/** 节点 父节点id */
	private String parentId;

	/** 节点 名称 */
	private String folderName;

	/** 文件 或 文件夹 ID */
	private String folderId;

	private File Filedata;

	private String FiledataFileName;

	private String FiledataContentType;

	/** 是否计算平时成绩 */
	private String extendOption;

	/** 点击下载的文件ID */
	private String fileId;

	/** 创建的文件标题 */
	private String fileTitle;

	/** 创建的文件内容 */
	private String fileContent;

	/** 文件类型 */
	private String fileType;

	/** 打开文件的url */
	private String openUrl;

	/** 上传文件摘要 */
	private String summary;

	/** 文件名 */
	private String fileName;

	private String studyrecordId;// 学习记录ID

	private IStudyService studyService;

	private ICourseService courseService;

	private SiteService siteService = (SiteService) ComponentManager.get(SiteService.class);

	private SecurityService securityService = (SecurityService) ComponentManager.get(SecurityService.class);

	private Log logger = LogFactory.getLog(FileAction.class);

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

	public ICourseService getCourseService() {
		return courseService;
	}

	public void setCourseService(ICourseService courseService) {
		this.courseService = courseService;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getOpenUrl() {
		return openUrl;
	}

	public void setOpenUrl(String openUrl) {
		this.openUrl = openUrl;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileTitle() {
		return fileTitle;
	}

	public void setFileTitle(String fileTitle) {
		this.fileTitle = fileTitle;
	}

	public String getFileContent() {
		return fileContent;
	}

	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public void setExtendOption(String extendOption) {
		this.extendOption = extendOption;
	}

	public String getExtendOption() {
		return this.extendOption;
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

	public String getFiledataContentType() {
		return FiledataContentType;
	}

	public void setFiledataContentType(String filedataContentType) {
		FiledataContentType = filedataContentType;
	}

	public String getFolderId() {
		return folderId;
	}

	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public IFileService getFileService() {
		return fileService;
	}

	public void setFileService(IFileService fileService) {
		this.fileService = fileService;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	/**
	 * 进入文件管理工具的入口
	 * 
	 * @return
	 */
	public String lookUpRes() throws Exception {
		try {
			// 判断用户角色
			Site site = fileService.getCurrentSite();
			String siteRef = siteService.siteReference(site.getId());
			String currentUser = fileService.getCurrentUser().getId();
			ServletActionContext.getRequest().getSession()
					.setAttribute("courseId", courseService.getCourseBySiteId(site.getId()).getId());
			if (securityService.unlock(currentUser, FunctionRegister.FILE_PERM_NAME, siteRef)) { // 当前用户为教师
				// do something since this user has permission
				return "teahcer_success";
			} else if (securityService.unlock(currentUser, FunctionRegister.FILE_ACCESS_PERM, siteRef)) {// 当前用户为学生
				CacheUtil.getInstance().getCacheOfStudyrecord(site.getId(), currentUser, studyrecordId);
				ServletActionContext.getRequest().setAttribute("studyrecordId", studyrecordId);
				return "student_success";
			} else {
				throw new Exception("无权限");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			// ServletActionContext.getRequest().setAttribute("data", "无权限");
			return ERROR;
		}
	}

	/**
	 * 展示树形列表
	 */
	public String execute() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		// response.setCharacterEncoding("UTF-8");

		Site site = fileService.getCurrentSite();
		String siteRef = siteService.siteReference(site.getId());
		String currentUser = fileService.getCurrentUser().getId();
		ServletActionContext.getRequest().getSession()
				.setAttribute("courseId", courseService.getCourseBySiteId(site.getId()).getId());
		// 用户权限判断
		if (securityService.unlock(currentUser, FunctionRegister.FILE_PERM_NAME, siteRef)) {
			// 用户拥有文件操作权限时
			if (fileService.getSiteRootFolder(fileService.getCurrentSite().getId()) == null) {
				// 若不存在课程跟文件夹则创建根文件夹
				fileService.createRootFolder(fileService.getCurrentSite());
			}
			File siteUploadFileDir = new File(Constants.FILE_PATH + fileService.getCurrentSite().getId());
			if (!siteUploadFileDir.exists()) {
				// 若硬盘上传路径中不存在本课程文件夹 则创建文件夹
				if (!siteUploadFileDir.mkdirs()) {
					throw new Exception("目录" + siteUploadFileDir.getPath() + "创建失败");
				}
			}
		} else {
			if (fileService.getSiteRootFolder(fileService.getCurrentSite().getId()) == null) {
				// 判断用户是否是访问权限返回
				return null;
			}
		}
		List<AsyncTreeModel> data = fileService.displayTree(node);
		response.getWriter().println(JsonBuilder.builderAsyncTreeJson(data));

		return null;
	}

	/**
	 * 创建文件夹
	 * 
	 * @return
	 * @throws Exception
	 */
	public String createFolder() throws Exception {
		AsyncTreeModel asyncTree = fileService.createResourceFolder(parentId, folderName);
		ServletActionContext.getRequest().setAttribute("data", JsonBuilder.builderObjectJson(asyncTree));
		return "result";
	}

	/**
	 * 删除文件夹
	 * 
	 * @return
	 */
	public String deleteFolder() throws Exception {
		if (!fileService.deleteResourceFolder(folderId)) {
			throw new Exception("删除文件夹失败!");
		}
		ServletActionContext.getRequest().setAttribute("data", "\"\"");
		return "result";
	}

	/**
	 * 检测文件夹是否有子节点
	 * 
	 * @return
	 */
	public String detectChild() throws Exception {

		// 获得文件夹下所有子节点 包括 文件 和 文件夹
		List childrenList = fileService.findAllChildByFolderId(folderId);

		if (childrenList != null && childrenList.size() > 0) {
			// 存在子节点
			ServletActionContext.getRequest().setAttribute("data", "{\"result\":1}");
		} else {
			// 不存在子节点
			ServletActionContext.getRequest().setAttribute("data", "{\"result\":0}");
		}
		return "result";
	}

	/**
	 * 文件上传
	 * 
	 * @return
	 */
	public String uploadFile() throws Exception {

		AsyncTreeModel asyncTree = fileService.uploadFile(Filedata, FiledataFileName, fileName, summary, folderId,
				extendOption);
		ServletActionContext.getRequest().setAttribute("data", JsonBuilder.builderObjectJson(asyncTree));
		return "result";
	}

	/**
	 * 修改文件时读取相应文件信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String readFileInfo() throws Exception {
		ResourceFileModel modifiedFile = fileService.readFileInfo(fileId, fileType);
		ServletActionContext.getRequest().setAttribute("data", JsonBuilder.builderObjectJson(modifiedFile));
		return "result";
	}

	/**
	 * 文件修改
	 * 
	 * @return
	 */
	public String modifyFile() throws Exception {
		AsyncTreeModel asyncTree = fileService.modifyFile(Filedata, FiledataFileName, fileContent, fileName, fileId,
				summary, extendOption);
		ServletActionContext.getRequest().setAttribute("data", JsonBuilder.builderObjectJson(asyncTree));
		return "result";
	}

	/**
	 * 文件下载
	 */
	public String downloadFile() throws Exception {

		ResourceFileModel downloadFile = fileService.downloadFile(fileId);
		ServletRequest request = ServletActionContext.getRequest();
		request.setAttribute("data", JsonBuilder.builderObjectJson(downloadFile));
		return "result";
	}

	/**
	 * 删除文件
	 * 
	 * @return
	 */
	public String deleteFile() throws Exception {
		if (!fileService.deleteFile(fileId)) {
			throw new Exception("删除失败");
		}

		ServletActionContext.getRequest().setAttribute("data", "\"\"");
		return "result";
	}

	/**
	 * 创建HTML 和 Txt 文件
	 * 
	 * @return
	 */
	public String createFile() throws Exception {
		AsyncTreeModel asyncTree = fileService.createTxtAndHtmlFile(fileType, fileTitle, folderId, extendOption,
				summary, fileContent);
		ServletActionContext.getRequest().setAttribute("data", JsonBuilder.builderObjectJson(asyncTree));
		return "result";
	}

	/**
	 * 移动文件
	 * 
	 * @return
	 */
	public String moveFile() throws Exception {

		// 变更文件的所属文件夹
		ResourceFileModel resourceFile = fileService.updateFile(fileId, parentId);

		ServletActionContext.getRequest().setAttribute("data", JsonBuilder.builderObjectJson(resourceFile));
		return "result";
	}

	/**
	 * 文件下载时打开的url
	 * 
	 * @return
	 */
	public String openURL() throws Exception {
		Site site = fileService.getCurrentSite();
		String siteRef = siteService.siteReference(site.getId());
		String currentUser = fileService.getCurrentUser().getId();
		try {
			ResourceFileModel downloadFile = fileService.downloadFile(fileId);
			String courseId = courseService.getCourseBySiteId(fileService.getCurrentSite().getId()).getId();
			// 用户权限判断
			if (securityService.unlock(currentUser, FunctionRegister.FILE_ACCESS_PERM, siteRef)) {// 当前用户为学生
				// 计算平时成绩
				if (CodeTable.IsCaculateScoreYes.equals(downloadFile.getExtendOption().toString())) {// 当文件计算平时成绩时
					if (studyrecordId != null && !"".equals(studyrecordId)) {// 学习记录不为空
						studyService.countUsuallyScore(courseId, fileService.getCurrentUser().getId(), studyrecordId);
					}
				}
			}
			HttpServletRequest request = ServletActionContext.getRequest();
			request.setAttribute("openURL", downloadFile.getFileUrl());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return "openUrl";
	}

	/**
	 * 检验当前站点下是否存在同名文件
	 * 
	 * @return
	 * @throws Exception
	 */
	public String checkFile() throws Exception {
		if (fileService.checkDuplicationFileName(folderId, fileName + fileType)) {
			throw new Exception("该文件夹下存在同名文件，请更改文件标题！");
		}

		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print("{\"success\":true,\"result\":true}");
		return null;
	}

}
