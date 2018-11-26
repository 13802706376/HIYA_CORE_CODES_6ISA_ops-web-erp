<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>优惠形式配置表管理</title>
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
		<li><a href="${ctx}/diagnosis/diagnosisDiscountTypeConfig/">优惠形式配置表列表</a></li>
		<li class="active"><a href="${ctx}/diagnosis/diagnosisDiscountTypeConfig/form?id=${diagnosisDiscountTypeConfig.id}">优惠形式配置表<shiro:hasPermission name="diagnosis:diagnosisDiscountTypeConfig:edit">${not empty diagnosisDiscountTypeConfig.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="diagnosis:diagnosisDiscountTypeConfig:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="diagnosisDiscountTypeConfig" action="${ctx}/diagnosis/diagnosisDiscountTypeConfig/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">行业属性：</label>
			<div class="controls">
				<form:select path="industryAttributeId" class="input-medium" id="orderNumber">
	                <form:option value="" label="请选择" />
	                <form:options items="${industryAttributes }" itemLabel="label"
	                    itemValue="id" htmlEscape="false" />
	            </form:select>
	            <span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">推广需求：</label>
			<div class="controls">
				<form:select path="activityRequirementId" class="input-medium" id="orderNumber">
                    <form:option value="" label="请选择" />
                    <form:options items="${activityRequirements }" itemLabel="label"
                        itemValue="id" htmlEscape="false" />
                </form:select>
                <span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">优惠形式：</label>
			<div class="controls">
				<form:select path="discountTypeId" class="input-medium" id="orderNumber">
                    <form:option value="" label="请选择" />
                    <form:options items="${discountTypes }" itemLabel="label"
                        itemValue="id" htmlEscape="false" />
                </form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">分值：</label>
			<div class="controls">
				<form:input path="score" htmlEscape="false" maxlength="11" class="input-xlarge required digits"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注信息：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>