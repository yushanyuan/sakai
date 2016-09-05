package com.bupticet.paperadmin.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author admin
 * 一份卷子的答案模型;
 */
public class AnswerToPaper implements Serializable{
	private static final long serialVersionUID = -94068853661593368L;
	private int id;
	private List<AnswerToQuestion> list;//代表每一道题的答案；
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<AnswerToQuestion> getList() {
		return list;
	}
	public void setList(List<AnswerToQuestion> list) {
		this.list = list;
	}
	
}
