<script type="text/javascript">
	function submit_${taskId!''}submitForm12(isLocation){

		var submit = function (v, h, f) {
				if (v == 'ok') {
						$.jBox.tip("正在修改，请稍等...", 'loading', {
						timeout : 3000,
						persistent : true
				});
				$.post("../jyk/flow/accountZhixiao/contact_shop_zhixiao", {
						isDecide: $('input[name="${taskId!''}license"]:checked').val(),
						taskId : $("#${taskId!''}taskIdTest12").val(),
						procInsId : $("#${taskId!''}procInsIdTest12").val()
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
		$.jBox.confirm("确认朋友圈授权成功吗？", "提示", submit);
	}
	
</script>
<input type='hidden' id='${taskId!''}taskIdTest12' name='taskIdTest' value="${taskId!''}"/>
<input type='hidden' id='${taskId!''}procInsIdTest12' name='procInsIdTest'  value="${procInsId!''}"/>
<div class="act-rs-item  ${detailType!''}  ${followTaskDetailWrap!''}">
	<div class="act-rs-left floatleft">
		<div class="act-rs-title padding15">
				<h3><a href="../workflow/taskDetail?taskId=${taskId!''}&followTaskDetailWrap=${followTaskDetailWrap!''}&detailType=${detailType!''}">${taskName!''}</a></h3>
			<div class="listSubmit">
				<button  onclick="submit_${taskId!''}submitForm12()" type="button" class="makesrue btn">确定完成</button>
				<a href="javascript:;" class="select-user open-select-user" style="float: left;" onclick="syncSelect('${taskId!''}', '${taskUser!''}','${taskUserId!''}')"></a>
			</div>
			<div class="taskPrincipal"><span class="taskPrincipalName">${taskUser!''}</span></div>
		</div>
		<div class="act-rs-form padding15">
			<div class="rs-label">
				<div class="control-group">
					<label style="float: left; line-height: 30px;">以下门店朋友圈授权成功：</label>
					<div class="controls">
						<ul class="storeList">
							<li>
								<a href="/store/basic/erpStoreInfo/urlStoreSynthesize/?shopId=006ecb74dffa45b38737ae56623d63d2&storeId=2d3c3693c562494695d6278720a0c956&isMain=0">
									门店1
								</a>
							</li>
							<li>
								<a href="/store/basic/erpStoreInfo/urlStoreSynthesize/?shopId=006ecb74dffa45b38737ae56623d63d2&storeId=2d3c3693c562494695d6278720a0c956&isMain=0">
									门店2
								</a>
							</li>
							<li>
								<a href="/store/basic/erpStoreInfo/urlStoreSynthesize/?shopId=006ecb74dffa45b38737ae56623d63d2&storeId=2d3c3693c562494695d6278720a0c956&isMain=0">
									门店3
								</a>
							</li>
						</ul>
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
			<button style="button" class="btn btn-large"  onclick="submit_${taskId!''}submitForm12(1)">确定完成</button>
			<span class="flowTaskState hide">已完成</span>
		</div>
	</div>
</div>