/**
 * 新增讨论
 * messageforum
 * @param {}
 *            node
 * @param {}
 *            courseId
 */
function addForumMessageForum(node,wintitle) {
	scrollToTop();
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
	
	
	
	var bbsStore = new Ext.data.SimpleStore({
				fields : ['value', 'text'],
				proxy : new Ext.data.HttpProxy({
							url : 'courseSpace_loadAreaBox.do'//加载论坛列表
						})
			});
	var areaId = new Ext.form.ComboBox({
				editable : false,
				name : 'areaId',
				store : bbsStore,
				fieldLabel : '<font style=color:red>*</font>论坛列表',
				hiddenName : 'areaId',
				emptyText : '-',
				mode : 'local',
				loadingText : '加载中...',
				triggerAction : 'all',
				valueField : 'value',
				displayField : 'text',
				width : 50,
				anchor : '95%',
				selectOnFocus : true,
				allowBlank : false
			});
	bbsStore.load({
				callback : function(r, callOptions, successFlag) {
					mask.hide();
				}
			});

	var forumstore = new Ext.data.SimpleStore({
				fields : ['value', 'text'],
				proxy : new Ext.data.HttpProxy({
							url : 'courseSpace_loadForumBox.do'//加载论坛主题列表
						})
			});
	var forumId = new Ext.form.ComboBox({
				editable : false,
				name : 'forumId',
				store : forumstore,
				fieldLabel : '<font style=color:red>*</font>论坛版块',
				hiddenName : 'forumId',
				emptyText : '-',
				mode : 'local',
				loadingText : '加载中...',
				triggerAction : 'all',
				valueField : 'value',
				displayField : 'text',
				width : 50,
				anchor : '95%',
				selectOnFocus : true,
				allowBlank : false
			});
	
	var topicstore = new Ext.data.SimpleStore({
		fields : ['value', 'text'],
		proxy : new Ext.data.HttpProxy({
					url : 'courseSpace_loadTopicBox.do'//加载论坛帖子列表
				})
	});
	var topicId = new Ext.form.ComboBox({
		editable : false,
		name : 'topicId',
		store : topicstore,
		fieldLabel : '<font style=color:red>*</font>论坛主题',
		hiddenName : 'topicId',
		emptyText : '-',
		mode : 'local',
		loadingText : '加载中...',
		triggerAction : 'all',
		valueField : 'value',
		displayField : 'text',
		width : 50,
		anchor : '95%',
		selectOnFocus : true,
		allowBlank : false
	});


	areaId.on('select', function(combo, record, index) { // Ext.form.ComboBox

				forumId.clearValue();
				topicId.clearValue();
				
				forumstore.load({
							params : {
								areaId : record.data.value
							}
						});

			});
	
	forumId.on('select', function(combo, record, index) { // Ext.form.ComboBox

		topicId.clearValue();
		
		topicstore.load({
					params : {
						forumId : record.data.value
					}
				});

	});

	var addForm = new Ext.form.FormPanel({
				labelAlign : 'right',
				title : '新增讨论',
				labelWidth : 110,
				labelSeparator : '：',
				autoHeight : true,
				anchor : '95%',
				lines : false,
				autoScroll : false,
				items : [{
							layout : 'form',
							items : [isCaculateScore, areaId, forumId, topicId]
						}]
			});
	var forumWin = new Ext.Window({
		layout : 'fit',
		x : 0,
		y : 0,
		width : document.body.clientWidth-50,
		autoHeight : true,
		autoScroll : false,
		title : wintitle+'添加讨论',
		closeAction : 'close',
		draggable : false,
		resizable : false,
		modal : true,
		plain : true,
		items : [addForm],
		buttons : [{
					text : '保存',
					handler : function() {
						if (!addForm.getForm().isValid()) {
							showInfo("数据校验未通过，请检查有效性",forumWin);
							return;
						}
						mask.show();
						addForm.getForm().submit({
									clientValidation : true,
									params : {
										node : node.attributes.id,
										nodeType : node.attributes.type,
										name : topicId.getRawValue()
									},
									url : 'courseSpace_addForum.do',//添加讨论
									success : function(form, action) {
										mask.hide();
										node.parentNode.reload();
										if (form.getValues().isCaculateScore == "1") {// 需要计算平时成绩
											courseNode.attributes.scoreNum = Number(courseNode.attributes.scoreNum) + 1;
											showQuestions("保存成功，是否开始设置分数百分比",forumWin, function(btn) {
														if (btn == 'ok') {// 确认继续操作
															setPercent();
														}
													});
										} else {
											showInfo("保存成功",forumWin);
										}
										forumWin.close();
									},
									failure : function(form, action) {
										showInfo(action.result.info,forumWin);
									}
								});
					}
				}, {
					text : '取消',
					handler : function() {
						forumWin.close();
					}
				}]
	});
	forumWin.show();
	var mask = new Ext.LoadMask(forumWin.body, {
		msg : '程序运行中....'
	});
	mask.show();
}
 

