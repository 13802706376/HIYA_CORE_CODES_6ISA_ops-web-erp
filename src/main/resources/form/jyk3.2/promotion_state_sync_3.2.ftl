<div class="task-detail-wrapper">
   <h3 class="task-title">推广状态同步</h3>
   <div class="task-sider">
      <div class="task-sider-title"><span>推广状态</span></div>
      <div class="task-sider-subtitle"><span>推广状态</span></div>
      <div class="task-sider-content">
         <dl class="content-wrap">
            <dt>推广门店：</dt>
            <dd class="margin-left-120">
               ${storeName!''}
            </dd>
         </dl>
        
        <#if (promotionchannelsLists)?? && (promotionchannelsLists?size > 0) >    
		<#list promotionchannelsLists as channelsInfo>   
			<#if (channelsInfo.promotionChannel)?? &&(channelsInfo.promotionChannel)=='1' >
		         <dl class="content-wrap">
		            <dt>朋友圈推广状态：</dt>
		            <dd class="margin-left-120">
			            <#if (channelsInfo.promoteStatus)?? &&(channelsInfo.promoteStatus)=='notstart' >
			             未开始
			            </#if>
			             <#if (channelsInfo.promoteStatus)?? &&(channelsInfo.promoteStatus)=='running' >
			            推广中
			            </#if>
			             <#if (channelsInfo.promoteStatus)?? &&(channelsInfo.promoteStatus)=='finish' >
			            推广结束
			            </#if>
		            </dd>
		         </dl>
		         
		         <dl class="content-wrap">
		            <dt>上线时间：</dt>
		            <dd class="margin-left-120">
		               ${channelsInfo.promoteStartDate?string('yyyy-MM-dd')}
		            </dd>
		         </dl>
		      </#if>
		      
		      <#if (channelsInfo.promotionChannel)?? &&(channelsInfo.promotionChannel)=='2' >
		         <dl class="content-wrap">
		            <dt>微博推广状态：</dt>
		            <dd class="margin-left-120">
			              <#if (channelsInfo.promoteStatus)?? &&(channelsInfo.promoteStatus)=='notstart' >
			             未开始
			            </#if>
			             <#if (channelsInfo.promoteStatus)?? &&(channelsInfo.promoteStatus)=='running' >
			            推广中
			            </#if>
			             <#if (channelsInfo.promoteStatus)?? &&(channelsInfo.promoteStatus)=='finish' >
			            推广结束
			            </#if>
		            </dd>
		         </dl>
		         
		         <dl class="content-wrap">
		            <dt>上线时间：</dt>
		            <dd class="margin-left-120">
		               ${channelsInfo.promoteStartDate?string('yyyy-MM-dd')}
		            </dd>
		         </dl>
		      </#if>
		      
		      <#if (channelsInfo.promotionChannel)?? &&(channelsInfo.promotionChannel)=='3' >
		         <dl class="content-wrap">
		            <dt>陌陌推广状态：</dt>
		            <dd class="margin-left-120">
			                <#if (channelsInfo.promoteStatus)?? &&(channelsInfo.promoteStatus)=='notstart' >
				             未开始
				            </#if>
				             <#if (channelsInfo.promoteStatus)?? &&(channelsInfo.promoteStatus)=='running' >
				            推广中
				            </#if>
				             <#if (channelsInfo.promoteStatus)?? &&(channelsInfo.promoteStatus)=='finish' >
				            推广结束
				            </#if>
		            </dd>
		         </dl>
		         
		         <dl class="content-wrap">
		            <dt>上线时间：</dt>
		            <dd class="margin-left-120">
		               ${channelsInfo.promoteStartDate?string('yyyy-MM-dd')}
		            </dd>
		         </dl>
		      </#if>
        </#list>      
     	</#if>
         
      </div>
   </div>
   <div class="task-sider">
      <div class="task-sider-title"><span>将推广展示截图上传到OEM后台</span></div>
      <div class="task-sider-subtitle"><span>相关资料</span><a href="" class="syncdownload">批量下载</a></div>
      <div class="task-sider-content">
         <dl class="content-wrap">
         
            <#if (promotionchannelsLists)?? && (promotionchannelsLists?size > 0) >    
			<#list promotionchannelsLists as channelsInfo>   
		           <#if (channelsInfo.promotionChannel)?? &&(channelsInfo.promotionChannel)=='1' >
		            <dt>朋友圈最终推广页面预览截图：</dt>
		            <dd class="margin-left-200">
		               <#if (friendPicLists)?? && (friendPicLists?size > 0) >    
						<#list friendPicLists as picInfo>   
		                 <p>${picInfo.fileName!''}</p>
		                </#list>      
     				 </#if>
		            </dd>
		          </#if>
		          
		          <#if (channelsInfo.promotionChannel)?? &&(channelsInfo.promotionChannel)=='2' >
		            <dt>微博最终推广页面预览截图：</dt>
		            <dd class="margin-left-200">
		               <#if (weiboPicLists)?? && (weiboPicLists?size > 0) >    
						<#list weiboPicLists as picInfo>   
		                 <p>${picInfo.fileName!''}</p>
		                </#list>      
     				 </#if>
		            </dd>
		          </#if>
		          
		          <#if (channelsInfo.promotionChannel)?? &&(channelsInfo.promotionChannel)=='3' >
		            <dt>陌陌最终推广页面预览截图：</dt>
		            <dd class="margin-left-200">
		               <#if (momoPicLists)?? && (momoPicLists?size > 0) >    
						<#list momoPicLists as picInfo>   
		                 <p>${picInfo.fileName!''}</p>
		                </#list>      
     				 </#if>
		            </dd>
		          </#if>
	        
	         </#list>      
     		</#if>
         </dl>
         
      </div>
   </div>
   <div class="task-sider">
      <div class="task-sider-title"><span>告知商户推广方案已上线</span></div>
      <div class="task-sider-subtitle"><span>任务操作</span></div>
      <div class="task-sider-content">
         <div class="content-wrap">
            <label class="content-label"><input type="checkbox" id="${taskId!''}InformShopUpLine1014"  <#if (flowdata.InformShopUpLine)?? && flowdata.InformShopUpLine =='true'> 
				  checked="checked" </#if>  name="srue">在商户运营服务群告知商户推广方案已上线</label>	
            
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
	
	var InformShopUpLine=$("#${taskId!''}InformShopUpLine1014").prop("checked");
	if (!InformShopUpLine ) {return;}
	var submit = function (v, h, f) {
			if (v == 'ok') {
					$.jBox.tip("正在修改，请稍等...", 'loading', {
					timeout : 3000,
					persistent : true
			});
			$.post("../jyk/flow/3p2/promotion_state_sync_3.2", {
					taskId: "${taskId!''}",//$("#taskId1017").val(),
					procInsId: "${procInsId!''}",
					splitId:$("#${taskId!''}splitId1014").val(),
					InformShopUpLine:InformShopUpLine,
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
	$.jBox.confirm("确定完成推广状态同步吗？", "提示", submit);
}
</script>