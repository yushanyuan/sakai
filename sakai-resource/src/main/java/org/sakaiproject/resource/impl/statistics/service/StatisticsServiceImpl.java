package org.sakaiproject.resource.impl.statistics.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.sakaiproject.resource.api.statistics.service.IStatisticsService;
import org.sakaiproject.resource.util.CodeTable;
import org.sakaiproject.resource.util.HibernateDaoSupport;
import org.sakaiproject.resource.util.QueryString;

public class StatisticsServiceImpl extends HibernateDaoSupport implements IStatisticsService {

	/** 查询活动成绩 1 代表作业，2代表自测 */
	public List<Map<String, Object>> findActivityScore(Map<String, Object> conditions, int activity) throws Exception {
		StringBuffer sbsql = new StringBuffer();
		if (activity == 1) {
			sbsql
					.append("select new map(su.firstName as name,su.publicstunum as publicstunum,su.organizationName as organizationName,su.cellphone as cellphone,su.telephone as telephone,su.email as email,mtr.attemptNumber as attemptNumber,mtr.status as status,mtr.score as score) "
							+ " from MeleteSakaiUserModel su,MeleteCourseModel mc,MeleteTestRecordModel mtr,MeleteStudyRecordModel msr "
							+ " where su.userId=msr.studentId and msr.courseId=mc.id and msr.studyrecordId=mtr.studyrecordId and mc.siteId=:siteId and mtr.testId=:id");

			// 给次数赋值
			if (CodeTable.greaterThan.equals((String) conditions.get("timesLimit"))) {
				sbsql.append(" and mtr.attemptNumber>:times");
			} else if (CodeTable.equals.equals((String) conditions.get("timesLimit"))) {
				sbsql.append(" and mtr.attemptNumber=:times");
			} else if (CodeTable.lessThen.equals((String) conditions.get("timesLimit"))) {
				sbsql.append(" and mtr.attemptNumber<:times");
			}
			// 给分数赋值
			sbsql.append(" and mtr.score >=:minScore");
			sbsql.append(" and mtr.score <=:maxScore");
			// 给状态赋值

			if (conditions.get("status") != null) {
				sbsql.append(" and mtr.status=:status");
			}
		} else {
			sbsql
					.append("select new map(su.firstName as name,su.publicstunum as publicstunum,su.organizationName as organizationName,su.cellphone as cellphone,su.telephone as telephone,su.email as email,msfr.attemptNumber as attemptNumber,msfr.status as status,msfr.score as score) "
							+ " from MeleteSakaiUserModel su,MeleteCourseModel mc,MeleteSelftestRecordModel msfr,MeleteStudyRecordModel msr "
							+ " where su.userId=msr.studentId and msr.courseId=mc.id and msr.studyrecordId=msfr.studyrecordId and mc.siteId=:siteId and msfr.selftestId=:id");
			// 给次数赋值
			if (CodeTable.greaterThan.equals((String) conditions.get("timesLimit"))) {
				sbsql.append(" and msfr.attemptNumber>:times");
			} else if (CodeTable.equals.equals((String) conditions.get("timesLimit"))) {
				sbsql.append(" and msfr.attemptNumber=:times");
			} else if (CodeTable.lessThen.equals((String) conditions.get("timesLimit"))) {
				sbsql.append(" and msfr.attemptNumber<:times");
			}
			// 给分数赋值
			sbsql.append(" and msfr.score >=:minScore");
			sbsql.append(" and msfr.score <=:maxScore");
			// 给状态赋值

			if (conditions.get("status") != null) {
				sbsql.append(" and msfr.status=:status");
			}
		}
		// 这个对象不需要作为条件
		conditions.remove("timesLimit");
		return this.findEntity(sbsql.toString(), conditions);
	}

