package com.yunnex.ops.erp.modules.workflow.flow.web.jyk;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.utils.DateUtils;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.act.entity.TaskExt;
import com.yunnex.ops.erp.modules.act.service.TaskExtService;
import com.yunnex.ops.erp.modules.holiday.service.ErpHolidaysService;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitGood;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitInfo;
import com.yunnex.ops.erp.modules.order.service.ErpOrderOriginalGoodService;
import com.yunnex.ops.erp.modules.order.service.ErpOrderOriginalInfoService;
import com.yunnex.ops.erp.modules.order.service.ErpOrderSplitInfoService;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStorePromotePhotoMaterial;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreInfoService;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStorePromotePhotoMaterialService;
import com.yunnex.ops.erp.modules.sys.utils.DwrUtils;
import com.yunnex.ops.erp.modules.workflow.flow.common.JykFlowConstants;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderFile;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderInputDetail;
import com.yunnex.ops.erp.modules.workflow.flow.service.ErpHisSplitServiceApi;
import com.yunnex.ops.erp.modules.workflow.flow.service.ErpOrderFileService;
import com.yunnex.ops.erp.modules.workflow.flow.service.ErpOrderInputDetailService;
import com.yunnex.ops.erp.modules.workflow.flow.service.ErpOrderSubTaskService;
import com.yunnex.ops.erp.modules.workflow.flow.service.WorkFlowMonitorService;
import com.yunnex.ops.erp.modules.workflow.flow.service.WorkFlowService;
import com.yunnex.ops.erp.modules.workflow.store.service.JykOrderChoiceStoreService;
import com.yunnex.ops.erp.modules.workflow.user.entity.ErpOrderFlowUser;
import com.yunnex.ops.erp.modules.workflow.user.service.ErpOrderFlowUserService;

/**
 * 聚引客联系商户前段控制Controller
 * 
 * @author yunnex
 * @date 2017年11月2日
 */
@Controller
@RequestMapping(value = "${adminPath}/jyk/flow")
public class JykFlowProductController extends BaseController {
    /** 工作流服务 */
    @Autowired
    private WorkFlowService workFlowService;
    /** 订单分单信息表 */
    @Autowired
    private ErpOrderSplitInfoService erpOrderSplitInfoService;
    @Autowired
    private ErpOrderOriginalInfoService erpOrderOriginalInfoService;
    @Autowired
    private ErpOrderFlowUserService erpOrderFlowUserService;
    @Autowired
    private ErpOrderInputDetailService inputDetailService;
    @Autowired
    private ErpOrderFileService erpOrderFileService;
    @Autowired
    private ErpOrderOriginalGoodService goodService;
    @Autowired
    private ErpOrderSubTaskService erpOrderSubTaskService;
    
    @Autowired
    private JykOrderChoiceStoreService jykOrderChoiceStoreService;
    @Autowired
    private ErpStorePromotePhotoMaterialService erpStorePromotePhotoMaterialService;
    
    @Autowired
    private ErpStoreInfoService erpStoreInfoService;
    @Autowired
    private ErpHisSplitServiceApi erpHisSplitServiceApi;
    @Autowired
    private ErpHolidaysService erpHolidaysService;
    @Autowired
    private WorkFlowMonitorService workFlowMonitorService;


    @Autowired
    private DwrUtils dwrUtils;
    @Autowired
    private TaskExtService taskExtService;

    @RequestMapping(value = "promotion_time_determination")
    @ResponseBody
    public JSONObject promotionTimeDetermination(String taskId, String procInsId, String channelList, String promotionTime, String promotionNextTime) {
        this.workFlowService.submitSubTask(procInsId, channelList, taskId);
        JSONObject resObject = new JSONObject();
        ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);
        String pendingProdFlag = split.getPendingProdFlag();
        TaskExt taskExt = new TaskExt();
        taskExt.setPendingProdFlag(Constant.NO);
        taskExt.setTaskId(taskId);

        // 下次推广时间
        if (StringUtils.isNotBlank(promotionNextTime)) {
            split.setNextContactTime(DateUtils.parseDate(promotionNextTime));
            split.setPendingProduced(Constant.YES);
            split.setPendingReason("D");
            split.setTimeoutFlag(Constant.NO);
            split.setPendingProdFlag(Constant.YES);
            taskExt.setPendingProdFlag(Constant.YES);
            this.workFlowService.setVariable(taskId, "inPendingReason", "NCT");
            this.workFlowService.setVariable(taskId, "promotionNextTime", DateUtils.formatDateTime(DateUtils.parseDate(promotionNextTime)));
        }
        // 推广时间
        if (StringUtils.isNotBlank(promotionTime)) {
            // 超过20工作日要进入待生产库
            Double distanceDays = 0D;
            try {
                Date parseDate = DateUtils.parseDate(promotionTime);
                distanceDays = DateUtils.getDistanceOfTwoDate(
                                this.erpHolidaysService.enddate(new Date(), JykFlowConstants.PLANNING_DATE_DISTINCT * 8), parseDate);
            } catch (ParseException e) {
                logger.info("日期解析出错！", e);
            }
            if (distanceDays.intValue() > 0) {
                split.setPromotionTime(DateUtils.parseDate(promotionTime));
                split.setPendingProduced(Constant.YES);
                split.setPendingReason("D");
                split.setTimeoutFlag(Constant.NO);
                split.setPendingProdFlag(Constant.YES);
                taskExt.setPendingProdFlag(Constant.YES);
                this.workFlowService.setVariable(taskId, "inPendingReason", "PT");
                this.workFlowService.setVariable(taskId, "promotionTime", DateUtils.formatDateTime(DateUtils.parseDate(promotionTime)));
            } else {
                Map<String, Object> vars = Maps.newHashMap();
                vars.put("isStarted", "1");
                split.setPendingProdFlag(Constant.NO);
                taskExt.setPendingProdFlag(Constant.NO);
                this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId, "推广时间确认", vars);
            }
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

