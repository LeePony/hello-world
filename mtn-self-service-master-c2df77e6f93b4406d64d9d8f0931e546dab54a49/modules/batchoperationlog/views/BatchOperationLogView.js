define([
    'hbs!modules/batchoperationlog/templates/BatchOperationLogTpl.hbs',
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
            this.$('#ChangeDate').combobox({
                dataTextField: 'name',
                dataValueField: 'value',
                dataSource: [
                    {name: '2017-01', value: '1'},
                    {name: '2016-12', value: '2'},
                    {name: '2016-11', value: '3'},
                    {name: '2016-10', value: '4'}
                ]
            });
            this.$('#BatchState').combobox({
                dataTextField: 'name',
                dataValueField: 'value',
                dataSource: [
                    {name: '2017-01', value: '1'},
                    {name: '2016-12', value: '2'},
                    {name: '2016-11', value: '3'},
                    {name: '2016-10', value: '4'}
                ]
            });
            this.$('#BatchEvent').combobox({
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

            this.$('#CreateTimeStart').datetimepicker({
                viewType: "date"
            });
            this.$('#CreateTimeEnd').datetimepicker({
                viewType: "date"
            });
            this.$('#ExecutionTimeStart').datetimepicker({
                viewType: "date"
            });
            this.$('#ExecutionTimeEnd').datetimepicker({
                viewType: "date"
            });
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
                    name: 'BatchNo',
                    label: 'Batch No.'
                }, {
                    name: 'BatchEvent',
                    label: 'Batch Event'
                }, {
                    name: 'BatchState',
                    label: 'Batch State'
                }, {
                    name: 'CreateDate',
                    label: 'Create Date'
                }, {
                    name: 'Operator',
                    label: 'Operator'
                }, {
                    name: 'ExecutionTime',
                    label: 'Execution Time'
                }, {
                    name: 'Comments',
                    label: 'Comments'
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