package com.yunnex.ops.erp.common.persistence;

import yunnex.common.core.dto.BaseDto;

/**
 * 请求 dto
 * 
 * @author linqunzhi
 * @date 2018年5月8日
 * @param <T>
 */
public class RequestDto<T> extends BaseDto {

    private static final long serialVersionUID = -5081814385782192340L;

    /** 分页 */
    private Page<T> page;

    public Page<T> getPage() {
        return page;
    }

    public void setPage(Page<T> page) {
        this.page = page;
    }



}
