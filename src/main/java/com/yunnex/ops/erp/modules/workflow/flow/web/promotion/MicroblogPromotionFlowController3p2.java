package com.yunnex.ops.erp.modules.workflow.flow.web.promotion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.workflow.flow.service.MicroblogPromotionFlow3P2Service;


/**
 * 微博推广提审流程
 * 
 * @author zjq
 * @date 2018年5月16日
 */
@Controller
@RequestMapping(value = "${adminPath}/microblog/flow/3p2")
public class MicroblogPromotionFlowController3p2 extends BaseController {
    
    @Autowired
    private MicroblogPromotionFlow3P2Service microblogPromotionFlow3P2Service;
   
    @RequestMapping(value = "microblog")
    @ResponseBody
    public void microblog(String storeId,String weiboId) {
         microblogPromotionFlow3P2Service.startMicroblogPromotionFlow(storeId, weiboId);
    }

    /**
     * 微博推广开户资料复审
     *
     * @param taskId
     * @param procInsId
     * @param isPass
     * @param reason
     * @param openOrTrans
     * @return
     * @date 2018年5月17日
     * @author zjq
     */
    @RequestMapping(value = "microblog_promote_info_review")
    @ResponseBody
    public JSONObject microblogPromoteInfoReviewV1(String taskId, String procInsId, String isPass, String reason, String openOrTrans) {
        return microblogPromotionFlow3P2Service.microblogPromoteInfoReviewV1(taskId, procInsId, isPass, reason, openOrTrans);
    }

    /**
     * 修改微博推广开户资料并提交
     *
     * @param taskId
     * @param procInsId
     * @return
     * @date 2018年5月17日
     * @author zjq
     */
    @RequestMapping(value = "modify_microblog_promote_info")
    @ResponseBody
    public JSONObject modifyMicroblogPromoteInfoV1(String taskId, String procInsId) {
        return microblogPromotionFlow3P2Service.modifyMicroblogPromoteInfoV1(taskId, procInsId);
    }


}