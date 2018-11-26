package com.yunnex.ops.erp.modules.workflow.flow.web.jyk;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.result.ServiceErrorResult;
import com.yunnex.ops.erp.common.result.SystemErrorResult;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.common.utils.AESUtil;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserFriends;
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserMomo;
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserWeibo;
import com.yunnex.ops.erp.modules.store.advertiser.service.ErpStoreAdvertiserFriendsService;
import com.yunnex.ops.erp.modules.store.advertiser.service.ErpStoreAdvertiserMomoService;
import com.yunnex.ops.erp.modules.store.advertiser.service.ErpStoreAdvertiserWeiboService;
import com.yunnex.ops.erp.modules.store.basic.entity.BusinessScope;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreCredentials;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreLinkman;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStorePromotePhotoMaterial;
import com.yunnex.ops.erp.modules.store.basic.service.BusinessScopeService;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreCredentialsService;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreInfoService;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreLinkmanService;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStorePromotePhotoMaterialService;
import com.yunnex.ops.erp.modules.workflow.flow.service.JykFlowStoreInfoService;

/**
 * 门店信息Controller
 * 
 * @author SunQ
 * @date 2018年1月10日
 */
@Controller
@RequestMapping(value = "${adminPath}/jyk/flow/storeInfo")
public class JykFlowStoreInfoController extends BaseController {

    @Autowired
    private ErpStoreInfoService erpStoreInfoService;
    @Autowired
    private ErpStorePromotePhotoMaterialService erpStorePromotePhotoMaterialService;
    @Autowired
    private ErpStoreAdvertiserFriendsService erpStoreAdvertiserFriendsService;
    @Autowired
    private ErpStoreAdvertiserWeiboService erpStoreAdvertiserWeiboService;
    @Autowired
    private ErpStoreAdvertiserMomoService erpStoreAdvertiserMomoService;
    @Autowired
    private ErpStoreCredentialsService erpStoreCredentialsService;
    @Autowired
    private BusinessScopeService businessScopeService;
    @Autowired
    private ErpStoreLinkmanService erpStoreLinkmanService;
    @Autowired
    private JykFlowStoreInfoService jykFlowStoreInfoService;
    
    /**
     * 业管审核朋友圈广告开户表单
     *
     * @param request
     * @param response
     * @return
     * @date 2018年1月10日
     * @author Administrator
     */
    @RequestMapping(value = "friendsform")
    public String friendsform(HttpServletRequest request, HttpServletResponse response, String datas) {
        return "modules/workflow/store/friendsform";
    }

    /**
     * 业管审核微博广告开户表单
     *
     * @param request
     * @param response
     * @return
     * @date 2018年1月10日
     * @author Administrator
     */
    @RequestMapping(value = "weiboform")
    public String weiboform(HttpServletRequest request, HttpServletResponse response, String datas) {
        return "modules/workflow/store/weiboform";
    }

    /**
     * 业管审核陌陌开户表单
     *
     * @param request
     * @param response
     * @return
     * @date 2018年1月10日
     * @author Administrator
     */
    @RequestMapping(value = "momoform")
    public String momoform(HttpServletRequest request, HttpServletResponse response, String datas) {
        return "modules/workflow/store/momoform";
    }

    /**
     * 策划专家补充陌陌开户资料表补充表
     *
     * @param request
     * @param response
     * @return
     * @date 2018年1月10日
     * @author Administrator
     */
    @RequestMapping(value = "momosupform")
    public String momosupform(HttpServletRequest request, HttpServletResponse response, String datas) {
        return "modules/workflow/store/momosupform";
    }

    /**
     * 推广图片素材查阅查看
     *
     * @param request
     * @param response
     * @return
     * @date 2018年1月10日
     * @author Administrator
     */
    @RequestMapping(value = "showstorepopularizepic")
    public String showstorepopularizepic(HttpServletRequest request, HttpServletResponse response, String datas) {
        return "modules/workflow/store/showstorepopularizepic";
    }

    /**
     * 业管—陌陌开户；对应任务：服务商订单聚引客开户流程-陌陌推广开户资料录入
     *
     * @param request
     * @param response
     * @return
     * @date 2018年1月10日
     * @author Administrator
     */
    @RequestMapping(value = "momoinfomationplanning")
    public String momoinfomationplanning(HttpServletRequest request, HttpServletResponse response, String datas) {
        return "modules/workflow/store/momoinfomationplanning";
    }

    /**
     * 商户尽快上传推广图片素材
     *
     * @param request
     * @param response
     * @return
     * @date 2018年1月10日
     * @author Administrator
     */
    @RequestMapping(value = "uploadpromotionalpictures")
    public String uploadpromotionalpictures(HttpServletRequest request, HttpServletResponse response, String datas) {
        return "modules/workflow/store/uploadpromotionalpictures";
    }

