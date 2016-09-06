/**
 * 
 */
package org.sakaiproject.resource.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.adl.parsers.dom.DOMTreeUtility;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.DOMReader;
import org.dom4j.io.DOMWriter;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.sakaiproject.resource.api.course.model.MeleteForumModel;
import org.sakaiproject.resource.api.course.model.MeleteModuleModel;
import org.sakaiproject.resource.api.course.model.MeleteSectionModel;
import org.sakaiproject.resource.api.course.model.MeleteSelfTestModel;
import org.sakaiproject.resource.api.course.model.MeleteTestModel;
import org.sakaiproject.resource.api.course.vo.Node;
import org.sakaiproject.resource.api.study.model.MeleteForumRecordModel;
import org.sakaiproject.resource.api.study.model.MeleteModuleRecordModel;
import org.sakaiproject.resource.api.study.model.MeleteSectionRecordModel;
import org.sakaiproject.resource.api.study.model.MeleteSelftestRecordModel;
import org.sakaiproject.resource.api.study.model.MeleteTestRecordModel;
import org.w3c.dom.Element;

/**
 * 本类完成获得 教务系统 与 考试系统 课程列表 功能
 * 
 * @author zhangxin
 * 
 */
public class CourseUtil {

	public static String createAPIQueryString(Map<String, String> queryMap, long time, String securitykey) {

		Map<String, String> map = new TreeMap<String, String>(queryMap);
		String qs = createQueryString(map);
		if (qs == null) {
			return null;
		}
		time = time / 1000;

		String hash = MD5Encrypt.MD5Encode(String.format("%s&time=%d&key=%s", qs, time, securitykey));
		hash = hash.toLowerCase();
		String apiqs = String.format("%s&time=%d&auth=%s", qs, time, hash);

		return apiqs;
	}

