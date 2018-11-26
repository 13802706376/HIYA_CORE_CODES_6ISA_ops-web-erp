package com.yunnex.ops.erp.modules.workflow.flow.service;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.history.HistoricVariableInstanceQuery;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.BooleanUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.persistence.BaseEntity;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.modules.act.dao.ActDao;
import com.yunnex.ops.erp.modules.act.service.ActDefExtService;
import com.yunnex.ops.erp.modules.act.service.ActTaskService;
import com.yunnex.ops.erp.modules.act.utils.ActUtils;
import com.yunnex.ops.erp.modules.holiday.service.ErpHolidaysService;
import com.yunnex.ops.erp.modules.order.dto.FlowServiceItemLinkDto;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderGoodServiceInfo;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.order.entity.SplitGoodForm;
import com.yunnex.ops.erp.modules.order.service.ErpOrderGoodServiceInfoService;
import com.yunnex.ops.erp.modules.order.service.ErpOrderOriginalInfoService;
import com.yunnex.ops.erp.modules.order.service.ErpOrderSplitInfoService;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.shop.service.ErpShopInfoService;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreInfoService;
import com.yunnex.ops.erp.modules.store.pay.service.ErpStorePayUnionpayService;
import com.yunnex.ops.erp.modules.store.pay.service.ErpStorePayWeixinService;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitServiceInfo;
import com.yunnex.ops.erp.modules.workflow.delivery.entity.ErpDeliveryService;
import com.yunnex.ops.erp.modules.workflow.delivery.service.ErpDeliveryServiceService;
import com.yunnex.ops.erp.modules.workflow.flow.constant.DeliveryFlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.dao.ErpOrderInputDetailDao;
import com.yunnex.ops.erp.modules.workflow.flow.dto.FlowInfoDto;
import com.yunnex.ops.erp.modules.workflow.flow.dto.FlowTaskDto;
import com.yunnex.ops.erp.modules.workflow.flow.dto.FlowTaskGroupDto;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderInputDetail;
import com.yunnex.ops.erp.modules.workflow.flow.from.WorkFlowQueryForm;
import com.yunnex.ops.erp.modules.workflow.flow.handler.ProcessStartContext;
import com.yunnex.ops.erp.modules.workflow.flow.strategy.IGroup;
import com.yunnex.ops.erp.modules.workflow.flow.strategy.IPermission;
import com.yunnex.ops.erp.modules.workflow.flow.strategy.OperatingPermissionValdate;
import com.yunnex.ops.erp.modules.workflow.flow.strategy.ProcContext;
import com.yunnex.ops.erp.modules.workflow.flow.strategy.TaskOrderNoGroup;
import com.yunnex.ops.erp.modules.workflow.remarks.entity.WorkflowRemarksInfo;
import com.yunnex.ops.erp.modules.workflow.remarks.service.WorkflowRemarksInfoService;
import com.yunnex.ops.erp.modules.workflow.user.service.ErpOrderFlowUserService;

/**
 * 
 * @author Ejon
 * @date 2018年5月24日
 */
@Service
public class WorkFlow3p25Service extends CrudService<ErpOrderInputDetailDao, ErpOrderInputDetail> implements BaseFlowInfoService {

