/**
 * 版权所有 北京思开科技有限公司 
 * All Rights Reserved
 */
package cn.net.sikai.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.sakaiproject.content.api.ContentCollection;
import org.sakaiproject.content.api.ContentCollectionEdit;
import org.sakaiproject.content.api.ContentHostingService;
import org.sakaiproject.content.api.ContentResource;
import org.sakaiproject.content.api.ContentResourceEdit;
import org.sakaiproject.content.api.ContentTypeImageService;
import org.sakaiproject.entity.api.ResourceProperties;
import org.sakaiproject.event.api.NotificationService;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.exception.IdUsedException;
import org.sakaiproject.exception.PermissionException;
import org.sakaiproject.exception.TypeException;
import org.sakaiproject.tool.api.Session;
import org.sakaiproject.tool.api.SessionManager;
import org.sakaiproject.tool.api.ToolManager;
import org.sakaiproject.tool.api.ToolSession;
import org.sakaiproject.util.Validator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cn.net.sikai.cc.DefaultHandler;
import cn.net.sikai.cc.MeleteLoader;
import cn.net.sikai.cc.Parser;
import cn.net.sikai.cc.ZipLoader;
import cn.net.sikai.model.SimplePage;
import cn.net.sikai.service.LessonBuilderHelperDao;

/**
 * @Description: TODO
 * @author: yushanyuan
 *
 */
@Controller
@RequestMapping("/zip")
public class ZipController {


	@Resource(name = "org.sakaiproject.tool.api.SessionManager")
	private SessionManager sessionManager;

	@Resource(name = "org.sakaiproject.tool.api.ToolManager")
	private ToolManager toolManager;

	@Resource(name = "org.sakaiproject.content.api.ContentHostingService")
	private ContentHostingService contentHostingService;

	@Resource(name = "org.sakaiproject.content.api.ContentTypeImageService")
	private ContentTypeImageService contentTypeImageService;

	@Resource(name = "cn.net.sikai.service.LessonBuilderHelperDao")
	private LessonBuilderHelperDao lessonBuilderHelperDao;

	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	public void setContentHostingService(ContentHostingService contentHostingService) {
		this.contentHostingService = contentHostingService;
	}

	public void setToolManager(ToolManager toolManager) {
		this.toolManager = toolManager;
	}


	public LessonBuilderHelperDao getLessonBuilderHelperDao() {
		return lessonBuilderHelperDao;
	}

	public void setLessonBuilderHelperDao(LessonBuilderHelperDao lessonBuilderHelperDao) {
		this.lessonBuilderHelperDao = lessonBuilderHelperDao;
	}

	public void setContentTypeImageService(ContentTypeImageService contentTypeImageService) {
		this.contentTypeImageService = contentTypeImageService;
	}

