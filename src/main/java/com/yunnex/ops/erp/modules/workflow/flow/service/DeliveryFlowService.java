package com.yunnex.ops.erp.modules.workflow.flow.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.common.constant.CommonConstants;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.persistence.BaseEntity;
import com.yunnex.ops.erp.common.service.BaseService;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.common.utils.AESUtil;
import com.yunnex.ops.erp.modules.act.service.ActTaskService;
import com.yunnex.ops.erp.modules.act.utils.ActUtils;
import com.yunnex.ops.erp.modules.holiday.service.ErpHolidaysService;
import com.yunnex.ops.erp.modules.material.entity.ErpOrderMaterialCreation;
import com.yunnex.ops.erp.modules.material.service.ErpOrderMaterialCreationService;
import com.yunnex.ops.erp.modules.message.service.ErpServiceMessageService;
import com.yunnex.ops.erp.modules.message.service.ErpServiceProgressService;
import com.yunnex.ops.erp.modules.order.service.ErpOrderGoodServiceInfoService;
import com.yunnex.ops.erp.modules.shop.constant.ShopConstant;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopActualLinkman;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.shop.service.ErpShopActualLinkmanService;
import com.yunnex.ops.erp.modules.shop.service.ErpShopInfoService;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreInfoService;
import com.yunnex.ops.erp.modules.store.pay.entity.ErpStorePayUnionpay;
import com.yunnex.ops.erp.modules.store.pay.entity.ErpStorePayWeixin;
import com.yunnex.ops.erp.modules.store.pay.service.ErpStorePayUnionpayService;
import com.yunnex.ops.erp.modules.store.pay.service.ErpStorePayWeixinService;
import com.yunnex.ops.erp.modules.sys.constant.SysConstant;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.service.SysConstantsService;
import com.yunnex.ops.erp.modules.sys.service.SystemService;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;
import com.yunnex.ops.erp.modules.visit.service.ErpVisitServiceInfoService;
import com.yunnex.ops.erp.modules.workflow.acceptance.service.ErpServiceAcceptanceService;
import com.yunnex.ops.erp.modules.workflow.delivery.constant.ErpDeliveryServiceConstants;
import com.yunnex.ops.erp.modules.workflow.delivery.entity.ErpDeliveryService;
import com.yunnex.ops.erp.modules.workflow.delivery.extraModel.DeliveryServiceWorkDays;
import com.yunnex.ops.erp.modules.workflow.delivery.service.ErpDeliveryServiceService;
import com.yunnex.ops.erp.modules.workflow.flow.common.JykFlowConstants;
import com.yunnex.ops.erp.modules.workflow.flow.constant.DeliveryFlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowFormConstant;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpFlowForm;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderFile;
import com.yunnex.ops.erp.modules.workflow.user.service.ErpOrderFlowUserService;
import com.yunnex.ops.erp.modules.workflow.zhct.dto.ZhctOpenConditionDto;
import com.yunnex.ops.erp.modules.workflow.zhct.service.ErpZhctProductRecordService;

@Service
public class DeliveryFlowService extends BaseService {
	@Autowired
	private ErpOrderFlowUserService erpOrderFlowUserService;
	@Autowired
	private ErpDeliveryServiceService erpDeliveryServiceService;
	@Autowired
	private ErpShopActualLinkmanService linkmanService;
	@Autowired
	private ErpShopInfoService erpShopInfoService;
	@Autowired
	private ErpFlowFormService erpFlowFormService;
	@Autowired
	private ErpStorePayWeixinService wxpayService;
	@Autowired
	private WorkFlowService workFlowService;
	@Autowired
	private ErpStoreInfoService erpStoreInfoService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private ErpOrderFileService erpOrderFileService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private ErpHolidaysService erpHolidaysService;
	@Autowired
	private SysConstantsService sysConstantsService;
	@Autowired
    private  ErpStorePayUnionpayService erpStorePayUnionpayService;
	@Autowired
	private ErpStorePayWeixinService erpStorePayWeixinService;
	@Autowired
	private ErpZhctProductRecordService erpZhctProductRecordService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private ErpOrderGoodServiceInfoService erpOrderGoodServiceInfoService;
    @Autowired
    private ActTaskService actTaskService;
    @Autowired
    private ErpServiceMessageService serviceMessageService;
    @Autowired
    private ErpServiceProgressService serviceProgressService;
    @Autowired
    private WorkFlowMonitorService workFlowMonitorService;
    @Autowired
    private ErpOrderMaterialCreationService erpOrderMaterialCreationService;
    @Autowired
    private ErpVisitServiceInfoService erpVisitServiceInfoService;
    @Autowired
    private ErpServiceAcceptanceService erpServiceAcceptanceService;

	
    /**
     * 查看订单信息，指派订单处理人员
     *
     * @param taskId
     * @param procInsId operationAdviser 运营顾问 openAccountConsultant 开户顾问 materialConsultant 物料顾问
     * @return
     * @date 2018年5月26日
     * @author hanhan
     */
	@Transactional(readOnly = false)
	public JSONObject assignOrderHandlers(Map<String, String> map) {
	    String taskId=map.get("taskId");
        String procInsId=map.get("procInsId");
        String operationAdviser=map.get("operationAdviser");
        String openAccountConsultant=map.get("openAccountConsultant");
        String materialConsultant=map.get("materialConsultant");
        String orderId=map.get("orderId");
	    JSONObject resObject = new JSONObject();
		logger.info(
                        "查看订单信息，指派订单处理人员start=== taskid[{}],procInsId[{}],orderId[{}],operationAdviser[{}],openAccountConsultant[{}],materialConsultant[{}]",
				taskId, procInsId, orderId, operationAdviser, openAccountConsultant, materialConsultant);
        // 根据流程id查询交付流程信息
		if (StringUtils.isNotBlank(operationAdviser)) {
			this.erpOrderFlowUserService.insertOrderFlowUser(operationAdviser, orderId, "",
					JykFlowConstants.OPERATION_ADVISER, procInsId);
		}
		if (StringUtils.isNotBlank(openAccountConsultant)) {
			this.erpOrderFlowUserService.insertOrderFlowUser(openAccountConsultant, orderId, "",
					JykFlowConstants.ACCOUNT_ADVISER, procInsId);
		}
		if (StringUtils.isNotBlank(materialConsultant)) {
			this.erpOrderFlowUserService.insertOrderFlowUser(materialConsultant, orderId, "",
					JykFlowConstants.MATERIAL_ADVISER, procInsId);
		}
        // 获取服务类型
		Object variable = taskService.getVariable(taskId, DeliveryFlowConstant.SERVICE_TYPE);
		String serviceType = variable == null ? null : variable.toString();
        // 保存启动时间 和 其他一些时间
		saveStartTimeOther(procInsId, serviceType);
        // 结束流程
		if (StringUtils.isNotBlank(operationAdviser) && StringUtils.isNotBlank(openAccountConsultant)
		                && StringUtils.isNotBlank(materialConsultant)) {
			Map<String, Object> vars = Maps.newHashMap();
			this.workFlowService.completeFlow2(new String[] { JykFlowConstants.OPERATION_ADVISER }, taskId, procInsId,
                            "完成指派订单处理人员", vars);
		}
		resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
		return resObject;
	}
	
	/**
     * 智慧餐厅菜单配置(交付流程)
     * 
     * @param taskId
     * @param procInsId
     * @param orderId
     * @param jsonStr
     * @param isFinished
     * @return
     */
	public JSONObject zhctMenuConfiguration(String taskId, String procInsId, String orderId, String jsonStr,String taskDefKey) {
		JSONObject resObject = new JSONObject();
        logger.info("智慧餐厅菜单配置(交付流程)start=== taskid[{}],procInsId[{}],orderId[{}], jsonStr[{}]",
				 taskId, procInsId, orderId, jsonStr);
		JSONObject jsonJ = JSONObject.parseObject(jsonStr);
        // 智慧餐厅菜单配置任务操作
		String zhctMenuConfigMemo = jsonJ.getString("zhctMenuConfigMemo");
		erpFlowFormService.saveErpFlowForm(taskId, procInsId, orderId, FlowFormConstant.ZHCT_MENU_CONFIG_MEMO,
				taskDefKey, zhctMenuConfigMemo);
		
		this.workFlowService.completeFlow2(new String[] {JykFlowConstants.OPERATION_ADVISER}, taskId, procInsId,
                        "智慧餐厅菜单配置", null);
		
		resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
		return resObject;
	}

