package org.sakaiproject.resource.util;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

/**
 * HibernateDao封装类，所有需要访问数据库的service继承此类。
 * 
 * @author 胡浩
 * 
 */
public class HibernateDaoSupport extends
		org.springframework.orm.hibernate3.support.HibernateDaoSupport {
	private static Log log = LogFactory.getLog(HibernateDaoSupport.class);
	/**
	 * 默认的分页每页记录数-20条
	 */
	public static final int LIMIT = 20;

	/**
	 * 创建一个对象
	 * 
	 * @author 胡浩
	 * @version 1.0.0
	 * @param entity
	 *            一个POJO对象，主键应为空
	 */
	public String createEntity(Object entity) {
		try {
			Object id = this.getHibernateTemplate().save(entity);
			return id.toString() ;
		} catch (RuntimeException re) {
			log.error("增加" + entity.toString() + "失败！", re);
			throw re;
		}
	}

	/**
	 * 修改一个对象
	 * 
	 * @author 胡浩
	 * @version 1.0.0
	 * @param entity
	 *            一个POJO对象，主键应不为空
	 */
	public void updateEntity(Object entity) {
		try {
			this.getHibernateTemplate().merge(entity);
		} catch (RuntimeException re) {
			re.printStackTrace();
			log.error("修改" + entity.toString() + "失败！", re);
			throw re;
		}
	}

	/**
	 * 删除一个对象
	 * 
	 * @author 胡浩
	 * @version 1.0.0
	 * @param entity
	 *            一个POJO对象，主键应不为空
	 */
	public void deleteEntity(Object entity) {
		try {
			this.getHibernateTemplate().delete(entity);
		} catch (RuntimeException re) {
			log.error("删除" + entity.toString() + "失败！", re);
			throw re;
		}
	}

	/**
	 * 删除一个对象
	 * 
	 * @author 胡浩
	 * @version 1.0.0
	 * @param clazz
	 *            POJO的class
	 * @param id
	 *            主键
	 */
	public void deleteEntity(Class clazz, String id) {
		this.getHibernateTemplate().delete(
				this.getHibernateTemplate().load(clazz, id));
	}

	/**
	 * 通过无参数的HQL执行修改或删除操作。
	 * 
	 * @author 胡浩
	 * @version 1.0.0
	 * @param queryString
	 *            无参数的HQL
	 * @return 执行修改或删除的记录数量
	 */
	public int updateEntity(final String queryString) {
		Integer result = (Integer) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session) {
						Query query = session.createQuery(queryString);
						return query.executeUpdate();
					}
				});
		return result.intValue();
	}

	/**
	 * <b>当参数较少时推荐使用此方法</b><br/> 通过带参数的HQL执行修改或删除操作。
	 * 
	 * @author 胡浩
	 * @version 1.0.0
	 * @param queryString
	 *            带参数的HQL，采用问号进行占位
	 * @param parameters
	 *            参数数组，注意数组的顺序要与HQL中的问号一一对应
	 * @return 执行修改或删除的记录数量
	 */
	public int updateEntity(final String queryString, final Object[] parameters) {
		Integer result = (Integer) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session) {
						Query query = session.createQuery(queryString);
						if (parameters != null) {
							for (int i = 0; i < parameters.length; i++) {
								query.setParameter(i, parameters[i]);
							}
						}
						return query.executeUpdate();
					}
				});
		return result.intValue();
	}

	/**
	 * <b>当参数较多时推荐使用此方法</b><br/> 通过带参数的HQL执行修改或删除操作。示例代码如下：
	 * 
	 * <pre>
	 * Map&lt;String, Object&gt; parameters = new HashMap&lt;String, Object&gt;();
	 * parameters.put(&quot;fileId&quot;, fileId);
	 * parameters.put(&quot;fileStatus&quot;, status);
	 * String queryString = &quot;update FileMeta set fileStatus=:fileStatus where fileId=:fileId&quot;;
	 * this.updateEntity(queryString, parameters);
	 * </pre>
	 * 
	 * @author 胡浩
	 * @version 1.0.0
	 * @param queryString
	 *            带参数的HQL，采用命名变量进行占位。
	 * @param parameters
	 *            参数Map，注意参数的名称应与HQL中的绑定变量相同
	 * @return 执行修改或删除的记录数量
	 */
	public int updateEntity(final String queryString,
			final Map<String, Object> parameters) {
		Integer result = (Integer) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session) {
						Query query = session.createQuery(queryString);
						if (parameters != null) {
							Iterator<String> it = parameters.keySet()
									.iterator();
							while (it.hasNext()) {
								String key = it.next();
								if(parameters.get(key).getClass().isArray()){
									query.setParameterList(key, (String[])parameters.get(key));
								}else{
									query.setParameter(key, parameters.get(key));
								}
							}
						}
						return query.executeUpdate();
					}
				});
		return result.intValue();
	}

	/**
	 * 用于得到指定ID的对象
	 * 
	 * @author 胡浩
	 * @version 1.0.0
	 * @param clazz
	 *            POJO的class
	 * @param id
	 *            主键
	 * @return 指定的对象
	 */
	public Object findEntityById(Class clazz, Object id) {
		return this.getHibernateTemplate().get(clazz, (Serializable) id);
	}

	/**
	 * 用于得到全部对象
	 * 
	 * @author 胡浩
	 * @version 1.0.0
	 * @param clazz
	 *            POJO的class
	 * @return 全部对象
	 */
	public List findAllEntity(Class clazz) {
		return this.getHibernateTemplate().find("from " + clazz.getName());
	}

	/**
	 * 通过指定无参数的HQL进行查询
	 * 
	 * @author 胡浩
	 * @version 1.0.0
	 * @param queryString
	 *            无参数的HQL
	 * @return 查询结果
	 */
	public List findEntity(String queryString) {
		return this.getHibernateTemplate().find(queryString);
	}

	/**
	 * 通过带参数的HQL和参数值进行查询。
	 * 
	 * @author 胡浩
	 * @version 1.0.0
	 * @param queryString
	 *            带参数的HQL，采用问号进行占位
	 * @param parameters
	 *            参数数组，注意数组的顺序应与HQL中的问号一一对应
	 * @return 查询结果
	 */
	public List findEntity(String queryString, Object[] parameters) {
		return getHibernateTemplate().find(queryString, parameters);
	}

	/**
	 * 通过带参数的HQL和参数值进行查询。示例代码如下：
	 * 
	 * <pre>
	 * // HQL参数Map
	 * Map&lt;String, String&gt; values = new HashMap&lt;String, String&gt;();
	 * values.put(&quot;parentId&quot;, parentId);// 父id
	 * // 查询指定ID的下级栏目
	 * String queryString = &quot;from Category where categoryParentId = :parentId order by categorySort,categoryName&quot;;
	 * return this.findEntity(queryString, values);
	 * </pre>
	 * 
	 * @param queryString
	 *            带参数的HQL，采用命名变量进行占位
	 * @param parameters
	 *            参数Map，注意参数的名称应与HQL中的命名变量相同
	 * @return 查询结果
	 */
	public List findEntity(final String queryString, final Map parameters) {
		if (queryString == null || parameters == null) {
			throw new IllegalArgumentException("HQL和参数不能为空");
		}
		log.debug("执行查询...");
		log.debug("查询语句：" + queryString);

		return (List) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public List doInHibernate(Session session)
							throws HibernateException {
						Query queryObject = session.createQuery(queryString);
						Iterator it = parameters.keySet().iterator();

						while (it.hasNext()) {
							Object key = it.next();
							queryObject.setParameter(key.toString(), parameters
									.get(key));
						}
						return queryObject.list();
					}
				});
	}

	/**
	 * 分页查询，返回一个对象数组。示例如下：
	 * 
	 * <pre>
	 * 
	 * Map&lt;String, Object&gt; parameters = new HashMap&lt;String, Object&gt;();//定义查询条件
	 * parameters.put(&quot;storyId&quot;, storyId);//设置查询条件
	 * 
	 * QueryString queryString = new QueryString();//定义一个查询对象
	 * queryString.setSelect(&quot;select b &quot;);//设置select字句
	 * queryString.setFrom(&quot; from Story a,Folder b &quot;);//设置from字句
	 * queryString.setWhere(&quot; where a.id=b.storyId and a.id=:storyId&quot;);//设置where字句
	 * 
	 * return this.findEntity(queryString, parameters, HibernateDaoSupport.LIMIT,
	 * 		start);//返回一个数组。数组中第一个对象是总记录数，第二个对象是list。
	 * </pre>
	 * 
	 * @param queryString
	 *            用于分页查询的hql对象
	 * @param parameters
	 *            查询参数
	 * @param limit
	 *            每页记录数
	 * @param start
	 *            从第多少条记录开始
	 * @return Object[0]-int,总记录数;Object[1]-List,记录
	 */
	public Object[] findEntity(final QueryString queryString,
			final Map parameters, final int limit, final int start) {
		Object[] obj = new Object[2];
		if (queryString == null || parameters == null) {
			throw new IllegalArgumentException("HQL和参数不能为空");
		}

		if (limit > 1000 || limit < 1) {
			throw new IllegalArgumentException("limit值无效");
		}
		if (start < 0) {
			throw new IllegalArgumentException("start值无效");
		}
		log.debug("执行查询...");
		log.debug("查询语句：" + queryString);
		Object object = this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						Query queryObject = session.createQuery(queryString
								.getCountSql());

						Iterator it = parameters.keySet().iterator();

						while (it.hasNext()) {
							Object key = it.next();
							queryObject.setParameter(key.toString(), parameters
									.get(key));
						}

						List countList = queryObject.list();

						return countList.get(0);
					}

				});
		int count = NumberUtils.toInt(object.toString());

		List list = (List) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public List doInHibernate(Session session)
							throws HibernateException {
						Query queryObject = session.createQuery(queryString
								.getSql());
						Iterator it = parameters.keySet().iterator();

						while (it.hasNext()) {
							Object key = it.next();
							queryObject.setParameter(key.toString(), parameters
									.get(key));
						}
						queryObject.setFirstResult(start);
						queryObject.setMaxResults(limit);
						return queryObject.list();
					}
				});
		obj[0] = count;
		obj[1] = list;
		return obj;
	}

	/**
	 * 分页查询，返回一个分页对象。
	 * 
	 * @param queryString
	 *            用于分页查询的hql
	 * @param parameters
	 *            查询参数
	 * @param limit
	 *            每页记录数
	 * @param start
	 *            从第多少条记录开始
	 * @return 分页对象
	 */
	public HibernatePager findPagerEntity(final QueryString queryString,
			final Map parameters, final int limit, final int pageNo) {
		Object[] obj = findEntity(queryString, parameters, limit, (pageNo - 1)
				* limit);
		return new HibernatePager(limit, pageNo, (Integer) obj[0],
				(List) obj[1]);
	}

	/**
	 * 一个通用的分页查询方法，此方法无法返回总的记录数和页数。 一般情况下不推荐使用此方法。
	 * 
	 * @param queryString
	 *            带参数的HQL，采用命名变量进行占位
	 * @param parameters
	 *            参数Map，注意参数的名称应与HQL中的命名变量相同
	 * @param pageSize
	 *            每页记录数
	 * @param pageNo
	 *            第几页
	 * @return 一个分页对象，包含查询结果和分页信息
	 */
	public HibernatePager findPagerEntity(final String queryString,
			final Map parameters, final int pageSize, final int pageNo) {
		if (queryString == null || parameters == null) {
			throw new IllegalArgumentException("HQL和参数不能为空");
		}

		if (pageSize > 50 || pageSize < 1) {
			throw new IllegalArgumentException("pageSize值无效");
		}
		if (pageNo < 1) {
			throw new IllegalArgumentException("pageNo值无效");
		}
		log.debug("执行查询...");
		log.debug("查询语句：" + queryString);
		return new HibernatePager(pageSize, pageNo, 0, (List) this
				.getHibernateTemplate().execute(new HibernateCallback() {
					public List doInHibernate(Session session)
							throws HibernateException {
						Query queryObject = session.createQuery(queryString);
						Iterator it = parameters.keySet().iterator();

						while (it.hasNext()) {
							Object key = it.next();
							queryObject.setParameter(key.toString(), parameters
									.get(key));
						}
						queryObject.setFirstResult((pageNo - 1) * pageSize);
						queryObject.setMaxResults(pageSize);
						return queryObject.list();
					}
				}));
	}

}
