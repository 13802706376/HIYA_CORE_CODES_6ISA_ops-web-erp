<script type="text/javascript">
	function submit_${taskId!''}submitForm1002(isLocation){
		var submit = function (v, h, f) {
				if (v == 'ok') {
						$.jBox.tip("正在修改，请稍等...", 'loading', {
						timeout : 3000,
						persistent : true
				});
				$.post("../sdi/flow/data_apply_shop", {
						taskId:$("#${taskId!''}taskId1002").val(),
						procInsId:$("#${taskId!''}procInsId1002").val()
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
		
		$.jBox.confirm("确定完成提交掌贝进件吗？", "提示", submit);
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
				<#if zhangbeiState?? && zhangbeiState== 1 >
					<button  onclick="submit_${taskId!''}submitForm1002()" type="button" class="makesrue btn btn-primary">确定完成</button>
					<a href="javascript:;" class="select-user open-select-user" style="float: left;" onclick="syncSelect('${taskId!''}', '${taskUser!''}','${taskUserId!''}')"></a>
				<#else>
					<button  onclick="submit_${taskId!''}submitForm1002()" type="button" class="makesrue btn">确定完成</button>
					<a href="javascript:;" class="select-user open-select-user" style="float: left;" onclick="syncSelect('${taskId!''}', '${taskUser!''}','${taskUserId!''}')"></a>
				</#if>
				-->
			</div>
				<div class="taskPrincipal"><span class="taskPrincipalName">${taskUser!''}</span></div>
		</div>
		
		<div class="act-rs-form padding15">
			<div class="rs-label">
				<label class="rs-label-wrapper">
					<span class="subTask" value="1">1.对商户提交的<a href="../store/basic/erpStoreInfo/urlErpShopInfoList/?id=${shopMainId!''}&form=taskList">掌贝进件资料</a>进行审核，确保资料的完整性和正确性；</span>
				</label>
			</div>
			
			<div class="rs-label">
				<label class="rs-label-wrapper">
					<span class="subTask" value="2">2.若必须由商户提供的资料不齐全，请与商户沟通并协助商户补全；</span>
				</label>
			</div>
			
			<div class="rs-label">
				<label class="rs-label-wrapper">
					<span class="subTask" value="3">3.将商户的资料补全，并完成<a href="../store/basic/erpStoreInfo/zhangbei/?id=${shopMainId!''}&form=taskList">掌贝进件</a>；</span>
				</label>
			</div>
			
			<div class="rs-label">
				<label class="rs-label-wrapper">
					<span class="subTask" value="4">4.若商户掌贝进件完成，请告知商户掌贝智慧服务中心（资料收集小程序）登录帐号密码变更为掌贝商户后台登录帐号密码。</span>
				</label>
			</div>
		
			<!--
			<div class="rs-label">
				<label class="rs-label-wrapper">
					<input type="checkbox"  value="1" name="${taskId!''}channelname1055">
					<span class="subTask" value="1">确认商户资料完整，并提交掌贝进件信息 ； 在ERP的“商户”栏目下完善商户资料，在进件管理栏目里进行提交进件信息；</span>
					&nbsp;<a href="../store/basic/erpStoreInfo/zhangbei?id=${shopMainId!''}&from=taskList"><span>掌贝进件</span></a>
				</label>
			</div>
			-->
		</div>
		
	</div>
	

<#-- <#if !isTaskDetail??> -->
	<div class="act-rs-right" id="taskTime">
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
<#-- </#if> -->
	
	
	
	
	
	<div class="act-process-left-footer" >
		<div class="footer-info floatleft">
			<p></p>
			<p>负责人：${taskUser!''}</p>
			<#-- <p>${startDate!''} -- ${endDate!''}</p> -->
		</div>
		<div class="footer-btn floatright">
			<button style="button" class="btn btn-large"  onclick="submit_${taskId!''}submitForm1002(1)">确定完成</button>
			<span class="flowTaskState hide">已完成</span>
		</div>
	</div>
</div>