/**   
 *   
 * 版本信息：   
 * Copyright 思开科技    
 * 版权所有   
 */
package org.sakaiproject.cmsrest.logic.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.cmsrest.logic.ConfigLogic;
/**
 * @author yushanyuan
 *
 */
public class ConfigLogicImpl implements ConfigLogic {
	
	private static Log logger = LogFactory.getLog(ConfigLogicImpl.class);
	
	private String account;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	
	public void init() {
		logger.info("init");
	}
}
