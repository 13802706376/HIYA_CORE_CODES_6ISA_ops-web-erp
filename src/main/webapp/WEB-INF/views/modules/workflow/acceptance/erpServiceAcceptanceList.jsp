<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>服务验收评价管理</title>
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
		<li class="active"><a href="${ctx}/workflow.acceptance/erpServiceAcceptance/">服务验收评价列表</a></li>
		<shiro:hasPermission name="workflow.acceptance:erpServiceAcceptance:edit"><li><a href="${ctx}/workflow.acceptance/erpServiceAcceptance/form">服务验收评价添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="erpServiceAcceptance" action="${ctx}/workflow.acceptance/erpServiceAcceptance/" method="post" class="breadcrumb form-search">
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
				<th>备注</th>
				<th>更新日期</th>
				<shiro:hasPermission name="workflow.acceptance:erpServiceAcceptance:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="erpServiceAcceptance">
			<tr>
				<td><a href="${ctx}/workflow.acceptance/erpServiceAcceptance/form?id=${erpServiceAcceptance.id}">
					${erpServiceAcceptance.remarks}
				</a></td>
				<td>
					<fmt:formatDate value="${erpServiceAcceptance.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="workflow.acceptance:erpServiceAcceptance:edit"><td>
    				<a href="${ctx}/workflow.acceptance/erpServiceAcceptance/form?id=${erpServiceAcceptance.id}">修改</a>
					<a href="${ctx}/workflow.acceptance/erpServiceAcceptance/delete?id=${erpServiceAcceptance.id}" onclick="return confirmx('确认要删除该服务验收评价吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>