	/** 统计活动(作业)平均次数和成绩 */
	public List<Map<String, Object>> statActivityAvgTimeAndScoreOfTestRecord(String siteId) throws Exception {
		Map<String, Object> parameters = new HashMap<String, Object>();
		StringBuffer sbsql = new StringBuffer(
				"select new map(mt.id as id,mt.name as title,(count(testAttempt)/count(distinct mtr.testrecordId)) as avgTimes,avg(testAttempt.score) as avgScore) "
						+ " from MeleteCourseModel mc,MeleteStudyRecordModel msr,MeleteTestRecordModel mtr,MeleteTestModel mt, MeleteTestAttemptModel testAttempt"
						+ " where testAttempt.meleteTestRecordId=mtr.testrecordId and mc.siteId=:siteId and mc.id=msr.courseId and msr.studyrecordId=mtr.studyrecordId and mtr.testId=mt.id and mtr.status=1 "
						+ " group by mt.id,mt.name");
		parameters.put("siteId", siteId);
		return this.findEntity(sbsql.toString(), parameters);
	}

	/** 统计活动(前测)平均次数和成绩 */
	public List<Map<String, Object>> statActivityAvgTimeAndScoreOfSelfTestRecord(String siteId) throws Exception {
		Map<String, Object> parameters = new HashMap<String, Object>();
		StringBuffer sbsql = new StringBuffer(
				"select new map(ms.id as id,ms.name as title,(sum(msfr.attemptNumber)/count(msfr.selftestrecordId)) as avgTimes,avg(msfr.score) as avgScore) "
						+ " from MeleteCourseModel mc,MeleteStudyRecordModel msr,MeleteSelftestRecordModel msfr,MeleteSelfTestModel ms "
						+ " where mc.siteId=:siteId and mc.id=msr.courseId and msr.studyrecordId=msfr.studyrecordId and msfr.selftestId=ms.id and msfr.status=1 "
						+ " group by ms.id,ms.name");
		parameters.put("siteId", siteId);
		return this.findEntity(sbsql.toString(), parameters);
	}

	/** 统计活动(论坛)平均次数和成绩 */
	public List<Map<String, Object>> statActivityAvgTimeAndScoreOfForumRecord(String siteId) throws Exception {
		Map<String, Object> parameters = new HashMap<String, Object>();
		StringBuffer sbsql = new StringBuffer(
				"select new map(mf.id as id,mf.name as title,(sum(mfr.attemptNumber)/count(mfr.forumrecordId)) as avgTimes,avg(mf.ratio) as avgScore) "
						+ " from MeleteCourseModel mc,MeleteStudyRecordModel msr,MeleteForumRecordModel mfr,MeleteForumModel mf "
						+ " where mc.siteId=:siteId and mc.id=msr.courseId and msr.studyrecordId=mfr.studyrecordId and mfr.forumId=mf.id and mfr.status=1 "
						+ " group by mf.id,mf.name");
		parameters.put("siteId", siteId);
		return this.findEntity(sbsql.toString(), parameters);
	}

	/** 统计模块平均通过时间 */
	public List<Map<String, Object>> statModelAvgThroughTime(String siteId) throws Exception {
		Map<String, Object> parameters = new HashMap<String, Object>();
		StringBuffer sbsql = new StringBuffer("select new map(ms.title as title,ms.avgPassTime as avgPassTime) "
				+ " from MeleteCourseModel mc ,MeleteModuleModel mm,MeleteSectionModel ms "
				+ " where mc.siteId=:siteId and mc.id=mm.courseId and mm.id=ms.moduleId order by mm.idx, ms.idx");
		parameters.put("siteId", siteId);
		return this.findEntity(sbsql.toString(), parameters);
	}

	/** 统计学生作业提交情况 */
	public List<Map<String, Object>> statStudentOfTestRecord(String siteId) throws Exception {
		Map<String, Object> parameters = new HashMap<String, Object>();
		StringBuffer sbsql = new StringBuffer();
		// 如果为true就只查询参与的人数
		sbsql
				.append("select new map(mt.id as id,mt.name as title,mtr.status as passStatus,mtr.attemptNumber as attemptNum) "
						+ " from MeleteCourseModel mc,MeleteStudyRecordModel msr,MeleteTestRecordModel mtr,MeleteTestModel mt "
						+ " where mc.siteId=:siteId and mc.id=msr.courseId and msr.studyrecordId=mtr.studyrecordId and mtr.testId=mt.id order by mt.belongType,mt.idx");
		parameters.put("siteId", siteId);
		return this.findEntity(sbsql.toString(), parameters);
	}

