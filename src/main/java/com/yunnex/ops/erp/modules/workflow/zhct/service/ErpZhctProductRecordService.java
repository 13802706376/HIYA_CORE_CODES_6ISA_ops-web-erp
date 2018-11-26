package com.yunnex.ops.erp.modules.workflow.zhct.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.activiti.editor.language.json.converter.util.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.persistence.BaseEntity;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;
import com.yunnex.ops.erp.modules.workflow.flow.constant.DeliveryFlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowFormConstant;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpFlowForm;
import com.yunnex.ops.erp.modules.workflow.flow.service.ErpFlowFormService;
import com.yunnex.ops.erp.modules.workflow.zhct.constant.ZhctOldShopFlowConstants;
import com.yunnex.ops.erp.modules.workflow.zhct.dao.ErpZhctProductRecordDao;
import com.yunnex.ops.erp.modules.workflow.zhct.dto.ZhctOpenConditionDto;
import com.yunnex.ops.erp.modules.workflow.zhct.entity.ErpZhctProductRecord;

/**
 * 智慧餐厅产品信息Service
 * @author yunnex
 * @version 2018-08-28
 */
@Service
public class ErpZhctProductRecordService extends CrudService<ErpZhctProductRecordDao, ErpZhctProductRecord> {
	@Autowired
	private ErpFlowFormService erpFlowFormService;

    @Override
	@Transactional(readOnly = false)
	public void save(ErpZhctProductRecord erpZhctProductRecord) {
		super.save(erpZhctProductRecord);
	}

    @Override
	@Transactional(readOnly = false)
	public void delete(ErpZhctProductRecord erpZhctProductRecord) {
		super.delete(erpZhctProductRecord);
	}

    /**
        *  查询智慧餐厅的开通条件
     * @param procInsId
     * @return
     */
	public JSONObject findZhctOpenConditionByProcInsId(String procInsId) {
		JSONObject object = new JSONObject();
		if(StringUtils.isBlank(procInsId)) {
			return object;
		}
		
		//获取商户店内是否有打印机
		String isShopPrinterID = erpFlowFormService.findByProcessIdAndAttrName(procInsId, FlowFormConstant.IS_SHOP_PRINTER_ID);
		object.put("isShopPrinterID", isShopPrinterID);
		object.put("printerNum", "");
		object.put("printerInfoJson", "");
		if("Y".equals(isShopPrinterID)){
			ErpZhctProductRecord zhctProductRecordList = this.dao.findByProcInsIdAndType(procInsId, ZhctOldShopFlowConstants.PRINTER);
			if(zhctProductRecordList!=null) {
				object.put("printerNum", zhctProductRecordList.getProdNum());
				object.put("printerInfoJson", zhctProductRecordList.getProdInfoJson());
			}
		}
		
		//店内交换机接口是否足够连接打印机
		String isConnectPrinter = erpFlowFormService.findByProcessIdAndAttrName(procInsId, FlowFormConstant.IS_SHOP_CONNECT_PRINTER_ID);
		object.put("isConnectPrinter", isConnectPrinter);
		
		//商户店内是否有收银机
		String isShopCashRegisterID = erpFlowFormService.findByProcessIdAndAttrName(procInsId, FlowFormConstant.IS_SHOP_CASH_REGISTER_ID);
		object.put("isShopCashRegisterID", isShopCashRegisterID);
		object.put("cashRegisterNum", "");
		object.put("cashRegisterInfoJson", "");
		if("Y".equals(isShopCashRegisterID)){
			ErpZhctProductRecord zhctProductRecordList = this.dao.findByProcInsIdAndType(procInsId, ZhctOldShopFlowConstants.CASH_REGISTER);
			if(zhctProductRecordList!=null) {
				object.put("cashRegisterNum", zhctProductRecordList.getProdNum());
				object.put("cashRegisterInfoJson", zhctProductRecordList.getProdInfoJson());
			}
		}
		
		//安装智慧餐厅客户端的电脑是否可以联网
		String isConnectInterID = erpFlowFormService.findByProcessIdAndAttrName(procInsId, FlowFormConstant.IS_CONNECT_INTERNET_ID);
		object.put("isConnectInterID",isConnectInterID);
		
		//商户店内布线及桌台场景
		String shopDesc = erpFlowFormService.findByProcessIdAndAttrName(procInsId, FlowFormConstant.SHOP_DESC);
		object.put("shopDesc",shopDesc);
		return object;
	}
	
	
	
