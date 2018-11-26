package com.yunnex.ops.erp.modules.order.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.common.constant.CommonConstants;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.result.IllegalArgumentErrorResult;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.common.utils.DateUtils;
import com.yunnex.ops.erp.common.utils.IdGen;
import com.yunnex.ops.erp.modules.act.dao.ActDao;
import com.yunnex.ops.erp.modules.act.entity.TaskExt;
import com.yunnex.ops.erp.modules.act.service.TaskExtService;
import com.yunnex.ops.erp.modules.act.utils.ActUtils;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisForm;
import com.yunnex.ops.erp.modules.diagnosis.service.DiagnosisFormService;
import com.yunnex.ops.erp.modules.good.category.service.ErpGoodCategoryService;
import com.yunnex.ops.erp.modules.message.service.ErpServiceMessageService;
import com.yunnex.ops.erp.modules.message.service.ErpServiceProgressService;
import com.yunnex.ops.erp.modules.order.constant.OrderSplitConstants;
import com.yunnex.ops.erp.modules.order.dao.ErpOrderSplitInfoDao;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalGood;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitGood;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitInfo;
import com.yunnex.ops.erp.modules.order.entity.SplitGoodEditNumForm;
import com.yunnex.ops.erp.modules.order.entity.SplitGoodForm;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreInfoService;
import com.yunnex.ops.erp.modules.store.constant.StoreConstant;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.service.UserService;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;
import com.yunnex.ops.erp.modules.workflow.channel.entity.JykOrderPromotionChannel;
import com.yunnex.ops.erp.modules.workflow.channel.service.JykOrderPromotionChannelService;
import com.yunnex.ops.erp.modules.workflow.flow.common.JykFlowConstants;
import com.yunnex.ops.erp.modules.workflow.flow.dao.JykFlowDao;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderFile;
import com.yunnex.ops.erp.modules.workflow.flow.entity.JykFlow;
import com.yunnex.ops.erp.modules.workflow.flow.handler.ProcessStartContext;
import com.yunnex.ops.erp.modules.workflow.flow.service.ErpOrderFileService;
import com.yunnex.ops.erp.modules.workflow.flow.service.JykFlowService;
import com.yunnex.ops.erp.modules.workflow.flow.service.WorkFlow3p25Service;
import com.yunnex.ops.erp.modules.workflow.flow.service.WorkFlowMonitorService;
import com.yunnex.ops.erp.modules.workflow.flow.service.WorkFlowService;
import com.yunnex.ops.erp.modules.workflow.store.service.JykOrderChoiceStoreService;
import com.yunnex.ops.erp.modules.workflow.user.dao.ErpOrderFlowUserDao;
import com.yunnex.ops.erp.modules.workflow.user.entity.ErpOrderFlowUser;
import com.yunnex.ops.erp.modules.workflow.user.service.ErpOrderFlowUserService;

/**
 * 分单Service
 * 
 * @author huanghaidong
 * @version 2017-10-24
 */
@Service
public class ErpOrderSplitInfoService extends CrudService<ErpOrderSplitInfoDao, ErpOrderSplitInfo> {

    private static final int CON_ZERO = 0;

    @Autowired
    private ErpOrderOriginalGoodService erpOrderOriginalGoodService;
    @Autowired
    private JykFlowDao jykFlowDao;
    @Autowired
    private ErpOrderOriginalInfoService erpOrderOriginalInfoService;
    @Autowired
    private ErpOrderSplitInfoDao erpOrderSplitInfoDao;
    @Autowired
    private UserService userService;
    @Autowired
    private ActDao actDao;
    @Autowired
    private TaskExtService taskExtService;
    @Autowired
    @Lazy(true)
    private WorkFlowService workFlowService;
    @Autowired
    private ErpOrderSplitGoodService erpOrderSplitGoodService;
    @Autowired
    private JykFlowService jykFlowService;
    @Autowired
    private ErpGoodCategoryService erpGoodCategoryService;
    @Autowired
    private ErpOrderFlowUserDao erpOrderFlowUserDao;
    @Autowired
    private DiagnosisFormService diagnosisFormService;
    @Autowired
    private ErpOrderFileService orderfileService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private JykOrderPromotionChannelService promotionChannelService;
    @Autowired
    @Lazy(true)
    private WorkFlow3p25Service workFlow3p25Service;
    @Autowired
    private WorkFlowMonitorService workFlowMonitorService;
    @Autowired
    private ErpOrderFlowUserService erpOrderFlowUserService;
    @Autowired
    private ErpServiceMessageService serviceMessageService;
    @Autowired
    private ErpServiceProgressService serviceProgressService;
    @Autowired
    private JykOrderChoiceStoreService jykOrderChoiceStoreService;
    @Autowired
    private ErpStoreInfoService erpStoreInfoService;



    public ErpOrderSplitInfo get(String id) {
        return super.get(id);
    }

    public List<ErpOrderSplitInfo> findList(ErpOrderSplitInfo erpOrderSplitInfo) {
        return super.findList(erpOrderSplitInfo);
    }

    public Page<ErpOrderSplitInfo> findPage(Page<ErpOrderSplitInfo> page, ErpOrderSplitInfo erpOrderSplitInfo) {
        return super.findPage(page, erpOrderSplitInfo);
    }

    @Transactional(readOnly = false)
    public void save(ErpOrderSplitInfo erpOrderSplitInfo) {
        super.save(erpOrderSplitInfo);


    }

    @Transactional(readOnly = false)
    public void delete(ErpOrderSplitInfo erpOrderSplitInfo) {
        super.delete(erpOrderSplitInfo);
    }

    public List<ErpOrderSplitInfo> getBystate(Integer status) {

        return erpOrderSplitInfoDao.getBystate(status, UserUtils.getUser().getId());
    }

    public List<ErpOrderSplitInfo> findcomplete(Integer status, String del, String orderNumber, String splitId, String shopId, Integer hurryFlag) {
        return erpOrderSplitInfoDao.findcomplete(status, del, orderNumber, splitId, shopId, hurryFlag);
    }


