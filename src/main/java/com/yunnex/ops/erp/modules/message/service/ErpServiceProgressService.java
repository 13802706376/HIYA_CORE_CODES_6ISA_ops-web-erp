package com.yunnex.ops.erp.modules.message.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.yunnex.ops.erp.common.constant.CommonConstants;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.modules.act.service.ActTaskService;
import com.yunnex.ops.erp.modules.message.constant.ServiceProgressTemplateConstants;
import com.yunnex.ops.erp.modules.message.dao.ErpServiceProgressDao;
import com.yunnex.ops.erp.modules.message.entity.ErpServiceProgress;
import com.yunnex.ops.erp.modules.message.entity.ErpServiceProgressTemplate;
import com.yunnex.ops.erp.modules.message.extraModel.ServiceProgressExtra;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.order.service.ErpOrderOriginalInfoService;
import com.yunnex.ops.erp.modules.visit.constants.VisitServiceItemConstants;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitServiceInfo;
import com.yunnex.ops.erp.modules.visit.service.ErpVisitServiceInfoService;
import com.yunnex.ops.erp.modules.workflow.acceptance.entity.ErpServiceAcceptance;
import com.yunnex.ops.erp.modules.workflow.acceptance.service.ErpServiceAcceptanceService;
import com.yunnex.ops.erp.modules.workflow.enums.FlowServiceType;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowConstant;

/**
 * 服务进度表Service
 * 
 * @author yunnex
 * @version 2018-07-04
 */
@Service
public class ErpServiceProgressService extends CrudService<ErpServiceProgressDao, ErpServiceProgress> {

    /** 临时 引流服务流程 版本号 */
    private static final int SPLIT_PROCESS_VERSION = 301;
    /** 临时其他服务流程 版本号 */
    private static final int OTHER_PROCESS_VERSION = 101;

    @Autowired
    private ErpServiceProgressTemplateService serviceProgressTemplateService;
    @Autowired
    private ActTaskService actTaskService;
    @Autowired
    private ErpOrderOriginalInfoService orderService;
    @Autowired
    private ErpServiceAcceptanceService serviceAcceptanceService;
    @Autowired
    @Lazy
    private ErpVisitServiceInfoService visitServiceInfoService;

    @Override
    public ErpServiceProgress get(String id) {
        return super.get(id);
    }

    @Override
    public List<ErpServiceProgress> findList(ErpServiceProgress erpServiceSchedule) {
        return super.findList(erpServiceSchedule);
    }

    @Override
    public Page<ErpServiceProgress> findPage(Page<ErpServiceProgress> page, ErpServiceProgress erpServiceSchedule) {
        return super.findPage(page, erpServiceSchedule);
    }

