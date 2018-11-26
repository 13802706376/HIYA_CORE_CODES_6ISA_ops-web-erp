package com.yunnex.ops.erp.modules.workflow.flow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.service.BaseService;
import com.yunnex.ops.erp.modules.act.service.ActTaskService;
import com.yunnex.ops.erp.modules.store.advertiser.service.ErpStoreAdvertiserFriendsService;

/**
 * 朋友圈提审服务类 3.2版本
 * 
 * @author zjq
 * @date 2018年5月16日
 */
@Service
public class WeChatPromotionFlow3P2Service extends BaseService {
   
    @Autowired
    private ActTaskService actTaskService;
    @Autowired
    private ErpStoreAdvertiserFriendsService advertiserFriendsService;



    public void startMicroblogPromotionFlow(String friendsId) {


    }
}