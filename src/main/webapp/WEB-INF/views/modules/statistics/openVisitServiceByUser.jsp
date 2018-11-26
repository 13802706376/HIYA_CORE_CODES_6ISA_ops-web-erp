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
	<title>上门服务管理</title>
	<link href="${ctxStatic}/bootstrap/2.3.1/css_cerulean/bootstrap.min.css" rel="stylesheet" type="text/css" />
	<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.min.css" rel="stylesheet" type="text/css" />
	<link href="${ctxStatic}/common/jeesite.css?v=${staticVersion}" rel="stylesheet" type="text/css" />
	<link href="${ctxStatic}/common/shopentry.min.css?v=${staticVersion}" rel="stylesheet" type="text/css" />
	<link href="${ctxStatic}/common/statistics.css?v=${staticVersion}" rel="stylesheet" type="text/css" />
	<link href="${ctxStatic}/css/teamOrder/servicePage.min.css?v=${staticVersion}" rel="stylesheet" type="text/css" />
</head>
<body>
	<div id="rootNode"></div>
	<script type="text/javascript">
		var ctx = '${ctx}', ctxStatic='${ctxStatic}';
		var pageDataObject = {
			uploadfileurl: "${fileUrl}",
			userid: "${fns:getUser().id}",
			username: "${fns:getUser().name}",
			visitCancel: '<shiro:hasPermission name="visitService:dataEdit:cancel">true</shiro:hasPermission>',
			visitUpdate: '<shiro:hasPermission name="visitService:dataEdit:update">true</shiro:hasPermission>',
			visitCreate: '<shiro:hasPermission name="visitService:dataEdit:create">true</shiro:hasPermission>',
			isPersonal: '<shiro:hasPermission name="visitService:dataQuery:personal">true</shiro:hasPermission>',
			isAll: '<shiro:hasPermission name="visitService:dataQuery:all">true</shiro:hasPermission>',
			isCompany: '<shiro:hasPermission name="visitService:dataQuery:company">true</shiro:hasPermission>'
		};  
	</script>
	<script src="${ctxStatic}/js/libs.js" type="text/javascript"></script>
	<script src="${ctxStatic}/common/erpshop.js?v=${staticVersion}" type="text/javascript"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/scripts/manifest.min.js?v=${staticVersion}" type="text/javascript"></script>
	<script src="${ctxStatic}/scripts/vendor.min.js?v=${staticVersion}" type="text/javascript"></script>
	<script src="${ctxStatic}/scripts/teamOrder/openVisitServiceByUser.min.js?v=${staticVersion}" type="text/javascript"></script>
<%-- 	<script src="http://localhost:7777/teamOrder/openVisitServiceByUser.min.js?v=${staticVersion}" type="text/javascript"></script> --%>
	
	<script type="text/javascript">var iframePage = '${ctx}/shop/erpShopInfo/shopSearchList';</script>
	<script>
		$(document).on('click', '#shopButton,#shopName', function(event) {
			event.preventDefault();
			// 正常打开	
			top.$.jBox.open("iframe:" + iframePage, "选择商户", 800, 550, {
				buttons:{"确定":"ok", "关闭":true},
				submit: function(v, h, f){	
					if(v === 'ok'){
						var frame = $(h).find("#jbox-iframe").contents();
						var shopid = frame.find("#shopId").val();
						var shopname = frame.find("#selectedShopName").val();
						var mainStoreAddress = frame.find("#mainStoreAddress").val();
							
						if(shopid != null && shopid !== ''){
							$("#shopInfoId").val(shopid);
							$("#shopName").val(shopname);
							$("#shopInfoIdEdit").val(shopid);
							$("#shopNameEdit").val(shopname);
							
							$("#serviceAddress").val(mainStoreAddress);
							$("#serviceAddressEdit").val(mainStoreAddress);
						}
					}
				},
				loaded: function(h){
					$(".jbox-content", top.document).css("overflow-y","hidden");
				}
			});
		});
	</script>
</body>
</html>