    /**
     * 分单操作
     *
     * @param goodId 订单商品id
     * @param num 数量
     * @return
     * @date 2017年10月26日
     * @author huanghaidong
     */
    @Transactional(readOnly = false)
    public ErpOrderSplitInfo split(String goodId, Integer num, String planningExpert) {
        ErpOrderOriginalGood erpOrderOriginalGood = erpOrderOriginalGoodService.get(String.valueOf(goodId));
        if (null == erpOrderOriginalGood || Integer.compare(erpOrderOriginalGood.getPendingNum(), num) < 0) {
            return null;
        }
        ErpOrderOriginalInfo erpOrderOriginalInfo = erpOrderOriginalInfoService.get(String.valueOf(erpOrderOriginalGood.getOrderId()));
        int count = countByOrderId(erpOrderOriginalGood.getOrderId());
        ErpOrderSplitInfo erpOrderSplitInfo = new ErpOrderSplitInfo();
        erpOrderSplitInfo.setOrderId(erpOrderOriginalGood.getOrderId());
        erpOrderSplitInfo.setShopId(erpOrderOriginalInfo.getShopId());
        erpOrderSplitInfo.setOrderNumber(erpOrderOriginalInfo.getOrderNumber());
        erpOrderSplitInfo.setSplitId(count + 1);
        erpOrderSplitInfo.setOriginalGoodId(erpOrderOriginalGood.getId());
        erpOrderSplitInfo.setGoodName(erpOrderOriginalGood.getGoodName());
        erpOrderSplitInfo.setGoodTypeId(erpOrderOriginalGood.getGoodTypeId());
        erpOrderSplitInfo.setGoodTypeName(erpOrderOriginalGood.getGoodTypeName());
        erpOrderSplitInfo.setNum(num);
        erpOrderSplitInfo.setPrice(erpOrderOriginalGood.getRealPrice());
        erpOrderSplitInfo.setStatus(ErpOrderSplitInfo.STATUS_PROCESS);
        erpOrderSplitInfo.setPlanningExpert(planningExpert);
        super.save(erpOrderSplitInfo);
        // 减少订单商品待处理商品数量，增加订单商品处理中的数量
        erpOrderOriginalGoodService.decreasePendingNum(goodId, num);
        return erpOrderSplitInfo;
    }

    /**
     * 多个商品拆单操作，后启动聚引客流程 只支持一个原始订单的多个商品分单
     *
     * @param splitGoodLists 分单商品列表
     * @param isFist 首次分单
     * @param planningExpert 策划专家
     * @return
     * @date 2018年4月4日
     * @author zjq
     */
    @Transactional(readOnly = false)
    public ErpOrderSplitInfo multiGoodSplitAndStartProcess(List<SplitGoodForm> splitGoodLists, boolean isFist, String planningExpert) {

        logger.info("Thread[{}], 原始订单分单操作,入参[{}],是否默认分单[{}],策划专家[{}]", Thread.currentThread().getName(), splitGoodLists, isFist, planningExpert);

        ErpOrderOriginalGood erpOrderOriginalGood = erpOrderOriginalGoodService.get(String.valueOf(splitGoodLists.get(0).getGoodId()));
        ErpOrderOriginalInfo erpOrderOriginalInfo = null;
        ErpOrderSplitInfo erpOrderSplitInfo = null;
        List<User> users = null;
        if (null != erpOrderOriginalGood) {
            // 封装分单信息
            erpOrderOriginalInfo = erpOrderOriginalInfoService.get(erpOrderOriginalGood.getOrderId());
            int count = countByOrderId(erpOrderOriginalGood.getOrderId());
            users = userService.getUserByRoleName(Constant.ORDER_MANAGER_COMMISSIONER);
            erpOrderSplitInfo = extractErpOrderOriginalInfo(erpOrderOriginalGood, erpOrderOriginalInfo, count, users, planningExpert, isFist);
            erpOrderSplitInfo.setIsNewRecord(true);
            erpOrderSplitInfo.setId(IdGen.uuid());
        } else {
            logger.info("Thread[{}],原始订单分单操作,GoodId ERROR [{}]", Thread.currentThread().getName());
            return null;
        }

        String jykGoodTypeId = erpGoodCategoryService.findByCode(Constant.JYK_ORDER_GOOD_TYPECODE).getId();

        for (SplitGoodForm splitGoodList : splitGoodLists) {

            erpOrderOriginalGood = erpOrderOriginalGoodService.get(String.valueOf(splitGoodList.getGoodId()));

            // 默认分单 聚引客类型套餐
            if (isFist && !jykGoodTypeId.equalsIgnoreCase(String.valueOf(erpOrderOriginalGood.getGoodTypeId()))) {
                // 分单操作只针对所有的聚引客类型套餐和数量
                continue;
            }
            // 订单商品不为空，待处理数量<分单商品数量
            if (null == erpOrderOriginalGood || Integer.compare(erpOrderOriginalGood.getPendingNum(), splitGoodList.getNum()) < 0) {
                logger.info("multiGoodSplitAndStartProcess fail SplitGoodList[{}],erpOrderOriginalGood[{}],erpOrderOriginalGood.getPendingNum[{}],splitGoodList.getNum[{}]",
                                splitGoodList, erpOrderOriginalGood, null == erpOrderOriginalGood ? "error" : erpOrderOriginalGood.getPendingNum(),
                                splitGoodList.getNum());
                return null;
            }
            // 原始订单商品数量信息
            // 待处理的商品数量
            erpOrderOriginalGood.setPendingNum(erpOrderOriginalGood.getPendingNum() - splitGoodList.getNum());
            // 处理中的商品数量
            erpOrderOriginalGood.setProcessNum(erpOrderOriginalGood.getProcessNum() + splitGoodList.getNum());
            erpOrderOriginalGoodService.save(erpOrderOriginalGood);
            // 原始订单数量信息
            erpOrderOriginalInfo.setPendingNum(erpOrderOriginalInfo.getPendingNum() - splitGoodList.getNum());
            erpOrderOriginalInfo.setProcessNum(erpOrderOriginalInfo.getProcessNum() + splitGoodList.getNum());

            // 分单表数量
            erpOrderSplitInfo.setNum(erpOrderSplitInfo.getNum() + splitGoodList.getNum());
            // 封装分单商品信息
            ErpOrderSplitGood erpOrderSplitGood = extractErpOrderSplitGood(erpOrderSplitInfo, erpOrderOriginalGood, splitGoodList);
            // 更新分单商品信息
            erpOrderSplitGoodService.save(erpOrderSplitGood);

        }

        if (erpOrderSplitInfo.getNum() > 0) {
            // 设置分单流程版本号
            erpOrderSplitInfo.setProcessVersion(ActUtils.getLastJuYingKeProcessVersion());
            // 更新分单表数量信息
            super.save(erpOrderSplitInfo);
            // 更新原始订单信息数量
            erpOrderOriginalInfoService.save(erpOrderOriginalInfo);
            boolean flag = false;
            // 启动聚引客流程
            if (StringUtils.isEmpty(planningExpert)) {
                flag = workFlowService.startJykWorkFlowNew(users.get(0).getId(), erpOrderOriginalInfo.getId(), erpOrderSplitInfo.getId());
            } else {
                flag = workFlowService.startJykWorkFlowNew(planningExpert, erpOrderOriginalInfo.getId(), erpOrderSplitInfo.getId());
            }

            logger.info("Thread[{}],原始订单分单操作结束,入参[{}],是否默认分单[{}] 流程启动[{}] 分单id[{}]", Thread.currentThread().getName(), splitGoodLists, isFist, flag,
                            erpOrderSplitInfo);

        }

        return erpOrderSplitInfo;
    }


