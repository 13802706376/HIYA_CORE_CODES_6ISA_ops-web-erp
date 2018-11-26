package com.yunnex.ops.erp.modules.workflow.flow.strategy;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import java.util.HashMap;
import java.util.Map;
import org.activiti.engine.TaskService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yunnex.ops.erp.common.persistence.BaseEntity;
import com.yunnex.ops.erp.modules.holiday.service.ErpHolidaysService;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.shop.service.ErpShopInfoService;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreInfoService;
import com.yunnex.ops.erp.modules.store.constant.StoreConstant;
import com.yunnex.ops.erp.modules.store.pay.entity.ErpStorePayUnionpay;
import com.yunnex.ops.erp.modules.store.pay.entity.ErpStorePayWeixin;
import com.yunnex.ops.erp.modules.store.pay.service.ErpStorePayUnionpayService;
import com.yunnex.ops.erp.modules.store.pay.service.ErpStorePayWeixinService;
import com.yunnex.ops.erp.modules.workflow.delivery.entity.ErpDeliveryService;
import com.yunnex.ops.erp.modules.workflow.delivery.service.ErpDeliveryServiceService;
import com.yunnex.ops.erp.modules.workflow.flow.constant.DeliveryFlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.dto.FlowInfoTask;

/**
 * 交付服务流程操作
 * 
 * @author Ejon
 * @date 2018年7月10日
 */
@Service
public class DeliveryProcessOperate implements IProcOperate {


      protected static Logger logger = LoggerFactory.getLogger(DeliveryProcessOperate.class);
    public static  Map<String, String> nodeExtensionNameMap =null;
    static{
        // 加载节点扩展名信息
        logger.info("start to get fileRule from db..");
        nodeExtensionNameMap=new HashMap<String, String>();
        nodeExtensionNameMap.put(DeliveryFlowConstant.SERVICE_TYPE_KE_CHANG_LAI, "智能全套客流落地服务");
        nodeExtensionNameMap.put(DeliveryFlowConstant.SERVICE_TYPE_JU_YIN_KE, "聚引客上门交付服务");
        nodeExtensionNameMap.put(DeliveryFlowConstant.SERVICE_TYPE_KE_CHANG_LAI_BASIC, "首次上门服务基础版");
        nodeExtensionNameMap.put(DeliveryFlowConstant.SERVICE_TYPE_VC, "售后上门服务收费");
        nodeExtensionNameMap.put(DeliveryFlowConstant.VISIT_TYPE_FMPS_M, "物料实施服务");
    }    @Autowired
    private ErpHolidaysService erpHolidaysService;

    @Autowired
    private ErpDeliveryServiceService erpDeliveryServiceService;

    @Autowired
    private ErpShopInfoService erpShopInfoService;
    @Autowired
    private ErpStoreInfoService erpStoreInfoService;
    @Autowired
    private ErpStorePayWeixinService erpStorePayWeixinService;
    @Autowired
    private ErpStorePayUnionpayService erpStorePayUnionpayService;
    @Autowired
    private TaskService taskService;

