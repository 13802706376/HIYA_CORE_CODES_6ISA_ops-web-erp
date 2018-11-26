<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>节假日管理</title>
	<meta name="decorator" content="default"/> 
	<script type="text/javascript">
		$(document).ready(function() {
			//dwr.engine.setActiveReverseAjax(true);  
			
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
		<li class="active"><a href="${ctx}/holiday/erpHolidays/">节假日列表</a></li>
		<shiro:hasPermission name="holiday:erpHolidays:edit"><li><a href="${ctx}/holiday/erpHolidays/form">节假日添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="erpHolidays" action="${ctx}/holiday/erpHolidays/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>节假日：</label>
				<input name="holidayDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${erpHolidays.holidayDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
			<th>节假日</th>
				<th>备注</th>
				<shiro:hasPermission name="holiday:erpHolidays:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="erpHolidays">
			<tr>
				<td>
					<fmt:formatDate value="${erpHolidays.holidayDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					${erpHolidays.remark}
				</td>
				<shiro:hasPermission name="holiday:erpHolidays:edit"><td>
    				<a href="${ctx}/holiday/erpHolidays/form?id=${erpHolidays.id}">修改</a>
					<a href="${ctx}/holiday/erpHolidays/delete?id=${erpHolidays.id}" onclick="return confirmx('确认要删除该新增节假日成功吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>