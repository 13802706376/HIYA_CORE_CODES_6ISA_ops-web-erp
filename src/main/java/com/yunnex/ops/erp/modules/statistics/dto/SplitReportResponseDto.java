package com.yunnex.ops.erp.modules.statistics.dto;

import com.yunnex.ops.erp.common.persistence.ResponseDto;

/**
 * 分单统计 ResponseDto
 * 
 * @author linqunzhi
 * @date 2018年5月14日
 */
public class SplitReportResponseDto extends ResponseDto<SplitReportResponseDto> {

    private static final long serialVersionUID = -3537396624130616976L;

    private Integer allCount;// 累计总接单数
    private Integer followCount;// 跟进订单总数
    private Integer qualificationsCount;// 资质问题订单总数
    private Integer activeDelayCount;// 主动延时订单总数
    private Integer allQualificationsCount;// 有过资质问题订单总数
    private Integer allActiveDelayCount;// 有过主动延时订单总数
    private Integer normalCount;// 正常跟进订单总数
    private Integer handleCount;// 有任务正在处理订单总数
    private Integer overTimeCount;// 超时订单总数
    private Integer riskCount;// 超时风险订单总数

    public Integer getAllCount() {
        return allCount;
    }

    public void setAllCount(Integer allCount) {
        this.allCount = allCount;
    }

    public Integer getFollowCount() {
        return followCount;
    }

    public void setFollowCount(Integer followCount) {
        this.followCount = followCount;
    }

    public Integer getQualificationsCount() {
        return qualificationsCount;
    }

    public void setQualificationsCount(Integer qualificationsCount) {
        this.qualificationsCount = qualificationsCount;
    }

    public Integer getActiveDelayCount() {
        return activeDelayCount;
    }

    public void setActiveDelayCount(Integer activeDelayCount) {
        this.activeDelayCount = activeDelayCount;
    }

    public Integer getNormalCount() {
        return normalCount;
    }

    public void setNormalCount(Integer normalCount) {
        this.normalCount = normalCount;
    }

    public Integer getHandleCount() {
        return handleCount;
    }

    public void setHandleCount(Integer handleCount) {
        this.handleCount = handleCount;
    }

    public Integer getOverTimeCount() {
        return overTimeCount;
    }

    public void setOverTimeCount(Integer overTimeCount) {
        this.overTimeCount = overTimeCount;
    }

    public Integer getRiskCount() {
        return riskCount;
    }

    public void setRiskCount(Integer riskCount) {
        this.riskCount = riskCount;
    }

    public Integer getAllQualificationsCount() {
        return allQualificationsCount;
    }

    public void setAllQualificationsCount(Integer allQualificationsCount) {
        this.allQualificationsCount = allQualificationsCount;
    }

    public Integer getAllActiveDelayCount() {
        return allActiveDelayCount;
    }

    public void setAllActiveDelayCount(Integer allActiveDelayCount) {
        this.allActiveDelayCount = allActiveDelayCount;
    }


}
