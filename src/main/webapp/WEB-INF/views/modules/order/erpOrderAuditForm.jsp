<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>订单审核</title>
	<meta name="decorator" content="default"/>
</head>
<body>
	<div class="act-top positionrelative" style="overflow: inherit;">
		<ul class="nav nav-tabs">
			<li class="active"><a>订单审核</a></li>
		</ul>
	</div>
	<div style="padding:10px;">
		<form:form id="searchForm" modelAttribute="erpOrderOriginalInfo" action="${ctx}/order/erpOrderOriginalInfo/orderAuditForm" method="post" class="breadcrumb form-search">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<ul class="ul-form">
				<li>
					<label>订单号：</label>
					<form:input path="orderNumber" htmlEscape="false" maxlength="50" class="input-medium"/>
				</li>
				<li><label>购买时间：</label><!-- ,maxDate:'#F{$dp.$D(\'findBuyEndDate\');}' -->
					<c:set var="bbd"><fmt:formatDate value="${erpOrderOriginalInfo.findBuyStartDate}" pattern="yyyy-MM-dd HH:mm"/></c:set>
					<c:set var="ebd"><fmt:formatDate value="${erpOrderOriginalInfo.findBuyEndDate}" pattern="yyyy-MM-dd HH:mm"/></c:set>
					<form:input path="findBuyStartDate" id="findBuyStartDate" type="text" maxlength="20" class="input-medium Wdate" value="${bbd}"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',isShowClear:false,readOnly:true});"/> - 
					<form:input path="findBuyEndDate" id="findBuyEndDate" type="text" maxlength="20" class="input-medium Wdate" value="${ebd}" 
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',isShowClear:false,readOnly:true});"/>
				</li>
				<li>
					<label>商户：</label>
					<form:input path="shopName" htmlEscape="false" maxlength="50" class="input-medium"/>
				</li>
				<li>
					<label>服务商：</label>
					<form:input path="agentName" htmlEscape="false" maxlength="50" class="input-medium"/>
				</li>
				<li>
					<label>状态：</label>
					<select id="order-status" name="auditStatus" value="${erpOrderOriginalInfo.auditStatus}" class="input-medium" style="width: 160px;">
						<option value=""<c:if test="!${erpOrderOriginalInfo.auditStatus}"> selected</c:if>>全部</option>
						<option value="0"<c:if test="${erpOrderOriginalInfo.auditStatus == '0'}"> selected</c:if>>待首次审核 </option>
						<option value="1"<c:if test="${erpOrderOriginalInfo.auditStatus == '1'}"> selected</c:if>>待二次审核 </option>
						<option value="3"<c:if test="${erpOrderOriginalInfo.auditStatus == '3'}"> selected</c:if>>审核不通过</option>
					</select>
				</li>
				<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
				<li class="btns"><input class="btn btn-warning" type="reset" value="重置"/></li>
			</ul>
		</form:form>
		<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th>订单号</th>
					<th>商户</th>
					<th>服务商</th>
					<th>订单创建人</th>
					<th>购买时间</th>
					<th>状态</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${page.list}" var="erpOrderOriginalInfo">
				<tr>
					<td>${erpOrderOriginalInfo.orderNumber}</td>
					<td>${erpOrderOriginalInfo.shopName}</td>
					<td>${erpOrderOriginalInfo.agentName}</td>
					<td>${erpOrderOriginalInfo.createByName}</td>
					<td><fmt:formatDate value="${erpOrderOriginalInfo.buyDate}" pattern="yyyy-MM-dd hh:mm"/></td>
					<td>${erpOrderOriginalInfo.auditStatusTxt}</td>
					<td>
						<shiro:hasPermission name="order:auditFlow:editData">
							<c:if test="${erpOrderOriginalInfo.auditStatus == '3'}">
								<a href="${ctx}/order/erpOrderOriginalInfo/toOrderReviewUpdate?orderId=${erpOrderOriginalInfo.id}">修改</a>
							</c:if>
						</shiro:hasPermission>
						<shiro:hasPermission name="order:auditFlow:audit">
							<c:if test="${erpOrderOriginalInfo.auditStatus != '3'}">
								<a href="javascript:;" auditStatus="${erpOrderOriginalInfo.auditStatus}" orderId="${erpOrderOriginalInfo.id}" class="toAudit">审核</a>
							</c:if>
						</shiro:hasPermission>
					</td>
				</tr>
				</c:forEach>
			</tbody>
		</table>
		<div class="pagination">${page}</div>
	</div>
	<script src="${ctxStatic}/common/erpshop.js?v=${staticVersion}" type="text/javascript"></script>
	<script src="${ctxStatic}/common/act.js?v=${staticVersion}" type="text/javascript"></script>
	<script type="text/javascript">
// 		$(document).ready(function() {
// 			$(document).on("click", ".toAudit", function(event) {
// 				event.preventDefault();
// 				/* Act on the event */
// 			}).on("click", "input[type='reset']", function(event) {
// 				$("select").select2("val", "");
// 			});
// 		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</body>
</html>