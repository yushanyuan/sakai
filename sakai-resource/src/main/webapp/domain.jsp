<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
  </head>
  <script>
	try{
		var param = window.location.hash.substring(1);
		if(param!=null && param!=undefined && param!=""){
			var map = changeToParam(param);
			var idx = map["idx"];
			var title = map["title"];
			var tHeight = Number(map["frameH"]);
	   	 	parent.parent.iframeOnLoad(tHeight,idx.split(","),title.split(","));
		}else{
			param = window.location.search.substring(1);
			var map = changeToParam(param);
			var tHeight = Number(map["frameH"]);
			parent.parent.pageHeightUpdate(tHeight);
		}
    }catch(e){}
    function changeToParam(param){
    	var obj = {};
    	var ps = param.split("&");
    	for(var i=0;i<ps.length;i++){
    		var map = ps[i].split("=");
    		obj[map[0]] = map[1];
    	}
    	return obj;
    }
  </script>
</html>
