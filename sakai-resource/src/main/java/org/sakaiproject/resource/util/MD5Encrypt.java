package org.sakaiproject.resource.util;

import java.security.MessageDigest;

/**
 * 用于MD5的加密处理类
 * @author seastar
 */
public class MD5Encrypt {
	  public MD5Encrypt() {
	  }

	  private final static String[] hexDigits = {
	      "0", "1", "2", "3", "4", "5", "6", "7",
	      "8", "9", "a", "b", "c", "d", "e", "f"};

	  /**
	   * 转换字节数组为16进制字串
	   * @param b 字节数组
	   * @return 16进制字串
	   */
	  public static String byteArrayToString(byte[] b) {
	    StringBuffer resultSb = new StringBuffer();
	    for (int i = 0; i < b.length; i++) {
	      resultSb.append(byteToHexString(b[i]));//若使用本函数转换则可得到加密结果的16进制表示，即数字字母混合的形式
	      //resultSb.append(byteToNumString(b[i]));//使用本函数则返回加密结果的10进制数字字串，即全数字形式
	    }
	    return resultSb.toString();
	  }

	@SuppressWarnings("unused")
	private static String byteToNumString(byte b) {

	    int _b = b;
	    if (_b < 0) {
	      _b = 256 + _b;
	    }

	    return String.valueOf(_b);
	  }

	  private static String byteToHexString(byte b) {
	    int n = b;
	    if (n < 0) {
	      n = 256 + n;
	    }
	    int d1 = n / 16;
	    int d2 = n % 16;
	    return hexDigits[d1] + hexDigits[d2];
	  }

	  public static String MD5Encode(String origin) {
	    String resultString = null;

	    try {
	      resultString = new String(origin);
	      MessageDigest md = MessageDigest.getInstance("MD5");

	      resultString = byteArrayToString(md.digest(resultString.getBytes()));
	    }
	    catch (Exception ex) {

	    }
	    return resultString;
	  }

	  public static String MD5Encode16(String origin) {
	    String resultString = null;

	    try {
	      resultString = new String(origin);
	      MessageDigest md = MessageDigest.getInstance("MD5");

	      String resultOf32 = byteArrayToString(md.digest(resultString.getBytes()));
	      resultString = resultOf32.substring(8, 24);
	    }
	    catch (Exception ex) {

	    }
	    return resultString;
	  }

	  public static void main(String[] args) {
	    //MD5Encrypt md5encrypt = new MD5Encrypt();
	    System.out.println(MD5Encrypt.MD5Encode("asdfghjk"));
	  }


}
