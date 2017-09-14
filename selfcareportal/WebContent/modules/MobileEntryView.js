define(function(require) {
    var MainLayout        = require('text!MainLayout');
    var HeaderView        = require('HeaderView');
    var ContentView       = require('ContentView');
    var FooterTemplate    = require('hbs!FooterTemplate');

    var EntryView = fish.View.extend({
        template: fish.compile(MainLayout),

        afterRender: function() {
            this.setView('#header', HeaderView);
            this.setView('#content', ContentView);
            this.$('#footer').html(FooterTemplate);
        }
    });

    // 用于绑定和响应全局事件
    var globalEvent = _.extend({}, Backbone.Events);
    window.globalEvent = globalEvent;

    return EntryView;
});
