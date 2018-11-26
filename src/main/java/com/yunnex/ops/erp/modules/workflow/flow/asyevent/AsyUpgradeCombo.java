package com.yunnex.ops.erp.modules.workflow.flow.asyevent;

import java.util.List;
import java.util.Optional;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.common.constant.CommonConstants;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.utils.IdGen;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.modules.act.dao.ActDao;
import com.yunnex.ops.erp.modules.act.dao.TaskExtDao;
import com.yunnex.ops.erp.modules.act.service.ActProInstService;
import com.yunnex.ops.erp.modules.act.utils.ActUtils;
import com.yunnex.ops.erp.modules.material.entity.ErpOrderMaterialCreation;
import com.yunnex.ops.erp.modules.material.service.ErpOrderMaterialCreationService;
import com.yunnex.ops.erp.modules.message.service.ErpServiceMessageService;
import com.yunnex.ops.erp.modules.message.service.ErpServiceProgressService;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.order.service.ErpOrderMaterialApiService;
import com.yunnex.ops.erp.modules.order.service.ErpOrderOriginalInfoService;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.shop.service.ErpShopInfoService;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreInfoService;
import com.yunnex.ops.erp.modules.sys.service.SysConstantsService;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitServiceInfo;
import com.yunnex.ops.erp.modules.visit.service.ErpVisitServiceInfoService;
import com.yunnex.ops.erp.modules.visit.service.ErpVisitServiceProductRecordService;
import com.yunnex.ops.erp.modules.workflow.delivery.entity.ErpDeliveryService;
import com.yunnex.ops.erp.modules.workflow.delivery.service.ErpDeliveryServiceService;
import com.yunnex.ops.erp.modules.workflow.flow.common.WorkFlowConstants;
import com.yunnex.ops.erp.modules.workflow.flow.constant.DeliveryFlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.dao.ErpFlowFormDao;
import com.yunnex.ops.erp.modules.workflow.flow.dao.ErpFlowFormHisDao;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpFlowForm;
import com.yunnex.ops.erp.modules.workflow.flow.service.DeliveryFlowService;
import com.yunnex.ops.erp.modules.workflow.flow.service.WorkFlow3p25Service;
import com.yunnex.ops.erp.modules.workflow.flow.service.WorkFlowMonitorService;
import com.yunnex.ops.erp.modules.workflow.flow.service.WorkFlowService;

/**
 * @author hanhan
 * @date 2018年8月27日
 */
@Component
public class AsyUpgradeCombo {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ErpDeliveryServiceService erpDeliveryServiceService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private ActProInstService actProInstService;
    @Autowired
    private ErpVisitServiceInfoService erpVisitServiceInfoService;
    @Autowired
    private ErpVisitServiceProductRecordService erpVisitServiceProductRecordService;
    @Autowired
    private WorkFlowMonitorService workFlowMonitorService;
    @Autowired
    private ErpShopInfoService erpShopInfoService;
    @Autowired
    private ErpStoreInfoService erpStoreInfoService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ActDao actDao;
    @Autowired
    private SysConstantsService sysConstantsService;
    @Autowired
    private ErpServiceMessageService serviceMessageService;
    @Autowired
    private ErpServiceProgressService serviceProgressService;
    @Autowired
    private ErpOrderOriginalInfoService erpOrderOriginalInfoService;
    @Autowired
    private ErpOrderMaterialApiService erpOrderMaterialApiService;
    @Autowired
    private ErpOrderMaterialCreationService erpOrderMaterialCreationService;
    @Autowired
    private DeliveryFlowService deliveryFlowService;
    @Autowired
    private TaskExtDao taskExtDao;
    @Autowired
    private ErpFlowFormDao erpFlowFormDao;
    @Autowired
    private ErpFlowFormHisDao erpFlowFormHisDao;
    @Autowired
    private WorkFlow3p25Service workFlowService;
    
