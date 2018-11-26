package com.yunnex.ops.erp.modules.order.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.utils.HttpUtil;
import com.yunnex.ops.erp.modules.good.category.entity.ErpGoodCategory;
import com.yunnex.ops.erp.modules.good.category.service.ErpGoodCategoryService;
import com.yunnex.ops.erp.modules.good.entity.ErpGoodInfo;
import com.yunnex.ops.erp.modules.good.service.ErpGoodInfoApiService;
import com.yunnex.ops.erp.modules.good.service.ErpGoodInfoService;
import com.yunnex.ops.erp.modules.order.constant.OrderConstants;
import com.yunnex.ops.erp.modules.order.dao.ErpOrderSplitInfoDao;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalGood;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitInfo;
import com.yunnex.ops.erp.modules.order.entity.SplitGoodForm;
import com.yunnex.ops.erp.modules.shop.constant.ShopConstant;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.shop.service.ErpShopInfoApiService;
import com.yunnex.ops.erp.modules.shop.service.ErpShopInfoService;
import com.yunnex.ops.erp.modules.workflow.flow.service.WorkFlow3p25Service;

@Service
public class ErpOrderOriginalInfoApiService {

    public static final String JYK = "5";

	@Autowired
    private ErpOrderOriginalInfoService erpOrderOriginalInfoService;

    @Autowired
    private ErpOrderOriginalGoodService erpOrderOriginalGoodService;

    @Autowired
    private ErpShopInfoApiService erpShopInfoApiService;

    @Autowired
    private ErpGoodCategoryService erpGoodCategoryService;

    @Autowired
    private ErpGoodInfoService erpGoodInfoService;

    @Autowired
    private ErpGoodInfoApiService erpGoodInfoApiService;
    
    @Autowired
    private ErpOrderSplitInfoDao erpOrderSplitInfoDao;

    @Autowired
    private WorkFlow3p25Service workFlow3p25Service;

    @Autowired
    private ErpOrderGoodServiceInfoService erpOrderGoodServiceInfoService;

    @Autowired
    private ErpShopInfoService shopInfoService;

    ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();//NOSONAR

    @Autowired
    protected DataSourceTransactionManager txManager;

    @Value("${api_order_info_url}")
    private String API_ORDER_INFO_URL;  //NOSONAR
    
    protected Logger logger = LoggerFactory.getLogger(getClass());//NOSONAR

    public boolean saveSingleOrder(String jsonStr) {
        // 异步需要手动控制事务
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // 事物隔离级别，开启新事务，这样会比较安全些。
        TransactionStatus status = txManager.getTransaction(def); // 获得事务状态
        try {
            JSONObject orderJsonObject = JSONObject.parseObject(jsonStr);
            // ERP入库只针对 3已支付，4进件中，5已进件 此3种状态订单
            Integer orderStatus = Optional.ofNullable(orderJsonObject.getInteger("order_status")).orElse(0);
            if (orderStatus == OrderConstants.PAY_STATUS_3 || orderStatus == OrderConstants.PAY_STATUS_4 || orderStatus == OrderConstants.PAY_STATUS_5) {
                // 只查询未作废的订单来更新，如果不存在就增加
                ErpOrderOriginalInfo orderInfo = erpOrderOriginalInfoService.getUnCancelOrderByOrderNo(orderJsonObject.getString("order_number"), 0);
                insertOrUpdateOrder(orderJsonObject, orderInfo == null ? null : orderInfo.getId());
            }
            txManager.commit(status);
            return true;
        } catch (Exception e) { // NOSONAR
            logger.error(e.getMessage(), e);
            txManager.rollback(status);
        }
        return false;
    }

