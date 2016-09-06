<%@ page language="java" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
%>
<html>
<head>
<title>学生参与情况统计</title>
<link rel="stylesheet" type="text/css" href="<%=path %>/resource/styles/ext-all.css" />
<link rel="stylesheet" type="text/css" href="<%=path %>/resource/styles/ext-extends.css" />
<script type="text/javascript" src="<%=path %>/resource/scripts/extjs/ext-base.js"></script>
<script type="text/javascript" src="<%=path %>/resource/scripts/extjs/ext-all.js"></script>
<script type="text/javascript" language="JavaScript" src="/library/js/headscripts.js"></script>
<script type="text/javascript" src="<%=path %>/resource/scripts/extjs/ext-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=path %>/resource/scripts/statistics/statStudent.js"></script>
<script type="text/javascript">
 	var path = "<%=path %>";
 	var sessionId="<%=session.getId()%>";
</script>
</head>
<body onload="<%= request.getAttribute("sakai.html.body.onload") %>">
</body>
</html>