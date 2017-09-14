// ===========================================================================
//  主区域的视图
//  容器视图，用于管理页面布局
// ===========================================================================
define([
    'text!modules/home/templates/MyOrdersTmpl.html',
    "i18n!modules/i18n/selfcare.i18n",
    'modules/home/actioins/OrderAction'
],function(MyOrdersTmpl, i18Data, orderAction) {
	var btn = 1;
	var param = {};
    var MainContentView = fish.View.extend({
        template: fish.compile(MyOrdersTmpl),

        render:function(){
            this.$el.html(this.template(i18Data));
            return this;
        },
        
        afterRender:function(){
            var that = this;
            that.$('#customTime').click(function(){
            	// 设置当前按钮样式，去掉另外两个按钮样式
            	that.$('#customTime').toggleClass('current');
                that.$('.custom-time').toggleClass('show');
            	that.$('#sixMonth').removeClass('current');
            	that.$('#threeMonth').removeClass('current');
            	if(btn != 3) {
            		btn = 3;
            	} else {
            		btn = 0;
            	}
            });
            that.$("#startDate").datetimepicker({
                viewType: "date"
            });
            that.$("#endDate").datetimepicker({
                viewType: "date"
            });
            that.$("#customTimeSearch").click(that.qryByCustomTime.bind(this));
            that.$("#threeMonth").click(function(){
            	// 设置当前按钮样式，去掉另外两个按钮样式
            	that.$('#threeMonth').toggleClass('current');
            	that.$('#sixMonth').removeClass('current');
            	that.$('#customTime').removeClass('current');
            	that.$('.custom-time').removeClass('show');
            	if(btn != 1) {
            		btn = 1;
            		that.qryByThreeMonth();
            	} else {
            		btn = 0;
            	}
            });
            that.$("#sixMonth").click(function(){
            	// 设置当前按钮样式，去掉另外两个按钮样式
            	that.$('#sixMonth').toggleClass('current');
            	that.$('#threeMonth').removeClass('current');
            	that.$('#customTime').removeClass('current');
            	that.$('.custom-time').removeClass('show');
            	if(btn != 2) {
            		btn = 2;
            		that.qryBySixMonth();
            	} else {
            		btn = 0;
            	}
            });
            
//            var opt = {
//            	shrinkToFit:true,
//                height: 400,
//                colModel: [{
//                    name: 'ORDER_NBR',
//                    label: 'Order Number',
//                    width: '25%'
//                },{
//                    name: 'EVENT_NAME',
//                    label: 'Event Name',
//                    width: '25%'
//                }, {
//                    name: 'ACCEPT_DATE',
//                    label: 'Created Date',
//                    width: '25%'
//                }, {
//                    name: 'ORDER_STATE_NAME',
//                    label: 'Status',
//                    width: '25%'
//                }]
//            };
            
            // UED团队说这个菜单是查询offer历史
            var opt = {
            	shrinkToFit:false,
                height: 500,
                colModel: [{
                    name: 'OFFER_NAME',
                    label: i18Data.SC_MY_OFFER_NAME,
                    width: '200'
                },{
                    name: 'ACTIVE_DATE',
                    label: i18Data.SC_MY_ACTIVE_DATE,
                    width: '200'
                }, {
                    name: 'STATE_NAME',
                    label: i18Data.SC_STATUS,
                    width: '200'
                }, {
                	name: 'COMMENTS',
                    label: i18Data.SC_DESCRIPTION,
                    width: '200'
                }]
            };
        
            that.$grid = that.$("#myOrdersGrid").grid(opt);
//            that.$grid.grid({
//            	rowNum : 20,
//            	rowList : [5,10,20,40],
//            	height: 500,
//            	pager: true,
//            	pageData : this.getPerData
//            });
            
            // 默认查询三个月
            that.qryByThreeMonth();
        },
        
        qryByCustomTime: function(){
        	if (!$("#searchForm").isValid()) {
        		return;
        	}
        	
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
        	
        	var that = this;
        	param = {CUSTOM_TIME:true, BEGIN_DATE:$("#startDate").val(), END_DATE:$("#endDate").val()};
        	that.qryOrder();
        },
        addMonth:function(Date,offset){
        	Date.setMonth(parseInt(Date.getMonth()) + parseInt(offset));
        	return Date;
        },
        qryByThreeMonth: function() {
        	param = {THREE_MONTH:true};
        	this.qryOrder();
        }, 
        qryBySixMonth: function() {
        	param = {SIX_MONTH:true};
        	this.qryOrder();
        },
        
        qryOrder: function() {
        	var that = this;
        	orderAction.qryAddOnOfferHis(param, function(data) {
        		// 清空此前数据
            	that.$grid.grid("clearGridData");
        		if(!data || !data.ADD_ON_OFFER_HIS || data.ADD_ON_OFFER_HIS.length == 0) {
                	// fish.info("There is no record.");
                	return;
        		} else {
        			// 构造数据
        			for(var i=0; i<data.ADD_ON_OFFER_HIS.length; i++) {
        				if(data.ADD_ON_OFFER_HIS[i].STATE == "B") {
        					data.ADD_ON_OFFER_HIS[i].STATE_NAME = i18Data.SC_MY_TERMINATION;
        				} else if(data.ADD_ON_OFFER_HIS[i].STATE == "E") {
        					data.ADD_ON_OFFER_HIS[i].STATE_NAME = i18Data.SC_MY_TWO_WAY_BLOCK;
        				} else if(data.ADD_ON_OFFER_HIS[i].STATE == "X") {
        					data.ADD_ON_OFFER_HIS[i].STATE_NAME = i18Data.SC_MY_TERMINATION;
        				}
        				else if(data.ADD_ON_OFFER_HIS[i].STATE == "A") {
        					data.ADD_ON_OFFER_HIS[i].STATE_NAME = i18Data.SC_MY_ACTIVE;
        				}
        			}
        			that.$grid.grid("addRowData", data.ADD_ON_OFFER_HIS);
        		}
        	});
        }
    });
    return MainContentView;
});
