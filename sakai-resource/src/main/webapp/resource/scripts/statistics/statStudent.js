Ext.onReady(function(){

		// 定义列模型
		var studentCM = new Ext.grid.ColumnModel([{
					// 表头显示的文字
					header : "活动名称",
					// 数据来源
					dataIndex : "title",
					sortable : true
				}, {
					header : "参与人数",// 表头文字
					id : "partakeNumbers",
					dataIndex : "partakeNumbers",// 数据来源
					//width : .25,
					sortable : true
				}, {
					header : "通过人数",// 表头文字
					id : "passNumbers",
					dataIndex : "passNumbers",// 数据来源
					sortable : true
				}, {
					header : "未通过人数",// 表头文字
					id : "unpassNumbers",
					dataIndex : "unpassNumbers",// 数据来源
					sortable : true
				}, {
					header : "未参与人数",// 表头文字
					id : "unpartakeNumbers",
					dataIndex : "unpartakeNumbers",// 数据来源
					sortable : true
				}
		]);
		
		// 定义数据列的数据来源,DownTimes表示资源下载次数
		var studentStore = new Ext.data.JsonStore({
			id : "studentStore",
			url : "statistics_statStudent.do?isShow=true",
			root : "root",
			fields : ["title", "partakeNumbers","passNumbers","unpassNumbers","unpartakeNumbers"]
		});
		
		studentStore.load();
		// 定义列表，把数据源和定义的列模型加载进来以显示，DownTimes表示资源下载次数
		var panel = new Ext.grid.GridPanel({
					id : "studentGP",
					region : "center",// 设置显示内容的位置
					cm : studentCM,// 指定列模型
					renderTo : Ext.getBody(),
					autoHeight : true,
					closable : true,// 指定为真使其可以关闭
					stripeRows : true,// true是显示行的分隔符，默认会加上这个样式x-grid3-row-alt，自己也可以覆盖
					loadMask : true,// true表示在数据加载过程中会有一个遮盖效果
					loadingText : "加载中...",
					store : studentStore,// 指定数据来源
					autoExpandColumn : "testName",// grid会把你指定的这列自动扩充来填满空白
					columnLines : true,// true是显示列分隔符
					viewConfig : {
						forceFit : true
					}
		});
})