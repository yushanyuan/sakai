package org.sakaiproject.resource.api.study.model;

import java.util.Date;

/**
 * 学习历史记录，用于记录学习位置
 * 
 * @author zihongyan 2013-3-21
 * 
 */
public class MeleteStudyHistoryRecordModel implements java.io.Serializable {

	/** 学习历史记录id */
	private Integer studyHistoryRecordId;

	/** 学生id */
	private String studentId;

	/** 课程id */
	private String courseId;

	/** 节点id 用于记录浏览的章节 */
	private String nodeId;

	/** 学习记录id */
	private String studyRecordId;

	/** 序号 :旧课程空间使用 */
	private String seqNo;

	/** 1旧课程空间2新课程空间 */
	private String type;

	/** 时间 */
	private Date historyDate = new Date();

	public Date getHistoryDate() {
		return historyDate;
	}

	public void setHistoryDate(Date historyDate) {
		this.historyDate = historyDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}

	

	public Integer getStudyHistoryRecordId() {
		return studyHistoryRecordId;
	}

	public void setStudyHistoryRecordId(Integer studyHistoryRecordId) {
		this.studyHistoryRecordId = studyHistoryRecordId;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getStudyRecordId() {
		return studyRecordId;
	}

	public void setStudyRecordId(String studyRecordId) {
		this.studyRecordId = studyRecordId;
	}
}
