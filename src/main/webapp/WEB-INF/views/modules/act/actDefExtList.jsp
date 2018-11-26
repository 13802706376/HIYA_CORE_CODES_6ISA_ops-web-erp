<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>流程节点扩展管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/act/actDefExt/">流程节点扩展列表</a></li>
		<shiro:hasPermission name="act:actDefExt:edit"><li><a href="${ctx}/act/actDefExt/form">流程节点扩展添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="actDefExt" action="${ctx}/act/actDefExt/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>流程定义：</label>
				<form:input path="processDefineKey" htmlEscape="false" maxlength="164" class="input-medium"/>
			</li>
			<li><label>节点ID：</label>
				<form:input path="actId" htmlEscape="false" maxlength="164" class="input-medium"/>
			</li>
			<li><label>角色名称：</label>
				<form:input path="roleName" htmlEscape="false" maxlength="300" class="input-medium"/>
			</li>
			<li><label>处理人：</label>
				<form:input path="assignee" htmlEscape="false" maxlength="200" class="input-medium"/>
			</li>
			<li><label>回调服务：</label>
				<form:input path="callbackService" htmlEscape="false" maxlength="2000" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>主键</th>
				<th>流程定义</th>
				<th>节点ID</th>
				<th>角色ID</th>
				<th>角色名称</th>
				<th>处理人</th>
				<th>表单模板</th>
				<th>邮件配置</th>
				<th>回调服务</th>
				<th>备注</th>
				
				<th>正常工时</th>
				<th>加急工时</th>
				<th>指派的角色</th>
                <th>指派的角色2</th>
				
				<th>是否是关键任务点</th>
				<th>正常既定消耗工时</th>
				<th>紧急既定消耗工时</th>
                <th>从待生产库激活后正常既定消耗工时</th>
                <th>从待生产库激活后紧急既定消耗工时</th>
				
				
				<th>创建人</th>
				<th>创建时间</th>
				<th>修改人</th>
				<th>修改时间</th>
				<shiro:hasPermission name="act:actDefExt:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="actDefExt">
			<tr>
				<td><a href="${ctx}/act/actDefExt/form?id=${actDefExt.id}">
					${actDefExt.id}
				</a></td>
				<td>
					${actDefExt.processDefineKey}
				</td>
				<td>
					${actDefExt.actId}
				</td>
				<td>
					${actDefExt.roleId}
				</td>
				<td>
					${actDefExt.roleName}
				</td>
				<td>
					${actDefExt.assignee}
				</td>
				<td>
					${actDefExt.formTemplate}
				</td>
				<td>
					${actDefExt.emailTemplate}
				</td>
				<td>
					${actDefExt.callbackService}
				</td>
				<td>
					${actDefExt.remark}
				</td>
				
					<td>
					${actDefExt.taskDateHours}
				</td>
					<td>
					${actDefExt.urgentTaskDateHours}
				</td>
					<td>
					${actDefExt.taskUserRole}
				</td>
					<td>
					${actDefExt.taskUserRole2}
				</td>
				
				<td>${actDefExt.isKeyPoint == "Y" ? "是" : "否" }</td>
				<td>${actDefExt.normalTaskHours }</td>
				<td>${actDefExt.urgentTaskHours }</td>
				<td>${actDefExt.activationNormalTaskHours }</td>
				<td>${actDefExt.activationUrgentTaskHours }</td>
				
				<td>
					${actDefExt.createUser}
				</td>
				<td>
					<fmt:formatDate value="${actDefExt.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${actDefExt.updateUser}
				</td>
				<td>
					<fmt:formatDate value="${actDefExt.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="act:actDefExt:edit"><td>
    				<a href="${ctx}/act/actDefExt/form?id=${actDefExt.id}">修改</a>
					<a href="${ctx}/act/actDefExt/delete?id=${actDefExt.id}" onclick="return confirmx('确认要删除该流程节点扩展吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>