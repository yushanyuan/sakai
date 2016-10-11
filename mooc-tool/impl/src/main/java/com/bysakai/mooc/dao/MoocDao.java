/**
 * 版权所有 北京思开科技有限公司 
 * All Rights Reserved
 */
package com.bysakai.mooc.dao;

import java.util.List;

/**
 * @author nesdu
 *
 */
import org.sakaiproject.genericdao.api.GeneralGenericDao;


public interface MoocDao extends GeneralGenericDao{
	 public List findByNamedQueryAndNamedParam(String queryName, String[] paramNames, Object[] values);

	 public List findCourse(final String speId,final String q,final String stutas,final Integer pages);
}
