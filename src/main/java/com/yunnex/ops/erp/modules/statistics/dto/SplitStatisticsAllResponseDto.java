package com.yunnex.ops.erp.modules.statistics.dto;

import java.util.List;

import com.yunnex.ops.erp.common.persistence.ResponseDto;

/**
 * 分单明细列表数据 ResponseDto
 * 
 * @author linqunzhi
 * @date 2018年5月15日
 */
public class SplitStatisticsAllResponseDto extends ResponseDto<SplitStatisticsAllResponseDto> {

    private static final long serialVersionUID = 1385378498867426290L;


    /** 总条数 */
    private long count;

    /** 完成分单数 */
    private int finishCount;

    /** 上线分单数 */
    private int newCount;

    /** 新接入分单数 */
    private int onlineCount;

    /** 列表结果集 */
    private List<SplitStatisticsResponseDto> list;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public int getFinishCount() {
        return finishCount;
    }

    public void setFinishCount(int finishCount) {
        this.finishCount = finishCount;
    }

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

    public List<SplitStatisticsResponseDto> getList() {
        return list;
    }

    public void setList(List<SplitStatisticsResponseDto> list) {
        this.list = list;
    }
}