	// -----------------------------
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Model model, @RequestParam("toolId") String toolId) throws IOException {
		model.addAttribute("toolId", toolId);
		return "import";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Model model, @RequestParam("toolId") String toolId, @RequestParam("zip") MultipartFile zip) {

		if (zip.getOriginalFilename() != null && !zip.isEmpty()) {
			File cc = null;
			File root = null;
			try {
				cc = File.createTempFile("ccloader", "file");
				root = File.createTempFile("ccloader", "root");
				if (root.exists()) {
					if (!root.delete()) {
						setErrMessage("unable to delete temp file for load");
						return "success";
					}
				}
				if (!root.mkdir()) {
					setErrMessage("unable to create temp directory for load");
					return "success";
				}
				BufferedInputStream bis = new BufferedInputStream(zip.getInputStream());
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(cc));
				byte[] buffer = new byte[8096];
				int n = 0;
				while ((n = bis.read(buffer, 0, 8096)) >= 0) {
					if (n > 0)
						bos.write(buffer, 0, n);
				}
				bis.close();
				bos.close();

				MeleteLoader meleteLoader = ZipLoader.getUtilities(cc, root.getCanonicalPath());
				Parser parser = Parser.createMeleteParser(meleteLoader);
				String pageId = findPageIdByTooIdInSakaiSiteTool(toolId);
				parser.parse(new DefaultHandler(this, meleteLoader, pageId));
			} catch (Exception e) {
				setErrMessage("simplepage.cc-error");

				e.printStackTrace();
			} finally {
				if (cc != null)
					try {
						deleteRecursive(cc);
					} catch (Exception e) {

					}
				try {
					deleteRecursive(root);
				} catch (Exception e) {

				}
			}
		}

		return "success";
	}

	/*public String getCurrentSiteId() {
		String currentSiteId = null;
		if (currentSiteId != null)
			return currentSiteId;
		try {
			currentSiteId = toolManager.getCurrentPlacement().getContext();
			return currentSiteId;
		} catch (Exception impossible) {
			return null;
		}
	}*/

	public void setErrMessage(String s) {
		ToolSession toolSession = sessionManager.getCurrentToolSession();
		if (toolSession == null) {
			System.out.println("Lesson Builder error not in tool: " + s);
			return;
		}
		List<String> errors = (List<String>) toolSession.getAttribute("lessonbuilder.errors");
		if (errors == null)
			errors = new ArrayList<String>();
		errors.add(s);
		toolSession.setAttribute("lessonbuilder.errors", errors);
	}

	public boolean deleteRecursive(File path) throws FileNotFoundException {
		if (!path.exists())
			throw new FileNotFoundException(path.getAbsolutePath());
		boolean ret = true;
		if (path.isDirectory()) {
			for (File f : path.listFiles()) {
				ret = ret && deleteRecursive(f);
			}
		}
		return ret && path.delete();
	}

	public String findPageIdByTooIdInSakaiSiteTool(String toolId){
		
		String pageId = null;
		try {
			pageId = lessonBuilderHelperDao.findPageIdByTooIdInSakaiSiteTool(toolId);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return pageId;
		
	}
	public Long getMainPageIdBySiteId(String siteId) {

		Long pageId = null;
		try {
			pageId = lessonBuilderHelperDao.getMainPageIdBySiteId(siteId);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return pageId;
	}
	public Long getMainPageIdByToolId(String toolId) {

		Long pageId = null;
		try {
			pageId = lessonBuilderHelperDao.getMainPageIdByToolId(toolId);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return pageId;
	}
	public String getToolIdBySiteId(String siteId){
		String toolId = null;
		try {
			toolId = lessonBuilderHelperDao.getToolIdBySiteId(siteId);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return toolId;
	}
	
	public String getSiteIdByToolId(String toolId){
		String siteId = null;
		try {
			siteId = lessonBuilderHelperDao.getSiteIdByToolId(toolId);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return siteId;
	}
	

	public ContentCollection makeBaseFolder(String name, String siteId) {
		if (name == null) {
			name = "meleteoldrec";
		}
		Session session = sessionManager.getCurrentSession();
		session.setUserId("admin");

		StringBuffer newname = new StringBuffer(contentHostingService.getSiteCollection(siteId));
		newname.append(name);
		name = newname.toString();
		
		try {
			ContentCollection c = contentHostingService.getCollection(name + "/");
			sessionManager.getCurrentSession().invalidate();
			return c;
		} catch (IdUnusedException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
		} catch (TypeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (PermissionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		
		
		ContentCollectionEdit collection = null;
		int tries = 1;
		for (; tries <= 10; tries++) {
			try {
				collection = contentHostingService.addCollection(name + "/"); // append
																				// /
																				// here
																				// because
																				// we
																				// may
																				// hack
																				// on
																				// the
																				// name

				String display = name;
				int main = name.lastIndexOf("/");
				if (main >= 0)
					display = display.substring(main + 1);
				collection.getPropertiesEdit().addProperty(ResourceProperties.PROP_DISPLAY_NAME, display);

				contentHostingService.commitCollection(collection);
				break; // got it
			} catch (IdUsedException e) {
				name = name + "-" + tries;
			} catch (Exception e) {
				return null;
			} 
		}
		
		sessionManager.getCurrentSession().invalidate();
		return collection;
	}

	public String addFile(String baseName, String the_file_id, InputStream infile) {

		ContentResource ref;
		Session session = sessionManager.getCurrentSession();
		session.setUserId("admin");
		
		try {
			ref = contentHostingService.getResource(baseName + the_file_id);
			sessionManager.getCurrentSession().invalidate();
			return ref.getUrl();
		} catch (PermissionException e2) {
			e2.printStackTrace();
		} catch (IdUnusedException e2) {
			//e2.printStackTrace();
		} catch (TypeException e2) {
			e2.printStackTrace();
		}
		// TODO 检查资源是否已经上传
		for (int tries = 1; tries < 3; tries++) {
			try {
				String name = the_file_id;
				int slash = the_file_id.lastIndexOf("/");
				if (slash >= 0)
					name = name.substring(slash + 1);
				String extension = Validator.getFileExtension(name);
				String type = contentTypeImageService.getContentType(extension);

				ContentResourceEdit edit = contentHostingService.addResource(baseName + the_file_id);

				edit.setContentType(type);
				edit.setContent(infile);
				edit.getPropertiesEdit().addProperty(ResourceProperties.PROP_DISPLAY_NAME, name);
				contentHostingService.commitResource(edit, NotificationService.NOTI_NONE);
				return contentHostingService.getResource(baseName + the_file_id).getUrl();
			
			} catch (IdUsedException e) {
				// remove existing if we are importing whole site.
				// otherwise this is an error (and should be impossible, as this
				// is a new directory)
				if (tries == 1) {
					try {
						contentHostingService.removeResource(baseName + the_file_id);
						continue;
					} catch (Exception e1) {
					}
				}
				System.out.println("CC loader: unable to get file " + the_file_id + " error: " + e);
			} catch (Exception e) {
				System.out.println("CC loader: unable to get file " + the_file_id + " error: " + e);
			}
			break; // if we get to the end, no need to retry; really a goto
					// would be clearer
		}
		sessionManager.getCurrentSession().invalidate();
		return null;
	}

	public SimplePage savePage(SimplePage page) {
		SimplePage dbPage = null;
		try {
			dbPage = lessonBuilderHelperDao.savePage(page);
		} catch (SQLException e) {
			setErrMessage("page.savePageerror");
			e.printStackTrace();
		}
		return dbPage;
	}

	public void insertItemByType1(Long pageId, String sakaiId, String name, String html) {
		try {
			lessonBuilderHelperDao.insertItemByType1(pageId, sakaiId, name, html);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void insertItemByType2(Long pageId, String sakaiId, String name) {
		try {
			lessonBuilderHelperDao.insertItemByType2(pageId, sakaiId, name);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void insertItemByType5(Long pageId, String html) {
		try {
			lessonBuilderHelperDao.insertItemByType5(pageId, html);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	 * public boolean saveItem(Object i, boolean requiresEditPermission) {
	 * String err = null; List<String>elist = new ArrayList<String>();
	 * 
	 * try { simplePageToolDao.saveItem(i, elist, "page.nowrite",
	 * requiresEditPermission); } catch (Throwable t) { // this is probably a
	 * bogus error, but find its root cause while (t.getCause() != null) { t =
	 * t.getCause(); } err = t.toString(); }
	 * 
	 * // if we got an error from saveItem use it instead if (elist.size() > 0)
	 * err = elist.get(0); if (err != null) { setErrMessage("page.savefailed" +
	 * err); return false; }
	 * 
	 * return true; }
	 * 
	 * public boolean saveItem(Object i) { return saveItem(i, true); }
	 * 
	 * public boolean update(Object i) { return update(i, true); }
	 * 
	 * // see notes for saveupdate
	 * 
	 * // requiresEditPermission determines whether simplePageToolDao should
	 * confirm // edit permissions before making the update boolean
	 * update(Object i, boolean requiresEditPermission) { String err = null;
	 * List<String>elist = new ArrayList<String>(); try {
	 * simplePageToolDao.update(i, elist, "nowrite", requiresEditPermission); }
	 * catch (Throwable t) { // this is probably a bogus error, but find its
	 * root cause while (t.getCause() != null) { t = t.getCause(); } err =
	 * t.toString(); } // if we got an error from saveItem use it instead if
	 * (elist.size() > 0) err = elist.get(0); if (err != null) { setErrMessage(
	 * ""); return false; }
	 * 
	 * return true; }
	 */
}
