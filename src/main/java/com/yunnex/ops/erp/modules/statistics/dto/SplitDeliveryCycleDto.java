package com.yunnex.ops.erp.modules.statistics.dto;

import com.yunnex.ops.erp.common.persistence.RequestDto;

/**
 * 分单统计 交付周期
 * 
 * @author linqunzhi
 * @date 2018年5月14日
 */
public class SplitDeliveryCycleDto extends RequestDto<SplitDeliveryCycleDto> {

    private static final long serialVersionUID = -4424863034120003923L;

    /** 区间最小值 */
    private Integer min;

    /** 区间最大值 */
    private Integer max;

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }
}