    private void insertOrUpdateOrder(JSONObject orderJsonObject, String orderId) {
        ErpOrderOriginalInfo erpOrderOriginalInfo = new ErpOrderOriginalInfo();
        erpOrderOriginalInfo.setId(orderId); // 如果存在ID，则更新
        erpOrderOriginalInfo.setOrderNumber(orderJsonObject.getString("order_number"));
        erpOrderOriginalInfo.setBuyDate(orderJsonObject.getDate("buy_date"));
        erpOrderOriginalInfo.setContactName(orderJsonObject.getString("contact_name"));
        erpOrderOriginalInfo.setSource(orderJsonObject.getString("source"));
        erpOrderOriginalInfo.setContactNumber(orderJsonObject.getString("contact_phone"));
        erpOrderOriginalInfo.setRealPrice(orderJsonObject.getLong("real_price"));
        erpOrderOriginalInfo.setShopId(orderJsonObject.getString("shop_extension_id").trim());
        erpOrderOriginalInfo.setShopExtensionId(orderJsonObject.getString("shop_extension_id").trim());
        erpOrderOriginalInfo.setShopName(orderJsonObject.getString("shop_name"));
        erpOrderOriginalInfo.setRemark(orderJsonObject.getString("remark"));
        erpOrderOriginalInfo.setOrderSource(0);//订单来源(0:OEM推送 1:erp添加 2: 易商)

        // 更新已经拆单的 shopId
        List<ErpOrderSplitInfo> orderSplitInfos = erpOrderSplitInfoDao.getByOrderId(orderId);
        if(!CollectionUtils.isEmpty(orderSplitInfos))
        {
              for(ErpOrderSplitInfo  erpOrderSplitInfo:orderSplitInfos)
              {
                   erpOrderSplitInfo.setShopId(orderJsonObject.getString("shop_extension_id").trim());
                   erpOrderSplitInfo.setShopName(orderJsonObject.getString("shop_name"));
                   erpOrderSplitInfoDao.update(erpOrderSplitInfo);
              }
        }
        
        // 如果不是新订单或者为服务商订单, 则不更新商户名称信息 订单类别 1直销 2渠道(服务商)',
        if(StringUtils.isBlank(orderId) || orderJsonObject.getInteger("order_type").intValue() == 2)
        {
            erpOrderOriginalInfo.setShopAbbreviation(orderJsonObject.getString("shop_abbreviation"));
            erpOrderOriginalInfo.setShopNumber(orderJsonObject.getString("shop_number"));
        }
        else
        {
            ErpOrderOriginalInfo dberpOrderOriginalInfo = erpOrderOriginalInfoService.get(orderId);
            erpOrderOriginalInfo.setShopAbbreviation(dberpOrderOriginalInfo.getShopAbbreviation());
            erpOrderOriginalInfo.setShopNumber(dberpOrderOriginalInfo.getShopNumber());
        }
        erpOrderOriginalInfo.setCreateAt(orderJsonObject.getDate("create_date"));
        erpOrderOriginalInfo.setPayDate(orderJsonObject.getDate("pay_date"));
        erpOrderOriginalInfo.setOrderStatus(orderJsonObject.getInteger("order_status"));
        erpOrderOriginalInfo.setOrderType(orderJsonObject.getInteger("order_type"));
        erpOrderOriginalInfo.setPromotePhone(orderJsonObject.getString("promote_phone"));
        erpOrderOriginalInfo.setPromoteContact(orderJsonObject.getString("promote_contact"));
        erpOrderOriginalInfo.setSalePerson(orderJsonObject.getString("sale_person"));
        erpOrderOriginalInfo.setAgentName(orderJsonObject.getString("agent_name"));
        erpOrderOriginalInfo.setAgentId(orderJsonObject.getInteger("agent_id"));
        erpOrderOriginalInfo.setCancel(0);
        erpOrderOriginalInfo.setAuditStatus(OrderConstants.ORDER_AUDIT_STATUS_2);// 同步的订单不需要走审核流程( /** 订单审核状态：通过 */)
        // 设置默认版本号
        erpOrderOriginalInfoService.setOrderVersion(erpOrderOriginalInfo);
        boolean isNewShop= erpShopInfoApiService.isNew(orderJsonObject.getString("shop_extension_id").trim());
        erpOrderOriginalInfo.setIsNewShop(isNewShop?Constant.YES:Constant.NO);
        erpOrderOriginalInfoService.save(erpOrderOriginalInfo);
        // 修改商户的当前状态
        changeShopCurrentStatus(erpOrderOriginalInfo);

        // 已经同步过的订单下面的商品数据，不会再改变；如果要改变，需要先作废订单；
        if (orderId != null) {
            return;
        }
        List<SplitGoodForm> splitGoodLists = new ArrayList<SplitGoodForm>();

        JSONArray goods = orderJsonObject.getJSONArray("goods");
        if (goods != null && goods.size() > 0) {
            for (int i = 0; i < goods.size(); i++) {
                JSONObject good = goods.getJSONObject(i);
                ErpOrderOriginalGood erpOrderOriginalGood = new ErpOrderOriginalGood();
                erpOrderOriginalGood.setOrderId(erpOrderOriginalInfo.getId());
                erpOrderOriginalGood.setGoodName(good.getString("name"));
                erpOrderOriginalGood.setGoodId(good.getLong("id"));
                erpOrderOriginalGood.setRealPrice(good.getLong("real_price"));
                erpOrderOriginalGood.setNum(good.getIntValue("num"));
                erpOrderOriginalGood.setPendingNum(good.getIntValue("num"));
                erpOrderOriginalGood.setProcessNum(0);
                erpOrderOriginalGood.setPackId(StringUtils.trimToNull(good.getString("pack_id")));
                erpOrderOriginalGood.setBizType(StringUtils.isEmpty(good.getString("biz_type"))?" ":good.getString("biz_type") );
                // 原始订单待处理数量
                if (null == erpOrderOriginalInfo.getPendingNum())
                    erpOrderOriginalInfo.setPendingNum(0);

                erpOrderOriginalGood.setFinishNum(0);
                ErpGoodInfo erpGoodInfo = erpGoodInfoService.getDetail(good.getString("id"));
                if (null == erpGoodInfo) {
                    erpGoodInfoApiService.sync();
                }
                ErpGoodCategory erpGoodCategory = erpGoodCategoryService.getByGoodId(good.getLong("id"));
                if (null != erpGoodCategory) {
                    erpOrderOriginalGood.setGoodTypeId(Long.valueOf(erpGoodCategory.getId()));
                    erpOrderOriginalGood.setGoodTypeName(erpGoodCategory.getName());

					// 更新原始订单待处理数量（只有聚引客的订单才需要更新待处理数量）
                    if (erpOrderOriginalGood.getGoodTypeId().equals(JYK)) {
                        erpOrderOriginalInfo.setPendingNum(erpOrderOriginalInfo.getPendingNum() + erpOrderOriginalGood.getNum());
                    }
                }
                erpOrderOriginalGoodService.save(erpOrderOriginalGood);

                SplitGoodForm form = new SplitGoodForm();
                form.setGoodId(erpOrderOriginalGood.getId());
                form.setNum(erpOrderOriginalGood.getNum());
                form.setPackId(erpOrderOriginalGood.getPackId());
                form.setGoodTypeId(erpOrderOriginalGood.getGoodTypeId());
                splitGoodLists.add(form);
            }
        }
        erpOrderOriginalInfoService.save(erpOrderOriginalInfo);
        // 保存订单服务项信息
        erpOrderGoodServiceInfoService.saveOrderGoodServiceInfo(erpOrderOriginalInfo.getId());
        // 启动运营服务流程
        workFlow3p25Service.startOperatingServiceProcess(splitGoodLists, erpOrderOriginalInfo);
    }

