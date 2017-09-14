// ===========================================================================
//  主区域的视图
//  容器视图，用于管理页面布局
// ===========================================================================
define([
    'text!modules/home/templates/MyCallHisotyTmpl.html',
    "i18n!modules/i18n/selfcare.i18n",
    'utils'
],function(MyCallHisotyTmpl,i18n,utils) {
    var MainContentView = fish.View.extend({
        template: fish.compile(MyCallHisotyTmpl),
        opt:{},
        getParam:function(){
        	var param ={};
        	var that = this;
        	var index = $("#tabs").tabs("option", "active");
        	var offset = $("Button[name='btn']").filter(".btn-primary").attr("offset");
        	if(offset==null){
        		param.START_DATE = $('#startDate').val();
        		param.END_DATE = $("#endDate").val();
        	}else{
        		param.OFFSET = offset;
        	}
        	param.TYPE = index;
        	return param;
        },
        render:function(){
            this.$el.html(this.template(i18n));
            return this;
        },
        afterRender:function(){
            var that = this;
            utils.callService('qrySysDate.do',{},function(data){
            	if(data&&data.MONTH_LIST){
            		for(var i=0;i<data.MONTH_LIST.length;i++){
            			var mon = data.MONTH_LIST[i];
            			
            			var but = $("<button type ='button' name ='btn'>")
            			but.addClass("btn btn-default");
            			if(i==0){
            				but.addClass("btn-primary");
            			}
            			but.attr({"offset":mon.OFFSET});
            			but.html(mon.MONTH)
            			that.$("#otherTime").before(but);
            		}
            	}
            	$("Button[name='btn']").each(function(i){
                	$(this).bind('click',function(e){
                		$("Button[name='btn']").filter(".btn-primary").toggleClass("btn-primary");
                	$('#otherTime').removeClass('btn-primary');
                    $('.custom-time').removeClass('show');
                	$(e.target).toggleClass("btn-primary");
                	fish.trigger("qryCdr")
                	});
                })
                that.initGrid();
                that.loadData();
            })
            that.$("#tabs").tabs({
                paging: true,
                autoResizable:true
            });
            that.$("#tabs").find("li").click(function(){
            	that.loadData();
            })
            this.$('#otherTime').click(function(){
            	
            	that.$('#otherTime').toggleClass('btn-primary');
            	that.$("Button[name='btn']").removeClass('btn-primary');
                that.$('.custom-time').toggleClass('show');
            });
            that.$("#startDate").datetimepicker({
                viewType: "date"
            });
            that.$("#endDate").datetimepicker({
                viewType: "date"
            });
            that.$('button[name="printBtn"]').click(function(){
            	var index = $(this).attr("index");
            	fish.popupView({
                    url:"modules/home/views/PrintView",
                    viewOption:{"gridOption":that.opt["opt"+index],"param":that.getParam()},
                    width:1024
                });
            });
            fish.on("qryCdr",function(){
            	that.loadData();
            });
            
            
            //控制lock按钮功能
            that.$('button[name =download]').click(function(){
            	var index =$(this).attr("index")
            	$("#Cols" + index).val(JSON.stringify(that.opt["opt"+index].colModel));
            	var param = that.getParam();
            	if(param==null||(param.OFFSET==null&&(param.START_DATE==''||param.END_DATE==''))){
            		fish.error(i18n.SC_SELECT_SEARCH_TIME);
            		return;
            	}
            	$('#param'+index).val(JSON.stringify(param));
            	$("#form"+index).submit();
            });

            this.$('#search').click(function(){
            	
            	 $("Button[name='btn']").removeClass("btn-primary")
            	$('#otherTime').addClass("btn-primary btn-default");
            	
            	if(!$('#startDate').isValid()||!$('#endDate').isValid()){
            		return;
            	}
            	
            	var startDate = new Date($('#startDate').val());
        		var bef6 = that.addMonth(new Date(),-6);
        		if(startDate-bef6 < 0){
        			fish.info(i18n.SC_EXCEED_SIX_MONTH);
        			return;
        		}
        		
        		var endDate = new Date($('#endDate').val());
        		if (endDate && endDate - startDate < 0){
        			fish.info(i18n.SC_START_END_DATE_EXCEPTION);
        			return;
        		}
        		
        		that.loadData();
            })
            
        }
   ,
    initGrid:function(){
    	var that  = this;
    	// 表格数据源
        mydata = [
           
            
        ];
        //参数配置
        var opt0 = {
            data: mydata,
            shrinkToFit:true,
            height:500,
            colModel: [{
                name: 'EVENT',
                label: 'Call Type',
                width: 150
            }, {
                name: 'CALLED_NUMBER',
                label: 'Called Number',
                width: 100
            }, {
                name: 'START_TIME',
                label: 'Start Time',
                width: 130
            }, {
                name: 'DURATION',
                label: 'Duration',
                width: 80
            }, {
                name: 'CHARGE1',
                label: 'Fee',
                width: 50
            }
            , {
                name: 'PACKAGE_NAME1',
                label: 'Package',
                width: 50
            }
            
            , {
                name: 'LOCATION',
                label: 'Location',
                width: 100
            }]
        };
        var opt1= {
                data: mydata,
                shrinkToFit:true,
                height:500,
                colModel: [{
                    name: 'EVENT',
                    label: 'Call Type',
                    width: 150
                }, {
                    name: 'APN',
                    label: 'Called Number',
                    width: 100
                }, {
                    name: 'START_TIME',
                    label: 'Start Time',
                    width: 130
                }, {
                    name: 'DURATION',
                    label: 'Duration',
                    width: 80
                }, 
                 {
                    name: 'PACKAGE_NAME1',
                    label: 'Package',
                    width: 50
                }
                , 
                {
                    name: 'CHARGE1',
                    label: 'Fee',
                    width: 50
                }]
            };
        var opt3 = {
                data: mydata,
                shrinkToFit:true,
                height: 500,
                colModel: [{
                    name: 'PACKAGE_NAME1',
                    label: 'Offer Name',
                    width: 150
                }, {
                    name: 'START_TIME',
                    label: 'Start Time',
                    width: 130
                }, {
                    name: 'CHARGE1',
                    label: 'Fee',
                    width: 80
                }, {
                    name: 'PACKAGE_NAME1',
                    label: 'Package',
                    width: 50
                }
                
                ]
            };
        var opt2 = {
                data: mydata,
                shrinkToFit:true,
                height:500,
                colModel: [{
                    name: 'EVENT',
                    label: 'Call Type',
                    width: 150
                }, {
                    name: 'CALLED_NUMBER',
                    label: 'Called Number',
                    width: 100
                }, {
                    name: 'START_TIME',
                    label: 'Start Time',
                    width: 130
                }, {
                    name: 'CHARGE1',
                    label: 'Fee',
                    width: 80
                }, {
                    name: 'PACKAGE_NAME1',
                    label: 'Package',
                    width: 50
                }
                
                ]
            };
        
        //语音
        that.$grid = that.$("#grid0").grid(opt0);
      //  that.opt0 = opt0;
      //语音
        that.$grid = that.$("#grid1").grid(opt1);

      //语音
        that.$grid = that.$("#grid2").grid(opt2);

      //语音
        that.$grid = that.$("#grid3").grid(opt3);
        that.opt.opt0 = opt0;
        that.opt.opt1 = opt1;
        that.opt.opt2 = opt2;
        that.opt.opt3 = opt3;

    },
    addMonth:function(Date,offset){
    	Date.setMonth(parseInt(Date.getMonth()) + parseInt(offset));
    	return Date;
    }
    ,
    
    btnClick:function(e){
    	
    },
    loadData:function(obj){
    	var that =this;

    	var param =that.getParam();
    	var index = $("#tabs").tabs("option", "active");
    	utils.callService('qryCdr.do',param,function(data){
    		if(data){
    			if(data.RESULT==1){
    				fish.popupView({
                        url:"modules/home/views/validateMsg4CallView",
                        viewOption:{"afterParam":param},
                        width:300,
                        height:300
                    });
    			}else{
    				if(data.RESULT){
    					that.$("#grid" + index).grid("clearGridData");
    					
    					that.$("#grid"+index).grid("addRowData",data.DATA.CDR_LIST);
    				}
    			}
    		}
    	})
    },
   
    getReloadData:function(list){
    	return {'rows' : list}
    }
    });
    return MainContentView;
});
