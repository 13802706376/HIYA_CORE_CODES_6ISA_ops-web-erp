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
	<title>团队绩效统计</title>
	<link href="${ctxStatic}/bootstrap/2.3.1/css_cerulean/bootstrap.min.css" rel="stylesheet" type="text/css" />
	<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.min.css" rel="stylesheet" type="text/css" />
	<link href="${ctxStatic}/common/jeesite.css?v=${staticVersion}" rel="stylesheet" type="text/css" />
	<link href="${ctxStatic}/common/shopentry.min.css?v=${staticVersion}" rel="stylesheet" type="text/css" />
	<link href="${ctxStatic}/common/statistics.css?v=${staticVersion}" rel="stylesheet" type="text/css" />
	<link href="${ctxStatic}/common/index.css?v=${staticVersion}" rel="stylesheet" type="text/css" />
	<link href="${ctxStatic}/css/teamOrder/teamperformance.min.css?v=${staticVersion}" rel="stylesheet" type="text/css" />
	<style type="text/css">
		.displayinline{ display: inline-block; }
		.positionrelative{ position: relative; }
		.positionabsolute{ position: absolute; }
		.padbottom20{ padding-bottom: 20px; }
		.selectRole{ margin-left: 230px; margin-top: 12px; }
		.filterwrapper{ height: 51px; z-index: 1;}
		.blockquote.positionabsolute{ top: -10px; left: 0px; }
		.filterBtn{height: 30px;}
		.ml88{ margin-left: 88px;}
		.filterBtn span{ padding-right: 10px; }
		.filterwrapper .filterBox{ top: 41px; left: 262px; width: 230px; border: #acacac solid 1px; border-radius: 2px; background-color: #fff; z-index: 9999;}
		.searching{ top: 0; left: 0; right: 0; bottom: 0; background-color: rgba(254,254,254,.9); text-indent: 2em; z-index: 10;}
		.filtersetting{ display: block; height: 32px; line-height: 32px; text-indent: 1em; text-decoration: none;}
		.filtersetting.on{ background-color: #f70; color: #fff;}
		.filterDiv .statistics-input{ height: auto; margin-left: 10px;}
		.dataCell{ height: 40px; line-height: 35px; margin-left: 10px; text-overflow: ellipsis; white-space: nowrap; overflow:hidden; }
		.filterBox .btns{ margin-right: 10px; padding-bottom: 8px; text-align: right; }
		.filterBox .btns > button:nth-of-type(2){ margin-left: 10px; }
		.greenDot:before{ display: inline-block; content: ''; width: 9px; height: 9px; border:#232323 solid 1px; border-radius: 50%; background-color: #0e0; margin-right: 6px;}
		.orangeDot:before{ display: inline-block; content: ''; width: 9px; height: 9px; border:#232323 solid 1px; border-radius: 50%; background-color: #f90; margin-right: 6px;}
		.blueDot:before{ display: inline-block; content: ''; width: 9px; height: 9px; border:#232323 solid 1px; border-radius: 50%; background-color: #09f; margin-right: 6px;}
		.redDot:before{ display: inline-block; content: ''; width: 9px; height: 9px; border:#232323 solid 1px; border-radius: 50%; background-color: #f30; margin-right: 6px;}
		.halfwidth{ width: 45%; display: inline-block; }
		.selectRole .multiselect .multiselect__tags{ height: 30px; overflow: hidden;}
		.selectRole .multiselect .multiselect__tags-wrap{ height: 26px; width: 100%;}
		.selectRole .multiselect .multiselect__single{ display: none; }
		.selectRole .multiselect .multiselect__tags-wrap .multiselect__tag{ margin-top: 4px; }
	</style>
	
	<script type="text/javascript">
		var ctx = '${ctx}', ctxStatic='${ctxStatic}';
		var pageDataObject = {
			uploadfileurl: "${fileUrl}",
			userid: "${fns:getUser().id}",
			username: "${fns:getUser().name}",
			orderAduit: '<shiro:hasPermission name="order:deliveryServicePerformanceTeamUrl:order">true</shiro:hasPermission>',
			serviceAduit: '<shiro:hasPermission name="order:deliveryServicePerformanceTeamUrl:service">true</shiro:hasPermission>'
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
	<script src="${ctxStatic}/scripts/teamOrder/teamperformance.min.js?v=${staticVersion}" type="text/javascript"></script>
<%-- 	<script src="http://localhost:7777/teamOrder/teamperformance.min.js?v=${staticVersion}" type="text/javascript"></script> --%>
</body>
</html>