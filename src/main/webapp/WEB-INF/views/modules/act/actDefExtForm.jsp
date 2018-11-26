<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>流程节点扩展管理</title>
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
		<li><a href="${ctx}/act/actDefExt/">流程节点扩展列表</a></li>
		<li class="active"><a href="${ctx}/act/actDefExt/form?id=${actDefExt.id}">流程节点扩展<shiro:hasPermission name="act:actDefExt:edit">${not empty actDefExt.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="act:actDefExt:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="actDefExt" action="${ctx}/act/actDefExt/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">流程定义KEY：</label>
			<div class="controls">
				<form:input path="processDefineKey" htmlEscape="false" maxlength="164" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">节点ID：</label>
			<div class="controls">
				<form:input path="actId" htmlEscape="false" maxlength="164" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">角色ID：</label>
			<div class="controls">
				<form:input path="roleId" htmlEscape="false" maxlength="164" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">角色名称：</label>
			<div class="controls">
				<form:input path="roleName" htmlEscape="false" maxlength="300" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">处理人：</label>
			<div class="controls">
				<form:input path="assignee" htmlEscape="false" maxlength="200" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">表单模板：</label>
			<div class="controls">
				<form:input path="formTemplate" htmlEscape="false" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">邮件配置：</label>
			<div class="controls">
				<form:input path="emailTemplate" htmlEscape="false" maxlength="2000" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">回调服务：</label>
			<div class="controls">
				<form:input path="callbackService" htmlEscape="false" maxlength="2000" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注：</label>
			<div class="controls">
				<form:input path="remark" htmlEscape="false" maxlength="2000" class="input-xlarge "/>
			</div>
		</div>
		
		
		
			<div class="control-group">
			<label class="control-label">正常工时：</label>
			<div class="controls">
				<form:input path="taskDateHours" htmlEscape="false" maxlength="2000" class="input-xlarge "/>
			</div>
		</div>
			<div class="control-group">
			<label class="control-label">加急工时：</label>
			<div class="controls">
				<form:input path="urgentTaskDateHours" htmlEscape="false" maxlength="2000" class="input-xlarge "/>
			</div>
		</div>
			<div class="control-group">
			<label class="control-label">指派的角色：</label>
			<div class="controls">
				<form:input path="taskUserRole" htmlEscape="false" maxlength="2000" class="input-xlarge "/>
			</div>
		</div>
			<div class="control-group">
			<label class="control-label">指派的角色2：</label>
			<div class="controls">
				<form:input path="taskUserRole2" htmlEscape="false" maxlength="2000" class="input-xlarge "/>
			</div>
		</div>
		
		
		
		
		<div class="control-group">
			<label class="control-label">修改人：</label>
			<div class="controls">
				<form:input path="updateUser" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
        
        <div class="control-group">
            <label class="control-label">是否是关键任务点：</label>
            <div class="controls">
                <form:select path="isKeyPoint" class="input-medium">
                    <form:option value="Y">是</form:option>
                    <form:option value="N">否</form:option>
                </form:select>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">正常既定消耗工时：</label>
            <div class="controls">
                <form:input path="normalTaskHours" htmlEscape="false" maxlength="64" class="input-xlarge "/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">紧急既定消耗工时：</label>
            <div class="controls">
                <form:input path="urgentTaskHours" htmlEscape="false" maxlength="64" class="input-xlarge "/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">从待生产库激活后正常既定消耗工时：</label>
            <div class="controls">
                <form:input path="activationNormalTaskHours" htmlEscape="false" maxlength="64" class="input-xlarge "/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">从待生产库激活后紧急既定消耗工时：</label>
            <div class="controls">
                <form:input path="activationUrgentTaskHours" htmlEscape="false" maxlength="64" class="input-xlarge "/>
            </div>
        </div>
        
		<div class="form-actions">
			<shiro:hasPermission name="act:actDefExt:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>