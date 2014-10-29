(function($){
		  var beginDate =new Date();
		   beginDate.setDate(beginDate.getDate()-1);
		   beginDate=beginDate.format('yyyy-MM-dd');
		   
		   var now =new Date().format('yyyy-MM-dd');
		   
		var userDataList = {};//操作人 key:name,value:id
		var editId=null;//编辑时选中行ID
		var editobj = null; //编辑时选中的对象
		
		//初始化表格
	   	 var defaults = {
	   		        title: "操作日期列表",
	   		        width:  '100%',
	   		        //width:  832,
			        height: 320,
	   		        url: '../../sys/findOperatelogPage',
		   		    usepager: true,
		 	        rownumbers:true,
		 	        useRp: true,
		 	        query:{startDate:now,endDate:now},
	   		        colModel : [
						{display: '操作人', name : 'user_name', width : '13%', sortable : false},
	   		            {display: '模块名称', name : 'sname', width :  '20%', sortable : false},
	   		            {display: '操作类型', name : 'op_type', width : '10%', sortable : false,formatter:function(value,row){
	   		                if(value==1){
	   		                    value = '查询';
	   		                }else if(value==2){
	   		                    value = '增加';
	   		                }else if(value==3){
	   		                	value ="修改";
	   		                }else if(value==4){
	   		                	value ="删除";
	   		                }
	   		                return value;
	   		            }}, 
	   		         	{display: '备注', name : 'remark', width :  '35%', sortable : false},
	   		          
	   		            {display: '时间', name : 'stamp', width :  '15%', sortable : true}
	   		        ],
	   		     	buttons : [
	   			        ],
	   		        searchitems :[
	   		            {display:'开始日期',name:'startDate',type:'date',value:beginDate},
				      	{display:'结束日期',name:'endDate',type:'date',value:now},
				      	{display:'操作人',html:'<div class="searchDis"><input type="text" class="form-control" autocomplete="on" name="userName" id="userName"><input type="hidden"  class="" value="" name="userId" id="userId"><ul class="show_list" id="customerlist"></ul></div>'
				      	},
				      	{
							display: '模块名称', 
							name: 'modelId',
							html:'<select name="modelId"><option value="">请选择---</option></select>'
						},
						{
							display: '操作类型', 
							name: 'opType',
							html:'<select name="opType"><option value="">请选择---</option><option value="1">查询</option><option value="2">增加</option><option value="3">修改</option><option value="4">删除</option></select>'
						}
	   		        ],
	   		     	order:true,
	   		        sortname: "stamp",
	   		        sortorder: "desc"
	   		    };
	   		    $('#dgd_oplog').sgDatagrid(defaults); 
	   		    $('input[name=startDate]').val(beginDate);
	   		    $('input[name=endDate]').val(now);
	   		    
	   		   $("input,select").keyup(function(event){
	            if(event.keyCode == 13){
	            	 $("#search").trigger('click');
	            }});
	   		/* $("#userName").sgAutoComplete({
				  bId:'userId',//绑定的id
	        	  bUList:'customerlist',//绑定的ul list
	        	  url:'../../getOrgOperators',
	              likeAttr:'opname',//模糊匹配的字段名
	              oparams:{isCompany:true},
	              backLi:{name:'opid',text:'opname'},
	              //clearIds:'vehicle_id,customer_id',
	              queryBack:function(){
	            	  $("#search").trigger('click');
	              }
			});*/
	   		//填充系统页面
	   		    $.ajax( {
	   			  type : 'post', 
	   			  async: false,   
	   			  contentType : 'application/json',     
	   			  dataType : 'json',     
	   			  url : '../../sys/findSysValue',   
	   			  data:JSON.stringify({stype:999,svalue:30000}),
	   			  success : function(data) {
	   				 if(data){
		 				  var type=$('#dgd_oplog .sDiv select[name=modelId]');
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
		})(jQuery)