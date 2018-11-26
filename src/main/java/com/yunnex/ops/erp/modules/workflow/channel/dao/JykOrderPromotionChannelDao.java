package com.yunnex.ops.erp.modules.workflow.channel.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.workflow.channel.entity.JykOrderPromotionChannel;

/**
 * 聚引客订单推广渠道管理DAO接口
 * @author Frank
 * @version 2017-10-27
 */
@MyBatisDao
public interface JykOrderPromotionChannelDao extends CrudDao<JykOrderPromotionChannel> {
    
   List<JykOrderPromotionChannel> findListBySplitId(String splitId);
   
   boolean deleteChannels(String splitId);
   
   List<Integer> getChannels(String splitId);
   
   /**
    * 获取选择推广渠道的名称
    *
    * @param splitId
    * @return
    * @date 2018年1月30日
    * @author SunQ
    */
   List<String> getChannelNames(@Param("splitId") String splitId);


    JykOrderPromotionChannel getChannelSelected(@Param("splitId") String splitId, @Param("channel") String channel);
}