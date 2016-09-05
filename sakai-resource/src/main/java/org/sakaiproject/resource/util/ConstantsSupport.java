package org.sakaiproject.resource.util;

import java.util.Map;

import org.sakaiproject.component.api.ServerConfigurationService;


/**
 * Constants support类。
 * 
 */
public class ConstantsSupport {

	private static java.util.Properties pro = new java.util.Properties();

	static {
		try {
			pro.load(Constants.class.getResourceAsStream("/pathconfig.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Map<String, String> constants;
	
	private ServerConfigurationService serverConfigurationService;
	
	public static ConstantsSupport getInstance() {
		return (ConstantsSupport) SpringContextUtil.getBean("constantsSupport");
	}


	public ServerConfigurationService getServerConfigurationService() {
		return serverConfigurationService;
	}


	public void setServerConfigurationService(
			ServerConfigurationService serverConfigurationService) {
		this.serverConfigurationService = serverConfigurationService;
	}


	/*public Map<String, String> getConstants() {
		return constants;
	}*/

	public void setConstants(Map<String, String> constants) {
		this.constants = constants;
	}
	
	public String get(String key){
		String value = serverConfigurationService.getString(key);
		if(null == value || "".equals(value)){
			value = pro.getProperty(key);
			if(null == value || "".equals(value)){
				value = this.constants.get(key);
			}
		}
		return value;
	}

}