/**
 * 修改讨论
 * messageforum
 * @param {}
 *            node
 * @param {}
 *            courseId
 */
function updateForumMessageForum(node,wintitle) {
	scrollToTop();
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
	var bbsStore = new Ext.data.SimpleStore({
				fields : ['value', 'text'],
				proxy : new Ext.data.HttpProxy({
							url : 'courseSpace_loadAreaBox.do'//加载论坛列表
						})
			});
	var areaId = new Ext.form.ComboBox({
				editable : false,
				name : 'areaId',
				store : bbsStore,
				fieldLabel : '<font style=color:red>*</font>论坛列表',
				hiddenName : 'areaId',
				emptyText : '-',
				mode : 'local',
				loadingText : '加载中...',
				triggerAction : 'all',
				valueField : 'value',
				displayField : 'text',
				width : 50,
				anchor : '95%',
				selectOnFocus : true,
				allowBlank : false
			});
	areaId.on('select', function(combo, record, index) { // Ext.form.ComboBox
				// 重新加载论坛版块
				forumId.clearValue();
				topicId.clearValue();
				forumstore.load({
							params : {
								'areaId' : record.data.value
							}
						});
			});

	var forumstore = new Ext.data.SimpleStore({
				fields : ['value', 'text'],
				proxy : new Ext.data.HttpProxy({
							url : 'courseSpace_loadForumBox.do'//加载论坛版块 列表
						})
			});
	var forumId = new Ext.form.ComboBox({
				editable : false,
				name : 'forumId',
				store : forumstore,
				fieldLabel : '<font style=color:red>*</font>论坛版块',
				hiddenName : 'forumId',
				emptyText : '-',
				mode : 'local',
				loadingText : '加载中...',
				triggerAction : 'all',
				valueField : 'value',
				displayField : 'text',
				width : 50,
				anchor : '95%',
				selectOnFocus : true,
				allowBlank : false
			});
	forumId.on('select', function(combo, record, index) { // Ext.form.ComboBox
		// 重新加载论坛主题
		topicId.clearValue();
		topicstore.load({
					params : {
						'forumId' : record.data.value
					}
				});
	});
	
	var topicstore = new Ext.data.SimpleStore({
		fields : ['value', 'text'],
		proxy : new Ext.data.HttpProxy({
					url : 'courseSpace_loadTopicBox.do'//加载论坛主题列表
				})
	});
	var topicId = new Ext.form.ComboBox({
		editable : false,
		name : 'topicId',
		store : topicstore,
		fieldLabel : '<font style=color:red>*</font>论坛主题',
		hiddenName : 'topicId',
		emptyText : '-',
		mode : 'local',
		loadingText : '加载中...',
		triggerAction : 'all',
		valueField : 'value',
		displayField : 'text',
		width : 50,
		anchor : '95%',
		selectOnFocus : true,
		allowBlank : false
	});

	
	var addForm = new Ext.form.FormPanel({
				labelAlign : 'right',
				title : '修改讨论',
				labelWidth : 110,
				labelSeparator : '：',
				autoHeight : true,
				anchor : '95%',
				lines : false,
				autoScroll : false,
				items : [{
							layout : 'form',
							items : [isCaculateScore, areaId, forumId, topicId]
						}]
			});
	var forumWin = new Ext.Window({
		layout : 'fit',
		x : 0,
		y : 0,
		width : document.body.clientWidth-50,
		autoHeight : true,
		autoScroll : false,
		title : wintitle+'修改讨论',
		closeAction : 'close',
		draggable : false,
		resizable : false,
		modal : true,
		plain : true,
		items : [addForm],
		buttons : [{
					text : '保存',
					handler : function() {
						if (!addForm.getForm().isValid()) {
							showInfo("数据校验未通过，请检查有效性",forumWin);
							return;
						}
						mask.show();
						addForm.getForm().submit({
									clientValidation : true,
									params : {
										node : node.attributes.id,
										name : topicId.getRawValue()
									},
									url : 'courseSpace_updateForum.do',//修改讨论
									success : function(form, action) {
										mask.hide();
										if (initIsCaculateScore == "1" && form.getValues().isCaculateScore == "2") {// 是改成否
											courseNode.attributes.scoreNum = Number(courseNode.attributes.scoreNum) - 1;
										} else if (initIsCaculateScore == "2" && form.getValues().isCaculateScore == "1") {// 否改成是
											courseNode.attributes.scoreNum = Number(courseNode.attributes.scoreNum) + 1;
										}
										if (form.getValues().isCaculateScore == "1") {// 需要计算平时成绩
											showQuestions("保存成功，是否开始设置分数百分比",forumWin, function(btn) {
														if (btn == 'ok') {// 确认继续操作
															setPercent();
														}
													});
										} else {
											showInfo("保存成功",forumWin);
										}
										node.parentNode.parentNode.reload();
										forumWin.close();
									},
									failure : function(form, action) {
										showInfo(action.result.info,forumWin);
									}
								});
					}
				}, {
					text : '取消',
					handler : function() {
						forumWin.close();
					}
				}]
	});
	forumWin.show();
	
	var mask = new Ext.LoadMask(forumWin.body, {
		msg : '程序运行中....'
	});
	mask.show();
	
	bbsStore.load({
		callback : function(r, callOptions, successFlag) {
			areaId.setValue(node.attributes.areaId);
			
			forumstore.load({
				params : {
					'areaId' : node.attributes.areaId
				},
				callback : function(r, callOptions, successFlag) {
					forumId.setValue(node.attributes.forumId);
					
					topicstore.load({
						params : {
							'forumId' : node.attributes.forumId
						},
						callback : function(r, callOptions, successFlag) {
							topicId.setValue(node.attributes.topicId);
							mask.hide();
						}
					});
					
				}
			});		
			
			
			
		}
	});

}







