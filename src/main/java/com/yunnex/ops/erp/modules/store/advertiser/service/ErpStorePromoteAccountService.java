package com.yunnex.ops.erp.modules.store.advertiser.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.modules.store.advertiser.dao.ErpStoreAdvertiserFriendsDao;
import com.yunnex.ops.erp.modules.store.advertiser.dao.ErpStoreAdvertiserMomoDao;
import com.yunnex.ops.erp.modules.store.advertiser.dao.ErpStoreAdvertiserWeiboDao;
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserFriends;
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserMomo;
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserWeibo;
import com.yunnex.ops.erp.modules.workflow.channel.dao.JykOrderPromotionChannelDao;

/**
 * 朋友圈广告主开通资料Service
 * @author yunnex
 * @version 2017-12-09
 */
@Service
public class ErpStorePromoteAccountService  {

    private static final int NUMBER_CHANNEL_1 = 1;
    private static final int NUMBER_CHANNEL_2 = 2;
    private static final int NUMBER_CHANNEL_3 = 3;
    
    private static final int NUMBER_STATE_4 = 4;
    
    @Autowired
    private ErpStoreAdvertiserFriendsDao erpStoreAdvertiserFriendsDao;
    
    @Autowired
    private ErpStoreAdvertiserMomoDao erpStoreAdvertiserMomoDao;
    
    @Autowired
    private ErpStoreAdvertiserWeiboDao erpStoreAdvertiserWeiboDao;
    
    @Autowired
    private JykOrderPromotionChannelDao jykOrderPromotionChannelDao;
    
    public boolean isPromoteAccountFinish(String storeId, String splitId) 
    {
        List<Integer> channels = jykOrderPromotionChannelDao.getChannels(splitId);
        for(Integer channel : channels){
            if(NUMBER_CHANNEL_1==channel.intValue()){
                ErpStoreAdvertiserFriends fri = erpStoreAdvertiserFriendsDao.getByStoreId(storeId);
                if(null==fri || (null!=fri && NUMBER_STATE_4!=fri.getAuditStatus())){
                    return false;
                }
            }
            
            if(NUMBER_CHANNEL_2==channel.intValue()){
                ErpStoreAdvertiserWeibo weibo = erpStoreAdvertiserWeiboDao.getByStoreId(storeId);
                if(null==weibo || (null!=weibo && NUMBER_STATE_4!=weibo.getAuditStatus())){
                    return false;
                }
            }
            
            if(NUMBER_CHANNEL_3==channel.intValue()){
                ErpStoreAdvertiserMomo momo = erpStoreAdvertiserMomoDao.getByStoreId(storeId);
                if(null==momo || (null!=momo && NUMBER_STATE_4!=momo.getAuditStatus())){
                    return false;
                }
            }
        }
        return true;
    }
}