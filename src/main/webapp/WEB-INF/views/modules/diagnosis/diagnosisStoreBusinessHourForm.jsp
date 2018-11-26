<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>经营诊断的门店的营业时间管理</title>
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
		<li><a href="${ctx}/diagnosis/diagnosisStoreBusinessHour/">经营诊断的门店的营业时间列表</a></li>
		<li class="active"><a href="${ctx}/diagnosis/diagnosisStoreBusinessHour/form?id=${diagnosisStoreBusinessHour.id}">经营诊断的门店的营业时间<shiro:hasPermission name="diagnosis:diagnosisStoreBusinessHour:edit">${not empty diagnosisStoreBusinessHour.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="diagnosis:diagnosisStoreBusinessHour:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="diagnosisStoreBusinessHour" action="${ctx}/diagnosis/diagnosisStoreBusinessHour/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">门店ID：</label>
			<div class="controls">
				<form:input path="storeInfoId" htmlEscape="false" maxlength="64" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">工作日（例：周一，周日 =》 ,1,7,）：</label>
			<div class="controls">
				<form:input path="workdays" htmlEscape="false" maxlength="20" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">开始时间（单位秒，例：10:30 =》 10*60*60+30*60）：</label>
			<div class="controls">
				<form:input path="startTime" htmlEscape="false" maxlength="11" class="input-xlarge required digits"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">结束时间（单位秒【结束时间不一定大于开始时间】，例：10:30 =》 10*60*60+30*60）：</label>
			<div class="controls">
				<form:input path="endTime" htmlEscape="false" maxlength="11" class="input-xlarge required digits"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">营业时间类型（normal: 正常营业时间；peak: 高峰营业时间）：</label>
			<div class="controls">
				<form:input path="businessType" htmlEscape="false" maxlength="20" class="input-xlarge required"/>
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
			<shiro:hasPermission name="diagnosis:diagnosisStoreBusinessHour:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>