    @Override
    public void operate(FlowInfoTask task) {

        // 任务进度时间
        try {
            task.setTaskEndDate(erpHolidaysService.enddate(task.getTaskStartDate(), task.getTaskHour()));
            task.setTaskConsumTime(computingTaskSchedule(task.getTaskStartDate(), task.getTaskHour()));
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        }
        // 提示任务显示进度
        // (0 :未进件, 1:待审核2:审核通过3:审核未通过4:已下架)
        if (task.getTaskRef().startsWith(DeliveryFlowConstant.ZHANGBEI_ACCOUNT_OPEN)) {
            ErpDeliveryService deliveryService = erpDeliveryServiceService.getDeliveryInfoByProsIncId(task.getProcInsId());
            ErpShopInfo erpShopInfo = erpShopInfoService.getByZhangbeiID(deliveryService.getShopId());
            ErpStoreInfo mainStore = erpStoreInfoService.findismain(erpShopInfo.getId(), BaseEntity.DEL_FLAG_NORMAL);
            String state="未提交";
            if(null!=mainStore){
                state =StoreConstant.StoreAuditStatus.getByStatus(mainStore.getAuditStatus());
            }
            task.setTaskName(task.getTaskName().concat("(").concat(state).concat(")"));
        }
        if (task.getTaskRef().startsWith(DeliveryFlowConstant.WECHAT_ACCOUNT_OPEN)) {
            ErpDeliveryService deliveryService = erpDeliveryServiceService.getDeliveryInfoByProsIncId(task.getProcInsId());
            ErpShopInfo erpShopInfo = erpShopInfoService.getByZhangbeiID(deliveryService.getShopId());
            ErpStoreInfo mainStore = erpStoreInfoService.findismain(erpShopInfo.getId(), BaseEntity.DEL_FLAG_NORMAL);
            String state="未提交";
            ErpStorePayWeixin erpStorePayWeixin =null;
            if(null!=mainStore){
               erpStorePayWeixin = erpStorePayWeixinService.get(mainStore.getWeixinPayId());
            }
            if (null != erpStorePayWeixin&&erpStorePayWeixin.getAuditStatus()!=null){
                state= StoreConstant.PayAuditStatus.getByStatus(erpStorePayWeixin.getAuditStatus());
            }
            task.setTaskName(task.getTaskName().concat("(").concat(state).concat(")"));
        }
        if (task.getTaskRef().startsWith(DeliveryFlowConstant.UNIONPAY_ACCOUNT_OPEN)) {
            ErpDeliveryService deliveryService = erpDeliveryServiceService.getDeliveryInfoByProsIncId(task.getProcInsId());
            ErpShopInfo erpShopInfo = erpShopInfoService.getByZhangbeiID(deliveryService.getShopId());
            ErpStoreInfo mainStore = erpStoreInfoService.findismain(erpShopInfo.getId(), BaseEntity.DEL_FLAG_NORMAL);
            ErpStorePayUnionpay erpStorePayUnionpay =null;
            String state="未提交";
            if(null!=mainStore){
                erpStorePayUnionpay = erpStorePayUnionpayService.get(mainStore.getUnionpayId());
            }
            if (null != erpStorePayUnionpay&&erpStorePayUnionpay.getAuditStatus()!=null){
                state= StoreConstant.PayAuditStatus.getByStatus(erpStorePayUnionpay.getAuditStatus());
            }
            task.setTaskName(task.getTaskName().concat("(").concat(state)
                            .concat(")"));
        }
        if(task.getTaskRef().startsWith(DeliveryFlowConstant.VISIT_SERVICE_SUBSCRIBE_PUBLIC)||task.getTaskRef().startsWith(DeliveryFlowConstant.VISIT_SERVICE_APPLY_PUBLIC)
                        ||task.getTaskRef().startsWith(DeliveryFlowConstant.VISIT_SERVICE_REVIEW_PUBLIC)||task.getTaskRef().startsWith(DeliveryFlowConstant.VISIT_SERVICE_MODIFY_PUBLIC)
                        ||task.getTaskRef().startsWith(DeliveryFlowConstant.VISIT_SERVICE_REMIND_PUBLIC)||task.getTaskRef().startsWith(DeliveryFlowConstant.VISIT_SERVICE_COMPLETE_PUBLIC)
                        ){
            String variable = taskService.getVariable(task.getTaskId(), DeliveryFlowConstant.SERVICE_TYPE)+StringUtils.EMPTY;
            String visitType = taskService.getVariable(task.getTaskId(), DeliveryFlowConstant.VISIT_TYPE)+StringUtils.EMPTY;
            //加载节点扩展名信息
            Map<String,String>map=DeliveryProcessOperate.nodeExtensionNameMap;
            String name="";
            if(DeliveryFlowConstant.SERVICE_TYPE_KE_CHANG_LAI.equals(variable)&&DeliveryFlowConstant.VISIT_TYPE_FMPS_M.equals(visitType)){
                name=map.get(DeliveryFlowConstant.VISIT_TYPE_FMPS_M);
            }else{
                name=map.get(variable); 
            }
            task.setTaskName(task.getTaskName().concat("(").concat(name).concat(")"));
        }
        
        
    }

    
    private Integer computingTaskSchedule(Date staterDate, Integer taskHours) {
        BigDecimal startDateLong = BigDecimal.valueOf(erpHolidaysService.calculateHours(staterDate, new Date()))
                        .multiply(BigDecimal.valueOf(60 * 60 * 1000));
        taskHours = taskHours == 0 ? 1 : taskHours;
        double taskHoursLong = (taskHours * 60 * 60 * 1000);
        return startDateLong.divide(BigDecimal.valueOf(taskHoursLong), 2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)).intValue();
    }

}
