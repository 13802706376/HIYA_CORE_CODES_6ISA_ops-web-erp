package com.yunnex.ops.erp.modules.order.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.common.utils.DateUtils;
import com.yunnex.ops.erp.common.utils.excel.FastExcel;
import com.yunnex.ops.erp.modules.order.constant.OrderConstants;
import com.yunnex.ops.erp.modules.order.dao.ErpOrderOriginalInfoDao;
import com.yunnex.ops.erp.modules.order.dao.ErpOrderSplitInfoDao;
import com.yunnex.ops.erp.modules.order.dto.OrderExcelResponseDto;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalGood;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitInfo;
import com.yunnex.ops.erp.modules.order.entity.SplitGoodForm;
import com.yunnex.ops.erp.modules.shop.service.ErpShopInfoService;
import com.yunnex.ops.erp.modules.shopdata.dao.ErpShopDataInputDao;
import com.yunnex.ops.erp.modules.shopdata.entity.ErpShopDataInput;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;
import com.yunnex.ops.erp.modules.workflow.delivery.entity.ErpDeliveryService;
import com.yunnex.ops.erp.modules.workflow.delivery.service.ErpDeliveryServiceService;
import com.yunnex.ops.erp.modules.workflow.flow.handler.ProcessStartContext;
import com.yunnex.ops.erp.modules.workflow.flow.service.WorkFlowMonitorService;


/**
 * 订单Service
 * 
 * @author huanghaidong
 * @version 2017-10-24
 */
@Service
public class ErpOrderOriginalInfoService extends CrudService<ErpOrderOriginalInfoDao, ErpOrderOriginalInfo> {

    private static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    private static final String ORDER_TIME_POINT = "2018-01-19 00:00:00";
    @Autowired
    @Lazy(true)
    private ErpOrderOriginalInfoDao erpOrderOriginalInfoDao;
    @Autowired
    private ErpOrderSplitInfoDao erpOrderSplitInfoDao;
    @Autowired
    private WorkFlowMonitorService workFlowMonitorService;
    @Autowired
    private ErpShopDataInputDao erpShopDataInputDao;
    @Lazy(true)
    @Autowired
    private ErpOrderSplitInfoService erpOrderSplitInfoService;

    @Autowired
    private ErpOrderGoodServiceInfoService erpOrderGoodServiceInfoService;
    @Autowired
    private ErpDeliveryServiceService erpDeliveryServiceService;
    /**
     * 订单商品
     */
    @Autowired
    private ErpOrderOriginalGoodService erpOrderOriginalGoodService;

    @Autowired
    private ErpShopInfoService erpShopInfoService;

    @Override
	public ErpOrderOriginalInfo get(String id) {
		return super.get(id);
	}

    @Override
	public List<ErpOrderOriginalInfo> findList(ErpOrderOriginalInfo erpOrderOriginalInfo) {
		return super.findList(erpOrderOriginalInfo);
	}

    @Override
	public Page<ErpOrderOriginalInfo> findPage(Page<ErpOrderOriginalInfo> page, ErpOrderOriginalInfo erpOrderOriginalInfo) {
		return super.findPage(page, erpOrderOriginalInfo);
	}
	
    @Override
	@Transactional(readOnly = false)
    public void save(ErpOrderOriginalInfo erpOrderOriginalInfo) {
		super.save(erpOrderOriginalInfo);
	}
	
    @Override
	@Transactional(readOnly = false)
	public void delete(ErpOrderOriginalInfo erpOrderOriginalInfo) {
		super.delete(erpOrderOriginalInfo);
	}

    public ErpOrderOriginalInfo getDetail(String id) {
        return erpOrderOriginalInfoDao.getDetail(id);
    }
    public ErpOrderOriginalInfo findOrderInfoByUpOrderNumber(String upOrderNumber) {
        return erpOrderOriginalInfoDao.findOrderInfoByUpOrderNumber(upOrderNumber);
    }
    
    public ErpOrderOriginalInfo getCalcInfo(String id, Integer goodType) {
    	return erpOrderOriginalInfoDao.getCalcInfo(id, goodType);
    }

    public int insert(ErpOrderOriginalInfo erpOrderOriginalInfo) {
        return erpOrderOriginalInfoDao.insert(erpOrderOriginalInfo);
    }

