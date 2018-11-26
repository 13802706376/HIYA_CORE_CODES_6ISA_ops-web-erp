<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>推广数据管理</title>
	<meta name="decorator" content="default"/>
	<link href="${ctxStatic}/common/act.css?v=${staticVersion}" type="text/css" rel="stylesheet" />
	<style type="text/css">
		.order-top-dropdown-menu{ position: absolute; top: 30px; left: -115px; }
		.order-top-dropdown-menu>li>a{ margin-right: 0px; }
		.order-select-catgry{ width: 14%; min-width: 110px; vertical-align: text-top;}
		.select2-container-multi .select2-choices .select2-search-choice{height: 16px; margin-bottom: 0px; }
		.select2-container-multi .select2-choices .select2-search-field{ height: 30px; line-height: 30px; }
		.select2-container-multi .select2-choices{ height: 30px !important; overflow-x: hidden; overflow-y: auto; }
		.red{ color:red; }
		@media screen and (max-width:1766px){
			.order-select-catgry dl dd input[type="text"]{ width: 13vw; min-width: 85px;}
		}
		@media screen and (max-width:1000px){
      		.order-select-catgry dl dd input[type="text"]{ width: 85px; }
		}
	</style>
</head>
<body>
	<div class="act-top positionrelative" style="overflow: inherit;">
		<ul class="nav nav-tabs">
			<li class="active"><a href="javascript:;">推广中</a></li>
			<li class=""><a href="">推广结束</a></li>
		</ul>
	</div>	
	<div style="padding:10px;">
		<form id="searchForm" action="${ctx}/order/erpOrderOriginalInfo/" method="post" class="breadcrumb form-search">
			<input id="pageNo" name="pageNo" type="hidden" value=""/>
			<input id="pageSize" name="pageSize" type="hidden" value=""/>
			<ul class="ul-form clearfix">
				<li><label>商户：</label>
					<input type="text" path="orderNumber" htmlEscape="false" maxlength="50" class="input-medium"/>
				</li>
				<li><label>订单号：</label>
					<input type="text" path="orderNumber" htmlEscape="false" maxlength="50" class="input-medium"/>
				</li>
				<li>
					<label style="width: 110px;">推广开始时间：</label>
					<input type="text" maxlength="20" class="input-medium Wdate" value=""
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false,readOnly:true});"/> - 
					<input type="text" maxlength="20" class="input-medium Wdate" value="" 
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false,readOnly:true});"/>
				</li>	
				<li>
					<label>投放通道：</label>
					<select id="order-status" multiple="multiple" path="orderStatusList" class="input-medium" style="width: 280px;">
						<options items="" itemLabel="label" itemValue="value" htmlEscape="false"/>
					<select>
				</li>
				<li>
					<label>投放顾问：</label>
					<select id="good-type" path="goodType" class="input-medium">
						<option value="全部">全部</option>
						<option value="顾问1">顾问1</option>
						<option value="顾问2">顾问2</option>
					<select>
				</li>
				<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="筛选"/></li>
				<li class="btns"><input class="btn btn-warning" type="reset" value="重置"/></li>				
			</ul>
		<form>
		
		<table id="contentTable" class="table table-striped table-bordered table-condensed" style="margin-top:15px;">
			<thead>
				<tr>
					<th>订单号</th>
					<th>购买时间</th>
					<th>订单类别</th>
					<th>商户名称</th>
					<th>待处理的商品数量</th>
					<th>处理中的商品数量</th>
					<th>完成的商品数量</th>
					<th>修改时间</th>
					<th>状态</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
			
				<tr>
					<td>
						
					</td>
					<td>
						
					</td>
					<td>
						
					</td>
					<td>
						
					</td>
					<td>
						
					</td>
					<td>
						
					</td>
					<td>
						
					</td>
					<td>
						
					</td>
					<td>
						
					</td>
					<td>
		    			<a href="${ctx}/promotion/erpPromoteDataInfo/promoteDetail
?splitOrderId=087ce46056994bb2943aec98e9fa6e07">查看详情</a>
					</td>
				</tr>
				<tr>
					<td>
						
					</td>
					<td>
						
					</td>
					<td>
						
					</td>
					<td>
						
					</td>
					<td>
						
					</td>
					<td>
						
					</td>
					<td>
						
					</td>
					<td>
						
					</td>
					<td>
						
					</td>
					<td>
		    			<a href="${ctx}/promotion/erpPromoteDataInfo/promoteDetail
?splitOrderId=363cd8bcac34401fb575494c2c66ac43">查看详情</a>
					</td>
				</tr>
				<tr>
					<td>
						
					</td>
					<td>
						
					</td>
					<td>
						
					</td>
					<td>
						
					</td>
					<td>
						
					</td>
					<td>
						
					</td>
					<td>
						
					</td>
					<td>
						
					</td>
					<td>
						
					</td>
					<td>
		    			<a href="${ctx}/promotion/erpPromoteDataInfo/promoteDetail
?splitOrderId=087ce46056994bb2943aec98e9fa6e07">查看详情</a>
					</td>
				</tr>
		
			</tbody>
		</table>
		<div class="pagination"></div>

	</div>
	
	<script type="text/javascript">
		$(document).ready(function() {
			/*var orderList = {
				init: function() {
					this.bindEvent();
				},
				bindEvent: function() {
					$('body').on('click', '#openselectModal', function(event) {						
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
			}

			orderList.init();

			$("input[type='reset']").click(function(){
				$("select").select2("val", "");
			});*/
		});
		/*function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }*/
		
	</script>
</body>
</html>