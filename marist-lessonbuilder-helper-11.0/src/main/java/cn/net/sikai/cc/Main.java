package cn.net.sikai.cc;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;
import org.jdom.*;

public class Main {
	private static final String ITEM_QUERY="/ims:manifest/ims:organizations/ims:organization/ims:item";
	private static final String ORG_QUERY="/ims:manifest/ims:organizations/ims:organization";
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		File cc = null;
		File root = null;
		try {
			cc = File.createTempFile("ccloader", "file");
			root = File.createTempFile("ccloader", "root");
			if (root.exists()) {
				if (!root.delete()) {
					System.out.println("delete dir error");
				}
				
			}
			if (!root.mkdir()) {
				System.out.println("make dir error");
			}
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File("C:/bupt/sakai/lbuilder/ljw-test_allModules2.zip")));
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(cc));
			byte[] buffer = new byte[8096];
			int n = 0;
			while ((n = bis.read(buffer, 0, 8096)) >= 0) {
				if (n > 0)
					bos.write(buffer, 0, n);
			}
			bis.close();
			bos.close();

			MeleteLoader meleteLoader = ZipLoader.getUtilities(cc, root.getCanonicalPath());
			Parser parser = Parser.createMeleteParser(meleteLoader);
			Element manifest = parser.getXML(meleteLoader, "imsmanifest.xml");
			
			Ns ns = new Ns();
		    // figure out which version we have, and set up ns to know about it
		    int v = 0;
		    for (; v < ns.getVersions(); v++) {
				ns.setVersion(v);
				// see if the namespace from the main manifest entry matches the candidate
				if (manifest.getNamespace().equals(ns.cc_ns()))
				    break;
			    }
			    if (v >= ns.getVersions()) {
				return;
		    }
			    
		    XPath path=XPath.newInstance(ORG_QUERY);
		    path.addNamespace(ns.cc_ns());
		    Element org = (Element)path.selectSingleNode(manifest);
			
			//Element org = manifest.getChild("organization");
			for(Iterator iter=org.getChildren("item",ns.cc_ns()).iterator(); iter.hasNext(); ){
				Element item=(Element)iter.next();
				processItem(item, ns);
			}
			Element ress = manifest.getChild("resources",ns.cc_ns());
			//System.out.println("ress name: "+ress.getName());
			
			
			boolean importtop = false;
		}catch(JDOMException e1){
			e1.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void processItem(Element item, Ns ns){
		Element title = item.getChild("title", ns.cc_ns());
		String pageTitle = "no name";
		if(title!=null && title.getValue()!=null){
			
			pageTitle = title.getValue();
			//System.out.println("item title: "+pageTitle);
		}
		//TODO 创建页面
		
		
		for(Iterator iter=item.getChildren("item",ns.cc_ns()).iterator(); iter.hasNext(); ){
			Element subitem=(Element)iter.next();
			processItem(subitem, ns);
		}
		
		
		String identifier = (String)item.getAttributeValue("identifierref");
		if(identifier!=null){
			System.out.println("item identifierref: "+identifier);
			//TODO processResource
			//TODO 上传资源
		}
		
		
		
	}
}
