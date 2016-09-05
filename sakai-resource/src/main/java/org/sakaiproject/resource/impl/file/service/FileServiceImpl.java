package org.sakaiproject.resource.impl.file.service;

import java.io.File;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.sakaiproject.authz.api.Role;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.resource.api.course.service.ICourseService;
import org.sakaiproject.resource.api.file.model.ResourceDownloadModel;
import org.sakaiproject.resource.api.file.model.ResourceFileModel;
import org.sakaiproject.resource.api.file.model.ResourceFolderModel;
import org.sakaiproject.resource.api.file.service.IFileService;
import org.sakaiproject.resource.api.study.service.IStudyService;
import org.sakaiproject.resource.util.AsyncTreeModel;
import org.sakaiproject.resource.util.Constants;
import org.sakaiproject.resource.util.HibernateDaoSupport;
import org.sakaiproject.resource.util.JsonBuilder;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.tool.api.ToolManager;
import org.sakaiproject.user.api.User;
import org.sakaiproject.user.api.UserDirectoryService;

public class FileServiceImpl extends HibernateDaoSupport implements IFileService {

	private UserDirectoryService userDirectoryService = (UserDirectoryService) ComponentManager
			.get(UserDirectoryService.class);

	private SiteService siteService = (SiteService) ComponentManager.get(SiteService.class);

	private ToolManager toolManager = (ToolManager) ComponentManager.get(ToolManager.class);

	private IStudyService studyService;

	private ICourseService courseService;

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

	/**
	 * 上传文件业务处理
	 * 
	 * @param Filedata
	 *            上传文件
	 * @param FiledataFileName
	 *            上传文件名称
	 * @param fileName
	 *            页面传过来的输入文件名
	 * @param summary
	 *            页面传过来的摘要
	 * @param folderId
	 *            文件父文件夹 ID
	 * @param extendOption
	 *            是否计算平时成绩
	 * @return 设置后的异步树节点
	 */
	public AsyncTreeModel uploadFile(File Filedata, String FiledataFileName, String fileName, String summary,
			String folderId, String extendOption) throws Exception {
//		String webPath = ServletActionContext.getServletContext().getRealPath("/");

		// 将有上传组件传过来的 summary 和 fileName 转换编码为 UTF-8
		summary = URLDecoder.decode(summary, "UTF-8");
		fileName = URLDecoder.decode(fileName, "UTF-8");

		// 日期显示格式化
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");

		// 文件后缀
		String suffix = FiledataFileName.substring(FiledataFileName.lastIndexOf("."));

		// 判断文件夹下是否有重名文件
		if (this.checkDuplicationFileName(folderId, fileName + suffix)) {
			throw new Exception("该文件夹下有重名文件");
		}

		File file = Filedata;

		// FIXME 将文件信息写入资源文件表并与文件夹表关联
		DecimalFormat decimalFormat = new DecimalFormat("#.00");
		ResourceFileModel resourceFile = new ResourceFileModel();
		resourceFile.setCreater(this.getCurrentUser().getId());
		resourceFile.setCreateTime(new Date());
		resourceFile.setFileName(fileName + suffix);
		resourceFile.setFileSize((double) file.length());
		// 文件类型
		resourceFile.setFileType(suffix);
		resourceFile.setFolderId(folderId);
		resourceFile.setModifier(getCurrentUser().getId());
		resourceFile.setUpdateTime(new Date());
		resourceFile.setStatus(1);
		resourceFile.setExtendOption(Integer.parseInt(extendOption));
		resourceFile.setSummary(summary);

		String parentId = folderId;

		ResourceFolderModel parentFolder = this.getFolderById(parentId);
		// 修改直接父文件夹的最后修改日期 与 最后更新者 并保存
		parentFolder.setUpdateTime(new Date());
		parentFolder.setModifier(this.getCurrentUser().getId());
		this.updateFolder(parentFolder);
		// 保存上传文件信息
		this.saveFile(resourceFile);
		resourceFile.setFileUrl(Constants.FILE_URI + this.getCurrentSite().getId() + "/"
				+ resourceFile.getId() + "/" + fileName+suffix);//设置文件URL
		this.updateEntity(resourceFile);//更新文件
		// 上传文件存放目录为 文件上传主目录/课程ID/资源文件ID/上传文件
		File fileSaveDir = new File(Constants.FILE_PATH + this.getCurrentSite().getId() + "/"
				+ resourceFile.getId());
		if (!fileSaveDir.mkdirs()) {
			throw new Exception("目录" + fileSaveDir.getPath() + "创建失败");
		}
		
		// 上传文件对象
		File tempFile = new File(fileSaveDir, fileName + suffix);
		FileUtils.copyFile(file, tempFile);
		file.delete();
		
		// 将上传文件 设置为树节点
		AsyncTreeModel asyncTree = new AsyncTreeModel();
		asyncTree.setId(resourceFile.getId());
		asyncTree.setParentId(parentId);
		asyncTree.setText(resourceFile.getFileName());
		asyncTree.setLeaf(true);
		asyncTree.setFolderSize(this.calFileSize(tempFile.length()));

		asyncTree.setUpdateTime(sdf.format(resourceFile.getUpdateTime()));
		asyncTree.setFileSummary(resourceFile.getSummary());
		asyncTree.setIconCls("task");
		asyncTree.setTreeNodeType("file");
		if (resourceFile.getExtendOption() == 1) {
			asyncTree.setIsCaculateScore("是");
		} else if (resourceFile.getExtendOption() == 2) {
			asyncTree.setIsCaculateScore("否");
		}
		asyncTree.setFileType(resourceFile.getFileType());
		asyncTree.setQtip("文件摘要：" + resourceFile.getSummary());

		ServletActionContext.getRequest().setAttribute("data", JsonBuilder.builderObjectJson(asyncTree));
		return asyncTree;
	}

