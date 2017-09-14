// ===========================================================================
//  主区域的视图
//  容器视图，用于管理页面布局
// ===========================================================================

define([
    'text!ContentLayout',
    'AsideMenuView',
    'utils',
    'i18n!modules/i18n/selfcare.i18n'
], function(ContentLayout, AsideMenuView,utils,i18nData) {
	
	var path = {};
    var menuOptions = {
       
    };

    var ContentView = fish.View.extend({
        template: fish.compile(ContentLayout),
        // 根据 hash 值获取相应的视图实例
        getContentView: function(url,e) {
        	var that = this;
        	var lang = fish.store.get("language");
        	if(lang == null){
        		if (navigator.userLanguage) {
					lang = navigator.userLanguage.substring(0,2).toLowerCase();
			    } else {
			    	lang = navigator.language.substring(0,2).toLowerCase();
			    }
        	}
        		require([url], function (view,i18nData) {
            		if (view) {
            			var newView =  new view(i18nData)
                        that.setView('#contentMain',newView);
                        that.renderViews('#contentMain');
                        window.$("#contentAside").addClass('close-aside').removeClass('show-aside');
                        window.$(".bottom-fill").removeClass('show');

                    } else {
                        if (window.$(e.target).find(".has-child").hasClass('icon-jianhao')) {
                            window.$(e.target).find(".sub-menu").hide();
                            window.$(e.target).find(".has-child").removeClass('icon-jianhao');
                            window.$(e.target).find(".has-child").addClass('icon-jiahao');
                        } else {
                            window.$(e.target).find(".sub-menu").show();
                            window.$(e.target).find(".has-child").removeClass('icon-jiahao');
                            window.$(e.target).find(".has-child").addClass('icon-jianhao');
                        }
                    }
            	});
        	      	
        },

        createMenu : function(){
        	var self = this;
              // 创建侧边栏视图
             var menu = new AsideMenuView(self.menuOptions);
             this.setView('#contentAside', menu);
             menu.render();
             // TODO: 初始化主内容区域，并且绑定 menu 的切换事件
             menu.on('change', function(e) {
            	 self.getContentView(e.options.url,e);                
             });
             
        },
        afterRender: function() {
        	var self = this;
        	var param ={};
        	utils.callService("QryScMenu.do",param,function(data){
        		if(data.CODE=='0'){
        			self.menuOptions = JSON.parse(data.RESULT);    
            		self.createMenu();
        		}
        		fish.trigger('overView','');
        	})
        }

    });

    return ContentView;
});
