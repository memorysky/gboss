(function($) {
		var LODOP; //声明为全局变量
		
		var customerlist = {};//经办人 key:name,value:id
		var unitlist={};
		var vehiclelist={};
		var payCtNolist = {};//托收合同号 key：合同号,value:customerId
		
		var paymentIds=[]; //addPrint;
		var printType=1;
		
		$('#btn_print_history').on('click',function(){
			var changeId=$('#changeId').val();
			var params={};
			if(changeId==1){
				params.customerName=$('#customerName').val();
				if(params.customerName.length==0){
					$(document).sgPup({
						message: 'message_info',
						text: "请选择输入正确的客户名称！"
					});
					return false;
				}
			}else if(changeId==2){
				params.vehicleNum=$('#vehicleNum').val();
				if(params.vehicleNum.length==0){
					$(document).sgPup({
						message: 'message_info',
						text: "请选择输入正确的车牌号码！"
					});
					return false;
				}
			}else if(changeId==3){
				params.unitNum=$('#unitNum').val();
				if(params.unitNum.length==0){
					$(document).sgPup({
						message: 'message_info',
						text: "请选择输入正确的车载电话！"
					});
					return false;
				}
			}else if(changeId==4){
				params.payCtNo=$('#payCtNo').val();
				if(params.payCtNo.length==0){
					$(document).sgPup({
						message: 'message_info',
						text: "请选择输入正确的合同！"
					});
					return false;
				}
			}
			  var beginDate2 =new Date();
			  params.startDate=beginDate2.format('yyyy-MM-dd');
			  
			  beginDate2.setDate(1);
		 	  params.endDate=beginDate2.format('yyyy-MM-dd');
		 	  
			 var partWinLoad=function(){
				 var obj= $('#dgd_print_history','#win_print_history');
				if(obj.get(0)){
					 var settings = obj.data('sgDatagrid');
					 settings.url='../../pay/findPrintHistorysPage';
					 settings.query=params;
					 obj.data('sgDatagrid', settings);
					 //obj.sgDatagrid('reload',{query:params});
		    	}
			};
			//弹出打印发票历史记录页面
	         var defaults = {
		                title:'发票打印历史信息',
		                id:'win_print_history',
		                url:'print_history.html',
		                success: partWinLoad,
		                width: 888,
		                height: 470,
		                buttons : [
		                           {name: '关闭', type: 'button', onpress : function (){
		                               $(document).sgWindow('close',{id:'win_print_history'});
		                            }
		                           }
		                       ]
		            };
		        $(document).sgWindow(defaults);
			
			
		});
    	var printThis = function() {//打印本页记录
    		paymentIds=[];
    		printType=1;
	    	//全选本页数据，再打印
	    	if(!$('#fpp-result input[name=checkall]').attr("checked")){
	    		$('#fpp-result input[name=checkall]').trigger('click');
	    	} 
	    	var obj = $('#fpp-result');
			var bDiv = $('.bDiv', obj);
			var rowData=null;
			var rowPaymentIds=null;
			var checkAll=$('input[type=checkbox][name=fpp-result_chx]', bDiv)
			
	    	//获得选中行的记录
	    	if (checkAll.length == 0) {
				$(document).sgPup({
					message: 'message_info',
					text: "请选择一个选项！"
				});
			}else if (checkAll.length > 0) {
				checkAll.each(function() {
					//if ($(this).attr("checked")) {
						rowData=$(this).data('data');
						rowPaymentIds=rowData.paymentIds;
						if(rowPaymentIds){
							rowPaymentIds=rowPaymentIds.split(',');
							$.each(rowPaymentIds,function(index,item){
								paymentIds.push(item);
							});
						}
					//}
				});
				var result=toServer();
				if(!result){
					return;
				}
			}
			//清除checkall
			if($('#fpp-result input[name=checkall]').attr("checked")){
	    		$('#fpp-result input[name=checkall]').removeAttr('checked');
	    	} 
			return;
	}
    
    var printChooseThis = function() {//打印本页选中记录
    	paymentIds=[];
    	printType=2;
    	var obj = $('#fpp-result');
		var bDiv = $('.bDiv', obj);
		var rowData=null;
		var rowPaymentIds=null
    	//获得选中行的记录
    	if ($('input[type=checkbox][checked=checked]', bDiv) != null && $('input[type=checkbox][checked=checked]', bDiv).length == 0) {
			$(document).sgPup({
				message: 'message_info',
				text: "请选择一个选项！"
			});
		}else if ($('input[type=checkbox][checked=checked]', bDiv) != null && $('input[type=checkbox][checked=checked]', bDiv).length >0) {
			$('input[type=checkbox][checked=checked]', bDiv).each(function() {
				if ($(this).attr("checked")) {
					rowData=$(this).data('data');
					rowPaymentIds=rowData.paymentIds;
					if(rowPaymentIds){
						rowPaymentIds=rowPaymentIds.split(',');
						$.each(rowPaymentIds,function(index,item){
							paymentIds.push(item);
						});
					}
					
				}
			});
			var result=toServer();
			if(!result){
				return;
			}
		}
		return;
	}
    
    var clearChooseThis = function() {//清除选中记录打印标记
    	paymentIds=[];
    	printType=3;
    	var obj = $('#fpp-result');
		var bDiv = $('.bDiv', obj);
		var rowData=null;
		var rowPaymentIds=null
    	//获得选中行的记录
    	if ($('input[type=checkbox][checked=checked]', bDiv) != null && $('input[type=checkbox][checked=checked]', bDiv).length == 0) {
			$(document).sgPup({
				message: 'message_info',
				text: "请选择一个选项！"
			});
		}else if ($('input[type=checkbox][checked=checked]', bDiv) != null && $('input[type=checkbox][checked=checked]', bDiv).length > 0) {
			$('input[type=checkbox][checked=checked]', bDiv).each(function() {
				if ($(this).attr("checked")) {
					rowData=$(this).data('data');
					rowPaymentIds=rowData.paymentIds;
					if(rowPaymentIds){
						rowPaymentIds=rowPaymentIds.split(',');
						$.each(rowPaymentIds,function(index,item){
							paymentIds.push(item);
						});
					}
				}
			});
			var result=toServer();
			if(!result){
				return;
			}
		}
		$('input[type=checkbox][checked=checked]', bDiv).removeAttr("checked");
		return;
	}
    
    var printAll = function() {//打印本次查询的所有记录
    	paymentIds=[];
    	printType=4;
    	var obj = $('#fpp-result');
		var bDiv = $('.bDiv', obj);
		var rowData=null;
		var rowPaymentIds=null
    	//所有的记录
        if ($('input[type=checkbox]', bDiv).length > 0) {
			$('input[type=checkbox]', bDiv).each(function() {
					rowData=$(this).data('data');
					rowPaymentIds=rowData.paymentIds;
					if(rowPaymentIds){
						rowPaymentIds=rowPaymentIds.split(',');
						$.each(rowPaymentIds,function(index,item){
							paymentIds.push(item);
						});
					}
			});
			var result=toServer();
			if(!result){
				return;
			}
		}else{
			$(document).sgPup({
				message: 'message_info',
				text: "没有找到本次需要打印的数据！"
			});
		}
		return;
	}
    
    var exportExcel = function() {//导出本次查询的所有记录
    	paymentIds=[];
    	printType=5;
    	var obj = $('#fpp-result');
		var bDiv = $('.bDiv', obj);
		var rowData=null;
		var rowPaymentIds=null
    	//所有的记录
        if ($('input[type=checkbox]', bDiv).length > 0) {
			$('input[type=checkbox]', bDiv).each(function() {
					rowData=$(this).data('data');
					rowPaymentIds=rowData.paymentIds;
					if(rowPaymentIds){
						rowPaymentIds=rowPaymentIds.split(',');
						$.each(rowPaymentIds,function(index,item){
							paymentIds.push(item);
						});
					}
			});
			var result=toServer();
			if(!result){
				return;
			}
		}else{
			$(document).sgPup({
				message: 'message_info',
				text: "没有找到本次需要打印的数据！"
			});
		}
		return;
	}
    var toServer=function(){//是否打印，打印就调用打印控件
    	var cmsg='确定打印?';
    	if(printType==3){
    		cmsg='确定清楚打印标记?';
    	}else if(printType==5){
    		cmsg='确定导出Excel?';
    	}
    	$(document).sgConfirm({
			text: cmsg,
			cfn: function(r) {
				if (r) {
					var param={};
					param['printType']=printType;
			    	var printFun=Number($('input[name=printFun]:checked').val());
					
			    	param['printFun']=printFun;
		        	
		        	var istart = $("#inputStart").val();
		        	var istartN = $("#inputStart").attr("name");
		        	var iend = $("#inputEnd").val();
		        	var isendN = $("#inputEnd").attr("name");
		        	if(istart!=null&& istart.trim().length>0&& istart!='null'){
		        		param[istartN] = istart;
		        	}
		        	if(iend!=null&& iend.trim().length>0&& iend!='null'){
		        		param[isendN] = iend;
		        	}
		        	var ckbId=null;
		        	$("input[name='ckb_type']:checked").each(function() {
		        		ckbId=$(this).attr('id');
		        		if(ckbId=='ckb_changeId'){
		        			var itext = $(".inputTextId").val();
		            		var iDate = $(".inputTextId").attr("name");
		            		if(itext!=null&& itext.trim().length>0&& itext!='null'){
		            			param[iDate] = itext;
		                	}
		            		itext = $(".inputText").val();
		            		iDate = $(".inputText").attr("name");
		            		if(itext!=null&& itext.trim().length>0&& itext!='null'){
		            			param[iDate] = itext;
		                	}
		        		}
						if(ckbId=='ckb_isDelivery'){
							var sId = $("#selectId").val();
			        		var slID = $("#selectId").attr("name");
			        		param[slID] = sId; 			
						 }
						if(ckbId=='ckb_printNum'){
							var ickv = $("input[name='printNum']:checked").val();
			        		var isID = $("input[name='printNum']:checked").attr("name");
			        		param[isID] = ickv;
						}
					});
			    
					if(paymentIds){
						param['paymentIds'] = paymentIds.join(',');
					}
		    		var keyValues=[];
		    		 for ( var k in param ){ // 方法
		    			keyValues.push(k+"="+param[k]);
		    		 }
			    	var url='../../pay/addPrint';
			    	//if((printFun==1 && printType!=3) || printType==5){//打印word
				    	
			    	if((printFun==1 && printType!=3) || printType==5){//打印word,导出Excel
			    		window.location.href='../../pay/exportAndPrint?'+keyValues.join('&');
			    	}else if(printType==3){//清除打印标记
			    		 $.ajax({
			                 type: "POST",
			                 async:false,
			                 contentType : "application/json",
			                 url: url,
			                 data: JSON.stringify(param),
			                 //data: param,
			                 dataType : "json",
			                 success: function(data){
			                 	if(data){
			                 	}else{
			                 		
			                 	}
			                     
			                 }, error : function(res,error) { 
			    		  	     if(res && res.responseText){ 
			    		  	    	 $(document).sgPup({message:'message_info',text:"调取数据失败。"});
			    		  	    }     
			    			  }  
			             });
			    	}else{//用网页打印
			    		if(printBefore()){
			    		 $.ajax({
			                 type: "POST",
			                 async:false,
			                 contentType : "application/json",
			                 url: url,
			                 data: JSON.stringify(param),
			                 //data: param,
			                 dataType : "json",
			                 success: function(data){
			                 	if(data){
									print0(data);
			                 	}
			                     
			                 }, error : function(res,error) { 
			    		  	     if(res && res.responseText){ 
			    		  	    	 $(document).sgPup({message:'message_info',text:"调取数据失败。"});
			    		  	    }     
			    			  }  
			             });
			    		}
			    	}
			    	//刷新表格
			    	$('#fpp-result').sgDatagrid('reload', 'sgDatagrid');
			    	return true;
				}else{
					return false;
				}

			}
		});
    }
	
	var defaults = {
			title: "",
			//width: 1113,
			width: '110%',
			height: 335,
			url: '../../pay/findPrintsPage',
			datatype: "json",
			usepager: true,
			rownumbers: true,
			useRp: true,
			colid: 'id', //主键
			colModel: [{
				display: '用户名称',
				name: 'customerName',
				width: '8%',
				sortable: false
			}, {
				display: '投递地址',
				name: 'address',
				width: '30%',
				sortable: false
			}, {
				display: '邮政编码',
				name: 'postCode',
				width: '8%',
				sortable: false
			},{
				display: '收件人',
				name: 'addressee',
				width: '7%',
				sortable: false
			},
			{
				display: '投递方式',
				name: 'isDeliveryVal',
				width: '8%',
				sortable: false,
				formatter: function(value, row) {
					return value;
				}
			},{
				display: '费用(元)',
				name: 'realAmount',
				width: '9%',
				sortable: false
			},{
				display: '扣款时间',
				name: 'payTime',
				width: '13%',
				sortable: false
			},{
				display: '已打印次数',
				name: 'printNum',
				width: '8%',
				sortable: false
			}],
			buttons: [{
				name: '打印本页记录',
				bclass: 'add',
				onpress: printThis
			},{
				name: '打印本页选中记录',
				bclass: 'add',
				onpress: printChooseThis
			},{
				name: '清除选中记录打印标记',
				bclass: 'add',
				onpress: clearChooseThis
			},{
				name: '打印本次查询的所有记录',
				bclass: 'add',
				onpress: printAll
			},{
				name: '导出本次查询到的所有记录',
				bclass: 'add',
				onpress: exportExcel
			}],
			sortname: "id",
			sortorder: "desc"
		};
		$('#fpp-result').sgDatagrid(defaults);
		//开始日期、结束日期赋默认值
	   var begeinDate =new Date();
	   begeinDate.setDate(1);
	   begeinDate=begeinDate.format('yyyy-MM-dd');
	   
	   var now =new Date().format('yyyy-MM-dd');
	   $('#inputStart').val(begeinDate);
	   $('#inputEnd').val(now);
	   
	   
	    $("#changeId").change(function(){
			var cid = $("#changeId").val();
			$(".inputText").val('');
			$(".inputTextId").val('');
			if(cid=='1'){
				$(".inputText").attr("name","customerName");
				$(".inputText").removeAttr("id");
				$(".inputText").attr("id","customerName");
				$(".inputText").removeAttr("list");
				$(".inputText").attr("list","customerlist");
				$(".inputTextId").attr("name","customerId");
				$(".inputTextId").removeAttr("id");
				$(".inputTextId").attr("id","customerId");
				$(".show_list").removeAttr("id");
				$(".show_list").attr("id","customerlist");
				$(".inputText").unbind();
				/*用户名称
				$("#customerName").sgAutoComplete({
					  bId:'customerId',//绑定的id
		        	  bUList:'customerlist',//绑定的ul list
		        	  url:'../../ba/getCustomers',
		              likeAttr:'customer_name',//模糊匹配的字段名
		              backLi:{name:'customer_id',text:'customer_name'},
		              queryBack:function(){
		            	  $("#search").trigger('click');
		              }
				});*/
			}else if(cid=='2'){
				$(".inputText").attr("name","vehicleNum");
				$(".inputText").removeAttr("id");
				$(".inputText").attr("id","vehicleNum");
				$(".inputTextId").attr("name","vehicleId");
				$(".inputTextId").removeAttr("id");
				$(".inputTextId").attr("id","vehicleId");
				$(".show_list").removeAttr("id");
				$(".show_list").attr("id","vehiclelist");
				$(".inputText").unbind();
				/*车牌号
				$("#vehicleNum").sgAutoComplete({
					  bId:'vehicleId',//绑定的id
		        	  bUList:'vehiclelist',//绑定的ul list
		        	  url:'../../ba/getVehicles',
		              likeAttr:'plate_no',//模糊匹配的字段名
		              backLi:{name:'vehicle_id',text:'plate_no'},
		              queryBack:function(){
		            	  $("#search").trigger('click');
		              }
				});*/
			}else if(cid=='3'){
				$(".inputText").attr("name","unitNum");
				$(".inputText").removeAttr("id");
				$(".inputText").attr("id","unitNum");
				$(".inputTextId").attr("name","unitId");
				$(".inputTextId").removeAttr("id");
				$(".inputTextId").attr("id","unitId");
				$(".show_list").removeAttr("id");
				$(".show_list").attr("id","unitlist");
				$(".inputText").unbind();
				 /*车台号码
				$("#unitNum").sgAutoComplete({
					  bId:'unitId',//绑定的id
		        	  bUList:'unitlist',//绑定的ul list
		        	  url:'../../ba/getUnits',
		              likeAttr:'call_letter',//模糊匹配的字段名
		              backLi:{name:'unit_id',text:'call_letter'},
		              queryBack:function(){
		            	  $("#search").trigger('click');
		              }
				});*/
			 
			}else if(cid=='0'){
				$(".inputText").removeAttr("name");
				$(".inputText").removeAttr("id");
				$(".inputTextId").removeAttr("name");
				$(".inputTextId").removeAttr("id");
				$(".show_list").removeAttr("id");
				$(".inputText").unbind();
			}else if(cid=='4'){//合同号
				$(".inputText").attr("name","payCtNo");
				$(".inputText").removeAttr("id");
				$(".inputText").attr("id","payCtNo");
				$(".inputTextId").attr("name","collectionId");
				$(".inputTextId").removeAttr("id");
				$(".inputTextId").attr("id","collectionId");
				$(".show_list").removeAttr("id");
				$(".show_list").attr("id","payCtNolist");
				$(".inputText").unbind();
				/*合同号
				$("#payCtNo").sgAutoComplete({
					  bId:'collectionId',//绑定的id
		        	  bUList:'payCtNolist',//绑定的ul list
		        	  url:'../../pay/findCollectionPage',
		              likeAttr:'payCtNo',//模糊匹配的字段名
		              backLi:{name:'collectionId',text:'payCtNo,acNo,acName'},
		              queryBack:function(){
		            	  $("#search").trigger('click');
		              }
				});*/
			}
	}); 
	    
	    $("#changeId").trigger('change');
	    
	    function MyPreview(printData) {	
			LODOP=getLodop();  
			LODOP.PRINT_INIT("打印控件Lodop_发票打印");
			CreateAllPages(printData);
			LODOP.PREVIEW();
			//LODOP.DO_ACTION("PREVIEW_CLOSE",0);
		};
		function CreateAllPages(printData){	
			//LODOP.ADD_PRINT_URL(0,0, "100%","100%","print.html");
			//LODOP.ADD_PRINT_HTM(0,0,"100%","100%",subWinBody.html());	
			//2014-7-14 前用这个，但是有时比较慢，因此改成NewPage()的方式;
			//LODOP.ADD_PRINT_HTM(0,0,"100%","100%",subWinBody.document.documentElement.innerHTML);	
			/* var newPages=$('#print_all > div',$(subWinBody.document.body));
			newPages.each(function(index,item){
				LODOP.NewPage();
				LODOP.ADD_PRINT_HTM(0,0,"100%","100%",item.outerHTML);
			}); */
			var itemList=null;
			var postCode=null;
			var postCodeAll=null;
			var postCodeLength=null;
			var postCodeLeft=0;
			var itemListTop=0;
	   		$.each(printData,function(index,item){
	   			LODOP.NewPage();
	   			//邮编
	   			postCodeAll=item.postCode;
	   			postCodeLength=postCodeAll.length;
	   			if(postCodeAll){
	   				for(var i=0;i<postCodeLength;i++){
	   					postCode=postCodeAll.charAt(i);
	   					if(postCode){
	   						if(i==0){
	   							postCodeLeft=10;
	   						}
	   						LODOP.ADD_PRINT_HTM("100mm",postCodeLeft+"mm","10mm","10mm","<font style='font-size: 12px;font-weight: bold;'>"+postCode+"</font>");
	   						postCodeLeft=postCodeLeft+13;
	   					}
	   				}
	   			}
	   			LODOP.ADD_PRINT_TEXT("115mm","20mm","130mm","10mm",item.address);
	   			LODOP.ADD_PRINT_TEXT("130mm","20mm","120mm","10mm",item.addressee);
	   			LODOP.ADD_PRINT_TEXT("199mm","35mm","40mm","2.6mm",item.callLetter);
	   			LODOP.ADD_PRINT_TEXT("204mm","35mm","40mm","2.6mm",item.customerName);
	   			LODOP.ADD_PRINT_TEXT("209mm","35mm","40mm","2.6mm",item.bank);
	   			LODOP.ADD_PRINT_TEXT("199mm","172mm","34mm","2.6mm",item.bwNo);
	   			LODOP.ADD_PRINT_TEXT("204mm","172mm","34mm","2.6mm",item.printDate);
	   			LODOP.ADD_PRINT_TEXT("209mm","130mm","50mm","2.6mm",item.acNo);
	   			LODOP.ADD_PRINT_TEXT("246mm","50mm","40mm","2.6mm",item.realAmountRMB);
	   			LODOP.ADD_PRINT_TEXT("246mm","124mm","40mm","2.6mm",item.realAmount);
	   			LODOP.ADD_PRINT_TEXT("253mm","52mm","60mm","2.6mm",item.paySEdate);
	   			LODOP.ADD_PRINT_TEXT("270mm","22mm","60mm","2.6mm",item.bwNo);
	   			LODOP.ADD_PRINT_TEXT("280mm","17mm","20mm","2.6mm",item.printDate);
	   			LODOP.ADD_PRINT_TEXT("280mm","42mm","50mm","2.6mm",item.customerName);
	   			LODOP.ADD_PRINT_TEXT("280mm","97mm","30mm","2.6mm",item.callLetter);
	   			LODOP.ADD_PRINT_TEXT("280mm","130mm","28mm","2.6mm",item.realAmount);
	   			
	   			itemList=item.itemList;
	   			$.each(itemList,function(index0,item0){
	   				if(index0==0){
	   					itemListTop=222;
	   				}
	   				if(index0%2==0){
	   					LODOP.ADD_PRINT_TEXT(itemListTop+"mm","10mm","34mm","2.6mm",item0.itemName);
	   					LODOP.ADD_PRINT_TEXT(itemListTop+"mm","50mm","20mm","2.6mm",item0.itemMoney);
	   					LODOP.ADD_PRINT_TEXT(itemListTop+"mm","76mm","30mm","2.6mm",item0.payDate);
	   				}else{
	   					LODOP.ADD_PRINT_TEXT(itemListTop+"mm","104mm","34mm","2.6mm",item0.itemName);
	   					LODOP.ADD_PRINT_TEXT(itemListTop+"mm","144mm","20mm","2.6mm",item0.itemMoney);
	   					LODOP.ADD_PRINT_TEXT(itemListTop+"mm","170mm","30mm","2.6mm",item0.payDate);
	   					itemListTop=itemListTop+5;
	   				}
	   		   		
	   			});
	   		});
		};	
		
		  //打印之前判断打印控件是否安装
	    var printBefore = function() {
			LODOP=getLodop();
			if(LODOP){
				if(!LODOP.VERSION){
					return false;
				}
			}
			return true;
		}
	    //打印
	    var print0 = function(printData) {
	    	/* var	sName='winPrint';
			var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
			if (isChrome){
			  sName='ifm_print_print';
			}
			window.open('print.html', sName, 'height=' + $(window).outerHeight() + ',width=' + $(window).outerWidth() + ',top=0,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no,alwaysRaised=yes');
			//新增遮罩层
			if (!isChrome){
				var mask = $('<div id="print_mask"></div>');
				mask.addClass('window-mask');
				mask.css('z-index', $('div.window').css('z-index') + 1); //如果有弹出窗口，则将遮罩层置为最上层
				var span = $('<span></span');
				var span = $('<span></span');
				span.css({
					position: 'absolute',
					left: $(window).outerWidth() / 2,
					top: $(window).outerHeight() / 2 - 60,
					color: 'red',
					'font-size': 'x-large',
					'font-weight': 'bold'
				});
				span.text('正在打印中...');
				mask.append(span);
				$(document.body).append(mask);
			} */
			
			MyPreview(printData);
		    return false;
		};
	})(jQuery);	
	
	
	  //打印页面元素设置
	var callback = function(subWinBody, subWin) {
		try{
		var itemList=null;
	   		$.each(printData,function(index,item){
				var print_body = $('<div style="width:99%;margin:0 auto;padding-top:350px;"></div>');
				var postCode_p = $('<div style="margin: 0px 0 0 60px;letter-spacing: 2.4em;">'+item.postCode+'</div>');
				var address_p = $('<div style="margin:40px 0 0 150px;">'+item.address+'</div>');
				var addressee_p = $('<div style=" margin: 50px 0 0 200px;">'+item.addressee+'</div>');
				var print_title = $('<div style="overflow:hidden;margin:0;margin-top: 240px;display: block;overflow:hidden;padding:0;border: 1px solid #fff;"></div>');
				var print_ul0 = $('<ul style="display: block;float:left;margin-left:150px;width:100px;"></ul>');
				var callLetter_p = $('<li style="list-style:none;height: 20px;line-height:20px;display:block;">'+item.callLetter+'</li>');
				var customerName_p = $('<li style="list-style:none;height: 20px;line-height:20px;display:block;">'+item.customerName+'</li>');
				var bank_p = $('<li style="list-style:none;height: 20px;line-height:20px;display:block;">'+item.bank+'</li>'); 
				var print_ul1 = $('<ul style="display: block;float:left;margin-left:100px;"></ul>');
				var bwNo_p = $('<li style="list-style:none;height: 20px;line-height:20px;display:block;">'+item.bwNo+'</li>');
				var printDate_p = $('<li style="list-style:none;height: 20px;line-height:20px;display:block;">'+item.printDate+'</li>');
				var acNo_p = $('<div style="float:left;margin-left: 120px;padding-top: 40px;">'+item.acNo+'</div>');
				var print_content = $('<div style="display: block;width:1000px;height:100px;margin-top:35px; overflow:hidden;"></div>');
				var print_foot = $('<div class="print_foot"></div>');
				var paySEdate_p = $('<div style="margin:10px 0 0 200px;">'+item.paySEdate+'</div>');
				var realAmountRMB_p = $('<span style="margin:0;padding:0;border:0;margin-left:190px;">'+item.realAmountRMB+'</span>');
				var realAmount_p = $('<span style="margin:0;padding:0;border:0;margin-left:200px;">'+item.realAmount+'</span>');
				var print_down = $('<div style="print_down"></div>');
				var print_ul2 = $('<ul style="display:bolock;margin-top:80px;overflow:hidden;height:20px;"></ul>');
				var printDate_r = $('<li style="list-style:none;display:inline-block;float:left;margin-left:30px;">'+item.printDate+'</li>');
				var customerName_r = $('<li style="list-style:none;display:inline-block;float:left;margin-left:100px;">'+item.customerName+'</li>');
				var callLetter_r = $('<li style="list-style:none;display:inline-block;float:left;margin-left:100px;">'+item.callLetter+'</li>');
				var realAmount_r = $('<li style="list-style:none;display:inline-block;float:left;margin-left:80px;">'+item.realAmount+'</li>');
				
				 
				 /* var print_body = $('<div class="print_body"></div>');
					var postCode_p = $('<div class="postCode0">'+item.postCode+'</div>');
					var addressee_p = $('<div class="addressee0">'+item.addressee+'</div>');
					var address_p = $('<div class="address0">'+item.address+'</div>');
					var print_title = $('<div class="print_title clearfix"></div>');
					var print_ul0 = $('<ul></ul>');
					var callLetter_p = $('<li class="callLetter0">'+item.callLetter+'</li>');
					var customerName_p = $('<li class="customerName0">'+item.customerName+'</li>');
					var bank_p = $('<li class="bank0">'+item.bank+'</li>'); 
					var print_ul1 = $('<ul></ul>');
					var bwNo_p = $('<li class="bwNo0">'+item.bwNo+'</li>');
					var printDate_p = $('<li class="printDate0">'+item.printDate+'</li>');
					var acNo_p = $('<div class="acNo0">'+item.acNo+'</div>');
					var print_content = $('<div class="print_content clearfix"></div>');
					var print_foot = $('<div class="print_foot"></div>');
					var paySEdate_p = $('<div class="paySEdate0">'+item.paySEdate+'</div>');
					var realAmountRMB_p = $('<span class="realAmountRMB0">'+item.realAmountRMB+'</span>');
					var realAmount_p = $('<span class="realAmount0">'+item.realAmount+'</span>');
					var print_down = $('<div class="print_down"></div>');
					var print_ul2 = $('<ul class="clearfix"></ul>');
					var printDate_r = $('<li>'+item.printDate+'</li>');
					var customerName_r = $('<li>'+item.customerName+'</li>');
					var callLetter_r = $('<li>'+item.callLetter+'</li>');
					var realAmount_r = $('<li>'+item.realAmount+'</li>');
					 */
				print_body.append(postCode_p);
				print_body.append(address_p);
				print_body.append(addressee_p);
				print_ul0.append(callLetter_p);
				print_ul0.append(customerName_p);
				print_ul0.append(bank_p);
				print_title.append(print_ul0);
				print_title.append(acNo_p);
				print_ul1.append(bwNo_p);
				print_ul1.append(printDate_p);
				print_title.append(print_ul1);
				print_body.append(print_title);
				itemList=item.itemList;
	   			$.each(itemList,function(index0,item0){
	   				var itemUl = $('<ul style="margin:0;display:block;float:left;width:370px;height:20px;overflow:hidden;"></ul>');
	   				var itemLi0 = $('<li style="list-style:none;display:inline-block;float:left;margin-left:40px;">'+ item0.itemName +'</li>');
	   				var itemLi1 = $('<li style="list-style:none;display:inline-block;float:left;margin-left:110px;">'+ item0.itemMoney +'</li>');
	   				var itemLi2 = $('<li style="list-style:none;display:inline-block;float:left;margin-left:40px;">'+ item0.payDate +'</li>');
	   				//alert(item0.itemName);
	   				itemUl.append(itemLi0);
	   				itemUl.append(itemLi1);
	   				itemUl.append(itemLi2);
	   				print_content.append(itemUl);
	   			});
	   		print_body.append(print_content);
				print_foot.append(realAmountRMB_p);
				print_foot.append(realAmount_p);
				print_body.append(print_foot);
				print_body.append(paySEdate_p);
				print_ul2.append(printDate_r);
				print_ul2.append(customerName_r);
				print_ul2.append(callLetter_r);
				print_ul2.append(realAmount_r);
				print_down.append(print_ul2);
				print_body.append(print_down);
	   		
	   		//$('#print_all',subWinBody).append(getOuterHtml(print_body));
				$('#print_all',subWinBody).append(print_body.get(0).outerHTML);
	   			
	   		});
			/* if (navigator.appName == 'Microsoft Internet Explorer') {
				subWin.print();
			} else {
				subWin.focus();
				subWin.print();
			} */
	   		subWin.focus();
	   		MyPreview(subWin);
		}catch(e){
			$(document).sgPup({message:'message_info',text:'请先安装打印控件!'});
			console.dir(e);
		}

	}