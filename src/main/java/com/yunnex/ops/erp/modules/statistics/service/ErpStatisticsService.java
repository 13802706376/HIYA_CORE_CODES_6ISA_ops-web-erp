package com.yunnex.ops.erp.modules.statistics.service;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.common.constant.CommonConstants;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.service.BaseService;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.common.utils.DateUtils;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.modules.act.dao.ActDao;
import com.yunnex.ops.erp.modules.act.service.ActTaskService;
import com.yunnex.ops.erp.modules.order.constant.OrderConstants;
import com.yunnex.ops.erp.modules.order.constant.OrderSplitConstants;
import com.yunnex.ops.erp.modules.order.service.ErpOrderSplitGoodService;
import com.yunnex.ops.erp.modules.order.service.ErpOrderSplitInfoService;
import com.yunnex.ops.erp.modules.order.util.OrderSplitUtil;
import com.yunnex.ops.erp.modules.statistics.constant.SplitStatisticsConstants;
import com.yunnex.ops.erp.modules.statistics.dao.ErpStatisticsDao;
import com.yunnex.ops.erp.modules.statistics.dto.SplitDeliveryCycleDto;
import com.yunnex.ops.erp.modules.statistics.dto.SplitReportResponseDto;
import com.yunnex.ops.erp.modules.statistics.dto.SplitStatisticsAllResponseDto;
import com.yunnex.ops.erp.modules.statistics.dto.SplitStatisticsRequestDto;
import com.yunnex.ops.erp.modules.statistics.dto.SplitStatisticsResponseDto;
import com.yunnex.ops.erp.modules.statistics.dto.SplitTeamMemberReportResponseDto;
import com.yunnex.ops.erp.modules.statistics.dto.SplitWeekAndMonthResponseDto;
import com.yunnex.ops.erp.modules.statistics.entity.ErpStatistics;
import com.yunnex.ops.erp.modules.statistics.entity.ErpStatisticsApi;
import com.yunnex.ops.erp.modules.statistics.entity.SplitStatistics;
import com.yunnex.ops.erp.modules.sys.entity.Role;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.service.RoleService;
import com.yunnex.ops.erp.modules.sys.service.UserService;
import com.yunnex.ops.erp.modules.team.service.ErpTeamUserService;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowConstant;
import com.yunnex.ops.erp.modules.workflow.user.entity.ErpOrderFlowUser;
import com.yunnex.ops.erp.modules.workflow.user.service.ErpOrderFlowUserService;



/**
 * 统计Service
 * 
 * @author yunnex
 * @version 2017-12-15
 */
@Service
public class ErpStatisticsService extends BaseService {

    @Autowired
    private ErpOrderSplitGoodService erpOrderSplitGoodService;
    @Autowired
    private ErpOrderFlowUserService ofuService;
    @Autowired
    private ErpOrderSplitInfoService splitService;
    @Autowired
    private ActDao actDao;
    @Autowired
    private ErpTeamUserService erpTeamUserService;
    @Autowired
    private ErpStatisticsDao erpStatisticsDao;
    @Autowired
    private ActTaskService actTaskService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    /** 超时时间天数 */
    private static final int OVER_TIME_DAYS = 16;

    /**
     * 获取团队分单明细表
     *
     * @param principalId
     * @param teamId
     * @param orderNum
     * @param shopName
     * @param orderType
     * @param startDateStr
     * @param endDateStr
     * @return
     * @throws ParseException
     * @date 2018年4月11日
     * @author linqunzhi
     */
    public ErpStatisticsApi findTeamStatistics(String principalId, String teamId, String orderNum, String shopName, String orderType,
                    String startDateStr, String endDateStr) throws ParseException {
        logger.info("findTeamStatistics start | principalId={}|teamId={}|orderNum={}|shopName={}|orderType={}|startDateStr={}|endDateStr={}",
                        principalId, teamId, orderNum, shopName, orderType, startDateStr, endDateStr);
        List<ErpOrderFlowUser> eofuList = null;
        if ("1".equals(teamId)) {
            // 查询所有团队
            eofuList = ofuService.findstatistics(principalId, orderNum, shopName, orderType, startDateStr, endDateStr);
        } else {
            // 查询某个团队
            eofuList = ofuService.findstatisticswhereteamId(teamId, orderNum, shopName, orderType, startDateStr, endDateStr);
        }
        ErpStatisticsApi api = convertErpStatisticsApi(eofuList);
        logger.info("findTeamStatistics end");
        return api;

    }
    
    /**
     * 获取团队分单明细表
     *
     * @param principalId
     * @param teamId
     * @param orderNum
     * @param shopName
     * @param orderType
     * @param startDateStr
     * @param endDateStr
     * @return
     * @throws ParseException
     * @date 2018年4月11日
     * @author linqunzhi
     */
    public ErpStatisticsApi findTeamStatistics2(String principalId, String teamId, String orderNum, String shopName, String orderType,
                    String startDateStr, String endDateStr) throws ParseException {
        logger.info("findTeamStatistics start | principalId={}|teamId={}|orderNum={}|shopName={}|orderType={}|startDateStr={}|endDateStr={}",
                        principalId, teamId, orderNum, shopName, orderType, startDateStr, endDateStr);
        List<ErpOrderFlowUser> eofuList = null;
        if ("1".equals(teamId)) {
            // 查询所有团队
            eofuList = ofuService.findstatistics2(principalId, orderNum, shopName, orderType, startDateStr, endDateStr);
        } else {
            // 查询某个团队
            eofuList = ofuService.findstatisticswhereteamId2(teamId, orderNum, shopName, orderType, startDateStr, endDateStr);
        }
        ErpStatisticsApi api = convertErpStatisticsApi(eofuList);
        logger.info("findTeamStatistics end");
        return api;

    }

