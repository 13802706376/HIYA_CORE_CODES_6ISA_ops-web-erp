package com.yunnex.ops.erp.modules.diagnosis.entity;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunnex.ops.erp.common.persistence.DataEntity;
import com.yunnex.ops.erp.modules.sys.entity.Dict;

/**
 * 经营诊断的门店的营业时间Entity
 * @author yunnex
 * @version 2018-03-29
 */
public class DiagnosisStoreBusinessHour extends DataEntity<DiagnosisStoreBusinessHour> implements Cloneable {
    private static final Logger LOGGER = LoggerFactory.getLogger(DiagnosisStoreBusinessHour.class);

    private static final int SIXTY = 60;
    private static final int TEN = 10;

    private static final long serialVersionUID = 1L;
    private String storeInfoId; // ERP门店ID
    private String workdays; // 工作日
    // 开始时间（单位秒，例：10:30 =》 10*60*60+30*60）
    private Integer startTime;
    // 结束时间（单位秒【结束时间不一定大于开始时间】，例：10:30 =》 10*60*60+30*60）
    private Integer endTime;
    // 营业时间类型（normal: 正常营业时间；peak: 高峰营业时间）
    private String businessType;

    /**
     * 开始时间，HH:mm格式显示
     */
    private String startTimeStr;

    /**
     * 结束时间，HH:mm格式显示
     */
    private String endTimeStr;

    private List<Dict> workdayList; // 工作日

    public DiagnosisStoreBusinessHour() {
        super();
    }

    public DiagnosisStoreBusinessHour(String id) {
        super(id);
    }

    /**
     * 把时间转换成HH:mm格式
     *
     * @param time 时间
     * @return
     * @date 2018年1月16日
     * @author zhangjl
     */
    public static String format2HHmm(Integer time) {
        if (null == time || time < 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        int scale = SIXTY;
        int hour = time / (scale * scale);
        if (hour < TEN) {
            sb.append("0");
        }
        sb.append(hour);
        sb.append(":");

        int minute = (time - hour * scale * scale) / scale;
        if (minute < TEN) {
            sb.append("0");
        }
        sb.append(minute);

        return sb.toString();
    }

    /**
     * 把时间转换成整数格式
     *
     * @param timeHHmm 时间（HH:mm格式）
     * @return
     * @date 2018年1月16日
     * @author zhangjl
     */
    public static Integer format2Int(String timeHHmm) {
        try {
            if (StringUtils.isEmpty(timeHHmm)) {
                LOGGER.warn("时间转换成整数格式，时间为空", timeHHmm);
                return -1;
            }
            String[] split = timeHHmm.split(":");

            int scale = SIXTY;
            return Integer.parseInt(split[0]) * scale * scale + Integer.parseInt(split[1]) * scale;
        } catch (Exception e) { // NOSONAR
            LOGGER.error("时间转换成整数格式异常", e);
        }
        return -1;
    }

    @Length(min = 1, max = 20, message = "工作日（例：周一，周日 =》 ,1,7,）长度必须介于 1 和 20 之间")
    public String getWorkdays() {
        return workdays;
    }

    public String getStoreInfoId() {
        return storeInfoId;
    }

    public void setStoreInfoId(String storeInfoId) {
        this.storeInfoId = storeInfoId;
    }

    public void setWorkdays(String workdays) {
        this.workdays = workdays;
    }

    @NotNull(message = "开始时间（单位秒，例：10:30 =》 10*60*60+30*60）不能为空")
    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    @NotNull(message = "结束时间（单位秒【结束时间不一定大于开始时间】，例：10:30 =》 10*60*60+30*60）不能为空")
    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    @Length(min = 1, max = 20, message = "营业时间类型（normal: 正常营业时间；peak: 高峰营业时间）长度必须介于 1 和 20 之间")
    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getStartTimeStr() {
        return startTimeStr;
    }

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
    }

    public String getEndTimeStr() {
        return endTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }

    public List<Dict> getWorkdayList() {
        return workdayList;
    }

    public void setWorkdayList(List<Dict> workdayList) {
        this.workdayList = workdayList;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public DiagnosisStoreBusinessHour clone() { // NOSONAR
        DiagnosisStoreBusinessHour businessHour = null;
        try {
            businessHour = (DiagnosisStoreBusinessHour) super.clone();
        } catch (CloneNotSupportedException e) {
            LOGGER.error("营业时间克隆异常!", e);
        }
        return businessHour;
    }
}
