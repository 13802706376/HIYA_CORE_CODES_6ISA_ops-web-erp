<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>待生产库</title>
	<meta name="decorator" content="default"/>
	<link href="${ctxStatic}/common/act.css?v=knmlsdfklknp" type="text/css" rel="stylesheet" />
</head>
<body>
	<!-- 待生产库 -->
	<div class="act-top positionrelative">
		<ul class="nav nav-tabs">
			<li class="acttitle">
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
			<li class="acttitle active">
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
			<div class="title-span inline-block" style="width: 58%;">任务</div>
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
								<h3>投放时间再确认</h3>
							</div>
							<div class="act-rs-form padding15">
								<p><a href="javascript:;" class="open-dialog">与客户再次确认投放时间</a></p>
							</div>
						</div>
						<div class="act-rs-right">
							<div class="padding15">
								<div class="act-time act-timer">开始：09-20&nbsp;&nbsp;12:00</div>
								<div class="act-time">到期：12-20&nbsp;&nbsp;12:00</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- 待生产库end -->

	<!-- 任务处理弹窗 -->
	<!-- <ul class="nav nav-tabs">
		<li class="acttitle active">
			<a href="${ctx}/act/task/todo/">任务</a>
		</li>
		<li class="acttitle">
			<a href="${ctx}/act/task/historic/">进度</a>
		</li>
	</ul>
	<div class="act-process">
		<div class="act-process-left floatleft">
			<h3 class="act-process-title">与客户沟通，确定最终投放时间</h3>
			<div class="form-horizontal">
				<div class="control-group">
					<label class="control-label">最终投放时间：</label>
					<div class="controls">
						<input id="startTime" name="startTime" type="text" readonly="readonly" maxlength="20" class="Wdate required" onclick=" WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});">
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">最终投放时间：</label>
					<div class="controls">
						<input type="text" maxlength="200" class="required">
					</div>
				</div>
			</div>
			<div class="act-process-left-footer">
				<div class="footer-info floatleft">
					<p>负责人：我</p>
					<p>09-07 12:00&nbsp;&nbsp;剩余：22小时</p>
				</div>
				<div class="footer-btn floatright">
					<button style="button" class="btn">确定完成</button>
				</div>
			</div>
		</div>
		<div class="act-process-right margin-right20">
			<div class="border-bottom-gray padding-left20 act-infos">
				<div class="padding-left0 act-infos-title">订单<span class="act-rword">急</span></div>
				<div class="padding-left0 info-item">订单：12345678913631331</div>
				<div class="padding-left0 info-item">广州美丽的餐饮连锁店广州美丽的餐饮连锁店</div>
				<div class="padding-left0 info-item">购买的商品类型—商品名称（服务商/商户）联系方式</div>
				<div class="padding-left0 info-item">购买时间：2017-09-29  20:12</div>
				<div class="padding-left0 info-item">投放日期：2017-10-11</div>
			</div>
			<div class="padding-left20 act-infos-other">
				<h3>任务相关资料</h3>
				<h4>输入项标题：</h4>
				<p>任务需求内容任务需求内容任务需求内容任务需求内容</p>
				<div class="border-bottom-gray padding-bottom10 act-infos-time">
					<span>大大明</span>
					<span class="inline-block floatright">2017-09-07 20:19</span>
				</div>
				<h4>设计稿：</h4>
				<p><a href="#####" class="act-link">任务需求内容任务需求内容任务需求内容任务需求内容.psd</a></p>
				<h4>投放方案：</h4>
				<p><a href="#####" class="act-link">任务需求内容任务需求内容任务需求内容任务需求内容.xls</a></p>
				<div class="border-bottom-gray padding-bottom10 act-infos-time">
					<span>大大明</span>
					<span class="inline-block floatright">2017-09-07 20:19</span>
				</div>
			</div>
		</div>
	</div> -->
	<!-- 任务处理弹窗end -->
	<!-- 任务进度 -->
	<!-- <ul class="nav nav-tabs">
		<li class="acttitle">
			<a href="${ctx}/act/task/todo/">任务</a>
		</li>
		<li class="acttitle active">
			<a href="${ctx}/act/task/historic/">进度</a>
		</li>
	</ul>
	<div class="act-process-timeline">
		<div class="act-process-timeline-left floatleft padding-lr-20">
			<div class="act-process-timeline-title border-bottom-gray">
				<div class="ptt-name inline-block width330">任务内容</div>
				<div class="ptt-name inline-block width120">负责人</div>
				<div class="ptt-name inline-block width150">时间</div>
			</div>
			<div class="act-process-timeline-item border-left-gray">
				<div class="timeline-wrap positionrelative">
					<div class="timeline-wrap-title wancheng border-bottom-gray">
						<div class="twt-title inline-block width330">开户资料收集与初审</div>
						<div class="twt-name inline-block width120">小明明</div>
						<div class="twt-name inline-block width150">2017-09-08 20:11</div>
					</div>
					<div class="timeline-wrap-content actc-list">
						<div class="width450 floatleft actc">
							<i class="icon-state-lsm icon-ok"></i>
							<span>规划与沟通最终投放时间</span>
						</div>
						<div class="twcontent-time margin-left450">2017-09-08 21:11</div>
					</div>
					<div class="timeline-wrap-content actc-list">
						<div class="width450 floatleft actc">
							<i class="icon-state-lsm icon-screenshot"></i>
							<span>规划与沟通最终投放时间</span>
						</div>
						<div class="twcontent-time margin-left450">2017-09-08 21:11</div>
					</div>
				</div>
	
				<div class="timeline-wrap positionrelative">
					<div class="timeline-wrap-title chulizh border-bottom-gray">
						<div class="twt-title inline-block width330">发VR发购水电费购地方购发二</div>
						<div class="twt-name inline-block width120">小明明</div>
						<div class="twt-name inline-block width150">2017-09-08 20:11</div>
					</div>
					<div class="timeline-wrap-content actc-list">
						<div class="width450 floatleft actc">
							<i class="icon-state-lsm icon-off"></i>
							<span>规划与沟通最终投放时间</span>
						</div>
						<div class="twcontent-time margin-left450">2017-09-08 21:11</div>
					</div>
					<div class="timeline-wrap-content actc-list">
						<div class="width450 floatleft actc">
							<i class="icon-state-lsm icon-screenshot"></i>
							<span>规划与沟通最终投放时间</span>
						</div>
						<div class="twcontent-time margin-left450">2017-09-08 21:11</div>
					</div>
				</div>
	
				<div class="timeline-wrap positionrelative">
					<div class="timeline-wrap-title liuzh border-bottom-gray">
						<div class="twt-title inline-block width330">refvrgvdfv电风扇购啊啊递四方二</div>
						<div class="twt-name inline-block width120">小明明sdgvrfgvwer</div>
						<div class="twt-name inline-block width150">2017-09-08 20:11</div>
					</div>
					<div class="timeline-wrap-content actc-list">
						<div class="width450 floatleft actc">
							<i class="icon-state-lsm icon-off"></i>
							<span>规划与沟通最终投放时间</span>
						</div>
						<div class="twcontent-time margin-left450">2017-09-08 21:11</div>
					</div>
					<div class="timeline-wrap-content actc-list">
						<div class="width450 floatleft actc">
							<i class="icon-state-lsm icon-screenshot"></i>
							<span>规划与沟通最终投放时间</span>
						</div>
						<div class="twcontent-time margin-left450">2017-09-08 21:11</div>
					</div>
				</div>
			</div>
		</div>
		<div class="act-process-right margin-left650 margin-right20">
			<div class="padding-left20 act-infos">
				<div class="padding-left0 act-infos-title">订单：12345678913631331<span class="act-rword">急</span></div>
				<div class="padding-left0 info-item">广州美丽的餐饮连锁店广州美丽的餐饮连锁店</div>
				<div class="padding-left0 info-item">购买的商品类型—商品名称（服务商/商户）联系方式</div>
				<div class="padding-left0 info-item">购买时间：2017-09-29  20:12</div>
				<div class="padding-left0 info-item">投放日期：2017-10-11</div>
			</div>
		</div>
	</div> -->
	<!-- 任务进度end -->


	<!-- 完成的订单 -->
	<!-- <div class="act-top positionrelative">
		<ul class="nav nav-tabs">
			<li class="acttitle active">
				<a href="${ctx}/act/task/process/">完成的订单</a>
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
	<div class="act-wrap act-completed-wrap">
		<div class="act-complate-title act-top-title">
			<span class="actt-top-ce inline-block">订单</span>
			<span class="actt-top-ce inline-block">商户</span>
			<span class="actt-top-ce inline-block">服务类型</span>
			<span class="actt-top-ce inline-block">时间</span>
		</div>
		<div class="act-complete-items">
			<div class="act-complete-item border-gray">
				<div class="complete-item-left floatleft actcp-left-wrap">
					<div class="floatleft actcp-left-wrap">
						<div class="clist-content">
							<p>订单：1236166546442151</p>
							<p>购买商品：轻聚引客</p>
							<p>购买时间：2017-09-29  20:11</p>
						</div>
					</div>
					<div class="act-cp-info act-cp-right">
						<div class="clist-content">
							<p>商户名称XXXXXXXXXXXX</p>
							<p>（服务商/商户）联系人-联系方式</p>
						</div>
					</div>
				</div>
				<div class="complete-item-right act-cp-right">
					<div class="floatleft actcp-left-wrap">
						<div class="clist-content">
							<p>聚引客</p>
							<p><a href="###">查看任务记录</a></p>
						</div>
					</div>
					<div class="act-cp-info act-cp-right">
						<div class="clist-content">
							<p>2017-09-29  20:11</p>
						</div>
					</div>
				</div>
			</div>
	
			<div class="act-complete-item border-gray">
				<div class="complete-item-left floatleft actcp-left-wrap">
					<div class="floatleft actcp-left-wrap">
						<div class="clist-content">
							<p>订单：1236166546442151</p>
							<p>购买商品：轻聚引客</p>
							<p>购买时间：2017-09-29  20:11</p>
						</div>
					</div>
					<div class="act-cp-info act-cp-right">
						<div class="clist-content">
							<p>商户名称XXXXXXXXXXXX</p>
							<p>（服务商/商户）联系人-联系方式</p>
						</div>
					</div>
				</div>
				<div class="complete-item-right act-cp-right">
					<div class="floatleft actcp-left-wrap">
						<div class="clist-content">
							<p>聚引客</p>
							<p><a href="###">查看任务记录</a></p>
						</div>
					</div>
					<div class="act-cp-info act-cp-right">
						<div class="clist-content">
							<p>2017-09-29  20:11</p>
						</div>
					</div>
				</div>
			</div>
	
			<div class="act-complete-item border-gray">
				<div class="complete-item-left floatleft actcp-left-wrap">
					<div class="floatleft actcp-left-wrap">
						<div class="clist-content">
							<p>订单：1236166546442151</p>
							<p>购买商品：轻聚引客</p>
							<p>购买时间：2017-09-29  20:11</p>
						</div>
					</div>
					<div class="act-cp-info act-cp-right">
						<div class="clist-content">
							<p>商户名称XXXXXXXXXXXX</p>
							<p>（服务商/商户）联系人-联系方式</p>
						</div>
					</div>
				</div>
				<div class="complete-item-right act-cp-right">
					<div class="floatleft actcp-left-wrap">
						<div class="clist-content">
							<p>聚引客</p>
							<p><a href="###">查看任务记录</a></p>
						</div>
					</div>
					<div class="act-cp-info act-cp-right">
						<div class="clist-content">
							<p>2017-09-29  20:11</p>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div> -->
	<!-- 完成的订单end -->
	<script type="text/javascript">
		$(document).ready(function() {
			var act = {
				bindEvent: function() {
					$('body').on('click', '.dorpdownbotm', function(event) {
						event.preventDefault();
						$(this).toggleClass('up');
						$(this).parents('.act-content-box').toggleClass('uppansl');
						$(this).siblings('.act-rs-item').eq(0).show().toggleClass('show');
						$(this).siblings('.act-rs-item').eq(0).find('.act-timer').show();
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
				},
				openDialog: function(){
					/*top.$.jBox("iframe:http://www.baidu.com", {
					    title: "百度一下",
					    width: 800,
					    height: 350,
					    buttons: { '关闭': true }
					});*/
					/*top.$.jBox('get:${ctx}/sys/menu/treeselect;JSESSIONID=<shiro:principal property="sessionid"/>', {title:'选择菜单', buttons:{'关闭':true}, width:300, height: 350, top:10});*/
				}
			};
			act.bindEvent();
			act.openDialog();
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
