<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
<jsp:directive.include file="/templates/header.jsp"/>
<jsp:directive.include file="/templates/includes.jsp"/>

<h2>添加课程实例</h2>
 
<form action="sectionSave.htm" method="POST">
	<input type="hidden" name="courseSetEid" value="${courseSetEid }" />
	 <table  class="itemSummary">
	 	
	 	<tr>
            <th><label>学院：</label></th>
            <td>${courseSetTitle }</td>
        </tr>
	 	<tr>
            <th><label>学期：</label></th>
            <td>
			<select id="academicSessionEid" name="academicSessionEid">
				<c:forEach items="${academicSessions }" var="item">
					<option value="${item.eid}">${item.title }</option>
				</c:forEach>
			</select>
			</td>
        </tr>
	 	
	 	<tr><th><label>课程：</label></th> 
			<td>
			<select id="canonicalCourseEid" name="canonicalCourseEid">
				<c:forEach items="${canonicalCourseList }" var="item">
					<option value="${item.eid}">${item.title }</option>
				</c:forEach>
			</select>
			</td>
		</tr>
	 	<tr>
            <th><label for="courseOfferingEid">课程标识：</label></th>
            <td><input type="text" id="courseOfferingEid" name="courseOfferingEid" />(例：2015communication_network)</td>
        </tr>
        <tr>
            <th><label for="courseOfferingName">课程名称：</label></th>
            <td><input type="text" id="courseOfferingName" name="courseOfferingName" />(例：2015现代通信网)</td>
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

