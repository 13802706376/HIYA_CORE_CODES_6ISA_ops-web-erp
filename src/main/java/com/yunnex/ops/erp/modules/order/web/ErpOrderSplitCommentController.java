package com.yunnex.ops.erp.modules.order.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunnex.ops.erp.common.constant.CommonConstants;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.order.service.ErpOrderSplitCommentService;
import com.yunnex.ops.erp.modules.order.view.SplitCommentView;

import yunnex.common.core.dto.ApiResult;

/**
 * 聚引客分单评论Controller
 * 
 * @author yunnex
 * @version 2018-01-30
 */
@Controller
@RequestMapping(value = "${adminPath}/order/erpOrderSplitComment")
public class ErpOrderSplitCommentController extends BaseController {

    @Autowired
    private ErpOrderSplitCommentService erpOrderSplitCommentService;

    /**
     * 分单评价视图list jsp
     *
     * @return
     * @date 2018年4月10日
     * @author linqunzhi
     */
    @RequestMapping(value = "viewListJsp")
    public String viewListJsp() {
        return "modules/order/erpOrderSplitCommentViewList";
    }

    @RequestMapping(value = "listBySplit")
    public @ResponseBody ApiResult<List<SplitCommentView>> listBySplit(String splitId) {
        ApiResult<List<SplitCommentView>> result = ApiResult.build();
        try {
            List<SplitCommentView> list = erpOrderSplitCommentService.findSplitCommentViewList(splitId);
            result.setEntry(list);
        } catch (ServiceException e) {
            result.error(e.getMessage());
        } catch (Exception e) {
            logger.info("获取分单评价列表失败", e);
            result.error(CommonConstants.FailMsg.SYSTEM);
        }
        return result;
    }

}
