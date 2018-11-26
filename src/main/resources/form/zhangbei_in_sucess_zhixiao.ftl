<script type="text/javascript">
	
</script>
<input type='hidden' id='${taskId!''}taskId1002' name='taskId' value="${taskId!''}"/>
<input type='hidden' id='${taskId!''}procInsId1002' name='procInsId'  value="${procInsId!''}"/>
<div class="act-rs-item  ${detailType!''}  ${followTaskDetailWrap!''}">
	<div class="act-rs-left floatleft">
		<div class="act-rs-title padding15">
			<h3><a href="../workflow/taskDetail?taskId=${taskId!''}&followTaskDetailWrap=${followTaskDetailWrap!''}&detailType=${detailType!''}">${taskName!''}</a></h3>
			<div class="listSubmit">
				
			</div>
			<div class="taskPrincipal"><span class="taskPrincipalName">${taskUser!''}</span></div>
		</div>
		<div class="act-rs-form form-horizontal padding15">
			<div class="rs-label" style="height:50px;">
				<div class="control-group">
					<label class="subTask" value="1">掌贝进件状态：
						<#if zhangbeiState?? && zhangbeiState==2 >
							<input type="hidden" value="" name="${taskId!''}channelname1002">
							<span><font color="green">掌贝进件审核通过</font></span>
						<#elseif zhangbeiState?? &&  zhangbeiState==3>
							<span><font color="red">掌贝进件审核未通过</font></span>
						<#elseif zhangbeiState?? && (zhangbeiState==1 || zhangbeiState==4)>
							<span><font color="red">掌贝进件待审核</font></span>
						<#else>
							<span><font color="red">掌贝进件待审核</font></span>
						</#if>
					</label>
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
			<button style="button" class="btn btn-large"  onclick="submit_${taskId!''}submitForm1002(1)">确定完成</button>
			<span class="flowTaskState hide">已完成</span>
		</div>
	</div>
</div>