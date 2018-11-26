<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>订单物料同步日志管理</title>
	<meta name="decorator" content="default"/>
    <style>.padding10{padding: 0 10px}</style>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/order/erpOrderMaterialSyncLog/">订单物料同步日志列表</a></li>
		<shiro:hasPermission name="order:erpOrderMaterialSyncLog:edit"><li><a href="${ctx}/order/erpOrderMaterialSyncLog/form">订单物料同步日志添加</a></li></shiro:hasPermission>
	</ul>
    <sys:message content="${message}"/>
	<%--<form:form id="searchForm" modelAttribute="erpOrderMaterialSyncLog" action="${ctx}/order/erpOrderMaterialSyncLog/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
            <li>
                <label>日期：</label>
                <input path="syncDateStart" id="syncDateStart" type="text" maxlength="20" class="input-medium Wdate" value="${bbd}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',isShowClear:false,readOnly:true});"/> - 
                <input path="syncDateEnd" id="syncDateEnd" type="text" maxlength="20" class="input-medium Wdate" value="${ebd}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',isShowClear:false,readOnly:true});"/>
            </li>
            <li>
                <label for="exceptionOnly" style="width:150px;text-align:left">
                <input id="exceptionOnly" name="exceptionOnly" type="checkbox" value="true"/>
                    只查询异常信息
                </label>
            </li>
			<li class="btns">
                <input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
            </li>
            <li class="btns"><input class="btn btn-warning" type="reset" value="重置"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="order:erpOrderMaterialSyncLog:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody id="tbody"></tbody>
	</table>
	<div id="pagination"></div>--%>
    <div id="rootNode"></div>
    <script type="text/javascript">
        var hasPermission = '<shiro:hasPermission name="order:erpOrderMaterialSyncLog:edit">true</shiro:hasPermission>';
        /*function page(n, size, str, cb) {
            var params = {
                exceptionOnly: true,
                page: {
                    pageNo: n,
                    pageSize: size
                }
            };
            $.ajax({
                type: 'POST',
                url: '${ctx}/order/erpOrderMaterialSyncLog/page',
                dataType: 'json',
                contentType: 'application/json;charset=utf-8',
                data: JSON.stringify(params),
                success: function (data) {
                    // typeof cb === 'function' && cb(data);
                    var dataHtml = '';
                    $.each(data.list, function (key, value) {
                        dataHtml +=
                            '<tr>'
                            + '<td><a href="${ctx}/order/erpOrderMaterialSyncLog/form?id=' + value.id + '">' + value.syncDate + '</a></td>'
                            + '<td>' + value.orderNo + '</td>'
                            + '</tr>'
                    });
                    $('#tbody').html(dataHtml);
                    $('#pagination').html(data.html);
                }
            });
            return false;
        }*/
    </script>
    <script src="${ctxStatic}/common/erpshop.js?v=${staticVersion}" type="text/javascript"></script>
    <script src="${ctxStatic}/scripts/manifest.min.js?v=${staticVersion}" type="text/javascript"></script>
    <script src="${ctxStatic}/scripts/vendor.min.js?v=${staticVersion}" type="text/javascript"></script>
    <script src="${ctxStatic}/scripts/sys/materialsyncloglist.min.js?v=${staticVersion}" type="text/javascript"></script>
    <!-- <script src="http://localhost:7777/sys/materialsyncloglist.min.js?v=${staticVersion}" type="text/javascript"></script> -->
</body>
</html>
