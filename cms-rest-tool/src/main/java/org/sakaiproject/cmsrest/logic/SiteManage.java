/**
 * 
 */
package org.sakaiproject.cmsrest.logic;

import java.util.List;
import java.util.Set;

import org.sakaiproject.authz.api.AuthzPermissionException;
import org.sakaiproject.authz.api.GroupNotDefinedException;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.exception.PermissionException;
import org.sakaiproject.user.api.User;
import org.sakaiproject.user.api.UserAlreadyDefinedException;
import org.sakaiproject.user.api.UserIdInvalidException;
import org.sakaiproject.user.api.UserLockedException;
import org.sakaiproject.user.api.UserNotDefinedException;
import org.sakaiproject.user.api.UserPermissionException;

/**
 * sakai站点操作类
 * 
 * @author yushanyuan
 *
 */
public interface SiteManage {


 
	/**
	 *  新建用户
	 * @param id
	 * @param eid
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param pw
	 * @param type
	 * @return
	 * @throws UserNotDefinedException
	 * @throws UserPermissionException
	 * @throws UserLockedException
	 * @throws UserIdInvalidException
	 * @throws UserAlreadyDefinedException
	 */
	User addUser(String id, String eid, String firstName, String lastName,
			String email, String pw, String type)
			throws Exception;
	
	/**
	 * 
	 * @param userId
	 * @throws Exception
	 */
	void removeUser(String userId)throws Exception;

	 
	/**
	 *  把用户加入站点
	 * @param siteId
	 * @param userEidList
	 * @param roleList
	 * @throws IdUnusedException
	 * @throws GroupNotDefinedException
	 * @throws UserNotDefinedException
	 * @throws AuthzPermissionException
	 */
	public void addUsersToSite(String siteId, List<String> userIdList,
			List<String> roleList) throws Exception;

	/**
	 * 把用户从站点删除
	 * @param siteId
	 * @param userEidList
	 * @throws GroupNotDefinedException
	 * @throws UserNotDefinedException
	 * @throws AuthzPermissionException
	 */
	public void removeUsersFromSite(String siteId, List<String> userIdList)
			throws Exception;

	/**
	 * 获取站点下指定角色的用户id
	 * 
	 * @param siteId
	 * @return
	 */
	public Set<String> queryUserIdBySiteId(String siteId, String role) throws Exception;
	
}
