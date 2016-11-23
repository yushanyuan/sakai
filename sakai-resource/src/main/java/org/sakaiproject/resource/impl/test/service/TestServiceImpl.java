package org.sakaiproject.resource.impl.test.service;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.sakaiproject.resource.api.course.model.MeleteSectionModel;
import org.sakaiproject.resource.api.course.model.MeleteSelfTestModel;
import org.sakaiproject.resource.api.course.model.MeleteTestModel;
import org.sakaiproject.resource.api.study.model.MeleteSelftestAttemptModel;
import org.sakaiproject.resource.api.study.model.MeleteSelftestRecordModel;
import org.sakaiproject.resource.api.study.model.MeleteTestAttemptModel;
import org.sakaiproject.resource.api.study.model.MeleteTestRecordModel;
import org.sakaiproject.resource.api.test.model.Test;
import org.sakaiproject.resource.api.test.model.TestRecord;
import org.sakaiproject.resource.api.test.service.ITestService;
import org.sakaiproject.resource.util.CacheElement;
import org.sakaiproject.resource.util.CodeTable;
import org.sakaiproject.resource.util.Constants;
import org.sakaiproject.resource.util.HibernateDaoSupport;
import org.sakaiproject.resource.util.QueryString;

import com.bupticet.paperadmin.tool.Helper;
import com.bupticet.paperadmin.tool.PaperCheckToolUtil;
import com.bupticet.paperadmin.tool.PaperToolUtil;

public class TestServiceImpl extends HibernateDaoSupport implements ITestService {
	public List<Test> getTestListBySiteId(String siteId,String userType)throws Exception{
		try{
			String hql = null;
			if(userType.equals("teacher")){
				hql = "from Test where status in ('1','2','3') and siteId=?";
			}else{
				hql = "from Test where status='2' and siteId=?";
			}
			List<Test> list = this.getHibernateTemplate().find(hql, siteId);
			return list;
		}catch(Exception e ){
			e.printStackTrace();
			throw e;
		}
	}
	
	public Object addModel(Object entity)throws Exception{
		try{
			Object id = this.getHibernateTemplate().save(entity);
			return id;
		}catch(Exception e ){
			e.printStackTrace();
			throw e;
		}
	}
	
	public Object getModel(Class clazz,Serializable id)throws Exception{
		try{
			Object model = this.getHibernateTemplate().get(clazz, id);
			return model;
		}catch(Exception e ){
			e.printStackTrace();
			throw e;
		}
	}
	
	public void updateModel(Object model)throws Exception{
		try{
			this.getHibernateTemplate().update(model);
		}catch(Exception e ){
			e.printStackTrace();
			throw e;
		}
	}
	
	public void deleteTest(String[] ids)throws Exception{
		try{
			int len = ids.length;
			Object[] param =new  Object[ids.length];
			String hql = "update Test set status='0' where testId in (";
			for(int i=0;i<len;i++){
				param[i] = Integer.parseInt(ids[i]);
				hql += " ? ";
				if(i!=len-1){
					hql += ",";
				}
			}
			hql += ")";
			this.getHibernateTemplate().bulkUpdate(hql, param);
		}catch(Exception e ){
			e.printStackTrace();
			throw e;
		}
	}
	
	public void updateTestStatus(String testId,String status)throws Exception{
		try{
			String hql = "update Test set status=? where testId=?";
			Object[] param = {status, Integer.parseInt(testId)};
			this.getHibernateTemplate().bulkUpdate(hql, param);
		}catch(Exception e ){
			e.printStackTrace();
			throw e;
		}
	}
	
