<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>服务项目管理</title>
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
		
		<li><a href="${ctx}/good/category/list">商品类型管理</a></li>
		
		<shiro:hasPermission name="good.category:erpGoodCategory:edit">
			<li><a href="${ctx}/good/category/form?sort=10">商品类型添加</a></li>
		</shiro:hasPermission>
		
<%-- 		<shiro:hasPermission name="good:erpGoodServiceItem:view"> --%>
			<li class="active"><a href="${ctx}/good/erpGoodServiceItem/list">服务项目管理</a></li>
<%-- 		</shiro:hasPermission> --%>
		
<%-- 		<shiro:hasPermission name="good:erpGoodServiceItem:view"> --%>
			<li><a href="${ctx}/good/erpGoodServiceItem/form?sort=10">服务项目添加</a></li>
<%-- 		</shiro:hasPermission> --%>
	</ul>
	<form:form id="searchForm" modelAttribute="erpGoodServiceItem" action="${ctx}/good/erpGoodServiceItem/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>服务项目名称：</label>
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
				<th>服务项目名称</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="erpGoodCategory">
			<tr>
				<td><a href="${ctx}/good/erpGoodServiceItem/form?id=${erpGoodCategory.id}">
					${erpGoodCategory.name}
				</a></td>
				<td>
				<c:choose>
				   <c:when test="${erpGoodCategory.readonly == 'Y'}">  
				   		 不可编辑    
				   </c:when>
				   <c:otherwise> 
	    				<a href="${ctx}/good/erpGoodServiceItem/form?id=${erpGoodCategory.id}">修改</a>
						<a href="${ctx}/good/erpGoodServiceItem/delete?id=${erpGoodCategory.id}" onclick="return confirmx('确认要删除该服务项目吗？', this.href)">删除</a>
				   </c:otherwise>
				</c:choose>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>