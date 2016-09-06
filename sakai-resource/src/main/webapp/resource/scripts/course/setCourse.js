function setCourse(node) {
	scrollToTop();
	var titleEdit = new Ext.form.TextField({
				name : 'title',
				value : node.attributes.title,
				fieldLabel : '<font style=color:red>*</font>课程名',
				plugins : [new Ext.ux.TextFieldReadOnlyStyle()],
				anchor : '95%',
				readOnly : true
			});
	//var jwStore = new Ext.data.SimpleStore({
	//			fields : ['value', 'text'],
	//			proxy : new Ext.data.HttpProxy({
	//						url : 'courseSpace_loadJwCourseBox.do'//加载教务系统课程列表
	//					})
	//		});
	//var jwCourseidEdit = new Ext.form.ComboBox({
	//			editable : false,
	//			name : 'jwCourseid',
	//			store : jwStore,
	//			fieldLabel : '<font style=color:red>*</font>教务系统课程名',
	//			hiddenName : 'jwCourseid',
	//			emptyText : '-',
	//			mode : 'local',
	//			loadingText : '加载中...',
	//			triggerAction : 'all',
	//			valueField : 'value',
	//			displayField : 'text',
	//			width : 50,
	//			anchor : '95%',
	//			selectOnFocus : true,
	//			allowBlank : false
	//		});

	var columnArr = [titleEdit /**,jwCourseidEdit,*/];
	//使用题库则加载题库课程
	var exStore;
	var exCourseidEdit;
	if(useExamSys){
		exStore = new Ext.data.SimpleStore({
			fields : ['value', 'text'],
			proxy : new Ext.data.HttpProxy({
						url : 'courseSpace_loadExamCourseBox.do'//加载作业系统课程列表
					})
		});
		exCourseidEdit = new Ext.form.ComboBox({
			//editable : false,
			name : 'exCourseid',
			value : node.attributes.exCourseid,
			store : exStore,
			fieldLabel : '题库对应课程',
			hiddenName : 'exCourseid',
			emptyText : '-',
			mode : 'local',
			loadingText : '加载中...',
			triggerAction : 'all',
			valueField : 'value',
			displayField : 'text',
			width : 50,
			anchor : '95%',
			selectOnFocus : true,
			allowBlank : true,
			typeAhead: true
		});
		columnArr.push(exCourseidEdit);
	}

	var resStore;
	var resCourseidEdit;
	if(useResSys){
		resStore = new Ext.data.SimpleStore({
			fields : ['value', 'text'],
			proxy : new Ext.data.HttpProxy({
						url : 'courseSpace_loadResCourseBox.do'//加载资源系统课程列表
					})
		});
		resCourseidEdit = new Ext.form.ComboBox({
			name : 'resCourseid',
			value : node.attributes.resCourseid,
			store : resStore,
			fieldLabel : '资源系统课程',
			hiddenName : 'resCourseid',
			emptyText : '-',
			mode : 'local',
			loadingText : '加载中...',
			triggerAction : 'all',
			valueField : 'value',
			displayField : 'text',
			width : 50,
			anchor : '95%',
			selectOnFocus : true,
			allowBlank : true,
			typeAhead: true
		});
		columnArr.push(resCourseidEdit);
	}

	var courseIdHide = new Ext.form.Hidden({
		name : 'courseId',
		value : node.attributes.id
	});	
	columnArr.push(courseIdHide);
	
	var leaf = node.attributes.leaf;
	//if (!leaf) {
		var playerTemplate = new Ext.form.ComboBox({
					editable : false,
					name : 'playerTemplate',
					value : node.attributes.playerTemplate,
					emptyText : '0',
					store : new Ext.data.SimpleStore({
								fields : ['value', 'text'],
								data : tplsJSON
							}),
					fieldLabel : '<font style=color:red>*</font>课程主题模板',
					hiddenName : 'playerTemplate',
					mode : 'local',
					loadingText : '加载中...',
					triggerAction : 'all',
					valueField : 'value',
					displayField : 'text',
					anchor : '95%',
					selectOnFocus : true,
					allowBlank : false
				});
		columnArr.push(playerTemplate);
	//}
	var formEdit = new Ext.form.FormPanel({
				region : 'center',
				labelAlign : 'right',
				labelWidth : 100,
				labelSeparator : '：',
				autoHeight : true,
				anchor : '100%',
				lines : false,
				autoScroll : true,
				items : [{
							layout : 'form',
							items : columnArr
						}],
				buttons : [{
							text : '保存',
							handler : function() {
								if (!formEdit.getForm().isValid()) {
									showInfo("数据校验未通过，请检查必填项");
									return;
								}
								mask.show();
								formEdit.getForm().submit({
									clientValidation : true,
									url : 'courseSpace_saveCourse.do',//设置课程信息
									success : function(form, action) {
										mask.hide();
										showInfo("保存成功",winEdit);// 显示执行成功提示
										//node.parentNode.reload();
										winEdit.close();
										window.location.reload();
									},
									failure : function(form, action) {

									}
								});
							}
						}, {
							text : '关闭',
							handler : function() {
								winEdit.close();
							}
						}]
			});

	var winEdit = new Ext.Window({
				title : '设置课程信息',
				y : 50,
				x : 200,
				width:500,
                height:200,
				items : [formEdit],
				closeAction : 'close',
				draggable : true,
				resizable : true,
				modal : true,
				layout : 'fit'
			});
	winEdit.show();
	
	var mask = new Ext.LoadMask(winEdit.body, {
		msg : '程序运行中....'
	});
	
	
	//jwStore.load({
	//	callback : function(r, callOptions, successFlag) {
	//		jwCourseidEdit.setValue(node.attributes.jwCourseid);
	//		exStore.load({
	//			callback : function(r, callOptions, successFlag) {
	//				exCourseidEdit.setValue(node.attributes.exCourseid);
	//				mask.hide();
	//			}
	//		});
	//	}
	//});
	if(exStore&&exCourseidEdit){ 
		mask.show();
		exStore.load({
			callback : function(r, callOptions, successFlag) {
				exCourseidEdit.setValue(node.attributes.exCourseid);
				mask.hide();
			}
		});
	}
	if(resStore&&resCourseidEdit){ 
		mask.show();
		resStore.load({
			callback : function(r, callOptions, successFlag) {
				resCourseidEdit.setValue(node.attributes.resCourseid);
				mask.hide();
			}
		});
	}	
}