	public TestRecord saveTestRecord(String siteId,  String userId, String paperId,
			String answer, String startTime, String masteryScore, String testId,String totalScore) throws Exception {
		try {
			TestRecord record = new TestRecord();
			record.setCheckStatus("0");
			record.setEndTime(new Date());
			record.setSiteId(siteId);
			Date startDate = new Date(Long.valueOf(startTime).longValue());
			record.setStartTime(startDate);
			record.setStudentId(userId);
			record.setTestId(Integer.valueOf(testId).intValue());
			record.setTestPaperid(paperId);
			record.setObjScore(0);
			record.setScore(0);
			record.setSubScore(0);
			record.setStatus("0");
			Object recordIdo = this.addModel(record);
			int recordId = Integer.parseInt(""+recordIdo);
		
			String path = Constants.getStuTestPath(userId, siteId);
			String testAnsFilename = Helper.getStuAnswerName(paperId, ""+recordId);
			File filepath = new File(path);
			if (!filepath.exists()) {
				filepath.mkdirs();
			}
			filepath = null;
			File ansF = new File(path, testAnsFilename);
			FileUtils.writeStringToFile(ansF, answer, "UTF8");
			PaperCheckToolUtil.checkPaperAction(new File(path + Helper.getAnswerNameById(paperId)), ansF);// 判卷
			float objScore = PaperCheckToolUtil.findObjStudentScore(ansF);//客观题分数			
			// 将得分转换为百分制分数
			BigDecimal percentageObjScore = new BigDecimal(objScore).divide(
					new BigDecimal(totalScore), 2, BigDecimal.ROUND_CEILING).multiply(new BigDecimal("100"));
			// 客观题分 存储的分数为百分制分数
			record.setObjScore(Float.parseFloat(percentageObjScore.toString()));
			// 总分 存储的分数为百分制分数
			record.setScore(Float.parseFloat(percentageObjScore.toString()));
			// 判断是否有主观题,是为T否为F
			if (PaperToolUtil.isExistObject(new File(path + Helper.getAnswerNameById(paperId)))) {
				record.setCheckStatus("1"); // 主观题未批改
			} else {
				record.setCheckStatus("0"); // 无主观题
				BigDecimal passScore = new BigDecimal(totalScore).multiply(new BigDecimal(masteryScore)).multiply(new BigDecimal("0.01"));
				if(objScore >= passScore.floatValue()){
					record.setStatus("1");
				}
			}
			this.updateModel(record);// 更新记录

			return record;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public Object[] findTestRecordList(String testId, String checkStatus, String endTime, int start)
			throws Exception {
		Object[] results = null;
		List<Object> resultList = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = sdf.parse(endTime);
		Map parameters = new HashMap();
		parameters.put("testId", Integer.parseInt(testId));
		parameters.put("checkStatus", checkStatus);
		parameters.put("endTime", date);
		QueryString hql = new QueryString();
	
		hql.setSelect("select new Map(sakaiuser.userId as userid,sakaiuser.firstName as studentName, sakaiuser.publicstunum as stuNum,"
						+ "sakaiuser.organizationName as eduCenter,"
						+ "testrecord.score as score,"
						+ "testrecord.checkStatus as checkStatus,"
						+ "testrecord.testrecordId as id,"
						+ "testrecord.testPaperid as paperid ) ");
		hql.setFrom("from MeleteSakaiUserModel sakaiuser, TestRecord testrecord ");
		hql.setWhere("where sakaiuser.userId=testrecord.studentId and testrecord.testId=:testId "
						+ "and testrecord.checkStatus=:checkStatus and testrecord.endTime<=:endTime");
		results = this.findEntity(hql, parameters, LIMIT, start);		
		
		return results;
	}
	
	public TestRecord checkTestSave(String recordId, String userid, File studentAnswerFile, String siteId
		) throws Exception {
		try {
			TestRecord record = (TestRecord)this.getModel(TestRecord.class, Integer.parseInt(recordId));
			// 通过测试记录获得作业信息ID
			int testId = record.getTestId();
			// 通过作业信息ID获得作业信息
			Test test = (Test) this.getModel(Test.class,testId);
			// 获得作业信息中的卷面总分
			int totalScore = test.getTotalScore();
			int masterySore = test.getMasteryScore();
			// 根据学生答案文件获得主观题分数
			float subscore = PaperCheckToolUtil.findSubStudentScore(studentAnswerFile);
			// 将主观题得分换算为百分制得分
			BigDecimal percentageSubScore = new BigDecimal(subscore).divide(
					new BigDecimal(totalScore), 2, BigDecimal.ROUND_CEILING).multiply(
					new BigDecimal("100"));
			// 存储作业主观题得分按百分制存储
			record.setSubScore(percentageSubScore.floatValue());
			float score = record.getObjScore() + percentageSubScore.floatValue();
			// 存储作业总分按百分制存储
			record.setScore(score);
			// 批改状态设为主观题已批改
			record.setCheckStatus("2");
			BigDecimal passScore = new BigDecimal(totalScore).multiply(new BigDecimal(masterySore)).multiply(new BigDecimal("0.01"));
			if(score >= passScore.floatValue()){
				record.setStatus("1");
			}
			
			this.updateEntity(record);// 更新记录
			return record;
		

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
