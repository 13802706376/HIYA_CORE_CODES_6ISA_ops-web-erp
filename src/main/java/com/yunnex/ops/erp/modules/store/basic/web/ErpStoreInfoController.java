package com.yunnex.ops.erp.modules.store.basic.web;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.shop.entity.BusinessCategory;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.shop.service.BusinessCategoryService;
import com.yunnex.ops.erp.modules.shop.service.ErpShopInfoService;
import com.yunnex.ops.erp.modules.store.advertiser.service.ErpStoreAdvertiserMomoService;
import com.yunnex.ops.erp.modules.store.basic.api.ErpFriendCheckApi;
import com.yunnex.ops.erp.modules.store.basic.api.ErpFriendsApi;
import com.yunnex.ops.erp.modules.store.basic.api.ErpMomoApi;
import com.yunnex.ops.erp.modules.store.basic.api.ErpMomoCheckApi;
import com.yunnex.ops.erp.modules.store.basic.api.ErpPayOpenApi;
import com.yunnex.ops.erp.modules.store.basic.api.ErpPhotoApi;
import com.yunnex.ops.erp.modules.store.basic.api.ErpShopRoleApi;
import com.yunnex.ops.erp.modules.store.basic.api.ErpShopStoreInfoApi;
import com.yunnex.ops.erp.modules.store.basic.api.ErpStoreSynthesizeApi;
import com.yunnex.ops.erp.modules.store.basic.api.ErpUnionPayApi;
import com.yunnex.ops.erp.modules.store.basic.api.ErpWeiboApi;
import com.yunnex.ops.erp.modules.store.basic.api.ErpWeiboCheckApi;
import com.yunnex.ops.erp.modules.store.basic.api.ErpWxPayApi;
import com.yunnex.ops.erp.modules.store.basic.api.ErpZhangbeiApi;
import com.yunnex.ops.erp.modules.store.basic.entity.BusinessScope;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreLinkman;
import com.yunnex.ops.erp.modules.store.basic.service.AuditService;
import com.yunnex.ops.erp.modules.store.basic.service.BusinessScopeService;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreInfoService;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreLinkmanService;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreService;
import com.yunnex.ops.erp.modules.store.constant.StoreConstant;
import com.yunnex.ops.erp.modules.store.pay.entity.ErpStoreBank;
import com.yunnex.ops.erp.modules.store.pay.service.ErpStoreBankService;
import com.yunnex.ops.erp.modules.workflow.flow.service.JykFlowAccountSignalService;

/**
 * 门店基本信息Controller
 * 
 * @author yunnex
 * @version 2017-12-09
 */
@Controller
@RequestMapping(value = "${adminPath}/store/basic/erpStoreInfo")
public class ErpStoreInfoController extends BaseController {
    @Autowired
    private ErpStoreInfoService erpStoreInfoService;
    @Autowired
    private ErpShopInfoService shopService;
    @Autowired
    private ErpStoreLinkmanService storelinkmanService;
    @Autowired
    private ErpStoreBankService bankService;
    @Autowired
    private ErpStoreAdvertiserMomoService momoService;
    @Autowired
    private BusinessScopeService scopeService;
    @Autowired
    private BusinessCategoryService categoryService;
    @Autowired
    private JykFlowAccountSignalService jykService;
    @Autowired
    private AuditService auditService;
    @Autowired
    private ErpStoreService erpStoreService;


