<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="org.sakaiproject.resource.api.course.model.*" %>
<%@ page import="org.sakaiproject.resource.api.study.model.*" %>
<%@ page import="org.sakaiproject.resource.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.math.*" %>
<%
	String path = request.getContextPath();
	MeleteStudyRecordModel studyRecord = (MeleteStudyRecordModel)request.getAttribute("studyRecord");
	MeleteCourseModel course = (MeleteCourseModel)request.getAttribute("course");
	List<MeleteModuleModel> moduleList = (List<MeleteModuleModel>)request.getAttribute("moduleList");
	List<MeleteModuleRecordModel> moduleRecordList = (List<MeleteModuleRecordModel>)request.getAttribute("moduleRecordList");
	List<MeleteSectionModel> sectionList = (List<MeleteSectionModel>)request.getAttribute("sectionList");
	List<MeleteSectionRecordModel> sectionRecordList = (List<MeleteSectionRecordModel>)request.getAttribute("sectionRecordList");
	List<MeleteSelfTestModel> selfList = (List<MeleteSelfTestModel>)request.getAttribute("selfList");
	List<MeleteSelftestRecordModel> selfRecordList = (List<MeleteSelftestRecordModel>)request.getAttribute("selfRecordList");
	List<MeleteTestModel> testList = (List<MeleteTestModel>)request.getAttribute("testList");
	List<MeleteTestRecordModel> testRecordList = (List<MeleteTestRecordModel>)request.getAttribute("testRecordList");
	List<MeleteForumModel> forumList = (List<MeleteForumModel>)request.getAttribute("forumList");
	List<MeleteForumRecordModel> forumRecordList = (List<MeleteForumRecordModel>)request.getAttribute("forumRecordList");
	
	String lessonStatus = studyRecord.getLessonStatus().toString().equals(CodeTable.passStatusYes)?"pass-gre":"pass-red";
	Float score = studyRecord.getScore();
	Long studyRecordId = studyRecord.getStudyrecordId();
	String courseId = course.getId();
	
	int mountPoint = 0;
	if(moduleList!=null && !moduleList.isEmpty()){
		mountPoint += moduleList.size();
	}
	if(sectionList!=null && !sectionList.isEmpty()){
		mountPoint += sectionList.size();
	}
	if(selfList!=null && !selfList.isEmpty()){
		mountPoint += selfList.size();
	}
	if(testList!=null && !testList.isEmpty()){
		mountPoint += testList.size();
	}
	if(forumList!=null && !forumList.isEmpty()){
		mountPoint += forumList.size();
	}
	int maxPoint = 24;
	int sacl = (maxPoint-1)/(mountPoint-1);
	int gridWidth = 200;
	int maxWidth = 0;
	int num = 0;
