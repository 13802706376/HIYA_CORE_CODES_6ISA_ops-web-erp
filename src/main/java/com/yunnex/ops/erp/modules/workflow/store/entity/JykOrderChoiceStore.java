package com.yunnex.ops.erp.modules.workflow.store.entity;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 聚引客订单推广门店管理Entity
 * @author SunQ
 * @date 2018年1月8日
 */
public class JykOrderChoiceStore extends DataEntity<JykOrderChoiceStore> {

    private static final long serialVersionUID = 6715029159056770899L;

    private String orderId;     // 原始订单信息
    private String splitId;     // 分单序号
    private String procInsId;   // 流程序号
    private String choiceStore; // 选择的推广门店
    private String remark;      // 备注
    private Long sort;      // 排序字段
    
    public JykOrderChoiceStore() {
        super();
    }

    public JykOrderChoiceStore(String id){
        super(id);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Length(min=1, max=64, message="原始订单信息长度必须介于 1 和 64 之间")
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    
    @Length(min=1, max=64, message="分单序号长度必须介于 1 和 64 之间")
    public String getSplitId() {
        return splitId;
    }

    public void setSplitId(String splitId) {
        this.splitId = splitId;
    }
    
    @Length(min=1, max=64, message="流程序号长度必须介于 1 和 64 之间")
    public String getProcInsId() {
        return procInsId;
    }

    public void setProcInsId(String procInsId) {
        this.procInsId = procInsId;
    }

    @Length(min=1, max=64, message="推广渠道长度必须介于 1 和 64 之间")
    public String getChoiceStore() {
        return choiceStore;
    }

    public void setChoiceStore(String choiceStore) {
        this.choiceStore = choiceStore;
    }
    
    @Length(min=0, max=256, message="备注长度必须介于 0 和 256 之间")
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
}