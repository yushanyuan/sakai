package org.sakaiproject.resource.api.file.service;

import java.io.File;
import java.util.List;

import org.sakaiproject.resource.api.file.model.ResourceDownloadModel;
import org.sakaiproject.resource.api.file.model.ResourceFileModel;
import org.sakaiproject.resource.api.file.model.ResourceFolderModel;
import org.sakaiproject.resource.util.AsyncTreeModel;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.user.api.User;

public interface IFileService {

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
			String folderId, String extendOption) throws Exception;

	/**
	 * 文件删除业务操作
	 * 
	 * @param fileId
	 *            要删除的文件ID
	 * @return 是否删除成功
	 * @throws Exception
	 */
	public boolean deleteFile(String fileId) throws Exception;

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
	public ResourceFileModel readFileInfo(String fileId, String fileType) throws Exception;

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
			String fileId, String summary, String extendOption) throws Exception;

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
			String summary, String fileContent) throws Exception;

	/**
	 * 删除资源文件夹
	 * 
	 * @param folderId
	 *            要删除的资源文件夹ID
	 * @return 删除是否额偶成功
	 * @throws Exception
	 */
	public boolean deleteResourceFolder(String folderId) throws Exception;

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
	public AsyncTreeModel createResourceFolder(String parentId, String folderName) throws Exception;

	/**
	 * 资源树展示操作
	 * 
	 * @param node
	 *            要展开的父节点ID
	 * @return
	 * @throws Exception
	 */
	public List<AsyncTreeModel> displayTree(String node) throws Exception;

	/**
	 * 得到当前站点
	 * 
	 * @return 当前课程id
	 */
	public Site getCurrentSite();

	/**
	 * 获得当前用户
	 * 
	 * @return
	 */
	public User getCurrentUser();

	/**
	 * 资源文件下载
	 * 
	 * @param fileId
	 *            资源文件ID
	 * @return
	 * @throws Exception
	 */
	public ResourceFileModel downloadFile(String fileId) throws Exception;

	/**
	 * 创建文件夹
	 * 
	 * @param resourceFolderModel
	 * @throws Exception
	 */
	public void createFolder(ResourceFolderModel resourceFolderModel) throws Exception;

	/**
	 * 得到指定父id的子节点
	 * 
	 * @param parentId
	 * @return 子节点集合
	 * @throws Exception
	 */
	public List<ResourceFolderModel> findChildByParentId(String parentId) throws Exception;

	/**
	 * 得到所有的父id
	 * 
	 * @param SiteId
	 * @throws Exception
	 */
	public List<String> queryAllParentIdBySiteId(String siteId) throws Exception;

	/**
	 * 删除文件夹
	 * 
	 * @param folderId
	 *            文件夹id
	 * @throws Exception
	 */
	public boolean deleteFolder(String folderId) throws Exception;

	/**
	 * 根据folderId 获得 对应的ResourceFolderModel 实体对象
	 * 
	 * @param folderId
	 * @return 对应的ResourceFolderModel 实体对象
	 * @throws Exception
	 */
	public ResourceFolderModel getFolderById(String folderId) throws Exception;

	/**
	 * 检查同一文件夹下是否有重名文件
	 * 
	 * @param fileName
	 * @param folderId
	 * @return 是否有重名文件
	 * @throws Exception
	 */
	public boolean checkDuplicationFileName(String folderId, String fileName) throws Exception;

	/**
	 * 保存资源文件信息
	 * 
	 * @param resourceFile
	 * @throws Exception
	 */
	public void saveFile(ResourceFileModel resourceFile) throws Exception;

	/**
	 * 根据父文件夹id获得该文件夹下的文件
	 * 
	 * @param parentId
	 * @return 该父文件夹下的所有文件
	 * @throws Exception
	 */
	public List<ResourceFileModel> getFileByParentId(String parentId) throws Exception;

	/**
	 * 更新资源文件夹
	 * 
	 * @param resourceFolder
	 * @throws Exception
	 */
	public void updateFolder(ResourceFolderModel resourceFolder) throws Exception;

	/**
	 * 上出某文件夹下的所有文件
	 * 
	 * @param parentId
	 * @throws Exception
	 */
	public void deleteFileByParentId(String parentId) throws Exception;

	/**
	 * 通过文件Id获得资源文件
	 * 
	 * @param fileId
	 * @return 资源文件对象
	 * @throws Exception
	 */
	public ResourceFileModel getFileByFileId(String fileId) throws Exception;

	/**
	 * 获得课程根文件夹
	 * 
	 * @param SiteId
	 * @return 课程根文件夹
	 * @throws Exception
	 */
	public ResourceFolderModel getSiteRootFolder(String siteId) throws Exception;

	/**
	 * 保存下载记录
	 * 
	 * @param downloadLog
	 * @throws Exception
	 */
	public void saveDownloadLog(ResourceDownloadModel downloadLog) throws Exception;

	/**
	 * 通过文件Id获得对应文件下载资源列表 暂时还未用到
	 * 
	 * @param fileId
	 * @return 文件对应下载资源列表
	 * @throws Exception
	 */
	public List<ResourceDownloadModel> getDownloadRecordByFileId(String fileId) throws Exception;

	/**
	 * 通过文件Id 删除文件对应的资源下载的记录
	 * 
	 * @param fileId
	 * @throws Exception
	 */
	public void deleteDownloadByFileId(String fileId) throws Exception;

	/**
	 * 通过文件id 删除对应的 资源文件
	 * 
	 * @param fileId
	 */
	public boolean deleteFileByFileId(String fileId) throws Exception;

	/**
	 * 通过文件Id和移动后的父文件夹Id更新资源文件记录
	 * 
	 * @param fileId
	 * @param parentId
	 * @throws Exception
	 */
	public ResourceFileModel updateFile(String fileId, String parentId) throws Exception;

	/**
	 * 找到文件夹下的所有子节点 包括子文件和子文件夹
	 * 
	 * @param folderId
	 * @return
	 * @throws Exception
	 */
	public List findAllChildByFolderId(String folderId) throws Exception;

	/**
	 * 更新文件
	 * 
	 * @param modifiedFile
	 * @throws Exception
	 */
	public void updateFile(ResourceFileModel modifiedFile) throws Exception;

	/**
	 * 获得某站点下所有计算平时成绩的文件资源
	 * 
	 * @param siteId
	 * @return 该站点下所有计算平时成绩的文件资源集合
	 * @throws Exception
	 */
	public List<ResourceFileModel> getAllNormalGradeFileBySiteId(String siteId) throws Exception;
	
	/**
	 * 创建课程根资源文件夹
	 * 
	 * @param siteId
	 */
	public void createRootFolder(Site currentSite);
	
	/**
	 * 通过站点ID 获得该站点下的所有文件资源
	 * @param siteId 站点ID
	 * @return 站点下的所有文件资源集合
	 * @throws Exception
	 */
	public List<ResourceFileModel> getAllFileBySiteId(String siteId) throws Exception;

}
