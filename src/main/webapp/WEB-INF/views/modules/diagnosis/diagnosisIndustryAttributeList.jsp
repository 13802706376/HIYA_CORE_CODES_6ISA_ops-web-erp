<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>行业属性管理</title>
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
		<li class="active"><a href="${ctx}/diagnosis/diagnosisIndustryAttribute/">行业属性列表</a></li>
		<shiro:hasPermission name="diagnosis:diagnosisIndustryAttribute:edit"><li><a href="${ctx}/diagnosis/diagnosisIndustryAttribute/form">行业属性添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="diagnosisIndustryAttribute" action="${ctx}/diagnosis/diagnosisIndustryAttribute/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>行业名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li><label>推荐的行业属性：</label>
				<form:input path="industryAttribute" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li><label>父类行业：</label>
				<form:input path="pid" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>当前行业分类级别,默认从1开始递增：</label>
				<form:input path="level" htmlEscape="false" maxlength="4" class="input-medium"/>
			</li>
			<li><label>排序：</label>
				<form:input path="order" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>行业名称</th>
				<th>推荐的行业属性</th>
				<th>父类行业</th>
				<th>当前行业分类级别,默认从1开始递增</th>
				<th>排序</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="diagnosis:diagnosisIndustryAttribute:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="diagnosisIndustryAttribute">
			<tr>
				<td><a href="${ctx}/diagnosis/diagnosisIndustryAttribute/form?id=${diagnosisIndustryAttribute.id}">
					${diagnosisIndustryAttribute.name}
				</a></td>
				<td>
					${diagnosisIndustryAttribute.industryAttribute}
				</td>
				<td>
					${diagnosisIndustryAttribute.pid}
				</td>
				<td>
					${diagnosisIndustryAttribute.level}
				</td>
				<td>
					${diagnosisIndustryAttribute.order}
				</td>
				<td>
					<fmt:formatDate value="${diagnosisIndustryAttribute.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${diagnosisIndustryAttribute.remarks}
				</td>
				<shiro:hasPermission name="diagnosis:diagnosisIndustryAttribute:edit"><td>
    				<a href="${ctx}/diagnosis/diagnosisIndustryAttribute/form?id=${diagnosisIndustryAttribute.id}">修改</a>
					<a href="${ctx}/diagnosis/diagnosisIndustryAttribute/delete?id=${diagnosisIndustryAttribute.id}" onclick="return confirmx('确认要删除该行业属性吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>