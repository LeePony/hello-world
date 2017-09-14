define([
    'hbs!modules/devicemgmt/templates/DepartmentTpl.hbs',
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
            this.$('#Form').form('disable');
            this.initGridFunc();
        },
        initGridFunc: function() {
            //数据源
            var mydata = [
                {ICCID:"544346411<p>Subscription Plan Name</p>",Status:'<i class="iconfont icon-Active"></i>',InSession:'<i class="iconfont icon-offline"></i>',MSISDN:"544346411<p>Employ Name</p>",Action:'<a href="#" class="text-primary js-detail">Details>></a>'},
                {ICCID:"544346411<p>Subscription Plan Name</p>",Status:'<i class="iconfont icon-Active"></i>',InSession:'<i class="iconfont icon-online"></i>',MSISDN:"544346411<p>Employ Name</p>",Action:'<a href="#" class="text-primary js-detail">Details>></a>'},
                {ICCID:"3",Status:'<i class="iconfont icon-Suspend"></i>',InSession:'<i class="iconfont icon-online"></i>',MSISDN:"ELECTRICITY...",Action:'<a href="#" class="text-primary js-detail">Details>></a>'},
                {ICCID:"4",Status:'<i class="iconfont icon-Active"></i>',InSession:'<i class="iconfont icon-offline"></i>',MSISDN:"ELECTRICITY...",Action:'<a href="#" class="text-primary js-detail">Details>></a>'},
                {ICCID:"5",Status:'<i class="iconfont icon-Active"></i>',InSession:'<i class="iconfont icon-online"></i>',MSISDN:"ELECTRICITY...",Action:'<a href="#" class="text-primary js-detail">Details>></a>'},
                {ICCID:"6",Status:'<i class="iconfont icon-Active"></i>',InSession:'<i class="iconfont icon-offline"></i>',MSISDN:"ELECTRICITY...",Action:'<a href="#" class="text-primary js-detail">Details>></a>'},
                {ICCID:"7",Status:'<i class="iconfont icon-Active"></i>',InSession:'<i class="iconfont icon-online"></i>',MSISDN:"ELECTRICITY...",Action:'<a href="#" class="text-primary js-detail">Details>></a>'},
                {ICCID:"8",Status:'<i class="iconfont icon-Suspend"></i>',InSession:'<i class="iconfont icon-offline"></i>',MSISDN:"ELECTRICITY...",Action:'<a href="#" class="text-primary js-detail">Details>></a>'},
                {ICCID:"9",Status:'<i class="iconfont icon-Active"></i>',InSession:'<i class="iconfont icon-online"></i>',MSISDN:"ELECTRICITY...",Action:'<a href="#" class="text-primary js-detail">Details>></a>'},
                {ICCID:"11",Status:'<i class="iconfont icon-Active"></i>',InSession:'<i class="iconfont icon-offline"></i>',MSISDN:"ELECTRICITY...",Action:'<a href="#" class="text-primary js-detail">Details>></a>'},
                {ICCID:"12",Status:'<i class="iconfont icon-offline"></i>',InSession:'<i class="iconfont icon-offline"></i>',MSISDN:"ELECTRICITY...",Action:'<a href="#" class="text-primary js-detail">Details>></a>'},
                {ICCID:"14",Status:'<i class="iconfont icon-Active"></i>',InSession:'<i class="iconfont icon-online"></i>',MSISDN:"ELECTRICITY...",Action:'<a href="#" class="text-primary js-detail">Details>></a>'},
                {ICCID:"14",Status:'<i class="iconfont icon-Active"></i>',InSession:'<i class="iconfont icon-offline"></i>',MSISDN:"ELECTRICITY...",Action:'<a href="#" class="text-primary js-detail">Details>></a>'},
                {ICCID:"15",Status:'<i class="iconfont icon-Active"></i>',InSession:'<i class="iconfont icon-offline"></i>',MSISDN:"ELECTRICITY...",Action:'<a href="#" class="text-primary js-detail">Details>></a>'},
                {ICCID:"16",Status:'<i class="iconfont icon-offline"></i>',InSession:'<i class="iconfont icon-offline"></i>',MSISDN:"ELECTRICITY...",Action:'<a href="#" class="text-primary js-detail">Details>></a>'},
                {ICCID:"13",Status:'<i class="iconfont icon-Active"></i>',InSession:'<i class="iconfont icon-offline"></i>',MSISDN:"ELECTRICITY...",Action:'<a href="#" class="text-primary js-detail">Details>></a>'},
                {ICCID:"17",Status:'<i class="iconfont icon-Active"></i>',InSession:'<i class="iconfont icon-online"></i>',MSISDN:"ELECTRICITY...",Action:'<a href="#" class="text-primary js-detail">Details>></a>'},
                {ICCID:"18",Status:'<i class="iconfont icon-Active"></i>',InSession:'<i class="iconfont icon-offline"></i>',MSISDN:"ELECTRICITY...",Action:'<a href="#" class="text-primary js-detail">Details>></a>'},
                {ICCID:"19",Status:'<i class="iconfont icon-Active"></i>',InSession:'<i class="iconfont icon-offline"></i>',MSISDN:"ELECTRICITY...",Action:'<a href="#" class="text-primary js-detail">Details>></a>'},
                {ICCID:"20",Status:'<i class="iconfont icon-Active"></i>',InSession:'<i class="iconfont icon-offline"></i>',MSISDN:"ELECTRICITY...",Action:'<a href="#" class="text-primary js-detail">Details>></a>'}
            ];
            //参数配置
            var opt = {
                data: mydata,
                height: 'auto',
                width: '100%',
                colModel: [{
                    name: 'ICCID',
                    label: 'ICCID',
                    width: 230
                }, {
                    name: 'Status',
                    label: 'Status'
                }, {
                    name: 'InSession',
                    label: 'In Session'
                }, {
                    name: 'MSISDN',
                    label: 'MSISDN'
                }, {
                    name: 'DataUsage',
                    label: 'Data Usage'
                }, {
                    name: 'SMSUsage',
                    label: 'SMS Usage'
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
                this.$grid.grid("setGridHeight", 600);
            }
        }
    });
});