var tabArray = [];
var main_center;
	
IFrameComponent = Ext.extend(Ext.BoxComponent, {
	onRender : function(ct, position) {
		this.el = ct.createChild({
			tag : 'iframe',
			id : this.id,
			style : 'height:200px;width:100%',
			name : this.name,
			frameBorder : 0,
			src : this.url
		});
	}
});	

/**
 * 定义打开tab页的方法
 * @param idx 标签页的序号
 * @param title 标签的名称
 * @param url 菜单对应的action
 */
function toTab(idx,title,url) { 
	var n = main_center.getComponent(idx);
	if (!n) { // 判断是否已经打开该面板
		if(url.indexOf("http") == -1){
		}
		var centerFrame = new IFrameComponent({
			border : false,
			region : 'center',
			id : idx,
			title : title,
			closable : true,
			name : idx + "frame",
			url : url
		});
	
		tabArray.push(idx);
		n = main_center.add(centerFrame);
	}
	
	main_center.setActiveTab(n);
	Ext.getCmp('viewport').doLayout();
	
}
Ext.onReady(function() {
	Ext.QuickTips.init();
	
	/**
	 * 主布局“中”面板 工作区的tab标签页
	 */
	main_center = new Ext.TabPanel({
		region : 'center',
		id : 'centerTab',
		autoScroll : true,
		tabPosition : 'top',
		layoutConfig : {
			animate : true
		},
		activeTab : 0,
		enableTabScroll : true,
		//如果想要由左边的超练级来控制显示面板，怎么下面的items不要写
		items:[
			new IFrameComponent({
				border : false,
				region : 'center',
				id : 'study_time',
				title : '学习时长',
				closable : false,
				name : "study_time",
				url : 'studyManage_toStudyTime.do'
			}),
			
			new IFrameComponent({
				border : false,
				region : 'center',
				id : 'test_score',
				title : '阶段成绩',
				closable : false,
				name : "test_score",
				url : 'studyManage_toTestScore.do'
			}),
			
			new IFrameComponent({
				border : false,
				region : 'center',
				id : 'total_score',
				title : '总成绩',
				closable : false,
				name : "total_score",
				url : 'studyManage_toScore.do'
			})
			
		]
	});
	
	/**
	 * 主页面布局 上 左（上、中） 中（上、中）
	 */
	var viewport = new Ext.Panel({
		id : 'viewport',
		layout : 'border',
		height : 700,
		renderTo: Ext.getBody(),
		items:[main_center]
	});
});