<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>微信支付开通资料管理</title>
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
		<li><a href="${ctx}/store/pay/erpStorePayWeixin/">微信支付开通资料列表</a></li>
		<li class="active"><a href="${ctx}/store/pay/erpStorePayWeixin/form?id=${erpStorePayWeixin.id}">微信支付开通资料<shiro:hasPermission name="store:pay:erpStorePayWeixin:edit">${not empty erpStorePayWeixin.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="store:pay:erpStorePayWeixin:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="erpStorePayWeixin" action="${ctx}/store/pay/erpStorePayWeixin/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">账号类型，0：对公账号，1：法人账号，默认0：</label>
			<div class="controls">
				<form:input path="accountType" htmlEscape="false" maxlength="4" class="input-xlarge required digits"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">开户名称：</label>
			<div class="controls">
				<form:input path="openAccountName" htmlEscape="false" maxlength="64" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">银行卡号：</label>
			<div class="controls">
				<form:input path="creditCardNo" htmlEscape="false" maxlength="64" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">支付银行ID：</label>
			<div class="controls">
				<form:input path="bankId" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">提供公众号账号、密码,0:否,1:是,默认0：</label>
			<div class="controls">
				<form:input path="provideAccountInfo" htmlEscape="false" maxlength="4" class="input-xlarge required digits"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">公众号登录账号：</label>
			<div class="controls">
				<form:input path="publicAccountNo" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">公众号登录密码：</label>
			<div class="controls">
				<form:input path="publicAccountPassword" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">公众号APPID：</label>
			<div class="controls">
				<form:input path="publicAccountAppid" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">审核状态，0：未提交，1：待审核，2：正在审核，3：拒绝，4：通过，默认0：</label>
			<div class="controls">
				<form:input path="auditStatus" htmlEscape="false" maxlength="4" class="input-xlarge required digits"/>
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
			<shiro:hasPermission name="store:pay:erpStorePayWeixin:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>