<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>法人信息管理</title>
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
		<li><a href="${ctx}/store/basic/erpStoreLegalPerson/">法人信息列表</a></li>
		<li class="active"><a href="${ctx}/store/basic/erpStoreLegalPerson/form?id=${erpStoreLegalPerson.id}">法人信息<shiro:hasPermission name="store:basic:erpStoreLegalPerson:edit">${not empty erpStoreLegalPerson.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="store:basic:erpStoreLegalPerson:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="erpStoreLegalPerson" action="${ctx}/store/basic/erpStoreLegalPerson/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">姓名：</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">身份证号码：</label>
			<div class="controls">
				<form:input path="idCardNo" htmlEscape="false" maxlength="18" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">身份证有效期起始日期：</label>
			<div class="controls">
				<input name="idCardStartDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${erpStoreLegalPerson.idCardStartDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">身份证有效期结束日期：</label>
			<div class="controls">
				<input name="idCardEndDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${erpStoreLegalPerson.idCardEndDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">法人身份证正面照：</label>
			<div class="controls">
				<form:hidden id="idCardFrontPhoto" path="idCardFrontPhoto" htmlEscape="false" maxlength="255" class="input-xlarge"/>
				<sys:ckfinder input="idCardFrontPhoto" type="files" uploadPath="/store/basic/erpStoreLegalPerson" selectMultiple="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">法人身份证反面照：</label>
			<div class="controls">
				<form:hidden id="idCardReversePhoto" path="idCardReversePhoto" htmlEscape="false" maxlength="255" class="input-xlarge"/>
				<sys:ckfinder input="idCardReversePhoto" type="files" uploadPath="/store/basic/erpStoreLegalPerson" selectMultiple="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">法人手持身份证照：</label>
			<div class="controls">
				<form:hidden id="idCardInHandPhoto" path="idCardInHandPhoto" htmlEscape="false" maxlength="255" class="input-xlarge"/>
				<sys:ckfinder input="idCardInHandPhoto" type="files" uploadPath="/store/basic/erpStoreLegalPerson" selectMultiple="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注信息：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="store:basic:erpStoreLegalPerson:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>