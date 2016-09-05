/**
 * 
 */
package org.sakaiproject.resource.tool.template.action;

import java.io.File;
import java.util.List;

import org.adl.samplerte.util.UnZipHandler;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.authz.api.SecurityService;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.resource.api.course.service.ICourseService;
import org.sakaiproject.resource.api.template.model.TemplateModel;
import org.sakaiproject.resource.api.template.service.ITemplateService;
import org.sakaiproject.resource.util.Constants;
import org.sakaiproject.resource.util.FunctionRegister;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.tool.api.ToolManager;
import org.sakaiproject.user.api.UserDirectoryService;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 模板管理
 * 
 * @author cedarwu
 */
public class TemplateAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Log logger = LogFactory.getLog(TemplateAction.class);

	private ITemplateService templateService;

	public ITemplateService getTemplateService() {
		return templateService;
	}

	public void setTemplateService(ITemplateService templateService) {
		this.templateService = templateService;
	}

	private ICourseService courseService = null;

	public ICourseService getCourseService() {
		return courseService;
	}

	public void setCourseService(ICourseService courseService) {
		this.courseService = courseService;
	}

	private SiteService siteService = (SiteService) ComponentManager.get(SiteService.class);
	private SecurityService securityService = (SecurityService) ComponentManager.get(SecurityService.class);
	private ToolManager toolManager = (ToolManager) ComponentManager.get(ToolManager.class);
	/** sakai业务逻辑类 */
	private UserDirectoryService userService = (UserDirectoryService) ComponentManager.get(UserDirectoryService.class);

	private TemplateModel templateModel;

	private List<TemplateModel> templateModels;

	/**
	 * 模板id
	 */
	private String id;

	private String name;

	private String title;

	private String summary;

	private String status;

	/**
	 * 文件上传对象
	 */
	private File zipFile;

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
			logger.error(e.getMessage(), e);
			throw e;
		}
		return site;
	}

	/**
	 * 获得当前用户名
	 * 
	 * @return
	 */
	private String getCurrentUserName() {
		return userService.getCurrentUser().getId();
	}

	/**
	 * 只是在前台跳转到目标页面没有实际操作
	 */
	public String execute() throws Exception {
		Site curSite = this.getCurrentSite();
		String siteRef = siteService.siteReference(curSite.getId());
		String currentUser = this.getCurrentUserName();
		if (securityService.unlock(currentUser, FunctionRegister.TEMPLATE_PERM_MAINTAIN, siteRef)) {// 管理员
			templateModels = templateService.query(status, 1000, 0);
			return "list";
		} else {
			addActionError("无权限");
			return ERROR;
		}
	}

	/**
	 * 编辑页面
	 */
	public String edit() {
		try {
			if (StringUtils.isNotBlank(id)) {
				templateModel = templateService.get(id);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return "edit";
	}

	/**
	 * 启用/停用
	 */
	public String publish() {
		try {
			if (StringUtils.isNotBlank(id)) {
				templateService.publish(id);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return "query";
	}

	/**
	 * 删除
	 */
	public String delete() {
		try {
			if (StringUtils.isNotBlank(id)) {
				templateModel = templateService.get(id);
				if (templateModel.getName().equals(TemplateModel.TEMPLATE_NAME_DEFAULT)) {
					addActionError("不可删除默认模板");
					return ERROR;
				}

				if (courseService.countCourseUseTemplate(templateModel.getName()) > 0) {
					addActionError("此模板已被使用，不能删除");
					return ERROR;
				}

				templateService.delete(templateModel);

				// 删除文件
				String dirPath = Constants.TEMPLATE_PATH_DIR + templateModel.getName();
				File dir = new File(dirPath);
				if (dir.exists()) {
					dir.delete();
				}
				String fileExt = ".zip";
				File file = new File(dirPath + fileExt);
				if (file.exists()) {
					file.delete();
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return "query";
	}

	/**
	 * 保存导入的资源
	 * 
	 * @throws Exception
	 */
	public String save() {
		try {
			// 保留原自定义模板的调用
			if (name.equals(TemplateModel.TEMPLATE_NAME_CUSTOM)) {
				addActionError(name + " 此名称不可用");
				return ERROR;
			}

			// 如果ID为空则是新增，如果不为空则是修改
			if (StringUtils.isBlank(id)) {
				// 检查是否重名
				TemplateModel ts = templateService.getByName(name);
				if (ts != null) {
					addActionError(name + " 此名称已被占用");
					return ERROR;
				}
				// 创建一个新的课件资源对象
				templateModel = new TemplateModel();
				// 给课件资源名称赋值
				templateModel.setName(name);

				// 创建(也就是上传)时间赋值
				templateModel.setCreateTime(new java.sql.Timestamp(System.currentTimeMillis()));
				// 创建用户赋值
				templateModel.setCreateUser(getCurrentUserName());
				// 默认不可用，导入文件之后才可用
				templateModel.setStatus(TemplateModel.ENUM_STATUS_DISABLED);
			} else {
				// 创建一个新的课件资源对象
				templateModel = templateService.get(id);
			}
			// 给课件资源名称赋值
			templateModel.setTitle(title);
			// 课件资源摘要赋值
			templateModel.setSummary(summary);

			// 修改时间赋值
			templateModel.setUpdateTime(new java.sql.Timestamp(System.currentTimeMillis()));
			// 给修改的用户赋值，第一次默认和创建者一样
			templateModel.setUpdateUser(getCurrentUserName());

			// 导入包
			if (zipFile != null) {
				if (zipFile.length() / 1024 / 1024 > 50) {
					addActionError("文件超过限定大小");
					return ERROR;
				}

				String dirPath = Constants.TEMPLATE_PATH_DIR + name;
				File dir = new File(dirPath);
				if (!dir.exists()) {
					dir.mkdir();
				}
				String fileExt = ".zip";

				File file = new File(dirPath + fileExt);
				if (file.exists()) {
					file.delete();
				}

				FileUtils.copyFile(zipFile, file);

				UnZipHandler uzh = new UnZipHandler(file.getPath(), dirPath + "/");
				uzh.extract();

				templateModel.setStatus(TemplateModel.ENUM_STATUS_ENABLED);
			}
			templateService.save(templateModel);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			addActionError(e.getMessage());
			return ERROR;
		}

		return "query";
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

	public List<TemplateModel> getTemplateModels() {
		return templateModels;
	}

	public void setTemplateModels(List<TemplateModel> templateModels) {
		this.templateModels = templateModels;
	}

	public TemplateModel getTemplateModel() {
		return templateModel;
	}

	public void setTemplateModel(TemplateModel templateModel) {
		this.templateModel = templateModel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public File getZipFile() {
		return zipFile;
	}

	public void setZipFile(File zipFile) {
		this.zipFile = zipFile;
	}

}
