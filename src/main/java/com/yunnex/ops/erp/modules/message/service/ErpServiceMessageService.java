package com.yunnex.ops.erp.modules.message.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.yunnex.ops.erp.common.utils.DateUtils;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.modules.act.service.ActTaskService;
import com.yunnex.ops.erp.modules.message.constant.ServiceMessageTemplateConstants;
import com.yunnex.ops.erp.modules.message.constant.ServiceProgressTemplateConstants;
import com.yunnex.ops.erp.modules.message.dao.ErpServiceMessageDao;
import com.yunnex.ops.erp.modules.message.entity.ErpServiceLink;
import com.yunnex.ops.erp.modules.message.entity.ErpServiceMessage;
import com.yunnex.ops.erp.modules.message.entity.ErpServiceMessageTemplate;
import com.yunnex.ops.erp.modules.workflow.delivery.entity.ErpDeliveryService;
import com.yunnex.ops.erp.modules.workflow.delivery.service.ErpDeliveryServiceService;
import com.yunnex.ops.erp.modules.workflow.enums.FlowServiceType;

/**
 * 服务通知表Service
 * 
 * @author yunnex
 * @version 2018-07-04
 */
@Service
public class ErpServiceMessageService extends CrudService<ErpServiceMessageDao, ErpServiceMessage> {

    @Autowired
    private ErpServiceMessageTemplateService messageTemplateService;
    @Autowired
    private ActTaskService actTaskService;
    @Autowired
    private ErpServiceLinkService serviceLinkService;
    @Autowired
    @Lazy
    private ServiceMessageVariableService messageVariableService;
    @Autowired
    private ErpDeliveryServiceService deliveryServiceService;

    @Override
    public ErpServiceMessage get(String id) {
        return super.get(id);
    }

    @Override
    public List<ErpServiceMessage> findList(ErpServiceMessage erpServiceMessage) {
        return super.findList(erpServiceMessage);
    }

    @Override
    public Page<ErpServiceMessage> findPage(Page<ErpServiceMessage> page, ErpServiceMessage erpServiceMessage) {
        return super.findPage(page, erpServiceMessage);
    }

