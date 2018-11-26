<script type="text/javascript">
function submit_${taskId!''}submitForm1051(isLocation){
	var audit = '';
	audit = $('input:radio[name="${taskId!''}channelname1051"]:checked').val();
	if(audit === '' || audit == null) {
		return;
	}
	if(audit == "2" && $('#${taskId!''}auditPromotionDataNot').val() == ""){
		$.jBox.tip("请填写驳回原因！", 'error');
		return;
	}
	var submit = function (v, h, f) {
			if (v == 'ok') {
					$.jBox.tip("正在修改，请稍等...", 'loading', {
					timeout : 3000,
					persistent : true
			});
			$.post("../jyk/flow/work_audit_promotion_data", {
					channelList:audit ,
					auditPromotionDataNot: $.trim($("#${taskId!''}auditPromotionDataNot").val()),
					taskId:$("#taskId1051").val(),
					procInsId:$("#procInsId1051").val()
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
	$.jBox.confirm("确定完成审核推广数据吗？", "提示", submit);
}
function fun_${taskId!''}setPlanAuditNot(){
			var html = '<div class="resonedialogwraper">'
				+'<h3 style="padding-left:25px;font-size:16px;color:#353535; font-weight: normal;">驳回原因：</h3>'
				+'<div class="box"><div class="textarea" style="padding-left:25px;"><textarea id="${taskId!''}resoneTextarea" name="resoneTextarea" style="width:300px; height:150px; resize:none;" placeholder="请填写驳回原因"></textarea></div>'
				+'</div></div>';
			var submit = function (v, h, f) {
				var text = $.trim($(h).find('#${taskId!''}resoneTextarea').val());
				$("#${taskId!''}Detail").html(text);
				if (text === '') {
					top.$.jBox.info('请填写驳回原因！');
					return false;
				}
				$('#${taskId!''}auditPromotionDataNot').val(text);
			    return true;
			};
			$.jBox(html, { title: "驳回原因", width: 460, submit: submit});
			console.log($("#${taskId!''}auditPromotionDataNot").val());
}
$(function() {
	$('input:radio[name="${taskId!''}channelname1051"]').click(function(){
 	  var audit=$('input:radio[name="${taskId!''}channelname1051"]:checked').val();
	if(audit == "2" ){
			fun_${taskId!''}setPlanAuditNot();
		}
  	});
});
</script>
<input type='hidden' id='taskId1051' name='taskId1051' value="${taskId!''}"/>
<input type='hidden' id='procInsId1051' name='procInsId1051'  value="${procInsId!''}"/>
<div class="act-rs-item  ${detailType!''}  ${followTaskDetailWrap!''}">
	<div class="act-rs-left floatleft">
		<div class="act-rs-title padding15">
			<h3><a href="../workflow/taskDetail?taskId=${taskId!''}&followTaskDetailWrap=${followTaskDetailWrap!''}&detailType=${detailType!''}">${taskName!''}</a></h3>
			<div class="listSubmit">
				<button onclick="submit_${taskId!''}submitForm1051()" type="button" class="makesrue btn">确定完成</button>
				<a href="javascript:;" class="select-user open-select-user" style="float: left;" onclick="syncSelect('${taskId!''}', '${taskUser!''}','${taskUserId!''}')"></a>
			</div>
			<div class="taskPrincipal"><span class="taskPrincipalName">${taskUser!''}</span></div>
		</div>
		<div class="act-rs-form padding15">
			<div class="rs-label">
				<label class="subTask" value="1">
					商户推广数据汇总表是否通过审核？
				</label>
			</div>
			<div class="rs-label">
				<label class="rs-label-wrapper">
					<input type="radio" value="1" name="${taskId!''}channelname1051">
					<span>通过</span>
				</label>
			</div>
			<div class="rs-label" style="height: auto; min-height: 30px;">
				<label class="rs-label-wrapper" style="float: left;">
					<input type="radio" value="2" name="${taskId!''}channelname1051">
					<span>不通过，驳回原因：<span id="${taskId!''}Detail"></span></span>
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
			<button style="button" class="btn btn-large"  onclick="submit_${taskId!''}submitForm1051(1)">确定完成</button>
			<span class="flowTaskState hide">已完成</span>
		</div>
	</div>
</div>