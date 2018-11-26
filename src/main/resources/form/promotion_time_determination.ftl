<script type="text/javascript">
function submit_${taskId!''}submitForm1008(isLocation){
	var channel='';
	var channels = ''; 
	var channelName =[];
	$('input[name="${taskId!''}channelname1008"]:checked').each(function(){ 
		channel=$(this).val();
		channels+=$(this).val()+","; 
		channelName.push($(this).val());
	});
	if(channelName.length === 0 && channels === '') {
		return;
		$.jBox.tip("请确认与商户电话沟通推广时间！", 'error');
	}

	if($.trim($('input[name="${taskId!''}promotionNextTime"]').val()) === '' && $('input[name="${taskId!''}channelname1008"]:checked').val() === '2') {
		$.jBox.tip("请选择下次与商户电话沟通推广时间！", 'error');
		return;
	}

	if($.trim($('input[name="${taskId!''}promotionTime"]').val()) === '' && $('input[name="${taskId!''}channelname1008"]:checked').val() === '1') {
		$.jBox.tip("请选择推广时间！", 'error');
		return;
	}
	
	var promotion="";
	var NextTime="";
	if(channel==="2"){
		NextTime=$('input[name="${taskId!''}promotionNextTime"]').val();
	}else if(channel==="1"){
		promotion=$('input[name="${taskId!''}promotionTime"]').val();
	}
	var submit = function (v, h, f) {
			if (v == 'ok') {
					$.jBox.tip("正在修改，请稍等...", 'loading', {
					timeout : 3000,
					persistent : true
			});
			$.post("../jyk/flow/promotion_time_determination", {
					channelList : channels,
					promotionTime: promotion,
					promotionNextTime: NextTime,
					taskId:$("#${taskId!''}taskId1008").val(),
					procInsId:$("#${taskId!''}procInsId1008").val()
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
	$.jBox.confirm("确定完成推广时间确认吗？", "提示", submit);
}
$(function() {
	

	 <#if promotionTime??> <#else> <#if promotionNextTime??> 
		 $("#${taskId!''}nosurepromotionTime").show();
	 	alert("111");
	 </#if> </#if>
	 <#if promotionTime??> checked="checked"   $("#${taskId!''}surepromotionTime").show();</#if> 
		$('body').on('change', '.${taskId!''}selectpromotionTime', function(event) {
				if ($(this).val() === '2') {
					$("#${taskId!''}surepromotionTime").hide();
					$("#${taskId!''}nosurepromotionTime").show();
				} else {
					$("#${taskId!''}nosurepromotionTime").hide();
					$("#${taskId!''}surepromotionTime").show();
				}
			});
	
});


</script>
<input type='hidden' id='${taskId!''}taskId1008' name='taskId1008' value="${taskId!''}"/>
<input type='hidden' id='${taskId!''}procInsId1008' name='procInsId1008' value="${procInsId!''}"/>
<div class="act-rs-item  ${detailType!''}  ${followTaskDetailWrap!''}">
	<div class="act-rs-left floatleft">
		<div class="act-rs-title padding15">
			<h3><a href="../workflow/taskDetail?taskId=${taskId!''}&followTaskDetailWrap=${followTaskDetailWrap!''}&detailType=${detailType!''}">${taskName!''}</a></h3>
			<div class="listSubmit">
				<button onclick="submit_${taskId!''}submitForm1008()" type="button" class="makesrue btn">确定完成</button>
				<a href="javascript:;" class="select-user open-select-user" style="float: left;" onclick="syncSelect('${taskId!''}', '${taskUser!''}','${taskUserId!''}')"></a>
			</div>
			<div class="taskPrincipal"><span class="taskPrincipalName">${taskUser!''}</span></div>
		</div>
		<#if vars?exists>
			<#list vars?keys as key> 
				<#if vars["promotionTime"]??>
					<#assign promotionTime = vars["promotionTime"]>
				</#if>
				<#if vars["promotionNextTime"]??>
					<#assign promotionNextTime = vars["promotionNextTime"]>
				</#if>
			</#list>
		</#if>
		<div class="act-rs-form form-horizontal padding15">
			<div class="rs-label" style="height:1px;">
				<div class="control-group">
					<label class="subTask"></label>
					<div class="controls"></div>
				</div>
			</div>
			<div class="rs-label">
				<div class="stdiv">
					<label style="margin-right:50px;">
						<input <#if promotionTime??> checked="checked"   	
						</#if> type="radio" value="1" name="${taskId!''}channelname1008" class="${taskId!''}selectpromotionTime">确定
					</label>
					<label>
						<input type="radio" <#if promotionTime??> <#else> <#if promotionNextTime??> checked="checked"  </#if> </#if> value="2" name="${taskId!''}channelname1008" class="${taskId!''}selectpromotionTime">不确定
					</label>
				</div>
			</div>
			<div class="rs-label" id="${taskId!''}surepromotionTime">
				<div class="control-group">
					<label style="float: left; line-height: 30px;">推广时间：</label>
					<div class="controls">
						<input id="notsure" name="${taskId!''}promotionTime" type="text" 
						readonly="readonly" maxlength="20" class="input-medium Wdate"  value="${promotionTime!""}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false,minDate:'%y-%M-%d'});">
					</div>
				</div>
			</div>
			<div class="rs-label hide" id="${taskId!''}nosurepromotionTime">
				<div class="control-group">
					<label style="float: left; line-height: 30px;">下次沟通时间：</label>
					<div class="controls">
						<input name="${taskId!''}promotionNextTime" id="next" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate" value="${promotionNextTime!""}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',isShowClear:false,minDate:'%y-%M-%d'});">
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
			<button style="button" class="btn btn-large"  onclick="submit_${taskId!''}submitForm1008(1)">确定完成</button>
			<span class="flowTaskState hide">已完成</span>
		</div>
	</div>
	<script type="text/javascript">	
			$("#${taskId!''}surepromotionTime").hide();
			$("#${taskId!''}nosurepromotionTime").hide();
		 <#if promotionTime??>
		 <#else> 
		 	<#if promotionNextTime??> 
			 	$("#${taskId!''}nosurepromotionTime").show();
		 	</#if> 
		 </#if>
		 <#if promotionTime??> 
		 	 $("#${taskId!''}surepromotionTime").show();
		 </#if> 
	</script>
</div>