<script type="text/javascript">
function submit_${taskId!''}submitForm1053(isLocation){
	var channels = ''; 
	var channelName = [];
	$('input[name="${taskId!''}channelname1053"]:checked').each(function(){ 
		channels+=$(this).val()+","; 
		channelName.push($(this).next('span').text());
	});
	
	var isFinished=false;
	var orderFileName="";
	
	if($("#${taskId!''}workEffectReportOutputPath").length > 0){
		orderFileName=$("#${taskId!''}workEffectReportOutputPath").val();
	}
	if($("#${taskId!''}workEffectReportOutputPath").length <= 0  && $("#${taskId!''}orderFileId90002").val() !=""){
		orderFileName=$("#${taskId!''}orderFileId90002").val();
	}
	
	if(orderFileName!="" && channelName.length == 3){
		isFinished=true;	
	}

	if(channelName.length === 0 && channels === '' && orderFileName === '') {
		return;
		$.jBox.tip("请勾选输出项！", 'error');
	}
	if($('.fileli').size() <= 0 && $.trim($('#${taskId!''}orderFileName90002').val()) === "" && $.trim($('#${taskId!''}orderFileId90002').val()) === ""){
		$.jBox.tip("请选择效果报告！", 'error');
		return;
	}
	var submit = function (v, h, f) {
			if (v == 'ok') {
					$.jBox.tip("正在修改，请稍等...", 'loading', {
					timeout : 3000,
					persistent : true
			});
			$.post("../jyk/flow/work_effect_report_output", {
				channelList: channels,
				orderFileId: $.trim($('#${taskId!''}orderFileId90002').val()),
				orderFileName:$.trim($('#${taskId!''}orderFileName90002').val()),
				isFinished:isFinished,
				taskId:$("#${taskId!''}taskId1053").val(),
				procInsId:$("#${taskId!''}procInsId1053").val()
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
	$.jBox.confirm("确定输出效果报告吗？", "提示", submit);
}
$(function() {
	//applyAjaxFileUpload("#${taskId!''}effectReportOutputDocument", "../workfile/file/upload");
	applyWebuploader("${taskDefKey!''}", "${procInsId!''}", "效果报告", "../workfile/file/uploads", 
		"${taskId!''}pickId", "${taskId!''}showFileNameInput", "${taskId!''}fileProgressDiv", "${taskId!''}orderFileId90002", "${taskId!''}orderFileName90002");
})
</script>
<input type='hidden' id="${taskId!''}taskId1053" name='taskId1053' value="${taskId!''}"/>
<input type='hidden' id="${taskId!''}procInsId1053" name='procInsId1053' value="${procInsId!''}"/>
<input type="hidden" id="${taskId!''}orderFileId90002">
<input type="hidden" id="${taskId!''}orderFileName90002">
<div class="act-rs-item ${detailType!''} ${followTaskDetailWrap!''}">
	<div class="act-rs-left floatleft">
		<div class="act-rs-title padding15">
			<h3><a href="../workflow/taskDetail?taskId=${taskId!''}&followTaskDetailWrap=${followTaskDetailWrap!''}&detailType=${detailType!''}">${taskName!''}</a></h3>
			<div class="listSubmit">
				<button onclick="submit_${taskId!''}submitForm1053()" type="button" class="makesrue btn">确定完成</button>
				<a href="javascript:;" class="select-user open-select-user" style="float: left;" onclick="syncSelect('${taskId!''}', '${taskUser!''}','${taskUserId!''}')"></a>
			</div>
			<div class="taskPrincipal"><span class="taskPrincipalName">${taskUser!''}</span></div>
		</div>
		<div class="hasuploadfile form-horizontal act-rs-form padding15">
			<div class="rs-label">
				<label class="rs-label-wrapper">
					<input type="checkbox" value="1" name="${taskId!''}channelname1053"
					 <#if subTaskList?? && (subTaskList?size > 0) ><#list subTaskList as subTask>
					 	<#if subTask.subTaskId == "1" && subTask.state=="1"> 
					 	checked="true"
						 	</#if>
						 </#list>
					  </#if>
					>
					<span class="subTask" value="1">输出效果报告并交付策划专家接口人审核</span>
				</label>
			</div>
			<div class="rs-label">
				<label class="rs-label-wrapper">
					<input type="checkbox" value="2" name="${taskId!''}channelname1053"
					 <#if subTaskList?? && (subTaskList?size > 0) ><#list subTaskList as subTask>
					 	<#if subTask.subTaskId == "2" && subTask.state=="1">
						 		 checked="true" 
						 	</#if>
						 </#list>
					  </#if>
					>
					<span class="subTask" value="2">将效果报告上传至OEM后台，并修改推广状态</span>
				</label>
			</div>
			<div class="rs-label">
				<label class="rs-label-wrapper">
					<input type="checkbox" value="3" name="${taskId!''}channelname1053"
						 <#if subTaskList?? && (subTaskList?size > 0) ><#list subTaskList as subTask>
					 	<#if subTask.subTaskId == "3" && subTask.state=="1">
						 		 checked="true"
						 	</#if>
						 </#list>
					  </#if>
					
					>
					<span class="subTask" value="3">在商户运营服务群告知商户效果报告已输出并引导商户登录商户管理后台查看</span>
				</label>
			</div>
			<div class="rs-label hasuploadfile">
				<div class="control-group">
					<label style="float: left; line-height: 32px;" value="4">效果报告：</label>
					<div class="controls positionrelative">
						<#if vars?exists>
			                <#list vars?keys as key> 
			                   <#if vars["workEffectReportOutput"]??>
			                   		<#assign workEffectReportOutput = vars["workEffectReportOutput"]>
			                   </#if>
			                </#list>
			          	</#if>
						<#if workEffectReportOutput?exists>
			            	<input type='hidden' id='${taskId!''}workEffectReportOutputPath' name='${taskId!''}workEffectReportOutputPath'  value="${workEffectReportOutput!""}"/>
						 	<!--<input type="file" class="required" id="${taskId!''}effectReportOutputDocument" name="upfile" data-val="${procInsId!''}" file-title="效果报告" order-file-id="${taskId!''}orderFileId90002" order-file-name="${taskId!''}orderFileName90002" />-->
						  	<div class="ajaxFileUploadInputDiv">
								<input id="${taskId!''}showFileNameInput" type="text" readonly="readonly">
								<div id="${taskId!''}pickId" class="filePickIds" data-subTaskId="4">选择文件</div>
							</div>
							<div class="fileProgressDiv">
								<div id="${taskId!''}fileProgressDiv" class="fileProgressWrap"></div>
							</div>
							<#if subTaskList?? && (subTaskList?size > 0) >
								<#list subTaskList as subTask>
							 		<#if subTask.orderFiles?? && (subTask.orderFiles?size > 0)>
										<ul class="fileviews">
								 			<#list subTask.orderFiles as fileinfo>
								 				<#if fileinfo.subTaskId=="4" >
									 		 		<li class="fileli">
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
					  		<!--<input type="file" class="required" id="${taskId!''}effectReportOutputDocument" name="upfile" data-val="${procInsId!''}" file-title="效果报告" order-file-id="${taskId!''}orderFileId90002" order-file-name="${taskId!''}orderFileName90002" />-->
						  	<div class="ajaxFileUploadInputDiv">
								<input id="${taskId!''}showFileNameInput" type="text" readonly="readonly">
								<div id="${taskId!''}pickId" class="filePickIds" data-subTaskId="4">选择文件</div>
							</div>
							<div class="fileProgressDiv">
								<div id="${taskId!''}fileProgressDiv" class="fileProgressWrap"></div>
							</div>
							<#if subTaskList?? && (subTaskList?size > 0) >
								<#list subTaskList as subTask>
							 		<#if subTask.orderFiles?? && (subTask.orderFiles?size > 0)>
										<ul class="fileviews">
								 			<#list subTask.orderFiles as fileinfo>
								 				<#if fileinfo.subTaskId=="4" >
									 		 		<li class="fileli">
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
			<button style="button" class="btn btn-large"  onclick="submit_${taskId!''}submitForm1053(1)">确定完成</button>
			<span class="flowTaskState hide">已完成</span>
		</div>
	</div>
</div>