    /* ===============================门店相关信息查询 START============================== */
    /**
     * 获取门店推广图片素材信息
     *
     * @param storeId
     * @return
     * @date 2018年1月10日
     * @author SunQ
     */
    @RequestMapping(value = "getStorePromotePhotoMaterial")
    @ResponseBody
    public JSONObject getStorePromotePhotoMaterial(String storeId) {
        JSONObject resObject = new JSONObject();
        ErpStorePromotePhotoMaterial storePromotePhotoMaterial = erpStorePromotePhotoMaterialService.findlistWhereStoreId("0", storeId);
        resObject.put("storePromotePhotoMaterial", storePromotePhotoMaterial);
        resObject.put("success", true);
        return resObject;
    }
    
    /**
     * 直销商户朋友圈推广开户资料业管审核信息
     *
     * @param storeId
     * @return
     * @date 2018年1月10日
     * @author SunQ
     */
    @RequestMapping(value = "getStoreFriendsInfomationZhixiao")
    @ResponseBody
    public JSONObject getStoreFriendsInfomationZhixiao(String storeId) {
        JSONObject resObject = new JSONObject();
        ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
        ErpStoreAdvertiserFriends advertiserFriends = erpStoreAdvertiserFriendsService.getByStoreId(storeId);
        ErpStoreCredentials credentials = erpStoreCredentialsService.get(storeInfo.getCredentialsId());
        resObject.put("advertiserFriends", advertiserFriends);
        resObject.put("credentials", credentials);
        resObject.put("success", true);
        return resObject;
    }
    
    /**
     * 直销商户微博推广开户资料业管审核
     *
     * @param storeId
     * @return
     * @date 2018年1月10日
     * @author SunQ
     */
    @RequestMapping(value = "getStoreWeiboInfomationZhixiao")
    @ResponseBody
    public JSONObject getStoreWeiboInfomationZhixiao(String storeId) {
        JSONObject resObject = new JSONObject();
        ErpStoreAdvertiserWeibo advertiserWeibo = erpStoreAdvertiserWeiboService.getByStoreId(storeId);
        if(null!=advertiserWeibo &&  StringUtils.isNotBlank(advertiserWeibo.getAccountPassword())){
            advertiserWeibo.setAccountPassword(AESUtil.decrypt(advertiserWeibo.getAccountPassword()));
        }
        ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
        ErpStoreCredentials credentials = erpStoreCredentialsService.get(storeInfo.getCredentialsId());
        ErpStoreLinkman linkMan = erpStoreLinkmanService.findWhereStoreId("0", storeId);
        List<BusinessScope> scopelist = businessScopeService.findAllList();
        resObject.put("storeInfo", storeInfo);
        resObject.put("credentials", credentials);
        resObject.put("linkMan", linkMan);
        resObject.put("scopelist", scopelist);
        resObject.put("advertiserWeibo", advertiserWeibo);
        resObject.put("success", true);
        return resObject;
    }
    
    /**
     * 直销商户陌陌推广开户资料业管审核信息
     *
     * @param storeId
     * @return
     * @date 2018年1月10日
     * @author SunQ
     */
    @RequestMapping(value = "getStoreMomoInfomationZhixiao")
    @ResponseBody
    public JSONObject getStoreMomoInfomationZhixiao(String storeId) {
        JSONObject resObject = new JSONObject();
        ErpStoreAdvertiserMomo advertiserMomo = erpStoreAdvertiserMomoService.getByStoreId(storeId);
        ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
        ErpStoreCredentials credentials = erpStoreCredentialsService.get(storeInfo.getCredentialsId());
        ErpStoreLinkman linkMan = erpStoreLinkmanService.findWhereStoreId("0", storeId);
        resObject.put("storeInfo", storeInfo);
        resObject.put("credentials", credentials);
        resObject.put("linkMan", linkMan);
        if (null != advertiserMomo) {
            resObject.put("advertiserMomo", advertiserMomo);
        }
        resObject.put("success", true);
        return resObject;
    }
    
    /**
     * 策划专家陌陌推广开户资料录入信息
     *
     * @param storeId
     * @return
     * @date 2018年1月10日
     * @author SunQ
     */
    @RequestMapping(value = "getStoreMomoInfomationPlanning")
    @ResponseBody
    public JSONObject getStoreMomoInfomationPlanning(String storeId) {
        JSONObject resObject = new JSONObject();
        
        resObject.put("success", true);
        return resObject;
    }
    
