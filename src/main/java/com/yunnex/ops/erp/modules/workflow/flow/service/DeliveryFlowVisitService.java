package com.yunnex.ops.erp.modules.workflow.flow.service;

import java.util.List;
import java.util.Map;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.service.BaseService;
import com.yunnex.ops.erp.modules.message.constant.ServiceProgressTemplateConstants;
import com.yunnex.ops.erp.modules.message.service.ErpServiceProgressService;
import com.yunnex.ops.erp.modules.message.service.ServiceMessageManagerService;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitCommentQuestion;
import com.yunnex.ops.erp.modules.order.service.ErpOrderSplitCommentQuestionService;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.shop.service.ErpShopInfoService;
import com.yunnex.ops.erp.modules.sys.dao.UserDao;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;
import com.yunnex.ops.erp.modules.visit.constants.ErpVisitServiceConstants;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitServiceInfo;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitServiceItem;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitServiceProductRecord;
import com.yunnex.ops.erp.modules.visit.service.ErpVisitServiceInfoService;
import com.yunnex.ops.erp.modules.visit.service.ErpVisitServiceProductRecordService;
import com.yunnex.ops.erp.modules.workflow.acceptance.entity.ErpServiceAcceptance;
import com.yunnex.ops.erp.modules.workflow.acceptance.service.ErpServiceAcceptanceService;
import com.yunnex.ops.erp.modules.workflow.delivery.entity.ErpDeliveryService;
import com.yunnex.ops.erp.modules.workflow.delivery.service.ErpDeliveryServiceService;
import com.yunnex.ops.erp.modules.workflow.flow.common.JykFlowConstants;
import com.yunnex.ops.erp.modules.workflow.flow.constant.DeliveryFlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowConstant.QuestionType;
import com.yunnex.ops.erp.modules.workflow.user.constant.OrderFlowUserConstants;
import com.yunnex.ops.erp.modules.workflow.user.entity.ErpOrderFlowUser;
import com.yunnex.ops.erp.modules.workflow.user.service.ErpOrderFlowUserService;
import com.yunnex.ops.erp.modules.workflow.zhct.constant.ZhctOldShopFlowConstants;
import com.yunnex.ops.erp.modules.workflow.zhct.dto.ZhctOpenConditionDto;
import com.yunnex.ops.erp.modules.workflow.zhct.service.ErpZhctProductRecordService;

/**
 * 聚引客3.2流程
 * 
 * @author hanhan
 * @date 2018年7月9日
 */
@Service
public class DeliveryFlowVisitService extends BaseService {
	@Autowired
    private TaskService taskService;
    @Autowired
    private WorkFlowService workFlowService;
    @Autowired
    private ErpVisitServiceInfoService erpVisitServiceInfoService;
    @Autowired
    private ErpFlowFormService erpFlowFormService;
    @Autowired
    private ErpVisitServiceProductRecordService erpVisitServiceProductRecordService;
    @Autowired
    private JykFlowBeiyiService jykFlowBeiyiService;
    @Autowired
	private ErpDeliveryServiceService erpDeliveryServiceService;
    @Autowired
	private ErpShopInfoService erpShopInfoService;
    @Autowired
    private ErpServiceAcceptanceService erpServiceAcceptanceService;
    @Autowired
    private ErpOrderSplitCommentQuestionService erpOrderSplitCommentQuestionService;
    @Autowired
	private ErpVisitServiceProductRecordService visitServiceProductRecordService;
    @Autowired
    private ServiceMessageManagerService serviceMessageManagerService;
    @Autowired
    private ErpOrderFlowUserService erpOrderFlowUserService;
    @Autowired
    private UserDao userDao;
    @Autowired
	private ErpZhctProductRecordService erpZhctProductRecordService;
    @Autowired
    private ErpServiceProgressService erpServiceProgressService;
    

