// ===========================================================================
//  主区域的视图
//  容器视图，用于管理页面布局
// ===========================================================================
define([
    "i18n!modules/i18n/selfcare.i18n",
    'text!modules/home/templates/AddOnOfferPopTmpl.html',
    'modules/home/actioins/AddOnOfferAction'
],function(i18Data, AddOnOfferPopTmpl, addOnOfferAction) {
    var MainContentView = fish.View.extend({
        template: fish.compile(AddOnOfferPopTmpl),
        events: {
            'click #save':'save',
            'click #cancel':'cancel'
        },
        initialize: function(option) {
        	this.option = option;
        },
        render:function(){
            this.setElement(this.template(i18Data));
            this.$el.appendTo("body");
            return this;
        },
        afterRender:function(){
        	var that = this;
        	this.$('#orderAccNbr').val(this.option.ACC_NBR);
        	this.$('#orderOfferName').val(this.option.OFFER_NAME);
    		this.$('#orderCharge').val(this.option.RECEIVABLE_CHARGE);
    		this.$('#orderBal').val(this.option.REAL_BAL);
    		this.balInt = this.option.BAL_INT;
    		this.chargeInt = this.option.CHARGE_INT;
    		this.offerId = this.option.OFFER_ID;
    		this.offerCode = this.option.OFFER_CODE;
    		if(this.option.POST_PAID=="N"){
    			$("#balanceDiv").show();
    			$("#chargeDiv").show();
    		}
    		this.$('#topUpLink').click(function(){
    			fish.trigger('topUp','');
    			that.cancel();
    		});
        },
        save:function(){
        	var that = this;
        	if(this.balInt != null && this.chargeInt != null && this.balInt < this.chargeInt) {
        		fish.info(i18Data.SC_HINT_BAL_NOT_ENOUGH);
        		return;
        	}
        	var param = {OFFER_ID:this.offerId, OFFER_CODE:this.offerCode};
        	
        	addOnOfferAction.addOffer(param, function(data){
        		fish.success(i18Data.SC_HINT_SUCCESS_SUBMIT_ORDER);
        		that.popup.dismiss();
        	});
        },
        cancel:function(){
            this.popup.close();
        }
    });
    return MainContentView;
});
