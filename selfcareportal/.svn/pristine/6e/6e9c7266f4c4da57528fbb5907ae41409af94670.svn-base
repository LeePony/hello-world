// ===========================================================================
//  主区域的视图
//  容器视图，用于管理页面布局
// ===========================================================================
define([
    'text!modules/home/templates/SimLostRestoreTmpl.html',
    "i18n!modules/i18n/selfcare.i18n",
    'modules/home/actioins/SimLostRestoreAction'
],function(SimLostRestoreTmpl,i18Data,simAction) {
    var MainContentView = fish.View.extend({
        template: fish.compile(SimLostRestoreTmpl),
        render:function(){
            this.$el.html(this.template(i18Data));
            return this;
        },
        afterRender:function(){
            var that = this;
            that.$(".btn_submit").hide();
            that.loadSimCardInfo();
        },
        
        events: {
    		"click .btn_submit": 'lostSimCard'
		},
		
		// 查询订户卡信息
		loadSimCardInfo: function() {
			var that = this;
			var subs = JSON.parse(sessionStorage.SESSION);
			this.subsId = subs.SUBS_ID;
			this.accNbr = subs.ACC_NBR;
			this.$(".accNbrId").val(this.accNbr);
			
			var param = {};
			param.SUBS_ID = this.subsId;
			param.SUBS_QUERY_FLAG = {QRY_INDEP_PROD:true,QRY_USED_RES:true,INCLUDE_TERMINATION:true};
			simAction.qrySubsInfo(param, function(data){
				if(data && data.SUBS) {
					subsDetail =  data.SUBS;
					var stateName = subsDetail.SUBS_BASE_DETAIL.PROD_STATE_NAME;
					var prodStateDate = subsDetail.SUBS_BASE_DETAIL.PROD_STATE_DATE;
					var blockReason = subsDetail.SUBS_BASE_DETAIL.BLOCK_REASON;
					prodState = subsDetail.SUBS_BASE_DETAIL.PROD_STATE;
					prefix = subsDetail.SUBS_BASE_DETAIL.PREFIX;
					this.$(".prodStateNameId").val(stateName);
					this.$(".stateDateId").val(prodStateDate);

					// 如果状态是A则按钮显示sim card lost，如果状态是E并且停机位是卡挂失停机则显示sim card restore
					if(prodState == "A"){
						this.$(".btn_submit").val(i18Data.SC_SIM_CARD_LOST);
						this.$(".btn_submit").show();
					} else if((prodState == "E" || prodState == "D") && blockReason != null && blockReason != ""){
						if(blockReason.substring(7, 8) == "2" || blockReason.substring(7, 8) == "1") {
							this.$(".btn_submit").text(i18Data.SC_SIM_CARD_RESTORE);
							this.$(".btn_submit").show();
						}
					}
					
					// usedRes
					if(subsDetail.USED_RES_EX_LIST && subsDetail.USED_RES_EX_LIST.length > 0) {
						for(var i=0; i<subsDetail.USED_RES_EX_LIST.length; i++) {
							if(subsDetail.USED_RES_EX_LIST[i].RES_TYPE == "B") {
								this.simCardId = subsDetail.USED_RES_EX_LIST[i].RES_ID;
								this.$(".iccidId").val(subsDetail.USED_RES_EX_LIST[i].RES_NBR);
								break;
							}
						}
					}
					if(this.simCardId != null) {
						// qry puk1, puk2
						simAction.qrySimCardInfo({SIM_CARD_ID:this.simCardId} , function(data) {
							if(data) {
								this.$(".puk1Id").val(data.PUK1);
								this.$(".puk2Id").val(data.PUK2);
							}
						});
					}
				}
			});
		},
		
		lostSimCard: function(e) {
			var that = this;
			var param = {SUBS_ID:this.subsId};
			
			if(prodState == "A") {
				
				fish.confirm(i18Data.SC_CONFIRM_LOST_SIM_CARD).result.then(function() {
					simAction.simCardLost( param, function(data) {
						// success
						fish.success(i18Data.SC_HINT_SUCCESS_SUBMIT_ORDER);
						
						that.$(".btn_submit").hide();
					});
		        });
				
			} else {
				
				fish.confirm(i18Data.SC_CONFIRM_RESTORE_SIM_CARD).result.then(function(){
					simAction.simCardRestore(param , function(data) {
						// success
						fish.success(i18Data.SC_HINT_SUCCESS_SUBMIT_ORDER);
						
						that.$(".btn_submit").hide();
					});
				});
			}
        }
    });
    return MainContentView;
});
