// 成功加入队列后的事件处理
function fileQueued(file) {
	// 选择窗口关闭时将上传窗口中的文件名 设置为 要上传的文件的文件名 不包括扩展名
	Ext.getCmp("fileName").setValue(file.name.substring(0, file.name.lastIndexOf(".")));
	Ext.getCmp("summary").setValue(file.name.substring(0, file.name.lastIndexOf(".")));
	var currentNode = Ext.getCmp("columeTree").getSelectionModel().getSelectedNode();
	// FIXME 将文件名发送到后台判断是否有重名文件
	Ext.Ajax.request({
				url : 'file_checkFile.do',
				params : {
					fileName : file.name,
					folderId : currentNode.id
				},
				success : function(result, request) {
					ajaxResponse(result, request, function() {
								// 删除此节点
								// currentNode.remove();
								// tree.root.reload();
								// tree.root.collapse(true,
								// true);
							});
				}
			});
	Ext.getDom("uploadFile").innerHTML = "上传文件:" + file.name;
}

// 加入队列失败的事件处理
function fileQueueError(file, error, message) {
	if (error == SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED) {
		showError("超出文件队列！");
	}
	if (error == SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT) {
		showError(file.name + "大小为" + file.size + "字节，超出服务器限制！");
	}
	if (error == SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE) {
		showError(file.name + "大小为零！");
	}
	if (error == SWFUpload.QUEUE_ERROR.INVALID_FILETYPE) {
		showError(file.name + "文件类型为" + file.type + "，超出服务器限制！");
	}
}
// 文件对话框关闭后的事件处理
function fileDialogComplete() {
	// this.startUpload();// 启动上传

}

// 开始上传后的事件处理
function uploadStart(file) {

	Ext.MessageBox.show({
				title : '请等待',
				msg : '正在上传...',
				progressText : '正在连接...',
				width : 300,
				progress : true,
				closable : false,
				animEl : 'uploadFileButtonPlaceHolder'
			});

}

function uploadProgress(file, bytesLoaded, bytesTotal) {

	updateDisplay.call(this, file);
}

function uploadSuccess(file, serverData) {
	// 更新进度条
	// updateDisplay.call(this, file);
	var result = Ext.decode(serverData);

	if (result.success) {
		if (!result.result) {
			showWarn(result.msg);
			var uploadFileWindow = Ext.getCmp("uploadFileWindow");
			uploadFileWindow.close();
			return;
		}
		// var record = Ext.getCmp(thumbnailTarget +
		// 'ListGP').getSelectionModel()
		// .getSelected();
		// record.set("thumbnail", result.msg);
		// document.getElementById("previewThumbnail").src = path + result.msg;
		showInfo("文件已经成功上传！");
		var currentNode = Ext.getCmp("columeTree").getSelectionModel().getSelectedNode();

		// ---------方法一成功后重新加载树
		// var tree = Ext.getCmp("columeTree");
		// tree.root.reload();
		// tree.root.collapse(true, true);
		// ------------------------------另一种方法在tree中动态增加一个节点

		if (result.data.fileOpType == "modify") {
			var pNode = currentNode.parentNode;
			var newNode = result.data;
			pNode.replaceChild(newNode, currentNode);
			pNode.reload;
		} else {
			// 在tree中增加一个节点
			var newNode = result.data;
			currentNode.appendChild(newNode);
			currentNode.getUI().getIconEl().src = 'resource/icons/folder_not_empty.png';
			// 如果当前节点没有展开，则展开它
			if (!currentNode.isExpanded()) {
				currentNode.expand();
			}
			// 如果当前节点是叶子，则将其图标改为非叶子
			if (currentNode.isLeaf()) {
				currentNode.leaf = false;
				currentNode.getUI().getIconEl().src = 'resource/icons/folder_not_empty.png';
			}
		}

		// ----------------------------------------
		var uploadFileWindow = Ext.getCmp("uploadFileWindow");
		uploadFileWindow.close();
	} else {
		showError(result.msg);
	}
	// showInfo("文件已经成功上传！");
	// 预览上传成功的图片
}

// 上传完成后的事件处理
function uploadComplete(file) {

}

function updateDisplay(file) {
	var b = Number(file.percentUploaded);
	Ext.MessageBox.updateProgress(b / 100, b.toFixed(2) + '% 完成');
}