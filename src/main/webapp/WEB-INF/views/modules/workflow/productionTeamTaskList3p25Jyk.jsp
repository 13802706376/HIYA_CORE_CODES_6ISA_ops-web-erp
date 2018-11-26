<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>团队任务</title>
	<meta name="decorator" content="default" />
	<link href="${ctxStatic}/common/act.css?v=dsf66ds16ds" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="${ctxStatic}/common/act.js?v=sdfsdf1sdf2vg52s1f5ac"></script>
	<style type="text/css">
		.productionTeamTaskList .listSubmit .open-select-user{ display: none; }
	</style>
</head>
<body>
	<div class="act-top positionrelative">
		<ul class="nav nav-tabs">
			<li class="acttitle"> <a href="${ctx}/workflow/teamtasklist">团队任务</a></li>
			<%-- <li class="acttitle active"><a href="${ctx}/workflow/teamtasklist">处理中</a></li>
			<shiro:hasPermission name="tasklist:pendingProductionTaskList">
				<li class="acttitle"><a href="${ctx}/workflow/productionTeamTaskList">待生产</a></li>
			</shiro:hasPermission> --%>
			
			<li class="acttitle">
				<a href="${ctx}/workflow/teamtasklist">处理中
					<span id="taskOverTime" class="redword">0</span> 
					<span id="taskOnlyWillOvertime" class="origeword">0</span> 
					<span id="taskNormal" class="greenword">0</span>
				</a>
			</li>
			<shiro:hasPermission name="tasklist:pendingProductionTaskList">
			<li class="acttitle active">
				<a href="${ctx}/workflow/productionTeamTaskList">待生产
					<span id="pendingProductionOverTime" class="redword">0</span> 
					<span id="pendingProductionOnlyWillOvertime" class="origeword">0</span> 
					<span id="pendingProductionNormal" class="greenword">0</span>
				</a>
			</li>
			</shiro:hasPermission>
			<li class="acttitle select-team-li">
				<select class="select-team" id="select-team" style="width: 160px;">
					<option value="1">所有团队</option>
					<c:forEach var="team" items="${workFlowQueryForm.teams}">
					 	<option value="${team.id}" <c:if test="${team.id eq workFlowQueryForm.teamId}">selected="selected"</c:if>>${team.teamName}</option>
					</c:forEach> 
				</select>
				<input type="hidden" id="selectteam" value="${workFlowQueryForm.teamId }">
			</li>
			<li class="acttitle select-team-li">
				<select class="select-team hide" id="select-teamUser" style="width: 160px;"></select>
				<input type="hidden" id="selectteamUser" value="">
			</li>
		</ul>
		<div class="act-top-right positionrabsolute">
			<a href="${ctx}/workflow/status">完成的订单</a>
			<button type="button" class="btn" id="openselectModal">
				<i class="icon-filter"></i>
			</button>
		</div>
	</div>
	<div class="act-wrap">
		<form:form id="workFlowQuery" modelAttribute="workFlowQueryForm" action="${ctx}/workflow/productionTeamTaskList" method="post" class="form-horizontal">
			<sys:message content="${message}" />
			<form:hidden path="pageNo" name="pageNo" id="pageNo" value="1"/>
			<form:hidden path="pageSize" name="pageSize" id="pageSize"/>
			<form:hidden path="taskStateList" name="taskStateList" id="taskStateList" />
			<form:hidden path="goodsType" name="goodsTypeList" id="goodsTypeList" />
			<form:hidden path="teamId" name="teamId" id="teamId" />
			<form:hidden path="teamUserId" name="teamUserId" id="teamUserId" />
			<div id="selectModal" class="select-modal">
				<div class="select-wrap clear">
					<div class="select-catgry">
						<dl class="sca">
							<dt>订单编号</dt>
							<dd>
								<!--<form:select path="orderNumber" class="input-medium">
									<form:option value="" label="请选择" />
									<form:options items="${list }" itemLabel="orderNo"
										itemValue="orderNo" htmlEscape="false" />
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
								<label><input type="checkbox" name="taskState" value="2">&nbsp;即将到期</label>
							</dd>
							<dd>
								<label><input type="checkbox" name="taskState" value="3">&nbsp;超时</label>
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
									<label><input type="checkbox" name="goodsTypoList"
										value="${good.id }">&nbsp;${good.name}</label>
								</dd>
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
								<form:input path="shopName" htmlEscape="false" maxlength="100" />
							</dd>
						</dl>
						<%--<dl class="sca">
							<dt>负责人:</dt>
							<dd>
								<input type="text" value="" id="selectTeamUserIdsearchForm" name="teamUserName" maxlength="100" readonly="readonly" onclick="syncSelect()">
								<input type="hidden" name="teamUserId" id="teamUserIdsearchForm" value="">
								<input type="hidden" name="teamId" id="teamIdsearchForm" value="">
							</dd>
						</dl>--%>
					</div>
				</div>
				<div class="select-btn-wrap">
					<input id="btnSubmit" class="btn btn-primary" type="submit"
						value="查找" />&nbsp;
					<button type="reset" class="btn btn-warning" id="resetBtn">重置</button>&nbsp;
					<button type="button" class="btn nosrue">取消</button>
				</div>
			</div>
		</form:form>
		<div class="act-top-title clear">
			<div class="title-span inline-block">订单 (&nbsp;${fn:length(list)}&nbsp;)</div>
			<div class="title-span inline-block">任务</div>
			<div class="title-span inline-block">时间</div>
		</div>
		<div class="act-content-wrap clear hide" id="actContentWrapper">

			<c:forEach items="${list}" var="flowFrom">
				<div
					class="act-content-box act-content-flowfrom clear istoggle">
					<div
						class="act-content-slide act-content-slide-left positionrelative">
						<c:if test="${flowFrom.hurryFlag =='1'}">
							<div class="act-tips-word positionrabsolute">
								<span>急</span>
							</div>
						</c:if>
						<div class="info-item-show">
							订单：${flowFrom.orderNo }</a>
						</div>
						<div class="info-item">${flowFrom.shopName }</div>
						<div class="info-item">${flowFrom.goodType }--${flowFrom.goodName }*${flowFrom.goodCount }</div>
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

					<div
						class="act-content-slide act-content-slide-right positionrelative">
						
							<c:forEach items="${flowFrom.subTaskStrList }" var="i">
								<div class="act-rs-left floatleft">
									<div class="act-rs-title padding15">
										<h3><a href="../workflow/taskDetail?taskId=${i.taskId}&readOnly=true">${i.taskName}</a></h3>
									</div>
									
								</div>
								<div class="act-rs-right">
									<div class="padding15">
										<div class="act-time">开始：${i.startDate}</div>
										<div class="act-time">到期：${i.endDate}</div>
										<div class="act-jd">
											<div class="act-time">到期：${i.endDate}</div>
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
										<div class="act-jd-word positionrelative">
											<div class="acjw positionrabsolute" style="left: 80%;">${i.subTaskConsumTime}%</div>
											
										</div>
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

		function submit(flag) {
			sessionStorage.removeItem('taskListFormData');
			var taskState=""; 
			var goodsTypeList ="";
			$('input[name="taskState"]:checked').each(function(){ 
				taskState += $(this).val()+","; 
			}); 
			$('input[name="goodsTypoList"]:checked').each(function(){ 
				goodsTypeList += $(this).val()+","; 
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
				//shopName: $.trim($('#shopName').val()),
				teamUserId: $('#teamUserId').val(),
				teamId: $('#teamId').val()
			}
			sessionStorage.setItem('taskListFormData', JSON.stringify(formDta));
		}

		$(document).ready(function() {
			var act = {
				init: function() {
					this.getTodoTeamTaskStat();
					this.getTeamUser();
					this.chk($("#taskStateList").val(), "taskState");
					this.chk($("#goodsTypeList").val(), "goodsTypoList");
					this.setFormData();
					this.bindEvent();
				},
				bindEvent : function() {
					$(".act-process-left-footer").hide();
					$('#actContentWrapper').show();
					$('body').on('click', '.dorpdownbotms', function(event) {
						event.preventDefault();
					    $(this).toggleClass('up');
					    $(this).parents('.act-content-box').toggleClass('istoggle');
					    $(this).siblings('.act-rs-item').eq(0).show();//.toggleClass('isshow');
					})
				   	.on('click', '#openselectModal', function(event) {
					    //打开选择框
					    event.preventDefault();
					    $("#selectModal").slideToggle();
				   	})
				   	.on('click', '.nosrue', function(event) {
					    event.preventDefault();
					    $("#selectModal").slideToggle();
					    //关闭选择框
				   	})
				   	.on('click', '.msrue', function(event) {
					    //0选择框提交
					    //event.preventDefault();
					    $("#selectModal").slideToggle();
				   	})
				   	.on('mouseenter', '.act-content-flowfrom', function(event) {
				    	$(this).find('.act-tips-porint').hide();
				   	})
				   	.on('mouseleave', '.act-content-flowfrom', function(event) {
				    	$(this).find('.act-tips-porint').show();
				   	})
				   	.on('change', '#select-team', function(event) {
				   		var teamId = $(this).val();
						$('#teamUserId').val('');
						$('#pageNo').val('1');
						$('#pageSize').val('15');
			  			$("#teamId").val(teamId);
			  			submit(true);
				   	}).on('change', '#select-teamUser', function(event) {
				   		var teamUserId = $(this).val();
					  	$('#teamUserId').val(teamUserId);
					  	$('#pageNo').val('1');
						$('#pageSize').val('15');
					  	submit(true);
				   	}).on('click', '#resetBtn', function() {
				   		$('#pageNo').val('1');
						$('#pageSize').val('15');
						$('#taskStateList').val('');
						$('#goodsTypeList').val('');
						$('#orderNumber').val('');
						$('#shopName').val('');
						$('#hurryFlag1').removeAttr('checked');
						sessionStorage.removeItem('taskListFormData');
						return true;
				   	});
				},
				getTeamUser: function() {
					var tid = $('#selectteam').val();
					var url = '${ctx}/workflow/pushTeamAndUser/?selectteam='+tid;
					$.ajax({
		                type: "GET",
		                url: url,
		                success: function (result) {
		                	var str = ['<option value="">请选择责任人</option>'];
		                    if (result && Array.isArray(result)) {
		                    	for (var i = 0, l = result.length; i < l; i++) {
		                    		if (result[i].teamId === tid) {
		                    			var data = result[i].teamUser || [];
		                    			var taskListFormData = sessionStorage.getItem('taskListFormData') || '{}';
		                    			var o = JSON.parse(taskListFormData);
		                    			data.forEach(function(el, inx) {
		                    				if (o.teamUserId === el.userId) {
												str.push('<option value="'+el.userId+'" selected="selected">'+el.userName+'</option>')
											} else {
												str.push('<option value="'+el.userId+'">'+el.userName+'</option>');
											}
		                    			});
		                    			setTimeout(function() {
		                    				$('#select-teamUser').append(str.join('')).select2().show();
		                    			}, 50);
		                    		}
		                    	}
		                    }
		                },
		                error: function(data) {
		                    console.log("error:"+data);
		                }
		            });
				},
				setFormData: function() {
					var taskListFormData = sessionStorage.getItem('taskListFormData');
					if (taskListFormData) {
						var o = JSON.parse(taskListFormData);
						$('#pageNo').val(o.pageNo);
						$('#pageSize').val(o.pageSize);
						$('#taskStateList').val(o.taskStateList);
						$('#goodsTypeList').val(o.goodsType);
						//$('#orderNumber').val(o.orderNumber);
						//$('#shopName').val(o.shopName);
						$('#teamUserId').val(o.teamUserId);
						$('#teamId').val(o.teamId);
						/*$('#teamUserIdsearchForm').val(o.teamUserId);
						$('#teamIdsearchForm').val(o.teamId);
						$('#selectTeamUserIdsearchForm').val(o.teamUserName);*/
						if (o._hurryFlag === 'on') {
							$('#hurryFlag1').attr('checked', true);
						} else {
							$('#hurryFlag1').removeAttr('checked');
						}
						this.chk(o.taskStateList, "taskState");
						this.chk(o.goodsType, "goodsTypoList");
					}
				},
				chk: function(list, chkName) {
					var list = list.split(",");
					for (var val in list) {
						if (list[val]) {
							$("input[name='"+chkName+"'][value='"+list[val]+"']").prop("checked", true);
						}
					}
				},
				getTodoTeamTaskStat: function() {
					$.ajax({
		                type: "GET",
		                url: "${ctx}/workflow/getTodoTeamTaskStat",
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
		                    console.log("error:"+data.responseText);
		                }
		            });

					$.ajax({
		                type: "GET",
		                url: "${ctx}/workflow/getProductionTeamTaskStat",
		                data: $('#workFlowQuery').serialize(),
		                success: function (result) {
		                    if (result) {
		                    	$("#orderCount").text(result.orderCount);
			                    $("#pendingProductionNormal").text(result.flowStatForm.taskNormal);
			                	$("#pendingProductionOnlyWillOvertime").text(result.flowStatForm.taskOnlyWillOvertime);
			                	$("#pendingProductionOverTime").text(result.flowStatForm.taskOverTime);
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
					sessionStorage.removeItem('taskListFormData');
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
		});
		/*function syncSelect(taskId, taskUser, userId){
			var submit = function(v, h, f){
				var selectUserArray = sessionStorage.getItem('selectUserArray');
				var selectTeamAddUserArray = sessionStorage.getItem('selectTeamAddUserArray');
				var witchsObj = sessionStorage.getItem('witchsObj');
				var o = [];
				if (witchsObj && witchsObj === 'teamAndUser') {
					o = selectTeamAddUserArray ? selectTeamAddUserArray : [];
				} else {
					o = selectUserArray ? selectUserArray : [];
				}
				if (o.length === 0 || !o) {
					top.$.jBox.info('请选择人员！');
					return false;
				}
				setTimeout(function() {
					$.post('${ctx}/workflow/acceptTeamUser', {taskId: taskId, userOrTeamObj: o,userId:userId}, function(data, textStatus, xhr) {
						/*$.jBox.closeTip();
						$.jBox.info(data);
						location.reload();
					});
				}, 10)
			};
			var url = '${ctx}/workflow/skipUrl?taskId='+taskId+'&taskUser='+taskUser+'&userId='+userId+'&selectteam='+$('#selectteam').val();
			$.jBox('iframe:'+url, {
				id: 'selectTeamOrUserWrapper',
				title:'选择菜单',
				width:460,
				height: 520,
				submit: submit,
				showScrolling: false,
				iframeScrolling: 'no'
			});
			return;
		}*/
		function syncSelect(taskId, taskUser, userId) {
			var submit = function(v, h, f){
				/*var iframe = $(h).find("#jbox-iframe").contents(),
					selectUserValue = iframe.find("#selectUserTeamtask")*/
				var selectUserArray = sessionStorage.getItem('selectUserArray');
				var selectTeamAddUserArray = sessionStorage.getItem('selectTeamAddUserArray');
				var witchsObj = sessionStorage.getItem('witchsObj');
				var o = [];
				if ((witchsObj && witchsObj === 'teamAndUser') || !taskId) {
					o = selectTeamAddUserArray ? selectTeamAddUserArray : [];
				} else {
					o = selectUserArray ? selectUserArray : [];
				}
				if (taskId) {
					if (o.length === 0 || !o) {
						top.$.jBox.info('请选择人员！');
						return false;
					}
					setTimeout(function() {
						$.post('${ctx}/workflow/acceptTeamUser', {taskId: taskId, userOrTeamObj: o,userId:userId}, function(data, textStatus, xhr) {
							location.reload();
						});
					}, 10)
				} else {
					o = JSON.parse(o)
					$('#teamUserIdsearchForm').val(o[0]['userId'] ? o[0]['userId'] : '');
					$('#teamIdsearchForm').val(o[0]['teamId'] ? o[0]['teamId'] : '');
					$('#selectTeamUserIdsearchForm').val(o[0]['userName'] ? o[0]['userName'] : '');
				}
			};
			var url = '${ctx}/workflow/skipUrl?taskId='+taskId+'&taskUser='+taskUser+'&userId='+userId+'&selectteam='+$('#selectteam').val();
			top.$.jBox.open('iframe:'+url, "选择人员", 460, 520, {
				id: 'selectTeamOrUserWrapper',
				submit: submit,
				loaded : function(h){
					$(".jbox-content", top.document).css("overflow-y","hidden");
				}
			});
			return;
		}
	</script>
</body>
</html>