    private static final String FLOW_VERSION_3P25 = "flowMark3p25";
    private static final String NAME = "name";
    private static final String ROLE = "role";
    /*运营服务来源ID*/
    private static final String ORDER_GOOD_SERVICE_ITEMID = "orderGoodServiceItemId";
    private static final String ORDER_GOOD_SERVICE_ITEM_UPDATE_FLAG = "orderGoodServiceItemUpdate";
    public static final String MATERIAL_SERVICE = "material_service";
    public static final String MARKETING_PLANNING = "marketing_planning";
    public static final String ACCOUNT_PAY_OPEN = "account_pay_open";
    public static final String SERVICE_STARTUP = "service_startup";
    @Autowired
    private ActDao actDao;
    @Autowired
    private WorkflowRemarksInfoService workflowRemarksInfoService;
    @Autowired
    private ErpOrderFlowUserService erpOrderFlowUserService;
    @Autowired
    private ErpDeliveryServiceService erpDeliveryServiceService;
    @Autowired
    @Lazy(true)
    private ErpOrderOriginalInfoService erpOrderOriginalInfoService;
    @Autowired
    private ErpShopInfoService erpShopInfoService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private ActTaskService actTaskService;
    @Autowired
    @Lazy(true)
    private ErpOrderSplitInfoService erpOrderSplitInfoService;
    @Autowired
    private ErpOrderGoodServiceInfoService erpOrderGoodServiceInfoService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private ErpHolidaysService erpHolidaysService;
    @Autowired
    private ActDefExtService actDefExtService;
    @Autowired
    private ErpStoreInfoService erpStoreInfoService;
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private FlowInfoService flowInfoService;
    /**
     * 根据责任人 查询当前任务a分组
     *
     * @param queryForm
     * @return
     * @date 2018年5月26日
     * @author zjq
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public List<FlowTaskGroupDto> queryTaskListPage(WorkFlowQueryForm queryForm) {
        // 权限验证
        IPermission permission = new OperatingPermissionValdate();
        String actType = permission.getPermitted(queryForm.getActType());
        if (StringUtils.isNotBlank(actType)) {
            queryForm.setActType(actType);
        }
        // 权限不足
        if (StringUtils.isEmpty(actType)) {
            throw new AuthenticationException("权限不足!");
        }
        // 设置当前处理人
        queryForm.setAssignee(UserUtils.getUser().getId());
        // 分页查询当前任务
        List<FlowTaskDto> flowTaskDtos = actDao.queryTaskListPage(queryForm);
        queryForm.setTotal(actDao.queryTaskListCount(queryForm));
        queryForm.setTaskKey(actDao.queryTaskKeyList(queryForm));

        // 数据准备
        ProcContext procContext = new ProcContext();
        procContext.operateTaskDto(flowTaskDtos);
        // 任务分组
        IGroup group = new TaskOrderNoGroup();
        List<FlowTaskGroupDto> groups = group.group(flowTaskDtos);
        return groups;
    }


    /**
     * 运营服务任务数量统计
     *
     * @return
     * @date 2018年5月29日
     * @author zjq
     */
    public String operatingStatistics() {
        WorkFlowQueryForm queryForm = new WorkFlowQueryForm();
        queryForm.setAssignee(UserUtils.getUser().getId());
        return JSONArray.toJSONString(actDao.queryOperatingStatistics(queryForm));
    }

    /**
     * 启动售后上门流程
     *
     * @date 2018年6月1日
     * @author zjq
     */
    public String startVisitServiceFlow(ErpVisitServiceInfo erpVisitServiceInfo, Map<String, Object> vars) {
        if (vars == null) {
            vars = Maps.newHashMap();
        }
        String userId = UserUtils.getUser().getId();
        vars.put("OperationAdviser", userId);
        String procInsId = actTaskService.startProcess(ActUtils.VISIT_SERVICE_FLOW[0], ActUtils.VISIT_SERVICE_FLOW[1],
                        erpVisitServiceInfo.getId(), "启动售后上门流程", vars);
        erpOrderFlowUserService.insertOrderFlowUser(userId, erpVisitServiceInfo.getId(), erpVisitServiceInfo.getId(), "OperationAdviser", procInsId);
        return procInsId;
    }


