function fitScreen() {
    var fp1 = $(parent.document).find("iframe")[0];
	var fp2 = $(parent.parent.document).find("#ifrm_study_manage")[0];
	var fp3 = $(parent.parent.parent.document).find(".portletMainIframe")[0];
    $("body").height(50+$("#panel").height());
    
	if($(fp2).height() < 700){
    	$(fp2).height(700);
	}
    if($(fp3).height() < 700){
    	$(fp3).height(700);
	}    
}

function openTestInfo(testrecordId){
	
	// 定义列模型
	var dataInfoListCM = new Ext.grid.ColumnModel([ new Ext.grid.RowNumberer(), 
	{
		// 表头显示的文字
		header : "尝试记录id",
		// 数据来源
		dataIndex : "testattemptId",
		hidden : true,
		// 是否可以编辑是否隐藏
		hideable : false
	},{
		// 表头显示的文字
		header : "作业试卷ID",
		// 数据来源
		dataIndex : "testPaperid",
		hidden : true,
		// 是否可以编辑是否隐藏
		hideable : false
	},{
		// 表头显示的文字
		header : "用户ID",
		// 数据来源
		dataIndex : "userId",
		hidden : true,
		// 是否可以编辑是否隐藏
		hideable : false
	},{
		// 表头显示的文字
		header : "作业ID",
		// 数据来源
		dataIndex : "testId",
		hidden : true,
		// 是否可以编辑是否隐藏
		hideable : false
	}, {
		header : "开始时间",// 表头文字
		id : "startTime",
		dataIndex : "startTime",// 数据来源
		width : .3,
		sortable : true
	}, {
		header : "结束时间",// 表头文字
		id : "endTime",
		dataIndex : "endTime",// 数据来源
		width : .3,
		sortable : true
	}, {
		header : "客观题得分",// 表头文字
		id : "objScore",
		dataIndex : "objScore",// 数据来源
		width : .1,
		sortable : true
	}, {
		header : "主观题得分",// 表头文字
		id : "subScore",
		dataIndex : "subScore",// 数据来源
		width : .1,
		sortable : true
	}, {
		header : "成绩",// 表头文字
		dataIndex : "score",// 数据来源
		// renderer : sizeRender,
		width : .1,
		sortable : true
	}, {
		header : "操作",// 表头文字
		width : .1,
		renderer:function(value,metaData,record)
		{
			return '<a href="melete_getTestAttemptRecordPaper.do?testPaperid='+record.get('testPaperid')+'&testattemptId='+record.get('testattemptId')+'&testId='+record.get('testId')+'&userId='+record.get('userId')+'" target="_blank">查看</a>';
		}
	}]);

	// 定义数据列的数据来源
	var dataInfoStore = new Ext.data.JsonStore({
		id : "dataStore",
		url : "studyManage_getTestScoreInfo.do",
		root : "root",
		baseParams: {testrecordId: testrecordId},
		remoteSort: false,
		fields : [ "userId","testId","testattemptId", "startTime", "endTime","testPaperid","objScore","subScore",
				"score" ]
	});
	
	// 定义列表，把数据源和定义的列模型加载进来以显示
	var dataInfoListGP = new Ext.grid.GridPanel({
		id : "dataInfoListGP",
		region : "center",// 设置显示内容的位置
		cm : dataInfoListCM,// 指定列模型
		autoHeight : true,
		closable : true,// 指定为真使其可以关闭
		stripeRows : true,// true是显示行的分隔符，默认会加上这个样式x-grid3-row-alt，自己也可以覆盖
		loadMask : true,// true表示在数据加载过程中会有一个遮盖效果
		loadingText : "加载中...",
		store : dataInfoStore,// 指定数据来源
		//autoExpandColumn : "title",// grid会把你指定的这列自动扩充来填满空白
		columnLines : true,// true是显示列分隔符
		layout : "fit",// 指定布局模式
		viewConfig : {
			forceFit : true
		}
	});
	
	
	// 弹出 窗口
	 var infoWin = new Ext.Window({
    	layout:'form',
	    width:650,	
        height:400,		
		autoHeight:false,
		autoScroll:true,
	    title : '阶段成绩',
	    closeAction:'hide',
	    draggable : false,
		resizable : false,
		modal:true,//True 表示为当window显示时对其后面的一切内容进行遮罩
	    plain: true,//True表示为渲染window body的背景为透明的背景，这样看来window body与边框元素（framing elements）融为一体， false表示为加入浅色的背景，使得在视觉上body元素与外围边框清晰地分辨出来（默认为false）。
	    items: [dataInfoListGP],
	    border :false,//True表示为显示出面板body元素的边框，false则隐藏
	    hideBorders :false,
	    frame :true,
	    buttons:[{
   	 		text : '关闭',
			iconCls : "np-reject",
			handler : function() {
				infoWin.hide();
   	 		}
	    }]
	});
	 infoWin.show();
	 dataInfoStore.load();
}

