define(['utils'], function(Remote){
	return{
		qrySubsInfo: function(param, success) {
			Remote.callService("QrySubsInfo.do", param, success);
		},
		
		qrySimCardInfo: function(param, success) {
			Remote.callService("QrySimCardDetail.do", param, success);
		},
		 
		simCardLost: function(param, success) {
			Remote.callService("SimCardLost.do", param, success)
		},
		
		simCardRestore: function(param, success) {
			Remote.callService("SimCardRestore.do", param, success)
		}
	};
});