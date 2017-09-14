define(function(require) {
    var LoginView         = require('modules/login/views/LoginView');
    var MainLayout        = require('text!MainLayout');
    var HeaderView        = require('modules/common/views/HeaderView');
    var ContentView       = require('modules/common/views/ContentView');
    var FooterTemplate    = require('text!modules/common/templates/Footer.html');
    var PersonalView  = require('modules/home/views/PersonalView');
//    var LoyaltyPointView = require('modules/home/views/LoyaltyPointView');

    var EntryView = fish.View.extend({
        template: fish.compile(MainLayout),
        manage: true,
        events: {
            'click #viewDetail': 'viewDetailFunc'
        },
        initialize:function(){
        	var lang = fish.store.get('language');
			if (lang != "en" && lang != "zh") {
			lang = "en";
			}
        	fish.setLanguage(lang);
        }
        ,
        afterRender: function() {
        	
//        	alert(fish.language);
        	var lang = fish.store.get('language');
			if (lang != "en" && lang != "zh") {
			lang = "en";
			}
        	fish.setLanguage(lang);

        	var header = new HeaderView();
        	var content = new ContentView();
        	var personal = new PersonalView();
            this.setView('#header', header);
            this.setView('#content',content);
            this.setView('#personal', personal);
           // this.$('#footer').html(FooterTemplate);
            header.render();
            content.render();
            personal.render();
        },
        viewDetailFunc:function(){
            //alert("ddd");
        }
    });

    // 用于绑定和响应全局事件
    var globalEvent = _.extend({}, Backbone.Events);
    window.globalEvent = globalEvent;

    return EntryView;
});
