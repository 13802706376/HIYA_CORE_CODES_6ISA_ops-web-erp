package com.yunnex.ops.erp.modules.workflow.flow.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.service.SystemService;
import com.yunnex.ops.erp.modules.workflow.flow.common.JykFlowConstants;
import com.yunnex.ops.erp.modules.workflow.flow.dao.SdiFlowDao;
import com.yunnex.ops.erp.modules.workflow.flow.entity.SdiFlow;
import com.yunnex.ops.erp.modules.workflow.user.entity.ErpOrderFlowUser;
import com.yunnex.ops.erp.modules.workflow.user.service.ErpOrderFlowUserService;

/**
 * 商户资料录入流程处理信息表
 * 
 * @author SunQ
 * @date 2017年12月9日
 */
@Service
public class SdiFlowService extends CrudService<SdiFlowDao, SdiFlow> {

    @Autowired
    private SdiFlowDao sdiFlowDao;
    @Autowired
    private SystemService systemService;
    @Autowired
    private ErpShopDataInputSubTaskService erpShopDataInputSubTaskService;
    @Autowired
    private ErpOrderFlowUserService erpOrderFlowUserService;
    /**
     * 获取商户资料录入业务表信息
     *
     * @param procInstId
     * @return
     * @date 2017年12月9日
     * @author SunQ
     */
    public SdiFlow getByProcInstId(String procInstId) {
        return sdiFlowDao.getByProcInstId(procInstId);
    }

    /**
     * 商户资料录入流程启动
     */
    @Override
    @Transactional(readOnly = false)
    public void save(SdiFlow sdiFlow) {
        sdiFlow.preInsert();
        dao.insert(sdiFlow);
    }
    
    /**
     * 更新内容
     * @param sdiFlow
     */
    @Transactional(readOnly = false)
    public void update(SdiFlow sdiFlow) {
        sdiFlowDao.update(sdiFlow);
    }
    
    /**
     * 通过订单ID获取商户资料录入业务表信息
     *
     * @param orderId
     * @return
     * @date 2018年1月5日
     * @author SunQ
     */
    public SdiFlow getSdiinfoByShopId(String shopId) {
        return sdiFlowDao.getSdiinfoByShopId(shopId);
    }
    
    /**
     * 指派运营顾问
     *
     * @param procInsId
     * @param operationAdviser
     * @param taskId
     * @param channelList
     * @date 2017年12月9日
     * @author SunQ
     */
    @Transactional(readOnly = false)
    public void assignOperationAdviser(String procInsId, String operationAdviser, String taskId) {
        // 指定运营顾问
        if (StringUtils.isNotBlank(operationAdviser)) {
            
            // 为防止更新其它字段
            SdiFlow sdiFlowUpdate = new SdiFlow();
            sdiFlowUpdate.setOperationAdviser(operationAdviser);
            sdiFlowUpdate.setProcInsId(procInsId);
            sdiFlowUpdate.preUpdate();
            sdiFlowDao.updateFlowByProcIncId(sdiFlowUpdate);
            logger.info("更新商户资料录入运营顾问为:{}", operationAdviser);
            
            // 修改子任务完成状态
            this.erpShopDataInputSubTaskService.updateState(taskId, "1");
        }
    }


    /**
     * 根据掌贝id获取运营顾问
     *
     * @param ShopId
     * @return
     * @date 2018年4月19日
     * @author zjq
     */
    public User findOperationAdviserByShopId(String ShopId) {

        // 获取商户运营顾问
        SdiFlow sdiInfo = getSdiinfoByShopId(ShopId);

        if (sdiInfo != null) {
            ErpOrderFlowUser erpOrderFlowUser = erpOrderFlowUserService.findByProcInsIdAndRoleName(sdiInfo.getProcInsId(),
                            JykFlowConstants.OPERATION_ADVISER);
            if (null != erpOrderFlowUser)
                return systemService.getUser(erpOrderFlowUser.getUser().getId());
        }
        return null;

    }


}