/**
 * 新增讨论
 * jforum
 * @param {}
 *            node
 * @param {}
 *            courseId
 */
function addForum(node,wintitle) {
	scrollToTop();
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
	var bbsStore = new Ext.data.SimpleStore({
				fields : ['value', 'text'],
				proxy : new Ext.data.HttpProxy({
							url : 'courseSpace_loadForumBox.do'//加载论坛列表
						})
			});
	var forumId = new Ext.form.ComboBox({
				editable : false,
				name : 'forumId',
				store : bbsStore,
				fieldLabel : '<font style=color:red>*</font>论坛列表',
				hiddenName : 'forumId',
				emptyText : '-',
				mode : 'local',
				loadingText : '加载中...',
				triggerAction : 'all',
				valueField : 'value',
				displayField : 'text',
				width : 50,
				anchor : '95%',
				selectOnFocus : true,
				allowBlank : false
			});
	bbsStore.load({
				callback : function(r, callOptions, successFlag) {
					mask.hide();
				}
			});

	var topicstore = new Ext.data.SimpleStore({
				fields : ['value', 'text'],
				proxy : new Ext.data.HttpProxy({
							url : 'courseSpace_loadTopicBox.do'//加载论坛主题列表
						})
			});
	var topicId = new Ext.form.ComboBox({
				editable : false,
				name : 'topicId',
				store : topicstore,
				fieldLabel : '<font style=color:red>*</font>论坛主题',
				hiddenName : 'topicId',
				emptyText : '-',
				mode : 'local',
				loadingText : '加载中...',
				triggerAction : 'all',
				valueField : 'value',
				displayField : 'text',
				width : 50,
				anchor : '95%',
				selectOnFocus : true,
				allowBlank : false
			});

	forumId.on('select', function(combo, record, index) { // Ext.form.ComboBox

				topicId.clearValue();
				topicstore.load({
							params : {
								forumId : record.data.value
							}
						});

			});

	var addForm = new Ext.form.FormPanel({
				labelAlign : 'right',
				title : '新增讨论',
				labelWidth : 110,
				labelSeparator : '：',
				autoHeight : true,
				anchor : '95%',
				lines : false,
				autoScroll : false,
				items : [{
							layout : 'form',
							items : [isCaculateScore, forumId, topicId]
						}]
			});
	var forumWin = new Ext.Window({
		layout : 'fit',
		x : 200,
		y : 50,
		width : 600,
		autoHeight : true,
		autoScroll : false,
		title : wintitle+'添加讨论',
		closeAction : 'close',
		draggable : true,
		resizable : true,
		modal : true,
		plain : true,
		items : [addForm],
		buttons : [{
					text : '保存',
					handler : function() {
						if (!addForm.getForm().isValid()) {
							showInfo("数据校验未通过，请检查有效性",forumWin);
							return;
						}
						mask.show();
						addForm.getForm().submit({
									clientValidation : true,
									params : {
										node : node.attributes.id,
										nodeType : node.attributes.type,
										name : topicId.getRawValue()
									},
									url : 'courseSpace_addForum.do',//添加讨论
									success : function(form, action) {
										mask.hide();
										node.parentNode.reload();
										if (form.getValues().isCaculateScore == "1") {// 需要计算平时成绩
											courseNode.attributes.scoreNum = Number(courseNode.attributes.scoreNum) + 1;
											showQuestions("保存成功，是否开始设置分数百分比",forumWin, function(btn) {
														if (btn == 'ok') {// 确认继续操作
															setPercent();
														}
													});
										} else {
											showInfo("保存成功",forumWin);
										}
										forumWin.close();
									},
									failure : function(form, action) {
										showInfo(action.result.info,forumWin);
									}
								});
					}
				}, {
					text : '取消',
					handler : function() {
						forumWin.close();
					}
				}]
	});
	forumWin.show();
	var mask = new Ext.LoadMask(forumWin.body, {
		msg : '程序运行中....'
	});
	mask.show();
}


