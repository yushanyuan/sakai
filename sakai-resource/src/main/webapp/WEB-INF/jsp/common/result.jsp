<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:if test="exception ==null">{"success":true,"result":true,"msg":"您的操作已经执行成功！","data":<s:property escape='0' value="#request.data"/>}</s:if>
<s:if test="exception !=null">{"success":true,"result":false,"msg":"<s:property value="exception.message"/>"}</s:if>