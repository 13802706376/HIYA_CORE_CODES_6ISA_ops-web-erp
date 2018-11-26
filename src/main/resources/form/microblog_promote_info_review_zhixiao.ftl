<script type="text/javascript">
	function submit_${taskId!''}submitForm12(isLocation){

		var submit = function (v, h, f) {
				if (v == 'ok') {
						$.jBox.tip("正在修改，请稍等...", 'loading', {
						timeout : 3000,
						persistent : true
				});
				$.post("../jyk/flow/accountZhixiao/microblog_promote_info_review_zhixiao", {
						isPass : $('input[name="${taskId!''}isPass"]:checked').val(),//isPass,
						taskId : $("#${taskId!''}taskIdTest12").val(),
						reason: $("#${taskId!''}reason").val(),
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
		$.jBox.confirm("确认完成微博推广开户资料复审吗？", "提示", submit);
	}
	
	$(function(){
		$(".${taskId!''}isPass").click(function(){
			var isPass = $('input[name="${taskId!''}isPass"]:checked').val();
			if (isPass == '2') {
				var html = '<div class="resonedialogwraper">'+
				'<div class="box">'+
					'<h3 style="padding-left:25px;font-size:16px;color:#353535; font-weight: normal;">驳回原因：</h3>'+
					'<div class="textarea" style="padding-left:25px;"><textarea class="resoneTextarea" style="width:300px; height:150px; resize:none;" placeholder="请填写驳回原因"></textarea></div>'+
				'</div></div>';
				var submits = function(v, h, f) {
					$("#${taskId!''}reason").val($(h).find('.resoneTextarea').val());
					return true;
				};
				top.$.jBox(html, {
				title: '填写驳回原因',
					width: 365,
					submit: submits
				});
			}
		});
	});
</script>
<input type='hidden' id="${taskId!''}taskIdTest12" name='taskIdTest' value="${taskId!''}"/>
<input type='hidden' id="${taskId!''}procInsIdTest12" name='procInsIdTest' value="${procInsId!''}"/>
<input type='hidden' id="${taskId!''}reason" value=""/>
<div class="act-rs-item  ${detailType!''}  ${followTaskDetailWrap!''}">
	<div class="act-rs-left floatleft">
		<div class="act-rs-title padding15">
				<h3><a href="../workflow/taskDetail?taskId=${taskId!''}&followTaskDetailWrap=${followTaskDetailWrap!''}&detailType=${detailType!''}">${taskName!''}</a></h3>
			<div class="listSubmit">
				<button onclick="submit_${taskId!''}submitForm12()" type="button" class="makesrue btn">确定完成</button>
				<a href="javascript:;" class="select-user open-select-user" style="float: left;" onclick="syncSelect('${taskId!''}', '${taskUser!''}','${taskUserId!''}')"></a>
			</div>
			<div class="taskPrincipal"><span class="taskPrincipalName">${taskUser!''}</span></div>
		</div>
		<div class="act-rs-form padding15">
			<div class="rs-label">
				<label class="rs-label-wrapper">
					<span class="subTask" value="1">是否审核通过：</span>
					<input type="radio" name="${taskId!''}isPass" value="1" class="${taskId!''}isPass"/>是
					<input type="radio" name="${taskId!''}isPass" value="2" class="${taskId!''}isPass"/>否
				</label>
			</div>
			<div class="rs-label">
				<label class="rs-label-wrapper">
					<span>选择的门店：${storeName!''}&nbsp;&nbsp;&nbsp;<a href="javascript:;" class="openWeiboForm" data-datas="${storeId!''},zhixiao">查看</a>&nbsp;&nbsp;&nbsp;<a href="../../download/adv/weibo?storeId=${storeId!''}" class="downloadForm">《直销商户微博推广开户资料业管审核表》</a></span>
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
			<button style="button" class="btn btn-large"  onclick="submit_${taskId!''}submitForm12(1)">确定完成</button>
			<span class="flowTaskState hide">已完成</span>
		</div>
	</div>
</div>