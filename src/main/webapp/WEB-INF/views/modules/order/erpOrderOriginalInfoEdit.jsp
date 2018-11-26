<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>菜单管理</title>
	<meta name="decorator" content="default"/>
	<style type="text/css">
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
				<shiro:lacksPermission name="order:info:edit">修改</shiro:lacksPermission>
			</a>
		</li>
	</ul>
	<sys:message content="${message}" />
	<div class="formwrapper positionrelative">
		<form:form id="inputForm" modelAttribute="orderInfo" action="${ctx}/order/erpOrderOriginalInfo/update" method="post" class="form-horizontal">
			<input type="hidden" value="" id="addProductData">
			<input type="hidden" name="id" value="${orderInfo.id}">
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
					<input id="buyDate" name="buyDate" value="${orderInfo.tempBuyDate}"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false,readOnly:true});" 
						type="text" class="required input-medium Wdate" maxlength="20" readonly="">
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
			
			<c:if test="${orderInfo.isNewShop=='Y'}">
				<div class="control-group">
					<label class="control-label">商户名称:</label>
					<div class="controls">
						<form:input id="shopName2" path="shopName" htmlEscape="false" maxlength="13" class="required input-xlarge" readonly="true"/>
						<span class="help-inline"><font color="red">*</font> </span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">联系电话:</label>
					<div class="controls">
						<form:input id="shopId2" path="shopId" htmlEscape="false" maxlength="13" class="required input-xlarge" readonly="true"/>
						<span class="help-inline"><font color="red">*</font> </span>
					</div>
				</div>
			</c:if>
			
			<c:if test="${orderInfo.isNewShop=='N'}">
				<div class="control-group">
					<label class="control-label">商户名称:</label>
					<div class="controls">
						<div class="input-append">
							<input id="shopId" name="shopId" type="hidden" value="${orderInfo.shopId}">
							<input id="shopShortName" name="shopAbbreviation" type="hidden" value="${orderInfo.shopAbbreviation}">
							<input id="shopName" name="shopName" readonly="readonly" type="text" value="${orderInfo.shopName}" data-msg-required="" class="required">
							<a id="shopButton" href="javascript:" class="btn">&nbsp;<i class="icon-search"></i>&nbsp;</a>&nbsp;&nbsp;
						</div>
						<span class="help-inline"><font color="red">*</font></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">商户账号:</label>
					<div class="controls">
						<form:input path="shopNumber" htmlEscape="false" maxlength="50" class="input-xlarge"/>
						<!-- <span class="help-inline"><font color="red">*</font> </span> -->
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">商户所属行业:</label>
					<div class="controls">
						<form:input id="industryType" path="industryType" htmlEscape="false" maxlength="50" class="input-xlarge"/>
						<!-- <span class="help-inline"><font color="red">*</font> </span> -->
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label">商户联系人:</label>
					<div class="controls">
						<form:input id="contactName" path="contactName" htmlEscape="false" maxlength="50" class="input-xlarge"/>
						<!-- <span class="help-inline"><font color="red">*</font> </span> -->
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">商户联系电话:</label>
					<div class="controls">
						<form:input id="contactPhone" path="contactNumber" htmlEscape="false" maxlength="13" class="input-xlarge"/>
						<!-- <span class="help-inline"><font color="red">*</font> </span> -->
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">服务商:</label>
					<div class="controls">
						<form:input id="serviceProvider" path="agentName" htmlEscape="false" maxlength="50" class="input-xlarge"/>
						<!-- <span class="help-inline"><font color="red">*</font> </span> -->
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">服务商联系人:</label>
					<div class="controls">
						<form:input path="promoteContact" htmlEscape="false" maxlength="50" class="input-xlarge"/>
						<!-- <span class="help-inline"><font color="red">*</font> </span> -->
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">服务商联系电话:</label>
					<div class="controls">
						<form:input id="serviceProviderPhone" path="promotePhone" htmlEscape="false" maxlength="13" class="input-xlarge"/>
						<!-- <span class="help-inline"><font color="red">*</font> </span> -->
					</div>
				</div>
			</c:if>			
			
			<div class="control-group">
				<label class="control-label">服务商编码:</label>
				<div class="controls">
					<form:input id="serviceCode" path="agentId" htmlEscape="false" maxlength="20" class="input-xlarge"/>
					<!-- <span class="help-inline"><font color="red">*</font> </span> -->
				</div>
			</div>
			<div class="control-group">
				<div class="control-label">
					购买的商品<%-- （已购聚引客服务）*数量 --%>：
				</div>
				<div class="controls" id="addProductWrap">
					<c:forEach items="${orderGoods}" var="orderGood">
						<div class="margin-bottom-10 addproductitem">
							<select class="products input-medium" value="${orderGood.goodId}">
								<option value="">请选择</option>
								<c:forEach items="${goodList}" var="goodInfo">
									<c:if test="${goodInfo.id==orderGood.goodId}"><option value="${goodInfo.id}" selected="selected">${goodInfo.name}</option></c:if>
									<c:if test="${goodInfo.id!=orderGood.goodId}"><option value="${goodInfo.id}">${goodInfo.name}</option></c:if>
								</c:forEach>
							</select>
							<input value="${orderGood.num}" class="addproductnum input-addproductnum" type="number" name="buyCounts" min="1">
							<button type="button" class="btn minus" title="删除该商品">-</button>
							<button type="button" class="btn addProduct" title="添加一个商品">+</button>
						</div>
					</c:forEach>
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
			<%--<div class="number-wrapper">
				<div class="add-number">
					<div class="form-horizontal" id="addProductWrap">
						<div class="control-group">
							<div class="control-label">
								（已购聚引客服务）*数量：
							</div>
							<div class="controls">&nbsp;</div>
						</div>
						
						<c:forEach items="${orderGoods}" var="orderGood">
							<div class="control-group addItem addproductitem">
								<div class="control-label">
									<select class="products input-medium select2-offscreen" tabindex="-1" name="goodIds" value="${orderGood.goodId}">
										<option value="">请选择</option>
										<c:forEach items="${goodList}" var="goodInfo">
											<c:if test="${goodInfo.id==orderGood.goodId}"><option value="${goodInfo.id}" selected="selected">${goodInfo.name}</option></c:if>
											<c:if test="${goodInfo.id!=orderGood.goodId}"><option value="${goodInfo.id}">${goodInfo.name}</option></c:if>
										</c:forEach>
									</select>
								</div>
								<div class="controls">
									<div class="products-num">
										<input class="addproductnum input-addproductnum" type="text" name="buyCounts" value="${orderGood.num}">
										<button type="button" class="btn minus">-</button>
										<button type="button" class="btn addProduct">+</button>
									</div>
								</div>
							</div>
						</c:forEach>
					</div>
				</div>
			</div>--%>
		</form:form>
	</div>
	<script type="text/javascript">var iframePage = '${ctx}/shop/erpShopInfo/shopSearchList';</script>
	<script type="text/javascript" src="${ctxStatic}/common/order.js?v=aefa2152abc919"></script>
</body>
</html>