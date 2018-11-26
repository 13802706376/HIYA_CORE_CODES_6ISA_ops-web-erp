<script type="text/javascript">
function submit_${taskId!''}submitForm1044(isLocation){
	var channels = ''; 
	var channelName =[];
	$('input[name="${taskId!''}channelname1044"]:checked').each(function(){ 
		channels+=$(this).val()+","; 
		channelName.push($(this).next('span').text());
	});
	if(channelName.length === 0 && channels === '') {
		return;
		$.jBox.tip("请选择方案提审方式！", 'error');
	}
	var isFinished=false;
	if(channelName.length === $('input[name="${taskId!''}channelname1044"]').length) {
		isFinished=true;
	}
	var submit = function (v, h, f) {
			if (v == 'ok') {
					$.jBox.tip("正在修改，请稍等...", 'loading', {
					timeout : 3000,
					persistent : true
			});
			$.post("../jyk/flow/work_promotion_plan_for", {
					channelList : channels,
					isFinished:isFinished,
					taskId:$("#${taskId!''}taskId1044").val(),
					procInsId:$("#${taskId!''}procInsId1044").val()
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
	$.jBox.confirm("确定推广方案提审吗？", "提示", submit);
}
</script>
<input type='hidden' id='${taskId!''}taskId1044' name='taskId1044' value="${taskId!''}"/>
<input type='hidden' id='${taskId!''}procInsId1044' name='procInsId1044'  value="${procInsId!''}"/>
<div class="act-rs-item  ${detailType!''}  ${followTaskDetailWrap!''}">
	<div class="act-rs-left floatleft">
		<div class="act-rs-title padding15">
			<h3><a href="../workflow/taskDetail?taskId=${taskId!''}&followTaskDetailWrap=${followTaskDetailWrap!''}&detailType=${detailType!''}">${taskName!''}</a></h3>
			<div class="listSubmit">
				<button onclick="submit_${taskId!''}submitForm1044()" type="button" class="makesrue btn">确定完成</button>
				<a href="javascript:;" class="select-user open-select-user" style="float: left;" onclick="syncSelect('${taskId!''}', '${taskUser!''}','${taskUserId!''}')"></a>
			</div>
			<div class="taskPrincipal"><span class="taskPrincipalName">${taskUser!''}</span></div>
		</div>
		<#if vars?exists>
		     <#list vars?keys as key> 
		         <#if vars["momo"]??>
		              <#assign momo = vars["momo"]>
		         </#if>
		         <#if vars["weibo"]??>
		             <#assign weibo = vars["weibo"]>
		         </#if>
		         <#if vars["friends"]??>
		              <#assign friends = vars["friends"]>
		         </#if>
		       </#list>
		 </#if>
		 
		
		<div class="act-rs-form padding15">
			<#if friends?exists && friends=="1" >
				<div class="rs-label">
					<label class="rs-label-wrapper">
						<input type="checkbox"  value="1" name="${taskId!''}channelname1044" 
						<#if subTaskList?? && (subTaskList?size > 0) ><#list subTaskList as subTask>
						 	<#if subTask.subTaskId == "1" && subTask.state=="1">
							 		 checked="true" 
							 	</#if>
							 </#list>
						  </#if>
						>
						<span  class="subTask" value="1">推广方案提审-朋友圈</span>
					</label>
				</div>
			</#if>
			<#if weibo?exists && weibo=="2" >
				<div class="rs-label">
					<label class="rs-label-wrapper">
						<input type="checkbox"  value="2" name="${taskId!''}channelname1044"
							<#if subTaskList?? && (subTaskList?size > 0) ><#list subTaskList as subTask>
						 	<#if subTask.subTaskId == "2" && subTask.state=="1">
							 		 checked="true"  
							 	</#if>
							 </#list>
						  </#if>
						>
						<span  class="subTask" value="2">推广方案提审-微博</span>
					</label>
				</div>
			</#if>
			<#if momo?exists && momo=="3" >
				<div class="rs-label">
					<label class="rs-label-wrapper">
						<input type="checkbox"  value="3" name="${taskId!''}channelname1044" 
							<#if subTaskList?? && (subTaskList?size > 0) ><#list subTaskList as subTask>
						 	<#if subTask.subTaskId == "3" && subTask.state=="1">
							 		 checked="true" 
							 	</#if>
							 </#list>
						  </#if>
						>
						<span  class="subTask" value="3">推广方案提审-陌陌</span>
					</label>
				</div>
			</#if>
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
			<button style="button" class="btn btn-large"  onclick="submit_${taskId!''}submitForm1044(1)">确定完成</button>
			<span class="flowTaskState hide">已完成</span>
		</div>
	</div>
</div>