    /**
     * 启动商户运营服务流程
     *
     * @date 2018年6月1日
     * @author zjq
     */
    public String startDeliveryServiceFlow(ErpOrderOriginalInfo erpOrderOriginalInfo, Map<String, Object> vars) {
        ErpDeliveryService erpDelivery = new ErpDeliveryService();
        erpDelivery.setOrderId(erpOrderOriginalInfo.getId());
        erpDelivery.setOrderNumber(erpOrderOriginalInfo.getOrderNumber());
        erpDelivery.setShopId(erpOrderOriginalInfo.getShopId());
        erpDelivery.setShopName(erpOrderOriginalInfo.getShopName());
        erpDelivery.setSource(erpOrderOriginalInfo.getOrderSource().toString());
        erpDelivery.setServiceType(vars.get(FlowConstant.SERVICETYPE)+"");
        
        String zhctActType=(String) vars.get(DeliveryFlowConstant.ZHCT_ACT_TYPE);
        //智慧餐厅交付服务流程
        if(StringUtils.isNotBlank(zhctActType) && DeliveryFlowConstant.SERVICE_TYPE_KE_ZHCT.equals(zhctActType)){
            erpDelivery.setZhctType(DeliveryFlowConstant.SERVICE_TYPE_KE_ZHCT);   
        }
        //智慧餐厅 （老商户） 流程
        else if(StringUtils.isNotBlank(zhctActType) && DeliveryFlowConstant.SERVICE_TYPE_KE_ZHCT_OLD.equals(zhctActType)){
            erpDelivery.setZhctType(DeliveryFlowConstant.SERVICE_TYPE_KE_ZHCT_OLD);  
            erpDeliveryServiceService.save(erpDelivery);
            //启动智慧餐厅 （老商户） 流程
            return actTaskService.startProcess(ActUtils.VISIT_SERVICE_ZHCT_FLOW, ActUtils.DELIVERY_SERVICE_FLOW[1], erpDelivery.getId(),
                    "启动智慧餐厅 （老商户） 流程", vars);
        }
        erpDeliveryServiceService.save(erpDelivery);
        vars.put(DeliveryFlowConstant.VISIT_TYPE, DeliveryFlowConstant.VISIT_TYPE_FMPS_I);
        //启动交付 流程
        return actTaskService.startProcess(ActUtils.DELIVERY_SERVICE_FLOW[0], ActUtils.DELIVERY_SERVICE_FLOW[1], erpDelivery.getId(),
                        "启动运营服务流程", vars);
    }

    /**
     * 
     * 获取流程通用模块信息
     * 
     * @param procInsId
     * @return
     * @date 2018年5月30日
     * @author zjq
     */
    public FlowInfoDto getProcGeneralInfo(String taskId, String procInsId, String processDefineKey) {

        FlowInfoDto flowInfoDto = new FlowInfoDto();

        flowInfoDto.setFlowName(processDefineKey);

        // 任务开始时间、结束时间，当前处理人
        if (StringUtils.isNoneBlank(taskId)) {
            setupTaskDateInfoAndAssignee(taskId, procInsId, flowInfoDto);
        }

        // 流程备注
        setupProcRemarks(procInsId, processDefineKey, flowInfoDto);


        // 订单信息模块
        ErpDeliveryService deliveryService = erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);

        if (null == deliveryService)
        {
            return flowInfoDto;
        }

        ErpOrderOriginalInfo erpOrderOriginalInfo = setupOrderInfo(flowInfoDto, deliveryService);

        // 订单处理人员信息模块
        setupProcAssignee(procInsId, flowInfoDto);

        // 商户进件状态
        ErpShopInfo shop = erpShopInfoService.getByZhangbeiID(erpOrderOriginalInfo.getShopId());
        wrapPayInfo(erpStoreInfoService, flowInfoDto, shop);

        // 订单处理进度
        Map<String, String> map = procProgress(procInsId);

