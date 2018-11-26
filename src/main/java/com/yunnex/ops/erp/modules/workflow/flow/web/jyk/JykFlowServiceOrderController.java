package com.yunnex.ops.erp.modules.workflow.flow.web.jyk;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitInfo;
import com.yunnex.ops.erp.modules.order.service.ErpOrderSplitInfoService;
import com.yunnex.ops.erp.modules.qualify.entity.ErpShopExtensionQualify;
import com.yunnex.ops.erp.modules.qualify.entity.ErpShopPayQualify;
import com.yunnex.ops.erp.modules.qualify.service.ErpShopExtensionQualifyService;
import com.yunnex.ops.erp.modules.qualify.service.ErpShopPayQualifyService;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.shop.service.ErpShopInfoService;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;
import com.yunnex.ops.erp.modules.workflow.flow.common.JykFlowConstants;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderFile;
import com.yunnex.ops.erp.modules.workflow.flow.service.ErpOrderFileService;
import com.yunnex.ops.erp.modules.workflow.flow.service.WorkFlowService;

/**
 * 服务商商户开户流程控制器
 * 
 * @author yunnex
 * @date 2017年11月4日
 */
@Controller
@RequestMapping(value = "${adminPath}/jyk/flow")
public class JykFlowServiceOrderController extends BaseController {
    @Autowired
    private WorkFlowService workFlowService;
    @Autowired
    private ErpShopInfoService erpShopInfoService;
    @Autowired
    private ErpOrderSplitInfoService erpOrderSplitInfoService;
    @Autowired
    private ErpShopPayQualifyService erpShopPayQualifyService;
    @Autowired
    private ErpShopExtensionQualifyService erpShopExtensionQualifyService;
    @Autowired
    private ErpOrderFileService erpOrderFileService;
    
