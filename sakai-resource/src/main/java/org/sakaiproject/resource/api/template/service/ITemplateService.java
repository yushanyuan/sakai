package org.sakaiproject.resource.api.template.service;

import java.util.List;

import org.sakaiproject.resource.api.template.model.TemplateModel;


public interface ITemplateService {

	/**
	 * 保存对象
	 * @param model
	 * @throws Exception
	 */
	public void save(TemplateModel model) throws Exception;
	
	/**
	 * 启用/停用
	 * @param id
	 * @throws Exception
	 */
	public void publish(String id) throws Exception;
	
	/**
	 * 删除
	 * @param id
	 * @throws Exception
	 */
	public void delete(TemplateModel model) throws Exception;
	
	public List<TemplateModel> query(String status, int pageSize, int page) throws Exception;
	
	/**
	 * 获取可使用的模板列表：默认模板优先
	 * @return
	 * @throws Exception
	 */
	public List<TemplateModel> getListForUse() throws Exception;
	
	/**
	 * 获取
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public TemplateModel get(String id) throws Exception;
	
	
	/**
	 * 根据名称获取
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public TemplateModel getByName(String name) throws Exception;
	
	/**
	 * 获取第一个默认模板（应该设置为唯一）
	 * @return
	 * @throws Exception
	 */
	public TemplateModel getDefault() throws Exception;

}
