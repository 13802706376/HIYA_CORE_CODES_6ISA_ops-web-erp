<script type="text/javascript">
	
	function submit_${taskId!''}submitForm12(isLocation){
	
		    var storeId = '';
		    var	storeName = '';
			var countrySelect = document.getElementById("${taskId!''}selectStoreId12");
			if(null!=countrySelect)
			{
			   var index = countrySelect.selectedIndex;//获取选中的index 888888
			    storeId = countrySelect.options[index].value;
			    storeName = countrySelect.options[index].text;
			}
			
			var channels = []; 
			$('input[name="${taskId!''}channels"]:checked').each(function(){ 
				channels.push($(this).val()); 
			});
			
			
			var license = $('input[name="${taskId!''}license"]:checked').val(),
			qualification = $('input[name="${taskId!''}qualification"]:checked').val(),
			extensionExpect = $('input[name="${taskId!''}extensionExpect"]:checked').val(),
			nextLicenseTime = $('input[name="${taskId!''}nextLicenseTime"]').val(),
			nextQualificationTime = $('input[name="${taskId!''}nextQualificationTime"]').val(),
			nextExtensionExpectTime = $('input[name="${taskId!''}nextExtensionExpectTime"]').val(),
			promotionTime = $('input[name="${taskId!''}promotionTime"]').val(),
			isFinished = false;


		if(license == '1' && qualification == '1' && extensionExpect == '1'){
			if(promotionTime === ''){
				$.jBox.tip("请选择预期投放时间！", 'error');
				return;
			}
			
			if(storeId === '') {
			    $.jBox.tip("请选择门店！", 'error');
			    return;
		    }
			isFinished = true;
		}	
		
		var submit = function (v, h, f) {
				if (v == 'ok') {
						$.jBox.tip("正在修改，请稍等...", 'loading', {
						timeout : 3000,
						persistent : true
				});
				// $.post("../jyk/flow/accountZhixiao/contact_shop_zhixiao_latest", {
				$.post("../jyk/flow/3p5/contact_shop_3.5", {
						storeId : storeId,
						storeName : storeName,
						channels : channels.toString(),
						license : license,
						qualification : qualification,
						extensionExpect : extensionExpect,
						nextLicenseTime : nextLicenseTime,
						nextQualificationTime : nextQualificationTime,
						nextExtensionExpectTime : nextExtensionExpectTime,
						promotionTime : promotionTime,
						isFinished : isFinished,
						taskId : $("#${taskId!''}taskIdTest12").val(),
						procInsId : $("#${taskId!''}procInsIdTest12").val()
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
		$.jBox.confirm("确认与商户对接吗？", "提示", submit);
	}
	
	$(function(){
		$(".${taskId!''}license").click(function(){
			var license = $('input[name="${taskId!''}license"]:checked').val();
			if(license=='1'){
				$("#${taskId!''}qualification").show();
				$("#${taskId!''}nextLicenseTime").hide();
			}else{
				$("#${taskId!''}qualification").hide();
				$("#${taskId!''}nextLicenseTime").show();
				$("#${taskId!''}extensionExpect").hide();
			}
		});
			
		$(".${taskId!''}qualification").click(function(){
			var qualification = $('input[name="${taskId!''}qualification"]:checked').val();
			if(qualification=='1'){
				$("#${taskId!''}extensionExpect").show();
				$("#${taskId!''}nextQualificationTime").hide();
				$("#${taskId!''}pipe").show();
			}else{
				$("#${taskId!''}extensionExpect").hide();
				$("#${taskId!''}nextQualificationTime").show();
				$("#${taskId!''}pipe").hide();
			}
		});
			
		$(".${taskId!''}extensionExpect").click(function(){
			var extensionExpect = $('input[name="${taskId!''}extensionExpect"]:checked').val();
			if(extensionExpect=='1'){
				$("#${taskId!''}nextExtensionExpectTime").hide();
				$("#${taskId!''}promotionTime").show();
				
			}else{
				$("#${taskId!''}nextExtensionExpectTime").show();
				$("#${taskId!''}promotionTime").hide();
				
			}
		});
	});
</script>
<input type='hidden' id='${taskId!''}taskIdTest12' name='taskIdTest' value="${taskId!''}"/>
<input type='hidden' id='${taskId!''}procInsIdTest12' name='procInsIdTest'  value="${procInsId!''}"/>
<div class="act-rs-item  ${detailType!''}  ${followTaskDetailWrap!''}">
	<div class="act-rs-left floatleft">
		<div class="act-rs-title padding15">
				<h3><a href="../workflow/taskDetail?taskId=${taskId!''}&followTaskDetailWrap=${followTaskDetailWrap!''}&detailType=${detailType!''}"><#if orderInfo.orderType==1>直销-<#elseif orderInfo.orderType==2>服务商-</#if>${taskName!''}</a></h3>
			<div class="listSubmit">
				<button  onclick="submit_${taskId!''}submitForm12()" type="button" class="makesrue btn">确定完成</button>
				<a href="javascript:;" class="select-user open-select-user" style="float: left;" onclick="syncSelect('${taskId!''}', '${taskUser!''}','${taskUserId!''}')"></a>
			</div>
			<div class="taskPrincipal"><span class="taskPrincipalName">${taskUser!''}</span></div>
		</div>
		
		<#if vars?exists>
			<#list vars?keys as key> 
				<#if vars["surePromotionButton"]??>
					<#assign surePromotionButton = vars["surePromotionButton"]>
				</#if>
				<#if vars["sureLicenseButton"]??>
					<#assign sureLicenseButton = vars["sureLicenseButton"]>
				</#if>
				<#if vars["sureQualificationButton"]??>
					<#assign sureQualificationButton = vars["sureQualificationButton"]>
				</#if>
				<#if vars["promotionTime"]??>
					<#assign promotionTime = vars["promotionTime"]>
				</#if>
				<#if vars["promotionNextTime"]??>
					<#assign promotionNextTime = vars["promotionNextTime"]>
				</#if>
				<#if vars["licenseNextTime"]??>
					<#assign licenseNextTime = vars["licenseNextTime"]>
				</#if>
				<#if vars["qualificationNextTime"]??>
					<#assign qualificationNextTime = vars["qualificationNextTime"]>
				</#if>
			</#list>
		</#if>
		
		<div class="act-rs-form padding15">
			<#--<div class="rs-label">
				<span class="rs-label-wrapper" style="display: inline-block;">
					<span class="subTask" value="">运营顾问：</span>
					<#if (sdiFlowUser)??>
						${sdiFlowUser}
					</#if>
				</span>
			</div>-->
			<div class="rs-label">
				<label class="subTask" value="1">选择要推广的门店：
            		<#if storeList?? && (storeList?size > 0) >
					 	<select id="${taskId!''}selectStoreId12" class="input-medium">
							<option value="" label="请选择">请选择</option>
							<#list storeList as store>
								<option value="${store.id!''}" label="${store.shortName!''}"
									<#if storeId?? && storeId==store.id> selected="selected" </#if> >${store.shortName!''}</option>
							</#list>
						</select>	
					<#else>
						<font color="red">该商户没有门店</font>
					</#if>
				</label>
			</div>
			
			<div class="rs-label">
				<span class="rs-label-wrapper" style="display: inline-block;">
					<span class="subTask" value="2">商户是否有营业执照：</span>
					<input type="radio" name="${taskId!''}license" value="1" class="${taskId!''}license" 
						<#if sureLicenseButton?? && sureLicenseButton=="1"> checked="checked" </#if>/>有
					<input type="radio" name="${taskId!''}license" value="2" class="${taskId!''}license"
						<#if sureLicenseButton?? && sureLicenseButton=="2"> checked="checked" </#if>/>无
				</span>
			</div>
			<#if sureLicenseButton?? && sureLicenseButton=="2">
				<div class="rs-label" id="${taskId!''}nextLicenseTime">
					<div class="control-group" style="border-bottom: 0px;">
						<label style="float: left; line-height: 30px;">下次沟通时间：</label>
						<div class="controls">
							<input name="${taskId!''}nextLicenseTime" id="next" type="text" readonly="readonly" maxlength="20" 
								class="input-medium Wdate" value="${licenseNextTime!""}" 
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',isShowClear:false,minDate:'%y-%M-%d'});">
						</div>
					</div>
				</div>
			<#else>
				<div class="rs-label hide" id="${taskId!''}nextLicenseTime">
					<div class="control-group" style="border-bottom: 0px;">
						<label style="float: left; line-height: 30px;">下次沟通时间：</label>
						<div class="controls">
							<input name="${taskId!''}nextLicenseTime" id="next" type="text" readonly="readonly" maxlength="20" 
								class="input-medium Wdate" value="${licenseNextTime!""}" 
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',isShowClear:false,minDate:'%y-%M-%d'});">
						</div>
					</div>
				</div>
			</#if>
			
			<#if sureLicenseButton?? && sureLicenseButton=="1">
				<div class="rs-label" id="${taskId!''}qualification">
					<span class="rs-label-wrapper" style="display: inline-block;">
						<span class="subTask" value="3">投放的门店资质是否齐全：</span>
						<input type="radio" name="${taskId!''}qualification" value="1" class="${taskId!''}qualification"
							<#if sureQualificationButton?? && sureQualificationButton=="1"> checked="checked" </#if>/>是
						<input type="radio" name="${taskId!''}qualification" value="2" class="${taskId!''}qualification"
							<#if sureQualificationButton?? && sureQualificationButton=="2"> checked="checked" </#if>/>否
					</span>
				</div>
				<div class="rs-label <#if !(sureQualificationButton??)>hide</#if> <#if sureQualificationButton?? && sureQualificationButton=="2"> hide </#if>" id="${taskId!''}pipe">
					<span class="rs-label-wrapper" style="display: inline-block;">
						<span class="subTask" value="2">推广通道：</span> 
						<input type="checkbox" name="${taskId!''}channels" class="${taskId!''}channels"  value="1"  <#if (vars.checkFriendFlag)?? && (vars.checkFriendFlag=="Y")> checked="checked" </#if>/>微信朋友圈
						<input type="checkbox" name="${taskId!''}channels" class="${taskId!''}channels"  value="2"  <#if (vars.checkMicroblogFlag)?? && (vars.checkMicroblogFlag=="Y")> checked="checked" </#if>/>新浪微博
						<input type="checkbox" name="${taskId!''}channels" class="${taskId!''}channels"  value="3"  <#if (vars.checkMomoFlag??) && (vars.checkMomoFlag=="Y")> checked="checked" </#if>/>陌陌
					</span>
				</div>
				<#if sureQualificationButton?? && sureQualificationButton=="2">
					<div class="rs-label" id="${taskId!''}nextQualificationTime">
						<div class="control-group" style="border-bottom: 0px;">
							<label style="float: left; line-height: 30px;">下次沟通时间：</label>
							<div class="controls">
								<input name="${taskId!''}nextQualificationTime" id="next" type="text" readonly="readonly" maxlength="20" 
									class="input-medium Wdate" value="${qualificationNextTime!""}"
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',isShowClear:false,minDate:'%y-%M-%d'});">
							</div>
						</div>
					</div>
				<#else>
					<div class="rs-label hide" id="${taskId!''}nextQualificationTime">
						<div class="control-group" style="border-bottom: 0px;">
							<label style="float: left; line-height: 30px;">下次沟通时间：</label>
							<div class="controls">
								<input name="${taskId!''}nextQualificationTime" id="next" type="text" readonly="readonly" maxlength="20" 
									class="input-medium Wdate" value="${qualificationNextTime!""}"
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',isShowClear:false,minDate:'%y-%M-%d'});">
							</div>
						</div>
					</div>
				</#if>
			<#else>
				<div class="rs-label hide" id="${taskId!''}qualification">
					<span class="rs-label-wrapper" style="display: inline-block;">
						<span class="subTask" value="3">投放的门店资质是否齐全：</span>
						<input type="radio" name="${taskId!''}qualification" value="1" class="${taskId!''}qualification"
							<#if sureQualificationButton?? && sureQualificationButton=="1"> checked="checked" </#if>/>是
						<input type="radio" name="${taskId!''}qualification" value="2" class="${taskId!''}qualification"
							<#if sureQualificationButton?? && sureQualificationButton=="2"> checked="checked" </#if>/>否
					</span>
				</div>
				<div class="rs-label <#if !(sureQualificationButton??)>hide</#if> <#if sureQualificationButton?? && sureQualificationButton == "2"> hide </#if>" id="${taskId!''}pipe">
					<span class="rs-label-wrapper" style="display: inline-block;">
						<span class="subTask" value="2">推广通道：</span> 
						<input type="checkbox" name="${taskId!''}channels" class="${taskId!''}channels"  value="1"  <#if (vars.checkFriendFlag)?? && (vars.checkFriendFlag=="Y")> checked="checked" </#if>/>微信朋友圈
						<input type="checkbox" name="${taskId!''}channels" class="${taskId!''}channels"  value="2"  <#if (vars.checkMicroblogFlag)?? && (vars.checkMicroblogFlag=="Y")> checked="checked" </#if>/>新浪微博
						<input type="checkbox" name="${taskId!''}channels" class="${taskId!''}channels"  value="3"  <#if (vars.checkMomoFlag??) && (vars.checkMomoFlag=="Y")> checked="checked" </#if>/>陌陌
					</span>
				</div>
				<#if sureQualificationButton?? && sureQualificationButton=="2">
					<div class="rs-label" id="${taskId!''}nextQualificationTime">
						<div class="control-group" style="border-bottom: 0px;">
							<label style="float: left; line-height: 30px;">下次沟通时间：</label>
							<div class="controls">
								<input name="${taskId!''}nextQualificationTime" id="next" type="text" readonly="readonly" maxlength="20" 
									class="input-medium Wdate" value="${qualificationNextTime!""}"
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',isShowClear:false,minDate:'%y-%M-%d'});">
							</div>
						</div>
					</div>
				<#else>
					<div class="rs-label hide" id="${taskId!''}nextQualificationTime">
						<div class="control-group" style="border-bottom: 0px;">
							<label style="float: left; line-height: 30px;">下次沟通时间：</label>
							<div class="controls">
								<input name="${taskId!''}nextQualificationTime" id="next" type="text" readonly="readonly" maxlength="20" 
									class="input-medium Wdate" value="${qualificationNextTime!""}"
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',isShowClear:false,minDate:'%y-%M-%d'});">
							</div>
						</div>
					</div>
				</#if>
			</#if>
			
			<#if sureLicenseButton?? && sureLicenseButton=="1" && sureQualificationButton?? && sureQualificationButton=="1">
				<div class="rs-label" id="${taskId!''}extensionExpect">
					<span class="rs-label-wrapper" style="display: inline-block;">
						<span class="subTask" value="4">与商户规划投放上线的预期时间：</span>
						<input type="radio" name="${taskId!''}extensionExpect" value="1" class="${taskId!''}extensionExpect"
							<#if surePromotionButton?? && surePromotionButton=="1"> checked="checked" </#if>/>是
						<input type="radio" name="${taskId!''}extensionExpect" value="2" class="${taskId!''}extensionExpect"
							<#if surePromotionButton?? && surePromotionButton=="2"> checked="checked" </#if>/>否
					</span>
				</div>
				<#if surePromotionButton?? && surePromotionButton=="1">
					<div class="rs-label" id="${taskId!''}promotionTime">
						<div class="control-group" style="border-bottom: 0px;">
							<label style="float: left; line-height: 30px;">预期投放时间：</label>
							<div class="controls">
								<input name="${taskId!''}promotionTime" id="next" type="text" readonly="readonly" maxlength="20" 
									class="input-medium Wdate" value="${promotionTime!""}" 
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',isShowClear:false,minDate:'%y-%M-%d'});">
							</div>
						</div>
					</div>
					<div class="rs-label hide" id="${taskId!''}nextExtensionExpectTime">
						<div class="control-group" style="border-bottom: 0px;">
							<label style="float: left; line-height: 30px;">下次沟通时间：</label>
							<div class="controls">
								<input name="${taskId!''}nextExtensionExpectTime" id="next" type="text" readonly="readonly" maxlength="20" 
									class="input-medium Wdate" value="${promotionNextTime!""}" 
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',isShowClear:false,minDate:'%y-%M-%d'});">
							</div>
						</div>
					</div>
				<#elseif surePromotionButton?? && surePromotionButton=="2">
					<div class="rs-label hide" id="${taskId!''}promotionTime">
						<div class="control-group" style="border-bottom: 0px;">
							<label style="float: left; line-height: 30px;">预期投放时间：</label>
							<div class="controls">
								<input name="${taskId!''}promotionTime" id="next" type="text" readonly="readonly" maxlength="20" 
									class="input-medium Wdate" value="${promotionTime!""}" 
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',isShowClear:false,minDate:'%y-%M-%d'});">
							</div>
						</div>
					</div>
					<div class="rs-label" id="${taskId!''}nextExtensionExpectTime">
						<div class="control-group" style="border-bottom: 0px;">
							<label style="float: left; line-height: 30px;">下次沟通时间：</label>
							<div class="controls">
								<input name="${taskId!''}nextExtensionExpectTime" id="next" type="text" readonly="readonly" maxlength="20" 
									class="input-medium Wdate" value="${promotionNextTime!""}" 
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',isShowClear:false,minDate:'%y-%M-%d'});">
							</div>
						</div>
					</div>
				<#else>	
					<div class="rs-label hide" id="${taskId!''}promotionTime">
						<div class="control-group" style="border-bottom: 0px;">
							<label style="float: left; line-height: 30px;">预期投放时间：</label>
							<div class="controls">
								<input name="${taskId!''}promotionTime" id="next" type="text" readonly="readonly" maxlength="20" 
									class="input-medium Wdate" value="${promotionTime!""}" 
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',isShowClear:false,minDate:'%y-%M-%d'});">
							</div>
						</div>
					</div>
					<div class="rs-label hide" id="${taskId!''}nextExtensionExpectTime">
						<div class="control-group" style="border-bottom: 0px;">
							<label style="float: left; line-height: 30px;">下次沟通时间：</label>
							<div class="controls">
								<input name="${taskId!''}nextExtensionExpectTime" id="next" type="text" readonly="readonly" maxlength="20" 
									class="input-medium Wdate" value="${promotionNextTime!""}" 
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',isShowClear:false,minDate:'%y-%M-%d'});">
							</div>
						</div>
					</div>
				</#if>
			<#else>
				<div class="rs-label hide" id="${taskId!''}extensionExpect">
					<span class="rs-label-wrapper" style="display: inline-block;">
						<span class="subTask" value="4">与商户规划投放上线的预期时间：</span>
						<input type="radio" name="${taskId!''}extensionExpect" value="1" class="${taskId!''}extensionExpect"
							<#if surePromotionButton?? && surePromotionButton=="1"> checked="checked" </#if>/>是
						<input type="radio" name="${taskId!''}extensionExpect" value="2" class="${taskId!''}extensionExpect"
							<#if surePromotionButton?? && surePromotionButton=="2"> checked="checked" </#if>/>否
					</span>
				</div>
				<#if surePromotionButton?? && surePromotionButton=="1">
					<div class="rs-label" id="${taskId!''}promotionTime">
						<div class="control-group" style="border-bottom: 0px;">
							<label style="float: left; line-height: 30px;">预期投放时间：</label>
							<div class="controls">
								<input name="${taskId!''}promotionTime" id="next" type="text" readonly="readonly" maxlength="20" 
									class="input-medium Wdate" value="${promotionTime!""}" 
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',isShowClear:false,minDate:'%y-%M-%d'});">
							</div>
						</div>
					</div>
				<#elseif surePromotionButton?? && surePromotionButton=="2">
					<div class="rs-label" id="${taskId!''}nextExtensionExpectTime">
						<div class="control-group" style="border-bottom: 0px;">
							<label style="float: left; line-height: 30px;">下次沟通时间：</label>
							<div class="controls">
								<input name="${taskId!''}nextExtensionExpectTime" id="next" type="text" readonly="readonly" maxlength="20" 
									class="input-medium Wdate" value="${promotionNextTime!""}" 
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',isShowClear:false,minDate:'%y-%M-%d'});">
							</div>
						</div>
					</div>
				<#else>	
					<div class="rs-label hide" id="${taskId!''}promotionTime">
						<div class="control-group" style="border-bottom: 0px;">
							<label style="float: left; line-height: 30px;">预期投放时间：</label>
							<div class="controls">
								<input name="${taskId!''}promotionTime" id="next" type="text" readonly="readonly" maxlength="20" 
									class="input-medium Wdate" value="${promotionTime!""}" 
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',isShowClear:false,minDate:'%y-%M-%d'});">
							</div>
						</div>
					</div>
					<div class="rs-label hide" id="${taskId!''}nextExtensionExpectTime">
						<div class="control-group" style="border-bottom: 0px;">
							<label style="float: left; line-height: 30px;">下次沟通时间：</label>
							<div class="controls">
								<input name="${taskId!''}nextExtensionExpectTime" id="next" type="text" readonly="readonly" maxlength="20" 
									class="input-medium Wdate" value="${promotionNextTime!""}" 
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',isShowClear:false,minDate:'%y-%M-%d'});">
							</div>
						</div>
					</div>
				</#if>
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
			<button style="button" class="btn btn-large"  onclick="submit_${taskId!''}submitForm12(1)">确定完成</button>
			<span class="flowTaskState hide">已完成</span>
		</div>
	</div>
</div>