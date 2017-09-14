// ===========================================================================
//  主区域的视图
//  容器视图，用于管理页面布局
// ===========================================================================
define([
    'text!modules/home/templates/TotalDivTmpl.html',
    'utils',
    "i18n!modules/i18n/selfcare.i18n"
],function(TotalDivTmpl,utils,i18n) {
    var TotalDivView = fish.View.extend({
        template: fish.compile(TotalDivTmpl),
        data:{},

        initialize: function(options) {
        	this.data = options;
        	//$(this).addClass("account-list");
        	
        },
        render:function(){
            this.$el.html(this.template(i18n));
            return this;
        },
        afterRender:function(){
        	this.data.SHOW_UNIT = this.data.SHOW_UNIT||"";
        	var mainTotal = utils.parseFormatNum(this.data.MAIN_TOTAL, 1);
        	var usageTotal = utils.parseFormatNum(this.data.USAGE_TOTAL, 1);
        	var remainTotal = utils.parseFormatNum(this.data.REMAIN_TOTAL, 1);
        	this.$('#total').html(mainTotal+this.data.SHOW_UNIT);
        	this.$('#used').html(usageTotal +this.data.SHOW_UNIT);
        	this.$('#remain').html(remainTotal +this.data.SHOW_UNIT);
        }
    });
     
    return TotalDivView;
});
