function trash(){
		//初始化所有标签的提示
		Ext.QuickTips.init();
		//定义错误信息的显示位置
		Ext.form.Field.prototype.msgTarget="side";
		
		//定义工具栏，右键也可以这样定义
		var resourceTrashMenu = [{
			//页面上显示的文本
			text : "还原课件",
			//图标路径
			iconCls : "arrow_undo",
			//前台事件对应的后台操作
			handler : function(){
				recourceBack();
			}
		},{
			//页面上显示的文本
			text : "还原所有课件",
			//图标路径
			iconCls : "arrow_in",
			//前台事件对应的后台操作
			handler : function(){
				recourceBackAll();
			}
		},{
			//页面上显示的文本
			text : "彻底删除课件",
			//图标路径
			iconCls : "np-reject",
			//前台事件对应的后台操作
			handler : function(){
				recourceTrashDelete();
			}
		},{
			//页面上显示的文本
			text : "清空回收站",
			//图标路径
			iconCls : "bin_empty",
			//前台事件对应的后台操作
			handler : function(){
				recourceClear();
			}
		},{
			//文本框
			text : "课件名称:",
			xtype : "tbtext",
			hideOnClick : true
		},{
			//文本框
			xtype : "textfield",
			id : "trashrsearch",
			nanme : "text"
		},{
			//页面上显示的文本
			text : "查询",
			//图标路径
			iconCls : "folder_find",
			//前台事件对应的后台操作
			handler : function(){
				recourceTrashFind();
			}
		}];
		
		//把定义的工具栏加载到菜单里
		var resourceTrashToolbar = new Ext.Toolbar(resourceTrashMenu);
		
		//定义课件列表的选择框的选择模型--单选还是多选，现在设置的是单选
		var checkBoxSM = new Ext.grid.CheckboxSelectionModel({
			singleSelect : false
		});
		
		// 表示资源的状态
		var sizeRender = function(value) {
			var sale = "M";
			if (value < 0.1) {
				value = value * 1024;
				sale = "K";
				if (value < 0.1) {
					value = value * 1024;
					sale = "B";
				}
			}
			return value+sale;
		};
		
		//显示格式化的时间格式
		var timeRender = function(value){
			var year = Ext.util.Format.substr(value,0,4);
			var mouth = Ext.util.Format.substr(value,5,2);
			var day = Ext.util.Format.substr(value,8,2);
			var last = Ext.util.Format.substr(value,11,8);
			var time = year+"年"+mouth+"月"+day+"日 "+last; 
			return time;
		};
		
		//定义列模型
		var resourceListCM = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(),
			checkBoxSM,{
				//表头显示的文字
				header : "标识",
				//数据来源
				dataIndex : "id",
				//是否隐藏
				hidden : true,
				//是否可以编辑是否隐藏
				hideable : false
			},{
				//表头显示的文字
				header : "课件名称",
				//数据来源
				dataIndex : "courseName",
				width : .25,
				align : "center",
				sortable : true
			},{
				header : "课件摘要",//表头文字
				dataIndex : "summary",//数据来源
				width : .25,//宽度，前面加点表示百分比
				align : "center",
				sortable : true
			},{
				header : "大小",//表头文字
				dataIndex : "fileSize",//数据来源
				width : .1,//宽度，前面加点表示百分比
				renderer : sizeRender,
				sortable : true,
				align : "center",
				sortable : true
			},{
				header : "创建时间",//表头文字
				dataIndex : "createTime",//数据来源
				renderer : timeRender,
				width : .2,//宽度，前面加点表示百分比
				sortable : true,
				align : "center",
				sortable : true
			},{
				header : "修改时间",//表头文字
				dataIndex : "updateTime",//数据来源
				renderer : timeRender,
				width : .2,//宽度，前面加点表示百分比
				sortable : true,
				align : "center",
				sortable : true
			}]);
		
		//定义数据列的数据来源
		var trashListStore = new Ext.data.JsonStore({
			url : path + "/courseware_query.do?status=2",
			root : "root",
			fields : ["id","courseName","summary","fileSize","createTime","updateTime"]
		});
		
		//加载数据
		trashListStore.load();
		
		//定义列表，把数据源和定义的列模型加载进来以显示
		var trashListGP = new Ext.grid.GridPanel({
			id : "trashListGP",
			region : "center",//设置显示内容的位置
			cm : resourceListCM,//指定列模型
			sm : checkBoxSM,
			closable : true,//指定为真使其可以关闭
			stripeRows : true,//true是显示行的分隔符，默认会加上这个样式x-grid3-row-alt，自己也可以覆盖
			loadMask : true,//true表示在数据加载过程中会有一个遮盖效果
			loadingText : "加载中...",
			store : trashListStore,//指定数据来源
			autoExpandColumn : "courseName",//grid会把你指定的这列自动扩充来填满空白
			columnLines : true,//true是显示列分隔符
			layout : "fit",//指定布局模式
			viewConfig : {
				forceFit : true				
			},
			tbar : resourceTrashToolbar,
			bbar : new Ext.PagingToolbar({
						id : "trashListGPPage",
						pageSize : 20,
						store : trashListStore,
						displayInfo : true
					})
		});
		
		
		//给行增加右键菜单--为什么不直接调这个事件rowcontextmenu
		trashListGP.on("rowcontextmenu",function(grid,rowIndex,e){
			e.preventDefault();//取消浏览器对当前事件做的默认操作
			if(rowIndex < 0){
				return;
			}
			var model = grid.getSelectionModel();
			model.selectRow(rowIndex);//选择这一行
			var rightClickMenu = new Ext.menu.Menu({
				items : resourceMenu
			});
			rightClickMenu.remove(rightClickMenu.findById("trashrsearch"));
			rightClickMenu.remove(rightClickMenu.findById("find"));
			rightClickMenu.showAt(e.getXY());//在右击的坐标处显示右键菜单
		});
		
		// 定义回收站列表窗口
		var trashListWindow = new Ext.Window({
					layout : "fit",
					title : "回收站",
					width : 850,
					height : 400,
					modal : true,
					resizable : false,
					closeAction : "close",
					items : [trashListGP],
					buttons : [{
								text : '关闭窗口',
								iconCls : 'np-reject-32',
								scale : 'medium',
								handler : function() {
									trashListWindow.hide();
								}
							}]
				});
	
		trashListWindow.show();
		
		//还原课件
		function recourceBack(){
			//获得用户选中的数据
			var selections = trashListGP.getSelectionModel().getSelections();
			if(selections == null || selections.length == 0){
				showWarn("请选择您要还原的课件！");
			}else{
				var selectionIds = getSelectionId(selections);
				Ext.Ajax.request({
						url : path + "/courseware_courseBack.do",
						method : "post",
						params : {selectionIds : selectionIds},
						success : function(result,request){
							doAjaxResponse(result, request,function() {
								recourceTrashFind();//重新加载回收站的数据
								resourceFind();//重新加载主页面的数据
							})
						}
					});
			}
		}
		
		//还原所有课件
		function recourceBackAll(){
			var bl = Ext.Msg.confirm("提示","您确认要还原所有的课件？",function(bl){
				if(bl == "yes"){
					Ext.Ajax.request({
						url : path + "/courseware_courseBackAll.do",
						success : function(result,request){
							doAjaxResponse(result, request,function() {
								recourceTrashFind();//重新加载回收站的数据
								resourceFind();//重新加载主页面的数据
							})
						}
					});
				}
			});
		}
		
		//彻底删除课件
		function recourceTrashDelete(){
			//获得用户选中的数据
			var selections = trashListGP.getSelectionModel().getSelections();
			if(selections == null || selections.length == 0){
				showWarn("请选择您要删除的课件！");
			}else{
				var selectionIds = getSelectionId(selections);
				var bl = Ext.Msg.confirm("提示","您确认要彻底删除该课件？(被删除课件无法还原)",function(bl){
					if(bl == "yes"){
						Ext.Ajax.request({
							url : path + "/courseware_remove.do",
							params : {selectionIds : selectionIds},
							method : "post",
							success : function(result,request){
								doAjaxResponse(result, request,function() {
									recourceTrashFind();//重新加载回收站的数据
								})
							}
						});
					}
				});
			}
		}
		
		//清空回收站
		function recourceClear(){
			var bl = Ext.Msg.confirm("提示","您确认要清空所有数据吗？(清空后数据无法还原)",function(bl){
				if(bl == "yes"){
					Ext.Ajax.request({
						url : path + "/courseware_removeAll.do",
						success : function(result,request){
							doAjaxResponse(result, request,function() {
								recourceTrashFind();//重新加载回收站的数据
							})
						}
					});
				}
			});
		}
		
		//查询
		function recourceTrashFind(){
			trashListStore.proxy.conn.url = path + "/courseware_findByName.do";
			if (resourceTrashToolbar.findById("trashrsearch").getValue() == null) {
				trashListStore.load({params:{status: "2"}});
			}else{
				trashListStore.load({params:{courseName: resourceTrashToolbar.findById("trashrsearch").getValue(),status: "2"}});
			}
		}
		
		function getSelectionId(selections){
			var arry = new Array();
			var i = 0;
			Ext.each(selections,function(item){//遍历record数组
	     		arry[i] = item.data.id;//将遍历出来的某个record加载到数组中
	     		i++;
	   	 	});
	   	 	return arry;
		}
	}