    /**
     * 获取团队分单明细表,针对执行中的任务存在超时的订单数
     *
     * @param principalId
     * @param teamId
     * @param orderNum
     * @param shopName
     * @param orderType
     * @param startDateStr
     * @param endDateStr
     * @return
     * @date 2018年4月11日
     * @author linqunzhi
     * @throws ParseException
     */
    public ErpStatisticsApi findOvertimeStatistics(String principalId, String teamId, String orderNum, String shopName, String orderType,
                    String startDateStr, String endDateStr) throws ParseException {
        logger.info("findOvertimeStatistics start | principalId={}|teamId={}|orderNum={}|shopName={}|orderType={}|startDateStr={}|endDateStr={}",
                        principalId, teamId, orderNum, shopName, orderType, startDateStr, endDateStr);
        List<ErpOrderFlowUser> eofuList = null;
        if ("1".equals(teamId)) {
            // 查询所有团队
            eofuList = ofuService.findoverTimestatistics(principalId, orderNum, shopName, orderType, startDateStr, endDateStr);
        } else {
            // 查询某个团队
            eofuList = ofuService.findoverTimewhereteamId(teamId, orderNum, shopName, orderType, startDateStr, endDateStr);
        }
        ErpStatisticsApi api = convertErpStatisticsApi(eofuList);
        logger.info("findOvertimeStatistics end");
        return api;
    }

    /**
     * 获取个人分单明细表
     *
     * @param principalId
     * @param orderNum
     * @param shopName
     * @param orderType
     * @param startDateStr
     * @param endDateStr
     * @return
     * @throws ParseException
     * @date 2018年4月11日
     * @author linqunzhi
     */
    public ErpStatisticsApi findUserStatistics(String principalId, String orderNum, String shopName, String orderType, String startDateStr,
                    String endDateStr) throws ParseException {
        logger.info("findUserStatistics start | principalId={}|orderNum={}|shopName={}|orderType={}|startDateStr={}|endDateStr={}", principalId,
                        orderNum, shopName, orderType, startDateStr, endDateStr);
        List<ErpOrderFlowUser> eofuList = ofuService.findstatisticswhereuserId(principalId, orderNum, shopName, orderType, startDateStr, endDateStr);
        ErpStatisticsApi api = convertErpStatisticsApi(eofuList);
        logger.info("findUserStatistics end");
        return api;

    }