    /**
     * 封装拆单信息
     *
     * @param erpOrderOriginalGood
     * @param erpOrderOriginalInfo
     * @param count
     * @return
     * @date 2018年4月2日
     * @author zjq
     */
    private ErpOrderSplitInfo extractErpOrderOriginalInfo(ErpOrderOriginalGood erpOrderOriginalGood, ErpOrderOriginalInfo erpOrderOriginalInfo,
                    int count, List<User> users, String planningExpert, boolean isFirst) {
        ErpOrderSplitInfo erpOrderSplitInfo = new ErpOrderSplitInfo();
        erpOrderSplitInfo.setOrderId(erpOrderOriginalGood.getOrderId());
        erpOrderSplitInfo.setShopId(erpOrderOriginalInfo.getShopId());
        erpOrderSplitInfo.setOrderNumber(erpOrderOriginalInfo.getOrderNumber());
        if (isFirst) {
            erpOrderSplitInfo.setSplitId(CON_ZERO);
        } else {
            erpOrderSplitInfo.setSplitId(count + 1);
        }
        erpOrderSplitInfo.setOriginalGoodId(erpOrderOriginalGood.getId());
        erpOrderSplitInfo.setStatus(ErpOrderSplitInfo.STATUS_PROCESS);
        // if (!StringUtils.isEmpty(planningExpert)) {
        // erpOrderSplitInfo.setPlanningExpert(planningExpert);
        // }
        // if ((StringUtils.isEmpty(planningExpert) && !users.isEmpty())) {
        // erpOrderSplitInfo.setPlanningExpert(users.get(CON_ZERO).getId());
        // }
        return erpOrderSplitInfo;
    }

    /**
     * 封装分单商品信息
     *
     * @param erpOrderSplitInfo
     * @param erpOrderOriginalGood
     * @return
     * @date 2018年4月2日
     * @author zjq
     */
    private ErpOrderSplitGood extractErpOrderSplitGood(ErpOrderSplitInfo erpOrderSplitInfo, ErpOrderOriginalGood erpOrderOriginalGood,
                    SplitGoodForm splitGoodList) {

        ErpOrderSplitGood erpOrderSplitGood = new ErpOrderSplitGood();
        erpOrderSplitGood.setOriginalSplitId(erpOrderSplitInfo.getId());
        erpOrderSplitGood.setOriginalGoodId(erpOrderOriginalGood.getId());
        erpOrderSplitGood.setGoodName(erpOrderOriginalGood.getGoodName());
        erpOrderSplitGood.setGoodTypeId(erpOrderOriginalGood.getGoodTypeId());
        erpOrderSplitGood.setGoodTypeName(erpOrderOriginalGood.getGoodTypeName());
        erpOrderSplitGood.setNum(splitGoodList.getNum());
        erpOrderSplitGood.setPrice(erpOrderOriginalGood.getRealPrice());
        return erpOrderSplitGood;
    }

    private Integer countByOrderId(String orderId) {
        return erpOrderSplitInfoDao.countByOrderId(orderId);
    }

    public List<ErpOrderSplitInfo> findListByOrderId(String orderId) {
        return erpOrderSplitInfoDao.findListByOrderId(orderId);
    }

    public List<ErpOrderSplitInfo> findListByOrderInfo(String orderId, Integer goodType) {
        return erpOrderSplitInfoDao.findListByOrderInfo(orderId, goodType);
    }

    /**
     * 编辑分单任务量，大于当前任务量，则扣除订单商品待处理量，如果小于当前任务量，则将多余的返回给订单商品待处理量
     *
     * @param id
     * @param num
     * @return
     * @date 2017年10月26日
     * @author huanghaidong
     */
    @Transactional(readOnly = false)
    public boolean editNum(String id, Integer num) {
        ErpOrderSplitInfo erpOrderSplitInfo = get(id);
        int difference = num - erpOrderSplitInfo.getNum();
        if (difference > 0) {
            ErpOrderOriginalGood erpOrderOriginalGood = erpOrderOriginalGoodService.get(erpOrderSplitInfo.getOriginalGoodId());
            if (erpOrderOriginalGood.getPendingNum() < difference) {
                return false;
            }
        }
        erpOrderSplitInfoDao.updateNum(id, num);
        erpOrderOriginalGoodService.decreasePendingNum(erpOrderSplitInfo.getOriginalGoodId(), difference);
        return true;
    }


