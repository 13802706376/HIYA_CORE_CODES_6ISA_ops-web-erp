<script type="text/javascript">
function submit_${taskId!''}submitForm1056(isLocation){
	var channels =[]; 
	var channelName =[];
	var recastNewShopExp = $("#${taskId!''}recastNewShopExp").val();
	var w = recastNewShopExp == 1 ? '' : '不';
	var submit = function (v, h, f) {
			if (v == 'ok') {
				$.jBox.tip("正在修改，请稍等...", 'loading', {
				timeout : 3000,
				persistent : true
			});
			$.post("../jyk/flow/contact_recast_shop", {
					recastNewShopExp : recastNewShopExp,
					taskId:$("#${taskId!''}taskId1056").val(),
					procInsId:$("#${taskId!''}procInsId1056").val()
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
	$.jBox.confirm("确认"+ w +"投放新门店吗？", "提示", submit);
}
</script>
<input type='hidden' id='${taskId!''}taskId1056' name='taskId1056' value="${taskId!''}"/>
<input type='hidden' id='${taskId!''}procInsId1056' name='procInsId1056'  value="${procInsId!''}"/>
<div class="act-rs-item  ${detailType!''}  ${followTaskDetailWrap!''}">
	<div class="act-rs-left floatleft">
		<div class="act-rs-title padding15">
			<h3><a href="../workflow/taskDetail?taskId=${taskId!''}&followTaskDetailWrap=${followTaskDetailWrap!''}&detailType=${detailType!''}">${taskName!''}</a></h3>
			<div class="listSubmit">
				<button  onclick="submit_${taskId!''}submitForm1056()" type="button" class="makesrue btn">确定完成</button>
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
					<label class="subTask" value="1">商户是否投放新门店：</label>
					<div class="controls" style="position: relative;">
						<select id="${taskId!''}recastNewShopExp">
							<option value="1" label="是">是</option>
							<option value="0" label="否">否</option>
						</select>
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
			<button style="button" class="btn btn-large"  onclick="submit_${taskId!''}submitForm1056(1)">确定完成</button>
			<span class="flowTaskState hide">已完成</span>
		</div>
	</div>
</div>