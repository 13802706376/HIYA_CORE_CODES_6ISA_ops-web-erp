package com.yunnex.ops.erp.modules.workflow.flow.service;

import java.util.List;
import java.util.Map;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.service.BaseService;
import com.yunnex.ops.erp.modules.act.service.ActTaskService;
import com.yunnex.ops.erp.modules.act.utils.ActUtils;
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserWeibo;
import com.yunnex.ops.erp.modules.store.advertiser.service.ErpStoreAdvertiserWeiboService;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreInfoService;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.yunnex.ops.erp.modules.sys.service.SystemService;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;
import com.yunnex.ops.erp.modules.workflow.flow.common.JykFlowConstants;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowConstant;

/**
 * 微博提审服务类3.2版本
 * 
 * @author zjq
 * @date 2018年5月16日
 */
@Service
public class MicroblogPromotionFlow3P2Service extends BaseService {
   
    private static final Logger logger = LoggerFactory.getLogger(MicroblogPromotionFlow3P2Service.class);
    protected static final String STRCONSTANT_1 = "1";
    protected static final String REVIEW_RESULT_MICROBLOG = "reviewResultMicroblog";
    protected static final int AUDIT_STATUS_PASSED = 4;
    @Autowired
    private ActTaskService actTaskService;
    @Autowired
    private ErpStoreAdvertiserWeiboService erpStoreAdvertiserWeiboService;
    @Autowired
    private SystemService systemService;
    @Autowired
    private ErpStoreInfoService erpStoreInfoService;
    @Autowired
    private WorkFlowService workFlowService;
    @Autowired
    private ErpFlowFormService erpFlowFormService;
    @Autowired
    private TaskService taskService;
    
    /**
     * 
     * 启动微博提审流程
     * 
     * @param weiboId
     * @date 2018年5月17日
     * @author zjq
     */
    @Transactional(readOnly = false)
    public boolean startMicroblogPromotionFlow(String storeId,String weiboId) {
        logger.info("启动微博提审流程:{}", weiboId);
        Map<String, Object> vars = Maps.newHashMap();
        // 获取业管-微博开户
        List<User> userListWeibo = systemService.getUserByRoleName("pi_weibo");
        String userId = userListWeibo.get(0).getId();
        if (!CollectionUtils.isEmpty(userListWeibo)) {
            vars.put(JykFlowConstants.pipeIndustryWeibo, userId);
        }
        Principal principal = UserUtils.getPrincipal();
        vars.put(JykFlowConstants.OPERATION_ADVISER,  principal.getId());
        String procInsId = actTaskService.startProcess(ActUtils.MICROBLOG_PROMOTION_FLOW[0], ActUtils.MICROBLOG_PROMOTION_FLOW[1], weiboId,
                        "启动微博提审流程", vars);
        // 保存流程数据
        ErpStoreAdvertiserWeibo erpStoreAdvertiserWeibo = erpStoreAdvertiserWeiboService.get(weiboId);
        erpStoreAdvertiserWeibo.setProcInsId(procInsId);
        erpStoreAdvertiserWeiboService.save(erpStoreAdvertiserWeibo);
        ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
        storeInfo.setWeiboExtension( STRCONSTANT_1);
        erpStoreInfoService.save(storeInfo);
        logger.info("启动微博提审流程:weiboId[{}],procInsId[{}]", weiboId, procInsId);
        return true;
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
    @Transactional(readOnly = false)
    public JSONObject microblogPromoteInfoReviewV1(String taskId, String procInsId, String isPass, String reason, String openOrTrans) {
        JSONObject resObject = new JSONObject();
        Map<String, Object> vars = Maps.newHashMap();
        logger.info("微博推广开户资料复审==start,taskId{},procInsId{},openOrTrans{},isPass{},reason{}", taskId, procInsId, openOrTrans, isPass, reason);
        ErpStoreAdvertiserWeibo advertiserweibo = erpStoreAdvertiserWeiboService.getByProcInsId(procInsId);
        vars.put(REVIEW_RESULT_MICROBLOG, isPass);
        // 保存新开户/转换信息
        if (StringUtils.isNotEmpty(openOrTrans)) {
                erpStoreAdvertiserWeiboService.updateOpenOrTrans(advertiserweibo.getId(), openOrTrans);
        }
        // 任务完成
        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.pipeIndustryWeibo, JykFlowConstants.OPERATION_ADVISER}, taskId, procInsId,
                        "完成微博推广开户资料复审", vars);
        Task task= this.taskService.createTaskQuery().processInstanceId(procInsId).taskDefinitionKeyLike("modify_microblog_promote_info%").singleResult();
        if(null!=task){
            String _tasKId= task.getId();
            if(StringUtils.isNotBlank(_tasKId))
            {
                erpFlowFormService.saveErpFlowForm(_tasKId, procInsId, advertiserweibo.getId(),FlowConstant.MICROBLOG_PROMOTION_FLOW_CHECK_V1, "microblog_promote_info_review_1.0",isPass);
                erpFlowFormService.saveErpFlowForm(_tasKId, procInsId, advertiserweibo.getId(),FlowConstant.MICROBLOG_PROMOTION_FLOW_CHECK_FAIL_REASON_V1,"microblog_promote_info_review_1.0",reason==null?"":reason); 
            }
        }
        // 保存微博审核状态
        advertiserweibo.setAuditStatus("Y".equals(isPass) ? AUDIT_STATUS_PASSED : 3);
        erpStoreAdvertiserWeiboService.save(advertiserweibo);
        // 同步状态
        ErpStoreInfo erpStoreInfo = erpStoreInfoService.getWeiboPromotionInfobyProcInsId(procInsId);
        advertiserweibo.setShopInfoId(erpStoreInfo.getShopInfoId());
        erpStoreAdvertiserWeiboService.syncAuditStatusByAccount(advertiserweibo);
        resObject.put("result", true);
        logger.info("微博推广开户资料复审==end,taskId{},procInsId{},openOrTrans{},isPass{},reason{}", taskId, procInsId, openOrTrans, isPass, reason);
        return resObject;
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
    @Transactional(readOnly = false)
    public JSONObject modifyMicroblogPromoteInfoV1(String taskId, String procInsId) {
            JSONObject resObject = new JSONObject();
            Map<String, Object> vars = Maps.newHashMap();
            ErpStoreAdvertiserWeibo advertiserweibo = erpStoreAdvertiserWeiboService.getByProcInsId(procInsId);
            advertiserweibo.setAuditStatus(1);
            erpStoreAdvertiserWeiboService.save(advertiserweibo);
        // 任务完成
        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.pipeIndustryWeibo}, taskId, procInsId, "完成修改微博推广开户资料", vars);
            resObject.put("result", true);
            resObject.put("result", true);
            return resObject;
    }
}