    /**
     * 根据工作流人员关系 转换 分单明细列表数据
     *
     * @param erpOrderFlowUserList
     * @return
     * @date 2018年4月11日
     * @author linqunzhi
     */
    private ErpStatisticsApi convertErpStatisticsApi(List<ErpOrderFlowUser> eofuList) throws ParseException {
        List<ErpStatistics> statislist = new ArrayList<ErpStatistics>();
        ErpStatisticsApi api = new ErpStatisticsApi();
        ErpStatistics statis = null;
        Integer orderCount = 0;// 判断是否有多条购买为复购单
        Integer newCount = 0;
        Integer onlineCount = 0;
        Integer createCount = 0;
        newCount = eofuList == null ? 0 : eofuList.size();
        if (newCount > 0 && eofuList != null) {
            for (int i = 0; i < newCount; i++) {
                ErpOrderFlowUser eofu = eofuList.get(i);
                String time = "";
                statis = new ErpStatistics();
                statis.setOrderNum(
                                eofu.getOrderNum() + (FlowConstant.STRING_0
                                                .equalsIgnoreCase(eofu.getSplitIds()) ? StringUtils.EMPTY : ("-" + eofu.getSplitIds())));
                statis.setBuyDate(eofu.getPayDate());
                statis.setOrderCategory(eofu.getOrderType());
                statis.setShopName(eofu.getShopName());
                statis.setServiceAndNum(erpOrderSplitGoodService.getServiceAndNum(eofu.getSid()));
                statis.setAccessDate(eofu.getCreateDates());
                statis.setTimeoutFlag(eofu.getTimeoutFlag());
                statis.setTaskStatus(eofu.getTaskStatus());
                // 项目异常原因
                if ("N".equals(eofu.getPendingProduced()) && StringUtils.isNotBlank(eofu.getPendingReason())) {
                    if (StringUtils.isNotBlank(eofu.getPendingReason()) && "Q".equals(eofu.getPendingReason())) {
                        statis.setProjectAnomaly("有存在过资质问题");
                    }

                    if (StringUtils.isNotBlank(eofu.getPendingReason()) && "D".equals(eofu.getPendingReason())) {
                        statis.setProjectAnomaly("有过商户主动延迟上线");
                    }
                } else {
                    statis.setProjectAnomaly("/");
                }
                // 上线时间
                if (null != eofu.getOnlineDate()) {
                    statis.setOnlineDate(DateUtils.formatDateTime(eofu.getOnlineDate()));
                }
                if (null != eofu.getManualDate()) {
                    statis.setManualDate(DateUtils.formatDateTime(eofu.getManualDate()));
                }
                if (null == eofu.getRemarks()) {
                    statis.setRemarks("-");
                } else {
                    statis.setRemarks(eofu.getRemarks());
                }

                statis.setAgent(eofu.getAgentName());
                statis.setPlanningExpert(eofu.getPlanningExpert());
                statis.setOperationAdviser(eofu.getOperationAdviser());
                if ("1".equals(eofu.getOsStatuss())) {
                    statis.setOrderState("已完成");
                    createCount = createCount + 1;
                } else if (!"1".equals(eofu.getOsStatuss()) && "Y".equals(eofu.getPendingProduced())) {
                    if (StringUtils.isNotBlank(eofu.getPendingReason()) && "Q".equals(eofu.getPendingReason())) {
                        statis.setOrderState("存在资质问题");
                        statis.setProjectAnomaly("有存在过资质问题");
                    }

                    if (StringUtils.isNotBlank(eofu.getPendingReason()) && "D".equals(eofu.getPendingReason())) {
                        statis.setOrderState("商户主动延迟上线");
                        statis.setProjectAnomaly("有过商户主动延迟上线");
                    }
                } else if (!"1".equals(eofu.getOsStatuss()) && null != eofu.getOnlineDate()) {
                    statis.setOrderState("投放中");
                } else {
                    if (!"1".equals(eofu.getOsStatuss()) && "N".equals(eofu.getPendingProduced())) {
                        statis.setOrderState("生产中");
                    } else {
                        statis.setOrderState("/");
                    }
                }
                // 交付周期
                if (null != eofu.getOnlineDate()) {
                    onlineCount = onlineCount + 1;
                    time = DateUtils.formatDateTime(eofu.getOnlineDate());
                    statis.setDeliveryCycle(jfzq(time, eofu.getPayDate()).toString());
                } else {
                    if (null != eofu.getOsStatuss() && "1".equals(eofu.getOsStatuss())) {
                        onlineCount = onlineCount + 1;
                        statis.setDeliveryCycle("/");
                    } else {
                        statis.setDeliveryCycle("未交付");
                    }
                }
                statis.setExtensionChannel(eofu.getPromotionChannel());
                statis.setFriendDate(eofu.getFriendsDate());
                statis.setMomoDate(eofu.getMomoDate());
                statis.setWeiboDate(eofu.getWeiboDate());
                statis.setCreatePresentation(eofu.getCreatePresentation());
                statis.setPid(eofu.getPid());
                statis.setSid(eofu.getSid());
                orderCount = splitService.WhereShopIdCount(eofu.getShopid(), eofu.getCreateDates(), Global.NO);
                if (orderCount > 0) {
                    if ("1".equals(eofu.getOrderSource())) {
                        statis.setNature("自建单,复购单");
                    } else {
                        statis.setNature("复购单");
                    }
                } else if ("1".equals(eofu.getHurryFlag())) {
                    if ("1".equals(eofu.getOrderSource())) {
                        statis.setNature("急单,自建单");
                    } else {
                        statis.setNature("急单");
                    }
                } else if ("0".equals(eofu.getHurryFlag())) {
                    if ("1".equals(eofu.getOrderSource())) {
                        statis.setNature("自建单");
                    } else {
                        statis.setNature("其它");
                    }
                } else {
                    statis.setNature("其它");
                }
                statis.setCommentCount(eofu.getCommentCount());

                /************** add by SunQ 2018-2-26 12:20:25 start **************/
                List<String> taskNames = actDao.findTaskNameByProcInsId(eofu.getPid());
                statis.setCurrentTasks(taskNames);
                /************** add by SunQ 2018-2-26 12:20:25 end **************/
                statislist.add(statis);
            }
        }
        api.setNewCount(newCount);
        api.setOnlineCount(onlineCount);
        api.setCreateCount(createCount);
        api.getStatistics().addAll(statislist);
        return api;
    }

    /** 计算2个日期排除 周六 周日的相差天数 */

    public Integer jfzq(String maxdate, String buydate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = format.parse(buydate);
        Date endDate = format.parse(maxdate);

        int result = 0;
        while (startDate.compareTo(endDate) <= 0) {
            if (startDate.getDay() != 6 && startDate.getDay() != 0) {
                result++;
                startDate.setDate(startDate.getDate() + 1);
            } else {
                startDate.setDate(startDate.getDate() + 1);
            }
        }
        return result;
    }

    /**
     * 获取团队分单明细列表
     *
     * @param page
     * @param dto
     * @param principalId 当前登录用户id
     * @return
     * @date 2018年5月11日
     * @author linqunzhi
     */
    public SplitStatisticsAllResponseDto findTeamSplitStatistics(Page<SplitStatisticsRequestDto> page, SplitStatisticsRequestDto dto,
                    String principalId) {
        String dtoStr = JSON.toJSONString(dto);
        logger.info("findSplitStatistics start |principalId={}|dto={}", principalId, dtoStr);
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
        List<String> userIdList = calculateUserIdList(dto.getUserIdList(), dto.getTeamId(), principalId);
        dto.setUserIdList(userIdList);
        SplitStatisticsAllResponseDto result = getSplitStatisticsResponseDto(page, dto);
        logger.info("findSplitStatistics end | result.Count={}", result.getCount());
        return result;
    }

