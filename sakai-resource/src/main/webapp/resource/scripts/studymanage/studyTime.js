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

function openStudyInfo(studyrecordId){
	
	// 定义列模型
	var dataInfoListCM = new Ext.grid.ColumnModel([ new Ext.grid.RowNumberer(), 
	{
		// 表头显示的文字
		header : "章节",
		// 数据来源
		dataIndex : "title",
		width : .3,
		sortable : true
	}, {
		header : "开始学习时间",// 表头文字
		id : "startStudyTime",
		dataIndex : "startStudyTime",// 数据来源
		width : .25,
		sortable : true
	}, {
		header : "结束学习时间",// 表头文字
		id : "endStudyTime",
		dataIndex : "endStudyTime",// 数据来源
		width : .25,
		sortable : true
	}, {
		header : "学习时长",// 表头文字
		dataIndex : "studyTime",// 数据来源
		// renderer : sizeRender,
		width : .2,
		sortable : true
	}]);

	// 定义数据列的数据来源
	var dataInfoStore = new Ext.data.JsonStore({
		id : "dataInfoStore",
		url : "studyManage_getStudyInfo.do",
		root : "root",
		baseParams: {studyrecordId: studyrecordId},
		remoteSort: false,
		fields : [ "title", "startStudyTime", "endStudyTime",
				"studyTime" ]
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
	    title : '学习记录',
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
		
	// 开始时间
	var studyStartTime = new Cls.form.DateTimeField({
		fieldLabel : '开始时间',  
		id : 'studystudyStartTime',
		name : 'studystudyStartTime',
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
	var studyEndTime = new Cls.form.DateTimeField({
		fieldLabel : '结束时间',
		id : 'studyEndTime',
		name : 'studyEndTime',
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
			if (!Ext.fly('downForm')) {
				var downForm = document.createElement('form');
				downForm.id = 'downForm';
				downForm.name = 'downForm';
				downForm.className = 'x-hidden';
				downForm.action = 'studyManage_exportStudyTime.do';
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
				var studyStartTimeHidden = document.createElement('input');
				studyStartTimeHidden.type = 'hidden';// 隐藏域 
				studyStartTimeHidden.name = 'studyStartTime';// form表单参数
				studyStartTimeHidden.value = Ext.util.Format.date(studyStartTime.getValue(), 'Y-m-d H:m:s');  // form表单值
				downForm.appendChild(studyStartTimeHidden);
				
				var studyEndTimeHidden = document.createElement('input');
				studyEndTimeHidden.type = 'hidden';// 隐藏域
				studyEndTimeHidden.name = 'studyEndTime';// form表单参数
				studyEndTimeHidden.value = Ext.util.Format.date(studyEndTime.getValue(), 'Y-m-d H:m:s');// form表单值
				downForm.appendChild(studyEndTimeHidden);
				document.body.appendChild(downForm);
				
			}
			Ext.fly('downForm').dom.submit();
			if (Ext.fly('downForm')) {
				document.body.removeChild(downForm);
			}
		}
	})
	
	var searchPanel = new Ext.form.FormPanel({// 表单面板
		url : 'studyManage_findStudyTime.do',
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
				items : [ stuNum ]
			}, {
				columnWidth : .2,
				layout : 'form',
				border:false,
				items : [ stuName ]
			} ,{
				columnWidth : .25,
				layout : 'form',
				border:false,
				items : [ studyStartTime ]
			}, { 
				columnWidth : .25,
				layout : 'form',
				border:false,
				items : [ studyEndTime ]
			}, {
				columnWidth : .1,
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
		header : "学习记录id",
		// 数据来源
		dataIndex : "studyrecordId",
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
		width : .3,
		sortable : true
	}, {
		header : "学习时长（分钟）",// 表头文字
		dataIndex : "totalTime",// 数据来源
		// renderer : sizeRender,
		width : .1,
		sortable : true
	}, {
		header : "操作",// 表头文字
		width : .1,
		renderer:function(value,metaData,record)
		{
			return'<a href=\"javascript:openStudyInfo(\''+record.get('studyrecordId')+'\');\" \">查看学习情况<a/>';
		}
	}]);

	// 定义数据列的数据来源
	var dataStore = new Ext.data.JsonStore({
		id : "dataStore",
		url : "studyManage_findStudyTime.do",
		root : "root",
		remoteSort: true,
		fields : [ "studyrecordId","stuName", "stuNum", "sex",
				"totalTime","speName","className" ]
	});
	
	dataStore.on('beforeload', function() {  
        Ext.apply(this.baseParams,{
			stuName : stuName.getValue(),
			stuNum : stuNum.getValue(),
			studyStartTime : studyStartTime.getValue(),
			studyEndTime : studyEndTime.getValue()
		});  
	});
	dataStore.on('load', function() {  
		Ext.Ajax.request({
			url : "studyManage_getStudyTimeSum.do",
			method : "post",
			params:{
				stuName : stuName.getValue(),
				stuNum : stuNum.getValue(),
				studyStartTime : studyStartTime.getValue(),
				studyEndTime : studyEndTime.getValue()
			},
			success : function(result,request){
				var obj = Ext.decode(result.responseText);
				sumPanel.update("平均时长:"+obj.avgTotalTime+"分钟");  
			}
		});
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