/**
 * 
 */
package org.sakaiproject.cmsrest.logic;

import java.util.List;

import org.sakaiproject.site.api.Site;

/**
 * @author yushanyuan
 *
 */
public interface AutoNewCourseSite {


	/**
	 * 创建课程站点
	 * @param sessionEid
	 * @param sessionTitle
	 * @param toolIds
	 * @param sectionEid
	 */
	public Site  newSite(String sessionEid, String sessionTitle, List<String> toolIds, String sectionEid) throws Exception;
	
	
	public void openSession();
	
	public void closeSession();
}
