package org.sakaiproject.resource.tool.test.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts2.ServletActionContext;
import org.sakaiproject.authz.api.SecurityService;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.resource.api.course.vo.PaperAttributeNew;
import org.sakaiproject.resource.api.course.vo.SchemaContentNew;
import org.sakaiproject.resource.api.course.vo.SchemaNewVO;
import org.sakaiproject.resource.api.test.model.Test;
import org.sakaiproject.resource.api.test.model.TestRecord;
import org.sakaiproject.resource.api.test.service.ITestService;
import org.sakaiproject.resource.util.AsyncGridModel;
import org.sakaiproject.resource.util.CodeTable;
import org.sakaiproject.resource.util.Constants;
import org.sakaiproject.resource.util.DES;
import org.sakaiproject.resource.util.FunctionRegister;
import org.sakaiproject.resource.util.JsonBuilder;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.tool.api.ToolManager;
import org.sakaiproject.user.api.UserDirectoryService;

import com.bupticet.paperadmin.tool.Helper;
import com.bupticet.paperadmin.tool.PaperCheckToolUtil;
import com.bupticet.paperadmin.tool.ServerActionTool;
import com.opensymphony.xwork2.ActionSupport;

public class TestAction extends ActionSupport{
	private String courseId = "1766";
	private String testId;
	private String testName;
	private String schemaId;//策略id
	private String totalScore;//满分
	private String masteryScore;//通过分数
	private String samepaper;//是否使用同一策略:1是、0否
	private String paperId;//试卷id
	private String answer;//答案
	private String startTime;//开始做作业时间
	private String start;//分页开始行数
	private String checkStatus;//批改状态
	private String endTime;//结束时间
	private String recordId;//记录id
	private String studentId;//学生id
	private String studentScore;//学生主观题分数
	
	private ITestService service;

	public String getTestId() {
		return testId;
	}

