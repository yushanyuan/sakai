/**
 * 版权所有 北京思开科技有限公司 
 * All Rights Reserved
 */
package com.bysakai.mooc.api;

import java.util.List;
import com.bysakai.mooc.model.Categorys;

/**
 * @author nesdu
 *
 */
public interface CategoryService {
	public List<Categorys> list();
	public void save(Categorys category);
	public void remove(Categorys category);
	public Categorys findOne(String id);
}