	/**
     * 电话联系商户，确认服务内容（服务类型有客常来）
     *
     * @param taskId
     * @param procInsId jsonStrt
     * @return
     * @date 2018年5月26日
     * @author hanhan
     */
	@Transactional(readOnly = false)
	public JSONObject telephoneConfirmService(String taskId, String procInsId, String orderId, String jsonStr,
			boolean isFinished) {
        logger.info("电话联系商户，确认服务内容(服务类型有客常来)start=== taskid[{}],procInsId[{}],orderId[{}], jsonStr[{}],isFinished[{}]",
				taskId, procInsId, orderId, jsonStr, isFinished);
        if(StringUtils.isBlank(procInsId)||StringUtils.isBlank(jsonStr)) {
            logger.error("DeliveryFlowService.telephoneConfirmService()入参procInsId={},jsonStr={}有误", procInsId, jsonStr);
            throw new ServiceException("流程ID或者jsonStr不能为空！");
        }
        
        JSONObject resObject = new JSONObject();
		ErpDeliveryService erpDeliveryService = erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
		ErpShopInfo shopinfo = erpShopInfoService.getByZhangbeiID(erpDeliveryService.getShopId());
		if (null == shopinfo) {
            resObject.put(FlowConstant.MESSAGE, "请先添加商户资料");
			resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
			return resObject;
		}
		
		JSONObject jsonJ = JSONObject.parseObject(jsonStr);
		
        // 保存联系人信息
		JSONArray inputShopOfficerInfo = jsonJ.getJSONArray("inputShopOfficerInfo");
		addShopOfficerInfo(inputShopOfficerInfo, shopinfo.getId());

        // 批量保存流程表单数据List
		List<ErpFlowForm> flowFormDataList = new ArrayList<>();
		ErpFlowForm flowForm = null;
		
        // 根据流程id查询流程里面所有的属性名
	    List<String> formAttrNames = erpFlowFormService.findFormAttrNamesByProcInsId(procInsId);

        // 智慧餐厅标识
	  	String zhctFlag = (String) taskService.getVariable(taskId, DeliveryFlowConstant.ZHCT_FLAG+"");

		String contactShopMemo = jsonJ.getString("contactShopMemo");
		if(formAttrNames.contains(FlowFormConstant.CONTACT_SHOP_MEMO)) {
			flowForm = new ErpFlowForm();
			flowForm.preUpdate();
			flowForm.setFormAttrValue(contactShopMemo);
			erpFlowFormService.updateErpFlowForm(procInsId,FlowFormConstant.CONTACT_SHOP_MEMO,flowForm);
		}else {
			if(StringUtils.isNotBlank(contactShopMemo)) {
				flowForm = new ErpFlowForm(taskId, orderId, procInsId, Constant.YES.equals(zhctFlag)?DeliveryFlowConstant.TELEPHONE_CONFIRM_SERVICE_ZHCT:DeliveryFlowConstant.TELEPHONE_CONFIRM_SERVICE, 
						FlowConstant.FLOW_FORM_DATA_ATTR_TYPE_NORMAL, FlowFormConstant.CONTACT_SHOP_MEMO, contactShopMemo, "");
				flowForm.preInsert();
				flowFormDataList.add(flowForm);
			}
		}

        // 建立商户服务群备注(表单数据)
		String shopServiceGroupMemo = jsonJ.getString("shopServiceGroupMemo");
		if(formAttrNames.contains(FlowFormConstant.SHOP_SERVICEGROUP_MEMO)){
			flowForm = new ErpFlowForm();
			flowForm.preUpdate();
			flowForm.setFormAttrValue(shopServiceGroupMemo);
			erpFlowFormService.updateErpFlowForm(procInsId,FlowFormConstant.SHOP_SERVICEGROUP_MEMO,flowForm);
		}else {
			if(StringUtils.isNotBlank(shopServiceGroupMemo)) {
				flowForm = new ErpFlowForm(taskId, orderId, procInsId, Constant.YES.equals(zhctFlag)?DeliveryFlowConstant.TELEPHONE_CONFIRM_SERVICE_ZHCT:DeliveryFlowConstant.TELEPHONE_CONFIRM_SERVICE, 
						FlowConstant.FLOW_FORM_DATA_ATTR_TYPE_NORMAL, FlowFormConstant.SHOP_SERVICEGROUP_MEMO, shopServiceGroupMemo, "");
				flowForm.preInsert();
				flowFormDataList.add(flowForm);
			}
		}
		
        // 任务操作部分(表单数据)
		String flowOperator = jsonJ.getString("flowOperator");
		if(formAttrNames.contains(FlowFormConstant.FLOW_OPERATOR)){
			flowForm = new ErpFlowForm();
			flowForm.preUpdate();
			flowForm.setFormAttrValue(flowOperator);
			erpFlowFormService.updateErpFlowForm(procInsId,FlowFormConstant.FLOW_OPERATOR,flowForm);
		}else {
			if(StringUtils.isNotBlank(flowOperator)) {
				flowForm = new ErpFlowForm(taskId, orderId, procInsId, Constant.YES.equals(zhctFlag)?DeliveryFlowConstant.TELEPHONE_CONFIRM_SERVICE_ZHCT:DeliveryFlowConstant.TELEPHONE_CONFIRM_SERVICE, 
						FlowConstant.FLOW_FORM_DATA_ATTR_TYPE_NORMAL, FlowFormConstant.FLOW_OPERATOR, flowOperator, "");
				flowForm.preInsert();
				flowFormDataList.add(flowForm);
			}
		}
		
        // 确定商户是否有微信公众号和口碑门店
		String isShopweixinID = jsonJ.getString("isShopweixinID");
		if(formAttrNames.contains(FlowFormConstant.IS_SHOP_WEIXIN_ID)) {
			flowForm = new ErpFlowForm();
			flowForm.preUpdate();
			flowForm.setFormAttrValue(isShopweixinID);
			erpFlowFormService.updateErpFlowForm(procInsId,FlowFormConstant.IS_SHOP_WEIXIN_ID,flowForm);
		}else {
			if(StringUtils.isNotBlank(isShopweixinID)) {
				flowForm = new ErpFlowForm(taskId, orderId, procInsId, Constant.YES.equals(zhctFlag)?DeliveryFlowConstant.TELEPHONE_CONFIRM_SERVICE_ZHCT:DeliveryFlowConstant.TELEPHONE_CONFIRM_SERVICE, 
						FlowConstant.FLOW_FORM_DATA_ATTR_TYPE_NORMAL, FlowFormConstant.IS_SHOP_WEIXIN_ID, isShopweixinID, "");
				flowForm.preInsert();
				flowFormDataList.add(flowForm);
			}
		}
		
		String isShopAliPayID = jsonJ.getString("isShopAliPayID");
		if(formAttrNames.contains(FlowFormConstant.IS_SHOP_ALIPAY_ID)) {
			flowForm = new ErpFlowForm();
			flowForm.preUpdate();
			flowForm.setFormAttrValue(isShopAliPayID);
			erpFlowFormService.updateErpFlowForm(procInsId,FlowFormConstant.IS_SHOP_ALIPAY_ID,flowForm);
		}else{
			if(StringUtils.isNotBlank(isShopAliPayID)) {
				flowForm = new ErpFlowForm(taskId, orderId, procInsId, Constant.YES.equals(zhctFlag)?DeliveryFlowConstant.TELEPHONE_CONFIRM_SERVICE_ZHCT:DeliveryFlowConstant.TELEPHONE_CONFIRM_SERVICE, 
						FlowConstant.FLOW_FORM_DATA_ATTR_TYPE_NORMAL, FlowFormConstant.IS_SHOP_ALIPAY_ID, isShopAliPayID, "");
				flowForm.preInsert();
				flowFormDataList.add(flowForm);
			}
		}
		
        // 批量保存流程表单数据
	    erpFlowFormService.batchInsert(flowFormDataList);
		
        // 智慧餐厅特有
		if(Constant.YES.equals(zhctFlag)) {
			ZhctOpenConditionDto dto = new ZhctOpenConditionDto();
			dto.setCashRegisterInfoJson(jsonJ.getString("cashRegisterInfoJson"));
			dto.setCashRegisterNum(jsonJ.getString("cashRegisterNum"));
			dto.setIsConnectInternet(jsonJ.getString("isConnectInternet"));
			dto.setIsConnectPrinter(jsonJ.getString("isConnectPrinter"));
			dto.setIsShopCashRegister(jsonJ.getString("isShopCashRegister"));
			dto.setIsShopPrinter(jsonJ.getString("isShopPrinter"));
			dto.setPrinterNum(jsonJ.getString("printerNum"));
			dto.setPrinterInfoJson(jsonJ.getString("printerInfoJson"));
			dto.setShopDesc(jsonJ.getString("shopDesc"));
            // 保存智慧餐厅前置条件
			erpZhctProductRecordService.saveOrUpdateZhctOpenCondition(dto, taskId, procInsId, orderId, shopinfo.getId(),DeliveryFlowConstant.TELEPHONE_CONFIRM_SERVICE_ZHCT);
		}
		
        if (inputShopOfficerInfo.size() < 2) {
            resObject.put(FlowConstant.MESSAGE, "最少录入两名商户负责人联系方式");
            resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
            return resObject;
        }
        
        // 完成任务
		if (isFinished && inputShopOfficerInfo.size() >= 2 
				&& null != flowOperator && flowOperator.split(",").length==2
				&&isShopAliPayID!=null
				&&isShopweixinID!=null) {
			Map<String, Object> vars = Maps.newHashMap();
            // 有无 掌贝账号 根据掌贝进件状态判断
			ErpStoreInfo erpStoreInfo=  erpStoreInfoService.findismain(shopinfo.getId(), Global.NO);
			vars.put(DeliveryFlowConstant.ZHANG_BEI_FLAG, "N");
			if (null!=erpStoreInfo &&"2".equals(erpStoreInfo.getAuditStatus() + "")) {
				vars.put(DeliveryFlowConstant.ZHANG_BEI_FLAG, "Y");
			}
			
            // 判断有没口碑门店
			vars.put(DeliveryFlowConstant.PUBLIC_PRAISE_FLAG, isShopAliPayID);
            // 判断有没公众号
			vars.put(DeliveryFlowConstant.PUBLIC_NUMBER_FLAG, isShopweixinID);
            // 判断是否有微信账号
			vars.put(DeliveryFlowConstant.WECHAT_ACCOUNT_FLAG, "N");
			if(null!=erpStoreInfo && StringUtils.isNotBlank(erpStoreInfo.getWeixinPayId())){
			    ErpStorePayWeixin weixinPay = wxpayService.get(erpStoreInfo.getWeixinPayId());   
                if(null!=weixinPay&&"2".equals(weixinPay.getAuditStatus()+"")){
                    vars.put(DeliveryFlowConstant.WECHAT_ACCOUNT_FLAG, "Y");
                }
            }
            // 判断是否有银联账号
			vars.put(DeliveryFlowConstant.UNIONPAY_ACCOUNT_FLAG, "N");
			if(null!=erpStoreInfo && StringUtils.isNotBlank(erpStoreInfo.getUnionpayId())){
                ErpStorePayUnionpay unionPay = erpStorePayUnionpayService.get(erpStoreInfo.getUnionpayId());   
                if(null!=unionPay&&"2".equals(unionPay.getAuditStatus()+"")){
                    vars.put(DeliveryFlowConstant.UNIONPAY_ACCOUNT_FLAG, "Y");
                }
            }
			String agentType = (String) taskService.getVariable(taskId, FlowConstant.AGENTTYPE);
			String serviceType = (String) taskService.getVariable(taskId, FlowConstant.SERVICETYPE);
			if (SysConstant.COMPANY_TYPE_AGENT.equalsIgnoreCase(agentType)
					&& DeliveryFlowConstant.SERVICE_TYPE_KE_CHANG_LAI.equalsIgnoreCase(serviceType)) {
				vars.put(FlowConstant.MARKETING_PLANNING, FlowConstant.RUNNING);
			}
			vars.put(FlowConstant.SERVICE_STARTUP, FlowConstant.FINISH);
			vars.put(FlowConstant.ACCOUNT_PAY_OPEN, FlowConstant.RUNNING);
			this.workFlowService.completeFlow2(new String[] { JykFlowConstants.OPERATION_ADVISER,
					JykFlowConstants.ACCOUNT_ADVISER, JykFlowConstants.MATERIAL_ADVISER }, taskId, procInsId,
                            "电话联系商户，确认服务内容", vars);
		}
		resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
		return resObject;
	}

