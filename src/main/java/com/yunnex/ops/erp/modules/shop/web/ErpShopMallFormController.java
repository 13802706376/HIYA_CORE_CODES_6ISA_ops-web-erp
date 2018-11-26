package com.yunnex.ops.erp.modules.shop.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.shop.service.ErpShopMallFormService;

/**
 * 商户店铺信息收集Controller
 * @author hanhan
 * @version 2018-05-26
 */
@Controller
@RequestMapping(value = "${adminPath}/shop/erpShopMallForm")
public class ErpShopMallFormController extends BaseController {

	@Autowired
	private ErpShopMallFormService erpShopMallFormService;
	
	
	@RequestMapping(value = "deleteShopMallFormById")
	@ResponseBody
	public JSONObject deleteShopMallFormById(String id) {
	    return erpShopMallFormService.deleteShopMallFormById(id);
	}
}