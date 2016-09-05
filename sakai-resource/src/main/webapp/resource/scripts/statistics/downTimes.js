//统计分类下载次数
Ext.onReady(function(){

		// 定义列模型
		var downTimesCM = new Ext.grid.ColumnModel([
				new Ext.grid.RowNumberer(),{
					// 表头显示的文字
					header : "资源名称",
					// 数据来源
					dataIndex : "fileName",
					//width : .25,
					sortable : true
				}, {
					header : "下载次数",// 表头文字
					id : "downloadCount",
					dataIndex : "downloadCount",// 数据来源
					//width : .25,
					sortable : true
				}
		]);
		
		// 定义数据列的数据来源,DownTimes表示资源下载次数
		var downTimesStore = new Ext.data.JsonStore({
			id : "downTimesStore",
			url : "statistics_downTimes.do?isShow=true",
			root : "root",
			fields : ["fileName", "downloadCount"]
		});
		
		//加载数据
		downTimesStore.load();
		
		// 定义列表，把数据源和定义的列模型加载进来以显示，DownTimes表示资源下载次数
		var gridPanel = new Ext.grid.GridPanel({
					id : "downTimesGP",
					region : "center",// 设置显示内容的位置
					cm : downTimesCM,// 指定列模型
					renderTo : Ext.getBody(),
					autoHeight : true,
					closable : true,// 指定为真使其可以关闭
					stripeRows : true,// true是显示行的分隔符，默认会加上这个样式x-grid3-row-alt，自己也可以覆盖
					loadMask : true,// true表示在数据加载过程中会有一个遮盖效果
					loadingText : "加载中...",
					store : downTimesStore,// 指定数据来源
					autoExpandColumn : "courseName",// grid会把你指定的这列自动扩充来填满空白
					columnLines : true,// true是显示列分隔符
					viewConfig : {
						forceFit : true
					},
					bbar : new Ext.PagingToolbar({
								id : "downTimesGridPage",
								pageSize : 20,
								store : downTimesStore,
								displayInfo : true
							})
		});
})