	public void setTestId(String testId) {
		this.testId = testId;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public String getSchemaId() {
		return schemaId;
	}

	public void setSchemaId(String schemaId) {
		this.schemaId = schemaId;
	}

	public String getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(String totalScore) {
		this.totalScore = totalScore;
	}

	public String getMasteryScore() {
		return masteryScore;
	}

	public void setMasteryScore(String masteryScore) {
		this.masteryScore = masteryScore;
	}

	public String getSamepaper() {
		return samepaper;
	}

	public void setSamepaper(String samepaper) {
		this.samepaper = samepaper;
	}

	public ITestService getService() {
		return service;
	}

	public void setService(ITestService service) {
		this.service = service;
	}
	
	private SiteService siteService = (SiteService) ComponentManager.get(SiteService.class);

	private ToolManager toolManager = (ToolManager) ComponentManager.get(ToolManager.class);
	
	private UserDirectoryService userDirectoryService = (UserDirectoryService) ComponentManager.get(UserDirectoryService.class);
	
	private SecurityService securityService = (SecurityService) ComponentManager.get(SecurityService.class);
	
	private Site getCurrentSite() throws Exception {
		Site site = null;
		try {
			site = siteService.getSite(toolManager.getCurrentPlacement()
					.getContext());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return site;
	}
	
	private String getCurrentUserId(){
		return userDirectoryService.getCurrentUser().getId();
	}
	
	public String execute() throws Exception{
		Site curSite = this.getCurrentSite();
		String siteRef = siteService.siteReference(curSite.getId()); 
		String currentUser = this.getCurrentUserId();
		if(securityService.unlock(currentUser, FunctionRegister.COURSE_SPACE_PERM, siteRef)){//教师
			return "teacher";
		} else if(securityService.unlock(currentUser, FunctionRegister.STUDY_SPACE_PERM, siteRef)){//学生
			return "student";
		}
		return null;
	}
	
	public String manageInit(){
		return "edit";
	}
	
	public String checkInit(){
		return "check";
	}
	
	public String statInit(){
		return "stat";
	}
	
	/**
	 * 获取全部作业（暂不用分页，以后根据需要看是否要改成分页查询）
	 * @return
	 */
	public String getTestList(){
		try{
			Site curSite = this.getCurrentSite();
			String siteId = curSite.getId();
			String siteRef = siteService.siteReference(siteId); 
			String currentUser = this.getCurrentUserId();
			String userType = null;
			if(securityService.unlock(currentUser, FunctionRegister.COURSE_SPACE_PERM, siteRef)){//教师
				userType = "teacher";
			} else if(securityService.unlock(currentUser, FunctionRegister.STUDY_SPACE_PERM, siteRef)){//学生
				userType = "student";
			}
			List<Test> list = service.getTestListBySiteId(siteId,userType);
			List<AsyncGridModel> data = new ArrayList<AsyncGridModel>();
			
			if(!list.isEmpty() && list.size()!=0){
				for(Test test: list){
					AsyncGridModel model = new AsyncGridModel();
					model.setDataIndex("testId", test.getTestId());//作业id
					model.setDataIndex("testName", test.getTestName());//作业名称
					model.setDataIndex("samepaper", test.getSamepaper());//是否使用同一策略:1是、0否
					model.setDataIndex("masteryScore", test.getMasteryScore());//通过分数
					int schemaId = test.getSchemaId();
					//SchemaTO schemaTO = SchemaToolUtil.getSchemaTO(schemaId);
					//String schemaName = schemaTO.strSchemaName;
					//model.setDataIndex("schemaId", schemaId);//策略id
					//model.setDataIndex("schemaName", schemaName);//策略名称
					model.setDataIndex("totalScore", test.getTotalScore());//满分
					model.setDataIndex("status", test.getStatus());//状态
					data.add(model);
				}
			}
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			String json = JsonBuilder.builderAsyncGridJson(data);
			response.getWriter().println(json);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获取策略列表
	 * @return
	 * @throws Exception
	 */
	public String loadSchemaBox() throws Exception {
		try {
			// int cid = new Integer(this.exCourseid).intValue();
			List<SchemaNewVO> ls = getSchemaListFromExam(this.courseId);// SchemaToolUtil.getSchemaTOListByCourseId(cid);
			StringBuffer sb = new StringBuffer("[");
			if (ls != null && ls.size() > 0) {
				for (int i = 0; i < ls.size(); i++) {
					SchemaNewVO schema = ls.get(i);
					int tid = schema.getnSchemaID();
					String tn = schema.getStrSchemaName();
					// SchemaTO schemaTO = SchemaToolUtil.getSchemaTO(tid);
					int score = 0;
					SchemaContentNew sc = schema.getcSchemaCont(); // schemaTO.cSchemaCont;
					if (sc != null) {
						PaperAttributeNew pa = sc.getcFullMark();
						if (pa != null) {
							score = pa.getnDemand();
						}
					}
					sb.append("['").append(tid).append("','").append(tn).append("','" + score + "'],");
				}
			}
			sb = sb.deleteCharAt(sb.length() - 1).append("]");

			HttpServletResponse response = ServletActionContext.getResponse();
			response.getWriter().println(sb.toString());
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 通过接口获取题库策略
	 * 
	 * @param examCourseId
	 * @return
	 * @throws Exception
	 */
	private List<SchemaNewVO> getSchemaListFromExam(String examCourseId) throws Exception {
		// 从新接口获取策略
		byte[] tt = DES.encrypt(Constants.OUTSYS_EXAM_KEY_ENTRY.getBytes(Charset.forName("utf-8")),
				userDirectoryService.getCurrentUser().getEid());
		String key = Base64.encodeBase64String(tt);
		String urlStr = Constants.OUTSYS_EXAM_URL_BASE
				+ Constants.OUTSYS_EXAM_URL_SCHEMA.replace("${courseId}", examCourseId).replace("${key}", key);
		URL url = new URL(urlStr);
		InputStream inputStream = url.openStream();

		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("utf-8")));
		StringBuilder resp_result = new StringBuilder();
		for (String s = reader.readLine(); s != null; s = reader.readLine()) {
			resp_result.append(s).append("\n");
		}
		reader.close();
		inputStream.close();

		List<SchemaNewVO> objs = null;
		JSONArray jsonArray = (JSONArray) JSONSerializer.toJSON(resp_result.toString());
		if (jsonArray != null) {
			objs = new ArrayList<SchemaNewVO>();
			List list = (List) JSONSerializer.toJava(jsonArray);
			for (Object o : list) {
				JSONObject jsonObject = JSONObject.fromObject(o);
				SchemaNewVO obj = (SchemaNewVO) JSONObject.toBean(jsonObject, SchemaNewVO.class);
				objs.add(obj);
			}
		}

		return objs;
	}
	/**
	 * 新增作业
	 * @return
	 */
	public String addTest(){
		try{
			Test test = new Test();
			test.setTestName(testName);
			test.setSiteId(this.getCurrentSite().getId());
			test.setCreationDate(new Date());
			test.setMasteryScore(Integer.valueOf(masteryScore).intValue());
			test.setSamepaper(samepaper);
			test.setSchemaId(Integer.valueOf(schemaId).intValue());
			test.setStatus("1");//1未发布
			test.setTotalScore(Integer.valueOf(totalScore).intValue());
			service.addModel(test);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 修改作业
	 * @return
	 */
	public String updateTest(){
		try{
			Test test = (Test)service.getModel(Test.class, Integer.parseInt(testId));
			test.setTestName(testName);
			test.setMasteryScore(Integer.valueOf(masteryScore).intValue());
			test.setSamepaper(samepaper);
			test.setSchemaId(Integer.valueOf(schemaId).intValue());
			test.setTotalScore(Integer.valueOf(totalScore).intValue());
			test.setModificationDate(new Date());
			service.updateModel(test);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 批量删除作业
	 * @return
	 */
	public String deleteTest(){
		try{
			String[] ids = testId.split(",");
			service.deleteTest(ids);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 发布作业
	 * @return
	 * @throws Exception
	 */
	public String publishTest()throws Exception{
		try{
			service.updateTestStatus(testId, "3");
			//生成试卷库
			String count = Constants.operateCount;//生成试卷的数量
			String siteId = this.getCurrentSite().getId();
			String materialPath = Constants.getTestMaterialPath(siteId);
			String libPath = Constants.getTestLibPath(siteId, testId);
			String ansPath = Constants.getTestLibAnswerPath(siteId, testId);
			ServerActionTool.generateTest(libPath, ansPath, 
					materialPath, new Integer(schemaId).intValue(), new Integer(count).intValue());
			service.updateTestStatus(testId, "2");
			return null;
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	
	public String writeTestInit() throws Exception {
		String studentId = getCurrentUserId();
		String siteId = this.getCurrentSite().getId();
		// 文件保存路径
		String path = Constants.getStuTestPath(studentId, siteId);
		String content = null;
		Date startTime = new Date();
		String testPaperId = null;
		try {
			String testPaperName = null;
			if (samepaper.equals("1")) {// 每次使用同一试卷
				testPaperName = Helper.TestPrefix + testId + Helper.EndTag;
				File destFile = new File(path, testPaperName);
				File aDestFile = new File(path, Helper.getAnswerName(testPaperName));
				if (!destFile.exists() || !aDestFile.exists()) {
					// 拷贝试卷文件
					File srcFile = getTestRandomPaper(siteId, testId);
					FileUtils.copyFile(srcFile, destFile);
					// 拷贝答案文件
					File aSrcFile = new File(Constants.getTestLibAnswerPath(siteId, testId), Helper
							.getAnswerName(srcFile.getName()));
					FileUtils.copyFile(aSrcFile, aDestFile);
				}
			} else {
				File srcFile = getTestRandomPaper(siteId, testId);
				testPaperName = srcFile.getName();
				File destFile = new File(path, testPaperName);
				File aDestFile = new File(path, Helper.getAnswerName(testPaperName));
				if (!destFile.exists() || !aDestFile.exists()) {
					FileUtils.copyFile(srcFile, destFile);
					// 拷贝答案文件
					File aSrcFile = new File(Constants.getTestLibAnswerPath(siteId, testId), Helper
							.getAnswerName(testPaperName));
					FileUtils.copyFile(aSrcFile, aDestFile);
				}
			}
			testPaperId = Helper.getPaperId(testPaperName);
			content = ServerActionTool.exportExercisePaper(Constants.getTestMaterialURL(siteId), path, testPaperName);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		ServletActionContext.getRequest().setAttribute("content", content);
		ServletActionContext.getRequest().setAttribute("startTime", startTime.getTime());
		ServletActionContext.getRequest().setAttribute("testId", testId);
		ServletActionContext.getRequest().setAttribute("paperId", testPaperId);
		ServletActionContext.getRequest().setAttribute("masteryScore", masteryScore);
		ServletActionContext.getRequest().setAttribute("samepaper", samepaper);
		ServletActionContext.getRequest().setAttribute("totalScore", totalScore);
		return "write";
	}
	
	public String writeTestSave() throws Exception {
		String studentId = getCurrentUserId();
		String siteId = this.getCurrentSite().getId();
		TestRecord testRecord = null;
		String content = "";
		try {
			testRecord = service.saveTestRecord(siteId, studentId, paperId, answer, startTime, masteryScore, testId, totalScore);
			int recordId = testRecord.getTestrecordId();
			String paperName = Helper.getPaperName(paperId);
			String answerName = Helper.getStuAnswerName(paperId, ""+recordId);
			String path2 = Constants.getStuTestPath(studentId, siteId);
			content = ServerActionTool.exportExamInfPaper(Constants.getTestMaterialURL(siteId), path2, paperName,
					path2, answerName, samepaper.equals(CodeTable.samePaperNo));

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		ServletActionContext.getRequest().setAttribute("data", JsonBuilder.builderObjectJson(content));
		return "result";
	}
	
	public String loadTestBox() throws Exception {
		String siteId = this.getCurrentSite().getId();
		List<Test> resultList = service.getTestListBySiteId(siteId, "student");
		StringBuffer sb = new StringBuffer("[");
		try {
			if (!resultList.isEmpty()) {
				for (int i = 0; i < resultList.size(); i++) {
					Test test = resultList.get(i);
					// 前台批改试卷下拉框的value为作业或前测的ID_类型码 类型1表示类型为作业
					sb.append("['").append(test.getTestId()).append("','").append(test.getTestName()).append("'],");
				}
				sb = sb.deleteCharAt(sb.length() - 1);
			}
			sb.append("]");
			HttpServletResponse response = ServletActionContext.getResponse();
			response.getWriter().println(sb.toString());
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 查找要批改的作业记录
	 * 
	 * @return
	 */
	public String findTestRecordList() throws Exception {
		try {
			int startInt = NumberUtils.toInt(start);
			String endTimeTrans = endTime.replace("T", " ");
			List<AsyncGridModel> data = new ArrayList<AsyncGridModel>();
			Object[] results = service.findTestRecordList(testId, checkStatus, endTimeTrans, startInt);
			List<Object> testList = (List<Object>) results[1];
			for (Object obj : testList) {
				Map map = (Map) obj;
				AsyncGridModel model = new AsyncGridModel();
				model.setDataIndex("checkStatus", map.get("checkStatus"));
				model.setDataIndex("studentName", map.get("studentName"));
				model.setDataIndex("stuNum", map.get("stuNum"));
				model.setDataIndex("eduCenter", map.get("eduCenter"));
				model.setDataIndex("score", map.get("score"));
				model.setDataIndex("id", map.get("id"));
				model.setDataIndex("userid", map.get("userid"));
				model.setDataIndex("paperid", map.get("paperid"));
				data.add(model);
			}
			String json = JsonBuilder.builderAsyncGridJson(data);
			ServletActionContext.getResponse().getWriter().println(json);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	public String checkTestInit() throws Exception {
		try {
			String pName = Helper.getPaperName(paperId);
			String answerName = Helper.getStuAnswerName(paperId, recordId);
			String path2 = "";
			String contentString = "";
			String siteId = this.getCurrentSite().getId();
			path2 = Constants.getStuTestPath(studentId, siteId);
			contentString = ServerActionTool.exportObjectPaper(Constants.getTestMaterialURL(siteId), path2,
					pName, path2, answerName);
			

			ServletActionContext.getRequest().setAttribute("content", contentString);
			ServletActionContext.getRequest().setAttribute("paperId", paperId);
			ServletActionContext.getRequest().setAttribute("recordId", recordId);
			ServletActionContext.getRequest().setAttribute("studentId", studentId);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return "checkPage";
	}

	/**
	 * 教师批完卷提交处理
	 * 
	 * @return
	 * @throws Exception
	 */
	public String checkTestSave() throws Exception {
		try {
			String answerFileName = Helper.getStuAnswerName(paperId, recordId);
			String path = "";
			String siteId = this.getCurrentSite().getId();
			path = Constants.getStuTestPath(studentId, siteId);
			
			try {
				File studentAnswerFile = new File(path, answerFileName);
				studentScore = studentScore.replace("\"[", "\"").replace("]\"", "\"");

				PaperCheckToolUtil.checkObjPaperAction(studentScore, studentAnswerFile);
				// String tag = studentAnswerFile.getName().substring(0, 1);
				service.checkTestSave(recordId, studentId, studentAnswerFile, siteId);
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	private static File getTestRandomPaper(String siteId, String testId) throws Exception {
		File p = new File(Constants.getTestLibPath(siteId, testId));
		if (!p.exists() || !p.isDirectory()) {
			throw new Exception("Can't found the test paper lib path!");
		}
		File[] fList = p.listFiles();
		int fSum = fList.length;
		if (fSum <= 0) {
			throw new Exception("Can't found papers in the test paper lib directory!");
		}
		Random r = new Random();
		int fNum = r.nextInt(fSum);
		return fList[fNum];
	}

	public String getPaperId() {
		return paperId;
	}

	public void setPaperId(String paperId) {
		this.paperId = paperId;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getStudentScore() {
		return studentScore;
	}

	public void setStudentScore(String studentScore) {
		this.studentScore = studentScore;
	}
}
