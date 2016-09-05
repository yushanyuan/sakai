/**
 * 
 */
package org.sakaiproject.resource.util;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.dispatcher.FilterDispatcher;

/**
 * 用于公告发布功能照片上传的过滤器 继承struts的FilterDispatcher
 * @author zhangxin
 *
 */
public class CKFinderFilter extends FilterDispatcher{

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest request = (HttpServletRequest) req;    
        String URI = request.getRequestURI();   
        String[] uriArray = URI.split("/resource/scripts/ckfinder/core/connector/java/*/");   
        int arrayLen = uriArray.length;   
        if (arrayLen >= 2) {   
            chain.doFilter(req, res);    
        }else {   
            super.doFilter(req, res, chain);     
        } 
	}

	
}
