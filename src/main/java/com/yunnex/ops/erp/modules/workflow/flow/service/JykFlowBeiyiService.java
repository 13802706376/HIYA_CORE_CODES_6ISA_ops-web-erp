package com.yunnex.ops.erp.modules.workflow.flow.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.constant.CommonConstants;
import com.yunnex.ops.erp.common.service.BaseService;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.modules.act.service.ActProInstService;
import com.yunnex.ops.erp.modules.act.service.ActTaskService;
import com.yunnex.ops.erp.modules.holiday.service.ErpHolidaysService;
import com.yunnex.ops.erp.modules.material.entity.ErpOrderMaterialCreation;
import com.yunnex.ops.erp.modules.material.service.ErpOrderMaterialCreationService;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.order.service.ErpOrderOriginalInfoService;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.shop.service.ErpShopInfoService;
import com.yunnex.ops.erp.modules.sys.constant.RoleConstant;
import com.yunnex.ops.erp.modules.sys.dao.UserDao;
import com.yunnex.ops.erp.modules.sys.entity.ServiceOperation;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.service.ServiceOperationService;
import com.yunnex.ops.erp.modules.sys.service.SysConstantsService;
import com.yunnex.ops.erp.modules.sys.service.UserService;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;
import com.yunnex.ops.erp.modules.visit.constants.ErpVisitServiceConstants;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitServiceInfo;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitServiceItem;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitServiceItemRecord;
import com.yunnex.ops.erp.modules.visit.service.ErpVisitServiceInfoService;
import com.yunnex.ops.erp.modules.workflow.delivery.constant.ErpDeliveryServiceConstants;
import com.yunnex.ops.erp.modules.workflow.delivery.entity.ErpDeliveryService;
import com.yunnex.ops.erp.modules.workflow.delivery.extraModel.DeliveryServiceWorkDays;
import com.yunnex.ops.erp.modules.workflow.delivery.service.ErpDeliveryServiceService;
import com.yunnex.ops.erp.modules.workflow.flow.common.JykFlowConstants;
import com.yunnex.ops.erp.modules.workflow.flow.constant.DeliveryFlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowFormConstant;
import com.yunnex.ops.erp.modules.workflow.flow.dao.JykFlowDao;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderFile;
import com.yunnex.ops.erp.modules.workflow.user.service.ErpOrderFlowUserService;

/**
 * 聚迎客3.2流程
 * 
 * @author hanhan
 * @date 2018年5月7日
 */
@Service
public class JykFlowBeiyiService extends BaseService {

    @Autowired
    private ErpOrderFileService erpOrderFileService;
    @Autowired
    private ErpOrderMaterialCreationService materialCreationService;
    @Autowired
    private WorkFlowService workFlowService;
    @Autowired
    private ErpVisitServiceInfoService erpVisitServiceInfoService;
    @Autowired
    private JykFlowDao jykFlowDao;
    @Autowired
    private ErpFlowFormService erpFlowFormService;
    @Autowired
    private ErpDeliveryServiceService erpDeliveryServiceService;
    @Autowired
    private ErpOrderOriginalInfoService erpOrderOriginalInfoService;
    @Autowired
    private ErpOrderFlowUserService erpOrderFlowUserService;
    @Autowired
    private ActTaskService actTaskService;
    @Autowired
    private WorkFlow3p25Service workFlow3p25Service;
    @Autowired
    private ErpHolidaysService erpHolidaysService;
    @Autowired
    private ErpShopInfoService erpShopInfoService;
    @Autowired
    private ServiceOperationService serviceOperationService;
    @Autowired
    private UserService userService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private SysConstantsService sysConstantsService;
    @Autowired
	private UserDao userDao;
    @Autowired
    private ActProInstService actProInstService;
    /**
     * 物料制作内容提交
     * 
     * @param taskId
     * @param procInsId
     * @param file
     * @return
     */
    @Transactional(readOnly = false)
    public JSONObject materialContentSubmit(String taskId, String procInsId, ErpOrderFile file) {
        Map<String, Object> vars = Maps.newHashMap();
        logger.info("物料制作内容提交", taskId, procInsId, file);
        JSONObject resObject = new JSONObject();
        if (file != null) {
            String filename = file.getFileName();
            if (StringUtil.isBlank(filename) || (!StringUtil.isBlank(filename) && filename.indexOf(".rar") == -1
                    && filename.indexOf(".zip") == -1)) {
                resObject.put("message", "物料制作文件不合理");
                resObject.put("result", false);
                return resObject;
            }
            // 将文件设置为有效
            file.setDelFlag("0");
            this.erpOrderFileService.save(file);
            List<ErpOrderMaterialCreation> list = materialCreationService.findMaterialCreation(procInsId);
            if (list.size() > 1) {
                resObject.put("message", "物料制作单过多有脏数据，请处理");
                resObject.put("result", false);
                return resObject;
            }
            if (CollectionUtils.isEmpty(list)) {
                resObject.put("message", "物料未制作");
                resObject.put("result", false);
                return resObject;
            }
            ErpOrderMaterialCreation em = !CollectionUtils.isEmpty(list) ? list.get(0) : null;
            if (em != null) {
                em.setStatus("waiting_order");
                em.setStatusName("待下单制作");
                em.setLayoutName(file.getFileName());
                em.setLayoutUrl(file.getFilePath());
                this.materialCreationService.save(em);
                this.workFlowService.completeFlow2(
                        new String[] { JykFlowConstants.MATERIAL_ADVISER, JykFlowConstants.OPERATION_ADVISER }, taskId,
                        procInsId, "物料制作内容提交", vars);
                // 保存 物料制作跟踪任务应该完成时间
                saveShouldMaterielTime(procInsId);
                resObject.put("message", "物料制作内容已提交");
                resObject.put("result", true);
            } else {
                resObject.put("message", "物料未制作");
                resObject.put("result", false);
            }
        } else {
            resObject.put("message", "物料制作内容无效");
            resObject.put("result", false);
        }
        return resObject;
    }

