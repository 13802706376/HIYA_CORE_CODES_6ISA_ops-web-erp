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
   <h3 class="task-title">微博充值申请补充</h3>
   <div class="task-sider">
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
	                    <#if storeWeiboInfo.auditStatus?? && storeWeiboInfo.auditStatus == 4 >
	                    通过
	                    </#if>
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
         <#if (storeWeiboData)?? && (storeWeiboData?size> 0) >    
		   <#list storeWeiboData as storeWeiboInfo>
	         <div class="border-bottom" name="list-label">
	            <dl class="content-wrap">
	               <dt>微博账号：</dt>
	               <dd  name="${taskId!''}accountNo1014"  class="margin-left-0">${storeWeiboInfo.accountNo!''}</dd>
	               <input type="hidden" name="${taskId!''}WeiboUid"  value="${storeWeiboInfo.uid!''}">
	            </dl>
	            <dl class="content-wrap">
	               <dt>输入申请充值金额（元）：</dt>
	               <dd>
	                  <input type="text" name="${taskId!''}applyAmount" value="" class="content-input">
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
         <button type="button" class="btn btn-large" onclick="submit_${taskId!''}123(1)">确定完成</button>
      </div>
   </div>
</div>
</#if>
<input type='hidden' id='${taskId!''}splitId1014' name='${taskId!''}splitId1014' value="${splitId!''}"/>
<script type="text/javascript">
  		 var task_flow_version = '3.2';
		
   
	function submit_${taskId!''}123(isLocation){
	
	var attr = [];
	console.log($(".list-label"));
		$("div[name=list-label]").each(function(obj){
			var weiboAccountNo = $("dd[name=${taskId!''}accountNo1014]",obj).html(),
				weiboUid =$("input[name=${taskId!''}WeiboUid]",obj).val(),
				applyAmount = $("input[name=${taskId!''}applyAmount]",obj).val();
		
			attr.push({
				weiboAccountNo: weiboAccountNo,
				weiboUid: weiboUid,
				applyAmount: applyAmount
			});
		});
	
	console.log(attr);
	
	var submit = function (v, h, f) {
			if (v == 'ok') {
					$.jBox.tip("正在修改，请稍等...", 'loading', {
					timeout : 3000,
					persistent : true
			});
			$.post("../jyk/flow/3p2/microblog_recharge_supplement_3.2", {
					taskId: "${taskId!''}",//$("#taskId1017").val(),
					procInsId: "${procInsId!''}",
					splitId:$("#${taskId!''}splitId1014").val(),
					jsonStr: JSON.stringify(attr)
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