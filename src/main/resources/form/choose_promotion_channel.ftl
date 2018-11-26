<script type="text/javascript">
function submit_${taskId!''}submitForm1003(isLocation){
	var channels = ""; 
	var channelName = [];
	$('input[name="${taskId!''}channelname1003"]:checked').each(function(){ 
		channels+=$(this).val()+","; 
		channelName.push($(this).next('span').text());
	}); 
	if(channels === ""){
		return;
		$.jBox.info("请选择推广渠道");
	}
	var submit = function (v, h, f) {
			if (v == 'ok') {
					$.jBox.tip("正在修改，请稍等...", 'loading', {
					timeout : 3000,
					persistent : true
			});
			$.post("../jyk/flow/choose_promotion_channel", {
					channelList : channels,
					taskId:$("#${taskId!''}taskId1003").val(),
					procInsId:$("#${taskId!''}procInsId1003").val()
				}, 
			function(data) {
				if (data.result) {
					$.jBox.info("指定推广渠道成功");
					if(isLocation==1){
						if (typeof updatePage === 'function') {
							updatePage();
						} else {
							window.location.href="../workflow/tasklist";
						}
					}else{
						window.location.reload();
					}
					//window.location.reload();
				} else {
					$.jBox.closeTip();
					$.jBox.info(data.message);
				}
			});
		}
		return true; 
	};
	$.jBox.confirm("确认订单的推广渠道为["+channelName+"]吗？", "提示", submit);
}
</script>
<input type='hidden' id='${taskId!''}taskId1003' name='taskId' value="${taskId!''}"/>
<input type='hidden' id='${taskId!''}procInsId1003' name='procInsId'  value="${procInsId!''}" />
<div class="act-rs-item ${detailType!''}  ${followTaskDetailWrap!''}">
	<div class="act-rs-left floatleft">
		<div class="act-rs-title padding15">
			<h3><a href="../workflow/taskDetail?taskId=${taskId!''}&followTaskDetailWrap=${followTaskDetailWrap!''}&detailType=${detailType!''}">${taskName!''}</a></h3>
			<div class="listSubmit">
				<button  onclick="submit_${taskId!''}submitForm1003()" type="button" class="makesrue btn">确定完成</button>
				<a href="javascript:;" class="select-user open-select-user" style="float: left;" onclick="syncSelect('${taskId!''}', '${taskUser!''}','${taskUserId!''}')"></a>
			</div>
			<div class="taskPrincipal"><span class="taskPrincipalName">${taskUser!''}</span></div>
		</div>
		<div class="act-rs-form padding15">
			<div class="rs-label">
				<label class="subTask" value="1">根据商户所在行业、套餐类型等选择推广通道</label>
				<#if channelService??>
					<#assign channels = channelService.getChannels(splitId)>
	 				<#function checkChannel channel>
	 					<#list channels as ch>
	 						<#if ch == channel>
	 							<#return "checked=checked">
	 						</#if>
	 					</#list>
		
	 					<#return "">
	 				</#function>
				</#if>
				<div class="controls margin-left0">
					<label><input type="checkbox" name="${taskId!''}channelname1003"  value="1" <#if channelService??>${checkChannel(1)}</#if>><span>&nbsp;微信朋友圈</span></input></label>
					<label><input type="checkbox" name="${taskId!''}channelname1003"  value="2" <#if channelService??>${checkChannel(2)}</#if>><span>&nbsp;新浪粉丝通</span></input></label>
					<label><input type="checkbox" name="${taskId!''}channelname1003"  value="3" <#if channelService??>${checkChannel(3)}</#if>><span>&nbsp;陌陌</span></input></label>
				</div>
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
			<button style="button" class="btn btn-large"  onclick="submit_${taskId!''}submitForm1003(1)">确定完成</button>
			<span class="flowTaskState hide">已完成</span>
		</div>
	</div>
	
</div>