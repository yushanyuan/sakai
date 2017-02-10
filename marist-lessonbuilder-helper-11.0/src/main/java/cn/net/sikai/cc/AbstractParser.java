/**
 * 版权所有 北京思开科技有限公司 
 * All Rights Reserved
 */
package cn.net.sikai.cc;

import java.io.IOException;
import java.io.StringBufferInputStream;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 * @Description: TODO
 * @author: yushanyuan
 *
 */
public abstract class AbstractParser {

	private static SAXBuilder builder;

	public static class NoOpEntityResolver implements EntityResolver {
		public InputSource resolveEntity(String publicId, String systemId) {
			return new InputSource(new StringBufferInputStream(""));
		}
	}

	static {
		builder = new SAXBuilder();
		builder.setEntityResolver(new NoOpEntityResolver());
	}

	public Element getXML(MeleteLoader the_cartridge, String the_file) throws IOException, ParseException {
		Element result = null;
		try {
			result = builder.build(the_cartridge.getFile(the_file)).getRootElement();
		} catch (Exception e) {
			throw new ParseException(e);
		}
		return result;
	}
}
