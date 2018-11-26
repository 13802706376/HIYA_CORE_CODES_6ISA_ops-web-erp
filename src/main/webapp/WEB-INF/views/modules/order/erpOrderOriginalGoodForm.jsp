<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>订单商品管理</title>
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
		<li><a href="${ctx}/order/erpOrderOriginalGood/">订单商品列表</a></li>
		<li class="active"><a href="${ctx}/order/erpOrderOriginalGood/form?id=${erpOrderOriginalGood.id}">订单商品<shiro:hasPermission name="order:erpOrderOriginalGood:edit">${not empty erpOrderOriginalGood.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="order:erpOrderOriginalGood:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="erpOrderOriginalGood" action="${ctx}/order/erpOrderOriginalGood/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">所属订单id：</label>
			<div class="controls">
				<form:input path="orderId" htmlEscape="false" maxlength="20" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">商品名称：</label>
			<div class="controls">
				<form:input path="goodName" htmlEscape="false" maxlength="50" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">商品类型id：</label>
			<div class="controls">
				<form:input path="goodTypeId" htmlEscape="false" maxlength="32" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">商品类型名称：</label>
			<div class="controls">
				<form:input path="goodTypeName" htmlEscape="false" maxlength="50" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">预计价格(单位：分)：</label>
			<div class="controls">
				<form:input path="prePrice" htmlEscape="false" maxlength="30" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">实际价格(单位：分)：</label>
			<div class="controls">
				<form:input path="realPrice" htmlEscape="false" maxlength="30" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">商品总共数量：</label>
			<div class="controls">
				<form:input path="num" htmlEscape="false" maxlength="11" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">处理中的商品数量：</label>
			<div class="controls">
				<form:input path="processNum" htmlEscape="false" maxlength="11" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">待处理的商品数量：</label>
			<div class="controls">
				<form:input path="pendingNum" htmlEscape="false" maxlength="11" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">已完成的商品数量：</label>
			<div class="controls">
				<form:input path="finishNum" htmlEscape="false" maxlength="11" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注：</label>
			<div class="controls">
				<form:input path="remark" htmlEscape="false" maxlength="256" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">排序字段：</label>
			<div class="controls">
				<form:input path="sort" htmlEscape="false" maxlength="20" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="order:erpOrderOriginalGood:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>