package com.yunnex.ops.erp.modules.workflow.flow.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.activiti.engine.RuntimeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.service.BaseService;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitInfo;
import com.yunnex.ops.erp.modules.order.service.ErpOrderSplitInfoService;
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserWeibo;
import com.yunnex.ops.erp.modules.store.advertiser.service.ErpStoreAdvertiserWeiboService;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreInfoService;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.service.SystemService;
import com.yunnex.ops.erp.modules.workflow.channel.constant.Constants;
import com.yunnex.ops.erp.modules.workflow.channel.entity.ErpChannelWeiboRecharge;
import com.yunnex.ops.erp.modules.workflow.channel.service.ErpChannelWeiboRechargeService;
import com.yunnex.ops.erp.modules.workflow.flow.common.JykFlowConstants;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpFlowForm;
import com.yunnex.ops.erp.modules.workflow.store.service.JykOrderChoiceStoreService;
import com.yunnex.ops.erp.modules.workflow.user.service.ErpOrderFlowUserService;

/**
 * 聚迎客3.2流程
 * 
 * @author hanhan
 * @date 2018年5月7日
 */
@Service
public class JykFlowWeiBo3P2Service extends BaseService {
    
    /** 工作流服务 */
    @Autowired
    private WorkFlowService workFlowService;
    @Autowired
    private ErpFlowFormService erpFlowFormService;
    @Autowired
    protected RuntimeService runtimeService;
    @Autowired
    private ErpOrderSplitInfoService erpOrderSplitInfoService;
    @Autowired
    private JykOrderChoiceStoreService jykOrderChoiceStoreService;
    @Autowired
    private ErpStoreInfoService erpStoreInfoService;
    @Autowired
    private ErpOrderFlowUserService erpOrderFlowUserService;
    @Autowired
    private ErpStoreAdvertiserWeiboService erpStoreAdvertiserWeiboService;
    @Autowired
    private ErpChannelWeiboRechargeService erpChannelWeiboRechargeService;
    @Autowired
    private SystemService systemService;

