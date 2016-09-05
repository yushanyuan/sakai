<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="org.sakaiproject.resource.api.course.vo.Node" %>
<%@ page import="org.sakaiproject.resource.api.study.model.MeleteStudyRecordModel" %>
<%@ page import="org.sakaiproject.resource.api.course.model.MeleteModuleModel" %>
<%@ page import="java.util.*" %>

<%
	String path = request.getContextPath();
	MeleteStudyRecordModel studyRecord = (MeleteStudyRecordModel)request.getAttribute("studyRecord");
	MeleteModuleModel module = (MeleteModuleModel)request.getAttribute("module");//当前"模块"
	List<Node> nodeList = (List<Node>)request.getAttribute("nodeList");
	Map<String, List<Node>> setionActivityMap = (Map<String, List<Node>>)request.getAttribute("setionActivityMap");
	Long studyRecordId = studyRecord.getStudyrecordId();
	
	boolean courseGuideExist = false;
	boolean teachGoalExist = false;
	boolean keyAndDifficute = false;
	boolean teachMethodExist = false;
	boolean learnNavigationExist = false;
	if(((module.getCourseGuide()!=null) && (!"".equals(module.getCourseGuide())))) {
		courseGuideExist = true;
	}
	if((module.getTeachGoal()!=null) && (!"".equals(module.getTeachGoal()))){
		teachGoalExist = true;
	}
	
	if((module.getKeyAndDifficute()!=null) && (!"".equals(module.getKeyAndDifficute()))){
		keyAndDifficute = true;
	}
	if((module.getTeachMethod()!=null) && (!"".equals(module.getTeachMethod()))){
		teachMethodExist = true;
	}
	
	if((module.getLearnNavigation()!=null) && (!"".equals(module.getLearnNavigation()))){
		learnNavigationExist = true;
	}
	
	//存放前测，作业，论坛的list容器
	List<Node> selftTestList = new ArrayList<Node>();
	List<Node> testList = new ArrayList<Node>();
	List<Node> forumList = new ArrayList<Node>();
%>

<html>
<head>
<title>章页面-蓝</title>
<meta http-equiv="X-UA-Compatible" content="IE=7" />
<link rel="stylesheet" type="text/css" href="<%=path %>/resource/styles/ext-all.css" />
<link rel="stylesheet" type="text/css" href="<%=path %>/resource/styles/ColumnNodeUI.css" />
<link rel="stylesheet" type="text/css" href="<%=path %>/resource/styles/ext-extends.css" />
<link rel="stylesheet" type="text/css" href="<%=path %>/resource/styles/GroupSummary.css" />
<script type="text/javascript" src="<%=path %>/resource/scripts/extjs/ext-base.js"></script>
<script type="text/javascript" language="JavaScript" src="/library/js/headscripts.js"></script>
<script type="text/javascript" src="<%=path %>/resource/scripts/extjs/ext-all.js"></script>
<script type="text/javascript" src="<%=path %>/resource/scripts/extjs/ext-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=path %>/resource/scripts/extjs/ColumnNodeUI.js"></script>
<script type="text/javascript" src="<%=path %>/resource/scripts/extjs/GroupSummary.js"></script>
<script type="text/javascript" src="<%=path %>/resource/scripts/util.js"></script>

