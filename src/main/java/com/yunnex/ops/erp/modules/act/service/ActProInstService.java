package com.yunnex.ops.erp.modules.act.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.yunnex.ops.erp.common.service.BaseService;
import com.yunnex.ops.erp.modules.act.dao.ActProcInstDao;
import com.yunnex.ops.erp.modules.workflow.flow.asyevent.AsyUpgradeComboDto;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowVariableConstants;

/**
 * 流程定义相关Controller
 * 
 * @author ThinkGem
 * @version 2013-11-03
 */
@Service
public class ActProInstService extends BaseService {

    @Autowired
    private ActProcInstDao actProcInstDao;

    @Transactional(readOnly = false)
    public void addBatchRuTask(String procInsId, String oldProcInsId, String procDefId) {
        actProcInstDao.addBatchRuTask(procInsId, oldProcInsId, procDefId);
    }

    @Transactional(readOnly = false)
    public void addBatchRuVariable(String procInsId, String oldProcInsId) {
        actProcInstDao.addBatchRuVariable(procInsId, oldProcInsId);
    }

    @Transactional(readOnly = false)
    public int addRuTask(AsyUpgradeComboDto asyDto) {
        return actProcInstDao.addRuTask(asyDto);
    }

    @Transactional(readOnly = false)
    public int deleteRuTaskByProcId(String procInsId) {
        return actProcInstDao.deleteRuTaskByProcId(procInsId);
    }

    @Transactional(readOnly = false)
    public int deleteRuVariableByProcId(String procInsId, String oldProcInsId) {
        return actProcInstDao.deleteRuVariableByProcId(procInsId, oldProcInsId);
    }

    @Transactional(readOnly = false)
    public int addHiTaskInst(AsyUpgradeComboDto asyDto) {
        return actProcInstDao.addHiTaskInst(asyDto);
    }

    @Transactional(readOnly = false)
    public void addHiTaskInstByProcInsId(String procInsId, String oldProcInsId) {
        actProcInstDao.addHiTaskInstByProcInsId(procInsId, oldProcInsId);
    }

    @Transactional(readOnly = false)
    public int deleteHiTaskByProcId(String procInsId) {
        return actProcInstDao.deleteHiTaskByProcId(procInsId);
    }

    @Transactional(readOnly = false)
    public void addBatchHiProcInst(String procInsId, String oldProcInsId) {
        actProcInstDao.addBatchHiProcInst(procInsId, oldProcInsId);
    }

    @Transactional(readOnly = false)
    public int deleteHiProcInstByProcId(String procInsId) {
        return actProcInstDao.deleteHiProcInstByProcId(procInsId);
    }

    @Transactional(readOnly = false)
    public void addBatchFlowForm(String procInsId, String oldProcInsId) {
        actProcInstDao.addBatchFlowForm(procInsId, oldProcInsId);
    }

    @Transactional(readOnly = false)
    public void addBatchOrderFile(String procInsId, String oldProcInsId) {
        actProcInstDao.addBatchOrderFile(procInsId, oldProcInsId);
    }

    @Transactional(readOnly = false)
    public void addBatchOrderFlowUser(String procInsId, String oldProcInsId, String orderId) {
        actProcInstDao.addBatchOrderFlowUser(procInsId, oldProcInsId, orderId);
    }

    @Transactional(readOnly = false)
    public void addBatchZhctRuTask(String procInsId, String oldProcInsId) {
        actProcInstDao.addBatchZhctRuTask(procInsId, oldProcInsId);
    }

    @Transactional(readOnly = false)
    public void deleteFlowUserByProInsId(String procInsId) {
        actProcInstDao.deleteFlowUserByProInsId(procInsId);
    }

    @Transactional(readOnly = false)
    public void updateRuTaskExIdByproId(String procInsId, String oldProcInsId) {
        actProcInstDao.updateRuTaskExIdByproId(procInsId, oldProcInsId);
    }

    @Transactional(readOnly = false)
    public void updateActIdByProId(String actId, String procInsId) {
        actProcInstDao.updateActIdByProId(actId, procInsId);
    }

    @Transactional(readOnly = false)
    public void deleteExecuIionByProId(String procInsId) {
        actProcInstDao.deleteExecuIionByProId(procInsId);
    }

    @Transactional(readOnly = false)
    public void updateExeCuTionByPid(String procInsId, String oldProcInsId, String procDefId) {
        actProcInstDao.updateExeCuTionByPid(procInsId, oldProcInsId, procDefId);
    }

    @Transactional(readOnly = false)
    public void deleteHiVarinstByProId(String procInsId, String oldProcInsId) {
        actProcInstDao.deleteHiVarinstByProId(procInsId, oldProcInsId);
    }

    @Transactional(readOnly = false)
    public void updateRuVarByProcId(String procInsId, String text, String name) {
        actProcInstDao.updateRuVarByProcId(procInsId, text, name);
    }

    @Transactional(readOnly = false)
    public void updateHiVarByProcId(String procInsId, String text, String name) {
        actProcInstDao.updateHiVarByProcId(procInsId, text, name);
    }

