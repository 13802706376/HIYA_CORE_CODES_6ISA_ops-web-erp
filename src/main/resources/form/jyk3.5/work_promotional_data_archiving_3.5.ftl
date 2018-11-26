<script type="text/javascript">
function submit_${taskId!''}submitForm1054(isLocation){
	var channels = ''; 
	var channelName = [];
	$('input[name="${taskId!''}channelname1054"]:checked').each(function(){ 
		channels+=$(this).val()+","; 
		channelName.push($(this).next('span').text());
	});
	if(channelName.length === 0 && channels === '') {
		$.jBox.tip("请勾选效果报告输出给商户选项", 'error');
		return; 
	}
	var isFinished = false;
	if(channelName.length === 2) {
		isFinished = true;
	}
	
	var submit = function (v, h, f) {
			if (v == 'ok') {
					$.jBox.tip("正在修改，请稍等...", 'loading', {
					timeout : 3000,
					persistent : true
			});
			$.post("../jyk/flow/work_promotional_data_archiving", {
					channelList : channels,
					isFinished : isFinished,
					taskId:$("#${taskId!''}taskId1054").val(),
					procInsId:$("#${taskId!''}procInsId1054").val()
				}, 
			function(data) {
				if (data.result) {
					if(isLocation==1){
						if (typeof updatePage === 'function') {
							updatePage();
						} else {
							window.location.href="../workflow/tasklist";
						}
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
	$.jBox.confirm("确定将效果报告输出给商户吗？", "提示", submit);
}
</script>
<input type='hidden' id='${taskId!''}taskId1054' name='${taskId!''}taskId1054' value="${taskId!''}"/>
<input type='hidden' id='${taskId!''}procInsId1054' name='${taskId!''}procInsId1054'  value="${procInsId!''}"/>
<div class="act-rs-item  ${detailType!''}  ${followTaskDetailWrap!''}">
	<div class="act-rs-left floatleft">
		<div class="act-rs-title padding15">
			<h3><a href="../workflow/taskDetail?taskId=${taskId!''}&followTaskDetailWrap=${followTaskDetailWrap!''}&detailType=${detailType!''}">${taskName!''}</a></h3>
			<div class="listSubmit">
				<button onclick="submit_${taskId!''}submitForm1054()" type="button" class="makesrue btn">确定完成</button>
				<a href="javascript:;" class="select-user open-select-user" style="float: left;" onclick="syncSelect('${taskId!''}', '${taskUser!''}','${taskUserId!''}')"></a>
			</div>
			<div class="taskPrincipal"><span class="taskPrincipalName">${taskUser!''}</span></div>
		</div>
		<div class="act-rs-form padding15">
			<div class="rs-label">
				<label class="rs-label-wrapper">
					<input type="checkbox" <#if (subTaskList[0])?? && subTaskList[1].state?? && subTaskList[0].state == "1">checked="checked"</#if> value="1" name="${taskId!''}channelname1054">
					<span class="subTask" value="1">将效果报告上传至OEM后台，并修改推广状态；</span>
				</label>
			</div>
			<div class="rs-label">
				<label class="rs-label-wrapper">
					<input type="checkbox" <#if (subTaskList[1])?? && subTaskList[1].state?? && subTaskList[1].state == "1">checked="checked"</#if> value="2" name="${taskId!''}channelname1054">
					<span class="subTask" value="2">在商户运营服务群告知商户效果图报告已输出并引导商户登录商户管理后台查看。</span>
				</label>
			</div>
			<div class="rs-label">
				<label class="rs-label-wrapper">
					<a href="javascript:;" class="uploadDataPresentation" data-datas="${procInsId!''},3">最终数据报告</a>
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
			<button style="button" class="btn btn-large"  onclick="submit_${taskId!''}submitForm1054(1)">确定完成</button>
			<span class="flowTaskState hide">已完成</span>
		</div>
	</div>
</div>