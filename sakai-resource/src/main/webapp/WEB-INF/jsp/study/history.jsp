<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String nodeId = (String)request.getAttribute("node");
	String studyrecordId = (String)request.getAttribute("studyrecordId");
%>
<html>
<head>
<script type="text/javascript">
	function history(){
		window.location.href="studySpace_fowardCourseware.do?node=<%=nodeId%>&studyrecordId=<%=studyrecordId%>"
	}
</script>
</head>
<body onload="history()">
</body>
</html>