    /**
     * 编辑分单商品数量
     *
     * @return
     * @date 2018年4月2日
     * @author zjq
     */
    @Transactional(readOnly = false)
    public boolean editMultiNum(List<SplitGoodEditNumForm> multiNumLists) {

        logger.info("Thread[{}],编辑分单数量,入参[{}]", Thread.currentThread().getName(), multiNumLists);

        ErpOrderSplitInfo erpOrderSplitInfo = null;
        ErpOrderOriginalInfo erpOrderOriginalInfo = null;

        for (SplitGoodEditNumForm multiNumForm : multiNumLists) {

            // 获取分单信息
            ErpOrderSplitGood erpOrderSplitGood = erpOrderSplitGoodService.get(multiNumForm.getId());
            erpOrderSplitInfo = erpOrderSplitInfoDao.get(erpOrderSplitGood.getOriginalSplitId());
            // 获取原始订单信息
            ErpOrderOriginalGood erpOrderOriginalGood = erpOrderOriginalGoodService.get(erpOrderSplitGood.getOriginalGoodId());
            erpOrderOriginalInfo = erpOrderOriginalInfoService.get(erpOrderOriginalGood.getOrderId());

            int difference = multiNumForm.getNum() - erpOrderSplitGood.getNum();

            // 编辑的数量 > 分单商品的数量 增加分单商品数量 减少原始订单数量
            if (multiNumForm.getNum() < 0 || (difference > 0 && erpOrderOriginalGood.getPendingNum() < difference)) {

                logger.error("编辑数量合法/编辑数量 >原始订单可编辑数量 编辑数量[{}] ,分单商品数量[id=%s num=%s],原始订单数量 [id=%s num=%s]", multiNumForm, erpOrderSplitGood.getId(),
                                erpOrderSplitGood.getNum(), erpOrderOriginalGood.getId(), erpOrderOriginalGood.getNum());
                return false;
            }

            // 更新原始订单商品待处理数量
            erpOrderOriginalGood.setPendingNum(erpOrderOriginalGood.getPendingNum() - difference);
            // 更新原始订单商品流程中数量
            erpOrderOriginalGood.setProcessNum(erpOrderOriginalGood.getProcessNum() + difference);

            // 更新分单商品的数量
            erpOrderSplitGood.setNum(erpOrderSplitGood.getNum() + difference);

            // 更新原始订单待处理数量
            erpOrderOriginalInfo.setPendingNum(erpOrderOriginalInfo.getPendingNum() - difference);
            // 更新原始订单流程中数量
            erpOrderOriginalInfo.setProcessNum(erpOrderOriginalInfo.getProcessNum() + difference);
            // 更新分单信息数量
            erpOrderSplitInfo.setNum(erpOrderSplitInfo.getNum() + difference);

            if (erpOrderSplitGood.getNum() > 0) {
                erpOrderSplitGoodService.save(erpOrderSplitGood);
            } else {
                // 设置删除标识
                erpOrderSplitGood.setDelFlag(Constant.STRCONSTANT);
                erpOrderSplitGoodService.delete(erpOrderSplitGood);


            }
            erpOrderOriginalGoodService.save(erpOrderOriginalGood);
            this.save(erpOrderSplitInfo);
            // 更新分单数量
            erpOrderOriginalInfoService.save(erpOrderOriginalInfo);
        }



        logger.info("Thread[{}],编辑分单数量结束,入参[{}]", Thread.currentThread().getName(), multiNumLists);

        return true;
    }

    /**
     * 修改策划专家
     *
     * @param planningExpert
     * @param splitId
     * @return
     * @date 2018年4月8日
     * @author zjq
     */
    @Transactional(readOnly = false)
    public boolean modifyPlanningExpert(String planningExpert, String splitId) {

        logger.info("策划专家修改[modifyPlanningExpert]==start==,splitId[{}],planningExpert[{}]", splitId, planningExpert);

        ErpOrderSplitInfo erpOrderSplitInfo = erpOrderSplitInfoDao.get(splitId);
        if (null != erpOrderSplitInfo && planningExpert.equalsIgnoreCase(erpOrderSplitInfo.getPlanningExpert())) {
            logger.info("策划专家修改前和修改后相同,splitId[{}],planningExpert[{}],DB[{}]", splitId, planningExpert, erpOrderSplitInfo);
            return true;
        }
        // 更新策划专家
        Integer result = erpOrderSplitInfoDao.updatePlanningExpert(planningExpert, splitId);
        if (result > 0) {
            updateErpOrderFlowUser(planningExpert, erpOrderSplitInfo);

            if (null == erpOrderSplitInfo) {
                logger.info("策划专家修改[modifyPlanningExpert] erpOrderSplitInfo is null ==start==,splitId[{}],planningExpert[{}]", splitId,
                                planningExpert);
                return false;
            }

            List<String> tasks = actDao.findTaskIdByProcInsId(erpOrderSplitInfo.getProcInsId());


            for (String taskId : tasks) {

                String role = actDao.getTaskRole(taskId, ActUtils.JYK_FLOW_NEW[0]);
                // 过滤掉角色信息不明确的任务
                if (StringUtils.isNotBlank(role) && Constant.PLANNING_PERSON.equalsIgnoreCase(role)) {
                    // 将对应任务的处理人都修改
                    this.workFlowService.getProcessEngine().getTaskService().setAssignee(taskId, planningExpert);
                    this.workFlowService.getProcessEngine().getTaskService().setVariable(taskId, JykFlowConstants.TASK_USER, planningExpert);
                }


            }

            logger.info("策划专家修改[modifyPlanningExpert] ==end==,splitId[{}],planningExpert[{}] ", splitId, planningExpert);

            return true;
        }
        return false;
    }

    /**
     * 更新订单加急状态
     *
     * @param id
     * @param hurryFlag
     * @return
     * @date 2017年10月31日
     * @author yunnex
     */
    @Transactional(readOnly = false)
    public boolean updateHurryFlag(String id, Integer hurryFlag) {
        return erpOrderSplitInfoDao.updateHurryFlag(id, hurryFlag) > 0;
    }

    /**
     * 通过查询条件获取分单信息
     *
     * @param shopName
     * @param orderNumber
     * @param hurryFlag
     * @return
     * @date 2017年10月31日
     * @author yunnex
     */
    public List<ErpOrderSplitInfo> findListByParams(String shopName, String orderNumber, Integer hurryFlag, List<String> goodTypes) {
        ErpOrderSplitInfo erpOrderSplitInfo = new ErpOrderSplitInfo();
        erpOrderSplitInfo.setShopName(shopName);
        erpOrderSplitInfo.setOrderNumber(orderNumber);
        erpOrderSplitInfo.setHurryFlag(hurryFlag);
        erpOrderSplitInfo.setGoodTypes(goodTypes);
        return erpOrderSplitInfoDao.findListByParams(erpOrderSplitInfo);
    }

    /**
     * 通过查询条件获取分单信息
     *
     * @param shopName
     * @param orderNumber
     * @param hurryFlag
     * @return
     * @date 2017年10月31日
     * @author yunnex
     */
    public List<String> findFollowOrderByParams(String shopName, String orderNumber, Integer hurryFlag, List<String> goodTypes) {
        ErpOrderSplitInfo erpOrderSplitInfo = new ErpOrderSplitInfo();
        erpOrderSplitInfo.setShopName(shopName);
        erpOrderSplitInfo.setOrderNumber(orderNumber);
        erpOrderSplitInfo.setHurryFlag(hurryFlag);
        erpOrderSplitInfo.setUserId(UserUtils.getUser().getId());
        // 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
        return erpOrderSplitInfoDao.findFollowOrderByParams(erpOrderSplitInfo);
    }

    public ErpOrderSplitInfo getByProsIncId(String procInsId) {
        return erpOrderSplitInfoDao.getByProcInstId(procInsId);
    }

    public List<ErpOrderSplitInfo> findListByOrderInfoAndUser(String orderId, Integer goodType) {
        return erpOrderSplitInfoDao.findListByOrderInfoAndUser(orderId, goodType);
    }

