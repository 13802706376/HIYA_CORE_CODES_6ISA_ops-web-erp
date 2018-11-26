package com.yunnex.ops.erp.modules.holiday.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.common.constant.CommonConstants;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.common.utils.DateUtils;
import com.yunnex.ops.erp.modules.holiday.dao.ErpHolidaysDao;
import com.yunnex.ops.erp.modules.holiday.entity.ErpHolidays;

/**
 * 节假日配置Service
 * @author pch
 * @version 2017-11-02
 */
@Service
public class ErpHolidaysService extends CrudService<ErpHolidaysDao, ErpHolidays> {
    
    private static final int NUMBER_MILSEC = 1000;
    private static final int NUMBER_SEC = 60;
    private static final int NUMBER_MIN = 60;
    private static final int NUMBER_HOU = 24;
    
    /** 天数的临界值 */
    private static final int CRITICALITY_DAYS = 50;
    /** 小于等于临界值取值天数 */
    private static final int LEFT_CRITICALITY_DAYS = CRITICALITY_DAYS * 2;

    /** 最大递归次数 int的取值区间 -2^31~(2^31-1) 假期平均值小于 （1/2 ,2天休息1天）,所以最大递归次数不会大于31次 */
    private static final int MAX_RECURSION_TIMES = 31;

    //每天的毫秒数  
    private static final long MILSEC_PER_DAY = new Long((long) NUMBER_MILSEC*NUMBER_SEC*NUMBER_MIN*NUMBER_HOU);
    
    @Override
    @Transactional(readOnly = false)
    public void save(ErpHolidays erpHolidays) {
        super.save(erpHolidays);
    }
    @Override
    @Transactional(readOnly = false) 
    public void delete(ErpHolidays erpHolidays) {
        super.delete(erpHolidays);
    }
    
    public int getholiday(Date stardate, Date enddate,String del) {
        return dao.getholiday(stardate, enddate,del);

    }

    public int wheredate(String inputdate,String del) {
        return dao.wheredate(inputdate,del);
    }

