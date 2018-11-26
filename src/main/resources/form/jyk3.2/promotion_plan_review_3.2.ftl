<script type="text/javascript">

function submit_${taskId!''}submitForm1014(isLocation){
   

   var submit = function (v, h, f) {
         if (v == 'ok') {
               $.jBox.tip("正在修改，请稍等...", 'loading', {
               timeout : 3000,
               persistent : true
         });
         $.post("../jyk/flow/3p2/promotion_plan_review_3.2", {
               taskId:$("#${taskId!''}taskId1014").val(),
               procInsId:$("#${taskId!''}procInsId1014").val(),
               splitId:$("#${taskId!''}splitId1014").val(),
               friendConfirm: $("#${taskId!''}friendconfirm1014").prop("checked"),
               momoConfirm: $("#${taskId!''}momoconfirm1014").prop("checked"),
               weiboConfirm: $("#${taskId!''}weiboconfirm1014").prop("checked"),
               launchinfo: encodeURI($("#${taskId!''}launchinfo1014").val())
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
	<#if vars?exists>
		        <#list vars?keys as key> 
		           <#if vars["chooseMomoFlag"]??>
		              <#assign chooseMomoFlag = vars["chooseMomoFlag"]>
		           </#if>
		           <#if vars["chooseFriendFlag"]??>
				       <#assign chooseFriendFlag = vars["chooseFriendFlag"]>
				   </#if>
				    <#if vars["chooseMicroblogFlag"]??>
		              <#assign chooseMicroblogFlag = vars["chooseMicroblogFlag"]>
		           </#if>
		         </#list>
		    </#if>

<div class="task-detail-wrapper">
   <#if chooseMicroblogFlag?? && chooseMicroblogFlag =='Y' >
   <h3 class="task-title">推广计划提审确认成功</h3>
   <div class="task-sider">
      <div class="task-sider-title"><span>上传商户资质到商户微博后台</span></div>
      <div class="task-sider-subtitle"><span>任务操作</span></div>
      <div class="task-sider-content">
         <div class="content-wrap">
            <p>获取商户行业资质及《微博推广承诺函》，并上传到商户微博后台</p>
            <div><a href="" class="btn">点击获取</a></div>
         </div>
      </div>
   </div>
   </#if>
   <div class="task-sider">
      <div class="task-sider-title"><span>确认推广计划提审成功</span></div>
      <div class="task-sider-subtitle"><span>任务操作</span></div>
      <div class="task-sider-content">
         <div class="content-wrap">
         	<#if chooseFriendFlag?? && chooseFriendFlag =='Y' >
            <div class="checkbox-list">
               <label class="content-label"><input id="${taskId!''}friendconfirm1014"  <#if (flowdata.friendConfirm)?? && flowdata.friendConfirm =='true'> checked="checked" </#if> type="checkbox">确认推广计划提审成功 - 朋友圈</label>
            </div>
            </#if>
            <#if chooseMicroblogFlag?? && chooseMicroblogFlag =='Y' >
            <div class="checkbox-list">
               <label class="content-label"><input id="${taskId!''}weiboconfirm1014"  <#if (flowdata.weiboConfirm)?? && flowdata.weiboConfirm =='true'> checked="checked" </#if> type="checkbox">确认推广计划提审成功 - 微博</label>
            </div>
            </#if>
            <#if chooseMomoFlag?? && chooseMomoFlag =='Y' >
            <div class="checkbox-list">
               <label class="content-label"><input id="${taskId!''}momoconfirm1014"  <#if (flowdata.momoConfirm)?? && flowdata.momoConfirm =='true'> checked="checked" </#if> type="checkbox">确认推广计划提审成功 - 陌陌</label>
            </div>
            </#if>
         </div>
      </div>
   </div>
   <div class="task-sider">
      <div class="task-sider-title"><span>投放信息</span></div>
      <div class="task-sider-subtitle"><span>任务操作</span></div>
      <div class="task-sider-content">
         <div class="content-wrap">
            <p>经营诊断与策划方案（投放顾问）：<a href="">查看</a></p>
         </div>
      </div>
      <div class="task-sider-subtitle"><span>任务操作</span></div>
      <div class="task-sider-content">
         <div class="content-wrap">
            <p>请输入商户全称、套餐数量、套餐信息（套餐版本+是否有曝光量赠送）、上线时间、投放城市、投放地点、其他特殊要求</p>
            <div class="textarea-wrap">
               <textarea id="${taskId!''}launchinfo1014" class="content-textarea"><#if (flowdata.launchinfo)?? >${flowdata.launchinfo}</#if></textarea>
            </div>
         </div>
      </div>
   </div>
   <div class="task-detail-footer">
      <div class="footer-info">
         <p>负责人：${taskUser!''}</p>
         <p>${startDate!''} —— ${endDate!''}</p>
      </div>
      <div class="footer-btn">
         <button type="button" class="btn btn-large" onclick="submit_${taskId!''}submitForm1014(1)">确定完成</button>
      </div>
   </div>
</div>
</#if>
<input type="hidden" name="orderFileId" id="orderFileId" value="">
<input type="hidden" name="orderFileName" id="orderFileName" value="">
<script type="text/javascript">
   var task_flow_version = '3.2';
   $(function() {
      var promotionPlanReview = function() {
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

      promotionPlanReview.init();
   })
</script>