	/** 统计学生前测情况 */
	public List<Map<String, Object>> statStudentOfSelfTestRecord(String siteId) throws Exception {
		Map<String, Object> parameters = new HashMap<String, Object>();
		StringBuffer sbsql = new StringBuffer();
		// 如果为true就只查询参与的人数
		sbsql
				.append("select new map(ms.id as id,ms.name as title,msfr.status as passStatus,msfr.attemptNumber as attemptNum) "
						+ " from MeleteCourseModel mc,MeleteStudyRecordModel msr,MeleteSelftestRecordModel msfr,MeleteSelfTestModel ms "
						+ " where mc.siteId=:siteId and mc.id=msr.courseId and msr.studyrecordId=msfr.studyrecordId and msfr.selftestId=ms.id order by ms.belongType, ms.idx");
		parameters.put("siteId", siteId);
		return this.findEntity(sbsql.toString(), parameters);
	}

	/** 统计学生论坛参与情况 */
	public List<Map<String, Object>> statStudentOfForumRecord(String siteId) throws Exception {
		Map<String, Object> parameters = new HashMap<String, Object>();
		StringBuffer sbsql = new StringBuffer();
		// 如果为true就只查询参与的人数
		sbsql
				.append("select new map(mf.id as id,mf.name as title,mfr.status as passStatus,mfr.attemptNumber as attemptNum) "
						+ " from MeleteCourseModel mc,MeleteForumRecordModel mfr,MeleteStudyRecordModel msr,MeleteForumModel mf "
						+ " where mc.siteId=:siteId and mc.id=msr.courseId and msr.studyrecordId=mfr.studyrecordId and mfr.forumId=mf.id order by mf.belongType, mf.idx");
		parameters.put("siteId", siteId);
		return this.findEntity(sbsql.toString(), parameters);
	}

	/** 根据站点ID查询所有的章节点 */
	public List<Map<String, Object>> getModuleBySiteId(String siteId, Boolean bl) throws Exception {
		Map<String, Object> parameters = new HashMap<String, Object>();
		StringBuffer sbsql = new StringBuffer(
				"select new map(mm.id as id,mm.title as title,mm.studyDay as studyDay,mm.parentId as pId,mm.childType as ct,mmr.startStudyTime as sst,mmr.studyTime as studyTime,mmr.status as status) "
						+ " from MeleteCourseModel mc,MeleteModuleModel mm,MeleteModuleRecordModel mmr ");
		if (bl == null) {
			sbsql.append(" where mc.siteId=:siteId and mc.id=mm.courseId and mm.id=mmr.moduleId ");
		} else {
			if (bl) {
				sbsql
						.append(" where mc.siteId=:siteId and mc.id=mm.courseId and mm.id=mmr.moduleId and mm.parentId is null ");
			} else {
				sbsql
						.append(" where mc.siteId=:siteId and mc.id=mm.courseId and mm.id=mmr.moduleId and mm.parentId is not null  ");
			}
		}
		sbsql.append(" order by mm.idx");
		parameters.put("siteId", siteId);
		return this.findEntity(sbsql.toString(), parameters);
	}

	/** 根据站点ID查询所有叶节点 */
	public List<Map<String, Object>> getSectionBySiteId(String siteId) throws Exception {
		Map<String, Object> parameters = new HashMap<String, Object>();
		StringBuffer sbsql = new StringBuffer(
				"select new map(ms.id as id,ms.moduleId as moduleId,ms.title as title,ms.studyTime as studyDay,msr.startStudyTime as sst,msr.studyTime as studyTime,msr.status as status) "
						+ " from MeleteCourseModel mc,MeleteSectionModel ms,MeleteSectionRecordModel msr "
						+ " where mc.siteId=:siteId and mc.id=msr.courseId and ms.id=msr.sectionId"
						+ " order by ms.idx");
		parameters.put("siteId", siteId);
		return this.findEntity(sbsql.toString(), parameters);
	}

