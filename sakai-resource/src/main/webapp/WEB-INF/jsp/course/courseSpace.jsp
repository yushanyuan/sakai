<%@ page language="java" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String userId = session.getAttribute("userEId").toString().trim();
	String exCourseId = session.getAttribute("exCourseId")==null?"":session.getAttribute("exCourseId").toString();
	String examSysEntryUrl = org.sakaiproject.resource.util.CourseUtil.getExamSysEntryUrl(userId,exCourseId);
	String useExamSys = "true";
	if(examSysEntryUrl == null){
		useExamSys = "false";
	}

	String resCourseId = session.getAttribute("resCourseId")==null?"":session.getAttribute("resCourseId").toString();
	String resSysEntryUrl = org.sakaiproject.resource.util.CourseUtil.getResSysEntryUrl(userId,resCourseId);
	String useResSys = "true";
	if(resSysEntryUrl == null){
		useResSys = "false";
	}
%>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="<%=path %>/resource/styles/ext-all.css" />
	<link rel="stylesheet" type="text/css" href="<%=path %>/resource/styles/ext-extends.css" />
	<script type="text/javascript" src="/library/js/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="<%=path %>/resource/scripts/extjs/ext-base.js"></script>
	<script type="text/javascript" src="<%=path %>/resource/scripts/extjs/ext-all.js"></script>
	<script type="text/javascript" language="JavaScript" src="/library/js/headscripts.js"></script>
	<script type="text/javascript" src="<%=path %>/resource/scripts/course/courseSpace.js"></script>
	<script>
		//是否使用题库系统
		var useExamSys = <%=useExamSys%>;
		var examSysEntryUrl = "<%=examSysEntryUrl%>";

		//是否使用资源系统
		var useResSys = <%=useResSys%>;
		var resSysEntryUrl = "<%=resSysEntryUrl%>";		
	
		function fitScreen(id) {
		    var fp1 = $("#"+id);
			var fp2 = $(parent.parent.document).find(".portletMainIframe")[0];
		    if (fp1) {
		    	//console.log($(document.getElementById(id).contentWindow.document.body).outerHeight())
		        $(fp1).height($(document.getElementById(id).contentWindow.document.body).outerHeight()+35);
		        if (fp2) {
		            $(fp2).height($(fp1).outerHeight() + 50);
		        }
		    }
		}
	</script>
</head>
<body>
</body>
</html>