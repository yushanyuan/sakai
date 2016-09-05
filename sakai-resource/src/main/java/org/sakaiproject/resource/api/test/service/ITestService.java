package org.sakaiproject.resource.api.test.service;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import org.sakaiproject.resource.api.test.model.Test;
import org.sakaiproject.resource.api.test.model.TestRecord;

public interface ITestService {

	/**
	 * 根据站点id查询作业集合
	 * @param siteId 站点id
	 * @return
	 * @throws Exception
	 */
	public List<Test> getTestListBySiteId(String siteId,String userType)throws Exception;
	
	public Object addModel(Object entity)throws Exception;
	
	public Object getModel(Class clazz,Serializable id)throws Exception;
	
	public void updateModel(Object model)throws Exception;
	
	public void deleteTest(String[] ids)throws Exception;
	
	public void updateTestStatus(String testId,String status)throws Exception;
	
	public TestRecord saveTestRecord(String siteId,  String userId, String paperId,
			String answer, String startTime, String masteryScore, String testId,String totalScore) throws Exception ;
	
	public Object[] findTestRecordList(String testId, String checkStatus, String endTime, int start)
	throws Exception;
	
	public TestRecord checkTestSave(String recordId, String userid, File studentAnswerFile, String siteId
	) throws Exception;
}
