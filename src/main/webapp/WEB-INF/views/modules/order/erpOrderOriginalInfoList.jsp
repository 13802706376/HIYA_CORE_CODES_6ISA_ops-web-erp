<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>订单管理</title>
	<meta name="decorator" content="default"/>
	<link href="${ctxStatic}/common/act.css?v=asdfc1sd51edcf1" type="text/css" rel="stylesheet" />
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
			<li class="active"><a href="${ctx}/order/erpOrderOriginalInfo/">订单列表</a></li>
		</ul>
		<div class="act-top-right positionrabsolute">
<!-- 			<form id="exportForm" action="" method="post" class="hide"></form> -->
			<form action="${ctx}/order/erpOrderOriginalInfo/export" method="post" id="exportForm" class="hide">
				<input id="jsonObject" type="hidden" value="" name="jsonObject">
			</form>
			<shiro:hasPermission name="order:material:sync">
				<button type="button" class="btn btn-success" id="materialUpdateSync">物料更新订单同步</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="order:list:button:export">
				<button type="button" class="btn btn-success" onclick="exportExcel()">导出</button>
			</shiro:hasPermission>
		</div>
	</div>
	
	<div style="padding:10px;">
		<form:form id="searchForm" modelAttribute="erpOrderOriginalInfo" action="${ctx}/order/erpOrderOriginalInfo/" method="post" class="breadcrumb form-search">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<ul class="ul-form">
				<li><label>订单号：</label>
					<form:input path="orderNumber" htmlEscape="false" maxlength="50" class="input-medium"/>
				</li>
				<li><label>购买时间：</label>
					<c:set var="bbd"><fmt:formatDate value="${erpOrderOriginalInfo.beginBuyDate}" pattern="yyyy-MM-dd"/></c:set>
					<c:set var="ebd"><fmt:formatDate value="${erpOrderOriginalInfo.endBuyDate}" pattern="yyyy-MM-dd"/></c:set>
					<form:input path="beginBuyDate" type="text" maxlength="20" class="input-medium Wdate" value="${bbd}"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false,readOnly:true});"/> - 
					<form:input path="endBuyDate" type="text" maxlength="20" class="input-medium Wdate" value="${ebd}" 
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false,readOnly:true});"/>
				</li>
				<li><label>商户名称：</label>
					<form:input path="shopName" htmlEscape="false" maxlength="50" class="input-medium"/>
				</li>
				<li>
					<label>订单类别：</label>
					<form:select path="orderType" class="input-medium">
						<form:option value="" label="请选择"/>
						<form:options items="${fns:getDictList('orderType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</li>
				<li>
					<label>订单状态：</label>
					<form:select id="order-status" multiple="multiple" path="orderStatusValues" class="input-medium" style="width: 500px;">
							  <c:forEach items="${fns:getDictList('order_status')}" var="orderStatus">
							   <c:set var="orderStatusStr" value=",${orderStatus.id},"></c:set>   
							       <c:choose>
							          <c:when test="${fn:contains(orderStatusValues, orderStatusStr)}">
							            <form:option  selected='selected' value="${ orderStatus.value}">${ orderStatus.label}</form:option>
							          </c:when>
							          <c:otherwise>
							            <form:option  value="${orderStatus.value}">${ orderStatus.label}</form:option>
							          </c:otherwise>
							       </c:choose>
					   </c:forEach>
					</form:select>
				</li>
				
				<li>
					<label>服务类型：</label>
					<form:select id="good-type" multiple="multiple"  path="goodTypeValues"  class="input-medium" style="width: 530px;">
					   <c:forEach items="${goodTypeList}" var="goodType">
					     <c:set var="goodTypeStr" value=",${goodType.id},"></c:set>   
					       <c:choose>
					          <c:when test="${fn:contains(goodTypeValues,goodTypeStr)}">
					            <form:option  selected='selected' value="${ goodType.id}">${ goodType.name}</form:option>
					          </c:when>
					          <c:otherwise>
					            <form:option  value="${goodType.id}">${ goodType.name}</form:option>
					          </c:otherwise>
					       </c:choose>
					   </c:forEach>
					</form:select>
				</li>
				<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
				<li class="btns"><input class="btn btn-warning" type="reset" value="重置"/></li>
				<!-- 服务商/分公司没有同步订单入口和新增订单入口 -->
				<c:if test="${isAgent eq false}">
				   <shiro:hasPermission name="order:list:button:sync">
				   	<li class="btns"><input class="btn btn-primary" type="button" value="同步" onclick="javascript:sync();"/></li>
				   </shiro:hasPermission>
				   <shiro:hasPermission name="order:list:button:addOrder">
				   	<li class="btns"><a href="${ctx}/order/erpOrderOriginalInfo/toAdd" class="btn btn-primary">新增订单</a></li>
				   </shiro:hasPermission>
				</c:if>
				<li class="clearfix"></li>
			</ul>
		</form:form>
		<sys:message content="${message}"/>
		<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th>订单号</th>
					<th>商户名称</th>
					<th>订单类别</th>
					<th>购买时间</th>
					<th>聚引客服务待处理</th>
					<th>商户运营服务待处理</th>
					<th>修改时间</th>
					<th>状态</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="erpOrderOriginalInfo">
				<tr>
					<td>
						<c:if test="${goodType == null }">
							<a href="${ctx}/order/erpOrderOriginalInfo/form?id=${erpOrderOriginalInfo.id}">
									${erpOrderOriginalInfo.orderNumber}
							</a>
						</c:if>
						<c:if test="${goodType !=null }">
							<a href="${ctx}/order/erpOrderOriginalInfo/form?id=${erpOrderOriginalInfo.id}&goodType=${goodType}">
									${erpOrderOriginalInfo.orderNumber}
							</a>
						</c:if>
					</td>
					<td>
						${erpOrderOriginalInfo.shopName}
					</td>
					<td>
						${fns:getDictLabel(erpOrderOriginalInfo.orderType, 'orderType', '未知')}
					</td>
					<td>
						<fmt:formatDate value="${erpOrderOriginalInfo.buyDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>
						${erpOrderOriginalInfo.pendingNum}
					</td>
					<td>
						${erpOrderOriginalInfo.pendingServiceNum}
					</td>
					<td>
						<fmt:formatDate value="${erpOrderOriginalInfo.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>
						<c:if test="${erpOrderOriginalInfo.cancel == 0}">正常</c:if>
						<c:if test="${erpOrderOriginalInfo.cancel == 1}"><span class="red">作废</span></c:if>
						<c:if test="${erpOrderOriginalInfo.cancel == 2}"><span class="red">结束</span></c:if>
					</td>
					<td>
						<shiro:hasPermission name="order:erpOrderOriginalInfo:view">
		    				<a href="${ctx}/order/erpOrderOriginalInfo/form?id=${erpOrderOriginalInfo.id}">查看</a>
						</shiro:hasPermission>

						<shiro:hasPermission name="order:erpOrderOriginalInfo:edit">
							<c:if test="${erpOrderOriginalInfo.splitCount==0 && erpOrderOriginalInfo.orderSource==1 && erpOrderOriginalInfo.sdiCount==0 && erpOrderOriginalInfo.goodType!=6}">
								<a href="${ctx}/order/erpOrderOriginalInfo/toEdit?id=${erpOrderOriginalInfo.id}">修改</a>
								<%--<a href="${ctx}/order/erpOrderOriginalInfo/toAdd?id=${erpOrderOriginalInfo.id}">修改</a>--%>
		    					<a href="${ctx}/order/erpOrderOriginalInfo/dbDelete?id=${erpOrderOriginalInfo.id}" onclick="return confirmx('确认要删除该订单吗？', this.href)">删除</a>
							</c:if>
						</shiro:hasPermission>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		<div class="pagination">${page}</div>
	</div>
	<script type="text/javascript">
		$(document).ready(function() {
			var orderList = {
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
					}).on('click', '#materialUpdateSync', function(event) {
						event.preventDefault();
						syncOrderMaterial();
					});
				}
			}

			orderList.init();

			$("input[type='reset']").click(function(){
				$("select").select2("val", "");
			});

			function syncOrderMaterial() {
				var buyDateEnd = '',
					buyDateStart = '';
				$.ajax({
					url: '${ctx}/order/erpOrderOriginalInfo/syncOrderMaterial',
					type: 'POST',
					dataType: 'json',
					data: {},
					success: function(data) {
						if (data) {
							$.jBox.tip("同步成功！", 'ifno');
							location.reload();
						} else {
							$.jBox.tip("同步失败！", 'ifno');
						}
					}
				})
			}
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		function sync(){
			var html = "<div style='padding:10px;'>创建时间：<input id='s_startAt' name='s_startAt' type='text' readonly='readonly' maxlength='20' class='input-medium Wdate' onclick='javascript:WdatePicker({dateFmt:\"yyyy-MM-dd HH:mm:ss\"})' />至<input id='s_endAt' name='s_endAt' type='text' readonly='readonly' maxlength='20' class='input-medium Wdate' onclick='javascript:WdatePicker({dateFmt:\"yyyy-MM-dd HH:mm:ss\"})' /></div>";
			var submit = function (v, h, f) {
			     if (f.s_startAt == '') {
			        $.jBox.tip("请选择开始时间。", 'error', { focusId: "s_startAt" }); // 关闭设置 yourname 为焦点
			        return false;
			    }
			     if (f.s_endAt == '') {
			    	 $.jBox.tip("请选择结束时间。", 'error', { focusId: "s_endAt" }); // 关闭设置 yourname 为焦点
				     return false;
			     }
			    
		   	 	$.jBox.tip("同步订单的请求已提交，请稍后查看...", 'loading', {
					timeout : 3000
				}); 
 			    
			    $.post("${ctx}/order/erpOrderOriginalInfo/syncAll", {
					startAt : f.s_startAt,
					endAt : f.s_endAt
				}, function(data) {
					if (data.result) {
						
					} else {
						$.jBox.closeTip();
						$.jBox.info("同步订单请求失败");
					}
				});
			    return true; 
			};

			$.jBox(html, { width:500, title: "同步订单", submit: submit });
		}
		
		var orderStatusValues = JSON.parse('${orderStatusValues}');
		var goodTypeValues = JSON.parse('${goodTypeValues}');
		var orderType = '${erpOrderOriginalInfo.orderType}';
		var shopName = '${erpOrderOriginalInfo.shopName}';
		var beginBuyDate = '<fmt:formatDate value="${erpOrderOriginalInfo.beginBuyDate}" pattern="yyyy-MM-dd"/>';
		var endBuyDate = '<fmt:formatDate value="${erpOrderOriginalInfo.endBuyDate}" pattern="yyyy-MM-dd"/>';
		var orderNumber = '${erpOrderOriginalInfo.orderNumber}';
		
		function exportExcel(){
			var params = {};
			if(orderStatusValues){
				Object.assign(params, {
					orderStatusValues:orderStatusValues
				});
			}
			
			if(goodTypeValues){
				Object.assign(params, {
					goodTypeValues:goodTypeValues
				});
			}
			
			if(orderType){
				Object.assign(params, {
					orderType:orderType
				});
			}
			
			if(shopName){
				Object.assign(params, {
					shopName:shopName
				});
			}
			
			if(beginBuyDate){
				Object.assign(params, {
					beginBuyDate:beginBuyDate
				});
			}
			
			if(endBuyDate){
				Object.assign(params, {
					endBuyDate:endBuyDate
				});
			}
			
			if(orderNumber){
				Object.assign(params, {
					orderNumber:orderNumber
				});
			}
			console.log(JSON.stringify(params));
			$('#jsonObject').val(JSON.stringify(params));
			$('#exportForm').submit();
		}
	</script>
</body>
</html>
