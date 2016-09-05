<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
<jsp:directive.include file="/templates/header.jsp"/>
<jsp:directive.include file="/templates/includes.jsp"/>
<script type="text/javascript">
	function back(){
		
		window.location.href="courseList.htm?courseSetEid="+document.getElementById("courseSetEid").value;
	}
</script>
<c:if test="${!empty alertMesage}">
	<div class="alertMessage">${ alertMesage}</div>
</c:if>
<div class="page-header">
	<h2><fmt:message key="studentList" /></h2>
</div>
<ul id="actionToolBar" class="navIntraTool actionToolBar">
	<li class="firstToolBarItem"><span><a href="courseStuAdd.htm?courseEid=${courseEid }&courseSetEid=${courseSetEid}"><fmt:message key="importEnrollment" /></a></span></li>
</ul>
<table  class="listHier lines nolines">
	<tr><th><fmt:message key="id" /></th><th><fmt:message key="userID" /></th><th><fmt:message key="fullname" /></th></tr>
		<c:forEach items="${userList }" var="u" varStatus="index">
			<tr><td>${index.index+1 }</td><td>${u.eid }</td><td>${u.lastName } ${u.firstName } </td>
			</tr>
		</c:forEach>
</table>

<input type="hidden" name="courseSetEid" id="courseSetEid" value="${courseSetEid }" />
<input class="btn" type="button" value="返回" onclick="back()"/>
<jsp:directive.include file="/templates/footer.jsp"/>

