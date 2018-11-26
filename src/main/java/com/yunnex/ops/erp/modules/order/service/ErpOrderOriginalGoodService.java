package com.yunnex.ops.erp.modules.order.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.order.dao.ErpOrderOriginalGoodDao;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalGood;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.order.entity.SplitGoodForm;

/**
 * 订单商品Service
 * 
 * @author huanghaidong
 * @version 2017-10-24
 */
@Service
public class ErpOrderOriginalGoodService extends CrudService<ErpOrderOriginalGoodDao, ErpOrderOriginalGood> {

    @Autowired
    private ErpOrderOriginalGoodDao erpOrderOriginalGoodDao;

    @Override
    public ErpOrderOriginalGood get(String id) {
        return super.get(id);
    }

    @Override
    public List<ErpOrderOriginalGood> findList(ErpOrderOriginalGood erpOrderOriginalGood) {
        return super.findList(erpOrderOriginalGood);
    }

    @Override
    public Page<ErpOrderOriginalGood> findPage(Page<ErpOrderOriginalGood> page, ErpOrderOriginalGood erpOrderOriginalGood) {
        return super.findPage(page, erpOrderOriginalGood);
    }

    @Override
    @Transactional(readOnly = false)
    public void save(ErpOrderOriginalGood erpOrderOriginalGood) {
        super.save(erpOrderOriginalGood);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(ErpOrderOriginalGood erpOrderOriginalGood) {
        super.delete(erpOrderOriginalGood);
    }

    public List<ErpOrderOriginalGood> findListByOrderId(String orderId) {
        return erpOrderOriginalGoodDao.findListByOrderId(orderId);
    }

    public List<ErpOrderOriginalGood> findListByOrderInfo(String orderId, Integer goodType) {
        return erpOrderOriginalGoodDao.findListByOrderInfo(orderId, goodType);
    }
    public List<ErpOrderOriginalGood> getListOriginalGood(String orderId) {
        return erpOrderOriginalGoodDao.getListOriginalGood(orderId);
    }
    
    
    @Transactional(readOnly = false)
    public boolean decreasePendingNum(String id, Integer num) {
        return erpOrderOriginalGoodDao.decreasePendingNum(id, num) > 0;
    }

    @Transactional(readOnly = false)
    public boolean decreaseProcessNum(String id, Integer num) {
        return erpOrderOriginalGoodDao.decreaseProcessNum(id, num) > 0;
    }

    @Transactional(readOnly = false)
    public void deleteByOrderId(String id) {
        erpOrderOriginalGoodDao.deleteByOrderId(id);
    }

    public List<ErpOrderOriginalGood> findJykAndKclDistinct(String orderId) {
        return erpOrderOriginalGoodDao.findJykAndKclDistinct(orderId);
    }

    /**
     * 获取订单的服务类型+版本号。 格式：客常来+聚引客 - 订单版本号
     * 
     * @param order
     * @return
     */
    public String getJykAndKclServiceType(ErpOrderOriginalInfo order) {
        String result = StringUtils.EMPTY;
        if (order == null) {
            return result;
        }

        List<ErpOrderOriginalGood> goods = dao.findJykAndKclDistinct(order.getId());
        if (CollectionUtils.isEmpty(goods)) {
            return result;
        }

        StringBuilder sb = new StringBuilder();
        for (ErpOrderOriginalGood good : goods) {
            sb.append(good.getGoodTypeName()).append(Constant.PLUS);
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(Constant.SPACE).append(Constant.DASH).append(Constant.SPACE).append(order.getOrderVersion());
        result = sb.toString();

        return result;
    }

    public Integer findJykPendingGoodNumByOrderId(String id) {
        return erpOrderOriginalGoodDao.findJykPendingGoodNumByOrderId(id);
    }

    /**
     * 业务定义：批量插入订单商品信息
     * 
     * @date 2018年7月3日
     * @author R/Q
     */
    public void batchInsert(List<ErpOrderOriginalGood> erpOrderOriginalGoods, String orderId) {
        super.dao.batchInsert(erpOrderOriginalGoods, orderId);
    }

    /**
     * 业务定义：根据订单ID查询商品信息
     * 
     * @date 2018年7月5日
     * @author R/Q
     */
    public List<SplitGoodForm> queryGoodFormList(String orderId) {
        return super.dao.queryGoodFormList(orderId);
    }

    /**
     * 业务定义：重置商品处理数量
     * 
     * @date 2018年8月31日
     * @author R/Q
     */
    public void resetPendingNum(List<SplitGoodForm> splitGoodLists) {
        super.dao.resetPendingNum(splitGoodLists);
    }

}
