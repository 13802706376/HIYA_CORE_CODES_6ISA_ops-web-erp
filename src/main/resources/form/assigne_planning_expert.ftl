<script type="text/javascript">
	function submit_${taskId!''}submitForm1002(isLocation){
		
		var planningExpert = $("#${taskId!''}planningExpert1002").val();
	
		if('' == planningExpert){
				$.jBox.tip("请选择策划专家！", 'error');
				return;
		}
	
		var submit = function (v, h, f) {
				if (v == 'ok') {
						$.jBox.tip("正在修改，请稍等...", 'loading', {
						timeout : 3000,
						persistent : true
				});
				$.post("../jyk/flow/planningexpert/assigne_planning_expert", {
						splitId:$("#${taskId!''}splitId1002").val(),
						taskId:$("#${taskId!''}taskId1002").val(),
						planningExpert:$("#${taskId!''}planningExpert1002").val()
					}, 
				function(data) {
					if (data.result) {
						if(isLocation==1){
							updatePage();
						}else{
							window.location.reload();
						}
					} else {
						$.jBox.closeTip();
						$.jBox.info(data.message);
					}
				});
			}
			return true; 
		};
		$.jBox.confirm("确定完成订单信息确认，指派策划专家吗？", "提示", submit);
	}
</script>
<style>
	.act-rs-title{
		position: relative;
	}
	.listSubmit {
	    position: absolute;
	    top: 0;
	    right: 10px;
	}
