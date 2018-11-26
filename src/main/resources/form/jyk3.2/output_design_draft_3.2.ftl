<script type="text/javascript">

function submit_${taskId!''}submitForm1014(isLocation){
	
	
 	var momos =	$("[data-fid='orderFileIdMomo']");
 	var weibo =	$("[data-fid='orderFileIdWeibo']");
 	var friends =	$("[data-fid='orderFileIdFriends']");
 	
 	var pickIdFriends = [];
 	var pickIdWeibo = [];
 	var pickIdMomo = [];
 	
 
	momos.each(function(){
		pickIdMomo.push($(this).attr("data-fileid"));
	});
	weibo.each(function(){
		pickIdWeibo.push($(this).attr("data-fileid"));
	});
	friends.each(function(){
		pickIdFriends.push($(this).attr("data-fileid"));
	});
	console.log(pickIdFriends);
	console.log(pickIdWeibo);
	console.log(pickIdMomo);
	
	var submit = function (v, h, f) {
			if (v == 'ok') {
					$.jBox.tip("正在修改，请稍等...", 'loading', {
					timeout : 3000,
					persistent : true
			});
			$.post("../jyk/flow/3p2/output_design_draft_3.2", {
					taskId:$("#${taskId!''}taskId1014").val(),
					procInsId:$("#${taskId!''}procInsId1014").val(),
					splitId:$("#${taskId!''}splitId1014").val(),
					pickIdFriends: pickIdFriends.toString(),
					pickIdWeibo: pickIdWeibo.toString(),
					pickIdMomo: pickIdMomo.toString(),
					momoLink:$("#${taskId!''}momoLink1014").val()
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
	$.jBox.confirm("确认提交吗？", "提示", submit);
	
}
</script>
<input type='hidden' id='${taskId!''}taskId1014' name='${taskId!''}taskId1014' value="${taskId!''}"/>
<input type='hidden' id='${taskId!''}splitId1014' name='${taskId!''}splitId1014' value="${splitId!''}"/>
<input type='hidden' id='${taskId!''}procInsId1014' name='${taskId!''}procInsId1014'  value="${procInsId!''}"/>
<#if !isTaskDetail??>
<div class="act-rs-item  ${detailType!''}  ${followTaskDetailWrap!''}">
	<div class="act-rs-left <#if !isTaskDetail??>floatleft<#else>isisTaskDetail</#if>">
		<div class="act-rs-title padding15">
				<h3>
					<a href="../workflow/taskDetail?taskId=${taskId!''}&followTaskDetailWrap=${followTaskDetailWrap!''}&taskUser=${taskUser!''}
					&taskDefKey=${taskDefKey!''}&splitId=${splitId!''}&startDate=${startDate!''}&endDate=${endDate!''}&procInsId=${procInsId!''}&detailType=${detailType!''}">${taskName!''}</a>
				</h3>
			<div class="listSubmit">
				<a href="javascript:;" class="select-user open-select-user" style="float: left;" onclick="syncSelect('${taskId!''}', '${taskUser!''}','${taskUserId!''}')"></a>
			</div>
			<div class="taskPrincipal"><span class="taskPrincipalName">${taskUser!''}</span></div>
		</div>
	</div>
</div>
<#else>

		<#if vars?exists>
		        <#list vars?keys as key> 
		           <#if vars["chooseMomoFlag"]??>
		              <#assign chooseMomoFlag = vars["chooseMomoFlag"]>
		           </#if>
		           <#if vars["chooseFriendFlag"]??>
				       <#assign chooseFriendFlag = vars["chooseFriendFlag"]>
				   </#if>
				    <#if vars["chooseMicroblogFlag"]??>
		              <#assign chooseMicroblogFlag = vars["chooseMicroblogFlag"]>
		           </#if>
		         </#list>
		    </#if>

<div class="task-detail-wrapper">
	<h3 class="task-title">输出设计稿</h3>
	<div class="task-sider">
		<div class="task-sider-subtitle"><span>相关资料</span></div>
		<div class="task-sider-content">
			<dl class="content-wrap">
				<dt>经营诊断与策划（设计）:</dt>
				<dd><a href="javascript:;">查看</a></dd>
			</dl>
		</div>
	</div>
	
	<#if chooseFriendFlag?? && chooseFriendFlag =='Y' >
	<div class="task-sider">
		<div class="task-sider-title"><span>输出朋友圈设计稿</span></div>
		<div class="task-sider-subtitle"><span>任务操作</span></div>
		<div class="task-sider-content">
			<dl class="content-wrap">
				<dt>朋友圈设计稿：</dt>
				<dd class="margin-left-120 positionrelative">
					<div class="ajaxFileUploadInputDiv">
						<input id="showFileNameInputFriends" type="text" readonly="readonly">
						<div id="pickIdFriends" class="filePickIds" data-subTaskId="1">选择文件</div>
					</div>
					<div class="fileProgressDiv">
						<div id="fileProgressDivFriends" class="fileProgressWrap">
							<#if (flowdata.pickIdFriends)?? && (flowdata.pickIdFriends?size > 0) >								
								<#list flowdata.pickIdFriends as fileinfo>								
									<div class="uploadfileprogress">
											<span class="propgressfilename" title="${fileinfo.fileName}">${fileinfo.fileName}</span>
											<span class="fileProgress" title="已上传100%." style="width:100%"></span>
											<span class="fileProgressTip">100%</span>
											<a href="javascript:;" class="filedelete icon-trash" data-fid="orderFileIdMomo" data-fname="${fileinfo.fileName}" data-input="showFileNameInputFriends" data-filename="${fileinfo.fileName}" data-fileid="${fileinfo.id}" title="删除"></a>
									</div>		
								</#list>						
						</#if>
						</div>
					</div>
				</dd>
			</dl>
		</div>
	</div>
	</#if>
	
	<#if chooseMicroblogFlag?? && chooseMicroblogFlag =='Y' >
	<div class="task-sider">
		<div class="task-sider-title"><span>输出微博设计稿</span></div>
		<div class="task-sider-subtitle"><span>任务操作</span></div>
		<div class="task-sider-content">
			<dl class="content-wrap">
				<dt>微博设计稿：</dt>
				<dd class="margin-left-120 positionrelative">
					<div class="ajaxFileUploadInputDiv">
						<input id="showFileNameInputWeibo" type="text" readonly="readonly">
						<div id="pickIdWeibo" class="filePickIds" data-subTaskId="1">选择文件</div>
					</div>
					<div class="fileProgressDiv">
						<div id="fileProgressDivWeibo" class="fileProgressWrap">
							<#if (flowdata.pickIdWeibo)?? && (flowdata.pickIdWeibo?size > 0) >								
								<#list flowdata.pickIdWeibo as fileinfo>								
									<div class="uploadfileprogress">
											<span class="propgressfilename" title="${fileinfo.fileName}">${fileinfo.fileName}</span>
											<span class="fileProgress" title="已上传100%." style="width:100%"></span>
											<span class="fileProgressTip">100%</span>
											<a href="javascript:;" class="filedelete icon-trash" data-fid="orderFileIdWeibo" data-fname="${fileinfo.fileName}" data-input="showFileNameInputFriends" data-filename="${fileinfo.fileName}" data-fileid="${fileinfo.id}" title="删除"></a>
									</div>		
								</#list>						
						</#if>
						</div>
					</div>
				</dd>
			</dl>
		</div>
	</div>
	</#if>
	
	<#if chooseMomoFlag?? && chooseMomoFlag =='Y' >
	<div class="task-sider">
		<div class="task-sider-title"><span>输出陌陌设计稿</span></div>
		<div class="task-sider-subtitle"><span>任务操作</span></div>
		<div class="task-sider-content">
			<dl class="content-wrap">
				<dt>陌陌设计稿：</dt>
				<dd class="margin-left-120 positionrelative">
					<div class="ajaxFileUploadInputDiv">
						<input id="showFileNameInputMomo" type="text" readonly="readonly">
						<div id="pickIdMomo" class="filePickIds" data-subTaskId="1">选择文件</div>
					</div>
					<div class="fileProgressDiv">
						<div id="fileProgressDivMomo" class="fileProgressWrap">
						<#if (flowdata.pickIdMomo)?? && (flowdata.pickIdMomo?size > 0) >								
								<#list flowdata.pickIdMomo as fileinfo>								
									<div class="uploadfileprogress">
											<span class="propgressfilename" title="${fileinfo.fileName}">${fileinfo.fileName}</span>
											<span class="fileProgress" title="已上传100%." style="width:100%"></span>
											<span class="fileProgressTip">100%</span>
											<a href="javascript:;" class="filedelete icon-trash" data-fid="orderFileIdFriends" data-fname="${fileinfo.fileName}" data-input="showFileNameInputFriends" data-filename="${fileinfo.fileName}" data-fileid="${fileinfo.id}" title="删除"></a>
									</div>		
								</#list>						
						</#if>
						</div>
					</div>
				</dd>
			</dl>
			<dl class="content-wrap">
				<dt>陌陌落地页链接：</dt>
				<dd class="margin-left-120">
					<label class="content-label"><input id="${taskId!''}momoLink1014" <#if (flowdata.momoLink)??>value="${flowdata.momoLink}"</#if> type="text" value="" name="momoLink"></label>
				</dd>
			</dl>
		</div>
		</#if>
		
	</div>
	<div class="task-detail-footer">
		<div class="footer-info">
			<p>负责人：${taskUser!''}</p>
			<p>${startDate!''} —— ${endDate!''}</p>
		</div>
		<div class="footer-btn">
			<button type="button" class="btn btn-large" onclick="submit_${taskId!''}submitForm1014(1)">确定完成</button>
		</div>
	</div>
</div>
</#if>
<input type="hidden" id="orderFileIdFriends">
<input type="hidden" id="orderFileNameFriends">
<input type="hidden" id="orderFileIdWeibo">
<input type="hidden" id="orderFileNameWeibo">
<input type="hidden" id="orderFileIdMomo">
<input type="hidden" id="orderFileNameMomo">
<script type="text/javascript">
	var task_flow_version = '3.2';
	$(function() {
		var outputDesignDraft = function() {
			var applyUploader = function() {
				applyWebuploader("${taskDefKey!''}", "${procInsId!''}", "朋友圈设计稿", "../workfile/file/uploads", "pickIdFriends", "showFileNameInputFriends", "fileProgressDivFriends", "orderFileIdFriends", "orderFileNameFriends");
				applyWebuploader("${taskDefKey!''}", "${procInsId!''}", "微博设计稿", "../workfile/file/uploads", "pickIdWeibo", "showFileNameInputWeibo", "fileProgressDivWeibo", "orderFileIdWeibo", "orderFileNameWeibo");
				applyWebuploader("${taskDefKey!''}", "${procInsId!''}", "陌陌设计稿", "../workfile/file/uploads", "pickIdMomo", "showFileNameInputMomo", "fileProgressDivMomo", "orderFileIdMomo", "orderFileNameMomo");
			},
			bindEvent = function() {
				$('body').on('click', '#contentListWrapper .list-label .delete-btn', function(event) {
					event.preventDefault();
					var obj = $(this), parent = $(this).parents('.list-label');
					deleteCoupon(obj, parent);
				});
			},
			init = function() {
				applyUploader();
				bindEvent();
			};

			return {
				init: init
			};
		}();

		outputDesignDraft.init();
	})
</script>
