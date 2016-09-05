/**   
 *   
 * 版本信息：   
 * Copyright 思开科技    
 * 版权所有   
 */
package org.sakaiproject.cmsrest.utils;


public class MD5Util {
	
	
	
	public static String md5(String strMd5) {
		try {
			java.security.MessageDigest alga = java.security.MessageDigest
					.getInstance("MD5");
			alga.update(strMd5.getBytes());
			byte[] digesta = alga.digest();
			String hs = "";
			String stmp = "";
			for (int n = 0; n < digesta.length; n++) {
				stmp = (java.lang.Integer.toHexString(digesta[n] & 0XFF));
				if (stmp.length() == 1) {
					hs += "0" + stmp;
				} else {
					hs = hs + stmp;
				}
				if (n < digesta.length - 1) {
					hs += "";
				}
			}
			return hs;
		} catch (java.security.NoSuchAlgorithmException ex) {
			throw new RuntimeException("非法摘要算法。");
			
		}
	}
	
	public static void main(String args[]){
		String md5key = "buptapi"; 
		String param = "1";
		String auth = md5key + param;
		
		System.out.println(md5(auth));
	}

}