	public void addShopOfficerInfo(JSONArray inputShopOfficerInfo, String shopinfoId) {
		if (!CollectionUtils.isEmpty(inputShopOfficerInfo)) {
			for (int i = 0; i < inputShopOfficerInfo.size(); i++) {
				JSONObject obj = inputShopOfficerInfo.getJSONObject(i);
				String id = obj.getString("id");
				ErpShopActualLinkman linkMan = new ErpShopActualLinkman();
				if (StringUtils.isNotBlank(id)) {
					linkMan.setId(id);
				}
				linkMan.setName(obj.getString("name"));
				linkMan.setPosition(obj.getString("position"));
				linkMan.setPhoneNo(obj.getString("phoneNo"));
				linkMan.setWechatID(obj.getString("wechatID"));
				linkMan.setShopInfoId(shopinfoId);
				linkmanService.save(linkMan);
			}
		}
	}
	

	/**
     * 进件资料收集
     *
     * @param taskId
     * @param procInsId completeToApplet
     * @return
     * @date 2018年5月26日
     * @author hanhan
     */
	@Transactional(readOnly = false)
	public JSONObject intoMaterialMollection(String taskId, String procInsId, String orderId, String completeToApplet) {
        logger.info("进件资料收集start=== taskid[{}],procInsId[{}],completeToApplet[{}]", taskId, procInsId,
				completeToApplet);
		JSONObject resObject = new JSONObject();
		erpFlowFormService.saveErpFlowForm(taskId, procInsId, orderId, FlowFormConstant.COMPLETE_TO_APPLET,
				DeliveryFlowConstant.INTO_MATERIAL_COLLECTION, completeToApplet);
		Map<String, Object> vars = Maps.newHashMap();
		ErpDeliveryService erpDeliveryService = erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
        ErpShopInfo shopinfo = erpShopInfoService.getByZhangbeiID(erpDeliveryService.getShopId());
        // 有无 掌贝账号 根据掌贝进件状态判断
        ErpStoreInfo erpStoreInfo=  erpStoreInfoService.findismain(shopinfo.getId(), Global.NO);
        vars.put(DeliveryFlowConstant.ZHANG_BEI_FLAG,Constant.NO);
        vars.put(DeliveryFlowConstant.WECHAT_ACCOUNT_FLAG, Constant.NO);
        vars.put(DeliveryFlowConstant.UNIONPAY_ACCOUNT_FLAG, Constant.NO);
        if(erpStoreInfo!=null){
	        if ("2".equals(erpStoreInfo.getAuditStatus() + "")) {
	            vars.put(DeliveryFlowConstant.ZHANG_BEI_FLAG, Constant.YES);
	        }
            // 判断是否有微信账号
	        if(StringUtils.isNotBlank(erpStoreInfo.getWeixinPayId())){
	            ErpStorePayWeixin weixinPay = wxpayService.get(erpStoreInfo.getWeixinPayId());   
	            if(null!=weixinPay&&"2".equals(weixinPay.getAuditStatus()+"")){
	                vars.put(DeliveryFlowConstant.WECHAT_ACCOUNT_FLAG, Constant.YES);
	            }
	        }
            // 判断是否有银联账号
	        if(StringUtils.isNotBlank(erpStoreInfo.getUnionpayId())){
	            ErpStorePayUnionpay unionPay = erpStorePayUnionpayService.get(erpStoreInfo.getUnionpayId());   
	            if(null!=unionPay&&"2".equals(unionPay.getAuditStatus()+"")){
	                vars.put(DeliveryFlowConstant.UNIONPAY_ACCOUNT_FLAG, Constant.YES);
	            }
	        }
        }
		this.workFlowService.completeFlow2(new String[] { JykFlowConstants.OPERATION_ADVISER,
                        JykFlowConstants.ACCOUNT_ADVISER, JykFlowConstants.MATERIAL_ADVISER}, taskId, procInsId, "进件资料收集",
				vars);
		resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
		return resObject;
	}

