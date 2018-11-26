package com.yunnex.ops.erp.modules.promotion.dao;

import java.util.List;
import java.util.Map;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.promotion.entity.ErpPromoteDataFriends;
import com.yunnex.ops.erp.modules.promotion.entity.ErpPromoteDataFriendsSum;

/**
 * 朋友圈推广数据DAO接口
 * 
 * @author yunnex
 * @version 2018-05-09
 */
@MyBatisDao
public interface ErpPromoteDataFriendsDao extends CrudDao<ErpPromoteDataFriends> {
    /**
     * 
     * 业务定义：按条件查询朋友圈推广数据
     * 
     * @date 2018年5月9日
     * @author R/Q
     */
    List<ErpPromoteDataFriends> queryFriendsData(ErpPromoteDataFriends paramObj);

    /**
     * 
     * 业务定义：根据分单ID和数据时间删除对应推广数据
     * 
     * @date 2018年5月10日
     * @author R/Q
     */
    void deleteBySplitIdAndDataTime(ErpPromoteDataFriends paramObj);

    /**
     * 业务定义：查询订单下的朋友圈推广合计数据
     * 
     * @date 2018年5月10日
     * @author R/Q
     */
    ErpPromoteDataFriendsSum queryFriendsDataSum(ErpPromoteDataFriends paramObj);

    /**
     * 
     * 业务定义：批量插入朋友圈信息
     * 
     * @date 2018年5月14日
     * @author R/Q
     */
    void batchInsert(List<ErpPromoteDataFriends> list);

    /**
     * 
     * 业务定义：删除日期重复数据
     * 
     * @date 2018年5月14日
     * @author R/Q
     */
    void batchDelete(Map<String, Object> paramMap);

    /**
     * 
     * 业务定义：计算上传数据汇总结果
     * 
     * @date 2018年5月14日
     * @author R/Q
     */
    List<ErpPromoteDataFriends> calSumData(Map<String, Object> paramMap);
}