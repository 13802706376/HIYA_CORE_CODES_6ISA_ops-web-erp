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
				<li class="acttitle "><a href="${ctx}/workflow/tasklist">我的任务
					<span id="taskOverTime" class="redword">${flowStatForm.taskOverTime }</span> <span id="taskOnlyWillOvertime"
 					class="origeword">${flowStatForm.taskOnlyWillOvertime }</span> <span id="taskNormal"
					class="greenword">${flowStatForm.taskNormal }</span>
			</a></li>
			<li class="acttitle active"><a href="${ctx}/workflow/followTaskList">我的关注
					<span class="redword">${flowStatForm.followOverTime }</span> <span
					class="origeword">${flowStatForm.followOnlyWillOvertime }</span> <span
					class="greenword">${flowStatForm.followNormal }</span>
			</a></li>
				<shiro:hasPermission name="tasklist:pendingProductionTaskList">
			<li class="acttitle"><a href="${ctx}/workflow/pendingProductionTaskList">待生产库</a>
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
			<form:hidden path="taskStateList" name="taskStateList" id="taskStateList"/>
			<form:hidden path="goodsType" name="goodsTypeList"  id="goodsTypeList"/>
			<div id="selectModal" class="select-modal">
				<div class="select-wrap clear">
					<div class="select-catgry">
						<dl class="sca">
							<dt>订单编号</dt>
							<dd>
							<form:select path="orderNumber" class="input-medium">
								<form:option value="" label="请选择"/>
								<form:options items="${list }" itemLabel="orderNo" itemValue="orderNo" htmlEscape="false"/>
							</form:select>
						</dl>
					</div>
					<div class="select-catgry">
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
					<div class="select-catgry">
						<dl class="sca">
							<dt>订单加急</dt>
							<dd>
								<label><form:checkbox path="hurryFlag" value="1"/>&nbsp;加急</label>
							</dd>
						</dl>
					</div>
					<div class="select-catgry">
						<dl class="sca">
							<dt>商品类型</dt>
							<c:forEach items="${categoryList}" var="good">
								<dd>
									<label><input type="checkbox" name="goodsTypoList" value="${good.id }" >&nbsp;${good.name}</label>							</dd>
							</c:forEach>
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
					 <button type="reset" class="btn  btn-warning">重置</button>&nbsp;
					<button type="button" class="btn nosrue">取消</button>
				</div>
			</div>
		</form:form>
		<div class="act-top-title clear">
			<div class="title-span inline-block" style="width: 25%;">订单 (&nbsp;${fn:length(list)}&nbsp;)</div>
			<div class="title-span inline-block" style="width: 50%;">任务</div>
			<div class="title-span inline-block" style="width: 8%;">负责人</div>
			<div class="title-span inline-block" style="width: 17%;">时间</div>
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
						<div class="act-content-slide act-content-slide-right positionrelative">
							<a href="javascript:;" class="positionrabsolute dorpdownbotm up">
								<i class="icon-chevron-down"></i>
							</a>
							<c:forEach items="${flowFrom.subTaskStrList }" var="i">
								<div class="act-rs-item tasklist-item-wrap myFollowTaskList">${i.subTaskDetail}</div>
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
							<a href="javascript:;" class="positionrabsolute dorpdownbotm up">
								<i class="icon-chevron-down"></i>
							</a>
							<c:forEach items="${flowFrom.subTaskStrList }" var="i">
								<div class="act-rs-item tasklist-item-wrap myFollowTaskList">${i.subTaskDetail}</div>
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
							<a href="javascript:;" class="positionrabsolute dorpdownbotm up">
								<i class="icon-chevron-down"></i>
							</a>
							<c:forEach items="${flowFrom.subTaskStrList }" var="i">
								<div class="act-rs-item tasklist-item-wrap myFollowTaskList">${i.subTaskDetail}</div>
							</c:forEach>
						</div>
					</div>
				</c:if>
			</c:forEach>
		</div>
	</div>
	<script type="text/javascript">
		sessionStorage.removeItem('taskListFormData');
		$(document).ready(function() {
			var act = {
				init: function() {
					this.chk($("#taskStateList").val(), "taskState");
					this.chk($("#goodsTypeList").val(), "goodsTypoList");
					this.followTaskCount();
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
					});
				},
				setFormData() {
					var taskListFormData = sessionStorage.getItem('taskListFormData');
					if (taskListFormData) {
						var o = JSON.parse(taskListFormData);
						$('#pageNo').val(o.pageNo);
						$('#pageSize').val(o.pageSize);
						$('#taskStateList').val(o.taskStateList);
						$('#goodsTypeList').val(o.goodsType);
						$('#orderNumber').val(o.orderNumber);
						$('#shopName').val(o.shopName);
						if (o._hurryFlag === 'on') {
							$('#hurryFlag1').attr('checked', true);
						} else {
							$('#hurryFlag1').removeAttr('checked');
						}
						this.chk(o.taskStateList, "taskState");
						this.chk(o.goodsType, "goodsTypoList");
					}
				},
				chk: function() {
					var list = list.split(",");
					for (var val in list) {
						if (list[val]) {
							$("input[name='"+chkName+"'][value='"+list[val]+"']").prop("checked", true);
						}
					}
				},
				followTaskCount: function() {
					$.ajax({  
		                type: "GET",  
		                url: "${ctx}/workflow/followTaskCount",  
		                success: function(data) {  
		                    if(data){
		                    	$("#taskOverTime").text(data.taskOverTime);
		                    	$("#taskOnlyWillOvertime").text(data.taskOnlyWillOvertime);
		                    	$("#taskNormal").text(data.taskNormal);
		                    }
		                }  
		            });
				},
				setTaskListFormData: function() {
					var formDta = {
						pageNo: $('#pageNo').val(),
						pageSize: $('#pageSize').val(),
						taskStateList: $('#taskStateList').val(),
						goodsType: $('#goodsTypeList').val(),
						orderNumber: $('#orderNumber').val(),
						_hurryFlag: $('#hurryFlag1').attr('checked') ? 'on' : '',
						hurryFlag: $('#hurryFlag1').attr('checked') ? '1' : '',
						shopName: $.trim($('#shopName').val())
					}
					sessionStorage.setItem('taskListFormData', JSON.stringify(formDta));
				}
			};
			act.init();
			
			$("#workFlowQuery").validate({
				submitHandler: function(form){
					sessionStorage.removeItem('taskListFormData');
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
					form.submit();
					act.setTaskListFormData()
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