    public Integer countByOrderNumber(String orderNumber) {
        Integer count = erpOrderOriginalInfoDao.countByOrderNumber(orderNumber);
        return null == count ? 0 : count.intValue();
    }

    public Integer countByCreateDate(String startAt, String endAt) {
        Integer count = erpOrderOriginalInfoDao.countByCreateDate(startAt, endAt);
        return null == count ? 0 : count.intValue();
    }

    @Transactional(readOnly = false)
    public void updateOrderStatus(String orderNumber, Integer orderStatus) {
        erpOrderOriginalInfoDao.updateOrderStatus(orderNumber, orderStatus);
    }
    
	public ErpOrderOriginalInfo getUnCancelOrderByOrderNo(String orderNumber, Integer cancel) {
	    return erpOrderOriginalInfoDao.getCancelOrderByOrderNo(orderNumber, cancel);
	}
	
	public List<ErpOrderOriginalInfo> getAgentId(String shopid,String del){
		return erpOrderOriginalInfoDao.getAgentId(shopid, del);
	}
	
	
	/**
     * 作废订单。将该订单下的所有流程结束（不删除流程数据，如变量和文件），然后将该订单标识为作废。
     *
     * @param orderId
     * @throws Exception
     * @date 2017年11月27日
     * @author hsr
     */
	@Transactional(readOnly = false)
    public void cancelOrder(String orderId) throws Exception {
        // 聚引客
        List<ErpOrderSplitInfo> orderSplitInfo = erpOrderSplitInfoDao.getByOrderId(orderId);
        if (!CollectionUtils.isEmpty(orderSplitInfo)) {
            for (ErpOrderSplitInfo erpOrderSplitInfo : orderSplitInfo) {
                workFlowMonitorService.endProcess(erpOrderSplitInfo.getProcInsId());
                erpOrderSplitInfoDao.delete(erpOrderSplitInfo);
            }
        }
       
        // 商户资料录入
        List<ErpShopDataInput> shopDataInputs = erpShopDataInputDao.getByOrderId(orderId);
        if (!CollectionUtils.isEmpty(shopDataInputs)) {
            for (ErpShopDataInput shopDataInput : shopDataInputs) {
                workFlowMonitorService.endProcess(shopDataInput.getProcInsId());
                erpShopDataInputDao.delete(shopDataInput);
            }
        }
        // 结束交付流程
        ErpDeliveryService erpDeliveryService= erpDeliveryServiceService.getDeliveryInfoByOrederId(orderId);
        if (erpDeliveryService!=null) {
                workFlowMonitorService.endProcess(erpDeliveryService.getProcInsId());
                erpDeliveryServiceService.delete(erpDeliveryService);
        }
        
	    erpOrderOriginalInfoDao.cancelOrder(orderId,OrderConstants.ORDER_CANCEL);
	}
	
	/**
     * 结束订单。将该订单下的所有流程结束 ，并且删除拆单信息。
     *
     * @param orderId
     * @throws Exception
     * @date 2018年4月20日
     * @author hanhan
     */
    @Transactional(readOnly = false)
    public void endOrder(String orderId) throws Exception {
        // 聚引客
        List<ErpOrderSplitInfo> orderSplitInfo = erpOrderSplitInfoDao.getByOrderId(orderId);
        if (!CollectionUtils.isEmpty(orderSplitInfo)) {
            for (ErpOrderSplitInfo erpOrderSplitInfo : orderSplitInfo) {
                workFlowMonitorService.endProcess(erpOrderSplitInfo.getProcInsId());
                erpOrderSplitInfoDao.deleteSplitInfoById(erpOrderSplitInfo.getId());
            }
        }
        // 商户资料录入
        List<ErpShopDataInput> shopDataInputs = erpShopDataInputDao.getByOrderId(orderId);
        if (!CollectionUtils.isEmpty(shopDataInputs)) {
            for (ErpShopDataInput shopDataInput : shopDataInputs) {
                workFlowMonitorService.endProcess(shopDataInput.getProcInsId());
                erpShopDataInputDao.delete(shopDataInput);
            }
        }
        // 结束交付流程
        ErpDeliveryService erpDeliveryService= erpDeliveryServiceService.getDeliveryInfoByOrederId(orderId);
        if (erpDeliveryService!=null) {
                workFlowMonitorService.endProcess(erpDeliveryService.getProcInsId());
                erpDeliveryServiceService.delete(erpDeliveryService);
        }
        erpOrderOriginalInfoDao.cancelOrder(orderId, OrderConstants.ORDER_END);
    }
	
	
    /**
     * erp创建订单方法
     *
     * @param erpOrderOriginalInfo
     * @param goods
     * @date 2017年11月27日
     * @author SunQ
     */
    @Transactional(readOnly = false)
    public void createOrder(ErpOrderOriginalInfo erpOrderOriginalInfo, List<ErpOrderOriginalGood> goods) {

        /* 保存订单对象 */
        super.save(erpOrderOriginalInfo);

        /* 获取订单的ID */
        String orderId = erpOrderOriginalInfo.getId();

        /* 保存商品信息 */
        for (ErpOrderOriginalGood good : goods) {
            good.setOrderId(orderId);
            erpOrderOriginalGoodService.save(good);
        }
    }