    /**
     * 服务商商户朋友圈推广开户资料业管录入信息
     *
     * @param storeId
     * @return
     * @date 2018年1月10日
     * @author SunQ
     */
    @RequestMapping(value = "getStoreFriendsInfomationService")
    @ResponseBody
    public JSONObject getStoreFriendsInfomationService(String storeId) {
        JSONObject resObject = new JSONObject();
        ErpStoreAdvertiserFriends advertiserFriends = erpStoreAdvertiserFriendsService.getByStoreId(storeId);
        resObject.put("advertiserFriends", advertiserFriends);
        resObject.put("success", true);
        return resObject;
    }
    
    /**
     * 服务商商户微博推广开户资料业管录入信息
     *
     * @param storeId
     * @return
     * @date 2018年1月10日
     * @author SunQ
     */
    @RequestMapping(value = "getStoreWeiboInfomationService")
    @ResponseBody
    public JSONObject getStoreWeiboInfomationService(String storeId) {
        JSONObject resObject = new JSONObject();
        ErpStoreAdvertiserWeibo advertiserWeibo = erpStoreAdvertiserWeiboService.getByStoreId(storeId);
        ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
        ErpStoreCredentials credentials = erpStoreCredentialsService.get(storeInfo.getCredentialsId());
        ErpStoreLinkman linkMan = erpStoreLinkmanService.findWhereStoreId("0", storeId);
        List<BusinessScope> scopelist = businessScopeService.findAllList();
        resObject.put("storeInfo", storeInfo);
        resObject.put("credentials", credentials);
        resObject.put("linkMan", linkMan);
        resObject.put("scopelist", scopelist);
        resObject.put("advertiserWeibo", advertiserWeibo);
        resObject.put("success", true);
        return resObject;
    }
    
    /**
     * 服务商商户陌陌推广开户资料业管录入信息
     *
     * @param storeId
     * @return
     * @date 2018年1月10日
     * @author SunQ
     */
    @RequestMapping(value = "getStoreMomoInfomationService")
    @ResponseBody
    public JSONObject getStoreMomoInfomationService(String storeId) {
        JSONObject resObject = new JSONObject();
        ErpStoreAdvertiserMomo advertiserMomo = erpStoreAdvertiserMomoService.getByStoreId(storeId);
        ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
        ErpStoreCredentials credentials = erpStoreCredentialsService.get(storeInfo.getCredentialsId());
        ErpStoreLinkman linkMan = erpStoreLinkmanService.findWhereStoreId("0", storeId);
        resObject.put("storeInfo", storeInfo);
        resObject.put("credentials", credentials);
        resObject.put("linkMan", linkMan);
        if (null != advertiserMomo) {
            resObject.put("advertiserMomo", advertiserMomo);
        }
        resObject.put("success", true);
        return resObject;
    }
    /* ===============================门店相关信息查询 END============================== */
    
    /* ===============================门店相关信息修改 START============================== */
    /**
     * 修改直销商户朋友圈推广开户资料业管审核信息
     *
     * @param storeId
     * @return
     * @date 2018年1月10日
     * @author SunQ
     */
    @RequestMapping(value = "updateStoreFriendsInfomationZhixiao")
    @ResponseBody
    public JSONObject updateStoreFriendsInfomationZhixiao(HttpServletRequest request, HttpServletResponse response) {
        JSONObject resObject = new JSONObject();
        String friendsInfomationStr = request.getParameter("friendsInfomation");
        JSONObject jsonfriendsInfomation = JSONObject.parseObject(friendsInfomationStr);
        String storeid = jsonfriendsInfomation.getString("storeid");
        ErpStoreInfo storeInfo = erpStoreInfoService.get(storeid);
        String accountOriginalId = jsonfriendsInfomation.getString("accountOriginalId");
        String advertiserScreenshot = jsonfriendsInfomation.getString("advertiserScreenshot");
        String storeScreenshot = jsonfriendsInfomation.getString("storeScreenshot");
        JSONObject credentialsObj = jsonfriendsInfomation.getJSONObject("credentials");
        String specialCertificate = credentialsObj.getString("specialCertificate");
        
        // 获取朋友圈开户资料信息
        ErpStoreAdvertiserFriends advertiserFriends = erpStoreAdvertiserFriendsService.getByStoreId(storeid);
        if(null==advertiserFriends || StringUtils.isBlank(advertiserFriends.getId())){
            advertiserFriends = new ErpStoreAdvertiserFriends();
        }
        advertiserFriends.setAccountOriginalId(accountOriginalId);
        advertiserFriends.setAdvertiserScreenshot(advertiserScreenshot);
        advertiserFriends.setStoreScreenshot(storeScreenshot);
        
        // 获取商户行业资质信息
        ErpStoreCredentials credentials = erpStoreCredentialsService.get(storeInfo.getCredentialsId());
        if(null==credentials || StringUtils.isBlank(credentials.getId())){
            credentials = new ErpStoreCredentials();
        }
        credentials.setSpecialCertificate(specialCertificate);
        
        // 保存修改
        erpStoreAdvertiserFriendsService.save(advertiserFriends);
        erpStoreCredentialsService.save(credentials);
        storeInfo.setAdvertiserFriendsId(advertiserFriends.getId());
        storeInfo.setCredentialsId(credentials.getId());
        erpStoreInfoService.save(storeInfo);
        resObject.put("success", true);
        return resObject;
    }
    
