/**
 * 版权所有 北京思开科技有限公司 
 * All Rights Reserved
 */
package com.bysakai.mooc.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bysakai.mooc.model.Teacher;
 
public class UploadUtil {

	private static Log logger = LogFactory.getLog(UploadUtil.class);
	
	private String apacheServerFilePath;
	private String apacheServerImagesPath;
	
	
 
	public String getApacheServerFilePath() {
		return apacheServerFilePath;
	}
	public void setApacheServerFilePath(String apacheServerFilePath) {
		this.apacheServerFilePath = apacheServerFilePath;
	}
	public String getApacheServerImagesPath() {
		return apacheServerImagesPath;
	}
	public void setApacheServerImagesPath(String apacheServerImagesPath) {
		this.apacheServerImagesPath = apacheServerImagesPath;
	}



	/**
	 * 上传图片
	 * @param inputStream
	 * @param fileName
	 * @param account
	 * @return
	 */
	public String uploadIcon(InputStream inputStream, String fileName, String oldImage) {

		String absPath = null;
		try {

			if (fileName != null && !fileName.equals("")) {// 一个上传的文件
				
				File dir = new File(this.apacheServerFilePath + this.apacheServerImagesPath);
				if (!dir.exists())
					dir.mkdirs();
				
				
				String houzhui = fileName.substring(fileName.lastIndexOf("."));
				String icon = UUIDUtil.getUUID() + houzhui;
				File file = new File(dir, icon);

				//FileUtils.forceDelete(file);

				FileOutputStream out = new FileOutputStream(file);
				byte[] b = new byte[1024];
				while (inputStream.read(b) != -1) {
					out.write(b);
				}
				out.flush();
				out.close();
				//删除旧头像
				//FileUtils.forceDelete(new File(Constant.apacheServerFilePath +  oldImage));
				absPath = this.apacheServerImagesPath + icon;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return absPath;
	}
	

}
