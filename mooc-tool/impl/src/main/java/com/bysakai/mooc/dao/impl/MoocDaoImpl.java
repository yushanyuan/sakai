/**
 * 版权所有 北京思开科技有限公司 
 * All Rights Reserved
 */
package com.bysakai.mooc.dao.impl;

 
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.sakaiproject.genericdao.hibernate.HibernateGeneralGenericDao;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import com.bysakai.mooc.dao.MoocDao;
import com.bysakai.mooc.model.Course;

/**
 * @author nesdu
 *
 */
public class MoocDaoImpl extends HibernateGeneralGenericDao implements MoocDao {
	private static Log log = LogFactory.getLog(MoocDaoImpl.class);

    public void init() {
        log.debug("init");
    }

    public List findByNamedQueryAndNamedParam(String queryName, String[] paramNames, Object[] values){
    	return getHibernateTemplate().findByNamedQueryAndNamedParam(queryName, paramNames, values);
    }
    
    public List findCourse(final String speId,final String q,final String stutas,final Integer pages){
    	return getHibernateTemplate().executeFind(new HibernateCallback<List<Course>>() {
			@Override
			public List<Course> doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer sb = new StringBuffer();
		    	sb.append("from com.bysakai.mooc.model.Course course where 1=1 ");
		    	if(speId!=null && !speId.equals("")){
		    		sb.append(" and course.category.id = '"+speId+"'");
		    	}
		    	
		    	if(q!=null && !q.equals("")){
		    		sb.append(" and (course.name like '%"+q+"%'");
		    		sb.append(" or teacher.name like '%"+q+"%')");
		    	}
		    	if(stutas!=null && !stutas.equals("")){
		    		if(stutas.equals("1")){
		    			sb.append(" and startDate > :sd ");
		    		}
		    		if(stutas.equals("2")){
		    			sb.append(" and startDate < :sd ");
		    		}
		    	}
		    	
		    	Integer pageSize = 10;
		    	Query query = session.createQuery(sb.toString()).setFirstResult((pages-1)*pageSize).setMaxResults(pageSize);
		    	if(!StringUtils.isEmpty(stutas)){
		    		query.setDate("sd", new Date());
		    	}
				return query.list();
			}
		});
    			
    }
    
}
