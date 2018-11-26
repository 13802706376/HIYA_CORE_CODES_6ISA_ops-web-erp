package com.yunnex.ops.erp.modules.order.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.order.dto.FlowServiceItemLinkDto;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderGoodServiceInfo;

/**
 * 订单商品服务DAO接口
 * 
 * @author yunnex
 * @version 2018-06-02
 */
@MyBatisDao
public interface ErpOrderGoodServiceInfoDao extends CrudDao<ErpOrderGoodServiceInfo> {
    void insertBatch(@Param("list") List<ErpOrderGoodServiceInfo> list);

    List<ErpOrderGoodServiceInfo> getOrderGoodServiceByOrderId(@Param("orderId") String orderId);
    Map<String, Object> querySum(@Param("orderId") String orderId);
    ErpOrderGoodServiceInfo getOrderGoodServiceByOrderIdSingle(@Param("orderId") String orderId, @Param("itemId") String itemId);

    ErpOrderGoodServiceInfo getOrderGoodServiceExists(@Param("orderId") String orderId, @Param("itemId") String itemId);

    /**
     * 业务定义：根据订单ID新增服务记录
     * 
     * @date 2018年7月3日
     * @author R/Q
     */
    void addByOrderId(String orderId);

    /**
     * 业务定义：根据订单ID删除服务记录
     * 
     * @date 2018年7月3日
     * @author R/Q
     */
    void deleteRecordByOrderId(String orderId);

    /**
     * 插入流程和服务的关联表
     *
     * @param dto
     * @return
     * @date 2018年7月18日
     */
    int insertErpFlowServiceItemLink(FlowServiceItemLinkDto dto);

    /**
     * 业务定义：订单详情-商户运营服务流程列表
     * 
     * @date 2018年9月3日
     * @author R/Q
     */
    List<Map<String, Object>> getDeliveryServiceByOrderId(@Param("orderId") String orderId);

    /**
     * 业务定义：根据流程实例ID获取数据
     * 
     * @date 2018年9月6日
     * @author R/Q
     */
    List<ErpOrderGoodServiceInfo> getOrderGoodServiceByProcInsId(@Param("procInsId") String procInsId);

    /**
     * 业务定义：复制服务项关联数据
     * 
     * @date 2018年9月6日
     * @author R/Q
     */
    void copyServiceItemLink(@Param("oldProcInsId") String oldProcInsId, @Param("newProcInsId") String newProcInsId);

}