    /**
     * 初始化 推广参数，根据promotionChannelList 如果存在设置成1，不存在设置成0
     * 
     * @date 2017年11月20日
     * @author czj
     */
    public void initExtendParams(Map<String, Object> vars, List<JykOrderPromotionChannel> promotionChannelList) {
        vars.put("isfriends", 0);
        vars.put("isweibo", 0);
        vars.put("ismomo", 0);
        for (JykOrderPromotionChannel promotionChannel : promotionChannelList) {
            if ("1".equals(promotionChannel.getPromotionChannel())) {
                vars.put("isfriends", 1);
            } else if ("2".equals(promotionChannel.getPromotionChannel())) {
                vars.put("isweibo", 1);
            } else if ("3".equals(promotionChannel.getPromotionChannel())) {
                vars.put("ismomo", 1);
            }
        }
    }

    /**
     * 通过查询条件获取分单信息
     *
     * @param shopName
     * @param orderNumber
     * @param hurryFlag
     * @return
     * @date 2017年12月27日
     * @author SunQ
     */
    public List<ErpOrderSplitInfo> findListOrderInfoAndTask(String shopName, String orderNumber, Integer hurryFlag, List<String> goodTypes,
                    List<String> userIds) {
        ErpOrderSplitInfo erpOrderSplitInfo = new ErpOrderSplitInfo();
        erpOrderSplitInfo.setShopName(shopName);
        erpOrderSplitInfo.setOrderNumber(orderNumber);
        erpOrderSplitInfo.setHurryFlag(hurryFlag);
        erpOrderSplitInfo.setGoodTypes(goodTypes);
        return erpOrderSplitInfoDao.findListOrderInfoAndTask(erpOrderSplitInfo, userIds);
    }

    public List<String> findProcIdListByShopId(String shopId) {
        return erpOrderSplitInfoDao.findProcIdListByShopId(shopId);
    }

    public Integer WhereShopIdCount(String shopid, String date, String del) {
        return erpOrderSplitInfoDao.WhereShopIdCount(shopid, date, del);
    }

    public Date getPromotionEndTime(String splitId) {
        return erpOrderSplitInfoDao.getPromotionEndTime(splitId);
    }

    @Transactional(readOnly = false)
    public boolean publishToWxapp(String splitId) {
        return erpOrderSplitInfoDao.publishToWxapp(splitId);
    }

    /*
     * V3.2 public List<ErpOrderSplitInfo> promotionalMaterials(String orderNumber, String shopName,
     * String status, String planningExpert) { return
     * erpOrderSplitInfoDao.promotionalMaterials(orderNumber, shopName, status, planningExpert); }
     */
    public List<ErpOrderSplitInfo> promotionalMaterials(String orderNumber, String shopName) {
        return erpOrderSplitInfoDao.promotionalMaterials(orderNumber, shopName);
    }

    public Map<String, String> getDiagnosisTaskInfo(String splitId) {
        return erpOrderSplitInfoDao.getDiagnosisTaskInfo(splitId);
    }

    public Integer findWhereUnderway(List<String> userIds) {
        return erpOrderSplitInfoDao.findWhereUnderway(userIds);
    }

    public Integer findWhereQualifications(List<String> userIds) {
        return erpOrderSplitInfoDao.findWhereQualifications(userIds);
    }

    public Integer findWhereActiveDelay(List<String> userIds) {
        return erpOrderSplitInfoDao.findWhereActiveDelay(userIds);
    }

    public Integer findWhereRiskCount(List<String> userIds) {
        return erpOrderSplitInfoDao.findWhereRiskCount(userIds);
    }

    public Integer findstatisticsReportNoCancel(List<String> userIds) {
        return erpOrderSplitInfoDao.findstatisticsReportNoCancel(userIds);
    }

    public List<ErpOrderSplitInfo> findWherefollowOrder(String assignne) {
        return erpOrderSplitInfoDao.findWherefollowOrder(assignne);
    }

    public List<ErpOrderSplitInfo> findWheretaskOrder(String assignne) {
        return erpOrderSplitInfoDao.findWheretaskOrder(assignne);
    }

    // 有过资质问题进行中订单
    public Integer findAllQualifications(List<String> userIds) {
        return erpOrderSplitInfoDao.findAllQualifications(userIds);
    }

    public List<ErpOrderSplitInfo> getByOrderId(String orderId) {
        return dao.getByOrderId(orderId);
    }

    public List<ErpOrderSplitInfo> findByOrderId(String orderId) {
        return dao.findByOrderId(orderId);
    }

    // 有过主动延迟的订单
    public Integer findAllActiveDelay(List<String> userIds) {
        return erpOrderSplitInfoDao.findAllActiveDelay(userIds);
    }

    @Transactional
    public BaseResult suspend(ErpOrderSplitInfo orderSplitInfo) {
        logger.info("-----------暂停流程 Start-----------------");
        logger.info("入参情况:id={},suspendReasonContent={},nextContactTime={},suspendReason={}", orderSplitInfo.getId(),
                        orderSplitInfo.getSuspendReasonContent(), orderSplitInfo.getNextContactTime(), orderSplitInfo.getSuspendReason());
        BaseResult result = new BaseResult();
        if (orderSplitInfo == null || StringUtils.isBlank(orderSplitInfo.getId())) {
            return new IllegalArgumentErrorResult();
        }

        if (StringUtils.isBlank(orderSplitInfo.getSuspendReason())) {
            return new BaseResult().error(OrderSplitConstants.INVALID_DATA_ERROR, "暂停原因未选择");
        }

        if (orderSplitInfo.getNextContactTime() == null) {
            return new BaseResult().error(OrderSplitConstants.INVALID_DATA_ERROR, "下次与商户沟通时间未选择");
        }

        // 下次与商户沟通时间必须大于服务器当前时间
        if (orderSplitInfo.getNextContactTime().getTime() <= new Date().getTime()) {
            return new BaseResult().error(OrderSplitConstants.INVALID_DATA_ERROR, "下次与商户沟通时间应大于当前系统时间");
        }

        // 更新拆单信息（是否暂停Y，暂停原因，具体暂停原因，是否待生产库Y，是否进入过待生产库Y,下次联络时间）
        ErpOrderSplitInfo erpOrderSplitInfo = erpOrderSplitInfoDao.get(orderSplitInfo.getId());
        if (erpOrderSplitInfo != null) {
            String suspendReason = orderSplitInfo.getSuspendReason();
            erpOrderSplitInfo.setSuspendFlag(Constant.YES);// 是否暂停
            erpOrderSplitInfo.setSuspendReason(suspendReason);// 暂停原因
            erpOrderSplitInfo.setSuspendReasonContent(orderSplitInfo.getSuspendReasonContent());// 具体暂停原因
            erpOrderSplitInfo.setNextContactTime(orderSplitInfo.getNextContactTime());// 下次联络时间
            erpOrderSplitInfo.setPendingProdFlag(Constant.YES);// 是否待生产库
            erpOrderSplitInfo.setPendingProduced(Constant.YES);// 是否进入过待生产库
            // 暂停原因是"存在资质问题"或"商户主动要求延迟上线"需要同步更新待生产库原因。
            if (suspendReason.equals(OrderSplitConstants.SUSPEND_REASON_QUALIFICATION)) {
                erpOrderSplitInfo.setPendingReason(OrderSplitConstants.PENDING_REASON_QUALIFICATION);
            }
            if (suspendReason.equals(OrderSplitConstants.SUSPEND_REASON_DELAY)) {
                erpOrderSplitInfo.setPendingReason(OrderSplitConstants.PENDING_REASON_DELAY);
            }
            erpOrderSplitInfo.preUpdate();
            erpOrderSplitInfoDao.update(erpOrderSplitInfo);

            // 修改拆单下面的任务表中的"是否待生产库"为Y(ACT_RU_TASK_EXT的pending_prod_flag为Y)
            updatePendingProdFlag(erpOrderSplitInfo, Constant.YES);
        }

        logger.info("出参情况:BaseResult={}", result);
        logger.info("-----------暂停流程 End-----------------");
        return result;
    }

