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
		<!-- <ul class="nav nav-tabs">
			<li class="acttitle active"><a href="${ctx}/workflow/tasklist">我的任务
					<span class="redword">${flowStatForm.taskOverTime }</span> <span
					class="origeword">${flowStatForm.taskOnlyWillOvertime }</span> <span
					class="greenword">${flowStatForm.taskNormal }</span>
			</a></li>
			<li class="acttitle"><a href="${ctx}/workflow/followTaskList">我的关注
					<span class="redword">${flowStatForm.followOverTime }</span> <span
					class="origeword">${flowStatForm.followOnlyWillOvertime }</span> <span
					class="greenword">${flowStatForm.followNormal }</span>
			</a></li>
			<li class="acttitle"><a
				href="${ctx}/workflow/pendingProductionTaskList">待生产库</a></li>
		</ul> -->
		<ul class="nav nav-tabs">
			<li class="acttitle" ><a href="${ctx}/workflow/teamtasklist">团队任务</a></li>
			<li class="acttitle "><a href="${ctx}/workflow/teamtasklist">处理中</a></li>
			<li class="acttitle active"><a href="${ctx}/workflow/productionTeamTaskList">待生产</a></li>
			<li class="acttitle select-team-li">
				<select class="select-team" id="select-team" style="width: 160px;">
					<option value="1">所有团队</option>
					<c:forEach var="team" items="${team}">
					 <option value="${team.id}" <c:if test="${team.id eq selectteam}">selected="selected"</c:if>>${team.teamName}</option>
					</c:forEach> 
				</select>
				<input type="hidden" id="selectteam" value="${selectteam }">
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
		<form:form id="workFlowQuery" modelAttribute="workFlowQueryForm" action="${ctx}/workflow/tasklist" method="post" class="form-horizontal">
			<sys:message content="${message}" />
			<form:hidden path="taskStateList" name="taskStateList"
				id="taskStateList" />
			<form:hidden path="goodsType" name="goodsTypeList" id="goodsTypeList" />
			<div id="selectModal" class="select-modal">
				<div class="select-wrap clear">
					<div class="select-catgry">
						<dl class="sca">
							<dt>订单编号</dt>
							<dd>
								<form:select path="orderNumber" class="input-medium">
									<form:option value="" label="请选择" />
									<form:options items="${list }" itemLabel="orderNo"
										itemValue="orderNo" htmlEscape="false" />
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
								<label><input type="checkbox" name="taskState" value="2">&nbsp;即将到期</label>
							</dd>
							<dd>
								<label><input type="checkbox" name="taskState" value="3">&nbsp;超时</label>
							</dd>
						</dl>
					</div>
					<div class="select-catgry">
						<dl class="sca">
							<dt>订单加急</dt>
							<dd>
								<label><input type="checkbox" value="1" name="hurryFlag">&nbsp;加急</label>
							</dd>
						</dl>
					</div>
					<div class="select-catgry">
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
					<div class="select-catgry">
						<dl class="sca">
							<dt>商户名称:</dt>
							<dd>
								<form:input path="shopName" htmlEscape="false" maxlength="100" />
							</dd>
						</dl>
					</div>
				</div>
				<div class="select-btn-wrap">
					<input id="btnSubmit" class="btn btn-primary" type="submit"
						value="查找" />&nbsp;
					<button type="reset" class="btn btn-warning">重置</button>&nbsp;
					<button type="button" class="btn nosrue">取消</button>
				</div>
			</div>
		</form:form>
		<div class="act-top-title clear">
			<div class="title-span inline-block">订单 (&nbsp;${fn:length(list)}&nbsp;)</div>
			<div class="title-span inline-block">任务</div>
			<div class="title-span inline-block">时间</div>
		</div>
		<div class="act-content-wrap clear">

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
						<a href="javascript:;"
							class="positionrabsolute dorpdownbotms up">
							<i class="icon-chevron-down"></i></a>

						<c:forEach items="${flowFrom.subTaskStrList }" var="i">
							<div class="act-rs-item tasklist-item-wrap productionTeamTaskList">${i.subTaskDetail}</div>
						</c:forEach>
					</div>
				</div>
			</c:forEach>
		</div>
	</div>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#select-team").change(function(){
				  var val=$(this).val();
				  location="${ctx}/workflow/productionTeamTaskList?selectteam="+val;
				 
				 });
			
			
			var act = {
				bindEvent : function() {
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
				   	});
				}
			};
			act.bindEvent();

			$(".act-process-left-footer").hide();
			
			$("#workFlowQuery").validate({
				submitHandler: function(form){
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
		function syncSelect(taskId, taskUser, userId){
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
						$.jBox.info(data);*/
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
		}
	</script>
</body>
</html>
