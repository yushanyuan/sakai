/**
 * 
 */
package org.sakaiproject.resource.impl.forum.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.sakaiproject.resource.api.forum.model.AreaModel;
import org.sakaiproject.resource.api.forum.model.ForumModel;
import org.sakaiproject.resource.api.forum.model.TopicModel;
import org.sakaiproject.resource.api.forum.service.ForumService;


/**
 * @author yushanyuan
 *2106-11-29 sakai11中不再使用jforum, 因此这个类屏蔽
 */
public class JForumServiceImpl  implements ForumService {
	public static String FORUM_NAME = "sakai.jforum.tool";
	
	/*private org.sakaiproject.api.app.jforum.JForumToolService jForumToolService;

	public void setjForumToolService(
			org.sakaiproject.api.app.jforum.JForumToolService jForumToolService) {
		this.jForumToolService = jForumToolService;
	}
*/
	public String getName(){
		return FORUM_NAME;//"jForum";
	}
	@Override
	public List<ForumModel> selectAllForumByArea(String areaId)
			throws Exception {
		List<ForumModel> forums = new ArrayList<ForumModel>();
		/*List<org.sakaiproject.api.app.jforum.ForumModel> list = jForumToolService.selectAllForumByCourse();
		if(list!=null && list.size()>0){
			for(org.sakaiproject.api.app.jforum.ForumModel f : list){
				ForumModel m = new ForumModel();
				m.setId(""+f.getId());
				m.setName(f.getName());
				forums.add(m);
			}
		}*/
		return forums;
	}

	@Override
	public List<TopicModel> selectAllTopicByForum(String forumId)
			throws Exception {
		List<TopicModel> topics = new ArrayList<TopicModel>();
		/*List<org.sakaiproject.api.app.jforum.TopicModel> list = jForumToolService.selectAllTopicByForum(Integer.parseInt(forumId), 0, Integer.MAX_VALUE);
		
		if(list!=null && list.size()>0){
			for(org.sakaiproject.api.app.jforum.TopicModel t : list){
				TopicModel m = new TopicModel();
				m.setTopicId(""+t.getTopicId());
				m.setTitle(t.getTitle());
				topics.add(m);
			}
		}*/
		return topics;
	}

	@Override
	public List<AreaModel> selectAllArea() throws Exception {
		return null;
	}

	@Override
	public String generateUriString(String areaId, String forumId, String topicId) {
		//return jForumToolService.generateUriString(Integer.parseInt(topicId));
		return null;
	}

	@Override
	public int countPostNumByTopicAndStudentName(String areaId, String forumId, String topicID, String userName)
			throws Exception {
		//return jForumToolService.countPostNumByTopicAndStudentName(Integer.parseInt(topicID), userName);
		return 0;
	}

	@Override
	public Map<String, Integer> countPostNumByTopicAndStudentName(
			String userName) throws Exception {
		//return jForumToolService.countPostNumByTopicAndStudentName(userName);
		return null;
	}
}
