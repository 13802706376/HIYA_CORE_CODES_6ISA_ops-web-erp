package com.yunnex.ops.erp.modules.team.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.team.entity.ErpTeamTotal;
import com.yunnex.ops.erp.modules.team.entity.ErpTeamUser;
import com.yunnex.ops.erp.modules.team.entity.ErpUserTotal;

/**
 * 成员DAO接口
 * 
 * @author wangwei
 * @version 2017-10-26
 */
@MyBatisDao
public interface ErpUserTotalDao {
	List<ErpUserTotal> findUserCount(ErpUserTotal erpUserTotal);
	List<ErpUserTotal> getUserByTeam(ErpUserTotal erpUserTotal);
	long getUserByTeamByCount(ErpUserTotal erpUserTotal);
	List<ErpUserTotal> getTeamNameByUser(ErpUserTotal erpUserTotal);
	List<ErpUserTotal> findServiceTypeByUser(String userId);
	ErpUserTotal findTeamNameByUser(String userId);
	int findNewCount(ErpUserTotal dto); 
	List<ErpUserTotal> findShouldflowCount(ErpUserTotal dto);
	List<ErpUserTotal> flowEndCount(ErpUserTotal dto);
	int noCompleteCount(ErpUserTotal dto);
	int completeCount(ErpUserTotal dto);
	int completeExCount(ErpUserTotal dto);
}
