package com.yunnex.ops.erp.modules.workflow.flow.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.qualify.service.ErpShopExtensionQualifyService;
import com.yunnex.ops.erp.modules.workflow.channel.dao.JykOrderPromotionChannelDao;
import com.yunnex.ops.erp.modules.workflow.channel.entity.JykOrderPromotionChannel;
import com.yunnex.ops.erp.modules.workflow.channel.service.JykOrderPromotionChannelService;
import com.yunnex.ops.erp.modules.workflow.flow.common.JykFlowConstants;
import com.yunnex.ops.erp.modules.workflow.flow.dao.JykFlowDao;
import com.yunnex.ops.erp.modules.workflow.flow.entity.JykFlow;
import com.yunnex.ops.erp.modules.workflow.user.service.ErpOrderFlowUserService;

/**
 * 聚引客流程处理信息表
 * 
 * @author yunnex
 * @date 2017年10月31日
 */
@Service
public class JykFlowContactShopService extends CrudService<JykFlowDao, JykFlow> {
    private static Logger logger = LoggerFactory.getLogger(JykFlowContactShopService.class);
    @Autowired
    private JykFlowDao jykFlowDao;
    /** 流程关联用户处理服务 */
    @Autowired
    private ErpOrderFlowUserService erpOrderFlowUserService;
    @Autowired
    private JykOrderPromotionChannelService jykOrderPromotionChannelService;
    @Autowired
    private ErpShopExtensionQualifyService erpShopExtensionQualifyService;
    @Autowired
    private ErpOrderSubTaskService erpOrderSubTaskService;
    @Autowired
    private WorkFlowService workFlowService;
    @Autowired
    private JykOrderPromotionChannelDao jykOrderPromotionChannelDao;

    /**
     * 指派策划专家
     * 
     * @param planningExpert
     * @date 2017年11月2日
     * @author yunnex
     */
    @Transactional(readOnly = false)
    public void assignPlanningExperts(String procInsId, String planningExpert, String taskId) {
        // 获取信息
        JykFlow jykFlow = jykFlowDao.getByProcInstId(procInsId);
        // 为防止更新其它字段
        JykFlow jykFlowUpdate = new JykFlow();
        jykFlowUpdate.setPlanningExpert(planningExpert);
        jykFlowUpdate.setProcInsId(procInsId);
        jykFlowUpdate.preUpdate();
        jykFlowDao.updateFlowByProcIncId(jykFlowUpdate);
        logger.info("更新聚引客订单策划人为:{}", planningExpert);
        // 插入订单流程信息表
        erpOrderFlowUserService.insertOrderFlowUser(planningExpert, jykFlow.getOrderId(), jykFlow.getSplitId(), JykFlowConstants.Planning_Expert,
                        procInsId);
        // 修改子任务完成状态
        workFlowService.submitSubTask(procInsId, "1", taskId);
    }

    /**
     * 策划专家指定订单的投放渠道
     * 
     * @param planningExpert
     * @date 2017年11月2日
     * @author yunnex
     */
    @Transactional(readOnly = false)
    public void choosePromotionChannel(String procInsId, String channelList, JykFlow jykFlow, String taskId) {
        // 先删除原来选择的渠道，再添加所选择的渠道
        jykOrderPromotionChannelDao.deleteChannels(jykFlow.getSplitId());
        String channel[] = channelList.split(",");
        // 插入订单推广渠道信息表
        for (int i = 0; i < channel.length; i++) {
            JykOrderPromotionChannel promotionChannel = new JykOrderPromotionChannel();
            promotionChannel.setOrderId(jykFlow.getOrderId());
            promotionChannel.setSplitId(jykFlow.getSplitId());
            promotionChannel.setPromotionChannel(channel[i]);
            jykOrderPromotionChannelService.save(promotionChannel);
        }
        // 修改子任务完成状态
        workFlowService.submitSubTask(procInsId, "1", taskId);
    }

    /**
     * 联系商户（服务商订单）
     *
     * @param procInsId
     * @param channelList
     * @param jykFlow
     * @date 2017年11月3日
     * @author yunnex
     */
    @Transactional(readOnly = false)
    public void contactService(String procInsId, String channelList, String taskId) {
        // 修改子任务完成状态
        workFlowService.submitSubTask(procInsId, "1", taskId);
    }

