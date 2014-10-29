(function($){
		var now =new Date().format('yyyy-MM-dd');
		 
		var editId=null;//编辑时选中行ID
		var editobj = null; //编辑时选中的对象
		
		var toAdd = function(){
    		//备忘保存
		   	$('#sys_memo_form').on('submit',function(e){
		   		//做表单提交
	            var params = {};

	            params.content = $('#sys_memo_remark','#sys_memo_form').val();
	            params.isAlert= 1;

	            //alert(JSON.stringify(params));
	            $.ajax( {
	    		  type : 'post', 
	    		  async: false,   
	    		  contentType : 'application/json',     
	    		  dataType : 'json',     
	    		  url : '../../sys/addUserRemark',   
	    		  data:JSON.stringify(params),
	    		  success : function(data) {
	    			  if(data){
	       				  if(data.success){
	       				  }
	       				$(document).sgPup({
							message: 'message_alert',
							text: data.msg
						});
	       				 
	       			  }else{
	       				  alert(data);
	       			  }
	    		  } ,     
	    		  error : function(res,error) { 
	    		  	 alert("responseText="+res.responseText+";error="+error);     
	    		  }    
	    		});
	            
	            $(document).sgWindow('close',{id:'sys_memo_window'}); 
	            $('#sys_memo_grid').sgDatagrid('reload','sgDatagrid');
	   		    $("#sys_memo_form").unbind();//以下两行可以阻止提交2次，暂时注释，也不会提交2次
			    e.stopPropagation();	
			    return false;
		   	});
    	}
		
		var toClose = function(){
			$(document).sgWindow('close', {
				id: 'sys_memo_window'
			});
	   	}
		 
		var addItem = function(){
			var adddefaults = {
	                title:'增加备忘',
	                id:'sys_memo_window',
	                form:'sys_memo_form',
	                url:'sys_memo_add_form.html',
	                width: 410,
	                height: 180,
	                buttons : [
	                           {name: '确定', type: 'submit', onpress : toAdd},
	                           {name: '关闭', type: 'submit', onpress : toClose }
	                       ]
	            };
	   		$(document).sgWindow(adddefaults);      	        
		}
		
		var delItem = function(){
	        var obj = $('#sys_memo_grid');
	        var bDiv = $('.bDiv',obj);

	        if($('input[type=checkbox][checked=checked]',bDiv)!=null&&$('input[type=checkbox][checked=checked]',bDiv).length==0){
	            $(document).sgPup({
					message: 'message_alert',
					text: '请选择一个选项！'
				});
	        }else{
	        	$(document).sgConfirm({
					text: '删除后不可恢复,确定删除?',
					cfn: function(r) {
						if (r) {
			        		var flag = false;
			        		$('input[type=checkbox][checked=checked]',bDiv).each(function(){
				                if($(this).attr("checked")){    
				                	editId=this.value;
				                	var params=[];
									params.push(editId);
				                	//打开窗口
				                	$.ajax( {
					            		  type : 'post', 
					            		  async: false,   
					            		  contentType : 'application/json',     
					            		  dataType : 'json',     
					            		  url : '../../sys/deleteUserRemarks',   
					            		  data:JSON.stringify(params),
					            		  success : function(data) {
					            			  if(data){
					            				 if(data.success){
					            					 $('#sys_memo_grid').sgDatagrid('reload','sgDatagrid');
					            				 }
					            				 $(document).sgPup({
					 								message: 'message_alert',
					 								text: data.msg
					 							});
					            			  }
					            		  } ,     
					            		  error : function(res,error) { 
					            		  	 alert("responseText="+res.responseText+";error="+error);     
					            		  }    
					            		}); 
				                }
				            });
			        		
			        		
	        	}	
					}
	        	});
	        }
	    }
		
		//初始化表格
	   	 var defaults = {
	   		        title: "备忘录列表",
	   		     	width:  '100%',
	   		        //width:  832,
			        height: 420,
	   		        url: '../../sys/findUserRemarks',
		   		    usepager: true,
		 	        rownumbers:true,
		 	        useRp: true,
		 	        colid:'id',  //主键
		 	        query:{startDate:now,endDate:now},
	   		        colModel : [
	   		            {display: '备忘信息', name : 'content', width : '62%', sortable : false},
	   		            {display: '是否提示', name : 'isAlert', width : '10%', sortable : false,formatter:function(value,row){
	   		                if(value==0){
	   		                    value = '否';
	   		                }else if(value==1){
	   		                    value = '是';
	   		                }else{
	   		                	value ="否";
	   		                }
	   		                return value;
	   		            }},   		            
	   		            {display: '日期', name : 'stamp', width : '20%', sortable : true,
		   		 		formatter: function(value, row) {
		   						if (value &&  value.length>10) {
		   							value =value.substring(0,10);
		   						} 
		   						return value;
		   					}
	   		            }
	   		        ],
	   		     	buttons : [
	   			            {name: '增加', bclass: 'add', onpress : addItem},
	   			            {separator: true},
				            {name: '删除', bclass: 'delete', onpress : delItem}
	   			        ],
	   		        searchitems :[
	   		            {display:'开始日期',name:'startDate',type:'date',value:now},
				      	{display:'结束日期',name:'endDate',type:'date',value:now}
	   		        ],
	   		     	order:true,
	   		        sortname: "stamp",
	   		        sortorder: "asc"
	   		    };
	   		    $('#sys_memo_grid').sgDatagrid(defaults); 
		})(jQuery)