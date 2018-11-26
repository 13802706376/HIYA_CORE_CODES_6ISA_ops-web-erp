<script type="text/javascript">
function submit_${taskId!''}submitForm(isLocation){
	if($.trim($("#${taskId!''}planningExperts").val()) === ""){
		return false;
		$.jBox.tip("请选择策划专家！", 'error');
	}
	var submit = function (v, h, f) {
			if (v == 'ok') {
				$.jBox.tip("正在提交，请稍等...", 'loading', {
				timeout : 3000,
				persistent : true
			});
			$.post("../jyk/flow/assign_planning_experts", {
					planningExpert: $.trim($("#${taskId!''}planningExperts").val()),
					taskId:$("#${taskId!''}taskId").val(),
					procInsId:$("#${taskId!''}procInsId").val()
				}, 
			function(data) {
				if (data.result) {
					$.jBox.info("指派策划专家成功!");
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
	$.jBox.confirm("确定指派任务给策划专家["+$("#${taskId!''}planningExperts option:selected").text()+"]吗？", "提示", submit);
}

</script>
<input type='hidden' id='${taskId!''}taskId' name='taskId' value="${taskId!''}"/>
<input type='hidden' id='${taskId!''}procInsId' name='procInsId'  value="${procInsId!''}"/>
<div class="act-rs-item ${detailType!''}  ${followTaskDetailWrap!''}">
	<div class="act-rs-left floatleft">
		<div class="act-rs-title padding15">
			<h3><a href="../workflow/taskDetail?taskId=${taskId!''}&followTaskDetailWrap=${followTaskDetailWrap!''}&detailType=${detailType!''}">${taskName!''}</a></h3>
			<div class="listSubmit">
				<button onclick="submit_${taskId!''}submitForm()" type="button" class="makesrue btn">确定完成</button>
				<a href="javascript:;" class="select-user open-select-user" style="float: left;" onclick="syncSelect('${taskId!''}', '${taskUser!''}','${taskUserId!''}')"></a>
				<a href="javascript:;" class="select-user open-select-user" style="float: left;" onclick="syncSelect('${taskId!''}', '${taskUser!''}','${taskUserId!''}')"></a>
			</div>
			<div class="taskPrincipal"><span class="taskPrincipalName">${taskUser!''}</span></div>
		</div>
		<div class="act-rs-form padding15">
			<div class="rs-label">
			<label class="subTask" value="1">指派负责处理订单的策划专家：</label>
			<label >
			 <#if taskUserList?? && (taskUserList?size > 0) >
			 	 <select id="${taskId!''}planningExperts" class="input-medium">
					<option value="" label="请选择">请选择</option>
					<#list taskUserList as user>
						<option <#if (assignee.user)?? && assignee.user.id == user.id>selected="selected"</#if> value="${user.id!''}" label="${user.name!''}">${user.name!''}</option>
					</#list>
				</select>	
			 <#else>
				<font color="red">请设置策划专家人员</font> 
			 </#if>
			
			 </label>
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
			<button style="button" class="btn btn-large"  onclick="submit_${taskId!''}submitForm(1)">确定完成</button>
			<span class="flowTaskState hide">已完成</span>
		</div>
	</div>
</div> 
