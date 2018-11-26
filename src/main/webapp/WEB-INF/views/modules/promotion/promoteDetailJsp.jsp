.act-top .right-top{
			
		}
		.main-block{
			margin:10px 20px;
			border:1px solid #ddd;
			padding:15px;
		}
		.main-block .main-block-title{
			font-size:16px;
			font-weight:600;
			position:relative;
			margin-bottom:10px;
		}
		.main-block .main-block-title-sub{
			font-size:14px;
			font-weight:600;
			margin-bottom:10px;
		}
		.list-ul{
			margin-bottom:10px;
		}
		.list-ul>li{
			margin-bottom:5px;
			margin-right:10px;
		}
		.main-block .main-block-title .unfold-switch{
			position:absolute;
			right:0;
			top:0;
			display:block;
			width: 20px;
			height:20px;					
			cursor: pointer;
			/*background-color:#ddd;*/
			border: 1px solid #000;
			border-radius: 50%;
		}
		.main-block .main-block-title .unfold-switch:before{
			position:absolute;
			right:4px;							
		}
		.main-block .main-block-title .unfold-switch.unfold-up:before{
			top:8px;
			content:"";
			width: 0;
			height: 0;		
			border-width: 6px;			
			border-style: solid;	
			border-color: #2e2f2f transparent transparent transparent;
		}
		.main-block .main-block-title .unfold-switch.unfold-down:before{
			top:0;
			content:"";
			width: 0;
			height: 0;		
			border-width: 6px;			
			border-style: solid;	
			border-color: transparent transparent #2e2f2f transparent ;
		}
		.base-info{
		}
		.base-info>ul>li{
			margin-right:15px;
			padding-top:15px;
		}
		.main-block-tab{
			border-color:transparent;
			padding:0;
		}
		#myTab{
			margin-bottom:0;
		}
		#tab-content{
			padding:15px 10px 15px 10px;
			border-left:1px solid #ddd;
			border-right:1px solid #ddd;
			border-bottom:1px solid #ddd;
		}
		.list-title{
			color: #d47058;
		}
		.input-control{
			margin-bottom:0 !important;
			height:30px !important;
			line-height: 30px !important;
		}
		.form-list{
			padding:10px 0;
		}
		.error-text{
			font-style: normal;
			color: red;
		}
		.upload-small-btn{
			font-size: 25px;
			margin-left: 10px;
		}
		.upload-small-btn.cancel{
			cursor: pointer;
		}
		.upload-small-btn.fa-times-circle{
			color: #c4283e;
		}
		.upload-small-btn.fa-check-circle{
			color: #0fc43f;
		}
		.upload-box .state{
			padding:10px 0;
		}
		<div class="main-block">
		<p class="main-block-title">推广信息<i class="unfold-switch unfold-up"></i></p>
		<div class="base-info">
			<div class="info-up">
				<ul class="clearfix list-ul">
					<li class="pull-left"><span class="list-title">订单号：</span>123445747855-2</li>
					<li class="pull-left"><span class="list-title">商户：</span>广州一日三餐餐饮有限公司</li>
					<li class="pull-left"><span class="list-title">聚引客版本：</span>3.0</li>		
					<li class="pull-left"><span class="list-title">推广套餐：</span>找套餐*2</li>		
					<li class="pull-left"><span class="list-title">承诺曝光量（次）：</span>21400000</li>		
					<li class="pull-left"><span class="list-title">推广通道：</span>朋友圈|微博 |陌陌</li>	
					<li class="pull-left"><span class="list-title">状态：</span>推广中</li>	
					<li class="pull-left"><span class="list-title">推广开始时间：</span>2018-03-11</li>	
					<li class="pull-left"><span class="list-title">推广结束时间：</span>/</li>	
				</ul>
			</div>
			<div class="info-down" style="display:none">
				<div class="unfold-info-block" style="border-bottom: 1px solid #ddd;margin-bottom: 10px;">
					<p class="main-block-title-sub">订单信息</p>
					<ul class="clearfix list-ul">
						<li class="pull-left"><span class="list-title">订单号：</span>123445747855-2</li>
						<li class="pull-left"><span class="list-title">商户：</span>广州一日三餐餐饮有限公司</li>
						<li class="pull-left"><span class="list-title">聚引客版本：</span>3.0</li>		
						<li class="pull-left"><span class="list-title">推广套餐：</span>找套餐*2</li>		
						<li class="pull-left"><span class="list-title">套餐金额：</span>1325244</li>		
						<li class="pull-left"><span class="list-title">承诺曝光量：</span>2231254</li>							
					</ul>
				</div>
				<div class="unfold-info-block" style="border-bottom: 1px solid #ddd;margin-bottom: 10px;padding-bottom:10px;">
					<p class="main-block-title-sub">投放信息</p>
					<ul class="clearfix list-ul">
						<li class="pull-left"><span class="list-title">投放顾问：</span>月光一族</li>
						<li class="pull-left"><span class="list-title">推广通道：</span>朋友圈|微博 |陌陌</li>
						<li class="pull-left"><span class="list-title">推广门店（2）：</span>两广白切鸡深圳分店 |白切鸭分店</li>														
					</ul>
					<p>
						<textarea rows="3" disabled style="width:100%;padding: 4px 6px;">据说是回显“推广方案提审成功”这个任务里面编辑的投放信息内容</textarea>
					</p>
				</div>
				<div class="unfold-info-block">
					<p class="main-block-title-sub">投放状态</p>
					<ul class="clearfix list-ul">
						<li class="pull-left"><span class="list-title">状态：</span>推广中</li>	
						<li class="pull-left"><span class="list-title">推广开始时间：</span>2018-03-11</li>	
						<li class="pull-left"><span class="list-title">推广结束时间：</span>/</li>														
					</ul>
				
				</div>
				
			</div>
		</div>
	</div>
	
	<div class="main-block main-block-tab">
		<ul class="nav nav-tabs" id="myTab">
		  <li class="active"><a href="#summarizing"  data-toggle="tab">汇总</a></li>
		  <li><a href="#coupon"  data-toggle="tab">卡券和核销</a></li>
		  <li><a href="#weixin"  data-toggle="tab">朋友圈</a></li>
		  <li><a href="#weibo"  data-toggle="tab">微博</a></li>
		  <li><a href="#momo"  data-toggle="tab">陌陌</a></li>
		</ul>
		 <!-- 这是tab内容 -->
		<div class="tab-content" id="tab-content">
			<!-- 汇总 start-->
		  <div class="tab-pane active" id="summarizing">
			<div class="unfold-info-block" style="border-bottom: 1px solid #ddd;margin-bottom: 10px;">
				<p class="main-block-title-sub">推广数据汇总</p>
				<ul class="clearfix list-ul row">
					<li class="pull-left span3"><span class="list-title">实际曝光量：</span>123442</li>
					<li class="pull-left span3"><span class="list-title">详情查看（点击）量：</span>325101201</li>
					<li class="pull-left span3"><span class="list-title">详情查看（点击）率（%）：</span>20%</li>		
					<li class="pull-left span3"><span class="list-title">销售线索数：</span>200</li>		
					<li class="pull-left span3"><span class="list-title">卡券领取数：</span>15244</li>		
					<li class="pull-left span3"><span class="list-title">卡券领取率（%）：</span>33%</li>
					<li class="pull-left span3"><span class="list-title">核销总数：</span>23512</li>	
					<li class="pull-left span3"><span class="list-title">到店核销率（%）：</span>20%</li>							
				</ul>
			</div>
			<div class="unfold-info-block" style="border-bottom: 1px solid #ddd;margin-bottom: 10px;">
				<p class="main-block-title-sub">推广成本汇总</p>
				<ul class="clearfix list-ul row">
					<li class="pull-left span3"><span class="list-title">渠道总成本：</span>123442</li>
					<li class="pull-left span3"><span class="list-title">真实曝光成本：</span>0.02</li>
					<li class="pull-left span3"><span class="list-title">详情查看成本（真实）：</span>233210</li>		
					<li class="pull-left span3"><span class="list-title">卡券领取成本：</span>0.04</li>		
					<li class="pull-left span3"><span class="list-title">详情查看成本（对外）：</span>0.05</li>		
					<li class="pull-left span3"><span class="list-title">实际曝光成本（对外）：</span>0.05</li>
					<li class="pull-left span3"><span class="list-title">核销成本：</span>0.02</li>	
					<li class="pull-left span3"><span class="list-title">微博粉丝通道成本：</span>32154</li>	
					<li class="pull-left span3"><span class="list-title">朋友圈通道成本：</span>32154</li>	
					<li class="pull-left span3"><span class="list-title">陌陌通道成本：</span>32154</li>							
				</ul>
			</div>
			<div class="unfold-info-block">
				<p class="main-block-title-sub">推广成本汇总</p>
				<table class="table">
					<thead>
						<tr>
							<th>
								时间
							</th>
							<th>
								成本
							</th>
							<th>
								曝光量
							</th>
							<th>
								详情查看量
							</th>
							<th>
								详情查看率（%）
							</th>
							<th>
								互动量
							</th>
							<th>
								销售线索数
							</th>
							<th>
								卡券领取数
							</th>
							<th>
								卡券领取率（%）
							</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>2018-12-22</td>
							<td>2212122</td>
							<td>2212</td>
							<td>2121</td>
							<td>30%</td>
							<td>225</td>
							<td>44</td>
							<td>22</td>
							<td>20%</td>
						</tr>
					</tbody>
				</table>
			</div>
		  </div>
		  <!-- 汇总 end -->
		  <!-- 卡券和核销 start-->
		  <div class="tab-pane" id="coupon">
			<div class="unfold-info-block">
				<p class="main-block-title-sub">卡券名称ABCDS-微信链接（卡券链接类别）</p>
				<table class="table">
					<thead>
						<tr>
						    <th>领券总量：354</th>
							<th colspan="3">核销总数：
								<input type="text" class="input-control" placeholder="核销总数">
								<button type="button" class="btn btn-success">确定</button>
							</th>
						</tr>
						<tr>
							<th>
								时间
							</th>
							<th>
								领券量
							</th>
							
							<th>
								操作
							</th>
							<th>
								<button type="button" class="btn get-coupon" data-model="add" data-coupon="2">新增领券量</button>
							</th>
							
						</tr>
					</thead>
					<tbody>
						<tr data-id="1238">
							<td class="coupon-time">2018-10-22</td>
							<td class="coupon-num">22612</td>
							<td>
								<button type="button" class="btn get-coupon" data-model="edit">编辑</button>
								<button type="button" class="btn delete_btn">删除</button>
							</td>
							<td>								
							</td>							
						</tr>
						<tr data-id="12338">
							<td class="coupon-time">2018-12-12</td>
							<td class="coupon-num">2264522</td>
							<td>
								<button type="button" class="btn get-coupon" data-model="edit">编辑</button>
								<button type="button" class="btn delete_btn">删除</button>
							</td>
							<td>								
							</td>							
						</tr>
					</tbody>
				</table>
			</div>
			
			<div class="unfold-info-block">
				<p class="main-block-title-sub">卡券名称ABCDS-手机号链接（卡券链接类别）</p>
				<table class="table">
					<thead>
						<tr>
						    <th>领券总量：354</th>
							<th colspan="3">核销总数：
								<input type="text" class="input-control" placeholder="核销总数">
								<button type="button" class="btn btn-success">确定</button>
							</th>
						</tr>
						<tr>
							<th>
								时间
							</th>
							<th>
								领券量
							</th>
							
							<th>
								操作
							</th>
							<th>
								<button type="button" class="btn get-coupon" data-model="add" data-coupon="2">新增领券量</button>
							</th>
							
						</tr>
					</thead>
					<tbody>
						<tr data-id="1230">
							<td class="coupon-time">2008-12-22</td>
							<td class="coupon-num">2812122</td>
							<td>
								<button type="button" class="btn get-coupon" data-model="edit">编辑</button>
								<button type="button" class="btn delete_btn">删除</button>
							</td>
							<td>								
							</td>							
						</tr>
						<tr data-id="1210">
							<td class="coupon-time">2022-12-22</td>
							<td class="coupon-num">2200122</td>
							<td>
								<button type="button" class="btn get-coupon" data-model="edit">编辑</button>
								<button type="button" class="btn delete_btn">删除</button>
							</td>
							<td>								
							</td>							
						</tr>
					</tbody>
				</table>
			</div>
			
		  </div>
		  <!-- 卡券和核销 end-->
		  <!-- 朋友圈 start-->
		  <div class="tab-pane" id="weixin">
		  	<div class="unfold-info-block">
				<div class="clearfix popularize-box">
					<div class="pull-left">
						<ul class="clearfix list-ul">	
									<li class="pull-left"><span class="list-title">状态：</span><span class="popularize-status" id="w-promotionStateTxt">/</span></li>
									<li class="pull-left"><span class="list-title">推广开始时间：</span><span id="w-promotionBeginDate">/</span></li>
									<li class="pull-left"><span class="list-title">推广结束时间：</span><span class="popularize-time" id="w-promotionEndDate">/</span></li>
								</ul>
					</div>
					<div class="pull-right">
						<button type="button" class="btn over-popularize" data-tongdao="weixin">确定推广结束</button>
					</div>
				</div>
				
			</div>
			<div class="unfold-info-block" style="border-bottom: 1px solid #ddd;margin-bottom: 10px;">
				<p class="main-block-title-sub">合计</p>
				<ul class="clearfix list-ul row">
					<li class="pull-left span3"><span class="list-title">曝光量：</span><span  id="w-exposureNumSum">/</span></li>
					<li class="pull-left span3"><span class="list-title">总消耗：</span><span  id="w-expenditureSum">/</span></li>
					<li class="pull-left span3"><span class="list-title">单次曝光成本：</span><span  id="w-singleExposureCostSum">/</span></li>
					<li class="pull-left span3"><span class="list-title">详情查看量：</span><span  id="w-detailsQueryNumSum">/</span></li>
					<li class="pull-left span3"><span class="list-title">详情查看率（%）：</span><span  id="w-detailsQuerySumPercent">/</span></li>
					<li class="pull-left span3"><span class="list-title">互动量：</span><span  id="w-interactionNumSum">/</span></li>
				</ul>
			</div>
			<table class="table">
					<thead>						
						<tr>
							<th width="100">
								<div style="width: 100px">时间</div>
							</th>
							<th>
								花费（元）
							</th>	
							<th>
								详情查看量
							</th>
							<th>
								详情查看成本
							</th>
							<th>
								详情查看率（%）
							</th>
							<th>
								曝光量
							</th>
							<th>
								原生推广页查看量
							</th>	
							<th>
								原生推广页查看成本
							</th>	
							<th>
								原生推广页查看率（%）
							</th>		
							<th>
								门店查看量
							</th>	
							<th>
								点赞评论量
							</th>		
							<th>
								关注量
							</th>	
							<th>
								原生推广页转发量
							</th>	
							<th>
								销售线索量
							</th>			
							<th>
								操作
							</th>					
						</tr>
					</thead>
					<tbody>
						<!--<tr data-id="1230">
							<td><span class="time">2018-12-22</span></td>
							<td><span class="money">1</span></td>
							<td><span class="detailViewNum">2</span></td>
							<td><span class="detailViewCost">3</span></td>
							<td><span class="detailViewRatio">4</span></td>
							<td><span class="exposureNum">5</span></td>
							<td><span class="yuanViewNum">6</span></td>
							<td><span class="yuanViewCost">7</span></td>
							<td><span class="yuanViewRatio">8</span></td>
							<td><span class="shopViewNum">9</span></td>
							<td><span class="zanCommentNum">10</span></td>
							<td><span class="concernNum">11</span></td>
							<td><span class="yuanViewTransmitNum">12</span></td>
							<td><span class="sellNum">7</span></td>
							<td>
								<div style="width:120px;">
									<button type="button" class="btn editData" data-type="weixin">编辑</button>
									<button type="button" class="btn delete_btn" data-type="weixin">删除</button>
								</div>
								
							</td>													
						</tr>-->
						
					</tbody>
				</table>
						
		  </div>
		  <!-- 朋友圈 end-->
		  <!-- 微博 start-->
		  <div class="tab-pane" id="weibo">
		  	<div class="unfold-info-block">
				<div class="clearfix popularize-box">
					<div class="pull-left">
						<ul class="clearfix list-ul">	
							<li class="pull-left"><span class="list-title">状态：</span><span class="popularize-status" id="wb-promotionStateTxt">/</span></li>
							<li class="pull-left"><span class="list-title">推广开始时间：</span><span  id="wb-promotionBeginDate">/</span></li>
							<li class="pull-left"><span class="list-title">推广结束时间：</span><span class="popularize-time" id="wb-promotionEndDate">/</span></li>
						</ul>
					</div>
					<div class="pull-right">
						<button type="button" class="btn over-popularize" data-tongdao="weibo">确定推广结束</button>
					</div>
				</div>
				
			</div>
			<div class="unfold-info-block" style="border-bottom: 1px solid #ddd;margin-bottom: 10px;">
				<p class="main-block-title-sub">合计</p>
				<ul class="clearfix list-ul row">
					<li class="pull-left span3"><span class="list-title">曝光量：</span><span id="wb-exposureNumSum">/</span></li>
					<li class="pull-left span3"><span class="list-title">总消耗：</span><span id="wb-expenditureSum">/</span></li>
					<li class="pull-left span3"><span class="list-title">单次曝光成本：</span><span id="wb-singleExposureCostSum">/</span></li>
					<li class="pull-left span3"><span class="list-title">导流量：</span><span id="wb-flowNumSum">/</span></li>
					<li class="pull-left span3"><span class="list-title">导流率（%）：</span><span id="wb-flowSumPercent">/</span></li>
					<li class="pull-left span3"><span class="list-title">互动量：</span><span id="wb-interactionNumSum">/</span></li>
				</ul>
			</div>
			<table class="table">
					<thead>						
						<tr>
							<th>
								时间
							</th>
							<th>
								广告计划
							</th>	
							<th>
								曝光量
							</th>
							<th>
								千次曝光成本
							</th>	
							<th>
								花费（元）
							</th>
							<th>
								转发
							</th>	
							<th>
								点赞
							</th>	
							<th>
								评论
							</th>		
							<th>
								导流数
							</th>	
							<th>
								导流率（%）
							</th>		
							<th>
								单次导流成本
							</th>	
							<th>
								加关注数
							</th>	
							<th>
								加关注率（%）
							</th>	
							<th>
								加关注成本
							</th>
							<th>
								小card图文点击
							</th>
							<th>
								互动数
							</th>
							<th>
								互动率（%）
							</th>	
							<th>
								单次互动成本
							</th>		
							<th>
								操作
							</th>					
						</tr>
					</thead>
					<tbody>
						<!--<tr data-id="1236">
							<td><span class="time">2018-12-22</span></td>
							<td><span class="advPlan">11</span></td>
							<td><span class="exposureNum">22</span></td>
							<td><span class="thousandExposureCost">33</span></td>
							<td><span class="money">44</span></td>
							<td><span class="transmit">55</span></td>
							<td><span class="zan">66</span></td>
							<td><span class="comment">77</span></td>
							<td><span class="diversionNum">88</span></td>
							<td><span class="diversionRatio">99</span></td>
							<td><span class="diversionCost">1010</span></td>
							<td><span class="attentionNum">1111</span></td>
							<td><span class="attentionRatio">1212</span></td>
							<td><span class="attentionCost">1313</span></td>
							<td><span class="cardClickNum">1414</span></td>
							<td><span class="interactionNum">1515</span></td>
							<td><span class="interactionRatio">1616</span></td>
							<td><span class="interactionCost">1717</span></td>
							<td>
								<div style="width:120px;">
									<button type="button" class="btn editData" data-type="weibo">编辑</button>
									<button type="button" class="btn delete_btn" data-type="weibo">删除</button>
								</div>
								
							</td>													
						</tr>-->
						
					</tbody>
				</table>
		  
		  </div>
		  <!-- 微博 end-->
		  <!--陌陌 start-->
		  <div class="tab-pane" id="momo">
		  	
		  	<div class="unfold-info-block">
				<div class="clearfix popularize-box">
					<div class="pull-left">
						<ul class="clearfix list-ul">
							<li class="pull-left"><span class="list-title">状态：</span><span class="popularize-status" id="mm-promotionStateTxt">/</span></li>
							<li class="pull-left"><span class="list-title">推广开始时间：</span><span  id="mm-promotionBeginDate">/</span></li>
							<li class="pull-left"><span class="list-title">推广结束时间：</span><span class="popularize-time" id="mm-promotionEndDate">/</span></li>
						</ul>
					</div>
					<div class="pull-right">
						<button type="button" class="btn over-popularize" data-tongdao="momo">确定推广结束</button>
					</div>
				</div>
				
			</div>
			<div class="unfold-info-block" style="border-bottom: 1px solid #ddd;margin-bottom: 10px;">
				<p class="main-block-title-sub">合计</p>
				<ul class="clearfix list-ul row">
					<li class="pull-left span3"><span class="list-title">曝光量：</span><span id="mm-showNumSum">/</span></li>
					<li class="pull-left span3"><span class="list-title">总消耗：</span><span id="mm-expenditureSum">/</span></li>
					<li class="pull-left span3"><span class="list-title">单次曝光成本：</span><span id="mm-singleShowCostSum">/</span></li>
					<li class="pull-left span3"><span class="list-title">点击量：</span><span id="mm-clickNumSum">/</span></li>
					<li class="pull-left span3"><span class="list-title">点击率（%）：</span><span id="mm-clickSumPercent">/</span></li>
				</ul>
			</div>
			<table class="table">
					<thead>						
						<tr>
							<th width="120">
								时间
							</th>
							<th>
								消耗（元）
							</th>	
							<th>
								展示量（次）
							</th>							
							<th>
								点击量（次）
							</th>	
							<th>
								点击率（%）
							</th>	
							<th>
								CPM（元）
							</th>								
							<th>
								平均点击单价（元）
							</th>								
							<th>
								操作
							</th>					
						</tr>
					</thead>
					<tbody>
						<!--<tr data-id="1230">
							<td><span class="time">2018-12-22</span></td>
							<td><span class="consume">331</span></td>
							<td><span class="showNum">332</span></td>							
							<td><span class="clickNum">33300</span></td>
							<td><span class="clickRatio">3</span></td>													
							<td><span class="thousandClickCost">33354</span></td>
							<td><span class="averageClickCost">33387</span></td>
							<td>
								<div style="width:120px;">
									<button type="button" class="btn editData" data-type="momo">编辑</button>
									<button type="button" class="btn delete_btn" data-type="momo">删除</button>
								</div>
								
							</td>													
						</tr>-->
					
					</tbody>
				</table>
		  	
		  </div>
		  <!--陌陌 end-->
		</div>
	</div>