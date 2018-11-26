package com.yunnex.ops.erp.modules.shop.api;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.utils.ResponeUtil;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.shop.service.ErpShopInfoApiService;
import com.yunnex.ops.erp.modules.store.basic.service.ErpSyncAuditApiService;
import com.yunnex.ops.erp.modules.workflow.flow.service.SdiFlowSignalService;

/**
 * 商户信息对外接口
 * 
 * @author SunQ
 * @date 2017年12月12日
 */
@Controller
@RequestMapping(value = "api/shop/")
public class ErpShopInfoApiController extends BaseController {
    private static final int STATE_TYPE_0 = 0;
    private static final int STATE_TYPE_1 = 1;
    private static final int STATE_TYPE_2 = 2;
    private static final int STATE_TYPE_3 = 3;

    private static final int STATE_0 = 0;
    private static final int STATE_2 = 2;

    @Autowired
    private ErpShopInfoApiService erpShopInfoApiService;
    @Autowired
    private ErpSyncAuditApiService erpSyncAuditApiService;
    @Autowired
    private SdiFlowSignalService sdiFlowSignalService;

    /**
     * 同步商户信息
     * 
     * @param jsonObject 请求参数 page：页码，pageSize: 分页大小，phoneNumbers：掌贝ID（数组）
     * @return 请求结果 0：成功，1：失败
     */
    @RequestMapping(value = "syncShopInfo", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> syncShopInfo(@RequestBody JSONObject jsonObject) {
        Map<String, Object> result = Maps.newHashMap();
        String msg = "同步商户信息";
        try {
            JSONObject data = erpShopInfoApiService.getDataFromOem(jsonObject);
            if (!ResponeUtil.isResponeValid(data)) {
                String code = data.getString("code");
                String errmsg = data.getString("errmsg");
                msg = msg + ": OEM响应失败! code: " + code + ", errmsg: " + errmsg;
                result.put("code", STATE_TYPE_1);
                result.put("message", msg);
                return result;
            }

            JSONArray shops = data.getJSONArray("shops");
            if (shops == null) {
                result.put("code", STATE_TYPE_2);
                result.put("message", "无商户信息返回, 请求参数: " + jsonObject);
                return result;
            }

            for (int i = 0; i < shops.size(); i++) {
                erpShopInfoApiService.syncOne(shops.getJSONObject(i));
            }
            result.put("code", 0);
            result.put("message", msg + "成功！");
        } catch (RuntimeException e) {
            logger.error(msg + "异常：", e);
            result.put("code", STATE_TYPE_3);
            result.put("message", msg + "失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 同步商户登录名接口
     */
    @RequestMapping(value = "syncShopLoginName", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject syncShopLoginName(String zhangbeiId, String loginName) {
        JSONObject resObject = new JSONObject();
        try {
            logger.info("syncShopLoginName->同步商户的登录名：zhangbeiId = {}, loginName = {}", zhangbeiId, loginName);
            if (StringUtils.isNotBlank(zhangbeiId) && StringUtils.isNotBlank(loginName)) {
                Map<String, String> map = erpShopInfoApiService.syncShopLoginName(zhangbeiId, loginName);
                if ("success".equals(map.get("state"))) {
                    resObject.put("code", 0);
                    resObject.put("message", map.get("message"));
                    logger.info("登录名更新成功");
                } else {
                    resObject.put("code", 1);
                    resObject.put("message", map.get("message"));
                    logger.info("登录名更新失败");
                }
            } else {
                resObject.put("code", 1);
                resObject.put("message", "zhangbeiId和loginName不能为空");
                logger.info("登录名更新失败");
            }
        } catch (RuntimeException e) {
            resObject.put("code", 1);
            resObject.put("message", "系统异常");
            logger.info("syncShopLoginName->出现异常：{}", e.getMessage(), e);
        }
        return resObject;
    }

    /**
     * 同步密码接口
     *
     * @return
     * @date 2017年12月12日
     * @author SunQ
     */
    @RequestMapping(value = "syncShopPassword")
    @ResponseBody
    public JSONObject syncShopPassword(String shopId, String passWord) {

        JSONObject resObject = new JSONObject();
        try {
            logger.info("syncShopPassword->同步密码的商户：shopId = {}", shopId);
            if (StringUtils.isNotBlank(shopId) && StringUtils.isNotBlank(passWord)) {
                Map<String, String> map = erpShopInfoApiService.syncShopPassword(shopId, passWord);
                if ("success".equals(map.get("state"))) {
                    resObject.put("code", 0);
                    resObject.put("message", map.get("message"));
                    logger.info("密码更新成功");
                } else {
                    resObject.put("code", 1);
                    resObject.put("message", map.get("message"));
                    logger.info("密码更新失败");
                }
            } else {
                resObject.put("code", 1);
                resObject.put("message", "shopId和passWord不能为空");
                logger.info("密码更新失败");
            }
        } catch (RuntimeException e) {
            resObject.put("code", 1);
            resObject.put("message", "系统异常");
            logger.info("syncShopPassword->出现异常：{}", e.getMessage());
        }
        return resObject;
    }

    /**
     * 同步进件状态接口
     *
     * @return
     * @date 2017年12月12日
     * @author SunQ
     */
    @RequestMapping(value = "syncShopState", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject syncShopState(@RequestBody Map<String, Object> params) {
        JSONObject resObject = new JSONObject();
        try {
        	logger.info("syncShopState->获取的参数：params = {}", params);
            String shopId = (params.get("shopId") == null ? "" : params.get("shopId").toString());
            String passWord = (params.get("passWord") == null ? "" : params.get("passWord").toString());
            Integer stateType = (params.get("stateType") == null ? 0 : Integer.parseInt(params.get("stateType").toString()));
            Integer state = (params.get("state") == null ? 0 : Integer.parseInt(params.get("state").toString()));
            String machineToolNumber = (params.get("machineToolNumber") == null ? "" : params.get("machineToolNumber").toString());
            String remarks = (params.get("remarks") == null ? "" : params.get("remarks").toString());
            String bankCardNum = (params.get("bankCardNum") == null ? "" : params.get("bankCardNum").toString());
            String businessLicenseNum = (params.get("businessLicenseNum") == null ? "" : params.get("businessLicenseNum").toString());
            if (StringUtils.isNotBlank(shopId)) {
                ErpShopInfo erpShopInfo = new ErpShopInfo();
                erpShopInfo.setZhangbeiId(shopId);

                if (stateType.intValue() == STATE_TYPE_0) {
                    resObject.put("code", 1);
                    resObject.put("message", "stateType不能为空");
                    logger.info("状态更新失败");
                    return resObject;
                }
                if (state.intValue() == STATE_0) {
                    resObject.put("code", 1);
                    resObject.put("message", "state不能为空");
                    logger.info("状态更新失败");
                    return resObject;
                }

                StringBuffer sbStr = new StringBuffer("");
                if (remarks != null) {
                    JSONArray array = JSONArray.parseArray(remarks);
                    if (array != null && !array.isEmpty()) {
                        for (int i = 0; i < array.size(); i++) {
                            sbStr.append(array.getString(i));
                            sbStr.append(";");
                        }
                    }
                }
                if (stateType == STATE_TYPE_1) {
                    // 将掌贝进件状态更新至门店信息
                    if (erpSyncAuditApiService.syncAudit(shopId, businessLicenseNum, bankCardNum, state, sbStr.toString(), "0")) {
                        if (state == STATE_2 && StringUtils.isNotBlank(passWord)) {
                            erpShopInfo.setPassword(passWord);
                        }
                        erpShopInfo.setZhangbeiState(state);
                        erpShopInfo.setZhangbeiRemark(sbStr.toString());
                    } else {
                        resObject.put("code", 1);
                        resObject.put("message", "更新信息至门店失败");
                        logger.info("状态更新失败");
                        return resObject;
                    }
                }
                if (stateType == STATE_TYPE_2) {
                    if (StringUtils.isBlank(bankCardNum) || StringUtils.isBlank(businessLicenseNum)) {
                        resObject.put("code", 1);
                        resObject.put("message", "bankCardNum和businessLicenseNum不能为空");
                        logger.info("状态更新失败");
                        return resObject;
                    }

                    // 将微信进件状态更新至门店信息
                    if (erpSyncAuditApiService.syncAudit(shopId, businessLicenseNum, bankCardNum, state, sbStr.toString(), "1")) {
                        erpShopInfo.setWechatpayState(state);
                        erpShopInfo.setWechatpayRemark(sbStr.toString());
                    } else {
                        resObject.put("code", 1);
                        resObject.put("message", "更新信息至门店失败");
                        logger.info("状态更新失败");
                        return resObject;
                    }
                }
                if (stateType == STATE_TYPE_3) {
                    if (StringUtils.isBlank(bankCardNum) || StringUtils.isBlank(businessLicenseNum)) {
                        resObject.put("code", 1);
                        resObject.put("message", "bankCardNum和businessLicenseNum不能为空");
                        logger.info("状态更新失败");
                        return resObject;
                    }
                    // 将银联进件状态更新至门店信息
                    if (erpSyncAuditApiService.syncAudit(shopId, businessLicenseNum, bankCardNum, machineToolNumber, state, sbStr.toString(), "2")) {
                        erpShopInfo.setUnionpayState(state);
                        erpShopInfo.setUnionpayRemark(sbStr.toString());
                    } else {
                        resObject.put("code", 1);
                        resObject.put("message", "更新信息至门店失败");
                        logger.info("状态更新失败");
                        return resObject;
                    }
                }
                Map<String, String> map = erpShopInfoApiService.syncShopState(erpShopInfo);
                if ("success".equals(map.get("state"))) {
                    resObject.put("code", 0);
                    resObject.put("message", map.get("message"));
                    logger.info("状态更新成功");

                    // 对于审核通过的状态，同时更新流程的进展
                    if (stateType == STATE_TYPE_1 && state == STATE_2) {
                        // 掌贝进件审核通过
                        // 通知资料录入流程
                        sdiFlowSignalService.zhangbeiIntopiece(shopId);
                        sdiFlowSignalService.deliveryServiceFlowZhangbeiIntopiece(shopId);
                    } else if (stateType == STATE_TYPE_2 && state == STATE_2) {
                        // 微信进件审核通过
                        // 通知资料录入流程
                        sdiFlowSignalService.wechatPayIntopiece(shopId, businessLicenseNum, bankCardNum);
                    } else if (stateType == STATE_TYPE_3 && state == STATE_2) {
                        // 银联进件审核通过
                        // 通知资料录入流程
                        sdiFlowSignalService.unionPayIntopiece(shopId, businessLicenseNum, bankCardNum);
                    }
                } else {
                    resObject.put("code", 1);
                    resObject.put("message", map.get("message"));
                    logger.info("状态更新失败");
                }
            } else {
                resObject.put("code", 1);
                resObject.put("message", "shopId不能为空");
                logger.info("状态更新失败");
            }
        } catch (RuntimeException e) {
            resObject.put("code", 1);
            resObject.put("message", "系统异常");
            logger.info("syncShopState->出现异常：{}", e.getMessage());
            logger.error(e.getMessage(), e);
        }
        return resObject;
    }

    /**
     * 根据商户编号查询商户下面的服务信息
     *
     * @param number
     * @return
     * @date 2018年7月9日
     */
    @RequestMapping(value = "getShopServiceInfoByNumber")
    @ResponseBody
    public BaseResult getShopServiceInfoByNumber(String number) {
        return erpShopInfoApiService.getShopServiceInfoByNumber(number);
    }
}
