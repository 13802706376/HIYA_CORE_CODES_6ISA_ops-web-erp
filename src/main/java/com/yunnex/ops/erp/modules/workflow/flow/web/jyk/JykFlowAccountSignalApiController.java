package com.yunnex.ops.erp.modules.workflow.flow.web.jyk;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.workflow.flow.service.JykFlowAccountSignalService;

/**
 * 流程自动流转对外调用接口
 * 
 * @author SunQ
 * @date 2018年1月17日
 */
@Controller
@RequestMapping(value = "api/accountSignal/")
public class JykFlowAccountSignalApiController extends BaseController {

    @Autowired
    private JykFlowAccountSignalService jykFlowAccountSignalService;
    
    /**
     * 商户完成推广图片上传
     *
     * @param storeId
     * @return
     * @date 2018年1月17日
     * @author SunQ
     */
    @RequestMapping(value="uploadPPromotionalPictures", method=RequestMethod.POST)
    @ResponseBody
    public JSONObject uploadPPromotionalPictures(@RequestParam String storeId) {
        
        JSONObject resObject = new JSONObject();
        try {
            if(StringUtils.isNotBlank(storeId)){
                jykFlowAccountSignalService.uploadPPromotionalPictures(storeId);
                resObject.put("code", 0);
                resObject.put("message", "操作成功");
                logger.info("商户完成推广图片上传流转操作成功");
            }else{
                resObject.put("code", 1);
                resObject.put("message", "storeId不能为空");
                logger.info("商户完成推广图片上传流转操作失败");
            }
        } catch (RuntimeException e) {
            resObject.put("code", 1);
            resObject.put("message", "系统异常");
            logger.info("uploadPPromotionalPictures->出现异常：{}", e.getMessage());
            logger.error(e.getMessage(), e);
        }
        return resObject;
    }
}