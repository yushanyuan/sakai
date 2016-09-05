<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
<jsp:directive.include file="/templates/header.jsp"/>
<jsp:directive.include file="/templates/includes.jsp"/>

<c:if test="${!empty alertMesage}">
	<div class="alertMessage">${ alertMesage}</div>
</c:if>
<h2>课程实例列表</h2>
<!-- 
<label>学期：</label>${academicSession.title }
<br/>
<label>学院：</label>${courseSet.title }
<br />
 -->
<ul id="actionToolBar" class="navIntraTool actionToolBar">
	<li class="firstToolBarItem"><span><a href="sectionInput.htm?courseSetEid=${courseSetEid}">添加课程实例</a></span></li>
</ul>

<table  class="listHier lines nolines">
	<tr><th>课程实例标识</th><th>课程实例</th><th>课程名</th><th>学期</th><th>操作</th></tr>
	<c:forEach items="${sectionSet }" var="item">
		<tr><td>${item.eid }</td><td>${item.title }</td><td>${item.canonicalCourseTitle }</td><td>${item.academicSessionTitle }</td>
			<td><a href="sectionDelete.htm?courseSetEid=${courseSetEid }&canonicalCourseEid=${canonicalCourseEid}&sectionEid=${item.eid}">删除课程实例</a></td>
		</tr>
	</c:forEach>
</table>

<jsp:directive.include file="/templates/footer.jsp"/>