    /**
     * 获取个人分单明细列表
     *
     * @param page
     * @param dto
     * @param principalId 当前登录用户id
     * @return
     * @date 2018年5月9日
     * @author linqunzhi
     */
    public SplitStatisticsAllResponseDto findUserSplitStatistics(Page<SplitStatisticsRequestDto> page, SplitStatisticsRequestDto dto,
                    String principalId) {
        String dtoStr = JSON.toJSONString(dto);
        logger.info("findUserSplitStatistics start | dto={}", dtoStr);
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
        SplitStatisticsAllResponseDto result = getSplitStatisticsResponseDto(page, dto);
        logger.info("findUserSplitStatistics end | result.Count={}", result.getCount());
        return result;
    }

    /**
     * 获取分单列表数据
     *
     * @param page
     * @param dto
     * @return
     * @date 2018年5月15日
     * @author linqunzhi
     */
    private SplitStatisticsAllResponseDto getSplitStatisticsResponseDto(Page<SplitStatisticsRequestDto> page, SplitStatisticsRequestDto dto) {
        SplitStatisticsAllResponseDto result = new SplitStatisticsAllResponseDto();
        Page<SplitStatisticsResponseDto> pageResult = findSplitStatistics(page, dto);
        result.setCount(pageResult.getCount());
        result.setList(pageResult.getList());
        SplitStatisticsRequestDto countDto = new SplitStatisticsRequestDto(dto.getStartDateStr(), dto.getEndDateStr(), dto.getUserIdList());
        // 新接入分单数量
        countDto.setSplitType(SplitStatisticsConstants.SPLIT_TYPE_NEW);
        result.setNewCount(this.countBySplitStatisticsRequestDto(countDto));
        // 上线分单数量
        countDto.setSplitType(SplitStatisticsConstants.SPLIT_TYPE_ONLINE);
        result.setOnlineCount(this.countBySplitStatisticsRequestDto(countDto));
        // 完成分单数量
        countDto.setSplitType(SplitStatisticsConstants.SPLIT_TYPE_FINISH);
        result.setFinishCount(this.countBySplitStatisticsRequestDto(countDto));
        return result;
    }

    /**
     * 获取分页分单明细列表
     * 
     * @param dto
     * @return
     * @date 2018年5月9日
     * @author linqunzhi
     * @param page
     */
    private Page<SplitStatisticsResponseDto> findSplitStatistics(Page<SplitStatisticsRequestDto> page, SplitStatisticsRequestDto dto) {
        Page<SplitStatisticsResponseDto> result = new Page<>();
        if (dto == null) {
            return result;
        }
        // 计算 是否需要对查询出来的列进行过滤 Y：是 N：否
        dto.setQueryColumnsFlag(calculateQueryColumnsFlag(dto));
        dto.setPage(page);
        List<SplitStatistics> list = erpStatisticsDao.findSplitStatistics(dto);
        if (CollectionUtils.isEmpty(list)) {
            return result;
        }
        long count = list.size();
        // 获取总条数
        if (page != null) {
            count = page.getCount();
        }
        result.setCount(count);
        SplitStatisticsResponseDto responseDto = null;
        // 循环list 获取 responseDto 集合
        if (CollectionUtils.isNotEmpty(list)) {
            List<SplitStatisticsResponseDto> responseList = new ArrayList<>();
            // 将集合放入 result 中
            result.setList(responseList);
            for (SplitStatistics splitStatistics : list) {
                responseDto = new SplitStatisticsResponseDto();
                // 订单号
                responseDto.setOrderNum(new StringBuilder(splitStatistics.getOrderNumber()).append(CommonConstants.Sign.DASH)
                                .append(splitStatistics.getSplitId()).toString());
                // 购买时间
                responseDto.setBuyDate(DateUtils.formatDate(splitStatistics.getBuyDate(), DateUtils.YYYY_MM_DD));
                // 订单类别名称
                responseDto.setOrderTypeName(splitStatistics.getOrderType() != null && splitStatistics
                                .getOrderType() == OrderConstants.TYPE_DIRECT ? "直销" : "服务商");
                // 商户名称
                responseDto.setShopName(splitStatistics.getShopName());
                // 已购服务
                responseDto.setServiceAndNums(erpOrderSplitGoodService.getServiceAndNum(splitStatistics.getId()));
                // 接入时间
                responseDto.setCreateDate(DateUtils.formatDate(splitStatistics.getCreateDate(), DateUtils.YYYY_MM_DD));
                // 备注
                responseDto.setRemarks(splitStatistics.getRemarks());
                // 服务商
                responseDto.setAgentName(splitStatistics.getAgentName());
                // 策划专家
                responseDto.setPlanningExpertName(splitStatistics.getPlanningExpertName());
                // 运营顾问
                responseDto.setOperationAdviserName(splitStatistics.getOperationAdviserName());
                // 订单状态
                responseDto.setSplitStatusName(convertSplitStatusName(splitStatistics.getSplitStatus()));
                // 交付周期
                responseDto.setDeliveryCycle(convertDeliveryCycle(splitStatistics.getDeliveryCycle()));
                // 推广通道
                responseDto.setExtensionChannel(splitStatistics.getExtensionChannelNames());
                // 朋友圈上线时间
                responseDto.setFriendOnlineDate(DateUtils.formatDate(splitStatistics.getFriendOnlineDate(), DateUtils.YYYY_MM_DD));
                // 陌陌上线时间
                responseDto.setMomoOnlineDate(DateUtils.formatDate(splitStatistics.getMomoOnlineDate(), DateUtils.YYYY_MM_DD));
                // 微博上线时间
                responseDto.setWeiboOnlineDate(DateUtils.formatDate(splitStatistics.getWeiboOnlineDate(), DateUtils.YYYY_MM_DD));
                // 完成效果报告时间
                responseDto.setPresentationDate(DateUtils.formatDateTime(splitStatistics.getPresentationDate()));
                // 工单性质
                responseDto.setSplitNatureNames(convertSplitNatureNames(splitStatistics.getSplitNatures()));
                // 是否有超时风险
                responseDto.setTimeoutFlag(CommonConstants.Sign.YES.equals(splitStatistics.getTimeoutFlag()) ? "是" : "否");
                // 执行中的任务是否存在超时
                responseDto.setTaskTimeoutFlag((CommonConstants.Sign.YES.equals(splitStatistics.getTaskTimeoutFlag())) ? "存在超时" : "不存在超时");
                // 订单上线时间
                responseDto.setOnlineDate(DateUtils.formatDate(splitStatistics.getOnlineDate(), DateUtils.YYYY_MM_DD));
                // 手动标记完成时间
                responseDto.setManualDate(DateUtils.formatDateTime(splitStatistics.getManualDate()));
                // 项目异常原因
                responseDto.setProjectAnomaly(convertProjectAnomaly(splitStatistics.getPendingReason()));
                // 分单id
                responseDto.setId(splitStatistics.getId());
                // 评论数
                responseDto.setCommentCount(splitStatistics.getCommentCount());
                // 是否为标记完成订单
                responseDto.setManualFinishFlag(CommonConstants.Sign.YES.equals(splitStatistics.getManualFinishFlag()) ? "是" : "否");
                // 流程id
                responseDto.setProcessId(splitStatistics.getProcessId());
                // 当前任务
                responseDto.setTaskNames(convertTaskNames(splitStatistics.getProcessId()));
                // 流程版本号
                responseDto.setProcessVersionName(OrderSplitUtil.convertProcessVersionName(splitStatistics.getProcessVersion()));

                responseList.add(responseDto);

            }
        }
        return result;
    }