    public Date enddate(Date stardates, Integer hourage) throws ParseException {//NOSONAR
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cld = Calendar.getInstance();
        Date stardate=starDate(stardates);
        Integer day = 0;
        Integer time = 0;
        if (hourage % 8 == 0) {
            day = hourage / 8;
            Date date = sdf.parse(sdf.format(stardate));
            cld.setTime(date);
            cld.add(Calendar.DATE, day);
            while(true){
            Integer count =getholiday(s.parse(s.format(stardate)), s.parse(s.format(cld.getTime())),Global.NO);
            if (count > 0) {
                cld.add(Calendar.DATE, 1);
                stardate=s.parse(s.format(cld.getTime()));
                cld.add(Calendar.DATE, count-1);
            }else{
                break;
            }
            }
            return cld.getTime();

        } else {
            day = hourage / 8;
            time = hourage - day * 8;
            Date date = sdf.parse(sdf.format(stardate));
            cld.setTime(date);
            cld.add(Calendar.DATE, day);
            while(true){
            Integer count = getholiday(s.parse(s.format(stardate)), s.parse(s.format(cld.getTime())),Global.NO);
            if (count > 0) {
                cld.add(Calendar.DATE, 1);
                stardate=s.parse(s.format(cld.getTime()));
                cld.add(Calendar.DATE, count-1);
            }else{
                break;
            }
            }

            // 开始时间
            Date startimeHour = cld.getTime();
            // 用来判断最开始时间是否超过了中午休息时间段
            Date affterdutytime = sdf.parse(s.format(cld.getTime()) + " 13:30:00");
            if (startimeHour.getTime() < affterdutytime.getTime()) {
                // 先加上hourage余下的小时数
                cld.add(Calendar.HOUR, time);
                // 中午下班时间
                Date offdutytime = sdf.parse(s.format(cld.getTime()) + " 12:00:00");
                // 最后得出时间，用来判断是否超过12点
                Date endtimeHour = sdf.parse(sdf.format(cld.getTime()));
                if (endtimeHour.getTime() > offdutytime.getTime()) {
                    cld.add(Calendar.HOUR, 1);
                    cld.add(Calendar.MINUTE, 30);
                }
                // 得出累加完中午休息的一个半小时最后时间
                Date endaffterdutytime = cld.getTime();
                // 用来判断最后时间是否超过当天下班时间
                Date nightdutytime = sdf.parse(s.format(cld.getTime()) + " 18:30:00");
                if (endaffterdutytime.getTime() > nightdutytime.getTime()) {
                    cld.add(Calendar.HOUR, 14);
                    cld.add(Calendar.MINUTE, 30);
                }

            } else {
                // 先加上hourage余下的小时数
                cld.add(Calendar.HOUR, time);
                Date endaffterdutytime = cld.getTime();
                // 用来判断最后时间是否超过当天下班时间
                Date nightdutytime = sdf.parse(s.format(cld.getTime()) + " 18:30:00");
                if (endaffterdutytime.getTime() > nightdutytime.getTime()) {
                    cld.add(Calendar.HOUR, 14);
                    cld.add(Calendar.MINUTE, 30);
                }
            }
            while(true){
                Integer count = getholiday(s.parse(s.format(stardate)), s.parse(s.format(cld.getTime())),Global.NO);
                if (count > 0) {
                    cld.add(Calendar.DATE, 1);
                    stardate=s.parse(s.format(cld.getTime()));
                    cld.add(Calendar.DATE, count-1);
                }else{
                    break;
                }
            }
            return cld.getTime();
        }
    }
    
    
    public Date starDate(Date stardate) throws ParseException{    
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date result=stardate;
        Calendar weekend = Calendar.getInstance();
        weekend.setTime(stardate);
        while(true){
            Integer c=wheredate(s.format(weekend.getTime()),Global.NO);
            if(c>0){
                weekend.add(Calendar.DATE, 1);
                result=sdf.parse(s.format(weekend.getTime())+" 9:00:00");                
            }else{
                break;
            }
        }
        Calendar begin=Calendar.getInstance();//获取当前时间
        begin.setTime(stardate);
        Calendar noonstart=(Calendar) begin.clone();//复制
        Calendar noonend=(Calendar) begin.clone();//复制
        noonstart.set(Calendar.HOUR_OF_DAY,12);//将一个时间设为当前12:00
        noonstart.set(Calendar.MINUTE, 0);
        noonstart.set(Calendar.SECOND, 0);
        noonend.set(Calendar.HOUR_OF_DAY, 13);//将第二个时间设为当前13:30
        noonend.set(Calendar.MINUTE, 30);
        noonend.set(Calendar.SECOND, 0);
        //18:30-00:00:00
        Calendar evenstart=(Calendar) begin.clone();//复制
        Calendar evenend=(Calendar) begin.clone();//复制
        evenstart.set(Calendar.HOUR_OF_DAY,18);
        evenstart.set(Calendar.MINUTE, 30);
        evenstart.set(Calendar.SECOND, 0);
        evenend.add(Calendar.DATE, 1);
        evenend.set(Calendar.HOUR_OF_DAY, 0);
        evenend.set(Calendar.MINUTE, 0);
        evenend.set(Calendar.SECOND, 0);
        //00:00:00-9:00:00
        Calendar weestart=(Calendar) begin.clone();//复制
        Calendar weeend=(Calendar) begin.clone();//复制
        weestart.set(Calendar.HOUR_OF_DAY,0);
        weestart.set(Calendar.MINUTE, 0);
        weestart.set(Calendar.SECOND, 0);
        weeend.set(Calendar.HOUR_OF_DAY, 9);
        weeend.set(Calendar.MINUTE, 0);
        weeend.set(Calendar.SECOND, 0);
        if(begin.after(noonstart)&&begin.before(noonend)){
            result=sdf.parse(s.format(begin.getTime())+" 13:30:00");    
        }else if(begin.after(evenstart)&&begin.before(evenend)){
            begin.add(Calendar.DATE, 1);
            result=sdf.parse(s.format(begin.getTime())+" 9:00:00");
        }else if(begin.after(weestart)&&begin.before(weeend)){
            result=sdf.parse(s.format(begin.getTime())+" 9:00:00");
        }
        return result;
    }
    