    /**
     * erp创建订单方法,并启动流程
     *
     * @param erpOrderOriginalInfo
     * @param goods
     * @date 2018年4月3日
     * @author zjq
     * @throws ServiceException
     */
    @Transactional(readOnly = false)
    public void createOrderAndStartProcess(ErpOrderOriginalInfo erpOrderOriginalInfo) {
        if (dao.countByOrderNumber(erpOrderOriginalInfo.getOrderNumber()) > 0) {
            throw new ServiceException("订单号已存在");
        }
        if (Constant.YES.equals(erpOrderOriginalInfo.getIsNewShop()) && erpShopInfoService.countShopByZhangbeiId(erpOrderOriginalInfo.getShopId()) > 0) {
            throw new ServiceException("该联系电话已经存在,联系电话将作为小程序登录账号使用,请更换联系电话");
        }
        // 设置默认值
        erpOrderOriginalInfo.setCreateAt(new Date());
        this.setOrderVersion(erpOrderOriginalInfo);// 新增订单版本号默认3.0
        erpOrderOriginalInfo.setCancel(OrderConstants.CANCEL_NO);
        erpOrderOriginalInfo.setOrderSource(OrderConstants.ORDER_SOURCE_ERP);
        erpOrderOriginalInfo.setOrderStatus(OrderConstants.ORDER_STATUS_3);
        // 如果为聚引客订单，不走审核流程
        if (erpOrderOriginalInfo.getGoodType().intValue() == OrderConstants.GOOD_TYPE_ID_KCL) {
            erpOrderOriginalInfo.setAuditStatus(OrderConstants.ORDER_AUDIT_STATUS_0);
        } else if (erpOrderOriginalInfo.getGoodType().intValue() == OrderConstants.GOOD_TYPE_ID_JYK) {
            erpOrderOriginalInfo.setAuditStatus(OrderConstants.ORDER_AUDIT_STATUS_2);
        }
        // 保存订单对象
        save(erpOrderOriginalInfo);
        if (CollectionUtils.isNotEmpty(erpOrderOriginalInfo.getErpOrderOriginalGoods())) {
            erpOrderOriginalGoodService.deleteByOrderId(erpOrderOriginalInfo.getId());
            erpOrderOriginalGoodService.batchInsert(erpOrderOriginalInfo.getErpOrderOriginalGoods(), erpOrderOriginalInfo.getId());
            // 保存订单服务项信息
            erpOrderGoodServiceInfoService.deleteRecordByOrderId(erpOrderOriginalInfo.getId());
            erpOrderGoodServiceInfoService.saveOrderGoodServiceInfo(erpOrderOriginalInfo.getId());
            // 启动流程
            List<SplitGoodForm> splitGoodLists = erpOrderOriginalGoodService.queryGoodFormList(erpOrderOriginalInfo.getId());

            if (splitGoodLists.stream().filter(good -> Optional.ofNullable(good.getGoodTypeId()).orElse(0L) == OrderConstants.GOOD_TYPE_ID_KCL)
                            .count() > 0) {
                ProcessStartContext.startOrderReview(erpOrderOriginalInfo);
            } else if (splitGoodLists.stream().filter(good -> Optional.ofNullable(good.getGoodTypeId()).orElse(0L) == OrderConstants.GOOD_TYPE_ID_JYK)
                            .count() > 0) {
                ProcessStartContext.startByErpOrder(erpOrderOriginalInfo, splitGoodLists);
            }
        } else {
            throw new ServiceException("该订单未选择购买的商品");
        }
    }

