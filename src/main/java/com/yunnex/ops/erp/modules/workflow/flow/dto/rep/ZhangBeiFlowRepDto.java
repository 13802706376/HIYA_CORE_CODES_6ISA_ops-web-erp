package com.yunnex.ops.erp.modules.workflow.flow.dto.rep;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单信息
 */
public class ZhangBeiFlowRepDto implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String shopInfoId;//商户id
    private Integer zhangbeiState;//掌贝进件状态
    private String  zhangbeiRemark;//审核不通过原因 
    private String  zhangbeiId;//掌贝账号
    private String  zhangbeiPassword;//掌贝密码
    public String getShopInfoId() {
        return shopInfoId;
    }
    public void setShopInfoId(String shopInfoId) {
        this.shopInfoId = shopInfoId;
    }
   
    public Integer getZhangbeiState() {
        return zhangbeiState;
    }
    public void setZhangbeiState(Integer zhangbeiState) {
        this.zhangbeiState = zhangbeiState;
    }
    public String getZhangbeiRemark() {
        return zhangbeiRemark;
    }
    public void setZhangbeiRemark(String zhangbeiRemark) {
        this.zhangbeiRemark = zhangbeiRemark;
    }
    public String getZhangbeiId() {
        return zhangbeiId;
    }
    public void setZhangbeiId(String zhangbeiId) {
        this.zhangbeiId = zhangbeiId;
    }
    public String getZhangbeiPassword() {
        return zhangbeiPassword;
    }
    public void setZhangbeiPassword(String zhangbeiPassword) {
        this.zhangbeiPassword = zhangbeiPassword;
    }
    
    
}