    private void updatePendingProdFlag(ErpOrderSplitInfo erpOrderSplitInfo, String pendingProdFlag) {
        String procInsId = erpOrderSplitInfo.getProcInsId();
        if (StringUtils.isNotBlank(procInsId)) {
            List<String> taskIdList = actDao.findTaskIdByProcInsId(procInsId);
            if (CollectionUtils.isNotEmpty(taskIdList)) {
                TaskExt taskExt = new TaskExt();
                taskExt.setPendingProdFlag(pendingProdFlag);
                taskExt.preUpdate();
                for (String taskId : taskIdList) {
                    taskExtService.updateTaskExtInfoByTaskId(taskExt, taskId);
                }
            }
        }
    }

    public BaseResult getSuspendData(String id) {
        logger.info("-----------重启流程获取回显数据 Start-----------------");
        logger.info("入参情况:id={}", id);
        if (StringUtils.isBlank(id)) {
            return new IllegalArgumentErrorResult();
        }

        Map<String, Object> resultMap = new HashMap<>();
        ErpOrderSplitInfo erpOrderSplitInfo = erpOrderSplitInfoDao.get(id);
        if (erpOrderSplitInfo != null) {
            resultMap.put("suspendReason", erpOrderSplitInfo.getSuspendReason());
            resultMap.put("suspendReasonContent", erpOrderSplitInfo.getSuspendReasonContent());
            resultMap.put("nextContactTime", DateUtils.formatDateTime(erpOrderSplitInfo.getNextContactTime()));
        }

        BaseResult result = new BaseResult();
        result.setAttach(resultMap);
        logger.info("出参情况:BaseResult={}", result);
        logger.info("-----------重启流程获取回显数据 End-----------------");
        return result;
    }

    @Transactional(readOnly = false)
    public BaseResult updateSuspendData(ErpOrderSplitInfo orderSplitInfo) {
        logger.info("-----------重启流程选择否更新暂停信息 Start-----------------");
        logger.info("入参情况:id={},suspendReasonContent={},nextContactTime={},suspendReason={}", orderSplitInfo.getId(),
                        orderSplitInfo.getSuspendReasonContent(), orderSplitInfo.getNextContactTime(), orderSplitInfo.getSuspendReason());

        BaseResult result = new BaseResult();
        if (orderSplitInfo == null || StringUtils.isBlank(orderSplitInfo.getId())) {
            return new IllegalArgumentErrorResult();
        }

        ErpOrderSplitInfo erpOrderSplitInfo = erpOrderSplitInfoDao.get(orderSplitInfo.getId());
        if (erpOrderSplitInfo != null) {
            erpOrderSplitInfo.setSuspendReason(orderSplitInfo.getSuspendReason());// 暂停原因
            erpOrderSplitInfo.setSuspendReasonContent(orderSplitInfo.getSuspendReasonContent());// 具体暂停原因
            erpOrderSplitInfo.setNextContactTime(orderSplitInfo.getNextContactTime());// 下次联络时间
            erpOrderSplitInfo.preUpdate();
            erpOrderSplitInfoDao.update(erpOrderSplitInfo);
        }

        logger.info("出参情况:BaseResult={}", result);
        logger.info("-----------重启流程选择否更新暂停信息 End-----------------");
        return result;
    }

    @Transactional(readOnly = false)
    public BaseResult restart(String id) {
        logger.info("-----------重启流程 Start-----------------");
        logger.info("入参情况:id={}", id);

        BaseResult result = new BaseResult();
        if (StringUtils.isBlank(id)) {
            return new IllegalArgumentErrorResult();
        }
        // 更新拆单信息（是否暂停N，是否待生产库N）
        ErpOrderSplitInfo erpOrderSplitInfo = erpOrderSplitInfoDao.get(id);
        if (erpOrderSplitInfo != null) {
            erpOrderSplitInfo.setSuspendFlag(Constant.NO);// 是否暂停
            erpOrderSplitInfo.setPendingProdFlag(Constant.NO);// 是否待生产库
            erpOrderSplitInfo.setActivationTime(new Date()); // 激活时间
            erpOrderSplitInfo.preUpdate();
            erpOrderSplitInfoDao.update(erpOrderSplitInfo);

            // 修改拆单下面的任务表中的"是否待生产库"为N(ACT_RU_TASK_EXT的pending_prod_flag为N)
            updatePendingProdFlag(erpOrderSplitInfo, Constant.NO);
        }
        logger.info("出参情况:BaseResult={}", result);
        logger.info("-----------重启流程 End-----------------");
        return result;
    }

    public void updatePromotionTime(Date promotionTime, String splitId) {

    }

