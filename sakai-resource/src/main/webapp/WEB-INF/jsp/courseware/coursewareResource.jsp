<%@ page language="java" import="java.util.*,org.sakaiproject.resource.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>资源库资源</title>
	  <link rel="stylesheet" type="text/css" href="<%=path %>/resource/styles/ext-all.css" />
	  <link rel="stylesheet" type="text/css" href="<%=path %>/resource/styles/ext-extends.css" />
	  <script type="text/javascript" src="<%=path %>/resource/scripts/extjs/ext-base.js"></script>
	  <script type="text/javascript" src="<%=path %>/resource/scripts/extjs/ext-all.js"></script>
	  <script type="text/javascript" src="<%=path %>/resource/scripts/extjs/ext-lang-zh_CN.js"></script>
	  <script type="text/javascript" src="<%=path %>/resource/scripts/courseware/resourceManage.js"></script>
	  <script type="text/javascript" src="<%=path %>/resource/scripts/courseware/resourceTrash.js"></script>
	  <script type="text/javascript" src="<%=path %>/resource/scripts/courseware/resourcePreview.js"></script>
	  <script type="text/javascript" src="<%=path %>/resource/scripts/util.js"></script>
	  <script type="text/javascript" src="<%=path%>/resource/scripts/swfupload/swfupload.js"></script>
	  <script type="text/javascript" src="<%=path%>/resource/scripts/swfupload/swfupload.queue.js"></script>
	  <script type="text/javascript" src="<%=path%>/resource/scripts/swfupload/swfupload.speed.js"></script>
	  <script type="text/javascript" src="<%=path%>/resource/scripts/file/swfupload.handler.js"></script>
	  <script type="text/javascript" language="JavaScript" src="/library/js/headscripts.js"></script>
	  <script type="text/javascript">
	  	var path = "<%=path %>";
	  	var sessionId="<%=session.getId()%>";
		var fileTypes="<%=Constants.COURSE_TYPE%>";
		var fileSizeLimit="<%=Constants.COURSE_SIZE%>";
	  </script>

  </head>
  
  <body onload="<%= request.getAttribute("sakai.html.body.onload") %>">
  <div id="coursewarediv" style="height:465px"></div>
  </body>
</html>
