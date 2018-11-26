package com.yunnex.ops.erp.modules.order.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.result.IllegalArgumentErrorResult;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.order.dao.ErpPromotionMaterialLogDao;
import com.yunnex.ops.erp.modules.order.entity.ErpPromotionMaterialLog;

/**
 * 推广资料操作日志Service
 * @author yunnex
 * @version 2018-05-09
 */
@Service
public class ErpPromotionMaterialLogService extends CrudService<ErpPromotionMaterialLogDao, ErpPromotionMaterialLog> {
    @Autowired
    private ErpPromotionMaterialLogDao erpPromotionMaterialLogDao;

	public ErpPromotionMaterialLog get(String id) {
		return super.get(id);
	}
	
	public List<ErpPromotionMaterialLog> findList(ErpPromotionMaterialLog erpPromotionMaterialLog) {
		return super.findList(erpPromotionMaterialLog);
	}
	
	public Page<ErpPromotionMaterialLog> findPage(Page<ErpPromotionMaterialLog> page, ErpPromotionMaterialLog erpPromotionMaterialLog) {
		return super.findPage(page, erpPromotionMaterialLog);
	}
	
	@Transactional(readOnly = false)
	public void save(ErpPromotionMaterialLog erpPromotionMaterialLog) {
		super.save(erpPromotionMaterialLog);
	}
	
	@Transactional(readOnly = false)
	public void delete(ErpPromotionMaterialLog erpPromotionMaterialLog) {
		super.delete(erpPromotionMaterialLog);
	}

    /**
     * 根据分单ID和推广资料ID查询推广资料日志信息
     *
     * @param splitId
     * @param promotionMaterialsId
     * @return
     * @date 2018年5月9日
     */
    public BaseResult getPromotionMaterialLogs(String splitId, String promotionMaterialsId) {
        logger.info("-----------根据分单ID和推广资料ID查询推广资料日志信息 start-----------------");
        logger.info("入参情况:splitId={},promotionMaterialsId={}", splitId, promotionMaterialsId);

        if (StringUtils.isBlank(splitId) || StringUtils.isBlank(promotionMaterialsId)) {
            return new IllegalArgumentErrorResult();
        }

        List<ErpPromotionMaterialLog> promotionMaterialLogs = erpPromotionMaterialLogDao.getPromotionMaterialLogs(splitId, promotionMaterialsId);
        BaseResult res = new BaseResult();
        res.setAttach(promotionMaterialLogs);

        logger.info("出参情况:BaseResult={}", res);
        logger.info("-----------根据分单ID和推广资料ID查询推广资料日志信息 end-----------------");
        return res;
    }
	
}