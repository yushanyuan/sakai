<%@ page language="java" pageEncoding="utf-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en"><head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="Content-Style-Type" content="text/css">
    <title>作业列表</title>
    <link href="/library/skin/tool_base.css" type="text/css" rel="stylesheet" media="all">
    <link href="/library/skin/newgray/tool.css" type="text/css" rel="stylesheet" media="all" />
   
	<link href="resource/styles/style.css" type="text/css" rel="stylesheet" media="all" />

		
 </head>
 <body>
<div class="portletBody">		 			 
	<div class="instruction clear">
      <table class="table table_stu">
        <tbody><tr>
          <th width="25%"  class="sk_co" >标题</th>
          <th width="10%" class="sk_co" >属性</th>
          <th width="8%" class="sk_co" >最高分</th>
          <th width="10%" class="sk_co" scope="col">通过状态</th>
          <th width="11%" class="sk_co" scope="col">通过条件（得分）</th>
          <th width="22%" class="sk_co" scope="col">做作业</th>
          <th width="14%" class="sk_co" scope="col">查看历史记录</th>
        </tr>
        <s:set name="nowTime" value="new java.util.Date()"></s:set>
      	<s:iterator value="meleteTestRecordModelList" var="m">
      	<tr>
          <td  class="sk_cl"><s:property value="#m.name"/></td>
          <td class="sk_cl">必修</td>
           <td class="sk_cl"><s:property value="#m.score"/></td>
          <td class="sk_cl">
          <s:if test="#m.status eq 1">
          	通过
          </s:if>
          <s:else>
          	未通过
          </s:else>	
          </td>
          <td class="sk_cl"><s:property value="#m.requirement"/></td>
          <td class="sk_cl">
          <s:if test='#m.isOpen eq 0'>
          <a class="link_ico go" href="studyPaper_writeTestInit.do?testId=<s:property value="#m.testId"/>&studyrecordId=<s:property value="studyrecordId"/>" target="_blank"><img src="resource/icons/icon_e.png">做作业</a>
	          <s:if test='#m.endOpenDate != null && #m.endOpenDate!=""'>
	          	<s:property value="#m.endOpenDate"/>截止
	          </s:if>
          </s:if>
          <s:elseif test='#m.isOpen eq 1'>
          	<span><s:property value="#m.endOpenDate"/>已截止</span>
          </s:elseif>
          <s:else>
          	<span><s:property value="#m.startOpenDate"/>开始</span>
          </s:else>	
          </td>
          <td class="sk_cl">
	          <s:if test='#m.isOpen eq 0 || #m.isOpen eq 1'>
	          <a class="link_ico" href="melete_getTestAttemptRecord.do?testRecordId=<s:property value="#m.testRecordId"/>" target="_blank"><img src="resource/icons/icon_clock.png">查看</a>
	          </s:if>
          </td>
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