    /**
     * 保存 物料制作跟踪任务应该完成时间
     *
     * @param procInsId
     * @date 2018年6月8日
     * @author linqunzhi
     */
    private void saveShouldMaterielTime(String procInsId) {
        // 物料制作跟踪任务应该完成时间
        ErpDeliveryService erpDeliveryService = erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
        if (erpDeliveryService == null) {
            logger.error("交付服务信息不存在 ！procInsId=", procInsId);
            throw new ServiceException(CommonConstants.FailMsg.DATA);
        }
        // 获取工作日天数配置值
        String josnStr = sysConstantsService
                .getConstantValByKey(ErpDeliveryServiceConstants.DELIVERY_SERVICE_WORK_DAYS);
        DeliveryServiceWorkDays workDays = null;
        if (StringUtils.isNotBlank(josnStr)) {
        	workDays = JSON.parseObject(josnStr, DeliveryServiceWorkDays.class);
        }
        // 物料制作跟踪任务应该完成的工作日天数
        int shouldMaterielDays = 0;
        if (workDays != null && workDays.getShouldMaterielDays() != null) {
            shouldMaterielDays = workDays.getShouldMaterielDays();
        }
        Date now = new Date();
        // 物料制作跟踪任务应该完成时间
        erpDeliveryService.setShouldMaterielTime(erpHolidaysService.getWorkDay(now, shouldMaterielDays));
        erpDeliveryServiceService.save(erpDeliveryService);

    }

    /**
     * 物料制作下单并进度同步
     * 
     * @param taskId
     * @param procInsId
     * @param file
     * @return
     */
    @Transactional(readOnly = false)
    public JSONObject materialContentSync(String taskId, String procInsId, ErpOrderMaterialCreation material) {
        Map<String, Object> vars = Maps.newHashMap();
        logger.info("物料制作下单并进度同步", taskId, procInsId, material);
        JSONObject resObject = new JSONObject();
        if ("arrived".equals(material.getStatus())) {
            this.workFlowService.completeFlow2(new String[] { JykFlowConstants.OPERATION_ADVISER }, taskId, procInsId,
                    "物料制作下单并进度同步", vars);
            resObject.put("message", "物料制作下单并进度已到店");
            resObject.put("result", true);
        } else {
            resObject.put("message", "物料制作下单并进度未到店");
            resObject.put("result", false);
        }
        return resObject;
    }

    /**
     * 物料制作下单并进度同步
     * 
     * @param taskId
     * @param procInsId
     * @param file
     * @return
     */
    @Transactional(readOnly = false)
    public void saveErpOrderFile(ErpOrderFile orderFile) {
        erpOrderFileService.save(orderFile);
    }

    /**
     * 物料制作跟踪
     * 
     * @param taskId
     * @param procInsId
     * @param file
     * @return
     */
    @Transactional(readOnly = false)
    public JSONObject materialMakingTracking(String taskId, String procInsId, ErpOrderMaterialCreation material,
            String channelType) {
        Map<String, Object> vars = Maps.newHashMap();
        JSONObject resObject = new JSONObject();
        if ("Pass".equals(channelType)) {
            // logger.info("物料制作下单并进度同步", taskId, procInsId, material); &&
            // "arrived".equals(material.getStatus())
            material.setStatus("arrived");
            material.setStatusName("已到店");
            this.materialCreationService.save(material);// 将文件设置为有效
            String variable = taskService.getVariable(taskId, DeliveryFlowConstant.SERVICE_TYPE)+"";
            if(DeliveryFlowConstant.SERVICE_TYPE_KE_CHANG_LAI.equals(variable)){
                vars.put(DeliveryFlowConstant.VISIT_TYPE,DeliveryFlowConstant.VISIT_TYPE_FMPS_M);
                vars.put(DeliveryFlowConstant.FIRST_SERVICE_M_SIGN,DeliveryFlowConstant.SERVICE_M_SIGN);
            }
            this.workFlowService.completeFlow2(new String[] { JykFlowConstants.OPERATION_ADVISER }, taskId, procInsId,
                    "物料制作跟踪", vars);
            Task task = this.taskService.createTaskQuery().processInstanceId(procInsId)
                    .taskDefinitionKeyLike("material_progress_sync%").singleResult();
            this.workFlowService.completeFlow2(new String[] { JykFlowConstants.OPERATION_ADVISER }, task.getId(),
                    procInsId, "物料制作下单并进度同步", vars);
            // 保存物料跟踪任务完成时间
            saveMaterielTime(procInsId);
            //升级二次赋值 查询历史
            actProInstService.deleteHiVarInstNameByProId(procInsId);
            resObject.put("message", "物料制作跟踪|物料已到店");
            resObject.put("result", true);
            return resObject;
        } else {
            resObject.put("message", "物料制作跟踪|物料未到店");
            resObject.put("result", true);
            return resObject;
        }
    }
    
    
    
    
    
