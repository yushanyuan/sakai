function fitScreen() {
    var fp1 = $(parent.document).find("iframe")[0];
	var fp2 = $(parent.parent.document).find("#ifrm_c_statistic")[0];
	var fp3 = $(parent.parent.parent.document).find(".portletMainIframe")[0];
    $("body").height(50+$("#panel").height());
    $(fp1).height($("body").outerHeight()+30);
	if($(fp2).height() < 470){
    	$(fp2).height(470);
	}
    if($(fp3).height() < 470){
    	$(fp3).height(470);
	}    
}

// 开启提示功能
Ext.QuickTips.init();

Ext.onReady(function(){
		
		//活动名称数据加载
		var actSimpleStore = new Ext.data.SimpleStore({
			fields: ["value","text"],
			proxy : new Ext.data.HttpProxy({
									url : 'statistics_findActiveNameBox.do'
								})
		});
		
		//活动名称下拉框
		var actNameCB = new Ext.form.ComboBox({
			fieldLabel: "<font color='red'>*</font>活动名称",
			xtype: "combo",
			name: "activeName",
			hiddenName: "activeName",//制定了这个名称则会动态生成一个以指定名称命名的隐藏域来存放数据
			emptyText: "---请选择---",
			mode: "local",//表示数据是从本地读取，默认是remote从服务器上读取
			valueField: "value",//这个值必须和store对应的fields里的值对应
			displayField: "text",//这个值必须和store对应的fields里的值对应
			store:actSimpleStore,
			anchor : '99%',
			triggerAction : 'all',
			grow: true,//根据内容自动伸缩
			editable: false,//设置下拉框是否可编辑，默认为true
			selectOnFocus : true,
			allowBlank : false
		});	
		
		//状态下拉框数据
		var status = new Ext.data.SimpleStore({
			fields: ["key","state"],
			data: passStatus
		});
		
		//状态下拉框
		var statusCB = new Ext.form.ComboBox({
			fieldLabel: "状态",
			//id: "statusid",
			xtype: "combo",
			name: "status",
			hiddenName: "status",//制定了这个名称则会动态生成一个以指定名称命名的隐藏域来存放数据
			emptyText: "---请选择---",
			mode: "local",//表示数据是从本地读取，默认是remote从服务器上读取
			valueField: "key",//这个值必须和store对应的fields里的值对应
			anchor : '70%',
			displayField: "state",//这个值必须和store对应的fields里的值对应
			store:status,
			selectOnFocus : true,
			triggerAction : 'all',
			selectOnFocus : true,
			editable: false//设置下拉框是否可编辑，默认为true
		});
		
		//运算符下拉框数据
		var timesLimit = new Ext.data.SimpleStore({
			fields: ["key","compare"],
			data: compareStatusArray
		});
		
		//运算符下拉框
		var timesLimitCB = new Ext.form.ComboBox({
			//id: "timesLimit",
			xtype: "combo",
			name: "timesLimit",
			hiddenName: "timesLimit",//制定了这个名称则会动态生成一个以指定名称命名的隐藏域来存放数据
			emptyText: "---请选择---",
			mode: "local",//表示数据是从本地读取，默认是remote从服务器上读取
			valueField: "key",//这个值必须和store对应的fields里的值对应
			displayField: "compare",////这个值必须和store对应的fields里的值对应
			store:timesLimit,
			triggerAction : 'all',
			anchor : '99%',
			value: "1",//默认显示第一个
			selectOnFocus : true,
			editable: false//设置下拉框是否可编辑，默认为true
		});
		
		//查询活动成绩里的查询选项
		var formP = new Ext.form.FormPanel({
			labelAlign: "right",
			frame: true,
			autoHeight: true,
			labelWidth: 80,
			bodyStyle: "padding:5px",
			items : [{
				layout : 'column',
				items : [{//第一列
					columnWidth : .33,
					layout : 'form',
					items : [actNameCB,statusCB]
				}, {//第二列
					columnWidth : .33,
					layout : 'form',
					items : [{
							fieldLabel: "<font color='red'>*</font>最低分数",
							id: "minScore",
							allowBlank: false,//不允许为空
							allowDecimals: false,//不允许是小数
							allowNegative: false,//不允许为负数
							maxText: "输入值不能大于100",
							minText: "输入值不能小于0",
							nanText: "只能输入正整数",
							anchor : '99%',
							minValue: 0,//最小值
							maxValue: 100,//最大值
							xtype: "numberfield",
							name: "minScore",
							value: "0"
						}
						,{
	                xtype: 'compositefield',
	                fieldLabel: "<font color='red'>*</font>次数限制",
	                msgTarget : 'side',
	                anchor    : '-20',
	                defaults: {
	                    flex: 1
	                },
	                items: [timesLimitCB,
	                    {
							xtype: "numberfield",
							id: "times",
							allowBlank: false,//不允许为空
							allowDecimals: false,//不允许是小数
							allowNegative: false,//不允许为负数
							minText: "输入值不能小于0",
							nanText: "只能输入正整数",
							minValue: 0,//最小值
							name: "times",
							width: 100
						}
	                ]
	            }
						]
				}, {//第三列
					columnWidth : .34,
					layout : 'form',
					items : [{
							fieldLabel: "<font color='red'>*</font>最高分数",
							id: "maxScore",
							allowBlank: false,//不允许为空
							anchor : '99%',
							allowDecimals: false,//不允许是小数
							allowNegative: false,//不允许为负数
							blankText: "不能为空",
							maxText: "输入值不能大于100",
							minText: "输入值不能小于0",
							nanText: "只能输入正整数",
							minValue: 0,//最小值
							maxValue: 100,//最大值
							xtype: "numberfield",
							name: "maxScore",
							value: 100
						},{
							text: "查询",
							id: "select",
							xtype: "button",
							name: "select",
							handler: function(){
								if(!formP.getForm().isValid()){
									showInfo("带星号的都必填！");
									return;
								}
								actStore.baseParams = {//是一个JSON对象，里面的数据会作为参数发送给后台
									activeName : actNameCB.getValue(),
									status : statusCB.getValue(),
									minScore : formP.findById("minScore").getValue(),
									times : Ext.getCmp("times").getValue(),
									timesLimit: timesLimitCB.getValue(),
									maxScore : formP.findById("maxScore").getValue()
								};

								actStore.load();
							}
						}]
				}]
			}]
		});
		
		//加载下拉列表框数据
		actSimpleStore.load();
		
		// 定义列模型
		var actCM = new Ext.grid.ColumnModel([{
					// 表头显示的文字
					header : "姓名",
					// 数据来源
					dataIndex : "name",
					//width : .25,
					sortable : true
				}, {
					header : "学号",// 表头文字
					id : "publicstunum",
					dataIndex : "publicstunum",// 数据来源
					//width : .25,
					sortable : true
				}, {
					header : "中心名称",// 表头文字
					id : "organizationName",
					dataIndex : "organizationName",// 数据来源
					//width : .25,
					sortable : true
				}, {
					header : "手机号",// 表头文字
					id : "cellphone",
					dataIndex : "cellphone",// 数据来源
					//width : .25,
					sortable : true
				}, {
					header : "固定电话",// 表头文字
					id : "telephone",
					dataIndex : "telephone",// 数据来源
					//width : .25,
					sortable : true
				}, {
					header : "Email",// 表头文字
					id : "email",
					dataIndex : "email",// 数据来源
					//width : .25,
					sortable : true
				}, {
					header : "次数",// 表头文字
					id : "attemptNumber",
					dataIndex : "attemptNumber",// 数据来源
					//width : .25,
					sortable : true
				}, {
					header : "状态",// 表头文字
					id : "status",
					dataIndex : "status",// 数据来源
					//width : .25,
					sortable : true
				}, {
					header : "成绩",// 表头文字
					id : "score",
					dataIndex : "score",// 数据来源
					//width : .25,
					sortable : true
				}
		]);
		
		// 定义数据列的数据来源,DownTimes表示资源下载次数
		var actStore = new Ext.data.JsonStore({
			id : "actStore",
			url : "statistics_findActivityScore.do?isShow=true",
			root : "root",
			fields : ["name", "publicstunum","organizationName", "cellphone","telephone", "email","attemptNumber", "status","score"]
		});
		
		//查询活动成绩右半部分下面那个表格
		var findActGP = new Ext.grid.GridPanel({
			id : "findActGP",
			region : "center",// 设置显示内容的位置
			cm : actCM,// 指定列模型
			autoHeight : true,
			stripeRows : true,// true是显示行的分隔符，默认会加上这个样式x-grid3-row-alt，自己也可以覆盖
			loadMask : true,// true表示在数据加载过程中会有一个遮盖效果
			loadingText : "加载中...",
			store : actStore,// 指定数据来源
			autoExpandColumn : "organizationName",// grid会把你指定的这列自动扩充来填满空白
			columnLines : true,// true是显示列分隔符
			viewConfig : {
				forceFit : true
			},
			bbar : new Ext.PagingToolbar({
						id : "findActGridPage",
						pageSize : 20,
						store : actStore,
						displayInfo : true
					})
		});
		
		//右边的主面板
    	var panel = new Ext.Panel({
			id: "panel",
     		split: true,
     		renderTo : Ext.getBody(),
       		region:'center',
     		items:[formP,findActGP]
    	});

    fitScreen();
})