    /** 
     * 获取截止到现在时间工作总时长 
     * @param time 
     * @return 
     */  

     SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");//NOSONAR
        
        //设置上班时间：该处时间可以根据实际情况进行调整  
        int abh = 9;//NOSONAR
        int abm = 30;//NOSONAR
        int aeh = 12;//NOSONAR
        int aem = 30;//NOSONAR
        int pbh = 14;//NOSONAR
        int pbm = 0;//NOSONAR
        int peh = 19;//NOSONAR
        int pem = 0;//NOSONAR
      
        float h1 = abh+(float)abm/60;  //NOSONAR
        float h2 = aeh+(float)aem/60;  //NOSONAR
        float h3 = pbh+(float)pbm/60;  //NOSONAR
        float h4 = peh+(float)pem/60;  //NOSONAR
      
        float hoursPerDay = h2-h1+h4-h3;//NOSONAR
      
        int daysPerWeek = 5;//NOSONAR
        float hoursPerWeek = hoursPerDay*daysPerWeek;//NOSONAR
      
        public float calculateHours(Date beginTime, Date endTime){  
      
            //对输入的字符串形式的时间进行转换  
        Date t1 = (Date) beginTime.clone();// 真实开始时间
            Date t2 = endTime;//真实结束时间  
      
      
            //对时间进行预处理  
            t1 = processBeginTime(t1);  
            t2 = processEndTime(t2);  
              
            //若开始时间晚于结束时间，返回0  
            if(t1.getTime()>t2.getTime()){  
                return 0;  
            }  
              
            //开始时间到结束时间的完整星期数  
            int weekCount = (int) ((t2.getTime()-t1.getTime())/(MILSEC_PER_DAY*7));  
            logger.debug("时间间隔内共包含了"+weekCount+"个完整的星期");  
              
            float totalHours = 0;  
            totalHours += weekCount * hoursPerWeek;  
      
            //调整结束时间，使开始时间和结束时间在一个星期的周期之内  
            t2.setTime(t2.getTime()-weekCount*7*MILSEC_PER_DAY);  
            logger.debug("结束时间调整为："+printDate(t2));  
              
            int dayCounts = 0;//记录开始时间和结束时间之间工作日天数  
              
            //调整开始时间，使得开始时间和结束时间在同一天，或者相邻的工作日内。  
            while(t1.getTime()<=t2.getTime()){  
                Date temp = new Date(t1.getTime()+MILSEC_PER_DAY);  
                temp = processBeginTime(temp);  
                temp.setHours(t1.getHours());  
                temp.setMinutes(t1.getMinutes());  
                if(temp.getTime()>t2.getTime()){  
                    break;  
                }else{  
                    t1 = temp;  
                    dayCounts++;  
                }  
            }  
            totalHours += dayCounts * hoursPerDay;  
              
            float hh1 = t1.getHours() + (float)t1.getMinutes()/60;  
            float hh2 = t2.getHours() + (float)t2.getMinutes()/60;  
              
            //处理开始结束是同一天的情况  
            if(t1.getDay()==t2.getDay()){  
                float tt = 0;  
                tt = hh2 - hh1;  
                if(hh1>=h1&&hh1<=h2&&hh2>=h3){  
                    tt = tt - (h3-h2);  
                }  
                totalHours += tt;  
            }else{  
                //处理开始结束不是同一天的情况  
                float tt1 = h4 - hh1;  
                float tt2 = hh2 - h1;  
                if(hh1<=h2){  
                    tt1 = tt1 - (h3-h2);  
                }  
                if(hh2>=h3){  
                    tt2 = tt2 - (h3-h2);  
                }  
                totalHours += (tt1 + tt2);  
            }  
              
            //logger.debug("总工作时间为："+totalHours+"小时");  
            return totalHours;  
        }  
      
