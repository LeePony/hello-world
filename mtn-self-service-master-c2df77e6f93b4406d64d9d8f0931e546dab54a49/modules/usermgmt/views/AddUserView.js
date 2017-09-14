define([
    'hbs!modules/usermgmt/templates/AddUserTpl.hbs',
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
            'click .js-edit':'onEdit',
            'click .js-new-win':'onNewWin'
        },
        //一些初始化设置 (不能进行dom操作)
        initialize: function(option) {
            this.$type = option.type;
        },
        //视图渲染完毕处理函数
        afterRender: function() {
            this.initGridFunc();
            this.$('#UserRights').combobox({
                placeholder: 'Please Select',
                dataTextField: 'name',
                dataValueField: 'value',
                dataSource: [
                    {name: 'System Administrator', value: 'a'},
                    {name: 'Department Administrator', value: 'b'},
                    {name: 'Normmal User', value: 'c'}
                ]
            });
            this.$('#Department').combobox({
                placeholder: 'Please Select',
                dataTextField: 'name',
                dataValueField: 'value',
                dataSource: [
                    {name: 'Department 1', value: 'a'},
                    {name: 'Department 2', value: 'b'}
                ]
            });
            if (this.$type === 'detail') {
                this.$('#Form').form('disable');
                var value = {
                    UserName: 'tanyl02',
                    UserRights: 'BASIC',
                    UserID: '--',
                    Email: '1@zte.ocm',
                    ManagerName: '--',
                    MSISDN: '--',
                    Department: '--'
                };
                this.$('#Form').form('value',value);
                this.$('#UserRights').combobox('value','a');
                this.$('#Department').combobox('value','a');
                this.$('.edit-box').show();
                this.$('.detail-box').hide();
                this.$('.js-pwd').hide();
            }
        },
        onNewWin: function() {
            fish.popupView({
                url:"modules/usermgmt/views/pop/ChooseMemberPopView",
                width: 680
                // viewOption:params
            });
        },
        onBack: function() {
            this.parentView.requireView({
                url: 'modules/usermgmt/views/UsersView',
                selector: "#contentMain"
            });
        },
        onSubmit: function() {
            this.$('#Form').form('disable');
            var value = {
                UserName: 'tanyl02',
                UserRights: 'BASIC',
                UserID: '--',
                Email: '1@zte.ocm',
                ManagerName: '--',
                MSISDN: '--',
                Department: '--'
            };
            this.$('#Form').form('value',value);
            this.$('#UserRights').combobox('value','a');
            this.$('#Department').combobox('value','a');
            this.$('.edit-btn').hide();
            this.$('.detail-btn').show();
            this.$('.js-pwd').hide();
        },
        onEdit: function() {
            this.$('#Form').form('enable');
            this.$('.edit-btn').show();
            this.$('.detail-btn').hide();
            this.$('.js-pwd').show();        
        },
        initGridFunc: function() {
            //数据源
            var mydata = [
                {UserID:"1",name:'ELECTRICITY ...',Email:"ioagyemang@ecggh.com",Department:"ELECTRICITY...",Action:'<a href="#" class="text-primary js-detail">Details>></a>'},
                {UserID:"2",name:'ELECTRICITY ...',Email:"ioagyemang@ecggh.com",Department:"ELECTRICITY...",Action:'<a href="#" class="text-primary js-detail">Details>></a>'},
                {UserID:"3",name:'ELECTRICITY ...',Email:"ioagyemang@ecggh.com",Department:"ELECTRICITY...",Action:'<a href="#" class="text-primary js-detail">Details>></a>'},
                {UserID:"4",name:'ELECTRICITY ...',Email:"ioagyemang@ecggh.com",Department:"ELECTRICITY...",Action:'<a href="#" class="text-primary js-detail">Details>></a>'},
                {UserID:"5",name:'ELECTRICITY ...',Email:"ioagyemang@ecggh.com",Department:"ELECTRICITY...",Action:'<a href="#" class="text-primary js-detail">Details>></a>'},
                {UserID:"6",name:'ELECTRICITY ...',Email:"ioagyemang@ecggh.com",Department:"ELECTRICITY...",Action:'<a href="#" class="text-primary js-detail">Details>></a>'},
                {UserID:"7",name:'ELECTRICITY ...',Email:"ioagyemang@ecggh.com",Department:"ELECTRICITY...",Action:'<a href="#" class="text-primary js-detail">Details>></a>'},
                {UserID:"8",name:'ELECTRICITY ...',Email:"ioagyemang@ecggh.com",Department:"ELECTRICITY...",Action:'<a href="#" class="text-primary js-detail">Details>></a>'},
                {UserID:"9",name:'ELECTRICITY ...',Email:"ioagyemang@ecggh.com",Department:"ELECTRICITY...",Action:'<a href="#" class="text-primary js-detail">Details>></a>'},
                {UserID:"11",name:'ELECTRICITY ...',Email:"ioagyemang@ecggh.com",Department:"ELECTRICITY...",Action:'<a href="#" class="text-primary js-detail">Details>></a>'},
                {UserID:"12",name:'ELECTRICITY ...',Email:"ioagyemang@ecggh.com",Department:"ELECTRICITY...",Action:'<a href="#" class="text-primary js-detail">Details>></a>'},
                {UserID:"14",name:'ELECTRICITY ...',Email:"ioagyemang@ecggh.com",Department:"ELECTRICITY...",Action:'<a href="#" class="text-primary js-detail">Details>></a>'},
                {UserID:"14",name:'ELECTRICITY ...',Email:"ioagyemang@ecggh.com",Department:"ELECTRICITY...",Action:'<a href="#" class="text-primary js-detail">Details>></a>'},
                {UserID:"15",name:'ELECTRICITY ...',Email:"ioagyemang@ecggh.com",Department:"ELECTRICITY...",Action:'<a href="#" class="text-primary js-detail">Details>></a>'},
                {UserID:"16",name:'ELECTRICITY ...',Email:"ioagyemang@ecggh.com",Department:"ELECTRICITY...",Action:'<a href="#" class="text-primary js-detail">Details>></a>'},
                {UserID:"13",name:'ELECTRICITY ...',Email:"ioagyemang@ecggh.com",Department:"ELECTRICITY...",Action:'<a href="#" class="text-primary js-detail">Details>></a>'},
                {UserID:"17",name:'ELECTRICITY ...',Email:"ioagyemang@ecggh.com",Department:"ELECTRICITY...",Action:'<a href="#" class="text-primary js-detail">Details>></a>'},
                {UserID:"18",name:'ELECTRICITY ...',Email:"ioagyemang@ecggh.com",Department:"ELECTRICITY...",Action:'<a href="#" class="text-primary js-detail">Details>></a>'},
                {UserID:"19",name:'ELECTRICITY ...',Email:"ioagyemang@ecggh.com",Department:"ELECTRICITY...",Action:'<a href="#" class="text-primary js-detail">Details>></a>'},
                {UserID:"20",name:'ELECTRICITY ...',Email:"ioagyemang@ecggh.com",Department:"ELECTRICITY...",Action:'<a href="#" class="text-primary js-detail">Details>></a>'}
            ];
            //参数配置
            var opt = {
                data: mydata,
                height: 'auto',
                width: '100%',
                colModel: [{
                    name: 'UserID',
                    label: 'User ID',
                    search: true
                }, {
                    name: 'name',
                    label: 'User Name',
                    search: true
                }, {
                    name: 'Email',
                    label: 'Email'
                }, {
                    name: 'Department',
                    label: 'Department'
                }, {
                    name: 'Action',
                    label: 'Action'
                }],
                rowNum: 9,
                pager: true
            };
            //加载grid
            this.$grid = this.$('#Grid').grid(opt);
            var data = this.$grid.grid("getRowData");
            this.$grid.grid("setSelection", data[0]);

            if (mydata.length > 9) {
                this.$grid.grid("setGridHeight", 390);
            }
        }
    });
});