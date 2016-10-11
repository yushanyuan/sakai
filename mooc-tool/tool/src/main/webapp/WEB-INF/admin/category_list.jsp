<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
<jsp:directive.include file="/templates/header.jsp"/>
<jsp:directive.include file="/templates/includes.jsp"/>


<c:if test="${!empty alertMesage}">
	<div class="alertMessage">${ alertMesage}</div>
</c:if>

 <ul id="actionToolBar" class="navIntraTool actionToolBar">
	<li class="firstToolBarItem"><span><a href="../teacher/list.htm">教师信息管理</a></span></li>
	<li class="firstToolBarItem"><span><a href="../category/list.htm">分类信息管理</a></span></li>
	<li class="firstToolBarItem"><span><a href="../course/list.htm">课程信息管理</a></span></li>
	<li class="firstToolBarItem"><span><a href="../recItem/list.htm">推荐课程管理</a></span></li>
</ul>

<h2>分类列表</h2>
<table  class="listHier lines nolines">
	<tr><th>分类名词</th><th>图片</th><th>操作</th></tr>
	<c:forEach items="${list }" var="t">
		<tr><td>${t.name }</td><td><img width="80px" height="80px" src="${moocServer }${t.icon }"/></td><td><a href="remove.htm?id=${t.id }">[删除]</a>  <a href="edit.htm?id=${t.id }">[修改]</a></td></tr>
	</c:forEach>
</table>
 
<ul id="actionToolBar" class="navIntraTool actionToolBar">
	<li class="firstToolBarItem"><span><a href="add.htm">添加专业</a></span></li>
</ul>
<jsp:directive.include file="/templates/footer.jsp"/>
  