    /**
     * 修改直销商户微博推广开户资料业管审核
     *
     * @param storeId
     * @return
     * @date 2018年1月10日
     * @author SunQ
     */
    @RequestMapping(value = "updateStoreWeiboInfomationZhixiao")
    @ResponseBody
    public JSONObject updateStoreWeiboInfomationZhixiao(HttpServletRequest request, HttpServletResponse response) {
        JSONObject resObject = new JSONObject();
        String weiboInfomationStr = request.getParameter("weiboInfomation");
        JSONObject jsonWeiboInfomation = JSONObject.parseObject(weiboInfomationStr);
        String storeid = jsonWeiboInfomation.getString("storeid");
        ErpStoreInfo storeInfo = erpStoreInfoService.get(storeid);
        ErpStoreAdvertiserWeibo advertiserWeibo = erpStoreAdvertiserWeiboService.getByStoreId(storeid);
        ErpStoreCredentials credentials = erpStoreCredentialsService.get(storeInfo.getCredentialsId());
        ErpStoreLinkman linkMan = erpStoreLinkmanService.findWhereStoreId("0", storeid);
        
        // 获取微博修改的内容
        JSONObject jsonAdvertiserWeibo = jsonWeiboInfomation.getJSONObject("advertiserWeibo");
        if(null==advertiserWeibo || StringUtils.isBlank(advertiserWeibo.getId())){
            advertiserWeibo = new ErpStoreAdvertiserWeibo();
        }
        advertiserWeibo.setAccountNo(jsonAdvertiserWeibo.getString("accountNo"));
        advertiserWeibo.setAccountType(jsonAdvertiserWeibo.getInteger("accountType"));
        advertiserWeibo.setAccountPassword(AESUtil.encrypt(jsonAdvertiserWeibo.getString("accountPassword")));
        advertiserWeibo.setUid(jsonAdvertiserWeibo.getString("uid"));
        advertiserWeibo.setNickName(jsonAdvertiserWeibo.getString("nickName"));
        advertiserWeibo.setRelationProveLetter(jsonAdvertiserWeibo.getString("relationProveLetter"));
        advertiserWeibo.setAdvAuthLetter(jsonAdvertiserWeibo.getString("advAuthLetter"));
        advertiserWeibo.setPromotePromiseLetter(jsonAdvertiserWeibo.getString("promotePromiseLetter"));
        // 默认值
        advertiserWeibo.setOpenOrTrans(jsonAdvertiserWeibo.getString("openOrTrans"));
        
        // 获取营业资质修改的内容
        JSONObject jsonCredentials = jsonWeiboInfomation.getJSONObject("credentials");
        if(null==credentials || StringUtils.isBlank(credentials.getId())){
            credentials = new ErpStoreCredentials();
        }
        credentials.setRegisterName(jsonCredentials.getString("registerName"));
        credentials.setRegisterNo(jsonCredentials.getString("registerNo"));
        credentials.setStartDate(jsonCredentials.getDate("startDate"));
        credentials.setEndDate(jsonCredentials.getDate("endDate"));
        credentials.setBusinessScope(jsonCredentials.getInteger("businessScope"));
        credentials.setBusinessLicence(jsonCredentials.getString("businessLicence"));
        credentials.setSpecialCertificate(jsonCredentials.getString("specialCertificate"));
        
        // 获取联系人修改的内容
        JSONObject jsonLinkMan = jsonWeiboInfomation.getJSONObject("linkMan");
        if(null==linkMan || StringUtils.isBlank(linkMan.getId())){
            linkMan = new ErpStoreLinkman();
            linkMan.setStoreInfoId(storeid);
        }
        linkMan.setName(jsonLinkMan.getString("name"));
        linkMan.setPhone(jsonLinkMan.getString("phone"));
        linkMan.setAddress(jsonLinkMan.getString("address"));
        linkMan.setEmail(jsonLinkMan.getString("email"));
        
        // 保存修改
        erpStoreAdvertiserWeiboService.save(advertiserWeibo);
        erpStoreCredentialsService.save(credentials);
        erpStoreLinkmanService.save(linkMan);
        storeInfo.setAdvertiserWeiboId(advertiserWeibo.getId());
        storeInfo.setCredentialsId(credentials.getId());
        erpStoreInfoService.save(storeInfo);
        resObject.put("success", true);
        return resObject;
    }
    
