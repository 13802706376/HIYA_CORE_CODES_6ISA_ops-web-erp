package com.yunnex.ops.erp.modules.sys.entity;

public class SqlEntity {
	private String sqlParam;
	private int curPage;
	private int pageSize;

	public String getSqlParam() {
		return sqlParam;
	}

	public void setSqlParam(String sqlParam) {
		this.sqlParam = sqlParam;
	}

	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	
}
