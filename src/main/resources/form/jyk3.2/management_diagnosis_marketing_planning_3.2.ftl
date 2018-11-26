<script type="text/javascript">
	var management_diagnosis_marketing_planning = '3.2';
	var task_flow_version = '3.2';
	function submit_${taskId!''}submitForm12(isLocation){
		if($('.li-${taskId!''}').size() <= 0 && $('#${taskId!''}showFileNameInput').size() > 0 && !$('#${taskId!''}orderFileId12').val() && !$('#${taskId!''}orderFileName12').val()){
			$.jBox.info("请选择你需要上传的文件。");
			return;
		}
	
		var channels = ''; 
		var channelName = [];
		$('input[name="${taskId!''}channelname12"]:checked').each(function(){ 
			channels+=$(this).val()+","; 
			channelName.push($(this).next('span').text());
		});
		if(channelName.length === 0 && channels === '') {
			$.jBox.tip("请选择推广通道！", 'error');
			return;
		}
		var isFinished=false;
		if(channels != '') {
			isFinished=true;
		}
		var submit = function (v, h, f) {
				if (v == 'ok') {
						$.jBox.tip("正在修改，请稍等...", 'loading', {
						timeout : 3000,
						persistent : true
				});
				$.post("../jyk/flow/accountZhixiao/management_diagnosis_marketing_planning_3.1", {
						orderFileName: $.trim($('#${taskId!''}orderFileName12').val()),
						orderFileId:$.trim($('#${taskId!''}orderFileId12').val()),
						channelList : channels,
						isFinished : isFinished,
						taskId:$("#${taskId!''}taskIdTest12").val(),
						procInsId:$("#${taskId!''}procInsIdTest12").val()
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
		$.jBox.confirm("确认经营诊断&营销方案吗？", "提示", submit);
	}
	
	$(function() {
		applyWebuploader("${taskDefKey!''}", "${procInsId!''}", "方案策划", "../workfile/file/uploads", 
			"${taskId!''}pickId", "${taskId!''}showFileNameInput", "${taskId!''}fileProgressDiv", "${taskId!''}orderFileId12", "${taskId!''}orderFileName12");
	});
</script>
<style type="text/css">.act-rs-item .act-rs-left.isisTaskDetail{ min-height: 40px;border:none; }</style>
<input type='hidden' id="${taskId!''}taskIdTest12" name='taskIdTest' value="${taskId!''}"/>
<input type='hidden' id="${taskId!''}procInsIdTest12" name='procInsIdTest' value="${procInsId!''}"/>

<input type="hidden" id="${taskId!''}orderFileId12">
<input type="hidden" id="${taskId!''}orderFileName12">

<div class="act-rs-item  ${detailType!''}  ${followTaskDetailWrap!''}">
	<div class="act-rs-left <#if !isTaskDetail??>floatleft<#else>isisTaskDetail</#if>">
		<div class="act-rs-title padding15">
				<h3><a href="../workflow/taskDetail?taskId=${taskId!''}&followTaskDetailWrap=${followTaskDetailWrap!''}&taskUser=${taskUser!''}
					&taskDefKey=${taskDefKey!''}&splitId=${splitId!''}&startDate=${startDate!''}&endDate=${endDate!''}&procInsId=${procInsId!''}&detailType=${detailType!''}">${taskName!''}</a></h3>
			<div class="listSubmit">
				<#-- <button  onclick="submit_${taskId!''}submitForm12()" type="button" class="makesrue btn">确定完成</button> -->
				<a href="javascript:;" class="select-user open-select-user" style="float: left;" onclick="syncSelect('${taskId!''}', '${taskUser!''}','${taskUserId!''}')"></a>
			</div>
			<div class="taskPrincipal"><span class="taskPrincipalName">${taskUser!''}</span></div>
		</div>
	</div>
	
<#if !isTaskDetail??>
	<div class="act-rs-right" id="taskTime">
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
</#if>

	<#-- <div class="act-process-left-footer" >
		<div class="footer-info floatleft">
			<p></p>
			<p>负责人：${taskUser!''}</p>
		</div>
		<div class="footer-btn floatright">
			<button style="button" class="btn btn-large"  onclick="submit_${taskId!''}submitForm12(1)">确定完成</button>
			<span class="flowTaskState hide">已完成</span>
		</div>
	</div> -->
	<div id="rootNode"></div>
</div>