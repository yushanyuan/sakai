// 开启提示功能
Ext.QuickTips.init();
var curNode = new Ext.tree.AsyncTreeNode({});
Ext.onReady(function() {
	var siteY = 0;
	var loadMask = new Ext.LoadMask(Ext.getBody(), {
				msg : '数据加载中....'
			});
	var treeWidth = document.body.clientWidth - 20;
	var loader = new Ext.tree.TreeLoader({
		dataUrl : 'studySpace_loadModuleCourse.do?studyrecordId='+ studyrecordId,
		uiProviders : {
			'col' : Ext.ux.tree.ColumnNodeUI
		}
	});
	var root = new Ext.tree.AsyncTreeNode({
				text : 'root',
				id : nodeId,
				type : "2"
			});
	var cm = [{
				header : '标题',
				width : treeWidth - 55 - 55 - 70,
				dataIndex : 'text'
			}, {
				header : '建议时长',
				width : 55,
				dataIndex : 'studyTimeShow',
				renderer : function(v){
					if(v!=null && v!="" && v!=undefined){
						return v+"分钟";
					}
				}
			}, 
			{
				header : '我的时长',
				width : 55,
				dataIndex : 'selfStudyTimeShow',
				renderer : function(v){
					if(v!=null && v!="" && v!=undefined){
						return v+"分钟";
					}
				}
			},
			{
				header : '主要属性',
				width : 70,
				dataIndex : 'mainAttr'
			}];
	var tree = new Ext.ux.tree.ColumnTree({
				region : 'center',
				rootVisible : false,
				autoScroll : true,
				title : pageTitle,
				columns : cm,
				renderTo : "treepanel",//Ext.getBody(),
				loader : loader,
				root : root
			});
	tree.on('load', function() {
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
				scrollImg();
			});
	tree.on('click', function(node,event) {
		siteY = (event.getXY())[1] - 50;
		curNode = node;
		if (node.attributes.type == "2") {
			
			// 若树节点类型为模块
			window.location.href = 'studySpace_fowardModule.do?node='+node.id+'&studyrecordId='+studyrecordId;
		} else if (node.attributes.type == 3) {			
			
			window.location.href = 'studySpace_fowardCourseware.do?node='+node.id+'&studyrecordId='+studyrecordId;
		} else if (node.attributes.type == "4") {// 作业
						window.showModalDialog("studyPaper_writeTestInit.do?testId="+node.id+"&studyrecordId="+studyrecordId, 
							window, "dialogWidth:" + screen.availWidth + ";dialogHeight:" + screen.availHeight);
		} else if (node.attributes.type == "5") {// 讨论	
			window.showModalDialog("studyPaper_forumInit.do?forumId="+node.id+"&studyrecordId="+studyrecordId, window, 
				"dialogWidth:" + screen.availWidth + ";dialogHeight:" + screen.availHeight);
		} else if (node.attributes.type == "6") {// 前测
			if (node.attributes.attemptNumber < 1) {
				window.showModalDialog("studyPaper_writeSelfTestInit.do?testId="+node.id+"&studyrecordId="+studyrecordId, window,
						"dialogWidth:" + screen.availWidth + ";dialogHeight:" + screen.availHeight);
			} else {
				showWarn('前测只能进行一次!',siteY);
			}
		}
	});

	// -------------------展示模块下面的作业前测列表--------------------

	// 定义数据列的数据来源
	var attemptListStore = new Ext.data.JsonStore({
				id : "checkWorkListStore",
				url : "studySpace_findAttemptList.do",
				root : "root",
				fields : ["id", "attemptName", "endTime", "objItem", "subItem", "score","paperid","courseId","testId","testType"]
			});

			
	// 定义列模型
	var attemptListCM = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), {
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
				header : "标题",
				// 数据来源
				dataIndex : "attemptName",
				width : .3,
				sortable : true,
				renderer : function(v, o, r) {
					return "<a href='studySpace_viewAttemptResult?attemptId=" + r.get("id") + "&paperid=" 
							+ r.get("paperid")+"&testId="+r.get("testId") + "&courseId=" + r.get("courseId") + "&testType=" + r.get("testType")
							+ "&studyrecordId=" + studyrecordId + "' target='_blank'>" + r.get("attemptName")
							+ "</a>";
				}
			}, {
				header : "完成日期",// 表头文字
				id : "endTime",
				dataIndex : "endTime",// 数据来源
				width : .2,
				sortable : true
			}, {
				header : "客观题",// 表头文字
				dataIndex : "objItem",// 数据来源
				width : .1,
				sortable : true
			}, {
				header : "主观题",// 表头文字
				dataIndex : "subItem",// 数据来源
				width : .2,
				sortable : true
			}, {
				header : "分数",// 表头文字
				dataIndex : "score",// 数据来源
				width : .2,
				sortable : true
			}]);

	// 定义列表，把数据源和定义的列模型加载进来以显示
	var attemptListGP = new Ext.grid.GridPanel({
				id : "attemptListGP",
				region : "center",// 设置显示内容的位置
				cm : attemptListCM,// 指定列模型
				autoHeight : true,
				closable : true,// 指定为真使其可以关闭
				stripeRows : true,// true是显示行的分隔符，默认会加上这个样式x-grid3-row-alt，自己也可以覆盖
				loadMask : true,// true表示在数据加载过程中会有一个遮盖效果
				loadingText : "加载中...",
				store : attemptListStore,// 指定数据来源
				autoExpandColumn : "attemptName",// grid会把你指定的这列自动扩充来填满空白
				columnLines : true,// true是显示列分隔符
				layout : "fit",// 指定布局模式
				viewConfig : {
					forceFit : true
				},
				renderTo:"viewAttempt"
			});
			
	attemptListStore.load({
		params : {
			moduleId :nodeId,
			studyrecordId : studyrecordId
		},
		callback : function(){
			pageHeightInit();
		}
	});
});