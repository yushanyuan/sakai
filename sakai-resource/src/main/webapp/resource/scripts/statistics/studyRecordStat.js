/**
 * 学生学习记录统计
 */
// 开启提示功能
Ext.QuickTips.init();
var courseNode = new Ext.tree.AsyncTreeNode({});
var curNode = new Ext.tree.AsyncTreeNode({});
// window.name = "studentStudy";
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
				if (child.attributes.type == "2") { // 是模块
					if (child.attributes.status == "0") { // 是隐藏模块
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

Ext.onReady(function() {
	var loadMask = new Ext.LoadMask(Ext.getBody(), {
				msg : '数据加载中....'
			});

	var treeWidth = document.body.clientWidth - 30;
	var loader = new Ext.tree.TreeLoader({
				dataUrl : 'statistics_loadStudyRecordStatData.do?studyrecordId='+studyrecordId,
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
				width : treeWidth * 0.55,
				dataIndex : 'text'
			}, {
				header : '开始学习时间',
				width : treeWidth * 0.15,
				dataIndex : 'startStudyDate'
			}, {
				header : '学习时长',
				width : treeWidth * 0.1,
				dataIndex : 'selfStudyTimeShow',
				renderer : function(v) {
					if (v != null && v != "") {
						return v + "分钟";
					}
				}
			},
			{
				header : '活动得分',
				width : treeWidth * 0.1,
				dataIndex : 'activityScore'
			},{
				header : '活动尝试次数',
				width : treeWidth * 0.1,
				dataIndex : 'attemptTimes',
				renderer : function(v) {
					if (v != null && v != "") {
						return v + "次";
					}
				}
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
					nodeIdx : node.attributes.showIdx,
					noderecordId : node.attributes.recordId,
					courseId : courseNode.attributes.id
				};
			}, loader);
	// 当新节点前触发
	tree.on('beforeappend', function(tree, parent, node) {
				loadMask.hide();// 将模态关闭
				if (node.attributes.type == "1") {// 课程节点
					courseNode = node;
				}
			});

});