package com.yunnex.ops.erp.modules.workflow.channel.dao;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.workflow.channel.dto.WeiboRechargeRequestDto;
import com.yunnex.ops.erp.modules.workflow.channel.dto.WeiboRechargeResponseDto;
import com.yunnex.ops.erp.modules.workflow.channel.entity.ErpChannelWeiboRecharge;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 微博通道充值DAO接口
 * @author yunnex
 * @version 2018-05-08
 */
@MyBatisDao
public interface ErpChannelWeiboRechargeDao extends CrudDao<ErpChannelWeiboRecharge> {

    List<WeiboRechargeResponseDto> findByPage(WeiboRechargeRequestDto requestDto);

    long count(WeiboRechargeRequestDto requestDto);

    long countSplitWeibo(@Param("splitId") String splitId, @Param("weiboAccountNo") String weiboAccountNo, @Param("weiboUid") String weiboUid);
   
    List<WeiboRechargeResponseDto> findWeiboRechargeBysplitId (@Param("splitId") String splitId,@Param("source") String source);
    

}
