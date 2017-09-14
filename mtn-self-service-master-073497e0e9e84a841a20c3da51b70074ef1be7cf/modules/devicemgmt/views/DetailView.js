define([
    'hbs!modules/devicemgmt/templates/DetailTpl.hbs',
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
            this.$('#Tabs').tabs();
            this.$('#ModifySubTabs').tabs();
            this.$('#PackageOrderTabs').tabs();
            this.$('#Form').form('disable');
            this.$('.accordion-menu span').click(function() {
                $(this).parent('.accordion-menu').toggleClass('menu-open').children('.menu-section').toggleClass('section-open');
            });
            this.$('#ChangeDate').combobox({
                editable: false,
                dataTextField: 'name',
                dataValueField: 'value',
                dataSource: [
                    {name: '2017-01', value: '1'},
                    {name: '2016-12', value: '2'},
                    {name: '2016-11', value: '3'},
                    {name: '2016-10', value: '4'}
                ]
            });
            this.$('#ChangeDate').combobox('value','1');
        },
        onBack: function() {
            this.parentView.requireView({
                url: 'modules/devicemgmt/views/DepartmentView',
                selector: "#contentMain"
            });
        }
    });
});