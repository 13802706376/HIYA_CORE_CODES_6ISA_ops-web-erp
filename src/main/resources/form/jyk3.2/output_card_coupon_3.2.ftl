<input type='hidden' id='${taskId!''}splitId1014' name='${taskId!''}splitId1014' value="${splitId!''}"/>
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
<div class="task-detail-wrapper">
	<h3 class="task-title">输出卡券</h3>
	<div class="task-sider">
		<!-- <h4 class="task-sider-title">查看推广提案</h4> -->
		<div class="task-sider-subtitle"><span>任务说明</span></div>
		<div class="task-sider-content">
			<p>请确保投放卡券已通过微信包审核，查看路径：老客营销 - 优惠卡券管理 - 卡券列表直接查看，或点击“查看卡包审核”。《查看卡包审核说明截图》</p>
		</div>
	</div>
	<div class="task-sider">
		<div class="task-sider-subtitle"><span>相关资料</span></div>
		<div class="task-sider-content">
			<dl class="content-wrap">
				<dt>经营诊断与策划:</dt>
				<dd><a href="javascript:;">查看</a></dd>
			</dl>
		</div>
	</div>
	<div class="task-sider">
		<div class="task-sider-subtitle"><span>任务操作</span></div>
		<div class="task-sider-content">
			<dl class="content-wrap">
				<dt>输入卡券信息：</dt>
				<dd>
					<a href="javascript:;" class="btn addCoupon">新增卡券</a>
				</dd>
			</dl>
			<div class="list-wrap" id="contentListWrapper">
				<ul class="list-title">
					<li>卡券名称</li>
					<li>卡券链接类别</li>
					<li>卡券链接</li>
				</ul>
				<#if (couponOutputList)?? && (couponOutputList?size > 0) >    
				<#list couponOutputList as fileinfo>   
				<ul class="list-label">
					<li>
						<input type="text" name="${taskId!''}couponName1014"  value="${fileinfo.couponName}" class="list-input">
						<input type="hidden" name="${taskId!''}couponId"  value="${fileinfo.id}">
					</li>
					<li>
						<select  class="list-select" name="${taskId!''}seleCouponType1014">
							<option value="">请选择</option>
							<option <#if (fileinfo.couponLinkCategory)?? &&(fileinfo.couponLinkCategory)=='weixin_link' >selected </#if>  value="weixin_link">微信链接</option>
							<option <#if (fileinfo.couponLinkCategory)?? &&(fileinfo.couponLinkCategory)=='cellphone_number_link' >selected </#if> value="cellphone_number_link">手机号链接</option>
						</select>
					</li>
					<li>
						<input type="text" name="${taskId!''}couponLinkCategory1014"  value="${fileinfo.couponLink}" class="list-input">
					</li>
					<li><a href="javascript:;" class="delete-btn deletecoupon" title="删除" data-id="${fileinfo.id}"><i class="icon-trash"></i></a></li>
				</ul>
			 </#list>      
     		 </#if>
			</div>
		</div>
	</div>
	<div class="task-sider">
		<div class="task-sider-content">
			<dl class="content-wrap">
				<dt>是否完成卡券信息输入:</dt>
				<dd>
					<label class="content-label"><input <#if !(flowdata.isCardInfo)?? || flowdata.isCardInfo !='Y'> checked="checked" </#if> type="radio" value="N" name="isFinish">否</label>
					<label class="content-label"><input <#if (flowdata.isCardInfo)?? && flowdata.isCardInfo =='Y'> checked="checked" </#if> type="radio" value="Y" name="isFinish">是</label>
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
			<button type="button" class="btn btn-large"  onclick="submit_${taskId!''}(1)">确定完成</button>
		</div>
	</div>
</div>
</#if>
<script type="text/javascript">
	var task_flow_version = '3.2';
	function submit_${taskId!''}(isLocation){
		var channels =[]; 
		var channelName =[];
	
		var textDesignPersonVars="";
		var designerVars="";
		
		var attr = [];
		
		var isFinished=false;

		$(".list-label").each(function(obj){
			var couponName = $(this).find("input[name=${taskId!''}couponName1014]").val(),
				couponLinkCategory = $(this).find("select[name=${taskId!''}seleCouponType1014]").val(),
				couponLink = $(this).find("input[name=${taskId!''}couponLinkCategory1014]").val(),
				id = $(this).find("input[name=${taskId!''}couponId]").val();
			attr.push({
				couponName: couponName,
				couponLinkCategory: couponLinkCategory,
				couponLink: couponLink,
				id: id
			});
		});
		
		if($("input[name=isFinish]:checked").val() === "Y" && attr.length) {
			isFinished = true;
		}
		
		var submit = function (v, h, f) {
			if (v === 'ok') {
				$.jBox.tip("正在修改，请稍等...", 'loading', {
				timeout : 3000,
				persistent : true
			});
			$.post("../jyk/flow/3p2/output_card_coupon_3.2", {
					isFinished: isFinished,
					taskId: "${taskId!''}",//$("#taskId1017").val(),
					procInsId: "${procInsId!''}",
					splitId:$("#${taskId!''}splitId1014").val(),
					jsonStr: JSON.stringify(attr),
					isCardInfo: $("input[name=isFinish]:checked").val()
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
		top.$.jBox.confirm("确定完成吗？", "提示", submit);
	}
	
	$(function() {
		var outputCardCoupon = function() {

			addCoupon = function(obj) {
				var str = '<ul class="list-label">'+
					'<li><input name="${taskId!''}couponName1014" type="text" class="list-input"></li>'+
					'<li>'+
						'<select  name="${taskId!''}seleCouponType1014" class="list-select">'+
							'<option value="">请选择</option>'+
							'<option  value="weixin_link">微信链接</option>'+
							'<option value="cellphone_number_link">手机号链接</option>'+
						'</select>'+
					'</li>'+
					'<li><input name="${taskId!''}couponLinkCategory1014"  type="text" class="list-input"></li>'+
					'<li><a href="javascript:;" class="delete-btn deletecoupon" title="删除"><i class="icon-trash"></i></a></li>'+
				'</ul>';
				obj.append(str);
			},
			bindEvent = function() {
				
				$('body').on('click', '#contentListWrapper .list-label .deletecoupon', function(event) {
					event.preventDefault();
					var id = $(this).attr('data-id');

					var obj = $(this), parent = $(this).parents('.list-label');
					var deletecouponSubmit = function(v, h, f) {
						if (v === 'ok') {
							$.jBox.tip("正在修改，请稍等...", 'loading', {
								timeout : 3000,
								persistent : true
							});
						}
						if (!id || id === 'null' || id === 'undefined') {
							parent.remove();
							return true;
						}
						$.post("../order/erpOrderCouponOutput/cardDelete", {id: id}, function(res) {
							if(res.result) {
								parent.remove();
							}
						})
						return true; 
					};
					top.$.jBox.confirm("确定删除该条数据吗？", "提示", deletecouponSubmit);
					
				}).on('click', '.addCoupon', function(event) {
					event.preventDefault();
					var obj = $('#contentListWrapper');
					addCoupon(obj);
				});
			},
			init = function() {
				bindEvent();
			};

			return {
				init: init
			};
		}();
		outputCardCoupon.init();
	})
</script>
