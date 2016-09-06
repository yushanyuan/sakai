var timer = 1*60000;//间隔时间


//弹出是否继续学习的提示
function stopStudy(){
	document.getElementById('stopStudyDiv').style.display='';
	stopStudyScrollImg();
	setTimeout("stopStudy();", promptInterval); 
	time = setInterval("cancelStudy();",skipInterval); 
}

// 继续学习 取消提示框
function continueStudy() {
	document.getElementById('stopStudyDiv').style.display='none';
	clearInterval(time);
}
// 不继续学习 跳到课程空间页面
function cancelStudy(){
	window.location.href='studySpace_cancelStudy.do?moduleId='+moduleId+'&detailRecordId='+detailRecordId+"&studyrecordId="+studyrecordId;
}
setTimeout("stopStudy();",  promptInterval); 

//让弹出的是否继续学习的确认框浮动
function stopStudyScrollImg() {
	var posX, posY;// 记录xy坐标
	if (window.parent) {
		if (window.parent.window.innerHeight) {// 页面高度
			posX = window.parent.window.pageXOffset;// 页面x坐标
			posY = window.parent.window.pageYOffset;// 纵坐标
		} else if (window.parent.window.document.documentElement
				&& window.parent.window.document.documentElement.scrollTop) {// 判断浏览器是否有documentElement对象
			posX = window.parent.window.document.documentElement.scrollLeft;
			posY = window.parent.window.document.documentElement.scrollTop;
		} else if (window.parent.window.document.body) {
			posX = window.parent.window.document.body.scrollLeft;
			posY = window.parent.window.document.body.scrollTop;
		}
	} else {
		if (window.innerHeight) {// 页面高度
			posX = window.pageXOffset;// 页面x坐标
			posY = window.pageYOffset;// 纵坐标
		} else if (document.documentElement
				&& document.documentElement.scrollTop) {// 判断浏览器是否有documentElement对象
			posX = document.documentElement.scrollLeft;
			posY = document.documentElement.scrollTop;
		} else if (document.body) {
			posX = document.body.scrollLeft;
			posY = document.body.scrollTop;
		}
	}
	var helperTool = window.document.getElementById("stopStudyDiv");
	helperTool.style.top = (posY + 100) + "px";// 这里设置初始化位置 距离页面顶部
	helperTool.style.right = (posX+400) + "px";// 距离页面左侧
	
	setTimeout("stopStudyScrollImg()", 100);// 执行时间间隔 100毫秒
}