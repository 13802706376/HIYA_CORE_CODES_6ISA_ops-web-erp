package com.yunnex.ops.erp.modules.workflow.flow.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.service.BaseService;
import com.yunnex.ops.erp.common.utils.AESUtil;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.order.service.ErpOrderOriginalInfoService;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopActualLinkman;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopMallForm;
import com.yunnex.ops.erp.modules.shop.pay.entity.ErpShopPayAlipa;
import com.yunnex.ops.erp.modules.shop.pay.service.ErpShopPayAlipaService;
import com.yunnex.ops.erp.modules.shop.service.ErpShopActualLinkmanService;
import com.yunnex.ops.erp.modules.shop.service.ErpShopInfoService;
import com.yunnex.ops.erp.modules.shop.service.ErpShopMallFormService;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreInfoService;
import com.yunnex.ops.erp.modules.store.pay.entity.ErpStorePayUnionpay;
import com.yunnex.ops.erp.modules.store.pay.entity.ErpStorePayWeixin;
import com.yunnex.ops.erp.modules.store.pay.service.ErpStorePayUnionpayService;
import com.yunnex.ops.erp.modules.store.pay.service.ErpStorePayWeixinService;
import com.yunnex.ops.erp.modules.sys.constant.RoleConstant;
import com.yunnex.ops.erp.modules.sys.entity.ServiceOperation;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.service.ServiceOperationService;
import com.yunnex.ops.erp.modules.sys.service.SystemService;
import com.yunnex.ops.erp.modules.sys.service.UserService;
import com.yunnex.ops.erp.modules.workflow.delivery.entity.ErpDeliveryService;
import com.yunnex.ops.erp.modules.workflow.delivery.service.ErpDeliveryServiceService;
import com.yunnex.ops.erp.modules.workflow.flow.common.JykFlowConstants;
import com.yunnex.ops.erp.modules.workflow.flow.constant.DeliveryFlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.dto.rep.OrderInfoFlowRepDto;
import com.yunnex.ops.erp.modules.workflow.flow.dto.rep.ZhangBeiFlowRepDto;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpFlowForm;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderFile;
import com.yunnex.ops.erp.modules.workflow.user.entity.ErpOrderFlowUser;
import com.yunnex.ops.erp.modules.workflow.user.service.ErpOrderFlowUserService;

