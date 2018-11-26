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
		//$.jBox.tip("请选择文案策划！", 'error');
		return;
	}
	/*if(designerVars === "") {
		$.jBox.tip("请选择设计师！", 'error');
		return;
	}*/

	var submit = function (v, h, f) {
			if (v == 'ok') {
					$.jBox.tip("正在修改，请稍等...", 'loading', {
					timeout : 3000,
					persistent : true
			});
			$.post("../jyk/flow/work_assign_planning_designer", {
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
<div class="act-rs-item  ${detailType!''}  ${followTaskDetailWrap!''}">
	<div class="act-rs-left floatleft">
		<div class="act-rs-title padding15">
			<h3><a href="../workflow/taskDetail?taskId=${taskId!''}&followTaskDetailWrap=${followTaskDetailWrap!''}&detailType=${detailType!''}">${taskName!''}</a></h3>
			<div class="listSubmit">
				<button  onclick="submit_${taskId!''}submitForm1017()" type="button" class="makesrue btn">确定完成</button>
				<a href="javascript:;" class="select-user open-select-user" style="float: left;" onclick="syncSelect('${taskId!''}', '${taskUser!''}','${taskUserId!''}')"></a>
			</div>
			<div class="taskPrincipal"><span class="taskPrincipalName">${taskUser!''}</span></div>
		</div>
		<div class="act-rs-form form-horizontal padding15">
			<div class="rs-label" style="height:1px;">
				<div class="control-group">
					<label class="subTask"></label>
					<div class="controls"></div>
				</div>
			</div>
			<div class="rs-label">
				<div class="control-group">
					<label class="subTask" value="1">指派文案策划：</label>
					<div class="controls">
					 <#if vars?exists>
		                <#list vars?keys as key> 
		                   <#if vars["textDesignPerson"]??>
		                   		<#assign textDesignPerson = vars["textDesignPerson"]>
		                   </#if>
		                </#list>
		          	  </#if>
		            	 <#-- <#if textDesignPerson?exists>
		            		  ${UserUtils.get(textDesignPerson).name} 
			            	 <input type='hidden' id='${taskId!''}textDesignPersonVars' name='${taskId!''}textDesignPersonVars' value="${textDesignPerson!""}" />
			            <#else> -->
							 <#if taskUserList?? && (taskUserList?size > 0) >
							 	 <select id="${taskId!''}textDesignPerson" class="input-medium">
									<option value="" label="请选择">请选择</option>
									<#list taskUserList as user>
										<option value="${user.id!''}" label="${user.name!''}" <#if textDesignPerson?? && user.name==UserUtils.get(textDesignPerson).name>selected="selected"</#if>>${user.name!''}</option>
									</#list>
								</select>	
							 <#else>
								<font color="red">指派投放顾问</font> 
							 </#if>
						<#-- </#if> -->
					</div>
				</div>
			</div>
			<div class="rs-label" style="height:1px;">
				<div class="control-group">
					<label class="subTask"></label>
					<div class="controls"></div>
				</div>
			</div>
			<div class="rs-label">
				<div class="control-group">
					<label class="subTask" value="2">指派设计师：</label>
					<div class="controls">
					 <#if vars?exists>
		                <#list vars?keys as key> 
		                   <#if vars["designer"]??>
		                   		<#assign designer = vars["designer"]>
		                   </#if>
		                </#list>
		          	  </#if>
		            	 <#-- <#if designer?exists>
		            		   ${UserUtils.get(designer).name} 
			            	 <input type='hidden' id='${taskId!''}designerVars' name='${taskId!''}designerVars' value="${designer!""}" />
			            <#else> -->
							 <#if taskUserList2?? && (taskUserList2?size > 0) >
						 		 <select id="${taskId!''}designer" class="input-medium">
									<option value="" label="请选择">请选择</option>
									<#list taskUserList2 as user>
										<option value="${user.id!''}" label="${user.name!''}" <#if designer?? && user.name==UserUtils.get(designer).name>selected="selected"</#if>>${user.name!''}</option>
									</#list>
								</select>	
							 <#else>
								<font color="red">指派投放顾问</font> 
							 </#if>
						  <#-- </#if> -->
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
			<button style="button" class="btn btn-large"  onclick="submit_${taskId!''}submitForm1017(1)">确定完成</button>
			<span class="flowTaskState hide">已完成</span>
		</div>
	</div>
</div>