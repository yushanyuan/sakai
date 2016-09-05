Ext.onReady(function(){

		// 定义列模型
		var modelAvgCM = new Ext.grid.ColumnModel([
			new Ext.grid.RowNumberer(),{
					// 表头显示的文字
					header : "标题",
					// 数据来源
					dataIndex : "title",
					sortable : true
				}, {
					header : "模块平均通过时间",// 表头文字
					id : "avgPassTime",
					dataIndex : "avgPassTime",// 数据来源
					//width : .25,
					sortable : true
				}
		]);
		
		// 定义数据列的数据来源,DownTimes表示资源下载次数
		var modelAvgStore = new Ext.data.JsonStore({
			id : "modelAvgStore",
			url : "statistics_statModelAvgThroughTime.do?isShow=true",
			root : "root",
			fields : ["title", "avgPassTime"]
		});
		
		//加载数据
		modelAvgStore.load();
		
		// 定义列表，把数据源和定义的列模型加载进来以显示，DownTimes表示资源下载次数
		var modelGridPanel = new Ext.grid.GridPanel({
					id : "modelAvgGP",
					region : "center",// 设置显示内容的位置
					cm : modelAvgCM,// 指定列模型
					renderTo : Ext.getBody(),
					autoHeight : true,
					closable : true,// 指定为真使其可以关闭
					stripeRows : true,// true是显示行的分隔符，默认会加上这个样式x-grid3-row-alt，自己也可以覆盖
					loadMask : true,// true表示在数据加载过程中会有一个遮盖效果
					loadingText : "加载中...",
					store : modelAvgStore,// 指定数据来源
					autoExpandColumn : "title",// grid会把你指定的这列自动扩充来填满空白
					columnLines : true,// true是显示列分隔符
					layout : "fit",// 指定布局模式
					viewConfig : {
						forceFit : true
					},
					bbar : new Ext.PagingToolbar({
								id : "modelAvgGridPage",
								pageSize : 20,
								store : modelAvgStore,
								displayInfo : true
							})
		});
})