<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="author" content="http://www.yunnex.com/">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta http-equiv="Expires" content="0">
    <meta http-equiv="Cache-Control" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-store">
    <title>服务商信息管理</title>
    <link href="${ctxStatic}/bootstrap/2.3.1/css_cerulean/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.min.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/common/jeesite.css?v=${staticVersion}" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/common/shopentry.min.css?v=${staticVersion}" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/common/statistics.css?v=${staticVersion}" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/serviceprovider/serviceprovider.min.css?v=${staticVersion}" rel="stylesheet"
          type="text/css"/>
    <script type="text/javascript">
        var ctx = '${ctx}', ctxStatic = '${ctxStatic}';
        var pageDataObject = {
            uploadfileurl: "${fileUrl}",
            userid: "${fns:getUser().id}",
            username: "${fns:getUser().name}",
            /* 同步 */
            syncAll: '<shiro:hasPermission name="agent:erpAgentInfo:syncAll">true</shiro:hasPermission>',
            /* 查看登录账号信息 */
            viewAgentUser: '<shiro:hasPermission name="agent:erpAgentInfo:user:view">true</shiro:hasPermission>',
            /* 编辑员工资料 */
            updateAgentUser: '<shiro:hasPermission name="sys:user:agent:update">true</shiro:hasPermission>',
            /* 查看员工信息 */
            viewAgentUserList: '<shiro:hasPermission name="sys:user:agent:list">true</shiro:hasPermission>'
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
	<script src="${ctxStatic}/scripts/serviceprovider/serviceprovider.min.js?v=${staticVersion}" type="text/javascript"></script>
<%-- 	<script src="http://localhost:7777/serviceprovider/serviceprovider.min.js?v=${staticVersion}" type="text/javascript"></script> --%>
</body>
</html>