    /**
     * 修改直销商户陌陌推广开户资料业管审核信息
     *
     * @param storeId
     * @return
     * @date 2018年1月10日
     * @author SunQ
     */
    @RequestMapping(value = "updateStoreMomoInfomationZhixiao")
    @ResponseBody
    public JSONObject updateStoreMomoInfomationZhixiao(HttpServletRequest request, HttpServletResponse response) {
        JSONObject resObject = new JSONObject();
        String str = request.getParameter("momoInfomation");
        JSONObject jsonMomoInfomation = JSONObject.parseObject(str);
        String storeid = jsonMomoInfomation.getString("storeid");
        ErpStoreInfo storeInfo = erpStoreInfoService.get(storeid);
        ErpStoreAdvertiserMomo advertiserMomo = erpStoreAdvertiserMomoService.getByStoreId(storeid);
        ErpStoreCredentials credentials = erpStoreCredentialsService.get(storeInfo.getCredentialsId());
        ErpStoreLinkman linkMan = erpStoreLinkmanService.findWhereStoreId("0", storeid);
        
        // 获取陌陌修改的内容
        JSONObject jsonAdvertiserMomo = jsonMomoInfomation.getJSONObject("advertiserMomo");
        if(null==advertiserMomo || StringUtils.isBlank(advertiserMomo.getId())){
            advertiserMomo = new ErpStoreAdvertiserMomo();
        }
        advertiserMomo.setAccountNo(jsonAdvertiserMomo.getString("accountNo"));
        advertiserMomo.setBrandName(jsonAdvertiserMomo.getString("brandName"));
        advertiserMomo.setIcp(jsonAdvertiserMomo.getString("icp"));
        advertiserMomo.setFollowZhangbeiScreenshot(jsonAdvertiserMomo.getString("followZhangbeiScreenshot"));
        advertiserMomo.setExpectedDeliveryTime(jsonAdvertiserMomo.getDate("expectedDeliveryTime"));
        advertiserMomo.setDeliveryUrl(jsonAdvertiserMomo.getString("deliveryUrl"));
        advertiserMomo.setDeliveryPic(jsonAdvertiserMomo.getString("deliveryPic"));
        advertiserMomo.setCopywritingPlan(jsonAdvertiserMomo.getString("copywritingPlan"));
        
        // 获取营业资质修改的内容
        JSONObject jsonCredentials = jsonMomoInfomation.getJSONObject("credentials");
        if(null==credentials || StringUtils.isBlank(credentials.getId())){
            credentials = new ErpStoreCredentials();
        }
        credentials.setRegisterName(jsonCredentials.getString("registerName"));
        credentials.setRegisterNo(jsonCredentials.getString("registerNo"));
        credentials.setBusinessLicence(jsonCredentials.getString("businessLicence"));
        credentials.setSpecialCertificate(jsonCredentials.getString("specialCertificate"));
        
        // 获取联系人修改的内容
        JSONObject jsonLinkMan = jsonMomoInfomation.getJSONObject("linkMan");
        if(null==linkMan || StringUtils.isBlank(linkMan.getId())){
            linkMan = new ErpStoreLinkman();
            linkMan.setStoreInfoId(storeid);
        }
        linkMan.setName(jsonLinkMan.getString("name"));
        linkMan.setPhone(jsonLinkMan.getString("phone"));
        
        // 获取门店修改的内容
        JSONObject jsonStoreInfo = jsonMomoInfomation.getJSONObject("storeInfo");
        storeInfo.setProductName(jsonStoreInfo.getString("productName"));
        storeInfo.setProductConcreteInfo(jsonStoreInfo.getString("productConcreteInfo"));
        storeInfo.setIcpMessage(jsonStoreInfo.getString("icpMessage"));
        
        // 保存修改
        erpStoreAdvertiserMomoService.save(advertiserMomo);
        erpStoreCredentialsService.save(credentials);
        erpStoreLinkmanService.save(linkMan);
        storeInfo.setAdvertiserMomoId(advertiserMomo.getId());
        storeInfo.setCredentialsId(credentials.getId());
        erpStoreInfoService.save(storeInfo);
        resObject.put("success", true);
        return resObject;
    }
    
