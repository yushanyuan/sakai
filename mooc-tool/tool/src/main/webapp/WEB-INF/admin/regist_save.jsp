<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
<jsp:directive.include file="/templates/header.jsp"/>
<jsp:directive.include file="/templates/includes.jsp"/>

<style type="text/css">
* {
	margin: 0;
	npadding: 0;
	outline: 0 !important;
}

html, body, div, span, applet, object, iframe, h1, h2, h3, h4, h5, h6, p,
	blockquote, pre, a, abbr, acronym, address, big, cite, code, del, dfn,
	em, font, img, ins, kbd, q, s, samp, small, strike, strong, sub, sup,
	tt, var, dl, dt, dd, ol, ul, li, fieldset, form, label, legend, table,
	caption, tbody, tfoot, thead, tr, th, td, figure {
	margin: 0;
	padding: 0
}

html, body {
	width: 100%;
	height: 100%;
}

body {
	margin: 0;
	padding: 0;
	font-size: 12px;
	font-family: 'Microsoft YaHei', Arial, Helvetica, sans-serif;
	background-color: #3ba9c4;
	background-size: cover;
}

ul, ol, li {
	list-style-type: none
}

a {
	color: #333;
}

a:hover, a:active {
	outline: 0;
}

a, a:visited {
	text-decoration: none;
}

.title {
	font-size: 25px;
	font-weight: bold;
	font-family: inherit;
	color: #219c6f
}

.clearfix {
	*zoom: 1;
}

.clearfix:before, .clearfix:after {
	display: table;
	line-height: 0;
	content: "";
}

.clearfix:after {
	clear: both;
}

.pull_right {
	float: right;
}

.login_container {
	width: 320px;
	height: 280px;
	padding: 50px;
	position: absolute;
	left: 50%;
	top: 40%;
	margin-left: -190px;
	margin-top: -170px;
	background-color: #FFF;
	border-radius: 30px;
	box-shadow: 0 0 50px rgba(0, 0, 0, 0.3);
	text-align: center;
	font-size: 14px;
}

.form_group {
	position: relative;
}

.form_group.error {
	text-align: left;
	margin-top: 15px;
	color: #bd5555;
}

.form_control {
	height: 48px;
	width: 100%;
	font-size: 14px;
	padding-left: 5px;
	vertical-align: baseline;
	box-shadow: none;
	border-radius: 0;
	border: 0;
	border-bottom: 1px solid #d6d6d6;
	background: transparent;
}

.form_options {
	margin-top: 20px;
}

.form_options .pull_right, .form_options .pull_right a {
	color: #bd5555;
}

.form_options .text_left {
	float: left;
	color: #999;
}

label.checkbox input {
	margin-right: 10px;
}

input.login {
	font-size: 18px;
	background-color: #219c6f;
	height: 42px;
	line-height: 40px;
	width: 40%;
	border-radius: 4px;
	border: 0;
	margin-top: 20px;
	color: #FFF;
	cursor: pointer;
}
input.regist {
	font-size: 18px;
	background-color: #219c6f;
	height: 42px;
	line-height: 40px;
	width: 40%;
	border-radius: 4px;
	border: 0;
	margin-top: 20px;
	color: #FFF;
	cursor: pointer;
}
</style>

 <body>

<div class="login_container">
<div class="title">北邮开放课程平台</div>
<form action="save.htm" method="POST" enctype="multipart/form-data">
	<input type="hidden" name="nextPage" value="${nextPage}" />
	 <table class="itemSummary">
	 	<tr>
            <th><label for="username">用户名：</label></th>
            <td><input type="text" id="username" name="username"/></td>
        </tr>
        <tr>
            <th><label for="name">姓名：</label></th>
            <td><input type="text" id="name" name="name"/></td>
        </tr>
        <tr>
            <th><label for="pwd">密码：</label></th>
            <td><input type="password" id="pwd" name="pwd"/></td>
        </tr>
         <tr>
            <th><label for="email">邮箱：</label></th>
            <td><input type="text" id="email" name="email"/></td>
        </tr>
        <tr>
            <td colspan=2>
                <input type="reset" />
                <input type="submit" value="提交"/>
            </td>
        </tr>
    </table>
</form>
</div>
</body>
 
