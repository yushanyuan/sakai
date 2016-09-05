function setAttributes(records){
	var siteY = 0;
	var gstore = new Ext.data.Store({
		remoteStore : true,
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalProperty',
			root : 'root'
		}, [{name : 'ico'},  //图标
			 {name : 'title'},  //标题
			 {name : 'required'}, //必修
			 {name : 'studyTime'},    //学习时间
			 {name : 'prerequids'},   //开启条件
			 {name : 'id'},//id
			 {name : 'pid'},//pid
			 {name : 'nodeType'}//类型
		]),
		pruneModifiedRecords : true
	});
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
		/**
		 * @author ys, 
		{
			header : '开启条件',
			width : .1,
			dataIndex : 'prerequids',
			renderer : function(v){
				return getValueByIdFromArr(prerequidsArray,v);
			},
			editor : new Ext.form.ComboBox({
				store : new Ext.data.SimpleStore({
					fields : ['value', 'text'],
					data : prerequidsArray
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
			header : '选修页的最少选修个数',
			width : .17,
			dataIndex : 'minSecNum',
			editor : new Ext.form.NumberField({
				allowNegative : false,
				allowDecimals : false
			}),
			renderer : function(v,o,r){//列的渲染函数
				var type = r.get("nodeType");
				if(type == "2"){ //模块
					return v ;
				}else{ //页
					return null;
				}
			}
		}
		*/
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
		autoHeight : true,
		autoScroll : true,
//		height : 600,
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
	        			var coll = gstore.query("id",pid);
	        			for(var i=0;i<coll.length;i++){
	        				coll.each(function(pr){
	        					/*if(pr!=null){
					        		if(e.value=="1"){//选修改成必修
					        			var asn = Number(pr.get("allSecNum"))-1;//上级选修总个数减1
					        			pr.set("allSecNum",asn); 
					        			if(Number(pr.get("minSecNum")) > asn){//最少选修个数大于总个数
					        				pr.set("minSecNum",asn); //重置最少选修个数
					        			}
					        			pr.set("allReqNum",Number(pr.get("allReqNum"))+1); //必修总个数加1
					        		}else{ //必修改成选修
					        			pr.set("allSecNum",Number(pr.get("allSecNum"))+1); //选修总个数加1
					        			pr.set("allReqNum",Number(pr.get("allReqNum"))-1); //必修总个数减1
					        		}
				        		}*/
	        				
	        				});
				        		
	        			}
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
					nodeTypes += r.get("nodeType") + ",";
					titles += r.get("title") + ",";
					if(r.get("nodeType")=="2"){
						var nextR = gstore.getAt(gstore.indexOf(r)+1);
						if(nextR != undefined){
							childTypes += gstore.getAt(gstore.indexOf(r)+1).get("nodeType") + ",";
						}
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
					   		showInfo("设置成功",siteY);
					   		setWin.close();
					   		courseNode.parentNode.reload();
					   	},
					   	params: {
					   		nodeType :nodeTypes, 
					   		node :ids, 
					   		required : requireds, 
					   		studyTime : studyTimes,
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
		}]
	});
	setWin.show();
	
	gstore.add(records, function() {
			pageHeightInit();
			gstore.modified = [];
		}
	); 
	
	
	var mask = new Ext.LoadMask(setWin.body, {
		msg : '程序运行中....'
	});
}