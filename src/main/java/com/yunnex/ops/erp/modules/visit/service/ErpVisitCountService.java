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
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;
import com.yunnex.ops.erp.modules.team.entity.ErpTeam;
import com.yunnex.ops.erp.modules.team.service.ErpTeamService;
import com.yunnex.ops.erp.modules.visit.dao.ErpVisitCountDao;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitCount;

/**
 * 上门服务Service
 * 
 * @author R/Q
 * @version 2018-05-26
 */
@Service
public class ErpVisitCountService extends BaseService {
	@Autowired
    private ErpVisitCountDao erpVisitCountDao;
	@Autowired
    private ErpTeamService erpTeamService;
	
	public JSONObject findTeamServiceCount(Page<ErpVisitCount> page,
			ErpVisitCount dto,
            String principalId) {
    	JSONObject resObject = new JSONObject();
    	dto.setUserId(UserUtils.getUser().getId());
    	String startDateStr = dto.getStartDateStr();
        String endDateStr = dto.getEndDateStr();
        if (StringUtils.isBlank(startDateStr) || StringUtils.isBlank(endDateStr)) {
            logger.error("开始时间 和 结束时间 不能为空");
            throw new ServiceException(CommonConstants.FailMsg.PARAM);
        }
        List<ErpVisitCount> result = null;
        try {
        	result=getUserByTeam(page, dto);
		} catch (Exception e) {
			// TODO: handle exception
			result=null;
		}
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
	
	public List<ErpVisitCount> findTeamServiceCount(ErpVisitCount dto,
            String principalId) {
    	dto.setAgentId(UserUtils.getUser().getAgentId());
    	String startDateStr = dto.getStartDateStr();
        String endDateStr = dto.getEndDateStr();
        if (StringUtils.isBlank(startDateStr) || StringUtils.isBlank(endDateStr)) {
            logger.error("开始时间 和 结束时间 不能为空");
            throw new ServiceException(CommonConstants.FailMsg.PARAM);
        }
        List<ErpVisitCount> result = getUserByTeam(dto);
        return result;
    }
	
	private List<ErpVisitCount> getUserByTeam(Page<ErpVisitCount> page,
			ErpVisitCount dto) {
    	List<ErpVisitCount> list=new ArrayList<ErpVisitCount>();
    	if(dto==null){
    		return list;
    	}
    	dto.setPage(page);
    	list=erpVisitCountDao.findByUserId(dto);
    	return list;
    }
	
	private List<ErpVisitCount> getUserByTeam(
			ErpVisitCount dto) {
    	List<ErpVisitCount> list=new ArrayList<ErpVisitCount>();
    	if(dto==null){
    		return list;
    	}
    	list=erpVisitCountDao.findByUserId(dto);
    	return list;
    }
	
	public List<ErpVisitCount> findTeamServiceAllCount(ErpVisitCount dto,
            String principalId) {
    	String startDateStr = dto.getStartDateStr();
        String endDateStr = dto.getEndDateStr();
        dto.setAgentId(UserUtils.getUser().getAgentId());
        if (StringUtils.isBlank(startDateStr) || StringUtils.isBlank(endDateStr)) {
            logger.error("开始时间 和 结束时间 不能为空");
            throw new ServiceException(CommonConstants.FailMsg.PARAM);
        }
        // 计算订单权限过滤数据
        dto = calculateOrderSecurity(dto,principalId);
        List<ErpVisitCount> result = getUserByTeam(dto);
        ErpVisitCount all=new ErpVisitCount();
        List<ErpVisitCount> list=new ArrayList<ErpVisitCount>();
        int allfirstBasicVisitCount=0;
        int allmaterialImplCount=0;
        int allfirstVisitCount=0;
        int alljykVisitCount=0;
        int alltrainingCount=0;
        int allfreeTrainingCount=0;
        int allcomHandCount=0;
        int allmaterialUpdateCount=0;
        int allzhctServiceCount=0;
        all.setTeamName("所有团队");
        for(ErpVisitCount team:result){
    		allfirstBasicVisitCount+=team.getFirstBasicVisitCount();
        	allmaterialImplCount+=team.getMaterialImplCount();
        	allfirstVisitCount+=team.getFirstVisitCount();
        	alljykVisitCount+=team.getJykVisitCount();
        	alltrainingCount+=team.getTrainingCount();
        	allfreeTrainingCount+=team.getFreeTrainingCount();
        	allcomHandCount+=team.getComHandCount();
        	allmaterialUpdateCount+=team.getMaterialUpdateCount();
        	allzhctServiceCount+=team.getZhctServiceCount();
        }
        all.setFirstVisitCount(allfirstVisitCount);//首次
    	all.setMaterialImplCount(allmaterialImplCount);
    	all.setJykVisitCount(alljykVisitCount);
    	all.setComHandCount(allcomHandCount);
    	all.setFreeTrainingCount(allfreeTrainingCount);
    	all.setFirstBasicVisitCount(allfirstBasicVisitCount);
    	all.setMaterialUpdateCount(allmaterialUpdateCount);
    	all.setTrainingCount(alltrainingCount);
    	all.setZhctServiceCount(allzhctServiceCount);
    	list.add(all);
        // 计算订单权限过滤数据
//        List<ErpVisitCount> result = getPageResponseDto(dto);
//        result=createAllTeam(result,dto);
        return list;
    }
	
	public List<ErpVisitCount> createAllTeam(List<ErpVisitCount> result,ErpVisitCount dto) {
    	List<ErpVisitCount> list=new ArrayList<ErpVisitCount>();
    	ErpVisitCount all=new ErpVisitCount();
    	if(!CollectionUtils.isEmpty(result)){
    		if(result.size()>1){
    			all.setTeamName("所有团队");
    			int allfirstVisitCount=0;
    			int allmaterialImplCount=0;
    			int alljykVisitCount=0;
    			int allcomHandCount=0;
    			int allfreeTrainingCount=0;
    	    	for(ErpVisitCount team:result){
    	    		int agentId=team.getAgentId();
    	    		dto.setAgentId(agentId);
    	    		dto.setCode(1);
    	    		int firstVisitCount=erpVisitCountDao.visitCount(dto);
    	    		allfirstVisitCount+=firstVisitCount;
    	    		dto.setCode(2);
    	    		int materialImplCount=erpVisitCountDao.visitCount(dto);
    	    		allmaterialImplCount+=materialImplCount;
    	    		dto.setCode(3);
    	    		int jykVisitCount=erpVisitCountDao.visitCount(dto);
    	    		alljykVisitCount+=jykVisitCount;
    	    		dto.setCode(4);
    	    		int comHandCount=erpVisitCountDao.visitCount(dto);
    	    		allcomHandCount+=comHandCount;
    	    		dto.setCode(5);
    	    		int freeTrainingCount=erpVisitCountDao.visitCount(dto);
    	    		allfreeTrainingCount+=freeTrainingCount;
    	    	}
    	    	all.setFirstVisitCount(allfirstVisitCount);//首次
    	    	all.setMaterialImplCount(allmaterialImplCount);
    	    	all.setJykVisitCount(alljykVisitCount);
    	    	all.setComHandCount(allcomHandCount);
    	    	all.setFreeTrainingCount(allfreeTrainingCount);
    	    	list.add(all);
    		}
    		return list;
	    }else{
	    	return null;
	    }
    }
	
	public List<ErpVisitCount> createTeam(List<ErpVisitCount> result,ErpVisitCount dto) {
    	if(!CollectionUtils.isEmpty(result)){
	    	for(ErpVisitCount team:result){
	    		int agentId=team.getAgentId();
	    		dto.setCode(1);
	    		dto.setAgentId(agentId);
	    		int firstVisitCount=erpVisitCountDao.visitCount(dto);
	    		team.setFirstVisitCount(firstVisitCount);//首次
	    		dto.setCode(2);
	    		int materialImplCount=erpVisitCountDao.visitCount(dto);
	    		team.setMaterialImplCount(materialImplCount);
	    		dto.setCode(3);
	    		int jykVisitCount=erpVisitCountDao.visitCount(dto);
	    		team.setJykVisitCount(jykVisitCount);
	    		dto.setCode(4);
	    		int comHandCount=erpVisitCountDao.visitCount(dto);
	    		team.setComHandCount(comHandCount);
	    		dto.setCode(5);
	    		int freeTrainingCount=erpVisitCountDao.visitCount(dto);
	    		team.setFreeTrainingCount(freeTrainingCount);
	    	}
	    	return result;
	    }else{
	    	return null;
	    }
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
	
	private List<ErpVisitCount> getPageResponseDto(Page<ErpVisitCount> page,
			ErpVisitCount dto) {
    	List<ErpVisitCount> list=new ArrayList<ErpVisitCount>();
    	if(dto==null){
    		return list;
    	}
    	dto.setPage(page);
    	list=erpVisitCountDao.findByUserId(dto);
    	return list;
    }
	
	private List<ErpVisitCount> getPageResponseDto(ErpVisitCount dto) {
    	List<ErpVisitCount> list=new ArrayList<ErpVisitCount>();
    	if(dto==null){
    		return list;
    	}
    	list=erpVisitCountDao.findByUserId(dto);
    	return list;
    }
	
	public List<ErpVisitCount> expTeamServiceCount(ErpVisitCount dto,
            String principalId) {
    	String startDateStr = dto.getStartDateStr();
        String endDateStr = dto.getEndDateStr();
        dto.setAgentId(UserUtils.getUser().getAgentId());
        if (StringUtils.isBlank(startDateStr) || StringUtils.isBlank(endDateStr)) {
            logger.error("开始时间 和 结束时间 不能为空");
            throw new ServiceException(CommonConstants.FailMsg.PARAM);
        }
        // 计算订单权限过滤数据
        dto = calculateOrderSecurity(dto,principalId);
        // 计算订单权限过滤数据
        List<ErpVisitCount> result = getUserByTeam(dto);
//        for(ErpVisitCount team:result){
//    		dto.setTeamId(team.getTeamId());
//    		dto.setCode(7);
//    		int firstBasicVisitCount=erpVisitCountDao.visitCount(dto);
//    		dto.setCode(2);
//        	int materialImplCount=erpVisitCountDao.visitCount(dto);
//        	dto.setCode(1);
//        	int firstVisitCount=erpVisitCountDao.visitCount(dto);
//        	dto.setCode(3);
//        	int jykVisitCount=erpVisitCountDao.visitCount(dto);
//        	dto.setCode(6);
//        	int trainingCount=erpVisitCountDao.visitCount(dto);
//        	dto.setCode(5);
//        	int freeTrainingCount=erpVisitCountDao.visitCount(dto);
//        	dto.setCode(4);
//        	int comHandCount=erpVisitCountDao.visitCount(dto);
//        	dto.setCode(8);
//        	int materialUpdateCount=erpVisitCountDao.visitCount(dto);
//        	team.setFirstBasicVisitCount(firstBasicVisitCount);
//        	team.setMaterialImplCount(materialImplCount);
//        	team.setFirstVisitCount(firstVisitCount);
//        	team.setJykVisitCount(jykVisitCount);
//        	team.setTrainingCount(trainingCount);
//        	team.setFreeTrainingCount(freeTrainingCount);
//        	team.setComHandCount(comHandCount);
//        	team.setMaterialUpdateCount(materialUpdateCount);
//    	}
        return result;
    }
}
