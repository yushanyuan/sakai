package org.sakaiproject.resource.util;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ckfinder.connector.configuration.ConfigurationPathBuilder;
import com.ckfinder.connector.utils.PathUtils;

/**
 * ckfinder 上传文件自定义路径配置 需在ckfinder的配置文件config.xml中的basePathBuilderImpl标签中指明该类
 * 
 * @author zhangxin
 * 
 */
public class ResourceConfigurationPathBuilder extends ConfigurationPathBuilder {

	private Log logger = LogFactory.getLog(ResourceConfigurationPathBuilder.class);

	@Override
	public String getBaseDir(HttpServletRequest request) {
		HttpSession session = request.getSession();
		logger.debug("---getBaseDir----courseId-----" + request.getParameter("courseId"));
		String baseDir = super.getBaseDir(request);
		String courseId = request.getParameter("courseId");
		if (StringUtils.isBlank(courseId)) {
			courseId = (String) session.getAttribute("courseId");
		}
		baseDir += courseId + File.separator;
		return baseDir;
	}

	@Override
	public String getBaseUrl(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String baseUrl = super.getBaseUrl(request);
		String courseId = request.getParameter("courseId");
		if (StringUtils.isBlank(courseId)) {
			courseId = (String) session.getAttribute("courseId");
		}
		String port = "";
		if(request.getServerPort() != 80){
			port = ":"+request.getServerPort();
		}
		baseUrl = request.getScheme()+"://"+request.getServerName()+ port + baseUrl + courseId;
		return PathUtils.addSlashToBeginning(PathUtils.addSlashToEnd(baseUrl));
	}

}
