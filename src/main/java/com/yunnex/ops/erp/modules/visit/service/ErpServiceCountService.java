package com.yunnex.ops.erp.modules.visit.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.yunnex.ops.erp.common.constant.CommonConstants;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.service.BaseService;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.modules.order.constant.OrderConstants;
import com.yunnex.ops.erp.modules.team.entity.ErpTeam;
import com.yunnex.ops.erp.modules.team.service.ErpTeamService;
import com.yunnex.ops.erp.modules.visit.dao.ErpServiceCountDao;
import com.yunnex.ops.erp.modules.visit.entity.ErpServiceCount;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitCount;

/**
 * 上门服务Service
 * 
 * @author R/Q
 * @version 2018-05-26
 */
@Service
public class ErpServiceCountService extends BaseService {
	@Autowired
    private ErpServiceCountDao erpServiceCountDao;
	@Autowired
    private ErpTeamService erpTeamService;
	
	public List<ErpServiceCount> findServiceServiceAllCount(ErpServiceCount dto,
            String principalId) {
    	String startDateStr = dto.getStartDateStr();
        String endDateStr = dto.getEndDateStr();
        if (StringUtils.isBlank(startDateStr) || StringUtils.isBlank(endDateStr)) {
            logger.error("开始时间 和 结束时间 不能为空");
            throw new ServiceException(CommonConstants.FailMsg.PARAM);
        }
        // 计算订单权限过滤数据
        dto = calculateOrderSecurity(dto,principalId);
//        List<Integer> agentIdList = getAgentIdListByTeam(dto.getTeamId());
//        dto.setAgentIdList(agentIdList);
        // 计算订单权限过滤数据
        List<ErpServiceCount> result = getPageResponseDto(dto);
        result=createAllTeam(result,dto);
        return result;
    }
	
	
	/**
     * 根据团队id 和 选择的 团队成员id 获取服务商编号
     *
     * @param teamId
     * @param userIdList
     * @return
     * @date 2018年6月4日
     * @author linqunzhi
     */
    private List<Integer> getAgentIdListByTeam(String teamId) {
        // 如果选择了团队成员 或者 没选择团队 ，则不进行服务商编号过滤
        if (StringUtils.isBlank(teamId)) {
            return null;
        }
        ErpTeam erpTeam = erpTeamService.get(teamId);
        List<Integer> result = null;
        if (erpTeam != null) {
            result = new ArrayList<>();
            result.add(erpTeam.getAgentId());
        }
        return result;
    }
	
	/**
     * 获取订单权限过滤数据
     * 
     * @param principalId
     * @return
     * @date 2018年6月4日
     * @author linqunzhi
     */
    private ErpVisitCount calculateOrderSecurity(ErpVisitCount dto,String principalId) {
        // 所有数据权限
        if (SecurityUtils.getSubject().isPermitted(OrderConstants.SECURITY_ALL)) {
        	dto.setAgentIdList(null);
        	dto.setOrderTypeList(null);
        	return dto;
        }
        //订单类型 
        List<Integer> orderTypeList = new ArrayList<>();
        // 服务商编号
        List<Integer> agentIdList = new ArrayList<>();
        if // 所有分公司数据
        (SecurityUtils.getSubject().isPermitted(OrderConstants.SECURITY_BRANCH_COMPANY)) {
            orderTypeList.add(OrderConstants.TYPE_DIRECT);
        }
        if// 所有服务商数据查
        (SecurityUtils.getSubject().isPermitted(OrderConstants.SECURITY_SERVICE_COMPANY)) {
            orderTypeList.add(OrderConstants.TYPE_SERVICE);
        }
        if // 所在公司数据
        (SecurityUtils.getSubject().isPermitted(OrderConstants.SECURITY_COMPANY)) {
            // 计算出查询的 服务商编号
            List<Integer> companyList = calculateAgentIdList(principalId);
            if (CollectionUtils.isNotEmpty(companyList)) {
                agentIdList.addAll(companyList);
            }
        }
        dto.setAgentIdList(agentIdList);
        dto.setOrderTypeList(orderTypeList);
        return dto;
    }
    
    /**
     * 计算服务商编号集合
     *
     * @param agentIdList
     * @param principalId
     * @return
     * @date 2018年5月29日
     * @author linqunzhi
     */
    private List<Integer> calculateAgentIdList(String principalId) {
        // 获取团队
        List<ErpTeam> list = erpTeamService.findByUserId(principalId);
        // 服务商编号集合
        List<Integer> result = null;
        Set<Integer> set = null;
        if (CollectionUtils.isNotEmpty(list)) {
            result = new ArrayList<>();
            set = new HashSet<>();
            Integer agentId = null;
            for (ErpTeam team : list) {
                // 服务商编号
                agentId = team.getAgentId();
                if (agentId != null) {
                    set.add(agentId);
                }
            }
            if (CollectionUtils.isNotEmpty(set)) {
                result.addAll(set);
            }
        }
        return result;
    }
    
