package com.yunnex.ops.erp.modules.order.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.order.dto.OrderExcelResponseDto;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;

/**
 * 订单DAO接口
 * 
 * @author huanghaidong
 * @version 2017-10-24
 */
@MyBatisDao
public interface ErpOrderOriginalInfoDao extends CrudDao<ErpOrderOriginalInfo> {

    ErpOrderOriginalInfo getDetail(@Param("id") String id);
    
    ErpOrderOriginalInfo getCancelOrderByOrderNo(@Param("orderNumber") String orderNumber, @Param("cancel") Integer cancel);
    
    ErpOrderOriginalInfo getCalcInfo(@Param("id") String id, @Param("goodType") Integer goodType);

    Integer countByOrderNumber(@Param("orderNumber") String orderNumber);

    Integer countByCreateDate(@Param("startAt") String startAt, @Param("endAt") String endAt);

    int updateOrderStatus(@Param("orderNumber") String orderNumber, @Param("orderStatus") Integer orderStatus);

    void cancelOrder(@Param("orderId") String orderId, @Param("cancel") Integer cancel);
    
    List<ErpOrderOriginalInfo> getAgentId(@Param("shopid") String shopid, @Param("del") String del);
    
	
    /**
     * 通过ID对订单进行物理删除
     *
     * @param id
     * @date 2017年11月28日
     * @author SunQ
     */
    void deleteById(@Param("id") String id);
    
    List<ErpOrderOriginalInfo> findWhereShopId(@Param("del")String del,@Param("shopid")String shopid);
    
    /**
     * 获取所有应该启动商户资料录入流程的订单
     *
     * @return
     * @date 2017年12月13日
     * @author SunQ
     */
    List<ErpOrderOriginalInfo> findSDIFlowOrderList();
    
    /**
     * 更新订单中的商户信息
     * 
     * @param shopId
     * @return
     */
    int updateShopInfoByShopId(@Param("shopId") String shopId, @Param("shopName") String shopName, 
    		@Param("shopAbbreviation") String shopAbbreviation, @Param("shopNumber") String shopNumber);

    /**
     * 更新订单版本号
     *
     * @param orderId
     * @param orderVersion
     * @return
     * @date 2018年4月9日
     * @author zjq
     */
    int updateOrderVersion(@Param("orderId") String orderId, @Param("orderVersion") String orderVersion);

    /**
     *
     * @param requestDto
     * @return
     * @date 2018年5月31日
     */
    List<OrderExcelResponseDto> findByPageWithExcel(ErpOrderOriginalInfo entity);

    /**
     * 根据包含 商品类型id 和 不包含商品类型id 获取订单信息
     *
     * @param id
     * @param goodTypeIdIn
     * @param goodTypeNotIn
     * @return
     * @date 2018年5月31日
     * @author linqunzhi
     */
    ErpOrderOriginalInfo getByGoodTypeIdInAndNotIn(@Param("id") String id, @Param("goodTypeIdIn") List<Integer> goodTypeIdIn,
                    @Param("goodTypeIdNotIn") List<Integer> goodTypeIdNotIn);

    /**
     * 根据订单id查询该订单下面的待处理服务数量
     *
     * @param id
     * @return
     * @date 2018年6月22日
     */
    Integer getPendingServiceNumById(String id);

    /**
     * 业务定义：查询订单审核列表数据-分页
     * 
     * @date 2018年7月4日
     * @author R/Q
     */
    List<ErpOrderOriginalInfo> queryAuditList(@Param("paramObj") ErpOrderOriginalInfo paramObj, @Param("page") Page<ErpOrderOriginalInfo> page);

    /**
     * 查找订单
     * 
     * @return
     */
    List<ErpOrderOriginalInfo> findUnCancelOrders(String orderNumber);

    /**
     * 查找订单数量（删除的和作废的算作不存在）
     * 
     * @param orderNumber
     * @return
     */
    int countOrderUnCancel(String orderNumber);

    /**
     * 根据物料订单ID查找是否存在（删除的和作废的算作不存在）
     * 
     * @param materialOrderId
     * @return
     */
    int countByYsOrderId(Long materialOrderId);

    /**
     * 更新物料订单信息
     * 
     * @param orderOriginalInfo
     * @return
     */
    int updateMaterialOrderInfo(ErpOrderOriginalInfo orderOriginalInfo);

    List<ErpOrderOriginalInfo> findByYsOrderId(Long ysOrderId);

    /**
     * 修改商户下的进件订单为非进件订单
     * 
     * @param shopId
     */
    void updateAuditOrdersToNo(String shopId);

    /**
     * 查找商户的进件订单，作废的不算
     * 
     * @return
     */
    ErpOrderOriginalInfo findAuditOrder(String zhangbeiId);
    /**
     * 根据升级后的订单号去查
     */
    ErpOrderOriginalInfo findOrderInfoByUpOrderNumber(String upOrderNumber);
    
}
