define([
    'hbs!modules/orderhistory/templates/OrderHistoryTpl.hbs',
    'i18n!i18n/common.i18n'
],function(template, i18n) {
    return fish.View.extend({
        el: false,
        template: template,
        //提供模板数据
        serialize: i18n,
        //视图事件定义
        events:{
            'click .js-shink':'onShink'
        },
        //一些初始化设置 (不能进行dom操作)
        initialize: function() {
        },
        //视图渲染完毕处理函数
        afterRender: function() {
            this._initPubVia();
            this.initGridFunc();
        },
        _initPubVia: function(){
            this.$grid = this.$('#Grid');
        },
        initGridFunc: function() {
            //数据源
            var mydata = [
                {NumberOrder:"1",OrderNo:'456',OrderedNo:'456',OperateDate:"2015-06-18 10:00:00",EventName:"Change APN",action:'<span href="#" class="text-primary">View Details <i class="iconfont icon-jiantou js-shink"></i></span>'},
                {NumberOrder:"1",OrderNo:'456',OrderedNo:'456',OperateDate:"2015-06-18 10:00:00",EventName:"Change APN",action:'<span href="#" class="text-primary">View Details <i class="iconfont icon-jiantou js-shink"></i></span>'},
                {NumberOrder:"1",OrderNo:'456',OrderedNo:'456',OperateDate:"2015-06-18 10:00:00",EventName:"Change APN",action:'<span href="#" class="text-primary">View Details <i class="iconfont icon-jiantou js-shink"></i></span>'},
                {NumberOrder:"1",OrderNo:'456',OrderedNo:'456',OperateDate:"2015-06-18 10:00:00",EventName:"Change APN",action:'<span href="#" class="text-primary">View Details <i class="iconfont icon-jiantou js-shink"></i></span>'},
                {NumberOrder:"1",OrderNo:'456',OrderedNo:'456',OperateDate:"2015-06-18 10:00:00",EventName:"Change APN",action:'<span href="#" class="text-primary">View Details <i class="iconfont icon-jiantou js-shink"></i></span>'}
            ];
            //参数配置
            var opt = {
                data: mydata,
                height: 'auto',
                width: '100%',
                colModel: [{
                    name: 'NumberOrder',
                    label: 'Number Order'
                }, {
                    name: 'OrderNo',
                    label: 'Order No.'
                }, {
                    name: 'OrderedNo',
                    label: 'Ordered No.'
                }, {
                    name: 'OperateDate',
                    label: 'Operate Date'
                }, {
                    name: 'EventName',
                    label: 'Event Name'
                }, {
                    name: 'action',
                    label: ''
                }],
                rowNum: 9,
                pager: true
            };
            //加载grid
            this.$grid.grid(opt);
            var data = this.$grid.grid("getRowData");
            this.$grid.grid("setSelection", data[0]);

            if (mydata.length > 9) {
                this.$grid.grid("setGridHeight", 390);
            }
        },
        onShink: function (e) {
            var rowData = this.$grid.grid('getSelection');
            var subRow = this.$(e.target).parents('tr').next();
            var curRow = this.$(e.target).parents('tr');
            if (subRow.hasClass('sub-row')) {
                if(curRow.hasClass('js-expandRow')) {
                    subRow.hide();
                    curRow.removeClass('js-expandRow');
                    curRow.find('.js-shink').removeClass('icon-jiantou2').addClass('icon-jiantou');
                } else {
                    subRow.show();
                    curRow.addClass('js-expandRow');
                    curRow.find('.js-shink').removeClass('icon-jiantou').addClass('icon-jiantou2');
                }
            } else {
                curRow.after('<tr role="row" class="sub-row">' +
                '<td colspan="6" class= "subRowTd_'+ rowData._id_ +'"></td></tr>');
                curRow.find('.js-shink').removeClass('icon-jiantou').addClass('icon-jiantou2');
                curRow.addClass('js-expandRow');
                this.$('td.subRowTd_'+ rowData._id_).html(this.$('.flow-box').html());
            }
        }
    });
});