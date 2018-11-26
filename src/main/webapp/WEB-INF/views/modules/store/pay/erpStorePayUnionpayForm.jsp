<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>银联支付开通资料管理</title>
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
		<li><a href="${ctx}/store/pay/erpStorePayUnionpay/">银联支付开通资料列表</a></li>
		<li class="active"><a href="${ctx}/store/pay/erpStorePayUnionpay/form?id=${erpStorePayUnionpay.id}">银联支付开通资料<shiro:hasPermission name="store:pay:erpStorePayUnionpay:edit">${not empty erpStorePayUnionpay.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="store:pay:erpStorePayUnionpay:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="erpStorePayUnionpay" action="${ctx}/store/pay/erpStorePayUnionpay/save" method="post" class="form-horizontal">
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
			<label class="control-label">审核状态，0：未提交，1：待审核，2：正在审核，3：拒绝，4：通过，默认0：</label>
			<div class="controls">
				<form:input path="auditStatus" htmlEscape="false" maxlength="4" class="input-xlarge required digits"/>
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
			<label class="control-label">当前银联账号绑定掌贝设备数量：</label>
			<div class="controls">
				<form:input path="zhangbeiBindCount" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">开户许可证或银联印鉴证：</label>
			<div class="controls">
				<form:hidden id="openAccountLicence" path="openAccountLicence" htmlEscape="false" maxlength="255" class="input-xlarge"/>
				<sys:ckfinder input="openAccountLicence" type="files" uploadPath="/store/pay/erpStorePayUnionpay" selectMultiple="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">法人银行卡正面照：</label>
			<div class="controls">
				<form:hidden id="creditCardFrontPhoto" path="creditCardFrontPhoto" htmlEscape="false" maxlength="255" class="input-xlarge"/>
				<sys:ckfinder input="creditCardFrontPhoto" type="files" uploadPath="/store/pay/erpStorePayUnionpay" selectMultiple="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">法人银行卡反面照：</label>
			<div class="controls">
				<form:hidden id="creditCardReversePhoto" path="creditCardReversePhoto" htmlEscape="false" maxlength="255" class="input-xlarge"/>
				<sys:ckfinder input="creditCardReversePhoto" type="files" uploadPath="/store/pay/erpStorePayUnionpay" selectMultiple="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">结算账号授权委托书：</label>
			<div class="controls">
				<form:hidden id="authorizeProxy" path="authorizeProxy" htmlEscape="false" maxlength="255" class="input-xlarge"/>
				<sys:ckfinder input="authorizeProxy" type="files" uploadPath="/store/pay/erpStorePayUnionpay" selectMultiple="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">银联支付三联单：</label>
			<div class="controls">
				<form:hidden id="lianDan" path="lianDan" htmlEscape="false" maxlength="512" class="input-xlarge"/>
				<sys:ckfinder input="lianDan" type="files" uploadPath="/store/pay/erpStorePayUnionpay" selectMultiple="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">门头照：</label>
			<div class="controls">
				<form:hidden id="storePhotoDoorHead" path="storePhotoDoorHead" htmlEscape="false" maxlength="255" class="input-xlarge"/>
				<sys:ckfinder input="storePhotoDoorHead" type="files" uploadPath="/store/pay/erpStorePayUnionpay" selectMultiple="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">收银台照：</label>
			<div class="controls">
				<form:hidden id="storePhotoCashierDesk" path="storePhotoCashierDesk" htmlEscape="false" maxlength="255" class="input-xlarge"/>
				<sys:ckfinder input="storePhotoCashierDesk" type="files" uploadPath="/store/pay/erpStorePayUnionpay" selectMultiple="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">店内环境照：</label>
			<div class="controls">
				<form:hidden id="storePhotoEnvironment" path="storePhotoEnvironment" htmlEscape="false" maxlength="255" class="input-xlarge"/>
				<sys:ckfinder input="storePhotoEnvironment" type="files" uploadPath="/store/pay/erpStorePayUnionpay" selectMultiple="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">补充资料：</label>
			<div class="controls">
				<form:hidden id="additionalPhoto" path="additionalPhoto" htmlEscape="false" maxlength="255" class="input-xlarge"/>
				<sys:ckfinder input="additionalPhoto" type="files" uploadPath="/store/pay/erpStorePayUnionpay" selectMultiple="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注信息：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="store:pay:erpStorePayUnionpay:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>