Ext.onReady(function(){

		// 定义列模型
		var activityAvgCM = new Ext.grid.ColumnModel([{
					// 表头显示的文字
					header : "活动名称",
					// 数据来源
					dataIndex : "title",
					//width : .25,
					sortable : true
				}, {
					header : "平均次数",// 表头文字
					id : "avgTimes",
					dataIndex : "avgTimes",// 数据来源
					//width : .25,
					sortable : true
				}, {
					header : "平均得分",// 表头文字
					id : "avgScore",
					dataIndex : "avgScore",// 数据来源
					//width : .25,
					sortable : true
				}
		]);
		
		// 定义数据列的数据来源,DownTimes表示资源下载次数
		var activityAvgStore = new Ext.data.JsonStore({
			id : "activityAvgStore",
			url : "statistics_statActivityAvgTimeAndScore.do?isShow=true",
			root : "root",
			fields : ["title", "avgTimes","avgScore"]
		});
		
		activityAvgStore.load();
		// 定义列表，把数据源和定义的列模型加载进来以显示，DownTimes表示资源下载次数
		var panel = new Ext.grid.GridPanel({
					id : "activityAvgGP",
					region : "center",// 设置显示内容的位置
					cm : activityAvgCM,// 指定列模型
					renderTo : Ext.getBody(),
					autoHeight : true,
					closable : true,// 指定为真使其可以关闭
					stripeRows : true,// true是显示行的分隔符，默认会加上这个样式x-grid3-row-alt，自己也可以覆盖
					loadMask : true,// true表示在数据加载过程中会有一个遮盖效果
					loadingText : "加载中...",
					store : activityAvgStore,// 指定数据来源
					autoExpandColumn : "testName",// grid会把你指定的这列自动扩充来填满空白
					columnLines : true,// true是显示列分隔符
					viewConfig : {
						forceFit : true
					},
					bbar : new Ext.PagingToolbar({
								id : "activityAvgGridPage",
								pageSize : 20,
								store : activityAvgStore,
								displayInfo : true
							})
		});
})