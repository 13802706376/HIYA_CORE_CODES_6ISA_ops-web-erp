<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>商品信息管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {
		$("#sync").click(function(){
			$.jBox.tip("正在同步，请稍等...", 'loading', {
				timeout : 0,
				persistent : true
			});
			$.post("${ctx}/good/erpGoodInfo/sync", {
				
			}, function(data) {
				if (data.result) {
					$.jBox.closeTip();
					$.jBox.info("同步服务请求已提交，请稍后查询...");
				} else {
					$.jBox.closeTip();
					$.jBox.info("同步失败");
				}
			});
		});
	});
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
	
	function updateGoodCateGory(s, id){
		var submit = function (v, h, f) {
			if (v == 'ok') {
					$.jBox.tip("正在修改，请稍等...", 'loading', {
					timeout : 3000,
					persistent : true
			});
			$.post("${ctx}/good/erpGoodInfo/updateCategoryId", {
					id : id, 
						categoryId : $(s).val()
			}, function(data){
				if (data.result) {
					window.location.reload();
				} else {
					$.jBox.closeTip();
					$.jBox.info(data.message);
				}	
			});
		
		}
		return true; 
	};
	$.jBox.confirm("你确认修改此商品的类型吗？", "提示", submit);
}
	
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/good/erpGoodInfo/">商品服务管理</a>
		</li>
		<shiro:hasPermission name="good.category:erpGoodCategory:view">
			<li><a href="${ctx}/good/category/list">商品类型管理</a></li>
		</shiro:hasPermission>
		<shiro:hasPermission name="good.category:erpGoodCategory:edit">
			<li><a href="${ctx}/good/category/form?sort=10">商品类型添加</a></li>
		</shiro:hasPermission>
		
<%-- 		<shiro:hasPermission name="good:erpGoodServiceItem:view"> --%>
			<li><a href="${ctx}/good/erpGoodServiceItem/list">服务项目管理</a></li>
<%-- 		</shiro:hasPermission> --%>
		
<%-- 		<shiro:hasPermission name="good:erpGoodServiceItem:view"> --%>
			<li><a href="${ctx}/good/erpGoodServiceItem/form?sort=10">服务项目添加</a></li>
<%-- 		</shiro:hasPermission> --%>
		<!-- 
			<shiro:hasPermission name="good:erpGoodInfo:edit">
				<li><a href="${ctx}/good/erpGoodInfo/form">商品信息添加</a></li>
			</shiro:hasPermission>
	 	-->
	</ul>
	<form:form id="searchForm" modelAttribute="erpGoodInfo"
		action="${ctx}/good/erpGoodInfo/" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<ul class="ul-form">
			<li><label>商品名称：</label> <form:input path="name"
					htmlEscape="false" maxlength="64" class="input-medium" /></li>
			<li><label>类型：</label> <form:select id="category.id"
					path="category.id" class="input-medium">
					<form:option value="" label="全部"></form:option>
					<form:options items="${goodCateGoryList}" itemLabel="name"
						itemValue="id" htmlEscape="false" />
				</form:select></li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary"
				type="submit" value="查询" /></li>
			<li class="btns"><input id="sync" class="btn btn-primary"
				type="button" value="同步" /></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>商品</th>
				<th>价格</th>
				<th>业务类型</th>
				<th>作为套餐里商品的服务项目</th>
				<th>作为单商品购买的服务项目</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="erpGoodInfo">
				<tr>
					<td>${erpGoodInfo.name}</td>
					<td>${erpGoodInfo.price / 100.0}</td>
					<shiro:hasPermission name="good:erpGoodInfo:edit">
						<td>
						<select onchange="updateGoodCateGory(this, ${erpGoodInfo.id});" style="min-width: 90px; width: auto;" <c:if test="${erpGoodInfo.id == 999}">disabled</c:if>>
							<option value="0">无</option>
							<c:forEach items="${goodCateGoryList }" var="goodCategory">
								<option value="${goodCategory.id }" <c:if test="${goodCategory.id == erpGoodInfo.categoryId }">selected</c:if>>${goodCategory.name }</option>
							</c:forEach>
						</select>
						</td>
					</shiro:hasPermission>
					
					<c:choose> 
					    <c:when  test="${fn:length(erpGoodInfo.packageServiceList) != 0}">   
								<td>
									<c:forEach items="${erpGoodInfo.packageServiceList}" var="packageService">
										<p>${packageService.serviceItemName }×${packageService.times }<p>
									</c:forEach>
								</td>
					 	</c:when>      
					    <c:otherwise>  
							<td>/</td>
					  	</c:otherwise> 
					</c:choose>
					
					<c:choose> 
					    <c:when test="${fn:length(erpGoodInfo.singleServiceList) != 0}">   
								<td>
									<c:forEach items="${erpGoodInfo.singleServiceList}" var="singleService">
										<p>${singleService.serviceItemName }×${singleService.times }<p>
									</c:forEach>
								</td>
					 	</c:when>      
					    <c:otherwise>  
							<td>/</td>
					  	</c:otherwise> 
					</c:choose>
					
					<td>
						<c:if test="${erpGoodInfo.id != 999}">
							<a href="${ctx}/good/erpGoodInfo/toEditPage?id=${erpGoodInfo.id}">编辑</a>
						</c:if></a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>