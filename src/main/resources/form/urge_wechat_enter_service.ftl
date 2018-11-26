<script type="text/javascript">
	function submit_${taskId!''}submitForm12(isLocation){
		var channels = ''; 
		var channelName = [];
		$('input[name="${taskId!''}channelname12"]:checked').each(function(){ 
			channels+=$(this).val()+","; 
			channelName.push($(this).next('span').text());
		});
		if(channelName.length === 0 && channels === '') {
			return;
			$.jBox.tip("请确保服务商提交微信支付进件信息！", 'error');
		}
		var isFinished=false;
		if(channelName.length == 3){
			isFinished=true;
		}
		var submit = function (v, h, f) {
				if (v == 'ok') {
						$.jBox.tip("正在修改，请稍等...", 'loading', {
						timeout : 3000,
						persistent : true
				});
				$.post("../jyk/flow/accountService/urge_wechat_enter_service", {
						channelList : channels,
						isFinished : isFinished,
						taskId:$("#${taskId!''}taskId12").val(),
						procInsId:$("#${taskId!''}procInsId12").val()
					}, 
				function(data) {
					if (data.result) {
						$.jBox.info("确保服务商提交微信支付进件信息任务完成.");
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
		$.jBox.confirm("确保服务商提交微信支付进件信息吗？", "提示", submit);
	}
</script>
<input type='hidden' id='${taskId!''}taskId12' name='taskIdTest' value="${taskId!''}"/>
<input type='hidden' id='${taskId!''}procInsId12' name='procInsIdTest'  value="${procInsId!''}"/>
<div class="act-rs-item  ${detailType!''}  ${followTaskDetailWrap!''}">
	<div class="act-rs-left floatleft">
		<div class="act-rs-title padding15">
				<h3><a href="../workflow/taskDetail?taskId=${taskId!''}&followTaskDetailWrap=${followTaskDetailWrap!''}&detailType=${detailType!''}">${taskName!''}</a></h3>
			<div class="listSubmit">
				<button onclick="submit_${taskId!''}submitForm12()" type="button" class="makesrue btn">确定完成</button>
				<a href="javascript:;" class="select-user open-select-user" style="float: left;" onclick="syncSelect('${taskId!''}', '${taskUser!''}','${taskUserId!''}')"></a>
			</div>
			<div class="taskPrincipal"><span class="taskPrincipalName">${taskUser!''}</span></div>
		</div>
		<div class="act-rs-form padding15">
			<div class="rs-label">
				<label class="rs-label-wrapper">
					<span>选择的门店：${storeName!''}</span>
				</label>
			</div>	
			<div class="rs-label">	
				<label class="rs-label-wrapper">
					<input type="checkbox" <#if (subTaskList[0])?? && subTaskList[0].state == "1">checked="checked"</#if>  value="1" name="${taskId!''}channelname12">
					<span class="subTask" value="1">督促服务商提交所选门店的微信支付进件信息</span>
				</label>
			</div>	
			<div class="rs-label">
				<label class="rs-label-wrapper">
					<input type="checkbox" <#if (subTaskList[0])?? && subTaskList[0].state == "1">checked="checked"</#if>  value="2" name="${taskId!''}channelname12">
					<span class="subTask" value="2">督促业管完成微信支付进件</span>
				</label>
			</div>	
			<div class="rs-label">
				<label class="rs-label-wrapper">
					<input type="checkbox" <#if (subTaskList[0])?? && subTaskList[0].state == "1">checked="checked"</#if>  value="3" name="${taskId!''}channelname12">
					<span class="subTask" value="3">与服务商确认推广门店的微信支付已开通完成</span>
				</label>
			</div>
				<!--
				<label class="rs-label-wrapper">
					<input type="checkbox" <#if (subTaskList[0])?? && subTaskList[0].state == "1">checked="checked"</#if>  value="1" name="${taskId!''}channelname12">
					<span class="subTask" value="1">确保服务商提交微信支付进件信息</span>
				</label>
				-->
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
			<button style="button" class="btn btn-large"  onclick="submit_${taskId!''}submitForm12(1)">确定完成</button>
			<span class="flowTaskState hide">已完成</span>
		</div>
	</div>
</div>