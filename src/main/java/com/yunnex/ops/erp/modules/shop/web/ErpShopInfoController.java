package com.yunnex.ops.erp.modules.shop.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.common.utils.MD5Util;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.order.service.ErpOrderOriginalInfoService;
import com.yunnex.ops.erp.modules.qualify.service.ErpShopExtensionQualifyService;
import com.yunnex.ops.erp.modules.qualify.service.ErpShopPayQualifyService;
import com.yunnex.ops.erp.modules.shop.constant.ShopConstant;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.shop.service.ErpShopInfoApiService;
import com.yunnex.ops.erp.modules.shop.service.ErpShopInfoService;
import com.yunnex.ops.erp.modules.shop.service.ErpShopInfoSyncApiService;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreInfoService;
import com.yunnex.ops.erp.modules.sys.entity.Dict;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.service.DictService;
import com.yunnex.ops.erp.modules.sys.service.UserService;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;


/**
 * 商户管理Controller
 * 
 * @author huanghaidong
 * @version 2017-10-24
 */
@Controller
@RequestMapping(value = "${adminPath}/shop/erpShopInfo")
public class ErpShopInfoController extends BaseController {

    private static final int NUMBER_SIZE_6 = 6;
    
    @Autowired
    private ErpShopInfoService erpShopInfoService;
    
    @Autowired
    private DictService dictService;

    @Autowired
    private ErpShopPayQualifyService erpShopPayQualifyService;

    @Autowired
    private ErpShopExtensionQualifyService erpShopExtensionQualifyService;

    @Autowired
    private ErpShopInfoApiService erpShopInfoApiService;
    @Autowired
    private ErpStoreInfoService storeService;
    
    @Autowired
    private ErpOrderOriginalInfoService erpOrderOriginalInfoService;
    
    @Autowired
    private ErpShopInfoSyncApiService erpShopInfoSyncApiService;

    @Autowired
    private UserService userService;

    @ModelAttribute
    public ErpShopInfo get(@RequestParam(required=false) String id) {
        ErpShopInfo entity = null;
        if (StringUtils.isNotBlank(id)){
            entity = erpShopInfoService.get(id);
        }
        if (entity == null){
            entity = new ErpShopInfo();
        }
        return entity;
    }
    
    @RequiresPermissions("shop:erpShopInfo:view")
    @RequestMapping(value = {"list", ""})
    public String list(ErpShopInfo erpShopInfo, HttpServletRequest request, HttpServletResponse response, Model model) throws ServiceException {
        erpShopInfo.setUpdateBy(UserUtils.getUser());// 权限校验用当前登录用户
        model.addAttribute("page", erpShopInfoService.findPage(new Page<>(request, response), erpShopInfo));
        return "modules/shop/erpShopInfoList";
    }

    /**
     * 获取当前登录用户有权限访问的所有运营顾问
     * 
     * @return
     */
    @RequestMapping("getOpsAdvisers")
    public @ResponseBody List<User> getOpsAdvisers() {
        return userService.getOpsAdviserByRightOfLoginUser();
    }

    @RequiresPermissions("shop:erpShopInfo:view")
    @RequestMapping(value = "form")
    public String form(ErpShopInfo erpShopInfo, Model model) {
        List<Dict> extensionList = dictService.findListByType("extension_passageway_qulify");
        List<Dict> payList = dictService.findListByType("pay_passageway_qulify");
        if (CollectionUtils.isNotEmpty(payList)) {
            List<String> payQualifyList = erpShopPayQualifyService.findPayQualifyList(erpShopInfo.getId());
            if (CollectionUtils.isNotEmpty(payQualifyList)) {
                for (Dict dict : payList) {
                    if (payQualifyList.contains(dict.getValue())) {
                        dict.setHasPermission(true);
                    }
                }
            }
        }

        if (CollectionUtils.isNotEmpty(extensionList)) {
            List<String> extensionQualifyList = erpShopExtensionQualifyService.findExtensionQualifyList(erpShopInfo.getId());
            if (CollectionUtils.isNotEmpty(extensionQualifyList)) {
                for (Dict dict : extensionList) {
                    if (extensionQualifyList.contains(dict.getValue())) {
                        dict.setHasPermission(true);
                    }
                }
            }
        }
        model.addAttribute("erpShopInfo", erpShopInfo).addAttribute("extensionList", extensionList).addAttribute("payList", payList);
        return "modules/shop/erpShopInfoForm";
    }

