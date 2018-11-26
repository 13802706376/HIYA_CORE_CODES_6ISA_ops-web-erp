<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>订单详情</title>
<meta name="decorator" content="default" />
<style type="text/css">
	.border-wrap {
		border: #dfdfdf solid 1px;
		border-radius: 5px;
	}
	
	.border-wrap:after {
		content: '';
		display: block;
		clear: both;
	}

	.margin-bottom-10 {
		margin-bottom: 10px;
	}

	.blockquote {
		margin-left: 10px;
		margin-top: 15px;
	}
	.blockquote>p{
	    float:left;
	    margin-top: 3px;
		margin-right: 10px;
	}
	.blockquote>input{
		margin-right:15px;
	}
	.spanspan {
		display: inline-block;
		width: 42%;
		border-width: 0px;
	}
	.nav li .comebacka:hover{
		background-color: transparent;
		border:1px solid transparent;
		text-decoration: underline;
	}
	.odercz{
		right: 10px;
	    top: 0px;
	    height: 38px;
	    line-height: 38px;
	    overflow: visible;
	}
	.padding10{ padding:0px 10px; }
	.dealTitle{
		font-size:14px;	
	}
	.dealItem{
		padding:0 10px;
		margin-bottom:10px;
	}
	.icon-btn{
		cursor: pointer;
	}
	.require{
		color:red;
		vertical-align: middle;
		font-size:14px;
		padding-right:3px;
	}
	.reason-box{
		position:relative
	}
	.mask{
		position:absolute;
		left:0;
		top:0;
		bottom:0;
		right:0;
		width:100%;
		height:100%
	}
	.dropdown-menu{
		border:none;
		padding:0px;
	}
