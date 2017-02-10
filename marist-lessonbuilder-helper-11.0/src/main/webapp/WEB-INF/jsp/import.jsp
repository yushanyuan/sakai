<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
<jsp:directive.include file="/templates/header.jsp"/>
<jsp:directive.include file="/templates/includes.jsp"/>


<h2>import melete zip</h2>
 
<form action="save.htm" method="POST" enctype="multipart/form-data">
	<input type="hidden" name="toolId" value="${toolId}" />
 <table class="itemSummary">
	 	 
        <tr>
            <th><label for="file">zip file：</label></th>
            <td><input type="file" id="zip" name="zip" /></td>
        </tr>
        
        <tr>
            <td colspan=2>
                <input type="reset" />
                <input type="submit" value="submit"/>
            </td>
        </tr>
    </table>
</form>

<jsp:directive.include file="/templates/footer.jsp"/>
