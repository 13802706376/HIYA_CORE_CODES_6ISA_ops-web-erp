package com.yunnex.ops.erp.modules.workflow.effect.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitInfo;
import com.yunnex.ops.erp.modules.order.service.ErpOrderSplitInfoService;
import com.yunnex.ops.erp.modules.workflow.effect.entity.JykDeliveryEffectInfo;
import com.yunnex.ops.erp.modules.workflow.effect.service.JykDeliveryEffectInfoService;

/**
 * 聚引客投放效果Controller
 * 
 * @author SunQ
 * @date 2018年1月25日
 */
@Controller
@RequestMapping(value = "${adminPath}/deliveryEffect/")
public class JykDeliveryEffectInfoController extends BaseController {

    @Autowired
    private JykDeliveryEffectInfoService jykDeliveryEffectInfoService;
    @Autowired
    private ErpOrderSplitInfoService erpOrderSplitInfoService;
    
    /**
     * 
     *
     * @param deliveryEffectInfo
     * @return
     * @date 2018年1月25日
     * @author SunQ
     */
    @ResponseBody
    @RequestMapping(value = "getInfo")
    public BaseResult getInfo(String procInsId) {
        BaseResult result = new BaseResult();
        JykDeliveryEffectInfo deliveryEffectInfo = jykDeliveryEffectInfoService.getByProcInsId(procInsId);
        if(deliveryEffectInfo==null){
            deliveryEffectInfo = new JykDeliveryEffectInfo();
        }
        result.setAttach(deliveryEffectInfo);
        return result;
    }
    
    /**
     * 上传投放效果图片页面
     *
     * @param request
     * @param response
     * @param model
     * @return
     * @date 2018年1月26日
     * @author SunQ
     */
    @RequestMapping(value = "uploadDeliveryEffect")
    public String uploadDeliveryEffect(HttpServletRequest request, HttpServletResponse response, Model model) {
        // model.addAttribute("", "");
        return "modules/workflow/effect/uploadDeliveryEffect";
    } 
    
    /**
     * 保存效果预览对象
     * state : 如果保存，对应值传“1”；发布到小程序，对应值传“2”；确认投放预览，对应值传“3”。
     * @return
     * @date 2018年1月25日
     * @author SunQ
     */
    @ResponseBody
    @RequestMapping(value = "save")
    public BaseResult save(HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        String data = request.getParameter("data");
        JSONObject infomation = JSONObject.parseObject(data); 
        String id = infomation.getString("id");
        String procInsId = infomation.getString("procInsId");
        String state = infomation.getString("state");
        String wechatoutPic = infomation.getString("outerImgUrlFriends");
        String wechatinPic = infomation.getString("innerImgUrlFriends");
        String weibooutPic = infomation.getString("outerImgUrlWeibo");
        String weiboinPic = infomation.getString("innerImgUrlWeibo");
        String momooutPic = infomation.getString("outerImgUrlMomo");
        String momoinPic = infomation.getString("innerImgUrlMomo");
        String wechatoutPicFileName = infomation.getString("outerImgNameFriends");
        String wechatinPicFileName = infomation.getString("innerImgNameFriends");
        String weibooutPicFileName = infomation.getString("outerImgNameWeibo");
        String weiboinPicFileName = infomation.getString("innerImgNameWeibo");
        String momooutPicFileName = infomation.getString("outerImgNameMomo");
        String momoinPicFileName = infomation.getString("innerImgNameMomo");
        
        if(StringUtils.isBlank(procInsId)){
            result.setCode("1");
            result.setMessage("procInsId不能为空");
            return result;
        }
        
        if(StringUtils.isBlank(state)){
            result.setCode("1");
            result.setMessage("state不能为空");
            return result;
        }
        
        if(StringUtils.isNotBlank(id)){
            JykDeliveryEffectInfo deliveryEffectInfo = jykDeliveryEffectInfoService.get(id);
            deliveryEffectInfo.setProcInsId(procInsId);
            deliveryEffectInfo.setOuterImgUrlFriends(wechatoutPic);
            deliveryEffectInfo.setOuterImgNameFriends(wechatoutPicFileName);
            deliveryEffectInfo.setOuterImgUrlWeibo(weibooutPic);
            deliveryEffectInfo.setOuterImgNameWeibo(weibooutPicFileName);
            deliveryEffectInfo.setOuterImgUrlMomo(momooutPic);
            deliveryEffectInfo.setOuterImgNameMomo(momooutPicFileName);
            deliveryEffectInfo.setInnerImgUrlFriends(wechatinPic);
            deliveryEffectInfo.setInnerImgNameFriends(wechatinPicFileName);
            deliveryEffectInfo.setInnerImgUrlWeibo(weiboinPic);
            deliveryEffectInfo.setInnerImgNameWeibo(weiboinPicFileName);
            deliveryEffectInfo.setInnerImgUrlMomo(momoinPic);
            deliveryEffectInfo.setInnerImgNameMomo(momoinPicFileName);
            deliveryEffectInfo.setState(state);
            jykDeliveryEffectInfoService.save(deliveryEffectInfo);
        }else{
            ErpOrderSplitInfo splitInfo = erpOrderSplitInfoService.getByProsIncId(procInsId);
            JykDeliveryEffectInfo deliveryEffectInfo = new JykDeliveryEffectInfo();
            deliveryEffectInfo.setOrderId(splitInfo.getOrderId());
            deliveryEffectInfo.setSplitId(splitInfo.getId());
            deliveryEffectInfo.setProcInsId(procInsId);
            deliveryEffectInfo.setOuterImgUrlFriends(wechatoutPic);
            deliveryEffectInfo.setOuterImgNameFriends(wechatoutPicFileName);
            deliveryEffectInfo.setOuterImgUrlWeibo(weibooutPic);
            deliveryEffectInfo.setOuterImgNameWeibo(weibooutPicFileName);
            deliveryEffectInfo.setOuterImgUrlMomo(momooutPic);
            deliveryEffectInfo.setOuterImgNameMomo(momooutPicFileName);
            deliveryEffectInfo.setInnerImgUrlFriends(wechatinPic);
            deliveryEffectInfo.setInnerImgNameFriends(wechatinPicFileName);
            deliveryEffectInfo.setInnerImgUrlWeibo(weiboinPic);
            deliveryEffectInfo.setInnerImgNameWeibo(weiboinPicFileName);
            deliveryEffectInfo.setInnerImgUrlMomo(momoinPic);
            deliveryEffectInfo.setInnerImgNameMomo(momoinPicFileName);
            deliveryEffectInfo.setState(state);
            jykDeliveryEffectInfoService.deleteBefore(splitInfo.getId());
            jykDeliveryEffectInfoService.save(deliveryEffectInfo);
        }
        return result;
    }
    
    /**
     * 推送效果预览对象到小程序
     *
     * @param id
     * @return
     * @date 2018年1月25日
     * @author SunQ
     */
    @ResponseBody
    @RequestMapping(value = "pushWxapp")
    public BaseResult pushWxapp(String id) {
        BaseResult result = new BaseResult();
        jykDeliveryEffectInfoService.updateState(id, "2");
        return result;
    }
    
    /**
     * 确认投放预览
     *
     * @param id
     * @return
     * @date 2018年1月25日
     * @author SunQ
     */
    @ResponseBody
    @RequestMapping(value = "confirmDelivery")
    public BaseResult confirmDelivery(String id) {
        BaseResult result = new BaseResult();
        jykDeliveryEffectInfoService.updateState(id, "3");
        return result;
    }
}