    @ModelAttribute
    public ErpStoreInfo get(@RequestParam(required = false) String id) {
        ErpStoreInfo entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = erpStoreInfoService.get(id);
        }
        if (entity == null) {
            entity = new ErpStoreInfo();
        }
        return entity;
    }

    @RequiresPermissions("store:basic:erpStoreInfo:view")
    @RequestMapping(value = {"list", ""})
    public String list(ErpStoreInfo erpStoreInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<ErpStoreInfo> page = erpStoreInfoService.findPage(new Page<ErpStoreInfo>(request, response), erpStoreInfo);
        model.addAttribute("page", page);
        return "modules/store/basic/erpStoreInfoList";
    }

    @RequiresPermissions("store:basic:erpStoreInfo:view")
    @RequestMapping(value = "form")
    public String form(ErpStoreInfo erpStoreInfo, Model model) {
        model.addAttribute("erpStoreInfo", erpStoreInfo);
        return "modules/store/basic/erpStoreInfoForm";
    }

    @RequiresPermissions("store:basic:erpStoreInfo:edit")
    @RequestMapping(value = "save")
    public String save(ErpStoreInfo erpStoreInfo, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, erpStoreInfo)) {
            return form(erpStoreInfo, model);
        }
        erpStoreInfoService.save(erpStoreInfo);
        addMessage(redirectAttributes, "保存门店基本信息成功");
        return "redirect:" + Global.getAdminPath() + "/store/basic/erpStoreInfo/?repage";
    }

    @RequiresPermissions("store:basic:erpStoreInfo:edit")
    @RequestMapping(value = "delete")
    public String delete(ErpStoreInfo erpStoreInfo, RedirectAttributes redirectAttributes) {
        erpStoreInfoService.delete(erpStoreInfo);
        addMessage(redirectAttributes, "删除门店基本信息成功");
        return "redirect:" + Global.getAdminPath() + "/store/basic/erpStoreInfo/?repage";
    }

    /**
     * 业务定义：门店资料管理跳转
     * 
     * @date 2018年5月31日
     */
    @RequestMapping(value = "urlErpShopInfoList")
    @RequiresPermissions("shop:list:button:storeDataManage")
    public String urlErpShopInfoList(String id, HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute("id", id);
        return "modules/shop/shopstore/shopStoreInfo";
    }

    /**
     * 商户有权限的访问人list
     * 
     * @throws ParseException
     * 
     */
    @RequestMapping(value = "shoprolelist")
    @ResponseBody
    public List<ErpShopRoleApi> shopRoleList(String shopid) {
        return erpStoreService.shopRoleList(shopid);
    }



    @RequestMapping(value = "getlist")
    @ResponseBody
    public ErpShopStoreInfoApi getList(String id) {
        return erpStoreService.getList(id);
    }


    @RequestMapping(value = "urlStoreSynthesize")
    public String urlStoreSynthesize(String storeid, HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute("id", storeid);
        return "modules/shop/shopstore/storeSynthesize";
    }

    /**
     * 门店综合信息
     * 
     */
    @RequestMapping(value = "getSynthesizeList")
    @ResponseBody
    public ErpStoreSynthesizeApi getSynthesizeList(String storeid, String shopid, String ismain) {
        return erpStoreService.getSynthesizeList(storeid, shopid, ismain);
    }

    /**
     * 门店综合信息新增联系人
     * 
     */
    @RequestMapping(value = "addlinkman")
    @ResponseBody
    public JSONObject addLinkman(String shopid, String linkname, String linkphone, String position) {
        return erpStoreService.addLinkman(shopid, linkname, linkphone, position);
    }

    /**
     * 门店综合信息新增联系人
     * 
     */
    @RequestMapping(value = "updatelinkman")
    @ResponseBody
    public JSONObject updateLinkman(String shopid, String linkid, String linkname, String linkphone, String position) {
        return erpStoreService.updateLinkman(shopid, linkid, linkname, linkphone, position);
    }

    /**
     * 门店综合信息删除联系人
     * 
     */
    @RequestMapping(value = "deletelinkman")
    @ResponseBody
    public JSONObject deleteLinkman(String linkid) {
        return erpStoreService.deleteLinkman(linkid);
    }

    /**
     * 门店基本信息跳转
     * 
     */
    @RequestMapping(value = "basicinformation")
    public String basicinformation(String shopid, HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute("id", shopid);
        return "modules/shop/shopstore/storeBasicInformation";
    }

    /**
     * 银联开通跳转
     * 
     */
    @RequestMapping(value = "unionpay")
    public String unionpay(String shopid, HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute("id", shopid);
        return "modules/shop/shopstore/unionpay";
    }

    /**
     * 微信开通跳转
     * 
     */
    @RequestMapping(value = "wxpay")
    public String wxpay(String shopid, HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute("id", shopid);
        return "modules/shop/shopstore/wxpay";
    }

    /**
     * 微信支付其他信息
     * 
     */
    @RequestMapping(value = "wxpayInfo")
    public String wxpayInfo(String shopid, HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute("id", shopid);
        return "modules/shop/shopstore/wxpayInfo";
    }

    /**
     * 广告主朋友圈开通跳转
     * 
     */
    @RequestMapping(value = "adwechat")
    public String adwechat(String shopid, HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute("id", shopid);
        return "modules/shop/shopstore/adwechat";
    }

    /**
     * 广告主weibo开通跳转
     * 
     */
    @RequestMapping(value = "adweibo")
    public String adweibo(String shopid, HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute("id", shopid);
        return "modules/shop/shopstore/adweibo";
    }

    /**
     * 广告主momo开通跳转
     * 
     */
    @RequestMapping(value = "admomo")
    public String admomo(String shopid, HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute("id", shopid);
        return "modules/shop/shopstore/admomo";
    }

    /**
     * 推广图片素材开通跳转
     * 
     */
    @RequestMapping(value = "promotion")
    public String promotion(String shopid, HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute("id", shopid);
        return "modules/shop/shopstore/promotion";
    }

    /**
     * 朋友圈审核跳转
     * 
     */
    @RequestMapping(value = "friendCheckUrl")
    public String friendCheckUrl(String id, HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute("id", id);
        return "modules/shop/shopstore/friendCheck";
    }

    /**
     * momo审核跳转
     * 
     */
    @RequestMapping(value = "momoCheckUrl")
    public String momoCheckUrl(String id, HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute("id", id);
        return "modules/shop/shopstore/momoCheck";
    }

    /**
     * weibo审核跳转
     * 
     */
    @RequestMapping(value = "weiboCheckUrl")
    public String weiboCheckUrl(String id, HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute("id", id);
        return "modules/shop/shopstore/weiboCheck";
    }

    /**
     * 门店基本信息list
     * 
     */
    @RequestMapping(value = "basicinformationlist")
    @ResponseBody
    public ErpStoreInfo basicInformationList(String shopid, String storeid) {
        return erpStoreService.basicInformationList(shopid, storeid);
    }

    /**
     * 门店基本信息保存
     * 
     * @throws ParseException
     * 
     */
    @RequestMapping(value = "addbasicinformation")
    @ResponseBody
    @RequiresPermissions(value = {StoreConstant.PERMISSION_STORE_EDIT, StoreConstant.PERMISSION_STORE_ADVANCED_EDIT}, logical = Logical.OR)
    public JSONObject saveBasicStoreInfo(String jsonObject) {
        return erpStoreService.saveBasicStoreInfo(jsonObject);
    }

    /**
     * 微信支付开通list
     * 
     * @throws ParseException
     * 
     */
    @RequestMapping(value = "wxpaylist")
    @ResponseBody
    public ErpWxPayApi wxpaylist(String shopid, String storeid) {
        return erpStoreService.wxpayList(shopid, storeid);
    }

    /**
     * 微信支付保存
     */
    @RequestMapping(value = "addwxpay")
    @ResponseBody
    @RequiresPermissions(value = {StoreConstant.PERMISSION_STORE_EDIT, StoreConstant.PERMISSION_STORE_ADVANCED_EDIT}, logical = Logical.OR)
    public JSONObject addWxpay(String jsonObject) {
        return erpStoreService.addWxpay(jsonObject);
    }


    /**
     * 新增门店基本信息list
     * 
     */
    @RequestMapping(value = "newStorelist")
    @ResponseBody
    public ErpStoreInfo newStoreList(String shopid, String storename) {
        return erpStoreService.newStoreList(shopid, storename);
    }

    /**
     * 新增门店基本信息保存
     *
     */
    @RequestMapping(value = "addNewStore")
    @ResponseBody
    public JSONObject addNewStore(String jsonObject) {
        return erpStoreService.addNewStore(jsonObject);
    }

    /**
     * 银联支付开通list
     */
    @RequestMapping(value = "unionpaylist")
    @ResponseBody
    public ErpUnionPayApi unionpayList(String shopid, String storeid) {
        return erpStoreService.unionpayList(shopid, storeid);
    }


    /**
     * 银联支付保存
     */
    @RequestMapping(value = "addunionpaypay")
    @ResponseBody
    @RequiresPermissions(value = {StoreConstant.PERMISSION_STORE_EDIT, StoreConstant.PERMISSION_STORE_ADVANCED_EDIT}, logical = Logical.OR)
    public JSONObject addUnionpayPay(String jsonObject) {
        return erpStoreService.addUnionpayPay(jsonObject);
    }


    /**
     * 朋友圈推广开通list
     */
    @RequestMapping(value = "friendslist")
    @ResponseBody
    public ErpFriendsApi friendslist(String shopid, String storeid) {
        return erpStoreService.friendsList(shopid, storeid);
    }

    /**
     * 朋友圈推广保存
     */
    @RequestMapping(value = "addfriends")
    @ResponseBody
    @RequiresPermissions(value = {StoreConstant.PERMISSION_STORE_EDIT, StoreConstant.PERMISSION_STORE_ADVANCED_EDIT}, logical = Logical.OR)
    public JSONObject addFriends(String jsonObject) {
        return erpStoreService.addFriends(jsonObject);
    }

    /**
     * 微博推广开通list
     */
    @RequestMapping(value = "weibolist")
    @ResponseBody
    public ErpWeiboApi weiboList(String shopid, String storeid) {
        return erpStoreService.weiboList(shopid, storeid);
    }

    /**
     * 微博推广保存
     */
    @RequestMapping(value = "addweibo")
    @ResponseBody
    @RequiresPermissions(value = {StoreConstant.PERMISSION_STORE_EDIT, StoreConstant.PERMISSION_STORE_ADVANCED_EDIT}, logical = Logical.OR)
    public JSONObject addWeibo(String jsonObject) {
        return erpStoreService.addWeibo(jsonObject);
    }

    /**
     * 陌陌推广开通list
     */
    @RequestMapping(value = "momolist")
    @ResponseBody
    public ErpMomoApi momoList(String shopid, String storeid) {
        return erpStoreService.momoList(shopid, storeid);
    }

    /**
     * 陌陌推广保存
     */
    @RequestMapping(value = "addmomo")
    @ResponseBody
    @RequiresPermissions(value = {StoreConstant.PERMISSION_STORE_EDIT, StoreConstant.PERMISSION_STORE_ADVANCED_EDIT}, logical = Logical.OR)
    public JSONObject addMomo(String jsonObject) {
        return erpStoreService.addMomo(jsonObject);
    }

    /**
     * 推广图片素材list
     */
    @RequestMapping(value = "photolist")
    @ResponseBody
    public ErpPhotoApi photoList(String shopid, String storeid) {
        return erpStoreService.photoList(shopid, storeid);
    }

    /**
     * 推广图片素材保存
     */
    @RequestMapping(value = "addphoto")
    @ResponseBody
    @RequiresPermissions(value = {StoreConstant.PERMISSION_STORE_EDIT, StoreConstant.PERMISSION_STORE_ADVANCED_EDIT}, logical = Logical.OR)
    public JSONObject addPhoto(String jsonObject) {
        return erpStoreService.addPhoto(jsonObject);
    }

    /**
     * 服务管理
     * 
     */
    @RequestMapping(value = "serviceManage")
    /*@RequiresPermissions("shop:list:button:serviceManage")*/
    public String serviceManage(String id, String zhangbeiId, String operationAdviserName, HttpServletRequest request, HttpServletResponse response,
                    Model model) {
        model.addAttribute("id", id);
        model.addAttribute("zhangbeiId", zhangbeiId);
        model.addAttribute("operationAdviserName",
                        Optional.ofNullable(operationAdviserName).orElse(shopService.getOpsAdviserNameByShopInfoId(id)));

        return "modules/shop/shopstore/serviceManage";
    }

    /**
     * 掌贝进件跳转
     * 
     */
    @RequestMapping(value = "zhangbei")
    @RequiresPermissions("shop:list:button:auditManage")
    public String zhangbei(String shopid, HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute("id", shopid);
        return "modules/shop/shopstore/zhangbei";
    }


    /**
     * 掌贝进件list
     * 
     * @throws ParseException
     * 
     */
    @RequestMapping(value = "zhangbeilist")
    @ResponseBody
    public ErpZhangbeiApi zhangbeilist(String shopid, HttpServletRequest request, HttpServletResponse response, Model model) {
        ErpShopInfo shop = shopService.get(shopid);
        List<ErpStoreInfo> store = erpStoreInfoService.findwhereshopidList(shopid, Global.NO);
        ErpZhangbeiApi zhangbeiApi = new ErpZhangbeiApi();
        zhangbeiApi.setZhangbeiId(shop.getZhangbeiId());
        zhangbeiApi.setShopName(shop.getName());
        zhangbeiApi.setShopId(shopid);
        zhangbeiApi.getStorelist().addAll(store);
        return zhangbeiApi;
    }

    /**
     * 掌贝进件设置主店
     */
    @RequestMapping(value = "mainstore")
    @ResponseBody
    public JSONObject setMainStore(String shopid, String storeid) {
        return erpStoreService.setMainStore(shopid, storeid);
    }


    /**
     * 掌贝进件提交审核
     * 
     * @throws JsonProcessingException
     * @throws ParseException
     * 
     */
    @RequestMapping(value = "zhangbeiaudit")
    @ResponseBody
    @RequiresPermissions(StoreConstant.PERMISSION_COMMIT_AUDIT)
    public JSONObject zhangbeiaudit(String shopId) {
        return auditService.zhangbeiaudit(shopId);
    }

    /**
     * 支付进件跳转
     * 
     */
    @RequestMapping(value = "payOpen")
    public String payOpen(String shopid, HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute("id", shopid);
        return "modules/shop/shopstore/payOpen";
    }

    /**
     * 微信进件list
     * 
     * @throws ParseException
     * 
     */
    @RequestMapping(value = "wxpayOpen")
    @ResponseBody
    public ErpPayOpenApi wxpayOpen(String shopid, HttpServletRequest request, HttpServletResponse response, Model model) {

        return erpStoreInfoService.wxpayOpen(shopid);
    }


    /**
     * 微信支付进件提交审核
     * 
     * @throws JsonProcessingException
     * @throws ParseException
     * 
     */
    @RequestMapping(value = "wxpayaudit")
    @ResponseBody
    @RequiresPermissions(StoreConstant.PERMISSION_COMMIT_AUDIT)
    public JSONObject wxpayaudit(String shopid, String storeids, String intopiecesName, HttpServletRequest request, HttpServletResponse response,
                    Model model) {
        return auditService.wxpayaudit(shopid, storeids, intopiecesName);
    }

    /**
     * 银联进件list
     * 
     * @throws ParseException
     * 
     */
    @RequestMapping(value = "unionOpen")
    @ResponseBody
    public ErpPayOpenApi unionOpen(String shopid, HttpServletRequest request, HttpServletResponse response, Model model) {

        return erpStoreInfoService.unionOpen(shopid);
    }


    /**
     * 银联支付进件提交审核
     * 
     * @throws JsonProcessingException
     * @throws ParseException
     * 
     */
    @RequestMapping(value = "unionaudit")
    @ResponseBody
    @RequiresPermissions(StoreConstant.PERMISSION_COMMIT_AUDIT)
    public JSONObject unionaudit(String shopid, String storeids, String intopiecesName, HttpServletRequest request, HttpServletResponse response,
                    Model model) {
        return auditService.unionaudit(shopid, storeids, intopiecesName);
    }


    /**
     * 根据shopid获取主店storeid
     *
     * 
     */
    @RequestMapping(value = "findstoreIdbyshopId")
    @ResponseBody
    public JSONObject findstoreIdbyshopId(String shopid, HttpServletRequest request, HttpServletResponse response, Model model) {
        JSONObject resObject = new JSONObject();
        ErpStoreInfo store = erpStoreInfoService.findismain(shopid, Global.NO);
        if (null == store) {
            resObject.put("storeid", false);
        } else {
            resObject.put("storeid", store.getId());
        }
        return resObject;
    }


    /**
     * 朋友圈审核list
     * 
     * @throws ParseException
     * 
     */
    @RequestMapping(value = "friendCheckList")
    @ResponseBody
    public List<ErpFriendCheckApi> friendCheckList(String shopId, HttpServletRequest request, HttpServletResponse response, Model model) {
        List<ErpFriendCheckApi> apilist = erpStoreInfoService.findfriendaudit(shopId);
        return apilist;
    }

    /**
     * 朋友圈提交审核
     * 
     * @throws JsonProcessingException
     * @throws ParseException
     * 
     */
    @RequestMapping(value = "friendCheck")
    @ResponseBody
    @RequiresPermissions(StoreConstant.PERMISSION_COMMIT_AUDIT)
    public JSONObject friendCheck(String storeId) {
        return auditService.friendSubmitAudit(storeId);
    }


    /**
     * 陌陌审核list
     */
    @RequestMapping(value = "momoCheckList")
    @ResponseBody
    public List<ErpMomoCheckApi> momoCheckList(String shopId) {
        return erpStoreService.momoCheckList(shopId);
    }

    /**
     * 陌陌提交审核
     * 
     * @throws JsonProcessingException
     * @throws ParseException
     * 
     */
    @RequestMapping(value = "momoCheck")
    @ResponseBody
    @RequiresPermissions(StoreConstant.PERMISSION_COMMIT_AUDIT)
    public JSONObject momoCheck(String storeids) {
        return auditService.momoCheck(storeids);
    }



    /**
     * 微博审核list
     * 
     * @throws ParseException
     * 
     */
    @RequestMapping(value = "weiboCheckList")
    @ResponseBody
    public List<ErpWeiboCheckApi> weiboCheckList(String shopId, HttpServletRequest request, HttpServletResponse response, Model model) {
        List<ErpWeiboCheckApi> apilist = erpStoreInfoService.findweiboaudit(shopId);
        return apilist;
    }

    /**
     * 微博提交审核
     * 
     * @throws JsonProcessingException
     * @throws ParseException
     * 
     */
    @RequestMapping(value = "weiboCheck")
    @ResponseBody
    @RequiresPermissions(StoreConstant.PERMISSION_COMMIT_AUDIT)
    public JSONObject weiboCheck(String storeId) {
        return auditService.weiboSubmitAudit(storeId);
    }



    /**
     * 银行信息copy接口
     * 
     * @throws ParseException
     * 
     */
    @RequestMapping(value = "bankCopy")
    @ResponseBody
    public List<ErpStoreBank> bankCopy(String shopId, HttpServletRequest request, HttpServletResponse response, Model model) {
        List<ErpStoreBank> list = bankService.findwhereshopid(shopId);
        if (!CollectionUtils.isEmpty(list)) {
            return list;
        }
        return list;
    }

    /**
     * 通过shopId获取商户的信息
     *
     * @param shopId
     * @return
     * @date 2018年3月29日
     * @author SunQ
     */
    @RequestMapping(value = "getShopInfo")
    @ResponseBody
    public BaseResult getShopInfo(String shopId) {
        BaseResult result = new BaseResult();
        ErpShopInfo shop = shopService.get(shopId);
        if (null != shop) {
            JSONObject obj = new JSONObject();
            // 商户id
            obj.put("shopId", shopId);
            // 商户编号
            obj.put("shopNumber", shop.getNumber());
            // 服务商
            obj.put("serviceProvider", shop.getServiceProvider());
            // 服务商联系方式
            obj.put("serviceProviderPhone", shop.getServiceProviderPhone());
            // 服务商区域
            obj.put("serviceAddress", shop.getAddress());
            // 门店数量
            obj.put("storeNum", shop.getStoreCount());
            // 商户名称
            obj.put("shopName", shop.getName());
            // 商户类型
            obj.put("industryType", shop.getIndustryType());
            // 小程序账号
            obj.put("xcxAccount", shop.getZhangbeiId());
            // 小程序密码
            if (0 == shop.getZhangbeiState()) {
                obj.put("xcxPass", shop.getZhangbeiId().substring(shop.getZhangbeiId().length() - 6));
            } else {
                obj.put("xcxPass", "商户已进件审核，密码可能修改");
            }
            result.setAttach(obj);
        } else {
            result.setCode("0");
            result.setMessage("商户不存在");
        }
        return result;
    }

    /**
     * 获取OEM商户进件对象
     *
     * @param shopid
     * @return
     * @date 2018年4月3日
     * @author SunQ
     */
    @RequestMapping(value = "findZhangbeiIntopieceInfo")
    @ResponseBody
    public BaseResult findZhangbeiIntopieceInfo(String shopid) {
        BaseResult result = new BaseResult();
        ErpShopInfo shop = shopService.get(shopid);
        List<ErpStoreInfo> store = erpStoreInfoService.findwhereshopidListForOEM(shopid, Global.NO, Global.YES);
        if (!CollectionUtils.isEmpty(store)) {
            for (ErpStoreInfo s : store) {
                s.setBusinesscategoryname(shop.getBusinessCategoryName());
            }
        }
        ErpZhangbeiApi zhangbeiApi = new ErpZhangbeiApi();
        zhangbeiApi.setZhangbeiId(shop.getZhangbeiId());
        zhangbeiApi.setShopName(shop.getName());
        zhangbeiApi.setShopId(shopid);
        zhangbeiApi.setIsAbnormal(shop.getIsAbnormal());
        zhangbeiApi.getStorelist().addAll(store);
        result.setAttach(zhangbeiApi);
        return result;
    }

    /**
     * 获取OEM商户微信支付进件对象
     *
     * @param shopid
     * @param request
     * @param response
     * @param model
     * @return
     * @date 2018年4月3日
     * @author SunQ
     */
    @RequestMapping(value = "findWxIntopieceInfo")
    @ResponseBody
    public BaseResult findWxIntopieceInfo(String shopid, HttpServletRequest request, HttpServletResponse response, Model model) {
        BaseResult result = new BaseResult();
        ErpShopInfo shop = shopService.get(shopid);
        List<ErpStoreInfo> store = erpStoreInfoService.findwxpayauditForOEM(shopid, Global.NO);
        ErpPayOpenApi payopenApi = new ErpPayOpenApi();
        payopenApi.setShopid(shop.getId());
        payopenApi.setShopname(shop.getName());
        payopenApi.getStore().addAll(store);
        result.setAttach(payopenApi);
        return result;
    }

    /**
     * 获取OEM商户银联支付进件对象
     *
     * @param shopid
     * @param request
     * @param response
     * @param model
     * @return
     * @date 2018年4月3日
     * @author SunQ
     */
    @RequestMapping(value = "findUnionIntopieceInfo")
    @ResponseBody
    public BaseResult findUnionIntopieceInfo(String shopid, HttpServletRequest request, HttpServletResponse response, Model model) {
        BaseResult result = new BaseResult();
        ErpShopInfo shop = shopService.get(shopid);
        List<ErpStoreInfo> store = erpStoreInfoService.findunionauditForOEM(shopid, Global.NO);
        ErpPayOpenApi payopenApi = new ErpPayOpenApi();
        payopenApi.setShopid(shop.getId());
        payopenApi.setShopname(shop.getName());
        payopenApi.getStore().addAll(store);
        result.setAttach(payopenApi);
        return result;
    }

    /**
     * 业务定义：查询商户主店信息，
     * 
     * @date 2018年5月3日
     * @author R/Q
     */
    @RequestMapping(value = "queryStoreInfo")
    @ResponseBody
    public ErpStoreInfo queryStoreInfo(String shopid, String storeid) {
        ErpStoreInfo store = null;
        ErpShopInfo shop = shopService.get(shopid);
        if (shop != null) {
            List<BusinessScope> scopelist = scopeService.findAllList();// 无奈的前端数据结构
            List<BusinessCategory> category = categoryService.findAllList();
            ErpStoreInfo mainStore = erpStoreInfoService.getIsmainStore(Global.NO, shopid, Global.YES);
            if (mainStore != null && StringUtils.equals(mainStore.getId(), storeid)) {
                mainStore.setBusinesscategory(shop.getBusinessCategory());
                mainStore.setBusinesscategoryname(shop.getBusinessCategoryName());
                mainStore.getScope().addAll(scopelist);
                mainStore.getCategory().addAll(category);
                return mainStore;
            }
            store = erpStoreInfoService.get(storeid);
            if (store != null && mainStore != null) {
                ErpStoreLinkman linkman = storelinkmanService.findWhereStoreId(Global.NO, mainStore.getId());
                if (linkman != null) {
                    linkman.setId(null);
                    linkman.setStoreInfoId(null);
                    mainStore.setStroeLinkMan(linkman);
                }
                mainStore.setId(store.getId());
                mainStore.setShortName(store.getShortName());
                mainStore.setShopId(shopid);
                mainStore.setShopName(shop.getName());
                mainStore.setBusinesscategory(shop.getBusinessCategory());// 无奈的表设计
                mainStore.setBusinesscategoryname(shop.getBusinessCategoryName());
                mainStore.getScope().addAll(scopelist);
                mainStore.getCategory().addAll(category);
                return mainStore;
            } else if (store != null) {
                store.setBusinesscategory(shop.getBusinessCategory());
                store.setBusinesscategoryname(shop.getBusinessCategoryName());
                store.getScope().addAll(scopelist);
                store.getCategory().addAll(category);
                return store;
            }
        }
        return store;
    }

}
