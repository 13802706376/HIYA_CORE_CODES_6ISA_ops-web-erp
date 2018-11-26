<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>商品分类管理</title>
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
		<shiro:hasPermission name="good:erpGoodInfo:view">
			<li><a href="${ctx}/good/erpGoodInfo/">商品服务管理</a></li>
		</shiro:hasPermission>
		<li><a href="${ctx}/good/category/">商品类型管理</a></li>
		<li class="active"><a
			href="${ctx}/good/category/form?id=${erpGoodCategory.id}">商品类型<shiro:hasPermission
					name="good.category:erpGoodCategory:edit">${not empty erpGoodCategory.id?'修改':'添加'}</shiro:hasPermission>
				<shiro:lacksPermission name="good.category:erpGoodCategory:edit">查看</shiro:lacksPermission></a></li>
				
		<li><a href="${ctx}/good/erpGoodServiceItem/list">服务项目管理</a></li>
		
		<li><a href="${ctx}/good/erpGoodServiceItem/form?sort=10">服务项目添加</a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="erpGoodCategory"
		action="${ctx}/good/category/save" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<div class="control-group">
			<label class="control-label">商品类型名称：</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="64"
					class="input-xlarge required" />
				<font color="red">*</font> </span>
			</div>
		</div>
		<!-- 
		<div class="control-group">
			<label class="control-label">商品类型编码：</label>
			<div class="controls">
				<form:input path="code" htmlEscape="false" maxlength="32"
					class="input-xlarge " />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">可编辑标记：</label>
			<div class="controls">
				<form:input path="readonly" htmlEscape="false" maxlength="1"
					class="input-xlarge " />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注：</label>
			<div class="controls">
				<form:input path="remark" htmlEscape="false" maxlength="256"
					class="input-xlarge " />
			</div>
		</div>
		 -->
		<div class="control-group">
			<label class="control-label">是否允许修改/删除:</label>
			<div class="controls">
				<form:select path="readonly">
					<form:options items="${fns:getDictList('good_category_flag')}"
						itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
				<span class="help-inline"><font color="red">*</font>
					“不可编辑”代表新增后不可修改，“可编辑”则表示修改后可删除</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">排序字段：</label>
			<div class="controls">
				<form:input path="sort" htmlEscape="false" maxlength="20"
					class="input-xlarge  digits" />
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="good.category:erpGoodCategory:edit">
				<input id="btnSubmit" class="btn btn-primary" type="submit"
					value="保 存" />&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回"
				onclick="history.go(-1)" />
		</div>
	</form:form>
</body>
</html>