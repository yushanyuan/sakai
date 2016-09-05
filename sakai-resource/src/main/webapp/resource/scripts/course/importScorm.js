var groups = {};

function importScorm(node){
	scrollToTop();
	var siteY = 0;
	var text = new Ext.form.Label({
		text : "课件名称:"
	})
	var searchText = new Ext.form.TextField({
		name : "text"
	});
	var searchButton = new Ext.Button({
		text : "查询",// 页面上显示的文本
		iconCls : "folder_find",// 图标路径，一般menu,button会有这个配置(属性)
		handler : function() {
			grid_store.load({
				params : {
					courseName : searchText.getValue(),
					status : "1"
				}
			});
		}
	});
	var searchPanel = new Ext.Toolbar({
		autoHeight:true,
		autoScroll:false,
		items :[text,searchText,searchButton]
	})
	
	var grid_store = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({//后台返回JSON格式的数据
			url : 'courseware_findByName.do'//查询课件资源action
		}),
		remoteStore : true, //允许远程排序
		pruneModifiedRecords : true, // 清除store中modified记录
		//reader用于将原始数据转换成Record实例
		reader : new Ext.data.JsonReader({
			totalProperty : 'total',
			root : 'root'
		}, 
			[
			 {name : 'id'},  //资源id
			 {name : 'courseName'}, //资源名称
			 {name : 'moduleIds'}, //要提交的模块id集合
			 {name : 'fileUrl'} //路径
			]
		),
		listeners :{
			load : function(){
				////pageHeightInit();
			}
		}
	});

    var expander = new Ext.ux.grid.RowExpander();
    var cm = new Ext.grid.ColumnModel({
        columns: [
            expander,
            {	
            	id:'id',
            	header: "id", 
            	dataIndex: 'id',
				sortable : false,
				hidden : true,
				hideable : false
            },
            {
            	header: "资源名称（"+getHelpImg(path)+"单击<img src=\""+path+"/resource/images/default/tree/elbow-plus-nl.gif\">展开资源的节点列表）", 
            	dataIndex: 'courseName',
				width : .8,
				sortable : false
            },
            {
            	header : '操作',
            	xtype: 'actioncolumn',
                width: .2,
                items: [{
                    icon   : path+ '/resource/icons/arrow_undo.png',  // Use a URL in the icon config
                    tooltip: '导入资源',
                    handler: function(grid, rowIndex, colIndex) {
                        var rec = grid_store.getAt(rowIndex);
                        var wareId = rec.get('id');
                        var oName = rec.get('courseName');
                        var oMod = groups[wareId];
						var mods = ",";
						if(oMod!=undefined){
							var values = oMod.getValue();
							for(var i=0;i<values.length;i++){
								mods += values[i].getRawValue() + ",";
							}
						}
						if(mods==","){
							showInfo("没有可以导入的节点",siteY);
						}
						else{
							showQuestions("您确定要导入资源"+oName+"?",siteY,function(btn){
								if (btn == 'ok') {//确认继续操作
									setImportScormAttributes(wareId,mods);
									importWin.close();
								}
							});
						}
                    }
                }]
            }
        ]
    })
	var grid = new Ext.grid.GridPanel({
        store: grid_store,
        cm: cm,
		loadMask :true,
        viewConfig: {
            forceFit:true
        },        
        autoHeight:true,
        autoScroll : false,
        plugins: expander,
        collapsible: false,
        animCollapse: false,
        title: '资源列表',
        tbar : searchPanel,
        bbar : new Ext.PagingToolbar({
			id : "resourceGridPage",
			pageSize : 20,
			store : grid_store,
			displayInfo : true
		}),
        listeners :{
			click : function( event ) {
				siteY = (event.getXY())[1] - 50;
			}
		}
    });
    
    var importWin = new Ext.Window({
    	layout:'form',
    	y:0,
	    width:600,		
		autoHeight:true,
		autoScroll:false,
	    title : '导入课件资源',
	    closeAction:'close',
	    draggable : false,
		resizable : false,
		modal:true,//True 表示为当window显示时对其后面的一切内容进行遮罩
	    plain: true,//True表示为渲染window body的背景为透明的背景，这样看来window body与边框元素（framing elements）融为一体， false表示为加入浅色的背景，使得在视觉上body元素与外围边框清晰地分辨出来（默认为false）。
	    items: [grid],
	    border :false,//True表示为显示出面板body元素的边框，false则隐藏
	    hideBorders :false,
	    frame :true,
	    buttons:[{
   	 		text : '关闭',
			iconCls : "np-reject",
			handler : function() {
				importWin.close();
				//pageHeightInit();
   	 		}
	    }]
	});
	importWin.show();
	
	grid_store.load({
		params : {
			courseName : searchText.getValue(),
			status : "1"
		}
	});
	grid_store.on('beforeload',function(){
	    grid_store.baseParams = {
			courseName : searchText.getValue(),
			status : "1"
		}
	}); 	
}

