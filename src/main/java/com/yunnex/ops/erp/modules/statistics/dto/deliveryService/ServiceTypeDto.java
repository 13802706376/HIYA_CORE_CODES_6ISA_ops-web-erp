package com.yunnex.ops.erp.modules.statistics.dto.deliveryService;

import java.util.List;

import yunnex.common.core.dto.BaseDto;

/**
 * 服务类型
 * 
 * @author linqunzhi
 * @date 2018年5月28日
 */
public class ServiceTypeDto extends BaseDto {

    private static final long serialVersionUID = -6720103849649945606L;

    /** 包含的集合 */
    private List<Integer> includeList;

    /** 不包含的集合 */
    private List<Integer> notIncludeList;

    public List<Integer> getIncludeList() {
        return includeList;
    }

    public void setIncludeList(List<Integer> includeList) {
        this.includeList = includeList;
    }

    public List<Integer> getNotIncludeList() {
        return notIncludeList;
    }

    public void setNotIncludeList(List<Integer> notIncludeList) {
        this.notIncludeList = notIncludeList;
    }
}
