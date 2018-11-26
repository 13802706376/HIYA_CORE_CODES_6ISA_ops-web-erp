<script type="text/javascript">

function submit_${taskId!''}submitForm1014(isLocation){
	
	
	var obj = {};
	
	
	obj.promotedate = $("#${taskId!''}promotedate1014").val();

	obj.hardwarecomplete = $("#${taskId!''}hardwarecomplete1014").prop("checked");
	
	obj.launchinfoconfirm = $("#${taskId!''}launchinfoconfirm1014").prop("checked");
	
	
	obj.momolinkconfirm = $("#${taskId!''}momolinkconfirm1014").prop("checked");
	obj.momoimgtext = $("#${taskId!''}momoimgtext1014").prop("checked");
	obj.momoimglink = $("#${taskId!''}momoimglink1014").prop("checked");
	obj.momostoreiphone = $("#${taskId!''}momostoreiphone1014").prop("checked");
	obj.momopreferentialrules = $("#${taskId!''}momopreferentialrules1014").prop("checked");
	obj.momooffical = $("#${taskId!''}momooffical1014").prop("checked");
	obj.momoshopname = $("#${taskId!''}momoshopname1014").prop("checked");
	obj.momoidname = $("#${taskId!''}momoidname1014").prop("checked");
	
	obj.weibolinkconfirm = $("#${taskId!''}weibolinkconfirm1014").prop("checked");
	obj.weiboimgtext = $("#${taskId!''}weiboimgtext1014").prop("checked");
	obj.weiboimglink = $("#${taskId!''}weiboimglink1014").prop("checked");
	obj.weibostoreiphone = $("#${taskId!''}weibostoreiphone1014").prop("checked");
	obj.weibopreferentialrules = $("#${taskId!''}weibopreferentialrules1014").prop("checked");
	obj.weibooffical = $("#${taskId!''}weibooffical1014").prop("checked");
	obj.weiboshopname = $("#${taskId!''}weiboshopname1014").prop("checked");
	obj.weiboidname = $("#${taskId!''}weiboidname1014").prop("checked");
	
	
	obj.weixinweixinothorskipcontent = $("#${taskId!''}weixinweixinothorskipcontent1014").prop("checked");
	obj.weixinweixinkclvipcard = $("#${taskId!''}weixinweixinkclvipcard1014").prop("checked");
	obj.weixinweixinnavigationaddr = $("#${taskId!''}weixinweixinnavigationaddr1014").prop("checked");
	obj.weixincoupondetail = $("#${taskId!''}weixincoupondetail1014").prop("checked");
	obj.weixincouponname = $("#${taskId!''}weixincouponname1014").prop("checked");
	obj.weixinofficialcontent = $("#${taskId!''}weixinofficialcontent1014").prop("checked");
	obj.weixindiscountsinfo = $("#${taskId!''}weixindiscountsinfo1014").prop("checked");
	obj.weixinshopname = $("#${taskId!''}weixinshopname1014").prop("checked");
	obj.weixinstoreconfirm = $("#${taskId!''}weixinstoreconfirm1014").prop("checked");
	obj.weixinimgmatch = $("#${taskId!''}weixinimgmatch1014").prop("checked");
	obj.weixinimgofficial = $("#${taskId!''}weixinimgofficial1014").prop("checked");
	obj.weixininnerofficial = $("#${taskId!''}weixininnerofficial1014").prop("checked");
	obj.weixinpreferentialrules = $("#${taskId!''}weixinpreferentialrules1014").prop("checked");
	obj.weixinvarietyconfirm = $("#${taskId!''}weixinvarietyconfirm1014").prop("checked");
	obj.weixinbottomstorelink = $("#${taskId!''}weixinbottomstorelink1014").prop("checked");
	obj.weixinbottomstore = $("#${taskId!''}weixinbottomstore1014").prop("checked");
	obj.weixinoutimgofficial = $("#${taskId!''}weixinoutimgofficial1014").prop("checked");
	
	

	var submit = function (v, h, f) {
			if (v == 'ok') {
					$.jBox.tip("正在修改，请稍等...", 'loading', {
					timeout : 3000,
					persistent : true
			});
			$.post("../jyk/flow/3p2/finally_confirm_launch_time_3.2", {
					taskId:$("#${taskId!''}taskId1014").val(),
					procInsId:$("#${taskId!''}procInsId1014").val(),
					splitId:$("#${taskId!''}splitId1014").val(),
					confirminfo: JSON.stringify(obj)
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
	<h3 class="task-title">最终确认推广计划</h3>
	<div class="task-sider">
		<div class="task-sider-title"><span>确认广告内容</span></div>
		<div class="task-sider-subtitle"><span>任务操作</span></div>
		<div class="task-sider-content">
			<div class="content-wrap">
				<div class="task-sider-title border-bottom"><span>微信推广通道</span></div>
				<div class="content-wrap">
					<div class="border-bottom">外层</div>
					<div class="checkbox-list">
						<label class="content-label"><input id="${taskId!''}weixinoutimgofficial1014" <#if (flowdata.weixinoutimgofficial)?? && flowdata.weixinoutimgofficial =='true'> checked="checked" </#if> type="checkbox">图片及图内文案</label>
					</div>
					<div class="checkbox-list">
						<label class="content-label"><input id="${taskId!''}weixinbottomstore1014" <#if (flowdata.weixinbottomstore)?? && flowdata.weixinbottomstore =='true'> checked="checked" </#if> type="checkbox">底部门店名称</label>
					</div>
					<div class="checkbox-list">
						<label class="content-label"><input id="${taskId!''}weixinbottomstorelink1014"  <#if (flowdata.weixinbottomstorelink)?? && flowdata.weixinbottomstorelink =='true'> checked="checked" </#if> type="checkbox">底部跳转门店地址、跳转页文案</label>
					</div>
				</div>
				<div class="content-wrap">
					<div class="border-bottom">内层</div>
					<div class="checkbox-list">
						<label class="content-label">
							<input type="checkbox">商户名称
							<span class="gray-word">（常见问题：因谐音等原因出现错误，但影响巨大，操作方法：逐字核对）</span>
						</label>
					</div>
					<div class="checkbox-list">
						<label class="content-label"><input id="${taskId!''}weixinvarietyconfirm1014"   <#if (flowdata.weixinvarietyconfirm)?? && flowdata.weixinvarietyconfirm =='true'> checked="checked" </#if> type="checkbox">菜品/产品的名称</label>
					</div>
					<div class="checkbox-list">
						<label class="content-label"><input id="${taskId!''}weixinpreferentialrules1014"  <#if (flowdata.weixinpreferentialrules)?? && flowdata.weixinpreferentialrules =='true'> checked="checked" </#if> type="checkbox">活动优惠及使用规则</label>
					</div>
					<div class="checkbox-list">
						<label class="content-label"><input id="${taskId!''}weixininnerofficial1014"  <#if (flowdata.weixininnerofficial)?? && flowdata.weixininnerofficial =='true'> checked="checked" </#if> type="checkbox">内层文案</label>
					</div>
					<div class="checkbox-list">
						<label class="content-label"><input id="${taskId!''}weixinimgofficial1014" <#if (flowdata.weixinimgofficial)?? && flowdata.weixinimgofficial =='true'> checked="checked" </#if> type="checkbox">图片及图内文案</label>
					</div>
					<div class="checkbox-list">
						<label class="content-label"><input id="${taskId!''}weixinimgmatch1014"	 <#if (flowdata.weixinimgmatch)?? && flowdata.weixinimgmatch =='true'> checked="checked" </#if> type="checkbox">图文的搭配
							<span class="gray-word">（常见问题：常见A图片配套为B文案，非A文案）</span>
						</label>
					</div>
					<div class="checkbox-list">
						<label class="content-label">
							<input id="${taskId!''}weixinstoreconfirm1014" <#if (flowdata.weixinstoreconfirm)?? && flowdata.weixinstoreconfirm =='true'> checked="checked" </#if> type="checkbox">门店电话及地址
							<span class="gray-word">（常见问题：容易出现复制错误；操作方法：首先确认位数,如手机号码是11位，其次于方案确认表中进行进一步确认）</span>
						</label>
					</div>
				</div>
				<div class="content-wrap">
					<div class="border-bottom">推广分享链接</div>
					<div class="checkbox-list">
						<label class="content-label"><input id="${taskId!''}weixinshopname1014" <#if (flowdata.weixinshopname)?? && flowdata.weixinshopname =='true'> checked="checked" </#if> type="checkbox">商户名称</label>
					</div>
					<div class="checkbox-list">
						<label class="content-label">
							<input id="${taskId!''}weixindiscountsinfo1014" <#if (flowdata.weixindiscountsinfo)?? && flowdata.weixindiscountsinfo =='true'> checked="checked" </#if> type="checkbox">优惠信息
							<span class="gray-word">（常见问题：因谐音等原因出现错误，但影响巨大；操作方法：逐字核对）</span>
						</label>
					</div>
					<div class="checkbox-list">
						<label class="content-label"><input id="${taskId!''}weixinofficialcontent1014" <#if (flowdata.weixinofficialcontent)?? && flowdata.weixinofficialcontent =='true'> checked="checked" </#if> type="checkbox">文案内容</label>
					</div>
				</div>
				<div class="content-wrap">
					<div class="border-bottom">按钮跳转后内容</div>
					<div class="checkbox-list">
						<label class="content-label"><input id="${taskId!''}weixincouponname1014" <#if (flowdata.weixincouponname)?? && flowdata.weixincouponname =='true'> checked="checked" </#if> type="checkbox">卡券名称</label>
					</div>
					<div class="checkbox-list">
						<label class="content-label">
							<input id="${taskId!''}weixincoupondetail1014"  <#if (flowdata.weixincoupondetail)?? && flowdata.weixincoupondetail =='true'> checked="checked" </#if> type="checkbox">卡券详情文案
							<span class="gray-word">（内容重要，但因入口太深，且经常以复制粘贴形式编辑，不易发现错误。容易被商户侧察觉出编辑有误处；操作方法：对着卡券信息表，进行比较核对）</span>
						</label>
					</div>
					<div class="checkbox-list">
						<label class="content-label"><input id="${taskId!''}weixinweixinnavigationaddr1014" <#if (flowdata.weixinweixinnavigationaddr)?? && flowdata.weixinweixinnavigationaddr =='true'> checked="checked" </#if> type="checkbox">一键导航地址</label>
					</div>
					<div class="checkbox-list">
						<label class="content-label"><input id="${taskId!''}weixinweixinkclvipcard1014"  <#if (flowdata.weixinweixinkclvipcard)?? && flowdata.weixinweixinkclvipcard =='true'> checked="checked" </#if> type="checkbox">客常来会员卡</label>
					</div>
					<div class="checkbox-list">
						<label class="content-label"><input  id="${taskId!''}weixinweixinothorskipcontent1014" <#if (flowdata.weixinweixinothorskipcontent)?? && flowdata.weixinweixinothorskipcontent =='true'> checked="checked" </#if>  type="checkbox">其余跳转页面内容</label>
					</div>
				</div>
			</div>
		</div>
		<div class="task-sider-content">
			<div class="content-wrap">
				<div class="task-sider-title border-bottom"><span>微博推广通道</span></div>
				<div class="content-wrap">
					<div class="border-bottom">文案</div>
					<div class="checkbox-list">
						<label class="content-label"><input id="${taskId!''}weiboidname1014" <#if (flowdata.weiboidname)?? && flowdata.weiboidname =='true'> checked="checked" </#if> type="checkbox">头像及ID名称</label>
					</div>
					<div class="checkbox-list">
						<label class="content-label"><input id="${taskId!''}weiboshopname1014" <#if (flowdata.weiboshopname)?? && flowdata.weiboshopname =='true'> checked="checked" </#if> type="checkbox">商户名称<span class="gray-word">（常见问题：因谐音等原因出现错误，但影响巨大，操作方法：逐字核对）</span></label>
					</div>
					<div class="checkbox-list">
						<label class="content-label"><input id="${taskId!''}weibooffical1014"	<#if (flowdata.weibooffical)?? && flowdata.weibooffical =='true'> checked="checked" </#if> type="checkbox">微博文案</label>
					</div>
					<div class="checkbox-list">
						<label class="content-label"><input id="${taskId!''}weibopreferentialrules1014" <#if (flowdata.weibopreferentialrules)?? && flowdata.weibopreferentialrules =='true'> checked="checked" </#if> type="checkbox">活动优惠及使用规则</label>
					</div>
					<div class="checkbox-list">
						<label class="content-label"><input id="${taskId!''}weibostoreiphone1014" <#if (flowdata.weibostoreiphone)?? && flowdata.weibostoreiphone =='true'> checked="checked" </#if> type="checkbox">门店电话及地址</label>
					</div>
				</div>
				<div class="content-wrap">
					<div class="border-bottom">图片</div>
					<div class="checkbox-list">
						<label class="content-label">
							<input id="${taskId!''}weiboimglink1014" <#if (flowdata.weiboimglink)?? && flowdata.weiboimglink =='true'> checked="checked" </#if> type="checkbox">图片跳转链接及对应文案
						</label>
					</div>
					<div class="checkbox-list">
						<label class="content-label"><input id="${taskId!''}weiboimgtext1014" <#if (flowdata.weiboimgtext)?? && flowdata.weiboimgtext =='true'> checked="checked" </#if> type="checkbox">图中文字</label>
					</div>
					<div class="checkbox-list">
						<label class="content-label"><input id="${taskId!''}weibolinkconfirm1014"	<#if (flowdata.weibolinkconfirm)?? && flowdata.weibolinkconfirm =='true'> checked="checked" </#if> type="checkbox">链接内容正确</label>
					</div>
				</div>
			</div>
		</div>
		<div class="task-sider-content">
			<div class="content-wrap">
				<div class="task-sider-title border-bottom"><span>陌陌推广通道</span></div>
				<div class="content-wrap">
					<div class="border-bottom">文案</div>
					<div class="checkbox-list">
						<label class="content-label"><input id="${taskId!''}momoidname1014" <#if (flowdata.momoidname)?? && flowdata.momoidname =='true'> checked="checked" </#if> type="checkbox">头像及ID名称</label>
					</div>
					<div class="checkbox-list">
						<label class="content-label"><input id="${taskId!''}momoshopname1014" <#if (flowdata.momoshopname)?? && flowdata.momoshopname =='true'> checked="checked" </#if> type="checkbox">商户名称<span class="gray-word">（常见问题：因谐音等原因出现错误，但影响巨大，操作方法：逐字核对）</span></label>
					</div>
					<div class="checkbox-list">
						<label class="content-label"><input id="${taskId!''}momooffical1014"	<#if (flowdata.momooffical)?? && flowdata.momooffical =='true'> checked="checked" </#if> type="checkbox">陌陌文案</label>
					</div>
					<div class="checkbox-list">
						<label class="content-label"><input	id="${taskId!''}momopreferentialrules1014" <#if (flowdata.momopreferentialrules)?? && flowdata.momopreferentialrules =='true'> checked="checked" </#if> type="checkbox">活动优惠及使用规则</label>
					</div>
					<div class="checkbox-list">
						<label class="content-label"><input id="${taskId!''}momostoreiphone1014"	<#if (flowdata.momostoreiphone)?? && flowdata.momostoreiphone =='true'> checked="checked" </#if> type="checkbox">门店电话及地址</label>
					</div>
				</div>
				<div class="content-wrap">
					<div class="border-bottom">图片</div>
					<div class="checkbox-list">
						<label class="content-label">
							<input id="${taskId!''}momoimglink1014" <#if (flowdata.momoimglink)?? && flowdata.momoimglink =='true'> checked="checked" </#if> type="checkbox">图片跳转链接及对应文案
						</label>
					</div>
					<div class="checkbox-list">
						<label class="content-label"><input id="${taskId!''}momoimgtext1014"	<#if (flowdata.momoimgtext)?? && flowdata.momoimgtext =='true'> checked="checked" </#if> type="checkbox">图中文字</label>
					</div>
					<div class="checkbox-list">
						<label class="content-label"><input id="${taskId!''}momolinkconfirm1014"	<#if (flowdata.momolinkconfirm)?? && flowdata.momolinkconfirm =='true'> checked="checked" </#if> type="checkbox">链接内容正确</label>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="task-sider">
		<div class="task-sider-title"><span>确认投放信息</span></div>
		<div class="task-sider-subtitle"><span>任务操作</span></div>
		<div class="task-sider-content">
			<div class="content-wrap">
				<div class="checkbox-list">
					<label class="content-label"><input id="${taskId!''}launchinfoconfirm1014" <#if (flowdata.launchinfoconfirm)?? && flowdata.launchinfoconfirm =='true'> checked="checked" </#if> type="checkbox">确认投放信息</label>
				</div>
				<div class="textarea-wrap">
					<textarea  id="${taskId!''}launchinfo1014"  class="content-textarea"><#if (flowdata.launchinfo)?? >${flowdata.launchinfo}</#if></textarea>
				</div>
				<p>*若对商户的投放信息有异议，请与投放顾问线下沟通</p>
			</div>
		</div>
	</div>
	<div class="task-sider">
		<div class="task-sider-title"><span>确认运营顾问已上门服务</span></div>
		<div class="task-sider-subtitle"><span>任务操作</span></div>
		<div class="task-sider-content">
			<div class="content-wrap">
				<div class="checkbox-list">
					<label class="content-label"><input  id="${taskId!''}hardwarecomplete1014" <#if (flowdata.hardwarecomplete)?? && flowdata.hardwarecomplete =='true'> checked="checked" </#if> type="checkbox">确认掌贝智能设备进店并完成店内人员的培训</label>
				</div>
			</div>
		</div>
	</div>
	<div class="task-sider">
		<div class="task-sider-title"><span>确认最终商户推广上线时间</span></div>
		<div class="task-sider-subtitle"><span>任务操作</span></div>
		<div class="task-sider-content">
			<dl class="content-wrap">
				<dt>最终推广上线时间：</dt>
				<dd class="margin-left-120">
					<input type="text"	id="${taskId!''}promotedate1014" <#if (flowdata.promotedate)??>value="${flowdata.promotedate}"</#if>  onclick="WdatePicker({dateFmt:'yyyy-MM-dd', isShowClear:false, readOnly:false});" class="erpstore-input Wdate">
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
			<button type="button" class="btn btn-large" onclick="submit_${taskId!''}submitForm1014(1)">确定完成</button>
		</div>
	</div>
</div>
<script type="text/javascript">
	var task_flow_version = '3.2';
	$(function() {
		var finallyConfirmLaunchTime = function() {
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

		finallyConfirmLaunchTime.init();
	})
</script>