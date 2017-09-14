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
            this.initGridFunc();
        },
        onBack: function() {
            this.parentView.requireView({
                url: 'modules/usermgmt/views/DepartmentView',
                selector: "#contentMain"
            });
        },
        initGridFunc: function() {
            //数据源
            var mydata = [
                {UserID:"1",name:'ELECTRICITY ...',Email:"ioagyemang@ecggh.com",Department:"ELECTRICITY...",Action:'<button type="button" class="btn btn-default">Detail</button>'},
                {UserID:"2",name:'ELECTRICITY ...',Email:"ioagyemang@ecggh.com",Department:"ELECTRICITY...",Action:'<button type="button" class="btn btn-default">Detail</button>'},
                {UserID:"3",name:'ELECTRICITY ...',Email:"ioagyemang@ecggh.com",Department:"ELECTRICITY...",Action:'<button type="button" class="btn btn-default">Detail</button>'},
                {UserID:"4",name:'ELECTRICITY ...',Email:"ioagyemang@ecggh.com",Department:"ELECTRICITY...",Action:'<button type="button" class="btn btn-default">Detail</button>'},
                {UserID:"5",name:'ELECTRICITY ...',Email:"ioagyemang@ecggh.com",Department:"ELECTRICITY...",Action:'<button type="button" class="btn btn-default">Detail</button>'},
                {UserID:"6",name:'ELECTRICITY ...',Email:"ioagyemang@ecggh.com",Department:"ELECTRICITY...",Action:'<button type="button" class="btn btn-default">Detail</button>'},
                {UserID:"7",name:'ELECTRICITY ...',Email:"ioagyemang@ecggh.com",Department:"ELECTRICITY...",Action:'<button type="button" class="btn btn-default">Detail</button>'},
                {UserID:"8",name:'ELECTRICITY ...',Email:"ioagyemang@ecggh.com",Department:"ELECTRICITY...",Action:'<button type="button" class="btn btn-default">Detail</button>'},
                {UserID:"9",name:'ELECTRICITY ...',Email:"ioagyemang@ecggh.com",Department:"ELECTRICITY...",Action:'<button type="button" class="btn btn-default">Detail</button>'},
                {UserID:"11",name:'ELECTRICITY ...',Email:"ioagyemang@ecggh.com",Department:"ELECTRICITY...",Action:'<button type="button" class="btn btn-default">Detail</button>'},
                {UserID:"12",name:'ELECTRICITY ...',Email:"ioagyemang@ecggh.com",Department:"ELECTRICITY...",Action:'<button type="button" class="btn btn-default">Detail</button>'},
                {UserID:"14",name:'ELECTRICITY ...',Email:"ioagyemang@ecggh.com",Department:"ELECTRICITY...",Action:'<button type="button" class="btn btn-default">Detail</button>'},
                {UserID:"14",name:'ELECTRICITY ...',Email:"ioagyemang@ecggh.com",Department:"ELECTRICITY...",Action:'<button type="button" class="btn btn-default">Detail</button>'},
                {UserID:"15",name:'ELECTRICITY ...',Email:"ioagyemang@ecggh.com",Department:"ELECTRICITY...",Action:'<button type="button" class="btn btn-default">Detail</button>'},
                {UserID:"16",name:'ELECTRICITY ...',Email:"ioagyemang@ecggh.com",Department:"ELECTRICITY...",Action:'<button type="button" class="btn btn-default">Detail</button>'},
                {UserID:"13",name:'ELECTRICITY ...',Email:"ioagyemang@ecggh.com",Department:"ELECTRICITY...",Action:'<button type="button" class="btn btn-default">Detail</button>'},
                {UserID:"17",name:'ELECTRICITY ...',Email:"ioagyemang@ecggh.com",Department:"ELECTRICITY...",Action:'<button type="button" class="btn btn-default">Detail</button>'},
                {UserID:"18",name:'ELECTRICITY ...',Email:"ioagyemang@ecggh.com",Department:"ELECTRICITY...",Action:'<button type="button" class="btn btn-default">Detail</button>'},
                {UserID:"19",name:'ELECTRICITY ...',Email:"ioagyemang@ecggh.com",Department:"ELECTRICITY...",Action:'<button type="button" class="btn btn-default">Detail</button>'},
                {UserID:"20",name:'ELECTRICITY ...',Email:"ioagyemang@ecggh.com",Department:"ELECTRICITY...",Action:'<button type="button" class="btn btn-default">Detail</button>'}
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