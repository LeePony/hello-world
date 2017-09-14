define([
    'hbs!modules/main/templates/IndexTpl.hbs',
    'i18n!i18n/common.i18n',
    'modules/home/views/HomeView'
],function(template, i18n, HomeView) {
    return fish.View.extend({
    	el:'body',
        template: template,
        //提供模板数据
        serialize: i18n,
        //视图事件定义
        events:{
            'click #ChangePassword': 'onChangePassword',
            'click #loginOut': 'loginOut'
        },
        //一些初始化设置 (不能进行dom操作)
        initialize: function() {
        },
        //视图渲染完毕处理函数
        afterRender: function() {
            this.$('#DropUser').dropdown({
                trigger: 'hover'
            })
        	this.$('.menu').hover(function(){
                this.$('.sub-menu-box').show();
            }.bind(this),function(){
                this.$('.sub-menu-box').hide();
            }.bind(this));
            this.changeMenu('7.1');
            this.$el.on('click', '#nav .menu-list', function(e) {
                $(e.target).parent().addClass('cur-menu').siblings().removeClass('cur-menu');
                this.$('.sub-menu-box li').removeClass('cur-menu');
                this.changeMenu($(e.target).parent().attr('data-index'));
                this.$('.sub-menu-box').hide();
            }.bind(this));
            this.$el.on('click', '.sub-menu-box li', function(e) {
                this.$('#nav .menu-list').removeClass('cur-menu');
                this.$('.sub-menu-box li').removeClass('cur-menu');
                var dataIndex = '';
                if ($(e.target).parent().attr('data-index')) {
                    dataIndex = $(e.target).parent().attr('data-index').charAt(0);
                }
                this.$('#nav li[data-index="'+ dataIndex + '"]').addClass('cur-menu');
                $(e.target).parent().addClass('cur-menu').siblings().removeClass('cur-menu');
                this.changeMenu($(e.target).parent().attr('data-index'));
                this.$('.sub-menu-box').hide();
            }.bind(this));
        },
        onChangePassword: function() {
            this.requireView({
                url: 'modules/changepassword/views/ChangePasswordView',
                selector: "#content"
            });
        },
        changeMenu: function(index) {
            switch (index) {
                case '1':
                    this.requireView({
                        url: 'modules/home/views/HomeView',
                        selector: "#content"
                    });
                    break;
                case '2':
                    this.requireView({
                        url: 'modules/usermgmt/views/UserMgmtView',
                        selector: "#content"
                    });
                    break;
                case '3':
                    this.requireView({
                        url: 'modules/devicemgmt/views/DeviceMgmtView',
                        selector: "#content"
                    });
                    break;
                case '4':
                    this.requireView({
                        url: 'modules/billinquiry/views/BillInquiryView',
                        selector: "#content"
                    });
                    break;
                case '4.1':
                    this.requireView({
                        url: 'modules/billinquiry/views/BillInquiryView',
                        selector: "#content"
                    });
                    break;
                case '4.2':
                    this.requireView({
                        url: 'modules/recharge/views/RechargeView',
                        selector: "#content"
                    });
                    break;
                case '4.4':
                    this.requireView({
                        url: 'modules/balanceinquiry/views/BalanceInquiryView',
                        selector: "#content"
                    });
                    break;
                case '5':
                    this.requireView({
                        url: 'modules/batchoperationlog/views/BatchOperationLogView',
                        selector: "#content"
                    });
                    break;
                case '5.1':
                    this.requireView({
                        url: 'modules/batchoperation/views/BatchOperationView',
                        selector: "#content"
                    });
                    break;
                case '5.2':
                    this.requireView({
                        url: 'modules/batchoperationlog/views/BatchOperationLogView',
                        selector: "#content"
                    });
                    break;
                case '6':
                    this.requireView({
                        url: 'modules/apnrequest/views/APNRequestView',
                        selector: "#content"
                    });
                    break;
                case '6.1':
                    this.requireView({
                        url: 'modules/apnrequest/views/APNRequestView',
                        selector: "#content"
                    });
                    break;
                case '6.2':
                    this.requireView({
                        url: 'modules/apnrequesthistory/views/APNRequestHistoryView',
                        selector: "#content"
                    });
                    break;
                case '7':
                    this.requireView({
                        url: 'modules/orderhistory/views/OrderHistoryView',
                        selector: "#content"
                    });
                    break;
                case '7.1':
                    this.requireView({
                        url: 'modules/simstate/views/SIMStateView',
                        selector: "#content"
                    });
                    break;
                case '7.2':
                    this.requireView({
                        url: 'modules/orderhistory/views/OrderHistoryView',
                        selector: "#content"
                    });
                    break;
                case '7.4':
                    this.requireView({
                        url: 'modules/top10devices/views/Top10DevicesView',
                        selector: "#content"
                    });
                    break;
                default:
                    break;
            }
        },
        /*asideToggle:function(e){
            e.stopPropagation();
            $('#contentAside').toggleClass('show-aside');
            $(".bottom-fill").toggleClass('show');
        },*/
        loginOut:function(e){
            this.requireView({
                url: 'modules/login/views/LoginView',
                selector: 'body'
            });
        }
    });
});