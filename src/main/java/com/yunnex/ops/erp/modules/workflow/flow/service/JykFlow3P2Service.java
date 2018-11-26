package com.yunnex.ops.erp.modules.workflow.flow.service;

import java.util.Date;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.Execution;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.service.BaseService;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderCouponOutput;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitGood;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitInfo;
import com.yunnex.ops.erp.modules.order.service.ErpOrderCouponOutputService;
import com.yunnex.ops.erp.modules.order.service.ErpOrderOriginalGoodService;
import com.yunnex.ops.erp.modules.order.service.ErpOrderOriginalInfoService;
import com.yunnex.ops.erp.modules.order.service.ErpOrderSplitInfoService;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreInfoService;
import com.yunnex.ops.erp.modules.sys.utils.DwrUtils;
import com.yunnex.ops.erp.modules.workflow.data.entity.JykDataPresentation;
import com.yunnex.ops.erp.modules.workflow.data.service.JykDataPresentationService;
import com.yunnex.ops.erp.modules.workflow.flow.common.JykFlowConstants;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowConstant;
import com.yunnex.ops.erp.modules.workflow.store.service.JykOrderChoiceStoreService;
import com.yunnex.ops.erp.modules.workflow.user.service.ErpOrderFlowUserService;

/**
 * 聚迎客3.2流程
 * 
 * @author hanhan
 * @date 2018年5月7日
 */
@Service
public class JykFlow3P2Service extends BaseService {
    
