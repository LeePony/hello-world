// ===========================================================================
//  主区域的视图
//  容器视图，用于管理页面布局
// ===========================================================================
define([
    'i18n!modules/i18n/selfcare.i18n',
    'text!modules/home/templates/LoanBalanceTmpl.html',
    'utils',
    'modules/home/actioins/LoanBalanceAction'
],function(i18Data, LoanBalanceTmpl,utils,loanBalanceAction) {
	var chargeBtn = "<button id='CHARGE_BTN' type='button' class='btn btn-date'>CHARGE_AMOUNT</button>";
	var btnArr = [];
	var nbrBtnArr = [];
	var currentCharge = 0;
	
    var MainContentView = fish.View.extend({
        template: fish.compile(LoanBalanceTmpl),
        
        events: {
        	"click #loanBalBtn": 'loanBal',
        	"click #nearlyThreeBtn": 'nearlyThree',
        	"click #nearlySixBtn": 'nearlySix',
        	"click #customTime": 'customTimeSearch',
        	"click #searchBtn": 'search'
        },
        
        render:function(){
            this.$el.html(this.template(i18Data));
            return this;
        },
        
        afterRender:function(){
            var that = this;
            that.$('#customTime').click(function(){
                that.$('.custom-time').toggleClass('show');
            });
            that.$("#startDate").datetimepicker({
                viewType: "date"
            });
            that.$("#endDate").datetimepicker({
                viewType: "date"
            });
            var subs = JSON.parse(sessionStorage.SESSION);
            this.subsId = subs.SUBS_ID;
            this.acctId = subs.ACCT_ID;
            this.accNbr = subs.ACC_NBR;
            that.$("#mobileNumber").val(this.accNbr);
            
            that.$("#tabs").tabs();
            
            var opt = {
                	shrinkToFit:false,
                    height: 600,
                    colModel: [{
                        name: 'CREATED_DATE',
                        label: i18Data.SC_LOAN_DATE,
                        width: 270
                    }, {
                        name: 'LOAN_AMOUNT',
                        label: i18Data.SC_LOAN_AMOUNT,
                        width: 270
                    },{
                    	name: 'COMMISSION',
                        label: i18Data.SC_SERVICE_CHARGE,
                        width: 270
                    }]
                };
            
            that.$('#hisTab').click(function() {
				if (!that.$grid) {
					that.$grid = that.$("#loanBalanceHisGrid").grid(opt);

					// that.$grid.grid({
					// rowNum : 20,
					// rowList : [5,10,20,40],
					// height: 200,
					// pager: true,
					// pageData : this.loadGrid(false)
					// });
					
					// 默认查询threeMonth
					that.nearlyThree();
				}
			});
            
            that.qryBalance();
            
            that.qryLoanBal();
            
        },
    
        qryBalance: function(){
    	    var that = this;
    	    var param = {SUBS_ID: this.subsId, ACCT_ID: this.acctId};
    	    loanBalanceAction.qryDefBalance(param, function(data){
    		    if(data) {
    		    	var balance = utils.parseFormatNum(data.GROSS_BAL, 1);
    			     that.$('#myBalanceId').val("("+i18Data.SC_CREDIT+") " + balance);
    		    }
    	    });
         },
         qryLoanBal: function(){
        	 var selLoan='<button type="button" click="selLoanBal(0)" class="btn btn-date">0Ks</button>';
        	 var param={};
        	 var that=this;
        	 loanBalanceAction.qryLoanBal(param,function(data){
//        		 that.$("#loanAmount").val(data.LOAN_AMOUNT);
        		 if(data && data.LOAN_AMOUNT){
        			var loanAmountArr = data.LOAN_AMOUNT.split(",");
        			var btnHtml = "";
         			var otherFlag = false;
         			for(var i=0; i<loanAmountArr.length; i++) {
         				if(i == 0) {
         					currentCharge = loanAmountArr[i];
         				}
         				var btnStr = "";
         				if(loanAmountArr[i] == "OTHER") {
         					otherFlag = true;
         					btnStr = otherBtn.replace("TRANSFER_OTHER", i18Data.SC_OTHER);
         					var between = i18Data.SC_AMOUNT_BETWEEN;
         					var min = 0;
         					var max = "";
         					if(loanAmountArr.length > 0 && loanAmountArr[0] != "OTHER") {
         						min = loanAmountArr[0];
         						if(loanAmountArr.length == 1) {
         							max = "--";
         						} else {
         							max = loanAmountArr[loanAmountArr.length - 2];
         						}
         					}
         					between = between.replace("{0}", min);
         					between = between.replace("{1}", max);
         					btnStr =  btnStr.replace("TRANSFER_AMOUNT", between);
         					
         				} else {
         					btnStr = chargeBtn.replace("CHARGE_AMOUNT", loanAmountArr[i]);
         				}
         				var idStr = "BTN_"+loanAmountArr[i];
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
         loanBal: function(){
        	 //先查询手续费
        	 var that = this;
        	 
         	loanBalanceAction.qryLoanCharge({"LOAN_AMOUNT":currentCharge},function(data){
         		var msg = i18Data.SC_LOAN_CONFIRM_MSG;
         		msg = msg.replace("{0}",data.CHARGE)
         		fish.confirm(msg).result.then(function(){
             		var param={"SUBS_ID":this.subsId, "LOAN_AMOUNT":currentCharge};
                 	
                 	loanBalanceAction.loanBal(param, function(data){
                 		fish.success("Loan balance Success!");
                 		that.qryBalance();
                 	});
         		});
         	});
        },
        //查询三个月
        nearlyThree:function(){
        	this.$('#nearlyThreeBtn').toggleClass('current');
        	this.$('#nearlySixBtn').removeClass('current');
        	this.$('#customTime').removeClass('current');
        	this.$('.custom-time').removeClass('show');
        	this.SEARCH_TYPE=3;
        	this.search();
        },
        //查询六个月
        nearlySix:function(){
        	this.$('#nearlySixBtn').toggleClass('current');
        	this.$('#nearlyThreeBtn').removeClass('current');
        	this.$('#customTime').removeClass('current');
        	this.$('.custom-time').removeClass('show');
        	this.SEARCH_TYPE=6;
        	this.search();
        },
        customTimeSearch:function(){
        	this.$('#customTime').toggleClass('current');
        	this.$('#nearlyThreeBtn').removeClass('current');
        	this.$('#nearlySixBtn').removeClass('current');
        	this.SEARCH_TYPE=-1;
        },
        //搜索
        search:function(){
        	var param = null;
        	if (!this.SEARCH_TYPE) {
        		return;
        	}
        	if (this.SEARCH_TYPE==3 || this.SEARCH_TYPE==6){
        		param={"SUBS_ID":this.subsId, "SEARCH_TYPE":this.SEARCH_TYPE}
        	}
        	else{
        		var startDate = new Date($('#startDate').val());
        		var endDate = new Date($('#endDate').val());
        		if (endDate && endDate - startDate < 0){
        			fish.info(i18Data.SC_START_END_DATE_EXCEPTION);
        			return;
        		}
        		
        		var startDate = this.$("#startDate").val();
        		var endDate = this.$("#endDate").val();
        		param={"SUBS_ID":this.subsId, "SEARCH_TYPE":0, "START_DATE":startDate, "END_DATE":endDate}	
        	}
        	
        	var that = this;
        	loanBalanceAction.qryLoanBalHis(param, function(data){
        		// 清空此前数据
            	that.$grid.grid("clearGridData");
        		if(!data || !data.LOAN_BAL_LIST || data.LOAN_BAL_LIST.length == 0) {
//                	fish.info("There is no record.");
                	that.$('#records').val("0 Record");
//                	that.$('#totalAmount').val(data.AMOUNT);
                	return;
        		} else {
        			that.$grid.grid("addRowData", data.LOAN_BAL_LIST);
        			that.$('#records').val(data.LOAN_BAL_LIST.length + " Record");
//        			that.$('#totalAmount').val(data.AMOUNT);
        		}
        		
        	});
        	//清楚缓存
        	this.SEARCH_TYPE=-1;
        }
    
    });
    return MainContentView;
});
