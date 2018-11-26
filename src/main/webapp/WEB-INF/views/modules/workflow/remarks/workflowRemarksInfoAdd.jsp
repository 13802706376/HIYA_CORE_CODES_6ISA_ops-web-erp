<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>添加备注</title>
	<meta name="decorator" content="default"/>
	
	<link href="${ctxStatic}/common/shopentry.min.css?v=${staticVersion}" rel="stylesheet" type="text/css" />
	<link href="${ctxStatic}/css/workflow/addremarks.min.css?v=${staticVersion}" rel="stylesheet" type="text/css" />
	
	<style type="text/css">
		.number-wrapper{ position: absolute; left: 660px; top: 4px; width: 320px; background-color: #fff; padding: 0px 0px 20px; }
		.number-wrapper .form-horizontal{ margin: 0px; }
		.number-wrapper .form-horizontal .control-group .control-label{ width: 170px; text-align: left;}
		.number-wrapper .form-horizontal .control-group .controls{ width: 130px; }
		.number-wrapper .form-horizontal .control-group .controls .products-num input{ width: 35px; }
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
	<script src="${ctxStatic}/scripts/workflow/addremarks.min.js?v=${staticVersion}" type="text/javascript"></script>
<%-- 	<script src="http://localhost:7777/workflow/addremarks.min.js?v=${staticVersion}" type="text/javascript"></script> --%>
</body>
</html>