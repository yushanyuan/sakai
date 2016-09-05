//设置分数所占平时成绩百分比功能
function setPercent(){ 
	var siteY = 0;
    var reader = new Ext.data.JsonReader({
		totalProperty : 'tatalProperty',
		root : 'root'
	}, 
		[
            {name: 'id', type: 'string'},
            {name: 'type', type: 'string'},
            {name: 'name', type: 'string'},
            {name: 'ratio', type: 'float'}
		]
	);
	var store = new Ext.data.Store({
        reader: reader,
        proxy : new Ext.data.HttpProxy({
            url: 'courseSpace_getActList.do',
            method: 'GET'
        })
    });

    var grid = new Ext.grid.EditorGridPanel({
        store: store,
        viewConfig : {                  //自动计算列宽
			forceFit : true
		},	
        columns: [
            {
                header: '标题',
                width: .6,
                sortable: false,
                dataIndex: 'name',
                hideable: false
            },{
                header: "百分比（"+getHelpImg(path)+"单击单元格可修改百分比数值）",
                width: .4,
                sortable: false,
                dataIndex: 'ratio',
                renderer : function(value,cell,r){
                	if(r.get("id")=="sum"){
                		return "<div style='color:red;font-weight:bold'>"+value+"%</div>";
                	}else{
                		return value + "%";
                	}
                },
                editor: new Ext.form.NumberField({
                   allowBlank: false,
                   allowNegative: false,
                   style: 'text-align:left'
                })
            }
        ],
		loadMask :true,
        frame: false,
        autoHeight:true,
        autoWidth : true,
        autoScroll : false,
        clicksToEdit: 1,
        collapsible: false,
        listeners: {
        	beforeedit: function(e){   
	            var currRecord = e.record;  
	            if (currRecord.get("id") == "sum" ){   //总计不可以设置
	                e.cancel = true;   
	            }
	        },
	        afteredit : function(e){
	        	//e.value是修改后的值，e.originalValue是原始值
	        	if(e.value!=e.originalValue){
	        		var sumRecord = store.getById("sum");
	        		Ext.Ajax.request({
						url: 'courseSpace_acountSum.do',
					   	success: function(response ,options ){
					   		var obj = Ext.decode(response.responseText);
							var ratioSum = obj.sum;
							sumRecord.set("ratio",ratioSum);
					   	},
					   	params: {
					   		addOne :e.value,
					   		addTwo :e.originalValue, 
					   		ratio : sumRecord.get("ratio")
					   	}
					});
	        	}
	        }
        }
    });
	
	var percentWin = new Ext.Window({
	    layout:'form',
	    x:0,
	    y:0,
	    width:document.body.clientWidth-50,
		autoHeight:true,
	    title : '设置分数百分比',
	    closeAction:'close',
	    draggable : false,
		resizable : false,
		modal:true,
	    plain: true,
	    //items: [grid,impressionForm],去掉设置印象分功能
	    items: [grid],
		buttons :[{
			text : '保存',
			handler : function() {
				grid.stopEditing();	//结束表格编辑状态	
				var sumRatio = store.getById("sum").get("ratio");
				if(sumRatio!="100"){//总和不等于100
					showError("百分比总和不等于100，不能保存");
					
				}
				else{//总和等于100，直接提交
					testSubmit(store);
				}
				return;
			},
			listeners :{
				click : function( o, event ) {
					siteY = (event.getXY())[1] - 50;
				}
			}		
		},{
			text : '取消',
			handler : function() {
				percentWin.close();
			}		
		}]
	});
	percentWin.show();
	var mask = new Ext.LoadMask(percentWin.body, {
		msg : '程序运行中....'
	});
	
    store.load({
     	params : {
			courseId : courseNode.id //参数：课程id
		},
		callback : function(){
			//pageHeightInit();
		}
    });
	
	function testSubmit(grid_store){
		var id = "";
		var type = "";
		var ratio = "";
		var dirtyInfo = false;
		var range = grid_store.getRange();
		for(var i=0;i<range.length;i++){
			var record = range[i];
			if(record.get("id")!="sum"){//该条记录有修改
				dirtyInfo = true;
				id += record.get("id") + ",";
				type += record.get("type") + ",";
				ratio += record.get("ratio") + ",";
				if(Number(record.get("ratio"))==0){
					showError("有百分比为0的活动，不能保存");
					return;
				}
			}
		}
		mask.show(siteY);
		Ext.Ajax.request({
			url: 'courseSpace_saveActPercent.do',
		   	success: function(response ,options ){
		   		mask.hide();
		   		showInfo("保存成功",siteY);
		   		percentWin.close();
		   	},
		   	failure: function(response ,options ){
		   	},
		   	params: {
		   		node :id,
		   		nodeType :type,
		   		ratio :ratio,
		   		courseId : courseNode.id,
		   		impressionScore : null,
		   		impressionType : null
		   	}
		});
	}

}
