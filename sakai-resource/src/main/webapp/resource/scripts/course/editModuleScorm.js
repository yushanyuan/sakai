/**
 * 添加模块(节点)
 * @param node
 * @param wintitle
 * @return
 */
function addModuleScorm(node,wintitle){
	scrollToTop();
	var siteY = 0;
	
	var wareStore = new Ext.data.SimpleStore({
		fields : ['value', 'text'],
		proxy : new Ext.data.HttpProxy({
			url : 'courseSpace_loadCoursewareBox.do'
		})
	});
	var comboWare = new Ext.form.ComboBox({
		editable : false,
		name : 'comboWare',
		store : wareStore,
		fieldLabel : '选择课件资源',
		hiddenName : 'comboWare',
		emptyText : '-',
		mode : 'remote',
		loadingText : '加载中...',
		triggerAction : 'all',
		valueField : 'value',
		displayField : 'text',
		width : 50,
		anchor : '99%',
		selectOnFocus : true,
		allowBlank : false
	});
	comboWare.on('select',function(combo, record, index){ //Ext.form.ComboBox combo, Ext.data.Record record, Number index 
		var wareId = record.get("value");
		Ext.Ajax.request({
			url: 'courseSpace_checkCourseScorm.do',
		   	success: function(response ,options ){
		   		var dataarr = Ext.decode(response.responseText);
		   		if(dataarr.errorMsg){
		   			showInfo(dataarr.errorMsg,0);
		   		}else{
		   			scormLoad.baseParams.wareId = wareId;
		   			scormRoot.reload();
		   		}
		   	},
		   	failure: function(response ,options ){
		   	},
		   	params: {
		   		wareId : wareId
		   	}
		});
		
	});	
	var scormRoot = new Ext.tree.AsyncTreeNode({
		text : '资源节点树',
		id : '0',
		type : '0'
	});
	var scormLoad = new Ext.tree.TreeLoader({
		dataUrl : 'courseSpace_initModuleScorm.do'
	});
	var wareTree = new Ext.tree.TreePanel({
		title : '课件资源节点列表',
		root : scormRoot,
		loader : scormLoad,
		rootVisible : true,
		frame : true,
		lines : false,
		split : true,
		checkModel: 'cascade',  //对树的级联多选
		useArrows:true,
		animate:true,
		containerScroll: true,
		autoHeight :true,
		autoScroll : true,
		listeners: {
            'checkchange': function(node, checked){
                if(checked){
                    node.getUI().addClass('complete');
                }else{
                    node.getUI().removeClass('complete');
                }
                node.expand();
                node.eachChild(function(child) {  
                    child.ui.toggleCheck(checked);  
                    child.attributes.checked = checked;  
                    child.fireEvent('checkchange', child, checked);  
                });  

            }
        }
	});
	var impForm = new Ext.Panel({		
		title : '从课件资源库引入',
		id:'impForm',
		layout:'form',
		autoHeight : true,
		anchor : '100%',
		lines : false,
		autoScroll : true,
		items : [comboWare,wareTree],
		buttons :[{
			text : '保存',
			handler : function() {
				var ids = ",";
				var pids = ",";
				var selNodes = wareTree.getChecked();
				Ext.each(selNodes, function(selnode){
                    ids += selnode.id + ",";
                    var parentN = selnode.parentNode;
                    while(parentN && parentN!=scormRoot && !parentN.attributes.checked ){
                    	pids += parentN.id + ",";
                    	parentN = parentN.parentNode;
                    }
                });
                if(ids == ","){		
				    mask.hide();
                	showInfo("没有选择节点",siteY);
                	return;
                }
                actMask.show();
                Ext.Ajax.request({
					url: 'courseSpace_importModuleScorm.do',
				   	success: function(response ,options ){
				    	mask.hide();
				    	var resdata = Ext.decode(response.responseText);
				    	if(resdata.success){
				    		showInfo("引入成功",siteY);//显示执行成功提示
				    	} else if(!resdata.success){
				    		showError(resdata.info);//显示执行失败提示	
				    	}
			    		var parent = node.parentNode;
			    		parent.reload();
						moduleWin.close();
						scrollToTop();
				   	},
				   	failure: function(response ,options ){		
				    	mask.hide();
				   	},
				   	params: {
				    	node:node.id, //父节点id
				    	nodeType:node.attributes.type, //父节点类型 
				   		moduleId : ids+";"+pids, //选中节点id集合
				   		courseId : courseNode.id, //课程节点id
				   		wareId :comboWare.getValue() //资源id
				   	}
				});
			},
			listeners :{
				click : function( o, event ) {
					siteY = (event.getXY())[1] - 50;
					mask.show(siteY);
				}
			}
		},{
			text : '取消',
			handler : function() {
				moduleWin.close();
			}		
		}]
	});
	
	var moduleWin = new Ext.Window({
	    layout:'fit',
		y : 100,
		x : 200,
		width:500,
	    autoHeight : true, //必须是false，否则最大化不起作用
	    autoScroll : false,
	    title : wintitle+'添加节点',
	    closeAction:'close',
	    draggable : true,
		resizable : true,
		modal:true,
	    plain: true,
	    items: new Ext.TabPanel({
	        activeTab:0,
			anchor : '100%',
			autoHeight : true,
	        border:false,
	        frame:true,
	        plain:true,
	        items:[impForm],
	        listeners:{
	        }
	    })
	});
	moduleWin.show();
	
	moduleWin.on('close', function() { 
	});
			
	moduleWin.focus();
	
	var mask = new Ext.LoadMask(moduleWin.body, {
		msg : '程序运行中....'
	});
}