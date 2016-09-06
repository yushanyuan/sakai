<%@ page language="java" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String examSysUrl = org.sakaiproject.resource.util.Constants.OUTSYS_EXAM_URL_BASE;
	String examSysSchemaUrl = examSysUrl + org.sakaiproject.resource.util.Constants.OUTSYS_EXAM_URL_SCHEMA;
	String examSysEntryUrl = examSysUrl + org.sakaiproject.resource.util.Constants.OUTSYS_EXAM_URL_ENTRY;
%>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="<%=path %>/resource/styles/ext-all.css" />
	<script type="text/javascript" src="<%=path %>/resource/scripts/extjs/ext-base.js"></script>
	<script type="text/javascript" src="<%=path %>/resource/scripts/extjs/ext-all.js"></script>
	<script type="text/javascript" src="<%=path %>/resource/scripts/codeTable.js"></script>
	<script type="text/javascript" src="<%=path %>/resource/scripts/util.js"></script>
	<script type="text/javascript" src="<%=path %>/resource/scripts/test/edit.js"></script>
	<script>
		var focus_path = null;
		var examSysSchemaUrl = "<%=examSysSchemaUrl%>";
		var examSysEntryUrl = "<%=examSysEntryUrl%>";
		
		function pageHeightInit(){
			<%= request.getAttribute("sakai.html.body.onload") %>
		}
		function setMainFrameHeight(frameId){
			//parent.parent.window.scrollTo(0,0);
			var minH = 240;
			var scrollH = document.body.scrollHeight + 70;
			var parentF = parent.document.getElementsByName("frame1")[0];
			var parentStyle = (parentF.style) ? parentF.style : parentF;
			var frame = parent.parent.document.getElementsByName(frameId)[0];
			var frameStyle = (frame.style) ? frame.style : frame;
			var newHeight = (scrollH >= minH?scrollH:minH);
			parentStyle.height=newHeight + "px";
			newHeight += 50;
			frameStyle.height=newHeight + "px";
		}
		function setFocus(elements){
			return;
		}
	</script>
</head>
<body></body>
</html>