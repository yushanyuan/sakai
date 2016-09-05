/**
 * 版权所有 北京思开科技有限公司 
 * All Rights Reserved
 */
package org.sakaiproject.cmsrest.utils;

import java.util.UUID;

/**
 * <p>
 * 关于<b>UUIDUtil</b>的说明
 * </p>
 * 
 * @author llk
 * @version 1.0
 * @since 
 *
 */
public class UUIDUtil {
	/**
	 * <p>
	 * 方法 getUUID
	 * </p>
	 * 获取32位唯一字符串
	 * 
	 * @return
	 */
	public static String getUUID(){ 
        String s = UUID.randomUUID().toString(); 
        //去掉“-”符号 
        return s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24); 
    } 
	/**
	 * <p> 方法 getUUID</p>
	 *获得指定数目的UUID
	 * @param number
	 * @return
	 */
	public static String[] getUUID(int number){ 
        if(number < 1){ 
            return null; 
        } 
        String[] ss = new String[number]; 
        for(int i=0;i<number;i++){ 
            ss[i] = getUUID(); 
        } 
        return ss; 
    } 
	
	public static void main(String[] args){
		System.out.println(UUIDUtil.getUUID());
	}
}
