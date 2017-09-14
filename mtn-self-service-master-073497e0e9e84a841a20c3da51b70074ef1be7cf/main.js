fish.View.configure({manage: true});

require(['modules/main/views/IndexView'], function(LoginView){
	new LoginView().render();
})