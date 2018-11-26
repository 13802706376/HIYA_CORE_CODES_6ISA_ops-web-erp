<script type="text/javascript">
	var isPass = "";
	function submit_${taskId!''}submitForm1002(isLocation){
		
		var submit = function (v, h, f) {
				if (v == 'ok') {
						$.jBox.tip("正在修改，请稍等...", 'loading', {
						timeout : 3000,
						persistent : true
				});
				$.post("../jyk/flow/accountZhixiao/confirm_momo_account_sucess_zhixiao", {
						//url : $("#${taskId!''}URL").val(),
						isPass : isPass,
						taskId : $("#${taskId!''}taskId1002").val(),
						procInsId : $("#${taskId!''}procInsId1002").val()
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
		$.jBox.confirm("确认陌陌推广开户完成吗？", "提示", submit);
	}
	
	$(function(){
		$(".${taskId!''}isPass").click(function(){
				isPass = $('input[name="${taskId!''}isPass"]:checked').val();
			});
	});
</script>
<input type='hidden' id='${taskId!''}taskId1002' name='taskId' value="${taskId!''}"/>
<input type='hidden' id='${taskId!''}procInsId1002' name='procInsId'  value="${procInsId!''}"/>
<div class="act-rs-item  ${detailType!''}  ${followTaskDetailWrap!''}">
	<div class="act-rs-left floatleft">
		<div class="act-rs-title padding15">
			<h3><a href="../workflow/taskDetail?taskId=${taskId!''}&followTaskDetailWrap=${followTaskDetailWrap!''}&detailType=${detailType!''}">${taskName!''}</a></h3>
			<div class="listSubmit">
				<button  onclick="submit_${taskId!''}submitForm1002()" type="button" class="makesrue btn">确定完成</button>
				<a href="javascript:;" class="select-user open-select-user" style="float: left;" onclick="syncSelect('${taskId!''}', '${taskUser!''}','${taskUserId!''}')"></a>
			</div>
			<div class="taskPrincipal"><span class="taskPrincipalName">${taskUser!''}</span></div>
		</div>
		<div class="act-rs-form form-horizontal padding15">
			<!--
			<div class="control-group">
				<label class="rs-label-wrapper">
					<span class="subTask" value="1">落地页地址URL：</span>
					<input type="text" id="${taskId!''}URL" name="${taskId!''}URL"/>
				</label>
			</div>
			-->
			<div class="control-group">
				<label class="rs-label-wrapper">
					<span class="subTask" value="1">陌陌推广开户是否完成：</span>
					<input type="radio" name="${taskId!''}isPass" value="1" class="${taskId!''}isPass"/>是
					<input type="radio" name="${taskId!''}isPass" value="2" class="${taskId!''}isPass"/>否
				</label>
			</div>
			<div class="control-group">
				<label class="rs-label-wrapper">
					<span>选择的门店：${storeName!''}</span>
					<a href="../../download/adv/momo?storeId=${storeId!''}" class="downloadForm">《直销商户陌陌推广开户资料业管审核表》</a>
				</label>
			</div>
		</div>
	</div>
	<div class="act-rs-right">
		<div class="padding15">
			<div class="act-time">开始：${startDate!''}</div>
			<div class="act-time">到期：${endDate!''}</div>
			<div class="act-jd">
			 <#if taskConsumTime?? && (taskConsumTime < taskConsumTimeMin) >
					<div class="progress progress-success">
						<div class="bar" style="width: ${taskConsumTime!''}%"></div>
					</div>
			<#elseif taskConsumTime?? && (taskConsumTime > taskConsumTimeMin && taskConsumTime < taskConsumTimeMax)>
					<div class="progress progress-warning">
						<div class="bar" style="width: ${taskConsumTime!''}%"></div>
					</div>
			<#else>
				<div class="progress progress-danger">
						<div class="bar" style="width: 100%"></div>
					</div>
			</#if>
			</div>
			<div class="act-jd-word positionrelative"><div class="acjw positionrabsolute" style="left: 80%;">${taskConsumTime!''}%</div></div>
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