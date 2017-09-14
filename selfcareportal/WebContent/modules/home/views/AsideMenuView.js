// ===========================================================================
//  主区域的视图
//  容器视图，用于管理页面布局
// ===========================================================================
define(function() {

    // 递归创建多级菜单的 DOM 节点
    function createMenu(menuOptions, level) {
        if (!menuOptions.menu) return null;

        level = level || 1;
        var $menu = $('<ul class="page-menu-list"></ul>').attr('data-level', level);
        switch ((level + "").length) {
            case 1: $menu.addClass('root-menu'); break;
            case 3: $menu.addClass('sub-menu'); break;
            case 5: $menu.addClass('sub-menu third-menu'); break;
        }
        var that = this;

        _.each(menuOptions.menu, function(cell, i) {
            var index = level + '.' + (i + 1);
            var $item = $('<li class="page-menu-item"></li>')
                .attr('data-index', index)
                .toggleClass('active', !!cell.active)
                .append(
                    $('<div class="menu-item-cell"></div>')
                        .append(
                            cell.icon ? $('<i></i>').addClass(cell.icon) : null,
                            cell.name ? $('<span></span>').html(cell.name) : null,
                            cell.attr ? $('<i></i>').addClass(cell.attr) : null
                        )
                )
            var event = { type: 'change:index', index: index };
            fish.on(cell.actionCode,function(){
            	$item.find(".menu-item-cell").click();
            })

            // 递归创建 menu list
            $item.append(createMenu(cell, index));
            $menu.append($item);
        });
        return $menu;
    }


    var AsideMenuView = fish.View.extend({
        events: {
            'click .menu-item-cell': 'handleClick'
        },

        initialize: function(options, configs) {
            var that = this;
            this.menuOptions = options;
            this.$menu = $('<div class="page-menu"></div>')
                .append(createMenu(options))
            this.$list = this.$menu.find('[data-index]');
            that.$('.root-menu > .page-menu-item').each(function(){
                if (that.$(this + '> .page-menu-list').text() !== '') {
                    that.$(this + '> .menu-item-cell').addClass('has-child');
                }
            });
        },

        getActiveItem: function() {
            return this.$list.filter(function(i, item) {
                return $(item).hasClass('active');
            });
        },

        getActiveIndex: function() {
            var $item = this.getActiveItem();
            return $item.length ? null : $item.attr('data-index');
        },

        getOptions: function(index) {
            var opts = this.menuOptions;
            _.chain(index.split('.'))
                .rest()
                .map(function(i) { return i - 1; })
                .each(function(key) { opts = opts.menu[key]; })
            return opts;
        },

        select: function(index) {
            var oldIndex = this.getActiveIndex();
            var event = { type: 'change:index', index: index };
            if (oldIndex !== index) {
                event.options = this.getOptions(index);
                this.$list.each(function(i, item) {
                    var $item = $(item);
                    if ($item.attr('data-index') === index) {
                        event.target = item;
                        $item.addClass('child-active');
                        $item.parents('.page-menu-item').addClass('active');
                    } else {
                        $item.removeClass('child-active');
                        if ($item.parents('.page-menu-item').attr('data-index') !== index.substring(0, 3)) {
                            $item.parents('.page-menu-item').removeClass('active');
                        }
                    }
                });
                this.trigger('change:index', event);
                this.trigger('change', event);
            }
        },

        handleClick: function(event) {
            var $item = $(event.currentTarget).closest('.page-menu-item');
            var index = $item.attr('data-index');
            this.select(index);
        },

        render: function() {
            this.$el.html(this.$menu);
            this.$('.root-menu > .page-menu-item:first-child').addClass('child-active');
        }
    });

    return AsideMenuView;
});
