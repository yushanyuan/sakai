/**
 * 
 */
package org.sakaiproject.resource.impl.forum.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sakaiproject.api.app.messageforums.BaseForum;
import org.sakaiproject.api.app.messageforums.Message;
import org.sakaiproject.api.app.messageforums.Topic;
import org.sakaiproject.db.api.SqlService;
import org.sakaiproject.resource.api.forum.model.AreaModel;
import org.sakaiproject.resource.api.forum.model.ForumModel;
import org.sakaiproject.resource.api.forum.model.TopicModel;
import org.sakaiproject.resource.api.forum.service.ForumService;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.tool.api.ToolManager;
import org.sakaiproject.site.api.SiteService;

/**
 * @author yushanyuan
 *
 */
public class MessageForumServiceImpl implements ForumService {

	public static String FORUM_NAME = "sakai.forums";

	private org.sakaiproject.api.app.messageforums.MessageForumsForumManager messageForumsForumManager;
	private org.sakaiproject.api.app.messageforums.MessageForumsMessageManager messageForumsMessageManager;
	private org.sakaiproject.api.app.messageforums.MessageForumsTypeManager messageForumsTypeManager;

	protected SqlService sqlService = null;

	public void setSqlService(SqlService service) {
		this.sqlService = service;
	}

	public void setMessageForumsForumManager(
			org.sakaiproject.api.app.messageforums.MessageForumsForumManager messageForumsForumManager) {
		this.messageForumsForumManager = messageForumsForumManager;
	}

	public void setMessageForumsMessageManager(
			org.sakaiproject.api.app.messageforums.MessageForumsMessageManager messageForumsMessageManager) {
		this.messageForumsMessageManager = messageForumsMessageManager;
	}

	public void setMessageForumsTypeManager(
			org.sakaiproject.api.app.messageforums.MessageForumsTypeManager messageForumsTypeManager) {
		this.messageForumsTypeManager = messageForumsTypeManager;
	}

	// -------------------------
	/*
	 * public static String selectAllForumByCourseId_sql =
	 * " SELECT f.id AS FORUMID, f.TITLE AS TITLE FROM mfr_area_t AS a , mfr_open_forum_t AS f WHERE a.ID=f.surrogateKey AND a.CONTEXT_ID= ? "
	 * ;
	 * 
	 * public static String selectAllTopicByForum =
	 * " SELECT t.id AS TOPICID , t.TITLE AS TITLE FROM mfr_topic_t AS t WHERE t.of_surrogateKey = ? "
	 * ;
	 * 
	 * public static String selectAllMessageByTopic =
	 * "  SELECT m.ID, m.TITLE FROM mfr_message_t AS m WHERE m.surrogateKey=? AND m.THREADID IS NULL  "
	 * ;
	 */
	public static String countTopicNum = "SELECT COUNT(m.id) AS CUNT , m.CREATED_BY AS USERID FROM mfr_message_t AS m, sakai_user_id_map AS u  WHERE m.CREATED_BY = u.USER_ID AND m.surrogateKey=?  AND m.draft = false AND m.deleted = false AND u.EID  =? GROUP BY m.CREATED_BY ";

	public static String countTopicNumByUsername = "SELECT COUNT(m.id) AS CUNT , m.surrogateKey AS TOPICID, m.CREATED_BY AS USERID FROM mfr_message_t AS m, sakai_user_id_map AS u  WHERE m.CREATED_BY = u.USER_ID AND m.draft = false AND m.deleted = false AND  u.EID  =? GROUP BY  m.CREATED_BY, m.surrogateKey ";

	public static String queryParentMessageByTopic = "SELECT id as ID, title AS TITLE FROM mfr_message_t WHERE THREADID IS NULL AND surrogatekey = ? ";

	public static String error_message = "";

	// --------------------------
	public String getName() {
		return FORUM_NAME;//"messageForum";
	}

