<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>朋友圈广告主开通资料管理</title>
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
		<li class="active"><a href="${ctx}/store/advertiser/erpStoreAdvertiserFriends/">朋友圈广告主开通资料列表</a></li>
		<shiro:hasPermission name="store:advertiser:erpStoreAdvertiserFriends:edit"><li><a href="${ctx}/store/advertiser/erpStoreAdvertiserFriends/form">朋友圈广告主开通资料添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="erpStoreAdvertiserFriends" action="${ctx}/store/advertiser/erpStoreAdvertiserFriends/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>提供公众号账号、密码,0:否,1:是,默认0：</label>
				<form:input path="provideAccountInfo" htmlEscape="false" maxlength="4" class="input-medium"/>
			</li>
			<li><label>公众号登录账号：</label>
				<form:input path="accountNo" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>公众号原始ID：</label>
				<form:input path="accountOriginalId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>提供公众号账号、密码,0:否,1:是,默认0</th>
				<th>公众号登录账号</th>
				<th>公众号原始ID</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="store:advertiser:erpStoreAdvertiserFriends:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="erpStoreAdvertiserFriends">
			<tr>
				<td><a href="${ctx}/store/advertiser/erpStoreAdvertiserFriends/form?id=${erpStoreAdvertiserFriends.id}">
					${erpStoreAdvertiserFriends.provideAccountInfo}
				</a></td>
				<td>
					${erpStoreAdvertiserFriends.accountNo}
				</td>
				<td>
					${erpStoreAdvertiserFriends.accountOriginalId}
				</td>
				<td>
					<fmt:formatDate value="${erpStoreAdvertiserFriends.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${erpStoreAdvertiserFriends.remarks}
				</td>
				<shiro:hasPermission name="store:advertiser:erpStoreAdvertiserFriends:edit"><td>
    				<a href="${ctx}/store/advertiser/erpStoreAdvertiserFriends/form?id=${erpStoreAdvertiserFriends.id}">修改</a>
					<a href="${ctx}/store/advertiser/erpStoreAdvertiserFriends/delete?id=${erpStoreAdvertiserFriends.id}" onclick="return confirmx('确认要删除该朋友圈广告主开通资料吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>