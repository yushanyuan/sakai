<%@page import="org.sakaiproject.resource.util.Constants"%>
<%@ page language="java" pageEncoding="utf-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en"><head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="Content-Style-Type" content="text/css">
    <title>模板列表</title>
    <link href="resource/styles/template.css" type="text/css" rel="stylesheet" media="all" />		
 </head>
 <body>
<div class="portletBody">
	<div class="instruction clear">
      <div style="padding:5px 0;">
        <form action="">
          <label for="">状态：</label>
          <select style="padding: 3px 5px;" name="status"><option value="">--全部--</option><option value="1">可用</option><option value="9">停用</option></select>
          &nbsp;&nbsp;&nbsp;&nbsp;<button>查询</button>&nbsp;&nbsp;&nbsp;&nbsp;<button id="btn_add">添加</button>
        </form>
      </div>
      <table class="table table_stu">
        <tbody><tr>
          <th width="15%"  class="sk_co" >标题</th>
          <th width="12%" class="sk_co" >标识名</th>
          <th class="sk_co" >描述</th>
          <th width="13%" class="sk_co" scope="col">创建时间</th>
          <th width="13%" class="sk_co" scope="col">更新时间</th>
          <th width="8%" class="sk_co" scope="col">默认</th>
          <th width="8%" class="sk_co" scope="col">状态</th>
          <th width="14%" class="sk_co" scope="col">操作</th>
        </tr>
        <s:iterator value="templateModels" id="m">
          <tr>
            <td><a href="template_edit.do?id=<s:property value='#m.id'/>"><s:property value="#m.title"/></a></td>
            <td><s:property value="#m.name"/></td>
            <td><s:property value="#m.summary"/></td>
            <td><s:date name="#m.createTime" format="yyyy-MM-dd HH:mm" /></td>
            <td><s:date name="#m.updateTime" format="yyyy-MM-dd HH:mm" /></td>
            <td>
              <s:if test="#m.isDefault">是</s:if>
              <s:else>否</s:else>
            </td>            
            <td>
              <s:if test="#m.status == 1">启用中</s:if>
              <s:else>已停用</s:else>
            </td>
            <td>
	            <a href="javascript:void(0);" onclick="downloadTemplate('<s:property value='#m.name'/>')">下载</a>&nbsp;&nbsp;
	            <a href="template_publish.do?id=<s:property value='#m.id'/>">
		            <s:if test="#m.status == 1">停用</s:if>
		            <s:else>启用</s:else>
            	</a>&nbsp;&nbsp;
            	<s:if test="#m.status != 1">
            		<a href="template_delete.do?id=<s:property value='#m.id'/>">删除</a>
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
    $("#btn_add").on("click",function () {
       location.href="template_edit.do";
       return false;
    })

    $buptnu.cware.fitScreen();
  });
  
  function downloadTemplate(name){
	  var path = '<%=Constants.TEMPLATE_PATH_URI%>';
	  var url = path + name + ".zip";
	  window.location.href = url;
  }
</script>
</body>
</html>