    /**
     * 修改策划专家陌陌推广开户资料录入信息
     *
     * @param storeId
     * @return
     * @date 2018年1月10日
     * @author SunQ
     */
    @RequestMapping(value = "updateStoreMomoInfomationPlanning")
    @ResponseBody
    public JSONObject updateStoreMomoInfomationPlanning(HttpServletRequest request, HttpServletResponse response) {
        JSONObject resObject = new JSONObject();
        String str = request.getParameter("momoInfomationPlanning");
        JSONObject jsonMomoInfomationPlanning = JSONObject.parseObject(str);
        String storeid = jsonMomoInfomationPlanning.getString("storeid");
        ErpStoreInfo storeInfo = erpStoreInfoService.get(storeid);
        
        // 获取陌陌修改的内容
        JSONObject jsonAdvertiserMomo = jsonMomoInfomationPlanning.getJSONObject("advertiserMomo");
        ErpStoreAdvertiserMomo advertiserMomo = null;
        if(StringUtils.isNotBlank(jsonAdvertiserMomo.getString("id"))){
            advertiserMomo = erpStoreAdvertiserMomoService.get(jsonAdvertiserMomo.getString("id"));
        }else{
            advertiserMomo = new ErpStoreAdvertiserMomo();
        }
        advertiserMomo.setDeliveryPic(jsonAdvertiserMomo.getString("deliveryPic"));
        advertiserMomo.setCopywritingPlan(jsonAdvertiserMomo.getString("copywritingPlan"));
        
        // 获取门店修改的内容
        JSONObject jsonStoreInfo = jsonMomoInfomationPlanning.getJSONObject("storeInfo");
        storeInfo.setProductName(jsonStoreInfo.getString("productName"));
        storeInfo.setProductConcreteInfo(jsonStoreInfo.getString("productConcreteInfo"));
        storeInfo.setIcpMessage(jsonStoreInfo.getString("icpMessage"));
        
        // 保存修改
        erpStoreAdvertiserMomoService.save(advertiserMomo);
        storeInfo.setAdvertiserMomoId(advertiserMomo.getId());
        erpStoreInfoService.save(storeInfo);
        resObject.put("success", true);
        return resObject;
    }
    
    /**
     * 修改服务商商户朋友圈推广开户资料业管录入信息
     *
     * @param storeId
     * @return
     * @date 2018年1月10日
     * @author SunQ
     */
    @RequestMapping(value = "updateStoreFriendsInfomationService")
    @ResponseBody
    public JSONObject updateStoreFriendsInfomationService(HttpServletRequest request, HttpServletResponse response) {
        JSONObject resObject = new JSONObject();
        String friendsInfomationStr = request.getParameter("friendsInfomation");
        JSONObject jsonObj = JSONObject.parseObject(friendsInfomationStr);
        String storeid = jsonObj.getString("storeid");
        ErpStoreInfo storeInfo = erpStoreInfoService.get(storeid);
        String accountOriginalId = jsonObj.getString("accountOriginalId");
        String advertiserScreenshot = jsonObj.getString("advertiserScreenshot");
        String storeScreenshot = jsonObj.getString("storeScreenshot");
        JSONObject credentialsObj = jsonObj.getJSONObject("credentials");
        String specialCertificate = credentialsObj.getString("specialCertificate");
        ErpStoreAdvertiserFriends advertiserFriends = erpStoreAdvertiserFriendsService.getByStoreId(storeid);
        if(null==advertiserFriends || StringUtils.isBlank(advertiserFriends.getId())){
            advertiserFriends = new ErpStoreAdvertiserFriends();
        }
        advertiserFriends.setAccountOriginalId(accountOriginalId);
        advertiserFriends.setAdvertiserScreenshot(advertiserScreenshot);
        advertiserFriends.setStoreScreenshot(storeScreenshot);
        ErpStoreCredentials credentials = null;
        if(StringUtils.isNotBlank(storeInfo.getCredentialsId())){
            credentials = erpStoreCredentialsService.get(storeInfo.getCredentialsId());
        }else{
            credentials = new ErpStoreCredentials();
        }
        credentials.setSpecialCertificate(specialCertificate);
        erpStoreAdvertiserFriendsService.save(advertiserFriends);
        erpStoreCredentialsService.save(credentials);
        storeInfo.setAdvertiserFriendsId(advertiserFriends.getId());
        storeInfo.setCredentialsId(credentials.getId());
        erpStoreInfoService.save(storeInfo);
        resObject.put("success", true);
        return resObject;
    }
    
