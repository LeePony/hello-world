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
            var __th = $(e.target);

            if (!__th.val()) {
                this.Primary();
                return;
            }
            if (__th.val().length < 6) {
                this.Weak();
                return;
            }
            var _r = this.checkPassword(__th);
            if (_r < 1) {
                this.Primary();
                return;
            }

            if (_r > 0 && _r < 2) {
                this.Weak();
            } else if (_r >= 2 && _r < 4) {
                this.Medium();
            } else if (_r >= 4) {
                this.Tough();
            }
        },
        Primary:function () {
            this.$('#pwdLevel_1').attr('class', 'pwd-tips-gray');
            this.$('#pwdLevel_2').attr('class', 'pwd-tips-gray');
            this.$('#pwdLevel_3').attr('class', 'pwd-tips-gray');
        },

        Weak:function () {
            this.$('#pwdLevel_1').attr('class', 'pwd-tips-danger');
            this.$('#pwdLevel_2').attr('class', 'pwd-tips-gray');
            this.$('#pwdLevel_3').attr('class', 'pwd-tips-gray');
        },

        Medium:function () {
            this.$('#pwdLevel_1').attr('class', 'pwd-tips-danger');
            this.$('#pwdLevel_2').attr('class', 'pwd-tips-warning');
            this.$('#pwdLevel_3').attr('class', 'pwd-tips-gray');
        },

        Tough:function () {
            this.$('#pwdLevel_1').attr('class', 'pwd-tips-danger');
            this.$('#pwdLevel_2').attr('class', 'pwd-tips-warning');
            this.$('#pwdLevel_3').attr('class', 'pwd-tips-success');
        },

        checkPassword: function (pwdinput) {
            var maths, smalls, bigs, corps, cat, num;
            var str = $(pwdinput).val()
            var len = str.length;

            var cat = /.{16}/g
            if (len == 0) return 1;
            if (len > 16) { $(pwdinput).val(str.match(cat)[0]); }
            cat = /.*[\u4e00-\u9fa5]+.*$/
            if (cat.test(str)) {
                return -1;
            }
            cat = /\d/;
            var maths = cat.test(str);
            cat = /[a-z]/;
            var smalls = cat.test(str);
            cat = /[A-Z]/;
            var bigs = cat.test(str);
            var corps = this.corpses(pwdinput);
            var num = maths + smalls + bigs + corps;

            if (len < 6) { return 1; }

            if (len >= 6 && len <= 8) {
                if (num == 1) return 1;
                if (num == 2 || num == 3) return 2;
                if (num == 4) return 3;
            }

            if (len > 8 && len <= 11) {
                if (num == 1) return 2;
                if (num == 2) return 3;
                if (num == 3) return 4;
                if (num == 4) return 5;
            }

            if (len > 11) {
                if (num == 1) return 3;
                if (num == 2) return 4;
                if (num > 2) return 5;
            }
        },

        corpses:function (pwdinput) {
            var cat = /./g
            var str = $(pwdinput).val();
            var sz = str.match(cat)
            for (var i = 0; i < sz.length; i++) {
                cat = /\d/;
                maths_01 = cat.test(sz[i]);
                cat = /[a-z]/;
                smalls_01 = cat.test(sz[i]);
                cat = /[A-Z]/;
                bigs_01 = cat.test(sz[i]);
                if (!maths_01 && !smalls_01 && !bigs_01) { return true; }
            }
            return false;
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
