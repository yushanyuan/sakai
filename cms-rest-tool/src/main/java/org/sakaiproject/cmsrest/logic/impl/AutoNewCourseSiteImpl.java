/**
 * 
 */
package org.sakaiproject.cmsrest.logic.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.authz.api.AuthzGroup;
import org.sakaiproject.authz.api.AuthzPermissionException;
import org.sakaiproject.authz.api.GroupNotDefinedException;
import org.sakaiproject.authz.api.AuthzGroupService;
import org.sakaiproject.authz.api.GroupProvider;
import org.sakaiproject.cmsrest.logic.AutoNewCourseSite;
import org.sakaiproject.component.cover.ServerConfigurationService;
import org.sakaiproject.content.api.ContentHostingService;
import org.sakaiproject.coursemanagement.api.AcademicSession;
import org.sakaiproject.coursemanagement.api.CourseManagementService;
import org.sakaiproject.coursemanagement.api.Section;
import org.sakaiproject.entity.api.EntityProducer;
import org.sakaiproject.entity.api.EntityTransferrer;
import org.sakaiproject.entity.api.EntityTransferrerRefMigrator;
import org.sakaiproject.entity.api.ResourceProperties;
import org.sakaiproject.entity.api.ResourcePropertiesEdit;
import org.sakaiproject.entity.api.EntityManager;
import org.sakaiproject.event.api.SessionState;
import org.sakaiproject.exception.IdInvalidException;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.exception.IdUsedException;
import org.sakaiproject.exception.PermissionException;
import org.sakaiproject.id.api.IdManager;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SitePage;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.site.api.ToolConfiguration;
import org.sakaiproject.site.util.SiteTypeUtil;
import org.sakaiproject.sitemanage.api.SectionFieldProvider;
import org.sakaiproject.tool.api.Session;
import org.sakaiproject.tool.api.SessionManager;
import org.sakaiproject.tool.api.Tool;
import org.sakaiproject.tool.api.ToolManager;
import org.sakaiproject.user.api.User;
import org.sakaiproject.user.api.UserDirectoryService;
import org.sakaiproject.util.ArrayUtil;
import org.sakaiproject.util.BaseResourcePropertiesEdit;
import org.sakaiproject.util.ResourceLoader;

/**
 * @author yushanyuan
 *
 */
public class AutoNewCourseSiteImpl implements AutoNewCourseSite {

	final static Log log = LogFactory.getLog(AutoNewCourseSiteImpl.class);
	/** Name of state attribute for Site instance id */
	private static final String STATE_SITE_INSTANCE_ID = "site.instance.id";

	/** Name of state attribute for Site Information */
	private static final String STATE_SITE_INFO = "site.info";
	private static final String STATE_SITE_TYPE = "site-type";

	private final static String PROP_SITE_LANGUAGE = "locale_string";
	/** The null/empty string */
	private static final String NULL_STRING = "";

	// SAK-23468
	private static final String STATE_NEW_SITE_STATUS_ISPUBLISHED = "newSiteStatusIsPublished";
	private static final String STATE_NEW_SITE_STATUS_TITLE = "newSiteStatusTitle";
	private static final String STATE_NEW_SITE_STATUS_ID = "newSiteStatusID";
	private static final String STATE_DEFAULT_SITE_TYPE = "default_site_type";
	private final static int UUID_LENGTH = 36;

	/** State attributes for using templates in site creation. */
	private static final String STATE_TEMPLATE_SITE = "site.templateSite";
	private static final String STATE_TEMPLATE_SITE_COPY_USERS = "site.templateSiteCopyUsers";
	private static final String STATE_TEMPLATE_SITE_COPY_CONTENT = "site.templateSiteCopyContent";
	private static final String STATE_TEMPLATE_PUBLISH = "site.templateSitePublish";

	// synoptic tool ids
	private static final String TOOL_ID_SUMMARY_CALENDAR = "sakai.summary.calendar";
	private static final String TOOL_ID_SYNOPTIC_ANNOUNCEMENT = "sakai.synoptic.announcement";
	private static final String TOOL_ID_SYNOPTIC_CHAT = "sakai.synoptic.chat";
	private static final String TOOL_ID_SYNOPTIC_MESSAGECENTER = "sakai.synoptic.messagecenter";
	private static final String TOOL_ID_SYNOPTIC_DISCUSSION = "sakai.synoptic.discussion";
	// home tool id
	private static final String TOOL_ID_HOME = "home";

	// Site Info tool id
	private static final String TOOL_ID_SITEINFO = "sakai.siteinfo";

	private static final String IMPORT_QUEUED = "import.queued";

	private static final String STATE_TOOL_EMAIL_ADDRESS = "toolEmailAddress";

	private static final String STATE_PROJECT_TOOL_LIST = "projectToolList";

	private final static String STATE_MULTIPLE_TOOL_ID_SET = "multipleToolIdSet";
	private final static String STATE_MULTIPLE_TOOL_ID_TITLE_MAP = "multipleToolIdTitleMap";
	private final static String STATE_MULTIPLE_TOOL_CONFIGURATION = "multipleToolConfiguration";

	private static final String STATE_TOOL_REGISTRATION_SELECTED_LIST = "toolRegistrationSelectedList";

	/** for course information */
	private final static String STATE_TERM_COURSE_LIST = "state_term_course_list";

	private final static String STATE_TERM_COURSE_HASH = "state_term_course_hash";

	private final static String STATE_TERM_SELECTED = "state_term_selected";

	private final static String STATE_INSTRUCTOR_SELECTED = "state_instructor_selected";

	private final static String STATE_FUTURE_TERM_SELECTED = "state_future_term_selected";

	private final static String STATE_ADD_CLASS_PROVIDER = "state_add_class_provider";

	private final static String STATE_ADD_CLASS_PROVIDER_CHOSEN = "state_add_class_provider_chosen";

	private final static String STATE_ADD_CLASS_PROVIDER_DESCRIPTION_CHOSEN = "state_add_class_provider_description_chosen";

	private final static String STATE_ADD_CLASS_MANUAL = "state_add_class_manual";

	private final static String STATE_AUTO_ADD = "state_auto_add";

	private final static String STATE_MANUAL_ADD_COURSE_NUMBER = "state_manual_add_course_number";

	private final static String STATE_MANUAL_ADD_COURSE_FIELDS = "state_manual_add_course_fields";

	public final static String PROP_SITE_REQUEST_COURSE = "site-request-course-sections";

	public final static String SITE_PROVIDER_COURSE_LIST = "site_provider_course_list";

	public final static String SITE_MANUAL_COURSE_LIST = "site_manual_course_list";

	private final static String STATE_SUBJECT_AFFILIATES = "site.subject.affiliates";

	private final static String STATE_ICONS = "icons";

	public static final String SITE_DUPLICATED = "site_duplicated";

	public static final String SITE_DUPLICATED_NAME = "site_duplicated_named";

	// types of site whose title can not be editable
	public static final String TITLE_NOT_EDITABLE_SITE_TYPE = "title_not_editable_site_type";

	// maximum length of a site title
	private static final String STATE_SITE_TITLE_MAX = "site_title_max_length";

	// types of site where site view roster permission is editable
	public static final String EDIT_VIEW_ROSTER_SITE_TYPE = "edit_view_roster_site_type";

	// htripath : for import material from file - classic import
	private static final String ALL_ZIP_IMPORT_SITES = "allzipImports";

	private static final String FINAL_ZIP_IMPORT_SITES = "finalzipImports";

	private static final String DIRECT_ZIP_IMPORT_SITES = "directzipImports";

	private static final String CLASSIC_ZIP_FILE_NAME = "classicZipFileName";

	private static final String SESSION_CONTEXT_ID = "sessionContextId";

	// page size for worksite setup tool
	private static final String STATE_PAGESIZE_SITESETUP = "state_pagesize_sitesetup";

	// page size for site info tool
	private static final String STATE_PAGESIZE_SITEINFO = "state_pagesize_siteinfo";

	private static final String IMPORT_DATA_SOURCE = "import_data_source";

	// Special tool id for Home page
	private static final String SITE_INFORMATION_TOOL = "sakai.iframe.site";

	private static final String STATE_CM_LEVELS = "site.cm.levels";

	private static final String STATE_CM_LEVEL_OPTS = "site.cm.level_opts";

	private static final String STATE_CM_LEVEL_SELECTIONS = "site.cm.level.selections";

	private static final String STATE_CM_SELECTED_SECTION = "site.cm.selectedSection";

	private static final String STATE_CM_REQUESTED_SECTIONS = "site.cm.requested";

	private static final String STATE_CM_SELECTED_SECTIONS = "site.cm.selectedSections";

	private static final String STATE_PROVIDER_SECTION_LIST = "site_provider_section_list";

	private static final String STATE_CM_CURRENT_USERID = "site_cm_current_userId";

