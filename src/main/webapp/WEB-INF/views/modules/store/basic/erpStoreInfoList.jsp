<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>门店基本信息管理</title>
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
		<li class="active"><a href="${ctx}/store/basic/erpStoreInfo/">门店基本信息列表</a></li>
		<shiro:hasPermission name="store:basic:erpStoreInfo:edit"><li><a href="${ctx}/store/basic/erpStoreInfo/form">门店基本信息添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="erpStoreInfo" action="${ctx}/store/basic/erpStoreInfo/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>是否是掌贝进件主体,0:否,1:是,默认0：</label>
				<form:input path="isMain" htmlEscape="false" maxlength="4" class="input-medium"/>
			</li>
			<li><label>门店简称：</label>
				<form:input path="shortName" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>门店所在城市：</label>
				<form:input path="city" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>商户类型，0：个体工商商户，1：企业商户，默认0：</label>
				<form:input path="businessType" htmlEscape="false" maxlength="4" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>是否是掌贝进件主体,0:否,1:是,默认0</th>
				<th>门店简称</th>
				<th>门店经营地址</th>
				<th>门店所在城市</th>
				<th>门店电话</th>
				<th>公司网址</th>
				<th>商户类型，0：个体工商商户，1：企业商户，默认0</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="store:basic:erpStoreInfo:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="erpStoreInfo">
			<tr>
				<td><a href="${ctx}/store/basic/erpStoreInfo/form?id=${erpStoreInfo.id}">
					${erpStoreInfo.isMain}
				</a></td>
				<td>
					${erpStoreInfo.shortName}
				</td>
				<td>
					${erpStoreInfo.address}
				</td>
				<td>
					${erpStoreInfo.city}
				</td>
				<td>
					${erpStoreInfo.telephone}
				</td>
				<td>
					${erpStoreInfo.companyUrl}
				</td>
				<td>
					${erpStoreInfo.businessType}
				</td>
				<td>
					<fmt:formatDate value="${erpStoreInfo.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${erpStoreInfo.remarks}
				</td>
				<shiro:hasPermission name="store:basic:erpStoreInfo:edit"><td>
    				<a href="${ctx}/store/basic/erpStoreInfo/form?id=${erpStoreInfo.id}">修改</a>
					<a href="${ctx}/store/basic/erpStoreInfo/delete?id=${erpStoreInfo.id}" onclick="return confirmx('确认要删除该门店基本信息吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>