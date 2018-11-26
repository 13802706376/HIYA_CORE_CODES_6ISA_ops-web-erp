<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>user管理</title>
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
		<li class="active"><a href="${ctx}/user/user/sysUser/">user列表</a></li>
		<shiro:hasPermission name="user:user:sysUser:edit"><li><a href="${ctx}/user/user/sysUser/form">user添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="sysUser" action="${ctx}/user/user/sysUser/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>姓名：</label>
				<form:input path="name" htmlEscape="false" maxlength="100" class="input-medium"/>
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
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="user:user:sysUser:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="sysUser">
			<tr>
				<td><a href="${ctx}/user/user/sysUser/form?id=${sysUser.id}">
					${sysUser.name}
				</a></td>
				<td>
					<fmt:formatDate value="${sysUser.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${sysUser.remarks}
				</td>
				<shiro:hasPermission name="user:user:sysUser:edit"><td>
    				<a href="${ctx}/user/user/sysUser/form?id=${sysUser.id}">修改</a>
					<a href="${ctx}/user/user/sysUser/delete?id=${sysUser.id}" onclick="return confirmx('确认要删除该user吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>