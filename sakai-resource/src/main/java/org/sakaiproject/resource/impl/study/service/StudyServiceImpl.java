package org.sakaiproject.resource.impl.study.service;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.sakaiproject.resource.api.course.model.MeleteForumModel;
import org.sakaiproject.resource.api.course.model.MeleteModuleModel;
import org.sakaiproject.resource.api.course.model.MeleteSectionModel;
import org.sakaiproject.resource.api.course.model.MeleteSelfTestModel;
import org.sakaiproject.resource.api.course.model.MeleteTestModel;
import org.sakaiproject.resource.api.study.model.MeleteForumRecordModel;
import org.sakaiproject.resource.api.study.model.MeleteModuleRecordModel;
import org.sakaiproject.resource.api.study.model.MeleteSectionRecordActivityModel;
import org.sakaiproject.resource.api.study.model.MeleteSectionRecordDetailModel;
import org.sakaiproject.resource.api.study.model.MeleteSectionRecordModel;
import org.sakaiproject.resource.api.study.model.MeleteSelftestAttemptModel;
import org.sakaiproject.resource.api.study.model.MeleteSelftestRecordModel;
import org.sakaiproject.resource.api.study.model.MeleteStudyHistoryRecordModel;
import org.sakaiproject.resource.api.study.model.MeleteStudyRecordModel;
import org.sakaiproject.resource.api.study.model.MeleteTestAttemptModel;
import org.sakaiproject.resource.api.study.model.MeleteTestRecordModel;
import org.sakaiproject.resource.api.study.service.IStudyService;
import org.sakaiproject.resource.util.CacheElement;
import org.sakaiproject.resource.util.CacheUtil;
import org.sakaiproject.resource.util.CodeTable;
import org.sakaiproject.resource.util.Constants;
import org.sakaiproject.resource.util.HibernateDaoSupport;
import org.sakaiproject.resource.util.QueryString;

import com.bupticet.paperadmin.tool.Helper;
import com.bupticet.paperadmin.tool.PaperCheckToolUtil;
import com.bupticet.paperadmin.tool.PaperToolUtil;

public class StudyServiceImpl extends HibernateDaoSupport implements IStudyService {

	/**
	 * 
	 * @see org.sakaiproject.resource.api.study.service.IStudyService#getModuleRecordByModuleId(java.lang.String)
	 */

