<script type="text/javascript">

function submit_${taskId!''}submitForm1014(isLocation){

	
	var submit = function (v, h, f) {
			if (v == 'ok') {
					$.jBox.tip("正在修改，请稍等...", 'loading', {
					timeout : 3000,
					persistent : true
			});
			$.post("../jyk/flow/3p2/promotion_proposal_confirmation_3.2", {
					taskId:$("#${taskId!''}taskId1014").val(),
					procInsId:$("#${taskId!''}procInsId1014").val(),
					splitId:$("#${taskId!''}splitId1014").val(),
					proposalConfirm:$("#${taskId!''}proposalconfirm1014").prop("checked")
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
	$.jBox.confirm("确认提交吗？", "提示", submit);
	
}

function submit_${taskId!''}publishToWxapp1014(isLocation){

	
	var submit = function (v, h, f) {
			if (v == 'ok') {
					$.jBox.tip("正在修改，请稍等...", 'loading', {
					timeout : 3000,
					persistent : true
			});
			$.post("../diagnosis/diagnosisForm/publishToWxapp", {
					taskId:$("#${taskId!''}taskId1014").val(),
					procInsId:$("#${taskId!''}procInsId1014").val(),
					splitId:$("#${taskId!''}splitId1014").val()
				}, 
			function(data) {
				if ("0"===data.code) {
					$.jBox.closeTip();
					$(".publishToWxapp dd").html("已发送");
				} else {
					$.jBox.closeTip();
					$.jBox.info(data.message);
				}
			});
		}
		return true; 
	};
	$.jBox.confirm("确认发布到小程序提交吗？", "提示", submit);
	
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
	<h3 class="task-title">将推广提案发给商户确认</h3>
	<div class="task-sider">
		<div class="task-sider-subtitle"><span>相关资料</span></div>
		<div class="task-sider-content">
			<dl class="content-wrap">
				<dt>经营诊断与策划（商户）:</dt>
				<dd><a href="javascript:;">查看</a></dd>
			</dl>
		</div>
	</div>
	<div class="task-sider">
		<div class="task-sider-subtitle"><span>任务操作</span></div>
		<div class="task-sider-content">
			<dl class="content-wrap publishToWxapp">
				<dt>将推广提案发给商户确认</dt>
				<dd>
				   <#if (publishToWxapp)?? && publishToWxapp == 1> 
						已发送
					<#else>
						<a href="javascript:;" onclick="submit_${taskId!''}publishToWxapp1014(this)">发布到小程序</a>
				    </#if>
				</dd>
			</dl>
			<dl class="content-wrap">
				<dt>
					<label class="content-label"><input type="checkbox" id="${taskId!''}proposalconfirm1014"  <#if (flowdata.proposalconfirm)?? && flowdata.proposalconfirm =='true'> checked="checked" </#if>  name="srue">与商户进行推广提案确认，并确定完成</label>
				</dt>
			</dl>
		</div>
	</div>
	<div class="task-detail-footer">
		<div class="footer-info">
			<p>负责人：${taskUser!''}</p>
			<p>${startDate!''} —— ${endDate!''}</p>
		</div>
		<div class="footer-btn">
			<button type="button" class="btn btn-large"  onclick="submit_${taskId!''}submitForm1014(1)">确定完成</button>
		</div>
	</div>
</div>
</#if>
<script type="text/javascript">
	var task_flow_version = '3.2';
</script>
