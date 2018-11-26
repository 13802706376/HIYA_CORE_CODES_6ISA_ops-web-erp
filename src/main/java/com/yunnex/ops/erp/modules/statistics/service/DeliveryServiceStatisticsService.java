package com.yunnex.ops.erp.modules.statistics.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunnex.ops.erp.common.constant.CommonConstants;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.service.BaseService;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.common.utils.DateUtils;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.modules.order.constant.OrderConstants;
import com.yunnex.ops.erp.modules.statistics.constant.DeliveryServiceStatisticsConstants;
import com.yunnex.ops.erp.modules.statistics.dao.DeliveryServiceDetailDao;
import com.yunnex.ops.erp.modules.statistics.dao.DeliveryServiceStatisticsDao;
import com.yunnex.ops.erp.modules.statistics.dao.DeliveryServiceStatisticsDetailDao;
import com.yunnex.ops.erp.modules.statistics.dto.OrderSecurityRequestDto;
import com.yunnex.ops.erp.modules.statistics.dto.deliveryService.DeliveryServiceDetailRequestDto;
import com.yunnex.ops.erp.modules.statistics.dto.deliveryService.DeliveryServiceStatisticsAllResponseDto;
import com.yunnex.ops.erp.modules.statistics.dto.deliveryService.DeliveryServiceStatisticsDetailDto;
import com.yunnex.ops.erp.modules.statistics.dto.deliveryService.DeliveryServiceStatisticsRequestDto;
import com.yunnex.ops.erp.modules.statistics.dto.deliveryService.DeliveryServiceStatisticsResponseDto;
import com.yunnex.ops.erp.modules.statistics.dto.deliveryService.QueryDataResponseDto;
import com.yunnex.ops.erp.modules.statistics.entity.DeliveryServiceStatistics;
import com.yunnex.ops.erp.modules.statistics.enums.DeliveryServiceStatisticsColumn;
import com.yunnex.ops.erp.modules.statistics.enums.DeliveryServiceStatisticsQuery;
import com.yunnex.ops.erp.modules.sys.constant.RoleConstant;
import com.yunnex.ops.erp.modules.sys.entity.Role;
import com.yunnex.ops.erp.modules.sys.service.RoleService;
import com.yunnex.ops.erp.modules.sys.service.SysConstantsService;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;
import com.yunnex.ops.erp.modules.team.dao.ErpTeamDao;
import com.yunnex.ops.erp.modules.team.entity.ErpTeam;
import com.yunnex.ops.erp.modules.team.entity.ErpUserTotal;
import com.yunnex.ops.erp.modules.team.service.ErpTeamService;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitCount;
import com.yunnex.ops.erp.modules.workflow.delivery.constant.ErpDeliveryServiceConstants;
import com.yunnex.ops.erp.modules.workflow.delivery.extraModel.DeliveryServiceWorkDays;
import com.yunnex.ops.erp.modules.workflow.flow.common.JykFlowConstants;
import com.yunnex.ops.erp.modules.workflow.user.dao.ErpOrderFlowUserDao;
import com.yunnex.ops.erp.modules.workflow.user.entity.ErpOrderFlowUser;

@Service
public class DeliveryServiceStatisticsService extends BaseService {
	@Autowired
    private ErpTeamDao erpTeamDao;
    @Autowired
    private DeliveryServiceStatisticsDao dao;
    @Autowired
    private DeliveryServiceStatisticsDetailDao servideDetailDao;
    @Autowired
    private DeliveryServiceDetailDao detailDao;
    @Autowired
    private ErpTeamService erpTeamService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private SysConstantsService sysConstantsService;
    @Autowired
    private ErpOrderFlowUserDao erpOrderFlowUserDao;
    

    /**
     * 获取生产服务团队订单管理明细列表
     *
     * @param page
     * @param dto
     * @param principalId
     * @return
     * @date 2018年5月29日
     * @author linqunzhi
     */
    public DeliveryServiceStatisticsAllResponseDto findTeamStatistics(Page<DeliveryServiceStatisticsRequestDto> page,
                    DeliveryServiceStatisticsRequestDto dto,
                    String principalId) {
        String dtoStr = JSON.toJSONString(dto);
        logger.info("findTeamStatistics start | dto={}|principalId={}", dtoStr, principalId);
        if (dto == null) {
            logger.error("dto 不能为空");
            throw new ServiceException(CommonConstants.FailMsg.PARAM);
        }
        String startDateStr = dto.getStartDateStr();
        String endDateStr = dto.getEndDateStr();
        if (StringUtils.isBlank(startDateStr) || StringUtils.isBlank(endDateStr)) {
            logger.error("开始时间 和 结束时间 不能为空");
            throw new ServiceException(CommonConstants.FailMsg.PARAM);
        }
        // 计算订单权限过滤数据
        OrderSecurityRequestDto orderSecurity = calculateOrderSecurity(principalId);
        dto.setOrderSecurity(orderSecurity);
        // 计算出查询的 服务商编号
        List<Integer> agentIdList = getAgentIdListByTeam(dto.getTeamId(), dto.getUserIdList());
        dto.setAgentIdList(agentIdList);
        if(dto.getServiceTypes().get(0).equals("MU")||dto.getServiceTypes().get(0).equals("VC")){
        	dto.setIsFlag("isMuOrVc");
        }else {
        	dto.setIsFlag(null);
        }
        DeliveryServiceStatisticsAllResponseDto result = getPageResponseDto(page, dto);
        // 计算不需要展示的查询条件
        List<String> notShowQueryList = getTeamNotShowQuery();
        result.setNotShowQueryList(notShowQueryList);
        // 设置是否延迟服务提示内容数组
        result.setDelayServiceContentArr(calculateDelayServiceContentArr());
        result.setDelayServiceMUArr(calculateMUContentArr());
        result.setDelayServiceVCArr(calculateVCContentArr());
        logger.info("findTeamStatistics end | result.count={}", result.getCount());
        return result;
    }

    /**
     * 计算是否延迟服务提示内容数组
     *
     * @return
     * @date 2018年6月11日
     * @author linqunzhi
     */
    private String[] calculateDelayServiceContentArr() {
        // 获取工作日天数配置值
        String josnStr = sysConstantsService.getConstantValByKey(ErpDeliveryServiceConstants.DELIVERY_SERVICE_WORK_DAYS);
        DeliveryServiceWorkDays workDays = null;
        if (StringUtils.isNotBlank(josnStr)) {
            try {
                workDays = JSON.parseObject(josnStr, DeliveryServiceWorkDays.class);
            } catch (Exception e) {
                logger.error("转换工作日错误", e);
            }

        }
         
        // 银联支付培训&测试（远程）任务应该完成的工作日天数
        int shouldTrainTestDays = 0;
        //物料制作跟踪任务应该完成的工作日天数
        int shouldMaterielDays = 0;
        // 上门服务完成（首次营销策划服务）任务应该完成的工作日天数
        int shouldVisitServiceDays = 0;
        if (workDays != null) {
            if (workDays.getShouldTrainTestDays() != null) {
                shouldTrainTestDays = workDays.getShouldTrainTestDays();
            }
            if (workDays.getShouldMaterielDays() != null) {
                shouldMaterielDays = workDays.getShouldMaterielDays();
            }
            if (workDays.getShouldVisitServiceDays() != null) {
                shouldVisitServiceDays = workDays.getShouldVisitServiceDays();
            }
        }
        String[] arr = new String[3];
        arr[0] = String.format(DeliveryServiceStatisticsConstants.OPEN_DELAY_FLAG_CONTENT, shouldTrainTestDays);
        arr[1] = String.format(DeliveryServiceStatisticsConstants.MATERIEL_DELAY_FLAG_CONTENT, shouldMaterielDays);
        arr[2] = String.format(DeliveryServiceStatisticsConstants.OPERATION_DELAYFLAG_CONTENT, shouldVisitServiceDays);
        return arr;
    }
    
    /**
     * 计算是否延迟服务提示内容数组
     *
     * @return
     * @date 2018年6月11日
     * @author linqunzhi
     */
    private String[] calculateVCContentArr() {
        // 获取工作日天数配置值
        String josnStr = sysConstantsService.getConstantValByKey(ErpDeliveryServiceConstants.DELIVERY_SERVICE_VC_DAYS);
        DeliveryServiceWorkDays workDays = null;
        if (StringUtils.isNotBlank(josnStr)) {
            try {
                workDays = JSON.parseObject(josnStr, DeliveryServiceWorkDays.class);
            } catch (Exception e) {
                logger.error("转换工作日错误", e);
            }

        }
         
        int juYinKeshouldFlowEndDays = 0;
        if (workDays != null) {
        	if (workDays.getJuYingKe() != null && workDays.getJuYingKe().getShouldFlowEndDays() != null) {
				juYinKeshouldFlowEndDays = workDays.getJuYingKe().getShouldFlowEndDays();
			}
        }
        String[] arr = new String[1];
        arr[0] = String.format(DeliveryServiceStatisticsConstants.VC_DELAYFLAG_CONTENT, juYinKeshouldFlowEndDays);
        return arr;
    }
    
