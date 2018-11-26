package com.yunnex.ops.erp.modules.holiday.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.holiday.entity.ErpHolidays;

/**
 * 节假日配置DAO接口
 * 
 * @author pch
 * @version 2017-11-02
 */
@MyBatisDao
public interface ErpHolidaysDao extends CrudDao<ErpHolidays> {
    public Date enddate(Date stardate, Integer hourage);

    public int getholiday(@Param("stardate") Date stardate, @Param("enddate") Date enddate, @Param("del") String del);

    public int wheredate(@Param("inputdate") String inputdate, @Param("del") String del);

    /**
     * 根据开始时间和结束时间 获取假期列表（startTime< data <= endTime）
     *
     * @param startTime
     * @param endTime
     * @return
     * @date 2018年5月30日
     * @author linqunzhi
     */
    public List<ErpHolidays> findByStartEnd(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    /**
     * 根据开始时间和结束时间获取 假期总天数（startTime< data <= endTime）
     *
     * @param startTime
     * @param time
     * @return
     * @date 2018年5月31日
     * @author linqunzhi
     */
    public int getCountByStartEnd(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

}
