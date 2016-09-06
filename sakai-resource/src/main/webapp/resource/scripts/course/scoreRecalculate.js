function fitScreen() {
	var sectionfrm = parent.document.getElementById("coursewareFrame");
	var fp1s = $(parent.document).find("iframe");
	var fp1;
	for (i = 0; i < fp1s.length; i++) {
		if (fp1s[i].id == "ifrm_c_score") {
			fp1 = fp1s[i];
			break;
		}
	}
	var fp2 = $(parent.parent.document).find(".portletMainIframe")[0];
	var extralHeight = 35;
	var mainHeight = $("#scoreListGP").height() + 50;
	if (mainHeight < 600) {
		mainHeight = 600;
	}

	$("body").height(mainHeight);

	if (fp1) {
		$(fp1).height($("body").outerHeight() + extralHeight);
		if (fp2) {
			$(fp2).height($(fp1).outerHeight() + 50);
		}
	}
}

var scoreListStore;
var searchPanel;

Ext.onReady(function() {// 课程空间新---平时成绩

	// 启用所有基于标签的提示
	Ext.QuickTips.init();

	// 开始时间
	var startTime = new Cls.form.DateTimeField({
		fieldLabel : '<font color="red">*</font>开始时间',  
		id : 'startTime',
		name : 'startTime',
		format : 'Y-m-d H:i:s',
		editable : false,
		style : 'padding-left:0px;',
		anchor : '95%,95%',
		allowBlank : false,
		listeners : {// 监听
			'select' : function(endDateDf, date) {
			}
		}
	});
	// 截止时间
	var endTime = new Cls.form.DateTimeField({
		fieldLabel : '<font color="red">*</font>结束时间',
		id : 'endTime',
		name : 'endTime',
		format : 'Y-m-d H:i:s',
		editable : false,
		style : 'padding-left:0px;',
		anchor : '95%,95%',
		allowBlank : false,
		listeners : {// 监听
			'select' : function(endDateDf, date) {
			}
		}
	});
	// 学号
	var stuNum = new Ext.form.TextField({
		fieldLabel : "学号",
		id : "stuNum",
		anchor : '99%',
		name : "stuNum"
	});

	// 学生姓名
	var stuName = new Ext.form.TextField({
		fieldLabel : "学生姓名",
		id : "stuName",
		anchor : '99%',
		name : "stuName"
	});
	var searchButton = new Ext.Button({
		// 页面上显示的文本
		text : "查询",
		id : "find",
		height : 25,
		// 图标路径，一般menu,button会有这个配置(属性)
//		iconCls : "folder_find",
		// 定义出发按钮所要执行的操作
		style: { marginRight: '10px', marginLeft:'10px'},
		handler : function() {
			if (!searchPanel.getForm().isValid()) {
				showInfo("查询条件不能为空");
				return;
			}

			scoreStore.load();
			
		}
	})

	var recalculateButton = new Ext.Button({
		// 页面上显示的文本
		text : "重新计算平时成绩",
		id : "recalculate",
		height : 25,
		// 图标路径，一般menu,button会有这个配置(属性)
//		iconCls : "folder_find",
		// 定义出发按钮所要执行的操作
		style: { marginRight: '10px', marginLeft:'10px'},
		handler : function() {
			var rows=Ext.getCmp("scoreListGP").getSelectionModel().getSelections(); //获取所有选中行，
			var str = '';
			for(var i=0;i <rows.length;i++){
				str = str + rows[i].get('studyrecordId') + ',';
			}
			if (str=='') {
				showInfo("请至少选择一条记录");
				return;
			}else{
				str = str.substring(0, str.length-1);
				var btn = Ext.Msg.confirm("提示", "确认要重新计算成绩吗？", function(btn, text) {
					if (btn == "yes") {
						Ext.Ajax.request({
							url : "scoreRecalculate_scoreRecalculate.do",
							method : "post",
							params : {
								studyRecordIds : str
							},
							success : function(result,request){
								if(result.responseText=='success'){
									scoreStore.reload();
								}
							}
						});
					}
				});
			}
		}
	})
	
	var recalculateAllButton = new Ext.Button({
		// 页面上显示的文本
		text : "全部重新计算成绩",
		id : "recalculateAll",
		height : 25,
		// 图标路径，一般menu,button会有这个配置(属性)
//		iconCls : "folder_find",
		// 定义出发按钮所要执行的操作
		style: { marginRight: '10px', marginLeft:'10px'},
		handler : function() {
			if (!searchPanel.getForm().isValid()) {
				showInfo("查询条件不能为空");
				return;
			}else{
				var btn = Ext.Msg.confirm("提示", "确认要按查询结果重新计算成绩吗？", function(btn, text) {
					if (btn == "yes") {
						Ext.Ajax.request({
							url : "scoreRecalculate_scoreRecalculateAll.do",
							method : "post",
							params : {
								startTime : startTime.getValue(),
								stuNum : stuNum.getValue(),
								stuName : stuName.getValue(),
								endTime : endTime.getValue()
							},
							success : function(result,request){
								if(result.responseText=='success'){
									scoreStore.reload();
								}
							}
						});
					}
				});
			}
		}
	})

	searchPanel = new Ext.form.FormPanel({// 表单面板
		url : 'scoreRecalculate_findScoreList.do',
		layout : 'form',
		labelAlign: "right",
		labelWidth: 80,
		region : "north",
		bodyStyle: "padding:5px",
		autoHeight : true,
		items : [ {
			layout : 'column',
			border:false,
			items : [ {
				columnWidth : .5,
				layout : 'form',
				border:false,
				items : [ startTime ]
			}, { 
				columnWidth : .5,
				layout : 'form',
				border:false,
				items : [ endTime ]
			} ]
		}, {
			layout : 'column',
			border:false,
			items : [ {
				columnWidth : .3,
				layout : 'form',
				border:false,
				items : [ stuNum ]
			}, {
				columnWidth : .3,
				layout : 'form',
				border:false,
				items : [ stuName ]
			}, {
				columnWidth : .4,
				layout : 'column',
				border:false,
				items : [ searchButton, recalculateButton,recalculateAllButton ]
			} ]
		} ]
	});
	
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false
	});

	// 定义列模型
	var scoreListCM = new Ext.grid.ColumnModel([ new Ext.grid.RowNumberer(), sm,{
		// 表头显示的文字
		header : "标识",
		// 数据来源
		dataIndex : "studyrecordId",
		// 是否隐藏
		hidden : true,
		// 是否可以编辑是否隐藏
		hideable : false
	}, {
		// 表头显示的文字
		header : "姓名",
		// 数据来源
		dataIndex : "studentName",
		width : .2,
		sortable : true
	}, {
		header : "学号",// 表头文字
		id : "stuNum",
		dataIndex : "stuNum",// 数据来源
		width : .2,
		sortable : true
	}, {
		header : "中心名称",// 表头文字
		dataIndex : "eduCenter",// 数据来源
		// renderer : sizeRender,
		width : .2,
		sortable : true
	}, {
		header : "成绩",// 表头文字
		dataIndex : "score",// 数据来源
		width : .2,
		sortable : true
	}, {
		header : "成绩更新时间",// 表头文字
		dataIndex : "scoreUpdateTime",// 数据来源
		width : .2,
		sortable : true
	} ]);

	// 定义数据列的数据来源
	scoreStore = new Ext.data.JsonStore({
		id : "scoreStore",
		url : "scoreRecalculate_findScoreList.do",// 查找要批改的作业记录
		root : "root",
		remoteSort: true,
		fields : [ "studyrecordId", "studentName", "stuNum", "eduCenter",
				"score", "userid", "startStudyTime", "scoreUpdateTime" ]
	});
	
	scoreStore.on('beforeload', function() {  
        Ext.apply(this.baseParams,{
			startTime : startTime.getValue(),
			stuNum : stuNum.getValue(),
			stuName : stuName.getValue(),
			endTime : endTime.getValue()
		});  
	});

	// 定义列表，把数据源和定义的列模型加载进来以显示
	var scoreListGP = new Ext.grid.GridPanel({
		id : "scoreListGP",
		region : "center",// 设置显示内容的位置
		cm : scoreListCM,// 指定列模型
		sm:sm,
		autoHeight : true,
		closable : true,// 指定为真使其可以关闭
		stripeRows : true,// true是显示行的分隔符，默认会加上这个样式x-grid3-row-alt，自己也可以覆盖
		loadMask : true,// true表示在数据加载过程中会有一个遮盖效果
		loadingText : "加载中...",
		store : scoreStore,// 指定数据来源
		autoExpandColumn : "courseName",// grid会把你指定的这列自动扩充来填满空白
		columnLines : true,// true是显示列分隔符
		layout : "fit",// 指定布局模式
		viewConfig : {
			forceFit : true
		},
		//selModel:Ext.create('Ext.selection.CheckboxModel',{mode:"SIMPLE"}),
		bbar : new Ext.PagingToolbar({
			id : "scoreGridPage",
			pageSize : 20,
			store : scoreStore,
			displayInfo : true
		})
	});

	// 工作区布局
	var viewport = new Ext.Viewport({
		id : 'viewport',
		layout : 'border', // 使用边界布局，页面被分割为东西南北中5个部分
		items : [ searchPanel, scoreListGP ]
	});
	fitScreen();
});
