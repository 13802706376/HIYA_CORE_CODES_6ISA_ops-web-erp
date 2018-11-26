package com.yunnex.ops.erp.modules.workflow.zhct.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.workflow.zhct.service.ErpZhctProductRecordService;

/**
 * 智慧餐厅产品信息Controller
 * @author yunnex
 * @version 2018-08-28
 */
@Controller
@RequestMapping(value = "${adminPath}/workflow/zhct/erpZhctProductRecord")
public class ErpZhctProductRecordController extends BaseController {

	@Autowired
	private ErpZhctProductRecordService erpZhctProductRecordService;
	
	@RequestMapping(value = "findZhctOpenConditionByProcInsId")
	@ResponseBody
	public JSONObject findZhctOpenConditionByProcInsId(String procInsId) {
		JSONObject jsonObject = erpZhctProductRecordService.findZhctOpenConditionByProcInsId(procInsId);
		return jsonObject;
	}
}