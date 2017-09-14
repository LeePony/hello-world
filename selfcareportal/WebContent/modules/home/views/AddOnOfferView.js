define([
    'text!modules/home/templates/AddOnOfferTmpl.html',
    "i18n!modules/i18n/selfcare.i18n",
    'modules/home/actioins/AddOnOfferAction'
],function(AddOnOfferTmpl,i18Data, addOnOfferAction) {
	var dpStr = "<li class='price-group-item'>"+
						    "<div class='row'>"+
							    "<div class='col-xs-6 col-sm-5 col-md-6 first'>"+
							        "<div class='item-label item-text'>OFFER_NAME</div>"+
							        "<div class='des item-text'>EFF_DATE</div>"+
							    "</div>"+
							    "<div class='col-xs-3 col-sm-4 col-md-3 second'>"+
							        "<div class='item-label item-text'>MONTHLY_RENTAL</div>"+
							        "<div class='cash item-text'>RENT_LIST_PRICE</div>"+
							    "</div>"+
//							    "<div class='col-xs-3 col-sm-3 col-md-3 third'>"+
//							        "<button id='CANCEL_BTN' class='btn btn-default'>CANCEL_LABEL</button>"+
//							    "</div>"+
							 "</div>"+
						"</li>";
	var dpStr2 = "<li class='price-group-item'>"+
    "<div class='row'>"+
	    "<div class='col-xs-6 col-sm-5 col-md-6 first'>"+
	        "<div class='item-label item-text'>OFFER_NAME</div>"+
	        "<div class='des item-text'>EFF_DATE</div>"+
	    "</div>"+
	    "<div class='col-xs-3 col-sm-4 col-md-3 second'>"+
	        "<div class='item-label item-text'>MONTHLY_RENTAL</div>"+
	        "<div class='cash item-text'>RENT_LIST_PRICE</div>"+
	    "</div>"+
	    "<div class='col-xs-3 col-sm-3 col-md-3 third'>"+
	        "<button id='CANCEL_BTN' class='btn btn-default'>CANCEL_LABEL</button>"+
	    "</div>"+
	 "</div>"+
    "</li>";
	
	var offerGorupStr = "<h3 class='menu-title'>OFFER_GROUP_NAME (OFFER_GROUP_MEM_COUNT)"+
                            "<i class='menu-iocn-up iconfont icon-jiantou'></i>"+
                            "<i class='menu-iocn-down iconfont icon-jiantou-copy'></i>"+
                        "</h3>";
	var offerGroupMem = "<li class='price-group-item'>"+
                            "<div class='row'>"+
                                "<div class='col-xs-6 col-sm-5 col-md-6 first'>"+
                                    "<div class='item-label item-text'>OFFER_NAME</div>"+
                                    "<div class='des item-text'>EFF_DATE</div>"+
                                "</div>"+
                                "<div class='col-xs-3 col-sm-4 col-md-3 second'>"+
                                    "<div class='item-label item-text'>MONTHLY_RENTAL</div>"+
                                    "<div class='cash item-text'>RENT_LIST_PRICE</div>"+
                                "</div>"+
                                "<div class='col-xs-3 col-sm-3 col-md-3 third'>"+
                                    "<button id='ORDER_BTN' class='btn btn-primary'>BUY_LABEL</button>"+
                                "</div>"+
                            "</div>"+
                        "</li>";
	var balInt = null;
	var chargeInt = null;
    var MainContentView = fish.View.extend({
    	template: fish.compile(AddOnOfferTmpl),
    	
    	render:function(){
            this.$el.html(this.template(i18Data));
            return this;
        },
        
        afterRender:function(){
            var that = this;
            
            that.$("#tabs").tabs({
                paging: true,
                autoResizable:true
            });
            
            this.qrySubsOffer();
            
            that.$('#orderOkBtn').click(function(){
            	that.orderOffer();
            });
            
            $(window).scroll(function(){  
                if ($(window).scrollTop()>100){  
                    that.$("#BackToTop").fadeIn(500);  
                } else {  
                    that.$("#BackToTop").fadeOut(500);  
                }  
            }); 
            that.$("#BackToTop").click(function(){  
                $('body,html').animate({scrollTop:0},500);  
                return false;  
            });

        },
        
        qrySubsOffer:function(){
        	var that = this;
        	// 包含实例数据和规格数据
        	addOnOfferAction.qrySubsOffers(function(data){
        		// 构造资费计划数据
        		if(data.SUBS_UPP_LIST && data.SUBS_UPP_LIST.length > 0) {
        			that.loadSubsUpp(data.SUBS_UPP_LIST);
        		}
        		if(data.DEPEND_PROD_LIST && data.DEPEND_PROD_LIST.length > 0) {
        			that.loadDependProd(data.DEPEND_PROD_LIST);
        		}
        		if(data.PRICE_GROUP_LIST && data.PRICE_GROUP_LIST.length > 0){
        			that.loadOfferGroup(data.PRICE_GROUP_LIST, true);
        		}
        		if(data.DEPEND_GROUP_LIST && data.DEPEND_GROUP_LIST.length > 0) {
        			that.loadOfferGroup(data.DEPEND_GROUP_LIST, false);
        		}
        		
        		that.$('.accordion-menu .menu-title').click(function() {
                    $(this).parent('.accordion-menu').toggleClass('menu-open').children('.menu-section').toggleClass('section-open');
                });
        	});
        },
        
        loadSubsUpp:function(subsUppList) {
        	var that = this;
        	var htmlStr = "";
        	var idArr = [];
        	for(var i=0; i<subsUppList.length; i++) {
        		var str = dpStr.replace("OFFER_NAME", subsUppList[i].OFFER_NAME);
        		str = str.replace("EFF_DATE", subsUppList[i].EFF_DATE);
        		str = str.replace("RENT_LIST_PRICE", subsUppList[i].RENT_LIST_PRICE ? subsUppList[i].RENT_LIST_PRICE + " " +i18Data.SC_KYATS:i18Data.SC_FREE);
        		str = str.replace("MONTHLY_RENTAL", i18Data.SC_MONTHLY_RENTAL);
        		str = str.replace("CANCEL_LABEL", i18Data.SC_CANCEL);
        		var idStr = subsUppList[i].SUBS_UPP_INST_ID + "_" + subsUppList[i].PRICE_PLAN_ID;
//        		str = str.replace("CANCEL_BTN", idStr);
        		idArr.push("#"+idStr);
        		htmlStr += str;
        	}
        	this.$('#subsUppUl').html(htmlStr);
        	// 将id监听事件
        	if(idArr.length > 0) {
        		for(var i=0; i<idArr.length; i++) {
        			that.$(idArr[i]).click(function(e){
        				var idStr = e.currentTarget.id;
                    	that.cancelSubsUpp(idStr);
                    });
        		}
        	}
        },
        
        cancelSubsUpp:function(idStr) {
        	var that = this;
        	var param = null;
        	if(idStr) {
        		var idArr = idStr.split("_");
        		param = {SUBS_UPP_INST_ID:idArr[0], OFFER_ID:idArr[1]};
        	}
        	fish.confirm(i18Data.SC_CONFIRM_CANCEL_PACKAGE).result.then(function() {
        		addOnOfferAction.delOffer(param, function(data){
        			fish.success(i18Data.SC_HINT_SUCCESS_SUBMIT_ORDER);
        			that.qrySubsOffer();
        		});
        	});
        },
        
        loadDependProd:function(dependProdList){
        	var that = this;
        	var htmlStr = "";
        	var idArr = [];
        	for(var i=0; i<dependProdList.length; i++) {
        		var str = dpStr2.replace("OFFER_NAME", dependProdList[i].OFFER_NAME);
        		str = str.replace("EFF_DATE", dependProdList[i].COMPLETED_DATE);
        		str = str.replace("RENT_LIST_PRICE", dependProdList[i].RENT_LIST_PRICE ? dependProdList[i].RENT_LIST_PRICE + " "+i18Data.SC_KYATS:i18Data.SC_FREE);
        		str = str.replace("MONTHLY_RENTAL", i18Data.SC_MONTHLY_RENTAL);
        		str = str.replace("CANCEL_LABEL", i18Data.SC_DISABLE);
        		var idStr = dependProdList[i].PROD_ID + "_" +dependProdList[i].OFFER_ID;
        		str = str.replace("CANCEL_BTN", idStr);
        		idArr.push("#"+idStr);
        		htmlStr += str;
        	}
        	this.$('#dependProdUl').html(htmlStr);
        	// 将id监听事件
        	if(idArr.length > 0) {
        		for(var i=0; i<idArr.length; i++) {
        			that.$(idArr[i]).click(function(e){
        				var idStr = e.currentTarget.id;
                    	that.cancelDependProd(idStr);
                    });
        		}
        	}
        },
        
        cancelDependProd:function(idStr) {
        	var that = this;
        	var param = null;
        	if(idStr) {
        		var idArr = idStr.split("_");
        		param = {PROD_ID:idArr[0], OFFER_ID:idArr[1]};
        	}
        	fish.confirm(i18Data.SC_CONFIRM_DISABLE_VAS).result.then(function() {
        		addOnOfferAction.delOffer(param, function(data){
        			fish.success(i18Data.SC_HINT_SUCCESS_SUBMIT_ORDER);
        			that.qrySubsOffer();
        		});
        	});
        },
        
        loadOfferGroup:function(offerGroupList, isPricePlan) {
        	var that = this;
        	var groupHtml = "";
        	var orderBtnArr = [];
        	for(var i=0; i<offerGroupList.length; i++){
        		var childList = offerGroupList[i].CHILDREN;
        		var groupStr = offerGorupStr.replace("OFFER_GROUP_NAME", offerGroupList[i].OFFER_GROUP_NAME);
        		groupStr = groupStr.replace("OFFER_GROUP_MEM_COUNT", childList.length);
        		var childHtml = "";
        		for(var j=0; j<childList.length; j++) {
        			var childStr = offerGroupMem.replace("OFFER_NAME", childList[j].OFFER_NAME);
        			childStr = childStr.replace("EFF_DATE", childList[j].OFFER.EFF_DATE);
        			childStr = childStr.replace("RENT_LIST_PRICE", childList[j].OFFER.RENT_LIST_PRICE ? childList[j].OFFER.RENT_LIST_PRICE + " "+i18Data.SC_KYATS:i18Data.SC_FREE);
        			childStr = childStr.replace("BUY_LABEL", isPricePlan ? i18Data.SC_BUY : i18Data.SC_ENABLE);
        			childStr = childStr.replace("MONTHLY_RENTAL", i18Data.SC_MONTHLY_RENTAL);
        			var btnId = offerGroupList[i].OFFER_GROUP_ID + "ORDER_BTN" + childList[j].OFFER.OFFER_ID;
        			var key = "#" + btnId;
        			orderBtnArr.push(key);
        			childStr = childStr.replace("ORDER_BTN", btnId);
        			childHtml += childStr;
        		}
        		childHtml = "<div class='menu-section'><ul class='price-group-ditail'>" + childHtml + "</ul></div>";
        		groupStr += childHtml;
        		groupStr = "<div class='accordion-menu'>" + groupStr + "</div>";
        		groupHtml += groupStr;
        	}
        	if(isPricePlan) {
        		this.$('#pricePlanOfferGroupDiv').html(groupHtml);
        	}
        	else {
        		this.$('#dependOfferGroupDiv').html(groupHtml);
        	}
        	
        	if(orderBtnArr.length > 0) {
        		for(var i=0; i< orderBtnArr.length; i++) {
        			that.$(orderBtnArr[i]).click(function(e){
        				var idStr = e.currentTarget.id;
        				idStr.indexOf('N')+1
                    	that.qryOrderFee(idStr.substring(idStr.indexOf('N')+1, idStr.length));
                    });
        		}
        	}
        },
        
        qryOrderFee:function(offerId) {
        	var that = this;
        	
        	// 根据订购的offer调用后台得到费用返回界面显示
        	addOnOfferAction.qryDpOrderFee({OFFER_ID:offerId}, function(data){
        		// 将数据显示到弹出界面中
        		var params = {
        			ACC_NBR:data.ACC_NBR,
        			OFFER_NAME:data.OFFER_NAME,
        			OFFER_CODE:data.OFFER_CODE,
        			RECEIVABLE_CHARGE:data.RECEIVABLE_CHARGE,
        			GROSS_BAL:data.GROSS_BAL,
        			REAL_BAL:data.REAL_BAL,
        			OFFER_ID:offerId,
        			BAL_INT:data.BAL_INT,
        			CHARGE_INT:data.CHARGE_INT,
        			POST_PAID:data.POST_PAID
        		};
        		
        		// 弹出提交页面
        		fish.popupView({
                    url:"modules/home/views/AddOnOfferPopView",
                    viewOption:params
                    //dismiss:that.submitCallBack
                });
        		
        	});
        },
        
        submitCallBack:function(){
        	// 更新数据
        	this.qrySubsOffer();
        },
        
        orderOffer:function() {
        	var that = this;
        	if(balInt != null && chargeInt != null && balInt < chargeInt) {
        		fish.info(i18Data.SC_HINT_BAL_NOT_ENOUGH);
        		return;
        	}
        	var param = {OFFER_ID:that.$('#offerIdTxt').val()};
        	addOnOfferAction.addOffer(param, function(data){
        		fish.success(i18Data.SC_HINT_SUCCESS_SUBMIT_ORDER);
        		
        		that.qrySubsOffer();
        	});
        }
    });
    return MainContentView;
});