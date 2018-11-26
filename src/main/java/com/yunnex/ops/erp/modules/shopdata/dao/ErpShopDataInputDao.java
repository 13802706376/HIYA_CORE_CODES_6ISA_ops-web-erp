package com.yunnex.ops.erp.modules.shopdata.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.shopdata.entity.ErpShopDataInput;

/**
 * 商户资料录入Dao
 * 
 * @author SunQ
 * @date 2017年12月7日
 */
@MyBatisDao
public interface ErpShopDataInputDao extends CrudDao<ErpShopDataInput> {

    /**
     * 获取已经提交资料录入流程集合
     *
     * @param erpShopDataInput
     * @return
     * @date 2017年12月8日
     * @author SunQ
     */
    List<ErpShopDataInput> findListByParams(ErpShopDataInput erpShopDataInput);
    
    /**
     * 通过流程ID获取对象
     *
     * @param prosIncId
     * @return
     * @date 2017年12月9日
     * @author SunQ
     */
    ErpShopDataInput getByProsIncId(@Param("procInsId") String procInsId);
    
    /**
     * 获取关注的流程ID集合
     *
     * @param erpShopDataInput
     * @return
     * @date 2017年12月14日
     * @author SunQ
     */
    List<String> findFollowByParams(ErpShopDataInput erpShopDataInput);
    
    /**
     * 获取商户是否提交过商户开户流程
     *
     * @param shopId
     * @return
     * @date 2017年12月14日
     * @author SunQ
     */
    int countByShopId(String shopId);

    /**
     * 通过商户掌贝ID获取对象
     * @param shopId
     * @return
     */
    ErpShopDataInput getByShopId(@Param("shopId") String shopId);
    
    List<ErpShopDataInput> getByOrderId(String orderId);
    
    /**
     * 通过掌贝ID获取正在进行的任务ID集合
     *
     * @param zhangbeiId
     * @return
     * @date 2018年1月18日
     * @author SunQ
     */
    List<String> findTaskId(String zhangbeiId);
}