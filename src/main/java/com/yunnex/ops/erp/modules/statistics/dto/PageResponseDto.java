package com.yunnex.ops.erp.modules.statistics.dto;

import java.util.List;

import com.yunnex.ops.erp.common.persistence.ResponseDto;

/**
 * 分页数据 responseDto
 * 
 * @author linqunzhi
 * @date 2018年5月28日
 * @param <T>
 */
public class PageResponseDto<T> extends ResponseDto<T> {

    private static final long serialVersionUID = 8509102981870916269L;

    /** 总条数 */
    private long count;

    /** 列表结果集 */
    private List<T> list;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

}
