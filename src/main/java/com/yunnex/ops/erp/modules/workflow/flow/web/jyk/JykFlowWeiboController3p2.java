package com.yunnex.ops.erp.modules.workflow.flow.web.jyk;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.workflow.flow.service.JykFlowWeiBo3P2Service;

/**
 * 聚引客  直销开户相关  Controller
 * 
 * @author yunnex
 * @date 2017年11月2日
 */
@Controller
@RequestMapping(value = "${adminPath}/jyk/flow/3p2")
public class JykFlowWeiboController3p2 extends BaseController {

    @Autowired
    private JykFlowWeiBo3P2Service jykFlowWeiBo3P2Service;

    /**
     * 确定进行微博充值
     *
     * @param taskId
     * @param isSureRecharge
     * @return
     * @date 2018年5月7日
     * @author hanhan
     */
    @RequestMapping(value = "sure_recharge_microblog_3.2")
    @ResponseBody
    public JSONObject sureRechargeMicroblog(String taskId, String procInsId, String splitId,String isSureRecharge ) {
     
        return  jykFlowWeiBo3P2Service.sureRechargeMicroblog(taskId, procInsId,splitId,isSureRecharge);
    }

    /**
     * 微博充值资料补充
     *
     * @param taskId
     * @param isSureRecharge
     * @return
     * @date 2018年5月7日
     * @author hanhan
     */
    @RequestMapping(value = "microblog_recharge_supplement_3.2")
    @ResponseBody
    public JSONObject microblogRechargeSupplement(String taskId, String procInsId, String splitId, HttpServletRequest request ) {
        String jsonStr=  request.getParameter("jsonStr");
        return  jykFlowWeiBo3P2Service.microblogRechargeSupplement(taskId, procInsId,splitId,jsonStr);
    }

    /**
     * 微博充值资料审核
     *
     * @param taskId
     * @param checkvalue
     * @return
     * @date 2018年5月7日
     * @author hanhan
     */
    @RequestMapping(value = "microblog_recharge_review_3.2")
    @ResponseBody
    public JSONObject microblogRechargeReview(String taskId, String procInsId,String splitId,String isPas, String reason) {
     
        return  jykFlowWeiBo3P2Service.microblogRechargeReview(taskId, procInsId, splitId, isPas,reason);
    }
   
    /**
     *微博充值资料修改
     *
     * @param taskId
     * @param checkvalue
     * @return
     * @date 2018年5月7日
     * @author hanhan
     */
    
    @RequestMapping(value = "microblog_recharge_modify_3.2")
    @ResponseBody
    public JSONObject microblogRechargeModify(String taskId, String procInsId,String splitId, HttpServletRequest request) {
        String jsonStr=  request.getParameter("jsonStr");
        return  jykFlowWeiBo3P2Service.microblogRechargeModify(taskId, procInsId, splitId, jsonStr);
    }
   
   /* *//**
     *微博充值完成
     *
     * @param taskId
     * @param checkvalue
     * @return
     * @date 2018年5月7日
     * @author hanhan
     *//*
    
    @RequestMapping(value = "microblog_recharge_finish_3.2")
    @ResponseBody
    public JSONObject microblogRechargeFinish(String taskId, String procInsId,String splitId, String jsonStr) {
     
        return  jykFlowWeiBo3P2Service.microblogRechargeModify(taskId, procInsId, splitId, jsonStr);
    }*/
    
    
    
}