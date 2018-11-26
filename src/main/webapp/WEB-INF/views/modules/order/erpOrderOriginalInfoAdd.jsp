<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>菜单管理</title>
	<meta name="decorator" content="default"/>
	<style type="text/css">
		.margin-bottom-10{margin-bottom: 10px}
		.input-addproductnum{ width: 70px }
		.number-wrapper{ position: absolute; left: 660px; top: 4px; width: 320px; background-color: #fff; padding: 0px 0px 20px; }
		.number-wrapper .form-horizontal{ margin: 0px; }
		.number-wrapper .form-horizontal .control-group .control-label{ width: 170px; text-align: left;}
		.number-wrapper .form-horizontal .control-group .controls{ width: 130px; }
		.number-wrapper .form-horizontal .control-group .controls .products-num input{ width: 35px; }
	</style>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="javascript:history.back()" class="comebacka">返回</a></li>
		<li class="active">
			<a href="${ctx}/order/erpOrderOriginalInfo/toAdd">订单
				<!-- <shiro:hasPermission name="order:info:edit">${not empty info.id?'修改':'添加'}</shiro:hasPermission> -->
				<shiro:lacksPermission name="order:info:edit">新增</shiro:lacksPermission>
			</a>
		</li>
	</ul>
	<sys:message content="${message}" />
	<div id="rootNode"></div>
	<!-- <div class="formwrapper positionrelative">
		<form:form id="inputForm" modelAttribute="info" action="${ctx}/order/erpOrderOriginalInfo/add" method="post" class="form-horizontal">
			<input type="hidden" value="" id="addProductData">
			<div class="control-group">
				<label class="control-label">订单类别:</label>
				<div class="controls">
					<form:select path="orderType" class="required input-medium">
						<form:option value="" label="请选择"></form:option>
						<form:option value="1" lable="直销">直销</form:option>
			            <form:option value="2" lable="服务商">服务商</form:option>
					</form:select>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">地推高手订单编号:</label>
				<div class="controls">
					<form:input path="orderNumber" htmlEscape="false" maxlength="50" class="required input-xlarge"/>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">购买时间:</label>
				<div class="controls">
					<input id="buyDate" name="buyDate" 
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false,readOnly:true});" 
						type="text" class="required input-medium Wdate" value="" maxlength="20" readonly="">
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">订单金额:</label>
				<div class="controls">
					<form:input path="tempRealPrice" htmlEscape="false" maxlength="12" class="required input-xlarge"/>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">是否是新商户:</label>
				<div class="controls">
					<input type="radio" name="isNewShop" value="Y" class="isnewshop">是
					<input type="radio" name="isNewShop" value="N" checked class="isnewshop">否
				</div>
			</div>
			<div id="isNotNewShop">
				<div class="control-group">
					<label class="control-label">商户名称:</label>
					<div class="controls">
						<div class="input-append">
							<input id="shopId" name="shopId" type="hidden">
							<input id="shopShortName" name="shopAbbreviation" type="hidden">
							<input id="shopName" name="shopName" readonly="readonly" type="text"
								value="选择商户" data-msg-required="" class="required">
							<a id="shopButton" href="javascript:" class="btn" style="">&nbsp;<i class="icon-search"></i>&nbsp;</a>&nbsp;&nbsp;
						</div>
						<span class="help-inline"><font color="red">*</font></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">商户账号:</label>
					<div class="controls">
						<form:input path="shopNumber" htmlEscape="false" maxlength="50" class="input-xlarge"/>
						<span class="help-inline"><font color="red">*</font> </span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">商户所属行业:</label>
					<div class="controls">
						<form:input id="industryType" path="industryType" htmlEscape="false" maxlength="50" class="input-xlarge"/>
						<span class="help-inline"><font color="red">*</font> </span>
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label">商户联系人:</label>
					<div class="controls">
						<form:input id="contactName" path="contactName" htmlEscape="false" maxlength="50" class="input-xlarge"/>
						<span class="help-inline"><font color="red">*</font> </span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">商户联系电话（手机）:</label>
					<div class="controls">
						<form:input id="contactPhone" path="contactNumber" htmlEscape="false" maxlength="13" class="input-xlarge"/>
						<span class="help-inline"><font color="red">*</font> </span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">服务商:</label>
					<div class="controls">
						<form:input id="serviceProvider" path="agentName" htmlEscape="false" maxlength="50" class="input-xlarge"/>
						<span class="help-inline"><font color="red">*</font> </span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">服务商联系人:</label>
					<div class="controls">
						<form:input path="promoteContact" htmlEscape="false" maxlength="50" class="input-xlarge"/>
						<span class="help-inline"><font color="red">*</font> </span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">服务商联系电话（手机）:</label>
					<div class="controls">
						<form:input id="serviceProviderPhone" path="promotePhone" htmlEscape="false" maxlength="13" class="input-xlarge"/>
						<span class="help-inline"><font color="red">*</font> </span>
					</div>
				</div>
			</div>
			<div id="isNewShop" style="display: none;">
				<div class="control-group">
					<label class="control-label">商户名称:</label>
					<div class="controls">
						<form:input id="shopName2" path="shopName" htmlEscape="false" maxlength="50" class="required input-xlarge"/>
						<span class="help-inline"><font color="red">*</font> </span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">联系电话（手机）:</label>
					<div class="controls">
						<form:input id="shopId2" path="shopId" htmlEscape="false" maxlength="13" class="required input-xlarge"/>
						<span class="help-inline"><font color="red">*</font> </span>
					</div>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">服务商编码:</label>
				<div class="controls">
					<form:input id="serviceCode" path="agentId" htmlEscape="false" maxlength="20" class="input-xlarge"/>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label">备注:</label>
				<div class="controls">
					<form:textarea path="remark" htmlEscape="false" rows="3" maxlength="256" class="input-xxlarge"/>
				</div>
			</div>
			<div class="control-group" style="border-bottom-width: 0px; margin-bottom: 0px;">
				<label class="control-label">&nbsp;</label>
				<div class="controls">
					<div id="messageBox" class="redword"></div>
				</div>
			</div>
			<div class="form-actions">
				<shiro:hasPermission name="order:erpOrderOriginalInfo:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="确 定"/>&nbsp;</shiro:hasPermission>
				<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
			</div>
			<div class="number-wrapper">
				<div class="add-number">
					<div class="form-horizontal" id="addProductWrap">
						<div class="control-group">
							<div class="control-label">
								（已购聚引客服务）*数量：
							</div>
							<div class="controls">&nbsp;</div>
						</div>
						<div class="control-group addItem addproductitem">
							<div class="control-label">
								<select class="products input-medium select2-offscreen" tabindex="-1" name="goodIds">
									<option value="">请选择</option>
									<c:forEach items="${goodList}" var="goodInfo">
										<option value="${goodInfo.id}">${goodInfo.name}</option>									
									</c:forEach>
								</select>
							</div>
							<div class="controls">
								<div class="products-num">
									<form:input value="1" path="" htmlEscape="false" maxlength="13" class="addproductnum required input-addproductnum"/>
									<input value="1" class="addproductnum input-addproductnum" type="text" name="buyCounts">
									<button type="button" class="btn minus">-</button>
									<button type="button" class="btn addProduct">+</button>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</form:form>
	</div> -->
	<script type="text/javascript">
		var erpOrderOriginalInfoAdd = function() {
			var queryAgentByShop = function(params, cb) {
					$.get(ctx+'/shop/erpShopInfo/queryAgentByShop?agentId='+params.agentId, function(data) {
						if (Array.isArray(data)) {
							params.agentId = data[0].agentId;
							params.agentName = data[0].agentName;
						}
						typeof cb === 'function' && cb(params);
					})
				},
				selectShop = function(cb) {
					var iframePage = '${ctx}/shop/erpShopInfo/shopSearchList';
					top.$.jBox.open("iframe:" + iframePage, "选择商户", 800, 550, {
						buttons:{"确定":"ok", "关闭":true},
						submit: function(v, h, f){
						if (v === 'ok') {
							var frame = $(h).find("#jbox-iframe").contents();
							var shopid = frame.find("#selectedShopId").val();
							var shopname = frame.find("#selectedShopName").val();
							var shopshortname = frame.find("#selectedShopShortName").val();
							var shopnumber = frame.find("#selectedShopNumber").val();
							var industryType = frame.find("#selectedIndustryType").val();
							var contactName = frame.find("#selectedContactName").val();
							var contactPhone = frame.find("#selectedContactPhone").val();
							var serviceProvider = frame.find("#selectedServiceProvider").val();
							var serviceProviderPhone = frame.find("#selectedServiceProviderPhone").val();
							var zhangbeiId = frame.find("#zhangbeiId").val();
							var agentId = frame.find("#agentId").val();
							if (shopid != null && shopid !== '') {
								var params = {
									shopid: shopid,
									shopname: shopname,
									shopshortname: shopshortname,
									shopnumber: shopnumber,
									industryType: industryType,
									contactName: contactName,
									contactPhone: contactPhone,
									serviceProvider: serviceProvider,
									serviceProviderPhone: serviceProviderPhone,
									zhangbeiId: zhangbeiId,
									agentId: agentId,
									agentName: ''
								}
								//typeof cb === 'function' && cb();
								queryAgentByShop(params, cb);
							}
						}
					},
					loaded: function(h){
						$(".jbox-content", top.document).css("overflow-y","hidden");
					}
				});
			}, 
			agentSearchList = function(cb) {
				var iframePage = '${ctx}/shop/erpShopInfo/agentSearchList';
				top.$.jBox.open("iframe:" + iframePage, "选择服务商", 800, 550, {
					buttons:{"确定":"ok", "关闭":true},
					submit: function(v, h, f){
						if (v === 'ok') {
							var frame = $(h).find("#jbox-iframe").contents();
							var agentId = frame.find("#agentId").val();
							var agentName = frame.find("#agentName").val();
							if (agentId != null && agentId !== '') {
								typeof cb === 'function' && cb({
									agentId: agentId,
									agentName: agentName
								})
							}
						}
					},
					loaded: function(h){
						$(".jbox-content", top.document).css("overflow-y","hidden");
					}
				});
			};

			return {
				selectShop: selectShop,
				agentSearchList: agentSearchList,
				hasPermissionKcl: '<shiro:hasPermission name="order:addOrder:type:kcl">true</shiro:hasPermission>',
				hasPermissionJyk: '<shiro:hasPermission name="order:addOrder:type:jyk">true</shiro:hasPermission>'
			}
		}();
	</script>
	<script src="${ctxStatic}/common/erpshop.js?v=${staticVersion}" type="text/javascript"></script>
	<script src="${ctxStatic}/scripts/manifest.min.js?v=${staticVersion}" type="text/javascript"></script>
	<script src="${ctxStatic}/scripts/vendor.min.js?v=${staticVersion}" type="text/javascript"></script>
	<script src="${ctxStatic}/scripts/order/erporderoriginalinfoadd.min.js?v=${staticVersion}" type="text/javascript"></script>
	<%-- <script src="http://localhost:7777/order/erporderoriginalinfoadd.min.js?v=${staticVersion}" type="text/javascript"></script> --%>
</body>
</html>