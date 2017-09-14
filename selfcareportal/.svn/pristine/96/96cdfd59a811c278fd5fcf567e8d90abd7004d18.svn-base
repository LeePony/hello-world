define(['utils'], function(utils) {

	function getSMSCode  (type,obj,that) {
			
			
			if(!$("#mobile").isValid()){
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
		
//		
		

	}

    return {
		getSMSCode :getSMSCode
	}
});