function setImportScormAttributes(wareId,mods){
	scrollToTop();
	var siteY = 0;
	var conn = new Ext.data.Connection({
	    url: "courseSpace_importScorm.do",
	    timeout : 1800000 //自定义超时时间，这里是30分钟 (默认30s)
	});

	//定义表格数据源
	var gstore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy(conn),
		remoteStore : false, 
		pruneModifiedRecords : true, // 清除store中modified记录
		reader : new Ext.data.JsonReader({
			totalProperty : 'tatalProperty',
			root : 'root'
		}, 
			[
			 {name : 'ico'},  //图标
			 {name : 'title'},  //标题
			 {name : 'required'}, //必修
			 {name : 'studyTime'},    //学习时间
			 {name : 'prerequids'},   //开启条件
			 //{name : 'minSecNum'}, //最少选修个数
			 {name : 'id'},//id
			 {name : 'pid'},//pid
			 {name : 'nodeType'},//类型
			 {name : 'childType'}//类型
			]
		)
	});
	
	
	// 定义表格列信息
	var gCM = new Ext.grid.ColumnModel([
		{
			header : '标题',    
			width : .5,           //宽度百分比
			dataIndex : 'title',//对应的记录集字段,
			renderer : function(v,o,r){//列的渲染函数
				var ico = r.get("ico");
				return ico + v;
			},
			editor:new Ext.form.TextField({
			})
		}, 
		{
			header : '属性',
			width : .1,
			dataIndex : 'required',
			renderer : function(v){//列的渲染函数
				return getValueByIdFromArr(requiredArray,v);
			},
			editor : new Ext.form.ComboBox({
				store : new Ext.data.SimpleStore({
					fields : ['value', 'text'],
					data : requiredArray
				}),
				mode : 'local',
				triggerAction : 'all',
				valueField : 'value',
				displayField : 'text',
				selectOnFocus : true,
				allowBlank : false
			})
		}, 
		{
			header : '建议学习时长',
			width : .13,
			dataIndex : 'studyTime',
			renderer : function(v,o,r){//列的渲染函数
				return v + "分钟";
				
			},
			editor : new Ext.form.NumberField({
				allowNegative : false,
				allowDecimals : false
			})
		}
	]);
	
	
	var setGrid = new Ext.grid.EditorGridPanel({// 表格容器定义
		loadMask : true,               //读取数据时的提示功能
		store : gstore,             //数据源
		cm : gCM,                        //列信息			
		viewConfig : {                  //自动计算列宽
			forceFit : true
		},			
		border : false,
	    title : "设置导入课件的属性（"+getHelpImg(path)+"单击单元格可对数据进行修改）",
		clicksToEdit:1,
		stripeRows : true,
		autoScroll : true,
		height : 600,
		listeners: {   
	        beforeedit: function(e){   
	            var currRecord = e.record;   
	            var colNum = e.column;
	            if (currRecord.get("nodeType") == "3" && colNum==4){   //页不可以设置最少选修个数
	                e.cancel = true;   
	            }
	        },
	        afteredit : function(e){
	        	if(e.column==1 && e.value!=e.originalValue){ //属性列的值发生变化
	        		var r = e.record; //获取当条记录
	        		var pid = r.get("pid"); //获取上级id
	        		if(pid!=""){ 
	        			var pr = gstore.getById(pid);
	        		}
	        		
	        	}
	        },
	        validateedit : function(e){
	        }
	    }  
	});
	var setWin = new Ext.Window({
	    y:0,
	    width:600,
	    autoHeight:true,
	    autoScroll : true,
	    closeAction:'close',
	    draggable : false,
		resizable : false,
		modal:true,
	    plain: true,
	    layout : 'form',
	    items: [setGrid],
		buttons :[{
			text : '保存',
			handler : function() {
				setGrid.stopEditing();
				var rs = gstore.getModifiedRecords(); //获取修改记录
				var ids = ""; //id集合
				var requireds = ""; //属性
				var studyTimes = ""; //学习时长
				var prerequidses = ""; //开启条件
				var nodeTypes = ""; //节点类型
				var childTypes = "";//下级节点类型
				var titles = "";
				for(var i=0;i<rs.length ;i++){
					var r = rs[i];
					var id = r.get("id");
					var required = r.get("required");
					var studyTime = r.get("studyTime");
					ids += id + ",";
					requireds += required + ",";
					studyTimes += studyTime + ",";
					prerequidses += r.get("prerequids") + ",";
					nodeTypes += r.get("nodeType") + ",";
					titles += r.get("title") + ",";
					if(r.get("nodeType")=="2"){
						childTypes += r.get("childType") + ",";
					}else{
						childTypes += "3,";
					}
					
				}
				if(ids != ""){
					mask.show(siteY);
					Ext.Ajax.request({
						url: 'courseSpace_setScormAttr.do',
					   	success: function(response ,options ){
					   		mask.hide();
					   		showInfo("保存成功",siteY);
					   		setWin.close();
					   	},
					   	failure: function(response ,options ){
					   	},
					   	params: {
					   		nodeType :nodeTypes, 
					   		node :ids, 
					   		required : requireds, 
					   		studyTime : studyTimes,
					   		prerequids : prerequidses,
					   		childType : childTypes,
					   		courseId:courseNode.id,
					   		title : titles
					   	}
					});
				}else{
					showInfo("没有修改任何属性，窗口自动关闭",siteY);
					setWin.close();
				}
			},
			listeners :{
				click : function( o, event ) {
					siteY = (event.getXY())[1] - 50;
				}
			}		
		},{
			text : '取消',
			handler : function() {
				setWin.close();
			}		
		}],
		listeners :{
			beforeclose:function(p){
				courseNode.parentNode.reload();
			}
		}
	});
	setWin.show();
	
	gstore.baseParams = {//是一个JSON对象，里面的数据会作为参数发送给后台
		node :wareId,
		moduleId :mods,
		courseId : courseNode.id
	};
	
	var mask = new Ext.LoadMask(setWin.body, {
		msg : '程序运行中....'
	});
	
	gstore.load({
		callback: function(){
			//pageHeightInit();
		}
	});
	
}

