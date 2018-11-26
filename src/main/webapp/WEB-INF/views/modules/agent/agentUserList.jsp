<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="author" content="http://www.yunnex.com/">
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta http-equiv="Expires" content="0">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Cache-Control" content="no-store">
<title>员工信息管理</title>
<link href="${ctxStatic}/bootstrap/2.3.1/css_cerulean/bootstrap.min.css"
	rel="stylesheet" type="text/css" />
<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.min.css"
	rel="stylesheet" type="text/css" />
<link href="${ctxStatic}/common/jeesite.css?v=${staticVersion}"
	rel="stylesheet" type="text/css" />
<link href="${ctxStatic}/common/shopentry.min.css?v=${staticVersion}"
	rel="stylesheet" type="text/css" />
<link href="${ctxStatic}/common/statistics.css?v=${staticVersion}"
	rel="stylesheet" type="text/css" />
<link
	href="${ctxStatic}/css/serviceprovider/staffInfos.min.css?v=${staticVersion}"
	rel="stylesheet" type="text/css" />
<script type="text/javascript">
		var ctx = '${ctx}', ctxStatic='${ctxStatic}';
		var pageDataObject = {
				   uploadfileurl: "${fileUrl}",
				   userid: "${fns:getUser().id}",
            username: "${fns:getUser().name}",
            /* 新增员工。带了agentId过来，或者是服务商用户，就可以根据配置的权限确定能否新增员工。 */
            <c:choose>
            	<c:when test="${from == 'agent' or fns:getUser().type == 'agent'}">
	            	createAgentUser: '<shiro:hasPermission name="sys:user:agent:create">true</shiro:hasPermission>',
	            </c:when>
	            <c:otherwise>
	            	createAgentUser: false,
	            </c:otherwise>
            </c:choose>
            /* 编辑员工 */
            updateAgentUser: '<shiro:hasPermission name="sys:user:agent:update">true</shiro:hasPermission>',
            /* 删除员工 */
            deleteAgentUser: '<shiro:hasPermission name="sys:user:agent:delete">true</shiro:hasPermission>',
            /* 重置密码 */
            resetPwd: '<shiro:hasPermission name="sys:user:agent:resetPwd">true</shiro:hasPermission>'
  };
	</script>
</head>
<body>
	<div id="rootNode"></div>
	<script src="${ctxStatic}/js/libs.js" type="text/javascript"></script>
	<script src="${ctxStatic}/common/erpshop.js?v=${staticVersion}" type="text/javascript"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/scripts/manifest.min.js?v=${staticVersion}" type="text/javascript"></script>
	<script src="${ctxStatic}/scripts/vendor.min.js?v=${staticVersion}" type="text/javascript"></script>
	<script src="${ctxStatic}/scripts/serviceprovider/staffInfos.min.js?v=${staticVersion}" type="text/javascript"></script>
	<%-- 	<script src="http://localhost:7777/serviceprovider/staffInfos.min.js?v=${staticVersion}" type="text/javascript"></script> --%>
</body>
</html>
