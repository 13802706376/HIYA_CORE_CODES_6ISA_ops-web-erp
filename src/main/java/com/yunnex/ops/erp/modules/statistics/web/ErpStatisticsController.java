package com.yunnex.ops.erp.modules.statistics.web;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.utils.excel.FastExcel;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.act.service.TaskExtService;
import com.yunnex.ops.erp.modules.good.entity.ErpGoodInfo;
import com.yunnex.ops.erp.modules.good.service.ErpGoodInfoService;
import com.yunnex.ops.erp.modules.order.dao.ErpOrderSplitInfoDao;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitInfo;
import com.yunnex.ops.erp.modules.order.entity.PromoteOrderSplit;
import com.yunnex.ops.erp.modules.order.service.ErpOrderSplitInfoService;
import com.yunnex.ops.erp.modules.order.service.ErpPromoteService;
import com.yunnex.ops.erp.modules.statistics.dto.SplitReportResponseDto;
import com.yunnex.ops.erp.modules.statistics.dto.SplitStatisticsAllResponseDto;
import com.yunnex.ops.erp.modules.statistics.dto.SplitStatisticsRequestDto;
import com.yunnex.ops.erp.modules.statistics.dto.SplitTeamMemberReportResponseDto;
import com.yunnex.ops.erp.modules.statistics.dto.SplitWeekAndMonthResponseDto;
import com.yunnex.ops.erp.modules.statistics.entity.ErpReport;
import com.yunnex.ops.erp.modules.statistics.entity.ErpStatistics;
import com.yunnex.ops.erp.modules.statistics.entity.ErpStatisticsApi;
import com.yunnex.ops.erp.modules.statistics.entity.ErpStatisticsSelected;
import com.yunnex.ops.erp.modules.statistics.entity.ErpTeamFollowOrder;
import com.yunnex.ops.erp.modules.statistics.entity.ErpWeekAndMonth;
import com.yunnex.ops.erp.modules.statistics.excelUtil.ExportExcelUtil;
import com.yunnex.ops.erp.modules.statistics.service.ErpStatisticsService;
import com.yunnex.ops.erp.modules.sys.entity.Role;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.yunnex.ops.erp.modules.sys.service.SystemService;
import com.yunnex.ops.erp.modules.sys.service.UserService;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;
import com.yunnex.ops.erp.modules.team.entity.ErpTeam;
import com.yunnex.ops.erp.modules.team.entity.ErpTeamUser;
import com.yunnex.ops.erp.modules.team.service.ErpTeamService;
import com.yunnex.ops.erp.modules.team.service.ErpTeamUserService;
import com.yunnex.ops.erp.modules.workflow.user.entity.ErpOrderFlowUser;
import com.yunnex.ops.erp.modules.workflow.user.service.ErpOrderFlowUserService;

import yunnex.common.core.dto.ApiResult;

@Controller
@RequestMapping(value = "${adminPath}/statistics/erpStatisticsController")
public class ErpStatisticsController extends BaseController{
    @Autowired
    private ErpTeamUserService erpTeamUserService;
    @Autowired
    private ErpTeamService erpTeamService;
    @Autowired
    private ErpGoodInfoService goodService;
    @Autowired
    private ErpOrderFlowUserService ofuService;
    @Autowired
    private ErpOrderSplitInfoService splitService;
    @Autowired
    private UserService userService;
    @Autowired
    private TaskExtService taskExtService;
    @Autowired
    private ErpOrderSplitInfoDao erpOrderSplitInfoDao;
    @Autowired
    private SystemService systemService;
    @Autowired
    private ErpStatisticsService erpStatisticsService;
    @Autowired
    private ErpPromoteService erpPromoteService;

    /**
     * 团队统计跳转
     * 
     */
    @RequestMapping(value = "teamStatisticsUrl")
    public String teamStatistics(HttpServletRequest request, HttpServletResponse response, Model model) {
        Principal principal = UserUtils.getPrincipal();
        List<ErpTeamUser> teamuser = erpTeamUserService.findwhereuser(Global.NO, Global.YES, principal.getId());
        // 如果不是团队管理员,直接 抛出权限不足异常
        if (null==teamuser||teamuser.isEmpty()) {
            throw new AuthenticationException("你不是团队管理员，无法查看团队订单！");
        }
        return "modules/statistics/teamstatistics";
    }

    /**
     * 团队统计跳转-数据表
     * 
     */
    @RequestMapping(value = "teamReportUrl")
    public String teamReportUrl(HttpServletRequest request, HttpServletResponse response, Model model) {
        Principal principal = UserUtils.getPrincipal();
        List<ErpTeamUser> teamuser = erpTeamUserService.findwhereuser(Global.NO, Global.YES, principal.getId());
        // 如果不是团队管理员,直接 抛出权限不足异常
        if (null==teamuser||teamuser.isEmpty()) {
            throw new AuthenticationException("你不是团队管理员，无法查看团队订单！");
        }
        return "modules/statistics/teamreport";
    }

    /**
     * 个人统计跳转
     * 
     */
    @RequestMapping(value = "personalStatistics")
    public String personalStatistics(HttpServletRequest request, HttpServletResponse response, Model model) {
        return "modules/statistics/personalstatistics";
    }

