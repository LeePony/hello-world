// ===========================================================================
//  主区域的视图
//  容器视图，用于管理页面布局
// ===========================================================================
define([
    'text!modules/home/templates/BalanceTransferTmpl.html',
    'modules/home/actioins/TopUpViewAction',
    'modules/home/actioins/TransferAction',
    'utils',
    'i18n!modules/i18n/selfcare.i18n'
],function( BalanceTransferTmpl, topUpAction, transferAction, utils, i18Data) {
	var chargeBtn = "<button id='CHARGE_BTN' type='button' class='btn btn-date'>CHARGE_AMOUNT</button>";
	var otherBtn = "<button id='CHARGE_BTN' type='button' class='btn btn-date clearfix'>TRANSFER_OTHER</button>"+
				   "<div class='other-amount'>"+
	                   "<input id='inputOtherAmt' name='inputOtherAmt' type='text' class='form-control'"+
	               "</div>"+
	               "<span class='fl button-info'>TRANSFER_AMOUNT</span>";
	var hisNbrBtn = "<button id='NBR_BTN' type='button' class='btn btn-date'>TRANS_HIS_NBR</button>";
	var btnArr = [];
	var nbrBtnArr = [];
	var currentCharge = 0;
    var MainContentView = fish.View.extend({
    	template: fish.compile(BalanceTransferTmpl),
    	events: {
            'click #historyNum': 'historyNumFunc'
        },
        intialize:function(){
        	btnArr = [];
        	nbrBtnArr = [];
        	currentCharge = 0;
        	this.dateBtn = "";
        },
        render:function(){
            this.$el.html(this.template(i18Data));
            return this;
        },
        afterRender:function(){
      
            var that = this;
            that.$("#tabs").tabs();
            
            that.$('#threeMonth').click(function(e){
            	var btnId = e.currentTarget.id;
            	that.dateBtnClick(btnId);
            });
            that.$('#sixMonth').click(function(e){
            	var btnId = e.currentTarget.id;
            	that.dateBtnClick(btnId);
            });
            that.$('#customTime').click(function(e){
                var btnId = e.currentTarget.id;
            	that.dateBtnClick(btnId);
            });
            that.$('#searchBtn').click(function(e){
            	if (!$("#searchForm").isValid()) {
            		return;
            	}
            	that.qryTransferHis();
            });
            that.$("#startDate").datetimepicker({
                viewType: "date"
            });
            that.$("#endDate").datetimepicker({
                viewType: "date"
            });
            that.$('#transferBtn').click(function(){
            	that.transfer();
            });
            that.$('#otherAmount').click(function(){
                that.$('.other-amount').removeClass('show');
            });
            that.$('#transferHisTab').click(function(){
            	// 初始化表格
            	if(!that.$grid) {
            		var opt = {
                        	shrinkToFit:false,
                            height: 500,
                            colModel: [{
                                name: 'CREATED_DATE',
                                label: i18Data.SC_TRANSFER_DATE,
                                width: 300
                            }, {
                                name: 'IN_ACC_NBR',
                                label: i18Data.SC_RECEIVER_NUMBER,
                                width: 300
                            }, {
                                name: 'CHARGE',
                                label: i18Data.SC_TRANSFER_AMOUNT,
                                width: 300
                            }]
                       	};
            		that.$grid = that.$("#transferHisGrid").grid(opt);
            		
            		// 默认查询threeMonth
            		that.dateBtnClick("threeMonth");
            	}
            });
            
            this.subs = JSON.parse(sessionStorage.SESSION);
            this.accNbr = this.subs.ACC_NBR;
			that.$('#accNbrId').val(this.accNbr);
            
            // 查询余额
			this.qryBalance();
			
			// 加载可以转账的额度
			that.qryTransferCharges();
			
			// 查询用户最近转账的4个号码
			that.qryTransferHisNumber();
        },
        historyNumFunc:function(){
            var that = this;
            if(nbrBtnArr == null || nbrBtnArr.length == 0){
            	return;
            }
            that.$('.history-num-list').toggleClass('show');
            that.$('.transfer .form-help i').toggleClass('active');
        },
        qryBalance: function(){
        	var that = this;
        	topUpAction.qryDefBalance({}, function(data){
        		if(data) {
        			if(data.PAID_FLAG=='2'){
        				that.$("#myBalance").hide();
        			}
        			var balance = utils.parseFormatNum(data.GROSS_BAL, 1);
        			that.$('#myBalanceId').val("("+i18Data.SC_CREDIT+") " + balance);
        		}
        	});
        },
        qryTransferCharges:function(){
        	var that = this;
        	transferAction.qryTransferCharges(function(data){
        		if(data && data.TRANSFER_CHARGES) {
        			var chargeArr = data.TRANSFER_CHARGES.split(",");
        			var btnHtml = "";
        			var otherFlag = false;
        			for(var i=0; i<chargeArr.length; i++) {
        				if(i == 0) {
        					currentCharge = chargeArr[i];
        				}
        				var btnStr = "";
        				if(chargeArr[i] == "OTHER") {
        					otherFlag = true;
        					btnStr = otherBtn.replace("TRANSFER_OTHER", i18Data.SC_OTHER);
        					var between = i18Data.SC_AMOUNT_BETWEEN;
        					var min = 0;
        					var max = "";
        					if(chargeArr.length > 0 && chargeArr[0] != "OTHER") {
        						min = chargeArr[0];
        						if(chargeArr.length == 1) {
        							max = "--";
        						} else {
        							max = chargeArr[chargeArr.length - 2];
        						}
        					}
        					between = between.replace("{0}", min);
        					between = between.replace("{1}", max);
        					btnStr =  btnStr.replace("TRANSFER_AMOUNT", between);
        					
        				} else {
        					btnStr = chargeBtn.replace("CHARGE_AMOUNT", chargeArr[i]);
        				}
        				var idStr = "BTN_"+chargeArr[i];
        				btnArr.push(idStr);
        				btnStr = btnStr.replace("CHARGE_BTN", idStr);
        				btnHtml += btnStr;
        			}
        			that.$('#chargeBtnDiv').html(btnHtml);
        			for(var i=0; i<btnArr.length; i++) {
        				that.$("#"+btnArr[i]).click(function(e){
        					var idStr = e.currentTarget.id;
                        	that.chargeBtnClick(idStr);
        				});
        			}
        			// 默认选中第一个
        			that.$("#"+btnArr[0]).toggleClass("current");
        			
        			$('#transferForm').validator({
        		        rules: {
        		            isOtherAmount: function(element) {
        		                // 返回true，则验证必填
        		                return currentCharge == "OTHER";
        		            }
        		        },
        		        fields: {
        		        	inputOtherAmt: 'required(isOtherAmount); inputOtherAmt'
        		        }
        		    });
        		}
        	});
        },
        chargeBtnClick:function(idStr){
        	// 点击按钮触发时将其他按钮样式移除
        	var that = this;
        	that.$("#"+idStr).toggleClass("current");
        	if(idStr == "BTN_OTHER") {
        		// 显示其他金额
        		that.$('.other-amount').toggleClass('show');
        	} else {
        		// 隐藏其他金额
        		that.$('.other-amount').removeClass('show');
        	}
        	for(var i=0; i<btnArr.length; i++) {
        		if(idStr != btnArr[i]) {
        			that.$("#"+btnArr[i]).removeClass("current");
        		}
			}
        	//记录金额
        	var selCharge = idStr.substring('BTN_'.length, idStr.length);
        	if(selCharge == currentCharge) {
        		that.$("#"+idStr).toggleClass("current");
        		if(selCharge == "OTHER"){
        			that.$('.other-amount').toggleClass('show');
        		}
        	}
        	currentCharge = selCharge;
        },
        qryTransferHisNumber:function() {
        	var that = this;
        	transferAction.qryTransferHisNbr(function(data){
        		if(data && data.TRANSFER_HIS_NBR) {
        			var nbrArr = data.TRANSFER_HIS_NBR.split(",");
        			var btnHtml = "";
        			nbrBtnArr  = [];
        			for(var i=0; i<nbrArr.length; i++){
        				var btnStr = hisNbrBtn.replace("TRANS_HIS_NBR", nbrArr[i]);
        				var idStr = "NBR_"+nbrArr[i];
        				btnStr = btnStr.replace("NBR_BTN", idStr);
        				nbrBtnArr.push(idStr);
        				btnHtml+=btnStr;
        			}
        			that.$('#hisNbrDiv').html(btnHtml+"<div class='arrow-top'></div>");
        			for(var i=0; i<nbrBtnArr.length;i++){
        				that.$("#"+nbrBtnArr[i]).click(function(e){
        					var idStr = e.currentTarget.id;
        					that.nbrBtnClick(idStr);
        				});
        			}
        		}
        	});
        },
        nbrBtnClick:function(idStr){
        	var that = this;
        	that.$("#"+idStr).toggleClass("current");
        	// 将输入框号码设置为选择的号码
        	that.$('#recNbr').val(idStr.substring('NBR_'.length, idStr.length));
        	for(var i=0; i<nbrBtnArr.length; i++) {
        		if(idStr != nbrBtnArr[i]) {
        			that.$("#"+nbrBtnArr[i]).removeClass("current");
        		}
			}
        	// 关闭历史号码框
        	that.$('.history-num-list').removeClass('show');
        },
        transfer:function(){
        	var that = this;
        	if (!$("#transferForm").isValid()) {
        		return;
        	}
        	 
        	// 校验转账金额不能超过余额
        	var charge = currentCharge;
        	if(charge == "OTHER") {
        		charge = that.$('#inputOtherAmt').val();
        	}
        	var receveNbr = that.$("#recNbr").val();
        	
        	transferAction.qryTransferCharge({"CHARGE":charge},function(data){
        		var msg = i18Data.SC_TRANS_CONFIRM_MSG;
        		msg = msg.replace("{0}",data.CHARGE)
        		fish.confirm(msg).result.then(function(){
        			transferAction.transferCharge({CHARGE:charge, RECEVE_NBR:receveNbr}, function(data){
        				if (data) {
        					if (!data.ERROR_CODE || data.ERROR_CODE == null || data.ERROR_CODE == "") {
        						fish.success(i18Data.SC_HINT_SUCCESS_TRANSFER);
        					}
        					else {
        						fish.error(data.ERROR_MESSAGE);
        					}
        				}
                		
                		// 查询号码历史
                		that.qryTransferHisNumber();
                		// 查询余额更新余额
                		that.qryBalance();
                	});
            	});
        	})
        },
        dateBtnClick:function(btnId) {
        	
        	if(this.dateBtn == btnId){
        		return;
        	}
        	else if(btnId == "threeMonth"){
        		this.$('#threeMonth').toggleClass('current');
        		this.$('#sixMonth').removeClass('current');
        		this.$('#customTime').removeClass('current');
        		this.$('.custom-time').removeClass('show');
        	} else if(btnId == "sixMonth") {
        		this.$('#threeMonth').removeClass('current');
        		this.$('#sixMonth').toggleClass('current');
        		this.$('#customTime').removeClass('current');
        		this.$('.custom-time').removeClass('show');
        		
        	} else if(btnId == "customTime") {
        		this.$('#threeMonth').removeClass('current');
        		this.$('#sixMonth').removeClass('current');
        		this.$('#customTime').toggleClass('current');
        		this.$('.custom-time').toggleClass('show');
        	}
        	this.dateBtn = btnId;
        	if(btnId == "threeMonth" || btnId == "sixMonth") {
        		// 查询数据
        		this.qryTransferHis();
        	}
        },
        
        qryTransferHis:function(){
        	var that = this;
        	var param = {};
        	if(this.dateBtn == "threeMonth") {
        		param = {THREE_MONTH:true};
        	} else if(this.dateBtn == "sixMonth") {
        		param = {SIX_MONTH:true};
        	} else if(this.dateBtn == "customTime") {
        		
        		var startDate = new Date($('#startDate').val());
        		var bef6 = this.addMonth(new Date(),-6);
        		if(startDate-bef6 < 0){
        			fish.info(i18Data.SC_EXCEED_SIX_MONTH);
        			return;
        		}
        		var endDate = new Date($('#endDate').val());
        		if (endDate && endDate - startDate < 0){
        			fish.info(i18Data.SC_START_END_DATE_EXCEPTION);
        			return;
        		}
        		
        		
        		param = {
        				CUSTOM_TIME:true,
        				BEGIN_DATE:$("#startDate").val(),
        				END_DATE:$("#endDate").val()
        			};
        	}
        	transferAction.qryTransferHis(param, function(data){
        		// 清空此前数据
            	that.$grid.grid("clearGridData");
        		if(data && data.TRANSFER_LIST) {
                	that.$grid.grid("addRowData", data.TRANSFER_LIST);
        		}
        	});
        },
        
        addMonth:function(Date,offset){
        	Date.setMonth(parseInt(Date.getMonth()) + parseInt(offset));
        	return Date;
        }
    });
    return MainContentView;
});
