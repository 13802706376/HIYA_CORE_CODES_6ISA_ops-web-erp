package com.yunnex.ops.erp.modules.visit.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.constant.CommonConstants;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;
import com.yunnex.ops.erp.modules.visit.constants.ErpVisitServiceConstants;
import com.yunnex.ops.erp.modules.visit.dao.ErpVisitServiceInfoDao;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitServiceDetailInfo;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitServiceInfo;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitServiceItem;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitServiceItemRecord;
import com.yunnex.ops.erp.modules.workflow.flow.common.JykFlowConstants;
import com.yunnex.ops.erp.modules.workflow.flow.constant.DeliveryFlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.service.JykFlowBeiyiService;
import com.yunnex.ops.erp.modules.workflow.flow.service.WorkFlowService;

/**
 * 上门服务Service
 * 
 * @author R/Q
 * @version 2018-05-26
 */
@Service
public class ErpVisitServiceInfoService extends CrudService<ErpVisitServiceInfoDao, ErpVisitServiceInfo> {


    private static final String MESSAGE = "message";

    private static final String CODE = "code";

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;
    
    @Autowired
    @Lazy(true)
    private JykFlowBeiyiService jykFlowBeiyiService;
    @Autowired
    private WorkFlowService workFlowService;
    

    @Override
    public ErpVisitServiceInfo get(String id) {
        ErpVisitServiceInfo shopServiceObj = super.get(id);
        if (shopServiceObj != null) {
            shopServiceObj.setItemRecords(dao.queryServiceItemRecordDataByServiceId(id));// 服务项数据
        }
        return shopServiceObj;
    }
    
    /**
     * 业务定义：校验预约开始时间冲突
     * 
     * @date 2018年6月4日
     * @author wangwei
     */
    public ErpVisitServiceInfo getVisitDetail(String serviceGoalCode) {
        return dao.getVisitDetail(serviceGoalCode);
    }

    /**
     * 业务定义：通过流程实例ID+上门目的code获取唯一上门服务数据
     * 
     * @date 2018年5月29日
     * @param procInsId 流程实例ID
     * @param serviceGoalCode 上门目的code
     * @author R/Q
     */
    public ErpVisitServiceInfo get(@Param("procInsId") String procInsId, @Param("serviceGoalCode") String serviceGoalCode) {
        ErpVisitServiceInfo shopServiceObj = dao.get(procInsId, serviceGoalCode);
        if (shopServiceObj != null) {
            shopServiceObj.setItemRecords(dao.queryServiceItemRecordDataByServiceId(shopServiceObj.getId()));// 服务项数据
        }
        return shopServiceObj;
    }

