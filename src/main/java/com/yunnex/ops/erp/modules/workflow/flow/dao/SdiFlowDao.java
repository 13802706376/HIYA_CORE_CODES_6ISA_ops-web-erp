package com.yunnex.ops.erp.modules.workflow.flow.dao;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.workflow.flow.entity.SdiFlow;

/**
 * 商户资料录入流程信息表DAO接口
 * 
 * @author SunQ
 * @date 2017年12月9日
 */
@MyBatisDao
public interface SdiFlowDao extends CrudDao<SdiFlow> {

    SdiFlow getByProcInstId(String procInsId);

    void updateFlowByProcIncId(SdiFlow sdiFlow);
    
    SdiFlow getSdiinfoByShopId(String shopId);
}