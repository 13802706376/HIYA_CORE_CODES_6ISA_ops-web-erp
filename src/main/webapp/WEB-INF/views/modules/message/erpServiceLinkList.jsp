<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>交互入口配置表管理</title>
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
		<li class="active"><a href="${ctx}/message/erpServiceLink/">交互入口配置表列表</a></li>
		<shiro:hasPermission name="message:erpServiceLink:edit"><li><a href="${ctx}/message/erpServiceLink/form">交互入口配置表添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="erpServiceLink" action="${ctx}/message/erpServiceLink/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>名称</th>
				<th>备注</th>
				<th>更新日期</th>
				<shiro:hasPermission name="message:erpServiceLink:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="erpServiceLink">
			<tr>
				<td><a href="${ctx}/message/erpServiceLink/form?id=${erpServiceLink.id}">
					${erpServiceLink.name}
				</a></td>
				<td>
					${erpServiceLink.remarks}
				</td>
				<td>
					<fmt:formatDate value="${erpServiceLink.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="message:erpServiceLink:edit"><td>
    				<a href="${ctx}/message/erpServiceLink/form?id=${erpServiceLink.id}">修改</a>
					<a href="${ctx}/message/erpServiceLink/delete?id=${erpServiceLink.id}" onclick="return confirmx('确认要删除该交互入口配置表吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>