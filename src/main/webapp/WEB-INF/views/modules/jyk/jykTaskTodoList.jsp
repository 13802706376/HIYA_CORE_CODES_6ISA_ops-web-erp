<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>我的任务</title>
<meta name="decorator" content="default" />
<link href="${ctxStatic}/common/act.css?v=s4sd145we5vcs652" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/common/act.js?v=efe51d5s6"></script>
</head>
<body>
	<div class="act-top positionrelative">
		<ul class="nav nav-tabs">
			<li class="acttitle active"><a href="${ctx}/act/task/todo/">我的任务
					<span class="redword">1</span> <span class="origeword">5</span> <span
					class="greenword">12</span>
			</a></li>
			<li class="acttitle"><a href="${ctx}/act/task/historic/">我的关注
					<span class="redword">10</span> <span class="origeword">50</span> <span
					class="greenword">12</span>
			</a></li>
			<li class="acttitle"><a href="${ctx}/act/task/process/">待生产库</a>
			</li>
		</ul>
		<div class="act-top-right positionrabsolute">
			<a href="###">完成的订单</a>
			<button type="button" class="btn" id="openselectModal">
				<i class="icon-filter"></i>
			</button>
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
		
			<c:forEach items="${list}" var="flowFrom">
				<div class="act-content-box clear">
					<div class="act-content-slide act-content-slide-left positionrelative">
						<c:if test="${flowFrom.hurryFlag =='1'}">
							<div class="act-tips-word positionrabsolute">
								<span>急</span>
							</div>
						</c:if>
						
						<div class="info-item">订单：${flowFrom.orderNo }</div>
						<div class="info-item">${flowFrom.shopName }</div>
						<div class="info-item">${flowFrom.goodType }--${flowFrom.goodName }*${flowFrom.goodCount }</div>
						<div class="info-item">${flowFrom.contactWay }</div>
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
					<a href="javascript:;" class="positionrabsolute dorpdownbotm">
						<i class="icon-chevron-down"></i>
					</a>
					<div class="act-rs-item">
						${flowFrom.subTaskStr }
					</div>
				</div>
				</div>
			</c:forEach>
		</div>
	</div>
	<script type="text/javascript">
		$(document).ready(
				function() {
					var act = {
						bindEvent : function() {
							$('body').on(
									'click',
									'.dorpdownbotm',
									function(event) {
										event.preventDefault();
										$(this).toggleClass('up');
										$(this).parents('.act-content-box')
												.toggleClass('uppansl');
										$(this).siblings('.act-rs-item').eq(0)
												.show().toggleClass('show');
									}).on('click', '#openselectModal',
									function(event) {
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
			$.get('${ctx}/act/task/claim', {
				taskId : taskId
			}, function(data) {
				if (data == 'true') {
					top.$.jBox.tip('签收完成');
					location = '${ctx}/act/task/todo/';
				} else {
					top.$.jBox.tip('签收失败');
				}
			});
		}
	</script>
</body>
</html>
