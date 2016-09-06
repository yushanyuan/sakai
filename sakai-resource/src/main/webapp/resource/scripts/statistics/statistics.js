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
	
	var item1 = new Ext.Panel({
        title: '统计分类一',
        html: '<a href="#" onclick="javascript:toTab(\'1\',\'统计资源下载次数\',\'statistics_downTimes.do\')">统计资源下载次数</a><p>'+
        		'<a href="#" onclick="javascript:toTab(\'2\',\'查询活动成绩\',\'statistics_findActivityScore.do\')">查询活动成绩</a><p>',
        cls:'empty'
    });
	var item2 = new Ext.Panel({
        title: '统计分类二',
        html: '<a href="#" onclick="javascript:toTab(\'3\',\'统计页平均通过时间\',\'statistics_statModelAvgThroughTime.do\')">统计页平均通过时间</a><p>'+
        		'<a href="#" onclick="javascript:toTab(\'4\',\'统计活动平均次数和成绩\',\'statistics_statActivityAvgTimeAndScore.do\')">统计活动平均次数和成绩</a><p>',
        cls:'empty'
    });
	var item3 = new Ext.Panel({
        title: '统计分类三',
        html: '<a href="#" onclick="javascript:toTab(\'5\',\'学生参与情况统计\',\'statistics_statStudent.do\')">学生参与情况统计</a><p>'+
        	'<a href="#" onclick="javascript:toTab(\'6\',\'扩展统计\',\'http://www.sina.com.cn\')">扩展统计</a><p>',
        cls:'empty'
    });
	/**
	 * 主布局“左”面板
	 */
	var main_left = new Ext.Panel({
		region : 'west',
		collapseMode : 'mini',
		lines : false,
		id : 'west-panel',
		split : true,
		width : 200,
		minSize : 150,
		maxSize : 200,
		margins : '0 0 0 0',
		layout : 'accordion',
		layoutConfig : {
			animate : true
		},
		items: [item1, item2, item3]
	});
	
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
				id : '2',
				title : '查询活动成绩',
				closable : false,
				name : "frame2",
				url : 'statistics_findActivityScore.do'
			}),
			
			new IFrameComponent({
				border : false,
				region : 'center',
				id : '4',
				title : '统计活动平均次数和成绩',
				closable : false,
				name : "frame4",
				url : 'statistics_statActivityAvgTimeAndScore.do'
			})
			
		]
	});
	
	/**
	 * 主页面布局 上 左（上、中） 中（上、中）
	 */
	var viewport = new Ext.Panel({
		id : 'viewport',
		layout : 'border',
		height : 465,
		renderTo: Ext.getBody(),
		items:[main_center]
	});
});