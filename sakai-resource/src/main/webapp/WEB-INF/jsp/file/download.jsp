<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String openUrl = (String)request.getAttribute("openURL");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>文件下载页面</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<script type="text/javascript">
	window.location.href='<%=openUrl%>'
</script>
  </head>
  
  <body>
  </body>
</html>
