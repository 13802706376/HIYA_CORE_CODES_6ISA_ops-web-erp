package com.yunnex.ops.erp.modules.team.service;


import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.yunnex.ops.erp.common.constant.CommonConstants;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.service.BaseService;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.modules.order.constant.OrderConstants;
import com.yunnex.ops.erp.modules.statistics.dto.OrderSecurityRequestDto;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;
import com.yunnex.ops.erp.modules.team.dao.ErpTeamTotalDao;
import com.yunnex.ops.erp.modules.team.dao.ErpUserTotalDao;
import com.yunnex.ops.erp.modules.team.entity.ErpTeam;
import com.yunnex.ops.erp.modules.team.entity.ErpTeamTotal;
import com.yunnex.ops.erp.modules.team.entity.ErpUserTotal;
import com.yunnex.ops.erp.modules.workflow.flow.constant.DeliveryFlowConstant;

/**
 * 团队Service
 * @author wangwei
 * @version 2017-10-26
 */
@Service
public class ErpUserTotalService extends BaseService {

    @Autowired
    private ErpUserTotalDao erpUserTotalDao;
    @Autowired
    private ErpTeamService erpTeamService;
    
    public List<ErpUserTotal> findUserCount(ErpUserTotal dto,
            String principalId) {
    	// 计算订单权限过滤数据
    	String startDateStr = dto.getStartDateStr();
        String endDateStr = dto.getEndDateStr();
        if (StringUtils.isBlank(startDateStr) || StringUtils.isBlank(endDateStr)) {
            logger.error("开始时间 和 结束时间 不能为空");
            throw new ServiceException(CommonConstants.FailMsg.PARAM);
        }
        List<ErpUserTotal> result = getUserByTeam(dto);
		if (!CollectionUtils.isEmpty(result)) {
			for (ErpUserTotal e : result) {
				String percentage = makePercentage(e);
				e.setPercentage(percentage);
			}
		}
        return result;
    }
    
    public JSONObject findUserCount(Page<ErpUserTotal> page,
    		ErpUserTotal dto,
            String principalId) {
    	dto.setUserId(UserUtils.getUser().getId());
    	JSONObject resObject = new JSONObject();
    	String startDateStr = dto.getStartDateStr();
        String endDateStr = dto.getEndDateStr();
        if (StringUtils.isBlank(startDateStr) || StringUtils.isBlank(endDateStr)) {
            logger.error("开始时间 和 结束时间 不能为空");
            throw new ServiceException(CommonConstants.FailMsg.PARAM);
        }
        List<ErpUserTotal> result=null;
        try{
//        	result = getUserByTeam(page, dto);
        	page.setCount(erpUserTotalDao.getUserByTeamByCount(dto));
            dto.setPage(page);
            page.setAutoCount(false);
            result= erpUserTotalDao.getUserByTeam(dto);
        }catch(Exception e ){
        	result=null;
        }
		if (!CollectionUtils.isEmpty(result)) {
			for (ErpUserTotal e : result) {
				String percentage = makePercentage(e);
				e.setPercentage(percentage);
			}
		}
        resObject.put("code", 0);
        resObject.put("list", result);
        long count = result== null?0:result.size();
        // 获取总条数
        if (page != null) {
            count = page.getCount();
        }
        resObject.put("count", count);
        return resObject;
    }
    
    private List<ErpUserTotal> getUserByTeam(Page<ErpUserTotal> page,
    		ErpUserTotal dto) {
    	List<ErpUserTotal> list=new ArrayList<ErpUserTotal>();
    	if(dto==null){
    		return list;
    	}
    	dto.setPage(page);
    	list=erpUserTotalDao.getUserByTeam(dto);
    	return list;
    }
    private List<ErpUserTotal> getUserByTeam(ErpUserTotal dto) {
    	List<ErpUserTotal> list=new ArrayList<ErpUserTotal>();
    	if(dto==null){
    		return list;
    	}
    	list=erpUserTotalDao.getUserByTeam(dto);
    	return list;
    }
	
    private static String formattedDecimalToPercentage(double decimal)
    {
    	//获取格式化对象
    	NumberFormat nt = NumberFormat.getPercentInstance();
    	//设置百分数精确度2即保留两位小数
    	nt.setMinimumFractionDigits(2);
    	return nt.format(decimal);
    }

    private String makePercentage(ErpUserTotal dto) {
    	int flowEndCount=dto.getFlowEndCount();
    	int completeCount=dto.getCompleteCount();
    	int shouldflowCount=dto.getShouldflowCount();
    	String percentage="";
    	if(flowEndCount!=0&&completeCount!=0&&shouldflowCount==0){
    		shouldflowCount=1;
    	}
    	if(shouldflowCount==0){
    		percentage="0.00%";
    	}else{
    		double d=(double)(flowEndCount-completeCount)/shouldflowCount;
        	percentage=formattedDecimalToPercentage(d);
    	}
    	return percentage;
    }
    
    public int shouldFlowCount(List<ErpUserTotal> list) {
    	//优先去首次》智慧》上门》物料
    	int count=0;
		for(ErpUserTotal e:list){
			if("FIRST".equals(e.getServiceType()) && e.getCurCount()>0){
				count=e.getCurCount();
				break;
			}else if(DeliveryFlowConstant.SERVICE_TYPE_KE_ZHCT.equals(e.getServiceType()) && e.getCurCount()>0){
				count=e.getCurCount();
				break;
			}else if("VC".equals(e.getServiceType()) && e.getCurCount()>0){
				count=e.getCurCount();
				break;
			}else if("MU".equals(e.getServiceType()) && e.getCurCount()>0){
				count=e.getCurCount();
				break;
			}else{
				count=0;
				break;
			}
		}
		return count;
    }
    
    public int flowEndCount(List<ErpUserTotal> list) {
    	//优先去首次》智慧》上门》物料
    	int count=0;
		for(ErpUserTotal e:list){
			if("FIRST".equals(e.getServiceType()) && e.getCurCount()>0){
				count=e.getCurCount();
				break;
			}else if(DeliveryFlowConstant.SERVICE_TYPE_KE_ZHCT.equals(e.getServiceType()) && e.getCurCount()>0){
				count=e.getCurCount();
				break;
			}else if("VC".equals(e.getServiceType()) && e.getCurCount()>0){
				count=e.getCurCount();
				break;
			}else if("MU".equals(e.getServiceType()) && e.getCurCount()>0){
				count=e.getCurCount();
				break;
			}else{
				count=0;
				break;
			}
		}
		return count;
    }
    
    /**
     * 获取订单权限过滤数据
     * 
     * @param principalId
     * @return
     * @date 2018年6月4日
     * @author linqunzhi
     */
    private ErpUserTotal calculateOrderSecurity(ErpUserTotal dto,String principalId) {
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

}
