/**
 * @license Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.html or http://ckeditor.com/license
 */

(function() {

	var defaultToPixel = CKEDITOR.tools.cssLength;

	function loadValue( objectNode, embedNode, paramMap ) {
		
	}

	function commitValue( objectNode, embedNode, paramMap ) {
		
	}

	CKEDITOR.dialog.add( 'videoplayer', function( editor ) {
		return {
			title: editor.lang.videoplayer.title,
			minWidth: 420,
			minHeight: 110,
			onShow: function() {
			},
			onOk: function() {
				var url = jQuery(jQuery(".cke_dialog_ui_input_text")[1]).val();
				var src = CKEDITOR.getUrl('plugins/videoplayer/images/placeholder.png');
				var element = CKEDITOR.dom.element.createFromHtml('<img class="jp_video_pop" rel="'+url+'" src="'+src+'"/>');
				editor.insertElement( element );
			},

			onHide: function() {
			},

			contents: [
			{
				id: 'info',
				label: editor.lang.videoplayer.info,
				accessKey: 'I',
				elements: [
					{
					type: 'vbox',
					padding: 0,
					children: [
						{
						type: 'hbox',
						widths: [ '280px', '110px' ],
						align: 'right',
						children: [
							{
							id: 'src',
							type: 'text',
							label: editor.lang.common.url,
							required: true,
							validate: CKEDITOR.dialog.validate.notEmpty(editor.lang.videoplayer.validateSrc),
							setup: loadValue,
							commit: commitValue,
							onLoad: function() {
								var dialog = this.getDialog()
							}
						},
						{
							type: 'button',
							id: 'browse',
							// v-align with the 'src' field.
							// TODO: We need something better than a fixed size here.
							style: 'display:inline-block;margin-top:10px;',
							label: editor.lang.common.browseServer,
							onClick: function() {
								var txtId = jQuery(jQuery(".cke_dialog_ui_input_text")[1]).attr("id");
						        buptnu.resmanage.showResWin(txtId);
						    }
						}
						]
					}
					]
				},
				{
					type: 'vbox',
					children: [
						{
							type: 'button',
							id: 'preview',
							style: 'display:inline-block;margin-top:10px;',
							label: editor.lang.common.preview,
							onClick: function() {
								buptnu.resmanage.preResWin(null);
						    }
						}
					]
				}
				]
			}
			]
		};
	});
})();