    /**
     * 物料部署
     * 
     * @param taskId
     * @param procInsId
     * @param file
     * @return
     */
    @Transactional(readOnly = false)
    public JSONObject materialDeployServiceUpdate(String taskId, String procInsId, ErpOrderMaterialCreation material, String completeMaterialDeployVideoShop,String node) {
        logger.info("物料部署start=== taskid[],procInsId[],material[], completeMaterialDeployVideoShop[]", taskId, procInsId,material,
            completeMaterialDeployVideoShop);
        JSONObject resObject = new JSONObject();
        erpFlowFormService.saveErpFlowForm(taskId, procInsId, material.getId(), FlowFormConstant.COMPLETE_MATERIAL_DEPLOY_VIDEOSHOP,
            node, completeMaterialDeployVideoShop);
        Map<String, Object> vars = Maps.newHashMap();
        this.workFlowService.completeFlow2(new String[] { JykFlowConstants.OPERATION_ADVISER }, taskId, procInsId, "物料部署",
                vars);
//        saveShouldMaterielTime1(procInsId);
        resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
        return resObject;
    }
    /**
     * 保存 物料制作跟踪任务应该完成时间
     *
     * @param procInsId
     * @date 2018年6月8日
     * @author linqunzhi
     */
    private void saveShouldMaterielTime1(String procInsId) {
    	ErpDeliveryService erpDeliveryService = erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
        if (erpDeliveryService == null) {
            logger.error("交付服务信息不存在 ！procInsId=", procInsId);
            throw new ServiceException(CommonConstants.FailMsg.DATA);
        }
        Date now = new Date();
        // 物料制作跟踪任务完成时间
        erpDeliveryService.setVisitServiceTime(now);
        erpDeliveryServiceService.save(erpDeliveryService);
    }
    
    
    /**
     * 保存物料跟踪任务完成时间
     *
     * @param procInsId
     * @date 2018年6月8日
     * @author linqunzhi
     */
    private void saveMaterielTime(String procInsId) {
        // 物料制作跟踪任务完成时间
        ErpDeliveryService erpDeliveryService = erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
        if (erpDeliveryService == null) {
            logger.error("交付服务信息不存在 ！procInsId=", procInsId);
            throw new ServiceException(CommonConstants.FailMsg.DATA);
        }
        Date now = new Date();
        // 物料制作跟踪任务完成时间
        erpDeliveryService.setMaterielTime(now);
        erpDeliveryServiceService.save(erpDeliveryService);

    }

    /**
     * 电话预约商户（物料实施服务）
     * 
     * @param taskId
     * @param procInsId
     * @param file
     * @return
     */
    @Transactional(readOnly = false)
    public JSONObject savePhoneRevation(String taskId, String procInsId, ErpVisitServiceInfo paramObj) {
        JSONObject resObject = new JSONObject();
        // 插入一条上门服务流程
        paramObj.setProcInsId(procInsId);
        paramObj.setAuditStatus("0");
        User user = UserUtils.getUser();
        paramObj.setAuditUser(userDao.findManagerFlow(procInsId)==null?user.getId():userDao.findManagerFlow(procInsId).getId());
        erpVisitServiceInfoService.saveVisitService(paramObj);
        resObject.put("message", "电话预约商户保存成功");
        resObject.put("visitId", paramObj.getId());
        resObject.put("result", true);
        return resObject;
    }

    /**
     * 电话预约商户（物料实施服务）
     * 
     * @param taskId
     * @param procInsId
     * @param file
     * @return
     */
    @Transactional(readOnly = false)
    public JSONObject phoneRevation(String taskId, String procInsId, String visitId, String channelType,
            ErpVisitServiceInfo paramObj, String formAttrName, String node) {
        JSONObject resObject = new JSONObject();
        Map<String, Object> vars = Maps.newHashMap();
        User user = UserUtils.getUser();
        paramObj.setAuditUser(userDao.findManagerFlow(procInsId)==null?user.getId():userDao.findManagerFlow(procInsId).getId());
        // 插入一条上门服务流程
        paramObj.setProcInsId(procInsId);
        if ("Pass".equals(channelType)) {
        	paramObj.setAuditStatus(ErpVisitServiceConstants.AUDIT_STATUS_RESERVED);// 已预约
            erpVisitServiceInfoService.saveVisitService(paramObj);
            erpFlowFormService.saveErpFlowForm(taskId, procInsId, paramObj.getId(), formAttrName, node,
                    channelType);
            this.workFlowService.completeFlow2(new String[] { JykFlowConstants.OPERATION_ADVISER }, taskId,
                    procInsId, "电话预约商户", vars);
            resObject.put("message", "电话预约商户成功");
            resObject.put("result", true);
        } else {
            erpVisitServiceInfoService.saveVisitService(paramObj);
            erpFlowFormService.saveErpFlowForm(taskId, procInsId, paramObj.getId(), formAttrName, node, channelType);
            resObject.put("message", "请选择是才能完成当前流程");
            resObject.put("result", true);
        }
        return resObject;
    }

