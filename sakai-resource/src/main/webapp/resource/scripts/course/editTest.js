/**
 * 添加作业
 * 
 * @param {}
 *            node 模块或者页节点
 * @param {}
 *            courseId 课程id
 */
function addTest(node,wintitle) {
	if(!courseNode.attributes.exCourseid || courseNode.attributes.exCourseid ==""){
		alert("请在‘课程设置’功能中指定‘题库课程’");
	}

	scrollToTop();
	var name = new Ext.form.TextField({
				name : 'name',
				fieldLabel : '<font style=color:red>*</font>作业名称',
				width:200,
				allowBlank : false
			});
	var startOpenDate = new Cls.form.DateTimeField({
		fieldLabel : '作业开放开始时间',
		id : 'startOpenDate',
		name : 'startOpenDate',
		format : 'Y-m-d H:i:s',
		editable : true,
		width:200,
		allowBlank : true,
		listeners : {//监听
			'select' : function(endDateDf, date) {
			}
		}
	});
	var endOpenDate = new Cls.form.DateTimeField({
		fieldLabel : '作业开放开始时间',
		id : 'endOpenDate',
		name : 'endOpenDate',
		format : 'Y-m-d H:i:s',
		editable : true,
		width:200,
		allowBlank : true,
		listeners : {//监听
			'select' : function(endDateDf, date) {
			}
		}
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
							defaultValue : '1',
							name : 'isCaculateScore'
						})
			});
	var buildType = new Ext.form.RadioGroup({
		name : 'buildType',
		fieldLabel : '<font style=color:red>*</font>组卷方式',
		anchor : '99%',
		columns : [80, 80],
		vertical : true,
		allowBlank : false,
		items : new Ext.ux.RadioGroupData({
					baseData : buildTypeArray,
					defaultValue : '1',
					name : 'buildType'
				})
	});
	var buildNum = new Ext.form.TextField({
		name : 'buildNum',
		value:operateCount,
		fieldLabel : '<font style=color:red>*</font>试卷生成份数',
		width:80,
		validator:function(value) {
			if(/^\d+$/.test(value) && value <= operateCount){
				return true;
			}else{
				return "试卷生成份数必须是数字，且不能大于"+operateCount;
			}
		},
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

	// 作业考试系统课程列表
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
				hiddenName : 'schemaId',
				emptyText : '-',
				mode : 'local',
				loadingText : '加载中...',
				triggerAction : 'all',
				valueField : 'value',
				displayField : 'text',
				anchor : '50%',
				selectOnFocus : true,
				allowBlank : false
			});
	exStore.load({
				callback : function(r, callOptions, successFlag) {
					mask.hide();
				}
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
					var schemaurl = examSysSchemaUrl.replace("${schemaId}",schemaValue);
					window.open(schemaurl);
				}
			})
	var goExamButton = new Ext.Button({
		text : '进入题库',
		width : 60,
		handler : function() {
			window.open(examSysEntryUrl);
		}
	})	

	schemaId.on('select', function(combo, record, index) { // Ext.form.ComboBox
				totalScore.setValue(record.get("score"));
			});
	var totalScore = new Ext.form.TextField({
				name : 'totalScore',
				fieldLabel : '总分',
				value : '0',
				width:100,
				readOnly : true
			});
	var masteryScore = new Ext.form.NumberField({
				name : 'masteryScore',
				fieldLabel : '<font style=color:red>*</font>建议通过百分比',
				emptyText : '0',
				width:100,
				allowNegative : false,// 不能输入负数
				allowDecimals : false, // 不能输入小数
				validator : function(value) {
					var total = totalScore.getValue();
					if (new Number(value) > 100) {
						return false;
					}
					return true;
				},
				invalidText : '建议通过百分比不能大于100'
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
							items : [name,startOpenDate,endOpenDate, isCaculateScore, samepaper, {
										xtype : 'compositefield',
										fieldLabel : '<font style=color:red>*</font>策略列表',
										msgTarget : 'side',
										anchor : '-20',
										defaults : {
											flex : 1
										},
										items : [schemaId, checkButton,goExamButton]
									},buildType,buildNum, totalScore, 
									{
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
	    t : 50,
	    x : 200,
	    width:600,
	    autoHeight : true,
	    autoScroll : false,
	    title : wintitle+'添加作业',
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
									url : 'courseSpace_addTest.do',//添加作业
									success : function(form, action) {
										mask.hide();
										if (action.result.info != null && action.result.info != "") {
											showWarn(action.result.info);
											return;
										}
										var id = action.result.testId;
										Ext.Ajax.request({
											url: 'courseSpace_publishTest.do',//发布作业
											params: {
												courseId : courseNode.id,
												testId : id,
												schemaId : form.getValues().schemaId
											},
										   	success: function(response ,options ){}
										});
										
										var parent = node.parentNode;
										parent.reload();
										if (form.getValues().isCaculateScore == "1") {// 需要计算平时成绩
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
 * 编辑作业
 * 
 * @param {}
 *            node 作业节点
 * @param {}
 *            courseId 课程id
 */
function updateTest(node,wintitle) {
	window.top.scrollTo(0, 0);
	var name = new Ext.form.TextField({
				name : 'name',
				value : node.attributes.name,
				fieldLabel : '<font style=color:red>*</font>作业名称',
				width:200,
				allowBlank : false
			});
	var startOpenDate = new Cls.form.DateTimeField({
		fieldLabel : '作业开放开始时间',
		id : 'startOpenDate',
		name : 'startOpenDate',
		format : 'Y-m-d H:i:s',
		editable : true,
		width:200,
		allowBlank : true,
		value : node.attributes.startOpenDate,
		listeners : {//监听
			'select' : function(endDateDf, date) {
			}
		}
	});
	var endOpenDate = new Cls.form.DateTimeField({
		fieldLabel : '作业开放开始时间',
		id : 'endOpenDate',
		name : 'endOpenDate',
		format : 'Y-m-d H:i:s',
		editable : true,
		width:200,
		allowBlank : true,
		value : node.attributes.endOpenDate,
		listeners : {//监听
			'select' : function(endDateDf, date) {
			}
		}
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
	var buildType = new Ext.form.RadioGroup({
		name : 'buildType',
		value : node.attributes.buildType,
		fieldLabel : '<font style=color:red>*</font>组卷方式',
		anchor : '99%',
		columns : [80, 80],
		vertical : true,
		allowBlank : false,
		items : new Ext.ux.RadioGroupData({
					baseData : buildTypeArray,
					name : 'buildType'
				})
	});
	var buildNum = new Ext.form.TextField({
		name : 'buildNum',
		value : node.attributes.buildNum,
		fieldLabel : '<font style=color:red>*</font>试卷生成份数',
		width:80,
		validator:function(value) {
			if(/^\d+$/.test(value) && value <= operateCount){
				return true;
			}else{
				return "试卷生成份数必须是数字，且不能大于"+operateCount;
			}
		},
		allowBlank : false
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
	var exStore = new Ext.data.SimpleStore({
				fields : ['value', 'text', 'score'],
				baseParams : {
					exCourseid : courseNode.attributes.exCourseid
				},
				proxy : new Ext.data.HttpProxy({
							url : 'courseSpace_loadSchemaBox.do'
						})
			});
	var schemaId = new Ext.form.ComboBox({
				editable : false,
				name : 'schemaId',
				store : exStore,
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
				width : 60,
				handler : function() {
					var schemaValue = schemaId.getValue();
					if (schemaValue == "") {
						showInfo("请选择一个策略",testWin);
						return;
					}
					var schemaurl = examSysSchemaUrl.replace("${schemaId}",schemaValue);
					window.open(schemaurl);
				}
			})
	var goExamButton = new Ext.Button({
		text : '进入题库',
		width : 110,
		handler : function() {
			window.open(examSysEntryUrl);
		}
	})	
	
	
	var totalScore = new Ext.form.TextField({
				name : 'totalScore',
				value : node.attributes.totalScore,
				fieldLabel : '总分',
				width:100,
				readOnly : true
			});
	var masteryScore = new Ext.form.NumberField({
				name : 'masteryScore',
				value : node.attributes.masteryScore,
				fieldLabel : '<font style=color:red>*</font>建议通过百分比',
				emptyText : '0',
				width:100,
				allowNegative : false,// 不能输入负数
				allowDecimals : false, // 不能输入小数
				validator : function(value) {
					var total = totalScore.getValue();
					if (new Number(value) > 100) {
						return false;
					}
					return true;
				},
				invalidText : '建议通过百分比不能大于100'
			})
	var minTimeInterval = new Ext.form.TextField({
				name : 'minTimeInterval',
				value : node.attributes.minTimeInterval,
				fieldLabel : '重做测试的最小间隔(分钟)',
				width: 100
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
							items : [name,startOpenDate,endOpenDate, isCaculateScore, samepaper, {
										xtype : 'compositefield',
										fieldLabel : '<font style=color:red>*</font>策略列表',
										msgTarget : 'side',
										anchor : '-20',
										defaults : {
											flex : 1
										},
										items : [schemaId, checkButton,goExamButton]
									}, buildType,buildNum,totalScore, {
										xtype : 'compositefield',
										msgTarget : 'side',
										anchor : '-20',
										defaults : {
											flex : 1
										},
										items : [masteryScore, new Ext.form.Label({
															text : '%'
														})]
									}, minTimeInterval]
						}]
			});
	var testWin = new Ext.Window({
	    layout:'fit',
		y : 50,
		x : 200,
		width:600,
	    autoHeight : true,
	    autoScroll : false,
	    title : wintitle+'修改作业',
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
									url : 'courseSpace_updateTest.do',//修改作业
									success : function(form, action) {
										mask.hide();
										if (action.result.info != null && action.result.info != "") {
											showWarn(action.result.msg);
											return;
										}
										if (initIsCaculateScore == "1" && form.getValues().isCaculateScore == "2") {// 是改成否
											courseNode.attributes.scoreNum = Number(courseNode.attributes.scoreNum) - 1;
										} else if (initIsCaculateScore == "2" && form.getValues().isCaculateScore == "1") {// 否改成是
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