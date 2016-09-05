/**
 * 
 */
package org.sakaiproject.cmsrest.logic.impl;

import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.authz.api.AuthzGroup;
import org.sakaiproject.authz.api.AuthzGroupService;
import org.sakaiproject.authz.api.AuthzPermissionException;
import org.sakaiproject.authz.api.GroupNotDefinedException;
import org.sakaiproject.authz.api.Member;
import org.sakaiproject.authz.api.SecurityService;
import org.sakaiproject.cmsrest.logic.SiteManage;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.user.api.User;
import org.sakaiproject.user.api.UserDirectoryService;
import org.sakaiproject.user.api.UserEdit;
import org.sakaiproject.user.api.UserNotDefinedException;

/**
 * @author yushanyuan
 *
 */
public class SiteManageImpl implements SiteManage {
	private static Log log = LogFactory.getLog(SiteManageImpl.class);

	public static final String SITE_DUPLICATED_NAME = "site_duplicated_named";

	private static final String STATE_SITE_INSTANCE_ID = "site.instance.id";

	private static final String SITE_INFORMATION_TOOL = "sakai.iframe.site";

	public static final String SITE_DUPLICATED = "site_duplicated";

	public String statusChoice = "active";

	private SiteService siteService;

	private AuthzGroupService authzGroupService;

	private UserDirectoryService userDirectoryService;

	public String getStatusChoice() {
		return statusChoice;
	}

	public void setStatusChoice(String statusChoice) {
		this.statusChoice = statusChoice;
	}

	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	public void setAuthzGroupService(AuthzGroupService authzGroupService) {
		this.authzGroupService = authzGroupService;
	}
	public void setUserDirectoryService(
			UserDirectoryService userDirectoryService) {
		this.userDirectoryService = userDirectoryService;
	}

	// ---------------------------------------------------

	public void init() {
		log.info("init");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sakaiproject.courseuser.logic.SiteManage#addUser(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public User addUser(String id, String eid, String firstName,
			String lastName, String email, String pw, String type)
			throws Exception{
		User user = null;
		try{
			user = userDirectoryService.getUserByEid(eid);
		}catch(UserNotDefinedException e){
			// 新建
			log.error("",e);
			return userDirectoryService.addUser(id, eid, firstName, lastName,
					email, pw, type, null);
		}
		// 如果存在就更新
		if (user != null) {
			UserEdit editUser = null;
			editUser = userDirectoryService.editUser(user.getId());
			editUser.setFirstName(firstName);
			editUser.setLastName(lastName);
			editUser.setEmail(email);
			editUser.setPassword(pw);
			if (type != null)
				editUser.setType(type);

			userDirectoryService.commitEdit(editUser);
			user = userDirectoryService.getUserByEid(eid);

		}  

		return user;
	}

	@Override
	public void removeUser(String userId)throws Exception {
		UserEdit editUser = userDirectoryService.editUser(userId);
		userDirectoryService.removeUser(editUser);
	}
	
	 

	@Override
	public void addUsersToSite(String siteId, List<String> userEidList,
			List<String> roleList) throws Exception {
		addUsersRealm(siteId, userEidList, roleList);
	}

	@Override
	public void removeUsersFromSite(String siteId, List<String> userIdList)
			throws Exception{
		removeUsersRealm(siteId, userIdList);
	}

	@Override
	public Set<String> queryUserIdBySiteId(String siteId, String role) throws Exception{
		String realmId = siteService.siteReference(siteId);

		AuthzGroup realmEdit;
		 
		realmEdit = authzGroupService.getAuthzGroup(realmId);
		if (realmEdit != null) {
			return realmEdit.getUsersHasRole(role);
		}
		 
		return null;
	}

	 
	// -----------------------------------private method -----------------------

	private void removeUsersRealm(String siteId, List<String> userIdList)
			throws GroupNotDefinedException, UserNotDefinedException,
			AuthzPermissionException {
		String realmId = siteService.siteReference(siteId);
		User user;
		AuthzGroup realmEdit = authzGroupService.getAuthzGroup(realmId);
		for (String userId : userIdList) {
			user = userDirectoryService.getUser(userId);
			if(user==null||user.getEid().equals("admin")){
				continue;
			}
			// save role for permission check
			if (user != null) {
				Member userMember = realmEdit.getMember(userId);
				if (userMember != null) {
					realmEdit.removeMember(userId);
				}
			}
		}
		authzGroupService.save(realmEdit);
	}

	
	private void addUsersRealm(String siteId, List<String> userEIdList,
			List<String> roleList) throws GroupNotDefinedException,
			UserNotDefinedException, IdUnusedException,
			AuthzPermissionException {

		Site site = siteService.getSite(siteId);
		String realmId = site.getReference();
		AuthzGroup realmEdit = authzGroupService.getAuthzGroup(realmId);

		for (int i = 0; i < userEIdList.size(); i++) {
			String id = userEIdList.get(i);

			String role = roleList.get(i);

			User user = null;
			try{
				user = userDirectoryService.getUserByEid(id);
			}catch(UserNotDefinedException e){
				e.printStackTrace();
			}
			if(user != null){
				realmEdit.addMember(user.getId(), role,
						statusChoice.equals("active"), false);
			}
			
		} // for
		authzGroupService.save(realmEdit);
	}

