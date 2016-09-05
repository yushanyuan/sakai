<%@ page language="java" pageEncoding="utf-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en"><head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="Content-Style-Type" content="text/css">
    <title>作业记录列表</title>
    <link href="/library/skin/tool_base.css" type="text/css" rel="stylesheet" media="all">
    <link href="/library/skin/newgray/tool.css" type="text/css" rel="stylesheet" media="all" />
	<link href="resource/styles/style.css" type="text/css" rel="stylesheet" media="all" />
 </head>
 <body>
<div class="portletBody">		 			 
	<h3>该课程中的作业记录列表如下：</h3>
	<div class="instruction clear">
      <table class="table table_stu">
        <tbody><tr>
          <th width="25%"  class="sk_co" >标题</th>
          <th width="12%" class="sk_co" >完成日期</th>
          <th width="12%" class="sk_co" scope="col">客观题</th>
          <th width="12%" class="sk_co" scope="col">主观题</th>
          <th width="15%" class="sk_co" scope="col">分数</th>
        </tr>
      	<s:iterator value="meleteTestAttempModelMap" var="m">
      	<tr>
          <td  class="sk_cl"><a href="melete_getTestAttemptRecordPaper.do?testPaperid=<s:property value="#m.testPaperid"/>&testattemptId=<s:property value="#m.testattemptId"/>&testId=<s:property value="#m.testId"/>" target="_blank"><s:property value="#m.name"/></a></td>
          <td class="sk_cl"><s:date name="#m.endTime" format="MM/dd/yyyy HH:mm"/></td>
          <td class="sk_cl">
          <s:property value="#m.objScore"/>
          </td>
          <td class="sk_cl"><s:property value="#m.subScore"/></td>
          <td class="sk_cl"><s:property value="#m.score"/></td>
        </tr>
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