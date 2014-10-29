(function($){
	var paymentIds=[];
	 var beginDate2 =new Date();
	 $('#inputDate').val(beginDate2.format('yyyy-MM-dd'));
	var clearThis = function(){//清除本页记录打印标记
		
		paymentIds=[]
		
   	//全选本页数据
		if(!$('#fepp-result input[name=checkall]').attr("checked")){
   		$('#fepp-result input[name=checkall]').trigger('click');
   	} 
   	
   	var obj = $('#fepp-result');
		var bDiv = $('.bDiv', obj);
		var rowData=null;
		var rowPaymentIds=null;
		var checkAll=$('input[type=checkbox][name=fepp-result_chx]', bDiv);
		
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
			var result=toServer1();
			if(!result){
				return;
			}
		}
		//清除checkall
		if($('#fepp-result input[name=checkall]').attr("checked")){
   		$('#fepp-result input[name=checkall]').removeAttr('checked');
   	} 
		
	}
	
	var clearChooseThis = function(){//清除本页选中记录的打印标记
		paymentIds=[];
   	var obj = $('#fepp-result');
		var bDiv = $('.bDiv', obj);
		var rowData=null;
		var rowPaymentIds=null;
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
			var result=toServer1();
			if(!result){
				return;
			}
		}
		$('input[type=checkbox][checked=checked]', bDiv).removeAttr("checked");
	}
	
	var clearAll = function(){//清除本次查询到所有记录的打印标记
		
		paymentIds=[];
   	var obj = $('#fepp-result');
		var bDiv = $('.bDiv', obj);
		var rowData=null;
		var rowPaymentIds=null;
   	//所有的记录
       if ($('input[type=checkbox]', bDiv).length > 0) {
			/* $('input[type=checkbox]', bDiv).each(function() {
					rowData=$(this).data('data');
					rowPaymentIds=rowData.paymentIds;
					if(rowPaymentIds){
						rowPaymentIds=rowPaymentIds.split(',');
						$.each(rowPaymentIds,function(index,item){
							paymentIds.push(item);
						});
					}
			}); */
			var result=toServer1();
			if(!result){
				return;
			}
		}
		
	}
	
	var toServer1 = function(){
		$(document).sgConfirm({
			text: '确定清楚打印标记?',
			cfn: function(r) {
				if (r) {
		    	     var url='../../pay/updatePrintNums';
		    		 $.ajax({
		                 type: "POST",
		                 async:false,
		                // contentType : "application/json",
		                 url: url,
		                 //data: JSON.stringify(param),
		                 data: {paymentIds:paymentIds},
		                 dataType : "json",
		                 success: function(data){
		
		                 }, error : function(res,error) { 
		    		  	     if(res && res.responseText){ 
		    		  	    	 $(document).sgPup({message:'message_info',text:"清除失败！！"});
		    		  	    }     
		    			  }  
		             });
			    	//刷新表格
			    	$('#fepp-result').sgDatagrid('reload', 'sgDatagrid');
			    	return true;
				}else{
					return false;
				}

			}
		});
	}
	
	var defaults = {
			title: "",
			//width: 1000,
			width: '100%',
			height: 395,
			url: '../../pay/findErrorPrintsPage',
			datatype: "json",
			usepager: true,
			rownumbers: true,
			useRp: true,
			colid: 'id', //主键
			colModel: [{
				display: '打印时间',
				name: 'printTime',
				width: '15%',
				sortable: false
			},{
				display: '用户名称',
				name: 'customerName',
				width: '10%',
				sortable: false
			}, {
				display: '投递地址',
				name: 'address',
				width: '25%',
				sortable: false
			}, {
				display: '邮政编码',
				name: 'postCode',
				width: '10%',
				sortable: false
			},{
				display: '收件人',
				name: 'acName',
				width: '10%',
				sortable: false
			},{
				display: '费用(元)',
				name: 'realAmount',
				width: '10%',
				sortable: false
			},{
				display: '已打印次数',
				name: 'printNum',
				width: '10%',
				sortable: false
			}],
			buttons: [{
				name: '清除本页记录打印标记',
				bclass: 'add',
				onpress: clearThis
			},{
				name: '清除本页选中记录的打印标记',
				bclass: 'add',
				onpress: clearChooseThis
			},{
				name: '清除本次查询到所有记录的打印标记',
				bclass: 'add',
				onpress: clearAll
			}],
			sortname: "id",
			sortorder: "desc"
		};
		$('#fepp-result').sgDatagrid(defaults);
	
})(jQuery)