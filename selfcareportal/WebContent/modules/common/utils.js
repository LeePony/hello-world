define(function() {

    // 根据 interfaceMap 配置项创建 Ajax 函数
    function createAjaxObject(interfaceMap) {
        return _.mapObject(interfaceMap, function(key, settings) {
            if (_.isString(settings)) settings = { url: settings };

            return function(param, callback, onerror) {
                return $.ajax(_.extend({
                    data: param,
                    type: 'POST',
                    dataType: 'json',
                }, settings)).done(function(data) {
                    callback && callback(data);
                }).error(function(err) {
                    onerror && onerror(err);
                });
            }
        });
    }
    function callService(url,param,successFunc,errorFunc,completeFunc,beforeFunc){
    	 var paramData = param;
         if (param && !param.__JSON_OBJ_STR_REQUEST_PARAM__) {
             var paramData = {
                 __JSON_OBJ_STR_REQUEST_PARAM__ : JSON.stringify(param)
             };
         }
    	
    	fish.ajax({
	        url:url,
	        method:'POST',//可以是POST或者GET，与jQuery原生的封装一致
	        data:paramData,//需要传递的参数
	        showMask:true,  //是否自动显式全局遮罩
	        showError:getErrorMsg?false:true, //在出现异常时自动显示异常信息，后台需要返回的数据格式：{code:'ERROR-1001',message:'登录失败'}
	        timeout:300000,   //超过3秒显式全局遮罩，此参数在showMask为true时才有效
	        beforeSend:function(){
	            if(beforeFunc){
	            	beforeFunc();
	            }
	        },
	        compelete:function(response,status){//注意这里回调函数的参数
	            if(completeFunc){
	            	completeFunc(response,status)
	            }
	        },
	        success: function(result,status,response) {//注意这里回调函数的参数
	            //如果ajax请求成功，success和compelete都会被调用！
	            if(successFunc){
	            	successFunc(result,status,response)
	            }
	        },
	        //【注意】如果你在这里配置了自己的error处理函数，fish.ajax就不会自动帮你显示异常信息了！
	        error:function(XHR,status,error){//注意这里回调函数的参数
	        	if(XHR.getResponseHeader('sessionstatus') == 'sessionTimeOut'){
	        		window.location.href = "login.html";
	        		return false;
	        	}
	        	if(getErrorMsg){
	        		getErrorMsg(XHR,status,error)
	        	}
	        }
	    });
    }
    function getErrorMsg(xhr,status,error){
    	if (xhr.responseText == null || xhr.responseText === "") {
            tip = "Sorry, this page is currently unavailable. Please try again later.";
        } else {
            var txt = xhr.responseText;
            if ((txt.indexOf("{") === 0) && (txt.lastIndexOf("}") === (txt.length - 1))) {
                var error = eval("(" + txt + ")");
                tip = error.message;
            } 
            else if (txt.indexOf("data-main='login'") != 0) {
            	window.location.href = "login.html";
        		return false;
            }
            else {
                tip = txt;
            }
        }
        fish.error(tip);
    }
    function placeholderSupport (){
		if(!isPlaceholderSupport()){   // 判断浏览器是否支持 placeholder 
		        
		        $('[placeholder]').focus(function() {
		            var input = $(this);
		            if (input.val() == input.attr('placeholder')) {
		                input.val('');
		                input.removeClass('placeholder');
		                if($(this).attr("name")=='password'||$(this).attr("name")=='repeatpassword'){
		                	this.type='password'
		                	
		                }
		            }
		        }).blur(function() {
		            var input = $(this);
		            if (input.val() == '' || input.val() == input.attr('placeholder')) {
		                input.addClass('placeholder');
		                input.val(input.attr('placeholder'));
		                if($(this).attr("name")=='password'|| $(this).attr("name")=='repeatpassword'){
		                	this.type='text'
		                	
		                }
		            }
		        }).blur();
		    };
		    function isPlaceholderSupport() {
		        return 'placeholder' in document.createElement('input');
		    }
		}
    function isPlaceholderSupport() {
        return 'placeholder' in document.createElement('input');
    }
    function clearPlaceholder(){
    	$(".placeholder").each(function(i){
			
			if($(this).val()==$(this).attr("placeholder")){
				$(this).val('')
			}
		})
    }
	function parseFormatNum(number, n) {
		if (n != 0) {
			n = (n > 0 && n <= 20) ? n : 2;
		}
		number = parseFloat((number + "").replace(/[^\d\.-]/g, "")).toFixed(n)
				+ "";
		var sub_val = number.split(".")[0].split("").reverse();
		var sub_xs = number.split(".")[1];

		var show_html = "";
		for (i = 0; i < sub_val.length; i++) {
			show_html += sub_val[i]
					+ ((i + 1) % 3 == 0 && (i + 1) != sub_val.length ? "," : "");
		}

		if (n == 0) {
			return show_html.split("").reverse().join("");
		} else {
			return show_html.split("").reverse().join("") + "." + sub_xs;
		}
	}
    return {
        createAjaxObject: createAjaxObject,
        callService : callService,
        getErrorMsg : getErrorMsg,
        placeholderSupport : placeholderSupport,
        clearPlaceholder:clearPlaceholder,
        parseFormatNum : parseFormatNum
    };
});
