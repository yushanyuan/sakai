<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
<jsp:directive.include file="/templates/header.jsp"/>
<jsp:directive.include file="/templates/includes.jsp"/>


<c:if test="${!empty alertMesage}">
	<div class="alertMessage">${ alertMesage}</div>
</c:if>
<h2>学期</h2>
 
<ul id="actionToolBar" class="navIntraTool actionToolBar">
	<li class="firstToolBarItem"><span><a href="academAdd.htm">添加学期</a></span></li>
</ul>
 
<table  class="listHier lines nolines">
	<tr><th>学期标识</th><th>学期</th><th>开始时间</th><th>结束时间</th><th>操作</th></tr>
	<c:forEach items="${academis }" var="item">
		<tr><td>${item.eid }</td><td>${item.title }</td><td>${item.startDate }</td><td>${item.endDate }</td><td><a href="academDelete.htm?eid=${item.eid }">删除</a></td></tr>
	</c:forEach>
</table>


<h2>学院</h2>
<ul id="actionToolBar" class="navIntraTool actionToolBar">
	<li class="firstToolBarItem"><span><a href="courseSetInput.htm">添加学院</a>	</span></li>
</ul>
<table  class="listHier lines nolines">
	<tr><th>学院标识</th><th>学院</th><th>查看课程</th><th>删除学院</th></tr>
	<c:forEach items="${courseSets }" var="item">
		<tr><td>${item.eid }</td><td>${item.title }</td><td><a href="canonicalCourseList.htm?courseSetEid=${item.eid }">查看课程</td>
		<td><a href="courseSetDelete.htm?eid=${item.eid }">删除学院</td></tr>
	</c:forEach>
</table>


<jsp:directive.include file="/templates/footer.jsp"/>
  