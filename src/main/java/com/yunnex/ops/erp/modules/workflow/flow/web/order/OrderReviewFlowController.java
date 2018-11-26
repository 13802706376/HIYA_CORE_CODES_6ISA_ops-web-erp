package com.yunnex.ops.erp.modules.workflow.flow.web.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.workflow.flow.service.OrderReviewFlowService;


@Controller
@RequestMapping(value = "${adminPath}/order/flow")
public class OrderReviewFlowController extends BaseController {


    @Autowired
    private OrderReviewFlowService orderReviewFlowService;

    /**
     * 首次审核订单
     *
     * @param taskId
     * @param procInsId
     * @return
     * @date 2018年7月5日
     * @author zjq
     * @throws ServiceException
     */
    @RequestMapping(value = "order_review_first", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public JSONObject order_review_first(@RequestParam(required = false, value = "procInsId") String procInsId,
                    String orderId, String auditResult, String verifyInfo, String reason) throws ServiceException {
        return orderReviewFlowService.orderReviewFirst(procInsId, orderId, auditResult, verifyInfo, reason);
    }


    /**
     * 二次订单审核
     *
     * @param taskId
     * @param procInsId
     * @return
     * @date 2018年7月5日
     * @author zjq
     * @throws ServiceException
     */
    @RequestMapping(value = "order_review_second", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public JSONObject order_review_second(@RequestParam(required = false, value = "procInsId") String procInsId, String orderId, String auditResult,
                    String reason)
                    throws ServiceException {
        return orderReviewFlowService.orderReviewSecond(procInsId, orderId, auditResult, reason);
    }


    /**
     * 修改/删除订单
     *
     * @param taskId
     * @param procInsId
     * @return
     * @date 2018年7月5日
     * @author zjq
     * @throws Exception
     */
    @RequestMapping(value = "modify_order_info", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public JSONObject modify_order_info(@RequestBody ErpOrderOriginalInfo erpOrderOriginalInfo,
                    @RequestParam(required = false, value = "procInsId") String procInsId, String orderId,
                    String auditResult) throws Exception {
        return orderReviewFlowService.modifyOrderInfo(erpOrderOriginalInfo,procInsId, orderId, auditResult);
    }

}