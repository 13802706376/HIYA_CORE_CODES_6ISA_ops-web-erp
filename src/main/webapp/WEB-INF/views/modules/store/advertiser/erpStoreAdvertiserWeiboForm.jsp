<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>微博广告主开通资料管理</title>
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
		<li><a href="${ctx}/store/advertiser/erpStoreAdvertiserWeibo/">微博广告主开通资料列表</a></li>
		<li class="active"><a href="${ctx}/store/advertiser/erpStoreAdvertiserWeibo/form?id=${erpStoreAdvertiserWeibo.id}">微博广告主开通资料<shiro:hasPermission name="store:advertiser:erpStoreAdvertiserWeibo:edit">${not empty erpStoreAdvertiserWeibo.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="store:advertiser:erpStoreAdvertiserWeibo:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="erpStoreAdvertiserWeibo" action="${ctx}/store/advertiser/erpStoreAdvertiserWeibo/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">账号类型，0：个人账号，1：企业账号，默认0：</label>
			<div class="controls">
				<form:input path="accountType" htmlEscape="false" maxlength="4" class="input-xlarge required digits"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">微博登录账号：</label>
			<div class="controls">
				<form:input path="accountNo" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">微博登录密码：</label>
			<div class="controls">
				<form:input path="accountPassword" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">微博UID：</label>
			<div class="controls">
				<form:input path="uid" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">微博昵称：</label>
			<div class="controls">
				<form:input path="nickName" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">账号关系证明函：</label>
			<div class="controls">
				<form:hidden id="relationProveLetter" path="relationProveLetter" htmlEscape="false" maxlength="255" class="input-xlarge"/>
				<sys:ckfinder input="relationProveLetter" type="files" uploadPath="/store/advertiser/erpStoreAdvertiserWeibo" selectMultiple="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">广告授权函：</label>
			<div class="controls">
				<form:hidden id="advAuthLetter" path="advAuthLetter" htmlEscape="false" maxlength="255" class="input-xlarge"/>
				<sys:ckfinder input="advAuthLetter" type="files" uploadPath="/store/advertiser/erpStoreAdvertiserWeibo" selectMultiple="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">推广承诺函：</label>
			<div class="controls">
				<form:hidden id="promotePromiseLetter" path="promotePromiseLetter" htmlEscape="false" maxlength="255" class="input-xlarge"/>
				<sys:ckfinder input="promotePromiseLetter" type="files" uploadPath="/store/advertiser/erpStoreAdvertiserWeibo" selectMultiple="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注信息：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="store:advertiser:erpStoreAdvertiserWeibo:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>