    @Transactional(readOnly = false)
    @Async // 必须有次注解
    public void upCombo(ErpOrderOriginalInfo erpOrderOriginalInfo, String procInsId, String newzhctFlag) {
        logger.info("客流全套落地服务套餐升级 ,订单信息[{}],流程id[{}]", erpOrderOriginalInfo, procInsId);
        try {
            Thread.sleep(2* 1000);
            String zhangbeiId = erpOrderOriginalInfo.getShopId();
            // 订单id
            String orderId = erpOrderOriginalInfo.getId();
            // 判断要不要升级套餐
            ErpDeliveryService deliveryService = isEasyPlus(zhangbeiId, procInsId);
            if (deliveryService == null) {
                logger.info("不需要套餐升级 流程id[{}]", procInsId);
                return;
            }
            // 设置交付服务的升级状态
            deliveryService.setUpgradeFlag(CommonConstants.Sign.YES);
            erpDeliveryServiceService.save(deliveryService);
           //维护订单信息
            updateOrderInfo(erpOrderOriginalInfo.getId(), deliveryService.getOrderId(), procInsId);
            String oldProcInsId = deliveryService.getProcInsId();
            // 流程版本
            String procDefId = taskService.createTaskQuery().active().processInstanceId(procInsId).singleResult().getProcessDefinitionId();
            actProInstService.deleteRuTaskByProcId(procInsId);
            //actProInstService.deleteHiVarinstByProId(procInsId, oldProcInsId);
            //ACT_HI_VARINST表
           // actProInstService.addBatchHiVarinst(procInsId, oldProcInsId);
            actProInstService.managerUpgradeVariable(procInsId, oldProcInsId);
            // 插入现在的流程节点信息 ACT_RU_VARIABLE表
            HistoricVariableInstance hiVariableZhctFlag = historyService.createHistoricVariableInstanceQuery().processInstanceId(oldProcInsId)
                            .variableName(DeliveryFlowConstant.ZHCT_FLAG).singleResult();
            String oldzhctFlag = "";
            if (null != hiVariableZhctFlag) {
                oldzhctFlag = hiVariableZhctFlag.getValue() + StringUtils.EMPTY;
            }
            String zhctActType="";
            if (Constant.YES.equals(newzhctFlag) || Constant.YES.equals(oldzhctFlag)) {
                // 老的有智慧餐厅 新的没有 修改为有
                actProInstService.updateRuVarByProcId(procInsId, Constant.YES, DeliveryFlowConstant.ZHCT_FLAG);
                actProInstService.updateHiVarByProcId(procInsId, Constant.YES, DeliveryFlowConstant.ZHCT_FLAG);
                zhctActType=DeliveryFlowConstant.SERVICE_TYPE_KE_ZHCT;
                ErpDeliveryService newDeliveryService =erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
                newDeliveryService.setZhctType(DeliveryFlowConstant.SERVICE_TYPE_KE_ZHCT);
                erpDeliveryServiceService.save(newDeliveryService);
            }
            // 保存启动时间 和 其他一些时间
            deliveryFlowService.saveStartTimeOther(procInsId, DeliveryFlowConstant.SERVICE_TYPE_KE_CHANG_LAI,zhctActType);
            actProInstService.updateRuVarByProcId(procInsId, DeliveryFlowConstant.SERVICE_TYPE_KE_CHANG_LAI, DeliveryFlowConstant.SERVICE_TYPE);
            actProInstService.updateHiVarByProcId(procInsId,DeliveryFlowConstant.SERVICE_TYPE_KE_CHANG_LAI, DeliveryFlowConstant.SERVICE_TYPE);
            actProInstService.updateRuVarByProcId(procInsId, Constant.YES,DeliveryFlowConstant.UP_FLOW_FLAG);
            actProInstService.updateHiVarByProcId(procInsId,Constant.YES, DeliveryFlowConstant.UP_FLOW_FLAG);
            
            // 老流程有 新流程没有 添加新流程的 智慧餐厅服务项绑定关系
            if (!Constant.YES.equals(newzhctFlag) && Constant.YES.equals(oldzhctFlag)) {
                // 老的有智慧餐厅 新的没有 修改为有
                String id = IdGen.uuid();
                actProInstService.addGoodServiceInfoById(id, erpOrderOriginalInfo.getId(), oldProcInsId,
                                sysConstantsService.getConstantValByKey(WorkFlowConstants.DELIVERY_ITEM_CODE_ZHCT));
                actProInstService.addFlowServiceItemLink(procInsId, id);
            }
            workFlowService.updateServiceItemNum(oldProcInsId);
            try {
             // 服务通知 处理
                serviceMessageService.upgradeFmpsBasic(procInsId, oldProcInsId, false, false);
                // 服务进度 处理
                serviceProgressService.upgradeFmpsBasic(procInsId, oldProcInsId, orderId, false, false, false);
            } catch (Exception e) {
                logger.error("服务升级，服务通知与进度错误:", e);
            }
            actProInstService.updateRuVarByProcId(procInsId, DeliveryFlowConstant.SERVICE_TYPE_KE_CHANG_LAI, DeliveryFlowConstant.SERVICE_TYPE);
            // ACT_HI_TASKINST 表
            actProInstService.deleteHiTaskByProcId(procInsId);
            actProInstService.addHiTaskInstByProcInsId(procInsId, oldProcInsId);
            // ACT_HI_PROCINST 表
            actProInstService.deleteHiProcInstByProcId(procInsId);
            actProInstService.addBatchHiProcInst(procInsId, oldProcInsId);
            // ACT_HI_ACTINST 表
            actProInstService.deleteHiactInstByProId(procInsId);
            actProInstService.addBatchHiActinst(procInsId, oldProcInsId, procDefId);
            // erp_order_flow_user表修改
            actProInstService.deleteFlowUserByProInsId(procInsId);
            actProInstService.addBatchOrderFlowUser(procInsId, oldProcInsId, erpOrderOriginalInfo.getId());
            // 判断在 开通服务监听之前 返回true,之后false
            boolean resultKey = findHisProcessKey(procInsId, DeliveryFlowConstant.SHOP_INFO_COLLECTION);
            if (!resultKey) {
                actProInstService.addBatchRuTask(procInsId, oldProcInsId, procDefId);
                actProInstService.updateRuTaskExIdByproId(procInsId, oldProcInsId);
                // ACT_RU_EXECUTION表
                String actId = runtimeService.createExecutionQuery().executionId(oldProcInsId).singleResult().getActivityId();
                actProInstService.updateActIdByProId(actId, procInsId);

                // 删除老的流程变量
                actDao.deleteVaribleByInstId(oldProcInsId);
                // 删除老的主干实例
                // actProInstService.deleteExecuIionByProId(oldProcInsId);
                actProInstService.updateExeCuTionByPid(procInsId, oldProcInsId, procDefId);
                actProInstService.updateEcuParentId(procInsId, oldProcInsId);
                /*
                 * 判断 （是新商户&& 掌贝后台创建门店 已经结束） || （老商户&& 进件资料收集 已经结束 ru_task 表 插入 ’掌贝后台创建门店‘ 这个节点信息
                 * 任务历史表也要插入这个节点信息
                 */
                boolean result3 = isGoZhctMenuKey(newzhctFlag, deliveryService, oldProcInsId);
                if (result3) {
                    /* ru_task 表 ACT_HI_TASKINST 表 插入 ’掌贝后台创建门店‘ 这个节点信息 */
                    // 插入ru_excluel
                    AsyUpgradeComboDto asyDto = new AsyUpgradeComboDto();
                    String id = IdGen.uuid();
                    asyDto.setId(id);
                    asyDto.setOldProcInsId(oldProcInsId);
                    asyDto.setProcInsId(procInsId);
                    asyDto.setActId(DeliveryFlowConstant.ZHCT_MENU_CONFIGURATION);
                    asyDto.setName(DeliveryFlowConstant.ZHCT_MENU_CONFIGURATION_NAME);
                    asyDto.setProcDefId(procDefId);
                    asyDto.setExcutionId(id);
                    actProInstService.addActRuExcution(asyDto);
                    actProInstService.addRuTaskExcuId(asyDto);
                    actProInstService.addHiTaskInst(asyDto);
                    // addZhctNode(procInsId, oldProcInsId, "智慧餐厅菜单配置",
                    // "zhct_menu_configuration",procDefId,null);
                }
                actProInstService.deleteRuTaskByProcId(oldProcInsId);
            } else {
                // 之前不包含 现在包含 智慧餐厅节点
                // boolean oldProcInsZhctMenuKey= taskEnd(oldProcInsId,"zhct_menu_configuration");
                /*
                 * //插入现在的流程节点信息 ACT_RU_VARIABLE表 HistoricVariableInstance hiVariableZhctFlag =
                 * historyService.createHistoricVariableInstanceQuery()
                 * .processInstanceId(oldProcInsId).variableName(DeliveryFlowConstant.ZHCT_FLAG).
                 * singleResult(); String zhctFlag = ""; if(null!=hiVariableZhctFlag){ zhctFlag =
                 * hiVariableZhctFlag.getValue()+StringUtils.EMPTY; } if("Y".equals(zhctFlag)
                 * &&!oldProcInsZhctMenuKey){ //跳到智慧餐厅节点 // addZhctNode(procInsId, oldProcInsId,
                 * "智慧餐厅菜单配置", "zhct_menu_configuration"); }
                 */
                // 跳到 商户资料收集节点
                addZhctNode(procInsId, oldProcInsId, DeliveryFlowConstant.SHOP_INFO_COLLECTION_NAME, DeliveryFlowConstant.SHOP_INFO_COLLECTION,
                                procDefId, Constant.YES);
                actProInstService.addBatchRuTask(procInsId, oldProcInsId, procDefId);
                // actProInstService.updateRuTaskExIdByproId(procInsId, oldProcInsId);
                // ACT_RU_EXECUTION表
                String actId = runtimeService.createExecutionQuery().executionId(oldProcInsId).singleResult().getActivityId();

                actProInstService.updateActIdByProId(actId, procInsId);
                // 删除老的流程变量
                actDao.deleteVaribleByInstId(oldProcInsId);
                // 删除老的主干实例
                // actProInstService.deleteExecuIionByProId(oldProcInsId);
                actProInstService.deleteRuTaskByProcId(oldProcInsId);
                actProInstService.updateExeCuTionByPid(procInsId, oldProcInsId, procDefId);
                actProInstService.updateEcuParentId(procInsId, oldProcInsId);
                actProInstService.updteExecActId("shop_info_collection", procInsId);
                actProInstService.deletRuExcuByproIdAndActId("shop_info_collection", procInsId);
                // 之前包含 现在不包含
                List<ErpVisitServiceInfo> listVistInfo = erpVisitServiceInfoService.getListByProcIdAndGoalCode(oldProcInsId, FlowConstant.ZHCT_GOAL);
                // 删除上门服务数据（除了智慧餐厅的）
                for (ErpVisitServiceInfo visitServiceInfo : listVistInfo) {
                    erpVisitServiceInfoService.deleteServiceItemRecordDataByServiceId(visitServiceInfo.getId());
                    erpVisitServiceProductRecordService.deleteVisitProductRecordByVisitId(visitServiceInfo.getId());
                    erpVisitServiceInfoService.delete(visitServiceInfo);
                }
            }
            // 修改上门智慧餐厅的流程id
            ErpVisitServiceInfo erpVisitServiceInfo = erpVisitServiceInfoService.getByGoalCode(oldProcInsId, FlowConstant.ZHCT_GOAL);
            if (erpVisitServiceInfo != null) {
                erpVisitServiceInfo.setProcInsId(procInsId);
                erpVisitServiceInfoService.save(erpVisitServiceInfo);
            }
            actProInstService.deleteRuTaskByProcId(oldProcInsId);
            /*try {
                workFlowMonitorService.endProcess(oldProcInsId);
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            // coppy流程表单数据
            actProInstService.addBatchFlowForm(procInsId, oldProcInsId);
            // coppy 流程文件
            actProInstService.addBatchOrderFile(procInsId, oldProcInsId);
            
            //流程结束shjian
            
            updateOldFlowInfo(oldProcInsId);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("我==啊!=========");
    }


    
    //流程结束时间
    public void updateOldFlowInfo(String procInsId){
        // 流程id
        taskExtDao.deleteTaskExtsByProcInsId(procInsId);
        // 删除 erp_flow_form
        List<ErpFlowForm> erpFlowFormList = erpFlowFormDao.findByProcInsId(procInsId);
        if (!CollectionUtils.isEmpty(erpFlowFormList)) {
            erpFlowFormHisDao.batchInsertFlowFormData(erpFlowFormList);
        }
        erpFlowFormDao.deleteByProcInsId(procInsId);
        // 交付服务流程 结束保存结束时间
            erpDeliveryServiceService.saveFlowEndTime(procInsId);
        // 流程结束，如果为正常结束的流程则更新服务项次数
           
    }
    
    
    public void updateOrderInfo(String newOrderId,String oldOrderId ,String procInsId){
        //维护订单表
        ErpOrderOriginalInfo oldOrderOriginalInfo= erpOrderOriginalInfoService.get(oldOrderId);
        ErpOrderOriginalInfo newOrderOriginalInfo= erpOrderOriginalInfoService.get(newOrderId);
        long ysOrderId=oldOrderOriginalInfo.getYsOrderId();
        newOrderOriginalInfo.setOrderCategory(oldOrderOriginalInfo.getOrderCategory());
        newOrderOriginalInfo.setYsOrderId(oldOrderOriginalInfo.getYsOrderId());
        newOrderOriginalInfo.setYsOrderBuyTime(oldOrderOriginalInfo.getYsOrderBuyTime());
        newOrderOriginalInfo.setYsOrderRealPrice(oldOrderOriginalInfo.getYsOrderRealPrice());
        newOrderOriginalInfo.setIsAuditOrder(oldOrderOriginalInfo.getIsAuditOrder());
        erpOrderOriginalInfoService.save(newOrderOriginalInfo);
        // 先保存升级后的订单号，这样才能在同步物料时拿到
        oldOrderOriginalInfo.setUpOrderNumber(newOrderOriginalInfo.getOrderNumber());
        erpOrderOriginalInfoService.save(oldOrderOriginalInfo); 
        if(ysOrderId!=0){
            erpOrderMaterialApiService.syncOneOrderMaterial(oldOrderOriginalInfo.getYsOrderId());
        }else{
            ErpOrderMaterialCreation orderMaterialCreation= erpOrderMaterialCreationService.findByOrderNumber(oldOrderOriginalInfo.getOrderNumber());
          if(orderMaterialCreation!=null){
             orderMaterialCreation.setOrderNumber(newOrderOriginalInfo.getOrderNumber());
             orderMaterialCreation.setProcInsId(procInsId);
             erpOrderMaterialCreationService.save(orderMaterialCreation);
          }
        }
        // 除升级订单号外的信息要在同步完物料后更新
        oldOrderOriginalInfo.setIsAuditOrder(Constant.NO);
        oldOrderOriginalInfo.setYsOrderId((long) 0);
        erpOrderOriginalInfoService.save(oldOrderOriginalInfo);
    }
    
    
    
    public void addZhctNode(String procInsId, String oldProcInsId, String name, String taskDefKey, String procDefId, String type) {
        /* ru_task 表 插入 ’掌贝后台创建门店‘ 这个节点信息 */
        AsyUpgradeComboDto asyDto = new AsyUpgradeComboDto();
        String id = IdGen.uuid();
        asyDto.setId(id);
        asyDto.setOldProcInsId(oldProcInsId);
        asyDto.setProcInsId(procInsId);
        asyDto.setName(name);
        asyDto.setTaskDefKey(taskDefKey);
        asyDto.setProcDefId(procDefId);
        asyDto.setType(type);
        actProInstService.addRuTask(asyDto);
        // ACT_HI_TASKINST
       // actProInstService.addHiTaskInst(asyDto);
        actProInstService.updateActInstTimeByProId(procInsId, taskDefKey);
        
        
    }

    public boolean isGoZhctMenuKey(String newzhctFlag, ErpDeliveryService deliveryService, String oldProcInsId) {
        ErpShopInfo shopinfo = erpShopInfoService.getByZhangbeiID(deliveryService.getShopId());
        // 有无 掌贝账号 根据掌贝进件状态判断
        ErpStoreInfo erpStoreInfo = erpStoreInfoService.findismain(shopinfo.getId(), Global.NO);
        //// 第一次不包含 升级后包含

        if (Constant.YES.equals(newzhctFlag) && !DeliveryFlowConstant.SERVICE_TYPE_KE_ZHCT.equals(deliveryService.getZhctType())) {
            /* 判断 （是新商户&& 掌贝后台创建门店 已经结束） || （老商户&& 进件资料收集 已经结束）{ */
            if (erpStoreInfo != null && "2".equals(erpStoreInfo.getAuditStatus() + "")) {
                boolean result1 = taskEnd(oldProcInsId, DeliveryFlowConstant.INTO_MATERIAL_COLLECTION);
                return result1;
            } else {
                boolean result2 = taskEnd(oldProcInsId, DeliveryFlowConstant.ZHANGBEI_STORE_CREATE);
                return result2;
            }
        }
        return false;

    }

    // 判断某个节点有点完成
    public boolean taskEnd(String procInsId, String taskKey) {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().processInstanceId(procInsId).list();
        if (list != null && list.size() > 0) {
            for (HistoricTaskInstance hi : list) {
                if (hi.getTaskDefinitionKey().equals(taskKey) && null != hi.getEndTime()) {
                    return true;
                }
            }
        }
        return false;
    }

    // 判断在 某个节点有没走 返回true,之后false
    public boolean findHisProcessKey(String procInsId, String taskKey) {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().processInstanceId(procInsId).list();
        if (list != null && list.size() > 0) {
            for (HistoricTaskInstance hi : list) {
                if (hi.getTaskDefinitionKey().equals(taskKey)) {
                    return true;
                }
            }
        }
        return false;
    }

    // 套餐否升级 升级返回老的流程id
    public ErpDeliveryService isEasyPlus(String zhangbeiId, String procInsId) {
        ErpDeliveryService deliveryService = erpDeliveryServiceService.getDeliveryInfoByShopIdAndServiceTypeDesc(zhangbeiId,
                        DeliveryFlowConstant.SERVICE_TYPE_KE_CHANG_LAI_BASIC, procInsId);
        List<Task> tasks = null;
        if (null != deliveryService) {
            tasks = workFlowMonitorService.getCurrentTasks(deliveryService.getProcInsId());

        }
        if (deliveryService != null && !CollectionUtils.isEmpty(tasks) && tasks.size() > 0) {
            return deliveryService;
        }
        return null;
    }

    public static void main(String[] args) {
        String a = "into_material_collection";
        if (a.equals(DeliveryFlowConstant.INTO_MATERIAL_COLLECTION)) {
            System.out.println("我==啊!==");
        }
    }


}
