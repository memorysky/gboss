	(function($) {
        var editId,editObj,isView;
		var loadSuccess = function(){
	 		//填充修改页面编辑前的值
	 		   if(editId && editObj){
	                   $("#agency",$("#payAccount_add")).val(editObj.agency);
	                   $("#accountCode",$("#payAccount_add")).val(editObj.accountCode);
	         		   $("#accountNo",$("#payAccount_add")).val(editObj.accountNo);
	         		   $("#bankCode",$("#payAccount_add")).val(editObj.bankCode);
	         		   $("#flag",$("#payAccount_add")).val(editObj.flag);
	         		   $("#ctIsfix",$("#payAccount_add")).val(editObj.ctIsfix);
	         		   $("#ctDay",$("#payAccount_add")).val(editObj.ctDay);
	         		   $("#stamp",$("#payAccount_add")).val(editObj.stamp);
	         		   $("#acId",$("#payAccount_add")).val(editObj.acId);
					   
	 		   }
	 		   //是查看
              if(isView){
            	  $('input,select',$("#payAccount_add")).attr('disabled',true);
              }
              isView=false;
	 		  editId=null;
	 		  editObj=null;
	 		  
	 	   }

	var winClose = function() {//关闭添加页面
		$(document).sgWindow('close', {
			id: 'add_payAccount'
		});
	}

	var savebill = function() {//添加分公司银行账号
		var params = {};
		var url = '../../service/addSubco';

		params.agency = $('#agency', '#payAccount_add').val();
		params.accountCode = $('#accountCode', '#payAccount_add').val();
		params.accountNo = $('#accountNo', '#payAccount_add').val();
		params.bankCode = $('#bankCode', '#payAccount_add').val();
		params.ctIsfix = $('#ctIsfix', '#payAccount_add').val();
		params.ctDay = $('#ctDay', '#payAccount_add').val();
		params.flag = $('#flag', '#payAccount_add').val();
		params.stamp = $('#stamp', '#payAccount_add').val();
		params.acId = $('#acId', '#payAccount_add').val();
		if(params.ctIsfix==1 && params.ctDay.length<1){
			$(document).sgPup({
				message: 'message_alert',
				text: '请选择固定托收日期!'
			});
			return false;
		}
        if(params.acId){
        	url = '../../service/updateSubco';
        }
		$(document).sgConfirm({
			text: '确定保存分公司银行账号吗?',
			cfn: function(r) {
				if (r) {

					$.ajax({
						type: 'post',
						async: false,
						contentType: 'application/json',
						dataType: 'json',
						url: url,
						data: JSON.stringify(params),
						success: function(data) {
							if (data) {
								if (data.success) {
									$('#payAccountContent').sgDatagrid('reload', 'sgDatagrid');
								}
								$(document).sgPup({
									message: 'message_alert',
									text: data.msg
								});
							}
						},
						error: function(res, error) {
							alert(res.responseText);
						}
					});
				}
			}
		});

		$('#payAccount_add').unbind(); //以下两行可以阻止提交2次，暂时注释，也不会提交2次
		$(document).sgWindow('close', {
			id: 'add_payAccount'
		});
		return false;
	};	

	var addItem = function() {//添加托收信息
		var defaults = {
			title: '添加分公司银行账号',
			id: 'add_payAccount',
			form: 'payAccount_form',
			url: 'payAccount_add.html',
			width: 400,
			height: 260,
			buttons: [{
				name: '保存',
				type: 'submit',
				onpress: savebill,
			},{
				name: '关闭',
				onpress: winClose,
			}]
		};
		$(document).sgWindow(defaults);
	}

	var modifyItem = function() {//修改选中托收信息
	    	 	var obj = $('#payAccountContent');
		        var bDiv = $('.bDiv',obj);
		        if($('input[type=checkbox][checked=checked]',bDiv)!=null&&$('input[type=checkbox][checked=checked]',bDiv).length>1){
		            $(document).sgPup({message:'message_info',text: "只能选择一条记录！"});
		        }else if($('input[type=checkbox][checked=checked]',bDiv)!=null&&$('input[type=checkbox][checked=checked]',bDiv).length==0){
		            $(document).sgPup({message:'message_info',text: "请选择一个选项！"});
		        }else if($('input[type=checkbox][checked=checked]',bDiv)!=null&&$('input[type=checkbox][checked=checked]',bDiv).length==1){
		        	 $('input[type=checkbox][checked=checked]',bDiv).each(function(){
			                if($(this).attr("checked")){  
			                	editId=this.value;
			                	editObj=$(this).data('data');
				    			var defaults = {
				    				title : '分公司银行账号修改',
				    				id : 'add_payAccount',
				    				form : 'payAccount_add',
				    				url : 'payAccount_add.html',
				    				success: loadSuccess,
				    				width : 400,
				    				height : 260,
				    				buttons : [ {
				    					name : '保存',
				    					type : 'submit',
				    					onpress : savebill
				    				}, {
				    					name : '关闭',
				    					onpress : winClose
				    				}]
				    			};
				    			$(document).sgWindow(defaults);
			                }
			            });
		   }else{
			   $(document).sgPup({message:'message_info',text: "请选择一个选项！"});
		    }
    	
	}	

	var viewItem = function() {//查看托收信息
	    	 	var obj = $('#payAccountContent');
		        var bDiv = $('.bDiv',obj);
		        if($('input[type=checkbox][checked=checked]',bDiv)!=null&&$('input[type=checkbox][checked=checked]',bDiv).length>1){
		            $(document).sgPup({message:'message_info',text: "只能选择一条记录！"});
		        }else if($('input[type=checkbox][checked=checked]',bDiv)!=null&&$('input[type=checkbox][checked=checked]',bDiv).length==0){
		            $(document).sgPup({message:'message_info',text: "请选择一个选项！"});
		        }else if($('input[type=checkbox][checked=checked]',bDiv)!=null&&$('input[type=checkbox][checked=checked]',bDiv).length==1){
		        	 $('input[type=checkbox][checked=checked]',bDiv).each(function(){
			                if($(this).attr("checked")){  
			                	editId=this.value;
			                	editObj=$(this).data('data');
			                	isView=true;
				    			var defaults = {
				    				title : '分公司银行账号查看',
				    				id : 'add_payAccount',
				    				form : 'payAccount_add',
				    				url : 'payAccount_add.html',
				    				success: loadSuccess,
				    				width : 400,
				    				height : 260,
				    				buttons : [ {
				    					name : '关闭',
				    					onpress : winClose
				    				}]
				    			};
				    			$(document).sgWindow(defaults);
			                }
			            });
		   }else{
			   $(document).sgPup({message:'message_info',text: "请选择一个选项！"});
		    }
    	
	}

	var deleteItem = function() {//删除托收信息
		var obj = $('#payAccountContent');
		var bDiv = $('.bDiv', obj);

		if ($('input[type=checkbox][checked=checked]', bDiv) != null && $('input[type=checkbox][checked=checked]', bDiv).length == 0) {
			$(document).sgPup({
				message: 'message_info',
				text: "请选择一个选项！"
			});
		}else if ($('input[type=checkbox][checked=checked]', bDiv) != null && $('input[type=checkbox][checked=checked]', bDiv).length > 1) {
			$(document).sgPup({
				message: 'message_info',
				text: "选择多于一个选项！！"
			});
		}else if ($('input[type=checkbox][checked=checked]', bDiv) != null && $('input[type=checkbox][checked=checked]', bDiv).length == 1) {
			$(document).sgConfirm({
				text: '删除后不可恢复,确定删除?',
				cfn: function(r) {
					if (r) {
						var flag = false;
						$('input[type=checkbox][checked=checked]', bDiv).each(function() {
							if ($(this).attr("checked")) {
								//ids.push(editobj.packId);
								$.ajax({
									type: 'post',
									async: false,
									contentType: 'application/json',
									dataType: 'json',
									url: '../../service/deleteSubco',
									data: JSON.stringify({acId:$(this).data('data').acId}),
									success: function(data) {
										if (data) {
											if (data.success) {
												$('#payAccountContent').sgDatagrid('reload', 'sgDatagrid');
											}
											$(document).sgPup({
												message: 'message_alert',
												text: data.msg
											});
										}
									},
									error: function(res, error) {
										if (res && res.responseText) {
											$(document).sgPup({
												message: 'message_info',
												text: res.responseText
											});
										}
									}
								});
							}
						});
						
						
					}

				}
			});
		}
	}

	var defaults = {
		title: "",
		width: '100%',
		//width: 970,
		height: 395,
		url: '../../service/findSubco',
		datatype: "json",
		usepager: false,
		rownumbers: true,
		useRp: true,
		colid: 'acId', //主键

		colModel: [{
			display: '托收机构',
			name: 'agency',
			width: '10%',
			sortable: false,
			formatter: function(value, row) {
				if (value == 1) {
					value = '银盛';
				} else if (value == 2) {
					value = '金融中心';
				}else if(value == 3){
					value = '银联';
				}
				return value;
			}
		}, {
			display: '收付单位代码',
			name: 'accountCode',
			width: '10%',
			sortable: false
		}, {
			display: '收付代码',
			name: 'accountNo',
			width: '10%',
			sortable: false
		},{
			display: '代办银行号',
			name: 'bankCode',
			width: '10%',
			sortable: false
		},{
			display: '是否固定托收',
			name: 'ctIsfix',
			width: '10%',
			sortable: false,
			formatter: function(value, row) {
				if (value == 1) {
					value = '<font color="green">是</font>';
				} else if (value == 0) {
					value = '<font color="red">否</font>';
				}
				return value;
			}
		},{
			display: '固定托收日期',
			name: 'ctDay',
			width: '10%',
			sortable: false
		},{
			display: '标志',
			name: 'flag',
			width: '10%',
			sortable: false,
			formatter: function(value, row) {
				if (value == 1) {
					value = '<font color="green">有效</font>';
				} else if (value == 0) {
					value = '<font color="red">无效</font>';
				}
				return value;
			}
		},{
			display: '定义时间',
			name: 'stamp',
			width: '20%',
			sortable: false
		}],

		buttons: [{
			name: '添加',
			bclass: 'add',
			onpress: addItem
		}, {
			name: '修改',
			bclass: 'add',
			onpress: modifyItem
		}, {
			name: '查看',
			bclass: 'add',
			onpress: viewItem
		},{
			name: '删除',
			bclass: 'delete',
			onpress: deleteItem
		}, ],
		searchitems: [{
			display: '托收机构', 
			value: '请输入机构',
			name: 'agency',
			html:'<select name="agency"><option value="">请选择---</option><option value="1">银盛</option><option value="2">金融中心</option><option value="3">银联</option></select>'
		}, ],
		sortname: "id",
		sortorder: "desc"
	};
	$('#payAccountContent').sgDatagrid(defaults);
})(jQuery);		