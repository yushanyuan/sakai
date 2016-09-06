//不考虑http错误，仅考虑业务错误。
function doResponse(form, action, dook) {
	var success = action.result.result;
	if (success) {
		showInfo(action.result.msg);
		dook();
	} else {
		showError(action.result.msg);
	}
}

function ajaxResponse(result, request, dook) {
	var obj = Ext.decode(result.responseText);
	var success = obj.result;
	if (success) {
		dook();
	} else {
		showError(obj.msg);
	}
}

function doAjaxResponse(result, request, dook) {
	var obj = Ext.decode(result.responseText);
	var success = obj.result;
	if (success) {
		showInfo(obj.msg);
		dook();
	} else {
		showError(obj.msg);
	}
}
function convertTime(value) {
	return Ext.util.Format.date(new Date(value), 'Y年m月d日 H:i:s');
}
function convertDate(value) {
	return Ext.util.Format.date(new Date(value), 'Y年m月d日');
}
function tipAt(title, html, position) {
	var qt = new Ext.QuickTip( {
		title : title,
		html : html,
		autoHide : true
	});
	qt.showAt(position);
}
function tip(cmp, title, html) {
	var qt = new Ext.QuickTip( {
		target : cmp,
		title : title,
		html : html,
		autoHide : true
	});
	qt.showAt(cmp.getPosition());
}
// 将二维数组转为对象，数组里仅能有两个元素
// 此方法可方便地根据值得到对应的中文。
function arrayToObject(array) {
	var object = {};// 建立一个空的对象
	for ( var i = 0; i < array.length; i++) {// 循环数组元素
		object[array[i][0]] = array[i][1];
	}
	return object;
}
function reloadCaptcha() {
	var timenow = new Date().getTime();
	document.getElementById("captchaImg").src = path + '/captchaServlet?_dc='
			+ timenow;
}
function showInfo(content,y) {
	if(y!=undefined && y!=null){
		Ext.Msg.show( {
			title : "系统提示",
			msg : content,
			y : ((typeof y == "number")?y:y.getHeight()/2),
			icon : Ext.Msg.INFO,
			buttons : Ext.Msg.OK
		});
	}
	else{
		Ext.Msg.show( {
			title : "系统提示",
			msg : content,
			icon : Ext.Msg.INFO,
			buttons : Ext.Msg.OK
		});
	}
	setTimeout(function() {
		Ext.Msg.hide();
	}, 2000);
}
function showWarn(content,y) {
	if(y!=undefined && y!=null){
		Ext.Msg.show( {
			title : "系统提示",
			msg : content,
			y : ((typeof y == "number")?y:y.getHeight()/2),
			icon : Ext.Msg.WARNING,
			buttons : Ext.Msg.OK
		});
	}
	else{
		Ext.Msg.show( {
			title : "系统提示",
			msg : content,
			icon : Ext.Msg.WARNING,
			buttons : Ext.Msg.OK
		});
	}
}
function showError(content) {
	Ext.Msg.show( {
		title : "系统错误",
		msg : content,
		icon : Ext.Msg.ERROR,
		buttons : Ext.Msg.OK
	});
}
function showQuestions(msg,y,fun){
	if(y!=undefined && y!=null){
		Ext.Msg.show({
	        title: '系统提示',
	        width : 300,
	        msg: msg,
	        y : ((typeof y == "number")?y:y.getHeight()/2),
	        fn : fun,
	        buttons: Ext.Msg.OKCANCEL,
	        icon: Ext.Msg.QUESTION
	    });
	}else{
		Ext.Msg.show({
	        title: '系统提示',
	        width : 300,
	        msg: msg,
	        fn : fun,
	        buttons: Ext.Msg.OKCANCEL,
	        icon: Ext.Msg.QUESTION
	    });
	}
    
}
function getlengthB(str) {
	return str.replace(/[^\x00-\xff]/g, "**").length;
}

Ext.ux.RadioGroupData = function(config){
	var name = config.name;
	var baseData = config.baseData;
	var defaultValue = config.defaultValue ? config.defaultValue : null;
	var returnArray = new Array();
	for(var i=0 ; i<baseData.length; i++){
		var item = baseData[i];
		returnArray.push({
			boxLabel : item[1],
			name: name,
			inputValue: item[0],
			checked : item[0] == defaultValue? true :false
		});
	}
	return returnArray;
};

Ext.ux.CheckGroupData = function(config){
	var name = config.name;
	var baseData = config.baseData;
	var defaultValue = config.defaultValue ? config.defaultValue : null;
	var checkType = config.checkType ? config.checkType : null;
	var returnArray = new Array();
	for(var i=0 ; i<baseData.length; i++){
		var item = baseData[i];
		var check = false;
		if(checkType == "all"){
			check = true;
		}
		else if(checkType == "single" && item[0] == defaultValue){
			check = true;
		}
		returnArray.push({
			boxLabel : item[1],
			name: name,
			inputValue: item[0],
			checked : check
		});
	}
	return returnArray;
};
Ext.ux.TextFieldReadOnlyStyle = function(style){
    this.init = function(f){
        f.style = 'border: medium none ;background:none';
        f.on('beforerender',function(){
            f.hideTrigger = true;
        })
    }
}


function getHelpImg(path){
	var helpImg = "<img ext:qtip=\"操作提示\" src=\""+path+"/resource/icons/help_tip.gif\">";
	return helpImg;
}