    /**
     * 计算 是否需要对查询出来的列进行过滤 Y：是 N：否
     *
     * @param queryColumnsFlag
     * @param dto
     * @return
     * @date 2018年5月10日
     * @author linqunzhi
     */
    private String calculateQueryColumnsFlag(SplitStatisticsRequestDto dto) {
        if (dto == null) {
            return CommonConstants.Sign.NO;
        }
        // 如果主动设置了值就不进行计算
        if (StringUtils.isNotBlank(dto.getQueryColumnsFlag())) {
            return dto.getQueryColumnsFlag();
        }
        String resultYes = CommonConstants.Sign.YES;
        if (CollectionUtils.isNotEmpty(dto.getDeliveryCycleList())) {
            return resultYes;
        }
        if (CollectionUtils.isNotEmpty(dto.getProjectAnomalyList())) {
            return resultYes;
        }
        if (CollectionUtils.isNotEmpty(dto.getSplitStatusList())) {
            return resultYes;
        }
        if (CollectionUtils.isNotEmpty(dto.getSplitNatureList())) {
            return resultYes;
        }
        if (StringUtils.isNotBlank(dto.getTaskTimeoutFlag())) {
            return resultYes;
        }
        return CommonConstants.Sign.NO;
    }

    /**
     * 转换当前任务名称
     *
     * @param processId
     * @return
     * @date 2018年5月9日
     * @author linqunzhi
     */
    private String convertTaskNames(String processId) {
        String result = null;
        List<String> taskNameList = actTaskService.findTaskNameByProcInsId(processId);
        if (CollectionUtils.isNotEmpty(taskNameList)) {
            StringBuilder builder = new StringBuilder();
            for (String str : taskNameList) {
                builder.append(CommonConstants.Sign.COMMA).append(str);
            }
            result = builder.toString();
            if (result.length() > 0) {
                result = result.substring(1);
            }
        }
        return result;
    }

    /**
     * 转换项目异常原因
     *
     * @param pendingReason
     * @return
     * @date 2018年5月9日
     * @author linqunzhi
     */
    private String convertProjectAnomaly(String pendingReason) {
        if ("Q".equals(pendingReason)) {
            return "有存在过资质问题";
        }
        if ("D".equals(pendingReason)) {
            return "有过商户主动延迟上线";
        }
        return null;
    }

