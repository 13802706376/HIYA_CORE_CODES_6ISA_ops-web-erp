<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>推广资料操作日志管理</title>
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
		<li class="active"><a href="${ctx}/order/erpPromotionMaterialLog/">推广资料操作日志列表</a></li>
		<shiro:hasPermission name="order:erpPromotionMaterialLog:edit"><li><a href="${ctx}/order/erpPromotionMaterialLog/form">推广资料操作日志添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="erpPromotionMaterialLog" action="${ctx}/order/erpPromotionMaterialLog/" method="post" class="breadcrumb form-search">
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
				<th>备注信息</th>
				<shiro:hasPermission name="order:erpPromotionMaterialLog:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="erpPromotionMaterialLog">
			<tr>
				<td><a href="${ctx}/order/erpPromotionMaterialLog/form?id=${erpPromotionMaterialLog.id}">
					${erpPromotionMaterialLog.remarks}
				</a></td>
				<shiro:hasPermission name="order:erpPromotionMaterialLog:edit"><td>
    				<a href="${ctx}/order/erpPromotionMaterialLog/form?id=${erpPromotionMaterialLog.id}">修改</a>
					<a href="${ctx}/order/erpPromotionMaterialLog/delete?id=${erpPromotionMaterialLog.id}" onclick="return confirmx('确认要删除该推广资料操作日志吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>