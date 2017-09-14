/**
 * Title: AppView.js
 * Description: AppView.js
 * Author: huang.xinghui
 * Created Date: 14-9-9 上午10:21
 * Copyright: Copyright 2014 ZTESOFT, Inc.
 */
fish.View.configure({
    manage: true,
    syncRender: true
});
define(['modules/PCEntryView'], function (PCEntryView) {
    var AppView = fish.View.extend({
        el: '#pageRoot',

        initialize: function() {
        	var pc= new PCEntryView();
            this.setView(pc);
        },

        afterRender: function () {
            var that = this;
        }
    });
    return AppView;
});