    /**
     * 下拉框接口
     * 
     */
    @RequestMapping(value = "selected")
    @ResponseBody
    public ErpStatisticsSelected selected(HttpServletRequest request, HttpServletResponse response, Model model) {
        ErpStatisticsSelected selected = new ErpStatisticsSelected();
        Principal principal = UserUtils.getPrincipal();
        List<ErpTeam> team = new ArrayList<ErpTeam>();
        // 根据团队管理员id查询到其管理员所有的团队
        List<ErpTeamUser> teamuser = erpTeamUserService.findwhereuser(Global.NO, Global.YES, principal.getId());
        if (null!=teamuser&&!teamuser.isEmpty()) {
            ErpTeam eteam = null;
            for (int i = 0; i < teamuser.size(); i++) {
                eteam = erpTeamService.get(teamuser.get(i).getTeamId());
                if (eteam != null) {
                    team.add(eteam);
                }
            }
            selected.getTeam().addAll(team);
        }
        List<ErpGoodInfo> good=goodService.findwherecategory();
        selected.getGood().addAll(good);
        List<User> planningPersons = userService.getUserByRoleName(Constant.PLANNING_PERSON);
        selected.getPlanningPersons().addAll(planningPersons);
        return selected;
    }

    /**
     * 获取团队分单明细表
     * 
     * @throws ParseException
     * 
     */
    @RequestMapping(value = "teamstatistics")
    @ResponseBody
    public ErpStatisticsApi teamstatistics(String teamId, String orderNum, String shopName, String orderType, String starDate, String endDate,
                    HttpServletRequest request, HttpServletResponse response, Model model) throws ParseException {
        Principal principal = UserUtils.getPrincipal();
        ErpStatisticsApi api = erpStatisticsService.findTeamStatistics(principal.getId(), teamId, orderNum, shopName, orderType,
                        starDate + " 00:00:00", endDate + " 23:59:59");
        return api;
    }
    
    /**
     * 获取团队分单明细表
     * 
     * @throws ParseException
     * 
     */
    @RequestMapping(value = "teamstatistics2")
    @ResponseBody
    public ErpStatisticsApi teamstatistics2(String teamId, String orderNum, String shopName, String orderType, String starDate, String endDate,
                    HttpServletRequest request, HttpServletResponse response, Model model) throws ParseException {
        Principal principal = UserUtils.getPrincipal();
        ErpStatisticsApi api = erpStatisticsService.findTeamStatistics2(principal.getId(), teamId, orderNum, shopName, orderType,
                        starDate + " 00:00:00", endDate + " 23:59:59");
        return api;
    }
    
    
    /**
     * 获取团队分单明细表,针对执行中的任务存在超时的订单数
     * 
     * @throws ParseException
     * 
     */
    @RequestMapping(value = "overtimestatistics")
    @ResponseBody
    public ErpStatisticsApi overtimestatistics(String teamId, String orderNum, String shopName, String orderType, String starDate, String endDate,
                    HttpServletRequest request, HttpServletResponse response, Model model) throws ParseException {
        Principal principal = UserUtils.getPrincipal();
        ErpStatisticsApi api = erpStatisticsService.findOvertimeStatistics(principal.getId(), teamId, orderNum, shopName, orderType,
                        starDate + " 00:00:00", endDate + " 23:59:59");
        return api;
    }
    
