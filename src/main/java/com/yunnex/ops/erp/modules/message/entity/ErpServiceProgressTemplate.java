package com.yunnex.ops.erp.modules.message.entity;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 服务进度模板表Entity
 * 
 * @author yunnex
 * @version 2018-07-04
 */
public class ErpServiceProgressTemplate extends DataEntity<ErpServiceProgressTemplate> {

    private static final long serialVersionUID = 1L;
    private String serviceType; // 服务类型（DeliveryService 交付服务 ，Split 引流推广服务）
    private String status; // 状态（NoBegin 未开始，Begin 开始，End 结束）
    private Integer sort; // 顺序（数字越小，优先级越高）
    private String content; // 节点显示内容（json字符串）
    private String linkId; // 跳转id
    private String taskDefinitionKeys; // 多个任务key值(多个逗号隔开)
    private String serviceRoles; // 服务角色（多个逗号隔开，开户顾问：accountAdviser 策划专家：PlanningExpert
                                 // 运营顾问：OperationAdviser）
    private Integer processVersion; // 流程版本号（3.1版本入库 int 301）
    private String type; // 服务进度类型
    private String name; // 服务进度名称
    private String taskKeyType;// 任务key触发类型（And 多个任务都结束时触发，Or 只要其中一个结束就触发）

    // 参数字段
    private String procInsId; // 流程实例id
    private List<String> ids;// ID集合，批量查询用

    public ErpServiceProgressTemplate() {
        super();
    }

    public ErpServiceProgressTemplate(String id) {
        super(id);
    }

    @Length(min = 1, max = 30, message = "服务类型（DeliveryService  交付服务 ，Split 引流推广服务）长度必须介于 1 和 30 之间")
    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    @Length(min = 1, max = 20, message = "状态（NoBegin 未开始，Begin 开始，End 结束）长度必须介于 1 和 20 之间")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @NotNull(message = "顺序（数字越小，优先级越高）不能为空")
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Length(min = 1, max = 2000, message = "节点显示内容（json字符串）长度必须介于 1 和 2000 之间")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Length(min = 1, max = 64, message = "跳转id长度必须介于 1 和 64 之间")
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

    @Length(min = 1, max = 200, message = "服务角色（多个逗号隔开，开户顾问：accountAdviser 策划专家：PlanningExpert 运营顾问：OperationAdviser）长度必须介于 1 和 200 之间")
    public String getServiceRoles() {
        return serviceRoles;
    }

    public void setServiceRoles(String serviceRoles) {
        this.serviceRoles = serviceRoles;
    }

    @NotNull(message = "流程版本号 （3.1版本入库  int 301）不能为空")
    public Integer getProcessVersion() {
        return processVersion;
    }

    public void setProcessVersion(Integer processVersion) {
        this.processVersion = processVersion;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaskKeyType() {
        return taskKeyType;
    }

    public void setTaskKeyType(String taskKeyType) {
        this.taskKeyType = taskKeyType;
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
