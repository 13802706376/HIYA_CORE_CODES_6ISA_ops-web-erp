package com.yunnex.ops.erp.modules.workflow.channel.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.workflow.channel.constant.Constants;
import com.yunnex.ops.erp.modules.workflow.channel.dao.JykOrderPromotionChannelDao;
import com.yunnex.ops.erp.modules.workflow.channel.entity.JykOrderPromotionChannel;

/**
 * 聚引客订单推广渠道管理Service
 * @author Frank
 * @version 2017-10-27
 */
@Service
public class JykOrderPromotionChannelService extends CrudService<JykOrderPromotionChannelDao, JykOrderPromotionChannel> {
    @Autowired
    JykOrderPromotionChannelDao  dao;
    public JykOrderPromotionChannel get(String id) {
        return super.get(id);
    }
    public List<JykOrderPromotionChannel> findListBySplitId(String splitId) {
        return dao.findListBySplitId(splitId);
    }
    
    public List<JykOrderPromotionChannel> findList(JykOrderPromotionChannel jykOrderPromotionChannel) {
        return super.findList(jykOrderPromotionChannel);
    }
    
    public Page<JykOrderPromotionChannel> findPage(Page<JykOrderPromotionChannel> page, JykOrderPromotionChannel jykOrderPromotionChannel) {
        return super.findPage(page, jykOrderPromotionChannel);
    }
    
    @Transactional(readOnly = false)
    public void save(JykOrderPromotionChannel jykOrderPromotionChannel) {
        if (StringUtils.isEmpty(jykOrderPromotionChannel.getPromoteStatus())) {
            jykOrderPromotionChannel.setPromoteStatus(Constants.NOTSTART);
        }
        super.save(jykOrderPromotionChannel);
    }
    
    @Transactional(readOnly = false)
    public void delete(JykOrderPromotionChannel jykOrderPromotionChannel) {
        super.delete(jykOrderPromotionChannel);
    }
    
    public List<Integer> getChannels(String splitId) {
        return dao.getChannels(splitId);
    }
    
    public List<String> getChannelNames(String splitId) {
        return dao.getChannelNames(splitId);
    }

    public JykOrderPromotionChannel getChannelSelected(String splitId, String channel) {
        return dao.getChannelSelected(splitId, channel);
    }

    public void update(JykOrderPromotionChannel channel) {
        dao.update(channel);
    }
}