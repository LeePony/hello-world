define([
    'hbs!modules/balanceinquiry/templates/BalanceInquiryTpl.hbs',
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
            var CreditLimit = echarts.init(document.getElementById('CreditLimit'));
            var option = {
                series : [
                    {
                        type:'pie',
                        itemStyle : {
                            normal : {
                                label : {
                                    position : 'inner',
                                    formatter : function (params) {                         
                                      return (params.percent - 0).toFixed(0) + '%'
                                    }
                                },
                                labelLine : {
                                    show : false
                                }
                            },
                            emphasis : {
                                label : {
                                    show : true,
                                    formatter : "{b}\n{d}%"
                                }
                            }
                        },
                        data:[{
                                value:40,name:'Used',selected:true,
                                itemStyle: {
                                    normal: {
                                        color: '#08579c'
                                    }
                                }
                            }, 
                            {
                                value:160,name:'Remained',
                                itemStyle: {
                                    normal: {
                                        color: '#ffbe00'
                                    }
                                }
                            }
                        ],
                        
                    }
                ]
            };

            CreditLimit.setOption(option);
        }
    });
});