    /** 工作流服务 */
    @Autowired
    private WorkFlowService workFlowService;
    @Autowired
    private ErpFlowFormService erpFlowFormService;
    @Autowired
    protected RuntimeService runtimeService;
    @Autowired
    private JykDataPresentationService jykDataPresentationService;
    @Autowired
    private ErpOrderSplitInfoService erpOrderSplitInfoService;
    @Autowired
    private JykOrderChoiceStoreService jykOrderChoiceStoreService;
    @Autowired
    private ErpStoreInfoService erpStoreInfoService;
    @Autowired
    private ErpHisSplitServiceApi erpHisSplitServiceApi;
    @Autowired
    private ErpOrderFlowUserService erpOrderFlowUserService;
    @Autowired
    private DwrUtils dwrUtils;
    @Autowired
    private ErpOrderOriginalInfoService erpOrderOriginalInfoService;
    @Autowired
    private ErpOrderOriginalGoodService goodService;
    @Autowired
    private JykDataPresentationService JykDataPresentationService;
    @Autowired
    private ErpOrderCouponOutputService erpOrderCouponOutputService;
    
    
    /**
     * 投放信息审阅
     *
     * @param taskId
     * @param consultantInterface
     * @return
     * @date 2018年5月7日
     * @author hanhan
     */
    @Transactional(readOnly = false)
    public JSONObject putInfoReview(String taskId, String procInsId,String splitId,String putInfoCheckBox,String promotionTimeCheckBox,boolean isFinished) {
        logger.info("投放信息审阅 start=== taskid[{}],procInsId[{}],splitId[{}], putInfoCheckBox[{}],promotionTimeCheckBox[{}]", taskId, procInsId, splitId,putInfoCheckBox,promotionTimeCheckBox);
        JSONObject resObject = new JSONObject();
        if(StringUtils.isBlank(promotionTimeCheckBox)&&StringUtils.isBlank(putInfoCheckBox)){
            resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_FALSE);
            resObject.put(FlowConstant.MESSAGE, "参数不合法!");
            return resObject;
        }
        erpFlowFormService.saveErpFlowForm(taskId, procInsId,splitId, FlowConstant.PUTINFO_CHECKBOX, "put_info_review_3.2",putInfoCheckBox);
        erpFlowFormService.saveErpFlowForm(taskId, procInsId,splitId, FlowConstant.PROMOTION_TIME_CHECKBOX,"put_info_review_3.2", promotionTimeCheckBox);
        if (  isFinished) {
            Map<String, Object> vars = Maps.newHashMap();
            this.workFlowService.completeFlow(JykFlowConstants.assignConsultant, taskId, procInsId, "投放信息审阅", vars);
        }
        resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
        return resObject;
    }
    
    /**
     * 广告主开户成功通知商户
     *
     * @param taskId
     * @param putInfoCheckBox 投放信息勾选框 是否勾选
     * @param notifyShopCheckBox 确认推广时间勾选框是否勾选
     * @return
     * @date 2018年5月7日
     * @author hanhan
     */
    @Transactional(readOnly = false)
    public JSONObject advertiserSuccessNotifyShop (String taskId, String procInsId,String splitId,String notifyShopCheckBox) {
        logger.info("广告主开户成功通知商户 start=== taskid[{}],procInsId[{}],splitId[{}], notifyShopCheckBox[{}]", taskId, procInsId, splitId,notifyShopCheckBox);
        JSONObject resObject = new JSONObject();
        // 保存流程表单数据
        erpFlowFormService.saveErpFlowForm(taskId, procInsId, splitId,FlowConstant.NOTIFY_SHOP_CHECKBOX,"advertiser_success_notify_shop_3.2", notifyShopCheckBox);
        Map<String, Object> vars = Maps.newHashMap();
        // 任务完成
        this.workFlowService.completeFlow(JykFlowConstants.OPERATION_ADVISER, taskId, procInsId, "广告主开户成功通知商户", vars);
        resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
        return resObject;
    }

    /**
     * 首日投放数据同步
     *
     * @param taskId
     * @param procInsId
     * @param reason
     * @return
     * @date 2018年5月9日
     * @author hanhan
     */
    @Transactional(readOnly = false)
    public JSONObject  workDataSynFirstDay (String taskId, String procInsId,String splitId,String firstDayPromoteDataInformShop,String jsonStr) {
        logger.info("首日投放数据同步 start=== taskid[{}],procInsId[{}],splitId[{}], firstDayPromoteDataInformShop[{}]", taskId, procInsId, splitId,firstDayPromoteDataInformShop);
        JSONObject resObject = new JSONObject();
        if("true".equals(firstDayPromoteDataInformShop)&& StringUtils.isBlank(jsonStr)){
            resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_FALSE);
            resObject.put(FlowConstant.MESSAGE, "首日效果报告文件没有上传！");
            return resObject;
        }
        JSONObject jsonJ = JSONObject.parseObject(jsonStr);
        String id = jsonJ.getString("id");
        String pdfUrl = jsonJ.getString("pdfUrl");
        String pdfName = jsonJ.getString("pdfName");
        saveReportData(id, procInsId, pdfUrl, pdfName, "1", "2");
       
      // 保存流程表单数据
       erpFlowFormService.saveErpFlowForm(taskId, procInsId, splitId,FlowConstant.FIRST_DAY_PROMOTE_DATA_INFORM_SHOP,"work_data_synchronization_first_day_3.2", firstDayPromoteDataInformShop);
       Map<String, Object> vars = Maps.newHashMap();
      // this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId, "首日推广数据同步", vars);
       // 首日推广数据同步
       ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);
       String storeId = jykOrderChoiceStoreService.getStoreIdBySplitId(split.getId());
       ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
       erpHisSplitServiceApi.promotionSync(split.getId(), storeInfo.getId(), storeInfo.getShortName());
       resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
       return resObject;
    }
    
    /**
     * 推广状态同步
     *
     * @param taskId
     * @param procInsId
     * @param InformShopUpLine
     * @return
     * @date 2018年5月9日
     * @author hanhan
     */
    @Transactional(readOnly = false)
    public JSONObject promotionStateSync(String taskId, String procInsId,String splitId,String InformShopUpLine){
        logger.info(" 推广状态同步 start=== taskid[{}],procInsId[{}],splitId[{}], InformShopUpLine[{}]", taskId, procInsId, splitId,InformShopUpLine);   
        JSONObject resObject = new JSONObject();
          // 保存流程表单数据
          erpFlowFormService.saveErpFlowForm(taskId, procInsId,splitId,FlowConstant.INFORM_SHOP_UP_LINE,"promotion_state_sync_3.2",InformShopUpLine);
          Map<String, Object> vars = Maps.newHashMap();
          String assignConsultant = this.erpOrderFlowUserService.findListByFlowId(procInsId, JykFlowConstants.Planning_Expert).getUser().getId();
          vars.put("assignConsultantFinish", assignConsultant);
          dwrUtils.dwr(assignConsultant);
          this.workFlowService.completeFlow(JykFlowConstants.assignConsultant, taskId, procInsId, "推广状态同步", vars);
          String userId = this.erpOrderFlowUserService.findListByFlowId(procInsId, JykFlowConstants.Planning_Expert).getUser().getId();
          dwrUtils.dwr(userId);
          resObject.put("result", true);
          return resObject;
    }
    
    
   
    /**
     * 效果报告输出给商户
     *
     * @param taskId
     * @param procInsId
     * @param InformShopUpLine
     * @return
     * @date 2018年5月9日
     * @author hanhan
     */
    @Transactional(readOnly = false)
    public  JSONObject effectReportExportedShop(String taskId, String procInsId,String splitId,String effectReportuploadOEM, String effectReportInformShopCheck,String jsonStr,boolean isFinished){
        logger.info("效果报告输出给商户 start=== taskid[{}],procInsId[{}],splitId[{}], effectReportuploadOEM[{}], effectReportInformShopCheck[{}], isFinished[{}]", taskId, 
            procInsId, splitId,effectReportuploadOEM,effectReportInformShopCheck,isFinished);   
        JSONObject resObject = new JSONObject();
        if(isFinished && StringUtils.isBlank(jsonStr)){
            resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_FALSE);
            resObject.put(FlowConstant.MESSAGE, "效果报告文件没有上传！");
            return resObject;
        }
        JSONObject jsonJ = JSONObject.parseObject(jsonStr);
        String id = jsonJ.getString("id");
        String pdfUrl = jsonJ.getString("pdfUrl");
        String pdfName = jsonJ.getString("pdfName");
        saveReportData(id, procInsId, pdfUrl, pdfName, "3", "2");
        
        erpFlowFormService.saveErpFlowForm(taskId, procInsId,splitId, FlowConstant.EFFECT_REPORT_UPLOAD_OEM, "effect_report_exported_shop_3.2",effectReportuploadOEM);
        erpFlowFormService.saveErpFlowForm(taskId, procInsId,splitId, FlowConstant.EFFECT_REPORT_INFORM_SHOP_CHECK,"effect_report_exported_shop_3.2", effectReportInformShopCheck);
        if (isFinished) {
            this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId, "推广资料存档", null);
            //更新订单为已完成状态 
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
        resObject.put("result", true);
        return resObject;
    }
    
    /**
     * 输出卡券
     *
     * @param taskId
     * @param procInsId
     * @param jsonStr 
     * @return
     * @date 2018年5月10日
     * @author hanhan
     */
    @Transactional(readOnly = false)
    public  JSONObject outputCardCoupon(String taskId, String procInsId, String splitId,String jsonStr,String isCardInfoInput, boolean isFinished){
        JSONObject resObject = new JSONObject();
        //保存流程表单数据
        erpFlowFormService.saveErpFlowForm(taskId, procInsId,splitId, FlowConstant.IS_CARD_INFO_INPUT,"output_card_coupon_3.2", isCardInfoInput);
        //保存卡券信息
        if(!StringUtils.isBlank(jsonStr)){
         JSONArray jsonArray = JSONArray.parseArray(jsonStr);
         for(int i=0;i<jsonArray.size(); i++){
             JSONObject jsonJ = jsonArray.getJSONObject(i); 
             String couponName= jsonJ.getString("couponName");
             String couponLinkCategory= jsonJ.getString("couponLinkCategory");
             String couponLink= jsonJ.getString("couponLink");
             String id=jsonJ.getString("id");
             ErpOrderCouponOutput erpCouponInfo= erpOrderCouponOutputService.get(id); 
             if(null==erpCouponInfo){
                 erpCouponInfo=new ErpOrderCouponOutput();
             }
             erpCouponInfo.setSplitId(splitId);
             erpCouponInfo.setCouponLink(couponLink);
             erpCouponInfo.setCouponLinkCategory(couponLinkCategory);
             erpCouponInfo.setCouponName(couponName);
             erpCouponInfo.setRemarks("");
             erpCouponInfo.setDelFlag("0");
             erpOrderCouponOutputService.save(erpCouponInfo);
         }
     }
        if (isFinished) {
            Map<String, Object> vars = Maps.newHashMap();
            // 任务完成
            this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId, "输出卡券", vars);
        }
        resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
        return resObject;
    }
    
    
    //判断是否激活 等待推广状态同步 的任务节点
    public void isActivatePromotionStateSyncFlow(String procInsId){
        //激活等待任务
        Execution execution = runtimeService.createExecutionQuery().processInstanceId(procInsId).activityId(FlowConstant.WAITING_PROMOTION_STATE_SYNC)
                        .singleResult();
        if(null!=execution){
           runtimeService.signal(execution.getId()); 
       }
    }
    
    
    public void saveReportData(String id,String procInsId,String pdfUrl,String pdfName,String dataType,String state){
        if(StringUtils.isNotBlank(id)){
            JykDataPresentation dataPresentation = jykDataPresentationService.get(id);
            dataPresentation.setProcInsId(procInsId);
            dataPresentation.setPdfUrl(pdfUrl);
            dataPresentation.setPdfName(pdfName);
            dataPresentation.setDataType(dataType);
            dataPresentation.setState(state);
            jykDataPresentationService.save(dataPresentation);
        }else{
            ErpOrderSplitInfo splitInfo = erpOrderSplitInfoService.getByProsIncId(procInsId);
            JykDataPresentation dataPresentation = new JykDataPresentation();
            dataPresentation.setOrderId(splitInfo.getOrderId());
            dataPresentation.setSplitId(splitInfo.getId());
            dataPresentation.setProcInsId(procInsId);
            dataPresentation.setPdfUrl(pdfUrl);
            dataPresentation.setPdfName(pdfName);
            dataPresentation.setDataType(dataType);
            dataPresentation.setState(state);
            jykDataPresentationService.deleteBefore(splitInfo.getId(), dataType);
            jykDataPresentationService.save(dataPresentation);
        }
    }
    
    
}