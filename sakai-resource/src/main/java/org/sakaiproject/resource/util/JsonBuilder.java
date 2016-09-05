package org.sakaiproject.resource.util;

import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;

public class JsonBuilder {
	public static String TREEJSON = "TREE";
	public static String GRIDJSON = "GRID";
	public static String COMBOJSON = "COMBO";
	public static String PLAINJSON = "PLAIN";
	public static String jsonType = PLAINJSON;
	public static XStream xstream = null;
	public static String root = "root";
	public static String totalProperty = "total";
	static {
		xstream = new XStream(new JsonHierarchicalStreamDriver() {
			public HierarchicalStreamWriter createWriter(Writer writer) {
				return new JsonWriter(writer, JsonWriter.DROP_ROOT_MODE);
			}
		});
	}

	public static String getJsonType() {
		return JsonBuilder.jsonType;
	}

	public static void setJsonType(String jsonType) {
		JsonBuilder.jsonType = jsonType;
	}

	/**
	 * 设置root属性名
	 * 
	 * @param root
	 *            root属性名
	 */
	public static void setRootString(String root) {
		JsonBuilder.root = root;
	}

	/**
	 * 设置totalProperty属性名
	 * 
	 * @param totalProperty
	 *            totalProperty属性名
	 */
	public static void setTotalProperty(String totalProperty) {
		JsonBuilder.totalProperty = totalProperty;
	}

	/**
	 * 将一个异步树形的list转化为json串
	 * 
	 * @param data
	 * @return
	 */
	public static String builderAsyncTreeJson(List<AsyncTreeModel> data) {
		StringBuffer sb = new StringBuffer();
		for (AsyncTreeModel model : data) {
			Map<String, Object> attrMap = model.getAttributes();
			model.setAttributes(null);
			String baseAttr = xstream.toXML(model);
			String attr = "";
			if (attrMap != null && attrMap.size() > 0) {
				Set<String> set = attrMap.keySet();
				for (Iterator<String> it = set.iterator(); it.hasNext();) {
					String key = it.next();
					Object val = attrMap.get(key);
					if (val == null) {
						val = "";
					}
					String sn = val.getClass().getSimpleName();// 获取数据类型
					if (sn.equals("String")) {
						attr += "\"" + key + "\":\"" + val + "\",";
					} else if (sn.equals("Integer")) {
						attr += "\"" + key + "\":\"" + val + "\",";
					} else if (sn.equals("Boolean")) {
						attr += "\"" + key + "\":" + val + ",";
					} else if (sn.equals("Double")) {
						attr += "\"" + key + "\":\"" + val + "\",";
					} else if (sn.equals("Long")) {
						attr += "\"" + key + "\":\"" + val + "\",";
					} else if (sn.equals("Short")) {
						attr += "\"" + key + "\":\"" + val + "\",";
					} else if (sn.equals("Date")) {
						attr += "\"" + key + "\":\"" + val.toString().substring(0, 10) + "\",";
					} else if (sn.equals("Timestamp")) {
						attr += "\"" + key + "\":\"" + val.toString().substring(0, 19) + "\",";
					} else {
						attr += "\"" + key + "\":\"" + val.toString() + "\",";
					}
				}
				baseAttr = baseAttr.substring(0, baseAttr.length() - 1) + "," + attr.substring(0, attr.length() - 1)
						+ "}";
			}
			sb.append(baseAttr + ",");
		}
		String str = "[]";
		if (sb.length() > 0) {
			str = "[" + sb.deleteCharAt(sb.length() - 1).toString() + "]";
			str = str.trim().replace("\r\n", "\n").trim().replace("\n", "");
		}
		//System.out.println("表格树的数据为：" + str);
		return str;
	}

	/**
	 * 将一个对象转化为json串
	 * 
	 * @param data
	 *            对象
	 * @return json串
	 */
	public static String builderObjectJson(Object data) {
		return xstream.toXML(data);
	}

	/**
	 * 将一个对象转化为符合ext js表单要求的json串
	 * 
	 * @param data
	 *            对象
	 * @return json串
	 */
	public static String builderFormJson(Object data) {
		String json = xstream.toXML(data);
		String extraJson = "{success:true,data:" + json + "}";
		return extraJson;
	}

	/**
	 * 将两个对象转为一个符合ext js表单要求的json串
	 * 
	 * @param data1
	 *            对象
	 * @param data2
	 *            对象
	 * @return json串
	 */
	public static String builderFormJson(Object data1, Object data2) {
		String json1 = xstream.toXML(data1);
		String json2 = xstream.toXML(data2);
		String json = StringUtils.substring(json1, 0, json1.length() - 1) + "," + StringUtils.substring(json2, 1);

		String extraJson = "{success:true,data:" + json + "}";
		return extraJson;
	}

	/**
	 * 将一个结果list和总记录数转化为符合ext js 表格要求的json串
	 * 
	 * @param data
	 *            结果list
	 * @param totalSize
	 *            总记录数
	 * @return json串，格式为{"total":238,"root":}
	 */
	public static String builderGridJson(List data, int totalSize) {
		String json = xstream.toXML(data);
		String extraJson = "{" + JsonBuilder.totalProperty + ":" + totalSize + "," + JsonBuilder.root + ":" + json
				+ "}";
		return extraJson;
	}

	public static String builderAsyncGridJson(List<AsyncGridModel> dataList) {
		int num = dataList.size();
		StringBuffer sb = new StringBuffer();
		sb.append("{tatalProperty:" + num + ",root:[");
		for (AsyncGridModel model : dataList) {
			sb.append(model.getData().substring(0, model.getData().length() - 1) + "}" + ",");
		}
		if (sb.toString().lastIndexOf(",") == sb.toString().length() - 1) {
			/** 把生成的json串最后的“，”去掉 */
			sb.delete(sb.toString().length() - 1, sb.toString().length());
		}
		sb.append("]}");
		//System.out.println("表格数据：" + sb.toString());
		return sb.toString();
	}
	
	public static String builderAsyncGridJson(List<AsyncGridModel> dataList,Integer num) {
		StringBuffer sb = new StringBuffer();
		sb.append("{total:" + num + ",root:[");
		for (AsyncGridModel model : dataList) {
			sb.append(model.getData().substring(0, model.getData().length() - 1) + "}" + ",");
		}
		if (sb.toString().lastIndexOf(",") == sb.toString().length() - 1) {
			/** 把生成的json串最后的“，”去掉 */
			sb.delete(sb.toString().length() - 1, sb.toString().length());
		}
		sb.append("]}");
		//System.out.println("表格数据：" + sb.toString());
		return sb.toString();
	}

}
