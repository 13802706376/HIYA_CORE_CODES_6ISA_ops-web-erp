<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>完成的订单</title>
	<meta name="decorator" content="default" />
	<link href="${ctxStatic}/common/act.css?v=${staticVersion}" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="${ctxStatic}/common/act.js?v=${staticVersion}"></script>
</head>
<body>
	<div class="act-top positionrelative">
		<div class="act-top-left positionrabsolute">
			<a href="javascript:;" class="comeback" onclick="history.back();">返回</a>
		</div>
		<ul class="nav nav-tabs">
			<li class="marginleft60 acttitle active">
				<a href="${ctx}/act/task/process/">完成的订单</a>
			</li>
		</ul>
	</div>
	<div id="selectModal" class="select-modal">
		<div class="select-wrap clear">
			<div class="select-catgry">
				<dl class="sca">
					<dt>已完成订单编号</dt>
					<dd>
						<select id="selectOrderId" class="select-orderid">
						<option value="">所有订单</option>		
						<c:forEach items="${requestScope.list }" var="t">
						<option value="${t.orderNumber}-${t.splitId}">${t.orderNumber}-${t.splitId}</option>
						</c:forEach>
						</select>			
					</dd>
				</dl>
			</div>
			<div class="select-catgry">
				<dl class="sca">
					<dt>订单加急</dt>
					<dd>
						<label><input type="checkbox" value="1" name="hurryFlag">&nbsp;加急</label>
					</dd>
				</dl>
			</div>
			<div class="select-catgry">
				<dl class="sca">
					<dt>商户</dt>
					<dd>
						<select id="selectshop" class="select-orderid">
						<option value="">所有商户</option>
							<c:forEach items="${requestScope.shoplist }" var="s">
							<option value="${s.zhangbeiId}">${s.name}</option>
							</c:forEach>
						</select>
					</dd>
				</dl>
			</div>
		</div>
		<div class="select-btn-wrap">
			<button type="button" class="btn btn-primary msrue" onclick="cx()">查找</button>
			<button type="reset" class="btn  btn-warning">重置</button>&nbsp;
			<button type="button" class="btn nosrue">取消</button>
		</div>
	</div>
	<div class="act-wrap act-completed-wrap">
		<div class="act-complate-title act-top-title">
			<span class="actt-top-ce inline-block">订单</span>
			<span class="actt-top-ce inline-block">商户</span>
			<span class="actt-top-ce inline-block">服务类型</span>
			<span class="actt-top-ce inline-block">时间</span>
		</div>
		<div class="act-complete-items">
		
		<c:forEach items="${requestScope.order }" var="o">
			<div class="act-complete-item border-gray">
				<div class="complete-item-left floatleft actcp-left-wrap">
					<div class="floatleft actcp-left-wrap">
						<div class="clist-content">
							<p>订单：${o.orderNumber}-${o.splitId}</p>
							<p>分单数：${o.num }</p>
							<p>购买商品：${o.goodName }</p>
							<p>购买时间：${o.buyTime }</p>
						</div>
					</div>
					<div class="act-cp-info act-cp-right">
						<div class="clist-content">
							<p>商户名称:${o.shopName }</p>
							<p>(${fns:getDictLabel(o.orderType, 'orderType', '未知')})&nbsp;联系人:${o.cname}</p>
							<p>联系方式:${o.phone }</p>
						</div>
					</div>
				</div>
				
				<div class="complete-item-right act-cp-right">
					<div class="floatleft actcp-left-wrap">
						<div class="clist-content">
							<p>${o.serverType }</p>
							<p><a href="${ctx }/workflow/toTaskHistoicFlow?procInstId=${o.procInsId}&from=processlist">查看任务记录</a></p>
							<!-- <p><a href="${ctx }/workflow/toTaskHistoicFlowByProcInsId?procInsId=${o.procInsId}">查看任务记录</a></p> -->
						</div>
					</div>
					<div class="act-cp-info act-cp-right">
						<div class="clist-content">
							<p>${o.promotionTime }</p>
						</div>
					</div>
				</div>
			</div>
			</c:forEach>
		</div>
	</div>
	<script type="text/javascript">
		$(function() {
			$("#workFlowQuery").validate({
				submitHandler: function(form){
					var taskState=""; 
					var goodsTypeList ="";
					$('input[name="taskState"]:checked').each(function(){ 
						taskState+=$(this).val()+","; 
					}); 
					$('input[name="goodsTypoList"]:checked').each(function(){ 
						goodsTypeList+=$(this).val()+","; 
					}); 
					
					$('#goodsTypeList').val(goodsTypeList);
					$('#taskStateList').val(taskState);
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
			$('body').on('click', '.dorpdownbotms', function(event) {
				event.preventDefault();
			    $(this).toggleClass('up');
			    $(this).parents('.act-content-box').toggleClass('istoggle');
			    $(this).siblings('.act-rs-item').eq(0).show();//.toggleClass('isshow');
			})
		   	.on('mouseenter', '.act-content-flowfrom', function(event) {
		    	$(this).find('.act-tips-porint').hide();
		   	})
		   	.on('mouseleave', '.act-content-flowfrom', function(event) {
		    	$(this).find('.act-tips-porint').show();
		   	});

		})
		
		function cx(){
			var oidAndSid=$("#selectOrderId").val();
			var shopId=$("#selectshop").val();
			var hurryflag=""; 
			$("input[name='hurryFlag']:checked").each(function(){ 
				hurryflag=$(this).val(); 
			//alert($(this).val()); 
			}) 
			location="${ctx}/workflow/complete?oidAndSid="+oidAndSid+"&shopId="+shopId+"&hurryflag="+hurryflag;
		}
	</script>
</body>