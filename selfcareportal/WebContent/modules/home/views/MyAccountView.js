// ===========================================================================
//  主区域的视图
//  容器视图，用于管理页面布局
// ===========================================================================
define([
    'modules/home/views/BounsView',
    'text!modules/home/templates/MyAccountTmpl.html',
    'utils',
    "i18n!modules/i18n/selfcare.i18n"
],function(BounsView, MyAccountTmpl,utils,i18n) {
    var MainContentView = fish.View.extend({
        template: fish.compile(MyAccountTmpl),
        sel :'',
        events:{
        	'click a[name = topup]':'gotoTopup',
        	'click #more':'gotoTopup',      		
        },
        afterRender:function(){
            var that = this;
            that.initData();
           
           
        },
        gotoTopup :function(){
        	fish.trigger('topUp','');
        },
        render:function(){
            this.$el.html(this.template(i18n));
            return this;
        },
        addRow:function(data){
        	var that = this;
        	var v = new BounsView (data);
        	this.setView("#bounsDiv",v,true);

        	v.render();
        	//$(v.el).find("div:first").addClass("account-list")
        },
        queryPaymentList :function(){
        	var param ={};
        	param.THREE_MONTH = "THREE_MONTH";
        	utils.callService('QryPaymentHis.do',{},function(data){
        		if(data&&data.PAYMENT_LIST){
        			
        		}
        	})
        },
        qryAcct:function(){
        	var that = this;
        	var param ={};
        	param.ACCT_CODE =$(that.sel).combobox('value');
        	utils.callService('qryAcctInfo.do',param,function(data){
        		if(data && data.RESULT != -1){

        			if(data.POST_PAID_FLAG){
        				$('#paidFlag').val(data.POST_PAID_FLAG=='Y'?i18n.SC_POSTPAID:i18n.SC_PREPAID)
        			}
        			if(data.defaultBal){
        			//	$('#defBal').html(data.Consumed||"");
        			}
        			that.removeView("#bounsDiv");
        			if(data.BalDtoList){
        				
        				for(var i=0;i<data.BalDtoList.length;i++){
        					that.addRow(data.BalDtoList[i]);
        				}      				
        			}
        			if(data.POST_PAID_FLAG=='Y'){
        				$("#postpaid").show();
        				$("#prepaid").hide();
        				var consume = utils.parseFormatNum(data.CONSUMED||'0', 1);
        				$('#due').html(consume);
        			}else{
        				$("#prepaid").show();
        				$("#postpaid").hide();
        				var consume = utils.parseFormatNum(data.CONSUMED||'0', 1);
        				var advance = utils.parseFormatNum(data.ADVANCE||'0', 1);
        				$('#used').html(consume+" "+ i18n.SC_KYATS);
        				$('#remain').html(advance +" " +i18n.SC_KYATS );
        				var param ={};
        				param.THREE_MONTH = true;
        				utils.callService('QryPaymentHis.do',param,function(data1){
        					if(data1&&data1.PAYMENT_LIST){
        						var e = data1.PAYMENT_LIST[0];
        						if(e){
        							$('#chargeDate').html(e.CREATED_DATE==null?"":e.CREATED_DATE.substring(0,10));
        							var charge = utils.parseFormatNum(e.CHARGE, 1);
            						$('#charge').html(charge +i18n.SC_KYATS);
        						}
        						
        					}
        				});
        				
        			}
        		}       		
        	});
        },
        initData:function(){
        	var that = this;
        	var datas={};
        	
        	utils.callService('queryAllAcct.do',{},function(data){
            	if(data&&data.ACCT_LIST){
            		 var sel = $('#selAccount').combobox({
                         placeholder: 'Please Select',
                         dataTextField: 'ACCT_NBR',
                         dataValueField: 'ACCT_NBR',
                         dataSource: data.ACCT_LIST
                     });
            		 sel.on('combobox:change', function (e) {
                         that.qryAcct();
                      });

                     that.sel = sel;
                     var session = JSON.parse(sessionStorage.SESSION);
                     if(session!=null){
                    	 
                    	 sel.combobox('value', session.ACCT_CODE);
                     }
            	}
            });
            
        	var param = {};
    		param.SUBS_QUERY_FLAG ={"QRY_INDEP_PROD":true,"QRY_ACCT":true}
    		utils.callService('qryMySubscriber.do',param,function(data){
    			if(data.SUBS.ACCT_EX && data.SUBS.ACCT_EX.POSTPAID =='N'){
    			}
    			else if (data.SUBS.ACCT_EX && data.SUBS.ACCT_EX.POSTPAID =='Y') {

    				$('#topupid').hide();
    			}
    		});
        }
        	
        	
        
    });
    
    return MainContentView;
});
