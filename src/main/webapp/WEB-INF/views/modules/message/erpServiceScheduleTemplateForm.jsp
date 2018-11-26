<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>服务进度模板表管理</title>
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
		<li><a href="${ctx}/message/erpServiceScheduleTemplate/">服务进度模板表列表</a></li>
		<li class="active"><a href="${ctx}/message/erpServiceScheduleTemplate/form?id=${erpServiceScheduleTemplate.id}">服务进度模板表<shiro:hasPermission name="message:erpServiceScheduleTemplate:edit">${not empty erpServiceScheduleTemplate.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="message:erpServiceScheduleTemplate:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="erpServiceScheduleTemplate" action="${ctx}/message/erpServiceScheduleTemplate/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">服务类型（DeliveryService  交付服务 ，Split 引流推广服务）：</label>
			<div class="controls">
				<form:input path="serviceType" htmlEscape="false" maxlength="30" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">状态（NoBegin 未开始，Begin 开始，End 结束）：</label>
			<div class="controls">
				<form:input path="status" htmlEscape="false" maxlength="20" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">顺序（数字越小，优先级越高）：</label>
			<div class="controls">
				<form:input path="sort" htmlEscape="false" maxlength="5" class="input-xlarge required digits"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">节点显示内容（json字符串）：</label>
			<div class="controls">
				<form:textarea path="content" htmlEscape="false" rows="4" maxlength="2000" class="input-xxlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">跳转id：</label>
			<div class="controls">
				<form:input path="linkId" htmlEscape="false" maxlength="64" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">多个任务key值(多个逗号隔开)：</label>
			<div class="controls">
				<form:input path="taskDefinitionKeys" htmlEscape="false" maxlength="300" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">服务角色（多个逗号隔开，开户顾问：accountAdviser 策划专家：PlanningExpert 运营顾问：OperationAdviser）：</label>
			<div class="controls">
				<form:input path="serviceRoles" htmlEscape="false" maxlength="200" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">流程版本号（3.1版本入库  int 301）：</label>
			<div class="controls">
				<form:input path="processVersion" htmlEscape="false" maxlength="5" class="input-xlarge required digits"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="message:erpServiceScheduleTemplate:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>