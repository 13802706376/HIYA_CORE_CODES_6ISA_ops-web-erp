package com.yunnex.ops.erp.modules.workflow.flow.strategy;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunnex.ops.erp.modules.holiday.service.ErpHolidaysService;
import com.yunnex.ops.erp.modules.workflow.flow.dto.FlowInfoTask;

/**
 * 聚引客流程操作
 * 
 * @author Ejon
 * @date 2018年7月10日
 */
@Service
public class JykProcessOperate implements IProcOperate {

    protected Logger logger = LoggerFactory.getLogger(JykProcessOperate.class);


    @Autowired
    private ErpHolidaysService erpHolidaysService;

    @Override
    public void operate(FlowInfoTask task) {

        try {
            task.setTaskEndDate(erpHolidaysService.enddate(task.getTaskStartDate(), task.getTaskHour()));
            task.setTaskConsumTime(computingTaskSchedule(task.getTaskStartDate(), task.getTaskHour()));
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private Integer computingTaskSchedule(Date staterDate, Integer taskHours) {
        BigDecimal startDateLong = BigDecimal.valueOf(erpHolidaysService.calculateHours(staterDate, new Date()))
                        .multiply(BigDecimal.valueOf(60 * 60 * 1000));
        taskHours = taskHours == 0 ? 1 : taskHours;
        double taskHoursLong = (taskHours * 60 * 60 * 1000);
        return startDateLong.divide(BigDecimal.valueOf(taskHoursLong), 2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)).intValue();
    }

}