    /**
     * 转换工单性质
     *
     * @param splitNature
     * @return
     * @date 2018年5月9日
     * @author linqunzhi
     */
    private String convertSplitNatureNames(String splitNatures) {
        if (StringUtils.isBlank(splitNatures)) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        if (splitNatures.contains("urgency")) {
            builder.append(",急单");
        }
        if (splitNatures.contains("repeat")) {
            builder.append(",复购单");
        }
        if (splitNatures.contains("self")) {
            builder.append(",自建单");
        }
        String result = builder.toString();
        if (result.length() > 0) {
            result = result.substring(1);
        }
        return result;

    }

    /**
     * 转换交付周期
     *
     * @param deliveryCycle
     * @return
     * @date 2018年5月9日
     * @author linqunzhi
     */
    private String convertDeliveryCycle(Integer deliveryCycle) {
        if (deliveryCycle == null || deliveryCycle == -1) {
            return null;
        }
        if (deliveryCycle == -2) {
            return "未交付";
        }
        return deliveryCycle.toString();
    }

    /**
     * 根据订单状态 转换订单状态名称
     *
     * @param splitStatus
     * @return
     * @date 2018年5月9日
     * @author linqunzhi
     */
    private String convertSplitStatusName(String splitStatus) {
        if ("productionBegin".equals(splitStatus)) {
            return "生产中";
        }
        if ("circulationBegin".equals(splitStatus)) {
            return "投放中";
        }
        if ("finish".equals(splitStatus)) {
            return "已完成";
        }
        if ("qualificationProblem".equals(splitStatus)) {
            return "存在资质问题";
        }
        if ("delayLaunch".equals(splitStatus)) {
            return "商户主动延迟上线";
        }
        if ("refunding".equals(splitStatus)) {
            return "退款中";
        }
        return null;
    }

    /**
     * 计算查询的成员id集合
     *
     * @param userIdList 前端参数成员id集合
     * @param teamId 团队id
     * @param principalId 当前登录用户id
     * @return
     * @date 2018年5月9日
     * @author linqunzhi
     */
    public List<String> calculateUserIdList(List<String> userIdList, String teamId, String principalId) {
        List<String> result = userIdList;
        if (CollectionUtils.isEmpty(result)) {
            if (StringUtils.isNotBlank(teamId)) {
                result = erpTeamUserService.findUserIdsByTeamId(teamId, CommonConstants.Sign.NUMBER_NO);
            } else {
                result = erpTeamUserService.findUserIdsByUserId(principalId, CommonConstants.Sign.NUMBER_NO);
            }
        }
        return result;
    }

    /**
     * 根据dto 查询数量
     *
     * @param dto
     * @return
     * @date 2018年5月15日
     * @author linqunzhi
     */
    private int countBySplitStatisticsRequestDto(SplitStatisticsRequestDto dto) {
        if (dto != null) {
            dto.setQueryColumnsFlag(calculateQueryColumnsFlag(dto));
        }
        return erpStatisticsDao.countBySplitStatisticsRequestDto(dto);
    }

