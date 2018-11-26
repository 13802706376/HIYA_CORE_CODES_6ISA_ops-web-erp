<script type="text/javascript">
function submit_${taskId!''}submitForm1050(isLocation){
	if($('.li-${taskId!''}').size() <= 0 && $('#${taskId!''}showFileNameInput').size() > 0 && !$('#${taskId!''}orderFileName11108').val() && $('#${taskId!''}orderFileId11108').val()) {
		return;
		$.jBox.tip("请选择推广数据汇总表！", 'error');
	}
	var submit = function (v, h, f) {
			if (v == 'ok') {
					$.jBox.tip("正在修改，请稍等...", 'loading', {
					timeout : 3000,
					persistent : true
			});
			$.post("../jyk/flow/work_data_consolidation_after_promotion", {
					orderFileId: $('#${taskId!''}orderFileId11108').val(),
					orderFileName: $('#${taskId!''}orderFileName11108').val(),
					taskId:$("#taskId1050").val(),
					procInsId:$("#procInsId1050").val()
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
	$.jBox.confirm("确定订单整理推广数据，形成商户推广数据汇总表并上传吗？", "提示", submit);
}
$(function() {
	//applyAjaxFileUpload("#${taskId!''}consolidationAfterPromotionPic", "../workfile/file/upload");
	applyWebuploader("${taskDefKey!''}", "${procInsId!''}", "投放方案输出", "../workfile/file/uploads", 
		"${taskId!''}pickId", "${taskId!''}showFileNameInput", "${taskId!''}fileProgressDiv", "${taskId!''}orderFileId11108", "${taskId!''}orderFileName11108");
})
</script>
<input type='hidden' id='taskId1050' name='taskId1050' value="${taskId!''}"/>
<input type='hidden' id='procInsId1050' name='procInsId1050' value="${procInsId!''}"/>
<input type="hidden" id="${taskId!''}orderFileId11108">
<input type="hidden" id="${taskId!''}orderFileName11108">
<div class="act-rs-item  ${detailType!''}  ${followTaskDetailWrap!''}">
	<div class="act-rs-left floatleft">
		<div class="act-rs-title padding15">
			<h3><a href="../workflow/taskDetail?taskId=${taskId!''}&followTaskDetailWrap=${followTaskDetailWrap!''}&detailType=${detailType!''}">${taskName!''}</a></h3>
			<div class="listSubmit">
				<button onclick="submit_${taskId!''}submitForm1050()" type="button" class="makesrue btn">确定完成</button>
				<a href="javascript:;" class="select-user open-select-user" style="float: left;" onclick="syncSelect('${taskId!''}', '${taskUser!''}','${taskUserId!''}')"></a>
			</div>
			<div class="taskPrincipal"><span class="taskPrincipalName">${taskUser!''}</span></div>
		</div>
		<div class="hasuploadfile act-rs-form form-horizontal padding15">
			<div class="rs-label" style="height:1px;">
				<div class="control-group">
					<label class="subTask"></label>
					<div class="controls"></div>
				</div>
			</div>
			<div class="hasuploadfile rs-label">
				<div class="control-group">
					<label class="subTask" value="1">推广数据汇总表：</label>
					<div class="controls positionrelative">
						<!--<input type="file" class="required ajaxFileUploadInput" id="${taskId!''}consolidationAfterPromotionPic" name="upfile" data-val="${procInsId!''}" file-title="商户推广数据汇总表" order-file-id="${taskId!''}orderFileId11108" order-file-name="${taskId!''}orderFileName11108" />-->
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
			<button style="button" class="btn btn-large"  onclick="submit_${taskId!''}submitForm1050(1)">确定完成</button>
			<span class="flowTaskState hide">已完成</span>
		</div>
	</div>
</div>