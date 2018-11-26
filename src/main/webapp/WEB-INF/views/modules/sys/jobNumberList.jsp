<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>工号管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var ctx = '${ctx}', ctxStatic='${ctxStatic}';
		var pageDataObject = {
			uploadfileurl: "${fileUrl}",
			userid: "${fns:getUser().id}",
			username: "${fns:getUser().name}"
		};
	</script>
</head>
<body>
	<div style="height: 38px; position: relative;">
		<ul class="nav nav-tabs">
			<li class="active"><a href="javascript:;">工号管理</a></li>
		</ul>
		<shiro:hasPermission name="sys:jobNumber:edit">
			<div style="position: absolute;right: 10px;top: 0px;height: 38px;line-height: 38px;">
				<button type="button" class="btn jobNumberAction">新增</button>
			</div>
		</shiro:hasPermission>
	</div>
	<div style="padding: 10px" id="rootNode">
		<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th>工号</th>
					<th>角色</th>
					<th>人员</th>
					<th>电话</th>
					<th>评分</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="jobNumber">
				<tr>
					<td><img src="${fileUrl}${jobNumber.iconImg}" style="border-radius: 50%; height: 40px; width: 40px;">&nbsp;${jobNumber.jobNumber}</td>
					<td>${jobNumber.roleName}</td>
					<td>${jobNumber.userName}</td>
					<td>${jobNumber.telephone}</td>
					<td>${jobNumber.score}</td>
					<td>
						<shiro:hasPermission name="sys:jobNumber:edit">
							<a href="javascript:;" class="jobNumberAction" data-jobnumberid="${jobNumber.id}">编辑</a>&nbsp;&nbsp;&nbsp;&nbsp;
							<a href="javascript:;" class="jobNumberDelete" data-jobnumberid="${jobNumber.id}">删除</a>
						</shiro:hasPermission>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		<div class="pagination">${page}</div>
	</div>
	<script type="text/javascript">
		function page(n, s) {
			location.href = ctx+'/sys/jobNumber/list?pageNo='+n+'&pageSize='+s;
		}
	</script>
	<script src="${ctxStatic}/common/erpshop.js?v=${staticVersion}" type="text/javascript"></script>
</body>
</html>