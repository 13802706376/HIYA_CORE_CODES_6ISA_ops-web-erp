package com.yunnex.ops.erp.modules.material.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.material.dto.MaterialCreationExcelResponseDto;
import com.yunnex.ops.erp.modules.material.dto.MaterialCreationRequestDto;
import com.yunnex.ops.erp.modules.material.entity.ErpOrderMaterialCreation;

/**
 * 物料制作DAO接口
 *
 * @author yunnex
 * @version 2018-05-25
 */
@MyBatisDao
public interface ErpOrderMaterialCreationDao extends CrudDao<ErpOrderMaterialCreation> {
    List<ErpOrderMaterialCreation> findByPage(MaterialCreationRequestDto dto);

    List<ErpOrderMaterialCreation> findMaterialCreation(String procInsId);

    Long count(MaterialCreationRequestDto dto);

    List<MaterialCreationExcelResponseDto> findByPageWithExcel(MaterialCreationRequestDto requestDto);

    ErpOrderMaterialCreation findByOrderNumber(String orderNumber);

    /**
     * 通过易商订单ID查找订单的物料制作内容
     *
     * @param ysOrderId
     * @return
     */
    ErpOrderMaterialCreation findByYsOrderId(Long ysOrderId);
    
    void updateAdviser(@Param("procInsId") String procInsId,@Param("userId") String userId);

    /**
     * 根据流程id获取唯一 数据
     *
     * @param procInsId
     * @return
     * @date 2018年7月24日
     * @author linqunzhi
     */
    ErpOrderMaterialCreation getByProcInsId(@Param("procInsId") String procInsId);
    
    void changeRoleUser(@Param("procInsId") String procInsId,@Param("userId") String userId,@Param("roleName") String roleName);
}
