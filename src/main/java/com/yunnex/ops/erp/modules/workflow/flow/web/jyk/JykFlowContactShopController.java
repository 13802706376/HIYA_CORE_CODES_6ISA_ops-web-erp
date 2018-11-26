package com.yunnex.ops.erp.modules.workflow.flow.web.jyk;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.utils.DateUtils;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.act.entity.TaskExt;
import com.yunnex.ops.erp.modules.act.service.TaskExtService;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitInfo;
import com.yunnex.ops.erp.modules.order.service.ErpOrderOriginalInfoService;
import com.yunnex.ops.erp.modules.order.service.ErpOrderSplitInfoService;
import com.yunnex.ops.erp.modules.qualify.entity.ErpShopExtensionQualify;
import com.yunnex.ops.erp.modules.qualify.entity.ErpShopPayQualify;
import com.yunnex.ops.erp.modules.qualify.service.ErpShopExtensionQualifyService;
import com.yunnex.ops.erp.modules.qualify.service.ErpShopPayQualifyService;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.shop.service.ErpShopInfoApiService;
import com.yunnex.ops.erp.modules.shop.service.ErpShopInfoService;
import com.yunnex.ops.erp.modules.sys.utils.DwrUtils;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;
import com.yunnex.ops.erp.modules.workflow.channel.entity.JykOrderPromotionChannel;
import com.yunnex.ops.erp.modules.workflow.channel.service.JykOrderPromotionChannelService;
import com.yunnex.ops.erp.modules.workflow.flow.common.JykFlowConstants;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderFile;
import com.yunnex.ops.erp.modules.workflow.flow.entity.JykFlow;
import com.yunnex.ops.erp.modules.workflow.flow.service.ErpOrderFileService;
import com.yunnex.ops.erp.modules.workflow.flow.service.JykFlowContactShopService;
import com.yunnex.ops.erp.modules.workflow.flow.service.JykFlowService;
import com.yunnex.ops.erp.modules.workflow.flow.service.WorkFlowService;
import com.yunnex.ops.erp.modules.workflow.user.service.ErpOrderFlowUserService;

/**
 * 聚引客联系商户前段控制Controller
 * 
 * @author yunnex
 * @date 2017年11月2日
 */
@Controller
@RequestMapping(value = "${adminPath}/jyk/flow")
public class JykFlowContactShopController extends BaseController {
    @Autowired
    private WorkFlowService workFlowService;
    @Autowired
    private JykFlowContactShopService jykFlowContactShopService;
    @Autowired
    private JykFlowService flowService;
    @Autowired
    private ErpOrderOriginalInfoService orderService;
    /** 订单分单信息表 */
    @Autowired
    private ErpOrderSplitInfoService erpOrderSplitInfoService;
    /** 商户信息表 */
    @Autowired
    private ErpShopInfoService erpShopInfoService;

    @Autowired
    private JykOrderPromotionChannelService jykOrderPromotionChannelService;
    @Autowired
    private ErpShopExtensionQualifyService erpShopExtensionQualifyService;
    @Autowired
    private ErpShopPayQualifyService erpShopPayQualifyService;
    @Autowired
    private ErpOrderFlowUserService erpOrderFlowUserService;

    @Autowired
    private ErpOrderFileService erpOrderFileService;

    @Autowired
    private ErpShopInfoApiService erpShopInfoApiService;

    @Autowired
    private DwrUtils dwrUtils;
    @Autowired
    private TaskExtService taskExtService;

    @RequestMapping(value = "assign_planning_experts")
    @ResponseBody
    public JSONObject assignPlanningExperts(String taskId, String procInsId, String planningExpert) {
        // 保存业务流转信息
        this.jykFlowContactShopService.assignPlanningExperts(procInsId, planningExpert, taskId);
        JSONObject resObject = new JSONObject();

        this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId,
                        UserUtils.getUser().getId() + "指派策划专家为:" + planningExpert, null);
        dwrUtils.dwr(planningExpert);
        resObject.put("result", true);

