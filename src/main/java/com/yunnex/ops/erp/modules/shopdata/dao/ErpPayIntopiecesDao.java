package com.yunnex.ops.erp.modules.shopdata.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.shopdata.entity.ErpPayIntopieces;

/**
 * 支付进件流程DAO
 * 
 * @author SunQ
 * @date 2017年12月20日
 */
@MyBatisDao
public interface ErpPayIntopiecesDao extends CrudDao<ErpPayIntopieces> {

    /**
     * 通过流程ID获取对象
     *
     * @param prosIncId
     * @return
     * @date 2017年12月9日
     * @author SunQ
     */
    ErpPayIntopieces getByProsIncId(@Param("procInsId") String procInsId);
    
    /**
     * 获取支付进件流程对接集合
     *
     * @param userId
     * @param orderNumber
     * @param shopName
     * @return
     * @date 2017年12月9日
     * @author SunQ
     */
    List<ErpPayIntopieces> findListByParams(@Param("userId") String userId, @Param("orderNumber") String orderNumber, @Param("shopName") String shopName);
    
    /**
     * 通过商户ID获取流程ID集合
     *
     * @param shopId
     * @param intopiecesType
     * @return
     * @date 2018年1月18日
     * @author SunQ
     */
    List<String> findTaskId(@Param("shopId") String shopId, @Param("intopiecesType") String intopiecesType);
}