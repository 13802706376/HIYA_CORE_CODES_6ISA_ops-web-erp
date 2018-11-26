<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>商户营业资质信息管理</title>
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
		<li class="active"><a href="${ctx}/store/basic/erpStoreCredentials/">商户营业资质信息列表</a></li>
		<shiro:hasPermission name="store:basic:erpStoreCredentials:edit"><li><a href="${ctx}/store/basic/erpStoreCredentials/form">商户营业资质信息添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="erpStoreCredentials" action="${ctx}/store/basic/erpStoreCredentials/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>注册名称：</label>
				<form:input path="registerName" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>注册号：</label>
				<form:input path="registerNo" htmlEscape="false" maxlength="30" class="input-medium"/>
			</li>
			<li><label>资质有效期起始日期：</label>
				<input name="startDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${erpStoreCredentials.startDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>资质有效期结束日期：</label>
				<input name="endDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${erpStoreCredentials.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>注册名称</th>
				<th>注册号</th>
				<th>注册城市</th>
				<th>注册地址</th>
				<th>经营范围</th>
				<th>资质有效期起始日期</th>
				<th>资质有效期结束日期</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="store:basic:erpStoreCredentials:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="erpStoreCredentials">
			<tr>
				<td><a href="${ctx}/store/basic/erpStoreCredentials/form?id=${erpStoreCredentials.id}">
					${erpStoreCredentials.registerName}
				</a></td>
				<td>
					${erpStoreCredentials.registerNo}
				</td>
				<td>
					${erpStoreCredentials.registerCity}
				</td>
				<td>
					${erpStoreCredentials.registerAddress}
				</td>
				<td>
					${erpStoreCredentials.businessScope}
				</td>
				<td>
					<fmt:formatDate value="${erpStoreCredentials.startDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${erpStoreCredentials.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${erpStoreCredentials.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${erpStoreCredentials.remarks}
				</td>
				<shiro:hasPermission name="store:basic:erpStoreCredentials:edit"><td>
    				<a href="${ctx}/store/basic/erpStoreCredentials/form?id=${erpStoreCredentials.id}">修改</a>
					<a href="${ctx}/store/basic/erpStoreCredentials/delete?id=${erpStoreCredentials.id}" onclick="return confirmx('确认要删除该商户营业资质信息吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>