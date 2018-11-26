package com.yunnex.ops.erp.modules.good.entity;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;
import com.yunnex.ops.erp.modules.good.category.entity.ErpGoodCategory;

/**
 * 商品信息管理Entity
 * 
 * @author Frank
 * @version 2017-10-21
 */
public class ErpGoodInfo extends DataEntity<ErpGoodInfo> {

    private static final long serialVersionUID = 1L;
    private String name; // 商品名称
    private Long price; // 商品价格（单位:分）
    private Integer sort; // 排序字段
    private Long categoryId; // 商品类别ID
    private String categoryName; // 商品类别名称

    private ErpGoodCategory category;
    private List<ErpGoodService> serviceList;// 商品对应的服务列表
    private List<ErpGoodService> packageServiceList = new ArrayList<>();// 属于套餐中的服务
    private List<ErpGoodService> singleServiceList = new ArrayList<>();// 单买服务

    private String isPackage;// 是否为套餐商品


    public ErpGoodCategory getCategory() {
        return category;
    }

    public void setCategory(ErpGoodCategory category) {
        this.category = category;
    }

    public ErpGoodInfo() {
        super();
    }

    public ErpGoodInfo(String id) {
        super(id);
    }

    @Length(min = 0, max = 64, message = "商品名称长度必须介于 0 和 64 之间")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Length(min = 0, max = 32, message = "商品价格不可为空!")
    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }


    @NotNull(message = "排序字段不可为空")
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @NotNull(message = "商品分类编号不能为空")
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public List<ErpGoodService> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<ErpGoodService> serviceList) {
        this.serviceList = serviceList;
    }

    public List<ErpGoodService> getPackageServiceList() {
        return packageServiceList;
    }

    public void setPackageServiceList(List<ErpGoodService> packageServiceList) {
        this.packageServiceList = packageServiceList;
    }

    public List<ErpGoodService> getSingleServiceList() {
        return singleServiceList;
    }

    public void setSingleServiceList(List<ErpGoodService> singleServiceList) {
        this.singleServiceList = singleServiceList;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getIsPackage() {
        return isPackage;
    }

    public void setIsPackage(String isPackage) {
        this.isPackage = isPackage;
    }

}
