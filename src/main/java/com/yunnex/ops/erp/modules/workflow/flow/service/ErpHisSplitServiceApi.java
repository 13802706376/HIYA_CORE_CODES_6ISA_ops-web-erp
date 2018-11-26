package com.yunnex.ops.erp.modules.workflow.flow.service;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.yunnex.ops.erp.modules.holiday.service.ErpHolidaysService;
import com.yunnex.ops.erp.modules.order.dao.ErpOrderSplitInfoDao;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitInfo;
import com.yunnex.ops.erp.modules.schedule.entity.ErpHisSplit;
import com.yunnex.ops.erp.modules.schedule.entity.ErpHisSplitValue;
import com.yunnex.ops.erp.modules.schedule.service.ErpHisSplitService;
import com.yunnex.ops.erp.modules.schedule.service.ErpHisSplitValueService;
import com.yunnex.ops.erp.modules.shop.dao.ErpShopInfoDao;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.sys.entity.JobNumberInfo;
import com.yunnex.ops.erp.modules.sys.service.JobNumberInfoService;
import com.yunnex.ops.erp.modules.workflow.channel.service.JykOrderPromotionChannelService;
import com.yunnex.ops.erp.modules.workflow.flow.common.JykFlowConstants;
import com.yunnex.ops.erp.modules.workflow.user.dao.ErpOrderFlowUserDao;
import com.yunnex.ops.erp.modules.workflow.user.entity.ErpOrderFlowUser;

@Service
public class ErpHisSplitServiceApi {
    