    /**
     * 确定进行微博充值
     *
     * @param taskId
     * procInsId
     * @param isSureRecharge
     * @return
     * @date 2018年5月7日
     * @author hanhan
     */
    @Transactional(readOnly = false)
    public JSONObject sureRechargeMicroblog(String taskId, String procInsId,String splitId,String isSureRecharge) {
        JSONObject resObject = new JSONObject();
        logger.info("确定进行微博充值 start=== taskid[{}],procInsId[{}],splitId[{}],isSureRecharge[{}]", taskId, procInsId,splitId,isSureRecharge);
       //判断该店微博开户资料审核有没通过
        String choiceStoreId= jykOrderChoiceStoreService.getStoreIdBySplitId(splitId);
        ErpStoreAdvertiserWeibo erpStoreAdvertiserWeibo=erpStoreAdvertiserWeiboService.getByStoreId(choiceStoreId);
        if(null==erpStoreAdvertiserWeibo||erpStoreAdvertiserWeibo.getAuditStatus()!=4){
            resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_FALSE);
            resObject.put(FlowConstant.MESSAGE, "请等待微博开户资料完成!"); 
            return resObject;
        }
        // 保存流程表单数据
        erpFlowFormService.saveErpFlowForm(taskId, procInsId, splitId,FlowConstant.IS_SURE_RECHARGE, "sure_recharge_microblog_3.2",isSureRecharge);
        Map<String, Object> vars = Maps.newHashMap();
        vars.put(FlowConstant.CHOOSE_MICROBLOG_RECHARGE_FLAG, Constant.NO);
        if(isSureRecharge.equals(Constant.YES)){
            vars.put(FlowConstant.CHOOSE_MICROBLOG_RECHARGE_FLAG, Constant.YES);
        }
        // 任务完成
        this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId,"完成确定进行微博充值", vars);
        resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
        return resObject;
    }
    
    
    /**
     * 微博充值资料补充
     *
     * @param taskId
     * @return
     * @date 2018年5月7日
     * @author hanhan
     */
    @Transactional(readOnly = false)
    public JSONObject microblogRechargeSupplement(String taskId, String procInsId,String splitId,String jsonStr) {
        logger.info("微博充值资料补充 start=== taskid[{}],procInsId[{}],splitId[{}],jsonStr[{}]", taskId, procInsId, splitId,jsonStr);
        JSONObject resObject = new JSONObject();
        if(!StringUtils.isBlank(jsonStr)){
            JSONArray jsonArray = JSONArray.parseArray(jsonStr);
            String choiceStoreId= jykOrderChoiceStoreService.getStoreIdBySplitId(splitId);
            ErpStoreInfo ErpStoreInfo=erpStoreInfoService.get(choiceStoreId);
            if(null==ErpStoreInfo){
                resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_FALSE);
                resObject.put(FlowConstant.MESSAGE, "分单id不存在！"); 
                return resObject;
            }
            ErpChannelWeiboRecharge recharge = new ErpChannelWeiboRecharge();
            recharge.setApplyDate(new Date());
            recharge.setSplitId(splitId);
            recharge.setSource(Constants.SOURCE_FLOW);
            recharge.setShopInfoId(ErpStoreInfo.getShopInfoId());
            recharge.setStatus(Constants.STATUS_COMMIT);
            for(int i=0;i<jsonArray.size(); i++){
                JSONObject jsonJ = jsonArray.getJSONObject(i); 
                recharge.setApplyAmount(Double.parseDouble(jsonJ.getString("applyAmount")));
                recharge.setWeiboAccountNo(jsonJ.getString("weiboAccountNo"));
                recharge.setWeiboUid(jsonJ.getString("weiboUid"));
                erpChannelWeiboRechargeService.save(recharge);
            }
            Map<String, Object> vars = Maps.newHashMap();
            //指派下一步角色的值
            // 获取微博充值审核开户
            List<User> userListFriends = systemService.getUserByRoleName("weibo_recharge_assessor");
            ErpOrderSplitInfo erpOrderSplitInfo =erpOrderSplitInfoService.get(splitId);
            if(!CollectionUtils.isEmpty(userListFriends)){
                //获取微博充值审核开户
                erpOrderFlowUserService.insertOrderFlowUser(userListFriends.get(0).getId(), erpOrderSplitInfo.getOrderId(), splitId, JykFlowConstants.weiboRechargeCommissioner, procInsId);
            }
            
            // 任务完成
            this.workFlowService.completeFlow2(new String[]{JykFlowConstants.weiboRechargeCommissioner}, taskId, procInsId,"微博充值资料补充", vars);
        }
        resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
        return resObject;
    }
   
    /**
     * 微博充值资料审核
     *
     * @param taskId
     * @return
     * @date 2018年5月7日
     * @author hanhan
     */
    @Transactional(readOnly = false)
    public JSONObject microblogRechargeReview(String taskId, String procInsId,String splitId, String isPas, String reason) {
        logger.info("微博充值资料审核 start=== taskid[{}],procInsId[{}],splitId[{}],reason[{}]", taskId, procInsId, splitId,reason);
        JSONObject resObject = new JSONObject();
        Map<String, Object> vars = Maps.newHashMap();
        vars.put(FlowConstant.MICROBLOG_RECHARGE_REVIEW_RESULT, Constant.NO);
        if(Constant.YES.equals(isPas)){
            vars.put(FlowConstant.MICROBLOG_RECHARGE_REVIEW_RESULT, Constant.YES);
        }
        erpFlowFormService.saveErpFlowForm(taskId, procInsId, splitId,FlowConstant.MICROBLO_RECHARGE_CHECK,"microblog_recharge_review_3.2", isPas);
        erpFlowFormService.saveErpFlowForm(taskId, procInsId,splitId, FlowConstant.MICROBLO_RECHARGE_CHECK_FAIL_REASON, "microblog_recharge_review_3.2",reason==null?"":reason);
        this.workFlowService.completeFlow(JykFlowConstants.assignConsultant, taskId, procInsId,"微博充值审核完成！", vars);
        resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
        return resObject;
    }
    
    /**
     *微博充值资料修改
     *
     * @param taskId
     * @return
     * @date 2018年5月7日
     * @author hanhan
     */
    @Transactional(readOnly = false)
    public JSONObject microblogRechargeModify(String taskId, String procInsId,String splitId, String jsonStr) {
        logger.info("微博充值资料补充 start=== taskid[{}],procInsId[{}],splitId[{}],jsonStr[{}]", taskId, procInsId, splitId,jsonStr);
        JSONObject resObject = new JSONObject();
        if(!StringUtils.isBlank(jsonStr)){
            JSONArray jsonArray = JSONArray.parseArray(jsonStr);
            String choiceStoreId= jykOrderChoiceStoreService.getStoreIdBySplitId(splitId);
            ErpStoreInfo ErpStoreInfo=erpStoreInfoService.get(choiceStoreId);
            if(null==ErpStoreInfo){
                resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_FALSE);
                resObject.put(FlowConstant.MESSAGE, "分单id不存在！"); 
                return resObject;
            }
            for(int i=0;i<jsonArray.size(); i++){
                JSONObject jsonJ = jsonArray.getJSONObject(i); 
                ErpChannelWeiboRecharge recharge= erpChannelWeiboRechargeService.get(jsonJ.getString("id"));
                recharge.setApplyAmount(Double.parseDouble(jsonJ.getString("applyAmount")));
                erpChannelWeiboRechargeService.save(recharge);
            }
            Map<String, Object> vars = Maps.newHashMap();
            // 任务完成
            this.workFlowService.completeFlow(JykFlowConstants.weiboRechargeCommissioner, taskId, procInsId,"微博充值资料修改", vars);
        }
        resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
        return resObject;
    }
    
    
    
    
    
}
