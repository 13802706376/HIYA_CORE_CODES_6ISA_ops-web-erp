package com.yunnex.ops.erp.modules.shop.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.shop.dto.JykServiceResponseDto;
import com.yunnex.ops.erp.modules.shop.dto.OperationServiceResponseDto;
import com.yunnex.ops.erp.modules.shop.dto.ShopInfoExcelResponseDto;
import com.yunnex.ops.erp.modules.shop.dto.ShopServiceResponseDto;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;

/**
 * 商户管理DAO接口
 * 
 * @author huanghaidong
 * @version 2017-10-24
 */
@MyBatisDao
public interface ErpShopInfoDao extends CrudDao<ErpShopInfo> {

    long total(ErpShopInfo erpShopInfo);

    Integer countShopByZhangbeiId(@Param("zhangbeiId") String zhangbeiId);

    int updateByZhangbeiId(@Param("erpShopInfo") ErpShopInfo erpShopInfo);
    
    ErpShopInfo findListByZhangbeiId(@Param("zhangbeiId") String zhangbeiId);

    ErpShopInfo findByZhangbeiId(@Param("zhangbeiId") String zhangbeiId);

    int updateIntoPiecesById(@Param("id") String id);
    
    List<ErpShopInfo> findshopwaiter(@Param("shopid") String shopid);
    
    List<ErpShopInfo> findshoprole(@Param("shopid") String shopid);
    
    List<ErpShopInfo> findshoproleTwo(@Param("shopid") String shopid);
    
    /**
     * 通过掌贝ID获取商户信息
     *
     * @param zhangbeiId
     * @return
     * @date 2017年12月12日
     * @author SunQ
     */
    ErpShopInfo getByZhangbeiID(@Param("zhangbeiId") String zhangbeiId);
    
    /**
     * 通过掌贝ID更新商户的密码
     *
     * @param zhangbeiId
     * @param password
     * @return
     * @date 2017年12月12日
     * @author SunQ
     */
    int updateShopPassword(@Param("zhangbeiId") String zhangbeiId, @Param("password") String password);

    /**
     * 更新商户登录名
     * 
     * @param zhangbeiId
     * @param loginName
     * @return
     */
    int updateShopLoginName(@Param("zhangbeiId") String zhangbeiId, @Param("loginName") String loginName);
 
    /**
     * 更新商户进件状态
     *
     * @param erpShopInfo
     * @return
     * @date 2017年12月12日
     * @author SunQ
     */
    int updateShopState(@Param("erpShopInfo") ErpShopInfo erpShopInfo);

   
    /**
     * 通过订单ID查找商户
     * 
     * @param zhangbeiId
     * @return
     */
    ErpShopInfo getByOrderID(@Param("orderId") String orderId);

    int updateStoreInfo(@Param("zhangbeiState") Integer zhangbeiState, @Param("storeCount") Integer storeCount, @Param("isAbnormal") String isAbnormal,
                    @Param("zhangbeiId") String zhangbeiId);
    


    
    /**
     * 获取商户当前的任务
     *
     * @param zhangbeiId
     * @return
     * @date 2018年1月9日
     * @author SunQ
     */
    List<String> findShopTaskId(@Param("zhangbeiId") String zhangbeiId);

    /**
     * 获取商户提交微信进件的门店
     *
     * @param zhangbeiId
     * @return
     * @date 2018年1月25日
     * @author SunQ
     */
    Integer countApplyWechatpayByShopId(@Param("zhangbeiId") String zhangbeiId);
    
    /**
     * 
     *
     * @param zhangbeiId
     * @return
     * @date 2018年1月25日
     * @author SunQ
     */
    Integer countApplyUnionpayByShopId(@Param("zhangbeiId") String zhangbeiId);
    
    ErpShopInfo findBySplitId(String splitId);
    
    /**
     * 更新商户异常状态
     *
     * @param zhangbeiId
     * @param isAbnormal
     * @return
     * @date 2018年4月2日
     * @author SunQ
     */
    int updateAbnormal(@Param("zhangbeiId") String zhangbeiId, @Param("isAbnormal") String isAbnormal);
   
    int updateAlipaStateById(@Param("id") String id, @Param("alipaState") String alipaState);

    Integer findServicePendingNum(@Param("zhangbeiId") String zhangbeiId);

    Integer findJykPendingNum(@Param("zhangbeiId") String zhangbeiId);

    List<ShopInfoExcelResponseDto> findByPageWithExcel(ErpShopInfo entity);
    
    /**
     * 业务定义：搜索查询服务商信息-分页
     * 
     * @date 2018年7月3日
     * @author R/Q
     */
    List<Map<String, Object>> agentSearchList(@Param("paramObj") ErpShopInfo paramObj, Page<Map<String, Object>> page);

    /**
     * 业务定义：查询服务商信息
     * 
     * @date 2018年7月3日
     * @author R/Q
     */
    List<Map<String, Object>> agentSearchList(@Param("paramObj") ErpShopInfo paramObj);

    /**
     * 更新商户下面所有正在运行的流程对应的运营顾问
     *
     * @param zhangbeiId
     * @param operationAdviserId
     * @date 2018年7月3日
     */
    int updateOpsAdviserOfShop(@Param("zhangbeiId") String zhangbeiId, @Param("operationAdviserId") String operationAdviserId);

    /**
     * 根据掌贝id查询商户下面的所有待处理服务信息
     *
     * @param zhangbeiId
     * @return
     * @date 2018年7月4日
     */
    List<OperationServiceResponseDto> getOperationServiceInfoByZhangbeiId(@Param("type") String type, @Param("zhangbeiId") String zhangbeiId);


    /**
     * 根据掌贝id获取商户下面的聚引客商品处理信息
     *
     * @param type 获取数据类型（pending待处理，process处理中，finish处理完成）
     * @param zhangbeiId
     * @return
     * @date 2018年7月5日
     */
    List<JykServiceResponseDto> getJykGoodInfoByZhangbeiId(@Param("type") String type, @Param("zhangbeiId") String zhangbeiId);


    /**
     * 根据掌贝id获取商户下面待处理,处理中,处理完成的总数量
     * type="operationService",则查询商户运营服务的总数量，type="jykService",则查询聚引客商品总数量
     * 
     * @param type
     * @param zhangbeiId
     * @return
     * @date 2018年7月4日
     */
    Map<String, Object> getServiceSumByZhangbeiId(@Param("serviceType") String serviceType, @Param("zhangbeiId") String zhangbeiId);


    /**
     * 根据掌贝id查询商户
     *
     * @param zhangbeiId
     * @return
     * @date 2018年7月9日
     */
    List<ShopServiceResponseDto> getServiceInfoByShopNumber(@Param("shopNumber") String shopNumber);


    /**
     * 根据掌贝id获取商户信息
     *
     * @param zhangbeiId
     * @return
     * @date 2018年7月24日
     * @author linqunzhi
     */
    ErpShopInfo getByZhangbeiId(@Param("zhangbeiId") String zhangbeiId);




    /**
     * 根据商户主键ID查询商户绑定的运营顾问名称
     *
     * @param zhangbeiId
     * @return
     * @date 2018年7月25日
     */
    String getOpsAdviserNameByShopInfoId(String shopInfoId);


}
