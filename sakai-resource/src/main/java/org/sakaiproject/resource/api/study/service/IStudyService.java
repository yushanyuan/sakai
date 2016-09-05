package org.sakaiproject.resource.api.study.service;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.sakaiproject.resource.api.study.model.MeleteForumRecordModel;
import org.sakaiproject.resource.api.study.model.MeleteModuleRecordModel;
import org.sakaiproject.resource.api.study.model.MeleteSectionRecordDetailModel;
import org.sakaiproject.resource.api.study.model.MeleteSectionRecordModel;
import org.sakaiproject.resource.api.study.model.MeleteSelftestAttemptModel;
import org.sakaiproject.resource.api.study.model.MeleteSelftestRecordModel;
import org.sakaiproject.resource.api.study.model.MeleteStudyHistoryRecordModel;
import org.sakaiproject.resource.api.study.model.MeleteStudyRecordModel;
import org.sakaiproject.resource.api.study.model.MeleteTestAttemptModel;
import org.sakaiproject.resource.api.study.model.MeleteTestRecordModel;

public interface IStudyService {
	/**
	 * 保存学习记录信息
	 * 
	 * @param course
	 * @throws Exception
	 */
	public String saveStudyRecord(MeleteStudyRecordModel studyRecord) throws Exception;

	/**
	 * 保存模块记录信息
	 * 
	 * @param course
	 * @throws Exception
	 */
	public void saveModuleRecord(MeleteModuleRecordModel moduleRecord) throws Exception;

	/**
	 * 保存节点记录信息
	 * 
	 * @param course
	 * @throws Exception
	 */
	public void saveSectionRecord(MeleteSectionRecordModel sectionRecord) throws Exception;

	/**
	 * 保存作业尝试记录信息
	 * 
	 * @param course
	 * @throws Exception
	 */
	public void saveTestAttempt(MeleteTestAttemptModel testAttempt) throws Exception;

	/**
	 * 保存作业记录信息
	 * 
	 * @param course
	 * @throws Exception
	 */
	public void saveTestRecord(MeleteTestRecordModel testRecord) throws Exception;

	/**
	 * 保存前测尝试记录信息
	 * 
	 * @param course
	 * @throws Exception
	 */
	public void saveSelftestAttempt(MeleteSelftestAttemptModel selftestAttempt) throws Exception;

	/**
	 * 保存前测记录信息
	 * 
	 * @param course
	 * @throws Exception
	 */
	public void saveSelftestRecord(MeleteSelftestRecordModel selftestRecord) throws Exception;

	/**
	 * 保存讨论记录信息
	 * 
	 * @param course
	 * @throws Exception
	 */
	public void saveForumRecord(MeleteForumRecordModel forumRecord) throws Exception;

	/**
	 * 保存节点记录详细信息
	 * 
	 * @param sectionDetail
	 * @throws Exception
	 */
	public void saveSectionRecordDetail(MeleteSectionRecordDetailModel sectionDetail) throws Exception;

	/**
	 * 根据学习记录Id和模块Id查询是否有对应的模块记录信息
	 * 
	 * @param studyrecordId
	 * @param moduleId
	 * @return
	 * @throws Exception
	 */
	public MeleteModuleRecordModel getModuleRecordById(String studyrecordId, String moduleId) throws Exception;

	/**
	 * 根据学习记录Id和节点Id查询是否有对应的节点记录信息
	 * 
	 * @param studyrecordId
	 * @param sectionId
	 * @return
	 * @throws Exception
	 */
	public MeleteSectionRecordModel getSectionRecordById(String studyrecordId, String sectionId) throws Exception;

	/**
	 * 根据id获取学习记录
	 * 
	 * @param studyRecordId
	 * @return
	 */
	public MeleteStudyRecordModel getStudyRecordById(String studyRecordId);

	/**
	 * 根据课程Id与学生Id获取学习记录信息
	 * 
	 * @param courseId
	 *            课程Id
	 * @param studentId
	 *            学生Id
	 * @return
	 * @throws Exception
	 */
	public MeleteStudyRecordModel getStudyRecordById(String courseId, String studentId) throws Exception;
	
