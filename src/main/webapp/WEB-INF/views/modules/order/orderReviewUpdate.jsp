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
	<title>物料制作管理</title>
	<link href="${ctxStatic}/bootstrap/2.3.1/css_cerulean/bootstrap.min.css" rel="stylesheet" type="text/css" />
	<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.min.css" rel="stylesheet" type="text/css" />
	<link href="${ctxStatic}/common/jeesite.css?v=${staticVersion}" rel="stylesheet" type="text/css" />
	<link href="${ctxStatic}/common/shopentry.min.css?v=${staticVersion}" rel="stylesheet" type="text/css" />
	<style type="text/css">
		.margin-bottom-10{margin-bottom: 10px}
		.input-addproductnum{ width: 70px }
		.number-wrapper{ position: absolute; left: 660px; top: 4px; width: 320px; background-color: #fff; padding: 0px 0px 20px; }
		.number-wrapper .form-horizontal{ margin: 0px; }
		.number-wrapper .form-horizontal .control-group .control-label{ width: 170px; text-align: left;}
		.number-wrapper .form-horizontal .control-group .controls{ width: 130px; }
		.number-wrapper .form-horizontal .control-group .controls .products-num input{ width: 35px; }
		.form-horizontal .control-group .controls input.input-xlarge,.form-horizontal .control-group .controls .addproductnum.input-addproductnum{ height: 30px; }
		.form-horizontal .control-group .controls a.btn{ padding: 7px 13px; }
		.audit-content{border: 1px solid #ccc;height: 120px;margin-left: 93px;width: 560px;margin-bottom: 15px;padding-left:10px;padding-top: 10px;}
		.audit-content textarea{width: 530px;height: 80px;}
	</style>
	<script type="text/javascript">
		var ctx = '${ctx}', ctxStatic='${ctxStatic}';
		var pageDataObject = {
			uploadfileurl: "${fileUrl}",
			userid: "${fns:getUser().id}",
			username: "${fns:getUser().name}"
		}; 
		
		var erpOrderOriginalInfoAdd = function() {
			var queryAgentByShop = function(params, cb) {
					$.get(ctx+'/shop/erpShopInfo/queryAgentByShop?agentId='+params.agentId, function(data) {
						if (Array.isArray(data)) {
							params.agentId = data[0].agentId;
							params.agentName = data[0].agentName;
						}
						typeof cb === 'function' && cb(params);
					})
				},
				selectShop = function(cb) {
					var iframePage = '${ctx}/shop/erpShopInfo/shopSearchList';
					top.$.jBox.open("iframe:" + iframePage, "选择商户", 800, 550, {
						buttons:{"确定":"ok", "关闭":true},
						submit: function(v, h, f){
						if (v === 'ok') {
							var frame = $(h).find("#jbox-iframe").contents();
							var shopid = frame.find("#selectedShopId").val();
							var shopname = frame.find("#selectedShopName").val();
							var shopshortname = frame.find("#selectedShopShortName").val();
							var shopnumber = frame.find("#selectedShopNumber").val();
							var industryType = frame.find("#selectedIndustryType").val();
							var contactName = frame.find("#selectedContactName").val();
							var contactPhone = frame.find("#selectedContactPhone").val();
							var serviceProvider = frame.find("#selectedServiceProvider").val();
							var serviceProviderPhone = frame.find("#selectedServiceProviderPhone").val();
							var zhangbeiId = frame.find("#zhangbeiId").val();
							var agentId = frame.find("#agentId").val();
							if (shopid != null && shopid !== '') {
								var params = {
									shopid: shopid,
									shopname: shopname,
									shopshortname: shopshortname,
									shopnumber: shopnumber,
									industryType: industryType,
									contactName: contactName,
									contactPhone: contactPhone,
									serviceProvider: serviceProvider,
									serviceProviderPhone: serviceProviderPhone,
									zhangbeiId: zhangbeiId,
									agentId: agentId,
									agentName: ''
								}
								//typeof cb === 'function' && cb();
								queryAgentByShop(params, cb);
							}
						}
					},
					loaded: function(h){
						$(".jbox-content", top.document).css("overflow-y","hidden");
					}
				});
			}, 
			agentSearchList = function(cb) {
				var iframePage = '${ctx}/shop/erpShopInfo/agentSearchList';
				top.$.jBox.open("iframe:" + iframePage, "选择服务商", 800, 550, {
					buttons:{"确定":"ok", "关闭":true},
					submit: function(v, h, f){
						if (v === 'ok') {
							var frame = $(h).find("#jbox-iframe").contents();
							var agentId = frame.find("#agentId").val();
							var agentName = frame.find("#agentName").val();
							if (agentId != null && agentId !== '') {
								typeof cb === 'function' && cb({
									agentId: agentId,
									agentName: agentName
								})
							}
						}
					},
					loaded: function(h){
						$(".jbox-content", top.document).css("overflow-y","hidden");
					}
				});
			};

			return {
				selectShop: selectShop,
				agentSearchList: agentSearchList
			}
		}();
	</script>
</head>
<body>
	<div id="rootNode"></div>
	<script src="${ctxStatic}/js/libs.js" type="text/javascript"></script>
	<script src="${ctxStatic}/common/erpshop.js?v=${staticVersion}" type="text/javascript"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/scripts/manifest.min.js?v=${staticVersion}" type="text/javascript"></script>
	<script src="${ctxStatic}/scripts/vendor.min.js?v=${staticVersion}" type="text/javascript"></script>
	<script src="${ctxStatic}/scripts/order/erporderoriginalinfoedit.min.js?v=${staticVersion}" type="text/javascript"></script>
<%-- 	<script src="http://localhost:7777/order/erporderoriginalinfoedit.min.js?v=${staticVersion}" type="text/javascript"></script> --%>
</body>
</html>