// 开启提示功能
Ext.QuickTips.init();

Ext.onReady(function(){
	
	//作业名称数据加载
	var testSimpleStore = new Ext.data.SimpleStore({
		fields: ["value","text"], 
		proxy : new Ext.data.HttpProxy({
			url : 'studyManage_getTestList.do'
		})
	});
	
	//作业名称下拉框
	var testNameCB = new Ext.form.ComboBox({
		fieldLabel: "<font color='red'>*</font>阶段作业",
		xtype: "combo",
		name: "testId",
		hiddenName: "testId",
		emptyText: "---请选择---",
		mode: "local",//表示数据是从本地读取，默认是remote从服务器上读取
		valueField: "value",//这个值必须和store对应的fields里的值对应
		displayField: "text",//这个值必须和store对应的fields里的值对应
		store:testSimpleStore,
		anchor : '99%',
		triggerAction : 'all',
		grow: true,//根据内容自动伸缩
		editable: false,//设置下拉框是否可编辑，默认为true
		selectOnFocus : true,
		allowBlank : false
	});	
	testSimpleStore.load();
	
	var scoreStart = new Ext.form.NumberField({
			fieldLabel: "最低分数",
			id: "scoreStart",
			allowBlank: true,//不允许为空
			allowDecimals: false,//不允许是小数
			allowNegative: false,//不允许为负数
			maxText: "输入值不能大于100",
			minText: "输入值不能小于0",
			nanText: "只能输入正整数",
			anchor : '99%',
			minValue: 0,//最小值
			maxValue: 100,//最大值
			name: "scoreStart"
		});
	var scoreEnd = new Ext.form.NumberField({
		fieldLabel: "最高分数",
		id: "scoreEnd",
		allowBlank: true,//不允许为空
		anchor : '99%',
		allowDecimals: false,//不允许是小数
		allowNegative: false,//不允许为负数
		blankText: "不能为空",
		maxText: "输入值不能大于100",
		minText: "输入值不能小于0",
		nanText: "只能输入正整数",
		minValue: 0,//最小值
		maxValue: 100,//最大值
		name: "scoreEnd"
	});
		
	// 开始时间
	var testStartTime = new Cls.form.DateTimeField({
		fieldLabel : '开始时间',  
		id : 'testStartTime',
		name : 'testStartTime',
		format : 'Y-m-d H:i:s',
		editable : false,
		style : 'padding-left:0px;',
		anchor : '95%,95%',
		listeners : {// 监听
			'select' : function(endDateDf, date) {
			}
		}
	});
	// 截止时间
	var testEndTime = new Cls.form.DateTimeField({
		fieldLabel : '结束时间',
		id : 'testEndTime',
		name : 'testEndTime',
		format : 'Y-m-d H:i:s',
		editable : false,
		style : 'padding-left:0px;',
		anchor : '95%,95%',
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
			if(!searchPanel.getForm().isValid()){
				return;
			}
			dataStore.load();
		}
	})

	var exportButton = new Ext.Button({
		// 页面上显示的文本
		text : "导出",
		id : "export",
		height : 25,
		// 图标路径，一般menu,button会有这个配置(属性)
//		iconCls : "folder_find",
		// 定义出发按钮所要执行的操作
		style: { marginRight: '10px', marginLeft:'10px'},
		handler : function() {
			if(!searchPanel.getForm().isValid()){
				return;
			}
			if (!Ext.fly('downForm')) {
				var downForm = document.createElement('form');
				downForm.id = 'downForm';
				downForm.name = 'downForm';
				downForm.className = 'x-hidden';
				downForm.action = 'studyManage_exportTestScore.do';
				downForm.method = 'post';
				downForm.target = '_blank'; // 打开新的下载页面
				var stuNameHidden = document.createElement('input');
				stuNameHidden.type = 'hidden';// 隐藏域
				stuNameHidden.name = 'stuName';// form表单参数
				stuNameHidden.value = stuName.getValue();// form表单值
				downForm.appendChild(stuNameHidden);
				
				var stuNumHidden = document.createElement('input');
				stuNumHidden.type = 'hidden';// 隐藏域
				stuNumHidden.name = 'stuNum';// form表单参数
				stuNumHidden.value = stuNum.getValue();// form表单值
				downForm.appendChild(stuNumHidden);
				
				var scoreStartHidden = document.createElement('input');
				scoreStartHidden.type = 'hidden';// 隐藏域
				scoreStartHidden.name = 'scoreStart';// form表单参数
				scoreStartHidden.value = scoreStart.getValue();// form表单值
				downForm.appendChild(scoreStartHidden);
				
				var scoreEndHidden = document.createElement('input');
				scoreEndHidden.type = 'hidden';// 隐藏域
				scoreEndHidden.name = 'scoreEnd';// form表单参数
				scoreEndHidden.value = scoreEnd.getValue();// form表单值
				downForm.appendChild(scoreEndHidden);
				document.body.appendChild(downForm);
				
				var testStartTimeHidden = document.createElement('input');
				testStartTimeHidden.type = 'hidden';// 隐藏域
				testStartTimeHidden.name = 'testStartTime';// form表单参数
				testStartTimeHidden.value = Ext.util.Format.date(testStartTime.getValue(), 'Y-m-d H:m:s');// form表单值
				downForm.appendChild(testStartTimeHidden);
				document.body.appendChild(downForm);
				
				var testEndTimeHidden = document.createElement('input');
				testEndTimeHidden.type = 'hidden';// 隐藏域
				testEndTimeHidden.name = 'testEndTime';// form表单参数
				testEndTimeHidden.value = Ext.util.Format.date(testEndTime.getValue(), 'Y-m-d H:m:s');// form表单值
				downForm.appendChild(testEndTimeHidden);
				document.body.appendChild(downForm);
				
				var testNameCBHidden = document.createElement('input');
				testNameCBHidden.type = 'hidden';// 隐藏域
				testNameCBHidden.name = 'testId';// form表单参数
				testNameCBHidden.value = testNameCB.getValue();// form表单值
				downForm.appendChild(testNameCBHidden);
				document.body.appendChild(downForm);
				
			}
			Ext.fly('downForm').dom.submit();
			if (Ext.fly('downForm')) {
				document.body.removeChild(downForm);
			}
		}
	})
	
	var searchPanel = new Ext.form.FormPanel({// 表单面板
		url : 'studyManage_findTestScore.do',
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
				columnWidth : .2,
				layout : 'form',
				border:false,
				items : [ testNameCB ]
			}, {
				columnWidth : .2,
				layout : 'form',
				border:false,
				items : [ scoreStart ]
			} ,{
				columnWidth : .2,
				layout : 'form',
				border:false,
				items : [ scoreEnd]
			}, {
				columnWidth : .2,
				layout : 'form',
				border:false,
				items : [ stuNum ]
			}, {
				columnWidth : .2,
				layout : 'form',
				border:false,
				items : [ stuName ]
			} ]
		} ,{
			layout : 'column',
			border:false,
			items : [{
				columnWidth : .25,
				layout : 'form',
				border:false,
				items : [ testStartTime ] 
			}, { 
				columnWidth : .25,
				layout : 'form',
				border:false,
				items : [ testEndTime ]
			}, {
				columnWidth : .2,
				layout : 'column',
				border:false,
				items : [ searchButton, exportButton ]
			}]
		} ]
	});
	
	// 汇总信息
	var sumPanel = new Ext.Panel({
        title: '汇总信息',//标题
        region : "south",
		bodyStyle: "padding:5px",
		autoHeight : true
    });
	
	// 定义列模型
	var dataListCM = new Ext.grid.ColumnModel([ new Ext.grid.RowNumberer(), {
		// 表头显示的文字
		header : "作业记录id",
		// 数据来源
		dataIndex : "testrecordId",
		hidden : true,
		// 是否可以编辑是否隐藏
		hideable : false
	}, {
		// 表头显示的文字
		header : "姓名",
		// 数据来源
		dataIndex : "stuName",
		width : .1,
		sortable : true
	}, {
		header : "学号",// 表头文字
		id : "stuNum",
		dataIndex : "stuNum",// 数据来源
		width : .1,
		sortable : true
	}, {
		header : "性别",// 表头文字
		id : "sex",
		dataIndex : "sex",// 数据来源
		width : .1,
		sortable : true
	}, {
		header : "专业",// 表头文字
		id : "speName",
		dataIndex : "speName",// 数据来源
		width : .2,
		sortable : true
	}, {
		header : "班级",// 表头文字
		id : "className",
		dataIndex : "className",// 数据来源
		width : .2,
		sortable : true
	}, {
		header : "尝试次数",// 表头文字
		dataIndex : "attemptNumber",// 数据来源
		// renderer : sizeRender,
		width : .1,
		sortable : true
	}, {
		header : "最高得分",// 表头文字
		dataIndex : "score",// 数据来源
		// renderer : sizeRender,
		width : .1,
		sortable : true
	}, {
		header : "操作",// 表头文字
		width : .1,
		renderer:function(value,metaData,record)
		{
			return'<a href=\"javascript:openTestInfo(\''+record.get('testrecordId')+'\');\" \">查看作业情况<a/>';
		}
	}]);

	// 定义数据列的数据来源
	var dataStore = new Ext.data.JsonStore({
		id : "dataStore",
		url : "studyManage_findTestScore.do",
		root : "root",
		remoteSort: true,
		fields : [ "testrecordId","stuName", "stuNum", "sex",
				"attemptNumber","score","speName","className" ]
	});
	
	dataStore.on('beforeload', function() {  
		if(!searchPanel.getForm().isValid()){
			Ext.apply(this.baseParams,{
				stuName : stuName.getValue(),
				stuNum : stuNum.getValue(),
				testStartTime : testStartTime.getValue(),
				testEndTime : testEndTime.getValue(),
				testId:'9999999',
				scoreStart: scoreStart.getValue(),
				scoreEnd: scoreEnd.getValue()
			});  
		}else{
			Ext.apply(this.baseParams,{
				stuName : stuName.getValue(),
				stuNum : stuNum.getValue(),
				testStartTime : testStartTime.getValue(),
				testEndTime : testEndTime.getValue(),
				testId:testNameCB.getValue(),
				scoreStart: scoreStart.getValue(),
				scoreEnd: scoreEnd.getValue()
			}); 
		}
         
	});
	dataStore.on('load', function() {  
		if(searchPanel.getForm().isValid()){
			Ext.Ajax.request({
				url : "studyManage_getTestScoreSum.do",
				method : "post",
				params:{
					stuName : stuName.getValue(),
					stuNum : stuNum.getValue(),
					testStartTime : testStartTime.getValue(),
					testEndTime : testEndTime.getValue(),
					testId:testNameCB.getValue(),
					scoreStart: scoreStart.getValue(),
					scoreEnd: scoreEnd.getValue()
				},
				success : function(result,request){
					var obj = Ext.decode(result.responseText);
					sumPanel.update(obj.testName+" 其所占分数比例:"+obj.testRatio+"%<br/>平均尝试次数："+obj.avgCount+"<br/>平均分数:"+obj.avgScore);  
				}
			});  
		}
	});

	// 定义列表，把数据源和定义的列模型加载进来以显示
	var dataListGP = new Ext.grid.GridPanel({
		id : "dataListGP",
		region : "center",// 设置显示内容的位置
		cm : dataListCM,// 指定列模型
		autoHeight : true,
		closable : true,// 指定为真使其可以关闭
		stripeRows : true,// true是显示行的分隔符，默认会加上这个样式x-grid3-row-alt，自己也可以覆盖
		loadMask : true,// true表示在数据加载过程中会有一个遮盖效果
		loadingText : "加载中...",
		store : dataStore,// 指定数据来源
		autoExpandColumn : "courseName",// grid会把你指定的这列自动扩充来填满空白
		columnLines : true,// true是显示列分隔符
		layout : "fit",// 指定布局模式
		viewConfig : {
			forceFit : true
		},
		//selModel:Ext.create('Ext.selection.CheckboxModel',{mode:"SIMPLE"}),
		bbar : new Ext.PagingToolbar({
			id : "dataGridPage",
			pageSize : 20,
			store : dataStore,
			displayInfo : true
		})
	});

	//右边的主面板
	var panel = new Ext.Panel({
		id: "panel",
 		split: true,
 		renderTo : Ext.getBody(),
 		region:'center',
		items : [ searchPanel, dataListGP,sumPanel ]
	});
	fitScreen();
})