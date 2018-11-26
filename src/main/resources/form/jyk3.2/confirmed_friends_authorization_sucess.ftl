<script type="text/javascript">
function submit_${taskId!''}submitForm1055(isLocation){
	var channels = ''; 
	var channelName = [];
	$('input[name="${taskId!''}channelname1055"]:checked').each(function(){ 
		channels+=$(this).val()+","; 
		channelName.push($(this).next('span').text());
	});
	if(channelName.length === 0 && channels === '') {
		return;
		$.jBox.tip("请确保确认朋友圈授权成功！", 'error');
	}
	var submit = function (v, h, f) {
			if (v == 'ok') {
					$.jBox.tip("正在修改，请稍等...", 'loading', {
					timeout : 3000,
					persistent : true
			});
			$.post("../friends/flow/3p2/confirmed_friends_authorization_sucess", {
					channelList : channels,
					taskId:$("#${taskId!''}taskId1055").val(),
					procInsId:$("#${taskId!''}procInsId1055").val()
				}, 
			function(data) {
				if (data.result) {
					$.jBox.info("确认朋友圈授权成功任务完成.");
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
	$.jBox.confirm("确认完成朋友圈授权成功吗？", "提示", submit);
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
					<input type="checkbox" <#if (subTaskList[0])?? && subTaskList[0].state == "1">checked="checked"</#if>  value="1" name="${taskId!''}channelname1055">
					<span class="subTask" value="1">确认朋友圈授权成功</span>
				</label>
			</div>
		</div>
	</div>
	<div class="act-rs-right">
		<div class="padding15">
			<div class="act-time">开始：${startDate!''}</div>
			<div class="act-time">到期：${endDate!''}</div>
			<div class="act-jd">
				<div class="progress progress-success">
						<div class="bar" style="width: 0%"></div>
					</div>
				</div>
			<!-- <div class="act-jd-word positionrelative"><div class="acjw positionrabsolute" style="left: 80%;">${taskConsumTime!''}%</div></div> -->
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