    /**
     * 电话预约商户（物料实施服务）
     * 
     * @param taskId
     * @param procInsId
     * @param file
     * @return
     */
    @Transactional(readOnly = false)
    public JSONObject homeServiceRevation(String taskId, String procInsId, String channelType, String visitId,
            ErpVisitServiceInfo paramObj, String formAttrName, String node) {
        JSONObject resObject = new JSONObject();
        User user = UserUtils.getUser();
        erpFlowFormService.saveErpFlowForm(taskId, procInsId, paramObj.getId(), formAttrName, node, channelType);
        if (StringUtils.isNotBlank(channelType) && channelType.indexOf("Pass") != -1) {
        	paramObj.setAuditUser(userDao.findManagerFlow(procInsId)==null?user.getId():userDao.findManagerFlow(procInsId).getId());
            paramObj.setProcInsId(procInsId);
            paramObj.setAuditStatus(ErpVisitServiceConstants.AUDIT_STATUS_SUBMITTED);// 待审核
            erpVisitServiceInfoService.saveVisitService(paramObj);
            Map<String, Object> vars = Maps.newHashMap();
            vars.put("reviewResult", "Pass");
            this.workFlowService.completeFlow2(new String[] { JykFlowConstants.OPERATION_MANAGER }, taskId, procInsId,
                    "上门服务提交申请", vars);// 下个节点运营经理
            resObject.put("message", "上门服务预约申请审批");
            resObject.put("result", true);
        } else {
        	Map<String, Object> vars = Maps.newHashMap();
            vars.put("reviewResult", "Return");
            this.workFlowService.completeFlow2(new String[] { JykFlowConstants.OPERATION_ADVISER }, taskId,
                    procInsId, "重新预约上门服务", vars);// 下个节点运营顾问
            resObject.put("message", "重新预约上门服务");
            resObject.put("result", true);
        }
        return resObject;
    }

    /**
     * 电话预约商户（物料实施服务）
     * 
     * @param taskId
     * @param procInsId
     * @param file
     * @return
     */
    @Transactional(readOnly = false)
    public JSONObject serviceRevation(String taskId, String procInsId, String channelType, String visitId,
            ErpVisitServiceInfo paramObj, String formAttrName, String node) {
        JSONObject resObject = new JSONObject();
        User user = UserUtils.getUser();
        if(!StringUtil.isBlank(taskId)){
        	erpFlowFormService.saveErpFlowForm(taskId, procInsId, paramObj.getId(), formAttrName, node, channelType);
        }
        if(userDao.findManagerFlow(procInsId)!=null){
        	paramObj.setAuditUser(userDao.findManagerFlow(procInsId)==null?user.getId():userDao.findManagerFlow(procInsId).getId());
        }
//        paramObj.setAuditUser(user.getId());
        paramObj.setProcInsId(procInsId);
        paramObj.setAuditStatus(ErpVisitServiceConstants.AUDIT_STATUS_SUBMITTED);// 待审核
        erpVisitServiceInfoService.saveVisitService(paramObj);
        Map<String, Object> vars = Maps.newHashMap();
        if(!StringUtil.isBlank(taskId)){
        	String formAttrName1 = "checkNotPassHomeService";
            String node1 = "visit_service_modify";
            Task task = this.taskService.createTaskQuery().processInstanceId(procInsId)
                    .taskDefinitionKeyLike("visit_service_modify%").singleResult();
            erpFlowFormService.saveErpFlowForm(task.getId(), procInsId, paramObj.getId(), formAttrName1, node1, "Pass");
            this.workFlowService.completeFlow2(new String[] { JykFlowConstants.OPERATION_MANAGER }, task.getId(),
                    procInsId, "售后上门服务提交申请", vars);// 下个节点运营经理
        }
        resObject.put("message", "上门服务预约申请审批");
        resObject.put("result", true);
        return resObject;
    }

    /**
     * 电话预约商户（物料实施服务）
     * 
     * @param taskId
     * @param procInsId
     * @param file
     * @return
     */
    @Transactional(readOnly = false)
    public JSONObject serviceRevationOther(String taskId, String procInsId, String channelType, String visitId,
            ErpVisitServiceInfo paramObj, String formAttrName, String node) {
        JSONObject resObject = new JSONObject();
        User user = UserUtils.getUser();
        erpFlowFormService.saveErpFlowForm(taskId, procInsId, paramObj.getId(), formAttrName, node, channelType);
        paramObj.setAuditUser(userDao.findManagerFlow(procInsId)==null?user.getId():userDao.findManagerFlow(procInsId).getId());
        paramObj.setProcInsId(procInsId);
        paramObj.setAuditStatus(ErpVisitServiceConstants.AUDIT_STATUS_SUBMITTED);// 待审核
        erpVisitServiceInfoService.updateVisitService(paramObj);
        Map<String, Object> vars = Maps.newHashMap();
        ErpShopInfo shop = erpShopInfoService.get(paramObj.getShopInfoId());
        // 指派运营经理
        ServiceOperation serviceOperation = serviceOperationService.getByServiceNo(shop.getAgentId() + "");
        if (serviceOperation != null) {
            String opsManagerId = StringUtils.isNotBlank(serviceOperation.getDefaultManagerId())
                    ? serviceOperation.getDefaultManagerId() : serviceOperation.getAlternativeManagerId();// 负责人
            this.erpOrderFlowUserService.insertOrderFlowUser(opsManagerId, paramObj.getId(), paramObj.getId(),
                    JykFlowConstants.OPERATION_MANAGER, procInsId);
        } else {
            List<User> operationAdviserUserList = userService
                    .getUserByRoleNameAndAgentId(RoleConstant.AGENT_OPERATION_MANAGER, shop.getAgentId());
            if (!CollectionUtils.isEmpty(operationAdviserUserList)) {
                String userId = operationAdviserUserList.get(0).getId();
                this.erpOrderFlowUserService.insertOrderFlowUser(userId, paramObj.getId(), paramObj.getId(),
                        JykFlowConstants.OPERATION_MANAGER, procInsId);
            }
        }
        this.workFlowService.completeFlow2(new String[] { JykFlowConstants.OPERATION_MANAGER }, taskId, procInsId,
                "修改(售后上门服务)", vars);// 下个节点运营顾问
        resObject.put("message", "已修改(售后上门服务)");
        resObject.put("result", true);
        return resObject;
    }

