package com.yunnex.ops.erp.modules.statistics.dto;

import com.yunnex.ops.erp.common.persistence.ResponseDto;

/**
 * 分单 周/月 统计数量 ResponseDto
 * 
 * @author linqunzhi
 * @date 2018年5月15日
 */
public class SplitWeekAndMonthResponseDto extends ResponseDto<SplitWeekAndMonthResponseDto> {

    private static final long serialVersionUID = 1259696092498485466L;

    /** 新接入分单数 */
    private int newCount;

    /** 上线分单数 */
    private int onlineCount;

    /** 上线分单中的超时分单数 */
    private int onlineCountOvertime;

    /** 上线分单平均周期 */
    private String avgCycle;

    public int getNewCount() {
        return newCount;
    }

    public void setNewCount(int newCount) {
        this.newCount = newCount;
    }

    public int getOnlineCount() {
        return onlineCount;
    }

    public void setOnlineCount(int onlineCount) {
        this.onlineCount = onlineCount;
    }

    public int getOnlineCountOvertime() {
        return onlineCountOvertime;
    }

    public void setOnlineCountOvertime(int onlineCountOvertime) {
        this.onlineCountOvertime = onlineCountOvertime;
    }

    public String getAvgCycle() {
        return avgCycle;
    }

    public void setAvgCycle(String avgCycle) {
        this.avgCycle = avgCycle;
    }
}
