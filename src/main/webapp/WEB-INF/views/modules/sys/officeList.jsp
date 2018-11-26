<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>机构管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			var tpl = $("#treeTableTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
			var data = ${fns:toJson(list)}, rootId = "${not empty office.id ? office.id : '0'}";
			addRow("#treeTableList", tpl, data, rootId, true);
			$("#treeTable").treeTable({expandLevel : 5});
		});
		function addRow(list, tpl, data, pid, root){
			for (var i=0; i<data.length; i++){
				var row = data[i];
				if ((${fns:jsGetVal('row.parentId')}) == pid){
					$(list).append(Mustache.render(tpl, {
						dict: {
							type: getDictLabel(${fns:toJson(fns:getDictList('sys_office_type'))}, row.type)
						},  pid: (root?0:pid), row: row
					}));
					addRow(list, tpl, data, row.id);
				}
			}
		}
		
		function sync(){
			$.jBox.tip("正在同步机构...", 'loading', {
				timeout : 0,
				permanent : true
			});
			$.post("${ctx}/sys/office/sync", {
				
			}, function(data) {
				$.jBox.closeTip();
				if (data.result) {
					$("#searchForm").submit();
					$.jBox.info("同步成功");
				} else {
					$.jBox.info("同步失败");
				}
			});
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/sys/office/list?id=${office.id}&parentIds=${office.parentIds}">机构列表</a></li>
	</ul>
	<shiro:hasPermission name="sys:office:edit">
	<form:form id="searchForm"  class="breadcrumb form-search ">
		<ul class="ul-form">
			<li class="btns">
				<input id="btnSync" class="btn btn-primary" type="button" value="同步" onclick="javascript:sync();"/>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	</shiro:hasPermission>
	<sys:message content="${message}"/>
	<table id="treeTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>机构名称</th><th>归属区域</th><th>机构编码</th><th>机构类型</th><th>备注</th> <shiro:hasPermission name="sys:office:edit"><th>操作</th></shiro:hasPermission> </tr></thead>
		<tbody id="treeTableList"></tbody>
	</table>
	<script type="text/template" id="treeTableTpl">
		<tr id="{{row.id}}" pId="{{pid}}">
			<td><a href="${ctx}/sys/office/form?id={{row.id}}">{{row.name}}</a></td>
			<td>{{row.area.name}}</td>
			<td>{{row.code}}</td>
			<td>{{dict.type}}</td>
			<td>{{row.remarks}}</td>

         <shiro:hasPermission name="sys:office:edit">
                <td>
					<a href="${ctx}/sys/office/delete?id={{row.id}}" onclick="return confirmx('确认要删除该角色吗？', this.href)">删除</a>
				</td>
          </shiro:hasPermission>	

		</tr>
	</script>
</body>
</html>