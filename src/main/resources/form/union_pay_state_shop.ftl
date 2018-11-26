<script type="text/javascript">
	function submit_${taskId!''}submitForm1002(isLocation){
		var channels = ''; 
		var channelName = [];
		$('input[name="${taskId!''}channelname1002"]:checked').each(function(){ 
			channels+=$(this).val()+","; 
			channelName.push($(this).next('span').text());
		});
		if(channelName.length === 0 && channels === '') {
			return;
			$.jBox.tip("请确认完成确认提交银联支付进件！", 'error');
		}
	
		var submit = function (v, h, f) {
				if (v == 'ok') {
						$.jBox.tip("正在修改，请稍等...", 'loading', {
						timeout : 3000,
						persistent : true
				});
				$.post("../sdi/flow/union_pay_state_shop", {
						taskId:$("#${taskId!''}taskId1002").val(),
						procInsId:$("#${taskId!''}procInsId1002").val(),
						channelList:channels
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
		$.jBox.confirm("确定完成确认提交银联支付进件吗？", "提示", submit);
	}
</script>
<input type='hidden' id='${taskId!''}taskId1002' name='taskId' value="${taskId!''}"/>
<input type='hidden' id='${taskId!''}procInsId1002' name='procInsId'  value="${procInsId!''}"/>
<div class="act-rs-item  ${detailType!''}  ${followTaskDetailWrap!''}">
	<div class="act-rs-left floatleft">
		<div class="act-rs-title padding15">
			<h3><a href="../workflow/taskDetail?taskId=${taskId!''}&followTaskDetailWrap=${followTaskDetailWrap!''}&detailType=${detailType!''}">${taskName!''}</a></h3>
			<div class="listSubmit">
				<!--
				<button  onclick="submit_${taskId!''}submitForm1002()" type="button" class="makesrue btn">确定完成</button>
				<a href="javascript:;" class="select-user open-select-user" style="float: left;" onclick="syncSelect('${taskId!''}', '${taskUser!''}','${taskUserId!''}')"></a>
				-->
			</div>
			<div class="taskPrincipal"><span class="taskPrincipalName">${taskUser!''}</span></div>
		</div>
		<div class="act-rs-form form-horizontal padding15">
			<div class="rs-label">
				<label class="rs-label-wrapper">
					<#if storeId??>
						<span class="subTask" value="1">1.对商户提交的<a href="../store/basic/erpStoreInfo/unionpay/?shopId=${shopMainId!''}&storeId=${storeId!''}&isMain=${isMain!''}&from=taskList">银联支付进件资料</a>进行审核，确保资料的完整性和正确性；</span>
					<#else>
						<span class="subTask" value="1">1.对商户提交的<a href="../store/basic/erpStoreInfo/urlErpShopInfoList?id==${shopMainId!''}&from=taskList">银联支付进件资料</a>进行审核，确保资料的完整性和正确性；</span>
					</#if>
				</label>
			</div>
			
			<div class="rs-label">
				<label class="rs-label-wrapper">
					<span class="subTask" value="2">2. 若必须由商户提供的资料不齐全，请与商户沟通并协助商户补全；</span>
				</label>
			</div>
			
			<div class="rs-label">
				<label class="rs-label-wrapper">
					<span class="subTask" value="3">3. 将商户的资料补全，并完成<a  href="../store/basic/erpStoreInfo/payOpen/?id=${shopMainId!''}&from=taskList&type=unionpay">银联支付进件</a>。</span>
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