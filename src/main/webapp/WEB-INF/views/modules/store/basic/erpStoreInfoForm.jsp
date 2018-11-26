<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>门店基本信息管理</title>
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
		<li><a href="${ctx}/store/basic/erpStoreInfo/">门店基本信息列表</a></li>
		<li class="active"><a href="${ctx}/store/basic/erpStoreInfo/form?id=${erpStoreInfo.id}">门店基本信息<shiro:hasPermission name="store:basic:erpStoreInfo:edit">${not empty erpStoreInfo.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="store:basic:erpStoreInfo:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="erpStoreInfo" action="${ctx}/store/basic/erpStoreInfo/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">是否是掌贝进件主体,0:否,1:是,默认0：</label>
			<div class="controls">
				<form:input path="isMain" htmlEscape="false" maxlength="4" class="input-xlarge required digits"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">门店简称：</label>
			<div class="controls">
				<form:input path="shortName" htmlEscape="false" maxlength="64" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">门店经营地址：</label>
			<div class="controls">
				<form:input path="address" htmlEscape="false" maxlength="64" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">门店所在城市：</label>
			<div class="controls">
				<form:input path="city" htmlEscape="false" maxlength="64" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">门店电话：</label>
			<div class="controls">
				<form:input path="telephone" htmlEscape="false" maxlength="20" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">公司网址：</label>
			<div class="controls">
				<form:input path="companyUrl" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">产品名称：</label>
			<div class="controls">
				<form:input path="productName" htmlEscape="false" maxlength="128" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">投放产品具体信息：</label>
			<div class="controls">
				<form:input path="productConcreteInfo" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">经营类目补充资质：</label>
			<div class="controls">
				<form:hidden id="additionalLicence" path="additionalLicence" htmlEscape="false" maxlength="1024" class="input-xlarge"/>
				<sys:ckfinder input="additionalLicence" type="files" uploadPath="/store/basic/erpStoreInfo" selectMultiple="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">商户类型，0：个体工商商户，1：企业商户，默认0：</label>
			<div class="controls">
				<form:input path="businessType" htmlEscape="false" maxlength="4" class="input-xlarge required digits"/>
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
			<label class="control-label">商户ID：</label>
			<div class="controls">
				<form:input path="shopInfoId" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">法人ID：</label>
			<div class="controls">
				<form:input path="legalPersonId" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">营业资质ID：</label>
			<div class="controls">
				<form:input path="credentialsId" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">微信支付ID：</label>
			<div class="controls">
				<form:input path="weixinPayId" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">银联支付ID：</label>
			<div class="controls">
				<form:input path="unionpayId" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">朋友圈广告主ID：</label>
			<div class="controls">
				<form:input path="advertiserFriendsId" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">微博广告主ID：</label>
			<div class="controls">
				<form:input path="advertiserWeiboId" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">陌陌广告主ID：</label>
			<div class="controls">
				<form:input path="advertiserMomoId" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注信息：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="store:basic:erpStoreInfo:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>