<script type="text/javascript">

	function submit_${taskId!''}submitForm1002(isLocation){
	
			var taskStarterDate = "${taskStarterDate!''}";	
			
			var openOrTrans = $('input[name="${taskId!''}openOrTrans"]:checked').val();
			
			if(openOrTrans === '' || undefined === openOrTrans){
				$.jBox.tip("请选择新开户/转户！", 'error');
				return;
			}
	
		
		
		
		
		var submit = function (v, h, f) {
				if (v == 'ok') {
						$.jBox.tip("正在修改，请稍等...", 'loading', {
						timeout : 3000,
						persistent : true
				});
				$.post("../jyk/flow/accountZhixiao/perfect_microblog_promote_info_zhixiao_latest", {
						taskId:$("#${taskId!''}taskId1002").val(),
						procInsId:$("#${taskId!''}procInsId1002").val(),
						openOrTrans : $('input[name="${taskId!''}openOrTrans"]:checked').val()
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
		$.jBox.confirm("确认完成微博推广开户资料吗？", "提示", submit);
	};

</script>
<input type='hidden' id='${taskId!''}taskId1002' name='taskId' value="${taskId!''}"/>
<input type='hidden' id='${taskId!''}procInsId1002' name='procInsId'  value="${procInsId!''}"/>
<div class="act-rs-item  ${detailType!''}  ${followTaskDetailWrap!''}">
	<div class="act-rs-left floatleft">
		<div class="act-rs-title padding15">
			<h3><a href="../workflow/taskDetail?taskId=${taskId!''}&followTaskDetailWrap=${followTaskDetailWrap!''}&detailType=${detailType!''}">${taskName!''}</a></h3>
			<div class="listSubmit">
				
				<button  onclick="submit_${taskId!''}submitForm1002()" type="button" class="makesrue btn">确定完成</button>
				<a href="javascript:;" class="select-user open-select-user" style="float: left;" onclick="syncSelect('${taskId!''}', '${taskUser!''}','${taskUserId!''}')"></a>
				
			</div>
			<div class="taskPrincipal"><span class="taskPrincipalName">${taskUser!''}</span></div>
		</div>
		<div class="act-rs-form form-horizontal padding15">
			<div class="rs-label">
				<label class="rs-label-wrapper">
					<span class="subTask" value="1">商户是转户还是开户：</span>
					<input type="radio" name="${taskId!''}openOrTrans" <#if (openOrTrans)?? && openOrTrans == "O">checked="checked"</#if> value="O" class="${taskId!''}openOrTrans"/>新开户
					<input type="radio" name="${taskId!''}openOrTrans" <#if (openOrTrans)?? && openOrTrans == "T">checked="checked"</#if> value="T" class="${taskId!''}openOrTrans"/>转户
				</label>
			</div>
			<div class="rs-label" style="height:50px;">
			
				<div class="rs-label">
					<label class="subTask" value="1">
						<span>完成微博推广开户资料并提交审核：${storeName!''}&nbsp;&nbsp;&nbsp;
							<a href="javascript:;" class="openWeiboForm" data-datas="${storeId!''},zhixiao,edit">
								编辑
							</a>&nbsp;&nbsp;
							
						</span>
						<input type="hidden" class="hide" value="1111111111">
					</label>
				</div>
			
				<div class="control-group">
					
					<span>
						已选门店：<a href="../store/basic/erpStoreInfo/adweibo/?shopId=${shopMainId!''}&storeId=${storeId!''}&isMain=${isMain!''}&form=taskList">${storeName!''}</a>
					</span>

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
			<button style="button" class="btn btn-large"  onclick="submit_${taskId!''}submitForm1002(1)">确定完成</button>
			<span class="flowTaskState hide">已完成</span>
		</div>
	</div>
</div>