 /**
     * 电话预约商户
     * 
     * @param taskId
     * @param procInsId
     * @return
     */
    @Transactional(readOnly = false)
    public JSONObject phoneRevationShop(String zhctFlag,String taskId, String procInsId, String channelType,ErpVisitServiceInfo paramObj, 
            Map<String, String>map) {
        JSONObject resObject = new JSONObject();
        //保存界面的Pass or Return选项
        erpFlowFormService.saveErpFlowForm(taskId, procInsId, paramObj.getId(),map.get("formAttrName") ,map.get("node"),channelType);
        
        Map<String, Object> vars = Maps.newHashMap();
        User user = UserUtils.getUser();
        //设置审核人
        paramObj.setAuditUser(userDao.findManagerFlow(procInsId)==null?user.getId():userDao.findManagerFlow(procInsId).getId());
        paramObj.setProcInsId(procInsId);
       
        // 插入一条上门服务流程
        if ("Pass".equals(channelType)) {
            paramObj.setAuditStatus(ErpVisitServiceConstants.AUDIT_STATUS_RESERVED);// 已预约
            erpVisitServiceInfoService.saveVisitService(paramObj);
            this.workFlowService.completeFlow2(new String[] {JykFlowConstants.OPERATION_ADVISER }, taskId,
                    procInsId, "电话预约商户", vars);
            resObject.put("message", "电话预约商户成功");
        } else {
            erpVisitServiceInfoService.saveVisitService(paramObj);
            resObject.put("message", "请选择是才能完成当前流程");
        }
        
        //判断是否需要 硬件交付
        List<ErpVisitServiceProductRecord> listProductObj=paramObj.getListProductObj();
        if(CollectionUtils.isNotEmpty(listProductObj)){
	        if(Constant.YES.equals(paramObj.getHardwareDeliverFlag())){
	            //保存信息
	            for(ErpVisitServiceProductRecord productObj:listProductObj){
	                productObj.setVisitServiceInfoId(paramObj.getId());
	                productObj.setShopInfoId(paramObj.getShopInfoId());
	                erpVisitServiceProductRecordService.save(productObj);
	            }
	        }else{
	            for(ErpVisitServiceProductRecord productObj:listProductObj){
	                erpVisitServiceProductRecordService.delete(productObj); 
	            }
	        }
        } 
        
        ErpDeliveryService erpDeliveryService = erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
		ErpShopInfo shopinfo = erpShopInfoService.getByZhangbeiID(erpDeliveryService.getShopId());
		if(zhctFlag.equals(Constant.YES)) {
			ZhctOpenConditionDto zhctOpenConditionDto = paramObj.getZhctOpenConditionDto();
			if(zhctOpenConditionDto!=null) {
				//保存智慧餐厅前置条件
				erpZhctProductRecordService.saveOrUpdateZhctOpenCondition(zhctOpenConditionDto, taskId, procInsId, "", shopinfo.getId(),map.get("node"));
			}
		}
        resObject.put("result", true);
        return resObject;
    }
    