	@Override
	public List<ForumModel> selectAllForumByArea(String areaId) throws Exception {
		List<ForumModel> result = new ArrayList<ForumModel>();
		if (areaId == null) {
			return result;
		}
		List topicList = messageForumsForumManager.getTopicsByIdWithMessages(Long.parseLong(areaId));
		if (topicList != null && topicList.size() > 0) {
			for (int i = 0; i < topicList.size(); i++) {
				Topic f = (Topic) topicList.get(i);
				ForumModel m = new ForumModel();
				m.setId("" + f.getId());
				m.setName(f.getTitle());
				result.add(m);
			}
		}
		return result;
	}

	@Override
	public List<TopicModel> selectAllTopicByForum(String forumId) throws Exception {
		List<TopicModel> topics = new ArrayList<TopicModel>();
		if (forumId == null) {
			return topics;
		}
		
		Connection connection = sqlService.borrowConnection();
		PreparedStatement p = connection.prepareStatement(queryParentMessageByTopic);
		p.setLong(1, Long.parseLong(forumId));

		ResultSet rs = p.executeQuery();
		while (rs.next()) {
			TopicModel t = new TopicModel();
			t.setTopicId("" + rs.getInt("ID"));
			t.setTitle(rs.getString("TITLE"));
			topics.add(t);
		}
		rs.close();
		p.close();
		
		return topics;
	}

	@Override
	public List<AreaModel> selectAllArea() throws Exception {
		List<AreaModel> result = new ArrayList<AreaModel>();
		List forumList = messageForumsForumManager.getForumByTypeAndContext(messageForumsTypeManager
				.getDiscussionForumType());
		if (forumList != null && forumList.size() > 0) {
			for (int i = 0; i < forumList.size(); i++) {
				BaseForum f = (BaseForum) forumList.get(i);
				AreaModel m = new AreaModel();
				m.setId("" + f.getId());
				m.setName(f.getTitle());
				result.add(m);
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sakaiproject.resource.api.forum.service.SakaiForumToolService#
	 * generateUriString(int)
	 */
	@Override
	public String generateUriString(String areaId, String forumId, String topicId) {
		// http://test.nesdu.com:8080/portal/tool/9e382267-02f8-45ad-9b1f-c16bf8b14634/discussionForum/message/dfViewMessageDirect.jsf?messageId=483&topicId=5228&forumId=162

		String url = "/tool/";
		String siteId = ((ToolManager) ComponentManager.get(ToolManager.class)).getCurrentPlacement().getContext();
		try {
			// Site site = SiteService.getSite(siteId);
			// ToolConfiguration t = site.getToolForCommonId("");
			url = url
					+ ((SiteService) ComponentManager.get(SiteService.class)).getSite(siteId)
							.getToolForCommonId(FORUM_NAME).getId();
			url = url + "/discussionForum/message/dfViewThreadDirect.jsf?messageId=";
			url = url + topicId;
			url = url + "&topicId=" + forumId;
			url = url + "&forumId=" + areaId;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url;
	}

	@Override
	public int countPostNumByTopicAndStudentName(String areaId, String forumId, String topicID, String userName)
			throws Exception {
		int num = 0;
		Connection connection = sqlService.borrowConnection();
		PreparedStatement p = connection.prepareStatement(countTopicNum);
		p.setLong(1, Long.parseLong(forumId));
		p.setString(2, userName);

		ResultSet rs = p.executeQuery();
		if (rs.next()) {
			num = rs.getInt("CUNT");
		}
		rs.close();
		p.close();
		if (connection != null)
			sqlService.returnConnection(connection);
		return num;
	}

	@Override
	public Map<String, Integer> countPostNumByTopicAndStudentName(String userName) throws Exception {
		Map<String, Integer> countRes = new HashMap<String, Integer>();

		Connection connection = sqlService.borrowConnection();
		PreparedStatement p = connection.prepareStatement(countTopicNumByUsername);
		p.setString(1, userName);
		ResultSet rs = p.executeQuery();
		while (rs.next()) {
			countRes.put(rs.getString("TOPICID"), Integer.valueOf(rs.getString("CUNT")));
		}
		rs.close();
		p.close();
		if (connection != null)
			sqlService.returnConnection(connection);
		return countRes;
	}

}
