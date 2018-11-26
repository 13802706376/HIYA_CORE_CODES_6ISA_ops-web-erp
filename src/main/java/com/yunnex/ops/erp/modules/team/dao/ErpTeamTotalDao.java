package com.yunnex.ops.erp.modules.team.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.team.entity.ErpTeamTotal;
import com.yunnex.ops.erp.modules.team.entity.ErpTeamUser;

/**
 * 成员DAO接口
 * 
 * @author wangwei
 * @version 2017-10-26
 */
@MyBatisDao
public interface ErpTeamTotalDao {
	List<ErpTeamTotal> findListByTeam(ErpTeamTotal erpTeamTotal);
	long findListByTeamByCount(ErpTeamTotal erpTeamTotal);
	List<ErpTeamTotal> getUserByTeamFirst(ErpTeamTotal erpTeamTotal);
	int findNewCount(ErpTeamTotal dto);

	List<ErpTeamTotal> findShouldflowCount(ErpTeamTotal dto);

	List<ErpTeamTotal> flowEndCount(ErpTeamTotal dto);

	int noCompleteCount(ErpTeamTotal dto);

	int completeCount(ErpTeamTotal dto);

	int completeExCount(ErpTeamTotal dto);
}
