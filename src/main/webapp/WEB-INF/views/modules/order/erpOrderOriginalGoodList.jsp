<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>订单商品管理</title>
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
		<li class="active"><a href="${ctx}/order/erpOrderOriginalGood/">订单商品列表</a></li>
		<shiro:hasPermission name="order:erpOrderOriginalGood:edit"><li><a href="${ctx}/order/erpOrderOriginalGood/form">订单商品添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="erpOrderOriginalGood" action="${ctx}/order/erpOrderOriginalGood/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>商品名称</th>
				<th>商品类型名称</th>
				<th>实际价格(单位：分)</th>
				<th>商品总共数量</th>
				<th>处理中的商品数量</th>
				<th>待处理的商品数量</th>
				<th>已完成的商品数量</th>
				<shiro:hasPermission name="order:erpOrderOriginalGood:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="erpOrderOriginalGood">
			<tr>
				<td><a href="${ctx}/order/erpOrderOriginalGood/form?id=${erpOrderOriginalGood.id}">
					${erpOrderOriginalGood.goodName}
				</a></td>
				<td>
					${erpOrderOriginalGood.goodTypeName}
				</td>
				<td>
					${erpOrderOriginalGood.realPrice}
				</td>
				<td>
					${erpOrderOriginalGood.num}
				</td>
				<td>
					${erpOrderOriginalGood.processNum}
				</td>
				<td>
					${erpOrderOriginalGood.pendingNum}
				</td>
				<td>
					${erpOrderOriginalGood.finishNum}
				</td>
				<shiro:hasPermission name="order:erpOrderOriginalGood:edit"><td>
    				<a href="${ctx}/order/erpOrderOriginalGood/form?id=${erpOrderOriginalGood.id}">修改</a>
					<a href="${ctx}/order/erpOrderOriginalGood/delete?id=${erpOrderOriginalGood.id}" onclick="return confirmx('确认要删除该订单商品吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>