    /**
     * 计算是否延迟服务提示内容数组
     *
     * @return
     * @date 2018年6月11日
     * @author linqunzhi
     */
    private String[] calculateMUContentArr() {
        // 获取工作日天数配置值
        String josnStr = sysConstantsService.getConstantValByKey(ErpDeliveryServiceConstants.DELIVERY_SERVICE_MATERIAL_DAYS);
        DeliveryServiceWorkDays workDays = null;
        if (StringUtils.isNotBlank(josnStr)) {
            try {
                workDays = JSON.parseObject(josnStr, DeliveryServiceWorkDays.class);
            } catch (Exception e) {
                logger.error("转换工作日错误", e);
            }

        }
         
        // 银联支付培训&测试（远程）任务应该完成的工作日天数
        int shouldVisitServiceDays = 0;
        //物料制作跟踪任务应该完成的工作日天数
        int shouldMaterielDays = 0;
        // 上门服务完成（首次营销策划服务）任务应该完成的工作日天数
        if (workDays != null) {
            if (workDays.getShouldTrainTestDays() != null) {
            	shouldVisitServiceDays = workDays.getShouldVisitServiceDays();
            }
            if (workDays.getShouldMaterielDays() != null) {
                shouldMaterielDays = workDays.getShouldMaterielDays();
            }
        }
        String[] arr = new String[2];
        arr[0] = String.format(DeliveryServiceStatisticsConstants.MUBU_DELAYFLAG_CONTENT, shouldVisitServiceDays);
        arr[1] = String.format(DeliveryServiceStatisticsConstants.MU_DELAYFLAG_CONTENT, shouldMaterielDays);
        return arr;
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
    private List<Integer> getAgentIdListByTeam(String teamId, List<String> userIdList) {
        // 如果选择了团队成员 或者 没选择团队 ，则不进行服务商编号过滤
        if (CollectionUtils.isNotEmpty(userIdList) || StringUtils.isBlank(teamId)) {
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
    private OrderSecurityRequestDto calculateOrderSecurity(String principalId) {
        // 所有数据权限
        if (SecurityUtils.getSubject().isPermitted(OrderConstants.SECURITY_ALL)) {
            return null;
        }
        OrderSecurityRequestDto dto = new OrderSecurityRequestDto();
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
     * 获取生产服务团队订单管理隐藏的查询条件
     *
     * @return
     * @date 2018年5月29日
     * @author linqunzhi
     */
    private static List<String> getTeamNotShowQuery() {
        List<String> result = new ArrayList<>();
        // 如果不是所有数据权限，则隐藏 订单类型查询
        if (!SecurityUtils.getSubject().isPermitted(OrderConstants.SECURITY_ALL)) {
            result.add(DeliveryServiceStatisticsQuery.ORDER_TYPE.getCode());
        }
        return result;
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

    /**
     * 获取生产服务订单信息列表
     *
     * @param page
     * @param dto
     * @return
     * @date 2018年5月28日
     * @author linqunzhi
     */
    private DeliveryServiceStatisticsAllResponseDto getPageResponseDto(Page<DeliveryServiceStatisticsRequestDto> page,
                    DeliveryServiceStatisticsRequestDto dto) {
        DeliveryServiceStatisticsAllResponseDto result = new DeliveryServiceStatisticsAllResponseDto();
        if (dto == null) {
            return result;
        }
        dto.setPage(page);
        List<DeliveryServiceStatistics> list = dao.findStatistics(dto);
        if (CollectionUtils.isEmpty(list)) {
            return result;
        }
        long count = list.size();
        // 获取总条数
        if (page != null) {
            count = page.getCount();
        }
        result.setCount(count);
        DeliveryServiceStatisticsResponseDto responseDto = null;
        // 循环list 获取 responseDto 集合
        if (CollectionUtils.isNotEmpty(list)) {
            List<DeliveryServiceStatisticsResponseDto> responseList = new ArrayList<>();
            // 将集合放入 result 中
            result.setList(responseList);
            for (DeliveryServiceStatistics statistics : list) {
            	if(!StringUtil.isBlank(statistics.getServiceType())){
            		statistics.setServiceType(changeServiceTypeSort(statistics.getServiceType()));
            		if(statistics.getServiceType().startsWith(",")){
            			statistics.setServiceType(statistics.getServiceType().substring(1));
            		}
            		if(statistics.getServiceType().endsWith(",")){
            			statistics.setServiceType(statistics.getServiceType().substring(0,statistics.getServiceType().length()-1));
            		}
            	}
                responseDto = new DeliveryServiceStatisticsResponseDto();
                // 订单号
                responseDto.setOrderNum(statistics.getOrderNumber());
                // 购买时间
                responseDto.setBuyDate(DateUtils.formatDate(statistics.getBuyDate(), DateUtils.YYYY_MM_DD_HH_MM));
                // 订单类别名称
                responseDto.setOrderType(statistics.getOrderType() != null && statistics
                                .getOrderType() == OrderConstants.TYPE_DIRECT ? "直销" : "服务商");
                // 商户名称
                responseDto.setShopName(statistics.getShopName());
                // 已购服务类别名称
                responseDto.setServiceTypeNames(statistics.getServiceType()==null?null:statistics.getServiceType().replace("JYK", "聚引客").replace("FMPS_BASIC", "客常来")
						.replace("FMPS", "客常来").replace("MU", "客常来").replace("VC", "客常来")
						.replace("INTO_PIECES", "客常来").replace("ZHCT_OLD", "智慧餐厅").replace("ZHCT", "智慧餐厅"));
                // 服务商
                responseDto.setAgentName(statistics.getAgentName());
                // 运营顾问
                responseDto.setOperationAdviserName(statistics.getOperationAdviserName());
                // 启动时间
                responseDto.setStartTime(DateUtils.formatDate(statistics.getStartTime(), DateUtils.YYYY_MM_DD_HH_MM));
                // 应完成交付时间
                responseDto.setShouldFlowEndTime(DateUtils.formatDate(statistics.getShouldFlowEndTime(), DateUtils.YYYY_MM_DD_HH_MM));

                // 是否已完成交付
                responseDto.setFlowEndFlag(convertFlag(statistics.getFlowEndFlag()));
                // 实际完成交付时间
                responseDto.setFlowEndTime(DateUtils.formatDate(statistics.getFlowEndTime(), DateUtils.YYYY_MM_DD_HH_MM));
                // 是否延期交付
                responseDto.setDelayFlag(convertFlag(statistics.getDelayFlag()));
                responseDto.setDelayDuration(String.valueOf(statistics.getDelayDuration()));
                // 开户顾问是否延期交付
                if(statistics.getFlowEndTime()!=null && statistics.getTrainTestTime()==null){
                	responseDto.setOpenDelayFlag("/");
                }else if(statistics.getFlowEndTime()!=null && statistics.getTrainTestTime()!=null){
                	responseDto.setOpenDelayFlag(convertFlag(statistics.getOpenDelayFlag()));
                }else{
                	if(statistics.getFlowEndTime()==null){
                		responseDto.setOpenDelayFlag(convertFlag(statistics.getOpenDelayFlag()));
                	}else{
                		responseDto.setOpenDelayFlag("/");
                	}
                }
                
                // 物料顾问是否延期交付
                if("INTO_PIECES".equals(statistics.getServiceType())){
                	responseDto.setMaterielDelayFlag("/");
                }else{
                	if(statistics.getFlowEndTime()!=null && statistics.getMaterielTime()==null){
                    	responseDto.setMaterielDelayFlag(null);
                    }else if(statistics.getFlowEndTime()!=null && statistics.getMaterielTime()!=null){
                    	responseDto.setMaterielDelayFlag(convertFlag(statistics.getMaterielDelayFlag()));
                    }else{
                    	if(statistics.getFlowEndTime()==null){
                    		responseDto.setMaterielDelayFlag(convertFlag(statistics.getMaterielDelayFlag()));
                    	}else{
                    		responseDto.setMaterielDelayFlag("/");
                    	}
                    }
                }
                // 运营顾问是否延期交付
                if("INTO_PIECES".equals(statistics.getServiceType())){
                	responseDto.setOperationDelayFlag("/");
                }else{
                	if(statistics.getFlowEndTime()!=null && statistics.getVisitServiceTime()==null){
                    	responseDto.setOperationDelayFlag("/");
                    }else if(statistics.getFlowEndTime()!=null && statistics.getVisitServiceTime()!=null){
                    	responseDto.setOperationDelayFlag(convertFlag(statistics.getOperationDelayFlag()));
                    }else{
                    	if(statistics.getFlowEndTime()==null){
                    		responseDto.setOperationDelayFlag(convertFlag(statistics.getOperationDelayFlag()));
                    	}else{
                    		responseDto.setOperationDelayFlag("/");
                    	}
                    }
                }
                
                // 开户顾问延期时长
                if(statistics.getFlowEndTime()!=null && statistics.getTrainTestTime()==null){
                	responseDto.setOpenDelayDuration("/");
                }else if(statistics.getFlowEndTime()!=null && statistics.getTrainTestTime()!=null){
                	responseDto.setOpenDelayDuration(String.valueOf(statistics.getOpenDelayDuration()));
                }else{
                	if(statistics.getFlowEndTime()==null){
                		responseDto.setOpenDelayDuration(String.valueOf(statistics.getOpenDelayDuration()));
                	}else{
                		responseDto.setOpenDelayDuration("/");
                	}
                }
                
                // 物料顾问延期时长
                if("INTO_PIECES".equals(statistics.getServiceType())){
                	responseDto.setMaterielDelayDuration("/");
                }else{
                	if(statistics.getFlowEndTime()!=null && statistics.getMaterielTime()==null){
                		responseDto.setMaterielDelayDuration("/");
                    }else if(statistics.getFlowEndTime()!=null && statistics.getMaterielTime()!=null){
                    	responseDto.setMaterielDelayDuration(String.valueOf(statistics.getMaterielDelayDuration()));
                    }else{
                    	if(statistics.getFlowEndTime()==null){
                    		responseDto.setMaterielDelayDuration(String.valueOf(statistics.getMaterielDelayDuration()));
                    	}else{
                    		responseDto.setMaterielDelayDuration("/");;
                    	}
                    }
                }
                // 运营顾问延期时长
                if("INTO_PIECES".equals(statistics.getServiceType())){
                	responseDto.setOperationDelayDuration("/");
                }else{
                	if(statistics.getFlowEndTime()!=null && statistics.getVisitServiceTime()==null){
                    	responseDto.setOperationDelayDuration("/");
                    }else if(statistics.getFlowEndTime()!=null && statistics.getVisitServiceTime()!=null){
                    	responseDto.setOperationDelayDuration(String.valueOf(statistics.getOperationDelayDuration()));
                    }else{
                    	if(statistics.getFlowEndTime()==null){
                    		responseDto.setOperationDelayDuration(String.valueOf(statistics.getOperationDelayDuration()));
                    	}else{
                    		responseDto.setOperationDelayDuration("/");
                    	}
                    }
                }
                if(!statistics.getRoles().contains("accountAdviser")){
            		responseDto.setOpenDelayFlag("/");
            	}
                if(!statistics.getRoles().contains("materialAdviser")){
                	responseDto.setMaterielDelayFlag("/");
            	}
                if(!statistics.getRoles().contains("OperationAdviser")){
                	responseDto.setOperationDelayFlag("/");
            	}
                if(!statistics.getRoles().contains("accountAdviser")){
            		responseDto.setOpenDelayDuration("/");
            	}
                if(!statistics.getRoles().contains("materialAdviser")){
                	responseDto.setMaterielDelayDuration("/");
            	}
                if(!statistics.getRoles().contains("OperationAdviser")){
                	responseDto.setOperationDelayDuration("/");
            	}
                
                // 订单id
                responseDto.setOrderId(statistics.getOrderId());
                // 流程id
                responseDto.setProcInsId(statistics.getProcInsId());
                
                responseDto.setServiceType(statistics.getServiceType());
                
                // 店铺id
                responseDto.setShopId(statistics.getShopId());
                
                responseDto.setExcptionLogo(statistics.getExcptionLogo());
                responseDto.setRoles(statistics.getRoles());
                
                responseDto.setVisitServiceTime(DateUtils.formatDate(statistics.getVisitServiceTime(), DateUtils.YYYY_MM_DD_HH_MM));
                responseDto.setShouldVisitServiceTime(DateUtils.formatDate(statistics.getShouldVisitServiceTime(), DateUtils.YYYY_MM_DD_HH_MM));
                responseDto.setMaterielTime(DateUtils.formatDate(statistics.getMaterielTime(), DateUtils.YYYY_MM_DD_HH_MM));
                responseDto.setShouldMaterielTime(DateUtils.formatDate(statistics.getShouldMaterielTime(), DateUtils.YYYY_MM_DD_HH_MM));
                responseDto.setTrainTestTime(DateUtils.formatDate(statistics.getTrainTestTime(), DateUtils.YYYY_MM_DD_HH_MM));
                responseDto.setShouldTrainTestTime(DateUtils.formatDate(statistics.getShouldTrainTestTime(), DateUtils.YYYY_MM_DD_HH_MM));
                
                responseList.add(responseDto);

            }
        }
        return result;
    }

    /**
     * 转换 Y、N 》 是、否
     *
     * @param flowEndFlag
     * @return
     * @date 2018年5月28日
     * @author linqunzhi
     */
    private static String convertFlag(String flowEndFlag) {
        return CommonConstants.Sign.YES.equals(flowEndFlag) ? "是" : "否";
    }

    /**
     * 获取生产服务个人明细列表
     *
     * @param page
     * @param requestDto
     * @param principalId
     * @return
     * @date 2018年5月29日
     * @author linqunzhi
     */
    public DeliveryServiceStatisticsAllResponseDto findUserStatistics(Page<DeliveryServiceStatisticsRequestDto> page,
                    DeliveryServiceStatisticsRequestDto dto, String principalId) {
        String dtoStr = JSON.toJSONString(dto);
        logger.info("findTeamStatistics start | dto={}|principalId={}", dtoStr, principalId);
        if (dto == null) {
            logger.error("dto 不能为空");
            throw new ServiceException(CommonConstants.FailMsg.PARAM);
        }
        String startDateStr = dto.getStartDateStr();
        String endDateStr = dto.getEndDateStr();
        if (StringUtils.isBlank(startDateStr) || StringUtils.isBlank(endDateStr)) {
            logger.error("开始时间 和 结束时间 不能为空");
            throw new ServiceException(CommonConstants.FailMsg.PARAM);
        }
        // 计算出查询的用户id集合
        List<String> userIdList = new ArrayList<>();
        userIdList.add(principalId);
        dto.setUserIdList(userIdList);
        DeliveryServiceStatisticsAllResponseDto result = getPageResponseDto(page, dto);
        // 计算不需要展示的列
        List<String> notShowColumn = getUserNotShowColumn(principalId);
        result.setNotShowColumnList(notShowColumn);
        // 设置是否延迟服务提示内容数组
        result.setDelayServiceContentArr(calculateDelayServiceContentArr());
        result.setDelayServiceMUArr(calculateMUContentArr());
        result.setDelayServiceVCArr(calculateVCContentArr());
        return result;
    }
    
    /**
     * 获取生产服务个人明细列表
     *
     * @param page
     * @param requestDto
     * @param principalId
     * @return
     * @date 2018年5月29日
     * @author linqunzhi
     */
    public JSONObject findServiceDetail(Page<DeliveryServiceDetailRequestDto> page,
    		DeliveryServiceDetailRequestDto dto, String principalId) {
        String dtoStr = JSON.toJSONString(dto);
        logger.info("findTeamStatistics start | dto={}|principalId={}", dtoStr, principalId);
        if (dto == null) {
            logger.error("dto 不能为空");
            throw new ServiceException(CommonConstants.FailMsg.PARAM);
        }
        String startDateStr = dto.getStartDateStr();
        String endDateStr = dto.getEndDateStr();
        if (StringUtils.isBlank(startDateStr) || StringUtils.isBlank(endDateStr)) {
            logger.error("开始时间 和 结束时间 不能为空");
            throw new ServiceException(CommonConstants.FailMsg.PARAM);
        }
        
        JSONObject result = getPageServiceDetail(page, dto);
        return result;
    }
    
    
    /**
     * 获取生产服务个人明细列表
     *
     * @param page
     * @param requestDto
     * @param principalId
     * @return
     * @date 2018年5月29日
     * @author linqunzhi
     */
    public List<Map> findFlow(ErpUserTotal erpUserTotal) {
    	List<Map> result = dao.findFlow(erpUserTotal);
        return result;
    }
    
    /**
     * 获取生产服务订单信息列表
     *
     * @param page
     * @param dto
     * @return
     * @date 2018年5月28日
     * @author linqunzhi
     */
    private JSONObject getPageServiceDetail(Page<DeliveryServiceDetailRequestDto> page,
    		DeliveryServiceDetailRequestDto dto) {
    	JSONObject resObject = new JSONObject();
        if (dto == null) {
            return null;
        }
        
        dto.setPage(page);
        List<DeliveryServiceDetailRequestDto> list = detailDao.getPageServiceDetail(dto);
        
        long count = list.size();
        // 获取总条数
        if (page != null) {
            count = page.getCount();
        }
        resObject.put("count", count);
        resObject.put("list", list);
        resObject.put("code", 0);
        return resObject;
    }
    
    /**
     * 获取生产服务订单信息列表
     *
     * @param page
     * @param dto
     * @return
     * @date 2018年5月28日
     * @author linqunzhi
     */
    public List<DeliveryServiceDetailRequestDto> getPageServiceDetail(DeliveryServiceDetailRequestDto dto) {
        List<DeliveryServiceDetailRequestDto> list = detailDao.getPageServiceDetail(dto);
        if(CollectionUtils.isNotEmpty(list)){
        	for(DeliveryServiceDetailRequestDto dt:list){
        		String procInsId = dt.getProcInsId();
        		ErpOrderFlowUser flow1=erpOrderFlowUserDao.findListByFlowId(procInsId,JykFlowConstants.OPERATION_ADVISER);
        		ErpOrderFlowUser flow2=erpOrderFlowUserDao.findListByFlowId(procInsId,JykFlowConstants.MATERIAL_ADVISER);
        		ErpOrderFlowUser flow3=erpOrderFlowUserDao.findListByFlowId(procInsId,JykFlowConstants.ACCOUNT_ADVISER);
        		dt.setOperationAdviser(flow1==null?"":flow1.getUser().getName());
        		dt.setMaterialAdviser(flow2==null?"":flow2.getUser().getName());
        		dt.setAccountAdviser(flow3==null?"":flow3.getUser().getName());
        	}
        }
        return list;
    }

    /**
     * 获取个人不需要展示的列
     *
     * @param principalId
     * @return
     * @date 2018年5月29日
     * @author linqunzhi
     */
    private List<String> getUserNotShowColumn(String principalId) {
        List<String> result = new ArrayList<>();
        Set<String> set = new HashSet<>();
        set.add(DeliveryServiceStatisticsColumn.AGENT_NAME.getCode());
        set.add(DeliveryServiceStatisticsColumn.OPEN_DELAY_FLAG.getCode());
        set.add(DeliveryServiceStatisticsColumn.MATERIEL_DELAY_FLAG.getCode());
        set.add(DeliveryServiceStatisticsColumn.OPERATION_DELAY_FLAG.getCode());
        set.add(DeliveryServiceStatisticsColumn.OPEN_DELAY_DURATION.getCode());
        set.add(DeliveryServiceStatisticsColumn.MATERIEL_DELAY_DURATION.getCode());
        set.add(DeliveryServiceStatisticsColumn.OPERATION_DELAY_DURATION.getCode());
        List<Role> list = roleService.findByUserId(principalId);
        if (CollectionUtils.isNotEmpty(list)) {
            for (Role role : list) {
                String enname = role.getEnname();
                // 根据拥有的角色 显示相关列
                if (RoleConstant.ACCOUNT_ADVISER.equals(enname) || RoleConstant.ACCOUNT_ADVISER_AGENT.equals(enname)) {
                    set.remove(DeliveryServiceStatisticsColumn.OPEN_DELAY_FLAG.getCode());
                    set.remove(DeliveryServiceStatisticsColumn.OPEN_DELAY_DURATION.getCode());
                } else if (RoleConstant.MATERIALADVISER.equals(enname) || RoleConstant.MATERIALADVISER_AGENT.equals(enname)) {
                    set.remove(DeliveryServiceStatisticsColumn.MATERIEL_DELAY_FLAG.getCode());
                    set.remove(DeliveryServiceStatisticsColumn.MATERIEL_DELAY_DURATION.getCode());
                } else if (RoleConstant.OPS_ADVISER.equals(enname) || RoleConstant.OPS_ADVISER_AGENT.equals(enname)) {
                    set.remove(DeliveryServiceStatisticsColumn.OPERATION_DELAY_FLAG.getCode());
                    set.remove(DeliveryServiceStatisticsColumn.OPERATION_DELAY_DURATION.getCode());
                }
            }
        }
        result.addAll(set);
        return result;
    }

    /**
     * 获取团队查询数据
     *
     * @param principalId
     * @return
     * @date 2018年5月31日
     * @author linqunzhi
     */
    public QueryDataResponseDto getTeamQueryData(String principalId) {
        logger.info("getTeamQueryData start | principalId={}", principalId);
        // 团队集合
        List<Map<String, String>> teamList = getQueryDataTeamList(principalId);
        // 时间维度集合
        List<Map<String, String>> dateTypeList = getQueryDateTypeList();
        // 服务类型集合
        List<Map<String, String>> serviceTypeList = getQueryServiceTypeList();
        
        // 服务类型集合
        List<Map<String, String>> serviceCodeList = getQueryServiceCodeList();
        // 组装dto
        QueryDataResponseDto result = new QueryDataResponseDto();
        result.setDateTypeList(dateTypeList);
        result.setServiceTypeList(serviceTypeList);
        result.setServiceCodeList(serviceCodeList);
        result.setTeamList(teamList);
        logger.info("getTeamQueryData end");
        return result;
    }
    
    /**
     * 获取团队查询数据
     *
     * @param principalId
     * @return
     * @date 2018年5月31日
     * @author linqunzhi
     */
    public JSONObject getServiceQueryData(String principalId) {
        logger.info("getTeamQueryData start | principalId={}", principalId);
        JSONObject resObject = new JSONObject();
        // 类别
        List<Map<String, String>> teamTypeList = getQueryTeamList(principalId);
        
        List<Map<String, String>> teamList = findTeamByService(principalId);
        
        List<Map<String, String>> userList = findUserByService(principalId);
        // 服务类型集合
        String serviceTypeList= getServiceTypeList();
        // 组装dto
        resObject.put("teamTypeList", teamTypeList);
        resObject.put("teamList", teamList);
        resObject.put("userList", userList);
        resObject.put("serviceTypeList", serviceTypeList);
        logger.info("getServiceQueryData end");
        return resObject;
    }

    /**
     * 获取团队查询数据
     *
     * @param principalId
     * @return
     * @date 2018年5月31日
     * @author linqunzhi
     */
    public List<Map<String, String>> getServiceByServiType(String serverType,String userId) {
        // 类别
        List<Map<String, String>> result = erpTeamDao.findTeamByType(serverType,userId);
        logger.info("getServiceQueryData end");
        return result;
    }
    
    /**
     * 获取团队查询数据
     *
     * @param principalId
     * @return
     * @date 2018年5月31日
     * @author linqunzhi
     */
    public List<Map<String, String>> getServiceTypeByTeam(String teamId) {
        // 类别
        List<Map<String, String>> result = erpTeamDao.findUserByService(teamId);
        logger.info("getServiceQueryData end");
        return result;
    }

    /**
     * 获取个人查询数据
     *
     * @return
     * @date 2018年5月31日
     * @author linqunzhi
     */
    public QueryDataResponseDto getUserQueryData() {
        logger.info("getUserQueryData start");
        // 时间维度集合
        List<Map<String, String>> dateTypeList = getQueryDateTypeList();
        // 服务类型集合
        List<Map<String, String>> serviceTypeList = getQueryServiceTypeList();
     // 服务类型集合
        List<Map<String, String>> serviceCodeList = getQueryServiceCodeList();
        // 组装dto
        QueryDataResponseDto result = new QueryDataResponseDto();
        result.setDateTypeList(dateTypeList);
        result.setServiceTypeList(serviceTypeList);
        result.setServiceCodeList(serviceCodeList);
        logger.info("getUserQueryData end");
        return result;
    }

    /**
     * 获取查询服务类型集合
     *
     * @date 2018年5月31日
     * @author linqunzhi
     */
    private static List<Map<String, String>> getQueryServiceTypeList() {
        List<Map<String, String>> serviceTypeList = new ArrayList<>();
        Map<String, String> serviceJu = new HashMap<>();
        serviceJu.put(DeliveryServiceStatisticsConstants.ServiceType.JU_YING_KE_CODE, DeliveryServiceStatisticsConstants.ServiceType.JU_YING_KE_NAME);
        Map<String, String> serviceKe = new HashMap<>();
        serviceKe.put(DeliveryServiceStatisticsConstants.ServiceType.KE_CHANG_LAI_CODE,
                        DeliveryServiceStatisticsConstants.ServiceType.KE_CHANG_LAI_NAME);
        Map<String, String> serviceZhihui = new HashMap<>();
        serviceJu.put(DeliveryServiceStatisticsConstants.ServiceType.ZHI_HUI_CODE, DeliveryServiceStatisticsConstants.ServiceType.ZHI_HUI_NAME);
        serviceTypeList.add(serviceJu);
        serviceTypeList.add(serviceKe);
        serviceTypeList.add(serviceZhihui);
        return serviceTypeList;
    }
    
    /**
     * 获取查询服务类型集合
     *
     * @date 2018年5月31日
     * @author linqunzhi
     */
    private static List<Map<String, String>> getQueryServiceCodeList() {
        List<Map<String, String>> serviceTypeList = new ArrayList<>();
        Map<String, String> fmps = new HashMap<>();
        fmps.put(DeliveryServiceStatisticsConstants.ServiceCode.FMPS, DeliveryServiceStatisticsConstants.ServiceCode.FMPS_CODE);
        Map<String, String> jyk = new HashMap<>();
        jyk.put(DeliveryServiceStatisticsConstants.ServiceCode.JYK, DeliveryServiceStatisticsConstants.ServiceCode.JYK_CODE);
        Map<String, String> into = new HashMap<>();
        into.put(DeliveryServiceStatisticsConstants.ServiceCode.INTOPIECES, DeliveryServiceStatisticsConstants.ServiceCode.INTOPIECES_CODE);
        Map<String, String> srids = new HashMap<>();
        srids.put(DeliveryServiceStatisticsConstants.ServiceCode.SRIDS, DeliveryServiceStatisticsConstants.ServiceCode.SRIDS_CODE);
        Map<String, String> fmps_basic = new HashMap<>();
        srids.put(DeliveryServiceStatisticsConstants.ServiceCode.FMPS_BASIC, DeliveryServiceStatisticsConstants.ServiceCode.FMPS_BASIC_CODE);
        serviceTypeList.add(fmps);
        serviceTypeList.add(jyk);
        serviceTypeList.add(into);
        serviceTypeList.add(srids);
        serviceTypeList.add(fmps_basic);
        return serviceTypeList;
    }
    
    /**
     * 获取查询服务类型集合
     *
     * @date 2018年5月31日
     * @author linqunzhi
     */
    private static String getServiceTypeList() {
        String ser="[{\"serviceName\":\"掌贝平台交付服务\",\"serviceType\":\"INTO_PIECES\"},"
        		+ "{\"serviceName\":\"智能客流运营全套落地服务\",\"serviceType\":\"FMPS\"},"
        		+ "{\"serviceName\":\"售后上门培训服务\",\"serviceType\":\"VC\"},"
        		+ "{\"serviceName\":\"聚引客服务\",\"serviceType\":\"JYK\"},"
        		+ "{\"serviceName\":\"首次上门基础策划服务\",\"serviceType\":\"FMPS_BASIC\"},"
        		+ "{\"serviceName\":\"物料更新服务\",\"serviceType\":\"MU\"}]";
//        serviceJu.put("掌贝售后维护服务包", null);
//        serviceJu.put("营销优化服务（远程）", null);
//        serviceJu.put("营销优化服务（上门）", null);
//        serviceJu.put("售后上门维修服务", null);
        return ser;
    }

    /**
     * 获取查询时间维度集合
     *
     * @date 2018年5月31日
     * @author linqunzhi
     */
    private static List<Map<String, String>> getQueryDateTypeList() {
        List<Map<String, String>> dateTypeList = new ArrayList<>();
        Map<String, String> dateBuy = new HashMap<>();
        dateBuy.put(DeliveryServiceStatisticsConstants.DateType.BUY_CODE, DeliveryServiceStatisticsConstants.DateType.BUY_NAME);
        Map<String, String> dateStart = new HashMap<>();
        dateStart.put(DeliveryServiceStatisticsConstants.DateType.START_CODE, DeliveryServiceStatisticsConstants.DateType.START_NAME);
        Map<String, String> dateShouldFlowEnd = new HashMap<>();
        dateShouldFlowEnd.put(DeliveryServiceStatisticsConstants.DateType.SHOULD_FLOW_END_CODE,
                        DeliveryServiceStatisticsConstants.DateType.SHOULD_FLOW_END_NAME);
        dateTypeList.add(dateBuy);
        dateTypeList.add(dateStart);
        dateTypeList.add(dateShouldFlowEnd);
        return dateTypeList;
    }

    /**
     * 获取查询团队list
     *
     * @param principalId
     * @date 2018年5月31日
     * @author linqunzhi
     */
    private List<Map<String, String>> getQueryDataTeamList(String principalId) {
//        List<ErpTeam> list = erpTeamService.findByUserId(principalId);
        List<ErpTeam> list = erpTeamService.findByUserId1(principalId,UserUtils.getUser().getAgentId());
        List<Map<String, String>> teamList = new ArrayList<>();
        Map<String, String> teamMap = null;
        if (CollectionUtils.isNotEmpty(list)) {
            for (ErpTeam team : list) {
                teamMap = new HashMap<>();
                teamMap.put(team.getId(), team.getTeamName());
                teamList.add(teamMap);
            }
        }
        return teamList;
    }

    /**
     * 获取查询团队list
     *
     * @param principalId
     * @date 2018年5月31日
     * @author linqunzhi
     */
    private List<Map<String, String>> getQueryTeamList(String principalId) {
        List<Map<String, String>> teamList = erpTeamService.findByService();
        return teamList;
    }
    
    /**
     * 获取查询团队list
     *
     * @param principalId
     * @date 2018年5月31日
     * @author linqunzhi
     */
    private List<Map<String, String>> findTeamByService(String principalId) {
        List<Map<String, String>> teamList = erpTeamService.findTeamByService(principalId);
        return teamList;
    }
    
    /**
     * 获取查询团队list
     *
     * @param principalId
     * @date 2018年5月31日
     * @author linqunzhi
     */
    private List<Map<String, String>> findUserByService(String principalId) {
        List<Map<String, String>> teamList = erpTeamService.findUserByService(principalId);
        return teamList;
    }
    
    /**
     * 获取生产服务团队订单管理明细列表
     *
     * @param page
     * @param dto
     * @param principalId
     * @return
     * @date 2018年5月29日
     * @author linqunzhi
     */
    public DeliveryServiceStatisticsAllResponseDto findTeamStatisticsDetail(Page<DeliveryServiceStatisticsDetailDto> page,
    		DeliveryServiceStatisticsDetailDto dto,
                    String principalId) {
        String dtoStr = JSON.toJSONString(dto);
        logger.info("findTeamStatistics start | dto={}|principalId={}", dtoStr, principalId);
        if (dto == null) {
            logger.error("dto 不能为空");
            throw new ServiceException(CommonConstants.FailMsg.PARAM);
        }
        DeliveryServiceStatisticsAllResponseDto result = getPageResponseDto1(page, dto);
        // 设置是否延迟服务提示内容数组
        result.setDelayServiceContentArr(calculateDelayServiceContentArr());
        result.setDelayServiceMUArr(calculateMUContentArr());
        result.setDelayServiceVCArr(calculateVCContentArr());
        logger.info("findTeamStatistics end | result.count={}", result.getCount());
        return result;
    }
    
    /**
     * 获取生产服务团队订单管理明细列表
     *
     * @param page
     * @param dto
     * @param principalId
     * @return
     * @date 2018年5月29日
     * @author linqunzhi
     */
    public DeliveryServiceStatisticsAllResponseDto findTeamStatisticsDetailByUser(Page<DeliveryServiceStatisticsDetailDto> page,
    		DeliveryServiceStatisticsDetailDto dto,
                    String principalId) {
        String dtoStr = JSON.toJSONString(dto);
        logger.info("findTeamStatistics start | dto={}|principalId={}", dtoStr, principalId);
        if (dto == null) {
            logger.error("dto 不能为空");
            throw new ServiceException(CommonConstants.FailMsg.PARAM);
        }
        DeliveryServiceStatisticsAllResponseDto result = getPageResponseDto(page, dto);
        // 设置是否延迟服务提示内容数组
        result.setDelayServiceContentArr(calculateDelayServiceContentArr());
        result.setDelayServiceMUArr(calculateMUContentArr());
        result.setDelayServiceVCArr(calculateVCContentArr());
        logger.info("findTeamStatistics end | result.count={}", result.getCount());
        return result;
    }
    
    /**
     * 获取生产服务订单信息列表
     *
     * @param page
     * @param dto
     * @return
     * @date 2018年5月28日
     * @author linqunzhi
     */
    private DeliveryServiceStatisticsAllResponseDto getPageResponseDto1(Page<DeliveryServiceStatisticsDetailDto> page,
    		DeliveryServiceStatisticsDetailDto dto) {
        DeliveryServiceStatisticsAllResponseDto result = new DeliveryServiceStatisticsAllResponseDto();
        if (dto == null) {
            return result;
        }
        dto.setPage(page);
        List<DeliveryServiceStatistics> list = servideDetailDao.findStatistics(dto);
        if (CollectionUtils.isEmpty(list)) {
            return result;
        }
        long count = list.size();
        // 获取总条数
        if (page != null) {
            count = page.getCount();
        }
        result.setCount(count);
        DeliveryServiceStatisticsResponseDto responseDto = null;
        // 循环list 获取 responseDto 集合
        if (CollectionUtils.isNotEmpty(list)) {
            List<DeliveryServiceStatisticsResponseDto> responseList = new ArrayList<>();
            // 将集合放入 result 中
            result.setList(responseList);
            for (DeliveryServiceStatistics statistics : list) {
                responseDto = new DeliveryServiceStatisticsResponseDto();
                if(!StringUtil.isBlank(statistics.getServiceType())){
                	statistics.setServiceType(changeServiceTypeSort(statistics.getServiceType()));
            		if(statistics.getServiceType().startsWith(",")){
            			statistics.setServiceType(statistics.getServiceType().substring(1));
            		}
            		if(statistics.getServiceType().endsWith(",")){
            			statistics.setServiceType(statistics.getServiceType().substring(0,statistics.getServiceType().length()-1));
            		}
            		
            	}
                // 订单号
                responseDto.setOrderNum(statistics.getOrderNumber());
                // 购买时间
                responseDto.setBuyDate(DateUtils.formatDate(statistics.getBuyDate(), DateUtils.YYYY_MM_DD_HH_MM));
                // 订单类别名称
                responseDto.setOrderType(statistics.getOrderType() != null && statistics
                                .getOrderType() == OrderConstants.TYPE_DIRECT ? "直销" : "服务商");
                // 商户名称
                responseDto.setShopName(statistics.getShopName());
                // 已购服务类别名称
                responseDto.setServiceTypeNames(statistics.getServiceType()==null?null:statistics.getServiceType().replace("JYK", "聚引客").replace("FMPS_BASIC", "客常来")
						.replace("FMPS", "客常来").replace("MU", "客常来").replace("VC", "客常来")
						.replace("INTO_PIECES", "客常来").replace("ZHCT_OLD", "智慧餐厅").replace("ZHCT", "智慧餐厅"));
                // 服务商
                responseDto.setAgentName(statistics.getAgentName());
                // 运营顾问
                responseDto.setOperationAdviserName(statistics.getOperationAdviserName());
                // 启动时间
                responseDto.setStartTime(DateUtils.formatDate(statistics.getStartTime(), DateUtils.YYYY_MM_DD_HH_MM));
                // 应完成交付时间
                responseDto.setShouldFlowEndTime(DateUtils.formatDate(statistics.getShouldFlowEndTime(), DateUtils.YYYY_MM_DD_HH_MM));

                // 是否已完成交付
                responseDto.setFlowEndFlag(convertFlag(statistics.getFlowEndFlag()));
                // 实际完成交付时间
                responseDto.setFlowEndTime(DateUtils.formatDate(statistics.getFlowEndTime(), DateUtils.YYYY_MM_DD_HH_MM));
                // 是否延期交付
                responseDto.setDelayFlag(convertFlag(statistics.getDelayFlag()));
             // 开户顾问是否延期交付
                if(statistics.getFlowEndTime()!=null && statistics.getTrainTestTime()==null){
                	responseDto.setOpenDelayFlag("/");
                }else if(statistics.getFlowEndTime()!=null && statistics.getTrainTestTime()!=null){
                	responseDto.setOpenDelayFlag(convertFlag(statistics.getOpenDelayFlag()));
                }else{
                	if(statistics.getFlowEndTime()==null){
                		responseDto.setOpenDelayFlag(convertFlag(statistics.getOpenDelayFlag()));
                	}else{
                		responseDto.setOpenDelayFlag("/");
                	}
                }
                // 物料顾问是否延期交付
                if("INTO_PIECES".equals(statistics.getServiceType())){
                	responseDto.setMaterielDelayFlag("/");
                }else{
                	if(statistics.getFlowEndTime()!=null && statistics.getMaterielTime()==null){
                    	responseDto.setMaterielDelayFlag(null);
                    }else if(statistics.getFlowEndTime()!=null && statistics.getMaterielTime()!=null){
                    	responseDto.setMaterielDelayFlag(convertFlag(statistics.getMaterielDelayFlag()));
                    }else{
                    	if(statistics.getFlowEndTime()==null){
                    		responseDto.setMaterielDelayFlag(convertFlag(statistics.getMaterielDelayFlag()));
                    	}else{
                    		responseDto.setMaterielDelayFlag("/");
                    	}
                    }
                }
                // 运营顾问是否延期交付
                if("INTO_PIECES".equals(statistics.getServiceType())){
                	responseDto.setOperationDelayFlag("/");
                }else{
                	if(statistics.getFlowEndTime()!=null && statistics.getVisitServiceTime()==null){
                    	responseDto.setOperationDelayFlag("/");
                    }else if(statistics.getFlowEndTime()!=null && statistics.getVisitServiceTime()!=null){
                    	responseDto.setOperationDelayFlag(convertFlag(statistics.getOperationDelayFlag()));
                    }else{
                    	if(statistics.getFlowEndTime()==null){
                    		responseDto.setOperationDelayFlag(convertFlag(statistics.getOperationDelayFlag()));
                    	}else{
                    		responseDto.setOperationDelayFlag("/");
                    	}
                    }
                }
                
                // 开户顾问延期时长
                if(statistics.getFlowEndTime()!=null && statistics.getTrainTestTime()==null){
                	responseDto.setOpenDelayDuration("/");
                }else if(statistics.getFlowEndTime()!=null && statistics.getTrainTestTime()!=null){
                	responseDto.setOpenDelayDuration(String.valueOf(statistics.getOpenDelayDuration()));
                }else{
                	if(statistics.getFlowEndTime()==null){
                		responseDto.setOpenDelayDuration(String.valueOf(statistics.getOpenDelayDuration()));
                	}else{
                		responseDto.setOpenDelayDuration("/");
                	}
                }
                
                // 物料顾问延期时长
                if("INTO_PIECES".equals(statistics.getServiceType())){
                	responseDto.setMaterielDelayDuration("/");
                }else{
                	if(statistics.getFlowEndTime()!=null && statistics.getMaterielTime()==null){
                		responseDto.setMaterielDelayDuration("/");
                    }else if(statistics.getFlowEndTime()!=null && statistics.getMaterielTime()!=null){
                    	responseDto.setMaterielDelayDuration(String.valueOf(statistics.getMaterielDelayDuration()));
                    }else{
                    	if(statistics.getFlowEndTime()==null){
                    		responseDto.setMaterielDelayDuration(String.valueOf(statistics.getMaterielDelayDuration()));
                    	}else{
                    		responseDto.setMaterielDelayDuration("/");;
                    	}
                    }
                }
                // 运营顾问延期时长
                if("INTO_PIECES".equals(statistics.getServiceType())){
                	responseDto.setOperationDelayDuration("/");
                }else{
                	if(statistics.getFlowEndTime()!=null && statistics.getVisitServiceTime()==null){
                    	responseDto.setOperationDelayDuration("/");
                    }else if(statistics.getFlowEndTime()!=null && statistics.getVisitServiceTime()!=null){
                    	responseDto.setOperationDelayDuration(String.valueOf(statistics.getOperationDelayDuration()));
                    }else{
                    	if(statistics.getFlowEndTime()==null){
                    		responseDto.setOperationDelayDuration(String.valueOf(statistics.getOperationDelayDuration()));
                    	}else{
                    		responseDto.setOperationDelayDuration("/");
                    	}
                    }
                }
                if(!statistics.getRoles().contains("accountAdviser")){
            		responseDto.setOpenDelayFlag("/");
            	}
                if(!statistics.getRoles().contains("materialAdviser")){
                	responseDto.setMaterielDelayFlag("/");
            	}
                if(!statistics.getRoles().contains("OperationAdviser")){
                	responseDto.setOperationDelayFlag("/");
            	}
                if(!statistics.getRoles().contains("accountAdviser")){
            		responseDto.setOpenDelayDuration("/");
            	}
                if(!statistics.getRoles().contains("materialAdviser")){
                	responseDto.setMaterielDelayDuration("/");
            	}
                if(!statistics.getRoles().contains("OperationAdviser")){
                	responseDto.setOperationDelayDuration("/");
            	}
                responseDto.setDelayDuration(String.valueOf(statistics.getDelayDuration()));
                // 订单id
                responseDto.setOrderId(statistics.getOrderId());
                // 流程id
                responseDto.setProcInsId(statistics.getProcInsId());
                
                responseDto.setServiceType(statistics.getServiceType());
                
                // 店铺id
                responseDto.setShopId(statistics.getShopId());
                
                responseDto.setExcptionLogo(statistics.getExcptionLogo());
                responseDto.setRoles(statistics.getRoles());
                
                responseDto.setVisitServiceTime(DateUtils.formatDate(statistics.getVisitServiceTime(), DateUtils.YYYY_MM_DD_HH_MM));
                responseDto.setShouldVisitServiceTime(DateUtils.formatDate(statistics.getShouldVisitServiceTime(), DateUtils.YYYY_MM_DD_HH_MM));
                responseDto.setMaterielTime(DateUtils.formatDate(statistics.getMaterielTime(), DateUtils.YYYY_MM_DD_HH_MM));
                responseDto.setShouldMaterielTime(DateUtils.formatDate(statistics.getShouldMaterielTime(), DateUtils.YYYY_MM_DD_HH_MM));
                responseDto.setTrainTestTime(DateUtils.formatDate(statistics.getTrainTestTime(), DateUtils.YYYY_MM_DD_HH_MM));
                responseDto.setShouldTrainTestTime(DateUtils.formatDate(statistics.getShouldTrainTestTime(), DateUtils.YYYY_MM_DD_HH_MM));
                responseList.add(responseDto);

            }
        }
        return result;
    }
    
    private String changeServiceTypeSort(String serviceTypes){
    	String[] sts=serviceTypes.split(",");
    	String ss="";
    	boolean run1=true;
    	boolean run2=true;
    	boolean run3=true;
		for(int i=0;i<sts.length;i++){
			if(serviceTypes.contains("FMPS_BASIC") && run1){
				ss+="FMPS_BASIC"+",";
				run1=false;
				continue;
			}
			if(serviceTypes.contains("FMPS") && run1){
				ss+="FMPS"+",";
				run1=false;
				continue;
			}
			if(serviceTypes.contains("JYK") && run3){
				ss+="JYK"+",";
				run3=false;
				continue;
			}
			if(serviceTypes.contains("VC") && run3){
				ss+="VC"+",";
				run3=false;
				continue;
			}
			if(serviceTypes.contains("MU") && run3){
				ss+="MU"+",";
				run3=false;
				continue;
			}
			if(serviceTypes.contains("ZHCT_OLD") && run2){
				ss+="ZHCT_OLD"+",";
				run2=false;
				continue;
			}
			if(serviceTypes.contains("ZHCT") && run2){
				ss+="ZHCT"+",";
				run2=false;
				continue;
			}
			if(serviceTypes.contains("INTO_PIECES") && run3){
				ss+="INTO_PIECES"+",";
				run3=false;
				continue;
			}
		}
    	return ss;
    }
    
    /**
     * 获取生产服务订单信息列表
     *
     * @param page
     * @param dto
     * @return
     * @date 2018年5月28日
     * @author linqunzhi
     */
    private DeliveryServiceStatisticsAllResponseDto getPageResponseDto(Page<DeliveryServiceStatisticsDetailDto> page,
    		DeliveryServiceStatisticsDetailDto dto) {
        DeliveryServiceStatisticsAllResponseDto result = new DeliveryServiceStatisticsAllResponseDto();
        if (dto == null) {
            return result;
        }
        dto.setPage(page);
        List<DeliveryServiceStatistics> list = servideDetailDao.findStatisticsByUser(dto);
        if (CollectionUtils.isEmpty(list)) {
            return result;
        }
        long count = list.size();
        // 获取总条数
        if (page != null) {
            count = page.getCount();
        }
        result.setCount(count);
        DeliveryServiceStatisticsResponseDto responseDto = null;
        // 循环list 获取 responseDto 集合
        if (CollectionUtils.isNotEmpty(list)) {
            List<DeliveryServiceStatisticsResponseDto> responseList = new ArrayList<>();
            // 将集合放入 result 中
            result.setList(responseList);
            for (DeliveryServiceStatistics statistics : list) {
                responseDto = new DeliveryServiceStatisticsResponseDto();
                if(!StringUtil.isBlank(statistics.getServiceType())){
                	statistics.setServiceType(changeServiceTypeSort(statistics.getServiceType()));
            		if(statistics.getServiceType().startsWith(",")){
            			statistics.setServiceType(statistics.getServiceType().substring(1));
            		}
            		if(statistics.getServiceType().endsWith(",")){
            			statistics.setServiceType(statistics.getServiceType().substring(0,statistics.getServiceType().length()-1));
            		}
            	}
                // 订单号
                responseDto.setOrderNum(statistics.getOrderNumber());
                // 购买时间
                responseDto.setBuyDate(DateUtils.formatDate(statistics.getBuyDate(), DateUtils.YYYY_MM_DD_HH_MM));
                // 订单类别名称
                responseDto.setOrderType(statistics.getOrderType() != null && statistics
                                .getOrderType() == OrderConstants.TYPE_DIRECT ? "直销" : "服务商");
                // 商户名称
                responseDto.setShopName(statistics.getShopName());
                // 已购服务类别名称
                responseDto.setServiceTypeNames(statistics.getServiceType()==null?null:statistics.getServiceType().replace("JYK", "聚引客").replace("FMPS_BASIC", "客常来")
						.replace("FMPS", "客常来").replace("MU", "客常来").replace("VC", "客常来")
						.replace("INTO_PIECES", "客常来").replace("ZHCT_OLD", "智慧餐厅").replace("ZHCT", "智慧餐厅"));
                // 服务商
                responseDto.setAgentName(statistics.getAgentName());
                // 运营顾问
                responseDto.setOperationAdviserName(statistics.getOperationAdviserName());
                // 启动时间
                responseDto.setStartTime(DateUtils.formatDate(statistics.getStartTime(), DateUtils.YYYY_MM_DD_HH_MM));
                // 应完成交付时间
                responseDto.setShouldFlowEndTime(DateUtils.formatDate(statistics.getShouldFlowEndTime(), DateUtils.YYYY_MM_DD_HH_MM));

                // 是否已完成交付
                responseDto.setFlowEndFlag(convertFlag(statistics.getFlowEndFlag()));
                // 实际完成交付时间
                responseDto.setFlowEndTime(DateUtils.formatDate(statistics.getFlowEndTime(), DateUtils.YYYY_MM_DD_HH_MM));
                // 是否延期交付
                responseDto.setDelayFlag(convertFlag(statistics.getDelayFlag()));
                // 开户顾问是否延期交付
                if(statistics.getFlowEndTime()!=null && statistics.getTrainTestTime()==null){
                	responseDto.setOpenDelayFlag("/");
                }else if(statistics.getFlowEndTime()!=null && statistics.getTrainTestTime()!=null){
                	responseDto.setOpenDelayFlag(convertFlag(statistics.getOpenDelayFlag()));
                }else{
                	if(statistics.getFlowEndTime()==null){
                		responseDto.setOpenDelayFlag(convertFlag(statistics.getOpenDelayFlag()));
                	}else{
                		responseDto.setOpenDelayFlag("/");
                	}
                }
                // 物料顾问是否延期交付
                if("INTO_PIECES".equals(statistics.getServiceType())){
                	responseDto.setMaterielDelayFlag("/");
                }else{
                	if(statistics.getFlowEndTime()!=null && statistics.getMaterielTime()==null){
                    	responseDto.setMaterielDelayFlag(null);
                    }else if(statistics.getFlowEndTime()!=null && statistics.getMaterielTime()!=null){
                    	responseDto.setMaterielDelayFlag(convertFlag(statistics.getMaterielDelayFlag()));
                    }else{
                    	if(statistics.getFlowEndTime()==null){
                    		responseDto.setMaterielDelayFlag(convertFlag(statistics.getMaterielDelayFlag()));
                    	}else{
                    		responseDto.setMaterielDelayFlag("/");
                    	}
                    }
                }
                // 运营顾问是否延期交付
                if("INTO_PIECES".equals(statistics.getServiceType())){
                	responseDto.setOperationDelayFlag("/");
                }else{
                	if(statistics.getFlowEndTime()!=null && statistics.getVisitServiceTime()==null){
                    	responseDto.setOperationDelayFlag("/");
                    }else if(statistics.getFlowEndTime()!=null && statistics.getVisitServiceTime()!=null){
                    	responseDto.setOperationDelayFlag(convertFlag(statistics.getOperationDelayFlag()));
                    }else{
                    	if(statistics.getFlowEndTime()==null){
                    		responseDto.setOperationDelayFlag(convertFlag(statistics.getOperationDelayFlag()));
                    	}else{
                    		responseDto.setOperationDelayFlag("/");
                    	}
                    }
                }
                
                // 开户顾问延期时长
                if(statistics.getFlowEndTime()!=null && statistics.getTrainTestTime()==null){
                	responseDto.setOpenDelayDuration("/");
                }else if(statistics.getFlowEndTime()!=null && statistics.getTrainTestTime()!=null){
                	responseDto.setOpenDelayDuration(String.valueOf(statistics.getOpenDelayDuration()));
                }else{
                	if(statistics.getFlowEndTime()==null){
                		responseDto.setOpenDelayDuration(String.valueOf(statistics.getOpenDelayDuration()));
                	}else{
                		responseDto.setOpenDelayDuration("/");
                	}
                }
                
                // 物料顾问延期时长
                if("INTO_PIECES".equals(statistics.getServiceType())){
                	responseDto.setMaterielDelayDuration("/");
                }else{
                	if(statistics.getFlowEndTime()!=null && statistics.getMaterielTime()==null){
                		responseDto.setMaterielDelayDuration("/");
                    }else if(statistics.getFlowEndTime()!=null && statistics.getMaterielTime()!=null){
                    	responseDto.setMaterielDelayDuration(String.valueOf(statistics.getMaterielDelayDuration()));
                    }else{
                    	if(statistics.getFlowEndTime()==null){
                    		responseDto.setMaterielDelayDuration(String.valueOf(statistics.getMaterielDelayDuration()));
                    	}else{
                    		responseDto.setMaterielDelayDuration("/");;
                    	}
                    }
                }
                // 运营顾问延期时长
                if("INTO_PIECES".equals(statistics.getServiceType())){
                	responseDto.setOperationDelayDuration("/");
                }else{
                	if(statistics.getFlowEndTime()!=null && statistics.getVisitServiceTime()==null){
                    	responseDto.setOperationDelayDuration("/");
                    }else if(statistics.getFlowEndTime()!=null && statistics.getVisitServiceTime()!=null){
                    	responseDto.setOperationDelayDuration(String.valueOf(statistics.getOperationDelayDuration()));
                    }else{
                    	if(statistics.getFlowEndTime()==null){
                    		responseDto.setOperationDelayDuration(String.valueOf(statistics.getOperationDelayDuration()));
                    	}else{
                    		responseDto.setOperationDelayDuration("/");
                    	}
                    }
                }
                if(!statistics.getRoles().contains("accountAdviser")){
            		responseDto.setOpenDelayFlag("/");
            	}
                if(!statistics.getRoles().contains("materialAdviser")){
                	responseDto.setMaterielDelayFlag("/");
            	}
                if(!statistics.getRoles().contains("OperationAdviser")){
                	responseDto.setOperationDelayFlag("/");
            	}
                if(!statistics.getRoles().contains("accountAdviser")){
            		responseDto.setOpenDelayDuration("/");
            	}
                if(!statistics.getRoles().contains("materialAdviser")){
                	responseDto.setMaterielDelayDuration("/");
            	}
                if(!statistics.getRoles().contains("OperationAdviser")){
                	responseDto.setOperationDelayDuration("/");
            	}
                responseDto.setDelayDuration(String.valueOf(statistics.getDelayDuration()));
                // 订单id
                responseDto.setOrderId(statistics.getOrderId());
                // 流程id
                responseDto.setProcInsId(statistics.getProcInsId());
                
                responseDto.setServiceType(statistics.getServiceType());
                
                // 店铺id
                responseDto.setShopId(statistics.getShopId());
                
                responseDto.setExcptionLogo(statistics.getExcptionLogo());
                
                responseDto.setRoles(statistics.getRoles());
                responseDto.setVisitServiceTime(DateUtils.formatDate(statistics.getVisitServiceTime(), DateUtils.YYYY_MM_DD_HH_MM));
                responseDto.setShouldVisitServiceTime(DateUtils.formatDate(statistics.getShouldVisitServiceTime(), DateUtils.YYYY_MM_DD_HH_MM));
                responseDto.setMaterielTime(DateUtils.formatDate(statistics.getMaterielTime(), DateUtils.YYYY_MM_DD_HH_MM));
                responseDto.setShouldMaterielTime(DateUtils.formatDate(statistics.getShouldMaterielTime(), DateUtils.YYYY_MM_DD_HH_MM));
                responseDto.setTrainTestTime(DateUtils.formatDate(statistics.getTrainTestTime(), DateUtils.YYYY_MM_DD_HH_MM));
                responseDto.setShouldTrainTestTime(DateUtils.formatDate(statistics.getShouldTrainTestTime(), DateUtils.YYYY_MM_DD_HH_MM));
                responseList.add(responseDto);

            }
        }
        return result;
    }
    
    /**
     * 获取生产服务团队订单管理明细列表
     *
     * @param page
     * @param dto
     * @param principalId
     * @return
     * @date 2018年5月29日
     * @author linqunzhi
     */
    public DeliveryServiceStatisticsAllResponseDto findTeamStatisticsByUser(Page<DeliveryServiceStatisticsDetailDto> page,
    		DeliveryServiceStatisticsDetailDto dto,
                    String principalId) {
        String dtoStr = JSON.toJSONString(dto);
        logger.info("findTeamStatistics start | dto={}|principalId={}", dtoStr, principalId);
        if (dto == null) {
            logger.error("dto 不能为空");
            throw new ServiceException(CommonConstants.FailMsg.PARAM);
        }
        DeliveryServiceStatisticsAllResponseDto result = getPageResponseDto2(page, dto);
        // 设置是否延迟服务提示内容数组
        result.setDelayServiceContentArr(calculateDelayServiceContentArr());
        result.setDelayServiceMUArr(calculateMUContentArr());
        result.setDelayServiceVCArr(calculateVCContentArr());
        logger.info("findTeamStatistics end | result.count={}", result.getCount());
        return result;
    }
    
    /**
     * 获取生产服务订单信息列表
     *
     * @param page
     * @param dto
     * @return
     * @date 2018年5月28日
     * @author linqunzhi
     */
    private DeliveryServiceStatisticsAllResponseDto getPageResponseDto2(Page<DeliveryServiceStatisticsDetailDto> page,
    		DeliveryServiceStatisticsDetailDto dto) {
        DeliveryServiceStatisticsAllResponseDto result = new DeliveryServiceStatisticsAllResponseDto();
        if (dto == null) {
            return result;
        }
        dto.setPage(page);
        List<DeliveryServiceStatistics> list = servideDetailDao.findStatisticsByUser2(dto);
        if (CollectionUtils.isEmpty(list)) {
            return result;
        }
        long count = list.size();
        // 获取总条数
        if (page != null) {
            count = page.getCount();
        }
        result.setCount(count);
        DeliveryServiceStatisticsResponseDto responseDto = null;
        // 循环list 获取 responseDto 集合
        if (CollectionUtils.isNotEmpty(list)) {
            List<DeliveryServiceStatisticsResponseDto> responseList = new ArrayList<>();
            // 将集合放入 result 中
            result.setList(responseList);
            for (DeliveryServiceStatistics statistics : list) {
                responseDto = new DeliveryServiceStatisticsResponseDto();
                if(!StringUtil.isBlank(statistics.getServiceType())){
                	statistics.setServiceType(changeServiceTypeSort(statistics.getServiceType()));
            		if(statistics.getServiceType().startsWith(",")){
            			statistics.setServiceType(statistics.getServiceType().substring(1));
            		}
            		if(statistics.getServiceType().endsWith(",")){
            			statistics.setServiceType(statistics.getServiceType().substring(0,statistics.getServiceType().length()-1));
            		}
            	}
                // 订单号
                responseDto.setOrderNum(statistics.getOrderNumber());
                // 购买时间
                responseDto.setBuyDate(DateUtils.formatDate(statistics.getBuyDate(), DateUtils.YYYY_MM_DD_HH_MM));
                // 订单类别名称
                responseDto.setOrderType(statistics.getOrderType() != null && statistics
                                .getOrderType() == OrderConstants.TYPE_DIRECT ? "直销" : "服务商");
                // 商户名称
                responseDto.setShopName(statistics.getShopName());
                // 已购服务类别名称
                responseDto.setServiceTypeNames(statistics.getServiceType()==null?null:statistics.getServiceType().replace("JYK", "聚引客").replace("FMPS_BASIC", "客常来")
						.replace("FMPS", "客常来").replace("MU", "客常来").replace("VC", "客常来")
						.replace("INTO_PIECES", "客常来").replace("ZHCT_OLD", "智慧餐厅").replace("ZHCT", "智慧餐厅"));
                // 服务商
                responseDto.setAgentName(statistics.getAgentName());
                // 运营顾问
                responseDto.setOperationAdviserName(statistics.getOperationAdviserName());
                // 启动时间
                responseDto.setStartTime(DateUtils.formatDate(statistics.getStartTime(), DateUtils.YYYY_MM_DD_HH_MM));
                // 应完成交付时间
                responseDto.setShouldFlowEndTime(DateUtils.formatDate(statistics.getShouldFlowEndTime(), DateUtils.YYYY_MM_DD_HH_MM));

                // 是否已完成交付
                responseDto.setFlowEndFlag(convertFlag(statistics.getFlowEndFlag()));
                // 实际完成交付时间
                responseDto.setFlowEndTime(DateUtils.formatDate(statistics.getFlowEndTime(), DateUtils.YYYY_MM_DD_HH_MM));
                // 是否延期交付
                responseDto.setDelayFlag(convertFlag(statistics.getDelayFlag()));
                // 开户顾问是否延期交付
                if(statistics.getFlowEndTime()!=null && statistics.getTrainTestTime()==null){
                	responseDto.setOpenDelayFlag("/");
                }else if(statistics.getFlowEndTime()!=null && statistics.getTrainTestTime()!=null){
                	responseDto.setOpenDelayFlag(convertFlag(statistics.getOpenDelayFlag()));
                }else{
                	if(statistics.getFlowEndTime()==null){
                		responseDto.setOpenDelayFlag(convertFlag(statistics.getOpenDelayFlag()));
                	}else{
                		responseDto.setOpenDelayFlag("/");
                	}
                }
                // 物料顾问是否延期交付
                if("INTO_PIECES".equals(statistics.getServiceType())){
                	responseDto.setMaterielDelayFlag("/");
                }else{
                	if(statistics.getFlowEndTime()!=null && statistics.getMaterielTime()==null){
                    	responseDto.setMaterielDelayFlag(null);
                    }else if(statistics.getFlowEndTime()!=null && statistics.getMaterielTime()!=null){
                    	responseDto.setMaterielDelayFlag(convertFlag(statistics.getMaterielDelayFlag()));
                    }else{
                    	if(statistics.getFlowEndTime()==null){
                    		responseDto.setMaterielDelayFlag(convertFlag(statistics.getMaterielDelayFlag()));
                    	}else{
                    		responseDto.setMaterielDelayFlag("/");
                    	}
                    }
                }
                // 运营顾问是否延期交付
                if("INTO_PIECES".equals(statistics.getServiceType())){
                	responseDto.setOperationDelayFlag("/");
                }else{
                	if(statistics.getFlowEndTime()!=null && statistics.getVisitServiceTime()==null){
                    	responseDto.setOperationDelayFlag("/");
                    }else if(statistics.getFlowEndTime()!=null && statistics.getVisitServiceTime()!=null){
                    	responseDto.setOperationDelayFlag(convertFlag(statistics.getOperationDelayFlag()));
                    }else{
                    	if(statistics.getFlowEndTime()==null){
                    		responseDto.setOperationDelayFlag(convertFlag(statistics.getOperationDelayFlag()));
                    	}else{
                    		responseDto.setOperationDelayFlag("/");
                    	}
                    }
                }
                
                // 开户顾问延期时长
                if(statistics.getFlowEndTime()!=null && statistics.getTrainTestTime()==null){
                	responseDto.setOpenDelayDuration("/");
                }else if(statistics.getFlowEndTime()!=null && statistics.getTrainTestTime()!=null){
                	responseDto.setOpenDelayDuration(String.valueOf(statistics.getOpenDelayDuration()));
                }else{
                	if(statistics.getFlowEndTime()==null){
                		responseDto.setOpenDelayDuration(String.valueOf(statistics.getOpenDelayDuration()));
                	}else{
                		responseDto.setOpenDelayDuration("/");
                	}
                }
                
                // 物料顾问延期时长
                if("INTO_PIECES".equals(statistics.getServiceType())){
                	responseDto.setMaterielDelayDuration("/");
                }else{
                	if(statistics.getFlowEndTime()!=null && statistics.getMaterielTime()==null){
                		responseDto.setMaterielDelayDuration("/");
                    }else if(statistics.getFlowEndTime()!=null && statistics.getMaterielTime()!=null){
                    	responseDto.setMaterielDelayDuration(String.valueOf(statistics.getMaterielDelayDuration()));
                    }else{
                    	if(statistics.getFlowEndTime()==null){
                    		responseDto.setMaterielDelayDuration(String.valueOf(statistics.getMaterielDelayDuration()));
                    	}else{
                    		responseDto.setMaterielDelayDuration("/");;
                    	}
                    }
                }
                // 运营顾问延期时长
                if("INTO_PIECES".equals(statistics.getServiceType())){
                	responseDto.setOperationDelayDuration("/");
                }else{
                	if(statistics.getFlowEndTime()!=null && statistics.getVisitServiceTime()==null){
                    	responseDto.setOperationDelayDuration("/");
                    }else if(statistics.getFlowEndTime()!=null && statistics.getVisitServiceTime()!=null){
                    	responseDto.setOperationDelayDuration(String.valueOf(statistics.getOperationDelayDuration()));
                    }else{
                    	if(statistics.getFlowEndTime()==null){
                    		responseDto.setOperationDelayDuration(String.valueOf(statistics.getOperationDelayDuration()));
                    	}else{
                    		responseDto.setOperationDelayDuration("/");
                    	}
                    }
                }
                
                if(!statistics.getRoles().contains("accountAdviser")){
            		responseDto.setOpenDelayFlag("/");
            	}
                if(!statistics.getRoles().contains("materialAdviser")){
                	responseDto.setMaterielDelayFlag("/");
            	}
                if(!statistics.getRoles().contains("OperationAdviser")){
                	responseDto.setOperationDelayFlag("/");
            	}
                if(!statistics.getRoles().contains("accountAdviser")){
            		responseDto.setOpenDelayDuration("/");
            	}
                if(!statistics.getRoles().contains("materialAdviser")){
                	responseDto.setMaterielDelayDuration("/");
            	}
                if(!statistics.getRoles().contains("OperationAdviser")){
                	responseDto.setOperationDelayDuration("/");
            	}
                
                responseDto.setDelayDuration(String.valueOf(statistics.getDelayDuration()));
                // 订单id
                responseDto.setOrderId(statistics.getOrderId());
                // 流程id
                responseDto.setProcInsId(statistics.getProcInsId());
                
                responseDto.setServiceType(statistics.getServiceType());
                
                // 店铺id
                responseDto.setShopId(statistics.getShopId());
                
                responseDto.setExcptionLogo(statistics.getExcptionLogo());
                responseDto.setRoles(statistics.getRoles());
                responseList.add(responseDto);

            }
        }
        return result;
    }
    
    /**
     * 获取生产服务团队订单管理明细列表
     *
     * @param page
     * @param dto
     * @param principalId
     * @return
     * @date 2018年5月29日
     * @author linqunzhi
     */
    public DeliveryServiceStatisticsAllResponseDto findStatisticsCompleteDetail(Page<DeliveryServiceStatisticsDetailDto> page,
    		DeliveryServiceStatisticsDetailDto dto,
                    String principalId) {
        String dtoStr = JSON.toJSONString(dto);
        logger.info("findTeamStatistics start | dto={}|principalId={}", dtoStr, principalId);
        if (dto == null) {
            logger.error("dto 不能为空");
            throw new ServiceException(CommonConstants.FailMsg.PARAM);
        }
        DeliveryServiceStatisticsAllResponseDto result = getPageResponseDto3(page, dto);
        // 设置是否延迟服务提示内容数组
        result.setDelayServiceContentArr(calculateDelayServiceContentArr());
        result.setDelayServiceMUArr(calculateMUContentArr());
        result.setDelayServiceVCArr(calculateVCContentArr());
        logger.info("findTeamStatistics end | result.count={}", result.getCount());
        return result;
    }
    
    /**
     * 获取生产服务订单信息列表
     *
     * @param page
     * @param dto
     * @return
     * @date 2018年5月28日
     * @author linqunzhi
     */
    private DeliveryServiceStatisticsAllResponseDto getPageResponseDto3(Page<DeliveryServiceStatisticsDetailDto> page,
    		DeliveryServiceStatisticsDetailDto dto) {
        DeliveryServiceStatisticsAllResponseDto result = new DeliveryServiceStatisticsAllResponseDto();
        if (dto == null) {
            return result;
        }
        dto.setPage(page);
        List<DeliveryServiceStatistics> list = servideDetailDao.findStatisticsComplete(dto);
        if (CollectionUtils.isEmpty(list)) {
            return result;
        }
        long count = list.size();
        // 获取总条数
        if (page != null) {
            count = page.getCount();
        }
        result.setCount(count);
        DeliveryServiceStatisticsResponseDto responseDto = null;
        // 循环list 获取 responseDto 集合
        if (CollectionUtils.isNotEmpty(list)) {
            List<DeliveryServiceStatisticsResponseDto> responseList = new ArrayList<>();
            // 将集合放入 result 中
            result.setList(responseList);
            for (DeliveryServiceStatistics statistics : list) {
                responseDto = new DeliveryServiceStatisticsResponseDto();
                // 订单号
                responseDto.setOrderNum(statistics.getOrderNumber());
                // 购买时间
                responseDto.setBuyDate(DateUtils.formatDate(statistics.getBuyDate(), DateUtils.YYYY_MM_DD_HH_MM));
                // 订单类别名称
                responseDto.setOrderType(statistics.getOrderType() != null && statistics
                                .getOrderType() == OrderConstants.TYPE_DIRECT ? "直销" : "服务商");
                // 商户名称
                responseDto.setShopName(statistics.getShopName());
                // 已购服务类别名称
                responseDto.setServiceTypeNames(statistics.getServiceType()==null?null:statistics.getServiceType().replace("JYK", "聚引客").replace("FMPS_BASIC", "客常来")
						.replace("FMPS", "客常来").replace("MU", "客常来").replace("VC", "客常来")
						.replace("INTO_PIECES", "客常来").replace("ZHCT_OLD", "智慧餐厅").replace("ZHCT", "智慧餐厅"));
                // 服务商
                responseDto.setAgentName(statistics.getAgentName());
                // 运营顾问
                responseDto.setOperationAdviserName(statistics.getOperationAdviserName());
                // 启动时间
                responseDto.setStartTime(DateUtils.formatDate(statistics.getStartTime(), DateUtils.YYYY_MM_DD_HH_MM));
                // 应完成交付时间
                responseDto.setShouldFlowEndTime(DateUtils.formatDate(statistics.getShouldFlowEndTime(), DateUtils.YYYY_MM_DD_HH_MM));

                // 是否已完成交付
                responseDto.setFlowEndFlag(convertFlag(statistics.getFlowEndFlag()));
                // 实际完成交付时间
                responseDto.setFlowEndTime(DateUtils.formatDate(statistics.getFlowEndTime(), DateUtils.YYYY_MM_DD_HH_MM));
                // 是否延期交付
                responseDto.setDelayFlag(convertFlag(statistics.getDelayFlag()));
                // 开户顾问是否延期交付
                if(statistics.getFlowEndTime()!=null && statistics.getTrainTestTime()==null){
                	responseDto.setOpenDelayFlag("/");
                }else if(statistics.getFlowEndTime()!=null && statistics.getTrainTestTime()!=null){
                	responseDto.setOpenDelayFlag(convertFlag(statistics.getOpenDelayFlag()));
                }else{
                	if(statistics.getFlowEndTime()==null){
                		responseDto.setOpenDelayFlag(convertFlag(statistics.getOpenDelayFlag()));
                	}else{
                		responseDto.setOpenDelayFlag("/");
                	}
                }
                // 物料顾问是否延期交付
                if("INTO_PIECES".equals(statistics.getServiceType())){
                	responseDto.setMaterielDelayFlag("/");
                }else{
                	if(statistics.getFlowEndTime()!=null && statistics.getMaterielTime()==null){
                    	responseDto.setMaterielDelayFlag(null);
                    }else if(statistics.getFlowEndTime()!=null && statistics.getMaterielTime()!=null){
                    	responseDto.setMaterielDelayFlag(convertFlag(statistics.getMaterielDelayFlag()));
                    }else{
                    	if(statistics.getFlowEndTime()==null){
                    		responseDto.setMaterielDelayFlag(convertFlag(statistics.getMaterielDelayFlag()));
                    	}else{
                    		responseDto.setMaterielDelayFlag("/");
                    	}
                    }
                }
                // 运营顾问是否延期交付
                if("INTO_PIECES".equals(statistics.getServiceType())){
                	responseDto.setOperationDelayFlag("/");
                }else{
                	if(statistics.getFlowEndTime()!=null && statistics.getVisitServiceTime()==null){
                    	responseDto.setOperationDelayFlag("/");
                    }else if(statistics.getFlowEndTime()!=null && statistics.getVisitServiceTime()!=null){
                    	responseDto.setOperationDelayFlag(convertFlag(statistics.getOperationDelayFlag()));
                    }else{
                    	if(statistics.getFlowEndTime()==null){
                    		responseDto.setOperationDelayFlag(convertFlag(statistics.getOperationDelayFlag()));
                    	}else{
                    		responseDto.setOperationDelayFlag("/");
                    	}
                    }
                }
                
                // 开户顾问延期时长
                if(statistics.getFlowEndTime()!=null && statistics.getTrainTestTime()==null){
                	responseDto.setOpenDelayDuration("/");
                }else if(statistics.getFlowEndTime()!=null && statistics.getTrainTestTime()!=null){
                	responseDto.setOpenDelayDuration(String.valueOf(statistics.getOpenDelayDuration()));
                }else{
                	if(statistics.getFlowEndTime()==null){
                		responseDto.setOpenDelayDuration(String.valueOf(statistics.getOpenDelayDuration()));
                	}else{
                		responseDto.setOpenDelayDuration("/");
                	}
                }
                
                // 物料顾问延期时长
                if("INTO_PIECES".equals(statistics.getServiceType())){
                	responseDto.setMaterielDelayDuration("/");
                }else{
                	if(statistics.getFlowEndTime()!=null && statistics.getMaterielTime()==null){
                		responseDto.setMaterielDelayDuration("/");
                    }else if(statistics.getFlowEndTime()!=null && statistics.getMaterielTime()!=null){
                    	responseDto.setMaterielDelayDuration(String.valueOf(statistics.getMaterielDelayDuration()));
                    }else{
                    	if(statistics.getFlowEndTime()==null){
                    		responseDto.setMaterielDelayDuration(String.valueOf(statistics.getMaterielDelayDuration()));
                    	}else{
                    		responseDto.setMaterielDelayDuration("/");;
                    	}
                    }
                }
                // 运营顾问延期时长
                if("INTO_PIECES".equals(statistics.getServiceType())){
                	responseDto.setOperationDelayDuration("/");
                }else{
                	if(statistics.getFlowEndTime()!=null && statistics.getVisitServiceTime()==null){
                    	responseDto.setOperationDelayDuration("/");
                    }else if(statistics.getFlowEndTime()!=null && statistics.getVisitServiceTime()!=null){
                    	responseDto.setOperationDelayDuration(String.valueOf(statistics.getOperationDelayDuration()));
                    }else{
                    	if(statistics.getFlowEndTime()==null){
                    		responseDto.setOperationDelayDuration(String.valueOf(statistics.getOperationDelayDuration()));
                    	}else{
                    		responseDto.setOperationDelayDuration("/");
                    	}
                    }
                }
                if(!statistics.getRoles().contains("accountAdviser")){
            		responseDto.setOpenDelayFlag("/");
            	}
                if(!statistics.getRoles().contains("materialAdviser")){
                	responseDto.setMaterielDelayFlag("/");
            	}
                if(!statistics.getRoles().contains("OperationAdviser")){
                	responseDto.setOperationDelayFlag("/");
            	}
                if(!statistics.getRoles().contains("accountAdviser")){
            		responseDto.setOpenDelayDuration("/");
            	}
                if(!statistics.getRoles().contains("materialAdviser")){
                	responseDto.setMaterielDelayDuration("/");
            	}
                if(!statistics.getRoles().contains("OperationAdviser")){
                	responseDto.setOperationDelayDuration("/");
            	}
                
                responseDto.setDelayDuration(String.valueOf(statistics.getDelayDuration()));
                // 订单id
                responseDto.setOrderId(statistics.getOrderId());
                // 流程id
                responseDto.setProcInsId(statistics.getProcInsId());
                
                responseDto.setServiceType(statistics.getServiceType());
                
                // 店铺id
                responseDto.setShopId(statistics.getShopId());
                
                responseDto.setExcptionLogo(statistics.getExcptionLogo());
                responseDto.setRoles(statistics.getRoles());
                responseList.add(responseDto);

            }
        }
        return result;
    }
}
