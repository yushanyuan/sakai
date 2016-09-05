IFrameComponent = Ext.extend(Ext.BoxComponent, {
	onRender : function(ct, position) {
		this.el = ct.createChild({
			tag : 'iframe',
			id : this.id,
			style : 'height:100px;width:100%',
			name : this.name,
			frameBorder : 0,
			src : this.url
		});
	}
});	
Ext.onReady(function() {
	// 开启提示功能
	Ext.QuickTips.init();

	var tab_items = [
		new IFrameComponent({
			border : false,
			autoScroll : false,
			region : 'center',
			id : 'ifrm_c_course',
			title : '课程组织',
			closable : false,
			name : "frame1",
			url : 'courseSpace_manageInit.do'
		})
	];
	if(useExamSys){
		tab_items.push(
			new IFrameComponent({
				border : false,
				autoScroll : false,
				region : 'center',
				id : 'ifrm_c_test',
				title : '批改作业',
				closable : false,
				name : "frame2",
				url : 'courseSpace_checkInit.do'
			})
		);
		tab_items.push(
			new IFrameComponent({
				border : false,
				autoScroll : false,
				region : 'center',
				id : 'ifrm_c_statistic',
				title : '统计分析',
				closable : false,
				name : "frame3",
				url : 'statistics.do'
			})
		);
		/*tab_items.push(
			new IFrameComponent({
				border : false,
				autoScroll : false,
				region : 'center',
				id : 'ifrm_c_score',
				title : '设置印象分',
				closable : false,
				name : "frame5",
				url : 'courseSpace_impressionScoreSetInit.do'
			})	
		);	*/			
	}	
	
	tab_items.push(
		new IFrameComponent({
			border : false,
			autoScroll : false,
			region : 'center',
			id : 'ifrm_c_score',
			title : '平时成绩',
			closable : false,
			name : "frame4",
			url : 'courseSpace_scoreInit.do'
		})
	);
	
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
		items: tab_items,
		listeners:{ 
		    tabchange:function(tp,p){ 
		        fitScreen(p.id);
		    } 
		} 		
	});
	
});