Ext.ns('Ext.ux.grid');
Ext.ux.grid.RowExpander = Ext.extend(Ext.util.Observable, {
    header : '',
    width : 20,
    sortable : false,
    fixed : true,
    hideable: false,
    menuDisabled : true,
    dataIndex : '',
    id : 'expander',
    lazyRender : true,
    enableCaching : true,
    constructor: function(config){
        Ext.apply(this, config);

        this.addEvents({
            beforeexpand: true,
            expand: true,
            beforecollapse: true,
            collapse: true
        });

        Ext.ux.grid.RowExpander.superclass.constructor.call(this);

        this.state = {};
        this.bodyContent = {};
    },

    getRowClass : function(record, rowIndex, p, ds){
        return 'x-grid3-row-collapsed';
    },

    init : function(grid){
        this.grid = grid;
        var view = grid.getView();
        view.getRowClass = this.getRowClass.createDelegate(this);
        view.enableRowBody = true;
        grid.on('render', this.onRender, this);
    },

    onRender: function() {
        var grid = this.grid;
        var mainBody = grid.getView().mainBody;
        mainBody.on('mousedown', this.onMouseDown, this, {delegate: '.x-grid3-row-expander'});
    },

    onMouseDown : function(e, t){
        e.stopEvent();
        var row = e.getTarget('.x-grid3-row');
        if(typeof row == 'number'){
            row = this.grid.view.getRow(row);
        }
        this[Ext.fly(row).hasClass('x-grid3-row-collapsed') ? 'expandRow' : 'collapseRow'](row);
    },

    renderer : function(v, p, record){
    	this.state[record.id] = undefined;
        p.cellAttr = 'rowspan="2"';
        return '<div class="x-grid3-row-expander">&#160;</div>';
    },

    expandRow : function(row){
    	var mask = new Ext.LoadMask(this.grid.getView().mainBody, {
			msg : '读取数据中....'
		});
		mask.show();
        if(typeof row == 'number'){
            row = this.grid.view.getRow(row);
        }
        var record = this.grid.store.getAt(row.rowIndex);
        var body = Ext.DomQuery.selectNode('tr:nth(2) div.x-grid3-row-body', row);
        if(this.state[record.id] == undefined){
	    	Ext.Ajax.request({
				url: 'courseSpace_getRootNodeByWareId.do',
			   	success: function(response ,options ){
			   		var dataarr = Ext.decode(response.responseText);
			   		if(dataarr.errorMsg){
			   			mask.hide();
			   			showInfo(dataarr.errorMsg,0);
			   			return;
			   		}
			   		var moduleData = new Ext.ux.CheckGroupData({
						baseData : dataarr,
						name : 'moduleId',
						checkType : 'all'
					});
			   		var checkModule = new Ext.form.CheckboxGroup({
			   			columns: 1,
			   			renderTo : body,
			   			items:moduleData
			   		});
		            Ext.fly(row).replaceClass('x-grid3-row-collapsed', 'x-grid3-row-expanded');
		            groups[record.id] = checkModule;
		            //pageHeightInit();
		            mask.hide();
		            this.state[record.id] = true;   	
			   	},
			   	failure: function(response ,options ){
			   	},
			   	params: {
			   		node : record.id
			   	}
			});
        }
        else{
        	Ext.fly(row).replaceClass('x-grid3-row-collapsed', 'x-grid3-row-expanded');		         
        }
    },

    collapseRow : function(row){
        if(typeof row == 'number'){
            row = this.grid.view.getRow(row);
        }
        var record = this.grid.store.getAt(row.rowIndex);
        var body = Ext.fly(row).child('tr:nth(1) div.x-grid3-row-body', true);
        Ext.fly(row).replaceClass('x-grid3-row-expanded', 'x-grid3-row-collapsed');
        this.fireEvent('collapse', this, record, body, row.rowIndex);
    }
});

Ext.preg('rowexpander', Ext.ux.grid.RowExpander);

Ext.grid.RowExpander = Ext.ux.grid.RowExpander;