<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String coursewarePath = (String)request.getAttribute("coursewarePath");
String moduleId = (String)request.getAttribute("moduleId");
String sectionId = (String)request.getAttribute("sectionId");
String sectionRecordId = (String)request.getAttribute("sectionRecordId");
Long rootModuleId = (Long)request.getAttribute("rootModuleId");
String next = request.getAttribute("next")==null?"true":(String)request.getAttribute("next");
String previous =  request.getAttribute("previous")==null?"true":(String)request.getAttribute("previous");

String selfStudyTime = (String)request.getAttribute("selfStudyTime");
String studyTime = (String)request.getAttribute("studyTime");
String submitInterval = (String)request.getAttribute("submitInterval");
String promptInterval = (String)request.getAttribute("promptInterval");
String skipInterval = (String)request.getAttribute("skipInterval");
String studyrecordId = (String)request.getAttribute("studyrecordId");
String detailRecordId = (String)request.getAttribute("detailRecordId");
String mainDomain = (String)request.getAttribute("mainDomain");
String agency = (String)request.getAttribute("agency");
%>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="<%=path %>/resource/styles/pageStyle.css" />
</head>
<body onunload="updateDetailRecordOnunload();">
<iframe id="coursewareFrame" name="coursewareFrame" src="" frameborder="0" width="100%" style="overflow:hidden;"></iframe>
<iframe id="agencyFrame" name="agencyFrame" src="" style="display:none" ></iframe>

<script type="text/javascript" src="/library/js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="/library/courseware/course_plugin.js"></script>
<script type="text/javascript">
	var submitInterval = '<%=submitInterval%>';
	var promptInterval = '<%=promptInterval%>';
	var skipInterval = '<%=skipInterval%>';
	var studyrecordId = "<%=studyrecordId%>";
	var moduleId = "<%=moduleId%>";
	var path ='<%=path%>';
	var sectionId = '<%=sectionId%>';
	var next = <%=next%>;
	var previous = <%=previous%>;
	var detailRecordId = '<%=detailRecordId%>';
	var sectionFrame;
	var agencyFrame;

	function Initialize() {
		var parentDomain = window.location.protocol + "//" +window.location.host + "<%=path%>";
		var searchparam= window.location.search;
	    sectionFrame = document.getElementById("coursewareFrame");	
	    agencyFrame = document.getElementById("agencyFrame");
	    var frameUrl = "<%=coursewarePath%>"+searchparam+"&sectionRecordId="+<%=sectionRecordId%>+"#"+parentDomain;
	    sectionFrame.src = frameUrl;

		var jsTimer=setInterval("updateDetailRecord();",submitInterval);
		
		//create helper tool on root page
		var paras = "node=" + sectionId + "&detailRecordId="+ detailRecordId +"&studyrecordId="+ studyrecordId;
		var config = {
			target:window,
			pre:{show:previous,url:"studySpace_getPreviousSection.do?"+paras},
			next:{show:next,url:'studySpace_getNextSection.do?'+paras},
			parent:{show:true,url:"studySpace_abovePage.do?type=sectionType&node=<%=rootModuleId%>&detailRecordId="+detailRecordId+"&studyrecordId="+studyrecordId},
			home:{show:true,url:"courseSpace.do?flag=true"}
		}
		config.pre.url = $buptnu.cware.getUrlFull(config.pre.url);
		config.next.url = $buptnu.cware.getUrlFull(config.next.url);
		config.parent.url = $buptnu.cware.getUrlFull(config.parent.url);
		config.home.url = $buptnu.cware.getUrlFull(config.home.url);

		var toplevel = window;
		if(top){
			toplevel = top;
		}
		//卸载工具栏
		$buptnu.cware.helperbarUnload(toplevel.document);
		
		try{
			toplevel.$buptnu.cware.helperbar(config);
		}catch(exp){}		
	};

	function updateDetailRecord(){
		$.post("studySpace_updateDetailRecord.do", "moduleId="+moduleId+"&detailRecordId="+detailRecordId+"&studyrecordId="+studyrecordId, function(data){});
	}

	function updateDetailRecordOnunload(){
		var toplevel = window;
		if(top){
			toplevel = top;
		}
		//卸载工具栏
		$buptnu.cware.helperbarUnload(top.document);

		$.post("studySpace_updateDetailRecord.do", "moduleId="+moduleId+"&detailRecordId="+detailRecordId+"&studyrecordId="+studyrecordId, function(data){});
	}

	$(function(){
		Initialize();
	})
</script>
</body>
</html>
