<script type="text/javascript">
	function submit_${taskId!''}submitForm1002(isLocation){
		var channelVal = '';
		$('input[name="${taskId!''}channelname1002"]:checked').each(function(){ 
			channelVal = $(this).val();
		});
	
		var submit = function (v, h, f) {
				if (v == 'ok') {
						$.jBox.tip("正在修改，请稍等...", 'loading', {
						timeout : 3000,
						persistent : true
				});
				$.post("../payInto/flow/need_union_pay_pay", {
						taskId:$("#${taskId!''}taskId1002").val(),
						channelVal:channelVal,
						procInsId:$("#${taskId!''}procInsId1002").val()
					}, 
				function(data) {
					if (data.result) {
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
		$.jBox.confirm("确定完成商户是否开通银联支付吗？", "提示", submit);
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
					<label class="subTask" value="1">商户是否需要开通银联支付：</label>
				</div>
			</div>
			<div class="rs-label">
				<div class="stdiv">
					<label style="margin-right:50px;">
						<input type="radio" value="1" name="${taskId!''}channelname1002" class="${taskId!''}selectpromotionTime">需要
					</label>
					<label>
						<input type="radio" value="2" name="${taskId!''}channelname1002" class="${taskId!''}selectpromotionTime">不需要
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
			<button style="button" class="btn btn-large"  onclick="submit_${taskId!''}submitForm1002(1)">确定完成</button>
			<span class="flowTaskState hide">已完成</span>
		</div>
	</div>
</div>