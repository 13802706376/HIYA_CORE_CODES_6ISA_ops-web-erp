<script type="text/javascript">
	var task_flow_version = '3.2';
	var is_modify_promotion_proposal = true;
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
	<div class="task-detail-wrapper">
		<h3 class="task-title">修改推广提案</h3>
		<div class="task-sider">
	      	<div class="task-sider-title"><span>修改建议</span></div>
	      	<div class="textarea-wrap">
	         	<textarea class="content-textarea" readonly><#if (flowdata.promotionFailReason)??>${flowdata.promotionFailReason}</#if></textarea>
	      	</div>
	   	</div>
	   	<div class="task-sider">
			<div id="rootNode"></div>
	   </div>
	</div>
</#if>