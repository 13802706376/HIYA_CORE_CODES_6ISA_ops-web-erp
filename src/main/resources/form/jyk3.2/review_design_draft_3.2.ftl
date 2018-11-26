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
	<h3 class="task-title">审核设计稿</h3>
	<div class="task-sider">
		<div class="task-sider-title"><span>查看推广提案</span></div>
		<div class="task-sider-subtitle"><span>相关资料</span></div>
		<div class="task-sider-content">
			<dl class="content-wrap">
				<dt>经营诊断与策划（文案）:</dt>
				<dd><a href="javascript:;">查看</a></dd>
			</dl>
		</div>
	</div>
	<div class="task-sider">
		<div class="task-sider-title"><span>设计稿是否通过？</span></div>
		<div class="task-sider-subtitle">
			<span>审核内容</span>
			<a href="javascript:;">下载</a>
		</div>
		<div class="task-sider-content">
			<#if chooseFriendFlag?? && chooseFriendFlag =='Y' >
				<dl class="content-wrap">
					<dt class="clear-float">朋友圈设计稿</dt>
					<dd class="margin-left-0">
						<#if (flowdata.pickIdFriends)?? && (flowdata.pickIdFriends?size > 0) >								
									<#list flowdata.pickIdFriends as fileinfo>								
										<p><a href="${fileinfo.filePath}">${fileinfo.fileName}</a></p>		
									</#list>
															
							</#if>
					</dd>
				</dl>
			</#if>
			<#if chooseMicroblogFlag?? && chooseMicroblogFlag =='Y' >
				<dl class="content-wrap">
					<dt class="clear-float">微博设计稿</dt>
					<dd class="margin-left-0">
					<#if (flowdata.pickIdWeibo)?? && (flowdata.pickIdWeibo?size > 0) >								
									<#list flowdata.pickIdWeibo as fileinfo>								
										<p><a href="${fileinfo.filePath}">${fileinfo.fileName}</a></p>		
									</#list>
															
							</#if>
					</dd>
				</dl>
			</#if>
			<#if chooseMomoFlag?? && chooseMomoFlag =='Y' >
				<dl class="content-wrap">
					<dt class="clear-float">陌陌设计稿</dt>
					<dd class="margin-left-0">
					<#if (flowdata.pickIdMomo)?? && (flowdata.pickIdMomo?size > 0) >								
									<#list flowdata.pickIdMomo as fileinfo>								
										<p><a href="${fileinfo.filePath}">${fileinfo.fileName}</a></p>		
									</#list>
															
							</#if>
					</dd>
				</dl>
			</#if>
		</div>
	</div>
	<div class="task-detail-footer">
		<div class="footer-info">
			<p>负责人：${taskUser!''}</p>
			<p>${startDate!''} —— ${endDate!''}</p>
		</div>
		<div class="footer-btn">
			<button type="button" class="btn btn-large btn-warning recharge" onclick="submit_${taskId!''}submitForm1014(1,'N')">审核不通过</button>
			<button type="button" class="btn btn-large rechargeok" onclick="submit_${taskId!''}submitForm1014(1,'Y')">审核通过</button>
		</div>
	</div>
</div>
</#if>
<script type="text/javascript">
	var task_flow_version = '3.2';
	$(function() {
		var reviewDesignDraft = function() {
			var submit = function(text, checkvalue) {
				var subm = function (v, h, f) {
					if (v == 'ok') {
						$.jBox.tip("正在修改，请稍等...", 'loading', {
							timeout : 3000,
							persistent : true
						});
						$.post("../jyk/flow/3p2/review_design_draft_3.2", {
							taskId: $("#${taskId!''}taskId1014").val(),
							procInsId: $("#${taskId!''}procInsId1014").val(),
							splitId: $("#${taskId!''}splitId1014").val(),
							checkvalue: checkvalue,
							reason: text
						}, 
						function(data) {
							if (data.result) {
								if (typeof updatePage === 'function') {
							updatePage();
						} else {
							window.location.href="../workflow/tasklist";
						}
								/*if(isLocation==1){
									if (typeof updatePage === 'function') {
							updatePage();
						} else {
							window.location.href="../workflow/tasklist";
						}
								}else{
									window.location.reload();
								}*/
							} else {
								$.jBox.closeTip();
								$.jBox.info(data.message);
							}
						});
					}
					return true; 
				};
				top.$.jBox.confirm("确认提交吗？", "提示", subm);
			},
			bindEvent = function() {
				$(document).on('click', '.recharge', function(event) {
					event.preventDefault();
					erpShopApp.auditConfirm(function(text) {
						submit(text, 'N')
					})
				}).on('click', '.rechargeok', function(event) {
					event.preventDefault();
					submit(null, 'Y')
				});
			},
			init = function() {
				bindEvent();
			};
			return {
				init: init
			}
		}();
		reviewDesignDraft.init();
	})
</script>