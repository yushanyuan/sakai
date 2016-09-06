<%@ page language="java" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="<%=path%>/resource/styles/ext-all.css" />
<script type="text/javascript" src="<%=path%>/resource/scripts/extjs/ext-base.js"></script>
<script type="text/javascript" src="<%=path%>/resource/scripts/extjs/ext-all.js"></script>
<script type="text/javascript" src="/library/js/headscripts.js"></script>
</head>
<body>
</body>
<script>
function pageHeightInit(){
	<%= request.getAttribute("sakai.html.body.onload") %>
}
IFrameComponent = Ext.extend(Ext.BoxComponent, {
	onRender : function(ct, position) {
		this.el = ct.createChild({
			tag : 'iframe',
			id : this.id,
			style : 'height:100%;width:100%',
			name : this.name,
			frameBorder : 0,
			src : this.url
		});
	}
});	
Ext.onReady(function() {
	Ext.QuickTips.init();
	var tabs = new Ext.TabPanel({
		id : 'centerTab',
		renderTo : Ext.getBody(),
		autoScroll : false,
		tabPosition : 'top',
		layoutConfig : {
			animate : true
		},
		activeTab : 0,
		enableTabScroll : false,
		items:[
			new IFrameComponent({
				border : false,
				autoScroll : false,
				region : 'center',
				id : '1',
				title : '作业管理',
				closable : false,
				name : "frame1",
				url : 'test_manageInit.do'
			})
		]
	});
})
</script>
</html>