<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
<jsp:directive.include file="/templates/header.jsp"/>
<jsp:directive.include file="/templates/includes.jsp"/>
<script type="text/javascript" src="/library/js/lang-datepicker/lang-datepicker.js"></script>

<h2>添加课程</h2>
 
<form action="save.htm" method="POST" enctype="multipart/form-data">
	 <input type="hidden" name="id" value="${course.id }" />
	 <table class="itemSummary">
	 	<tr>
            <th><label for="name">名称：</label></th>
            <td><input type="text" id="name" name="name" value="${course.name}"/></td>
        </tr>
       	<tr>
            <th><label for="categoryId">所属专业：</label></th>
            <td>
            	<select name="categoryId">
            		<c:forEach items="${speList }" var="s">
            			<option value="${s.id }"  <c:if test="${s.id==course.category.id }"> selected='selected'</c:if> >${s.name }</option>
            		</c:forEach>
            	</select>
            </td>
        </tr>
        <tr>
            <th><label for="teacherId">主讲老师：</label></th>
            <td>
            	<select name="teacherId">
            		<c:forEach items="${teacherList }" var="s">
            			<option value="${s.id }"  <c:if test="${s.id==course.teacher.id }"> selected='selected'</c:if> >${s.name }</option>
            		</c:forEach>
            	</select>
            </td>
        </tr>
         <tr>
            <th><label for="assistant">助教：</label></th>
            <td><input type="text" id="assistant" name="assistant" value="${course.assistant}"/></td>
        </tr>
         <tr>
            <th><label for="startDate">开课时间：</label></th>
            <td> 
            <!-- 
            <input class="Wdate" id="startDate" type="text" onfocus="new WdatePicker('startDate','%Y-%M-%D',true)" name="startDate" value="<fmt:formatDate value="${course.startDate}" pattern="yyyy-MM-dd"/>"/> -->
            <input type="text" id="startDate" name="startDate" class="form-control datepicker" value="">
            <script type="text/javascript">
				localDatePicker({
					input:'#startDate',
					useTime:1,
					parseFormat: 'YYYY-MM-DD HH:mm'
					 
				});
			</script>
            </td>
        </tr>
         <tr>
            <th><label for="endDate">学时：</label></th>
            <td> 
            	<input type="text" id="studyTime" name="studyTime" value="${course.studyTime}"/></td>
            </td>
        </tr>
        <tr>
            <th><label for="certification">认证：</label></th>
            <td>
                <select id="certification" name="certification">
                <option value="提供" <c:if test="${course.certification=='提供' }"> selected='selected'</c:if> >提供</option>
                <option value="不提供" <c:if test="${course.certification=='不提供'}"> selected='selected'</c:if> >不提供</option>
                </select>
            </td>
        </tr>
         <tr>
            <th><label for="studyNum">在线人数：</label></th>
            <td> 
            	<input type="text" id="studyNum" name="studyNum" value="${course.studyNum}"/></td>
            </td>
        </tr>
        <tr>
            <th><label for="siteId">sakai课程：</label></th>
            <td>
            	<select name="siteId">
            		<c:forEach items="${siteList }" var="s">
            			<option value="${s.id }" <c:if test="${s.id==course.siteId }"> selected='selected'</c:if> >${s.title }</option>
            		</c:forEach>
            	</select>
            </td>
        </tr>
        <tr>
            <th><label for="sakaiSiteUrl">sakai课程主页：</label></th>
            <td><textarea name="sakaiSiteUrl" cols="100" rows="2">${course.sakaiSiteUrl}</textarea></td>
        </tr>
        <tr>
            <th><label for="image">图片：</label></th>
            <td><input type="file" id="image" name="image" /></td>
        </tr>
         <tr>
            <th><label for="video">导播视频：</label></th>
            <td><input type="file" id="video" name="video" /></td>
        </tr>
        <tr>
            <th><label for="desc">简介：</label></th>
            <td><textarea name="desc" cols="100" rows="3">${course.desc}</textarea></td>
        </tr>
        <tr>
            <th><label for="syllabus">大纲：</label></th>
            <td><textarea name="syllabus" cols="100" rows="40">${course.syllabus}</textarea></td>
        </tr>
        <tr>
            <th><label for="qas">常见问题：</label></th>
            <td><textarea name="qas" cols="100" rows="20">${course.qas}</textarea></td>
        </tr>
        <c:forEach var="i" begin="1" end="8" varStatus="status">
	        <tr>
	            <th><label for="relatedCourse${status.index}">相关课程${status.index}：</label></th>
	            <td>
	            	<select name="relatedCourse" id="relatedCourse${status.index}">
	            	    <option value="" >无</option>
	            		<c:forEach items="${courseList }" var="s">
	            			<option value="${s.id }" <c:if test="${not empty course.relatedCourse && (fn:length(course.relatedCourse) > (status.index-1))}"><c:if test="${s.id==course.relatedCourse[status.index-1].id }"> selected='selected'</c:if></c:if> >${s.name }</option>
	            		</c:forEach>
	            	</select>
	            </td>
	        </tr>
        </c:forEach>
        <tr>
            <td colspan=2 align="right">
                <input type="reset" />
                <input type="submit" value="提交"/>
            </td>
        </tr>
    </table>
</form>

<jsp:directive.include file="/templates/footer.jsp"/>