	// 根据学习记录Id和模块Id查询是否有对应的模块记录信息
	public MeleteModuleRecordModel getModuleRecordById(String studyrecordId, String moduleId) throws Exception {
		try {
			String hql = "from MeleteModuleRecordModel where studyrecordId =? and moduleId = ?";
			Object[] parameters = { Long.valueOf(studyrecordId), Long.valueOf(moduleId) };
			List<MeleteModuleRecordModel> moduleRecords = this.findEntity(hql, parameters);
			if (moduleRecords != null && moduleRecords.size() > 0) {
				return moduleRecords.get(0);
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 
	 * @see org.sakaiproject.resource.api.study.service.IStudyService#getSectionRecordBySectionId(java.lang.String)
	 */
	// 根据学习记录Id和节点Id查询是否有对应的节点记录信息
	public MeleteSectionRecordModel getSectionRecordById(String studyrecordId, String sectionId) throws Exception {
		try {
			String hql = "from MeleteSectionRecordModel where studyrecordId =? and sectionId = ?";
			Object[] parameters = { Long.valueOf(studyrecordId), Long.valueOf(sectionId) };
			List<MeleteSectionRecordModel> sectionRecords = this.findEntity(hql, parameters);
			if (sectionRecords != null && sectionRecords.size() > 0) {
				return sectionRecords.get(0);
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 
	 * @see org.sakaiproject.resource.api.study.service.IStudyService#saveForumRecord(org.sakaiproject.resource.api.study.model.MeleteForumRecordModel)
	 */
	// 保存讨论记录信息
	public void saveForumRecord(MeleteForumRecordModel forumRecord) throws Exception {
		try {
			this.createEntity(forumRecord);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 
	 * @see org.sakaiproject.resource.api.study.service.IStudyService#saveModuleRecord(org.sakaiproject.resource.api.study.model.MeleteModuleRecordModel)
	 */
	// 保存模块记录信息
	public void saveModuleRecord(MeleteModuleRecordModel moduleRecord) throws Exception {
		try {
			this.createEntity(moduleRecord);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 
	 * @see org.sakaiproject.resource.api.study.service.IStudyService#saveSectionRecord(org.sakaiproject.resource.api.study.model.MeleteSectionRecordModel)
	 */
	// 保存节点记录信息
	public void saveSectionRecord(MeleteSectionRecordModel sectionRecord) throws Exception {
		try {
			this.createEntity(sectionRecord);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 
	 * @see org.sakaiproject.resource.api.study.service.IStudyService#saveSelftestAttempt(org.sakaiproject.resource.api.study.model.MeleteSelftestAttemptModel)
	 */
	// 保存前测尝试记录信息
	public void saveSelftestAttempt(MeleteSelftestAttemptModel selftestAttempt) throws Exception {
		try {
			this.createEntity(selftestAttempt);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 
	 * @see org.sakaiproject.resource.api.study.service.IStudyService#saveSelftestRecord(org.sakaiproject.resource.api.study.model.MeleteSelftestRecordModel)
	 */
	// 保存前测记录信息
	public void saveSelftestRecord(MeleteSelftestRecordModel selftestRecord) throws Exception {
		try {
			this.createEntity(selftestRecord);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 
	 * @see org.sakaiproject.resource.api.study.service.IStudyService#saveStudyRecord(org.sakaiproject.resource.api.study.model.MeleteStudyRecordModel)
	 */
	// 保存学习记录信息
	public String saveStudyRecord(MeleteStudyRecordModel studyRecord) throws Exception {
		try {
			return this.createEntity(studyRecord);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 
	 * @see org.sakaiproject.resource.api.study.service.IStudyService#saveTestAttempt(org.sakaiproject.resource.api.study.model.MeleteTestAttemptModel)
	 */
	// 保存作业尝试记录信息
	public void saveTestAttempt(MeleteTestAttemptModel testAttempt) throws Exception {
		try {
			this.createEntity(testAttempt);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 
	 * @see org.sakaiproject.resource.api.study.service.IStudyService#saveTestRecord(org.sakaiproject.resource.api.study.model.MeleteTestRecordModel)
	 */
	// 保存作业记录信息
	public void saveTestRecord(MeleteTestRecordModel testRecord) throws Exception {
		try {
			this.createEntity(testRecord);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	// 根据Id获取学习记录信息
	public MeleteStudyRecordModel getStudyRecordById(String studyRecordId) {
		try {
			if(StringUtils.isBlank(studyRecordId)){
				return null;
			}
			Long srId = Long.parseLong(studyRecordId);
			String hql = "from MeleteStudyRecordModel where studyrecordId=?";
			Object[] parameters = { srId };
			List<MeleteStudyRecordModel> list = this.findEntity(hql, parameters);
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public MeleteStudyRecordModel getStudyRecordById(String courseId, String studentId) throws Exception {
		try {
			String hql = "from MeleteStudyRecordModel where courseId=? and studentId=?";
			Object[] parameters = { courseId, studentId };
			List<MeleteStudyRecordModel> list = this.findEntity(hql, parameters);
			if (list != null && list.size() > 0) {
				return list.get(0);
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}
	
	/**
	 * 根据条件获取学习记录信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public Object[] getStudyRecordByCondtion(String courseId,Date startTime,Date endTime,String stuNum,String stuName,Integer start,String sort,String dir) throws Exception{
		Object[] results = null;
		List<Object> resultList = null;
		
		Map parameters = new HashMap();
		parameters.put("courseId", courseId);
		parameters.put("lessonStatus", Long.valueOf(CodeTable.passStatusNo));
		QueryString hql = new QueryString();
		// 如果是作业
		hql.setSelect("select new Map(sakaiuser.userId as userid,sakaiuser.firstName as studentName, sakaiuser.publicstunum as stuNum,"
				+ "sakaiuser.organizationName as eduCenter,studyrecord.courseId as courseId,studyrecord.score as score," +
				" studyrecord.startStudyTime as startStudyTime,studyrecord.scoreUpdateTime as scoreUpdateTime," +
				" studyrecord.studyrecordId as studyrecordId) ");
		hql.setFrom("from MeleteSakaiUserModel sakaiuser, MeleteStudyRecordModel studyrecord ");
		StringBuffer sb = new StringBuffer();
		sb.append("where sakaiuser.userId=studyrecord.studentId ");
		sb.append(" and studyrecord.courseId=:courseId and studyrecord.lessonStatus=:lessonStatus ");
		if(startTime != null){
			sb.append(" and studyrecord.scoreUpdateTime >= :startTime ");
			parameters.put("startTime", startTime);
		}
		
		if(endTime != null){
			sb.append(" and studyrecord.scoreUpdateTime <= :endTime");
			parameters.put("endTime", endTime);
		}
		
		if(StringUtils.isNotBlank(stuNum)){
			sb.append(" and sakaiuser.publicstunum like :stuNum ");
			parameters.put("stuNum", "%"+stuNum.trim()+"%");
		}
		
		if(StringUtils.isNotBlank(stuName)){
			sb.append(" and sakaiuser.firstName like :stuName ");
			parameters.put("stuName", "%"+stuName.trim()+"%");
		}
		
		if(StringUtils.isNotBlank(sort) && StringUtils.isNotBlank(dir)){
			if(sort.equals("studentName")){
				sort = "sakaiuser.firstName";
			}
			if(sort.equals("stuNum")){
				sort = "sakaiuser.publicstunum";
			}
			if(sort.equals("eduCenter")){
				sort = "sakaiuser.organizationName";
			}
			if(sort.equals("score")){
				sort = "studyrecord.score";
			}
			if(sort.equals("scoreUpdateTime")){
				sort = "studyrecord.scoreUpdateTime";
			}
			sb.append(" order by " + sort + " " + dir);
		}
		hql.setWhere(sb.toString());//
		if(start == null){
			Object[] obj = new Object[2];
			List list = this.findEntity(hql.getSql(), parameters);
			obj[0] = list.size();
			obj[1] = list; 
			results= obj;
		}else{
			results = this.findEntity(hql, parameters, LIMIT, start);// 分页查询，返回一个对象数组
		}
		
        return results;
	}

	/**
	 * 
	 * @see org.sakaiproject.resource.api.study.service.IStudyService#saveSectionRecordDetail(org.sakaiproject.resource.api.study.model.MeleteSectionRecordDetailModel)
	 */
	// 保存节点记录详细信息
	public void saveSectionRecordDetail(MeleteSectionRecordDetailModel sectionDetail) throws Exception {
		try {
			this.createEntity(sectionDetail);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 
	 * @see org.sakaiproject.resource.api.study.service.IStudyService#updateModel(java.lang.Object)
	 */
	// 升级修改对象模块
	public void updateModel(Object o) throws Exception {
		try {
			this.updateEntity(o);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 
	 * @see org.sakaiproject.resource.api.study.service.IStudyService#getSectionRecordDetails(java.lang.String)
	 */
	// 根据节点学习记录获取节点学习详细记录信息列表
	public List<MeleteSectionRecordDetailModel> getSectionRecordDetailList(String sectionrecordId) throws Exception {
		try {
			String hql = "from MeleteSectionRecordDetailModel where sectionrecordId=?";
			Object[] parameters = { Long.valueOf(sectionrecordId) };
			List<MeleteSectionRecordDetailModel> list = this.findEntity(hql, parameters);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 
	 * @see org.sakaiproject.resource.api.study.service.IStudyService#getSectionRecordDetailById(java.lang.String)
	 */
	// 根据主键Id获取节点记录明细对象
	public MeleteSectionRecordDetailModel getSectionRecordDetailById(String detailRecordId) throws Exception {
		return (MeleteSectionRecordDetailModel) this.getHibernateTemplate().get(MeleteSectionRecordDetailModel.class,
				Long.valueOf(detailRecordId));
	}

	/**
	 * 
	 * @see org.sakaiproject.resource.api.study.service.IStudyService#getSectionRecordById(java.lang.String)
	 */
	// 根据主键Id获取节点记录对象
	public MeleteSectionRecordModel getSectionRecordById(Long sectionRecordId) throws Exception {
		return (MeleteSectionRecordModel) this.getHibernateTemplate().get(MeleteSectionRecordModel.class,
				sectionRecordId);
	}

	/**
	 * 
	 * @see org.sakaiproject.resource.api.study.service.IStudyService#getModuleRecordById(java.lang.Long)
	 */
	// 根据主键Id获取模块记录对象
	public MeleteModuleRecordModel getModuleRecordById(Long moduleRecordId) throws Exception {
		return (MeleteModuleRecordModel) this.getHibernateTemplate().get(MeleteModuleRecordModel.class, moduleRecordId);
	}

	/**
	 * 
	 * @see org.sakaiproject.resource.api.study.service.IStudyService#getForumRecordById(java.lang.String,
	 *      java.lang.String)
	 */
	// 根据学习记录Id和讨论Id查询讨论记录信息
	public MeleteForumRecordModel getForumRecordById(String studyrecordId, String forumId) throws Exception {
		try {
			String hql = "from MeleteForumRecordModel where studyrecordId =? and forumId = ?";
			Object[] parameters = { Long.valueOf(studyrecordId), Long.valueOf(forumId) };
			List<MeleteForumRecordModel> forumRecords = this.findEntity(hql, parameters);
			if (forumRecords != null && forumRecords.size() > 0) {
				return forumRecords.get(0);
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 
	 * @see org.sakaiproject.resource.api.study.service.IStudyService#getSelftestRecordById(java.lang.String,
	 *      java.lang.String)
	 */
	// 根据学习记录Id和前测Id查询讨论记录信息
	public MeleteSelftestRecordModel getSelftestRecordById(String studyrecordId, String selftestId) throws Exception {
		try {
			String hql = "from MeleteSelftestRecordModel where studyrecordId =? and selftestId = ?";
			Object[] parameters = { Long.valueOf(studyrecordId), Long.valueOf(selftestId) };
			List<MeleteSelftestRecordModel> selftestRecords = this.findEntity(hql, parameters);
			if (selftestRecords != null && selftestRecords.size() > 0) {
				return selftestRecords.get(0);
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 
	 * @see org.sakaiproject.resource.api.study.service.IStudyService#getTestRecordById(java.lang.String,
	 *      java.lang.String)
	 */
	// 根据学习记录Id和作业Id查询讨论记录信息
	public MeleteTestRecordModel getTestRecordById(String studyrecordId, String testId) throws Exception {
		try {
			String hql = "from MeleteTestRecordModel where studyrecordId =? and testId = ?";
			Object[] parameters = { Long.valueOf(studyrecordId), Long.valueOf(testId) };
			List<MeleteTestRecordModel> testRecords = this.findEntity(hql, parameters);
			if (testRecords != null && testRecords.size() > 0) {
				return testRecords.get(0);
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sakaiproject.resource.api.study.service.IStudyService#SaveTestAttempt
	 * (java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	// 保存提交的作业
	public MeleteTestAttemptModel saveTestAttempt(String courseId, String recordId, String userId, String paperId,
			String answer, String startTime, String passScore, String studyrecordId, String testId) throws Exception {
		try {
			CacheElement ce = CacheUtil.getInstance().getCacheOfStudyrecord(studyrecordId);
			CacheElement cacheCourse = CacheUtil.getInstance().getCacheOfCourse(courseId);

			boolean checkUsuallyScore = false;
			MeleteTestRecordModel record = ce.getTestRecord(Long.valueOf(testId));// 从缓存中获得作业记录

			MeleteTestModel testModel = cacheCourse.getTest(Long.valueOf(testId));// 从缓存中获得作业

			Long totalScore = testModel.getTotalScore();
			Long num = record.getAttemptNumber();// 获取尝试次数
			if (num == null) {
				num = new Long(0);
			}
			Date startDate = new Date(Long.valueOf(startTime).longValue());
			Date entTime = new Date();

			MeleteTestAttemptModel attempt = new MeleteTestAttemptModel();// 创建作业尝试记录
			attempt.setEndTime(entTime);
			attempt.setCourseId(courseId);
			attempt.setMeleteTestRecordId(new Long(recordId));
			attempt.setStartTime(startDate);
			attempt.setTestPaperid(paperId);
			attempt.setObjScore(new Float(0));
			attempt.setScore(new Float(0));
			attempt.setSubScore(new Float(0));
			attempt.setOrderIndex(num + 1);
			String attemptId = this.createEntity(attempt);// 保存尝试记录生成id

			String path = Constants.getStuTestPath(userId, courseId);
			String testAnsFilename = Helper.getStuAnswerName(paperId, attemptId);
			File filepath = new File(path);
			if (!filepath.exists()) {
				filepath.mkdirs();
			}
			filepath = null;
			File ansF = new File(path, testAnsFilename);
			FileUtils.writeStringToFile(ansF, answer, "UTF8");
			PaperCheckToolUtil.checkPaperAction(new File(path + Helper.getAnswerNameById(paperId)), ansF);// 判卷
			Float objScore = new Float(PaperCheckToolUtil.findObjStudentScore(ansF));
			
			// 2016.10.28修改存储的分数为实际分数（保证和试卷分数一致），更新平时成绩时换算为100分制
			// 重新计算通过分数,目前传入的passScore其实是通过百分比
			BigDecimal total = new BigDecimal(totalScore);
			BigDecimal mastery = new BigDecimal(passScore);
			BigDecimal passScoreBD = total.multiply(mastery.divide(new BigDecimal("100")));
			passScore = passScoreBD.toString();
//			BigDecimal percentageObjScore = new BigDecimal(objScore.toString()).divide(
//					new BigDecimal(totalScore.toString()), 2, BigDecimal.ROUND_CEILING).multiply(new BigDecimal("100"));
			
			
			// 客观题分 存储的分数为百分制分数
			//attempt.setObjScore(percentageObjScore.floatValue());
			// 2016.10.28修改存储的分数为实际分数（保证和试卷分数一致），更新平时成绩时换算为100分制
			attempt.setObjScore(objScore);
			// 总分 存储的分数为百分制分数
			//ttempt.setScore(percentageObjScore.floatValue());
			// 2016.10.28修改存储的分数为实际分数（保证试卷分数一致），更新平时成绩时换算为100分制
			attempt.setScore(objScore);
			
			
			
			// 判断是否有主观题,是为T否为F
			if (PaperToolUtil.isExistObject(new File(path + Helper.getAnswerNameById(paperId)))) {
				attempt.setPagerstatus(CodeTable.subCheckNo);// 主观题未批改
			} else {
				attempt.setPagerstatus(CodeTable.subNotExist);// 无主观题
				
//				if (record.getScore() == null || (record.getScore().floatValue() < percentageObjScore.floatValue())) {// 作业分数小于尝试分数，更新作业分数
//					record.setScore(percentageObjScore.floatValue());
//					checkUsuallyScore = true;
//					// 作业状态为未通过且分数大于通过分数
//					if (record.getStatus().toString().equals(CodeTable.passStatusNo)
//							&& percentageObjScore.floatValue() >= new Float(passScore)) {
//						record.setStatus(new Long(CodeTable.passStatusYes));// 更新为已通过
//					}
//				}
				if (record.getScore() == null || (record.getScore().floatValue() < attempt.getScore().floatValue())) {// 作业分数小于尝试分数，更新作业分数
					record.setScore(attempt.getScore().floatValue());
					checkUsuallyScore = true;
					// 作业状态为未通过且分数大于通过分数
					if (record.getStatus().toString().equals(CodeTable.passStatusNo)
							&& record.getScore().floatValue() >= new Float(passScore)) {
						record.setStatus(new Long(CodeTable.passStatusYes));// 更新为已通过
					}
				}
			}
			this.updateEntity(attempt);// 更新尝试记录

			record.setAttemptNumber(num + 1);// 作业记录的尝试次数加1

			record.setLastCommitTime(entTime);
			if (record.getStartStudyTime() == null) {
				record.setStartStudyTime(startDate);
			}
			this.updateEntity(record);// 更新作业记录 数据库
			ce.editTestRecord(record);// 更新作业记录 缓存

			if (checkUsuallyScore) {
				// 计算平时成绩
				countUsuallyScore(courseId, userId, studyrecordId);
			}

			return attempt;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}
	
	// 保存提交的作业(旧课程空间)
	public MeleteTestAttemptModel saveOldMeleteTestAttempt(String courseId, String recordId, String userId, String paperId, String answer,
			String startTime, String passScore, String studyrecordId, String testId) throws Exception {
		// String courseId,
		// String studentId, String moduleId, String sectionId, String testId,
		// String paperId, boolean isFirst, Date startTime,
		// String stuAnswerString
		MeleteStudyRecordModel studyRecord = CacheUtil.getInstance().checkStudyRecord(courseId, userId);
		// MeleteCourseModel course = courseService.getCourseBySiteId(courseId);

		// 获取作业记录
		MeleteTestRecordModel testRecord = this.getTestRecordById(studyRecord.getStudyrecordId().toString(), testId);
        testRecord.setCourseId(courseId);
		Long num = testRecord.getAttemptNumber();// 获取尝试次数
		if (num == null) {
			num = new Long(0);
		}

		Date startDate = new Date(Long.valueOf(startTime).longValue());
		Date entTime = new Date();

		MeleteTestAttemptModel attempt = new MeleteTestAttemptModel();// 创建作业尝试记录
		attempt.setEndTime(entTime);
		attempt.setCourseId(courseId);
		attempt.setMeleteTestRecordId(new Long(recordId));
		attempt.setStartTime(startDate);
		attempt.setTestPaperid(paperId);
		attempt.setObjScore(new Float(0));
		attempt.setScore(new Float(0));
		attempt.setSubScore(new Float(0));
		attempt.setOrderIndex(num + 1);
		String attemptId = this.createEntity(attempt);// 保存尝试记录生成id
		if (attemptId != null && Long.parseLong(attemptId) != 0 && answer != null) {

			String path = Constants.getStuTestPath(userId, courseId);
			String testAnsFilename = Helper.getStuAnswerName(paperId, attemptId);
			File filepath = new File(path);
			if (!filepath.exists()) {
				filepath.mkdirs();
			}
			filepath = null;
			File ansF = new File(path, testAnsFilename);
			FileUtils.writeStringToFile(ansF, answer, "UTF8");
			PaperCheckToolUtil.checkPaperAction(new File(path + Helper.getAnswerNameById(paperId)), ansF);// 判卷
			attempt.setObjScore(PaperCheckToolUtil.findObjStudentScore(ansF));// 客观题分
			attempt.setScore(PaperCheckToolUtil.findObjStudentScore(ansF));// 总分
			// 判断是否有主观题,是为T否为F(0.主观题未批改 1.主观题已经批改 2.无主观题)
			if (PaperToolUtil.isExistObject(new File(path + Helper.getAnswerNameById(paperId)))) {
				attempt.setPagerstatus("0");
			} else {
				attempt.setPagerstatus("2");
			}
		}

		this.updateEntity(attempt);

		testRecord.setAttemptNumber(num + 1);
		testRecord.setLastCommitTime(entTime);
		if (testRecord.getStartStudyTime() == null) {
			testRecord.setStartStudyTime(startDate);
		}

		if (testRecord.getScore() < attempt.getScore()) {
			testRecord.setScore(attempt.getScore());
			this.updateEntity(testRecord);
			String sql = "select count from MELETE_TEST where id = :testId";
			Object o = this.getSession().createSQLQuery(sql).setParameter("testId", Long.parseLong(testId)).uniqueResult();
			if (o != null && ((Integer) o) == 1) {
				this.countOldMeleteCourseScore(courseId, userId, "");
			}
		} else {
			this.updateEntity(testRecord);
		}
		return attempt;
	}

	/**
	 * 计算课程成绩，旧课程空间（查出课程下所有的作业与测试，所有的学生完成的作业与测试的做答记录，用作业或测试的ID去匹配）
	 * 
	 * @param courseId
	 *            课程ID
	 * @param studentId
	 *            学生ID
	 */
	@SuppressWarnings("unchecked")
	public void countOldMeleteCourseScore(String courseId, String studentId,String noPassReason)  throws Exception{
		float scoreSum = 0;
		
		String moduleHomeworkSql = "select modulehome0_.HOMEWORK_ID as HOMEWORK_ID,  modulehome0_.TOTAL_SCORE as TOTAL_SCORE,  modulehome0_.RATIO as RATIO, modulehome0_.COUNT as COUNT from melete_homework modulehome0_, melete_course_module coursemodu1_ where modulehome0_.BELONG_TYPE='MODULEHOME' and coursemodu1_.COURSE_ID=:courseId and coursemodu1_.DELETE_FLAG=0 and modulehome0_.COUNT=1 and modulehome0_.MODULE_ID=coursemodu1_.MODULE_ID order by coursemodu1_.SEQ_NO, modulehome0_.NAME";
		List<Map<String,Object>> moduleHomeworkList = this.getSession().createSQLQuery(moduleHomeworkSql).setParameter("courseId", courseId).list();
		
		String  sectionHomeworkSql = "select sectionhom0_.HOMEWORK_ID as HOMEWORK_ID, sectionhom0_.TOTAL_SCORE as TOTAL_SCORE, sectionhom0_.RATIO as RATIO, sectionhom0_.COUNT as COUNT from melete_homework sectionhom0_, melete_section section1_, melete_course_module coursemodu2_ where sectionhom0_.BELONG_TYPE='SECTIONHOME' and coursemodu2_.DELETE_FLAG=0 and coursemodu2_.COURSE_ID=:courseId and sectionhom0_.COUNT=1 and sectionhom0_.SECTION_ID=section1_.SECTION_ID and section1_.MODULE_ID=coursemodu2_.MODULE_ID order by coursemodu2_.SEQ_NO, sectionhom0_.NAME";
		List<Map<String,Object>> sectionHomeworkList = this.getSession().createSQLQuery(sectionHomeworkSql).setParameter("courseId", courseId).list();
		
		String  moduleTestSql = "select moduletest0_.TEST_ID as TEST_ID, moduletest0_.TOTAL_SCORE as TOTAL_SCORE,  moduletest0_.RATIO as RATIO, moduletest0_.COUNT as COUNT from melete_test moduletest0_ left outer join melete_module module2_ on moduletest0_.MODULE_ID=module2_.MODULE_ID, melete_course_module coursemodu1_ where moduletest0_.BELONG_TYPE='MODULETEST' and coursemodu1_.DELETE_FLAG=0 and coursemodu1_.COURSE_ID=:courseId and moduletest0_.MODULE_ID=coursemodu1_.MODULE_ID and moduletest0_.COUNT=1 order by coursemodu1_.SEQ_NO, moduletest0_.NAME";
		List<Map<String,Object>> moduleTestList = this.getSession().createSQLQuery(moduleTestSql).setParameter("courseId", courseId).list();
		
		String  sectionTestSql = "select sectiontes0_.TEST_ID as TEST_ID, sectiontes0_.TOTAL_SCORE as TOTAL_SCORE, sectiontes0_.RATIO as RATIO, sectiontes0_.COUNT as COUNT from melete_test sectiontes0_ left outer join melete_section section2_ on sectiontes0_.SECTION_ID=section2_.SECTION_ID, melete_course_module coursemodu1_ where sectiontes0_.BELONG_TYPE='SECTIONTEST' and section2_.DELETE_FLAG=0 and coursemodu1_.DELETE_FLAG=0 and coursemodu1_.COURSE_ID=:courseId and section2_.MODULE_ID=coursemodu1_.MODULE_ID and section2_.SECTION_ID=sectiontes0_.SECTION_ID and sectiontes0_.COUNT=1 order by coursemodu1_.SEQ_NO, sectiontes0_.NAME";
		List<Map<String,Object>> sectionTestList = this.getSession().createSQLQuery(sectionTestSql).setParameter("courseId", courseId).list();
		
		MeleteStudyRecordModel studyRecord = CacheUtil.getInstance().checkStudyRecord(courseId, studentId);		
		
		String  homeworkRecordsSql = "select  homeworkre0_.SCORE as SCORE, homeworkre0_.HOMEWORK_NAME as HOMEWORK_NAME,homeworkre0_.HOMEWORK_ID as HOMEWORK_ID from melete_homework_record homeworkre0_ where homeworkre0_.STUDYRECORD=:studyRecord";
		List<Map<String,Object>> homeworkRecords = this.getSession().createSQLQuery(sectionTestSql).setParameter("studyRecord",studyRecord.getStudyrecordId()).list();
		
		String  testRecordsSql = "select  testrecord0_.SCORE as SCORE ,testrecord0_.TEST_NAME as TEST_NAME, testrecord0_.TEST_ID as TEST_ID from melete_test_record testrecord0_ where testrecord0_.STUDYRECORD=:studyRecord order by testrecord0_.START_STUDY_TIME";
		List<Map<String,Object>> testRecords = this.getSession().createSQLQuery(sectionTestSql).setParameter("studyRecord",studyRecord.getStudyrecordId()).list();
		
		boolean notSetRadio = true;
		int timesCount = 0;
		float simpleScoreSum = 0;
		for (Iterator iter = moduleHomeworkList.iterator(); iter.hasNext();) {
			Map<String,Object> moduleHomework = (Map<String,Object>) iter.next();
			if (((Boolean)moduleHomework.get("COUNT"))==true) {
				timesCount++;
				for (Map<String,Object> homeworkRecord : homeworkRecords) {

					if (homeworkRecord.get("HOMEWORK_ID").toString().equals(
							moduleHomework.get("HOMEWORK_ID").toString())) {
						if ((Float)moduleHomework.get("RATIO") == 0) {
							simpleScoreSum = simpleScoreSum
									+ ((Float)homeworkRecord.get("SCORE") > (Float)moduleHomework
											.get("TOTAL_SCORE") ? (Float)moduleHomework
											.get("TOTAL_SCORE") : (Float)homeworkRecord
											.get("SCORE"))
									/ (Float)moduleHomework.get("TOTAL_SCORE");
						} else {
							notSetRadio = false;
							scoreSum = scoreSum
									+ ((Float)homeworkRecord.get("SCORE") > (Float)moduleHomework
											.get("TOTAL_SCORE") ? (Float)moduleHomework
											.get("TOTAL_SCORE") : (Float)homeworkRecord
											.get("SCORE"))
									* (Float)moduleHomework.get("RATIO")
									/ (Float)moduleHomework.get("TOTAL_SCORE");
						}
					}
				}
			}
		}
		for (Iterator iter = sectionHomeworkList.iterator(); iter.hasNext();) {
			Map<String,Object> sectionHomework = (Map<String,Object>) iter.next();
			if (((Boolean)sectionHomework.get("COUNT"))==true) {
				timesCount++;
				for (Map<String,Object> homeworkRecord : homeworkRecords) {

					if (homeworkRecord.get("HOMEWORK_ID").toString().equals(
							sectionHomework.get("HOMEWORK_ID").toString())) {
						if ((Float)sectionHomework.get("RATIO") == 0) {

							simpleScoreSum = simpleScoreSum
									+ ((Float)homeworkRecord.get("SCORE") > (Float)sectionHomework
											.get("TOTAL_SCORE") ? (Float)sectionHomework
											.get("TOTAL_SCORE") : (Float)homeworkRecord
											.get("SCORE"))
									/ (Float)sectionHomework.get("TOTAL_SCORE");
						} else {
							notSetRadio = false;
							scoreSum = scoreSum
									+ ((Float)homeworkRecord.get("SCORE") > (Float)sectionHomework
											.get("TOTAL_SCORE") ? (Float)sectionHomework
											.get("TOTAL_SCORE") : (Float)homeworkRecord
											.get("SCORE"))
									* (Float)sectionHomework.get("RATIO")
									/ (Float)sectionHomework.get("TOTAL_SCORE");
						}
					}
				}
			}
		}
		for (Iterator iter = moduleTestList.iterator(); iter.hasNext();) {
			Map<String,Object> moduleTest = (Map<String,Object>) iter.next();
			if (((Boolean)moduleTest.get("COUNT"))==true) {
				timesCount++;
				for (Map<String,Object> testRecord : testRecords) {

					if (testRecord.get("TEST_ID").toString().equals(moduleTest.get("TEST_ID").toString())) {
						if ((Float)moduleTest.get("RATIO") == 0) {
							simpleScoreSum = simpleScoreSum
									+ ((Float)testRecord.get("SCORE") > (Float)moduleTest
											.get("TOTAL_SCORE") ? (Float)moduleTest
											.get("TOTAL_SCORE") : (Float)testRecord
											.get("SCORE"))
									/ (Float)moduleTest.get("TOTAL_SCORE");
						} else {
							notSetRadio = false;
							scoreSum = scoreSum
									+ ((Float)testRecord.get("SCORE") > (Float)moduleTest
											.get("TOTAL_SCORE") ? (Float)moduleTest
											.get("TOTAL_SCORE") : (Float)testRecord
											.get("SCORE"))
									* (Float)moduleTest.get("RATIO")
									/ (Float)moduleTest.get("TOTAL_SCORE");
						}
					}
				}
			}
		}
		for (Iterator iter = sectionTestList.iterator(); iter.hasNext();) {
			Map<String,Object> sectionTest = (Map<String,Object>) iter.next();
			if (((Boolean)sectionTest.get("COUNT"))==true) {
				timesCount++;
				for (Map<String,Object> testRecord : testRecords) {

					if (testRecord.get("TEST_ID").toString().equals(sectionTest.get("TEST_ID").toString())) {
						if ((Float)sectionTest.get("RATIO") == 0) {
							simpleScoreSum = simpleScoreSum
									+ ((Float)testRecord.get("SCORE") > (Float)sectionTest
											.get("TOTAL_SCORE") ? (Float)sectionTest
											.get("TOTAL_SCORE") : (Float)testRecord
											.get("SCORE"))
									/ (Float)sectionTest.get("TOTAL_SCORE");
						} else {
							notSetRadio = false;
							scoreSum = scoreSum
									+ ((Float)testRecord.get("SCORE") > (Float)sectionTest
											.get("TOTAL_SCORE") ? (Float)sectionTest
											.get("TOTAL_SCORE") : (Float)testRecord
											.get("SCORE"))
									* (Float)sectionTest.get("RATIO")
									/ (Float)sectionTest.get("TOTAL_SCORE");
						}
					}
				}
			}
		}
		if (notSetRadio) {
			scoreSum = 100 * simpleScoreSum / timesCount;
		}
		// scoreSum += 0.05;
		// DecimalFormat format = new DecimalFormat("#0.0");// 输出一位小数
		// scoreSum = Float.parseFloat(format.format(scoreSum));
		scoreSum = (float) (Math.round(scoreSum * 10) / 10.0); // 小数点后一位前移，并四舍五入
		if (scoreSum > studyRecord.getScore()) {
			if (scoreSum > 100) {
				scoreSum = 100f;
			}
			studyRecord.setScore(scoreSum);
			studyRecord.setScoreUpdateTime(new Date());
			try {
				updateEntity(studyRecord);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
		}
	}
	
	

	// 保存提交的前测
	public MeleteSelftestAttemptModel saveSelftestAttempt(String courseId, String recordId, String userId,
			String paperId, String answer, String startTime, String passScore, String studyrecordId, String selfTestId)
			throws Exception {
		try {
			CacheElement ce = CacheUtil.getInstance().getCacheOfStudyrecord(studyrecordId);
			CacheElement cacheCourse = CacheUtil.getInstance().getCacheOfCourse(courseId);

			boolean checkUsuallyScore = false;
			MeleteSelftestRecordModel record = ce.getSelftestRecord(Long.valueOf(selfTestId));// 从缓存中获得前测记录
			MeleteSelfTestModel meleteSelfTestModel = cacheCourse.getSelftest(Long.valueOf(selfTestId));// 从缓存中获得前测

			Long totalScore = meleteSelfTestModel.getTotalScore();// 前测总分用于后面的转换百分比分数计算
			Long num = record.getAttemptNumber();// 获取尝试次数
			if (num == null) {
				num = new Long(0);
			}
			Date startDate = new Date(Long.valueOf(startTime).longValue());
			Date entTime = new Date();

			MeleteSelftestAttemptModel attempt = new MeleteSelftestAttemptModel();// 创建前测尝试记录
			attempt.setEndTime(entTime);
			attempt.setCourseId(courseId);
			attempt.setMeleteSelftestRecordId(new Long(recordId));
			attempt.setStartTime(startDate);
			attempt.setSelftestPaperid(paperId);
			attempt.setObjScore(new Float(0));
			attempt.setScore(new Float(0));
			attempt.setSubScore(new Float(0));
			attempt.setOrderIndex(num + 1);
			String attemptId = this.createEntity(attempt);// 保存尝试记录生成id

			String path = Constants.getStuSelfTestPath(userId, courseId);
			String testAnsFilename = Helper.getStuAnswerName(paperId, attemptId);
			File filepath = new File(path);
			if (!filepath.exists()) {
				filepath.mkdirs();
			}
			filepath = null;
			File ansF = new File(path, testAnsFilename);
			FileUtils.writeStringToFile(ansF, answer, "UTF8");
			PaperCheckToolUtil.checkPaperAction(new File(path + Helper.getAnswerNameById(paperId)), ansF);// 判卷
			Float objScore = new Float(PaperCheckToolUtil.findObjStudentScore(ansF));
			
			
//			BigDecimal percentageObjScore = new BigDecimal(objScore.toString()).divide(
//					new BigDecimal(totalScore.toString()), 2, BigDecimal.ROUND_CEILING).multiply(new BigDecimal("100"));
			// 2016.10.28修改存储的分数为实际分数（保证和试卷分数一致），更新平时成绩时换算为100分制
			// 重新计算通过分数,目前传入的passScore其实是通过百分比
			BigDecimal total = new BigDecimal(totalScore);
			BigDecimal mastery = new BigDecimal(passScore);
			BigDecimal passScoreBD = total.multiply(mastery.divide(new BigDecimal("100")));
			passScore = passScoreBD.toString();
			
			//attempt.setObjScore(percentageObjScore.floatValue());// 客观题分
			// 存储形式为百分制分数
			//attempt.setScore(percentageObjScore.floatValue());// 总分 存储形式为百分制分数
			// 2016.10.28修改存储的分数为实际分数（保证和试卷分数一致），更新平时成绩时换算为100分制
			attempt.setObjScore(objScore.floatValue());// 客观题分
			// 存储形式为百分制分数
			attempt.setScore(objScore.floatValue());// 总分 存储形式为百分制分数
			
			// 判断是否有主观题,是为T否为F
			if (PaperToolUtil.isExistObject(new File(path + Helper.getAnswerNameById(paperId)))) {
				attempt.setPagerstatus(CodeTable.subCheckNo);// 主观题未批改
			} else {
				attempt.setPagerstatus(CodeTable.subNotExist);// 无主观题
				if (record.getScore() == null || (record.getScore().floatValue() < attempt.getScore().floatValue())) {// 作业分数小于尝试分数，更新作业分数
					record.setScore(attempt.getScore().floatValue());// 前测记录中的存储形式为百分制分数
					checkUsuallyScore = true;
					// 前测状态为未通过且分数 大于等于 通过分数
					if (record.getStatus().toString().equals(CodeTable.passStatusNo)
							&& record.getScore().floatValue() >= new Float(passScore)) {
						record.setStatus(new Long(CodeTable.passStatusYes));// 更新为已通过
					}
				}
			}
			this.updateEntity(attempt);// 更新尝试记录

			record.setAttemptNumber(num + 1);// 前测记录的尝试次数加1

			record.setLastCommitTime(entTime);
			if (record.getStartStudyTime() == null) {
				record.setStartStudyTime(startDate);
			}
			this.updateEntity(record);// 更新数据库中的前测记录
			ce.editSelftestRecord(record);// 更新缓存中的前测记录
			if (checkUsuallyScore) {
				this.countUsuallyScore(courseId, userId, studyrecordId);
			}
			return attempt;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}
	
	
	/**
	 * 批量重新计算平时成绩
	 * @param studyrecordIds
	 * @throws Exception
	 */
	public void updateScoreByStudyRecordIds(String studyrecordIds)throws Exception{
		if(StringUtils.isNotBlank(studyrecordIds)){
			String[] studyIds = studyrecordIds.split(",");
			for(String id : studyIds){
				try{
					CacheElement ce = CacheUtil.getInstance().getCacheOfStudyrecord(id);
					this.countUsuallyScore(ce.getStudyRecord().getCourseId(), ce.getStudyRecord().getStudentId(), ce.getStudyRecord().getStudyrecordId().toString());
				}catch(Exception e){
					e.printStackTrace();
					logger.error(e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * 计算平时成绩
	 * 
	 * @throws Exception
	 */
	public void countUsuallyScore(String courseId, String userId, String studyrecordId) throws Exception {
		CacheElement ce = CacheUtil.getInstance().getCacheOfStudyrecord(studyrecordId);

		List<Object[]> testlist = null;
		List<Object[]> selftestlist = null;
		List<Object[]> forumlist = null;
		List fileList = null;
		Double testSumScore = new Double(0);
		Double selfTestSumScore = new Double(0);
		Double forumSumScore = new Double(0);

		// testlist = ce.getTestAndRecordList();// 从缓存中获得作业以及作业记录的集合
		// 直接从数据库读取作业记录数据数据 zihongyan 2014-03-07
		String testhql = "select mt.ratio as ratio,mtr.score as score,mt.totalScore as totalScore from MeleteTestRecordModel mtr,MeleteTestModel mt "
				+ "where mt.id=mtr.testId and mt.isCaculateScore=? and mtr.studyrecordId=? and mt.status=?";
		Object[] testParameters = { Long.parseLong("1"), Long.parseLong(studyrecordId),
				Long.parseLong(CodeTable.normal) };
		testlist = findEntity(testhql, testParameters);
		if (testlist != null && !testlist.isEmpty() && testlist.size() > 0) {
			for (int i = 0; i < testlist.size(); i++) {
				Object[] obj = (Object[]) testlist.get(i);
				// 换算成百分制并计算成绩
				testSumScore = testSumScore.doubleValue() + (new Double((Float) obj[1]).doubleValue()/new Double((Long) obj[2]).doubleValue() * 100)
						* new Double((Float) obj[0]).doubleValue() * 0.01;
			}
		}
		logger.debug("------testSumScore--------" + testSumScore);

		// selftestlist = ce.getSelftestAndRecordList();// 从缓存中获得前测以及前测记录的集合
		// 直接从数据库读取前侧数据
		String selftesthql = "select ms.ratio as ratio,msr.score as score,ms.totalScore as totalScore from MeleteSelftestRecordModel msr,MeleteSelfTestModel ms "
				+ "where ms.id=msr.selftestId and ms.isCaculateScore=? and msr.studyrecordId=?";
		Object[] selftestParameters = { Long.parseLong("1"), Long.parseLong(studyrecordId) };
		selftestlist = findEntity(selftesthql, selftestParameters);
		if (selftestlist != null && !selftestlist.isEmpty() && selftestlist.size() > 0) {
			for (int i = 0; i < selftestlist.size(); i++) {
				Object[] obj = (Object[]) selftestlist.get(i);
				selfTestSumScore = selfTestSumScore.doubleValue() + (new Double((Float) obj[1]).doubleValue()/new Double((Long) obj[2]).doubleValue() * 100)
						* new Double((Float) obj[0]).doubleValue() * 0.01;
			}
		}

		logger.debug("------selfTestSumScore--------" + selfTestSumScore);
		// forumlist = ce.getForumAndRecordList();// 从缓存中获得讨论以及讨论记录的集合 

		// 从数据库直接读取讨论以及讨论记录的集合
		String forumhql = "select mf.ratio from MeleteForumRecordModel mfr,MeleteForumModel mf "
				+ "where mf.id=mfr.forumId and mf.isCaculateScore=? and mfr.studyrecordId=? and mfr.status=?";
		Object[] forumParameters = { Long.parseLong("1"), Long.parseLong(studyrecordId),
				Long.parseLong(CodeTable.normal) };
		forumlist = findEntity(forumhql, forumParameters);

		if (forumlist != null && !forumlist.isEmpty() && forumlist.size() > 0) {
			for (int i = 0; i < forumlist.size(); i++) {
				Object[] obj = (Object[]) selftestlist.get(i);
				forumSumScore = forumSumScore + new Double((Float) obj[0]).doubleValue();
			}
		}
		logger.debug("------forumSumScore--------" + forumSumScore);
		String filehql = "select avg(fileModel.ratio) from ResourceDownloadModel downloadModel, ResourceFileModel fileModel, ResourceFolderModel folderModel "
				+ "where downloadModel.fileId=fileModel.id and fileModel.folderId=folderModel.id "
				+ "and folderModel.siteId=? and downloadModel.downloader=? and fileModel.extendOption=? group by downloadModel.fileId,downloadModel.downloader";
		String siteId = this.getSiteIdByCourseId(courseId);
		Object[] fileParameters = { siteId, userId, new Integer(CodeTable.IsCaculateScoreYes) };
		fileList = findEntity(filehql, fileParameters);

		Double testScore = testSumScore;
		Double selftestScore = selfTestSumScore;
		Double forumScore = forumSumScore;
		Double fileScore = new Double(0);
		try {
			for (int i = 0; i < fileList.size(); i++) {
				fileScore += (Double) fileList.get(i);
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			logger.error(e1.getMessage(), e1);
		}
		logger.debug("------fileScore--------" + fileScore); 
		Double sumScore = testScore + selftestScore + forumScore + fileScore;
		Float sumScoref = new Float(sumScore);
		try {
			MeleteStudyRecordModel studyRecord = this.getStudyRecordById(courseId, userId);
			if (studyRecord != null && studyRecord.getScore() < sumScoref) {
			//if(studyRecord != null){
				// 增加更新时间 2014-7-31
				studyRecord.setScoreUpdateTime(new Date());
				sumScoref = sumScoref == null ? 0 : sumScoref;
				BigDecimal bd = new BigDecimal((double)sumScoref);    
				bd = bd.setScale(0,BigDecimal.ROUND_HALF_UP);    
				sumScoref = bd.floatValue();  
				studyRecord.setScore(sumScoref);
				this.updateEntity(studyRecord);
				// 更新学习记录缓存 zihongyan 2013-03-05 
				ce.setStudyRecord(studyRecord);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}

	}

	/**
	 * 通过课程ID 得到站点ID
	 * 
	 * @param courseId
	 * @return
	 */
	private String getSiteIdByCourseId(String courseId) {
		String siteId = "";
		Map<String, String> values = new HashMap<String, String>();
		String hql = "select courseModel.siteId from MeleteCourseModel courseModel where courseModel.id=:courseId";
		values.put("courseId", courseId);
		List resultList = findEntity(hql, values);
		if (resultList != null && !resultList.isEmpty()) {
			siteId = (String) resultList.get(0);
		}

		return siteId;
	}

	public boolean checkSectionPassStatus(Long sectionId, Long studyrecordId, boolean checkAct) {
		String sri = "";
		if(studyrecordId!=null){
			sri = studyrecordId.toString();
		}
		CacheElement ce = CacheUtil.getInstance().getCacheOfStudyrecord(sri);
		CacheElement cacheCourse = CacheUtil.getInstance().getCacheOfCourse(ce.getStudyRecord().getCourseId());

		MeleteSectionRecordModel sectionRecord = ce.getSectonRecord(sectionId);// 从缓存中获得页记录
		MeleteSectionModel section = cacheCourse.getSecton(sectionId);// 从缓存中获得页

		if (checkAct) {
			List<MeleteTestModel> testList = cacheCourse.getTestListByParentId(sectionId.toString(), CodeTable.section);// 从缓存中取页下的作业列表
			if (testList != null && !testList.isEmpty()) {// 若缓存中存在
				for (int i = 0; i < testList.size(); i++) {
					MeleteTestRecordModel testRecord = ce.getTestRecord(testList.get(i).getId());// 从缓存中获得作业记录
					if (CodeTable.IsCaculateScoreYes.equals(testList.get(i).getIsCaculateScore().toString())
							&& CodeTable.passStatusNo.equals(testRecord.getStatus().toString())) {
						return false;
					}
				}
			}

			List<MeleteForumModel> forumList = cacheCourse.getForumListByParentId(sectionId.toString(),
					CodeTable.section);// 从缓存中获得讨论
			if (forumList != null && !forumList.isEmpty()) {
				for (int i = 0; i < forumList.size(); i++) {
					MeleteForumRecordModel forumRecord = ce.getForumRecord(forumList.get(i).getId());// 从缓存中获得讨论记录
					if (CodeTable.IsCaculateScoreYes.equals(forumList.get(i).getIsCaculateScore().toString())
							&& CodeTable.passStatusNo.equals(forumRecord.getStatus().toString())) {
						return false;
					}
				}
			}

			// 判断页的学习时长是否大于教师规定学习时长
			if (sectionRecord.getStudyTime() < section.getStudyTime()) {
				// 学生学习时长 小于 规定的学习时长 则该页不通过
				return false;
			}
		}

		sectionRecord.setStatus(new Long(CodeTable.passStatusYes));
		sectionRecord.setSectionPassTime(new Date());
		sectionRecord.setPassStudyTime(sectionRecord.getStudyTime());
		this.updateEntity(sectionRecord);
		// 更新缓存中的页记录
		ce.editSectionRecord(sectionRecord);

		return true;
	}

	// 根据模块节点ID 学习记录ID 检查并更改对应的模块节点是否通过
	public boolean checkModulePassStatus(Long moduleId, Long studyrecordId, boolean checkAct) {
		CacheElement ce = CacheUtil.getInstance().getCacheOfStudyrecord(studyrecordId.toString());
		CacheElement cacheCourse = CacheUtil.getInstance().getCacheOfCourse(ce.getStudyRecord().getCourseId());
		MeleteModuleModel module = cacheCourse.getModule(moduleId);// 从缓存中获得模块

		if (checkAct) {
			List<MeleteTestModel> testList = cacheCourse.getTestListByParentId(moduleId.toString(), CodeTable.module);
			if (testList != null && !testList.isEmpty() && testList.size() > 0) {
				for (int i = 0; i < testList.size(); i++) {
					MeleteTestRecordModel testRecord = ce.getTestRecord(testList.get(i).getId());
					if (CodeTable.IsCaculateScoreYes.equals(testList.get(i).getIsCaculateScore().toString())
							&& CodeTable.passStatusNo.equals(testRecord.getStatus().toString())) {
						return false;
					}
				}
			}

			List<MeleteForumModel> forumList = cacheCourse
					.getForumListByParentId(moduleId.toString(), CodeTable.module);
			if (forumList != null && !forumList.isEmpty() && forumList.size() > 0) {
				for (int i = 0; i < forumList.size(); i++) {
					MeleteForumRecordModel forumRecord = ce.getForumRecord(forumList.get(i).getId());
					if (CodeTable.IsCaculateScoreYes.equals(forumList.get(i).getIsCaculateScore().toString())
							&& CodeTable.passStatusNo.equals(forumRecord.getStatus().toString())) {
						return false;
					}
				}
			}

			String childType = module.getChildType();
			if (childType != null && childType.equals(CodeTable.module)) {// 下级节点是模块
				// List<MeleteModuleModel> childrenList =
				// ce.getModuleListByParentid(moduleId.toString());//
				// 从缓存中获得下级模块集合
				// for (int i = 0; i < childrenList.size(); i++) {
				// MeleteModuleRecordModel moduleRecord =
				// ce.getModuleRecord(childrenList.get(i).getId());// 获得模块记录
				// if
				// (CodeTable.passStatusNo.equals(moduleRecord.getStatus().toString())
				// &&
				// CodeTable.required.equals(module.getRequired().toString())) {
				// // 模块记录未通过 且 该模块 为必修 返回false
				// return false;
				// }
				// }
			} else if (childType != null && childType.equals(CodeTable.section)) {// 下级节点是页
				// List<MeleteSectionModel> sectionList =
				// ce.getSectionListByModuleId(moduleId.toString());
				// for (int i = 0; i < sectionList.size(); i++) {
				// MeleteSectionRecordModel sectionRecord =
				// ce.getSectonRecord(sectionList.get(i).getId());
				// if
				// (CodeTable.passStatusNo.equals(sectionRecord.getStatus().toString())
				// &&
				// CodeTable.required.equals(sectionList.get(i).getRequired().toString()))
				// {
				// return false;
				// }
				// }
			}
		}

		// 没有未通过的活动，表示该模块已通过
		String update = "update MeleteModuleRecordModel set status=?,modulePassTime=?,passStudyTime=studyTime  where studyrecordId=? and moduleId=? and status=? ";
		Object[] param = { new Long(CodeTable.passStatusYes), new Date(), new Long(studyrecordId), new Long(moduleId),
				new Long(CodeTable.passStatusNo) };
		int updateNum = this.updateEntity(update, param);

		MeleteModuleRecordModel moduleRecord = ce.getModuleRecord(moduleId);// 在缓存中获得模块记录
		moduleRecord.setStatus(new Long(CodeTable.passStatusYes));
		moduleRecord.setModulePassTime(new Date());
		moduleRecord.setPassStudyTime(moduleRecord.getStudyTime());
		ce.editModuleRecord(moduleRecord);// 更新缓存中的模块记录
		if (updateNum > 0) {
			Long parentId = module.getParentId();
			if (parentId != null) {// 判断上级节点的通过状态
				checkModulePassStatus(parentId, studyrecordId, true);
			} else {// 上级节点是课程，判断课程的通过状态
				checkCoursePassStatus(module.getCourseId(), studyrecordId);
			}
		}
		return true;
	}

	// 检查课程通过状态
	private boolean checkCoursePassStatus(String courseId, Long studyrecordId) {
		String hql = "select count(*) from MeleteModuleRecordModel record,MeleteModuleModel module "
				+ "where module.id = record.moduleId and module.parentId is null and module.courseId=? and record.studyrecordId=? "
				+ "and record.status=? and module.required =?";
		Object[] p1 = { courseId, studyrecordId, new Long(CodeTable.passStatusNo), new Long(CodeTable.required) };
		List list = this.findEntity(hql, p1);
		if (!list.isEmpty() && list.size() != 0 && !list.get(0).toString().equals("0")) {
			return false;
		}
		hql = "update MeleteStudyRecordModel set lessonStatus=?,coursePassTime=? where studyrecordId=?";
		Object[] p = { new Long(CodeTable.passStatusYes), new Date(), studyrecordId };
		this.updateEntity(hql, p);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sakaiproject.resource.api.study.service.IStudyService#SaveForumRecord
	 * (java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	// 保存提交的讨论
	public void saveForumRecord(String courseId, String userId, String topicId, String studyrecordId) throws Exception {
		try {
			CacheElement ce = CacheUtil.getInstance().getCacheOfStudyrecord(studyrecordId);
			String hql = "select record from MeleteForumRecordModel record,MeleteForumModel forum,MeleteStudyRecordModel study "
					+ "where record.forumId=forum.id and forum.topicId=? and record.courseId=? "
					+ "and record.studyrecordId=study.studyrecordId and study.studentId=? ";
			Object[] param = { topicId, courseId, userId };
			List list = this.findEntity(hql, param);
			boolean checkUsuallyScore = false;
			if (!list.isEmpty()) {
				MeleteForumRecordModel record = (MeleteForumRecordModel) list.get(0);
				String status = record.getStatus().toString();
				Long attemptNum = record.getAttemptNumber();
				if (attemptNum == null) {
					attemptNum = new Long(0);
				}
				if (attemptNum == 0) {
					checkUsuallyScore = true;
				}
				record.setAttemptNumber(attemptNum + 1);
				if (status.equals(CodeTable.passStatusNo)) {
					record.setStatus(new Long(CodeTable.passStatusYes));
					this.updateEntity(record);// 更新讨论记录到数据库
					ce.editForumRecord(record);// 更新讨论记录到缓存
				} else {
					this.updateEntity(record);// 更新讨论记录到数据库
					ce.editForumRecord(record);// 更新讨论记录到缓存
				}
				if (checkUsuallyScore) {
					this.countUsuallyScore(courseId, userId, studyrecordId);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 教师批完试卷后业务处理方法
	 * 
	 * @param attemptId
	 *            尝试记录
	 * @param userid
	 *            用户ID
	 * @param studentAnswerFile
	 *            学生答案文件
	 * @param courseId
	 *            课程ID
	 * @param paperType
	 *            批改的测试类型 1为作业 2为前测
	 * @throws Exception
	 */
	// 教师批完试卷后业务处理方法
	public Object checkTestSave(String attemptId, String userid, File studentAnswerFile, String courseId,
			String paperType, String studyrecordId) throws Exception {
		CacheElement ce = CacheUtil.getInstance().getCacheOfStudyrecord(studyrecordId);
		Object result = null;
		try {
			if (CodeTable.paperTypeTest.equals(paperType)) {

				// 测试类型是作业
				MeleteTestAttemptModel attemp = (MeleteTestAttemptModel) this.findEntityById(
						MeleteTestAttemptModel.class, new Long(attemptId));

				// 获得尝试记录中的作业记录ID
				Long recordIdString = attemp.getMeleteTestRecordId();

				// 根据测试记录ID获得作业记录
				MeleteTestRecordModel record = (MeleteTestRecordModel) this.findEntityById(MeleteTestRecordModel.class,
						new Long(recordIdString));
				// 通过测试记录获得作业信息ID
				Long testidLong = record.getTestId();

				// 通过作业信息ID获得作业信息
				MeleteTestModel test = (MeleteTestModel) this.findEntityById(MeleteTestModel.class,
						new Long(testidLong));
				// 获得作业信息中的卷面总分
				Float totalScore = new Float(test.getTotalScore());

				// 根据学生答案文件获得主观题分数
				Float subscore = new Float(PaperCheckToolUtil.findSubStudentScore(studentAnswerFile));
				
				
				// 将主观体得分换算为百分制得分
				//BigDecimal percentageSubScore = new BigDecimal(subscore.toString()).divide(
				//		new BigDecimal(totalScore.toString()), 2, BigDecimal.ROUND_CEILING).multiply(
				//		new BigDecimal("100"));
				
				// 存储作业得分按百分制存储
				//attemp.setSubScore(percentageSubScore.floatValue());

				// 存储作业总分按百分制存储
				//attemp.setScore(new Float(attemp.getObjScore().floatValue() + percentageSubScore.floatValue()));
				
				// 2016.10.28修改存储的分数为实际分数（保证和试卷分数一致），更新平时成绩时换算为100分制
				attemp.setSubScore(subscore.floatValue());
				attemp.setScore(new Float(attemp.getObjScore().floatValue() + subscore.floatValue()));
				
				// 批改状态设为主观题已批改
				attemp.setPagerstatus(CodeTable.subCheckYes);
				this.updateEntity(attemp);// 更新记录

				if (record.getScore() == null || (record.getScore().floatValue() < attemp.getScore().floatValue())) { // 记录分数小于尝试分数，更新记录分数
					record.setScore(attemp.getScore() == null ? 0 : attemp.getScore());
					if (record.getStatus().toString().equals(CodeTable.passStatusNo)) {// 记录状态为未通过
						// 检查本次分数是否大于通过分数

						//Long passScoreLong = test.getMasteryScore();
						// 分数大于等于测试记录中的通过分数百分比 则为通过
						// TODO 验证长整型和float类型比较
						//if (passScoreLong.longValue() <= attemp.getScore().floatValue()) {
						//	record.setStatus(new Long(CodeTable.passStatusYes));
						//}
						// 2016.10.28修改存储的分数为实际分数（保证和试卷分数一致），更新平时成绩时换算为100分制
						BigDecimal total = new BigDecimal(totalScore);
						BigDecimal mastery = new BigDecimal(test.getMasteryScore().toString());
						BigDecimal passScoreBD = total.multiply(mastery.divide(new BigDecimal("100")));
						Float passScore = passScoreBD.floatValue();
						if (passScore.floatValue() <= attemp.getScore().floatValue()) {
							record.setStatus(new Long(CodeTable.passStatusYes));
						}
					}
					this.updateEntity(record);// 更新数据库的作业记录
					ce.editTestRecord(record);// 更新缓存中的作业记录
					this.countUsuallyScore(courseId, userid, studyrecordId);// 计算平时成绩
				}
				result = record;
			} else if (CodeTable.paperTypeSelfTest.equals(paperType)) {// 测试类型是前测

				// 根据前测ID获得前测尝试记录
				MeleteSelftestAttemptModel selfAttemp = (MeleteSelftestAttemptModel) this.findEntityById(
						MeleteSelftestAttemptModel.class, new Long(attemptId));
				// 通过前测尝试记录获得前测记录ID
				Long recordId = selfAttemp.getMeleteSelftestRecordId();
				// 通过前测记录ID获得前测记录
				MeleteSelftestRecordModel record = (MeleteSelftestRecordModel) this.findEntityById(
						MeleteSelftestRecordModel.class, new Long(recordId));
				// 获得前测信息ID
				Long selfTestid = record.getSelftestId();
				// 获得前测信息
				MeleteSelfTestModel selfTest = (MeleteSelfTestModel) this.findEntityById(MeleteSelfTestModel.class,
						new Long(selfTestid));

				// 获得前测信息中的前测总分
				Float totalScore = new Float(selfTest.getTotalScore());

				// 通过学生答案文件获得主观题分数
				Float subscore = new Float(PaperCheckToolUtil.findSubStudentScore(studentAnswerFile));
				
				
				//BigDecimal percentageSubScore = new BigDecimal(subscore.toString()).divide(
				//		new BigDecimal(totalScore.toString()), 2, BigDecimal.ROUND_CEILING).multiply(
				//		new BigDecimal("100"));
				
				// 存储得分按百分制存储
				//selfAttemp.setSubScore(percentageSubScore.floatValue());

				// 存储总分按百分制存储
				//selfAttemp.setScore(new Float(selfAttemp.getObjScore().floatValue() + percentageSubScore.floatValue()));
				
				// 2016.10.28修改存储的分数为实际分数（保证和试卷分数一致），更新平时成绩时换算为100分制
				selfAttemp.setSubScore(subscore.floatValue());
				selfAttemp.setScore(new Float(selfAttemp.getObjScore().floatValue() + subscore.floatValue()));
				
				selfAttemp.setPagerstatus(CodeTable.subCheckYes);// 批改状态设为主观题已批改
				this.updateEntity(selfAttemp);// 更新尝试记录

				if (record.getScore() == null || (record.getScore().floatValue() < selfAttemp.getScore().floatValue())) { // 记录分数小于尝试分数，更新记录分数
					record.setScore(selfAttemp.getScore());
					if (record.getStatus().toString().equals(CodeTable.passStatusNo)) {// 记录状态为未通过

						// 在前测信息中获得通过分数
//						Long passScoreLong = selfTest.getMasteryScore();
//						// 通过分数小于 尝试记录的总分数
//						if (passScoreLong.longValue() <= selfAttemp.getScore().floatValue()) {
//							record.setStatus(new Long(CodeTable.passStatusYes));
//
//						}
						// 2016.10.28修改存储的分数为实际分数（保证和试卷分数一致），更新平时成绩时换算为100分制
						BigDecimal total = new BigDecimal(totalScore);
						BigDecimal mastery = new BigDecimal(selfTest.getMasteryScore().toString());
						BigDecimal passScoreBD = total.multiply(mastery.divide(new BigDecimal("100")));
						Float passScore = passScoreBD.floatValue();
						if (passScore.floatValue() <= selfAttemp.getScore().floatValue()) {
							record.setStatus(new Long(CodeTable.passStatusYes));
						}
					}
					this.updateEntity(record);// 更新数据库中的前测记录
					ce.editSelftestRecord(record);// 更新缓存中的前测记录
					this.countUsuallyScore(courseId, userid, studyrecordId);// 计算平时成绩
				}
				result = record;
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
		return result;
	}

	/**
	 * 通过作业记录ID 获得 作业记录
	 * 
	 * @param testRecordId
	 * @return
	 * @throws Exception
	 */
	// 通过作业记录ID 获得 作业记录
	public MeleteTestRecordModel getTestRecordByTestRecordId(String testRecordId) throws Exception {
		MeleteTestRecordModel result = (MeleteTestRecordModel) this.findEntityById(MeleteTestRecordModel.class,
				new Long(testRecordId));
		return result;
	}

	/**
	 * 通过前测记录Id 获得前测记录
	 * 
	 * @return
	 * @throws Exception
	 */
	// 通过前测记录Id 获得前测记录
	public MeleteSelftestRecordModel getSelfTestRecordBySelfTestRecordId(String selfTestRecordId) throws Exception {
		MeleteSelftestRecordModel result = (MeleteSelftestRecordModel) this.findEntityById(
				MeleteSelftestRecordModel.class, new Long(selfTestRecordId));
		return result;
	}

	/**
	 * 根据活动id 获得 section 或者 module
	 * 
	 * @param activityId
	 *            活动ID
	 * @param activityType
	 *            活动类型
	 * @param studyrecordId
	 *            学习记录
	 * @return
	 * @throws Exception
	 */
	// 根据活动id 获得 section 或者 module
	public Object getSectionOrModuleByActivityId(String activityId, String activityType, String studyrecordId)
			throws Exception {
		CacheElement ce = CacheUtil.getInstance().getCacheOfStudyrecord(studyrecordId);
		CacheElement cacheCourse = CacheUtil.getInstance().getCacheOfCourse(ce.getStudyRecord().getCourseId());
		Object result = null;
		if (CodeTable.test.equals(activityType)) {// 活动为作业
			MeleteTestModel test = cacheCourse.getTest(Long.valueOf(activityId));// 从缓存中获得
			if (CodeTable.belongSection.equals(test.getBelongType())) {
				// 若作业属于页节点
				MeleteSectionModel section = cacheCourse.getSecton(test.getSectionId());// 从缓存中获得页
				result = section;
			} else if (CodeTable.belongMudole.equals(test.getBelongType())) {
				// 若作业属于模块节点
				MeleteModuleModel module = cacheCourse.getModule(test.getModuleId());// 从缓存中获得模块
				result = module;
			}

		} else if (CodeTable.selftest.equals(activityType)) {// 活动为前测
			MeleteSelfTestModel selfTest = cacheCourse.getSelftest(Long.valueOf(activityId));// 从缓存中获得
			if (CodeTable.belongSection.equals(selfTest.getBelongType())) {
				// 若前测属于页节点
				MeleteSectionModel section = cacheCourse.getSecton(selfTest.getSectionId());// 从缓存中获得页
				result = section;
			} else if (CodeTable.belongMudole.equals(selfTest.getBelongType())) {
				// 若前测属于模块节点
				MeleteModuleModel module = cacheCourse.getModule(selfTest.getModuleId());// 从缓存中获得模块
				result = module;
			}

		} else if (CodeTable.forum.equals(activityType)) {// 活动为讨论
			MeleteForumModel forum = cacheCourse.getForum(Long.valueOf(activityId));// 从缓存中获得
			if (CodeTable.belongSection.equals(forum.getBelongType())) {
				// 若讨论属于页节点
				MeleteSectionModel section = cacheCourse.getSecton(forum.getSectionId());// 从缓存中获得页
				result = section;
			} else if (CodeTable.belongMudole.equals(forum.getBelongType())) {
				// 若讨论属于模块节点
				MeleteModuleModel module = cacheCourse.getModule(forum.getModuleId());// 从缓存中获得模块
				result = module;
			}
		}
		return result;
	}

	/**
	 * 通过moduleID studyrecordId 获得模块下的所有作业 和前测的尝试记录
	 * 
	 * @param moduleId
	 * @param studyrecordId
	 * @return
	 * @throws Exception
	 */
	public List<Object> getAttemptByModuleIdAndStudyRecordId(String moduleId, String studyrecordId) throws Exception {
		List<Object> resultList = new ArrayList<Object>();
		try {
			// 查询模块节点下的作业尝试记录
			String hqlTestAttempt = "select new Map(testAttempt.testattemptId as id,testRecord.testName as attemptName,testAttempt.endTime as endTime,testAttempt.objScore as objItem,testAttempt.subScore as subItem,testAttempt.score as score,testAttempt.pagerstatus as pagerstatus,testAttempt.testPaperid as paperid,testRecord.courseId as courseId, testRecord.testId as testId,'"
					+ CodeTable.test
					+ "' as testType)"
					+ " from MeleteTestRecordModel testRecord,MeleteTestAttemptModel testAttempt "
					+ "where testRecord.testrecordId=testAttempt.meleteTestRecordId and testRecord.moduleId=? and testRecord.studyrecordId=? order by testAttempt.meleteTestRecordId";
			Object[] params = { Long.parseLong(moduleId), Long.parseLong(studyrecordId) };
			resultList.addAll(this.findEntity(hqlTestAttempt, params));

			// 查询模块节点下的前测尝试记录
			String hqlSelfTestAttempt = "select new Map(selfTestAttempt.selftestattemptId as id,selfTestRecord.selftestName as attemptName,selfTestAttempt.endTime as endTime,selfTestAttempt.objScore as objItem,selfTestAttempt.subScore as subItem,selfTestAttempt.score as score,selfTestAttempt.pagerstatus as pagerstatus,selfTestAttempt.selftestPaperid as paperid, selfTestRecord.courseId as courseId, selfTestRecord.selftestId as testId,'"
					+ CodeTable.selftest
					+ "' as testType)"
					+ " from MeleteSelftestRecordModel selfTestRecord,MeleteSelftestAttemptModel selfTestAttempt  "
					+ "where selfTestRecord.selftestrecordId=selfTestAttempt.meleteSelftestRecordId and selfTestRecord.moduleId=? and selfTestRecord.studyrecordId=? order by selfTestAttempt.meleteSelftestRecordId";
			resultList.addAll(this.findEntity(hqlSelfTestAttempt, params));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return resultList;
	}

	public List getActivityValue(String sectionRecordId, String activityId) throws Exception {
		try {
			String hql = "select count(*), max(activityValue) from MeleteSectionRecordActivityModel where sectionRecordId=? and activityId=?";
			Object[] param = { Long.valueOf(sectionRecordId), activityId };
			List list = this.getHibernateTemplate().find(hql, param);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	public void setActivityValue(String sectionRecordId, String activityId, String activityValue) throws Exception {
		try {
			MeleteSectionRecordActivityModel model = new MeleteSectionRecordActivityModel();
			model.setSectionRecordId(Long.valueOf(sectionRecordId));
			model.setActivityId(activityId);
			model.setActivityValue(Float.valueOf(activityValue));
			model.setActivityTime(new Date());
			this.getHibernateTemplate().save(model);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 为学生增加印象分数
	 * 
	 * @param selectionIds
	 *            所选择的学生的学习记录
	 * @param scores
	 *            加分前的学生平时成绩
	 * @param impScore
	 *            增加的印象分数
	 * @return
	 * @throws Exception
	 */
	public boolean addImpScore(String[] selectionIds, String[] scores, String impScore) throws Exception {
		boolean result = false;

		Long[] studyRecordIds = new Long[selectionIds.length];
		Float[] stuScores = new Float[scores.length];

		try {
			for (int i = 0; i < scores.length; i++) {
				stuScores[i] = Float.valueOf(scores[i]);
				studyRecordIds[i] = Long.valueOf(selectionIds[i]);
				Float sum = stuScores[i] + Float.valueOf(impScore);
				String hql = "update MeleteStudyRecordModel studyRecord set studyRecord.score=? where studyRecord.studyrecordId=?";
				Object[] params = new Object[2];
				params[1] = studyRecordIds[i];
				if (sum >= 0.0f && sum <= 100.0f) {
					params[0] = sum;
				} else if (sum < 0.0f) {// 若加和后的平时成绩 小于0 则将学习记录中的平时成绩改为 0
					params[0] = new Float(0.0f);
				} else if (sum > 100.0f) {// 若加和后的平时成绩 大于100 则将学习记录中的平时成绩改为 100
					params[0] = new Float(100.0f);
				}
				this.updateEntity(hql, params);

				// 更新缓存
				CacheElement ce = CacheUtil.getInstance().getCacheOfStudyrecord(selectionIds[i]);
				if (ce != null) {
					// 若缓存不为空则更新
					MeleteStudyRecordModel studyRecord = ce.getStudyRecord();
					if (sum >= 0.0f && sum <= 100.0f) {
						studyRecord.setScore(sum);
					} else if (sum < 0.0f) {// 若加和后的平时成绩 小于0 则将学习记录中的平时成绩改为 0
						studyRecord.setScore(new Float(0.0f));
					} else if (sum > 100.0f) {// 若加和后的平时成绩 大于100 则将学习记录中的平时成绩改为
												// 100
						studyRecord.setScore(new Float(100.0f));
					}
					ce.setStudyRecord(studyRecord);
				}
			}
			result = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return false;
		}

		return result;
	}

	/**
	 * 用于保存学习历史记录
	 * 
	 * @author zihongyan 2013-3-21
	 * @param stuHistoryRecord
	 *            学习历史记录实体
	 * @return 保存信息
	 */
	public String saveStudyHistoryRecord(MeleteStudyHistoryRecordModel stuHistoryRecord) {
		// TODO Auto-generated method stub
		try {
			this.createEntity(stuHistoryRecord);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return e.getMessage();
		}
		return stuHistoryRecord.getStudentId();
	}

	/**
	 * 根据学生的id和课程id查询是否有浏览历史记录
	 * 
	 * @author zihongyan 2013-3-21
	 * @param studentId
	 *            学生id
	 * @param courseId
	 *            课程id
	 * @return 浏览历史记录
	 */
	public MeleteStudyHistoryRecordModel findStudyHistortRecordByStudentIDAndCourseId(String studentId, String courseId) {
		// TODO Auto-generated method stub
		try {
			String hql = "from MeleteStudyHistoryRecordModel where studentId =? and courseId = ? and type='2'";
			Object[] parameters = { studentId, courseId };
			List<MeleteStudyHistoryRecordModel> meleteStudyHistoryRecords = this.findEntity(hql, parameters);
			if (meleteStudyHistoryRecords != null && !meleteStudyHistoryRecords.isEmpty()) {
				return meleteStudyHistoryRecords.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 根据学生的id和课程id删除浏览历史记录
	 * 
	 * @author zihongyan 2013-3-21
	 * @param studentId
	 *            学生id
	 * @param courseId
	 *            课程id
	 */
	public void deleteStudyHistoryRecordByStudentIdAndCourseId(String studentId, String courseId) {
		// TODO Auto-generated method stub
		try {
			MeleteStudyHistoryRecordModel studyHistoryRecord = findStudyHistortRecordByStudentIDAndCourseId(studentId,
					courseId);
			if (studyHistoryRecord != null) {
				this.deleteEntity(studyHistoryRecord);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 根据学习记录Id查询作业记录信息
	 * 
	 * @param studyrecordId
	 * @param testId
	 * @return
	 * @throws Exception
	 */
	public List<MeleteTestRecordModel> getTestRecordByStudyrecordId(String studyrecordId) throws Exception {
		try {
			String hql = "from MeleteTestRecordModel where studyrecordId =?";
			Object[] parameters = { Long.valueOf(studyrecordId) }; 
			List<MeleteTestRecordModel> moduleRecords = this.findEntity(hql, parameters);
			if (moduleRecords != null && moduleRecords.size() > 0) {
				return moduleRecords;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}
	
	/**
	 * 根据学习记录id得到所有Section的学习时长和
	 * @param studyrecordId
	 * @return
	 */
	public Long getSectionStudyTimeSum(String studyrecordId)throws Exception{
		try {
			String hql = "select sum(studyTime) from MeleteSectionRecordModel where studyrecordId =?";
			Object[] parameters = { Long.valueOf(studyrecordId) };
			List list = this.findEntity(hql, parameters);
			if (list != null && !list.isEmpty()) {
				return (Long)list.get(0) == null ? 0l : (Long)list.get(0);
			} else {
				return 0l;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}
	
	/**
	 * 根据模块学习记录id得到所有Section的学习时长和
	 * @param studyrecordId
	 * @return
	 */
	public Long getSectionStudyTimeSumByModule(Long moduleId)throws Exception{
		try {
			String hql = "select sum(studyTime) from MeleteSectionRecordModel where meleteModuleRecordId =?";
			Object[] parameters = { moduleId };
			List list = this.findEntity(hql, parameters);
			if (list != null && !list.isEmpty()) {
				return (Long)list.get(0) == null ? 0l : (Long)list.get(0);
			} else {
				return 0l;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}
	
	/**
	 * 根据学习节点id得到所有Section的学习时长和
	 * @param sectionrecordId
	 * @return
	 */
	public Long getSectionStudyTimeDetailSum(Long sectionrecordId)throws Exception{
		try {
			String hql = "select sum(studyTime) from MeleteSectionRecordDetailModel where sectionrecordId =?";
			Object[] parameters = {sectionrecordId};
			List list = this.findEntity(hql, parameters);
			if (list != null && !list.isEmpty()) {
				return (Long)list.get(0) == null ? 0l : (Long)list.get(0);
			} else {
				return 0l;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}
	
	/**
	 * 得到最早的页学习时间
	 * @param studyrecordId
	 * @return
	 */
	public Date getSectionFirstStartStudyTime(String studyrecordId)throws Exception{
		try {
			String hql = " select min(startStudyTime) from MeleteSectionRecordModel where studyrecordId =?";
			Object[] parameters = { Long.valueOf(studyrecordId) };
			List list = this.findEntity(hql, parameters);
			if (list != null && !list.isEmpty()) {
				return (Date)list.get(0);
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
	}
}
