<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%String pageBasePath = request.getServletPath();%>
<!DOCTYPE html>
<html style="overflow-x:auto;overflow-y:auto;">
<head>
	<title><sitemesh:title/> - Powered By 运营中心</title>
	<% if(pageBasePath.indexOf("/admin/workflow/taskDetail") != -1 || pageBasePath.indexOf("/admin/order/erpOrderOriginalInfo/toAdd") != -1){ %>
		<%@include file="/WEB-INF/views/include/headNew.jsp" %>
		<% }else{ %>
		<%@include file="/WEB-INF/views/include/head.jsp" %>
	<% } %>
	<sitemesh:head/>
	<script type="text/javascript">var shop_store_data_advanced_edit = '<shiro:hasPermission name="shop:storeData:advancedEdit">true</shiro:hasPermission>';</script>
</head>
<body>
	<sitemesh:body/>
	<script type="text/javascript">//<!-- 无框架时，左上角显示菜单图标按钮。
		if(!(self.frameElement && self.frameElement.tagName=="IFRAME")){
			$("body").prepend("<i id=\"btnMenu\" class=\"icon-th-list\" style=\"cursor:pointer;float:right;margin:10px;\"></i><div id=\"menuContent\"></div>");
			$("#btnMenu").click(function(){
				top.$.jBox('get:${ctx}/sys/menu/treeselect;JSESSIONID=<shiro:principal property="sessionid"/>', {title:'选择菜单', buttons:{'关闭':true}, width:300, height: 350, top:10});
				//if ($("#menuContent").html()==""){$.get("${ctx}/sys/menu/treeselect", function(data){$("#menuContent").html(data);});}else{$("#menuContent").toggle(100);}
			});
			$('.nav .comebacka').hide();
		}//-->
	</script>
</body>
</html>