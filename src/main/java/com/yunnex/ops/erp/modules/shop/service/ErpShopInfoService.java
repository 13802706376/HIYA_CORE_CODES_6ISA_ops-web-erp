package com.yunnex.ops.erp.modules.shop.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.result.IllegalArgumentErrorResult;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.common.utils.excel.FastExcel;
import com.yunnex.ops.erp.modules.act.dao.ActDao;
import com.yunnex.ops.erp.modules.shop.dao.ErpShopInfoDao;
import com.yunnex.ops.erp.modules.shop.dto.JykServiceResponseDto;
import com.yunnex.ops.erp.modules.shop.dto.OperationServiceResponseDto;
import com.yunnex.ops.erp.modules.shop.dto.ShopInfoExcelResponseDto;
import com.yunnex.ops.erp.modules.shop.dto.ShopServiceResponseDto;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreInfoService;


/**
 * 商户管理Service
 * 
 * @author huanghaidong
 * @version 2017-10-24
 */
@Service
public class ErpShopInfoService extends CrudService<ErpShopInfoDao, ErpShopInfo> {

    /**
     * 商户管理Dao
     */
    @Autowired
    private ErpShopInfoDao erpShopInfoDao;

    @Autowired
    private ActDao actDao;

    @Autowired
    @Lazy(true)
    private ErpStoreInfoService storeService;

    @Override
    public ErpShopInfo get(String id) {
        return super.get(id);
    }

    @Override
    public List<ErpShopInfo> findList(ErpShopInfo erpShopInfo) {
        return super.findList(erpShopInfo);
    }

    public ErpShopInfo findListByZhangbeiId(String zhangbeiId) {
        ErpShopInfo shopInfo = erpShopInfoDao.findListByZhangbeiId(zhangbeiId);
        return shopInfo;
    }

    /**
     * 查找未被删除的商户
     * 
     * @param zhangbeiId
     * @return
     */
    public ErpShopInfo findByZhangbeiId(String zhangbeiId) {
        return dao.findByZhangbeiId(zhangbeiId);
    }

    @Override
    public Page<ErpShopInfo> findPage(Page<ErpShopInfo> page, ErpShopInfo erpShopInfo) {
        erpShopInfo.setPage(page);
        page.setAutoCount(false);
        page.setPageQuery(false);
        page.setCount(dao.total(erpShopInfo));
        page.setPageQuery(true);
        return super.findPage(page, erpShopInfo);
    }

