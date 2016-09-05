/**
 * 版权所有 北京思开科技有限公司 
 * All Rights Reserved
 */
package org.sakaiproject.cmsrest.logic;

import java.sql.SQLException;

/**
 * @Description: TODO
 * @author: yushanyuan
 *
 */
public interface CmDao {

	/**
	 * @param title
	 * @return
	 */
	public String getEIdByTitle(String title) throws SQLException;
}