%>
<html>
	<head>
	<title>山坡模板</title>
	<link rel="stylesheet" href="resource/styles/hill.css" type="text/css">	
	<script type="text/javascript" language="JavaScript" src="/library/js/headscripts.js"></script>
	<script language="javascript">
	var perDiv = <%=(maxPoint - (mountPoint-1) * sacl)%>;
	function MM_showHideLayers(divId) { //v6.0
	 	var perObj = document.getElementById("Layer"+perDiv);
	 	if(perObj!=undefined)
	 		perObj.style.visibility = "hidden";

		perDiv = divId;
		var obj = document.getElementById("Layer"+divId);
		if(obj!=undefined)
			obj.style.visibility = "visible";
		return;
	}
    function clickLink(linkId) {
        var fireOnThis = document.getElementById(linkId);

        if (document.createEvent) {
            var evObj = document.createEvent('MouseEvents');
            evObj.initEvent('click', true, false);
            fireOnThis.dispatchEvent(evObj);
        }
        else if (document.createEventObject) {
            fireOnThis.fireEvent('onclick');
        }
    }
    function pageHeightInit(){
		<%= request.getAttribute("sakai.html.body.onload") %>
	}
	</script>
	</head>
	<body onload="pageHeightInit()">
		<div id="wrap">
			<div id="content">
				<div id="sy"><!--整体背景-->
					<div id="sl"><!--图标说明框-->
						<div>
							<span id="pass">通过</span>
							<span id="nopas">未通过</span>
							<span id="open">开启</span>
							<span id="close">未开启</span>
						</div>
					</div>
					<!--节点-->
					<%
					if(moduleList!=null && !moduleList.isEmpty()){
						for(int i=0;i<moduleList.size();i++){
							int idx = maxPoint - (mountPoint-i-1) * sacl;
							MeleteModuleModel module = moduleList.get(i);
							Long moduleId = module.getId();
							MeleteModuleRecordModel moduleRecord = moduleRecordList.get(i);
							Long moduleRecordId = moduleRecord.getModulerecordId();
							String title = module.getTitle();
							String studyTime = module.getStudyDay().toString();
							if(maxWidth < Integer.parseInt(studyTime)){
								maxWidth = Integer.parseInt(studyTime);
							}
							String avgTime = module.getAvgPassTime() == null ? "0" : module.getAvgPassTime().toString();
							if(maxWidth < Integer.parseInt(avgTime)){
								maxWidth = Integer.parseInt(avgTime);
							}
							String myTime = moduleRecord.getStudyTime().toString();
							if(maxWidth < Integer.parseInt(myTime)){
								maxWidth = Integer.parseInt(myTime);
							}
						%>
						<a id="zhuti<%= idx %>" onmouseover="MM_showHideLayers('<%= idx %>')"
							href="javascript:window.location.href='studySpace_fowardHillModule.do?moduleId=<%=moduleId%>&noderecordId=<%=moduleRecordId%>&courseId=<%=courseId%>&studyrecordId=<%=studyRecordId%>'"
							onfocus="this.blur()"><%= title %></a>
						<%
						}
						num += moduleList.size();
					}
					if(sectionList!=null && !sectionList.isEmpty()){
						for(int i=0;i<sectionList.size();i++){
							int idx = maxPoint - (mountPoint-i-1) * sacl;
							MeleteSectionModel section = sectionList.get(i);
							Long sectionId = section.getId();
							String title = section.getTitle();
							String sectionPath = section.getPath();
							Long moduleId = section.getModuleId();
							Long status = section.getStatus();
							MeleteSectionRecordModel sectionRecord = sectionRecordList.get(i);
							Long sectionRecordId = sectionRecord.getSectionrecordId();
							String studyTime = section.getStudyTime().toString();
							if(maxWidth < Integer.parseInt(studyTime)){
								maxWidth = Integer.parseInt(studyTime);
							}
							String avgTime = section.getAvgPassTime() == null ? "0" : section.getAvgPassTime().toString();
							if(maxWidth < Integer.parseInt(avgTime)){
								maxWidth = Integer.parseInt(avgTime);
							}
							String myTime = sectionRecord.getStudyTime().toString();
							if(maxWidth < Integer.parseInt(myTime)){
								maxWidth = Integer.parseInt(myTime);
							}
						%>
						<a id="zhuti<%= idx %>" onmouseover="MM_showHideLayers('<%= idx %>')"
							href="javascript:window.location.href='studySpace_fowardCourseware.do?node=<%=sectionId%>&courseId=<%=courseId%>
							&path=<%=sectionPath%>&moduleId=<%=moduleId%>&sectionRecordId=<%=sectionRecordId%>&status=<%=status%>
							&selfStudyTime=<%=myTime%>&studyTime=<%=studyTime%>&studyrecordId=<%=studyRecordId%>'"
							onfocus="this.blur()"><%= title %></a>
						<%
						}
						num += sectionList.size();
					}
					if(testList!=null && !testList.isEmpty()){
						for(int i=0;i<testList.size();i++){
							int idx = maxPoint - (mountPoint-num-i-1) * sacl;
							MeleteTestModel test = testList.get(i);
							Long masteryScore = test.getMasteryScore();
							Long testId = test.getId();
							String samepaper = test.getSamepaper();
							MeleteTestRecordModel testRecord = testRecordList.get(i);
							Long testRecordId = testRecord.getTestrecordId();
							String title = test.getName();
						%>
						<a id="zhuti<%=idx%>" onmouseover="MM_showHideLayers('<%= idx %>')"
							href="javascript:window.showModalDialog('studyPaper_writeTestInit.do?courseId=<%=courseId%>
							&testId=<%=testId%>&testrecordId=<%=testRecordId%>&passScore=<%=masteryScore%>
							&samepaper=<%=samepaper%>&studyrecordId=<%=studyRecordId%>', window,
							'dialogWidth:document.body.clientWidth;dialogHeight:document.body.clientHeight')"
							onfocus="this.blur()"><%= title %></a>
						<%
						}
						num += testList.size();
					}
					if(forumList!=null && !forumList.isEmpty()){
						for(int i=0;i<forumList.size();i++){
							int idx = maxPoint - (mountPoint-num-i-1) * sacl;
							MeleteForumModel forum = forumList.get(i);
							String topicId = forum.getTopicId();
							Long forumId = forum.getId();
							MeleteForumRecordModel forumRecord = forumRecordList.get(i);
							Long forumRecordId = forumRecord.getForumrecordId();
							String title = forum.getName();
						%>
						<a id="zhuti<%=idx%>" onmouseover="MM_showHideLayers('<%= idx %>')"
							href="javascript:window.showModalDialog('studyPaper_forumInit.do?courseId=<%=courseId%>
							&topicId=<%=topicId%>&forumId=<%=forumId%>&forumrecordId=<%=forumRecordId%>&studyrecordId=<%=studyRecordId%>', window, 
							'dialogWidth:document.body.clientWidth;dialogHeight:document.body.clientHeight')"
							onfocus="this.blur()"><%= title %></a>
						<%
						}
						num += forumList.size();
					}
					if(selfList!=null && !selfList.isEmpty()){
						for(int i=0;i<selfList.size();i++){
							int idx = maxPoint - (mountPoint-num-i-1) * sacl;
							MeleteSelfTestModel self = selfList.get(i);
							Long selfId = self.getId();
							Long schemaId = self.getSchemaId();
							Long masteryScore = self.getMasteryScore();
							String samepaper = self.getSamepaper();
							MeleteSelftestRecordModel selfRecord = selfRecordList.get(i);
							Long selfRecordId = selfRecord.getSelftestrecordId();
							String title = self.getName();
						%>
						<a id="zhuti<%=idx%>" onmouseover="MM_showHideLayers('<%= idx %>')"
							href="javascript:window.showModalDialog('studyPaper_writeSelfTestInit.do?courseId=<%=courseId%>
							&schemaId=<%=schemaId%>&testId=<%=selfId%>&samepaper=<%=samepaper%>&testrecordId=<%=selfRecordId%>
							&passScore=<%=masteryScore%>&studyrecordId=<%=studyRecordId%>', window,
							'dialogWidth:document.body.clientWidth;dialogHeight:document.body.clientHeight')"
							onfocus="this.blur()"><%= title %></a>
						<%
						}
					}
					%>
					<!--节点说明-->
					<%
					BigDecimal cellWidth = maxWidth==0?new BigDecimal(0):(new BigDecimal(gridWidth).divide(new BigDecimal(maxWidth),1, BigDecimal.ROUND_FLOOR));
					if(moduleList!=null && !moduleList.isEmpty()){
						for(int i=0;i<moduleList.size();i++){
							int idx = maxPoint - (mountPoint-i-1) * sacl;
							MeleteModuleModel module = moduleList.get(i);
							MeleteModuleRecordModel moduleRecord = moduleRecordList.get(i);
							String title = module.getTitle();
							String required = module.getRequired().toString().equals("1")?"必修":"选修";
							String openStatus = moduleRecord.getOpenStatus().toString().equals(CodeTable.openStatusYes)?"open":"close";
							String passStr = moduleRecord.getStatus().toString().equals(CodeTable.passStatusYes)?"pass-gre":"pass-red";
							String studyTime = module.getStudyDay().toString();
							int studyWidth = new BigDecimal(studyTime).multiply(cellWidth).intValue();
							String avgTime = module.getAvgPassTime() == null ? "0" : module.getAvgPassTime().toString();
							int avgWidth = new BigDecimal(avgTime).multiply(cellWidth).intValue();
							String myTime = moduleRecord.getStudyTime().toString();
							int myWidth = new BigDecimal(myTime).multiply(cellWidth).intValue();
						%>
						<div style="visibility:<%=(i==0?"visible":"hidden")%> ;" id="Layer<%= idx %>" class="layer">
							<div id="zhuti">
								<table border="0" cellpadding="0" cellspacing="0" width="100%">
									<tbody>
										<tr>
											<td style="font-weight: bold; font-size: 13px;">
												<%= title %>
											</td>
										</tr>
										<tr>
											<td>
												属性:<%=required%>
												&nbsp;&nbsp; 
												开启状态:<img src="resource/images/hill/<%=openStatus%>.gif" height="11" width="14">
												&nbsp;&nbsp;&nbsp; 
												通过状态: <img src="resource/images/hill/<%=passStr%>.gif" height="13" width="10">
											</td>
										</tr>
										<tr>
											<td>
												<table border="0" cellpadding="0" cellspacing="0" width="100%">
													<tbody>
														<tr>
															<td colspan="6" rowspan="3"><!-- 模拟柱状图部分 -->
																<div id="xxztbg0">
																	<table width="100%">
																		<tbody>
																			<tr>
																				<td>
																					<span id="jd1" style="width: <%=studyWidth%>px;"></span>
																					<span><%=studyTime%>分钟</span>
																				</td>
																			</tr>
																			<tr>
																				<td>
																					<span id="jd2" style="width: <%=avgWidth%>px;"></span>
																					<span><%=avgTime%>分钟</span>
																				</td>
																			</tr>
																			<tr>
																				<td>
																					<span id="jd3" style="width: <%=myWidth%>px;"></span>
																					<span><%=myTime%>分钟</span>
																				</td>
																			</tr>
																		</tbody>
																	</table>
																</div>
															</td>
															<td>
																建议时长
															</td>
														</tr>
														<tr>
															<td>
																平均时长
															</td>
														</tr>
														<tr>
															<td>
																我的时长
															</td>
														</tr>
													</tbody>
												</table>
											</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
						<%
						}
					}
					if(sectionList!=null && !sectionList.isEmpty()){
						for(int i=0;i<sectionList.size();i++){
							int idx = maxPoint - (mountPoint-i-1) * sacl;
							MeleteSectionModel section = sectionList.get(i);
							MeleteSectionRecordModel sectionRecord = sectionRecordList.get(i);
							String title = section.getTitle();
							String required = section.getRequired().toString().equals("1")?"必修":"选修";
							String openStatus = sectionRecord.getOpenStatus().toString().equals(CodeTable.openStatusYes)?"open":"close";
							String passStr = sectionRecord.getStatus().toString().equals(CodeTable.passStatusYes)?"pass-gre":"pass-red";
							String studyTime = section.getStudyTime().toString();
							int studyWidth = new BigDecimal(studyTime).multiply(cellWidth).intValue();
							String avgTime = section.getAvgPassTime() == null ? "0" : section.getAvgPassTime().toString();
							int avgWidth = new BigDecimal(avgTime).multiply(cellWidth).intValue();
							String myTime = sectionRecord.getStudyTime().toString();
							int myWidth = new BigDecimal(myTime).multiply(cellWidth).intValue();
						%>
						<div style="visibility:<%=(i==0?"visible":"hidden")%> ;" id="Layer<%= idx %>" class="layer">
							<div id="zhuti">
								<table border="0" cellpadding="0" cellspacing="0" width="100%">
									<tbody>
										<tr>
											<td style="font-weight: bold; font-size: 13px;">
												<%= title %>
											</td>
										</tr>
										<tr>
											<td>
												属性:<%=required%>
												&nbsp;&nbsp; 
												开启状态:<img src="resource/images/hill/<%=openStatus%>.gif" height="11" width="14">
												&nbsp;&nbsp;&nbsp; 
												通过状态: <img src="resource/images/hill/<%=passStr%>.gif" height="13" width="10">
											</td>
										</tr>
										<tr>
											<td>
												<table border="0" cellpadding="0" cellspacing="0" width="100%">
													<tbody>
														<tr>
															<td colspan="6" rowspan="3"><!-- 模拟柱状图部分 -->
																<div id="xxztbg0">
																	<table width="100%">
																		<tbody>
																			<tr>
																				<td>
																					<span id="jd1" style="width: <%=studyWidth%>px;"></span>
																					<span><%=studyTime%>分钟</span>
																				</td>
																			</tr>
																			<tr>
																				<td>
																					<span id="jd2" style="width: <%=avgWidth%>px;"></span>
																					<span><%=avgTime%>分钟</span>
																				</td>
																			</tr>
																			<tr>
																				<td>
																					<span id="jd3" style="width: <%=myWidth%>px;"></span>
																					<span><%=myTime%>分钟</span>
																				</td>
																			</tr>
																		</tbody>
																	</table>
																</div>
															</td>
															<td>
																建议时长
															</td>
														</tr>
														<tr>
															<td>
																平均时长
															</td>
														</tr>
														<tr>
															<td>
																我的时长
															</td>
														</tr>
													</tbody>
												</table>
											</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
						<%
						}
					}
					%>
					<!--课程状态-->
					<div id="zt" style="position: absolute; top: 370px; right: 10%;">
						课程通过状态：
						<img src="resource/images/hill/<%=lessonStatus%>.gif" style="margin-right: 5px;">
							课程平时成绩： 
						<span style="margin-right: 5px; margin-left: 3px;"><%=score%></span>
						<a
							href="javascript:clickLink('listmodulesStudentform:studyRecordList')"
							onmouseover="document.forms['listmodulesStudentform'].target='_blank'"
							onmouseout="document.forms['listmodulesStudentform'].target=''"><img
								src="resource/images/hill/sy-tm_15.gif" style="margin-right: 3px;">
								学习记录统计
						</a>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>