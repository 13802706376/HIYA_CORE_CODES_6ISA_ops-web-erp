package com.yunnex.ops.erp.modules.workflow.flow.service;

import java.util.List;
import java.util.Map;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.service.BaseService;
import com.yunnex.ops.erp.modules.act.service.ActTaskService;
import com.yunnex.ops.erp.modules.act.utils.ActUtils;
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserFriends;
import com.yunnex.ops.erp.modules.store.advertiser.service.ErpStoreAdvertiserFriendsService;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreInfoService;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.yunnex.ops.erp.modules.sys.service.SystemService;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;
import com.yunnex.ops.erp.modules.workflow.flow.common.JykFlowConstants;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowConstant;

/**
 * 朋友圈提审服务类 3.2版本
 * 
 * @author zjq
 * @date 2018年5月16日
 */
@Service
public class FriendsPromotionFlow3P2Service extends BaseService {
    protected static final String STRCONSTANT_1 = "1";
    protected static final int AUDIT_STATUS_PASSED = 4;
    @Autowired
    private ActTaskService actTaskService;
    @Autowired
    private ErpStoreAdvertiserFriendsService advertiserFriendsService;
    @Autowired
    private SystemService systemService;
    @Autowired
    private ErpStoreInfoService erpStoreInfoService;
    @Autowired
    private ErpFlowFormService erpFlowFormService;
    @Autowired
    private WorkFlowService workFlowService;
    @Autowired
    private TaskService taskService;

    @Transactional(readOnly = false)
    public boolean startMicroblogPromotionFlow(String storeId, String friendsId) {
        Map<String, Object> vars = Maps.newHashMap();
        // 获取业管-朋友圈开户
        List<User> userListFriends = systemService.getUserByRoleName("pi_friends");
        String userId = userListFriends.get(0).getId();
        if (!CollectionUtils.isEmpty(userListFriends)) {
            vars.put(JykFlowConstants.pipeIndustryFriends, userId);
        }
        Principal principal = UserUtils.getPrincipal();
        vars.put(JykFlowConstants.OPERATION_ADVISER, principal.getId());
        // 启动流程
        String procInsId = actTaskService.startProcess(ActUtils.FRIENDS_PROMOTION_FLOW[0], ActUtils.FRIENDS_PROMOTION_FLOW[1], friendsId,
                        "朋友圈推广开户资料复审并发起朋友圈授权", vars);
        // 保存流程数据
        ErpStoreAdvertiserFriends erpStoreAdvertiserFriends = advertiserFriendsService.get(friendsId);
        erpStoreAdvertiserFriends.setProcInsId(procInsId);
        advertiserFriendsService.save(erpStoreAdvertiserFriends);
        ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
        storeInfo.setFriendExtension(STRCONSTANT_1);
        erpStoreInfoService.save(storeInfo);
        return true;
    }

    @Transactional(readOnly = false)
    public JSONObject friendsPromoteInfoReviewV1(String taskId, String procInsId, String isPass, String reason) {
        JSONObject resObject = new JSONObject();
        Map<String, Object> vars = Maps.newHashMap();
        ErpStoreAdvertiserFriends advertiserFriends = advertiserFriendsService.getByProcInsId(procInsId);
        vars.put("reviewResultFriends", isPass);
        advertiserFriends.setAuditStatus("Y".equals(isPass) ? 4 : 3);
        advertiserFriendsService.save(advertiserFriends);
        // 任务完成
        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.OPERATION_ADVISER}, taskId, procInsId, "完成朋友圈推广开户资料复审", vars);
        Task task = this.taskService.createTaskQuery().processInstanceId(procInsId).taskDefinitionKeyLike("modify_friends_promote_info%")
                        .singleResult();
        if (null != task) {
            String _tasKId = task.getId();
            if (StringUtils.isNotBlank(_tasKId)) {
                erpFlowFormService.saveErpFlowForm(_tasKId, procInsId, advertiserFriends.getId(), FlowConstant.FRIENDS_PROMOTION_FLOW_CHECK_V1,
                                "friends_promote_info_review_1.0", isPass);
                erpFlowFormService.saveErpFlowForm(_tasKId, procInsId, advertiserFriends.getId(),
                                FlowConstant.FRIENDS_PROMOTION_FLOW_CHECK_FAIL_REASON_V1, "friends_promote_info_review_1.0", reason);
            }
        }
        resObject.put("result", true);
        return resObject;
    }

    @Transactional(readOnly = false)
    public JSONObject modifyFriendsPromoteInfoV1(String taskId, String procInsId) {
        JSONObject resObject = new JSONObject();
        Map<String, Object> vars = Maps.newHashMap();
        // 任务完成
        ErpStoreAdvertiserFriends advertiserFriends = advertiserFriendsService.getByProcInsId(procInsId);
        advertiserFriends.setAuditStatus(1);
        advertiserFriendsService.save(advertiserFriends);
        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.pipeIndustryFriends}, taskId, procInsId, "完成修改朋友圈推广开户资料", vars);
        resObject.put("result", true);
        return resObject;
    }

    @Transactional(readOnly = false)
    public JSONObject confirmedFriendsAuthorizationSucessV1(String taskId, String procInsId) {
        JSONObject resObject = new JSONObject();
        // 设置流程变量
        Map<String, Object> vars = Maps.newHashMap();
        // 任务完成
        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.OPERATION_ADVISER}, taskId, procInsId, "完成确认朋友圈授权成功", vars);
        // 同步状态
        ErpStoreInfo erpStoreInfo = erpStoreInfoService.getFriendsPromotionInfobyProcInsId(procInsId);
        ErpStoreAdvertiserFriends advertiserFriends = advertiserFriendsService.getByProcInsId(procInsId);
        advertiserFriends.setShopInfoId(erpStoreInfo.getShopInfoId());
        advertiserFriends.setAuditStatus(4);
        advertiserFriendsService.syncAuditStatusByAccount(advertiserFriends);
        resObject.put("result", true);
        return resObject;
    }



}
