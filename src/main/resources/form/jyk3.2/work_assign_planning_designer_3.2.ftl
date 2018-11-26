<script type="text/javascript">
function submit_${taskId!''}submitForm1017(isLocation){
	var channels =[]; 
	var channelName =[];
	
	var textDesignPersonVars="";
	var designerVars="";
	var isFinished=false;
	
	if($("#${taskId!''}textDesignPersonVars").length > 0){
		textDesignPersonVars=$("#${taskId!''}textDesignPersonVars").val();
	}
	
	if($("#${taskId!''}textDesignPersonVars").length <= 0  && $('#${taskId!''}textDesignPerson option:selected').val() !=""){
		textDesignPersonVars=$("#${taskId!''}textDesignPerson option:selected").val();
	}
		
	if($("#${taskId!''}designerVars").length > 0){
		designerVars=$("#${taskId!''}designerVars").val();
	}
	if($("#${taskId!''}designerVars").length <= 0  && $("#${taskId!''}designer option:selected").val() != ""){
		designerVars=$('#${taskId!''}designer option:selected').val();
	}
	
	if(textDesignPersonVars!="" && designerVars!=""){
		isFinished=true;
	}
	
	if(textDesignPersonVars === "" && designerVars === "") {
		$.jBox.tip("请选择文案策划！", 'error');
		return;
	}
	if(designerVars === "") {
		$.jBox.tip("请选择设计师！", 'error');
		return;
	}

	var submit = function (v, h, f) {
			if (v == 'ok') {
					$.jBox.tip("正在修改，请稍等...", 'loading', {
					timeout : 3000,
					persistent : true
			});
			$.post("../jyk/flow/3p2/work_assign_planning_designer_3.2", {
					textDesignPerson: $('#${taskId!''}textDesignPerson option:selected').val(),
					designer: $('#${taskId!''}designer option:selected').val(),
					isFinished: isFinished,
					taskId: "${taskId!''}",//$("#taskId1017").val(),
					procInsId: "${procInsId!''}"//$("#procInsId1017").val()
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
	$.jBox.confirm("确定指派文案策划、设计师吗？", "提示", submit);
}
</script>
<input type='hidden' id='taskId1017' name='taskId1017' value="${taskId!''}"/>
<input type='hidden' id='procInsId1017' name='procInsId1017'  value="${procInsId!''}"/>
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
	<h3 class="task-title">指派文案策划、设计师</h3>
	<div class="task-sider">
		<div class="task-sider-subtitle"><span>相关资料</span></div>
		<div class="task-sider-content">
			<dl class="content-wrap">
				<dt>经营诊断与策划</dt>
				<dd><a href="javascript:;">查看</a></dd>
			</dl>
		</div>
	</div>
	<div class="task-sider">
		<div class="task-sider-subtitle"><span>任务操作</span></div>
		<div class="task-sider-content">
			<dl class="content-wrap">
				<dt>指派文案策划：</dt>
				<dd>
					<#if taskUserList?? && (taskUserList?size > 0) >
							 	 <select id="${taskId!''}textDesignPerson" class="input-medium">
									<option value="" label="请选择">请选择</option>
									<#list taskUserList as user>
										<option value="${user.id!''}" label="${user.name!''}" <#if textDesignPerson?? && user.name==UserUtils.get(textDesignPerson).name>selected="selected"</#if>>${user.name!''}</option>
									</#list>
								</select>	
							 <#else>
								<font color="red">指派文案策划</font> 
							 </#if>
				</dd>
			</dl>
			<dl class="content-wrap">
				<dt>指派设计师：</dt>
				<dd>
					<#if taskUserList2?? && (taskUserList2?size > 0) >
						 		 <select id="${taskId!''}designer" class="input-medium">
									<option value="" label="请选择">请选择</option>
									<#list taskUserList2 as user>
										<option value="${user.id!''}" label="${user.name!''}" <#if designer?? && user.name==UserUtils.get(designer).name>selected="selected"</#if>>${user.name!''}</option>
									</#list>
								</select>	
							 <#else>
								<font color="red">指派设计师</font> 
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
			<button type="button" class="btn btn-large"  onclick="submit_${taskId!''}submitForm1017(1)">确定完成</button>
		</div>
	</div>
</div>
</#if>
<script type="text/javascript">
	var task_flow_version = '3.2';
</script>
