<%@ page language="java" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String jsonResult = (String)request.getAttribute("jsonResult");
	String templateRelativeUrl = org.sakaiproject.resource.util.Constants.TEMPLATE_PATH_URI_RELATIVE;
	String template = (String)request.getAttribute("template");
	String templateUrl = templateRelativeUrl + template;
	
	String userId = session.getAttribute("userEId").toString().trim();
	
	String resCourseId = session.getAttribute("resCourseId")==null?"":session.getAttribute("resCourseId").toString();
	String resSysBaseUrl = org.sakaiproject.resource.util.CourseUtil.getResSysEntryUrl(userId,resCourseId);
	String useResSys = "true";
	if(resSysBaseUrl == null){
		useResSys = "false";
	}
%>
<!DOCTYPE html>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="<%=path %>/resource/styles/ext-all.css" />
	<link rel="stylesheet" type="text/css" href="<%=path %>/resource/styles/ColumnNodeUI.css" />
	<link rel="stylesheet" type="text/css" href="<%=path %>/resource/styles/ext-extends.css" />
	<script type="text/javascript" src="/library/js/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="/library/js/json2.js"></script>

	<script type="text/javascript" src="<%=path %>/resource/scripts/extjs/ext-base.js"></script>
	<script type="text/javascript" src="<%=path %>/resource/scripts/extjs/ext-all.js"></script>
	<script type="text/javascript" src="<%=path %>/resource/scripts/extjs/ext-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=path %>/resource/scripts/extjs/ColumnNodeUI.js"></script>
	<script type="text/javascript" src="<%=path %>/resource/scripts/util.js"></script>
	
	<script type="text/javascript" src="<%=path %>/resource/scripts/ckeditor/ckeditor.js"></script>
	<script type="text/javascript" src="<%=path %>/resource/scripts/ckeditor/config.js"></script>

	<script type="text/javascript" src="<%=path %>/resource/scripts/codeTable.js"></script>
	<script type="text/javascript" src="<%=path %>/resource/scripts/course/editSection.js"></script>

	
	<script type="text/javascript">
		var path = "<%=path %>";
		var userName = "<%=session.getAttribute("userName")%>";
		var courseId = "<%=session.getAttribute("courseId")%>";
		var resCourseId = "<%=session.getAttribute("resCourseId")%>";
		var useResSys = <%=useResSys%>;
		var nodeId = "<%=request.getAttribute("node")%>";
		var tplUrl = "<%=templateUrl%>";

		var cfg = {			
			filebrowserBrowseUrl : path + '/resource/scripts/ckfinder/ckfinder.jsp?courseId='+courseId,
			filebrowserImageBrowseUrl : path + '/resource/scripts/ckfinder/ckfinder.jsp?type=Images&courseId='+courseId,
			filebrowserFlashBrowseUrl : path + '/resource/scripts/ckfinder/ckfinder.jsp?type=Flash&courseId='+courseId,
			filebrowserUploadUrl : path + '/resource/scripts/ckfinder/core/connector/java/connector.java?command=QuickUpload&type=Files&courseId='+courseId,
			filebrowserImageUploadUrl : path + '/resource/scripts/ckfinder/core/connector/java/connector.java?command=QuickUpload&type=Images&courseId='+courseId,
			filebrowserFlashUploadUrl : path + '/resource/scripts/ckfinder/core/connector/java/connector.java?command=QuickUpload&type=Flash&courseId='+courseId,
			height : 360,
			fullPage: true,
			baseFloatZIndex : 8000,
			allowedContent : true
		};	
		if(useResSys){
			cfg.toolbar='Full';
		}
		jQuery.extend(editorConfig, cfg);

		jQuery(document).ready(function(){	
			t = jQuery.parseJSON(jQuery("#txt_json").html());
			//console.log(t)
			updateSection(t,"课程空间-页编辑")
		})	
	</script>	
</head>
<body>
	<textarea id="txt_json" style="display:none"><%=jsonResult%></textarea>
</body>
</html>