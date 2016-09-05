// 开启提示功能
Ext.QuickTips.init();
var courseNode = new Ext.tree.AsyncTreeNode({});
var curNode = new Ext.tree.AsyncTreeNode({});
Ext.form.FieldSet.prototype.onCheckClick = function() {
	this[this.checkbox.dom.checked ? 'expand' : 'collapse']();
	var formobj = this.findParentByType(Ext.form.FormPanel);
	formobj.doLayout();
	formobj.setHeight(600);
	var wo = this.findParentByType(Ext.Window);
	if (this.checkbox.dom.checked) {
		wo.maximize();
	} else {
		wo.restore();
	}
}
function changeDisplay(parentNode, display) {
	parentNode.eachChild(function(child) {
				if (child.attributes.type == "2") { // 是节点
					if (child.attributes.status == "0") { // 是隐藏节点
						if (display) { // 显示
							child.show();
						} else { // 不显示
							child.hide();
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
					child.show();
				} else { // 不显示
					child.hide();
				}
			});
	courseNode.show();
}
function showIcoType(icoType) {
	courseNode.cascade(function(child) {
				if (child.attributes.type == icoType) {
					child.bubble(function(parent) {
								parent.show();
							});
				}
			});
}
Ext.onReady(function() {
	var siteY = 0;
	var loadMask = new Ext.LoadMask(Ext.getBody(), {
				msg : '数据加载中....'
			});

	var showScore = new Ext.form.Checkbox({
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
														parent.show();
													});
										}
									});
						} else {
							changeAllNodeDisplay(true);
						}
					}
				}
			});
	var toolIcon = new Ext.Toolbar({
				height : '30',
				items : [showScore, new Ext.form.Label({
									// @author:wangchengfa
									// text : '图标说明：'
									text : '导航：'
								}),  new Ext.Button({
									text : "<img src='resource/icons/house_go.png'>全部",
									handler : function() {
										changeAllNodeDisplay(true);
									}
								}),new Ext.Button({
									text : "<img src='resource/icons/module.gif'>节点",
									handler : function() {
										changeAllNodeDisplay(false);
										showIcoType("2");
									}
								}), new Ext.Button({
									text : "<img src='resource/icons/section.gif'>页",
									handler : function() {
										changeAllNodeDisplay(false);
										showIcoType("3");
									}
								}), new Ext.Button({
									text : "<img src='resource/icons/test.gif'>作业",
									handler : function() {
										changeAllNodeDisplay(false);
										showIcoType("4");
									}
								}), new Ext.Button({
									text : "<img src='resource/icons/selftest.gif'>前测",
									handler : function() {
										changeAllNodeDisplay(false);
										showIcoType("6");
									}
								}), new Ext.Button({
									text : "<img src='resource/icons/forum.gif'>讨论",
									handler : function() {
										changeAllNodeDisplay(false);
										showIcoType("5");
									}
								})]
			});
	var message = new Ext.Toolbar({
				height : '30',
				items : [new Ext.form.Label({
									html : '&nbsp;&nbsp;'
								}), new Ext.form.Label({
									text : '课程平时成绩：' + score
								}), new Ext.form.Label({
									html : "&nbsp;&nbsp;<a href='statistics.do?studyrecordId="+studyrecordId+"' target='_blank'>学习记录统计</a>"
								})]
			});
	var tool = new Ext.Panel({
				layout : 'form',
				renderTo : 'toolpanel',
				height : 60,
				region : 'north',
				layoutConfig : {
					animate : true
				},
				items : [toolIcon, message]
			})
	
	var treeWidth = document.body.clientWidth - 30;
	
	var loader = new Ext.tree.TreeLoader({
				dataUrl : 'studySpace_loadCourse.do?studyrecordId='+studyrecordId,
				uiProviders : {
					'col' : Ext.ux.tree.ColumnNodeUI
				}
			});
	var root = new Ext.tree.AsyncTreeNode({
				text : 'root',
				id : '0',
				type : '0'
			});
	var cm = [{
				header : '标题',
				width : treeWidth * 0.6,
				dataIndex : 'text'
			}, {
				header : '建议时长',
				width : treeWidth * 0.1,
				dataIndex : 'studyTimeShow',
				renderer : function(v) {
					if (v != null && v != "") {
						return v + "分钟";
					}
				}
			},
			{
				header : '我的时长',
				width : treeWidth * 0.1,
				dataIndex : 'selfStudyTimeShow',
				renderer : function(v) {
					if (v != null && v != "") {
						return v + "分钟";
					}
				}
			}, {
				header : '主要属性',
				width : treeWidth * 0.2,
				dataIndex : 'mainAttr'
			}];
	var tree = new Ext.ux.tree.ColumnTree({
		renderTo : 'treepanel',
		rootVisible : false,
		autoScroll : true,
		title : '课程管理',
		columns : cm,
		loader : loader,
		root : root
	});
	tree.on('expandnode', function() {
		pageHeightInit();

	})

	// 当加载子节点时触发
	tree.on('beforeload', function(node) {
		loadMask.show();// 将模态开启
		loader.baseParams = {
			nodeType : node.attributes.type, // 节点类型
			nodeIdx : node.attributes.showIdx
		};
	}, loader);
	// 当新节点前触发
	tree.on('beforeappend', function(tree, parent, node) {
		loadMask.hide();// 将模态关闭
		if (node.attributes.type == "1") {// 课程节点
			courseNode = node;
		}
	});

	tree.on('click', function(node , event) {
		siteY = (event.getXY())[1] - 50;
		curNode = node;
		if (node.attributes.type == 2) {//模块
			
			//若树节点类型为模块
			window.location.href = 'studySpace_fowardModule.do?node='+node.id+'&studyrecordId='+studyrecordId;
		} else if (node.attributes.type == 3) {//页
			//若树节点类型为页
			window.location.href = 'studySpace_fowardCourseware.do?node='+node.id+'&studyrecordId='+ studyrecordId;
			
		} else if (node.attributes.type == "4") {// 作业
						window.showModalDialog("studyPaper_writeTestInit.do?testId="+node.id+"&studyrecordId="+studyrecordId, window,
								"dialogWidth:" + screen.availWidth + ";dialogHeight:" + screen.availHeight);					
		} else if (node.attributes.type == "5") {// 讨论
			window.showModalDialog("studyPaper_forumInit.do?forumId="+node.id+"&studyrecordId="+studyrecordId, window, 
				"dialogWidth:" + screen.availWidth + ";dialogHeight:" + screen.availHeight);
		} else if (node.attributes.type == "6") {// 前测
//			alert(node.attributes.attemptNumber);
			if (node.attributes.attemptNumber < 1) {
				window.showModalDialog("studyPaper_writeSelfTestInit.do?testId="+node.id+"&studyrecordId="+studyrecordId, window,
					"dialogWidth:" + screen.availWidth + ";dialogHeight:" + screen.availHeight);
			} else {
				showWarn('前测只能进行一次!',siteY);
			}
		}
	});
});