<script type="text/javascript">
function submit_${taskId!''}submitForm1008(isLocation){
	var channels = ''; 
	var channelName =[];
	$('input[name="${taskId!''}channelname1008"]:checked').each(function(){ 
		channels+=$(this).val()+","; 
		channelName.push($(this).val());
	});
	if(channelName.length === 0 && channels === '') {
		return;
		$.jBox.tip("请确认与商户电话沟通推广时间！", 'error');
	}

	if($.trim($('input[name="${taskId!''}nextQualificationTime"]').val()) === '' && $('input[name="${taskId!''}channelname1008"]:checked').val() === '2') {
		$.jBox.tip("请选择下次与商户电话沟通推广时间！", 'error');
		return;
	}


	
	var submit = function (v, h, f) {
			if (v == 'ok') {
					$.jBox.tip("正在修改，请稍等...", 'loading', {
					timeout : 3000,
					persistent : true
			});
			$.post("../jyk/flow/channel_confirm_business_qualification", {
					channelList : channels,
					nextQualificationTime: $('input[name="${taskId!''}nextQualificationTime"]').val(),
					taskId:$("#${taskId!''}taskId1008").val(),
					reviewResult:$("#${taskId!''}reviewResult").val(),
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
	$.jBox.confirm("确定执行此次审批吗？", "提示", submit);
}
$(function() {
	

	 <#if promotionTime??> <#else> <#if nextQualificationTime??> 
		 $("#${taskId!''}nosurepromotionTime").show();
	 	alert("111");
	 </#if> </#if>
	 <#if promotionTime??> checked="checked"   $("#${taskId!''}surepromotionTime").show();</#if> 
		$('body').on('change', '.${taskId!''}selectpromotionTime', function(event) {
				if ($(this).val() === '2') {
					
					$("#${taskId!''}nosurepromotionTime").show();
				} else {
					
					$("#${taskId!''}nosurepromotionTime").hide();
				}
			});
	
});

function yes_${taskId!''}()
{
    //document.getElementById("next").value="";
    $("#${taskId!''}reviewResult").val("yes");
}
function not_${taskId!''}()
{
    //document.getElementById("notsure").value="";
    $("#notsure").val("");
    $("#${taskId!''}reviewResult").val("no");
}

</script>
<input type='hidden' id='${taskId!''}taskId1008' name='taskId1008' value="${taskId!''}"/>
<input type='hidden' id='${taskId!''}procInsId1008' name='procInsId1008' value="${procInsId!''}"/>
<input type='hidden' id='${taskId!''}reviewResult' name='reviewResult' value="Y"/>
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
				<#if vars["nextQualificationTime"]??>
					<#assign nextQualificationTime = vars["nextQualificationTime"]>
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
						</#if> type="radio" value="1" name="${taskId!''}channelname1008" class="${taskId!''}selectpromotionTime" onclick="yes_${taskId!''}()">资质齐全
					</label>
					<label>
						<input type="radio" <#if nextQualificationTime??> checked="checked"  </#if>  value="2" name="${taskId!''}channelname1008" class="${taskId!''}selectpromotionTime" onclick="not_${taskId!''}()">资质不齐全,确认下次沟通时间
					</label>
				</div>
			</div>
		
		
			<div class="rs-label hide" id="${taskId!''}nosurepromotionTime">
				<div class="control-group">
					<label style="float: left; line-height: 30px;">下次沟通时间：</label>
					<div class="controls">
						<input name="${taskId!''}nextQualificationTime" id="next" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate" value="${nextQualificationTime!""}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',isShowClear:false,minDate:'%y-%M-%d'});">
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
			$("#${taskId!''}nosurepromotionTime").hide();
		 	<#if nextQualificationTime??> 
			 	$("#${taskId!''}nosurepromotionTime").show();
		 	</#if> 
	</script>
</div>