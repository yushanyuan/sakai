<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
<jsp:directive.include file="/templates/header.jsp"/>
<jsp:directive.include file="/templates/includes.jsp"/>
<script type="text/javascript">
	function back(){
		
		window.location.href="courseStuList.htm?courseEid="+document.getElementById("courseEid").value+"&courseSetEid="+document.getElementById("courseSetEid").value;
	}
</script>

<h2><fmt:message key="importEnrollment" /></h2>
<div class="instruction"><fmt:message key="msg_importStudents" /></div>
<form action="courseStuSave.htm" method="POST"  enctype="multipart/form-data">
	<input type="hidden" name="siteId" value="${site.id }" />
	<input type="hidden" name="courseEid" id="courseEid" value="${courseEid }" />
	<input type="hidden" name="courseSetEid" id="courseSetEid" value="${courseSetEid }" />
	 <table class="itemSummary">
	 	<tr>
            <th><label for="id"><fmt:message key="course" />：</label></th>
            <td>${site.title }</td>
        </tr>
        <tr>
           <th><label for="file"><fmt:message key="file" />：</label></th>
            <td><input type="file" id="fileUpload" name="fileUpload" /></td>
        </tr>
        <tr>
           <th><label for="studentTemplate"><fmt:message key="studentTemplate" />：</label></th>
            <td><a href="download.htm?fileName=students.txt">students.txt</a></td>
        </tr>
        <tr>
            <td colspan=2>
                <input class="btn" type="button" value="取消" onclick="back()"/>
                <input class="btn" type="submit" value="提交"/>
            </td>
        </tr>
    </table>
</form>

<jsp:directive.include file="/templates/footer.jsp"/>

