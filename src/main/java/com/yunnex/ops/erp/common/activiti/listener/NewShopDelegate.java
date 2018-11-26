package com.yunnex.ops.erp.common.activiti.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunnex.ops.erp.common.utils.MD5Util;
import com.yunnex.ops.erp.common.utils.SpringContextHolder;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.order.service.ErpOrderOriginalInfoService;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.shop.service.ErpShopInfoService;
import com.yunnex.ops.erp.modules.workflow.delivery.entity.ErpDeliveryService;
import com.yunnex.ops.erp.modules.workflow.delivery.service.ErpDeliveryServiceService;
import com.yunnex.ops.erp.modules.workflow.flow.common.JykFlowConstants;

/**
 * 新增商户服务
 * @author caozhijun
 *
 */
public class NewShopDelegate implements JavaDelegate
{
    private static final Logger LOGGER = LoggerFactory.getLogger(NewShopDelegate.class);
    private ErpShopInfoService erpShopInfoService = SpringContextHolder.getBean(ErpShopInfoService.class);
    private ErpOrderOriginalInfoService erpOrderOriginalInfoService = SpringContextHolder.getBean(ErpOrderOriginalInfoService.class);
    private ErpDeliveryServiceService erpDeliveryServiceService = SpringContextHolder.getBean(ErpDeliveryServiceService.class);
    
    /** 默认密码长度 */
    private static final int DEFAULT_PASSWORD_LENGTH = 6;

    @Override
    public void execute(DelegateExecution execution) throws Exception
    {
       String  procInsId= execution.getProcessInstanceId();
       ErpDeliveryService erpDeliveryService =erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
       ErpOrderOriginalInfo orderInfo = this.erpOrderOriginalInfoService.get(erpDeliveryService.getOrderId());
       ErpShopInfo erpShopInfo = this.erpShopInfoService.findListByZhangbeiId(erpDeliveryService.getShopId());
       if(null==erpShopInfo){
           Object operationAdviser = execution.getVariable(JykFlowConstants.OPERATION_ADVISER);
           String zhangbeiID = orderInfo.getShopId();
           // 使用掌贝账号的后6位作为密码,并进行MD5加密
            if (zhangbeiID.length() < DEFAULT_PASSWORD_LENGTH) {
               // 不足6位的情况，在末尾补'0'
                zhangbeiID = StringUtils.rightPad(zhangbeiID, DEFAULT_PASSWORD_LENGTH, '0');
           }
            String passWord = MD5Util.md5(zhangbeiID.substring(zhangbeiID.length() - DEFAULT_PASSWORD_LENGTH, zhangbeiID.length()));
           ErpShopInfo addshopinfo = new ErpShopInfo();
           addshopinfo.setName(orderInfo.getShopName());
           // 商户来源为ERP添加
           addshopinfo.setSource("1");
           addshopinfo.setZhangbeiId(zhangbeiID);
           addshopinfo.setPassword(passWord);
           addshopinfo.setOrderId(orderInfo.getId());
           addshopinfo.setAgentId(orderInfo.getAgentId());
           addshopinfo.setOperationAdviserId(operationAdviser+"");
           erpShopInfoService.save(addshopinfo);
            // 在交付服务流程中创建商户时，将商户与触发创建商户条件的流程对应的订单置为进件订单
            erpOrderOriginalInfoService.updateAuditOrder(orderInfo);
            LOGGER.info("在流程中创建商户并将当前订单设为进件订单！zhangbeiId={}, orderNumber={}", zhangbeiID, orderInfo.getOrderNumber());
       }
    }
}
