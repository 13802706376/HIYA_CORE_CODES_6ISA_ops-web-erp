<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>保存&ldquo;第一层重点宣传图片&rdquo;成功管理</title>
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
		<li><a href="${ctx}/diagnosis/diagnosisFirstAdImage/">保存&ldquo;第一层重点宣传图片&rdquo;成功列表</a></li>
		<li class="active"><a href="${ctx}/diagnosis/diagnosisFirstAdImage/form?id=${diagnosisFirstAdImage.id}">保存&ldquo;第一层重点宣传图片&rdquo;成功<shiro:hasPermission name="diagnosis:diagnosisFirstAdImage:edit">${not empty diagnosisFirstAdImage.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="diagnosis:diagnosisFirstAdImage:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="diagnosisFirstAdImage" action="${ctx}/diagnosis/diagnosisFirstAdImage/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">分单ID：</label>
			<div class="controls">
				<form:input path="splitId" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">类型，1：微信朋友圈，2：新浪微博，3：陌陌，second_adv：第二层广告图：</label>
			<div class="controls">
				<form:input path="type" htmlEscape="false" maxlength="10" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">图片路径：</label>
			<div class="controls">
				<form:input path="imgUrl" htmlEscape="false" maxlength="512" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注信息：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="diagnosis:diagnosisFirstAdImage:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>