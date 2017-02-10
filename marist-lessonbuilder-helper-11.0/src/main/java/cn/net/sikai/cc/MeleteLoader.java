/**
 * 版权所有 北京思开科技有限公司 
 * All Rights Reserved
 */
package cn.net.sikai.cc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Description: TODO
 * @author: yushanyuan
 *
 */
public interface MeleteLoader {
	public abstract InputStream getFile(String the_target)
		      throws FileNotFoundException, IOException;
}
