/**
 * 版权所有 北京思开科技有限公司 
 * All Rights Reserved
 */
package cn.net.sikai.cc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import cn.net.sikai.model.SimplePage;

/**
 * @Description: TODO
 * @author: yushanyuan
 *
 */
public class Parser extends AbstractParser {

	MeleteLoader utils;

	private static final String IMS_MANIFEST = "imsmanifest.xml";
	private static final String ORG_QUERY = "/ims:manifest/ims:organizations/ims:organization";
	private static final String REC_QUERY = "/ims:manifest/ims:resources";

	private static final String ITEM_QUERY = "/ims:manifest/ims:organizations/ims:organization/ims:item";
	private static final String CC_RESOURCE = "resource";
	private static final String CC_RESOURCES = "resources";

	

	private Parser(MeleteLoader the_cu) {
		super();
		utils = the_cu;
	}

	public void parse(DefaultHandler the_handler) throws FileNotFoundException, ParseException {
		try {
			Element manifest = this.getXML(utils, IMS_MANIFEST);
			processManifest(manifest, the_handler);
		} catch (Exception e) {
			the_handler.getZipController().setErrMessage("page.cc-error");
			System.out.println("parse error, stack trace follows " + e);
			e.printStackTrace();
			// throw new ParseException(e.getMessage(),e);
		}
	}

	private void processManifest(Element the_manifest, DefaultHandler the_handler) throws ParseException {
		Ns ns = new Ns();
		the_handler.setNs(ns);
		// figure out which version we have, and set up ns to know about it
		int v = 0;
		for (; v < ns.getVersions(); v++) {
			ns.setVersion(v);
			// see if the namespace from the main manifest entry matches the
			// candidate
			if (the_manifest.getNamespace().equals(ns.cc_ns()))
				break;
		}
		if (v >= ns.getVersions()) {
			the_handler.getZipController().setErrMessage("page.wrong-cc-version");
			return;
		}

		the_handler.setManifestXml(the_manifest);
		the_handler.setManifestMetadataXml(null);//创建资源里面的跟目录
		try {
			XPath path = XPath.newInstance(ORG_QUERY);
			path.addNamespace(ns.cc_ns());
			Element org = (Element) path.selectSingleNode(the_manifest);

			XPath rec_path = XPath.newInstance(REC_QUERY);
			rec_path.addNamespace(ns.cc_ns());
			Element recs = (Element) rec_path.selectSingleNode(the_manifest);

			Long mainPageId = null;
			if (org != null) {// 获取lb主页面的pageId
				mainPageId = the_handler.checkMainPage();
			}
			if (mainPageId == null) {
				the_handler.getZipController().setErrMessage("page.nomainpage");
				return;
			}
			Map<String, String> imageUrlMap = new HashMap<String, String>();//上传的附件的地址
			for (Iterator iter = org.getChildren("item", ns.cc_ns()).iterator(); iter.hasNext();) {
				Element item = (Element) iter.next();
				processItem(mainPageId, mainPageId, item, the_handler, ns, recs, imageUrlMap);
			}
			Element ress = the_manifest.getChild("resources", ns.cc_ns());
			System.out.println("ress name: " + ress.getName());

			the_handler.endManifest();

		} catch (JDOMException e) {
			System.err.println(e.getMessage());
			throw new ParseException(e.getMessage(), e);
		}

	}

	public void processItem(Long mainPageId, Long parentId, Element item, DefaultHandler the_handler, Ns ns,
			Element recs, Map<String, String> imageUrlMap) {
		Element title = item.getChild("title", ns.cc_ns());
		String pageTitle = "no name";
		if (title != null && title.getValue() != null) {

			pageTitle = title.getValue();
			System.out.println("item title: " + pageTitle);
		}
		// 创建页面
		SimplePage page = the_handler.addPage(mainPageId, parentId, pageTitle);

		if (page == null) {
			return;
		}
		the_handler.insertItemByType2(page.getParent(), ""+page.getPageId(), page.getTitle());
		
		String identifierref = (String) item.getAttributeValue("identifierref");
		if (identifierref != null) {
			System.out.println("item identifier: " + identifierref);
			// TODO processResource
			// TODO 上传资源2016-3-10
			// TODO
			// 不需要上传资源，因为资源就在resource中，Ls和melete在一个课程下.但是要生成数据库表lesson_builder_items
			//2016-3-12 网页中的图片要上传，因为路径变了。
			processResource(page, identifierref, recs, the_handler, ns, imageUrlMap);
		} 
		

		for (Iterator iter = item.getChildren("item", ns.cc_ns()).iterator(); iter.hasNext();) {
			Element subitem = (Element) iter.next();
			processItem(mainPageId, page.getPageId(), subitem, the_handler, ns, recs, imageUrlMap);
		}

	}

