define(['utils'], function(Remote){
	return{
		qryDefBalance: function(param, success) {
			Remote.callService("QryDefaultBal.do", param, success);
		},
		
		vcRecharge: function(param, success) {
			Remote.callService("VCRecharge.do", param, success);
		},
		
		qryPaymentHis: function(param, success) {
			Remote.callService("QryPaymentHis.do", param, success);
		}, 
		
		qryPaymentHisCount: function(param, success) {
			param.zsmart_query_page = {"count":true};
			Remote.callService("QryPaymentHis.do", param, success);
		}
	};
});