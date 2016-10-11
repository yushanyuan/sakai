<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
<jsp:directive.include file="/templates/header.jsp"/>
<jsp:directive.include file="/templates/includes.jsp"/>


<h2>添加推荐课程</h2>
 
<form action="save.htm" method="POST" enctype="multipart/form-data">
	 <table class="itemSummary">
	 	
       	<tr>
            <th><label for="courseId">选课程：</label></th>
            <td>
            	<select name="courseId">
            		<c:forEach items="${courseList }" var="s">
            			<option value="${s.id }"  <c:if test="${s.id==recItem.course.id }"> selected='selected'</c:if> >${s.name }</option>
            		</c:forEach>
            	</select>
            </td>
        </tr>
        <tr>
            <th><label for="recValue">推荐值：</label></th>
            <td><input type="text" id="recValue" name="recValue" value="${recItem.course.recValue}"/></td>
        </tr>
        <tr>
            <td colspan=2 align="right">
                <input type="reset" />
                <input type="submit" value="提交"/>
            </td>
        </tr>
    </table>
</form>

<jsp:directive.include file="/templates/footer.jsp"/>

