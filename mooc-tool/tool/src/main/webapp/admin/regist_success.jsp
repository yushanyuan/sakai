<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
<jsp:directive.include file="/templates/header.jsp"/>
<jsp:directive.include file="/templates/includes.jsp"/>
 
<body>
<form id="mform" name="mform" method="post" action="/authn/login" enctype="application/x-www-form-urlencoded">
<input type="hidden"  name="eid" value="${username }"/>
<input type="hidden"  name="pw"  value="${password }"/>
<input type="hidden" name="url" id="url"  value="${nextPage }"/>
如果等待时间过长，请手动点击按钮直接跳转
<input id="submitbut" type="submit" value="跳转"/>

</form>	
<script type="text/javascript">
try{
	document.forms["mform"].submit();
}catch(e){
	alert("由于您的浏览器安全设置过高，不能自动跳转到教学系统，请手动点击“进入教学系统”按钮。\r\n或者将浏览器安全设置恢复为“默认设置”，下次就可以自动跳转了。");
}
</script>
 