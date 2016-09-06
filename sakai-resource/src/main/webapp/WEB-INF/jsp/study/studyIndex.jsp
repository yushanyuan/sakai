<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String url = (String)request.getParameter("url");
%>
<html>
<head>
<script type="text/javascript">
	var tplUrl = "<%=url%>";

	function history(){
		if(tplUrl=="" || tplUrl=="null"){
			document.getElementById("tip").innerHTML="未找到课程模板";
			return;
		}
		window.location.href=tplUrl;
	}
</script>
</head>
<body onload="history()">
	<h1 id="tip"></h1>
</body>
</html>
