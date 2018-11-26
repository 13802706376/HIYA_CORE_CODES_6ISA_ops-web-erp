package com.yunnex.ops.erp.modules.workflow.flow.web.jyk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.workflow.flow.common.JykFlowConstants;
import com.yunnex.ops.erp.modules.workflow.flow.service.JykFlow3P5Service;

/**
 * 聚引客3.5.0版本优化更新控制器
 * 
 * @author R/Q
 * @date 2018年10月18日
 */
@Controller
@RequestMapping(value = "${adminPath}/jyk/flow/3p5")
public class JykFlowController3p5 extends BaseController {

    @Autowired
    private JykFlow3P5Service jykFlow3P5Service;


    /**
     * 业务定义：3.5.0【商户对接/确认推广门店/资质/推广时间】节点完成
     * 
     * @date 2018年10月18日
     * @author R/Q
     */
    @ResponseBody
    @RequestMapping(value = JykFlowConstants.CONTACT_SHOP_3P5)
    public JSONObject contactShop3P5(String taskId, String procInsId, boolean isFinished, String storeId, String storeName, String license,
                    String qualification, String extensionExpect, String nextLicenseTime, String nextQualificationTime,
                    String nextExtensionExpectTime, String promotionTime, String[] channels) {
        return jykFlow3P5Service.contactShop3P5(taskId, procInsId, isFinished, storeId, storeName, license, qualification, extensionExpect,
                        nextLicenseTime, nextQualificationTime, nextExtensionExpectTime, promotionTime, channels);
    }

}
