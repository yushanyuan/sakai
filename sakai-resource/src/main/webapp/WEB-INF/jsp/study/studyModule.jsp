<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String nodeId = (String) request.getAttribute("nodeId");
	String next = request.getAttribute("next") == null ? "true" : (String) request.getAttribute("next");
	String previous = request.getAttribute("previous") == null ? "true" : (String) request
			.getAttribute("previous");
	String studyrecordId = (String) request.getAttribute("studyrecordId");
%>
<html>
	<head>
		<script type="text/javascript">
var path = "<%=path%>";
var nodeId = "<%=nodeId%>";
var studyrecordId = "<%=studyrecordId%>";
var pageTitle = "<%=request.getAttribute("nodeText")%>";
</script>
		<link rel="stylesheet" type="text/css"
			href="<%=path%>/resource/styles/ext-all.css" />
		<link rel="stylesheet" type="text/css"
			href="<%=path%>/resource/styles/ColumnNodeUI.css" />
		<link rel="stylesheet" type="text/css"
			href="<%=path%>/resource/styles/ext-extends.css" />
		<link rel="stylesheet" type="text/css"
			href="<%=path%>/resource/styles/pageStyle.css" />
		<script type="text/javascript"
			src="<%=path%>/resource/scripts/extjs/ext-base.js">
</script>
		<script type="text/javascript" language="JavaScript"
			src="/library/js/headscripts.js">
</script>
		<script type="text/javascript"
			src="/library/js/jquery-1.7.1.min.js">
</script>
		<script type="text/javascript"
			src="<%=path%>/resource/scripts/extjs/ext-all.js">
</script>
		<script type="text/javascript"
			src="<%=path%>/resource/scripts/extjs/ext-lang-zh_CN.js">
</script>
		<script type="text/javascript"
			src="<%=path%>/resource/scripts/extjs/ColumnNodeUI.js">
</script>
		<script type="text/javascript"
			src="<%=path%>/resource/scripts/util.js">
</script>
		<script type="text/javascript"
			src="<%=path%>/resource/scripts/codeTable.js">
</script>
		<script type="text/javascript"
			src="<%=path%>/resource/scripts/study/helptool.js">
</script>
		<script type="text/javascript"
			src="<%=path%>/resource/scripts/study/moduleCourse.js">
</script>
		<script type="text/javascript">
function pageHeightInit() {
<%=request.getAttribute("sakai.html.body.onload")%>
}
</script>
	</head>
	<body onload="pageHeightInit()">
		<br>
		<h2><%=request.getAttribute("nodeText")%></h2>
		<%=request.getAttribute("description") == null ? "" : request.getAttribute("description")%>
		<br>
		<br>
		<br>
		<h2>
			该节点内容如下：
		</h2>
		<div id="helperToolId"
			style="position: absolute; width: 129px; z-index: 1; right: 2px; top: 0px">
		</div>
		<div id='treepanel'></div>
		<br>
		<h2>
			已完成的测试如下：
		</h2>
		<div id='viewAttempt'></div>
		<script type="text/javascript">
//scrollImg();
</script>
	</body>
</html>
