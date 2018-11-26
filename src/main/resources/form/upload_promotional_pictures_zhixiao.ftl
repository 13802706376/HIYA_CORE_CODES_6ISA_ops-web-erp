<script type="text/javascript">
	function submit_${taskId!''}submitForm12(isLocation){
		var channels = ''; 
		var channelName = [];
		$('input[name="${taskId!''}channelname12"]:checked').each(function(){ 
			channels+=$(this).val()+","; 
			channelName.push($(this).next('span').text());
		});
		
		var submit = function (v, h, f) {
				if (v == 'ok') {
						$.jBox.tip("正在修改，请稍等...", 'loading', {
						timeout : 3000,
						persistent : true
				});
				$.post("../jyk/flow/accountZhixiao/upload_promotional_pictures_zhixiao", {
						channelList : channels,
						taskId:$("#${taskId!''}taskId12").val(),
						procInsId:$("#${taskId!''}procInsId12").val()
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
		$.jBox.confirm("确认完成上传推广图片素材吗？", "提示", submit);
	}
</script>
<input type='hidden' id='${taskId!''}taskId12' name='taskIdTest' value="${taskId!''}"/>
<input type='hidden' id='${taskId!''}procInsId12' name='procInsIdTest'  value="${procInsId!''}"/>
<div class="act-rs-item  ${detailType!''}  ${followTaskDetailWrap!''}">
	<div class="act-rs-left floatleft">
		<div class="act-rs-title padding15">
				<h3><a href="../workflow/taskDetail?taskId=${taskId!''}&followTaskDetailWrap=${followTaskDetailWrap!''}&detailType=${detailType!''}">${taskName!''}</a></h3>
			<div class="listSubmit">
				<!--
				<button  onclick="submit_${taskId!''}submitForm12()" type="button" class="makesrue btn">确定完成</button>
				<a href="javascript:;" class="select-user open-select-user" style="float: left;" onclick="syncSelect('${taskId!''}', '${taskUser!''}','${taskUserId!''}')"></a>
				-->
			</div>
			<div class="taskPrincipal"><span class="taskPrincipalName">${taskUser!''}</span></div>
		</div>
		<div class="act-rs-form padding15">
			<div class="rs-label">
				<label class="subTask" value="1">收集商户的推广图片素材并通过邮件方式发送给策划专家</label>
				<!--<label class="subTask" value="1">提示商户尽快上传推广图片素材</label>
				<a href="javascript:;" class="uploadPromotionalPictures" data-datas="${shopMainId!''},${storeId!''},zhixiao">上传图片素材</a>-->
				<!--
				<label class="rs-label-wrapper">
					<input type="checkbox" <#if (subTaskList[0])?? && subTaskList[0].state == "1">checked="checked"</#if>  value="1" name="${taskId!''}channelname12">
					<span class="subTask" value="1">确认完成上传推广图片素材</span>
				</label>
				-->
			</div>
		</div>
	</div>
	<div class="act-rs-right">
		<div class="padding15">
		<input type="text" style="display:none;"  value="khfckhn"/>
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
			<button style="button" class="btn btn-large btn-primary"  onclick="submit_${taskId!''}submitForm12(1)">确定完成</button>
			<span class="flowTaskState hide">已完成</span>
		</div>
	</div>
</div>