    public String maxDate(String time1, String time2) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if ((null == time1 || "".equals(time1)) && (null == time2 || "".equals(time2))) {
            return "null";
        } else if ((null != time1 && !"".equals(time1)) && (null == time2 || "".equals(time2))) {
            return time1;
        } else if ((null == time1 || "".equals(time1)) && (null != time2 && !"".equals(time2))) {
            return time2;
        } else {
            Date d1 = format.parse(time1);
            Date d2 = format.parse(time2);
            if (d1.getTime() > d2.getTime()) {
                return time1;
            } else {
                return time2;
            }
        }

    }

    public Integer jfzq(String maxdate, String buydate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = format.parse(buydate);
        Date endDate = format.parse(maxdate);

        int result = 0;
        while (startDate.compareTo(endDate) <= 0) {
            if (startDate.getDay() != 6 && startDate.getDay() != 0) {
                result++;
                startDate.setDate(startDate.getDate() + 1);
            } else {
                startDate.setDate(startDate.getDate() + 1);
            }
        }
        return result;
    }


    /**
     * 导出
     * 
     * @throws IOException
     * @throws InvalidFormatException
     * 
     */
    @RequestMapping(value = "exportexcel")
    @ResponseBody
    public JSONObject exportexcel(String jsonObject, HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
        JSONObject resObject = new JSONObject();
        String newJson = StringEscapeUtils.unescapeHtml4(jsonObject);
        List<ErpStatistics> list = JSON.parseObject(newJson, new TypeReference<List<ErpStatistics>>() {});

        FastExcel.exportExcel(response, "订单统计导出", list);

        resObject.put("result", true);
        resObject.put("message", "导出成功");
        return resObject;

    }

    /**
     * 导出
     * @throws Exception 
     * 
     * @throws InvalidFormatException
     * 
     */
    @RequestMapping(value = "exportTogetherExcel")
    @ResponseBody
    public JSONObject exportTogetherExcel(String startDate,String endDate,String order_type, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
    	String sd=startDate.replaceAll("-", "").replaceAll(":","").replaceAll("/", "").replaceAll(" ", "");
    	String ed=endDate.replaceAll("-", "").replaceAll(":","").replaceAll("/", "").replaceAll(" ", "");
    	Principal principal = UserUtils.getPrincipal();
    	JSONObject resObject = new JSONObject();
        List<Map<String, Object>> dataset= null;
        List<Map<String, Object>> distinctData= null;
        List<PromoteOrderSplit> orderList=erpPromoteService.findPromoteOrder(principal.getId(),startDate,endDate,order_type);
        List<PromoteOrderSplit> splitList=erpPromoteService.findPromoteSplit(principal.getId(),startDate,endDate,order_type);
        if(orderList!=null){
        	distinctData=getDistinctDataset(orderList,splitList);
        	dataset=getDataset(orderList,splitList);
        }
        ExportExcelUtil eUtil=new ExportExcelUtil();
        String fileName="聚引客收入统计"+sd+"-"+ed;
        eUtil.exportTogetherExcel(response, fileName, "聚引客收入统计",dataset,distinctData);
        resObject.put("result", true);
        resObject.put("message", "导出成功");
        return resObject;
        
    }
    
    private static List<Map<String, Object>> getDistinctDataset(List<PromoteOrderSplit> orderList,List<PromoteOrderSplit> splitList){
    	List<Map<String, Object>> distincData= new ArrayList<Map<String, Object>>();
    	for(PromoteOrderSplit order:orderList){
    		Map<String, Object> map=new HashMap<String, Object>();
    		int rows=1;
    		String order_number=order.getOrderNumber();
    		String status=order.getPromoteStatus();
	    	for(PromoteOrderSplit split:splitList){
				String ordernumber=split.getOrderNumber();
    			if(order_number.equals(ordernumber)){
    				rows=split.getRowCount();
    			}
			}
			map.put("order_number", order_number);
			map.put("rows", rows==0?1:rows);
			distincData.add(map);
    	}
    	return distincData;
    }
    
    private static List<Map<String, Object>> getDataset(List<PromoteOrderSplit> orderList,List<PromoteOrderSplit> splitList){
    	List<Map<String, Object>> list=merginData(orderList,splitList);
    	List<Map<String, Object>> datas= new ArrayList<Map<String, Object>>();
    	for(Map<String, Object> m:list){
    		int run=0;
    		int runnull=0;
    		String order_number=(String) m.get("ordernumber");
    		String shop_name=(String) m.get("shop_name");
    		String buy_date=(String) m.get("buy_date");
    		String contact_number=(String) m.get("contact_number");
    		String order_type=(String) m.get("order_type");
    		String company_name=(String) m.get("company_name");
    		String agent_name=(String) m.get("agent_name");
    		String goodNames=(String) m.get("goodNames");
    		Double expenditurePyall=(Double) m.get("expenditurePy");
    		Double expenditureWball=(Double) m.get("expenditureWb");
    		Double expenditureMmall=(Double) m.get("expenditureMm");
    		for(PromoteOrderSplit split:splitList){
    			String orderNumber=split.getOrderNumber();
    			String promotionChannel=split.getPromotionChannel();
				String promoteStartDate=split.getPromoteStartDate();
				String promoteEndDate=split.getPromoteEndDate();
				String goodName=split.getGoodName();
				String status=split.getPromoteStatus();
				Double expenditurePy=split.getExpenditurePy();
				Double expenditureWb=split.getExpenditureWb();
				Double expenditureMm=split.getExpenditureMm();
				int rows=split.getRowCount();
    			if(order_number.equals(orderNumber)){
    				runnull++;
    				Map<String, Object> map=new HashMap<String, Object>();
    				map.put("orderNumber", order_number);
    				map.put("shopName", shop_name);
    				map.put("buyDate", buy_date);
    				map.put("contactNumber", contact_number);
    				map.put("orderType", order_type);
    				map.put("companyName", company_name);
    				map.put("agentName", agent_name);
    				map.put("goodNames", goodNames);
    				map.put("goodName", goodName);
    				map.put("promoteStatus", status);
    				map.put("promotionChannel", promotionChannel);
    				map.put("promoteStartDate", promoteStartDate);
    				map.put("promoteEndDate", promoteEndDate);
    				map.put("expenditurePy", expenditurePy);
    				map.put("expenditureWb", expenditureWb);
    				map.put("expenditureMm", expenditureMm);
    				map.put("expenditurePyall", expenditurePyall);
    				map.put("expenditureWball", expenditureWball);
    				map.put("expenditureMmall", expenditureMmall);
    				if(!StringUtils.isEmpty(status) && status.indexOf("running")==-1 && status.indexOf("notstart")==-1){
    					datas.add(map);
    					run++;
    				}
    				if(runnull==rows && run==0){
    					datas.add(map);
    					run++;
    				}
//    				datas.add(map);
    			}
    		}
    	}
    	return datas;
    }
    
    private static List<Map<String, Object>> merginData(List<PromoteOrderSplit> orderList,List<PromoteOrderSplit> splitList){
    	List<Map<String, Object>> distincData= new ArrayList<Map<String, Object>>();
    	for(PromoteOrderSplit order:orderList){
    		int rows=0;
    		String order_number=order.getOrderNumber();
    		String shop_name=order.getShopName();
    		String buy_date=order.getBuyDate();
    		String status=order.getPromoteStatus();
    		String contact_number=order.getContactNumber();
    		String order_type=order.getOrderType();
    		String company_name=order.getCompanyName();
    		String agent_name=order.getAgentName();
    		String goodNames=order.getGoodNames();
    		Double expenditurePy_=0.0;
			Double expenditureWb_=0.0;
			Double expenditureMm_=0.0;
    		for(PromoteOrderSplit split:splitList){
    			String ordernumber=split.getOrderNumber();
    			String promoteStatus=split.getPromoteStatus();
    			if(order_number.equals(ordernumber)){
    				int rowscount=split.getRowCount();
    				rows++;
    				String goodName=split.getGoodName();
    				Double expenditurePy=split.getExpenditurePy();
    				Double expenditureWb=split.getExpenditureWb();
    				Double expenditureMm=split.getExpenditureMm();
    				if(!StringUtils.isEmpty(status) && status.indexOf("running")==-1 && status.indexOf("notstart")==-1){
    					expenditurePy_+=expenditurePy;
        				expenditureWb_+=expenditureWb;
        				expenditureMm_+=expenditureMm;
    				}
//    				expenditurePy_+=expenditurePy;
//    				expenditureWb_+=expenditureWb;
//    				expenditureMm_+=expenditureMm;
    				Map<String, Object> map=new HashMap<String, Object>();
    				if(rows==rowscount){
    					map.put("ordernumber", ordernumber);
    					map.put("promoteStatus", status);
    					map.put("shop_name", shop_name);
    					map.put("buy_date", buy_date);
    					map.put("contact_number", contact_number);
    					map.put("order_type", order_type);
    					map.put("company_name", company_name);
    					map.put("agent_name", agent_name);
    					map.put("goodNames", goodNames);
    					map.put("expenditurePy", expenditurePy_);
        				map.put("expenditureWb", expenditureWb_);
        				map.put("expenditureMm", expenditureMm_);
        				distincData.add(map);
    				}
    			}
    		}
    	}
    	return distincData;
    }
    

    /**
     * 保存备注
     * 
     */
    @RequestMapping(value = "addremark")
    @ResponseBody
    public JSONObject addremark(String sid, String remark, HttpServletRequest request, HttpServletResponse response, Model model) {
        JSONObject resObject = new JSONObject();
        ErpOrderSplitInfo split = splitService.get(sid);
        if (null == split) {
            resObject.put("result", false);
            resObject.put("message", "增加备注失败");
            return resObject;
        }
        split.setRemark(remark);
        splitService.save(split);
        resObject.put("result", true);
        resObject.put("message", "增加备注成功");
        return resObject;
    }


    /**
     * 获取个人分单明细表
     * 
     * @throws ParseException
     * 
     */
    @RequestMapping(value = "userstatistics")
    @ResponseBody
    public ErpStatisticsApi userstatistics(String orderNum, String shopName, String orderType, String starDate, String endDate,
                    HttpServletRequest request, HttpServletResponse response, Model model) throws ParseException {
        Principal principal = UserUtils.getPrincipal();
        ErpStatisticsApi api = erpStatisticsService.findUserStatistics(principal.getId(), orderNum, shopName, orderType, starDate + " 00:00:00",
                        endDate + " 23:59:59");
        return api;
    }
    
    
    
    /**
     * 团队订单统计周月报表
     * 
     * @throws ParseException
     * 
     */
    @RequestMapping(value = "weekAndMonthOld")
    @ResponseBody
    public ErpWeekAndMonth weekAndMonth(String starDate, String endDate,String weekOrMonth,
                    HttpServletRequest request, HttpServletResponse response, Model model) throws ParseException {
        Principal principal = UserUtils.getPrincipal();
        ErpWeekAndMonth api = new ErpWeekAndMonth();
        List<String> sList=new ArrayList<String>();
        String time = "";
        Integer newCount = 0;
        Integer onlineCount = 0;
        Integer overTime=0;
        Integer onlineCountOvertime=0;
        double cycle=0.00;
        Integer cycleNum=0;
        List<ErpOrderFlowUser> ofu=new ArrayList<ErpOrderFlowUser>();
        List<ErpOrderFlowUser> ofuOnline=new ArrayList<ErpOrderFlowUser>();
        if("week".equals(weekOrMonth)){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance(); 
            Date date=new Date();
             cal.setTime(date);  
             // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了  
             int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天  
             if (1 == dayWeek) {  
                cal.add(Calendar.DAY_OF_MONTH, -1);  
             }
             // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一  
             cal.setFirstDayOfWeek(Calendar.MONDAY);  
             // 获得当前日期是一个星期的第几天  
             int day = cal.get(Calendar.DAY_OF_WEEK);  
             // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值  
             cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);  
             String imptimeBegin = sdf.format(cal.getTime());  
             cal.add(Calendar.DATE, 6);  
             String imptimeEnd = sdf.format(cal.getTime());
             api.setStarTime(imptimeBegin);
             api.setEndTime(imptimeEnd);
             ofu = ofuService.findstatisticsReport(principal.getId(), imptimeBegin + " 00:00:00",
                     imptimeEnd + " 23:59:59");
             ofuOnline = ofuService.findstatisticsReportOnline(principal.getId(), imptimeBegin + " 00:00:00",
                     imptimeEnd + " 23:59:59");
        }else if("month".equals(weekOrMonth)){
            Calendar cale =Calendar.getInstance(); 
            // 获取当月第一天和最后一天  
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
            String firstday, lastday;  
            // 获取前月的第一天  
            cale = Calendar.getInstance();  
            cale.add(Calendar.MONTH, 0);  
            cale.set(Calendar.DAY_OF_MONTH, 1);  
            firstday = format.format(cale.getTime());  
            // 获取前月的最后一天  
            cale = Calendar.getInstance();  
            cale.add(Calendar.MONTH, 1);  
            cale.set(Calendar.DAY_OF_MONTH, 0);  
            lastday = format.format(cale.getTime());
            api.setStarTime(firstday);
            api.setEndTime(lastday);
            ofu = ofuService.findstatisticsReport(principal.getId(), firstday + " 00:00:00",
                    lastday + " 23:59:59");
            ofuOnline = ofuService.findstatisticsReportOnline(principal.getId(), firstday + " 00:00:00",
                    lastday + " 23:59:59");
        }else{
            api.setStarTime(starDate);
            api.setEndTime(endDate);
            ofu = ofuService.findstatisticsReport(principal.getId(), starDate + " 00:00:00",
                    endDate + " 23:59:59");
            ofuOnline = ofuService.findstatisticsReportOnline(principal.getId(), starDate + " 00:00:00",
                    endDate + " 23:59:59");
        }
        newCount = ofu.size();
        if (null != ofu && !ofu.isEmpty()) {
            for (int i = 0; i < ofu.size(); i++) {
                sList.add(ofu.get(i).getPid());
            }

        }
        if (null != ofuOnline && !ofuOnline.isEmpty()) {
            for (int i = 0; i < ofuOnline.size(); i++) {
                Integer count = 0;
                Object[] o = null;
                // 交付周期
                if (null != ofuOnline.get(i).getPromotionChannel() || "".equals(ofuOnline.get(i).getPromotionChannel())) {
                    o = ofuOnline.get(i).getPromotionChannel().split(",");
                    if (null != ofuOnline.get(i).getFriendsDate() || "".equals(ofuOnline.get(i).getFriendsDate())) {
                        count = count + 1;
                    }
                    if (null != ofuOnline.get(i).getMomoDate() || "".equals(ofuOnline.get(i).getMomoDate())) {
                        count = count + 1;
                    }
                    if (null != ofuOnline.get(i).getWeiboDate() || "".equals(ofuOnline.get(i).getWeiboDate())) {
                        count = count + 1;
                    }
                    if (o.length > count) {
                        
                    } else {
                        onlineCount = onlineCount + 1;
                        time = maxDate(ofuOnline.get(i).getFriendsDate(), ofuOnline.get(i).getMomoDate());
                        if ("null".equals(time)) {
                            if(jfzq(ofuOnline.get(i).getWeiboDate(), ofuOnline.get(i).getPayDate())>15){
                                onlineCountOvertime=onlineCountOvertime+1;
                            }
                        } else {
                            time = maxDate(time, ofuOnline.get(i).getWeiboDate());
                            if(jfzq(time, ofuOnline.get(i).getPayDate())>15){
                                onlineCountOvertime=onlineCountOvertime+1;
                            } 
                        }
                    }
                }
                if(ofuOnline.get(i).getOnlineUseTime()>0){
                    cycleNum=cycleNum+1;
                    cycle=cycle+ofuOnline.get(i).getOnlineUseTime();
                }
            }

        }
        if(!sList.isEmpty()){
            overTime=taskExtService.findOverTime(sList);
        }
        api.setDayOvertime(overTime);
        api.setNewCount(newCount);
        api.setOnlineCountOvertime(onlineCountOvertime);
        api.setOnlineCount(onlineCount);
        if(cycle!=0&&cycleNum!=0){
            BigDecimal bd = new BigDecimal(cycle/cycleNum/8);
            bd = bd.setScale(3, RoundingMode.HALF_UP);
            api.setAvgCycle(bd.toString()+"个工作日");
        }else{
            api.setAvgCycle("0");
        }
        return api;

    }
    
    /**
     * 团队订单统计周月报表
     *
     * @param starDate
     * @param endDate
     * @param weekOrMonth
     * @param teamId
     * @param request
     * @param response
     * @return
     * @throws ParseException
     * @date 2018年3月19日
     * @author SunQ
     */
    @RequestMapping(value = "weekAndMonth")
    @ResponseBody
    public ErpWeekAndMonth weekAndMonthNew(String starDate, String endDate,String weekOrMonth, String teamId,
                    HttpServletRequest request, HttpServletResponse response) throws ParseException {
        // Principal principal = UserUtils.getPrincipal();
        ErpWeekAndMonth api = new ErpWeekAndMonth();
        Integer newCount = 0;
        Integer onlineCount = 0;
        Integer onlineCountOvertime=0;
        if("week".equals(weekOrMonth)){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance(); 
            Date date=new Date();
            cal.setTime(date);  
            // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了  
            int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天  
            if (1 == dayWeek) {  
                cal.add(Calendar.DAY_OF_MONTH, -1);  
            }
            // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一  
            cal.setFirstDayOfWeek(Calendar.MONDAY);  
            // 获得当前日期是一个星期的第几天  
            int day = cal.get(Calendar.DAY_OF_WEEK);  
            // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值  
            cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);  
            String imptimeBegin = sdf.format(cal.getTime());  
            cal.add(Calendar.DATE, 6);  
            String imptimeEnd = sdf.format(cal.getTime());
            api.setStarTime(imptimeBegin);
            api.setEndTime(imptimeEnd);
            starDate = imptimeBegin + " 00:00:00";
            endDate = imptimeEnd + " 23:59:59";
        }else if("month".equals(weekOrMonth)){
            Calendar cale =Calendar.getInstance(); 
            // 获取当月第一天和最后一天  
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
            String firstday, lastday;  
            // 获取前月的第一天  
            cale = Calendar.getInstance();  
            cale.add(Calendar.MONTH, 0);  
            cale.set(Calendar.DAY_OF_MONTH, 1);  
            firstday = format.format(cale.getTime());  
            // 获取前月的最后一天  
            cale = Calendar.getInstance();  
            cale.add(Calendar.MONTH, 1);  
            cale.set(Calendar.DAY_OF_MONTH, 0);  
            lastday = format.format(cale.getTime());
            api.setStarTime(firstday);
            api.setEndTime(lastday);
            starDate = firstday + " 00:00:00";
            endDate = lastday + " 23:59:59";
        }else{
            api.setStarTime(starDate);
            api.setEndTime(endDate);
            starDate = starDate + " 00:00:00";
            endDate = endDate + " 23:59:59";
        }
        
        // 获取团队相关的所有成员
        List<String> userIds = getTeamUsers(request);
        
        // 获取订单统计信息
        Map<String, Object> result = ofuService.statisticsWeekAndMonth(userIds, starDate, endDate);
        newCount = Integer.parseInt(result.get("newCount").toString());
        onlineCount = Integer.parseInt(result.get("onlineCount1").toString()) + Integer.parseInt(result.get("onlineCount2").toString());
        onlineCountOvertime = Integer.parseInt(result.get("onlineOverCount").toString());
        BigDecimal usedTotalTime = new BigDecimal(result.get("usedTotalTime").toString()).setScale(3, BigDecimal.ROUND_HALF_UP);
        
        api.setNewCount(newCount);
        api.setOnlineCount(onlineCount);
        api.setOnlineCountOvertime(onlineCountOvertime);
        if(usedTotalTime.compareTo(new BigDecimal("0")) == 1){
            BigDecimal bd = 
                            usedTotalTime.divide(new BigDecimal(result.get("onlineCount1").toString()), 3, BigDecimal.ROUND_HALF_UP).divide(new BigDecimal("8"), 3, BigDecimal.ROUND_HALF_UP);
            api.setAvgCycle(bd.toString()+"个工作日");
        }else{
            api.setAvgCycle("0");
        }
        return api;
    }
    
    /** 获取团队下的所有用户 */
    private List<String> getTeamUsers(HttpServletRequest request) {
        Principal principal = UserUtils.getPrincipal();
        String teamId = request.getParameter("teamId");
        List<String> teamIds = new ArrayList<>();
        
        // 所有团队
        if (StringUtils.isNotBlank(teamId) && "1".equals(teamId)) {
            List<ErpTeamUser> teamuser = erpTeamUserService.findwhereuser(Global.NO, Global.YES, principal.getId());
            if(CollectionUtils.isNotEmpty(teamuser)){
                for(ErpTeamUser teamUser : teamuser){
                    teamIds.add(teamUser.getTeamId());
                }
            }
        }
        
        // 指定团队
        if (StringUtils.isNotBlank(teamId) && !"1".equals(teamId)) {
            teamIds.add(teamId);
        }
        return erpTeamUserService.findTeamUserIds(teamIds);
    }

    /**
     * 团队订单统计数据表
     * 
     * @throws ParseException
     * 
     */
    @RequestMapping(value = "report")
    @ResponseBody
    public ErpReport report(HttpServletRequest request, HttpServletResponse response, Model model) throws ParseException {
        List<String> teamUsers = getTeamUsers(request);
        ErpReport report = new ErpReport();
        // 累计总接单数
        Integer orderCount = erpOrderSplitInfoDao.findstatisticsReportNoCancel(teamUsers);
        report.setOrderCount(orderCount);
        // 累计总接已完成单数
        Integer competeCount = erpOrderSplitInfoDao.findstatisticsReportCompete(teamUsers);
        report.setCompeteCount(competeCount);
        // 当前跟进订单总数
        Integer followCount = erpOrderSplitInfoDao.findWhereUnderway(teamUsers);
        report.setFollowCount(followCount);
        // 当前存在资质问题的, 正放在待生产库的跟进订单数
        Integer qualificationsCount = erpOrderSplitInfoDao.findWhereQualifications(teamUsers);
        report.setQualificationsCount(qualificationsCount);
        // 存在过资质问题的跟进订单数
        Integer allQualificationsCount = erpOrderSplitInfoDao.findAllQualifications(teamUsers);
        report.setAllQualificationsCount(allQualificationsCount);
        // 商户主动要求延迟上线，正放在待生产库的订单的数量
        Integer activeDelayCount = erpOrderSplitInfoDao.findWhereActiveDelay(teamUsers);
        report.setActiveDelayCount(activeDelayCount);
        // 商户曾经主动要求延迟上线的跟进订单数
        Integer allActiveDelayCount = erpOrderSplitInfoDao.findAllActiveDelay(teamUsers);
        report.setAllActiveDelayCount(allActiveDelayCount);
        // 正常跟进订单数（除存在过资质问题和商户曾经主动要求延迟上线的订单）
        Integer normalOrders = erpOrderSplitInfoDao.findNormalOrders(teamUsers);
        report.setNormalCount(normalOrders);
        // 当前有任务正在处理的跟进订单数=当前跟进订单总数-“当前存在资质问题的跟进订单数“-“商户当前主动要求延迟上线的跟进订单数”
        report.setHandleCount(followCount - qualificationsCount - activeDelayCount);
        // 有超时任务的订单数
        Integer overTimeCount = taskExtService.findOverTimeByOrder(teamUsers);
        report.setOverTimeCount(overTimeCount);
        // 有超时风险的订单数
        Integer riskCount = erpOrderSplitInfoDao.findWhereRiskCount(teamUsers);
        report.setTeamUsers(teamUsers);
        report.setRiskCount(riskCount);
        return report;
    
    }
    
    /**
     * 团队订单统计成员跟进订单
     * 
     * @throws ParseException
     * 
     */
    @RequestMapping(value = "followOrder")
    @ResponseBody
    public ApiResult<List<ErpTeamFollowOrder>> followOrder(String teamId,HttpServletRequest request, HttpServletResponse response, Model model) throws ParseException {
        Principal principal = UserUtils.getPrincipal();
        List<ErpTeamFollowOrder> follow=new ArrayList<ErpTeamFollowOrder>();
        if("1".equals(teamId)){
            follow=ofuService.findstatisticsFollowOrder(principal.getId());
        }else{
            follow=ofuService.findstatisticsFollowOrderWhereTeamId(teamId);
        }
        Integer followOrderCount=0;
        Integer taskOrder=0;
        if(!follow.isEmpty()&&null!=follow){
            for(int i=0;i<follow.size();i++){
                //有正在进行的订单
                List<ErpOrderSplitInfo> conductFromList = splitService.findWheretaskOrder(follow.get(i).getUserId());
                List<String> listOne=new ArrayList<String>();
                for(int c=0;c<conductFromList.size();c++){
                    listOne.add(conductFromList.get(c).getId());
                }
                //有关注任务的订单,此时可能一个订单存在关注也存在正在进行。
                List<ErpOrderSplitInfo> flowFromList = splitService.findWherefollowOrder(follow.get(i).getUserId());
                List<String> listTwo=new ArrayList<String>();
                for(int c=0;c<flowFromList.size();c++){
                    listTwo.add(flowFromList.get(c).getId());
                }
                //排除正在进行和关注重复的，避免相加相同订单相加两次
                List<String> listThree=getDiffrent(listOne,listTwo);
                follow.get(i).setFollowOrder(flowFromList.size()+listThree.size());
                followOrderCount=followOrderCount+flowFromList.size()+listThree.size();
                follow.get(i).setTaskOrder(conductFromList.size());
                taskOrder=taskOrder+conductFromList.size();
            }
            ErpTeamFollowOrder avgfollow=new ErpTeamFollowOrder();
            avgfollow.setUserName("人均");
            avgfollow.setFollowOrder(followOrderCount/follow.size());
            avgfollow.setTaskOrder(taskOrder/follow.size());
            follow.add(avgfollow);
            
        }
        return ApiResult.build(follow);
    
    }
    
    private static List<String> getDiffrent(List<String> list1, List<String> list2) {
        List<String> diff = new ArrayList<String>();
        for(String str:list1)
        {
            if(!list2.contains(str))
            {
                diff.add(str);
            }
        }
        return diff;
    }

    /**
     * 异步获取角色列表
     *
     * @param id
     * @return
     * @date 2018年1月24日
     * @author SunQ
     */
    @ResponseBody
    @RequestMapping(value = {"getRoleData"})
    public ApiResult<List<Role>> getRoleData() {
        List<Role> roles = systemService.findAllRole();
        return ApiResult.build(roles);
    }

    /**
     * 获取团队分单明细列表
     *
     * @param dto
     * @param request
     * @param response
     * @return
     * @date 2018年5月9日
     * @author linqunzhi
     */
    @ResponseBody
    @RequestMapping(value = "/findTeamSplitStatistics", method = RequestMethod.POST)
    public ApiResult<SplitStatisticsAllResponseDto> findTeamSplitStatistics(@RequestBody SplitStatisticsRequestDto dto, HttpServletRequest request,
                    HttpServletResponse response) {
        Principal principal = UserUtils.getPrincipal();
        SplitStatisticsAllResponseDto page = erpStatisticsService.findTeamSplitStatistics(new Page<SplitStatisticsRequestDto>(request, response),
                        dto, principal.getId());
        return ApiResult.build(page);

    }

    /**
     * 导出团队 分单明细列表
     *
     * @param dto
     * @param request
     * @param response
     * @param model
     * @return
     * @throws IOException
     * @date 2018年5月11日
     * @author linqunzhi
     */
    @RequestMapping(value = "exportTeamSplit", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject exportTeamSplit(@RequestParam String jsonObject, HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
        // 转换数据
        SplitStatisticsRequestDto dto = JSON.parseObject(StringEscapeUtils.unescapeHtml4(jsonObject), SplitStatisticsRequestDto.class);
        Principal principal = UserUtils.getPrincipal();
        SplitStatisticsAllResponseDto result = erpStatisticsService.findTeamSplitStatistics(null, dto, principal.getId());
        FastExcel.exportExcel(response, "订单统计导出", result == null ? null : result.getList());
        JSONObject resObject = new JSONObject();
        resObject.put("result", true);
        resObject.put("message", "导出成功");
        return resObject;

    }

    /**
     * 获取个人分单明细列表
     *
     * @param dto
     * @param request
     * @param response
     * @return
     * @date 2018年5月9日
     * @author linqunzhi
     */
    @ResponseBody
    @RequestMapping(value = "/findUserSplitStatistics", method = RequestMethod.POST)
    public ApiResult<SplitStatisticsAllResponseDto> findUserSplitStatistics(@RequestBody SplitStatisticsRequestDto dto, HttpServletRequest request,
                    HttpServletResponse response) {
        Principal principal = UserUtils.getPrincipal();
        SplitStatisticsAllResponseDto result = erpStatisticsService.findUserSplitStatistics(new Page<SplitStatisticsRequestDto>(request, response),
                        dto, principal.getId());
        return ApiResult.build(result);

    }

    /**
     * 导出个人分单明细列表
     *
     * @param dto
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @date 2018年5月14日
     * @author linqunzhi
     */
    @RequestMapping(value = "exportUserSplit", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject exportUserSplit(@RequestParam String jsonObject, HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
        // 转换数据
        SplitStatisticsRequestDto dto = JSON.parseObject(StringEscapeUtils.unescapeHtml4(jsonObject), SplitStatisticsRequestDto.class);
        Principal principal = UserUtils.getPrincipal();
        SplitStatisticsAllResponseDto result = erpStatisticsService.findUserSplitStatistics(null, dto, principal.getId());
        FastExcel.exportExcel(response, "订单统计导出", result == null ? null : result.getList());
        JSONObject resObject = new JSONObject();
        resObject.put("result", true);
        resObject.put("message", "导出成功");
        return resObject;
    }

    /**
     * 获取团队订单总数统计
     *
     * @return
     * @date 2018年5月14日
     * @author linqunzhi
     */
    @RequestMapping(value = "getTeamSplitReport")
    @ResponseBody
    public ApiResult<SplitReportResponseDto> getTeamSplitReport(String teamId) {
        Principal principal = UserUtils.getPrincipal();
        SplitReportResponseDto result = erpStatisticsService.getTeamSplitReport(principal.getId(), teamId);
        return ApiResult.build(result);
    }

    /**
     * 获取团队分单周/月统计数据
     *
     * @param teamId
     * @param startDateStr
     * @param endDateStr
     * @return
     * @date 2018年5月15日
     * @author linqunzhi
     */
    @RequestMapping(value = "getTeamSplitWeekAndMonth")
    @ResponseBody
    public BaseResult getTeamSplitWeekAndMonth(String teamId, String startDateStr, String endDateStr) {
        Principal principal = UserUtils.getPrincipal();
        SplitWeekAndMonthResponseDto result = erpStatisticsService.getTeamSplitWeekAndMonth(principal.getId(), teamId, startDateStr, endDateStr);
        return new BaseResult(result);


    }

    /**
     * 团队成员跟进订单统计
     *
     * @return
     * @date 2018年5月15日
     * @author linqunzhi
     */
    @RequestMapping(value = "findTeamSplitWeekAndMonth")
    @ResponseBody
    public BaseResult findTeamMemberSplitReport(String teamId) {
        Principal principal = UserUtils.getPrincipal();
        List<SplitTeamMemberReportResponseDto> result = erpStatisticsService.findTeamMemberSplitReport(principal.getId(), teamId);
        return new BaseResult(result);

    }

}