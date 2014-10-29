(function($){
		//开始日期、结束日期赋默认值
		   var begeinDate =new Date();
		   begeinDate.setDate(begeinDate.getDate()-7);
		   begeinDate=begeinDate.format('yyyy-MM-dd');
		   
		   var now =new Date().format('yyyy-MM-dd');
		   $('#startDate').val(begeinDate);
		   $('#endDate').val(now);
		   
		var defaults = {
				title: "",
				width: 2340,
				height: 395,
				url: '../../pay/findPaymentPage',
				datatype: "json",
				usepager: true,
				rownumbers: true,
				useRp: true,
				isCount:true,
				//colid: 'id', //主键
				colModel: [{
					display: '客户名称',
					name: 'customerName',
					width: 102,
					sortable: false
				}, {
					display: '车牌号码',
					name: 'plateNo',
					width: 102,
					sortable: false
				},{
					display: '车载电话',
					name: 'callLetter',
					width: 102,
					sortable: false
				},{
					display: '开通日期',
					name: 'serviceDate',
					width: 100,
					sortable: false
				},{
					display: '银行名称',
					name: 'bank',
					width: 100,
					sortable: false
				},{
					display: '银行账号',
					name: 'acNo',
					width: 120,
					sortable: false
				},{
					display: '托收合同号',
					name: 'payCtNo',
					width: 80,
					sortable: false,
				},{
					display: '收据单号',
					name: 'bwNo',
					width: 102,
					sortable: false,
				},{
					display: '应收费总额(元)',
					name: 'acAmount',
					width: 102,
					sortable: false,
					isAdd:true
				}, {
					display: '实收费用总额(元)',
					name: 'realAmount',
					width: 102,
					sortable: false,
					isAdd:true
				},{
					display: '应缴费明细(元)',
					name: 'feetypes1',
					width: 252,
					sortable: false,
					formatter: function(value, row) {
						if(value){
							return '<div style="text-align:left;">'+value+'</div>';
						}else{
							return '';
						}
					}
				},{
					display: '实际缴费明细(元)',
					name: 'feetypes2',
					width: 362,
					sortable: false,
					formatter: function(value, row) {
						if(value){
							return '<div style="text-align:left;">'+value+'</div>';
						}else{
							return '';
						}
					}
				}, {
					display: '付费方式',
					name: 'payModel',
					width: 102,
					sortable: false,
					formatter: function(value, row) {
						return value;
					}
				}, {
					display: '缴费时间',
					name: 'payTime',
					width: 112,
					sortable: false,
				},{
					display: '操作时间',
					name: 'stamp',
					width: 112,
					sortable: false,
				},{
					display: '发票打印次数',
					name: 'printNum',
					width: 102,
					sortable: false,
				},{
					display: '发票打印时间',
					name: 'printTime',
					width: 112,
					sortable: false,
				},{
					display: '备注',
					name: 'remark',
					width: 102,
					sortable: false,
				} ],
				sortname: "id",
				sortorder: "desc"
			};
			$('.findPmP-down').sgDatagrid(defaults);
		
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