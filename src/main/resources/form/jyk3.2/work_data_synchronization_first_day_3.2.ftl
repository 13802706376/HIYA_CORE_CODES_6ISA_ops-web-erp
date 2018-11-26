<div class="task-detail-wrapper">
   <h3 class="task-title">首日推广数据同步</h3>
   <div class="task-sider">
      <div class="task-sider-title"><span>查看首日推广数据</span></div>
      <div class="task-sider-subtitle"><span>任务操作</span></div>
      <div class="task-sider-content">
         <dl class="content-wrap">
            <dt>首日推广数据：</dt>
            <dd class="margin-left-120">
               <a href="" class="btn">查看</a>
            </dd>
         </dl>
      </div>
   </div>
   <div class="task-sider">
      <div class="task-sider-title"><span>首日推广数据简报同步给商户</span></div>
      <div class="task-sider-subtitle"><span>任务操作</span></div>
      <div class="task-sider-content">
         <div class="content-wrap">
            <label class="content-label"><input type="checkbox" id="${taskId!''}firstDayPromoteDataInformShop1014"  <#if (flowdata.firstDayPromoteDataInformShop)?? && flowdata.firstDayPromoteDataInformShop =='true'> 
				  checked="checked" </#if>  name="srue">完成首日推广数据简报同步给商户</label>	
         </div>
      </div>
   </div>
   <div class="task-sider">
      <div class="task-sider-title"><span>上传首日效果报告进行存档</span></div>
      <div class="task-sider-subtitle"><span>任务操作</span></div>
      <div class="task-sider-content">
         <dl class="content-wrap">
            <dt>首日效果报告：</dt>
            <dd class="margin-left-120 positionrelative">
               <div class="ajaxFileUploadInputDiv">
                  <input id="showFileNameInput" type="text" readonly="readonly">
                  <div id="pickId" class="filePickIds">选择文件</div>
               </div>
               <div class="fileProgressDiv">
                  <div id="fileProgressDiv" class="fileProgressWrap"></div>
               </div>
               <#if (firstDayDataReport.pdfName)??>
	               <div class="firstDayDataReport">
	                	 ${firstDayDataReport.pdfName!''}
	                	 <input type="hidden" name="${taskId!''}id" value="${firstDayDataReport.id!''}">
	                	 <input type="hidden" name="${taskId!''}pdfUrl" value="${firstDayDataReport.pdfUrl!''}">
	               </div>
               </#if>
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
         <button type="button" class="btn btn-large submitform" onclick="">确定完成</button>
      </div>
   </div>
</div>
<input type="hidden" name="orderFileId" id="orderFileId" value="">
<input type="hidden" name="orderFileName" id="orderFileName" value="">
<input type='hidden' id='${taskId!''}splitId1014' name='${taskId!''}splitId1014' value="${splitId!''}"/>
<input type="hidden" name="orderFileIdNew" id="orderFileIdNew" value="">
<input type="hidden" name="orderFileNameNew" id="orderFileNameNew" value="">
<input type="hidden" name="orderFilUrl" id="orderFilUrl" value="">
<script type="text/javascript">
   var task_flow_version = '3.2';
   $(function() {
      var workDataSyncFristDay = function() {
         var applyUploader = function() {
            applyWebuploader("${taskDefKey!''}", "${procInsId!''}", "首日效果报告", "../workfile/file/onlyUpload", "pickId", "showFileNameInput", "fileProgressDiv", "orderFileId", "orderFileName", false, "pdf", "${splitId!''}", function(file, data) {
               console.log(file, data)
               $("#orderFileNameNew").val(data.fileName);
               $("#orderFilUrl").val(data.url);
            });
         },
         bindEvent = function() {
            $('body').on('click', '.submitform', function(event) {
               var firstDayPromoteDataInformShop =$("#${taskId!''}firstDayPromoteDataInformShop1014").prop("checked");
               var jsonStr = {
                  pdfUrl:$("#orderFilUrl").val() || "${(firstDayDataReport.pdfUrl)!''}",
                  pdfName:$("#orderFileNameNew").val() || "${(firstDayDataReport.pdfName)!''}",
                  id:"${(firstDayDataReport.id)!''}"
               }
               var submit = function (v, h, f) {
                  if (v == 'ok') {
                        $.jBox.tip("正在修改，请稍等...", 'loading', {
                        timeout : 3000,
                        persistent : true
                  });
                  $.post("../jyk/flow/3p2/work_data_synchronization_first_day_3.2", {
                        taskId: "${taskId!''}",//$("#taskId1017").val(),
                        procInsId: "${procInsId!''}",
                        splitId:$("#${taskId!''}splitId1014").val(),
                        jsonStr: JSON.stringify(jsonStr),
                        firstDayPromoteDataInformShop:firstDayPromoteDataInformShop,
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
               $.jBox.confirm("确定完成数据同步吗？", "提示", submit);
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

      workDataSyncFristDay.init();
   })
</script>