    /**
     * 获取团队订单统计
     *
     * @param principalId
     * @param teamId
     * @return
     * @date 2018年5月14日
     * @author linqunzhi
     */
    public SplitReportResponseDto getTeamSplitReport(String principalId, String teamId) {
        logger.info("getTeamReport start | principalId={}|teamId={}", principalId, teamId);
        String startDateStr = SplitStatisticsConstants.DEFAULT_START_DATE_STR;
        String endDateStr = DateUtils.formatDateTime(new Date());
        List<String> userIdList = calculateUserIdList(null, teamId, principalId);
        SplitReportResponseDto result = new SplitReportResponseDto();
        // 累计总接单数
        SplitStatisticsRequestDto splitDto = new SplitStatisticsRequestDto(startDateStr, endDateStr, userIdList);
        result.setAllCount(this.countBySplitStatisticsRequestDto(splitDto));

        // 当前跟进订单总数 (订单状态：非完成订单)
        result.setFollowCount(getCountFollowSplit(startDateStr, endDateStr, userIdList));

        // 当前有任务正在处理的跟进订单数 (订单状态：投放中、生产中)
        result.setHandleCount(getCountTaskSplit(startDateStr, endDateStr, userIdList));

        // 正常跟进订单数(订单状态：投放中、生产中，项目异常原因：/)
        splitDto = new SplitStatisticsRequestDto(startDateStr, endDateStr, userIdList);
        List<String> splitStatusList = new ArrayList<>();
        splitStatusList.add(SplitStatisticsConstants.SPLIT_STATUS_CIRCULATION_BEGIN);
        splitStatusList.add(SplitStatisticsConstants.SPLIT_STATUS_PRODUCTION_BEGIN);
        splitDto.setSplitStatusList(splitStatusList);
        List<String> projectAnomalyList = new ArrayList<>();
        projectAnomalyList.add(CommonConstants.Sign.FORWARD_SLASH);
        splitDto.setProjectAnomalyList(projectAnomalyList);
        result.setNormalCount(this.countBySplitStatisticsRequestDto(splitDto));

        // 当前存在资质问题的跟进订单数(订单状态：存在资质问题)
        splitDto = new SplitStatisticsRequestDto(startDateStr, endDateStr, userIdList);
        splitStatusList = new ArrayList<>();
        splitStatusList.add(SplitStatisticsConstants.SPLIT_STATUS_QUALIFICATION_PROBLEM);
        splitDto.setSplitStatusList(splitStatusList);
        result.setQualificationsCount(this.countBySplitStatisticsRequestDto(splitDto));

        // 存在过资质问题的跟进订单数(订单状态：非完成订单，项目异常原因：存在过资质问题)
        splitDto = new SplitStatisticsRequestDto(startDateStr, endDateStr, userIdList);
        splitDto.setSplitStatusList(getNotFinshList());
        projectAnomalyList = new ArrayList<>();
        projectAnomalyList.add(OrderSplitConstants.PENDING_REASON_QUALIFICATION);
        splitDto.setProjectAnomalyList(projectAnomalyList);
        result.setAllQualificationsCount(this.countBySplitStatisticsRequestDto(splitDto));

        // 商户主动要求延迟上线的跟进订单数(订单状态：商户主动延迟上线)
        splitDto = new SplitStatisticsRequestDto(startDateStr, endDateStr, userIdList);
        splitStatusList = new ArrayList<>();
        splitStatusList.add(SplitStatisticsConstants.SPLIT_STATUS_DELAY_LAUNCH);
        splitDto.setSplitStatusList(splitStatusList);
        result.setActiveDelayCount(this.countBySplitStatisticsRequestDto(splitDto));

        // 商户曾经主动要求延迟上线的跟进订单数(订单状态：非完成订单，项目异常原因：商户主动要求延迟)
        splitDto = new SplitStatisticsRequestDto(startDateStr, endDateStr, userIdList);
        splitDto.setSplitStatusList(getNotFinshList());
        projectAnomalyList = new ArrayList<>();
        projectAnomalyList.add(OrderSplitConstants.PENDING_REASON_DELAY);
        splitDto.setProjectAnomalyList(projectAnomalyList);
        result.setAllActiveDelayCount(this.countBySplitStatisticsRequestDto(splitDto));

        // 有超时风险的订单数(订单状态：非完成订单，是否有超时风险:是)
        splitDto = new SplitStatisticsRequestDto(startDateStr, endDateStr, userIdList);
        splitDto.setSplitStatusList(getNotFinshList());
        splitDto.setTimeoutFlag(CommonConstants.Sign.YES);
        result.setRiskCount(this.countBySplitStatisticsRequestDto(splitDto));

        // 执行中的任务存在超时的订单数(订单状态：非完成订单，执行中的任务是否存在超时:是)
        splitDto = new SplitStatisticsRequestDto(startDateStr, endDateStr, userIdList);
        splitDto.setSplitStatusList(getNotFinshList());
        splitDto.setTaskTimeoutFlag(CommonConstants.Sign.YES);
        result.setOverTimeCount(this.countBySplitStatisticsRequestDto(splitDto));

        logger.info("getTeamReport end");
        return result;
    }

    /**
     * 获取团队分单周/月统计数据
     *
     * @param principalId
     * @param teamId
     * @param startDateStr
     * @param endDateStr
     * @return
     * @date 2018年5月15日
     * @author linqunzhi
     */
    public SplitWeekAndMonthResponseDto getTeamSplitWeekAndMonth(String principalId, String teamId, String startDateStr, String endDateStr) {
        logger.info("getTeamSplitWeekAndMonth start | principalId={}|teamId={}|startDateStr={}|endDateStr={}", principalId, teamId, startDateStr,
                        endDateStr);
        List<String> userIdList = calculateUserIdList(null, teamId, principalId);
        SplitWeekAndMonthResponseDto result = new SplitWeekAndMonthResponseDto();

        // 新接入分单数量
        SplitStatisticsRequestDto countDto = new SplitStatisticsRequestDto(startDateStr, endDateStr, userIdList);
        countDto.setSplitType(SplitStatisticsConstants.SPLIT_TYPE_NEW);
        result.setNewCount(this.countBySplitStatisticsRequestDto(countDto));

        // 上线分单数量
        countDto = new SplitStatisticsRequestDto(startDateStr, endDateStr, userIdList);
        countDto.setSplitType(SplitStatisticsConstants.SPLIT_TYPE_ONLINE);
        result.setOnlineCount(this.countBySplitStatisticsRequestDto(countDto));

        // 上线分单中的超时分单数(项目异常原因：/，交付周期 >15)
        countDto = new SplitStatisticsRequestDto(startDateStr, endDateStr, userIdList);
        countDto.setSplitType(SplitStatisticsConstants.SPLIT_TYPE_ONLINE);
        List<String> projectAnomalyList = new ArrayList<>();// 项目异常原因
        projectAnomalyList.add(CommonConstants.Sign.FORWARD_SLASH);
        countDto.setProjectAnomalyList(projectAnomalyList);
        List<SplitDeliveryCycleDto> deliveryCycleList = new ArrayList<>();// 交付周期
        SplitDeliveryCycleDto deliveryCycle = new SplitDeliveryCycleDto();
        deliveryCycle.setMin(OVER_TIME_DAYS);
        deliveryCycleList.add(deliveryCycle);
        countDto.setDeliveryCycleList(deliveryCycleList);
        result.setOnlineCountOvertime(this.countBySplitStatisticsRequestDto(countDto));

        // 上线订单平均周期(项目异常原因：/)
        countDto = new SplitStatisticsRequestDto(startDateStr, endDateStr, userIdList);
        countDto.setSplitType(SplitStatisticsConstants.SPLIT_TYPE_ONLINE);
        projectAnomalyList = new ArrayList<>();// 项目异常原因
        projectAnomalyList.add(CommonConstants.Sign.FORWARD_SLASH);
        countDto.setProjectAnomalyList(projectAnomalyList);
        double avgCycle = erpStatisticsDao.getOnlineAvgCycle(countDto);
        String avgCycleStr = new StringBuilder().append(avgCycle).append("个工作日").toString();
        result.setAvgCycle(avgCycleStr);
        String resultStr = JSON.toJSONString(result);
        logger.info("getTeamSplitWeekAndMonth end | result={}", resultStr);
        return result;
    }

