<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>我的关注</title>
	<meta name="decorator" content="default"/>
	<link href="${ctxStatic}/common/act.css?v=aswcdfg85d5f" type="text/css" rel="stylesheet" />
</head>
<body>
	<div class="act-top positionrelative">
		<ul class="nav nav-tabs">
			<li class="acttitle">
				<a href="${ctx}/act/task/todo/">我的任务
					<span class="redword">1</span>
					<span class="origeword">5</span>
					<span class="greenword">12</span>
				</a>
			</li>
			<li class="acttitle active">
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
			<div class="title-span inline-block" style="width: 25%;">订单</div>
			<div class="title-span inline-block" style="width: 50%;">任务</div>
			<div class="title-span inline-block" style="width: 8%;">负责人</div>
			<div class="title-span inline-block" style="width: 17%;">时间</div>
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
								<span class="makesrue">小小牛</span>
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
								<span class="makesrue">小小牛</span>
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
