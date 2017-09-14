// ===========================================================================
//  主区域的视图
//  容器视图，用于管理页面布局
// ===========================================================================
define([
    'text!modules/home/templates/CustomerProfileTmpl.html',
    'text!modules/home/templates/CustomerProfileShowTmpl.html',
    'text!modules/home/templates/EditCustomerProfileTmpl.html',
    'utils',
    'i18n!modules/i18n/selfcare.i18n'
],function(CustomerProfileTmpl,CustomerProfileShowTmpl, EditCustomerProfileTmpl,utils,i18Data) {
	var webroot = null;
	var frontNRC = null;
	var backNRC = null;
	var oldFrontNRC = null;
	var oldBackNRC = null;
	var flag = "false";
    var MainContentView = fish.View.extend({
        template: fish.compile(CustomerProfileTmpl),
        subs : JSON.parse(sessionStorage.SESSION),
        custData : {},
        occupation:{},
        occupationSel:'',
        events:{
        	'click #customerEidt' :'customerEidt',
        	'click #editOk' :'editOk',
        	'click #cancel' :'cancel',
        	'click #up_btn' :'submitCustNRC',
        	'click #imgEidt' : 'imgEidt'
        },
        render:function(){
            this.$el.html(this.template(i18Data));
            return this;
        },
        afterRender:function(){
            var that = this;
            this.front = "false";
            this.back = "false";
            frontNRC = null;
            backNRC = null;
            
            webroot=document.location.href;
            webroot=webroot.substring(webroot.indexOf('//')+2,webroot.length);
            if (webroot && webroot.indexOf("care") >= 0) {
            	webroot="https://care.mpt.com.mm";
            }
            else {
            	webroot=webroot.substring(webroot.indexOf('/')+1,webroot.length);
                webroot=webroot.substring(0,webroot.indexOf('/'));
                webroot = "/"+webroot+"/";
            }
            
            var tm=fish.compile(CustomerProfileShowTmpl);
            this.$('#customerEidtForm').html(tm(i18Data))
            that.initData();
            
            that.$el.on('click','#frontshowDiv,#backshowDiv',function(){
                that.$('.carrousel').show();
                that.$('.carrousel .wrapper img').attr('src',$(this).find('img').attr('src'));
            });
            that.$el.on('click','.close-enlarge',function(){
                that.$('.carrousel').hide();
            });
            that.showNRC();
            
            $('#frontSideFile').fileupload({
                url: webroot + '/FileUploadServlet?action=submitNRC&prefix=frontSide',
                dataType: 'json',
                autoUpload: true,
                previewCanvas: false,
                acceptFileTypes: /(\.|\/)(jpg|jpeg|png)$/i,
                maxFileSize: 3145728,
                disableImageResize: /Android(?!.*Chrome)|Opera/
                    .test(window.navigator.userAgent),
                previewMaxWidth: 190,
                previewMaxHeight: 120,
                previewCrop: true,
                singleFileUploads:false
            }).on('fileupload:add', function (e, data) {
            	if (frontNRC) {
            		var param = {DEL_TEMP_CUST_NRC:frontNRC.name}
                	utils.callService('deltempCustNRC.do',param,function(data){
                		
                	});
            	}
            	
            	frontNRC = null;
                data.context = $('#frontFilelist');
                var li = $('#frontLi');
            	if(li) {
            		li.remove();
            	}
            	
                $.each(data.files, function (index, file) {
                	if(index == 0) {
                		var node = $('<li id="frontLi" class="filelist-item"/>');
                        // node.append(closeButton.clone(true));
                        node.append('<div class="info">' + file.name + '</div>');
                        node.prependTo(data.context);
                	}
                });
            }) .on('fileupload:processalways', function (e, data) {
                var index = data.index,
                    file = data.files[index],
                    node = $(data.context.children()[index]);
                if (file.preview) {
                	//var pnode = $('<p class="enlarge-img"/>');
                	//pnode.prepend($("<img width='100%'/>").attr("src", file.preview.toDataURL()));
                    that.front = 'true';
                    node.prepend($("<img width='100%'/>").attr("src", file.preview.toDataURL()));
                }
                if (file.error) {
                    // node.append($('<span class="error/> ').text(file.error));
                	fish.info(that.getError(file.error), function(){
                		node.remove();
                	});
                }
            }).on('fileupload:done', function (e, data) {
                $.each(data.result, function (index, file) {
                    if (file.filePath) {
                        //var link = $('<a>').attr('target', '_blank').prop('href', file.url);
                        //$(data.context.children()[index]).wrap(link);
                        frontNRC = file;
                    } else if (file.error) {
                        //var error = $('<span class="error"/>').text(file.error);
                        // $(data.context.children()[index]).append(error);
                        fish.info(that.getError(file.error), function(){
                        	$(data.context.children()[index]).remove();
                    	});
                    }
                });
            }).on('fileupload:fail', function (e, data) {
            	if (data && data.files) {
            		 $.each(data.files, function (index) {
                         //var error = $('<span class="error"/>').text('File upload failed.');
                         //$(data.context.children()[index]).append(error);
                     	if(file.error) {
                     		fish.info(that.getError(file.error), function(){
                             	$(data.context.children()[index]).remove();
                         	});
                     	}
                     });
            	}
            	else {
            		fish.error(i18Data.SC_NET_EXCEPTION_MSG);
            	}
            	
            });
            
            $('#backSideFile').fileupload({
                url: webroot + '/FileUploadServlet?action=submitNRC&prefix=backSide',
                dataType: 'json',
                autoUpload: true,
                previewCanvas: false,
                acceptFileTypes: /(\.|\/)(jpg|jpeg|png)$/i,
                maxFileSize: 3145728,
                disableImageResize: /Android(?!.*Chrome)|Opera/
                    .test(window.navigator.userAgent),
                previewMaxWidth: 190,
                previewMaxHeight: 120,
                previewCrop: true,
                singleFileUploads:false
            }).on('fileupload:add', function (e, data) {
            	if (backNRC) {
            		var param = {DEL_TEMP_CUST_NRC:backNRC.name}
                	utils.callService('deltempCustNRC.do',param,function(data){
                		
                	});
            	}
            	backNRC = null;
                data.context = $('#backFilelist');
                var li = $('#backLi');
            	if(li) {
            		li.remove();
            	}
                $.each(data.files, function (index, file) {
                	if(index == 0) {
                		var node = $('<li id="backLi" class="filelist-item"/>');
                        // node.append(closeButton.clone(true));
                        node.append('<div class="info">' + file.name + '</div>');
                        node.prependTo(data.context);
                	}
                });
            }) .on('fileupload:processalways', function (e, data) {
                var index = data.index,
                    file = data.files[index],
                    node = $(data.context.children()[index]);
                if (file.preview) {
                    that.back = 'true';
                    node.prepend($("<img width='100%'/>").attr("src", file.preview.toDataURL()));
                }
                if (file.error) {
                    // node.append($('<span class="error/> ').text(file.error));
                	fish.info(that.getError(file.error), function(){
                		node.remove();
                	});
                }
            }).on('fileupload:done', function (e, data) {
                $.each(data.result, function (index, file) {
                    if (file.filePath) {
                        //var link = $('<a>').attr('target', '_blank').prop('href', file.url);
                        //$(data.context.children()[index]).wrap(link);
                    	backNRC = file;
                    } else if (file.error) {
                        //var error = $('<span class="error"/>').text(file.error);
                        //$(data.context.children()[index]).append(error);
                    	fish.info(that.getError(file.error), function(){
                        	$(data.context.children()[index]).remove();
                    	});
                    }
                });
            }).on('fileupload:fail', function (e, data) {
            	if (data && data.files) {
           		 $.each(data.files, function (index) {
                        //var error = $('<span class="error"/>').text('File upload failed.');
                        //$(data.context.children()[index]).append(error);
                    	if(file.error) {
                    		fish.info(that.getError(file.error), function(){
                            	$(data.context.children()[index]).remove();
                        	});
                    	}
                    });
           	}
           	else {
           		fish.error(i18Data.SC_NET_EXCEPTION_MSG);
           	}
//                $.each(data.files, function (index) {
//                    // var error = $('<span class="error"/>').text('File upload failed.');
//                    // $(data.context.children()[index]).append(error);
//                	fish.info(that.getError(file.error), function() {
//                    	$(data.context.children()[index]).remove();
//                	});
//                });
            });
            
            if (flag == "true") {
            	fish.info(i18Data.REGISTRATION_MSG);
            	flag = "false";
            }
        },
        
        getError: function(errorCode) {
        	if(errorCode == "COMMON.UPLOAD_FILE_CAN_NOT_BE_EMPTY") {
        		return i18Data.SC_CUST_NRC_FILE_EMPTY;
        	}else if(errorCode == "COMMON.UPLOAD_FILE_EXCEED_MAX"){
        		return i18Data.SC_CUST_NRC_EXCEED_MAX;
        	}else if(errorCode == "COMMON.UPLOAD_FAIL"){
        		return i18Data.SC_CUST_NRC_FAIL;
        	}else if(errorCode == "COMMON.IMG_FORMAT_ERROR"){
        		return i18Data.SC_CUST_NRC_IMG_FORMAT_ERROR;
        	}
        	else {
        		return errorCode;
        	}
        },
        
        submitCustNRC:function() {
        	var that = this;
        	if(!that.checkFile()){
        		return false;
        	}
        	
        	var param = {};
        	if (frontNRC && frontNRC.name) {
        		param.FRONT_FILE_NAME=frontNRC.name,
        		param.FRONT_FILE_URL=frontNRC.filePath,
        		param.FRONT_FILE_SIZE=frontNRC.size
        	}
        	if(backNRC && backNRC.name) {
        		param.BACK_FILE_NAME = backNRC.name;
        		param.BACK_FILE_URL = backNRC.filePath;
        		param.BACK_FILE_SIZE = backNRC.size;
        	}
        	if(oldFrontNRC && oldFrontNRC.CUST_IMAGE_ID && this.front === 'true') {
        		var oldNrc = {
        				CUST_IMAGE_ID:oldFrontNRC.CUST_IMAGE_ID,
        				COMMENTS:oldFrontNRC.COMMENTS
        		}
        	    param.OLD_FRONT_NRC=oldNrc;
        	}
        	if(oldBackNRC && oldBackNRC.CUST_IMAGE_ID && this.back === 'true') {
        		var oldNrc = {
        				CUST_IMAGE_ID:oldBackNRC.CUST_IMAGE_ID,
        				COMMENTS:oldBackNRC.COMMENTS
        		}
        		param.OLD_BACK_NRC=oldNrc;
        	}
        	utils.callService('submitCustNRC.do',param,function(data){
        		if(data.CODE != '0'){
        			fish.error(i18Data.SC_CUST_NRC_MSG_ERROR);
        		}else{
//        			that.initData();
//        			// 隐藏上传组件，显示预览组件
//        			that.showNRC();
        			that.validateCustMsg();
        			that.afterRender();
        		}
        	});
        },
        
        checkFile: function() {
        	//判断是否为修改
        	if (oldFrontNRC && this.front != 'true' && this.back != 'true') {
        		fish.info(i18Data.SC_CUST_NRC_SELECT_NRC);
        		return false;
        	}
        		
        	// 正面是必须的
            if (this.front === 'true' &&  !frontNRC) {
                fish.info(i18Data.SC_CUST_NRC_WAIT);
                return false;
            }
            if (this.back === 'true' &&  !backNRC) {
                fish.info(i18Data.SC_CUST_NRC_WAIT);
                return false;
            }
        	if(!frontNRC && !oldFrontNRC) {
        		fish.info(i18Data.SC_CUST_NRC_SELECT_FRONT);
        		return false;
        	}
        	//if(!frontNRC && !backNRC) {
        		//fish.info(i18Data.SC_CUST_NRC_SELECT);
        		//return false;
        	//} else if(!frontNRC) {
        		//fish.info(i18Data.SC_CUST_NRC_SELECT_FRONT);
        		//return false;
        	//} else if(!backNRC) {
        		//fish.info(i18Data.SC_CUST_NRC_SELECT_BACK);
        		//return false;
        	//}
        	return true;
        },
        
        showNRC:function() {
        	var that = this;
        	utils.callService('qryCustImage.do',{},function(data){
        		if(data.IMAGE_LIST && data.IMAGE_LIST.length > 0) {
        			$("#frontshowDiv").show();
        			$("#backshowDiv").show();
        			$("#descDiv").hide();
        			$("#frontDiv").hide();
        			$("#frontFileDiv").hide();
        			$("#backDiv").hide();
        			$("#backFileDiv").hide();
        			$("#up_btn").hide();
        			
        			var front = false;
        			var back = false;
        			for(var i=0; i<data.IMAGE_LIST.length; i++) {
        				if(data.IMAGE_LIST[i].IMAGE_NAME.indexOf("frontSide") > -1){
        					//$("#frontImg").attr("src", "data:image/png;base64,"+data.IMAGE_LIST[0].IMAGE));
            				that.$("#frontImg").prepend($("<img width='100%'/>").attr("src", "data:image/png;base64,"+data.IMAGE_LIST[i].IMAGE));
            				front = true;
            				oldFrontNRC = data.IMAGE_LIST[i];
        				}
        				else if(data.IMAGE_LIST[i].IMAGE_NAME.indexOf("backSide") > -1) {
        					//$("#backImg").attr("src", "data:image/png;base64,"+data.IMAGE_LIST[1].IMAGE));
            				that.$("#backImg").prepend($("<img width='100%'/>").attr("src", "data:image/png;base64,"+data.IMAGE_LIST[i].IMAGE));
            				back = true;
            				oldBackNRC = data.IMAGE_LIST[i];
        				}
        			}
        			
        			if(!front && !back) {
        				// 显示图片，隐藏上传
            			if(data.IMAGE_LIST.length > 0){
            				//$("#frontImg").attr("src", "data:image/png;base64,"+data.IMAGE_LIST[0].IMAGE));
            				that.$("#frontImg").prepend($("<img width='100%'/>").attr("src", "data:image/png;base64,"+data.IMAGE_LIST[0].IMAGE));
            				front = true;
            				oldFrontNRC = data.IMAGE_LIST[i];
            			}
            			if(data.IMAGE_LIST.length > 1){
            				//$("#backImg").attr("src", "data:image/png;base64,"+data.IMAGE_LIST[1].IMAGE));
            				that.$("#backImg").prepend($("<img width='100%'/>").attr("src", "data:image/png;base64,"+data.IMAGE_LIST[1].IMAGE));
            				back = true;
            				oldBackNRC = data.IMAGE_LIST[i];
            			}
        			}
        			
        			if(front) {
        				$("#frontshowDiv").show();
        				$("#imgEidt").show();
        			} else {
        				$("#frontshowDiv").hide();
        			}
        			if(back) {
        				$("#backshowDiv").show();
        			} else {
        				$("#backshowDiv").hide();
        			}
        			
        		} else {
        			// 显示上传，隐藏图片
        			$("#frontshowDiv").hide();
        			$("#backshowDiv").hide();
        			$("#descDiv").show();
        			$("#frontDiv").show();
        			$("#frontFileDiv").show();
        			$("#backDiv").show();
        			$("#backFileDiv").show();
        			$("#up_btn").show();
        			$("#imgEidt").hide();
        		}
        	});
        },
        imgEidt:function(){
        	$("#descDiv").show();
			$("#frontDiv").show();
			$("#frontFileDiv").show();
			$("#backDiv").show();
			$("#backFileDiv").show();
			$("#up_btn").show();
			$("#imgEidt").hide();
			
			$("#frontshowDiv").hide();
			$("#backshowDiv").hide();
        },
        customerEidt :function(){
        	var that = this;
        	var tm=fish.compile(EditCustomerProfileTmpl);
        	 this.$('#customerEidtForm').html(tm(i18Data));
        	 var sel = $('#Occupation').combobox({
                 placeholder: 'Please Select',
                 dataTextField: 'OCCUPATION_NAME',
                 dataValueField: 'OCCUPATION_ID',
                 dataSource: that.occupation
             });
        	 this.occupationSel = sel
             this.renderCustInfo(this.custData,false);             
        },
        editOk:function(){
        	var param = this.custData;
        	if(!$('#customerEidtForm').isValid()){
        		return;
        	}
        	if ($('#custName').val().length >= 60) {
        		fish.info(i18Data.SC_CUST_NAME_TOO_LANG);
        		return;
        	}
        	param.ADDRESS = $('#address').val();
        	param.CUST_NAME = $('#custName').val();
        	$("#customerName").html(param.CUST_NAME);
//        	$("#custNameId").html(param.CUST_NAME);
//        	JSON.parse(sessionStorage.SESSION).CUST_NAME = param.CUST_NAME;
        	
        	param.PHONE_NUMBER = $('#contactNumber').val();
        	param.OCCUPATION_ID = this.occupationSel.combobox('value')
        	param.EMAIL = $('#email').val();
        	param.CERT_NBR= $('#docNumber').val();
        	if (this.CERT && this.CERT.CERT_ID) {
        		param.CERT_ID = this.CERT.CERT_ID;
        		param.CERT_TYPE_ID = this.CERT.CERT_TYPE_ID;
        	}
        	else if (param.CERT_NBR && param.CERT_NBR != ""){
        		param.CERT_TYPE_ID = '1';
        	}
        	var that = this;
        	utils.callService('modCust.do',param,function(data){
        		if(data.CODE!='0'){
					if(data.CODE!='1'){
						fish.error(i18Data.SC_EDIT_CERT_NBR_ERROR);
					}
					else {
						fish.error(i18Data.SC_EDIT_CUST_MSG_ERROR);
					}
        			
        		}else{
//        			var tms = fish.compile(CustomerProfileShowTmpl);
//        			that.$('#customerEidtForm').html(tms(i18Data));
//        			that.initData();
//        			that.showNRC();
        			that.validateCustMsg();
        			that.afterRender();
        		}
        	});
        	
        },
        cancel:function(){
        	var tm=fish.compile(CustomerProfileShowTmpl);
        	this.$('#customerEidtForm').html(tm(i18Data));
        	this.renderCustInfo(this.custData,true);
//        	this.showNRC();
        	this.afterRender();
        },
    	initData:function(){
    		var param = {};
    		var that = this;
    		//param.CUST_ID = this.subs.CUST_ID;
    		param.CERT_FLAG = 'Y';
    		utils.callService('qryCustDetail.do',param,function(data){
    			that.renderCustInfo(data,true);
    			that.custData = data;
    		});
    		utils.callService('qryOccupation.do',{},function(data){
    			that.occupation = data.OCCUPATION_LIST;
    		})
    	},
    	renderCustInfo: function(data,isShow){
    		$('#custName').val(data.CUST_NAME);
    		$('#custType').val(data.CUST_TYPE_NAME);
			$('#createDate').val(data.CREATED_DATE);
			$('#gender').val(data.GENDER=='F'?'Female':'Male');
			$('#email').val(data.EMAIL);
			$('#address').val(data.ADDRESS);
			$('#birthday').val(data.BIRTHDAY_DAY);
			$('#contactNumber').val(data.PHONE_NUMBER);
			if(isShow){
				$('#Occupation').val(data.OCCUPATION_NAME)
			}else{
				if(data.OCCUPATION_ID){
					this.occupationSel.combobox('value', data.OCCUPATION_ID.toString())
				}
				
			}			
			if(data.CERT){
				$('#docType').val(data.CERT.CERT_TYPE_NAME);
				this.CERT = data.CERT;
				var certNbr = data.CERT.CERT_NBR;
				$('#docNumberShow').val(certNbr);
				$('#docNumber').val(certNbr);
//				if (certNbr && certNbr.length > 3) {
////					$('#docNumberShow').val(certNbr.substring(0, certNbr.length-3) + "***");
//					$('#docNumber').val(certNbr);
//				}
			}   						
    	},
    	validateCustMsg:function(){
    		var certNbr = $('#docNumberShow').val();
    		var custName = $('#custName').val();
    		var docNo = $('#docNumber').val();
    		if ((certNbr||docNo) && custName && (frontNRC || oldFrontNRC)) {
    			flag = "true";
    		}
    		else {
    			flag = "false";
    		}
    	}
    });
    
    return MainContentView;
});
