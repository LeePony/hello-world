requirejs.config({
    // baseUrl: 'modules',
    paths: {
        utils: './modules/common/utils',

        EntryView      : './modules/PCEntryView',
        LoginView      : './modules/login/views/LoginView',
        MainLayout     : './modules/common/templates/PCMainLayout.html',
        ContentView    : './modules/common/views/ContentView',
        ContentLayout  : './modules/common/templates/ContentLayout.html'
    }
});
fish.View.configure({
    manage: true,
    syncRender: true,
    waitSeconds: 0
});

define(['modules/login/views/LoginView'], function(LoginView) {
	
    var AppView = fish.View.extend({
        el: '#pageRoot',

        initialize: function() {
            this.setView(new LoginView());
        },

        afterRender: function () {
            var that = this;
        }
    });
    new AppView().render();
    return AppView;
});
