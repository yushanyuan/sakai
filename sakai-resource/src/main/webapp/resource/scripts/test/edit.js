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
			 {name : 'samepaper'}, //重做随机取卷:0是、1否
			 {name : 'masteryScore'}, //通过分数
			 {name : 'schemaId'}, //策略id
			 {name : 'schemaName'}, //策略名称
			 {name : 'totalScore'},//满分
			 {name : 'status'}//状态
			]
		),
		listeners :{
			load : function(){
				pageHeightInit();
			}
		}
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
			header : '是否使用同一策略',
			width : .2,
			sortable : true,
			dataIndex : 'samepaper',
			renderer : function(v){//列的渲染函数
				return v=="1"?"是":"否";
			}
		}, 
		{
			header : '策略',
			width : .2,
			sortable : true,
			dataIndex : 'schemaName'
		}, 
		{
			header : '满分',
			width : .1,
			sortable : true,
			dataIndex : 'totalScore'
		}, 
		{
			header : '通过分数(%)',
			width : .1,
			sortable : true,
			dataIndex : 'masteryScore'
		}, 
		{
			header : '状态',
			width : .2,
			sortable : true,
			dataIndex : 'status',
			renderer : function(v){//列的渲染函数
				return v=="1"?"未发布":(v=="2"?"已发布":"发布中");
			}
		}
	]);
	
	var addBut = new Ext.Button({
		text : '新增',
		handler : function() {
			addTest();
		}
	})
	
	var updateBut = new Ext.Button({
		text : '修改',
		handler : function() {
			var rows = grid.getSelectionModel().getSelections();
			if (rows.length == 1) {
				updateTest(rows[0]);
			}else if (rows.length > 1) { //选择了多条记录
				showInfo("只能选择一条记录进行操作！")
			} else {//没有选择记录
				showInfo("没有选择记录");
			}
		}
	})
	
	var deleteBut = new Ext.Button({
		text : '删除',
		handler : function() {
			var rows = grid.getSelectionModel().getSelections();
			if (rows.length == 0) {
				showInfo("没有选择记录");
			}else{
				showQuestions('确定要删除选中的作业?', function(btn) {
					if (btn == 'ok') {
//						load_Mask.show();
						var ids = "";
						for(var i=0;i<rows.length;i++)
						{
							var record = rows[i];
							var testId = record.get('testId');
							ids += testId + ",";					
						}
						Ext.Ajax.request({
							url : 'test_deleteTest.do',
							success : function(response, options) {
//								loadMask.hide();
								showInfo("执行成功");
								store.reload();
							},
							failure : function(response, options) {
							},
							params : {
								testId : ids
							}
						});
					}
				})
			}
		}
	});
	
	
    var publishBut = new Ext.Button({
		text : '发布',
		handler : function() {
			var rows = grid.getSelectionModel().getSelections();
			if (rows.length == 1) {
				var record = rows[0];
				showInfo("试卷发布中，该过程耗时较长，您可以先做其他操作");
				Ext.Ajax.request({
					url : 'test_publishTest.do',
					success : function(response, options) {
						showInfo("执行成功");
						store.reload();
					},
					params : {
						testId : record.get("testId"),
						schemaId : record.get("schemaId")
					}
				});
			}else if (rows.length > 1) { //选择了多条记录
				showInfo("只能选择一条记录进行操作！")
			} else {//没有选择记录
				showInfo("没有选择记录");
			}
		}
	});
	
	var paperBut = new Ext.Button({
		text : '进入题库',
		handler : function() {
			window.open(examSysEntryUrl,"paperWin","_blank");
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
        tbar : [addBut, updateBut, deleteBut, publishBut, paperBut],
        bbar : [addBut, updateBut, deleteBut, publishBut, paperBut]
    });
    
    store.load();
   
    //添加作业
	function addTest() {
		var testName = new Ext.form.TextField({
			name : 'testName',
			fieldLabel : '<font style=color:red>*</font>作业名称',
			anchor : '99%',
			allowBlank : false
		});
		var samepaper = new Ext.form.RadioGroup({
			name : 'samepaper',
			fieldLabel : '<font style=color:red>*</font>重做随机取卷',
			anchor : '99%',
			columns : [80, 80],
			vertical : true,
			allowBlank : false,
			items : new Ext.ux.RadioGroupData({
				baseData : samaArray,
				defaultValue : '1',
				name : 'samepaper'
			})
		});
		var schemaId = new Ext.form.ComboBox({
			editable : false,
			name : 'schemaId',
			store : new Ext.data.SimpleStore({
				fields : ['value', 'text', 'score'],
				proxy : new Ext.data.HttpProxy({
					url : 'test_loadSchemaBox.do'
				})
			}),
			hiddenName : 'schemaId',
			emptyText : '-',
			mode : 'local',
			loadingText : '加载中...',
			triggerAction : 'all',
			valueField : 'value',
			displayField : 'text',
			anchor : '50%',
			selectOnFocus : true,
			allowBlank : false,
			listeners : {
				select : function(combo, record, index){
					totalScore.setValue(record.get("score"));
				}
			}
		});
		var checkButton = new Ext.Button({
			text : '预览策略',
			width : 100,
			handler : function() {
				var schemaValue = schemaId.getValue();
				if (schemaValue == "") {
					showInfo("请选择一个策略");
					return;
				}
				var schemaurl =  examSysSchemaUrl + schemaValue;
				window.open(schemaurl);
			}
		})
		var totalScore = new Ext.form.TextField({
			name : 'totalScore',
			fieldLabel : '总分',
			value : '0',
			anchor : '99%',
			readOnly : true
		});
		var masteryScore = new Ext.form.NumberField({
			name : 'masteryScore',
			fieldLabel : '<font style=color:red>*</font>通过分数百分比',
			emptyText : '0',
			anchor : '50%',
			allowNegative : false,// 不能输入负数
			allowDecimals : false, // 不能输入小数
			validator : function(value) {
				var total = totalScore.getValue();
				if (new Number(value) > 100) {
					return false;
				}
				return true;
			},
			invalidText : '通过分数百分比不能大于100'
		})
		var addForm = new Ext.form.FormPanel({
			labelAlign : 'right',
			title : '新增作业',
			labelWidth : 180,
			labelSeparator : '：',
			autoHeight : true,
			anchor : '95%',
			lines : false,
			autoScroll : false,
			items : [{
				layout : 'form',
				items : [
					testName,
					samepaper, 
					{
						xtype : 'compositefield',
						fieldLabel : '<font style=color:red>*</font>策略列表',
						msgTarget : 'side',
						anchor : '-20',
						defaults : {
							flex : 1
						},
						items : [schemaId, checkButton]
					}, 
					totalScore,
					{
						xtype : 'compositefield',
						msgTarget : 'side',
						anchor : '-20',
						defaults : {
							flex : 1
						},
						items : [masteryScore, new Ext.form.Label({text : '%'})]
					}
				]
			}]
		});
		var addWin = new Ext.Window({
		    layout:'fit',
		    x : 0,
		    y : 0,
		    width:document.body.clientWidth-50,
		    autoHeight : true,
		    autoScroll : false,
		    title : '添加作业',
		    closeAction:'close',
		    draggable : false,
			resizable : false,
			modal : true,
			plain : true,
			items : [addForm],
			buttons : [{
				text : '保存',
				handler : function() {
					if (!addForm.getForm().isValid()) {
						showInfo("数据校验未通过，请检查有效性");
						return;
					}
					mask.show();
					addForm.getForm().submit({
						clientValidation : true,
						url : 'test_addTest.do',
						success : function(form, action) {
							mask.hide();
							store.reload();
							showInfo("保存成功");
							addWin.close();
						},
						failure : function(form, action) {
						}
					});
				}
			}, {
				text : '取消',
				handler : function() {
					addWin.close();
				}
			}]
		});
		addWin.show();
		
		var mask = new Ext.LoadMask(addWin.body, {
			msg : '程序运行中....'
		});
		mask.show();
		schemaId.getStore().load({
			callback : function(r, callOptions, successFlag) {
				mask.hide();
			}
		});
	}
	
	//修改作业
	function updateTest(record) {
		var testName = new Ext.form.TextField({
			name : 'testName',
			value : record.get("testName"),
			fieldLabel : '<font style=color:red>*</font>作业名称',
			anchor : '99%',
			allowBlank : false
		});
		var samepaper = new Ext.form.RadioGroup({
			name : 'samepaper',
			value : record.get("samepaper"),
			fieldLabel : '<font style=color:red>*</font>重做随机取卷',
			anchor : '99%',
			columns : [80, 80],
			vertical : true,
			allowBlank : false,
			items : new Ext.ux.RadioGroupData({
				baseData : samaArray,
				name : 'samepaper'
			})
		});
		var schemaId = new Ext.form.ComboBox({
			editable : false,
			name : 'schemaId',
			store : new Ext.data.SimpleStore({
				fields : ['value', 'text', 'score'],
				proxy : new Ext.data.HttpProxy({
					url : 'test_loadSchemaBox.do'
				})
			}),
			hiddenName : 'schemaId',
			emptyText : '-',
			mode : 'local',
			loadingText : '加载中...',
			triggerAction : 'all',
			valueField : 'value',
			displayField : 'text',
			anchor : '99%',
			selectOnFocus : true,
			allowBlank : false,
			listeners : {
				select : function(combo, record, index){
					totalScore.setValue(record.get("score"));
				}
			}
		});
		var checkButton = new Ext.Button({
			text : '预览策略',
			width : 100,
			handler : function() {
				var schemaValue = schemaId.getValue();
				if (schemaValue == "") {
					showInfo("请选择一个策略");
					return;
				}
				var schemaurl =  examSysSchemaUrl + schemaValue;
				window.open(schemaurl);
			}
		})
		var totalScore = new Ext.form.TextField({
			name : 'totalScore',
			value : record.get("totalScore"),
			fieldLabel : '总分',
			anchor : '99%',
			readOnly : true
		});
		var masteryScore = new Ext.form.NumberField({
			name : 'masteryScore',
			value : record.get("masteryScore"),
			fieldLabel : '<font style=color:red>*</font>通过分数百分比',
			emptyText : '0',
			anchor : '50%',
			allowNegative : false,// 不能输入负数
			allowDecimals : false, // 不能输入小数
			validator : function(value) {
				var total = totalScore.getValue();
				if (new Number(value) > 100) {
					return false;
				}
				return true;
			},
			invalidText : '通过分数百分比不能大于100'
		});
		var updateForm = new Ext.form.FormPanel({
			labelAlign : 'right',
			title : '修改作业',
			labelWidth : 180,
			labelSeparator : '：',
			autoHeight : true,
			anchor : '95%',
			lines : false,
			autoScroll : false,
			items : [{
				layout : 'form',
				items : [
					testName,  
					samepaper, 
					{
						xtype : 'compositefield',
						fieldLabel : '<font style=color:red>*</font>策略列表',
						msgTarget : 'side',
						anchor : '-20',
						defaults : {
							flex : 1
						},
						items : [schemaId, checkButton]
					}, 
					totalScore, 
					{
						xtype : 'compositefield',
						msgTarget : 'side',
						anchor : '-20',
						defaults : {
							flex : 1
						},
						items : [masteryScore, new Ext.form.Label({text : '%'})]
					}
				]
			}]
		});
		var updateWin = new Ext.Window({
		    layout:'fit',
		    x:0,
		    y:0,
		    width:document.body.clientWidth-50,
		    autoHeight : true,
		    autoScroll : false,
		    title : '修改作业',
		    closeAction:'close',
		    draggable : false,
			resizable : false,
			modal : true,
			plain : true,
			items : [updateForm],
			buttons : [{
				text : '保存',
				handler : function() {
					if (!updateForm.getForm().isValid()) {
						showInfo("数据校验未通过，请检查有效性");
						return;
					}
					mask.show();
					updateForm.getForm().submit({
						clientValidation : true,
						params : {
							testId : record.get("testId")
						},
						url : 'test_updateTest.do',
						success : function(form, action) {
							mask.hide();
							showInfo("保存成功");
							store.reload();
							updateWin.close();
						},
						failure : function(form, action) {
							showInfo(action.result.info);
						}
					});
				}
			}, {
				text : '取消',
				handler : function() {
					updateWin.close();
				}
			}]
		});
		updateWin.show();
		var mask = new Ext.LoadMask(updateWin.body, {
			msg : '程序运行中....'
		});
		mask.show();
		schemaId.getStore().load({
			callback : function(r, callOptions, successFlag) {
				mask.hide();
				schemaId.setValue(record.get("schemaId"));
			}
		});
	}
});


