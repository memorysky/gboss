(function($) {
	var editId,editObj,isView;
	var loadSuccess = function(){
 		//填充修改页面编辑前的值
 		   if(editId && editObj){
 		   	      $("#itemId",$("#service_add")).val(editObj.itemId);
				  $("#inputCode",$("#service_add")).val(editObj.itemCode);
				  $("#inputName",$("#service_add")).val(editObj.itemName);
			      $("#feetypeId",$("#service_add")).val(editObj.feetypeId);
				  $("#selectFlag",$("#service_add")).val(editObj.flag);
				  $("#inputRemark",$("#service_add")).val(editObj.remark);
				  $("#inputPrice",$("#service_add")).val(editObj.price);
				  
 		   }
 		//是查看
           if(isView){
         	  $('input,select',$("#service_add")).attr('disabled',true);
           }
          isView=false;
 		  editId=null;
 		  editObj=null;
 		  
 	   }


	var winClose = function() {//关闭添加页面
		$(document).sgWindow('close', {
			id: 'add_service'
		});
	}


	var savebill = function() {
		//保存服务项
		var params = {};
		var url = '../../service/addServiceitem';

		params.itemId = $('#itemId', '#service_add').val();
		params.itemCode = $('#inputCode', '#service_add').val();
		params.itemName = $('#inputName', '#service_add').val();
		params.feetypeId = $('#feetypeId', '#service_add').val();
		params.flag = $('#selectFlag', '#service_add').val();;
		params.remark = $('#inputRemark', '#service_add').val();
		params.price = $('#inputPrice', '#service_add').val();
		if(params.itemId){
			 url = '../../service/updateServiceitem';
		}
		$(document).sgConfirm({
			text: '确定保存服务项吗?',
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
									$('#serviceContent').sgDatagrid('reload', 'sgDatagrid');
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

		$('#service_add').unbind(); //以下两行可以阻止提交2次，暂时注释，也不会提交2次
		$(document).sgWindow('close', {
			id: 'add_service'
		});
		return false;
	};	


	var addItem = function() {//添加服务项
		var defaults = {
			title: '添加服务项',
			id: 'add_service',
			form: 'service_form',
			url: 'service_add.html',
			width: 420,
			height: 220,
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

	var modifyItem = function() {//修改服务项
	    	 	var obj = $('#serviceContent');
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
				    				title : '服务项修改',
				    				id : 'add_service',
				    				form : 'service_form',
				    				url : 'service_add.html',
				    				success: loadSuccess,
				    				width : 420,
				    				height : 220,
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

	var viewItem = function() {//查看服务项
	    	 	var obj = $('#serviceContent');
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
				    				title : '服务项查看',
				    				id : 'add_service',
				    				form : 'add_service_form',
				    				url : 'service_add.html',
				    				success: loadSuccess,
				    				width : 420,
				    				height : 220,
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

	var deleteItem = function() {//删除服务项
		var obj = $('#serviceContent');
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
						//var ids = [];
						$('input[type=checkbox][checked=checked]', bDiv).each(function() {
							if ($(this).attr("checked")) {
								//ids.push(editobj.packId);
								$.ajax({
									type: 'post',
									async: false,
									contentType: 'application/json',
									dataType: 'json',
									url: '../../service/deleteServiceitem',
									data: JSON.stringify({itemId:$(this).data('data').itemId}),
									success: function(data) {
										if (data) {
											if (data.success) {
												$('#serviceContent').sgDatagrid('reload', 'sgDatagrid');
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
		//width: 781,
		width: '100%',
		height: 395,
		url: '../../service/findServiceItem',
		datatype: "json",
		usepager: false,
		rownumbers: true,
		useRp: false,
		colid: 'itemId', //主键

		colModel: [{
			display: '服务项编号',
			name: 'itemCode',
			width: '15%',
			sortable: false
		}, {
			display: '服务项名称',
			name: 'itemName',
			width: '30%',
			sortable: false
		}/* ,{
			display: '计费类型',
			name: 'feetypeId',
			width: 140,
			sortable: false,
			formatter: function(value, row) {
				if (value == 1) {
					value = '服务费';
				} else if (value == 2) {
					value = 'SIM卡';
				}else if (value == 3) {
					value = '保险';
				}else if (value == 4) {
					value = '网上查车';
				}
				return value;
			}
		} */,{
			display: '是否对新用户有效',
			name: 'flag',
			width: '15%',
			sortable: false,
			formatter: function(value, row) {
				if (value == 1) {
					value = '<font color="green">是</font>';
				} else if (value == 0) {
					value = '<font color="red">否</font>';
				}
				return value;
			}
		},/*  {
			display: '价格',
			name: 'price',
			width: 140,
			sortable: false
		}, */{
			display: '定义时间',
			name: 'stamp',
			width: '15%',
			sortable: false
		}, {
			display: '服务项描述',
			name: 'remark',
			width: '15%',
			sortable: false
		}, ],

		buttons: [{
			name: '添加',
			bclass: 'add',
			onpress: addItem
		}, {
			name: '修改',
			bclass: 'modify',
			onpress: modifyItem
		}, {
			name: '查看',
			bclass: 'view',
			onpress: viewItem
		},{
			name: '删除',
			bclass: 'delete',
			onpress: deleteItem
		}, ],
		searchitems: [{
			display: '服务项编号', 
			value: '请输入服务项编号',
			name: 'itemCode',
			type: 'text'
		},{
			display: '服务项名称', 
			value: '请输入服务项名称',
			name: 'itemName',
			type: 'text'
		} ],
		sortname: "id",
		sortorder: "desc"
	};
	$('#serviceContent').sgDatagrid(defaults);
})(jQuery);		