        /** 
         * 格式化输出时间： yyyy-mm-dd hh:mm:ss 星期x 
         * @param t 
         * @return 
         */  
        private String printDate(Date t) {  
            String str;  
            String xingqi = null;  
            switch (t.getDay()) {  
            case 0:  
                xingqi = "星期天";  
                break;  
            case 1:  
                xingqi = "星期一";  
                break;  
            case 2:  
                xingqi = "星期二";  
                break;  
            case 3:  
                xingqi = "星期三";  
                break;  
            case 4:  
                xingqi = "星期四";  
                break;  
            case 5:  
                xingqi = "星期五";  
                break;  
            case 6:  
                xingqi = "星期六";  
                break;  
            default:  
                break;  
            }  
            str = format.format(t)+"  "+xingqi;  
            return str;  
        }  
      
        /** 
         * 对结束时间进行预处理，使其处于工作日内的工作时间段内 
         * @param t 
         * @return 
         */  
        private Date processEndTime(Date t) {  
      
            float h = t.getHours() + (float)t.getMinutes()/60;  
      
            //若结束时间晚于下午下班时间，将其设置为下午下班时间  
            if(h>=h4){  
                t.setHours(peh);  
                t.setMinutes(pem);  
            }else {  
                //若结束时间介于中午休息时间，那么设置为上午下班时间  
                if(h>=h2&&h<=h3){  
                    t.setHours(aeh);  
                    t.setMinutes(aem);  
                }else{  
                    //若结束时间早于上午上班时间，日期向前推一天，并将时间设置为下午下班时间  
                    if(t.getHours()<=h1){  
                        t.setTime(t.getTime()-MILSEC_PER_DAY);  
                        t.setHours(peh);  
                        t.setMinutes(pem);  
                    }  
                }  
            }  
      
            //若结束时间是周末，那么将结束时间向前推移到最近的工作日的下午下班时间  
            if(t.getDay()==0||t.getDay()==6){  
                t.setTime(t.getTime()-MILSEC_PER_DAY*(t.getDay()==6?1:2));  
                t.setHours(peh);  
                t.setMinutes(pem);  
            }  
              
            return t;  
        }  
      
        /** 
         * 对开始时间进行预处理 
         * @param t1 
         * @return 
         */  
        private Date processBeginTime(Date t) {  
      
            float h = t.getHours() + (float)t.getMinutes()/60;  
      
            //若开始时间晚于下午下班时间，将开始时间向后推一天  
            if(h>=h4){  
                t.setTime(t.getTime()+MILSEC_PER_DAY);  
                t.setHours(abh);  
                t.setMinutes(abm);  
            }else {  
                //若开始时间介于中午休息时间，那么设置为下午上班时间  
                if(h>=h2&&h<=h3){  
                    t.setHours(pbh);  
                    t.setMinutes(pbm);  
                }else{  
                    //若开始时间早于上午上班时间，将hour设置为上午上班时间  
                    if(t.getHours()<=h1){  
                        t.setHours(abh);  
                        t.setMinutes(abm);  
                    }  
                }  
            }  
      
            //若开始时间是周末，那么将开始时间向后推移到最近的工作日的上午上班时间  
            if(t.getDay()==0||t.getDay()==6){  
                t.setTime(t.getTime()+MILSEC_PER_DAY*(t.getDay()==6?2:1));  
                t.setHours(abh);  
                t.setMinutes(abm);  
            }  
            return t;  
    }

