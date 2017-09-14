define([
    'hbs!modules/apnrequesthistory/templates/APNRequestHistoryTpl.hbs',
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
            this.initGridFunc();
        },
        initGridFunc: function() {
            //数据源
            var mydata = [];
            //参数配置
            var opt = {
                data: mydata,
                height: 'auto',
                width: '100%',
                colModel: [{
                    name: 'Name',
                    label: 'Access Point Name'
                }, {
                    name: 'CreatedDate',
                    label: 'Created Date'
                }, {
                    name: 'State',
                    label: 'State'
                }, {
                    name: 'UpdateDate',
                    label: 'State Update Date'
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