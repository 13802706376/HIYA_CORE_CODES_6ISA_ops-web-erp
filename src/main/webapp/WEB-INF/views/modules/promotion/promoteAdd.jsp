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
	<title>新增数据</title>
	<link href="${ctxStatic}/bootstrap/2.3.1/css_cerulean/bootstrap.min.css" rel="stylesheet" type="text/css" />
	<link href="${ctxStatic}/bootstrap/2.3.1/awesome/font-awesome.min.css" type="text/css" rel="stylesheet" />
	<link href="${ctxStatic}/common/jeesite.css?v=${staticVersion}" rel="stylesheet" type="text/css" />
	<link href="${ctxStatic}/common/shopentry.min.css?v=${staticVersion}" rel="stylesheet" type="text/css" />
	<script type="text/javascript">
		var ctx = '${ctx}', ctxStatic='${ctxStatic}';
		var pageDataObject = {
			uploadfileurl: "${fileUrl}",
			userid: "${fns:getUser().id}"
		};
	</script>
</head>
<body>
	<div id="promotionWrapper"></div>
	<script src="${ctxStatic}/jquery/jquery-1.8.3.min.js" type="text/javascript"></script>
	<script src="${ctxStatic}/bootstrap/2.3.1/js/bootstrap.min.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js" type="text/javascript"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/common/erpshop.js?v=${staticVersion}" type="text/javascript"></script>
	<script type="text/javascript" src="${ctxStatic}/scripts/manifest.min.js?v=${staticVersion}"></script>
	<script type="text/javascript" src="${ctxStatic}/scripts/vendor.min.js?v=${staticVersion}"></script>
	<!-- <script type="text/javascript" src="${ctxStatic}/scripts/promotiondatamanage/adderppromotedata.min.js?v=${staticVersion}"></script> -->
	<script type="text/javascript" src="http://localhost:7777/promotiondatamanage/adderppromotedata.min.js?v=${staticVersion}"></script>
</body>
</html>