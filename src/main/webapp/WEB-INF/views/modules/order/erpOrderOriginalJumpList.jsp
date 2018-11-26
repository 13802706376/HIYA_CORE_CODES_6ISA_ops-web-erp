<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>订单任务跳转</title>
<meta name="decorator" content="default" />
<style type="text/css">
	.border-wrap {
		border: #dfdfdf solid 1px;
		border-radius: 5px;
	}

	.border-wrap:after {
		content: '';
		display: block;
		clear: both;
	}

	.margin-bottom-10 {
		margin-bottom: 10px;
	}

	.blockquote {
		margin-left: 10px;
		margin-top: 15px;
	}

	.spanspan {
		display: inline-block;
		width: 42%;
		border-width: 0px;
	}
	.nav li .comebacka:hover{
		background-color: transparent;
		border:1px solid transparent;
		text-decoration: underline;
	}
	.odercz{
		right: 10px;
	    top: 0px;
	    height: 38px;
	    line-height: 38px;
	    overflow: visible;
	}
	.selectRoleNameBox{ padding: 20px 0px; }
	.selectRoleNameBox .control-group{ border-bottom-width: 0px; }
	.selectRoleNameBox .control-group .controls{ margin-left: 100px; }
	.selectRoleNameBox .control-group .control-label{ width: 100px; }
</style>
</head>
<body>
	<div class="act-top positionrelative">
		<ul class="nav nav-tabs">
			<li><a href="javascript:history.back()" class="comebacka">返回</a></li>
			<li class="active">
				<a href="${ctx}/order/erpOrderOriginalJumpList/form?id=${info.id}">
					任务跳转
				</a>
			</li>
		</ul>
	</div>
	<div style="padding:10px;">
		<div class="border-wrap">
			<blockquote class="blockquote">
				<p>任务</p>
			</blockquote>
			<table id="contentTable" class="table table-striped table-condensed">
				<!--  table-bordered table-condensed -->
				<thead>
					<tr>
						<th>任务序号</th>
						<th>任务内容</th>
						<th>负责人</th>
						<shiro:hasPermission name="order:erpOrderSplitInfo:edit">
							<th>操作</th>
						</shiro:hasPermission>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${jumpTasks}" var="task" varStatus="sort">
						<%-- <c:if test="${not empty activity.properties.name && activity.properties.name != 'subProcess'}"> --%>
						<tr>
							<td align="center">${sort.count}</td>
							<td>${task.taskName}</td>
							<td>${task.assignee}</td>
							<td>
								<shiro:hasPermission name="order:erpOrderOriginalInfo:edit">
									<c:if test="${task.current == 1 }">
										当前任务
									</c:if>
									<c:if test="${task.current != 1 }">
											<button class="btn btn-primary taskJump" data-taskid="${task.id}" data-userid="${task.userId }" data-assignee="${task.assignee}">
												跳转到这任务
											</button>
									</c:if>
								</shiro:hasPermission>
							</td>
						</tr>
						<%-- </c:if> --%>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
	<script type="text/javascript">
		$(function() {
			var taskJump = {
				procInsId: '${procInsId}',
				init: function() {
					var self = this;
					$('body').on('click', '.taskJump', function(event) {
						event.preventDefault();
						var taskid = $(this).attr('data-taskid'),
							assignee = $(this).attr('data-assignee');
						//self.getUserByRoleName(taskid);
						if ($.trim(assignee)) {
							self.jumpTask(taskid, $(this).data("userid"));
						} else {
							self.getUserByRoleName(taskid);
						}
					});
				},
				getUserByRoleName: function(taskId) {
					var self = this;
					$.get('${ctx}/workflow/procins/getUserByRoleName?taskId=' + taskId, function(data) {
						var str = '<div class="form-horizontal selectRoleNameBox"><div class="control-group"><label class="control-label">选择负责人：</label><div class="controls"><select class="selectRoleName" style="width:160px;"><option value="" label="请选择">请选择</option>';
						var arr = [];
						if (data instanceof Array && data.length !== 0) {
							for (var i = 0, l = data.length; i < l; i++) {
								arr.push('<option value="'+data[i].id+'" label="'+data[i].name+'">'+data[i].name+'</option>');
							}
							str = str + arr.join('') + '</select></div></div></div>';
							self.jumpWithRoleName(taskId, str);
						} else {
							$.jBox.closeTip();
							$.jBox.tip("请先选择负责人！", "error");
							return false;
						}
					});
				},
				jumpTask: function(taskId, userId) {
					var self = this;
					$.get('${ctx}/workflow/procins/hasSubProcess?procInsId=' + self.procInsId, function(data) {
						$.jBox.closeTip();
						if (data.result) {	//有子流程时result为false
							self.jump(taskId, userId);
						} else {
							$.jBox.tip("该流程还有未结束的子流程, 无法跳转！", "info");
						}
					});
				},
				jump: function(taskId, userId) {
					var self = this;
					$.get("${ctx}/workflow/procins/tasks/jump", {
						procInsId: self.procInsId,
						taskId: taskId,
						userId: userId
					}, function(data) {
						if (data.result) {
							$.jBox.tip("任务跳转成功！", "info");
							setTimeout(function() {
								location.reload();
							}, 3000);
						} else {
							$.jBox.closeTip();
							$.jBox.tip("任务跳转失败！", "error");
						}
					});
				},
				jumpWithRoleName: function(taskId, str) {
					var self = this;
					var submit = function(v, h, f) {
						var userId = $(h).find('select.selectRoleName').val();
						if (!userId) {
							top.$.jBox.tip("请选择负责人！", "error");
							return false;
						} else {
							//跳转
							self.jumpTask(taskId, userId);
						}
					};
					$.jBox(str, { width:320, title: "选择负责人", submit: submit});
					$('.selectRoleName').select2();
				}
			};
			taskJump.init();
		})
	</script>
</body>
</html>