/**
 * 修改讨论
 *  jforum
 * @param {}
 *            node
 * @param {}
 *            courseId
 */
function updateForum(node,wintitle) {
	scrollToTop();
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
	var bbsStore = new Ext.data.SimpleStore({
				fields : ['value', 'text'],
				proxy : new Ext.data.HttpProxy({
							url : 'courseSpace_loadForumBox.do'//加载论坛列表
						})
			});
	var forumId = new Ext.form.ComboBox({
				editable : false,
				name : 'forumId',
				store : bbsStore,
				fieldLabel : '<font style=color:red>*</font>论坛列表',
				hiddenName : 'forumId',
				emptyText : '-',
				mode : 'local',
				loadingText : '加载中...',
				triggerAction : 'all',
				valueField : 'value',
				displayField : 'text',
				width : 50,
				anchor : '95%',
				selectOnFocus : true,
				allowBlank : false
			});
	forumId.on('select', function(combo, record, index) { // Ext.form.ComboBox
				// 重新加载论坛主题
				topicId.clearValue();
				topicstore.load({
							params : {
								'forumId' : record.data.value
							}
						});
			});

	var topicstore = new Ext.data.SimpleStore({
				fields : ['value', 'text'],
				proxy : new Ext.data.HttpProxy({
							url : 'courseSpace_loadTopicBox.do'//加载论坛主题列表
						})
			});
	var topicId = new Ext.form.ComboBox({
				editable : false,
				name : 'topicId',
				store : topicstore,
				fieldLabel : '<font style=color:red>*</font>论坛主题',
				hiddenName : 'topicId',
				emptyText : '-',
				mode : 'local',
				loadingText : '加载中...',
				triggerAction : 'all',
				valueField : 'value',
				displayField : 'text',
				width : 50,
				anchor : '95%',
				selectOnFocus : true,
				allowBlank : false
			});
	
	var addForm = new Ext.form.FormPanel({
				labelAlign : 'right',
				title : '修改讨论',
				labelWidth : 110,
				labelSeparator : '：',
				autoHeight : true,
				anchor : '95%',
				lines : false,
				autoScroll : false,
				items : [{
							layout : 'form',
							items : [isCaculateScore, forumId, topicId]
						}]
			});
	var forumWin = new Ext.Window({
		layout : 'fit',
		x : 200,
		y : 50,
		width : 600,
		autoHeight : true,
		autoScroll : false,
		title : wintitle+'修改讨论',
		closeAction : 'close',
		draggable : true,
		resizable : true,
		modal : true,
		plain : true,
		items : [addForm],
		buttons : [{
					text : '保存',
					handler : function() {
						if (!addForm.getForm().isValid()) {
							showInfo("数据校验未通过，请检查有效性",forumWin);
							return;
						}
						mask.show();
						addForm.getForm().submit({
									clientValidation : true,
									params : {
										node : node.attributes.id,
										name : topicId.getRawValue()
									},
									url : 'courseSpace_updateForum.do',//修改讨论
									success : function(form, action) {
										mask.hide();
										if (initIsCaculateScore == "1" && form.getValues().isCaculateScore == "2") {// 是改成否
											courseNode.attributes.scoreNum = Number(courseNode.attributes.scoreNum) - 1;
										} else if (initIsCaculateScore == "2" && form.getValues().isCaculateScore == "1") {// 否改成是
											courseNode.attributes.scoreNum = Number(courseNode.attributes.scoreNum) + 1;
										}
										if (form.getValues().isCaculateScore == "1") {// 需要计算平时成绩
											showQuestions("保存成功，是否开始设置分数百分比",forumWin, function(btn) {
														if (btn == 'ok') {// 确认继续操作
															setPercent();
														}
													});
										} else {
											showInfo("保存成功",forumWin);
										}
										node.parentNode.parentNode.reload();
										forumWin.close();
									},
									failure : function(form, action) {
										showInfo(action.result.info,forumWin);
									}
								});
					}
				}, {
					text : '取消',
					handler : function() {
						forumWin.close();
					}
				}]
	});
	forumWin.show();
	
	var mask = new Ext.LoadMask(forumWin.body, {
		msg : '程序运行中....'
	});
	mask.show();
	
	bbsStore.load({
		callback : function(r, callOptions, successFlag) {
			forumId.setValue(node.attributes.forumId);
			
			topicstore.load({
				params : {
					'forumId' : node.attributes.forumId
				},
				callback : function(r, callOptions, successFlag) {
					topicId.setValue(node.attributes.topicId);
					mask.hide();
				}
			});		
			
			
			
		}
	});

}