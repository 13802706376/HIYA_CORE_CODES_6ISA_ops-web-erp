package com.yunnex.ops.erp.modules.promotion.web;

import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.druid.util.StringUtils;
import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.constant.CommonConstants;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderCouponOutput;
import com.yunnex.ops.erp.modules.order.service.ErpOrderCouponOutputService;
import com.yunnex.ops.erp.modules.promotion.constant.PromotionConstants;
import com.yunnex.ops.erp.modules.promotion.entity.ErpOrderCouponReceiveRecord;
import com.yunnex.ops.erp.modules.promotion.entity.ErpPromoteDataFriends;
import com.yunnex.ops.erp.modules.promotion.entity.ErpPromoteDataMomo;
import com.yunnex.ops.erp.modules.promotion.entity.ErpPromoteDataWeibo;
import com.yunnex.ops.erp.modules.promotion.service.ErpOrderCouponReceiveRecordService;
import com.yunnex.ops.erp.modules.promotion.service.ErpPromoteDataFriendsService;
import com.yunnex.ops.erp.modules.promotion.service.ErpPromoteDataMomoService;
import com.yunnex.ops.erp.modules.promotion.service.ErpPromoteDataWeiboService;

/**
 * 朋友圈推广数据Controller
 * 
 * @author yunnex
 * @version 2018-05-09
 */
@Controller
@RequestMapping(value = "${adminPath}/promotion/erpPromoteDataInfo")
public class ErpPromoteDataInfoController extends BaseController {

    @Autowired
    private ErpPromoteDataFriendsService erpPromoteDataFriendsService;

    @Autowired
    private ErpPromoteDataWeiboService erpPromoteDataWeiboService;

    @Autowired
    private ErpPromoteDataMomoService erpPromoteDataMomoService;

    @Autowired
    private ErpOrderCouponReceiveRecordService ErpOrderCouponReceiveRecordService;

    @Autowired
    private ErpOrderCouponOutputService erpOrderCouponOutputService;

    /**
     * 
     * 业务定义：跳转推广数据管理主页面
     * 
     * @date 2018年5月10日
     * @author R/Q
     */
    @RequiresPermissions("promotion:erpPromoteData:view")
    @RequestMapping(value = "promoteDataList")
    public String promoteDataList() {
        return "modules/promotion/promoteDataList";
    }

    /**
     * 
     * 业务定义：跳转推广数据管理详情页面
     * 
     * @date 2018年5月10日
     * @author R/Q
     */
    @RequiresPermissions("promotion:erpPromoteData:view")
    @RequestMapping(value = "promoteDetail")
    public String promoteDetail() {
        return "modules/promotion/promoteDetail";
    }

    /**
     * 
     * 业务定义：跳转推广数据管理报表页面
     * 
     * @date 2018年5月10日
     * @author R/Q
     */
    @RequiresPermissions("promotion:erpPromoteData:view")
    @RequestMapping(value = "promoteReports")
    public String promoteReportsl() {
        return "modules/promotion/promoteReports";
    }

    /**
     * 
     * 业务定义：跳转推广数据新增数据页面
     * 
     * @date 2018年5月10日
     * @author R/Q
     */
    @RequiresPermissions("promotion:erpPromoteData:view")
    @RequestMapping(value = "promoteAdd")
    public String promoteAdd() {
        return "modules/promotion/promoteAdd";
    }

    /**
     * 
     * 业务定义：查询朋友圈推广数据
     * 
     * @date 2018年5月9日
     * @author R/Q
     */
    @ResponseBody
    @RequestMapping(value = "/queryFriendspromoteData")
    public Object queryFriendspromoteData(ErpPromoteDataFriends erpPromoteDataFriends) {
        return erpPromoteDataFriendsService.queryFriendsPromoteData(erpPromoteDataFriends);
    }

    /**
     * 
     * 业务定义：依据ID查询朋友圈数据
     * 
     * @date 2018年5月10日
     * @author R/Q
     */
    @ResponseBody
    @RequestMapping(value = "/queryFriendspromoteDataById")
    public Object queryFriendspromoteDataById(ErpPromoteDataFriends paramObj) {
        return erpPromoteDataFriendsService.queryFriendsData(paramObj);
    }

    /**
     * 
     * 业务定义：保存朋友圈数据
     * 
     * @date 2018年5月10日
     * @author R/Q
     */
    @ResponseBody
    @RequestMapping(value = "/saveFriendsData")
    public Object saveFriendsData(ErpPromoteDataFriends erpPromoteDataFriends) {
        return erpPromoteDataFriendsService.saveFriendsData(erpPromoteDataFriends);
    }

