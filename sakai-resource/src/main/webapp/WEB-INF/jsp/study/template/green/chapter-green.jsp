<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="org.sakaiproject.resource.api.course.vo.Node" %>
<%@ page import="org.sakaiproject.resource.api.study.model.MeleteStudyRecordModel" %>
<%@ page import="java.util.*" %>

<%
	MeleteStudyRecordModel studyRecord = (MeleteStudyRecordModel)request.getAttribute("studyRecord");
	List<Node> nodeList = (List<Node>)request.getAttribute("nodeList");
	boolean maxThan7 = nodeList.size() > 7;
	Long studyRecordId = studyRecord.getStudyrecordId();
	Float score = studyRecord.getScore();
	String rightTitleInit = "";
	String rightStudyTimeInit = "";
	String rightMyTimeInit = "";
	
	Map<String, String> styleid = new HashMap<String, String>();
	styleid.put("1","01");
	styleid.put("2","02");
	styleid.put("3","03");
	styleid.put("4","04");
	styleid.put("5","05");
	styleid.put("6","06");
	styleid.put("7","07");
	styleid.put("8","08");
	styleid.put("9","09");
	styleid.put("10","10");
	styleid.put("11","11");
	styleid.put("12","12");
	styleid.put("13","13");
	styleid.put("14","14");
	
	Map<String, String> shortTitle = new HashMap<String, String>();
	shortTitle.put("1","第一章");
	shortTitle.put("2","第二章");
	shortTitle.put("3","第三章");
	shortTitle.put("4","第四章");
	shortTitle.put("5","第五章");
	shortTitle.put("6","第六章");
	shortTitle.put("7","第七章");
	shortTitle.put("8","第八章");
	shortTitle.put("9","第九章");
	shortTitle.put("10","第十章");
	shortTitle.put("11","第十一章");
	shortTitle.put("12","第十二章");
	shortTitle.put("13","第十三章");
	shortTitle.put("14","第十四章");
%>

<html>
<head>
<title>课程空间模板-绿</title>
<meta http-equiv="X-UA-Compatible" content="IE=7" />
<link href="resource/styles/template/ke-R.css" type="text/css" rel="stylesheet" media="all" />
<script type="text/javascript" language="JavaScript" src="/library/js/headscripts.js"></script>
<script type=text/javascript>
	var samaArray = ["01","02","03","04","05","06","07","08","09","10","11","12","13","14"];
			
	function setTab03Syn (i, nodeListSize)
	{
		selectTab03Syn(i, nodeListSize);
	}
	
	function selectTab03Syn (i,nodeListSize)
	{
		var j;
		for(j=0;j<nodeListSize;j++) {
			if(j==i) {
				document.getElementById("font" + samaArray[i]).style.color="#a4b5c2";
			} else {
				document.getElementById("font" + samaArray[j]).style.color="#000000";
			}
		}
	}
	
	function pageHeightInit(){
		<%= request.getAttribute("sakai.html.body.onload") %>
	}
</script>
</head>
<body onload="pageHeightInit()">
	<div id="main-G">
		<table width="98%" border="0" cellspacing="0">
		  <tr>
			<td class="bj-left">&nbsp;</td>
			<td class="bj-tu">
			<div class="zhang-z0"></div>
			<div class="zhang-z1">
				<%
				if((nodeList != null) && (nodeList.size() != 0)) {
					for(int i=0;(i<nodeList.size()) && (i<7);i++){
						Node node = nodeList.get(i);
						String id = node.getId();
						String title = node.getTitle();
						//建议时长
						String studyTime = node.getStudyTime();
						//我的时长
						String myTime = node.getMyTime();
						if(i==0) {
							rightTitleInit = title;
							rightStudyTimeInit = studyTime;
							rightMyTimeInit = myTime;
						}
						System.out.println("状态   " + node.getPassStatus());
				%>
				<div id="font<%=styleid.get(String.valueOf(i+1)) %>" class="tab<%=styleid.get(String.valueOf(i+1))%>" onMouseOver="a('<%= title %>', '<%= studyTime %>' ,'<%= myTime %>');setTab03Syn(<%=i%>, <%=nodeList.size()%>);document.getElementById('bg')" onclick="location.href='studySpace_fowardTemplateRGBModule.do?moduleId=<%=id%>&studyrecordId=<%=studyRecordId%>&template=green'"><img src="resource/images/template/row1.gif" width="14" height="14"/><%=shortTitle.get(String.valueOf(i+1)) %></div>
				<%
					}
				}
				%>
			</div><!--zhang-z1-->
			<div class="zhang-z2">
				<%
				if((nodeList != null) && (nodeList.size() != 0)) {
					if(maxThan7) {
						for(int i=7;(i<nodeList.size()) && (i<14);i++){
							Node node = nodeList.get(i);
							String id = node.getId();
							String title = node.getTitle();
							//建议时长
							String studyTime = node.getStudyTime();
							//我的时长
							String myTime = node.getMyTime();
				%>
					<div id="font<%=styleid.get(String.valueOf(i+1)) %>" class="tab<%=styleid.get(String.valueOf(i+1))%>" onMouseOver="a('<%= title %>', '<%= studyTime %>' ,'<%= myTime %>');setTab03Syn(<%=i%>, <%=nodeList.size()%>);document.getElementById('bg')" onclick="location.href='studySpace_fowardTemplateRGBModule.do?moduleId=<%=id%>&studyrecordId=<%=studyRecordId%>&template=green'"><img src="resource/images/template/row1.gif" width="14" height="14"/><%=shortTitle.get(String.valueOf(i+1)) %></div>
				<%
						}
					}
				}
				%>
			</div><!--zhang-z2-->
			
	  <div class="zhang-an">
			<div id=TabTab03Con01>
				<div class="z1-an"><span id ="moudleChinese" ><%=rightTitleInit%></span></div>
				<div class="times">
					<p>建议时长：<img src="resource/images/template/time.jpg" width="10" height="10" />&nbsp;<span id ="studyTime"><%=rightStudyTimeInit%>分钟</span></p>
					<p>我的时长：<img src="resource/images/template/time.jpg" width="10" height="10" />&nbsp;<span id ="myTime"><%=rightMyTimeInit%>分钟</span></p>
			  </div>
				<div class="zt-an">课程平时成绩：<span style="margin-right:5px; margin-left:3px;" ><%=score%></span><img src="resource/images/template/sy-tm_15.gif" width="10" height="12" style="margin-right:5px; margin-right:3px;"  />
					<a href="statistics.do?studyrecordId=<%=studyRecordId%>" target='_blank'>学习记录统计</a>
				</div>
			</div>
	  </div>
			</td>
			<td class="bj-right">&nbsp;</td>
		  </tr>
		</table>
		
		<script type="text/javascript">
			function a(title, studyTime ,myTime) {
				document.getElementById('moudleChinese').innerHTML = title;
				document.getElementById('studyTime').innerHTML = studyTime;
				document.getElementById('myTime').innerHTML = myTime;
			}
		</script>
		
	</div>
</body>
</html>
