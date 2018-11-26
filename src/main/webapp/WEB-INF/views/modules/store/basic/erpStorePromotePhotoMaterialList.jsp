<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>推广图片素材管理</title>
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
		<li class="active"><a href="${ctx}/store/basic/erpStorePromotePhotoMaterial/">推广图片素材列表</a></li>
		<shiro:hasPermission name="store:basic:erpStorePromotePhotoMaterial:edit"><li><a href="${ctx}/store/basic/erpStorePromotePhotoMaterial/form">推广图片素材添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="erpStorePromotePhotoMaterial" action="${ctx}/store/basic/erpStorePromotePhotoMaterial/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>审核状态，0：未提交，1：待审核，2：正在审核，3：拒绝，4：通过，默认0：</label>
				<form:input path="auditStatus" htmlEscape="false" maxlength="4" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>审核状态，0：未提交，1：待审核，2：正在审核，3：拒绝，4：通过，默认0</th>
				<th>门店环境图</th>
				<th>产品图</th>
				<th>门店环境图数量</th>
				<th>产品图数量</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="store:basic:erpStorePromotePhotoMaterial:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="erpStorePromotePhotoMaterial">
			<tr>
				<td><a href="${ctx}/store/basic/erpStorePromotePhotoMaterial/form?id=${erpStorePromotePhotoMaterial.id}">
					${erpStorePromotePhotoMaterial.auditStatus}
				</a></td>
				<td>
					${erpStorePromotePhotoMaterial.environmentPhoto}
				</td>
				<td>
					${erpStorePromotePhotoMaterial.productPhoto}
				</td>
				<td>
					${erpStorePromotePhotoMaterial.environmentPhotoCount}
				</td>
				<td>
					${erpStorePromotePhotoMaterial.productPhotoCount}
				</td>
				<td>
					<fmt:formatDate value="${erpStorePromotePhotoMaterial.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${erpStorePromotePhotoMaterial.remarks}
				</td>
				<shiro:hasPermission name="store:basic:erpStorePromotePhotoMaterial:edit"><td>
    				<a href="${ctx}/store/basic/erpStorePromotePhotoMaterial/form?id=${erpStorePromotePhotoMaterial.id}">修改</a>
					<a href="${ctx}/store/basic/erpStorePromotePhotoMaterial/delete?id=${erpStorePromotePhotoMaterial.id}" onclick="return confirmx('确认要删除该推广图片素材吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>