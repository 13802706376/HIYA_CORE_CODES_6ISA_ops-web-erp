package com.yunnex.ops.erp.modules.workflow.store.dao;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.workflow.flow.dto.AdvAuditStatusDto;
import com.yunnex.ops.erp.modules.workflow.store.entity.JykOrderChoiceStore;

import java.util.List;

/**
 * 聚引客订单推广门店管理DAO接口
 * 
 * @author SunQ
 * @date 2018年1月8日
 */
@MyBatisDao
public interface JykOrderChoiceStoreDao extends CrudDao<JykOrderChoiceStore> {

    /**
     * 获取推广门店对象
     *
     * @param splitId
     * @return
     * @date 2018年1月8日
     * @author SunQ
     */
    JykOrderChoiceStore getBySplitId(String splitId);

    /**
     * 获取门店ID
     *
     * @param splitId
     * @return
     * @date 2018年1月8日
     * @author SunQ
     */
    String getStoreIdBySplitId(String splitId);

    /**
     * 获取门店ID
     *
     * @param procInsId
     * @return
     * @date 2018年1月10日
     * @author SunQ
     */
    String getStoreIdByProcInsId(String procInsId);

    /**
     * 通过流程ID更新已选的门店的状态
     *
     * @param procInsId
     * @date 2018年1月13日
     * @author SunQ
     */
    void deleteByByProcInsId(String procInsId);

    /**
     * 后期可能支持1个分单 选择多个推广门店
     *
     * @param splitId
     * @return
     * @date 2018年5月15日
     * @author hanhan
     */

    List<String> getAllStoreIdBySplitId(String splitId);

    List<AdvAuditStatusDto> getAdvAuditStatus(String splitId);
}
