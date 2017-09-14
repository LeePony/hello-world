// ===========================================================================
//  主区域的视图
//  容器视图，用于管理页面布局
// ===========================================================================
define([ 'text!modules/home/templates/TopUpTmpl.html',
         "i18n!modules/i18n/selfcare.i18n",
         'utils',
		'modules/home/actioins/TopUpViewAction' ], function(TopUpTmpl,i18Data, utils,
		topUpAction) {

	var MainContentView = fish.View.extend({
		template : fish.compile(TopUpTmpl),

		events : {
			"click #rechargeBtn" : 'recharge'
		},
		
		render:function(){
            this.$el.html(this.template(i18Data));
            return this;
        },

		intialize : function() {
			this.btn = 0;
			this.param = {};
		},

		afterRender : function() {
			var that = this;

			that.$("#tabs").tabs();

			that.$('#customTime').click(function() {
				// 设置当前按钮样式，去掉另外两个按钮样式
				that.$('#customTime').toggleClass('current');
				that.$('.custom-time').toggleClass('show');
				that.$('#sixMonth').removeClass('current');
				that.$('#threeMonth').removeClass('current');
				if (this.btn != 3) {
					this.btn = 3;
				} else {
					this.btn = 0;
				}
			});
			that.$("#startDate").datetimepicker({
				viewType : "date"
			});
			that.$("#endDate").datetimepicker({
				viewType : "date"
			});
			that.$("#customTimeSearch").click(that.qryByCustomTime.bind(this));
			that.$("#threeMonth").click(function() {
				// 设置当前按钮样式，去掉另外两个按钮样式
				that.$('#threeMonth').toggleClass('current');
				that.$('#sixMonth').removeClass('current');
				that.$('#customTime').removeClass('current');
				that.$('.custom-time').removeClass('show');
				if (this.btn != 1) {
					this.btn = 1;
					that.qryByThreeMonth();
				} else {
					this.btn = 0;
				}
			});
			that.$("#sixMonth").click(function() {
				// 设置当前按钮样式，去掉另外两个按钮样式
				that.$('#sixMonth').toggleClass('current');
				that.$('#threeMonth').removeClass('current');
				that.$('#customTime').removeClass('current');
				that.$('.custom-time').removeClass('show');
				if (this.btn != 2) {
					this.btn = 2;
					that.qryBySixMonth();
				} else {
					this.btn = 0;
				}
			});

			var opt = {
				shrinkToFit : false,
				height : 500,
				colModel : [ {
					name : 'CREATED_DATE',
					label : i18Data.SC_PAYMENT_DATE,
					width : 200
				}, {
					name : 'PAYMENT_METHOD_NAME',
					label : i18Data.SC_PAYMENT_METHOD,
					width : 200
				}, {
					name : 'CHARGE',
					label : i18Data.SC_SUBMIT_AMOUNT,
					width : 200
				}, {
					name : 'EXP_DATE',
					label : i18Data.SC_BALANCE_EXPIRE_DATE,
					width : 200
				} ]
			};

			that.$('#hisTab').click(function() {
				if (!that.$grid) {
					that.$grid = that.$("#paymentHisGrid").grid(opt);

					// that.$grid.grid({
					// rowNum : 20,
					// rowList : [5,10,20,40],
					// height: 200,
					// pager: true,
					// pageData : this.loadGrid(false)
					// });
					
					// 默认查询threeMonth
					that.$("#threeMonth").click();
				}
			});

			this.subs = JSON.parse(sessionStorage.SESSION);
			this.subsId = this.subs.SUBS_ID;
			this.acctId = this.subs.ACCT_ID;
			this.accNbr = this.subs.ACC_NBR;
			that.$('.accNbrLabel').val(this.accNbr);
			that.$('#custNameId').val(this.subs.CUST_NAME);

			// 查询余额
			this.qryBalance();
			
			// PIN 只能输入数字
			this.checkInputNumber();
		},

		qryBalance : function() {
			var that = this;
			var param = {
				SUBS_ID : this.subsId,
				ACCT_ID : this.acctId
			};
			topUpAction.qryDefBalance(param, function(data) {
				if (data) {
					var balance = utils.parseFormatNum(data.GROSS_BAL, 1);
					that.$('#myBalanceId').val("("+i18Data.SC_CREDIT+") " + balance);
				}
			});
		},

		recharge : function() {
			// 校验
			if (!$("#rechargeForm").isValid()) {
				return;
			}
			var that = this;
			var param = {
				SUBS_ID : this.subsId,
				VOUCHER_CARD : that.$('#vcCardId').val()
			};
			topUpAction.vcRecharge(param, function(data) {
				fish.success(i18Data.SC_HINT_RECHARGE_SUCCESS);
				that.$('#vcCardId').val('');
				that.qryBalance();
			});
		},

		qryByCustomTime : function() {
			if (!$("#searchForm").isValid()) {
				return;
			}
			var that = this;
			
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
				CUSTOM_TIME : true,
				BEGIN_DATE : $("#startDate").val(),
				END_DATE : $("#endDate").val()
			};
			// this.loadGrid(true);
			that.qryPaymentHis();
		},
		
		addMonth:function(Date,offset){
        	Date.setMonth(parseInt(Date.getMonth()) + parseInt(offset));
        	return Date;
        },

		qryByThreeMonth : function() {
			param = {
				THREE_MONTH : true
			};
			// this.loadGrid(true);
			this.qryPaymentHis();
		},

		qryBySixMonth : function() {
			param = {
				SIX_MONTH : true
			};
			// this.loadGrid(true);
			this.qryPaymentHis();
		},

		qryPaymentHis : function() {
			var that = this;
			topUpAction.qryPaymentHis(param, function(data) {
				// 清空此前数据
				that.$grid.grid("clearGridData");
				if (!data || !data.PAYMENT_LIST
						|| data.PAYMENT_LIST.length == 0) {
					// fish.info("There is no record.");
					that.$('#records').val("0 "+i18Data.SC_RECORD);
					that.$('#totalAmount').val(data.AMOUNT);
					return;
				} else {
					that.$grid.grid("addRowData", data.PAYMENT_LIST);
					that.$('#records')
							.val(data.PAYMENT_LIST.length + " " + i18Data.SC_RECORD);
					that.$('#totalAmount').val(data.AMOUNT);
				}
			});
		},
		
		checkInputNumber : function() {
			$("#vcCardId").keypress(function (event) {
		        var eventObj = event || e;
		        var keyCode = eventObj.keyCode || eventObj.which;
		        if ((keyCode >= 48 && keyCode <= 57)) {
		        	return true;
		        }
		        else {
		            return false;
		        }
		    }).focus(function () {
		    	//禁用输入法
		        this.style.imeMode = 'disabled';
		    }).bind("paste", function () {
		    	//获取剪切板的内容
		        var clipboard = window.clipboardData.getData("Text");
		        if (/^\d+$/.test(clipboard)) {
		        	return true;
		        }
		        else {
		            return false;
		        }
		    });
		},

		loadGrid : function(reset) {
			if (this.btn == 0) {
				return;
			}
			var that = this;
			topUpAction.qryPaymentHisCount(param,
					function(data) {
						if (!data || data.CNT == 0) {
							// 清空此前数据
							that.$grid.grid("clearGridData");
							// fish.info("There is no record.");
							that.$('#records').val("0 "+ i18Data.SC_RECORD);
							return;
						}
						var count = data.CNT;
						that.$('#records').val(count + " " + i18Data.SC_RECORD);
						pageLength = that.$grid.grid("getGridParam", "rowNum");
						pageIndex = reset ? 1 : that.$grid.grid("getGridParam",
								"page");
						var index = pageIndex - 1;
						param.zsmart_query_page = {
							"page_index" : pageIndex + "",
							"page_size" : pageLength + ""
						};
						topUpAction.qryPaymentHis(param, function(data) {
							var list = data.PAYMENT_LIST;
							// that.$grid.grid("addRowData", list);
							that.$grid.grid("reloadData", {
								'rows' : list,
								'page' : pageIndex,
								'records' : count,
							});
						});
					});
		}
	});
	return MainContentView;
});
