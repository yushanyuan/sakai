/**
 * 添加页
 * @param node
 * @param wintitle
 * @return
 */
function addSectionScorm(node,wintitle){
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
		dataUrl : 'courseSpace_initSectionScorm.do'
	});
	var wareTree = new Ext.tree.TreePanel({
		title : '课件资源页列表',
		root : scormRoot,
		loader : scormLoad,
		rootVisible : true,
		frame : true,
		lines : false,
		split : true,
		useArrows:true,
		animate:true,
		containerScroll: true,
		autoScroll : true,
		listeners: {
            'checkchange': function(node, checked){
                if(checked){
                    node.getUI().addClass('complete');
                }else{
                    node.getUI().removeClass('complete');
                }
            }
        }
	});
	var impForm = new Ext.Panel({		
		title : '从课件资源库引入',
		layout:'form',
		anchor : '99%',
		lines : false,
	    height: 300,
	    autoScroll : true,
		items : [comboWare,wareTree],
		buttons :[{
			text : '保存',
			handler : function() {
				var ids = "";
				var texts = "";
				var selNodes = wareTree.getChecked();
				Ext.each(selNodes, function(selnode){
                    ids += selnode.id + ",";
                    texts += selnode.text + ",";
                });
                if(ids == ""){
                	showInfo("没有选择页",siteY);
                	return;
                }
                mask.show(siteY);
                Ext.Ajax.request({
					url: 'courseSpace_importSectionScorm.do',
				   	success: function(response ,options ){
				   		mask.hide();
						showInfo("引入成功",siteY);//显示执行成功提示
			    		node.parentNode.reload();
						sectionWin.close();
				   	},
				   	failure: function(response ,options ){
				   	},
				   	params: {
				   		moduleId:node.attributes.id, //模块id
				   		node : ids, //选中节点id集合
				   		title : texts,
				   		courseId : courseNode.id, //课程节点id
				   		wareId :comboWare.getValue() //资源id
				   	}
				});
			},
			listeners :{
				click : function( o, event ) {
					siteY = (event.getXY())[1] - 50;
				}
			}
		
		},{
			text : '取消',
			handler : function() {
				sectionWin.close();
			}
		}]
	});
	var sectionWin = new Ext.Window({
	    layout:'fit',
		y : 100,
		x : 200,
		width:500,
	    autoHeight : true,
	    autoScroll : false,
	    title : wintitle+'添加页',
	    closeAction:'close',
	    draggable : true,
		resizable : true,
		modal:true,
	    plain: true,
	    items: new Ext.TabPanel({
	        activeTab:0,
			anchor : '99%',
	        border:false,
	        frame:true,
	        plain:true,
	        items:[impForm]
	    })
	});
	sectionWin.show();
	
	sectionWin.on('close', function() {

	});

	sectionWin.focus();
	
	var mask = new Ext.LoadMask(sectionWin.body, {
		msg : '程序运行中....'
	});
}
