package com.yunnex.ops.erp.modules.workflow.user.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.constant.CommonConstants;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.common.utils.SpringContextHolder;
import com.yunnex.ops.erp.modules.act.service.ActTaskService;
import com.yunnex.ops.erp.modules.material.service.ErpOrderMaterialCreationService;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitInfo;
import com.yunnex.ops.erp.modules.order.service.ErpOrderSplitInfoService;
import com.yunnex.ops.erp.modules.statistics.entity.ErpTeamFollowOrder;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.visit.service.ErpVisitServiceInfoService;
import com.yunnex.ops.erp.modules.workflow.flow.common.JykFlowConstants;
import com.yunnex.ops.erp.modules.workflow.user.dao.ErpOrderFlowUserDao;
import com.yunnex.ops.erp.modules.workflow.user.entity.ErpOrderFlowUser;

/**
 * 工作流人员关系Service
 * 
 * @author Frank
 * @version 2017-10-27
 */
@Service
public class ErpOrderFlowUserService extends CrudService<ErpOrderFlowUserDao, ErpOrderFlowUser> {
    
    @Autowired
    private ActTaskService actTaskService;

    @Override
    @Transactional(readOnly = false)
    public void save(ErpOrderFlowUser erpOrderFlowUser) {
        super.save(erpOrderFlowUser);
    }