    /**
     * 业务定义：保存审核流程状态
     * 
     * @date 2018年7月5日
     * @author R/Q
     */
    @Transactional(readOnly = false)
    public void saveAuditStatus(ErpOrderOriginalInfo erpOrderOriginalInfo) throws ServiceException {
        if (erpOrderOriginalInfo == null || StringUtils.isBlank(erpOrderOriginalInfo.getId())) {
            throw new ServiceException("订单数据出错");
        }
        ErpOrderOriginalInfo baseObj = super.get(erpOrderOriginalInfo);
        baseObj.setAuditStatus(erpOrderOriginalInfo.getAuditStatus());
        this.save(baseObj);
    }

    /**
     * 业务定义：修改订单信息
     * 
     * @date 2018年7月5日
     * @author R/Q
     */
    @Transactional(readOnly = false)
    public void updateOrderData(ErpOrderOriginalInfo erpOrderOriginalInfo) throws ServiceException {
        if (erpOrderOriginalInfo == null || StringUtils.isBlank(erpOrderOriginalInfo.getId())) {
            throw new ServiceException("订单数据出错");
        }
        save(erpOrderOriginalInfo);
        if (CollectionUtils.isNotEmpty(erpOrderOriginalInfo.getErpOrderOriginalGoods())) {
            erpOrderOriginalGoodService.deleteByOrderId(erpOrderOriginalInfo.getId());
            erpOrderOriginalGoodService.batchInsert(erpOrderOriginalInfo.getErpOrderOriginalGoods(), erpOrderOriginalInfo.getId());
            // 保存订单服务项信息
            erpOrderGoodServiceInfoService.deleteRecordByOrderId(erpOrderOriginalInfo.getId());
            erpOrderGoodServiceInfoService.saveOrderGoodServiceInfo(erpOrderOriginalInfo.getId());
        } else {
            throw new ServiceException("该订单未选择购买的商品");
        }
    }

    /**
     * erp删除订单方法
     *
     * @param erpOrderOriginalInfo
     * @date 2017年11月28日
     * @author SunQ
     */
    @Transactional(readOnly = false)
    public void deleteOrder(ErpOrderOriginalInfo erpOrderOriginalInfo) {

        /* 删除之前保存的商品信息 */
        erpOrderOriginalGoodService.deleteByOrderId(erpOrderOriginalInfo.getId());

        /* 删除订单 */
        erpOrderOriginalInfoDao.deleteById(erpOrderOriginalInfo.getId());
    }

