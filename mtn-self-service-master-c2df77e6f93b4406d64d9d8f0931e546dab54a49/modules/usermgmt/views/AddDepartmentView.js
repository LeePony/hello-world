define([
    'hbs!modules/usermgmt/templates/AddDepartmentTpl.hbs',
    'i18n!i18n/common.i18n'
],function(template, i18n) {
    return fish.View.extend({
        el: false,
        template: template,
        //提供模板数据
        serialize: i18n,
        //视图事件定义
        events:{
            'click .js-back':'onBack'
        },
        //一些初始化设置 (不能进行dom操作)
        initialize: function() {
        },
        //视图渲染完毕处理函数
        afterRender: function() {
        },
        onBack: function() {
            this.parentView.requireView({
                url: 'modules/usermgmt/views/DepartmentView',
                selector: "#contentMain"
            });
        }
    });
});