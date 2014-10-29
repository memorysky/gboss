(function($){
    var create = function(obj,settings){
        var mDiv = $('<div></div>'); // 标题容器
        var hDiv = $('<div></div>'); // 表格头容器
        var bDiv = $('<div></div>'); // 表格主体容器
        var tDiv = $('<div></div>'); // 表格工具栏容器
        var sDiv = $('<article class="fpp-query clearfix"></article>'); // 表格搜索栏
        var pDiv = $('<div></div>'); // 表格分页容器
        var oDiv = $('<div class="overFlow"></div>');//用于滚动条的容器

        var hDivWidth=settings.width;
        //hDiv.css('width',settings.width);
        //bDiv.css('width',settings.width);
        pDiv.css('width',hDivWidth);// 设置表格的宽度
        
        if(hDivWidth && isNaN(hDivWidth) && hDivWidth.indexOf('%')>0){
        	hDivWidth=obj.width()*hDivWidth.substring(0,hDivWidth.length-1)/100-2;//减去border的宽度
        }    
        
        var sch_check = $("input[type='checkbox']");
        //复选框属性变化
        sch_check.change(function(){
            if (!$(this).attr("checked")) {
            	$(this).attr("checked","checked");
            }else if($(this).attr("checked")){
            	$(this).removeAttr("checked");
            };
        });
        
        //搜索栏查询
        $('#search').click(function(event){
        	var sourceset = $(obj).data('sgDatagrid');
        	var query = {};

        	//query = sourceset.query;   		
        	
        	var ckbId=null;
        	/*$("input[name='ckb_type']:checked").each(function() {
        		ckbId=$(this).attr('id');
        		if(ckbId=='ckb_date'){
        			var itext = $("#inputDate").val();
            		var iDate = $("#inputDate").attr("name");
                	query[iDate] = itext;
        		}
				if(ckbId=='ckb_num'){
					var sId = $("#numStart").val();
	        		var slID = $("#numEnd").attr("name");
	            	query[slID] = sId; 			
				 }
			});*/
        	var itext = $("#inputDate").val();
    		var iDate = $("#inputDate").attr("name");
        	query[iDate] = itext;
        	settings.query = query;        	
            createbody(obj,settings);
            
            event.preventDefault();//阻止默认事件
        	     	
        });
        
        // 初始化工具栏
        if(settings.buttons){
            tDiv.addClass('tDiv');
            $.each(settings.buttons,function(k,v){
                if(!v.separator){
                    var tool = $('<button></button>');
                    tool.addClass("btn-default");
                    tool.addClass("btn");
                    tool.html(v.name);

                    tool.on('click',function(){
                        (v.onpress)(this);
                    });
                    tDiv.append(tool);
                };
            });
            obj.append(tDiv);
        };

        // 初始化表头
        if(settings.colModel){
            hDiv.addClass('hDiv');
            
            //当数据内容的表格有滚动条时，表头与内容表格无法对齐，为了对齐，在表头表格外加上一个div
            hDiv.append('<div style="display:block" class="hDiv-header-inner"></div>');
         
            var table = $('<table class="table table-bordered table-condensed"></table>');
            table.attr('cellpadding',0);
            table.attr('cellspacing',0);
            table.css('width',hDivWidth);// 设置表格的宽度
            
           // alert(hDivWidth);
            //table.width(hDivWidth-17);
            var thead = $('<thead></thead>');
            var tr = $('<tr class="active"></tr>');
            
            if(settings.colid){
                var th_chx = $('<th axis="checkbox"></th>');
                var hdiv_chx = $('<input type="checkbox" name="checkall" />');
                
                if(!settings.isNotCheckall){// 是全选
                	th_chx.append(hdiv_chx);
                }else{
                	th_chx.attr('width','13px');
                };
                
                // 全选事件代码
                hdiv_chx.change(function(){
                    if (!hdiv_chx.attr("checked")) {
                        hdiv_chx.attr("checked",true);
                        $('input[type=checkbox]',bDiv).prop("checked",true);
                        $('input[type=checkbox]',bDiv).attr("checked","checked");
                    }else if(hdiv_chx.attr("checked")){
                        hdiv_chx.removeAttr("checked");
                        $('input[type=checkbox]',bDiv).prop("checked",null);
                        $('input[type=checkbox]',bDiv).removeAttr("checked");
                    };
                });
                tr.append(th_chx);
            };
            if(settings.rownumbers){
            	var th_rownum = $('<th axis="rownumbers">序号</th>');
            	th_rownum.width(34);
            	tr.append(th_rownum);
            };
            $.each(settings.colModel,function(k,v){
                var head = $('<th></th>');
                head.attr('axis', v.name);
                head.width(v.width);
                var div = $('<div></div>');
                if(v.sortable){
                    head.addClass('sorted');
                    div.attr('axis', v.name);
                    if(v.name==settings.sortname){
                        if(settings.sortname=='desc'){
                            div.attr('title','降序');
                            div.attr('sort','desc');
                        }else{
                            div.attr('title','升序');
                            div.attr('sort','asc');
                        };
                        div.addClass('s'+settings.sortorder);
                        div.addClass('thOver');
                    };

                    div.hover(function(){
                        if(!$(this).attr('class')){
                            $(this).addClass('sdesc');
                        };
                    },function(){
                       if($(this).attr('class').indexOf('thOver')>=0){

                       }else{
                           $(this).removeClass();
                       };
                    });

                    div.click(function(){
                        if($(this).attr('class').indexOf('thOver')>=0){
                            if($(this).attr('class')=='sdesc thOver'){
                                $(this).removeClass();
                                $(this).attr('title','升序');
                                $(this).attr('sort','asc');
                                $(this).addClass('sasc thOver');
                            }else{
                                $(this).removeClass();
                                $(this).attr('title','降序');
                                $(this).attr('sort','desc');
                                $(this).addClass('sdesc thOver');
                            };
                        }else{
                            $('.sorted .thOver' ,hDiv).each(function(){
                                $(this).removeClass();
                                $(this).removeAttr('title');
                            });
                            $(this).attr('title','降序');
                            $(this).attr('sort','desc');
                            $(this).addClass('thOver');

                        };
                        // 排序事件代码
                        changeSort(obj,settings,$(this));
                    });

                };
                if(v.editable){
                	head.attr('editable',v.editable);
                };
                if(v.disabled){
                	head.attr('disabled',v.disabled);
                };
                if(v.isnum){
                	head.attr('isnum',v.isnum);
                };
                if(v.isrequest){
                	head.attr('isrequest',v.isrequest);
                };
                if(v.html){
                	head.attr('ishtml',true);
                };
                if(v.isAdd){
                	head.attr('isAdd',v.isAdd);
                };
                div.append(v.display);
                head.append(div);
                tr.append(head);
            });
            thead.append(tr);
            table.append(thead);
            hDiv.append(table);
            //$('div.hDiv-header-inner',hDiv).append(table);
            oDiv.append(hDiv);

        };

        // 初始化表格主体
        bDiv.addClass('bDiv');
        bDiv.css('height',settings.height+'px');
        var tgrid = $('<table class="table table-striped table-bordered table-condensed"></table>');
        tgrid.attr('id',obj.attr("id")+"_tbl");
        tgrid.attr('cellpadding',0);
        tgrid.attr('cellspacing',0);
        tgrid.attr('border',0);
        //tgrid.width(hDivWidth-17);
        tgrid.css('width',hDivWidth);// 设置表格的宽度
        //当查询出来的数据滚动时，上面的表头跟着滚动
        bDiv.on('scroll',function(){
        	hDiv.scrollTop($(this).scrollTop());
        	hDiv.scrollLeft($(this).scrollLeft());
        });
        
        bDiv.append(tgrid);
        oDiv.append(bDiv);
        obj.append(oDiv);
        // 初始化分页栏
        if(settings.usepager){
            pDiv.addClass('pDiv');
            pDiv.addClass('clearfix');

            var pagenum = Math.ceil(settings.total/settings.rp);
            if(settings.useRp){
                var pgroup = $('<div></div>');
                pgroup.addClass('pGroup');
                var pselect = $('<select></select>');
                pselect.attr('id',obj.attr("id")+'_rp');
                pselect.attr('name','rp');
                $.each(settings.rpOptions,function(k,v){
                    var options = $('<option></option>');
                    if(settings.rp== v){
                        options.attr('selected','selected')
                    };
                    options.attr('value',v);
                    options.html(v+'&nbsp;&nbsp;');
                    pselect.append(options);
                });
                pgroup.append(pselect);
                pDiv.append(pgroup);

                $('select', pDiv).change(function () {
                    settings.rp = +this.value;
                    createbody(obj,settings);
                });
            };

            var pager = $('<div class="pGroup clearfix"><div class="pFirst button" title="首页"><span class="glyphicon glyphicon-fast-backward"></span></div><div class="pPrev button" title="上一页"><span class="glyphicon glyphicon-step-backward"></span></div></div>' +
                '<div class="pGroup"><span class="pControl">第<input class="input-sm" type="text" size="4" value="'+settings.page+'">页&nbsp;&nbsp;共<span>'+pagenum+'</span>页</span></div>'+
                '<div class="pGroup"><div class="pNext button" title="下一页"><span class="glyphicon glyphicon-step-forward"></span></div><div class="pLast button" title="末页"><span class="glyphicon glyphicon-fast-forward"></span></div></div>'+
                '<div class="pGroup"><div class="pReload button" title="刷新"><span class="glyphicon glyphicon-refresh"></span></div></div><div class="pGroup"><span class="pPageStat"></span></div>');
            pDiv.append(pager);

            $('.pReload', pDiv).click(function () {
            	$('input[type=checkbox]',hDiv).removeAttr("checked");
                createbody(obj,settings);
            });
            $('.pFirst', pDiv).click(function () {
            	$('input[type=checkbox]',hDiv).removeAttr("checked");
                changePage('first',obj,settings);
            });
            $('.pPrev', pDiv).click(function () {
            	$('input[type=checkbox]',hDiv).removeAttr("checked");
                changePage('prev',obj,settings);
            });
            $('.pNext', pDiv).click(function () {
            	$('input[type=checkbox]',hDiv).removeAttr("checked");
                changePage('next',obj,settings);
            });
            $('.pLast', pDiv).click(function () {
            	$('input[type=checkbox]',hDiv).removeAttr("checked");
                changePage('last',obj,settings);
            });
            $('.pControl input', pDiv).keydown(function (e) {
            	$('input[type=checkbox]',hDiv).removeAttr("checked");
                if (e.keyCode == 13){changePage('input',obj,settings)};
            });
            obj.css({'overflow-x':'hidden'});
            obj.append(pDiv);
        };
       
    };

    var buildpager = function(obj,settings){
        $('.pControl input', obj).val(settings.page);
        $('.pControl span', obj).html(settings.pages);
        var r1 = (settings.page - 1) * settings.rp + 1;
        var r2 = r1 + settings.rp - 1;
        if (settings.total < r2) {
            r2 = settings.total;
        };
        var stat = settings.pagestat;
        stat = stat.replace(/{from}/, r1);
        stat = stat.replace(/{to}/, r2);
        stat = stat.replace(/{total}/, settings.total);
        $('.pPageStat', obj).html(stat);
    };

    var createbody = function(obj,settings){
        $('.pPageStat', obj).html(settings.procmsg);
        $('.pReload', obj).addClass('loading');
	    var param = {};

	    if(settings.usepager){
	    	param['pageNo']=settings.page;
	    	param['pageSize']=settings.rp;
	    	param['filter']=settings.query;
	    }else{
	    	param = settings.query;
	    };
	    if(settings.order){
	    	param['order']=settings.sortname;
	    	param['is_desc']=(settings.sortorder=="desc"?true:false);
	    };
	    
	    obj.data('sgDatagrid', settings);

        $.ajax({
            type: "POST",
            async:false,
            contentType : "application/json",
            url: settings.url,
            data: JSON.stringify(param),
            dataType : "json",
            success: function(json){
            	if(settings.usepager){
            		if(json){
                        settings.total = json.total;
                        settings.page = json.index;
                    };
                    addData(json,obj,settings);
            	}else{            		
            		addDataOther(json,obj,settings);
            	};
            	
                
            }
        });
    };

    var addData = function(json,obj,settings){
        $('.pReload', obj).removeClass('loading');
        if (!json) {
            $('.pPageStat', obj).html(settings.errormsg);
            return false;
        };

        settings.pages = Math.ceil(settings.total / settings.rp);
        buildpager(obj,settings);

        var tby = $('<tbody></tbody>') ;
        var hdv = $('.hDiv',obj);
        
        //需要计数的列
        var countArray=[];//key为列名，value为合计的值
        
        $.each(json.items,function(i,row){
            var tr = $('<tr></tr>');
            if(settings.hiddenId){
            	var idinput = $("<input type='hidden' id='"+settings.hiddenId+"' name='"+settings.hiddenId+"' />");
            	idinput.val(row[settings.hiddenId]);
            	tr.append(idinput);
            };
            $('thead tr:first th', hdv).each( function(){
                var td = $('<td></td>');
                var editable = $(this).attr('editable');
                var disabled = $(this).attr('disabled');
                var isnum = $(this).attr('isnum');
                var isrequest = $(this).attr('isrequest');
                var tdx = $(this).attr('axis');
                var isAdd = $(this).attr('isAdd');
                //合计
                if(isAdd){
               	 if(countArray[tdx]){
               		 countArray[tdx] =Number(countArray[tdx])+Number(row[tdx]);
               	 }else{
               		 countArray[tdx] =Number(row[tdx]);
               	 }
                }
            	if(tdx=='checkbox'){
                    var check = $('<input type="checkbox" />');
                     check.attr('axis',row[settings.colid]);
                     check.attr('name',obj.attr("id")+"_chx");
                     check.attr('value',row[settings.colid]);                    
                     check.data('data',row);                   
                     td.append(check);
                     // 此处添加onchange事件代码
                     check.change(function(){
                         if (!check.attr("checked")) {
                             check.attr("checked","checked");
                         }else if(check.attr("checked")){
                             check.removeAttr("checked");
                         };
                     });
                 }else if(tdx=='rownumbers'){
                 	var tdwidth = $(this).css('width');
                    td.css('width',tdwidth);
                 	var num = $("#"+obj.attr("id")+'_rp').val()*(settings.page-1)+i+1;
                 	td.append(num);
                 	
                 }else{
                	 var tdwidth = $(this).css('width');
                     td.css('width',tdwidth);
                     var dv = $('<div></div>');
                	 if(editable){
                		 var editinput = $("<input type='text' />");
                     	 editinput.attr('name',tdx);
                     	 editinput.width(tdwidth);
                     	 if(disabled){
                    		 editinput.attr('disabled','disabled');
                    		 editinput.val(row[tdx]);
                    	 };
                     	 if(isnum){
                     		editinput.attr('type','number');
                     	 };
                     	 if(isrequest){
                     		editinput.attr('required',isrequest);
                     	 };
                     	if(ishtml){
                     		$.each(settings.colModel,function(k,v){
                                if(v.name==tdx&& v.html!=null){
                                    dv.html(v.html);
                                };
                          });
                     	 }else{
                     		dv.append(editinput);
                     	 };
                     }else{                    	 
                         var flag = false;
                         $.each(settings.colModel,function(k,v){
                               if(v.name==tdx&& v.formatter!=null){
                                   flag = true;
                                   dv.html((v.formatter)(row[tdx],row));
                               };
                         });

                         if(!flag) dv.html(row[tdx]);                         
                     };
                	 td.append(dv); 
                     
                 };              

                tr.append(td);
                td = null;
            });
            tby.append(tr);
        });
        
        //添加合计到 最后一行
        if(settings.isCount){
        	
        	 //如果表格数据需要合计，添加合计到 最后一行
            var totalTr= $('<tr></tr>');
            //第一列需要合计的列的索引
            var fisrtIndex=0;
        	 $('thead tr:first th', hdv).each( function(i,v){
        		 var td = $('<td></td>');
        		 var tdx = $(this).attr('axis');
        		 var isAdd = $(this).attr('isAdd');
        		 var tdwidth = $(this).css('width');
                 td.css('width',tdwidth);
        		//累加需要合计的列数据
        		 if(isAdd){
        			 if(fisrtIndex==0){
        				 fisrtIndex=i;
        			 }
	        		 if(countArray[tdx]){
	        			 
	        		    if(BASEisNotFloat(countArray[tdx])){
	        		    	 countArray[tdx]=countArray[tdx].toFixed(2);
	        		     }
	        			 td.append('<div style="font-weight:bold;">'+countArray[tdx]+'</div>');
	        		 }else{
	        			 td.append('<div style="font-weight:bold;">0</div>');
	        		 }
        		 }
        		 
            	 totalTr.append(td); 
        	 });
        	
        	//将“合计”放到统计列的前一列
        	if(fisrtIndex!=0){
        		$('td:eq('+(fisrtIndex-1)+')',totalTr).append('<div style="font-weight:bold;text-align: right;">合计</div>');
        	}
        	tby.append(totalTr);
        }
        var tbl_id = "#"+obj.attr("id")+"_tbl";
        var tbl = $(tbl_id);
        $('tr', tbl).unbind();
        $(tbl).empty();
        tbl.append(tby);
        $('tr:odd',tbl).addClass('normalRow');

        tby = null;
        hdv = null;
        tbl = null;
        tbl_id = null;
        json = null;
    };
    
    var addDataOther = function(json,obj,settings){

        var tby = $('<tbody></tbody>') ;
        var hdv = $('.hDiv',obj);
        
        //需要计数的列
        var countArray=[];//key为列名，value为合计的值
        
        $.each(json,function(i,row){
            var tr = $('<tr></tr>');
            if(settings.hiddenId){
            	var idinput = $("<input type='hidden' id='"+settings.hiddenId+"' name='"+settings.hiddenId+"' />");
            	idinput.val(row[settings.hiddenId]);
            	tr.append(idinput);
            };
            $('thead tr:first th', hdv).each( function(){
                var td = $('<td></td>');
                var editable = $(this).attr('editable');
                var disabled = $(this).attr('disabled');
                var isnum = $(this).attr('isnum');
                var isrequest = $(this).attr('isrequest');
                var ishtml = $(this).attr('ishtml');
                var tdx = $(this).attr('axis');
                var isAdd = $(this).attr('isAdd');
                //合计
                if(isAdd){
               	 if(countArray[tdx]){
               		 countArray[tdx] =Number(countArray[tdx])+Number(row[tdx]);
               	 }else{
               		 countArray[tdx] =Number(row[tdx]);
               	 }
                }
            	if(tdx=='checkbox'){
                    var check = $('<input type="checkbox"/>');
                     check.attr('axis',row[settings.colid]);
                     check.attr('name',obj.attr("id")+"_chx");
                     check.attr('value',row[settings.colid]);
                     check.data('data',row);
                     td.append(check);
                     // 此处添加onchange事件代码
                     check.change(function(){
                         if (!check.attr("checked")) {
                             check.attr("checked","checked");
                         }else if(check.attr("checked")){
                             check.removeAttr("checked");
                         };
                         
                         // add by zfy 2013-11-04 begin--用于追加checkBox触发的事件
                         if(settings.chkChange){
                        	 settings.chkChange(i,row,check.attr("checked"));
                         };
                         // add by zfy 2013-11-04 end--
                     });
                 }else if(tdx=='rownumbers'){
                 	var tdwidth = $(this).css('width');
                    td.css('width',tdwidth);
                 	var num = i+1;
                 	td.append(num);              	
                 }else{
                	 var tdwidth = $(this).css('width');
                     td.css('width',tdwidth);
                     var dv = $('<div></div>');
                	 if(editable){
                     	 var editinput = $("<input type='text' />");
                     	 editinput.attr('name',tdx);
                     	 editinput.width(tdwidth);
     
                     	 if(disabled){
                     		 editinput.attr('disabled','disabled');
                     		 editinput.val(row[tdx]);
                     	 };
                     	 if(isnum){
                     		editinput.attr('type','number');
                     	 };
                     	 if(isrequest){
                     		editinput.attr('required',isrequest);
                     	 };
                     	 if(ishtml){
                     		$.each(settings.colModel,function(k,v){
                                if(v.name==tdx&& v.html!=null){
                                    dv.html(v.html);
                                };
                          });
                     	 }else{
                     		dv.append(editinput);
                     	 };
                     	 
                     }else{                         
                         var flag = false;
                         $.each(settings.colModel,function(k,v){
                               if(v.name==tdx&& v.formatter!=null){
                                   flag = true;
                                   dv.html((v.formatter)(row[tdx],row));
                               };
                         });

                         if(!flag) dv.html(row[tdx]);                         
                     };
                	 td.append(dv);
                     
                 };
           

                tr.append(td);
                td = null;
            });
            tby.append(tr);
        });
        
        //添加合计到 最后一行
        if(settings.isCount){
        	 //如果表格数据需要合计，添加合计到 最后一行
            var totalTr= $('<tr></tr>');
            //第一列需要合计的列的索引
            var fisrtIndex=0;
        	 $('thead tr:first th', hdv).each( function(i,v){
        		 var td = $('<td></td>');
        		 var tdx = $(this).attr('axis');
        		 var isAdd = $(this).attr('isAdd');
        		 var tdwidth = $(this).css('width');
                 td.css('width',tdwidth);
        		//累加需要合计的列数据
        		 if(isAdd){
        			 if(fisrtIndex==0){
        				 fisrtIndex=i;
        			 }
	        		 if(countArray[tdx]){
	        			 
	        		    if(BASEisNotFloat(countArray[tdx])){
	        		    	 countArray[tdx]=countArray[tdx].toFixed(2);
	        		     }
	        			 td.append('<div style="font-weight:bold;">'+countArray[tdx]+'</div>');
	        		 }else{
	        			 td.append('<div style="font-weight:bold;">0</div>');
	        		 }
        		 }
        		 
            	 totalTr.append(td); 
        	 });
        	
        	//将“合计”放到统计列的前一列
        	if(fisrtIndex!=0){
        		$('td:eq('+(fisrtIndex-1)+')',totalTr).append('<div style="font-weight:bold;text-align: right;">合计</div>');
        	}
        	tby.append(totalTr);
        }
        var tbl_id = "#"+obj.attr("id")+"_tbl";
        var tbl = $(tbl_id);
        $('tr', tbl).unbind();
        $(tbl).empty();
        tbl.append(tby);
        $('tr:odd',tbl).addClass('normalRow');

        tby = null;
        hdv = null;
        tbl = null;
        tbl_id = null;
        json = null;
    };

    var appendData = function(json,obj,settings){
        $('.pReload', obj).removeClass('loading');
        if (!json) {
            $('.pPageStat', obj).html(settings.errormsg);
            return false;
        };

        settings.pages = Math.ceil(settings.total / settings.rp);
        buildpager(obj,settings);

        var tbl_id = "#"+obj.attr("id")+"_tbl";
        var tbl = $(tbl_id);
        $('tr', tbl).unbind();
       
        var tby = $('tbody',tbl) ;
        if(!tby.get(0)){
        	tby=$('<tbody></tbody>') ;
        	tbl.append(tby);
        };
        
        var hdv = $('.hDiv',obj);
        
        //需要计数的列
        var countArray=[];//key为列名，value为合计的值
        
        $.each(json.items,function(i,row){
            var tr = $('<tr></tr>');
            if(settings.hiddenId){
            	var idinput = $("<input type='hidden' id='"+settings.hiddenId+"' name='"+settings.hiddenId+"' />");
            	idinput.val(row[settings.hiddenId]);
            	tr.append(idinput);
            };
            $('thead tr:first th', hdv).each( function(){
                var td = $('<td></td>');
                var editable = $(this).attr('editable');
                var disabled = $(this).attr('disabled');
                var isnum = $(this).attr('isnum');
                var isrequest = $(this).attr('isrequest');
                var ishtml = $(this).attr('ishtml');
                var tdx = $(this).attr('axis');
                var isAdd = $(this).attr('isAdd');
                //合计
                if(isAdd){
               	 if(countArray[tdx]){
               		 countArray[tdx] =Number(countArray[tdx])+Number(row[tdx]);
               	 }else{
               		 countArray[tdx] =Number(row[tdx]);
               	 }
                }
            	if(tdx=='checkbox'){
                    var check = $('<input type="checkbox" style="margin-top:8px;" />');
                     check.attr('axis',row[settings.colid]);
                     check.attr('name',obj.attr("id")+"_chx");
                     check.attr('value',row[settings.colid]);                    
                     check.data('data',row);                   
                     td.append(check);
                     // 此处添加onchange事件代码
                     check.change(function(){
                         if (!check.attr("checked")) {
                             check.attr("checked","checked");
                         }else if(check.attr("checked")){
                             check.removeAttr("checked");
                         };
                     });
                 }else if(tdx=='rownumbers'){
                 	var tdwidth = $(this).css('width');
                    td.css('width',tdwidth);
                 	var num = $("#"+obj.attr("id")+'_rp').val()*(settings.page-1)+i+1;
                 	td.append(num);
                 	
                 }else{
                	 var tdwidth = $(this).css('width');
                     td.css('width',tdwidth);
                     var dv = $('<div></div>');
                	 if(editable){
                		 var editinput = $("<input type='text' />");
                     	 editinput.attr('name',tdx);
                     	 editinput.width(tdwidth);
                     	 if(disabled){
                    		 editinput.attr('disabled','disabled');
                    		 editinput.val(row[tdx]);
                    	 };
                     	 if(isnum){
                     		editinput.attr('type','number');
                     	 };
                     	 if(isrequest){
                     		editinput.attr('required',isrequest);
                     	 };
                     	if(ishtml){
                     		$.each(settings.colModel,function(k,v){
                                if(v.name==tdx&& v.html!=null){
                                    dv.html(v.html);
                                };
                          });
                     	 }else{
                     		dv.append(editinput);
                     	 };
                     }else{                    	 
                         var flag = false;
                         $.each(settings.colModel,function(k,v){
                               if(v.name==tdx&& v.formatter!=null){
                                   flag = true;
                                   dv.html((v.formatter)(row[tdx],row));
                               };
                         });

                         if(!flag) dv.html(row[tdx]);                         
                     };
                	 td.append(dv); 
                     
                 };               

                tr.append(td);
                td = null;
            });
            tby.append(tr);
        });
        //添加合计到 最后一行
        if(settings.isCount){
        	 //如果表格数据需要合计，添加合计到 最后一行
            var totalTr= $('<tr></tr>');
            //第一列需要合计的列的索引
            var fisrtIndex=0;
        	 $('thead tr:first th', hdv).each( function(i,v){
        		 var td = $('<td></td>');
        		 var tdx = $(this).attr('axis');
        		 var isAdd = $(this).attr('isAdd');
        		 var tdwidth = $(this).css('width');
                 td.css('width',tdwidth);
        		//累加需要合计的列数据
        		 if(isAdd){
        			 if(fisrtIndex==0){
        				 fisrtIndex=i;
        			 }
	        		 if(countArray[tdx]){
	        			 
	        		    if(BASEisNotFloat(countArray[tdx])){
	        		    	 countArray[tdx]=countArray[tdx].toFixed(2);
	        		     }
	        			 td.append('<div style="font-weight:bold;">'+countArray[tdx]+'</div>');
	        		 }else{
	        			 td.append('<div style="font-weight:bold;">0</div>');
	        		 }
        		 }
        		 
            	 totalTr.append(td); 
        	 });
        	
        	//将“合计”放到统计列的前一列
        	if(fisrtIndex!=0){
        		$('td:eq('+(fisrtIndex-1)+')',totalTr).append('<div style="font-weight:bold;text-align: right;">合计</div>');
        	}
        	tby.append(totalTr);
        }
        $('tr:odd',tbl).addClass('normalRow');

        tby = null;
        hdv = null;
        tbl = null;
        tbl_id = null;
        json = null;
    };
    
    var appendDataOther = function(json,obj,settings){
    	var tbl_id = "#"+obj.attr("id")+"_tbl";
        var tbl = $(tbl_id);
        $('tr', tbl).unbind();
       
        var tby = $('tbody',tbl);
        if(!tby.get(0)){
        	tby=$('<tbody></tbody>') ;
        	tbl.append(tby);
        };
        var hdv = $('.hDiv',obj);
        
        //需要计数的列
        var countArray=[];//key为列名，value为合计的值
        
        $.each(json,function(i,row){
            var tr = $('<tr></tr>');
            if(settings.hiddenId){
            	var idinput = $("<input type='hidden' id='"+settings.hiddenId+"' name='"+settings.hiddenId+"' />");
            	idinput.val(row[settings.hiddenId]);
            	tr.append(idinput);
            };
            $('thead tr:first th', hdv).each( function(){
                var td = $('<td></td>');
                var editable = $(this).attr('editable');
                var disabled = $(this).attr('disabled');
                var isnum = $(this).attr('isnum');
                var isrequest = $(this).attr('isrequest');
                var ishtml = $(this).attr('ishtml');
                var tdx = $(this).attr('axis');
                var isAdd = $(this).attr('isAdd');
                //合计
                if(isAdd){
               	 if(countArray[tdx]){
               		 countArray[tdx] =Number(countArray[tdx])+Number(row[tdx]);
               	 }else{
               		 countArray[tdx] =Number(row[tdx]);
               	 }
                }
            	if(tdx=='checkbox'){
                    var check = $('<input type="checkbox" style="margin-top:8px;" />');
                     check.attr('axis',row[settings.colid]);
                     check.attr('name',obj.attr("id")+"_chx");
                     check.attr('value',row[settings.colid]);
                     check.data('data',row);
                     td.append(check);
                     // 此处添加onchange事件代码
                     check.change(function(){
                         if (!check.attr("checked")) {
                             check.attr("checked","checked");
                         }else if(check.attr("checked")){
                             check.removeAttr("checked");
                         };
                         
                         // add by zfy 2013-11-04 begin--用于追加checkBox触发的事件
                         if(settings.chkChange){
                        	 settings.chkChange(i,row,check.attr("checked"));
                         };
                         // add by zfy 2013-11-04 end--
                     });
                 }else if(tdx=='rownumbers'){
                 	var tdwidth = $(this).css('width');
                    td.css('width',tdwidth);
                     //update by zfy begin 2013-12-18
                 	var num = $(tbl_id+' tr').length+1;
                 	//update by zfy end 2013-12-18
                 	td.append(num);              	
                 }else{
                	 var tdwidth = $(this).css('width');
                     td.css('width',tdwidth);
                     var dv = $('<div></div>');
                	 if(editable){
                     	 var editinput = $("<input type='text' />");
                     	 editinput.attr('name',tdx);
                     	 editinput.width(tdwidth);
     
                    	 editinput.val(row[tdx]);
                    	 
                     	 if(disabled){
                     		 editinput.attr('disabled','disabled');
                     	 };
                     	 if(isnum){
                     		editinput.attr('type','number');
                     	 };
                     	 if(isrequest){
                     		editinput.attr('required',isrequest);
                     	 };
                     	 if(ishtml){
                     		$.each(settings.colModel,function(k,v){
                                if(v.name==tdx&& v.html!=null){
                                    dv.html(v.html);
                                };
                          });
                     	 }else{
                     		dv.append(editinput);
                     	 };
                     }else{                         
                         var flag = false;
                         $.each(settings.colModel,function(k,v){
                               if(v.name==tdx&& v.formatter!=null){
                                   flag = true;
                                   dv.html((v.formatter)(row[tdx],row));
                               };
                         });

                         if(!flag){dv.html(row[tdx]);}                         
                     };
                	 td.append(dv);
                     
                 };
           

                tr.append(td);
                td = null;
            });
            tby.append(tr);
        });
        //添加合计到 最后一行
        if(settings.isCount){
        	 //如果表格数据需要合计，添加合计到 最后一行
            var totalTr= $('<tr></tr>');
            //第一列需要合计的列的索引
            var fisrtIndex=0;
        	 $('thead tr:first th', hdv).each( function(i,v){
        		 var td = $('<td></td>');
        		 var tdx = $(this).attr('axis');
        		 var isAdd = $(this).attr('isAdd');
        		 var tdwidth = $(this).css('width');
                 td.css('width',tdwidth);
        		//累加需要合计的列数据
        		 if(isAdd){
        			 if(fisrtIndex==0){
        				 fisrtIndex=i;
        			 }
	        		 if(countArray[tdx]){
	        			 
	        		    if(BASEisNotFloat(countArray[tdx])){
	        		    	 countArray[tdx]=countArray[tdx].toFixed(2);
	        		     }
	        			 td.append('<div style="font-weight:bold;">'+countArray[tdx]+'</div>');
	        		 }else{
	        			 td.append('<div style="font-weight:bold;">0</div>');
	        		 }
        		 }
        		 
            	 totalTr.append(td); 
        	 });
        	
        	//将“合计”放到统计列的前一列
        	if(fisrtIndex!=0){
        		$('td:eq('+(fisrtIndex-1)+')',totalTr).append('<div style="font-weight:bold;text-align: right;">合计</div>');
        	}
        	tby.append(totalTr);
        }
        $('tr:odd',tbl).addClass('normalRow');
        
        tby = null;
        hdv = null;
        tbl = null;
        tbl_id = null;
        json = null;
    };
    
    var changePage = function(ctype,obj,settings){
    	settings.newp = settings.page;
        switch (ctype) {
            case 'first':
                settings.newp = 1;
                break;
            case 'prev':
                if (settings.page > 1) {                	
                    settings.newp = parseInt(settings.page) - 1;
                };
                break;
            case 'next':
                if (settings.page < settings.pages) {
                    settings.newp = parseInt(settings.page) + 1;
                };
                break;
            case 'last':
                settings.newp = settings.pages;
                break;
            case 'input':
                var nv = parseInt($('.pControl input', obj).val());
                if (isNaN(nv)) {
                    nv = 1;
                };
                if (nv < 1) {
                    nv = 1;
                }else if (nv > settings.pages) {
                    nv = settings.pages;
                };
                $('.pControl input', obj).val(nv);
                settings.newp = nv;
                break;
        };
        if (settings.newp == settings.page) {
            return false;
        };
        settings.page = settings.newp;
        createbody(obj,settings);
    };

    var changeSort = function(obj,settings,head){
        settings.sortname = head.attr('axis');
        settings.sortorder = head.attr("sort");

        createbody(obj,settings);
    };

    var methods = {
        init: function(options) {
            return this.each(function(){
                var $this = $(this);
                var settings = $this.data('sgDatagrid');

                if(typeof(settings) == 'undefined') {

                    var defaults = {
                        title:false,
                        width: 600,
                        height:240,
                        url:'datagrid.json',
                        exporturl:false,
                        method:'POST',
                        dataType:'json',
                        errormsg:'连接出错！',
                        usepager:false,
                        page:1,
                        total:1,
                        pages:1,
                        useRp:false,
                        rp:10,
                        rpOptions:[10, 15, 20, 30, 50],
                        pagestat: '显示 {from} 到 {to} 条&nbsp;&nbsp;&nbsp;&nbsp;共 {total} 条纪录',
                        procmsg: '正在读取数据……！',
                        query: {},
                        nomsg: '没有数据！',   // 搜索为空时显示的信息
                        onChangeSort: false, // 改变排序时的自调函数
                        onSuccess: false, // 执行成功时的自调函数
                        onError: false,   // 执行出错时的自调函数
                        onSubmit: false, // 提交时的自调函数
                        order: false,
                        isNotCheckall: false,// 默认全选
                        isCount:false//是否合计
                    };

                    settings = $.extend({}, defaults, options);

                    $this.data('sgDatagrid', settings);
                } else {
                    settings = $.extend({}, settings, options);
                };

                // 代码在这里运行
                create($this,settings);
            });
        },
        reload: function(options){
           var settings = $(this).data('sgDatagrid');
           settings = $.extend({}, settings, options);
           if(settings.isFixed==false){// 如果条件不固定，则不做任何处理
        	   
           }else{　　　　　　　　　　　　// 否则把条件固定到数据中
        	   $(this).data('sgDatagrid', settings);
           };           
    
           createbody($(this),settings);
        },
        destroy: function(options){
            return $(this).each(function(){
                var $this = $(this);

                $this.removeData('sgDatagrid');
                $this.empty();
            });
        },
        val: function(options){
            var someValue = this.eq(0).html();

            return someValue;
        },
        appendData: function(rowDatas){
            var settings = $(this).data('sgDatagrid');
            if(settings.isFixed==false){// 如果条件不固定，则不做任何处理
         	   
            }else{　　　　　　　　　　　　// 否则把条件固定到数据中
         	   $(this).data('sgDatagrid', settings);
            };   
            
            if(settings.usepager){
        		if(json){
                    settings.total = json.total;
                    settings.page = json.index;
                };
                appendData(rowDatas,$(this),settings);
        	}else{
        		appendDataOther(rowDatas,$(this),settings);
        	};
         }
    };

    $.fn.sgDatagrid= function(){
        var method = arguments[0];

        if(methods[method]) {
            method = methods[method];
            arguments = Array.prototype.slice.call(arguments, 1);
        } else if( typeof(method) == 'object' || !method ) {
            method = methods.init;
        } else {
            $.error( 'Method ' +  method + ' does not exist on jQuery.sgDatagrid' );
            return this;
        };
        return method.apply(this, arguments);

    };
})(jQuery);