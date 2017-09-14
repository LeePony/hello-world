requirejs.config({
    // baseUrl: 'modules',
    paths: {
        hbs  : './frm/requirejs/plugins/hbs',
        text : './frm/requirejs/plugins/text',
        i18n : './frm/requirejs/plugins/i18n',

        utils   : './modules/common/utils',

        EntryView      : './modules/MobileEntryView',
        BaseView       : './modules/common/views/BaseView',
        MainLayout     : './modules/common/templates/MobileMainLayout.html',
        ContentView    : './modules/common/views/ContentView',
        ContentLayout  : './modules/common/templates/ContentLayout.html',
        HeaderView     : './modules/common/views/HeaderView',
        HeaderTemplate : './modules/common/templates/Header',
        FooterTemplate : './modules/common/templates/Footer',
    }
});

// 启用 rem 布局的功能
window.enableFlexible && enableFlexible();


define(['EntryView'], function(EntryView) {

    var app = new EntryView({
        el: $('#pageRoot'),
    });

    app.render();

    window.app = app;
    return app;
});