	/**
     * 掌贝后台创建门店
     *
     * @param taskId
     * @param procInsId completeCreateStore
     * @return
     * @date 2018年5月26日
     * @author hanhan
     */
	@Transactional(readOnly = false)
	public JSONObject zhangbeiStoreCreate(String taskId, String procInsId, String orderId, String completeCreateStore) {
        logger.info("掌贝后台创建门店start=== taskid[{}],procInsId[{}],orderId[{}]completeCreateStore[{}]", taskId, procInsId,
				orderId, completeCreateStore);
		JSONObject resObject = new JSONObject();
        // 判断后台有没创建门店
		erpFlowFormService.saveErpFlowForm(taskId, procInsId, orderId, FlowFormConstant.COMPLETE_CREATE_STORE,
				DeliveryFlowConstant.ZHANGBEI_STORE_CREATE, completeCreateStore);
		ErpDeliveryService erpDeliveryService = erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
		ErpShopInfo shop = erpShopInfoService.getByZhangbeiID(erpDeliveryService.getShopId());
		ErpStoreInfo erpStoreInfo = erpStoreInfoService.findismain(shop.getId(), Global.NO);
		if (erpStoreInfo == null) {
			resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_FALSE);
            resObject.put(FlowConstant.MESSAGE, "请至少创建一个主门店！");
			return resObject;
		}
		Map<String, Object> vars = Maps.newHashMap();
		this.workFlowService.completeFlow2(new String[] { JykFlowConstants.OPERATION_ADVISER }, taskId, procInsId,
                        "进件资料收集", vars);
		resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
		return resObject;
	}

	/**
     * 公众号开通
     *
     * @param taskId
     * @param procInsId jsonStr
     * @return
     * @date 2018年5月26日
     * @author hanhan
     */
	@Transactional(readOnly = false)
	public JSONObject publicNumberOpen(String taskId, String procInsId, String orderId, String jsonStr,
			boolean isFinished) {
        logger.info("公众号开通=== taskid[{}],procInsId[{}],orderId[{}],jsonStr[{}],isFinished[{}]", taskId, procInsId,
				orderId, jsonStr, isFinished);
		JSONObject resObject = new JSONObject();
		JSONObject jsonJ = JSONObject.parseObject(jsonStr);
		String publicFileInfoId = jsonJ.getString("publicFileInfoId");
		if (StringUtils.isNotBlank(publicFileInfoId)) {
			erpFlowFormService.saveErpFlowForm(taskId, procInsId, orderId, FlowFormConstant.PUBLIC_FILE_INFO_ID,
					DeliveryFlowConstant.PUBLIC_NUMBER_OPEN, publicFileInfoId, ErpFlowForm.FILE);
			String[] fileIdLists = publicFileInfoId.split(Constant.SEMICOLON);
			for (String id : fileIdLists) {
				ErpOrderFile fileInfo = erpOrderFileService.get(id);
				fileInfo.setDelFlag("0");
				erpOrderFileService.delete(fileInfo);
			}
		}
        // 公众号操作备忘
		String weixinAccountOpenMemo = jsonJ.getString("weixinAccountOpenMemo");
		if (null != weixinAccountOpenMemo) {
			erpFlowFormService.saveErpFlowForm(taskId, procInsId, orderId, FlowFormConstant.WEI_XIN_ACCOUNT_OPEN_MEMO,
					DeliveryFlowConstant.PUBLIC_NUMBER_OPEN, weixinAccountOpenMemo);
		}
        // 公众号信息保存
		JSONObject publicAccountNoInfo = jsonJ.getJSONObject("publicAccountNoInfo");
		if (null != weixinAccountOpenMemo) {
			ErpDeliveryService erpDeliveryService = erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
			ErpShopInfo shop = erpShopInfoService.getByZhangbeiID(erpDeliveryService.getShopId());
			ErpStoreInfo erpStoreInfo = erpStoreInfoService.findismain(shop.getId(), Global.NO);
			if (erpStoreInfo == null) {
				resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_FALSE);
                resObject.put(FlowConstant.MESSAGE, "请等待掌贝后台创建门店任务完成！");
				return resObject;
			}
			String id = publicAccountNoInfo.getString("id");
			ErpStorePayWeixin wxpay = null;
			if (StringUtils.isNotBlank(id)) {
				wxpay = wxpayService.get(id);
			} else {
				wxpay = new ErpStorePayWeixin();
			}
			wxpay.setPublicAccountNo(publicAccountNoInfo.getString("publicAccountNo"));
			wxpay.setPublicAccountPassword(AESUtil.encrypt(publicAccountNoInfo.getString("publicAccountPassword")));
			wxpay.setPublicAccountAppid(publicAccountNoInfo.getString("publicAccountAppid"));
			wxpay.setPublicAccountName(publicAccountNoInfo.getString("publicAccountName"));
			wxpay.setSyncOem(ShopConstant.whether.NO);
			wxpayService.save(wxpay);
			erpStoreInfo.setWeixinPayId(wxpay.getId());
			erpStoreInfoService.save(erpStoreInfo);
		}
        // 结束任务
		if (isFinished && null != weixinAccountOpenMemo) {
		    ErpDeliveryService erpDeliveryService = erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
	        ErpShopInfo shopinfo = erpShopInfoService.getByZhangbeiID(erpDeliveryService.getShopId());
		    Map<String, Object> vars = Maps.newHashMap();
	        ErpStoreInfo erpStoreInfo=  erpStoreInfoService.findismain(shopinfo.getId(), Global.NO);
	        vars.put(DeliveryFlowConstant.WECHAT_ACCOUNT_FLAG, "N");
            // 判断是否有微信账号
	        if(erpStoreInfo!=null&&StringUtils.isNotBlank(erpStoreInfo.getWeixinPayId())){
	            ErpStorePayWeixin weixinPay = wxpayService.get(erpStoreInfo.getWeixinPayId());   
	            if(null!=weixinPay&&"2".equals(weixinPay.getAuditStatus()+"")){
	                vars.put(DeliveryFlowConstant.WECHAT_ACCOUNT_FLAG, "Y");
	            }
	        }
			this.workFlowService.completeFlow2(new String[] { JykFlowConstants.ACCOUNT_ADVISER }, taskId, procInsId,
                            "公众号开通", vars);
		}
		resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
		return resObject;
	}

	/**
     * 微信账号开通 — （进件状态）
     *
     * @param taskId
     * @param procInsId
     * @param weixinPayCheck
     * @return
     * @date 2018年5月26日
     * @author hanhan
     */
	@Transactional(readOnly = false)
	public JSONObject wechatAccountOpen(String taskId, String procInsId, String orderId, String weixinPayCheck) {
        logger.info("微信支付开通 — （进件状态）start=== taskid[{}],procInsId[{}],orderId[{}],weixinPayCheck[{}]", taskId,
				procInsId, orderId, weixinPayCheck);
		JSONObject resObject = new JSONObject();
		erpFlowFormService.saveErpFlowForm(taskId, procInsId, orderId, FlowFormConstant.WEI_XIN_PAY_CHECK,
				DeliveryFlowConstant.WECHAT_ACCOUNT_OPEN, weixinPayCheck);
        // 查询微信进件状态
		ErpDeliveryService erpDeliveryService = erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
		ErpShopInfo shop = erpShopInfoService.getByZhangbeiID(erpDeliveryService.getShopId());
        ErpStoreInfo mainStore = erpStoreInfoService.findismain(shop.getId(), BaseEntity.DEL_FLAG_NORMAL);
        if (null != mainStore) {
            // 微信支付进件状态
            ErpStorePayWeixin erpStorePayWeixin = erpStorePayWeixinService.get(mainStore.getWeixinPayId());
            if (null!=erpStorePayWeixin&& "2".equals(erpStorePayWeixin.getAuditStatus() + "")) {
    			Map<String, Object> vars = Maps.newHashMap();
                // 获取业管-微信支付
    			List<User> userListFriends = systemService.getUserByRoleName("pi_friends");
    			if (!CollectionUtils.isEmpty(userListFriends)) {
    			    String userId = userListFriends.get(0).getId();
    			    vars.put(JykFlowConstants.pipeIndustryFriends, userId);
    				erpOrderFlowUserService.insertOrderFlowUser(userId, orderId, "", JykFlowConstants.pipeIndustryFriends,
    						procInsId);
    			}else{
    			    resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_FALSE);
                    resObject.put(FlowConstant.MESSAGE, "当前用户没有权限获取下步处理人信息权限！");
    	            return resObject;
    			}
    			this.workFlowService.completeFlow2(new String[] { JykFlowConstants.pipeIndustryFriends }, taskId, procInsId,
                                "微信账号开通", vars);
    		}
        }
		resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
		return resObject;
	}

	/**
     * 微信支付商户号配置
     *
     * @param taskId
     * @param procInsId weixinPayMerNoDeploy
     * @return
     * @date 2018年5月26日
     * @author hanhan
     */
	@Transactional(readOnly = false)
	public JSONObject wechatShopConfiguration(String taskId, String procInsId, String orderId,
			String weixinPayMerNoDeploy) {
        logger.info("微信支付商户号配置）start=== taskid[{}],procInsId[{}],orderId[{}], weixinPayMerNoDeploy[{}]", taskId,
				procInsId, orderId, weixinPayMerNoDeploy);
		JSONObject resObject = new JSONObject();
		erpFlowFormService.saveErpFlowForm(taskId, procInsId, orderId, FlowFormConstant.WEI_XIN_PAY_MERNO_DEPLOY,
				DeliveryFlowConstant.WECHAT_SHOP_CONFIGURATION, weixinPayMerNoDeploy);
		Map<String, Object> vars = Maps.newHashMap();
		this.workFlowService.completeFlow2(new String[] { JykFlowConstants.OPERATION_ADVISER,
                        JykFlowConstants.ACCOUNT_ADVISER, JykFlowConstants.MATERIAL_ADVISER}, taskId, procInsId, "微信支付商户号配置",
				vars);
		resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
		return resObject;
	}

	/**
     * 支付宝口碑申请
     *
     * @param taskId
     * @param procInsId
     * @param orderId weixinPayMerNoDeploy
     * @return
     * @date 2018年5月26日
     * @author hanhan
     */
	@Transactional(readOnly = false)
	public JSONObject alipayPublicPraiseApply(String taskId, String procInsId, String orderId, String jsonStr,
			boolean isFinished) {
        logger.info("支付宝口碑申请 start=== taskid[{}],procInsId[{}],orderId[{}], jsonStr[{}], isFinished[{}]", taskId,
				procInsId, orderId, jsonStr, isFinished);
		JSONObject resObject = new JSONObject();
		JSONObject jsonJ = JSONObject.parseObject(jsonStr);
        // 支付宝口碑操作备忘
		String ailipaMemo = jsonJ.getString("ailipaMemo");
		if (StringUtils.isNotBlank(ailipaMemo)) {
			erpFlowFormService.saveErpFlowForm(taskId, procInsId, orderId, FlowFormConstant.ALIPA_MEMO,
					DeliveryFlowConstant.ALIPAY_PUBLIC_PRAISE_APPLY, ailipaMemo);
		}
		String openAlipaflag = jsonJ.getString("openAlipaflag");
		erpFlowFormService.saveErpFlowForm(taskId, procInsId, orderId, FlowFormConstant.OPEN_ALIPA_FLAG,
				DeliveryFlowConstant.ALIPAY_PUBLIC_PRAISE_APPLY, openAlipaflag);
        // 更新上传的图片
		String alipaAccountScreenshot = jsonJ.getString("alipaAccountScreenshot");
		if (StringUtils.isNotBlank(alipaAccountScreenshot)) {
			erpFlowFormService.saveErpFlowForm(taskId, procInsId, orderId, FlowFormConstant.ALIPA_ACCOUNT_SCREENSHOT,
					DeliveryFlowConstant.ALIPAY_PUBLIC_PRAISE_APPLY, alipaAccountScreenshot, ErpFlowForm.FILE);
			String[] fileIdLists = alipaAccountScreenshot.split(Constant.SEMICOLON);
			for (String id : fileIdLists) {
				ErpOrderFile fileInfo = erpOrderFileService.get(id);
				fileInfo.setDelFlag("0");
				erpOrderFileService.delete(fileInfo);
			}
		}
		String zhangbeiBoundStoreScreenshot = jsonJ.getString("zhangbeiBoundStoreScreenshot");
		if (StringUtils.isNotBlank(zhangbeiBoundStoreScreenshot)) {
			erpFlowFormService.saveErpFlowForm(taskId, procInsId, orderId,
					FlowFormConstant.ZHANGBEI_BOUND_STORE_SCREENSHOT, DeliveryFlowConstant.ALIPAY_PUBLIC_PRAISE_APPLY,
					zhangbeiBoundStoreScreenshot, ErpFlowForm.FILE);
			String[] fileIdLists = zhangbeiBoundStoreScreenshot.split(Constant.SEMICOLON);
			for (String id : fileIdLists) {
				ErpOrderFile fileInfo = erpOrderFileService.get(id);
				fileInfo.setDelFlag("0");
				erpOrderFileService.delete(fileInfo);
			}
		}
		ErpDeliveryService erpDeliveryService = erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
		ErpShopInfo shop = erpShopInfoService.getByZhangbeiID(erpDeliveryService.getShopId());
        // if ("Y".equals(openAlipaflag)) {//贝蚁2.1优化，屏蔽口碑账号数据录入
        // // 支付宝账号信息
        // JSONObject ailipaInfo = jsonJ.getJSONObject("ailipaInfo");
        // if (null != ailipaMemo) {
        // String id = ailipaInfo.getString("id");
        // ErpShopPayAlipa erpShopPayAlipa = null;
        // if (StringUtils.isNotBlank(id)) {
        // erpShopPayAlipa = erpShopPayAlipaService.get(id);
        // } else {
        // erpShopPayAlipa = new ErpShopPayAlipa();
        // }
        // // 查找商户id
        // erpShopPayAlipa.setAccountNo(ailipaInfo.getString("accountNo"));
        // erpShopPayAlipa.setAccountPassword(ailipaInfo.getString("accountPassword"));
        // erpShopPayAlipa.setShopInfoId(shop.getId());
        // erpShopPayAlipaService.save(erpShopPayAlipa);
        // }
        // }
		if ("N".equals(openAlipaflag)) {
			String notOpenAlipaChatScreenshot = jsonJ.getString("notOpenAlipaChatScreenshot");
			if (StringUtils.isNotBlank(notOpenAlipaChatScreenshot)) {
				erpFlowFormService.saveErpFlowForm(taskId, procInsId, orderId,
						FlowFormConstant.NOT_OPEN_ALIPA_CHAT_SCREENSHOT,
						DeliveryFlowConstant.ALIPAY_PUBLIC_PRAISE_APPLY, notOpenAlipaChatScreenshot, ErpFlowForm.FILE);
				String[] fileIdLists = notOpenAlipaChatScreenshot.split(Constant.SEMICOLON);
				for (String id : fileIdLists) {
					ErpOrderFile fileInfo = erpOrderFileService.get(id);
					fileInfo.setDelFlag("0");
					erpOrderFileService.delete(fileInfo);
				}
			}
            // 结束流程
			Map<String, Object> vars = Maps.newHashMap();
			this.workFlowService.completeFlow2(new String[] { JykFlowConstants.OPERATION_ADVISER }, taskId, procInsId,
                            "支付宝口碑申请", vars);
			erpShopInfoService.updateAlipaStateById(shop.getId(), "notOpen");
			resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
			return resObject;
		}
        // 判断结束任务
		if (isFinished) {
			Map<String, Object> vars = Maps.newHashMap();
			this.workFlowService.completeFlow2(new String[] { JykFlowConstants.OPERATION_ADVISER }, taskId, procInsId,
                            "支付宝口碑申请", vars);
			erpShopInfoService.updateAlipaStateById(shop.getId(), "open");
		}
		resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
		return resObject;
	}

	/**
     * 银联支付开通
     *
     * @param taskId
     * @param procInsId machineNoConfiguration
     * @return
     * @date 2018年5月26日
     * @author hanhan
     */
	@Transactional(readOnly = false)
	public JSONObject unionpayAccountOpen(String taskId, String procInsId, String orderId,
			String machineNoConfiguration, String unionpayOpenState, String notOpenUnionpayScreenshot) {
		logger.info(
                        "银联支付开通start=== taskid[{}],procInsId[{}],orderId[{}],machineNoConfiguration[{}],unionpayOpenState [{}]",
				taskId, procInsId, orderId, machineNoConfiguration, unionpayOpenState);
		JSONObject resObject = new JSONObject();
		ErpDeliveryService erpDeliveryService = erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
		ErpShopInfo shop = erpShopInfoService.getByZhangbeiID(erpDeliveryService.getShopId());
		ErpStoreInfo erpStoreInfo = erpStoreInfoService.findismain(shop.getId(), Global.NO);
		if (erpStoreInfo == null) {
			resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_FALSE);
            resObject.put(FlowConstant.MESSAGE, "请等待掌贝后台创建门店任务完成！");
			return resObject;
		}
		erpFlowFormService.saveErpFlowForm(taskId, procInsId, orderId, FlowFormConstant.MACHINE_NO_CONFIGURATION,
				DeliveryFlowConstant.UNIONPAY_ACCOUNT_OPEN, machineNoConfiguration);
		erpFlowFormService.saveErpFlowForm(taskId, procInsId, orderId, FlowFormConstant.UNIONPAY_OPEN_STATE,
				DeliveryFlowConstant.UNIONPAY_ACCOUNT_OPEN, unionpayOpenState);
        // 银联支付进件状态
        ErpStorePayUnionpay erpStorePayUnionpay = erpStorePayUnionpayService.get(erpStoreInfo.getUnionpayId());
		if ("Y".equals(unionpayOpenState) && erpStorePayUnionpay!=null&&erpStorePayUnionpay.getAuditStatus() == 2) {
			Map<String, Object> vars = Maps.newHashMap();
			vars.put(DeliveryFlowConstant.UNIONPAY_OPEN_FLAG,"Y" );
			this.workFlowService.completeFlow2(new String[] { JykFlowConstants.ACCOUNT_ADVISER }, taskId, procInsId,
                            "银联支付开通", vars);
		} else if ("N".equals(unionpayOpenState)) {
			if (StringUtils.isNotBlank(notOpenUnionpayScreenshot)) {
                // 更新不开通银联标示
				String storeId=erpStoreInfo.getId();
				erpStoreInfoService.updateNotOpenUnionpayFlag(storeId,Constant.YES);
				
				erpFlowFormService.saveErpFlowForm(taskId, procInsId, orderId,
						FlowFormConstant.NOT_OPEN_UNIONPAY_SCREENSHOT, DeliveryFlowConstant.UNIONPAY_ACCOUNT_OPEN,
						notOpenUnionpayScreenshot, ErpFlowForm.FILE);
				String[] fileIdLists = notOpenUnionpayScreenshot.split(Constant.SEMICOLON);
				for (String id : fileIdLists) {
					ErpOrderFile fileInfo = erpOrderFileService.get(id);
					fileInfo.setDelFlag("0");
					erpOrderFileService.delete(fileInfo);
				}
				Map<String, Object> vars = Maps.newHashMap();
				vars.put(DeliveryFlowConstant.UNIONPAY_OPEN_FLAG,"N" );
				this.workFlowService.completeFlow2(new String[] { JykFlowConstants.ACCOUNT_ADVISER }, taskId, procInsId,
                                "银联支付开通", vars);
			}
		}
		resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
		return resObject;
	}

	/**
     * 银联支付培训&测试（远程）
     *
     * @param taskId
     * @param procInsId
     * @param orderId
     * @param unionpayTrainTest
     * @return
     * @date 2018年5月26日
     * @author hanhan
     */
	@Transactional(readOnly = false)
	public JSONObject unionpayAccountTrain(String taskId, String procInsId, String orderId, String unionpayTrainTest) {
        logger.info("银联支付培训&测试（远程））start=== taskid[{}],procInsId[{}],orderId[{}], unionpayTrainTest[{}]", taskId,
				procInsId, orderId, unionpayTrainTest);
		JSONObject resObject = new JSONObject();
		erpFlowFormService.saveErpFlowForm(taskId, procInsId, orderId, FlowFormConstant.UNIONPAY_TRAIN_TEST,
				DeliveryFlowConstant.UNIONPAY_ACCOUNT_TRAIN, unionpayTrainTest);
		Map<String, Object> vars = Maps.newHashMap();
		this.workFlowService.completeFlow2(new String[] { JykFlowConstants.OPERATION_ADVISER }, taskId, procInsId,
                        "银联支付培训&测试（远程）", vars);
		saveTrainsTime(procInsId);
		resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
		return resObject;
	}
	
	/**
     * 保存 物料制作跟踪任务应该完成时间
     *
     * @param procInsId
     * @date 2018年6月8日
     * @author linqunzhi
     */
    private void saveTrainsTime(String procInsId) {
    	ErpDeliveryService erpDeliveryService = erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
        if (erpDeliveryService == null) {
            logger.error("交付服务信息不存在 ！procInsId=", procInsId);
            throw new ServiceException(CommonConstants.FailMsg.DATA);
        }
        Date now = new Date();
        // 物料制作跟踪任务完成时间
        erpDeliveryService.setTrainTestTime(now);
        erpDeliveryServiceService.save(erpDeliveryService);
    }

	/**
     * 保存 银联支付培训&测试（远程） 完成时间
     *
     * @param procInsId
     * @date 2018年6月8日
     * @author linqunzhi
     */
    @Transactional
    public void saveTrainTestTime(String procInsId) {
        // 记录 银联支付培训&测试（远程） 完成时间
		ErpDeliveryService erpDeliveryService = erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
		if (erpDeliveryService == null) {
            logger.error("交付服务信息不存在 ！procInsId={}", procInsId);
			throw new ServiceException(CommonConstants.FailMsg.DATA);
		}
        Date now = new Date();
        // 银联支付培训&测试（远程）任务完成时间
        erpDeliveryService.setTrainTestTime(now);
		erpDeliveryServiceService.save(erpDeliveryService);
	}
    
    /**
     * 保存启动时间 和 其他相关时间
     *
     * @param procInsId
     * @param serviceType
     * @date 2018年6月7日
     * @author linqunzhi
     */
	public void saveStartTimeOther(String procInsId, String serviceType) {
		logger.info("saveStartTimeOther start | procInsId={}|serviceType={}", procInsId, serviceType);
		ErpDeliveryService erpDeliveryService = erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
		if (erpDeliveryService == null) {
            logger.error("交付服务信息不存在 ！procInsId={}", procInsId);
			throw new ServiceException(CommonConstants.FailMsg.DATA);
		}
        // 获取工作日天数配置值
		String josnStr="";
		if(DeliveryFlowConstant.SERVICE_TYPE_JU_YIN_KE.equals(erpDeliveryService.getServiceType())
				|| DeliveryFlowConstant.SERVICE_TYPE_KE_CHANG_LAI.equals(erpDeliveryService.getServiceType())
				|| DeliveryFlowConstant.SERVICE_TYPE_JU_YIN_DATA.equals(erpDeliveryService.getServiceType())
				|| DeliveryFlowConstant.SERVICE_TYPE_KE_CHANG_LAI_BASIC.equals(erpDeliveryService.getServiceType())){
            // {"juYingKe":{"shouldFlowEndDays":13},"keChangLai":{"shouldFlowEndDays":25},"shouldTrainTestDays":23,"shouldMaterielDays":9,"shouldVisitServiceDays":13}
			josnStr = sysConstantsService
					.getConstantValByKey(ErpDeliveryServiceConstants.DELIVERY_SERVICE_WORK_DAYS);	
		}else if(DeliveryFlowConstant.SERVICE_TYPE_MU.equals(erpDeliveryService.getServiceType())){
            // {"juYingKe":{"shouldFlowEndDays":10},"keChangLai":{"shouldFlowEndDays":10},"shouldTrainTestDays":0,"shouldMaterielDays":9,"shouldVisitServiceDays":10}
			josnStr = sysConstantsService
					.getConstantValByKey(ErpDeliveryServiceConstants.DELIVERY_SERVICE_MATERIAL_DAYS);	
		}else if(DeliveryFlowConstant.SERVICE_TYPE_VC.equals(erpDeliveryService.getServiceType())){
            // {"juYingKe":{"shouldFlowEndDays":3},"keChangLai":{"shouldFlowEndDays":3},"shouldTrainTestDays":0,"shouldMaterielDays":0,"shouldVisitServiceDays":0}
			josnStr = sysConstantsService
					.getConstantValByKey(ErpDeliveryServiceConstants.DELIVERY_SERVICE_VC_DAYS);
		}
		
		DeliveryServiceWorkDays workDays = null;
		if (StringUtils.isNotBlank(josnStr)) {
			try {
				workDays = JSON.parseObject(josnStr, DeliveryServiceWorkDays.class);
			} catch (Exception e) {
                logger.error("转换工作日错误", e);
			}

		}
        // 聚引客应完成交付的工作日天数
		int juYinKeshouldFlowEndDays = 0;
        // 客常来应完成交付的工作日天数
		int keChangLaishouldFlowEndDays = 0;
        // 银联支付培训&测试（远程）任务应该完成的工作日天数
		int shouldTrainTestDays = 0;
        // 上门服务完成（首次营销策划服务）任务应该完成的工作日天数
		int shouldVisitServiceDays = 0;
		if (workDays != null) {
			if (workDays.getJuYingKe() != null && workDays.getJuYingKe().getShouldFlowEndDays() != null) {
				juYinKeshouldFlowEndDays = workDays.getJuYingKe().getShouldFlowEndDays();
			}
			if (workDays.getKeChangLai() != null && workDays.getKeChangLai().getShouldFlowEndDays() != null) {
				keChangLaishouldFlowEndDays = workDays.getKeChangLai().getShouldFlowEndDays();
			}
			if (workDays.getShouldTrainTestDays() != null) {
				shouldTrainTestDays = workDays.getShouldTrainTestDays();
			}
			if (workDays.getShouldVisitServiceDays() != null) {
				shouldVisitServiceDays = workDays.getShouldVisitServiceDays();
			}
		}
        // 启动时间
		Date now = new Date();
        // 应完成交付时间
		Date shouldFlowEndTime = null;
		if (DeliveryFlowConstant.SERVICE_TYPE_JU_YIN_KE.equals(serviceType)) {
			shouldFlowEndTime = erpHolidaysService.getWorkDay(now, juYinKeshouldFlowEndDays);
		} else if(DeliveryFlowConstant.SERVICE_TYPE_KE_CHANG_LAI.equals(serviceType)) {
			shouldFlowEndTime = erpHolidaysService.getWorkDay(now, keChangLaishouldFlowEndDays);
		} else if(DeliveryFlowConstant.SERVICE_TYPE_MU.equals(serviceType)){
			shouldFlowEndTime = erpHolidaysService.getWorkDay(now, keChangLaishouldFlowEndDays);
		} else if(DeliveryFlowConstant.SERVICE_TYPE_VC.equals(serviceType)){
			shouldFlowEndTime = erpHolidaysService.getWorkDay(now, keChangLaishouldFlowEndDays);
		} else if(DeliveryFlowConstant.SERVICE_TYPE_KE_CHANG_LAI_BASIC.equals(serviceType)){
			shouldFlowEndTime = erpHolidaysService.getWorkDay(now, keChangLaishouldFlowEndDays);
        } else if(DeliveryFlowConstant.SERVICE_TYPE_JU_YIN_DATA.equals(serviceType)){
        	shouldFlowEndTime = erpHolidaysService.getWorkDay(now, keChangLaishouldFlowEndDays);
        } 
        // 银联支付培训&测试（远程）任务应该完成时间
		Date shouldTrainTestTime = erpHolidaysService.getWorkDay(now, shouldTrainTestDays);
        // 上门服务完成（首次营销策划服务）任务应该完成时间
		Date shouldVisitServiceTime = erpHolidaysService.getWorkDay(now, shouldVisitServiceDays);
		erpDeliveryService.setStartTime(now);
		erpDeliveryService.setShouldFlowEndTime(shouldFlowEndTime);
		erpDeliveryService.setShouldTrainTestTime(shouldTrainTestTime);
		erpDeliveryService.setShouldVisitServiceTime(shouldVisitServiceTime);
		erpDeliveryServiceService.save(erpDeliveryService);
		logger.info("saveStartTimeOther end");
	}
	
	public ErpDeliveryService saveStartTimeOther1(String procInsId) {
		ErpDeliveryService erpDeliveryService = erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
		if (erpDeliveryService == null) {
            logger.error("交付服务信息不存在 ！procInsId={}", procInsId);
			throw new ServiceException(CommonConstants.FailMsg.DATA);
		}
        // 应完成交付时间
		Date shouldTrainTestTime=null;
		
		Date shouldVisitServiceTime=null;
		
		Date shouldMaterielTime=null;
		
        // 聚引客应完成交付的工作日天数
		int juYinKeshouldFlowEndDays = 0;
        // 银联支付培训&测试（远程）任务应该完成的工作日天数
		int shouldTrainTestDays = 0;
        // 上门服务完成（首次营销策划服务）任务应该完成的工作日天数
		int shouldVisitServiceDays = 0;
		
		int shouldMaterielServiceDays = 0;
		
        // 获取工作日天数配置值
		String josnStr="";
		String serviceType=erpDeliveryService.getServiceType();
		if(StringUtil.isBlank(serviceType)){
			if(DeliveryFlowConstant.SERVICE_TYPE_KE_ZHCT_OLD.equals(erpDeliveryService.getZhctType())){
				josnStr = sysConstantsService
						.getConstantValByKey(ErpDeliveryServiceConstants.DELIVERY_SERVICE_ZHCT_DAYS);
				DeliveryServiceWorkDays workDays = null;
				if (StringUtils.isNotBlank(josnStr)) {
					try {
						workDays = JSON.parseObject(josnStr, DeliveryServiceWorkDays.class);
					} catch (Exception e) {
                        logger.error("转换工作日错误", e);
					}
				}
				if (workDays != null) {
					juYinKeshouldFlowEndDays = workDays.getJuYingKe().getShouldFlowEndDays();
					shouldTrainTestTime = erpHolidaysService.getWorkDay(erpDeliveryService.getStartTime()==null?new Date():erpDeliveryService.getStartTime(), juYinKeshouldFlowEndDays);
					shouldVisitServiceTime = erpHolidaysService.getWorkDay(erpDeliveryService.getStartTime()==null?new Date():erpDeliveryService.getStartTime(), juYinKeshouldFlowEndDays);
					shouldMaterielTime=erpHolidaysService.getWorkDay(erpDeliveryService.getStartTime()==null?new Date():erpDeliveryService.getStartTime(), juYinKeshouldFlowEndDays);
				}
			}
		}else{
			if(DeliveryFlowConstant.SERVICE_TYPE_JU_YIN_KE.equals(erpDeliveryService.getServiceType())
					|| DeliveryFlowConstant.SERVICE_TYPE_KE_CHANG_LAI.equals(erpDeliveryService.getServiceType())
					|| DeliveryFlowConstant.SERVICE_TYPE_JU_YIN_DATA.equals(erpDeliveryService.getServiceType())
					|| DeliveryFlowConstant.SERVICE_TYPE_KE_CHANG_LAI_BASIC.equals(erpDeliveryService.getServiceType())){
				josnStr = sysConstantsService
						.getConstantValByKey(ErpDeliveryServiceConstants.DELIVERY_SERVICE_WORK_DAYS);	
			}else if(DeliveryFlowConstant.SERVICE_TYPE_MU.equals(erpDeliveryService.getServiceType())){
				josnStr = sysConstantsService
						.getConstantValByKey(ErpDeliveryServiceConstants.DELIVERY_SERVICE_MATERIAL_DAYS);	
			}else if(DeliveryFlowConstant.SERVICE_TYPE_VC.equals(erpDeliveryService.getServiceType())){
				josnStr = sysConstantsService
						.getConstantValByKey(ErpDeliveryServiceConstants.DELIVERY_SERVICE_VC_DAYS);
			}
			DeliveryServiceWorkDays workDays = null;
			if (StringUtils.isNotBlank(josnStr)) {
				try {
					workDays = JSON.parseObject(josnStr, DeliveryServiceWorkDays.class);
				} catch (Exception e) {
                    logger.error("转换工作日错误", e);
				}
			}
			if (workDays != null) {
				if (workDays.getShouldTrainTestDays() != null) {
					shouldTrainTestDays = workDays.getShouldTrainTestDays();
				}
				if (workDays.getShouldVisitServiceDays() != null) {
					shouldVisitServiceDays = workDays.getShouldVisitServiceDays();
				}
				if (workDays.getShouldMaterielDays() != null) {
					shouldMaterielServiceDays = workDays.getShouldMaterielDays();
				}
			}
            // 银联支付培训&测试（远程）任务应该完成时间
			shouldTrainTestTime = erpHolidaysService.getWorkDay(erpDeliveryService.getStartTime()==null?new Date():erpDeliveryService.getStartTime(), shouldTrainTestDays);
            // 上门服务完成（首次营销策划服务）任务应该完成时间
			shouldVisitServiceTime = erpHolidaysService.getWorkDay(erpDeliveryService.getStartTime()==null?new Date():erpDeliveryService.getStartTime(), shouldVisitServiceDays);
			
			shouldMaterielTime =erpHolidaysService.getWorkDay(erpDeliveryService.getStartTime()==null?new Date():erpDeliveryService.getStartTime(), shouldMaterielServiceDays);
		}
		erpDeliveryService.setShouldTrainTestTime(shouldTrainTestTime);
		erpDeliveryService.setShouldVisitServiceTime(shouldVisitServiceTime);
		erpDeliveryService.setShouldMaterielTime(shouldMaterielTime);
		return erpDeliveryService;
	}

	/**
     * 保存启动时间 和 其他相关时间
     *
     * @param procInsId
     * @param serviceType
     * @date 2018年6月7日
     * @author linqunzhi
     */
	public void saveStartTimeOther(String procInsId, String serviceType, String zhctActType) {
		logger.info("saveStartTimeOther start | procInsId={}|serviceType={}", procInsId, serviceType);
		ErpDeliveryService erpDeliveryService = erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
		if (erpDeliveryService == null) {
            logger.error("交付服务信息不存在 ！procInsId={}", procInsId);
			throw new ServiceException(CommonConstants.FailMsg.DATA);
		}
        // 启动时间
		Date now = new Date();
        // 应完成交付时间
		Date shouldFlowEndTime = null;
		
		Date shouldTrainTestTime=null;
		
		Date shouldVisitServiceTime=null;
		
		Date shouldMaterielTime=null;
		
        // 聚引客应完成交付的工作日天数
		int juYinKeshouldFlowEndDays = 0;
        // 客常来应完成交付的工作日天数
		int keChangLaishouldFlowEndDays = 0;
        // 银联支付培训&测试（远程）任务应该完成的工作日天数
		int shouldTrainTestDays = 0;
        // 上门服务完成（首次营销策划服务）任务应该完成的工作日天数
		int shouldVisitServiceDays = 0;
		
		int shouldMaterielServiceDays = 0;
		
        // 获取工作日天数配置值
		String josnStr="";
		
		if(StringUtil.isBlank(serviceType)){
			if(DeliveryFlowConstant.SERVICE_TYPE_KE_ZHCT_OLD.equals(erpDeliveryService.getZhctType())){
				josnStr = sysConstantsService
						.getConstantValByKey(ErpDeliveryServiceConstants.DELIVERY_SERVICE_ZHCT_DAYS);
				DeliveryServiceWorkDays workDays = null;
				if (StringUtils.isNotBlank(josnStr)) {
					try {
						workDays = JSON.parseObject(josnStr, DeliveryServiceWorkDays.class);
					} catch (Exception e) {
                        logger.error("转换工作日错误", e);
					}
				}
				if (workDays != null) {
					juYinKeshouldFlowEndDays = workDays.getJuYingKe().getShouldFlowEndDays();
					shouldFlowEndTime = erpHolidaysService.getWorkDay(now, juYinKeshouldFlowEndDays);
					shouldTrainTestTime = erpHolidaysService.getWorkDay(now, juYinKeshouldFlowEndDays);
					shouldVisitServiceTime = erpHolidaysService.getWorkDay(now, juYinKeshouldFlowEndDays);
					shouldMaterielTime=erpHolidaysService.getWorkDay(now, juYinKeshouldFlowEndDays);
				}
			}
		}else{
			if(DeliveryFlowConstant.SERVICE_TYPE_JU_YIN_KE.equals(erpDeliveryService.getServiceType())
					|| DeliveryFlowConstant.SERVICE_TYPE_KE_CHANG_LAI.equals(erpDeliveryService.getServiceType())
					|| DeliveryFlowConstant.SERVICE_TYPE_JU_YIN_DATA.equals(erpDeliveryService.getServiceType())
					|| DeliveryFlowConstant.SERVICE_TYPE_KE_CHANG_LAI_BASIC.equals(erpDeliveryService.getServiceType())){
				josnStr = sysConstantsService
						.getConstantValByKey(ErpDeliveryServiceConstants.DELIVERY_SERVICE_WORK_DAYS);	
			}else if(DeliveryFlowConstant.SERVICE_TYPE_MU.equals(erpDeliveryService.getServiceType())){
				josnStr = sysConstantsService
						.getConstantValByKey(ErpDeliveryServiceConstants.DELIVERY_SERVICE_MATERIAL_DAYS);	
			}else if(DeliveryFlowConstant.SERVICE_TYPE_VC.equals(erpDeliveryService.getServiceType())){
				josnStr = sysConstantsService
						.getConstantValByKey(ErpDeliveryServiceConstants.DELIVERY_SERVICE_VC_DAYS);
			}
			DeliveryServiceWorkDays workDays = null;
			if (StringUtils.isNotBlank(josnStr)) {
				try {
					workDays = JSON.parseObject(josnStr, DeliveryServiceWorkDays.class);
				} catch (Exception e) {
                    logger.error("转换工作日错误", e);
				}
			}
			if (workDays != null) {
				if (workDays.getJuYingKe() != null && workDays.getJuYingKe().getShouldFlowEndDays() != null) {
					juYinKeshouldFlowEndDays = workDays.getJuYingKe().getShouldFlowEndDays();
				}
				if (workDays.getKeChangLai() != null && workDays.getKeChangLai().getShouldFlowEndDays() != null) {
					keChangLaishouldFlowEndDays = workDays.getKeChangLai().getShouldFlowEndDays();
				}
				if (workDays.getShouldTrainTestDays() != null) {
					shouldTrainTestDays = workDays.getShouldTrainTestDays();
				}
				if (workDays.getShouldVisitServiceDays() != null) {
					shouldVisitServiceDays = workDays.getShouldVisitServiceDays();
				}
				if (workDays.getShouldMaterielDays() != null) {
					shouldMaterielServiceDays = workDays.getShouldMaterielDays();
				}
			}
			if (DeliveryFlowConstant.SERVICE_TYPE_JU_YIN_KE.equals(serviceType)) {
				shouldFlowEndTime = erpHolidaysService.getWorkDay(now, juYinKeshouldFlowEndDays);
			} else if(DeliveryFlowConstant.SERVICE_TYPE_KE_CHANG_LAI.equals(serviceType)) {
				shouldFlowEndTime = erpHolidaysService.getWorkDay(now, keChangLaishouldFlowEndDays);
			} else if(DeliveryFlowConstant.SERVICE_TYPE_MU.equals(serviceType)){
				shouldFlowEndTime = erpHolidaysService.getWorkDay(now, keChangLaishouldFlowEndDays);
			} else if(DeliveryFlowConstant.SERVICE_TYPE_VC.equals(serviceType)){
				shouldFlowEndTime = erpHolidaysService.getWorkDay(now, keChangLaishouldFlowEndDays);
			} else if(DeliveryFlowConstant.SERVICE_TYPE_KE_CHANG_LAI_BASIC.equals(serviceType)){
				shouldFlowEndTime = erpHolidaysService.getWorkDay(now, keChangLaishouldFlowEndDays);
	        } else if(DeliveryFlowConstant.SERVICE_TYPE_JU_YIN_DATA.equals(serviceType)){
	        	shouldFlowEndTime = erpHolidaysService.getWorkDay(now, keChangLaishouldFlowEndDays);
	        } 
            // 银联支付培训&测试（远程）任务应该完成时间
			shouldTrainTestTime = erpHolidaysService.getWorkDay(now, shouldTrainTestDays);
            // 上门服务完成（首次营销策划服务）任务应该完成时间
			shouldVisitServiceTime = erpHolidaysService.getWorkDay(now, shouldVisitServiceDays);
			
			shouldMaterielTime =erpHolidaysService.getWorkDay(now, shouldMaterielServiceDays);
		}
		erpDeliveryService.setStartTime(now);
		erpDeliveryService.setShouldFlowEndTime(shouldFlowEndTime);
		erpDeliveryService.setShouldTrainTestTime(shouldTrainTestTime);
		erpDeliveryService.setShouldVisitServiceTime(shouldVisitServiceTime);
		erpDeliveryService.setShouldMaterielTime(shouldMaterielTime);
		erpDeliveryServiceService.save(erpDeliveryService);
 		logger.info("saveStartTimeOther end");
	}

	/*
     * protected void fillTaskInfo(String taskId) throws ParseException {
     * 
     * Act act = new Act(); Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
     * 
     * int taskHours = act.getTaskDateHours(); Date enddate =
     * erpHolidaysService.enddate(task.getTaskStarterDate(), taskHours); try {
     * act.setTaskBusinessEndDate(enddate); } catch (Exception e) {
     * act.setTaskBusinessEndDate(null); }
     * 
     * if (act.getTaskStarterDate() != null) { freemarkerMap.put("startDate",
     * DateUtils.formatDate(act.getTaskStarterDate(), "MM-dd HH:mm")); } if
     * (act.getTaskBusinessEndDate() != null) { freemarkerMap.put("endDate",
     * DateUtils.formatDate(act.getTaskBusinessEndDate(), "MM-dd HH:mm")); } else {
     * freemarkerMap.put("endDate", "未指定"); }
     * 
     * }
     * 
     */

    /**
     * 业务定义：交付服务流程标记完成
     * 
     * @date 2018年9月5日
     * @author R/Q
     */
    @Transactional
    public Map<String, Object> finishWorkFlow(String procInsId) {
        Map<String, Object> returnMap = Maps.newHashMap();
        logger.info("商户运营服务流程标记完成：procInsId={}", procInsId);
        ErpDeliveryService erpDeliveryService = erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
        try {
//        	setFinishedTime(erpDeliveryService);
        	erpDeliveryServiceService.updateTime1(erpDeliveryService);
            workFlowMonitorService.endProcess(procInsId);
            returnMap.put(CommonConstants.RETURN_CODE, CommonConstants.RETURN_CODE_SUCCESS);
        } catch (Exception e) {
            logger.error("商户运营服务流程重启操作出错。error={}", e);// NOSONAR
            returnMap.put(CommonConstants.RETURN_CODE, CommonConstants.RETURN_CODE_FAIL);
            returnMap.put(CommonConstants.RETURN_MESSAGE, CommonConstants.SYSTEM_ERROR_MESSAGE);
        }
        return returnMap;
    }
    
    private void setFinishedTime(ErpDeliveryService erpDeliveryService){
    	Date now = new Date();
        if(erpDeliveryService.getVisitServiceTime()==null){
        	erpDeliveryService.setVisitServiceTime(now);
        }
        if(erpDeliveryService.getMaterielTime()==null){
        	erpDeliveryService.setMaterielTime(now);
        }
        if(erpDeliveryService.getTrainTestTime()==null){
        	erpDeliveryService.setTrainTestTime(now);
        }
        if(erpDeliveryService.getShouldTrainTestTime()==null){
        	erpDeliveryService.setShouldTrainTestTime(erpDeliveryService.getShouldFlowEndTime());
        }
        if(erpDeliveryService.getShouldMaterielTime()==null){
        	erpDeliveryService.setShouldMaterielTime(erpDeliveryService.getShouldFlowEndTime());
        }
        if(erpDeliveryService.getShouldVisitServiceTime()==null){
        	erpDeliveryService.setShouldVisitServiceTime(erpDeliveryService.getShouldFlowEndTime());
        }
        erpDeliveryServiceService.save(erpDeliveryService);
    }

    /**
     * 业务定义：重启工作流
     * 
     * @date 2018年8月30日
     * @author R/Q
     */
    @Transactional
    public Map<String, Object> resetWorkFlow(String procInsId) {
        Map<String, Object> returnMap = Maps.newHashMap();
        logger.info("交付流程重启：procInsId={}", procInsId);// NOSONAR
        try {
            String exeId = this.getSingleExecutionId(procInsId);
            Map<String, Object> vars = runtimeService.getVariables(exeId);// 获取流程参数
            this.initVariable(vars);// 初始化流程参数
            ErpDeliveryService erpDelivery = erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);// 获取交付服务信息
            runtimeService.setVariable(exeId, DeliveryFlowConstant.RESET_FLAG, UserUtils.getUser().getName());// 流程参数添加重启标识
            workFlowMonitorService.endProcess(procInsId);
            String procDefKey = StringUtils.equals(erpDelivery.getZhctType(),
                            DeliveryFlowConstant.SERVICE_TYPE_KE_ZHCT_OLD) ? ActUtils.VISIT_SERVICE_ZHCT_FLOW : ActUtils.DELIVERY_SERVICE_FLOW[0];// 依据serviceType判断启动对应流程图实例
            String newProcInsId = actTaskService.startProcess(procDefKey, ActUtils.DELIVERY_SERVICE_FLOW[1], erpDelivery.getId(), "启动运营服务流程", vars);// 启动新流程
            // 通知重启
            serviceMessageService.restartFlow(procInsId);
            // 进度重启
            serviceProgressService.restartFlow(newProcInsId, procInsId);
            erpDelivery.setProcInsId(newProcInsId);// 更新流程实例ID
            erpDelivery=setDoingTime(erpDelivery);
            this.initOrderData(procInsId, newProcInsId);// 初始化业务数据
            erpDeliveryServiceService.updateTime(erpDelivery);
            returnMap.put(CommonConstants.RETURN_CODE, CommonConstants.RETURN_CODE_SUCCESS);
            logger.info("交付流程重启成功：newProcInsId={}", newProcInsId);// NOSONAR
        } catch (Exception e) {
            logger.error("交付流程流程重启操作出错。error={}", e);// NOSONAR
            returnMap.put(CommonConstants.RETURN_CODE, CommonConstants.RETURN_CODE_FAIL);
            returnMap.put(CommonConstants.RETURN_MESSAGE, CommonConstants.SYSTEM_ERROR_MESSAGE);
        }
        return returnMap;
    }

    private ErpDeliveryService setDoingTime(ErpDeliveryService erpDelivery){
    	erpDelivery.setFlowEndTime(null);
        erpDelivery.setShouldFlowEndTime(null);
        erpDelivery.setVisitServiceTime(null);
        erpDelivery.setShouldVisitServiceTime(null);
        erpDelivery.setMaterielTime(null);
        erpDelivery.setShouldMaterielTime(null);
        erpDelivery.setTrainTestTime(null);
        erpDelivery.setShouldTrainTestTime(null);
        return erpDelivery;
    }
    
    /**
     * 
     * 业务定义：根据流程实例ID获取ExecutionID
     * 
     * @date 2018年9月10日
     * @author R/Q
     */
    private String getSingleExecutionId(String procInsId) {
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(procInsId).list();
        if (CollectionUtils.isEmpty(tasks)) {
            logger.error("无对应Task信息。procInsId={}", procInsId);// NOSONAR
            throw new ServiceException();
        }
        List<Execution> executions = runtimeService.createExecutionQuery().executionId(tasks.get(0).getExecutionId()).list();
        if (CollectionUtils.isEmpty(executions)) {
            logger.error("无对应Execution信息。procInsId={}", procInsId);// NOSONAR
            throw new ServiceException();
        }
        return executions.get(0).getId();
    }

    /**
     * 业务定义：初始化流程中被变更的流程参数
     * 
     * @date 2018年9月11日
     * @author R/Q
     */
    private void initVariable(Map<String, Object> vars) {
        // 初始化上门服务类型
        vars.put(DeliveryFlowConstant.VISIT_TYPE, DeliveryFlowConstant.VISIT_TYPE_FMPS_I);
        // ..
    }

    /**
     * 业务定义：初始化订单相关业务数据
     * 
     * @date 2018年9月12日
     * @author R/Q
     */
    private void initOrderData(String oldProcInsId, String newProcInsId) {
        // 初始化物料信息
        ErpOrderMaterialCreation erpOrderMaterialCreation = erpOrderMaterialCreationService.getByProcInsId(oldProcInsId);
        if (erpOrderMaterialCreation != null) {
            erpOrderMaterialCreation.setProcInsId(newProcInsId);
            erpOrderMaterialCreationService.save(erpOrderMaterialCreation);
        }
        // 将终止的流程处理人复制到重启的流程中
        erpOrderFlowUserService.copyFlowUsers(oldProcInsId, newProcInsId);
        // 复制服务项关联数据
        erpOrderGoodServiceInfoService.copyServiceItemLink(oldProcInsId, newProcInsId);
        // 重置上门服务数据
        erpVisitServiceInfoService.resetByProcInsId(oldProcInsId, newProcInsId);
        // 删除上门服务评价数据
        erpServiceAcceptanceService.deleteByProcInsId(oldProcInsId);
    }

}
