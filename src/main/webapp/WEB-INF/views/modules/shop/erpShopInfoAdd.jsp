<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>商户管理</title>
	<meta name="decorator" content="default"/>
	<style type="text/css">
		.number-wrapper{ position: absolute; left: 660px; top: 4px; width: 320px; background-color: #fff; padding: 0px 0px 20px; }
		.number-wrapper .form-horizontal{ margin: 0px; }
		.number-wrapper .form-horizontal .control-group .control-label{ width: 170px; text-align: left;}
		.number-wrapper .form-horizontal .control-group .controls{ width: 130px; }
		.number-wrapper .form-horizontal .control-group .controls .products-num input{ width: 35px; }
	</style>
	
	<script type="text/javascript">
		$(function() {
			var shopForm = {
				init: function() {
					$("#inputForm").validate({
						submitHandler: function(form){
							$('#btnSubmit').attr('disabled', true);
							var zhangbeiId = $.trim($('#zhangbeiId').val()),
								contactName = $.trim($('#contactName').val()),
								contactPhone = $.trim($('#contactPhone').val()),
								contactEmail = $.trim($('#contactEmail').val()),
								address = $.trim($("#address").val()),
								fullname = $.trim($('#fullname').val()),
								abbreviation = $.trim($('#abbreviation').val());
							
							setTimeout(function() {
								loading('正在提交，请稍等...');
								form.submit();
							}, 20);
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
				}
			};
	
			shopForm.init();
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="javascript:history.back()" class="comebacka">返回</a></li>
		<li class="active">
			<a href="${ctx}/shop/erpShopInfo/toAdd">商户新增</a>
		</li>
	</ul>
	<sys:message content="${message}" />
	<div class="formwrapper positionrelative">
		<form:form id="inputForm" modelAttribute="info" action="${ctx}/shop/erpShopInfo/add" method="post" class="form-horizontal">
			<form:hidden path="orderId"/>
			<div class="control-group">
				<label class="control-label">商户账号:</label>
				<div class="controls">
					<form:input id="zhangbeiId" path="zhangbeiId" htmlEscape="false" maxlength="11" class="required input-xlarge"/>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">联系人:</label>
				<div class="controls">
					<form:input id="contactName" path="contactName" htmlEscape="false" maxlength="50" class="required input-xlarge"/>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">联系人电话:</label>
				<div class="controls">
					<form:input id="contactPhone" path="contactPhone" htmlEscape="false" maxlength="11" class="required input-xlarge"/>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">联系邮箱:</label>
				<div class="controls">
					<form:input id="contactEmail" path="contactEmail" htmlEscape="false" maxlength="50" class="required input-xlarge"/>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">商户地址:</label>
				<div class="controls">
					<form:input id="address" path="address" htmlEscape="false" maxlength="100" class="required input-xlarge"/>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">商户全称:</label>
				<div class="controls">
					<form:input id="fullname" path="name" htmlEscape="false" maxlength="50" class="required input-xlarge"/>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label">商户简称:</label>
				<div class="controls">
					<form:input id="abbreviation" path="abbreviation" htmlEscape="false" maxlength="50" class="required input-xlarge"/>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
			</div>
			
			<div class="control-group" style="border-bottom-width: 0px; margin-bottom: 0px;">
				<label class="control-label">&nbsp;</label>
				<div class="controls">
					<div id="messageBox" class="redword"></div>
				</div>
			</div>
			<div class="form-actions">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="确 定"/>&nbsp;
				<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1);"/>
			</div>
		</form:form>
	</div>
</body>
</html>