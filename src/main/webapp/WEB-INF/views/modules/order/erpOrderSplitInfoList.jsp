<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>分单管理</title>
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
		<li class="active"><a href="${ctx}/order/erpOrderSplitInfo/">分单列表</a></li>
		<shiro:hasPermission name="order:erpOrderSplitInfo:edit"><li><a href="${ctx}/order/erpOrderSplitInfo/form">分单添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="erpOrderSplitInfo" action="${ctx}/order/erpOrderSplitInfo/" method="post" class="breadcrumb form-search">
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
				<th>num</th>
				<shiro:hasPermission name="order:erpOrderSplitInfo:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="erpOrderSplitInfo">
			<tr>
				<td><a href="${ctx}/order/erpOrderSplitInfo/form?id=${erpOrderSplitInfo.id}">
					${erpOrderSplitInfo.goodName}
				</a></td>
				<td>
					${erpOrderSplitInfo.goodTypeName}
				</td>
				<td>
					${erpOrderSplitInfo.num}
				</td>
				<shiro:hasPermission name="order:erpOrderSplitInfo:edit"><td>
    				<a href="${ctx}/order/erpOrderSplitInfo/form?id=${erpOrderSplitInfo.id}">修改</a>
					<a href="${ctx}/order/erpOrderSplitInfo/delete?id=${erpOrderSplitInfo.id}" onclick="return confirmx('确认要删除该分单吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>