package com.yunnex.ops.erp.modules.order.web;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.common.constant.CommonConstants;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.good.category.entity.ErpGoodCategory;
import com.yunnex.ops.erp.modules.good.category.service.ErpGoodCategoryService;
import com.yunnex.ops.erp.modules.good.entity.ErpGoodInfo;
import com.yunnex.ops.erp.modules.good.service.ErpGoodInfoService;
import com.yunnex.ops.erp.modules.order.constant.OrderConstants;
import com.yunnex.ops.erp.modules.order.constant.OrderSplitConstants;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderGoodServiceInfo;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalGood;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitInfo;
import com.yunnex.ops.erp.modules.order.service.ErpOrderGoodServiceInfoService;
import com.yunnex.ops.erp.modules.order.service.ErpOrderMaterialApiService;
import com.yunnex.ops.erp.modules.order.service.ErpOrderOriginalGoodService;
import com.yunnex.ops.erp.modules.order.service.ErpOrderOriginalInfoApiService;
import com.yunnex.ops.erp.modules.order.service.ErpOrderOriginalInfoService;
import com.yunnex.ops.erp.modules.order.service.ErpOrderSplitInfoService;
import com.yunnex.ops.erp.modules.sys.constant.RoleConstant;
import com.yunnex.ops.erp.modules.sys.entity.Dict;
import com.yunnex.ops.erp.modules.sys.entity.Role;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.service.SystemService;
import com.yunnex.ops.erp.modules.sys.service.UserService;
import com.yunnex.ops.erp.modules.sys.utils.DictUtils;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;
import com.yunnex.ops.erp.modules.workflow.channel.entity.JykOrderPromotionChannel;
import com.yunnex.ops.erp.modules.workflow.channel.service.JykOrderPromotionChannelService;
import com.yunnex.ops.erp.modules.workflow.flow.common.JykFlowConstants;
import com.yunnex.ops.erp.modules.workflow.flow.service.ErpFlowFormService;
import com.yunnex.ops.erp.modules.workflow.user.service.ErpOrderFlowUserService;


/**
 * 订单Controller
 * 
 * @author huanghaidong
 * @version 2017-10-24
 */
@Controller
@RequestMapping(value = "${adminPath}/order/erpOrderOriginalInfo")
public class ErpOrderOriginalInfoController extends BaseController {

    @Autowired
    private ErpOrderOriginalInfoService erpOrderOriginalInfoService;

    @Autowired
    private ErpOrderOriginalGoodService erpOrderOriginalGoodService;

    @Autowired
    private ErpOrderSplitInfoService erpOrderSplitInfoService;

    @Autowired
    private ErpOrderOriginalInfoApiService erpOrderOriginalInfoApiService;
    @Autowired
    private JykOrderPromotionChannelService jykOrderPromotionChannelService;

    @Autowired
    private SystemService systemService;

    @Autowired
    private ErpGoodCategoryService cateGoryService;

    @Autowired
    private ErpGoodInfoService erpGoodInfoService; // 商品Service
    
    @Autowired
    private ErpOrderGoodServiceInfoService erpOrderGoodServiceInfoService;

    @Autowired
    private ErpOrderMaterialApiService erpOrderMaterialApiService;

    @Autowired
    private ErpFlowFormService erpFlowFormService;

    @Autowired
    private ErpOrderFlowUserService erpOrderFlowUserService;

    @Autowired
    private UserService userService;

