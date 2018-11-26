<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%> 
<html>
<head>
	<title>我的关注</title>
	<meta name="decorator" content="default"/>
	<link href="${ctxStatic}/common/act.css?v=dsfd3213d16d165dce6" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="${ctxStatic}/common/act.js?v=${staticVersion}"></script>
	
</head>
<body>
	<div class="act-top positionrelative">
		<ul class="nav nav-tabs">
			<li class="acttitle">
				<a href="${ctx}/workflow/tasklist">我的任务
					<span id="taskOverTime" class="redword">0</span> 
					<span id="taskOnlyWillOvertime" class="origeword">0</span> 
					<span id="taskNormal" class="greenword">0</span>
				</a>
			</li>
			<li class="acttitle active">
				<a href="${ctx}/workflow/followTaskList">我的关注
					<span id="followOverTime" class="redword">0</span> 
					<span id="followOnlyWillOvertime" class="origeword">0</span> 
					<span id="followNormal" class="greenword">0</span>
				</a>
			</li>
			<shiro:hasPermission name="tasklist:pendingProductionTaskList">
			<li class="acttitle">
				<a href="${ctx}/workflow/pendingProductionTaskList">待生产库
					<span id="pendingProductionOverTime" class="redword">0</span> 
					<span id="pendingProductionOnlyWillOvertime" class="origeword">0</span> 
					<span id="pendingProductionNormal" class="greenword">0</span>
				</a>
			</li>
			</shiro:hasPermission>
		</ul>
		<div class="act-top-right positionrabsolute">
			<button type="button" class="btn" id="openselectModal"><i class="icon-filter"></i></button>
		</div>
	</div>
	<div class="act-wrap">
		<form:form id="workFlowQuery" modelAttribute="workFlowQueryForm" action="${ctx}/workflow/followTaskList" method="post" class="form-horizontal">
			<sys:message content="${message}"/>
			<form:hidden path="pageNo" name="pageNo" id="pageNo" value="1"/>
			<form:hidden path="pageSize" name="pageSize" id="pageSize"/>
			<form:hidden path="taskStateList" name="taskStateList" id="taskStateList"/>
			<form:hidden path="goodsType" name="goodsTypeList"  id="goodsTypeList"/>
			<div id="selectModal" class="select-modal">
				<div class="select-wrap clear">
					<div class="select-catgry">
						<dl class="sca">
							<dt>订单编号</dt>
							<dd>
							<!--<form:select path="orderNumber" class="input-medium">
								<form:option value="" label="请选择"/>
								<form:options items="${list }" itemLabel="orderNo" itemValue="orderNo" htmlEscape="false"/>
							</form:select>-->
							<form:input path="orderNumber" htmlEscape="false" maxlength="100" id="orderNumber" style="width:95%"/>
						</dl>
					</div>
					<div class="select-catgry" style="width:10%;">
						<dl class="sca">
							<dt>任务状态</dt>
								<dd>
									<label><input type="checkbox" name="taskState" value="1">&nbsp;正常</label>
								</dd>
								<dd>
									<label><input type="checkbox"  name="taskState" value="2">&nbsp;即将到期</label>
								</dd>
								<dd>
									<label><input type="checkbox"  name="taskState" value="3">&nbsp;超时</label>
								</dd>
						</dl>
					</div>
					<div class="select-catgry" style="width:10%;">
						<dl class="sca">
							<dt>订单加急</dt>
							<dd>
								<label><form:checkbox path="hurryFlag" value="1"/>&nbsp;加急</label>
							</dd>
						</dl>
					</div>
					<div class="select-catgry" style="width:10%;">
						<dl class="sca">
							<dt>商品类型</dt>
							<c:forEach items="${categoryList}" var="good">
								<dd>
									<label><input type="checkbox" name="goodsTypoList" value="${good.id }" >&nbsp;${good.name}</label>							</dd>
							</c:forEach>
						</dl>
					</div>
					<div class="select-catgry" style="width:10%;">
						<dl class="sca">
							<dt>排序</dt>
							<dd>
								<form:radiobutton path="orderBy" value="orderDate"/>默认<br/>
								<form:radiobutton path="orderBy" value="shopName"/>商户名称
							</dd>
						</dl>
					</div>
					<div class="select-catgry">
						<dl class="sca">
							<dt>商户名称:</dt>
							<dd>
								<dd><form:input path="shopName" htmlEscape="false" maxlength="100" /></dd>
							</dd>
						</dl>
					</div>
				</div>
				<div class="select-btn-wrap">
					 <input id="btnSubmit" class="btn btn-primary" type="submit" value="查找"/>&nbsp;
					 <button type="reset" class="btn  btn-warning" id="resetBtn">重置</button>&nbsp;
					<button type="button" class="btn nosrue">取消</button>
				</div>
			</div>
		</form:form>
		<div class="act-top-title clear">
			<div class="title-span inline-block" style="width: 25%;">订单 ( <span id="orderCount">0</span> )</div>
			<div class="title-span inline-block" style="width: 50%;">任务</div>
			<div class="title-span inline-block" style="width: 8%;">负责人</div>
			<div class="title-span inline-block" style="width: 17%;">到期</div>
		</div>
		<div class="act-content-wrap clear hide" id="actContentWrapper">

			<c:forEach items="${list}" var="flowFrom">
				<c:if test="${flowFrom.flowMark=='jyk_flow'}">
					<div class="act-content-box myFollowTaskListCb clear istoggle">
						<div class="act-content-slide act-content-slide-left positionrelative">
							<c:if test="${flowFrom.hurryFlag =='1'}">
								<div class="act-tips-word positionrabsolute">
									<span>急</span>
								</div>
							</c:if>
							<div class="info-item-show">订单：${flowFrom.orderNo }</div>
							<div class="info-item">${flowFrom.shopName }</div>
							<div class="info-item">${flowFrom.taskDisplay }</div>
							<div class="info-item">(${fns:getDictLabel(flowFrom.orderType, 'orderType', '未知')})&nbsp;${flowFrom.contactWay }</div>
							<div class="info-item">
								购买时间：
								<fmt:formatDate value="${flowFrom.orderTime}" type="both"
									pattern="yyyy-MM-dd HH:mm:ss" />
							</div>
							<div class="info-item">
								投放日期：
								<fmt:formatDate value="${flowFrom.deliveryTime}" type="both"
									pattern="yyyy-MM-dd HH:mm:ss" />
							</div>
						</div>
						<div class="act-content-slide act-content-slide-right positionrelative">
							
							<c:forEach items="${flowFrom.subTaskStrList }" var="i">
								<div class="act-rs-left floatleft">
									<div class="act-rs-title padding15">
										<h3><a href="../workflow/taskDetail?detailType=followTaskList&taskId=${i.taskId}&readOnly=true">${i.taskName}</a></h3>
										<a href="javascript:;"  style="float: right;">${i.assigneeName}</a>
									</div>
								</div>
								<div class="act-rs-right">
									<div class="padding15"  style="height: 60px;">
										<div class="act-time">开始：${i.startDate}</div>
										<div class="act-time">到期：${i.endDate}</div>
										<div class="act-jd">
											<div class="">${i.endDate}</div>
											<c:choose>
												<c:when test="${i.subTaskConsumTime < 80}">
													<div class="progress progress-success">
														<div class="bar" style="width: ${i.subTaskConsumTime}%"></div>
													</div>
												</c:when>
												<c:when test="${i.subTaskConsumTime >= 80 && i.subTaskConsumTime < 100}">
													<div class="progress progress-warning">
														<div class="bar" style="width: ${i.subTaskConsumTime}%"></div>
													</div>
												</c:when>
												<c:otherwise>
													<div class="progress progress-danger">
														<div class="bar" style="width: 100%"></div>
													</div>
												</c:otherwise>
											</c:choose> 
										</div>
										<div class="act-jd-word positionrelative"><div class="acjw positionrabsolute" style="left: 80%;">${i.subTaskConsumTime}%</div></div>
									</div>
								</div>
							</c:forEach>
						</div>
					</div>
				</c:if>
				
				<c:if test="${flowFrom.flowMark=='sdi_flow'}">
					<div class="act-content-box myFollowTaskListCb clear istoggle">
						<div class="act-content-slide act-content-slide-left positionrelative">
							<div class="info-item-show">
								订单：${flowFrom.orderNo }
							</div>
							<div class="info-item">${flowFrom.shopName }</div>
							<div class="info-item">${flowFrom.goodName }</div>
							<div class="info-item">(${fns:getDictLabel(flowFrom.orderType, 'orderType', '未知')})&nbsp;${flowFrom.contactWay }</div>
							
							<div class="info-item">
								购买时间：
								<fmt:formatDate value="${flowFrom.orderTime}" type="both"
									pattern="yyyy-MM-dd HH:mm:ss" />
							</div>
							<div class="info-item">
								
							</div>
						</div>
	
						<div class="act-content-slide act-content-slide-right positionrelative">
							
							<c:forEach items="${flowFrom.subTaskStrList }" var="i">
								<div class="act-rs-left floatleft">
									<div class="act-rs-title padding15" >
										<h3><a href="../workflow/taskDetail?detailType=followTaskList&taskId=${i.taskId}&readOnly=true">${i.taskName}</a></h3> 
										<a href="javascript:;"  style="float: right;">${i.assigneeName}</a>
									</div>
									
								</div>
								<div class="act-rs-right">
									<div class="padding15" style="height: 60px;">
										<div class="act-time">开始：${i.startDate}</div>
										<div class="act-time">到期：${i.endDate}</div>
										<div class="act-jd">
											<div class="">${i.endDate}</div>
											<c:choose>
												<c:when test="${i.subTaskConsumTime < 80}">
													<div class="progress progress-success">
														<div class="bar" style="width: ${i.subTaskConsumTime}%"></div>
													</div>
												</c:when>
												<c:when test="${i.subTaskConsumTime >= 80 && i.subTaskConsumTime < 100}">
													<div class="progress progress-warning">
														<div class="bar" style="width: ${i.subTaskConsumTime}%"></div>
													</div>
												</c:when>
												<c:otherwise>
													<div class="progress progress-danger">
														<div class="bar" style="width: 100%"></div>
													</div>
												</c:otherwise>
											</c:choose> 
										</div>
										<div class="act-jd-word positionrelative"><div class="acjw positionrabsolute" style="left: 80%;">${i.subTaskConsumTime}%</div></div>
									</div>
								</div> 
							</c:forEach>
						</div>
					</div>
				</c:if>
				
				<c:if test="${flowFrom.flowMark=='payInto_flow'}">
					<div class="act-content-box myFollowTaskListCb clear istoggle">
						<div class="act-content-slide act-content-slide-left positionrelative">
							<div class="info-item-show">
								进件名称：${flowFrom.orderNo}
							</div>
							<div class="info-item">${flowFrom.shopName }</div>
							<div class="info-item">${flowFrom.goodName }</div>
							<!-- <div class="info-item">(${fns:getDictLabel(flowFrom.orderType, 'orderType', '未知')})&nbsp;${flowFrom.contactWay }</div> -->
							
							<div class="info-item">
								提交时间：
								<fmt:formatDate value="${flowFrom.orderTime}" type="both"
									pattern="yyyy-MM-dd HH:mm:ss" />
							</div>
							<div class="info-item">
								
							</div>
						</div>
	
						<div class="act-content-slide act-content-slide-right positionrelative">
							
							<c:forEach items="${flowFrom.subTaskStrList }" var="i">
								<div class="act-rs-left floatleft">
									<div class="act-rs-title padding15">
										<h3><a href="../workflow/taskDetail?detailType=followTaskList&taskId=${i.taskId}&readOnly=true">${i.taskName}</a></h3>
									</div>
								</div>
								<div class="act-rs-right">
									<div class="padding15"  style="height: 60px;">
										<div class="act-time">开始：${startDate}</div>
										<div class="act-time">到期：${endDate}</div>
										<div class="act-jd">
											<div class="">${endDate}</div>
											<c:choose>
												<c:when test="${i.subTaskConsumTime < 80}">
													<div class="progress progress-success">
														<div class="bar" style="width: ${i.subTaskConsumTime}%"></div>
													</div>
												</c:when>
												<c:when test="${i.subTaskConsumTime >= 80 && i.subTaskConsumTime < 100}">
													<div class="progress progress-warning">
														<div class="bar" style="width: ${i.subTaskConsumTime}%"></div>
													</div>
												</c:when>
												<c:otherwise>
													<div class="progress progress-danger">
														<div class="bar" style="width: 100%"></div>
													</div>
												</c:otherwise>
											</c:choose> 
										</div>
										<div class="act-jd-word positionrelative"><div class="acjw positionrabsolute" style="left: 80%;">${i.subTaskConsumTime}%</div></div>
									</div>
								</div> 
							</c:forEach>
						</div>
					</div>
				</c:if>
			</c:forEach>
		</div>
		<%@ include file="page.jsp" %>
	</div>
	<script type="text/javascript">
		function submit(flag) {
			sessionStorage.removeItem('followTaskListData');
			var taskState=""; 
			var goodsTypeList ="";
			$('input[name="taskState"]:checked').each(function(){ 
				taskState+=$(this).val()+","; 
			}); 
			$('input[name="goodsTypoList"]:checked').each(function(){ 
				goodsTypeList+=$(this).val()+","; 
			});
			$('#goodsTypeList').val(goodsTypeList);
			$('#taskStateList').val(taskState);
			loading('正在提交，请稍等...');
			if (flag) {
				setTaskListFormData();
				$("#workFlowQuery").submit();
			}
			$('#actContentWrapper').show();
		}

		function setTaskListFormData() {
			var formDta = {
				pageNo: $('#pageNo').val(),
				pageSize: $('#pageSize').val(),
				taskStateList: $('#taskStateList').val(),
				goodsType: $('#goodsTypeList').val(),
				//orderNumber: $('#orderNumber').val(),
				_hurryFlag: $('#hurryFlag1').attr('checked') ? 'on' : '',
				hurryFlag: $('#hurryFlag1').attr('checked') ? '1' : '',
				//shopName: $.trim($('#shopName').val())
			}
			sessionStorage.setItem('followTaskListData', JSON.stringify(formDta));
		}

		$(document).ready(function() {
			/*var pn = getQueryString('pageNo', location.href);
			if (!pn) {
				sessionStorage.removeItem('followTaskListData');
			}*/
			var act = {
				init: function() {
					this.chk($("#taskStateList").val(), "taskState");
					this.chk($("#goodsTypeList").val(), "goodsTypoList");
					this.followTaskCount();
					this.setFormData();
					this.bindEvent();
				},
				bindEvent: function() {
					$('#actContentWrapper').show();
					$('body').on('click', '.dorpdownbotm', function(event) {
						event.preventDefault();
					    $(this).toggleClass('up');
					    $(this).parents('.act-content-box').toggleClass('istoggle');
					    $(this).siblings('.act-rs-item').eq(0).show();
						/*$(this).toggleClass('up');
						$(this).parents('.act-content-box').toggleClass('uppansl');
						$(this).siblings('.act-rs-item').eq(0).show().toggleClass('show');*/
					}).on('click', '#openselectModal', function(event) {
						//打开选择框
						event.preventDefault();
						$("#selectModal").slideToggle();
					}).on('click', '.nosrue', function(event) {
						event.preventDefault();
						$("#selectModal").slideToggle();
						//关闭选择框
					}).on('click', '.msrue', function(event) {
						//0选择框提交
						//event.preventDefault();
						$("#selectModal").slideToggle();
					}).on('click', '#resetBtn', function() {
						$('#pageNo').val('1');
						$('#pageSize').val('15');
						$('#taskStateList').val('');
						$('#goodsTypeList').val('');
						$('#orderNumber').val('');
						$('#shopName').val('');
						$('#teamUserIdsearchForm').val('');
						$('#teamIdsearchForm').val('');
						$('#selectTeamUserIdsearchForm').val('');
						$('#hurryFlag1').removeAttr('checked');
						sessionStorage.removeItem('followTaskListData');
						return true;
				   	});
				},
				chk: function(list, chkName) {
					var list = list.split(",");
					for (var val in list) {
						if (list[val]) {
							$("input[name='"+chkName+"'][value='"+list[val]+"']").prop("checked", true);
						}
					}
				},
				setFormData: function() {
					var followTaskListData = sessionStorage.getItem('followTaskListData');
					if (followTaskListData) {
						var o = JSON.parse(followTaskListData);
						$('#pageNo').val(o.pageNo);
						$('#pageSize').val(o.pageSize);
						$('#taskStateList').val(o.taskStateList);
						$('#goodsTypeList').val(o.goodsType);
						//$('#orderNumber').val(o.orderNumber);
						//$('#shopName').val(o.shopName);
						if (o._hurryFlag === 'on') {
							$('#hurryFlag1').attr('checked', true);
						} else {
							$('#hurryFlag1').removeAttr('checked');
						}
						this.chk(o.taskStateList, "taskState");
						this.chk(o.goodsType, "goodsTypoList");
					}
				},
				followTaskCount: function() {
					$.ajax({
		                type: "GET",
		                url: "${ctx}/workflow/getTodoTaskStat",
		                /* data: $('#workFlowQuery').serialize(), */
		                success: function (result) {
		                    if (result) {
		                    	/* $("#orderCount").text(result.orderCount); */
			                    $("#taskNormal").text(result.flowStatForm.taskNormal);
			                	$("#taskOnlyWillOvertime").text(result.flowStatForm.taskOnlyWillOvertime);
			                	$("#taskOverTime").text(result.flowStatForm.taskOverTime);
		                    }
		                },
		                error: function(data) {
		                    // console.log("error:"+data.responseText);
		                }
		            });
					
					$.ajax({
		                type: "GET",
		                url: "${ctx}/workflow/getFollowTaskStat",
		                data: $('#workFlowQuery').serialize(),
		                success: function (result) {
		                    if (result) {
		                    	$("#orderCount").text(result.orderCount);
			                    $("#followNormal").text(result.flowStatForm.taskNormal);
			                	$("#followOnlyWillOvertime").text(result.flowStatForm.taskOnlyWillOvertime);
			                	$("#followOverTime").text(result.flowStatForm.taskOverTime);
		                    }
		                },
		                error: function(data) {
		                    console.log("error:"+data.responseText);
		                }
		            });
				}
			};
			act.init();

			$(document).on('click', '#btnSubmit', function() {
				var taskState = ""; 
				var goodsTypeList ="";
				$('input[name="taskState"]:checked').each(function(){ 
					taskState += $(this).val()+",";
				}); 
				$('input[name="goodsTypoList"]:checked').each(function(){ 
					goodsTypeList += $(this).val()+",";
				});

				$('#pageNo').val('1');
				
				$('#goodsTypeList').val(goodsTypeList);
				$('#taskStateList').val(taskState);
				loading('正在提交，请稍等...');
				$("#workFlowQuery").submit();
				$('#actContentWrapper').show();
			});

			$("#workFlowQuery").validate({
				submitHandler: function(form){
					sessionStorage.removeItem('followTaskListData');
					submit(false);
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

			/* $.ajax({  
                type: "GET",  
                url: "${ctx}/workflow/followTaskCount",  
                success: function(data) {  
                    if(data){
                    	$("#taskOverTime").text(data.taskOverTime);
                    	$("#taskOnlyWillOvertime").text(data.taskOnlyWillOvertime);
                    	$("#taskNormal").text(data.taskNormal);
                    }
                }  
            }); */
			<shiro:hasPermission name="tasklist:pendingProductionTaskList">
				$.ajax({
	                type: "GET",
	                url: "${ctx}/workflow/getPendingProductionTaskStat",
	                /* data: $('#workFlowQuery').serialize(), */
	                success: function (result) {
	                    if (result) {
	                    	/* $("#orderCount").text(result.orderCount); */
		                    $("#pendingProductionNormal").text(result.flowStatForm.taskNormal);
		                	$("#pendingProductionOnlyWillOvertime").text(result.flowStatForm.taskOnlyWillOvertime);
		                	$("#pendingProductionOverTime").text(result.flowStatForm.taskOverTime);
	                    }
	                },
	                error: function(data) {
	                    console.log("error:"+data.responseText);
	                }
	            });
			</shiro:hasPermission>
		});
		
	</script>
</body>
</html>