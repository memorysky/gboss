var level='0';//判断用户是总部、分公司、营业处的人
	(function($){
		var companyDataList = null;
		var lastMonthDay=new Date();//上个月的今天
    	//lastMonthDay.setMonth(lastMonthDay.getMonth()-1);
		lastMonthDay.setDate(1);
    	lastMonthDay=lastMonthDay.format('yyyy-MM-dd');
		var today=new Date().format('yyyy-MM-dd');
		
		  //修改表格的宽度
        var height =$('#main_bd',window.parent.document).height()-288;
		
    	//初始化表格
    	 var defaults = {
    		        title: "托收失败查询",
    		        width:  '155%',
    		        fitColumn:  true,
    		        height: height,
    		        url: '../../pay/findPaytxtDtPage',
    		        usepager: true,
    		        rownumbers:true,
    		        useRp: true,
    		        colModel : [
	                    {display: '客户名称', name : 'customerName', width : 120, sortable : false},
	                    {display: '车牌号码', name : 'plateNo', width : 120, sortable : false},
	                    {display: '车载电话', name : 'callLetter', width : 90, sortable : false},
	                    {display: '开通日期', name : 'serviceDate', width : 90, sortable : false},
	                    {display: '费用总额', name : 'feeSum', width : 90, sortable : false,isAdd:true},
	       		 		{display: '服务费', name : 'amount1', width : 90, sortable : false},
	       		 		{display: '服务截止时间', name : 'sdate1', width : 190, sortable : false},
	       		 		{display: 'SIM卡流量费', name : 'amount2', width : 90, sortable : false},
	       		 		{display: 'SIM卡截止时间', name : 'sdate2', width : 190, sortable : false},
		       		 	{display: '盗抢险费', name : 'amount3', width : 110, sortable : false},
	       		 		{display: '盗抢险截止时间', name : 'sdate3', width : 190, sortable : false},
	       		 		{display: '网查费', name : 'amount4', width : 90, sortable : false},
	       		 		{display: '网查截止时间', name : 'sdate4', width : 190, sortable : false},
	       		 		{
	    					display: '银行客户名称',
	    					name: 'custName',
	    					width: 110,
	    					sortable: false
	    				},{
	    					display: '银行账号',
	    					name: 'accountNo',
	    					width: 150,
	    					sortable: false
	    				},{
	    					display: '托收合同',
	    					name: 'contractNo',
	    					width: 110,
	    					sortable: false
	    				},{
	    					display: '托收合同(文件中)',
	    					name: 'payContractNo',
	    					width: 110,
	    					sortable: false
	    				}, {
	    					display: '托收机构',
	    					name: 'agency',
	    					width: 110,
	    					sortable: false,
	    					formatter: function(value, row) {
	    						if (value == 1) {
	    							value = '银盛';
	    						} else if (value == 2) {
	    							value = '金融中心';
	    						}else if (value == 3) {
	    							value = '银联';
	    						}
	    						return value;
	    					}
	    				},
	    				 {
	    					display: '托收文件名',
	    					name: 'txtName',
	    					width: 100,
	    					sortable: false
	    				}, {
	    					display: '托收标记',
	    					name: 'payTagStr',
	    					width: 110,
	    					sortable: false,
	    					formatter: function(value, row) {
	    						if (row.payTag=='X') {
	    							value = '<font color="#FF7400">'+value+'</font>';
	    						}else if (row.payTag=='00' || row.payTag=='Y') {
	    							value = '<font color="#2DD700">'+value+'</font>';
	    						}else{
	    							value = '<font color="red">'+value+'</font>';
	    						}
	    						return value;
	    					}
	    				},
	       		 	    {display: '操作时间', name : 'stamp', width : 110, sortable : false}
    		        ],
    		        buttons : [
    		        ],
    		        searchitems :[
		                {display:'开始日期',name:'startDate',type:'date',value:lastMonthDay,width:150},
						{display:'结束日期',name:'endDate',type:'date',value:today,width:150},
						{display:'分公司',html:'<input type="text" name="companyName" id="companyName"  list="companyDataList"/><input type="hidden" name="subcoNo" id="subcoNo" />'},
						{display:'客户名称',html:'<input type="text" name="customerName" id="customerName" />'},
						{display:'车牌号码',html:'<input type="text" name="plateNo" id="plateNo" />'},
						{display:'车载电话',html:'<input type="text" name="callLetter" id="callLetter" />'},
						{display:'银行账号',html:'<input type="text" name="accountNo" id="accountNo" />'},
						{display:'托收合同',html:'<input type="text" name="payContractNo" id="payContractNo" />'},
						{display:'托收文件名',html:'<input type="text" name="txtName" id="txtName" />'},
						{display:'托收机构',html:'<select name="agency"><option value="">请选择---</option><option value="1">银盛</option><option value="2">金融中心</option><option value="3">银联</option></select>'},
						{display:'托收结果',html:'<select name="payTag"><option value="">请选择---</option><option value="X">送盘</option><option value="Y">托收成功</option><option value="N">托收失败</option></select>'}
    		        ],
    		        isCount:true,
    		        exporturl:'../../pay/exportPaytxtDt'//导出excel
    		    };
    		    $('#dgd_paytxtdt').sgDatagrid(defaults); 
    		    
    		  //自动查询分公司
    			var stockCompany = function(){
    				var params = {};
    				params.companyname = this.value;
    				var obj = $(this);
    				$.ajax({
    					  type : 'post', 
    					  async: true,   
    					  contentType : 'application/json',     
    					  dataType : 'json',     
    					  url : '../../getBranchs',   
    					  data:JSON.stringify(params),
    					  success : function(data) {
    						  if(data){
    							  var companys = data;
    							  if(companys.length>0){
    								     $("#companyDataList").empty();
    							  		companyDataList = {};
    							   }
    							  $.each( companys, function(key,value){
    								  var op = $("<option></option>");
    								  op.val(obj.val()+" "+value.companyname);
    								  $("#companyDataList").append(op);
    								  
    								  companyDataList[value.companyname]=value.companyno;
    								});
    						  }else{
    							  $(document).sgPup({message:'message_info',text: data});
    						  }
    					  } ,     
    					  error : function(res,error) { 
    					  	     if(res && res.responseText){ $(document).sgPup({message:'message_info',text: res.responseText});}     
    					  }    
    					});
    			};
    			
    			  //判断是分公司还是总部，如果是分公司，隐藏分公司列表
    			$.ajax({
    				  type : 'POST', 
    				  async: false,   
    				  url : '../../getUserCompanyLevel',
    				  dataType : 'json',    
    				  contentType : 'application/json',     
    				  data: JSON.stringify({}),
    				  success : function(data) {
    					  if(data){
    						  if(data.success){
    							  level=data.result;
    							 if(level=='0'){//总部
    								   //填分公司
    									$.ajax({
    										  type : 'post', 
    										  async: true,   
    										  contentType : 'application/json',     
    										  dataType : 'json',     
    										  url : '../../getBranchs',   
    										  data:JSON.stringify({}),
    										  success : function(data) {
    											  if(data){
    												  var companys = data;
    				    							  if(companys.length>0){
    				    								     $("#companyDataList").empty();
    				    							  		companyDataList = {};
    				    							   }
    				    							  $.each( companys, function(key,value){
    				    								  var op = $("<option></option>");
    				    								  op.val(value.companyname);
    				    								  $("#companyDataList").append(op);
    				    								  
    				    								  companyDataList[value.companyname]=value.companyno;
    				    								});
    												  
    												  //$("#companyName").on('keyup',stockCompany);
    												  $("#companyName").on('change',function(){
    													    var strs = this.value.split(" ");
    													    if(companyDataList[strs[strs.length-1]]){
    													    	$(this).val(strs[strs.length-1]);
    															
    															$("#subcoNo").val(companyDataList[strs[strs.length-1]]);
    															
    															if($("#subcoNo").val().length==0){
    																$("#companyName").val("");
    															}
    													    }else{
    													    	$(this).val('');
    													    	$("#subcoNo").val("");
    													   }
    													    
    													});
    												  
    											  }else{
    												  $(document).sgPup({message:'message_info',text: data});
    											  }
    										  } ,     
    										  error : function(res,error) { 
    										  	     if(res && res.responseText){ $(document).sgPup({message:'message_info',text: res.responseText});}     
    										  }    
    										});
    							 }else if(level=='1'){//分公司
    								 //隐藏分公司
    								 $('#dgd_paytxtdt .sDiv form .pGroup:eq(2)').hide();
    							 }
    				     		  
    						  }else{
    							$(document).sgPup({message:'message_info',text: data.msg}); 
    						  }
    					  }
    				  } 
    			});
			 
	})(jQuery)