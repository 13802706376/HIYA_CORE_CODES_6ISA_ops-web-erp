<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>文件信息管理</title>
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
		<li class="active"><a href="${ctx}/file/erpFileInfo/">文件信息列表</a></li>
		<shiro:hasPermission name="file:erpFileInfo:edit"><li><a href="${ctx}/file/erpFileInfo/form">文件信息添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="erpFileInfo" action="${ctx}/file/erpFileInfo/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>文件名：</label>
				<form:input path="name" htmlEscape="false" maxlength="512" class="input-medium"/>
			</li>
			<li><label>文件类型/扩展名：</label>
				<form:input path="type" htmlEscape="false" maxlength="12" class="input-medium"/>
			</li>
			<li><label>文件大小：</label>
				<form:input path="size" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>文件路径：</label>
				<form:input path="path" htmlEscape="false" maxlength="512" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>文件名</th>
				<th>文件类型/扩展名</th>
				<th>文件大小</th>
				<th>文件路径</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="file:erpFileInfo:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="erpFileInfo">
			<tr>
				<td><a href="${ctx}/file/erpFileInfo/form?id=${erpFileInfo.id}">
					${erpFileInfo.name}
				</a></td>
				<td>
					${erpFileInfo.type}
				</td>
				<td>
					${erpFileInfo.size}
				</td>
				<td>
					${erpFileInfo.path}
				</td>
				<td>
					<fmt:formatDate value="${erpFileInfo.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${erpFileInfo.remarks}
				</td>
				<shiro:hasPermission name="file:erpFileInfo:edit"><td>
    				<a href="${ctx}/file/erpFileInfo/form?id=${erpFileInfo.id}">修改</a>
					<a href="${ctx}/file/erpFileInfo/delete?id=${erpFileInfo.id}" onclick="return confirmx('确认要删除该文件信息吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>