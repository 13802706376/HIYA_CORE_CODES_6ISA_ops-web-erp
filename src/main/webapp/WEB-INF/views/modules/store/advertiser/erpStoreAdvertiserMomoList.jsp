<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>陌陌广告主开通资料管理</title>
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
		<li class="active"><a href="${ctx}/store/advertiser/erpStoreAdvertiserMomo/">陌陌广告主开通资料列表</a></li>
		<shiro:hasPermission name="store:advertiser:erpStoreAdvertiserMomo:edit"><li><a href="${ctx}/store/advertiser/erpStoreAdvertiserMomo/form">陌陌广告主开通资料添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="erpStoreAdvertiserMomo" action="${ctx}/store/advertiser/erpStoreAdvertiserMomo/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>陌陌号：</label>
				<form:input path="accountNo" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>品牌名称：</label>
				<form:input path="brandName" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>ICP：</label>
				<form:input path="icp" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>陌陌号</th>
				<th>品牌名称</th>
				<th>ICP</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="store:advertiser:erpStoreAdvertiserMomo:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="erpStoreAdvertiserMomo">
			<tr>
				<td><a href="${ctx}/store/advertiser/erpStoreAdvertiserMomo/form?id=${erpStoreAdvertiserMomo.id}">
					${erpStoreAdvertiserMomo.accountNo}
				</a></td>
				<td>
					${erpStoreAdvertiserMomo.brandName}
				</td>
				<td>
					${erpStoreAdvertiserMomo.icp}
				</td>
				<td>
					<fmt:formatDate value="${erpStoreAdvertiserMomo.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${erpStoreAdvertiserMomo.remarks}
				</td>
				<shiro:hasPermission name="store:advertiser:erpStoreAdvertiserMomo:edit"><td>
    				<a href="${ctx}/store/advertiser/erpStoreAdvertiserMomo/form?id=${erpStoreAdvertiserMomo.id}">修改</a>
					<a href="${ctx}/store/advertiser/erpStoreAdvertiserMomo/delete?id=${erpStoreAdvertiserMomo.id}" onclick="return confirmx('确认要删除该陌陌广告主开通资料吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>