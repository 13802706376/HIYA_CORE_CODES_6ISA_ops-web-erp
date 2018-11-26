package com.yunnex.ops.erp.modules.workflow.delivery.service;

import java.util.Date;
import java.util.List;

import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.yunnex.ops.erp.common.constant.CommonConstants;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.modules.holiday.service.ErpHolidaysService;
import com.yunnex.ops.erp.modules.sys.service.SysConstantsService;
import com.yunnex.ops.erp.modules.workflow.delivery.constant.ErpDeliveryServiceConstants;
import com.yunnex.ops.erp.modules.workflow.delivery.dao.ErpDeliveryServiceDao;
import com.yunnex.ops.erp.modules.workflow.delivery.entity.ErpDeliveryService;
import com.yunnex.ops.erp.modules.workflow.delivery.extraModel.DeliveryServiceWorkDays;
import com.yunnex.ops.erp.modules.workflow.flow.constant.DeliveryFlowConstant;

/**
 * erp_delivery_serviceService
 * @author hanhan
 * @version 2018-05-26
 */
@Service
public class ErpDeliveryServiceService extends CrudService<ErpDeliveryServiceDao, ErpDeliveryService> {
    @Autowired
    private ErpDeliveryServiceDao erpDeliveryServiceDao;
    @Autowired
	private ErpHolidaysService erpHolidaysService;
    @Autowired
	private SysConstantsService sysConstantsService;
	public ErpDeliveryService get(String id) {
		return super.get(id);
	}
	
	public List<ErpDeliveryService> findList(ErpDeliveryService erpDeliveryService) {
		return super.findList(erpDeliveryService);
	}
	
	public Page<ErpDeliveryService> findPage(Page<ErpDeliveryService> page, ErpDeliveryService erpDeliveryService) {
		return super.findPage(page, erpDeliveryService);
	}
	
	@Transactional(readOnly = false)
	public void save(ErpDeliveryService erpDeliveryService) {
		super.save(erpDeliveryService);
	}
	
	@Transactional(readOnly = false)
	public void delete(ErpDeliveryService erpDeliveryService) {
		super.delete(erpDeliveryService);
	}
	
	@Transactional(readOnly = false)
    public ErpDeliveryService getDeliveryInfoByProsIncId(String procInsId) {
	   return erpDeliveryServiceDao.getDeliveryInfoByProsIncId(procInsId);
    }
	@Transactional(readOnly = false)
    public  List<String>  findTaskIdByShopId(String zhangbeiId) {
       return erpDeliveryServiceDao.findTaskIdByShopId(zhangbeiId);
    }
    public ErpDeliveryService getDeliveryInfoByOrederId(String orderId) {
        return erpDeliveryServiceDao.getDeliveryInfoByOrederId(orderId);
    }
    
    
    @Transactional(readOnly = false)
    public void updateTime(ErpDeliveryService erpDelivery) {
    	erpDeliveryServiceDao.updateTime(erpDelivery);
    }
    
    @Transactional(readOnly = false)
    public void updateTime1(ErpDeliveryService erpDelivery) {
    	erpDeliveryServiceDao.updateTime1(erpDelivery);
    }
    /**
     * 保存 流程结束时间
     *
     * @param procInsId
     * @date 2018年6月8日
     * @author linqunzhi
     */
    @Transactional(readOnly = false)
    public void saveFlowEndTime(String procInsId) {
        logger.info("saveFlowEndTime start | procInsId={}", procInsId);
        // 记录 流程结束时间
        ErpDeliveryService erpDeliveryService = this.getDeliveryInfoByProsIncId(procInsId);
        if (erpDeliveryService == null) {
            logger.error("交付服务信息不存在 ！procInsId={}", procInsId);
            throw new ServiceException(CommonConstants.FailMsg.DATA);
        }
        Date now = new Date();
        // 流程结束时间
        erpDeliveryService.setFlowEndTime(now);
//        if(StringUtils.isEmpty(erpDeliveryService.getVisitServiceTime())){
//        	erpDeliveryService.setVisitServiceTime(now);
//        }
//        if(StringUtils.isEmpty(erpDeliveryService.getMaterielTime())){
//        	erpDeliveryService.setMaterielTime(now);
//        }
//        if(StringUtils.isEmpty(erpDeliveryService.getTrainTestTime())){
//        	erpDeliveryService.setTrainTestTime(now);
//        }
        if(StringUtils.isEmpty(erpDeliveryService.getShouldTrainTestTime())
        		||StringUtils.isEmpty(erpDeliveryService.getShouldVisitServiceTime())
        		||StringUtils.isEmpty(erpDeliveryService.getShouldMaterielTime())){
        	erpDeliveryService=saveStartTimeOther1(erpDeliveryService);
        }
        this.save(erpDeliveryService);
        logger.info("saveFlowEndTime end");
    }
    
