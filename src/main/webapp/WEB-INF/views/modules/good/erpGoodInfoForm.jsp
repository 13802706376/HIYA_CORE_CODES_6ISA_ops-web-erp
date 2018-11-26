<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>商品信息管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(
			function() {
				$("#name").focus();
				$("#inputForm")
						.validate(
								{
									submitHandler : function(form) {
										loading('正在提交，请稍等...');
										form.submit();
									},
									errorContainer : "#messageBox",
									errorPlacement : function(error, element) {
										$("#messageBox").text("输入有误，请先更正。");
										if (element.is(":checkbox")
												|| element.is(":radio")
												|| element.parent().is(
														".input-append")) {
											error.appendTo(element.parent()
													.parent());
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
		<li><a href="${ctx}/good/erpGoodInfo/">商品信息管理</a></li>
		<li class="active"><a
			href="${ctx}/good/erpGoodInfo/form?id=${erpGoodInfo.id}">商品信息<shiro:hasPermission
					name="good:erpGoodInfo:edit">${not empty erpGoodInfo.id?'修改':'添加'}</shiro:hasPermission>
				<shiro:hasPermission name="good:erpGoodInfo:edit">
					<li><a href="${ctx}/good/category/list">商品类型管理</a></li>
				</shiro:hasPermission> <shiro:hasPermission name="good.category:erpGoodCategory:edit">
					<li><a href="${ctx}/good/category/form?sort=10">商品分类添加</a>
					</li>
				</shiro:hasPermission>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="erpGoodInfo"
		action="${ctx}/good/erpGoodInfo/save" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<div class="control-group">
			<label class="control-label">商品名称：</label>
			<div class="controls">
				<label>${erpGoodInfo.name }</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">商品价格：</label>
			<div class="controls">
				<label>${erpGoodInfo.price  / 100.0}</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">商品类型：</label>
			<div class="controls">
				<form:select id="categoryId" path="categoryId" class="input-medium">
					<form:options items="${goodCateGoryList}" itemLabel="name"
						itemValue="id" htmlEscape="false" />
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">排序字段：</label>
			<div class="controls">
				<form:input path="sort" htmlEscape="false" class="input-xlarge  digits" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4"
					maxlength="256" class="input-xxlarge " />
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="good:erpGoodInfo:edit">
				<input id="btnSubmit" class="btn btn-primary" type="submit"
					value="保 存" />&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回"
				onclick="history.go(-1)" />
		</div>
	</form:form>
</body>
</html>