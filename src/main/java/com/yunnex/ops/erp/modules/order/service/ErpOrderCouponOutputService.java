package com.yunnex.ops.erp.modules.order.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.result.IllegalArgumentErrorResult;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.order.constant.OrderSplitConstants;
import com.yunnex.ops.erp.modules.order.dao.ErpOrderCouponOutputDao;
import com.yunnex.ops.erp.modules.order.dto.CouponOutputRequestDto;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderCouponOutput;
import com.yunnex.ops.erp.modules.order.entity.ErpPromotionMaterialLog;

/**
 * 卡券输出Service
 * @author yunnex
 * @version 2018-05-08
 */
@Service
public class ErpOrderCouponOutputService extends CrudService<ErpOrderCouponOutputDao, ErpOrderCouponOutput> {
    @Autowired
    private ErpOrderCouponOutputDao erpOrderCouponOutputDao;
    @Autowired
    ErpPromotionMaterialLogService erpPromotionMaterialLogService;

	public ErpOrderCouponOutput get(String id) {
		return super.get(id);
	}
	
	public List<ErpOrderCouponOutput> findList(ErpOrderCouponOutput erpOrderCouponOutput) {
		return super.findList(erpOrderCouponOutput);
	}
	
	public Page<ErpOrderCouponOutput> findPage(Page<ErpOrderCouponOutput> page, ErpOrderCouponOutput erpOrderCouponOutput) {
		return super.findPage(page, erpOrderCouponOutput);
	}
	
	@Transactional(readOnly = false)
	public void save(ErpOrderCouponOutput erpOrderCouponOutput) {
		super.save(erpOrderCouponOutput);
	}
	
	@Transactional(readOnly = false)
	public void delete(ErpOrderCouponOutput erpOrderCouponOutput) {
		super.delete(erpOrderCouponOutput);
	}


    public List<ErpOrderCouponOutput> findListBySplitId(String splitId) {
        return erpOrderCouponOutputDao.findListBySplitId(splitId);
    }

    @Transactional
    public BaseResult updateCouponOutputInfo(CouponOutputRequestDto couponOutputRequestDto) {
        logger.info("-----------卡券输出信息-修改完成 start-----------------");
        logger.info("入参情况:couponOutputRequestDto={}", couponOutputRequestDto);

        if (StringUtils.isBlank(couponOutputRequestDto.getPromotionMaterialId()) || StringUtils.isBlank(couponOutputRequestDto.getSplitId())) {
            return new IllegalArgumentErrorResult();
        }

        // 删除卡券输出信息
        List<String> removedCouponIds = couponOutputRequestDto.getRemovedCouponIds();
        if (CollectionUtils.isNotEmpty(removedCouponIds)) {
            erpOrderCouponOutputDao.batchDelete(removedCouponIds);
        }

        // 保存卡券输出信息
        List<ErpOrderCouponOutput> orderCouponOutputs = couponOutputRequestDto.getOrderCouponOutputs();
        if (CollectionUtils.isNotEmpty(orderCouponOutputs)) {
            for (ErpOrderCouponOutput couponOutput : orderCouponOutputs) {
                this.save(couponOutput);
            }
        }

        // 增加推广资料操作日志
        ErpPromotionMaterialLog log = new ErpPromotionMaterialLog(couponOutputRequestDto.getSplitId(),
                        couponOutputRequestDto.getPromotionMaterialId(), OrderSplitConstants.MODIFYING);
        erpPromotionMaterialLogService.save(log);

        BaseResult res = new BaseResult();
        logger.info("出参情况:BaseResult={}", res);
        logger.info("-----------卡券输出信息-修改完成 end-----------------");
        return new BaseResult();
    }

}