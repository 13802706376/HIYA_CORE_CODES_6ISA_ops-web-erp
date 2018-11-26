<script type="text/javascript">

function submit_${taskId!''}submitForm1014(isLocation,flag){
	
	
 	var outerImgFriends =	$("[data-fid='orderFileIdFriends1']");
 	var innerImgFriends =	$("[data-fid='orderFileIdFriends2']");
 	var outerImgWeibo =	$("[data-fid='orderFileIdWeibo1']");
 	var innerImgWeibo =	$("[data-fid='orderFileIdWeibo2']");
 	var outerImgMomo =	$("[data-fid='orderFileIdMomo1']");
 	var innerImgMomo =	$("[data-fid='orderFileIdMomo2']");
 	
 	
 	var outerImgFriends1 =	[];
 	var innerImgFriends1 =	[];
 	var outerImgWeibo1 =	[];
 	var innerImgWeibo1 =	[];
 	var outerImgMomo1 =	[];
 	var innerImgMomo1 =	[];
 	
 
	outerImgFriends.each(function(){
		outerImgFriends1.push($(this).attr("data-fileid"));
	});
	innerImgFriends.each(function(){
		innerImgFriends1.push($(this).attr("data-fileid"));
	});
	outerImgWeibo.each(function(){
		outerImgWeibo1.push($(this).attr("data-fileid"));
	});
	innerImgWeibo.each(function(){
		innerImgWeibo1.push($(this).attr("data-fileid"));
	});
	outerImgMomo.each(function(){
		outerImgMomo1.push($(this).attr("data-fileid"));
	});
	innerImgMomo.each(function(){
		innerImgMomo1.push($(this).attr("data-fileid"));
	});
	console.log(outerImgFriends1);
	console.log(innerImgFriends1);
	console.log(outerImgWeibo1);
	console.log(innerImgWeibo1);
	console.log(outerImgMomo1);
	console.log(innerImgMomo1);
	
	var submit = function (v, h, f) {
			if (v == 'ok') {
					$.jBox.tip("正在修改，请稍等...", 'loading', {
					timeout : 3000,
					persistent : true
			});
			$.post("../jyk/flow/3p2/work_promotion_scheme_preview_3.2", {
					taskId:$("#${taskId!''}taskId1014").val(),
					procInsId:$("#${taskId!''}procInsId1014").val(),
					splitId:$("#${taskId!''}splitId1014").val(),
					outerImgFriends: outerImgFriends1.toString(),
					innerImgFriends: innerImgFriends1.toString(),
					outerImgWeibo: outerImgWeibo1.toString(),
					innerImgWeibo: innerImgWeibo1.toString(),
					outerImgMomo: outerImgMomo1.toString(),
					innerImgMomo: innerImgMomo1.toString(),
					qrcode:$("#${taskId!''}qrcode1014").prop("checked"),
					checkvalue:$("#fileProgressDivFriends1").length
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
	<h3 class="task-title">输出推广页面预览给策划专家</h3>
	<div class="task-sider">
		<div class="task-sider-title"><span>朋友圈推广页面预览</span></div>
		<div class="task-sider-subtitle"><span>相关资料</span></div>
		<div class="task-sider-content">
			<div class="content-wrap">
				<p>推广文案</p>
				<p><a href="">《推广文案朋友圈》</a></p>
				<p>设计稿 - 朋友圈</p>
				<#if (flowdata.pickIdFriends)?? && (flowdata.pickIdFriends?size > 0) >								
								<#list flowdata.pickIdFriends as fileinfo>								
									<p><a href="${fileinfo.filePath}">${fileinfo.fileName}</a></p>		
								</#list>
														
						</#if>
			</div>
		</div>
		<#if chooseFriendFlag?? && chooseFriendFlag =='Y' >
		<div class="task-sider-subtitle"><span>相关资料</span></div>
		<div class="task-sider-title"><span>朋友圈推广页面预览截图</span></div>
		<div class="task-sider-content">
			<dl class="content-wrap">
				<dt>外层入口截图：</dt>
				<dd class="margin-left-120 positionrelative">
					<div class="ajaxFileUploadInputDiv">
						<input id="showFileNameInputFriends1" type="text" readonly="readonly">
						<div id="pickIdFriends1" class="filePickIds">选择文件</div>
					</div>
					<div class="fileProgressDiv">
						<div id="fileProgressDivFriends1" class="fileProgressWrap">
						<#if (flowdata.outerImgFriends)?? && (flowdata.outerImgFriends?size > 0) >								
								<#list flowdata.outerImgFriends as fileinfo>								
									<div class="uploadfileprogress">
											<span class="propgressfilename" title="${fileinfo.fileName}">${fileinfo.fileName}</span>
											<span class="fileProgress" title="已上传100%." style="width:100%"></span>
											<span class="fileProgressTip">100%</span>
											<a href="javascript:;" class="filedelete icon-trash" data-fid="orderFileIdFriends1" data-fname="${fileinfo.fileName}" data-input="showFileNameInputFriends" data-filename="${fileinfo.fileName}" data-fileid="${fileinfo.id}" title="删除"></a>
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
						<input id="showFileNameInputFriends2" type="text" readonly="readonly">
						<div id="pickIdFriends2" class="filePickIds">选择文件</div>
					</div>
					<div class="fileProgressDiv">
						<div id="fileProgressDivFriends2" class="fileProgressWrap">
							<#if (flowdata.innerImgFriends)?? && (flowdata.innerImgFriends?size > 0) >								
								<#list flowdata.innerImgFriends as fileinfo>								
									<div class="uploadfileprogress">
											<span class="propgressfilename" title="${fileinfo.fileName}">${fileinfo.fileName}</span>
											<span class="fileProgress" title="已上传100%." style="width:100%"></span>
											<span class="fileProgressTip">100%</span>
											<a href="javascript:;" class="filedelete icon-trash" data-fid="orderFileIdFriends2" data-fname="${fileinfo.fileName}" data-input="showFileNameInputFriends" data-filename="${fileinfo.fileName}" data-fileid="${fileinfo.id}" title="删除"></a>
									</div>		
								</#list>						
						</#if>
						</div>
					</div>
				</dd>
			</dl>
			<div class="content-wrap">
				<div>
					<label class="content-label">
						<input type="checkbox" id="${taskId!''}qrcode1014" <#if (flowdata.qrcode)?? && flowdata.qrcode =='true'> checked="checked" </#if>  name="tomp" value="Y">朋友圈：输出推广页面预览二维码发给策划专家
					</label>
				</div>
			</div>
		</div>
	</div>
	</#if>
	<#if chooseMicroblogFlag?? && chooseMicroblogFlag =='Y' >
	<div class="task-sider">
		<div class="task-sider-title"><span>微博推广页面预览</span></div>
		<div class="task-sider-subtitle"><span>相关资料</span></div>
		<div class="task-sider-content">
			<div class="content-wrap">
				<p>推广文案</p>
				<p><a href="">《推广文案微博》</a></p>
				<p>设计稿 - 微博</p>
				<#if (flowdata.pickIdWeibo)?? && (flowdata.pickIdWeibo?size > 0) >								
								<#list flowdata.pickIdWeibo as fileinfo>								
									<p><a href="${fileinfo.filePath}">${fileinfo.fileName}</a></p>		
								</#list>
														
						</#if>
				<p>微博账号和资质资料<a href="javascript:;" class="openWeiboForm" data-datas="${storeId!''},zhixiao">查看</a></p>
			</div>
		</div>
		<div class="task-sider-subtitle"><span>相关资料</span></div>
		<div class="task-sider-title"><span>微博推广页面预览截图</span></div>
		<div class="task-sider-content">
			<dl class="content-wrap">
				<dt>外层入口截图：</dt>
				<dd class="margin-left-120 positionrelative">
					<div class="ajaxFileUploadInputDiv">
						<input id="showFileNameInputWeibo1" type="text" readonly="readonly">
						<div id="pickIdWeibo1" class="filePickIds">选择文件</div>
					</div>
					<div class="fileProgressDiv">
						<div id="fileProgressDivWeibo1" class="fileProgressWrap">
							<#if (flowdata.outerImgWeibo)?? && (flowdata.outerImgWeibo?size > 0) >								
								<#list flowdata.outerImgWeibo as fileinfo>								
									<div class="uploadfileprogress">
											<span class="propgressfilename" title="${fileinfo.fileName}">${fileinfo.fileName}</span>
											<span class="fileProgress" title="已上传100%." style="width:100%"></span>
											<span class="fileProgressTip">100%</span>
											<a href="javascript:;" class="filedelete icon-trash" data-fid="orderFileIdWeibo1" data-fname="${fileinfo.fileName}" data-input="showFileNameInputFriends" data-filename="${fileinfo.fileName}" data-fileid="${fileinfo.id}" title="删除"></a>
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
						<input id="showFileNameInputWeibo2" type="text" readonly="readonly">
						<div id="pickIdWeibo2" class="filePickIds">选择文件</div>
					</div>
					<div class="fileProgressDiv">
						<div id="fileProgressDivWeibo2" class="fileProgressWrap">
							<#if (flowdata.innerImgWeibo)?? && (flowdata.innerImgWeibo?size > 0) >								
								<#list flowdata.innerImgWeibo as fileinfo>								
									<div class="uploadfileprogress">
											<span class="propgressfilename" title="${fileinfo.fileName}">${fileinfo.fileName}</span>
											<span class="fileProgress" title="已上传100%." style="width:100%"></span>
											<span class="fileProgressTip">100%</span>
											<a href="javascript:;" class="filedelete icon-trash" data-fid="orderFileIdWeibo2" data-fname="${fileinfo.fileName}" data-input="showFileNameInputFriends" data-filename="${fileinfo.fileName}" data-fileid="${fileinfo.id}" title="删除"></a>
									</div>		
								</#list>						
						</#if>
						</div>
					</div>
				</dd>
			</dl>
		</div>
	</div>
	</#if>
	<#if chooseMomoFlag?? && chooseMomoFlag =='Y' >
	<div class="task-sider">
		<div class="task-sider-title"><span>陌陌推广页面预览</span></div>
		<div class="task-sider-subtitle"><span>相关资料</span></div>
		<div class="task-sider-content">
			<div class="content-wrap">
				<p>推广文案</p>
				<p><a href="">《推广文案陌陌》</a></p>
				<p>设计稿 - 陌陌</p>
				<#if (flowdata.pickIdMomo)?? && (flowdata.pickIdMomo?size > 0) >								
								<#list flowdata.pickIdMomo as fileinfo>								
									<p><a href="${fileinfo.filePath}">${fileinfo.fileName}</a></p>		
								</#list>
														
						</#if>
			</div>
		</div>
		<div class="task-sider-subtitle"><span>相关资料</span></div>
		<div class="task-sider-title"><span>陌陌推广页面预览截图</span></div>
		<div class="task-sider-content">
			<dl class="content-wrap">
				<dt>外层入口截图：</dt>
				<dd class="margin-left-120 positionrelative">
					<div class="ajaxFileUploadInputDiv">
						<input id="showFileNameInputMomo1" type="text" readonly="readonly">
						<div id="pickIdMomo1" class="filePickIds">选择文件</div>
					</div>
					<div class="fileProgressDiv">
						<div id="fileProgressDivMomo1" class="fileProgressWrap">
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
						<input id="showFileNameInputMomo2" type="text" readonly="readonly">
						<div id="pickIdMomo2" class="filePickIds">选择文件</div>
					</div>
					<div class="fileProgressDiv">
						<div id="fileProgressDivMomo2" class="fileProgressWrap">
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
			<dl class="content-wrap">
				<dt>陌陌落地页链接：</dt>
				<dd class="margin-left-120 positionrelative">
					<a href="${flowdata.momoLink!''}" target="_blank">${flowdata.momoLink!''}</a>
				</dd>
			</dl>
		</div>
		</#if>
	</div>
	<div class="task-detail-footer">
		<div class="footer-info">
			<p>负责人：${taskUser!''}</p>
			<p>${startDate!''} —— ${endDate!''}</p>
		</div>
		<div class="footer-btn">
			<button type="button" class="btn btn-large" onclick="submit_${taskId!''}submitForm1014(1,'Y')">确认完成</button>
		</div>
	</div>
</div>
</#if>
<input type="hidden" name="orderFileIdFriends1" id="orderFileIdFriends1" value="">
<input type="hidden" name="orderFileNameFriends1" id="orderFileNameFriends1" value="">
<input type="hidden" name="orderFileIdFriends2" id="orderFileIdFriends2" value="">
<input type="hidden" name="orderFileNameFriends2" id="orderFileNameFriends2" value="">
<input type="hidden" name="orderFileIdWeibo1" id="orderFileIdWeibo1" value="">
<input type="hidden" name="orderFileNameWeibo1" id="orderFileNameWeibo1" value="">
<input type="hidden" name="orderFileIdWeibo2" id="orderFileIdWeibo2" value="">
<input type="hidden" name="orderFileNameWeibo2" id="orderFileNameWeibo2" value="">
<input type="hidden" name="orderFileIdMomo1" id="orderFileIdMomo1" value="">
<input type="hidden" name="orderFileNameMomo1" id="orderFileNameMomo1" value="">
<input type="hidden" name="orderFileIdMomo2" id="orderFileIdMomo2" value="">
<input type="hidden" name="orderFileNameMomo2" id="orderFileNameMomo2" value="">
<script type="text/javascript">
	var task_flow_version = '3.2';
	$(function() {
		var workPromotionSchemePreview = function() {
			var applyUploader = function() {
				applyWebuploader("${taskDefKey!''}", "${procInsId!''}", "朋友圈外层入口截图", "../workfile/file/uploads", "pickIdFriends1", "showFileNameInputFriends1", "fileProgressDivFriends1", "orderFileIdFriends1", "orderFileNameFriends1");
				applyWebuploader("${taskDefKey!''}", "${procInsId!''}", "朋友圈落地页截图", "../workfile/file/uploads", "pickIdFriends2", "showFileNameInputFriends2", "fileProgressDivFriends2", "orderFileIdFriends2", "orderFileNameFriends2");
				applyWebuploader("${taskDefKey!''}", "${procInsId!''}", "微博外层入口截图", "../workfile/file/uploads", "pickIdWeibo1", "showFileNameInputWeibo1", "fileProgressDivWeibo1", "orderFileIdWeibo1", "orderFileNameWeibo1");
				applyWebuploader("${taskDefKey!''}", "${procInsId!''}", "微博落地页截图", "../workfile/file/uploads", "pickIdWeibo2", "showFileNameInputWeibo2", "fileProgressDivWeibo2", "orderFileIdWeibo2", "orderFileNameWeibo2");
				applyWebuploader("${taskDefKey!''}", "${procInsId!''}", "陌陌外层入口截图", "../workfile/file/uploads", "pickIdMomo1", "showFileNameInputMomo1", "fileProgressDivMomo1", "orderFileIdMomo1", "orderFileNameMomo1");
				applyWebuploader("${taskDefKey!''}", "${procInsId!''}", "陌陌落地页截图", "../workfile/file/uploads", "pickIdMomo2", "showFileNameInputMomo2", "fileProgressDivMomo2", "orderFileIdMomo2", "orderFileNameMomo2");
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

		workPromotionSchemePreview.init();
	})
</script>