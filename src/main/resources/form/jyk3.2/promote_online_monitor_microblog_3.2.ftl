<div class="task-detail-wrapper">
   <h3 class="task-title">推广上线监测 - 微博</h3>
   <div class="task-sider">
      <div class="task-sider-title"><span>推广数据监控和上传</span></div>
      <div class="task-sider-subtitle"><span>任务说明</span></div>
      <div class="task-sider-content">
         <div class="content-wrap">
            <p>每日上传投放数据，并监测投放数据；</p>
            <p>推广结束，该任务自动标记完成；</p>
         </div>
      </div>
   </div>
   <div class="task-sider">
      <div class="task-sider-subtitle"><span>推广状态</span></div>
      <div class="task-sider-content">
         <dl class="content-wrap">
            <dt>推广状态：</dt>
            <dd class="margin-left-120">
               <#if (flowdata.weiboChannelSelected.promoteStatus)??>
            		<#if flowdata.weiboChannelSelected.promoteStatus == 'running'>
            		推广中
            		</#if>
            	</#if>
            </dd>
         </dl>
         <dl class="content-wrap">
            <dt>推广开始时间：</dt>
            <dd class="margin-left-120">
               <#if (flowdata.weiboChannelSelected.promoteStartDate)??>
            		${flowdata.weiboChannelSelected.promoteStartDate?string('yyyy-MM-dd')}
            	</#if>
            </dd>
         </dl>
         <dl class="content-wrap">
            <dt>推广结束时间：</dt>
            <dd class="margin-left-120">
               /
            </dd>
         </dl>
      </div>
   </div>
   <div class="task-sider">
      <div class="task-sider-subtitle"><span>任务操作</span></div>
      <div class="task-sider-content">
         <div class="content-wrap">
            <a href="" class="btn" target="_blank">上传推广数据</a>
         </div>
      </div>
   </div>
   <div class="task-detail-footer">
      <div class="footer-info">
         <p>负责人：${taskUser!''}</p>
         <p>${startDate!''} —— ${endDate!''}</p>
      </div>
      <div class="footer-btn">
         <button type="button" class="btn btn-large" onclick=""></button>
      </div>
   </div>
</div>
<script type="text/javascript">
   var task_flow_version = '3.2';
   $(function() {
      var promotOnlineMonitorWeibo = function() {
         var 
         bindEvent = function() {
            $('body').on('click', '#contentListWrapper .list-label .delete-btn', function(event) {
               
            });
         },
         init = function() {
            applyUploader();
            bindEvent();
         };

         return {
            init: init
         };
      }();

      promotOnlineMonitorWeibo.init();
   })
</script>