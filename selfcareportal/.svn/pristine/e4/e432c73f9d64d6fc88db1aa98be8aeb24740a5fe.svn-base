define([
     
    'text!modules/home/templates/OverViewTmpl.html',
    "i18n!modules/i18n/selfcare.i18n",
    'utils',
    'modules/home/views/detailView',
    'modules/home/views/TotalDivView'
],function(OverviewTmpl,i18n,utils,detailView,totalDivView) {
    var MainContentView = fish.View.extend({
        template: fish.compile(OverviewTmpl),
        render:function(){
            this.$el.html(this.template(i18n));
            return this;
        },
        events:{
        	'click #topUpBtn':'topUp',
        	'click #detailBtn':'showDetail',
        	'click #contactUsBtn':'contactUs'
        },
        showDetail:function(){
        	fish.trigger('callHistory','');
        },
        topUp:function (){
        	fish.trigger('topUp','');
        },
        contactUs:function(){
        	fish.trigger('contactUs','');
        },
        afterRender:function(){
            var that = this;
            var myChart = echarts.init(document.getElementById('billingCycle'));
            var option = {
                    title: {
                    text: '',
                    subtext: i18n.SC_KYATS,
                    x: 'center',
                    y: 'center',
                    itemGap: 2,
                    textStyle : {
                        color : '#fd0909',
                        fontSize : 24,
                        fontWeight : 'bold'
                    },
                    subtextStyle : {
                        color : '#555',
                        fontSize : 16
                    }
                },
                tooltip: {
                    trigger: 'item',
                    formatter: "{a} <br/>{b}: <br/>{c} ({d}%)",
                    	position: function(p) {
                            return [p[0] -50, p[1] +20];
                        }
                },
                series: [
                    {
                        name:'Usage Total',
                        type:'pie',
                        radius: ['50%', '80%'],
                        avoidLabelOverlap: false,
                        label: {
                            normal: {
                                show: false,
                                position: 'center',
                            },
                            emphasis: {
                                show: false,
                                textStyle: {
                                    fontSize: '14',
                                    fontWeight: 'bold'
                                }
                            }
                        },
                        labelLine: {
                            normal: {
                                show: false
                            }
                        },
                        data:[
                            {
                                value:10, name:i18n.SC_ONETIME_CHARGE,
                                itemStyle: {
                                    normal: {
                                        color: '#ffca05'
                                    }
                                }
                            },
                            {
                                value:66, name:i18n.SC_MONTHLY_CHARGE,
                                itemStyle: {
                                    normal: {
                                        color: '#66cbff'
                                    }
                                }
                            }
                        ]
                    }
                ]
            };
            that.initData(option,myChart);
           
            // 查询用户是否上传过图片，如果没有上传过则跳转到CustProfile界面
            that.toCustProf();
        },
        initData:function(option,myChart){
        	var that = this;
//        	var session = JSON.parse(sessionStorage.SESSION);
        	$('#monthDiv').html((new Date().toDateString().substring(4,7)) + (new Date().toDateString().substring(10)));
        	utils.callService('qryCurCharge.do',{},function (data){
        		if(data){
        			var chartData = option.series[0].data;
        			chartData[0].value = data.EventCharge;
        			chartData[1].value = data.RecurringCharge;
        			option.title.text = data.UsageTotal;
        			var recurringCharge = utils.parseFormatNum(data.RecurringCharge, 1);
        			var eventCharge = utils.parseFormatNum(data.EventCharge, 1);
        			var usageTotal = utils.parseFormatNum(data.UsageTotal, 1);
        			$('#recurringChargeSpan').html(recurringCharge +'  '+ i18n.SC_KYATS);
        			$('#onetimeChargeSpan').html(eventCharge +'  '+ i18n.SC_KYATS);
        			$('#totalSpan').html(usageTotal +'  '+ i18n.SC_KYATS);
            		 myChart.setOption(option);
        		}
        		
        	});
        	utils.callService('qryUsageDetail.do',{},function(data){
        		
        		if(data){
        			if(data.D){
        				that.addPackage("#totalData","#dataPackage",data.D)
        			}
        			if(data.V){
        				that.addPackage("#totalVoice","#voicepackage",data.V)
        			}
        			if(data.S){
        				that.addPackage("#totalSms","#smspackage",data.S)
        			}
        			if(data.O){
        				that.addPackage("#totalOther","#otherpackage",data.O)
        			}
        			$('#points').html(data.POINTS||"");
        			var consumed = utils.parseFormatNum(data.Consumed, 1);
        			$('#totalUsage').html(consumed +" "+i18n.SC_KYATS);
        			var advance = utils.parseFormatNum(data.Advance, 1);
        			$('#defBal').html(advance+" "+i18n.SC_KYATS);
        			if(data.POSTPAID=='2'){
        				$('#postpaidDiv').show();
        				$('#prepaidDiv').hide();
        			}else{
        				$('#prepaidDiv').show();
        				$('#postpaidDiv').hide();
        			}
        		}
        		
        	});
			utils.callService('QryPaymentHis.do',{"SIX_MONTH":true},function(data1){
				if(data1&&data1.PAYMENT_LIST){
					var e = data1.PAYMENT_LIST[0];
					if(e){
						var charge = utils.parseFormatNum(e.CHARGE, 1);
						$('#charge').html(charge + " " +i18n.SC_KYATS);
					}
					
				}
			});
			
			var param = {};
    		param.SUBS_QUERY_FLAG ={"QRY_INDEP_PROD":true,"QRY_ACCT":true}
    		utils.callService('qryMySubscriber.do',param,function(data){
    			if(data.SUBS.ACCT_EX && data.SUBS.ACCT_EX.POSTPAID =='N'){
    				$("#balanceDiv").show();
    			}
    			else if (data.SUBS.ACCT_EX && data.SUBS.ACCT_EX.POSTPAID =='Y') {
    				$("#balanceDiv").show();
    				$('#topUpBtn').hide();
    			}
    		});
        },
        addPackage:function(totalSel,selector,data){
        	var that = this;
        	if(data.BAL_LIST== null){
        		return;
        	}
        	
				$.each(data.BAL_LIST,function(index,item){
					item.SHOW_UNIT = data.SHOW_UNIT
					var v = new detailView(item);
    	        	that.setView(selector,v,true);
    	        	v.render();
				})
				if(data.BAL_LIST.length>1){
					var v0 = new totalDivView(data);
		        	that.setView(totalSel,v0,true);
		        	v0.render();
				}
	        	
        },
        toCustProf:function(){
        	utils.callService('CheckCustNRC.do',{},function(data){
        		if(data.UPLOAD_NRC && data.UPLOAD_NRC == "N") {
        			fish.trigger('custProf','');
        		}
        	});
        }
    });
    
    return MainContentView;
});
