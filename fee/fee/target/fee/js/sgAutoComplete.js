(function($){
	var idx = -1; 
    var create = function(obj,settings){
    	 var bUList=$('#'+settings.bUList);/*对应的ul List*/
    	 var bId=$('#'+settings.bId);/*对应的ID*/
    	 var likeAttr=settings.likeAttr;/*模糊匹配的字段名*/
    	 var oparams=settings.oparams;/*其他参数*/
    	 var url=settings.url;
    	 var backLi=settings.backLi;/*回传的数据进行组装*/
    	 var backLiName=null;/*li 中的name*/
    	 var backLiText=null;/*li 中的text*/
    	 var backLiTexts=null;/*li 中的text 字符串拼接*/
    	 var clearIds=settings.clearIds;/*回车后需要清空的其他ID*/
    	 
    	 if(backLi){
    		 backLiName='v.'+backLi.name;
    		 backLiText=backLi.text;
    		 backLiText=backLiText.split(',');
    		 if(backLiText){
    			 backLiTexts=backLiText.join('+"   "+v.');
    			 backLiTexts='v.'+backLiTexts;
    		 }
    	 }
			
    	 /*执行搜索的方法*/
    	 var recommend=function(){
    		if(bUList){
    			bUList.html('');
    			idx = -1;
    			var params = {};
    			params.pageNo = 1;
    			params.pageSize = settings.rp;
    			params.filter = {};
    			params.filter[likeAttr] = obj.val();
    			if(oparams){
    				$.each(oparams,function(k,v){
    					params.filter[k] = v;
    				}); 
    			}
    			$.ajax( {
    				type : 'post', 
    				async: true,   
    				contentType : 'application/json',     
    				dataType : 'json',     
    				url : url,   
    				data:JSON.stringify(params),
    				success : function(data) {
    					if(data){
							if(data.items.length>0){
								bUList.html('');
							}else{
								bUList.append('<li>没有数据！</li>');
							}
							$.each(data.items,function(k,v){
								bUList.append('<li name='+eval(backLiName)+' id="rec_'+k+'">'+eval(backLiTexts)+'</li>');
							}); 
							if(data.items.length==1){/*如果只有一个结果，则默认选择那一项，不用手动选择*/
								/*获得第一个li*/
								var liFirst=$('li:first',bUList );
			                    select_word(liFirst.text(),liFirst.attr('name'),true);
			                    $('li',bUList ).show();
							}
							bUList.show();
    					}else{
    						bUList.hide();
    					}
    				},     
    				error : function(res,error) {
    					$(document).sgPup({message:'message_alert',text: res.responseText}); 
    				}    
    			});
    		}
             
         };

         /**
          * 选择推荐词
          * @param idx 推荐词索引
          */
         function next_word(idx){
        	 $('li',bUList ).removeClass("current");
             $("#rec_"+idx).addClass("current");
             //选择项聚焦时改变输入框内容
             var ul = $('li',bUList ).parent().parent();
             var input = ul.find("input[type='text']");
             input.val($("#rec_"+idx).text());
         };

         /**
          * 选定推荐词
          * @param word 推荐词
          */
         function select_word(word,id,isClearHide){
         	
        	bId.val(id);
            obj.val(word);  /*将选择的那个词填入搜索框 */
            
           /*回车后需要清空的其他ID
	   	/*	 if(clearIds){
	   			var clearIds1=clearIds.split(',');
	   			 $.each(clearIds1,function(k,v){
	   				$('#'+v).val('');
	   			 });
	   		 }*/
             if(!isClearHide){
            	 bUList.html('').hide(); /*去掉推荐栏*/
             }
             /*自定义触发事件*/
             if(settings.queryBack){
            	 settings.queryBack();
             }
         };
  
         /*windows回车事件禁用*/
   	     $(window.document).keydown(function(event){
   	         if(event.keyCode==13) {
   	        	 return false;
   	         }
   	     });
         /*键盘上下移动光标和按回车*/
         obj.keyup(function(event,ekeyCode){
             max = $('li',bUList ).length - 1; /*一共有max个推荐词(因为从0开始计数，所以要-1)*/
             var liCurrent=$("li.current",bUList);
             if(event.keyCode == 40){ /*按了下箭头*/
                 if(idx < max) idx++;
                 next_word(idx);
             }else if(event.keyCode == 38){  /*按了上箭头      */           
                 if(idx > 0) idx--;
                 next_word(idx);
             }else if(event.keyCode == 13 || ekeyCode==13){ /*按了回车*/
            	 /*如果此时有高亮候选词，选中它并搜索;如果此时没有候选词，则直接使用搜索框内容触发搜索
                 if(liCurrent.text()){ 
                     var id = liCurrent.attr('name');	
                     select_word(liCurrent.text(),id);
                 }else{ 
                 	 recommend();
                 }*/
                 //对enter键操作进行内容处理，空内容时查询下拉，下拉选项与输入框不一致时查询下来；
                 //只有在内容与下拉一致且选中才进行自定义查询
            	 var selectedLi = $("#rec_"+idx);
            	 var ul = selectedLi.parent();
            	 var div = ul.parent();
            	 var input = div.find("input[type='text']");
            	 if(input.val() != "" && input.val() == selectedLi.text()){
            		 var hiddenInput = div.find("input[type='hidden']");
            		 hiddenInput.val(selectedLi.attr("name"));
            		 settings.queryBack();
            		 ul.html("");
            	 }else{
                     recommend();
            	 }
             }
             event.stopPropagation();    /*  阻止事件冒泡*/
             event.preventDefault();  /*阻止默认行为 ( 表单提交 )*/
         });
         
         /*input 框改变事件 change事件没用*/
         obj.on('input',function(e){  
        	 var $this=$(this);
        	 if($this.val().length<1){
        		 var ul = $this.parent().find("ul");
        		 ul.html("");
        		 bId.val('');
        	 }
        });  
         
         
         /*鼠标点击推荐词被点击时*/
         $("body").on("click", "#"+settings.bUList+" li", function(event){
         	var id = $('#'+settings.bUList+' li.current').attr('name');	
         	if(id){
         		select_word($(this).text(),id);
         	}
             event.stopPropagation();    /*  阻止事件冒泡*/
             event.preventDefault();  /*阻止默认行为 ( 表单提交 )*/
         });
         /*鼠标滑过推荐词时*/
         $("body").on("mouseover", "#"+settings.bUList+" li", function(event){
        	 var id=$(this).attr("id");
        	 if(id){
        		 idx = id.replace(/[^0-9]/ig,""); /*只取数字部分*/
        		 next_word(idx);
        	 }
             event.stopPropagation();    /*  阻止事件冒泡*/
             event.preventDefault();  /*阻止默认行为 ( 表单提交 )*/
         });
         
         /* 事件处理 END */
         $(document).bind('click',function(){
        	 $('li',bUList ).hide();
         });
    };

    var methods = {
        init: function(options) {
            return this.each(function(){
                var $this = $(this);
                var settings = $this.data('sgAutoComplete');

                if(typeof(settings) == 'undefined') {

                    var defaults = {
                    		  bId:'',/*绑定的id*/
                        	  bUList:'',/*绑定的ul list*/
                    		  width: 150,
                              height:20,
                              url:'datagrid.json',
                              likeAttr:'',/*模糊匹配的字段名 eg:plate_no*/
                              oparms:{},/*其他参数*/
                              backLi:{},/*回传的数据进行组装,eg :{name:'vehicle_id',text:'plate_no,vehicle_id'}*/
                              clearIds:'',/*回车后需要清空的其他ID，eg：'unit_id,customer_id'*/
                              exporturl:false,
                              method:'POST',
                              dataType:'json',
                              errormsg:'连接出错！',
                              usepager:false,
                              page:1,
                              total:1,
                              pages:1,
                              useRp:true,
                              rp:10,
                              rpOptions:[10, 15, 20, 30, 50],
                              pagestat: '显示 {from} 到 {to} 条&nbsp;&nbsp;&nbsp;&nbsp;共 {total} 条纪录',
                              procmsg: '正在读取数据……！',
                              query: {},
                              nomsg: '没有数据！',   /* 搜索为空时显示的信息*/
                              onChangeSort: false, /* 改变排序时的自调函数*/
                              queryBack:false/*查询后触发事件*/
                    };

                    settings = $.extend({}, defaults, options);

                    $this.data('sgAutoComplete', settings);
                } else {
                    settings = $.extend({}, settings, options);
                };

                /* 代码在这里运行*/
                create($this,settings);
            });
        },
        destroy: function(options) {
            return $(this).each(function(){
                var $this = $(this);

                $this.removeData('sgAutoComplete');
            });
        },
        val: function(options) {
            var someValue = this.eq(0).html();

            return someValue;
        }
    };

    $.fn.sgAutoComplete= function(){
        var method = arguments[0];

        if(methods[method]) {
            method = methods[method];
            arguments = Array.prototype.slice.call(arguments, 1); /* 将参数转换成真正的数组*/
        } else if( typeof(method) == 'object' || !method ) {
            method = methods.init;
        } else {
            $.error( 'Method ' +  method + ' does not exist on jQuery.sgAutoComplete' );
            return this;
        };
        return method.apply(this, arguments);

    };

})(jQuery);
