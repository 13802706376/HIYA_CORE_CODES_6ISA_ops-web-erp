<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>人员部门关联关系管理</title>
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
		<li class="active"><a href="${ctx}/sys/sysOfficeUser/">人员部门关联关系列表</a></li>
		<shiro:hasPermission name="sys:sysOfficeUser:edit"><li><a href="${ctx}/sys/sysOfficeUser/form">人员部门关联关系添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="sysOfficeUser" action="${ctx}/sys/sysOfficeUser/" method="post" class="breadcrumb form-search">
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
				<th>更新日期</th>
				<shiro:hasPermission name="sys:sysOfficeUser:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="sysOfficeUser">
			<tr>
				<td><a href="${ctx}/sys/sysOfficeUser/form?id=${sysOfficeUser.id}">
					${sysOfficeUser.remarks}
				</a></td>
				<td>
					<fmt:formatDate value="${sysOfficeUser.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="sys:sysOfficeUser:edit"><td>
    				<a href="${ctx}/sys/sysOfficeUser/form?id=${sysOfficeUser.id}">修改</a>
					<a href="${ctx}/sys/sysOfficeUser/delete?id=${sysOfficeUser.id}" onclick="return confirmx('确认要删除该人员部门关联关系吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>