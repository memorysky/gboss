 
        $(document).ready(function(){
            $(".item").mouseover(function(){
                $(this).addClass("current");
            });
            $(".item").mouseout(function(){
                $(this).removeClass("current");
            });

            $("#asos").on('click', function() {
            	var sosWin=window.open('http://s.952100.com','sos');
            	sosWin.focus();
            });
			var toUpdateUser = function() {
				$('#sys_user_form').on(
						'submit',
						function(e) {
							var params = {};
							params.mobile = $('#mobile','#sys_user_form').val();
							params.phone = $('#phone','#sys_user_form').val();
							params.fax = $('#fax','#sys_user_form').val();
							params.mail = $('#email','#sys_user_form').val();
							params.post = $('#post','#sys_user_form').val();
							params.remark = $('#remark','#sys_user_form').val();
							$.ajax({
								type : 'post',
								async : false,
								contentType : 'application/json',
								dataType : 'json',
								url : 'updateOperator',
								data : JSON.stringify(params),
								success : function(data) {
									if (data) {
										if (data.success) {
											$(document).sgPup({message:'message_alert',text: data.msg});
										} else {
											$(document).sgPup({message:'message_alert',text: "修改用户资料失败！"});
										}

									} else {
										alert(data);
									}
								},
								error : function(res, error) {
									$(document).sgPup({message:'message_alert',text: "responseText=" + res.responseText
										+ ";error=" + error});
								}
							});

							$(document).sgWindow('close', {
								id : 'sys_user_window'
							});
							$("#sys_user_form").unbind();
							e.stopPropagation();
							return false;
						});
			};
			var toCloseUser = function() {
				$("#sys_user_window" + "_mask").remove();
				$("#sys_user_window").remove();
			}
			var loadSuccess = function(){
				$.ajax({
					type : 'post',
					async : false,
					contentType : 'application/json',
					dataType : 'json',
					url : 'getCurrentOperator',
					data : JSON.stringify({}),
					success : function(data) {
						if (data) {
							$("#mobile","#sys_user_form").val(data.mobile);
							$("#phone","#sys_user_form").val(data.phone);
							$("#fax","#sys_user_form").val(data.fax);
							$("#email","#sys_user_form").val(data.mail);
							$("#post","#sys_user_form").val(data.post);
							$("#remark","#sys_user_form").val(data.remark);
						} else {
							alert(data);
						}
					},
					error : function(res, error) {
						
					}
				})
			}
			
            $("#updateuser").on('click', function() {
				 var adddefaults = {
					title : '修改用户资料',
					id : 'sys_user_window',
					form : 'sys_user_form',
					url : 'web/hp/user.html',
					width : 380,
					height : 260,
					success: loadSuccess,
					buttons : [ {
						name : '确定',
						type : 'submit',
						onpress : toUpdateUser
					}, {
						name : '关闭',
						onpress : toCloseUser
					} ]
				};
				$(document).sgWindow(adddefaults); 
			});
            
			var toClosePWD = function() {
				$("#sys_pwd_window" + "_mask").remove();
				$("#sys_pwd_window").remove();
			}
            
			var toUpdatePWD = function() {
				$('#sys_pwd_form').on(
						'submit',
						function(e) {
							var params = {};
							params.oldPassword = $('#oldpwd','#sys_pwd_form').val();
							params.newPassword = $('#newpwd','#sys_pwd_form').val();
							var newpwd2 = $('#newpwd2','#sys_pwd_form').val();
							if(newpwd2!=params.newPassword){
								$(document).sgPup({message:'message_alert',text: "两次新密码输入不一致！"});
								return false;
							}
							$.ajax({
								type : 'post',
								async : false,
								contentType : 'application/json',
								dataType : 'json',
								url : 'updatePassword',
								data : JSON.stringify(params),
								success : function(data) {
									if (data) {
										if (data.success) {
											$(document).sgPup({message:'message_alert',text: data.msg});
										} else {
											$(document).sgPup({message:'message_alert',text: "修改密码失败！"});
										}

									} else {
										alert(data);
									}
								},
								error : function(res, error) {
									$(document).sgPup({message:'message_alert',text: "responseText=" + res.responseText
										+ ";error=" + error});
								}
							});

							$(document).sgWindow('close', {
								id : 'sys_pwd_window'
							});
							$("#sys_pwd_form").unbind();
							e.stopPropagation();
							return false;
						});
			};
            
			$("#updatepwd").on('click', function() {
				var adddefaults = {
					title : '修改密码',
					id : 'sys_pwd_window',
					form : 'sys_pwd_form',
					url : 'web/hp/pwd.html',
					width : 340,
					height : 135,
					buttons : [ {
						name : '确定',
						type : 'submit',
						onpress : toUpdatePWD
					}, {
						name : '关闭',
						onpress : toClosePWD
					} ]
				};
				$(document).sgWindow(adddefaults);
			});
			
			/*设置个人主页*/
			$("#setmainpage").on('click', function() {
				var query = {};
				query.mainUrl = window.mainUrl;
				query.mainModuleid = window.mainModule;
				$.ajax({
					type : 'post',
					async : false,
					contentType : 'application/json',
					dataType : 'json',
					url : 'updateOperator',
					data : JSON.stringify(query),
					success : function(data) {
						if (data) {
							$(document).sgPup({message:'message_alert',text: "个人主页修改成功！"});
						} else {
							alert(data);
						}
					},
					error : function(res, error) {
					}
				});
			});
			
			var toClose = function() {
				$("#sys_memo_window" + "_mask").remove();
				$("#sys_memo_window").remove();
			}
			
			var toAdd = function() {
				/*备忘保存*/
				$('#sys_memo_form').on(
						'submit',
						function(e) {
							var params = {};

							params.content = $('#sys_memo_remark',
									'#sys_memo_form').val();
							params.isAlert = 1;

							$.ajax({
								type : 'post',
								async : false,
								contentType : 'application/json',
								dataType : 'json',
								url : 'sys/addUserRemark',
								data : JSON.stringify(params),
								success : function(data) {
									if (data) {
										if (data.success) {
											$(document).sgPup({message:'message_alert',text: data.msg});
										} else {
											$(document).sgPup({message:'message_alert',text: "增加备忘录失败！"});
										}

									} else {
										alert(data);
									}
								},
								error : function(res, error) {
									$(document).sgPup({message:'message_alert',text: "responseText=" + res.responseText
										+ ";error=" + error});
								}
							});

							$(document).sgWindow('close', {
								id : 'sys_memo_window'
							});
							if ($('#sys_memo_grid').length > 0)
								$('#sys_memo_grid').sgDatagrid('reload',
										'sgDatagrid');
							$("#sys_memo_form").unbind();
							e.stopPropagation();
							return false;
						});
			};
			
			$("#addMemo").on('click', function() {
				var adddefaults = {
					title : '增加备忘',
					id : 'sys_memo_window',
					form : 'sys_memo_form',
					url : 'web/hp/sys_memo_add_form.html',
					width : 380,
					height : 150,
					buttons : [ {
						name : '确定',
						type : 'submit',
						onpress : toAdd
					}, {
						name : '关闭',
						type : 'button',
						onpress : toClose
					} ]
				};
				$(document).sgWindow(adddefaults);
			});
			
			var oftenVehicleWinLoad=function(){
				var data=window.gbossLocalStorage.getItems('storage_vehicle');
				if(data){
					$('#dgd_often_vehicle').sgDatagrid('appendData',data);
				}
			}
			var clearItems=function(){
	    		window.gbossLocalStorage.removeItem('storage_vehicle');
	    		$('#often_vehicle_window #dgd_often_vehicle .bDiv table').empty();
	    	}
			$("#addOftenVehicle").on('click', function() {
				var adddefaults = {
					title : '最近操作车牌号码、车载电话',
					id : 'often_vehicle_window',
					form : 'often_vehicle_form',
					url : 'web/hp/often_vehicle.html',
					width : 440,
					height : 280,
					success : oftenVehicleWinLoad,
					buttons : [ {
						name:'清空',
						type:'button',
						onpress:clearItems
					},{
						name : '关闭',
						type : 'button',
						onpress : function() {
			    			$(document).sgWindow('close', {
			    				id: 'often_vehicle_window'
			    			});
			    		}
					} ]
				};
				$(document).sgWindow(adddefaults);
			});
			
			$.ajax({
				type : 'post',
				async : false,
				contentType : 'application/json',
				dataType : 'json',
				url : 'getCurrentOperator',
				data : JSON.stringify({}),
				success : function(data) {
					if (data) {
						$('#welcometext').html("欢迎！" + data.opname + ".");
						if(data.url){
						}else{
							data.url='homepage.html';
						}
						window.mainUrl = data.url;
						window.mainModule = data.moduleid;
						$("#homepage").attr('src',  data.url);
						
					} else {
						alert(data);
					}
				},
				error : function(res, error) {
					window.location.reload();
				}
			});
        });
   
   
        (function($) {
            var defaults = {
                url : 'moduleList',
            };
            $('#navFrame').sgNav(defaults);
            
            
            var toSubmit = function(e) {
    			/*备忘修改*/
    			$('#sys_memo_form').on(
    					'submit',
    					function(e) {
    						var params = {};

    						params.id = $('#sys_memo_id', '#sys_memo_form')
    								.val();
    						params.content = $('#sys_memo_remark',
    								'#sys_memo_form').val();
    						params.isAlert = 0;
    						$.ajax({
    							type : 'post',
    							async : false,
    							contentType : 'application/json',
    							dataType : 'json',
    							url : 'sys/updateUserRemark',
    							data : JSON.stringify(params),
    							success : function(data) {
    								if (data) {
    									if (data.success) {
    										$(document).sgPup({message:'message_alert',text: data.msg});
    									} else {
    										$(document).sgPup({message:'message_alert',text: "备忘录修改失败！"});
    									}

    								} else {
    									alert(data);
    								}
    							},
    							error : function(res, error) {
    								$(document).sgPup({message:'message_alert',text: "responseText=" + res.responseText
    									+ ";error=" + error});
    							}
    						});

    						$(document).sgWindow('close', {
    							id : 'sys_memo_window'
    						});
    						$("#sys_memo_form").unbind();
    						e.stopPropagation();
    						return false;
    					});
    		}

    		var toClose = function() {
    			$(document).sgWindow('close', {
    				id: 'sys_memo_window'
    			});
    		}
            $.ajax({
    			type : 'post',
    			async : false,
    			contentType : 'application/json',
    			dataType : 'json',
    			url : 'sys/findUserRemarks',
    			data : JSON.stringify({
    				pageNo : 1,
    				pageSize : 1,
    				filter : {
    					isAlert : 1
    				}
    			}),
    			success : function(data) {
    				if (data) {
    					if (data.items.length > 0) {
    						var remarkdefaults = {
    							title : '备忘提示',
    							id : 'sys_memo_window',
    							form : 'sys_memo_form',
    							url : 'web/hp/sys_memo_form.html',
    							width : 380,
    							height : 150,
    							buttons : [ {
    								name : '不再提示',
    								type : 'submit',
    								onpress : toSubmit
    							}, {
    								name : '关闭',
    								type : 'submit',
    								onpress : toClose
    							} ]
    						};
    						$(document).sgWindow(remarkdefaults);
    					}
    				} else {
    					alert(data);
    				}
    			},
    			error : function(res, error) {
    			}
    		});
        })(jQuery);
   
   
    function addTab(title, url){
        if ($('#tabs').tabs('exists', title)){
            $('#tabs').tabs('select', title);
            var currTab = $('#tabs').tabs('getSelected');
            var url = $(currTab.panel('options').content).attr('src');
            if(url != undefined && currTab.panel('options').title != '首页') {
                $('#tabs').tabs('update',{
                    tab:currTab,
                    options:{
                        content:createFrame(url)
                    }
                })
            }
        } else {
            var content = createFrame(url);
            $('#tabs').tabs('add',{
                title:title,
                content:content,
                closable:true
            });
        }
        tabClose();
    }

    function createFrame(url) {
        window.mainUrl = url;
        var s = '<iframe scrolling="no" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';
        return s;
    }
            
    function tabClose() {
        /*双击关闭TAB选项卡*/
        $(".tabs-inner").dblclick(function(){
            var subtitle = $(this).children(".tabs-closable").text();
            $('#tabs').tabs('close',subtitle);
        })
        /*为选项卡绑定右键*/
        $(".tabs-inner").bind('contextmenu',function(e){
            $('#mm').menu('show', {
                left: e.pageX,
                top: e.pageY
            });

            var subtitle =$(this).children(".tabs-closable").text();

            $('#mm').data("currtab",subtitle);
            $('#tabs').tabs('select',subtitle);
            return false;
        });
    }       
    /*绑定右键菜单事件*/
    function tabCloseEven() {
        /*刷新*/
        $('#mm-tabupdate').click(function(){
            var currTab = $('#tabs').tabs('getSelected');
            var url = $(currTab.panel('options').content).attr('src');
            if(url != undefined && currTab.panel('options').title != '首页') {
                $('#tabs').tabs('update',{
                    tab:currTab,
                    options:{
                        content:createFrame(url)
                    }
                })
            }
        })
       /*关闭当前*/
        $('#mm-tabclose').click(function(){
            var currtab_title = $('#mm').data("currtab");
            $('#tabs').tabs('close',currtab_title);
        })
        /*全部关闭*/
        $('#mm-tabcloseall').click(function(){
            $('.tabs-inner span').each(function(i,n){
                var t = $(n).text();
                if(t != '首页') {
                    $('#tabs').tabs('close',t);
                }
            });
        });
        /*关闭除当前之外的TAB*/
        $('#mm-tabcloseother').click(function(){
            var prevall = $('.tabs-selected').prevAll();
            var nextall = $('.tabs-selected').nextAll();        
            if(prevall.length>0){
                prevall.each(function(i,n){
                    var t=$('a:eq(0) span',$(n)).text();
                    if(t != '首页') {
                        $('#tabs').tabs('close',t);
                    }
                });
            }
            if(nextall.length>0) {
                nextall.each(function(i,n){
                    var t=$('a:eq(0) span',$(n)).text();
                    if(t != '首页') {
                        $('#tabs').tabs('close',t);
                    }
                });
            }
            return false;
        });
        /*关闭当前右侧的TAB*/
        $('#mm-tabcloseright').click(function(){
            var nextall = $('.tabs-selected').nextAll();
            if(nextall.length==0){
                alert('后边没有啦~~');
                return false;
            }
            nextall.each(function(i,n){
                var t=$('a:eq(0) span',$(n)).text();
                $('#tabs').tabs('close',t);
            });
            return false;
        });
        /*关闭当前左侧的TAB*/
        $('#mm-tabcloseleft').click(function(){
            var prevall = $('.tabs-selected').prevAll();
            if(prevall.length==0){
                alert('到头了，前边没有啦~~');
                return false;
            }
            prevall.each(function(i,n){
                var t=$('a:eq(0) span',$(n)).text();
                $('#tabs').tabs('close',t);
            });
            return false;
        });

        /*退出*/
        $("#mm-exit").click(function(){
            $('#mm').menu('hide');
        })
    }

    $(function() {
        tabCloseEven();
        $('.cs-navi-tab').click(function() {
            var $this = $(this);
            var href = $this.attr('src');
            var title = $this.text();
            addTab(title, href);
        });
    });
        
    function GetDateStr(AddDayCount) {
        var dd = new Date();
        dd.setDate(dd.getDate()+AddDayCount);/*获取AddDayCount天后的日期*/
        return dd.format('yyyy-MM-dd');
    }
    function logout() {
        $(document).sgConfirm({
            title:'提示',
            text: '确认退出系统?',
            cfn:function(r){
                if(r){
                    location.href = "logout";
                }
            }});
    }
    
   