    @Override
    @Transactional(readOnly = false)
    public void save(ErpServiceMessage erpServiceMessage) {
        super.save(erpServiceMessage);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(ErpServiceMessage erpServiceMessage) {
        super.delete(erpServiceMessage);
    }

    /**
     * 管理服务通知
     *
     * @param procInsId
     * @param serviceType
     * @param orderId
     * @date 2018年7月9日
     * @author linqunzhi
     */
    public void managerMessage(String procInsId, String serviceType, String taskDefinitionKey) {
        logger.info("createMessage start | procInsId={}|serviceType={}|taskDefinitionKey={}", procInsId, serviceType, taskDefinitionKey);
        ErpServiceMessageTemplate template = new ErpServiceMessageTemplate();
        template.setServiceType(serviceType);
        template.setTaskDefinitionKeys(taskDefinitionKey);
        // 获取服务通知模板
        List<ErpServiceMessageTemplate> templateList = messageTemplateService.findList(template);
        int size = templateList == null ? 0 : templateList.size();
        logger.info("通知模板 size={}", size);
        if (size > 0) {
            for (ErpServiceMessageTemplate messageTemplate : templateList) {
                // 是否需要进行新增或修改
                boolean needManager = needManager(procInsId, taskDefinitionKey, messageTemplate);
                if (needManager) {
                    String status = messageTemplate.getStatus();
                    // 如果是启动，则创建消息
                    if (ServiceMessageTemplateConstants.STATUS_BEGIN.equals(status)) {
                        createMessage(procInsId, serviceType, taskDefinitionKey, messageTemplate);
                    } else {
                        updateMessge(procInsId, serviceType, messageTemplate);
                    }
                }
            }
        }
        logger.info("createMessage end");
    }

    /**
     * 管理服务通知
     *
     * @param procInsId
     * @param serviceTypeList
     * @param taskDefinitionKey
     * @date 2018年8月28日
     * @author linqunzhi
     */
    @Transactional
    public void managerMessage(String procInsId, List<String> serviceTypeList, String taskDefinitionKey) {
        if (CollectionUtils.isNotEmpty(serviceTypeList)) {
            for (String serviceType : serviceTypeList) {
                managerMessage(procInsId, serviceType, taskDefinitionKey);
            }
        }
    }

    private void updateMessge(String procInsId, String serviceType, ErpServiceMessageTemplate messageTemplate) {
        String nodeType = messageTemplate.getNodeType();
        ErpServiceMessage message = new ErpServiceMessage();
        message.setNodeType(nodeType);
        message.setProcInsId(procInsId);
        message.setServiceType(serviceType);
        List<ErpServiceMessage> serviceMessageList = this.findList(message);
        if (CollectionUtils.isNotEmpty(serviceMessageList)) {
            for (ErpServiceMessage serviceMessage : serviceMessageList) {
                // 如果设置了 结束的间隔时间，则取该时间，反之 取当前时间
                Date endTime = calculateEndTime(messageTemplate.getEndIntervals());
                if (endTime == null) {
                    endTime = new Date();
                }
                serviceMessage.setEndTime(endTime);
                this.save(serviceMessage);
            }
        }
    }

    /**
     * 创建服务通知
     *
     * @param procInsId
     * @param serviceType
     * @param messageTemplate
     * @date 2018年7月9日
     * @author linqunzhi
     * @param taskDefinitionKey
     */
    private void createMessage(String procInsId, String serviceType, String taskDefinitionKey, ErpServiceMessageTemplate messageTemplate) {
        String nodeType = messageTemplate.getNodeType();
        ErpServiceMessage param = new ErpServiceMessage();
        param.setNodeType(nodeType);
        param.setProcInsId(procInsId);
        param.setServiceType(serviceType);
        List<ErpServiceMessage> existList = this.findList(param);
        if (CollectionUtils.isNotEmpty(existList)) {
            logger.info("通知已发送过，不再发送 。procInsId={}|nodeType={}", procInsId, nodeType);
            return;
        }
        Map<String, String> variableMap = messageVariableService.getVariableMap(procInsId, serviceType, messageTemplate.getNodeType());
        String content = messageTemplate.getContent(); // 服务通知类容
        String linkId = messageTemplate.getLinkId();
        String linkType = null;// 交互入口类型
        String linkParam = null; // 交互入口参数
        if (StringUtils.isNotBlank(linkId)) {
            ErpServiceLink link = serviceLinkService.get(linkId);
            if (link != null) {
                linkParam = link.getParam();
                linkType = link.getType();
            }
        }
        String serviceNums = "@ServiceNums@";// 服务数量
        String zhangbeiId = "@ZhangbeiId@";// 掌贝id
        content = content == null ? "" : content;
        String key = null;
        String vaule = null;
        // 将变量替换成实际值
        if (variableMap != null) {
            for (Entry<String, String> entry : variableMap.entrySet()) {
                key = entry.getKey();
                vaule = entry.getValue();
                serviceNums = serviceNums.replace(key, vaule);
                zhangbeiId = zhangbeiId.replace(key, vaule);
                content = content.replace(key, vaule);
            }
        }
        ErpServiceMessage message = new ErpServiceMessage();
        message.setServiceNums(serviceNums);
        message.setContent(content);
        message.setType(messageTemplate.getType());
        message.setNodeType(messageTemplate.getNodeType());
        message.setNodeName(messageTemplate.getNodeName());
        message.setStartTime(calculateStartTime(messageTemplate.getStartIntervals()));
        message.setEndTime(calculateEndTime(messageTemplate.getEndIntervals()));
        message.setLinkType(linkType);
        message.setLinkParam(linkParam);
        message.setZhangbeiId(zhangbeiId);
        message.setProcInsId(procInsId);
        message.setTaskDefinitionKey(taskDefinitionKey);
        message.setServiceType(serviceType);
        this.save(message);
    }

    /**
     * 计算开始时间
     *
     * @param startIntervals
     * @return
     * @date 2018年7月25日
     * @author linqunzhi
     */
    private Date calculateStartTime(Integer startIntervals) {
        Date result = new Date();
        if (startIntervals != null && startIntervals >= 0) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(result);
            cal.add(Calendar.HOUR_OF_DAY, startIntervals);
            result = cal.getTime();
        }
        return result;
    }

