<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
<jsp:directive.include file="/templates/header.jsp"/>
<jsp:directive.include file="/templates/includes.jsp"/>


<h2>添加学院</h2>
 
<form action="courseSetSave.htm" method="POST">
	 <table class="itemSummary">
	 	<tr>
            <th><label for="courseSetEid">学院标识：</label></th>
            <td><input type="text" id="courseSetEid" name="courseSetEid" />(例：buptnu)</td>
        </tr>
        <tr>
            <th><label for="courseSetName">学院名称：</label></th>
            <td><input type="text" id="courseSetName" name="courseSetName" />(例：北邮网院)</td>
        </tr>
       
        <tr>
            <td colspan=2>
                <input type="reset" />
                <input type="submit" value="提交"/>
            </td>
        </tr>
    </table>
</form>

<jsp:directive.include file="/templates/footer.jsp"/>