	public void processResource(SimplePage page, String identifierref, Element recs,DefaultHandler the_handler, Ns ns, Map<String, String> imageUrlMap) {

		// 查找指定的资源
		if (recs == null) {
			return;
		}
		Element resource = null;
		for (Iterator iter = recs.getChildren("resource", ns.cc_ns()).iterator(); iter.hasNext();) {
			Element temp = (Element) iter.next();
			if (identifierref.equals(temp.getAttributeValue("identifier"))) {
				resource = temp;
				break;
			}
		}

		if (resource == null) {
			return;
		}

		String href = resource.getAttributeValue("href");
		if (href.indexOf("http") != -1) {// 附件
			//String name = href.substring(href.lastIndexOf("/")+1);
			Element fileElem = (Element)resource.getChildren("file", ns.cc_ns()).iterator().next();
			String filePath = fileElem.getAttributeValue("href");
			String name =  filePath.substring(filePath.lastIndexOf("/")+1);
			String fileUrl = href;
			String saveFileUrl = fileUrl;
			try {
				fileUrl = the_handler.addFile(name, utils.getFile(filePath));
				if(fileUrl!=null && fileUrl.indexOf("/group/")!=-1){
					
					imageUrlMap.put(fileUrl.substring(fileUrl.lastIndexOf("/")+1), fileUrl);
					String decodeFileUrl = URLDecoder.decode(fileUrl,"utf-8");
					imageUrlMap.put(decodeFileUrl.substring(decodeFileUrl.lastIndexOf("/")+1), decodeFileUrl);
					
					String subFileUrl = fileUrl.substring(fileUrl.indexOf("/group/"));
					saveFileUrl = URLDecoder.decode(subFileUrl,"utf-8");
					
					
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e){
				
			}
		
			
			the_handler.insertItemByType1(page.getPageId(), saveFileUrl, name, new MimetypesFileTypeMap().getContentType(name));
		} else if(href.indexOf(".html") != -1){// 可以编辑的页面
			
			
			//上传页面中用到的图片
			for (Iterator iter = resource.getChildren("file", ns.cc_ns()).iterator(); iter.hasNext();) {
				Element temp = (Element) iter.next();
				String fileHref = temp.getAttributeValue("href");
				if (href.equals(fileHref)) {
					continue;
				}
				String the_file_id = fileHref;
				if(the_file_id.lastIndexOf("/")!=-1){
					the_file_id = the_file_id.substring(the_file_id.lastIndexOf("/")+1);
				}
				try {
					String fileUrl = the_handler.addFile(the_file_id, utils.getFile(fileHref));
					imageUrlMap.put(the_file_id, fileUrl);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e){
					
				}
			}
	
			//编辑html内容，改下图片链接的地址
			BufferedReader br = null;
			try {
				br = new BufferedReader(new InputStreamReader(utils.getFile(href), "UTF-8"));
				StringBuffer content = new StringBuffer();
				String line = br.readLine();
				while (line != null) {
					content.append(line);
					line = br.readLine();
				}
				String imageContent = content.toString();
				try{
					imageContent = replaceUrlByJsoup(content.toString(), imageUrlMap);
				}catch(Exception e){
					e.printStackTrace();
				}
				
				
				// insert l_b_items
				the_handler.insertItemByType5(page.getPageId(), imageContent);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					if(br !=null){
						br.close();
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}
	private String replaceUrlByJsoup(String content, Map<String, String> imageUrlMap) throws UnsupportedEncodingException{
		
		Document doc = Jsoup.parse(content);
		//修改图片
		Elements images = doc.getElementsByTag("img");
		for (org.jsoup.nodes.Element link : images) {
		  String imgSrc = link.attr("src");
		  if(imgSrc!=null && imgSrc.lastIndexOf("/")!=-1){
			  String imgFileName = imgSrc.substring(imgSrc.lastIndexOf("/")+1);
			  if(imageUrlMap.get(imgFileName)!=null || imageUrlMap.get(URLDecoder.decode(imgFileName,"utf-8"))!=null){
				  link.attr("src", imageUrlMap.get(imgFileName));
			  }
		  }
		}
		//修改超链接
		Elements links = doc.getElementsByTag("a");
		for (org.jsoup.nodes.Element link : links) {
		  String imgSrc = link.attr("href");
		  if(imgSrc!=null && imgSrc.lastIndexOf("/")!=-1){
			  String imgFileName = imgSrc.substring(imgSrc.lastIndexOf("/")+1);
			  if(imageUrlMap.get(imgFileName)!=null || imageUrlMap.get(URLDecoder.decode(imgFileName,"utf-8"))!=null){
				  link.attr("href", imageUrlMap.get(imgFileName));
			  }
		  }
		}
		
		return doc.toString();
	}
	
	private  String replaceUrl(String content, Map imageUrlMap){
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
			if(filePath.indexOf("http")!=-1){
				return beforeImg + beforeSrc + beforeYin + filePath + replaceUrl(afterOtherYin, imageUrlMap);
			}
			
			String filename = filePath.substring(filePath.lastIndexOf("/")+1);
			if(imageUrlMap.get(filename)!=null){
				return beforeImg + beforeSrc + beforeYin + imageUrlMap.get(filename) + replaceUrl(afterOtherYin, imageUrlMap);	
			}
			return beforeImg + beforeSrc + beforeYin + filePath + replaceUrl(afterOtherYin, imageUrlMap);
		}
		return content;
	}
	
	// ------------------
	public static Parser createMeleteParser(MeleteLoader the_cartridge) throws FileNotFoundException, IOException {
		return new Parser(the_cartridge);
	}
}
