/**
 * @license Copyright (c) 2003-2014, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.html or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function( config ) {
	// Define changes to default configuration here. For example:
	// config.language = 'fr';
	// config.uiColor = '#AADC6E';
};

editorConfig = {};
CKEDITOR.editorConfig(editorConfig);
var basePath = "/library/editor/"+"ckextraplugins/";
CKEDITOR.plugins.addExternal('movieplayer',basePath+'movieplayer/', 'plugin.js');  
var cfg = {			
	extraPlugins: 'movieplayer,videoplayer',
	toolbar_Full:
    [
        ['Source','-','Templates'],
        // Uncomment the next line and comment the following to enable the default spell checker.
        // Note that it uses spellchecker.net, displays ads and sends content to remote servers without additional setup.
        //['Cut','Copy','Paste','PasteText','PasteFromWord','-','Print', 'SpellChecker', 'Scayt'],
        ['Cut','Copy','Paste','PasteText','PasteFromWord','-','Print'],
        ['Undo','Redo','-','Find','Replace','-','SelectAll','RemoveFormat'],
        ['NumberedList','BulletedList','-','Outdent','Indent','Blockquote','CreateDiv'],
        '/',
        ['Bold','Italic','Underline','Strike','-','Subscript','Superscript'],
					['atd-ckeditor'],
        ['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
        ['BidiLtr', 'BidiRtl' ],
        ['Link','Unlink','Anchor'],
        ['AudioRecorder','Image','Movie','Flash','VideoPlayer','Table','HorizontalRule','Smiley','SpecialChar','fmath_formula'],
        '/',
        ['Styles','Format','Font','FontSize'],
        ['TextColor','BGColor'],
        ['Maximize', 'ShowBlocks']
    ],
    stylesSet:
	[
  		{ name : '本节标题', element : 'div', attributes : { 'class' : 'h-page' } },
		{ name : '一级标题', element : 'div', attributes : { 'class' : 'h-1' } },
		{ name : '二级标题', element : 'p', attributes : { 'class' : 'h-2' } },
		{ name : '正文段落', element : 'p'}
	],
    toolbar_NotRes:
    [
        ['Source','-','Templates'],
        // Uncomment the next line and comment the following to enable the default spell checker.
        // Note that it uses spellchecker.net, displays ads and sends content to remote servers without additional setup.
        //['Cut','Copy','Paste','PasteText','PasteFromWord','-','Print', 'SpellChecker', 'Scayt'],
        ['Cut','Copy','Paste','PasteText','PasteFromWord','-','Print'],
        ['Undo','Redo','-','Find','Replace','-','SelectAll','RemoveFormat'],
        ['NumberedList','BulletedList','-','Outdent','Indent','Blockquote','CreateDiv'],
        '/',
        ['Bold','Italic','Underline','Strike','-','Subscript','Superscript'],
					['atd-ckeditor'],
        ['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
        ['BidiLtr', 'BidiRtl' ],
        ['Link','Unlink','Anchor'],
        ['AudioRecorder','Image','Movie','Flash','Table','HorizontalRule','Smiley','SpecialChar','fmath_formula'],
        '/',
        ['Styles','Format','Font','FontSize'],
        ['TextColor','BGColor'],
        ['Maximize', 'ShowBlocks']
    ],
    toolbar: 'NotRes'
};		
jQuery.extend(editorConfig, cfg);
