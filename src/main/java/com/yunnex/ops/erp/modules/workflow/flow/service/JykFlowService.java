package com.yunnex.ops.erp.modules.workflow.flow.service;

import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreInfoService;
import com.yunnex.ops.erp.modules.workflow.channel.service.JykOrderPromotionChannelService;
import com.yunnex.ops.erp.modules.workflow.store.service.JykOrderChoiceStoreService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.workflow.flow.common.JykFlowConstants;
import com.yunnex.ops.erp.modules.workflow.flow.dao.JykFlowDao;
import com.yunnex.ops.erp.modules.workflow.flow.entity.JykFlow;
import com.yunnex.ops.erp.modules.workflow.user.service.ErpOrderFlowUserService;

import java.util.List;

/**
 * 聚引客流程处理信息表
 * 
 * @author yunnex
 * @date 2017年10月31日
 */
@Service
public class JykFlowService extends CrudService<JykFlowDao, JykFlow> {
    @Autowired
    private JykFlowDao jykFlowDao;
    /** 流程关联用户处理服务 */
    @Autowired
    private ErpOrderFlowUserService erpOrderFlowUserService;

    @Autowired
    private ErpStoreInfoService erpStoreInfoService;
    @Autowired
    private JykOrderChoiceStoreService jykOrderChoiceStoreService;

    @Autowired
    private JykOrderPromotionChannelService promotionChannelService;

    /**
     * 获取聚引客业务表信息
     * 
     * @param procInstId
     * @return
     * @date 2017年10月31日
     * @author yunnex
     */
    public JykFlow getByProcInstId(String procInstId) {
        return jykFlowDao.getByProcInstId(procInstId);
    }

    /**
     * 聚引客流程启动
     */
    @Transactional(readOnly = false)
    public void save(JykFlow jykFlow) {
        jykFlow.preInsert();
        dao.insert(jykFlow);
        String flowUserType = JykFlowConstants.PLANNING_EXPERT_INTERFACE_MAN;
        // 插入流程关联用户信息表
        erpOrderFlowUserService.insertOrderFlowUser(jykFlow.getPlanningExpertInterface(), jykFlow.getOrderId(), jykFlow.getSplitId(), flowUserType,
                        jykFlow.getProcInsId());
    }

    /**
     * 修改门店及推广通道的推广状态
     *
     * @param splitId
     */
    @Transactional
    public String updateExtension(String splitId) {
        String result = "";
        String storeId = jykOrderChoiceStoreService.getStoreIdBySplitId(splitId);
        ErpStoreInfo erpStoreInfo = erpStoreInfoService.get(storeId);
        if (erpStoreInfo == null) {
            result = "没有推广的门店！";
            return result;
        }

        String flag = "1";

        // 推广门店
        erpStoreInfo.setStoreExtension(flag);

        result += "推广通道：";
        List<Integer> channels = promotionChannelService.getChannels(splitId);
        if (CollectionUtils.isNotEmpty(channels)) {
            for (Integer channel : channels) {
                String ch = String.valueOf(channel);
                if (Constant.CHANNEL_1.equals(ch)) {
                    erpStoreInfo.setFriendExtension(flag);
                } else if (Constant.CHANNEL_2.equals(ch)) {
                    erpStoreInfo.setWeiboExtension(flag);
                } else if (Constant.CHANNEL_3.equals(ch)) {
                    erpStoreInfo.setMomoExtension(flag);
                }
                result += ch + ", ";
            }
        }

        erpStoreInfoService.save(erpStoreInfo);
        result += "成功！";
        return result;
    }


}
