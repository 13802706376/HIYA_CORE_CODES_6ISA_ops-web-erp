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
   <h3 class="task-title">微博充值完成</h3>
   <div class="task-sider">
      <div class="task-sider-title"><span>微博充值管理</span></div>
      <div class="task-sider-subtitle"><span>任务操作</span></div>
     
      <#if (weiboReachargeLists)?? && (weiboReachargeLists?size> 0) >    
		  <#list weiboReachargeLists as weiboReachargeInfo>   
		  
		      <div class="task-sider-content">
		         <dl class="content-wrap">
		            <dt>微博账号：</dt>
		            <dd class="margin-left-0">${weiboReachargeInfo.weiboAccountNo!''}</dd>
		         </dl>
		         <dl class="content-wrap">
		            <dt>充值状态：</dt>
		            <dd class="margin-left-0">
		            <#if (weiboReachargeInfo.status)?? &&(weiboReachargeInfo.status)=='Commit' >
		                 提交
			            </#if>
			            <#if (weiboReachargeInfo.status)?? &&(weiboReachargeInfo.status)=='Applying' >
		                 申请中
			            </#if>
			            <#if (weiboReachargeInfo.status)?? &&(weiboReachargeInfo.status)=='Success' >
		                 成功
			            </#if>
			            <#if (weiboReachargeInfo.status)?? &&(weiboReachargeInfo.status)=='Cancel' >
		                 取消
			            </#if>
		            </dd>
		         </dl>
		      </div>
     
     	 </#list>      
      </#if>
      <p>查看微博充值管理页<a href="" target="_blank" class="btn">查看</a></p>
   </div>
   <div class="task-detail-footer">
      <div class="footer-info">
         <p>负责人：${taskUser!''}</p>
         <p>${startDate!''} —— ${endDate!''}</p>
      </div>
      <!-- <div class="footer-btn">
         <button type="button" class="btn btn-large btn-warning" onclick="">审核不通过</button>
         <button type="button" class="btn btn-large" onclick="">确定完成</button>
      </div> -->
   </div>
</div>
</#if>
<script type="text/javascript">
   var task_flow_version = '3.2';
   $(function() {
      var weiboRechargeFinish = function() {
         var bindEvent = function() {
            $('body').on('click', '#contentListWrapper .list-label .delete-btn', function(event) {
               
            });
         },
         init = function() {
            bindEvent();
         };

         return {
            init: init
         };
      }();

      weiboRechargeFinish.init();
   })
</script>