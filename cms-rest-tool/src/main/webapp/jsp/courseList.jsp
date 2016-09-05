<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
<jsp:directive.include file="/templates/header.jsp"/>
<jsp:directive.include file="/templates/includes.jsp"/>

<c:if test="${!empty alertMesage}">
	<div class="alertMessage">${ alertMesage}</div>
</c:if>
<div class="page-header">
	<h2><fmt:message key="courseList" /></h2>
</div>
<ul id="actionToolBar" class="navIntraTool actionToolBar">
	<li class="firstToolBarItem"><span><a href="courseAdd.htm?courseSetEid=${courseSetEid }"><fmt:message key="importCourseFile" /></a></span></li>
</ul>
<table  class="listHier lines nolines">
	<tr><th><fmt:message key="id" /></th><th><fmt:message key="course" /></th><th><fmt:message key="viewEnrollment" /></th><th><fmt:message key="remove" /></th></tr>
		<c:forEach items="${canonicalCourseList }" var="item" varStatus="index">
			<tr><td>${index.index+1 }</td><td>${item.title }</td><td><a href="courseStuList.htm?courseEid=${item.eid }&courseSetEid=${courseSetEid}"><fmt:message key="view" /></a></td>
				<td><a href="courseDelete.htm?courseEid=${item.eid }&courseSetEid=${courseSetEid}"><fmt:message key="remove" /></a></td>
			</tr>
		</c:forEach>
</table>

 
<jsp:directive.include file="/templates/footer.jsp"/>

