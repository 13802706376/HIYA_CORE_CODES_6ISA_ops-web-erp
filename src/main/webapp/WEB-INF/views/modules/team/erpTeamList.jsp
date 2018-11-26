<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>团队管理</title>
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
		<li class="active"><a href="${ctx}/team/erpTeam/">团队列表</a></li>
		<shiro:hasPermission name="team:erpTeam:edit"><li><a href="${ctx}/team/erpTeam/form">团队添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="erpTeam" action="${ctx}/team/erpTeam/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>团队名称：</label>
				<form:input path="teamName" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>团队名称</th>
				<th>团队管理员</th>
				<th>团队成员</th>
				<th>公司类型</th>
				<shiro:hasPermission name="team:erpTeam:edit">
					<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="erpTeam">
			<tr>
				<c:choose>
					<c:when test="${erpTeam.companyType != 'agent'}">
						<td><a href="${ctx}/team/erpTeam/form?id=${erpTeam.id}">
								${erpTeam.teamName}
						</a></td>
					</c:when>
					<c:otherwise>
						<td>${erpTeam.teamName}</td>
					</c:otherwise>
				</c:choose>
				<td>${erpTeam.leaderNames }</td>
				<td title="${erpTeam.memberNames}"><c:if
						test="${not empty erpTeam.memberNames}">${fn:substring(erpTeam.memberNames, 0, 10)}......</c:if></td>
				<td>${fns:getDictLabel(erpTeam.companyType, 'company_type', '无')}</td>
				<td>
					<c:if test="${erpTeam.companyType != 'agent'}">
						<shiro:hasPermission name="team:erpTeam:edit">
							<a href="${ctx}/team/erpTeam/form?id=${erpTeam.id}">修改</a>
							<a href="${ctx}/team/erpTeam/delete?id=${erpTeam.id}"
							   onclick="return confirmx('确认要删除该团队吗？', this.href)">删除</a>
						</shiro:hasPermission>
					</c:if>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