    /**
     * 获取 开始时间之后的 days 个 工作日的日期
     *
     * @param start
     * @param days
     * @return
     * @date 2018年5月30日
     * @author linqunzhi
     */
    public Date getWorkDay(Date startTime, int days) {
        String startTimeStr = DateUtils.formatDateTime(startTime);
        logger.info("getWorkDay start | startTime={}|days={}",startTimeStr , days);
        if (days < 0) {
            logger.info("不支持负数计算！days={}", days);
            throw new ServiceException(CommonConstants.FailMsg.DATA);
        }
        Date result = getWorkDay(startTime, days, CRITICALITY_DAYS, LEFT_CRITICALITY_DAYS, MAX_RECURSION_TIMES);
        String resultStr = DateUtils.formatDateTime(result);
        logger.info("getWorkDay end | result={}", resultStr);
        return result;

    }

    private Date getWorkDay(Date startTime, int days, int criticalityDays, int leftCriticalityDays, int maxRecursionTimes) {
        // 0天或时间为空 不计算
        if (days == 0 || startTime == null) {
            return startTime;
        }
        if (maxRecursionTimes <= 0) {
            logger.info("递归次数已使用完，假期数据有异常!maxRecursionTimes={}", maxRecursionTimes);
            throw new ServiceException(CommonConstants.FailMsg.DATA);
        }
        maxRecursionTimes--;
        Calendar cal = Calendar.getInstance();
        cal.setTime(startTime);
        if (days <= criticalityDays) {
            // 如果小于临界天数 ，则查询 开始时间到 leftCriticalityDays 之间的所有假期，进行计算
            cal.add(Calendar.DAY_OF_YEAR, leftCriticalityDays);
            // 查询中间的假期集合
            List<ErpHolidays> list = dao.findByStartEnd(startTime, cal.getTime());
            if (CollectionUtils.isEmpty(list)) {
                // 如果期间没假期，则返回 startTime 加上 days
                cal.setTime(startTime);
                cal.add(Calendar.DAY_OF_YEAR, days);
                return cal.getTime();
            } else {
                // 假期map
                Map<String, String> holidaysMap = getHolidaysMap(list);
                Calendar calAdd = Calendar.getInstance();
                calAdd.setTime(startTime);
                String calAddDateStr = null;
                //循环增加天数，直到days = 0 或循环结束
                for (int i = 0; i < leftCriticalityDays; i++) {
                    calAdd.add(Calendar.DAY_OF_YEAR, 1);
                    calAddDateStr = DateUtils.formatDate(calAdd.getTime(), DateUtils.YYYY_MM_DD);
                    if (holidaysMap.get(calAddDateStr) == null) {
                        // 如果当天不是假期 ，则天数减1
                        days--;
                    }
                    if (days <= 0) {
                        break;
                    }
                }
                // 如果剩余天数大于0，继续执行该方法
                if(days > 0 ) {
                   return  getWorkDay(calAdd.getTime(), days, criticalityDays, leftCriticalityDays, maxRecursionTimes);
                } else {
                    return calAdd.getTime();
                }
            }
        } else {// 如果天数大于临界天数
            cal.add(Calendar.DAY_OF_YEAR, days);
            // 获取假期总天数
            int count = dao.getCountByStartEnd(startTime, cal.getTime());
            if (count == 0) {
                return cal.getTime();
            } else {
                return getWorkDay(cal.getTime(), count, criticalityDays, leftCriticalityDays, maxRecursionTimes);
            }
        }
    }

    /**
     * 获取假期map
     *
     * @param list
     * @return
     * @date 2018年6月1日
     * @author linqunzhi
     */
    private static Map<String, String> getHolidaysMap(List<ErpHolidays> list) {
        Map<String, String> result = new HashMap<>();
        if (CollectionUtils.isEmpty(list)) {
            return result;
        }
        for (ErpHolidays day : list) {
            String holidayStr = DateUtils.formatDate(day.getHolidayDate(), DateUtils.YYYY_MM_DD);
            if(holidayStr != null) {
                result.put(holidayStr, holidayStr);
            }
        }
        return result;
    }
}