	private static final String STATE_CM_AUTHORIZER_LIST = "site_cm_authorizer_list";

	private static final String STATE_CM_AUTHORIZER_SECTIONS = "site_cm_authorizer_sections";

	// map of synoptic tool and the related tool ids
	private final static Map<String, List<String>> SYNOPTIC_TOOL_ID_MAP;
	static {
		SYNOPTIC_TOOL_ID_MAP = new HashMap<String, List<String>>();
		SYNOPTIC_TOOL_ID_MAP.put(TOOL_ID_SUMMARY_CALENDAR,
				new ArrayList(Arrays.asList("sakai.schedule")));
		SYNOPTIC_TOOL_ID_MAP.put(TOOL_ID_SYNOPTIC_ANNOUNCEMENT, new ArrayList(
				Arrays.asList("sakai.announcements")));
		SYNOPTIC_TOOL_ID_MAP.put(TOOL_ID_SYNOPTIC_CHAT,
				new ArrayList(Arrays.asList("sakai.chat")));
		SYNOPTIC_TOOL_ID_MAP.put(
				TOOL_ID_SYNOPTIC_MESSAGECENTER,
				new ArrayList(Arrays.asList("sakai.messages", "sakai.forums",
						"sakai.messagecenter")));
		SYNOPTIC_TOOL_ID_MAP.put(TOOL_ID_SYNOPTIC_DISCUSSION, new ArrayList(
				Arrays.asList("sakai.discussion")));
	}

	// map of synoptic tool and message bundle properties, used to lookup an
	// internationalized tool title
	private final static Map<String, String> SYNOPTIC_TOOL_TITLE_MAP;
	static {
		SYNOPTIC_TOOL_TITLE_MAP = new HashMap<String, String>();
		SYNOPTIC_TOOL_TITLE_MAP.put(TOOL_ID_SUMMARY_CALENDAR, "java.reccal");
		SYNOPTIC_TOOL_TITLE_MAP.put(TOOL_ID_SYNOPTIC_ANNOUNCEMENT,
				"java.recann");
		SYNOPTIC_TOOL_TITLE_MAP.put(TOOL_ID_SYNOPTIC_CHAT, "java.recent");
		SYNOPTIC_TOOL_TITLE_MAP.put(TOOL_ID_SYNOPTIC_MESSAGECENTER,
				"java.recmsg");
		SYNOPTIC_TOOL_TITLE_MAP
				.put(TOOL_ID_SYNOPTIC_DISCUSSION, "java.recdisc");
	}

	/** the web content tool id **/
	private final static String WEB_CONTENT_TOOL_ID = "sakai.iframe";
	private final static String SITE_INFO_TOOL_ID = "sakai.iframe.site";
	private final static String WEB_CONTENT_TOOL_SOURCE_CONFIG = "source";
	private final static String WEB_CONTENT_TOOL_SOURCE_CONFIG_VALUE = "http://";

	private static final String SITE_MODE_SITESETUP = "sitesetup";

	private static final String SITE_MODE_SITEINFO = "siteinfo";

	private static final String SITE_MODE_HELPER = "helper";

	private static final String SITE_MODE_HELPER_DONE = "helper.done";

	private static final String STATE_SITE_MODE = "site_mode";

	private static final String TERM_OPTION_ALL = "-1";

	/** portlet configuration parameter values* */
	/** Resource bundle using current language locale */
	private static ResourceLoader rb = new ResourceLoader("sitesetupgeneric");
	private static ResourceLoader cfgRb = new ResourceLoader("multipletools");

	// -------------------------------------------

	private IdManager idManager;
	private SiteService siteService;

	private ToolManager toolManager;
	private EntityManager entityManager;
	private UserDirectoryService userDirectoryService;
	private ContentHostingService m_contentHostingService;// (ContentHostingService)
															// ComponentManager.get("org.sakaiproject.content.api.ContentHostingService");
	private CourseManagementService cmService;
	private AuthzGroupService authzGroupService;
	private GroupProvider groupProvider;
	private SectionFieldProvider sectionFieldProvider;
	private SessionManager sessionManager;

