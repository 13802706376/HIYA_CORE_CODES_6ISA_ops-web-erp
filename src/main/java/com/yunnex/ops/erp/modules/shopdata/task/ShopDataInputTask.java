package com.yunnex.ops.erp.modules.shopdata.task;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.order.service.ErpOrderOriginalInfoService;
import com.yunnex.ops.erp.modules.shopdata.entity.ErpShopDataInput;
import com.yunnex.ops.erp.modules.shopdata.service.ErpShopDataInputService;
import com.yunnex.ops.erp.modules.sys.entity.ServiceOperation;
import com.yunnex.ops.erp.modules.sys.service.ServiceOperationService;
import com.yunnex.ops.erp.modules.workflow.flow.service.WorkFlowService;

/**
 * 商户资料录入流程启动定时任务(暂时不使用)
 * 
 * @author SunQ
 * @date 2017年12月13日
 */
//@Service(value = "shopDataInputTask")
//@Lazy(false)
public class ShopDataInputTask {
    
    private static final Logger LOGGER = Logger.getLogger(ShopDataInputTask.class);
    
    private static final int SLEEP_MILSEC_1000 = 1000;
    
    @Autowired
    private WorkFlowService workFlowService;
    
    @Autowired
    private ErpOrderOriginalInfoService erpOrderOriginalInfoService;
    
    @Autowired
    private ErpShopDataInputService erpShopDataInputService;
    
    @Autowired
    private ServiceOperationService serviceOperationService;
    
    /**
     * 定时判断新订单是否需要启动商户资料录入流程
     *
     * @date 2017年12月13日
     * @author SunQ
     */
    //@Scheduled(cron = "0 0/45 * * * ?")   //每45min
    public void startProcess() {
        
        LOGGER.info("定时>---获取订单中符合启动商户资料录入流程的订单任务---<启动");
        List<ErpOrderOriginalInfo> list = erpOrderOriginalInfoService.findSDIFlowOrderList();
        for(ErpOrderOriginalInfo erpOrderOriginalInfo : list){
            // 过滤非直销的订单和之前已经启动过流程的订单
            if("1".equals(erpOrderOriginalInfo.getOrderType().toString()) 
                            && erpShopDataInputService.countByShopId(erpOrderOriginalInfo.getShopId())==0) {
                try {
                    ServiceOperation serviceOperate = serviceOperationService.getByServiceNo(erpOrderOriginalInfo.getAgentId()+"");
                    if(serviceOperate!=null){
                        ErpShopDataInput shopDataInput = new ErpShopDataInput();
                        shopDataInput.setOrderId(erpOrderOriginalInfo.getId());
                        shopDataInput.setOrderNumber(erpOrderOriginalInfo.getOrderNumber());
                        shopDataInput.setOrderType(erpOrderOriginalInfo.getOrderType());
                        shopDataInput.setSource(erpOrderOriginalInfo.getSource());
                        shopDataInput.setShopId(erpOrderOriginalInfo.getShopId());
                        shopDataInput.setShopName(erpOrderOriginalInfo.getShopName());
                        shopDataInput.setAddress("");
                        shopDataInput.setPlanningExpert(
                            StringUtils.isNotBlank(serviceOperate.getDefaultManagerId()) ? serviceOperate.getDefaultManagerId() : serviceOperate.getAlternativeManagerId());//负责人
                        erpShopDataInputService.save(shopDataInput);
                        workFlowService.startShopDataInputWorkFlow(shopDataInput.getPlanningExpert(), erpOrderOriginalInfo.getId(), shopDataInput.getId());
                    }
                    Thread.sleep(SLEEP_MILSEC_1000);
                } catch (InterruptedException e) {
                    LOGGER.error(e.getMessage(), e);
                    Thread.currentThread().interrupt();
                }
            }
        }
        LOGGER.info("定时>---获取订单中符合启动商户资料录入流程的订单任务---<结束");
    }
}