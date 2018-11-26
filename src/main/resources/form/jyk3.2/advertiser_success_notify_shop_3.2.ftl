<div class="task-detail-wrapper">
   <h3 class="task-title">广告主开户成功通知商户</h3>
   <div class="task-sider">
      <div class="task-sider-title"><span>确认广告主开户情况，开户成功后通知商户</span></div>
      <div class="task-sider-subtitle"><span>开户状态</span></div>
      <div class="task-sider-content">
         <div class="border-bottom store-wrapper">
            <dl class="content-wrap margin-left-0">
               <dt>推广门店（1）：</dt>
               <dd class="margin-left-0">
                  ${storeName!''}
               </dd>
            </dl>
        <#if (channels)?? && (channels?size> 0) >    
	        <#list channels as channelsInfo>   
	            <#if (channelsInfo)?? && channelsInfo == 2 > 	
		            <dl class="content-wrap margin-left-0">
		               <dt>微博广告主开通状态：</dt>
		               <dd class="margin-left-0">
		                    ${weiboAuditStatus!''}
		               </dd>
		            </dl>
		          </#if>
	           	  <#if (channelsInfo)?? && channelsInfo== 1 >
		            <dl class="content-wrap margin-left-0">
		               <dt>朋友圈广告主开通状态：</dt>
		               <dd class="margin-left-0">
		                   ${friendsAuditStatus!''}
		               </dd>
		            </dl>
	             </#if>
                  <#if (channelsInfo)?? && channelsInfo ==3 >
		            <dl class="content-wrap margin-left-0">
		               <dt>陌陌广告主开通状态：</dt>
		               <dd class="margin-left-0">
		                  ${momoAuditStatus!''}
		               </dd>
		            </dl>
		          </#if>
		      </#list>      
     	</#if>
         </div>
      </div>
   </div>
   <div class="task-sider">
      <div class="task-sider-subtitle"><span>任务操作</span></div>
      <div class="task-sider-content">
         <div class="checkbox-list margin-top-20">
            <label class="content-label"><input type="checkbox" id="${taskId!''}notifyShopCheckBox1014"  <#if (flowdata.notifyShopCheckBox)?? && flowdata.notifyShopCheckBox =='true'> 
				  checked="checked" </#if>  name="srue">告主开通成功，通知商户</label>	
         </div>
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
	
	var notifyShopCheckBox = $("#${taskId!''}notifyShopCheckBox1014").prop("checked")
	
	if (!notifyShopCheckBox ) {return;}

	var submit = function (v, h, f) {
			if (v == 'ok') {
					$.jBox.tip("正在修改，请稍等...", 'loading', {
					timeout : 3000,
					persistent : true
			});
			$.post("../jyk/flow/3p2/advertiser_success_notify_shop_3.2", {
					taskId: "${taskId!''}",//$("#taskId1017").val(),
					procInsId: "${procInsId!''}",
					splitId:$("#${taskId!''}splitId1014").val(),
					notifyShopCheckBox:notifyShopCheckBox,
				}, 
			function(data) {
				if (data.result) {
					if(isLocation===1){
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
	$.jBox.confirm("确定通知商户吗？", "提示", submit);
}
</script>