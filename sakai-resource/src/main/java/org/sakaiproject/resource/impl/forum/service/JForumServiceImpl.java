package org.sakaiproject.resource.impl.forum.service;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.sakaiproject.resource.api.forum.model.AreaModel;
import org.sakaiproject.resource.api.forum.model.ForumModel;
import org.sakaiproject.resource.api.forum.model.TopicModel;
import org.sakaiproject.resource.api.forum.service.ForumService;


/**
 * @author yushanyuan
 * 已去掉 JForum 论坛的依赖，所以这个类只是实现一个空接口。
 */
public class JForumServiceImpl  implements ForumService {
	public static String FORUM_NAME = "sakai.jforum.tool";

	public String getName(){
		return FORUM_NAME;//"jForum";
	}
	@Override
	public List<ForumModel> selectAllForumByArea(String areaId)
			throws Exception {
		List<ForumModel> forums = new ArrayList<ForumModel>();
		return forums;
	}

	@Override
	public List<TopicModel> selectAllTopicByForum(String forumId)
			throws Exception {
		List<TopicModel> topics = new ArrayList<TopicModel>();
		return topics;
	}

	@Override
	public List<AreaModel> selectAllArea() throws Exception {
		return null;
	}

	@Override
	public String generateUriString(String areaId, String forumId, String topicId) {
		return "";
	}

	@Override
	public int countPostNumByTopicAndStudentName(String areaId, String forumId, String topicID, String userName)
			throws Exception {
		return 0;
	}

	@Override
	public Map<String, Integer> countPostNumByTopicAndStudentName(
			String userName) throws Exception {
		Map<String, Integer> countRes = new HashMap<String, Integer>();
		return countRes;
	}
}
