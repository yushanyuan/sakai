/**
 * 版权所有 北京思开科技有限公司 
 * All Rights Reserved
 */
package cn.net.sikai.cc;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdom.Element;
import org.sakaiproject.content.api.ContentCollection;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.exception.PermissionException;
import org.sakaiproject.exception.TypeException;

import cn.net.sikai.model.SimplePage;
import cn.net.sikai.web.ZipController;

/**
 * @Description: TODO
 * @author: yushanyuan
 *
 */
public class DefaultHandler {

	MeleteLoader utils = null;

	private String title = null;
	private String description = null;
	private String baseName = null;
	private String baseUrl = null;
	private String toolId = null;
	boolean importtop = false;
	Element manifestXml = null;


	Ns ns = null;
	ZipController zipController = null;

	public ZipController getZipController() {
		return zipController;
	}

	public void setManifestXml(Element the_xml) {
		manifestXml = the_xml;
	}

	public void setNs(Ns n) {
		ns = n;
	}

	public Ns getNs() {
		return ns;
	}

	public DefaultHandler(ZipController bean, MeleteLoader utils, String toolId) {
		super();
		this.utils = utils;
		this.zipController = bean;
		this.toolId = toolId;
	}
	
	public void setManifestMetadataXml(Element the_md) {
	      ContentCollection baseCollection = makeBaseFolder(null);
	      baseName = baseCollection.getId();
	      baseUrl = baseCollection.getUrl();
	      // kill the hostname part. We want to use relative URLs
	      /*int relPart = baseUrl.indexOf("/access/");
	      if (relPart >= 0)
		  baseUrl = baseUrl.substring(relPart);
*/
	  }

	public Long checkMainPage(){
		
		//检查是否已经有lb的主页了。
		//String siteId = zipController.getCurrentSiteId();
		Long mainPageId = zipController.getMainPageIdByToolId(toolId);
		//没有页面，创建主页
			//TODO
			//simplePageToolDao.makePage(toolId, siteId, "lessonBuilder", null, null);
		return mainPageId;
	}
	
	public ContentCollection makeBaseFolder(String name){
		String siteId = zipController.getSiteIdByToolId(toolId);
		return zipController.makeBaseFolder(name , siteId);
	}
	
	
	
	
	public SimplePage addPage(Long mainPageId, Long parentId, String title)  {
		//String siteId = zipController.getCurrentSiteId();
		//String toolId = zipController.getToolIdBySiteId(siteId);
		String siteId = zipController.getSiteIdByToolId(toolId);
		SimplePage page = new SimplePage(toolId, siteId, title, parentId, mainPageId);//simplePageToolDao.makePage(pages.get(0).getToolId(), siteId, title, parentId, mainPageId);
		SimplePage dbPage = zipController.savePage(page);
		
		return dbPage;
	}
	 
	public void insertItemByType5(Long pageId, String html){
		zipController.insertItemByType5(pageId, html);
	}
	
	public void insertItemByType2(Long pageId, String sakaiId, String name){
		zipController.insertItemByType2(pageId, sakaiId, name);
	}
	
	public void insertItemByType1(Long pageId, String sakaiId, String name,String html){
		zipController.insertItemByType1(pageId, sakaiId, name, html);
	}
	
	public String addFile(String the_file_id, InputStream infile) throws PermissionException, IdUnusedException, TypeException {
		return zipController.addFile(baseName, the_file_id, infile);
	}

	public void endManifest() {
	}

}
