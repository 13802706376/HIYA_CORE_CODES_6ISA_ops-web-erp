package com.yunnex.ops.erp.modules.promotion.dao;

import java.util.List;
import java.util.Map;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.promotion.entity.ErpPromoteDataMomo;
import com.yunnex.ops.erp.modules.promotion.entity.ErpPromoteDataMomoSum;

/**
 * 推广数据-陌陌DAO接口
 * 
 * @author yunnex
 * @version 2018-05-10
 */
@MyBatisDao
public interface ErpPromoteDataMomoDao extends CrudDao<ErpPromoteDataMomo> {

    /**
     * 
     * 业务定义：查询陌陌推广数据
     * 
     * @date 2018年5月10日
     * @author R/Q
     */
    List<ErpPromoteDataMomo> queryMomoData(ErpPromoteDataMomo paramObj);

    /**
     * 
     * 业务定义：根据分单ID和数据时间删除对应推广数据
     * 
     * @date 2018年5月10日
     * @author R/Q
     */
    void deleteBySplitIdAndDataTime(ErpPromoteDataMomo paramObj);

    /**
     * 
     * 业务定义：查询陌陌合计数据
     * 
     * @date 2018年5月10日
     * @author R/Q
     */
    ErpPromoteDataMomoSum queryMomoDataSum(ErpPromoteDataMomo paramObj);

    /**
     * 
     * 业务定义：批量插入数据
     * 
     * @date 2018年5月14日
     * @author R/Q
     */
    void batchInsert(Map<String, Object> paramMap);

    /**
     * 
     * 业务定义：批量删除日期重复数据
     * 
     * @date 2018年5月14日
     * @author R/Q
     */
    void batchDelete(Map<String, Object> paramMap);
	
}