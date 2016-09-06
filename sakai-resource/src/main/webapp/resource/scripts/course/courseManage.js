// 开启提示功能
Ext.QuickTips.init(); 	//课程组织
var courseNode = new Ext.tree.AsyncTreeNode({});
var actMask;
var playerTemplate='';
document.onclick = function() {
}
function fitScreen() {
    var sectionfrm = parent.document.getElementById("coursewareFrame");
    var fp1 = $(parent.document).find("iframe")[0];
	var fp2 = $(parent.parent.document).find(".portletMainIframe")[0];
	var extralHeight = 35;
	var mainHeight = $("#treepanel").height()+$("#toolpanel").height();
	if(mainHeight <300){
		mainHeight = 300;
	}

    $("body").height(mainHeight);
    if (fp1) {
        $(fp1).height($("body").outerHeight()+extralHeight);
        if (fp2) {
            $(fp2).height($(fp1).outerHeight() + 50);
        }
    }
}

function showNode (node) {
	node.ui.node.hidden = false;
	if(node.ui.wrap){
	    node.ui.wrap.style.display = "";
	}
}

function hideNode (node) {
	node.ui.node.hidden = true;
	if(node.ui.wrap){
	    node.ui.wrap.style.display = "none";
	}
}

function changeDisplay(parentNode, display) {
	parentNode.eachChild(function(child) {
		if (child.attributes.type == "2") { // 是模块    child属性参数类型是模块
			if (child.attributes.status == "0") { // 是隐藏模块
				if (display) { // 显示
					showNode(child);//显示  隐藏
				} else { // 不显示
					hideNode(child);//隐藏
				}
			} else {
				changeDisplay(child, display);
			}
		}
	});
}
function changeAllNodeDisplay(display) {
	courseNode.cascade(function(child) {
		if (display) { // 显示
			showNode(child);
		} else { // 不显示
			hideNode(child);
		}
	});
	showNode(courseNode);
}
function showIcoType(icoType) {
	courseNode.cascade(function(child) {
		if (child.attributes.type == icoType) {
			child.bubble(function(parent) {
				showNode(parent);
			});
		}
	});
}
function komp(url){
	window.open(url);
}

