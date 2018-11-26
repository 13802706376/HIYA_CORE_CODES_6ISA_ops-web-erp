package com.yunnex.ops.erp.modules.order.entity;

/**
 * 
 * 
 * @author zjq
 * @date 2018年4月2日
 */
public class SplitGoodEditNumForm {
    // 分单商品id
    String id;
    // 分单商品数量
    Integer num;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "MultiNumForm [id=" + id + ", num=" + num + ", getId()=" + getId() + ", getNum()=" + getNum() + "]";
    }

}