</style>
<input type='hidden' id='${taskId!''}taskId1002' name='taskId' value="${taskId!''}"/>
<input type='hidden' id='${taskId!''}procInsId1002' name='procInsId'  value="${procInsId!''}"/>
<input type='hidden' id='${taskId!''}splitId1002' name='splitId'  value="${splitId!''}"/>
<div class="act-rs-item  ${detailType!''}  ${followTaskDetailWrap!''}">
	<div class="act-rs-left floatleft">
		<div class="act-rs-title padding15">
			<h3><a href="../workflow/taskDetail?taskId=${taskId!''}&followTaskDetailWrap=${followTaskDetailWrap!''}&detailType=${detailType!''}">${taskName!''}</a></h3>
			<div class="listSubmit">
				<button  onclick="submit_${taskId!''}submitForm1002()" type="button" class="makesrue btn">确定完成</button>
				<a href="javascript:;" class="select-user open-select-user" style="float: left;" onclick="syncSelect('${taskId!''}', '${taskUser!''}','${taskUserId!''}')"></a>
				<a href="javascript:;" class="select-user open-select-user" style="float: left;" onclick="syncSelect('${taskId!''}', '${taskUser!''}','${taskUserId!''}')"></a>
			</div>
				<div class="taskPrincipal"><span class="taskPrincipalName">${taskUser!''}</span></div>
		</div>
		<div class="act-rs-form form-horizontal padding15 container-fluid">
			<div class="row-fluid clearfix">
				<div class="span6">
					<div class="rs-label">
						<span class="rs-label-wrapper" style="display: inline-block;">
							<span class="subTask">订单号：</span>
							<#if (orderInfo.orderNumber)??>${orderInfo.orderNumber}</#if>
						</span>
					</div>
					<div class="rs-label">
						<span class="rs-label-wrapper" style="display: inline-block;">
							<span class="subTask">购买时间：</span>
							<#if (orderInfo.buyDate)??>${orderInfo.buyDate?string("yyyy-MM-dd HH:mm:ss")}</#if>
						</span>
					</div>
					<div class="rs-label">
						<span class="rs-label-wrapper" style="display: inline-block;">
							<span class="subTask">商户名称：</span>
							<#if (orderInfo.shopName)??>${orderInfo.shopName}</#if>
						</span>
					</div>
					<div class="rs-label">
						<span class="rs-label-wrapper" style="display: inline-block;">
							<span class="subTask">所属行业：</span>
							<#if (orderInfo.industryType)??>${orderInfo.industryType}</#if>
						</span>
					</div>
					<div class="rs-label">
						<span class="rs-label-wrapper" style="display: inline-block;">
							<span class="subTask">商户联系人：</span>
							<#if (orderInfo.contactName)??>${orderInfo.contactName}</#if>
						</span>
					</div>
					<div class="rs-label">
						<span class="rs-label-wrapper" style="display: inline-block;">
							<span class="subTask">商户联系人电话：</span>
							<#if (orderInfo.contactNumber)??>${orderInfo.contactNumber}</#if>
						</span>
					</div>
					
				</div>
				
				<div class="span6">
					<div class="rs-label">
						<span class="rs-label-wrapper" style="display: inline-block;">
							<span class="subTask">业务推广联系人：</span>
							<#if (orderInfo.promoteContact)??>${orderInfo.promoteContact}</#if>
						</span>
					</div>
					<div class="rs-label">
						<span class="rs-label-wrapper" style="display: inline-block;">
							<span class="subTask">业务推广联系方式：</span>						
							<#if (orderInfo.promotePhone)??>${orderInfo.promotePhone}</#if>
						</span>
					</div>
					<div class="rs-label">
						<span class="rs-label-wrapper" style="display: inline-block;">
							<span class="subTask">地推员：</span>
							<#if (orderInfo.salePerson)??>${orderInfo.salePerson}</#if>
						</span>
					</div>
					
					<div class="rs-label" style="min-height: 30px;">
						<span class="rs-label-wrapper" style="display: inline-block;">
							<span class="subTask">套餐名称*数量：</span>
							<#if (orderGoodName)??>${orderGoodName}</#if>
						</span>
					</div>
					<div class="rs-label">
						<span class="rs-label-wrapper" style="display: inline-block;">
							<span class="subTask">聚引客套餐版本：</span>
							<#if (orderInfo.orderVersion)??>${orderInfo.orderVersion}</#if>
						</span>
					</div>
					<div class="rs-label">
						<span class="rs-label-wrapper" style="display: inline-block;">
							<span class="subTask">订单备注信息：</span>
							<#if (orderInfo.remark)??>${orderInfo.remark}</#if>
						</span>
					</div>
					
				</div>
			
			</div>
			
			<div class="row-fluid clearfix">
				<div class="span12">
				<div class="rs-label height45">
					<div class="control-group">
						<label class="subTask" value="1">指派策划专家：</label>
						<div class="controls">
							 
							 	 <select id='${taskId!''}planningExpert1002' class="input-medium">
									<option value="" label="请选择">请选择</option>
									<#if planningPerson??>
										<#list planningPerson as item>  
											<option value="<#if item.id??>${item.id}</#if>"><#if item.name??>${item.name}</#if></option>  
										</#list>
									</#if>
								</select>	
							
							 
						</div>
					</div>
				</div>
			</div>
			</div>
				
						
		</div>
	</div>
	<div class="act-rs-right">
		<div class="padding15">
			<div class="act-time">开始：${startDate!''}</div>
			<div class="act-time">到期：${endDate!''}</div>
			<div class="act-jd">
				<div class="progress progress-success">
						<div class="bar" style="width: 0%"></div>
					</div>
				</div>
			<!-- <div class="act-jd-word positionrelative"><div class="acjw positionrabsolute" style="left: 80%;">${taskConsumTime!''}%</div></div> -->
		</div>
	</div> 
	<div class="act-process-left-footer" >
		<div class="footer-info floatleft">
			<p></p>
			<p>负责人：${taskUser!''}</p>
		</div>
		<div class="footer-btn floatright">
			<button style="button" class="btn btn-large"  onclick="submit_${taskId!''}submitForm1002(1)">确定完成</button>
			<span class="flowTaskState hide">已完成</span>
		</div>
	</div>
</div>
<script>
	$.post("../order/erpOrderOriginalInfo/getFlowUsers", {
			procInsId:$("#${taskId!''}procInsId1002").val(),
	},function(data) {
		if(data.PlanningExpert){
			$("#${taskId!''}planningExpert1002").val(data.PlanningExpert.userId);
		}
	});
</script>