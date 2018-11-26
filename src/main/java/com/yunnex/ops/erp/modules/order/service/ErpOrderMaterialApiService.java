package com.yunnex.ops.erp.modules.order.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.yunnex.ops.erp.api.dto.request.MaterialContentRequestDto;
import com.yunnex.ops.erp.api.dto.request.OrderMaterialRequestDto;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.result.IllegalArgumentErrorResult;
import com.yunnex.ops.erp.common.result.ResourceUnexistErrorResult;
import com.yunnex.ops.erp.common.result.ServiceErrorResult;
import com.yunnex.ops.erp.common.result.SystemErrorResult;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.common.utils.NumberUtils;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.modules.agent.entity.ErpAgentInfo;
import com.yunnex.ops.erp.modules.agent.entity.SysServiceOperationManager;
import com.yunnex.ops.erp.modules.agent.service.ErpAgentInfoService;
import com.yunnex.ops.erp.modules.agent.service.SysServiceOperationManagerService;
import com.yunnex.ops.erp.modules.good.entity.ErpGoodInfo;
import com.yunnex.ops.erp.modules.good.service.ErpGoodInfoService;
import com.yunnex.ops.erp.modules.material.constant.MaterialCreationConstant;
import com.yunnex.ops.erp.modules.material.dao.ErpOrderMaterialCreationDao;
import com.yunnex.ops.erp.modules.material.entity.ErpOrderMaterialContent;
import com.yunnex.ops.erp.modules.material.entity.ErpOrderMaterialCreation;
import com.yunnex.ops.erp.modules.material.service.ErpOrderMaterialContentService;
import com.yunnex.ops.erp.modules.order.constant.OrderConstants;
import com.yunnex.ops.erp.modules.order.constant.OrderMaterialSyncStatusEnum;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderMaterialSyncLog;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalGood;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSendInfo;
import com.yunnex.ops.erp.modules.order.exception.OrderMaterialException;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.shop.service.ErpShopInfoService;
import com.yunnex.ops.erp.modules.sys.constant.DictConstant;
import com.yunnex.ops.erp.modules.sys.utils.DictUtils;
import com.yunnex.ops.erp.modules.workflow.delivery.constant.ErpDeliveryServiceConstants;
import com.yunnex.ops.erp.modules.workflow.delivery.entity.ErpDeliveryService;
import com.yunnex.ops.erp.modules.workflow.delivery.service.ErpDeliveryServiceService;
import com.yunnex.ops.erp.modules.workflow.flow.common.JykFlowConstants;
import com.yunnex.ops.erp.modules.workflow.flow.handler.ProcessStartContext;
import com.yunnex.ops.erp.modules.workflow.user.entity.ErpOrderFlowUser;
import com.yunnex.ops.erp.modules.workflow.user.service.ErpOrderFlowUserService;

import yunnex.common.core.dto.ApiResult;
import yunnex.common.mybatis.PageResult;
import yunnex.operation.biz.facade.MaterialOrderFacade;
import yunnex.operation.biz.request.query.MaterialOrderReq;
import yunnex.operation.biz.response.MaterialCfgResp;
import yunnex.operation.biz.response.MaterialOrderResp;

/**
 * 订单物料同步接口
 */