    /**
     * 根据经营诊断选择套餐信息，动态更新分单商品及数量
     *
     * @date 2018年4月13日
     * @author zjq
     */
    @Transactional(readOnly = false)
    public void updateSplitGoodInfoByDiagnosis(String splitId) {

        DiagnosisForm diagnosisForm = diagnosisFormService.getDiagnosisFormBySplitId(splitId);

        ErpOrderSplitInfo erpOrderSplitInfo = this.get(splitId);

        List<ErpOrderSplitGood> erpOrderSplitGoods = erpOrderSplitInfo.getErpOrderSplitGoods();

        if (null != diagnosisForm && null != erpOrderSplitInfo) {

            String splitGoodIdStr = diagnosisForm.getPackageSelection();

            logger.info("根据经营诊断选择套餐信息，动态更新分单商品及数量==start==,分单id [{}],选择套餐信息[{}],分单商品信息[{}]", splitId, splitGoodIdStr, erpOrderSplitGoods);

            List<SplitGoodEditNumForm> multiNumLists = new ArrayList<SplitGoodEditNumForm>();

            // 封装表单数据
            for (ErpOrderSplitGood splitGood : erpOrderSplitGoods) {

                if (Constant.YES.equalsIgnoreCase(splitGood.getIsPromote())) {

                    // 选中套餐
                    SplitGoodEditNumForm editNumForm = new SplitGoodEditNumForm();
                    editNumForm.setId(splitGood.getId());
                    editNumForm.setNum(splitGood.getNum());
                    multiNumLists.add(editNumForm);
                    continue;
                }
                // 未选中套餐
                SplitGoodEditNumForm editNumForm = new SplitGoodEditNumForm();
                editNumForm.setId(splitGood.getId());
                editNumForm.setNum(0);
                multiNumLists.add(editNumForm);

            }
            // 更新分单数据
            editMultiNum(multiNumLists);

            logger.info("根据经营诊断选择套餐信息，动态更新分单商品及数量==end==,分单id [{}],选择套餐信息[{}],分单商品信息[{}]", splitId, splitGoodIdStr, erpOrderSplitGoods);
        }

    }


    /**
     * 流程节点<指派策划专家>
     *
     * @param taskId
     * @param planningExpert
     * @return
     * @date 2018年4月17日
     * @author zjq
     */
    @Transactional(readOnly = false)
    public JSONObject assignePlanningExpert(String taskId, String planningExpert, String splitId) {

        JSONObject resObject = new JSONObject();

        logger.info("指派策划专家=start:assignePlanningExpert:taskId{},planningExpert{}", taskId, planningExpert);

        if (StringUtils.isNotEmpty(taskId) && StringUtils.isNotEmpty(planningExpert)) {

            ErpOrderSplitInfo erpOrderSplitInfo = this.get(splitId);


            Map<String, Object> vars = Maps.newHashMap();
            // 设置任务处理人
            vars.put(JykFlowConstants.Planning_Expert, planningExpert);
            vars.put(JykFlowConstants.TASK_USER, planningExpert);
            // 更新流程中的策划专家
            modifyPlanningExpert(planningExpert, erpOrderSplitInfo.getId());

            this.workFlowService.completeFlow2(new String[] {JykFlowConstants.Planning_Expert}, taskId, erpOrderSplitInfo.getProcInsId(),
                            "确认订单信息,指派策划专家", vars);

            // 聚引客流程信息
            JykFlow flow = jykFlowService.getByProcInstId(erpOrderSplitInfo.getProcInsId());
            if (null != flow) {
                flow.setPlanningExpertInterface(planningExpert);
                flow.setPlanningExpert(planningExpert);
                jykFlowDao.update(flow);
            } else {
                flow = new JykFlow();
                flow.setOrderId(erpOrderSplitInfo.getOrderId());
                flow.setSplitId(erpOrderSplitInfo.getId());
                flow.setPlanningExpertInterface(planningExpert);
                flow.setPlanningExpert(planningExpert);
                flow.setProcInsId(erpOrderSplitInfo.getProcInsId());
                jykFlowService.save(flow);
            }

            resObject.put("result", true);
        } else {
            resObject.put("result", false);
            resObject.put("message", "请选择需要推广的门店!");
        }

        logger.info("指派策划专家=end:assignePlanningExpert:taskId{},planningExpert{}", taskId, planningExpert);

        return resObject;
    }

    protected void updateErpOrderFlowUser(String planningExpert, ErpOrderSplitInfo erpOrderSplitInfo) {
        ErpOrderFlowUser erpOrderFlowUser = erpOrderFlowUserDao.findByProcInsIdAndRoleName(erpOrderSplitInfo.getProcInsId(),
                        JykFlowConstants.Planning_Expert);
        erpOrderFlowUser.getUser().setId(planningExpert);
        erpOrderFlowUser.setOrderId(erpOrderSplitInfo.getOrderId());
        erpOrderFlowUser.setSplitId(erpOrderSplitInfo.getId());
        erpOrderFlowUser.setUpdateBy(UserUtils.getUser());
        erpOrderFlowUser.setUpdateDate(new Date());
        erpOrderFlowUser.setFlowId(erpOrderSplitInfo.getProcInsId());
        erpOrderFlowUserDao.update(erpOrderFlowUser);
    }

    public Integer deleteSplitInfoById(String id) {
        return erpOrderSplitInfoDao.deleteSplitInfoById(id);
    }

    public ErpOrderFile getOrderFileById(String id) {
        return orderfileService.get(id);
    }

    public List<ErpOrderSplitInfo> findByOrderNumber(String orderNumber) {
        if (StringUtils.isBlank(orderNumber)) {
            return new ArrayList<>();
        }
        return dao.findByOrderNumber(orderNumber);
    }

    /**
     * 读取推广时间。<br/>
     * 3.2版本前流程（V1.1 、2.6、3.1流程）的读取“推广上线”任务完成的时间；<br/>
     * 在V3.2流程，读取推广上线任务里输入的推广上线时间（各推广通道中最早上线的时间）；<br/>
     * 若最终, 推广上线时间是空的，则读取预期推广上线时间；<br/>
     *
     * @param splitInfo
     * @return
     */
    public Date getPromotionTime(ErpOrderSplitInfo splitInfo) {
        if (splitInfo == null) {
            return null;
        }

        Date date = null;
        if (splitInfo.getProcessVersion() < OrderSplitConstants.PROCESS_VERSION_302) {
            // “推广上线”任务完成的时间
            List<HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery()
                            .processInstanceId(splitInfo.getProcInsId()).activityId("work_promotion_online")
                            .orderByHistoricActivityInstanceStartTime().desc().listPage(0, 1);
            if (CollectionUtils.isNotEmpty(historicActivityInstances)) {
                date = historicActivityInstances.get(0).getEndTime();
            }
        } else {
            // 各推广通道中最早上线的时间
            List<JykOrderPromotionChannel> channels = promotionChannelService.findListBySplitId(splitInfo.getId());
            if (CollectionUtils.isNotEmpty(channels)) {
                List<Date> dates = Lists.newArrayList();
                for (JykOrderPromotionChannel channel : channels) {
                    if (channel.getPromoteStartDate() != null) {
                        dates.add(channel.getPromoteStartDate());
                    }
                }
                if (CollectionUtils.isNotEmpty(dates)) {
                    date = Collections.min(dates);
                }
            }
        }

        // 预期推广上线时间
        if (date == null) {
            date = splitInfo.getPromotionTime();
        }
        return date;
    }

