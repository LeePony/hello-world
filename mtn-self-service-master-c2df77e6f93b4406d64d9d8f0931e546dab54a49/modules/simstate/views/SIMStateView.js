define([
    'hbs!modules/simstate/templates/SIMStateTpl.hbs',
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
            var SIMState = echarts.init(document.getElementById('SIMState'));
            var option = {
                title: {
                    text: 'The Number Each State Card',
                    itemGap: 20,
                    subtext: 'All=43'
                },
                grid: {
                    x: 100
                },
                color: ['#ffbe00'],
                axis : [
                    {
                        type : 'category', 
                        splitArea: {
                            show: true,
                            areaStyle: {
                                color: [
                                    '#fff',
                                    '#f9f9f9'
                                ]
                            }        
                        }
                    }
                ],
                xAxis : [
                    {
                        type : 'category',
                        data : ['Active','Termination','One-Way Block','Two-Way Block','Inactive'],
                        axisLine: {
                            lineStyle: {
                                color: '#ffbe00',
                                width: 1,
                                type: 'solid'
                            }
                        },
                        splitLine: {
                            lineStyle: {
                                color: '#e9e9e9',
                                width: 1,
                                type: 'solid'
                            }
                        },
                        axisTick: {
                            show: false
                        }
                    }
                ],
                yAxis : [
                    {
                        type : 'value',
                        axisLine: {
                            lineStyle: {
                                color: '#ffbe00',
                                width: 2,
                                type: 'solid'
                            }
                        },
                        splitLine: {
                            lineStyle: {
                                color: '#e9e9e9',
                                width: 1,
                                type: 'solid'
                            }
                        }
                    }
                ],
                series : [
                    {
                        type:'bar',
                        data:[10, 26, 0, 4, 1],
                        itemStyle: {
                            normal: {
                                label : {
                                    show: true,
                                    textStyle: {
                                        color: '#373737'
                                    }
                                }
                            }
                        }
                    }
                ]
            };

            SIMState.setOption(option);
        }
    });
});