(function($){
		  var now =new Date();
		  //now.setMonth(now.getMonth()+3);
		 $('#serviceEdate').val(now.format('yyyy-MM-dd'));
		 now.setDate(now.getDate()-1);
		 $('#serviceSdate').val(now.format('yyyy-MM-dd'));
		 /* $('#simEdate').val(now);
		 $('#insuranceEdate').val(now);
		 $('#webgisEdate').val(now); */
		 
		//填充缴费方式
		    $.ajax( {
			  type : 'post', 
			  async: false,   
			  contentType : 'application/json',     
			  dataType : 'json',     
			  url : '../../sys/findSysValue',   
			  data:JSON.stringify({stype:3050}),
			  success : function(data) {
				 if(data){
	 				  var type=$('#payModel');
	 				  if(type){
	 					  $.each(data,function(k,v){
	 						 type.append("<option value='" +v.svalue+"'>" + v.sname+"</option>");
	     				  }); 
	 				  }
	 				 
	 			  }else{
	 				  $(document).sgPup({message:'message_info',text: data});
	 			  }
			  },     
			  error : function(res,error) { 
			  	     if(res && res.responseText){ $(document).sgPup({message:'message_info',text: res.responseText});}     
			  }    
			});
		 
		//填充计费项目类型
		    $.ajax( {
			  type : 'post', 
			  async: false,   
			  contentType : 'application/json',     
			  dataType : 'json',     
			  url : '../../sys/findSysValue',   
			  data:JSON.stringify({stype:3100}),
			  success : function(data) {
				 if(data){
	 				  var type=$('#arrType');
	 				  if(type){
	 					  $.each(data,function(k,v){
	 						 type.append("<option value='" +v.svalue+"'>" + v.sname+"</option>");
	     				  }); 
	 				  }
	 				 
	 			  }else{
	 				  $(document).sgPup({message:'message_info',text: data});
	 			  }
			  },     
			  error : function(res,error) { 
			  	     if(res && res.responseText){ $(document).sgPup({message:'message_info',text: res.responseText});}     
			  }    
			});
		var defaults = {
				title: "",
				//width: 1600,
				width: 1600,
				height: 395,
				url: '../../pay/findArrearageInfosPage',
				datatype: "json",
				usepager: true,
				rownumbers: true,
				useRp: true,
				//colid: 'id', //主键

				colModel: [{
					display: '客户名称',
					name: 'customerName',
					width: 100,
					sortable: false
				},{
					display: '车牌号码',
					name: 'plateNo',
					width: 100,
					sortable: false
				}, {
					display: '车载电话',
					name: 'callLetter',
					width: 100,
					sortable: false
				}, {
					display: '联系电话',
					name: 'phones',
					width: 136,
					sortable: false
				}, {
					display: '销售经理',
					name: 'sales',
					width: 90,
					sortable: false,
				},{
					display: '开通日期',
					name: 'serviceDate',
					width: 120,
					sortable: false,
				},{
					display: '缴费方式',
					name: 'payModel',
					width: 90,
					sortable: false,
					formatter: function(value, row) {
						return value;
					}
				},
				{
					display: '汇总[欠费金额、服务截止日期]',
					name: 'feeItems',
					width: 800,
					sortable: false,
					formatter: function(value, row) {
						value = '<div style="text-align:left;color:red;">'+value+'</div>';
						return value;
					}
				}],
				sortname: "id",
				sortorder: "desc"
			};
			$('#dlg_findAIP').sgDatagrid(defaults);
			
			/*自动搜索 客户名称
			$("#customerName").sgAutoComplete({
				  bId:'customerId',//绑定的id
	        	  bUList:'customerlist',//绑定的ul list
	        	  url:'../../ba/getCustomers',
	              likeAttr:'customer_name',//模糊匹配的字段名
	             // backLi:{name:'customer_id',text:'customer_name,id_no'},//显示在 li中的text可以显示多个
	              backLi:{name:'customer_id',text:'customer_name'},
	              //clearIds:'unit_id,vehicle_id',
	              queryBack:function(){
	            	  $("#query").trigger('click');
	              }
			});*/
			/*自动搜索 车牌号
			$("#vehicleNum").sgAutoComplete({
				  bId:'vehicleId',//绑定的id
	        	  bUList:'vehiclelist',//绑定的ul list
	        	  url:'../../ba/getVehicles',
	              likeAttr:'plate_no',//模糊匹配的字段名
	              backLi:{name:'vehicle_id',text:'plate_no'},
	             // clearIds:'unit_id,customer_id',
	              queryBack:function(){
	            	  $("#query").trigger('click');
	              }
			});*/
			/*自动搜索  车载呼号
			$("#unitNum").sgAutoComplete({
				  bId:'unitId',//绑定的id
	        	  bUList:'unitlist',//绑定的ul list
	        	  url:'../../ba/getUnits',
	              likeAttr:'call_letter',//模糊匹配的字段名
	              backLi:{name:'unit_id',text:'call_letter'},
	              //clearIds:'vehicle_id,customer_id',
	              queryBack:function(){
	            	  $("#query").trigger('click');
	              }
			});*/
	        /* 自动搜索 END*/
	})(jQuery);