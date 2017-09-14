define([
    'hbs!modules/batchoperation/templates/BatchOperationTpl.hbs',
    'i18n!i18n/common.i18n'
],function(template, i18n) {
    return fish.View.extend({
        el: false,
        template: template,
        //提供模板数据
        serialize: i18n,
        //视图事件定义
        events:{
        },
        //一些初始化设置 (不能进行dom操作)
        initialize: function() {
        },
        //视图渲染完毕处理函数
        afterRender: function() {
            this.$('.js-input-file').fileupload({
                url: 'http://10.45.4.30:7888',
                dataType: 'json',
                acceptFileTypes: /(\.|\/)(gif|jpg|jpeg|png)$/i,
                add: function(e, data) {
                    data.context = this.$('.filelist-ul');
                    $.each(data.files, function (index, file) {
                        var node = $('<li class="filelist-item">');
                        node.append('<div class="info">' + file.name + '</div>');
                        node.prependTo(data.context);
                    }.bind(this));
                }.bind(this),
                done: function(e, data) {
                }
            });
        }
    });
});