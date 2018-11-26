/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yunnex.ops.erp.modules.act.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.act.entity.Act;
import com.yunnex.ops.erp.modules.workflow.flow.dto.FlowTaskDto;
import com.yunnex.ops.erp.modules.workflow.flow.from.FlowForm;
import com.yunnex.ops.erp.modules.workflow.flow.from.WorkFlowQueryForm;

/**
 * 审批DAO接口
 * 
 * @author thinkgem
 * @version 2014-05-16
 */
@MyBatisDao
public interface ActDao extends CrudDao<Act> {

    public int updateProcInsIdByBusinessId(Act act);
    
    boolean deleteTask(String id);
    
    boolean deleteVaribleByInstId(String id);
    
    List<FlowForm> findTodoTasks(WorkFlowQueryForm queryForm);
    
    int findTodoTaskCount(WorkFlowQueryForm queryForm);
    
    List<FlowForm> findFollowTasks(WorkFlowQueryForm queryForm);

    int findFollowTaskCount(WorkFlowQueryForm queryForm);

    List<FlowForm> findPendingProductionTasks(WorkFlowQueryForm queryForm);

    int findPendingProductionTaskCount(WorkFlowQueryForm queryForm);
    
    List<FlowForm> findTeamTasks(WorkFlowQueryForm queryForm);
    
    int findTeamTaskCount(WorkFlowQueryForm queryForm);
    
    /**
     * 获取流程正在进行的任务
     *
     * @param procInsId
     * @return
     * @date 2018年1月31日
     * @author SunQ
     */
    List<String> findTaskKeyByProcInsId(@Param("procInsId") String procInsId);
    
    /**
     * 获取流程正在进行的任务名称
     *
     * @param procInsId
     * @return
     * @date 2018年2月26日
     * @author SunQ
     */
    List<String> findTaskNameByProcInsId(@Param("procInsId") String procInsId);
    
    List<String> overJyk();
    
    /**
     * 获取流程正在进行的任务名称
     *
     * @param procInsId
     * @return
     * @date 2018年2月26日
     * @author SunQ
     */
    List<String> findTidByProcInsId(@Param("procInsId") String procInsId);

    /**
     * 获取流程正在进行的任务ID
     *
     * @param procInsId
     * @return
     * @date 2018年4月11日
     */
    List<String> findTaskIdByProcInsId(@Param("procInsId") String procInsId);
    
    /**
     * 查看任务对应的角色
     *
     * @param taskId
     * @param processKey
     * @return
     * @date 2018年3月9日
     * @author SunQ
     */
    String getTaskRole(@Param("taskId") String taskId, @Param("processKey") String processKey);
    
    /**
     * 获取流程对应角色的任务ID集合
     *
     * @param procInsId
     * @param processKey
     * @param roleName
     * @return
     * @date 2018年3月9日
     * @author SunQ
     */
    List<String> findRoleTasks(@Param("procInsId") String procInsId, @Param("processKey") String processKey, @Param("roleName") String roleName);

    /**
     * 分页查询任务列表
     *
     * @param queryForm
     * @return
     * @date 2018年5月26日
     * @author zjq
     */
    List<FlowTaskDto> queryTaskListPage(WorkFlowQueryForm queryForm);

    /**
     * 查询当前任务总数
     *
     * @param queryForm
     * @return
     * @date 2018年5月26日
     * @author zjq
     */
    int queryTaskListCount(WorkFlowQueryForm queryForm);

    public List<Map<String, String>> queryTaskKeyList(WorkFlowQueryForm queryForm);

    void changeRoleUser(@Param("procInsId") String procInsId,@Param("userId") String userId,@Param("roleName") String roleName);
    
    
    void changeRoleUser1(@Param("procInsId") String procInsId,@Param("userId") String userId,@Param("roleName") String roleName);
    
    void changeRoleUser2(@Param("procInsId") String procInsId,@Param("userId") String userId,@Param("roleName") String roleName);
    
    public List<Map<String, String>> queryOperatingStatistics(WorkFlowQueryForm queryForm);

    /**
     * 获取 流程id中流程节点是否完成（返回值大于0 ，则为完成）
     *
     * @param procInsId
     * @param taskDefKey
     * @return
     * @date 2018年7月9日
     * @author linqunzhi
     */
    public int getFinishTaskCount(@Param("procInsId") String procInsId, @Param("taskDefKey") String taskDefKey);

    /**
     * 根据掌贝id更新商户下面所有正在运行并且对应处理人角色为运营顾问的任务对应的处理人为指定的运营顾问
     *
     * @param procInsId
     * @param operationAdviserId
     * @return
     * @date 2018年7月12日
     */
    public int updateAssigneeByZhangbeiId(@Param("zhangbeiId") String zhangbeiId, @Param("operationAdviserId") String operationAdviserId);

    /**
     * 业务定义：获取流程实例中节点是否正在执行
     * 
     * @date 2018年8月9日
     * @author R/Q
     */
    public int getRunTaskCount(@Param("procInsId") String procInsId, @Param("taskDefKeys") List<String> taskDefKeys);

}
