package com.yunnex.ops.erp.modules.order.entity;

/**
 * form
 * 
 * @author zjq
 * @date 2018年4月2日
 */
public class SplitGoodForm {

    // 商品id
    String goodId;
    // 拆单数量
    Integer num;
    // 分单id
    String splitId;
    // 规划专家
    String planningExpert;

    private String packId;

    private Long goodTypeId;

    public Long getGoodTypeId() {
        return goodTypeId;
    }

    public void setGoodTypeId(Long goodTypeId) {
        this.goodTypeId = goodTypeId;
    }

    public String getPackId() {
        return packId;
    }

    public void setPackId(String packId) {
        this.packId = packId;
    }

    public String getGoodId() {
        return goodId;
    }

    public void setGoodId(String goodId) {
        this.goodId = goodId;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getSplitId() {
        return splitId;
    }

    public void setSplitId(String splitId) {
        this.splitId = splitId;
    }

    public String getPlanningExpert() {
        return planningExpert;
    }

    public void setPlanningExpert(String planningExpert) {
        this.planningExpert = planningExpert;
    }

    @Override
    public String toString() {
        return "SplitGoodForm [goodId=" + goodId + ", num=" + num + ", splitId=" + splitId + ", planningExpert=" + planningExpert + ", packId=" + packId + ", goodTypeId=" + goodTypeId + "]";
    }



}
