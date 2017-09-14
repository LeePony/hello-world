// ===========================================================================
//  主区域的视图
//  容器视图，用于管理页面布局
// ===========================================================================
define([
    'text!modules/home/templates/ChangePasswordTmpl.html',
    "i18n!modules/i18n/selfcare.i18n",
    'utils'
],function(ChangePasswordTmpl,i18n,utils) {
	var flag = "false";
    var MainContentView = fish.View.extend({
        template: fish.compile(ChangePasswordTmpl),
        subs : JSON.parse(sessionStorage.SESSION),
        events :{
        	'click #changePwd' : 'changePwd'
        },
        afterRender:function(){
            var that = this;
            that.initData();
        },
        render:function(){
            this.$el.html(this.template(i18n));
            return this;
        },
        initData : function(){
        	if(this.subs!=null){
        		$('#cust').val(this.subs.CUST_NAME);
        		$('#AccNbr').val(this.subs.ACC_NBR);
        		$('#password').val("");
        		$('#oldPassword').val("");
        		$('#repeatPwd').val("");
        		
        		$("#oldPasswordImgClose").css("display", "none");
        		$("#oldPasswordImg, #oldPasswordImgClose").on("click",function() {
        			if ($("#oldPassword").attr("type") == "password") {
        	            $("#oldPassword").attr("type", "text");
        	            $("#oldPasswordImg").css("display", "none");
        	            $("#oldPasswordImgClose").css("display", "inline-block");
        	        }
        	        else {
        	            $("#oldPassword").attr("type", "password");
        	            $("#oldPasswordImg").css("display", "inline-block");
        	            $("#oldPasswordImgClose").css("display", "none");
        	        }
        		});
        		
        		$("#passwordImgClose").css("display", "none");
        		$("#passwordImg, #passwordImgClose").on("click",function() {
        			if ($("#password").attr("type") == "password") {
        	            $("#password").attr("type", "text");
        	            $("#passwordImg").css("display", "none");
        	            $("#passwordImgClose").css("display", "inline-block");
        	        }
        	        else {
        	            $("#password").attr("type", "password");
        	            $("#passwordImg").css("display", "inline-block");
        	            $("#passwordImgClose").css("display", "none");
        	        }
        		});
        		
        		$("#repeatPwdImgClose").css("display", "none");
        		$("#repeatPwdImg, #repeatPwdImgClose").on("click",function() {
        			if ($("#repeatPwd").attr("type") == "password") {
        	            $("#repeatPwd").attr("type", "text");
        	            $("#repeatPwdImg").css("display", "none");
        	            $("#repeatPwdImgClose").css("display", "inline-block");
        	        }
        	        else {
        	            $("#repeatPwd").attr("type", "password");
        	            $("#repeatPwdImg").css("display", "inline-block");
        	            $("#repeatPwdImgClose").css("display", "none");
        	        }
        		});
        		$("#password, #repeatPwd").on("blur",function() {
	        		var that = this;
    		        function checkPasswordSafe(password) {
				    	// 最低：纯字母、纯数字
				    	// 低：     字母+数字
				    	// 中：     大写字母+小写字母
				    	// 高：     大写字母+小写字母+数字
				    	// 最高：大写字母+小写字母+数字+特殊字符
				    	if(/^[0-9]*$/.test(password) || /^[A-Z]*$/.test(password)|| /^[a-z]*$/.test(password)){
				    		return 1;
				    	}else if (/^[a-z0-9]*$/.test(password)|| /^[A-Z0-9]*$/.test(password)) {
				    		return 2;
				    	}else if (/^[a-zA-Z]*$/.test(password)) {
				    		return 3;
				    	}else if(/^[a-zA-Z0-9]*$/.test(password)){
				    		return 4;
				    	}else if (/[a-z]+/.test(password) && /[A-Z]+/.test(password) && /[0-9]+/.test(password) && /[`~!@#$%^&*()-=_+{}|\\[\];':"<>,.?/]+/.test(password)) {
				    		return 5; 
				    	}
				    }
		        	if(flag == "true"){
		        		return;
		        	}
		        	flag = "true";
		        	if (($("#password").val() != "" && checkPasswordSafe($("#password").val()) != 5 || 
		        		($("#repeatPwd").val() != "" && checkPasswordSafe($("#repeatPwd").val()) != 5))) {
		        		fish.error(i18n.SC_PASSWORD_CHAR_NUMBER_UPPER).result.then(function() {
		        			flag = "false";
		        		});
		        		return;
		        	}
		        	if ($("#password").val() != "" && $("#repeatPwd").val() != "" && $("#password").val() != $("#repeatPwd").val()) {
		        		fish.error(i18n.SC_REPEAT_PASSWORD_NOT_MATCH).result.then(function() {
 		        			flag = "false";
		        		});
		        	}
		        	else {
		        		flag = "false";
		        	}
		        	
        		});
        	}
        },
        changePwd : function(){
        	var that = this;
//        	var res = $("#changePwdForm").isValid();
//        	if(!res){
//        		return;
//        	}
        	if (!that.checkPasswordLengthAndSafe($("#password").val(), $("#repeatPwd").val())) {
				return;
			}
        	if(this.subs!=null){
        		var param ={};
            	//param.CUST_CODE=this.subs.CUST_CODE;
            	//param.CUST_ID=this.subs.CUST_ID;
            	param.PWD = $("#password").val();
            	param.OLD_PWD = $("#oldPassword").val();
            	utils.callService("changePassword.do",param,function(data){
            		if(data.CODE!='0'){
            			fish.error(data.MSG);
            		}else{
            			fish.info('Successed in change password');
//            			that.afterRender();
            			that.$('#password').val("");
            			that.$('#oldPassword').val("");
            			that.$('#repeatPwd').val("");
            		}
            	})
        	}
        	
        },
        checkPassword : function(){
        	var that = this;
        	if($("#password").val() == "" || $("#repeatPwd").val() == null){
        		return;
        	}
        	if (!that.checkPasswordLengthAndSafe($("#password").val(), $("#repeatPwd").val())) {
				return;
			}   	
        },
        checkPasswordSafe : function(password) {
        	// 最低：纯字母、纯数字
        	// 低：     字母+数字
        	// 中：     大写字母+小写字母
        	// 高：     大写字母+小写字母+数字
        	// 最高：大写字母+小写字母+数字+特殊字符
        	if(/^[0-9]*$/.test(password) || /^[A-Z]*$/.test(password)|| /^[a-z]*$/.test(password)){
        		return 1;
        	}else if (/^[a-z0-9]*$/.test(password)|| /^[A-Z0-9]*$/.test(password)) {
        		return 2;
        	}else if (/^[a-zA-Z]*$/.test(password)) {
        		return 3;
        	}else if(/^[a-zA-Z0-9]*$/.test(password)){
        		return 4;
        	}else if (/[a-z]+/.test(password) && /[A-Z]+/.test(password) && /[0-9]+/.test(password) && /[`~!@#$%^&*()-=_+{}|\\[\];':"<>,.?/]+/.test(password)) {
				return 5;
        	}
        },
        checkPasswordLengthAndSafe : function(password, repeatPassword) {
        	var that = this;
        	if (password != repeatPassword) {
        		fish.info(i18n.SC_REPEAT_PASSWORD_NOT_MATCH);
        		return false;
        	}
        	var level = that.checkPasswordSafe(password);
        	if (level != 5) {
        		fish.info(i18n.SC_PASSWORD_CHAR_NUMBER_UPPER);
        		return false;
        	}
        	return true;
        }
    
    });
    return MainContentView;
});