    /**
     * 
     * 业务定义：删除朋友圈数据
     * 
     * @date 2018年5月10日
     * @author R/Q
     */
    @ResponseBody
    @RequestMapping(value = "/deleteFriendsData")
    public Object deleteFriendsData(@RequestBody ErpPromoteDataFriends erpPromoteDataFriends) {
        return erpPromoteDataFriendsService.deleteFriendsData(erpPromoteDataFriends);
    }

    /**
     * 
     * 业务定义：查询微博推广数据
     * 
     * @date 2018年5月10日
     * @author R/Q
     */
    @ResponseBody
    @RequestMapping(value = "/queryWeiboPromoteData")
    public Object queryWeiboPromoteData(ErpPromoteDataWeibo paramObj) {
        return erpPromoteDataWeiboService.queryWeiboPromoteData(paramObj);
    }

    /**
     * 
     * 业务定义：根据ID查询微博推广数据
     * 
     * @date 2018年5月10日
     * @author R/Q
     */
    @ResponseBody
    @RequestMapping(value = "/queryWeiboPromoteDataById")
    public Object queryWeiboPromoteDataById(ErpPromoteDataWeibo paramObj) {
        return erpPromoteDataWeiboService.queryWeiboPromoteDataById(paramObj);
    }

    /**
     * 
     * 业务定义：保存微博推广数据
     * 
     * @date 2018年5月10日
     * @author R/Q
     */
    @ResponseBody
    @RequestMapping(value = "/saveWeiboData")
    public Object saveWeiboData(ErpPromoteDataWeibo paramObj) {
        return erpPromoteDataWeiboService.saveWeiboData(paramObj);
    }

    /**
     * 
     * 业务定义：删除微博数据
     * 
     * @date 2018年5月10日
     * @author R/Q
     */
    @ResponseBody
    @RequestMapping(value = "/deleteWeiboData")
    public Object deleteWeiboData(@RequestBody ErpPromoteDataWeibo paramObj) {
        return erpPromoteDataWeiboService.deleteWeiboData(paramObj);
    }

    /**
     * 
     * 业务定义：查询陌陌推广数据
     * 
     * @date 2018年5月10日
     * @author R/Q
     */
    @ResponseBody
    @RequestMapping(value = "/queryMomoPromoteData")
    public Object queryMomoPromoteData(ErpPromoteDataMomo paramObj) {
        return erpPromoteDataMomoService.queryMomoPromoteData(paramObj);
    }

    /**
     * 
     * 业务定义：根据ID查询陌陌推广数据
     * 
     * @date 2018年5月10日
     * @author R/Q
     */
    @ResponseBody
    @RequestMapping(value = "/queryMomoPromoteDataById")
    public Object queryMomoPromoteDataById(ErpPromoteDataMomo paramObj) {
        return erpPromoteDataMomoService.queryMomoPromoteDataById(paramObj);
    }

    /**
     * 
     * 业务定义：保存陌陌推广数据
     * 
     * @date 2018年5月10日
     * @author R/Q
     */
    @ResponseBody
    @RequestMapping(value = "/saveMomoData")
    public Object saveMomoData(ErpPromoteDataMomo paramObj) {
        return erpPromoteDataMomoService.saveMomoData(paramObj);
    }

    /**
     * 业务定义：删除陌陌数据
     * 
     * @date 2018年5月10日
     * @author R/Q
     */
    @ResponseBody
    @RequestMapping(value = "/deleteMomoData")
    public Object deleteMomoData(@RequestBody ErpPromoteDataMomo paramObj) {
        return erpPromoteDataMomoService.deleteMomoData(paramObj);
    }