        return resObject;
    }

    @RequestMapping(value = "choose_promotion_channel")
    @ResponseBody
    public JSONObject choosePromotionChannel(String taskId, String procInsId, String channelList) {
        JSONObject resObject = new JSONObject();
        // 获取信息
        JykFlow jykFlow = flowService.getByProcInstId(procInsId);
        if (jykFlow == null) {
            resObject.put("result", false);
            resObject.put("message", "非法的订单编号，请确认订单编号是否正确!");
            return resObject;
        }
        // 设置流程变量
        Map<String, Object> vars = Maps.newHashMap();
        String[] channel = channelList.split(",");
        for (int i = 0; i < channel.length; i++) {
            if ("1".equals(channel[i])) {
                vars.put("friends", "1");
            }
            if ("2".equals(channel[i])) {
                vars.put("weibo", "2");
            }
            if ("3".equals(channel[i])) {
                vars.put("momo", "3");
            }
        }
        // 保存推广渠道
        this.jykFlowContactShopService.choosePromotionChannel(procInsId, channelList, jykFlow, taskId);
        ErpOrderOriginalInfo order = orderService.get(jykFlow.getOrderId());
        if (order == null) {
            resObject.put("result", false);
            resObject.put("message", "非法的订单编号，请确认订单编号是否正确!");
            return resObject;
        }
        vars.put("orderType", order.getOrderType());
        this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId, "策划专家设置推广渠道", vars);
        resObject.put("result", true);
        return resObject;
    }

    @RequestMapping(value = "contact_service")
    @ResponseBody
	public JSONObject contactService(String taskId, String procInsId, String channelList) {
		JSONObject resObject = new JSONObject();
		// 提交流程变量
		Map<String, Object> vars = Maps.newHashMap();
		ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);
		ErpShopInfo erpShopInfo = erpShopInfoService.findListByZhangbeiId(split.getShopId());
		vars.put("PromotionTimeDetermination", this.erpOrderFlowUserService
				.findListByFlowId(procInsId, JykFlowConstants.Planning_Expert).getUser().getId());

		if (erpShopInfo == null) {

			// 本地数据库无商户信息, 复投、微信支付、云移默认为否
			// 非复投
			vars.put("recast", 0);
			// 非微信支付
			vars.put("iswecharPay", 1);
			// 非云移(没有掌贝进件)
			vars.put("isyunnex", 1);
		} else {
			int recast = this.jykFlowContactShopService.getRecastbyProcInsId(split.getId(), erpShopInfo.getId());
			// 获取商户是否为复投商户
			vars.put("recast", recast);

			vars.put("iswecharPay", 1);
			vars.put("isyunnex", 1);
			
			List<String> shopPayQualifuList = erpShopPayQualifyService.findPayQualifyList(erpShopInfo.getId());
			// 微信支付
			if (shopPayQualifuList.contains("2")) {
				vars.put("iswecharPay", 0);
			}
			// 是否有长辈账号 ？
			if (StringUtils.isNotEmpty(erpShopInfo.getIntoPieces()) && "1".equals(erpShopInfo.getIntoPieces())) {
				vars.put("isyunnex", 0);
			}
		}

		this.jykFlowContactShopService.contactService(procInsId, channelList, taskId);
		this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId, "服务商订单-联系商户成功", vars);
		resObject.put("result", true);
		return resObject;
	}

    /**
     * 
     * 与商户对接（策划专家）
     * 
     * @param taskId
     * @param procInsId
     * @param channelList
     * @return
     * @date 2017年11月3日
     * @author yunnex
     */
    @RequestMapping(value = "planning_expert_contact_shop")
    @ResponseBody
    public JSONObject planningExpertContactShop(String taskId, String procInsId, String channelList, boolean isFinished) {
        JSONObject resObject = new JSONObject();
        workFlowService.submitSubTask(procInsId, channelList, taskId);
        if (isFinished) {
            // 提交流程变量
            Map<String, Object> vars = Maps.newHashMap();
            ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);
            ErpShopInfo erpShopInfo = erpShopInfoService.findListByZhangbeiId(split.getShopId());
            if(erpShopInfo==null){
		            vars.put("isyunnex", 0);
		            vars.put("iswecharPay", 1);
		            vars.put("isfriends", 1);
		            vars.put("isweibo", 1);
		            vars.put("ismomo", 1);
            }
            else
            {
				            List<JykOrderPromotionChannel> promotionChannelList = jykOrderPromotionChannelService.findListBySplitId(split.getId());
				            
				            //初始化 推广参数
				            erpOrderSplitInfoService.initExtendParams(vars, promotionChannelList);
				            vars.put("iswecharPay", 1);
				            vars.put("isyunnex", 1);
				            
				                List<String> shopExtensionList = erpShopExtensionQualifyService.findExtensionQualifyList(erpShopInfo.getId());
				                List<String> shopPayQualifuList = erpShopPayQualifyService.findPayQualifyList(erpShopInfo.getId());
				                for (JykOrderPromotionChannel promotionChannel : promotionChannelList) {
				                    // 查询商户是否拥有对应的商户资质
				                    if (shopExtensionList.contains(promotionChannel.getPromotionChannel())) {
				                        // 微信朋友圈
				                        if ("1".equals(promotionChannel.getPromotionChannel())) {
				                            if (shopExtensionList.contains("1")) {
				                                vars.put("isfriends", 0);
				                            }
				                            // 微博
				                        } else if ("2".equals(promotionChannel.getPromotionChannel())) {
				                            if (shopExtensionList.contains("2")) {
				                                vars.put("isweibo", 0);
				                            }
				                            // 陌陌
				                        } else if ("3".equals(promotionChannel.getPromotionChannel())) {
				                            if (shopExtensionList.contains("3")) {
				                                vars.put("ismomo", 0);
				                            }
				                        }
				                    }
				                }
				                
				                if (shopPayQualifuList.contains("2")) {
				                    vars.put("iswecharPay", 0);
				                }
				                if (StringUtils.isNotEmpty(erpShopInfo.getIntoPieces()) && "1".equals(erpShopInfo.getIntoPieces())) {
				                    vars.put("isyunnex", 0);
				                }
            }
            
            
            this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId, "服务商订单-联系商户成功", vars);
        }
        resObject.put("result", true);
        return resObject;
    }

    @RequestMapping(value = "assign_operation_manager")
    @ResponseBody
    public JSONObject assignOperationManager(String taskId, String procInsId, String operationManager) {
        JSONObject resObject = new JSONObject();
        // 保存业务流转信息
        this.jykFlowContactShopService.assignOperationManager(procInsId, operationManager, taskId);
        Map<String, Object> vars = Maps.newHashMap();

        vars.put(JykFlowConstants.OPERATION_MANAGER, operationManager);
        this.workFlowService.completeFlow(JykFlowConstants.OPERATION_MANAGER, taskId, procInsId,
                        UserUtils.getUser().getId() + "指派运营经理为:" + operationManager, vars);
        resObject.put("result", true);
        dwrUtils.dwr(operationManager);
        return resObject;
    }

    @RequestMapping(value = "assign_operation_adviser")
    @ResponseBody
    public JSONObject assignOperationAdviser(String taskId, String procInsId, String channelList, String operationAdviser, boolean isFinished) {
        JSONObject resObject = new JSONObject();
        // 保存业务流转信息
        this.jykFlowContactShopService.assignOperationAdviser(procInsId, operationAdviser, taskId, channelList);
        if (isFinished) {
            ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);
            ErpShopInfo erpShopInfo = erpShopInfoService.findListByZhangbeiId(split.getShopId());
            Map<String, Object> vars = Maps.newHashMap();
            vars.put("PromotionTimeDetermination", this.erpOrderFlowUserService.findListByFlowId(procInsId, JykFlowConstants.Planning_Expert)
                    .getUser().getId());
            if(erpShopInfo==null){
            	
            	// 本地数据库无商户信息, 复投、微信支付、云移默认为否
            	// 非复投
            	vars.put("recast", 0);
            	// 非微信支付
            	vars.put("iswecharPay", 1);
            	// 非云移(没有掌贝进件)
                vars.put("isyunnex", 1);
            }else{
            	int  recast = this.jykFlowContactShopService.getRecastbyProcInsId(split.getId(), erpShopInfo.getId());
                // 获取商户是否为复投商户
                vars.put("recast", recast);
               
                
                vars.put("iswecharPay", 1);
                vars.put("isyunnex", 1);
                
                List<String> shopPayQualifuList = erpShopPayQualifyService.findPayQualifyList(erpShopInfo.getId());
                // 微信支付
                if (shopPayQualifuList.contains("2")) {
                    vars.put("iswecharPay", 0);
                }
                //是否有掌贝账号 ？
                if (StringUtils.isNotEmpty(erpShopInfo.getIntoPieces()) &&"1".equals(erpShopInfo.getIntoPieces())) {
                    vars.put("isyunnex", 0);
                }
            }
            
            this.workFlowService.completeFlow2(new String[] {JykFlowConstants.Planning_Expert, JykFlowConstants.OPERATION_ADVISER}, taskId, procInsId,
                            UserUtils.getUser().getId() + "指派运营顾问为:" + operationAdviser, vars);
            dwrUtils.dwr(operationAdviser);
        } else {
            // 添加流程变量
            if (StringUtils.isNotBlank(operationAdviser)) {
                this.workFlowService.setVariable(taskId, JykFlowConstants.OPERATION_ADVISER, operationAdviser);
            }
        }
        resObject.put("result", true);
        return resObject;
    }

    @RequestMapping(value = "contact_recast_shop")
    @ResponseBody
    public JSONObject contactRecastShop(String taskId, String procInsId, String recastNewShopExp) {
        JSONObject resObject = new JSONObject();
        workFlowService.submitSubTask(procInsId, "1", taskId);
        ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);
        ErpOrderOriginalInfo order = orderService.get(split.getOrderId());
        // 提交流程变量
        Map<String, Object> vars = Maps.newHashMap();
        vars.put("isNewBranch", recastNewShopExp);
        vars.put("orderType", order.getOrderType());
        if (order.getOrderType() == 1) {
            this.workFlowService.completeFlow(JykFlowConstants.OPERATION_ADVISER, taskId, procInsId, "与复投商户沟通", vars);
        } else {
            this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId, "与复投商户沟通", vars);
        }
        resObject.put("result", true);
        return resObject;
    }

    @RequestMapping(value = "branch_create")
    @ResponseBody
    public JSONObject branchCreate(String taskId, String procInsId) {
        JSONObject resObject = new JSONObject();
        workFlowService.submitSubTask(procInsId, "1", taskId);
        this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId, "督促服务商协助商户在公众平台创建新门店", null);
        resObject.put("result", true);
        return resObject;
    }

    @RequestMapping(value = "branch_create_adviser")
    @ResponseBody
    public JSONObject branchCreateAdviser(String taskId, String procInsId, String channelList, boolean isFinished) {
        JSONObject resObject = new JSONObject();
        workFlowService.submitSubTask(procInsId, channelList, taskId);
        if (isFinished) {
            this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId, "确定商户在公众平台创建新门店", null);
        }
        resObject.put("result", true);
        return resObject;
    }


    /**
     * 
     * 与商户对接（运营顾问）
     * 
     * @param taskId
     * @param procInsId
     * @param channelList
     * @return
     * @date 2017年11月3日
     * @author yunnex
     */
    @RequestMapping(value = "operation_adviser_contact_shop")
    @ResponseBody
    public JSONObject operationAdviserContactShop(String taskId, String procInsId, String channelList, boolean isFinished) {
        JSONObject resObject = new JSONObject();
        // 修改子任务完成状态
        workFlowService.submitSubTask(procInsId, channelList, taskId);

        if (isFinished) {
            // 提交流程变量
            Map<String, Object> vars = Maps.newHashMap();
            ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);
            ErpShopInfo erpShopInfo = erpShopInfoService.findListByZhangbeiId(split.getShopId());
            if(erpShopInfo==null){
            	    vars.put("isyunnex", 0);
            	    vars.put("iswecharPay", 1);
		            vars.put("isfriends", 1);
		            vars.put("isweibo", 1);
		            vars.put("ismomo", 1);
            }
            else
            {
		            List<JykOrderPromotionChannel> promotionChannelList = jykOrderPromotionChannelService.findListBySplitId(split.getId());
		            
		            //初始化 推广参数
		            erpOrderSplitInfoService.initExtendParams(vars, promotionChannelList);
		            
		            vars.put("iswecharPay", 1);
		            vars.put("isyunnex", 1);
		            
		                List<String> shopExtensionList = erpShopExtensionQualifyService.findExtensionQualifyList(erpShopInfo.getId());
		                List<String> shopPayQualifuList = erpShopPayQualifyService.findPayQualifyList(erpShopInfo.getId());
		       
		                for (JykOrderPromotionChannel promotionChannel : promotionChannelList) {
		                    // 查询商户是否拥有对应的商户资质
		                    if (shopExtensionList.contains(promotionChannel.getPromotionChannel())) {
		                        // 微信朋友圈
		                        if ("1".equals(promotionChannel.getPromotionChannel())) {
		                            if (shopExtensionList.contains("1")) {
		                                vars.put("isfriends", 0);
		                            }
		                            // 微博
		                        } else if ("2".equals(promotionChannel.getPromotionChannel())) {
		                            if (shopExtensionList.contains("2")) {
		                                vars.put("isweibo", 0);
		                            }
		                            // 陌陌
		                        } else if ("3".equals(promotionChannel.getPromotionChannel())) {
		                            if (shopExtensionList.contains("3")) {
		                                vars.put("ismomo", 0);
		                            }
		                        }
		                    }
		                }
		                
		                if (shopPayQualifuList.contains("2")) {
		                    vars.put("iswecharPay", 0);
		                }
		                if (StringUtils.isNotEmpty(erpShopInfo.getIntoPieces()) &&"1".equals(erpShopInfo.getIntoPieces())) {
		                    vars.put("isyunnex", 0);
		                }
            }
            
            
            this.workFlowService.completeFlow(JykFlowConstants.OPERATION_ADVISER, taskId, procInsId, "直销订单-联系商户成功", vars);
        }
        resObject.put("result", true);
        return resObject;
    }


    @RequestMapping(value = "confirm_yunnex_info_pieces")
    @ResponseBody
    public JSONObject confirmYunnexInfoPieces(String taskId, String procInsId, String channelList, boolean isFinished) {
        JSONObject resObject = new JSONObject();
        ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);
        boolean flag=this.erpShopInfoApiService.isShopInputPieces(split.getShopId());
        
        if(!flag){
            resObject.put("result", false);
            resObject.put("message", "无法从地推系统中同步此商户数据，请确认是否进件成功，手机号:"+split.getShopId());
            return resObject;
        }

        workFlowService.submitSubTask(procInsId, channelList, taskId);
       
        if (isFinished) {
            this.workFlowService.completeFlow2(new String[] {JykFlowConstants.Planning_Expert, JykFlowConstants.OPERATION_ADVISER}, taskId, procInsId, "掌贝进件", null);
        }
        resObject.put("result", true);
        return resObject;
    }
    
    

    @RequestMapping(value = "channel_confirm_business_qualification")
    @ResponseBody
    public JSONObject channelConfirmBusinessQualification(String taskId, String procInsId, String channelList, String nextQualificationTime,String reviewResult) {
     
        this.workFlowService.submitSubTask(procInsId, channelList, taskId);
        JSONObject resObject = new JSONObject();
        ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);
        String pendingProdFlag = split.getPendingProdFlag();
        TaskExt taskExt = new TaskExt();
        taskExt.setTaskId(taskId);
        boolean isPass = "yes".equals(reviewResult);
        // 下次推广时间 ,如果下次推广时间不为空，则审核不通过，否则审核通过  
        if (!isPass) 
        {
            split.setNextQualificationTime(DateUtils.parseDate(nextQualificationTime));
            this.workFlowService.setVariable(taskId, "nextQualificationTime", DateUtils.formatDateTime(DateUtils.parseDate(nextQualificationTime)));
            split.setPendingReason("Q");
            split.setTimeoutFlag(Constant.NO);
            split.setPendingProduced(Constant.YES);
            split.setPendingProdFlag(Constant.YES);
            taskExt.setPendingProdFlag(Constant.YES);
        }
        else
        {
            Map<String, Object> vars = Maps.newHashMap();
            this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId, "推广时间确认", vars);
            split.setPendingProdFlag(Constant.NO);
            taskExt.setPendingProdFlag(Constant.NO);
        }
        // 手动从待生产库激活
        if (Constant.YES.equals(pendingProdFlag) && Constant.NO.equals(split.getPendingProdFlag())) {
            split.setActivationTime(new Date());
        }
        this.erpOrderSplitInfoService.save(split);
        taskExtService.updateTaskState(taskExt);
        
        resObject.put("result", true);
        return resObject;
    
    }
    
    
    
    @RequestMapping(value = "channel_confirm_business_license")
    @ResponseBody
    public JSONObject channelConfirmBusinessLicense(String taskId, String procInsId, String channelList, String nextLicenseTime,String reviewResult) {
     
        this.workFlowService.submitSubTask(procInsId, channelList, taskId);
        JSONObject resObject = new JSONObject();
        ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);
        String pendingProdFlag = split.getPendingProdFlag();
        TaskExt taskExt = new TaskExt();
        taskExt.setTaskId(taskId);
        boolean isPass = "yes".equals(reviewResult);
        // 下次推广时间 ,如果下次推广时间不为空，则审核不通过，否则审核通过  
        if (!isPass) 
        {
            split.setNextLicenseTime(DateUtils.parseDate(nextLicenseTime));
            this.workFlowService.setVariable(taskId, "nextLicenseTime", DateUtils.formatDateTime(DateUtils.parseDate(nextLicenseTime)));
            split.setPendingProduced(Constant.YES);
            split.setPendingReason("Q");
            split.setTimeoutFlag(Constant.NO);
            split.setPendingProdFlag(Constant.YES);
            taskExt.setPendingProdFlag(Constant.YES);
        }
        else
        {
        	 Map<String, Object> vars = Maps.newHashMap();
             this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId, "推广时间确认", vars);
            split.setPendingProdFlag(Constant.NO);
            taskExt.setPendingProdFlag(Constant.NO);
        }
        // 手动从待生产库激活
        if (Constant.YES.equals(pendingProdFlag) && Constant.NO.equals(split.getPendingProdFlag())) {
            split.setActivationTime(new Date());
        }
        this.erpOrderSplitInfoService.save(split);
        taskExtService.updateTaskState(taskExt);
        
        resObject.put("result", true);
        return resObject;
    
    }
    
    
    
    @RequestMapping(value = "sales_confirm_business_qualification")
    @ResponseBody
    public JSONObject salesConfirmBusinessQualification(String taskId, String procInsId, String channelList, boolean isFinished) {
        JSONObject resObject = new JSONObject();
        
        // 修改子任务完成状态
        workFlowService.submitSubTask(procInsId, channelList, taskId);
        if (isFinished) {
        	this.workFlowService.completeFlow(JykFlowConstants.OPERATION_ADVISER, taskId, procInsId, "微信支付进件", null);
        }
        resObject.put("result", true);
        return resObject;
    }
    
    @RequestMapping(value = "sales_confirm_business_license")
    @ResponseBody
    public JSONObject salesConfirmBusinessLicense(String taskId, String procInsId, String channelList, boolean isFinished) {
        JSONObject resObject = new JSONObject();
        
        // 修改子任务完成状态
        workFlowService.submitSubTask(procInsId, channelList, taskId);
        if (isFinished) {
        	this.workFlowService.completeFlow(JykFlowConstants.OPERATION_ADVISER, taskId, procInsId, "微信支付进件", null);
        }
        resObject.put("result", true);
        return resObject;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    @RequestMapping(value = "confirm_weChat_pay_into_pieces")
    @ResponseBody
    public JSONObject confirmWeChatPayIntoPieces(String taskId, String procInsId, String channelList, boolean isFinished) {
        JSONObject resObject = new JSONObject();
        
        
        workFlowService.submitSubTask(procInsId, channelList, taskId);
        if (isFinished) {
            this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId, "微信支付进件", null);
        }
        resObject.put("result", true);
        return resObject;
    }
    
    

    @RequestMapping(value = "confirm_submit_friends_account_info")
    @ResponseBody
    public JSONObject confirmSubmitFriendsAccountInfo(String taskId, String procInsId, String channelList, boolean isFinished) {
        JSONObject resObject = new JSONObject();
        
        
        workFlowService.submitSubTask(procInsId, channelList, taskId);
        if (isFinished) {
            this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId, "协助完成朋友圈推广开户", null);
        }
        resObject.put("result", true);
        return resObject;
    }

    @RequestMapping(value = "confirm_submit_weibo_account_info")
    @ResponseBody
    public JSONObject confirmSubmitWeiboAccountInfo(String taskId, String procInsId, String channelList, boolean isFinished) {
        JSONObject resObject = new JSONObject();
        
        
        workFlowService.submitSubTask(procInsId, channelList, taskId);
        if (isFinished) {
            this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId, "提交微博推广开户资料", null);
        }
        resObject.put("result", true);
        return resObject;
    }

    @RequestMapping(value = "confirm_submit_momo_account_info")
    @ResponseBody
    public JSONObject confirmSubmitMomoAccountInfo(String taskId, String procInsId, String channelList, boolean isFinished) {
        JSONObject resObject = new JSONObject();
        
        
        // 修改子任务完成状态
        workFlowService.submitSubTask(procInsId, channelList, taskId);
        if (isFinished) {
            this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId, "提交陌陌推广开户资料", null);
        }
        resObject.put("result", true);
        return resObject;
    }

    @RequestMapping(value = "confirm_upload_picture_material")
    @ResponseBody
    public JSONObject confirmUploadPictureMaterial(String taskId, String procInsId, String channelList) {
        JSONObject resObject = new JSONObject();
        
        
        // 保存业务流转信息
        this.workFlowService.submitSubTask(procInsId, channelList, taskId);
        this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId, "提交推广图片素材", null);
        resObject.put("result", true);
        return resObject;
    }

    @RequestMapping(value = "apply_zhixiao_yunnex_info_pieces")
    @ResponseBody
    public JSONObject applyZhixiaoYunnexInfoPieces(String taskId, String procInsId, String channelList, boolean isFinished) {
        JSONObject resObject = new JSONObject();
        // 保存业务流转信息
        this.workFlowService.submitSubTask(procInsId, channelList, taskId);
        if (isFinished) {
            // 更新商户进件状态为已进件
            ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);
            ErpShopInfo erpShopInfo = erpShopInfoService.findListByZhangbeiId(split.getShopId());
    
            if(null!=erpShopInfo)
            {
            	 this.erpShopInfoService.updateIntoPiecesById(erpShopInfo.getId());
            }
           
            this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId, UserUtils.getUser().getId() + "运营顾问为:" + UserUtils
                            .getUser().getId() + "掌贝进件成功", null);
        }
        resObject.put("result", true);
        return resObject;
    }

    @RequestMapping(value = "apply_zhixiao_weChat_pay_into_pieces")
    @ResponseBody
    public JSONObject applyZhixiaoWeChatPayIntoPieces(String taskId, String procInsId, String channelList) {
        JSONObject resObject = new JSONObject();
        // 保存业务流转信息
        this.workFlowService.submitSubTask(procInsId, channelList, taskId);
        // 更新商户进件状态为已进件
        ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);
      
        ErpShopInfo erpShopInfo = erpShopInfoService.findListByZhangbeiId(split.getShopId());
    
        if (erpShopInfo != null) {
            ErpShopPayQualify payQualify = new ErpShopPayQualify();
            payQualify.setShopId(erpShopInfo.getId());
            payQualify.setPayValue("2");
            this.erpShopPayQualifyService.save(payQualify);
        }
        this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId, UserUtils.getUser().getId() + "运营顾问为:" + UserUtils
                        .getUser().getId() + "微信支付进件成功", null);
        resObject.put("result", true);
        return resObject;
    }

    @RequestMapping(value = "apply_zhixiao_submit_friends_account_info")
    @ResponseBody
    public JSONObject applyZhixiaoSubmitFriendsAccountInfo(String taskId, String procInsId, String channelList, boolean isFinished) {
        JSONObject resObject = new JSONObject();
        // 保存业务流转信息
        this.workFlowService.submitSubTask(procInsId, channelList, taskId);
        if (isFinished) {
            // 更新商户进件状态为已进件
            ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);

       
            ErpShopInfo erpShopInfo = erpShopInfoService.findListByZhangbeiId(split.getShopId());
    
            if(null!=erpShopInfo)
            {
            	  ErpShopExtensionQualify extensionQualify = new ErpShopExtensionQualify();
                  extensionQualify.setShopId(erpShopInfo.getId());
                  extensionQualify.setExtensionValue("1");
                  this.erpShopExtensionQualifyService.save(extensionQualify);
            }

            this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId, UserUtils.getUser().getId() + "运营顾问为:" + UserUtils
                            .getUser().getId() + "支付进件成功", null);
        }
        resObject.put("result", true);
        return resObject;
    }

    @RequestMapping(value = "apply_zhixiao_submit_weibo_account_info")
    @ResponseBody
    public JSONObject applyZhixiaoSubmitWeiboAccountInfo(String taskId, String procInsId, String channelList, boolean isFinished) {
        JSONObject resObject = new JSONObject();
        // 保存业务流转信息
        this.workFlowService.submitSubTask(procInsId, channelList, taskId);
        if (isFinished) {
            // 更新商户进件状态为已进件
            ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);
        
            ErpShopInfo erpShopInfo = erpShopInfoService.findListByZhangbeiId(split.getShopId());
        
            if (erpShopInfo != null) {
                ErpShopExtensionQualify extensionQualify = new ErpShopExtensionQualify();
                extensionQualify.setShopId(erpShopInfo.getId());
                extensionQualify.setExtensionValue("2");
                this.erpShopExtensionQualifyService.save(extensionQualify);
            }
            this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId, UserUtils.getUser().getId() + "运营顾问为:" + UserUtils
                            .getUser().getId() + "微博进件成功", null);
        }
        resObject.put("result", true);
        return resObject;
    }

    @RequestMapping(value = "apply_zhixiao_submit_momo_account_info")
    @ResponseBody
    public JSONObject applyZhixiaoSubmitMomoAccountInfo(String taskId, String procInsId, String channelList, boolean isFinished) {
        JSONObject resObject = new JSONObject();
        // 保存业务流转信息
        this.workFlowService.submitSubTask(procInsId, channelList, taskId);
        if (isFinished) {
            // 更新商户进件状态为已进件
            ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);

    
            ErpShopInfo erpShopInfo = erpShopInfoService.findListByZhangbeiId(split.getShopId());
          
            if(null!=erpShopInfo)
            {
            	  ErpShopExtensionQualify extensionQualify = new ErpShopExtensionQualify();
                  extensionQualify.setShopId(erpShopInfo.getId());
                  extensionQualify.setExtensionValue("3");
                  this.erpShopExtensionQualifyService.save(extensionQualify);
            }
            this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId, UserUtils.getUser().getId() + "运营顾问为:" + UserUtils
                            .getUser().getId() + "陌陌进件成功", null);
        }
        resObject.put("result", true);
        return resObject;
    }

    @RequestMapping(value = "apply_zhixiao_upload_picture_material")
    @ResponseBody
    public JSONObject applyServiceUploadPictureMaterial(String taskId, String procInsId, String orderFileId, String orderFileName) {
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