	@Transactional(readOnly=false)
	public void saveOrUpdateZhctOpenCondition(ZhctOpenConditionDto dto,String taskId,String procInsId,String orderId ,String shopInfoId,String node) {
		if(null==dto) {
			return;
		}
		
		//根据流程id查询流程所有的表单属性名
	    List<String> formAttrNames = erpFlowFormService.findFormAttrNamesByProcInsId(procInsId);
	    
		//批量保存流程表单数据
		List<ErpFlowForm> batchInsertData = new ArrayList<>();
		ErpFlowForm flowForm=null;
		
		//商户店内是否有打印机
		String isShopPrinter = dto.getIsShopPrinter();
		if(formAttrNames.contains(FlowFormConstant.IS_SHOP_PRINTER_ID)) {
			flowForm = new ErpFlowForm();
			flowForm.preUpdate();
			flowForm.setFormAttrValue(isShopPrinter);
			erpFlowFormService.updateErpFlowForm(procInsId,FlowFormConstant.IS_SHOP_PRINTER_ID,flowForm);
		}else {
			if(StringUtils.isNotBlank(isShopPrinter)) {
				if(node!=null&&node.contains("old")) {
					flowForm = new ErpFlowForm(taskId,orderId, procInsId, ZhctOldShopFlowConstants.VISIT_SERVICE_SUBSCRIBE_ZHCT_OLD, 
							FlowConstant.FLOW_FORM_DATA_ATTR_TYPE_NORMAL, FlowFormConstant.IS_SHOP_PRINTER_ID, isShopPrinter, "");
				}else {
					flowForm = new ErpFlowForm(taskId,orderId, procInsId, DeliveryFlowConstant.TELEPHONE_CONFIRM_SERVICE_ZHCT, 
							FlowConstant.FLOW_FORM_DATA_ATTR_TYPE_NORMAL, FlowFormConstant.IS_SHOP_PRINTER_ID, isShopPrinter, "");
				}
				flowForm.preInsert();
				batchInsertData.add(flowForm);
			}
		}
		
		//保存打印机信息
		saveOrUpdateZhctProductInfo(isShopPrinter,ZhctOldShopFlowConstants.PRINTER, shopInfoId, procInsId, dto.getPrinterInfoJson(),dto.getPrinterNum());
		
		//店内交换机接口是否够连接打印机
		String isConnectPrinter =dto.getIsConnectPrinter();
		if(formAttrNames.contains(FlowFormConstant.IS_SHOP_CONNECT_PRINTER_ID)) {
			flowForm = new ErpFlowForm();
			flowForm.preUpdate();
			flowForm.setFormAttrValue(isConnectPrinter);
			erpFlowFormService.updateErpFlowForm(procInsId,FlowFormConstant.IS_SHOP_CONNECT_PRINTER_ID,flowForm);
		}else {
			if(StringUtils.isNotBlank(isConnectPrinter)) {
				if(node!=null&&node.contains("old")) {
					flowForm = new ErpFlowForm(taskId,orderId, procInsId, ZhctOldShopFlowConstants.VISIT_SERVICE_SUBSCRIBE_ZHCT_OLD, 
							FlowConstant.FLOW_FORM_DATA_ATTR_TYPE_NORMAL, FlowFormConstant.IS_SHOP_CONNECT_PRINTER_ID, isConnectPrinter, "");
				}else {
					flowForm = new ErpFlowForm(taskId, orderId, procInsId, DeliveryFlowConstant.TELEPHONE_CONFIRM_SERVICE_ZHCT, 
							FlowConstant.FLOW_FORM_DATA_ATTR_TYPE_NORMAL, FlowFormConstant.IS_SHOP_CONNECT_PRINTER_ID, isConnectPrinter, "");
				}
				flowForm.preInsert();
				batchInsertData.add(flowForm);
			}
		}
		
		//商户店内是否有收银机
		String isShopCashRegister = dto.getIsShopCashRegister();
		if(formAttrNames.contains(FlowFormConstant.IS_SHOP_CASH_REGISTER_ID)) {
			flowForm = new ErpFlowForm();
			flowForm.preUpdate();
			flowForm.setFormAttrValue(isShopCashRegister);
			erpFlowFormService.updateErpFlowForm(procInsId,FlowFormConstant.IS_SHOP_CASH_REGISTER_ID,flowForm);
		}else {
			if(StringUtils.isNotBlank(isShopCashRegister)) {
				if(node!=null&&node.contains("old")) {
					flowForm = new ErpFlowForm(taskId,orderId, procInsId, ZhctOldShopFlowConstants.VISIT_SERVICE_SUBSCRIBE_ZHCT_OLD, 
							FlowConstant.FLOW_FORM_DATA_ATTR_TYPE_NORMAL, FlowFormConstant.IS_SHOP_CASH_REGISTER_ID, isShopCashRegister, "");
				}else {
					flowForm = new ErpFlowForm(taskId, orderId, procInsId, DeliveryFlowConstant.TELEPHONE_CONFIRM_SERVICE_ZHCT, 
							FlowConstant.FLOW_FORM_DATA_ATTR_TYPE_NORMAL, FlowFormConstant.IS_SHOP_CASH_REGISTER_ID, isShopCashRegister, "");
				}
				
				flowForm.preInsert();
				batchInsertData.add(flowForm);
			}
		}
		
		//保存收银机信息
		saveOrUpdateZhctProductInfo(isShopCashRegister,ZhctOldShopFlowConstants.CASH_REGISTER, shopInfoId, procInsId, dto.getCashRegisterInfoJson(),dto.getCashRegisterNum());
		
	    //安装智慧餐厅客户端的电脑是否可以联网
	    String isConnectInternet = dto.getIsConnectInternet();
	    if(formAttrNames.contains(FlowFormConstant.IS_CONNECT_INTERNET_ID)) {
	    	flowForm = new ErpFlowForm();
	    	flowForm.preUpdate();
	    	flowForm.setFormAttrValue(isConnectInternet);
			erpFlowFormService.updateErpFlowForm(procInsId,FlowFormConstant.IS_CONNECT_INTERNET_ID,flowForm);
		}else {
			if(StringUtils.isNotBlank(isConnectInternet)) {
				if(node!=null&&node.contains("old")) {
					flowForm = new ErpFlowForm(taskId,orderId, procInsId, ZhctOldShopFlowConstants.VISIT_SERVICE_SUBSCRIBE_ZHCT_OLD, 
							FlowConstant.FLOW_FORM_DATA_ATTR_TYPE_NORMAL, FlowFormConstant.IS_CONNECT_INTERNET_ID, isConnectInternet, "");
				}else {
					flowForm = new ErpFlowForm(taskId, orderId, procInsId, DeliveryFlowConstant.TELEPHONE_CONFIRM_SERVICE_ZHCT, FlowConstant.FLOW_FORM_DATA_ATTR_TYPE_NORMAL,
							FlowFormConstant.IS_CONNECT_INTERNET_ID, isConnectInternet, "");
				}
			
				flowForm.preInsert();
				batchInsertData.add(flowForm);
			}
		}
	    
	    //商户店内布线及桌台场景
	    String shopDesc = dto.getShopDesc();
	    if(formAttrNames.contains(FlowFormConstant.SHOP_DESC)) {
	    	flowForm = new ErpFlowForm();
	    	flowForm.preUpdate();
	    	flowForm.setFormTextValue(shopDesc);
			erpFlowFormService.updateErpFlowForm(procInsId,FlowFormConstant.SHOP_DESC,flowForm);
		}else {
			if(StringUtils.isNotBlank(shopDesc)) {
				if(node!=null&&node.contains("old")) {
					flowForm = new ErpFlowForm(taskId,orderId, procInsId, ZhctOldShopFlowConstants.VISIT_SERVICE_SUBSCRIBE_ZHCT_OLD, 
							FlowConstant.FLOW_FORM_DATA_ATTR_TYPE_TEXT, FlowFormConstant.SHOP_DESC, "", shopDesc);
				}else {
					flowForm = new ErpFlowForm(taskId, orderId, procInsId, DeliveryFlowConstant.TELEPHONE_CONFIRM_SERVICE_ZHCT,
							FlowConstant.FLOW_FORM_DATA_ATTR_TYPE_TEXT, FlowFormConstant.SHOP_DESC, "", shopDesc);
				}
				
				flowForm.preInsert();
				batchInsertData.add(flowForm);
			}
		}
	    
	    //批量保存流程表单数据
	    erpFlowFormService.batchInsert(batchInsertData);
	}
	
	
	
	private void saveOrUpdateZhctProductInfo(String hasFlag,String prodType,String shopInfoId,String procInsId,String prodInfoJson,String printerNum) {
		if(StringUtils.isBlank(hasFlag) 
				|| StringUtils.isBlank(prodType)
				|| StringUtils.isBlank(shopInfoId)
				|| StringUtils.isBlank(procInsId)) {
			return;
		}
		
		ErpZhctProductRecord zhctProductRecord = dao.findByProcInsIdAndType(procInsId, prodType);
		if(zhctProductRecord!=null) {
			zhctProductRecord.setProdNum(printerNum);
			zhctProductRecord.setProdInfo(prodInfoJson);
			this.save(zhctProductRecord);
		}else {
			if(Constant.YES.equals(hasFlag)) {
				//保存打印机信息
				ErpZhctProductRecord entity = new ErpZhctProductRecord();
				entity.setProcInsId(procInsId);
				entity.setShopInfoId(shopInfoId);
				entity.setProdNum(printerNum);
				entity.setProdInfo(prodInfoJson);
				entity.setProdType(prodType);
				this.save(entity);
			}
		}
	}
}