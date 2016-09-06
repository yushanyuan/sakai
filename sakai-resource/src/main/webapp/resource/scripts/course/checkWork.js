function fitScreen() {
    var sectionfrm = parent.document.getElementById("coursewareFrame");
    var fp1 = $(parent.document).find("iframe")[0];
	var fp2 = $(parent.parent.document).find(".portletMainIframe")[0];
	var extralHeight = 35;
	var mainHeight = $("#checkWorkListGP").height()+50;
	if(mainHeight <200){
		mainHeight = 200;
	}

    $("body").height(mainHeight);

    if (fp1) {
        $(fp1).height($("body").outerHeight()+extralHeight);
        if (fp2) {
            $(fp2).height($(fp1).outerHeight() + 50);
        }
    }
}

var checkWorkListStore;
var searchPanel;

Ext.onReady(function() {//课程空间新---批改作业

			// 启用所有基于标签的提示
			Ext.QuickTips.init();

			var paperstore = new Ext.data.SimpleStore({//简单的数据集，内置数组解析器Ext.data.ArrayReader，读取数组数据。
						fields : ['value', 'text'],//字段与数组间的映射关系
						proxy : new Ext.data.HttpProxy({
									url : 'checkwork_loadPaperBox.do' //加载课程下的作业和前测试卷列表
								})
					});
			// 批改试卷名称下拉菜单 实际为自测和前测的id
			var testAndStestId = new Ext.form.ComboBox({
						editable : false,
						name : 'testAndStestId',
						store : paperstore,
						fieldLabel : '<font style=color:red>*</font>批改试卷名称',
						hiddenName : 'testAndStestId',
						emptyText : '-',
						mode : 'local',
						loadingText : '加载中...',
						triggerAction : 'all',//触发按钮执行all和query
						valueField : 'value',
						displayField : 'text',
						width : 80,
						anchor : '95%',
						selectOnFocus : true,
						allowBlank : false
					});
			paperstore.load();

			var checkStatusBoxStore = new Ext.data.SimpleStore({
						fields : ['value', 'text'],
						data : checkstatusArray
					});
			var checkStatusBox = new Ext.form.ComboBox({//组合框
						editable : false,
						name : 'checkStatus',
						store : checkStatusBoxStore,
						fieldLabel : '<font style=color:red>*</font>批改状态',
						hiddenName : 'checkStatus',
						emptyText : '-',
						mode : 'local',
						value:'0',
						loadingText : '加载中...',
						triggerAction : 'all',
						valueField : 'value',
						displayField : 'text',
						width : 90,
						anchor : '95%',
						selectOnFocus : true,
						allowBlank : false

					});

			// 截止时间
			var endDateDf = new Cls.form.DateTimeField({
						fieldLabel : '<span style="color:red">*</span>截止时间',
						id : 'endTime',
						name : 'endTime',
						format : 'Y-m-d H:i:s',
						editable : false,
						style : 'padding-left:0px;',
						anchor : '95%,95%',
						allowBlank : false,
						listeners : {//监听
							'select' : function(endDateDf, date) {
							}
						}
					});
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
							
									checkWorkListStore.load({
												params : {
													testAndStestId :testAndStestId.getValue(),
													checkStatus : checkStatusBox.getValue(),
													endTime : endDateDf.getValue()
												}
											});
						}
					})
			searchPanel = new Ext.form.FormPanel({//表单面板
						url : 'checkwork_findCheckWorkList.do',
						layout : 'fit',
						region : "north",
						height : 30,
						items : [{
									layout : 'column',
									items : [{// 第一列：
										columnWidth : .25,
										layout : 'form',
										items : [testAndStestId]
									}, {	// 第二列：
												columnWidth : .25,
												layout : 'form',
												items : [checkStatusBox]
											}, {// 第3列：
												columnWidth : .4,
												layout : 'form',
												items : [endDateDf]
											}, {// 第3列：
												columnWidth : .1,
												layout : 'form',
												items : [searchButton]
											}]
								}]
					});

			// 定义列模型
			var checkWorkListCM = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), {
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
						dataIndex : "studentName",
						width : .25,
						sortable : true
					}, {
						header : "学号",// 表头文字
						id : "stuNum",
						dataIndex : "stuNum",// 数据来源
						width : .25,
						sortable : true
					}, {
						header : "中心名称",// 表头文字
						dataIndex : "eduCenter",// 数据来源
						// renderer : sizeRender,
						width : .1,
						sortable : true
					}, {
						header : "成绩",// 表头文字
						dataIndex : "score",// 数据来源
						width : .2,
						sortable : true
					}, {
						header : "类型",// 表头文字
						dataIndex : "paperType",// 数据来源
						width : .2,
						sortable : true,
						renderer : function(v,o,r) {
							if (r.get("paperType")=="1") {
								return "作业";
							} else if(r.get("paperType")=="2"){
								return "前测";
							}
						}
					},{
						header : "操作",// 表头文字
						dataIndex : "operation",// 数据来源
						width : .2,
						sortable : true,
						renderer : function(v,o,r) {
							return "<a href='checkwork_checkObjInit.do?attemptId="+r.get("id")+"&userid="+r.get("userid")+"&paperid="+r.get("paperid")+"&courseid="+r.get("courseid")+"&paperType="+r.get("paperType")+"&studyrecordId="+r.get("studyrecordId")+"' target='_blank'>批改</a>";
						}
					}]);

			// 定义数据列的数据来源
			checkWorkListStore = new Ext.data.JsonStore({
						id : "checkWorkListStore",
						url : "checkwork_findCheckWorkList.do",//查找要批改的作业记录
						root : "root",
						fields : ["id", "studentName", "stuNum", "eduCenter", "score", "operation","userid","paperid","courseid", "paperType","studyrecordId"]
					});

			// 定义列表，把数据源和定义的列模型加载进来以显示
			var checkWorkListGP = new Ext.grid.GridPanel({
						id : "checkWorkListGP",
						region : "center",// 设置显示内容的位置
						cm : checkWorkListCM,// 指定列模型
						autoHeight : true,
						closable : true,// 指定为真使其可以关闭
						stripeRows : true,// true是显示行的分隔符，默认会加上这个样式x-grid3-row-alt，自己也可以覆盖
						loadMask : true,// true表示在数据加载过程中会有一个遮盖效果
						loadingText : "加载中...",
						store : checkWorkListStore,// 指定数据来源
						autoExpandColumn : "courseName",// grid会把你指定的这列自动扩充来填满空白
						columnLines : true,// true是显示列分隔符
						layout : "fit",// 指定布局模式
						viewConfig : {
							forceFit : true
						},
						bbar : new Ext.PagingToolbar({
									id : "checkWorkGridPage",
									pageSize : 20,
									store : checkWorkListStore,
									displayInfo : true
								})
					});

			// 工作区布局
			var viewport = new Ext.Viewport({
						id : 'viewport',
						layout : 'border', // 使用边界布局，页面被分割为东西南北中5个部分
						items : [searchPanel, checkWorkListGP]
					});
			fitScreen();
		});

function checkInit(id) {
	

}