define([
    'hbs!modules/usermgmt/templates/UserMgmtTpl.hbs',
    'i18n!i18n/common.i18n',
    'modules/usermgmt/views/AsideView',
    'modules/usermgmt/views/UsersView',
    'modules/usermgmt/views/DepartmentView'
],function(template, i18n, AsideView, UsersView, DepartmentView) {
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
            this.setViews({
                '#contentAside': new AsideView(),
                '#contentMain': new DepartmentView()
            })
        },
        //视图渲染完毕处理函数
        afterRender: function() {
        }
    });
});