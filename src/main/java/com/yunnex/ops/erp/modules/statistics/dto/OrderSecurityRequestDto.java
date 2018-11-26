package com.yunnex.ops.erp.modules.statistics.dto;

import java.util.List;

import com.yunnex.ops.erp.common.persistence.RequestDto;

/**
 * 订单安全过滤dto
 * 
 * @author linqunzhi
 * @date 2018年6月4日
 */
public class OrderSecurityRequestDto extends RequestDto<OrderSecurityRequestDto> {

    private static final long serialVersionUID = -8967786597165390167L;

    /** 服务商编号 */
    private List<Integer> agentIdList;

    /** 订单类别 （1：直销 2：服务商） */
    private List<Integer> orderTypeList;

    public List<Integer> getAgentIdList() {
        return agentIdList;
    }

    public void setAgentIdList(List<Integer> agentIdList) {
        this.agentIdList = agentIdList;
    }

    public List<Integer> getOrderTypeList() {
        return orderTypeList;
    }

    public void setOrderTypeList(List<Integer> orderTypeList) {
        this.orderTypeList = orderTypeList;
    }


}
