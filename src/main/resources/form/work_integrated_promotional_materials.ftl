<script type="text/javascript">
function submit_${taskId!''}submitForm1041(isLocation){
	var channels =""; 
	var channelName =[];
	$('input[name="${taskId!''}channelname1041"]:checked').each(function(){ 
		channels+=$(this).val()+","; 
		channelName.push($(this).next('span').text());
	});
	
	var isFinished=false;
	var workIntegratedPromotionalMaterialsVars="";
	
	if($("#${taskId!''}workIntegratedPromotionalMaterials").length > 0){
		outputVars=$("#${taskId!''}workIntegratedPromotionalMaterials").val();
	}else{
		outputVars=$.trim($('#${taskId!''}orderFileId10022').val());
	}
	
	if(outputVars!="" && channels != ""){
		isFinished=true;
	}
	
	if(channelName.length === 0 && channels === '' && outputVars === '') {
		return;
		$.jBox.tip("文案及图片等素材与商户的需求相符！", 'error');
	}

	if($('.li-${taskId!''}').size() <= 0 && $('#${taskId!''}showFileNameInput').size() > 0 && $.trim($('#${taskId!''}orderFileName10022').val()) === "" && $.trim($('#${taskId!''}orderFileId10022').val()) === "") {
		//$.jBox.tip("请选择推广创意初稿！", 'error');
		return;
	}

	var submit = function (v, h, f) {
			if (v == 'ok') {
					$.jBox.tip("正在修改，请稍等...", 'loading', {
					timeout : 3000,
					persistent : true
			});
			$.post("../jyk/flow/work_integrated_promotional_materials", {
				channelList : channels,
				orderFileId: $.trim($('#${taskId!''}orderFileId10022').val()),
				orderFileName:$.trim($('#${taskId!''}orderFileName10022').val()),
				isFinished:isFinished,
				taskId:$("#${taskId!''}taskId1041").val(),
				procInsId:$("#${taskId!''}procInsId1041").val()
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
	$.jBox.confirm("确定整合推广素材吗？", "提示", submit);
}
$(function() {
	//applyAjaxFileUpload("#${taskId!''}ajaxFileUploadInput1041", "../workfile/file/upload");
	applyWebuploader("${taskDefKey!''}", "${procInsId!''}", "推广创意初稿", "../workfile/file/uploads", 
		"${taskId!''}pickId", "${taskId!''}showFileNameInput", "${taskId!''}fileProgressDiv", "${taskId!''}orderFileId10022", "${taskId!''}orderFileName10022");
})
</script>
<input type='hidden' id='${taskId!''}taskId1041' name='taskId1041' value="${taskId!''}"/>
<input type='hidden' id='${taskId!''}procInsId1041' name='procInsId1041' value="${procInsId!''}"/>
<input type="hidden" id="${taskId!''}orderFileId10022">
<input type="hidden" id="${taskId!''}orderFileName10022">
<div class="act-rs-item ${detailType!''} ${followTaskDetailWrap!''}">
					<#if vars?exists>
		                <#list vars?keys as key> 
		                   <#if vars["workPromotionPlanPreviewConfirmationIsSuccess"]??>
				              <#assign workPromotionPlanPreviewConfirmationIsSuccess = vars["workPromotionPlanPreviewConfirmationIsSuccess"]>
				           </#if>
		                </#list>
		          	  </#if>
	<div class="act-rs-left floatleft">
		<div class="act-rs-title padding15">
			<h3><a href="../workflow/taskDetail?taskId=${taskId!''}&followTaskDetailWrap=${followTaskDetailWrap!''}&detailType=${detailType!''}">${taskName!''}
				<#if workPromotionPlanPreviewConfirmationIsSuccess??>
					【退回】
				</#if>
			</a></h3>
			<div class="listSubmit">
				<button onclick="submit_${taskId!''}submitForm1041()" type="button" class="makesrue btn">确定完成</button>
				<a href="javascript:;" class="select-user open-select-user" style="float: left;" onclick="syncSelect('${taskId!''}', '${taskUser!''}','${taskUserId!''}')"></a>
			</div>
			<div class="taskPrincipal"><span class="taskPrincipalName">${taskUser!''}</span></div>
		</div>
		<#if vars?exists>
		        <#list vars?keys as key> 
		           <#if vars["workPromotionPlanPreviewConfirmationIsSuccess"]??>
		              <#assign workPromotionPlanPreviewConfirmationIsSuccess = vars["workPromotionPlanPreviewConfirmationIsSuccess"]>
		           </#if>
		         </#list>
		    </#if>
		<div class="hasuploadfile form-horizontal act-rs-form padding15">
			<div class="rs-label">
				<label class="rs-label-wrapper">
					<input type="checkbox"  value="1" name="${taskId!''}channelname1041"
					<#if subTaskList?? && (subTaskList?size > 0) >
						<#list subTaskList as subTask>
						 	<#if subTask.subTaskId == "1" && subTask.state=="1">
						 		 <#if workPromotionPlanPreviewConfirmationIsSuccess?exists >
								 <#else>
								 		 checked="true"
								 </#if>
							 </#if>
							
						 </#list>
					 </#if>
					>
					<span class="subTask" value="1">保证文案及图片等素材与商户的需求相符</span>
				</label>
			</div>
			<div class="rs-labelhasuploadfile ">
				<div class="control-group">
					<label class="subTask" value="2">推广创意初稿：</label>
					<div class="controls positionrelative">
						<#if vars?exists>
			                <#list vars?keys as key> 
			                   <#if vars["workIntegratedPromotionalMaterials"]??>
			                   		<#assign workIntegratedPromotionalMaterials = vars["workIntegratedPromotionalMaterials"]>
			                   </#if>
			                </#list>
		          		</#if>
					 	<#if workIntegratedPromotionalMaterials?exists>
					  		<#if workPromotionPlanPreviewConfirmationIsSuccess?exists >
						  		<!--<input type="file" class="required" id="${taskId!''}ajaxFileUploadInput1041" name="upfile" data-val="${procInsId!''}" file-title="推广创意初稿" order-file-id="${taskId!''}orderFileId10022" order-file-name="${taskId!''}orderFileName10022" />-->
						  		<div class="ajaxFileUploadInputDiv">
									<input id="${taskId!''}showFileNameInput" type="text" readonly="readonly">
									<div id="${taskId!''}pickId" class="filePickIds" data-subTaskId="2">选择文件</div>
								</div>
								<div class="fileProgressDiv">
									<div id="${taskId!''}fileProgressDiv" class="fileProgressWrap"></div>
								</div>
							<#else>
								<div class="ajaxFileUploadInputDiv">
									<input id="${taskId!''}showFileNameInput" type="text" readonly="readonly">
									<div id="${taskId!''}pickId" class="filePickIds" data-subTaskId="2">选择文件</div>
								</div>
								<div class="fileProgressDiv">
									<div id="${taskId!''}fileProgressDiv" class="fileProgressWrap"></div>
								</div>
								<#if subTaskList?? && (subTaskList?size > 0) >
									<#list subTaskList as subTask>
								 		<#if subTask.orderFiles?? && (subTask.orderFiles?size > 0)>
											<ul class="fileviews">
									 			<#list subTask.orderFiles as fileinfo>
									 				<#if fileinfo.subTaskId=="2" >
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
			            		<input type='hidden' id='${taskId!''}workIntegratedPromotionalMaterials' name='${taskId!''}workIntegratedPromotionalMaterials' value=" ${workIntegratedPromotionalMaterials!""}" />
							</#if>
				  		<#else>
							<!--<input type="file" class="required" id="${taskId!''}ajaxFileUploadInput1041" name="upfile" data-val="${procInsId!''}" file-title="推广创意初稿" order-file-id="${taskId!''}orderFileId10022" order-file-name="${taskId!''}orderFileName10022" />-->
							<div class="ajaxFileUploadInputDiv">
								<input id="${taskId!''}showFileNameInput" type="text" readonly="readonly">
								<div id="${taskId!''}pickId" class="filePickIds" data-subTaskId="2">选择文件</div>
							</div>
							<div class="fileProgressDiv">
								<div id="${taskId!''}fileProgressDiv" class="fileProgressWrap"></div>
							</div>
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
			<button style="button" class="btn btn-large"  onclick="submit_${taskId!''}submitForm1041(1)">确定完成</button>
			<span class="flowTaskState hide">已完成</span>
		</div>
	</div>
</div>