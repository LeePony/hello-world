define([
    'text!modules/home/templates/FAndFNumberTmpl.html',
    'modules/home/actioins/FAndFNumberTmplAction',
    "i18n!modules/i18n/selfcare.i18n"
],function(FAndFNumberTmpl, fellowAction, i18Data) {
    var MainContentView = fish.View.extend({
        template: fish.compile(FAndFNumberTmpl),
        events: {
			"click .addFellowBtn": 'addFellowNbr'
		}, 
		render:function(){
            this.$el.html(this.template(i18Data));
            return this;
        },
        afterRender:function(){
            var that = this;
            that.$("#effectiveDate").datetimepicker({
                viewType: "datetime"
            });
            that.$("#expiryDate").datetimepicker({
                viewType: "datetime"
            });
            
            var opt = {
                shrinkToFit:false,
                colModel: [{
                    name: 'id',
                    key:true,
                    hidden:true
                }, {
                    name: 'FELLOW_NBR',
                    label: i18Data.SC_NUMBER,
                    width: 213
                }, {
                    name: 'EFF_DATE',
                    label: i18Data.SC_EFF_DATE,
                    width: 200
                }, {
                    name: 'EXP_DATE',
                    label: i18Data.SC_EXP_DATE,
                    width: 200
                }, {
                    formatter: 'actions',
                    align: "center",
                    width: 200,
                    formatoptions: {
                        editbutton: false, 
                        delbutton: false,  
                        inlineButtonAdd: [{ 
                            id: "delete", 
                            className: "delete",
                            icon: "iconfont icon-iconfontlajitong",
                            title: "delete"
                        }]
                    }
                }]
            };
            
            that.$grid = that.$("#grid").grid(opt);
            
            that.$grid.on("click", ".delete", this.delFellowNbr.bind(this));
            
            var subs = JSON.parse(sessionStorage.SESSION);
        	this.subsId = subs.SUBS_ID;

            // 查询订户有效的亲情号码
            this.loadSubsFellowNbr(this.subsId, that.$grid);
        },
        
        loadSubsFellowNbr: function(subsId, grid){
			var param = {};
			param.SUBS_ID = subsId;
			param.SUBS_QUERY_FLAG = {"QRY_FELLOW_NBR":true, "INCLUDE_TERMINATION":true};
			
			fellowAction.qrySubsFellowNbr(param, function(data){
				if(data && data.SUBS && data.SUBS.FELLOW_NBR_EX_LIST) {
					var fellowNbrList = data.SUBS.FELLOW_NBR_EX_LIST;
					grid.grid("addRowData", fellowNbrList);
				}
			});
        },
        
        addFellowNbr: function() {
        	var that = this;
        	// 校验
        	if (!$("#fellowForm").isValid()) {
        		return;
        	}
        	var param = {
        		SUBS_ID:this.subsId,
        		FELLOW_NBR:$("#fellowNbr").val(),
        		EFF_DATE:$("#effectiveDate").val(),
        		EXP_DATE:$("#expiryDate").val()
        	};
        	fish.confirm(i18Data.SC_CONFIRM_ADD_FELLOW_NUMBER).result.then(function(){
        		fellowAction.addFellowNbr(param, function(data){
            		// success
            		if(data && data.DP_DOES_NOT_EXIST == "Y" && data.RENT_LIST_PRICE) {
            			// 需要自动订购依赖产品，并且存在租费
            			fish.confirm(i18Data.SC_FELLOW_NBR_DP_OFFER1 + data.RENT_LIST_PRICE + i18Data.SC_FELLOW_NBR_DP_OFFER2).result.then(function(){
            				param.CONFIRM_ORDER_DP = "Y";
            				fellowAction.addFellowNbr(param, function(data){
            					fish.success(i18Data.SC_HINT_SUCCESS_SUBMIT_ORDER);
            					// 添加成功以后将新增的数据添加到表格中
            					that.$grid.grid("addRowData", param);
            				});
            			});
            		} else {
            			fish.success(i18Data.SC_HINT_SUCCESS_SUBMIT_ORDER);
            			that.$grid.grid("addRowData", param);
            		}
            		
    			});
        	});
        	
        },
        
        delFellowNbr: function(e) {
        	
        	var that = this;
        	
        	fish.confirm(i18Data.SC_CONFIRM_DELETE_FELLOW_NUMBER).result.then(function() {
        		var selrow = that.$grid.grid("getSelection");
        		if(selrow) {
        			var param = {SUBS_ID: that.subsId, FELLOW_NBR:selrow.FELLOW_NBR};
        			fellowAction.delFellowNbr(param, function(data){
        				// success
        				fish.success(i18Data.SC_HINT_SUCCESS_SUBMIT_ORDER);
        				// 删除行
        				that.$grid.grid("delRowData", selrow);
        			});
        		}
	        });
        }
        
    });
    return MainContentView;
});
