<script type="text/javascript">
function submit_${taskId!''}submitForm1025(isLocation){

	if($('.li-${taskId!''}').size() <= 0 && $('#${taskId!''}showFileNameInput').size() > 0 && $.trim($('#${taskId!''}orderFileName10010').val()) === '' && $.trim($('#${taskId!''}orderFileId10010').val()) === ''){
		//$.jBox.tip("请上传推广创意素材！", 'error');
		return; 
	}
	var submit = function (v, h, f) {
			if (v == 'ok') {
					$.jBox.tip("正在修改，请稍等...", 'loading', {
					timeout : 3000,
					persistent : true
			});
			$.post("../jyk/flow/work_summarize_promotion_materials", {
				orderFileId: $.trim($('#${taskId!''}orderFileId10010').val()),
				orderFileName:$.trim($('#${taskId!''}orderFileName10010').val()),
				taskId:$("#${taskId!''}taskId1025").val(),
				procInsId:$("#${taskId!''}procInsId1025").val()
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
	$.jBox.confirm("确定汇总并同步推广素材吗？", "提示", submit);
}
$(function() {
	//applyAjaxFileUpload("#${taskId!''}ajaxFileUploadInput1025",  "../workfile/file/upload");
	applyWebuploader("${taskDefKey!''}", "${procInsId!''}", "推广创意素材", "../workfile/file/uploads", 
		"${taskId!''}pickId", "${taskId!''}showFileNameInput", "${taskId!''}fileProgressDiv", "${taskId!''}orderFileId10010", "${taskId!''}orderFileName10010");
});
</script>
<input type='hidden' id='${taskId!''}taskId1025' name='taskId1025' value="${taskId!''}"/>
<input type='hidden' id='${taskId!''}procInsId1025' name='procInsId1025' value="${procInsId!''}"/>
<input type="hidden" id="${taskId!''}orderFileId10010">
<input type="hidden" id="${taskId!''}orderFileName10010">
<div class="act-rs-item ${detailType!''} ${followTaskDetailWrap!''}">
	<#if vars?exists>
        <#list vars?keys as key> 
           <#if vars["workReleasePlanAuditTemp"]??>
           		<#assign workReleasePlanAuditTemp = vars["workReleasePlanAuditTemp"]>
           </#if>
           <#if vars["workPromotionPlanPreviewConfirmationIsSuccess"]??>
              <#assign workPromotionPlanPreviewConfirmationIsSuccess = vars["workPromotionPlanPreviewConfirmationIsSuccess"]>
           </#if>
        </#list>
  	</#if>
	<div class="act-rs-left floatleft">
		<div class="act-rs-title padding15">
			<h3><a href="../workflow/taskDetail?taskId=${taskId!''}&followTaskDetailWrap=${followTaskDetailWrap!''}&detailType=${detailType!''}">${taskName!''}
				<#if workReleasePlanAuditTemp?exists || workPromotionPlanPreviewConfirmationIsSuccess??>
					【退回】
				</#if>
			</a></h3>
			<div class="listSubmit">
				<button  onclick="submit_${taskId!''}submitForm1025()" type="button" class="makesrue btn">确定完成</button>
				<a href="javascript:;" class="select-user open-select-user" style="float: left;" onclick="syncSelect('${taskId!''}', '${taskUser!''}','${taskUserId!''}')"></a>
			</div>
			<div class="taskPrincipal"><span class="taskPrincipalName">${taskUser!''}</span></div>
		</div>
		<div class="hasuploadfile form-horizontal act-rs-form padding15">
			<div class="rs-label hasuploadfile">
				<div class="control-group">
					<label class="subTask" value="1">推广创意素材：</label>
					<div class="controls positionrelative">
						<!--<input type="file" class="required" id="${taskId!''}ajaxFileUploadInput1025" name="upfile" data-val="${procInsId!''}" file-title="推广创意素材" order-file-id="${taskId!''}orderFileId10010" order-file-name="${taskId!''}orderFileName10010" />-->
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
			<button style="button" class="btn btn-large"  onclick="submit_${taskId!''}submitForm1025(1)">确定完成</button>
			<span class="flowTaskState hide">已完成</span>
		</div>
	</div>
</div>