</style>
<script type="text/javascript">
		$(document).ready(function() {
 			$("#e_splitOrderPromotion").live("click",function(){
 				WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false,minDate:'%y-%M-%d'});
 			});
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
			$('body').on('click', '.orderzf', function(event) {
				event.preventDefault();
				var id = '${info.id}'; //订单id
				var submit = function (v, h, f) {
				    if (v == 'ok') {
				    	 $.jBox.tip("正在处理，请稍等...", 'loading', {
							timeout : 0,
							persistent : true
						});
				    	 $.post("${ctx}/order/erpOrderOriginalInfo/cancel/" + id, {}, function(data) {
							if (data.result) {
								$.jBox.info("订单作废成功！");
								window.location.reload();
							} else {
								$.jBox.closeTip();
								$.jBox.info("订单作废失败！");
							}
						});
				    }
				    return true; //close
				};
				$.jBox.confirm("确定将此订单作废吗？", "提示", submit);
			});
			
			$('body').on('click', '.orderEnd', function(event) {
				event.preventDefault();
				var id = '${info.id}'; //订单id
				var submit = function (v, h, f) {
				    if (v == 'ok') {
				    	 $.jBox.tip("正在处理，请稍等...", 'loading', {
							timeout : 0,
							persistent : true
						});
				    	 $.post("${ctx}/order/erpOrderOriginalInfo/endOrder/" + id, {}, function(data) {
							if (data.result) {
								$.jBox.info("订单结束成功！");
								window.location.reload();
							} else {
								$.jBox.closeTip();
								$.jBox.info("订单结束失败！");
							}
						});
				    }
				    return true; //close
				};
				$.jBox.confirm("确定将此订单结束吗？", "提示", submit);
			});
			
			//切换聚引客版本
			$("#service").on("change",function(e){  
	            var data = $(this).val();				
	            $.post('${ctx}/order/erpOrderOriginalInfo/updateOrderVersion',{
					'orderId':'${info.id }',
					'orderVersion':data
				},function(data){
					if (data.result) {
						$.jBox.info("修改成功");
					} else {
						$.jBox.closeTip();
						$.jBox.info("修改失败");
					}
				},'json');
	        });
		});
		
		function finishProcInsId(procInsId){
			var submit = function (v, h, f) {
				if (v == 'ok') {
				    $.jBox.tip("正在修改，请稍等...", 'loading', {
						timeout : 0,
						persistent : true
					});
				    
				    $.post("${ctx}/workflow/finishProcInsId", {
				    	procInsId : procInsId
					}, function(data) {
						if (data.result) {
							$.jBox.info("中止任务成功");
							window.location.reload();
						} else {
							$.jBox.closeTip();
							$.jBox.info("无法中止此任务,请联系系统管理员");
						}
					});
				    return true;
			  }
			};
			$.jBox.confirm("当前正在执行的任务会强行中止,确定强行将此订单标记完成吗？", "提示", submit);
		}
		function showTask(procInsId){
			window.location.href="${ctx}/workflow/taskHistoicFlowByProcInsId?procInsId="+procInsId;
		}
		
		function deal(){	
			var dealOrderHtml='';		
			
			$('#serverTable>tbody>tr.dealTr').each(function(index,ele){
				//从表格筛选出 待处理的服务数量 大于0的服务 
				
				if( Number($(ele).find('.pendingNum').text()) > 0 && Number($(ele).attr('data-goodTypeid'))==5 ){  //只有聚引客的可以分单
					
					var goodId=$(ele).attr('data-id');
					var goodName=$(ele).find('.goodName').text();
					var pendingNum=Number($(ele).find('.pendingNum').text());		
					
					var selectArry=[];
															
					dealOrderHtml+='<div class="dealItem" style="margin-bottom: 10px;" data-goodid="'+goodId+'"  data-limit="'+pendingNum+'">';		
					dealOrderHtml+='<label class="control-label" style="display:inline-block; width:195px;"><input type="checkbox" name="dealCheck">'+goodName+'</label>';
					//dealOrderHtml+='<input class="orderInputNum" type="text" id="splitOrderNum_'+index+'" name="splitOrderNum_'+index+'"  data-goodid="'+goodId+'" data-limit="'+pendingNum+'" onblur="checkInput(this,\'check\')"/>';					
					dealOrderHtml+='<select class="orderInputNum" id="splitOrderNum_'+index+'" name="splitOrderNum_'+index+'" style="width:220px;">'					
					while(pendingNum--) {
						selectArry.push(pendingNum+1); //组装数组					
					}
					selectArry.reverse();
					
					for(var i=0;i<selectArry.length;i++){
						dealOrderHtml+='<option value="'+selectArry[i]+'">'+selectArry[i]+'</option>'	
					}
					
					dealOrderHtml+='</select>'					
					dealOrderHtml+='</div>';
					
				}
				
			});
			
			
			//没有可处理的项目时
			if(Number($('#pending-total').text())<=0){
				$.jBox.tip("没有可处理的项目。", 'error'); 						
				return false;				
			}
			
			var html = "<div style='padding:10px 10px 20px;'>"
				+"<p class='dealTitle'><b>选择套餐和数量</b></p>"
				//+"<label class='control-label' style='display:inline-block; width:195px;'>本次处理任务量：</label><input type='text' id='splitOrderNum' name='splitOrderNum' />"
				+dealOrderHtml
				/*+"<p class='dealTitle'><b>选择策划专家</b></p>"
				+"<label class='control-label' style='display:inline-block; width:195px;'>选择负责人（策划专家）：</label>"
				+"<select id='planningExpert' name='planningExpert' style='width:220px;'>"
				+"<c:forEach items='${userList }' var='user'>"
				+"		<option value='${user.id}'>${user.name }</option>"
				+"</c:forEach>"
				+"</select>"*/
				+"</div>";
				
				var loaded=function(a){
					//$(a).find("#planningExpert").select2();
					$(a).find('.orderInputNum').select2();
				}
				var submit = function (v, h, f) {
					
					var childItemArr=[];
					var regCheck=0;
					$(h).find('.dealItem').each(function(index,ele){
						if( $(ele).find('input[type="checkbox"]').prop('checked')){
							var $input=$(ele).find('.orderInputNum');	
							var selectVal=$input.select2("val");	
	
							/*if($($input).val()==''){
								$(this).find('input').focus();
								$.jBox.tip('请输入本次任务处理量。','error');
								regCheck++;
								return;
							}*/
							
							//checkInput($(ele).find('.orderInputNum'));//点击提交时再次检查可输入的值
	
							childItemArr.push({
								'goodId':$(ele).attr('data-goodid'),
								'num':selectVal
								//'planningExpert':f.planningExpert
							});
						}
					});
	
					/*if(regCheck>0){
						$.jBox.tip('请输入本次任务处理量。','error');
						return false;
					}*/
					if(childItemArr.length<=0  ){
						$.jBox.tip('请选择套餐和任务处理量。', 'error'); // 关闭设置 yourname 为焦点								
						return false;

					}
												
					$.jBox.tip("正在分单，请稍等...", 'loading', {
						timeout : 0,
						persistent : true
					});
				    
				    $.ajax({
				    		url:"${ctx}/order/erpOrderSplitInfo/multiSplit",
				    		data:JSON.stringify(childItemArr),
				    		success:function(data) {
									if (data.result) {
										$.jBox.info("分单成功");
										window.location.reload();
									} else {
										$.jBox.closeTip();
										$.jBox.info("分单失败,请确认待处理服务数量是否足够");
									}
								},
							type:'post',
							contentType:"application/json"});
				    return true;
				};
			$.jBox(html, { title: "本次任务处理量", width: 460, submit: submit,loaded:loaded});			
		}		
		
		function altertime(splitOrderId){
			//var html = "<div style='padding:10px;'><input type='text' id='e_splitOrderNum' name='e_splitOrderNum' value='" + nowNum + "'/></div><div style='padding:10px;'><input type='text' id='e_splitOrderPromotion' name='e_splitOrderPromotion' onclick='WdatePicker({dateFmt:"'yyyy-MM-dd HH:mm:ss'",isShowClear:false,mindate:"+'%y-%m-%d'+"});'/></div>";

			var html = "<div style='padding:10px;'>推广时间变更至：<input type='text' id='e_splitOrderPromotion'  class='input-medium Wdate' name='e_splitOrderPromotion'/></div>";
			//var html = "<div style='padding:10px;'><input type='text' id='e_splitOrderPromotion' name='e_splitOrderPromotion' onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/></div>";
			var submit = function (v, h, f) {
			    if (f.e_splitOrderPromotion == '') {
			        $.jBox.tip("请输入修改的推广时间。", 'error', { focusId: "e_splitOrderPromotion" }); // 关闭设置 yourname 为焦点
			        return false;
			    }
			    
			    
			    $.jBox.tip("正在修改，请稍等...", 'loading', {
					timeout : 0,
					persistent : true
				});
			    
			    $.post("${ctx}/order/erpOrderSplitInfo/altertime", {
					id : splitOrderId,
					times:f.e_splitOrderPromotion
				}, function(data) {
					if (data.result) {
						$.jBox.closeTip()
						$.jBox.tip("修改成功");
						setTimeout(function(){
							window.location.reload();
						} , 2000)
						
					} else {
						$.jBox.closeTip();
						$.jBox.info("修改失败,该订单还未与客户确认推广时间");
					}
				});
			    return true;
			};

			$.jBox(html, { title: "本次任务处理量", submit: submit });
		}

		function hurry(id) {
			var submit = function (v, h, f) {
			    if (v == 'ok') {
			    	 $.jBox.tip("正在修改，请稍等...", 'loading', {
						timeout : 0,
						persistent : true
					});
			    	 $.post("${ctx}/order/erpOrderSplitInfo/hurry", {
							id : id
					}, function(data) {
						if (data.result) {
							$.jBox.info("加急成功");
							window.location.reload();
						} else {
							$.jBox.closeTip();
							$.jBox.info("修改失败");
						}
					});
			    }
			    return true; //close
			};

			$.jBox.confirm("确定加急吗？", "提示", submit);
		}
		
		function procInsTasks(procInsId) {
			location.href = "${ctx}/workflow/procins/tasks?procInsId="+procInsId;
		}
		
		/*
		** 编辑服务名称
		*/
		function editServeName(splitOrderId,ele){	
			var dealOrderHtml='';	
			var $editList=$(ele).closest('td').find('input[type="hidden"]');
			var $serverTable=$('#serverTable').find('.dealTr');
			
			$($editList).each(function(index,e){
				
				var goodId=$(e).attr('originalgoodid'),//对应 聚引客条目的id
				nownum=$(e).attr('goodnum'), //当前的数量
				goodname=$(e).attr('goodname'); //服务名称
				var splitid=$(e).attr('splitgoodid'); //id
				$($serverTable).each(function(j,d){
					if($(d).attr('data-id')==goodId){
						var selectArry=[];
						
						var limitNum=parseInt($(e).attr('goodnum'))+parseInt($(d).find('.pendingNum').text()); //最大输入数量为当前的val值+待处理的值
											
						dealOrderHtml+='<div class="dealItem" data-goodid="'+splitid+'">';		
						dealOrderHtml+='<label class="control-label" style="display:inline-block; width:195px;">'+goodname+'</label>';
						//dealOrderHtml+='<input class="orderInputNum" type="text" id="splitOrderNum_'+index+'" name="splitOrderNum_'+index+'" data-goodid="'+splitid+'" data-limit="'+limitNum+'" onblur="checkInput(this)" value="'+nownum+'"/>';					
						dealOrderHtml+='<select class="orderInputNum" id="splitOrderNum_'+index+'" name="splitOrderNum_'+index+'" style="width:220px;">'
						while(limitNum--) {
							selectArry.push(limitNum+1); //组装数组					
						}
						selectArry.reverse();
						
						for(var i=0;i<selectArry.length;i++){
							if(nownum==selectArry[i]){
								dealOrderHtml+='<option value="'+selectArry[i]+'" selected>'+selectArry[i]+'</option>'
							}else{
								dealOrderHtml+='<option value="'+selectArry[i]+'">'+selectArry[i]+'</option>'	
							}
							
						}
						dealOrderHtml+='</select>'	
						dealOrderHtml+='</div>';
					}
					
				});
				
			})
			
			var html = "<div style='padding:10px 10px 20px;'>"
				+"<p class='dealTitle'><b>修改服务数量</b></p>"
				+dealOrderHtml			
				+"</div>";
			var loaded=function(a){
				$(a).find('.orderInputNum').select2();
			}
			var submit = function (v, h, f) {
				
				var childItemArr=[];
				$(h).find('.dealItem').each(function(index,ele){					   				
					var $input=$(ele).find('.orderInputNum');	
					var selectVal=$input.select2("val");
					/*if(!$($input).val()){
						$.jBox.tip("请输入本次任务处理量。", 'error', { focusId: $input.attr('id') }); // 关闭设置 yourname 为焦点								
						return false;							
					}*/
					childItemArr.push({
						'id':$(ele).attr('data-goodid'),
						'num':selectVal
					})
				});
				
				/*if(childItemArr.length<=0){
					$.jBox.tip("请输入任务处理量。", 'error'); // 关闭设置 yourname 为焦点								
					return false;
				}*/
				
				$.jBox.tip("正在修改，请稍等...", 'loading', {
					timeout : 0,
					persistent : true
				});
				
				$.ajax({
		    		url:"${ctx}/order/erpOrderSplitInfo/editMultiNum",
		    		data:JSON.stringify(childItemArr),			    		
					type:'post',
					contentType:"application/json",
					success:function(data) {
						if (data.result) {
							$.jBox.info("修改成功");
							window.location.reload();
						} else {
							$.jBox.closeTip();
							$.jBox.info("修改失败");
						}
					},
				});
			   				   
			    return true;
			};
			$.jBox(html, { title: "本次任务处理量", width: 460, submit: submit,loaded:loaded});
		
	
		}
		/*
		** 编辑策划专家 
		*/
		function editPlanningExpert(splitOrderId,eidtId){
			
			var html = "<div style='padding:10px 10px 20px;'>"				
				+"<label class='control-label' style='display:inline-block; width:195px;'>选择负责人（策划专家）：</label>"
				+"<select id='planningExpert' name='planningExpert' style='width:220px;'>"
				+"<c:forEach items='${userList }' var='user'>"
				+"		<option value='${user.id}'>${user.name }</option>"
				+"</c:forEach>"
				+"</select>"
				+"</div>";
				
				var loaded=function(a){
					var planning=$(a).find("#planningExpert").select2();
					planning.val(eidtId).trigger("change"); //设置选中
					planning.change();
				};
				var submit = function (v, h, f) {
										
					$.jBox.tip("操作正在进行中...", 'loading', {
						timeout : 0,
						persistent : true
					});
					
					var obj = {};
					obj.planningExpert = f.planningExpert;
					obj.splitId = splitOrderId;
					$.ajax({
			    		url:"${ctx}/order/erpOrderSplitInfo/modifyPlanningExpert",
			    		data:{
			    			"splitId":splitOrderId,
							"planningExpert":f.planningExpert
			    		},			    		
						type:'get',
						contentType:"application/json",
						success:function(data) {
							if (data.result) {
								$.jBox.info("修改成功");
								window.location.reload();
							} else {
								$.jBox.closeTip();
								$.jBox.info("修改失败");
							}
						},
					});
				   				   
				    return true;
				};
			$.jBox(html, { title: "本次任务处理量", width: 460, submit: submit,loaded:loaded});			
		}

		/*
		* 输入框失去焦点判断输入是否合法
		*/
		function checkInput(ele,checked){
			var inputId=$(ele).attr('id');
			var orderNum=Number($(ele).attr('data-limit'));
			var inputVal=Number($(ele).val());
			if(checked){
				var $checkbox=$(ele).closest('.dealItem').find('input[type="checkbox"]');				
				if( $checkbox && !$checkbox.is(':checked') ){
					return false;
				}
			}
			if (!/^[0-9]+$/.test(inputVal)) {
				$(ele).val('')
		    	$.jBox.tip("请输入合法数字。", 'error', { focusId: inputId }); // 关闭设置 yourname 为焦点			    
		    }
			if(inputVal>orderNum){
				$(ele).val('')
				$.jBox.tip("请输入一个小于待处理数量的值。", 'error', { focusId: inputId }); // 关闭设置 yourname 为焦点		
			}
			if(inputVal<=0){
				$.jBox.tip("请输入一个大于0的值。", 'error', { focusId: inputId }); // 关闭设置 yourname 为焦点	
			}			
		}
		
		/*
		*暂停
		*/
		function pauseTasks(splitOrderId){
			var html = "<div style='padding:10px 10px 20px;'>"	
				+'<p>只能基于商户原因（包括推迟推广、暂停推广、推广门店临时更换等问题），暂停该订单服务；暂停后的订单服务，任务处于冻结状态并不显示在任务栏目里，重启后回到冻结前的任务节点；可以在我的待生产库里直接重启服务</p>'
				+'<p class="dealTitle"><b><span class="require">*</span>暂停原因：</b></p>'		
				+'<div style="margin-bottom:10px;">'
				+'<select id="selectReason" name="selectReason" style="width:220px;">'
				+'<c:forEach items="${suspendReasonDicts }" var="reason">'
				+'		<option value="${reason.value}">${reason.label }</option>'
				+'</c:forEach>'
				+'</select>'
				+'</div>'
				+'<div>'
				+'<textarea id="detailReason" name="detailReason" rows="3" style="width:100%;box-sizing: border-box;" placeholder="输入具体暂停原因"></textarea>'
				+'</div>'
				+'<p class="dealTitle"><b><span class="require">*</span>下次与商户沟通是否进行服务的时间：</b></p>'
				+'<div>'
				+'<input id="nextDate" name="nextDate" value="" onclick="WdatePicker({dateFmt:\'yyyy-MM-dd HH:mm:ss\',isShowClear:false,readOnly:true});" type="text" class="required input-medium Wdate" maxlength="20" readonly="">'
				+'</div>'
				+'</div>';		
				var loaded=function(a){
					$(a).find("#selectReason").select2();
				};
				var submit = function (v, h, f) {
					if (v == true){
						//点击确定暂停				    	
						$.jBox.tip("操作正在进行中...", 'loading', {
							timeout : 0,
							persistent : true
						});				
						if(!f.selectReason){
							$.jBox.tip("请选择暂停理由。", 'error', { focusId: 'selectReason' });
							return false;
						}
						if(!f.nextDate){
							$.jBox.tip("请选择时间。", 'error', { focusId: 'nextDate' });
							return false;
						}
						var obj = {};
						obj.id = splitOrderId;
						obj.suspendReasonContent=f.detailReason; 				
						obj.suspendReason=f.selectReason;
						obj.nextContactTime=f.nextDate;												
						$.post('${ctx}/order/erpOrderSplitInfo/suspend',obj,function(data){						

							if (data.code=='0') {
								$.jBox.info("操作成功");
								window.location.reload();
							} else {
								$.jBox.closeTip();
								$.jBox.info(data.message);								
							}
						},'json');
						
				    }else{				    	
				    	return true;  //关闭窗口
				    }
			   				   				  
				};
				$.jBox(html, { title: "确定暂停该订单服务", width: 460,buttons: { '确定暂停': true, '取消': false}, submit: submit,loaded:loaded});
			
		}
		/*
		*重启服务
		*/
		function restartTasks(splitOrderId) {
			var submit = function(v, h, f) {
				if (v === 'ok') {
					$.jBox.tip("正在修改，请稍等...", 'loading', {
						timeout : 3000,
						persistent : true
					});
					var url = ctx+"/order/erpOrderSplitInfo/restart";
					$.post(url, {
						id: splitOrderId
					},
					function(data) {
						$.jBox.closeTip();
						if (data.code === '0') {
							$.jBox.tip('重启服务成功！');
							window.location.reload();
						} else {
							$.jBox.tip('处理失败，请稍后重试！');
						}
					});
					return true;
				}
			};
			top.$.jBox.confirm("是否重启服务？", "重启服务", submit,{buttons:{"确定重启":'ok',"取消，不重启":false}});
		}
		
		function restartTasksBtn(id,goodId,num){
			var splitGoodLists = [{goodId:goodId,num:num}];
			var submit = function(v, h, f) {
				if (v === 'ok') {
					$.jBox.tip("正在修改，请稍等...", 'loading', {
						timeout : 3000,
						persistent : true
					});
					
					$.ajax({
			    		url:"${ctx}/order/erpOrderSplitInfo/resetWorkFlow/"+id,
			    		data:JSON.stringify(splitGoodLists),
			    		success:function(res) {
							if(res.returnCode === 'success'){
								$.jBox.tip('重启流程成功！');
								window.location.reload();
							}else{
								$.jBox.tip(res.returnMessage || '重启流程失败！');
							}
						},
						type:'post',
						contentType:"application/json"
					});
					return true;
				}
			};
			top.$.jBox.confirm("是否确定重启服务流程，流程重启后，流程将重头开始，并且，需重新指派任务处理人", "重启流程", submit);
		}
		
		function redeploy(procInsId, orderId, splitId, type, serviceType) {
			var url = type === 'jyk' ? '${ctx}/order/erpOrderOriginalInfo/getTransferInfo' : '${ctx}/visit/flow/changeRoleList';
			var objMap = {
				'FMPS': {
					operationManager: '运营经理：',
					OperationAdviser: '运营顾问：',
					accountAdviser: '开户顾问：',
					materialAdviser: '物料顾问：'
				},
				'FMPS_BASIC': {
					operationManager: '运营经理：',
					OperationAdviser: '运营顾问：',
					accountAdviser: '开户顾问：',
					materialAdviser: '物料顾问：'
				},
				'JYK': {
					operationManager: '运营经理：',
					OperationAdviser: '运营顾问：',
					accountAdviser: '开户顾问：'
				},
				'INTO_PIECES': {
					operationManager: '运营经理：',
					OperationAdviser: '运营顾问：',
					accountAdviser: '开户顾问：'
				},
				'ZHCT_OLD': {
					operationManager: '运营经理：',
					OperationAdviser: '运营顾问：'
				},
				'MU': {
					operationManager: '运营经理：',
					OperationAdviser: '运营顾问：'
				},
				'VC': {
					operationManager: '运营经理：',
					OperationAdviser: '运营顾问：'
				}
			};
			var obj = type === 'jyk' ? {
				PlanningExpert:'策划专家：',
				designer:'设计师：',
				assignTextDesignPerson:'文案：',
				assignConsultant:'投放顾问：'
			} : objMap[serviceType];

			if (type !== 'jyk' && (!serviceType || typeof obj !== 'object')) {
				top.$.jBox.tip('该运营服务流程不能进行转派！');
				return false;
			}

			$.get(url, {
				procInsId:procInsId
			}, function(res){
				var newRes = {};
				if (type !== 'jyk' && !res.result) {
					top.$.jBox.tip('获取转派任务处理人失败！');
					return false;
				}
				if (type !== 'jyk' && Array.isArray(res.list)) {
					var list = res.list;
					var listMap = {
						operationManager: 'userOperationManagerList',
						accountAdviser: 'openAccountConsultantUserList',
						OperationAdviser: 'operationAdviserUserList',
						materialAdviser: 'materialConsultantUserList'
					};
					
					Object.keys(obj).forEach(function(el) {
						var roleOfList = list.filter(function(roles) {
							return roles.role === el;
						});

						if (roleOfList.length) {
							var roleOflistMapEl = res[listMap[el]].filter(function(roles) {
								return roleOfList[0].userId === roles.id;
							});
							if (roleOflistMapEl.length === 0 || !roleOflistMapEl) {
								roleOfList[0].id = roleOfList[0].userId;
								res[listMap[el]].push(roleOfList[0]);
							}
						}

						var uList = res[listMap[el]];
						newRes[el] = res[listMap[el]] || [];
						if (uList.length) {
							for (var i = 0, len = uList.length; i < len; i++) {
								for (var ii = 0, l = list.length; ii < l; ii++) {
									if (uList[i].id === list[ii].userId && list[ii].role === el) {
										newRes[el][i].userId =  list[ii].userId;
										break;
									}
								}
							}
						}
					});
					res.roleUsersMap = newRes;
				}

				setRedeploy(res, procInsId, orderId, splitId, type, obj, serviceType);
			});
		}
		
		function setRedeploy(res, procInsId, orderId, splitId, type, obj, serviceType) {
			var roleUsersMap = res.roleUsersMap, html = '';
			var changeRoleUser = function(userOrTeamObj) {
				$.post('${ctx}/delivery/flow/changeFlowUser', {
					flowUsers: JSON.stringify(userOrTeamObj)
				}/*{
					procInsId: procInsId,
					userOrTeamObj: JSON.stringify(userOrTeamObj)
				}*/, function(data, textStatus, xhr) {
					if (data.returnCode === 'success') {
						top.$.jBox.tip(data.message || '转派成功！');
						location.reload();
					} else {
						top.$.jBox.tip(data.message || '转派失败！');
					}
				});
			};
			var submit = function(v, h, f) {
				var userOrTeamObj = [];
				if (v === 'ok') {
					for (var key in roleUsersMap) {
						var val = $(h).find('#'+key).val();
						var params = type === 'jyk' ? {
							'flowId': procInsId,
							'flowUserId': '',
							'orderId': orderId,
							'splitId': splitId,
							'user': {id: ''}
						} : {
							'flowId': procInsId,
							'flowUserId': '',
							'orderId': orderId,
							'splitId': '',
							'user': {id: ''}
						};
						if (val) {
							params['user']['id'] = val.split(';')[0];
							params['flowUserId'] = val.split(';')[1];
							userOrTeamObj.push(params);

							/*userOrTeamObj.push({
					            userId: val.split(';')[0],
					            roleName: val.split(';')[1]
					        });*/
							/*top.$.jBox.tip('请选择'+obj[key].slice(0, -1));
							return false;*/
						}
					}
					if (userOrTeamObj.length === 0) {return true;}
					changeRoleUser(userOrTeamObj);
			    }
			    return true;   				  
			};

			// console.log(roleUsersMap);

			for (var key in roleUsersMap) {
				if (!Array.isArray(roleUsersMap[key]) || !obj[key]) {continue;}
				html += '<p style="margin-left: 80px;margin-top: 15px;">'+
						   '<label style="width: 70px;">'+obj[key]+'</label>'+
						   '<select id="'+key+'"><option value="">请选择</option>';
				
				roleUsersMap[key].forEach(function(item, inx) {
					if ((res[key] && res[key].userId === item.id) || item.userId === item.id) {
						html += '<option value="'+item.id+';'+key+'" selected="selected">'+item.name+'</option>';
					} else {
						html += '<option value="'+item.id+';'+key+'">'+item.name+'</option>';
					}
				});
				html += '</select></p>';
			}
			
			top.$.jBox(html, { title: "转派任务处理人", width: 460,buttons: { '确定': 'ok', '取消': true}, submit: submit});
		}

		/*
			交付服务流程标记完成
		 */
		function finishWorkFlow(procInsId) {
			var submit = function submit(v, h, f) {
				if (v !== 'ok') {return true;}
				top.$.jBox.tip("正在修改，请稍等...", 'loading', {
					timeout : 0,
					persistent : true
				});
				$.get('${ctx}/delivery/flow/finishWorkFlow', {
					procInsId: procInsId
				}, function(data) {
					if (data.returnCode === 'success') {
						top.$.jBox.tip(data.returnMessage || '标记完成成功！');
						location.reload();
					} else {
						top.$.jBox.tip(data.returnMessage || '标记完成失败！');
					}
				});
				return true;
			};

			top.$.jBox.confirm("当前正在执行的任务会强行中止,确定强行将此订单标记完成吗？", "提示", submit);
		}

		/*
			商戶運營流程重啟工作流
		 */
		function resetWorkFlow(procInsId) {
			top.$.jBox.confirm("是否确定重启服务流程，流程重启后，流程将重头开始，并且，需重新指派任务处理人", "重启流程", submit);
			function submit(v, h, f) {
				if (v !== 'ok') {return true;}
				top.$.jBox.tip("正在修改，请稍等...", 'loading', {
					timeout : 3000,
					persistent : true
				});
				$.get('${ctx}/delivery/flow/resetWorkFlow', {
					procInsId: procInsId
				}, function(data) {
					if (data.returnCode === 'success') {
						top.$.jBox.tip(data.returnMessage || '重启服务流程成功！');
						location.reload();
					} else {
						top.$.jBox.tip(data.returnMessage || '重启服务流程失败！');
					}
				});
				return true;
			}
		}

		function restartTasksOld(splitOrderId){
			var html = "<div style='padding:10px 10px 20px;'>"	
			
				+'<div class="reason-box">'
				+'<p class="dealTitle"><b>与商户沟通启动服务</b></p>'
				+'<p class="dealTitle"><b><span class="require">*</span>暂停原因：</b></p>'		
				+'<div style="margin-bottom:10px;">'
				+'<select id="selectReason" name="selectReason" style="width:220px;">'
				+'<c:forEach items="${suspendReasonDicts }" var="reason">'
				+'		<option value="${reason.value}">${reason.label }</option>'
				+'</c:forEach>'
				+'</select>'
				+'</div>'
				+'<div>'
				+'<textarea id="detailReason" name="detailReason" rows="3" style="width:100%;box-sizing: border-box;" placeholder="输入具体暂停原因"></textarea>'
				+'</div>'
				+'<p class="dealTitle"><b><span class="require">*</span>下次与商户沟通是否进行服务的时间：</b></p>'
				+'<div>'
				+'<input id="nextDate" name="nextDate" value="" onclick="WdatePicker({dateFmt:\'yyyy-MM-dd HH:mm:ss\',isShowClear:false,readOnly:true});" type="text" class="required input-medium Wdate" maxlength="20" readonly="">'
				+'</div>'
				+'<div class="mask"></div>'
				+'</div>'
				
				+'<p class="dealTitle"><b><span class="require">*</span>是否重启服务：</b></p>'
				+'<div style="margin-left:-20px;">'
				+'<label class="checkbox inline"><input type="radio" id="isRestart2" name="isRestart" value="否">否</label>'
				+'<label class="checkbox inline"><input type="radio" id="isRestart1" name="isRestart" value="是" checked>是</label>'				
				+'</div>'
				+'</div>';
				
				var loaded=function (a){
					//弹出框初始化执行
					//暂停内容回显
					var loading=$.jBox.tip("数据加载中...", 'loading');
	
					$.post('${ctx}/order/erpOrderSplitInfo/getSuspendData/'+splitOrderId,{},function(data){						
						$.jBox.closeTip(loading);
						if(data.code=='0'){
							if(data.attach){
								var selectReason=$(a).find("#selectReason").select2();
								selectReason.val(data.attach.suspendReason).trigger("change"); //设置选中
								selectReason.change();
								$(a).find("#detailReason").val(data.attach.suspendReasonContent);
								$(a).find("#nextDate").val(data.attach.nextContactTime);
							}							
						}
					},'json');
														
					$(a).on('click','input[name="isRestart"]',(function(){
					
						var v=$(this).val();						
						if(v=='否'){ //点击了否，去掉表单中的disabled
							//$(a).find('#selectReason,#detailReason,#nextDate').attr("disabled",false);
							$(a).find('.mask').hide();
						}else{
							//$(a).find('#selectReason,#detailReason,#nextDate').attr("disabled",true);
							$(a).find('.mask').show();
						}
					}))
				};
				var submit = function (v, h, f) {
					if (v == true){
						//点击确定暂停		
						$.jBox.tip("操作正在进行中...", 'loading', {
							timeout : 0,
							persistent : true
						});

						if(!f.selectReason){
							$.jBox.tip("请选择暂停理由。", 'error', { focusId: 'selectReason' });
							return false;
						}
						if(!f.nextDate){
							$.jBox.tip("请选择时间。", 'error', { focusId: 'nextDate' });
							return false;
						}
						var isRestart=$(h).find('input[name="isRestart"]:checked').val();
						
						var obj = {};
						var url='';
						if(isRestart=='否'){
							obj.id = splitOrderId;
							obj.suspendReasonContent=f.detailReason;
							obj.suspendReason=f.selectReason;
							obj.nextContactTime=f.nextDate;
							url='${ctx}/order/erpOrderSplitInfo/updateSuspendData'
						}else{
							obj.id = splitOrderId;
							url='${ctx}/order/erpOrderSplitInfo/restart'
						}	
					
						$.get(url,obj,function(data){						

							if (data.code=='0') {
								$.jBox.info("操作成功");
								window.location.reload();
							} else {
								$.jBox.closeTip();
								$.jBox.info("操作成功");
								
							}
						},'json');
				    }else{				    	
				    	 return true;  //关闭窗口
				    }
   				   				  
				};				
				$.jBox(html, { title: "已暂停订单服务，与商户沟通启动服务", width: 460,buttons: { '确定完成': true, '取消': false}, submit: submit,loaded:loaded});		
			
		}
 	</script>
