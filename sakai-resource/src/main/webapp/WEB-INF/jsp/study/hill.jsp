<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="org.sakaiproject.resource.api.course.vo.Node" %>
<%@ page import="org.sakaiproject.resource.api.study.model.MeleteStudyRecordModel" %>
<%@ page import="org.sakaiproject.resource.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.math.*" %>
<%
	MeleteStudyRecordModel studyRecord = (MeleteStudyRecordModel)request.getAttribute("studyRecord");
	List<Node> nodeList = (List<Node>)request.getAttribute("nodeList");
	
	String lessonStatus = studyRecord.getLessonStatus().toString().equals(CodeTable.passStatusYes)?"pass-gre":"pass-red";
	Float score = studyRecord.getScore();
	Long studyRecordId = studyRecord.getStudyrecordId();
	
	int mountPoint = nodeList.size();//要显示的节点总个数
	int maxPoint = 24;//最多可以显示的节点个数
	int sacl = 0;
	if(mountPoint > 1){
		sacl = (maxPoint-1)/(mountPoint-1);
	}
	int gridWidth = 200;
	int maxWidth = 0;
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
					<!--节点-->
					<%
						for(int i=0;i<nodeList.size();i++){
							//生成唯一的值，区分每个连接的ID
							int idx = maxPoint - (mountPoint-i-1) * sacl;
							Node node = nodeList.get(i);
							String id = node.getId();
							String title = node.getTitle();
							String type = node.getType();
							if(type.equals("2")){
								//计算maxWith用于模拟柱状图部分显示建议，平均，我的时长
								String studyTime = node.getStudyTime();
								if(maxWidth < Integer.parseInt(studyTime)){
									maxWidth = Integer.parseInt(studyTime);
								}
								String avgTime = node.getAvgTime();
								if(maxWidth < Integer.parseInt(avgTime)){
									maxWidth = Integer.parseInt(avgTime);
								}
								String myTime = node.getMyTime();
								if(maxWidth < Integer.parseInt(myTime)){
									maxWidth = Integer.parseInt(myTime);
								}
								%>
								<a id="zhuti<%=idx%>" onmouseover="MM_showHideLayers('<%= idx %>')"
									href="javascript:window.location.href='studySpace_fowardHillModule.do?moduleId=<%=id%>&studyrecordId=<%=studyRecordId%>'"
									onfocus="this.blur()"><%= title %></a>
								<%
							}else if(type.equals("3")){
								//计算maxWith用于模拟柱状图部分显示建议，平均，我的时长
								String studyTime = node.getStudyTime();
								if(maxWidth < Integer.parseInt(studyTime)){
									maxWidth = Integer.parseInt(studyTime);
								}
								String avgTime = node.getAvgTime();
								if(maxWidth < Integer.parseInt(avgTime)){
									maxWidth = Integer.parseInt(avgTime);
								}
								String myTime = node.getMyTime();
								if(maxWidth < Integer.parseInt(myTime)){
									maxWidth = Integer.parseInt(myTime);
								}
								%>
								<a id="zhuti<%= idx %>" onmouseover="MM_showHideLayers('<%= idx %>')"
									href="javascript:window.location.href='studySpace_fowardCourseware.do?node=<%=id%>&studyrecordId=<%=studyRecordId%>'"
									onfocus="this.blur()"><%= title %></a>
								<%
							}else if(type.equals("4")){
								%>
								<a id="zhuti<%=idx%>" onmouseover="MM_showHideLayers('<%= idx %>')"
									href="javascript:window.showModalDialog('studyPaper_writeTestInit.do?testId=<%=id%>&studyrecordId=<%=studyRecordId%>', 
										window, 'dialogWidth:document.body.clientWidth;dialogHeight:document.body.clientHeight')"
									onfocus="this.blur()"><%= title %></a>
								<%
							}else if(type.equals("5")){
								%>
								<a id="zhuti<%=idx%>" onmouseover="MM_showHideLayers('<%= idx %>')"
									href="javascript:window.showModalDialog('studyPaper_forumInit.do?forumId=<%=id%>&studyrecordId=<%=studyRecordId%>', 
										window, 'dialogWidth:document.body.clientWidth;dialogHeight:document.body.clientHeight')"
									onfocus="this.blur()"><%= title %></a>
								<%
							}else if(type.equals("6")){
								%>
								<a id="zhuti<%=idx%>" onmouseover="MM_showHideLayers('<%= idx %>')"
									href="javascript:window.showModalDialog('studyPaper_writeSelfTestInit.do?testId=<%=id%>&studyrecordId=<%=studyRecordId%>', 
										window, 'dialogWidth:document.body.clientWidth;dialogHeight:document.body.clientHeight')"
									onfocus="this.blur()"><%= title %></a>
								<%
							}
						}

					%>
					<!--节点说明-->
					<%
					BigDecimal cellWidth = maxWidth==0?new BigDecimal(0):(new BigDecimal(gridWidth).divide(new BigDecimal(maxWidth),1, BigDecimal.ROUND_FLOOR));
					for(int i=0;i<nodeList.size();i++){
						int idx = maxPoint - (mountPoint-i-1) * sacl;
						Node node = nodeList.get(i);
						String type = node.getType();
						if(type.equals(CodeTable.module) || type.equals(CodeTable.section)){
							String title = node.getTitle();
							//以下注销的三个变量不需要在显示了
							//String required = node.getRequired().equals("1")?"必修":"选修";
							//String openStatus = node.getOpenStatus().equals(CodeTable.openStatusYes)?"open":"close";
							//String passStr = node.getPassStatus().equals(CodeTable.passStatusYes)?"pass-gre":"pass-red";
							String studyTime = node.getStudyTime();
							int studyWidth = new BigDecimal(studyTime).multiply(cellWidth).intValue();//建议时长
							String avgTime = node.getAvgTime();
							int avgWidth = new BigDecimal(avgTime).multiply(cellWidth).intValue();//平均时长
							String myTime = node.getMyTime();
							int myWidth = new BigDecimal(myTime).multiply(cellWidth).intValue();//我的时长
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
							课程平时成绩： 
						<span style="margin-right: 5px; margin-left: 3px;"><%=score%></span>
						<a href='statistics.do?studyrecordId=<%=studyRecordId%>' target='_blank' >
							<img src="resource/images/hill/sy-tm_15.gif" style="margin-right: 3px;">
							学习记录统计
						</a>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>