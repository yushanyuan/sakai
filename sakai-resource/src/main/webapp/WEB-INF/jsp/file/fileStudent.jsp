<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String studyrecordId = (String)request.getAttribute("studyrecordId");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>学生访问资源文件页面</title>
    
	 <link rel="stylesheet" type="text/css" href="resource/styles/ext-all.css" />
    <!-- Common Styles for the examples -->

    <link rel="stylesheet" type="text/css" href="resource/styles/ColumnNodeUI.css" />
      <link rel="stylesheet" type="text/css" href="resource/styles/ext-extends.css" /> 
      <script type="text/javascript">
		var sessionId="<%=session.getId()%>";
		var studyrecordId = "<%=studyrecordId%>"
    </script>
	<script type="text/javascript" language="JavaScript" src="/library/js/headscripts.js"></script>
    <script type="text/javascript" src="resource/scripts/extjs/ext-base.js"></script>
    <script type="text/javascript" src="resource/scripts/extjs/ext-all.js"></script>
<script type="text/javascript" src="resource/scripts/extjs/ext-lang-zh_CN.js"></script>
    <script type="text/javascript" src="resource/scripts/extjs/ColumnNodeUI.js"></script>
    <script type="text/javascript" src="resource/scripts/util.js"></script>
    <script type="text/javascript" src="resource/scripts/file/fileStudent.js"></script>
  </head>
  
	<body onload="<%= request.getAttribute("sakai.html.body.onload") %>">
  </body>
</html>
