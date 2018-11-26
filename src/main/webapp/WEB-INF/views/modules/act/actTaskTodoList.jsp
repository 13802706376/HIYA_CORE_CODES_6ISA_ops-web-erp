<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>我的任务</title>
	<meta name="decorator" content="default"/>
	<link href="${ctxStatic}/common/act.css?v=asdswwc85d5f" type="text/css" rel="stylesheet" />
</head>
<body>
	<div class="act-top positionrelative">
		<ul class="nav nav-tabs">
			<li class="acttitle active">
				<a href="${ctx}/act/task/todo/">我的任务
					<span class="redword">1</span>
					<span class="origeword">5</span>
					<span class="greenword">12</span>
				</a>
			</li>
			<li class="acttitle">
				<a href="${ctx}/act/task/historic/">我的关注
					<span class="redword">10</span>
					<span class="origeword">50</span>
					<span class="greenword">12</span>
				</a>
			</li>
			<li class="acttitle">
				<a href="${ctx}/act/task/process/">待生产库</a>
			</li>
		</ul>
		<div class="act-top-right positionrabsolute">
			<a href="###">完成的订单</a>
			<button type="button" class="btn" id="openselectModal"><i class="icon-filter"></i></button>
		</div>
	</div>
	<div id="selectModal" class="select-modal">
		<div class="select-wrap clear">
			<div class="select-catgry">
				<dl class="sca">
					<dt>订单编号</dt>
					<dd>
						<select id="selectOrderId" class="select-orderid"></select>
					</dd>
				</dl>
			</div>
			<div class="select-catgry">
				<dl class="sca">
					<dt>任务状态</dt>
					<dd>
						<label><input type="checkbox" name="">&nbsp;全部</label>
					</dd>
					<dd>
						<label><input type="checkbox" name="">&nbsp;正常</label>
					</dd>
					<dd>
						<label><input type="checkbox" name="">&nbsp;即将到期</label>
					</dd>
					<dd>
						<label><input type="checkbox" name="">&nbsp;超时</label>
					</dd>
				</dl>
			</div>
			<div class="select-catgry">
				<dl class="sca">
					<dt>订单加急</dt>
					<dd>
						<label><input type="checkbox" name="">&nbsp;全部</label>
					</dd>
					<dd>
						<label><input type="checkbox" name="">&nbsp;加急</label>
					</dd>
				</dl>
			</div>
			<div class="select-catgry">
				<dl class="sca">
					<dt>商品类型</dt>
					<dd>
						<label><input type="checkbox" name="">&nbsp;全部</label>
					</dd>
					<dd>
						<label><input type="checkbox" name="">&nbsp;聚引客</label>
					</dd>
					<dd>
						<label><input type="checkbox" name="">&nbsp;常来客</label>
					</dd>
					<dd>
						<label><input type="checkbox" name="">&nbsp;智慧店铺</label>
					</dd>
					<dd>
						<label><input type="checkbox" name="">&nbsp;代运营</label>
					</dd>
				</dl>
			</div>
			<div class="select-catgry">
				<dl class="sca">
					<dt>商户</dt>
					<dd>
						<select id="selectOrderId" class="select-orderid"></select>
					</dd>
				</dl>
			</div>
		</div>
		<div class="select-btn-wrap">
			<button type="button" class="btn btn-primary msrue">查找</button>
			<button type="button" class="btn nosrue">取消</button>
		</div>
	</div>
	<div class="act-wrap">
		<div class="act-top-title clear">
			<div class="title-span inline-block">订单</div>
			<div class="title-span inline-block">任务</div>
			<div class="title-span inline-block">时间</div>
		</div>
		<div class="act-content-wrap clear">
			<div class="act-content-box clear">
				<div class="act-content-slide act-content-slide-left positionrelative">
					<div class="act-tips-word positionrabsolute"><span>急</span></div>
					<div class="info-item">订单：12345678913631331</div>
					<div class="info-item">广州美丽的餐饮连锁店广州美丽的餐饮连锁店</div>
					<div class="info-item">购买的商品类型—商品名称（服务商/商户）联系方式</div>
					<div class="info-item">购买时间：2017-09-29  20:12</div>
					<div class="info-item">投放日期：2017-10-11</div>
				</div>
				<div class="act-content-slide act-content-slide-right positionrelative">
					<a href="javascript:;" class="positionrabsolute dorpdownbotm">
						<i class="icon-chevron-down"></i>
					</a>
					<div class="act-rs-item">
						<div class="act-rs-left floatleft">
							<div class="act-rs-title padding15">
								<h3>拉群收集客户资料</h3>
								<button class="makesrue btn btn-primary">确认完成</button>
							</div>
							<div class="act-rs-form padding15">
								<div class="rs-label">
									<input type="checkbox" name="checkbox1">
									<label for="checkbox1">方案顾问自我介绍，以及详细服务流程</label>
								</div>
								<div class="rs-label">
									<input type="checkbox" name="checkbox2">
									<label for="checkbox2">审核开户资料</label>
								</div>
							</div>
						</div>
						<div class="act-rs-right">
							<div class="padding15">
								<div class="act-time">开始：09-20&nbsp;&nbsp;12:00</div>
								<div class="act-time">到期：12-20&nbsp;&nbsp;12:00</div>
								<div class="act-jd">
									<div class="progress progress-danger">
										<div class="bar" style="width: 100%"></div>
									</div>
								</div>
								<div class="act-jd-word positionrelative"><div class="acjw positionrabsolute" style="left: 86%;">100%</div></div>
							</div>
						</div>
					</div>
					<div class="act-rs-item">
						<div class="act-rs-left floatleft">
							<div class="act-rs-title padding15">
								<h3>拉群收集客户资料</h3>
								<button type="button" class="makesrue btn btn-primary">确认完成</button>
							</div>
							<div class="act-rs-form padding15">
								<div class="rs-label">
									<input type="checkbox" name="checkbox1">
									<label for="checkbox1">方案顾问自我介绍，以及详细服务流程</label>
								</div>
								<div class="rs-label">
									<input type="checkbox" name="checkbox2">
									<label for="checkbox2">审核开户资料</label>
								</div>
							</div>
						</div>
						<div class="act-rs-right">
							<div class="padding15">
								<div class="act-time">开始：09-20&nbsp;&nbsp;12:00</div>
								<div class="act-time">到期：12-20&nbsp;&nbsp;12:00</div>
								<div class="act-jd">
									<div class="progress progress-warning">
										<div class="bar" style="width: 80%"></div>
									</div>
								</div>
								<div class="act-jd-word positionrelative"><div class="acjw positionrabsolute" style="left: 77%;">80%</div></div>
							</div>
						</div>
					</div>
					<div class="act-rs-item">
						<div class="act-rs-left floatleft">
							<div class="act-rs-title padding15">
								<h3>拉群收集客户资料</h3>
								<button type="button" class="makesrue btn">任务完成</button>
							</div>
							<div class="act-rs-form padding15">
								<div class="rs-label">
									<input type="checkbox" name="checkbox1">
									<label for="checkbox1">方案顾问自我介绍，以及详细服务流程</label>
								</div>
								<div class="rs-label">
									<input type="checkbox" name="checkbox2">
									<label for="checkbox2">审核开户资料</label>
								</div>
							</div>
						</div>
						<div class="act-rs-right">
							<div class="padding15">
								<div class="act-time">开始：09-20&nbsp;&nbsp;12:00</div>
								<div class="act-time">到期：12-20&nbsp;&nbsp;12:00</div>
								<div class="act-jd">
									<div class="progress progress-success">
										<div class="bar" style="width: 50%"></div>
									</div>
								</div>
								<div class="act-jd-word positionrelative"><div class="acjw positionrabsolute" style="left: 47%;">50%</div></div>
							</div>
						</div>
					</div>
				</div>
			</div>
					<div class="act-content-box-detail clear">
			<div class="act-content-box clear">
				<div class="act-content-slide act-content-slide-left positionrelative">
					<div class="act-tips-word positionrabsolute"><span>急</span></div>
					<div class="info-item">订单：12345678913631331</div>
					<div class="info-item">广州美丽的餐饮连锁店广州美丽的餐饮连锁店</div>
					<div class="info-item">购买的商品类型—商品名称（服务商/商户）联系方式</div>
					<div class="info-item">购买时间：2017-09-29  20:12</div>
					<div class="info-item">投放日期：2017-10-11</div>
				</div>
				<div class="act-content-slide act-content-slide-right positionrelative">
					<a href="javascript:;" class="positionrabsolute dorpdownbotm">
						<i class="icon-chevron-down"></i>
					</a>
					<div class="act-rs-item">
						<div class="act-rs-left floatleft">
							<div class="act-rs-title padding15">
								<h3>拉群收集客户资料</h3>
								<button class="makesrue btn btn-primary">确认完成</button>
							</div>
							<div class="act-rs-form padding15">
								<div class="rs-label">
									<input type="checkbox" name="checkbox1">
									<label for="checkbox1">方案顾问自我介绍，以及详细服务流程</label>
								</div>
								<div class="rs-label">
									<input type="checkbox" name="checkbox2">
									<label for="checkbox2">审核开户资料</label>
								</div>
							</div>
						</div>
						<div class="act-rs-right">
							<div class="padding15">
								<div class="act-time">开始：09-20&nbsp;&nbsp;12:00</div>
								<div class="act-time">到期：12-20&nbsp;&nbsp;12:00</div>
								<div class="act-jd">
									<div class="progress progress-success">
										<div class="bar" style="width: 50%"></div>
									</div>
								</div>
								<div class="act-jd-word positionrelative"><div class="acjw positionrabsolute" style="left: 47%;">50%</div></div>
							</div>
						</div>
					</div>
					
				</div>
			</div>
			<div class="act-content-box clear">
				<div class="act-content-slide act-content-slide-left positionrelative">
					<div class="act-tips-word positionrabsolute"><span>急</span></div>
					<div class="info-item">订单：12345678913631331</div>
					<div class="info-item">广州美丽的餐饮连锁店广州美丽的餐饮连锁店</div>
					<div class="info-item">购买的商品类型—商品名称（服务商/商户）联系方式</div>
					<div class="info-item">购买时间：2017-09-29  20:12</div>
					<div class="info-item">投放日期：2017-10-11</div>
				</div>
				<div class="act-content-slide act-content-slide-right positionrelative">
					<a href="javascript:;" class="positionrabsolute dorpdownbotm">
						<i class="icon-chevron-down"></i>
					</a>
					<div class="act-rs-item">
						<div class="act-rs-left floatleft">
							<div class="act-rs-title padding15">
								<h3>拉群收集客户资料</h3>
								<button class="makesrue btn btn-primary">确认完成</button>
							</div>
							<div class="act-rs-form padding15">
								<div class="rs-label">
									<input type="checkbox" name="checkbox1">
									<label for="checkbox1">方案顾问自我介绍，以及详细服务流程</label>
								</div>
								<div class="rs-label">
									<input type="checkbox" name="checkbox2">
									<label for="checkbox2">审核开户资料</label>
								</div>
							</div>
						</div>
						<div class="act-rs-right">
							<div class="padding15">
								<div class="act-time">开始：09-20&nbsp;&nbsp;12:00</div>
								<div class="act-time">到期：12-20&nbsp;&nbsp;12:00</div>
								<div class="act-jd">
									<div class="progress progress-warning">
										<div class="bar" style="width: 80%"></div>
									</div>
								</div>
								<div class="act-jd-word positionrelative"><div class="acjw positionrabsolute" style="left: 77%;">80%</div></div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- <form:form id="searchForm" modelAttribute="act" action="${ctx}/act/task/todo/" method="get" class="breadcrumb form-search">
		<div>
			<label>流程类型：&nbsp;</label>
			<form:select path="procDefKey" class="input-medium">
				<form:option value="" label="全部流程"/>
				<form:options items="${fns:getDictList('act_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			<label>创建时间：</label>
			<input id="beginDate"  name="beginDate"  type="text" readonly="readonly" maxlength="20" class="input-medium Wdate" style="width:163px;"
				value="<fmt:formatDate value="${act.beginDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
				　--　
			<input id="endDate" name="endDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate" style="width:163px;"
				value="<fmt:formatDate value="${act.endDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
			&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		</div>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>标题</th>
				<th>当前环节</th><%--
				<th>任务内容</th> --%>
				<th>流程名称</th>
				<th>流程版本</th>
				<th>创建时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="act">
				<c:set var="task" value="${act.task}" />
				<c:set var="vars" value="${act.vars}" />
				<c:set var="procDef" value="${act.procDef}" /><%--
				<c:set var="procExecUrl" value="${act.procExecUrl}" /> --%>
				<c:set var="status" value="${act.status}" />
				<tr>
					<td>
						<c:if test="${empty task.assignee}">
							<a href="javascript:claim('${task.id}');" title="签收任务">${fns:abbr(not empty act.vars.map.title ? act.vars.map.title : task.id, 60)}</a>
						</c:if>
						<c:if test="${not empty task.assignee}">
							<a href="${ctx}/act/task/form?taskId=${task.id}&taskName=${fns:urlEncode(task.name)}&taskDefKey=${task.taskDefinitionKey}&procInsId=${task.processInstanceId}&procDefId=${task.processDefinitionId}&status=${status}">${fns:abbr(not empty vars.map.title ? vars.map.title : task.id, 60)}</a>
						</c:if>
					</td>
					<td>
						<a target="_blank" href="${pageContext.request.contextPath}/act/diagram-viewer?processDefinitionId=${task.processDefinitionId}&processInstanceId=${task.processInstanceId}">${task.name}</a>
					</td><%--
					<td>${task.description}</td> --%>
					<td>${procDef.name}</td>
					<td><b title='流程版本号'>V: ${procDef.version}</b></td>
					<td><fmt:formatDate value="${task.createTime}" type="both"/></td>
					<td>
						<c:if test="${empty task.assignee}">
							<a href="javascript:claim('${task.id}');">签收任务</a>
						</c:if>
						<c:if test="${not empty task.assignee}"><%--
							<a href="${ctx}${procExecUrl}/exec/${task.taskDefinitionKey}?procInsId=${task.processInstanceId}&act.taskId=${task.id}">办理</a> --%>
							<a href="${ctx}/act/task/form?taskId=${task.id}&taskName=${fns:urlEncode(task.name)}&taskDefKey=${task.taskDefinitionKey}&procInsId=${task.processInstanceId}&procDefId=${task.processDefinitionId}&status=${status}">任务办理</a>
						</c:if>
						<shiro:hasPermission name="act:process:edit">
							<c:if test="${empty task.executionId}">
								<a href="${ctx}/act/task/deleteTask?taskId=${task.id}&reason=" onclick="return promptx('删除任务','删除原因',this.href);">删除任务</a>
							</c:if>
						</shiro:hasPermission>
						<a target="_blank" href="${pageContext.request.contextPath}/act/diagram-viewer?processDefinitionId=${task.processDefinitionId}&processInstanceId=${task.processInstanceId}">跟踪</a><%-- 
						<a target="_blank" href="${ctx}/act/task/trace/photo/${task.processDefinitionId}/${task.executionId}">跟踪2</a> 
						<a target="_blank" href="${ctx}/act/task/trace/info/${task.processInstanceId}">跟踪信息</a> --%>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table> -->
	<script type="text/javascript">
		$(document).ready(function() {
			var act = {
				bindEvent: function() {
					$('body').on('click', '.dorpdownbotm', function(event) {
						event.preventDefault();
						$(this).toggleClass('up');
						$(this).parents('.act-content-box').toggleClass('uppansl');
						$(this).siblings('.act-rs-item').eq(0).show().toggleClass('show');
					}).on('click', '#openselectModal', function(event) {
						//打开选择框
						event.preventDefault();
						$('html, body').addClass('hidesroll');
						$("#selectModal").addClass('show');
					}).on('click', '.nosrue', function(event) {
						event.preventDefault();
						$('html, body').removeClass('hidesroll');
						$("#selectModal").removeClass('show');
						//关闭选择框
					}).on('click', '.msrue', function(event) {
						//0选择框提交
						event.preventDefault();
						$('html, body').removeClass('hidesroll');
						$("#selectModal").removeClass('show');
					});
				}
			};
			act.bindEvent();
		});
		/**
		 * 签收任务
		 */
		function claim(taskId) {
			$.get('${ctx}/act/task/claim' ,{taskId: taskId}, function(data) {
				if (data == 'true'){
		        	top.$.jBox.tip('签收完成');
		            location = '${ctx}/act/task/todo/';
				}else{
		        	top.$.jBox.tip('签收失败');
				}
		    });
		}
	</script>
</body>
</html>
