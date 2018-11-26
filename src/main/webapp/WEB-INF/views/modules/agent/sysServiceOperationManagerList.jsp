<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>服务商运营经关系对应表管理</title>
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
		<li class="active"><a href="${ctx}/agent/sysServiceOperationManager/">分公司列表</a></li>
		<shiro:hasPermission name="agent:sysServiceOperationManager:edit"><li><a href="${ctx}/agent/sysServiceOperationManager/form">分公司添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="sysServiceOperationManager" action="${ctx}/agent/sysServiceOperationManager/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>服务商编号：</label>
				<form:input path="serviceNo" htmlEscape="false" maxlength="10" class="input-medium"/>
			</li>
			<li><label>分公司名称：</label>
				<form:input path="companyName" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>服务区域：</label>
				<form:input path="serverAddress" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
	<%-- 		<li><label>默认运营经理人员：</label>
				<form:input path="defaultManager" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>备选运营经理人员子任务处理人：</label>
				<form:input path="alternativeManager" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li> --%>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
		
				<th>服务商编号</th>
				<th>分公司名称</th>
				<th>服务区域</th>
				<th>角色</th>
				<th>默认经理ID</th>
				<th>默认经理</th>
				<th>备选经理ID</th>
				<th>备选经理</th>
				<th>修改时间</th>
				<th>修改人</th>
				<shiro:hasPermission name="agent:sysServiceOperationManager:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="sysServiceOperationManager">
			<tr>
		
				<td >
				<a href="${ctx}/agent/sysServiceOperationManager/form?id=${sysServiceOperationManager.id}">
					${sysServiceOperationManager.serviceNo}
					</a>
				</td>
				<td>
					${sysServiceOperationManager.companyName}
				</td>
				<td>
					${sysServiceOperationManager.serverAddress}
				</td>
				<td>
					${sysServiceOperationManager.roleName}
				</td>
				
					<td>
					${sysServiceOperationManager.defaultManagerId}
				</td>
				<td>
					${sysServiceOperationManager.defaultManager}
				</td>
				<td>
					${sysServiceOperationManager.alternativeManagerId}
				</td>
				<td>
					${sysServiceOperationManager.alternativeManager}
				</td>
				<td>
					<fmt:formatDate value="${sysServiceOperationManager.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${sysServiceOperationManager.updateBy.id}
				</td>
				<shiro:hasPermission name="agent:sysServiceOperationManager:edit"><td>
    				<a href="${ctx}/agent/sysServiceOperationManager/form?id=${sysServiceOperationManager.id}">修改</a>
					<a href="${ctx}/agent/sysServiceOperationManager/delete?id=${sysServiceOperationManager.id}" onclick="return confirmx('确认要删除该服务商运营经关系对应表吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>