    /**
     * 审核上门服务预约申请
     * 
     * @param taskId
     * @param procInsId
     * @param file
     * @return
     */
    @Transactional(readOnly = false)
    public JSONObject checkHomeServiceRevation(String taskId, String procInsId, ErpVisitServiceInfo paramObj,
            String channelType, String visitId, String formAttrName, String node) {
        JSONObject resObject = new JSONObject();
        User user = UserUtils.getUser();
        paramObj.setAuditUser(userDao.findManagerFlow(procInsId)==null?user.getId():userDao.findManagerFlow(procInsId).getId());
        erpFlowFormService.saveErpFlowForm(taskId, procInsId, paramObj.getId(), formAttrName, node, channelType);
        if (StringUtils.isNotBlank(channelType) && channelType.indexOf("Pass") != -1) {
            paramObj.setAuditStatus(ErpVisitServiceConstants.AUDIT_STATUS_AUDITED);// 已审核通过
            erpVisitServiceInfoService.auditVisitService(paramObj);
            Map<String, Object> vars = Maps.newHashMap();
            vars.put("reviewResult", "Pass");
            this.workFlowService.completeFlow2(new String[] { JykFlowConstants.OPERATION_ADVISER }, taskId, procInsId,
                    "审核通过", vars);// 下个节点运营经理
            resObject.put("message", "上门服务预约申请审批");
            resObject.put("result", true);
        } else {
            paramObj.setAuditStatus(ErpVisitServiceConstants.AUDIT_STATUS_DISMISSED);// 已审核不通过
            erpVisitServiceInfoService.auditVisitService(paramObj);
            Map<String, Object> vars = Maps.newHashMap();
            vars.put("reviewResult", "Return");
            this.workFlowService.completeFlow2(new String[] { JykFlowConstants.OPERATION_ADVISER }, taskId, procInsId,
                    "审核不通过", vars);// 下个节点运营顾问
            resObject.put("message", "审核不通过");
            resObject.put("result", true);
        }
        return resObject;
    }

    /**
     * 电话预约商户（物料实施服务）
     * 
     * @param taskId
     * @param procInsId
     * @param file
     * @return
     */
    @Transactional(readOnly = false)
    public JSONObject checkNotPassHomeService(String taskId, String procInsId, ErpVisitServiceInfo paramObj,
            String channelType, String visitId, String formAttrName, String node) {
        JSONObject resObject = new JSONObject();
        User user = UserUtils.getUser();
        erpFlowFormService.saveErpFlowForm(taskId, procInsId, paramObj.getId(), formAttrName, node, channelType);
        if (StringUtils.isNotBlank(channelType) && channelType.indexOf("Pass") != -1) {
        	paramObj.setAuditUser(userDao.findManagerFlow(procInsId)==null?user.getId():userDao.findManagerFlow(procInsId).getId());
            paramObj.setProcInsId(procInsId);
            paramObj.setAuditStatus(ErpVisitServiceConstants.AUDIT_STATUS_SUBMITTED);// 待审核
            erpVisitServiceInfoService.saveVisitService(paramObj);
            Map<String, Object> vars = Maps.newHashMap();
            vars.put("reviewResult", "Pass");
            this.workFlowService.completeFlow2(new String[] { JykFlowConstants.OPERATION_ADVISER }, taskId, procInsId,
                    "二次提交申请", vars);// 下个节点运营经理
            resObject.put("message", "上门服务预约申请审批");
            resObject.put("result", true);
        } else {
        	paramObj.setAuditUser(userDao.findManagerFlow(procInsId)==null?user.getId():userDao.findManagerFlow(procInsId).getId());
            paramObj.setProcInsId(procInsId);
            paramObj.setAuditStatus(ErpVisitServiceConstants.AUDIT_STATUS_RESERVED);// 已预约
            erpVisitServiceInfoService.saveVisitService(paramObj);
            Map<String, Object> vars = Maps.newHashMap();
            vars.put("reviewResult", "Return");
            this.workFlowService.completeFlow2(new String[] { JykFlowConstants.OPERATION_ADVISER }, taskId, procInsId,
                    "二次重新预约上门服务", vars);// 下个节点运营顾问
            resObject.put("message", "重新预约上门服务");
            resObject.put("result", true);
        }
        return resObject;
    }

