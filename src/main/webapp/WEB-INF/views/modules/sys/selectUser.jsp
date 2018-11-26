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
	<link href="${ctxStatic}/bootstrap/2.3.1/css_cerulean/bootstrap.min.css" rel="stylesheet" type="text/css" />
	<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.min.css" rel="stylesheet" type="text/css" />
	<link href="${ctxStatic}/common/jeesite.css?v=efefed1cv5dsv155" rel="stylesheet" type="text/css" />
	<link href="${ctxStatic}/common/shopentry.min.css?v=${staticVersion}" rel="stylesheet" type="text/css" />
	<title>工号管理</title>
	<meta name="decorator" content="default"/>
	<style type="text/css">
		.iconImg{ display: inline-block; text-align: center; width: 50px; height: 50px; border-radius: 50%; border:3px #fff solid; }
		.iconImg.on{ border:3px #f40 solid;}
		.iconImgs .iconml{ display: inline-block; width: 56px; height: 56px; margin-right: 15px; margin-bottom: 15px}
		.searchWrapper .input-append{margin-left:20px}.searchWrapper .userList{padding:10px 1px}.searchWrapper .userList .users .user{height:30px;line-height:30px;display:block;text-indent:2em}.searchWrapper .userList .users:hover{background-color:#eee}.searchWrapper .userList .users .on{background-color:#e61;color:#fff}
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
	<div style="padding: 10px" id="rootNode"></div>
	<script src="${ctxStatic}/jquery/jquery-1.8.3.min.js" type="text/javascript"></script>
	<script src="${ctxStatic}/common/erpshop.js?v=${staticVersion}" type="text/javascript"></script>
	<script src="${ctxStatic}/scripts/manifest.min.js?v=${staticVersion}" type="text/javascript"></script>
	<script src="${ctxStatic}/scripts/vendor.min.js?v=${staticVersion}" type="text/javascript"></script>
	<script src="${ctxStatic}/scripts/sys/selectusers.min.js?v=${staticVersion}" type="text/javascript"></script>
	
</body>
</html>