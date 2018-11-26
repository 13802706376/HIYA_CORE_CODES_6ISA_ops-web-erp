package com.yunnex.ops.erp.modules.workflow.acceptance.dao;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.workflow.acceptance.entity.ErpServiceAcceptance;

/**
 * 服务验收评价DAO接口
 * @author yunnex
 * @version 2018-07-04
 */
@MyBatisDao
public interface ErpServiceAcceptanceDao extends CrudDao<ErpServiceAcceptance> {

    /**
     * 根据上门信息id 获取 验收及评分 信息
     *
     * @param visitInfoId
     * @return
     * @date 2018年7月23日
     * @author linqunzhi
     */
    ErpServiceAcceptance getByVisitId(@Param("visitInfoId") String visitInfoId);
    
    public ErpServiceAcceptance getAcceptanceInfoByVisitId(String visitInfoId);

    /**
     * 根据流程id 删除服务验收及评价
     *
     * @param procInsId
     * @date 2018年9月13日
     * @author linqunzhi
     */
    void deleteByProcInsId(@Param("procInsId") String procInsId);
}