    /**
     * 电话预约商户（物料实施服务）
     * 
     * @param taskId
     * @param procInsId
     * @param file
     * @return
     */
    @Transactional(readOnly = false)
    public JSONObject checkNotPassHome(String taskId, String procInsId, ErpVisitServiceInfo paramObj,
            String channelType, String visitId, String formAttrName, String node) {
        JSONObject resObject = new JSONObject();
        User user = UserUtils.getUser();
        erpFlowFormService.saveErpFlowForm(taskId, procInsId, paramObj.getId(), formAttrName, node, channelType);
        erpOrderFlowUserService.insertOrderFlowUser(user.getId(), taskId, visitId, JykFlowConstants.OPERATION_ADVISER,
                procInsId);
        if (StringUtils.isNotBlank(channelType) && channelType.indexOf("Pass") != -1) {
            Map<String, Object> vars = Maps.newHashMap();
            vars.put("reviewResult", "Pass");
            this.workFlowService.completeFlow2(new String[] { JykFlowConstants.OPERATION_ADVISER }, taskId, procInsId,
                    "完成上门服务(上门服务提醒)", vars);// 下个节点运营经理
            resObject.put("message", "完成上门服务");
            resObject.put("result", true);
        } else {
        	paramObj.setAuditUser(userDao.findManagerFlow(procInsId)==null?user.getId():userDao.findManagerFlow(procInsId).getId());
            paramObj.setProcInsId(procInsId);
            paramObj.setAuditStatus(ErpVisitServiceConstants.AUDIT_STATUS_SUBMITTED);// 待审核
            erpVisitServiceInfoService.saveVisitService(paramObj);
            Map<String, Object> vars = Maps.newHashMap();
            vars.put("reviewResult", "Return");
            this.workFlowService.completeFlow2(new String[] { JykFlowConstants.OPERATION_ADVISER }, taskId, procInsId,
                    "二次重新预约上门服务", vars);// 下个节点运营顾问
            resObject.put("message", "修改上门服务申请");
            resObject.put("result", true);
        }
        return resObject;
    }

    /**
     * 电话预约商户（物料实施服务）
     * 
     * @param taskId
     * @param procInsId
     * @param file
     * @return
     */
    @Transactional(readOnly = false)
    public JSONObject remindDoorService(String taskId, String procInsId, String channelType, String visitId,
            String formAttrName, String node, ErpVisitServiceInfo paramObj) {
        JSONObject resObject = new JSONObject();
        User user = UserUtils.getUser();
        erpFlowFormService.saveErpFlowForm(taskId, procInsId, paramObj.getId(), formAttrName, node, channelType);
        if (StringUtils.isNotBlank(channelType) && channelType.indexOf("Pass") != -1) {
            Map<String, Object> vars = Maps.newHashMap();
            vars.put("reviewResult", "Pass");

            this.workFlowService.completeFlow2(new String[] { JykFlowConstants.OPERATION_ADVISER }, taskId, procInsId,
                    "完成上门服务(上门服务提醒)", vars);// 下个节点运营经理
            resObject.put("message", "完成上门服务");
            resObject.put("result", true);
        } else {
        	paramObj.setAuditUser(userDao.findManagerFlow(procInsId)==null?user.getId():userDao.findManagerFlow(procInsId).getId());
            paramObj.setProcInsId(procInsId);
            paramObj.setAuditStatus(ErpVisitServiceConstants.AUDIT_STATUS_SUBMITTED);// 待审核
            erpVisitServiceInfoService.updateVisitService(paramObj);
            Map<String, Object> vars = Maps.newHashMap();
            vars.put("reviewResult", "Return");
            this.workFlowService.completeFlow2(new String[] { JykFlowConstants.OPERATION_ADVISER }, taskId, procInsId,
                    "修改(上门服务提醒)", vars);// 下个节点运营顾问
            resObject.put("message", "已修改(上门服务提醒)");
            resObject.put("result", true);
        }
        return resObject;
    }

    /**
     * 电话预约商户（物料实施服务）
     * 
     * @param taskId
     * @param procInsId
     * @param file
     * @return
     */
    @Transactional(readOnly = false)
    public JSONObject afterRemindDoorService(String taskId, String procInsId, String channelType, String visitId,
            String formAttrName, String node, ErpVisitServiceInfo paramObj) {
        JSONObject resObject = new JSONObject();
        User user = UserUtils.getUser();
        if (StringUtils.isNotBlank(channelType) && channelType.indexOf("Pass") != -1) {
            Map<String, Object> vars = Maps.newHashMap();
            vars.put("reviewResult", "Pass");
            this.workFlowService.completeFlow2(new String[] { JykFlowConstants.OPERATION_ADVISER }, taskId, procInsId,
                    "完成上门服务(上门服务提醒)", vars);// 下个节点运营经理
            resObject.put("message", "完成上门服务");
            resObject.put("result", true);
        } else {
        	paramObj.setAuditUser(userDao.findManagerFlow(procInsId)==null?user.getId():userDao.findManagerFlow(procInsId).getId());
            paramObj.setProcInsId(procInsId);
            paramObj.setAuditStatus(ErpVisitServiceConstants.AUDIT_STATUS_SUBMITTED);// 待审核
            erpVisitServiceInfoService.updateVisitService(paramObj);
            Map<String, Object> vars = Maps.newHashMap();
            vars.put("reviewResult", "Return");
            resObject.put("message", "已修改(上门服务提醒)");
            resObject.put("result", true);
        }
        return resObject;
    }

