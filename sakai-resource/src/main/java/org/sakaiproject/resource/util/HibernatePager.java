package org.sakaiproject.resource.util;

import java.util.List;

public class HibernatePager {
	private int pageSize = 0;//每页记录数
	private int pageNo = 0;//当前页码
	private int count = 0;//记录总数
	private int pageCount = 0;//页总数
	private List result = null;//结果list

	/**
	 * 创建分页对象
	 * 
	 * @param pageSize
	 *            每页显示记录数
	 * @param pageNo
	 *            当前页码
	 * @param count
	 *            总记录数
	 * @param result
	 *            分页结果
	 */
	public HibernatePager(int pageSize, int pageNo, int count, List result) {
		this.pageSize = pageSize;
		this.pageNo = pageNo;
		this.count = count;
		this.pageCount = (int) Math.ceil((float) count / pageSize);
		this.result = result;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List getResult() {
		return result;
	}

	public void setResult(List result) {
		this.result = result;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

}
