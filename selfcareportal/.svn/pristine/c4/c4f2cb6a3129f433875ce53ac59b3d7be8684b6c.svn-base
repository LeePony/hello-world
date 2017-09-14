// ===========================================================================
//  主区域的视图
//  容器视图，用于管理页面布局
// ===========================================================================
define([
    'i18n!modules/i18n/selfcare.i18n',
    'text!modules/home/templates/ContactUsTmpl.html',
    'modules/home/actioins/ContactUsAction'
],function(i18Data, ContactUsTmpl, contactUsAction) {
    var MainContentView = fish.View.extend({
        template: fish.compile(ContactUsTmpl),
        render:function(){
        	 this.$el.html(this.template(i18Data));
             return this;
        },
        afterRender:function(){
            var that = this;
            that.$("#tabs").tabs();
            this.$('#close-ticket').click(function(){
                $(this).parents('li').hide();
            });
            
            that.$('#accNbrInput').val(JSON.parse(sessionStorage.SESSION).ACC_NBR);
            
            that.$('#submitBtn').click(function(e){
            	that.submit();
            });
        },
        
        submit:function(){
        	var that = this;
        	if(!that.$('#emailForm').isValid()) {
        		return;
        	}
        	
        	var param = {};
        	param.TYPE = that.$('#typeTxt').val();
        	param.TITLE = that.$('#titleTxt').val();
        	param.COMMENTS = that.$('#commentsTxt').val();
        	param.CONTACT_NUMBER = that.$('#contNbrTxt').val();
        	
        	contactUsAction.sendMail(param, function(data){
        		fish.success(i18Data.SC_SUCCESS_TO_SEND_EMAIL);
        		that.$('#typeTxt').val('');
        		that.$('#titleTxt').val('');
        		that.$('#commentsTxt').val('');
        		that.$('#contNbrTxt').val('');
        	});
        }
    });
    return MainContentView;
});
