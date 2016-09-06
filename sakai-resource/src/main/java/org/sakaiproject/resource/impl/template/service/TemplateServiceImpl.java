package org.sakaiproject.resource.impl.template.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.sakaiproject.resource.api.template.model.TemplateModel;
import org.sakaiproject.resource.api.template.service.ITemplateService;
import org.sakaiproject.resource.util.HibernateDaoSupport;

/**
 * 课件模板管理
 * 
 * @author cedarwu
 * 
 */
public class TemplateServiceImpl extends HibernateDaoSupport implements ITemplateService {

	public void save(TemplateModel model) throws Exception {
		this.getHibernateTemplate().saveOrUpdate(model);
	}

	public void publish(String id) throws Exception {
		TemplateModel m = this.get(id);
		String status = TemplateModel.ENUM_STATUS_ENABLED;
		if (m.getStatus() != null && m.getStatus().equals(TemplateModel.ENUM_STATUS_ENABLED)) {
			status = TemplateModel.ENUM_STATUS_DISABLED;
		}

		Map<String, Object> parameters = new HashMap<String, Object>();
		StringBuffer queryString = new StringBuffer("update TemplateModel m set m.status=:status where m.id=:id");
		parameters.put("status", status);
		parameters.put("id", id);
		this.updateEntity(queryString.toString(), parameters);
	}

	public void delete(TemplateModel m) throws Exception {
		this.deleteEntity(m);
	}

	public List<TemplateModel> query(String status, int pageSize, int page) throws Exception {
		String hql = "from TemplateModel where 1=1 ";
		Object[] parameters = null;
		if (StringUtils.isNotBlank(status)) {
			hql = hql + " and status=?";
			parameters = new Object[] { status };
		}

		hql = hql + " order by isDefault desc, name";
		List<TemplateModel> list = this.findEntity(hql, parameters);

		return list;
	}

	public TemplateModel get(String id) throws Exception {
		return (TemplateModel) this.findEntityById(TemplateModel.class, id);
	}

	public List<TemplateModel> getListForUse() throws Exception {
		return query(TemplateModel.ENUM_STATUS_ENABLED, 1000, 0);
	}

	public TemplateModel getDefault() throws Exception {
		String hql = "from TemplateModel where status=? and isDefault=true";
		Object[] parameters = { TemplateModel.ENUM_STATUS_ENABLED };
		List<TemplateModel> list = this.findEntity(hql, parameters);

		if (list == null || list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	public TemplateModel getByName(String name) throws Exception {
		String hql = "from TemplateModel where name=?";
		Object[] parameters = { name };
		List<TemplateModel> list = this.findEntity(hql, parameters);

		if (list == null || list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

}
