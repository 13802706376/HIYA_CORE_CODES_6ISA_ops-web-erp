<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>修改密码</title>
	<meta name="decorator" content="default"/>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/user/info">个人信息</a></li>
		<li class="active"><a href="${ctx}/sys/user/modifyPwd">修改密码</a></li>
	</ul><br/>
	<form id="inputForm" modelAttribute="user" action="#" method="post" class="form-horizontal">
		<div class="control-group">
			<label class="control-label">原密码:</label>
			<div class="controls">
				<input id="oldPassword" name="originalPwd" type="password" value="" maxlength="50" minlength="3" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">新密码:</label>
			<div class="controls">
				<input id="newPassword" name="newPwd" type="password" value="" maxlength="50" minlength="3" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">确认新密码:</label>
			<div class="controls">
				<input id="confirmNewPassword" name="confirmPwd" type="password" value="" maxlength="50" minlength="3" class="required" equalTo="#newPassword"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"></label>
			<div id="error-message-show" class="controls" style="color:red;display:none;"></div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="button" value="保 存"/>
		</div>
	</form>
	
	<script src="${ctxStatic}/common/erpshop.js?v=${staticVersion}" type="text/javascript"></script>
	
	<script type="text/javascript">
		$(document).ready(function() {
			$("#oldPassword").focus();
			
			$("#btnSubmit").click(function(){
				var originalPwd = $("#oldPassword").val();
				var newPwd = $("#newPassword").val();
				var confirmPwd = $("#confirmNewPassword").val();
				
				if(!originalPwd || !newPwd || !confirmPwd){
					$("#error-message-show").text('所有信息必须填写!').show();
					return;
				}
				
				if(newPwd !== confirmPwd){
					$("#error-message-show").text('新密码与确认密码不一致，请重新输入!').show();
					return;
				}
				
				$.post('${ctx}/sys/user/modifyAgentUserPwd',{confirmPwd:confirmPwd,newPwd:newPwd,originalPwd:originalPwd},function(res){ 
					if(res.code === '0'){
						popup.tip('修改成功!', "success");
					}else{
						popup.tip(res.message, "error");
					}
				},'json');
			});
		});
	</script>
</body>
</html>