    /**
     * erp更新订单方法
     *
     * @param erpOrderOriginalInfo
     * @param goods
     * @date 2017年11月28日
     * @author SunQ
     */
    @Transactional(readOnly = false)
    public void updateOrder(ErpOrderOriginalInfo erpOrderOriginalInfo, List<ErpOrderOriginalGood> goods) {

        /* 获取订单ID */
        String orderId = erpOrderOriginalInfo.getId();

        /* 获取数据库中保存的对象 */
        ErpOrderOriginalInfo dbErpOrderOriginalInfo = erpOrderOriginalInfoDao.get(orderId);

        /* 将修改内容赋值到数据库对象 */
        dbErpOrderOriginalInfo.setUpdateDate(erpOrderOriginalInfo.getUpdateDate());
        dbErpOrderOriginalInfo.setOrderType(erpOrderOriginalInfo.getOrderType());
        dbErpOrderOriginalInfo.setOrderNumber(erpOrderOriginalInfo.getOrderNumber());
        dbErpOrderOriginalInfo.setShopId(erpOrderOriginalInfo.getShopId());
        dbErpOrderOriginalInfo.setShopAbbreviation(erpOrderOriginalInfo.getShopAbbreviation());
        dbErpOrderOriginalInfo.setShopName(erpOrderOriginalInfo.getShopName());
        dbErpOrderOriginalInfo.setShopNumber(erpOrderOriginalInfo.getShopNumber());
        dbErpOrderOriginalInfo.setBuyDate(erpOrderOriginalInfo.getBuyDate());
        dbErpOrderOriginalInfo.setRealPrice(erpOrderOriginalInfo.getRealPrice());
        dbErpOrderOriginalInfo.setIndustryType(erpOrderOriginalInfo.getIndustryType());
        dbErpOrderOriginalInfo.setContactName(erpOrderOriginalInfo.getContactName());
        dbErpOrderOriginalInfo.setContactNumber(erpOrderOriginalInfo.getContactNumber());
        dbErpOrderOriginalInfo.setPromoteContact(erpOrderOriginalInfo.getPromoteContact());
        dbErpOrderOriginalInfo.setPromotePhone(erpOrderOriginalInfo.getPromotePhone());
        dbErpOrderOriginalInfo.setRemark(erpOrderOriginalInfo.getRemark());
        dbErpOrderOriginalInfo.setAgentId(erpOrderOriginalInfo.getAgentId());
        dbErpOrderOriginalInfo.setAgentName(erpOrderOriginalInfo.getAgentName());

        /* 更新数据库对象 */
        super.save(dbErpOrderOriginalInfo);

        /* 删除之前保存的商品信息 */
        erpOrderOriginalGoodService.deleteByOrderId(orderId);

        /* 保存新的商品信息 */
        for (ErpOrderOriginalGood good : goods) {
            good.setOrderId(orderId);
            erpOrderOriginalGoodService.save(good);
        }
    }
    
    public List<ErpOrderOriginalInfo> findWhereShopId(String del,String shopid){
    	return erpOrderOriginalInfoDao.findWhereShopId(del, shopid);
    }
    
    public List<ErpOrderOriginalInfo> findSDIFlowOrderList() {
        return erpOrderOriginalInfoDao.findSDIFlowOrderList();
    }
    
    @Transactional(readOnly = false)
    public void updateShopInfoByShopId(String shopId, String shopName, String shopAbbreviation, String shopNumber) {
        erpOrderOriginalInfoDao.updateShopInfoByShopId(shopId, shopName, shopAbbreviation, shopNumber);
    }

    @Transactional(readOnly = false)
    public void updateOrderVersion(String orderId, String orderVersion) {
        erpOrderOriginalInfoDao.updateOrderVersion(orderId, orderVersion);
    }

    public void setOrderVersion(ErpOrderOriginalInfo erpOrderOriginalInfo) {
        erpOrderOriginalInfo.setOrderVersion(Constant.ORDER_VERSION);
        try {
            if (erpOrderOriginalInfo.getBuyDate().before(DateUtils.parseDate(ORDER_TIME_POINT, YYYY_MM_DD_HH_MM_SS))) {
                erpOrderOriginalInfo.setOrderVersion(Constant.ORDER_VERSION_2);
            }
        } catch (ParseException e1) {
            logger.error(e1.getMessage(), e1);
        }
    }

    public BaseResult export(ErpOrderOriginalInfo entity, HttpServletResponse response) {
        logger.info("订单列表 - Excel导出入参：requestDto = {}", JSON.toJSON(entity));
        entity.setUpdateBy(UserUtils.getUser());// 权限校验用当前登录用户
        List<OrderExcelResponseDto> list = erpOrderOriginalInfoDao.findByPageWithExcel(entity);

        BaseResult result = new BaseResult();
        if (CollectionUtils.isEmpty(list)) {
            result.setCode(BaseResult.CODE_ERROR_ARG);
            result.setMessage("在此条件下查无数据！");
            return result;
        }

        for (OrderExcelResponseDto orderExcelResponseDto : list) {
            // 根据订单id查询该订单下面的商户运营服务待处理数量
            Map<String, Object> resultMap = erpOrderGoodServiceInfoService.querySum(orderExcelResponseDto.getId());
            if (resultMap != null) {
                BigDecimal shopOperationPendingServiceNum = (BigDecimal) resultMap.get("pendingNum");
                shopOperationPendingServiceNum = shopOperationPendingServiceNum == null ? BigDecimal.ZERO : shopOperationPendingServiceNum;
                orderExcelResponseDto.setShopOperationPendingServiceNum(shopOperationPendingServiceNum.intValue());
            }

            Integer pendingNum = erpOrderOriginalGoodService.findJykPendingGoodNumByOrderId(orderExcelResponseDto.getId());
            orderExcelResponseDto.setPendingNum(pendingNum == null ? 0 : pendingNum);
        }

        try {
            FastExcel.exportExcel(response, "订单列表", list);
        } catch (IOException e) {
            String msg = "订单列表导出失败！";
            logger.error(msg, e);
            result.setCode(BaseResult.CODE_ERROR_ARG);
            result.setMessage(msg);
            return result;
        }
        return result;
    }

