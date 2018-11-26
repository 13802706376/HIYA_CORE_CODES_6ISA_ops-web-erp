<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>团队管理</title>
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
		<li><a href="${ctx}/team/erpTeam/">团队列表</a></li>
		<li class="active"><a href="${ctx}/team/erpTeam/form?id=${erpTeam.id}">团队<shiro:hasPermission name="team:erpTeam:edit">${not empty erpTeam.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="team:erpTeam:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="erpTeam" action="${ctx}/team/erpTeam/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">团队名称：</label>
			<div class="controls">
				<form:input path="teamName" htmlEscape="false" maxlength="100" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">团队类别:</label>
			<div class="controls">
				<form:select path="agentId" class="input-medium">
					<form:option value="-1">总部</form:option>
					<c:forEach items="${branches}" var="branch">
						<form:option value="${branch.serviceNo}">${branch.companyName}</form:option>
					</c:forEach>
				</form:select>
				<span class="help-inline"></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">团队管理员：</label>
			<div class="controls">
				<sys:treeselect id="teamLeader" name="teamLeaderIds" value="${leaderIds }" labelName="teamLeader.ids"
								labelValue="${leaderName }"
								title="团队管理员" url="/team/erpTeam/treeData" cssClass="" allowClear="true"
								checked="true"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">团队成员：</label>
			<div class="controls">
			<sys:treeselect id="teamMember" name="teamMemberIds" value="${memberIds }" labelName="teamMember.ids" labelValue="${memberName }"
					title="团队管理员" url="/team/erpTeam/treeData" cssClass="" allowClear="true" checked="true"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="team:erpTeam:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
