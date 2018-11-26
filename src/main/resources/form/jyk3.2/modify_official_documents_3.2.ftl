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
	<h3 class="task-title">修改文案</h3>
	<div class="task-sider">
		<div class="task-sider-title"><span>修改建议</span></div>
		<div class="task-sider-content">
			<dl class="content-wrap margin-left-0">
				<dt class="width0">&nbsp;</dt>
				<dd class="margin-left-0">
					<textarea class="content-textarea" readonly placeholder="修改建议"><#if (flowdata.officalFailReason)??>${flowdata.officalFailReason}</#if></textarea>
				</dd>
			</dl>
		</div>
	</div>
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
	<#if chooseFriendFlag?? && chooseFriendFlag =='Y' >
		<div class="task-sider">
			<div class="task-sider-title"><span>输出朋友圈推广文案</span></div>
			<div class="task-sider-subtitle"><span>任务操作</span></div>
			<div class="task-sider-content">
				<div style="width:100%;height:360px;" id="editorFriend"></div>
			</div>
		</div>
	</#if>
	<#if chooseMicroblogFlag?? && chooseMicroblogFlag =='Y' >
		<div class="task-sider">
			<div class="task-sider-title"><span>输出朋微博推广文案</span></div>
			<div class="task-sider-subtitle"><span>任务操作</span></div>
			<div class="task-sider-content">
				<div style="width:100%;height:360px;" id="editorWeibo"></div>
			</div>
		</div>
	</#if>
	<#if chooseMomoFlag?? && chooseMomoFlag =='Y' >
		<div class="task-sider">
			<div class="task-sider-title"><span>输出陌陌推广文案</span></div>
			<div class="task-sider-subtitle"><span>任务操作</span></div>
			<div class="task-sider-content">
				<div style="width:100%;height:360px;" id="editorMomo"></div>
			</div>
		</div>
	</#if>
	<div class="task-sider">
		<h4 class="task-sider-title">确定推广文案完整输入</h4>
		<div class="task-sider-subtitle"><span>任务操作</span></div>
		<div class="task-sider-content">
			<dl class="content-wrap">
				<dt>是否完成推广文案完整输入:</dt>
				<dd>
					<label class="content-label"><input <#if (flowdata.officalmodifycheckvalue)?? && flowdata.officalmodifycheckvalue =='N'> checked="checked" </#if> type="radio" value="N" name="isFinish">否</label>
					<label class="content-label"><input <#if (flowdata.officalmodifycheckvalue)?? && flowdata.officalmodifycheckvalue =='Y'> checked="checked" </#if> type="radio" value="Y" name="isFinish">是</label>
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
			<button type="button" class="btn btn-large"  onclick="submit_${taskId!''}submitForm1014(1)">确定完成</button>
		</div>
	</div>
</div>

<script type="text/javascript">
	var task_flow_version = '3.2';
	
	function submit_${taskId!''}submitForm1014(isLocation){
		
			var submit = function (v, h, f) {
				if (v == 'ok') {
						$.jBox.tip("正在修改，请稍等...", 'loading', {
						timeout : 3000,
						persistent : true
				});
				$.post("../jyk/flow/3p2/modify_official_documents_3.2", {
						taskId:$("#${taskId!''}taskId1014").val(),
						procInsId:$("#${taskId!''}procInsId1014").val(),
						splitId:$("#${taskId!''}splitId1014").val(),
						editorWeibo: encodeURI(UE.getEditor('editorWeibo').getContent()),
						editorFriend: encodeURI(UE.getEditor('editorFriend').getContent()),
						editorMomo: encodeURI(UE.getEditor('editorMomo').getContent()),
						checkvalue: $("input[name=isFinish]:checked").val()
					}, 
				function(data) {
					if (data.result) {
						if(isLocation==1){
							updatePage();
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
	
	$(function(){
        
        //判断ueditor 编辑器是否创建成功
        var ue = UE.getEditor('editorFriend');
        ue.addListener("ready", function () {
			ue.execCommand('inserthtml', '<#if (flowdata.editorFriend)??>${flowdata.editorFriend}</#if>');
			ue.execCommand('serverparam', {'procInsId': '${procInsId!''}','taskDefKey': '${taskDefKey!''}','fileTitle': '朋友圈推广文案附件'});
        });
        
        var ae = UE.getEditor('editorWeibo');
        ae.addListener("ready", function () {
			ae.execCommand('inserthtml', '<#if (flowdata.editorWeibo)??>${flowdata.editorWeibo}</#if>');
			ae.execCommand('serverparam', {'procInsId': '${procInsId!''}','taskDefKey': '${taskDefKey!''}','fileTitle': '微博推广文案附件'});
        });
        
        var ce = UE.getEditor('editorMomo');
        ce.addListener("ready", function () {
			ce.execCommand('inserthtml', '<#if (flowdata.editorMomo)??>${flowdata.editorMomo}</#if>');
			ce.execCommand('serverparam', {'procInsId': '${procInsId!''}','taskDefKey': '${taskDefKey!''}','fileTitle': '陌陌推广文案附件'});
        });
        
    });
</script>
</#if>