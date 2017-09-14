// ===========================================================================
//  主区域的视图
//  容器视图，用于管理页面布局
// ===========================================================================
define([
    'BaseView',
    'text!modules/home/templates/PersonalInfoTmpl.html',
    'text!modules/home/templates/LoyaltyPointTmpl.html'
],function(BaseView, PersonalInfoTmpl, LoyaltyPointTmpl) {
    var MainContentView = BaseView.extend({
        template: LoyaltyPointTmpl,
        afterRender:function(){
        	debugger
            var that = this;
            that.$("#personal-info").html(PersonalInfoTmpl);
            that.initData();
        },
        initData:function(){
        	$('#mobileNumberLabel').html('111')
        	$('#mobileNumberLabel').html('111')
        }
    });
    return MainContentView;
});
