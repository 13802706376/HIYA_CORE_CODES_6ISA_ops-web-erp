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
	<title>聚引客分单评论管理</title>
	<link href="${ctxStatic}/bootstrap/2.3.1/css_cerulean/bootstrap.min.css" rel="stylesheet" type="text/css" />
	<link href="${ctxStatic}/bootstrap/2.3.1/awesome/font-awesome.min.css" type="text/css" rel="stylesheet" />
	<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.min.css" rel="stylesheet" type="text/css" />
	<link href="${ctxStatic}/common/jeesite.css?v=${staticVersion}" rel="stylesheet" type="text/css" />
	<link href="${ctxStatic}/common/statistics.css?v=${staticVersion}" rel="stylesheet" type="text/css" />
	<link href="${ctxStatic}/css/ordersplitcomment/ordersplitcomment.min.css?v=${staticVersion}" rel="stylesheet" type="text/css" />
	<style type="text/css">.multiselect{z-index:2;}.multiselect__content-wrapper{z-index: 9999 !important;}</style>
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
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/common/erpshop.js?v=${staticVersion}" type="text/javascript"></script>
	<script src="${ctxStatic}/scripts/manifest.min.js?v=${staticVersion}" type="text/javascript"></script>
	<script src="${ctxStatic}/scripts/vendor.min.js?v=${staticVersion}" type="text/javascript"></script>
	<script src="${ctxStatic}/scripts/ordersplitcomment/ordersplitcomment.min.js?v=${staticVersion}" type="text/javascript"></script>
<!-- 	<script src="http://localhost:7777/ordersplitcomment/ordersplitcomment.min.js?f0f1ca8a87a2d71d1d3d" type="text/javascript"></script> -->
</body>
</html>