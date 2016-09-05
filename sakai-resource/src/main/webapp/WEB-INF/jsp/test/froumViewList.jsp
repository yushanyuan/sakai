<%@ page language="java" pageEncoding="utf-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en"><head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="Content-Style-Type" content="text/css">
    <title>论坛列表</title>
    <link href="/library/skin/tool_base.css" type="text/css" rel="stylesheet" media="all">
    <link href="/library/skin/newgray/tool.css" type="text/css" rel="stylesheet" media="all" />
    <link href="resource/styles/style.css" type="text/css" rel="stylesheet" media="all" />
 </head>
 <body>
<div class="portletBody">		 			 
	<div class="instruction clear">
      <table class="table table_stu">
        <tbody>
        <tr>
          <th width="25%"  class="sk_co" >论坛名称</th>
          <th width="12%" class="sk_co" >创建时间</th>
        </tr>
      	<s:iterator value="meleteForumModelList" var="f">
      	<tr>
          <td  class="sk_cl"><a href="studyPaper_forumInit.do?forumId=<s:property value="#f.id"/>&studyrecordId=<s:property value="studyrecordId"/>" target="_blank"><s:property value="#f.name"/></a></td>
          <td class="sk_cl">  <s:date name="#f.creationDate" format="MM/dd/yyyy HH:mm"/></td>
      	</s:iterator>
      	</tbody>
      </table>
	</div>								
</div>
<script src="/library/js/jquery-1.7.1.min.js"></script> 
<script src="/library/courseware/course_plugin.js"></script> 
<script>
  $(window).load(function(){
    $buptnu.cware.fitScreen();
  });
</script>
</body>
</html>