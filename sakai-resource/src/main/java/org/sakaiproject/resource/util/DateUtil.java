/**
 * 
 */
package org.sakaiproject.resource.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author yushanyuan
 *日期转换
 */
public class DateUtil {

	public static String dateTimeStr = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 
	 * @param dateStr
	 * @param pattern
	 * @return
	 */
	public static Date stringToDate(String dateStr, String pattern){
		SimpleDateFormat fo = new SimpleDateFormat(pattern);
		try {
			return fo.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String dateToString(Date date, String pattern){
		SimpleDateFormat fo = new SimpleDateFormat(pattern);
		
		return fo.format(date);
	}
	
}