@Service
public class DeliveryFlowDetailsService extends BaseService{
    @Autowired
    ErpOrderFlowUserService erpOrderFlowUserService;
    @Autowired
    ErpDeliveryServiceService erpDeliveryServiceService;
    @Autowired
    private ErpShopActualLinkmanService linkmanService;
    @Autowired
    private ErpShopInfoService erpShopInfoService;
    @Autowired
    private ErpFlowFormService erpFlowFormService;
    @Autowired
    private ErpStorePayWeixinService wxpayService;
    @Autowired
    private ErpShopPayAlipaService erpShopPayAlipaService;
    @Autowired
    private ErpOrderOriginalInfoService erpOrderOriginalInfoService;
    @Autowired
    private ErpStoreInfoService erpStoreInfoService;
    @Autowired
    private SystemService systemService;
    @Autowired
    private ErpOrderFileService erpOrderFileService;
    @Autowired
    private UserService userService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private  ErpStorePayUnionpayService erpStorePayUnionpayService;
    @Autowired
    private ServiceOperationService serviceOperationService;
    @Autowired
    private ErpShopMallFormService erpShopMallFormService;
    /**
     * 查询订单详情信息
     */
    public JSONObject getOrderInfo(String taskId,String procInsId) {
        JSONObject resObject = new JSONObject();
        ErpDeliveryService erpDeliveryService =erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
        ErpOrderOriginalInfo orderOriginalInfo =erpOrderOriginalInfoService.get(erpDeliveryService.getOrderId());
        setOrderInfoDto(orderOriginalInfo, resObject,taskId,procInsId);
        ServiceOperation serviceOperation =serviceOperationService.getByServiceNo(orderOriginalInfo.getAgentId()+"");
        List<User> operationAdviserUserList=null;
        List<User> openAccountConsultantUserList=null;
        List<User> materialConsultantUserList =null;
        String opsAdviserRole;
        if(serviceOperation==null){
            opsAdviserRole = RoleConstant.OPS_ADVISER_AGENT;
            openAccountConsultantUserList = userService.getUserByRoleNameAndAgentId(RoleConstant.ACCOUNT_ADVISER_AGENT, orderOriginalInfo.getAgentId());
            materialConsultantUserList = userService.getUserByRoleNameAndAgentId(RoleConstant.MATERIALADVISER_AGENT, orderOriginalInfo.getAgentId());
        }else{
            opsAdviserRole = RoleConstant.OPS_ADVISER;
            openAccountConsultantUserList= systemService.getUserByRoleName(RoleConstant.ACCOUNT_ADVISER);
            materialConsultantUserList= systemService.getUserByRoleName(RoleConstant.MATERIALADVISER);
        }
        // 获取指定服务商运营顾问列表
        if (Constant.NEGATIVE_ONE != orderOriginalInfo.getAgentId()) {
            operationAdviserUserList = userService.findRoleUsersByAgent(orderOriginalInfo.getAgentId(), opsAdviserRole);
        }
        resObject.put("operationAdviser", operationAdviserUserList);
        resObject.put("openAccountConsultant", openAccountConsultantUserList);
        resObject.put("materialConsultant", materialConsultantUserList);
        resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
        return resObject;
    }
    public void setOrderInfoDto( ErpOrderOriginalInfo orderOriginalInfo,JSONObject resObject,String taskId,String procInsId ){
        OrderInfoFlowRepDto orderRepdto=new OrderInfoFlowRepDto();
        orderRepdto.setOrderId(orderOriginalInfo.getId());
        orderRepdto.setOrderNo(orderOriginalInfo.getOrderNumber());
        orderRepdto.setShopName(orderOriginalInfo.getShopName());
        orderRepdto.setShopAbbreviation(orderOriginalInfo.getShopAbbreviation());
        orderRepdto.setBuyDate(orderOriginalInfo.getBuyDate());
        orderRepdto.setContactName(orderOriginalInfo.getContactName());
        orderRepdto.setContactPhone(orderOriginalInfo.getContactNumber());
        orderRepdto.setSalePhone(orderOriginalInfo.getPromotePhone());
        orderRepdto.setSaleName(orderOriginalInfo.getPromoteContact());

        // 根据服务类型编号分别设置服务类型名称以及服务项名称
        String serviceType = (String) taskService.getVariable(taskId, FlowConstant.SERVICETYPE);
        String zhctActType = (String) taskService.getVariable(taskId, DeliveryFlowConstant.ZHCT_ACT_TYPE);
        if(DeliveryFlowConstant.SERVICE_TYPE_KE_CHANG_LAI.equals(serviceType)){
            orderRepdto.setServiceItems("智能客流运营全套落地服务");
            orderRepdto.setServiceType("常客来");
        }else if(DeliveryFlowConstant.SERVICE_TYPE_JU_YIN_KE.equals(serviceType)){
            orderRepdto.setServiceItems("聚引客交付服务");
            orderRepdto.setServiceType("聚引客");
        }else if(DeliveryFlowConstant.SERVICE_TYPE_MU.equals(serviceType)){
            orderRepdto.setServiceItems("物料更新服务");
            orderRepdto.setServiceType("常客来");
        }else if(DeliveryFlowConstant.SERVICE_TYPE_VC.equals(serviceType)){
            orderRepdto.setServiceItems("售后上门培训付费服务");
            orderRepdto.setServiceType("常客来");
        }else if(DeliveryFlowConstant.SERVICE_TYPE_KE_CHANG_LAI_BASIC.equals(serviceType)){
            orderRepdto.setServiceItems("首次上门服务（基础版）");
            orderRepdto.setServiceType("常客来");
        } else if (DeliveryFlowConstant.SERVICE_TYPE_JU_YIN_DATA.equals(serviceType)) {
            orderRepdto.setServiceItems("掌贝平台交付服务");
            orderRepdto.setServiceType("客常来");
        }else if ("".equals(serviceType)&&DeliveryFlowConstant.SERVICE_TYPE_KE_ZHCT_OLD.equals(zhctActType)) {
            orderRepdto.setServiceItems("智慧餐厅安装交付服务");
            orderRepdto.setServiceType("智慧餐厅");
        }else{
            orderRepdto.setServiceItems("未知");
            orderRepdto.setServiceType("未知");
        }
        
        //智慧餐厅
        String zhctFlag = (String) taskService.getVariable(taskId, DeliveryFlowConstant.ZHCT_FLAG);
        if(Constant.YES.equals(zhctFlag)) {
        	orderRepdto.setServiceItems(orderRepdto.getServiceItems().concat("<br>智慧餐厅安装交付服务"));
        	orderRepdto.setServiceType(orderRepdto.getServiceType().concat("、智慧餐厅"));
        }
        
        if("Y".equals(orderOriginalInfo.getIsNewShop())){
           orderRepdto.setShopType("新商户"); 
        }else{
           orderRepdto.setShopType("旧商户"); 
        }
        resObject.put("orederInfo", orderRepdto);
    }
   
   
    /**
     *电话联系商户,确认服务内容 节点  进件资料收集节点
     *
     * @param taskId
     * @param procInsId
     * @return
     * @date 2018年5月26日
     */
    public JSONObject getFlowOrderAndShopInfo(String taskId,String procInsId) {
       Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        JSONObject resObject = new JSONObject();
        ErpDeliveryService erpDeliveryService =erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
        ErpOrderOriginalInfo orderOriginalInfo =erpOrderOriginalInfoService.get(erpDeliveryService.getOrderId());
        ErpShopInfo shopInfo=  erpShopInfoService.getByZhangbeiID(erpDeliveryService.getShopId());
        if(null==shopInfo||StringUtils.isBlank(shopInfo.getZhangbeiId())){
            resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_FALSE);
            resObject.put(FlowConstant.MESSAGE, "掌贝id不存在");
            return resObject; 
        }
        setOrderInfoDto(orderOriginalInfo, resObject,taskId,procInsId);
       //流程负责人
        ErpOrderFlowUser flowUserOperationAdviser =erpOrderFlowUserService.findListByFlowId(procInsId,JykFlowConstants.OPERATION_ADVISER); 
        if(flowUserOperationAdviser!=null){
            User user= userService.get(flowUserOperationAdviser.getUser());
            resObject.put("operationAdviser",user!=null?user.getName():""); 
        }
        ErpOrderFlowUser flowUserOpenAccountConsultant =erpOrderFlowUserService.findListByFlowId(procInsId,JykFlowConstants.ACCOUNT_ADVISER);
        if(flowUserOpenAccountConsultant!=null){
            User user= userService.get(flowUserOpenAccountConsultant.getUser());
            resObject.put("openAccountConsultant", user!=null?user.getName():"");
        }
        ErpOrderFlowUser flowUserMaterialConsultant =erpOrderFlowUserService.findListByFlowId(procInsId, JykFlowConstants.MATERIAL_ADVISER);
        if(flowUserMaterialConsultant!=null){
            User user= userService.get(flowUserMaterialConsultant.getUser());
            resObject.put("materialConsultant", user!=null?user.getName():"");  
        }
        //商户联系人
        if(shopInfo!=null){
            List<ErpShopActualLinkman> linkmanList = linkmanService.findShopLinmanByShopId(Global.NO, shopInfo.getId());  
            resObject.put("shopLinkmanList", linkmanList);  
        }
        Map<String, Object> map = Maps.newHashMap();
       if(task.getTaskDefinitionKey().startsWith("telephone_confirm_service")){
           fillFrameMarkerMap(map, erpFlowFormService.findByProcessIdAndTask(procInsId,"telephone_confirm_service"));
           resObject.put("flowdata", map);
       }
        resObject.put("weixinGroup",orderOriginalInfo.getShopAbbreviation()+"掌贝运营"+(int)((Math.random()*9+1)*10000));  
        if(task.getTaskDefinitionKey().startsWith("into_material_collection")){
            resObject.put("zhangbeiNo",shopInfo.getZhangbeiId());  
            String zhangbeiID = shopInfo.getZhangbeiId();
            if(zhangbeiID.length() < 6){
                // 不足6位的情况，在末尾补'0'
                zhangbeiID = StringUtils.rightPad(zhangbeiID, 6, '0');
            }
            String passWord =zhangbeiID.substring(zhangbeiID.length()-6, zhangbeiID.length());
            resObject.put("zhangbeiPassword",passWord);  
        }
        resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
        return resObject;
    }
    
    /**
     *掌贝信息
     *
     * @param taskId
     * @param procInsId
     * @return
     * @date 2018年5月26日
     */
    public JSONObject getFlowzhangBeiInfo(String taskId,String procInsId) {
        JSONObject resObject = new JSONObject();
        ErpDeliveryService erpDeliveryService =erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
        ErpShopInfo shopInfo=  erpShopInfoService.getByZhangbeiID(erpDeliveryService.getShopId());
        ZhangBeiFlowRepDto zhangBeidto=new ZhangBeiFlowRepDto();
        zhangBeidto.setShopInfoId(shopInfo.getId());
        zhangBeidto.setZhangbeiId(shopInfo.getZhangbeiId());
        if(null==shopInfo||StringUtils.isBlank(shopInfo.getZhangbeiId())){
            resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_FALSE);
            resObject.put(FlowConstant.MESSAGE, "掌贝id不存在！");
            return resObject; 
        }
        String zhangbeiID = shopInfo.getZhangbeiId();
        if(zhangbeiID.length() < 6){
            // 不足6位的情况，在末尾补'0'
            zhangbeiID = StringUtils.rightPad(zhangbeiID, 6, '0');
        }
        String passWord =zhangbeiID.substring(zhangbeiID.length()-6, zhangbeiID.length());
        zhangBeidto.setZhangbeiPassword(passWord);
        zhangBeidto.setZhangbeiRemark(shopInfo.getZhangbeiRemark());
        zhangBeidto.setZhangbeiState(shopInfo.getZhangbeiState());
        resObject.put("zhangbeiInfo",zhangBeidto); 
        resObject.put("orderId", erpDeliveryService.getOrderId());
        resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
        return resObject;
    }
    
    
    /**
     *微信相关的信息
     *
     * @param taskId
     * @param procInsId
     * @return
     * @date 2018年5月26日
     */
    public JSONObject getFlowWeiXinInfo(String taskId,String procInsId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        JSONObject resObject = new JSONObject();
        ErpDeliveryService erpDeliveryService =erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
        ErpShopInfo shop = erpShopInfoService.getByZhangbeiID(erpDeliveryService.getShopId());
        if(task.getTaskDefinitionKey().startsWith("public_number_open")||task.getTaskDefinitionKey().startsWith("wechat_shop_configuration")){
            ErpStoreInfo erpStoreInfo=  erpStoreInfoService.findismain(shop.getId(), Global.NO);

            if(null!=shop){
               ErpStorePayWeixin wxpay = wxpayService.get(erpStoreInfo.getWeixinPayId());
               if(null!=wxpay&&StringUtils.isNotBlank(wxpay.getPublicAccountPassword())){
                   wxpay.setPublicAccountPassword(AESUtil.decrypt(wxpay.getPublicAccountPassword()));
               }
               resObject.put("publicAccountNoInfo", wxpay);
               resObject.put("shopName", shop.getName());
           }
           Map<String, Object> map = Maps.newHashMap();
           fillFrameMarkerMap(map, erpFlowFormService.findByProcessIdAndTask(procInsId,"public_number_open"));
           resObject.put("flowdata", map);
        }
       if(task.getTaskDefinitionKey().startsWith("wechat_account_open")){
           ErpStoreInfo erpStoreInfo=  erpStoreInfoService.findismain(shop.getId(), Global.NO);
           ErpStorePayWeixin weixinPay=null;
           if(null!=erpStoreInfo && StringUtils.isNotBlank(erpStoreInfo.getWeixinPayId())){
               weixinPay = wxpayService.get(erpStoreInfo.getWeixinPayId());   
           }
           resObject.put("wechatpayState", weixinPay!=null?weixinPay.getAuditStatus():0);
           resObject.put("wechatpayRemark",weixinPay!=null? weixinPay.getAuditContent():"");
       }
        resObject.put("shopInfoId", shop.getId());
        resObject.put("orderId", erpDeliveryService.getOrderId());
        resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
        return resObject;
    }
    
    
    /**
     *支付宝口碑
     *
     * @param taskId
     * @param procInsId
     * @return
     * @date 2018年5月26日
     */
    public JSONObject getFlowAliPaInfo(String taskId,String procInsId) {
        JSONObject resObject = new JSONObject();
        ErpDeliveryService erpDeliveryService =erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
        ErpShopInfo shop = erpShopInfoService.getByZhangbeiID(erpDeliveryService.getShopId());
        ErpShopPayAlipa erpShopPayAlipa =erpShopPayAlipaService.getShopAilpaInfoByShopInfoId(shop.getId());
        resObject.put("ailipaInfo", erpShopPayAlipa);
        Map<String, Object> map = Maps.newHashMap();
        fillFrameMarkerMap(map, erpFlowFormService.findByProcessIdAndTask(procInsId,"alipay_public_praise_apply"));
        resObject.put("flowdata", map);
        resObject.put("orderId", erpDeliveryService.getOrderId());
        resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
        return resObject;
    }
    
    /**
     *银联支付信息
     *
     * @param taskId
     * @param procInsId
     * @return
     * @date 2018年5月26日
     */
    public JSONObject getFlowUnionpayInfo(String taskId,String procInsId) {
        JSONObject resObject = new JSONObject();
        ErpDeliveryService erpDeliveryService =erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
        ErpShopInfo shop = erpShopInfoService.getByZhangbeiID(erpDeliveryService.getShopId());
        ErpStoreInfo erpStoreInfo=  erpStoreInfoService.findismain(shop.getId(), Global.NO);
        ErpStorePayUnionpay unionPay =null;
        if(null!=erpStoreInfo){
            unionPay = erpStorePayUnionpayService.get(erpStoreInfo.getUnionpayId());
            resObject.put("machineToolNumber", unionPay!=null? unionPay.getMachineToolNumber():"");
        }
        resObject.put("shopInfoId", shop.getId());
        resObject.put("unionpayState", unionPay!=null? unionPay.getAuditStatus():0);
        resObject.put("unionpayRemark", unionPay!=null?unionPay.getAuditContent():"");
        resObject.put("orderId", erpDeliveryService.getOrderId());
        resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
        return resObject;
    }
    
    
    /**
     *商户信息收集（首次营销策划服务）
     *
     * @param taskId
     * @param procInsId
     * @return
     * @date 2018年5月26日
     */
    public JSONObject getshopInfoCollection(String taskId,String procInsId) {
        JSONObject resObject = new JSONObject();
        ErpDeliveryService erpDeliveryService =erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
        ErpShopInfo shop = erpShopInfoService.getByZhangbeiID(erpDeliveryService.getShopId());
        List<ErpShopMallForm> mallFormlist =erpShopMallFormService.getShopMallFormListByShopInfoId(shop.getId());
        resObject.put("mallFormlist", mallFormlist);
        resObject.put("orderId", erpDeliveryService.getOrderId());
        resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
        return resObject;
    }
    
    
    
    private int fillFrameMarkerMap(Map<String, Object> map, List<ErpFlowForm> erpFlowForms) {
        for (ErpFlowForm erpFlowForm : erpFlowForms) {
            if (ErpFlowForm.NORMAL.equalsIgnoreCase(erpFlowForm.getFormAttrType())) {
                map.put(erpFlowForm.getFormAttrName(), erpFlowForm.getFormAttrValue());
            } else if (ErpFlowForm.FILE.equalsIgnoreCase(erpFlowForm.getFormAttrType())) {
                fillErpOrderFile(map, erpFlowForm.getFormAttrName(), erpFlowForm.getFormAttrValue());
            } else {
                map.put(erpFlowForm.getFormAttrName(), erpFlowForm.getFormTextValue());
            }
        }
        return erpFlowForms.size();
    }
    
    private void fillErpOrderFile(Map<String, Object> map, String formAttrName, String formAttrValue) {
        List<ErpOrderFile> files = new ArrayList<ErpOrderFile>();
        if (StringUtils.isNoneBlank(formAttrValue)) {
            for (String id : formAttrValue.split(Constant.SEMICOLON)) {
                if (StringUtils.isNoneBlank(id))
                    files.add(erpOrderFileService.get(id));
            }
        }
        map.put(formAttrName, files);
    }
    
   
}
