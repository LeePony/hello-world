define([
    'hbs!modules/devicemgmt/templates/AsideTpl.hbs',
    'i18n!i18n/common.i18n'
],function(template, i18n) {
    return fish.View.extend({
        template: template,
        //提供模板数据
        serialize: i18n,
        //视图事件定义
        events:{
        },
        //一些初始化设置 (不能进行dom操作)
        initialize: function() {
            
        },
        //视图渲染完毕处理函数
        afterRender: function() {
            this.initTreeFunc();
        },
        initTreeFunc: function() {
            var fNodes = [
                { name:"Company001", open:true,
                    children: [
                        { name:"Department001", open:true,
                            children: [
                                { name:"Sub-sector001" },
                                { name:"Sub-sector002" }
                            ]},
                        { name:"Department002" },
                        { name:"Department003" }
                    ]
                }
            ];
            function showIconForTree(treeNode) {
                return !treeNode.isParent;
            }
            
            var options = {
                edit: {
                    enable: false
                },
                data: {
                    simpleData: {
                        enable: true
                    }
                },
                view: {
                    showIcon: showIconForTree
                },
                fNodes: fNodes
            };
            this.$tree = this.$("#Tree").tree(options);
            var treeId = this.$tree.tree('getNodes');
            this.$tree.tree('selectNode',treeId[0]);
        }
    });
});