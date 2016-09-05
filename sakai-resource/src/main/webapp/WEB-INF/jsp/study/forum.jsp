<%@ page language="java" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String url = (String)request.getAttribute("forumUrl");
	String courseId = (String)request.getAttribute("courseId");	
	String studyrecordId = (String)request.getAttribute("studyrecordId");
%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="<%=path %>/resource/styles/ext-all.css" />
<script type="text/javascript" src="<%=path %>/resource/scripts/extjs/ext-base.js"></script>
<script type="text/javascript" src="<%=path %>/resource/scripts/extjs/ext-all.js"></script>
<script type="text/javascript" language="JavaScript" src="/library/js/headscripts.js"></script>
<script type="text/javascript" language="JavaScript" src="resource/scripts/study/prototype.js"></script>
<script type="text/javascript">
window.name = "student";
var courseId = "<%=courseId %>";
var studyrecordId = "<%=studyrecordId%>"
var sNode = null;
if(Prototype.Browser.IE){//ie
	sNode = dialogArguments.curNode;
}else{//ff
	sNode = window.opener.curNode;
}
function submitForumAttempt(topicId){
	var url = 'studyPaper_writeForumSave.do';
	var pars = 'courseId=' + courseId + '&topicId=' +topicId+'&studyrecordId='+studyrecordId ;
	
	var myAjax = new Ajax.Request(
        url,
        {
			method: 'post', 
			parameters: pars,
			onSuccess:function(){
				sNode.parentNode.reload();
			}
         }
    );
}
</script>
</head>
<body onload="<%= request.getAttribute("sakai.html.body.onload") %>">
<iframe style="width:100%;height:100%" src="<%=url%>">
</iframe>
</body>
</html>