    private List<ErpServiceCount> getPageResponseDto(Page<ErpServiceCount> page,
    		ErpServiceCount dto) {
    	List<ErpServiceCount> list=new ArrayList<ErpServiceCount>();
    	if(dto==null){
    		return list;
    	}
    	dto.setPage(page);
    	list=erpServiceCountDao.findServiceCount(dto);
    	return list;
    }
	
	private List<ErpServiceCount> getPageResponseDto(ErpServiceCount dto) {
    	List<ErpServiceCount> list=new ArrayList<ErpServiceCount>();
    	if(dto==null){
    		return list;
    	}
    	list=erpServiceCountDao.findServiceCount(dto);
    	return list;
    }
	
	public JSONObject findServiceSuccessCount(Page<ErpServiceCount> page,
			ErpServiceCount dto,
            String principalId) {
    	JSONObject resObject = new JSONObject();
    	String startDateStr = dto.getStartDateStr();
        String endDateStr = dto.getEndDateStr();
        if (StringUtils.isBlank(startDateStr) || StringUtils.isBlank(endDateStr)) {
            logger.error("开始时间 和 结束时间 不能为空");
            throw new ServiceException(CommonConstants.FailMsg.PARAM);
        }
        // 计算订单权限过滤数据
        dto = calculateOrderSecurity(dto,principalId);
        List<ErpServiceCount> result = getPageResponseDto(page,dto);
        resObject.put("code", 0);
        resObject.put("list", result);
        long count = result==null?0:result.size();
        // 获取总条数
        if (page != null) {
            count = page.getCount();
        }
        resObject.put("count", count);
        
        return resObject;
    }
	
	public List<ErpServiceCount> findServiceSuccessCount(ErpServiceCount dto,
            String principalId) {
    	String startDateStr = dto.getStartDateStr();
        String endDateStr = dto.getEndDateStr();
        if (StringUtils.isBlank(startDateStr) || StringUtils.isBlank(endDateStr)) {
            logger.error("开始时间 和 结束时间 不能为空");
            throw new ServiceException(CommonConstants.FailMsg.PARAM);
        }
        // 计算订单权限过滤数据
        dto = calculateOrderSecurity(dto,principalId);
        // 计算订单权限过滤数据
        List<ErpServiceCount> result = getPageResponseDto(dto);
        return result;
    }
	
	public List<ErpServiceCount> createTeam(List<ErpServiceCount> result,ErpServiceCount dto) {
		return null;
    }
	
	public List<ErpServiceCount> createAllTeam(List<ErpServiceCount> result,ErpServiceCount dto) {
    	return null;
    }
	
	/**
     * 获取订单权限过滤数据
     * 
     * @param principalId
     * @return
     * @date 2018年6月4日
     * @author linqunzhi
     */
    private ErpServiceCount calculateOrderSecurity(ErpServiceCount dto,String principalId) {
        // 所有数据权限
        if (SecurityUtils.getSubject().isPermitted(OrderConstants.TEAM_ALL)) {
        	dto.setAgentIdList(null);
        	dto.setOrderTypeList(null);
        	return dto;
        }
        //订单类型 
        List<Integer> orderTypeList = new ArrayList<>();
        // 服务商编号
        List<Integer> agentIdList = new ArrayList<>();
        if // 所有分公司数据
        (SecurityUtils.getSubject().isPermitted(OrderConstants.TEAM_BRANCH_COMPANY)) {
            orderTypeList.add(OrderConstants.TYPE_DIRECT);
        }
        if// 所有服务商数据查
        (SecurityUtils.getSubject().isPermitted(OrderConstants.TEAM_SERVICE_COMPANY)) {
            orderTypeList.add(OrderConstants.TYPE_SERVICE);
        }
        if // 所在公司数据
        (SecurityUtils.getSubject().isPermitted(OrderConstants.TEAM_COMPANY)) {
            // 计算出查询的 服务商编号
            List<Integer> companyList = calculateAgentIdList(principalId);
            if (CollectionUtils.isNotEmpty(companyList)) {
                agentIdList.addAll(companyList);
            }
        }
        dto.setAgentIdList(agentIdList);
        dto.setOrderTypeList(orderTypeList);
        return dto;
    }
}