    /**
     * 根据包含 商品类型id 和 不包含商品类型id 获取订单信息
     *
     * @return
     * @date 2018年5月31日
     * @author linqunzhi
     */
    public ErpOrderOriginalInfo getByGoodTypeIdInAndNotIn(String id, List<Integer> goodTypeIdIn, List<Integer> goodTypeIdNotIn) {
        String goodTypeIdInStr = JSON.toJSONString(goodTypeIdIn);
        String goodTypeIdNotInStr = JSON.toJSONString(goodTypeIdNotIn);
        logger.info("getByGoodTypeIdInAndNotIn start| id={}|goodTypeIdIn={}|goodTypeIdNotIn={}", id, goodTypeIdInStr, goodTypeIdNotInStr);
        ErpOrderOriginalInfo result = erpOrderOriginalInfoDao.getByGoodTypeIdInAndNotIn(id, goodTypeIdIn, goodTypeIdNotIn);
        logger.info("getByGoodTypeIdInAndNotIn end | result={}", result);
        return result;
    }

    /**
     * 业务定义：查询订单审核列表数据-分页
     * 
     * @date 2018年7月4日
     * @author R/Q
     */
    public Page<ErpOrderOriginalInfo> queryAuditList(ErpOrderOriginalInfo paramObj, Page<ErpOrderOriginalInfo> page) {
        page.setList(super.dao.queryAuditList(paramObj, page));
        return page;
    }

    /**
     * 业务定义：获取订单详情+对应商品信息
     * 
     * @date 2018年7月6日
     * @author R/Q
     */
    public ErpOrderOriginalInfo getOrderAndGood(String orderId) {
        ErpOrderOriginalInfo erpOrderOriginalInfo = this.get(orderId);
        if (erpOrderOriginalInfo != null) {
            erpOrderOriginalInfo.setErpOrderOriginalGoods(erpOrderOriginalGoodService.findListByOrderId(orderId));
        }
        return erpOrderOriginalInfo;
    }

    public List<ErpOrderOriginalInfo> findUnCancelOrders(String orderNumber) {
        return dao.findUnCancelOrders(orderNumber);
    }

    public int countOrderUnCancel(String orderNumber) {
        return dao.countOrderUnCancel(orderNumber);
    }

    public int countByYsOrderId(Long ysOrderId) {
        return dao.countByYsOrderId(ysOrderId);
    }

    @Transactional
    public int updateMaterialOrderInfo(ErpOrderOriginalInfo orderOriginalInfo) {
        return dao.updateMaterialOrderInfo(orderOriginalInfo);
    }

    public List<ErpOrderOriginalInfo> findByYsOrderId(Long ysOrderId) {
        return dao.findByYsOrderId(ysOrderId);
    }

    /**
     * 修改商户下的进件订单为当前订单(一个商户只能有一个进件订单)
     * 
     * @param orderOriginalInfo
     */
    @Transactional
    public void updateAuditOrder(ErpOrderOriginalInfo orderOriginalInfo) {
        if (orderOriginalInfo == null) {
            return;
        }
        // 修改商户下的所有进件订单为非进件订单
        dao.updateAuditOrdersToNo(orderOriginalInfo.getShopId());
        // 修改当前订单为进件订单
        orderOriginalInfo.setIsAuditOrder(Constant.YES);
        dao.update(orderOriginalInfo);
    }

    public ErpOrderOriginalInfo findAuditOrder(String zhangbeiId) {
        return dao.findAuditOrder(zhangbeiId);
    }
}
