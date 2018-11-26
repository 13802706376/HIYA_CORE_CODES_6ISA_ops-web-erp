<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>朋友圈广告主开通资料管理</title>
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
		<li><a href="${ctx}/store/advertiser/erpStoreAdvertiserFriends/">朋友圈广告主开通资料列表</a></li>
		<li class="active"><a href="${ctx}/store/advertiser/erpStoreAdvertiserFriends/form?id=${erpStoreAdvertiserFriends.id}">朋友圈广告主开通资料<shiro:hasPermission name="store:advertiser:erpStoreAdvertiserFriends:edit">${not empty erpStoreAdvertiserFriends.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="store:advertiser:erpStoreAdvertiserFriends:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="erpStoreAdvertiserFriends" action="${ctx}/store/advertiser/erpStoreAdvertiserFriends/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
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
				<form:input path="accountNo" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">公众号登录密码：</label>
			<div class="controls">
				<form:input path="accountPassword" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">公众号原始ID：</label>
			<div class="controls">
				<form:input path="accountOriginalId" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">广告主开通截图：</label>
			<div class="controls">
				<form:hidden id="advertiserScreenshot" path="advertiserScreenshot" htmlEscape="false" maxlength="255" class="input-xlarge"/>
				<sys:ckfinder input="advertiserScreenshot" type="files" uploadPath="/store/advertiser/erpStoreAdvertiserFriends" selectMultiple="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">门店开通截图：</label>
			<div class="controls">
				<form:hidden id="storeScreenshot" path="storeScreenshot" htmlEscape="false" maxlength="255" class="input-xlarge"/>
				<sys:ckfinder input="storeScreenshot" type="files" uploadPath="/store/advertiser/erpStoreAdvertiserFriends" selectMultiple="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注信息：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="store:advertiser:erpStoreAdvertiserFriends:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>