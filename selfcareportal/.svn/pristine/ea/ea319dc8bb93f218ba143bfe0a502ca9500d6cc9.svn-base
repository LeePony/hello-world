define(['utils'], function(Remote){
	return{
		qrySubsOffers: function(success) {
			Remote.callService("QrySubsOffer.do", {}, success);
		},
		
		qryDpOrderFee: function(param, success, error) {
			Remote.callService("QryDpOrderFee.do", param, success, error);
		},
		
		addOffer: function(param, success) {
			Remote.callService("AddOffer.do", param, success);
		},
		
		delOffer: function(param, success) {
			Remote.callService("DelOffer.do", param, success);
		}
	};
});