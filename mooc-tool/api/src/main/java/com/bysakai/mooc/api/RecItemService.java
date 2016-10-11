/**
 * 版权所有 北京思开科技有限公司 
 * All Rights Reserved
 */
package com.bysakai.mooc.api;

import java.util.List;

import com.bysakai.mooc.model.RecItem;

/**
 * @author nesdu
 *
 */
public interface RecItemService {

	public List<RecItem> list();
	public void save(RecItem recList);
	public void remove(RecItem recList);
	public RecItem findOne(String id);
	
}
