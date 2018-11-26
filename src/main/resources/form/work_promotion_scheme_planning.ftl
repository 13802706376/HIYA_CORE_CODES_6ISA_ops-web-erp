<script type="text/javascript">
function submit_${taskId!''}submitForm1032(isLocation){
	var channels = ''; 
	var channelName =[];
	$('input[name="${taskId!''}channelname1032"]:checked').each(function(){ 
		channels+=$(this).val()+","; 
		channelName.push($(this).next('span').text());
	});

	if(channelName.length === 0 && channels === '') {
		//$.jBox.tip("请勾选与商户确认信息！", 'error');
		return;
	}
	
	var orderFileName="";
	var assignTextDesignInterfacePerson="";
	var assignConsultantInterface="";
	var isFinished=false;
	
	if($("#${taskId!''}workPromotionSchemePlanningFile").length > 0){
		orderFileName=$("#${taskId!''}workPromotionSchemePlanningFile").val();
	}
	if($("#${taskId!''}workPromotionSchemePlanningFile").length <= 0  && $("#${taskId!''}orderFileId10002").val() !=""){
		orderFileName=$("#${taskId!''}orderFileId10002").val();
	}
	
	if($("#${taskId!''}textDesignInterfacePerson").length > 0){
		assignTextDesignInterfacePerson=$("#${taskId!''}textDesignInterfacePerson").val();
	}else{
		assignTextDesignInterfacePerson=$("#${taskId!''}assignTextDesignInterfacePerson option:selected").val();
	}
	
	if($("#${taskId!''}consultantInterface").length > 0){
		assignConsultantInterface=$("#${taskId!''}consultantInterface").val();
	}else{
		assignConsultantInterface=$("#${taskId!''}assignConsultantInterface option:selected").val();
	}
	
	if(orderFileName!="" && assignTextDesignInterfacePerson !="" && assignConsultantInterface!="" && channelName.length == 2){
		isFinished=true;	
	}

	if(assignTextDesignInterfacePerson === ""){
		$.jBox.tip("请选择文案设计接口人！", 'error');
		return;
	}
	if(assignConsultantInterface === ""){
		$.jBox.tip("请选择投放顾问接口人！", 'error');
		return;
	}
	if(orderFileName === ""){
		$.jBox.tip("请上传聚引客商户内部信息流转表！", 'error');
		return;
	}
	var submit = function (v, h, f) {
			if (v == 'ok') {
					$.jBox.tip("正在修改，请稍等...", 'loading', {
					timeout : 3000,
					persistent : true
			});
			$.post("../jyk/flow/work_promotion_scheme_planning", {
					channelList : channels,
					orderFileId: $.trim($('#${taskId!''}orderFileId10002').val()),
					orderFileName:$.trim($('#${taskId!''}orderFileName10002').val()),
					assignTextDesignInterfacePerson:  $("#${taskId!''}assignTextDesignInterfacePerson option:selected").val(),
					assignConsultantInterface: $("#${taskId!''}assignConsultantInterface option:selected").val(),
					isFinished:isFinished,
					taskId:$("#${taskId!''}taskId1032").val(),
					procInsId:$("#${taskId!''}procInsId1032").val()
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
	$.jBox.confirm("确定完成推广方案策划与创意重点确认吗？", "提示", submit);
}
$(function() {
	//applyAjaxFileUpload("#${taskId!''}ajaxFileUploadInput1032", "../workfile/file/upload");
	applyWebuploader("${taskDefKey!''}", "${procInsId!''}", "聚引客商户内部信息流转表", "../workfile/file/uploads", 
		"${taskId!''}pickId", "${taskId!''}showFileNameInput", "${taskId!''}fileProgressDiv", "${taskId!''}orderFileId10002", "${taskId!''}orderFileName10002");
});
</script>
<input type='hidden' id='${taskId!''}taskId1032' name='taskId1032' value="${taskId!''}"/>
<input type='hidden' id='${taskId!''}procInsId1032' name='procInsId1032' value="${procInsId!''}"/>
<input type="hidden" id="${taskId!''}orderFileId10002">
<input type="hidden" id="${taskId!''}orderFileName10002">
<div class="act-rs-item ${detailType!''} ${followTaskDetailWrap!''}">
	<div class="act-rs-left floatleft">
		<div class="act-rs-title padding15">
			<h3><a href="../workflow/taskDetail?taskId=${taskId!''}&followTaskDetailWrap=${followTaskDetailWrap!''}&detailType=${detailType!''}">${taskName!''}</a></h3>
			<div class="listSubmit">
				<button  onclick="submit_${taskId!''}submitForm1032()" type="button" class="makesrue btn">确定完成</button>
				<a href="javascript:;" class="select-user open-select-user" style="float: left;" onclick="syncSelect('${taskId!''}', '${taskUser!''}','${taskUserId!''}')"></a>
			</div>
			<div class="taskPrincipal"><span class="taskPrincipalName">${taskUser!''}</span></div>
		</div>
		<div class="hasuploadfile act-rs-form form-horizontal padding15">
			<div class="rs-label">
				<label class="rs-label-wrapper">
					<input type="checkbox"  value="1" name="${taskId!''}channelname1032"
					 <#if subTaskList?? && (subTaskList?size > 0) ><#list subTaskList as subTask>
					 	<#if subTask.subTaskId == "1" && subTask.state=="1">
						 		 checked="true"
						 	</#if>
						 </#list>
					  </#if>
					>
					<span class="subTask" value="1">与商户确认推广方案</span>
				</label>
			</div>
			<div class="rs-label">
				<label class="rs-label-wrapper">
					<input type="checkbox" value="2" name="${taskId!''}channelname1032"
						<#if subTaskList?? && (subTaskList?size > 0) ><#list subTaskList as subTask>
					 	<#if subTask.subTaskId == "2" && subTask.state=="1">
						 		 checked="true"
						 	</#if>
						 </#list>
					  </#if>
					>
					<span class="subTask" value="2">与商户确认创意重点</span>
				</label>
			</div>
			<div class="rs-label">
				<div class="control-group">
					<label class="subTask" value="3" style="width:190px;">指派文案设计接口人：</label>
					<div class="controls">
						<#if vars?exists>
		                <#list vars?keys as key> 
		                   <#if vars["textDesignInterfacePerson"]??>
		                   		<#assign textDesignInterfacePerson = vars["textDesignInterfacePerson"]>
		                   </#if>
		                </#list>
		          	  </#if>
		            	<#-- <#if textDesignInterfacePerson?exists>
		            		  ${UserUtils.get(textDesignInterfacePerson).name} 
			            	 <input type='hidden' id='${taskId!''}textDesignInterfacePerson' name='${taskId!''}textDesignInterfacePerson' value="${textDesignInterfacePerson!""}" />
			            <#else> -->
							 <#if taskUserList?? && (taskUserList?size > 0) >
							 	 <select id="${taskId!''}assignTextDesignInterfacePerson" class="input-medium">
									<option value="" label="请选择">请选择</option>
									<#list taskUserList as user>
										<option value="${user.id!''}" label="${user.name!''}" <#if textDesignInterfacePerson?? && user.name==UserUtils.get(textDesignInterfacePerson).name>selected="selected"</#if>>${user.name!''}</option>
									</#list>
								</select>	
							 <#else>
								<font color="red">指派文案设计接口人</font> 
							 </#if>
						<#-- </#if> -->
					</div>
				</div>
			</div>
			<div class="rs-label">
				<div class="control-group">
					<label class="subTask" value="4" style="width:190px;">指派投放顾问接口人：</label>
					<div class="controls">
					<#if vars?exists>
		                <#list vars?keys as key> 
		                   <#if vars["consultantInterface"]??>
		                   		<#assign consultantInterface = vars["consultantInterface"]>
		                   </#if>
		                    <#if vars["workPromotionSchemePlanningFile"]??>
		                   		<#assign workPromotionSchemePlanningFile = vars["workPromotionSchemePlanningFile"]>
		                   </#if>
		                </#list>
		          	  </#if>
		            	 <#-- <#if consultantInterface?exists>
		            	  	 ${UserUtils.get(consultantInterface).name} 
			            	 <input type='hidden' id='${taskId!''}consultantInterface' name='${taskId!''}consultantInterface' value="${consultantInterface!""}" />
			            <#else> -->
							 <#if taskUserList2?? && (taskUserList2?size > 0) >
							 	 <select id="${taskId!''}assignConsultantInterface" class="input-medium">
									<option value="" label="请选择">请选择</option>
									<#list taskUserList2 as user>
										<option value="${user.id!''}" label="${user.name!''}" <#if consultantInterface?? && user.name==UserUtils.get(consultantInterface).name>selected="selected"</#if>>${user.name!''}</option>
									</#list>
								</select>	
							 <#else>
								<font color="red">指派投放顾问接口人</font> 
							 </#if>
						 <#-- </#if> -->
					</div>
				</div>
			</div>
			<div class="rs-label hasuploadfile">
				<div class="control-group">
					<label class="subTask" value="5" style="width:190px;">聚引客商户内部信息流转表：</label>
					<div class="controls positionrelative">
				 		<#if workPromotionSchemePlanningFile?exists>
			            	<input type='hidden' id='${taskId!''}workPromotionSchemePlanningFile' name='${taskId!''}workPromotionSchemePlanningFile'  value="${workPromotionSchemePlanningFile!""}"/>
			            	<!--<input type="file" class="required" id="${taskId!''}ajaxFileUploadInput1032" name="upfile" data-val="${procInsId!''}" file-title="聚引客商户内部信息流转表" order-file-id="${taskId!''}orderFileId10002" order-file-name="${taskId!''}orderFileName10002" />-->
							<div class="ajaxFileUploadInputDiv">
								<input id="${taskId!''}showFileNameInput" type="text" readonly="readonly">
								<div id="${taskId!''}pickId" class="filePickIds" data-subTaskId="5">选择文件</div>
							</div>
							<div class="fileProgressDiv">
								<div id="${taskId!''}fileProgressDiv" class="fileProgressWrap"></div>
							</div>
							<#if subTaskList?? && (subTaskList?size > 0) >
								<#list subTaskList as subTask>
							 		<#if subTask.orderFiles?? && (subTask.orderFiles?size > 0)>
										<ul class="fileviews">
								 			<#list subTask.orderFiles as fileinfo>
								 				<#if fileinfo.subTaskId=="5" >
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
						  	<!--<input type="file" class="required" id="${taskId!''}ajaxFileUploadInput1032" name="upfile" data-val="${procInsId!''}" file-title="聚引客商户内部信息流转表" order-file-id="${taskId!''}orderFileId10002" order-file-name="${taskId!''}orderFileName10002" />-->
							<div class="ajaxFileUploadInputDiv">
								<input id="${taskId!''}showFileNameInput" type="text" readonly="readonly">
								<div id="${taskId!''}pickId" class="filePickIds" data-subTaskId="5">选择文件</div>
							</div>
							<div class="fileProgressDiv">
								<div id="${taskId!''}fileProgressDiv" class="fileProgressWrap"></div>
							</div>
							<#if subTaskList?? && (subTaskList?size > 0) >
								<#list subTaskList as subTask>
							 		<#if subTask.orderFiles?? && (subTask.orderFiles?size > 0)>
										<ul class="fileviews">
								 			<#list subTask.orderFiles as fileinfo>
								 				<#if fileinfo.subTaskId=="5" >
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
			<button style="button" class="btn btn-large"  onclick="submit_${taskId!''}submitForm1032(1)">确定完成</button>
			<span class="flowTaskState hide">已完成</span>
		</div>
	</div>
</div>