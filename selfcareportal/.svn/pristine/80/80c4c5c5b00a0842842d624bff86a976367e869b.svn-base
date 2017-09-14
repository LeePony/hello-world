define([
    'text!modules/common/templates/Header.html',
    'utils',
    "i18n!modules/i18n/selfcare.i18n"
], function(template,utils,i18n) {
    var HeaderView = fish.View.extend({
        template: fish.compile(template),
        manage: true,
        languageList :[],

       // el: false,
        events: {
            'click #asideToggle': 'asideToggle',
            'click #language-down': 'languageToggle',
            'click #loginOut': 'loginOutFunc',
            'click .language-list > div' : 'changeLanguage',
        },
        initialize :function(){
			var param ={};
			var that = this;
        	utils.callService('queryLanguageList.do',param,function(data){
				that.languageList = data.LANGUAGE_LIST;
				that.initLangData(data.LANGUAGE_LIST);
				var defLang = 'en';
				if(fish.store.get("defaultLanguage")){
					defLang = fish.store.get("defaultLanguage");
				}else{
					if (navigator.userLanguage) {
						defLang = navigator.userLanguage.substring(0,2).toLowerCase();
				    } else {
				    	defLang = navigator.language.substring(0,2).toLowerCase();
				    }
				}
				utils.placeholderSupport();
				if(fish.store.get('language-label')){
					$('#langSpan').html(fish.store.get('language-label'));
				}
 			})
			this.i18nData =i18n;
         },
        render:function(){
            this.$el.html(this.template(i18n));
            return this;
        },
        initLangData : function (data){
			if(data){
				var list = data;
				for(var n=0;n<list.length;n++){
					var div =$('<div>');
					div.attr("lang",list[n].KEY);
					if (list[n].KEY && list[n].KEY=="my") {
						div.attr("label","ျမန္မာ");
						list[n].SHOW_VALUE="ျမန္မာ";
					}
					else {
						div.attr("label",list[n].SHOW_VALUE);
					}
					div.html(list[n].SHOW_VALUE);
					$('#lang_list').append(div);
				}
				
			}
		},
        afterRender:function(){
        	this.$el.html(this.template(this.i18nData));
        	$(document).bind('click',function(){ 
                window.$('#contentAside').removeClass('show-aside');
                window.$(".bottom-fill").removeClass('show');
            });
        },
        changeLanguage : function(event) {
			var lang = $(event.target).attr("lang");
			var label = $(event.target).attr("label");
			fish.store.set("language", lang);
			fish.store.set("defaultLanguage", lang);
			fish.store.set("language-label",label);
			$.cookie("ZSMART_LOCALE",lang);
			window.location.reload();			
		},
        asideToggle:function(e){
        	e.stopPropagation();
            window.$("#contentAside").toggleClass('show-aside');
            window.$(".bottom-fill").toggleClass('show');
            this.$(".menu-info").hide();
        },
        languageToggle:function(){
            this.$(".language-list").toggleClass('show-list');
        },
        loginOutFunc:function(){
        	fish.confirm(i18n.SC_LOGOUT_CONFIRM).result.then(function() {
        		utils.callService('Logout.do',{},function(data){
            		window.location.href = "login.html";
            	})
	        });       	
        }
    });

    return HeaderView;
});