    /**
     * 电话预约商户（物料实施服务）
     * 
     * @param taskId
     * @param procInsId
     * @param file
     * @return
     */
    @Transactional(readOnly = false)
    public JSONObject completeDoorService(String taskId, String procInsId, ErpVisitServiceInfo paramObj, String visitId,
            String formAttrName, String node, List<ErpOrderFile> fileList) {
        JSONObject resObject = new JSONObject();
        String filePath = "";
        for (ErpOrderFile ef : fileList) {
            ef.setDelFlag("0");
            filePath += ef.getFilePath() + ";";
            this.erpOrderFileService.save(ef);
        }
        User user = UserUtils.getUser();
        paramObj.setReceivingReport(filePath);
        paramObj.setAuditUser(userDao.findManagerFlow(procInsId)==null?user.getId():userDao.findManagerFlow(procInsId).getId());
        paramObj.setAuditStatus(ErpVisitServiceConstants.AUDIT_STATUS_COMPLETED);
        erpFlowFormService.saveErpFlowForm(taskId, procInsId, paramObj.getId(), formAttrName, node,
                ErpVisitServiceConstants.AUDIT_STATUS_COMPLETED);
        erpVisitServiceInfoService.completedVisitService(paramObj);
        Map<String, Object> vars = Maps.newHashMap();

        Task task = this.taskService.createTaskQuery().processInstanceId(procInsId)
                .taskDefinitionKeyLike("train_service_record%").singleResult();
        if (task != null) {
            this.workFlowService.completeFlow2(new String[] { JykFlowConstants.OPERATION_ADVISER }, task.getId(),
                    procInsId, "培训备忘", vars);
        }
        this.workFlowService.completeFlow2(new String[] { JykFlowConstants.OPERATION_ADVISER }, taskId, procInsId,
                "上门服务完成", vars);// 下个节点运营经理
        if (DeliveryFlowConstant.VISIT_SERVICE_COMPLETE_FIRST.equals(node)) {
            // 保存 上门服务完成（首次营销策划服务）
            saveVisitServiceTime(procInsId);
        }

        resObject.put("message", "完成上门服务");
        resObject.put("result", true);
        return resObject;
    }

    /**
     * 保存 上门服务完成（首次营销策划服务）时间
     *
     * @param procInsId
     * @date 2018年6月8日
     * @author linqunzhi
     */
    @Transactional(readOnly = false)
    public  void saveVisitServiceTime2(String procInsId) {
        ErpDeliveryService erpDeliveryService = erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
        if (erpDeliveryService == null) {
            logger.error("交付服务信息不存在 ！procInsId=", procInsId);
            throw new ServiceException(CommonConstants.FailMsg.DATA);
        }
        Date now = new Date();
        if(StringUtil.isBlank(erpDeliveryService.getServiceType())){
        	erpDeliveryService.setVisitServiceTime(now);
            erpDeliveryServiceService.save(erpDeliveryService);
        }
    }
    
    /**
     * 保存 上门服务完成（首次营销策划服务）时间
     *
     * @param procInsId
     * @date 2018年6月8日
     * @author linqunzhi
     */
    @Transactional(readOnly = false)
    public  void saveVisitServiceTime(String procInsId) {
        ErpDeliveryService erpDeliveryService = erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
        if (erpDeliveryService == null) {
            logger.error("交付服务信息不存在 ！procInsId=", procInsId);
            throw new ServiceException(CommonConstants.FailMsg.DATA);
        }
        Date now = new Date();
    	erpDeliveryService.setVisitServiceTime(now);
        erpDeliveryServiceService.save(erpDeliveryService);

    }

    /**
     * 查询流程下各节点操作记录
     * 
     * @param taskId
     * @param procInsId
     * @param file
     * @return
     */
    @Transactional(readOnly = false)
    public JSONObject findProcessByProcId(String procInsId) {
        JSONObject resObject = new JSONObject();
        if (!StringUtil.isBlank(procInsId)) {
            List<Map<String, Object>> assignees = jykFlowDao.findProcessByProcId(procInsId);
            resObject.put("message", "查询成功");
            resObject.put("result", assignees);
        } else {
            resObject.put("message", "请传入流程ID");
            resObject.put("result", null);
        }
        return resObject;
    }

