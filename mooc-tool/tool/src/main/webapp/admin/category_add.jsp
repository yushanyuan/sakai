<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
<jsp:directive.include file="/templates/header.jsp"/>
<jsp:directive.include file="/templates/includes.jsp"/>


<h2>添加分类</h2>
 
<form action="save.htm" method="POST" enctype="multipart/form-data">
	<input type="hidden" name="id" value="${category.id }" />
	 <table class="itemSummary">
	 	<tr>
            <th><label for="name">名称：</label></th>
            <td><input type="text" id="name" name="name" value="${category.name }"/></td>
        </tr>
        <tr>
            <th><label for="icon">图片：</label></th>
            <td><input type="file" id="icon" name="icon" /></td>
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