        flowInfoDto.setFlowMap(map);
        return flowInfoDto;
    }


    /**
     * 设置订单信息
     *
     * @param flowInfoDto
     * @param deliveryService
     * @return
     * @date 2018年6月5日
     * @author zjq
     */
    private ErpOrderOriginalInfo setupOrderInfo(FlowInfoDto flowInfoDto, ErpDeliveryService deliveryService) {
        ErpOrderOriginalInfo erpOrderOriginalInfo = erpOrderOriginalInfoService.get(deliveryService.getOrderId());
        if (null != deliveryService && StringUtils.isNotBlank(deliveryService.getOrderId())) {
            flowInfoDto.setErpOrderOriginalInfo(erpOrderOriginalInfo);
        }
        return erpOrderOriginalInfo;
    }


    /**
     * 设置流程处理人信息
     *
     * @param procInsId
     * @param flowInfoDto
     * @date 2018年6月5日
     * @author zjq
     */
    private void setupProcAssignee(String procInsId, FlowInfoDto flowInfoDto) {
        List<Map<String, String>> maps = erpOrderFlowUserService.findByProcInsId(procInsId);
        Map<String, String> map = Maps.newHashMap();
        for (Map<String, String> _map : maps) {
            map.put(_map.get(ROLE), _map.get(NAME));
        }
        flowInfoDto.setErpOrderFlowUsers(map);
    }


    /**
     * 设置流程备注信息
     *
     * @param procInsId
     * @param processDefineKey
     * @param flowInfoDto
     * @date 2018年6月5日
     * @author zjq
     */
    private void setupProcRemarks(String procInsId, String processDefineKey, FlowInfoDto flowInfoDto) {
        List<WorkflowRemarksInfo> remarksInfo = workflowRemarksInfoService.findListByProcInsId(procInsId);
        flowInfoDto.setRemarksInfo(remarksInfo);
    }


    /**
     * 设置任务时间和处理人员信息
     *
     * @param taskId
     * @param procInsId
     * @param flowInfoDto
     * @date 2018年6月5日
     * @author zjq
     */
    private void setupTaskDateInfoAndAssignee(String taskId, String procInsId, FlowInfoDto flowInfoDto) {
        Task task = taskService.createTaskQuery().processInstanceId(procInsId).taskId(taskId).singleResult();
        
        int taskHour = Integer.parseInt(actDefExtService.getByActId(task.getTaskDefinitionKey()).getTaskDateHours());
        flowInfoDto.setStartDate(task.getCreateTime());
        try {
            flowInfoDto.setEndDate(erpHolidaysService.enddate(task.getCreateTime(), taskHour));
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
            flowInfoDto.setEndDate(null);
        }
        flowInfoDto.setAssignee(UserUtils.get(task.getAssignee()).getName());
    }

    /**
     * 流程处理进度
     *
     * @param procInsId
     * @return
     * @date 2018年6月5日
     * @author zjq
     */
    private Map<String, String> procProgress(String procInsId) {
        Map<String, String> map;
        map = new HashMap<String, String>();
        HistoricVariableInstanceQuery hisquery = historyService.createHistoricVariableInstanceQuery()
                        .processInstanceId(procInsId);
        map.put(SERVICE_STARTUP, queryHistoricVariableInstanceVal(hisquery.variableName(SERVICE_STARTUP).singleResult()));
        map.put(ACCOUNT_PAY_OPEN, queryHistoricVariableInstanceVal(hisquery.variableName(ACCOUNT_PAY_OPEN).singleResult()));
        map.put(MARKETING_PLANNING, queryHistoricVariableInstanceVal(hisquery.variableName(MARKETING_PLANNING).singleResult()));
        map.put(MATERIAL_SERVICE, queryHistoricVariableInstanceVal(hisquery.variableName(MATERIAL_SERVICE).singleResult()));
        return map;
    }


    private String queryHistoricVariableInstanceVal(HistoricVariableInstance historicVariableInstance) {
        if (null == historicVariableInstance)
        {
            return StringUtils.EMPTY;
        }
        return historicVariableInstance.getValue().toString();
    }


    /**
     * 启动运营服务流程
     *
     * @param splitGoodLists
     * @param erpOrderOriginalInfo
     * @date 2018年6月4日
     * @author zjq
     */
    @Transactional(readOnly = false)
    public void startOperatingServiceProcess(List<SplitGoodForm> splitGoodLists, ErpOrderOriginalInfo erpOrderOriginalInfo) {
        ProcessStartContext.startByBeihuOrder(erpOrderOriginalInfo, splitGoodLists);
    }

    /**
     * 流程结束更新流程对应服务项次数
     *
     * @return
     * @date 2018年6月4日
     * @author zjq
     */
    @Transactional(readOnly = false)
    public void updateServiceItemNum(String procInsId) {
        
        Optional<Object> deliveryId = Optional.ofNullable(runtimeService.getVariable(procInsId, ORDER_GOOD_SERVICE_ITEMID));

        Optional<Object> isUpdate = Optional.ofNullable(runtimeService.getVariable(procInsId, ORDER_GOOD_SERVICE_ITEM_UPDATE_FLAG));

        if (BooleanUtils.isNotTrue(BooleanUtils.toBoolean(isUpdate.orElse(FlowConstant.RESULT_FALSE).toString()))) {

            deliveryId.ifPresent((obj) -> {

                List<String> items = Arrays.asList(obj.toString().split(Constant.COMMA));

                items.forEach(str -> {

                    Optional<ErpOrderGoodServiceInfo> erpOrderGoodServiceInfo = Optional
                                    .ofNullable(erpOrderGoodServiceInfoService.get(str));

                    erpOrderGoodServiceInfo.ifPresent((serviceItem) -> {

                        // 更新服务项目次数
                        serviceItem.setProcessNum(serviceItem.getProcessNum() - 1);

                        serviceItem.setFinishNum(serviceItem.getFinishNum() + 1);

                        erpOrderGoodServiceInfoService.save(serviceItem);

                    });
                });
                runtimeService.setVariable(procInsId, ORDER_GOOD_SERVICE_ITEM_UPDATE_FLAG, FlowConstant.RESULT_TRUE);

                logger.info("updateServiceItemNum success,deliveryId[{}]", deliveryId);

            });

        }
    }

    /**
     * 流程启动，更新流程服务项次数
     *
     * @param splitGoodLists
     * @param erpOrderOriginalInfo
     * @param vars
     * @param goodServiceInfos
     * @date 2018年7月16日
     * @author zjq
     */
    @Transactional(readOnly = false)
    public void startDeliveryFlow(List<SplitGoodForm> splitGoodLists, ErpOrderOriginalInfo erpOrderOriginalInfo, Map<String, Object> vars,
                    ErpOrderGoodServiceInfo... goodServiceInfos) {

        logger.info("交付服务流程 —— 交付服务流程,splitGoodLists[{}],订单id[{}],订单编号[{}]", splitGoodLists, erpOrderOriginalInfo.getId(),
                        erpOrderOriginalInfo.getOrderNumber());

        List<ErpOrderGoodServiceInfo> infos = Arrays.asList(goodServiceInfos);

        //修改商品服务数量
        infos.stream().forEach(serviceItem -> {
            serviceItem.setPendingNum(serviceItem.getPendingNum() - 1);
            serviceItem.setProcessNum(serviceItem.getProcessNum() + 1);
            erpOrderGoodServiceInfoService.save(serviceItem);
        });

        vars.put(ORDER_GOOD_SERVICE_ITEMID, vars.get(FlowConstant.SERVICE_SOURCE_ID)+StringUtils.EMPTY);

        String procInsId = startDeliveryServiceFlow(erpOrderOriginalInfo, vars);
        
        //建立服务项目和流程的关联关系
        addFlowServiceLinkData(vars, procInsId);
        
        vars.put(FlowConstant.PROCINSID, procInsId);
        logger.info("交付服务流程 —— 交付服务流程,procInsId[{}],vars[{}]", procInsId, vars);
    }
    

    public FlowInfoDto getFlowInfo(String taskId, String procInsId, String flowMark) {

        FlowInfoDto flowInfoDto = null;

        HistoricProcessInstance pi = processEngine.getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(procInsId)
                        .singleResult();
        if (pi.getProcessDefinitionKey().startsWith(ActUtils.VISIT_SERVICE_FLOW[0]) || pi.getProcessDefinitionKey()
                        .startsWith(ActUtils.DELIVERY_SERVICE_FLOW[0])|| pi.getProcessDefinitionKey()
                                .startsWith(ActUtils.VISIT_SERVICE_ZHCT_FLOW))
        {
            flowInfoDto = this.getProcGeneralInfo(taskId, procInsId, pi.getProcessDefinitionKey());
            flowInfoDto.setFlowMark(FLOW_VERSION_3P25);
            return flowInfoDto;
        }
        flowInfoDto = flowInfoService.getFlowInfo(procInsId);
        flowInfoDto.setFlowMark(flowMark);
        return flowInfoDto;
    }
    
     public void addFlowServiceLinkData(Map<String, Object> vars,String procInsId){
          String ServiceSourceIds=vars.get(FlowConstant.SERVICE_SOURCE_ID)+StringUtils.EMPTY;
          if(StringUtils.isNotBlank(ServiceSourceIds) ){
             String[] ServiceSourceId = ServiceSourceIds.split(Constant.COMMA);
             FlowServiceItemLinkDto dto=new FlowServiceItemLinkDto();
             dto.setProcInsId(procInsId);
             dto.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
             for(String id:ServiceSourceId){
                 dto.setServiceSourceId(id);
                 erpOrderGoodServiceInfoService.addErpFlowServiceItemLink(dto);   
            }
         } 
     }
    
    
}
