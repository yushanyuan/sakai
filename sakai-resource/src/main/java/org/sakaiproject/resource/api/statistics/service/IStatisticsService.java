package org.sakaiproject.resource.api.statistics.service;

import java.util.List;
import java.util.Map;

public interface IStatisticsService {
	/**查询活动成绩 1 代表作业，2代表自测*/
	public List<Map<String,Object>> findActivityScore(Map<String,Object> conditions, int activity) throws Exception;
	/**统计模块平均通过时间*/
	public List<Map<String,Object>> statModelAvgThroughTime(String siteId) throws Exception;
	/**统计活动(作业)平均次数和成绩*/
	public List<Map<String,Object>> statActivityAvgTimeAndScoreOfTestRecord(String siteId) throws Exception;
	/**统计活动(前测)平均次数和成绩*/
	public List<Map<String,Object>> statActivityAvgTimeAndScoreOfSelfTestRecord(String siteId) throws Exception;
	/**统计活动(论坛)平均次数和成绩*/
	public List<Map<String,Object>> statActivityAvgTimeAndScoreOfForumRecord(String siteId) throws Exception;
	/**统计学生作业提交情况*/
	public List<Map<String,Object>> statStudentOfTestRecord(String siteId) throws Exception;
	/**统计学生前测情况*/
	public List<Map<String,Object>> statStudentOfSelfTestRecord(String siteId) throws Exception;
	/**统计学生论坛参与情况*/
	public List<Map<String,Object>> statStudentOfForumRecord(String siteId) throws Exception;
	/**
	 * 根据站点ID查询所有的章节
	 * @param bl 是否只查询最外层章节点，如果是否则只查询枝节点，如果为空就全查
	 * */
	public List<Map<String,Object>> getModuleBySiteId(String siteId,Boolean bl) throws Exception;
	/**根据站点ID查询所有叶节点*/
	public List<Map<String,Object>> getSectionBySiteId(String siteId) throws Exception;
	/**根据站点ID查询所有作业*/
	public List<Map<String,Object>> getTestBySIiteId(String siteId) throws Exception;
	/**根据站点ID查询所有前测*/
	public List<Map<String,Object>> getSelfTestBySIiteId(String siteId) throws Exception;
	/**根据站点ID查询所有论坛*/
	public List<Map<String,Object>> getForumBySIiteId(String siteId) throws Exception;
}
