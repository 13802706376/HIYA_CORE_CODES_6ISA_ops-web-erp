package com.yunnex.ops.erp.modules.message.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 服务通知表Entity
 * 
 * @author yunnex
 * @version 2018-07-04
 */
public class ErpServiceMessage extends DataEntity<ErpServiceMessage> {

    private static final long serialVersionUID = 1L;
    private String serviceNums; // 服务*数量
    private String content; // 内容
    private String type; // 类型（Pending 待处理，Key 关键信息）
    private Date startTime;// 开始时间
    private Date endTime; // 结束时间
    private String linkType; // link_type
    private String linkParam; // 跳转参数
    private String procInsId; // 流程id
    private String taskDefinitionKey; // 任务key值
    private String serviceType; // 服务类型（DeliveryService 交付服务 ，Split 引流推广服务）
    private String nodeType; // 节点类型
    private String nodeName; // 节点名称
    private String zhangbeiId;// 掌贝id

    public ErpServiceMessage() {
        super();
    }

    public ErpServiceMessage(String id) {
        super(id);
    }

    @Length(min = 1, max = 100, message = "服务*数量长度必须介于 1 和 100 之间")
    public String getServiceNums() {
        return serviceNums;
    }

    public void setServiceNums(String serviceNums) {
        this.serviceNums = serviceNums;
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "结束时间不能为空")
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Length(min = 1, max = 50, message = "link_type长度必须介于 1 和 50 之间")
    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    @Length(min = 1, max = 200, message = "跳转参数长度必须介于 1 和 200 之间")
    public String getLinkParam() {
        return linkParam;
    }

    public void setLinkParam(String linkParam) {
        this.linkParam = linkParam;
    }

    @Length(min = 1, max = 64, message = "流程id长度必须介于 1 和 64 之间")
    public String getProcInsId() {
        return procInsId;
    }

    public void setProcInsId(String procInsId) {
        this.procInsId = procInsId;
    }

    @Length(min = 1, max = 50, message = "任务key值长度必须介于 1 和 50 之间")
    public String getTaskDefinitionKey() {
        return taskDefinitionKey;
    }

    public void setTaskDefinitionKey(String taskDefinitionKey) {
        this.taskDefinitionKey = taskDefinitionKey;
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

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getZhangbeiId() {
        return zhangbeiId;
    }

    public void setZhangbeiId(String zhangbeiId) {
        this.zhangbeiId = zhangbeiId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
}
