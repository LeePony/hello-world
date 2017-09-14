define([
    'hbs!modules/apnrequest/templates/APNRequestTpl.hbs',
    'i18n!i18n/common.i18n'
],function(template, i18n) {
    return fish.View.extend({
    	el:false,
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
            this.$('#ProtocolsNeeded').combobox({
                placeholder: 'Please Select',
                dataTextField: 'name',
                dataValueField: 'value',
                dataSource: [
                    {name: 'System Administrator', value: 'a'},
                    {name: 'Department Administrator', value: 'b'},
                    {name: 'Normmal User', value: 'c'}
                ]
            });
            this.$('#IPAddressAllocation').combobox({
                placeholder: 'Please Select',
                dataTextField: 'name',
                dataValueField: 'value',
                dataSource: [
                    {name: 'System Administrator', value: 'a'},
                    {name: 'Department Administrator', value: 'b'},
                    {name: 'Normmal User', value: 'c'}
                ]
            });
            this.$('.icon-jingtanhao').tooltip({ placement: 'right'});
        }
    });
});