    @RequestMapping(value = "apply_service_yunnex_info_pieces")
    @ResponseBody
    public JSONObject applyServiceYunnexInfoPieces(String taskId, String procInsId, String channelList, boolean isFinished) {
        JSONObject resObject = new JSONObject();
        // 保存业务流转信息
        this.workFlowService.submitSubTask(procInsId, channelList, taskId);
        if (isFinished) {
            // 更新商户进件状态为已进件
            ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);
            ErpShopInfo erpShopInfo = erpShopInfoService.findListByZhangbeiId(split.getShopId());
         /*   if(erpShopInfo==null){
                resObject.put("result", false);
                resObject.put("message", "商户数据没有同步，待商户数据同步后再处理该订单。");
                return resObject;
            }*/
         /*   if (!this.erpShopInfoApiService.isShopInputPieces(split.getShopId())) {
                resObject.put("result", false);
                resObject.put("message", "掌贝地推未进件，请确认是否已完成地推进件。");
                return resObject;
            }*/
            if(null!=erpShopInfo)
            {
            	 this.erpShopInfoService.updateIntoPiecesById(erpShopInfo.getId());
            }
            this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId, UserUtils.getUser().getId() + "策划专家为:" + UserUtils
                            .getUser().getId() + "掌贝进件成功", null);
        }
        resObject.put("result", true);
        return resObject;
    }

    @RequestMapping(value = "apply_service_weChat_pay_into_pieces")
    @ResponseBody
    public JSONObject applyServiceWeChatPayIntoPieces(String taskId, String procInsId, String channelList) {
        JSONObject resObject = new JSONObject();
        // 保存业务流转信息
        this.workFlowService.submitSubTask(procInsId, channelList,taskId);
        // 更新商户进件状态为已进件
        ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);
        ErpShopInfo erpShopInfo = erpShopInfoService.findListByZhangbeiId(split.getShopId());
      /*  if(erpShopInfo==null){
            resObject.put("result", false);
            resObject.put("message", "商户数据没有同步，待商户数据同步后再处理该订单。");
            return resObject;
        }*/
     /*   if (!this.erpShopInfoApiService.isShopInputPieces(split.getShopId())) {
            resObject.put("result", false);
            resObject.put("message", "掌贝地推未进件，请确认是否已完成地推进件。");
            return resObject;
        }*/
        if(null!=erpShopInfo)
        {
        	ErpShopPayQualify payQualify = new ErpShopPayQualify();
            payQualify.setShopId(erpShopInfo.getId());
            payQualify.setPayValue("2");
            this.erpShopPayQualifyService.save(payQualify);
        }
        this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId, UserUtils.getUser().getId() + "策划专家为:" + UserUtils
                        .getUser().getId() + "微信支付进件成功", null);
        resObject.put("result", true);
        return resObject;
    }

    @RequestMapping(value = "apply_service_submit_friends_account_info")
    @ResponseBody
    public JSONObject applyServiceSubmitFriendsAccountInfo(String taskId, String procInsId, String channelList, boolean isFinished) {
        JSONObject resObject = new JSONObject();
        // 保存业务流转信息
        this.workFlowService.submitSubTask(procInsId, channelList,taskId);
        if (isFinished) {
            // 更新商户进件状态为已进件
            ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);
      /*      if (!this.erpShopInfoApiService.isShopInputPieces(split.getShopId())) {
                resObject.put("result", false);
                resObject.put("message", "掌贝地推未进件，请确认是否已完成地推进件。");
                return resObject;
            }*/
            ErpShopInfo erpShopInfo = erpShopInfoService.findListByZhangbeiId(split.getShopId());
          /*  if(erpShopInfo==null){
                resObject.put("result", false);
                resObject.put("message", "商户数据没有同步，待商户数据同步后再处理该订单。");
                return resObject;
            }*/
            if(null!=erpShopInfo)
            {
            	 ErpShopExtensionQualify extensionQualify = new ErpShopExtensionQualify();
                 extensionQualify.setShopId(erpShopInfo.getId());
                 extensionQualify.setExtensionValue("1");
                 this.erpShopExtensionQualifyService.save(extensionQualify);
            }
            this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId, UserUtils.getUser().getId() + "策划专家为:" + UserUtils
                            .getUser().getId() + "支付进件成功", null);
        }
        resObject.put("result", true);
        return resObject;
    }

    @RequestMapping(value = "apply_service_submit_weibo_account_info")
    @ResponseBody
    public JSONObject applyServiceSubmitWeiboAccountInfo(String taskId, String procInsId, String channelList, boolean isFinished) {
        JSONObject resObject = new JSONObject();
        // 保存业务流转信息
        this.workFlowService.submitSubTask(procInsId, channelList,taskId);
        if (isFinished) {
            // 更新商户进件状态为已进件
            ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);
            ErpShopInfo erpShopInfo = erpShopInfoService.findListByZhangbeiId(split.getShopId());
        /*    if(erpShopInfo==null){
                resObject.put("result", false);
                resObject.put("message", "商户数据没有同步，待商户数据同步后再处理该订单。");
                return resObject;
            }*/
        /*    if (!this.erpShopInfoApiService.isShopInputPieces(split.getShopId())) {
                resObject.put("result", false);
                resObject.put("message", "掌贝地推未进件，请确认是否已完成地推进件。");
                return resObject;
            }*/
            if(null!=erpShopInfo)
            {
                ErpShopExtensionQualify extensionQualify = new ErpShopExtensionQualify();
                extensionQualify.setShopId(erpShopInfo.getId());
                extensionQualify.setExtensionValue("2");
                this.erpShopExtensionQualifyService.save(extensionQualify);
            }
            this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId, UserUtils.getUser().getId() + "策划专家为:" + UserUtils
                            .getUser().getId() + "微博进件成功", null);
        }
        resObject.put("result", true);
        return resObject;
    }

    @RequestMapping(value = "apply_service_submit_momo_account_info")
    @ResponseBody
    public JSONObject applyServiceSubmitMomoAccountInfo(String taskId, String procInsId, String channelList, boolean isFinished) {
        JSONObject resObject = new JSONObject();
        // 保存业务流转信息
        this.workFlowService.submitSubTask(procInsId, channelList,taskId);
        if (isFinished) {
            // 更新商户进件状态为已进件
            ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);
          /*  if (!this.erpShopInfoApiService.isShopInputPieces(split.getShopId())) {
                resObject.put("result", false);
                resObject.put("message", "掌贝地推未进件，请确认是否已完成地推进件。");
                return resObject;
            }*/
            ErpShopInfo erpShopInfo = erpShopInfoService.findListByZhangbeiId(split.getShopId());
     /*       if(erpShopInfo==null){
                resObject.put("result", false);
                resObject.put("message", "商户数据没有同步，待商户数据同步后再处理该订单。");
                return resObject;
            }*/
            if(null != erpShopInfo)
            {
            	 ErpShopExtensionQualify extensionQualify = new ErpShopExtensionQualify();
                 extensionQualify.setShopId(erpShopInfo.getId());
                 extensionQualify.setExtensionValue("3");
                 this.erpShopExtensionQualifyService.save(extensionQualify);
            }
            this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId, UserUtils.getUser().getId() + "策划专家为:" + UserUtils
                            .getUser().getId() + "陌陌进件成功", null);
        }
        resObject.put("result", true);
        return resObject;
    }
    

    @RequestMapping(value = "apply_service_upload_picture_material")
    @ResponseBody
    public JSONObject applyServiceUploadPictureMaterial(String taskId, String procInsId, String orderFileId,String orderFileName) {
        JSONObject resObject = new JSONObject();
        
        if (StringUtils.isNotBlank(orderFileId)) {
            Map<String, Object> vars = Maps.newHashMap();

            // 获取多文件对象
            String[] orderFileIds = orderFileId.split(",");
            for (String id : orderFileIds) {
                // 更新当前的文件为正式文件
                ErpOrderFile file = this.erpOrderFileService.get(id);
                file.setDelFlag("0");
                this.erpOrderFileService.save(file);
            }
            this.workFlowService.setVariable(taskId, "UploadPictureMaterial", orderFileName);
            // 修改子任务完成状态
            this.workFlowService.submitSubTask(procInsId, "1,", taskId);
            this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId, "提交推广图片素材", vars);
            // 插入上传文件信息表
        }
        resObject.put("result", true);
        return resObject;
    }

}
