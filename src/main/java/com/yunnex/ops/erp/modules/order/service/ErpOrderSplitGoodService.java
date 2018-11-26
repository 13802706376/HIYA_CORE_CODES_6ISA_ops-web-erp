package com.yunnex.ops.erp.modules.order.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.order.dao.ErpOrderSplitGoodDao;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitGood;

/**
 * 分单商品Service
 * 
 * @author huanghaidong
 * @version 2017-10-24
 */
@Service
public class ErpOrderSplitGoodService extends CrudService<ErpOrderSplitGoodDao, ErpOrderSplitGood> {


    @Autowired
    private ErpOrderSplitGoodDao erpOrderSplitGoodDao;

    /**
     * 根据分单id获取商品信息
     * 
     * @param originalSplitId
     * @return 单商品和数据 商品名称*数量;商品名称;数据
     * @date 2018年3月30日
     * @author zjq
     */
    public String getServiceAndNum(String originalSplitId) {

        if (!StringUtils.isEmpty(originalSplitId)) {
            List<ErpOrderSplitGood> goods = erpOrderSplitGoodDao.getErpOrderSplitGoodBySplitId(originalSplitId);
            StringBuilder builder = new StringBuilder();
            for (ErpOrderSplitGood erpOrderSplitGood : goods) {
                builder.append(Constant.SEMICOLON).append(erpOrderSplitGood.getGoodName()).append(Constant.ASTERISK)
                                .append(erpOrderSplitGood.getNum());
            }
            if (builder.length() > 0) {
                builder.deleteCharAt(0);
            }
            return builder.toString();
        }
        return StringUtils.EMPTY;
    }



    /**
     * 聚引客--尊店家-中*1
     *
     * @param originalSplitId
     * @return
     * @date 2018年4月2日
     * @author zjq
     */
    public String getServiceAndNumAndType(String originalSplitId) {

        if (!StringUtils.isEmpty(originalSplitId)) {
            List<ErpOrderSplitGood> goods = erpOrderSplitGoodDao.getErpOrderSplitGoodBySplitId(originalSplitId);
            StringBuilder builder = new StringBuilder();
            for (ErpOrderSplitGood erpOrderSplitGood : goods) {
                if (StringUtils.isEmpty(erpOrderSplitGood.getGoodTypeName())) {
                    builder.append(erpOrderSplitGood.getGoodName()).append(Constant.ASTERISK).append(erpOrderSplitGood.getNum())
                                    .append(Constant.COMMA);
                    continue;
                }
                builder.append(StringUtils.isEmpty(erpOrderSplitGood.getGoodTypeName()) ? StringUtils.EMPTY : erpOrderSplitGood.getGoodTypeName())
                                .append(Constant.DASH).append(Constant.DASH).append(erpOrderSplitGood.getGoodName()).append(Constant.ASTERISK)
                                .append(erpOrderSplitGood.getNum()).append(Constant.COMMA);
            }
            if (builder.length() > 0) {
                builder.deleteCharAt(builder.length() - 1);
            }
            return builder.toString();
        }
        return StringUtils.EMPTY;
    }

    @Override
    @Transactional(readOnly = false)
    public void save(ErpOrderSplitGood erpOrderSplitGood) {
        if (StringUtils.isEmpty(erpOrderSplitGood.getIsPromote())) {
            erpOrderSplitGood.setIsPromote(Constant.NO);
        }
        super.save(erpOrderSplitGood);
    }

    /**
     * 获取选择推广的聚引客套餐
     * 
     * @param splitId
     * @return
     */
    public List<ErpOrderSplitGood> getPromoteGoods(String splitId) {
        if (splitId == null) {
            return new ArrayList<>();
        }
        List<ErpOrderSplitGood> goods = erpOrderSplitGoodDao.getErpOrderSplitGoodBySplitId(splitId);
        if (CollectionUtils.isEmpty(goods)) {
            return new ArrayList<>();
        }

        List<ErpOrderSplitGood> result = Lists.newArrayList();
        for (ErpOrderSplitGood good : goods) {
            if (Constant.YES.equals(good.getIsPromote())) {
                result.add(good);
            }
        }

        return result;
    }

    /**
     * 业务定义：依据分单ID删除对应商品信息
     * 
     * @date 2018年8月31日
     * @author R/Q
     */
    public void deleteBySplitId(String splitId) {
        dao.deleteBySplitId(splitId);
    }
}
