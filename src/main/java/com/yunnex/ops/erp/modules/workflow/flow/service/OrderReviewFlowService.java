package com.yunnex.ops.erp.modules.workflow.flow.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.service.BaseService;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.modules.order.constant.OrderConstants;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.order.entity.SplitGoodForm;
import com.yunnex.ops.erp.modules.order.service.ErpOrderGoodServiceInfoService;
import com.yunnex.ops.erp.modules.order.service.ErpOrderOriginalGoodService;
import com.yunnex.ops.erp.modules.order.service.ErpOrderOriginalInfoService;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.service.SystemService;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.handler.ProcessStartContext;


/**
 * 订单审核流程
 * 
 * @author Ejon
 * @date 2018年7月5日
 */
@Service
public class OrderReviewFlowService extends BaseService {

    private static final String DELETE = "Delete";
    private static final String PASS = "Pass";
    private static final String RETURN = "Return";
    private static final String REVIEW_RESULT = "reviewResult";
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WorkFlowService workFlowService;
    @Autowired
    private SystemService systemService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ErpOrderOriginalInfoService erpOrderOriginalInfoService;
    @Autowired
    private ErpOrderOriginalGoodService erpOrderOriginalGoodService;
    @Autowired
    private ErpFlowFormService erpFlowFormService;
    @Autowired
    private ErpOrderGoodServiceInfoService erpOrderGoodServiceInfoService;
    
    /**
     * 
     * 首次审核订单
     * 
     * @param taskId
     * @param procInsId
     * @param auditResult
     * @return
     * @date 2018年7月5日
     * @author zjq
     * @throws ServiceException
     */
    @Transactional(readOnly = false)
    public JSONObject orderReviewFirst(String procInsId, String orderId, String auditResult, String verifyInfo, String reason)
                    throws ServiceException {

        JSONObject resObject = new JSONObject();

        resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
        
        Task task = taskService.createTaskQuery().processInstanceId(procInsId)
                        .taskDefinitionKeyLike(FlowConstant.ORDER_REVIEW_FIRST.concat(Constant.PERCENT))
                        .taskCandidateUser(UserUtils.getUser().getId())
                        .singleResult();

        if (null == task) {
            resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_FALSE);
            resObject.put(FlowConstant.MESSAGE, "任务不存在或已经审核，请尝试刷新页面!");
            return resObject;
        }

        erpFlowFormService.saveErpFlowForm(task.getId(), procInsId, orderId, "reason",
                        FlowConstant.ORDER_REVIEW_FIRST, reason);
        erpFlowFormService.saveErpFlowForm(task.getId(), procInsId, orderId, "verifyInfo",
                        FlowConstant.ORDER_REVIEW_FIRST, verifyInfo);

        Map<String, Object> vars = Maps.newHashMap();
        
        vars.put(REVIEW_RESULT, auditResult);
        
        ErpOrderOriginalInfo erpOrderOriginalInfo = erpOrderOriginalInfoService.get(orderId);

        if(PASS.equalsIgnoreCase(auditResult))
        {
            List<User> users = systemService.getUserByRoleName(FlowConstant.SECOND_ORDER_AUDITOR_SYSROLE);
            String ids = users.stream().filter(user -> StringUtils.isNoneBlank(user.getId())).map(user -> user.getId()).distinct()
                            .collect(Collectors.joining(Constant.COMMA));
            vars.put(FlowConstant.SECOND_ORDER_AUDITOR_FLOWROLE, ids);

            erpOrderOriginalInfo.setAuditStatus(OrderConstants.ORDER_AUDIT_STATUS_1);

        }
        if(RETURN.equalsIgnoreCase(auditResult))
        {
            List<User> users = systemService.getUserByRoleName(FlowConstant.ORDER_CREATOR_SYSROLE);
            String ids = users.stream().filter(user -> StringUtils.isNoneBlank(user.getId())).map(user -> user.getId()).distinct()
                            .collect(Collectors.joining(Constant.COMMA));
            vars.put(FlowConstant.ORDER_CREATOR_FLOWROLE, ids);
            erpOrderOriginalInfo.setAuditStatus(OrderConstants.ORDER_AUDIT_STATUS_3);
        }