    /**
     * 业务定义：上传推广数据
     * 
     * @date 2018年5月14日
     * @author R/Q
     */
    @ResponseBody
    @RequestMapping(value = "/importPrommteData")
    public Object importPrommteData(MultipartFile uploadFile, String splitOrderId, String importType) {
        Map<String, Object> returnMap = Maps.newHashMap();
        try {
            if (StringUtils.equals(importType, PromotionConstants.IMPORT_TYPE_FRIENDS)) {
                erpPromoteDataFriendsService.importPrommteData(uploadFile, splitOrderId);
            } else if (StringUtils.equals(importType, PromotionConstants.IMPORT_TYPE_MOMO)) {
                erpPromoteDataMomoService.importPrommteData(uploadFile, splitOrderId);
            } else if (StringUtils.equals(importType, PromotionConstants.IMPORT_TYPE_WEIBO)) {
                erpPromoteDataWeiboService.importPrommteData(uploadFile, splitOrderId);
            } else {
                returnMap.put(CommonConstants.RETURN_CODE, CommonConstants.RETURN_CODE_FAIL);
                returnMap.put(CommonConstants.RETURN_MESSAGE, "请选择正确的上传类型。");
                return returnMap;
            }
            returnMap.put(CommonConstants.RETURN_CODE, CommonConstants.RETURN_CODE_SUCCESS);
        } catch (ServiceException se) {
            returnMap.put(CommonConstants.RETURN_CODE, CommonConstants.RETURN_CODE_FAIL);
            returnMap.put(CommonConstants.RETURN_MESSAGE, se.getMessage());
        } catch (Exception e) {
            logger.error("推广数据管理-上传数据失败，splitOrderId={}，importType={}，错误信息={}", splitOrderId, importType, e);
            returnMap.put(CommonConstants.RETURN_CODE, CommonConstants.RETURN_CODE_FAIL);
            returnMap.put(CommonConstants.RETURN_MESSAGE, "上传失败，系统错误。");
        }
        return returnMap;
    }

    /**
     * 业务定义：查询分单对应卡券信息
     * 
     * @date 2018年5月22日
     * @author R/Q
     */
    @ResponseBody
    @RequestMapping(value = "/queryCouponData")
    public Object queryCouponData(String splitOrderId) {
        return ErpOrderCouponReceiveRecordService.queryCouponData(splitOrderId);
    }

    /**
     * 业务定义：删除卡券领卷记录
     * 
     * @date 2018年5月22日
     * @author R/Q
     */
    @ResponseBody
    @RequestMapping(value = "/deleteCouponData")
    public Object deleteCouponData(@RequestBody ErpOrderCouponReceiveRecord paramObj) {
        Map<String, Object> returnMap = Maps.newHashMap();
        try {
            ErpOrderCouponReceiveRecordService.delete(paramObj);
            returnMap.put(CommonConstants.RETURN_CODE, CommonConstants.RETURN_CODE_SUCCESS);
        } catch (ServiceException se) {
            logger.error("推广数据-删除卡券领卷记录失败，paramObj={}，错误信息={}", paramObj, se);
            returnMap.put(CommonConstants.RETURN_CODE, CommonConstants.RETURN_CODE_FAIL);
        }
        return returnMap;
    }

    /**
     * 业务定义：保存卡券领券记录
     * 
     * @date 2018年5月22日
     * @author R/Q
     */
    @ResponseBody
    @RequestMapping(value = "/saveCouponData")
    public Object saveCouponData(@RequestBody ErpOrderCouponReceiveRecord paramObj) {
        Map<String, Object> returnMap = Maps.newHashMap();
        try {
            super.beanValidator(paramObj);
            ErpOrderCouponReceiveRecordService.save(paramObj);
            returnMap.put(CommonConstants.RETURN_CODE, CommonConstants.RETURN_CODE_SUCCESS);
        } catch (ServiceException se) {
            logger.error("推广数据-保存卡券领卷记录失败，paramObj={}，错误信息={}", paramObj, se);
            returnMap.put(CommonConstants.RETURN_CODE, CommonConstants.RETURN_CODE_FAIL);
            returnMap.put(CommonConstants.RETURN_MESSAGE, se.getMessage());
        }
        return returnMap;
    }

    /**
     * 业务定义：修改卡券核销数量
     * 
     * @date 2018年5月22日
     * @author R/Q
     */
    @ResponseBody
    @RequestMapping(value = "/saveWriteOffNum")
    public Object saveWriteOffNum(String couponOutputId, Integer writeOffNum) {
        Map<String, Object> returnMap = Maps.newHashMap();
        try {
            if (StringUtil.isBlank(couponOutputId) || writeOffNum == null) {
                throw new ServiceException("请求参数异常");
            }
            ErpOrderCouponOutput paramObj = erpOrderCouponOutputService.get(couponOutputId);
            paramObj.setWriteOffNum(writeOffNum);
            erpOrderCouponOutputService.save(paramObj);
            returnMap.put(CommonConstants.RETURN_CODE, CommonConstants.RETURN_CODE_SUCCESS);
        } catch (ServiceException se) {
            logger.error("推广数据-修改卡券核销数量失败，couponOutputId={}，writeOffNum={}，错误信息={}", couponOutputId, writeOffNum, se);
            returnMap.put(CommonConstants.RETURN_CODE, CommonConstants.RETURN_CODE_FAIL);
            returnMap.put(CommonConstants.RETURN_MESSAGE, se.getMessage());
        }
        return returnMap;
    }

}
