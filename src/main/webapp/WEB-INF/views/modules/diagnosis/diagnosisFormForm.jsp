<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
	<title>经营诊断营销策划表单管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
        $(document).ready(function () {
            //$("#name").focus();
            $("#inputForm").validate({
                submitHandler: function (form) {
                    loading('正在提交，请稍等...');
                    form.submit();
                },
                errorContainer: "#messageBox",
                errorPlacement: function (error, element) {
                    $("#messageBox").text("输入有误，请先更正。");
                    if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
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
	<li><a href="${ctx}/diagnosis/diagnosisForm/">经营诊断营销策划表单列表</a></li>
	<li class="active"><a
			href="${ctx}/diagnosis/diagnosisForm/form?id=${diagnosisForm.id}">经营诊断营销策划表单<shiro:hasPermission
			name="diagnosis:diagnosisForm:edit">${not empty diagnosisForm.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission
			name="diagnosis:diagnosisForm:edit">查看</shiro:lacksPermission></a></li>
</ul>
<br/>
<form:form id="inputForm" modelAttribute="diagnosisForm" action="${ctx}/diagnosis/diagnosisForm/save" method="post"
		   class="form-horizontal">
	<form:hidden path="id"/>
	<sys:message content="${message}"/>
	<div class="control-group">
		<label class="control-label">分单ID：</label>
		<div class="controls">
			<form:input path="splitId" htmlEscape="false" maxlength="64" class="input-xlarge required"/>
			<span class="help-inline"><font color="red">*</font> </span>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">套餐其他信息补充：</label>
		<div class="controls">
			<form:input path="packageAdditional" htmlEscape="false" maxlength="512" class="input-xlarge "/>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">商户产品／服务信息了解（大众点评）：</label>
		<div class="controls">
			<form:input path="serviceKnow" htmlEscape="false" maxlength="64" class="input-xlarge "/>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">本次推广联系人：</label>
		<div class="controls">
			<form:input path="contactPerson" htmlEscape="false" maxlength="30" class="input-xlarge "/>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">本次推广联系人电话：</label>
		<div class="controls">
			<form:input path="contactPhone" htmlEscape="false" maxlength="20" class="input-xlarge "/>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">主打产品/服务特色：</label>
		<div class="controls">
			<form:input path="majorProduct" htmlEscape="false" maxlength="1000" class="input-xlarge "/>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">本次推广的产品/服务特色：</label>
		<div class="controls">
			<form:input path="promoteProduct" htmlEscape="false" maxlength="1000" class="input-xlarge "/>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">活动主题/推广需求，多个以半角逗号隔开：</label>
		<div class="controls">
			<form:input path="activityRequirements" htmlEscape="false" maxlength="100" class="input-xlarge required"/>
			<span class="help-inline"><font color="red">*</font> </span>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">活动目的，多个以半角逗号隔开：</label>
		<div class="controls">
			<form:input path="activityGoal" htmlEscape="false" maxlength="255" class="input-xlarge required"/>
			<span class="help-inline"><font color="red">*</font> </span>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">品牌文化亮点：</label>
		<div class="controls">
			<form:input path="brandLightspot" htmlEscape="false" maxlength="1000" class="input-xlarge required"/>
			<span class="help-inline"><font color="red">*</font> </span>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">创意文化：</label>
		<div class="controls">
			<form:input path="originalityCulture" htmlEscape="false" maxlength="1000" class="input-xlarge required"/>
			<span class="help-inline"><font color="red">*</font> </span>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">电话后补充的诊断内容：</label>
		<div class="controls">
			<form:input path="diagnosisContentAdditional" htmlEscape="false" maxlength="1000"
						class="input-xlarge required"/>
			<span class="help-inline"><font color="red">*</font> </span>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">参考推文或材料：</label>
		<div class="controls">
			<form:input path="referenceMaterial" htmlEscape="false" maxlength="1000" class="input-xlarge required"/>
			<span class="help-inline"><font color="red">*</font> </span>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">主推：</label>
		<div class="controls">
			<form:input path="mainPush" htmlEscape="false" maxlength="255" class="input-xlarge required"/>
			<span class="help-inline"><font color="red">*</font> </span>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">备选1：</label>
		<div class="controls">
			<form:input path="backupFirst" htmlEscape="false" maxlength="255" class="input-xlarge required"/>
			<span class="help-inline"><font color="red">*</font> </span>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">备选2：</label>
		<div class="controls">
			<form:input path="backupSecond" htmlEscape="false" maxlength="255" class="input-xlarge required"/>
			<span class="help-inline"><font color="red">*</font> </span>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">投放地域：</label>
		<div class="controls">
			<form:input path="pushArea" htmlEscape="false" maxlength="255" class="input-xlarge required"/>
			<span class="help-inline"><font color="red">*</font> </span>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">掌贝后台账号：</label>
		<div class="controls">
			<form:input path="shopUsername" htmlEscape="false" maxlength="45" class="input-xlarge required"/>
			<span class="help-inline"><font color="red">*</font> </span>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">掌贝后台密码：</label>
		<div class="controls">
			<form:input path="shopPassword" htmlEscape="false" maxlength="255" class="input-xlarge required"/>
			<span class="help-inline"><font color="red">*</font> </span>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">第一层重点宣传文案：</label>
		<div class="controls">
			<form:input path="firstPropagandaContent" htmlEscape="false" maxlength="1000"
						class="input-xlarge required"/>
			<span class="help-inline"><font color="red">*</font> </span>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">第二层重点宣传文案：</label>
		<div class="controls">
			<form:input path="secondPropagandaContent" htmlEscape="false" maxlength="1000"
						class="input-xlarge required"/>
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
		<shiro:hasPermission name="diagnosis:diagnosisForm:edit"><input id="btnSubmit" class="btn btn-primary"
																		type="submit"
																		value="保 存"/>&nbsp;</shiro:hasPermission>
		<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
	</div>
</form:form>
</body>
</html>