<link href="resource/styles/template/zhang.css" rel="stylesheet" type="text/css" />
<script type=text/javascript>
<!--
var courseGuideExist = <%=courseGuideExist %>;
	var teachGoalExist = <%=teachGoalExist %>;
	var keyAndDifficute = <%=keyAndDifficute %>;
	var teachMethodExist = <%=teachMethodExist %>;
	var learnNavigationExist = <%=learnNavigationExist %>;

	function setTab03Syn ( mark )
	{
		selectTab03Syn(mark);
	}
	
	function selectTab03Syn ( mark )
	{
		if(document.getElementById("font1")) {
			if(mark=="courseGuide"){
				document.getElementById("font1").style.color="#ea7e27";
				document.getElementById("TabTab03Con31").style.display="block";
			}else{
				document.getElementById("font1").style.color="#006600";
				document.getElementById("TabTab03Con31").style.display="none";
			}
		}
		
		if(document.getElementById("font2")) {
			if(mark=="teachGoal"){
				document.getElementById("font2").style.color="#ea7e27";
				document.getElementById("TabTab03Con32").style.display="block";
			}else{
				document.getElementById("font2").style.color="#006600";
				document.getElementById("TabTab03Con32").style.display="none";
			}
		}
		
		if(document.getElementById("font3")) {
			if(mark=="keyAndDifficute"){
				document.getElementById("font3").style.color="#ea7e27";
				document.getElementById("TabTab03Con33").style.display="block";
			}else{
				document.getElementById("font3").style.color="#006600";
				document.getElementById("TabTab03Con33").style.display="none";
			}
		}
		
		if(document.getElementById("font4")) {
			if(mark=="teachMethod"){
				document.getElementById("font4").style.color="#ea7e27";
				document.getElementById("TabTab03Con34").style.display="block";
			}else{
				document.getElementById("font4").style.color="#006600";
				document.getElementById("TabTab03Con34").style.display="none";
			}
		}
		
		if(document.getElementById("font5")) {
			if(mark=="learnNavigation"){
				document.getElementById("font5").style.color="#ea7e27";
				document.getElementById("TabTab03Con35").style.display="block";
			}else{
				document.getElementById("font5").style.color="#006600";
				document.getElementById("TabTab03Con35").style.display="none";
			}
		}
	}

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}

function pageHeightInit(){
		<%= request.getAttribute("sakai.html.body.onload") %>
	}
	
	function topDisplayInit(){
		if(courseGuideExist) {
			document.getElementById("font1").style.color="#ea7e27";
			document.getElementById("TabTab03Con31").style.display="block";
			return;
		}
		
		if(teachGoalExist) {
			document.getElementById("font2").style.color="#ea7e27";
			document.getElementById("TabTab03Con32").style.display="block";
			return;
		}
		
		if(keyAndDifficute) {
			document.getElementById("font3").style.color="#ea7e27";
			document.getElementById("TabTab03Con33").style.display="block";
			return;
		}
		
		if(teachMethodExist) {
			document.getElementById("font4").style.color="#ea7e27";
			document.getElementById("TabTab03Con34").style.display="block";
			return;
		}
		
		if(learnNavigationExist) {
			document.getElementById("font5").style.color="#ea7e27";
			document.getElementById("TabTab03Con35").style.display="block";
			return;
		}
	}
	
	function selfTestInitDisplay(id, attemptNumber){
		if(attemptNumber < 1) {
			window.location.href='studyPaper_writeSelfTestInit.do?testId=' + id + '&studyrecordId=<%=studyRecordId%>';
		} else {
			showWarn('前测只能进行一次!',400);
		}
	}
//-->
</script>
</head>