    @ModelAttribute
    public ErpOrderOriginalInfo get(@RequestParam(required = false) String id) {
        ErpOrderOriginalInfo entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = erpOrderOriginalInfoService.get(id);
        }
        if (entity == null) {
            entity = new ErpOrderOriginalInfo();
        }
        return entity;
    }

    @RequiresPermissions("order:erpOrderOriginalInfo:view")
    @RequestMapping(value = {"list", ""})
    public String list(ErpOrderOriginalInfo erpOrderOriginalInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
        erpOrderOriginalInfo.setUpdateBy(UserUtils.getUser());// 权限校验用当前登录用户
        Page<ErpOrderOriginalInfo> page = erpOrderOriginalInfoService.findPage(new Page<ErpOrderOriginalInfo>(request, response),
                        erpOrderOriginalInfo);

        // 封装聚引客待处理商品数量
        encapsulateJykPendingGoodNum(page);

        // 封装商户运营待处理服务数量
        encapsulatePendingServiceNum(page);

        model.addAttribute("page", page);

        // 服务类型
        List<ErpGoodCategory> goodCateGoryList = cateGoryService.findList(new ErpGoodCategory());
        model.addAttribute("goodTypeList", goodCateGoryList);

        // 服务类型查询参数回显
        model.addAttribute("goodTypeValues", Arrays.toString(erpOrderOriginalInfo.getGoodTypeValues()));

        // 订单状态查询参数回显
        model.addAttribute("orderStatusValues", Arrays.toString(erpOrderOriginalInfo.getOrderStatusValues()));
        
        // 返回实体
        model.addAttribute("erpOrderOriginalInfo", erpOrderOriginalInfo);

        // 服务商/分公司没有同步订单入口和新增订单入口
        String type = UserUtils.getUser().getType();
        model.addAttribute("isAgent", OrderConstants.AGENT.equals(type) ? true : false);

        return "modules/order/erpOrderOriginalInfoList";
    }
    
    private void encapsulatePendingServiceNum(Page<ErpOrderOriginalInfo> page) {
        if (page == null) {
            return;
        }
        List<ErpOrderOriginalInfo> list = page.getList();
        if (CollectionUtils.isNotEmpty(list)) {
            for (ErpOrderOriginalInfo orderInfo : list) {
                Map<String, Object> map = erpOrderGoodServiceInfoService.querySum(orderInfo.getId());
                if (MapUtils.isNotEmpty(map)) {
                    BigDecimal pendingServiceNum = (BigDecimal) map.get("pendingNum");
                    orderInfo.setPendingServiceNum(pendingServiceNum == null ? new BigDecimal(0) : pendingServiceNum);
                } else {
                    orderInfo.setPendingServiceNum(new BigDecimal(0));
                }
            }
        }
    }

   
    private void encapsulateJykPendingGoodNum(Page<ErpOrderOriginalInfo> page) {
        if (page == null) {
            return;
        }
        List<ErpOrderOriginalInfo> list = page.getList();
        if (CollectionUtils.isNotEmpty(list)) {
            for (ErpOrderOriginalInfo orderInfo : list) {
                Integer pendingNum = erpOrderOriginalGoodService.findJykPendingGoodNumByOrderId(orderInfo.getId());
                orderInfo.setPendingNum(pendingNum == null ? 0 : pendingNum);
            }
        }
    }

    @RequiresPermissions("order:erpOrderOriginalInfo:view")
    @RequestMapping(value = "form")
    public String form(ErpOrderOriginalInfo erpOrderOriginalInfo, Model model, RedirectAttributes redirectAttributes) {
        List<ErpOrderOriginalGood> erpOrderOriginalGoods = erpOrderOriginalGoodService.getListOriginalGood(erpOrderOriginalInfo.getId());

        List<ErpOrderSplitInfo> erpOrderSplitInfos = erpOrderSplitInfoService.findListByOrderInfoAndUser(erpOrderOriginalInfo.getId(), 
                                                            erpOrderOriginalInfo.getGoodType());
        List<Dict> orderVersionDicts = DictUtils.getDictList(Constant.ORDER_VERSION_DICT_CODE);
        List<Dict> suspendReasonDicts = DictUtils.getDictList(OrderSplitConstants.SUSPEND_REASON);
        for (ErpOrderSplitInfo erpOrderSplitInfo : erpOrderSplitInfos) {
            erpOrderSplitInfo.setTaskDisplay("");
            List<JykOrderPromotionChannel> channelList = this.jykOrderPromotionChannelService.findListBySplitId(erpOrderSplitInfo.getId());
            if (null==channelList||channelList.isEmpty()) {
                erpOrderSplitInfo.setChannel("");
            } else {
                StringBuffer sb = new StringBuffer();
                sb.append("[");
                for (JykOrderPromotionChannel jykOrderPromotionChannel : channelList) {
                    sb.append(DictUtils.getDictLabel(jykOrderPromotionChannel.getPromotionChannel(), "extension_passageway_qulify", " ")).append(" ");
                }
                sb.append("]");
                erpOrderSplitInfo.setChannel(sb.toString());
            }
        }

        model.addAttribute("info", erpOrderOriginalInfoService.getCalcInfo(erpOrderOriginalInfo.getId(), erpOrderOriginalInfo.getGoodType()))
                        .addAttribute("goods", erpOrderOriginalGoods)
                        .addAttribute("splits", erpOrderSplitInfos).addAttribute("versiondicts", orderVersionDicts)
                        .addAttribute("suspendReasonDicts", suspendReasonDicts);

        Role role = systemService.getRoleByEnname(DictUtils.getDictValue("split_order", "split_order", ""));

        if (role == null) {
            addMessage(redirectAttributes, "请设置");
            return "modules/order/erpOrderOriginalInfoForm";
        }
        List<User> userList = systemService.findUser(new User(new Role(role.getId())));
        model.addAttribute("userList", userList);

        // 服务商/分公司没有同步聚引客服务模块的操作权限,没有聚引客生产流程模块的浏览和操作权限，没有作废订单和结束订单的操作权限；
        String type = UserUtils.getUser().getType();
        model.addAttribute("isAgent", OrderConstants.AGENT.equals(type) ? true : false);

        // 添加商户运营服务流程模块
        List<Map<String, Object>> deliveryServiceInfoList = erpOrderGoodServiceInfoService.getDeliveryServiceByOrderId(erpOrderOriginalInfo.getId());
        model.addAttribute("deliveryServiceInfoList", deliveryServiceInfoList);
        // 添加商户运营服务信息模块
        List<ErpOrderGoodServiceInfo> orderGoodServiceInfoList = erpOrderGoodServiceInfoService.getOrderGoodServiceInfo(erpOrderOriginalInfo.getId());
        
        model.addAttribute("orderGoodServiceInfoList", orderGoodServiceInfoList);
        return "modules/order/erpOrderOriginalInfoForm";
    }

    @RequiresPermissions("order:erpOrderOriginalInfo:edit")
    @RequestMapping(value = "save")
    public String save(ErpOrderOriginalInfo erpOrderOriginalInfo, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, erpOrderOriginalInfo)) {
            return form(erpOrderOriginalInfo, model, redirectAttributes);
        }
        erpOrderOriginalInfoService.save(erpOrderOriginalInfo);
        addMessage(redirectAttributes, "保存订单成功");
        return "redirect:" + Global.getAdminPath() + "/order/erpOrderOriginalInfo/?repage";
    }

    @RequiresPermissions("order:erpOrderOriginalInfo:edit")
    @RequestMapping(value = "delete")
    public String delete(ErpOrderOriginalInfo erpOrderOriginalInfo, RedirectAttributes redirectAttributes) {
        erpOrderOriginalInfoService.delete(erpOrderOriginalInfo);
        addMessage(redirectAttributes, "删除订单成功");
        return "redirect:" + Global.getAdminPath() + "/order/erpOrderOriginalInfo/?repage";
    }

    /**
     * 业务定义：同步订单
     * 
     * @date 2018年5月31日
     */
    @RequestMapping(value = "syncAll")
    @RequiresPermissions("order:list:button:sync")
    public JSONObject syncAll(@RequestParam(value = "startAt" ,required = true) String startAt,
                    @RequestParam(value = "endAt", required = true) String endAt) {
        JSONObject jsonObject = new JSONObject();
        boolean result = erpOrderOriginalInfoApiService.syncBatchOrder(startAt, endAt);
        jsonObject.put("result", result);
        return jsonObject;
    }

    /**
     * 物料更新订单同步
     * 
     * @return
     */
    @RequiresPermissions("order:material:sync")
    @RequestMapping(value = "syncOrderMaterial", method = RequestMethod.POST)
    public @ResponseBody Boolean syncOrderMaterial() {
        return erpOrderMaterialApiService.syncOrderMaterial();
    }

    /**
     * 同步一条订单物料信息
     *
     * @return
     */
    @RequestMapping(value = "syncOneOrderMaterial", method = RequestMethod.POST)
    public @ResponseBody BaseResult syncOneOrderMaterial(Long ysOrderId) {
        return erpOrderMaterialApiService.syncOneOrderMaterial(ysOrderId);
    }

    /**
     * 去新增订单页面
     *
     * @param erpOrderOriginalInfo
     * @param model
     * @return
     * @date 2017年11月24日
     * @author SunQ
     */
    @RequiresPermissions("order:list:button:addOrder")
    @RequestMapping(value = "toAdd")
    public String toAdd(ErpOrderOriginalInfo erpOrderOriginalInfo, Model model) {
        if (erpOrderOriginalInfo == null) {
            erpOrderOriginalInfo = new ErpOrderOriginalInfo();
        }
        model.addAttribute("info", erpOrderOriginalInfo);
        return "modules/order/erpOrderOriginalInfoAdd";
    }

    /**
     * 业务定义：依据商品类型查询对应商品列表
     * 
     * @date 2018年7月4日
     * @author R/Q
     */
    @ResponseBody
    @RequestMapping(value = "queryGoodList")
    public Object queryGoodList(String goodType) {
        ErpGoodInfo goodInfo = new ErpGoodInfo();
        ErpGoodCategory category = new ErpGoodCategory();
        if (StringUtils.equals(goodType, "6")) {
            goodInfo.setId("999");// 贝蚁V2.0，新增聚引客订单只查询ERP新增的特殊商品，此处做特殊处理
        } else {
            category.setId(goodType);
            goodInfo.setCategory(category);
            goodInfo.setIsPackage("N");// 新增订单只能选择单买模式的商品，过滤掉没有单买模式的商品
        }
        List<ErpGoodInfo> goodList = erpGoodInfoService.findList(goodInfo);
        return goodList;
    }

    /**
     * 保存新增订单
     *
     * @param erpOrderOriginalInfo
     * @return
     * @date 2017年11月24日
     * @author SunQ
     */
    @ResponseBody
    @RequiresPermissions("order:erpOrderOriginalInfo:edit")
    @RequestMapping(value = "add")
    public Object add(@RequestBody ErpOrderOriginalInfo erpOrderOriginalInfo) {
        Map<String, Object> returnMap = Maps.newHashMap();
        try {
            erpOrderOriginalInfoService.createOrderAndStartProcess(erpOrderOriginalInfo);
            returnMap.put(CommonConstants.RETURN_CODE, CommonConstants.RETURN_CODE_SUCCESS);
        } catch (ServiceException e) {
            returnMap.put(CommonConstants.RETURN_CODE, CommonConstants.RETURN_CODE_FAIL);
            returnMap.put(CommonConstants.RETURN_MESSAGE, e.getMessage());
        } catch (Exception e) {
            logger.error("新增订单信息发生错误：{}", e);// NOSONAR
            returnMap.put(CommonConstants.RETURN_CODE, CommonConstants.RETURN_CODE_FAIL);
            returnMap.put(CommonConstants.RETURN_MESSAGE, "系统错误，请查看日志");
        }
        return returnMap;
    }
    

    @RequiresPermissions("order:erpOrderOriginalInfo:edit")
    @RequestMapping(value = "dbDelete")
    public String dbDelete(ErpOrderOriginalInfo erpOrderOriginalInfo, RedirectAttributes redirectAttributes) {

        /* 获取订单信息 */
        ErpOrderOriginalInfo dbErpOrderOriginalInfo = erpOrderOriginalInfoService.get(erpOrderOriginalInfo.getId());

        /* 判断订单是否拆单 */
        if (dbErpOrderOriginalInfo.getSplitCount() > 0) {
            addMessage(redirectAttributes, "订单已经拆单,无法执行该操作");
        } else {
            erpOrderOriginalInfoService.deleteOrder(erpOrderOriginalInfo);
            addMessage(redirectAttributes, "删除订单成功");
        }
        return "redirect:" + Global.getAdminPath() + "/order/erpOrderOriginalInfo?goodType=5";
    }

    /**
     * 去修改页面
     *
     * @param model
     * @return
     * @date 2017年11月27日
     * @author SunQ
     */
    @RequiresPermissions("order:erpOrderOriginalInfo:edit")
    @RequestMapping(value = "toEdit")
    public String toEdit(ErpOrderOriginalInfo erpOrderOriginalInfo, Model model) {

        /* 获取订单信息 */
        ErpOrderOriginalInfo orderInfo = erpOrderOriginalInfoService.get(erpOrderOriginalInfo.getId());

        /* 对支付时间进行格式转换,用于时间控件回显 */
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        orderInfo.setTempBuyDate(sdf.format(orderInfo.getBuyDate()));
        
        /* 对实际支付价格进行处理 */
        BigDecimal price = BigDecimal.valueOf(orderInfo.getRealPrice());
        orderInfo.setTempRealPrice(price.divide(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP).toString());

        /* 获取订单商品列表 */
        List<ErpOrderOriginalGood> orderGoods = erpOrderOriginalGoodService.findListByOrderId(erpOrderOriginalInfo.getId());

        /* 获取商品列表 */
        ErpGoodInfo goodInfo = new ErpGoodInfo();
        /* 设置查询聚引客类型产品 */
        ErpGoodCategory category = new ErpGoodCategory();
        category.setId("5");
        goodInfo.setCategory(category);
        List<ErpGoodInfo> goodList = erpGoodInfoService.findList(new ErpGoodInfo());

        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("orderGoods", orderGoods);
        model.addAttribute("goodList", goodList);
        return "modules/order/erpOrderOriginalInfoEdit";
    }
    
    /**
     * 保存修改订单
     *
     * @param erpOrderOriginalInfo
     * @param model
     * @param redirectAttributes
     * @return
     * @date 2017年11月24日
     * @author SunQ
     */
    @RequiresPermissions("order:erpOrderOriginalInfo:edit")
    @RequestMapping(value = "update")
    public String update(ErpOrderOriginalInfo erpOrderOriginalInfo, String goodIds, String buyCounts, Model model,
                    RedirectAttributes redirectAttributes) {

        /* 获取订单信息 */
        ErpOrderOriginalInfo dbErpOrderOriginalInfo = erpOrderOriginalInfoService.get(erpOrderOriginalInfo.getId());

        /* 判断订单号是否存在 */
        if (!dbErpOrderOriginalInfo.getOrderNumber().equals(erpOrderOriginalInfo.getOrderNumber()) 
                        && erpOrderOriginalInfoService.countByOrderNumber(erpOrderOriginalInfo.getOrderNumber()) > 0) {
            addMessage(redirectAttributes, "订单号已存在");
            return "redirect:" + Global.getAdminPath() + "/order/erpOrderOriginalInfo/toEdit?id=" + erpOrderOriginalInfo.getId();
        }

        /* 判断订单是否拆单 */
        if (dbErpOrderOriginalInfo.getSplitCount() > 0) {
            addMessage(redirectAttributes, "订单已经拆单,无法执行该操作");
            return "redirect:" + Global.getAdminPath() + "/order/erpOrderOriginalInfo/toEdit?id=" + erpOrderOriginalInfo.getId();
        } else {
            Date now = new Date(System.currentTimeMillis());
            erpOrderOriginalInfo.setUpdateDate(now); // 更新时间
            BigDecimal price = new BigDecimal(erpOrderOriginalInfo.getTempRealPrice());
            erpOrderOriginalInfo.setRealPrice(price.multiply(new BigDecimal("100")).longValue());// 实际价格

            List<ErpOrderOriginalGood> goods = new ArrayList<ErpOrderOriginalGood>();
            if (StringUtils.isNotBlank(goodIds)) {
                String[] goodid = goodIds.split(",");
                String[] buycount = buyCounts.split(",");

                ErpOrderOriginalGood goodInfo = null;
                for (int i = 0; i < goodid.length; i++) {
                    ErpGoodInfo erpGoodInfo = erpGoodInfoService.get(goodid[i]);
                    ErpGoodCategory erpGoodCategory = cateGoryService.get(erpGoodInfo.getCategoryId().toString());
                    goodInfo = new ErpOrderOriginalGood();
                    goodInfo.setGoodName(erpGoodInfo.getName());// 商品名称
                    goodInfo.setGoodId(Long.valueOf(goodid[i])); // 商品id
                    goodInfo.setGoodTypeId(erpGoodInfo.getCategoryId());// 商品类型id
                    goodInfo.setGoodTypeName(erpGoodCategory.getName());// 商品类型名称
                    goodInfo.setPrePrice(erpGoodInfo.getPrice());// 预计价格(单位：分)
                    goodInfo.setPrePrice(erpGoodInfo.getPrice());// 实际价格(单位：分)
                    goodInfo.setNum(Integer.parseInt(buycount[i]));// 商品总共数量
                    goodInfo.setProcessNum(0);// 处理中的商品数量
                    goodInfo.setPendingNum(Integer.parseInt(buycount[i]));// 待处理的商品数量
                    goodInfo.setFinishNum(0);// 已完成的商品数量
                    goodInfo.setSort((long) i);// 排序字段
                    goods.add(goodInfo);
                }
            }

            erpOrderOriginalInfoService.updateOrder(erpOrderOriginalInfo, goods);
            addMessage(redirectAttributes, "修改订单成功");
            return "redirect:" + Global.getAdminPath() + "/order/erpOrderOriginalInfo?goodType=5";
        }
    }
    
    /**
     * 作废订单
     *
     * @param orderId
     * @return
     * @date 2017年11月27日
     * @author hsr
     */
    @RequiresPermissions("order:erpOrderOriginalInfo:cancelOrder")
    @RequestMapping(value = "cancel/{orderId}", method = RequestMethod.POST)
    public @ResponseBody JSONObject cancelOrder(@PathVariable("orderId") String orderId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", true);
        try {
            erpOrderOriginalInfoService.cancelOrder(orderId);
        } catch (Exception e) {
            jsonObject.put("result", false);
            logger.error("订单作废异常！", e);
        }
        return jsonObject;
    }
    
    
    /**
     * 订单结束
     *
     * @param orderId
     * @return
     * @date 2018年4月20日
     * @author hanhan
     */
    @RequiresPermissions("order:erpOrderOriginalInfo:endOrder")
    @RequestMapping(value = "endOrder/{orderId}", method = RequestMethod.POST)
    public @ResponseBody JSONObject endOrder(@PathVariable("orderId") String orderId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", true);
        try {
            erpOrderOriginalInfoService.endOrder(orderId);
        } catch (Exception e) {
            jsonObject.put("result", false);
            logger.error("订单结束异常！", e);
        }
        return jsonObject;
    }
    
    
    

    @RequiresPermissions("order:detail:jykService:updateOrderVersion")
    @RequestMapping(value = "updateOrderVersion", method = RequestMethod.POST)
    public @ResponseBody JSONObject updateOrderVersion(String orderId, String orderVersion) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", true);
        try {
            erpOrderOriginalInfoService.updateOrderVersion(orderId, orderVersion);
        } catch (Exception e) {
            jsonObject.put("result", false);
            logger.error("订单版本号更新异常！", e);
        }
        return jsonObject;
    }

    @RequiresPermissions("order:list:button:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public void export(@RequestParam String jsonObject, HttpServletResponse response) {
        erpOrderOriginalInfoService.export(JSON.parseObject(StringEscapeUtils.unescapeHtml4(jsonObject), ErpOrderOriginalInfo.class), response);
    }

    /**
     * 业务定义：查询订单审核列表数据-分页
     * 
     * @date 2018年7月4日
     * @author R/Q
     */
    @ResponseBody
    @RequestMapping(value = "queryAuditList")
    public Object queryAuditList(ErpOrderOriginalInfo paramObj, HttpServletRequest request, HttpServletResponse response) {
        return erpOrderOriginalInfoService.queryAuditList(paramObj, new Page<ErpOrderOriginalInfo>(request, response));
    }

    /**
     * 业务定义：跳转至订单审核列表页面
     * 
     * @date 2018年7月6日
     * @author R/Q
     */
    @RequestMapping(value = "orderAuditForm")
    public String orderAuditForm(ErpOrderOriginalInfo paramObj, HttpServletRequest request, HttpServletResponse response,Model model) {
        model.addAttribute("page", erpOrderOriginalInfoService.queryAuditList(paramObj, new Page<ErpOrderOriginalInfo>(request, response)));
        return "modules/order/erpOrderAuditForm";
    }

    /**
     * 业务定义：跳转至订单审核详情页面
     * 
     * @date 2018年7月9日
     * @author R/Q
     */
    @RequestMapping(value = "orderInfoAuditForm")
    public String orderInfoAuditForm(String orderId, Model model) {
        model.addAttribute("orderId", orderId);
        return "modules/order/orderInfoAuditForm";
    }

    @RequestMapping(value = "toOrderReviewUpdate")
    public String orderReviewUpdate(String orderId, Model model) {

        return "modules/order/orderReviewUpdate";
    }

    @RequestMapping(value = "getOrderAndGood", method = RequestMethod.GET)
    public @ResponseBody JSONObject getOrderAndGood(String orderId) {
        JSONObject jsonObject = new JSONObject();
        ErpOrderOriginalInfo erpOrderOriginalInfo = erpOrderOriginalInfoService.getOrderAndGood(orderId);
        jsonObject.put("order", erpOrderOriginalInfo);
        if (null != erpOrderOriginalInfo && StringUtils.isNotBlank(erpOrderOriginalInfo.getProcInsId())) {

            jsonObject.put("reason", erpFlowFormService.findByProcessIdAndAttrName(erpOrderOriginalInfo.getProcInsId(), "reason"));
            jsonObject.put("verifyInfo", erpFlowFormService.findByProcessIdAndAttrName(erpOrderOriginalInfo.getProcInsId(), "verifyInfo"));
        }
        return jsonObject;
    }

    /**
     * 业务定义：根据流程ID获取对应转派信息
     * 
     * @date 2018年8月28日
     * @author R/Q
     */
    @ResponseBody
    @RequestMapping(value = "getTransferInfo")
    public Object getTransferInfo(String procInsId) {
        Map<String,Object> returnMap = Maps.newHashMap();
        List<Map<String, String>> flowUserList = erpOrderFlowUserService.findByProcInsId(procInsId);// 当前流程处理人
        if (CollectionUtils.isNotEmpty(flowUserList)) {
            for (Map<String, String> map : flowUserList) {
                returnMap.put(map.get("role"), map);
            }
        }
        Map<String, Object> roleUsersMap = Maps.newHashMap();
        roleUsersMap.put(JykFlowConstants.Planning_Expert, userService.getUserByRoleName(RoleConstant.PLANNING_PERSON));
        roleUsersMap.put(JykFlowConstants.designer, userService.getUserByRoleName(RoleConstant.DESIGNER));
        roleUsersMap.put(JykFlowConstants.assignTextDesignPerson, userService.getUserByRoleName(RoleConstant.TEXT_DESIGN_PERSON));
        roleUsersMap.put(JykFlowConstants.assignConsultant, userService.getUserByRoleName(RoleConstant.PUT_ADVISOR_PERSON));
        returnMap.put("roleUsersMap", roleUsersMap);
        return returnMap;
    }
    
    /**
     * 转派
     *
     * @param erpOrderOriginalInfo
     * @param model
     * @return
     * @date 2017年11月24日
     * @author SunQ
     */
    @RequestMapping(value = "/redeploy")
    public String redeploy(HttpServletRequest request, HttpServletResponse response, Model model) {
        return "modules/order/redeploy";
    }

    /**
     * 
     * 业务定义：获取流程处理人-显示默认值用
     * 
     * @date 2018年9月3日
     * @author R/Q
     */
    @ResponseBody
    @RequestMapping(value = "getFlowUsers")
    public Object getFlowUsers(String procInsId) {
        Map<String, Object> returnMap = Maps.newHashMap();
        List<Map<String, String>> flowUserList = erpOrderFlowUserService.findByProcInsId(procInsId);// 当前流程处理人
        if (CollectionUtils.isNotEmpty(flowUserList)) {
            for (Map<String, String> map : flowUserList) {
                returnMap.put(map.get("role"), map);
            }
        }
        return returnMap;
    }
}
