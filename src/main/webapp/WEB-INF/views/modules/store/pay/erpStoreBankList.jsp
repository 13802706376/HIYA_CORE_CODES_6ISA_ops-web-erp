<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>银行信息管理</title>
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
		<li class="active"><a href="${ctx}/store/pay/erpStoreBank/">银行信息列表</a></li>
		<shiro:hasPermission name="store:pay:erpStoreBank:edit"><li><a href="${ctx}/store/pay/erpStoreBank/form">银行信息添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="erpStoreBank" action="${ctx}/store/pay/erpStoreBank/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>开户名称：</label>
				<form:input path="openAccountName" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>银行卡号：</label>
				<form:input path="creditCardNo" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>银行联行号，唯一对应支行：</label>
				<form:input path="bankNo" htmlEscape="false" maxlength="64" class="input-medium"/>
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
				<th>银行联行号，唯一对应支行</th>
				<th>当前银联账号绑定掌贝设备数量</th>
				<th>支付方式，0：微信，1：银联</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="store:pay:erpStoreBank:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="erpStoreBank">
			<tr>
				<td><a href="${ctx}/store/pay/erpStoreBank/form?id=${erpStoreBank.id}">
					${erpStoreBank.openAccountName}
				</a></td>
				<td>
					${erpStoreBank.creditCardNo}
				</td>
				<td>
					${erpStoreBank.bankNo}
				</td>
				<td>
					${erpStoreBank.zhangbeiBindCount}
				</td>
				<td>
					${erpStoreBank.payWay}
				</td>
				<td>
					<fmt:formatDate value="${erpStoreBank.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${erpStoreBank.remarks}
				</td>
				<shiro:hasPermission name="store:pay:erpStoreBank:edit"><td>
    				<a href="${ctx}/store/pay/erpStoreBank/form?id=${erpStoreBank.id}">修改</a>
					<a href="${ctx}/store/pay/erpStoreBank/delete?id=${erpStoreBank.id}" onclick="return confirmx('确认要删除该银行信息吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>