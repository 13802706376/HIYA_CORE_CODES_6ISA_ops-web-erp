<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>微博广告主开通资料管理</title>
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
		<li class="active"><a href="${ctx}/store/advertiser/erpStoreAdvertiserWeibo/">微博广告主开通资料列表</a></li>
		<shiro:hasPermission name="store:advertiser:erpStoreAdvertiserWeibo:edit"><li><a href="${ctx}/store/advertiser/erpStoreAdvertiserWeibo/form">微博广告主开通资料添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="erpStoreAdvertiserWeibo" action="${ctx}/store/advertiser/erpStoreAdvertiserWeibo/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>账号类型，0：个人账号，1：企业账号，默认0：</label>
				<form:input path="accountType" htmlEscape="false" maxlength="4" class="input-medium"/>
			</li>
			<li><label>微博登录账号：</label>
				<form:input path="accountNo" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>微博UID：</label>
				<form:input path="uid" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>微博昵称：</label>
				<form:input path="nickName" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>账号类型，0：个人账号，1：企业账号，默认0</th>
				<th>微博登录账号</th>
				<th>微博UID</th>
				<th>微博昵称</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="store:advertiser:erpStoreAdvertiserWeibo:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="erpStoreAdvertiserWeibo">
			<tr>
				<td><a href="${ctx}/store/advertiser/erpStoreAdvertiserWeibo/form?id=${erpStoreAdvertiserWeibo.id}">
					${erpStoreAdvertiserWeibo.accountType}
				</a></td>
				<td>
					${erpStoreAdvertiserWeibo.accountNo}
				</td>
				<td>
					${erpStoreAdvertiserWeibo.uid}
				</td>
				<td>
					${erpStoreAdvertiserWeibo.nickName}
				</td>
				<td>
					<fmt:formatDate value="${erpStoreAdvertiserWeibo.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${erpStoreAdvertiserWeibo.remarks}
				</td>
				<shiro:hasPermission name="store:advertiser:erpStoreAdvertiserWeibo:edit"><td>
    				<a href="${ctx}/store/advertiser/erpStoreAdvertiserWeibo/form?id=${erpStoreAdvertiserWeibo.id}">修改</a>
					<a href="${ctx}/store/advertiser/erpStoreAdvertiserWeibo/delete?id=${erpStoreAdvertiserWeibo.id}" onclick="return confirmx('确认要删除该微博广告主开通资料吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>