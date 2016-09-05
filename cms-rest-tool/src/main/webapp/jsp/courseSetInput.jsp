<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
<jsp:directive.include file="/templates/header.jsp"/>
<jsp:directive.include file="/templates/includes.jsp"/>
<script type="text/javascript">
	function back(){
		
		window.location.href="index.htm";
	}
</script>

<h2><fmt:message key="new" /></h2>
 
<form action="courseSetSave.htm" method="POST">
	 <table class="itemSummary">
        <tr>
            <th><label for="courseSetName"><fmt:message key="name" />：</label></th>
            <td><input type="text" id="courseSetName" name="courseSetName" /><fmt:message key="collegeName" /></td>
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

