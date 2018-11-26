<script type="text/javascript">
function submit_${taskId!''}submitForm1016(isLocation){
	var channels = ''; 
	var channelName = [];
	$('input[name="${taskId!''}channelname1016"]:checked').each(function(){ 
		channels+=$(this).val()+","; 
		channelName.push($(this).next('span').text());
	});
	
	var friendsVal = $("#friendsVal").val();
	var weiboVal = $("#weiboVal").val();
	var momoVal = $("#momoVal").val();
	
	var isFinished = false;
	if(channelName.length === 1){
		if(friendsVal === '1' && (weiboVal === '2' || momoVal === '3')){
			if(($.trim($('#${taskId!''}wechatCardLinkPath').val())!="" || $.trim($('#${taskId!''}wechatCardLink1016').val()) !="") 
				&& ($.trim($('#${taskId!''}mobileCardLinkPath').val())!="" || $.trim($('#${taskId!''}mobileCardLink1016').val()) !="")){
				isFinished = true;
			}
		}
		
		if(friendsVal === '1' && weiboVal != '2' && momoVal != '3'){
			if($.trim($('#${taskId!''}wechatCardLinkPath').val())!="" || $.trim($('#${taskId!''}wechatCardLink1016').val()) !=""){
				isFinished = true;
			}
		}
		
		if(friendsVal != '1' && (weiboVal === '2' || momoVal === '3')){
			if($.trim($('#${taskId!''}mobileCardLinkPath').val())!="" || $.trim($('#${taskId!''}mobileCardLink1016').val()) !=""){
				isFinished = true;
			}
		}
	}
	
	if(channels === '' && channelName.length === 0) {
		if(friendsVal === '1' && (weiboVal === '2' || momoVal === '3')){
			if($.trim($('#${taskId!''}wechatCardLinkPath').val()) ==="" && $.trim($('#${taskId!''}wechatCardLink1016').val()) ===""
				&& $.trim($('#${taskId!''}mobileCardLinkPath').val()) ==="" && $.trim($('#${taskId!''}mobileCardLink1016').val()) ===""){
				return;
			}
		}
		
		if(friendsVal === '1' && weiboVal != '2' && momoVal != '3'){
			if($.trim($('#${taskId!''}wechatCardLinkPath').val()) ==="" && $.trim($('#${taskId!''}wechatCardLink1016').val()) ===""){
				return;
			}
		}
		
		if(friendsVal != '1' && (weiboVal === '2' || momoVal === '3')){
			if($.trim($('#${taskId!''}mobileCardLinkPath').val()) ==="" && $.trim($('#${taskId!''}mobileCardLink1016').val()) ===""){
				return;
			}
		}
	}
	
	var wechatCardLink = '';
	var mobileCardLink = '';
	if(friendsVal === '1' && (weiboVal === '2' || momoVal === '3')){
		wechatCardLink = $.trim($('#${taskId!''}wechatCardLink1016').val());
		mobileCardLink = $.trim($('#${taskId!''}mobileCardLink1016').val());
	}
	
	if(friendsVal === '1' && weiboVal != '2' && momoVal != '3'){
		wechatCardLink = $.trim($('#${taskId!''}wechatCardLink1016').val());
	}
	
	if(friendsVal != '1' && (weiboVal === '2' || momoVal === '3')){
		mobileCardLink = $.trim($('#${taskId!''}mobileCardLink1016').val());
	}
	
	var submit = function (v, h, f) {
			if (v == 'ok') {
				$.jBox.tip("正在修改，请稍等...", 'loading', {
				timeout : 3000,
				persistent : true
			});
			$.post("../jyk/flow/work_output_card_coupon", {
				channelList : channels,
				wechatCardLink : wechatCardLink,
				mobileCardLink : mobileCardLink,
				isFinished:isFinished,
				taskId:$("#${taskId!''}taskId1016").val(),
				procInsId:$("#${taskId!''}procInsId1016").val()
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
	$.jBox.confirm("确定输出卡券吗？", "提示", submit);
}
</script>
<input type='hidden' id='${taskId!''}taskId1016' name='taskId1016' value="${taskId!''}"/>
<input type='hidden' id='${taskId!''}procInsId1016' name='procInsId1016' value="${procInsId!''}"/>
<div class="act-rs-item  ${detailType!''}  ${followTaskDetailWrap!''}">
	<div class="act-rs-left floatleft">
		<div class="act-rs-title padding15">
			<h3><a href="../workflow/taskDetail?taskId=${taskId!''}&followTaskDetailWrap=${followTaskDetailWrap!''}&detailType=${detailType!''}">${taskName!''}</a></h3>
			<div class="listSubmit">
				<button  onclick="submit_${taskId!''}submitForm1016()" type="button" class="makesrue btn">确定完成</button>
				<a href="javascript:;" class="select-user open-select-user" style="float: left;" onclick="syncSelect('${taskId!''}', '${taskUser!''}','${taskUserId!''}')"></a>
			</div>
			<div class="taskPrincipal"><span class="taskPrincipalName">${taskUser!''}</span></div>
		</div>
		<div class="act-rs-form form-horizontal padding15">
		
			<#if vars?exists>
		        <#list vars?keys as key> 
		            <#if vars["UploadPictureMaterial"]??>
		               <#assign UploadPictureMaterial = vars["UploadPictureMaterial"]>
		            </#if>
		            
		            <#if vars["ZhangbeiInFlag"]??>
		               <#assign ZhangbeiInFlag = vars["ZhangbeiInFlag"]>
		            </#if>
		            
			         <#if vars["friends"]??>
		              	<#assign friends = vars["friends"]>
			         </#if>
			         <#if vars["weibo"]??>
		             	<#assign weibo = vars["weibo"]>
			         </#if>
		            <#if vars["momo"]??>
		              	<#assign momo = vars["momo"]>
			         </#if>
		       </#list>
		    </#if>
		    
		    <input type="hidden" id="friendsVal" value="${friends!''}"/>
		    <input type="hidden" id="weiboVal" value="${weibo!''}"/>
		    <input type="hidden" id="momoVal" value="${momo!''}"/>
		    
			<div class="rs-label height50" style="height:50px;">
				<div class="control-group">
					<label style="float: left; line-height:30px;">
						<input type="checkbox" name="${taskId!''}channelname1016" value="1" 
						<#if subTaskList?? && (subTaskList?size > 0) >
							<#list subTaskList as subTask>
						 		<#if subTask.subTaskId == "1" && subTask.state=="1">
							 		 checked="true"
							 	</#if>
							 </#list>
						  </#if>
						
						>
						<span class="subTask" value="1" style="float: initial;">登陆商户后台，制作卡券</span>
					</label>
					<div class="controls">
						&nbsp;
					</div>
				</div>
			</div>
			
			<!--
			<div class="rs-label">
				<div class="control-group">
					<label   class="subTask" value="2">输入卡券链接：</label>
					<div class="controls">
					<#if vars?exists>
		                <#list vars?keys as key> 
		                   <#if vars["cardLink"]??>
		                   		<#assign cardLink = vars["cardLink"]>
		                   </#if>
		                </#list>
		          	  </#if>
		            <#if cardLink?exists>
		            		 ${cardLink!""}
			            <input type='hidden' id='${taskId!''}cardLinkPath' name='${taskId!''}cardLinkPath' value="${cardLink!""}" />
			        <#else>
						<input type="text" name="${taskId!''}cardLink" id="${taskId!''}cardLink1016" maxlength="500">
				    </#if>
					</div>
				</div>
			</div>
			-->
			
			<label class="subTask" value="2"></label>
			<#if friends?exists && friends=="1" >
				<div class="rs-label">
					<div class="control-group">
						<label style="float:left;">
							微信领券链接：
						</label>
						<div class="controls">
							<#if vars?exists>
				                <#list vars?keys as key> 
				                   <#if vars["wechatCardLink"]??>
				                   		<#assign wechatCardLink = vars["wechatCardLink"]>
				                   </#if>
				                </#list>
				          	</#if>
				            <#if wechatCardLink?exists>
				            	${wechatCardLink!""}
					            <input type='hidden' id='${taskId!''}wechatCardLinkPath' name='${taskId!''}wechatCardLinkPath' value="${wechatCardLink!""}" />
					        <#else>
								<input type="text" id="${taskId!''}wechatCardLink1016" name="${taskId!''}wechatCardLink" maxlength="500">
						    </#if>
						</div>
					</div>
				</div>
			</#if>
			<#if (weibo?exists && weibo=="2") || (momo?exists && momo=="3") >
				<div class="rs-label">
					<div class="control-group">
						<label style="float:left;">
							手机领券链接：
						</label>	
						<div class="controls">
							<#if vars?exists>
				                <#list vars?keys as key> 
				                   <#if vars["mobileCardLink"]??>
				                   		<#assign mobileCardLink = vars["mobileCardLink"]>
				                   </#if>
				                </#list>
				          	</#if>
				            <#if mobileCardLink?exists>
				            	${mobileCardLink!""}
					            <input type='hidden' id='${taskId!''}mobileCardLinkPath' name='${taskId!''}mobileCardLinkPath' value="${mobileCardLink!""}" />
					        <#else>
								<input type="text" id="${taskId!''}mobileCardLink1016" name="${taskId!''}cardLink" maxlength="500">
						    </#if>
						</div>
					</div>
				</div>
			 </#if> 
		</div>
	</div>
	<div class="act-rs-right">
		<div class="padding15">
			<div class="act-time">开始：${startDate!''}</div>
			<div class="act-time">到期：${endDate!''}</div>
			<div class="act-jd">
			 <#if taskConsumTime?? && (taskConsumTime < taskConsumTimeMin) >
					<div class="progress progress-success">
						<div class="bar" style="width: ${taskConsumTime!''}%"></div>
					</div>
			<#elseif taskConsumTime?? && (taskConsumTime > taskConsumTimeMin && taskConsumTime < taskConsumTimeMax)>
					<div class="progress progress-warning">
						<div class="bar" style="width: ${taskConsumTime!''}%"></div>
					</div>
			<#else>
				<div class="progress progress-danger">
						<div class="bar" style="width: 100%"></div>
					</div>
			</#if>
			</div>
			<div class="act-jd-word positionrelative"><div class="acjw positionrabsolute" style="left: 80%;">${taskConsumTime!''}%</div></div>
		</div>
	</div>
	<div class="act-process-left-footer" >
		<div class="footer-info floatleft">
			<p></p>
			<p>负责人：${taskUser!''}</p>
		</div>
		<div class="footer-btn floatright">
			<button style="button" class="btn btn-large"  onclick="submit_${taskId!''}submitForm1016(1)">确定完成</button>
			<span class="flowTaskState hide">已完成</span>
		</div>
	</div>
</div>