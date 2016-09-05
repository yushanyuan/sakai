<%@ page language="java" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String lessonStatus = (String)request.getAttribute("lessonStatus");
	String score = (String)request.getAttribute("score");
	String showStuStatus = (String)request.getAttribute("showStuStatus");
	String studyrecordId = (String)request.getAttribute("studyrecordId");
%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="<%=path %>/resource/styles/ext-all.css" />
<link rel="stylesheet" type="text/css" href="<%=path %>/resource/styles/ColumnNodeUI.css" />
<link rel="stylesheet" type="text/css" href="<%=path %>/resource/styles/ext-extends.css" />
<link rel="stylesheet" type="text/css" href="<%=path %>/resource/styles/GroupSummary.css" />
<script type="text/javascript" src="<%=path %>/resource/scripts/extjs/ext-base.js"></script>
<script type="text/javascript" language="JavaScript" src="/library/js/headscripts.js"></script>
<script type="text/javascript" src="<%=path %>/resource/scripts/extjs/ext-all.js"></script>
<script type="text/javascript" src="<%=path %>/resource/scripts/extjs/ext-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=path %>/resource/scripts/extjs/ColumnNodeUI.js"></script>
<script type="text/javascript" src="<%=path %>/resource/scripts/extjs/GroupSummary.js"></script>
<script type="text/javascript" src="<%=path %>/resource/scripts/util.js"></script>
<script type="text/javascript" src="<%=path %>/resource/scripts/codeTable.js"></script>
<script type="text/javascript" src="<%=path %>/resource/scripts/study/studyManage.js"></script>
<script type="text/javascript">
var path = "<%=path %>";
var lessonStatus = "<%=lessonStatus %>";
var showStuStatus = "<%=showStuStatus %>";
var score = "<%=score %>";
var sessionId="<%=session.getId()%>";
var studyrecordId = "<%=studyrecordId%>";
function pageHeightInit(){
	<%= request.getAttribute("sakai.html.body.onload") %>
}
</script>
</head>
<body onload="pageHeightInit()">
<div id='toolpanel'></div>
<div id='treepanel'></div>
</body>
</html>