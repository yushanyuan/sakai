/*

 * ! Ext JS Library 3.3.0 Copyright(c) 2006-2010 Ext JS, Inc.
 * licensing@extjs.com http://www.extjs.com/license
 */

/**
 * @author ys
 * */
///////////////////////////////////////////////////////
IFrameComponent = Ext.extend(Ext.BoxComponent, {
	onRender : function(ct, position) {
		this.el = ct.createChild({
			tag : 'iframe',
			id : this.id,
			style : 'height: 100%;width:100%',
			name : this.name,
			frameBorder : 0,
			src : this.url
		});
	}
});



Ext.onReady(function() {
	// 开启提示功能
	Ext.QuickTips.init();
	
	var tabs = new Ext.TabPanel({
		id : 'centerTab',
		renderTo : Ext.getBody(),
		autoScroll : false,
		tabPosition : 'top',
		layoutConfig : {
			animate : true
		},
		activeTab : 0,
		enableTabScroll : false,
		items:[
			new Ext.tree.TreePanel({
				title : '文件资源管理',
				root : new Ext.tree.AsyncTreeNode({
							hidden  : true
						})
			}),
			new IFrameComponent({
				border : false,
				region : 'center',
				id : '1',
				title : '统计资源下载次数',
				closable : false,
				name : "frame1",
				url : 'statistics_downTimes.do'
			})
			
			
		]
	});
		
});
///////////////////////////////////////////////

 var mouseX = 0;
 var mouseY = 0;
