<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
<jsp:directive.include file="/templates/header.jsp"/>
<jsp:directive.include file="/templates/includes.jsp"/>


<h2>添加学期</h2>
 
<form action="academSave.htm" method="POST">
	 <table class="itemSummary">
	 	<tr>
            <th><label for="eid">学期标识：</label></th>
            <td><input type="text" id="eid" name="eid" />(例：2015spring)</td>
        </tr>
        <tr>
            <th><label for="academName">学期：</label></th>
            <td><input type="text" id="academName" name="academName" />(例：2015春)</td>
        </tr>
        <tr>
            <th><label for="startDate">开始日期：</label></th>
            <td><input type="text" id="startDate" name="startDate" />(例：2015-02-25)</td>
        </tr>
         <tr>
            <th><label for="endDate">结束日期：</label></th>
            <td><input type="text" id="endDate" name="endDate" />(例：2015-06-30)</td>
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

