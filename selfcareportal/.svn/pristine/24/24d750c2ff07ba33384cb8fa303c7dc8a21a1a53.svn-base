// ===========================================================================
//  主区域的视图
//  容器视图，用于管理页面布局
// ===========================================================================
define([
    'text!modules/home/templates/PersonalInfoTmpl.html',
    'utils',
    "i18n!modules/i18n/selfcare.i18n"
],function(PersonalInfoTmpl,utils,i18n) {
    var MainContentView = fish.View.extend({
        template: fish.compile(PersonalInfoTmpl),
        render:function(){
            this.$el.html(this.template(i18n));
            return this;
        },
        afterRender:function(){
        	var that =this;
            that.initData();
        },
        
        initData:function(){
        	var that = this;
        		var param = {};
        	//	param.SUBS_ID = subs.SUBS_ID;
        		param.SUBS_QUERY_FLAG ={"QRY_INDEP_PROD":true,"QRY_ACCT":true}
        		utils.callService('qryMySubscriber.do',param,function(data){
        			if(data.SUBS.SUBS_BASE_DETAIL){
        				var subsDetail = data.SUBS.SUBS_BASE_DETAIL
        				$('#mobileNumberLabel').text(subsDetail.ACC_NBR||'');
        				$('#subsPlanLabel').text(subsDetail.SUBS_PLAN_NAME||'');
                    	
                    	$('#statusLabel').text(subsDetail.PROD_STATE_NAME||''); 
                    	if(subsDetail.BLOCK_REASON_ID&&subsDetail.BLOCK_REASON_ID!=''){
                    		$('#hover').popover({content: i18n.SC_BLOCK_REASON + i18n[subsDetail.BLOCK_REASON_ID],trigger: 'hover'});
                    	}
                    	if(subsDetail.COMPLETED_DATE){
                    		$('#activeLabel').text(subsDetail.COMPLETED_DATE.substr(0,10)||'');
                    	}                 	
        			}
        			if(data.SUBS.ACCT_EX){
        				var flag = data.SUBS.ACCT_EX.POSTPAID =='Y'?i18n.SC_POSTPAID:i18n.SC_PREPAID;
        				$('#paidTypeLabel').text(flag||'');
        			}
        			if(data.SUBS.SUBS_CUST){
        				$("#customerName").html(data.SUBS.SUBS_CUST.CUST_NAME)
        			}
        			
                	
        		})
        		
        	
        	
        	var param = {};
        	utils.callService('QryAd.do',param,function(data){
        		if(data&&data.AD_LIST){
        			$.each(data.AD_LIST,function(idx){
        				var div = $('<div>').addClass('slide-list');
        				var img = $('<img>').attr({'width':'100%','height':'100%','src':data.AD_LIST[idx].URL});
        				div.append(img);
        				$('#adDiv').append(div);
        				
        			})
        			that.$('.slide').height((that.$('.slide').width() * 338) / 1352);
    	            slide(".slide",".slide-list",".slide-btn","cur",1000,3000);
    	            $(window).resize(function(){
    	                that.$('.slide').height((that.$('.slide').width() * 338) / 1352);
    	            });
        		}
        	})
        }
    });
    return MainContentView;
});