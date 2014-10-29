(function($){
		//开始日期、结束日期赋默认值
		   var begeinDate =new Date();
		   begeinDate.setDate(1);
		   begeinDate=begeinDate.format('yyyy-MM-dd');
		   
		   var now =new Date().format('yyyy-MM-dd');
		   $('#startDate').val(begeinDate);
		   $('#endDate').val(now);
		var defaults = {
				title: "",
				width: 1717,
				height: 395,
				url: '../../pay/findPaytxtPage',
				datatype: "json",
				usepager: true,
				rownumbers: true,
				useRp: true,
				isCount:true,
				//colid: 'id', //主键

				colModel: [{
					display: '客户名称',
					name: 'customerName',
					width: 110,
					sortable: false
				},{
					display: '银行账号类型',
					name: 'accountType',
					width: 110,
					sortable: false,
					formatter: function(value, row) {
						if (value == 0) {
							value = '借记卡';
						} else if (value == 1) {
							value = '存折';
						}else if (value == 2) {
							value = '信用卡';
						}
						return value;
					}
				},{
					display: '银行账号',
					name: 'accountNo',
					width: 150,
					sortable: false
				},{
					display: '银行编号',
					name: 'bankNo',
					width: 80,
					sortable: false
				},{
					display: '银行客户名称',
					name: 'custName',
					width: 110,
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
					width: 200,
					sortable: false
				},
				 {
					display: '记录数',
					name: 'recordNum',
					width: 110,
					sortable: false
				},
				 {
					display: '费用总额(元)',
					name: 'feeSum',
					width: 110,
					sortable: false,
					isAdd:true
				},
				 {
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
				 {
					display: '操作时间',
					name: 'stamp',
					width: 110,
					sortable: false
				},
				 {
					display: '扣款时间',
					name: 'payTime',
					width: 110,
					sortable: false
				}
				],
				sortname: "id",
				sortorder: "desc"
			};
			$('.findPtP-down').sgDatagrid(defaults);
		
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
	        /* 自动搜索 END*/
	})(jQuery);