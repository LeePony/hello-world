requirejs.config({
     baseUrl: '.',
    paths: {
        hbs  : './frm/requirejs/plugins/hbs',
        text : './frm/requirejs/plugins/text',
        i18n : './frm/requirejs/plugins/i18n',

        utils: './modules/common/utils',

        EntryView      : './modules/PCEntryView',
        BaseView       : './modules/common/views/BaseView',
        LoginView      : './modules/login/views/LoginView',
        MainLayout     : './modules/common/templates/PCMainLayout.html',
        ContentView    : './modules/common/views/ContentView',
        ContentLayout  : './modules/common/templates/ContentLayout.html',
        HeaderView     : './modules/common/views/HeaderView',
        HeaderTemplate : './modules/common/templates/Header',
        FooterTemplate : './modules/common/templates/Footer',

        

        AsideMenuView       : './modules/home/views/AsideMenuView',
        MainContentView     : './modules/home/views/MainContentView',
        LoyaltyPointView    : './modules/home/views/LoyaltyPointView',
        PersonalView   		: './modules/home/views/PersonalView'
    }
});

define(['EntryView'], function(EntryView) {

    var app = new EntryView({
        el: $('#pageRoot'),
    });
    app.render();
    window.app = app;
    return app;
});
