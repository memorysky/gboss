(function($){
		//日期初始化
		$('#downLoadDate').val(new Date().format('yyyy-MM-dd'));
		
		var types=[];
		
		//查看有哪些托收机构,没有的就隐藏
		$.ajax( {
			  type : 'post', 
			  async: false,   
			  contentType : 'application/json',     
			  dataType : 'json',     
			  url : '../../service/findSubco',  
			  data:JSON.stringify({flag:1}),
			  success : function(data) {
				  if(data){
					  var divs= $('div.clearfix');
					  $.each(data,function(k,v){
						  if(v.agency==1){
							  $(divs[3]).show();
						  }else if(v.agency==2){
							  $(divs[2]).show();
						  }else if(v.agency==3){
							  $(divs[4]).show();
						  }
					  });
				  }else{
					  $(document).sgPup({message:'message_info',text: '请先设置分公司的银行账号!'});
				  }
			  } ,     
			  error : function(res,error) { 
				  $(document).sgPup({message:'message_info',text: res.responseText});
			  }     
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
						 $('#btn_lockadd').hide();
						 $('#spn_datalock').show();
					 }else{//未加锁
						 $('#btn_lockadd').show();
						 $('#spn_datalock').hide();
					 }
				  }else{
					  $(document).sgPup({message:'message_info',text: '数据锁异常!'});
				  }
			  } ,     
			  error : function(res,error) { 
					$('#btn_lockadd').show();
					$('#spn_datalock').hide();
			  }     
			});
		//下载按钮点击事件
		$('button[id^=btn_down]').on('click',function(){
			var $this=$(this);
			var id=$this.attr('id');
			if(id=='btn_down1'){
				id=1;
				types['1']=id;
			}else if(id=='btn_down2'){
				id=2;
				types['2']=id;
				if(typeof(types['1']) == 'undefined'){
					//$(document).sgPup({message:'message_alert',text: "请先下载金融中心的交易文件！"});
					$(document).sgPup({message:'message_info',text: '请先下载金融中心的交易文件！'});
					return false;
				}
			}else if(id=='btn_down3'){
				id=3;
				types['3']=id;
			}else if(id=='btn_down4'){
				id=4;
				types['4']=id;
				if(typeof(types['3']) == 'undefined'){
					$(document).sgPup({message:'message_info',text: '请先下载银盛的交易文件！'});
					//$(document).sgPup({message:'message_alert',text: "请先下载银盛的交易文件！"});
					return false;
				}
			}else if(id=='btn_down5'){
				id=5;
				types['5']=id;
			}
			$(document).sgConfirm({
				text: '确定下载托收文件?',
				cfn: function(r) {
					if (r) {
						//先判断数据锁有没有添加
						//查看数据锁状态
						$.ajax( {
							  type : 'post', 
							  async: false,   
							  contentType : 'application/json',     
							  dataType : 'json',     
							  url : '../../pay/getDatalockByCompanyId',  
							  data:JSON.stringify({stamp:$('#downLoadDate').val(),locktag:id,times:$('#times').val()}),
							  success : function(data) {
								  if(data){
									 var locktag=data.locktag;
									 if(locktag==1){//加锁
										 var url="../../pay/downPaytxtFile?downLoadDate="+$('#downLoadDate').val()+"&type="+id+"&times="+$('#times').val();
										 window.location.href=url;
									 }else if(locktag==0){//未加锁
										 $(document).sgPup({message:'message_info',text: '请先添加数据锁!'});
										 $('#spn_datalock').hide();
										 $('#btn_lockadd').show();
										 $('#btn_lockadd').css('border','1px solid red');
									 }else if(locktag==3){//该文件名已存在
										 $(document).sgConfirm({
												text: '该文件批次已存在,如需下载最新数据，建议修改批次再下载，是否修改批次？',
												cfn: function(r) {
													if (r) {
														
														}else{//不修改批次
															 var url="../../pay/downPaytxtFile?downLoadDate="+$('#downLoadDate').val()+"&type="+id+"&times="+$('#times').val();
															 window.location.href=url;
														}
													}});
									 }
								  }else{
									  $(document).sgPup({message:'message_info',text: '数据锁异常!'});
								  }
							  } ,     
							  error : function(res,error) { 
									$('#btn_lockadd').show();
									$('#spn_datalock').hide();
							  }     
							});
					}

				}
			});
		});
		
		//数据加锁按钮点击事件
		$('#btn_lockadd').on('click',function(){
			$(document).sgConfirm({
				text: '加锁后托收资料不能修改，确定加锁?',
				cfn: function(r) {
					if (r) {
						  $.ajax( {
							  type : 'post', 
							  async: false,   
							  contentType : 'application/json',     
							  dataType : 'json',     
							  url : '../../pay/addUpdateDataLock',  
							  data:JSON.stringify({locktag:1}),
							  success : function(data) {
								  if(data){
									  if(data.success){
										$('#btn_lockadd').hide();
										$('#spn_datalock').show();
									  }
									  $(document).sgPup({message:'message_info',text: data.msg});
								  }else{
									  alert(data);
								  }
							  } ,     
							  error : function(res,error) { 
								  $(document).sgPup({message:'message_info',text: res.responseText});
							  }     
							}); 
					}

				}
			});
		});
	})(jQuery)