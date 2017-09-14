define([ 'text!modules/login/templates/LoginTemplate.html',
		'text!modules/login/templates/LoginForm.html',
		'text!modules/login/templates/SignupForm.html',
		'text!modules/login/templates/ForgetPasswordForm.html',
		'text!modules/login/templates/ResetPasswordForm.html',
		'text!modules/login/templates/LoginWithSmsCode.html',
		'utils',
		'modules/login/actions/LoginAction',
		"i18n!modules/i18n/selfcare.i18n"
		], function( LoginTemplate,
		LoginForm, SignupForm, ForgetPasswordForm, ResetPasswordForm,LoginWithSmsCode,
		 utils,LoginAction,i18nData) {
	var LoginView = fish.View.extend({
		template : fish.compile(LoginTemplate),
		
		languageList:{},
		events : {
			'click .language-list > div' : 'changeLanguage',
			'click #language-down' :'languageToggle',
			'click *' :'hintLanguage'
				
		},
		initialize :function(){
			var param ={};
			var that = this;
			utils.callService('queryLanguageList.do',param,function(data){
				that.languageList = data.LANGUAGE_LIST;
				that.initLangData(data.LANGUAGE_LIST);
				var defLang = 'my';
				if(fish.store.get("defaultLanguage")){
					defLang = fish.store.get("defaultLanguage");
				}else{
					if (navigator.userLanguage) {
						defLang = navigator.userLanguage.substring(0,2).toLowerCase();
				    } else {
				    	defLang = navigator.language.substring(0,2).toLowerCase();
				    }
				    if (defLang != "my") {
				    defLang = "my";
				    }
				    
				}
				utils.placeholderSupport();
				$("div[lang="+defLang+"]").trigger("click");			
			})
		}
		,
		
		afterRender : function() {
			var that = this;
			
//			that.$('#header').html( that.changeView(HeaderTemplate));
//			that.$('#footer').html(that.changeView(FooterTemplate));
			
			//fish.store.set('language',baseLang)
			that.$('#loginForm').html(fish.compile(LoginForm));
			var view = fish.compile(LoginForm);
			//this.$('#loginForm').html(view(i18nlogin));

			
			// 跳转到注册
			that.$el.on('click', '#signUp', function() {
				that.changeView(fish.compile(SignupForm));								
			});

			// 跳转到登录
			that.$el.on('click', '#signIn', function() {
				
				that.changeView(fish.compile(LoginForm))
			});
			// 跳转到验证码登录
			that.$el.on('click', '#loginSMS', function() {
				that.changeView(fish.compile(LoginWithSmsCode))
			});

			// 跳转到忘记密码
			that.$el.on('click', '#forgotPwd', function() {
				that.changeView(fish.compile(ForgetPasswordForm));
			});
			
			//获取注册验证码
			that.$el.on('click', '#getSignCode', function() {
				that.getSMSCode("REGISTER",$(this),that);
			});
			//忘记密码验证码
			that.$el.on('click','#getFTPwdCode',function(){
				
				that.getSMSCode("FORGET_PWD",$(this),that);
			})
			that.$el.on('click','#getLoginCode',function(){
				that.getSMSCode("LOGIN_WITH_SMS",$(this),that);
			})
			//注册
			that.$el.on('click', '#regist_btn', function() {
				that.register();
			});
			//忘记密码
			that.$el.on('click', '#FTPwdSubmit-btn', function() {
				that.forgetPWD();
			});
			that.$el.on('click', '#loginSms_btn', function() {
				that.loginSMS();
			});
			
			
			//登录
			that.$el.on('click', '#loginStep', function() {
				that.login();
			});
			that.$el.on('keyup', '#password', function() {
				if (event.which == 13) {
					that.login();
				}
			});
			that.$el.on('keyup', '#password1', function() {
            	that.showSecurityLevel();
            });
			
			utils.placeholderSupport();
			//判断是否需要跳转到注册界面
			if (document.location.search.substr(1)) {
				var params = document.location.search.substr(1);
				var aParam = "";
				if (params.indexOf('&') > 0) {
					aParam = document.location.search.substr(1).split('&')[0].split('=') ;
				}
				else {
					aParam = document.location.search.substr(1).split('=') ;
				}
				var sParamName = decodeURIComponent( aParam[0] ) ;
				var sParamValue = decodeURIComponent( aParam[1] ) ;
				if ("sign-up" == sParamName && "1" == sParamValue) {
					$('#signUp').click();
				}
			}
			
		},
		initLangData : function (data){
			if(data){
				var list = data;
				for(var n=0;n<list.length;n++){
					var div =$('<div>');
					div.attr("lang",list[n].KEY);
					if (list[n].KEY && list[n].KEY=="my") {
						div.attr("label","ျမန္မာ");
						list[n].SHOW_VALUE="ျမန္မာ";
					}
					else {
						div.attr("label",list[n].SHOW_VALUE);
					}
					div.html(list[n].SHOW_VALUE);
					$('#lang_list').append(div);
				}
				
			}
			if ($.cookie("selfCare_user_name")&&$.cookie("selfCare_password")) {
				$("#chkRemeberMe").attr("checked", "checked");
		        $("#mobile").val($.cookie("selfCare_user_name"));
		        $("#password").val($.cookie("selfCare_password"));
		        this.IS_COOKIE_PWD = "TRUE";
		    }
			else {
				this.IS_COOKIE_PWD = "FALSE";
			}
		},
		languageToggle : function() {
			this.$(".language-list").toggleClass('show');
		},
		
		hintLanguage : function(event){
//			if(this.$(".language-list").hasClass('show')){
//				this.$(".language-list").toggleClass('show');
//			}
		}
		,
		changeView : function(view){
			var lang = fish.store.get('language');
			if (lang != "en" && lang != "my") {
			lang = "en";
			}
			
			var that = this;
			var moduleURL = "modules/i18n/selfcare.i18n."+lang;
			require([ moduleURL ], function(i18nlogin) {
				this.$('#loginForm').html(view(i18nlogin));
				if($("#langSpan")){
					$("#langSpan").html(fish.store.get("language-label"))
				}
				that.initLangData(that.languageList);
				utils.placeholderSupport();
				
				that.initData();
			});
		},
		changeLanguage : function(event) {
			var lang = $(event.target).attr("lang");
			if (lang != "en" && lang != "zh" && lang != "my") {
			lang = "en";
			}
			var label = $(event.target).attr("label");
			
			fish.setLanguage(lang);			
			fish.store.set("language", lang);
			fish.store.set("defaultLanguage", lang);
			fish.store.set("language-label",label);
			$.cookie("ZSMART_LOCALE",lang);
			this.changeView(fish.compile(LoginForm))			
		},
		
		register : function (){
			var that = this;
			utils.clearPlaceholder();
			var valFlag = $('#signUpForm').isValid();
			if(!valFlag){
				return;
			}
			if (!that.checkPasswordLengthAndSafe($("#password1").val(), $("#repeatPassword").val())) {
				return;
			}
			
			var param = {
					ACC_NBR : $("#mobile").val(),
					VER_CODE : $("#code").val(),
					MSG_TYPE : "REGISTER",
					PWD:$("#password1").val()
				}
			utils.callService("register.do", param, function(obj) {
				fish.info(obj.MSG,function(){
					window.location.href = obj.URL;
				})
				
			}, function(xhr, status, error) {
				utils.getErrorMsg(xhr, status, error);
				
			});
		},
		forgetPWD : function(){
			utils.clearPlaceholder();
			if(!$('form').isValid()){
				return;
			}
			var param = {
					ACC_NBR : $("#mobile").val(),
					VER_CODE : $("#code").val(),
					MSG_TYPE : "FORGET_PWD",
					PWD:$("#password").val()
				}
			utils.callService("resetPwd.do", param, function(obj) {
				fish.info(obj.MSG,function(){
					window.location.href = obj.URL;
				})
				
			}, function(xhr, status, error) {
				utils.getErrorMsg(xhr, status, error);
				
			});
		},
		loginSMS : function(){
			var that = this;
			utils.clearPlaceholder();
			if(!$('form').isValid()){
				return;
			}
			var param = {
					ACC_NBR : $("#mobile").val(),
					VER_CODE : $("#code").val(),
					MSG_TYPE : "LOGIN_WITH_SMS",
					PWD:$("#password").val()
				}
			utils.callService("loginWithSmsCode.do", param, function(data) {
				sessionStorage.SESSION = JSON.stringify(data.SESSION);
				//window.location.href = data.URL;
				// 如果用户没用上传NRC提示上传
				that.showNRCPromt(data.URL);
				
			}, function(xhr, status, error) {
				utils.getErrorMsg(xhr, status, error);
				
			});
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
		getSMSCode :function(type ,obj , that){
			$("#mobile").filter(".placeholder").val('');
			var valFlag = $('#mobile').isValid();
			if(!valFlag){
				return;
			}
			
			obj.attr("disabled", "true");
			var param = {
				ACC_NBR : $("#mobile").val(),
				MSG_TYPE : type
			}
			utils.callService('sendMsgCode.do', param, function(data) {
				that.setIntval(obj,data.effSecond);
			}, function(xhr, status, error) {
				utils.getErrorMsg(xhr, status, error);
				obj.removeAttr("disabled");
			});
		},
		login : function() {
			var that = this;
			$(".placeholder").val('');
			var valFlag = $('#signInForm').isValid();
			if(!valFlag){
				return;
			}
			var param = {
				ACC_NBR : $("#mobile").val(),
				LOGIN_PASSWD : $("#password").val(),
				IS_COOKIE_PWD : this.IS_COOKIE_PWD
			}
			
			utils.callService('Login.do', param, function(data) {
				var lang = fish.store.get('language');
				if (!data.IS_SUCCESS) {
					fish.error(i18nData.SC_LOGIN_FAILED);
				} else {
					if (1 === data.RESTULT_CODE) {
						var moduleURL = "modules/i18n/selfcare.i18n."+lang;
	        			require([ moduleURL ], function(i18nlogin) {
	        			    var info = i18nlogin.SC_PASSWORD_NOT_CORRECT;
	        			    fish.warn(info);
	    	        	});
	        		    return;
					}
					else if (2 === data.RESTULT_CODE) {
						var moduleURL = "modules/i18n/selfcare.i18n."+lang;
	        			require([ moduleURL ], function(i18nlogin) {
	        			    var info = i18nlogin.SC_ACCOUNT_LOCKED_HALFHOUR;
	        			    fish.warn(info);
	    	        	});
	        		    return;
					}
					
					if ($("#chkRemeberMe").is(":checked")) {
	                    // 存储一个带30天期限的 cookie
	                    $.cookie("selfCare_user_name", $("#mobile").val(), {expires: 30});
	                    if (!that.IS_COOKIE_PWD || that.IS_COOKIE_PWD != "TRUE") {
	                    	utils.callService('passwordEncryption.do', param, function(data) {
		                    	$.cookie("selfCare_password", data.PASSWORD, {expires: 30});
		                    	that.IS_COOKIE_PWD = "TRUE";
							});
	                    }
	                    else {
	                    	$.cookie("selfCare_password", $("#password").val(), {expires: 30});
	                    }
	                }
	            	else {
	            		$.cookie("selfCare_user_name", "", {expires: -1});
	            	}
					// 安全性校验
					$.cookie("domain", ".care.mpt.com.mm");
					$.cookie("secure", true);
					$.cookie("HttpOnly", true);
					sessionStorage.SESSION = JSON.stringify(data.SESSION);
					//window.location.href = data.URL;
					// 如果用户没用上传NRC提示上传
					that.showNRCPromt(data.URL);
				}
			}, function(xhr, status, error) {
				utils.getErrorMsg(xhr, status, error);
			});
		},
		
		showNRCPromt:function(url){
			var lang = fish.store.get('language');
			
				utils.callService('CheckCustNRC.do',{},function(data){
					
	        		if(data.UPLOAD_NRC && data.UPLOAD_NRC == "N") {
	        			var moduleURL = "modules/i18n/selfcare.i18n."+lang;
	        			require([ moduleURL ], function(i18nlogin) {
	        			var info = i18nlogin.SC_CUST_NRC_IS_NULL;
	        			fish.info(info, function(){
	        				window.location.href = url;
	        			});

	    	        	});
	        		}
	        		else {
	        			window.location.href = url;
	        		}
			});
			
		},
		
		init : function(){
			var lang = fish.store.get('lang');
			if (lang != "en" && lang != "zh" && lang != "my") {
			lang = "en";
			}
			
			$('#langSpan').html(fish.store.get('lang'))
			var that = this;
			$('.language-list > div').bind('click',function(){
				that.changeLan($(this).attr("lang"),$(this).attr("label"));
				that.languageToggle()
			});
		},
		
		showSecurityLevel : function() {
        	var that = this;
        	var pwd = $("#password1").val();
        	var levelId = that.checkPasswordSafe(pwd);
        	
        	if (levelId == 1) {
        		$('#securityLevel').html(i18nData.SC_SECURITY_LEVEL + ":  " + i18nData.SC_LOWER);
        		$('#securityLevel').removeClass();
        		$('#securityLevel').addClass("text-danger");
        	}
        		
        	else if (levelId == 2) {
        		$('#securityLevel').html(i18nData.SC_SECURITY_LEVEL + ":  " + i18nData.SC_LOW);
        		$('#securityLevel').removeClass();
        		$('#securityLevel').addClass("text-danger");
        	}
        		
        	else if (levelId == 3) {
        		$('#securityLevel').html(i18nData.SC_SECURITY_LEVEL + ":  " + i18nData.SC_MEIDUM);
        		$('#securityLevel').removeClass();
        		$('#securityLevel').addClass("text-warning");
        	}
        		
        	else if (levelId == 4) {
        		$('#securityLevel').html(i18nData.SC_SECURITY_LEVEL + ":  " + i18nData.SC_HIGH);
        		$('#securityLevel').removeClass();
        		$('#securityLevel').addClass("text-success");
        	}
        		
        	else if (levelId == 5) {
        		$('#securityLevel').html(i18nData.SC_SECURITY_LEVEL + ":  " + i18nData.SC_HIGHER);
        		$('#securityLevel').removeClass();
        		$('#securityLevel').addClass("text-success");
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
        	}else if (/^[a-zA-Z0-9`~!@#$%^&*()-=_+{}|\\[\];':"<>,.?/]*$/.test(password)) {
        		return 5;
        	}
        },
        checkPasswordLengthAndSafe : function(password, repeatPassword) {
        	var that = this;
        	if (password != repeatPassword) {
        		fish.info(i18nData.SC_REPEAT_PASSWORD_NOT_MATCH);
        		return false;
        	}
//        	var level = that.checkPasswordSafe(password);
//        	if (level != 5) {
//        		fish.info(i18nData.SC_PASSWORD_CHAR_NUMBER_UPPER);
//        		return false;
//        	}
        	return true;
        },
        initData : function(){

        	$("#passwordImgClose1").css("display", "none");
    		$("#passwordImg1, #passwordImgClose1").on("click",function() {
    			if ($("#password1").attr("type") == "password") {
    	            $("#password1").attr("type", "text");
    	            $("#passwordImg1").css("display", "none");
    	            $("#passwordImgClose1").css("display", "inline-block");
    	        }
    	        else {
    	            $("#password1").attr("type", "password");
    	            $("#passwordImg1").css("display", "inline-block");
    	            $("#passwordImgClose1").css("display", "none");
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
    			if ($("#repeatPassword").attr("type") == "password") {
    	            $("#repeatPassword").attr("type", "text");
    	            $("#repeatPwdImg").css("display", "none");
    	            $("#repeatPwdImgClose").css("display", "inline-block");
    	        }
    	        else {
    	            $("#repeatPassword").attr("type", "password");
    	            $("#repeatPwdImg").css("display", "inline-block");
    	            $("#repeatPwdImgClose").css("display", "none");
    	        }
    		});
        }
	});
	return LoginView;
});
