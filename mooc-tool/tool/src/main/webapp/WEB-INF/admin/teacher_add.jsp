<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
<jsp:directive.include file="/templates/header.jsp"/>
<jsp:directive.include file="/templates/includes.jsp"/>


<h2>添加老师</h2>
 
<form action="save.htm" method="POST" enctype="multipart/form-data">
	<input type="hidden" name="id" value="${teacher.id }" />
	 <table class="itemSummary">
	 	<tr>
            <th><label for="name">名称：</label></th>
            <td><input type="text" id="name" name="name" value="${teacher.name}"/></td>
        </tr>
        <tr>
            <th><label for="image">头像：</label></th>
            <td><input type="file" id="image" name="image" /></td>
        </tr>
        <tr>
            <th><label for="profile">简介：</label></th>
            <td><textarea name="profile" cols="100" rows="10">${teacher.profile}</textarea></td>
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

