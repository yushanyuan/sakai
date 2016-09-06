function resourcePreview(node){
	var siteY = 0;
	var wareId = node[0].data.id;
	var scormRoot = new Ext.tree.AsyncTreeNode({
		text : '资源节点树',
		id : '0',
		type : '0'
	});
	var scormLoad = new Ext.tree.TreeLoader({
		dataUrl : 'courseware_initPreviewTree.do'
	});
	
	var wareTree = new Ext.tree.TreePanel({
		title : '课件资源节点列表',
		root : scormRoot,
		loader : scormLoad,
		rootVisible : true,
		frame : true,
		lines : false,
		split : true,
		useArrows:true,
		animate:true,
		containerScroll: true,
		height:400,
		autoHeight :false,
		autoScroll : true,
		listeners: {
			"load":function(node){
				if(node.childNodes && node.childNodes.length>0){
					for(i = 0; i < node.childNodes.length; i++){
						node.childNodes[i].attributes.checked = null;
						if(node.childNodes[i].leaf){
							node.childNodes[i].text = node.childNodes[i].text+"<span style='color:#1562B0'>预览</span>";
						}
					}
				}
			},
			'click': function(node, e) {
		        if (node.isLeaf()) {
		        	window.open(node.attributes.url);
		        }
		    }    
        }
	});
	var moduleWin = new Ext.Window({
	    layout:'fit',
		width:500,
	    autoHeight : true, //必须是false，否则最大化不起作用
	    autoScroll : false,
	    title : node[0].data.courseName+'课件预览',
	    closeAction:'close',
	    draggable : true,
		resizable : false,
		modal:true,
	    plain: true,
	    items: new Ext.TabPanel({
	        activeTab:0,
			anchor : '100%',
			autoHeight : true,
	        border:false,
	        frame:true,
	        plain:true,
	        items:[wareTree],
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
	mask.show();
	Ext.Ajax.request({
		url: 'courseware_createPreView.do',
	   	success: function(response ,options ){
	   		var dataarr = Ext.decode(response.responseText);
	   		mask.hide();
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
}