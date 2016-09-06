<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
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
		<script type="text/javascript" src="<%=path %>/resource/scripts/statistics/studyRecordStat.js"></script>
		<script type="text/javascript">
			var studyrecordId = "<%=studyrecordId%>";
			var sessionId="<%=session.getId()%>";
			function pageHeightInit(){
				<%= request.getAttribute("sakai.html.body.onload") %>
			}
		</script>
</head>
 
  <body>
   <div id='treepanel'></div>
  </body>
</html>
