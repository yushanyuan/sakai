<%@ page language="java" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String jsonResult = (String)request.getAttribute("jsonResult");

	String userName = session.getAttribute("userName").toString().trim();
	String userId = session.getAttribute("userEId").toString().trim();

	String resCourseId = session.getAttribute("resCourseId")==null?"":session.getAttribute("resCourseId").toString();
	String resSysBaseUrl = org.sakaiproject.resource.util.CourseUtil.getResSysEntryUrl(userId,resCourseId);
	String useResSys = "true";
	if(resSysBaseUrl == null){
		useResSys = "false";
	}
%>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="<%=path %>/resource/styles/ext-all.css" />
	<link rel="stylesheet" type="text/css" href="<%=path %>/resource/styles/ColumnNodeUI.css" />
	<link rel="stylesheet" type="text/css" href="<%=path %>/resource/styles/ext-extends.css" />
	<script type="text/javascript" src="/library/js/jquery-1.7.1.min.js"></script>

	<script type="text/javascript" src="<%=path %>/resource/scripts/extjs/ext-base.js"></script>
	<script type="text/javascript" src="<%=path %>/resource/scripts/extjs/ext-all.js"></script>
	<script type="text/javascript" src="<%=path %>/resource/scripts/extjs/ext-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=path %>/resource/scripts/extjs/ColumnNodeUI.js"></script>
	<script type="text/javascript" src="<%=path %>/resource/scripts/util.js"></script>

	<script type="text/javascript" src="<%=path %>/resource/scripts/ckeditor/ckeditor.js"></script>
	<script type="text/javascript" src="<%=path %>/resource/scripts/ckeditor/config.js"></script>

	<script type="text/javascript" src="<%=path %>/resource/scripts/codeTable.js"></script>
	<script type="text/javascript" src="<%=path %>/resource/scripts/course/editModule.js"></script>

	<script type="text/javascript">
		var path = "<%=path %>";
		var userName = "<%=userName%>";
		var courseId = "<%=session.getAttribute("courseId")%>";
		var resCourseId = "<%=session.getAttribute("resCourseId")%>";
		var nodeId = "<%=request.getAttribute("node")%>";
		var nodeType = "<%=request.getAttribute("nodeType")%>";
		var useResSys = <%=useResSys%>;
		
		jQuery(document).ready(function(){	
			var cfg = {
				filebrowserBrowseUrl : path + '/resource/scripts/ckfinder/ckfinder.jsp?courseId='+courseId,
				filebrowserImageBrowseUrl : path + '/resource/scripts/ckfinder/ckfinder.jsp?type=Images&courseId='+courseId,
				filebrowserFlashBrowseUrl : path + '/resource/scripts/ckfinder/ckfinder.jsp?type=Flash&courseId='+courseId,
				filebrowserUploadUrl : path + '/resource/scripts/ckfinder/core/connector/java/connector.java?command=QuickUpload&type=Files&courseId='+courseId,
				filebrowserImageUploadUrl : path + '/resource/scripts/ckfinder/core/connector/java/connector.java?command=QuickUpload&type=Images&courseId='+courseId,
				filebrowserFlashUploadUrl : path + '/resource/scripts/ckfinder/core/connector/java/connector.java?command=QuickUpload&type=Flash&courseId='+courseId,
				height : 180
			};		
			jQuery.extend(editorConfig, cfg);
		
			//console.log(t)
			t = jQuery.parseJSON(jQuery("#txt_json").html());
			//console.log(t)
			updateModule(t,"课程空间-节点编辑")
		})	
	</script>	
</head>
<body>
	<textarea id="txt_json" style="display:none"><%=jsonResult%></textarea>
</body>
</html>