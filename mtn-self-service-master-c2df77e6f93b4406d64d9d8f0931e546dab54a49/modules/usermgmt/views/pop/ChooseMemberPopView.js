define([
    'hbs!modules/usermgmt/templates/pop/ChooseMemberPopTpl.hbs',
    'i18n!i18n/common.i18n'
],function(template, i18n) {
    return fish.View.extend({
        // el: false,
        template: template,
        //提供模板数据
        serialize: i18n,
        //视图事件定义
        events:{
            'click .js-back':'onBack',
            'click .js-submit':'onSubmit',
            'click .js-edit':'onEdit'
        },
        //一些初始化设置 (不能进行dom操作)
        initialize: function() {
        },
        //视图渲染完毕处理函数
        afterRender: function() {
            this.initGridFunc();
            this.$('#AccountNo').combobox({
                placeholder: 'Please Select',
                dataTextField: 'name',
                dataValueField: 'value',
                dataSource: [
                    {name: 'System Administrator', value: 'a'},
                    {name: 'Department Administrator', value: 'b'},
                    {name: 'Normmal User', value: 'c'}
                ]
            });
        },
        onBack: function() {
            this.parentView.requireView({
                url: 'modules/usermgmt/views/UsersView',
                selector: "#contentMain"
            });
        },
        initGridFunc: function() {
            //数据源
            var mydata = [
                {MSISDN:"1",Department:"ELECTRICITY..."},
                {MSISDN:"2",Department:"ELECTRICITY..."},
                {MSISDN:"3",Department:"ELECTRICITY..."},
                {MSISDN:"4",Department:"ELECTRICITY..."},
                {MSISDN:"5",Department:"ELECTRICITY..."},
                {MSISDN:"6",Department:"ELECTRICITY..."},
                {MSISDN:"7",Department:"ELECTRICITY..."},
                {MSISDN:"8",Department:"ELECTRICITY..."},
                {MSISDN:"9",Department:"ELECTRICITY..."},
                {MSISDN:"11",Department:"ELECTRICITY..."},
                {MSISDN:"12",Department:"ELECTRICITY..."},
                {MSISDN:"14",Department:"ELECTRICITY..."},
                {MSISDN:"14",Department:"ELECTRICITY..."},
                {MSISDN:"15",Department:"ELECTRICITY..."},
                {MSISDN:"16",Department:"ELECTRICITY..."},
                {MSISDN:"13",Department:"ELECTRICITY..."},
                {MSISDN:"17",Department:"ELECTRICITY..."},
                {MSISDN:"18",Department:"ELECTRICITY..."},
                {MSISDN:"19",Department:"ELECTRICITY..."},
                {MSISDN:"20",Department:"ELECTRICITY..."}
            ];
            //参数配置
            var opt = {
                data: mydata,
                height: 'auto',
                width: '100%',
                colModel: [{
                    name: 'MSISDN',
                    label: 'MSISDN'
                }, {
                    name: 'Department',
                    label: 'Department'
                }],
                multiselect:true
            };
            //加载grid
            this.$grid = this.$('#ChooseMemberGrid').grid(opt);
            var data = this.$grid.grid("getRowData");
            this.$grid.grid("setSelection", data[0]);

            if (mydata.length > 8) {
                this.$grid.grid("setGridHeight", 390);
            }
        }
    });
});