    /**
     * 上门服务预约申请
     * 
     * @param taskId
     * @param procInsId
     * @return
     */
    @Transactional(readOnly = false)
    public JSONObject visitServiceRevationApply(String zhctFlag,String taskId, String procInsId, String channelType, 
            ErpVisitServiceInfo paramObj, String formAttrName, String taskDefKey) {
        JSONObject resObject = new JSONObject();
        User user = UserUtils.getUser();
        erpFlowFormService.saveErpFlowForm(taskId, procInsId, paramObj.getId(), formAttrName, taskDefKey, channelType);
        
        if (StringUtils.isNotBlank(channelType) && channelType.indexOf("Pass") != -1) {
        	if(Constant.YES.equals(zhctFlag)) {
        		ErpDeliveryService erpDeliveryService = erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
        		ErpShopInfo shopinfo = erpShopInfoService.getByZhangbeiID(erpDeliveryService.getShopId());
        		ZhctOpenConditionDto zhctOpenConditionDto = paramObj.getZhctOpenConditionDto();
    			if(zhctOpenConditionDto!=null) {
    				//保存智慧餐厅前置条件
    				erpZhctProductRecordService.saveOrUpdateZhctOpenCondition(zhctOpenConditionDto, taskId, procInsId, "", shopinfo.getId(),taskDefKey);
    			}
    			
    			 //判断是否需要 硬件交付
    	        List<ErpVisitServiceProductRecord> listProductObj=paramObj.getListProductObj();
    	        if(CollectionUtils.isNotEmpty(listProductObj)){
    		        if(Constant.YES.equals(paramObj.getHardwareDeliverFlag())){
    		            //保存信息
    		            for(ErpVisitServiceProductRecord productObj:listProductObj){
    		                productObj.setVisitServiceInfoId(paramObj.getId());
    		                productObj.setShopInfoId(paramObj.getShopInfoId());
    		                erpVisitServiceProductRecordService.save(productObj);
    		            }
    		        }else{
    		            for(ErpVisitServiceProductRecord productObj:listProductObj){
    		                erpVisitServiceProductRecordService.delete(productObj); 
    		            }
    		        }
    	        } 
    			
        	}
        	
        	paramObj.setAuditUser(userDao.findManagerFlow(procInsId)==null?user.getId():userDao.findManagerFlow(procInsId).getId());
            paramObj.setProcInsId(procInsId);
            paramObj.setAuditStatus(ErpVisitServiceConstants.AUDIT_STATUS_SUBMITTED);// 待审核
            erpVisitServiceInfoService.saveVisitService(paramObj);
            
            Map<String, Object> vars = Maps.newHashMap();
            vars.put("reviewResult", "Pass");
            this.workFlowService.completeFlow2(new String[] { JykFlowConstants.OPERATION_MANAGER }, taskId, procInsId,
                    "上门服务提交申请", vars);
            resObject.put("message", "上门服务预约申请审批");
            resObject.put("result", true);
        } else {
            Map<String, Object> vars = Maps.newHashMap();
            vars.put("reviewResult", "Return");
            this.workFlowService.completeFlow2(new String[] { JykFlowConstants.OPERATION_ADVISER }, taskId,
                    procInsId, "重新预约上门服务", vars);
            resObject.put("message", "重新预约上门服务");
            resObject.put("result", true);
        }
        return resObject;
    }
    /**
     * 审核上门服务预约申请
     * 
     * @param taskId
     * @param procInsId
     * @return
     */
    @Transactional(readOnly = false)
    public JSONObject checkVisitServiceRevation(String taskId, String procInsId, ErpVisitServiceInfo paramObj,
            String channelType, String visitId, String formAttrName, String node) {
    	
    	JSONObject resObject = new JSONObject();
    	erpFlowFormService.saveErpFlowForm(taskId, procInsId, paramObj.getId(), formAttrName, node, channelType);

        User user = UserUtils.getUser();
        paramObj.setAuditUser(userDao.findManagerFlow(procInsId)==null?user.getId():userDao.findManagerFlow(procInsId).getId());
       
        if (StringUtils.isNotBlank(channelType) && channelType.indexOf("Pass") != -1) {
            paramObj.setAuditStatus(ErpVisitServiceConstants.AUDIT_STATUS_AUDITED);// 已审核通过
            erpVisitServiceInfoService.auditVisitService(paramObj);
            Map<String, Object> vars = Maps.newHashMap();
            vars.put("reviewResult", "Pass");
            // 已预约上门 服务通知
            serviceMessageManagerService.reserved(procInsId, visitId);
            this.workFlowService.completeFlow2(new String[] { JykFlowConstants.OPERATION_ADVISER }, taskId, procInsId,
                    "审核通过", vars);// 下个节点运营经理
            resObject.put("message", "上门服务预约申请审批");
            resObject.put("result", true);
        } else {
            paramObj.setAuditStatus(ErpVisitServiceConstants.AUDIT_STATUS_DISMISSED);// 已审核不通过
            erpVisitServiceInfoService.auditVisitService(paramObj);
            Map<String, Object> vars = Maps.newHashMap();
            vars.put("reviewResult", "Return");
            this.workFlowService.completeFlow2(new String[] { JykFlowConstants.OPERATION_ADVISER }, taskId, procInsId,
                    "审核不通过", vars);// 下个节点运营顾问
            resObject.put("message", "审核不通过");
            resObject.put("result", true);
        }
        return resObject;
    }
    /**
     * 上门服务预约申请不通过&修改
     * 
     * @param taskId
     * @param procInsId
     * @return
     */
    @Transactional(readOnly = false)
    public JSONObject checkNotPassVisitService(String zhctFlag,String taskId, String procInsId, ErpVisitServiceInfo paramObj,
            String channelType, String visitId, String formAttrName, String node) {
        JSONObject resObject = new JSONObject();
        User user = UserUtils.getUser();
        erpFlowFormService.saveErpFlowForm(taskId, procInsId, paramObj.getId(), formAttrName, node, channelType);
        if (StringUtils.isNotBlank(channelType) && channelType.indexOf("Pass") != -1) {
        	if(Constant.YES.equals(zhctFlag)) {
        		ErpDeliveryService erpDeliveryService = erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
        		ErpShopInfo shopinfo = erpShopInfoService.getByZhangbeiID(erpDeliveryService.getShopId());
        		ZhctOpenConditionDto zhctOpenConditionDto = paramObj.getZhctOpenConditionDto();
    			if(zhctOpenConditionDto!=null) {
    				//保存智慧餐厅前置条件
    				erpZhctProductRecordService.saveOrUpdateZhctOpenCondition(zhctOpenConditionDto, taskId, procInsId, "", shopinfo.getId(),node);
    			}
    			
    			 //判断是否需要 硬件交付
    	        List<ErpVisitServiceProductRecord> listProductObj=paramObj.getListProductObj();
    	        if(CollectionUtils.isNotEmpty(listProductObj)){
    		        if(Constant.YES.equals(paramObj.getHardwareDeliverFlag())){
    		            //保存信息
    		            for(ErpVisitServiceProductRecord productObj:listProductObj){
    		                productObj.setVisitServiceInfoId(paramObj.getId());
    		                productObj.setShopInfoId(paramObj.getShopInfoId());
    		                erpVisitServiceProductRecordService.save(productObj);
    		            }
    		        }else{
    		            for(ErpVisitServiceProductRecord productObj:listProductObj){
    		                erpVisitServiceProductRecordService.delete(productObj); 
    		            }
    		        }
    	        } 
    			
        	}
        	paramObj.setAuditUser(userDao.findManagerFlow(procInsId)==null?user.getId():userDao.findManagerFlow(procInsId).getId());
            paramObj.setProcInsId(procInsId);
            paramObj.setAuditStatus(ErpVisitServiceConstants.AUDIT_STATUS_SUBMITTED);// 待审核
            erpVisitServiceInfoService.saveVisitService(paramObj);
            Map<String, Object> vars = Maps.newHashMap();
            vars.put("reviewResult", "Pass");
            this.workFlowService.completeFlow2(new String[] { JykFlowConstants.OPERATION_ADVISER }, taskId, procInsId,
                    "二次提交申请", vars);// 下个节点运营经理
            resObject.put("message", "上门服务预约申请审批");
            resObject.put("result", true);
        } else {
        	paramObj.setAuditUser(userDao.findManagerFlow(procInsId)==null?user.getId():userDao.findManagerFlow(procInsId).getId());
            paramObj.setProcInsId(procInsId);
            paramObj.setAuditStatus(ErpVisitServiceConstants.AUDIT_STATUS_RESERVED);// 已预约
            erpVisitServiceInfoService.saveVisitService(paramObj);
            Map<String, Object> vars = Maps.newHashMap();
            vars.put("reviewResult", "Return");
            this.workFlowService.completeFlow2(new String[] { JykFlowConstants.OPERATION_ADVISER }, taskId, procInsId,
                    "二次重新预约上门服务", vars);// 下个节点运营顾问
            resObject.put("message", "重新预约上门服务");
            resObject.put("result", true);
        }
        return resObject;
    }

//    /**
//     * 上门服务提醒
//     * 
//     * @param taskId
//     * @param procInsId
//     * @return
//     */
//    @Transactional(readOnly = false)
//    public JSONObject remindDoorService(String taskId, String procInsId, String channelType,
//            String formAttrName, String node, ErpVisitServiceInfo paramObj) {
//        JSONObject resObject = new JSONObject();
//        User user = UserUtils.getUser();
//        erpFlowFormService.saveErpFlowForm(taskId, procInsId, paramObj.getId(), formAttrName, node, channelType);
//        if (StringUtils.isNotBlank(channelType) && channelType.indexOf("Return") != -1) {
//            paramObj.setAuditUser(user.getId());
//            paramObj.setProcInsId(procInsId);
//            paramObj.setAuditStatus(ErpVisitServiceConstants.AUDIT_STATUS_SUBMITTED);// 待审核
//            erpVisitServiceInfoService.updateVisitService(paramObj);
//            Map<String, Object> vars = Maps.newHashMap();
//            vars.put("reviewResult", "Return");
//            this.workFlowService.completeFlow2(new String[] { JykFlowConstants.OPERATION_ADVISER }, taskId, procInsId,
//                    "修改(上门服务提醒)", vars);// 下个节点运营顾问
//            resObject.put("message", "已修改(上门服务提醒)");
//            resObject.put("result", true);
//        }
//        return resObject;
//    }
    
   

