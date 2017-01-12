package org.sakaiproject.resource.tool.studymanage.action;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.resource.api.course.model.MeleteCourseModel;
import org.sakaiproject.resource.api.course.model.MeleteTestModel;
import org.sakaiproject.resource.api.course.service.ICourseService;
import org.sakaiproject.resource.api.statistics.service.IStatisticsService;
import org.sakaiproject.resource.util.AsyncGridModel;
import org.sakaiproject.resource.util.CacheElement;
import org.sakaiproject.resource.util.CacheUtil;
import org.sakaiproject.resource.util.JsonBuilder;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.tool.api.ToolManager;

import com.opensymphony.xwork2.ActionSupport;

public class StudyManageAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Log logger = LogFactory.getLog(StudyManageAction.class);

	private SiteService siteService = (SiteService) ComponentManager.get(SiteService.class);

	private ToolManager toolManager = (ToolManager) ComponentManager.get(ToolManager.class);

	private IStatisticsService statisticsService;

	private ICourseService courseService;

	// 起始
	private String start;

	// 排序字段
	private String sort;

	// 排序方式
	private String dir;

	/**
	 * 学生姓名
	 */
	private String stuName;

	/**
	 * 学号
	 */
	private String stuNum;

	/**
	 * 学习开始时间
	 */
	private String studyStartTime;

	/**
	 * 学习结束时间
	 */
	private String studyEndTime;

	/**
	 * 学习记录id
	 */
	private Long studyrecordId;

	/**
	 * 分数范围开始
	 */
	private Long scoreStart;

	/**
	 * 分数范围结束
	 */
	private Long scoreEnd;

	/**
	 * 阶段作业id
	 */
	private String testId;

	/**
	 * 做作业开始时间
	 */
	private String testStartTime;

	/**
	 * 做作业结束时间
	 */
	private String testEndTime;

	/**
	 * 测试记录id
	 */
	private Long testrecordId;

	/**
	 * 获得当前站点
	 * 
	 * @return
	 * @throws Exception
	 */
	private Site getCurrentSite() throws Exception {
		Site site = null;
		try {
			site = siteService.getSite(toolManager.getCurrentPlacement().getContext());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
		return site;
	}

	private MeleteCourseModel getCurrentCourse() throws Exception {
		Site site = this.getCurrentSite();
		// 检查缓存
		CacheElement cacheCourse = CacheUtil.getInstance().getCacheOfCourse(site.getId());
		return cacheCourse.getCourse();
	}

	/**
	 * 进入到成绩管理页面
	 */
	public String execute() throws Exception {
		return "success";
	}

	/**
	 * 查询学习时长
	 */
	public String toStudyTime() {
		return "studyTime";
	}
	
	private Map<String,Object> getStudyTimeConditions() throws Exception{
		String startTimeTrans = null;
		String endTimeTrans = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = null;
		Date endDate = null;

		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("courseId", getCurrentCourse().getId());
		if (StringUtils.isNotBlank(stuName)) {
			conditions.put("stuName", stuName);
		}

		if (StringUtils.isNotBlank(stuNum)) {
			conditions.put("stuNum", stuNum);
		}

		if (StringUtils.isNotBlank(studyStartTime)) {
			startTimeTrans = studyStartTime.replace("T", " ");
			startDate = sdf.parse(startTimeTrans);
			conditions.put("studyStartTime", startDate);
		}
		if (StringUtils.isNotBlank(studyEndTime)) {
			endTimeTrans = studyEndTime.replace("T", " ");
			endDate = sdf.parse(endTimeTrans);
			conditions.put("studyEndTime", endDate);
		}
		return conditions;
	}

	/**
	 * 查询学习时长
	 */
	public String findStudyTime() throws Exception {

		try {
			int startInt = NumberUtils.toInt(start);// null和异常返回默认值

			List<AsyncGridModel> data = new ArrayList<AsyncGridModel>();
			Object[] results = statisticsService.getStudyTimeByCondition(getStudyTimeConditions(), startInt, sort, dir);
			List<Object> testList = (List<Object>) results[1];
			for (Object obj : testList) {// 遍历testList
				Map map = (Map) obj;
				AsyncGridModel model = new AsyncGridModel();
				model.setDataIndex("studyrecordId", map.get("studyrecordId"));
				model.setDataIndex("stuName", map.get("stuName"));
				model.setDataIndex("stuNum", map.get("stuNum"));
				model.setDataIndex("speName",map.get("speName"));
				model.setDataIndex("className",map.get("className"));
				model.setDataIndex("sex", this.getSexFormat((String) map.get("sex")));
				model.setDataIndex("startStudyTime", map.get("startStudyTime"));
				model.setDataIndex("totalTime", map.get("totalTime")==null ? 0 : map.get("totalTime"));
				data.add(model);
			}
			String json = JsonBuilder.builderAsyncGridJson(data, (Integer) results[0]);
			ServletActionContext.getResponse().getWriter().println(json);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * 查询学习时长汇总信息
	 */
	public String getStudyTimeSum() throws Exception {

		try {

			Map<String, Object> result = statisticsService.getStudyTimeSumByCondition(getStudyTimeConditions());
			String json = JsonBuilder.builderMapJson(result);
			ServletActionContext.getResponse().getWriter().println(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * 查询学习详情
	 */
	public String getStudyInfo() throws Exception {

		try {
			List<AsyncGridModel> data = new ArrayList<AsyncGridModel>();
			Object[] results = statisticsService.getStudyInfo(studyrecordId, sort, dir);
			List<Object> testList = (List<Object>) results[1];
			for (Object obj : testList) {// 遍历testList
				Map map = (Map) obj;
				AsyncGridModel model = new AsyncGridModel();
				model.setDataIndex("title", map.get("title"));
				model.setDataIndex("startStudyTime", map.get("startStudyTime"));
				model.setDataIndex("endStudyTime", map.get("endStudyTime"));
				model.setDataIndex("studyTime", map.get("studyTime"));
				data.add(model);
			}
			String json = JsonBuilder.builderAsyncGridJson(data, (Integer) results[0]);
			ServletActionContext.getResponse().getWriter().println(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * 导出学习时长
	 * 
	 * @return
	 */
	public String exportStudyTime() {

		try {

			Object[] results = statisticsService.getStudyTimeByCondition(getStudyTimeConditions(), null, sort, dir);
			List<Object> testList = (List<Object>) results[1];

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("学习时长"); 
			sheet.setColumnWidth(0, 6000); 
			sheet.setColumnWidth(1, 6000);
			sheet.setColumnWidth(2, 6000);
			sheet.setColumnWidth(3, 6000);
			sheet.setColumnWidth(4, 6000);
			XSSFRow row = sheet.createRow((int) 0);

			XSSFCellStyle style = workbook.createCellStyle();
			style.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
			style.setFillForegroundColor((short) 13);// 设置背景色
			style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
			
			style.setBorderBottom(XSSFCellStyle.BORDER_THIN); //下边框
			style.setBorderLeft(XSSFCellStyle.BORDER_THIN);//左边框
			style.setBorderTop(XSSFCellStyle.BORDER_THIN);//上边框
			style.setBorderRight(XSSFCellStyle.BORDER_THIN);//右边框
			
			int i = 0;
			XSSFCell cell = row.createCell(0);
			cell.setCellValue("学号");
			cell.setCellStyle(style);
			cell = row.createCell(1);
			cell.setCellValue("姓名");
			cell.setCellStyle(style);
			cell = row.createCell(2);
			cell.setCellValue("专业");
			cell.setCellStyle(style);
			cell = row.createCell(3);
			cell.setCellValue("班级");
			cell.setCellStyle(style);
			cell = row.createCell(4);
			cell.setCellValue("性别");
			cell.setCellStyle(style);
			cell = row.createCell(5);
			cell.setCellValue("学习时长");
			cell.setCellStyle(style); 

			for (Object obj : testList) {// 遍历testList
				Map map = (Map) obj;
				row = sheet.createRow(++i);
				row.createCell(0).setCellValue((String) map.get("stuNum"));
				row.createCell(1).setCellValue((String) map.get("stuName"));
				row.createCell(2).setCellValue((String) map.get("speName"));
				row.createCell(3).setCellValue((String) map.get("className"));
				row.createCell(4).setCellValue(this.getSexFormat((String) map.get("sex")));
				row.createCell(5).setCellValue(map.get("totalTime")!=null ? (Long) map.get("totalTime") : new Long(0));
			}
			String fileName = "学习时长.xlsx";
			HttpServletResponse response = ServletActionContext.getResponse();
			fileName = new String(fileName.getBytes(), "ISO8859-1");
			response.setContentType("application/octet-stream;charset=ISO8859-1");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			response.addHeader("Pargam", "no-cache");
			response.addHeader("Cache-Control", "no-cache");
			workbook.write(response.getOutputStream());
			response.getOutputStream().flush();
			response.getOutputStream().close();  
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}  

		return null;
	}

	/**
	 * 查询阶段成绩
	 */
	public String toTestScore() {
		return "testScore";
	}

	/**
	 * 作业列表
	 * 
	 * @return
	 */
	public String getTestList() throws Exception {
		// 根据站点ID所有的作业名称
		try {
			MeleteCourseModel course = courseService.getCourseBySiteId(getCurrentSite().getId());
			List<MeleteTestModel> list = courseService.getAllMeleteTestModelByCourseId(course.getId());
			StringBuffer sb = new StringBuffer("[");

			if (!list.isEmpty()) {
				for (int i = 0; i < list.size(); i++) {
					MeleteTestModel testModel = list.get(i);
					sb.append("['").append(testModel.getId()).append("','").append(testModel.getName()).append("'],");

				}
				sb = sb.deleteCharAt(sb.length() - 1);
			}
			sb.append("]");
			ServletActionContext.getResponse().getWriter().println(sb.toString());
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}
	
	private Map<String,Object> getTestScoreConditions() throws Exception {
		String startTimeTrans = null;
		String endTimeTrans = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = null;
		Date endDate = null;

		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("courseId", getCurrentCourse().getId());
		if (StringUtils.isNotBlank(stuName)) {
			conditions.put("stuName", stuName);
		}

		if (StringUtils.isNotBlank(stuNum)) {
			conditions.put("stuNum", stuNum);
		}

		if (StringUtils.isNotBlank(testStartTime)) {
			startTimeTrans = testStartTime.replace("T", " ");
			startDate = sdf.parse(startTimeTrans);
			conditions.put("testStartTime", startDate);
		}
		if (StringUtils.isNotBlank(testEndTime)) {
			endTimeTrans = testEndTime.replace("T", " ");
			endDate = sdf.parse(endTimeTrans);
			conditions.put("testEndTime", endDate);
		}
		if (StringUtils.isNotBlank(testId)) {
			conditions.put("testId", testId);
		}
		if (scoreStart != null) {
			conditions.put("scoreStart", scoreStart);
		}
		if (scoreEnd != null) {
			conditions.put("scoreEnd", scoreEnd);
		}
		return conditions;
	}

	/**
	 * 查询阶段成绩
	 */
	public String findTestScore() throws Exception {

		try {
			int startInt = NumberUtils.toInt(start);// null和异常返回默认值
			List<AsyncGridModel> data = new ArrayList<AsyncGridModel>();
			Object[] results = statisticsService.getTestScoreByCondition(getTestScoreConditions(), startInt, sort, dir);
			List<Object> testList = (List<Object>) results[1];
			for (Object obj : testList) {// 遍历testList
				Map map = (Map) obj;
				AsyncGridModel model = new AsyncGridModel();
				model.setDataIndex("testrecordId", map.get("testrecordId"));
				model.setDataIndex("stuName", map.get("stuName"));
				model.setDataIndex("stuNum", map.get("stuNum"));
				model.setDataIndex("speName",map.get("speName"));
				model.setDataIndex("className",map.get("className"));
				model.setDataIndex("sex", this.getSexFormat((String) map.get("sex")));
				model.setDataIndex("startStudyTime", map.get("startStudyTime"));
				model.setDataIndex("attemptNumber", map.get("attemptNumber"));
				model.setDataIndex("score", map.get("score")==null ? 0 : map.get("score"));
				data.add(model);
			}
			String json = JsonBuilder.builderAsyncGridJson(data, (Integer) results[0]);
			ServletActionContext.getResponse().getWriter().println(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * 查询阶段成绩汇总信息
	 */
	public String getTestScoreSum() throws Exception {

		try {

			Map<String, Object> result = statisticsService.getTestScoreSumByCondition(getTestScoreConditions());
			MeleteCourseModel course = courseService.getCourseBySiteId(getCurrentSite().getId());
			List<MeleteTestModel> list = courseService.getAllMeleteTestModelByCourseId(course.getId());

			if (!list.isEmpty()) {
				for (int i = 0; i < list.size(); i++) {
					MeleteTestModel testModel = list.get(i);
					if (testModel.getId() == Long.parseLong(testId)) {
						result.put("testName", testModel.getName());
						result.put("testRatio", testModel.getRatio());
					}
				}
			}

			String json = JsonBuilder.builderMapJson(result);
			ServletActionContext.getResponse().getWriter().println(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * 查询阶段成绩详情
	 */
	public String getTestScoreInfo() throws Exception {

		try {
			List<AsyncGridModel> data = new ArrayList<AsyncGridModel>();
			Object[] results = statisticsService.getTestScoreInfo(testrecordId, sort, dir);
			List<Object> testList = (List<Object>) results[1];
			for (Object obj : testList) {// 遍历testList
				Map map = (Map) obj;
				AsyncGridModel model = new AsyncGridModel();
				model.setDataIndex("userId", map.get("userId"));
				model.setDataIndex("testId", map.get("testId"));
				model.setDataIndex("testattemptId", map.get("testattemptId"));
				model.setDataIndex("startTime", map.get("startTime"));
				model.setDataIndex("endTime", map.get("endTime"));
				model.setDataIndex("testPaperid", map.get("testPaperid"));
				model.setDataIndex("objScore", map.get("objScore"));
				model.setDataIndex("subScore", map.get("subScore"));
				model.setDataIndex("score", map.get("score"));
				data.add(model);
			}
			String json = JsonBuilder.builderAsyncGridJson(data, (Integer) results[0]);
			ServletActionContext.getResponse().getWriter().println(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * 导出阶段成绩
	 * 
	 * @return
	 */
	public String exportTestScore() {
		try {
			Object[] results = statisticsService.getTestScoreByCondition(getTestScoreConditions(), null, sort, dir);
			List<Object> testList = (List<Object>) results[1];
			
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("阶段成绩"); 
			sheet.setColumnWidth(0, 6000); 
			sheet.setColumnWidth(1, 6000);
			sheet.setColumnWidth(2, 6000);
			sheet.setColumnWidth(3, 6000);
			sheet.setColumnWidth(4, 6000);
			sheet.setColumnWidth(5, 6000);
			XSSFRow row = sheet.createRow((int) 0);

			XSSFCellStyle style = workbook.createCellStyle();
			style.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
			style.setFillForegroundColor((short) 13);// 设置背景色
			style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
			
			style.setBorderBottom(XSSFCellStyle.BORDER_THIN); //下边框
			style.setBorderLeft(XSSFCellStyle.BORDER_THIN);//左边框
			style.setBorderTop(XSSFCellStyle.BORDER_THIN);//上边框
			style.setBorderRight(XSSFCellStyle.BORDER_THIN);//右边框
			
			int i = 0;
			XSSFCell cell = row.createCell(i);
			cell.setCellValue("学号");
			cell.setCellStyle(style);
			cell = row.createCell(1);
			cell.setCellValue("姓名");
			cell.setCellStyle(style);
			cell = row.createCell(2);
			cell.setCellValue("专业");
			cell.setCellStyle(style);
			cell = row.createCell(3);
			cell.setCellValue("班级");
			cell.setCellStyle(style);
			cell = row.createCell(4);
			cell.setCellValue("性别");
			cell.setCellStyle(style);
			cell = row.createCell(5);
			cell.setCellValue("尝试次数");
			cell.setCellStyle(style);
			cell = row.createCell(6);
			cell.setCellValue("最高得分");
			cell.setCellStyle(style);

			for (Object obj : testList) {// 遍历testList
				Map map = (Map) obj;
				row = sheet.createRow(++i);
				row.createCell(0).setCellValue((String) map.get("stuNum"));
				row.createCell(1).setCellValue((String) map.get("stuName"));
				row.createCell(2).setCellValue((String) map.get("speName"));
				row.createCell(3).setCellValue((String) map.get("className"));
				row.createCell(4).setCellValue(this.getSexFormat((String) map.get("sex")));
				row.createCell(5).setCellValue(map.get("attemptNumber")!=null ? (Long) map.get("attemptNumber") : new Long(0));
				row.createCell(6).setCellValue(map.get("score")!=null ? (Float) map.get("score") : new Long(0));
			}
			String fileName = "阶段成绩.xlsx";
			HttpServletResponse response = ServletActionContext.getResponse();
			fileName = new String(fileName.getBytes(), "ISO8859-1");
			response.setContentType("application/octet-stream;charset=ISO8859-1");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			response.addHeader("Pargam", "no-cache");
			response.addHeader("Cache-Control", "no-cache");
			workbook.write(response.getOutputStream());
			response.getOutputStream().flush();
			response.getOutputStream().close();  
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * 查询总成绩
	 */
	public String toScore() {
		return "score";
	}

	private Map<String,Object> getScoreConditions() throws Exception{
		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("courseId", getCurrentCourse().getId());
		if (StringUtils.isNotBlank(stuName)) {
			conditions.put("stuName", stuName);
		}

		if (StringUtils.isNotBlank(stuNum)) {
			conditions.put("stuNum", stuNum);
		}

		if (scoreStart != null) {
			conditions.put("scoreStart", scoreStart);
		}
		if (scoreEnd != null) {
			conditions.put("scoreEnd", scoreEnd);
		}
		return conditions;
	}
	/**
	 * 查询总成绩
	 */
	public String findScore() throws Exception {

		try {
			int startInt = NumberUtils.toInt(start);// null和异常返回默认值
			List<AsyncGridModel> data = new ArrayList<AsyncGridModel>();
			Object[] results = statisticsService.getScoreByCondition(getScoreConditions(), startInt, sort, dir);
			List<Object> testList = (List<Object>) results[1];
			for (Object obj : testList) {// 遍历testList
				Map map = (Map) obj;
				AsyncGridModel model = new AsyncGridModel();
				model.setDataIndex("studyrecordId", map.get("studyrecordId"));
				model.setDataIndex("stuName", map.get("stuName"));
				model.setDataIndex("stuNum", map.get("stuNum"));
				model.setDataIndex("speName",map.get("speName"));
				model.setDataIndex("className",map.get("className"));
				model.setDataIndex("sex", this.getSexFormat((String) map.get("sex")));
				model.setDataIndex("score", map.get("score")==null ? 0 : map.get("score"));
				data.add(model);
			}
			String json = JsonBuilder.builderAsyncGridJson(data, (Integer) results[0]);
			ServletActionContext.getResponse().getWriter().println(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * 查询总成绩汇总信息
	 */
	public String getScoreSum() throws Exception {

		try {

			Map<String, Object> result = statisticsService.getScoreSumByCondition(getScoreConditions());

			String json = JsonBuilder.builderMapJson(result);
			ServletActionContext.getResponse().getWriter().println(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * 查询总成绩详情
	 */
	public String getScoreInfo() throws Exception {

		try {
			List<AsyncGridModel> data = new ArrayList<AsyncGridModel>();
			Object[] results = statisticsService.getScoreInfo(studyrecordId, sort, dir);
			List<Object> testList = (List<Object>) results[1];
			for (Object obj : testList) {// 遍历testList
				Map map = (Map) obj;
				AsyncGridModel model = new AsyncGridModel();
				model.setDataIndex("name", map.get("name"));
				model.setDataIndex("ratio", map.get("ratio"));
				model.setDataIndex("attemptNumber", map.get("attemptNumber"));
				model.setDataIndex("score", map.get("score"));
				data.add(model);
			}
			String json = JsonBuilder.builderAsyncGridJson(data, (Integer) results[0]);
			ServletActionContext.getResponse().getWriter().println(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * 导出总成绩
	 * 
	 * @return
	 */
	public String exportScore() {
		try {

			int startInt = NumberUtils.toInt(start);// null和异常返回默认值
			Object[] results = statisticsService.getScoreByCondition(getScoreConditions(), null, sort, dir);
			List<Object> testList = (List<Object>) results[1];
			
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("总成绩"); 
			sheet.setColumnWidth(0, 6000); 
			sheet.setColumnWidth(1, 6000);
			sheet.setColumnWidth(2, 6000);
			sheet.setColumnWidth(3, 6000);
			XSSFRow row = sheet.createRow((int) 0);

			XSSFCellStyle style = workbook.createCellStyle();
			style.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
			style.setFillForegroundColor((short) 13);// 设置背景色
			style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
			
			style.setBorderBottom(XSSFCellStyle.BORDER_THIN); //下边框
			style.setBorderLeft(XSSFCellStyle.BORDER_THIN);//左边框
			style.setBorderTop(XSSFCellStyle.BORDER_THIN);//上边框
			style.setBorderRight(XSSFCellStyle.BORDER_THIN);//右边框
			
			int i = 0;
			XSSFCell cell = row.createCell(0);
			cell.setCellValue("学号");
			cell.setCellStyle(style);
			cell = row.createCell(1);
			cell.setCellValue("姓名");
			cell.setCellStyle(style);
			cell = row.createCell(2);
			cell.setCellValue("专业");
			cell.setCellStyle(style);
			cell = row.createCell(3);
			cell.setCellValue("班级");
			cell.setCellStyle(style);
			cell = row.createCell(4);
			cell.setCellValue("性别");
			cell.setCellStyle(style);
			cell = row.createCell(5);
			cell.setCellValue("成绩");
			cell.setCellStyle(style);

			for (Object obj : testList) {// 遍历testList
				Map map = (Map) obj;
				row = sheet.createRow(++i);
				row.createCell(0).setCellValue((String) map.get("stuNum"));
				row.createCell(1).setCellValue((String) map.get("stuName"));
				row.createCell(2).setCellValue((String) map.get("speName"));
				row.createCell(3).setCellValue((String) map.get("className"));
				row.createCell(4).setCellValue(this.getSexFormat((String) map.get("sex")));
				row.createCell(5).setCellValue(map.get("score")!=null ? (Float) map.get("score") : new Long(0));
			}
			String fileName = "总成绩.xlsx";
			HttpServletResponse response = ServletActionContext.getResponse();
			fileName = new String(fileName.getBytes(), "ISO8859-1");
			response.setContentType("application/octet-stream;charset=ISO8859-1");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			response.addHeader("Pargam", "no-cache");
			response.addHeader("Cache-Control", "no-cache");
			workbook.write(response.getOutputStream());
			response.getOutputStream().flush();
			response.getOutputStream().close();  
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}

		return null;
	}
	
	private String getSexFormat(String sex){
		if ("F".equals(sex)) {
			sex = "女";
		} else if ("M".equals(sex)) {
			sex = "男";
		} else {
			sex = "";
		}
		return sex;
	}
	
	private String getTimestampFormat(Timestamp t){
		String date = null; 
		if(t!=null){
			date = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss").format(new Date(t.getTime()));	
		}
		return date;
	}

	public SiteService getSiteService() {
		return siteService;
	}

	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	public ToolManager getToolManager() {
		return toolManager;
	}

	public void setToolManager(ToolManager toolManager) {
		this.toolManager = toolManager;
	}

	public IStatisticsService getStatisticsService() {
		return statisticsService;
	}

	public void setStatisticsService(IStatisticsService statisticsService) {
		this.statisticsService = statisticsService;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getStuName() {
		return stuName;
	}

	public void setStuName(String stuName) {
		this.stuName = stuName;
	}

	public String getStuNum() {
		return stuNum;
	}

	public void setStuNum(String stuNum) {
		this.stuNum = stuNum;
	}

	public String getStudyStartTime() {
		return studyStartTime;
	}

	public void setStudyStartTime(String studyStartTime) {
		this.studyStartTime = studyStartTime;
	}

	public String getStudyEndTime() {
		return studyEndTime;
	}

	public void setStudyEndTime(String studyEndTime) {
		this.studyEndTime = studyEndTime;
	}

	public Long getStudyrecordId() {
		return studyrecordId;
	}

	public void setStudyrecordId(Long studyrecordId) {
		this.studyrecordId = studyrecordId;
	}

	public Long getScoreStart() {
		return scoreStart;
	}

	public void setScoreStart(Long scoreStart) {
		this.scoreStart = scoreStart;
	}

	public Long getScoreEnd() {
		return scoreEnd;
	}

	public void setScoreEnd(Long scoreEnd) {
		this.scoreEnd = scoreEnd;
	}

	public String getTestId() {
		return testId;
	}

	public void setTestId(String testId) {
		this.testId = testId;
	}

	public String getTestStartTime() {
		return testStartTime;
	}

	public void setTestStartTime(String testStartTime) {
		this.testStartTime = testStartTime;
	}

	public String getTestEndTime() {
		return testEndTime;
	}

	public void setTestEndTime(String testEndTime) {
		this.testEndTime = testEndTime;
	}

	public ICourseService getCourseService() {
		return courseService;
	}

	public void setCourseService(ICourseService courseService) {
		this.courseService = courseService;
	}

	public Long getTestrecordId() {
		return testrecordId;
	}

	public void setTestrecordId(Long testrecordId) {
		this.testrecordId = testrecordId;
	}

}
