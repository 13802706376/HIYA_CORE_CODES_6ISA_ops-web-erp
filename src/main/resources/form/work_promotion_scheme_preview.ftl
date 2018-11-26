<script type="text/javascript">
function submit_${taskId!''}submitForm1042(isLocation){
	var isFinished=false;
	
	var friends="";
	var weibo="";
	var momo="";
	var s = ''
	
	if($("#${taskId!''}previewWechatPicPath").length>0){
		friends=$("#${taskId!''}previewWechatPicPath").val();
		s += '1';
	}else{
		friends=$.trim($('#${taskId!''}orderFileId10030').val());
	}
	if($("#${taskId!''}previewWechatPicPath").length==0 && $("#${taskId!''}previewWechatPic").length==0 ){
		friends="noExists";
	}
	
	if($("#${taskId!''}previewWeiboPicPath").length>0){
		weibo=$("#${taskId!''}previewWeiboPicPath").val();
		s += '1';
	}else{
		weibo=$.trim($('#${taskId!''}orderFileId10031').val());
	}
	if($("#${taskId!''}previewWeiboPicPath").length==0 && $("#${taskId!''}previewWeiboPic").length==0 ){
		weibo="noExists";
	}
	
	if($("#${taskId!''}previewMomoPicPath").length>0){
		momo=$("#${taskId!''}previewMomoPicPath").val();
		s += '1';
	}else{
		momo=$.trim($('#${taskId!''}orderFileId10032').val());
	}
	if($("#${taskId!''}previewMomoPicPath").length==0 && $("#${taskId!''}previewMomoPic").length==0 ){
		momo="noExists";
	}
	
	if(friends!="" && weibo!="" && momo!=""){
		isFinished=true;
		s += '1';
	}

	if(s === '') {return;}

	if($('.li-${taskId!''}').size() <= 0 && $('#${taskId!''}showFileNameInput1').size() > 0 && $.trim($('#${taskId!''}orderFileName10030').val()) === "" && $.trim($('#${taskId!''}orderFileId10030').val()) === ""){
		$.jBox.tip("请选择朋友圈推广预览截图！", 'error');
		return;
	}
	if($('.li-${taskId!''}').size() <= 0 && $('#${taskId!''}showFileNameInput2').size() > 0 && $.trim($('#${taskId!''}orderFileName10031').val()) === "" && $.trim($('#${taskId!''}orderFileId10031').val()) === ""){
		$.jBox.tip("请选择微博推广预览截图！", 'error');
		return;
	}
	if($('.li-${taskId!''}').size() <= 0 && $('#${taskId!''}showFileNameInput3').size() > 0 && $.trim($('#${taskId!''}orderFileName10032').val()) === "" && $.trim($('#${taskId!''}orderFileId10032').val()) === ""){
		$.jBox.tip("请选择陌陌推广预览截图！", 'error');
		return;
	}
	var submit = function (v, h, f) {
			if (v == 'ok') {
					$.jBox.tip("正在修改，请稍等...", 'loading', {
					timeout : 3000,
					persistent : true
			});
			$.post("../jyk/flow/work_promotion_scheme_preview", {
					orderFileIdFriends:$.trim($('#${taskId!''}orderFileId10030').val()), 
                    orderFileIdWeibo:$.trim($('#${taskId!''}orderFileId10031').val()),
                    orderFileIdMomo:$.trim($('#${taskId!''}orderFileId10032').val()),
                    orderFileNameFriends:$.trim($('#${taskId!''}orderFileName10030').val()),
                    orderFileNameWeibo:$.trim($('#${taskId!''}orderFileName10031').val()),
                    orderFileNameMomo:$.trim($('#${taskId!''}orderFileName10032').val()), 
					isFinished:isFinished,
					taskId:$("#${taskId!''}taskId1042").val(),
					procInsId:$("#${taskId!''}procInsId1042").val()
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
	$.jBox.confirm("确定上传推广方案预览输出吗？", "提示", submit);
}
$(function() {
	//applyAjaxFileUpload("#${taskId!''}previewWechatPic", "../workfile/file/upload");
	//applyAjaxFileUpload("#${taskId!''}previewWeiboPic", "../workfile/file/upload");
	//applyAjaxFileUpload("#${taskId!''}previewMomoPic", "../workfile/file/upload");
	applyWebuploader("${taskDefKey!''}", "${procInsId!''}", "朋友圈推广预览截图", "../workfile/file/uploads", 
		"${taskId!''}pickId1", "${taskId!''}showFileNameInput1", "${taskId!''}fileProgressDiv1", "${taskId!''}orderFileId10030", "${taskId!''}orderFileName10030");
	applyWebuploader("${taskDefKey!''}", "${procInsId!''}", "微博推广预览截图", "../workfile/file/uploads", 
		"${taskId!''}pickId2", "${taskId!''}showFileNameInput2", "${taskId!''}fileProgressDiv2", "${taskId!''}orderFileId10031", "${taskId!''}orderFileName10031");
	applyWebuploader("${taskDefKey!''}", "${procInsId!''}", "陌陌推广终预览截图", "../workfile/file/uploads", 
		"${taskId!''}pickId3", "${taskId!''}showFileNameInput3", "${taskId!''}fileProgressDiv3", "${taskId!''}orderFileId10032", "${taskId!''}orderFileName10032");
}) 
</script>
<input type='hidden' id='${taskId!''}taskId1042' name='taskId1042' value="${taskId!''}"/>
<input type='hidden' id='${taskId!''}procInsId1042' name='procInsId1042' value="${procInsId!''}"/>
<input type="hidden" id="${taskId!''}orderFileId10030">
<input type="hidden" id="${taskId!''}orderFileName10030">
<input type="hidden" id="${taskId!''}orderFileId10031">
<input type="hidden" id="${taskId!''}orderFileName10031">
<input type="hidden" id="${taskId!''}orderFileId10032">
<input type="hidden" id="${taskId!''}orderFileName10032">
<div class="act-rs-item ${detailType!''} ${followTaskDetailWrap!''}">
	<#if vars?exists>
    	<#list vars?keys as key> 
	       <#if vars["workPromotionPlanPreviewConfirmationIsSuccess"]??>
	          <#assign workPromotionPlanPreviewConfirmationIsSuccess = vars["workPromotionPlanPreviewConfirmationIsSuccess"]>
	       </#if>
	       
	       <#if vars["workPromotionRechargeSuccess"]??>
	          <#assign workPromotionRechargeSuccess = vars["workPromotionRechargeSuccess"]>
	       </#if>
    	</#list>
  	</#if>
	<div class="act-rs-left workpromotionspreview floatleft">
		<div class="act-rs-title padding15">
			<h3><a href="../workflow/taskDetail?taskId=${taskId!''}&followTaskDetailWrap=${followTaskDetailWrap!''}&detailType=${detailType!''}">${taskName!''}
			<#if workPromotionPlanPreviewConfirmationIsSuccess??>
					【退回】
				</#if>
			</a></h3>
			<div class="listSubmit">
				<button onclick="submit_${taskId!''}submitForm1042()" type="button" class="makesrue btn" <#if workPromotionRechargeSuccess?exists && workPromotionRechargeSuccess=="1"><#else>disabled="disabled"</#if>>确定完成</button>
				<a href="javascript:;" class="select-user open-select-user" style="float: left;" onclick="syncSelect('${taskId!''}', '${taskUser!''}','${taskUserId!''}')"></a>
			</div>
			<div class="taskPrincipal"><span class="taskPrincipalName">${taskUser!''}</span></div>
		</div>
		<#if vars?exists>
		     <#list vars?keys as key> 
		        <#if vars["previewWechatPic"]??>
		              <#assign previewWechatPic = vars["previewWechatPic"]>
		        </#if>
		      	<#if vars["previewWeiboPic"]??>
		              <#assign previewWeiboPic = vars["previewWeiboPic"]>
		        </#if>
		        <#if vars["previewMomoPic"]??>
		              <#assign previewMomoPic = vars["previewMomoPic"]>
		         </#if>
		         <#if vars["momo"]??>
		              <#assign momo = vars["momo"]>
		         </#if>
		         <#if vars["weibo"]??>
		             <#assign weibo = vars["weibo"]>
		         </#if>
		         <#if vars["friends"]??>
		              <#assign friends = vars["friends"]>
		         </#if>
		         <#if vars["workPromotionPlanPreviewConfirmationIsSuccess"]??>
		              <#assign workPromotionPlanPreviewConfirmationIsSuccess = vars["workPromotionPlanPreviewConfirmationIsSuccess"]>
		          </#if>
		       </#list>
		 </#if>
		 
		<div class="hasuploadfile form-horizontal act-rs-form padding15">
			<div class="rs-label" style="height:1px;">
				<div class="control-group">
					<label class="subTask"></label>
					<div class="controls"></div>
				</div>
			</div>
			
			<#if workPromotionRechargeSuccess?exists && workPromotionRechargeSuccess=="1" >
				<#if friends?exists && friends=="1" >
					<div class="rs-label hasuploadfile">
						<div class="control-group">
							<label class="subTask" value="1">朋友圈推广预览截图：</label>
							<div class="controls" style="position: relative;">
						 		<#if previewWechatPic?exists>
						  			<#if workPromotionPlanPreviewConfirmationIsSuccess?exists >
										<!--<input type="file" class="required" id="${taskId!''}previewWechatPic" name="upfile" data-val="${procInsId!''}" file-title="朋友圈推广预览截图" order-file-id="${taskId!''}orderFileId10030" order-file-name="${taskId!''}orderFileName10030" />-->
										<div class="ajaxFileUploadInputDiv">
											<input id="${taskId!''}showFileNameInput1" type="text" readonly="readonly">
											<div id="${taskId!''}pickId1" class="filePickIds" data-subTaskId="1">选择文件</div>
										</div>
										<div class="fileProgressDiv">
											<div id="${taskId!''}fileProgressDiv1" class="fileProgressWrap"></div>
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
							            <input type='hidden' id='${taskId!''}previewWechatPicPath' name='${taskId!''}previewWechatPicPath'  value="1"/>
							            <!--<input type="file" class="required" id="${taskId!''}previewWechatPic" name="upfile" data-val="${procInsId!''}" file-title="朋友圈推广预览截图" order-file-id="${taskId!''}orderFileId10030" order-file-name="${taskId!''}orderFileName10030" />-->
										<div class="ajaxFileUploadInputDiv">
											<input id="${taskId!''}showFileNameInput1" type="text" readonly="readonly">
											<div id="${taskId!''}pickId1" class="filePickIds" data-subTaskId="1">选择文件</div>
										</div>
										<div class="fileProgressDiv">
											<div id="${taskId!''}fileProgressDiv1" class="fileProgressWrap"></div>
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
						 		<#else>
									<!--<input type="file" class="required" id="${taskId!''}previewWechatPic" name="upfile" data-val="${procInsId!''}" file-title="朋友圈推广预览截图" order-file-id="${taskId!''}orderFileId10030" order-file-name="${taskId!''}orderFileName10030" />-->
									<div class="ajaxFileUploadInputDiv">
										<input id="${taskId!''}showFileNameInput1" type="text" readonly="readonly">
										<div id="${taskId!''}pickId1" class="filePickIds" data-subTaskId="1">选择文件</div>
									</div>
									<div class="fileProgressDiv">
										<div id="${taskId!''}fileProgressDiv1" class="fileProgressWrap"></div>
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
				</#if>
				<#if weibo?exists && weibo=="2" >
					<div class="rs-label hasuploadfile">
						<div class="control-group">
							<label class="subTask" value="2">微博推广预览截图：</label>
							<div class="controls" style="position: relative;">
						 		<#if previewWeiboPic?exists>
						 	 		<#if workPromotionPlanPreviewConfirmationIsSuccess?exists >
										<!--<input type="file" class="required" id="${taskId!''}previewWeiboPic" name="upfile" data-val="${procInsId!''}" file-title="微博推广预览截图" order-file-id="${taskId!''}orderFileId10031" order-file-name="${taskId!''}orderFileName10031" />-->
										<div class="ajaxFileUploadInputDiv">
											<input id="${taskId!''}showFileNameInput2" type="text" readonly="readonly">
											<div id="${taskId!''}pickId2" class="filePickIds" data-subTaskId="2">选择文件</div>
										</div>
										<div class="fileProgressDiv">
											<div id="${taskId!''}fileProgressDiv2" class="fileProgressWrap"></div>
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
												 	</#if>
													</ul>
										 	</#list>
										</#if>					
							  		<#else>
						           		 <input type='hidden' id='${taskId!''}previewWeiboPicPath' name='${taskId!''}previewWeiboPicPath'  value="1" />
						           		 <!--<input type="file" class="required" id="${taskId!''}previewWeiboPic" name="upfile" data-val="${procInsId!''}" file-title="微博推广预览截图" order-file-id="${taskId!''}orderFileId10031" order-file-name="${taskId!''}orderFileName10031" />-->
										<div class="ajaxFileUploadInputDiv">
											<input id="${taskId!''}showFileNameInput2" type="text" readonly="readonly">
											<div id="${taskId!''}pickId2" class="filePickIds" data-subTaskId="2">选择文件</div>
										</div>
										<div class="fileProgressDiv">
											<div id="${taskId!''}fileProgressDiv2" class="fileProgressWrap"></div>
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
							  		</#if>
						 		<#else>
									<!--<input type="file" class="required" id="${taskId!''}previewWeiboPic" name="upfile" data-val="${procInsId!''}" file-title="微博推广预览截图" order-file-id="${taskId!''}orderFileId10031" order-file-name="${taskId!''}orderFileName10031" />-->
									<div class="ajaxFileUploadInputDiv">
										<input id="${taskId!''}showFileNameInput2" type="text" readonly="readonly">
										<div id="${taskId!''}pickId2" class="filePickIds" data-subTaskId="2">选择文件</div>
									</div>
									<div class="fileProgressDiv">
										<div id="${taskId!''}fileProgressDiv2" class="fileProgressWrap"></div>
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
						 		</#if>
							</div>
						</div>
					</div>
				</#if>
				<#if momo?exists && momo=="3" >
					<div class="rs-label hasuploadfile">
						<div class="control-group">
							<label class="subTask" value="3">陌陌推广预览截图：</label>
							<div class="controls" style="position: relative;">
							 	<#if previewMomoPic?exists>
							  		<#if workPromotionPlanPreviewConfirmationIsSuccess?exists >
										<!--<input type="file" class="required" id="${taskId!''}previewMomoPic" name="upfile" data-val="${procInsId!''}" file-title="陌陌推广预览截图" order-file-id="${taskId!''}orderFileId10032" order-file-name="${taskId!''}orderFileName10032" />-->
										<div class="ajaxFileUploadInputDiv">
											<input id="${taskId!''}showFileNameInput3" type="text" readonly="readonly">
											<div id="${taskId!''}pickId3" class="filePickIds" data-subTaskId="3">选择文件</div>
										</div>
										<div class="fileProgressDiv">
											<div id="${taskId!''}fileProgressDiv3" class="fileProgressWrap"></div>
										</div>
										<#if subTaskList?? && (subTaskList?size > 0) >
											<#list subTaskList as subTask>
										 		<#if subTask.orderFiles?? && (subTask.orderFiles?size > 0)>
													<ul class="fileviews">
											 			<#list subTask.orderFiles as fileinfo>
											 				<#if fileinfo.subTaskId=="3" >
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
						            	<input type='hidden' id='${taskId!''}previewMomoPicPath' name='${taskId!''}previewMomoPicPath'  value="1"/>
						            	<!--<input type="file" class="required" id="${taskId!''}previewMomoPic" name="upfile" data-val="${procInsId!''}" file-title="陌陌推广预览截图" order-file-id="${taskId!''}orderFileId10032" order-file-name="${taskId!''}orderFileName10032" />-->
										<div class="ajaxFileUploadInputDiv">
											<input id="${taskId!''}showFileNameInput3" type="text" readonly="readonly">
											<div id="${taskId!''}pickId3" class="filePickIds" data-subTaskId="3">选择文件</div>
										</div>
										<div class="fileProgressDiv">
											<div id="${taskId!''}fileProgressDiv3" class="fileProgressWrap"></div>
										</div>
										<#if subTaskList?? && (subTaskList?size > 0) >
											<#list subTaskList as subTask>
										 		<#if subTask.orderFiles?? && (subTask.orderFiles?size > 0)>
													<ul class="fileviews">
											 			<#list subTask.orderFiles as fileinfo>
											 				<#if fileinfo.subTaskId=="3" >
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
							 	<#else>
									<!--<input type="file" class="required" id="${taskId!''}previewMomoPic" name="upfile" data-val="${procInsId!''}" file-title="陌陌推广预览截图" order-file-id="${taskId!''}orderFileId10032" order-file-name="${taskId!''}orderFileName10032" />-->
									<div class="ajaxFileUploadInputDiv">
										<input id="${taskId!''}showFileNameInput3" type="text" readonly="readonly">
										<div id="${taskId!''}pickId3" class="filePickIds" data-subTaskId="3">选择文件</div>
									</div>
									<div class="fileProgressDiv">
										<div id="${taskId!''}fileProgressDiv3" class="fileProgressWrap"></div>
									</div>
									<#if subTaskList?? && (subTaskList?size > 0) >
										<#list subTaskList as subTask>
									 		<#if subTask.orderFiles?? && (subTask.orderFiles?size > 0)>
												<ul class="fileviews">
										 			<#list subTask.orderFiles as fileinfo>
										 				<#if fileinfo.subTaskId=="3" >
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
				</#if>
			<#else>
				<font color="red">推广充值未完成，无法处理当前任务.</font>
			</#if>
			<div class="rs-label">
				<div class="control-group">
					<label><input type="checkbox" name="${taskId!''}channelname1042" disabled <#if friends?exists && friends=="1"> checked="checked" </#if>><span>&nbsp;微信朋友圈</span></input></label>
					<label><input type="checkbox" name="${taskId!''}channelname1042" disabled <#if weibo?exists && weibo=="2"> checked="checked" </#if>><span>&nbsp;新浪粉丝通</span></input></label>
					<label><input type="checkbox" name="${taskId!''}channelname1042" disabled <#if momo?exists && momo=="3"> checked="checked" </#if>><span>&nbsp;陌陌</span></input></label>
				</div>
			</div>
			<div class="rs-label">
				<div class="control-group">
					<div><a href="javascript:;" class="selectUploadPreviewPic" data-datas="<#if friends?exists && friends=='1'>Friends,</#if><#if weibo?exists && weibo=='2'>Weibo,</#if><#if momo?exists && momo=='3'>Momo,</#if>" data-sid="${procInsId!''}">上传推广通道投放效果图片</a></div>
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
			<button style="button" class="btn btn-large"  onclick="submit_${taskId!''}submitForm1042(1)">确定完成</button>
			<span class="flowTaskState hide">已完成</span>
		</div>
	</div>
</div>