    @Override
    @Transactional(readOnly = false)
    public void save(ErpServiceProgress erpServiceSchedule) {
        super.save(erpServiceSchedule);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(ErpServiceProgress erpServiceSchedule) {
        super.delete(erpServiceSchedule);
    }

    /**
     * 创建未开始服务进度
     *
     * @param procInsId
     * @param serviceType
     * @param orderId
     * @date 2018年7月10日
     * @author linqunzhi
     */
    @Transactional(readOnly = false)
    public void createNoBeginSchedule(String agentType,String procInsId, String serviceType, String orderId) {
        int processVersion = getDefaultProcessVersion(serviceType);
        createNoBeginSchedule(agentType,procInsId, serviceType, processVersion, orderId);
    }

    /**
     * 创建未开始服务进度
     *
     * @param procInsId
     * @param serviceTypeList
     * @param orderId
     * @date 2018年8月30日
     * @author linqunzhi
     */
    @Transactional
    public void createNoBeginSchedule(String agentType,String procInsId, List<String> serviceTypeList, String orderId) {
        if (CollectionUtils.isNotEmpty(serviceTypeList)) {
            for (String serviceType : serviceTypeList) {
                createNoBeginSchedule(agentType,procInsId, serviceType, orderId);
            }
        }
    }

    private static int getDefaultProcessVersion(String serviceType) {
        if (FlowConstant.ServiceType.SPLIT_JU_YIN_KE.equals(serviceType)) {
            return SPLIT_PROCESS_VERSION;
        }
        return OTHER_PROCESS_VERSION;
    }

    /**
     * 创建未开始服务进度
     *
     * @param proc_ins_id
     * @param service_type
     * @return
     * @date 2018年7月9日
     * @author linqunzhi
     */
    @Transactional(readOnly = false)
    private void createNoBeginSchedule(String agentType,String procInsId, String serviceType, int processVersion, String orderId) {
        logger.info("createNoBeginSchedule start | procInsId={}|serviceType={}|processVersion={}|orderId={}", procInsId, serviceType, processVersion,
                        orderId);
        ErpOrderOriginalInfo order = orderService.get(orderId);
        // 获取掌贝id
        String zhangbeiId = orderId == null ? null : order.getShopId();
        ErpServiceProgressTemplate template = new ErpServiceProgressTemplate();
        template.setServiceType(serviceType);
        template.setStatus(ServiceProgressTemplateConstants.STATUS_NO_BEGIN);
        template.setProcessVersion(processVersion);
        // 获取未启动服务进度模板
        List<ErpServiceProgressTemplate> list = serviceProgressTemplateService.findList(template);
        int size = list == null ? 0 : list.size();
        logger.info("模板size={}", size);
        if (size > 0) {
            ErpServiceProgress serviceSchedule = null;
            for (ErpServiceProgressTemplate scheduleTemplate : list) {
            	//交付流程为服务商的，不需要创建”物料部署服务“和“物料制作服务”进度
            	if("Agent".equals(agentType)&&("MaterialProduction".equals(scheduleTemplate.getType())||"MaterialDeployment".equals(scheduleTemplate.getType()))) {
            		continue;
            	}
                String templateId = scheduleTemplate.getId();
                serviceSchedule = new ErpServiceProgress();
                serviceSchedule.setProcInsId(procInsId);
                serviceSchedule.setTemplateId(templateId);
                serviceSchedule.setZhangbeiId(zhangbeiId);
                this.save(serviceSchedule);
                // 如果服务进度是 服务启动，则直接指向begin状态
                if (ServiceProgressTemplateConstants.Type.SERVICE_START.equals(scheduleTemplate.getType())) {
                    this.updateTemplateIdByManual(procInsId, serviceType, ServiceProgressTemplateConstants.Type.SERVICE_START,
                                    ServiceProgressTemplateConstants.STATUS_BEGIN);
                }
            }
        }
        logger.info("createNoBeginSchedule end");
    }


    /**
     * 根据流程id 更改服务进度对应模板id
     *
     * @param procInsId
     * @param serviceType
     * @param taskDefinitionKey
     * @date 2018年7月10日
     * @author linqunzhi
     */
    @Transactional(readOnly = false)
    public void updateTemplateIdByProcInsId(String procInsId, String serviceType, String taskDefinitionKey) {
        int processVersion = getDefaultProcessVersion(serviceType);
        this.updateTemplateIdByProcInsId(procInsId, serviceType, taskDefinitionKey, processVersion);
    }

    /**
     * 根据流程id 更改服务进度对应模板id
     *
     * @param procInsId
     * @param serviceTypeList
     * @param taskDefinitionKey
     * @date 2018年8月28日
     * @author linqunzhi
     */
    @Transactional(readOnly = false)
    public void updateTemplateIdByProcInsId(String procInsId, List<String> serviceTypeList, String taskDefinitionKey) {
        if (CollectionUtils.isNotEmpty(serviceTypeList)) {
            for (String serviceType : serviceTypeList) {
                updateTemplateIdByProcInsId(procInsId, serviceType, taskDefinitionKey);
            }
        }
    }

    /**
     * 根据流程id 更改服务进度对应模板id
     *
     * @param procInsId
     * @date 2018年7月9日
     * @author linqunzhi
     */
    @Transactional(readOnly = false)
    public void updateTemplateIdByProcInsId(String procInsId, String serviceType, String taskDefinitionKey, int processVersion) {
        logger.info("updateTemplateIdByProcInsId start | procInsId={}|serviceType={}|taskDefinitionKey={}|processVersion={}", procInsId, serviceType,
                        taskDefinitionKey, processVersion);
        ErpServiceProgressTemplate template = new ErpServiceProgressTemplate();
        template.setServiceType(serviceType);
        template.setTaskDefinitionKeys(taskDefinitionKey);
        template.setProcessVersion(processVersion);
        // 获取服务进度模板
        List<ErpServiceProgressTemplate> list = serviceProgressTemplateService.findList(template);
        int size = list == null ? 0 : list.size();
        logger.info("模板size={}", size);
        if (size > 0) {
            for (ErpServiceProgressTemplate scheduleTemplate : list) {
                // 是否需要进行更新
                boolean needUpdate = needUpdate(procInsId, taskDefinitionKey, scheduleTemplate);
                if (needUpdate) {
                    // 服务进度类型
                    String type = scheduleTemplate.getType();
                    // 进行修改服务进度
                    updateTemplate(scheduleTemplate, procInsId, serviceType, type);
                }
            }
        }
        logger.info("updateTemplateIdByProcInsId end");
    }

    /**
     * 获取本次是否需要进行更新
     *
     * @param procInsId
     * @param scheduleTemplate
     * @param taskDefinitionKey
     * @return
     * @date 2018年7月9日
     * @author linqunzhi
     */
    private boolean needUpdate(String procInsId, String taskDefinitionKey, ErpServiceProgressTemplate scheduleTemplate) {
        String definitionKeys = scheduleTemplate.getTaskDefinitionKeys();
        if (StringUtils.isBlank(definitionKeys)) {
            return false;
        }
        // 获取节点数组
        String[] definitionKeyArr = definitionKeys.split(CommonConstants.Sign.COMMA);
        // 如果只有一个任务节点，直接返回true
        if (definitionKeyArr.length == 1) {
            return true;
        }
        String taskKeyType = scheduleTemplate.getTaskKeyType();
        // 如果任务触发条件为 Or ,直接返回true
        if (ServiceProgressTemplateConstants.TASK_KEY_TYPE_OR.equals(taskKeyType)) {
            return true;
        }
        // 触发条件为 And，循环遍历任务节点，如果都已完成，则返回true
        boolean allTaskFinish = true;
        for (String key : definitionKeyArr) {
            if (key.equals(taskDefinitionKey)) {
                continue;
            }
            // 判断任务节点是否已完成
            boolean taskIsFinish = actTaskService.taskIsFinish(procInsId, key);
            if (!taskIsFinish) {
                allTaskFinish = false;
                // 退出循环
                break;
            }
        }
        return allTaskFinish;
    }


    /**
     * 手动触发 更改服务进度对应模板id
     *
     * @param procInsId
     * @param serviceType
     * @param type
     * @date 2018年7月19日
     * @author linqunzhi
     */
    @Transactional(readOnly = false)
    public void updateTemplateIdByManual(String procInsId, String serviceType, String type, String status) {
        logger.info("updateTemplateIdByManual start | procInsId={}|serviceType={}|type={}|status={}", procInsId, serviceType, type, status);
        int processVersion = getDefaultProcessVersion(serviceType);
        updateTemplateIdByManual(procInsId, serviceType, type, status, processVersion);
        logger.info("updateTemplateIdByManual end");
    }

    private void updateTemplateIdByManual(String procInsId, String serviceType, String type, String status, int processVersion) {
        // 获取服务进度模板
        ErpServiceProgressTemplate scheduleTemplate = serviceProgressTemplateService.getOnly(serviceType, type, status, processVersion);
        // 进行修改服务进度
        updateTemplate(scheduleTemplate, procInsId, serviceType, type);
    }

    /**
     * 修改服务进度
     *
     * @param scheduleTemplate
     * @param procInsId
     * @param serviceType
     * @param type
     * @date 2018年8月9日
     * @author linqunzhi
     * 
     */
    private void updateTemplate(ErpServiceProgressTemplate scheduleTemplate, String procInsId, String serviceType, String type) {
        if (scheduleTemplate != null) {
            ErpServiceProgress serviceProgress = dao.getByProcInsIdAndType(procInsId, serviceType, type);
            if (serviceProgress == null) {
                logger.info("服务进度不存在！procInsId={}|type={}", procInsId, type);
                return;
            }
            String oldTemplateId = serviceProgress.getTemplateId();
            // 获取旧模板数据
            ErpServiceProgressTemplate oldTemplate = serviceProgressTemplateService.get(oldTemplateId);
            if (oldTemplate == null) {
                logger.info("服务进度不存在模板数据！templateId={}", oldTemplateId);
                return;
            }
            // 旧模板状态
            String oldStatus = oldTemplate.getStatus();
            if (ServiceProgressTemplateConstants.STATUS_END.equals(oldStatus)) {
                logger.info("模板状态为结束，不需要再次修改  templateId={}", oldTemplateId);
                return;
            }
            serviceProgress.setTemplateId(scheduleTemplate.getId());
            // 如果启动时间为空，则设置启动时间（存在未启动就结束的情况，所以不管begin 还是 end 进来 都设置启动时间）
            if (serviceProgress.getStartTime() == null) {
                serviceProgress.setStartTime(new Date());
            }
            this.save(serviceProgress);
        }
    }

    /**
     * 业务定义：保存流程进度
     * 
     * @date 2018年7月23日
     * @author R/Q
     */
    @Transactional(readOnly = false)
    public void saveProgress(String paramJson) {
        if (StringUtils.isBlank(paramJson)) {
            throw new ServiceException("前台传递参数错误");
        }
        List<ErpServiceProgressTemplate> paramList = JSON.parseArray(StringEscapeUtils.unescapeHtml4(paramJson), ErpServiceProgressTemplate.class);
        if (CollectionUtils.isNotEmpty(paramList)) {
            paramList.stream().forEach(paramObj -> this.updateTemplateIdByManual(paramObj.getProcInsId(), paramObj.getServiceType(),
                            paramObj.getType(), paramObj.getStatus()));
        }
    }

    /**
     * 根据流程id和type删除流程进度
     *
     * @param procInsId
     * @param serviceType
     * @param type
     * @date 2018年8月10日
     * @author linqunzhi
     */
    @Transactional(readOnly = false)
    public void deleteByProcInsIdType(String procInsId, String serviceType, String type) {
        logger.info("deleteByProcInsIdType start | procInsId={}|serviceType={}|type={}", procInsId, serviceType, type);
        ErpServiceProgress progress = dao.getByProcInsIdAndType(procInsId, serviceType, type);
        this.delete(progress);
        logger.info("deleteByProcInsIdType end");
    }

    /**
     * 根据流程id 和 服务进度类型 获取服务进度
     *
     * @param procInsId
     * @param serviceType
     * @param type
     * @return
     * @date 2018年8月13日
     * @author linqunzhi
     */
    public ErpServiceProgress getByProcInsIdAndType(String procInsId, String serviceType, String type) {
        logger.info("getByProcInsIdAndType start | procInsId={}|serviceType={}|type={}", procInsId,serviceType, type);
        ErpServiceProgress result = dao.getByProcInsIdAndType(procInsId, serviceType, type);
        logger.info("getByProcInsIdAndType end");
        return result;
    }

    /**
     * 流程重启
     * 
     *
     * @param serviceTypeList 服务类型集合
     * @param newProcInsId 新流程id
     * @param oldProcInsId 老流程id
     * @param orderId 订单id
     * @date 2018年8月22日
     * @author linqunzhi
     */
    @Transactional(readOnly = false)
    public void restartFlow(String newProcInsId, String oldProcInsId) {
        logger.info("restartFlow start");
        logger.info("param | newProcInsId={}|oldProcInsId={}", newProcInsId, oldProcInsId);
        ErpServiceProgress progress = new ErpServiceProgress();
        progress.setProcInsId(oldProcInsId);
        List<ErpServiceProgress> oldList = dao.findList(progress);
        int oldSize = oldList == null ? 0 : oldList.size();
        logger.info("oldList.size = {}", oldSize);
        // 删除老数据
        if (oldSize > 0) {
            for (ErpServiceProgress serviceProgress : oldList) {
                dao.delete(serviceProgress);
            }
        }
        logger.info("restartFlow end");
    }

    /**
     * 首次营销基础版升级
     *
     * @param newProcInsId
     * @param oldProcInsId
     * @param isAddZhuiHui 是否新加 智慧餐厅服务
     * @param isMenuConfigNode 是否开启智慧餐厅服务节点
     * @date 2018年8月23日
     * @author linqunzhi
     */
    public void upgradeFmpsBasic(String newProcInsId, String oldProcInsId, String orderId, boolean isAddZhuiHui, boolean isMenuConfigNode,
                    boolean isServiceStartEnd) {
        logger.info("upgradeFmpsBasic start | newProcInsId={}|oldProcInsId={}|isAddZhuiHui={}|isMenuConfigNode={}", newProcInsId, oldProcInsId,
                        isAddZhuiHui, isMenuConfigNode);
        managerUpgradeFmpsProgress(newProcInsId, oldProcInsId, OTHER_PROCESS_VERSION);
        managerUpgradeZhiHuiProgress(newProcInsId, oldProcInsId, isAddZhuiHui, isMenuConfigNode, isServiceStartEnd, OTHER_PROCESS_VERSION, orderId);
        logger.info("upgradeFmpsBasic end");
    }

    /**
     * 管理 智慧餐厅服务 进度
     *
     * @param newProcInsId
     * @param oldProcInsId
     * @param isAddZhiHui
     * @param isMenuConfigNode
     * @param isServiceStartEnd 服务启动是否结束状态
     * @date 2018年8月23日
     * @author linqunzhi
     * @param otherProcessVersion
     */
    private void managerUpgradeZhiHuiProgress(String newProcInsId, String oldProcInsId, boolean isAddZhiHui, boolean isMenuConfigNode,
                    boolean isServiceStartEnd, int processVersion, String orderId) {
        // 是否新增 智慧餐厅服务
        if (isAddZhiHui) {
            if (isMenuConfigNode) {
                // end 服务启动
                updateTemplateIdByManual(newProcInsId, FlowServiceType.ZHI_HUI_CAN_TING.getType(),
                                ServiceProgressTemplateConstants.Type.SERVICE_START, ServiceProgressTemplateConstants.STATUS_END);
                // begin 菜单配置服务
                updateTemplateIdByManual(newProcInsId, FlowServiceType.ZHI_HUI_CAN_TING.getType(), ServiceProgressTemplateConstants.Type.MENU_CONFIG,
                                ServiceProgressTemplateConstants.STATUS_BEGIN);
            } else {
                if (isServiceStartEnd) {
                    // end 服务启动
                    updateTemplateIdByManual(newProcInsId, FlowServiceType.ZHI_HUI_CAN_TING.getType(),
                                    ServiceProgressTemplateConstants.Type.SERVICE_START, ServiceProgressTemplateConstants.STATUS_END);
                }
            }
        } else {
            ServiceProgressExtra extra = new ServiceProgressExtra();
            extra.setProcInsId(oldProcInsId);
            extra.setServiceType(FlowServiceType.ZHI_HUI_CAN_TING.getType());
            List<ErpServiceProgress> deleteList = new ArrayList<>();
            List<ServiceProgressExtra> oldList = dao.findExtra(extra);
            if (CollectionUtils.isEmpty(oldList)) {
                return;
            }
            // 智慧餐厅 数据
            Map<String, ErpServiceProgress> zhctMap = getErpServiceProgressMap(newProcInsId, FlowServiceType.ZHI_HUI_CAN_TING.getType());
            // 复制老数据
            for (ServiceProgressExtra progressExtra : oldList) {
                String id = progressExtra.getProgressId();
                ErpServiceProgress deleteProgress = new ErpServiceProgress();
                deleteProgress.setId(id);
                deleteList.add(deleteProgress);
                ErpServiceProgress newProgress = dao.get(id);
                if (newProgress == null) {
                    logger.info("服务进度不存在！id={}", id);
                    continue;
                }
                ErpServiceProgress pro = zhctMap.get(progressExtra.getType());
                if (pro != null && StringUtils.isNotBlank(pro.getId())) {
                    newProgress.setId(pro.getId());
                    newProgress.setProcInsId(newProcInsId);
                    save(newProgress);
                }
            }
            // 删除老数据
            for (ErpServiceProgress delete : deleteList) {
                dao.delete(delete);
            }
            // 先根据老流程获取，如果没有 再根据新流程id获取
            ErpVisitServiceInfo visit = visitServiceInfoService.getByGoalCode(oldProcInsId, VisitServiceItemConstants.Goal.ZHCT_CODE);
            if (visit == null) {
                visit = visitServiceInfoService.getByGoalCode(newProcInsId, VisitServiceItemConstants.Goal.ZHCT_CODE);
            }
            // 上门id
            String visitId = visit == null ? null : visit.getId();
            // 智慧餐厅服务 将服务验收评价数据流程id更改过来
            ErpServiceAcceptance acceptance = serviceAcceptanceService.getByVisitId(visitId);
            if (acceptance != null) {
                acceptance.setProcInsId(newProcInsId);
                serviceAcceptanceService.save(acceptance);
            }
        }

    }

    /**
     * 管理智能客流运营落地服务 进度
     *
     * @param newProcInsId
     * @param oldProcInsId
     * @date 2018年8月23日
     * @author linqunzhi
     */
    private void managerUpgradeFmpsProgress(String newProcInsId, String oldProcInsId, int processVersion) {
        ServiceProgressExtra extra = new ServiceProgressExtra();
        extra.setProcInsId(oldProcInsId);
        extra.setServiceType(FlowServiceType.DELIVERY_FMPS_BASIC.getType());
        List<ErpServiceProgress> deleteList = new ArrayList<>();
        List<ServiceProgressExtra> oldList = dao.findExtra(extra);
        if (CollectionUtils.isEmpty(oldList)) {
            return;
        }
        // 获取临界节点模板数据
        ErpServiceProgressTemplate borderBasicTemplate = serviceProgressTemplateService.getOnly(FlowServiceType.DELIVERY_FMPS_BASIC.getType(),
                        ServiceProgressTemplateConstants.Type.VISIT_SERVICE, ServiceProgressTemplateConstants.STATUS_NO_BEGIN, processVersion);
        if (borderBasicTemplate == null) {
            return;
        }
        // 临界节点 顺序大小
        int borderSort = borderBasicTemplate.getSort();
        // 首次营销进度信息
        Map<String, ErpServiceProgress> fmpsMap = getErpServiceProgressMap(newProcInsId, FlowServiceType.DELIVERY_FMPS.getType());
        ErpServiceProgress deleteProgress = null;
        String zhangbeiId = null;
        for (ServiceProgressExtra progressExtra : oldList) {
            zhangbeiId = progressExtra.getZhangbeiId();
            String id = progressExtra.getProgressId();
            String type = progressExtra.getType();
            deleteProgress = new ErpServiceProgress();
            deleteProgress.setId(id);
            deleteList.add(deleteProgress);
            int sort = progressExtra.getSort();
            // 顺序小于临界值
            if (borderSort >= sort) {
                // 如果是上门服务 需要替换成 智能客流运营全套落地服务
                if (ServiceProgressTemplateConstants.Type.VISIT_SERVICE.equals(type)) {
                    type = ServiceProgressTemplateConstants.Type.FLOOR_SERVICE;
                }
                ErpServiceProgressTemplate template = serviceProgressTemplateService.getOnly(FlowServiceType.DELIVERY_FMPS.getType(), type,
                                progressExtra.getStatus(), progressExtra.getProcessVersion());
                if (template != null) {
                    ErpServiceProgress newProgress = fmpsMap.get(type);
                    // 如果是 临界节点，而且已不是 未启动状态，则需要标记为刚启动状态
                    if (borderSort == sort && !ServiceProgressTemplateConstants.STATUS_NO_BEGIN.equals(progressExtra.getStatus())) {
                        template = serviceProgressTemplateService.getOnly(FlowServiceType.DELIVERY_FMPS.getType(), type,
                                        ServiceProgressTemplateConstants.STATUS_BEGIN, processVersion);
                        newProgress.setTemplateId(template.getId());
                        newProgress.setStartTime(new Date());
                    } else {
                        newProgress.setTemplateId(template.getId());
                        newProgress.setStartTime(progressExtra.getStartTime());
                    }
                }
            }
        }
        // 设置掌贝id的值，新增进度
        for (Entry<String, ErpServiceProgress> entry : fmpsMap.entrySet()) {
            ErpServiceProgress newProgress = entry.getValue();
            newProgress.setZhangbeiId(zhangbeiId);
            newProgress.setProcInsId(newProcInsId);
            save(newProgress);
        }
        // 删除老数据
        for (ErpServiceProgress del : deleteList) {
            dao.delete(del);
        }
    }

    /**
     * 获取 map 根据流程id和服务类型
     *
     * @param procInsId
     * @return
     * @date 2018年8月24日
     * @author linqunzhi
     */
    private Map<String, ErpServiceProgress> getErpServiceProgressMap(String procInsId, String serviceType) {
        Map<String, ErpServiceProgress> result = new HashMap<String, ErpServiceProgress>();
        ServiceProgressExtra queryParam = new ServiceProgressExtra();
        queryParam.setServiceType(serviceType);
        queryParam.setProcInsId(procInsId);
        // 获取智能客流 进度列表
        List<ServiceProgressExtra> list = dao.findExtra(queryParam);
        int size = list == null ? 0 : list.size();
        logger.info("进度size={}", size);
        if (size > 0) {
            ErpServiceProgress serviceSchedule = null;
            for (ServiceProgressExtra progressExtra : list) {
                String templateId = progressExtra.getId();
                serviceSchedule = new ErpServiceProgress();
                serviceSchedule.setId(progressExtra.getProgressId());
                serviceSchedule.setTemplateId(templateId);
                result.put(progressExtra.getType(), serviceSchedule);
            }
        }
        return result;
    }

}
