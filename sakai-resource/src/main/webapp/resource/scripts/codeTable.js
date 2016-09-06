//必修or选修
var requiredArray = [["1", "必修"], ["2", "选修"]];
//开启条件
var prerequidsArray = [["1", "有"], ["2", "无"]];
//是否计算成绩
var isCaculateScoreArray = [["1", "是"], ["2", "否"]];
//是否同一策略
var samaArray = [["2", "是"], ["1", "否"]];
//加分方式
var impressionArray = [["1", "自动加分"], ["2", "查询学号加分"]];
//批改状态
var checkstatusArray = [["1", "已批改"], ["0", "未批改"]];

//是否在学生界面显示通过状态
var showStuStatus = [["0","否"],["1","是"]];

//播放模板
var playerTemplateArray = [["custom", "自定义模板"],["default", "默认模板"],["kecheng-R","模板-红"],["kecheng-G","模板-绿"],["kecheng-B","模板-蓝"]];


//比较状态
var compareStatusArray = [["1", "大于"], ["2", "等于"],["3", "小于"]];
//通过状态
var passStatus = [["1","通过"],["2","未通过"]];
//组卷方式
var buildTypeArray = [["1", "即时组卷"], ["2", "已有试卷"]]; 
function getValueByIdFromArr(arrObj, oId) {
	for (var i = 0; i < arrObj.length; i++) {
		if (arrObj[i][0] == oId) {
			return arrObj[i][1];
		}
	}
	return "";
}