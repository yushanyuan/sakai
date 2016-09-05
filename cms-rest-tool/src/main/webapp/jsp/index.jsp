<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
<jsp:directive.include file="/templates/header.jsp"/>
<jsp:directive.include file="/templates/includes.jsp"/>


<c:if test="${!empty alertMesage}">
	<div class="alertMessage">${ alertMesage}</div>
</c:if>
<div class="page-header">
<h2><fmt:message key="academic_session"></fmt:message></h2>
</div>

 
<ul id="actionToolBar" class="navIntraTool actionToolBar">
	<li class="firstToolBarItem"><span><a href="academAdd.htm"><fmt:message key="new"></fmt:message></a></span></li>
</ul>
 
<table  class="listHier lines nolines">
	<tr><th><fmt:message key="academic_session" /></th><th><fmt:message key="startdate" /></th><th><fmt:message key="enddate" /></th><th><fmt:message key="action" /></th></tr>
	<c:forEach items="${academis }" var="item">
		<tr><td>${item.title }</td><td><fmt:formatDate value="${item.startDate }" pattern="yyyy-MM-dd"/></td><td><fmt:formatDate value="${item.endDate }" pattern="yyyy-MM-dd"/></td><td><a href="academDelete.htm?eid=${item.eid }"><fmt:message key="remove" /></a></td></tr>
	</c:forEach>
</table>

<div class="page-header">
	<h2><fmt:message key="college" /></h2>
</div>
<ul id="actionToolBar" class="navIntraTool actionToolBar">
	<li class="firstToolBarItem"><span><a href="courseSetInput.htm"><fmt:message key="new" /></a>	</span></li>
</ul>
<table  class="listHier lines nolines">
	<tr><th><fmt:message key="id" /></th><th><fmt:message key="college" /></th><th><fmt:message key="view" /></th><th><fmt:message key="remove" /></th></tr>
	<c:forEach items="${courseSets }" var="item" varStatus="index">
		<tr><td>${index.index+1 }</td><td>${item.title }</td><td><a href="courseList.htm?courseSetEid=${item.eid }"><fmt:message key="view" /></td>
		<td><a href="courseSetDelete.htm?eid=${item.eid }"><fmt:message key="remove" /></td></tr>
	</c:forEach>
</table>


<jsp:directive.include file="/templates/footer.jsp"/>
  