    /**
     * 团队成员跟进订单统计
     *
     * @param principalId
     * @param teamId
     * @return
     * @date 2018年5月15日
     * @author linqunzhi
     */
    public List<SplitTeamMemberReportResponseDto> findTeamMemberSplitReport(String principalId, String teamId) {
        logger.info("findTeamMemberSplitReport start | principalId={}|teamId={}", principalId, teamId);
        List<String> userIdList = calculateUserIdList(null, teamId, principalId);
        List<SplitTeamMemberReportResponseDto> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(userIdList)) {
            SplitTeamMemberReportResponseDto responseDto = null;
            String roleNames = null;
            User user = null;
            StringBuilder builder = null;
            String startDateStr = SplitStatisticsConstants.DEFAULT_START_DATE_STR;
            String endDateStr = DateUtils.formatDateTime(new Date());
            List<String> userIds = null;
        for (String userId : userIdList) {
                responseDto = new SplitTeamMemberReportResponseDto();
                responseDto.setUserId(userId);
                // 获取用户信息
                user = userService.get(userId);
                if (user != null) {
                    responseDto.setUserName(user.getName());
                }
                // 获取用户所有角色
                List<Role> roleList = roleService.findByUserId(userId);
                if (CollectionUtils.isNotEmpty(roleList)) {
                    builder = new StringBuilder();
                    for (Role role : roleList) {
                        builder.append(CommonConstants.Sign.COMMA).append(role.getName());
                    }
                    roleNames = builder.toString();
                    if (roleNames.length() > 0) {
                        roleNames = roleNames.substring(1);
                    }
                    // 设置角色名称
                    responseDto.setUserRole(roleNames);
                }
                userIds = new ArrayList<>();
                userIds.add(userId);
                // 当前跟进订单数
                responseDto.setFollowOrder(getCountFollowSplit(startDateStr, endDateStr, userIds));
                // 有任务正在处理的订单数
                responseDto.setTaskOrder(getCountTaskSplit(startDateStr, endDateStr, userIds));

                result.add(responseDto);
            }
        }
        logger.info("findTeamMemberSplitReport end | result.size={}", result.size());
        return result;
    }

    /**
     * 获取当前跟进分单数（分单状态：非完成分单）
     *
     * @param startDateStr
     * @param endDateStr
     * @param userIdList
     * @return
     * @date 2018年5月17日
     * @author linqunzhi
     */
    private int getCountFollowSplit(String startDateStr, String endDateStr, List<String> userIdList) {
        SplitStatisticsRequestDto countDto = new SplitStatisticsRequestDto(startDateStr, endDateStr, userIdList);
        countDto.setSplitStatusList(getNotFinshList());
        return countBySplitStatisticsRequestDto(countDto);
    }

    /**
     * 获取有任务正在处理的分单数（分单状态：投放中、生产中）
     *
     * @param startDateStr
     * @param endDateStr
     * @param userIdList
     * @return
     * @date 2018年5月17日
     * @author linqunzhi
     */
    private int getCountTaskSplit(String startDateStr, String endDateStr, List<String> userIdList) {
        SplitStatisticsRequestDto countDto = new SplitStatisticsRequestDto(startDateStr, endDateStr, userIdList);
        List<String> splitStatusList = new ArrayList<>();
        splitStatusList.add(SplitStatisticsConstants.SPLIT_STATUS_CIRCULATION_BEGIN);
        splitStatusList.add(SplitStatisticsConstants.SPLIT_STATUS_PRODUCTION_BEGIN);
        countDto.setSplitStatusList(splitStatusList);
        return countBySplitStatisticsRequestDto(countDto);
    }

    /**
     * 获取非完成分单状态集合
     *
     * @return
     * @date 2018年5月17日
     * @author linqunzhi
     */
    private static List<String> getNotFinshList() {
        List<String> splitStatusList = new ArrayList<>();
        splitStatusList.add(SplitStatisticsConstants.SPLIT_STATUS_CIRCULATION_BEGIN);
        splitStatusList.add(SplitStatisticsConstants.SPLIT_STATUS_DELAY_LAUNCH);
        splitStatusList.add(SplitStatisticsConstants.SPLIT_STATUS_PRODUCTION_BEGIN);
        splitStatusList.add(SplitStatisticsConstants.SPLIT_STATUS_QUALIFICATION_PROBLEM);
        splitStatusList.add(SplitStatisticsConstants.SPLIT_STATUS_DELAY_REFUNDING);
        return splitStatusList;
    }

}
