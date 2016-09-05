<%@ page language="java" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
%>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="<%=path %>/resource/styles/ext-all.css" />
	<link rel="stylesheet" type="text/css" href="<%=path %>/resource/styles/ext-extends.css" />
	<link rel="stylesheet" type="text/css" href="<%=path %>/resource/styles/DateTimePicker.css" />
	<script type="text/javascript" src="/library/js/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="<%=path %>/resource/scripts/extjs/ext-base.js"></script>
	<script type="text/javascript" src="<%=path %>/resource/scripts/extjs/ext-all.js"></script>
	<script type="text/javascript" src="<%=path %>/resource/scripts/extjs/ext-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=path %>/resource/scripts/course/DateTimeField.js"></script>
	<script type="text/javascript" src="<%=path %>/resource/scripts/codeTable.js"></script>
	<script type="text/javascript" src="<%=path %>/resource/scripts/util.js"></script>
	<script type="text/javascript" src="<%=path %>/resource/scripts/course/checkWork.js"></script>
	<script type="text/javascript">
	var path = "<%=path %>";
	var sessionId="<%=session.getId()%>";
	</script>
</head>
 <body>
</body>
</html>