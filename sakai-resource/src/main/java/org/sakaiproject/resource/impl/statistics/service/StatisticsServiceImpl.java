package org.sakaiproject.resource.impl.statistics.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sakaiproject.resource.api.statistics.service.IStatisticsService;
import org.sakaiproject.resource.util.CodeTable;
import org.sakaiproject.resource.util.HibernateDaoSupport;

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
}