    @Override
    @Transactional(readOnly = false)
    public void save(ErpShopInfo erpShopInfo) {
        super.save(erpShopInfo);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(ErpShopInfo erpShopInfo) {
        super.delete(erpShopInfo);
    }

    /**
     * 获取掌贝ID的商户数量
     *
     * @param zhangbeiId
     * @return
     * @date 2018年2月7日
     * @author SunQ
     */
    public int countShopByZhangbeiId(String zhangbeiId) {
        Integer count = erpShopInfoDao.countShopByZhangbeiId(zhangbeiId);
        return count == null ? 0 : count.intValue();
    }

    @Transactional(readOnly = false)
    public boolean updateByZhangbeiId(ErpShopInfo erpShopInfo) {
        return erpShopInfoDao.updateByZhangbeiId(erpShopInfo) > 0;
    }

    @Transactional(readOnly = false)
    public boolean updateIntoPiecesById(String id) {
        return erpShopInfoDao.updateIntoPiecesById(id) > 0;
    }

    @Transactional(readOnly = false)
    public boolean insert(ErpShopInfo erpShopInfo) {
        return erpShopInfoDao.insert(erpShopInfo) > 0;
    }

    public Page<ErpShopInfo> searchList(Page<ErpShopInfo> page, ErpShopInfo erpShopInfo) {

        erpShopInfo.setPage(page);
        page.setList(erpShopInfoDao.findAllList(erpShopInfo));
        return page;
    }
    
   public List<ErpShopInfo> findshopwaiter(String shopid)
   {
    return erpShopInfoDao.findshopwaiter(shopid);
       
   }
   
   public List<ErpShopInfo> findshoprole(String shopid){
       return erpShopInfoDao.findshoprole(shopid);
   }
   
   public List<ErpShopInfo> findshoproleTwo(String shopid){
       return erpShopInfoDao.findshoproleTwo(shopid);
   }
   
   
   public ErpShopInfo getByZhangbeiID(String shopId) {
       return erpShopInfoDao.getByZhangbeiID(shopId);
   }
   
   @Transactional(readOnly = false)
   public boolean updateShopPassword(String shopId, String passWord) {
       return erpShopInfoDao.updateShopPassword(shopId, passWord) > 0;
   }
   
    @Transactional(readOnly = false)
    public boolean updateShopLoginName(String zhangbeiId, String loginName) {
        return erpShopInfoDao.updateShopLoginName(zhangbeiId, loginName) > 0;
    }

   @Transactional(readOnly = false)
   public boolean updateShopState(ErpShopInfo erpShopInfo) {
       return erpShopInfoDao.updateShopState(erpShopInfo) > 0;
   }
  
   public ErpShopInfo getByOrderID(String orderId) {
       return erpShopInfoDao.getByOrderID(orderId);
   }
   
   @Transactional(readOnly = false)
    public int updateStoreInfo(Integer zhangbeiState, Integer storeCount, String isAbnormal, String zhangbeiId) {
        return erpShopInfoDao.updateStoreInfo(zhangbeiState, storeCount, isAbnormal, zhangbeiId);
   }
   
   public List<String> findShopTaskId(String zhangbeiId){
       return erpShopInfoDao.findShopTaskId(zhangbeiId);
   }
   
   public int countApplyWechatpayByShopId(@Param("zhangbeiId") String zhangbeiId) {
       Integer count = erpShopInfoDao.countApplyWechatpayByShopId(zhangbeiId);
       return count == null ? 0 : count.intValue();
   }
   
   public int countApplyUnionpayByShopId(@Param("zhangbeiId") String zhangbeiId) {
       Integer count = erpShopInfoDao.countApplyUnionpayByShopId(zhangbeiId);
       return count == null ? 0 : count.intValue();
   }
   
   @Transactional(readOnly = false)
   public boolean updateAbnormal(String zhangbeiId, String isAbnormal) {
       return erpShopInfoDao.updateAbnormal(zhangbeiId, isAbnormal) > 0;
   }
   
   public static String strSetNULLtoEmpty(Object str){
       if(str==null){
           return "";  
       }
       return str.toString();
   } 
   @Transactional(readOnly = false)
   public void updateAlipaStateById(String id, String alipaState) {
       erpShopInfoDao.updateAlipaStateById(id, alipaState);
   }

    /**
     * 根据掌贝id查询商户下面的待处理服务数量
     *
     * @param zhangbeiId
     * @return
     * @throws ServiceException
     * @date 2018年7月3日
     */
    public Integer findServicePendingNum(String zhangbeiId) throws ServiceException {
        if (StringUtils.isBlank(zhangbeiId)) {
            throw new ServiceException("掌贝id不能为空");
        }

        return erpShopInfoDao.findServicePendingNum(zhangbeiId);
    }

    /**
     * 根据掌贝id查询商户下面的聚引客待处理数量
     *
     * @param zhangbeiId
     * @return
     * @throws ServiceException
     * @date 2018年7月3日
     */
    public Integer findJykPendingNum(String zhangbeiId) throws ServiceException {
        if (StringUtils.isBlank(zhangbeiId)) {
            throw new ServiceException("掌贝id不能为空");
        }

        return erpShopInfoDao.findJykPendingNum(zhangbeiId);
    }

    public JSONObject export(ErpShopInfo entity, HttpServletResponse response) {
        logger.info("商户列表 - Excel导出入参：requestDto = {}", JSON.toJSON(entity));
        List<ShopInfoExcelResponseDto> list = erpShopInfoDao.findByPageWithExcel(entity);

        JSONObject result = new JSONObject();
        if (CollectionUtils.isNotEmpty(list)) {
            for (ShopInfoExcelResponseDto dto : list) {
                if (dto.getStoreCount() == null) {
                    dto.setStoreCount(Constant.ZERO);
                }
                // 运营服务待处理
                if (dto.getPendingServiceNum() == null) {
                    dto.setPendingServiceNum(Constant.ZERO);
                }
                // 聚引客待处理
                if (dto.getPendingJykNum() == null) {
                    dto.setPendingJykNum(Constant.ZERO);
                }
            }
        }

        try {
            FastExcel.exportExcel(response, "商户列表", list);
        } catch (IOException e) {
            String msg = "商户列表导出失败！";
            logger.error(msg, e);
            result.put("code", BaseResult.CODE_ERROR_ARG);
            result.put("message", msg);
            return result;
        }
        result.put("code", 0);
        result.put("message", "导出商户列表成功！");
        return result;
    }
   
    /**
     * 业务定义：搜索查询服务商信息-分页
     * 
     * @date 2018年7月3日
     * @author R/Q
     */
    public Page<Map<String, Object>> agentSearchList(ErpShopInfo paramObj, Page<Map<String, Object>> page) {
        List<Map<String, Object>> agentList = super.dao.agentSearchList(paramObj, page);
        page.setList(agentList);
        return page;
    }

    /**
     * 业务定义：查询服务商信息
     * 
     * @date 2018年7月3日
     * @author R/Q
     */
    public List<Map<String, Object>> agentSearchList(ErpShopInfo paramObj) {
        return super.dao.agentSearchList(paramObj);
    }

    @Transactional
    public BaseResult updateOpsAdviserOfShop(String zhangbeiId, String operationAdviserId) {
        if (StringUtils.isBlank(zhangbeiId) || StringUtils.isBlank(operationAdviserId)) {
            return new IllegalArgumentErrorResult();
        }

        // 更新商户绑定的运营顾问
        ErpShopInfo erpShopInfo = new ErpShopInfo();
        erpShopInfo.setZhangbeiId(zhangbeiId);
        erpShopInfo.setOperationAdviserId(operationAdviserId);
        erpShopInfoDao.updateByZhangbeiId(erpShopInfo);

        // 更新商户下面所有订单下面的正在运行的流程对应的运营顾问(不更新历史流程)
        erpShopInfoDao.updateOpsAdviserOfShop(zhangbeiId, operationAdviserId);

        // 更新表ACT_RU_TASK中ASSIGNEE_对应的任务指派人（只更新当前正在运营的任务并且该任务的指派人是运营顾问角色）
        actDao.updateAssigneeByZhangbeiId(zhangbeiId, operationAdviserId);
        return new BaseResult();
    }

    /**
     * 查询商户下面的待处理总数量
     *
     * @param zhangbeiId
     * @return
     * @date 2018年7月4日
     */
    public BaseResult getServiceSumByZhangbeiId(String serviceType, String zhangbeiId) {
        if (StringUtils.isBlank(zhangbeiId) || StringUtils.isBlank(serviceType)) {
            return new IllegalArgumentErrorResult();
        }

        if (!serviceType.equals("operationService") && !serviceType.equals("jykService")) {
            return new IllegalArgumentErrorResult();
        }

        Map<String, Object> sumMap = erpShopInfoDao.getServiceSumByZhangbeiId(serviceType, zhangbeiId);

        return new BaseResult().setAttach(sumMap);
    }

    /**
     * 根据掌贝id查询商户下面聚引客商品处理信息
     *
     * @param type
     * @param zhangbeiId
     * @return
     * @date 2018年7月4日
     */
    public BaseResult getJykGoodInfoByZhangbeiId(String type, String zhangbeiId) {
        if (StringUtils.isBlank(type) || StringUtils.isBlank(zhangbeiId)) {
            return new IllegalArgumentErrorResult();
        }

        if (!type.equals("pending") && !type.equals("process") && !type.equals("finish")) {
            return new IllegalArgumentErrorResult();
        }

        List<JykServiceResponseDto> jykGoodInfoList = erpShopInfoDao.getJykGoodInfoByZhangbeiId(type, zhangbeiId);
        return new BaseResult().setAttach(jykGoodInfoList);
    }

    /**
     * 根据掌贝id查询商户下面的运营服务信息，type="pending"查询待处理的，type=''process'查询处理中的，type='finish'查询处理完成的
     *
     * @param type
     * @param zhangbeiId
     * @return
     * @date 2018年7月6日
     */
    public BaseResult getOperationServiceByZhangbeiId(String type, String zhangbeiId) {
        if (StringUtils.isBlank(type) || StringUtils.isBlank(zhangbeiId)) {
            return new IllegalArgumentErrorResult();
        }

        if (!type.equals("pending") && !type.equals("process") && !type.equals("finish")) {
            return new IllegalArgumentErrorResult();
        }

        List<OperationServiceResponseDto> operationServiceInfoList = erpShopInfoDao.getOperationServiceInfoByZhangbeiId(type, zhangbeiId);
        if (CollectionUtils.isNotEmpty(operationServiceInfoList)) {
            for (OperationServiceResponseDto dto : operationServiceInfoList) {
            	if(dto!=null&&dto.getServiceTerm()!=null&&dto.getServiceTerm()==1200) {
            		dto.setServiceTerm(null);
            		dto.setExpirationTime(null);
            		dto.setAvailability("正常");
            	}else {
            		if (dto != null && dto.getExpirationTime() != null) {
                        dto.setAvailability(dto.getExpirationTime().before(new Date()) ? "无效" : "正常");
                    }
            	}
            }
        }
        return new BaseResult().setAttach(operationServiceInfoList);
    }

    public BaseResult getShopServiceInfoByNumber(String number) {
        if (StringUtils.isBlank(number)) {
            return new IllegalArgumentErrorResult();
        }
        List<ShopServiceResponseDto> list = erpShopInfoDao.getServiceInfoByShopNumber(number);
        return new BaseResult().setAttach(list);
    }

 public ErpShopInfo getByZhangbeiId(String zhangbeiId) {
        ErpShopInfo shopInfo = erpShopInfoDao.getByZhangbeiId(zhangbeiId);
        return shopInfo;
    }




public String getOpsAdviserNameByShopInfoId(String id) {
        if (StringUtils.isBlank(id)) {
            throw new ServiceException("没有传商户主键ID参数");
        }

        return dao.getOpsAdviserNameByShopInfoId(id);
    }


}