	/**
	 * 功能：用一个Map生成一个QueryString，参数的顺序不可预知。
	 * 
	 * @param queryString
	 * @return
	 */
	public static String createQueryString(Map<String, String> queryMap) {

		if (queryMap == null) {
			return null;
		}

		try {
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() == null) {
					continue;
				}
				String key = entry.getKey().trim();
				String value = URLEncoder.encode(entry.getValue().trim(), "utf-8");
				sb.append(String.format("%s=%s&", key, value));
			}
			return sb.substring(0, sb.length() - 1);
		} catch (StringIndexOutOfBoundsException e) {
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getUserKeyForExamSys(String curUserName) throws Exception {
		if (StringUtils.isBlank(Constants.OUTSYS_EXAM_KEY_ENTRY)) {
			return null;
		}

		String userName = null;
		// 连接题库是否用指定用户，如果是的话，就使用配置的指定用户连接题库，否则就使用当前sakai用户。
		if (StringUtils.isBlank(Constants.OUTSYS_EXAM_USER)) {
			userName = curUserName;
		} else {
			userName = Constants.OUTSYS_EXAM_USER;
		}

		byte[] tt = DES.encrypt(Constants.OUTSYS_EXAM_KEY_ENTRY.getBytes(Charset.forName("utf-8")), userName);
		String key = URLEncoder.encode(Base64.encodeBase64String(tt), "utf-8");

		return key;
	}

	/**
	 * 进入题库
	 * 
	 * @param curUserName
	 * @param courseId
	 * @return
	 * @throws Exception
	 */
	public static String getExamSysEntryUrl(String curUserName, String courseId) throws Exception {
		String key = getUserKeyForExamSys(curUserName);
		String examSysUrl = Constants.OUTSYS_EXAM_URL_BASE;
		if (StringUtils.isBlank(examSysUrl)) {
			return null;
		}
		return examSysUrl + Constants.OUTSYS_EXAM_URL_ENTRY.replace("${key}", key).replace("${courseId}", courseId);
	}

	public static String getExamSchemaEntryUrl(String curUserName, String courseId) throws Exception {
		String key = getUserKeyForExamSys(curUserName);
		String examSysUrl = Constants.OUTSYS_EXAM_URL_BASE;
		if (StringUtils.isBlank(examSysUrl)) {
			return null;
		}
		return examSysUrl + Constants.OUTSYS_EXAM_URL_SCHEMA_VIEW.replace("${key}", key);
	}

	public static String getUserKeyForResSys(String curUserName) throws Exception {
		if (StringUtils.isBlank(Constants.OUTSYS_RESSYS_KEY_ENTRY)) {
			return null;
		}
		String userName = null;
		// 连接题库是否用指定用户，如果是的话，就使用配置的指定用户连接题库，否则就使用当前sakai用户。
		if (StringUtils.isBlank(Constants.OUTSYS_RESSYS_USER)) {
			userName = curUserName;
		} else {
			userName = Constants.OUTSYS_RESSYS_USER;
		}

		byte[] tt = DES.encrypt(Constants.OUTSYS_RESSYS_KEY_ENTRY.getBytes(Charset.forName("utf-8")), userName);
		String key = URLEncoder.encode(Base64.encodeBase64String(tt), "utf-8");

		return key;
	}

	/**
	 * 进入资源系统
	 * 
	 * @param curUserName
	 * @param courseId
	 * @return
	 * @throws Exception
	 */
	public static String getResSysEntryUrl(String curUserName, String courseId) throws Exception {
		String key = getUserKeyForResSys(curUserName);
		String sysUrl = Constants.OUTSYS_RESSYS_URL_BASE;
		if (key == null || StringUtils.isBlank(sysUrl)) {
			return null;
		}
		return sysUrl
				+ Constants.OUTSYS_RESSYS_URL_ENTRY.replace("${code}", Constants.OUTSYS_RESSYS_KEY_CODE)
						.replace("${key}", key).replace("${courseId}", courseId);
	}

	/**
	 * 资源系统的资源列表接口url
	 * 
	 * @param curUserName
	 * @param courseId
	 * @return
	 * @throws Exception
	 */
	public static String getResSysResourcesUrl(String curUserName, String courseId) throws Exception {
		String sysUrl = Constants.OUTSYS_RESSYS_URL_BASE;
		if (StringUtils.isBlank(sysUrl)) {
			return null;
		}
		Map<String, String> queryMap = new HashMap<String, String>();
		// String userName = null;
		// 连接题库是否用指定用户，如果是的话，就使用配置的指定用户连接题库，否则就使用当前sakai用户。
		// if (StringUtils.isBlank(Constants.OUTSYS_RESSYS_USER)) {
		// userName = curUserName;
		// } else {
		// userName = Constants.OUTSYS_RESSYS_USER;
		// }
		// queryMap.put("uid", userName);
		queryMap.put("from", Constants.OUTSYS_RESSYS_KEY_CODE);
		queryMap.put("cid", courseId);
		String queryString = CourseUtil.createAPIQueryString(queryMap, System.currentTimeMillis(),
				Constants.OUTSYS_RESSYS_KEY_ENTRY);
		return sysUrl + Constants.OUTSYS_RESSYS_URL_RESOURCE + "?" + queryString;
	}

	/**
	 * 资源系统的课程列表接口url
	 * 
	 * @param curUserName
	 * @return
	 * @throws Exception
	 */
	public static String getResSysCourcesUrl(String curUserName) throws Exception {
		String sysUrl = Constants.OUTSYS_RESSYS_URL_BASE;
		if (StringUtils.isBlank(sysUrl)) {
			return null;
		}
		Map<String, String> queryMap = new HashMap<String, String>();
		String userName = null;
		// 连接题库是否用指定用户，如果是的话，就使用配置的指定用户连接题库，否则就使用当前sakai用户。
		if (StringUtils.isBlank(Constants.OUTSYS_RESSYS_USER)) {
			userName = curUserName;
		} else {
			userName = Constants.OUTSYS_RESSYS_USER;
		}
		queryMap.put("account", userName);
		queryMap.put("from", Constants.OUTSYS_RESSYS_KEY_CODE);
		String queryString = CourseUtil.createAPIQueryString(queryMap, System.currentTimeMillis(),
				Constants.OUTSYS_RESSYS_KEY_ENTRY);
		return sysUrl + Constants.OUTSYS_RESSYS_URL_COURSE + "?" + queryString;
	}

	/**
	 * 解析资源类型
	 * 
	 * @param url
	 * @return
	 */
	public static String parseResourceType(String url) {
		String type = CodeTable.RESOURCE_TYPE_UNKNOWN;
		if (StringUtils.isBlank(url) || url.lastIndexOf(".") <= 0) {
			return type;
		}

		String ext = url.substring(url.lastIndexOf(".") + 1).toLowerCase();
		if (ext.equals("flv") || ext.equals("mp4")) {
			type = CodeTable.RESOURCE_TYPE_VIDEO;
		} else if (ext.equals("jpg") || ext.equals("gif") || ext.equals("png")) {
			type = CodeTable.RESOURCE_TYPE_IMAGE;
		} else if (ext.equals("mp3") || ext.equals("ogg") || ext.equals("acc")) {
			type = CodeTable.RESOURCE_TYPE_AUDIO;
		}

		return type;
	}

	public static String getWebPath() {
		return Constants.WEB_PATH;

	}

	public static String getSectionPath(String path) {
		String url = null;
		if (path != null) {
			url = Constants.SECTION_URL;
			url += path.replace("\\", "/");
		}
		return url;
	}

	public static String getServicePath() {
		return Constants.WEB_PATH;
	}

	public static String getSecurePath() {
		return Constants.SECURE_PATH;
	}

	public static boolean createFile(File file) throws Exception {
		boolean result = true;
		try {
			if (!file.exists()) {
				File folder = file.getParentFile();
				createFolder(folder);
				file.createNewFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	public static boolean createFolder(File folder) throws Exception {
		boolean result = true;
		try {
			if (!folder.exists()) {
				folder.mkdirs();
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	/***
	 * 整合数据，把传过来的集合整合到一个集合里，以两个集合的两个类为一组，封装到返回集合里的一个类里
	 * 比如把moduleList里的一个类和moduleRecordList里的一个类封装到一个node类里 传过来的集合都是已经排好序的
	 * 
	 * @param moduleList
	 *            课程
	 * @param moduleRecordList
	 *            课程记录，存放学生学习课程的信息
	 * @param sectionList
	 *            节点
	 * @param sectionRecordList
	 *            节点记录，存放学生学习节点的信息
	 * @param selfList
	 *            自测
	 * @param selfRecordList
	 *            自测记录，存放学生做自测的信息
	 * @param testList
	 *            作业
	 * @param testRecordList
	 *            作业记录，存放学生做作业的信息
	 * @param forumList
	 *            讨论
	 * @param forumRecordList
	 *            讨论记录，存放学生讨论的信息
	 * @return
	 */
	public static List<Node> changeToNode(List<MeleteModuleModel> moduleList,
			List<MeleteModuleRecordModel> moduleRecordList, List<MeleteSectionModel> sectionList,
			List<MeleteSectionRecordModel> sectionRecordList, List<MeleteSelfTestModel> selfList,
			List<MeleteSelftestRecordModel> selfRecordList, List<MeleteTestModel> testList,
			List<MeleteTestRecordModel> testRecordList, List<MeleteForumModel> forumList,
			List<MeleteForumRecordModel> forumRecordList) {
		List<Node> returnList = new ArrayList<Node>();
		// 如果课程集合不为空，则遍历整合课程集合、课程记录集合
		if (moduleList != null && !moduleList.isEmpty()) {
			for (int i = 0; i < moduleList.size(); i++) {
				MeleteModuleModel module = moduleList.get(i);
				if (String.valueOf(module.getStatus()).equals(CodeTable.del)) {
					continue;
				}
				Node node = new Node();
				node.setType(CodeTable.module);
				node.setId(module.getId().toString());
				node.setTitle(module.getTitle());
				node.setStudyTime(module.getStudyDay().toString());
				node.setAvgTime(module.getAvgPassTime().toString());
				node.setRequired(module.getRequired().toString());
				node.setDescription(module.getDescription());
				node.setCourseGuide(module.getCourseGuide());
				node.setTeachGoal(module.getTeachGoal());
				node.setKeyAndDifficute(module.getKeyAndDifficute());
				node.setTeachMethod(module.getTeachMethod());
				node.setLearnNavigation(module.getLearnNavigation());
				node.setRequirement(module.getRequirement());

				node.setVideoPicPath(module.getVideoPicPath());
				node.setVideoUrl(module.getVideoUrl());
				node.setVideoType(module.getVideoType());
				node.setVideoSize(module.getVideoSize());
				node.setVideoTime(CourseUtil.formatVideoTime(module.getVideoTime()));

				// moduleRecordList是根据moduleList里的对象排好序的
				if (moduleRecordList != null && moduleRecordList.size() > i) {
					MeleteModuleRecordModel moduleRecord = moduleRecordList.get(i);
					if (moduleRecord == null) {
						node.setMyTime("0");
					} else {
						node.setMyTime(moduleRecord.getStudyTime().toString());
					}
				}

				returnList.add(node);
			}
		}
		// 如果节点集合不为空，则遍历整合节点集合、节点记录集合
		if (sectionList != null && !sectionList.isEmpty()) {
			for (int i = 0; i < sectionList.size(); i++) {
				MeleteSectionModel model = sectionList.get(i);
				if (String.valueOf(model.getStatus()).equals(CodeTable.del)) {
					continue;
				}
				Node node = new Node();
				node.setType(CodeTable.section);
				node.setId(model.getId().toString());
				node.setTitle(model.getTitle());
				node.setStudyTime(model.getStudyTime().toString());
				node.setAvgTime(model.getAvgPassTime().toString());
				node.setRequired(model.getRequired().toString());
				node.setDescription(model.getDescription());
				node.setRequirement(model.getRequirement());

				node.setVideoPicPath(model.getVideoPicPath());
				node.setVideoUrl(model.getVideoUrl());
				node.setVideoType(model.getVideoType());
				node.setVideoSize(model.getVideoSize());
				node.setVideoTime(CourseUtil.formatVideoTime(model.getVideoTime()));

				if (sectionRecordList != null && sectionRecordList.size() > i) {
					MeleteSectionRecordModel record = sectionRecordList.get(i);
					node.setMyTime(record.getStudyTime().toString());
				}
				returnList.add(node);
			}
		}
		// 如果前测集合不为空，则遍历整合前测集合、前测记录集合
		if (selfList != null && !selfList.isEmpty()) {
			for (int i = 0; i < selfList.size(); i++) {
				MeleteSelfTestModel model = selfList.get(i);
				if (String.valueOf(model.getStatus()).equals(CodeTable.del)) {
					continue;
				}
				Node node = new Node();
				node.setType(CodeTable.selftest);
				node.setId(model.getId().toString());
				node.setTitle(model.getName());
				node.setRequirement(model.getRequirement());

				if (selfRecordList != null && selfRecordList.size() > i) {
					MeleteSelftestRecordModel record = selfRecordList.get(i);
					node.setPassStatus(record.getStatus().toString());
					node.setAttemptNumber(record.getAttemptNumber());
				}
				returnList.add(node);
			}
		}
		// 如果作业集合不为空，则遍历整合作业集合、作业记录集合
		if (testList != null && !testList.isEmpty()) {
			for (int i = 0; i < testList.size(); i++) {
				MeleteTestModel model = testList.get(i);
				if (String.valueOf(model.getStatus()).equals(CodeTable.del)) {
					continue;
				}
				Node node = new Node();
				node.setType(CodeTable.test);
				node.setId(model.getId().toString());
				node.setTitle(model.getName());
				node.setRequirement(model.getRequirement());

				if (testRecordList != null && testRecordList.size() > i) {
					MeleteTestRecordModel record = testRecordList.get(i);
					node.setPassStatus(record.getStatus().toString());
				}

				returnList.add(node);
			}
		}
		// 如果讨论(论坛)集合不为空，则遍历整合讨论(论坛)集合、讨论(论坛)记录集合
		if (forumList != null && !forumList.isEmpty()) {
			for (int i = 0; i < forumList.size(); i++) {
				MeleteForumModel model = forumList.get(i);
				if (String.valueOf(model.getStatus()).equals(CodeTable.del)) {
					continue;
				}
				Node node = new Node();
				node.setType(CodeTable.forum);
				node.setId(model.getId().toString());
				node.setTitle(model.getName());
				node.setRequirement(model.getRequirement());

				if (forumRecordList != null && forumRecordList.size() > i) {
					MeleteForumRecordModel record = forumRecordList.get(i);
					node.setPassStatus(record.getStatus().toString());
				}

				returnList.add(node);
			}
		}

		return returnList;
	}

	public static String formatVideoTime(String videoTime) {
		if (StringUtils.isBlank(videoTime)) {
			return "";
		}
		// video的格式是00:00:00.00，页面显示格式是1:01:01或00:40
		// 输入格式不正确返回""
		if (videoTime.matches("[0-9][0-9][:][0-9][0-9][:][0-9][0-9][.][0-9][0-9]")) {
			// 去掉毫秒
			String timeStr = videoTime.substring(0, 8);
			String[] timeStrs = timeStr.split(":");
			String rst = "";
			if (!timeStrs[0].equals("00")) {
				rst = Integer.valueOf(timeStrs[0]) + ":";
			}
			rst += timeStrs[1] + ":" + timeStrs[2];
			System.out.println(rst);
			return rst;
		} else {
			return "";
		}
	}

	/**
	 * 生成一个新的课件描述文件中item元素中的identifier属性值
	 */
	public static String getItemId() {
		return "ITEM-" + getUUID();
	}

	/**
	 * 生成一个新的课件描述文件中的resource元素中的identifier属性值
	 */
	public static String getResId() {
		return "RES-" + getUUID();
	}

	/**
	 * 生成一个新的课件描述文件中的根元素manifest元素中的identifier属性值
	 */
	public static String getManifestId() {
		return "MANIFEST-" + getUUID();
	}

	/**
	 * 生成一个新的课件描述文件中的organizations元素中的default属性值与identifier属性值
	 */
	public static String getOrganizationsDefault() {
		return "ORG-" + getUUID();
	}

	/**
	 * 获得UUID
	 * 
	 * @return uuidString
	 */
	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replace("-", "").toUpperCase();
	}

	/**
	 * 通过课程Id获得课程课件的存放路径
	 * 
	 * @param courseId
	 * @return 课程课件存放路径
	 */
	public static String getCoursePathByCourseId(String courseId) {
		return getServicePath() + "/" + Constants.SECTION_PATH + "/" + courseId + "/";
	}

	/**
	 * 创建课程课件描述文件(无实质课件内容的文件)并返回org.w3c.dom.Document 的对象
	 * 
	 * @param courseId
	 */
	public static void createManifest(String courseId) throws Exception {
		String manifestXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<!--This is a Reload version 2.5.5 SCORM 2004 Content Package document-->"
				+ "<!--Spawned from the Reload Content Package Generator - http://www.reload.ac.uk-->"
				+ "<manifest xmlns=\"http://www.imsglobal.org/xsd/imscp_v1p1\" xmlns:imsmd=\"http://ltsc.ieee.org/xsd/LOM\" "
				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:adlcp=\"http://www.adlnet.org/xsd/adlcp_v1p3\" "
				+ "xmlns:imsss=\"http://www.imsglobal.org/xsd/imsss\" xmlns:adlseq=\"http://www.adlnet.org/xsd/adlseq_v1p3\" xmlns:adlnav=\"http://www.adlnet.org/xsd/adlnav_v1p3\" "
				+ "identifier=\"\" "
				+ "xsi:schemaLocation=\"http://www.imsglobal.org/xsd/imscp_v1p1 imscp_v1p1.xsd http://ltsc.ieee.org/xsd/LOM lom.xsd http://www.adlnet.org/xsd/adlcp_v1p3 adlcp_v1p3.xsd http://www.imsglobal.org/xsd/imsss imsss_v1p0.xsd http://www.adlnet.org/xsd/adlseq_v1p3 adlseq_v1p3.xsd http://www.adlnet.org/xsd/adlnav_v1p3 adlnav_v1p3.xsd\">"
				+ "<metadata>" + "<schema>ADL SCORM</schema>" + "<schemaversion>2004 3rd Edition</schemaversion>"
				+ "</metadata>" + "<organizations default=\"\">"
				+ "<organization identifier=\"\" structure=\"hierarchical\">" + "<title>Organization</title>"
				+ "</organization>" + "</organizations>" + "<resources>" + "</resources>" + "</manifest>";
		Document manifestDoc = DocumentHelper.parseText(manifestXML);
		DOMWriter domWriter = new DOMWriter();
		org.w3c.dom.Document courseDocument = domWriter.write(manifestDoc);
		Element manifestElement = courseDocument.getDocumentElement();
		manifestElement.setAttribute("identifier", getManifestId());
		Element orgnizationsElement = (Element) DOMTreeUtility.getNode(manifestElement, "organizations");// 获得organizations元素
		String orgId = getOrganizationsDefault();
		orgnizationsElement.setAttribute("default", orgId);
		Element orgElement = (Element) DOMTreeUtility.getNode(orgnizationsElement, "organization");
		orgElement.setAttribute("identifier", orgId);
		// 写入到课程目录下的课件描述文件中
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");
		// 将更改的document写入到课件存储目录中的imsmanifest.xml文件
		String coursePath = getCoursePathByCourseId(courseId);
		XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(coursePath + "imsmanifest.xml"), format);
		DOMReader domReader = new DOMReader();
		Document document = domReader.read(courseDocument);
		xmlWriter.write(document);
		xmlWriter.close();
	}

	/**
	 * 获得文件扩展名
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getExtName(String fileName) {
		int pos = fileName.lastIndexOf(".");
		return pos < 0 ? "" : fileName.substring(pos + 1);
	}

	public static void main(String[] args) {
		long time = System.currentTimeMillis() / 1000;
		String query = "uid=1&from=1&time=" + time;
		String hash = MD5Encrypt.MD5Encode(query + "&key=reskey0001");
		// 以下两行用于测试/api/user/courses?uid=1&from=myres&time=1428544762&auth=bd81b8868021bc80641014df080e7344

		System.out.println(query + "&auth=" + hash);
		Map<String, String> m = new HashMap<String, String>();
		m.put("uid", "1");
		m.put("from", "1");
		m.put("cid", "1");
		System.out.println(CourseUtil.createAPIQueryString(m, time, "reskey0001"));
	}
}
