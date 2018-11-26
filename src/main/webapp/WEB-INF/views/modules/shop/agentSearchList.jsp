<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>

<head>
	<title>数据选择</title>
	<meta http-equiv="Content-Type" content="text/html;charset=utf-8">
	<meta name="author" content="http://jeesite.com/">
	<meta name="renderer" content="webkit">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta http-equiv="Expires" content="0">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-store">
	<script src="//hm.baidu.com/hm.js?82116c626a8d504a5c0675073362ef6f"></script>
	<script src="/ops-web-erp/static/jquery/jquery-1.8.3.min.js" type="text/javascript"></script>
	<link href="/ops-web-erp/static/bootstrap/2.3.1/css_cerulean/bootstrap.min.css" type="text/css" rel="stylesheet">
	<script src="/ops-web-erp/static/bootstrap/2.3.1/js/bootstrap.min.js" type="text/javascript"></script>
	<link href="/ops-web-erp/static/bootstrap/2.3.1/awesome/font-awesome.min.css" type="text/css" rel="stylesheet">
	<!--[if lte IE 7]><link href="/ops-web-erp/static/bootstrap/2.3.1/awesome/font-awesome-ie7.min.css" type="text/css" rel="stylesheet" /><![endif]-->
	<!--[if lte IE 6]><link href="/ops-web-erp/static/bootstrap/bsie/css/bootstrap-ie6.min.css" type="text/css" rel="stylesheet" />
	<script src="/ops-web-erp/static/bootstrap/bsie/js/bootstrap-ie.min.js" type="text/javascript"></script><![endif]-->
	<!-- <link href="/ops-web-erp/static/jquery-select2/3.4/select2.min.css" rel="stylesheet">
	<script src="/ops-web-erp/static/jquery-select2/3.4/select2.min.js" type="text/javascript"></script> -->
	<!-- <link href="/ops-web-erp/static/jquery-validation/1.11.0/jquery.validate.min.css" type="text/css" rel="stylesheet">
	<script src="/ops-web-erp/static/jquery-validation/1.11.0/jquery.validate.min.js" type="text/javascript"></script> -->
	<link href="/ops-web-erp/static/jquery-jbox/2.3/Skins/Bootstrap/jbox.min.css" rel="stylesheet">
	<script src="/ops-web-erp/static/jquery-jbox/2.3/jquery.jBox-2.3.min.js" type="text/javascript"></script>
	<script src="/ops-web-erp/static/My97DatePicker/WdatePicker.js" type="text/javascript"></script><link href="/ops-web-erp/static/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" type="text/css">
	<script src="/ops-web-erp/static/common/mustache.min.js" type="text/javascript"></script>
	<link href="/ops-web-erp/static/common/jeesite.css" type="text/css" rel="stylesheet">
	<!-- <script src="/ops-web-erp/static/common/jeesite.js?aw6sd55a446a" type="text/javascript"></script> -->
	<script type="text/javascript">var ctx = '/ops-web-erp/admin', ctxStatic='/ops-web-erp/static';</script>
	<meta name="decorator" content="blank">
	<style type="text/css">
		ul,li{ list-style: none; margin:0; padding: 0}
		.erpshopul li{ height: 30px; line-height: 30px;border-bottom: #ededed 1px solid;white-space:nowrap; overflow: hidden; text-overflow: ellipsis;}
		.erpshopul li a{ display: block; text-indent: 2em; }
		.erpshopul li.on a{ display: block; background-color: #e93; color: #fff;}
	</style>
	<script type="text/javascript">
		//选择商户后信息回填至订单添加页面
		function checkedShopinfo(index) {
			$("#agentId").val($("#id_"+index).val());
			$("#agentName").val($("#agentName_"+index).val());
			$(".itemshop").removeClass('on');
			$(".itemshop").eq(parseInt(index)).addClass('on');
		}
		
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#shopSearchForm").submit();
        	return false;
        }
	</script>
</head>

<body marginwidth="0" marginheight="0">
	<input id="agentId" type="hidden"/>
	<input id="agentName" type="hidden"/>
	<div id="search" class="form-search hide" style="padding: 10px 0px 0px 13px; display: block;">
		<form:form id="shopSearchForm" action="${ctx}/shop/erpShopInfo/agentSearchList" method="post" class="breadcrumb form-search">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<label for="key" class="control-label" style="padding:5px 5px 3px 0;">关键字：</label>
			<input id="keyword" type="text" name="keyWord" value="${keyWord}" maxlength="60" style="width:150px;">
			<button class="btn" type="submit">搜索</button>
		</form:form>
	</div>
	<div id="tree" class="ztree" style="padding:15px 20px;">
		<ul class="erpshopul">
			<c:forEach items="${page.list}" var="agent" varStatus="index">
				<li class="level0 itemshop">
					<span class="button level0 switch root_open"></span>
					<a class="level0 curSelectedNode" onclick="checkedShopinfo('${index.index}');" target="_blank" title="${agent.agentName}" style="cursor: pointer; text-decoration: none;">
						<span>${agent.agentName}</span>
						<input type="hidden" id="id_${index.index}" value="${agent.agentId}">
						<input type="hidden" id="agentName_${index.index}" value="${agent.agentName}">
					</a>
				</li>
			</c:forEach>
		</ul>
	</div>
	<div class="pagination">${page}</div>
</body>
</html>