<body onload="pageHeightInit();topDisplayInit();">
	<div class="main-B">
		<%
			if(courseGuideExist || teachGoalExist || keyAndDifficute || teachMethodExist || learnNavigationExist) {
		%>
		<div class="c-1">
			<%
				if((module.getCourseGuide()!=null) && (!"".equals(module.getCourseGuide()))) {
			%>
			<div id="font1" class="tab1" onclick="setTab03Syn('courseGuide');">课前引导</div>
			<div class="cc2"></div>
			<%
				}
	 		%>
	 		
	 		<%
				if((module.getTeachGoal()!=null) && (!"".equals(module.getTeachGoal()))) {
			%>
			<div id="font2" class="tab2" onclick="setTab03Syn('teachGoal');">教学目标</div>
			<div class="cc2"></div>
			<%
				}
	 		%>
	 		
	 		<%
				if((module.getKeyAndDifficute()!=null) && (!"".equals(module.getKeyAndDifficute()))) {
			%>
			<div id="font3" class="tab3" onclick="setTab03Syn('keyAndDifficute');">重点难点</div>
			<div class="cc2"></div>
			<%
				}
	 		%>
	 		
	 		<%
				if((module.getTeachMethod()!=null) && (!"".equals(module.getTeachMethod()))) {
			%>	
			<div id="font4" class="tab4" onclick="setTab03Syn('teachMethod');">教学方法</div>
			<div class="cc2"></div>
			<%
				}
	 		%>
	 		
	 		<%
				if((module.getLearnNavigation()!=null) && (!"".equals(module.getLearnNavigation()))) {
			%>	
			<div id="font5" class="tab5" onclick="setTab03Syn('learnNavigation');">学习导航</div>
			<%
				}
	 		%>
		</div>
		<div class="zhang-an">
			<%
				if((module.getCourseGuide()!=null) && (!"".equals(module.getCourseGuide()))) {
			%>
			<div id=TabTab03Con31>
				<div class="z1-an"><img src="resource/images/template/jian.jpg" width="13" height="12" />&nbsp;&nbsp;<%=module.getCourseGuide() %></div>
			</div>
			<%
				}
	 		%>
	 		
	 		<%
				if((module.getTeachGoal()!=null) && (!"".equals(module.getTeachGoal()))) {
			%>
			<div id=TabTab03Con32 style="display:none">
				<div class="z1-an"><img src="resource/images/template/jian.jpg" width="13" height="12" />&nbsp;&nbsp;<%=module.getTeachGoal() %></div>
			</div>
			<%
				}
	 		%>
	 		
	 		<%
				if((module.getKeyAndDifficute()!=null) && (!"".equals(module.getKeyAndDifficute()))) {
			%>
			<div id=TabTab03Con33 style="display:none"> 
				<div class="z1-an"><img src="resource/images/template/jian.jpg" width="13" height="12" />&nbsp;&nbsp;<%=module.getKeyAndDifficute() %></div>
			</div>
			<%
				}
	 		%>
	 		
	 		<%
				if((module.getTeachMethod()!=null) && (!"".equals(module.getTeachMethod()))) {
			%>	
			<div id=TabTab03Con34 style="display:none">
				<div class="z1-an"><img src="resource/images/template/jian.jpg" width="13" height="12" />&nbsp;&nbsp;<%=module.getTeachMethod() %></div>
			</div>
			<%
				}
	 		%>
	 		
	 		<%
				if((module.getLearnNavigation()!=null) && (!"".equals(module.getLearnNavigation()))) {
			%>
			<div id=TabTab03Con35 style="display:none">
				<div class="z1-an"><img src="resource/images/template/jian.jpg" width="13" height="12" />&nbsp;&nbsp;<%=module.getLearnNavigation() %></div>
			</div>
			<%
				}
	 		%>	
		</div>
		<%
			}
		%>
		
	  <div class="celss">&nbsp;</div>
	  <div class="c-1">
	       <table width="97%" border="0" cellspacing="0" cellpadding="0">
			  <tr>
				<td width="10%"><img src="resource/images/template/index1-lie-left-B.jpg" width="140" height="61" /></td>
				<td width="89%" class="cc3">&nbsp;</td>
				<td width="1%"><img src="resource/images/template/index1-lie-right-B.jpg" width="5" height="56" /></td>
			  </tr>
			</table>
		</div>
		
		<%
			if((nodeList!=null) && (nodeList.size()>0)) {
				for(int i=0;i<nodeList.size();i++){
					Node node = nodeList.get(i);
					String id = node.getId();
					String title = node.getTitle();
					String type = node.getType();
					String required = node.getRequired();
					//建议学习时长
					String studyTime = node.getStudyTime();
					//我的时长
					String myTime = node.getMyTime();
					
					if((myTime==null) || ("".equals(myTime))) {
						myTime = "0";
					}
					
					// 当前节点为"模块"
					if(type.equals("2")){
		%>
		<div class="zhang-jie">
	       <table border="0" cellpadding="0" cellspacing="0" width="96%" >
		 			<tr>
               			 <td width="60%" class="z-jie1"><a href="location.href='studySpace_fowardTemplateRGBModule.do?moduleId=<%=id%>&studyrecordId=<%=studyRecordId%>&template=index-1B'"><%=title %></a></td>
						 <td width="10%" ></td>
               			 <td width="240" class="z-jie2"><% if("1".equals(required)){ %>必修<% }else{%>选修<% }%>┊<% if("0".equals(studyTime)){ %>无<% }else{ out.print(studyTime);%>分钟<%}%>┊<img src="resource/images/template/time.jpg" width="10" height="10" /> <%=myTime %>分钟</td>
        			</tr>  
			  </table>	  
</div>
			<div class="zhang-jie2">
					<table border="0" cellpadding="0" cellspacing="0" width="96%" >
		 			<tr>
               			 <td width="49"><img src="resource/images/template/index1-xian-left-B.jpg" /></td>
						 <td width="93%" class="c4"></td>
               			 <td width="49"><img src="resource/images/template/index1-xian-right-B.jpg" /></td>
        			</tr>  
			  </table>	  
			</div>
		<%
					// 若当前节点为"页"
				}else if(type.equals("3")){
		
		%>
		<div class="zhang-jie">
	       <table border="0" cellpadding="0" cellspacing="0" width="96%" >
		 			<tr>
               			 <td width="60%" class="z-jie1"><a href="javascript:window.location.href='studySpace_fowardCourseware.do?node=<%=id%>&studyrecordId=<%=studyRecordId%>'"><%=title %></a></td>
						 <td width="10%" ></td>
               			 <td width="240" class="z-jie2"><% if("1".equals(required)){ %>必修<% }else{%>选修<% }%>┊<% if("0".equals(studyTime)){ %>无<% }else{ out.print(studyTime);%>分钟<%}%>┊<img src="resource/images/template/time.jpg" width="10" height="10" /> <%=myTime %>分钟</td>
        			</tr>  
			  </table>	  
</div>
			<div class="zhang-jie2">
					<table border="0" cellpadding="0" cellspacing="0" width="96%" >
		 			<tr>
               			 <td width="49"><img src="resource/images/template/index1-xian-left-B.jpg" /></td>
						 <td width="93%" class="c4"></td>
               			 <td width="49"><img src="resource/images/template/index1-xian-right-B.jpg" /></td>
        			</tr>  
			  </table>	  
			</div>
			
		<ul>
		<%
					if(setionActivityMap != null) {
						List<Node> sectionActivity = setionActivityMap.get(id);
		%>
		<ul>
					<%
						for(int k = 0; k < sectionActivity.size(); k++) {
							Node setionNode = sectionActivity.get(k);
							String sectionActivityId = setionNode.getId();
							String sectionActivityTitle = setionNode.getTitle();
							String sectionActivityType = setionNode.getType();
							//当前节点为"作业"
							if(sectionActivityType.equals("4")) {
		%>
			<li style="margin-left:50px;margin-top:10px;"><a href="studyPaper_writeTestInit.do?testId=<%=sectionActivityId%>&studyrecordId=<%=studyRecordId%>"><img src="resource/icons/test.gif" /><%=sectionActivityTitle %></a></li>
		<%
							//当前节点为"论坛"
							}else if(sectionActivityType.equals("5")) {
		%>
			<li style="margin-left:50px;margin-top:10px;"><a href="studyPaper_forumInit.do?forumId=<%=sectionActivityId%>&studyrecordId=<%=studyRecordId%>"><img src="resource/icons/forum.gif" /><%=sectionActivityTitle %></a></li>
		<%
							//当前节点为"前测"
							}else if(sectionActivityType.equals("6")) {
								Long attemptNumber = node.getAttemptNumber();
		%>
			<li style="margin-left:50px;margin-top:10px;"><a href="javascript:selfTestInitDisplay(<%=sectionActivityId%>,<%=attemptNumber%>)"><img src="resource/icons/selftest.gif" /><%=sectionActivityTitle %></a></li>
		<%
							}
						}
					%>
		</ul>
		
		<%
					}
					//当前节点为"作业"
					}else if(type.equals("4")){
						testList.add(node);
					//当前节点为"论坛"
					}else if(type.equals("5")){
						forumList.add(node);
					//当前节点为"前测"
					}else if(type.equals("6")){
						selftTestList.add(node);
					}
				}
			}
		%>

		<div class="celss"></div>
		<div class="c-1">
			<!-- 论坛 -->
			<%
				if(forumList!=null && !forumList.isEmpty()){
			%>
			<div class="f-left">
				<div class="f1"><img src="resource/images/template/index1-c1-B.jpg" /></div>
				<div class="f2">同学们可以通过这里进行学习。</div>
				<div class="celss"></div>
				<div class="f3">
					<table border="0" cellpadding="0" cellspacing="0" width="96%" >
		 			<tr>
               			 <td width="49"><img src="resource/images/template/index1-xian-left-B.jpg" /></td>
						 <td width="93%" class="c4"></td>
               			 <td width="49"><img src="resource/images/template/index1-xian-right-B.jpg" /></td>
        			</tr>  
			  </table>	  
				</div>
				<div class="f4">
				<%
					for(int i=0;i<forumList.size();i++){
						Node node = forumList.get(i);
						String id = node.getId();
						String title = node.getTitle();
				%>
				<div class="fz"><img src="resource/images/template/index1-box3-B.jpg" />&nbsp;&nbsp;<a href="studyPaper_forumInit.do?forumId=<%=id%>&studyrecordId=<%=studyRecordId%>"><%= title %></a></div>
				<%
					}
				%>
			</div>
			</div>
			<%
				}
			%>
			
			<!-- 前测 -->
			<%
				if(selftTestList!=null && !selftTestList.isEmpty()){
			%>
			<div class="f-left">
				<div class="f1"><img src="resource/images/template/index1-c2-B.jpg" width="76" height="69" /></div>
				<div class="f2">同学们可以通过这里进行学习。</div>
				<div class="celss"></div>
				<div class="f3">
					<table border="0" cellpadding="0" cellspacing="0" width="96%" >
		 			<tr>
               			 <td width="49"><img src="resource/images/template/index1-xian-left-B.jpg" /></td>
						 <td width="93%" class="c4"></td>
               			 <td width="49"><img src="resource/images/template/index1-xian-right-B.jpg" /></td>
        			</tr>  
			  </table>	  
				</div>
				<div class="f4">
				<%
					for(int i=0;i<selftTestList.size();i++){
						Node node = selftTestList.get(i);
						String id = node.getId();
						String title = node.getTitle();
						Long attemptNumber = node.getAttemptNumber();
				%>
				<div class="fz"><img src="resource/images/template/index1-box3-B.jpg" />&nbsp;&nbsp;<a href="javascript:selfTestInitDisplay(<%=id%>,<%=attemptNumber%>)"><%= title %></a></div>
				<%
					}
				%>
			</div>
			</div>
			<%
				}
			%>
			
			<!-- 作业 -->
			<%
				if(testList!=null && !testList.isEmpty()){
			%>
			<div class="f-left">
				<div class="f1"><img src="resource/images/template/index1-c3-B.jpg" width="76" height="69" /></div>
				<div class="f2">同学们可以通过这里进行学习。</div>
				<div class="celss"></div>
				<div class="f3">
					<table border="0" cellpadding="0" cellspacing="0" width="96%" >
		 			<tr>
               			 <td width="49"><img src="resource/images/template/index1-xian-left-B.jpg" /></td>
						 <td width="93%" class="c4"></td>
               			 <td width="49"><img src="resource/images/template/index1-xian-right-B.jpg" /></td>
        			</tr>  
			  </table>	  
				</div>
				<div class="f4">
				<%
					for(int i=0;i<testList.size();i++){
						Node node = testList.get(i);
						String id = node.getId();
						String title = node.getTitle();
				%>
				<div class="fz"><img src="resource/images/template/index1-box3-B.jpg" />&nbsp;&nbsp;<a href="studyPaper_writeTestInit.do?testId=<%=id%>&studyrecordId=<%=studyRecordId%>"><%= title %></a></div>
				<%
					}
				%>
				</div>
			</div>
			<%
				}
			%>
			
		</div>
	</div>
</body>
</html>
