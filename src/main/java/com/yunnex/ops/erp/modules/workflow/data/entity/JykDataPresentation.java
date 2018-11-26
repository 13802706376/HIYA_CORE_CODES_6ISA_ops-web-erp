package com.yunnex.ops.erp.modules.workflow.data.entity;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 聚引客数据报告对象
 * 
 * @author SunQ
 * @date 2018年1月23日
 */
public class JykDataPresentation extends DataEntity<JykDataPresentation> {

    private static final long serialVersionUID = 2538357213327549725L;

    /**
     * 原始订单信息
     */
    private String orderId;
    /**
     * 分单序号
     */
    private String splitId;
    /**
     * 流程序号
     */
    private String procInsId;
    /**
     * 数据报告类型(1:首日2:过程中3:最终)
     */
    private String dataType;
    /**
     * PDF文件URL
     */
    private String pdfUrl;
    /**
     * PDF文件名称
     */
    private String pdfName;
    /**
     * 状态(0:默认,表示不使用1:当前正在使用2:已发布到小程序)
     */
    private String state;
    /**
     * 备注
     */
    private String remark;
    /**
     * 排序字段
     */
    private Long sort;
    
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public String getSplitId() {
        return splitId;
    }
    public void setSplitId(String splitId) {
        this.splitId = splitId;
    }
    public String getProcInsId() {
        return procInsId;
    }
    public void setProcInsId(String procInsId) {
        this.procInsId = procInsId;
    }
    public String getDataType() {
        return dataType;
    }
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
    public String getPdfUrl() {
        return pdfUrl;
    }
    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }
    public String getPdfName() {
        return pdfName;
    }
    public void setPdfName(String pdfName) {
        this.pdfName = pdfName;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
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