	/** 根据站点ID查询所有作业 */
	public List<Map<String, Object>> getTestBySIiteId(String siteId) throws Exception {
		Map<String, Object> parameters = new HashMap<String, Object>();
		StringBuffer sbsql = new StringBuffer(
				"select new map(mt.sectionId as sectionId,mt.moduleId as moduleId,mt.name as name,mtr.score as score,mtr.attemptNumber as num) "
						+ " from MeleteCourseModel mc,MeleteTestModel mt,MeleteTestRecordModel mtr "
						+ " where mc.siteId=:siteId and mc.id=mtr.courseId and mt.id=mtr.testId" + " order by mt.idx");
		parameters.put("siteId", siteId);
		return this.findEntity(sbsql.toString(), parameters);
	}

	/** 根据站点ID查询所有前测 */
	public List<Map<String, Object>> getSelfTestBySIiteId(String siteId) throws Exception {
		Map<String, Object> parameters = new HashMap<String, Object>();
		StringBuffer sbsql = new StringBuffer(
				"select new map(ms.sectionId as sectionId,ms.moduleId as moduleId,ms.name as name,msr.score as score,msr.attemptNumber as num) "
						+ " from MeleteCourseModel mc,MeleteSelfTestModel ms,MeleteSelftestRecordModel msr "
						+ " where mc.siteId=:siteId and mc.id=msr.courseId and ms.id=msr.selftestId"
						+ " order by ms.idx");
		parameters.put("siteId", siteId);
		return this.findEntity(sbsql.toString(), parameters);
	}

	/** 根据站点ID查询所有论坛 */
	public List<Map<String, Object>> getForumBySIiteId(String siteId) throws Exception {
		Map<String, Object> parameters = new HashMap<String, Object>();
		StringBuffer sbsql = new StringBuffer(
				"select new map(mf.sectionId as sectionId,mf.moduleId as moduleId,mf.name as name,mfr.attemptNumber as num) "
						+ " from MeleteCourseModel mc,MeleteForumModel mf,MeleteForumRecordModel mfr "
						+ " where mc.siteId=:siteId and mc.id=mfr.courseId and mf.id=mfr.forumId" + " order by mf.idx");
		parameters.put("siteId", siteId);
		return this.findEntity(sbsql.toString(), parameters);
	}
	
