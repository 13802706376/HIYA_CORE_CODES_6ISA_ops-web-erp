<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>聚引客分单评论管理</title>
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
		<li><a href="javascript:history.back()" class="comebacka">返回</a></li>
		<li class="active"><a href="${ctx}/order/erpOrderSplitComment/">聚引客评论列表</a></li>
		<shiro:hasPermission name="order:erpOrderSplitComment:edit"><li><a href="${ctx}/order/erpOrderSplitComment/form">聚引客分单评论添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="erpOrderSplitComment" action="${ctx}/order/erpOrderSplitComment/" method="post" class="breadcrumb form-search hide">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	</form:form>
	<c:set var="answer1Items" value="${fn:split('非常满意,满意,一般,不满意', ',') }"></c:set>
	<c:set var="answer2Items" value="${fn:split('购买体验,资料收集服务人员服务态度,推广上线服务人员服务态度,服务人员专业度,沟通反馈及时性', ',') }"></c:set>
	<c:set var="answer3Items" value="${fn:split('一定会,会考虑,不会', ',') }"></c:set>
	
	<div style="padding:10px;">
		<c:forEach items="${page.list}" var="erpOrderSplitComment">
			<div style="padding-bottom: 20px; padding-top: 20px; border-bottom: #cecece solid 1px;">
				<div>
					<h4>综合评价</h4>
					<div style="height: 45px; line-height: 45px;">
						<span style="font-weight: bold; font-size: 20px; color: #e60;">${erpOrderSplitComment.score}分</span>
						<c:forEach begin="1" end="${erpOrderSplitComment.score}">
							<i class="icon-star" style="color: #e60;"></i>
						</c:forEach>
					</div>
				</div>
				<p>1. 您对我们的聚引客服务满意度: <c:out value="${answer1Items[erpOrderSplitComment.answer1]}"/></p>
				<p>2. 您对聚引客服务有哪些不满意的地方?: 
					<c:forTokens items="${erpOrderSplitComment.answer2 }" delims="," var="item" varStatus="status">
						<c:if test="${status.index > 0 }">， </c:if>
						<c:out value="${answer2Items[item]}"></c:out>
					</c:forTokens>
				</p>
				<p>3. 您后续会购买聚引客满足您的推广需求吗?: <c:out value="${answer3Items[erpOrderSplitComment.answer3]}"/></p>
				<p>4. 您还有其他想对我们说的话或建议么?: ${erpOrderSplitComment.answer4}</p>
				<p style="margin-top: 15px;">评分时间: <fmt:formatDate value="${erpOrderSplitComment.createDate}" pattern="yyyy-MM-dd HH:mm"/></p>
			</div>
		</c:forEach>
		
		<div class="pagination">${page}</div>
	</div>
	
</body>
</html>