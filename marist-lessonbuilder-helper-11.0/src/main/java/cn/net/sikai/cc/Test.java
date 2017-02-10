package cn.net.sikai.cc;

import java.util.Map;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String href = "https://ilearn.marist.edu/access/<img src=\"/images/ss.jpg\" />content/group/f9e9b57b-654f-4d2d-aecc-4b3858919260/1-s2.0-S1389128613003058-main.pdf";
		System.out.println(href.substring(0,href.indexOf("<img")));
		System.out.println(href.substring(href.indexOf("<img")));
		System.out.println(replaceUrl(href));
	}

	private static String replaceUrl(String content){
		int imgIndex = content.indexOf("<img");
		if(imgIndex>0){
			String beforeImg = content.substring(0, imgIndex);
			String containImg = content.substring(imgIndex);
			int srcIndex = containImg.indexOf("src");
			
			String beforeSrc = containImg.substring(0, srcIndex);
			String afterSrc = containImg.substring(srcIndex);
			
			int yinIndex = afterSrc.indexOf("\"");
			String beforeYin = afterSrc.substring(0, yinIndex+1);
			String afterYin = afterSrc.substring(yinIndex+1);
			
			int otherYinIndex = afterYin.indexOf("\"");
			String filePath = afterYin.substring(0, otherYinIndex);
			String afterOtherYin = afterYin.substring(otherYinIndex);
			
			String filename = filePath.substring(filePath.lastIndexOf("/")+1);
			
			
			return beforeImg + beforeSrc + beforeYin + "xx" + replaceUrl(afterOtherYin);
		}
		return content;
	}
	
}