Ext.onReady(function() {
	Ext.QuickTips.init();
	// 定义码表的树形列表
	var treeWidth = document.body.clientWidth - 10;
	var columnWidth = treeWidth / 10;
	var tree = new Ext.ux.tree.ColumnTree({
				id : 'columeTree',
				height : 500,
				width : "100%",
				rootVisible : false,
				autoScroll : true,
				//title : ' ',
				header : true,
				renderTo : Ext.getBody(),
				stripeRows : true,
				enableDD : true,
				columns : [{
							header : '标题',
							width : columnWidth * 3.3,
							dataIndex : 'text'
						}, {
							header : '文件摘要',
							width : columnWidth * 2.6,
							dataIndex : 'fileSummary'
						}, {
							header : '文件大小',
							width : columnWidth * 0.7,
							dataIndex : 'folderSize'
						}, {
							header : '是否计算平时成绩',
							width : columnWidth * 1.3,
							dataIndex : 'extendOption'
						}, {
							header : '最后修改时间',
							width : columnWidth * 1.7,
							dataIndex : 'updateTime'
						}],

				loader : new Ext.tree.TreeLoader({
							dataUrl : 'file.do',
							uiProviders : {
								'col' : Ext.ux.tree.ColumnNodeUI
							}
						}),

				root : new Ext.tree.AsyncTreeNode({
							text : 'testRoot',
							id : '0'
						})
			});

	// 给tree增加右键事件
	tree.on('contextmenu', treeContextHandler);

	// 点击文件下载
	tree.on('click', function(node) {

		if (node.leaf && node.attributes.treeNodeType=="file") {
			//节点为叶子 节点类型为文件时
			window.open("file_openURL.do?fileId="+node.id);
		}
		

			});

	tree.on('nodedragover', function(e) {
				 var node = e.target;
				 if(node.leaf) {
				 	node.leaf=false;
				 	node.attributes.iconCls='folder-not-empty';
				 }
				 
			});

	tree.on('nodedrop', function(e) {
				if (e.point == "append") {
					showInfo("将 " + e.dropNode.text + "移动到 " + e.target.text + " 下");
					var fileId = e.dropNode.id;
					var parentId = e.target.id;
					if (e.dropNode.leaf) {
						Ext.Ajax.request({
									url : 'file_moveFile.do',
									params : {
										fileId : fileId,
										parentId : parentId
									},
									success : function(result, request) {
										doAjaxResponse(result, request, function() {
												});
									}
								});
					}
				}
			});
	// 定义右键菜单
	var contextMenu = new Ext.menu.Menu({
				items : [{
							itemId : 'create',
							text : '创建文件夹',
							iconCls : 'np-create-folder',
							handler : createFolder
						}, '-', {
							itemId : 'delete',
							text : '删除文件夹',
							iconCls : 'np-delete-folder',
							handler : deleteFolder
						}, {
							itemId : 'upload',
							text : '上传文件',
							iconCls : 'upload-file',
							handler : uploadFile
						}, '-', {
							itemId : 'htmlFile',
							text : '创建HTML文件',
							iconCls : 'create-html-file',
							handler : createHtmlFile
						}, {
							itemId : 'txtFile',
							text : '创建文本文件',
							iconCls : 'create-txt-file',
							handler : createTxtFile
						}]
			});

	var contextMenuFile = new Ext.menu.Menu({
				items : [{
							itemId : 'deleteFile',
							text : '删除文件',
							iconCls : 'np-delete',
							handler : deleteFile
						}, {
							itemId : 'updateFile',
							text : '修改文件',
							iconCls : 'modify-file',
							handler : modifyFile
						}]
			});
	// 显示右键菜单
	function treeContextHandler(node, e) {
		mouseX = e.getPageX();
		mouseY = e.getPageY();
		node.select();// 选中当前行
		// 在当前行的锚点显示菜单
		// contextMenu.show(node.ui.getAnchor());
		// 在鼠标的位置显示菜单

		if (node.leaf) {
			if (node.attributes.treeNodeType == 'folder') {
				contextMenu.showAt(e.getXY());
			} else if (node.attributes.treeNodeType == 'file') {
				contextMenuFile.showAt(e.getXY());
			}
		} else {
			contextMenu.showAt(e.getXY());
		}
	}

	// ==========================开始表单项目==========================//

	// 定义文件夹表单项目
	var folderFormItems = function() {

		return {
			folderId : {
				xtype : 'hidden',
				id : 'id',
				fieldLabel : '节点ID'
			},
			parentId : {
				xtype : 'hidden',
				id : 'parentId',
				fieldLabel : '父节点ID'
			},
			folderName : {
				xtype : 'textfield',
				id : 'folderName',
				fieldLabel : '文件夹名称',
				allowBlank : false,
				width : 200,
				validator : function(value) {
					if (getlengthB(value) > 255) {
						return false;
					}
					return true;
				},
				invalidText : '不能超过255个字节'
			}
		};
	}

	// ==========================结束表单项目==========================//

	// 创建子文件夹
	function createFolder() {
		// 获取当前栏目的id
		var currentNode = tree.getSelectionModel().getSelectedNode();

		var parentId = currentNode.id;
		// 得到表单项目
		var formItems = folderFormItems();
		formItems.parentId.value = parentId;
		var createFolderFP = new Ext.form.FormPanel({
			id : 'createFolderForm',
			labelAlign : 'right',
			url : 'file_createFolder.do',
			frame : true,
			labelSeparator : '：',
			labelWidth : 80,
			autoHeight : true,
			anchor : '100%',
			items : [formItems.folderId, formItems.parentId, formItems.folderName],
			buttons : [{
				text : '创建文件夹',
				iconCls : 'np-accept-32',
				scale : 'medium',
				handler : function() {
					createFolderFP.getForm().submit({
						success : function(form, action) {
							doResponse(form, action, function() {

										// 在tree中增加一个节点
										// var newNode = action.result.data;
										newNode = action.result.data;

										currentNode.appendChild(newNode);
										// 如果当前节点没有展开，则展开它
										if (!currentNode.isExpanded()) {
											currentNode.expand();
										}
										// 如果当前节点是叶子，则将其图标改为非叶子
										if (currentNode.isLeaf()) {
											currentNode.leaf = false;
											currentNode.getUI().getIconEl().src = 'resource/icons/folder_not_empty.png';
										}
										// 关闭创建栏目窗口
										createFolderWindow.close();
									})
						}
					});
				}
			}, {
				text : '关闭窗口',
				iconCls : 'np-reject-32',
				scale : 'medium',
				handler : function() {
					createFolderWindow.close();
				}
			}]
		});
		// 定义创建栏目弹出窗口
		var createFolderWindow = new Ext.Window({
					layout : 'fit',
					y:mouseY,
					title : '创建文件夹',
					width : 400,
					modal : true,
					resizable : false,
					closeAction : 'close',
					buttonAlign : 'center',
					items : [createFolderFP]
				});
		createFolderWindow.show();
	}

	// 删除文件夹
	function deleteFolder() {
		// 获取当前栏目的id
		var currentNode = tree.getSelectionModel().getSelectedNode();

		var folderId = currentNode.id;
		var btn = Ext.Msg.confirm("系统提示", "删除操作不可恢复,是否继续？", function(btn, text) {
					if (btn == 'yes') {
						// 发送是否有子节点请求
						Ext.Ajax.request({
									url : 'file_detectChild.do',
									params : {
										folderId : folderId
									},
									success : function(result, request) {
										ajaxResponse(result, request, function() {
													var obj = Ext.decode(result.responseText)
													if (obj.data.result == "0") {// 如果没有子节点，则可以删除
														// 发送删除请求和参数
														Ext.Ajax.request({
																	url : 'file_deleteFolder.do',
																	params : {
																		folderId : folderId
																	},
																	success : function(result, request) {
																		doAjaxResponse(result, request, function() {
																			// 删除此节点
																			currentNode.remove();
																			});
																	}
																});
													} else if (obj.data.result == "1") {
														// 有子节点时
														var btn2 = Ext.Msg.confirm("系统提示", "该文件夹不为空,是否继续？", function(
																		btn2, text) {
																	if (btn2 == "yes") {
																		// 发送删除请求和参数
																		Ext.Ajax.request({
																					url : 'file_deleteFolder.do',
																					params : {
																						folderId : folderId
																					},
																					success : function(result, request) {
																						doAjaxResponse(result, request,
																								function() {
																									// 删除此节点
																									currentNode
																											.remove();
																								});
																					}
																				});
																	}
																});
													}
												});
									}
								});

					} else {

					}
				});
	}

	function confirmNormalGrade() {
		var extendOption = "1";
		var btnConfirm = Ext.Msg.confirm("上传提示", "是否计算平时成绩", function(btnConfirm, text) {
					if (btnConfirm == "yes") {
						extendOption = "1";
						uploadFile(extendOption);
					} else {
						extendOption = "2";
						uploadFile(extendOption);
					}
				});

	}

	// -------------------------------------------------------上传文件------------------------------------------------------------
	// 文件上传的表单项
	var uploadFileFormItems = function() {
		return {
			extendOption : {
				xtype : 'radiogroup',
				id : 'extendOption',
				fieldLabel : '是否计算平时成绩',
				allowBlank : false,
				items : [{
							boxLabel : '是',
							name : 'extendOption',
							inputValue : '1',
							checked : true
						}, {
							boxLabel : '否',
							name : 'extendOption',
							inputValue : '2'
						}]
			},
			fileName : {
				xtype : 'textfield',
				id : 'fileName',
				fieldLabel : '文件名称',
				width : 320,
				allowBlank : false,
				validator : function(value) {
					if (getlengthB(value) > 255) {
						return false;
					}
					return true;
				},
				invalidText : '不能超过255个字节'

			},
			summary : {
				xtype : 'textarea',
				id : 'summary',
				fieldLabel : '文件摘要',
				width : 320,
				allowBlank : false
			}
		}
	}

	// 上传文件功能入口
	function uploadFile() {
		var uploadFileWindow;
		fileTarget = "file";
		var currentNode = tree.getSelectionModel().getSelectedNode();// 返回选中的第一行的记录
		if (currentNode == null) {// 如果没有选择一条记录，则弹出提示
			showWarn("你必须选择一条记录才能进行此操作。");
			return;
		}
		var folderId = currentNode.id;// 得到当前行id
		var formItems = uploadFileFormItems();

		// 创建表单
		var uploadFileFP = new Ext.form.FormPanel({
					id : 'uploadFileForm',
					labelAlign : 'right',
					frame : true,
					labelSeparator : '：',
					labelWidth : 120,
					autoHeight : true,
					anchor : '100%',
					items : [{
								xtype : 'box',
								fieldLabel : '选择上传文件',
								autoEl : {
									tag : 'div',
									html : '<span id="uploadFileButtonPlaceHolder"></span><span id="uploadFile"></span>'
								}
							}, formItems.extendOption, formItems.fileName, formItems.summary],
					buttons : [{
						text : '上传文件',
						id : 'uploadFileButton',
						iconCls : 'np-accept-32',
						scale : 'medium',
						handler : function() {// 提交表单并获取url的响应
							if (uploadFileFP.getForm().isValid()) {
								if (swfu.getFile(0) != null) {// 如果有待上传文件
									swfu.addPostParam('extendOption',
											encodeURI(Ext.getCmp('extendOption').getValue().inputValue));
									swfu.addPostParam('fileName', encodeURI(Ext.getCmp('fileName').getValue()));
									swfu.addPostParam('summary', encodeURI(Ext.getCmp('summary').getValue()));
									swfu.startUpload();
								} else {
									showWarn("请选择上传文件!");
								}
							}
						}
					}, {
						text : '关闭窗口',
						iconCls : 'np-reject-32',
						scale : 'medium',
						handler : function() {
							uploadFileWindow.close();// 关闭上传文件窗口
						}
					}]
				});

		// 定义上传文件弹出窗口
		uploadFileWindow = new Ext.Window({
					id : 'uploadFileWindow',
					layout : 'fit',
					title : '文件上传',
					modal : true,
					width : 500,
					resizable : false,
					closeAction : 'close',
					buttonAlign : 'right',
					items : [uploadFileFP],
					y:mouseY
				});

		uploadFileWindow.on("close", function() {
					swfu.destroy();
				});
		uploadFileWindow.show();
		var settings = {
			flash_url : "resource/scripts/swfupload/swfupload.swf",// swfupload的位置
			upload_url : "file_uploadFile.do;jsessionid=" + sessionId,// 服务器端处理上传文件的servlet

			file_types : fileTypes,// 允许的文件类型
			file_types_description : "文件类型",// 文件类型的描述
			file_size_limit : fileSizeLimit + " B",// 最大文件尺寸
			file_queue_limit : 0,// 文件队列的大小。0为不限制
			debug : false,// 是否调试模式 输出调试信息
			post_params : {
				folderId : folderId,
				target : fileTarget
			},
			button_image_url : "resource/images/blank.png",
			button_text : "<b>选择</b>",// 按钮文本，支持Html
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
			file_queue_error_handler : fileQueueError,// 文件加入到上传队列失败后触发
			file_dialog_complete_handler : fileDialogComplete,// 文件选择框关闭后，文件处理完成时触发
			upload_start_handler : uploadStart,// 将文件往服务端上传之前触发此事件
			upload_progress_handler : uploadProgress,// 该事件由flash定时触发，提供三个参数分别访问上传文件对象、已上传的字节数，总共的字节数
			// upload_error_handler : uploadError,// 只要上传被终止或者没有成功完成，那么该事件都将被触发
			upload_success_handler : uploadSuccess,// 当文件上传的处理已经完成（这里的完成只是指向目标处理程序发送了Files信息，只管发，不管是否成功接收），并且服务端返回了200的HTTP状态时，触发此事件。
			upload_complete_handler : uploadComplete
			// 当上传队列中的一个文件完成了一个上传周期，无论是成功(uoloadSuccess触发)还是失败(uploadError触发)，此事件都会被触发
		};

		var swfu = new SWFUpload(settings);
	}

	function deleteFile() {
		// 获取当前文件夹的id
		var currentNode = tree.getSelectionModel().getSelectedNode();
		var fileId = currentNode.id;
		var btn = Ext.Msg.confirm("系统提示", "删除操作不可恢复,是否继续？", function(btn, text) {
					if (btn == 'yes') {
						// 如果没有子节点，则可以删除
						// 发送删除请求和参数
						Ext.Ajax.request({
									url : 'file_deleteFile.do',
									params : {
										fileId : fileId
									},
									success : function(result, request) {
										doAjaxResponse(result, request, function() {
											// 删除此节点
											currentNode.remove();
											});
									}
								});
					} else {

					}
				});
	}

	// 定义创建HTML表单项目
	var htmlFileFormItems = function() {
		return {
			htmlId : {
				xtype : 'hidden',
				id : 'id'
			},
			folderId : {
				xtype : 'hidden',
				id : 'folderId'
			},
			fileType : {
				xtype : 'hidden',
				id : 'fileType',
				value : '.html'
			},
			extendOption : {
				xtype : 'radiogroup',
				id : 'extendOption',
				fieldLabel : '是否计算平时成绩',
				allowBlank : false,
				items : [{
							boxLabel : '是',
							name : 'extendOption',
							inputValue : 1,
							checked : true
						}, {
							boxLabel : '否',
							name : 'extendOption',
							inputValue : 2
						}]
			},
			fileTitle : {
				xtype : 'textfield',
				id : 'fileTitle',
				fieldLabel : 'HTML文件标题',
				allowBlank : false,
				width : 450,
				validator : function(value) {
					if (getlengthB(value) > 255) {
						return false;
					}
					return true;
				},
				invalidText : '不能超过255个字节',
				listeners : {
					'blur' : function(obj) {
						Ext.Ajax.request({
									url : 'file_checkFile.do',
									params : {
										fileName : obj.getValue(),
										fileType : ".html",
										folderId:Ext.getCmp("columeTree").getSelectionModel().getSelectedNode().id
									},
									success : function(result, request) {
										ajaxResponse(result, request, function() {
													// FIXME 返回成功后操作
												});
									}
								});
						return false;
					}
				}
			},
			fileSummary : {
				xtype : 'textarea',
				id : 'summary',
				fieldLabel : 'HTML文件摘要',
				allowBlank : false,
				width : 450,
				height : 50
			},
			fileContent : {
				xtype : 'textarea',
				id : 'fileContent',
				fieldLabel : 'HTML文件内容',
				allowBlank : false,
				width : 530,
				height : 200
			}
		};
	}

	// 创建HTML文件
	function createHtmlFile() {
		var ckeditor;
		// 获取当前文件夹的id
		var currentNode = tree.getSelectionModel().getSelectedNode();
		var formItems = htmlFileFormItems();
		formItems.folderId.value = currentNode.id;

		var createHTMLFileFP = new Ext.form.FormPanel({
			id : 'createHTMLForm',
			url : 'file_createFile.do',
			labelAlign : 'right',
			frame : true,
			labelSeparator : '：',
			labelWidth : 110,
			autoHeight : true,
			anchor : '100%',
			items : [formItems.htmlId, formItems.folderId, formItems.fileType, formItems.extendOption,
					formItems.fileTitle, formItems.fileSummary, formItems.fileContent],
			buttons : [{
				text : '创建',
				iconCls : 'np-accept-32',
				scale : 'medium',
				handler : function() {
					// 将编辑器的数据更新到content中。
					CKEDITOR.instances.fileContent.updateElement();
					if (CKEDITOR.instances.fileContent.getData() == '<p>\u000a\u0009&nbsp;</p>'
							|| CKEDITOR.instances.fileContent.getData().length == 0) {
						showWarn("你必须填写一些内容。");
						return false;
					}
					createHTMLFileFP.getForm().submit({
						success : function(form, action) {
							doResponse(form, action, function() {
										var newNode = action.result.data;
										if (currentNode.isLeaf()) {
											currentNode.leaf = false;
											currentNode.getUI().getIconEl().src = 'resource/icons/folder_not_empty.png';
										}
										currentNode.appendChild(newNode);
										currentNode.reload;
										// 关闭创建故事窗口
										createHTMLWindow.close();
									})
						}
					});
				}
			}, {
				text : '取消创建',
				iconCls : 'np-reject-32',
				scale : 'medium',
				handler : function() {
					createHTMLWindow.close();
				}
			}]
		});
		// 定义创建HTML文件弹出窗口
		var createHTMLWindow = new Ext.Window({
					layout : 'fit',
					title : '创建HTML文件',
					width : 800,
					modal : true,
					resizable : false,
					closeAction : 'close',
					buttonAlign : 'center',
					y:mouseY,
					items : [createHTMLFileFP]
				});
		createHTMLWindow.show();
		createHTMLWindow.on('close', function() {
					CKEDITOR.remove(ckeditor);
				});
		ckeditor = CKEDITOR.replace('fileContent', {
				filebrowserBrowseUrl : path + '/resource/scripts/ckfinder/ckfinder.jsp?courseId='+courseId,
				filebrowserImageBrowseUrl : path + '/resource/scripts/ckfinder/ckfinder.jsp?type=Images&courseId='+courseId,
				filebrowserFlashBrowseUrl : path + '/resource/scripts/ckfinder/ckfinder.jsp?type=Flash&courseId='+courseId,
				filebrowserUploadUrl : path + '/resource/scripts/ckfinder/core/connector/java/connector.java?command=QuickUpload&type=Files&courseId='+courseId,
				filebrowserImageUploadUrl :path + '/resource/scripts/ckfinder/core/connector/java/connector.java?command=QuickUpload&type=Images&courseId='+courseId,
				filebrowserFlashUploadUrl :path + '/resource/scripts/ckfinder/core/connector/java/connector.java?command=QuickUpload&type=Flash&courseId='+courseId,
				height : 180
			});
	}

	// 定义创建文本文件表单项目
	var txtFileFormItems = function() {
		return {
			folderId : {
				xtype : 'hidden',
				id : 'folderId'
			},
			fileType : {
				xtype : 'hidden',
				id : 'fileType',
				value : '.txt'
			},
			extendOption : {
				xtype : 'radiogroup',
				id : 'extendOption',
				fieldLabel : '是否计算平时成绩',
				allowBlank : false,
				items : [{
							boxLabel : '是',
							name : 'extendOption',
							inputValue : 1,
							checked : true
						}, {
							boxLabel : '否',
							name : 'extendOption',
							inputValue : 2
						}]
			},
			fileTitle : {
				xtype : 'textfield',
				id : 'fileTitle',
				fieldLabel : '文本文件标题',
				allowBlank : false,
				width : 400,
				validator : function(value) {
					if (getlengthB(value) > 255) {
						return false;
					}
					return true;
				},
				invalidText : '不能超过255个字节',
				listeners : {
					'blur' : function(obj) {
						Ext.Ajax.request({
									url : 'file_checkFile.do',
									params : {
										fileName : obj.getValue(),
										fileType : ".txt"
									},
									success : function(result, request) {
										ajaxResponse(result, request, function() {
													// FIXME 返回成功后操作
												});
									}
								});
						return false;
					}
				}
			},
			fileSummary : {
				xtype : 'textarea',
				id : 'summary',
				fieldLabel : '文本文件摘要',
				allowBlank : false,
				width : 400,
				height : 50
			},
			fileContent : {
				xtype : 'textarea',
				id : 'fileContent',
				fieldLabel : '文本文件内容',
				allowBlank : false,
				width : 400,
				height : 250
			}
		};
	}

	// ---------------------------------------------创建文本文件------------------------------------------
	function createTxtFile() {
		// 获取当前文件夹的id
		var currentNode = tree.getSelectionModel().getSelectedNode();
		var formItems = txtFileFormItems();

		formItems.folderId.value = currentNode.id;

		var createTxtFileFP = new Ext.form.FormPanel({
			id : 'createTxtForm',
			url : 'file_createFile.do',
			labelAlign : 'right',
			frame : true,
			labelSeparator : '：',
			labelWidth : 110,
			autoHeight : true,
			anchor : '100%',
			items : [formItems.folderId, formItems.fileType, formItems.extendOption, formItems.fileTitle,
					formItems.fileSummary, formItems.fileContent],
			buttons : [{
				text : '创建',
				iconCls : 'np-accept-32',
				scale : 'medium',
				handler : function() {

					createTxtFileFP.getForm().submit({
						success : function(form, action) {
							doResponse(form, action, function() {
										var newNode = action.result.data;
										if (currentNode.isLeaf()) {
											currentNode.leaf = false;
											currentNode.getUI().getIconEl().src = 'resource/icons/folder_not_empty.png';
										}
										currentNode.appendChild(newNode);
										// 关闭创建故事窗口
										createTxtWindow.close();
									})
						}
					});
				}
			}, {
				text : '取消创建',
				iconCls : 'np-reject-32',
				scale : 'medium',
				handler : function() {
					createTxtWindow.close();
				}
			}]
		});
		// 定义创建文件弹出窗口
		var createTxtWindow = new Ext.Window({
					layout : 'fit',
					title : '创建文本文件',
					width : 560,
					modal : true,
					resizable : false,
					closeAction : 'close',
					buttonAlign : 'center',
					y:mouseY,
					items : [createTxtFileFP]
				});
		createTxtWindow.show();
	}

	// --------------------------------------文件修改-------------------------------------

	// 修改文本文件表单
	var txtFileUploadFormItems = function() {
		return {
			id : {
				xtype : 'hidden',
				id : 'fileId'
			},
			fileType : {
				xtype : 'hidden',
				id : 'fileType',
				value : '.txt'
			},
			extendOption : {
				xtype : 'radiogroup',
				id : 'extendOption',
				fieldLabel : '是否计算平时成绩',
				allowBlank : false,
				items : [{
							boxLabel : '是',
							name : 'extendOption',
							inputValue : 1,
							checked : true
						}, {
							boxLabel : '否',
							name : 'extendOption',
							inputValue : 2
						}]
			},
			fileName : {
				xtype : 'textfield',
				id : 'fileName',
				fieldLabel : '文本文件标题',
				allowBlank : false,
				width : 400,
				validator : function(value) {
					if (getlengthB(value) > 255) {
						return false;
					}
					return true;
				},
				invalidText : '不能超过255个字节'
			},
			summary : {
				xtype : 'textarea',
				id : 'summary',
				fieldLabel : '文本文件摘要',
				allowBlank : false,
				width : 400,
				height : 50
			},
			fileContent : {
				xtype : 'textarea',
				id : 'fileContent',
				fieldLabel : '文本文件内容',
				allowBlank : false,
				width : 400,
				height : 250
			}
		};
	}

	// 修改HTML文件的表单项目
	var modifyHTMLFileFormItems = function() {
		return {
			id : {
				xtype : 'hidden',
				id : 'fileId'
			},
			fileType : {
				xtype : 'hidden',
				id : 'fileType',
				value : '.html'
			},
			extendOption : {
				xtype : 'radiogroup',
				id : 'extendOption',
				fieldLabel : '是否计算平时成绩',
				allowBlank : false,
				items : [{
							boxLabel : '是',
							name : 'extendOption',
							inputValue : 1,
							checked : true
						}, {
							boxLabel : '否',
							name : 'extendOption',
							inputValue : 2
						}]
			},
			fileName : {
				xtype : 'textfield',
				id : 'fileName',
				fieldLabel : 'HTML文件标题',
				allowBlank : false,
				width : 450,
				validator : function(value) {
					if (getlengthB(value) > 255) {
						return false;
					}
					return true;
				},
				invalidText : '不能超过255个字节'
			},
			summary : {
				xtype : 'textarea',
				id : 'summary',
				fieldLabel : 'HTML文件摘要',
				allowBlank : false,
				width : 450,
				height : 50
			},
			fileContent : {
				xtype : 'textarea',
				id : 'fileContent',
				fieldLabel : 'HTML文件内容',
				allowBlank : false,
				width : 530,
				height : 200
			}
		};
	}

	// 文件上传的表单项
	var modifyFileFormItems = function() {
		return {
			fileId : {
				xtype : 'hidden',
				id : 'fileId'
			},
			extendOption : {
				xtype : 'radiogroup',
				id : 'extendOption',
				fieldLabel : '是否计算平时成绩',
				allowBlank : false,
				items : [{
							boxLabel : '是',
							name : 'extendOption',
							inputValue : '1',
							checked : true
						}, {
							boxLabel : '否',
							name : 'extendOption',
							inputValue : '2'
						}]
			},
			fileName : {
				xtype : 'textfield',
				id : 'fileName',
				fieldLabel : '文件名称',
				width : 320,
				allowBlank : false,
				validator : function(value) {
					if (getlengthB(value) > 255) {
						return false;
					}
					return true;
				},
				invalidText : '不能超过255个字节'
			},
			summary : {
				xtype : 'textarea',
				id : 'summary',
				fieldLabel : '文件摘要',
				width : 320,
				allowBlank : false
			}
		}
	}

	// 更改文件功能入口
	function modifyFile() {

		var uploadFileWindow;
		fileTarget = "file";
		var currentNode = tree.getSelectionModel().getSelectedNode();// 返回选中的第一行的记录
		if (currentNode == null) {// 如果没有选择一条记录，则弹出提示
			showWarn("你必须选择一条记录才能进行此操作。");
			return;
		}

		var fileType = currentNode.attributes.fileType;
		var fileId = currentNode.id;// 得到当前行id
		if (fileType == ".txt") {
			// 修改文本文件时 弹出txt 文本编辑器
			var formItems = txtFileUploadFormItems();
			formItems.id.value = currentNode.id;

			var modifyTxtFileFP = new Ext.form.FormPanel({
						id : 'updateTxtForm',
						url : 'file_modifyFile.do',
						labelAlign : 'right',
						frame : true,
						labelSeparator : '：',
						labelWidth : 110,
						autoHeight : true,
						anchor : '100%',
						items : [formItems.id, formItems.fileType, formItems.extendOption, formItems.fileName,
								formItems.summary, formItems.fileContent],
						buttons : [{
									text : '更改',
									iconCls : 'np-accept-32',
									scale : 'medium',
									handler : function() {

										modifyTxtFileFP.getForm().submit({
													success : function(form, action) {
														doResponse(form, action, function() {
																	var newNode = action.result.data;
																	var pNode = currentNode.parentNode;
																	pNode.replaceChild(newNode, currentNode);
																	// 关闭创建故事窗口
																	modifyTxtWindow.close();
																})
													}
												});
									}
								}, {
									text : '取消更改',
									iconCls : 'np-reject-32',
									scale : 'medium',
									handler : function() {
										modifyTxtWindow.close();
									}
								}]
					});
			modifyTxtFileFP.getForm().load({
						url : 'file_readFileInfo.do',
						params : {
							fileId : fileId,
							fileType : ".txt"
						},
						success : function(form, action) {
							var readFile = action.result.data;
						}
					});

			// 定义更新文件弹出窗口
			var modifyTxtWindow = new Ext.Window({
						layout : 'fit',
						title : '创建文本文件',
						width : 560,
						modal : true,
						resizable : false,
						closeAction : 'close',
						buttonAlign : 'center',
						y:mouseY,
						items : [modifyTxtFileFP]
					});
			modifyTxtWindow.show();

		} else if (fileType == ".html") {
			// 修改html文件时 弹出html 编辑器
			var ckeditor;
			var formItems = modifyHTMLFileFormItems();
			formItems.id.value = currentNode.id;

			var modifyHTMLFileFP = new Ext.form.FormPanel({
						id : 'modifyHTMLForm',
						url : 'file_modifyFile.do',
						labelAlign : 'right',
						frame : true,
						labelSeparator : '：',
						labelWidth : 110,
						autoHeight : true,
						anchor : '100%',
						items : [formItems.id, formItems.fileType, formItems.extendOption, formItems.fileName,
								formItems.summary, formItems.fileContent],
						buttons : [{
							text : '更改',
							iconCls : 'np-accept-32',
							scale : 'medium',
							handler : function() {
								// 将编辑器的数据更新到content中。
								CKEDITOR.instances.fileContent.updateElement();
								if (CKEDITOR.instances.fileContent.getData() == '<p>\u000a\u0009&nbsp;</p>'
										|| CKEDITOR.instances.fileContent.getData().length == 0) {
									showWarn("你必须填写一些内容。");
									return false;
								}
								modifyHTMLFileFP.getForm().submit({
											success : function(form, action) {
												doResponse(form, action, function() {
															var newNode = action.result.data;
															var pNode = currentNode.parentNode;
															pNode.replaceChild(newNode, currentNode);
															// 关闭窗口
															modifyHTMLWindow.close();
														})
											}
										});
							}
						}, {
							text : '取消更改',
							iconCls : 'np-reject-32',
							scale : 'medium',
							handler : function() {
								modifyHTMLWindow.close();
							}
						}]
					});
			modifyHTMLFileFP.getForm().load({
						url : 'file_readFileInfo.do',
						params : {
							fileId : fileId,
							fileType : ".html"
						},
						success : function(form, action) {
							var readFile = action.result.data;
							CKEDITOR.instances.fileContent.setData(Ext.getCmp('fileContent').getValue());
						}
					});
			// 定义更改HTML文件弹出窗口
			var modifyHTMLWindow = new Ext.Window({
						layout : 'fit',
						title : '更改HTML文件',
						width : 800,
						modal : true,
						resizable : false,
						closeAction : 'close',
						buttonAlign : 'center',
						y:mouseY,
						items : [modifyHTMLFileFP]
					});
			modifyHTMLWindow.show();
			modifyHTMLWindow.on('close', function() {
						CKEDITOR.remove(ckeditor);
					});
			ckeditor = CKEDITOR.replace('fileContent', {
						toolbar : [
								['Source', '-', 'NewPage', 'Preview', '-', 'Templates'],
								['Cut', 'Copy', 'Paste', 'PasteText', 'PasteFromWord', '-', 'Print', 'SpellChecker',
										'Scayt'],
								['Undo', 'Redo', '-', 'Find', 'Replace', '-', 'SelectAll', 'RemoveFormat'], '/',
								['Bold', 'Italic', 'Underline', 'Strike', '-', 'Subscript', 'Superscript'],
								['NumberedList', 'BulletedList', '-', 'Outdent', 'Indent', 'Blockquote'],
								['JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock'],
								['Link', 'Unlink', 'Anchor'], ['Table', 'HorizontalRule', 'SpecialChar', 'PageBreak'],
								'/', ['Styles', 'Format', 'Font', 'FontSize'], ['TextColor', 'BGColor']],
						height : 180
					});
		} else {
			// 其他类型文件的修改

			var formItems = modifyFileFormItems();
			formItems.fileId.value = fileId;
			// 创建表单
			var uploadFileFP = new Ext.form.FormPanel({
				id : 'uploadFileForm',
				url : 'file_modifyFile.do',
				labelAlign : 'right',
				frame : true,
				labelSeparator : '：',
				labelWidth : 150,
				autoHeight : true,
				anchor : '100%',
				items : [{
					xtype : 'box',
					fieldLabel : '选择要替换的文件(可选)',
					autoEl : {
						tag : 'div',
						html : '<span id="uploadFileButtonPlaceHolder"></span><span id="uploadFile"></span>'
					}
				}, formItems.extendOption, formItems.fileName, formItems.summary, formItems.fileId],
				buttons : [{
					text : '更改文件',
					id : 'uploadFileButton',
					iconCls : 'np-accept-32',
					scale : 'medium',
					handler : function() {// 提交表单并获取url的响应
						if (uploadFileFP.getForm().isValid()) {
							if (swfu.getFile(0) != null) {// 如果有待上传文件
								// 提示是否覆盖以前的文件
								var btn = Ext.Msg.confirm("系统提示", "是否覆盖以前文件？", function(btn, text) {
											if (btn == 'yes') {
												// 覆盖以前文件
												swfu.addPostParam('extendOption', encodeURI(Ext.getCmp('extendOption')
																.getValue().inputValue));
												swfu.addPostParam('fileName', encodeURI(Ext.getCmp('fileName')
																.getValue()));
												swfu.addPostParam('summary',
														encodeURI(Ext.getCmp('summary').getValue()));
												swfu.addPostParam('fileId', encodeURI(Ext.getCmp('fileId').getValue()));
												swfu.startUpload();
											} else {
												// 若不覆盖则直接提交表单
												uploadFileFP.getForm().submit({
															success : function(form, action) {
																doResponse(form, action, function() {
																			var pNode = currentNode.parentNode;
																			var newNode = action.result.data;
																			pNode.replaceChild(newNode, currentNode);
																			pNode.reload;
																			// 关闭创建故事窗口
																			uploadFileWindow.close();
																		})
															}
														});

											}
										});

							} else {
								// 若没有文件直接提交表单
								uploadFileFP.getForm().submit({
											success : function(form, action) {
												doResponse(form, action, function() {
															var pNode = currentNode.parentNode;
															var newNode = action.result.data;
															pNode.replaceChild(newNode, currentNode);
															// 关闭创建故事窗口
															uploadFileWindow.close();
														})
											}
										});
							}
						}
					}
				}, {
					text : '取消更改',
					iconCls : 'np-reject-32',
					scale : 'medium',
					handler : function() {
						uploadFileWindow.close();// 关闭上传文件窗口
					}
				}]
			});

			uploadFileFP.getForm().load({
						url : 'file_readFileInfo.do',
						params : {
							fileId : fileId
						},
						success : function(form, action) {
							var modifiedFile = action.result.data;
						}
					});

			// 定义上传文件弹出窗口
			uploadFileWindow = new Ext.Window({
						id : 'uploadFileWindow',
						layout : 'fit',
						title : '文件修改',
						modal : true,
						width : 530,
						resizable : false,
						closeAction : 'close',
						buttonAlign : 'right',
						y:mouseY,
						items : [uploadFileFP]
					});

			uploadFileWindow.on("close", function() {
						swfu.destroy();
					});
			uploadFileWindow.show();
			var settings = {
				flash_url : "resource/scripts/swfupload/swfupload.swf",// swfupload的位置
				upload_url : "file_modifyFile.do;jsessionid=" + sessionId,// 服务器端处理上传文件的servlet

				file_types : fileTypes,// 允许的文件类型
				file_types_description : "文件类型",// 文件类型的描述
				file_size_limit : fileSizeLimit + " B",// 最大文件尺寸
				file_queue_limit : 0,// 文件队列的大小。0为不限制
				debug : false,// 是否调试模式 输出调试信息
				post_params : {
					folderId : fileId
				},
				button_image_url : "resource/images/blank.png",
				button_text : "<b>选择</b>",// 按钮文本，支持Html
				button_width : "61",// 按钮宽
				button_height : "22",// 按钮高
				button_text_left_padding : 15,// 设置Flash
				// Button上文字距离左侧的距离，可以使用负值。
				button_placeholder_id : "uploadFileButtonPlaceHolder",// 按钮放置的位置
				button_action : SWFUpload.BUTTON_ACTION.SELECT_FILE,// 单文件上传
				button_cursor : SWFUpload.CURSOR.HAND,// 此参数可以设置鼠标划过Flash
				moving_average_history_size : 40,
				// 事件处理函数
				file_queued_handler : fileQueued,// 文件加入到上传队列成功后触发
				file_queue_error_handler : fileQueueError,// 文件加入到上传队列失败后触发
				file_dialog_complete_handler : fileDialogComplete,// 文件选择框关闭后，文件处理完成时触发
				upload_start_handler : uploadStart,// 将文件往服务端上传之前触发此事件
				upload_progress_handler : uploadProgress,// 该事件由flash定时触发，提供三个参数分别访问上传文件对象、已上传的字节数，总共的字节数
				// 只要上传被终止或者没有成功完成，那么该事件都将被触发
				upload_success_handler : uploadSuccess,// 当文件上传的处理已经完成（这里的完成只是指向目标处理程序发送了Files信息，只管发，不管是否成功接收），并且服务端返回了200的HTTP状态时，触发此事件。
				upload_complete_handler : uploadComplete
				// 当上传队列中的一个文件完成了一个上传周期，无论是成功(uoloadSuccess触发)还是失败(uploadError触发)，此事件都会被触发
			};

			var swfu = new SWFUpload(settings);
		}
	}

});
	

