var buptnu = {resmanage:{targetInput:null}};
buptnu.resmanage.showResWin = function(target){
	selectWin.show();	
	grid_store.load();
	buptnu.resmanage.targetInput = target;
}
buptnu.resmanage.preResWin = function (viewUrl) {
	if((viewUrl == null || viewUrl.length ==0) && buptnu.resmanage.targetInput){
		viewUrl = document.getElementById(buptnu.resmanage.targetInput).value;
	}
	if(viewUrl == null || viewUrl.length ==0){
		return;
	}	
	var urlelem = jQuery("#hd_videourl");
    if (!urlelem || urlelem.length == 0) {
    	jQuery('body').append("<input type='hidden' id='hd_videourl' value='" + viewUrl + "'/>");
    } else {
        urlelem.val(viewUrl);
    }
	viewResWin.show();
}

//修改页
function updateSection(section,wintitle){
	var htmltmpl = '<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html><head><meta content="text/html; charset=utf-8" http-equiv="Content-Type"><title></title><link href="'+tplUrl+'/css/section.css" rel="stylesheet" type="text/css"><link href="'+tplUrl+'/css/plugin.css" rel="stylesheet" type="text/css"></head><body><div id="main"><div></div></div><script src="/library/js/jquery-1.7.1.min.js"></script><script src="/library/courseware/course_plugin.js"></script><script src="'+tplUrl+'/js/course_section.js"></script><script src="/library/courseware/Functions.js"></script><script src="/library/courseware/APIWrapper.js"></script></body></html>';
	var saveUrl = 'courseSpace_updateSection.do';
	if(!section){
		section = {
		  "id": "",
		  "title": "",
		  "description": "",
		  "path": "",
		  "createdByFname": " admin",
		  "sectionSelftest": "",
		  "required": 0,
		  "videoContent": 0,
		  "audioContent": 0,		  
		  "textualContent": 1,
		  "videoUrl":"",
		  "videoSize":0,
		  "videoTime":"",
		  "studyTime": 0
		}
		saveUrl = 'courseSpace_addSection.do';
	}

	var siteY = 0;
	var	title = new Ext.form.TextField({
		name : 'title',
		value : section.title,
		fieldLabel : '<font style=color:red>*</font>页标题',
		width: 300,
		allowBlank : false
	});
	var initRequired = section.required;
	var	required = new Ext.form.RadioGroup({
		name : 'required',
		value : section.required,
		fieldLabel : '<font style=color:red>*</font>页属性',
		anchor : '99%',
		columns: [80, 80],
        vertical: true,
        allowBlank : false,
		items: new Ext.ux.RadioGroupData({
			baseData : requiredArray,
			name : 'required'
		})
	});
	var textualContent = new Ext.form.Checkbox({
		//name: 'textual',		
		width: 80,
		//boxLabel: '图文内容',
		checked :section.textualContent=="1"?true:false,
		hidden:true				
	});
	var videoContent = new Ext.form.Checkbox({
		//name: 'video',
		width: 80,
		checked :section.videoContent=="1"?true:false,
		hidden:true
		//boxLabel: '视频内容',
	});
	var audioContent = new Ext.form.Checkbox({
		//name: 'audio',
		width: 80,
		checked :section.audioContent=="1"?true:false,
		//boxLabel: '音频内容',
		hidden:true
	});
	var	contentType = new Ext.form.RadioGroup({
		id : 'contentType',
		fieldLabel : '<font style=color:red>*</font>形式',
		anchor : '99%',
		columns: [80, 80],
        vertical: true,
        allowBlank : false,
		items: [{ boxLabel: "图文内容",name:"contentTypeRadio", inputValue: "1" },
		        { boxLabel: "视频内容",name:"contentTypeRadio", inputValue: "2" }],
        listeners: {
        	change: function () {
	            if (Ext.getCmp("contentType").getValue().inputValue=="1") {
	            	changeContentType(false);
	            } else {
	            	changeContentType(true);
	            }
	        }
		}
	});
	// 改变内容类型的输入方式,true:视频，false：图文
	var changeContentType = function(flag){
		if (flag) {
        	Ext.getCmp("selectResUrl").setDisabled(false); 
    		Ext.getCmp("selectResUrl").setVisible(true);
    		Ext.getCmp("videoPicPath").show();
    		Ext.getCmp("launchData").hide();
    		textualContent.setValue("0");
    		videoContent.setValue("1");
    		audioContent.setValue("0");
        } else {
    		Ext.getCmp("selectResUrl").setDisabled(true); 
    		Ext.getCmp("selectResUrl").setVisible(false);
    		Ext.getCmp("videoPicPath").hide();
    		Ext.getCmp("launchData").show();
    		textualContent.setValue("1");
    		videoContent.setValue("0");
    		audioContent.setValue("0");
        }
	}
	var description = new Ext.form.TextArea({
		name : 'description',
		value : section.description,
		fieldLabel:'说明',
        width: 500
	})
	
	var studyTime = new Ext.form.NumberField({
		name : 'studyTime',
		value : section.studyTime,
		fieldLabel : '建议学习时长(分钟)',
		emptyText : '0',
		width: 200,
		allowNegative : false,//不能输入负数
		allowDecimals : false //不能输入小数
	})	
	var	videoPicPath = new Ext.form.TextField({
		id : 'videoPicPath',
		name : 'videoPicPath',
		value : section.videoPicPath,
		fieldLabel : '视频图片地址',
		anchor : '99%',
		hidden:true
	})
	var videoUrl = new Ext.form.TextField({
		name : 'videoUrl',
		value : section.videoUrl,
		fieldLabel : '视频地址',
		width:500
	})
	var	videoSize = new Ext.form.Hidden({
		name : 'videoSize',
		value : section.videoSize
	})
	var	videoTime = new Ext.form.Hidden({
		name : 'videoTime',
		value : section.videoTime
	})
	var resItems = [videoUrl,videoSize,videoTime];
	if(useResSys){
		var selectResBtn= new Ext.Button({
			text : '选择资源',
			handler : function(){
				var viewUrl = videoUrl.getValue();
				buptnu.resmanage.showResWin(null);
			}
		})
		var	viewResBtn= new Ext.Button({
			text : '资源预览',
			handler : function(){
				var viewUrl = videoUrl.getValue();
				buptnu.resmanage.preResWin(viewUrl);
			}
		})
		resItems.push(selectResBtn);
		resItems.push(viewResBtn);
	}
	var	selectResUrl = {
	   id:'selectResUrl',
	   xtype: 'compositefield',
	   items: resItems,
	   hidden: true
	}
	/*//判断是否选择了音频或视频，如果选择了则显示
	if(videoContent.getValue() || audioContent.getValue()){
		selectResUrl = {
		   id:'selectResUrl',
		   xtype: 'compositefield',
		   items: resItems
		}
	}*/
	var baseAttr =  [
		title, required,		
		contentType,videoPicPath,selectResUrl,			 
		 {
			xtype : 'textarea',
			id : 'launchData',
			fieldLabel : '编辑',
			allowBlank : false,
			anchor : '99%',
			height : 360
		}, {
			xtype : 'hidden',
			id : 'fileUrl',
			value:section.path
		},
		description,studyTime
	];
	
	var updateForm = new Ext.form.FormPanel({
		labelAlign : 'right',
		title : wintitle,
		labelWidth : 100,
		labelSeparator : '：',
		autoHeight : true,
		width:1024,
		lines : false,
		border:false,
		items : [{
			layout : 'form',
			items : baseAttr
		}],
		buttons :[{
			text : '保存',
			handler : function() {
				// 将编辑器的数据更新到content中。
				CKEDITOR.instances.launchData.updateElement();

				if (!updateForm.getForm().isValid()) {
					showInfo("数据校验未通过，请检查有效性",siteY);	
					return;
				}
				mask.show(siteY);
				updateForm.getForm().submit({
				    clientValidation: true,
				    params :{
				    	node:nodeId, //页id
				    	courseId:courseId,
				    	textualContent:textualContent.getValue()==true?"1":"0",
				    	videoContent:videoContent.getValue()==true?"1":"0",
				    	audioContent:audioContent.getValue()==true?"1":"0"
				    },
				    url: saveUrl,
				    success: function(form, action) {
				    	mask.hide();	
						showInfo("保存成功",siteY);//显示执行成功提示
						if(initRequired != form.getValues().required){
							section.parentsection.parentsection.reload();    
						}else{
							section.parentsection.reload();
						}
						sectionWin.close();
				    },
				    failure: function(form, action) {
						showInfo(action.result.info,siteY);
				    }
				});
			},
			listeners :{
				click : function( o, event ) {
					siteY = (event.getXY())[1] - 50;
				}
			}		
		}]
	});
	
	//设置形式radio默认选择
	if(textualContent.getValue()=="1"){
		contentType.setValue("1");
		changeContentType(false);
	}else if(videoContent.getValue()=="1"){
		contentType.setValue("2");
		changeContentType(true);
	}
	
	updateForm.getForm().load({
		url : 'courseSpace_updateSectionInit.do',
		params : {
			fileUrl : section.path
		},
		success : function(form, action) {
			var val = jQuery("#launchData").val();
			//console.log(val);
			if(jQuery.trim(val) == ""){
				jQuery("#launchData").val(htmltmpl);
			}

			var ckeditor = CKEDITOR.replace('launchData', editorConfig);
		}
	});
			
	updateForm.render(document.body);
	
	var mask = new Ext.LoadMask(document.body, {
		msg : '程序运行中....'
	});
	
	grid_store = new Ext.data.ArrayStore({
        url:'courseSpace_findResUrl.do?resCourseId='+resCourseId,//请求后台数据
        fields:[
		 {name : 'id'},  //资源id
		 {name : 'courseName'}, //资源名称
		 {name : 'httpPath'}, //路径
		 {name : 'catalogPath'}, //目录
		 {name : 'fileType'},//类型
		 {name : 'videoSize'},//大小
		 {name : 'videoTime'}//时长
		]
    });
	
	 // create the Grid
    var grid = new Ext.grid.GridPanel({
        store: grid_store,
        columns: [
            {
                id       : 'catalogPath',
                header   : '目录', 
                width    : 200, 
                sortable : true, 
                dataIndex: 'catalogPath'
            },
            {
                header   : '资源名称', 
                width    : 200, 
                sortable : true, 
                dataIndex: 'courseName'
            },
            {
                xtype: 'actioncolumn',
                width: 50,
                items: [{
                    icon   : 'resource/icons/pass.gif',  // Use a URL in the icon config
                    tooltip: '选择',
                    handler: function(grid, rowIndex, colIndex) {
                        var rec = grid_store.getAt(rowIndex);
                        var url = rec.get('httpPath');
                        if(!buptnu.resmanage.targetInput){
                        	videoUrl.setValue(url);
                        	videoSize.setValue(rec.get('videoSize'));
                        	videoTime.setValue(rec.get('videoTime'));
                    	}else{
                    		document.getElementById(buptnu.resmanage.targetInput).value = url;
                    	}
						selectWin.hide();
                    }
                }]
            }
        ],
        stripeRows: true,
        //autoExpandColumn: 'catalogPath',
        height: 500,
        width: 500,
        title: '资源列表',
        // config options for stateful behavior
        stateful: true,
        stateId: 'grid'
    });
	
	// 弹出资源选择窗口
	selectWin = new Ext.Window({
    	layout:'form',
	    width:500,	
        height:500,		
		autoHeight:false,
		autoScroll:false,
	    title : '选择资源',
	    closeAction:'hide',
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
				selectWin.hide();
   	 		}
	    }]
	});
		// 弹出资源预览窗口
	viewResWin = new Ext.Window({
    	layout:'form',
	    width:640,	
        height:400,		
		autoHeight:false,
		autoScroll:false,
	    title : '资源预览',
	    closeAction:'hide',
	    draggable : false,
		resizable : false,
		modal:true,//True 表示为当window显示时对其后面的一切内容进行遮罩
	    plain: true,//True表示为渲染window body的背景为透明的背景，这样看来window body与边框元素（framing elements）融为一体， false表示为加入浅色的背景，使得在视觉上body元素与外围边框清晰地分辨出来（默认为false）。
	    html:"<iframe id='viewResIframe' src='' width='640' height='360' scrolling='no' frameborder='0'></iframe>",
	    border :false,//True表示为显示出面板body元素的边框，false则隐藏
	    hideBorders :false,
	    frame :true
	});
	 
	 viewResWin.on("hide",function(){
		var urlelem = jQuery("#hd_videourl");
		var viewResIframe = jQuery("#viewResIframe");
        viewResIframe.prop("src","") ;
     });
	 viewResWin.on("show",function(){
	    var viewResIframe = jQuery("#viewResIframe");
	    viewResIframe.prop("src","/library/courseware/templates/default/video.html?onlyGiven=true") ;	
	 });
}
