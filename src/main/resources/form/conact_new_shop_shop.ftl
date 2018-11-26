<script type="text/javascript">
	function submit_${taskId!''}submitForm1055(isLocation){
		var hasYyzz = $('input[name="${taskId!''}hasYyzz"]:checked').val();
		var hasZzqq = $('input[name="${taskId!''}hasZzqq"]:checked').val();
		var channels = ''; 
		var channelName = [];
		$('input[name="${taskId!''}channelname1055"]:checked').each(function(){ 
			channels+=$(this).val()+","; 
			channelName.push($(this).next('span').text());
		});
		if(channelName.length === 0 && channels === '') {
		    $.jBox.tip("请确认联系商户！", 'error');
			return;
		}
		var isFinished = false;
		if(channelName.length === 3 && hasYyzz != '' && hasZzqq != '' 
		       && typeof hasYyzz !== 'undefined' && typeof hasZzqq !== 'undefined') {
			isFinished = true;
		}
	
		
		var submit = function (v, h, f) {
				if (v == 'ok') {
						$.jBox.tip("正在修改，请稍等...", 'loading', {
						timeout : 3000,
						persistent : true
				});
				$.post("../sdi/flow/conact_new_shop_shop", {
						channelList : channels,
						hasYyzz : hasYyzz,//$('input[name="${taskId!''}hasYyzz"]:checked').val(),
						hasZzqq : hasZzqq,//$('input[name="${taskId!''}hasZzqq"]:checked').val(),
						isFinished : isFinished,
						taskId : $("#${taskId!''}taskId1055").val(),
						procInsId : $("#${taskId!''}procInsId1055").val()
					}, 
				function(data) {
					if (data.result) {
						$.jBox.info("联系商户任务完成.");
						if(isLocation==1){
							updatePage();
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
		$.jBox.confirm("确认完成联系新商户吗？", "提示", submit);
	}
</script>
<input type='hidden' id='${taskId!''}taskId1055' name='taskId1055' value="${taskId!''}"/>
<input type='hidden' id='${taskId!''}procInsId1055' name='procInsId1055'  value="${procInsId!''}"/>
<div class="act-rs-item  ${detailType!''}  ${followTaskDetailWrap!''}">
	<div class="act-rs-left floatleft">
		<div class="act-rs-title padding15" id="sub">
			<h3><a href="../workflow/taskDetail?taskId=${taskId!''}&followTaskDetailWrap=${followTaskDetailWrap!''}&detailType=${detailType!''}">${taskName!''}</a></h3>
			<div class="listSubmit">
				<button onclick="submit_${taskId!''}submitForm1055()" type="button" class="makesrue btn">确定完成</button>
				<a href="javascript:;" class="select-user open-select-user" style="float: left;" onclick="syncSelect('${taskId!''}', '${taskUser!''}','${taskUserId!''}')"></a>
			</div>
			<div class="taskPrincipal"><span class="taskPrincipalName">${taskUser!''}</span></div>
		</div>
		<div class="act-rs-form padding15">
			<div class="rs-label">
				<label class="rs-label-wrapper">
					<input type="checkbox" <#if (subTaskList[0])?? && subTaskList[0].state == "1">checked="checked"</#if> value="1" name="${taskId!''}channelname1055">
					<span class="subTask" value="1">电话联系商户，介绍服务内容，获取商户资质情况及微信号</span>
				</label>
			</div>
			
			<div class="rs-label">
				<label class="rs-label-wrapper">
					<span class="subTask" value="2">确认商户是否有营业执照：</span>
					<#if (subTaskList[1])?? && subTaskList[1].remark?? && subTaskList[1].remark == "1">
						<input type="radio" name="${taskId!''}hasYyzz" value="1" class="${taskId!''}hasYyzz" checked="checked"/>是
						<input type="radio" name="${taskId!''}hasYyzz" value="2" class="${taskId!''}hasYyzz"/>否
					<#elseif (subTaskList[1])?? && subTaskList[1].remark?? && subTaskList[1].remark == "2">
						<input type="radio" name="${taskId!''}hasYyzz" value="1" class="${taskId!''}hasYyzz"/>是
						<input type="radio" name="${taskId!''}hasYyzz" value="2" class="${taskId!''}hasYyzz" checked="checked" />否
					<#else>
						<input type="radio" name="${taskId!''}hasYyzz" value="1" class="${taskId!''}hasYyzz"/>是
						<input type="radio" name="${taskId!''}hasYyzz" value="2" class="${taskId!''}hasYyzz"/>否
					</#if>
				</label>
			</div>
			
			<div class="rs-label">
				<label class="rs-label-wrapper">
					<!-- <input type="checkbox" <#if (subTaskList[2])?? && subTaskList[2].state == "1">checked="checked"</#if> value="3" name="${taskId!''}channelname1055"> -->
					<span class="subTask" value="3">确认商户是否资质齐全：</span>
					<#if (subTaskList[2])?? && subTaskList[2].remark?? && subTaskList[2].remark == "1">
						<input type="radio" name="${taskId!''}hasZzqq" value="1" class="${taskId!''}hasZzqq" checked="checked"/>是
						<input type="radio" name="${taskId!''}hasZzqq" value="2" class="${taskId!''}hasZzqq"/>否
					<#elseif (subTaskList[2])?? && subTaskList[2].remark?? && subTaskList[2].remark == "2">
						<input type="radio" name="${taskId!''}hasZzqq" value="1" class="${taskId!''}hasZzqq"/>是
						<input type="radio" name="${taskId!''}hasZzqq" value="2" class="${taskId!''}hasZzqq" checked="checked" />否
					<#else>
						<input type="radio" name="${taskId!''}hasZzqq" value="1" class="${taskId!''}hasZzqq"/>是
						<input type="radio" name="${taskId!''}hasZzqq" value="2" class="${taskId!''}hasZzqq"/>否
					</#if>
				</label>
			</div>
			
			<div class="rs-label">
				<label class="rs-label-wrapper">
					<input type="checkbox" <#if (subTaskList[3])?? && subTaskList[3].state == "1">checked="checked"</#if> value="4" name="${taskId!''}channelname1055">
					<span class="subTask" value="4">服务卡发放：微信群发放服务卡</span>
				</label>
			</div>
			
			<div class="rs-label">
				<label class="rs-label-wrapper">
					<input type="checkbox" <#if (subTaskList[4])?? && subTaskList[4].state?? && subTaskList[4].state == "1">checked="checked"</#if> value="5" name="${taskId!''}channelname1055">
					<span class="subTask" value="5">资料收集：微信群发掌贝智慧服务中心（资料收集小程序）链接，附上小程序登录帐号密码（小程序账号：${zhangbeiId!''},默认密码：${passWord!''}），提醒商户提交所需资料</span>
				</label>
			</div>
		</div>
	</div>
		<div class="act-rs-right" id="taskTime">
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
			<button style="button" class="btn btn-large"  onclick="submit_${taskId!''}submitForm1055(1)">确定完成</button>
			<span class="flowTaskState hide">已完成</span>
		</div>
	</div>
</div>