function scrollImg() {
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
	var helperTool = window.document.getElementById("helperToolId");
	helperTool.style.top = (posY + 5) + "px";// 这里设置初始化位置 距离页面顶部
	helperTool.style.right = (posX) + "px";// 距离页面左侧
	
	setTimeout("scrollImg()", 100);// 执行时间间隔 100毫秒
}