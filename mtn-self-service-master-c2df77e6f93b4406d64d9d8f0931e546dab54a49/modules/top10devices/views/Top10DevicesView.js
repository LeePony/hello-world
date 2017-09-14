define([
    'hbs!modules/top10devices/templates/Top10DevicesTpl.hbs',
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
            this.$('#Tabs').tabs();
            this.initGridFunc();
        },
        initGridFunc: function() {
            //数据源
            var mydata = [
                {id:"1",ICCID:'89860 0MFSS YYGXX XXXXP',Data:'200M<span class="progressbar" style="width:100%;"></span>'},
                {id:"2",ICCID:'89860 0MFSS YYGXX XXXXP',Data:'180M<span class="progressbar" style="width:90%;"></span>'},
                {id:"3",ICCID:'89860 0MFSS YYGXX XXXXP',Data:'120M<span class="progressbar" style="width:60%;"></span>'},
                {id:"4",ICCID:'89860 0MFSS YYGXX XXXXP',Data:'60M<span class="progressbar" style="width:30%;"></span>'},
                {id:"5",ICCID:'89860 0MFSS YYGXX XXXXP',Data:'40M<span class="progressbar" style="width:20%;"></span>'}
            ];
            //参数配置
            var opt = {
                data: mydata,
                height: 'auto',
                width: '100%',
                colModel: [{
                    name: 'id',
                    label: '',
                    width: '5%'
                }, {
                    name: 'ICCID',
                    label: 'ICCID',
                    width: '25%'
                }, {
                    name: 'Data',
                    label: 'Data',
                    width: '70%'
                }]
            };
            //加载grid
            this.$('#Grid').grid(opt);
            this.$('#Grid').grid("getRowData");
        }
    });
});