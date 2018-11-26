package com.yunnex.ops.erp.modules.visit.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.team.entity.ErpTeamTotal;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitCount;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitServiceInfo;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitServiceItem;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitServiceItemRecord;

/**
 * 上门服务DAO接口
 * 
 * @author R/Q
 * @version 2018-05-26
 */
@MyBatisDao
public interface ErpVisitCountDao{
	List<ErpVisitCount> findByUserId(ErpVisitCount erpTeamTotal);
	int visitCount(ErpVisitCount erpTeamTotal);
}
