<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
<jsp:directive.include file="/templates/header.jsp"/>
<jsp:directive.include file="/templates/includes.jsp"/>


<h2>添加课程</h2>
 
<form action="canonicalCourseSave.htm" method="POST">
	 <table class="itemSummary">
	 	<tr>
            <th><label for="courseEid">课程标识：</label></th>
            <td><input type="text" id="courseEid" name="courseEid" />(例：communication_network)</td>
        </tr>
        <tr>
            <th><label for="courseName">课程名称：</label></th>
            <td><input type="text" id="courseName" name="courseName" />(例：现代通信网)</td>
        </tr>
       <tr>
            <th><label for="courseSetEid">学院：</label></th>
            <td>
            	<select id="courseSetEid" name="courseSetEid">
            		<c:forEach items="${courseSets }" var="item">
            			<option value="${item.eid }">${item.title }</option>
            		</c:forEach>
            	</select>
            </td>
        </tr>
        
        <tr>
            <td colspan=2>
                <input type="reset" />
                <input type="submit" value="提交" />
            </td>
        </tr>
    </table>
</form>

<jsp:directive.include file="/templates/footer.jsp"/>