    /**
     * 订单A从贝虎系统成功同步到贝蚁系统后，若该订单A对应的商户的”当前状态为“停用”，则将该商户对应的进件订单设为订单A，同时，将商户的”当前状态“改为”可用”
     * 
     * @param orderOriginalInfo
     */
    private void changeShopCurrentStatus(ErpOrderOriginalInfo orderOriginalInfo) {
        if (orderOriginalInfo == null) {
            return;
        }
        if (orderOriginalInfo.getOrderStatus() == null || orderOriginalInfo.getOrderStatus() == OrderConstants.ORDER_STATUS_CANCEL) {
            return;
        }

        ErpShopInfo shopDB = shopInfoService.findByZhangbeiId(orderOriginalInfo.getShopId());
        if (shopDB == null) {
            return;
        }
        if (ShopConstant.whether.NO.equals(shopDB.getCurrentStatus())) {
            shopDB.setCurrentStatus(ShopConstant.whether.YES);
            shopDB.setSort(ShopConstant.Sort.ENABLE);
            shopInfoService.save(shopDB);
            erpOrderOriginalInfoService.updateAuditOrder(orderOriginalInfo);
            logger.info("订单同步，商户进件订单变更。zhangbeiId={}, orderNumber={}", shopDB.getZhangbeiId(), orderOriginalInfo.getOrderNumber());
        }
    }

    @Transactional(readOnly = false)
    public boolean syncBatchOrder(String startAt, String endAt) {
        singleThreadExecutor.submit(new SyncOrderThread(startAt, endAt));
        return true;
    }

    public class SyncOrderThread implements Runnable {

        private String startAt;
        private String endAt;

        public SyncOrderThread(String startAt, String endAt) {
            this.startAt = startAt;
            this.endAt = endAt;
        }

        @Override
        public void run() {
            try {
                String reqUrl = String.format(API_ORDER_INFO_URL, URLEncoder.encode(startAt, "utf-8"), URLEncoder.encode(endAt, "utf-8"));
                while (true) {
                    JSONObject syncObject = getOrderDetailFromDiTui(reqUrl);
                    if(null!=syncObject)
                    {
                           JSONObject syncData = syncObject.getJSONObject("data");
                           JSONArray results = syncData.getJSONArray("results");
                           if (null == results || results.size() == 0) {
                               break;
                           }
                           for (int i = 0; i < results.size(); i++) {
                               saveSingleOrder(results.getJSONObject(i).toJSONString());
                           }
                           reqUrl = syncData.getString("next");
                           if (StringUtils.isEmpty(reqUrl) || !syncData.getBooleanValue("has_next")) {
                               break;
                           }
                    }
                    else
                    {
                        break;
                    }
                }
            } catch (RuntimeException | UnsupportedEncodingException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    private JSONObject getOrderDetailFromDiTui(String reqUrl) {
        String resStr = HttpUtil.sendHttpGetReqToServer(reqUrl);
        
        logger.info("getOrderDetailFromDiTui >resStr= {}",resStr);
        if (StringUtils.isNotEmpty(resStr)) {
            return JSONObject.parseObject(resStr);
        }
        return null;
    }

}
