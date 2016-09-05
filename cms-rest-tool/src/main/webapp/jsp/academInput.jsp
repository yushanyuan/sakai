<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
<jsp:directive.include file="/templates/header.jsp"/>
<jsp:directive.include file="/templates/includes.jsp"/>

<script type="text/javascript">
	function back(){
		
		window.location.href="index.htm";
	}
</script>
<h2><fmt:message key="new" /></h2>
 
<form action="academSave.htm" method="POST">
	 <table class="itemSummary">
        <tr>
            <th><label for="academName"><fmt:message key="academic_session" />：</label></th>
            <td><input type="text" id="academName" name="academName" /><fmt:message key="academName" /></td>
        </tr>
        <tr>
            <th><label for="startDate"><fmt:message key="startdate" />：</label></th>
            <td><input type="text" id="startDate" name="startDate" /><fmt:message key="startdateEG" /></td>
        </tr>
         <tr>
            <th><label for="endDate"><fmt:message key="enddate" />：</label></th>
            <td><input type="text" id="endDate" name="endDate" /><fmt:message key="enddateEG" /></td>
        </tr>
        <tr>
            <td colspan=2>
                <input type="button" class="btn" value="取消" onclick="back()"/>
                <input type="submit" class="btn" value="提交"/>
            </td>
        </tr>
    </table>
</form>

<jsp:directive.include file="/templates/footer.jsp"/>

