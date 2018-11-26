package com.yunnex.junit.statistics;

import com.alibaba.fastjson.JSON;
import com.yunnex.junit.BaseTest;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.modules.statistics.dto.SplitDeliveryCycleDto;
import com.yunnex.ops.erp.modules.statistics.dto.SplitReportResponseDto;
import com.yunnex.ops.erp.modules.statistics.dto.SplitStatisticsAllResponseDto;
import com.yunnex.ops.erp.modules.statistics.dto.SplitStatisticsRequestDto;
import com.yunnex.ops.erp.modules.statistics.dto.SplitTeamMemberReportResponseDto;
import com.yunnex.ops.erp.modules.statistics.dto.SplitWeekAndMonthResponseDto;
import com.yunnex.ops.erp.modules.statistics.service.ErpStatisticsService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 分单统计相关测试
 *
 * @author linqunzhi
 * @date 2018年5月11日
 */
public class ErpSplitStatisticsTest extends BaseTest {

    @Autowired
    private ErpStatisticsService erpStatisticsService;

    /**
     * 查询团队分单信息列表
     *
     * @date 2018年5月14日
     * @author linqunzhi
     */
    @Test
    public void findTeamSplitStatistics() {
        String principalId = "1";// 系统管理员
        Page<SplitStatisticsRequestDto> page = new Page<>(1, 10);
        SplitStatisticsRequestDto dto = new SplitStatisticsRequestDto();
        // 团队id
        // dto.setTeamId("db1b8bf080254f6aad63aafba81d0ea3");

        dto.setStartDateStr("2017-11-20");
        dto.setEndDateStr("2018-08-08");

        // 交付周期
        List<SplitDeliveryCycleDto> deliveryCycleList = new ArrayList<>();
        String jsonStr = "{'min':'-2','max':''}";
        SplitDeliveryCycleDto deliveryCycleDto1 = JSON.parseObject(jsonStr, SplitDeliveryCycleDto.class);
        jsonStr = "{'min':'-2','max':'15'}";
        SplitDeliveryCycleDto deliveryCycleDto2 = JSON.parseObject(jsonStr, SplitDeliveryCycleDto.class);
        deliveryCycleList.add(deliveryCycleDto1);
        deliveryCycleList.add(deliveryCycleDto2);
        dto.setDeliveryCycleList(deliveryCycleList);
        // 工单性质
        List<String> splitNatureList = new ArrayList<>();
        splitNatureList.add("urgency");
        dto.setSplitNatureList(splitNatureList);
        // 项目异常原因
        List<String> projectAnomalyList = new ArrayList<>();
        projectAnomalyList.add("Q");
        dto.setProjectAnomalyList(projectAnomalyList);
        // 流程版本号
        List<String> processVersionList = new ArrayList<>();
        processVersionList.add("301");
        dto.setProcessVersionList(processVersionList);
        // 任务中是否存在超时
        dto.setTaskTimeoutFlag("Y");
        // 商品id
        List<String> goodIdList = new ArrayList<>();
        goodIdList.add("37");
        goodIdList.add("38");
        dto.setGoodIdList(goodIdList);

        SplitStatisticsAllResponseDto result = erpStatisticsService.findTeamSplitStatistics(page, dto, principalId);
        System.err.println(JSON.toJSONString(result));
    }


    /**
     * 查询个人分单信息列表
     *
     * @date 2018年5月14日
     * @author linqunzhi
     */
    @Test
    public void findUserSplitStatistics() {
        String principalId = "1";// 系统管理员
        Page<SplitStatisticsRequestDto> page = new Page<>(1, 10);
        SplitStatisticsRequestDto dto = new SplitStatisticsRequestDto();
        dto.setStartDateStr("2017-11-20");
        dto.setEndDateStr("2018-08-08");
        // 交付周期
        List<SplitDeliveryCycleDto> deliveryCycleList = new ArrayList<>();
        String jsonStr = "{'min':'-2','max':''}";
        SplitDeliveryCycleDto deliveryCycleDto1 = JSON.parseObject(jsonStr, SplitDeliveryCycleDto.class);
        jsonStr = "{'min':'-2','max':'15'}";
        SplitDeliveryCycleDto deliveryCycleDto2 = JSON.parseObject(jsonStr, SplitDeliveryCycleDto.class);
        deliveryCycleList.add(deliveryCycleDto1);
        deliveryCycleList.add(deliveryCycleDto2);
        dto.setDeliveryCycleList(deliveryCycleList);
        // 工单性质
        List<String> splitNatureList = new ArrayList<>();
        splitNatureList.add("urgency");
        dto.setSplitNatureList(splitNatureList);
        // 项目异常原因
        List<String> projectAnomalyList = new ArrayList<>();
        projectAnomalyList.add("Q");
        dto.setProjectAnomalyList(projectAnomalyList);
        // 流程版本号
        List<String> processVersionList = new ArrayList<>();
        processVersionList.add("301");
        dto.setProcessVersionList(processVersionList);
        // 任务中是否存在超时
        dto.setTaskTimeoutFlag("Y");

        // 商品id
        List<String> goodIdList = new ArrayList<>();
        goodIdList.add("37");
        goodIdList.add("38");
        dto.setGoodIdList(goodIdList);
        SplitStatisticsAllResponseDto result = erpStatisticsService.findUserSplitStatistics(page, dto, principalId);
        System.err.println(JSON.toJSONString(result));
    }

    /**
     * 获取团队统计数量
     *
     * @date 2018年5月15日
     * @author linqunzhi
     */
    @Test
    public void getTeamSplitReport() {
        SplitReportResponseDto result = erpStatisticsService.getTeamSplitReport("1", null);
        System.out.println(JSON.toJSONString(result));
    }

    /**
     * 获取团队分单周/月统计数据
     *
     * @date 2018年5月15日
     * @author linqunzhi
     */
    @Test
    public void getTeamSplitWeekAndMonth() {
        String principalId = "1";
        String teamId = "";
        String startDateStr = "2017-01-01";
        String endDateStr = "2018-05-05";
        SplitWeekAndMonthResponseDto result = erpStatisticsService.getTeamSplitWeekAndMonth(principalId, teamId, startDateStr, endDateStr);
        System.out.println(JSON.toJSONString(result));
    }

    /**
     * 团队成员跟进订单统计
     *
     * @date 2018年5月16日
     * @author linqunzhi
     */
    @Test
    public void findTeamMemberSplitReport() {
        String principalId = "1";
        String teamId = "";
        List<SplitTeamMemberReportResponseDto> result = erpStatisticsService.findTeamMemberSplitReport(principalId, teamId);
        System.out.println(JSON.toJSONString(result));
    }

    public static void main(String[] args) {
        String jsonStr = "{'teamId':null,'orderNum':null,'shopName':null,'splitType':null,'startDateStr':'2017-07-25 00:00:00','endDateStr':'2018-05-15 23:59:59','orderTypeList':['2'],'goodIdList':null,'planningExpertIdList':null,'splitStatusList':null,'projectAnomalyList':null,'timeoutFlag':null,'taskTimeoutFlag':null,'manualFinishFlag':null,'extensionChannelList':null,'splitNatureList':null}";
        SplitStatisticsRequestDto dto = JSON.parseObject(jsonStr, SplitStatisticsRequestDto.class);
        System.out.println(dto.getStartDateStr());
    }

}
