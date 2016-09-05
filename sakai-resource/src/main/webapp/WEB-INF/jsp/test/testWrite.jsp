<%@ page language="java" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String content = (String)request.getAttribute("content");
	Long startTime = (Long)request.getAttribute("startTime");
	String testId = (String)request.getAttribute("testId");
	String paperId = (String)request.getAttribute("paperId");
	String masteryScore = (String)request.getAttribute("masteryScore");
	String samepaper = (String)request.getAttribute("samepaper");
	String totalScore = (String)request.getAttribute("totalScore");
%>
<html>
<head>
<title>做作业</title>
<script type="text/javascript" src="resource/scripts/extjs/ext-base.js"></script>
<script type="text/javascript" src="resource/scripts/extjs/ext-all.js"></script>
<script type="text/javascript" src="resource/scripts/study/prototype.js"></script>
<script type="text/javascript" src="/library/editor/FCKeditor/fckeditor.js"></script>
<script type="text/javascript">
var path = "<%=path%>";
var testId = "<%=testId %>";
var paperId = "<%=paperId %>";
var masteryScore = "<%=masteryScore %>";
var samepaper = "<%=samepaper %>";
var totalScore = "<%=totalScore%>";
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
function view_test_student_submit(obj,url){
	var startTime=document.getElementById("startTime").value;
	if(confirm("您确定要提交吗？")){
	 	obj.disabled=true;//按钮置灰
	 	for(var i=1;;i++){
			var textareaNode = document.getElementById("textareIndex"+i);
			if(textareaNode){
				textareaNode.value = FCKeditorAPI.GetInstance("textareIndex"+i).GetXHTML(true); 
			}else{
				break;
			}
		}
		var answer={};
		var elems=document.getElementById("testPaper").elements;
		for(var i=0;i<elems.length;i++){
			var elem=elems[i];
			var answerId=elem.getAttribute("name");
			var num=elem.getAttribute("num");
			var qId=elem.getAttribute("qid");
			var answerValue="";
			var classname="";
			if(Prototype.Browser.IE){
			  classname = elem.getAttribute("classname");
			}else{
			  classname=elem.getAttribute("class");
			}
			if(classname=="objText"){
				var checkboxs=document.getElementsByName(answerId);
				for(var j=0;j<checkboxs.length;j++){
					if(checkboxs[j].checked==true){
						answerValue = answerValue + checkboxs[j].value.strip() + ";";
					}
				}
				if(	answer[qId]==null||answer[qId]==""){
					answer[qId]={};
					answer[qId]["id"]=qId;
					answer[qId]["studentAnswer"]=[];
					answer[qId]["studentAnswer"][num]=answerValue;
				}else if(answer[qId]["studentAnswer"][num]==null||answer[qId]["studentAnswer"][num]==""){
					answer[qId]["studentAnswer"][num]=answerValue;
				}
				answerValue="";
			}else if(classname=="subText"){
				answerValue=elem.value;
				if(	answer[qId]==null||answer[qId]==""){
					answer[qId]={};
					answer[qId]["id"]=qId;
					answer[qId]["studentAnswer"]=[];
					answer[qId]["studentAnswer"][num]=answerValue;
				}else{
					answer[qId]["studentAnswer"][num]=answerValue;
				}
				answerValue="";
			}
		}	
	   	Ext.Ajax.request({
			url: url+'?answer=' + encodeURIComponent(Object.toJSON(answer)),
		   	success: function(response ,options ){
		   		var inner = Ext.decode(response.responseText).data;
		   		document.getElementById("contentDiv").innerHTML = inner;
		   	},
		   	failure: function(response ,options ){
		   	},
		   	params: {
		   		testId :testId,
		   		paperId : paperId,
		   		samepaper : samepaper,
		   		startTime : startTime,
		   		masteryScore : masteryScore,
		   		totalScore : totalScore
		   	}
		}); 
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
	<input id="submitAnswer" type="button" name="submitAnswer" value="提交答案" onclick="view_test_student_submit(this,'test_writeTestSave.do')" />
</div>
</form>
</div>
</body>
</html>