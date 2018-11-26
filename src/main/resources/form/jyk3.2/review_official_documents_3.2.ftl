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
	<h3 class="task-title">审核文案</h3>
	<div class="task-sider">
		<div class="task-sider-title"><span>查看推广提案</span></div>
		<div class="task-sider-subtitle"><span>相关资料</span></div>
		<div class="task-sider-content">
			<dl class="content-wrap">
				<dt>经营诊断与策划（文案）:</dt>
				<dd><a href="javascript:;">查看</a></dd>
			</dl>
		</div>
	</div>
	<div class="task-sider">
		<div class="task-sider-title"><span>推广文案是否通过？</span></div>
		<div class="task-sider-subtitle"><span>审核内容</span></div>
		<div class="task-sider-content">
			<#if chooseFriendFlag?? && chooseFriendFlag =='Y' >
				<dl class="content-wrap">
					<dt>推广文案 - 朋友圈:</dt>
					<dd class="margin-left-120">
						<div style="width:100%;height:360px;" contenteditable="false" id="editorFriend"></div>
					</dd>
				</dl>
			</#if>
			<#if chooseMicroblogFlag?? && chooseMicroblogFlag =='Y' >
				<dl class="content-wrap">
					<dt>推广文案 - 微博:</dt>
					<dd class="margin-left-120">
						<div style="width:100%;height:360px;" contenteditable="false" id="editorWeibo"></div>
					</dd>
				</dl>
			</#if>
			<#if chooseMomoFlag?? && chooseMomoFlag =='Y' >
				<dl class="content-wrap">
					<dt>推广文案 - 陌陌:</dt>
					<dd class="margin-left-120">
						<div style="width:100%;height:360px;" contenteditable="false" id="editorMomo"></div>
					</dd>
				</dl>
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
<input type='hidden' id='${taskId!''}taskId1014' name='${taskId!''}taskId1014' value="${taskId!''}"/>
<input type='hidden' id='${taskId!''}splitId1014' name='${taskId!''}splitId1014' value="${splitId!''}"/>
<input type='hidden' id='${taskId!''}procInsId1014' name='${taskId!''}procInsId1014'  value="${procInsId!''}"/>
<script type="text/javascript">
	var task_flow_version = '3.2';
	var editorFriend = UE.getEditor('editorFriend', {
        initialFrameWidth: "100%",readonly: true
    });
	var editorWeibo = UE.getEditor('editorWeibo', {
        initialFrameWidth: "100%",readonly: true
    });
	var editorMomo = UE.getEditor('editorMomo', {
        initialFrameWidth: "100%",readonly: true
    });

	$(function() {
		var reviewOffcialDoc = function() {
			var submit = function(text, checkvalue) {
					var subm = function (v, h, f) {
						if (v == 'ok') {
							$.jBox.tip("正在修改，请稍等...", 'loading', {
							timeout : 3000,
							persistent : true
						});
						$.post("../jyk/flow/3p2/review_official_documents_3.2", {
								taskId: $("#${taskId!''}taskId1014").val(),
								procInsId: $("#${taskId!''}procInsId1014").val(),
								splitId: $("#${taskId!''}splitId1014").val(),
								checkvalue: checkvalue,
								reason: text
							}, 
							function(data) {
								if (data.result) {
									if (typeof updatePage === 'function') {
							updatePage();
						} else {
							window.location.href="../workflow/tasklist";
						}
									/*if(isLocation==1){
										if (typeof updatePage === 'function') {
							updatePage();
						} else {
							window.location.href="../workflow/tasklist";
						}
									}else{
										window.location.reload();
									}*/
								} else {
									$.jBox.closeTip();
									$.jBox.info(data.message);
								}
							});
						}
						return true; 
					};
					top.$.jBox.confirm("确认提交吗？", "提示", subm);
				},
				bindEvent = function() {
					$(document).on('click', '.recharge', function(event) {
						event.preventDefault();
						erpShopApp.auditConfirm(function(text) {
							submit(text, 'N');
						})
					}).on('click', '.rechargeok', function(event) {
						event.preventDefault();
						submit(null, 'Y');
					});
				},
				initEditor = function() {
					//判断ueditor 编辑器是否创建成功
			        var ue = UE.getEditor('editorFriend');
			        ue.addListener("ready", function () {
						ue.execCommand('inserthtml', '<#if (flowdata.editorFriend)??>${flowdata.editorFriend}</#if>');
			        });
			        
			        var ae = UE.getEditor('editorWeibo');
			        ae.addListener("ready", function () {
						ae.execCommand('inserthtml', '<#if (flowdata.editorWeibo)??>${flowdata.editorWeibo}</#if>');
			        });
			        
			        var ce = UE.getEditor('editorMomo');
			        ce.addListener("ready", function () {
						ce.execCommand('inserthtml', '<#if (flowdata.editorMomo)??>${flowdata.editorMomo}</#if>');
			        });
				},
				init = function() {
					initEditor();
					bindEvent();
				};
			return {
				init: init
			}
		}();
		reviewOffcialDoc.init();
	})
</script>