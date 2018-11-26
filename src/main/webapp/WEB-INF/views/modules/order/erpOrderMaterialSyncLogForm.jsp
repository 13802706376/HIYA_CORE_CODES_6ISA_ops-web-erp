<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>订单物料同步日志管理</title>
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
		<li><a href="${ctx}/order/erpOrderMaterialSyncLog/">订单物料同步日志列表</a></li>
		<li class="active"><a href="${ctx}/order/erpOrderMaterialSyncLog/form?id=${erpOrderMaterialSyncLog.id}">订单物料同步日志<shiro:hasPermission name="order:erpOrderMaterialSyncLog:edit">${not empty erpOrderMaterialSyncLog.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="order:erpOrderMaterialSyncLog:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="erpOrderMaterialSyncLog" action="${ctx}/order/erpOrderMaterialSyncLog/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">物料服务订单号：</label>
			<div class="controls">
				<form:input path="materialServiceOrderNo" htmlEscape="false" maxlength="50" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">贝虎订单号：</label>
			<div class="controls">
				<form:input path="orderNo" htmlEscape="false" maxlength="50" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">掌贝id：</label>
			<div class="controls">
				<form:input path="zhangbeiId" htmlEscape="false" maxlength="20" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">订单类别。First：首次，Update：更新：</label>
			<div class="controls">
				<form:input path="orderType" htmlEscape="false" maxlength="20" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">同步时间：</label>
			<div class="controls">
				<input name="syncDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${erpOrderMaterialSyncLog.syncDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">恢复正常时间：</label>
			<div class="controls">
				<input name="recoverDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${erpOrderMaterialSyncLog.recoverDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">同步情况。Normal: 正常；NoOrderNo: 没有订单号；NoZhangbeiId: 没有掌贝账号；NoFirstMarketingPlanService: 没有首次营销策划流程；SyncMaterialStatusFailed: 物料制作状态同步失败：</label>
			<div class="controls">
				<form:input path="syncStatus" htmlEscape="false" maxlength="30" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">同步情况中文名称。见 sync_status 字段说明：</label>
			<div class="controls">
				<form:input path="syncStatusName" htmlEscape="false" maxlength="30" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注信息：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="order:erpOrderMaterialSyncLog:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>