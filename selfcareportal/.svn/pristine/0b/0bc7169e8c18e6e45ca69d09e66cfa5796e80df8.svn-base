// ===========================================================================
//  主区域的视图
//  容器视图，用于管理页面布局
// ===========================================================================
define([
    'text!modules/home/templates/UsedBalDetail.html',
    'utils',
    "i18n!modules/i18n/selfcare.i18n"
],function(detailTml,utils,i18n) {
    var detailView = fish.View.extend({
        template: fish.compile(detailTml),
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
        	var total = utils.parseFormatNum(this.data.Total, 1);
        	var consumeBal = utils.parseFormatNum(this.data.ConsumeBal, 1);
        	var realBal = utils.parseFormatNum(this.data.RealBal, 1);
        	this.$('#total').html(total +" "+ this.data.SHOW_UNIT);
        	this.$('#usage').html(consumeBal +" " + this.data.SHOW_UNIT);
        	this.$('#remain').html(realBal +" " + this.data.SHOW_UNIT);
        	this.$('#packageName').html(this.data.ACCT_RES_NAME);
        	this.$('#percent').attr("Style","width:"+this.data.Rate+"%")
        }
    });
     
    return detailView;
});