</head>
<body>
	<div class="act-top positionrelative">
		<ul class="nav nav-tabs">
			<li><a href="javascript:history.back()" class="comebacka">返回</a></li>
			<li class="active">
				<a href="${ctx}/order/erpOrderOriginalInfo/form?id=${info.id}">订单<shiro:hasPermission
						name="order:info:edit">${not empty info.id?'修改':'添加'}</shiro:hasPermission>
					<shiro:lacksPermission name="order:info:edit">查看</shiro:lacksPermission>
				</a>
			</li>
		</ul>
		<shiro:hasPermission name="order:detail:more:mod">
			<div class="odercz btn-group positionrabsolute">
				<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
					<i class="icon-align-justify"></i>
				</a>
				<!-- 服务商/分公司没有作废订单和结束订单的操作权限； -->
				<c:if test="${isAgent eq false }">
					<ul class="dropdown-menu" style="left: -120px;">
						<shiro:hasPermission name="order:erpOrderOriginalInfo:cancelOrder">
							<li>
								<c:if test="${info.cancel == 0 }"><a href="javascript:;" class="orderzf">订单作废</a></c:if>
								<c:if test="${info.cancel == 1 }"><a>订单已作废</a></c:if>
							</li>
						</shiro:hasPermission>
						<shiro:hasPermission name="order:erpOrderOriginalInfo:endOrder">
							<li>
								<c:if test="${info.cancel == 0 }"><a href="javascript:;"
																	 class="orderEnd">订单结束</a></c:if>
								<c:if test="${info.cancel == 2 }"><a>订单已结束</a></c:if>
							</li>
						</shiro:hasPermission>
					</ul>
				</c:if>
			</div>
		</shiro:hasPermission>
	</div>
	<div class="padding10">
		<div class="border-wrap margin-bottom-10">
			<blockquote class="blockquote clearfix">
				<p>订单基本信息</p>
			</blockquote>
			<form:form id="inputForm" modelAttribute="info" action="${ctx}/order/info/save" method="post" class="form-horizontal">
				<%--<form:hidden path="id" />--%>
				<input type="hidden" name="id" id="id" value="${info.id}">
				<sys:message content="${message}" />
				<div class="control-group spanspan">
					<label class="control-label">订单号：</label>
					<div class="controls">
						<label>${info.orderNumber }</label>
					</div>
				</div>
				<div class="control-group spanspan">
					<label class="control-label">购买时间：</label>
					<div class="controls">
						<label><fmt:formatDate value="${info.buyDate}"
								pattern="yyyy-MM-dd HH:mm:ss" /></label>
					</div>
				</div>
				<div class="control-group spanspan">
					<label class="control-label">录单时间：</label>
					<div class="controls">
						<label><fmt:formatDate value="${info.createDate}"
								pattern="yyyy-MM-dd HH:mm:ss" /></label>
					</div>
				</div>
				<div class="control-group spanspan">
					<label class="control-label">商户名称：</label>
					<div class="controls">
						<label>${info.shopName }</label>
					</div>
				</div>
				<div class="control-group spanspan">
					<label class="control-label">联系人：</label>
					<div class="controls">
						<label>${info.contactName }</label>
					</div>
				</div>
				<div class="control-group spanspan">
					<label class="control-label">联系电话：</label>
					<div class="controls">
						<label>${info.contactNumber }</label>
					</div>
				</div>
				<div class="control-group spanspan">
					<label class="control-label">销售人：</label>
					<div class="controls">
						<label>${info.salePerson }</label>
					</div>
				</div>
				<div class="control-group spanspan">
					<label class="control-label">服务商名称：</label>
					<div class="controls">
						<label>${info.agentName }</label>
					</div>
				</div>
				<div class="control-group spanspan">
					<label class="control-label">推广联系人：</label>
					<div class="controls">
						<label>${info.promoteContact }</label>
					</div>
				</div>			
				<div class="control-group spanspan">
					<label class="control-label">推广人联系方式：</label>
					<div class="controls">
						<label>${info.promotePhone }</label>
					</div>
				</div>
				<div class="control-group spanspan">
					<label class="control-label">订单类别：</label>
					<div class="controls">
						<label>${fns:getDictLabel(info.orderType, "orderType", "未知")}</label>
					</div>
				</div>
			</form:form>
		</div>
		<div class="border-wrap margin-bottom-10">
			<blockquote class="blockquote clearfix">
				<p>订单备注</p>
			</blockquote>
			<div style="margin-left:15px;margin-right:10px;">
				<p>${info.remark }</p>
			</div>
		</div>
		<div class="border-wrap margin-bottom-10">
			<blockquote class="blockquote clearfix">
				<p>购买的商品</p>
				
			</blockquote>
			<table id="buyServerTable" class="table table-striped table-condensed">
				<!--  table-bordered table-condensed -->
				<thead>
					<tr>
						<th></th>
						<th>服务名称</th>
						<th>服务类型</th>
						<th>价格</th>
						<th>数量</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${goods}" var="erpOrderOriginalGood">
						<tr>
							<td></td>
							<td>${erpOrderOriginalGood.goodName}</td>
							<td>${erpOrderOriginalGood.goodTypeName}</td>
							<td>${erpOrderOriginalGood.realPrice / 100.0}</td>
							<td>${erpOrderOriginalGood.num}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>

		<shiro:hasPermission name="order:detail:jykService:mod">
			<div class="border-wrap margin-bottom-10">
				<blockquote class="blockquote clearfix">
					<p>聚引客服务处理</p>
					<c:if test="${info.orderStatus != -1}">
						<label>
							聚引客套餐版本：

							<shiro:hasPermission name="order:detail:jykService:updateOrderVersion">
								<!-- 服务商/分公司没有聚引客服务处理模块的操作权限 ； -->
								<select id="service" name="service"
										<c:if test="${isAgent eq true}">disabled="disabled"</c:if> style="width:220px;">
									<c:forEach items="${versiondicts}" var="versiondict" varStatus="status">
										<option
												<c:if test="${info.orderVersion == versiondict.value }">selected</c:if>
												value="${versiondict.value }">${versiondict.value }</option>
									</c:forEach>
								</select>
							</shiro:hasPermission>
							<shiro:lacksPermission name="order:detail:jykService:updateOrderVersion">
								<select id="service" name="service" disabled style="width:220px;">
									<c:forEach items="${versiondicts}" var="versiondict" varStatus="status">
										<option
												<c:if test="${info.orderVersion == versiondict.value }">selected</c:if>
												value="${versiondict.value }">${versiondict.value }</option>
									</c:forEach>
								</select>
							</shiro:lacksPermission>

						</label>
						<shiro:hasPermission name="order:detail:jykService:multiSplit">
							<!-- 服务商/分公司没有聚引客服务处理模块的操作权限 ； -->
							<input
									class="btn btn-primary pull-right" type="button" value="聚引客业务处理"
									<c:if test="${isAgent eq true}">disabled="disabled"</c:if>
									onclick="deal()">
						</shiro:hasPermission>
					</c:if>
				</blockquote>
				<table id="serverTable" class="table table-striped table-condensed">
					<!--  table-bordered table-condensed -->
					<thead>
					<tr>
						<th></th>
						<th>套餐名称</th>
						<th>数量</th>
						<th>待处理</th>
						<th>处理中</th>
						<th>处理完成</th>
					</tr>
					</thead>
					<tbody>
					<c:set var="splitgoodpendingNum" scope="session" value="0"/>
					<c:set var="splitgoodprocessNum" scope="session" value="0"/>
					<c:set var="splitgoodfinishNum" scope="session" value="0"/>
					<c:forEach items="${goods}" var="erpOrderOriginalGood" varStatus="status">
						<!-- 只显示聚引客的产品 -->
						<c:if test="${erpOrderOriginalGood.goodTypeId == 5}">
							<tr class="dealTr" data-id="${erpOrderOriginalGood.id}"
								data-goodTypeid="${erpOrderOriginalGood.goodTypeId}">
								<td></td>
								<td class="goodName">${erpOrderOriginalGood.goodName}</td>
								<td>${erpOrderOriginalGood.num}</td>
								<td class="pendingNum">${erpOrderOriginalGood.pendingNum}</td>
								<td>${erpOrderOriginalGood.processNum}</td>
								<td>${erpOrderOriginalGood.finishNum}</td>
								<c:set var="splitgoodpendingNum" scope="session"
									   value="${erpOrderOriginalGood.pendingNum+splitgoodpendingNum }"/>
								<c:set var="splitgoodprocessNum" scope="session"
									   value="${erpOrderOriginalGood.processNum+splitgoodprocessNum }"/>
								<c:set var="splitgoodfinishNum" scope="session"
									   value="${erpOrderOriginalGood.finishNum+splitgoodfinishNum }"/>
							</tr>
						</c:if>
					</c:forEach>
					<tr>
						<td></td>
						<td>合计</td>
						<td><c:out
								value="${splitgoodpendingNum + splitgoodprocessNum + splitgoodfinishNum}"></c:out></td>
						<td id="pending-total">${splitgoodpendingNum}</td>
						<td>${splitgoodprocessNum}</td>
						<td>${splitgoodfinishNum}</td>
					</tr>
					</tbody>
				</table>
			</div>
		</shiro:hasPermission>

		<shiro:hasPermission name="order:detail:operationService:mod">
			<div class="border-wrap margin-bottom-10">
				<blockquote class="blockquote clearfix">
					<p>商户运营服务处理</p>
				</blockquote>
				<table id="serverTable" class="table table-striped table-condensed">
					<thead>
					<tr>
						<td></td>
						<th>商品名称</th>
						<th>服务项目</th>
						<th>数量</th>
						<th>待处理</th>
						<th>处理中</th>
						<th>处理完成</th>
						<th>服务期限（月）</th>
						<th>到期时间</th>
						<th>有效性</th>
					</tr>
					</thead>
					<tbody>
					<c:set var="splitServiceNum" scope="session" value="0"/>
					<c:set var="splitPendingNum" scope="session" value="0"/>
					<c:set var="splitProcessNum" scope="session" value="0"/>
					<c:set var="splitFinishNum" scope="session" value="0"/>
					<c:forEach items="${orderGoodServiceInfoList}" var="orderGoodServiceInfo">
						<tr class="dealTr">
							<td></td>
							<td>${orderGoodServiceInfo.goodName}</td>
							<td>${orderGoodServiceInfo.serviceItemName}</td>
							<td>${orderGoodServiceInfo.serviceNum}</td>
							<td>${orderGoodServiceInfo.pendingNum}</td>
							<td>${orderGoodServiceInfo.processNum}</td>
							<td>${orderGoodServiceInfo.finishNum}</td>
							<td>${orderGoodServiceInfo.serviceTerm}</td>
							<td><fmt:formatDate value="${orderGoodServiceInfo.expirationTime}" type="both"
												pattern="yyyy-MM-dd HH:mm:ss"/></td>
							<td>
								<c:if test="${orderGoodServiceInfo.isValid == true}">正常</c:if>
								<c:if test="${orderGoodServiceInfo.isValid == false}">过期</c:if>
							</td>
							<c:set var="splitServiceNum" scope="session"
								   value="${orderGoodServiceInfo.serviceNum+splitServiceNum }"/>
							<c:set var="splitPendingNum" scope="session"
								   value="${orderGoodServiceInfo.pendingNum+splitPendingNum }"/>
							<c:set var="splitProcessNum" scope="session"
								   value="${orderGoodServiceInfo.processNum+splitProcessNum }"/>
							<c:set var="splitFinishNum" scope="session"
								   value="${orderGoodServiceInfo.finishNum+splitFinishNum }"/>
						</tr>
					</c:forEach>
					<tr>
						<td></td>
						<td>合计</td>
						<td></td>
						<td>${splitServiceNum}</td>
						<td>${splitPendingNum}</td>
						<td>${splitProcessNum}</td>
						<td>${splitFinishNum}</td>
						<td colspan="3"></td>
					</tr>
					</tbody>
				</table>
			</div>
		</shiro:hasPermission>

		<shiro:hasPermission name="order:detail:jykFlow:mod">
			<!-- 服务商/分公司没有聚引客生产流程模块的浏览和操作权限； -->
			<c:if test="${isAgent eq false}">
				<div class="border-wrap margin-bottom-10">
					<blockquote class="blockquote clearfix">
						<p>聚引客生产流程</p>
					</blockquote>
					<table id="contentTable" class="table table-striped table-condensed">
						<thead>
						<tr>
							<th></th>
							<th>分单号</th>
							<th>服务名称*数量</th>
							<!-- <th>服务类型</th>
							<th>数量</th> -->
							<th>投放通道</th>
							<th>策划专家</th>
							<!-- <th>分单人</th> -->
							<th>流程启动时间</th>
							<th>状态</th>
							<th width="550">操作</th>
						</tr>
						</thead>
						<tbody>
						<c:forEach items="${splits}" var="erpOrderSplitInfo">
							<tr>
								<td></td>
								<td>${erpOrderSplitInfo.splitId}</td>
								<td>
									<c:forEach items="${erpOrderSplitInfo.erpOrderSplitGoods}" var="erpOrderSplitGood">
										<input type="hidden" name="${erpOrderSplitGood.id }"
											   splitgoodid="${erpOrderSplitGood.id }" splitid="${erpOrderSplitInfo.id}"
											   originalgoodid="${erpOrderSplitGood.originalGoodId }"
											   goodnum="${erpOrderSplitGood.num }"
											   goodname="${erpOrderSplitGood.goodName }">${erpOrderSplitGood.goodName}*${erpOrderSplitGood.num}&nbsp;
									</c:forEach>
									<shiro:hasPermission name="order:erpOrderSplitInfo:editMultiNum">
										<c:if test="${erpOrderSplitInfo.status == 0}">
											<i class="icon-pencil icon-btn"
											   onclick="editServeName('${erpOrderSplitInfo.id}',this)"></i>
										</c:if>
									</shiro:hasPermission>
								</td>
									<%-- <td>${erpOrderSplitInfo.goodTypeName}</td>
                                    <td>${erpOrderSplitInfo.num}</td> --%>
								<td>${erpOrderSplitInfo.channel}</td>
								<td>${erpOrderSplitInfo.planningName}
									<shiro:hasPermission name="order:erpOrderSplitInfo:modifyPlanningExpert">
										<c:if test="${erpOrderSplitInfo.status == 0}">
											<i class="icon-pencil icon-btn planningExpert-btn"
											   data-val="${erpOrderSplitGood.id }"
											   onclick="editPlanningExpert('${erpOrderSplitInfo.id}','${erpOrderSplitInfo.planningExpert}')"></i>
										</c:if>
									</shiro:hasPermission>
								</td>
									<%-- <td>${erpOrderSplitInfo.createBy.name} </td> --%>
								<td><fmt:formatDate value="${erpOrderSplitInfo.createDate}" type="both"
													pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td>
									<!-- 订单状态为处理中 -->
									<c:if test="${erpOrderSplitInfo.status == 0 }">
										<c:choose>
											<c:when test="${erpOrderSplitInfo.suspendFlag == 'Y' }">
												<c:if test="${info.cancel == 1 }"><span>已暂停</span></c:if>
												<c:if test="${info.cancel == 0 }"><a
														href="${ctx }/workflow/toTaskHistoicFlowByProcInsId?procInsId=${erpOrderSplitInfo.act.procInsId}">已暂停</a></c:if>
											</c:when>
											<c:otherwise>
												<c:if test="${info.cancel == 1 }"><span>处理中</span></c:if>
												<c:if test="${info.cancel == 0 }"><a
														href="${ctx }/workflow/toTaskHistoicFlowByProcInsId?procInsId=${erpOrderSplitInfo.act.procInsId}">处理中</a></c:if>
											</c:otherwise>
										</c:choose>
									</c:if>
									<!-- 订单状态为已完成 -->
									<c:if test="${erpOrderSplitInfo.status == 1}">
										<c:if test="${info.cancel == 1 }"><span>处理完成</span></c:if>
										<c:if test="${info.cancel == 0 }"><a
												href="${ctx }/workflow/toTaskHistoicFlowByProcInsId?procInsId=${erpOrderSplitInfo.act.procInsId}">处理完成</a></c:if>
									</c:if>
									<!-- 订单状态为已取消 -->
									<c:if test="${erpOrderSplitInfo.status == 2}">
										<c:if test="${info.cancel == 1 }"><span>已取消</span></c:if>
										<c:if test="${info.cancel == 0 }"><a
												href="${ctx }/workflow/toTaskHistoicFlowByProcInsId?procInsId=${erpOrderSplitInfo.act.procInsId}">已取消</a></c:if>
									</c:if>
								</td>
								<td>
									<!-- 作废订单(cancel 是否是作废订单，0：否，1：是), 不允许操作。erpOrderSplitInfo.status=0处理中 1已完成 2已取消 -->
									<c:if test="${erpOrderSplitInfo.status == 0 }">
										<c:choose>
											<c:when test="${erpOrderSplitInfo.suspendFlag == 'Y' }">
												<shiro:hasPermission name="order:detail:jykFlow:suspendOrRestart">
													<button class="btn btn-primary"
															onclick="restartTasks('${erpOrderSplitInfo.id}')"
															<c:if test="${info.cancel == 1 }">disabled</c:if>>重启服务
													</button>
												</shiro:hasPermission>
											</c:when>
											<c:otherwise>
												<c:if test="${erpOrderSplitInfo.hurryFlag == 1}">
													<button class="btn btn-danger" disabled>已加急</button>
												</c:if>
												<c:if test="${erpOrderSplitInfo.hurryFlag != 1}">
													<shiro:hasPermission name="order:erpOrderSplitInfo:hurry">
														<button class="btn btn-danger"
																onclick="hurry('${erpOrderSplitInfo.id }')"
																<c:if test="${info.cancel == 1 }">disabled</c:if>>加急
														</button>
													</shiro:hasPermission>
												</c:if>
												<shiro:hasPermission name="order:erpOrderSplitInfo:altertime">
													<input class="btn btn-success" type="button" value="变更推广时间"
														   onclick="altertime('${erpOrderSplitInfo.id }')"
														   <c:if test="${info.cancel == 1 }">disabled</c:if>>
												</shiro:hasPermission>
												<shiro:hasPermission name="workflow:finishProcInsId">
													<button class="btn btn-warning"
															onclick="finishProcInsId('${erpOrderSplitInfo.act.procInsId}')"
															<c:if test="${info.cancel == 1 }">disabled</c:if>>标记完成
													</button>
												</shiro:hasPermission>
												<shiro:hasPermission name="workflow:jumpTask">
													<%--<button class="btn btn-primary" onclick="procInsTasks('${erpOrderSplitInfo.procInsId}')" <c:if test="${info.cancel == 1 }">disabled</c:if>>跳转任务</button>--%>
												</shiro:hasPermission>
												<shiro:hasPermission name="order:detail:jykFlow:suspendOrRestart">
													<button class="btn btn-danger"
															onclick="pauseTasks('${erpOrderSplitInfo.id}')"
															<c:if test="${info.cancel == 1 }">disabled</c:if>>暂停服务
													</button>
												</shiro:hasPermission>
												<shiro:hasPermission name="order:detail:jykFlow:restartFlow">
													<c:if test="${erpOrderSplitInfo.status == 0 }">
														<button class="btn btn-primary"
																onclick="restartTasksBtn('${erpOrderSplitInfo.act.procInsId}','${erpOrderSplitInfo.originalGoodId}','${erpOrderSplitInfo.num}')">
															重启流程
														</button>
													</c:if>
												</shiro:hasPermission>
												<shiro:hasPermission name="order:detail:jykFlow:transferAssignee">
													<button class="btn btn-primary"
															onclick="redeploy('${erpOrderSplitInfo.act.procInsId}', '${erpOrderSplitInfo.orderId}', '${erpOrderSplitInfo.id}', 'jyk')">
														转派
													</button>
												</shiro:hasPermission>
											</c:otherwise>
										</c:choose>
									</c:if>
								</td>
							</tr>
						</c:forEach>
						</tbody>
					</table>
				</div>
			</c:if>
		</shiro:hasPermission>

		<shiro:hasPermission name="order:detail:operationServiceFlow:mod">
			<!-- 服务商/分公司没有聚引客生产流程模块的浏览和操作权限； -->
			<c:if test="${isAgent eq false}">
				<div class="border-wrap margin-bottom-10">
					<blockquote class="blockquote clearfix">
						<p>商户运营服务流程</p>
					</blockquote>
					<table id="contentTableShop" class="table table-striped table-condensed">
						<thead>
						<tr>
							<th></th>
								<%--<th>分单号</th>--%>
							<th>服务类型</th>
							<th>服务项目</th>
							<th>运营顾问</th>
							<th>流程启动时间</th>
							<th>状态</th>
							<th width="550">操作</th>
						</tr>
						</thead>
						<tbody>
							<%--${deliveryServiceInfoList} --%>
						<c:forEach items="${deliveryServiceInfoList}" var="goodsSplit">
							<tr>
								<td></td>
								<td>${goodsSplit.goodType}</td>
								<td>
									<c:set value="${fn:split(goodsSplit.serviceItemName, ',')}" var="serviceItemName" />
									<c:forEach items="${serviceItemName}" var="name">
										${name}<br />
									</c:forEach>
								</td>
								<td>
									<c:if test="${null!=goodsSplit.operationAdviser}">${goodsSplit.operationAdviser}</c:if>
									<c:if test="${null==goodsSplit.operationAdviser}">/</c:if>
								</td>
								<td><fmt:formatDate value="${goodsSplit.startDate}" type="both"
													pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td>
									<a href="${ctx}/workflow/toTaskHistoicFlowByProcInsId?procInsId=${goodsSplit.procInsId}">
										<c:choose>
											<c:when test="${goodsSplit.status == 2}">
												已取消
											</c:when>
											<c:otherwise>
												<c:if test="${null!=goodsSplit.flowEndTime}">已完成</c:if>
												<c:if test="${null==goodsSplit.flowEndTime}">处理中</c:if>
											</c:otherwise>
										</c:choose>
									</a>
									<span class="hide">${goodsSplit.flowEndTime}</span>
								</td>
								<td>
									<c:if test="${null==goodsSplit.flowEndTime and goodsSplit.status != 2}">
										<%--状态为“已完成”时，隐藏“操作”栏下所有操作按钮--%>
										<shiro:hasPermission name="order:detail:operationServiceFlow:markComplete">
											<button class="btn btn-warning"
													onclick="finishWorkFlow('${goodsSplit.procInsId}')">标记完成
											</button>
										</shiro:hasPermission>
										<shiro:hasPermission name="order:detail:operationServiceFlow:restartFlow">
											<button class="btn btn-primary"
													onclick="resetWorkFlow('${goodsSplit.procInsId}')">重启流程
											</button>
										</shiro:hasPermission>
										<shiro:hasPermission name="order:detail:operationServiceFlow:transferAssignee">
											<button class="btn btn-primary"
													onclick="redeploy('${goodsSplit.procInsId}', '${goodsSplit.orderId}', 'splitId', 'shop', '${goodsSplit.serviceType}')">转派
											</button>
										</shiro:hasPermission>
									</c:if>
								</td>
							</tr>
						</c:forEach>
						</tbody>
					</table>
				</div>
			</c:if>
		</shiro:hasPermission>

	</div>

	<script src="${ctxStatic}/common/act.js?v=${staticVersion}" type="text/javascript"></script>
</body>
</html>
