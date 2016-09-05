<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String content = (String)request.getAttribute("content");
	String paperid = (String)request.getAttribute("paperId");
	String recordId = (String)request.getAttribute("recordId");
	String userid = (String)request.getAttribute("studentId");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script type="text/javascript" language="JavaScript" src="resource/scripts/study/prototype.js"></script>
</head>
<script type="text/javascript">
var path = "<%=path %>";
function get_check_paper_answer(recordId){
	var answer={};
	var elems=document.getElementById("checkPaper-"+recordId).elements;
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
		if(classname=="inputText"){
			answerValue=elem.value;
			if(	answer[qId]==null||answer[qId]==""){
				answer[qId]={};
				answer[qId]["id"]=qId;
				answer[qId]["studentScore"]=[];
				answer[qId]["studentScore"][num]=answerValue;
			}else{
				answer[qId]["studentScore"][num]=answerValue;
			}
		}
	}
	return encodeURIComponent(Object.toJSON(answer))
}
function check_paper_submit(checkPaperId,recordId,userId){
	if(confirm("确认要提交?")){
    var pars = 'paperId=' + checkPaperId + '&recordId=' + recordId+ '&studentId=' +userId + '&studentScore=' +get_check_paper_answer(recordId) ;//+'&HTSID='+HTSID ;

    var myAjax = new Ajax.Request(
    		'test_checkTestSave.do',
        {
			method: 'post', 
			parameters: pars,
			onSuccess: function() {
        	 try{
        		if(document.getElementById("checkPaper-"+recordId)){
        			var elements=document.getElementById("checkPaper-"+recordId).elements;
        			for(var i=0;i<elements.length;i++){
        				elements[i].disabled=true;
        			}
        		 document.getElementById("checkPaper-"+recordId).disabled=true;	
        		}
        		if(document.getElementById("submitCheckScore")){
        		  document.getElementById("submitCheckScore").disabled=true;
        		}
    			alert("提交成功！");
    			var parentwindow = window.opener;
    			parentwindow.store.reload();
        	 }catch(e){
        		 alert(e);
        	 }
        	} 
         }
         );
	}
}
</script>
<body>
<form id="checkPaper-<%=recordId %>">
<%=content%>
<center>
<input type="button" value=提交成绩 class ="submitCheckScore" id="submitCheckScore"
onclick="check_paper_submit('<%=paperid %>','<%=recordId %>','<%=userid %>')"/>
</center>
</form>
</body>
</html>