   /**
     * 上门服务提醒
     * 
     * @param taskId
     * @param procInsId
     * @return
     */
    @Transactional(readOnly = false)
    public JSONObject remindDoorService(String taskId, String procInsId, String channelType,
            String formAttrName, String node, ErpVisitServiceInfo paramObj) {
    	
        erpFlowFormService.saveErpFlowForm(taskId, procInsId, paramObj.getId(), formAttrName, node, channelType);
        
        JSONObject resObject = new JSONObject();
        User user = UserUtils.getUser();
        if (StringUtils.isNotBlank(channelType) && channelType.indexOf("Pass") != -1) {
            Map<String, Object> vars = Maps.newHashMap();
            vars.put("reviewResult", "Pass");
            // 将上门提醒标识保存为Y
            erpVisitServiceInfoService.saveRemindFlagYes(paramObj.getId());
            // 上门提醒结束 服务通知管理
            serviceMessageManagerService.remindDoor(paramObj.getId());
            this.workFlowService.completeFlow2(new String[] { JykFlowConstants.OPERATION_ADVISER }, taskId, procInsId,
                    "完成上门服务(上门服务提醒)", vars);// 下个节点运营经理
            resObject.put("message", "完成上门服务");
            resObject.put("result", true);
        } else {
        	paramObj.setAuditUser(userDao.findManagerFlow(procInsId)==null?user.getId():userDao.findManagerFlow(procInsId).getId());
            paramObj.setProcInsId(procInsId);
            paramObj.setAuditStatus(ErpVisitServiceConstants.AUDIT_STATUS_SUBMITTED);// 待审核
            erpVisitServiceInfoService.updateVisitService(paramObj);
            Map<String, Object> vars = Maps.newHashMap();
            vars.put("reviewResult", "Return");
            this.workFlowService.completeFlow2(new String[] { JykFlowConstants.OPERATION_ADVISER }, taskId, procInsId,
                    "修改(上门服务提醒)", vars);// 下个节点运营顾问
            resObject.put("message", "已修改(上门服务提醒)");
            resObject.put("result", true);
        }
        return resObject;
    }


   
    /**
	 * 上门服务完成节点
	 */
    public JSONObject getVisitServiceCompleteDetail(String serviceGoalCode, String procInsId, String visitType,String taskDefKey,String serviceType) {
        JSONObject resObject = new JSONObject();
        ErpDeliveryService erpDeliveryService =erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
        ErpShopInfo shopInfo=  erpShopInfoService.getByZhangbeiID(erpDeliveryService.getShopId());
        List<String> list = erpVisitServiceInfoService.findVisitIdByProcInsId(procInsId,serviceGoalCode);
        ErpVisitServiceInfo erpVisitServiceInfo=erpVisitServiceInfoService.getVisitDetail(serviceGoalCode);
        
        //电话预约商户节点需要回显用户之前的选择visit_service_subscribe_public,visit_service_subscribe_zhct,visit_service_subscribe_zhct_old,
        if(DeliveryFlowConstant.VISIT_SERVICE_SUBSCRIBE_ZHCT.equals(taskDefKey)) {
        	 String value = erpFlowFormService.findByCondition(taskDefKey, procInsId, "visitServiceSubscribePublic_" +visitType);
             resObject.put("affirmVisitTimeList", value);
        }else if(DeliveryFlowConstant.VISIT_SERVICE_SUBSCRIBE_PUBLIC.equals(taskDefKey)) {
        	String value = erpFlowFormService.findByCondition("visit_service_subscribe_public_" + serviceType + "_" + visitType, procInsId, "visitServiceSubscribePublic_" +visitType);
            resObject.put("affirmVisitTimeList", value);
        }else if(ZhctOldShopFlowConstants.VISIT_SERVICE_SUBSCRIBE_ZHCT_OLD.equals(taskDefKey)) {
        	  String value = erpFlowFormService.findByCondition(taskDefKey, procInsId, "visitServiceSubscribePublic");
              resObject.put("affirmVisitTimeList", value);
        }
        
        //默认读取运营顾问信息
        ErpOrderFlowUser erpOrderFlowUser = erpOrderFlowUserService.findByProcInsIdAndRoleName(procInsId,
                        OrderFlowUserConstants.FLOW_USER_OPERATION_ADVISER);
        if (erpOrderFlowUser != null && erpOrderFlowUser.getUser() != null) {
            resObject.put("operationAdviserName", erpOrderFlowUser.getUser().getName());
        } else {
            resObject.put("operationAdviserName", "");
        }

        //获取硬件信息
        List<ErpVisitServiceProductRecord> productlists=null;
        if(!CollectionUtils.isEmpty(list)){
            productlists= visitServiceProductRecordService.getProductInfoByVisitServiceId(list.get(0));
        }
        resObject.put("listProductObj", productlists);
      //完成评价信息
        if (CollectionUtils.isEmpty(list)) {
			ErpVisitServiceInfo esi=new ErpVisitServiceInfo();
			esi.setServiceGoalCode(serviceGoalCode);
			esi.setShopInfoId(shopInfo.getId());
			esi.setProcInsId(procInsId);
			List<ErpVisitServiceItem> ls = erpVisitServiceInfoService.queryServiceItemData(esi);
			resObject.put("message", "查询成功");
			resObject.put("result", true);
			resObject.put("shop", shopInfo);
			resObject.put("serviceTypeTxt", erpVisitServiceInfo!=null?erpVisitServiceInfo.getServiceTypeTxt():"无服务类型");
			resObject.put("serviceGoalTxt", erpVisitServiceInfo!=null?erpVisitServiceInfo.getServiceGoalTxt():"无上门目的");
			resObject.put("shop", shopInfo);
			resObject.put("deliveryAcceptInfo ", null);
			resObject.put("productInfo ", null);
			resObject.put("trainInfo ", null);
			resObject.put("result1", null);
			resObject.put("result2", ls);
			return resObject;
		}else{
			String visitId = list.get(0);
			//完成评价信息
	        ErpServiceAcceptance serviceAcceptance= erpServiceAcceptanceService.getAcceptanceInfoByVisitId(visitId);
	        if(serviceAcceptance!=null){
	           //产品信息
	            List<ErpOrderSplitCommentQuestion>productInfo =  erpOrderSplitCommentQuestionService.getCommentAnswerByComIdAndType(serviceAcceptance.getId(), QuestionType.SELECT_MULTIPLE_TABLE);  
	            resObject.put("productInfo", productInfo);
	            //交付培训
	            List<ErpOrderSplitCommentQuestion> trainInfo =  erpOrderSplitCommentQuestionService.getCommentAnswerByComIdAndType(serviceAcceptance.getId(), QuestionType.SELECT_MULTIPLE);
	            resObject.put("trainInfo", trainInfo);
	        }
	        
	        ErpShopInfo shop=null;
	        ErpVisitServiceInfo ll = erpVisitServiceInfoService.get(visitId);
	        if (!StringUtil.isBlank(visitId)) {
	            shop =shopInfo;
	        }
	        List<ErpVisitServiceItem> ls = erpVisitServiceInfoService.queryServiceItemData(ll);
	        resObject.put("message", "查询成功");
	        resObject.put("result", true);
	        resObject.put("shop", shop);
	        resObject.put("deliveryAcceptInfo", serviceAcceptance);
	        resObject.put("serviceTypeTxt", erpVisitServiceInfo!=null?erpVisitServiceInfo.getServiceTypeTxt():"无服务类型");
			resObject.put("serviceGoalTxt", erpVisitServiceInfo!=null?erpVisitServiceInfo.getServiceGoalTxt():"无上门目的");
	        resObject.put("result1", ll);
	        resObject.put("result2", ls);
	        return resObject;
		}
	}


