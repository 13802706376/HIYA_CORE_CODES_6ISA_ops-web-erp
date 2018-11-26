package com.yunnex.ops.erp.modules.workflow.flow.entity;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 订单输入项
 * 
 * @author Frank
 * @version 2017-11-02
 */
public class ErpOrderInputDetail extends DataEntity<ErpOrderInputDetail> {

    private static final long serialVersionUID = 1L;
    private String splitId; // 分单序号
    private String inputTaskName; // 输入项标题
    private String inputTaskKey = ""; // 输入项key
    private String inputDetail; // 输入项内容
    private String remark; // 备注
    private Long sort; // 排序字段
    private String createName;
    private String createTime;


    public String getInputTaskKey() {
        return inputTaskKey;
    }

    public void setInputTaskKey(String inputTaskKey) {
        this.inputTaskKey = inputTaskKey;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public ErpOrderInputDetail() {
        super();
    }

    public ErpOrderInputDetail(String id) {
        super(id);
    }

    @Length(min = 1, max = 64, message = "分单序号长度必须介于 1 和 64 之间")
    public String getSplitId() {
        return splitId;
    }

    public void setSplitId(String splitId) {
        this.splitId = splitId;
    }

    @Length(min = 1, max = 64, message = "输入项标题长度必须介于 1 和 64 之间")
    public String getInputTaskName() {
        return inputTaskName;
    }

    public void setInputTaskName(String inputTaskName) {
        this.inputTaskName = inputTaskName;
    }

    public String getInputDetail() {
        return inputDetail;
    }

    public void setInputDetail(String inputDetail) {
        this.inputDetail = inputDetail;
    }

    @Length(min = 0, max = 256, message = "备注长度必须介于 0 和 256 之间")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

}
