<script type="text/javascript">

function submit_${taskId!''}submitForm1014(isLocation){
	

	var submit = function (v, h, f) {
			if (v == 'ok') {
					$.jBox.tip("正在修改，请稍等...", 'loading', {
					timeout : 3000,
					persistent : true
			});
			$.post("../jyk/flow/3p2/promotion_plan_preview_confirmation_3.2", {
					taskId:$("#${taskId!''}taskId1014").val(),
					procInsId:$("#${taskId!''}procInsId1014").val(),
					splitId:$("#${taskId!''}splitId1014").val(),
					confirm: $("#${taskId!''}confirm1014").prop("checked")
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
	<h3 class="task-title">推广页面预览确认</h3>
	<div class="task-sider">
		<div class="task-sider-title"><span>推广页面预览截图</span></div>
		<div class="task-sider-subtitle"><span>相关资料</span><a href="">下载</a></div>
		<#if chooseFriendFlag?? && chooseFriendFlag =='Y' >
		<div class="task-sider-content">
			<div class="task-sider-title"><span>朋友圈推广页面预览截图：</span></div>
			<dl class="content-wrap">
				<dt>外层入口截图：</dt>
				<dd class="margin-left-120">
					<#if (flowdata.outerImgFriends)?? && (flowdata.outerImgFriends?size > 0) >								
								<#list flowdata.outerImgFriends as fileinfo>								
									<p><a href="${fileinfo.filePath}">${fileinfo.fileName}</a></p>		
								</#list>
														
						</#if>
				</dd>
			</dl>
			<dl class="content-wrap">
				<dt>落地页截图：</dt>
				<dd class="margin-left-120">
					<#if (flowdata.innerImgFriends)?? && (flowdata.innerImgFriends?size > 0) >								
								<#list flowdata.innerImgFriends as fileinfo>								
									<p><a href="${fileinfo.filePath}">${fileinfo.fileName}</a></p>		
								</#list>
														
						</#if>
				</dd>
			</dl>
		</div>
		</#if>
		<#if chooseMicroblogFlag?? && chooseMicroblogFlag =='Y' >
		<div class="task-sider-content">
			<div class="task-sider-title"><span>微博推广页面预览截图：</span></div>
			<dl class="content-wrap">
				<dt>外层入口截图：</dt>
				<dd class="margin-left-120">
					<#if (flowdata.outerImgWeibo)?? && (flowdata.outerImgWeibo?size > 0) >								
								<#list flowdata.outerImgWeibo as fileinfo>								
									<p><a href="${fileinfo.filePath}">${fileinfo.fileName}</a></p>		
								</#list>
														
						</#if>
				</dd>
			</dl>
			<dl class="content-wrap">
				<dt>落地页截图：</dt>
				<dd class="margin-left-120">
					<#if (flowdata.innerImgWeibo)?? && (flowdata.innerImgWeibo?size > 0) >								
								<#list flowdata.innerImgWeibo as fileinfo>								
									<p><a href="${fileinfo.filePath}">${fileinfo.fileName}</a></p>		
								</#list>
														
						</#if>
				</dd>
			</dl>
		</div>
		</#if>
		<#if chooseMomoFlag?? && chooseMomoFlag =='Y' >
		<div class="task-sider-content">
			<div class="task-sider-title"><span>陌陌推广页面预览截图：</span></div>
			<dl class="content-wrap">
				<dt>外层入口截图：</dt>
				<dd class="margin-left-120">
					<#if (flowdata.outerImgMomo)?? && (flowdata.outerImgMomo?size > 0) >								
								<#list flowdata.outerImgMomo as fileinfo>								
									<p><a href="${fileinfo.filePath}">${fileinfo.fileName}</a></p>		
								</#list>
														
						</#if>
				</dd>
			</dl>
			<dl class="content-wrap">
				<dt>落地页截图：</dt>
				<dd class="margin-left-120">
					<#if (flowdata.innerImgMomo)?? && (flowdata.innerImgMomo?size > 0) >								
								<#list flowdata.innerImgMomo as fileinfo>								
									<p><a href="${fileinfo.filePath}">${fileinfo.fileName}</a></p>		
								</#list>
														
						</#if>
				</dd>
			</dl>
			<dl class="content-wrap">
				<dt>陌陌落地页链接：</dt>
				<dd class="margin-left-120">
					<a href="${flowdata.momoLink!''}" target="_blank">${flowdata.momoLink!''}</a>
				</dd>
			</dl>
		</div>
		</#if>
	</div>
	<div class="task-sider">
		<div class="task-sider-subtitle"><span>商户反馈</span></div>
		<div class="task-sider-content">
			<p>商户在掌贝智慧服务中心（小程序）里确定推广预览：
				<#if (flowdata.shopPromoteConfirm)?? && flowdata.shopPromoteConfirm =='3'> 已确定
				<#else>
					未确定
				</#if>
			</p>
		</div>
	</div>
	<div class="task-sider">
		<div class="task-sider-subtitle"><span>任务操作</span></div>
		<div class="task-sider-content">
			<div class="checkbox-list">
				<label class="content-label"><input  id="${taskId!''}confirm1014" <#if (flowdata.confirm)?? && flowdata.confirm =='true'> checked="checked" </#if> type="checkbox" name="srue">确定商户通过推广页面预览</label>
			</div>
		</div>
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
<script type="text/javascript">
	var task_flow_version = '3.2';
	$(function() {
		var promotionPlanPreviewConfrim = function() {
			var bindEvent = function() {
				$('body').on('click', '#contentListWrapper .list-label .delete-btn', function(event) {
					
				});
			},
			init = function() {
				bindEvent();
			};

			return {
				init: init
			};
		}();

		promotionPlanPreviewConfrim.init();
	})
</script>