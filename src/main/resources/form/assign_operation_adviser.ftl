<script type="text/javascript">
function submit_${taskId!''}submitForm1002(isLocation){
	var channels =""; 
	var channelName =[];
	$('input[name="${taskId!''}channelname1002"]:checked').each(function(){ 
		channels+=$(this).val()+","; 
		channelName.push($(this).next('span').text());
	});
	var isFinished=false;
	var adviser = '';
	if($("#${taskId!''}OperationAdviser").length>0){
		adviser=$("#${taskId!''}OperationAdviser").val();
	}
	if($("#${taskId!''}selectOrderId1002").length>0){
		adviser=$("#${taskId!''}selectOrderId1002 option:selected").val();
	}
	if(adviser !="" && channels!=""){
		isFinished=true;	
	}
	if (channelName.length === 0 && adviser === '') {
		return;
	}
	var submit = function (v, h, f) {
			if (v == 'ok') {
					$.jBox.tip("正在提交 ，请稍等...", 'loading', {
					timeout : 3000,
					persistent : true
			});
			$.post("../jyk/flow/assign_operation_adviser", {
					channelList : channels,
					isFinished:isFinished,
					operationAdviser : $("#${taskId!''}selectOrderId1002 option:selected").val(),
					taskId:$("#${taskId!''}taskId1002").val(),
					procInsId:$("#${taskId!''}procInsId1002").val()
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
	$.jBox.confirm("你确定完成【指派运营顾问与建群确认】任务吗？", "提示", submit);
}
</script>
<input type='hidden' id='${taskId!''}taskId1002' name='taskId' value="${taskId!''}"/>
<input type='hidden' id='${taskId!''}procInsId1002' name='procInsId'  value="${procInsId!''}"/>
<div class="act-rs-item  ${detailType!''}  ${followTaskDetailWrap!''}">
	<div class="act-rs-left floatleft">
		<div class="act-rs-title padding15">
			<h3><a href="../workflow/taskDetail?taskId=${taskId!''}&followTaskDetailWrap=${followTaskDetailWrap!''}&detailType=${detailType!''}">${taskName!''}</a></h3>
			<div class="listSubmit">
				<button  onclick="submit_${taskId!''}submitForm1002()" type="button" class="makesrue btn">确定完成</button>
				<a href="javascript:;" class="select-user open-select-user" style="float: left;" onclick="syncSelect('${taskId!''}', '${taskUser!''}','${taskUserId!''}')"></a>
			</div>
			<div class="taskPrincipal"><span class="taskPrincipalName">${taskUser!''}</span></div>
		</div>
		<div class="act-rs-form form-horizontal padding15">
			<div class="rs-label" style="height:50px;">
				<div class="control-group">
					<label class="subTask" value="1">指派运营顾问：
					  <#if vars?exists>
		                <#list vars?keys as key> 
		                   <#if vars["OperationAdviser"]??>
		                   		<#assign OperationAdviser = vars["OperationAdviser"]>
		                   </#if>
		                </#list>
		          	  </#if>
		            	<#-- <#if OperationAdviser?exists>
		            		 ${UserUtils.get(OperationAdviser).name} 
			            	 <input type='hidden' id='${taskId!''}OperationAdviser' name='${taskId!''}OperationAdviser' value="${OperationAdviser!""}" />
			            <#else> -->
				            <#if taskUserList?? && (taskUserList?size > 0) >
								 <select id="${taskId!''}selectOrderId1002" class="input-medium">
									<option value="" label="请选择">请选择</option>
										<#list taskUserList as user>
											<option value="${user.id!''}" label="${user.name!''}" <#if OperationAdviser?? && user.name==UserUtils.get(OperationAdviser).name>selected="selected"</#if>>${user.name!''}</option>
										</#list>
								</select>	
							<#else>
										<font color="red">请设置运营顾问人员</font> 
							</#if>
				        <#-- </#if> -->
					</label>
				</div>
			</div>
			<div class="rs-label">
				<label class="rs-label-wrapper">
					<input type="checkbox"  value="2" name="${taskId!''}channelname1002" 
					  <#if subTaskList?? && (subTaskList?size > 0) ><#list subTaskList as subTask>
					 	<#if subTask.subTaskId == "2" && subTask.state=="1">
						 		 checked="true"
						 	</#if>
						 </#list>
					  </#if>>
					<span class="subTask" value="2">确保商户运营服务群（群成员：运营经理、运营顾问、策划专家）存在</span>
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
			<button style="button" class="btn btn-large"  onclick="submit_${taskId!''}submitForm1002(1)">确定完成</button>
			<span class="flowTaskState hide">已完成</span>
		</div>
	</div>
</div>