define([
    'hbs!modules/usermgmt/templates/AsideTpl.hbs',
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
            this.$('#Tabs').tabs({
                activate: function(e, ui) {
                    if (ui.newPanel.attr("id") === 'TabsOrg') {
                        this.parentView.requireView({
                            url: 'modules/usermgmt/views/DepartmentView',
                            selector: "#contentMain"
                        });
                    }
                    if (ui.newPanel.attr("id") === 'TabsRole') {
                        this.parentView.requireView({
                            url: 'modules/usermgmt/views/UsersView',
                            selector: "#contentMain"
                        });
                    }
                }.bind(this)
            });
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
            function addHoverDom(treeNode) {
                var treeNodeId = treeNode.tId;
                var sObj = $("#" + treeNodeId + "_span");
                if (treeNode.editNameFlag || $("#addBtn_" + treeNodeId).length > 0) return;
                var addStr = "<span class='add iconfont icon-tianjia' id='addBtn_" + treeNodeId
                    + "' title='add node' onfocus='this.blur();'></span>";
                if (treeNodeId === 'Tree_1') {
                    sObj.after(addStr);
                }
                var btn = $("#addBtn_" + treeNodeId);
                if (btn) btn.bind("click", function () {
                    this.parentView.requireView({
                        url: 'modules/usermgmt/views/AddDepartmentView',
                        selector: "#contentMain"
                    });
                }.bind(this));
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
                    showIcon: showIconForTree,
                    addHoverDom: addHoverDom.bind(this)
                },
                fNodes: fNodes
            };
            this.$tree = this.$("#Tree").tree(options);
            var treeId = this.$tree.tree('getNodes');
            this.$tree.tree('selectNode',treeId[0]);
        }
    });
});