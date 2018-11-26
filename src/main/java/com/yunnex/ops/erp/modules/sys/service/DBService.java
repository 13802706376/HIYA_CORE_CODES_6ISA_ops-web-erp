package com.yunnex.ops.erp.modules.sys.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.yunnex.ops.erp.common.constant.CommonConstants;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.common.utils.StringUtils;

@Service
public class DBService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DBService.class);
    private static String TYPE_SELECT = "SELECT";
    private static String TYPE_UPDATE = "UPDATE";
    private static String TYPE_DELETE = "DELETE";
    private static String TYPE_INSERT = "INSERT";
    private static String TYPE_ALTER = "ALTER";

    // update、delete 最大操作数量
    private static final int MAX_SIZE = 20;


    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;

    public List<List<Map<String, Object>>> excuteSql(String sqls,int curPage,int pageSize) {
        LOGGER.info("excuteSql start | sqls={}", sqls);
        if (StringUtils.isBlank(sqls)) {
            return null;
        }
        List<List<Map<String, Object>>> result = new ArrayList<>();
        Connection conn = null;
        Statement st = null;
        // dml map
        Map<String, Object> dmlMap = null;
        // dml 结果集
        List<Map<String, Object>> dmlList = new ArrayList<>();
        try {
            conn = getConnection(url, username, password);
            conn.setAutoCommit(false);
            st = conn.createStatement();
            String upperSql = null;
            int whereIndex = -1;
            // query list
            List<Map<String, Object>> selectList = null;
            // 多个query list
            List<List<Map<String, Object>>> selectResult = new ArrayList<>();
            // 查询总数的sql
            String queryCountSql = null;
            // 查询数据列表的sql
            String querySql = null;
            String[] sqlArr = sqls.split(CommonConstants.Sign.SEMICOLON);
            for (String sql : sqlArr) {
                sql = convertSql(sql);
                if (StringUtils.isBlank(sql)) {
                    continue;
                }
                LOGGER.info("当前执行sql={}", sql);
                upperSql = sql.toUpperCase();
                whereIndex = upperSql.lastIndexOf(" WHERE ");
                // sql类型
                String sqlType = TYPE_SELECT;
                if (upperSql.startsWith(TYPE_UPDATE)) {
                    sqlType = TYPE_UPDATE;
                } else if (upperSql.startsWith(TYPE_DELETE)) {
                    sqlType = TYPE_DELETE;
                } else if (upperSql.startsWith(TYPE_INSERT)) {
                    sqlType = TYPE_INSERT;
                } else if (upperSql.startsWith(TYPE_ALTER)) {
                    sqlType = TYPE_ALTER;
                }
                // 如果 删除、修改、插入
                if (TYPE_UPDATE.equals(sqlType) || TYPE_INSERT.equals(sqlType) || TYPE_DELETE.equals(sqlType)) {
                    // 如果是update 或 delete
                    if (TYPE_UPDATE.equals(sqlType) || TYPE_DELETE.equals(sqlType)) {
                        // 必须包含 where 条件
                        if (whereIndex < 0) {
                            String message = new StringBuilder().append("DML 语句必须包含 where 条件|sql= {").append(sql).append("}").toString();
                            throw new ServiceException(message);
                        }
                        // 组装 查询总条数 和 查询数据列表 sql
                        if (TYPE_UPDATE.equals(sqlType)) {
                            int setIndex = upperSql.indexOf(" SET ");
                            queryCountSql = sql.replace(sql.substring(0, TYPE_UPDATE.length()), "SELECT count(*) FROM");
                            setIndex = queryCountSql.toUpperCase().indexOf(" SET ");
                            whereIndex = queryCountSql.toUpperCase().lastIndexOf(" WHERE ");
                            queryCountSql = queryCountSql.replace(queryCountSql.substring(setIndex, whereIndex), CommonConstants.Sign.SPACE);
                            querySql = sql.replace(sql.substring(0, TYPE_UPDATE.length()), "SELECT * FROM");
                            setIndex = querySql.toUpperCase().indexOf(" SET ");
                            whereIndex = querySql.toUpperCase().lastIndexOf(" WHERE ");
                            querySql = querySql.replace(querySql.substring(setIndex, whereIndex), CommonConstants.Sign.SPACE);
                        } else {
                            queryCountSql = sql.replace(sql.substring(0, TYPE_DELETE.length()), "SELECT count(*) ");
                            querySql = sql.replace(sql.substring(0, TYPE_DELETE.length()), "SELECT * ");
                        }
                        // 备份 update 和 delete 的数据
//                        backUpdateDeleteData(sql, queryCountSql, querySql, st);
                    }
                    int count = st.executeUpdate(sql);
                    dmlMap = new HashMap<>();
                    dmlMap.put("DML", new StringBuilder().append(sqlType).append(CommonConstants.Sign.SPACE).append(count)
                                    .append(" rows data | sql={").append(sql).append("}").toString());
                    dmlList.add(dmlMap);
                } else if(TYPE_ALTER.equals(sqlType)){
                	boolean count = st.execute(sql);
                	dmlMap = new HashMap<>();
                    dmlMap.put("ALTER", new StringBuilder().append(sqlType).append(CommonConstants.Sign.SPACE).append(!count)
                                    .append(" rows data | sql={").append(sql).append("}").toString());
                    dmlList.add(dmlMap);
                } else {
                    // 获取select 数据列表
                    selectList = selectSqlList(sql, st,curPage,pageSize);
                    if (CollectionUtils.isNotEmpty(selectList)) {
                        selectResult.add(selectList);
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(dmlList)) {
                result.add(dmlList);
            }
            if (CollectionUtils.isNotEmpty(selectResult)) {
                result.addAll(selectResult);
            }
            conn.commit();
        } catch (Exception e) {
            dmlMap = new HashMap<>();
            dmlMap.put("error", e.getMessage());
            dmlList = new ArrayList<>();
            dmlList.add(dmlMap);
            result = new ArrayList<>();
            result.add(dmlList);
            try {
                conn.rollback();
            } catch (SQLException e1) {
                LOGGER.error("回滚事务异常 ", e);
            }
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    LOGGER.error("关闭st异常 ", e);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    LOGGER.error("关闭conn异常 ", e);
                }
            }

        }
        LOGGER.info("excuteSql end | result.size={}", result.size());
        return result;
    }

    /**
     * 获取selet 数据
     *
     * @param sql
     * @param st
     * @return
     * @date 2018年5月21日
     * @author linqunzhi
     * @throws SQLException
     */
    private List<Map<String, Object>> selectSqlList(String sql, Statement st, int curPage, int pageSize) throws SQLException {
    	if(sql.indexOf("limit")==-1){
    		sql=sql+" limit "+curPage+","+pageSize;
    	}
        ResultSet rs = null;
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        try {
            rs = st.executeQuery(sql);
            ResultSetMetaData md = rs.getMetaData(); // 获得结果集结构信息,元数据
            int columnCount = md.getColumnCount(); // 获得列数
            while (rs.next()) {
                Map<String, Object> rowData = new LinkedHashMap<String, Object>();
                for (int i = 1; i <= columnCount; i++) {
                    rowData.put(md.getColumnLabel(i), rs.getObject(i));
                }
                result.add(rowData);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (rs != null) {
                // 关闭rs
                rs.close();
            }
        }
        return result;
    }

    /**
     * 备份 更新 删除 数据
     *
     * @param sql
     * @param queryCountSql
     * @param querySql
     * @param st
     * @throws SQLException
     * @date 2018年5月21日
     * @author linqunzhi
     */
    private void backUpdateDeleteData(String sql, String queryCountSql, String querySql, Statement st) throws SQLException {
        // 查询总条数
        ResultSet rs = null;
        // update、delete 备份list
        List<Map<String, Object>> backList = null;
        try {
            rs = st.executeQuery(queryCountSql);
            int queryCount = 0;
            while (rs.next()) {
                queryCount = rs.getInt(1);
            }
            if (queryCount > MAX_SIZE) {
                String message = new StringBuilder().append("DML 语句操作数据不能大于").append(MAX_SIZE).append("条|sql={").append(sql).append("}").toString();
                throw new ServiceException(message);
            }
            backList = selectSqlList(querySql, st, 0, MAX_SIZE);

        } catch (SQLException e) {
            throw e;
        } finally {
            // 关闭rs
            if (rs != null) {
                rs.close();
            }
        }
        LOGGER.info("操作前数据：{}", JSON.toJSONString(backList));
    }

    /**
     * 转换sql 1：去掉前后的空格 2：换行符 替换为空 3：# -- 开头 替换为空
     *
     * @param sql
     * @return
     * @date 2018年5月21日
     * @author linqunzhi
     */
    private String convertSql(String sql) {
        if (sql == null || sql.equals("") || sql.indexOf("#") != -1 || sql.indexOf("--") != -1) {
            return "";
        } else {
            return sql.replaceAll("\n", " ").replaceAll("^[　 ]+|[　 ]+$", " ").trim();
        }
    }


    /**
     * 连接数据库
     *
     * @param url
     * @param userName
     * @param password
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     * @date 2018年5月21日
     * @author linqunzhi
     */
    private Connection getConnection(String url, String userName, String password) throws ClassNotFoundException, SQLException {
        Connection conn = DriverManager.getConnection(url, userName, password);
        return conn;
    }

}