    /**
     * 业务定义：重启工作流
     * 
     * @date 2018年8月30日
     * @author R/Q
     */
    @Transactional
    public Map<String, Object> resetWorkFlow(String procInsId, List<SplitGoodForm> splitGoodLists) {
        Map<String, Object> returnMap = Maps.newHashMap();
        logger.info("聚引客生产流程重启：procInsId={}", procInsId);// NOSONAR
        try {
            returnMap = this.resetCheck(procInsId);// 重启校验
            if (!returnMap.isEmpty()) {
                return returnMap;
            }
            erpOrderOriginalGoodService.resetPendingNum(splitGoodLists);// 重置数量
            ErpOrderSplitInfo result = ProcessStartContext.startByOrderSplit(splitGoodLists);// 使用分单法重启该流程
            if (result == null) {
                logger.error("聚引客生产流程终止后分单操作出错。");// NOSONAR
                returnMap.put(CommonConstants.RETURN_CODE, CommonConstants.RETURN_CODE_FAIL);
                returnMap.put(CommonConstants.RETURN_MESSAGE, CommonConstants.SYSTEM_ERROR_MESSAGE);
                return returnMap;
            }
            ErpOrderSplitInfo newObj = this.dao.get(result.getId());
            this.initData(procInsId, newObj);// 初始化数据
            workFlowMonitorService.endProcess(procInsId);// 终止原有流程
            returnMap.put(CommonConstants.RETURN_CODE, CommonConstants.RETURN_CODE_SUCCESS);
            // 通知重启
            serviceMessageService.restartFlow(procInsId);
            // 进度重启
            serviceProgressService.restartFlow(newObj.getProcInsId(), procInsId);
            logger.info("聚引客生产流程重启成功：newProcInsId={}", newObj.getProcInsId());// NOSONAR
        } catch (Exception e) {
            logger.error("聚引客生产流程重启操作出错。error={}", e);// NOSONAR
            returnMap.put(CommonConstants.RETURN_CODE, CommonConstants.RETURN_CODE_FAIL);
            returnMap.put(CommonConstants.RETURN_MESSAGE, CommonConstants.SYSTEM_ERROR_MESSAGE);
        }
        return returnMap;
    }

    /**
     * 业务定义：初始化数据
     * 
     * @date 2018年9月27日
     * @author R/Q
     */
    private void initData(String oldProcInsId, ErpOrderSplitInfo newObj) {
        ErpOrderSplitInfo oldObj = this.dao.getByProcInstId(oldProcInsId);
        newObj.setSplitId(oldObj.getSplitId());// 保持原有分单号
        // 分单表内策划专家字段同步
        ErpOrderFlowUser erpOrderFlowUser = erpOrderFlowUserService.findListByFlowId(oldProcInsId, JykFlowConstants.Planning_Expert);
        if (erpOrderFlowUser != null && erpOrderFlowUser.getUser() != null) {
            newObj.setPlanningExpert(erpOrderFlowUser.getUser().getId());
        }
        this.save(newObj);
        // 删除原有分单对应分单商品信息
        erpOrderSplitGoodService.deleteBySplitId(oldObj.getId());
        // 删除原有分单
        this.delete(oldObj);
        // 将终止的流程处理人赋值到重启的流程中
        erpOrderFlowUserService.copyFlowUsers(oldProcInsId, newObj.getProcInsId());
        // 初始化推广信息
        String storeId = jykOrderChoiceStoreService.getStoreIdByProcInsId(oldProcInsId);
        erpStoreInfoService.resetExtension(storeId);
        jykOrderChoiceStoreService.deleteByByProcInsId(oldProcInsId);
    }

    /**
     * 业务定义：重启校验
     * 
     * @date 2018年9月28日
     * @author R/Q
     */
    private Map<String, Object> resetCheck(String procInsId) {
        Map<String, Object> returnMap = Maps.newHashMap();
        String storeId = jykOrderChoiceStoreService.getStoreIdByProcInsId(procInsId);
        ErpStoreInfo erpStoreInfo = erpStoreInfoService.findOnetoManyAll(Global.NO, storeId);
        if (erpStoreInfo != null) {
            StringBuffer sb = new StringBuffer();
            if (Global.YES.equals(erpStoreInfo.getFriendExtension()) && erpStoreInfo.getFriend() != null && erpStoreInfo.getFriend()
                            .getAuditStatus() != StoreConstant.ADVERTISER_AUDIT_STATUS_PASS && erpStoreInfo.getFriend()
                                            .getAuditStatus() != StoreConstant.AdvertiserAuditStatus.UN_COMMIT.getStatus()) {
                sb.append("【朋友圈】");
            }
            if (Global.YES.equals(erpStoreInfo.getWeiboExtension()) && erpStoreInfo.getWeibo() != null && erpStoreInfo.getWeibo()
                            .getAuditStatus() != StoreConstant.ADVERTISER_AUDIT_STATUS_PASS && erpStoreInfo.getWeibo()
                                            .getAuditStatus() != StoreConstant.AdvertiserAuditStatus.UN_COMMIT.getStatus()) {
                sb.append("【微博】");
            }
            if (Global.YES.equals(erpStoreInfo.getMomoExtension()) && erpStoreInfo.getMomo() != null && erpStoreInfo.getMomo()
                            .getAuditStatus() != StoreConstant.ADVERTISER_AUDIT_STATUS_PASS && erpStoreInfo.getMomo()
                                            .getAuditStatus() != StoreConstant.AdvertiserAuditStatus.UN_COMMIT.getStatus()) {
                sb.append("【陌陌】");
            }
            if (StringUtils.isNotBlank(sb.toString())) {
                returnMap.put(CommonConstants.RETURN_CODE, CommonConstants.RETURN_CODE_FAIL);
                returnMap.put(CommonConstants.RETURN_MESSAGE, sb.toString() + "推广账号开户中，无法进行重启操作。");
            }
        }
        return returnMap;
    }

    @Transactional
    @SuppressWarnings("rawtypes")
    public void updateExceptionReason(String exception, Integer status, List ids) {
        dao.updateExceptionReason(exception, status, ids);
    }
}