	/*private String actionForTemplate(String courseTitle, String contentSummary) {

		SessionState state = new MySessionState();
		state.setAttribute(STATE_SITE_INSTANCE_ID,
				configLogic.getSiteTemplateId());

		// Let actionForTemplate know to make any permanent changes before
		// continuing to the next template
		String direction = contentSummary;
		String option = "option";// params.getString("option");

		String title = courseTitle;// params.getString("title");
		state.setAttribute(SITE_DUPLICATED_NAME, title);

		String newSiteId = idManager.createUuid();
		try {
			String oldSiteId = (String) state
					.getAttribute(STATE_SITE_INSTANCE_ID);
			// SAK-20797
			long oldSiteQuota = this.getSiteSpecificQuota(oldSiteId);

			// UserDirectoryService userDirectoryService =
			// (UserDirectoryService)ComponentManager.get(UserDirectoryService.class);
			// User user =
			// userDirectoryService.authenticate("system-user","lajsdf908q092348,lajf09809;'../,..<.d>dfwer");
			User user = userDirectoryService.authenticate(
					configLogic.getAccount(), configLogic.getAccountPassword());
			if (user != null) {

				Session s = sessionManager.startSession();
				s.setUserId(user.getId());
				sessionManager.setCurrentSession(s);
				if (s == null) {
					throw new RuntimeException("开启会话失败。");
				}
			}
			Site site = siteService.addSite(newSiteId, getStateSite(state));

			// get the new site icon url
			if (site.getIconUrl() != null) {
				site.setIconUrl(transferSiteResource(oldSiteId, newSiteId,
						site.getIconUrl()));
			}

			// set title
			site.setTitle(title);

			// SAK-20797 alter quota if required
			boolean duplicateQuota = true;// params.getString("dupequota") !=
											// null ?
											// params.getBoolean("dupequota") :
											// false;
			if (duplicateQuota == true) {

				if (oldSiteQuota > 0) {
					log.info("Saving quota");
					try {
						String collId = m_contentHostingService
								.getSiteCollection(site.getId());

						ContentCollectionEdit col = m_contentHostingService
								.editCollection(collId);

						ResourcePropertiesEdit resourceProperties = col
								.getPropertiesEdit();
						resourceProperties.addProperty(
								ResourceProperties.PROP_COLLECTION_BODY_QUOTA,
								new Long(oldSiteQuota).toString());
						m_contentHostingService.commitCollection(col);

					} catch (Exception ignore) {
						log.warn("saveQuota: unable to duplicate site-specific quota for site : "
								+ site.getId() + " : " + ignore);
					}
				}
			}

			try {
				siteService.save(site);

				// import tool content
				importToolContent(oldSiteId, site, false);

				String siteType = site.getType();

				// save again
				siteService.save(site);

				String realm = siteService.siteReference(site.getId());
				try {
					AuthzGroup realmEdit = authzGroupService
							.getAuthzGroup(realm);
					if (SiteTypeUtil.isCourseSite(siteType)) {
						// also remove the provider id attribute if any
						realmEdit.setProviderGroupId(null);
					}

					// add current user as the maintainer
					realmEdit.addMember(userDirectoryService.getCurrentUser()
							.getId(), site.getMaintainRole(), true, false);

					authzGroupService.save(realmEdit);
				} catch (GroupNotDefinedException e) {
					log.warn(
							this
									+ ".actionForTemplate chef_siteinfo-duplicate: IdUnusedException, not found, or not an AuthzGroup object "
									+ realm, e);
					// addAlert(state, rb.getString("java.realm"));
				} catch (AuthzPermissionException e) {
					// addAlert(state, this + rb.getString("java.notaccess"));
					log.warn(this
							+ ".actionForTemplate chef_siteinfo-duplicate: ", e);
				}

			} catch (IdUnusedException e) {
				log.warn(this
						+ " actionForTemplate chef_siteinfo-duplicate:: IdUnusedException when saving "
						+ newSiteId);
			} catch (PermissionException e) {
				log.warn(this
						+ " actionForTemplate chef_siteinfo-duplicate:: PermissionException when saving "
						+ newSiteId);
			}

			// TODO: hard coding this frame id
			// is fragile, portal dependent, and
			// needs to be fixed -ggolden
			// schedulePeerFrameRefresh("sitenav");
			// scheduleTopRefresh();

			// send site notification
			// sendSiteNotification(state, site, null);

			state.setAttribute(SITE_DUPLICATED, Boolean.TRUE);

			return newSiteId;
		} catch (IdInvalidException e) {
			// addAlert(state, rb.getString("java.siteinval"));
			log.warn(this + ".actionForTemplate chef_siteinfo-duplicate: "
					+ " site id = " + newSiteId, e);
		} catch (IdUsedException e) {
			// addAlert(state, rb.getString("java.sitebeenused"));
			log.warn(this + ".actionForTemplate chef_siteinfo-duplicate: "
					+ " site id = " + newSiteId, e);
		} catch (PermissionException e) {
			// addAlert(state, rb.getString("java.allowcreate"));
			log.warn(this + ".actionForTemplate chef_siteinfo-duplicate: "
					+ " site id = " + newSiteId, e);
		}
		return null;
	}*/

