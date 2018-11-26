<script type="text/javascript">
function submit_${taskId!''}submitForm1015(isLocation){
	if($.trim($('#${taskId!''}assignConsultant option:selected').val()) === ''){
		return;
		$.jBox.tip("请选择投放顾问！", 'error');
	}
	var submit = function (v, h, f) {
			if (v == 'ok') {
					$.jBox.tip("正在修改，请稍等...", 'loading', {
					timeout : 3000,
					persistent : true
			});
			$.post("../jyk/flow/3p2/assigned_operation_adviser_3.2", {
					assignConsultant : $('#${taskId!''}assignConsultant option:selected').val(),
					taskId:$("#${taskId!''}taskId1015").val(),
					procInsId:$("#${taskId!''}procInsId1015").val()
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
	$.jBox.confirm("确定指派投放顾问吗？", "提示", submit);
}
</script>
<input type='hidden' id='${taskId!''}taskId1015' name='taskId1015' value="${taskId!''}"/>
<input type='hidden' id='${taskId!''}procInsId1015' name='procInsId1015'  value="${procInsId!''}"/>
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
	<h3 class="task-title">指派投放顾问</h3>
	<div class="task-sider">
		<div class="task-sider-subtitle"><span>相关资料</span></div>
		<div class="task-sider-content">
			<dl class="content-wrap">
				<dt>经营诊断与策划（投放顾问）：</dt>
				<dd><a href="javascript:;">查看</a></dd>
			</dl>
		</div>
	</div>
	<div class="task-sider">
		<div class="task-sider-subtitle"><span>任务操作</span></div>
		<div class="task-sider-content">
			<dl class="content-wrap">
				<dt>指派投放顾问：</dt>
				<dd>
					<#if taskUserList?? && (taskUserList?size > 0) >
						 	 <select id="${taskId!''}assignConsultant" class="input-medium">
								<option value="" label="请选择">请选择</option>
								<#list taskUserList as user>
									<option value="${user.id!''}" label="${user.name!''}">${user.name!''}</option>
								</#list>
							</select>	
					<#else>
							<font color="red">指派投放顾问</font> 
					</#if>
				</dd>
			</dl>
		</div>
	</div>
	<div class="task-detail-footer">
		<div class="footer-info">
			<p>负责人：${taskUser!''}</p>
			<p>${startDate!''} —— ${endDate!''}</p>
		</div>
		<div class="footer-btn">
			<button type="button" class="btn btn-large"  onclick="submit_${taskId!''}submitForm1015(1)">确定完成</button>
		</div>
	</div>
</div>
</#if>
<script type="text/javascript">
	var task_flow_version = '3.2';
</script>
