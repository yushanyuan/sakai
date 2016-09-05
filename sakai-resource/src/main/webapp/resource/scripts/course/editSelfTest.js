/**
 * 添加前测
 * 
 * @param {}
 *            node 模块或者页节点
 * @param {}
 *            courseId 课程id
 */
function addSelfTest(node,wintitle) {
	scrollToTop();
	var name = new Ext.form.TextField({
				name : 'name',
				fieldLabel : '<font style=color:red>*</font>前测名称',
				anchor : '99%',
				allowBlank : false
			});
	var isCaculateScore = new Ext.form.RadioGroup({
				name : 'isCaculateScore',
				fieldLabel : '<font style=color:red>*</font>是否计算平时成绩',
				anchor : '99%',
				columns : [80, 80],
				vertical : true,
				allowBlank : false,
				items : new Ext.ux.RadioGroupData({
							baseData : isCaculateScoreArray,
							defaultValue : '2',
							name : 'isCaculateScore'
						})
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
	var exStore = new Ext.data.SimpleStore({
				fields : ['value', 'text', 'score'],
				baseParams : {
					exCourseid : courseNode.attributes.exCourseid
				},
				proxy : new Ext.data.HttpProxy({
							url : 'courseSpace_loadSchemaBox.do'//获取策略列表
						})
			});
	var schemaId = new Ext.form.ComboBox({
				editable : false,
				name : 'schemaId',
				store : exStore,
				fieldLabel : '<font style=color:red>*</font>策略列表',
				hiddenName : 'schemaId',
				emptyText : '-',
				mode : 'local',
				loadingText : '加载中...',
				triggerAction : 'all',
				valueField : 'value',
				displayField : 'text',
				anchor : '99%',
				selectOnFocus : true,
				allowBlank : false
			});
	exStore.load({
				callback : function(r, callOptions, successFlag) {
					mask.hide();
				}
			});
	schemaId.on('select', function(combo, record, index) { // Ext.form.ComboBox
				totalScore.setValue(record.get("score"));
			});
	var checkButton = new Ext.Button({
				text : '预览策略',
				width : 60,
				handler : function() {
					var schemaValue = schemaId.getValue();
					if (schemaValue == "") {
						showInfo("请选择一个策略",testWin);
						return;
					}
					var schemaurl = examSysSchemaUrl + schemaValue;
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
				title : '新增前测',
				labelWidth : 170,
				labelSeparator : '：',
				autoHeight : true,
				anchor : '95%',
				lines : false,
				autoScroll : false,
				items : [{
							layout : 'form',
							items : [name, isCaculateScore, samepaper, {
										xtype : 'compositefield',
										fieldLabel : '<font style=color:red>*</font>策略列表',
										msgTarget : 'side',
										anchor : '-20',
										defaults : {
											flex : 1
										},
										items : [schemaId, checkButton]
									}, totalScore, {
										xtype : 'compositefield',
										msgTarget : 'side',
										anchor : '-20',
										defaults : {
											flex : 1
										},
										items : [masteryScore, new Ext.form.Label({
															text : '%'
														})]
									}]
						}]
			});
	var testWin = new Ext.Window({
	    layout:'fit',
	    x:200,
	    y:50,
	    width:600,
	    autoHeight : true,
		autoScroll : false,
	    title : wintitle+'添加前测',
	    closeAction:'close',
	    draggable : true,
		resizable : true,
		modal : true,
		plain : true,
		items : [addForm],
		buttons : [{
					text : '保存',
					handler : function() {
						if (!addForm.getForm().isValid()) {
							showInfo("数据校验未通过，请检查有效性",testWin);
							return;
						}
						mask.show();
						addForm.getForm().submit({
									clientValidation : true,
									params : {
										node : node.attributes.id,
										nodeType : node.attributes.type,
										courseId : courseNode.id
									},
									url : 'courseSpace_addSelfTest.do',//添加前测
									success : function(form, action) {
										mask.hide();
										if (action.result.info != null && action.result.info != "") {
											showWarn(action.result.info);
											return;
										}
										//生成前测试卷库
										var id = action.result.selfTestId;
										Ext.Ajax.request({
											url: 'courseSpace_publishSelfTest.do',//发布前测作业
											params: {
												courseId : courseNode.id,
												testId : id,
												schemaId : form.getValues().schemaId
											},
										   	success: function(response ,options ){}
										});
										
										node.parentNode.reload();
										if (form.getValues().isCaculateScore == "1") {// 需要计算平时成绩
											// 课程计分活动总数+1
											courseNode.attributes.scoreNum = Number(courseNode.attributes.scoreNum) + 1;
											showQuestions("保存成功，是否开始设置分数百分比",testWin, function(btn) {
														if (btn == 'ok') {// 确认继续操作
															setPercent();
														}
													});
										} else {
											showInfo("保存成功",testWin);
										}
										testWin.close();
									},
									failure : function(form, action) {
										showInfo(action.result.info,testWin);
									}
								});
					}
				}, {
					text : '取消',
					handler : function() {
						testWin.close();
					}
				}]
	});
	testWin.show();
	var mask = new Ext.LoadMask(testWin.body, {
		msg : '程序运行中....'
	});
	mask.show();
}
/**
 * 修改前测
 * 
 * @param {}
 *            node 前测节点
 * @param {}
 *            courseId 课程id
 */
function updateSelfTest(node,wintitle) {
	scrollToTop();
	var name = new Ext.form.TextField({
				name : 'name',
				value : node.attributes.name,
				fieldLabel : '<font style=color:red>*</font>前测名称',
				anchor : '99%',
				allowBlank : false
			});
	var initIsCaculateScore = node.attributes.isCaculateScore;
	var isCaculateScore = new Ext.form.RadioGroup({
				name : 'isCaculateScore',
				value : node.attributes.isCaculateScore,
				fieldLabel : '<font style=color:red>*</font>是否计算平时成绩',
				anchor : '99%',
				columns : [80, 80],
				vertical : true,
				allowBlank : false,
				items : new Ext.ux.RadioGroupData({
							baseData : isCaculateScoreArray,
							name : 'isCaculateScore'
						})
			});
	var exStore = new Ext.data.SimpleStore({
				fields : ['value', 'text', 'score'],
				baseParams : {
					exCourseid : courseNode.attributes.exCourseid
				},
				proxy : new Ext.data.HttpProxy({
							url : 'courseSpace_loadSchemaBox.do'
						})
			});
	var samepaper = new Ext.form.RadioGroup({
				name : 'samepaper',
				value : node.attributes.samepaper,
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
				store : exStore,
				fieldLabel : '<font style=color:red>*</font>策略列表',
				hiddenName : 'schemaId',
				emptyText : '-',
				mode : 'local',
				loadingText : '加载中...',
				triggerAction : 'all',
				valueField : 'value',
				displayField : 'text',
				anchor : '99%',
				selectOnFocus : true,
				allowBlank : false
			});
	exStore.load({
				callback : function(r, callOptions, successFlag) {
					mask.hide();
					schemaId.setValue(node.attributes.schemaId);
				}
			});
	schemaId.on('select', function(combo, record, index) { // Ext.form.ComboBox
				totalScore.setValue(record.get("score"));
			});
	var checkButton = new Ext.Button({
				text : '预览策略',
				width : 100,
				handler : function() {
					var schemaValue = schemaId.getValue();
					if (schemaValue == "") {
						showInfo("请选择一个策略",testWin);
						return;
					}
					var schemaurl = examSysSchemaUrl + schemaValue;
					window.open(schemaurl);
				}
			})
	var totalScore = new Ext.form.TextField({
				name : 'totalScore',
				value : node.attributes.totalScore,
				fieldLabel : '总分',
				anchor : '99%',
				readOnly : true
			});
	var masteryScore = new Ext.form.NumberField({
				name : 'masteryScore',
				value : node.attributes.masteryScore,
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
	var updateForm = new Ext.form.FormPanel({
				labelAlign : 'right',
				title : '修改前测',
				labelWidth : 170,
				labelSeparator : '：',
				autoHeight : true,
				anchor : '95%',
				lines : false,
				autoScroll : false,
				items : [{
							layout : 'form',
							items : [name, isCaculateScore, samepaper, {
										xtype : 'compositefield',
										fieldLabel : '<font style=color:red>*</font>策略列表',
										msgTarget : 'side',
										anchor : '-20',
										defaults : {
											flex : 1
										},
										items : [schemaId, checkButton]
									}, totalScore, {
										xtype : 'compositefield',
										msgTarget : 'side',
										anchor : '-20',
										defaults : {
											flex : 1
										},
										items : [masteryScore, new Ext.form.Label({
															text : '%'
														})]
									}]
						}]
			});
	var testWin = new Ext.Window({
	    layout:'fit',
	    x:200,
	    y:50,
	    width:600,
	    autoHeight : true,
		autoScroll : false,
	    title : wintitle+'修改前测',
	    closeAction:'close',
	    draggable : true,
		resizable : true,
		modal : true,
		plain : true,
		items : [updateForm],
		buttons : [{
					text : '保存',
					handler : function() {
						if (!updateForm.getForm().isValid()) {
							showInfo("数据校验未通过，请检查有效性",testWin);
							return;
						}
						mask.show();
						updateForm.getForm().submit({
									clientValidation : true,
									params : {
										node : node.attributes.id
									},
									url : 'courseSpace_updateSelfTest.do',
									success : function(form, action) {
										mask.hide();
										if (action.result.info != null && action.result.info != "") {
											showWarn(action.result.info);
											return;
										}
										if (initIsCaculateScore == "1" && form.getValues().isCaculateScore == "2") {// 是改成否
											// 计分活动总数-1
											courseNode.attributes.scoreNum = Number(courseNode.attributes.scoreNum) - 1;
										} else if (initIsCaculateScore == "2" && form.getValues().isCaculateScore == "1") {// 否改成是
											// 计分活动总数+1
											courseNode.attributes.scoreNum = Number(courseNode.attributes.scoreNum) + 1;
										}
										if (form.getValues().isCaculateScore == "1") {// 需要计算平时成绩
											showQuestions("保存成功，是否开始设置分数百分比",testWin, function(btn) {
														if (btn == 'ok') {// 确认继续操作
															setPercent();
														}
													});
										} else {
											showInfo("保存成功",testWin);
										}
										node.parentNode.parentNode.reload();
										testWin.close();
									},
									failure : function(form, action) {
										showInfo(action.result.info,testWin);
									}
								});
					}
				}, {
					text : '取消',
					handler : function() {
						testWin.close();
					}
				}]
	});
	testWin.show();
	var mask = new Ext.LoadMask(testWin.body, {
		msg : '程序运行中....'
	});
	mask.show();
	
}