    /**
     * 判断商户是否为复投商户
     *
     * @return
     * @date 2017年11月3日
     * @author yunnex
     */
    public int getRecastbyProcInsId(String splitId, String shopId) {
        boolean isReCast = true;
        List<String> shopExtensionList = erpShopExtensionQualifyService.findExtensionQualifyList(shopId);
        List<JykOrderPromotionChannel> channelList = jykOrderPromotionChannelService.findListBySplitId(splitId);
        for (JykOrderPromotionChannel jykOrderPromotionChannel : channelList) {
            // 商户推广资质是否包含当前订单的推广渠道
            if (shopExtensionList.contains(jykOrderPromotionChannel.getPromotionChannel())) {} else {
                logger.info("商户不包含当前推广渠道信息|商户:{},分单编号:{},渠道编号:{}", shopId, splitId, jykOrderPromotionChannel.getPromotionChannel());
                isReCast = false;
                break;
            }
        }
        return isReCast ? 1 : 0;
    }

    /**
     * 策划专家
     *
     * @param taskId
     * @param procInsId
     * @param channelList
     * @return
     * @date 2017年11月3日
     * @author yunnex
     */
    /*@Transactional(readOnly = false)
    public void planningExpertContactShop(String procInsId, String channelList, String taskId) {
        String channel[] = channelList.split(",");
        // 插入订单推广渠道信息表
        for (int i = 0; i < channel.length; i++) {
            // 修改子任务完成状态
            this.erpOrderSubTaskService.updateState(taskId, channel[i]);
        }
    }*/

    /**
     * 指派策划经理
     * 
     * @param planningExpert
     * @date 2017年11月2日
     * @author yunnex
     */
    @Transactional(readOnly = false)
    public void assignOperationManager(String procInsId, String operationManager, String taskId) {
        // 获取信息
        JykFlow jykFlow = jykFlowDao.getByProcInstId(procInsId);
        // 为防止更新其它字段
        JykFlow jykFlowUpdate = new JykFlow();
        jykFlowUpdate.setOperationManager(operationManager);
        jykFlowUpdate.setProcInsId(procInsId);
        jykFlowUpdate.preUpdate();
        jykFlowDao.updateFlowByProcIncId(jykFlowUpdate);
        logger.info("更新聚引客订单运营经理为:{}", operationManager);
        // 插入订单流程信息表
        erpOrderFlowUserService.insertOrderFlowUser(operationManager, jykFlow.getOrderId(), jykFlow.getSplitId(), JykFlowConstants.OPERATION_MANAGER,
                        procInsId);
        // 修改子任务完成状态
        workFlowService.submitSubTask(procInsId, "1", taskId);
    }

    /**
     * 指派运营顾问
     * 
     * @param planningExpert
     * @date 2017年11月2日
     * @author yunnex
     */
    @Transactional(readOnly = false)
    public void assignOperationAdviser(String procInsId, String operationAdviser, String taskId, String channelList) {
        // 修改子任务完成状态
        workFlowService.submitSubTask(procInsId, channelList, taskId);
        // 指定运营顾问
        if (StringUtils.isNotBlank(operationAdviser)) {
            // 获取信息
            JykFlow jykFlow = jykFlowDao.getByProcInstId(procInsId);
            // 为防止更新其它字段
            JykFlow jykFlowUpdate = new JykFlow();
            jykFlowUpdate.setOperationAdviser(operationAdviser);
            jykFlowUpdate.setProcInsId(procInsId);
            jykFlowUpdate.preUpdate();
            jykFlowDao.updateFlowByProcIncId(jykFlowUpdate);
            logger.info("更新聚引客订单策划人为:{}", operationAdviser);
            // 插入订单流程信息表
            erpOrderFlowUserService.insertOrderFlowUser(operationAdviser, jykFlow.getOrderId(), jykFlow.getSplitId(),
                            JykFlowConstants.OPERATION_ADVISER, procInsId);
            // 修改子任务完成状态
            this.erpOrderSubTaskService.updateState(taskId, "1");
        }


    }



}
