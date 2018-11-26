<script type="text/javascript">
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
				$.post("../jyk/flow/accountZhixiao/management_diagnosis_marketing_planning", {
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
<input type='hidden' id="${taskId!''}taskIdTest12" name='taskIdTest' value="${taskId!''}"/>
<input type='hidden' id="${taskId!''}procInsIdTest12" name='procInsIdTest' value="${procInsId!''}"/>

<input type="hidden" id="${taskId!''}orderFileId12">
<input type="hidden" id="${taskId!''}orderFileName12">

<div class="act-rs-item  ${detailType!''}  ${followTaskDetailWrap!''}">
	<div class="act-rs-left floatleft">
		<div class="act-rs-title padding15">
				<h3><a href="../workflow/taskDetail?taskId=${taskId!''}&followTaskDetailWrap=${followTaskDetailWrap!''}&detailType=${detailType!''}">${taskName!''}</a></h3>
			<div class="listSubmit">
				<button  onclick="submit_${taskId!''}submitForm12()" type="button" class="makesrue btn">确定完成</button>
				<a href="javascript:;" class="select-user open-select-user" style="float: left;" onclick="syncSelect('${taskId!''}', '${taskUser!''}','${taskUserId!''}')"></a>
			</div>
			<div class="taskPrincipal"><span class="taskPrincipalName">${taskUser!''}</span></div>
		</div>
		<div class="hasuploadfile form-horizontal act-rs-form padding15">
			<div class="rs-label" style="height:1px;">
				<div class="control-group">
					<label class="subTask"></label>
					<div class="controls"></div>
				</div>
			</div>
			<div class="rs-label hasuploadfile">
				<div class="control-group">
					<label class="subTask" value="1">方案策划：</label>
					<div class="controls positionrelative" style="margin-left:100px;">
						<div class="ajaxFileUploadInputDiv">
							<input id="${taskId!''}showFileNameInput" type="text" readonly="readonly">
							<div id="${taskId!''}pickId" class="filePickIds" data-subTaskId="1">选择文件</div>
						</div>
						<div class="fileProgressDiv">
							<div id="${taskId!''}fileProgressDiv" class="fileProgressWrap"></div>
						</div>
						<#if subTaskList?? && (subTaskList?size > 0) >
							<#list subTaskList as subTask>
						 		<#if subTask.orderFiles?? && (subTask.orderFiles?size > 0)>
									<ul class="fileviews">
							 			<#list subTask.orderFiles as fileinfo>
							 				<#if fileinfo.subTaskId=="1" >
								 		 		<li class="fileli li-${taskId!''}">
													<span class="filename" title="${fileinfo.fileName}">${fileinfo.fileName}</span>
													<a href="javascript:;" class="filedelete icon-trash" data-filename="${fileinfo.fileName}" data-fileid="${fileinfo.id}" title="删除"></a>
												</li>
											</#if>
										</#list>
									</ul>
							 	</#if>
						 	</#list>
						</#if>
					</div>
				</div>
			</div>
			
			<div class="rs-label">
				<label class="rs-label-wrapper" class="subTask" value="2">选择推广通道：</label>
				<span class="controls margin-left0">
					<label><input type="checkbox" name="${taskId!''}channelname12"  value="1" ><span>&nbsp;微信朋友圈</span></input></label>
					<label><input type="checkbox" name="${taskId!''}channelname12"  value="2" ><span>&nbsp;新浪粉丝通</span></input></label>
					<label><input type="checkbox" name="${taskId!''}channelname12"  value="3" ><span>&nbsp;陌陌</span></input></label>
				</span>
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
			<button style="button" class="btn btn-large"  onclick="submit_${taskId!''}submitForm12(1)">确定完成</button>
			<span class="flowTaskState hide">已完成</span>
		</div>
	</div>
</div>