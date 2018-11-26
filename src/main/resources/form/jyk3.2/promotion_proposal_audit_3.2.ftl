<script type="text/javascript">
var task_flow_version = '3.2';
var is_promotion_proposal_audit = true;
function submit_${taskId!''}submitForm1014(isLocation,flag){
	var subm = function(ras) {
		var submit = function (v, h, f) {
			if (v == 'ok') {
					$.jBox.tip("正在修改，请稍等...", 'loading', {
					timeout : 3000,
					persistent : true
			});
			$.post("../jyk/flow/3p2/promotion_proposal_audit_3.2", {
					taskId:$("#${taskId!''}taskId1014").val(),
					procInsId:$("#${taskId!''}procInsId1014").val(),
					splitId:$("#${taskId!''}splitId1014").val(),
					isPass:flag,
					reason: ras
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
		$.jBox.confirm("确认提交吗？", "提示", submit);
	};

	if (flag === 'N') {
		erpShopApp.auditConfirm(function(ras) {
			subm(ras)
		})
	} else {
		subm()
	}
}
</script>
<input type='hidden' id='${taskId!''}taskId1014' name='${taskId!''}taskId1014' value="${taskId!''}"/>
<input type='hidden' id='${taskId!''}splitId1014' name='${taskId!''}splitId1014' value="${splitId!''}"/>
<input type='hidden' id='${taskId!''}procInsId1014' name='${taskId!''}procInsId1014'  value="${procInsId!''}"/>
<#if !isTaskDetail??>
<div class="act-rs-item  ${detailType!''}  ${followTaskDetailWrap!''}">
	<div class="act-rs-left <#if !isTaskDetail??>floatleft<#else>isisTaskDetail</#if>">
		<div class="act-rs-title padding15">
				<h3>
					<a href="../workflow/taskDetail?taskId=${taskId!''}&followTaskDetailWrap=${followTaskDetailWrap!''}&taskUser=${taskUser!''}
					&taskDefKey=${taskDefKey!''}&splitId=${splitId!''}&startDate=${startDate!''}&endDate=${endDate!''}&procInsId=${procInsId!''}&detailType=${detailType!''}">${taskName!''}</a>
				</h3>
			<div class="listSubmit">
				<a href="javascript:;" class="select-user open-select-user" style="float: left;" onclick="syncSelect('${taskId!''}', '${taskUser!''}','${taskUserId!''}')"></a>
			</div>
			<div class="taskPrincipal"><span class="taskPrincipalName">${taskUser!''}</span></div>
		</div>
	</div>
</div>
<#else>
<div class="task-detail-wrapper">
	<div class="task-sider">
		<div id="rootNode"></div>
   	</div>
   	<div class="task-detail-footer">
      	<div class="footer-btn">
        	<button type="button" class="btn btn-large btn-warning" onclick="submit_${taskId!''}submitForm1014(1,'N')">审核不通过</button>
        	<button type="button" class="btn btn-large" onclick="submit_${taskId!''}submitForm1014(1,'Y')">审核通过</button>
      	</div>
   	</div>
</div>
</#if>
