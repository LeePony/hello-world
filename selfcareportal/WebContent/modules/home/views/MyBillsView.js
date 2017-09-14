// ===========================================================================
//  主区域的视图
//  容器视图，用于管理页面布局
// ===========================================================================
define([
    'text!modules/home/templates/MyBillsTmpl.html',
    "i18n!modules/i18n/selfcare.i18n",
    'utils'
],function(MyBillsTmpl,i18n,utils) {
    var MainContentView = fish.View.extend({
        template: fish.compile(MyBillsTmpl),
        acctArray :[],
        acctSel : '',
        events:{
        	//"Button[name='btn']":'btnClick'
        },
        afterRender:function(){
            var that = this;
           
            utils.callService('queryAllAcct.do',{"POSTPAID":"Y"},function(data){
            	if(data&&data.ACCT_LIST){
            		 var sel = $('#selAccount').combobox({
                         placeholder: 'Please Select',
                         dataTextField: 'ACCT_NBR',
                         dataValueField: 'ACCT_NBR',
                         dataSource: data.ACCT_LIST
                     });
                     
                     sel.on('combobox:change', function (e) {
                         that.loadBillData();
                      });
                     that.acctSel = sel;
            	}
            })
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
            			$("#MonthButton").append(but);
            		}
            	}
            	$("Button[name='btn']").each(function(i){
                	$(this).bind('click',function(e){
                		$("Button[name='btn']").filter(".btn-primary").toggleClass("btn-primary");
                    	$(e.target).toggleClass("btn-primary");
                    	that.loadBillData();
                	});
                })
            })
            
           
        },
        render:function(){
            this.$el.html(this.template(i18n));
            return this;
        },
        addMonth:function(date,offset){
        	var month = parseInt(date.getMonth()) + parseInt(offset);
        	date.setMonth(month)
        	var show = date.toISOString().substring(0,7)
        	return show;
        },
        btnClick:function(e){
        	
        },
        loadBillData:function(){
        	var param = {};
        	param.acctCode =this.acctSel.combobox('value');
        	param.offset = $('button[name=btn]').filter(".btn-primary").attr('offset');
        	if(param.acctCode==null ||param.acctCode==''){
        		return;
        	}
        	utils.callService('qryBillInfo.do',param,function(data){
        		if(!data ||data.RESULT==-1){
        			fish.info(i18n.SC_DATA_NOT_FOUND);
        			$('#billDiv').hide();      			
        		}else{
        			$('#billDiv').show();
        			var total = utils.parseFormatNum(data.Total, 1);
        			$('#Total').val(total);
        			var advance = utils.parseFormatNum(data.Advance, 1);
        			$('#Advance').val(advance);
        			var adjustment = utils.parseFormatNum(data.Adjustment, 1);
        			$('#Adjustment').val(adjustment);
        			var opening = utils.parseFormatNum(data.Opening, 1);
        			$('#Opening').val(opening);
        			var closing = utils.parseFormatNum(data.Closing, 1);
        			$('#Closing').val(closing);
        			var sessionData = JSON.parse(sessionStorage.SESSION);
        			$("#custName").val(sessionData.CUST_NAME);
        			$("#acctCode").val(param.acctCode);
        			$("#billMonth").val(data.BillMonth);
        			$("#billNo").val(data.BillNo);
        			$("#Amount").val(closing);
        			var detail = data.Detail;
        			$('#acctItemDiv').empty();
        			if(detail){
        				for(var i in detail){
        					var key ='SC_'+i
        					var div = $("<div class ='col-xs-12 col-sm-12 list-group'>");
        					var label =$("<label class='col-xs-8 col-sm-8 control-label'>").html(i18n[key]);
        					var div1 =$("<div class ='col-xs-4 col-sm-4'>");
        					var input = $("<input type=text class='form-control form-control-static control ' readonly >").val(detail[i]);
        					div1.append(input);
        					div.append(label);
        					div.append(div1);
        					$('#acctItemDiv').append(div);
        				}
        			}
        		}
        	})
        }
    });
    return MainContentView;
});
