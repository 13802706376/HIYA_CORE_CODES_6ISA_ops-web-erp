package com.yunnex.ops.erp.modules.message.entity;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 服务通知模板表Entity
 * 
 * @author yunnex
 * @version 2018-07-04
 */
public class ErpServiceMessageTemplate extends DataEntity<ErpServiceMessageTemplate> {

    private static final long serialVersionUID = 1L;
    private String content; // 内容
    private String type; // 类型（Pending 待处理，Key 关键信息）
    private String linkId; // 跳转id(交互入口)
    private String taskDefinitionKeys; // 多个任务key值(多个逗号隔开)
    private String taskKeyType; // 任务key触发条件（And: 多个任务都结束时触发，Or : 只要其中一个结束就触发）
    private Integer endIntervals; // 结束的间隔时间（单位：小时，如果大于等于0则认为有时间触发结束）
    private String serviceType; // 服务类型（DeliveryService 交付服务 ，Split 引流推广服务）
    private String status; // 状态（Begin 开始，End 结束）
    private String nodeType; // 节点类型
    private String nodeName; // 节点名称
    private Integer startIntervals; // 开始的间隔时间（单位：小时，如果大于等于0则认为有时间触发结束）


    // 参数字段
    private String procInsId; // 流程实例id
    private List<String> ids;// ID集合，批量查询用

    public ErpServiceMessageTemplate() {
        super();
    }

    public ErpServiceMessageTemplate(String id) {
        super(id);
    }

    @Length(min = 1, max = 2000, message = "内容长度必须介于 1 和 2000 之间")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Length(min = 1, max = 30, message = "类型（Pending 待处理，Key 关键信息）长度必须介于 1 和 30 之间")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Length(min = 1, max = 64, message = "跳转id(交互入口)长度必须介于 1 和 64 之间")
    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    @Length(min = 1, max = 300, message = "多个任务key值(多个逗号隔开)长度必须介于 1 和 300 之间")
    public String getTaskDefinitionKeys() {
        return taskDefinitionKeys;
    }

    public void setTaskDefinitionKeys(String taskDefinitionKeys) {
        this.taskDefinitionKeys = taskDefinitionKeys;
    }

    @Length(min = 1, max = 20, message = "任务key触发条件（And: 多个任务都结束时触发，Or : 只要其中一个结束就触发）长度必须介于 1 和 20 之间")
    public String getTaskKeyType() {
        return taskKeyType;
    }

    public void setTaskKeyType(String taskKeyType) {
        this.taskKeyType = taskKeyType;
    }

    @Length(min = 1, max = 5, message = "结束的间隔时间（单位：小时，如果大于等于0则认为有时间触发结束）长度必须介于 1 和 5 之间")
    public Integer getEndIntervals() {
        return endIntervals;
    }

    public void setEndIntervals(Integer endIntervals) {
        this.endIntervals = endIntervals;
    }

    @Length(min = 1, max = 30, message = "服务类型（DeliveryService  交付服务 ，Split 引流推广服务）长度必须介于 1 和 30 之间")
    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public Integer getStartIntervals() {
        return startIntervals;
    }

    public void setStartIntervals(Integer startIntervals) {
        this.startIntervals = startIntervals;
    }

    public String getProcInsId() {
        return procInsId;
    }

    public void setProcInsId(String procInsId) {
        this.procInsId = procInsId;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
