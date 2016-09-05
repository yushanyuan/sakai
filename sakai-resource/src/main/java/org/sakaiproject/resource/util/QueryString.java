package org.sakaiproject.resource.util;

/**
 * 查询对象。一个查询对象由五部分组成：select + from + where + order + group。
 * @author 胡浩 
 * @FIXME 当order和group中有参数时，会导致查询出错。
 */
public class QueryString {
	private String select = "";
	private String from = "";
	private String where = "";
	private String order = "";
	private String group = "";

	public String getSelect() {
		return select;
	}

	public void setSelect(String select) {
		this.select = select;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}

	public void appendWhere(String w) {
		this.where += " " + w;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getSql() {
		return select + " " + from + " " + where + " " + order + " " + group;
	}

	public String getCountSql() {
		return "select count(*) " + from + " " + where;
	}
}
