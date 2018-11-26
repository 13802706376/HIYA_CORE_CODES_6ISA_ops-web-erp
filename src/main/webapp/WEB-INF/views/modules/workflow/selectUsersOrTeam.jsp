<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html class="wrapper">
	<head>
		<title>选择成员</title>
		<meta name="decorator" content="default"/>
		<link href="${ctxStatic}/common/act.css?v=2dfsd22fd2a" type="text/css" rel="stylesheet" />
		<style type="text/css">
			.aps{width:100%; height: 435px; overflow-x: hidden; overflow-y: auto;}
			.aps .tab-content .accordion-inner{ padding: 0px !important; }
			.aps .tab-content .accordion-inner a{ display: block; padding-left: 10px; height: 30px; line-height: 30px; text-decoration: none; }
			.aps .tab-content .accordion-inner a.on{ background-color: #e61; color: #fff; }
			/* .aps #accordionaccordion{max-height: 285px; overflow-y: auto;}
			.aps #selectTeamTeamtask .accordion-body{max-height: 285px; overflow-y: auto;}
			.aps #selectTeamTeamtask .wrpper{max-height: 380px; overflow-y: auto;} */
			.aps .accordion{ margin-bottom: 0px; }
		</style>
	</head>
	<body class="wrapper">
		<div class="selectUserOrTeam" id="selectUserOrTeamWrapper"></div>
		<script type="text/javascript">
			var selectteam = getQueryString('selectteam', location.href);
			window.__userId__ = getQueryString('userId', location.href);
			window.__pushTeamUserUrl__ = '${ctx}/workflow/pushTeamUser/?selectteam='+selectteam;
			window.__pushTeamAndUserUrl__ = '${ctx}/workflow/pushTeamAndUser/?selectteam='+selectteam;
		</script>
		<script type="text/javascript" src="${ctxStatic}/scripts/manifest.min.js?v=${staticVersion}"></script>
		<script type="text/javascript" src="${ctxStatic}/scripts/vendor.min.js?v=${staticVersion}"></script>
		<script type="text/javascript" src="${ctxStatic}/scripts/workflow/teamtask.min.js?v=${staticVersion}"></script>
		<!-- <script type="text/javascript" src="//localhost:7777/workflow/teamtask.min.js?v=${staticVersion}"></script> -->
	</body>
</html>