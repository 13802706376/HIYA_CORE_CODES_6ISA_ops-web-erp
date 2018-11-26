package com.yunnex.ops.erp.modules.workflow.delivery.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.workflow.delivery.entity.ErpDeliveryService;

/**
 * erp_delivery_serviceDAO接口
 * @author hanhan
 * @version 2018-05-26
 */
@MyBatisDao
public interface ErpDeliveryServiceDao extends CrudDao<ErpDeliveryService> {
    ErpDeliveryService  getDeliveryInfoByProsIncId (@Param("procInsId") String procInsId);
    
    List<String> findTaskIdByShopId(String zhangbeiId);
    ErpDeliveryService getDeliveryInfoByShopIdAsc(String zhangbeiId);
    ErpDeliveryService getDeliveryInfoByShopId(String zhangbeiId);
   
    ErpDeliveryService getDeliveryInfoByOrederId(@Param("orderId") String orderId);
    
    void updateTime1(ErpDeliveryService erpDelivery);

    void updateTime(ErpDeliveryService erpDelivery);

    /**
     * 计算某种流程类型的订单数量
     * 
     * @param orderNumber
     * @param serviceType
     * @return
     */
    int countByOrderNumberAndServiceType(@Param("orderNumber") String orderNumber, @Param("serviceType") String serviceType);
    
    ErpDeliveryService getDeliveryInfoByShopIdAndServiceTypeDesc(@Param("zhangbeiId")String zhangbeiId,@Param("serviceType") String serviceType,@Param("procInsId") String procInsId);

    List<ErpDeliveryService> findByOrderNumberAndServiceType(@Param("orderNumber") String orderNumber, @Param("types") List<String> types);

    List<ErpDeliveryService> findByOrederId(String orderId);

    void updateExceptionLogo(@Param("exception") String exception, @Param("flowEndTime") Date flowEndTime, @Param("status") Integer status,
                    @Param("ids") List ids);
}
