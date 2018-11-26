<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>优惠形式配置表管理</title>
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
		<li class="active"><a href="${ctx}/diagnosis/diagnosisDiscountTypeConfig/">优惠形式配置表列表</a></li>
		<shiro:hasPermission name="diagnosis:diagnosisDiscountTypeConfig:edit"><li><a href="${ctx}/diagnosis/diagnosisDiscountTypeConfig/form">优惠形式配置表添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="diagnosisDiscountTypeConfig" action="${ctx}/diagnosis/diagnosisDiscountTypeConfig/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>推荐的行业属性</th>
				<th>推广需求</th>
				<th>优惠形式</th>
				<th>分值</th>
				<th>备注信息</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="diagnosisDiscountTypeConfig">
			<tr>
				<td>${diagnosisDiscountTypeConfig.industryAttributeName }</td>
				<td>${diagnosisDiscountTypeConfig.activityRequirementName }</td>
				<td>${diagnosisDiscountTypeConfig.discountTypeName }</td>
				<td>${diagnosisDiscountTypeConfig.score }</td>
				<td>${diagnosisDiscountTypeConfig.remarks}</td>
				<td>
    				<a href="${ctx}/diagnosis/diagnosisDiscountTypeConfig/form?id=${diagnosisDiscountTypeConfig.id}">修改</a>
					<a href="${ctx}/diagnosis/diagnosisDiscountTypeConfig/delete?id=${diagnosisDiscountTypeConfig.id}" onclick="return confirmx('确认要删除该优惠形式配置表吗？', this.href)">删除</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>