    /**
     * 上门服务完成
     * 
     * @param taskId
     * @param procInsId
     * @return
     */
    @Transactional(readOnly = false)
    public JSONObject completeVisitService(String taskId, String procInsId, ErpVisitServiceInfo paramObj,
            String formAttrName, String node,boolean isFinished) {
    	
    	erpFlowFormService.saveErpFlowForm(taskId, procInsId, paramObj.getId(), formAttrName, node,
                paramObj.getAuditStatus());
    	
        JSONObject resObject = new JSONObject();
        User user = UserUtils.getUser();
        //获取operationManager
        User operationManager = userDao.findManagerFlow(procInsId);
        paramObj.setAuditUser(operationManager==null?user.getId():operationManager.getId());
        
        Object serviceTypeObj = taskService.getVariable(taskId, FlowConstant.SERVICETYPE);
        Object agentTypeObj = taskService.getVariable(taskId, FlowConstant.AGENTTYPE);
        String serviceType = serviceTypeObj==null?null:serviceTypeObj.toString();
        String agentType = agentTypeObj==null?null:agentTypeObj.toString();
        
        if(isFinished){
            paramObj.setAuditStatus(ErpVisitServiceConstants.AUDIT_STATUS_COMPLETED);
            
	        Map<String, Object> vars = Maps.newHashMap();
	        Task task = this.taskService.createTaskQuery().processInstanceId(procInsId)
	                .taskDefinitionKeyLike("train_service_record%").singleResult();
	        
	        if (task != null) {
	            this.workFlowService.completeFlow2(new String[] { JykFlowConstants.OPERATION_ADVISER }, task.getId(),
	                    procInsId, "培训备忘", vars);
	        }
	        
	        this.workFlowService.completeFlow2(new String[] { JykFlowConstants.OPERATION_ADVISER }, taskId, procInsId,
	                "上门服务完成", vars);// 下个节点运营经理
	        
	        if (node.contains(DeliveryFlowConstant.VISIT_SERVICE_COMPLETE_FIRST) && !node.equals(DeliveryFlowConstant.VISIT_SERVICE_COMPLETE_FIRST_M)) {
	            // 保存 上门服务完成（首次营销策划服务）
	            jykFlowBeiyiService.saveVisitServiceTime(procInsId);
	        }
	        if (node.contains(DeliveryFlowConstant.VISIT_SERVICE_COMPLETE_ZHCT)) {
	            // 保存 上门服务完成（首次营销策划服务）
	            jykFlowBeiyiService.saveVisitServiceTime2(procInsId);
	        }
	        resObject.put("message", "完成上门服务");
        }else{
            resObject.put("message", "录入资料已保存");   
        }
        
        erpVisitServiceInfoService.completedVisitService(paramObj);
        resObject.put("result", true);
        
        //智能客流运营全套落地服务和首次上门服务基础版的“服务结束”在此启动
        if(!"visit_service_complete_zhct".equals(node)&&"Agent".equals(agentType)&&(DeliveryFlowConstant.SERVICE_TYPE_KE_CHANG_LAI_BASIC.equals(serviceType)
        		||DeliveryFlowConstant.SERVICE_TYPE_KE_CHANG_LAI.equals(serviceType))) {
            erpServiceProgressService.updateTemplateIdByManual(procInsId, serviceType, "ServiceEnd",
                    ServiceProgressTemplateConstants.STATUS_BEGIN);
        }
        return resObject;
    }



}