    @RequiresPermissions("shop:erpShopInfo:edit")
    @RequestMapping(value = "save")
    public String save(ErpShopInfo erpShopInfo, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, erpShopInfo)){
            return form(erpShopInfo, model);
        }
        erpShopInfoService.save(erpShopInfo);
        addMessage(redirectAttributes, "保存商户成功");
        return "redirect:"+Global.getAdminPath()+"/shop/erpShopInfo/?repage";
    }
    
    @RequiresPermissions("shop:erpShopInfo:edit")
    @RequestMapping(value = "delete")
    public String delete(ErpShopInfo erpShopInfo, RedirectAttributes redirectAttributes) {
        erpShopInfoService.delete(erpShopInfo);
        addMessage(redirectAttributes, "删除商户成功");
        return "redirect:"+Global.getAdminPath()+"/shop/erpShopInfo/?repage";
    }

    @RequiresPermissions("shop:list:button:batchSyncShop")
    @RequestMapping(value = "syncAll")
    @ResponseBody
    public JSONObject syncAll() {
        boolean result = erpShopInfoApiService.syncAll();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        return jsonObject;
    }

    /**
     * 商户选择查询页面
     *
     * @param request
     * @param response
     * @param keyWord
     * @param model
     * @return
     * @date 2017年11月27日
     * @author SunQ
     */
    @RequestMapping(value = "shopSearchList")
    public String shopSearchList(HttpServletRequest request, HttpServletResponse response, String keyWord, Model model) {
        ErpShopInfo erpShopInfo = new ErpShopInfo();
        erpShopInfo.setDelFlag("0");
        if (StringUtils.isNotBlank(keyWord)) {
            erpShopInfo.setName(keyWord);
            erpShopInfo.setNumber(keyWord);
        }

        Page<ErpShopInfo> page = erpShopInfoService.searchList(new Page<ErpShopInfo>(request, response), erpShopInfo);
        model.addAttribute("page", page);
        model.addAttribute("shopList", page.getList());
        model.addAttribute("keyWord", keyWord);
        return "modules/shop/erpShopSearchList";
    }
    
    /**
     * 业务定义：搜索查询服务商信息
     * 
     * @date 2018年7月3日
     * @author R/Q
     */
    @RequestMapping(value = "agentSearchList")
    public String agentSearchList(HttpServletRequest request, HttpServletResponse response, ErpShopInfo paramObj, Model model) {
        Page<Map<String, Object>> page = erpShopInfoService.agentSearchList(paramObj, new Page<Map<String, Object>>(request, response));
        model.addAttribute("page", page);
        return "modules/shop/agentSearchList";
    }

    /**
     * 业务定义：查询商户的服务商ID对应的服务商信息
     * 
     * @date 2018年7月3日
     * @author R/Q
     * @throws ServiceException
     */
    @ResponseBody
    @RequestMapping(value = "queryAgentByShop")
    public Object agentSearchList(ErpShopInfo paramObj) throws ServiceException {
        List<Map<String, Object>> returnList = erpShopInfoService.agentSearchList(paramObj);
        if (CollectionUtils.isEmpty(returnList) || returnList.size() > 1) {
            throw new ServiceException("商户对应的服务商信息异常");
        }
        return returnList;
    }

    /**
     * 去新增商户页面
     * 
     * @param model
     * @return
     */
    @RequestMapping(value = "toAdd")
    public String toAdd(String orderId, Model model) {
        ErpShopInfo erpShopInfo = new ErpShopInfo();
        ErpOrderOriginalInfo orderInfo = erpOrderOriginalInfoService.get(orderId);
        erpShopInfo.setOrderId(orderId);
        erpShopInfo.setZhangbeiId(orderInfo.getShopId());
        erpShopInfo.setName(orderInfo.getShopName());
        erpShopInfo.setAbbreviation(orderInfo.getShopAbbreviation());
        model.addAttribute("info", erpShopInfo);
        return "modules/shop/erpShopInfoAdd";
    }
    
    /**
     * 保存商户信息
     * 
     * @return
     */
    @RequestMapping(value = "add")
    public String add(ErpShopInfo erpShopInfo, Model model, RedirectAttributes redirectAttributes){
        
        // 掌贝账号已存在
        if(erpShopInfoService.countShopByZhangbeiId(erpShopInfo.getZhangbeiId()) > 0){
            addMessage(redirectAttributes, "掌贝账号已存在,请重新输入!");
            return "redirect:" + Global.getAdminPath() + "/shop/toAdd";
        }
        
        String zhangbeiID = erpShopInfo.getZhangbeiId();
        // 使用掌贝账号的后6位作为密码,并进行MD5加密
        if(zhangbeiID.length() < NUMBER_SIZE_6){
            // 不足6位的情况，在末尾补'0'
            zhangbeiID = StringUtils.rightPad(zhangbeiID, NUMBER_SIZE_6, '0');
        }
        String passWord = MD5Util.md5(zhangbeiID.substring(zhangbeiID.length()-NUMBER_SIZE_6, zhangbeiID.length()));
        // 商户来源为ERP添加
        erpShopInfo.setSource("1");
        erpShopInfo.setPassword(passWord);
        
        erpShopInfoService.save(erpShopInfo);
        addMessage(redirectAttributes, "商户保存成功");
        return "redirect:" + Global.getAdminPath() + "/workflow/tasklist";
    }
    
    /**
     * 去修改商户页面(我的任务入口)
     * 
     * @param model
     * @return
     */
    @RequestMapping(value = "toEdit")
    public String toEdit(String orderId, Model model) {
        ErpShopInfo erpShopInfo = erpShopInfoService.getByOrderID(orderId);
        model.addAttribute("info", erpShopInfo);
        return "modules/shop/erpShopInfoEdit";
    }
    
    /**
     * 去修改商户页面(商户列表)
     * 
     * @param model
     * @return
     */
    @RequestMapping(value = "toEdit2")
    public String toEdit2(String id, Model model) {
        ErpShopInfo erpShopInfo = erpShopInfoService.get(id);
        model.addAttribute("info", erpShopInfo);
        return "modules/shop/erpShopInfoEdit";
    }
    
    /**
     * 保存商户信息
     * 
     * @return
     */
    @RequestMapping(value = "update")
    public String update(ErpShopInfo erpShopInfo, String type, Model model, RedirectAttributes redirectAttributes){
        
        // 获取之前保存的商户信息
        ErpShopInfo dbErpShopInfo = erpShopInfoService.get(erpShopInfo.getId());
        
        // 掌贝账号已存在
        if (!dbErpShopInfo.getZhangbeiId().equals(erpShopInfo.getZhangbeiId()) && erpShopInfoService.countShopByZhangbeiId(erpShopInfo.getZhangbeiId()) > 0) {
            addMessage(redirectAttributes, "掌贝账号已存在,请重新输入!");
            return "redirect:" + Global.getAdminPath() + "/shop/toEdit?orderId=" + erpShopInfo.getOrderId();
        }
        
        String zhangbeiID = erpShopInfo.getZhangbeiId();
        // 使用掌贝账号的后6位作为密码,并进行MD5加密
        if(zhangbeiID.length() < NUMBER_SIZE_6){
            // 不足6位的情况，在末尾补'0'
            zhangbeiID = StringUtils.rightPad(zhangbeiID, NUMBER_SIZE_6, '0');
        }
        String passWord = MD5Util.md5(zhangbeiID.substring(zhangbeiID.length()-NUMBER_SIZE_6, zhangbeiID.length()));
        dbErpShopInfo.setPassword(passWord);
        dbErpShopInfo.setZhangbeiId(erpShopInfo.getZhangbeiId());
        dbErpShopInfo.setContactName(erpShopInfo.getContactName());
        dbErpShopInfo.setContactPhone(erpShopInfo.getContactPhone());
        dbErpShopInfo.setContactEmail(erpShopInfo.getContactEmail());
        dbErpShopInfo.setAddress(erpShopInfo.getAddress());
        dbErpShopInfo.setName(erpShopInfo.getName());
        dbErpShopInfo.setAbbreviation(erpShopInfo.getAbbreviation());
        erpShopInfoService.save(dbErpShopInfo);
        addMessage(redirectAttributes, "商户保存成功");
        return "redirect:" + Global.getAdminPath() + "/workflow/tasklist";
    }
    
    /** 同步商户门店 */
    /** 已废弃，前台调用已注释 */
    @RequiresPermissions("shop:erpShopInfo:edit")
    @RequestMapping(value = "syncShopStores")
    public @ResponseBody Boolean syncShopStores() {
        return erpShopInfoApiService.syncShopStores();
    }

    /**
     * 同步进件信息(批量同步)
     *
     * @return
     * @date 2018年4月3日
     * @author SunQ
     */
    @RequiresPermissions("shop:list:button:batchSyncStore")
    @RequestMapping(value = "syncAbnormalAll")
    public @ResponseBody Boolean syncAbnormalAll() {
        return erpShopInfoSyncApiService.syncAbnormalAll();
    }
    
    /**
     * 同步进件信息(单个同步)
     *
     * @param zhangbeiId
     * @return
     * @date 2018年4月3日
     * @author SunQ
     */
    @RequiresPermissions("shop:list:button:singleSyncStore")
    @RequestMapping(value = "syncAbnormal")
    @ResponseBody
    public BaseResult syncAbnormal(String zhangbeiId) {
        return erpShopInfoSyncApiService.syncAbnormal(zhangbeiId);
    }
    
    /**
     * 门店同步的方式
     *
     * @param zhangbeiId
     * @param type (Cover:直接覆盖,Retain:保留两者)
     * @return
     * @date 2018年4月9日
     * @author SunQ
     */
    @RequestMapping(value = "syncType")
    @ResponseBody
    public BaseResult syncType(String zhangbeiId, String type) {
        BaseResult result = new BaseResult();
        boolean flag = false;
        if(ShopConstant.syncType.COVER.equals(type)){
            flag = erpShopInfoSyncApiService.mainStoreUpdate(zhangbeiId);
        }
        if(ShopConstant.syncType.RETAIN.equals(type)){
            flag = erpShopInfoSyncApiService.mainStoreReplace(zhangbeiId);
        }
        if(flag){
            result.setCode(ShopConstant.returnStatus.SUCCESS); 
        }else{
            result.setCode(ShopConstant.returnStatus.FAIL);
        }
        return result;
    }

    /*@RequiresPermissions("shop:list:button:export")*/
    @RequestMapping(value = "export")
    @ResponseBody
    public JSONObject export(String jsonObject, HttpServletResponse response) {
        return erpShopInfoService.export(JSON.parseObject(StringEscapeUtils.unescapeHtml4(jsonObject), ErpShopInfo.class), response);
    }

    /**
     * 功能描述：绑定运营顾问 更新商户表的运营顾问为指定的 运营顾问 更新指定商户下面所有订单关联下面的正在运行的流程对应的运营顾问为指定的运营顾问
     *
     * @param zhangbeiId 掌贝id
     * @param operationAdviserId 运营顾问id
     * @return
     * @date 2018年7月4日
     */
    @RequestMapping(value = "updateOpsAdviserOfShop")
    @ResponseBody
    public BaseResult updateOpsAdviserOfShop(String zhangbeiId, String operationAdviserId) {
        return erpShopInfoService.updateOpsAdviserOfShop(zhangbeiId, operationAdviserId);
    }

    /**
     * 业务定义：搜索查询运营顾问
     * 
     * @date 2018年7月3日
     * @author R/Q
     */
    @RequestMapping(value = "findOperationAdviserList")
    public String findOperationAdviserList(HttpServletRequest request, HttpServletResponse response, Model model) {
        return "modules/shop/findOperationAdviserList";
    }

    /**
     * 根据掌贝Id查询商户下面的运营服务信息 type="pending"查询待处理的，type="process"查询处理中的，“finish”查询处理完成的
     *
     * @param type
     * @param zhangbeiId
     * @return
     * @date 2018年7月6日
     */
    @RequestMapping(value = "getOperationServiceByZhangbeiId")
    @ResponseBody
    public BaseResult getOperationServiceByZhangbeiId(String type, String zhangbeiId) {
        return erpShopInfoService.getOperationServiceByZhangbeiId(type, zhangbeiId);
    }

    /**
     * 根据掌贝id查询商户下面的服务总数量
     *
     * @param serviceType 服务类型，operationService为运营服务，jykService为聚引客服务
     * @param zhangbeiId
     * @return
     * @date 2018年7月6日
     */
    @RequestMapping(value = "getServiceSumByZhangbeiId")
    @ResponseBody
    public BaseResult getServiceSumOfShop(String serviceType, String zhangbeiId) {
        return erpShopInfoService.getServiceSumByZhangbeiId(serviceType, zhangbeiId);
    }

    /**
     * 根据掌贝id查询商户下面所有的聚引客商品信息 type="pending"查询待处理的，type="process"查询处理中的，“finish”查询处理完成的
     *
     * @param type
     * @param zhangbeiId
     * @return
     * @date 2018年7月6日
     */
    @RequestMapping(value = "getJykGoodInfoByZhangbeiId")
    @ResponseBody
    public BaseResult getJykPendingGoodInfoOfShop(String type, String zhangbeiId) {
        return erpShopInfoService.getJykGoodInfoByZhangbeiId(type, zhangbeiId);
    }
}
