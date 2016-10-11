/**
 * 版权所有 北京思开科技有限公司 
 * All Rights Reserved
 */
package com.bysakai.mooc.rest.vo;

import java.io.Serializable;

/**
 * @author nesdu
 *
 */
public class MsgVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String data;
	private String msg;
	

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