    @Transactional(readOnly = false)
    public void updateEcuParentId(String procInsId, String oldProcInsId) {
        actProcInstDao.updateEcuParentId(procInsId, oldProcInsId);
    }

    @Transactional(readOnly = false)
    public void updteExecActId(String actId, String procInsId) {
        actProcInstDao.updteExecActId(actId, procInsId);
    }

    @Transactional(readOnly = false)
    public int addActRuExcution(AsyUpgradeComboDto asyDto) {
        return actProcInstDao.addActRuExcution(asyDto);
    }

    @Transactional(readOnly = false)
    public int addRuTaskExcuId(AsyUpgradeComboDto asyDto) {
        return actProcInstDao.addRuTaskExcuId(asyDto);
    }

    @Transactional(readOnly = false)
    public void addFlowServiceItemLink(String procInsId, String serviceSourceId) {
        actProcInstDao.addFlowServiceItemLink(procInsId, serviceSourceId);
    }

    @Transactional(readOnly = false)
    public void addGoodServiceInfoById(String Id, String orderId, String oldProcInsId, String serviceItemId) {
        actProcInstDao.addGoodServiceInfoById(Id, orderId, oldProcInsId, serviceItemId);
    }

    @Transactional(readOnly = false)
    public void addBatchHiActinst(String procInsId, String oldProcInsId, String procDefId) {
        actProcInstDao.addBatchHiActinst(procInsId, oldProcInsId, procDefId);
    }

    @Transactional(readOnly = false)
    public void deleteHiactInstByProId(String procInsId) {
        actProcInstDao.deleteHiactInstByProId(procInsId);
    }

    @Transactional(readOnly = false)
    public void addBatchHiVarinst(String procInsId, String oldProcInsId) {
        actProcInstDao.addBatchHiVarinst(procInsId, oldProcInsId);
    }

    @Transactional(readOnly = false)
    public void updateSysHiActinstInfo(String procInsId, String actId) {
        actProcInstDao.updateSysHiActinstInfo(procInsId, actId);
    }

    @Transactional(readOnly = false)
    public void deleteHiVarInstNameByProId(String procInsId) {
        actProcInstDao.deleteHiVarInstNameByProId(procInsId);
    }

    /**
     * 基础版 升级 智能客流 流程变量处理
     *
     * @param newProcInsId
     * @param oldProcInsId
     * @date 2018年9月19日
     * @author linqunzhi
     */
    @Transactional
    public void managerUpgradeVariable(String newProcInsId, String oldProcInsId) {
        logger.info("managerUpgradeVariable start | newProcInsId={}|oldProcInsId={}", newProcInsId, oldProcInsId);
        //获取需要删除的老流程变量 
        List<String> oldVariableList = needDeleteOldVariable();
        this.deleteRuVariableByNames(oldProcInsId, oldVariableList);
        // 删除老流程变量存在的新流程变量
        this.deleteRuVariableByProcId(newProcInsId, oldProcInsId);
        // 将老流程变量 复制 到 新流程变量中
        this.addBatchRuVariable(newProcInsId, oldProcInsId);
        actProcInstDao.updateHiVarinstIdByProId(newProcInsId);
        logger.info("managerUpgradeVariable end");
    }

    /**
     * 根据流程id和变量名称集合 删除流程变量
     *
     * @param procInsId
     * @param variableList
     * @date 2018年9月19日
     * @author linqunzhi
     */
    @Transactional
    public void deleteRuVariableByNames(String procInsId, List<String> variableList) {
        logger.info("deleteRuVariableByNames start");
        String variableListStr = JSON.toJSONString(variableList);
        logger.info("param | procInsId={}|variableListStr={}", procInsId, variableListStr);
        actProcInstDao.deleteRuVariableByNames(procInsId, variableList);
        logger.info("deleteRuVariableByNames end");
    }

    /**
     * 需要删除的老流程变量
     *
     * @return
     * @date 2018年9月19日
     * @author linqunzhi
     */
    private static List<String> needDeleteOldVariable() {
        List<String> list = new ArrayList<String>();
        list.add(FlowVariableConstants.GOOD_SERVIE_TYPE_LIST);
        return list;
    }
    
    @Transactional(readOnly = false)
    public void updateHiActInstName(  String name,String actId ,String taskId ) {
          actProcInstDao.updateHiActInstName(name,actId,taskId);
   } 
    @Transactional(readOnly = false)
    public void updateHiTaskInstName(  String name,String taskId ) {
          actProcInstDao.updateHiTaskInstName(name,taskId);
   } 
    @Transactional(readOnly = false)
    public void updateActInstTimeByProId(  String procInsId,String actId ) {
          actProcInstDao.updateActInstTimeByProId(procInsId,actId);
   }  
    
    @Transactional(readOnly = false)
    public void deletRuExcuByproIdAndActId(String actId, String procInsId) {
        actProcInstDao.deletRuExcuByproIdAndActId(actId, procInsId);
    }
}