	/**
	 * 文件删除业务操作
	 * 
	 * @param fileId
	 *            要删除的文件ID
	 * @return 是否删除成功
	 * @throws Exception
	 */
	public boolean deleteFile(String fileId) throws Exception {
		// 是否删除成功的标志
		boolean result = false;

		ResourceFileModel currentFile = null;

		// 级联删除下载资源表中与此文件相关的记录
		this.deleteDownloadByFileId(fileId);

		currentFile = this.getFileByFileId(fileId);

		String parentFolderId = currentFile.getFolderId();
		ResourceFolderModel pFolder = this.getFolderById(parentFolderId);

		// 更改直接父文件夹的最后修改日期 与 最后修改者 并保存
		pFolder.setUpdateTime(new Date());
		pFolder.setModifier(this.getCurrentUser().getId());
		this.updateFolder(pFolder);

		// 删除数据库中的文件记录
		if (!this.deleteFileByFileId(fileId)) {
			throw new Exception("删除失败");
		}
		// 删除存储文件的以资源文件ID命名的文件夹和文件本身

		File dFile = new File(Constants.FILE_PATH + this.getCurrentSite().getId() + "/"
				+ currentFile.getId());
		this.deleteFileFolder(dFile);
		result = true;
		return result;
	}

	/**
	 * 删除存储文件的以资源文件ID命名的文件夹和文件本身
	 * 
	 * @param dFileDir
	 *            文件存储的以文件资源文件ID命名的文件夹
	 * @return 删除是否成功
	 */
	public boolean deleteFileFolder(File dFileDir) {
		boolean result = false;
		for (int i = 0; i < dFileDir.listFiles().length; i++) {
			dFileDir.listFiles()[i].delete();// 删除文件夹中的上传的资源文件
		}
		dFileDir.delete();
		result = true;
		return result;
	}

	/**
	 * 获得当前用户
	 * 
	 * @return
	 */
	public User getCurrentUser() {
		return userDirectoryService.getCurrentUser();
	}

