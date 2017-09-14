// ===========================================================================
//  所有视图的基础视图
//  可以实现自动渲染子视图等扩展功能
// ===========================================================================
define(function() {

    // 创建(绑定了执行环境的)函数
    // @param env: 函数的执行环境
    // @param fnBody: 函数体字符串
    function Fn(env, fnBody) {
        return function execute(fnBody) {
            return (new Function('with(this){'+fnBody+'}')).apply(env);
        }
    }

    // js 变量名的正则表达式 (有待优化)
    var varsReg = /[a-zA-Z_$][\w\$]*/g;

    // 向 model 中添加监听 change 事件的方法
    function addModelEvent(model, attrs, updater) {
        _.each(attrs, function(key) {
            if (model.has(key)) {
                model.on('change:' + key, updater);
            }
        });
    }

    var BaseView = Backbone.View.extend({

        // 获取有效数据 (可用于渲染模板)
        serialize: function() {
            var model = this.model instanceof Backbone.Model
                ? this.model.toJSON()
                : this.model;
            return _.extend(_.pick(this, 'i18n'), model);
        },
        // 编译模板
        compileTemplate: function(i18nData) {
            var tpl = this.template;
            if (_.isFunction(tpl)) {
                tpl = tpl($.extend({},this.serialize(),i18nData));
            }
            return String(tpl);
        },

        setView: function(selector, View, options) {
            var view = View instanceof Backbone.View ? View : new View(options);
            view.setElement(this.$(selector));
            view.render(options);
            return this;
        },

        render: function(i18nData) {

            // 编译自身模板
            this.$el.addClass(this.className).attr('id', this.id);
            if (this.template) {
                this.$el.html(this.compileTemplate(i18nData));
            }

            // 编译自定义标签
            window.XTag && XTag.compile(this);

            this.afterRender();

            // 监听 model，自动更新 DOM
            this._autoUpdateElement();
        },

        // 实现修改 model 时自动更新 DOM 元素的值
        // TODO: 支持更多的指令
        _autoUpdateElement: function() {
            var model = this.model;
            function runExp(exp) {
                return Fn(model.toJSON())('return ' + exp);
            }

            if (model instanceof Backbone.Model) {

                // 绑定模型和DOM元素，指令从标签的 data-html 属性中获取
                this.$('[data-html]').each(function(i, element) {
                    var exp = element.getAttribute('data-html');
                    addModelEvent(model, exp.match(varsReg), function() {
                        element.innerHTML = runExp(exp);
                    });
                });

                // 表单元素可以实现双向绑定
                this.$('[data-model]').each(function(i, element) {
                    var $element = $(element);
                    var exp = $element.attr('data-model');
                    var attrs = exp.match(varsReg);
                    if (attrs.length === 1 && model.has(attrs[0])) {
                        $element.on('keyup', function(event) {
                            model.set(attrs[0], $element.val());
                        });
                    }
                    addModelEvent(model, attrs, function() {
                        if (!$element.is(':focus')) {
                            $element.val(runExp(exp));
                        }
                    });
                });

            }
        },

        afterRender: function() {}
    });

    window.BaseView = BaseView;
    return BaseView;
});