@Service
public class ErpOrderMaterialApiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErpOrderMaterialApiService.class);

    // 文件存放路径
    @Value("${userfiles.basedir}")
    private String fileBaseDir;

    // 订单号规则
    private static final String ORDER_NO_FORMAT = "yyMMddHHmmss";
    private static final Integer THOUSAND = 1000;
    private static final Integer PAGE_SIZE = 10;
    private static final String SYNC_MSG = "同步订单物料内容";
    private static final String SYNC_ONE_MSG = "同步一条订单物料信息";
    private static final String SYNC_MATERIAL_STATUS = "同步订单物料制作状态到易商平台！";
    private static final Long SLEEP_TWO_SECOND = 2000L;

    // 单线程线程池
    ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor(); // NOSONAR

    @Autowired
    protected DataSourceTransactionManager txManager;
    @Autowired
    private ErpOrderOriginalInfoService orderOriginalInfoService;
    @Autowired
    private ErpShopInfoService shopInfoService;
    @Autowired
    private ErpOrderMaterialSyncLogService orderMaterialSyncLogService;
    @Autowired
    private ErpDeliveryServiceService deliveryServiceService;
    @Autowired
    private ErpOrderMaterialContentService orderMaterialContentService;
    @Autowired
    private ErpOrderSendInfoService orderSendInfoService;
    @Autowired
    private ErpGoodInfoService goodInfoService;
    @Autowired
    private ErpOrderOriginalGoodService orderOriginalGoodService;
    @Autowired
    private ErpOrderMaterialCreationDao orderMaterialCreationDao;
    @Autowired
    private ErpOrderGoodServiceInfoService erpOrderGoodServiceInfoService;
    @Autowired
    private ErpAgentInfoService agentInfoService;
    @Autowired
    private SysServiceOperationManagerService sysServiceOperationManagerService;
    @Autowired
    private MaterialOrderFacade materialOrderFacade;
    @Autowired
    private ErpOrderFlowUserService orderFlowUserService;

    // 物料服务相关流程（首购订单）
    private static final List<String> SERVICE_TYPES_FMPS_LIST;

    static {
        SERVICE_TYPES_FMPS_LIST = Lists.newArrayList();
        SERVICE_TYPES_FMPS_LIST.add(ErpDeliveryServiceConstants.SERVICE_TYPE_FMPS);
        SERVICE_TYPES_FMPS_LIST.add(ErpDeliveryServiceConstants.SERVICE_TYPE_FMPS_BASIC);
    }

    /**
     * 同步订单物料内容(异步)
     * 
     * @return
     */
    public Boolean syncOrderMaterial() {
        LOGGER.info("{}(异步)", SYNC_MSG);

        singleThreadExecutor.submit(() -> syncOrderMaterials());

        return true;
    }

    /**
     * 按条件同步订单物料
     * 
     */
    public void syncOrderMaterials() {
        long start = System.currentTimeMillis();
        LOGGER.info("{}开始！", SYNC_MSG);
        MaterialOrderReq materialOrderReq = new MaterialOrderReq();
        materialOrderReq.setPageSize(PAGE_SIZE);
        int pageNum = Constant.ONE;
        while (true) {
            materialOrderReq.setPageNum(pageNum++);
            ApiResult<PageResult<MaterialOrderResp>> result = materialOrderFacade.pullUnSynchronization(materialOrderReq);
            if (result == null || !result.isSuccess()) {
                LOGGER.info("{}易商接口调用失败！pageNum={}", SYNC_MSG, pageNum - 1);
                break;
            }
            PageResult<MaterialOrderResp> pageResult;
            List<MaterialOrderResp> list;
            if ((pageResult = result.getEntry()) == null || CollectionUtils.isEmpty(list = pageResult.getList())) {
                break;
            }
            if (CollectionUtils.isNotEmpty(list)) {
                list.forEach(materialOrderResp -> saveOne(convertOrderMaterial(materialOrderResp)));
            }
            if (pageResult.getPage() != null && !pageResult.getPage().isHasNextPage()) {
                break;
            }
        }
        LOGGER.info("{}结束！耗时：{}s", SYNC_MSG, (System.currentTimeMillis() - start) / THOUSAND);
    }

    /**
     * 同步一条订单物料信息
     *
     * @param ysOrderId
     * @return
     */
    public BaseResult syncOneOrderMaterial(Long ysOrderId) {
        LOGGER.info("{}入参：ysOrderId={}", SYNC_ONE_MSG, ysOrderId);
        if (ysOrderId == null) {
            return new IllegalArgumentErrorResult("易商订单ID不能为空！");
        }
        MaterialOrderReq req = new MaterialOrderReq();
        req.setOrderId(ysOrderId);

        ApiResult<PageResult<MaterialOrderResp>> result = materialOrderFacade.pullUnSynchronization(req);
        if (result == null || !result.isSuccess()) {
            return new ServiceErrorResult("易商接口调用失败！");
        }

        PageResult<MaterialOrderResp> pageResult;
        List<MaterialOrderResp> list;
        if ((pageResult = result.getEntry()) == null || CollectionUtils.isEmpty(list = pageResult.getList())) {
            return new ResourceUnexistErrorResult("易商订单不存在!");
        }

        return saveOne(convertOrderMaterial(list.get(Constant.ZERO)));
    }

    private OrderMaterialRequestDto convertOrderMaterial(MaterialOrderResp materialOrderResp) {
        if (materialOrderResp == null) {
            return null;
        }

        OrderMaterialRequestDto requestDto = new OrderMaterialRequestDto();
        requestDto.setOrderNumber(materialOrderResp.getOrderNumber());
        requestDto.setZhangbeiId(materialOrderResp.getZhangbeiId());
        requestDto.setYsOrderId(materialOrderResp.getYsOrderId());
        requestDto.setYsOrderRealPrice(materialOrderResp.getYsOrderRealPrice());
        requestDto.setYsOrderBuyTime(materialOrderResp.getYsOrderBuyTime());
        requestDto.setOrderReceiveAddress(materialOrderResp.getOrderReceiveAddress());
        requestDto.setOrderLinkPhone(materialOrderResp.getOrderLinkPhone());
        requestDto.setOrderLinkMan(materialOrderResp.getOrderLinkMan());
        requestDto.setMaterialPackageUrl(materialOrderResp.getMaterialPackageUrl());

        List<MaterialCfgResp> materialCfgRespList = materialOrderResp.getMaterialRespList();
        if (CollectionUtils.isNotEmpty(materialCfgRespList)) {
            List<MaterialContentRequestDto> materialContents = new ArrayList<>();
            materialCfgRespList.forEach(materialCfgResp -> {
                MaterialContentRequestDto materialContent = convertMaterialContent(materialCfgResp);
                if (materialContent != null) {
                    materialContents.add(materialContent);
                }
            });
            requestDto.setMaterialContents(materialContents);
        }

        return requestDto;
    }

    private static MaterialContentRequestDto convertMaterialContent(MaterialCfgResp materialCfgResp) {
        if (materialCfgResp == null) {
            return null;
        }
        MaterialContentRequestDto requestDto = new MaterialContentRequestDto();
        requestDto.setMaterialQuality(materialCfgResp.getMaterialQuality());
        requestDto.setScenarioType(materialCfgResp.getScenarioType());
        requestDto.setMaterialTypeName(materialCfgResp.getMaterialTypeName());
        requestDto.setFrontName(materialCfgResp.getFrontName());
        requestDto.setReverseName(materialCfgResp.getReverseName());
        requestDto.setFrontImage(materialCfgResp.getFrontImage());
        requestDto.setReverseImage(materialCfgResp.getReverseImage());
        requestDto.setResourceUrl(materialCfgResp.getResourceUrl());
        requestDto.setSize(materialCfgResp.getSize());
        requestDto.setMaterialAmount(materialCfgResp.getMaterialAmount());
        return requestDto;
    }

    /**
     * 添加一条订单物料信息，手动回滚事务
     *
     * @param requestDto
     */
    public BaseResult saveOne(OrderMaterialRequestDto requestDto) {
        // 异步需要手动控制事务
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED); // 事物隔离级别，开启新事务，这样会比较安全些。
        TransactionStatus status = txManager.getTransaction(def); // 获得事务状态
        try {
            syncOne(requestDto);
            txManager.commit(status);
        } catch (OrderMaterialException e) {
            // 记录异常
            String exMsg = e.getMessage();
            saveSyncLog(requestDto, exMsg);
            txManager.commit(status);
            String statusName = OrderMaterialSyncStatusEnum.getByName(exMsg);
            LOGGER.info("{}:异常!{}!", SYNC_ONE_MSG, statusName); // NOSONAR
            return new ServiceErrorResult(statusName);
        } catch (Exception e) { // NOSONAR
            txManager.rollback(status);
            LOGGER.error("保存失败！事务回滚！", e);
            return new SystemErrorResult();
        }
        return new BaseResult();
    }

    /**
     * 保存一条订单物料信息.
     * <p>
     * 一个物料订单对应一个贝虎订单或一个ERP生成的订单。
     * 
     * @param requestDto
     * @return
     */
    public void syncOne(OrderMaterialRequestDto requestDto) {
        LOGGER.info("{}入参:{}", SYNC_ONE_MSG, JSON.toJSON(requestDto));
        checkNotNull(requestDto);

        Long ysOrderId = requestDto.getYsOrderId();
        if (ysOrderId == null) {
            LOGGER.error("{}易商订单ID为空！无法保存！", SYNC_ONE_MSG);
            return;
        }

        ErpOrderOriginalInfo erpOrderOriginalInfo = (ErpOrderOriginalInfo) Optional.of(ysOrderId).map(id -> {
            List<ErpOrderOriginalInfo> orders = orderOriginalInfoService.findByYsOrderId(id);
            return CollectionUtils.isNotEmpty(orders) ? orders.get(Constant.ZERO) : null;
        }).orElse(null);

        // 物料订单是否已经入库
        if (null == erpOrderOriginalInfo) {
            // 如果贝虎订单号不为空，则订单类别为首次，否则为更新
            String orderCategory = StringUtils
                            .isNotBlank(requestDto.getOrderNumber()) ? OrderConstants.ORDER_CATEGORY_FIRST : OrderConstants.ORDER_CATEGORY_UPDATE;
            requestDto.setOrderCategory(orderCategory);

            // 订单类别：首次（有订单号）
            if (OrderConstants.ORDER_CATEGORY_FIRST.equals(orderCategory)) {
                procFirstCategory(requestDto);
            }
            // 订单类别：更新（没有订单号）
            else if (OrderConstants.ORDER_CATEGORY_UPDATE.equals(orderCategory)) {
                procUpdateCategory(requestDto);
            }
        } else {
            // 如果订单升级过，使用新的订单号
            if (StringUtils.isNotBlank(erpOrderOriginalInfo.getUpOrderNumber())) {
                requestDto.setOrderNumber(erpOrderOriginalInfo.getUpOrderNumber());
            } else {
                requestDto.setOrderNumber(erpOrderOriginalInfo.getOrderNumber());
            }
            requestDto.setOrderCategory(erpOrderOriginalInfo.getOrderCategory());
        }

        // 保存订单物料清单
        saveMaterialContent(requestDto);

        // 保存订单物料制作内容
        saveMaterialCreation(requestDto);

        // 保存订单寄送信息
        saveOrderSendInfo(requestDto);

        // 同步物料制作状态到易商，如果失败，记录异常，那边会定时同步
        syncMaterialStatus(ysOrderId);

        // 恢复正常
        saveSyncLog(requestDto, OrderMaterialSyncStatusEnum.NORMAL.getName());

        LOGGER.info("{}成功！", SYNC_ONE_MSG);
    }

    /**
     * 订单类别为“首次”的处理逻辑
     *
     * @param requestDto
     * @return
     */
    private void procFirstCategory(OrderMaterialRequestDto requestDto) {
        checkNotNull(requestDto);

        ErpShopInfo shopInfoDB = shopInfoService.findByZhangbeiId(requestDto.getZhangbeiId());
        /* 没有商户异常记录 */
        if (shopInfoDB == null) {
            throw new OrderMaterialException(OrderMaterialSyncStatusEnum.SHOP_NOT_EXIST.getName());
        }

        // 检查订单是否存在
        String orderNumber = requestDto.getOrderNumber();
        List<ErpOrderOriginalInfo> unCancelOrders = orderOriginalInfoService.findUnCancelOrders(orderNumber);
        if (CollectionUtils.isEmpty(unCancelOrders)) {
            throw new OrderMaterialException(OrderMaterialSyncStatusEnum.ORDER_NOT_EXIST.getName());
        }
        ErpOrderOriginalInfo orderOriginalInfo = unCancelOrders.get(Constant.ZERO);
        String upOrderNumber = orderOriginalInfo.getUpOrderNumber();
        String orderNo = orderNumber;
        // 如果订单（流程）升级过，使用新订单代替旧订单
        if (StringUtils.isNotBlank(upOrderNumber)) {
            orderNo = upOrderNumber;
            requestDto.setOrderNumber(upOrderNumber);
            orderOriginalInfo.setOrderNumber(upOrderNumber);
        } else {
            orderOriginalInfo.setOrderNumber(orderNumber);
        }

        /* 没有物料制作服务相关流程，2个中任意1个启动即通过 */
        List<ErpDeliveryService> deliveryServices = deliveryServiceService.findByOrderNumberAndServiceType(orderNo, SERVICE_TYPES_FMPS_LIST);
        if (CollectionUtils.isEmpty(deliveryServices)) {
            throw new OrderMaterialException(OrderMaterialSyncStatusEnum.ORDER_MATERIAL_CREATION_FLOW_NOT_EXIST.getName());
        }

        // 更新易商订单信息
        orderOriginalInfo.setOrderCategory(requestDto.getOrderCategory());
        orderOriginalInfo.setYsOrderId(requestDto.getYsOrderId());
        orderOriginalInfo.setYsOrderBuyTime(requestDto.getYsOrderBuyTime());
        orderOriginalInfo.setYsOrderRealPrice(requestDto.getYsOrderRealPrice());
        orderOriginalInfoService.updateMaterialOrderInfo(orderOriginalInfo);
    }

    private ErpShopInfo getShopInfo(String zhangbeiId) {
        ErpShopInfo shopInfo = null;
        if (StringUtils.isNotBlank(zhangbeiId)) {
            shopInfo = shopInfoService.findByZhangbeiId(zhangbeiId);
        }
        if (shopInfo == null) {
            // 商户不存在异常
            throw new OrderMaterialException(OrderMaterialSyncStatusEnum.SHOP_NOT_EXIST.getName());
        }
        return shopInfo;
    }

    /**
     * 订单类别为“更新”的处理逻辑
     *
     * @param requestDto
     */
    private void procUpdateCategory(OrderMaterialRequestDto requestDto) {
        checkNotNull(requestDto);

        String zhangbeiId = requestDto.getZhangbeiId();
        ErpShopInfo shopInfo = getShopInfo(zhangbeiId);

        Integer agentId = shopInfo.getAgentId();
        if (agentId == null || agentId == Constant.NEGATIVE_ONE) {
            // 商户对应的服务商不存在异常
            throw new OrderMaterialException(OrderMaterialSyncStatusEnum.SHOP_AGENT_NOT_EXIST.getName());
        }

        // 服务商信息，先查服务商表，没有再查分公司表
        ErpAgentInfo agentInfo = agentInfoService.findByAgentId(agentId);
        SysServiceOperationManager operationManager;
        String agentName;
        Integer orderType;
        if (agentInfo == null) {
            operationManager = sysServiceOperationManagerService.findByServiceNo(agentId.toString());
            if (operationManager == null) {
                // 商户对应的服务商不存在异常
                throw new OrderMaterialException(OrderMaterialSyncStatusEnum.SHOP_AGENT_NOT_EXIST.getName());
            } else {
                orderType = OrderConstants.ORDER_TYPE_DIRECT;
                agentName = operationManager.getCompanyName();
            }
        } else {
            orderType = OrderConstants.ORDER_TYPE_SERVICE;
            agentName = agentInfo.getName();
        }

        String goodId = DictUtils.getDictValue(DictConstant.GOOD_MATERIAL_UPDATE_SERVICE_ID, DictConstant.TYPE_GOOD_ID, null);
        if (StringUtils.isBlank(goodId)) {
            throw new ServiceException(SYNC_ONE_MSG + "出错, 数据字典没有配置商品“物料更新服务”的ID！");
        }

        ErpGoodInfo goodInfo = goodInfoService.findGoodAndCategoryById(goodId);
        if (goodInfo == null) {
            throw new ServiceException(SYNC_ONE_MSG + "出错，商品“物料更新服务”不存在！");
        }

        // 新增订单
        ErpOrderOriginalInfo orderOriginalInfo = new ErpOrderOriginalInfo();
        orderOriginalInfo.setShopName(shopInfo.getName());
        orderOriginalInfo.setShopAbbreviation(shopInfo.getAbbreviation());
        orderOriginalInfo.setShopNumber(shopInfo.getNumber());
        orderOriginalInfo.setAgentId(agentId);
        orderOriginalInfo.setAgentName(agentName);
        orderOriginalInfo.setOrderType(orderType);
        String orderNo = genOrderNo();
        // 传递生成的订单号
        requestDto.setOrderNumber(orderNo);
        orderOriginalInfo.setOrderNumber(orderNo);
        orderOriginalInfo.setShopId(zhangbeiId);
        orderOriginalInfo.setShopExtensionId(zhangbeiId);
        orderOriginalInfo.setOrderCategory(requestDto.getOrderCategory());
        orderOriginalInfo.setYsOrderId(requestDto.getYsOrderId());
        orderOriginalInfo.setYsOrderBuyTime(requestDto.getYsOrderBuyTime());
        orderOriginalInfo.setYsOrderRealPrice(requestDto.getYsOrderRealPrice());
        orderOriginalInfo.setCancel(OrderConstants.CANCEL_NO); // 正常状态
        orderOriginalInfo.setOrderSource(OrderConstants.ORDER_SOURCE_YS);// 同步易商时生成
        orderOriginalInfo.setAuditStatus(OrderConstants.ORDER_AUDIT_STATUS_2);
        orderOriginalInfo.setOrderStatus(OrderConstants.ORDER_STATUS_3); // 已支付
        orderOriginalInfo.setBuyDate(requestDto.getYsOrderBuyTime());
        orderOriginalInfo.setPayDate(requestDto.getYsOrderBuyTime());
        orderOriginalInfo.setPrePrice(requestDto.getYsOrderRealPrice());
        orderOriginalInfo.setRealPrice(requestDto.getYsOrderRealPrice());
        orderOriginalInfo.setCreateAt(new Date());
        orderOriginalInfoService.setOrderVersion(orderOriginalInfo);
        orderOriginalInfoService.save(orderOriginalInfo);

        // 关联商品-物料更新服务
        ErpOrderOriginalGood orderOriginalGood = new ErpOrderOriginalGood();
        orderOriginalGood.setGoodId(Long.valueOf(goodId));
        orderOriginalGood.setGoodName(goodInfo.getName());
        orderOriginalGood.setGoodTypeId(goodInfo.getCategoryId());
        orderOriginalGood.setGoodTypeName(goodInfo.getCategoryName());
        orderOriginalGood.setPrePrice(requestDto.getYsOrderRealPrice());
        orderOriginalGood.setRealPrice(requestDto.getYsOrderRealPrice());
        orderOriginalGood.setOrderId(orderOriginalInfo.getId());
        // 默认数量为1
        orderOriginalGood.setNum(Constant.ONE);
        orderOriginalGood.setPendingNum(Constant.ZERO);
        orderOriginalGood.setProcessNum(Constant.ONE);
        orderOriginalGood.setFinishNum(Constant.ZERO);
        orderOriginalGoodService.save(orderOriginalGood);

        // 保存订单服务项信息
        erpOrderGoodServiceInfoService.deleteRecordByOrderId(orderOriginalInfo.getId());
        erpOrderGoodServiceInfoService.saveOrderGoodServiceInfo(orderOriginalInfo.getId());

        // 启动新的物料更新服务流程
        ProcessStartContext.startByMerchantsOrder(orderOriginalInfo);
    }

    /**
     * 睡眠2秒后生成订单号，防止重复
     * 
     * @return
     */
    private String genOrderNo() {
        try {
            Thread.sleep(SLEEP_TWO_SECOND);
        } catch (InterruptedException e) { // NOSONAR
            LOGGER.info("生成订单号前休眠2秒被打断！", e);
        }
        return getDateStr(ORDER_NO_FORMAT);
    }

    /**
     * 保存订单寄送信息，所有信息以易商那边为准
     * 
     * @param requestDto
     */
    private void saveOrderSendInfo(OrderMaterialRequestDto requestDto) {
        checkNotNull(requestDto);

        Long ysOrderId = requestDto.getYsOrderId();
        ErpOrderSendInfo orderSendInfoDB = orderSendInfoService.findByYsOrderId(ysOrderId);

        if (orderSendInfoDB == null) {
            orderSendInfoDB = new ErpOrderSendInfo();
        }
        orderSendInfoDB.setOrderNumber(requestDto.getOrderNumber());
        orderSendInfoDB.setYsOrderId(requestDto.getYsOrderId());
        orderSendInfoDB.setLinkMan(requestDto.getOrderLinkMan());
        orderSendInfoDB.setLinkPhone(requestDto.getOrderLinkPhone());
        orderSendInfoDB.setReceiveAddress(requestDto.getOrderReceiveAddress());
        orderSendInfoService.save(orderSendInfoDB);
    }

    /**
     * 如果订单没有物料制作内容，则新增，否则更新
     *
     * @param requestDto
     */
    private void saveMaterialCreation(OrderMaterialRequestDto requestDto) {
        checkNotNull(requestDto);

        ErpOrderMaterialCreation materialCreation = null;
        String orderNumber = requestDto.getOrderNumber();
        Long ysOrderId = requestDto.getYsOrderId();
        // 先按易商订单ID查找，没有再按贝虎订单号查找，以兼容新旧数据
        materialCreation = orderMaterialCreationDao.findByYsOrderId(ysOrderId);
        if (materialCreation == null && StringUtils.isNotBlank(orderNumber)) {
            materialCreation = orderMaterialCreationDao.findByOrderNumber(orderNumber);
        }

        // 商户信息
        String shopName = null;
        String zhangbeiId = requestDto.getZhangbeiId();
        if (StringUtils.isNotBlank(zhangbeiId)) {
            ErpShopInfo shopInfoDB = getShopInfo(zhangbeiId);
            shopName = shopInfoDB.getName();
        }

        if (materialCreation == null) {
            materialCreation = new ErpOrderMaterialCreation();
        }

        if (OrderConstants.ORDER_CATEGORY_FIRST.equals(requestDto.getOrderCategory())) {
            // 查找订单对应的流程和运营顾问
            String procInstId = null;
            String operationAdviserId = null;
            String operationAdviserName = null;
            List<ErpDeliveryService> deliveryServices = deliveryServiceService.findByOrderNumberAndServiceType(orderNumber, SERVICE_TYPES_FMPS_LIST);
            if (CollectionUtils.isNotEmpty(deliveryServices)) {
                ErpDeliveryService erpDeliveryService = deliveryServices.get(Constant.ZERO);
                procInstId = erpDeliveryService.getProcInsId();
            }
            ErpOrderFlowUser flowUser = orderFlowUserService.findByProcInsIdAndRoleName(procInstId, JykFlowConstants.OPERATION_ADVISER);
            if (flowUser != null && flowUser.getUser() != null) {
                operationAdviserId = flowUser.getUser().getId();
                operationAdviserName = flowUser.getUser().getName();
            }
            // 流程实例
            materialCreation.setProcInsId(procInstId);
            // 运营顾问
            materialCreation.setOperationAdviserId(operationAdviserId);
            materialCreation.setOperationAdviserName(operationAdviserName);
        }

        materialCreation.setOrderNumber(orderNumber);
        materialCreation.setYsOrderId(ysOrderId);
        materialCreation.setMaterialPath(requestDto.getMaterialPackageUrl());
        // 商户信息
        materialCreation.setZhangbeiId(zhangbeiId);
        materialCreation.setShopName(shopName);

        if (StringUtils.isBlank(materialCreation.getId())) {
            // 物料同步过来后，即为“待下单制作”状态
            materialCreation.setStatus(MaterialCreationConstant.WAITING_ORDER);
            materialCreation.setStatusName(MaterialCreationConstant.WAITING_ORDER_CN);
            materialCreation.setIsNewRecord(false);
            materialCreation.preInsert();
            orderMaterialCreationDao.insert(materialCreation);
        } else {
            // 当状态为“待设计稿”时，更新为“待下单制作”
            String status = materialCreation.getStatus();
            if (StringUtils.isBlank(status) || MaterialCreationConstant.WAITING_LAYOUT.equals(status)) {
                materialCreation.setStatus(MaterialCreationConstant.WAITING_ORDER);
                materialCreation.setStatusName(MaterialCreationConstant.WAITING_ORDER_CN);
            }
            materialCreation.preUpdate();
            orderMaterialCreationDao.update(materialCreation);
        }
    }

    /**
     * 保存订单物料内容（清单）
     */
    private void saveMaterialContent(OrderMaterialRequestDto requestDto) {
        checkNotNull(requestDto);

        // 物理删除指定订单下的所有物料内容
        orderMaterialContentService.deleteByYsOrderId(requestDto.getYsOrderId());

        List<MaterialContentRequestDto> materialContents = requestDto.getMaterialContents();
        List<ErpOrderMaterialContent> materialContentList = materialContents.stream().map(materialContentDto -> {
            ErpOrderMaterialContent materialContent = new ErpOrderMaterialContent();
            String resourceUrl = materialContentDto.getResourceUrl();
            String frontImage = materialContentDto.getFrontImage();
            if (StringUtils.isNotBlank(frontImage)) {
                materialContent.setFrontImage(resourceUrl + frontImage);
            } else {
                materialContent.setFrontImage(Constant.BLANK);
            }
            String reverseImage = materialContentDto.getReverseImage();
            if (StringUtils.isNotBlank(reverseImage)) {
                materialContent.setReverseImage(resourceUrl + reverseImage);
            } else {
                materialContent.setReverseImage(Constant.BLANK);
            }
            materialContent.setFrontName(StringUtils.nullToBlank(materialContentDto.getFrontName()));
            materialContent.setReverseName(StringUtils.nullToBlank(materialContentDto.getReverseName()));
            materialContent.setSize(StringUtils.nullToBlank(materialContentDto.getSize()));
            materialContent.setMaterialAmount(NumberUtils.nullToZero(materialContentDto.getMaterialAmount()));
            materialContent.setScenarioType(NumberUtils.nullToZero(materialContentDto.getScenarioType()));
            materialContent.setMaterialTypeName(StringUtils.nullToBlank(materialContentDto.getMaterialTypeName()));
            materialContent.setMaterialQuality(StringUtils.nullToBlank(materialContentDto.getMaterialQuality()));
            // 贝虎订单号
            materialContent.setOrderNumber(StringUtils.nullToBlank(requestDto.getOrderNumber()));
            // 易商订单ID
            materialContent.setYsOrderId(NumberUtils.nullToZero(requestDto.getYsOrderId()));
            materialContent.setIsNewRecord(false);
            materialContent.preInsert();
            return materialContent;
        }).collect(Collectors.toList());

        // 批量保存
        if (CollectionUtils.isNotEmpty(materialContentList)) {
            orderMaterialContentService.saveBatch(materialContentList);
        }
    }

    private static String getDateStr(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }

    /**
     * 保存同步日志
     *
     * @param requestDto
     */
    private void saveSyncLog(OrderMaterialRequestDto requestDto, String syncStatus) {
        if (requestDto == null) {
            return;
        }

        ErpOrderMaterialSyncLog syncLogDB = orderMaterialSyncLogService.findByYsOrderId(requestDto.getYsOrderId());
        if (syncLogDB == null) {
            syncLogDB = new ErpOrderMaterialSyncLog();
        }

        syncLogDB.setOrderNumber(requestDto.getOrderNumber());
        syncLogDB.setYsOrderId(requestDto.getYsOrderId());
        syncLogDB.setZhangbeiId(requestDto.getZhangbeiId());
        syncLogDB.setOrderCategory(requestDto.getOrderCategory());
        syncLogDB.setSyncDate(new Date());
        syncLogDB.setSyncStatus(syncStatus);
        syncLogDB.setSyncStatusName(OrderMaterialSyncStatusEnum.getByName(syncStatus));
        // 恢复正常时间
        if (OrderMaterialSyncStatusEnum.NORMAL.getName().equals(syncStatus)) {
            syncLogDB.setRecoverDate(new Date());
        }

        orderMaterialSyncLogService.save(syncLogDB);
        LOGGER.info("{}:保存同步日志!{}", SYNC_ONE_MSG, syncLogDB);
    }

    private static void checkNotNull(OrderMaterialRequestDto requestDto) {
        if (requestDto == null) {
            String msg = "订单物料数据为空，无法保存！";
            LOGGER.error("{}!{}", SYNC_ONE_MSG, msg);
            throw new ServiceException(msg);
        }
    }

    /**
     * 根据易商订单ID同步物料制作状态给易商
     * 
     * @param ysOrderId
     */
    public BaseResult syncMaterialStatus(Long ysOrderId) {
        LOGGER.info("{}ysOrderId={}", SYNC_MATERIAL_STATUS, ysOrderId);
        if (ysOrderId == null) {
            return new IllegalArgumentErrorResult("易商订单ID不能为空！");
        }

        ErpOrderMaterialCreation orderMaterialCreation = orderMaterialCreationDao.findByYsOrderId(ysOrderId);
        if (orderMaterialCreation == null) {
            String msg = "该订单没有对应的物料制作状态！";
            LOGGER.info("{}{}ysOrderId={}", SYNC_MATERIAL_STATUS, msg, ysOrderId);
            return new ResourceUnexistErrorResult(msg);
        }

        try {
            String status = orderMaterialCreation.getStatus();
            LOGGER.info("{}ysOrderId={},status={}", SYNC_MATERIAL_STATUS, ysOrderId, status);
            ApiResult<Integer> result = materialOrderFacade.updateOrderStatus(ysOrderId, status);
            if (result == null || !result.isSuccess()) {
                throw new OrderMaterialException();
            }
        } catch (Exception e) { // NOSONAR
            throw new OrderMaterialException(OrderMaterialSyncStatusEnum.SYNC_MATERIAL_STATUS_FAILED.getName());
        }

        LOGGER.info("{}成功！", SYNC_MATERIAL_STATUS);
        return new BaseResult();
    }

}
