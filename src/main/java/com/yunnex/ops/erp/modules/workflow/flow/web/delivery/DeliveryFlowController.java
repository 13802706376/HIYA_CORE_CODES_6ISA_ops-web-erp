package com.yunnex.ops.erp.modules.workflow.flow.web.delivery;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.workflow.flow.service.DeliveryFlowService;
import com.yunnex.ops.erp.modules.workflow.user.service.ErpOrderFlowUserService;


/**
 * 聚引客 直销开户相关 Controller
 * 
 * @author yunnex
 * @date 2017年11月2日
 */
@Controller
@RequestMapping(value = "${adminPath}/delivery/flow/")

public class DeliveryFlowController extends BaseController {
    @Autowired
    private DeliveryFlowService deliveryFlowService;
    @Autowired
    private ErpOrderFlowUserService erpOrderFlowUserService;
    
    /**
     * 查看订单信息，指派订单处理人员
     *
     * @param taskId
     * @param procInsId operationAdviser 运营顾问 openAccountConsultant 开户顾问 materialConsultant 物料顾问
     * @return
     * @date 2018年5月26日
     * @author hanhan
     */
    @RequestMapping(value = "assign_order_handlers")
    @ResponseBody
    public JSONObject assignOrderHandlers(@RequestBody Map<String, String> map) {
      
        return  deliveryFlowService.assignOrderHandlers(map);
    }
    
    /**
     * 电话联系商户，确认服务内容（服务类型有客常来）
     *
     * @param taskId
     * @param procInsId request
     * @return
     * @date 2018年5月26日
     * @author hanhan
     */
    @RequestMapping(value = "telephone_confirm_service")
    @ResponseBody
    public JSONObject telephoneConfirmService(String taskId,String procInsId, String orderId,HttpServletRequest request,boolean isFinished) {
        String jsonStr=  request.getParameter("jsonStr");
        return  deliveryFlowService.telephoneConfirmService(taskId, procInsId,orderId,jsonStr,isFinished);
    }
    
    /**
     * 进件资料收集
     *
     * @param taskId
     * @param procInsId
     * @param orderId completeToApplet
     * @return
     * @date 2018年5月26日
     * @author hanhan
     */
    @RequestMapping(value = "into_material_collection")
    @ResponseBody
    public JSONObject intoMaterialMollection(String taskId, String procInsId, String orderId ,String completeToApplet) {
        return  deliveryFlowService.intoMaterialMollection(taskId, procInsId,orderId,completeToApplet);
    }
    
    /**
     * 掌贝后台创建门店
     *
     * @param taskId
     * @param procInsId
     * @param orderId completeCreateStore
     * @return
     * @date 2018年5月26日
     * @author hanhan
     */
    @RequestMapping(value = "zhangbei_store_create")
    @ResponseBody
    public JSONObject zhangbeiStoreCreate(String taskId, String procInsId,String orderId, String completeCreateStore) {
        return  deliveryFlowService.zhangbeiStoreCreate(taskId, procInsId,orderId,completeCreateStore);
    }
    
    /**
     * 公众号开通
     *
     * @param taskId
     * @param procInsId
     * @param orderId request
     * @return
     * @date 2018年5月26日
     * @author hanhan
     */
    @RequestMapping(value = "public_number_open")
    @ResponseBody
    public JSONObject publicNumberOpen(String taskId, String procInsId,  String orderId,HttpServletRequest request,boolean isFinished ) {
        String jsonStr=  request.getParameter("jsonStr");
        return  deliveryFlowService.publicNumberOpen(taskId, procInsId,orderId,jsonStr, isFinished );
    }
  
    /**
     * 微信账号开通
     *
     * @param taskId
     * @param procInsId
     * @param orderId sixishfisi
     * @return
     * @date 2018年5月26日
     * @author hanhan
     */
    @RequestMapping(value = "wechat_account_open")
    @ResponseBody
    public JSONObject wechatAccountOpen(String taskId, String procInsId,  String orderId,  String weixinPayCheck) {
        return  deliveryFlowService.wechatAccountOpen(taskId, procInsId,orderId,weixinPayCheck);
    }
    
    /**
     * 微信支付商户号配置
     *
     * @param taskId
     * @param procInsId
     * @param orderId weixinPayMerNoDeploy
     * @return
     * @date 2018年5月26日
     * @author hanhan
     */
    @RequestMapping(value = "wechat_shop_configuration")
    @ResponseBody
    public JSONObject wechatShopConfiguration(String taskId, String procInsId, String orderId,  String weixinPayMerNoDeploy) {
        return  deliveryFlowService.wechatShopConfiguration(taskId, procInsId,orderId,weixinPayMerNoDeploy);
    }
    
    /**
     * 支付宝口碑申请
     *
     * @param taskId
     * @param procInsId
     * @param orderId
     * @param request
     * @param isFinished
     * @return
     * @date 2018年5月26日
     * @author hanhan
     */
    @RequestMapping(value = "alipay_public_praise_apply")
    @ResponseBody
    public JSONObject alipayPublicPraiseApply(String taskId, String procInsId,  String orderId,HttpServletRequest request,boolean isFinished ) {
        String jsonStr=  request.getParameter("jsonStr");
        return  deliveryFlowService.alipayPublicPraiseApply(taskId, procInsId,orderId,jsonStr, isFinished );
    }
    
    /**
     * 银联支付开通
     *
     * @param taskId
     * @param procInsId
     * @param orderId
     * 
     * @return
     * @date 2018年5月26日
     * @author hanhan
     */
    @RequestMapping(value = "unionpay_account_open")
    @ResponseBody
    public JSONObject unionpayAccountOpen(String taskId, String procInsId,  String orderId,String machineNoConfiguration,String unionpayOpenState ,String notOpenUnionpayScreenshot) {
        return  deliveryFlowService.unionpayAccountOpen(taskId, procInsId,orderId,machineNoConfiguration,unionpayOpenState,notOpenUnionpayScreenshot);
    }
    
    /**
     * 银联支付培训&测试（远程）
     *
     * @param taskId
     * @param procInsId
     * @param orderId
     * 
     * @return
     * @date 2018年5月26日
     * @author hanhan
     */
    @RequestMapping(value = "unionpay_account_train")
    @ResponseBody
    public JSONObject unionpayAccountTrain(String taskId, String procInsId,  String orderId,String unionpayTrainTest) {
        return  deliveryFlowService.unionpayAccountTrain(taskId, procInsId,orderId,unionpayTrainTest);
    }
    
    /**
     * 业务定义：交付服务流程标记完成
     * 
     * @date 2018年9月5日
     * @author R/Q
     */
    @RequiresPermissions("order:detail:operationServiceFlow:markComplete")
    @ResponseBody
    @RequestMapping(value = "finishWorkFlow")
    public Object finishWorkFlow(String procInsId) {
        return deliveryFlowService.finishWorkFlow(procInsId);
    }

    /**
     * 业务定义：重启工作流
     * 
     * @date 2018年8月30日
     * @author R/Q
     */
    @RequiresPermissions("order:detail:operationServiceFlow:restartFlow")
    @ResponseBody
    @RequestMapping(value = "resetWorkFlow")
    public Object resetWorkFlow(String procInsId) {
        return deliveryFlowService.resetWorkFlow(procInsId);
    }

    /**
     * 业务定义：转派流程处理人
     * 
     * @date 2018年9月11日
     * @author R/Q
     */
    @ResponseBody
    @RequestMapping(value = "changeFlowUser")
    public Object changeFlowUser(String flowUsers) {
        return erpOrderFlowUserService.batchMergeFlowUser(flowUsers);
    }
}
