package org.sakaiproject.resource.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class CharsetEncodingFilter implements Filter {
	private String defaultEncode = "UTF-8";

	/**
	 * <p>
	 * 获得Web.xml文件中配置的字符编码字符串[charset],默认为‘UTF-8’
	 * </p>
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig config) throws ServletException {
		if (config.getInitParameter("charset") != null) {
			defaultEncode = config.getInitParameter("charset");
		}
	}

	public void destroy() {
	}

	/**
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		ServletRequest srequest = request;
		srequest.setCharacterEncoding(defaultEncode);
		response.setCharacterEncoding(defaultEncode);
		chain.doFilter(srequest, response);
	}
}
