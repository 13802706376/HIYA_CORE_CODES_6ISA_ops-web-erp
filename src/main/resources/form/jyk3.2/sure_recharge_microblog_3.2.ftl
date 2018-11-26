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
   <h3 class="task-title">确定进行微博充值</h3>
   <div class="task-sider">
      <div class="task-sider-title"><span>微博充值申请</span></div>
      <div class="task-sider-subtitle"><span>相关资料</span></div>
      <div class="task-sider-content">
         <p class="margin-left-14">本次推广套餐：${taskDisplay!''}&nbsp;&nbsp;&nbsp;&nbsp;本次推广门店数：${(storeWeiboData?size)!''} </p>
         <div class="border-top store-wrapper">
           
          <#if (storeWeiboData)?? && (storeWeiboData?size> 0) >    
		  <#list storeWeiboData as storeWeiboInfo>   
            <dl class="content-wrap margin-left-0">
               <dt>推广门店（1）：</dt>
               <dd class="margin-left-0">
                   ${storeWeiboInfo.storeName!''}
               </dd>
            </dl>
            <dl class="content-wrap margin-left-0">
               <dt>该门店微博广告主开通状态：</dt>
               <dd class="margin-left-0">
                    ${storeWeiboInfo.auditStatus!''}
               </dd>
            </dl>
            <dl class="content-wrap margin-left-0">
               <dt>该门店微博账号：</dt>
               <dd class="margin-left-0">
                   ${storeWeiboInfo.accountNo!''}
               </dd>
            </dl>
            <dl class="content-wrap margin-left-0">
               <dt>该门店微博UID：</dt>
               <dd class="margin-left-0">
                 ${storeWeiboInfo.uid!''}
               </dd>
            </dl>
        
           </#list>      
     	  </#if>
         </div>
      </div>
   </div>
   <div class="task-sider">
      <div class="task-sider-subtitle"><span>任务操作</span></div>
      <div class="task-sider-content">
         <dl class="content-wrap">
            <dt>是否进行微博充值：</dt>
            <dd>
               <label class="content-label"><input <#if (flowdata.isSureRecharge)?? && flowdata.isSureRecharge =='N'> checked="checked"  </#if> type="radio" value="N" name="isSureRecharge">否</label>
			   <label class="content-label"><input <#if (flowdata.isSureRecharge)?? && flowdata.isSureRecharge  =='Y'> checked="checked" </#if> type="radio" value="Y" name="isSureRecharge">是</label>
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
</#if>
<input type='hidden' id='${taskId!''}splitId1014' name='${taskId!''}splitId1014' value="${splitId!''}"/>
<script type="text/javascript">
	var task_flow_version = '3.2';
	
	function submit_${taskId!''}submitForm1017(isLocation){

	var submit = function (v, h, f) {
			if (v == 'ok') {
					$.jBox.tip("正在修改，请稍等...", 'loading', {
					timeout : 3000,
					persistent : true
			});
			$.post("../jyk/flow/3p2/sure_recharge_microblog_3.2", {
					taskId: "${taskId!''}",//$("#taskId1017").val(),
					procInsId: "${procInsId!''}",
					splitId:$("#${taskId!''}splitId1014").val(),
					isSureRecharge: $("input[name=isSureRecharge]:checked").val()
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
	$.jBox.confirm("确定提交吗？", "提示", submit);
}
</script>