<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>商户管理管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/shop/erpShopInfo/">商户管理列表</a></li>
		<li class="active"><a href="${ctx}/shop/erpShopInfo/form?id=${erpShopInfo.id}">商户管理<shiro:hasPermission name="shop:erpShopInfo:edit">${not empty erpShopInfo.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="shop:erpShopInfo:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="erpShopInfo" action="${ctx}/shop/erpShopInfo/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">商户编号：</label>
			<div class="controls">
				<label>${erpShopInfo.number }</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">掌贝账号：</label>
			<div class="controls">
				<label>${erpShopInfo.zhangbeiId }</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">商户来源：</label>
			<div class="controls">
				<label>
					<c:if test="${erpShopInfo.source=='0'}">OEM同步</c:if>
					<c:if test="${erpShopInfo.source=='1'}">ERP新增</c:if>
				</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">商户名称：</label>
			<div class="controls">
				<label>${erpShopInfo.name }</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">商户简称：</label>
			<div class="controls">
				<label>${erpShopInfo.abbreviation }</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">行业类型：</label>
			<div class="controls">
				<label>${erpShopInfo.industryType }</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">商户地址：</label>
			<div class="controls">
				<label>${erpShopInfo.address }</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">联系邮箱：</label>
			<div class="controls">
				<label>${erpShopInfo.contactEmail }</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">商户联系人：</label>
			<div class="controls">
				<label>${erpShopInfo.contactName }</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">联系电话：</label>
			<div class="controls">
				<label>${erpShopInfo.contactPhone }</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">推广通道资质：</label>
			<div class="controls">
				<c:forEach items="${extensionList }" var="extension">
					<input type="checkbox" disabled <c:if test="${extension.hasPermission == true }">checked</c:if>>
					${extension.label }
				</c:forEach>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">支付通道资质：</label>
			<div class="controls">
				<c:forEach items="${payList }" var="pay">
					<input type="checkbox" disabled <c:if test="${pay.hasPermission == true }">checked</c:if>>
					${pay.label }
				</c:forEach>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>