    /**
     * 计算结束时间
     *
     * @param endIntervals
     * @return
     * @date 2018年7月9日
     * @author linqunzhi
     */
    private static Date calculateEndTime(Integer endIntervals) {
        Date result = null;
        if (endIntervals != null && endIntervals >= 0) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.HOUR_OF_DAY, endIntervals);
            result = cal.getTime();
        }
        return result;
    }

    /**
     * 是否需要新增或修改
     *
     * @param procInsId
     * @param taskDefinitionKey
     * @param messageTemplate
     * @return
     * @date 2018年7月9日
     * @author linqunzhi
     */
    private boolean needManager(String procInsId, String taskDefinitionKey, ErpServiceMessageTemplate messageTemplate) {
        String definitionKeys = messageTemplate.getTaskDefinitionKeys();
        if (StringUtils.isBlank(definitionKeys)) {
            return false;
        }
        // 获取节点数组
        String[] definitionKeyArr = definitionKeys.split(CommonConstants.Sign.COMMA);
        // 如果只有一个任务节点，直接返回true
        if (definitionKeyArr.length == 1) {
            return true;
        }
        String taskKeyType = messageTemplate.getTaskKeyType();
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
     * 手动触发 服务通知信息
     *
     * @param procInsId
     * @param serviceType
     * @param type
     * @param status
     * @date 2018年7月19日
     * @author linqunzhi
     */
    @Transactional(readOnly = false)
    public void managerMessageByManual(String procInsId, String serviceType, String nodeType, String status) {
        logger.info("managerMessageByManual start | procInsId={}|serviceType={}|nodeType={}|status={}");
        ErpServiceMessageTemplate messageTemplate = messageTemplateService.getOnly(serviceType, nodeType, status);
        if (messageTemplate != null) {
            // 如果是启动，则创建消息
            if (ServiceMessageTemplateConstants.STATUS_BEGIN.equals(status)) {
                createMessage(procInsId, serviceType, null, messageTemplate);
            } else {
                updateMessge(procInsId, serviceType, messageTemplate);
            }
        }
        logger.info("managerMessageByManual end");
    }

    /**
     * 根据订单id 管理交付流程 消息
     *
     * @param procInsId 流程id
     * @param nodeType 节点类型
     * @param status 状态
     * @date 2018年7月25日
     * @author linqunzhi
     */
    public void managerDeliveryMessageByProcInsId(String procInsId, String nodeType, String status) {
        ErpDeliveryService delivery = deliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
        if (delivery == null) {
            logger.info("服务流程不存在！procInsId={}", procInsId);
            return;
        }
        managerMessageByManual(procInsId, delivery.getServiceType(), nodeType, status);
    }

    /**
     * 业务定义：保存通知信息
     * 
     * @date 2018年7月23日
     * @author R/Q
     */
    @Transactional(readOnly = false)
    public void saveMessageRecord(String paramJson) {
        if (StringUtils.isBlank(paramJson)) {
            throw new ServiceException("前台传递参数错误");
        }
        List<ErpServiceMessageTemplate> paramList = JSON.parseArray(StringEscapeUtils.unescapeHtml4(paramJson), ErpServiceMessageTemplate.class);
        if (CollectionUtils.isNotEmpty(paramList)) {
            paramList.stream().forEach(paramObj -> this.managerMessageByManual(paramObj.getProcInsId(), paramObj.getServiceType(),
                            paramObj.getNodeType(), paramObj.getStatus()));
        }
    }

    @Transactional(readOnly = false)
    public void managerMessageByManualDate(String procInsId, String serviceType, String nodeType, String status, Date time) {
        logger.info("managerMessageByManual start | procInsId={}|serviceType={}|nodeType={}|status={},time={}", procInsId, serviceType, nodeType,
                        status, DateUtils.formatDateTime(time));
        ErpServiceMessageTemplate messageTemplate = messageTemplateService.getOnly(serviceType, nodeType, status);
        if (messageTemplate != null) {
            Date now = new Date();
            int intervals = calculateIntervals(time, now);
            // 如果是启动，则创建消息
            if (ServiceMessageTemplateConstants.STATUS_BEGIN.equals(status)) {
                messageTemplate.setStartIntervals(intervals);
                createMessage(procInsId, serviceType, null, messageTemplate);
            } else {
                messageTemplate.setEndIntervals(intervals);
                updateMessge(procInsId, serviceType, messageTemplate);
            }
        }
        logger.info("managerMessageByManual end");
    }

    /**
     * 获取相差 小时
     *
     * @param time
     * @param now
     * @return
     * @date 2018年7月26日
     * @author linqunzhi
     */
    private static int calculateIntervals(Date time, Date now) {
        if (time == null || now == null) {
            return -1;
        }
        long diff = time.getTime() - now.getTime();
        if (diff < 0) {
            return -1;
        }
        return (int) (diff / 1000 / 60 / 60);
    }

    /**
     * 流程重启
     *
     * @param oldProInsId 老流程id
     * @date 2018年8月22日
     * @author linqunzhi
     */
    @Transactional(readOnly = false)
    public void restartFlow(String oldProInsId) {
        logger.info("restartFlow start | oldProInsId={}", oldProInsId);
        ErpServiceMessage m = new ErpServiceMessage();
        m.setProcInsId(oldProInsId);
        List<ErpServiceMessage> oldList = dao.findList(m);
        int oldSize = oldList == null ? 0 : oldList.size();
        logger.info("oldSize = {}", oldSize);
        if (oldSize > 0) {
            for (ErpServiceMessage message : oldList) {
                dao.delete(message);
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
     * @date 2018年8月22日
     * @author linqunzhi
     */
    @Transactional(readOnly = false)
    public void upgradeFmpsBasic(String newProcInsId, String oldProcInsId, boolean isAddZhuiHui, boolean isMenuConfigNode) {
        logger.info("upgradeFmpsBasic start | newProcInsId={}|oldProcInsId={}|isAddZhuiHui={}|isMenuConfigNode={}", newProcInsId, oldProcInsId,
                        isAddZhuiHui, isMenuConfigNode);
        managerUpgradeFmpsMessage(newProcInsId, oldProcInsId);
        managerUpgradeZhiHuiMessage(newProcInsId, oldProcInsId, isAddZhuiHui, isMenuConfigNode);
        logger.info("upgradeFmpsBasic end");

    }

    private void managerUpgradeZhiHuiMessage(String newProcInsId, String oldProcInsId, boolean isAddZhuiHui, boolean isMenuConfigNode) {
        // 需要删除的通知信息
        List<ErpServiceMessage> deleteList = new ArrayList<>();
        // 新加 智慧餐厅
        if (isAddZhuiHui) {
            // 是否开始 菜单配置服务节点
            if (isMenuConfigNode) {
                // 触发 begin 菜单配置服务 通知
                managerMessageByManual(newProcInsId, FlowServiceType.ZHI_HUI_CAN_TING.getType(), ServiceMessageTemplateConstants.NodeType.MENU_CONFIG,
                                ServiceMessageTemplateConstants.STATUS_BEGIN);
            }
        } else {
            ErpServiceMessage m = new ErpServiceMessage();
            m.setProcInsId(oldProcInsId);
            m.setServiceType(FlowServiceType.ZHI_HUI_CAN_TING.getType());
            // 智慧餐厅通知信息
            List<ErpServiceMessage> zhihuiList = dao.findList(m);
            if (CollectionUtils.isNotEmpty(zhihuiList)) {
                ErpServiceMessage deleteMessage = null;
                for (ErpServiceMessage message : zhihuiList) {
                    String id = message.getId();
                    deleteMessage = new ErpServiceMessage();
                    deleteMessage.setId(id);
                    deleteList.add(deleteMessage);
                    copyZhiHuiCanTingMessage(id, message.getNodeType(), newProcInsId);
                }
            }
        }
        // 删除老流程数据
        if (CollectionUtils.isNotEmpty(deleteList)) {
            for (ErpServiceMessage message : deleteList) {
                dao.delete(message);
            }
        }
    }

    private void managerUpgradeFmpsMessage(String newProcInsId, String oldProcInsId) {
        ErpServiceMessage m = new ErpServiceMessage();
        m.setProcInsId(oldProcInsId);
        m.setServiceType(FlowServiceType.DELIVERY_FMPS_BASIC.getType());
        // 需要删除的通知信息
        List<ErpServiceMessage> deleteList = new ArrayList<>();
        // 首次营销通知信息
        List<ErpServiceMessage> basicList = dao.findList(m);
        if (CollectionUtils.isNotEmpty(basicList)) {
            ErpServiceMessage deleteMessage = null;
            for (ErpServiceMessage message : basicList) {
                String id = message.getId();
                deleteMessage = new ErpServiceMessage();
                deleteMessage.setId(id);
                deleteList.add(deleteMessage);
                // 复制通知数据
                copyBasicMessage(id, message.getNodeType(), newProcInsId);
                // 临界节点数据生成
                borderBasicMessage(message.getNodeType(), newProcInsId);
            }
        }
        // 删除老流程数据
        if (CollectionUtils.isNotEmpty(deleteList)) {
            for (ErpServiceMessage message : deleteList) {
                dao.delete(message);
            }
        }
    }

    /**
     * 复制智慧餐厅相关通知
     *
     * @param id
     * @param nodeType
     * @param newProcInsId
     * @date 2018年8月23日
     * @author linqunzhi
     */
    private void copyZhiHuiCanTingMessage(String id, String nodeType, String newProcInsId) {
        if (StringUtils.isBlank(id)) {
            return;
        }
        ErpServiceMessage newMessage = dao.get(id);
        if (newMessage == null) {
            return;
        }
        newMessage.setId(null);
        newMessage.setProcInsId(newProcInsId);
        save(newMessage);
    }

    /**
     * 边界节点通知
     *
     * @param nodeType
     * @param newProcInsId
     * @date 2018年8月23日
     * @author linqunzhi
     */
    private void borderBasicMessage(String nodeType, String newProcInsId) {
        if (ServiceMessageTemplateConstants.NodeType.VISIT_SERVICE.equals(nodeType)) {
            // 触发智能客流运营全套落地服务通知
            managerMessageByManual(newProcInsId, FlowServiceType.DELIVERY_FMPS.getType(), ServiceMessageTemplateConstants.NodeType.FLOOR_SERVICE,
                            ServiceMessageTemplateConstants.STATUS_BEGIN);
        }

    }

    /**
     * 复制 首次营销基础版 通知信息
     *
     * @param id
     * @param nodeType
     * @param procInsId
     * @date 2018年8月22日
     * @author linqunzhi
     */

    private void copyBasicMessage(String id, String nodeType, String procInsId) {
        if (StringUtils.isBlank(id)) {
            return ;
        }
        if (ServiceMessageTemplateConstants.NodeType.DATA_COLLECT
                        .equals(nodeType) || ServiceMessageTemplateConstants.NodeType.MATERIAL_PRODUCTION_DELAY.equals(nodeType)) {
            ErpServiceMessage newMessage = dao.get(id);
            if(newMessage == null) {
                return;
            }
            newMessage.setId(null);
            newMessage.setProcInsId(procInsId);
            newMessage.setServiceType(FlowServiceType.DELIVERY_FMPS.getType());
            save(newMessage);
        }
    }
}
