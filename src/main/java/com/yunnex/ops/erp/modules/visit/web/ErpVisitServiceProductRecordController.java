package com.yunnex.ops.erp.modules.visit.web;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.alibaba.fastjson.JSONObject;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitServiceProductRecord;
import com.yunnex.ops.erp.modules.visit.service.ErpVisitServiceProductRecordService;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowConstant;

/**
 * 硬件产品交付Controller
 * @author hanhan
 * @version 2018-07-09
 */
@Controller
@RequestMapping(value = "${adminPath}/visit/erpVisitServiceProductRecord")
public class ErpVisitServiceProductRecordController extends BaseController {

	@Autowired
	private ErpVisitServiceProductRecordService erpVisitServiceProductRecordService;
	
	@RequestMapping(value = "delete")
	@ResponseBody
	public JSONObject delete(String id) {
	    JSONObject resObject = new JSONObject();
	    ErpVisitServiceProductRecord erpVisitServiceProductRecord=new ErpVisitServiceProductRecord();
	    erpVisitServiceProductRecord.setId(id);
	    erpVisitServiceProductRecordService.delete(erpVisitServiceProductRecord);
		resObject.put(FlowConstant.MESSAGE, "删除硬件产品交付成功");
        resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
        return resObject;
	}

}