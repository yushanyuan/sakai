package com.bupticet.paperadmin.model;

/**
 * @author admin 涓?閬撹瘯棰樼殑绛旀;
 */
public class AnswerToQuestion {
	
	public static String SCORE="score";//标准分
	public static String KNOWID="knowId";//知识点id；
	public static String KNOWNAME="knowName";//知识点名；
	public static String ID="id";//试题id
	public static String ANSWER="answer";//标准答案
	public static String STUDENTANSWER="studentAnswer";//学生答案
	public static String TYPEID="typeId";//题型；例如：单选、多选等；
	public static String STUDENTSCORE="studentScore";//学生得分；
	public static String ISOBJECT="isobject";//是否是主观题；（0:false;1:true）
	public static String ISRIGHT="isright";//是否正确；（0:false;1:true）

	
	private int id;// 试题id
	
	private float score;// 试题分数

	private String[] answer;// 答案

	private int isobject; // 是否主观题

	private int typeId;// 试题类型id；

	private int knowId;//知识点id；
	
	private String knowName;//知识点名；
	
	public String[] getAnswer() {
		return answer;
	}

	public void setAnswer(String[] answer) {
		this.answer = answer;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public String getKnowName() {
		return knowName;
	}

	public void setKnowName(String knowName) {
		this.knowName = knowName;
	}

	public int getKnowId() {
		return knowId;
	}

	public void setKnowId(int knowId) {
		this.knowId = knowId;
	}

	public int getIsobject() {
		return isobject;
	}

	public void setIsobject(int isobject) {
		this.isobject = isobject;
	}
}
