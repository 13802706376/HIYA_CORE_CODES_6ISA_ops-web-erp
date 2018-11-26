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
   <h3 class="task-title">微博充值资料补充</h3>
   <div class="task-sider">
      <div class="task-sider-title"><span>微博充值申请补充</span></div>
      <div class="task-sider-subtitle"><span>相关资料</span></div>
      <div class="task-sider-content">
         <p class="margin-left-14">本次推广套餐：${taskDisplay!''}&nbsp;&nbsp;&nbsp;&nbsp;本次推广门店数：${(storeWeiboData?size)!''}</p>
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
         <div class="content-wrap">请参考《聚引客产品套餐曝光定义及各通道成本分配表》</div>
        
         <#if (weiboReachargeLists)?? && (weiboReachargeLists?size> 0) >    
		   <#list weiboReachargeLists as weiboReachargeInfo>
         <div class="border-bottom">
            <dl class="content-wrap">
               <dt>微博账号：</dt>
               <dd class="margin-left-0">${weiboReachargeInfo.weiboAccountNo!''}</dd>
            </dl>
            <dl class="content-wrap">
               <dt>输入申请充值金额（元）：</dt>
               <dd>
                  ${weiboReachargeInfo.applyAmount!''}
               </dd>
            </dl>
         </div>
     	</#list>      
      </#if>
     	  
      </div>
   </div>
   <div class="task-detail-footer">
      <div class="footer-info">
         <p>负责人：${taskUser!''}</p>
         <p>${startDate!''} —— ${endDate!''}</p>
      </div>
      <div class="footer-btn">
         <button type="button" class="btn btn-large btn-warning recharge">审核不通过</button>
         <button type="button" class="btn btn-large rechargeok">审核通过</button>
      </div>
   </div>
</div>
</#if>
<script type="text/javascript">
   var task_flow_version = '3.2';
   $(function() {
      var weiboRechargeReview = function() {
         var submit = function(isPas, reason) {
         	$.post('../jyk/flow/3p2/microblog_recharge_review_3.2', {
				taskId: "${taskId!''}",
				procInsId: "${procInsId!''}",
				splitId: "${splitId!''}",
				isPas: isPas,
				reason: reason
			}, function(data) {
				if(data.result) {
					if (typeof updatePage === 'function') {
							updatePage();
						} else {
							window.location.href="../workflow/tasklist";
						}
				}
			})
         },
         bindEvent = function() {
            $('body').on('click', '.recharge', function(event) {
               erpShopApp.auditConfirm(function(text) {
					submit('N', text)
               })
            }).on('click', '.rechargeok', function() {
            	submit('Y')
            });
         },
         init = function() {
            bindEvent();
         };

         return {
            init: init
         };
      }();

      weiboRechargeReview.init();
   })
</script>