    /**
     * 修改服务商商户微博推广开户资料业管录入信息
     *
     * @param storeId
     * @return
     * @date 2018年1月10日
     * @author SunQ
     */
    @RequestMapping(value = "updateStoreWeiboInfomationService")
    @ResponseBody
    public JSONObject updateStoreWeiboInfomationService(HttpServletRequest request, HttpServletResponse response) {
        JSONObject resObject = new JSONObject();
        String weiboInfomationStr = request.getParameter("weiboInfomation");
        JSONObject jsonWeiboInfomation = JSONObject.parseObject(weiboInfomationStr);
        String storeid = jsonWeiboInfomation.getString("storeid");
        ErpStoreInfo storeInfo = erpStoreInfoService.get(storeid);
        ErpStoreAdvertiserWeibo advertiserWeibo = erpStoreAdvertiserWeiboService.getByStoreId(storeid);
        ErpStoreCredentials credentials = erpStoreCredentialsService.get(storeInfo.getCredentialsId());
        ErpStoreLinkman linkMan = erpStoreLinkmanService.findWhereStoreId("0", storeid);
        
        // 获取微博修改的内容
        JSONObject jsonAdvertiserWeibo = jsonWeiboInfomation.getJSONObject("advertiserWeibo");
        if(null==advertiserWeibo || null==advertiserWeibo.getId()){
            advertiserWeibo = new ErpStoreAdvertiserWeibo();
        }
        advertiserWeibo.setAccountNo(jsonAdvertiserWeibo.getString("accountNo"));
        advertiserWeibo.setAccountPassword(AESUtil.encrypt(jsonAdvertiserWeibo.getString("accountPassword")));
        advertiserWeibo.setUid(jsonAdvertiserWeibo.getString("uid"));
        advertiserWeibo.setNickName(jsonAdvertiserWeibo.getString("nickName"));
        advertiserWeibo.setRelationProveLetter(jsonAdvertiserWeibo.getString("relationProveLetter"));
        advertiserWeibo.setAdvAuthLetter(jsonAdvertiserWeibo.getString("advAuthLetter"));
        advertiserWeibo.setPromotePromiseLetter(jsonAdvertiserWeibo.getString("promotePromiseLetter"));
        
        advertiserWeibo.setOpenOrTrans(jsonAdvertiserWeibo.getString("openOrTrans"));
        // 获取营业资质修改的内容
        JSONObject jsonCredentials = jsonWeiboInfomation.getJSONObject("credentials");
        if(null==credentials || StringUtils.isBlank(credentials.getId())){
            credentials = new ErpStoreCredentials();
        }
        credentials.setRegisterName(jsonCredentials.getString("registerName"));
        credentials.setRegisterNo(jsonCredentials.getString("registerNo"));
        credentials.setStartDate(jsonCredentials.getDate("startDate"));
        credentials.setEndDate(jsonCredentials.getDate("endDate"));
        credentials.setBusinessScope(jsonCredentials.getInteger("businessScope"));
        credentials.setBusinessLicence(jsonCredentials.getString("businessLicence"));
        credentials.setSpecialCertificate(jsonCredentials.getString("specialCertificate"));
        
        // 获取联系人修改的内容
        JSONObject jsonLinkMan = jsonWeiboInfomation.getJSONObject("linkMan");
        if(null==linkMan || StringUtils.isBlank(linkMan.getId())){
            linkMan = new ErpStoreLinkman();
            linkMan.setStoreInfoId(storeid);
        }
        linkMan.setName(jsonLinkMan.getString("name"));
        linkMan.setPhone(jsonLinkMan.getString("phone"));
        linkMan.setAddress(jsonLinkMan.getString("address"));
        linkMan.setEmail(jsonLinkMan.getString("email"));
        
        // 保存修改
        erpStoreAdvertiserWeiboService.save(advertiserWeibo);
        erpStoreCredentialsService.save(credentials);
        erpStoreLinkmanService.save(linkMan);
        storeInfo.setAdvertiserWeiboId(advertiserWeibo.getId());
        storeInfo.setCredentialsId(credentials.getId());
        erpStoreInfoService.save(storeInfo);
        resObject.put("success", true);
        return resObject;
    }
    