	/** 查询学习时长 */
	public Object[] getStudyTimeByCondition(Map<String, Object> conditions,Integer start,String sort,String dir) throws Exception{
		Object[] results = null;
		List<Object> resultList = null;		
		Map parameters = new HashMap();
		
		QueryString hql = new QueryString();
		// 如果是作业
		hql.setSelect("select new Map(m.studyrecordId as studyrecordId,su.firstName as stuName,su.publicstunum as stuNum,su.sex as sex,m.startStudyTime as startStudyTime,m.totalTime as totalTime,su.specialtyName as speName,su.studentclassName as className)  ");
		hql.setFrom("from MeleteStudyRecordModel m,MeleteSakaiUserModel su ");
		StringBuffer sb = new StringBuffer();
		 
		sb.append(" where su.userId=m.studentId and m.courseId=:courseId  ");
		parameters.put("courseId", conditions.get("courseId"));
		
		if(conditions.get("stuName") != null){
			sb.append(" and su.firstName like :stuName ");
			parameters.put("stuName", "%"+conditions.get("stuName")+"%");
		}
		
		if(conditions.get("stuNum") != null){
			sb.append("and su.publicstunum = :stuNum");
			parameters.put("stuNum", conditions.get("stuNum"));
		}
		
		if (conditions.get("studyStartTime") != null) {
			sb.append(" and m.startStudyTime>=:studyStartTime");
			parameters.put("studyStartTime", conditions.get("studyStartTime"));
		}
		
		if (conditions.get("studyEndTime") != null) {
			sb.append(" and m.startStudyTime<=:studyEndTime");
			parameters.put("studyEndTime", conditions.get("studyEndTime"));
		}
		
		if(StringUtils.isNotBlank(sort) && StringUtils.isNotBlank(dir)){
			if(sort.equals("stuName")){
				sort = "su.firstName";
			}
			if(sort.equals("stuNum")){
				sort = "su.publicstunum";
			}
			if(sort.equals("totalTime")){
				sort = "m.totalTime";
			}
			if(sort.equals("sex")){
				sort = "su.sex";
			}
			sb.append(" order by " + sort + " " + dir);
		}else{
			sb.append(" order by m.totalTime desc");
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
	
	/** 查询学习时长汇总数据 */
	public Map<String,Object> getStudyTimeSumByCondition(Map<String, Object> conditions) throws Exception{
		Map parameters = new HashMap();
		
		QueryString hql = new QueryString();
		// 如果是作业
		hql.setSelect("select sum(m.totalTime)/count(m.studyrecordId) ");
		hql.setFrom("from MeleteStudyRecordModel m,MeleteSakaiUserModel su ");
		StringBuffer sb = new StringBuffer();
		
		sb.append(" where su.userId=m.studentId and m.courseId=:courseId   ");
		parameters.put("courseId", conditions.get("courseId"));
		
		if(conditions.get("stuName") != null){
			sb.append(" and su.firstName like :stuName ");
			parameters.put("stuName", "%"+conditions.get("stuName")+"%");
		}
		
		if(conditions.get("stuNum") != null){
			sb.append("and su.publicstunum = :stuNum");
			parameters.put("stuNum", conditions.get("stuNum"));
		}
		
		if (conditions.get("studyStartTime") != null) {
			sb.append(" and m.startStudyTime>=:studyStartTime");
			parameters.put("studyStartTime", conditions.get("studyStartTime"));
		}
		
		if (conditions.get("studyEndTime") != null) {
			sb.append(" and m.startStudyTime<=:studyEndTime");
			parameters.put("studyEndTime", conditions.get("studyEndTime"));
		}
		
		hql.setWhere(sb.toString());//
		List list = this.findEntity(hql.getSql(), parameters);
		Map<String,Object> map = new HashMap<String, Object>();
		if(list != null && !list.isEmpty()){
			Long avgTotalTime = (Long)list.get(0);
			map.put("avgTotalTime", avgTotalTime != null ? avgTotalTime : 0l);
		}
		return map;
	}
	
	/** 查询详细学习情况 */
	public Object[] getStudyInfo(Long studyrecordId,String sort,String dir) throws Exception{
		Object[] results = null;
		List<Object> resultList = null;		
		Map parameters = new HashMap();
		
		QueryString hql = new QueryString();
		hql.setSelect("select new Map(s.title as title,d.startStudyTime as startStudyTime,d.endStudyTime as endStudyTime,d.studyTime as studyTime) ");
		hql.setFrom("from MeleteSectionRecordModel m,MeleteSectionModel s,MeleteSectionRecordDetailModel d ");
		StringBuffer sb = new StringBuffer();
		
		sb.append(" where m.sectionId = s.id and m.sectionrecordId =  d.sectionrecordId and  m.studyrecordId = :studyrecordId ");
		if(StringUtils.isNotBlank(sort) && StringUtils.isNotBlank(dir)){
			if(sort.equals("title")){
				sort = "s.title";
			}
			if(sort.equals("startStudyTime")){
				sort = "d.startStudyTime";
			}
			if(sort.equals("endStudyTime")){
				sort = "d.endStudyTime";
			}
			if(sort.equals("studyTime")){
				sort = "d.studyTime";
			}
			sb.append(" order by " + sort + " " + dir);
		}else{
			sb.append(" order by d.startStudyTime desc");
		}
		
		hql.setWhere(sb.toString());
		parameters.put("studyrecordId", studyrecordId);
		
		Object[] obj = new Object[2];
		List list = this.findEntity(hql.getSql(), parameters);
		obj[0] = list.size();
		obj[1] = list; 
		results= obj;
		
        return results;
	}
	
	/** 查询阶段成绩 */
	public Object[] getTestScoreByCondition(Map<String, Object> conditions,Integer start,String sort,String dir) throws Exception{
		Object[] results = null;
		List<Object> resultList = null;		
		Map parameters = new HashMap();
		
		QueryString hql = new QueryString();
		// 如果是作业
		hql.setSelect("select new Map(m.testrecordId as testrecordId,su.firstName as stuName,su.publicstunum as stuNum,su.sex as sex,m.startStudyTime as startStudyTime,m.attemptNumber as attemptNumber,m.score as score,su.specialtyName as speName,su.studentclassName as className)  ");
		hql.setFrom("from MeleteTestRecordModel m,MeleteSakaiUserModel su,MeleteStudyRecordModel r ");
		StringBuffer sb = new StringBuffer();
		
		sb.append(" where su.userId=r.studentId and r.courseId=:courseId  and r.studyrecordId = m.studyrecordId and m.attemptNumber > 0");
		parameters.put("courseId", conditions.get("courseId"));
		
		if(conditions.get("stuName") != null){
			sb.append(" and su.firstName like :stuName ");
			parameters.put("stuName", "%"+conditions.get("stuName")+"%");
		}
		
		if(conditions.get("stuNum") != null){
			sb.append("and su.publicstunum = :stuNum");
			parameters.put("stuNum", conditions.get("stuNum"));
		}
		
		if (conditions.get("testStartTime") != null) {
			sb.append(" and m.startStudyTime>=:testStartTime");
			parameters.put("testStartTime", conditions.get("testStartTime"));
		}
		
		if (conditions.get("testEndTime") != null) {
			sb.append(" and m.startStudyTime<=:testEndTime");
			parameters.put("testEndTime", conditions.get("testEndTime"));
		}
		
		if (conditions.get("testId") != null) {
			sb.append(" and m.testId=:testId");
			parameters.put("testId", Long.parseLong((String)conditions.get("testId")));
		}
		
		if (conditions.get("scoreStart") != null) {
			sb.append(" and m.score>=:scoreStart");
			parameters.put("scoreStart", Float.valueOf(String.valueOf(conditions.get("scoreStart"))));
		}
		
		if (conditions.get("scoreEnd") != null) {
			sb.append(" and m.score<=:scoreEnd"); 
			parameters.put("scoreEnd", Float.valueOf(String.valueOf(conditions.get("scoreEnd"))));
		}
		
		if(StringUtils.isNotBlank(sort) && StringUtils.isNotBlank(dir)){
			if(sort.equals("stuName")){
				sort = "su.firstName";
			}
			if(sort.equals("stuNum")){
				sort = "su.publicstunum";
			}
			if(sort.equals("sex")){
				sort = "su.sex";
			}
			if(sort.equals("attemptNumber")){
				sort = "m.attemptNumber";
			}
			if(sort.equals("score")){
				sort = "m.score";
			}
			sb.append(" order by " + sort + " " + dir);
		}else{
			sb.append(" order by m.score desc");
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
	
	/** 查询阶段成绩汇总数据 */
	public Map<String,Object> getTestScoreSumByCondition(Map<String, Object> conditions) throws Exception{
		Map parameters = new HashMap();
		
		QueryString hql = new QueryString();
		// 如果是作业
		hql.setSelect("select sum(m.attemptNumber)/count(m.testrecordId) as avgCount,sum(m.score)/count(m.testrecordId) as avgScore");
		hql.setFrom("from MeleteTestRecordModel m,MeleteSakaiUserModel su,MeleteStudyRecordModel r ");
		StringBuffer sb = new StringBuffer();
		
		sb.append(" where su.userId=r.studentId and r.courseId=:courseId   and r.studyrecordId = m.studyrecordId and m.attemptNumber > 0");
		parameters.put("courseId", conditions.get("courseId"));
		
		if(conditions.get("stuName") != null){
			sb.append(" and su.firstName like :stuName ");
			parameters.put("stuName", "%"+conditions.get("stuName")+"%");
		}
		
		if(conditions.get("stuNum") != null){
			sb.append("and su.publicstunum = :stuNum");
			parameters.put("stuNum", conditions.get("stuNum"));
		}
		
		if (conditions.get("testStartTime") != null) {
			sb.append(" and m.startStudyTime>=:testStartTime");
			parameters.put("testStartTime", conditions.get("testStartTime"));
		}
		
		if (conditions.get("testEndTime") != null) {
			sb.append(" and m.startStudyTime<=:testEndTime");
			parameters.put("testEndTime", conditions.get("testEndTime"));
		}
		
		if (conditions.get("testId") != null) {
			sb.append(" and m.testId=:testId");
			parameters.put("testId", Long.parseLong((String)conditions.get("testId")));
		}
		
		if (conditions.get("scoreStart") != null) {
			sb.append(" and m.score>=:scoreStart");
			parameters.put("scoreStart", Float.valueOf(String.valueOf(conditions.get("scoreStart"))));
		}
		
		if (conditions.get("scoreEnd") != null) {
			sb.append(" and m.score<=:scoreEnd"); 
			parameters.put("scoreEnd", Float.valueOf(String.valueOf(conditions.get("scoreEnd"))));
		}
		
		hql.setWhere(sb.toString());//
		List list = this.findEntity(hql.getSql(), parameters);
		Map<String,Object> map = new HashMap<String, Object>();
		if(list != null && !list.isEmpty()){
			Object[] o = (Object[])list.get(0);
			Double avgScore = ((o != null && o[1] != null) ? (Double)o[1] : 0);
			map.put("avgCount", (o != null && o[0] != null) ? o[0] : 0l);
			map.put("avgScore", avgScore.intValue());
		}
		return map;
	}
	
	/** 查询详细阶段成绩情况 */
	public Object[] getTestScoreInfo(Long testrecordId,String sort,String dir) throws Exception{
		Object[] results = null;
		List<Object> resultList = null;		
		Map parameters = new HashMap();
		
		QueryString hql = new QueryString();
		hql.setSelect("select new Map(s.studentId as userId,r.testId as testId,d.testattemptId as testattemptId,d.startTime as startTime,d.endTime as endTime,d.objScore as objScore,d.subScore as subScore,d.testPaperid as testPaperid,d.score as score) ");
		hql.setFrom("from MeleteTestAttemptModel d,MeleteTestRecordModel r,MeleteStudyRecordModel s ");
		StringBuffer sb = new StringBuffer();
		
		sb.append(" where d.meleteTestRecordId = :testrecordId and d.meleteTestRecordId = r.testrecordId and s.studyrecordId = r.studyrecordId ");
		if(StringUtils.isNotBlank(sort) && StringUtils.isNotBlank(dir)){
			if(sort.equals("startTime")){
				sort = "d.startTime";
			}
			if(sort.equals("endTime")){
				sort = "d.endTime";
			}
			if(sort.equals("score")){
				sort = "d.score";
			}
			sb.append(" order by " + sort + " " + dir);
		}else{
			sb.append(" order by d.orderIndex asc");
		}
		
		hql.setWhere(sb.toString());
		parameters.put("testrecordId", testrecordId);
		
		Object[] obj = new Object[2]; 
		List list = this.findEntity(hql.getSql(), parameters);
		obj[0] = list.size();
		obj[1] = list; 
		results= obj;
		
        return results;
	}
	
	/** 查询总成绩 */
	public Object[] getScoreByCondition(Map<String, Object> conditions,Integer start,String sort,String dir) throws Exception{
		Object[] results = null;
		List<Object> resultList = null;		
		Map parameters = new HashMap();
		
		QueryString hql = new QueryString();
		// 如果是作业
		hql.setSelect("select new Map(m.studyrecordId as studyrecordId,su.firstName as stuName,su.publicstunum as stuNum,su.sex as sex,m.score as score,su.specialtyName as speName,su.studentclassName as className) ");
		hql.setFrom("from MeleteSakaiUserModel su,MeleteStudyRecordModel m ");
		StringBuffer sb = new StringBuffer();
		
		sb.append(" where su.userId=m.studentId and m.courseId=:courseId   ");
		parameters.put("courseId", conditions.get("courseId"));
		
		if(conditions.get("stuName") != null){
			sb.append(" and su.firstName like :stuName ");
			parameters.put("stuName", "%"+conditions.get("stuName")+"%");
		}
		
		if(conditions.get("stuNum") != null){
			sb.append("and su.publicstunum = :stuNum");
			parameters.put("stuNum", conditions.get("stuNum"));
		}
		
		if (conditions.get("scoreStart") != null) {
			sb.append(" and m.score>=:scoreStart");
			parameters.put("scoreStart", Float.valueOf(String.valueOf(conditions.get("scoreStart"))));
		}
		
		if (conditions.get("scoreEnd") != null) {
			sb.append(" and m.score<=:scoreEnd"); 
			parameters.put("scoreEnd", Float.valueOf(String.valueOf(conditions.get("scoreEnd"))));
		}
		
		if(StringUtils.isNotBlank(sort) && StringUtils.isNotBlank(dir)){
			if(sort.equals("stuName")){
				sort = "su.firstName";
			}
			if(sort.equals("stuNum")){
				sort = "su.publicstunum";
			}
			if(sort.equals("sex")){
				sort = "su.sex";
			}
			if(sort.equals("score")){
				sort = "m.score";
			}
			sb.append(" order by " + sort + " " + dir);
		}else{
			sb.append(" order by m.score desc");
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
	
	/** 查询总成绩汇总数据 */
	public Map<String,Object> getScoreSumByCondition(Map<String, Object> conditions) throws Exception{
		Map parameters = new HashMap();
		
		QueryString hql = new QueryString();
		// 如果是作业
		hql.setSelect("select sum(m.score)/count(m.studyrecordId) as avgScore");
		hql.setFrom("from MeleteSakaiUserModel su,MeleteStudyRecordModel m ");
		StringBuffer sb = new StringBuffer();
		
		sb.append(" where su.userId=m.studentId and m.courseId=:courseId   ");
		parameters.put("courseId", conditions.get("courseId"));
		
		if(conditions.get("stuName") != null){
			sb.append(" and su.firstName like :stuName ");
			parameters.put("stuName", "%"+conditions.get("stuName")+"%");
		}
		
		if(conditions.get("stuNum") != null){
			sb.append("and su.publicstunum = :stuNum");
			parameters.put("stuNum", conditions.get("stuNum"));
		}
		
		if (conditions.get("scoreStart") != null) {
			sb.append(" and m.score>=:scoreStart");
			parameters.put("scoreStart", Float.valueOf(String.valueOf(conditions.get("scoreStart"))));
		}
		
		if (conditions.get("scoreEnd") != null) {
			sb.append(" and m.score<=:scoreEnd"); 
			parameters.put("scoreEnd", Float.valueOf(String.valueOf(conditions.get("scoreEnd"))));
		}
		
		hql.setWhere(sb.toString());//
		List list = this.findEntity(hql.getSql(), parameters);
		Map<String,Object> map = new HashMap<String, Object>();
		if(list != null && !list.isEmpty()){
			Double avgTotalTime = (Double)list.get(0);
			map.put("avgScore", avgTotalTime != null ? avgTotalTime.intValue() : 0);
		}
		return map;
	}
	
	/** 查询详细总成绩情况 */
	public Object[] getScoreInfo(Long studyrecordId,String sort,String dir) throws Exception{
		Object[] results = null;
		List<Object> resultList = null;		
		Map parameters = new HashMap();
		
		QueryString hql = new QueryString();
		hql.setSelect("select new Map(t.name as name,t.ratio as ratio,m.attemptNumber as attemptNumber,m.score as score) ");
		hql.setFrom("from MeleteTestRecordModel m,MeleteTestModel t ");
		StringBuffer sb = new StringBuffer();
		
		sb.append(" where m.testId = t.id and m.studyrecordId = :studyrecordId ");
		if(StringUtils.isNotBlank(sort) && StringUtils.isNotBlank(dir)){
			if(sort.equals("name")){
				sort = "t.name";
			}
			if(sort.equals("attemptNumber")){
				sort = "m.attemptNumber";
			}
			if(sort.equals("score")){
				sort = "m.score";
			}
			sb.append(" order by " + sort + " " + dir);
		}else{
			sb.append(" order by t.idx asc");
		}
		
		hql.setWhere(sb.toString());
		parameters.put("studyrecordId", studyrecordId);
		
		Object[] obj = new Object[2];
		List list = this.findEntity(hql.getSql(), parameters);
		obj[0] = list.size();
		obj[1] = list; 
		results= obj;
		
        return results;
	}
}
