var store;
var searchPanel;
Ext.onReady(function() {
	// 启用所有基于标签的提示
	Ext.QuickTips.init();

	// 批改试卷名称下拉菜单
	var testId = new Ext.form.ComboBox({
		editable : false,
		name : 'testId',
		store : new Ext.data.SimpleStore({
			fields : ['value', 'text'],
			proxy : new Ext.data.HttpProxy({
				url : 'test_loadTestBox.do'
			})
		}),
		fieldLabel : '<font style=color:red>*</font>批改试卷名称',
		hiddenName : 'testId',
		emptyText : '-',
		mode : 'local',
		loadingText : '加载中...',
		triggerAction : 'all',
		valueField : 'value',
		displayField : 'text',
		width : 80,
		anchor : '95%',
		selectOnFocus : true,
		allowBlank : false
	});
	testId.getStore().load();

	var checkStatusBox = new Ext.form.ComboBox({
		editable : false,
		name : 'checkStatus',
		store : new Ext.data.SimpleStore({
			fields : ['value', 'text'],
			data : checkstatusArray
		}),
		fieldLabel : '<font style=color:red>*</font>批改状态',
		hiddenName : 'checkStatus',
		emptyText : '-',
		mode : 'local',
		value:'0',
		loadingText : '加载中...',
		triggerAction : 'all',
		valueField : 'value',
		displayField : 'text',
		width : 90,
		anchor : '95%',
		selectOnFocus : true,
		allowBlank : false
	});

	// 截止时间
	var endDateDf = new Cls.form.DateTimeField({
		fieldLabel : '<span style="color:red">*</span>截止时间',
		id : 'endTime',
		name : 'endTime',
		format : 'Y-m-d H:i:s',
		editable : false,
		style : 'padding-left:0px;',
		anchor : '95%,95%',
		allowBlank : false
	});
	var searchButton = new Ext.Button({
		// 页面上显示的文本
		text : "查询",
		id : "find",
		height : 25,
		// 图标路径，一般menu,button会有这个配置(属性)
		iconCls : "folder_find",
		// 定义出发按钮所要执行的操作
		handler : function() {
			if (!searchPanel.getForm().isValid()) {
				showInfo("查询条件不能为空");	
				return;
			}
		
			store.load({
				params : {
					testId :testId.getValue(),
					checkStatus : checkStatusBox.getValue(),
					endTime : endDateDf.getValue()
				}
			});
		}
	})
	searchPanel = new Ext.form.FormPanel({
		layout : 'fit',
		region : "north",
		height : 30,
		items : [{
			layout : 'column',
			items : [{// 第一列：
				columnWidth : .25,
				layout : 'form',
				items : [testId]
			}, {	// 第二列：
				columnWidth : .25,
				layout : 'form',
				items : [checkStatusBox]
			}, {// 第3列：
				columnWidth : .4,
				layout : 'form',
				items : [endDateDf]
			}, {// 第4列：
				columnWidth : .1,
				layout : 'form',
				items : [searchButton]
			}]
		}]
	});
		

	// 定义列模型
	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), {
			header : "姓名",
			dataIndex : "studentName",
			width : .25,
			sortable : true
		}, {
			header : "学号",// 表头文字
			dataIndex : "stuNum",// 数据来源
			width : .25,
			sortable : true
		}, {
			header : "中心名称",// 表头文字
			dataIndex : "eduCenter",// 数据来源
			width : .1,
			sortable : true
		}, {
			header : "成绩",// 表头文字
			dataIndex : "score",// 数据来源
			width : .2,
			sortable : true
		}, {
			header : "操作",// 表头文字
			dataIndex : "operation",// 数据来源
			width : .2,
			sortable : true,
			renderer : function(v,o,r) {
				return "<a href='test_checkTestInit.do?recordId="+r.get("id")+"&studentId="+r.get("userid")+"&paperId="+r.get("paperid")+"' target='_blank'>批改</a>";
			}
		}
	]);

	// 定义数据列的数据来源
	store = new Ext.data.JsonStore({
		url : "test_findTestRecordList.do",
		root : "root",
		fields : ["id", "studentName", "stuNum", "eduCenter", "score", "operation","userid","paperid"]
	});

	// 定义列表，把数据源和定义的列模型加载进来以显示
	var gridPanel = new Ext.grid.GridPanel({
		region : "center",// 设置显示内容的位置
		cm : cm,// 指定列模型
		autoHeight : true,
		closable : true,// 指定为真使其可以关闭
		stripeRows : true,// true是显示行的分隔符，默认会加上这个样式x-grid3-row-alt，自己也可以覆盖
		loadMask : true,// true表示在数据加载过程中会有一个遮盖效果
		loadingText : "加载中...",
		store : store,// 指定数据来源
		columnLines : true,// true是显示列分隔符
		layout : "fit",// 指定布局模式
		viewConfig : {
			forceFit : true
		},
		bbar : new Ext.PagingToolbar({
			id : "checkWorkGridPage",
			pageSize : 20,
			store : store,
			displayInfo : true
		})
	});

	// 工作区布局
	var viewport = new Ext.Viewport({
		id : 'viewport',
		layout : 'border', // 使用边界布局，页面被分割为东西南北中5个部分
		items : [searchPanel, gridPanel]
	});

});