Ext.onReady(function() {
	var loadMask = new Ext.LoadMask(Ext.getBody(), {
				msg : '数据加载中....'
			});
	actMask = new Ext.LoadMask(Ext.getBody(), {
				msg : '程序运行中....'
			});

	var chkShowHide = new Ext.form.Checkbox({
		boxLabel : '显示隐藏节点',
		checked : true,
		width : 100,
		listeners : {
			check : function(obj, checked) {
				changeDisplay(courseNode, checked);
			}
		}
	});

	var bar_items = [
		new Ext.form.Label({
			text : '导航：'
		}), new Ext.Button({
			text : "全部",
			iconCls:"btn-home",
			handler : function() {
				changeAllNodeDisplay(true);
			}
		}), new Ext.Button({
			text : "节点",
			iconCls:"btn-module",
			handler : function() {
				changeAllNodeDisplay(false);
				showIcoType("2");
			}
		}), new Ext.Button({
			text : "页",
			iconCls:"btn-section",
			handler : function() {
				changeAllNodeDisplay(false);
				showIcoType("3");
			}
		}), new Ext.Button({
			text : "讨论",
			iconCls:"btn-forum",
			handler : function() {
				changeAllNodeDisplay(false);
				showIcoType("5");
			}
		}), new Ext.form.Label({
			text : '',width : 100		
		}), chkShowHide
		,new Ext.form.Checkbox({
			boxLabel : '只显示计算平时成绩的活动',
			checked : false,
			width : 200,
			listeners : {
				check : function(obj, checked) {
					if (checked) {
						changeAllNodeDisplay(false);
						courseNode.cascade(function(child) {
							if (child.attributes.isCaculateScore == "1") {
								child.bubble(function(parent) {
									showNode(parent);
								});
							}
						});
					} else {
						changeAllNodeDisplay(true);
					}
				}
			}
		})
	]
	//开启作业
	if(useExamSys){
		var btn_test = new Ext.Button({
			text : "作业",
			iconCls:"btn-test",
			handler : function() {
				changeAllNodeDisplay(false);
				showIcoType("4");
			}
		})
		bar_items.splice(4, 0, btn_test);
		var btn_selftest = new Ext.Button({
			text : "前测",
			iconCls:"btn-selftest",
			handler : function() {
				changeAllNodeDisplay(false);
				showIcoType("6");
			}
		})
		bar_items.splice(5, 0, btn_selftest);

		var goExamButton = new Ext.Button({
			text : '进入题库',
			iconCls:"btn-questionbank",
			handler : function() {
				window.open(examSysEntryUrl);
			}
		})		
		bar_items.splice(10, 0, goExamButton);	
	}
	
	if(useResSys){
		var goResButton = new Ext.Button({
			text : '进入资源系统',
			iconCls:"btn-questionbank",
			handler : function() {
				window.open(resSysEntryUrl);
			}
		})		
		bar_items.splice(11, 0, goResButton);	
	}

	// 定义工具栏
	var toolIcon = new Ext.Toolbar({
		height : '30',
		items : bar_items
	});

	var tool = new Ext.Panel({
				layout : 'form',
				renderTo : 'toolpanel',
				height : 30,
				width: '100%',
				region : 'north',
				layoutConfig : {
					animate : true
				},
				items : [toolIcon]
			})
	var treeWidth = document.body.clientWidth;
	var loader = new Ext.tree.TreeLoader({
				dataUrl : 'courseSpace_loadCourse.do?dt='+new Date(),//加载课程树
				uiProviders : {
					'col' : Ext.ux.tree.ColumnNodeUI
				}
			});
	var root = new Ext.tree.AsyncTreeNode({
				text : '<font style=color:red>root</font>',
				id : '0',
				type : '0'
			});
	var cm = [{
				header : '标题（' + getHelpImg(path) + '右键单击标题显示快捷菜单）',
				width : treeWidth * 0.60,
				dataIndex : 'text'
			}, {
				header : '建议学习时长',
				width : treeWidth * 0.15,
				dataIndex : 'studyTimeShow'
			}, {
				header : '主要属性',
				width : treeWidth * 0.2,
				dataIndex : 'mainAttr'   
			}, {
				header : '预览',
				width : treeWidth * 0.05,
				renderer : function (value, cellmeta, record, rowIndex, columnIndex, store) {
					var url;
					if("3"==record.type){
						url="/portal/tool/"+toolId+"/studySpace_fowardCourseware.do?node="+record.id+"&dt="+Math.round(Math.random()*10000);
						return "<a target='_blank' onclick='komp(\""+url+"\");'>预览</a>";
					}
					if("1"==record.type&&record.playerTemplate!='custom'&&record.playerTemplate!=''){
						playerTemplate=record.playerTemplate;
						url=tplPath+record.playerTemplate+"/chapter.html?placementId="+toolId+"&dt="+Math.round(Math.random()*10000);
						return "<a target='_blank' onclick='komp(\""+url+"\");'>预览</a>";
					}
					if("2"==record.type&&playerTemplate!='custom'&&playerTemplate!=''){
						url=tplPath+playerTemplate+"/module.html?placementId="+toolId+"&module="+record.id+"&dt="+Math.round(Math.random()*10000)+" mid="+record.id;
						return "<a target='_blank' onclick='komp(\""+url+"\");'>预览</a>";
					}
				}  
			}];
	var tree = new Ext.ux.tree.ColumnTree({
				region : 'center',
				renderTo : 'treepanel',
				autoHeight : true,
				rootVisible : false,
				autoScroll : true,
				title : '课程管理',
				enableDD : true,// 允许拖拽
				columns : cm,
				loader : loader,
				root : root
			});

	tree.on('beforenodedrop', function(dropEvent) {// 当一个DD对象在置下到某个节点之前触发，用于预处理，返回false则取消置下
				if (confirm("您确定要拖拽？")) {
					loadMask.show();
					var node = dropEvent.target; // 目标结点
					var data = dropEvent.data; // 拖拽的数据
					if (data.node) {// 树内拖拽，修改原父节点的所有子节点的序号
						var dropNode = data.node;// 被拖拽的节点
						var dropParent = dropNode.parentNode;
						var descParent = node.parentNode;
						var point = dropEvent.point; // 拖拽到目标结点的位置
						Ext.Ajax.request({
									url : 'courseSpace_dragNode.do',//拖拽某一个模块或者页节点到另外一个位置
									success : function(response, options) {
										loadMask.hide();
										showInfo("执行成功");
										root.reload();
									},
									failure : function(response, options) {
										return false;
									},
									params : {
										point : point,
										destId : node.id,
										destType : node.attributes.type,
										node : dropNode.id,
										nodeType : dropNode.attributes.type
									}
								});
					}
					return true;
				} else {
					return false;
				}
			})

	tree.on('beforeload', function(node) { // 加载分支节点
				loadMask.show();// 显示加载中提示
				loader.baseParams = {
					nodeType : node.attributes.type, // 节点类型
					nodeIdx : node.attributes.showIdx,
					showHide : true,
					showScore : false,
					childType : node.attributes.childType
					// 下级节点类型
				};
			}, loader);

	tree.on('beforeappend', function(tree, parent, node) {
				loadMask.hide();// 隐藏加载中提示
				if (node.attributes.type == "1") {// 课程节点
					courseNode = node;
				} else if (node.attributes.type == "2" && node.attributes.status == "0" && !chkShowHide.getValue()) {
					hideNode(node);
				}
			});

	tree.on('nodedragover', function(e) {// 当树节点成为拖动目标时触发，返回false签名这次置下将不允许
				var data = e.data; // 拖拽的数据
				var node = e.target;// 目标节点
				if (data.node)// 树内拖拽
				{
					if (node.leaf == true) {
						node.leaf = false;
					}
					var dropNode = data.node;// 被拖拽的节点
					var point = e.point;// 插入方式
					if (point == "append") {
						if (dropNode.parentNode.id == node.id) {
							return false;
						}
						if (node.attributes.type == "2") {// 目标节点是模块
							var leaf = node.attributes.leaf;// 是否为叶子
							var subLeaf = node.attributes.subLeaf;
							var secLeaf = node.attributes.secLeaf;
							if (dropNode.attributes.type == "2" && !(leaf ? false : (secLeaf ? false : true))) { // 拖拽模块
								return true;
							} else if (dropNode.attributes.type == "3" && !(leaf ? false : (subLeaf ? false : true))) {// 拖拽页
								return true;
							} else {
								return false;
							}
						} else if (node.attributes.type == "1") {// 目标节点是课程
							if (dropNode.attributes.type != "2") {
								return false;
							}
						}

					} else {
						if (node.attributes.type != dropNode.attributes.type) {
							return false;
						}
					}
				} else {
					return false;
				}
			})

	tree.on('expandnode', function() {// 展开节点时，重新设置页面高度，防止出现双滚动条
				//pageHeightInit();
				fitScreen();
				eval(document.body.onload);
			})

	tree.on('contextmenu', function(node, event) {// 声明菜单类型
				node.select();
				var curNode = node;
				var wintitle = "";
				while (curNode && curNode != root) {
					wintitle = curNode.text + "——" + wintitle;
					curNode = curNode.parentNode;
				}
				// 定义右键菜单对象
				var contextmenu = new Ext.menu.Menu({
							items : []
						});
				var nodeType = node.attributes.type;
				if (nodeType == "1") { // 课程节点
					var status = node.attributes.status;// 课程状态
					var leaf = node.attributes.leaf;// 是否为叶子
					var setCourseMenu = contextmenu.add({
								text : '设置课程信息',
								handler : function() {
									contextmenu.hide();
									setCourse(node);
								}
							});
					var setAttrMenu = contextmenu.add({
						text : '设置节点和页的属性',
						disabled : leaf,
						handler : function() {// 单击事件句柄
							contextmenu.hide();
							var record = new Ext.data.Record.create([{
										name : 'ico'
									}, // 图标
									{
										name : 'title'
									}, // 标题
									{
										name : 'required'
									}, // 必修
									{
										name : 'studyTime'
									}, // 学习时间
									{
										name : 'prerequids'
									}, // 开启条件
									{
										name : 'id'
									},// id
									{
										name : 'pid'
									},// pid
									{
										name : 'nodeType'
									}// 类型
							]);
							var arr = [];
							courseNode.cascade(function(child) {
								if (child.attributes.type == "2") {// 模块
									var level = child.getDepth() - 1;
									var icon = "";
									for (var i = 1; i < level; i++) {
										icon += "<img height=16 width=16 src='resource/images/default/s.gif'>";
									}
									var j = new record({
												ico : icon + "<img src='resource/icons/module.gif'>",
												title : child.attributes.title,
												required : child.attributes.required,
												studyTime : child.attributes.studyDay,
												id : child.attributes.id,
												pid : child.attributes.parentId,
												nodeType : "2"
											});
									arr.push(j);
								} else if (child.attributes.type == "3") {// 页
									var level = child.getDepth() - 1;
									var icon = "";
									for (var i = 1; i < level; i++) {
										icon += "<img height=16 width=16 src='resource/images/default/s.gif'>";
									}
									var j = new record({
												ico : icon + "<img src='resource/icons/section.gif'>",
												title : child.attributes.title,
												required : child.attributes.required,
												studyTime : child.attributes.studyTime,
												id : child.attributes.id,
												pid : child.attributes.moduleId,
												nodeType : "3"
											});
									arr.push(j);
								}
							});
							setAttributes(arr);
						}
					});
					// 设置分数所占平时成绩百分比
					var setPcntMenu = contextmenu.add({
								text : '设置分数所占平时成绩百分比',
								disabled : node.attributes.scoreNum == "0" ? true : false,
								handler : function() {// 单击事件句柄
									contextmenu.hide();
									setPercent(node.id);
								}
							});
					// 分隔线
					contextmenu.add('-');
					// 添加节点
					var addModuleMenu = contextmenu.add({
								text : '添加节点',
								disabled : status == "0" ? true : false,
								handler : function() {// 单击事件句柄
									contextmenu.hide();
									//addModule(node, wintitle);
									jQuery("#btn_go_module").attr("href",jQuery("#btn_go_module").data('href') + "?action=new&node="+node.attributes.id+"&nodeType="+node.attributes.type);
									document.getElementById("btn_go_module").click();
								}
							});
					contextmenu.add({
								text : '添加节点-导入',
								disabled : status == "0" ? true : false,
								handler : function() {// 单击事件句柄
									contextmenu.hide();
									addModuleScorm(node, wintitle);
								}
							});						
					// 分隔线
					contextmenu.add('-');
					// 导入课件资源
					var impWareMenu = contextmenu.add({
								text : '导入课件资源',
								disabled : status == "0" ? true : false,
								handler : function() {// 单击事件句柄
									contextmenu.hide();
									importScorm(node);
								}
							});
					// 导出课件资源
					var expWareMenu = contextmenu.add({
								text : '导出课件资源',
								disabled : leaf,
								handler : function() {// 单击事件句柄
									contextmenu.hide();
									showQuestions("您确定要导出课件？",null, function(btn) {
										var exportMask = new Ext.LoadMask(Ext.getBody(), {
												msg : '导出课件执行中....'
										});
												exportMask.show();
												if (btn == 'ok') {// 确认继续操作
													Ext.Ajax.request({
																url : 'courseSpace_exportScorm.do',
																success : function(response, options) {
																	exportMask.hide();
																	var resdata = Ext.decode(response.responseText);
																	var downloadUrl = resdata.downloadUrl;
																	window.location.href=downloadUrl;
																},
																failure : function(response, options) {
																	exportMask.hide();
																	alert("fail");
																},
																params : {
																	node : node.id
																}
															});
												}else {
													exportMask.hide();
												}
											});
								}
							});
				} else if (nodeType == "2") {// 模块节点
					var status = node.attributes.status;// 状态
					var leaf = node.attributes.leaf;// 是否为叶子
					var childType = node.attributes.childType;// 子节点类型
					if (status == "0") {// 隐藏
						contextmenu.add({
									text : '恢复',
									handler : function() {// 单击事件句柄
										contextmenu.hide();
										showQuestions("您确定要恢复该节点？",null, function(btn) {
													if (btn == 'ok') {// 确认继续操作
														loadMask.show();
														Ext.Ajax.request({
															//恢复模块，将模块状态改为1，同时该模块下的子模块、页、活动的状态也都要改为1
																	url : 'courseSpace_showModule.do',
																	success : function(response, options) {
																		loadMask.hide();
																		showInfo("执行成功");
																		if (node.parentNode.id != courseNode.id) {
																			var obj = Ext.decode(response.responseText);
																			var scoreNum = obj.scoreNum;
																			courseNode.attributes.scoreNum = scoreNum;// 重新设置课程的计分活动总数
																		}
																		node.parentNode.parentNode.reload();
																		// node.parentNode.expand(true);
																	},
																	failure : function(response, options) {
																	},
																	params : {
																		node : node.id,
																		courseId : courseNode.id,
																		parent : node.parentNode.id,
																		parentType : node.parentNode.attributes.type
																	}
																});
													}
												});
									}
								});
					} else {// 正常
						contextmenu.add({
									text : '添加节点',
									disabled : (childType == "2" || childType == "") ? false : true,
									handler : function() {// 单击事件句柄
										contextmenu.hide();
										//addModule(node, wintitle);
										jQuery("#btn_go_module").attr("href",jQuery("#btn_go_module").data('href') + "?action=new&node="+node.attributes.id+"&nodeType="+node.attributes.type);
										document.getElementById("btn_go_module").click();
									}
								});
						contextmenu.add({
									text : '添加节点-导入',
									disabled : (childType == "2" || childType == "") ? false : true,
									handler : function() {// 单击事件句柄
										contextmenu.hide();
										addModuleScorm(node, wintitle);
									}
								});						
						contextmenu.add({
									text : '添加页',
									disabled : (childType == "3" || childType == "") ? false : true,
									handler : function() {// 单击事件句柄
										contextmenu.hide();
										//addSection(node, wintitle);
										jQuery("#btn_go_section").attr("href",jQuery("#btn_go_section").data('href') + "?action=new&node="+node.attributes.id);
										document.getElementById("btn_go_section").click();
									}
								});
						contextmenu.add({
									text : '添加页-导入',
									disabled : (childType == "3" || childType == "") ? false : true,
									handler : function() {// 单击事件句柄
										contextmenu.hide(); 
										addSectionScorm(node, wintitle);
									}
								});
						//开启作业
						if(useExamSys){						
							contextmenu.add({
								text : '添加作业',
								handler : function() {// 单击事件句柄
									contextmenu.hide();
									addTest(node, wintitle);
								}
							});
						}
						contextmenu.add({
									text : '添加讨论',
									handler : function() {// 单击事件句柄
										contextmenu.hide();
										if(forumType=='sakai.forums'){
											addForumMessageForum(node, wintitle);
										}else{
											addForum(node, wintitle);
										}
										
									}
								});
						contextmenu.add({
									text : '添加前测',
									disabled : node.attributes.selftest == "" ? false : true,
									handler : function() {// 单击事件句柄
										contextmenu.hide();
										addSelfTest(node, wintitle);
									}
								});
						contextmenu.add('-');
						contextmenu.add({
									text : '编辑',
									handler : function() {// 单击事件句柄
										contextmenu.hide();
										//updateModule(node, wintitle);
										jQuery("#btn_go_module").attr("href",jQuery("#btn_go_module").data('href') + "?node="+node.attributes.id);
										document.getElementById("btn_go_module").click();
									}
								});
						contextmenu.add({
									text : '删除',
									handler : function() {// 单击事件句柄
										contextmenu.hide();
										showQuestions("您确定要删除该节点？删除操作不可恢复",null, function(btn) {
													if (btn == 'ok') {// 确认继续操作
														loadMask.show();
														Ext.Ajax.request({
																	url : 'courseSpace_delModule.do',//删除模块
																	success : function(response, options) {
																		loadMask.hide();
																		showInfo("删除成功");
																		if (node.parentNode.id != courseNode.id) {
																			var obj = Ext.decode(response.responseText);
																			var scoreNum = obj.scoreNum;
																			courseNode.attributes.scoreNum = scoreNum;// 重新设置课程的计分活动总数
																		}
																		node.parentNode.parentNode.reload();
																	},
																	failure : function(response, options) {
																	},
																	params : {
																		node : node.id,
																		courseId : courseNode.id,
																		parent : node.parentNode.id,
																		parentType : node.parentNode.attributes.type,
																		brotherNum : node.parentNode.childNodes.length
																	}
																});
													}
												});
									}
								});
						contextmenu.add({
									text : '隐藏',
									handler : function() {// 单击事件句柄
										contextmenu.hide();
										showQuestions("您确定要隐藏该节点？隐藏后的节点可恢复",null, function(btn) {
													if (btn == 'ok') {// 确认继续操作
														loadMask.show();
														Ext.Ajax.request({
															//隐藏模块，将模块状态改为0，同时该模块下的子模块、页、活动的状态也都要改为0
																	url : 'courseSpace_hideModule.do',
																	success : function(response, options) {
																		loadMask.hide();
																		showInfo("节点已被隐藏，若想恢复请执行右键菜单中的恢复操作");
																		if (node.parentNode.id != courseNode.id) {
																			var obj = Ext.decode(response.responseText);
																			var scoreNum = obj.scoreNum;
																			courseNode.attributes.scoreNum = scoreNum;// 重新设置课程的计分活动总数
																		}
																		node.parentNode.parentNode.reload();
																	},
																	failure : function(response, options) {
																	},
																	params : {
																		node : node.id,
																		courseId : courseNode.id,
																		parent : node.parentNode.id,
																		parentType : node.parentNode.attributes.type
																	}
																});
													}
												});
									}
								});
					}
				} else if (nodeType == "3") {// 页节点
					//开启作业
					if(useExamSys){	
						contextmenu.add({
							text : '添加作业',
							handler : function() {// 单击事件句柄
								contextmenu.hide();
								addTest(node, wintitle);
							}
						});
					}
					contextmenu.add({
								text : '添加讨论',
								handler : function() {// 单击事件句柄
									contextmenu.hide();
									if(forumType=='sakai.forums'){
										addForumMessageForum(node, wintitle);
									}else{
										addForum(node, wintitle);
									}
								}
							});
					contextmenu.add({
								text : '添加前测',
								disabled : node.attributes.selftest == "" ? false : true,
								handler : function() {// 单击事件句柄
									contextmenu.hide();
									addSelfTest(node, wintitle);
								}
							});
					contextmenu.add('-');
					contextmenu.add({
								text : '编辑',
								handler : function() {// 单击事件句柄
									contextmenu.hide();
									//updateSection(node, wintitle);
									//console.log(node)
									jQuery("#btn_go_section").attr("href",jQuery("#btn_go_section").data('href') + "?node="+node.attributes.id);
									document.getElementById("btn_go_section").click();
								}
							});
					contextmenu.add({
								text : '删除',
								handler : function() {// 单击事件句柄
									contextmenu.hide();
									showQuestions("您确定要删除该页？删除操作不可恢复",null, function(btn) {
												if (btn == 'ok') {// 确认继续操作
													loadMask.show();
													Ext.Ajax.request({
																//删除页
																url : 'courseSpace_delSection.do',
																success : function(response, options) {
																	var obj = Ext.decode(response.responseText);
																	var allActNum = obj.scoreNum;
																	courseNode.attributes.scoreNum = allActNum;// 重新设置课程的计分活动总数
																	loadMask.hide();
																	showInfo("删除成功");
																	node.parentNode.parentNode.reload();
																},
																failure : function(response, options) {
																},
																params : {
																	node : node.id,
																	courseId : courseNode.id,
																	parent : node.parentNode.id,
																	brotherNum : node.parentNode.childNodes.length
																}
															});
												}
											});
								}
							});
				} else if (nodeType == "4") {// 作业节点
					contextmenu.add({
								text : '编辑',
								handler : function() {// 单击事件句柄
									contextmenu.hide();
									updateTest(node, wintitle);
								}
							});
					contextmenu.add({
						text : '删除',
						handler : function() {// 单击事件句柄
							contextmenu.hide();
							showQuestions("您确定要删除该作业？删除操作不可恢复",null, function(btn) {
								if (btn == 'ok') {// 确认继续操作
									loadMask.show();
									Ext.Ajax.request({
												url : 'courseSpace_delTest.do',//删除作业
												success : function(response, options) {
													loadMask.hide();
													var extend = node.attributes.isCaculateScore;// 是否计算平时成绩
													showInfo("删除成功");
													if (extend == "1") {
														courseNode.attributes.scoreNum = Number(courseNode.attributes.scoreNum)
																- 1;
														node.parentNode.parentNode.reload();
													} else {
														node.remove();
													}
												},
												failure : function(response, options) {
												},
												params : {
													node : node.id,
													isCaculateScore : node.attributes.isCaculateScore,
													parent : node.parentNode.id,
													parentType : node.parentNode.attributes.type
												}
											});
								}
							});
						}
					});
				} else if (nodeType == "5") {// 讨论节点
					contextmenu.add({
								text : '编辑',
								handler : function() {// 单击事件句柄
									contextmenu.hide();
									if(forumType=='sakai.forums'){
										updateForumMessageForum(node, wintitle);
									}else{
										updateForum(node, wintitle);
									}
									
								}
							});
					contextmenu.add({
						text : '删除',
						handler : function() {// 单击事件句柄
							contextmenu.hide();
							showQuestions("您确定要删除该讨论？删除操作不可恢复",null, function(btn) {
								if (btn == 'ok') {// 确认继续操作
									loadMask.show();
									Ext.Ajax.request({
												url : 'courseSpace_delForum.do',//删除讨论
												success : function(response, options) {
													loadMask.hide();
													var extend = node.attributes.isCaculateScore;// 是否计算平时成绩
													showInfo("删除成功");
													if (extend == "1") {
														courseNode.attributes.scoreNum = Number(courseNode.attributes.scoreNum)
																- 1;
														node.parentNode.parentNode.reload();
													} else {
														node.remove();
													}
												},
												failure : function(response, options) {
												},
												params : {
													node : node.id,
													isCaculateScore : node.attributes.isCaculateScore,
													parent : node.parentNode.id,
													parentType : node.parentNode.attributes.type
												}
											});
								}
							});
						}
					});
				} else if (nodeType == "6") {// 前测节点
					contextmenu.add({
								text : '编辑',
								handler : function() {// 单击事件句柄
									contextmenu.hide();
									updateSelfTest(node, wintitle);
								}
							});
					contextmenu.add({
						text : '删除',
						handler : function() {// 单击事件句柄
							contextmenu.hide();
							showQuestions("您确定要删除该前测？删除操作不可恢复",null, function(btn) {
								if (btn == 'ok') {// 确认继续操作
									loadMask.show();
									Ext.Ajax.request({
												url : 'courseSpace_delSelfTest.do',//删除前测
												success : function(response, options) {
													loadMask.hide();
													var extend = node.attributes.isCaculateScore;// 是否计算平时成绩
													showInfo("删除成功");
													if (extend == "1") {
														courseNode.attributes.scoreNum = Number(courseNode.attributes.scoreNum)
																- 1;
														node.parentNode.parentNode.reload();
													} else {
														node.parentNode.attributes.selftest = "";
														node.remove();
													}
												},
												failure : function(response, options) {
												},
												params : {
													node : node.id
												}
											});
								}
							});
						}
					});
				}
				event.preventDefault();
				contextmenu.showAt(event.getXY());// 取得鼠标点击坐标，展示菜单
			});

});