	/**
	 * 得到当前站点
	 * 
	 * @return 当前课程id
	 */
	public Site getCurrentSite() {
		Site site = null;
		try {
			site = siteService.getSite(toolManager.getCurrentPlacement().getContext());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return site;
	}

	/**
	 * 根据资源文件ID 文件类型 读取资源文件
	 * 
	 * @param fileId
	 *            资源文件ID
	 * @param fileType
	 *            文件类型
	 * @return 读取的资源文件对象
	 * @throws Exception
	 */
	public ResourceFileModel readFileInfo(String fileId, String fileType) throws Exception {
		ResourceFileModel modifiedFile = this.getFileByFileId(fileId);
		modifiedFile.setFileName(modifiedFile.getFileName().substring(0, modifiedFile.getFileName().lastIndexOf(".")));
		if (".txt".equalsIgnoreCase(fileType) || ".html".equalsIgnoreCase(fileType)) {
			// 若文件类型为 txt 或 html 则需要 读取文件内容
			File readFile = new File(Constants.FILE_PATH + this.getCurrentSite().getId() + "/"
					+ modifiedFile.getId() + "/" + modifiedFile.getFileName() + modifiedFile.getFileType());

			// 读取txt 或 html 文件内容
			String content = FileUtils.readFileToString(readFile);

			// 将读取的文件内容放入 modifiedFile 的fileContent 属性中
			modifiedFile.setFileContent(content.toString());
		}
		return modifiedFile;
	}

	/**
	 * 更改文件业务操作
	 * 
	 * @param Filedata
	 *            更改文件时重新上传的文件
	 * @param FiledataFileName
	 *            重新上传的文件名
	 * @param fileContent
	 *            当更改文件类型为 txt 或 html 时 的文件内容
	 * @param fileName
	 *            更改后的文件名称
	 * @param fileId
	 *            要更改的 资源文件 的id
	 * @param summary
	 *            文件摘要
	 * @param extendOption
	 *            是否计算平时成绩
	 * @return 更改后要展示的树节点
	 * @throws Exception
	 */
	public AsyncTreeModel modifyFile(File Filedata, String FiledataFileName, String fileContent, String fileName,
			String fileId, String summary, String extendOption) throws Exception {
		// 日期显示格式化
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
		// 获得要修改的资源文件对象
		ResourceFileModel modifiedFile = this.getFileByFileId(fileId);

		modifiedFile.setUpdateTime(new Date());
		// 设置文件的最后的修改者
		modifiedFile.setModifier(this.getCurrentUser().getId());

		modifiedFile.setSummary(summary);

		modifiedFile.setExtendOption(Integer.parseInt(extendOption));
		// 更改后的文件全名 包括扩展名
		String newFileName = "";

		// 以前的文件
		File oldFile = new File(Constants.FILE_PATH + this.getCurrentSite().getId() + "/"
				+ modifiedFile.getId() + "/" + modifiedFile.getFileName());

		if (Filedata != null) {
			DecimalFormat df = new DecimalFormat("#.00");
			// 若存在上传文件则覆盖
			newFileName = fileName + FiledataFileName.substring(FiledataFileName.lastIndexOf("."));
			File writeFile = Filedata;

			if (newFileName.equals(modifiedFile.getFileName())) {
				// 判断该文件夹下是否有重名文件

				// 检查更改的文件名在当前文件夹下是否有重名文件
				if (this.checkDuplicationFileName(modifiedFile.getFolderId(), newFileName)) {
					throw new Exception("该文件夹下有重名文件!");
				}
			}

			// 更改名称后的新文件
			File tempFile = new File(Constants.FILE_PATH + getCurrentSite().getId() + "/"
					+ modifiedFile.getId() + "/" + fileName
					+ FiledataFileName.substring(FiledataFileName.lastIndexOf(".")));

			// 将以前的文件重命名为更改后的文件名
			oldFile.renameTo(tempFile);

			// 用上传的新文件覆盖以前的文件
			FileUtils.copyFile(writeFile, tempFile);

			// 更改文件大小
			String fileSize = df.format((double) writeFile.length() / 1024 / 1024);
			modifiedFile.setFileSize((double) writeFile.length());

			modifiedFile.setFileName(fileName + FiledataFileName.substring(FiledataFileName.lastIndexOf(".")));
			modifiedFile.setFileType(FiledataFileName.substring(FiledataFileName.lastIndexOf(".")));
			modifiedFile.setFileUrl(Constants.FILE_URI + this.getCurrentSite().getId() + "/" + modifiedFile.getId()
					+ "/" + modifiedFile.getFileName());

			writeFile.delete();
		} else {
			// 若上传文件为空 则修改文件时没有上传文件

			newFileName = fileName + modifiedFile.getFileType();

			// 更改文件名后的新文件
			File writeFile = new File(Constants.FILE_PATH + this.getCurrentSite().getId() + "/"
					+ modifiedFile.getId() + "/" + newFileName);

			if (fileContent != null && !"".equalsIgnoreCase(fileContent)) {
				// 若fileContent 不为空 则修改的是 txt 或 html 文件
				if (!newFileName.equals(modifiedFile.getFileName())) {
					// 判断该文件夹下是否有重名文件
					if (this.checkDuplicationFileName(modifiedFile.getFolderId(), newFileName)) {
						throw new Exception("该文件夹下有重名文件!");
					}

				}
				// 向文件中写入内容
				FileUtils.writeStringToFile(oldFile, fileContent);
			}
			// 将以前的文件重命名为新文件名称
			oldFile.renameTo(writeFile);
			// 更改资源文件的文件名字段 以及 URL
			modifiedFile.setFileName(fileName + modifiedFile.getFileType());
			modifiedFile.setFileUrl(this.getCurrentSite().getId() + "/" + modifiedFile.getId()
					+ "/" + modifiedFile.getFileName());
			modifiedFile.setFileSize((double) writeFile.length());
		}

		// 保存修改文件
		this.updateFile(modifiedFile);

		// 将修改后的文件设置为树的节点
		AsyncTreeModel asyncTree = new AsyncTreeModel();
		asyncTree.setId(modifiedFile.getId());
		asyncTree.setParentId(modifiedFile.getFolderId());
		asyncTree.setText(modifiedFile.getFileName());
		asyncTree.setLeaf(true);
		asyncTree.setFolderSize(this.calFileSize(modifiedFile.getFileSize()));
		asyncTree.setUpdateTime(sdf.format(modifiedFile.getUpdateTime()));
		asyncTree.setIconCls("task");
		asyncTree.setTreeNodeType("file");
		asyncTree.setFileOpType("modify");// 文件操作类型为 modify
		if (modifiedFile.getExtendOption() == 1) {
			asyncTree.setIsCaculateScore("是");
		} else if (modifiedFile.getExtendOption() == 2) {
			asyncTree.setIsCaculateScore("否");
		}
		asyncTree.setFileType(modifiedFile.getFileType());
		asyncTree.setFileSummary(modifiedFile.getSummary());
		asyncTree.setQtip("文件摘要：" + modifiedFile.getSummary());

		return asyncTree;
	}

	/**
	 * 创建txt 和 html 文件 业务方法
	 * 
	 * @param fileType
	 *            创建文件类型
	 * @param fileTitle
	 *            文件名称
	 * @param folderId
	 *            所在文件夹ID
	 * @param extendOption
	 *            是否计算平时成绩
	 * @param summary
	 *            文件摘要
	 * @param fileContent
	 *            文件内容
	 * @return 新建树节点
	 * @throws Exception
	 */
	public AsyncTreeModel createTxtAndHtmlFile(String fileType, String fileTitle, String folderId, String extendOption,
			String summary, String fileContent) throws Exception {

		// 格式化日期显示格式
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");

		// 将文件信息写入数据库
		ResourceFileModel resourceFile = new ResourceFileModel();

		resourceFile.setFileName(fileTitle + fileType);

		// 判断该文件下是否存在 相同文件
		if (this.checkDuplicationFileName(folderId, resourceFile.getFileName())) {
			throw new Exception("该文件夹下存在同名文件!");
		}
		resourceFile.setCreater(this.getCurrentUser().getId());
		resourceFile.setCreateTime(new Date());
		resourceFile.setFileSize(0.0);
		// 文件类型
		resourceFile.setFileType(fileType);

		resourceFile.setFileUrl(Constants.FILE_PATH);
		resourceFile.setFolderId(folderId);
		resourceFile.setModifier(getCurrentUser().getId());
		resourceFile.setUpdateTime(new Date());
		resourceFile.setStatus(1);
		resourceFile.setExtendOption(Integer.parseInt(extendOption));
		resourceFile.setSummary(summary);
		this.saveFile(resourceFile);

		// 新建文件： 文件上传路径/课程ID/上传文件ID/上传文件
		File newFile = new File(Constants.FILE_PATH + this.getCurrentSite().getId() + "/"
				+ resourceFile.getId() + "/" + resourceFile.getFileName());

		// 将文件内容写入新文件
		FileUtils.writeStringToFile(newFile, fileContent);

		// 更新资源文件记录的 URL
		resourceFile.setFileUrl(Constants.FILE_URI + this.getCurrentSite().getId() + "/" + resourceFile.getId() + "/"
				+ resourceFile.getFileName());

		// 更新资源文件记录
		resourceFile.setFileSize((double) newFile.length());
		this.updateFile(resourceFile);

		// 将资源文件信息 设置为 树节点
		AsyncTreeModel asyncTree = new AsyncTreeModel();
		asyncTree.setId(resourceFile.getId());
		asyncTree.setParentId(folderId);
		asyncTree.setText(resourceFile.getFileName());
		asyncTree.setLeaf(true);

		asyncTree.setFolderSize(this.calFileSize(newFile.length()));
		asyncTree.setUpdateTime(sdf.format(resourceFile.getUpdateTime()));
		asyncTree.setIconCls("task");
		asyncTree.setTreeNodeType("file");
		asyncTree.setFileSummary(resourceFile.getSummary());

		if (resourceFile.getExtendOption() == 1) {
			asyncTree.setIsCaculateScore("是");
		} else if (resourceFile.getExtendOption() == 2) {
			asyncTree.setIsCaculateScore("否");
		}
		asyncTree.setFileType(resourceFile.getFileType());
		asyncTree.setQtip("文件摘要：" + resourceFile.getSummary());

		return asyncTree;
	}

	/**
	 * 根据文件大小计算相应单位下的文件大小
	 * 
	 * @param fileLength
	 * @return 对应单位下的文件大小的字符串
	 */
	public String calFileSize(double fileLength) {
		String result = "";
		// 格式化小数位数
		DecimalFormat decimalFormat = new DecimalFormat("#0.00");
		double fileSize = fileLength / 1024 / 1024;
		String scale = "M";
		if (fileSize < 1.0) {
			fileSize = fileSize * 1024;
			scale = "K";
			if (fileSize < 1.0) {
				fileSize = fileSize * 1024;
				scale = "B";
			}
		}

		result = decimalFormat.format(fileSize) + scale;
		return result;
	}

	/**
	 * 删除资源文件夹
	 * 
	 * @param folderId
	 *            要删除的资源文件夹ID
	 * @return 删除是否额偶成功
	 * @throws Exception
	 */
	public boolean deleteResourceFolder(String folderId) throws Exception {
		boolean result = false;
		// 判断是否为课程根目录 课程根目录无法删除
		ResourceFolderModel folder = this.getFolderById(folderId);
		if ("0".equalsIgnoreCase(folder.getParentId())) {
			// 课程根目录的父节点id 为 0
			throw new Exception("课程根目录无法删除！");
		}

		// TODO 递归删除文件夹下的文件
		if (!this.deleteFileAndFolder(folderId)) {
			// 删除子文件以及文件夹失败返回
			throw new Exception("删除失败");
		}

		// TODO 循环更改父文件夹大小
		String parentFolderId = folder.getParentId();

		// 修改直接父文件夹 最后修改日期
		ResourceFolderModel pFolder = this.getFolderById(parentFolderId);
		pFolder.setUpdateTime(new Date());
		this.updateFolder(pFolder);

		// 删除子文件以及子文件夹成功更改完文件夹大小后删除此文件夹
		if (!this.deleteFolder(folderId)) {
			throw new Exception("删除失败");
		}

		result = true;
		return result;
	}

	/**
	 * 递归删除文件夹下的所有子文件和子文件夹
	 * 
	 * @param folderId
	 *            文件夹id
	 */
	public boolean deleteFileAndFolder(String folderId) {
		boolean deleteResult = true;
		try {
			List<ResourceFolderModel> childrenFolderList = this.findChildByParentId(folderId);

			// 删除子文件夹
			for (ResourceFolderModel childFolder : childrenFolderList) {
				this.deleteFileAndFolder(childFolder.getId());
				this.deleteFolder(childFolder.getId());
			}

			// 删除子文件
			this.deleteFileByParentId(folderId);
		} catch (Exception e) {
			e.printStackTrace();
			deleteResult = false;
		}

		return deleteResult;
	}

	/**
	 * 创建资源文件夹操作
	 * 
	 * @param parentId
	 *            父文件夹ID
	 * @param folderName
	 *            资源文件夹名称
	 * @return 新建树节点
	 * @throws Exception
	 */
	public AsyncTreeModel createResourceFolder(String parentId, String folderName) throws Exception {
		// 日期显示格式化
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
		// 获得父节点下所有节点
		List<ResourceFolderModel> childrenList = this.findChildByParentId(parentId);

		// 判断同一目录下是否有同名文件夹
		for (ResourceFolderModel child : childrenList) {
			if (folderName.equals(child.getFolderName())) {
				throw new Exception("该目录下存在同名文件夹");
			}
		}

		ResourceFolderModel folder = new ResourceFolderModel();
		folder.setCreater(this.getCurrentUser().getId());
		folder.setCreateTime(new Date());
		folder.setFolderName(folderName);
		folder.setModifier(this.getCurrentUser().getId());
		folder.setParentId(parentId);
		folder.setSiteId(this.getCurrentSite().getId());
		folder.setStatus(1);
		folder.setUpdateTime(new Date());

		// 创建文件夹
		this.createFolder(folder);

		// 获得新建文件夹的直接父文件夹
		ResourceFolderModel parentFolder = this.getFolderById(parentId);

		// 更改直接父文件夹的最后修改时间
		parentFolder.setUpdateTime(new Date());
		// 更新直接父文件夹
		this.updateFolder(parentFolder);

		AsyncTreeModel asyncTree = new AsyncTreeModel();
		asyncTree.setId(folder.getId());
		asyncTree.setParentId(parentId);
		asyncTree.setText(folder.getFolderName());
		asyncTree.setLeaf(true);
		asyncTree.setIconCls("folder-empty");
		asyncTree.setTreeNodeType("folder");
		// 文件夹 不显示 大小
		asyncTree.setFolderSize("");
		asyncTree.setUpdateTime(sdf.format(folder.getUpdateTime()));
		return asyncTree;
	}

	/**
	 * 资源树展示操作
	 * 
	 * @param node
	 *            要展开的父节点ID
	 * @return
	 * @throws Exception
	 */
	public List<AsyncTreeModel> displayTree(String node) throws Exception {
		// 判断这门课成是否有 课程 根文件夹 不存在则创建课程根文件夹
		Site site = this.getCurrentSite();
		String currentUser = this.getCurrentUser().getId();
		Role userRole = site.getUserRole(currentUser);

		// 格式化日期显示
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
		String parentId = node;
		List<AsyncTreeModel> data = new ArrayList<AsyncTreeModel>();

		if ("0".equalsIgnoreCase(parentId)) {
			// 为课程根节点时
			ResourceFolderModel siteFolder = this.getSiteRootFolder(this.getCurrentSite().getId());
			AsyncTreeModel asyncTree = new AsyncTreeModel();
			asyncTree.setId(siteFolder.getId());
			asyncTree.setParentId(parentId);
			asyncTree.setText(siteFolder.getFolderName());
			asyncTree.setTreeNodeType("folder");

			// 文件夹大小 不显示
			asyncTree.setFolderSize("");
			asyncTree.setUpdateTime(sdf.format(siteFolder.getUpdateTime()));
			if (this.isFolderEmpty(parentId)) {
				asyncTree.setLeaf(true);
				asyncTree.setIconCls("folder-empty");
			} else {
				asyncTree.setLeaf(false);
				asyncTree.setIconCls("folder-not-empty");
			}
			data.add(asyncTree);

		} else {

			// 得到指定父id的子节点
			List<ResourceFolderModel> childList = this.findChildByParentId(parentId);

			// 获得当前父文件夹下的文件
			List<ResourceFileModel> fileList = this.getFileByParentId(parentId);
			for (ResourceFileModel resourceFile : fileList) {
				// 遍历子文件 将子文件放入树节点对象中
				List<ResourceDownloadModel> downRecordList = this.getDownloadRecordByFileIdAndUserId(resourceFile.getId(), this.getCurrentUser().getId());
				Map<String, Object> attrMap = new HashMap<String, Object>();
				attrMap.put("downloadTimes", downRecordList.size());
				AsyncTreeModel asyncTree = new AsyncTreeModel();
				asyncTree.setId(resourceFile.getId());
				asyncTree.setParentId(parentId);
				asyncTree.setText(resourceFile.getFileName());
				asyncTree.setLeaf(true);
				asyncTree.setFolderSize(this.calFileSize(resourceFile.getFileSize()));
				asyncTree.setUpdateTime(sdf.format(resourceFile.getUpdateTime()));
				asyncTree.setFileSummary(resourceFile.getSummary());
				asyncTree.setIconCls("task");
				asyncTree.setTreeNodeType("file");
				asyncTree.setAttributes(attrMap);
				asyncTree.setQtip("文件摘要：" + resourceFile.getSummary());
				if (resourceFile.getExtendOption() == 1) {
					asyncTree.setIsCaculateScore("是");
				} else if (resourceFile.getExtendOption() == 2) {
					asyncTree.setIsCaculateScore("否");
				}
				asyncTree.setFileType(resourceFile.getFileType());
				data.add(asyncTree);
			}

			for (ResourceFolderModel resourceFolder : childList) {
				// 遍历子文件夹 将子文件夹放入树节点对象中
				AsyncTreeModel asyncTree = new AsyncTreeModel();
				asyncTree.setId(resourceFolder.getId());
				asyncTree.setParentId(parentId);
				asyncTree.setText(resourceFolder.getFolderName());

				asyncTree.setTreeNodeType("folder");
				if (this.isFolderEmpty(resourceFolder.getId())) {
					asyncTree.setLeaf(true);
					asyncTree.setIconCls("folder-empty");
				} else {
					asyncTree.setLeaf(false);
					asyncTree.setIconCls("folder-not-empty");
				}
				// 文件夹大小不显示
				asyncTree.setFolderSize("");

				asyncTree.setUpdateTime(sdf.format(resourceFolder.getUpdateTime()));
				data.add(asyncTree);
			}
		}

		return data;
	}

	/**
	 * 通过文件Id和用户Id获得对应文件下载资源列表 暂时还未用到
	 * 
	 * @param fileId
	 * @param userId
	 * @return 文件对应下载资源列表
	 * @throws Exception
	 */
	public List<ResourceDownloadModel> getDownloadRecordByFileIdAndUserId(String fileId, String userId) throws Exception {

		Map<String, String> values = new HashMap<String, String>();
		values.put("fileId", fileId);
		values.put("userId", userId);
		String hql = "from ResourceDownloadModel as resourceDownload where resourceDownload.fileId=:fileId and resourceDownload.downloader=:userId";
		return this.findEntity(hql, values);
	}
	
	/**
	 * 判断文件夹是否为空
	 * 
	 * @param parentId
	 *            父文件夹ID
	 * @return 父文件夹是否为空
	 */
	public boolean isFolderEmpty(String parentId) {
		boolean result = true;
		try {
			List childrenList = this.findAllChildByFolderId(parentId);
			if (childrenList != null && childrenList.size() != 0) {
				result = false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 创建课程根资源文件夹
	 * 
	 * @param siteId
	 */
	public void createRootFolder(Site currentSite) {
		ResourceFolderModel rootFolder = new ResourceFolderModel();
		rootFolder.setCreater(this.getCurrentUser().getId());
		rootFolder.setCreateTime(new Date());
		rootFolder.setFolderName(currentSite.getTitle());
		rootFolder.setModifier(this.getCurrentUser().getId());
		rootFolder.setParentId("0");
		rootFolder.setSiteId(currentSite.getId());
		rootFolder.setStatus(1);
		rootFolder.setUpdateTime(new Date());

		try {
			this.createFolder(rootFolder);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 资源文件下载
	 * 
	 * @param fileId
	 *            资源文件ID
	 * @return
	 * @throws Exception
	 */
	public ResourceFileModel downloadFile(String fileId) throws Exception {

		ResourceFileModel downloadFile = this.getFileByFileId(fileId);
		if (downloadFile == null) {
			throw new Exception("下载出错");
		}

		// 创建并保存 下载文件记录
		ResourceDownloadModel downloadLog = new ResourceDownloadModel();
		downloadLog.setDownloader(getCurrentUser().getId());
		downloadLog.setFileId(downloadFile.getId());
		downloadLog.setLastTime(new Date());

		// 保存资源文件下载记录
		this.saveDownloadLog(downloadLog);

		// 文件下载次数加1 并更新
		downloadFile.setDownloadCount(downloadFile.getDownloadCount() + 1);
		// 设置文件下载路径
//		downloadFile.setFileUrl(Constants.FILE_URI + getCurrentSite().getId() + "/"
//				+ downloadFile.getId() + "/" + downloadFile.getFileName());
		this.updateFile(downloadFile);
		
		return downloadFile;
	}

	/**
	 * 创建文件夹
	 * 
	 * @param resourceFolderModel
	 * @throws Exception
	 */
	public void createFolder(ResourceFolderModel resourceFolderModel) throws Exception {
		this.createEntity(resourceFolderModel);
	}

	/**
	 * 得到指定父id的子节点
	 * 
	 * @param parentId
	 * @return 子节点集合
	 * @throws Exception
	 */
	public List<ResourceFolderModel> findChildByParentId(String parentId) throws Exception {

		Map<String, String> values = new HashMap<String, String>();
		values.put("parentId", parentId);

		String hql = "from ResourceFolderModel as resourceFolder where resourceFolder.parentId=:parentId";
		return this.findEntity(hql, values);
	}

	/**
	 * @param siteId
	 *            得到所有的父id
	 * @throws Exception
	 */
	public List<String> queryAllParentIdBySiteId(String siteId) throws Exception {
		Map<String, String> values = new HashMap<String, String>();
		values.put("siteId", siteId);
		String hql = "select resourceFolder.parentId from ResourceFolderModel as resourceFolder where resourceFolder.siteId=:siteId group by resourceFolder.parentId";
		return findEntity(hql, values);
	}

	/**
	 * 删除文件夹
	 * 
	 * @param folderId
	 *            文件夹id
	 * @return 删除是否成功
	 * @throws Exception
	 */
	public boolean deleteFolder(String folderId) throws Exception {
		boolean result = false;
		this.deleteEntity(ResourceFolderModel.class, folderId);
		result = true;
		return result;
	}

	/**
	 * 根据folderId 获得 对应的ResourceFolderModel 实体对象
	 * 
	 * @param folderId
	 * @return 对应的ResourceFolderModel 实体对象
	 * @throws Exception
	 */
	public ResourceFolderModel getFolderById(String folderId) throws Exception {

		return (ResourceFolderModel) findEntityById(ResourceFolderModel.class, folderId);
	}

	/**
	 * 检查同一文件夹下是否有重名文件
	 * 
	 * @param fileName
	 * @param folderId
	 * @return 是否有重名文件
	 * @throws Exception
	 */
	public boolean checkDuplicationFileName(String folderId, String fileName) throws Exception {

		boolean result = false;
		Map<String, String> values = new HashMap<String, String>();
		values.put("folderId", folderId);
		values.put("fileName", fileName);
		String hql = "from ResourceFileModel as resourceFile where resourceFile.folderId=:folderId and resourceFile.fileName=:fileName";
		List<ResourceFileModel> fileList = this.findEntity(hql, values);
		if (fileList != null && fileList.size() > 0) {
			result = true;
		}

		return result;
	}

	/**
	 * 保存资源文件信息
	 * 
	 * @param resourceFile
	 * @throws Exception
	 */
	public void saveFile(ResourceFileModel resourceFile) throws Exception {
		this.createEntity(resourceFile);
	}

	/**
	 * 根据父文件夹id获得该文件夹下的文件
	 * 
	 * @param parentId
	 * @return 该父文件夹下的所有文件
	 * @throws Exception
	 */
	public List<ResourceFileModel> getFileByParentId(String parentId) throws Exception {
		Map<String, String> values = new HashMap<String, String>();
		values.put("parentId", parentId);
		String hql = "from ResourceFileModel as resourceFile where resourceFile.folderId=:parentId";
		return this.findEntity(hql, values);
	}

	/**
	 * 更新资源文件夹
	 * 
	 * @param resourceFolder
	 * @throws Exception
	 */
	public void updateFolder(ResourceFolderModel resourceFolder) throws Exception {
		this.updateEntity(resourceFolder);
	}

	/**
	 * 删除某文件夹下的所有文件
	 * 
	 * @param parentId
	 * @throws Exception
	 */
	public void deleteFileByParentId(String parentId) throws Exception {
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("parentId", parentId);
		String hql = "delete from ResourceFileModel as resourceFile where resourceFile.folderId =:parentId";

		this.updateEntity(hql, values);
	}

	/**
	 * 通过文件Id获得资源文件
	 * 
	 * @param fileId
	 * @return 资源文件对象
	 * @throws Exception
	 */
	public ResourceFileModel getFileByFileId(String fileId) throws Exception {

		return (ResourceFileModel) findEntityById(ResourceFileModel.class, fileId);
	}

	/**
	 * 获得课程根文件夹是否存在
	 * 
	 * @param SiteId
	 * @return 课程根文件夹
	 * @throws Exception
	 */
	public ResourceFolderModel getSiteRootFolder(String siteId) throws Exception {
		ResourceFolderModel result = null;
		Map<String, String> values = new HashMap<String, String>();
		values.put("siteId", siteId);

		String hql = "from ResourceFolderModel as resourceFolder where resourceFolder.siteId=:siteId and resourceFolder.parentId='0'";

		List<ResourceFolderModel> folderList = this.findEntity(hql, values);
		if (folderList != null && folderList.size() > 0) {
			result = folderList.get(0);
		}

		return result;
	}

	/**
	 * 保存下载记录
	 * 
	 * @param downloadLog
	 * @throws Exception
	 */
	public void saveDownloadLog(ResourceDownloadModel downloadLog) throws Exception {
		this.createEntity(downloadLog);
	}

	/**
	 * 通过文件Id获得对应文件下载资源列表 暂时还未用到
	 * 
	 * @param fileId
	 * @return 文件对应下载资源列表
	 * @throws Exception
	 */
	public List<ResourceDownloadModel> getDownloadRecordByFileId(String fileId) throws Exception {

		Map<String, String> values = new HashMap<String, String>();
		values.put("fileId", fileId);
		String hql = "from ResourceDownloadModel as resourceDownload where resourceDownload.fileId=:fileId";

		return this.findEntity(hql, values);
	}

	/**
	 * 通过文件Id 删除 文件对应的资源下载的记录
	 * 
	 * @param fileId
	 * @throws Exception
	 */
	public void deleteDownloadByFileId(String fileId) throws Exception {
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("fileId", fileId);
		String hql = "delete from ResourceDownloadModel as rdownload where rdownload.fileId=:fileId";
		this.updateEntity(hql, values);
	}

	/**
	 * 通过文件id 删除对应的 资源文件
	 * 
	 * @param fileId
	 */
	public boolean deleteFileByFileId(String fileId) throws Exception {
		boolean result = false;
		this.deleteEntity(ResourceFileModel.class, fileId);
		result = true;

		return result;
	}

	/**
	 * 通过文件Id和移动后的父文件夹Id更新资源文件记录
	 * 
	 * @param fileId
	 * @param parentId
	 * @throws Exception
	 */
	public ResourceFileModel updateFile(String fileId, String parentId) throws Exception {
		// 根据fileid查出对象
		ResourceFileModel resourceFile = (ResourceFileModel) this.findEntityById(ResourceFileModel.class, fileId);

		ResourceFolderModel oldParentFolder = (ResourceFolderModel) this.findEntityById(ResourceFolderModel.class,
				resourceFile.getFolderId());
		ResourceFolderModel newParentFolder = (ResourceFolderModel) this.findEntityById(ResourceFolderModel.class,
				parentId);

		// 更新对象的文件夹Id
		resourceFile.setFolderId(parentId);
		// 更新
		this.updateEntity(resourceFile);
		this.updateEntity(oldParentFolder);
		this.updateEntity(newParentFolder);

		return resourceFile;
	}

	/**
	 * 找到文件夹下的所有子节点 包括子文件和子文件夹的id
	 * 
	 * @param folderId
	 * @return
	 * @throws Exception
	 */
	public List findAllChildByFolderId(String folderId) throws Exception {
		Map<String, String> values = new HashMap<String, String>();
		values.put("folderId", folderId);
		List childrenList = new ArrayList();
		String hqlFolder = "from ResourceFolderModel as folder  where  folder.parentId=:folderId";
		String hqlFile = "from ResourceFileModel as file where file.folderId=:folderId";
		childrenList.addAll(this.findEntity(hqlFolder, values));
		childrenList.addAll(this.findEntity(hqlFile, values));
		return childrenList;
	}

	/**
	 * 更新文件
	 * 
	 * @param modifiedFile
	 * @throws Exception
	 */
	public void updateFile(ResourceFileModel modifiedFile) throws Exception {
		this.updateEntity(modifiedFile);
	}

	/**
	 * 获得某站点下所有计算平时成绩的文件资源
	 * 
	 * @param siteId
	 * @return 该站点下所有计算平时成绩的文件资源集合
	 * @throws Exception
	 */
	public List<ResourceFileModel> getAllNormalGradeFileBySiteId(String siteId) throws Exception {

		Map<String, String> values = new HashMap<String, String>();
		values.put("siteId", siteId);
		String hql = "select file from ResourceFileModel as file, ResourceFolderModel as folder   where file.folderId=folder.id and file.extendOption=1 and folder.siteId=:siteId";

		return this.findEntity(hql, values);
	}

	/**
	 * 通过站点ID 获得该站点下的所有文件资源
	 * 
	 * @param siteId
	 *            站点ID
	 * @return 站点下的所有文件资源集合
	 * @throws Exception
	 */
	public List<ResourceFileModel> getAllFileBySiteId(String siteId) throws Exception {
		Map<String, String> values = new HashMap<String, String>();
		values.put("siteId", siteId);
		String hql = "select file from ResourceFileModel as file, ResourceFolderModel as folder   where file.folderId=folder.id and folder.siteId=:siteId";

		return this.findEntity(hql, values);
	}
}
