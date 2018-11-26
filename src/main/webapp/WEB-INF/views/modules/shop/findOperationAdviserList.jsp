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
	<title>掌贝进件</title>
	<link href="${ctxStatic}/bootstrap/2.3.1/css_cerulean/bootstrap.min.css" rel="stylesheet" type="text/css" />
	<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.min.css" rel="stylesheet" type="text/css" />
	<style type="text/css">
		ul, li{ list-style: none; margin: 0; }
		a{ text-decoration: none; }
		a:hover{ text-decoration: none; }
		.erpshopul{padding: 5px 10px}
		.serachInput{margin: 10px 0px 10px 100px;}
		.serachInput .input-name{ width:160px; height: 25px; }
		.erpshopul li{ height: 30px; line-height: 30px; text-indent: 2em; border-bottom: #dedede 1px solid; }
		.erpshopul li.on{ background-color: #e60; color: #fff; }
		.erpshopul li.on .list{color: #fff; }
		.erpshopul li .list{ display: block; }
	</style>
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
	<script src="${ctxStatic}/js/libs.js" type="text/javascript"></script>
	<script src="${ctxStatic}/common/erpshop.js?v=${staticVersion}" type="text/javascript"></script>
	<script src="${ctxStatic}/scripts/manifest.min.js?v=${staticVersion}" type="text/javascript"></script>
	<script src="${ctxStatic}/scripts/vendor.min.js?v=${staticVersion}" type="text/javascript"></script>
	<script src="${ctxStatic}/scripts/sys/findoperationadviser.min.js?v=${staticVersion}" type="text/javascript"></script>
	<!-- <script src="http://localhost:7777/sys/findoperationadviser.min.js?v=${staticVersion}" type="text/javascript"></script> -->
</body>
</html>