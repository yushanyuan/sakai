function fitScreen() {
    var sectionfrm = parent.document.getElementById("coursewareFrame");
    var fp1 = $(parent.document).find("iframe")[0];
	var fp2 = $(parent.parent.document).find(".portletMainIframe")[0];
	var extralHeight = 35;
    $("body").height(50+$("#studentListGP").height());
    if (fp1) {
        $(fp1).height($("body").outerHeight()+extralHeight);
        if (fp2) {
            $(fp2).height($(fp1).outerHeight() + 50);
        }
    }
}

var studentListStore;
// var checkWorkToolbar;
var searchPanel;
/**
 * 设置印象分功能
 */
Ext.onReady(function() {

			// 启用所有基于标签的提示
			Ext.QuickTips.init();
			//从后台获得中心名称数据
			var eduCenterStore = new Ext.data.SimpleStore({
						fields : ['value', 'text'],
						proxy : new Ext.data.HttpProxy({
									url : 'courseSpace_loadEduCenterBox.do'
								})
					});
			// 中心名称下拉菜单 实际为中心id
			var eduCenterId = new Ext.form.ComboBox({
						editable : false,
						name : 'eduCenterId',
						store : eduCenterStore,
						fieldLabel : '中心名称',
						hiddenName : 'eduCenterId',
						emptyText : '-',
						mode : 'local',
						loadingText : '加载中...',
						triggerAction : 'all',
						valueField : 'value',
						displayField : 'text',
						width : 150,
						anchor : '95%',
						selectOnFocus : true,
						allowBlank : true
					});
			eduCenterStore.load();//加载中心名称数据

			// 学生姓名输入文本框
			var stuName = new Ext.form.TextField({
						fieldLabel : "学生姓名",
						id : "stuName",
						name : "stuName",
						allowBlank : true
					});

			// 学生学号输入文本框
			var stuNum = new Ext.form.TextField({
						fieldLabel : "学号",
						id : "stuNum",
						name : "stuNum",
						allowBlank : true
					});
			//查询按钮
			var searchButton = new Ext.Button({
						// 页面上显示的文本
						text : "查询",
						id : "find",
						height : 25,
						// 图标路径，一般menu,button会有这个配置(属性)
						iconCls : "folder_find",
						// 定义出发按钮所要执行的操作
						handler : function() {
							if (!searchPanel.getForm().isValid()) {
								showInfo("查询条件不能为空");
								return;
							}

							studentListStore.load({
										params : {
											eduCenterId : eduCenterId.getValue(),
											stuName : stuName.getValue(),
											stuNum : stuNum.getValue()
										}
									});
						}
					})
			searchPanel = new Ext.form.FormPanel({
						url : 'courseSpace_findImpScoreStudents.do',
						layout : 'fit',
						region : "north",
						height : 30,
						items : [{
									layout : 'column',
									items : [{// 第一列：
										columnWidth : .3,
										layout : 'form',
										items : [eduCenterId]
									}, {	// 第二列：
												columnWidth : .3,
												layout : 'form',
												items : [stuName]
											}, {// 第3列：
												columnWidth : .3,
												layout : 'form',
												items : [stuNum]
											}, {// 第3列：
												columnWidth : .1,
												layout : 'form',
												items : [searchButton]
											}]
								}]
					});

			// 定义资源列表的选择框的选择模型--单选还是多选，现在设置的是多选
			var checkBoxSM = new Ext.grid.CheckboxSelectionModel({
						singleSelect : false
					});

			// 定义列模型
			var studentListCM = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), checkBoxSM, {
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
						header : "姓名",
						// 数据来源
						dataIndex : "stuName",
						width : .25,
						sortable : true
					}, {
						header : "成绩",// 表头文字
						dataIndex : "score",// 数据来源
						width : .1,
						sortable : true
					}, {
						header : "中心名称",// 表头文字
						dataIndex : "eduCenter",// 数据来源
						width : .25,
						sortable : true
					}, {
						header : "学号",// 表头文字
						id : "stuNum",
						dataIndex : "stuNum",// 数据来源
						width : .2,
						sortable : true
					}]);

			// 定义数据列的数据来源
			studentListStore = new Ext.data.JsonStore({
						id : "studentListStore",
						url : "courseSpace_findImpScoreStudents.do",
						root : "root",
						fields : ["id", "stuName", "score", "stuNum", "eduCenter", "userid", "courseid",
								"studyrecordId"]
					});

			// 教师印象分输入文本框
			var impressionScoreSet = new Ext.form.NumberField({
				name : 'impressionScoreSet',
				fieldLabel : '印象分数',
				value : '10',
				width : 150,
				allowNegative : true,// 能输入负数
				allowDecimals : true// 能输入小数
				});

			// 加分按钮
			var addScoreButton = new Ext.Button({
						text : "加分",// 页面上显示的文本
						iconCls : "np-accept",// 图标路径，一般menu,button会有这个配置(属性)
						handler : function() {
							var rows = studentListGP.getSelectionModel().getSelections();
							if (rows.length == 0) {
								showInfo("没有选择记录");
							} else {
								showQuestions('确定要添加印象分?',null, function(btn) {
											if (btn == 'ok') {
												var ids = "";
												var scores = "";
												for (var i = 0; i < rows.length; i++) {
													var record = rows[i];
													var studyRecordId = record.get('id');
													var score = record.get('score');
													ids += studyRecordId + ",";
													scores += score + ",";
												}
												Ext.Ajax.request({
															url : 'courseSpace_addImpScore.do',
															success : function(response, options) {
																var obj = Ext.decode(response.responseText);
																if (obj.result == true) {
																	showInfo(obj.data);
																} else {
																	showError(obj.msg);
																}
																studentListStore.reload();
															},
															failure : function(response, options) {
															},
															params : {
																selectionIds : ids,
																scores : scores,
																impScore : impressionScoreSet.getValue()
															}
														});
											}
										})
							}
						}
					});

			// 获得选中的课件的ID集合
			function getSelectionId(selections) {
				var arry = new Array();
				var i = 0;
				Ext.each(selections, function(item) {// 遍历record数组
							arry[i] = item.data.id;// 将遍历出来的某个record加载到数组中
							i++;
						});
				return arry;
			}

			// 工具栏
			var addScorePanel = new Ext.Toolbar({
						// region : 'north',
						autoHeight : true,
						autoScroll : false,
						items : [new Ext.form.Label({ text:"印象分数:"}),impressionScoreSet, addScoreButton]
					});

			// 定义列表，把数据源和定义的列模型加载进来以显示
			var studentListGP = new Ext.grid.GridPanel({
						id : "studentListGP",
						region : "center",// 设置显示内容的位置
						cm : studentListCM,// 指定列模型
						sm : checkBoxSM,
						autoHeight : true,
						closable : true,// 指定为真使其可以关闭
						stripeRows : true,// true是显示行的分隔符，默认会加上这个样式x-grid3-row-alt，自己也可以覆盖
						loadMask : true,// true表示在数据加载过程中会有一个遮盖效果
						loadingText : "加载中...",
						store : studentListStore,// 指定数据来源
						autoExpandColumn : "courseName",// grid会把你指定的这列自动扩充来填满空白
						columnLines : true,// true是显示列分隔符
						layout : "fit",// 指定布局模式
						viewConfig : {
							forceFit : true
						},
						tbar : addScorePanel,
						bbar : new Ext.PagingToolbar({
									id : "studentGridPage",
									pageSize : 20,
									store : studentListStore,
									displayInfo : true
								})
					});

			// 工作区布局
			var viewport = new Ext.Viewport({
						id : 'viewport',
						layout : 'border', // 使用边界布局，页面被分割为东西南北中5个部分
						items : [searchPanel, studentListGP]
					});


			fitScreen();
		});

function checkInit(id) {

}