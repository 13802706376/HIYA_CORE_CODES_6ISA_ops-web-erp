$(function() {
	var orderForm = {
		productData: [],
		init: function() {
			$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					$('#btnSubmit').attr('disabled', true);
					var isNew = $("input[name='isNewShop']:checked").val();
					var cellPhoneReg=/^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\d{8}$/;  //手机号码正则
					var phoneReg=/^[0-9|-]{7,15}$/; //座机号码正则
					if(isNew=='Y'){
						var orderType = $.trim($('#orderType').val()),
						orderNumber = $.trim($('#orderNumber').val()),
						buyDate = $.trim($('#buyDate').val()),
						realPrice = $.trim($('#tempRealPrice').val()),
						shopId = $("#shopId2").val(),
						shopName = $.trim($('#shopName2').val()),
						remark = $.trim($('#remark').val()),
						productsVal = [];
						if (orderType === '' || $('#orderType').val() == null) {
							$.jBox.tip('请选择订单类别！', 'error');
							$('#btnSubmit').removeAttr('disabled');
							return false;
						}
						if (!(/^[0-9|-]+$/.test(orderNumber))) {
							$.jBox.tip('请填写正确的地推高手订单编号，应为数字或-组合！', 'error');
							$('#btnSubmit').removeAttr('disabled');
							return false;
						}
						if (buyDate === '') {
							$.jBox.tip('请选择购买时间！', 'error');
							$('#btnSubmit').removeAttr('disabled');
							return false;
						}
						if (!(/^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/.test(realPrice))) {
							$.jBox.tip('请输入合法的订单金额！', 'error');
							$('#btnSubmit').removeAttr('disabled');
							return false;
						}
						if(shopName === ''){
							$.jBox.tip('商户名称不能为空！', 'error');
							$('#btnSubmit').removeAttr('disabled');
							return false;
						}
						if(shopId === ''){
							$.jBox.tip('手机号码不能为空！', 'error');
							$('#btnSubmit').removeAttr('disabled');
							return false;
						}
						if(!(cellPhoneReg.test(shopId))){							
							$.jBox.tip('请输入正确的手机号码！', 'error');
							$('#btnSubmit').removeAttr('disabled');
							return false;
						}
					}else{
						var orderType = $.trim($('#orderType').val()),
							orderNumber = $.trim($('#orderNumber').val()),
							buyDate = $.trim($('#buyDate').val()),
							realPrice = $.trim($('#tempRealPrice').val()),
							shopId = $("#shopId").val(),
							shopName = $.trim($('#shopName').val()),
							shopNumber = $.trim($('#shopNumber').val()),
							industryType = $.trim($('#industryType').val()),
							contactName = $.trim($('#contactName').val()),
							contactPhone = $.trim($('#contactPhone').val()),
							serviceProvider = $.trim($('#serviceProvider').val()),
							serviceProviderPhone = $.trim($('#serviceProviderPhone').val()),
							remark = $.trim($('#remark').val()),
							productsVal = [];
						var serviceCode=$.trim($('#serviceCode').val());//服务商编码只能是数字
						if (orderType === '' || $('#orderType').val() == null) {
							$.jBox.tip('请选择订单类别！', 'error');
							$('#btnSubmit').removeAttr('disabled');
							return false;
						}
						if (!(/^[0-9|-]+$/.test(orderNumber))) {
							$.jBox.tip('请填写正确的地推高手订单编号，应为数字或-组合！', 'error');
							$('#btnSubmit').removeAttr('disabled');
							return false;
						}
						if (buyDate === '') {
							$.jBox.tip('请选择购买时间！', 'error');
							$('#btnSubmit').removeAttr('disabled');
							return false;
						}
						if (!(/^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/.test(realPrice))) {
							$.jBox.tip('请输入合法的订单金额！', 'error');
							$('#btnSubmit').removeAttr('disabled');
							return false;
						}
						if(shopId === ''){
							$.jBox.tip('必须选择一个商户！', 'error');
							$('#btnSubmit').removeAttr('disabled');
							return false;
						}
						if (shopNumber !== '' && !(/^[0-9]+$/.test(shopNumber))) {
							$.jBox.tip('商户账号只能能包含数字！', 'error');
							$('#btnSubmit').removeAttr('disabled');
							return false;
						}
						if (industryType !== '' && (/[\*&￥@#%^$!！~`()\\\|/.,，:;；：=+<>《》]+/).test(industryType)) {
							$.jBox.tip('商户所属行业不能包含特殊字符！', 'error');
							$('#btnSubmit').removeAttr('disabled');
							return false;
						}
						if (orderType === '1' && contactName !== '' && (/[\*&￥@#%^$!！~`()\\\|/.,，:;；：=+<>《》]+/).test(contactName)) {
							// /^[a-z|A-Z|\u4e00-\u9fa5|\s\-&@#]+$/
							$.jBox.tip('商户联系人只能是中文或英文！', 'error');
							$('#btnSubmit').removeAttr('disabled');
							return false;
						}
						if (orderType === '1' && contactPhone !== '' && !(cellPhoneReg.test(contactPhone))) {
							$.jBox.tip('请填写正确的商户手机号码！', 'error');
							$('#btnSubmit').removeAttr('disabled');
							return false;
						}
						if (orderType === '2' && serviceProvider !== '' && (/[\*&￥@#%^$!！~`()\\\|/.,，:;；：=+<>《》]+/).test(serviceProvider)) {
							$.jBox.tip('服务商联系人只能是中文或英文！', 'error');
							$('#btnSubmit').removeAttr('disabled');
							return false;
						}
						if (orderType === '2' && serviceProviderPhone !== '' && !(cellPhoneReg.test(serviceProviderPhone))) {
							$.jBox.tip('请填写正确的服务商手机号码！', 'error');
							$('#btnSubmit').removeAttr('disabled');
							return false;
						}					
						if ( serviceCode && !(/^[0-9]+$/.test(serviceCode))) {
							$.jBox.tip('服务商编码只能是数字！', 'error');
							$('#btnSubmit').removeAttr('disabled');
							return false;
						}
					}
					$('select.products').each(function(index, el) {
						var val = $(this).val();
						if (val !== '') {
							productsVal.push(val);
						}
					});				
					setTimeout(function() {
						if (productsVal.length === 0) {
							//已购聚引客服务
							$.jBox.tip('请至少选择一个商品！', 'error');
							$('#btnSubmit').removeAttr('disabled');
							return false;
						}
						loading('正在提交，请稍等...');
						form.submit();
					}, 20);
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
			this.bindEvent();
		},
		bindEvent: function() {
			var me = this;
			$('body').on('click', '.addProduct', function(event) {
				event.preventDefault();
				me.addProduct($(this), $(this).parent('.addproductitem'), $(this).parent('.addproductitem').index());
			}).on('click', '.minus', function(event) {
				event.preventDefault();
				me.minus($(this), $(this).parent('.addproductitem'), $(this).parent('.addproductitem').index());
			}).on('input', '.addproductnum', function(){
				var v = $(this).val();
				if (v.length === 1) {
					$(this).val(v.replace(/[^1-9]/g,''));
				} else {
					$(this).val(v.replace(/\D/g,''));
				}
			}).on('click', '#shopButton, #shopName', function(event) {
				event.preventDefault();
				// 是否限制选择，如果限制，设置为disabled
				if ($("#shopButton").hasClass("disabled")){
					return true;
				}
				// 正常打开	
				top.$.jBox.open("iframe:" + iframePage, "选择商户", 800, 550, {
					buttons:{"确定":"ok", "关闭":true},
					submit: function(v, h, f){	
						if(v === 'ok'){
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
							
							if(shopid != null && shopid !== ''){
								$("#shopId").val(shopid);
								$("#shopName").val(shopname);
								$("#shopShortName").val(shopshortname);
								$("#shopNumber").val(shopnumber);
								$("#industryType").val(industryType);
								$("#contactName").val(contactName);
								$("#contactPhone").val(contactPhone);
								$("#serviceProvider").val(serviceProvider);
								$("#serviceProviderPhone").val(serviceProviderPhone);
							}
						}
					},
					loaded: function(h){
						$(".jbox-content", top.document).css("overflow-y","hidden");
					}
				});
			}).on('change', '.isnewshop', function() {
				console.log($('input[name="isNewShop"]:checked').val());
				if ($('input[name="isNewShop"]:checked').val() === 'Y') {
					$('#isNewShop').show();
					$('#isNotNewShop').hide();
				} else {
					$('#isNotNewShop').show();
					$('#isNewShop').hide();
				}
			})
		},
		addProduct: function(el, pel, index) {
			var s = pel.clone(0, 0);
			$('#addProductWrap').append(s);
			s.find('select').select2();
			s.find('.select2-container').hide();
			s.find('.select2-container:last').show();
			s.find('.addproductnum').val('1');
			s.find('select').val('');
			s.find('.select2-chosen').text('请选择');
		},
		minus: function(el, pel, index) {
			if (index === 0) {
				el.siblings('.addproductnum').val('');
				pel.find('select').val('');
				pel.find('.select2-chosen').text('请选择');
				return;
			};
			pel.remove();
		}
	};

	orderForm.init();
});