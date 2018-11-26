<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>act_ru_task_ext生成管理</title>
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
		<li class="active"><a href="${ctx}/act/taskExt/">act_ru_task_ext生成列表</a></li>
		<shiro:hasPermission name="act:taskExt:edit"><li><a href="${ctx}/act/taskExt/form">act_ru_task_ext生成添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="taskExt" action="${ctx}/act/taskExt/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>任务ID：</label>
				<form:input path="taskId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>是否是待生产库：</label>
				<form:input path="pendingProdFlag" htmlEscape="false" maxlength="1" class="input-medium"/>
			</li>
			<li><label>任务状态：</label>
				<form:input path="status" htmlEscape="false" maxlength="1" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>主键</th>
				<th>任务ID</th>
				<th>是否是待生产库</th>
				<th>任务状态</th>
				<th>备注</th>
				<th>创建人</th>
				<th>创建时间</th>
				<th>修改人</th>
				<th>修改时间</th>
				<shiro:hasPermission name="act:taskExt:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="taskExt">
			<tr>
				<td><a href="${ctx}/act/taskExt/form?id=${taskExt.id}">
					${taskExt.id}
				</a></td>
				<td>
					${taskExt.taskId}
				</td>
				<td>
					${taskExt.pendingProdFlag}
				</td>
				<td>
					${taskExt.status}
				</td>
				<td>
					${taskExt.remark}
				</td>
				<td>
					${taskExt.createBy}
				</td>
				<td>
					<fmt:formatDate value="${taskExt.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${taskExt.updateBy}
				</td>
				<td>
					<fmt:formatDate value="${taskExt.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="act:taskExt:edit"><td>
    				<a href="${ctx}/act/taskExt/form?id=${taskExt.id}">修改</a>
					<a href="${ctx}/act/taskExt/delete?id=${taskExt.id}" onclick="return confirmx('确认要删除该act_ru_task_ext生成吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>