	public void setIdManager(IdManager idManager) {
		this.idManager = idManager;
	}

	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	public void setToolManager(ToolManager toolManager) {
		this.toolManager = toolManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void setUserDirectoryService(
			UserDirectoryService userDirectoryService) {
		this.userDirectoryService = userDirectoryService;
	}

	public void setM_contentHostingService(
			ContentHostingService m_contentHostingService) {
		this.m_contentHostingService = m_contentHostingService;
	}

	public void setCmService(CourseManagementService cmService) {
		this.cmService = cmService;
	}

	public void setAuthzGroupService(AuthzGroupService authzGroupService) {
		this.authzGroupService = authzGroupService;
	}

	public void setGroupProvider(GroupProvider groupProvider) {
		this.groupProvider = groupProvider;
	}

	public void setSectionFieldProvider(
			SectionFieldProvider sectionFieldProvider) {
		this.sectionFieldProvider = sectionFieldProvider;
	}

	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	// -------------------------------------------------

	public void init() {
		log.info("init");
	}

	@Override
	public Site newSite(String sessionEid, String sessionTitle,
			List<String> toolIds, String sectionEid)  throws Exception{
		openSession();

		Site site = doFinish(sessionEid, sessionTitle, toolIds, sectionEid);

		//closeSession();
		return site;
	}

	private Site doFinish(String sessionEid, String sessionTitle,
			List<String> toolIds, String sectionEid) throws IdInvalidException, IdUsedException, PermissionException {

		Map state = new HashMap();

		state.put("TITLE", sessionTitle);// 2015chun
		state.put("TERM", sessionEid);// 2015spring
		state.put("SITE_TYPE", "course");// course
		state.put("CONTACT_NAME", "");

		state.put(STATE_TERM_SELECTED, sessionEid);

		// [sakai.lessonbuildertool, sakai.iframe, sakai.basiclti]
		Set<String> multiToolIdSet = new HashSet<String>();
		multiToolIdSet.add("sakai.lessonbuildertool");
		multiToolIdSet.add("sakai.iframe");
		multiToolIdSet.add("sakai.basiclti");
		state.put(STATE_MULTIPLE_TOOL_ID_SET, multiToolIdSet);

		// [home, sakai.site.roster2, sakai.sections, sakai.siteinfo]
		List<String> chosenList = new ArrayList<String>();
		chosenList.add("home");
		// chosenList.add("sakai.site.roster2");
		// chosenList.add("sakai.sections");
		chosenList.add("sakai.siteinfo");

		if (toolIds != null && toolIds.size() > 0) {
			for (String toolId : toolIds) {
				chosenList.add(toolId);
			}
		}

		state.put(STATE_TOOL_REGISTRATION_SELECTED_LIST, chosenList);

		state.put(STATE_SITE_TYPE, "course");

		// sitesetup
		state.put(STATE_SITE_MODE, "sitesetup");

		// 课程实例的eid
		List<String> sectionEids = new ArrayList<String>();
		if(sectionEid!=null){
			sectionEids.add(sectionEid);
		}
		state.put(STATE_CM_REQUESTED_SECTIONS, sectionEids);

		List<String> providerSectionList = new ArrayList<String>();
		providerSectionList.add(sectionEid);
		state.put(STATE_ADD_CLASS_PROVIDER_CHOSEN, providerSectionList);
		
		addNewSite(state);
		Site site = getStateSite(state);
		// SAK-23468 Add new site params to state
		setNewSiteStateParameters(site, state);

		saveFeatures(state, site);

		ResourcePropertiesEdit rp = site.getPropertiesEdit();

		// for course sites
		String siteType = site.getType();
		if (SiteTypeUtil.isCourseSite(siteType)) {
			AcademicSession term = null;
			if (state.get(STATE_TERM_SELECTED) != null) {
				term = cmService.getAcademicSession(state.get(
						STATE_TERM_SELECTED).toString());
				rp.addProperty(Site.PROP_SITE_TERM, term.getTitle());
				rp.addProperty(Site.PROP_SITE_TERM_EID, term.getEid());
			}

			// update the site and related realm based on the rosters chosen or
			// requested
			updateCourseSiteSections(state, site.getId(), rp, term);
		}

		// commit site
		commitSite(site);

		return site;
	}

	private void saveFeatures(Map state, Site site) {

		String siteType = checkNullSiteType(state, site.getType(), site.getId());

		List wSetupPageList = new Vector();
		Set multipleToolIdSet = null;
		Map multipleToolIdTitleMap = new HashMap();

		WorksiteSetupPage wSetupPage = new WorksiteSetupPage();
		WorksiteSetupPage wSetupHome = new WorksiteSetupPage();

		List pageList = new Vector();
		// declare some flags used in making decisions about Home, whether to
		// add, remove, or do nothing
		boolean hasHome = false;
		String homePageId = null;
		boolean homeInWSetupPageList = false;

		List chosenList = (List) state
				.get(STATE_TOOL_REGISTRATION_SELECTED_LIST);
		boolean hasEmail = false;
		boolean hasSiteInfo = true;

		// tools to be imported from other sites?
		Hashtable importTools = null;

		hasHome = true;
		// order the id list
		chosenList = orderToolIds(state, siteType, chosenList, false);

		if (hasHome) {
			SitePage page = site.getPage(homePageId);

			if (!homeInWSetupPageList) {
				// if Home is chosen and Home is not in wSetupPageList, add Home
				// to site and wSetupPageList
				page = site.addPage();

				// page.setTitle(rb.getString("java.home"));
				page.setTitle("主页");

				wSetupHome.pageId = page.getId();
				wSetupHome.pageTitle = page.getTitle();
				wSetupHome.toolId = TOOL_ID_HOME;
				wSetupPageList.add(wSetupHome);
			}
			// the list tools on the home page
			List<ToolConfiguration> toolList = page.getTools();
			// get tool id set for Home page from configuration
			List<String> homeToolIds = getHomeToolIds(state,
					!homeInWSetupPageList, page);

			// count
			int nonSynopticToolIndex = 0, synopticToolIndex = 0;

			for (String homeToolId : homeToolIds) {
				if (!SYNOPTIC_TOOL_ID_MAP.containsKey(homeToolId)) {
					if (!pageHasToolId(toolList, homeToolId)) {
						// not a synoptic tool and is not in Home page yet, just
						// add it
						Tool reg = toolManager.getTool(homeToolId);
						if (reg != null) {
							ToolConfiguration tool = page.addTool();
							tool.setTool(homeToolId, reg);
							tool.setTitle(reg.getTitle() != null ? reg
									.getTitle() : "");
							tool.setLayoutHints("0," + nonSynopticToolIndex++);
						}
					}
				} else {
					// synoptic tool
					List<String> parentToolList = (List<String>) SYNOPTIC_TOOL_ID_MAP
							.get(homeToolId);
					List chosenListClone = new Vector();
					// chosenlist may have things like
					// bcf89cd4-fa3a-4dda-80bd-ed0b89981ce7sakai.chat
					// get list of the actual tool names
					List<String> chosenOrigToolList = new ArrayList<String>();
					for (String chosenTool : (List<String>) chosenList)
						chosenOrigToolList.add(findOriginalToolId(state,
								chosenTool));
					chosenListClone.addAll(chosenOrigToolList);
					boolean hasAnyParentToolId = chosenListClone
							.removeAll(parentToolList);

					// first check whether the parent tool is available in site
					// but its parent tool is no longer selected
					if (pageHasToolId(toolList, homeToolId)) {
						if (!hasAnyParentToolId
								&& !siteService.isUserSite(site.getId())) {
							for (ListIterator iToolList = toolList
									.listIterator(); iToolList.hasNext();) {
								ToolConfiguration tConf = (ToolConfiguration) iToolList
										.next();
								// avoid NPE when the tool definition is missing
								if (tConf.getTool() != null
										&& homeToolId.equals(tConf.getTool()
												.getId())) {
									page.removeTool((ToolConfiguration) tConf);
									break;
								}
							}
						} else {
							synopticToolIndex++;
						}
					}

					// then add those synoptic tools which wasn't there before
					if (!pageHasToolId(toolList, homeToolId)
							&& hasAnyParentToolId) {
						try {
							// use value from map to find an internationalized
							// tool title
							String toolTitleText = rb
									.getString(SYNOPTIC_TOOL_TITLE_MAP
											.get(homeToolId));
							addSynopticTool(page, homeToolId, toolTitleText,
									synopticToolIndex + ",1",
									synopticToolIndex++);
						} catch (Exception e) {
							log.warn(
									this + ".saveFeatures addSynotpicTool: "
											+ e.getMessage() + " site id = "
											+ site.getId() + " tool = "
											+ homeToolId, e);
						}
					}

				}
			}

			if (page.getTools().size() == 1) {
				// only use one column layout
				page.setLayout(SitePage.LAYOUT_SINGLE_COL);
			}

			// mark this page as Home page inside its property
			if (page.getProperties().getProperty(SitePage.IS_HOME_PAGE) == null) {
				page.getPropertiesEdit().addProperty(SitePage.IS_HOME_PAGE,
						Boolean.TRUE.toString());
			}

		} // add Home

		// declare flags used in making decisions about whether to add, remove,
		// or do nothing
		boolean inChosenList;
		boolean inWSetupPageList;

		Set categories = new HashSet();
		categories.add((String) state.get(STATE_SITE_TYPE));
		Set toolRegistrationSet = toolManager.findTools(categories, null);

		// first looking for any tool for removal
		Vector removePageIds = new Vector();
		for (ListIterator k = wSetupPageList.listIterator(); k.hasNext();) {
			wSetupPage = (WorksiteSetupPage) k.next();
			String pageToolId = wSetupPage.getToolId();

			// use page id + tool id for multiple tool instances
			if (isMultipleInstancesAllowed(findOriginalToolId(state, pageToolId))) {
				pageToolId = wSetupPage.getPageId() + pageToolId;
			}

			inChosenList = false;

			for (ListIterator j = chosenList.listIterator(); j.hasNext();) {
				String toolId = (String) j.next();
				if (pageToolId.equals(toolId)) {
					inChosenList = true;
				}
			}

			// exclude the Home page if there is any
			if (!inChosenList
					&& !(homePageId != null && wSetupPage.getPageId().equals(
							homePageId))) {
				removePageIds.add(wSetupPage.getPageId());
			}
		}

		for (int i = 0; i < removePageIds.size(); i++) {
			// if the tool exists in the wSetupPageList, remove it from the site
			String removeId = (String) removePageIds.get(i);
			SitePage sitePage = site.getPage(removeId);
			site.removePage(sitePage);

			// and remove it from wSetupPageList
			for (ListIterator k = wSetupPageList.listIterator(); k.hasNext();) {
				wSetupPage = (WorksiteSetupPage) k.next();
				if (!wSetupPage.getPageId().equals(removeId)) {
					wSetupPage = null;
				}
			}
			if (wSetupPage != null) {
				wSetupPageList.remove(wSetupPage);
			}
		}

		// then looking for any tool to add
		for (ListIterator j = orderToolIds(state, siteType, chosenList, false)
				.listIterator(); j.hasNext();) {
			String toolId = (String) j.next();
			boolean multiAllowed = isMultipleInstancesAllowed(findOriginalToolId(
					state, toolId));
			// exclude Home tool
			if (!toolId.equals(TOOL_ID_HOME)) {
				// Is the tool in the wSetupPageList?
				inWSetupPageList = false;
				for (ListIterator k = wSetupPageList.listIterator(); k
						.hasNext();) {
					wSetupPage = (WorksiteSetupPage) k.next();
					String pageToolId = wSetupPage.getToolId();

					// use page Id + toolId for multiple tool instances
					if (isMultipleInstancesAllowed(findOriginalToolId(state,
							pageToolId))) {
						pageToolId = wSetupPage.getPageId() + pageToolId;
					}

					if (pageToolId.equals(toolId)) {
						inWSetupPageList = true;
						// but for tool of multiple instances, need to change
						// the title
						if (multiAllowed) {
							SitePage pEdit = (SitePage) site
									.getPage(wSetupPage.pageId);
							pEdit.setTitle((String) multipleToolIdTitleMap
									.get(toolId));
							List toolList = pEdit.getTools();
							for (ListIterator jTool = toolList.listIterator(); jTool
									.hasNext();) {
								ToolConfiguration tool = (ToolConfiguration) jTool
										.next();
								String tId = tool.getTool().getId();
								if (isMultipleInstancesAllowed(findOriginalToolId(
										state, tId))) {
									// set tool title
									tool.setTitle((String) multipleToolIdTitleMap
											.get(toolId));
									// save tool configuration
									saveMultipleToolConfiguration(state, tool,
											toolId);
								}
							}
						}
					}
				}
				if (inWSetupPageList) {
					// if the tool already in the list, do nothing so to save
					// the
					// option settings
				} else {
					// if in chosen list but not in wSetupPageList, add it to
					// the
					// site (one tool on a page)
					Tool toolRegFound = null;
					for (Iterator i = toolRegistrationSet.iterator(); i
							.hasNext();) {
						Tool toolReg = (Tool) i.next();
						String toolRegId = toolReg.getId();
						if (toolId.equals(toolRegId)) {
							toolRegFound = toolReg;
							break;
						} else if (multiAllowed && toolId.startsWith(toolRegId)) {
							try {
								// in case of adding multiple tools, tool id is
								// of format ORIGINAL_TOOL_ID + INDEX_NUMBER
								Integer.parseInt(toolId.replace(toolRegId, ""));
								toolRegFound = toolReg;
								break;
							} catch (Exception parseException) {
								// ignore parse exception
							}
						}
					}

					if (toolRegFound != null) {
						// we know such a tool, so add it
						WorksiteSetupPage addPage = new WorksiteSetupPage();
						SitePage page = site.addPage();
						addPage.pageId = page.getId();
						if (multiAllowed) {
							// set tool title
							page.setTitle((String) multipleToolIdTitleMap
									.get(toolId));
							page.setTitleCustom(true);
						} else {
							// other tools with default title
							page.setTitle(toolRegFound.getTitle());
						}
						page.setLayout(SitePage.LAYOUT_SINGLE_COL);

						// if so specified in the tool's registration file,
						// configure the tool's page to open in a new window.
						if ("true".equals(toolRegFound.getRegisteredConfig()
								.getProperty("popup"))) {
							page.setPopup(true);
						}
						ToolConfiguration tool = page.addTool();
						tool.setTool(toolRegFound.getId(), toolRegFound);
						addPage.toolId = toolId;
						wSetupPageList.add(addPage);

						// set tool title
						if (multiAllowed) {
							// set tool title
							tool.setTitle((String) multipleToolIdTitleMap
									.get(toolId));
							// save tool configuration
							saveMultipleToolConfiguration(state, tool, toolId);
						} else {
							tool.setTitle(toolRegFound.getTitle());
						}
					}
				}
			}
		} // for

		// commit
		commitSite(site);

		site = refreshSiteObject(site);

		// reorder Home and Site Info only if the site has not been customized
		// order before
		if (!site.isCustomPageOrdered()) {
			// the steps for moving page within the list
			int moves = 0;
			if (hasHome) {
				SitePage homePage = null;
				// Order tools - move Home to the top - first find it
				pageList = site.getPages();
				if (pageList != null && pageList.size() != 0) {
					for (ListIterator i = pageList.listIterator(); i.hasNext();) {
						SitePage page = (SitePage) i.next();
						if (isHomePage(page)) {
							homePage = page;
							break;
						}
					}
				}
				if (homePage != null) {
					moves = pageList.indexOf(homePage);
					for (int n = 0; n < moves; n++) {
						homePage.moveUp();
					}
				}
			}

			// if Site Info is newly added, more it to the last
			if (hasSiteInfo) {
				SitePage siteInfoPage = null;
				pageList = site.getPages();
				String[] toolIds = { TOOL_ID_SITEINFO };
				if (pageList != null && pageList.size() != 0) {
					for (ListIterator i = pageList.listIterator(); siteInfoPage == null
							&& i.hasNext();) {
						SitePage page = (SitePage) i.next();
						int s = page.getTools(toolIds).size();
						if (s > 0) {
							siteInfoPage = page;
							break;
						}
					}
					if (siteInfoPage != null) {
						// move home from it's index to the first position
						moves = pageList.indexOf(siteInfoPage);
						for (int n = moves; n < pageList.size(); n++) {
							siteInfoPage.moveDown();
						}
					}
				}
			}
		}

		// commit
		commitSite(site);

		site = refreshSiteObject(site);

		// import
		importToolIntoSite(chosenList, importTools, site);

	}

	private void addNewSite(Map state) throws IdInvalidException,
			IdUsedException, PermissionException {
		if (getStateSite(state) != null) {
			// There is a Site in state already, so use it rather than creating
			// a new Site
			return;
		}

		SiteInfo siteInfo = new SiteInfo();
		siteInfo.setTitle(state.get("TITLE").toString());// 2015chun
		siteInfo.setTerm(state.get("TERM").toString());// 2015spring
		siteInfo.setSite_type(state.get("SITE_TYPE").toString());// course
		siteInfo.setSite_contact_name("Sakai Administrator");

		String id = idManager.createUuid();
		siteInfo.site_id = id;

		state.put(STATE_SITE_INFO, siteInfo);

		Site site = null;

		site = siteService.addSite(id, siteInfo.site_type);
		// add current user as the maintainer
		site.addMember(userDirectoryService.getCurrentUser().getId(),
				site.getMaintainRole(), true, false);

		String title = StringUtils.trimToNull(siteInfo.title);
		String description = siteInfo.description;
		// setAppearance(state, site, siteInfo.iconUrl);
		site.setDescription(description);
		if (title != null) {
			site.setTitle(title);
		}

		ResourcePropertiesEdit rp = site.getPropertiesEdit();

		// / site language information

		rp.addProperty(PROP_SITE_LANGUAGE, "");

		site.setShortDescription(siteInfo.short_description);
		site.setPubView(siteInfo.include);
		site.setJoinable(siteInfo.joinable);
		site.setJoinerRole(siteInfo.joinerRole);
		site.setPublished(true);// 决定站点是否发布
		// site contact information
		rp.addProperty(Site.PROP_SITE_CONTACT_NAME, siteInfo.site_contact_name);
		rp.addProperty(Site.PROP_SITE_CONTACT_EMAIL,
				siteInfo.site_contact_email);

		// SAK-22790 add props from SiteInfo object
		rp.addAll(siteInfo.getProperties());

		// bjones86 - SAK-24423 - update site properties for joinable site
		// settings
		updateSitePropertiesFromSiteInfoOnAddNewSite(siteInfo, rp);

		state.put(STATE_SITE_INSTANCE_ID, site.getId());
		// commit newly added site in order to enable related realm
		commitSite(site);

	}

	// ---------------------------------------------------------

	/**
	 * Update course site and related realm based on the roster chosen or
	 * requested
	 * 
	 * @param state
	 * @param siteId
	 * @param rp
	 * @param term
	 */
	private void updateCourseSiteSections(Map state, String siteId,
			ResourcePropertiesEdit rp, AcademicSession term) {
		// whether this is in the process of editing a site?
		boolean editingSite = ((String) state.get(STATE_SITE_MODE))
				.equals(SITE_MODE_SITEINFO) ? true : false;

		List providerCourseList = state.get(STATE_ADD_CLASS_PROVIDER_CHOSEN) == null ? new ArrayList()
				: (List) state.get(STATE_ADD_CLASS_PROVIDER_CHOSEN);
		int manualAddNumber = 0;
		if (state.get(STATE_MANUAL_ADD_COURSE_NUMBER) != null) {
			manualAddNumber = ((Integer) state
					.get(STATE_MANUAL_ADD_COURSE_NUMBER)).intValue();
		}

		List<SectionObject> cmRequestedSections = new ArrayList<SectionObject>();

		List<String> sectionEids = (List<String>) state
				.get(STATE_CM_REQUESTED_SECTIONS);
		if (sectionEids != null && sectionEids.size() > 0) {
			for (String sectionEid : sectionEids) {
				Section secton = cmService.getSection(sectionEid);
				SectionObject sectionObject = new SectionObject(secton);
				List authorizers = new ArrayList<String>();
				authorizers.add("admin");
				sectionObject.setAuthorizer(authorizers);
				cmRequestedSections.add(sectionObject);
			}
		}

		List<SectionObject> cmAuthorizerSections = (List<SectionObject>) state
				.get(STATE_CM_AUTHORIZER_SECTIONS);

		String realm = siteService.siteReference(siteId);

		if ((providerCourseList != null) && (providerCourseList.size() != 0)) {
			try {
				AuthzGroup realmEdit = authzGroupService.getAuthzGroup(realm);
				String providerRealm = buildExternalRealm(siteId, state,
						providerCourseList,
						StringUtils.trimToNull(realmEdit.getProviderGroupId()));
				realmEdit.setProviderGroupId(providerRealm);
				authzGroupService.save(realmEdit);
			} catch (GroupNotDefinedException e) {
				log.warn(
						this
								+ ".updateCourseSiteSections: IdUnusedException, not found, or not an AuthzGroup object",
						e);
			} catch (AuthzPermissionException e) {
				log.warn(this + rb.getString("java.notaccess"));
			}

			// sendSiteNotification(state, getStateSite(state),
			// providerCourseList);
		}

		if (manualAddNumber != 0) {
			// set the manual sections to the site property
			String manualSections = rp.getProperty(PROP_SITE_REQUEST_COURSE) != null ? rp
					.getProperty(PROP_SITE_REQUEST_COURSE) + "+"
					: "";

			// manualCourseInputs is a list of a list of SectionField
			List manualCourseInputs = (List) state
					.get(STATE_MANUAL_ADD_COURSE_FIELDS);

			// but we want to feed a list of a list of String (input of
			// the required fields)
			for (int j = 0; j < manualAddNumber; j++) {
				manualSections = manualSections.concat(
						sectionFieldProvider.getSectionEid(term.getEid(),
								(List) manualCourseInputs.get(j))).concat("+");
			}

			// trim the trailing plus sign
			manualSections = trimTrailingString(manualSections, "+");

			rp.addProperty(PROP_SITE_REQUEST_COURSE, manualSections);
			// send request
			// sendSiteRequest(state, "new", manualAddNumber,
			// manualCourseInputs, "manual");
		}

		if (cmRequestedSections != null && cmRequestedSections.size() > 0
				|| state.get(STATE_CM_SELECTED_SECTIONS) != null) {
			// set the cmRequest sections to the site property

			String cmRequestedSectionString = "";

			if (!editingSite) {
				// but we want to feed a list of a list of String (input of
				// the required fields)
				for (int j = 0; j < cmRequestedSections.size(); j++) {
					cmRequestedSectionString = cmRequestedSectionString.concat(
							(cmRequestedSections.get(j)).eid).concat("+");
				}

				// trim the trailing plus sign
				cmRequestedSectionString = trimTrailingString(
						cmRequestedSectionString, "+");

				// sendSiteRequest(state, "new", cmRequestedSections.size(),
				// cmRequestedSections, "cmRequest");
			} else {
				cmRequestedSectionString = rp
						.getProperty(STATE_CM_REQUESTED_SECTIONS) != null ? (String) rp
						.getProperty(STATE_CM_REQUESTED_SECTIONS) : "";

				// get the selected cm section
				if (state.get(STATE_CM_SELECTED_SECTIONS) != null) {
					List<SectionObject> cmSelectedSections = (List) state
							.get(STATE_CM_SELECTED_SECTIONS);
					if (cmRequestedSectionString.length() != 0) {
						cmRequestedSectionString = cmRequestedSectionString
								.concat("+");
					}
					for (int j = 0; j < cmSelectedSections.size(); j++) {
						cmRequestedSectionString = cmRequestedSectionString
								.concat((cmSelectedSections.get(j)).eid)
								.concat("+");
					}

					// trim the trailing plus sign
					cmRequestedSectionString = trimTrailingString(
							cmRequestedSectionString, "+");

					// sendSiteRequest(state, "new", cmSelectedSections.size(),
					// cmSelectedSections, "cmRequest");
				}
			}

			// update site property
			if (cmRequestedSectionString.length() > 0) {
				rp.addProperty(STATE_CM_REQUESTED_SECTIONS,
						cmRequestedSectionString);
			} else {
				rp.removeProperty(STATE_CM_REQUESTED_SECTIONS);
			}
		}

		if (cmAuthorizerSections != null && cmAuthorizerSections.size() > 0
				|| state.get(STATE_CM_SELECTED_SECTIONS) != null) {
			// set the cmAuthorizer sections to the site property

			String cmAuthorizerSectionString = "";

			if (!editingSite) {
				// but we want to feed a list of a list of String (input of
				// the required fields)
				for (int j = 0; j < cmAuthorizerSections.size(); j++) {
					cmAuthorizerSectionString = cmAuthorizerSectionString
							.concat((cmAuthorizerSections.get(j)).eid).concat(
									"+");
				}

				// trim the trailing plus sign
				cmAuthorizerSectionString = trimTrailingString(
						cmAuthorizerSectionString, "+");

				// sendSiteRequest(state, "new", cmAuthorizerSections.size(),
				// cmAuthorizerSections, "cmRequest");
			} else {
				cmAuthorizerSectionString = rp
						.getProperty(STATE_CM_AUTHORIZER_SECTIONS) != null ? (String) rp
						.getProperty(STATE_CM_AUTHORIZER_SECTIONS) : "";

				// get the selected cm section
				if (state.get(STATE_CM_SELECTED_SECTIONS) != null) {
					List<SectionObject> cmSelectedSections = (List) state
							.get(STATE_CM_SELECTED_SECTIONS);
					if (cmAuthorizerSectionString.length() != 0) {
						cmAuthorizerSectionString = cmAuthorizerSectionString
								.concat("+");
					}
					for (int j = 0; j < cmSelectedSections.size(); j++) {
						cmAuthorizerSectionString = cmAuthorizerSectionString
								.concat((cmSelectedSections.get(j)).eid)
								.concat("+");
					}

					// trim the trailing plus sign
					cmAuthorizerSectionString = trimTrailingString(
							cmAuthorizerSectionString, "+");

					// sendSiteRequest(state, "new", cmSelectedSections.size(),
					// cmSelectedSections, "cmRequest");
				}
			}

			// update site property
			if (cmAuthorizerSectionString.length() > 0) {
				rp.addProperty(STATE_CM_AUTHORIZER_SECTIONS,
						cmAuthorizerSectionString);
			} else {
				rp.removeProperty(STATE_CM_AUTHORIZER_SECTIONS);
			}
		}
	}

	/**
	 * Trim the trailing occurance of specified string
	 * 
	 * @param cmRequestedSectionString
	 * @param trailingString
	 * @return
	 */
	private String trimTrailingString(String cmRequestedSectionString,
			String trailingString) {
		if (cmRequestedSectionString.endsWith(trailingString)) {
			cmRequestedSectionString = cmRequestedSectionString.substring(0,
					cmRequestedSectionString.lastIndexOf(trailingString));
		}
		return cmRequestedSectionString;
	}

	/**
	 * buildExternalRealm creates a site/realm id in one of three formats, for a
	 * single section, for multiple sections of the same course, or for a
	 * cross-listing having multiple courses
	 * 
	 * @param sectionList
	 *            is a Vector of CourseListItem
	 * @param id
	 *            The site id
	 */
	private String buildExternalRealm(String id, Map state,
			List<String> providerIdList, String existingProviderIdString) {
		String realm = siteService.siteReference(id);
		if (!authzGroupService.allowUpdate(realm)) {
			// addAlert(state, rb.getString("java.rosters"));
			return null;
		}

		List<String> allProviderIdList = new Vector<String>();

		// see if we need to keep existing provider settings
		if (existingProviderIdString != null) {
			allProviderIdList.addAll(Arrays.asList(groupProvider
					.unpackId(existingProviderIdString)));
		}

		// update the list with newly added providers
		allProviderIdList.addAll(providerIdList);

		if (allProviderIdList == null || allProviderIdList.size() == 0)
			return null;

		String[] providers = new String[allProviderIdList.size()];
		providers = (String[]) allProviderIdList.toArray(providers);

		String providerId = groupProvider.packId(providers);
		return providerId;

	} // buildExternalRealm
		// import tool content into site

	private void importToolIntoSite(List toolIds, Hashtable importTools,
			Site site) {
		if (importTools != null) {
			Map transversalMap = new HashMap();

			// import resources first
			boolean resourcesImported = false;
			for (int i = 0; i < toolIds.size() && !resourcesImported; i++) {
				String toolId = (String) toolIds.get(i);

				if (toolId.equalsIgnoreCase("sakai.resources")
						&& importTools.containsKey(toolId)) {
					List importSiteIds = (List) importTools.get(toolId);

					for (int k = 0; k < importSiteIds.size(); k++) {
						String fromSiteId = (String) importSiteIds.get(k);
						String toSiteId = site.getId();

						String fromSiteCollectionId = m_contentHostingService
								.getSiteCollection(fromSiteId);
						String toSiteCollectionId = m_contentHostingService
								.getSiteCollection(toSiteId);

						Map<String, String> entityMap = transferCopyEntities(
								toolId, fromSiteCollectionId,
								toSiteCollectionId);
						if (entityMap != null) {
							transversalMap.putAll(entityMap);
						}
						resourcesImported = true;
					}
				}
			}

			// import other tools then
			for (int i = 0; i < toolIds.size(); i++) {
				String toolId = (String) toolIds.get(i);
				if (!toolId.equalsIgnoreCase("sakai.resources")
						&& importTools.containsKey(toolId)) {
					List importSiteIds = (List) importTools.get(toolId);
					for (int k = 0; k < importSiteIds.size(); k++) {
						String fromSiteId = (String) importSiteIds.get(k);
						String toSiteId = site.getId();
						if (SITE_INFO_TOOL_ID.equals(toolId)) {
							copySiteInformation(fromSiteId, site);
						} else {
							Map<String, String> entityMap = transferCopyEntities(
									toolId, fromSiteId, toSiteId);
							if (entityMap != null) {
								transversalMap.putAll(entityMap);
							}
							resourcesImported = true;
						}
					}
				}
			}

			// update entity references
			for (int i = 0; i < toolIds.size(); i++) {
				String toolId = (String) toolIds.get(i);
				if (importTools.containsKey(toolId)) {
					List importSiteIds = (List) importTools.get(toolId);
					for (int k = 0; k < importSiteIds.size(); k++) {
						String toSiteId = site.getId();
						updateEntityReferences(toolId, toSiteId,
								transversalMap, site);
					}
				}
			}
		}
	} // importToolIntoSite

	/**
	 * refresh site object
	 * 
	 * @param site
	 * @return
	 */
	private Site refreshSiteObject(Site site) {
		// refresh the site object
		try {
			site = siteService.getSite(site.getId());
		} catch (Exception e) {
			// error getting site after tool modification
			log.warn(this + " - cannot get site " + site.getId()
					+ " after inserting lti tools");
		}
		return site;
	}

	/**
	 * Save configuration values for multiple tool instances
	 */
	private void saveMultipleToolConfiguration(Map state,
			ToolConfiguration tool, String toolId) {
		// get the configuration of multiple tool instance
		HashMap<String, HashMap<String, String>> multipleToolConfiguration = state
				.get(STATE_MULTIPLE_TOOL_CONFIGURATION) != null ? (HashMap<String, HashMap<String, String>>) state
				.get(STATE_MULTIPLE_TOOL_CONFIGURATION)
				: new HashMap<String, HashMap<String, String>>();

		// set tool attributes
		HashMap<String, String> attributes = multipleToolConfiguration
				.get(toolId);

		if (attributes != null) {
			for (Map.Entry<String, String> attributeEntry : attributes
					.entrySet()) {
				String attribute = attributeEntry.getKey();
				String attributeValue = attributeEntry.getValue();
				// if we have a value
				if (attributeValue != null) {
					// if this value is not the same as the tool's registered,
					// set it in the placement
					if (!attributeValue.equals(tool.getTool()
							.getRegisteredConfig().getProperty(attribute))) {
						tool.getPlacementConfig().setProperty(attribute,
								attributeValue);
					}

					// otherwise clear it
					else {
						tool.getPlacementConfig().remove(attribute);
					}
				}

				// if no value
				else {
					tool.getPlacementConfig().remove(attribute);
				}
			}
		}
	}

	private void addSynopticTool(SitePage page, String toolId,
			String toolTitle, String layoutHint, int position) {
		page.setLayout(SitePage.LAYOUT_DOUBLE_COL);

		// Add synoptic announcements tool
		ToolConfiguration tool = page.addTool();
		Tool reg = toolManager.getTool(toolId);
		tool.setTool(toolId, reg);
		tool.setTitle(toolTitle);
		tool.setLayoutHints(layoutHint);

		// count how many synoptic tools in the second/right column
		int totalSynopticTools = 0;
		for (ToolConfiguration t : page.getTools()) {
			if (t.getToolId() != null
					&& SYNOPTIC_TOOL_ID_MAP.containsKey(t.getToolId())) {
				totalSynopticTools++;
			}
		}
		// now move the newly added synoptic tool to proper position
		for (int i = 0; i < (totalSynopticTools - position - 1); i++) {
			tool.moveUp();
		}
	}

	/**
	 * toolId might be of form original tool id concatenated with number find
	 * whether there is an counterpart in the the multipleToolIdSet
	 * 
	 * @param state
	 * @param toolId
	 * @return
	 */
	private String findOriginalToolId(Map state, String toolId) {
		// treat home tool differently
		if (toolId.equals(TOOL_ID_HOME) || SITE_INFO_TOOL_ID.equals(toolId)) {
			return toolId;
		} else {
			Set categories = new HashSet();
			categories.add((String) state.get(STATE_SITE_TYPE));
			Set toolRegistrationList = toolManager.findTools(categories, null);
			String rv = null;
			if (toolRegistrationList != null) {
				for (Iterator i = toolRegistrationList.iterator(); rv == null
						&& i.hasNext();) {
					Tool tool = (Tool) i.next();
					String tId = tool.getId();
					rv = originalToolId(toolId, tId);
				}
			}
			return rv;
		}
	}

	/**
	 * what are the tool ids within Home page? If this is for a newly added Home
	 * tool, get the tool ids from template site or system set default Else if
	 * this is an existing Home tool, get the tool ids from the page
	 * 
	 * @param state
	 * @param newHomeTool
	 * @param homePage
	 * @return
	 */
	private List<String> getHomeToolIds(Map state, boolean newHomeTool,
			SitePage homePage) {
		List<String> rv = new Vector<String>();

		// if this is a new Home tool page to be added, get the tool ids from
		// definition (template site first, and then configurations)
		Site site = getStateSite(state);

		String siteType = site != null ? site.getType() : "";

		// First: get the tool ids from configuration files
		// initially by "wsetup.home.toolids" + site type, and if missing, use
		// "wsetup.home.toolids"
		if (ServerConfigurationService.getStrings("wsetup.home.toolids."
				+ siteType) != null) {
			rv = new ArrayList(Arrays.asList(ServerConfigurationService
					.getStrings("wsetup.home.toolids." + siteType)));
		} else if (ServerConfigurationService.getStrings("wsetup.home.toolids") != null) {
			rv = new ArrayList(Arrays.asList(ServerConfigurationService
					.getStrings("wsetup.home.toolids")));
		}

		// Second: if tool list is empty, get it from the template site settings
		if (rv.isEmpty()) {
			// template site
			Site templateSite = null;
			String templateSiteId = "";

			if (siteService.isUserSite(site.getId())) {
				// myworkspace type site: get user type first, and then get the
				// template site
				try {
					User user = userDirectoryService.getUser(siteService
							.getSiteUserId(site.getId()));
					templateSiteId = SiteService.USER_SITE_TEMPLATE + "."
							+ user.getType();
					templateSite = siteService.getSite(templateSiteId);
				} catch (Throwable t) {

					log.debug(this + ": getHomeToolIds cannot find site "
							+ templateSiteId + t.getMessage());
					// use the fall-back, user template site
					try {
						templateSiteId = siteService.USER_SITE_TEMPLATE;
						templateSite = siteService.getSite(templateSiteId);
					} catch (Throwable tt) {
						log.debug(this + ": getHomeToolIds cannot find site "
								+ templateSiteId + tt.getMessage());
					}
				}
			} else {
				// not myworkspace site
				// first: see whether it is during site creation process and
				// using a template site
				templateSite = (Site) state.get(STATE_TEMPLATE_SITE);

				if (templateSite == null) {
					// second: if no template is chosen by user, then use
					// template based on site type
					templateSiteId = SiteService.SITE_TEMPLATE + "." + siteType;
					try {
						templateSite = siteService.getSite(templateSiteId);
					} catch (Throwable t) {
						log.debug(this + ": getHomeToolIds cannot find site "
								+ templateSiteId + t.getMessage());

						// thrid: if cannot find template site with the site
						// type, use the default template
						templateSiteId = SiteService.SITE_TEMPLATE;
						try {
							templateSite = siteService.getSite(templateSiteId);
						} catch (Throwable tt) {
							log.debug(this
									+ ": getHomeToolIds cannot find site "
									+ templateSiteId + tt.getMessage());
						}
					}
				}
			}
			if (templateSite != null) {
				// get Home page and embedded tool ids
				for (SitePage page : (List<SitePage>) templateSite.getPages()) {
					String title = page.getTitle();

					if (isHomePage(page)) {
						// found home page, add all tool ids to return value
						for (ToolConfiguration tConfiguration : (List<ToolConfiguration>) page
								.getTools()) {
							String toolId = tConfiguration.getToolId();
							if (toolManager.getTool(toolId) != null)
								rv.add(toolId);
						}
						break;
					}
				}
			}
		}

		// Third: if the tool id list is still empty because we cannot find any
		// template site yet, use the default settings
		if (rv.isEmpty()) {
			if (siteType.equalsIgnoreCase("myworkspace")) {
				// first try with MOTD tool
				if (toolManager.getTool("sakai.motd") != null)
					rv.add("sakai.motd");

				if (rv.isEmpty()) {
					// then try with the myworkspace information tool
					if (toolManager.getTool("sakai.iframe.myworkspace") != null)
						rv.add("sakai.iframe.myworkspace");
				}
			} else {
				// try the site information tool
				if (toolManager.getTool("sakai.iframe.site") != null)
					rv.add("sakai.iframe.site");
			}

			// synoptical tools
			if (toolManager.getTool(TOOL_ID_SUMMARY_CALENDAR) != null) {
				rv.add(TOOL_ID_SUMMARY_CALENDAR);
			}

			if (toolManager.getTool(TOOL_ID_SYNOPTIC_ANNOUNCEMENT) != null) {
				rv.add(TOOL_ID_SYNOPTIC_ANNOUNCEMENT);
			}

			if (toolManager.getTool(TOOL_ID_SYNOPTIC_CHAT) != null) {
				rv.add(TOOL_ID_SYNOPTIC_CHAT);
			}
			if (toolManager.getTool(TOOL_ID_SYNOPTIC_MESSAGECENTER) != null) {
				rv.add(TOOL_ID_SYNOPTIC_MESSAGECENTER);
			}
		}

		// Fourth: if this is an existing Home tool page, get any extra tool ids
		// in the page already back to the list
		if (!newHomeTool) {
			// found home page, add all tool ids to return value
			for (ToolConfiguration tConfiguration : (List<ToolConfiguration>) homePage
					.getTools()) {
				String hToolId = tConfiguration.getToolId();
				if (!rv.contains(hToolId)) {
					rv.add(hToolId);
				}
			}
		}

		return rv;
	}

	/**
	 * whether this tool title is of Home tool title
	 * 
	 * @param toolTitle
	 * @return
	 */
	private boolean isHomePage(SitePage page) {
		if (page.getProperties().getProperty(SitePage.IS_HOME_PAGE) != null) {
			// check based on the page property first
			return true;
		} else {
			// if above fails, check based on the page title
			String pageTitle = page.getTitle();
			return TOOL_ID_HOME.equalsIgnoreCase(pageTitle)
					|| rb.getString("java.home").equalsIgnoreCase(pageTitle);
		}
	}

	private List orderToolIds(Map state, String type, List<String> toolIdList,
			boolean synoptic) {
		List rv = new Vector();

		// look for null site type
		if (type == null && state.get(STATE_DEFAULT_SITE_TYPE) != null) {
			type = (String) state.get(STATE_DEFAULT_SITE_TYPE);
		}

		if (type != null && toolIdList != null) {
			List<String> orderedToolIds = ServerConfigurationService
					.getToolOrder(type);
			for (String tool_id : orderedToolIds) {
				for (String toolId : toolIdList) {
					String rToolId = originalToolId(toolId, tool_id);
					if (rToolId != null) {
						rv.add(toolId);
						break;
					} else {
						List<String> parentToolList = (List<String>) SYNOPTIC_TOOL_ID_MAP
								.get(toolId);
						if (parentToolList != null
								&& parentToolList.contains(tool_id)) {
							rv.add(toolId);
							break;
						}
					}
				}
			}
		}

		// add those toolids without specified order
		if (toolIdList != null) {
			for (String toolId : toolIdList) {
				if (!rv.contains(toolId)) {
					rv.add(toolId);
				}
			}
		}
		return rv;

	} // orderToolIds

	private String originalToolId(String toolId, String toolRegistrationId) {
		String rv = null;
		if (toolId.equals(toolRegistrationId)) {
			rv = toolRegistrationId;
		} else if (toolId.indexOf(toolRegistrationId) != -1
				&& isMultipleInstancesAllowed(toolRegistrationId)) {
			// the multiple tool id format is of SITE_IDTOOL_IDx, where x is an
			// intger >= 1
			if (toolId.endsWith(toolRegistrationId)) {
				// get the site id part out
				String uuid = toolId.replaceFirst(toolRegistrationId, "");
				if (uuid != null && uuid.length() == UUID_LENGTH)
					rv = toolRegistrationId;
			} else {
				String suffix = toolId.substring(toolId
						.indexOf(toolRegistrationId)
						+ toolRegistrationId.length());
				try {
					Integer.parseInt(suffix);
					rv = toolRegistrationId;
				} catch (Exception e) {
					// not the right tool id
					// log.debug(this +
					// ".findOriginalToolId not matching tool id = " +
					// toolRegistrationId + " original tool id=" + toolId +
					// e.getMessage(), e);
				}
			}

		}
		return rv;
	}

	protected void updateEntityReferences(String toolId, String toContext,
			Map transversalMap, Site newSite) {
		if (toolId.equalsIgnoreCase(SITE_INFORMATION_TOOL)) {
			updateSiteInfoToolEntityReferences(transversalMap, newSite);
		} else {
			for (Iterator i = entityManager.getEntityProducers().iterator(); i
					.hasNext();) {
				EntityProducer ep = (EntityProducer) i.next();
				if (ep instanceof EntityTransferrerRefMigrator
						&& ep instanceof EntityTransferrer) {
					try {
						EntityTransferrer et = (EntityTransferrer) ep;
						EntityTransferrerRefMigrator etRM = (EntityTransferrerRefMigrator) ep;

						// if this producer claims this tool id
						if (ArrayUtil.contains(et.myToolIds(), toolId)) {
							etRM.updateEntityReferences(toContext,
									transversalMap);
						}
					} catch (Throwable t) {
						log.warn(
								"Error encountered while asking EntityTransfer to updateEntityReferences at site: "
										+ toContext, t);
					}
				}
			}
		}
	}

	private void updateSiteInfoToolEntityReferences(Map transversalMap,
			Site newSite) {
		if (transversalMap != null && transversalMap.size() > 0
				&& newSite != null) {
			Set<Entry<String, String>> entrySet = (Set<Entry<String, String>>) transversalMap
					.entrySet();

			String msgBody = newSite.getDescription();
			if (msgBody != null && !"".equals(msgBody)) {
				boolean updated = false;
				Iterator<Entry<String, String>> entryItr = entrySet.iterator();
				while (entryItr.hasNext()) {
					Entry<String, String> entry = (Entry<String, String>) entryItr
							.next();
					String fromContextRef = entry.getKey();
					if (msgBody.contains(fromContextRef)) {
						msgBody = msgBody.replace(fromContextRef,
								entry.getValue());
						updated = true;
					}
				}
				if (updated) {
					// update the site b/c some tools (Lessonbuilder) updates
					// the site structure (add/remove pages) and we don't want
					// to
					// over write this
					try {
						newSite = siteService.getSite(newSite.getId());
						newSite.setDescription(msgBody);
						siteService.save(newSite);
					} catch (IdUnusedException e) {
						// TODO:
					} catch (PermissionException e) {
						// TODO:
					}
				}
			}
		}
	}

	private void copySiteInformation(String fromSiteId, Site toSite) {
		try {
			Site fromSite = siteService.getSite(fromSiteId);
			// we must get the new site again b/c some tools (lesson builder)
			// can make changes to the site structure (i.e. add pages).
			Site editToSite = siteService.getSite(toSite.getId());
			editToSite.setDescription(fromSite.getDescription());
			editToSite.setInfoUrl(fromSite.getInfoUrl());
			commitSite(editToSite);
			toSite = editToSite;
		} catch (IdUnusedException e) {

		}
	}

	/**
	 * Read from tool registration whether multiple registration is allowed for
	 * this tool
	 * 
	 * @param toolId
	 * @return
	 */
	private boolean isMultipleInstancesAllowed(String toolId) {
		Tool tool = toolManager.getTool(toolId);
		if (tool != null) {
			Properties tProperties = tool.getRegisteredConfig();
			return (tProperties.containsKey("allowMultipleInstances") && tProperties
					.getProperty("allowMultipleInstances").equalsIgnoreCase(
							Boolean.TRUE.toString())) ? true : false;
		}
		return false;
	}

	/**
	 * adjust site type
	 * 
	 * @param state
	 * @param site
	 * @return
	 */
	private String checkNullSiteType(Map state, String type, String siteId) {
		if (type == null) {
			if (siteId != null && siteService.isUserSite(siteId)) {
				type = "myworkspace";
			} else if (state.get(STATE_DEFAULT_SITE_TYPE) != null) {
				// for those sites without type, use the tool set for default
				// site type
				type = (String) state.get(STATE_DEFAULT_SITE_TYPE);
			}
		}
		return type;
	}

	/**
	 * Transfer a copy of all entites from another context for any entity
	 * producer that claims this tool id.
	 * 
	 * @param toolId
	 *            The tool id.
	 * @param fromContext
	 *            The context to import from.
	 * @param toContext
	 *            The context to import into.
	 */
	protected Map transferCopyEntities(String toolId, String fromContext,
			String toContext) {
		// TODO: used to offer to resources first - why? still needed? -ggolden

		Map transversalMap = new HashMap();

		// offer to all EntityProducers
		for (Iterator i = entityManager.getEntityProducers().iterator(); i
				.hasNext();) {
			EntityProducer ep = (EntityProducer) i.next();
			if (ep instanceof EntityTransferrer) {
				try {
					EntityTransferrer et = (EntityTransferrer) ep;

					// if this producer claims this tool id
					if (ArrayUtil.contains(et.myToolIds(), toolId)) {
						if (ep instanceof EntityTransferrerRefMigrator) {
							EntityTransferrerRefMigrator etMp = (EntityTransferrerRefMigrator) ep;
							Map<String, String> entityMap = etMp
									.transferCopyEntitiesRefMigrator(
											fromContext, toContext,
											new Vector());
							if (entityMap != null) {
								transversalMap.putAll(entityMap);
							}
						} else {
							et.transferCopyEntities(fromContext, toContext,
									new Vector());
						}
					}
				} catch (Throwable t) {
					log.warn(
							this
									+ ".transferCopyEntities: Error encountered while asking EntityTransfer to transferCopyEntities from: "
									+ fromContext + " to: " + toContext, t);
				}
			}
		}

		return transversalMap;
	}

	protected Site getStateSite(Map state) {
		return getStateSite(state, false);

	} // getStateSite

	/**
	 * check whether the page tool list contains certain toolId
	 * 
	 * @param pageToolList
	 * @param toolId
	 * @return
	 */
	private boolean pageHasToolId(List pageToolList, String toolId) {
		for (Iterator iPageToolList = pageToolList.iterator(); iPageToolList
				.hasNext();) {
			ToolConfiguration toolConfiguration = (ToolConfiguration) iPageToolList
					.next();
			Tool t = toolConfiguration.getTool();
			if (t != null && toolId.equals(toolConfiguration.getTool().getId())) {
				return true;
			}
		}
		return false;
	}

	protected Site getStateSite(Map state, boolean autoContext) {
		Site site = null;

		if (state.get(STATE_SITE_INSTANCE_ID) != null) {
			try {
				site = siteService.getSite((String) state
						.get(STATE_SITE_INSTANCE_ID));
			} catch (Exception ignore) {
			}
		}
		if (site == null && autoContext) {
			String siteId = toolManager.getCurrentPlacement().getContext();
			try {
				site = siteService.getSite(siteId);
				state.put(STATE_SITE_INSTANCE_ID, siteId);
			} catch (Exception ignore) {
			}
		}
		return site;
	} // getStateSite

	public void commitSite(Site site) {
		try {
			siteService.save(site);
		} catch (IdUnusedException e) {
			// TODO:
		} catch (PermissionException e) {
			// TODO:
		}

	}// commitSite

	public boolean updateSitePropertiesFromSiteInfoOnAddNewSite(
			SiteInfo siteInfo, ResourcePropertiesEdit props) {
		if (props == null || siteInfo == null) {
			return false;
		}

		if (siteService.isGlobalJoinGroupEnabled()) {
			props.addProperty("joinerGroup", siteInfo.joinerGroup);
		}

		if (siteService.isGlobalJoinExcludedFromPublicListEnabled()) {
			props.addProperty("joinExcludeFromPublicList",
					Boolean.toString(siteInfo.joinExcludePublic));
		}

		if (siteService.isGlobalJoinLimitByAccountTypeEnabled()) {
			props.addProperty("joinLimitByAccountType",
					Boolean.toString(siteInfo.joinLimitByAccountType));
			props.addProperty("joinLimitedAccountTypes",
					siteInfo.joinLimitedAccountTypes);
		}

		return true;
	}

	// SAK-23468
	private void setNewSiteStateParameters(Site site, Map state) {
		if (site != null) {
			state.put(STATE_NEW_SITE_STATUS_ISPUBLISHED,
					Boolean.valueOf(site.isPublished()));
			state.put(STATE_NEW_SITE_STATUS_ID, site.getId());
			state.put(STATE_NEW_SITE_STATUS_TITLE, site.getTitle());
		}
	}

	// SAK-23468
	private void clearNewSiteStateParameters(SessionState state) {
		state.removeAttribute(STATE_NEW_SITE_STATUS_ISPUBLISHED);
		state.removeAttribute(STATE_NEW_SITE_STATUS_ID);
		state.removeAttribute(STATE_NEW_SITE_STATUS_TITLE);

	}

	// --------------------------------------------

	public class SiteInfo {
		public String site_id = NULL_STRING; // getId of Resource

		public String external_id = NULL_STRING; // if matches site_id

		// connects site with U-M
		// course information

		public String site_type = "";

		public String iconUrl = NULL_STRING;

		public String infoUrl = NULL_STRING;

		public boolean joinable = false;

		public String joinerRole = NULL_STRING;

		public String title = NULL_STRING; // the short name of the site

		public Set<String> siteRefAliases = new HashSet<String>(); // the
																	// aliases
																	// for the
																	// site
																	// itself

		public String short_description = NULL_STRING; // the short (20 char)

		// description of the
		// site

		public String description = NULL_STRING; // the longer description of

		// the site

		public String additional = NULL_STRING; // additional information on

		// crosslists, etc.

		public boolean published = false;

		public boolean include = true; // include the site in the Sites index;

		// default is true.

		public String site_contact_name = NULL_STRING; // site contact name

		public String site_contact_email = NULL_STRING; // site contact email

		public String term = NULL_STRING; // academic term

		public ResourceProperties properties = new BaseResourcePropertiesEdit();

		// bjones86 - SAK-24423 - joinable site settings
		public String joinerGroup = NULL_STRING;

		public String getJoinerGroup() {
			return joinerGroup;
		}

		public boolean joinExcludePublic = false;

		public boolean getJoinExcludePublic() {
			return joinExcludePublic;
		}

		public boolean joinLimitByAccountType = false;

		public boolean getJoinLimitByAccountType() {
			return joinLimitByAccountType;
		}

		public String joinLimitedAccountTypes = NULL_STRING;

		public String getJoinLimitedAccountTypes() {
			return joinLimitedAccountTypes;
		} // end joinable site settings

		public String getSiteId() {
			return site_id;
		}

		public void setSiteId(String siteId) {
			this.site_id = siteId;
		}

		public String getSiteType() {
			return site_type;
		}

		public void setSite_type(String siteType) {
			this.site_type = siteType;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getDescription() {
			return description;
		}

		public String getIconUrl() {
			return iconUrl;
		}

		public String getInfoUrll() {
			return infoUrl;
		}

		public boolean getJoinable() {
			return joinable;
		}

		public String getJoinerRole() {
			return joinerRole;
		}

		public String getAdditional() {
			return additional;
		}

		public boolean getPublished() {
			return published;
		}

		public boolean getInclude() {
			return include;
		}

		public String getSiteContactName() {
			return site_contact_name;
		}

		public void setSite_contact_name(String site_contact_name) {
			this.site_contact_name = site_contact_name;
		}

		public String getSiteContactEmail() {
			return site_contact_email;
		}

		public String getFirstAlias() {
			return siteRefAliases.isEmpty() ? NULL_STRING : siteRefAliases
					.iterator().next();
		}

		public void addProperty(String key, String value) {
			properties.addProperty(key, value);
		}

		public ResourceProperties getProperties() {
			return properties;
		}

		public Set<String> getSiteRefAliases() {
			return siteRefAliases;
		}

		public void setSiteRefAliases(Set<String> siteRefAliases) {
			this.siteRefAliases = siteRefAliases;
		}

		public String getTerm() {
			return term;
		}

		public void setTerm(String term) {
			this.term = term;
		}

	} // SiteInfo

	/*
	 * WorksiteSetupPage is a utility class for working with site pages
	 * configured by Worksite Setup
	 */
	public class WorksiteSetupPage {
		public String pageId = NULL_STRING;

		public String pageTitle = NULL_STRING;

		public String toolId = NULL_STRING;

		public String getPageId() {
			return pageId;
		}

		public String getPageTitle() {
			return pageTitle;
		}

		public String getToolId() {
			return toolId;
		}

	} // WorksiteSetupPage

	/**
	 * this object is used for displaying purposes in chef_site-newSiteCourse.vm
	 */
	public class SectionObject {
		public Section section;

		public String eid;

		public String title;

		public String category;

		public String categoryDescription;

		public boolean isLecture;

		public boolean attached;

		public List<String> authorizer;

		public String description;

		public SectionObject(Section section) {
			this.section = section;
			this.eid = section.getEid();
			this.title = section.getTitle();
			this.category = section.getCategory();
			List<String> authorizers = new ArrayList<String>();
			if (section.getEnrollmentSet() != null) {
				Set<String> instructorset = section.getEnrollmentSet()
						.getOfficialInstructors();
				if (instructorset != null) {
					for (String instructor : instructorset) {
						authorizers.add(instructor);
					}
				}
			}
			this.authorizer = authorizers;
			this.categoryDescription = cmService
					.getSectionCategoryDescription(section.getCategory());
			if ("01.lct".equals(section.getCategory())) {
				this.isLecture = true;
			} else {
				this.isLecture = false;
			}
			Set set = authzGroupService.getAuthzGroupIds(section.getEid());
			if (set != null && !set.isEmpty()) {
				this.attached = true;
			} else {
				this.attached = false;
			}
			this.description = section.getDescription();
		}

		public Section getSection() {
			return section;
		}

		public String getEid() {
			return eid;
		}

		public String getTitle() {
			return title;
		}

		public String getCategory() {
			return category;
		}

		public String getCategoryDescription() {
			return categoryDescription;
		}

		public boolean getIsLecture() {
			return isLecture;
		}

		public boolean getAttached() {
			return attached;
		}

		public String getDescription() {
			return description;
		}

		public List<String> getAuthorizer() {
			return authorizer;
		}

		public String getAuthorizerString() {
			StringBuffer rv = new StringBuffer();
			if (authorizer != null && !authorizer.isEmpty()) {
				for (int count = 0; count < authorizer.size(); count++) {
					// concatenate all authorizers into a String
					if (count > 0) {
						rv.append(", ");
					}
					rv.append(authorizer.get(count));
				}
			}
			return rv.toString();
		}

		public void setAuthorizer(List<String> authorizer) {
			this.authorizer = authorizer;
		}

	} // SectionObject constructor

	@Override
	public void openSession() {
		Session session = sessionManager.getCurrentSession();
		session.setUserId("admin");
	}

	@Override
	public void closeSession() {
		sessionManager.getCurrentSession().invalidate();
	}
}
