var startTime;

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

function view_test_student_submit(obj,url){
	startTime=document.getElementById("startTime").value;
	if(confirm("您确定要提交吗？")){
	 	obj.disabled=true;
	 	for(var i=1;;i++){
			var textareaNode = document.getElementById("textareIndex"+i);
			if(!textareaNode){
				break;
			}
			textareaNode.value = FCKeditorAPI.GetInstance("textareIndex"+i).GetXHTML(true); 
		}
		var answer={};
		var elems=document.getElementById("testPaper").elements;
		for(var i=0;i<elems.length;i++){
			var elem=elems[i];
			var answerId=elem.getAttribute("name");
			var num=elem.getAttribute("num");
			var qId=elem.getAttribute("qid");
			var answerValue="";
			var classname = jQuery(elem).attr('class');

			if(classname=="objText"){
				var checkboxs=document.getElementsByName(answerId);
				for(var j=0;j<checkboxs.length;j++){
					if(checkboxs[j].checked==true){
						answerValue = answerValue + jQuery.trim(checkboxs[j].value) + ";";
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
		jQuery.post(url, {
			answerString: JSON.stringify(answer),
	   		courseId :courseId,
	   		testId :testId,
	   		paperId : paperId,
	   		testrecordId :testrecordId,
	   		samepaper : samepaper,
	   		startTime :startTime,
	   		passScore :passScore,
	   		paperType: paperType,
	   		studyrecordId:studyrecordId			
		}, function(data, textStatus, xhr) {
	   		/*try{
	   			sNode.parentNode.reload();//刷新父窗口
	   		}catch(e){}*/
	   		var d = JSON.parse(data);
	   		//console.log(d)
	   		document.getElementById("contentDiv").innerHTML = d.data;//显示答案
		});
	}
}

function get_check_paper_answer(attemptId){
	var answer={};
	var elems=document.getElementById("checkPaper-"+attemptId).elements;
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
function check_paper_submit(checkPaperId,attemptId,userId,courseid,paperType,studyrecordId){
	if(confirm("确认要提交?")){
    var pars = 'paperid=' + checkPaperId + '&attemptId=' + attemptId+ '&userid=' +userId+ '&courseid=' +courseid+ '&studentScoreString=' +get_check_paper_answer(attemptId)+'&paperType='+paperType +"&studyrecordId="+studyrecordId;//+'&HTSID='+HTSID ;
    var myAjax = new Ajax.Request(
    		'checkwork_checkObjSave.do',
        {
			method: 'post', 
			parameters: pars,
			onSuccess: function() {
        	 try{
        		if(document.getElementById("checkPaper-"+attemptId)){
        			var elements=document.getElementById("checkPaper-"+attemptId).elements;
        			for(var i=0;i<elements.length;i++){
        				elements[i].disabled=true;
        			}
        		 document.getElementById("checkPaper-"+attemptId).disabled=true;	
        		}
        		if(document.getElementById("submitCheckScore")){
        		  document.getElementById("submitCheckScore").disabled=true;
        		}
    			alert("提交成功！");
    			var parentwindow = window.opener;
    			parentwindow.checkWorkListStore.reload();
        	 }catch(e){
        		 alert(e);
        	 }
        	} 
         }
         );
	}
}