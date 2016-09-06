<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String content = (String)request.getAttribute("content");
	String paperid = (String)request.getAttribute("paperid");
	String courseid = (String)request.getAttribute("courseid");
	String attemptId = (String)request.getAttribute("attemptId");
	String userid = (String)request.getAttribute("userid");
	String paperType = (String)request.getAttribute("paperType");
	String studyrecordId = (String)request.getAttribute("studyrecordId");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script type="text/javascript" language="JavaScript" src="resource/scripts/study/prototype.js"></script>
<script type="text/javascript" language="JavaScript" src="resource/scripts/study/recordscripts.js"></script>
</head>
<script type="text/javascript">
var path = "<%=path %>";
var paperid = "<%=paperid %>";
var courseid = "<%=courseid %>";
var attemptId = "<%=attemptId %>";
var userid = "<%=userid %>";
var paperType = "<%=paperType %>"
var studyrecordId = "<%=studyrecordId%>"
</script>
<body>
<form id="checkPaper-<%=attemptId %>">
<%=content%>
<center>
<input type="button" value=提交成绩 class ="submitCheckScore" id="submitCheckScore"
onclick="check_paper_submit('<%=paperid %>','<%=attemptId %>','<%=userid %>','<%=courseid %>','<%=paperType %>','<%=studyrecordId%>')"/>
</center>
</form>
</body>
</html>
