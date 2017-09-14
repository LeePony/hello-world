// ===========================================================================
//  主区域的视图
//  容器视图，用于管理页面布局
// ===========================================================================
define([
    'text!modules/home/templates/validateMsg4CallHistory.html',
    'utils',
    "i18n!modules/i18n/selfcare.i18n"
],function(validateMsg4CallHistory,utils,i18n) {
    var validateMsg4CallView = fish.View.extend({
        template: fish.compile(validateMsg4CallHistory),
        data:{},
        events:{
        	'click #btnGet':'getSMSCode',
        	'click #submit':'submit'
        },

        initialize: function(options) {
        	this.data = options;
        	//$(this).addClass("account-list");
        	
        },
        render:function(){
            this.setElement(this.template(i18n));
            this.$el.appendTo("body");
            
            // PIN 只能输入数字
			this.checkInputNumber();
            return this;
        },
        afterRender:function(){
        	this.getSMSCode();
        },
        getSMSCode :function(){
        	var that = this;
        	utils.callService('sendMsg4ValidateCallHistory.do',{},function(data){
        		that.setIntval($('#btnGet'),data.effSecond);
        	})
        },
        setIntval : function(obj,effSecond) {
			obj.attr("disabled", "true");
			var txt = obj.html();
			var curCount = effSecond;
			InterValObj = window.setInterval(function() {
				if (curCount == 0) {
					window.clearInterval(InterValObj);// 停止计时器
					// 通知后台 验证码已失效

					obj.removeAttr("disabled");// 启用按钮
					obj.html(txt);
				} else {
					obj.html(curCount);
					curCount--;

				}
			}, 1000);
		},
		
		checkInputNumber : function() {
			$("#code").keypress(function (event) {
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
		
		submit:function(){
			if(!$('#code').isValid()){
				return;
			}
			var param = {};
			param.MSG = $('#code').val();
			var that = this;
			utils.callService('validate4CallHistory.do',param,function(data){
        		if(data.RESULT==0){
        			fish.info(i18n.SC_VALIDATE_SUCCESS,function(){
        				that.popup.dismiss();
                		fish.trigger("qryCdr");
        			});
            		
        		}
        	})
		}
    });
     
    return validateMsg4CallView;
});