        erpOrderOriginalInfoService.saveAuditStatus(erpOrderOriginalInfo);
        workFlowService.completeFlow2(new String[] {}, task.getId(), procInsId, "首次审核订单", vars);
        return resObject;
    }



    /**
     * 二次订单审核
     *
     * @param taskId
     * @param procInsId
     * @param auditResult
     * @return
     * @date 2018年7月5日
     * @author zjq
     * @throws ServiceException
     */
    @Transactional(readOnly = false)
    public JSONObject orderReviewSecond(String procInsId, String orderId, String auditResult, String reason) throws ServiceException {
        JSONObject resObject = new JSONObject();

        Map<String, Object> vars = Maps.newHashMap();

        resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
        
        Task task = taskService.createTaskQuery().processInstanceId(procInsId)
                        .taskDefinitionKeyLike(FlowConstant.ORDER_REVIEW_SECOND.concat(Constant.PERCENT))
                        .taskCandidateUser(UserUtils.getUser().getId()).singleResult();
        if (null == task) {
            resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_FALSE);
            resObject.put(FlowConstant.MESSAGE, "任务不存在或已经审核，请尝试刷新页面!");
            return resObject;
        }

        erpFlowFormService.saveErpFlowForm(task.getId(), procInsId, orderId, "reason",
                        FlowConstant.ORDER_REVIEW_SECOND, reason);

        vars.put(REVIEW_RESULT, auditResult);

        ErpOrderOriginalInfo erpOrderOriginalInfo = erpOrderOriginalInfoService.get(orderId);

        if (RETURN.equalsIgnoreCase(auditResult)) {
            List<User> users = systemService.getUserByRoleName(FlowConstant.ORDER_CREATOR_SYSROLE);
            String ids = users.stream().filter(user -> StringUtils.isNoneBlank(user.getId())).map(user -> user.getId()).distinct()
                            .collect(Collectors.joining(Constant.COMMA));
            vars.put(FlowConstant.ORDER_CREATOR_FLOWROLE, ids);
            erpOrderOriginalInfo.setAuditStatus(OrderConstants.ORDER_AUDIT_STATUS_3);
        }

        workFlowService.completeFlow2(new String[] {}, task.getId(), procInsId, "二次订单审核", vars);

        if (PASS.equalsIgnoreCase(auditResult)) {
            erpOrderOriginalInfo.setAuditStatus(OrderConstants.ORDER_AUDIT_STATUS_2);
            // 启动流程
            List<SplitGoodForm> splitGoodLists = erpOrderOriginalGoodService.queryGoodFormList(erpOrderOriginalInfo.getId());
            ProcessStartContext.startByErpOrder(erpOrderOriginalInfo, splitGoodLists);
        }
        erpOrderOriginalInfoService.saveAuditStatus(erpOrderOriginalInfo);

        return resObject;
    }



    /**
     * 修改/删除订单
     *
     * @param taskId
     * @param procInsId
     * @param auditResult
     * @return
     * @date 2018年7月5日
     * @author zjq
     * @throws Exception
     */
    @Transactional(readOnly = false)
    public JSONObject modifyOrderInfo(ErpOrderOriginalInfo erpOrderOriginalInfo,String procInsId, String orderId, String auditResult) throws Exception {

        JSONObject resObject = new JSONObject();

        resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);

        Task task = taskService.createTaskQuery().processInstanceId(procInsId)
                        .taskDefinitionKeyLike(FlowConstant.MODIFY_ORDER_INFO.concat(Constant.PERCENT))
                        .taskCandidateUser(UserUtils.getUser().getId()).singleResult();
        if (null == task) {
            resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_FALSE);
            resObject.put(FlowConstant.MESSAGE, "任务不存在或已经审核，请尝试刷新页面!");
            return resObject;
        }

        if (CollectionUtils.isNotEmpty(erpOrderOriginalInfo.getErpOrderOriginalGoods())) {
            erpOrderOriginalGoodService.deleteByOrderId(erpOrderOriginalInfo.getId());
            erpOrderOriginalGoodService.batchInsert(erpOrderOriginalInfo.getErpOrderOriginalGoods(), erpOrderOriginalInfo.getId());
            // 保存订单服务项信息
            erpOrderGoodServiceInfoService.deleteRecordByOrderId(erpOrderOriginalInfo.getId());
            erpOrderGoodServiceInfoService.saveOrderGoodServiceInfo(erpOrderOriginalInfo.getId());
        }
        
       /* ErpOrderOriginalInfo OrderOriginalInfo = erpOrderOriginalInfoService.get(orderId);

        OrderOriginalInfo.setAuditStatus(OrderConstants.ORDER_AUDIT_STATUS_0);

        erpOrderOriginalInfoService.saveAuditStatus(OrderOriginalInfo);*/
        erpOrderOriginalInfo.setAuditStatus(OrderConstants.ORDER_AUDIT_STATUS_0);
        erpOrderOriginalInfoService.save(erpOrderOriginalInfo);

        Map<String, Object> vars = Maps.newHashMap();

        vars.put(REVIEW_RESULT, auditResult);

        if (DELETE.equalsIgnoreCase(auditResult)) {

            erpOrderOriginalInfoService.cancelOrder(orderId);

            workFlowService.completeFlow2(new String[] {}, task.getId(), procInsId, "删除订单", vars);

            return resObject;

        } else {

            workFlowService.completeFlow2(new String[] {}, task.getId(), procInsId, "修改订单", vars);

        }
        return resObject;
    }



}