	/*private long getSiteSpecificQuota(String siteId) {
		long quota = 0;
		try {
			Site site = siteService.getSite(siteId);
			if (site != null) {
				quota = getSiteSpecificQuota(site);
			}
		} catch (IdUnusedException e) {
			log.warn("Quota calculation could not find the site " + siteId
					+ "for site specific quota calculation",
					log.isDebugEnabled() ? e : null);
		}
		return quota;
	}

	private long getSiteSpecificQuota(Site site) {
		long quota = 0;
		try {
			String collId = m_contentHostingService.getSiteCollection(site
					.getId());
			ContentCollection site_collection = m_contentHostingService
					.getCollection(collId);
			long siteSpecific = site_collection.getProperties()
					.getLongProperty(
							ResourceProperties.PROP_COLLECTION_BODY_QUOTA);
			quota = siteSpecific;
		} catch (Exception ignore) {
			log.warn("getQuota: reading quota property for site : "
					+ site.getId() + " : " + ignore);
			quota = 0;
		}
		return quota;
	}

	*//**
	 * get the Site object based on SessionState attribute values
	 * 
	 * @return Site object related to current state; null if no such Site object
	 *         could be found
	 *//*
	protected Site getStateSite(SessionState state) {
		return getStateSite(state, false);

	} // getStateSite

	*//**
	 * get the Site object based on SessionState attribute values
	 * 
	 * @param autoContext
	 *            - If true, we fall back to a context if it exists
	 * @return Site object related to current state; null if no such Site object
	 *         could be found
	 *//*
	protected Site getStateSite(SessionState state, boolean autoContext) {
		Site site = null;

		if (state.getAttribute(STATE_SITE_INSTANCE_ID) != null) {
			try {
				site = siteService.getSite((String) state
						.getAttribute(STATE_SITE_INSTANCE_ID));
			} catch (Exception ignore) {
			}
		}
		if (site == null && autoContext) {
			String siteId = toolManager.getCurrentPlacement().getContext();
			try {
				site = siteService.getSite(siteId);
				state.setAttribute(STATE_SITE_INSTANCE_ID, siteId);
			} catch (Exception ignore) {
			}
		}
		return site;
	} // getStateSite

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
						log.error("",e);
						// TODO:
					} catch (PermissionException e) {
						// TODO:
						log.error("",e);
					}
				}
			}
		}
	}

	*//**
	 * Notification sent when a course site is set up automatcally
	 * 
	 *//*
	
	 * private void sendSiteNotification(SessionState state, Site site, List
	 * notifySites) { boolean courseSite =
	 * SiteTypeUtil.isCourseSite(site.getType());
	 * 
	 * String term_name = ""; if (state.getAttribute(STATE_TERM_SELECTED) !=
	 * null) { term_name = ((AcademicSession) state
	 * .getAttribute(STATE_TERM_SELECTED)).getEid(); } // get the request email
	 * from configuration String requestEmail = getSetupRequestEmailAddress();
	 * User currentUser = UserDirectoryService.getCurrentUser(); if
	 * (requestEmail != null && currentUser != null) {
	 * userNotificationProvider.notifySiteCreation(site, notifySites,
	 * courseSite, term_name, requestEmail); } // if
	 * 
	 * // reset locale to user default
	 * 
	 * rb.setContextLocale(null);
	 * 
	 * }
	 // sendSiteNotification

	private String transferSiteResource(String oSiteId, String nSiteId,
			String siteAttribute) {
		String rv = "";

		String accessUrl = ServerConfigurationService.getAccessUrl();
		if (siteAttribute != null && siteAttribute.indexOf(oSiteId) != -1
				&& accessUrl != null) {
			// stripe out the access url, get the relative form of "url"
			Reference ref = entityManager.newReference(siteAttribute
					.replaceAll(accessUrl, ""));
			try {
				ContentResource resource = m_contentHostingService
						.getResource(ref.getId());
				// the new resource
				ContentResource nResource = null;
				String nResourceId = resource.getId().replaceAll(oSiteId,
						nSiteId);
				try {
					nResource = m_contentHostingService
							.getResource(nResourceId);
				} catch (Exception n2Exception) {
					// copy the resource then
					try {
						nResourceId = m_contentHostingService.copy(
								resource.getId(), nResourceId);
						nResource = m_contentHostingService
								.getResource(nResourceId);
					} catch (Exception n3Exception) {
						log.error("",n3Exception);
					}
				}

				// get the new resource url
				rv = nResource != null ? nResource.getUrl(false) : "";

			} catch (Exception refException) {
				log.warn(this
						+ ":transferSiteResource: cannot find resource with ref="
						+ ref.getReference() + " " + refException.getMessage());
			}
		}

		return rv;
	}

	*//**
	 * copy tool content from old site
	 * 
	 * @param oSiteId
	 * @param site
	 *//*
	private void importToolContent(String oSiteId, Site site,
			boolean bypassSecurity) {
		String nSiteId = site.getId();

		// import tool content
		if (bypassSecurity) {
			// importing from template, bypass the permission checking:
			// temporarily allow the user to read and write from assignments
			// (asn.revise permission)
			securityService.pushAdvisor(new SecurityAdvisor() {
				public SecurityAdvice isAllowed(String userId, String function,
						String reference) {
					return SecurityAdvice.ALLOWED;
				}
			});
		}

		List pageList = site.getPages();
		Set<String> toolsCopied = new HashSet<String>();

		Map transversalMap = new HashMap();

		if (!((pageList == null) || (pageList.size() == 0))) {
			for (ListIterator i = pageList.listIterator(); i.hasNext();) {
				SitePage page = (SitePage) i.next();

				List pageToolList = page.getTools();
				if (!(pageToolList == null || pageToolList.size() == 0)) {

					Tool tool = ((ToolConfiguration) pageToolList.get(0))
							.getTool();
					String toolId = tool != null ? tool.getId() : "";
					if (toolId.equalsIgnoreCase("sakai.resources")) {
						// handle
						// resource
						// tool
						// specially
						Map<String, String> entityMap = transferCopyEntities(
								toolId,
								m_contentHostingService
										.getSiteCollection(oSiteId),
								m_contentHostingService
										.getSiteCollection(nSiteId));
						if (entityMap != null) {
							transversalMap.putAll(entityMap);
						}
					} else if (toolId.equalsIgnoreCase(SITE_INFORMATION_TOOL)) {
						// handle Home tool specially, need to update the site
						// infomration display url if needed
						String newSiteInfoUrl = transferSiteResource(oSiteId,
								nSiteId, site.getInfoUrl());
						site.setInfoUrl(newSiteInfoUrl);
					} else {
						// other
						// tools
						// SAK-19686 - added if statement and toolsCopied.add
						if (!toolsCopied.contains(toolId)) {
							Map<String, String> entityMap = transferCopyEntities(
									toolId, oSiteId, nSiteId);
							if (entityMap != null) {
								transversalMap.putAll(entityMap);
							}
							toolsCopied.add(toolId);
						}
					}
				}
			}

			// update entity references
			toolsCopied = new HashSet<String>();
			for (ListIterator i = pageList.listIterator(); i.hasNext();) {
				SitePage page = (SitePage) i.next();

				List pageToolList = page.getTools();
				if (!(pageToolList == null || pageToolList.size() == 0)) {
					Tool tool = ((ToolConfiguration) pageToolList.get(0))
							.getTool();
					String toolId = tool != null ? tool.getId() : "";

					updateEntityReferences(toolId, nSiteId, transversalMap,
							site);
				}
			}
		}

		if (bypassSecurity) {
			securityService.popAdvisor();
		}
	}

	*//**
	 * Transfer a copy of all entites from another context for any entity
	 * producer that claims this tool id.
	 * 
	 * @param toolId
	 *            The tool id.
	 * @param fromContext
	 *            The context to import from.
	 * @param toContext
	 *            The context to import into.
	 *//*
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
	}*/
}
