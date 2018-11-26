/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yunnex.ops.erp.common.persistence.dialect.db;

import com.yunnex.ops.erp.common.persistence.dialect.Dialect;

/**
 * Oracle的方言实现
 * @author poplar.yfyang
 * @version 1.0 2010-10-10 下午12:31
 * @since JDK 1.5
 */
public class OracleDialect implements Dialect {
    
    public static final int DEFAULT_SQL_LENGTH_DEC = 11;
    public static final int DEFAULT_SQL_LENGTH_ADD = 100;
    
    @Override
    public boolean supportsLimit() {
        return true;
    }

    @Override
    public String getLimitString(String sql, int offset, int limit) {
        return getLimitString(sql, offset, Integer.toString(offset), Integer.toString(limit));
    }

    /**
     * 将sql变成分页sql语句,提供将offset及limit使用占位符号(placeholder)替换.
     * <pre>
     * 如mysql
     * dialect.getLimitString("select * from user", 12, ":offset",0,":limit") 将返回
     * select * from user limit :offset,:limit
     * </pre>
     *
     * @param sqlInput               实际SQL语句
     * @param offset            分页开始纪录条数
     * @param offsetPlaceholder 分页开始纪录条数－占位符号
     * @param limitPlaceholder  分页纪录条数占位符号
     * @return 包含占位符的分页sql
     */
    public String getLimitString(String sqlInput, int offset, String offsetPlaceholder, String limitPlaceholder) {
        String sql= sqlInput.trim();
        boolean isForUpdate = false;
        if (sql.toLowerCase().endsWith(" for update")) {
            sql = sql.substring(0, sql.length() - DEFAULT_SQL_LENGTH_DEC);
            isForUpdate = true;
        }
        StringBuilder pagingSelect = new StringBuilder(sql.length() + DEFAULT_SQL_LENGTH_ADD);

        if (offset > 0) {
            pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
        } else {
            pagingSelect.append("select * from ( ");
        }
        pagingSelect.append(sql);
        if (offset > 0) {
            String endString = offsetPlaceholder + "+" + limitPlaceholder;
            pagingSelect.append(" ) row_ where rownum <= "+endString+") where rownum_ > ").append(offsetPlaceholder);
        } else {
            pagingSelect.append(" ) where rownum <= "+limitPlaceholder);
        }

        if (isForUpdate) {
            pagingSelect.append(" for update");
        }

        return pagingSelect.toString();
    }

}
