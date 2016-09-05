<%@ page language="java" import="java.util.*,org.sakaiproject.resource.util.*,org.sakaiproject.resource.api.template.model.*" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%
  String url = Constants.TEMPLATE_PATH_URI;
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta http-equiv="Content-Style-Type" content="text/css">
  <title>模板列表</title>
  <link href="resource/styles/template.css" type="text/css" rel="stylesheet" media="all" />
</head>
<body>
  <div class="portletBody">
    <form class="edit" action="template_save.do" enctype="multipart/form-data" method="POST">
      <div class="row">
        <label for="">标题：</label>
        <input type="hidden" name="id" value="${templateModel.id}">
        <input type="text" name="title" id="title" value="${templateModel.title}" maxlength="50"/><span>*</span>
      </div>
       <div class="row">
        <label for="">标识名：</label>
        <input type="text" name="name" id="name" value="${templateModel.name}" maxlength="50"/><span>*</span>
      </div>
      <div class="row">
        <label for="">描述：</label>
        <textarea name="summary" style="width:400px;height:70px;">${templateModel.summary}</textarea>
      </div>
      <div class="row">
        <label for="">上传模板：</label><s:file name="zipFile" size="30" label="上传" />
        <span>注：zip压缩包，大小不超过50m;</span>
      </div>
      <s:if test="%{templateModel.name !=null}">
      <div class="row">
        <label for="">浏览模板：</label><a href="<%=url%>${templateModel.name}/<%=TemplateModel.TEMPLATE_PAGE_CHAPTER%>" target="_blank">chapter.html</a>
        &nbsp;&nbsp;<a href="<%=url%>${templateModel.name}/<%=TemplateModel.TEMPLATE_PAGE_MODULE%>" target="_blank">module.html</a>
      </div>  
      </s:if>    
      <div class="row" style="padding-left:150px;">
        <button id="btn_save">保存</button>&nbsp;&nbsp;&nbsp;&nbsp;<button id="btn_back">返回</button>
      </div>
    </form>
  </div>
  <script src="/library/js/jquery-1.7.1.min.js"></script>
  <script src="/library/courseware/course_plugin.js"></script>
  <script>
    $(window).load(function(){
      $("#btn_save").on("click",function () {
          if($.trim($("#title").val()) ==""){
            alert("请填写模板标题");
            $("#title").focus();
            return false;
          }
          if($.trim($("#name").val()) ==""){
            alert("请填写模板标识名");
            $("#name").focus();
            return false;
          }          
      })

      $("#btn_back").on("click",function () {
         location.href="template.do";
         return false;
      })

      $buptnu.cware.fitScreen();
    });
  </script>
</body>
</html>