<div class="task-detail-wrapper">
	<h3 class="task-title">投放信息审阅</h3>
	<div class="task-sider">
		<div class="task-sider-title"><span>检查投放信息完整</span></div>
		<div class="task-sider-subtitle"><span>任务操作</span></div>
		<div class="task-sider-content">
			<div class="content-wrap">
				<p>经营诊断与策划方案（投放顾问）：<a href="" class="btn">查看</a></p>
			</div>
		</div>
	</div>
	<div class="task-sider">
		<div class="task-sider-subtitle"><span>任务操作</span></div>
		<div class="task-sider-content">
			<div class="content-wrap">
				<div class="checkbox-list">
				<label class="content-label"><input type="checkbox" name="putInfoCheckBox" id="${taskId!''}putInfoCheckBox1014"  <#if (flowdata.putInfoCheckBox)?? && flowdata.putInfoCheckBox =='true'> 
				  checked="checked" </#if>  name="srue">确认投放信息：</label>	
				</div>
				<div class="textarea-wrap">
					<textarea id="${taskId!''}launchinfo1014" class="content-textarea" disabled="true"><#if (flowdata.launchinfo)?? >${flowdata.launchinfo}</#if></textarea>
				</div>
				<div class="checkbox-list margin-top-20">
					<label class="content-label"><input type="checkbox" name="putInfoCheckBox" id="${taskId!''}promotionTimeCheckBox1014"  <#if (flowdata.promotionTimeCheckBox)?? && flowdata.promotionTimeCheckBox =='true'> 
				  checked="checked" </#if>  name="srue">确认最终推广上线时间</label>	
				</div>
			</div>
			<dl class="content-wrap">
				<dt>最终推广上线时间：</dt>
				<dd class="margin-left-120">
					2018-10-12  15:15
				</dd>
			</dl>
		</div>
	</div>
	<div class="task-detail-footer">
		<div class="footer-info">
			<p>负责人：${taskUser!''}</p>
			<p>${startDate!''} —— ${endDate!''}</p>
		</div>
		<div class="footer-btn">
			<button type="button" class="btn btn-large" onclick="submit_${taskId!''}submitForm1017(1)">确定完成</button>
		</div>
	</div>
</div>
<input type='hidden' id='${taskId!''}splitId1014' name='${taskId!''}splitId1014' value="${splitId!''}"/>
<script type="text/javascript">
	var task_flow_version = '3.2';
	
	function submit_${taskId!''}submitForm1017(isLocation){
		var channels =[]; 
		var channelName =[];
		
		var textDesignPersonVars="";
		var designerVars="";
		var isFinished=false;
		var putInfoCheckBox = $("#${taskId!''}putInfoCheckBox1014").prop("checked");
		var promotionTimeCheckBox = $("#${taskId!''}promotionTimeCheckBox1014").prop("checked");
		if( promotionTimeCheckBox && putInfoCheckBox) {
			isFinished = true;
		}
		
		if (!putInfoCheckBox && !promotionTimeCheckBox) {return;}

		var submit = function (v, h, f) {
			if (v == 'ok') {
					$.jBox.tip("正在修改，请稍等...", 'loading', {
					timeout : 3000,
					persistent : true
			});
			$.post("../jyk/flow/3p2/put_info_review_3.2", {
					isFinished: isFinished,
					taskId: "${taskId!''}",//$("#taskId1017").val(),
					procInsId: "${procInsId!''}",
					splitId:$("#${taskId!''}splitId1014").val(),
					putInfoCheckBox:$("#${taskId!''}putInfoCheckBox1014").prop("checked"),
					promotionTimeCheckBox:$("#${taskId!''}promotionTimeCheckBox1014").prop("checked")
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
	$.jBox.confirm("确定投放信息审阅吗？", "提示", submit);
}
</script>