    /**
     * 查询流程下各节点操作记录
     * 
     * @param taskId
     * @param procInsId
     * @param file
     * @return
     */
    @Transactional(readOnly = false)
    public JSONObject saveVisitService(ErpVisitServiceInfo paramObj) {
        JSONObject resObject = new JSONObject();
        List<ErpVisitServiceItemRecord> ll=paramObj.getItemRecords();
    	for(ErpVisitServiceItemRecord e:ll){
    		e.setServiceItemAttendees("无");
    	}
        User user = UserUtils.getUser();
        ErpShopInfo shop = erpShopInfoService.get(paramObj.getShopInfoId());
        boolean isrun = false;
        String procInsId = "";
        // 指派运营经理
        ServiceOperation serviceOperation = serviceOperationService.getByServiceNo(shop.getAgentId() + "");
        // 获取运营经理
        if (serviceOperation != null) {
            String opsManagerId = StringUtils.isNotBlank(serviceOperation.getDefaultManagerId())
                    ? serviceOperation.getDefaultManagerId() : serviceOperation.getAlternativeManagerId();// 负责人
            paramObj.setAuditUser(userDao.findManagerFlow(procInsId)==null?opsManagerId:userDao.findManagerFlow(procInsId).getId());  
            paramObj.setServiceStartTime(new Date());
            this.erpVisitServiceInfoService.saveVisitService(paramObj);
            procInsId = workFlow3p25Service.startVisitServiceFlow(paramObj, null);
            this.erpOrderFlowUserService.insertOrderFlowUser(opsManagerId, procInsId, paramObj.getId(),
                    JykFlowConstants.OPERATION_MANAGER, procInsId);
            isrun = true;
            resObject.put("userManager", UserUtils.get(opsManagerId));
        } else {
            List<User> operationAdviserUserList = userService
                    .getUserByRoleNameAndAgentId(RoleConstant.AGENT_OPERATION_MANAGER, shop.getAgentId());
            if (!CollectionUtils.isEmpty(operationAdviserUserList)) {
            	String userId = operationAdviserUserList.get(0).getId();
            	paramObj.setAuditUser(userDao.findManagerFlow(procInsId)==null?userId:userDao.findManagerFlow(procInsId).getId());
            	paramObj.setServiceStartTime(new Date());
            	this.erpVisitServiceInfoService.saveVisitService(paramObj);
                procInsId = workFlow3p25Service.startVisitServiceFlow(paramObj, null);
                this.erpOrderFlowUserService.insertOrderFlowUser(userId, procInsId, paramObj.getId(),
                        JykFlowConstants.OPERATION_MANAGER, procInsId);
                isrun = true;
                resObject.put("userManager", UserUtils.get(userId));
            }
        }
        if (!isrun) {
            resObject.put("message", "当前" + shop.getId() + "没有分配运营经理");
            resObject.put("result", false);
        } else {
            String formAttrName = "homeServiceRevation";
            String node = "visit_service_apply";
            Map<String, Object> vars = Maps.newHashMap();
            Task task = this.taskService.createTaskQuery().processInstanceId(procInsId)
                    .taskDefinitionKeyLike("visit_service_apply%").singleResult();
            erpFlowFormService.saveErpFlowForm(task.getId(), procInsId, paramObj.getId(), formAttrName, node, "Pass");
            this.workFlowService.completeFlow2(new String[] { JykFlowConstants.OPERATION_MANAGER }, task.getId(),
                    procInsId, "售后上门服务提交申请", vars);// 下个节点运营经理
            resObject.put("message", "新增成功");
            resObject.put("result", true);
        }
        return resObject;
    }

    /**
     * 查询流程下各节点操作记录
     * 
     * @param taskId
     * @param procInsId
     * @param file
     * @return
     */
    @Transactional(readOnly = false)
    public void changeRoleUser(String procInsId, String userId, String roleName) {
        actTaskService.changeRoleUser(procInsId, userId, roleName);
        erpOrderFlowUserService.changeRoleUser(procInsId, userId, roleName);
    }

    /**
     * 查询流程下各节点操作记录
     * 
     * @param taskId
     * @param procInsId
     * @param file
     * @return
     */
    @Transactional(readOnly = false)
    public List<Map<String, Object>> getContentList(String taskId, String procInsId) {
    	List<Map<String, Object>> list = jykFlowDao.getContentList(taskId, procInsId);
        return list;
    }

    /**
     * 查询流程下各节点操作记录
     * 
     * @param taskId
     * @param procInsId
     * @param file
     * @return
     */
    @Transactional(readOnly = false)
    public List<Map<String, Object>> getContentListDetail(String procInsId, String name) {
    	List<Map<String, Object>> list = jykFlowDao.getContentListDetail(procInsId, name);
        return list;
    }

    /**
     * 业务定义：查询商户培训类型服务项记录，查询范围=首次营销策划上门服务+物料上门服务
     * 
     * @date 2018年5月31日
     * @author R/Q
     */
    public JSONObject queryTrainItemRecordOther(String taskId, String procInsId) {
        JSONObject resObject = new JSONObject();
        ErpDeliveryService erpDeliveryService = erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
        ErpOrderOriginalInfo orderOriginalInfo = erpOrderOriginalInfoService.get(erpDeliveryService.getOrderId());
        ErpShopInfo shop = erpShopInfoService.findListByZhangbeiId(orderOriginalInfo.getShopId());
        if ("Y".equals(orderOriginalInfo.getIsNewShop())) {
            resObject.put("shopType", "新商户");
        } else {
            resObject.put("shopType", "旧商户");
        }
        String serviceType = (String) taskService.getVariable(taskId, FlowConstant.SERVICETYPE);
        if (DeliveryFlowConstant.SERVICE_TYPE_KE_CHANG_LAI.equals(serviceType)) {
            resObject.put("serviceItems", "首次营销策划服务");
            resObject.put("orderGoodType", "常客来");
        } else if (DeliveryFlowConstant.SERVICE_TYPE_JU_YIN_KE.equals(serviceType)) {
            resObject.put("serviceItems", "聚引客交付服务");
            resObject.put("orderGoodType", "聚引客");
        } else {
            resObject.put("serviceItems", "未知");
            resObject.put("orderGoodType", "未知");
        }
        List<ErpVisitServiceItemRecord> visitList = erpVisitServiceInfoService.queryTrainItemRecord(shop.getId());
        List<ErpVisitServiceItem> serviceItem = erpVisitServiceInfoService.queryTrainItem();
        resObject.put("orderOriginalInfo", orderOriginalInfo);
        resObject.put("serviceItem", serviceItem);
        resObject.put("visitList", visitList);
        return resObject;
    }
}