    /**
     * 保存启动时间 和 其他相关时间
     *
     * @param procInsId
     * @param serviceType
     * @date 2018年6月7日
     * @author linqunzhi
     */
	public ErpDeliveryService saveStartTimeOther1(ErpDeliveryService erpDeliveryService) {
        // 启动时间
		Date now = erpDeliveryService.getStartTime()==null?new Date():erpDeliveryService.getStartTime();
        // 应完成交付时间
		Date shouldFlowEndTime = null;
		
		Date shouldTrainTestTime=null;
		
		Date shouldVisitServiceTime=null;
		
		Date shouldMaterielTime=null;
		
        // 聚引客应完成交付的工作日天数
		int juYinKeshouldFlowEndDays = 0;
        // 客常来应完成交付的工作日天数
		int keChangLaishouldFlowEndDays = 0;
        // 银联支付培训&测试（远程）任务应该完成的工作日天数
		int shouldTrainTestDays = 0;
        // 上门服务完成（首次营销策划服务）任务应该完成的工作日天数
		int shouldVisitServiceDays = 0;
		
		int shouldMaterielServiceDays = 0;
		
        // 获取工作日天数配置值
		String josnStr="";
		String serviceType=erpDeliveryService.getServiceType();
		if(StringUtil.isBlank(serviceType)){
			if(DeliveryFlowConstant.SERVICE_TYPE_KE_ZHCT_OLD.equals(erpDeliveryService.getZhctType())){
				josnStr = sysConstantsService
						.getConstantValByKey(ErpDeliveryServiceConstants.DELIVERY_SERVICE_ZHCT_DAYS);
				DeliveryServiceWorkDays workDays = null;
				if (!StringUtil.isBlank(josnStr)) {
					try {
						workDays = JSON.parseObject(josnStr, DeliveryServiceWorkDays.class);
					} catch (Exception e) {
                        logger.error("转换工作日错误", e);
					}
				}
				if (workDays != null) {
					juYinKeshouldFlowEndDays = workDays.getJuYingKe().getShouldFlowEndDays();
					shouldFlowEndTime = erpHolidaysService.getWorkDay(now, juYinKeshouldFlowEndDays);
					shouldTrainTestTime = erpHolidaysService.getWorkDay(now, juYinKeshouldFlowEndDays);
					shouldVisitServiceTime = erpHolidaysService.getWorkDay(now, juYinKeshouldFlowEndDays);
					shouldMaterielTime=erpHolidaysService.getWorkDay(now, juYinKeshouldFlowEndDays);
				}
			}
		}else{
			if(DeliveryFlowConstant.SERVICE_TYPE_JU_YIN_KE.equals(erpDeliveryService.getServiceType())
					|| DeliveryFlowConstant.SERVICE_TYPE_KE_CHANG_LAI.equals(erpDeliveryService.getServiceType())
					|| DeliveryFlowConstant.SERVICE_TYPE_JU_YIN_DATA.equals(erpDeliveryService.getServiceType())
					|| DeliveryFlowConstant.SERVICE_TYPE_KE_CHANG_LAI_BASIC.equals(erpDeliveryService.getServiceType())){
				josnStr = sysConstantsService
						.getConstantValByKey(ErpDeliveryServiceConstants.DELIVERY_SERVICE_WORK_DAYS);	
			}else if(DeliveryFlowConstant.SERVICE_TYPE_MU.equals(erpDeliveryService.getServiceType())){
				josnStr = sysConstantsService
						.getConstantValByKey(ErpDeliveryServiceConstants.DELIVERY_SERVICE_MATERIAL_DAYS);	
			}else if(DeliveryFlowConstant.SERVICE_TYPE_VC.equals(erpDeliveryService.getServiceType())){
				josnStr = sysConstantsService
						.getConstantValByKey(ErpDeliveryServiceConstants.DELIVERY_SERVICE_VC_DAYS);
			}
			DeliveryServiceWorkDays workDays = null;
			if (!StringUtil.isBlank(josnStr)) {
				try {
					workDays = JSON.parseObject(josnStr, DeliveryServiceWorkDays.class);
				} catch (Exception e) {
                    logger.error("转换工作日错误", e);
				}
			}
			if (workDays != null) {
				if (workDays.getJuYingKe() != null && workDays.getJuYingKe().getShouldFlowEndDays() != null) {
					juYinKeshouldFlowEndDays = workDays.getJuYingKe().getShouldFlowEndDays();
				}
				if (workDays.getKeChangLai() != null && workDays.getKeChangLai().getShouldFlowEndDays() != null) {
					keChangLaishouldFlowEndDays = workDays.getKeChangLai().getShouldFlowEndDays();
				}
				if (workDays.getShouldTrainTestDays() != null) {
					shouldTrainTestDays = workDays.getShouldTrainTestDays();
				}
				if (workDays.getShouldVisitServiceDays() != null) {
					shouldVisitServiceDays = workDays.getShouldVisitServiceDays();
				}
				if (workDays.getShouldMaterielDays() != null) {
					shouldMaterielServiceDays = workDays.getShouldMaterielDays();
				}
			}
			if (DeliveryFlowConstant.SERVICE_TYPE_JU_YIN_KE.equals(serviceType)) {
				shouldFlowEndTime = erpHolidaysService.getWorkDay(now, juYinKeshouldFlowEndDays);
			} else if(DeliveryFlowConstant.SERVICE_TYPE_KE_CHANG_LAI.equals(serviceType)) {
				shouldFlowEndTime = erpHolidaysService.getWorkDay(now, keChangLaishouldFlowEndDays);
			} else if(DeliveryFlowConstant.SERVICE_TYPE_MU.equals(serviceType)){
				shouldFlowEndTime = erpHolidaysService.getWorkDay(now, keChangLaishouldFlowEndDays);
			} else if(DeliveryFlowConstant.SERVICE_TYPE_VC.equals(serviceType)){
				shouldFlowEndTime = erpHolidaysService.getWorkDay(now, keChangLaishouldFlowEndDays);
			} else if(DeliveryFlowConstant.SERVICE_TYPE_KE_CHANG_LAI_BASIC.equals(serviceType)){
				shouldFlowEndTime = erpHolidaysService.getWorkDay(now, keChangLaishouldFlowEndDays);
	        } else if(DeliveryFlowConstant.SERVICE_TYPE_JU_YIN_DATA.equals(serviceType)){
	        	shouldFlowEndTime = erpHolidaysService.getWorkDay(now, keChangLaishouldFlowEndDays);
	        } 
            // 银联支付培训&测试（远程）任务应该完成时间
			shouldTrainTestTime = erpHolidaysService.getWorkDay(now, shouldTrainTestDays);
            // 上门服务完成（首次营销策划服务）任务应该完成时间
			shouldVisitServiceTime = erpHolidaysService.getWorkDay(now, shouldVisitServiceDays);
			
			shouldMaterielTime =erpHolidaysService.getWorkDay(now, shouldMaterielServiceDays);
		}
		erpDeliveryService.setShouldFlowEndTime(shouldFlowEndTime);
		erpDeliveryService.setShouldTrainTestTime(shouldTrainTestTime);
		erpDeliveryService.setShouldVisitServiceTime(shouldVisitServiceTime);
		erpDeliveryService.setShouldMaterielTime(shouldMaterielTime);
 		return erpDeliveryService;
	}

    public int countByOrderNumberAndServiceType(String orderNumber, String serviceType) {
        return dao.countByOrderNumberAndServiceType(orderNumber, serviceType);
    }
    public  ErpDeliveryService getDeliveryInfoByShopIdAndServiceTypeDesc(String zhangbeiId,String serviceType,String procInsId) {
       return erpDeliveryServiceDao.getDeliveryInfoByShopIdAndServiceTypeDesc(zhangbeiId,serviceType,procInsId);
    }

    public List<ErpDeliveryService> findByOrderNumberAndServiceType(String orderNumber, List<String> types) {
        return dao.findByOrderNumberAndServiceType(orderNumber, types);
    }

    public List<ErpDeliveryService> findByOrederId(String orderId) {
        return dao.findByOrederId(orderId);
    }

    @Transactional
    public void updateExceptionLogo(String exception, Date flowEndTime, Integer status, List ids) {
        dao.updateExceptionLogo(exception, flowEndTime, status, ids);
    }
}