	/**
	 * 根据条件获取学习记录信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public Object[] getStudyRecordByCondtion(String courseId,Date startTime,Date endTime,String stuNum,String stuName,Integer start,String sort,String dir) throws Exception;

	/**
	 * 修改一个对象
	 * 
	 * @param o
	 * @throws Exception
	 */
	public void updateModel(Object o) throws Exception;

	/**
	 * 根据节点学习记录获取节点学习详细记录信息列表
	 * 
	 * @param sectionrecordId
	 *            节点学习记录Id
	 * @return
	 * @throws Exception
	 */
	public List<MeleteSectionRecordDetailModel> getSectionRecordDetailList(String sectionrecordId) throws Exception;

	/**
	 * 根据主键Id获取节点记录明细对象
	 * 
	 * @param detailRecordId
	 * @return
	 * @throws Exception
	 */
	public MeleteSectionRecordDetailModel getSectionRecordDetailById(String detailRecordId) throws Exception;

	/**
	 * 根据主键Id获取节点记录对象
	 * 
	 * @param detailRecordId
	 * @return
	 * @throws Exception
	 */
	public MeleteSectionRecordModel getSectionRecordById(Long sectionRecordId) throws Exception;

	/**
	 * 根据主键Id获取模块记录对象
	 * 
	 * @param detailRecordId
	 * @return
	 * @throws Exception
	 */
	public MeleteModuleRecordModel getModuleRecordById(Long moduleRecordId) throws Exception;

	/**
	 * 根据学习记录Id和讨论Id查询讨论记录信息
	 * 
	 * @param studyrecordId
	 * @param forumId
	 * @return
	 * @throws Exception
	 */
	public MeleteForumRecordModel getForumRecordById(String studyrecordId, String forumId) throws Exception;

	/**
	 * 根据学习记录Id和作业Id查询讨论记录信息
	 * 
	 * @param studyrecordId
	 * @param testId
	 * @return
	 * @throws Exception
	 */
	public MeleteTestRecordModel getTestRecordById(String studyrecordId, String testId) throws Exception;

	/**
	 * 根据学习记录Id和前测Id查询讨论记录信息
	 * 
	 * @param selftestId
	 * @param testId
	 * @return
	 * @throws Exception
	 */
	public MeleteSelftestRecordModel getSelftestRecordById(String studyrecordId, String selftestId) throws Exception;

	/**
	 * 保存提交的作业
	 * 
	 * @param courseId
	 * @param recordId
	 * @param userId
	 * @param paperId
	 * @param answer
	 * @param startTime
	 * @param passScore
	 * @param studyrecordId
	 *            学习记录ID
	 * @param testId
	 *            作业ID
	 * @throws Exception
	 */
	public MeleteTestAttemptModel saveTestAttempt(String courseId, String recordId, String userId, String paperId,
			String answer, String startTime, String passScore, String studyrecordId, String testId) throws Exception;

	/**
	 * 保存提交的前测
	 * 
	 * @param courseId
	 * @param recordId
	 * @param userId
	 * @param paperId
	 * @param answer
	 * @param startTime
	 * @param passScore
	 * @param studyrecordId
	 *            学习记录ID
	 * @param selfTestId
	 *            前测ID
	 * @return
	 * @throws Exception
	 */
	public MeleteSelftestAttemptModel saveSelftestAttempt(String courseId, String recordId, String userId,
			String paperId, String answer, String startTime, String passScore, String studyrecordId, String selfTestId)
			throws Exception;

	/**
	 * 保存提交的讨论
	 * 
	 * @param courseId
	 * @param userId
	 * @param recordId
	 * @param forumId
	 * @param studyrecordId
	 *            //学习记录ID
	 * @throws Exception
	 */
	public void saveForumRecord(String courseId, String userId, String topicId, String studyrecordId) throws Exception;

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
	 *            批改的测试类型
	 * @throws Exception
	 */
	public Object checkTestSave(String attemptId, String userid, File studentAnswerFile, String courseId,
			String paperType, String studyrecordId) throws Exception;

