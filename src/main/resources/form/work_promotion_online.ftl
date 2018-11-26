<script type="text/javascript">
function submit_${taskId!''}submitForm1045(isLocation){
	var channels =[]; 
	var channelName =[];
	var s = '';
	$('input[name="${taskId!''}channelname1045"]:checked').each(function(){ 
		channels+=$(this).val()+","; 
		channelName.push($(this).next('span').text());
		s += '1';
	});
	var friends="";
	var weibo="";
	var momo="";
	var isFinished=false;
	if($("#${taskId!''}onlinePreviewMomoPicPath").length>0){
		s += '1';
		friends=$("#${taskId!''}onlinePreviewMomoPicPath").val();
	}else{
		friends=$.trim($('#${taskId!''}orderFileId70030').val());
	}
	if($("#${taskId!''}onlinePreviewMomoPicPath").length == 0  && $("#${taskId!''}onlinePreviewMomoPic").length == 0){
		friends="noExists";
	}
	
	if($("#${taskId!''}onlinePreviewWeiboPicPath").length>0){
		s += '1';
		weibo=$("#${taskId!''}onlinePreviewWeiboPicPath").val();
	}else{
		weibo=$.trim($('#${taskId!''}orderFileId70031').val());
	}
	if($("#${taskId!''}onlinePreviewWeiboPicPath").length == 0  && $("#${taskId!''}onlinePreviewWeiboPic").length == 0){
		weibo="noExists";
	}
	
	if($("#${taskId!''}onlinePreviewMomoPicPath").length>0){
		s += '1';
		momo=$("#${taskId!''}onlinePreviewMomoPicPath").val();
	}else{
		momo=$.trim($('#${taskId!''}orderFileId70032').val());
	}
	
	if($("#${taskId!''}onlinePreviewMomoPicPath").length == 0  && $("#${taskId!''}onlinePreviewMomoPic").length == 0){
		momo="noExists";
	}
	
	if(friends!="" && weibo!="" && momo!="" && channels!=""){
		isFinished=true;
	}

	if(s === '') {
		return;
	}

	if($('.li-${taskId!''}').size() <= 0 && $('#${taskId!''}showFileNameInput1').size() > 0 && $.trim($('#${taskId!''}orderFileName70030').val()) === "" && $.trim($('#${taskId!''}orderFileId70030').val()) === ""){
		$.jBox.tip("请选择朋友圈推广终稿！", 'error');
		return;
	}
	if($('.li-${taskId!''}').size() <= 0 && $('#${taskId!''}showFileNameInput2').size() > 0 && $.trim($('#${taskId!''}orderFileName70031').val()) === "" && $.trim($('#${taskId!''}orderFileId70031').val()) === ""){
		$.jBox.tip("请选择微博推广终稿！", 'error');
		return;
	}
	if($('.li-${taskId!''}').size() <= 0 && $('#${taskId!''}showFileNameInput3').size() > 0 && $.trim($('#${taskId!''}orderFileName70032').val()) === "" && $.trim($('#${taskId!''}orderFileId70032').val()) === ""){
		$.jBox.tip("请选择陌陌推广终稿！", 'error');
		return;
	}
	
	var submit = function (v, h, f) {
			if (v == 'ok') {
					$.jBox.tip("正在修改，请稍等...", 'loading', {
					timeout : 3000,
					persistent : true
			});
			$.post("../jyk/flow/work_promotion_online", {
					channelList : channels,
					orderFileIdFriends:$.trim($('#${taskId!''}orderFileId70030').val()), 
                    orderFileIdWeibo:$.trim($('#${taskId!''}orderFileId70031').val()),
                    orderFileIdMomo:$.trim($('#${taskId!''}orderFileId70032').val()),
                    orderFileNameFriends:$.trim($('#${taskId!''}orderFileName70030').val()),
                    orderFileNameWeibo:$.trim($('#${taskId!''}orderFileName70031').val()),
                    orderFileNameMomo:$.trim($('#${taskId!''}orderFileName70032').val()), 
					isFinished:isFinished,
					taskId:$("#${taskId!''}taskId1045").val(),
					procInsId:$("#${taskId!''}procInsId1045").val()
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
	$.jBox.confirm("确定推广上线吗？", "提示", submit);
}
$(function() {
	//applyAjaxFileUpload("#${taskId!''}onlinePreviewWechatPic", "../workfile/file/upload");
	//applyAjaxFileUpload("#${taskId!''}onlinePreviewWeiboPic", "../workfile/file/upload");
	//applyAjaxFileUpload("#${taskId!''}onlinePreviewMomoPic", "../workfile/file/upload");
	applyWebuploader("${taskDefKey!''}", "${procInsId!''}", "朋友圈推广终稿预览截图", "../workfile/file/uploads", 
		"${taskId!''}pickId1", "${taskId!''}showFileNameInput1", "${taskId!''}fileProgressDiv1", "${taskId!''}orderFileId70030", "${taskId!''}orderFileName70030");
	applyWebuploader("${taskDefKey!''}", "${procInsId!''}", "微博推广终稿预览截图", "../workfile/file/uploads", 
		"${taskId!''}pickId2", "${taskId!''}showFileNameInput2", "${taskId!''}fileProgressDiv2", "${taskId!''}orderFileId70031", "${taskId!''}orderFileName70031");
	applyWebuploader("${taskDefKey!''}", "${procInsId!''}", "陌陌推广终稿预览截图", "../workfile/file/uploads", 
		"${taskId!''}pickId3", "${taskId!''}showFileNameInput3", "${taskId!''}fileProgressDiv3", "${taskId!''}orderFileId70032", "${taskId!''}orderFileName70032");
})
</script>
<input type='hidden' id='${taskId!''}taskId1045' name='taskId1045' value="${taskId!''}"/>
<input type='hidden' id='${taskId!''}procInsId1045' name='procInsId1045' value="${procInsId!''}"/>
<input type="hidden" id="${taskId!''}orderFileId70030">
<input type="hidden" id="${taskId!''}orderFileName70030">
<input type="hidden" id="${taskId!''}orderFileId70031">
<input type="hidden" id="${taskId!''}orderFileName70031">
<input type="hidden" id="${taskId!''}orderFileId70032">
<input type="hidden" id="${taskId!''}orderFileName70032">

<div class="act-rs-item ${detailType!''} ${followTaskDetailWrap!''}">
	<div class="act-rs-left floatleft">
		<div class="act-rs-title padding15">
			<h3><a href="../workflow/taskDetail?taskId=${taskId!''}&followTaskDetailWrap=${followTaskDetailWrap!''}&detailType=${detailType!''}">${taskName!''}</a></h3>
			<div class="listSubmit">
				<button onclick="submit_${taskId!''}submitForm1045()" type="button" class="makesrue btn">确定完成</button>
				<a href="javascript:;" class="select-user open-select-user" style="float: left;" onclick="syncSelect('${taskId!''}', '${taskUser!''}','${taskUserId!''}')"></a>
			</div>
			<div class="taskPrincipal"><span class="taskPrincipalName">${taskUser!''}</span></div>
		</div>
		<#if vars?exists>
     		<#list vars?keys as key>
     			<#if vars["wechatPaySuccess"]??>
              		<#assign wechatPaySuccess = vars["wechatPaySuccess"]>
	        	</#if>
        		<#if vars["onlinePreviewWechatPic"]??>
              		<#assign onlinePreviewWechatPic = vars["onlinePreviewWechatPic"]>
	        	</#if>
		      	<#if vars["onlinePreviewWeiboPic"]??>
	              	<#assign onlinePreviewWeiboPic = vars["onlinePreviewWeiboPic"]>
		        </#if>
		        <#if vars["onlinePreviewMomoPic"]??>
	              	<#assign onlinePreviewMomoPic = vars["onlinePreviewMomoPic"]>
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
   			</#list>
	 	</#if>
		<div class="hasuploadfile form-horizontal act-rs-form padding15">
			<div class="rs-label" style="height:1px;">
				<div class="control-group">
					<label class="subTask"></label>
					<div class="controls"></div>
				</div>
			</div>
			
			<#if wechatPaySuccess?exists && wechatPaySuccess=="1" >
				<div class="rs-label">
					<label class="rs-label-wrapper">
						<input type="checkbox"  value="1" name="${taskId!''}channelname1045"
							 <#if subTaskList?? && (subTaskList?size > 0) ><#list subTaskList as subTask>
						 	<#if subTask.subTaskId == "1" && subTask.state=="1">
							 		 checked="true" 
							 	</#if>
							 </#list>
						  </#if>
						>
						<span class="subTask" value="1">上线前对推广方案进行检查</span>
					</label>
				</div>
				<#if friends?exists && friends=="1" >
					<div class="rs-label hasuploadfile">
						<div class="control-group">
							<label class="control-label">朋友圈推广终稿：</label>
							<div class="controls positionrelative">
								 <#if onlinePreviewWechatPic?exists>
						            <input type='hidden' id='${taskId!''}onlinePreviewWechatPicPath' name='${taskId!''}onlinePreviewWechatPicPath'  value="1"/>
						            <!--<input type="file" class="p_online_pic required ajaxFileUploadInput" id="${taskId!''}onlinePreviewWechatPic" name="upfile" data-val="${procInsId!''}" file-title="朋友圈推广终稿预览截图" order-file-id="${taskId!''}orderFileId70030" order-file-name="${taskId!''}orderFileName70030" />-->
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
									<!--<input type="file" class="p_online_pic required ajaxFileUploadInput" id="${taskId!''}onlinePreviewWechatPic" name="upfile" data-val="${procInsId!''}" file-title="朋友圈推广终稿预览截图" order-file-id="${taskId!''}orderFileId70030" order-file-name="${taskId!''}orderFileName70030" />-->
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
							<label class="control-label">微博推广终稿：</label>
							<div class="controls positionrelative">
								 <#if onlinePreviewWeiboPic?exists>
						            <input type='hidden' id='${taskId!''}onlinePreviewWeiboPicPath' name='${taskId!''}onlinePreviewWeiboPicPath'  value="1"/>
						            <!--<input type="file" class="p_online_pic required ajaxFileUploadInput" id="${taskId!''}onlinePreviewWeiboPic" name="upfile" data-val="${procInsId!''}" file-title="微博推广终稿预览截图" order-file-id="${taskId!''}orderFileId70031" order-file-name="${taskId!''}orderFileName70031" />-->
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
								 <#else>
									<!--<input type="file" class="p_online_pic required ajaxFileUploadInput" id="${taskId!''}onlinePreviewWeiboPic" name="upfile" data-val="${procInsId!''}" file-title="微博推广终稿预览截图" order-file-id="${taskId!''}orderFileId70031" order-file-name="${taskId!''}orderFileName70031" />-->
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
							<label class="control-label">陌陌推广终稿：</label>
							<div class="controls positionrelative">
								 <#if onlinePreviewMomoPic?exists>
						            <input type='hidden' id='${taskId!''}onlinePreviewMomoPicPath' name='${taskId!''}onlinePreviewMomoPicPath'  value="1"/>
						            <!--<input type="file" class="p_online_pic required ajaxFileUploadInput" id="${taskId!''}onlinePreviewMomoPic" name="upfile" data-val="${procInsId!''}" file-title="陌陌推广终稿预览截图" order-file-id="${taskId!''}orderFileId70032" order-file-name="${taskId!''}orderFileName70032" />-->
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
									<!--<input type="file" class="p_online_pic required ajaxFileUploadInput" id="${taskId!''}onlinePreviewMomoPic" name="upfile" data-val="${procInsId!''}" file-title="陌陌推广终稿预览截图" order-file-id="${taskId!''}orderFileId70032" order-file-name="${taskId!''}orderFileName70032" />-->
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
				<font color="red">微信支付开通未完成，无法处理当前任务.</font>
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
			<button style="button" class="btn btn-large"  onclick="submit_${taskId!''}submitForm1045(1)">确定完成</button>
			<span class="flowTaskState hide">已完成</span>
		</div>
	</div>
</div>