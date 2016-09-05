/**
 * 
 */
package org.sakaiproject.cmsrest.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author yushanyuan
 *
 */
public class PropertiesUtil {
	private static Properties pop = new Properties(); 
	
	static{
		InputStream is = null;
		try{
			is = PropertiesUtil.class.getClassLoader().getResourceAsStream("config.properties");
			pop.load(is);
		}catch(Exception e){
			e.printStackTrace();
			
		}finally{
			try {
				if(is!=null)is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static Properties getPop() {
		return pop;
	}
	public static void setPop(Properties pop) {
		PropertiesUtil.pop = pop;
	}
	public static String getTools(){
		
		return pop.getProperty("tools");
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	}
}
