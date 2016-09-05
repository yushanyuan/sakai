package org.sakaiproject.resource.util;

import org.apache.struts2.ServletActionContext;

public class CodeTable {
	public final static String required = "1"; // 必修

	public final static String elective = "2"; // 选修

	public final static String defaultImpression = "10"; // 默认印象分

	public final static String auto = "1"; // 自动加分

	public final static String searchStuID = "2"; // 查询学号加分

	public final static String template_default = "default"; // 默认播放器
	
	public final static String template_hill = "hill";//播放模板为山坡
	
	public final static String template_red = "kecheng-R";//播放模板为"模板-红"
	
	public final static String template_green = "kecheng-G";//播放模板为"模板-绿"
	
	public final static String template_blue = "kecheng-B";//播放模板为"模板-蓝"

	public final static String del = "-1";// 删除态

	public final static String hide = "0";// 隐藏态

	public final static String normal = "1";// 正常态

	public final static String stop = "2";// 停用态

	public final static String IsCaculateScoreYes = "1";// 计算成绩

	public final static String IsCaculateScoreNo = "2";// 不计算成绩

	public final static String prerequisiteYes = "1";// 有开启条件

	public final static String prerequisiteNo = "2";// 无开启条件

	public final static String course = "1";// 树节点为课程

	public final static String module = "2";// 树节点为模块

	public final static String section = "3";// 树节点为页

	public final static String test = "4";// 树节点为作业

	public final static String selftest = "6";// 树节点为前测

	public final static String forum = "5";// 树节点为讨论

	public final static String file = "7";// 树节点为资源

	public final static String belongMudole = "1";// 所属类型为节点

	public final static String belongSection = "2";// 所属类型为页

	public final static String checkstatusYes = "1";// 已批改

	public final static String checkstatusNo = "2";// 未批改

	public final static String passStatusYes = "1";// 通过

	public final static String passStatusNo = "2";// 未通过

	public final static String openStatusYes = "1";// 开启

	public final static String openStatusNo = "2";// 未开启

	public final static String samePaperYes = "1";// 同一策略

	public final static String samePaperNo = "2";// 不同策略

	public final static String subCheckNo = "0";// 主观题未批改

	public final static String subCheckYes = "1";// 主观题已经批改

	public final static String subNotExist = "2";// 无主观题

	public final static String paperTypeTest = "1";// 测试类型为作业
	
	public final static String paperTypeSelfTest = "2";// 测试类型为前测
	
	public final static String showStuStatusNo = "0";//在学生界面不显示通过状态
	
	public final static String showStuStatusYes = "1";//在学生界面显示通过状态
	
	public final static String updateType = "update";//更改类型为 更改
	
	public final static String addType = "add";//更改类型为 添加
	
	public final static String icoCourse = "resource/icons/folder_empty.png";// 课程图标

	public final static String icoModule = "resource/icons/module.gif";// 模块图标

	public final static String icoHide = "resource/icons/module_hide.gif";// 隐藏模块图标

	public final static String icoSection = "resource/icons/section.gif";// 页图标

	public final static String icoTest = "resource/icons/test.gif";// 作业图标

	public final static String icoSelftest = "resource/icons/selftest.gif";// 前测图标

	public final static String icoForum = "resource/icons/forum.gif";// 讨论图标

	public final static String icoPass = "resource/icons/pass.gif";// 通过图标

	public final static String icoNoPass = "resource/icons/no_pass.png";// 未通过图标

	public final static String icoOpen = "resource/icons/open.png";// 开启图标

	public final static String icoNoOpen = "resource/icons/no_open.png";// 未开启图标

	public final static String icoRequired = "resource/icons/required.gif";// 必修图标

	public final static String icoElective = "resource/icons/elective.gif";// 选修图标

	public final static String icoLucency = "resource/images/default/s.gif";// 通用透明图标

	public final static String greaterThan = "1";//大于
	
	public final static String equals = "2";//等于
	
	public final static String lessThen = "3";//小于
	
	/**
	 * 资源类型
	 */
	public final static String RESOURCE_TYPE_UNKNOWN = "0";
	public final static String RESOURCE_TYPE_VIDEO = "1";
	public final static String RESOURCE_TYPE_AUDIO = "2";
	public final static String RESOURCE_TYPE_IMAGE = "3";
	
	public final static String getRequiredIco() {
		String path = ServletActionContext.getRequest().getContextPath();
		String ico = "<img ext:qtip=\'必修\' src=\'" + icoRequired + "\'>";
		return ico;
	}

	public final static String getElectiveIco() {
		String ico = "<img ext:qtip=\'选修\' src=\'" + icoElective + "\'>";
		return ico;
	}

	public final static String getPassIco() {
		String ico = "<img ext:qtip=\'通过\' src=\'" + icoPass + "\'>";
		return ico;
	}

	public final static String getNoPassIco() {
		String ico = "<img ext:qtip=\'未通过\' src=\'" + icoNoPass + "\'>";
		return ico;
	}

	public final static String getOpenIco() {
		String ico = "<img ext:qtip=\'开启\' src=\'" + icoOpen + "\'>";
		return ico;
	}

	public final static String getNoOpenIco() {
		String ico = "<img ext:qtip=\'未开启\' src=\'" + icoNoOpen + "\'>";
		return ico;
	}

}
