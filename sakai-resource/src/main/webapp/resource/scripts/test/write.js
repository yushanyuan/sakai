Ext.onReady(function() {
	Ext.QuickTips.init();
	var store = new Ext.data.Store({
		//proxy用于从某个途径读取原始数据
		proxy : new Ext.data.HttpProxy({//后台返回JSON格式的数据
			url : 'test_getTestList.do'
		}),
		remoteStore : true, //允许远程排序
		pruneModifiedRecords : true, // 清除store中modified记录
		//reader用于将原始数据转换成Record实例
		reader : new Ext.data.JsonReader({
			totalProperty : 'tatalProperty',
			root : 'root'
		}, 
			[
			 {name : 'testId'},  //作业id
			 {name : 'testName'}, //作业名称
			 {name : 'totalScore'},//满分
			 {name : 'masteryScore'} //通过分数
			]
		)
	});
	var sm = new Ext.grid.CheckboxSelectionModel();
	
	var cm = new Ext.grid.ColumnModel([
		sm, //复选框 
		{
			header : '作业名称',    
			width : .2,          
			sortable : true,      
			dataIndex : 'testName'
		}, 
		{
			header : '满分',
			width : .1,
			sortable : true,
			dataIndex : 'totalScore'
		}, 
		{
			header : '通过分数',
			width : .1,
			sortable : true,
			dataIndex : 'masteryScore'
		}
	]);
	
	var writeBut = new Ext.Button({
		text : '写作业',
		handler : function() {
			var rows = grid.getSelectionModel().getSelections();
			if (rows.length == 1) {
				var record = rows[0];
				var testId = record.get("testId");
				var masteryScore = record.get("masteryScore");
				var totalScore = record.get("totalScore");
				var samepaper = record.get("samepaper");
				var param = "testId=" + testId + "&masteryScore=" + masteryScore + "&samepaper=" + samepaper + "&totalScore=" + totalScore;
				window.showModalDialog("test_writeTestInit.do?" + param, window,
					"dialogWidth:" + window.screen.availWidth + ";dialogHeight:" + window.screen.availHeight);
			}else if (rows.length > 1) { //选择了多条记录
				showInfo("只能选择一条记录进行操作！")
			} else {//没有选择记录
				showInfo("没有选择记录");
			}
		}
	})
	
	
	var grid = new Ext.grid.GridPanel({
        store: store,
        viewConfig : {                  //自动计算列宽
			forceFit : true
		},	
        cm : cm,                        //列信息
		sm : sm,						//复选框			
		loadMask :true,
        frame: false,
        autoHeight:true,
        renderTo : Ext.getBody(),
        tbar : [writeBut],
        bbar : [writeBut]
    });
   
	store.load();
})