	/**
	 * 计算平时成绩
	 * 
	 * @throws Exception
	 */
	public void countUsuallyScore(String courseId, String userId, String studyrecordId) throws Exception;
	
	/**
	 * 批量重新计算平时成绩
	 * @param studyrecordIds
	 * @throws Exception
	 */
	public void updateScoreByStudyRecordIds(String studyrecordIds)throws Exception;

	/**
	 * 根据页节点ID 学习记录ID 检查并更改对应的页节点是否通过
	 * 
	 * @param sectionId
	 *            页节点ID
	 * @param studyrecordId
	 *            学习记录ID
	 * @param checkAct
	 *            是否检查页节点下活动是否通过 以及时长是否大于规定时长
	 * @return 是否通过的检查结果
	 * @throws Exception
	 */
	public boolean checkSectionPassStatus(Long sectionId, Long studyrecordId, boolean checkAct) throws Exception;

	/**
	 * 根据模块节点ID 学习记录ID 检查并更改对应的模块节点是否通过
	 * 
	 * @param moduleId
	 *            模块节点ID
	 * @param studyrecordId
	 *            学习记录ID
	 * @param checkAct
	 *            是否检查模块节点下活动是否通过
	 * @return 是否通过的检查结果
	 * @throws Exception
	 */
	public boolean checkModulePassStatus(Long moduleId, Long studyrecordId, boolean checkAct) throws Exception;

	/**
	 * 通过作业记录ID 获得 作业记录
	 * 
	 * @param testRecordId
	 * @return
	 * @throws Exception
	 */
	public MeleteTestRecordModel getTestRecordByTestRecordId(String testRecordId) throws Exception;

	/**
	 * 通过前测记录Id 获得前测记录
	 * 
	 * @return
	 * @throws Exception
	 */
	public MeleteSelftestRecordModel getSelfTestRecordBySelfTestRecordId(String selfTestRecordId) throws Exception;

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
	public Object getSectionOrModuleByActivityId(String activityId, String activityType, String studyrecordId)
			throws Exception;

	/**
	 * 通过moduleID studyrecordId 获得模块下的所有作业和前测尝试记录
	 * 
	 * @param moduleId
	 * @param studyrecordId
	 * @return
	 * @throws Exception
	 */
	public List<Object> getAttemptByModuleIdAndStudyRecordId(String moduleId, String studyrecordId) throws Exception;

	public List getActivityValue(String sectionRecordId, String activityId) throws Exception;

	public void setActivityValue(String sectionRecordId, String activityId, String activityValue) throws Exception;

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
	public boolean addImpScore(String[] selectionIds, String[] scores, String impScore) throws Exception;

	/**
	 * 用于保存学习历史记录
	 * 
	 * @author zihongyan 2013-3-21
	 * @param stuHistoryRecord
	 *            学习历史记录实体
	 * @return 保存信息
	 */
	public String saveStudyHistoryRecord(MeleteStudyHistoryRecordModel stuHistoryRecord);

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
	public MeleteStudyHistoryRecordModel findStudyHistortRecordByStudentIDAndCourseId(String studentId, String courseId);

	/**
	 * 根据学生的id和课程id删除浏览历史记录
	 * 
	 * @author zihongyan 2013-3-21
	 * @param studentId
	 *            学生id
	 * @param courseId
	 *            课程id
	 */
	public void deleteStudyHistoryRecordByStudentIdAndCourseId(String studentId, String courseId);

	/**
	 * 根据学习记录Id查询作业记录信息
	 * 
	 * @param studyrecordId
	 * @param testId
	 * @return
	 * @throws Exception
	 */
	public List<MeleteTestRecordModel> getTestRecordByStudyrecordId(String studyrecordId) throws Exception;
}
