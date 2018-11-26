package com.yunnex.ops.erp.modules.shop.entity;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 经营类目Entity
 * @author 11
 * @version 2017-12-20
 */
public class BusinessCategory extends DataEntity<BusinessCategory> {
    
    private static final long serialVersionUID = 1L;
    private Long categoryId;        // 业务主键
    private String categoryName;        // 类目名称
    private Long father;        // 父类目id
    private Integer categoryType;        // 1:事业单位;2:个体/企业(新数据不使用);3:个体工商户;4:企业
    private String isNew;        // 是否是新使用的(0：不是新的，1：是新的)
    
    public BusinessCategory() {
        super();
    }

    public BusinessCategory(String id){
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

    @NotNull(message="业务主键不能为空")
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
    
    @Length(min=1, max=100, message="类目名称长度必须介于 1 和 100 之间")
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    @NotNull(message="父类目id不能为空")
    public Long getFather() {
        return father;
    }

    public void setFather(Long father) {
        this.father = father;
    }
    
    @NotNull(message="1:事业单位;2:个体/企业(新数据不使用);3:个体工商户;4:企业不能为空")
    public Integer getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(Integer categoryType) {
        this.categoryType = categoryType;
    }
    
    @Length(min=1, max=3, message="是否是新使用的(0：不是新的，1：是新的)长度必须介于 1 和 3 之间")
    public String getIsNew() {
        return isNew;
    }

    public void setIsNew(String isNew) {
        this.isNew = isNew;
    }
}