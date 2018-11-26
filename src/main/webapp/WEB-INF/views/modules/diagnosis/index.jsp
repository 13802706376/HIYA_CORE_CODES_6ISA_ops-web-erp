<%@ page contentType="text/html;charset=UTF-8" %>
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
	<title>门店基本信息</title>
	<link href="${ctxStatic}/jquery-select2/3.4/select2.min.css" rel="stylesheet" />
	<link href="${ctxStatic}/bootstrap/2.3.1/css_cerulean/bootstrap.min.css" rel="stylesheet" type="text/css" />
	<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.min.css" rel="stylesheet" type="text/css" />
	<link href="${ctxStatic}/common/jeesite.css?v=66ds416ds2d31263" rel="stylesheet" type="text/css" />
	<link href="${ctxStatic}/common/shopentry.min.css?v=62ad63da692d31263" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="${ctxStatic}/css/promotion/promotion.min.css">
	<script type="text/javascript">
		var ctx = '${ctx}', ctxStatic='${ctxStatic}';
		var pageDataObject = {
			uploadfileurl: "${fileUrl}",
			userid: "${fns:getUser().id}",
			username: "${fns:getUser().name}"
		};
	</script>
</head>
<body>
	<div id="rootNode"></div>
	<script src="${ctxStatic}/jquery/jquery-1.8.3.min.js" type="text/javascript"></script>
	<script src="${ctxStatic}/bootstrap/2.3.1/js/bootstrap.min.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery-select2/3.4/select2.min.js" type="text/javascript"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/common/erpshop.js?v=de16df526s69e95d6" type="text/javascript"></script>
	<script type="text/javascript" src="${ctxStatic}/common/popup.js?v=${staticVersion}"></script>
	<script type="text/javascript" src="${ctxStatic}/js/promotion/runtime.min.js?v=${staticVersion}"></script>
	<script type="text/javascript" src="${ctxStatic}/js/promotion/vendor.min.js?v=${staticVersion}"></script>
	<script type="text/javascript" src="${ctxStatic}/js/promotion/promotion.min.js?v=${staticVersion}"></script>
</body>
</html>