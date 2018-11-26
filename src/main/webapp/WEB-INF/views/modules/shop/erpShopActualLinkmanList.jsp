<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>商户实际联系人信息管理</title>
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
		<li class="active"><a href="${ctx}/shop/erpShopActualLinkman/">商户实际联系人信息列表</a></li>
		<shiro:hasPermission name="shop:erpShopActualLinkman:edit"><li><a href="${ctx}/shop/erpShopActualLinkman/form">商户实际联系人信息添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="erpShopActualLinkman" action="${ctx}/shop/erpShopActualLinkman/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>姓名：</label>
				<form:input path="name" htmlEscape="false" maxlength="30" class="input-medium"/>
			</li>
			<li><label>手机号：</label>
				<form:input path="phoneNo" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label>职位：</label>
				<form:input path="position" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>姓名</th>
				<th>手机号</th>
				<th>职位</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="shop:erpShopActualLinkman:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="erpShopActualLinkman">
			<tr>
				<td><a href="${ctx}/shop/erpShopActualLinkman/form?id=${erpShopActualLinkman.id}">
					${erpShopActualLinkman.name}
				</a></td>
				<td>
					${erpShopActualLinkman.phoneNo}
				</td>
				<td>
					${erpShopActualLinkman.position}
				</td>
				<td>
					<fmt:formatDate value="${erpShopActualLinkman.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${erpShopActualLinkman.remarks}
				</td>
				<shiro:hasPermission name="shop:erpShopActualLinkman:edit"><td>
    				<a href="${ctx}/shop/erpShopActualLinkman/form?id=${erpShopActualLinkman.id}">修改</a>
					<a href="${ctx}/shop/erpShopActualLinkman/delete?id=${erpShopActualLinkman.id}" onclick="return confirmx('确认要删除该商户实际联系人信息吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>