    /**
     * 修改服务商商户陌陌推广开户资料业管录入信息
     *
     * @param storeId
     * @return
     * @date 2018年1月10日
     * @author SunQ
     */
    @RequestMapping(value = "updateStoreMomoInfomationService")
    @ResponseBody
    public JSONObject updateStoreMomoInfomationService(HttpServletRequest request, HttpServletResponse response) {
        JSONObject resObject = new JSONObject();
        String str = request.getParameter("momoInfomation");
        JSONObject jsonMomoInfomation = JSONObject.parseObject(str);
        String storeid = jsonMomoInfomation.getString("storeid");
        ErpStoreInfo storeInfo = erpStoreInfoService.get(storeid);
        ErpStoreAdvertiserMomo advertiserMomo = erpStoreAdvertiserMomoService.getByStoreId(storeid);
        ErpStoreCredentials credentials = erpStoreCredentialsService.get(storeInfo.getCredentialsId());
        ErpStoreLinkman linkMan = erpStoreLinkmanService.findWhereStoreId("0", storeid);
        
        // 获取陌陌修改的内容
        JSONObject jsonAdvertiserMomo = jsonMomoInfomation.getJSONObject("advertiserMomo");
        if(null==advertiserMomo || StringUtils.isBlank(advertiserMomo.getId())){
            advertiserMomo = new ErpStoreAdvertiserMomo();
        }
        advertiserMomo.setAccountNo(jsonAdvertiserMomo.getString("accountNo"));
        advertiserMomo.setBrandName(jsonAdvertiserMomo.getString("brandName"));
        advertiserMomo.setIcp(jsonAdvertiserMomo.getString("icp"));
        advertiserMomo.setFollowZhangbeiScreenshot(jsonAdvertiserMomo.getString("followZhangbeiScreenshot"));
        advertiserMomo.setExpectedDeliveryTime(jsonAdvertiserMomo.getDate("expectedDeliveryTime"));
        advertiserMomo.setDeliveryUrl(jsonAdvertiserMomo.getString("deliveryUrl"));
        advertiserMomo.setDeliveryPic(jsonAdvertiserMomo.getString("deliveryPic"));
        advertiserMomo.setCopywritingPlan(jsonAdvertiserMomo.getString("copywritingPlan"));
        
        // 获取营业资质修改的内容
        JSONObject jsonCredentials = jsonMomoInfomation.getJSONObject("credentials");
        if(null==credentials || StringUtils.isBlank(credentials.getId())){
            credentials = new ErpStoreCredentials();
        }
        credentials.setRegisterName(jsonCredentials.getString("registerName"));
        credentials.setRegisterNo(jsonCredentials.getString("registerNo"));
        credentials.setBusinessLicence(jsonCredentials.getString("businessLicence"));
        credentials.setSpecialCertificate(jsonCredentials.getString("specialCertificate"));
        
        // 获取联系人修改的内容
        JSONObject jsonLinkMan = jsonMomoInfomation.getJSONObject("linkMan");
        if(null==linkMan || StringUtils.isBlank(linkMan.getId())){
            linkMan = new ErpStoreLinkman();
            linkMan.setStoreInfoId(storeid);
        }
        linkMan.setName(jsonLinkMan.getString("name"));
        linkMan.setPhone(jsonLinkMan.getString("phone"));
        
        // 获取门店修改的内容
        JSONObject jsonStoreInfo = jsonMomoInfomation.getJSONObject("storeInfo");
        storeInfo.setProductName(jsonStoreInfo.getString("productName"));
        storeInfo.setProductConcreteInfo(jsonStoreInfo.getString("productConcreteInfo"));
        storeInfo.setIcpMessage(jsonStoreInfo.getString("icpMessage"));
        
        // 保存修改
        erpStoreAdvertiserMomoService.save(advertiserMomo);
        erpStoreCredentialsService.save(credentials);
        erpStoreLinkmanService.save(linkMan);
        storeInfo.setAdvertiserMomoId(advertiserMomo.getId());
        storeInfo.setCredentialsId(credentials.getId());
        erpStoreInfoService.save(storeInfo);
        resObject.put("success", true);
        return resObject;
    }
    /* ===============================门店相关信息修改 END============================== */

    /**
     * 上传微博推广开户资料（Excel）
     * 
     * @param file
     * @param storeId
     * @return
     */
    @RequestMapping(value = "uploadStoreWeiboInfomationService", method = RequestMethod.POST)
    public @ResponseBody BaseResult uploadStoreWeiboInfomationService(@RequestParam(value = "attach") MultipartFile file, String storeId) {
        BaseResult result = null;
        try {
            result = jykFlowStoreInfoService.uploadStoreWeiboInfomationService(file, storeId);
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
            return new ServiceErrorResult(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new SystemErrorResult();
        }
        return result;
    }

    /**
     * 上传陌陌推广开户资料（Excel）
     * 
     * @param file
     * @param storeId
     * @return
     */
    @RequestMapping(value = "uploadStoreMomoInfomationService", method = RequestMethod.POST)
    public @ResponseBody BaseResult uploadStoreMomoInfomationService(@RequestParam(value = "attach") MultipartFile file, String storeId) {
        BaseResult result = null;
        try {
            result = jykFlowStoreInfoService.uploadStoreMomoInfomationService(file, storeId);
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
            return new ServiceErrorResult(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new SystemErrorResult();
        }
        return result;
    }

}
