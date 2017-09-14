// ===========================================================================
//  主区域的视图
//  容器视图，用于管理页面布局
// ===========================================================================
define([
    'text!modules/home/templates/OverViewTmpl.html'
],function(OverviewTmpl) {
    var MainContentView = fish.View.extend({
        template: fish.compile(OverviewTmpl),
        afterRender:function(){
            var that = this;
        }
    });
    var demo = window.demo = new MainContentView();
    demo.render();
    return MainContentView;
});
