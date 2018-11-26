<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>运营服务</title>
<meta name="decorator" content="default" />
<link href="${ctxStatic}/common/act.css?v=dsfd3213d16d165dce6" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/common/act.js?v=${staticVersion}"></script>
<style type="text/css">
	.overflow-visible{overflow: visible;}
	.overflow-visible .act-tab-menu{ height: 38px; overflow: hidden; }
	.act-tab-btngroup{top: 0; right: 5px; display: none;}
	.act-tab-btngroup .dropdown-menu{width: 220px; left: -180px; z-index: 10;}
	.act-tab-btngroup .dropdown-menu .acttitle a span{padding-left: 0px;}
</style>
</head>
<body>
	<div class="act-top positionrelative overflow-visible">
		<div class="act-tab-menu">
			<ul class="nav nav-tabs" id="nav_nav_tabs">			
				<shiro:hasPermission name="tab:service_startup:view"> <!-- 服务启动 -->
					<li class="acttitle <c:if test="${act_type == 'service_startup'}">active</c:if>" id="service_startup">
						<a href="${ctx}/workflow/3p25/tasklist?actType=service_startup">服务启动<span>&nbsp;</span></a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="tab:account_pay_open:view"><!-- 账号和支付开通服务 -->	
					<li class="acttitle <c:if test="${act_type == 'account_pay_open'}">active</c:if>" id="account_pay_open">
						<a href="${ctx}/workflow/3p25/tasklist?actType=account_pay_open">账号和支付开通服务<span>&nbsp;</span></a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="tab:marketing_planning:view"><!-- 营销策划服务 -->
					<li class="acttitle <c:if test="${act_type == 'marketing_planning'}">active</c:if>" id="marketing_planning">
						<a href="${ctx}/workflow/3p25/tasklist?actType=marketing_planning">营销策划服务<span>&nbsp;</span></a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="tab:training_service:view"><!-- 培训服务 -->
					<li class="acttitle <c:if test="${act_type == 'training_service'}">active</c:if>" id="training_service">
						<a href="${ctx}/workflow/3p25/tasklist?actType=training_service">培训服务<span>&nbsp;</span></a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="tab:material_service:view"><!-- 物料服务 -->
					<li class="acttitle <c:if test="${act_type == 'material_service'}">active</c:if>" id="material_service">
						<a href="${ctx}/workflow/3p25/tasklist?actType=material_service">物料服务<span>&nbsp;</span></a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="tab:aftersale_visit:view"><!-- 售后上门服务 -->
					<li class="acttitle <c:if test="${act_type == 'aftersale_visit'}">active</c:if>" id="aftersale_visit">
						<a href="${ctx}/workflow/3p25/tasklist?actType=aftersale_visit">售后上门服务<span>&nbsp;</span></a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="tab:advertiser_open:view"><!-- 广告主开通服务 -->
					<li class="acttitle <c:if test="${act_type == 'advertiser_open'}">active</c:if>" id="advertiser_open">
						<a href="${ctx}/workflow/3p25/tasklist?actType=advertiser_open">广告主开通服务<span>&nbsp;</span></a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="tab:jyk_delivery:view"><!-- 聚引客上门交付服务 -->
					<li class="acttitle <c:if test="${act_type == 'jyk_delivery'}">active</c:if>" id="jyk_delivery">
						<a href="${ctx}/workflow/3p25/tasklist?actType=jyk_delivery">聚引客上门交付服务<span>&nbsp;</span></a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="tab:review_visit:view"><!-- 审核上门服务申请 -->
					<li class="acttitle <c:if test="${act_type == 'review_visit'}">active</c:if>" id="review_visit">
						<a href="${ctx}/workflow/3p25/tasklist?actType=review_visit">审核上门服务申请<span>&nbsp;</span></a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="tab:wisdom_shop:view"><!-- 智慧餐厅服务 -->
					<li class="acttitle <c:if test="${act_type == 'wisdom_shop'}">active</c:if>" id="wisdom_shop">
						<a href="${ctx}/workflow/3p25/tasklist?actType=wisdom_shop">智慧餐厅安装交付服务<span>&nbsp;</span></a>
					</li>
				</shiro:hasPermission>
			</ul>
			<div class="act-tab-btngroup positionrabsolute" id="workflowListActTabBtngroup">
				<div class="act-tabs btn-group">
					<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
						<i class="icon-align-justify"></i>
					</a>
					<ul class="dropdown-menu">
						<shiro:hasPermission name="tab:material_service:view"><!-- 物料服务 -->
							<li class="acttitle <c:if test="${act_type == 'material_service'}">active</c:if>" id="material_service_hide">
								<a href="${ctx}/workflow/3p25/tasklist?actType=material_service">物料服务（<span>&nbsp;</span>）</a>
							</li>
						</shiro:hasPermission>
						<shiro:hasPermission name="tab:aftersale_visit:view"><!-- 售后上门服务 -->
							<li class="acttitle <c:if test="${act_type == 'aftersale_visit'}">active</c:if>" id="aftersale_visit_hide">
								<a href="${ctx}/workflow/3p25/tasklist?actType=aftersale_visit">售后上门服务（<span>&nbsp;</span>）</a>
							</li>
						</shiro:hasPermission>
						<shiro:hasPermission name="tab:advertiser_open:view"><!-- 广告主开通服务 -->
							<li class="acttitle <c:if test="${act_type == 'advertiser_open'}">active</c:if>" id="advertiser_open_hide">
								<a href="${ctx}/workflow/3p25/tasklist?actType=advertiser_open">广告主开通服务（<span>&nbsp;</span>）</a>
							</li>
						</shiro:hasPermission>
						<shiro:hasPermission name="tab:jyk_delivery:view"><!-- 聚引客上门交付服务 -->
							<li class="acttitle <c:if test="${act_type == 'jyk_delivery'}">active</c:if>" id="jyk_delivery_hide">
								<a href="${ctx}/workflow/3p25/tasklist?actType=jyk_delivery">聚引客上门交付服务（<span>&nbsp;</span>）</a>
							</li>
						</shiro:hasPermission>
						<shiro:hasPermission name="tab:review_visit:view"><!-- 审核上门服务申请 -->
							<li class="acttitle <c:if test="${act_type == 'review_visit'}">active</c:if>" id="review_visit_hide">
								<a href="${ctx}/workflow/3p25/tasklist?actType=review_visit">审核上门服务申请（<span>&nbsp;</span>）</a>
							</li>
						</shiro:hasPermission>
						<shiro:hasPermission name="tab:wisdom_shop:view"><!-- 智慧餐厅服务 -->
							<li class="acttitle <c:if test="${act_type == 'wisdom_shop'}">active</c:if>" id="wisdom_shop_hide">
								<a href="${ctx}/workflow/3p25/tasklist?actType=wisdom_shop">智慧餐厅安装交付服务（<span>&nbsp;</span>）</a>
							</li>
						</shiro:hasPermission>
					</ul>
				</div>
			</div>
		</div>
	</div>
	<div></div>
	<div class="act-wrap">
		<form:form id="workFlowQuery" modelAttribute="workFlowQueryForm" action="${ctx}/workflow/3p25/tasklist" method="post" class="breadcrumb form-search">
			<sys:message content="${message}" />
			<form:hidden path="pageNo" name="pageNo" id="pageNo" value="1"/>
			<form:hidden path="pageSize" name="pageSize" id="pageSize"/>
			<form:hidden path="actType" name="actType" id="actType"/>
			<ul class="ul-form">
				<li>
					<label>订单编号：</label>
					<form:input path="orderNumber" htmlEscape="false" maxlength="100" id="orderNumber" style="width:150px"/>
				</li>
				<li>
					<label>商户名称：</label>
					<form:input path="shopName" htmlEscape="false" maxlength="100" id="shopName" style="width:150px" />
				</li>
				<li>
					<label>任务：</label>
					<form:select style="width:210px;"  path="taskRef">
						<option value="" label="请选择" >请选择</option>
						 <c:forEach items="${tasks}" var="asmap">  
						 		<form:option value="${asmap.taskRef}">${asmap.taskName}</form:option>
		                </c:forEach>
					</form:select>
				</li>
				<li>
					<label>任务状态：</label>
					<form:select style="width:150px;"  path="taskState">
						<option value="" label="请选择" >请选择</option>
						<form:option value="1">&nbsp;正常</form:option>
						<form:option value="2">&nbsp;即将到期</form:option>
						<form:option value="3">&nbsp;到期</form:option>
					</form:select>
				</li>
				<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="筛选" /></li>
				<li class="btns"><button type="reset" class="btn btn-warning" id="resetBtn">重置</button></li>
			</ul>
		</form:form>
		<div class="act-top-title clear">
			<div class="title-span inline-block">订单</div>
			<div class="title-span inline-block">任务</div>
			<div class="title-span inline-block">到期</div>
		</div>
		<div class="act-content-wrapper clear" id="actContentWrapper">
			<c:forEach items="${list}" var="flowFrom">
				<div class="act-content-box act-content-flowfrom clear">
					<div class="act-content-slide act-content-slide-left positionrelative">
						<c:if test="${flowFrom.hurryFlag == '1'}">
							<div class="act-tips-word positionrabsolute">
								<span>急</span>
							</div>
						</c:if>
						<div class="info-item-show">
							<c:choose>
								<c:when test="${flowFrom.procInsKey=='unionpay_intopieces_flow' || flowFrom.procInsKey=='wechatpay_intopieces_flow'}">
									进件名称：${flowFrom.number }
								</c:when>
								<c:when test="${flowFrom.procInsKey=='friends_promotion_flow'}">
									开户名称：朋友圈开户_${flowFrom.number }
								</c:when>
								<c:when test="${flowFrom.procInsKey=='microblog_promotion_flow'}">
									开户名称：微博开户_${flowFrom.number }
								</c:when>
								<c:when test="${flowFrom.procInsKey=='visit_service_flow'}">
									商户名称:${flowFrom.sName }
								</c:when>
								<c:otherwise>
									订单：${flowFrom.number }
								</c:otherwise>
							</c:choose>		
						</div>
						<div class="info-item">
							<c:choose>
								<c:when test="${flowFrom.procInsKey=='visit_service_flow'}">
								</c:when>
								<c:otherwise>
									商户名称:${flowFrom.sName }
								</c:otherwise>
							</c:choose>	
						</div>
						
					</div>
					<div class="act-content-slide act-content-slide-right positionrelative">		
						<c:forEach items="${flowFrom.equipment }" var="i">
							<div class="act-rs-item tasklist-item-wrap">
								<h3 class="task-name" style="font-size:20px;">
									<c:choose>
										<c:when test="${i.flowMark == 'history' }">
											<!-- 历史流程跳转历史订单详情页面 -->
											<a href="../../workflow/taskDetail?taskId=${i.taskId}&procInsId=${i.procInsId}&detailType=operatingServices&actType=${act_type}&taskDefKey=${i.taskRef}&processDefineKey=${i.processDefineKey}&&taskName=${i.taskName}" class="totaskdetailpage">${i.taskName}</a>	
										</c:when>
										<c:otherwise>
											<a href="../../workflow/3p25/taskDetail?taskId=${i.taskId}&procInsId=${i.procInsId}&detailType=operatingServices&actType=${act_type}&taskDefKey=${i.taskRef}&processDefineKey=${i.processDefineKey}&&taskName=${i.taskName}" class="totaskdetailpage">${i.taskName}</a>
										</c:otherwise>
									</c:choose>									
								</h3>
								<div class="task-procins-wrap">
									<div class="task-date" title="2018-11-11 — 2018:11:11"><fmt:formatDate pattern="MM-dd HH:mm" value="${i.taskEndDate}" /></div>
									<c:if test="${i.taskConsumTime < 80}">
										<div class="progress progress-success">
											<div class="bar" style="width: ${i.taskConsumTime}%"></div>
										</div>
									</c:if>
									<c:if test="${i.taskConsumTime >= 80 && i.taskConsumTime < 100}">
										<div class="progress progress-warning">
											<div class="bar" style="width: ${i.taskConsumTime}%"></div>
										</div>
									</c:if>
									<c:if test="${i.taskConsumTime >= 100}">
										<div class="progress progress-danger">
											<div class="bar" style="width: 100%" title="${i.taskConsumTime}%"></div>
										</div>
									</c:if>
									<div class="procins-value" title="${i.taskConsumTime}%">${i.taskConsumTime}%</div>
									<%--<c:choose>
										<c:when test="${i.taskConsumTime < 80}">
											<div class="progress progress-success">
												<div class="bar" style="width: ${i.taskConsumTime}%"></div>&nbsp;${i.taskConsumTime}%
											</div>
										</c:when>
										<c:when test="${i.taskConsumTime > 80 && i.taskConsumTime < 10000}">
											<div class="progress progress-warning">
												<div class="bar" style="width: ${i.taskConsumTime}%"></div>&nbsp;${i.taskConsumTime}%
											</div>
										</c:when>
										 <c:otherwise>
											<div class="progress progress-danger">
												<div class="bar" style="width: 100%"></div>&nbsp;${i.taskConsumTime}%
											</div>
										</c:otherwise> 
									</c:choose> --%>
								</div>
							</div>
						</c:forEach>
					</div>
				</div>
			</c:forEach>
		</div>
		<%@ include file="page.jsp" %>
	</div>
	
	<script type="text/javascript">
		//=====================start
		function submit(flag) {
			loading('正在提交，请稍等...');
			if (flag) {
				$("#workFlowQuery").submit();
			}
			$('#actContentWrapper').show();
		}

		$(document).ready(function() {
			operatingStatistics();
			setFormData();
			removeSessionStorageByName('beiyi_task_list_page');
			var beiyi_task_flow_to_where = getSessionStorageByName('beiyiTaskFlowToWhere')
			if (!beiyi_task_flow_to_where) {
				setSessionStorage('beiyiTaskFlowToWhere', location.pathname/*+'?actType=service_startup'*/);
			}
			//新增数据统计
			function operatingStatistics() {
					$.ajax({  
		            type: "GET",  
		            url: "${ctx}/workflow/3p25/operatingStatistics",  
		            success: function(data) {  
		                if(data){
		                	var jsonStr = JSON.parse(data);
		                	jsonStr.forEach(function(value){
		                		var o = $("span",$("#"+value.name));
		                		var ob = $("span",$("#"+value.name+"_hide"));
		                		if(o) o.html(value["total"]);
		                		if(ob) ob.html(value["total"]);
		                	});
		                	
		                }
		            }  
		        });
			}

	        function setSessionStorage(name, contxt) {
	        	top.globalUtil.sessionStorageSet(name, contxt)
	        }

	        function removeSessionStorageByName(name) {
	        	top.globalUtil.sessionStorageRemove(name)
	        }

	        function getSessionStorageByName(name) {
	        	top.globalUtil.sessionStorageGet(name)
	        }

	        function setFormData() {
				var taskListFormData = getSessionStorageByName('taskListFormDataBeiyi');
				if (taskListFormData) {
					$('#pageNo').val(taskListFormData.pageNo);
					$('#pageSize').val(taskListFormData.pageSize);
					$('#actType').val(taskListFormData.actType);
					$('#orderNumber').val(taskListFormData.orderNumber);
					$('#shopName').val(taskListFormData.shopName);
					$('#taskRef').select2('val', taskListFormData.taskRef);
					$('#taskState').select2('val', taskListFormData.taskState);
				}
			}

	        function setTaskListFormData() {
	        	var formDta = {
	        		pageNo: $('#pageNo').val(),
					pageSize: $('#pageSize').val(),
					actType: $('#actType').val(),
					orderNumber: $('#orderNumber').val(),
					shopName: $('#shopName').val(),
					taskRef: $('#taskRef').val(),
					taskState: $('#taskState').val(),
				}
				setSessionStorage('taskListFormDataBeiyi', JSON.stringify(formDta));
	        }

	        $(document).on('click', '#nav_nav_tabs .acttitle a', function(event) {
	        	event.preventDefault();
	        	var href = $(this).attr('href');
	        	var actType = getQueryString('actType', href);
	        	$('#actType').val(actType);
	        	setSessionStorage('beiyiTaskFlowToWhere', href);
	        	setTaskListFormData();
	        	location.href = href;
	        }).on('click', '.totaskdetailpage', function(event) {
	        	event.preventDefault();
	        	var href = $(this).attr('href').split("&&")[0];
	        	setSessionStorage('beiyi_task_list_page', location.pathname);
	        	
	        	var taskName = $(this).attr('href').split("&&")[1];
	        	location.href = href+"&taskName="+encodeURI(encodeURI(taskName.split('=')[1]));
	        });

	        $("#workFlowQuery").validate({
				submitHandler: function(form) {
					removeSessionStorageByName('taskListFormDataBeiyi');
					submit(0);
					form.submit();
					setTaskListFormData();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});

		});
	</script>
</body>
</html>
