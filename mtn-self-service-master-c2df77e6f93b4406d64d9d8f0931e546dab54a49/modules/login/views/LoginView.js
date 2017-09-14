/**
 * Title: loginview.js
 * Description: loginview.js
 * Copyright: Copyright 2016 ZTESOFT, Inc.
 */
define([
    'hbs!modules/login/templates/LoginTpl.hbs',
    'hbs!modules/login/templates/LoginForm',
    'hbs!modules/login/templates/SignupForm',
    'hbs!modules/login/templates/ForgetPasswordForm',
    'hbs!modules/login/templates/ResetPasswordForm',
    'hbs!modules/login/templates/SetPasswordForm'
],function(LoginTpl, LoginForm, SignupForm, ForgetPasswordForm, ResetPasswordForm, SetPasswordForm) {
    return fish.View.extend({
    	el:'body',
        template: LoginTpl,
        //提供模板数据
        serialize: '',
        //视图事件定义
        events:{
            'click .get-Verify-code': 'gitVerifyCode',
            'click #loginStep': 'loginClick',
            'keyup #Password': 'onKeyupPassword'
        },
        //一些初始化设置 (不能进行dom操作)
        initialize: function() {
           
        },
        //视图渲染完毕处理函数
        afterRender: function() {
            this.$('#loginForm').html(LoginForm);
            slide("#slide-login",".slide-list",".slide-btn","cur",1000,3000);

            // 跳转到注册
            this.$el.on('click', '#signUp', function(){
                this.$('#loginForm').html(SignupForm);
            }.bind(this));

            // 跳转到登录
            this.$el.on('click', '#signIn', function(){
                this.$('#loginForm').html(LoginForm);
            }.bind(this));

            // 跳转到忘记密码
            this.$el.on('click', '#forgotPwd', function(){
                this.$('#loginForm').html(ForgetPasswordForm);
                this.$('#UserName').focus();
            }.bind(this));

            // 跳转到设置密码
            this.$el.on('click', '#SetPassword', function(){
                this.$('#loginForm').html(SetPasswordForm);
                this.$('#UserName').focus();
            }.bind(this));
            this.$('#UserName').focus();
        },
        onKeyupPassword: function(e) {
            this.KeyupPassword(e);
        },
        languageToggle:function(){
            this.$(".language-list").toggleClass('show');
        },
        loginClick: function(event) {
            this.requireView({
                url: 'modules/main/views/IndexView',
                selector: "body"
            });
        },
        //点击获取验证码
        gitVerifyCode:function(){
            var $code = this.$('.get-Verify-code');
            if( !$code.hasClass('active')){
                $code.addClass('active');
                var t=60;
                var a=setInterval(times,1000);//1000毫秒
                function times(){
                    t--;
                    //刷新时间显示
                    $code.html(t+'s');
                    if(t <= 0){
                        $code.removeClass('active').html('Get');
                        clearInterval(a);
                        //倒计时结束
                    }
                }
            }
        }
    });
});
