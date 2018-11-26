package com.yunnex.ops.erp.common.persistence.dialect.db;
import java.util.Locale;

import com.yunnex.ops.erp.common.persistence.dialect.Dialect;

/**
 * DB2的分页数据库方言实现
 *
 * @author poplar.yfyang
 * @version 1.0 2010-10-10 下午12:31
 * @since JDK 1.5
 */
public class DB2Dialect implements Dialect {
    
    public static final int DEFAULT_ROW_NUMBER = 50;
    public static final int DEFAULT_SQL_LENGTH = 100;
    
    @Override
    public boolean supportsLimit() {
        return true;
    }

    private static String getRowNumber(String sql) {
        StringBuilder rownumber = new StringBuilder(DEFAULT_ROW_NUMBER)
                .append("rownumber() over(");

        int orderByIndex = sql.toLowerCase(Locale.US).indexOf("order by");

        if (orderByIndex > 0 && !hasDistinct(sql)) {
            rownumber.append(sql.substring(orderByIndex));
        }

        rownumber.append(") as rownumber_,");

        return rownumber.toString();
    }

    private static boolean hasDistinct(String sql) {
        return sql.toLowerCase(Locale.US).contains("select distinct");
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
     * @param sql               实际SQL语句
     * @param offset            分页开始纪录条数
     * @param offsetPlaceholder 分页开始纪录条数－占位符号
     * @param limitPlaceholder  分页纪录条数占位符号
     * @return 包含占位符的分页sql
     */
    public String getLimitString(String sql, int offset, String offsetPlaceholder, String limitPlaceholder) {
        int startOfSelect = sql.toLowerCase(Locale.US).indexOf("select");

        StringBuilder pagingSelect = new StringBuilder(sql.length() + DEFAULT_SQL_LENGTH)
                .append(sql.substring(0, startOfSelect)) 
                .append("select * from ( select ") 
                .append(getRowNumber(sql));

        if (hasDistinct(sql)) {
            pagingSelect.append(" row_.* from ( ") 
                    .append(sql.substring(startOfSelect)) 
                    .append(" ) as row_");
        } else {
            pagingSelect.append(sql.substring(startOfSelect + 6)); 
        }

        pagingSelect.append(" ) as temp_ where rownumber_ ");

        if (offset > 0) {
            String endString = offsetPlaceholder + "+" + limitPlaceholder;
            pagingSelect.append("between ").append(offsetPlaceholder)
                    .append("+1 and ").append(endString);
        } else {
            pagingSelect.append("<= ").append(limitPlaceholder);
        }

        return pagingSelect.toString();
    }
}
