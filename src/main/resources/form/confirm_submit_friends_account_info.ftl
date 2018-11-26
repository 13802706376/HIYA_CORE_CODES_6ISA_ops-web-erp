<script type="text/javascript">
function submit_${taskId!''}submitForm1004(isLocation){
	var channels =[]; 
	var channelName =[];
	$('input[name="${taskId!''}channelname1004"]:checked').each(function(){ 
		channels+=$(this).val()+","; 
		channelName.push($(this).next('span').text());
	});
	if(channelName.length === 0) {
		return;
		$.jBox.tip("请选择协助完成朋友圈推广开户方式！", 'error');
	}
	var isFinished=false;
	if(channelName.length == 3) {
		isFinished=true;
	}
	var submit = function (v, h, f) {
			if (v == 'ok') {
					$.jBox.tip("正在修改，请稍等...", 'loading', {
					timeout : 3000,
					persistent : true
			});
			$.post("../jyk/flow/confirm_submit_friends_account_info", {
					channelList : channels,
					isFinished:isFinished,
					taskId:$("#${taskId!''}taskId1004").val(),
					procInsId:$("#${taskId!''}procInsId1004").val()
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
	$.jBox.confirm("确定协助完成朋友圈推广开户吗？", "提示", submit);
}
</script>
<input type='hidden' id='${taskId!''}taskId1004' name='taskId' value="${taskId!''}"/>
<input type='hidden' id='${taskId!''}procInsId1004' name='procInsId'  value="${procInsId!''}"/>
<div class="act-rs-item  ${detailType!''}  ${followTaskDetailWrap!''}">
	<div class="act-rs-left floatleft">
		<div class="act-rs-title padding15">
			<h3><a href="../workflow/taskDetail?taskId=${taskId!''}&followTaskDetailWrap=${followTaskDetailWrap!''}&detailType=${detailType!''}">${taskName!''}</a></h3>
			<div class="listSubmit">
				<button  onclick="submit_${taskId!''}submitForm1004()" type="button" class="makesrue btn">确定完成</button>
				<a href="javascript:;" class="select-user open-select-user" style="float: left;" onclick="syncSelect('${taskId!''}', '${taskUser!''}','${taskUserId!''}')"></a>
			</div>
			<div class="taskPrincipal"><span class="taskPrincipalName">${taskUser!''}</span></div>
		</div>
		<div class="act-rs-form padding15">
			<div class="rs-label">
				<label class="rs-label-wrapper">
					<input type="checkbox" value="1" name="${taskId!''}channelname1004"
					<#if subTaskList?? && (subTaskList?size > 0) ><#list subTaskList as subTask>
					 	<#if subTask.subTaskId == "1" && subTask.state=="1">
						 		 checked="true"
						 	</#if>
						 </#list>
					  </#if>
					>
					<span class="subTask" value="1">协助商户完成微信公众平台广告主及门店的开通</span>
				</label>
			</div>
			<div class="rs-label">
				<label class="rs-label-wrapper">
					<input type="checkbox" value="2" name="${taskId!''}channelname1004"
					<#if subTaskList?? && (subTaskList?size > 0) ><#list subTaskList as subTask>
					 	<#if subTask.subTaskId == "2" && subTask.state=="1">
						 		 checked="true" 
						 	</#if>
						 </#list>
					  </#if>
					>
					<span class="subTask" value="2">通过邮件提交朋友圈推广开户资料给业管，并抄送给对应策划专家</span>
				</label>
			</div>
			<div class="rs-label">
				<label class="rs-label-wrapper">
					<input type="checkbox" value="3" name="${taskId!''}channelname1004"
					<#if subTaskList?? && (subTaskList?size > 0) ><#list subTaskList as subTask>
					 	<#if subTask.subTaskId == "3" && subTask.state=="1">
						 		 checked="true"
						 	</#if>
						 </#list>
					  </#if>
					>
					<span class="subTask" value="3">协助商户完成朋友圈授权，并将结果邮件回复业管，抄送给对应策划专家</span>
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
			<button style="button" class="btn btn-large"  onclick="submit_${taskId!''}submitForm1004(1)">确定完成</button>
			<span class="flowTaskState hide">已完成</span>
		</div>
	</div>
</div>