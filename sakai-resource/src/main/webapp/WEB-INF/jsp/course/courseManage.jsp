<%@page import="org.sakaiproject.resource.util.Constants"%>
<%@ page language="java" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String tplsJSON = (String)request.getSession().getAttribute("tplsJSON");
	String userName = session.getAttribute("userName").toString().trim();
	String userId = session.getAttribute("userEId").toString().trim();

	String exCourseId = session.getAttribute("exCourseId")==null?"":session.getAttribute("exCourseId").toString();
	String examSysEntryUrl = org.sakaiproject.resource.util.CourseUtil.getExamSysEntryUrl(userId,exCourseId);
	String examSysSchemaUrl = org.sakaiproject.resource.util.CourseUtil.getExamSchemaEntryUrl(userId,exCourseId);
	String useExamSys = "true";
	if(examSysEntryUrl == null){
		useExamSys = "false";
	}
	
	String resCourseId = session.getAttribute("resCourseId")==null?"":session.getAttribute("resCourseId").toString();
	String resSysEntryUrl = org.sakaiproject.resource.util.CourseUtil.getResSysEntryUrl(userId,resCourseId);
	String useResSys = "true";
	if(resSysEntryUrl == null){
		useResSys = "false";
	}	
	
	String toolId = session.getAttribute("toolId")==null?"":session.getAttribute("toolId").toString();
	String siteId = session.getAttribute("siteId")==null?"":session.getAttribute("siteId").toString();
%>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="<%=path %>/resource/styles/ext-all.css" />
	<link rel="stylesheet" type="text/css" href="<%=path %>/resource/styles/ColumnNodeUI.css" />
	<link rel="stylesheet" type="text/css" href="<%=path %>/resource/styles/ext-extends.css" />
	<link rel="stylesheet" type="text/css" href="<%=path %>/resource/styles/DateTimePicker.css" />
	<script type="text/javascript" src="/library/js/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="/library/js/json2.js"></script>

	<script type="text/javascript">
		var path = "<%=path %>";
		var toolId = "<%=toolId%>";
		var siteId = "<%=siteId%>";
		var t = "<%=org.sakaiproject.resource.util.Constants.SECTION_URL%>";
		var sessionId="<%=session.getId()%>";
		var userName = "<%=userName%>";
		var courseId = "<%=session.getAttribute("courseId")%>";
		var exCourseId = "<%=exCourseId%>";
		//生成试卷的最大份数
		var operateCount = "<%=Constants.operateCount%>";
		//使用那个论坛
		var forumType = "<%=session.getAttribute("forumType")%>";
		//是否使用题库系统
		var useExamSys = <%=useExamSys%>;
		var examSysSchemaUrl = "<%=examSysSchemaUrl%>";
		var examSysEntryUrl = "<%=examSysEntryUrl%>";
		
		//是否使用资源系统
		var useResSys = <%=useResSys%>;
		var resSysEntryUrl = "<%=resSysEntryUrl%>";		
		
		// 模板访问路径
		var tplPath = "<%=Constants.TEMPLATE_PATH_URI_RELATIVE%>";
		var tpls = jQuery.parseJSON('<%=tplsJSON%>');
		var tplsJSON = [];
		if(tpls){
			jQuery.each(tpls,function(index, el) {
				tplsJSON.push([el.name,el.title]);
			});
		}
		//console.log(tplsJSON)
		var focus_path = null;
		
		function setFocus(elements){
			return;
		}
		function scrollToTop(){
			window.top.scrollTo(0, 0);
		}
	</script>
	

	<script type="text/javascript" src="<%=path %>/resource/scripts/extjs/ext-base.js"></script>
	<script type="text/javascript" src="<%=path %>/resource/scripts/extjs/ext-all.js"></script>
	<script type="text/javascript" src="<%=path %>/resource/scripts/extjs/ext-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=path %>/resource/scripts/extjs/ColumnNodeUI.js"></script>
	<script type="text/javascript" src="<%=path %>/resource/scripts/util.js"></script>	
	<script type="text/javascript" src="<%=path %>/resource/scripts/course/DateTimeField.js"></script>

	<script type="text/javascript" src="<%=path %>/resource/scripts/codeTable.js"></script>
	<script type="text/javascript" src="<%=path %>/resource/scripts/course/courseManage.js"></script>
	<script type="text/javascript" src="<%=path %>/resource/scripts/course/setCourse.js"></script>
	<script type="text/javascript" src="<%=path %>/resource/scripts/course/editModuleScorm.js"></script>
	<script type="text/javascript" src="<%=path %>/resource/scripts/course/editSectionScorm.js"></script>
	<script type="text/javascript" src="<%=path %>/resource/scripts/course/editTest.js"></script>
	<script type="text/javascript" src="<%=path %>/resource/scripts/course/editSelfTest.js"></script>
	<script type="text/javascript" src="<%=path %>/resource/scripts/course/editForum.js"></script>
	<script type="text/javascript" src="<%=path %>/resource/scripts/course/setPercent.js"></script>
	<script type="text/javascript" src="<%=path %>/resource/scripts/course/importScorm.js"></script>
	<script type="text/javascript" src="<%=path %>/resource/scripts/course/setAttributes.js"></script>
</head>
<body>
	<a id="btn_go_module" href='courseSpace_goModuleEdit.do' data-href='courseSpace_goModuleEdit.do' target='_blank' style="display:none">模块-编辑</a>
	<a id="btn_go_section" href='courseSpace_goSectionEdit.do' data-href='courseSpace_goSectionEdit.do' target='_blank' style="display:none">页-编辑</a>
	<div id='toolpanel'></div>
	<div id='treepanel'></div>
</body>
</html>