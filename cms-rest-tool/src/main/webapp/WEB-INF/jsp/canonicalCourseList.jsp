<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
<jsp:directive.include file="/templates/header.jsp"/>
<jsp:directive.include file="/templates/includes.jsp"/>

<c:if test="${!empty alertMesage}">
	<div class="alertMessage">${ alertMesage}</div>
</c:if>
<h2>课程列表</h2>
 <ul id="actionToolBar" class="navIntraTool actionToolBar">
	<li class="firstToolBarItem"><span><a href="canonicalCourseInput.htm">添加课程</a></span></li>
</ul>
<table  class="listHier lines nolines">
	<tr><th>课程标识</th><th>课程</th><th>查看课程实例</th><th>删除课程</th></tr>
	<tr>
		<td>
		<c:forEach items="${canonicalCourseList }" var="item">
			<tr><td>${item.eid }</td><td>${item.title }</td><td><a href="sectionList.htm?courseSetEid=${courseSetEid}&canonicalCourseEid=${item.eid }">查看课程实例</a></td>
				<td><a href="canonicalCourseDelete.htm?courseEid=${item.eid }&courseSetEid=${courseSetEid}">删除课程</a></td>
			</tr>
		</c:forEach>
		</td>
	</tr>
</table>

 
<jsp:directive.include file="/templates/footer.jsp"/>

