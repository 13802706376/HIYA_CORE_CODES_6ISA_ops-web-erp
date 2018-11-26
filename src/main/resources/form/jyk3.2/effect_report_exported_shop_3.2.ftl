<input type="hidden" name="orderFileId" id="orderFileId" value="">
<input type="hidden" name="orderFileName" id="orderFileName" value="">
<input type='hidden' id='${taskId!''}splitId1014' name='${taskId!''}splitId1014' value="${splitId!''}"/>
<input type="hidden" name="orderFileIdNew" id="orderFileIdNew" value="">
<input type="hidden" name="orderFileNameNew" id="orderFileNameNew" value="">
<input type="hidden" name="orderFilUrl" id="orderFilUrl" value="">
<div class="task-detail-wrapper">
   <h3 class="task-title">效果报告输出给商户</h3>
   <div class="task-sider">
      <div class="task-sider-title"><span>查看最终推广数据</span></div>
      <div class="task-sider-subtitle"><span>任务操作</span></div>
      <div class="task-sider-content">
         <dl class="content-wrap">
            <dt>最终推广数据：</dt>
            <dd class="margin-left-120">
               <a href="" class="btn" target="_blank">查看</a>
            </dd>
         </dl>
      </div>
   </div>
   <div class="task-sider">
      <div class="task-sider-title"><span>最终效果报告同步给商户</span></div>
      <div class="task-sider-subtitle"><span>任务操作</span></div>
      <div class="task-sider-content">
         <div class="content-wrap">
            <div class="checkbox-list">
                  <label class="content-label"><input type="checkbox" id="${taskId!''}effectReportuploadOEM1014"  <#if (flowdata.effectReportuploadOEM)?? && flowdata.effectReportuploadOEM =='true'> 
				  checked="checked" </#if>  name="srue">将效果报告上传至OEM后台，并修改推广状态</label>	
                
             </div>
             <div class="checkbox-list">
                  <label class="content-label"><input type="checkbox" id="${taskId!''}effectReportInformShopCheck1014"  <#if (flowdata.effectReportInformShopCheck)?? && flowdata.effectReportInformShopCheck =='true'> 
				  checked="checked" </#if>  name="srue">在商户运营服务群告知商户效果报告已输出并引导商户登录商户管理后台查看</label>	
                
             </div>
         </div>
      </div>
   </div>
   <div class="task-sider">
      <div class="task-sider-title"><span>上传最终效果报告进行存档</span></div>
      <div class="task-sider-subtitle"><span>任务操作</span></div>
      <div class="task-sider-content">
         <dl class="content-wrap">
            <dt>效果报告：</dt>
            <dd class="margin-left-120 positionrelative">
               <div class="ajaxFileUploadInputDiv">
                  <input id="showFileNameInput" type="text" readonly="readonly">
                  <div id="pickId" class="filePickIds">选择文件</div>
               </div>
               <div class="fileProgressDiv">
                  <div id="fileProgressDiv" class="fileProgressWrap"></div>
               </div>
              
               <#if (notifyShopDataReport.pdfName)??>
	               <div class="firstDayDataReport">
	                	 ${notifyShopDataReport.pdfName!''}
	                	 <input type="hidden" name="${taskId!''}id" value="${notifyShopDataReport.id!''}">
	                	 <input type="hidden" name="${taskId!''}pdfUrl" value="${notifyShopDataReport.pdfUrl!''}">
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
         <button type="button" class="btn btn-large submitform">确定完成</button>
      </div>
   </div>
</div>
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
               };
              
            	var isFinished=false;
				var effectReportuploadOEM = $("#${taskId!''}effectReportuploadOEM1014").prop("checked");
				var effectReportInformShopCheck = $("#${taskId!''}effectReportInformShopCheck1014").prop("checked");
				if( effectReportuploadOEM && effectReportInformShopCheck) {
					isFinished = true;
				}
				if (!effectReportuploadOEM && !effectReportInformShopCheck) {return;}
			
               	var submit = function (v, h, f) {
                  if (v == 'ok') {
                        $.jBox.tip("正在修改，请稍等...", 'loading', {
                        timeout : 3000,
                        persistent : true
                  });
                 $.post("../jyk/flow/3p2/effect_report_exported_shop_3.2", {
					isFinished: isFinished,
					taskId: "${taskId!''}",//$("#taskId1017").val(),
					procInsId: "${procInsId!''}",
					splitId:$("#${taskId!''}splitId1014").val(),
					jsonStr: JSON.stringify(jsonStr),
					effectReportuploadOEM:effectReportuploadOEM,
					effectReportInformShopCheck:effectReportInformShopCheck
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
               $.jBox.confirm("确定完成吗？", "提示", submit);
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