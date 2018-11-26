/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yunnex.ops.erp.modules.act.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.act.entity.Act;
import com.yunnex.ops.erp.modules.workflow.flow.asyevent.AsyUpgradeComboDto;

/**
 * 审批DAO接口
 * 
 * @author thinkgem
 * @version 2014-05-16
 */
@MyBatisDao
public interface ActProcInstDao extends CrudDao<Act> {

    void addBatchRuTask(@Param("procInsId") String procInsId,@Param("oldProcInsId") String oldProcInsId,@Param("procDefId") String procDefId );
    void updateRuTaskExIdByproId(@Param("procInsId") String procInsId,@Param("oldProcInsId") String oldProcInsId);
    void updateActIdByProId (@Param("actId") String actId,@Param("procInsId") String procInsId);
    void deleteExecuIionByProId (@Param("procInsId") String procInsId);
    void updateExeCuTionByPid (@Param("procInsId") String procInsId,@Param("oldProcInsId") String oldProcInsId,@Param("procDefId") String procDefId);
    void addBatchRuVariable(@Param("procInsId") String procInsId,@Param("oldProcInsId") String oldProcInsId);
    int addRuTask(AsyUpgradeComboDto  asyDto );
    int deleteRuTaskByProcId (@Param("procInsId") String procInsId);
    int deleteRuVariableByProcId (@Param("procInsId") String procInsId,@Param("oldProcInsId") String oldProcInsId);
    int addHiTaskInst (AsyUpgradeComboDto  asyDto );
    void addHiTaskInstByProcInsId(@Param("procInsId") String procInsId,@Param("oldProcInsId") String oldProcInsId);
    int deleteHiTaskByProcId  (@Param("procInsId") String procInsId);
    void addBatchHiProcInst(@Param("procInsId") String procInsId,@Param("oldProcInsId") String oldProcInsId);
    int deleteHiProcInstByProcId (@Param("procInsId") String procInsId);
    void addBatchFlowForm (@Param("procInsId") String procInsId,@Param("oldProcInsId") String oldProcInsId);
    void addBatchOrderFile (@Param("procInsId") String procInsId,@Param("oldProcInsId") String oldProcInsId);
    void addBatchOrderFlowUser (@Param("procInsId") String procInsId,@Param("oldProcInsId") String oldProcInsId,@Param("orderId") String orderId);
    void  addBatchZhctRuTask (@Param("procInsId") String procInsId,@Param("oldProcInsId") String oldProcInsId);
    void  deleteFlowUserByProInsId (@Param("procInsId") String procInsId);
    void  deleteHiVarinstByProId(@Param("procInsId") String procInsId,@Param("oldProcInsId") String oldProcInsId);
    void  updateRuVarByProcId (@Param("procInsId") String procInsId,@Param("text") String text ,@Param("name") String name);
    void  updateHiVarByProcId (@Param("procInsId") String procInsId,@Param("text") String text ,@Param("name") String name);
    void updateEcuParentId(@Param("procInsId") String procInsId,@Param("oldProcInsId") String oldProcInsId);
    void updteExecActId(@Param("actId") String actId,@Param("procInsId") String procInsId);
    int addActRuExcution(AsyUpgradeComboDto  asyDto );
    int addRuTaskExcuId(AsyUpgradeComboDto  asyDto );
    void addFlowServiceItemLink(@Param("procInsId") String procInsId, @Param("serviceSourceId") String serviceSourceId);
    void addGoodServiceInfoById(@Param("Id") String Id,  @Param("orderId") String orderId, @Param("oldProcInsId") String oldProcInsId, @Param("serviceItemId") String serviceItemId);
    void addBatchHiActinst (@Param("procInsId") String procInsId,@Param("oldProcInsId") String oldProcInsId,@Param("procDefId") String procDefId);
    void deleteHiactInstByProId(@Param("procInsId") String procInsId);
    void addBatchHiVarinst(@Param("procInsId") String procInsId,@Param("oldProcInsId") String oldProcInsId);
    void updateSysHiActinstInfo(@Param("procInsId") String procInsId,@Param("actId") String actId);
    void deleteHiVarInstNameByProId(@Param("procInsId") String procInsId);

    /**
     * 根据流程id和变量名称集合 删除流程变量
     *
     * @param procInsId
     * @param variableNameList
     * @date 2018年9月19日
     * @author linqunzhi
     */
    int deleteRuVariableByNames(@Param("procInsId") String procInsId, @Param("variableNameList") List<String> variableNameList);
    
    void updateHiActInstName(@Param("name") String name,@Param("actId") String actId,@Param("taskId") String taskId);  
    
    void updateHiTaskInstName(@Param("name") String name,@Param("taskId") String taskId);  
    void updateHiVarinstIdByProId(@Param("procInsId") String procInsId);
    void updateActInstTimeByProId(@Param("procInsId") String procInsId,@Param("actId") String actId);
    void deletRuExcuByproIdAndActId(@Param("actId") String actId,@Param("procInsId") String procInsId);
    
}
