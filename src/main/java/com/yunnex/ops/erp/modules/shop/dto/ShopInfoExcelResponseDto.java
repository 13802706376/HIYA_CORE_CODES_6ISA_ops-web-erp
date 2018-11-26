package com.yunnex.ops.erp.modules.shop.dto;

import java.io.Serializable;

import com.yunnex.ops.erp.common.utils.excel.annotation.MapperCell;

public class ShopInfoExcelResponseDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;// 商户id

    @MapperCell(order = 1, cellName = "掌贝账号")
    private String zhangbeiId;

    @MapperCell(order = 2, cellName = "商户名称")
    private String name;

    @MapperCell(order = 3, cellName = "商户简称")
    private String abbreviation;

    @MapperCell(order = 4, cellName = "已添加门店数")
    private Integer storeCount;

    @MapperCell(order = 5, cellName = "运营服务待处理")
    private Integer pendingServiceNum;// 运营服务待处理

    @MapperCell(order = 6, cellName = "聚引客待处理")
    private Integer pendingJykNum;// 待处理聚引客数量

    @MapperCell(order = 7, cellName = "运营顾问")
    private String operationAdviserName;

    public String getZhangbeiId() {
        return zhangbeiId;
    }

    public void setZhangbeiId(String zhangbeiId) {
        this.zhangbeiId = zhangbeiId;
    }


    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOperationAdviserName() {
        return operationAdviserName;
    }

    public void setOperationAdviserName(String operationAdviserName) {
        this.operationAdviserName = operationAdviserName;
    }

    public Integer getPendingServiceNum() {
        return pendingServiceNum;
    }

    public void setPendingServiceNum(Integer pendingServiceNum) {
        this.pendingServiceNum = pendingServiceNum;
    }

    public Integer getPendingJykNum() {
        return pendingJykNum;
    }

    public void setPendingJykNum(Integer pendingJykNum) {
        this.pendingJykNum = pendingJykNum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getStoreCount() {
        return storeCount;
    }

    public void setStoreCount(Integer storeCount) {
        this.storeCount = storeCount;
    }
}
