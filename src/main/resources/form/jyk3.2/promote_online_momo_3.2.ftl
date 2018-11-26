<script type="text/javascript">

function submit_${taskId!''}submitForm1014(isLocation,flag){
	
 	var outerImgMomo =	$("[data-fid='orderFileIdMomo1']");
 	var innerImgMomo =	$("[data-fid='orderFileIdMomo2']");
 	
 	var outerImgMomo1 =	[];
 	var innerImgMomo1 =	[];
 	

	outerImgMomo.each(function(){
		outerImgMomo1.push($(this).attr("data-fileid"));
	});
	innerImgMomo.each(function(){
		innerImgMomo1.push($(this).attr("data-fileid"));
	});

	console.log(outerImgMomo1);
	console.log(innerImgMomo1);
	
	var submit = function (v, h, f) {
			if (v == 'ok') {
					$.jBox.tip("正在修改，请稍等...", 'loading', {
					timeout : 3000,
					persistent : true
			});
			$.post("../jyk/flow/3p2/promote_online_momo_3.2", {
					taskId:$("#${taskId!''}taskId1014").val(),
					procInsId:$("#${taskId!''}procInsId1014").val(),
					splitId:$("#${taskId!''}splitId1014").val(),
					outerImgMomo: outerImgMomo1.toString(),
					innerImgMomo: innerImgMomo1.toString(),
					promoteDate:$("#${taskId!''}momopromotedate1014").val()
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
<div class="task-detail-wrapper">
   <h3 class="task-title">推广上线 - 陌陌</h3>
   <div class="task-sider">
      <div class="task-sider-title"><span>陌陌推广上线，上传陌陌最终推广页面预览截图</span></div>
      <div class="task-sider-subtitle"><span>任务操作</span></div>
      <div class="task-sider-content">
         <div class="content-wrap">
            <p>陌陌最终推广页面预览截图：</p>
         </div>
         <dl class="content-wrap">
            <dt>外层入口截图：</dt>
            <dd class="margin-left-120 positionrelative">
               <div class="ajaxFileUploadInputDiv">
                  <input id="showFileNameInput1" type="text" readonly="readonly">
                  <div id="pickId1" class="filePickIds">选择文件</div>
               </div>
               <div class="fileProgressDiv">
                  <div id="fileProgressDiv1" class="fileProgressWrap">
                  <#if (flowdata.outerImgMomo)?? && (flowdata.outerImgMomo?size > 0) >								
								<#list flowdata.outerImgMomo as fileinfo>								
									<div class="uploadfileprogress">
											<span class="propgressfilename" title="${fileinfo.fileName}">${fileinfo.fileName}</span>
											<span class="fileProgress" title="已上传100%." style="width:100%"></span>
											<span class="fileProgressTip">100%</span>
											<a href="javascript:;" class="filedelete icon-trash" data-fid="orderFileIdMomo1" data-fname="${fileinfo.fileName}" data-input="showFileNameInputFriends" data-filename="${fileinfo.fileName}" data-fileid="${fileinfo.id}" title="删除"></a>
									</div>		
								</#list>						
						</#if>
                  </div>
               </div>
            </dd>
         </dl>
         <dl class="content-wrap">
            <dt>落地页截图：</dt>
            <dd class="margin-left-120 positionrelative">
               <div class="ajaxFileUploadInputDiv">
                  <input id="showFileNameInput2" type="text" readonly="readonly">
                  <div id="pickId2" class="filePickIds">选择文件</div>
               </div>
               <div class="fileProgressDiv">
                  <div id="fileProgressDiv2" class="fileProgressWrap">
                  <#if (flowdata.innerImgMomo)?? && (flowdata.innerImgMomo?size > 0) >								
								<#list flowdata.innerImgMomo as fileinfo>								
									<div class="uploadfileprogress">
											<span class="propgressfilename" title="${fileinfo.fileName}">${fileinfo.fileName}</span>
											<span class="fileProgress" title="已上传100%." style="width:100%"></span>
											<span class="fileProgressTip">100%</span>
											<a href="javascript:;" class="filedelete icon-trash" data-fid="orderFileIdMomo2" data-fname="${fileinfo.fileName}" data-input="showFileNameInputFriends" data-filename="${fileinfo.fileName}" data-fileid="${fileinfo.id}" title="删除"></a>
									</div>		
								</#list>						
						</#if>
                  </div>
               </div>
            </dd>
         </dl>
      </div>
   </div>
   <div class="task-sider">
      <div class="task-sider-title"><span>陌陌推广上线时间</span></div>
      <div class="task-sider-subtitle"><span>任务操作</span></div>
      <div class="task-sider-content">
         <dl class="content-wrap">
            <dt>陌陌推广上线时间：</dt>
            <dd class="margin-left-120">
               <input type="text" id="${taskId!''}momopromotedate1014" <#if (flowdata.momoPromoteDate)??>value="${flowdata.momoPromoteDate}"</#if> onclick="WdatePicker({dateFmt:'yyyy-MM-dd', isShowClear:false, readOnly:false});" class="erpstore-input Wdate">
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
         <button type="button" class="btn btn-large" onclick="submit_${taskId!''}submitForm1014(1,'Y')">确定完成</button>
      </div>
   </div>
</div>
<input type="hidden" name="orderFileId1" id="orderFileId1" value="">
<input type="hidden" name="orderFileName1" id="orderFileName1" value="">
<input type="hidden" name="orderFileId2" id="orderFileId2" value="">
<input type="hidden" name="orderFileName2" id="orderFileName2" value="">
<script type="text/javascript">
   var task_flow_version = '3.2';
   $(function() {
      var promotOnlineMomo = function() {
         var applyUploader = function() {
            applyWebuploader("${taskDefKey!''}", "${procInsId!''}", "朋友圈外层入口截图", "../workfile/file/uploads", "pickId1", "showFileNameInput1", "fileProgressDiv1", "orderFileIdMomo1", "orderFileName1");
            applyWebuploader("${taskDefKey!''}", "${procInsId!''}", "朋友圈落地页截图", "../workfile/file/uploads", "pickId2", "showFileNameInput2", "fileProgressDiv2", "orderFileIdMomo2", "orderFileName2");
         },
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

      promotOnlineMomo.init();
   })
</script>