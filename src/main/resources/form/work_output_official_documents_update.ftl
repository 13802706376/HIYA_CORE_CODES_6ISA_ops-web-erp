<script type="text/javascript">
function submit_${taskId!''}submitForm1020(isLocation){
	if($('.li-${taskId!''}').size() <= 0 && $('#${taskId!''}showFileNameInput').size() > 0 && $.trim($('#${taskId!''}orderFileName10008').val()) === "" && $.trim($('#${taskId!''}orderFileId10008').val()) === "") {
		return;
		$.jBox.tip("请选择商户推广文案！", 'error');
	}
	var submit = function (v, h, f) {
			if (v == 'ok') {
					$.jBox.tip("正在修改，请稍等...", 'loading', {
					timeout : 3000,
					persistent : true
			});
			$.post("../jyk/flow/work_output_official_documents", {
					orderFileId: $.trim($('#${taskId!''}orderFileId10008').val()),
					orderFileName:$.trim($('#${taskId!''}orderFileName10008').val()),
					taskId:$("#${taskId!''}taskId1020").val(),
					procInsId:$("#${taskId!''}procInsId1020").val()
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
	$.jBox.confirm("确认输出方案吗？", "提示", submit);
}
$(function() {
	//applyAjaxFileUpload("#${taskId!''}ajaxFileUploadInput1020",  "../workfile/file/upload");
	applyWebuploader("${taskDefKey!''}", "${procInsId!''}", "商户推广文案", "../workfile/file/uploads", 
		"${taskId!''}pickId", "${taskId!''}showFileNameInput", "${taskId!''}fileProgressDiv", "${taskId!''}orderFileId10008", "${taskId!''}orderFileName10008");
})
</script>
<input type='hidden' id='${taskId!''}taskId1020' name='taskId1020' value="${taskId!''}"/>
<input type='hidden' id='${taskId!''}procInsId1020' name='procInsId1020' value="${procInsId!''}"/>
<input type="hidden" id="${taskId!''}orderFileId10008">
<input type="hidden" id="${taskId!''}orderFileName10008">
<div class="act-rs-item ${detailType!''} ${followTaskDetailWrap!''}">
	<div class="act-rs-left floatleft">
		<div class="act-rs-title padding15">
			<#if vars?exists>
		        <#list vars?keys as key> 
		           <#if vars["workReleasePlanAuditTemp"]??>
		              <#assign workReleasePlanAuditTemp = vars["workReleasePlanAuditTemp"]>
		           </#if>
		           <#if vars["UploadPictureMaterial"]??>
				       <#assign UploadPictureMaterial = vars["UploadPictureMaterial"]>
				   </#if>
				    <#if vars["workPromotionPlanPreviewConfirmationIsSuccess"]??>
		              <#assign workPromotionPlanPreviewConfirmationIsSuccess = vars["workPromotionPlanPreviewConfirmationIsSuccess"]>
		           </#if>
		         </#list>
		    </#if>
			<h3><a href="../workflow/taskDetail?taskId=${taskId!''}&followTaskDetailWrap=${followTaskDetailWrap!''}&detailType=${detailType!''}">${taskName!''}
				<#if workReleasePlanAuditTemp?exists || workPromotionPlanPreviewConfirmationIsSuccess??>
					【退回】
				</#if>
			</a></h3>
			<div class="listSubmit">
				<button  onclick="submit_${taskId!''}submitForm1020()" type="button" class="makesrue btn">确定完成</button>
				<a href="javascript:;" class="select-user open-select-user" style="float: left;" onclick="syncSelect('${taskId!''}', '${taskUser!''}','${taskUserId!''}')"></a>
			</div>
			<div class="taskPrincipal">
				<span class="taskPrincipalName">${taskUser!''}</span>
			</div>
		</div>
		    <#if UploadPictureMaterial?exists  >
				<div class="hasuploadfile act-rs-form form-horizontal padding15">
					<div class="rs-label" style="height:1px;">
						<div class="control-group">
							<label class="subTask"></label>
							<div class="controls"></div>
						</div>
					</div>
					<div class="rs-label hasuploadfile">
						<div class="control-group">
							<label class="subTask" value="1">上传商户推广文案：</label>
							<div class="controls positionrelative">
								<#if workReleasePlanAuditTemp?exists >
									 <!--<input type="file" class="required" id="${taskId!''}ajaxFileUploadInput1020" name="upfile" data-val="${procInsId!''}" file-title="商户推广文案" order-file-id="${taskId!''}orderFileId10008" order-file-name="${taskId!''}orderFileName10008" />-->
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
								<#else> 
									 <!--<input type="file" class="required" id="${taskId!''}ajaxFileUploadInput1020" name="upfile" data-val="${procInsId!''}" file-title="商户推广文案" order-file-id="${taskId!''}orderFileId10008" order-file-name="${taskId!''}orderFileName10008" />-->
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
								</#if> 
							</div>
						</div>
					</div>
				</div>
			<#else>
				<font color="red" style="margin-left:15px;">推广图片未上传，无法处理当前任务.</font>
			</#if>
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
			<button style="button" class="btn btn-large"  onclick="submit_${taskId!''}submitForm1020(1)">确定完成</button>
			<span class="flowTaskState hide">已完成</span>
		</div>
	</div>
</div>