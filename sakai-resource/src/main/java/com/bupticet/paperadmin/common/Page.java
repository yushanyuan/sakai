package com.bupticet.paperadmin.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Page implements Serializable {

	// to Define a Empty Page Object

	public static final Page EMPTY_PAGE = new Page(0, 0, 0, false,
			Collections.EMPTY_LIST);

	private List objects;// 当前页记录列表

	private int start;// 当前页首记录在数据库查询结果记录集中的位置，从0开始

	private int currentPageNO;// 当前页码，从1开始

	private int pageSum;// 总页数

	private boolean hasNext;// 是否有下一页

	/**
	 * 
	 * 
	 * Page(int pageSum,int start,int currentPageNO,boolean hasNext,List
	 * objects):
	 * 
	 * 
	 * 构造函数，封装页面记录信息
	 * 
	 * 
	 */

	public Page(int pageSum, int start, int currentPageNO, boolean hasNext,
			List objects)

	{

		this.pageSum = pageSum;

		this.start = start;

		this.currentPageNO = currentPageNO;

		this.hasNext = hasNext;

		this.objects = new ArrayList(objects);

	}

	/**
	 * 
	 * 
	 * public List getList():
	 * 
	 * 
	 * 获取记录列表对象
	 * 
	 * 
	 */

	// public int getStart(){return start;}

	public List getList() {
		return objects;
	}

	/**
	 * 
	 * 
	 * public boolean isNextPageAvailable():
	 * 
	 * 
	 * 获取是否还有下一页
	 * 
	 * 
	 */

	public boolean isNextPageAvailable() {
		return hasNext;
	}

	/**
	 * 
	 * 
	 * public boolean isNextPageAvailable():
	 * 
	 * 
	 * 获取是否有前一页
	 * 
	 * 
	 */

	public boolean isPreviousPageAvailable() {
		return start > 0;
	}

	/**
	 * 
	 * 
	 * public int getStartOfNextPage():
	 * 
	 * 
	 * 获取下一页记录的开始序号
	 * 
	 * 
	 */

	public int getStartOfNextPage() {
		return start + objects.size();
	}

	/**
	 * 
	 * 
	 * public int getStartOfPreviousPage():
	 * 
	 * 
	 * 获取前一页记录的开始序号
	 * 
	 * 
	 */

	public int getStartOfPreviousPage(String count) {
		return Math.max(start - (Integer.parseInt(count)), 0);
	}

	/**
	 * 
	 * 
	 * public int getSize():
	 * 
	 * 
	 * 获取本页记录条数
	 * 
	 * 
	 */

	public int getSize() {
		return objects.size();
	}

	/**
	 * 
	 * 
	 * public int getPageSum():
	 * 
	 * 
	 * 获取要显示的总页数
	 * 
	 * 
	 */

	public int getPageSum() {
		return pageSum;
	}

	/**
	 * 
	 * 
	 * public int getCurrentPageNO():
	 * 
	 * 
	 * 获取当前页码
	 * 
	 * 
	 */

	public int getCurrentPageNO() {
		return currentPageNO;
	}

}
