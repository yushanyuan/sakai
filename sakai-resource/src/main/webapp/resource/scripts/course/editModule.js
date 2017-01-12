/**
 * 修改模块(节点)
 * @param {} node
 */
function updateModule(m,wintitle){
	var saveUrl = 'courseSpace_updateModule.do';
	//console.log(m)
	if(!m){
		m = {
			//videoPicPath:"",
			//videoUrl:"",
			title:"",
			required:"",
			studyDay:"",
			learnObj:"",
			keywords:"",
			whatsNext:"",
			videoPicPath:"",
			videoUrl:"",
			videoSize:0,
			videoTime:"",
			id:""
		}		
		saveUrl = 'courseSpace_addModule.do';
	}

	var siteY = 0;
	var	title = new Ext.form.TextField({
		name : 'title',
		value : m.title,
		fieldLabel : '<font style=color:red>*</font>节点标题',
		width:300,
		allowBlank : false
	});
	var initRequired = m.required;//修改前的必修属性
	var	required = new Ext.form.RadioGroup({
		name : 'required',
		value : initRequired,
		fieldLabel : '<font style=color:red>*</font>节点属性',
		anchor : '99%',
		columns: [80, 80],
        vertical: true,
        allowBlank : false,
		items: new Ext.ux.RadioGroupData({
			baseData : requiredArray,
			name : 'required'
		})
	});
	var studyDay = new Ext.form.NumberField({
		name : 'studyDay',
		value : m.studyDay,
		fieldLabel : '<font style=color:red>*</font>建议学习时长(分钟)',
		emptyText : '0',
		width:300,
		allowNegative : false,//不能输入负数
		allowDecimals : false //不能输入小数
	})
	
	var	createdByFname = new Ext.form.TextField({
		name : 'createdByFname',
		fieldLabel : '修改人',
		plugins : [new Ext.ux.TextFieldReadOnlyStyle()],
		value : userName,
		anchor : '99%',
		readOnly : true
	});
	var baseAttr = [title,required,studyDay,createdByFname];
	var learnObj = new Ext.form.TextArea({
		name : 'learnObj',
		value : m.learnObj,
		fieldLabel : '节点概要/目标',
		anchor : '99%'
	})
	var keywords = new Ext.form.TextArea({
		name : 'keywords',
		value : m.keywords,
		fieldLabel : '关键字',
		anchor : '99%'
	})
	
	var whatsNext = new Ext.form.TextArea({
		name : 'whatsNext',
		value : m.whatsNext,
		fieldLabel : '下一步',
		anchor : '99%'
	})
	var	videoPicPath = new Ext.form.TextField({
		name : 'videoPicPath',
		value : m.videoPicPath,
		fieldLabel : '视频图片地址',
		anchor : '99%'
	})
	var	videoUrl = new Ext.form.TextField({
		name : 'videoUrl',
		value : m.videoUrl,
		fieldLabel : '视频地址',
		width:500
	})
	var	videoSize = new Ext.form.Hidden({
		name : 'videoSize',
		value : m.videoSize
	})
	var	videoTime = new Ext.form.Hidden({
		name : 'videoTime',
		value : m.videoTime
	})
	var resItems = [videoUrl,videoSize,videoTime];
	if(useResSys){
		var	selectResBtn= new Ext.Button({
			text : '选择资源',
			handler : function() {
				selectWin.show();	
				grid_store.load();			
			}
		})
		var	viewResBtn= new Ext.Button({
			text : '资源预览',
			handler : function() {
				viewResWin.show();			
			}
		})
		resItems.push(selectResBtn);
		resItems.push(viewResBtn);
	}
	var	selectResUrl = {
	   xtype: 'compositefield',
	   items: resItems
	}
	var extAttr = [
		videoPicPath, selectResUrl, learnObj, keywords, whatsNext, 
		{
			xtype : 'textarea',
			id : 'description',
			fieldLabel : '说明',
			allowBlank : true,
			anchor : '99%',
			height : 200
		},{
			xtype : 'textarea',
			id : 'courseGuide',
			fieldLabel : '课程引导',
			allowBlank : true,
			anchor : '99%',
			height : 200
		},{
			xtype : 'textarea',
			id : 'teachGoal',
			fieldLabel : '教学目标',
			allowBlank : true,
			anchor : '99%',
			height : 200
		},{
			xtype : 'textarea',
			id : 'keyAndDifficute',
			fieldLabel : '重点难点',
			allowBlank : true,
			anchor : '99%',
			height : 200
		},{
			xtype : 'textarea',
			id : 'teachMethod',
			fieldLabel : '教学方法',
			allowBlank : true,
			anchor : '99%',
			height : 200
		},{
			xtype : 'textarea',
			id : 'learnNavigation',
			fieldLabel : '学习导航',
			allowBlank : true,
			anchor : '99%',
			height : 200
		}
	]
	Array.prototype.push.apply(baseAttr,extAttr);  
	var updateForm = new Ext.form.FormPanel({
		labelAlign : 'right',
		title : '修改节点',
		labelWidth : 120,
		labelSeparator : '：',
		autoHeight : true,
		width : 820,
		lines : false,
		border:false,
		autoScroll : true,
		items : [{
			layout : 'form',
			items : baseAttr
		}],
		buttons :[{
			text : '保存',
			handler : function() {
				// 将编辑器的数据更新到content中。
				CKEDITOR.instances.description.updateElement();
				CKEDITOR.instances.courseGuide.updateElement();
				CKEDITOR.instances.teachGoal.updateElement();
				CKEDITOR.instances.keyAndDifficute.updateElement();
				CKEDITOR.instances.teachMethod.updateElement();
				CKEDITOR.instances.learnNavigation.updateElement();
				
				if (!updateForm.getForm().isValid()) {
					showInfo("数据校验未通过，请检查有效性",siteY);	
					return;
				}
				mask.show(siteY);
				updateForm.getForm().submit({
				    clientValidation: true,
				    params :{
				    	node:nodeId,
				    	nodeType:nodeType,
				    	courseId:courseId
				    },
				    url: saveUrl,
				    success: function(form, action) {
				    	mask.hide();
						showInfo("保存成功",siteY);//显示执行成功提示					
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

	updateForm.getForm().load({
				url : 'courseSpace_updateModuleInit.do',
				params : {
					moduleId : m.id
				},
				success : function(form, action) {
					//CKEDITOR.instances.description.setData(action.result.data);
					CKEDITOR.instances.description.setData(action.result.data.description);
					CKEDITOR.instances.courseGuide.setData(action.result.data.courseGuide);
					CKEDITOR.instances.teachGoal.setData(action.result.data.teachGoal);
					CKEDITOR.instances.keyAndDifficute.setData(action.result.data.keyAndDifficute);
					CKEDITOR.instances.teachMethod.setData(action.result.data.teachMethod);
					CKEDITOR.instances.learnNavigation.setData(action.result.data.learnNavigation);
				}
			});

	updateForm.render(document.body);

	ckeditor = CKEDITOR.replace('description', editorConfig);
	ckeditor_courseGuide = CKEDITOR.replace('courseGuide', editorConfig);
	ckeditor_teachGoal = CKEDITOR.replace('teachGoal', editorConfig);
	ckeditor_keyAndDifficute = CKEDITOR.replace('keyAndDifficute', editorConfig);
	ckeditor_teachMethod = CKEDITOR.replace('teachMethod', editorConfig);
	ckeditor_learnNavigation = CKEDITOR.replace('learnNavigation', editorConfig);	
	
	var mask = new Ext.LoadMask(document.body, {
		msg : '程序运行中....'
	});
	
	var grid_store = new Ext.data.ArrayStore({
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
                        videoUrl.setValue(rec.get('httpPath'));
                        videoSize.setValue(rec.get('videoSize'));
                        videoTime.setValue(rec.get('videoTime'));
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
	 var selectWin = new Ext.Window({
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
	 var viewResWin = new Ext.Window({
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
		var urlelem = jQuery("#hd_videourl");
		var viewUrl = videoUrl.getValue();
        if (!urlelem || urlelem.length == 0) {
        	jQuery('body').append("<input type='hidden' id='hd_videourl' value='" + viewUrl + "'/>");
        } else {
            urlelem.val(viewUrl);
        }
        var viewResIframe = jQuery("#viewResIframe");
        viewResIframe.prop("src","/library/courseware/templates/default/video.html?onlyGiven=true") ;
	 });
}