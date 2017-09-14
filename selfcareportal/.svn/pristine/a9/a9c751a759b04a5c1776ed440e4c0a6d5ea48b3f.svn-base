// ===========================================================================
//  主区域的视图
//  容器视图，用于管理页面布局
// ===========================================================================
define([
    'text!modules/home/templates/MyLoginHistoryTmpl.html',
    "i18n!modules/i18n/selfcare.i18n",
    'utils'
],function(MyLoginHistoryTmpl,i18n,utils) {
    var MainContentView = fish.View.extend({
        template: fish.compile(MyLoginHistoryTmpl),
        loginHistoryData :[],
        opt : {},
        events :{
        	'click #threeMonth':'qryThreeMonth',
        	'click #sixMonth':'qrySixMonth',
        	'click #qryByDate':'qryByDate'
        },
        render:function(){
            this.$el.html(this.template(i18n));
            return this;
        },
        subs : JSON.parse(sessionStorage.SESSION),
        afterRender:function(){
            var that = this;
            this.opt = {
            		data :[],
            		pager : true,
                    shrinkToFit:false,
                    height:500,
                    colModel: [{
                        name: 'ACC_NBR',
                        label: i18n.SC_SUBSCRIBER_NUMBER,
                        width: 420,
                        height:100
                    }, {
                        name: 'LOGIN_DATE',
                        label: i18n.SC_LOGIN_DATE,
                        width: 420,
                        height:100
                    }]
                };
            that.$('#customTime').click(function(){
                that.$('.custom-time').toggleClass('show');
                $('.current').toggleClass('current');
            	$(this).toggleClass('current');
            });
            that.$("#startDate").datetimepicker({
                viewType: "date"
            });
            that.$("#endDate").datetimepicker({
                viewType: "date"
            });
            that.qryThreeMonth();
        },
        qryThreeMonth : function(){
        	$('.current').toggleClass('current');
        	$('#threeMonth').toggleClass('current');
        	$('.custom-time').removeClass('show');
        	this.loadLoginHistory(-3);
        	
        },
        qrySixMonth :function(){
        	$('.current').toggleClass('current');
        	$('#sixMonth').toggleClass('current');
        	$('.custom-time').removeClass('show');
        	this.loadLoginHistory(-6);
        	
        },
        qryByDate :function(){
        	
        	if(!$('#startDate').isValid()||!$('#endDate').isValid()){
        		return;
        	}
        	
        	var startDate = new Date($('#startDate').val());
    		var bef6 = this.addMonth(new Date(),-6);
    		if(startDate-bef6 < 0){
    			fish.info(i18n.SC_EXCEED_SIX_MONTH);
    			return;
    		}
    		
    		var endDate = new Date($('#endDate').val());
    		if (endDate && endDate - startDate < 0){
    			fish.info(i18n.SC_START_END_DATE_EXCEPTION);
    			return;
    		}
    		
        	$('.current').toggleClass('current');
        	$('#customTime').toggleClass('current');
        	this.loadLoginHistory(null,$('#startDate').val(),$("#endDate").val())
        },
        loadLoginHistory: function(month,startDate,endDate){
        	var param = {};
        	//param.SUBS_ID = this.subs.SUBS_ID;
        	param.MONTHS = month;
        	param.START_DATE = startDate;
        	param.END_DATE = endDate;
        	var that = this;
        	utils.callService('qryLoginHistory.do',param,function(data){
        		if(data&&data.LOGIN_LIST){
        			that.opt.data = data.LOGIN_LIST;
        		}else{
        			that.opt.data = [];
        		}
        		$('#grid').grid(that.opt);
        	})
        	
        },
        addMonth:function(Date,offset){
        	Date.setMonth(parseInt(Date.getMonth()) + parseInt(offset));
        	return Date;
        }
    });
    return MainContentView;
});
