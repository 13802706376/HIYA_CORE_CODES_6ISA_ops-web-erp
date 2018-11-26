<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>商品分类管理</title>
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
		<shiro:hasPermission name="good:erpGoodInfo:view">
			<li><a href="${ctx}/good/erpGoodInfo/">商品服务管理</a></li>
		</shiro:hasPermission>
		<li class="active"><a href="${ctx}/good/category/erpGoodCategory/">商品类型管理</a></li>
		<shiro:hasPermission name="good.category:erpGoodCategory:edit">
			<li><a href="${ctx}/good/category/form?sort=10">商品类型添加</a></li>
		</shiro:hasPermission>
		
		<li><a href="${ctx}/good/erpGoodServiceItem/list">服务项目管理</a></li>
		
		<li><a href="${ctx}/good/erpGoodServiceItem/form?sort=10">服务项目添加</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="erpGoodCategory" action="${ctx}/good/category/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>类型名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>商品类型名称</th>
				<shiro:hasPermission name="good.category:erpGoodCategory:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="erpGoodCategory">
			<tr>
				<td><a href="${ctx}/good/category/form?id=${erpGoodCategory.id}">
					${erpGoodCategory.name}
				</a></td>
				<c:choose>
				   <c:when test="${erpGoodCategory.readonly == '1'}">  
				   		 <td>  不可编辑    </td>
				   </c:when>
				   <c:otherwise> 
						<shiro:hasPermission name="good.category:erpGoodCategory:edit"><td>
		    				<a href="${ctx}/good/category/form?id=${erpGoodCategory.id}">修改</a>
							<a href="${ctx}/good/category/delete?id=${erpGoodCategory.id}" onclick="return confirmx('确认要删除该商品分类吗？', this.href)">删除</a>
						</td></shiro:hasPermission>
				   </c:otherwise>
				</c:choose>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>