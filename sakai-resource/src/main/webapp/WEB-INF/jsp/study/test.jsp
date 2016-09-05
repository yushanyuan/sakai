<%@ page language="java" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String content = (String)request.getAttribute("content");
	Long startTime = (Long)request.getAttribute("startTime");
	String courseId = (String)request.getAttribute("courseId");
	String testId = (String)request.getAttribute("testId");
	String paperId = (String)request.getAttribute("paperId");
	String testrecordId = (String)request.getAttribute("testrecordId");
	String passScore = (String)request.getAttribute("passScore");
	String samepaper = (String)request.getAttribute("samepaper");
	String paperType = (String)request.getAttribute("paperType");
	String studyrecordId = (String)request.getAttribute("studyrecordId");

	String needWait = null;
	Object needWaitObj = request.getAttribute("needWait");
	if(needWaitObj != null){
		needWait = needWaitObj.toString();
	}
%>
<html>
<head>
	<title>做作业</title>
	<link rel="stylesheet" type="text/css" href="resource/styles/paper.css" />
	<script type="text/javascript" language="JavaScript" src="/library/js/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" language="JavaScript" src="/library/js/json2.js"></script>
	<script type="text/javascript" language="JavaScript" src="resource/scripts/study/recordscripts.js"></script>
	<script type="text/javascript" src="/library/editor/FCKeditor/fckeditor.js"></script>
	<script type="text/javascript">
		var path = "<%=path %>";
		var courseId = "<%=courseId %>";
		var testId = "<%=testId %>";
		var paperId = "<%=paperId %>";
		var testrecordId = "<%=testrecordId %>";
		var passScore = "<%=passScore %>";
		var samepaper = "<%=samepaper %>";
		var paperType = <%=paperType%>
		var studyrecordId = "<%=studyrecordId%>";
		
		function replaceAllTextarea(){
			for(var i=1;;i++){
				var textareaNode = document.getElementById("textareIndex"+i);
				if(!textareaNode){					
					break;
				}
				replaceTextarea("textareIndex"+i);
			}
		}
	</script>
</head>
<body onload="replaceAllTextarea();">
	<div id="contentDiv">
		<%if(needWait != null && needWait.equals("true")){%>
			每次作业的最小间隔为5分钟
		<%}else{%>

		<form id="testPaper" action="">
			<%=content%>
		</form>
		<form id="submitForm">
			<input id="startTime" type="hidden" name="startTime" value="<%=startTime%>" />
			<div align="center">
				<input id="submitAnswer" type="button" name="submitAnswer" value="提交答案" onclick="view_test_student_submit(this,'studyPaper_writeTestSave.do')" />
			</div>
		</form>
		<%}%>
	</div>
</body>
</html>