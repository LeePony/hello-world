// ===========================================================================
//  主区域的视图
//  容器视图，用于管理页面布局
// ===========================================================================
define([
    'text!modules/home/templates/BounsTmpl.html',
    'utils',
    "i18n!modules/i18n/selfcare.i18n"
],function(BounsTmpl,utils,i18n) {
    var BounsView = fish.View.extend({
        template: fish.compile(BounsTmpl),
        data:{},
        events:{
        	'click #topup':'gotoTopup'
        },
        initialize: function(options) {
        	this.data = options;
        	//$(this).addClass("account-list");
        	
        },
        render:function(){
            this.$el.html(this.template(i18n));
            return this;
        },
        afterRender:function(){       	
        	this.$('#effDate').html(this.data.EFF_DATE||"");     
        	this.$('#expDate').html(this.data.EXP_DATE||""); 
        	this.$('#acctResName').html(this.data.ACCT_RES_NAME||"");  
        	var consume = utils.parseFormatNum(this.data.ConsumeBal||"0", 1);
        	var remain = utils.parseFormatNum(this.data.RealBal||"0", 1);
        	this.$('#consumeBal').html(consume +" " + this.data.SHOW_UNIT);
        	this.$('#remainBal').html(remain +" " +this.data.SHOW_UNIT);
        	$(this.el).addClass("account-list");
        }
    });
     
    return BounsView;
});
