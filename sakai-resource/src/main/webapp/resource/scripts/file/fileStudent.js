/*
 * ! Ext JS Library 3.3.0 Copyright(c) 2006-2010 Ext JS, Inc.
 * licensing@extjs.com http://www.extjs.com/license
 */
Ext.onReady(function() {// 学生访问时只允许 点击下载文件

			Ext.QuickTips.init();
			// 定义码表的树形列表
			var treeWidth = document.body.clientWidth - 10;
			var columnWidth = treeWidth / 10;
			var root = new Ext.tree.AsyncTreeNode({
									text : 'testRoot',
									id : '0'
								})
			var tree = new Ext.ux.tree.ColumnTree({
						id : 'columeTree',
						height : 480,
						width : "100%",
						rootVisible : false,
						autoScroll : true,
						title : '文件资源管理',
						renderTo : Ext.getBody(),
						stripeRows : true,
						columns : [{
									header : '标题',
									width : columnWidth * 6,
									dataIndex : 'text'
								}, {
									header : '最后修改时间',
									width : columnWidth * 1.5,
									dataIndex : 'updateTime'
								}, {
									header : '是否计算平时成绩',
									width : columnWidth * 1.0,
									dataIndex : 'extendOption'
								}, {
									header : '文件大小',
									width : columnWidth * 1.0,
									dataIndex : 'folderSize'
								}, {
									header : '下载次数',
									width : columnWidth * 1.0,
									dataIndex : 'downloadTimes'
								}],

						loader : new Ext.tree.TreeLoader({
									dataUrl : 'file.do',
									uiProviders : {
										'col' : Ext.ux.tree.ColumnNodeUI
									}
								}),
						root : root
					});

			// 给tree增加点击
			tree.on('click', function(node) {
						if (node.leaf && node.attributes.treeNodeType == "file") {
							window.open("file_openURL.do?fileId=" + node.id+"&studyrecordId="+studyrecordId);
						}
						root.reload();
					});

		});