    @Transactional(readOnly = false)
    public void changeRoleUser(String procInsId, String userId, String roleName) {
        dao.changeRoleUser(procInsId, userId, roleName);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(ErpOrderFlowUser erpOrderFlowUser) {
        super.delete(erpOrderFlowUser);
    }

    @Transactional(readOnly = false)
    public void insertOrderFlowUser(String userId, String orderId, String splitId, String flowUserType, String flowId) {
        // 插入订单流程信息表
        ErpOrderFlowUser orderFlowUser = new ErpOrderFlowUser();
        ErpOrderFlowUser erpOrderFlowUser = dao.findByProcInsIdAndRoleName(flowId, flowUserType);
        orderFlowUser.setId(erpOrderFlowUser == null ? null : erpOrderFlowUser.getId());
        orderFlowUser.setOrderId(orderId);
        orderFlowUser.setSplitId(splitId);
        orderFlowUser.setFlowId(flowId);
        orderFlowUser.setUser(new User(userId));
        orderFlowUser.setFlowUserId(flowUserType);
        this.save(orderFlowUser);
        logger.info("插入订单关联用户角色表成功|对象:{}", orderFlowUser);
    }

    public ErpOrderFlowUser findListByFlowId(String procInsId, String flowUserId) {
        return dao.findListByFlowId(procInsId, flowUserId);
    }

    public List<ErpOrderFlowUser> findListByFlowIdAndUserId(String procInsId, String userId) {
        return dao.findListByFlowIdAndUserId(procInsId, userId);
    }

    public ErpOrderFlowUser findByProcInsIdAndRoleName(String procInsId, String roleName) {
        return dao.findByProcInsIdAndRoleName(procInsId, roleName);
    }

    public List<ErpOrderFlowUser> findtest() {
        return dao.findtest();
    }

    public List<ErpOrderFlowUser> findstatistics(@Param("userId") String userId, @Param("orderNum") String orderNum,
                    @Param("shopName") String shopName, @Param("orderType") String orderType, @Param("starDate") String starDate,
                    @Param("endDate") String endDate) {
        return dao.findstatistics(userId, orderNum, shopName, orderType, starDate, endDate);
    }

    public List<ErpOrderFlowUser> findstatistics2(@Param("userId") String userId, @Param("orderNum") String orderNum,
                    @Param("shopName") String shopName, @Param("orderType") String orderType, @Param("starDate") String starDate,
                    @Param("endDate") String endDate) {
        return dao.findstatistics2(userId, orderNum, shopName, orderType, starDate, endDate);
    }

    public List<ErpOrderFlowUser> findstatisticswhereteamId(String teamId, String orderNum, String shopName, String orderType, String starDate,
                    String endDate) {
        return dao.findstatisticswhereteamId(teamId, orderNum, shopName, orderType, starDate, endDate);
    }

    public List<ErpOrderFlowUser> findstatisticswhereteamId2(String teamId, String orderNum, String shopName, String orderType, String starDate,
                    String endDate) {
        return dao.findstatisticswhereteamId2(teamId, orderNum, shopName, orderType, starDate, endDate);
    }

    public List<ErpOrderFlowUser> findstatisticswhereuserId(String userId, String orderNum, String shopName, String orderType, String starDate,
                    String endDate) {
        return dao.findstatisticswhereuserId(userId, orderNum, shopName, orderType, starDate, endDate);
    }

    public List<ErpOrderFlowUser> findstatisticsReport(String userId, String starDate, String endDate) {
        return dao.findstatisticsReport(userId, starDate, endDate);
    }

    public List<ErpOrderFlowUser> findstatisticsReportNoCancel(String userId) {
        return dao.findstatisticsReportNoCancel(userId);
    }

    public List<ErpTeamFollowOrder> findstatisticsFollowOrder(String userId) {
        return dao.findstatisticsFollowOrder(userId);
    }

    public List<ErpTeamFollowOrder> findstatisticsFollowOrderWhereTeamId(String teamId) {
        return dao.findstatisticsFollowOrderWhereTeamId(teamId);
    }

    public List<ErpOrderFlowUser> findstatisticsReportOnline(String userId, String starDate, String endDate) {
        return dao.findstatisticsReportOnline(userId, starDate, endDate);

    }

    public Map<String, Object> statisticsWeekAndMonth(List<String> userIds, String startDate, String endDate) {
        return dao.statisticsWeekAndMonth(userIds, startDate, endDate);
    }

    public List<ErpOrderFlowUser> findoverTimestatistics(String userId, String orderNum, String shopName, String orderType, String starDate,
                    String endDate) {
        return dao.findoverTimestatistics(userId, orderNum, shopName, orderType, starDate, endDate);
    }

    public List<ErpOrderFlowUser> findoverTimewhereteamId(String teamId, String orderNum, String shopName, String orderType, String starDate,
                    String endDate) {
        return dao.findoverTimewhereteamId(teamId, orderNum, shopName, orderType, starDate, endDate);
    }

    public ErpOrderFlowUser findBySplitIdAndFlowUser(String splitId, String flowUserId) {
        return dao.findBySplitIdAndFlowUser(splitId, flowUserId);
    }

    public List<Map<String, String>> findByProcInsId(String procInsId) {
        return dao.findByProcInsId(procInsId);
    }
    
    public Map<String, Object> getFlowUser(String procInsId) {
        Map<String, Object> returnMap = Maps.newHashMap();
        List<Map<String, String>> flowUserList = dao.findByProcInsId(procInsId);// 当前流程处理人
        if (CollectionUtils.isNotEmpty(flowUserList)) {
            for (Map<String, String> map : flowUserList) {
                returnMap.put(map.get("role"), map);
            }
        }
        return returnMap;
    }

    public List<ErpOrderFlowUser> findByOrderId(String orderId) {
        return dao.findByOrderId(orderId);
    }

    @Transactional
    public void copyFlowUsers(String oldProcInsId, String newProcInsId) {
        dao.deleteByProcInsId(newProcInsId);
        dao.copyFlowUsers(oldProcInsId, newProcInsId);
    }

    /**
     * 业务定义：批量转派流程处理人
     * 
     * @date 2018年9月11日
     * @author R/Q
     */
    @Transactional
    public Map<String, Object> batchMergeFlowUser(String flowUsers) {
        Map<String, Object> returnMap = Maps.newHashMap();
        logger.info("流程处理人转派：flowUsers={}", flowUsers);// NOSONAR
        try {
            if (StringUtils.isNotBlank(flowUsers)) {
                List<ErpOrderFlowUser> erpOrderFlowUsers = JSON.parseArray(StringEscapeUtils.unescapeHtml4(flowUsers), ErpOrderFlowUser.class);
                if (CollectionUtils.isNotEmpty(erpOrderFlowUsers)) {
                    for (ErpOrderFlowUser erpOrderFlowUser : erpOrderFlowUsers) {
                        ErpOrderFlowUser dbObj = this.dao.findListByFlowId(erpOrderFlowUser.getFlowId(), erpOrderFlowUser.getFlowUserId());
                        if (dbObj == null) {
                            this.save(erpOrderFlowUser);
                        } else {
                            dbObj.setUser(erpOrderFlowUser.getUser());
                            this.save(dbObj);
                        }
                        // 修改当前处理人
                        actTaskService.changeRoleUser(erpOrderFlowUser.getFlowId(), erpOrderFlowUser.getUser().getId(),
                                        erpOrderFlowUser.getFlowUserId());
                        // 运营顾问转派
                        if (JykFlowConstants.OPERATION_ADVISER.equals(erpOrderFlowUser.getFlowUserId())) {
                            // 需要同步物料表中物料顾问字段
                            ErpOrderMaterialCreationService materialCreationService = SpringContextHolder
                                            .getBean(ErpOrderMaterialCreationService.class);
                            materialCreationService.updateAdviser(erpOrderFlowUser.getFlowId(), erpOrderFlowUser.getUser().getId());
                            // 需要同步上门服务处理人
                            ErpVisitServiceInfoService erpVisitServiceInfoService = SpringContextHolder.getBean(ErpVisitServiceInfoService.class);
                            erpVisitServiceInfoService.updateServiceUser(erpOrderFlowUser.getUser().getId(), erpOrderFlowUser.getFlowId());
                        }
                        // 策划专家转派需要同步更新分单表中策划专家字段
                        if (JykFlowConstants.Planning_Expert.equals(erpOrderFlowUser.getFlowUserId()) && StringUtils
                                        .isNotBlank(erpOrderFlowUser.getSplitId())) {
                            ErpOrderSplitInfoService splitService = SpringContextHolder.getBean(ErpOrderSplitInfoService.class);
                            ErpOrderSplitInfo erpOrderSplitInfo = splitService.get(erpOrderFlowUser.getSplitId());
                            erpOrderSplitInfo.setPlanningExpert(erpOrderFlowUser.getUser().getId());
                            splitService.save(erpOrderSplitInfo);
                        }
                    }
                }
            }
            returnMap.put(CommonConstants.RETURN_CODE, CommonConstants.RETURN_CODE_SUCCESS);
        } catch (Exception e) {
            logger.error("批量转派流程处理人出错。error={}", e);// NOSONAR
            returnMap.put(CommonConstants.RETURN_CODE, CommonConstants.RETURN_CODE_FAIL);
            returnMap.put(CommonConstants.RETURN_MESSAGE, CommonConstants.SYSTEM_ERROR_MESSAGE);
        }
        return returnMap;
    }

    /**
     * 业务定义：根据订单ID获取对应交付服务最新指定的流程节点处理人
     * 
     * @date 2018年10月18日
     * @author R/Q
     */
    public List<ErpOrderFlowUser> getDeliveryFlowUserByOrderId(String orderId) {
        return dao.getDeliveryFlowUserByOrderId(orderId);
    }
}
