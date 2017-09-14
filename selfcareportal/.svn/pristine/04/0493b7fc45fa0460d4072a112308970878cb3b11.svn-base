define([
    'text!modules/home/templates/Print.html',
    "i18n!modules/i18n/selfcare.i18n",
    'utils'
],function(printTmpl,i18n,utils) {
	 var MainContentView = fish.View.extend({
	        template: fish.compile(printTmpl),
	        option:{},
	        param:{},
	        initialize: function(option,param) {
	        	this.option = option;
	        	this.param = option.param;
	        },
	        events:{
	        	'click #print1':'printAll'
	        },
	        render:function(){
	            this.setElement(this.template(i18n));
	            this.$el.appendTo("body");
	            return this;
	        },
	        afterRender:function(){
	        	var that = this;
	        	this.option.gridOption.height= 'auto';
	        	this.option.gridOption.width = 1024;
	        	utils.callService('qryCdr.do',this.param,function(data){
	        		if(data){
	        			if(data.RESULT!=1&&data.RESULT){
	        				$("#printGrid").grid(that.option.gridOption);
	        				$("#printGrid").grid("addRowData",data.DATA.CDR_LIST);
	        				$("#printAllDiv").css({'left':'0px','top':'0px','heigth':'100%'})
	        	        	$("#printAllDiv").jqprint();
	        				that.popup.close();
	        			}
	        		}
	        	},
	        	function(xhr, status, error) {
					utils.getErrorMsg(xhr, status, error);
					that.popup.close();
				})
	        },
	        printAll:function(){
	        	
	        }
	    });
	    return MainContentView;
});