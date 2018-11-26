<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>银联支付开通资料管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/store/pay/erpStorePayUnionpay/">银联支付开通资料列表</a></li>
		<shiro:hasPermission name="store:pay:erpStorePayUnionpay:edit"><li><a href="${ctx}/store/pay/erpStorePayUnionpay/form">银联支付开通资料添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="erpStorePayUnionpay" action="${ctx}/store/pay/erpStorePayUnionpay/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>账号类型，0：对公账号，1：法人账号，默认0：</label>
				<form:input path="accountType" htmlEscape="false" maxlength="4" class="input-medium"/>
			</li>
			<li><label>开户名称：</label>
				<form:input path="openAccountName" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>银行卡号：</label>
				<form:input path="creditCardNo" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>当前银联账号绑定掌贝设备数量：</label>
				<form:input path="zhangbeiBindCount" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>账号类型，0：对公账号，1：法人账号，默认0</th>
				<th>审核状态，0：未提交，1：待审核，2：正在审核，3：拒绝，4：通过，默认0</th>
				<th>开户名称</th>
				<th>银行卡号</th>
				<th>当前银联账号绑定掌贝设备数量</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="store:pay:erpStorePayUnionpay:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="erpStorePayUnionpay">
			<tr>
				<td><a href="${ctx}/store/pay/erpStorePayUnionpay/form?id=${erpStorePayUnionpay.id}">
					${erpStorePayUnionpay.accountType}
				</a></td>
				<td>
					${erpStorePayUnionpay.auditStatus}
				</td>
				<td>
					${erpStorePayUnionpay.openAccountName}
				</td>
				<td>
					${erpStorePayUnionpay.creditCardNo}
				</td>
				<td>
					${erpStorePayUnionpay.zhangbeiBindCount}
				</td>
				<td>
					<fmt:formatDate value="${erpStorePayUnionpay.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${erpStorePayUnionpay.remarks}
				</td>
				<shiro:hasPermission name="store:pay:erpStorePayUnionpay:edit"><td>
    				<a href="${ctx}/store/pay/erpStorePayUnionpay/form?id=${erpStorePayUnionpay.id}">修改</a>
					<a href="${ctx}/store/pay/erpStorePayUnionpay/delete?id=${erpStorePayUnionpay.id}" onclick="return confirmx('确认要删除该银联支付开通资料吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>