    @Override
    @Transactional(readOnly = false)
    public void save(ErpVisitServiceInfo erpVisitServiceInfo) {
        if (erpVisitServiceInfo == null) {
            throw new ServiceException("保存上门服务，传递数据错误。");
        }
        try {
            User user = UserUtils.getUser();
            super.save(erpVisitServiceInfo);
            if (CollectionUtils.isNotEmpty(erpVisitServiceInfo.getItemRecords())) {
                dao.deleteServiceItemRecordDataByServiceId(erpVisitServiceInfo.getId());
                dao.batchInsertServiceItemRecordData(erpVisitServiceInfo.getItemRecords(), erpVisitServiceInfo.getId(), user.getId());
            }
        } catch (Exception e) {
            logger.error("保存上门服务数据发生错误，erpVisitServiceInfo={}，错误信息={}", erpVisitServiceInfo, e);
            throw new ServiceException("保存上门服务数据发生错误。");
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(ErpVisitServiceInfo erpVisitServiceInfo) {
        super.delete(erpVisitServiceInfo);
    }

    /**
     * 业务定义：分页查询商户服务数据列表
     * 
     * @date 2018年5月26日
     * @author R/Q
     */
    public Page<ErpVisitServiceInfo> queryShopServiceDataList(Page<ErpVisitServiceInfo> page, ErpVisitServiceInfo paramObj) {
        paramObj.setUpdateBy(UserUtils.getUser());
        page.setList(dao.queryVisitServiceDataList(paramObj, page));
        return page;
    }

    /**
     * 业务定义：查询服务项目
     * 
     * @date 2018年5月29日
     * @param paramObj.serviceGoalCode 服务目的
     * @param paramObj.shopInfoId 商户ID
     * @author R/Q
     */
    public List<String> findVisitIdByProcInsId(String procInsId, String serviceGoalCode) {
        return dao.findVisitIdByProcInsId(procInsId, serviceGoalCode);
    }
   
    /**
     * 业务定义：查询服某条流程 除了这个上门目的 其他 务项目
     *
     * @param procInsId
     * @param serviceGoalCode
     * @return
     * @date 2018年8月31日
     * @author zjq
     */
    public List<ErpVisitServiceInfo> getListByProcIdAndGoalCode(String procInsId, String serviceGoalCode) {
        return dao.getListByProcIdAndGoalCode(procInsId, serviceGoalCode);
    }

    
    
    /**
     * 业务定义：查询服务项目
     * 
     * @date 2018年5月29日
     * @param paramObj.serviceGoalCode 服务目的
     * @param paramObj.shopInfoId 商户ID
     * @author R/Q
     */
    public List<Map<String, String>> findDoorVisitIdByProcInsId(String procInsId) {
        return dao.findDoorVisitIdByProcInsId(procInsId);
    }

    /**
     * 业务定义：查询服务项目
     * 
     * @date 2018年5月29日
     * @param paramObj.serviceGoalCode 服务目的
     * @param paramObj.shopInfoId 商户ID
     * @author R/Q
     */
    public List<ErpVisitServiceItem> queryServiceItemData(ErpVisitServiceInfo paramObj) {
        return dao.queryServiceItemData(paramObj);
    }

    /**
     * 业务定义：查询上门目的列表
     * 
     * @date 2018年5月29日
     * @author R/Q
     */
    public List<Map<String, Object>> queryServiceGoalData(String serviceTypeCode) {
        return dao.queryServiceGoalData(serviceTypeCode);
    }

    /**
     * 业务定义：新增/保存上门服务
     * 
     * @date 2018年5月29日
     * @author R/Q
     */
    @Transactional(readOnly = false)
    public void saveVisitService(ErpVisitServiceInfo paramObj) {
        if (paramObj != null && StringUtils.isBlank(paramObj.getId())) {
            paramObj.setServiceUser(UserUtils.getUser().getId());
        }
        this.save(paramObj);
    }

    /**
     * 业务定义：修改上门服务
     * 
     * @date 2018年5月29日
     * @author R/Q
     */
    @Transactional(readOnly = false)
    public void updateVisitService(ErpVisitServiceInfo paramObj) {
        paramObj.setAuditStatus(ErpVisitServiceConstants.AUDIT_STATUS_SUBMITTED);// 状态=待审核
        this.save(paramObj);
    }

    /**
     * 业务定义：取消上门服务
     * 
     * @date 2018年5月29日
     * @author R/Q
     */
    @Transactional(readOnly = false)
    public void cancelVisitService(ErpVisitServiceInfo paramObj) {
        ErpVisitServiceInfo dbObj = super.get(paramObj.getId());
        if (ErpVisitServiceConstants.SERVICE_TYPE_BASIC.equals(dbObj.getServiceTypeCode())) {
            throw new ServiceException("基础服务不能取消。");
        }
        paramObj.setAuditStatus(ErpVisitServiceConstants.AUDIT_STATUS_CALLED);// 状态=已取消
        this.save(paramObj);
        // 关闭流程
        String procInsId = dbObj.getProcInsId();
        if (StringUtils.isNotBlank(procInsId) && runtimeService.createProcessInstanceQuery().processInstanceId(procInsId).count() == 1) {
            runtimeService.deleteProcessInstance(procInsId, "取消上门服务");
        }
    }

    /**
     * 业务定义：审核上门服务
     * 
     * @date 2018年5月29日
     * @author R/Q
     */
    @Transactional(readOnly = false)
    public void auditVisitService(ErpVisitServiceInfo paramObj) {
        if (!ErpVisitServiceConstants.AUDIT_STATUS_AUDITED.equals(paramObj.getAuditStatus()) && !ErpVisitServiceConstants.AUDIT_STATUS_DISMISSED
                        .equals(paramObj.getAuditStatus())) {
            logger.error("上门服务审核，审核状态错误，paramObj={}", paramObj);
            throw new ServiceException("上门服务审核，审核状态错误。");
        }
        this.save(paramObj);
    }

    /**
     * 业务定义：完成上门服务
     * 
     * @date 2018年5月29日
     * @author R/Q
     */
    @Transactional(readOnly = false)
    public void completedVisitService(ErpVisitServiceInfo paramObj) {
        ErpVisitServiceInfo dbObj = super.get(paramObj.getId());
        User user = UserUtils.getUser();
        if (!StringUtils.equals(user.getId(), dbObj.getServiceUser())) {
            throw new ServiceException("无法完成非本人的上门服务。");
        }
        paramObj.setAuditStatus(ErpVisitServiceConstants.AUDIT_STATUS_COMPLETED);
        this.save(paramObj);
    }

    /**
     * 业务定义：查询商户培训类型服务项记录，查询范围=首次营销策划上门服务+物料上门服务
     * 
     * @date 2018年5月31日
     * @author R/Q
     */
    public List<ErpVisitServiceItemRecord> queryTrainItemRecord(String shopInfoId) {
        return dao.queryTrainItemRecord(shopInfoId);
    }

    /**
     * 业务定义：查询商户培训类型服务项，查询范围=首次营销策划上门服务+物料上门服务
     * 
     * @date 2018年5月31日
     * @author R/Q
     */
    public List<ErpVisitServiceItem> queryTrainItem() {
        return dao.queryTrainItem();
    }

    /**
     * 业务定义：校验预约开始时间冲突
     * 
     * @date 2018年6月4日
     * @author R/Q
     */
    public List<ErpVisitServiceInfo> checkAppointedDate(ErpVisitServiceInfo paramObj) {
        paramObj.setServiceUser(UserUtils.getUser().getId());
        return dao.checkAppointedDate(paramObj);
    }
    
    /**
     * 业务定义：校验预约开始时间冲突
     * 
     * @date 2018年6月4日
     * @author wangwei
     */
    public List<ErpVisitServiceInfo> getHeader() {
        return dao.getHeader();
    }
    
    /**
     * 业务定义：校验预约开始时间冲突
     * 
     * @date 2018年6月4日
     * @author wangwei
     */
    public List<ErpVisitServiceInfo> getHeaderText(String serviceGoalTxt) {
        return dao.getHeaderText(serviceGoalTxt);
    }
    
    /**
     * 业务定义：依据根据上门服务查询权限查询对应团队信息
     * 
     * @date 2018年7月5日
     * @author R/Q
     */
    public List<Map<String, Object>> queryTeamByRole() {
        return dao.queryTeamByRole(UserUtils.getUser().getId());
    }



    /**
     * 小程序验收确认
     *
     * @param visit 上门ID
     * @return
     * @date 2018年7月9日
     * @author zjq
     */
    @Transactional(readOnly = false)
    public JSONObject acceptance(String visitId) {

        logger.info("接收到小程序推送的上门服务ID[{}]:", visitId);

        JSONObject resObject = new JSONObject();

        Optional<String> visit = Optional.ofNullable(StringUtils.trimToNull(visitId));
        
        if (BooleanUtils.isNotTrue(visit.isPresent())) {
            resObject.put(CODE, "1");
            resObject.put(MESSAGE, "非法参数!");
            return resObject;
        }

        visit.ifPresent(_visitId -> {
            
            ErpVisitServiceInfo erpVisitServiceInfo = this.get(_visitId);

            if (null != erpVisitServiceInfo && Optional.ofNullable(erpVisitServiceInfo.getProcInsId()).isPresent()) {

                Task task = taskService.createTaskQuery().processInstanceId(StringUtils.trimToNull(erpVisitServiceInfo.getProcInsId()))
                                .taskDefinitionKeyLike(DeliveryFlowConstant.VISIT_SERVICE_REMIND.concat(Constant.PERCENT)).singleResult();

                Optional<Task> optional = Optional.ofNullable(task);

                optional.orElseGet(() -> {
                    resObject.put(CODE, "2");
                    resObject.put(MESSAGE, "任务数据查询为空!");
                    return null;
                });
                optional.ifPresent(_task -> {
                    Task trainTask = this.taskService.createTaskQuery().processInstanceId(_task.getProcessInstanceId())
                                    .taskDefinitionKeyLike("train_service_record%").singleResult();
                            if (trainTask != null) {
                                Map<String, Object> vars=new HashMap<String, Object>();
                                this.workFlowService.completeFlow2(new String[] { JykFlowConstants.OPERATION_ADVISER }, trainTask.getId(),
                                        trainTask.getProcessInstanceId(), "培训备忘", vars);
                            }
                    if (DeliveryFlowConstant.VISIT_SERVICE_REMIND_FIRST_3V3.equals(_task.getTaskDefinitionKey())) {
                        // 保存 上门服务完成（首次营销策划服务）
                        jykFlowBeiyiService.saveVisitServiceTime(_task.getProcessInstanceId());
                    }
                    taskService.complete(_task.getId());
                    erpVisitServiceInfo.setAuditStatus(ErpVisitServiceConstants.AUDIT_STATUS_COMPLETED);
                    this.save(erpVisitServiceInfo);
                    resObject.put(CODE, "0");
                    logger.info("小程序确认验收,任务ID[{}]", _task.getId());
                });

            } else {
                resObject.put(CODE, "3");
                resObject.put(MESSAGE, "非法的上门服务ID");
            }
            
        });
        logger.info("ERP处理结果:{}", resObject);
        return resObject;
    }

    /**
     * 业务定义：查询团队成员上门服务数量-分页
     * 
     * @date 2018年7月12日
     * @author R/Q
     */
    public Map<String, Object> queryTeamUserServiceCount(ErpVisitServiceInfo paramObj, Page<Map<String, Object>> page) {
    	paramObj.setUserId(UserUtils.getUser().getId());
        Map<String, Object> returnMap = Maps.newHashMap();
        List<Map<String, Object>> list=dao.queryTeamUserServiceCount(paramObj, UserUtils.getUser().getAgentId(), page);
        page.setList(list);
        long count = list==null?0:list.size();
        // 获取总条数
        if (page != null) {
            count = page.getCount();
        }
        page.setCount(count);
        returnMap.put("page", page);
//        if(list.size()>1){
//        	returnMap.put("total", dao.queryTeamUserServiceTotal(paramObj, UserUtils.getUser().getAgentId()));
//        }
        return returnMap;
    }

    /**
     * 获取上门信息
     *
     * @param procInsId
     * @param serviceGoalCode
     * @return
     * @date 2018年7月23日
     * @author linqunzhi
     */
    public ErpVisitServiceInfo getByGoalCode(String procInsId, String serviceGoalCode) {
        ErpVisitServiceInfo info = dao.getByGoalCode(procInsId, serviceGoalCode);
        return info;
    }

    /**
     * 将上门提醒保存为Y
     *
     * @param id
     * @date 2018年8月1日
     * @author linqunzhi
     */
    @Transactional(readOnly = false)
    public void saveRemindFlagYes(String id) {
        if (StringUtils.isBlank(id)) {
            return;
        }
        ErpVisitServiceInfo info = new ErpVisitServiceInfo();
        info.setId(id);
        info.setRemindFlag(CommonConstants.Sign.YES);
        this.save(info);
    }



    /**
     * 业务定义：分页查询商户服务数据列表
     * 
     * @date 2018年5月26日
     * @author R/Q
     */
    public Page<ErpVisitServiceDetailInfo> queryShopServiceDetailList(Page<ErpVisitServiceDetailInfo> page, ErpVisitServiceDetailInfo paramObj) {
        paramObj.setUpdateBy(UserUtils.getUser());
        page.setList(dao.queryShopServiceDetailList(paramObj, page));
        return page;
    }
    
    /**
     * 删除上门服务安排记录信息获取上门信息
     *
     * @param serviceInfoId
     * @return
     * @date 2018年7月23日
     */
    public void deleteServiceItemRecordDataByServiceId(String serviceInfoId ) {
        dao.deleteServiceItemRecordDataByServiceId(serviceInfoId);
    }

    /**
     * 业务定义：重置
     * 
     * @date 2018年9月12日
     * @author R/Q
     */
    public void resetByProcInsId(String oldProcInsId, String newProcInsId) {
        dao.resetByProcInsId(oldProcInsId, newProcInsId);
    }

    /**
     * 业务定义：修改服务人员
     * 
     * @date 2018年9月18日
     * @author R/Q
     */
    public void updateServiceUser(String serviceUser, String procInsId) {
        dao.updateServiceUser(serviceUser, procInsId);
    }

  
}
