package com.yunnex.ops.erp.common.persistence;

import java.util.List;

/**
 * 分页结果
 * 
 * @param <T>
 */
public class Pager<T> {

    private List<T> data; // 分页列表数据
    private Integer pageNo; // 当前页码
    private Integer pageSize;

    private Long total;

    private Boolean isPage = true; // 是否分页

    private static final Integer DEFAULT_PAGE_NO = 1;
    private static final Integer DEFAULT_PAGE_SIZE = 10;

    public Pager() {}

    public Pager(List<T> data, Long total) {
        this.data = data;
        this.total = total;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Integer getPageNo() {
        if (this.pageNo == null) {
            this.pageNo = DEFAULT_PAGE_NO;
        }
        return this.pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        if (this.pageSize == null) {
            this.pageSize = DEFAULT_PAGE_SIZE;
        }
        return this.pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Boolean getPage() {
        return isPage;
    }

    public void setPage(Boolean page) {
        isPage = page;
    }

    public int getFirstResult() {
        return (getPageNo() - 1) * getPageSize();
    }

    public int getMaxResults() {
        return getPageSize();
    }

}
