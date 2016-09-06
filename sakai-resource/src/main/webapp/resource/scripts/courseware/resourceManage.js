var resourceToolbar;
var resourceListStore;
var swfu;
var fileName;
var rightClickMenu;
Ext.onReady(function() {
	// 启用所有基于标签的提示
	Ext.QuickTips.init();//鼠标悬停提示
	// 错误提示地方，旁边
	Ext.form.Field.prototype.msgTarget = "side";//错误信息显示位置 在字段的右边显示一个提示信息

	// 定义工具栏菜单，右键也可以这么定义
	var resourceMenu = [{
				// 页面上显示的文本
				text : "导入新课件",
				// 图标路径，一般menu,button会有这个配置(属性)
				iconCls : "np-create", //css class
				// 定义出发按钮所要执行的操作
				handler : function() {
					//销毁右键弹出来的菜单
					removeRightClickMenu();
					// 0代表上传1代表修改
					resourceImport(0);
				}
			}, {
				// 页面上显示的文本
				text : "预览课件",
				// 图标路径，一般menu,button会有这个配置(属性)
				iconCls : "np-preview",
				// 定义出发按钮所要执行的操作
				handler : function() {
					resourcePreView();
				}
			}, {
				//页面上显示的文本 
				text : "导出资源",
				//图标路径，一般menu,button会有这个配置(属性)
				iconCls : "btn-questionbank",
				//定义出发按钮所要执行的操作 
				handler : function(){
					resourceExport();
				}
			}, {
				// 页面上显示的文本
				text : "修改课件",
				// 图标路径，一般menu,button会有这个配置(属性)
				iconCls : "np-update",
				// 定义出发按钮所要执行的操作
				handler : function() {
					//销毁右键弹出来的菜单
					removeRightClickMenu();
					// 0代表上传1代表修改
					resourceImport(1);
				}
			}, {
				// 页面上显示的文本
				text : "删除课件",
				// 图标路径，一般menu,button会有这个配置(属性)
				iconCls : "np-delete",
				// 定义出发按钮所要执行的操作
				handler : function() {
					//销毁右键弹出来的菜单
					removeRightClickMenu();
					resourceDelete();
				}
			}, {
				// 文本框
				fieldLabel : "课件名称:",
				xtype : "textfield",
				id : "rsearch",
				name : "text",
				value : "请输入课件名称",
				listeners : {
					focus : function() {
						if (resourceToolbar.findById("rsearch").getValue() == "请输入课件名称") {
							resourceToolbar.findById("rsearch").setValue("");
						}
					}
				}
			}, {
				// 页面上显示的文本
				text : "查询",
				id : "find",
				// 图标路径，一般menu,button会有这个配置(属性)
				iconCls : "folder_find",
				// 定义出发按钮所要执行的操作
				handler : function() {
					resourceFind();
				}
			}, {
				// 页面上显示的文本
				text : "回收站",
				// 图标路径，一般menu,button会有这个配置(属性)
				iconCls : "bin_closed",
				// 定义出发按钮所要执行的操作
				handler : function() {
					//销毁右键弹出来的菜单
					removeRightClickMenu();
					trash();
				}
			}];

	// 把定义的工具栏加载到菜单里
	resourceToolbar = new Ext.Toolbar(resourceMenu);

	// 定义资源列表的选择框的选择模型--单选还是多选，现在设置的是单选
	var checkBoxSM = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false
		});

	// 表示资源的状态
	var sizeRender = function(value) {
		var sale = "M";//mb
		if (value < 0.1) {
			value = value * 1024;
			sale = "K";//kb
			if (value < 0.1) {
				value = value * 1024;
				sale = "B";//byte
			}
		}
		return value+sale;
	};

	// 显示格式化的时间格式
	var timeRender = function(value) {
		var year = Ext.util.Format.substr(value, 0, 4);
		var mouth = Ext.util.Format.substr(value, 5, 2);
		var day = Ext.util.Format.substr(value, 8, 2);
		var last = Ext.util.Format.substr(value, 11, 8);
		var time = year + "年" + mouth + "月" + day + "日 " + last;
		return time;
	};
	
	//课件播放地址
	var playUrl = function(value) {
		if(value != "")
			return "<a target='_blank' href='"+value+"'>播放</a>";
		else
			return "";
	}

	// 定义列模型
	var resourceListCM = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(),//行号
			checkBoxSM, {
				// 表头显示的文字
				header : "标识",
				// 数据来源
				dataIndex : "id",
				// 是否隐藏
				hidden : true,
				// 是否可以编辑是否隐藏
				hideable : false
			}, {
				// 表头显示的文字
				header : "课件名称",
				// 数据来源
				dataIndex : "courseName",
				width : .25,
				sortable : true
			}, {
				header : "课件摘要",// 表头文字
				id : "summary",
				dataIndex : "summary",// 数据来源
				width : .25,
				sortable : true
			}, {
				header : "大小",// 表头文字
				dataIndex : "fileSize",// 数据来源
				renderer : sizeRender,
				width : .1,
				sortable : true
			}, {
				header : "创建时间",// 表头文字
				dataIndex : "createTime",// 数据来源
				renderer : timeRender,
				width : .2,
				sortable : true
			}, {
				header : "修改时间",// 表头文字
				dataIndex : "updateTime",// 数据来源
				renderer : timeRender,
				width : .2,
				sortable : true
			}, {
				header : "播放地址",// 表头文字
				id : "play_url",
				dataIndex : "play_url",// 数据来源
				renderer: playUrl,
				width : .25,
				sortable : true
			}]);

	// 定义数据列的数据来源
	resourceListStore = new Ext.data.JsonStore({
		id : "resourceListStore",
		url : path + "/courseware_query.do?status=1",//0上传1修改
		root : "root",
		fields : ["id", "courseName", "summary", "fileUrl", "fileSize",
				"creater", "createTime", "createDate", "modifier", "updateTime","play_url"]
	});//字段定义信息，包含字段 与数据对象间的映射关系

	// 加载数据
	resourceListStore.load();

	// 定义列表，把数据源和定义的列模型加载进来以显示
	var resourceListGP = new Ext.grid.GridPanel({
				id : "resourceListGP",
				region : "center",// 设置显示内容的位置
				cm : resourceListCM,// 指定列模型
				sm : checkBoxSM,
				renderTo : 'coursewarediv',
				autoHeight : true,
				closable : true,// 指定为真使其可以关闭
				stripeRows : true,// true是显示行的分隔符，默认会加上这个样式x-grid3-row-alt，自己也可以覆盖
				loadMask : true,// true表示在数据加载过程中会有一个遮盖效果
				loadingText : "加载中...",
				store : resourceListStore,// 指定数据来源
				autoExpandColumn : "courseName",// grid会把你指定的这列自动扩充来填满空白
				columnLines : true,// true是显示列分隔符
				layout : "fit",// 指定布局模式
				viewConfig : {
					forceFit : true
				},
				tbar : resourceToolbar,
				bbar : new Ext.PagingToolbar({
							id : "resourceGridPage",
							pageSize : 20,
							store : resourceListStore,
							displayInfo : true
						})
			});


	// 给行增加右键菜单--为什么不直接调这个事件rowcontextmenu
	resourceListGP.on("rowcontextmenu", function(grid, rowIndex, e) {
				e.preventDefault();// 取消浏览器对当前事件做的默认操作
				if (rowIndex < 0) {
					return;
				}
				var model = grid.getSelectionModel();
				model.selectRow(rowIndex);// 选择这一行
				rightClickMenu = new Ext.menu.Menu({
							items : resourceMenu
						});
				rightClickMenu.remove(rightClickMenu.findById("rsearch"));
				rightClickMenu.remove(rightClickMenu.findById("find"));
				rightClickMenu.showAt(e.getXY());// 在右击的坐标处显示右键菜单
			});

	// 导入或修改新资源
	function resourceImport(update) {
		var createFP;
		// 创建FormPanel
		if(update == 0){
			createFP = new Ext.form.FormPanel({
				id : "createFP",
				width : 200,
				height : 200,
				url : path + "/courseware_save.do",
				method : "post",
				labelAlign : "right",
				// fileUpload : true,
				frame : true,
				labelSeparator : "：",
				labelWidth : 80,
				autoHeight : true,
				anchor : "100%",
				items : [{
							xtype : "hidden",
							name : "id",
							id : "id"
						}, {
							xtype : "box",
							fieldLabel : "课件文件",
							autoEl : {
								tag : 'div',
								html : '<span id="uploadFileButtonPlaceHolder"></span><span id="courseSpan"></span>'
							}
						}, {
							// 文本框
							fieldLabel : "<font color='red'>*</font>课件名称",
							xtype : "textfield",
							width : 200,
							id : "courseName",
							name : "courseName"
						}, {
							// 文本框
							fieldLabel : "课件摘要",
							xtype : "textarea",
							width : 200,
							id : "summary",
							name : "summary"
						}, {
							// 文本框
							fieldLabel : "播放地址",
							xtype : "textfield",
							width : 200,
							id : "play_url",
							name : "play_url"
						},{
						//文本框
						text : "标红星的为必填项。",
						xtype : "tbtext",
						hideOnClick : true
						}],
				buttons : [{
					text : "保存",
					id : "uploadMediaButton",
					iconCls : "login",
					scale : "medium",
					handler : function() {
						if(createFP.getForm().isValid()){
							if (swfu.getFile(0) != null) {
								swfu.addPostParam("id", createFP.findById("id")
												.getValue());
								swfu.addPostParam("courseName", createFP
												.findById("courseName").getValue());
								swfu.addPostParam("summary", createFP
												.findById("summary").getValue());
								swfu.addPostParam("play_url", createFP
												.findById("play_url").getValue());
								swfu.startUpload();
							} else {
									createFP.getForm().submit({
												success : function(form, action) {
													doResponse(form, action,
															function() {
																resourceFind();// 重新加载数据
																upWindow.close();
															})
												}
											});
	
							}
						}
					}
				}, {
					text : "关闭",
					iconCls : "np-reject-32",
					scale : "medium",
					handler : function() {
						upWindow.close();
					}
				}]
			});
		}else if (update == 1) {// 如果修改资源就把选中的资源加载到页面上
			
			createFP = new Ext.form.FormPanel({
				id : "createFP",
				width : 200,
				height : 200,
				url : path + "/courseware_save.do",
				method : "post",
				labelAlign : "right",
				frame : true,
				labelSeparator : "：",
				labelWidth : 80,
				autoHeight : true,
				anchor : "100%",
				items : [{
							xtype : "hidden",
							name : "id",
							id : "id"
						}, {
							xtype : "box",
							fieldLabel : "课件文件",
							autoEl : {
								tag : 'div',
								html : '<span id="uploadFileButtonPlaceHolder"></span><span id="courseSpan"></span>'
							}
						}, {
							// 文本框
							fieldLabel : "<font color='red'>*</font>课件名称",
							xtype : "textfield",
							width : 200,
							id : "courseName",
							name : "courseName",
							allowBlank : false
						}, {
							// 文本框
							fieldLabel : "课件摘要",
							xtype : "textarea",
							width : 200,
							id : "summary",
							name : "summary"
						}, {
							// 文本框
							fieldLabel : "播放地址",
							xtype : "textfield",
							width : 200,
							id : "play_url",
							name : "play_url"
						},{
						//文本框
						text : "标红星的为必填项。如需修改课件文件请重新上传。",
						xtype : "tbtext",
						hideOnClick : true
						}],
				buttons : [{
					text : "保存",
					id : "uploadMediaButton",
					iconCls : "login",
					scale : "medium",
					handler : function() {
							if (swfu.getFile(0) != null) {
								swfu.addPostParam("id", createFP.findById("id")
												.getValue());
								swfu.addPostParam("courseName", createFP
												.findById("courseName").getValue());
								swfu.addPostParam("summary", createFP
												.findById("summary").getValue());
								swfu.addPostParam("play_url", createFP
												.findById("play_url").getValue());				
								swfu.startUpload();
							} else {
									createFP.getForm().submit({
												success : function(form, action) {
													doResponse(form, action,
															function() {
																resourceFind();// 重新加载数据
																upWindow.close();
															})
												}
											});
	
							}
					}
				}, {
					text : "关闭",
					iconCls : "np-reject-32",
					scale : "medium",
					handler : function() {
						upWindow.close();
					}
				}]
			});
			//获得用户选中的数据
			var selections = resourceListGP.getSelectionModel().getSelections();
			if (selections == null || selections.length == 0) {// 如果没有选择一条记录，则弹出提示
				showWarn("请选择一条记录！");
				return;
			}else if(selections.length > 1){
				showWarn("只能选择一条记录");
				return;
			}
			createFP.getForm().loadRecord(selections[0]);
		}

		var upWindow = new Ext.Window({
					id : "upWindow",
					title : "课件资源上传",
					layout : "fit",
					width : 350,
					height : 250,
					autoDestroy : true,
					closeAction : "close",// 这样只是隐藏，默认是销毁
					modal : true,
					items : [createFP]
				});
		upWindow.on("close", function() {
					swfu.destroy();
				});
		upWindow.show();

		var settings = {
			flash_url : path + "/resource/scripts/swfupload/swfupload.swf",// swfupload的位置
			upload_url : path + "/courseware_save.do;jsessionid=" + sessionId,// 服务器端处理上传文件的servlet
			// 上传时的action
			// 路径必须要加上上下文路径
			// 否则
			// 会出现 IE兼容 firefox不兼容情况
			file_types : fileTypes,// 允许的文件类型
			file_types_description : "文件类型",// 文件类型的描述
			file_size_limit : fileSizeLimit + " B",// 最大文件尺寸
			file_queue_limit :1,// 文件队列的大小。0为不限制
			// debug : false,// 是否调试模式 输出调试信息
			button_image_url : path + "/resource/images/blank.png",
			button_text : "<b>上传</b>",// 按钮文本，支持Html
			button_width : "61",// 按钮宽
			button_height : "22",// 按钮高
			button_text_left_padding : 15,// 设置Flash Button上文字距离左侧的距离，可以使用负值。
			button_placeholder_id : "uploadFileButtonPlaceHolder",// 按钮放置的位置
			button_action : SWFUpload.BUTTON_ACTION.SELECT_FILE,// 单文件上传
			button_cursor : SWFUpload.CURSOR.HAND,// 此参数可以设置鼠标划过Flash
			// Button时的光标状态。默认为SWFUpload.CURSOR.ARROW，如果设置为SWFUpload.CURSOR.HAND，则为手形
			// button_window_mode : SWFUpload.WINDOW_MODE.OPAQUE,
			moving_average_history_size : 40,
			// 事件处理函数
			file_queued_handler : fileQueued,// 文件加入到上传队列成功后触发
			file_dialog_start_handler: dialogStart,
			trigger_error_event : false,
			file_queue_error_handler : fileQueueError,// 文件加入到上传队列失败后触发
			// file_dialog_complete_handler : fileDialogComplete,//
			// 文件选择框关闭后，文件处理完成时触发
			upload_start_handler : uploadStart,// 将文件往服务端上传之前触发此事件
			upload_progress_handler : uploadProgress,// 该事件由flash定时触发，提供三个参数分别访问上传文件对象、已上传的字节数，总共的字节数
			upload_error_handler : uploadError,// 只要上传被终止或者没有成功完成，那么该事件都将被触发
			upload_success_handler : uploadSuccess
			// 当文件上传的处理已经完成（这里的完成只是指向目标处理程序发送了Files信息，只管发，不管是否成功接收），并且服务端返回了200的HTTP状态时，触发此事件。
			// upload_complete_handler : uploadComplete
			// 当上传队列中的一个文件完成了一个上传周期，无论是成功(uoloadSuccess触发)还是失败(uploadError触发)，此事件都会被触发
		};

		swfu = new SWFUpload(settings);

	}

	// 预览资源
	function resourcePreView() {
		var selections = resourceListGP.getSelectionModel().getSelections();
		if(selections == null || selections.length == 0){
			showWarn("请选择一条记录！");
			return;
		}else if(selections.length > 1){
			showWarn("只能选择一条记录");
			return;
		}
		resourcePreview(selections);
	}

	// 导出资源
	function resourceExport() {
		var selections = resourceListGP.getSelectionModel().getSelections();
		if(selections == null || selections.length == 0){
			showWarn("请选择一条记录！");
			return;
		}else if(selections.length > 1){
			showWarn("只能选择一条记录");
			return;
		}
		var courseId = selections[0].data.id;
		var btn = Ext.Msg.confirm("提示", "确认要导出该课件吗？", function(btn, text) {
					if (btn == "yes") {
						Ext.Ajax.request({
									url : path + "/courseware_export.do",
									method : "post",
									timeout: 180000,
									params : {
										id : courseId
									},
									success : function(response, options) {
										var resdata = Ext.decode(response.responseText);
										if(resdata.result){
											var downloadUrl = resdata.msg;
											window.location.href=downloadUrl;
										}else{
											showInfo(resdata.msg);
										}
									},
									failure : function(response, options) {
										alert("导出失败！");
									}
								});
					}
				});
	}

	// 删除资源
	function resourceDelete() {
		//获得用户选中的数据
		var selections = resourceListGP.getSelectionModel().getSelections();
		if (selections == null || selections.length == 0) {
			showWarn("请选择一条记录！");
			return;
		}
		var selectionIds = getSelectionId(selections);
		var btn = Ext.Msg.confirm("提示", "确认要删除该课件吗？", function(btn, text) {
					if (btn == "yes") {
						Ext.Ajax.request({
									url : path + "/courseware_delete.do",
									method : "post",
									params : {
										selectionIds : selectionIds
									},
									success : function(result, request) {
										doAjaxResponse(result, request,function() {
											resourceFind();
										})
									}
								});
					}
				});
	}

	// 验证formpanl属性不能为空
	function checkNull(fp) {
		if (fp.findById("courseName").getValue() == "") {
			showInfo("课件名称不能为空！");
			return false;
		}else if (fp.findById("summary").getValue() == "") {
			Ext.Msg.alert("提示", "课件摘要不能为空"); return false; 
		}else {
			return true;
		}
	}

	// 加入队列失败的事件处理
	function fileQueueError(file, error, message) {
		if (error == SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED) {
			showError("超出文件队列！");
		}
		if (error == SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT) {
			showError(file.name + "大小为" + file.size + "字节，超出服务器限制！");
		}
		if (error == SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE) {
			showError(file.name + "大小为零！");
		}
		if (error == SWFUpload.QUEUE_ERROR.INVALID_FILETYPE) {
			showError(file.name + "文件类型为" + file.type + "，超出服务器限制！");
		}
	}


	//上传成功后调的函数
	function uploadSuccess(file, serverData) {
		if(Ext.decode(serverData).success==true){
			showInfo(Ext.decode(serverData).msg);
		}else{
			showError(Ext.decode(serverData).msg);
		}
		Ext.getCmp('resourceListGP').getStore().reload();
		Ext.getCmp('upWindow').close();
	}
	//上传失败后调的函数
	function uploadError(file, errorCode, message) {
		if (errorCode === SWFUpload.UPLOAD_ERROR.FILE_CANCELLED) {
			// Don't show cancelled error boxes
			return;
		}
		showError("上传失败！");
	}
	//选中上传文件的窗体打开时触发的事件
	function dialogStart(){
		this.cancelUpload();
	}
	//文件加入到上传队列成功后触发
	function fileQueued(file){
		//给课件名称赋值
		if(Ext.getCmp("courseName").getValue() == "")
			Ext.getCmp("courseName").setValue(getExtention(file.name));
		//给课程摘要赋值
		if(Ext.getCmp("summary").getValue() == "")
			Ext.getCmp("summary").setValue(getExtention(file.name));
		document.getElementById("courseSpan").innerHTML = "(课件名称："+file.name+")";
	}
	//获取文件名称
	function getExtention(str){
		return str.substring(0,str.lastIndexOf("."));
	}
	//获得选中的课件的ID集合
	function getSelectionId(selections){
		var arry = new Array();
		var i = 0;
		Ext.each(selections,function(item){//遍历record数组
     		arry[i] = item.data.id;//将遍历出来的某个record加载到数组中
     		i++;
   	 	});
   	 	return arry;
	}
	
	//隐藏或销毁右键出来的对象
	function removeRightClickMenu(){
		if(rightClickMenu != null)
			rightClickMenu.removeAll();
	}

});
// 查询资源
	function resourceFind() {
		resourceListStore.proxy.conn.url = path + "/courseware_findByName.do";
		if (resourceToolbar.findById("rsearch").getValue() == "请输入课件名称"
				|| resourceToolbar.findById("rsearch").getValue() == null) {
			resourceListStore.load({
						params : {
							status : "1"
						}
					});
		} else {
			resourceListStore.load({
						params : {
							courseName : resourceToolbar.findById("rsearch")
									.getValue(),
							status : "1"
						}
					});
		}
	}
