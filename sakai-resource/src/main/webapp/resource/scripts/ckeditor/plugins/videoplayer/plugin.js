/*
Copyright (c) 2003-2011, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/
(function()
{
    CKEDITOR.plugins.add('videoplayer', {
	lang:'zh-cn',
    requires: 'dialog,fakeobjects',
    icons: 'videoplayer', 
    onLoad: function () {

    },
    init: function (editor) {
        var pluginName = 'videoplayer';
        lang = editor.lang.videoplayer;
        editor.addCommand(pluginName, new CKEDITOR.dialogCommand(pluginName));
        editor.ui.addButton && editor.ui.addButton('VideoPlayer', {
            label: lang.toolbar,
            command: pluginName,
            toolbar: 'insert'
        });

        CKEDITOR.dialog.add(pluginName, this.path + 'dialogs/videoplayer.js');
        
        editor.on('doubleclick', function (evt) {
            var element = evt.data.element;
            if (element.is('img') && element.data('cke-real-element-type') == pluginName)
                evt.data.dialog = pluginName;
        });
    },
    afterInit: function (editor) {
        var dataProcessor = editor.dataProcessor,
            dataFilter = dataProcessor && dataProcessor.dataFilter;
    }
    });
})();
CKEDITOR.tools.extend(CKEDITOR.config, {
    /**
	 * Save as `<embed>` tag only. This tag is unrecommended.
	 *
	 * @cfg {Boolean} [videoEmbedTagOnly=false]
	 * @member CKEDITOR.config
	 */
    videoEmbedTagOnly: false,

    /**
	 * Add `<embed>` tag as alternative: `<object><embed></embed></object>`.
	 *
	 * @cfg {Boolean} [videoAddEmbedTag=false]
	 * @member CKEDITOR.config
	 */
    videoAddEmbedTag: true,

    /**
	 * Use {@link #videoEmbedTagOnly} and {@link #videoAddEmbedTag} values on edit.
	 *
	 * @cfg {Boolean} [videoConvertOnEdit=false]
	 * @member CKEDITOR.config
	 */
    videoConvertOnEdit: false
});