    /**
     * 日志对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ErpHisSplitServiceApi.class);
    
	@Autowired
	private ErpHisSplitService hisService;
	@Autowired
	private ErpHisSplitValueService hisvalueService;
	@Autowired
	private ErpOrderSplitInfoDao erpOrderSplitInfoDao;
	@Autowired
	private ErpShopInfoDao erpShopInfoDao;
	@Autowired
	private ErpOrderFlowUserDao erpOrderFlowUserDao;
	@Autowired
	private ErpHolidaysService holidayService;
	@Autowired
	private JobNumberInfoService jobService;
	@Autowired
	private JykOrderPromotionChannelService jykOrderPromotionChannelService;
	
    /**
     * 订单生效保存
     * 
     */
	public void orderEffective(String splitId){
	    try {
	        ErpOrderSplitInfo erpOrderSplitInfo = erpOrderSplitInfoDao.get(splitId);
	        ErpShopInfo erpShopInfo = erpShopInfoDao.getByZhangbeiID(erpOrderSplitInfo.getShopId());
	        ErpHisSplit his=new ErpHisSplit();
	        if(null!=erpShopInfo){
	        	his.setShopInfoId(erpShopInfo.getId());	
	        }else{
	        	his.setShopInfoId("");	
	        }
	        his.setOrderId(erpOrderSplitInfo.getOrderId());
	        his.setOrderNum(erpOrderSplitInfo.getOrderNumber());
	        his.setSplitId(erpOrderSplitInfo.getId());
	        his.setSplitNum(Integer.toString(erpOrderSplitInfo.getNum()));
	        his.setProcessTitle("订单生效");
	        his.setProcessType("1");
	        hisService.save(his);
	        ErpHisSplitValue hv=new ErpHisSplitValue();
	        hv.setHisSplitId(his.getId());
	        hv.setHisContent("订单类型");
	        hv.setHisValue(erpOrderSplitInfo.getGoodTypeName());
	        hisvalueService.save(hv);
	        ErpHisSplitValue hv1=new ErpHisSplitValue();
	        hv1.setHisSplitId(his.getId());
	        hv1.setHisContent("套餐名称");
	        hv1.setHisValue(erpOrderSplitInfo.getGoodName());
	        hisvalueService.save(hv1);
	        ErpHisSplitValue hv2=new ErpHisSplitValue();
	        hv2.setHisSplitId(his.getId());
	        hv2.setHisContent("套餐数量");
	        hv2.setHisValue(Integer.toString(erpOrderSplitInfo.getNum()));
	        hisvalueService.save(hv2);
	        ErpHisSplitValue hv3=new ErpHisSplitValue();
	        hv3.setHisSplitId(his.getId());
	        hv3.setHisContent("生效时间");
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        hv3.setHisValue(sdf.format(erpOrderSplitInfo.getCreateDate()));
	        hisvalueService.save(hv3);
        } catch (RuntimeException e) {
            LOGGER.info("orderEffective-->执行异常：{}", e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
	}
	
    /**
     * 指派策划专家成功
     * 
     */
	public void planning(String splitId){
	    try {
	        ErpOrderSplitInfo erpOrderSplitInfo = erpOrderSplitInfoDao.get(splitId);
	        ErpShopInfo erpShopInfo = erpShopInfoDao.getByZhangbeiID(erpOrderSplitInfo.getShopId());
	        ErpOrderFlowUser planningExpert = erpOrderFlowUserDao.findByProcInsIdAndRoleName(erpOrderSplitInfo.getProcInsId(), JykFlowConstants.Planning_Expert);
	        ErpOrderFlowUser operationAdviser = erpOrderFlowUserDao.findByProcInsIdAndRoleName(erpOrderSplitInfo.getProcInsId(), JykFlowConstants.OPERATION_ADVISER);
	        JobNumberInfo jobplanning=jobService.getByUserId(planningExpert.getUser().getId());
	        ErpHisSplit his=new ErpHisSplit();
	        if(null!=erpShopInfo){
	        	his.setShopInfoId(erpShopInfo.getId());	
	        }else{
	        	his.setShopInfoId("");	
	        }
	        his.setOrderId(erpOrderSplitInfo.getOrderId());
	        his.setOrderNum(erpOrderSplitInfo.getOrderNumber());
	        his.setSplitId(erpOrderSplitInfo.getId());
	        his.setSplitNum(Integer.toString(erpOrderSplitInfo.getNum()));
	        his.setProcessTitle("专属服务人员指派中");
	        his.setProcessType("2");
	        hisService.save(his);
	        ErpHisSplitValue hv=new ErpHisSplitValue();
	        hv.setHisSplitId(his.getId());
	        hv.setHisContent("策划专家");
	        if(jobplanning==null){
	        	hv.setHisValue("");
	        }else{
		        hv.setHisValue(jobplanning.getJobNumber());	
	        }
	        hisvalueService.save(hv);
	        ErpHisSplitValue hv5=new ErpHisSplitValue();
	        hv5.setHisSplitId(his.getId());
	        hv5.setHisContent("生效时间");
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        hv5.setHisValue(sdf.format(new Date()));
	        hisvalueService.save(hv5);
	        if(operationAdviser!=null){
		        JobNumberInfo joboperation=jobService.getByUserId(operationAdviser.getUser().getId());
		        ErpHisSplitValue hv1=new ErpHisSplitValue();
		        hv1.setHisSplitId(his.getId());
		        hv1.setHisContent("运营顾问");
		        if(joboperation==null){
		        	hv1.setHisValue("");
		        }else{
		        	hv1.setHisValue(joboperation.getJobNumber());
		        }
		        hisvalueService.save(hv1);
	        }
        } catch (RuntimeException e) {
            LOGGER.info("planning-->执行异常：{}", e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
	}
	
    /**
     * 确认推广门店
     * 
     */
	public void extensionStore(String splitId,String storeInfoId,String storeName){
			try{
	
			        ErpOrderSplitInfo erpOrderSplitInfo = erpOrderSplitInfoDao.get(splitId);
			        ErpShopInfo erpShopInfo = erpShopInfoDao.getByZhangbeiID(erpOrderSplitInfo.getShopId());
			        ErpOrderFlowUser planningExpert = erpOrderFlowUserDao.findByProcInsIdAndRoleName(erpOrderSplitInfo.getProcInsId(), JykFlowConstants.Planning_Expert);
			        ErpOrderFlowUser operationAdviser = erpOrderFlowUserDao.findByProcInsIdAndRoleName(erpOrderSplitInfo.getProcInsId(), JykFlowConstants.OPERATION_ADVISER);
			        JobNumberInfo jobplanning=jobService.getByUserId(planningExpert.getUser().getId());
					ErpHisSplit his=new ErpHisSplit();
					his.setErpStoreInfoId(storeInfoId);
			        his.setShopInfoId(erpShopInfo.getId());
			        his.setOrderId(erpOrderSplitInfo.getOrderId());
			        his.setOrderNum(erpOrderSplitInfo.getOrderNumber());
			        his.setSplitId(erpOrderSplitInfo.getId());
			        his.setSplitNum(Integer.toString(erpOrderSplitInfo.getNum()));
					his.setProcessTitle("确认推广门店");
					his.setProcessType("3");
					hisService.save(his);
					ErpHisSplitValue hv=new ErpHisSplitValue();
					hv.setHisSplitId(his.getId());
					hv.setHisContent("策划专家");
					if(jobplanning==null){
			        	hv.setHisValue("");
			        }else{
				        hv.setHisValue(jobplanning.getJobNumber());	
			        }
					hisvalueService.save(hv);
					if(operationAdviser!=null){
				        JobNumberInfo joboperation=jobService.getByUserId(operationAdviser.getUser().getId());
				        ErpHisSplitValue hv1=new ErpHisSplitValue();
				        hv1.setHisSplitId(his.getId());
				        hv1.setHisContent("运营顾问");
				        if(joboperation==null){
				        	hv1.setHisValue("");
				        }else{
				        	hv1.setHisValue(joboperation.getJobNumber());
				        }
				        hisvalueService.save(hv1);
			        }
					ErpHisSplitValue hv2=new ErpHisSplitValue();
					hv2.setHisSplitId(his.getId());
					hv2.setHisContent("门店名称");
					hv2.setHisValue(storeName);
					hisvalueService.save(hv2);
			        ErpHisSplitValue hv5=new ErpHisSplitValue();
			        hv5.setHisSplitId(his.getId());
			        hv5.setHisContent("生效时间");
			        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			        hv5.setHisValue(sdf.format(new Date()));
			        hisvalueService.save(hv5);
			    } catch (Exception e) {
			        LOGGER.info("extensionStore-->执行异常：{}", e.getMessage());
			        LOGGER.error(e.getMessage(), e);
			    }
	}
	
    /**
     * 确认投放门店资质齐全
     * 
     */
	public void qualification(String splitId,String isPass,String storeInfoId,String storeName){
	    try {
	        ErpOrderSplitInfo erpOrderSplitInfo = erpOrderSplitInfoDao.get(splitId);
	        ErpShopInfo erpShopInfo = erpShopInfoDao.getByZhangbeiID(erpOrderSplitInfo.getShopId());  
	        ErpOrderFlowUser operationAdviser = erpOrderFlowUserDao.findByProcInsIdAndRoleName(erpOrderSplitInfo.getProcInsId(), JykFlowConstants.OPERATION_ADVISER);
	        ErpHisSplit his=new ErpHisSplit();
	        his.setErpStoreInfoId(storeInfoId);
	        his.setShopInfoId(erpShopInfo.getId());
	        his.setOrderId(erpOrderSplitInfo.getOrderId());
	        his.setOrderNum(erpOrderSplitInfo.getOrderNumber());
	        his.setSplitId(erpOrderSplitInfo.getId());
	        his.setSplitNum(Integer.toString(erpOrderSplitInfo.getNum()));
	        his.setProcessTitle("确认投放门店资质齐全");
	        his.setProcessType("4");
	        hisService.save(his);
	        if("1".equals(isPass)){
	        	ErpHisSplitValue hv2=new ErpHisSplitValue();
				hv2.setHisSplitId(his.getId());
				hv2.setHisContent("门店名称");
				hv2.setHisValue(storeName);
				hisvalueService.save(hv2);
				ErpHisSplitValue hv3=new ErpHisSplitValue();
		        hv3.setHisSplitId(his.getId());
		        hv3.setHisContent("投放门店资质是否齐全");
		        hv3.setHisValue("0");
		        hisvalueService.save(hv3);
	        }else{
	        	ErpHisSplitValue hv2=new ErpHisSplitValue();
				hv2.setHisSplitId(his.getId());
				hv2.setHisContent("门店名称");
				hv2.setHisValue(storeName);
				hisvalueService.save(hv2);
				ErpHisSplitValue hv3=new ErpHisSplitValue();
		        hv3.setHisSplitId(his.getId());
		        hv3.setHisContent("投放门店资质是否齐全");
		        hv3.setHisValue("1");//0为齐全，1为不齐全
		        hisvalueService.save(hv3);
	        }
	        if(operationAdviser!=null){
		        JobNumberInfo joboperation=jobService.getByUserId(operationAdviser.getUser().getId());
		        ErpHisSplitValue hv1=new ErpHisSplitValue();
		        hv1.setHisSplitId(his.getId());
		        hv1.setHisContent("运营顾问");
		        if(joboperation==null){
		        	hv1.setHisValue("");
		        }else{
		        	hv1.setHisValue(joboperation.getJobNumber());
		        }
		        hisvalueService.save(hv1);
	        }
	        ErpHisSplitValue hv5=new ErpHisSplitValue();
	        hv5.setHisSplitId(his.getId());
	        hv5.setHisContent("生效时间");
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        hv5.setHisValue(sdf.format(new Date()));
	        hisvalueService.save(hv5);
        } catch (Exception e) {
            LOGGER.info("qualification-->执行异常：{}", e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
	}
	
	 /**
     * 经营诊断完成
     * 
     */
	public void diagnosis(String splitId,String time,String storeInfoId,String storeName){
	    try {
	    	SimpleDateFormat s=new SimpleDateFormat("yyyy-MM-dd");
	    	Date date=s.parse(time);
	    	ErpOrderSplitInfo erpOrderSplitInfo = erpOrderSplitInfoDao.get(splitId);
	        ErpShopInfo erpShopInfo = erpShopInfoDao.getByZhangbeiID(erpOrderSplitInfo.getShopId());  
	        ErpOrderFlowUser planningExpert = erpOrderFlowUserDao.findByProcInsIdAndRoleName(erpOrderSplitInfo.getProcInsId(), JykFlowConstants.Planning_Expert);
	        JobNumberInfo jobplanning=jobService.getByUserId(planningExpert.getUser().getId());
	        ErpHisSplit his=new ErpHisSplit();
	        his.setErpStoreInfoId(storeInfoId);
	        his.setShopInfoId(erpShopInfo.getId());
	        his.setOrderId(erpOrderSplitInfo.getOrderId());
	        his.setOrderNum(erpOrderSplitInfo.getOrderNumber());
	        his.setSplitId(erpOrderSplitInfo.getId());
	        his.setSplitNum(Integer.toString(erpOrderSplitInfo.getNum()));
	        his.setProcessTitle("经营诊断完成");
	        his.setProcessType("5");
	        hisService.save(his);
	        ErpHisSplitValue hv2=new ErpHisSplitValue();
			hv2.setHisSplitId(his.getId());
			hv2.setHisContent("门店名称");
			hv2.setHisValue(storeName);
			hisvalueService.save(hv2);
			ErpHisSplitValue hv1=new ErpHisSplitValue();
	        hv1.setHisSplitId(his.getId());
	        hv1.setHisContent("策划专家");
	        if(jobplanning==null){
	        	hv1.setHisValue("");
	        }else{
		        hv1.setHisValue(jobplanning.getJobNumber());	
	        }
	        hisvalueService.save(hv1);
	        ErpHisSplitValue hv3=new ErpHisSplitValue();
	        hv3.setHisSplitId(his.getId());
	        hv3.setHisContent("电话沟通完成日期");
	        hv3.setHisValue(s.format(date));
	        hisvalueService.save(hv3);
	        ErpHisSplitValue hv5=new ErpHisSplitValue();
	        hv5.setHisSplitId(his.getId());
	        hv5.setHisContent("生效时间");
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        hv5.setHisValue(sdf.format(new Date()));
	        hisvalueService.save(hv5);
        } catch (Exception e) {
            LOGGER.info("diagnosis-->执行异常：{}", e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
	}
	
    /**
     * 确认营销策划方案
     * 
     */
	public void marketingPlanning(String splitId,String time,String storeInfoId,String storeName){
	    try {
	    	SimpleDateFormat s=new SimpleDateFormat("yyyy-MM-dd");
	    	Date date=s.parse(time);
	        ErpOrderSplitInfo erpOrderSplitInfo = erpOrderSplitInfoDao.get(splitId);
	        ErpShopInfo erpShopInfo = erpShopInfoDao.getByZhangbeiID(erpOrderSplitInfo.getShopId());  
	        ErpHisSplit his=new ErpHisSplit();
	        his.setErpStoreInfoId(storeInfoId);
	        his.setShopInfoId(erpShopInfo.getId());
	        his.setOrderId(erpOrderSplitInfo.getOrderId());
	        his.setOrderNum(erpOrderSplitInfo.getOrderNumber());
	        his.setSplitId(erpOrderSplitInfo.getId());
	        his.setSplitNum(Integer.toString(erpOrderSplitInfo.getNum()));
	        his.setProcessTitle("确认营销策划方案");
	        his.setProcessType("6");
	        hisService.save(his);
	        ErpHisSplitValue hv2=new ErpHisSplitValue();
			hv2.setHisSplitId(his.getId());
			hv2.setHisContent("门店名称");
			hv2.setHisValue(storeName);
			hisvalueService.save(hv2);
	        ErpHisSplitValue hv1=new ErpHisSplitValue();
	        hv1.setHisSplitId(his.getId());
	        hv1.setHisContent("电话沟通完成日期");
	        hv1.setHisValue(s.format(holidayService.enddate(date, 24)));
	        hisvalueService.save(hv1);
	        ErpHisSplitValue hv5=new ErpHisSplitValue();
	        hv5.setHisSplitId(his.getId());
	        hv5.setHisContent("生效时间");
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        hv5.setHisValue(sdf.format(new Date()));
	        hisvalueService.save(hv5);
        } catch (Exception e) {
            LOGGER.info("marketingPlanning-->执行异常：{}", e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
	}
	
    /**
     * 指派文案策划
     * 
     */
	public void creativity(String splitId,String time,String storeInfoId,String storeName){//time=沟通完时间加三天
	    try {
	    	SimpleDateFormat s=new SimpleDateFormat("yyyy-MM-dd");
	    	Date date=s.parse(time);
	        ErpOrderSplitInfo erpOrderSplitInfo = erpOrderSplitInfoDao.get(splitId);
	        ErpShopInfo erpShopInfo = erpShopInfoDao.getByZhangbeiID(erpOrderSplitInfo.getShopId());  
	        ErpHisSplit his=new ErpHisSplit();
	        his.setErpStoreInfoId(storeInfoId);
	        his.setShopInfoId(erpShopInfo.getId());
	        his.setOrderId(erpOrderSplitInfo.getOrderId());
	        his.setOrderNum(erpOrderSplitInfo.getOrderNumber());
	        his.setSplitId(erpOrderSplitInfo.getId());
	        his.setSplitNum(Integer.toString(erpOrderSplitInfo.getNum()));
	        his.setProcessTitle("推广创意输出中");
	        his.setProcessType("7");
	        hisService.save(his);
	        ErpHisSplitValue hv2=new ErpHisSplitValue();
			hv2.setHisSplitId(his.getId());
			hv2.setHisContent("门店名称");
			hv2.setHisValue(storeName);
			hisvalueService.save(hv2);
	        ErpHisSplitValue hv1=new ErpHisSplitValue();
	        hv1.setHisSplitId(his.getId());
	        hv1.setHisContent("电话沟通完成日期");
	        hv1.setHisValue(s.format(holidayService.enddate(date, 24)));
	        hisvalueService.save(hv1);
	        ErpHisSplitValue hv5=new ErpHisSplitValue();
	        hv5.setHisSplitId(his.getId());
	        hv5.setHisContent("生效时间");
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        hv5.setHisValue(sdf.format(new Date()));
	        hisvalueService.save(hv5);
        } catch (Exception e) {
            LOGGER.info("creativity-->执行异常：{}", e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
	}
	
    /**
     * 上传推广方案
     * 
     */
	public void uploadExtension(String splitId,String storeInfoId,String storeName){//time=沟通完时间加三天
	    try {
	        ErpOrderSplitInfo erpOrderSplitInfo = erpOrderSplitInfoDao.get(splitId);
	        ErpShopInfo erpShopInfo = erpShopInfoDao.getByZhangbeiID(erpOrderSplitInfo.getShopId());  
	        ErpOrderFlowUser planningExpert = erpOrderFlowUserDao.findByProcInsIdAndRoleName(erpOrderSplitInfo.getProcInsId(), JykFlowConstants.Planning_Expert);
	        ErpHisSplit his=new ErpHisSplit();
	        his.setErpStoreInfoId(storeInfoId);
	        his.setShopInfoId(erpShopInfo.getId());
	        his.setOrderId(erpOrderSplitInfo.getOrderId());
	        his.setOrderNum(erpOrderSplitInfo.getOrderNumber());
	        his.setSplitId(erpOrderSplitInfo.getId());
	        his.setSplitNum(Integer.toString(erpOrderSplitInfo.getNum()));
	        his.setProcessTitle("推广创意确认中");
	        his.setProcessType("8");
	        hisService.save(his);
	        ErpHisSplitValue hv2=new ErpHisSplitValue();
			hv2.setHisSplitId(his.getId());
			hv2.setHisContent("门店名称");
			hv2.setHisValue(storeName);
			hisvalueService.save(hv2);
			ErpHisSplitValue hv1=new ErpHisSplitValue();
	        hv1.setHisSplitId(his.getId());
	        hv1.setHisContent("策划专家花名ID");
	        hv1.setHisValue(planningExpert.getUser().getId());
	        hisvalueService.save(hv1);
	        ErpHisSplitValue hv5=new ErpHisSplitValue();
	        hv5.setHisSplitId(his.getId());
	        hv5.setHisContent("生效时间");
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        hv5.setHisValue(sdf.format(new Date()));
	        hisvalueService.save(hv5);
        } catch (Exception e) {
            LOGGER.info("uploadExtension-->执行异常：{}", e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
	}
	
    /**
     * 推广方案预留确认后
     * 
     */
	public void confirmExtension(String splitId,String storeInfoId,String storeName){//time=沟通完时间加三天
	    try {
	        ErpOrderSplitInfo erpOrderSplitInfo = erpOrderSplitInfoDao.get(splitId);
	        ErpShopInfo erpShopInfo = erpShopInfoDao.getByZhangbeiID(erpOrderSplitInfo.getShopId());  
	        ErpOrderFlowUser planningExpert = erpOrderFlowUserDao.findByProcInsIdAndRoleName(erpOrderSplitInfo.getProcInsId(), JykFlowConstants.Planning_Expert);
	        ErpOrderFlowUser operationAdviser = erpOrderFlowUserDao.findByProcInsIdAndRoleName(erpOrderSplitInfo.getProcInsId(), JykFlowConstants.OPERATION_ADVISER);
	        ErpHisSplit his=new ErpHisSplit();
	        his.setErpStoreInfoId(storeInfoId);
	        his.setShopInfoId(erpShopInfo.getId());
	        his.setOrderId(erpOrderSplitInfo.getOrderId());
	        his.setOrderNum(erpOrderSplitInfo.getOrderNumber());
	        his.setSplitId(erpOrderSplitInfo.getId());
	        his.setSplitNum(Integer.toString(erpOrderSplitInfo.getNum()));
	        his.setProcessTitle("推广创意确认");
	        his.setProcessType("9");
	        hisService.save(his);
	        ErpHisSplitValue hv2=new ErpHisSplitValue();
			hv2.setHisSplitId(his.getId());
			hv2.setHisContent("门店名称");
			hv2.setHisValue(storeName);
			hisvalueService.save(hv2);
			ErpHisSplitValue hv1=new ErpHisSplitValue();
	        hv1.setHisSplitId(his.getId());
	        hv1.setHisContent("策划专家花名ID");
	        hv1.setHisValue(planningExpert.getUser().getId());
	        hisvalueService.save(hv1);
	        if(operationAdviser!=null){
	        	ErpHisSplitValue hv3=new ErpHisSplitValue();
		        hv3.setHisSplitId(his.getId());
		        hv3.setHisContent("运营顾问花名ID");
		        hv3.setHisValue(operationAdviser.getUser().getId());
		        hisvalueService.save(hv3);
	        }     
	        ErpHisSplitValue hv5=new ErpHisSplitValue();
	        hv5.setHisSplitId(his.getId());
	        hv5.setHisContent("生效时间");
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        hv5.setHisValue(sdf.format(new Date()));
	        hisvalueService.save(hv5);
        } catch (Exception e) {
            LOGGER.info("confirmExtension-->执行异常：{}", e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
	}
	
    /**
     * 确认投放通道后
     * 
     */
	public void deliveryChannel(String splitId,String delivery,String storeInfoId,String storeName){//time=沟通完时间加三天
	    try {
	        List<String> names = jykOrderPromotionChannelService.getChannelNames(splitId);
	        String deliverys = StringUtils.collectionToCommaDelimitedString(names);
	        ErpOrderSplitInfo erpOrderSplitInfo = erpOrderSplitInfoDao.get(splitId);
	        ErpShopInfo erpShopInfo = erpShopInfoDao.getByZhangbeiID(erpOrderSplitInfo.getShopId());  
	        ErpOrderFlowUser planningExpert = erpOrderFlowUserDao.findByProcInsIdAndRoleName(erpOrderSplitInfo.getProcInsId(), JykFlowConstants.Planning_Expert);
	        ErpOrderFlowUser operationAdviser = erpOrderFlowUserDao.findByProcInsIdAndRoleName(erpOrderSplitInfo.getProcInsId(), JykFlowConstants.OPERATION_ADVISER);
	        JobNumberInfo jobplanning=jobService.getByUserId(planningExpert.getUser().getId());
	        ErpHisSplit his=new ErpHisSplit();
	        his.setErpStoreInfoId(storeInfoId);
	        his.setShopInfoId(erpShopInfo.getId());
	        his.setOrderId(erpOrderSplitInfo.getOrderId());
	        his.setOrderNum(erpOrderSplitInfo.getOrderNumber());
	        his.setSplitId(erpOrderSplitInfo.getId());
	        his.setSplitNum(Integer.toString(erpOrderSplitInfo.getNum()));
	        his.setProcessTitle("推广通道确定");
	        his.setProcessType("10");
	        hisService.save(his);
	        ErpHisSplitValue hv2=new ErpHisSplitValue();
			hv2.setHisSplitId(his.getId());
			hv2.setHisContent("门店名称");
			hv2.setHisValue(storeName);
			hisvalueService.save(hv2);
			ErpHisSplitValue hv1=new ErpHisSplitValue();
	        hv1.setHisSplitId(his.getId());
	        hv1.setHisContent("投放通道");
	        hv1.setHisValue(deliverys);
	        hisvalueService.save(hv1);
	        ErpHisSplitValue hv3=new ErpHisSplitValue();
	        hv3.setHisSplitId(his.getId());
	        hv3.setHisContent("策划专家");
	        if(jobplanning==null){
	        	hv3.setHisValue("");
	        }else{
		        hv3.setHisValue(jobplanning.getJobNumber());	
	        }
	        hisvalueService.save(hv3);
	        if(operationAdviser!=null){
		        JobNumberInfo joboperation=jobService.getByUserId(operationAdviser.getUser().getId());
		        ErpHisSplitValue hv4=new ErpHisSplitValue();
		        hv4.setHisSplitId(his.getId());
		        hv4.setHisContent("运营顾问");
		        if(joboperation==null){
		        	hv4.setHisValue("");
		        }else{
		        	hv4.setHisValue(joboperation.getJobNumber());
		        }
		        hisvalueService.save(hv4);
	        }
	        ErpHisSplitValue hv5=new ErpHisSplitValue();
	        hv5.setHisSplitId(his.getId());
	        hv5.setHisContent("生效时间");
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        hv5.setHisValue(sdf.format(new Date()));
	        hisvalueService.save(hv5);
        } catch (Exception e) {
            LOGGER.info("deliveryChannel-->执行异常：{}", e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
	}
	
    /**
     * 完成投放平台开户
     * 
     */
	public void openAccount(String splitId,String delivery,String time,String storeInfoId,String storeName){//time=沟通完时间加三天
	    try {
	        List<String> names = jykOrderPromotionChannelService.getChannelNames(splitId);
            String deliverys = StringUtils.collectionToCommaDelimitedString(names);
	    	SimpleDateFormat s=new SimpleDateFormat("yyyy-MM-dd");
	    	Date date=s.parse(time);
	        ErpOrderSplitInfo erpOrderSplitInfo = erpOrderSplitInfoDao.get(splitId);
	        ErpShopInfo erpShopInfo = erpShopInfoDao.getByZhangbeiID(erpOrderSplitInfo.getShopId());  
	        ErpHisSplit his=new ErpHisSplit();
	        his.setErpStoreInfoId(storeInfoId);
	        his.setShopInfoId(erpShopInfo.getId());
	        his.setOrderId(erpOrderSplitInfo.getOrderId());
	        his.setOrderNum(erpOrderSplitInfo.getOrderNumber());
	        his.setSplitId(erpOrderSplitInfo.getId());
	        his.setSplitNum(Integer.toString(erpOrderSplitInfo.getNum()));
	        his.setProcessTitle("完成投放平台开户");
	        his.setProcessType("11");
	        hisService.save(his);
	        /*String[] store=storeName.split(",");
			ErpHisSplitValue hv2=null;
			for(int i=0;i<store.length;i++){
				hv2=new ErpHisSplitValue();
				hv2.setHisSplitId(his.getId());
				hv2.setHisContent("门店名称");
				hv2.setHisValue(store[i]);
				hisvalueService.save(hv2);
			}*/
	        ErpHisSplitValue hv2=new ErpHisSplitValue();
			hv2.setHisSplitId(his.getId());
			hv2.setHisContent("门店名称");
			hv2.setHisValue(storeName);
			hisvalueService.save(hv2);
			ErpHisSplitValue hv1=new ErpHisSplitValue();
	        hv1.setHisSplitId(his.getId());
	        hv1.setHisContent("投放通道");
	        hv1.setHisValue(deliverys);
	        hisvalueService.save(hv1);
	        ErpHisSplitValue hv3=new ErpHisSplitValue();
	        hv3.setHisSplitId(his.getId());
	        hv3.setHisContent("电话沟通完成日期");
	        hv3.setHisValue(s.format(holidayService.enddate(date, 24)));
	        hisvalueService.save(hv3);
	        ErpHisSplitValue hv5=new ErpHisSplitValue();
	        hv5.setHisSplitId(his.getId());
	        hv5.setHisContent("生效时间");
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        hv5.setHisValue(sdf.format(new Date()));
	        hisvalueService.save(hv5);
        } catch (Exception e) {
            LOGGER.info("openAccount-->执行异常：{}", e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
	}
	
	
    /**
     * 推广创意确认后
     * 
     */
    public void promotionCreativity(String splitId, String storeInfoId, String storeName) {// time=沟通完时间加三天
	    try {
	        List<String> names = jykOrderPromotionChannelService.getChannelNames(splitId);
            String deliverys = StringUtils.collectionToCommaDelimitedString(names);
	        ErpOrderSplitInfo erpOrderSplitInfo = erpOrderSplitInfoDao.get(splitId);
	        ErpShopInfo erpShopInfo = erpShopInfoDao.getByZhangbeiID(erpOrderSplitInfo.getShopId());  
	        ErpOrderFlowUser planningExpert = erpOrderFlowUserDao.findByProcInsIdAndRoleName(erpOrderSplitInfo.getProcInsId(), JykFlowConstants.Planning_Expert);
	        JobNumberInfo jobplanning=jobService.getByUserId(planningExpert.getUser().getId());
	        ErpHisSplit his=new ErpHisSplit();
	        his.setErpStoreInfoId(storeInfoId);
	        his.setShopInfoId(erpShopInfo.getId());
	        his.setOrderId(erpOrderSplitInfo.getOrderId());
	        his.setOrderNum(erpOrderSplitInfo.getOrderNumber());
	        his.setSplitId(erpOrderSplitInfo.getId());
	        his.setSplitNum(Integer.toString(erpOrderSplitInfo.getNum()));
	        his.setProcessTitle("推广创意通过投放平台审核");
	        his.setProcessType("12");
	        hisService.save(his);
	        ErpHisSplitValue hv2=new ErpHisSplitValue();
			hv2.setHisSplitId(his.getId());
			hv2.setHisContent("门店名称");
			hv2.setHisValue(storeName);
			hisvalueService.save(hv2);
			ErpHisSplitValue hv1=new ErpHisSplitValue();
	        hv1.setHisSplitId(his.getId());
	        hv1.setHisContent("投放通道");
	        hv1.setHisValue(deliverys);
	        hisvalueService.save(hv1);	       
	        ErpHisSplitValue hv3=new ErpHisSplitValue();
	        hv3.setHisSplitId(his.getId());
	        hv3.setHisContent("策划专家");
	        if(jobplanning==null){
	        	hv3.setHisValue("");
	        }else{
		        hv3.setHisValue(jobplanning.getJobNumber());	
	        }
	        hisvalueService.save(hv3);
	        ErpHisSplitValue hv5=new ErpHisSplitValue();
	        hv5.setHisSplitId(his.getId());
	        hv5.setHisContent("生效时间");
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        hv5.setHisValue(sdf.format(new Date()));
	        hisvalueService.save(hv5);
        } catch (Exception e) {
            LOGGER.info("promotionCreativity-->执行异常：{}", e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
	}
	
	
    /**
     * 正式推广上线
     * 
     */
	public void promotionOnline(String splitId,String delivery,String storeInfoId,String storeName){//time=沟通完时间加三天
	    try {
	        List<String> names = jykOrderPromotionChannelService.getChannelNames(splitId);
            String deliverys = StringUtils.collectionToCommaDelimitedString(names);
	        ErpOrderSplitInfo erpOrderSplitInfo = erpOrderSplitInfoDao.get(splitId);
	        ErpShopInfo erpShopInfo = erpShopInfoDao.getByZhangbeiID(erpOrderSplitInfo.getShopId());  
	        ErpHisSplit his=new ErpHisSplit();
	        his.setErpStoreInfoId(storeInfoId);
	        his.setShopInfoId(erpShopInfo.getId());
	        his.setOrderId(erpOrderSplitInfo.getOrderId());
	        his.setOrderNum(erpOrderSplitInfo.getOrderNumber());
	        his.setSplitId(erpOrderSplitInfo.getId());
	        his.setSplitNum(Integer.toString(erpOrderSplitInfo.getNum()));
	        his.setProcessTitle("推广正式上线");
	        his.setProcessType("13");
	        hisService.save(his);
	        ErpHisSplitValue hv2=new ErpHisSplitValue();
			hv2.setHisSplitId(his.getId());
			hv2.setHisContent("门店名称");
			hv2.setHisValue(storeName);
			hisvalueService.save(hv2);
			ErpHisSplitValue hv1=new ErpHisSplitValue();
	        hv1.setHisSplitId(his.getId());
	        hv1.setHisContent("投放通道");
	        hv1.setHisValue(deliverys);
	        hisvalueService.save(hv1);
	        ErpHisSplitValue hv5=new ErpHisSplitValue();
	        hv5.setHisSplitId(his.getId());
	        hv5.setHisContent("生效时间");
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        hv5.setHisValue(sdf.format(new Date()));
	        hisvalueService.save(hv5);
        } catch (Exception e) {
            LOGGER.info("promotionOnline-->执行异常：{}", e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
	}
	
    /**
     * 首日推广数据同步
     * 
     */
	public void promotionSync(String splitId,String storeInfoId,String storeName){//time=沟通完时间加三天
	    try {
	        ErpOrderSplitInfo erpOrderSplitInfo = erpOrderSplitInfoDao.get(splitId);
	        ErpShopInfo erpShopInfo = erpShopInfoDao.getByZhangbeiID(erpOrderSplitInfo.getShopId());  
	        ErpOrderFlowUser planningExpert = erpOrderFlowUserDao.findByProcInsIdAndRoleName(erpOrderSplitInfo.getProcInsId(), JykFlowConstants.Planning_Expert);
	        ErpOrderFlowUser operationAdviser = erpOrderFlowUserDao.findByProcInsIdAndRoleName(erpOrderSplitInfo.getProcInsId(), JykFlowConstants.OPERATION_ADVISER);
	        ErpHisSplit his=new ErpHisSplit();
	        his.setErpStoreInfoId(storeInfoId);
	        his.setShopInfoId(erpShopInfo.getId());
	        his.setOrderId(erpOrderSplitInfo.getOrderId());
	        his.setOrderNum(erpOrderSplitInfo.getOrderNumber());
	        his.setSplitId(erpOrderSplitInfo.getId());
	        his.setSplitNum(Integer.toString(erpOrderSplitInfo.getNum()));
	        his.setProcessTitle("首日推广数据同步");
	        his.setProcessType("14");
	        hisService.save(his);
	        ErpHisSplitValue hv2=new ErpHisSplitValue();
			hv2.setHisSplitId(his.getId());
			hv2.setHisContent("门店名称");
			hv2.setHisValue(storeName);
			hisvalueService.save(hv2);
			ErpHisSplitValue hv1=new ErpHisSplitValue();
	        hv1.setHisSplitId(his.getId());
	        hv1.setHisContent("策划专家花名ID");
	        hv1.setHisValue(planningExpert.getUser().getId());
	        hisvalueService.save(hv1);
	        if(operationAdviser!=null){
	        	ErpHisSplitValue hv3=new ErpHisSplitValue();
		        hv3.setHisSplitId(his.getId());
		        hv3.setHisContent("运营顾问花名ID");
		        hv3.setHisValue(operationAdviser.getUser().getId());
		        hisvalueService.save(hv3);
	        }
	        ErpHisSplitValue hv5=new ErpHisSplitValue();
	        hv5.setHisSplitId(his.getId());
	        hv5.setHisContent("生效时间");
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        hv5.setHisValue(sdf.format(new Date()));
	        hisvalueService.save(hv5);
        } catch (Exception e) {
            LOGGER.info("promotionSync-->执行异常：{}", e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
	}
	
    /**
     * 第二次推广数据同步
     * 
     */
	public void promotionSyncTwo(String splitId,String storeInfoId,String storeName){//time=沟通完时间加三天
	    try {
	        ErpOrderSplitInfo erpOrderSplitInfo = erpOrderSplitInfoDao.get(splitId);
	        ErpShopInfo erpShopInfo = erpShopInfoDao.getByZhangbeiID(erpOrderSplitInfo.getShopId());  
	        ErpOrderFlowUser planningExpert = erpOrderFlowUserDao.findByProcInsIdAndRoleName(erpOrderSplitInfo.getProcInsId(), JykFlowConstants.Planning_Expert);
	        ErpOrderFlowUser operationAdviser = erpOrderFlowUserDao.findByProcInsIdAndRoleName(erpOrderSplitInfo.getProcInsId(), JykFlowConstants.OPERATION_ADVISER);
	        ErpHisSplit his=new ErpHisSplit();
	        his.setErpStoreInfoId(storeInfoId);
	        his.setShopInfoId(erpShopInfo.getId());
	        his.setOrderId(erpOrderSplitInfo.getOrderId());
	        his.setOrderNum(erpOrderSplitInfo.getOrderNumber());
	        his.setSplitId(erpOrderSplitInfo.getId());
	        his.setSplitNum(Integer.toString(erpOrderSplitInfo.getNum()));
	        his.setProcessTitle("数据报告同步");
	        his.setProcessType("15");
	        hisService.save(his);
	        ErpHisSplitValue hv2=new ErpHisSplitValue();
			hv2.setHisSplitId(his.getId());
			hv2.setHisContent("门店名称");
			hv2.setHisValue(storeName);
			hisvalueService.save(hv2);
			ErpHisSplitValue hv1=new ErpHisSplitValue();
	        hv1.setHisSplitId(his.getId());
	        hv1.setHisContent("策划专家花名ID");
	        hv1.setHisValue(planningExpert.getUser().getId());
	        hisvalueService.save(hv1);
	        if(operationAdviser!=null){
	        	ErpHisSplitValue hv3=new ErpHisSplitValue();
		        hv3.setHisSplitId(his.getId());
		        hv3.setHisContent("运营顾问花名ID");
		        hv3.setHisValue(operationAdviser.getUser().getId());
		        hisvalueService.save(hv3);
	        }
	        ErpHisSplitValue hv5=new ErpHisSplitValue();
	        hv5.setHisSplitId(his.getId());
	        hv5.setHisContent("生效时间");
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        hv5.setHisValue(sdf.format(new Date()));
	        hisvalueService.save(hv5);
        } catch (Exception e) {
            LOGGER.info("promotionSyncTwo-->执行异常：{}", e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
	}
	
	
    /**
     * 效果报告输出给商户
     * 
     */
	public void giveShop(String splitId,String storeInfoId,String storeName){//time=沟通完时间加三天
	    try {
	        ErpOrderSplitInfo erpOrderSplitInfo = erpOrderSplitInfoDao.get(splitId);
	        ErpShopInfo erpShopInfo = erpShopInfoDao.getByZhangbeiID(erpOrderSplitInfo.getShopId());  
	        ErpOrderFlowUser planningExpert = erpOrderFlowUserDao.findByProcInsIdAndRoleName(erpOrderSplitInfo.getProcInsId(), JykFlowConstants.Planning_Expert);
	        ErpOrderFlowUser operationAdviser = erpOrderFlowUserDao.findByProcInsIdAndRoleName(erpOrderSplitInfo.getProcInsId(), JykFlowConstants.OPERATION_ADVISER);
	        ErpHisSplit his=new ErpHisSplit();
	        his.setErpStoreInfoId(storeInfoId);
	        his.setShopInfoId(erpShopInfo.getId());
	        his.setOrderId(erpOrderSplitInfo.getOrderId());
	        his.setOrderNum(erpOrderSplitInfo.getOrderNumber());
	        his.setSplitId(erpOrderSplitInfo.getId());
	        his.setSplitNum(Integer.toString(erpOrderSplitInfo.getNum()));
	        his.setProcessTitle("效果报告输出");
	        his.setProcessType("16");
	        hisService.save(his);
	        ErpHisSplitValue hv2=new ErpHisSplitValue();
			hv2.setHisSplitId(his.getId());
			hv2.setHisContent("门店名称");
			hv2.setHisValue(storeName);
			hisvalueService.save(hv2);
			ErpHisSplitValue hv1=new ErpHisSplitValue();
	        hv1.setHisSplitId(his.getId());
	        hv1.setHisContent("策划专家花名ID");
	        hv1.setHisValue(planningExpert.getUser().getId());
	        hisvalueService.save(hv1);
	        if(operationAdviser!=null){
	        	ErpHisSplitValue hv3=new ErpHisSplitValue();
		        hv3.setHisSplitId(his.getId());
		        hv3.setHisContent("运营顾问花名ID");
		        hv3.setHisValue(operationAdviser.getUser().getId());
		        hisvalueService.save(hv3);
	        }
	        ErpHisSplitValue hv5=new ErpHisSplitValue();
	        hv5.setHisSplitId(his.getId());
	        hv5.setHisContent("生效时间");
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        hv5.setHisValue(sdf.format(new Date()));
	        hisvalueService.save(hv5);
        } catch (Exception e) {
            LOGGER.info("giveShop-->执行异常：{}", e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
	}

}



