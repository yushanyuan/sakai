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
%>
<html>
<head>
<title>做前测</title>
<link rel="stylesheet" type="text/css" href="resource/styles/paper.css" />
<script type="text/javascript" src="<%=path %>/resource/scripts/extjs/ext-base.js"></script>
<script type="text/javascript" src="<%=path %>/resource/scripts/extjs/ext-all.js"></script>
<script type="text/javascript" language="JavaScript" src="resource/scripts/study/prototype.js"></script>
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
var studyrecordId = "<%=studyrecordId%>"
var sNode = null;
if(Prototype.Browser.IE){//ie
	sNode = dialogArguments.curNode;
}else{//ff
	sNode = window.opener.curNode;
}
function replaceTextarea(textarea_id){
	var oFCKeditor = new FCKeditor(textarea_id);
	oFCKeditor.BasePath = "/library/editor/FCKeditor/";
	oFCKeditor.Width  = "700" ;
	oFCKeditor.Height = "625" ;
	var collectionId = "/group-user/3a9d5efb-da7b-4e1f-8b7c-d561405b6edb/52d0834f-fdeb-4680-83a6-e8e92f248933/";
	oFCKeditor.Config['ImageBrowserURL'] = oFCKeditor.BasePath + "editor/filemanager/browser/default/browser.html?Connector=/sakai-fck-connector/web/editor/filemanager/browser/default/connectors/jsp/connector&Type=Image&CurrentFolder=" + collectionId;
	oFCKeditor.Config['LinkBrowserURL'] = oFCKeditor.BasePath + "editor/filemanager/browser/default/browser.html?Connector=/sakai-fck-connector/web/editor/filemanager/browser/default/connectors/jsp/connector&Type=Link&CurrentFolder=" + collectionId;
	oFCKeditor.Config['FlashBrowserURL'] = oFCKeditor.BasePath + "editor/filemanager/browser/default/browser.html?Connector=/sakai-fck-connector/web/editor/filemanager/browser/default/connectors/jsp/connector&Type=Flash&CurrentFolder=" + collectionId;
	oFCKeditor.Config['ImageUploadURL'] = oFCKeditor.BasePath + "/sakai-fck-connector/web/editor/filemanager/browser/default/connectors/jsp/connector?Type=Image&Command=QuickUpload&Type=Image&CurrentFolder=" + collectionId;
	oFCKeditor.Config['FlashUploadURL'] = oFCKeditor.BasePath + "/sakai-fck-connector/web/editor/filemanager/browser/default/connectors/jsp/connector?Type=Flash&Command=QuickUpload&Type=Flash&CurrentFolder=" + collectionId;
	oFCKeditor.Config['LinkUploadURL'] = oFCKeditor.BasePath + "/sakai-fck-connector/web/editor/filemanager/browser/default/connectors/jsp/connector?Type=File&Command=QuickUpload&Type=Link&CurrentFolder=" + collectionId;
	oFCKeditor.Config['CurrentFolder'] = collectionId;
	oFCKeditor.Config['CustomConfigurationsPath'] = "/library/editor/FCKeditor/config.js";
//	oFCKeditor.Config['LinkUpload'] = true;
//	oFCKeditor.Config['ImageUpload'] = true;
//	oFCKeditor.Config['FlashUpload'] = true;
	oFCKeditor.ReplaceTextarea();
} 
function replaceAllTextarea(){
	for(var i=1;;i++){
		var textareaNode = document.getElementById("textareIndex"+i);
		if(textareaNode){
			replaceTextarea("textareIndex"+i);
		}else{
			break;
		}
	}

}
</script>
</head>
<body onload="replaceAllTextarea();">
<div id="contentDiv">
<form id="testPaper" action="">
<%=content%>
</form>
<form id="submitForm">
<input id="startTime" type="hidden" name="startTime" value="<%=startTime%>" />
<div align="center">
	<input id="submitAnswer" type="button" name="submitAnswer" value="提交答案" onclick="view_test_student_submit(this,'studyPaper_writeSelfTestSave.do')" />
</div>
</form>
</div>
</body>
</html>