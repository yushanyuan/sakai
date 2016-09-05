/**
 * 
 */
package org.sakaiproject.resource.api.forum.service;

import java.util.List;
import java.util.Map;

import org.sakaiproject.resource.api.forum.model.AreaModel;
import org.sakaiproject.resource.api.forum.model.ForumModel;
import org.sakaiproject.resource.api.forum.model.TopicModel;

/**
 * 查找论坛的一些信息
 * @author yushanyuan
 *
 */
public interface ForumService {
	
	/**
	 * 论坛类型
	 * @return
	 */
	public abstract String getName();
	
	/**
	 * @return
	 * @throws Exception
	 *             根据讨论区找到所有的论坛;
	 */
	public abstract List<ForumModel> selectAllForumByArea(String areaId) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 *             根据论坛找到所有的主题;
	 */
	public abstract List<TopicModel> selectAllTopicByForum(String forumId) throws Exception;

	/**
	 * 根据站点 返回 所有的讨论区
	 * @param topicId
	 * @return
	 * @throws Exception
	 */
	public abstract List<AreaModel> selectAllArea()throws Exception;
	
	/**
	 * @param areaId
	 * @param forumId
	 * @param topicId
	 * @return 根据话题的id生成该话题的uri，以便学生在学习时进入话题；
	 */
	public abstract String generateUriString(String areaId, String forumId, String topicId);

	/**
	 * 根据学生ID，论坛ID统计数
	 * 
	 * @return
	 */
	public abstract int countPostNumByTopicAndStudentName(String areaId, String forumId, String topicID, String userName) throws Exception;

	/**
	 * 根据学生ID，论坛ID数组 统计回帖数
	 * 
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	public abstract Map<String, Integer> countPostNumByTopicAndStudentName(String userName) throws Exception;
}
