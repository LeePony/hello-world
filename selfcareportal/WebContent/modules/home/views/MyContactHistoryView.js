// ===========================================================================
//  主区域的视图
//  容器视图，用于管理页面布局
// ===========================================================================
define([
    'text!modules/home/templates/MyContactHistoryTmpl.html',
    'utils',
    "i18n!modules/i18n/selfcare.i18n"
],function(MyContactHistoryTmpl,utils,i18n) {
	var isByCustDate=false;
    var MainContentView = fish.View.extend({
        template: fish.compile(MyContactHistoryTmpl),
        opt : {},
        events :{
        	'click #dateCon >button':'qryByMonth',
        	'click #searchByDate':'searchByDate',
        	'click #channelList > button':'qryByChannel'
        },
        render:function(){
            this.$el.html(this.template(i18n));
            return this;
        },
        qryByMonth : function(e){
        	$('#dateCon >button').filter('.current').toggleClass('current');
        	isByCustDate = false;
        	$(e.target).toggleClass('current');
        	if(!$(e.target).hasClass('clearfix')){
    			this.loadContactHistory();
    		}
        	if(e.target.id !='customTime'){
        		$('.custom-time').removeClass('show');
        	}
        	
        	
        },
        searchByDate :function(e){
        	$('#dateCon >button').filter('.current').toggleClass('current');
        	$('#customTime').toggleClass('current');
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
    		
    		isByCustDate = true;
        	this.loadContactHistory(true)
        },
        addMonth:function(Date,offset){
        	Date.setMonth(parseInt(Date.getMonth()) + parseInt(offset));
        	return Date;
        },
        qryByChannel:function(e){
        	var that = this;
        	$('#channelList >button').filter('.current').toggleClass('current');
        	$(e.target).toggleClass('current');
        	this.loadContactHistory(isByCustDate);
        },
        afterRender:function(){
            var that = this;
            this.opt = {
            		data :[],
            		pager : true,
                    shrinkToFit:false,
                    height:500,
                    colModel: [ {
                        name: 'CREATED_DATE',
                        label: i18n.SC_DATE,
                        width: 405,
                        height:100
                    }, {
                        name: 'CHANNEL_NAME',
                        label: i18n.SC_CHANNEL,
                        width: 140,
                        height:100,
                        hidden: true
                    }, {
                    	name: 'OPERATOR_TYPE',
                        label: i18n.SC_OPERATOR_TYPE,
                        width: 140,
                        height:100,
                        hidden: true
                    },
                    {
                        name: 'OPERATOR',
                        label: i18n.SC_OPERATOR,
                        width: 140,
                        height:100,
                        hidden: true
                    },
                    
                    {
                        name: 'EVENT',
                        label: i18n.SC_EVENT,
                        width: 405,
                        height:100
                    }
                    ]
                };
            that.$('#grid').grid(that.opt);
            that.$('#customTime').click(function(){
                that.$('.custom-time').toggleClass('show');
            });
            that.$("#startDate").datetimepicker({
                viewType: "date"
            });
            that.$("#endDate").datetimepicker({
                viewType: "date"
            });
            that.loadContactHistory();
        },
        loadContactHistory: function(isCustomer){
        	var timeFlag = $('#dateCon >button').filter('.current').attr("lastMonth");
        	var param = {};
        	if(isCustomer || timeFlag == "other" ){
        		param.START_DATE = $('#startDate').val();
        		param.END_DATE = $('#endDate').val();
        	}
        	else{
        		param.MONTHS = $('#dateCon >button').filter('.current').attr("lastMonth");
        	}
        	param.CHANNEL = $('#channelList >button').filter('.current').attr('channel');
        	var that = this;
        	utils.callService('qryContactHistory.do',param,function(data){
        		if(data&&data.CUST_CONTACT_LIST){
        			that.opt.data = data.CUST_CONTACT_LIST;
        			
        		}else{
        			that.opt.data = [];
        		}
//        		$('#grid').grid(that.opt);
        		$('#grid').grid("reloadData",that.opt.data);
        	})
        	
        }
    });
    return MainContentView;
});
