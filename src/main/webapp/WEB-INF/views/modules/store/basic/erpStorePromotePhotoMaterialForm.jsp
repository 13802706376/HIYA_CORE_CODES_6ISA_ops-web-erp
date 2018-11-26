<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>推广图片素材管理</title>
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
		<li><a href="${ctx}/store/basic/erpStorePromotePhotoMaterial/">推广图片素材列表</a></li>
		<li class="active"><a href="${ctx}/store/basic/erpStorePromotePhotoMaterial/form?id=${erpStorePromotePhotoMaterial.id}">推广图片素材<shiro:hasPermission name="store:basic:erpStorePromotePhotoMaterial:edit">${not empty erpStorePromotePhotoMaterial.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="store:basic:erpStorePromotePhotoMaterial:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="erpStorePromotePhotoMaterial" action="${ctx}/store/basic/erpStorePromotePhotoMaterial/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">审核状态，0：未提交，1：待审核，2：正在审核，3：拒绝，4：通过，默认0：</label>
			<div class="controls">
				<form:input path="auditStatus" htmlEscape="false" maxlength="4" class="input-xlarge required digits"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">门店环境图：</label>
			<div class="controls">
				<form:hidden id="environmentPhoto" path="environmentPhoto" htmlEscape="false" maxlength="2048" class="input-xlarge"/>
				<sys:ckfinder input="environmentPhoto" type="files" uploadPath="/store/basic/erpStorePromotePhotoMaterial" selectMultiple="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">产品图：</label>
			<div class="controls">
				<form:hidden id="productPhoto" path="productPhoto" htmlEscape="false" maxlength="2048" class="input-xlarge"/>
				<sys:ckfinder input="productPhoto" type="files" uploadPath="/store/basic/erpStorePromotePhotoMaterial" selectMultiple="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">门店环境图数量：</label>
			<div class="controls">
				<form:input path="environmentPhotoCount" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">产品图数量：</label>
			<div class="controls">
				<form:input path="productPhotoCount" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">门店信息ID：</label>
			<div class="controls">
				<form:input path="storeInfoId" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注信息：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="store:basic:erpStorePromotePhotoMaterial:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>