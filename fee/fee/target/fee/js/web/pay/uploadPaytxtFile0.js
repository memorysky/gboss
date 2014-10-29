(function($){
	var paytxtFiles;
	var ul=$('.sub_nav');
	$('#paytxtFile').change(function(){
		paytxtFiles=$(this).get(0);
		paytxtFiles=paytxtFiles.files;
		ul.empty();
		$.each(paytxtFiles,function(k,v){
			ul.append('<li>'+v.name+'</li>');
		});
	});
	//取消
	$('button:reset').on('click',function(){
		ul.empty();
		$('#div_result').html('');
	});
	
	$("#form_paytxt_imp").on('submit',function(e){
		var lis=$('li',ul);
		if(lis.length==0){
			$(document).sgPup({message:'message_info',text: '请选择要上传的文件!'});
			return false;
		}
		return true;
	});
	
	//查看数据锁状态
	$.ajax( {
		  type : 'post', 
		  async: false,   
		  contentType : 'application/json',     
		  dataType : 'json',     
		  url : '../../pay/getDatalockByCompanyId',  
		  data:JSON.stringify({}),
		  success : function(data) {
			  if(data){
				 var locktag=data.locktag;
				 if(locktag==1){//加锁
					 $('#btn_lockadd').show();
					 $('#spn_datalock').hide();
				 }else{//未加锁
					 $('#btn_lockadd').hide();
					 $('#spn_datalock').show();
				 }
			  }else{
				  $(document).sgPup({message:'message_info',text: '数据锁异常!'});
			  }
		  } ,     
		  error : function(res,error) { 
				$('#btn_lockadd').hide();
				$('#spn_datalock').show();
		  }     
		});
	
	//数据加锁按钮点击事件
	$('#btn_lockadd').on('click',function(){
		$(document).sgConfirm({
			text: '解锁后托收资料可以修改，确定解锁?',
			cfn: function(r) {
				if (r) {
					  $.ajax( {
						  type : 'post', 
						  async: false,   
						  contentType : 'application/json',     
						  dataType : 'json',     
						  url : '../../pay/addUpdateDataLock',  
						  data:JSON.stringify({locktag:0}),
						  success : function(data) {
							  if(data){
								  if(data.success){
									  $('#btn_lockadd').hide();
									  $('#spn_datalock').show();
								  }
								  $(document).sgPup({message:'message_info',text: data.msg});
							  }else{
								  $(document).sgPup({message:'message_info',text: data});
							  }
						  } ,     
						  error : function(res,error) { 
						  	 alert("responseText="+res.responseText+";error="+error);     
						  }     
						});
				}

			}
		});
	});
})(jQuery)

function callback(msg,flag)   
	{   
	$('#div_result').html(msg);
	 //$(document).sgPup({message:'message_info',text: msg});
	} 