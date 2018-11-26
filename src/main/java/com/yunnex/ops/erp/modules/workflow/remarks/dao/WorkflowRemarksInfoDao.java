package com.yunnex.ops.erp.modules.workflow.remarks.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.workflow.remarks.entity.WorkflowRemarksInfo;

/**
 * 流程备注DAO接口
 * 
 * @author sunq
 * @version 2018-03-29
 */
@MyBatisDao
public interface WorkflowRemarksInfoDao extends CrudDao<WorkflowRemarksInfo> {
    
    /**
     * 获取流程下的备注列表
     *
     * @param procInsId
     * @param flowType
     * @return
     * @date 2018年3月29日
     * @author SunQ
     */
    List<WorkflowRemarksInfo> findListByProcInsId(@Param("procInsId") String procInsId);
    
    /**
     * 获取流程下的备注列表
     *
     * @param procInsId
     * @param flowType
     * @return
     * @date 2018年3月29日
     * @author SunQ
     */
    String findMessageByProcInsId(@Param("procInsId") String procInsId);

    /**
     * 业务定义：查询流程备注项
     * 
     * @date 2018年7月9日
     * @author R/Q
     */
    List<Map<String, Object>> queryRemarkItems(@Param("workflowKey") String workflowKey, @Param("workflowNodeKey") String workflowNodeKey);
}