    @RequestMapping(value = "work_management_diagnosis")
    @ResponseBody
    public JSONObject workManagementDiagnosis(String taskId, String procInsId, String channelList, boolean isFinished) {
        JSONObject resObject = new JSONObject();
        // 保存业务流转信息
        this.workFlowService.submitSubTask(procInsId, channelList, taskId);
        if (isFinished) {
            this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId, "经营诊断与方案策划", null);
        }
        resObject.put("result", true);
        return resObject;
    }

    @RequestMapping(value = "work_promotion_scheme_planning")
    @ResponseBody
    public JSONObject workPromotionSchemePlanning(String taskId, String procInsId, String channelList, String orderFileId, String orderFileName,
                    String assignTextDesignInterfacePerson, String assignConsultantInterface, boolean isFinished) {
        JSONObject resObject = new JSONObject();

        if (StringUtils.isNotBlank(channelList)) {
            // 保存业务流转信息
            this.workFlowService.submitSubTask(procInsId, channelList, taskId);
        }
        ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);
        if (StringUtils.isNotBlank(assignTextDesignInterfacePerson)) {
            erpOrderSubTaskService.updateState(taskId, "3");
            // 插入订单流程信息表
            erpOrderFlowUserService.insertOrderFlowUser(assignTextDesignInterfacePerson, split.getOrderId(), split.getId(),
                            JykFlowConstants.assignTextDesignInterfacePerson, procInsId);
            this.workFlowService.setVariable(taskId, "textDesignInterfacePerson", assignTextDesignInterfacePerson);
        }
        if (StringUtils.isNotBlank(assignConsultantInterface)) {
            erpOrderSubTaskService.updateState(taskId, "4");
            // 插入订单流程信息表
            erpOrderFlowUserService.insertOrderFlowUser(assignConsultantInterface, split.getOrderId(), split.getId(),
                            JykFlowConstants.assignConsultantInterface, procInsId);
            this.workFlowService.setVariable(taskId, "consultantInterface", assignConsultantInterface);
        }
        // 添加流程变量
        if (StringUtils.isNotBlank(orderFileId)) {
            erpOrderSubTaskService.updateState(taskId, "5");
            
            String[] fileIds = orderFileId.split(",");
            for(String fileId :fileIds){
                // 更新当前的文件为正式文件
                ErpOrderFile file = this.erpOrderFileService.get(fileId);
                file.setDelFlag("0");
                this.erpOrderFileService.save(file);
            }
            this.workFlowService.setVariable(taskId, "workPromotionSchemePlanningFile", orderFileName);
        }
        if (isFinished) {
            Map<String, Object> vars = Maps.newHashMap();
            String textDesignInterfacePerson = this.erpOrderFlowUserService
                            .findListByFlowId(procInsId, JykFlowConstants.assignTextDesignInterfacePerson).getUser().getId();
            String consultantInterface = this.erpOrderFlowUserService.findListByFlowId(procInsId, JykFlowConstants.assignConsultantInterface)
                            .getUser().getId();

            vars.put("textDesignInterfacePerson", textDesignInterfacePerson);
            vars.put("consultantInterface", consultantInterface);
            this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId, "经营诊断与方案策划", vars);

            dwrUtils.dwr(textDesignInterfacePerson);
            dwrUtils.dwr(consultantInterface);

        }
        resObject.put("result", true);
        return resObject;
    }
    
    @RequestMapping(value = "work_promotion_scheme_planning_new")
    @ResponseBody
    public JSONObject workPromotionSchemePlanningNew(String taskId, String procInsId, String channelList, 
                     String orderFileId, String orderFileName, boolean isFinished) {
        JSONObject resObject = new JSONObject();

        if (StringUtils.isNotBlank(channelList)) {
            // 保存业务流转信息
            this.workFlowService.submitSubTask(procInsId, channelList, taskId);
        }
        ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);
        // 选择的门店
        String storeId = jykOrderChoiceStoreService.getStoreIdBySplitId(split.getId());
        
        // 添加流程变量
        if (StringUtils.isNotBlank(orderFileId)) {
            erpOrderSubTaskService.updateState(taskId, "5");
            
            String[] fileIds = orderFileId.split(",");
            for(String fileId :fileIds){
                // 更新当前的文件为正式文件
                ErpOrderFile file = this.erpOrderFileService.get(fileId);
                file.setDelFlag("0");
                this.erpOrderFileService.save(file);
            }
            this.workFlowService.setVariable(taskId, "workPromotionSchemePlanningFile", orderFileName);
        }
        
        // 判断是否上传过推广素材
        ErpStorePromotePhotoMaterial promotePhotoMaterial = erpStorePromotePhotoMaterialService.findlistWhereStoreId("0", storeId);
        if(promotePhotoMaterial!=null){
            this.workFlowService.setVariable(taskId, "UploadPictureMaterial", promotePhotoMaterial.getStoreInfoId());
        }
        
        if (isFinished) {
            Map<String, Object> vars = Maps.newHashMap();
            String textDesignInterfacePerson = this.erpOrderFlowUserService
                            .findListByFlowId(procInsId, JykFlowConstants.assignTextDesignInterfacePerson).getUser().getId();
            String consultantInterface = this.erpOrderFlowUserService.findListByFlowId(procInsId, JykFlowConstants.assignConsultantInterface)
                            .getUser().getId();

            vars.put("textDesignInterfacePerson", textDesignInterfacePerson);
            vars.put("consultantInterface", consultantInterface);
            this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId, "经营诊断与方案策划", vars);

            dwrUtils.dwr(textDesignInterfacePerson);
            dwrUtils.dwr(consultantInterface);

        }
        resObject.put("result", true);
        return resObject;
    }

    @RequestMapping(value = "work_assignment_consultant")
    @ResponseBody
    public JSONObject workAssignmentConsultant(String taskId, String procInsId, String assignConsultant) {
        JSONObject resObject = new JSONObject();

        ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);
        if (StringUtils.isNotBlank(assignConsultant)) {
            // 保存业务流转信息
            this.workFlowService.submitSubTask(procInsId, "1,", taskId);
            // 插入订单流程信息表
            erpOrderFlowUserService.insertOrderFlowUser(assignConsultant, split.getOrderId(), split.getId(), JykFlowConstants.assignConsultant,
                            procInsId);
        }
        this.workFlowService.completeFlow(JykFlowConstants.assignConsultant, taskId, procInsId, "经营诊断与方案策划", null);
        String userId = this.erpOrderFlowUserService.findListByFlowId(procInsId, JykFlowConstants.assignConsultant).getUser().getId();
        dwrUtils.dwr(userId);
        resObject.put("result", true);
        return resObject;
    }

    @RequestMapping(value = "work_assign_planning_designer")
    @ResponseBody
    public JSONObject workAssignPlanningDesigner(String taskId, String procInsId, String textDesignPerson, String designer, boolean isFinished) {
        JSONObject resObject = new JSONObject();
        ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);
        if (StringUtils.isNotBlank(textDesignPerson)) {
            // 保存业务流转信息
            this.workFlowService.submitSubTask(procInsId, "1,", taskId);
            // 插入订单流程信息表
            erpOrderFlowUserService.insertOrderFlowUser(textDesignPerson, split.getOrderId(), split.getId(), JykFlowConstants.assignTextDesignPerson,
                            procInsId);
            this.workFlowService.setVariable(taskId, "textDesignPerson", textDesignPerson);
        }
        if (StringUtils.isNotBlank(designer)) {
            // 保存业务流转信息
            this.workFlowService.submitSubTask(procInsId, "2,", taskId);
            // 插入订单流程信息表
            erpOrderFlowUserService.insertOrderFlowUser(designer, split.getOrderId(), split.getId(), JykFlowConstants.designer, procInsId);

            this.workFlowService.setVariable(taskId, "designer", designer);
        }
        if (isFinished) {
            Map<String, Object> vars = Maps.newHashMap();
            String assignTextDesignPerson = this.erpOrderFlowUserService.findListByFlowId(procInsId, JykFlowConstants.assignTextDesignPerson)
                            .getUser().getId();
            String designerName = this.erpOrderFlowUserService.findListByFlowId(procInsId, JykFlowConstants.designer).getUser().getId();
            vars.put("textDesignPerson", assignTextDesignPerson);
            vars.put("designer", designerName);
            vars.put("isAll", "1");
            this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId, "指派方案与设计师", vars);
            dwrUtils.dwr(assignTextDesignPerson);
            dwrUtils.dwr(designerName);

            // 指派文案策划
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String storeId = jykOrderChoiceStoreService.getStoreIdBySplitId(split.getId());
            ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
            erpHisSplitServiceApi.creativity(split.getId(), sdf.format(new Date(System.currentTimeMillis())), storeInfo.getId(), storeInfo.getShortName());
        }
        resObject.put("result", true);
        return resObject;
    }

    @RequestMapping(value = "work_release_scheme_output")
    @ResponseBody
    public JSONObject workPromotionSchemePlanning(String taskId, String procInsId, String orderFileId, String orderFileName) {
        JSONObject resObject = new JSONObject();
        // 再次提交
        if (StringUtils.isNotBlank(orderFileId)) {
            
            String[] fileIds = orderFileId.split(",");
            for(String fileId : fileIds){
                // 更新当前的文件为正式文件
                ErpOrderFile file = this.erpOrderFileService.get(fileId);
                file.setDelFlag("0");
                this.erpOrderFileService.save(file);
            }
            // 保存业务流转信息
            this.workFlowService.submitSubTask(procInsId, "1,", taskId);
            this.workFlowService.completeFlow(JykFlowConstants.assignConsultantInterface, taskId, procInsId, "投放方案输出", null);
            String userId = this.erpOrderFlowUserService.findListByFlowId(procInsId, JykFlowConstants.assignConsultantInterface).getUser().getId();
            dwrUtils.dwr(userId);
        }
        resObject.put("result", true);
        return resObject;
    }

    @RequestMapping(value = "work_output_official_documents")
    @ResponseBody
    public JSONObject workOutputOfficialDocuments(String taskId, String procInsId, String orderFileId, String orderFileName) {
        JSONObject resObject = new JSONObject();
        // 保存业务流转信息
        this.workFlowService.submitSubTask(procInsId, "1,", taskId);
        Task task=this.workFlowService.getTaskById(taskId);
        if(!task.getProcessVariables().containsKey("UploadPictureMaterial")){
            resObject.put("message", "未上传图片推广素材，无法提交 当前任务");
            resObject.put("result", false);
            return resObject;
        }
        
        if (StringUtils.isNotBlank(orderFileId)) {
            // 将同类型文件变更为删除状态
            ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);
            this.erpOrderFileService.deleteByProcInsId("商户推广文案", split.getProcInsId());
            
            // 获取多文件对象
            String[] orderFileIds = orderFileId.split(",");
            for (String id : orderFileIds) {
                // 更新当前的文件为正式文件
                ErpOrderFile file = this.erpOrderFileService.get(id);
                file.setDelFlag("0");
                this.erpOrderFileService.save(file);
            }
            /*
             * //将当前生效的文件更新为未删除 ErpOrderFile file = this.erpOrderFileService.get(orderFileId);
             * file.setDelFlag("0"); this.erpOrderFileService.save(file);
             */
            this.workFlowService.completeFlow(JykFlowConstants.assignTextDesignPerson, taskId, procInsId, "商户推广方案", null);
            String userId = this.erpOrderFlowUserService.findListByFlowId(procInsId, JykFlowConstants.assignTextDesignPerson).getUser().getId();
            dwrUtils.dwr(userId);
            
            // 上传推广方案
            String storeId = jykOrderChoiceStoreService.getStoreIdBySplitId(split.getId());
            ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
            erpHisSplitServiceApi.uploadExtension(split.getId(), storeInfo.getId(), storeInfo.getShortName());
        }
        resObject.put("result", true);
        return resObject;
    }

    @RequestMapping(value = "work_output_design_draft")
    @ResponseBody
    public JSONObject workOutputDesignDraft(String taskId, String procInsId, String outputLink, String orderFileId, String orderFileName,
                    boolean isFinished) {
        JSONObject resObject = new JSONObject();
        
        Task task=this.workFlowService.getTaskById(taskId);
        if(!task.getProcessVariables().containsKey("UploadPictureMaterial")){
            resObject.put("message", "未上传图片推广素材，无法提交 当前任务");
            resObject.put("result", false);
            return resObject;
        }
        // 保存业务流转信息
        this.workFlowService.submitSubTask(procInsId, "1,", taskId);
        ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);
        if (StringUtils.isNotBlank(outputLink)) {
            ErpOrderInputDetail erpOrderInputDetail = this.inputDetailService.getBySplitId(split.getId(), "陌陌落地页链接");
            if(erpOrderInputDetail==null){
                erpOrderInputDetail= new ErpOrderInputDetail();
            }
            erpOrderInputDetail.setInputDetail(outputLink);
            erpOrderInputDetail.setInputTaskName("陌陌落地页链接");
            erpOrderInputDetail.setSplitId(split.getId());
            inputDetailService.save(erpOrderInputDetail);
            this.workFlowService.setVariable(taskId, "momoDetail", outputLink);
            this.workFlowService.submitSubTask(procInsId, "1,", taskId);
        }

        if (StringUtils.isNotBlank(orderFileId)) {
            this.erpOrderFileService.deleteByProcInsId("输出设计稿", split.getProcInsId());

            // 获取多文件对象
            String[] orderFileIds = orderFileId.split(",");
            for (String id : orderFileIds) {
                // 更新当前的文件为正式文件
                ErpOrderFile file = this.erpOrderFileService.get(id);
                file.setDelFlag("0");
                this.erpOrderFileService.save(file);
            }
            /*
             * modify by SunQ 2017-11-24 14:47:21 modify reason：之前为单文件上传方式，现改为多文件上传 ErpOrderFile
             * file = this.erpOrderFileService.get(orderFileId); file.setDelFlag("0");
             * this.erpOrderFileService.save(file);
             */
            this.workFlowService.setVariable(taskId, "workOutputFesignFraft", orderFileName);
            this.workFlowService.submitSubTask(procInsId, "2,", taskId);
        }
        if (isFinished) {
            this.workFlowService.completeFlow(JykFlowConstants.assignTextDesignPerson, taskId, procInsId, "输出设计稿", null);
            String userId = this.erpOrderFlowUserService.findListByFlowId(procInsId, JykFlowConstants.assignTextDesignPerson).getUser().getId();
            dwrUtils.dwr(userId);
        }
        resObject.put("result", true);
        return resObject;
    }

    @RequestMapping(value = "work_output_card_coupon")
    @ResponseBody
    public JSONObject workOutputCardCoupon(String taskId, String procInsId, String channelList, String wechatCardLink, String mobileCardLink, boolean isFinished) {
        JSONObject resObject = new JSONObject();
        
        // 验证推广图片素材是否上传
        
        /*
         * Task task=this.workFlowService.getTaskById(taskId);
         * 
         * if(!task.getProcessVariables().containsKey("UploadPictureMaterial")){
         * resObject.put("message", "未上传图片推广素材，无法提交 当前任务"); resObject.put("result", false); return
         * resObject; }
         */
        ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);
        // 保存业务流转信息
        this.workFlowService.submitSubTask(procInsId, channelList, taskId);
        if (StringUtils.isNotBlank(wechatCardLink)) {
            ErpOrderInputDetail erpOrderInputDetail = new ErpOrderInputDetail();
            erpOrderInputDetail.setInputDetail(wechatCardLink);
            erpOrderInputDetail.setInputTaskName("微信领券链接");
            erpOrderInputDetail.setSplitId(split.getId());
            inputDetailService.save(erpOrderInputDetail);
            this.workFlowService.setVariable(taskId, "wechatCardLink", wechatCardLink);
            // 保存业务流转信息
            erpOrderSubTaskService.updateState(taskId, "2");
        }
        if (StringUtils.isNotBlank(mobileCardLink)) {
            ErpOrderInputDetail erpOrderInputDetail = new ErpOrderInputDetail();
            erpOrderInputDetail.setInputDetail(mobileCardLink);
            erpOrderInputDetail.setInputTaskName("手机领券链接");
            erpOrderInputDetail.setSplitId(split.getId());
            inputDetailService.save(erpOrderInputDetail);
            this.workFlowService.setVariable(taskId, "mobileCardLink", mobileCardLink);
            // 保存业务流转信息
            erpOrderSubTaskService.updateState(taskId, "2");
        }
        if (isFinished) {
            
            // 兼容老流程和新流程 （3.0之前老流程叫 PlanningExpert，3.0叫做AssignConsultant ）
            Map<String, Object> vars = Maps.newHashMap();
            Map<String, String> userMap = Maps.newHashMap();
            userMap.put(JykFlowConstants.assignConsultant, JykFlowConstants.assignConsultant);
            userMap.put("TaskUser", JykFlowConstants.Planning_Expert);
            this.workFlowService.completeFlowCompatibility(userMap, taskId, procInsId, "输出卡券", vars);
        }
        resObject.put("result", true);
        return resObject;
    }

    @RequestMapping(value = "work_release_plan_audit")
    @ResponseBody
    public JSONObject workReleasePlanAudit(String taskId, String procInsId, String channelList, String planAuditNot) {
        JSONObject resObject = new JSONObject();
        // 审核通过
        if (StringUtils.isNotBlank(channelList) && "1".equals(channelList)) {
            Map<String, Object> vars = Maps.newHashMap();
            vars.put("workReleasePlanAudit", "1");
            // 保存业务流转信息
            this.workFlowService.submitSubTask(procInsId, "1,", taskId);
            this.workFlowService.completeFlow(JykFlowConstants.assignConsultant, taskId, procInsId, "投放充值", vars);
            String userId = this.erpOrderFlowUserService.findListByFlowId(procInsId, JykFlowConstants.Planning_Expert).getUser().getId();
            dwrUtils.dwr(userId);
        } else {
            Map<String, Object> vars = Maps.newHashMap();
            vars.put("workReleasePlanAudit", "0");
            ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);
            Task task = this.workFlowService.getTaskById(taskId);
            ErpOrderInputDetail erpOrderInputDetail = new ErpOrderInputDetail();
            erpOrderInputDetail.setInputDetail(planAuditNot);
            erpOrderInputDetail.setInputTaskName(task.getName());
            erpOrderInputDetail.setSplitId(split.getId());
            inputDetailService.save(erpOrderInputDetail);

            // 保存业务流转信息
            this.workFlowService.submitSubTask(procInsId, "1,", taskId);
            this.workFlowService.completeFlow(JykFlowConstants.assignConsultant, taskId, procInsId, "修改投放方案", vars);
            String userId = this.erpOrderFlowUserService.findListByFlowId(procInsId, JykFlowConstants.assignConsultant).getUser().getId();
            dwrUtils.dwr(userId);
        }

        resObject.put("result", true);
        return resObject;
    }

    @RequestMapping(value = "work_summarize_promotion_materials")
    @ResponseBody
    public JSONObject workSummarizePromotionMaterials(String taskId, String procInsId, String orderFileId, String orderFileName) {
        JSONObject resObject = new JSONObject();
        // 保存业务流转信息
        this.workFlowService.submitSubTask(procInsId, "1,", taskId);
        if (StringUtils.isNotBlank(orderFileId)) {
            ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);
            this.erpOrderFileService.deleteByProcInsId("推广创意素材", split.getProcInsId());
            
            String[] fileIds = orderFileId.split(",");
            for(String fileId : fileIds){
                ErpOrderFile file = this.erpOrderFileService.get(fileId);
                file.setDelFlag("0");
                this.erpOrderFileService.save(file);
            }
            
            this.workFlowService.completeFlow2(new String[] {JykFlowConstants.assignTextDesignInterfacePerson}, taskId, procInsId, "汇总并同步推广素材 ",
                            null);
            
            
            String userId = this.erpOrderFlowUserService.findListByFlowId(procInsId, JykFlowConstants.assignTextDesignInterfacePerson).getUser().getId();
            dwrUtils.dwr(userId);
        }

        resObject.put("result", true);
        return resObject;
    }

    @RequestMapping(value = "work_promotion_recharge")
    @ResponseBody
    public JSONObject workPromotionRecharge(String taskId, String procInsId, String channelList, boolean isFinished) {
        JSONObject resObject = new JSONObject();

        // 保存业务流转信息
        this.workFlowService.submitSubTask(procInsId, channelList, taskId);
        if (isFinished) {
            Map<String, Object> vars = Maps.newHashMap();
            // 新增推广充值流程变量
            vars.put("workPromotionRechargeSuccess", "1");
            this.workFlowService.setVariable(taskId, "workPromotionRechargeSuccess", "1");
            this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId, "推广充值完成", vars);
            String userId = this.erpOrderFlowUserService.findListByFlowId(procInsId, JykFlowConstants.Planning_Expert).getUser().getId();
            dwrUtils.dwr(userId);
        }
        resObject.put("result", true);
        return resObject;
    }

    @RequestMapping(value = "work_promotion_of_internal_audit")
    @ResponseBody
    public JSONObject workPromotionOfInternalAudit(String taskId, String procInsId, String audit, String internalAuditNot,String fixedDocumentsVal) {
        JSONObject resObject = new JSONObject();
        // 审核通过
        if (StringUtils.isNotBlank(audit) && "1".equals(audit)) {
            Map<String, Object> vars = Maps.newHashMap();
            
            Task task1=this.workFlowService.getTaskById(taskId);
            // 表示预览审核不通过的情况,直接跳转到下一个节点
            if(task1.getProcessVariables().containsKey("workPromotionPlanPreviewConfirmationIsSuccess")
                            && "1".equals(task1.getProcessVariables().get("workPromotionPlanPreviewConfirmationIsSuccess"))){
                vars.put("workReleasePlanAudit", "3");
            }else{
                vars.put("workReleasePlanAudit", "1");
            }
            // 保存业务流转信息
            
         
            // 兼容老流程和新流程 （3.0之前老流程叫 PlanningExpert，3.0叫做AssignConsultant ）
            Map<String, String> userMap = Maps.newHashMap();
            userMap.put(JykFlowConstants.assignConsultant, JykFlowConstants.assignConsultant);
            userMap.put("TaskUser", JykFlowConstants.Planning_Expert);
            this.workFlowService.completeFlowCompatibility(userMap, taskId, procInsId, "推广素材内审通过", vars);
            
            
            String userId = this.erpOrderFlowUserService.findListByFlowId(procInsId, JykFlowConstants.assignConsultant).getUser().getId();
            dwrUtils.dwr(userId);
        } else if (StringUtils.isNotBlank(audit) && "2".equals(audit)) {
            
            
            ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);
            Task task = this.workFlowService.getTaskById(taskId);
            
            ErpOrderInputDetail erpOrderInputDetail =this.inputDetailService.getBySplitId(split.getId(), task.getName());
            if(erpOrderInputDetail==null){
                erpOrderInputDetail= new ErpOrderInputDetail();
            }
            erpOrderInputDetail.setInputDetail(internalAuditNot);
            erpOrderInputDetail.setInputTaskName(task.getName());
            erpOrderInputDetail.setSplitId(split.getId());
            inputDetailService.save(erpOrderInputDetail);
            
            Map<String, Object> vars = Maps.newHashMap();
            vars.put("workReleasePlanAudit", "2");
            vars.put("fixeddocument", "0");
            vars.put("designspath", "0");
            vars.put("workReleasePlanAuditTemp","1");
            vars.put("isAll", 0);
            String textDesignPerson = null ;
            String designer = null;
            if(StringUtils.isNotBlank(fixedDocumentsVal)){
                String[] fixedDoucuments = fixedDocumentsVal.split(",");
                if(fixedDoucuments.length==2){
                    vars.put("isAll", 1);
                }else{
                    for (String fixed : fixedDoucuments) {
                        // 跳转到修改方案
                        if("1".equals(fixed)){
                            textDesignPerson = this.erpOrderFlowUserService.findListByFlowId(procInsId, JykFlowConstants.assignTextDesignPerson).getUser().getId();
                            vars.put("fixeddocument", "1");
                        }
                        if("2".equals(fixed)){
                            designer=this.erpOrderFlowUserService.findListByFlowId(procInsId, JykFlowConstants.designer).getUser().getId();
                            vars.put("designspath", "1");
                        }
                    }
                }
            }
           
            if(StringUtils.isNotBlank(textDesignPerson)){
                vars.put("textDesignPerson", textDesignPerson);
                dwrUtils.dwr(textDesignPerson);
            }
            if(StringUtils.isNotBlank(designer)){
                vars.put("designer", designer);
                dwrUtils.dwr(designer);
            }
            
            // 兼容老流程和新流程 （3.0之前老流程叫 PlanningExpert，3.0叫做AssignConsultant ）
            Map<String, String> userMap = Maps.newHashMap();
            userMap.put(JykFlowConstants.assignConsultant, JykFlowConstants.assignConsultant);
            userMap.put("TaskUser", JykFlowConstants.Planning_Expert);
            this.workFlowService.completeFlowCompatibility(userMap, taskId, procInsId, "推广素材内审不通过", vars);
            
            // this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId,
            // procInsId, "推广素材内审不通过", vars);
            
            
            
        }
        this.workFlowService.submitSubTask(procInsId, "1,", taskId);
        resObject.put("result", true);
        return resObject;
    }

    @RequestMapping(value = "work_modif_of_delivery_plan")
    @ResponseBody
    public JSONObject workModifDeliveryPlan(String taskId, String procInsId, String orderFileId, String orderFileName) {
        JSONObject resObject = new JSONObject();
        // 再次提交
        if (StringUtils.isNotBlank(orderFileId)) {

            // 获取多文件对象
            String[] orderFileIds = orderFileId.split(",");
            for (String id : orderFileIds) {
                // 更新当前的文件为正式文件
                ErpOrderFile file = this.erpOrderFileService.get(id);
                file.setDelFlag("0");
                this.erpOrderFileService.save(file);
            }

            // 保存业务流转信息
            this.workFlowService.submitSubTask(procInsId, "1,", taskId);
            this.workFlowService.completeFlow(JykFlowConstants.assignConsultantInterface, taskId, procInsId, "投放方案修改", null);
            String userId = this.erpOrderFlowUserService.findListByFlowId(procInsId, JykFlowConstants.assignConsultantInterface).getUser().getId();
            dwrUtils.dwr(userId);
        }
        resObject.put("result", true);
        return resObject;
    }

    @RequestMapping(value = "work_integrated_promotional_materials")
    @ResponseBody
    public JSONObject workIntegratedPromotionalMaterials(String taskId, String procInsId, String channelList, String orderFileId,
                    String orderFileName, boolean isFinished) {
        JSONObject resObject = new JSONObject();
        ErpOrderFlowUser flowUser = this.erpOrderFlowUserService.findListByFlowId(procInsId, JykFlowConstants.assignConsultant);
        if(flowUser==null){
            resObject.put("result", false);
            resObject.put("message", "订单还未指定[投放顾问]，请联系相关任务处理负责人.");
            return resObject;
        }
        // 保存业务流转信息
        this.workFlowService.submitSubTask(procInsId, channelList, taskId);

        if (StringUtils.isNotBlank(orderFileId)) {
            ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);
            this.erpOrderFileService.deleteByProcInsId("推广创意初稿", split.getProcInsId());
            
            // 获取多文件对象
            String[] orderFileIds = orderFileId.split(",");
            for (String id : orderFileIds) {
                // 更新当前的文件为正式文件
                ErpOrderFile file = this.erpOrderFileService.get(id);
                file.setDelFlag("0");
                this.erpOrderFileService.save(file);
            }

            this.workFlowService.setVariable(taskId, "workIntegratedPromotionalMaterials", orderFileName);
            // 保存业务流转信息
            this.workFlowService.submitSubTask(procInsId, "2,", taskId);
        }
        if (isFinished) {
            this.workFlowService.completeFlow(JykFlowConstants.assignConsultant, taskId, procInsId, "整合推广素材", null);
            String userId = this.erpOrderFlowUserService.findListByFlowId(procInsId, JykFlowConstants.assignConsultant).getUser().getId();
            dwrUtils.dwr(userId);
        }
        resObject.put("result", true);
        return resObject;
    }

    @RequestMapping(value = "work_promotion_scheme_preview")
    @ResponseBody
    public JSONObject workPromotionSchemePreview(String taskId, String procInsId, String orderFileIdFriends, String orderFileIdWeibo,
                    String orderFileIdMomo, String orderFileNameFriends, String orderFileNameWeibo, String orderFileNameMomo, boolean isFinished) {
        JSONObject resObject = new JSONObject();
        if (StringUtils.isNotBlank(orderFileIdFriends)) {
            this.workFlowService.submitSubTask(procInsId, "1,", taskId);
            
            ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);
            this.erpOrderFileService.deleteByProcInsId("朋友圈推广预览截图", split.getProcInsId());
            
            String[] fileIds = orderFileIdFriends.split(",");
            for(String fileId : fileIds){
                ErpOrderFile orderFile = this.erpOrderFileService.get(fileId);
                orderFile.setDelFlag("0");
                this.erpOrderFileService.save(orderFile);
            }
            this.workFlowService.setVariable(taskId, "previewWechatPic", orderFileNameFriends);
        }
        if (StringUtils.isNotBlank(orderFileIdWeibo)) {
            this.workFlowService.submitSubTask(procInsId, "2,", taskId);
            ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);
            this.erpOrderFileService.deleteByProcInsId("微博推广预览截图", split.getProcInsId());
            
            String[] fileIds = orderFileIdWeibo.split(",");
            for(String fileId : fileIds){
                ErpOrderFile orderFile = this.erpOrderFileService.get(fileId);
                orderFile.setDelFlag("0");
                this.erpOrderFileService.save(orderFile);
            }
            this.workFlowService.setVariable(taskId, "previewWeiboPic", orderFileNameWeibo);
        }
        if (StringUtils.isNotBlank(orderFileIdMomo)) {
            
            ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);
            this.erpOrderFileService.deleteByProcInsId("陌陌推广预览截图", split.getProcInsId());
            this.workFlowService.submitSubTask(procInsId, "3,", taskId);
            
            String[] fileIds = orderFileIdMomo.split(",");
            for(String fileId : fileIds){
                ErpOrderFile orderFile = this.erpOrderFileService.get(fileId);
                orderFile.setDelFlag("0");
                this.erpOrderFileService.save(orderFile);
            }
            this.workFlowService.setVariable(taskId, "previewMomoPic", orderFileNameMomo);
        }

        if (isFinished) {
            this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId, "整合推广素材", null);
            String userId = this.erpOrderFlowUserService.findListByFlowId(procInsId, JykFlowConstants.assignConsultant).getUser().getId();
            dwrUtils.dwr(userId);
        }
        resObject.put("result", true);
        return resObject;
    }

    @RequestMapping(value = "work_promotion_plan_preview_confirmation")
    @ResponseBody
    public JSONObject workPromotionPlanPreviewConfirmation(String taskId, String procInsId, String audit, String planComfirmNotResone, String fixedDocumentsVal) {
        JSONObject resObject = new JSONObject();
        // 审核通过
        if (StringUtils.isNotBlank(audit) && "1".equals(audit)) {
            Map<String, Object> vars = Maps.newHashMap();
            vars.put("previewConfirmation", "1");
            // 保存业务流转信息
            this.workFlowService.completeFlow(JykFlowConstants.assignConsultant, taskId, procInsId, "推广素材预览确认通过", vars);
            String userId = this.erpOrderFlowUserService.findListByFlowId(procInsId, JykFlowConstants.Planning_Expert).getUser().getId();
            dwrUtils.dwr(userId);
            
            ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);
            // 推广方案预留确认后
            String storeId = jykOrderChoiceStoreService.getStoreIdBySplitId(split.getId());
            ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
            erpHisSplitServiceApi.confirmExtension(split.getId(), storeInfo.getId(), storeInfo.getShortName());
        }else if (StringUtils.isNotBlank(audit) && "2".equals(audit)) {
            ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);
            Task task = this.workFlowService.getTaskById(taskId);
            ErpOrderInputDetail erpOrderInputDetail =this.inputDetailService.getBySplitId(split.getId(), task.getName());
            if(erpOrderInputDetail==null){
                erpOrderInputDetail= new ErpOrderInputDetail();
            }
            erpOrderInputDetail.setInputDetail(planComfirmNotResone);
            erpOrderInputDetail.setInputTaskName(task.getName());
            erpOrderInputDetail.setSplitId(split.getId());
            inputDetailService.save(erpOrderInputDetail);
            
            Map<String, Object> vars = Maps.newHashMap();
            vars.put("previewConfirmation", "2");
            vars.put("fixeddocument", "0");
            vars.put("designspath", "0");
            vars.put("workReleasePlanAuditTemp","1");
            vars.put("isAll",0);
            String textDesignPerson = null ;
            String designer = null;
            if(StringUtils.isNotBlank(fixedDocumentsVal)){
                String[] fixedDoucuments = fixedDocumentsVal.split(",");
                if(fixedDoucuments.length==2){
                    vars.put("isAll", 1);
                }else{
                    for (String fixed : fixedDoucuments) {
                        // 跳转到修改方案
                        if("1".equals(fixed)){
                            textDesignPerson = this.erpOrderFlowUserService.findListByFlowId(procInsId, JykFlowConstants.assignTextDesignPerson).getUser().getId();
                            vars.put("fixeddocument", "1");
                        }
                        if("2".equals(fixed)){
                            designer=this.erpOrderFlowUserService.findListByFlowId(procInsId, JykFlowConstants.designer).getUser().getId();
                            vars.put("designspath", "1");
                        }
                    }
                }
            }
            if(StringUtils.isNotBlank(textDesignPerson)){
                vars.put("textDesignPerson", textDesignPerson);
                dwrUtils.dwr(textDesignPerson);
            }
            if(StringUtils.isNotBlank(designer)){
                vars.put("designer", designer);
                dwrUtils.dwr(designer);
            }
            // 增加跳转的环境变量
            this.workFlowService.setVariable(taskId, "workPromotionPlanPreviewConfirmationIsSuccess", "1");
            
            this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId, "预览确认不通过", vars);
        }
        
        
        
        this.workFlowService.submitSubTask(procInsId, "1,", taskId);
        resObject.put("result", true);
        return resObject;
    }

    @RequestMapping(value = "work_promotion_plan_for")
    @ResponseBody
    public JSONObject workPromotionPlanFor(String taskId, String procInsId, String channelList, boolean isFinished) {
        JSONObject resObject = new JSONObject();
        // 保存业务流转信息
        this.workFlowService.submitSubTask(procInsId, channelList, taskId);
        if (isFinished) {
            
            // 兼容老流程和新流程 （3.0之前老流程叫 TaskUser，3.0叫做PlanningExpert ）
            Map<String, Object> vars = Maps.newHashMap();
            Map<String, String> userMap = Maps.newHashMap();
            userMap.put(JykFlowConstants.Planning_Expert, JykFlowConstants.Planning_Expert);
            userMap.put("TaskUser", JykFlowConstants.OPERATION_ADVISER);
            this.workFlowService.completeFlowCompatibility(userMap, taskId, procInsId, "推广方案提审", vars);
            
            String userId = this.erpOrderFlowUserService.findListByFlowId(procInsId, JykFlowConstants.Planning_Expert).getUser().getId();
            dwrUtils.dwr(userId);
            ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);
            // 正式推广上线
            String storeId = jykOrderChoiceStoreService.getStoreIdBySplitId(split.getId());
            ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
            if (storeInfo != null) {
                erpHisSplitServiceApi.promotionCreativity(split.getId(), storeId, storeInfo.getShortName());
            } else {
                logger.info("门店不存在！storeId={} ", storeId);
            }

        }
        resObject.put("result", true);
        return resObject;
    }
    
    /**
     * 最终上线时间确认
     *
     * @param taskId
     * @param procInsId
     * @param onlineTime
     * @return
     * @date 2018年1月15日
     * @author SunQ
     */
    @RequestMapping(value = "finally_confirm_extension_time")
    @ResponseBody
    public JSONObject finallyConfirmExtensionTime(String taskId, String procInsId, String promotionTime) {
        JSONObject resObject = new JSONObject();
        TaskExt taskExt = new TaskExt();
        taskExt.setTaskId(taskId);
        // 保存业务流转信息
        this.workFlowService.submitSubTask(procInsId, "1", taskId);
        ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);
        split.setPromotionTime(DateUtils.parseDate(promotionTime));
        // split.setPendingProduced(Constant.YES);
        // split.setPendingProdFlag(Constant.YES);
        // taskExt.setPendingProdFlag(Constant.YES);
        // this.taskExtService.updateTaskState(taskExt);
        this.erpOrderSplitInfoService.save(split);
        this.workFlowService.completeFlow(JykFlowConstants.assignConsultant, taskId, procInsId, "最终上线时间确认", null);
        String userId = this.erpOrderFlowUserService.findListByFlowId(procInsId, JykFlowConstants.assignConsultant).getUser().getId();
        dwrUtils.dwr(userId);
        resObject.put("result", true);
        return resObject;
    }

    @RequestMapping(value = "work_promotion_online")
    @ResponseBody
    public JSONObject workPromotionOnline(String taskId, String channelList, String procInsId, String orderFileIdFriends, String orderFileIdWeibo,
                    String orderFileIdMomo, String orderFileNameFriends, String orderFileNameWeibo, String orderFileNameMomo, boolean isFinished) {
        JSONObject resObject = new JSONObject();
        // 保存业务流转信息
        this.workFlowService.submitSubTask(procInsId, channelList, taskId);

        if (StringUtils.isNotBlank(orderFileIdFriends)) {
            this.workFlowService.submitSubTask(procInsId, "1,", taskId);

            String[] fileIds = orderFileIdFriends.split(",");
            for (String fileId : fileIds) {
                ErpOrderFile orderFile = this.erpOrderFileService.get(fileId);
                orderFile.setDelFlag("0");
                this.erpOrderFileService.save(orderFile);
            }
            this.workFlowService.setVariable(taskId, "onlinePreviewWechatPic", orderFileNameFriends);
        }
        if (StringUtils.isNotBlank(orderFileIdWeibo)) {
            this.workFlowService.submitSubTask(procInsId, "2,", taskId);

            String[] fileIds = orderFileIdWeibo.split(",");
            for (String fileId : fileIds) {
                ErpOrderFile orderFile = this.erpOrderFileService.get(fileId);
                orderFile.setDelFlag("0");
                this.erpOrderFileService.save(orderFile);
            }
            this.workFlowService.setVariable(taskId, "onlinePreviewWeiboPic", orderFileNameWeibo);
        }
        if (StringUtils.isNotBlank(orderFileIdMomo)) {
            this.workFlowService.submitSubTask(procInsId, "3,", taskId);

            String[] fileIds = orderFileIdMomo.split(",");
            for (String fileId : fileIds) {
                ErpOrderFile orderFile = this.erpOrderFileService.get(fileId);
                orderFile.setDelFlag("0");
                this.erpOrderFileService.save(orderFile);
            }
            this.workFlowService.setVariable(taskId, "onlinePreviewMomoPic", orderFileNameMomo);
        }

        if (isFinished) {
            this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId, "推广方案提审", null);
            String userId = this.erpOrderFlowUserService.findListByFlowId(procInsId, JykFlowConstants.assignConsultant).getUser().getId();
            dwrUtils.dwr(userId);
            
            ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);
            // 正式推广上线
            String storeId = jykOrderChoiceStoreService.getStoreIdBySplitId(split.getId());
            ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
            erpHisSplitServiceApi.promotionOnline(split.getId(), "", storeInfo.getId(), storeInfo.getShortName());
            
            // 记录订单最终推广上线的用时
            ErpOrderOriginalInfo orderInfo = erpOrderOriginalInfoService.get(split.getOrderId());
            float between = erpHolidaysService.calculateHours(orderInfo.getBuyDate(), new Date(System.currentTimeMillis()));
            split.setOnlineUseTime(Double.parseDouble(String.valueOf(between)));
            split.setOnlineDate(new Date(System.currentTimeMillis()));
            erpOrderSplitInfoService.save(split);
        }
        resObject.put("result", true);
        return resObject;
    }

    @RequestMapping(value = "work_extended_stat_synchronization")
    @ResponseBody
    public JSONObject workExtendedStatSynchronization(String taskId, String procInsId, String channelList, boolean isFinished) {
        JSONObject resObject = new JSONObject();
        // 保存业务流转信息
        this.workFlowService.submitSubTask(procInsId, channelList, taskId);
        if (isFinished) {
            Map<String, Object> vars = Maps.newHashMap();
            String assignConsultant = this.erpOrderFlowUserService.findListByFlowId(procInsId, JykFlowConstants.Planning_Expert).getUser().getId();
            vars.put("assignConsultantFinish", assignConsultant);
            dwrUtils.dwr(assignConsultant);
            this.workFlowService.completeFlow(JykFlowConstants.assignConsultant, taskId, procInsId, "推广状态同步", vars);
            String userId = this.erpOrderFlowUserService.findListByFlowId(procInsId, JykFlowConstants.Planning_Expert).getUser().getId();
            dwrUtils.dwr(userId);
        }

        resObject.put("result", true);
        return resObject;
    }


    @RequestMapping(value = "work_first_day_promotion_data_collation")
    @ResponseBody
    public JSONObject workFirstDayPromotionDataCollation(String taskId, String procInsId, String orderFileId, String orderFileName) {
        JSONObject resObject = new JSONObject();
        // 保存业务流转信息
        this.workFlowService.submitSubTask(procInsId, "1,", taskId);
        if (StringUtils.isNotBlank(orderFileId)) {

            // 获取多文件对象
            String[] orderFileIds = orderFileId.split(",");
            for (String id : orderFileIds) {
                // 更新当前的文件为正式文件
                ErpOrderFile file = this.erpOrderFileService.get(id);
                file.setDelFlag("0");
                this.erpOrderFileService.save(file);
            }

            Map<String, Object> vars = Maps.newHashMap();
            String assignConsultant = this.erpOrderFlowUserService.findListByFlowId(procInsId, JykFlowConstants.assignConsultant).getUser().getId();
            vars.put("assignConsultantFinish", assignConsultant);
            this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId, "首日推广数据整理", vars);
            String userId = this.erpOrderFlowUserService.findListByFlowId(procInsId, JykFlowConstants.assignConsultant).getUser().getId();
            dwrUtils.dwr(userId);
        }
        resObject.put("result", true);
        return resObject;
    }

    @RequestMapping(value = "work_data_synchronization_on_the_first_day")
    @ResponseBody
    public JSONObject workDataSynchronizationOnTheFirstDay(String taskId, String procInsId, String channelList, boolean isFinished) {
        JSONObject resObject = new JSONObject();
        // 保存业务流转信息
        this.workFlowService.submitSubTask(procInsId, channelList, taskId);
        if (isFinished) {
            Map<String, Object> vars = Maps.newHashMap();
            this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId, "首日推广数据同步", vars);
            
            // 首日推广数据同步
            ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);
            String storeId = jykOrderChoiceStoreService.getStoreIdBySplitId(split.getId());
            ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
            erpHisSplitServiceApi.promotionSync(split.getId(), storeInfo.getId(), storeInfo.getShortName());
        }
        resObject.put("result", true);
        return resObject;
    }

    @RequestMapping(value = "work_audit_promotion_data")
    @ResponseBody
    public JSONObject workAuditPromotionData(String taskId, String procInsId, String channelList, String auditPromotionDataNot) {
        JSONObject resObject = new JSONObject();
        // 审核通过
        if (StringUtils.isNotBlank(channelList) && "1".equals(channelList)) {
            Map<String, Object> vars = Maps.newHashMap();
            vars.put("workAuditPromotionData", "1");
            // 保存业务流转信息
            this.workFlowService.submitSubTask(procInsId, "1,", taskId);
            this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId, "审核推广数据", vars);
            String userId = this.erpOrderFlowUserService.findListByFlowId(procInsId, JykFlowConstants.Planning_Expert).getUser().getId();
            dwrUtils.dwr(userId);
        } else {
            Map<String, Object> vars = Maps.newHashMap();
            vars.put("workAuditPromotionData", "2");
            ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);
            Task task = this.workFlowService.getTaskById(taskId);
            ErpOrderInputDetail erpOrderInputDetail = new ErpOrderInputDetail();
            erpOrderInputDetail.setInputDetail(auditPromotionDataNot);
            erpOrderInputDetail.setInputTaskName(task.getName());
            erpOrderInputDetail.setSplitId(split.getId());
            inputDetailService.save(erpOrderInputDetail);
            // 保存业务流转信息
            this.workFlowService.submitSubTask(procInsId, "1,", taskId);
            this.workFlowService.completeFlow(JykFlowConstants.assignConsultant, taskId, procInsId, "审核推广数据不通过", vars);
            String userId = this.erpOrderFlowUserService.findListByFlowId(procInsId, JykFlowConstants.assignConsultant).getUser().getId();
            dwrUtils.dwr(userId);
        }

        resObject.put("result", true);
        return resObject;
    }

    @RequestMapping(value = "work_data_consolidation_after_promotion")
    @ResponseBody
    public JSONObject workDataConsolidationAfterPromotion(String taskId, String procInsId, String orderFileId, String orderFileName) {
        JSONObject resObject = new JSONObject();
        // 保存业务流转信息
        this.workFlowService.submitSubTask(procInsId, "1,", taskId);
        if (StringUtils.isNotBlank(orderFileId)) {

            // 获取多文件对象
            String[] orderFileIds = orderFileId.split(",");
            for (String id : orderFileIds) {
                // 更新当前的文件为正式文件
                ErpOrderFile file = this.erpOrderFileService.get(id);
                file.setDelFlag("0");
                this.erpOrderFileService.save(file);
            }

            this.workFlowService.completeFlow(JykFlowConstants.assignConsultantInterface, taskId, procInsId, "推广结束后数据整理", null);
            String userId = this.erpOrderFlowUserService.findListByFlowId(procInsId, JykFlowConstants.assignConsultantInterface).getUser().getId();
            dwrUtils.dwr(userId);
        }
        resObject.put("result", true);
        return resObject;
    }

    @RequestMapping(value = "work_reorganize_promotion_data")
    @ResponseBody
    public JSONObject workReorganizePromotionData(String taskId, String procInsId, String orderFileId, String orderFileName) {
        JSONObject resObject = new JSONObject();
        // 保存业务流转信息
        this.workFlowService.submitSubTask(procInsId, "1,", taskId);
        if (StringUtils.isNotBlank(orderFileId)) {
            
            String[] fileIds = orderFileId.split(",");
            for(String fileId : fileIds){
                ErpOrderFile file = this.erpOrderFileService.get(fileId);
                file.setDelFlag("0");
                this.erpOrderFileService.save(file); 
            }
            this.workFlowService.completeFlow(JykFlowConstants.assignConsultant, taskId, procInsId, "重新整理推广数据", null);
            String userId = this.erpOrderFlowUserService.findListByFlowId(procInsId, JykFlowConstants.assignTextDesignPerson).getUser().getId();
            dwrUtils.dwr(userId);
        }
        resObject.put("result", true);
        return resObject;
    }


    @RequestMapping(value = "work_effect_report_output")
    @ResponseBody
    public JSONObject workEffectReportOutput(String taskId, String procInsId, String channelList, String orderFileId, String orderFileName,
                    boolean isFinished) {
        JSONObject resObject = new JSONObject();

        if (StringUtils.isNotBlank(channelList)) {
            // 保存业务流转信息
            this.workFlowService.submitSubTask(procInsId, channelList, taskId);
        }

        // 添加流程变量
        if (StringUtils.isNotBlank(orderFileId)) {

            // 获取多文件对象
            String[] orderFileIds = orderFileId.split(",");
            for (String id : orderFileIds) {
                // 更新当前的文件为正式文件
                ErpOrderFile file = this.erpOrderFileService.get(id);
                file.setDelFlag("0");
                this.erpOrderFileService.save(file);
            }

            this.workFlowService.setVariable(taskId, "workEffectReportOutput", orderFileName);
        }
        if (isFinished) {
            Map<String, Object> vars = Maps.newHashMap();
            this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId, "经营诊断与方案策划", vars);
            
            // 第二次推广数据同步
            ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);
            String storeId = jykOrderChoiceStoreService.getStoreIdBySplitId(split.getId());
            ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
            erpHisSplitServiceApi.promotionSyncTwo(split.getId(), storeInfo.getId(), storeInfo.getShortName());
        }
        resObject.put("result", true);
        return resObject;
    }
    
    /**
     * 审核效果报告
     *
     * @param taskId
     * @param procInsId
     * @param isPass 是否通过 1：通过2：不通过
     * @return
     * @date 2018年1月15日
     * @author SunQ
     */
    @RequestMapping(value = "work_effect_report_review")
    @ResponseBody
    public JSONObject workEffectReportReview(String taskId, String procInsId, String isPass, String reason){
        JSONObject resObject = new JSONObject();
        Map<String, Object> vars = Maps.newHashMap();
        
        vars.put("reviewResult", "N");
        if("1".equals(isPass)){
            vars.put("reviewResult", "Y");
        }
        // 修改子任务完成状态
        this.workFlowService.submitSubTask(procInsId, "1", taskId);
        this.erpOrderSubTaskService.updateTaskRemark(taskId, "1", reason);
        // 任务完成
        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.Planning_Expert}, taskId, procInsId, "审核效果报告", vars);
        resObject.put("result", true);
        return resObject;
    }
    
    /**
     * 修改效果报告
     *
     * @param taskId
     * @param procInsId
     * @param channelList
     * @return
     * @date 2018年1月15日
     * @author SunQ
     */
    @RequestMapping(value = "work_effect_report_modify")
    @ResponseBody
    public JSONObject workEffectReportModify(String taskId, String procInsId, String channelList) {
        JSONObject resObject = new JSONObject();
        
        // 设置流程变量
        Map<String, Object> vars = Maps.newHashMap();
        // 修改子任务完成状态
        this.workFlowService.submitSubTask(procInsId, "1", taskId);
        // 任务完成
        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.Planning_Expert}, taskId, procInsId, "修改效果报告", vars);
        resObject.put("result", true);
        return resObject;
    }

    @RequestMapping(value = "work_promotional_data_archiving")
    @ResponseBody
    public JSONObject workPromotionalDataArchiving(String taskId, String procInsId, String channelList, boolean isFinished) {
        return this.workPromotionalDataArchivinga(taskId, procInsId, channelList, isFinished);
    }

    @Transactional
    private JSONObject workPromotionalDataArchivinga(String taskId, String procInsId, String channelList, boolean isFinished) {
        JSONObject resObject = new JSONObject();
        // 任务最终确认完成
        this.workFlowService.submitSubTask(procInsId, channelList, taskId);
        if (StringUtils.isNotBlank(channelList) && isFinished) {
            this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId, "推广资料存档", null);
            // 更新订单为已完成状态
            ErpOrderSplitInfo erpOrderSplit=this.erpOrderSplitInfoService.getByProsIncId(procInsId);
            erpOrderSplit.setStatus(1);
            erpOrderSplit.setEndTime(new Date());
            this.erpOrderSplitInfoService.save(erpOrderSplit);
            
            // 更新原始订单信息
            ErpOrderOriginalInfo erpOrderOriginalInfo = erpOrderOriginalInfoService.get(erpOrderSplit.getOrderId());

            // 更新商品完成数量
            for (ErpOrderSplitGood erpOrderSplitGood : erpOrderSplit.getErpOrderSplitGoods()) {
                this.goodService.decreaseProcessNum(erpOrderSplitGood.getOriginalGoodId(), erpOrderSplitGood.getNum());
                erpOrderOriginalInfo.setProcessNum(erpOrderOriginalInfo.getProcessNum() - erpOrderSplitGood.getNum());
                erpOrderOriginalInfo.setFinishNum(erpOrderOriginalInfo.getFinishNum() + erpOrderSplitGood.getNum());
            }
            erpOrderOriginalInfoService.save(erpOrderOriginalInfo);

            // 效果报告输出给商户
            String storeId = jykOrderChoiceStoreService.getStoreIdBySplitId(erpOrderSplit.getId());
            ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
            erpHisSplitServiceApi.giveShop(erpOrderSplit.getId(), storeInfo.getId(), storeInfo.getShortName());
        }
        try {
            workFlowMonitorService.endProcess(procInsId);// 联动结束流程实例
        } catch (Exception e) {
            logger.error("work_promotional_data_archiving节点结束联动流程实例结束出错，procInsId={}，error={}", procInsId, e);
        }
        resObject.put("result", true);
        return resObject;
    }


}
