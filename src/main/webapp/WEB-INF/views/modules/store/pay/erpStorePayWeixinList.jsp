<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>微信支付开通资料管理</title>
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
		<li class="active"><a href="${ctx}/store/pay/erpStorePayWeixin/">微信支付开通资料列表</a></li>
		<shiro:hasPermission name="store:pay:erpStorePayWeixin:edit"><li><a href="${ctx}/store/pay/erpStorePayWeixin/form">微信支付开通资料添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="erpStorePayWeixin" action="${ctx}/store/pay/erpStorePayWeixin/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>开户名称：</label>
				<form:input path="openAccountName" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>银行卡号：</label>
				<form:input path="creditCardNo" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>公众号登录账号：</label>
				<form:input path="publicAccountNo" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>公众号APPID：</label>
				<form:input path="publicAccountAppid" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>开户名称</th>
				<th>银行卡号</th>
				<th>公众号登录账号</th>
				<th>公众号登录密码</th>
				<th>公众号APPID</th>
				<th>审核状态，0：未提交，1：待审核，2：正在审核，3：拒绝，4：通过，默认0</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="store:pay:erpStorePayWeixin:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="erpStorePayWeixin">
			<tr>
				<td><a href="${ctx}/store/pay/erpStorePayWeixin/form?id=${erpStorePayWeixin.id}">
					${erpStorePayWeixin.openAccountName}
				</a></td>
				<td>
					${erpStorePayWeixin.creditCardNo}
				</td>
				<td>
					${erpStorePayWeixin.publicAccountNo}
				</td>
				<td>
					${erpStorePayWeixin.publicAccountPassword}
				</td>
				<td>
					${erpStorePayWeixin.publicAccountAppid}
				</td>
				<td>
					${erpStorePayWeixin.auditStatus}
				</td>
				<td>
					<fmt:formatDate value="${erpStorePayWeixin.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${erpStorePayWeixin.remarks}
				</td>
				<shiro:hasPermission name="store:pay:erpStorePayWeixin:edit"><td>
    				<a href="${ctx}/store/pay/erpStorePayWeixin/form?id=${erpStorePayWeixin.id}">修改</a>
					<a href="${ctx}/store/pay/erpStorePayWeixin/delete?id=${erpStorePayWeixin.id}" onclick="return confirmx('确认要删除该微信支付开通资料吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>