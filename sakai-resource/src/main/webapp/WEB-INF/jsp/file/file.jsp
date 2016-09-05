<%@ page language="java" import="java.util.*,org.sakaiproject.resource.util.*,org.sakaiproject.component.cover.*,org.sakaiproject.tool.api.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%String path = request.getContextPath(); 
SessionManager sManager = (SessionManager)ComponentManager.get(SessionManager.class);
%>
<html>
<head>
<title>columnTreePanel</title>
    <link rel="stylesheet" type="text/css" href="resource/styles/ext-all.css" />
    <!-- Common Styles for the examples -->

    <link rel="stylesheet" type="text/css" href="resource/styles/ColumnNodeUI.css" />
      <link rel="stylesheet" type="text/css" href="resource/styles/ext-extends.css" /> 
<script type="text/javascript" language="JavaScript" src="/library/js/headscripts.js"></script>
    
     <script type="text/javascript">
		var path="<%=path%>";
		var sessionId="<%=session.getId()%>";
		var fileTypes="<%=Constants.FILE_TYPE%>";
		var fileSizeLimit="<%=Constants.FILE_SIZE%>";
		var courseId = "<%=session.getAttribute("courseId")%>";
    </script>
    <!-- LIBS -->
    <script type="text/javascript" src="resource/scripts/extjs/ext-base.js"></script>
    <!-- ENDLIBS -->
    <script type="text/javascript" src="resource/scripts/extjs/ext-all.js"></script>
<script type="text/javascript" src="resource/scripts/extjs/ext-lang-zh_CN.js"></script>
    <script type="text/javascript" src="resource/scripts/extjs/ColumnNodeUI.js"></script>
    <script type="text/javascript" src="resource/scripts/swfupload/swfupload.js"></script>
	<script type="text/javascript" src="resource/scripts/swfupload/swfupload.queue.js"></script>
	<script type="text/javascript" src="resource/scripts/swfupload/swfupload.speed.js"></script>
    <script type="text/javascript" src="resource/scripts/util.js"></script>
    <script type="text/javascript" src="resource/scripts/ckeditor/ckeditor.js"></script>
    <script type="text/javascript" src="resource/scripts/ckeditor/ckeditor_plugin.js"></script>
    <script type="text/javascript" src="resource/scripts/file/file.js"></script>
    <script type="text/javascript" src="resource/scripts/file/swfupload.handler.js"></script>
    <!-- EXAMPLES -->

</head>
<body onload="<%= request.getAttribute("sakai.html.body.onload") %>">

</body>
</html>